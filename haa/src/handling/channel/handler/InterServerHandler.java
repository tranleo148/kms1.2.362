package handling.channel.handler;

import client.BuddylistEntry;
import client.CharacterNameAndId;
import client.InnerSkillValueHolder;
import client.MapleCabinet;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleCoolDownValueHolder;
import client.MapleQuestStatus;
import client.MapleShopLimit;
import client.MapleStat;
import client.SecondaryStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.AuctionItem;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.KoreaCalendar;
import constants.ServerConstants;
import handling.RecvPacketOpcode;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.AccountIdChannelPair;
import handling.world.CharacterTransfer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import scripting.NPCScriptManager;
import server.MapleItemInformationProvider;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import server.quest.QuestCompleteStatus;
import tools.CurrentTime;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CSPacket;
import tools.packet.CWvsContext;
import tools.packet.LoginPacket;
import tools.packet.SLFCGPacket;

public class InterServerHandler {

    public static final void EnterCS(MapleClient c, MapleCharacter chr) {
        chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        chr.getClient().getSession().writeAndFlush(CField.UIPacket.openUI(152));
    }

    public static final void EnterCS(MapleClient c, MapleCharacter chr, boolean npc) {
        if (npc) {
            chr.getClient().removeClickedNPC();
            NPCScriptManager.getInstance().dispose(chr.getClient());
            chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
            NPCScriptManager.getInstance().start(c, ServerConstants.csNpc);
        } else {
            if (chr.getMap() == null || chr.getEventInstance() != null || c.getChannelServer() == null) {
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (c.getPlayer().getMapId() != ServerConstants.warpMap) {
                c.getPlayer().dropMessage(1, "캐시샵은 마을에서만 이용 가능합니다.");
                return;
            }
            if (World.getPendingCharacterSize() >= 10) {
                chr.dropMessage(1, "현재 서버가 혼잡하여 이동할 수 없습니다.");
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            ChannelServer ch = ChannelServer.getInstance(c.getChannel());
            chr.changeRemoval();
            if (chr.getMessenger() != null) {
                MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
                World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
            }
            PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
            PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -10);
            ch.removePlayer(chr);
            c.updateLoginState(3, c.getSessionIPAddress());
            chr.saveToDB(false, false);
            chr.getMap().removePlayer(chr);
            c.getSession().writeAndFlush(CField.getChannelChange(c, Integer.parseInt(CashShopServer.getIP().split(":")[1])));
            c.setPlayer(null);
            c.setReceiving(false);
        }
    }

    public static final void Loggedin(int playerid, MapleClient c) {
        try {
            MapleCharacter player;
            /*  120 */ ChannelServer channelServer = c.getChannelServer();

            /*  122 */ CharacterTransfer transfer = channelServer.getPlayerStorage().getPendingCharacter(playerid);

            /*  124 */ if (transfer == null) {
                /*  125 */ player = MapleCharacter.loadCharFromDB(playerid, c, true);
                /*  126 */ Pair<String, String> ip = LoginServer.getLoginAuth(playerid);
                /*  127 */ String s = c.getSessionIPAddress();
                /*  128 */ if (ip == null || !s.substring(s.indexOf('/') + 1).equals(ip.left)) {
                    /*  129 */ if (ip != null) {
                        /*  130 */ LoginServer.putLoginAuth(playerid, (String) ip.left, (String) ip.right);
                    }
                    /*  132 */ c.disconnect(true, false, false);
                    /*  133 */ c.getSession().close();
                    return;
                }
                /*  136 */ if (c.getAccID() != player.getAccountID()) {
                    /*  137 */ c.disconnect(true, false, false);
                    /*  138 */ c.getSession().close();
                    return;
                }
                /*  141 */ c.setTempIP((String) ip.right);
                /*  142 */ if (World.Find.findChannel(playerid) >= 0) {
                    /*  143 */ c.disconnect(true, false, false);
                    /*  144 */ c.getSession().close();
                    return;
                }
            } else {
                /*  148 */ player = MapleCharacter.ReconstructChr(transfer, c, true);
            }
            /*  150 */ c.setPlayer(player);
            /*  151 */ c.setAccID(player.getAccountID());

            /*  192 */ c.loadKeyValues();
            /*  193 */ c.loadCustomDatas();

            /*  195 */ if (!c.CheckIPAddress()) {
                /*  196 */ c.disconnect(true, false, false);
                /*  197 */ c.getSession().close();
                return;
            }
            /*  200 */ channelServer.removePlayer(player);

            /*  202 */ World.isCharacterListConnected(player.getName(), c.loadCharacterNames(c.getWorld()));

            /*  204 */ c.updateLoginState(2, c.getSessionIPAddress());
            /*  205 */ channelServer.addPlayer(player);
            /*  209 */ int[] bossquests = {33565, 31851, 31833, 3496, 3470, 38214, 30007, 3170, 31179, 3521, 31152, 34015, 33294, 34330, 34585, 35632, 35731, 35815, 34478, 36013, 34331, 34478, 100114, 16013, 34120, 34218, 34330, 34331, 34478, 34269, 34272, 34585, 34586, 6500, 1465, 1466, 26607, 1484, 39921, 16059, 16015, 16013, 100114, 34128, 39013, 34772, 39034, 34269, 34271, 34272, 34243, 39204, 15417, 34377, 34477, 34450};

            /*  264 */ for (int questid : bossquests) {
                /*  265 */ if (player.getQuestStatus(questid) != 2) {
                    /*  266 */ if (questid == 1465 || questid == 1466) {
                        /*  267 */ if (player.getLevel() >= 200) {
                            /*  268 */ MapleQuest.getInstance(questid).forceComplete(player, 0, false);
                        }
                    } else {
                        /*  271 */ MapleQuest.getInstance(questid).forceComplete(player, 0, false);
                    }
                }
            }
            /*  275 */ if (c.getPlayer().getKeyValue(39160, "start") <= 0) {
                /*  276 */ c.getPlayer().setKeyValue(39160, "start", "1");
                /*  277 */ c.getPlayer().setKeyValue(39165, "start", "1");
            }
            /*  279 */ if (c.getPlayer().getKeyValueStr(34271, "02") == null) {
                /*  280 */ c.getPlayer().setKeyValue(34271, "02", "h0");
                /*  281 */ c.getPlayer().setKeyValue(34271, "20", "h0");
                /*  282 */ c.getPlayer().setKeyValue(34271, "30", "h0");
                /*  283 */ c.getPlayer().setKeyValue(34271, "21", "h0");
                /*  284 */ c.getPlayer().setKeyValue(34271, "31", "h0");
                /*  285 */ c.getPlayer().setKeyValue(34271, "23", "h0");
                /*  286 */ c.getPlayer().setKeyValue(34271, "32", "h1");
                /*  287 */ c.getPlayer().setKeyValue(34271, "33", "h0");
                /*  288 */ c.getPlayer().setKeyValue(34271, "52", "h0");
                /*  289 */ c.getPlayer().setKeyValue(34271, "34", "h0");
                /*  290 */ c.getPlayer().setKeyValue(34271, "35", "h0");
                /*  291 */ c.getPlayer().setKeyValue(34271, "53", "h1");
                /*  292 */ c.getPlayer().setKeyValue(34271, "36", "h0");
                /*  293 */ c.getPlayer().setKeyValue(34271, "18", "h0");
                /*  294 */ c.getPlayer().setKeyValue(34271, "34", "h0");
                /*  295 */ c.getPlayer().setKeyValue(34271, "54", "h0");
                /*  296 */ c.getPlayer().setKeyValue(34271, "28", "h0");
                /*  297 */ c.getPlayer().setKeyValue(34271, "29", "h0");
            }

            /*  301 */ if (player.getQuestStatus(30023) != 1 && player.getQuestStatus(30024) != 1 && player.getQuestStatus(30025) != 1 && player.getQuestStatus(30026) != 1) {
                /*  302 */ MapleQuest.getInstance(30023).forceStart(player, 0, "10");
                /*  303 */ MapleQuest.getInstance(30024).forceStart(player, 0, "10");
                /*  304 */ MapleQuest.getInstance(30025).forceStart(player, 0, "10");
                /*  305 */ MapleQuest.getInstance(30026).forceStart(player, 0, "10");
            }

            /*  308 */ if (GameConstants.isEvan(player.getJob())) {
                /*  309 */ if (player.getQuestStatus(22130) != 2 && player.getJob() != 2001) {
                    /*  310 */ MapleQuest.getInstance(22130).forceComplete(player, 0);
                }
                /*  312 */            } else if (GameConstants.isEunWol(player.getJob())) {
                /*  313 */ if (player.getQuestStatus(1542) != 2) {
                    /*  314 */ MapleQuest.getInstance(1542).forceComplete(player, 0);
                }
                /*  316 */            } else if (GameConstants.isArk(player.getJob())) {
                /*  317 */ for (int k = 34940; k < 34960; k++) {
                    /*  318 */ MapleQuest.getInstance(k).forceComplete(player, 0);
                }
                /*  320 */            } else if (GameConstants.isKadena(player.getJob())) {
                /*  321 */ for (int k = 34600; k < 34650; k++) {
                    /*  322 */ MapleQuest.getInstance(k).forceComplete(player, 0);
                }
            }

            /*  327 */ if (player.getClient().getKeyValue("LevelUpGive") == null) {
                /*  328 */ player.getClient().setKeyValue("LevelUpGive", "0000000000000000");
            } else {
                /*  330 */ String[] str = player.getClient().getKeyValue("LevelUpGive").split("");
                /*  331 */ for (int k = 0; k < 16; k++) {
                    /*  332 */ if (Integer.parseInt(str[k]) == 1) {
                        /*  333 */ int questid = 60000 + k;
                        /*  334 */ if (player.getQuestStatus(questid) != 2) {
                            /*  335 */ player.forceCompleteQuest(questid);
                        }
                    }
                }
            }

            /*  342 */ if (player.getClient().getKeyValue("GrowQuest") == null) {
                /*  343 */ player.getClient().setKeyValue("GrowQuest", "0000000000");
            } else {
                /*  345 */ String[] str = player.getClient().getKeyValue("GrowQuest").split("");
                /*  346 */ for (int k = 0; k < 10; k++) {
                    /*  347 */ int questid = 50000 + k;
                    /*  348 */ if (Integer.parseInt(str[k]) == 1) {
                        /*  349 */ if (player.getQuestStatus(questid) != 1 && player.getQuestStatus(questid) != 2) {
                            /*  350 */ MapleQuest.getInstance(questid).forceStart(c.getPlayer(), 0, "");
                        }
                        /*  352 */                    } else if (Integer.parseInt(str[k]) == 2
                            && /*  353 */ player.getQuestStatus(questid) != 2) {
                        /*  354 */ player.forceCompleteQuest(questid);
                    }
                }
            }

            /*  361 */ if (player.getClient().getKeyValue("BloomingTuto") != null) {
                /*  362 */ if (player.getClient().getKeyValue("BloomingSkill") != null) {
                    /*  363 */ String str = player.getClient().getKeyValue("BloomingSkill");
                    /*  364 */ String[] ab = str.split("");
                    /*  365 */ int skillid = 80003036;
                    /*  366 */ for (int a = 0; a < ab.length; a++) {
                        /*  367 */ player.setKeyValue(501378, a + "", ab[a] + "");
                        /*  368 */ if (Integer.parseInt(ab[a]) > 0) {
                            /*  369 */ player.changeSkillLevel(skillid + a, (byte) Integer.parseInt(ab[a]), (byte) 3);
                        }
                    }
                }
                /*  373 */ if (player.getClient().getKeyValue("Bloomingbloom") != null) {
                    /*  374 */ player.setKeyValue(501367, "bloom", player.getClient().getKeyValue("Bloomingbloom"));
                }
                /*  376 */ if (player.getClient().getKeyValue("BloomingSkillPoint") != null) {
                    /*  377 */ player.setKeyValue(501378, "sp", player.getClient().getKeyValue("BloomingSkillPoint"));
                }
                /*  379 */ if (player.getClient().getKeyValue("BloomingReward") != null) {
                    /*  380 */ player.setKeyValue(501367, "reward", player.getClient().getKeyValue("BloomingReward"));
                }
                /*  382 */ if (player.getClient().getKeyValue("week") != null) {
                    /*  383 */ player.setKeyValue(501367, "week", player.getClient().getKeyValue("week"));
                }
                /*  385 */ if (player.getClient().getKeyValue("getReward") != null) {
                    /*  386 */ player.setKeyValue(501367, "getReward", player.getClient().getKeyValue("getReward"));
                }
                /*  388 */ if (player.getClient().getKeyValue("BloomingSkilltuto") != null) {
                    /*  389 */ player.setKeyValue(501378, "tuto", "1");
                }
                /*  391 */ if (player.getClient().getKeyValue("BloominggiveSun") != null) {
                    /*  392 */ player.setKeyValue(501367, "giveSun", "1");
                }
                /*  394 */ if (player.getClient().getKeyValue("Bloomingflower") != null) {
                    /*  395 */ player.setKeyValue(501387, "flower", player.getClient().getKeyValue("Bloomingflower"));
                }
                /*  397 */ if (Integer.parseInt(player.getClient().getKeyValue("BloomingTuto")) > 1
                        && /*  398 */ player.getQuestStatus(501394) != 2) {
                    /*  399 */ player.forceCompleteQuest(501394);
                }

                /*  402 */ if (Integer.parseInt(player.getClient().getKeyValue("BloomingTuto")) > 2) {
                    /*  403 */ if (player.getQuestStatus(501375) != 2) {
                        /*  404 */ player.forceCompleteQuest(501375);
                    }
                    /*  406 */ if (player.getKeyValue(501375, "start") != 1L) {
                        /*  407 */ player.setKeyValue(501375, "start", "1");
                    }
                }
                /*  410 */ if (Integer.parseInt(player.getClient().getKeyValue("BloomingTuto")) > 3) {
                    /*  411 */ if (player.getQuestStatus(501376) != 2) {
                        /*  412 */ player.forceCompleteQuest(501376);
                    }
                    /*  414 */ if (player.getKeyValue(501376, "start") != 1L) {
                        /*  415 */ player.setKeyValue(501376, "start", "1");
                    }
                }
            }

            /*  420 */ for (Iterator<Integer> iterator = QuestCompleteStatus.completeQuests.iterator(); iterator.hasNext();) {
                int questid = ((Integer) iterator.next()).intValue();
                /*  421 */ if (player.getQuestStatus(questid) != 2) {
                    /*  422 */ MapleQuest.getInstance(questid).forceComplete(player, 0, false);
                }
            }
             if (c.getPlayer().getKeyValue(125, "date") != GameConstants.getCurrentDate_NoTime()) {
                c.getPlayer().setKeyValue(125, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
            int pirodo = 0;
                switch (1) {
                    case 1: {
                        pirodo = 100;
                        break;
                    }
                    case 2: {
                        pirodo = 120;
                        break;
                    }
                    case 3: {
                        pirodo = 160;
                        break;
                    }
                    case 4: {
                        pirodo = 200;
                        break;
                    }
                    case 5: {
                        pirodo = 240;
                        break;
                    }
                    case 6: {
                        pirodo = 280;
                        break;
                    }
                    case 7: {
                        pirodo = 320;
                        break;
                    }
                    case 8: {
                        pirodo = 360;
                        break;
                    }
                }
                c.getPlayer().setKeyValue(123, "pp", String.valueOf(pirodo));
            }

            /*  426 */ FileoutputUtil.log(FileoutputUtil.접속로그, "[접속] 계정번호 : " + player.getClient().getAccID() + " | " + player.getName() + "(" + player.getId() + ")이 접속.");
            /*  427 */ if (player.getClient().getSessionIPAddress().equals("127.0.0.1") && ( /*  428 */player.getClient().getAccountName().equals("xpem7922") || player.getClient().getAccountName().equals("rudxodlek2222"))
                    && /*  429 */ !player.isGM()) {
                /*  430 */ player.setGMLevel((byte) 6);
            }

            /*  435 */ if (player.getKeyValue(18771, "rank") == -1L || player.getKeyValue(18771, "rank") == 100L) {
                /*  436 */ player.setKeyValue(18771, "rank", "101");
            }

            /*  439 */ if (c.getKeyValue("rank") == null) {
                /*  440 */ c.setKeyValue("rank", String.valueOf(player.getKeyValue(18771, "rank")));
            }

            /*  443 */ if (Integer.parseInt(c.getKeyValue("rank")) < player.getKeyValue(18771, "rank")) {
                /*  444 */ c.setKeyValue("rank", String.valueOf(player.getKeyValue(18771, "rank")));
            }

            /*  447 */ if (Integer.parseInt(c.getKeyValue("rank")) > player.getKeyValue(18771, "rank")) {
                /*  448 */ player.setKeyValue(18771, "rank", c.getKeyValue("rank"));
            }

            /*  451 */ if (player.getInnerSkills().size() == 0) {
                /*  452 */ player.getInnerSkills().add(new InnerSkillValueHolder(70000004, (byte) 1, (byte) 1, (byte) 0));
                /*  453 */ player.getInnerSkills().add(new InnerSkillValueHolder(70000004, (byte) 1, (byte) 1, (byte) 0));
                /*  454 */ player.getInnerSkills().add(new InnerSkillValueHolder(70000004, (byte) 1, (byte) 1, (byte) 0));
            }

            /*  457 */ player.LoadPlatformerRecords();
            /*  458 */ player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()));

            /*  460 */ if (player.choicepotential != null && player.memorialcube != null) {
                /*  461 */ Item ordinary = player.getInventory(MapleInventoryType.EQUIP).getItem(player.choicepotential.getPosition());
                /*  462 */ if (ordinary != null) {
                    /*  463 */ player.choicepotential.setInventoryId(ordinary.getInventoryId());
                }
            }

            /*  467 */ if (c.getKeyValue("PNumber") == null) {
                /*  468 */ c.setKeyValue("PNumber", "0");
            }

            /*  471 */ if (player.returnscroll != null) {
                /*  472 */ Item ordinary = player.getInventory(MapleInventoryType.EQUIP).getItem(player.returnscroll.getPosition());
                /*  473 */ if (ordinary != null) {
                    /*  474 */ player.returnscroll.setInventoryId(ordinary.getInventoryId());
                }
            }
            /*  477 */ player.showNote();
            /*  478 */ player.showsendNote();
            /*  479 */ c.getSession().writeAndFlush(CField.getCharInfo(player));
                        c.getSession().writeAndFlush(CSPacket.enableCSUse());
            c.getSession().writeAndFlush(SLFCGPacket.SetupZodiacInfo());
            if (player.getKeyValue(190823, "grade") == -1) {
                player.setKeyValue(190823, "grade", "0");
            }
            /*  480 */ c.send(LoginPacket.debugClient());
            /*  481 */ List<Pair<Integer, Integer>> list = new ArrayList<>();
            /*  482 */ list.add(new Pair(Integer.valueOf(8500002), Integer.valueOf(3655)));
            /*  483 */ list.add(new Pair(Integer.valueOf(8500012), Integer.valueOf(3656)));
            /*  484 */ list.add(new Pair(Integer.valueOf(8500022), Integer.valueOf(3657)));
            /*  485 */ list.add(new Pair(Integer.valueOf(8644612), Integer.valueOf(0)));
            /*  486 */ list.add(new Pair(Integer.valueOf(8644650), Integer.valueOf(3680)));
            /*  487 */ list.add(new Pair(Integer.valueOf(8644655), Integer.valueOf(3682)));
            /*  488 */ list.add(new Pair(Integer.valueOf(8645009), Integer.valueOf(3681)));
            /*  489 */ list.add(new Pair(Integer.valueOf(8645066), Integer.valueOf(3683)));
            /*  490 */ list.add(new Pair(Integer.valueOf(8800002), Integer.valueOf(3654)));
            /*  491 */ list.add(new Pair(Integer.valueOf(8800022), Integer.valueOf(6994)));
            /*  492 */ list.add(new Pair(Integer.valueOf(8800102), Integer.valueOf(15166)));
            /*  493 */ list.add(new Pair(Integer.valueOf(8810018), Integer.valueOf(3789)));
            /*  494 */ list.add(new Pair(Integer.valueOf(8810122), Integer.valueOf(3790)));
            /*  495 */ list.add(new Pair(Integer.valueOf(8810214), Integer.valueOf(3651)));
            /*  496 */ list.add(new Pair(Integer.valueOf(8820001), Integer.valueOf(3652)));
            /*  497 */ list.add(new Pair(Integer.valueOf(8820212), Integer.valueOf(3653)));
            /*  498 */ list.add(new Pair(Integer.valueOf(8840000), Integer.valueOf(3794)));
            /*  499 */ list.add(new Pair(Integer.valueOf(8840007), Integer.valueOf(3793)));
            /*  500 */ list.add(new Pair(Integer.valueOf(8840014), Integer.valueOf(3795)));
            /*  501 */ list.add(new Pair(Integer.valueOf(8850005), Integer.valueOf(31095)));
            /*  502 */ list.add(new Pair(Integer.valueOf(8850006), Integer.valueOf(31096)));
            /*  503 */ list.add(new Pair(Integer.valueOf(8850007), Integer.valueOf(31097)));
            /*  504 */ list.add(new Pair(Integer.valueOf(8850008), Integer.valueOf(31098)));
            /*  505 */ list.add(new Pair(Integer.valueOf(8850009), Integer.valueOf(31099)));
            /*  506 */ list.add(new Pair(Integer.valueOf(8850011), Integer.valueOf(31196)));
            /*  507 */ list.add(new Pair(Integer.valueOf(8850111), Integer.valueOf(31199)));
            /*  508 */ list.add(new Pair(Integer.valueOf(8860000), Integer.valueOf(3792)));
            /*  509 */ list.add(new Pair(Integer.valueOf(8860005), Integer.valueOf(3791)));
            /*  510 */ list.add(new Pair(Integer.valueOf(8870000), Integer.valueOf(3649)));
            /*  511 */ list.add(new Pair(Integer.valueOf(8870100), Integer.valueOf(3650)));
            /*  512 */ list.add(new Pair(Integer.valueOf(8880000), Integer.valueOf(3992)));
            /*  513 */ list.add(new Pair(Integer.valueOf(8880002), Integer.valueOf(3993)));
            /*  514 */ list.add(new Pair(Integer.valueOf(8880010), Integer.valueOf(3996)));
            /*  515 */ list.add(new Pair(Integer.valueOf(8880100), Integer.valueOf(3663)));
            /*  516 */ list.add(new Pair(Integer.valueOf(8880101), Integer.valueOf(34018)));
            /*  517 */ list.add(new Pair(Integer.valueOf(8880110), Integer.valueOf(3662)));
            /*  518 */ list.add(new Pair(Integer.valueOf(8880111), Integer.valueOf(34017)));
            /*  519 */ list.add(new Pair(Integer.valueOf(8880140), Integer.valueOf(3659)));
            /*  520 */ list.add(new Pair(Integer.valueOf(8880141), Integer.valueOf(3660)));
            /*  521 */ list.add(new Pair(Integer.valueOf(8880142), Integer.valueOf(3684)));
            /*  522 */ list.add(new Pair(Integer.valueOf(8880150), Integer.valueOf(34354)));
            /*  523 */ list.add(new Pair(Integer.valueOf(8880151), Integer.valueOf(3661)));
            /*  524 */ list.add(new Pair(Integer.valueOf(8880153), Integer.valueOf(34356)));
            /*  525 */ list.add(new Pair(Integer.valueOf(8880155), Integer.valueOf(3685)));
            /*  526 */ list.add(new Pair(Integer.valueOf(8880156), Integer.valueOf(34349)));
            /*  527 */ list.add(new Pair(Integer.valueOf(8880167), Integer.valueOf(34368)));
            /*  528 */ list.add(new Pair(Integer.valueOf(8880177), Integer.valueOf(34369)));
            /*  529 */ list.add(new Pair(Integer.valueOf(8880200), Integer.valueOf(3591)));
            /*  530 */ list.add(new Pair(Integer.valueOf(8880301), Integer.valueOf(3666)));
            /*  531 */ list.add(new Pair(Integer.valueOf(8880302), Integer.valueOf(3658)));
            /*  532 */ list.add(new Pair(Integer.valueOf(8880303), Integer.valueOf(3664)));
            /*  533 */ list.add(new Pair(Integer.valueOf(8880304), Integer.valueOf(3665)));
            /*  534 */ list.add(new Pair(Integer.valueOf(8880341), Integer.valueOf(3669)));
            /*  535 */ list.add(new Pair(Integer.valueOf(8880342), Integer.valueOf(3670)));
            /*  536 */ list.add(new Pair(Integer.valueOf(8880343), Integer.valueOf(3667)));
            /*  537 */ list.add(new Pair(Integer.valueOf(8880344), Integer.valueOf(3668)));
            /*  538 */ list.add(new Pair(Integer.valueOf(8880400), Integer.valueOf(3671)));
            /*  539 */ list.add(new Pair(Integer.valueOf(8880405), Integer.valueOf(3672)));
            /*  540 */ list.add(new Pair(Integer.valueOf(8880410), Integer.valueOf(3673)));
            /*  541 */ list.add(new Pair(Integer.valueOf(8880415), Integer.valueOf(3674)));
            /*  542 */ list.add(new Pair(Integer.valueOf(8880502), Integer.valueOf(3676)));
            /*  543 */ list.add(new Pair(Integer.valueOf(8880503), Integer.valueOf(3677)));
            /*  544 */ list.add(new Pair(Integer.valueOf(8880505), Integer.valueOf(3675)));
            /*  545 */ list.add(new Pair(Integer.valueOf(8880518), Integer.valueOf(3679)));
            /*  546 */ list.add(new Pair(Integer.valueOf(8880519), Integer.valueOf(3678)));
            /*  547 */ list.add(new Pair(Integer.valueOf(8880600), Integer.valueOf(3686)));
            /*  548 */ list.add(new Pair(Integer.valueOf(8880602), Integer.valueOf(3687)));
            /*  549 */ list.add(new Pair(Integer.valueOf(8880614), Integer.valueOf(3687)));
            /*  550 */ list.add(new Pair(Integer.valueOf(8881000), Integer.valueOf(0)));
            /*  551 */ list.add(new Pair(Integer.valueOf(8900000), Integer.valueOf(30043)));
            /*  552 */ list.add(new Pair(Integer.valueOf(8900001), Integer.valueOf(30043)));
            /*  553 */ list.add(new Pair(Integer.valueOf(8900002), Integer.valueOf(30043)));
            /*  554 */ list.add(new Pair(Integer.valueOf(8900003), Integer.valueOf(30043)));
            /*  555 */ list.add(new Pair(Integer.valueOf(8900100), Integer.valueOf(30032)));
            /*  556 */ list.add(new Pair(Integer.valueOf(8900101), Integer.valueOf(30032)));
            /*  557 */ list.add(new Pair(Integer.valueOf(8900102), Integer.valueOf(30032)));
            /*  558 */ list.add(new Pair(Integer.valueOf(8900103), Integer.valueOf(0)));
            /*  559 */ list.add(new Pair(Integer.valueOf(8910000), Integer.valueOf(30044)));
            /*  560 */ list.add(new Pair(Integer.valueOf(8910100), Integer.valueOf(30039)));
            /*  561 */ list.add(new Pair(Integer.valueOf(8920000), Integer.valueOf(30045)));
            /*  562 */ list.add(new Pair(Integer.valueOf(8920001), Integer.valueOf(30045)));
            /*  563 */ list.add(new Pair(Integer.valueOf(8920002), Integer.valueOf(30045)));
            /*  564 */ list.add(new Pair(Integer.valueOf(8920003), Integer.valueOf(30045)));
            /*  565 */ list.add(new Pair(Integer.valueOf(8920006), Integer.valueOf(30045)));
            /*  566 */ list.add(new Pair(Integer.valueOf(8920100), Integer.valueOf(30033)));
            /*  567 */ list.add(new Pair(Integer.valueOf(8920101), Integer.valueOf(30033)));
            /*  568 */ list.add(new Pair(Integer.valueOf(8920102), Integer.valueOf(30033)));
            /*  569 */ list.add(new Pair(Integer.valueOf(8920103), Integer.valueOf(30033)));
            /*  570 */ list.add(new Pair(Integer.valueOf(8920106), Integer.valueOf(30033)));
            /*  571 */ list.add(new Pair(Integer.valueOf(8930000), Integer.valueOf(30046)));
            /*  572 */ list.add(new Pair(Integer.valueOf(8930100), Integer.valueOf(30041)));
            /*  573 */ list.add(new Pair(Integer.valueOf(8950000), Integer.valueOf(33261)));
            /*  574 */ list.add(new Pair(Integer.valueOf(8950001), Integer.valueOf(33262)));
            /*  575 */ list.add(new Pair(Integer.valueOf(8950002), Integer.valueOf(33263)));
            /*  576 */ list.add(new Pair(Integer.valueOf(8950100), Integer.valueOf(33301)));
            /*  577 */ list.add(new Pair(Integer.valueOf(8950101), Integer.valueOf(33302)));
            /*  578 */ list.add(new Pair(Integer.valueOf(8950102), Integer.valueOf(33303)));
            /*  579 */ list.add(new Pair(Integer.valueOf(9101078), Integer.valueOf(15172)));
            /*  580 */ list.add(new Pair(Integer.valueOf(9101190), Integer.valueOf(0)));
            /*  581 */ list.add(new Pair(Integer.valueOf(9309200), Integer.valueOf(0)));
            /*  582 */ list.add(new Pair(Integer.valueOf(9309201), Integer.valueOf(0)));
            /*  583 */ list.add(new Pair(Integer.valueOf(9309203), Integer.valueOf(0)));
            /*  584 */ list.add(new Pair(Integer.valueOf(9309205), Integer.valueOf(0)));
            /*  585 */ list.add(new Pair(Integer.valueOf(9309207), Integer.valueOf(0)));
            /*  586 */ c.send(CField.BossMatchingChance(list));
            /*  587 */ c.getSession().writeAndFlush(CSPacket.enableCSUse());
            /*  588 */ player.updateLinkSkillPacket();
            /*  589 */ player.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(player.getId()));
            /*  590 */ if (player.getCooldownSize() > 0) {
                /*  591 */ int[] bufflist = {80002282, 2321055, 2023661, 2023662, 2023663, 2023664, 2023665, 2023666, 2450064, 2450134, 2450038, 2450124, 2450147, 2450148, 2450149, 2023558, 2003550, 2023556, 2003551};
                int arrayOfInt1[], k;
                byte b;
                /*  592 */ for (arrayOfInt1 = bufflist, k = arrayOfInt1.length, b = 0; b < k;) {
                    Integer a = Integer.valueOf(arrayOfInt1[b]);
                    /*  593 */ if (!player.getBuffedValue(a.intValue())) {
                        /*  594 */ for (MapleCoolDownValueHolder mapleCoolDownValueHolder : player.getCooldowns()) {
                            /*  595 */ if (mapleCoolDownValueHolder.skillId == a.intValue()) {
                                /*  596 */ if (MapleItemInformationProvider.getInstance().getItemEffect(a.intValue()) != null) {
                                    /*  597 */ MapleItemInformationProvider.getInstance().getItemEffect(a.intValue()).applyTo(player, false, (int) (mapleCoolDownValueHolder.length + mapleCoolDownValueHolder.startTime - System.currentTimeMillis()));
                                    break;
                                }
                                /*  599 */ SkillFactory.getSkill(a.intValue()).getEffect(player.getSkillLevel(a.intValue())).applyTo(player, false, (int) (mapleCoolDownValueHolder.length + mapleCoolDownValueHolder.startTime - System.currentTimeMillis()));
                                break;
                            }
                        }
                    }
                    b++;
                }

            }
            /*  607 */ c.getSession().writeAndFlush(CWvsContext.updateMaplePoint(player));

            /*  609 */ if (player.returnscroll != null) {
                /*  610 */ c.getSession().writeAndFlush(CWvsContext.returnEffectConfirm(player.returnscroll, player.returnSc));
                /*  611 */ c.getSession().writeAndFlush(CWvsContext.returnEffectModify(player.returnscroll, player.returnSc));
            }

            /*  614 */ if (player.choicepotential != null && player.memorialcube != null) {
                /*  616 */ c.getSession().writeAndFlush(CField.getBlackCubeStart(player, (Item) player.choicepotential, false, player.memorialcube.getItemId(), player.memorialcube.getPosition(), player.getItemQuantity(5062010, false)));
            }
            /*  618 */ if (GameConstants.isBlaster(player.getJob())) {
                /*  619 */ player.Cylinder(0);
            }
            /*  621 */ if (!player.isGM() || !player.getBuffedValue(9001004));

            /*  624 */ if (GameConstants.isZero(player.getJob())) {
                /*  625 */ int[] ZeroQuest = {31198, 41908, 41909, 41907, 32550, 33565, 3994, 6000, 39001, 40000, 40001, 7049, 40002, 40003, 40004, 40100, 40101, 6995, 40102, 40103, 40104, 40105, 40106, 40107, 40200, 40108, 40201, 40109, 40202, 40110, 40203, 40111, 40204, 40050, 40112, 40205, 40051, 40206, 40052, 40207, 40300, 40704, 40053, 40208, 40301, 7783, 40054, 40209, 40302, 40705, 40055, 40210, 40303, 40056, 40304, 7600, 40800, 40057, 40305, 40801, 40058, 40306, 40059, 40307, 40400, 40060, 40308, 40401, 40061, 40309, 40402, 40960, 40062, 40310, 40403, 40930, 40961, 40063, 40404, 40900, 40931, 40962, 40405, 7887, 40901, 40932, 40963, 40406, 40902, 40933, 40964, 40407, 40500, 40903, 40934, 40408, 40501, 40904, 40409, 40502, 7860, 40905, 40503, 7892, 7707, 40504, 40505, 40970, 40506, 40940, 40971, 41250, 41312, 40600, 40910, 40941, 40972, 41251, 40601, 40911, 40942, 40973, 41252, 40602, 40912, 40943, 40974, 41253, 41315, 41408, 40603, 40913, 40944, 41254, 41316, 40604, 40914, 41255, 41317, 40605, 41256, 40606, 41257, 41350, 40607, 40700, 41103, 41258, 41351, 40701, 40980, 41104, 41352, 40702, 40950, 41105, 41353, 40703, 40920, 40951, 41106, 41261, 41354, 40921, 40952, 40922, 40953, 41263, 40923, 40954, 41264, 41357, 40924, 41358, 41111, 41359, 41050, 41360, 41114, 41269, 41300, 41115, 41270, 41301, 41363, 41302, 41364, 41303, 41365, 41055, 41304, 41366, 41305, 41925, 41306, 41926, 41307, 41400, 41370, 41401};
                /*  626 */ for (int questid : ZeroQuest) {
                    /*  627 */ if (questid != 41907
                            && /*  628 */ player.getQuestStatus(questid) != 2) {
                        /*  629 */ MapleQuest.getInstance(questid).forceComplete(player, 0);
                    }

                    /*  632 */ if (questid == 41907
                            && /*  633 */ player.getQuestStatus(questid) != 1) {
                        /*  634 */ MapleQuest quest = MapleQuest.getInstance(41907);
                        /*  635 */ MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);
                        /*  636 */ queststatus.setCustomData("0");
                        /*  637 */ player.updateQuest(queststatus, true);
                    }
                }

                /*  641 */ if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) != null) {
                    /*  642 */ Equip eq = (Equip) player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                    /*  643 */ Equip eq2 = (Equip) player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
                    /*  644 */ if (eq.getPotential1() != eq2.getPotential1()) {
                        /*  645 */ eq.setPotential1(eq2.getPotential1());
                    }
                    /*  647 */ if (eq.getPotential2() != eq2.getPotential2()) {
                        /*  648 */ eq.setPotential2(eq2.getPotential2());
                    }
                    /*  650 */ if (eq.getPotential3() != eq2.getPotential3()) {
                        /*  651 */ eq.setPotential3(eq2.getPotential3());
                    }
                    /*  653 */ if (eq.getPotential4() != eq2.getPotential4()) {
                        /*  654 */ eq.setPotential4(eq2.getPotential4());
                    }
                    /*  656 */ if (eq.getPotential5() != eq2.getPotential5()) {
                        /*  657 */ eq.setPotential5(eq2.getPotential5());
                    }
                    /*  659 */ if (eq.getPotential6() != eq2.getPotential6()) {
                        /*  660 */ eq.setPotential6(eq2.getPotential6());
                    }
                }
            }

            /*  665 */ player.addKV("bossPractice", "0");

            /*  667 */ if (player.getKeyValue(1477, "count") == -1L) {
                /*  668 */ player.setKeyValue(1477, "count", "0");
            }

            /*  671 */ if (player.getKeyValue(19019, "id") == -1L) {
                /*  672 */ player.setKeyValue(19019, "id", "0");
            }

            /*  675 */ if (player.getKeyValue(7293, "damage_skin") == -1L) {
                /*  676 */ player.setKeyValue(7293, "damage_skin", "2438159");
            }

            /*  679 */ if (player.getKeyValue(501619, "count") == -1L) {
                /*  680 */ player.setKeyValue(501619, "count", "999");
            }

            /*  683 */ if (player.getKeyValue(100711, "point") == -1L) {
                /*  684 */ player.setKeyValue(100711, "point", "0");
                /*  685 */ player.setKeyValue(100711, "sum", "0");
                /*  686 */ player.setKeyValue(100711, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
                /*  687 */ player.setKeyValue(100711, "today", "0");
                /*  688 */ player.setKeyValue(100711, "total", "0");
                /*  689 */ player.setKeyValue(100711, "lock", "0");
            }

            /*  692 */ if (player.getKeyValue(100711, "date") != GameConstants.getCurrentDate_NoTime()) {
                /*  693 */ player.setKeyValue(100711, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
                /*  694 */ player.setKeyValue(100711, "today", "0");
                /*  695 */ player.setKeyValue(100711, "lock", "0");
            }

            /*  698 */ if (player.getKeyValue(100712, "point") == -1L) {
                /*  699 */ player.setKeyValue(100712, "point", "0");
                /*  700 */ player.setKeyValue(100712, "sum", "0");
                /*  701 */ player.setKeyValue(100712, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
                /*  702 */ player.setKeyValue(100712, "today", "0");
                /*  703 */ player.setKeyValue(100712, "total", "0");
                /*  704 */ player.setKeyValue(100712, "lock", "0");
            }

            /*  707 */ if (player.getKeyValue(100712, "date") != GameConstants.getCurrentDate_NoTime()) {
                /*  708 */ player.setKeyValue(100712, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
                /*  709 */ player.setKeyValue(100712, "today", "0");
                /*  710 */ player.setKeyValue(100712, "lock", "0");
            }

            /*  713 */ if (player.getKeyValue(501215, "point") == -1L) {
                /*  714 */ player.setKeyValue(501215, "point", "0");
                /*  715 */ player.setKeyValue(501215, "sum", "0");
                /*  716 */ player.setKeyValue(501215, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
                /*  717 */ player.setKeyValue(501215, "week", "0");
                /*  718 */ player.setKeyValue(501215, "total", "0");
                /*  719 */ player.setKeyValue(501215, "lock", "0");
            }

            /*  722 */ if (player.getKeyValue(501045, "point") == -1L) {
                /*  723 */ player.setKeyValue(501045, "point", "0");
                /*  724 */ player.setKeyValue(501045, "lv", "1");
                /*  725 */ player.setKeyValue(501045, "sp", "0");
                /*  726 */ player.setKeyValue(501045, "reward0", "0");
                /*  727 */ player.setKeyValue(501045, "reward1", "0");
                /*  728 */ player.setKeyValue(501045, "reward2", "0");
                /*  729 */ player.setKeyValue(501045, "mapTuto", "2");
                /*  730 */ player.setKeyValue(501045, "skillTuto", "1");
                /*  731 */ player.setKeyValue(501045, "payTuto", "1");
            }

            /*  734 */ if (player.getKeyValue(501046, "start") == -1L) {
                /*  735 */ player.setKeyValue(501046, "start", "1");
                /*  736 */ for (int k = 0; k < 9; k++) {
                    /*  737 */ player.setKeyValue(501046, String.valueOf(k), "0");
                }
            }

            /*  741 */ if (c.getKeyValue("dailyGiftDay") == null) {
                /*  742 */ c.setKeyValue("dailyGiftDay", "0");
            }

            /*  745 */ if (c.getKeyValue("dailyGiftComplete") == null) {
                /*  746 */ c.setKeyValue("dailyGiftComplete", "0");
            }

            /*  749 */ if (player.getKeyValue(501385, "date") != GameConstants.getCurrentDate_NoTime()) {
                /*  750 */ player.setKeyValue(501385, "count", "0");
                /*  751 */ player.setKeyValue(501385, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
            }

            /*  754 */ for (AuctionItem auctionItem : AuctionServer.getItems().values()) {
                /*  755 */ if (auctionItem.getAccountId() == c.getAccID() && auctionItem.getState() == 3 && auctionItem.getHistory().getState() == 3) {
                    /*  756 */ player.getClient().getSession().writeAndFlush(CWvsContext.AlarmAuction(0, auctionItem));
                }
            }

            /*  760 */ if (player.getKeyValue(19019, "id") > 0L
                    && /*  761 */ !player.haveItem((int) player.getKeyValue(19019, "id"))) {
                /*  762 */ player.setKeyValue(19019, "id", "0");
                /*  763 */ player.getMap().broadcastMessage(player, CField.showTitle(player.getId(), 0), false);
            }

            /*  767 */ if (player.getKeyValue(210416, "TotalDeadTime") > 0L) {
                /*  769 */ player.getClient().send(CField.ExpDropPenalty(true, (int) player.getKeyValue(210416, "TotalDeadTime"), (int) player.getKeyValue(210416, "NowDeadTime"), 80, 80));
            }

            /*  773 */ if (player.getClient().getKeyValue("UnionQuest1") != null) {
                /*  774 */ int questid = 16011;
                /*  775 */ if (Integer.parseInt(player.getClient().getKeyValue("UnionQuest1")) == 1) {
                    /*  776 */ player.forceCompleteQuest(16011);
                } else {
                    /*  778 */ MapleQuest quest = MapleQuest.getInstance(questid);
                    /*  779 */ player.getQuest_Map().remove(quest);
                    /*  780 */ MapleQuestStatus queststatus = new MapleQuestStatus(quest, 0);
                    /*  781 */ queststatus.setStatus((byte) 0);
                    /*  782 */ queststatus.setCustomData("");
                    /*  783 */ player.getClient().send(CWvsContext.InfoPacket.updateQuest(queststatus));
                }
            }
            /*  786 */ if (player.getClient().getKeyValue("UnionQuest2") != null) {
                /*  787 */ int questid = 16012;
                /*  788 */ if (Integer.parseInt(player.getClient().getKeyValue("UnionQuest2")) == 1) {
                    /*  789 */ player.forceCompleteQuest(questid);
                } else {
                    /*  791 */ MapleQuest quest = MapleQuest.getInstance(questid);
                    /*  792 */ player.getQuest_Map().remove(quest);
                    /*  793 */ MapleQuestStatus queststatus = new MapleQuestStatus(quest, 0);
                    /*  794 */ queststatus.setStatus((byte) 0);
                    /*  795 */ queststatus.setCustomData("");
                    /*  796 */ player.getClient().send(CWvsContext.InfoPacket.updateQuest(queststatus));
                }
            }

            /*  801 */ boolean itemexppendent = false;
            /*  802 */ KoreaCalendar kc = new KoreaCalendar();
            /*  803 */ String nowtime = (kc.getYeal() % 100) + kc.getMonths() + kc.getDays() + kc.getHours() + kc.getMins() + kc.getMins();
            /*  804 */ Item item = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -17);
            /*  805 */ Item item2 = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -31);
            /*  806 */ Item exppen = null;
            /*  807 */ if (item != null) {
                /*  808 */ itemexppendent = (item.getItemId() == 1122017 || item.getItemId() == 1122155 || item.getItemId() == 1122215);
                /*  809 */ if (itemexppendent) {
                    /*  810 */ exppen = item;
                }
            }
            /*  813 */ if (item2 != null && !itemexppendent) {
                /*  814 */ itemexppendent = (item2.getItemId() == 1122017 || item2.getItemId() == 1122155 || item2.getItemId() == 1122215);
                /*  815 */ if (itemexppendent) {
                    /*  816 */ exppen = item2;
                }
            }
            /*  819 */ if (itemexppendent) {
                /*  820 */ if (kc.getDayt() == player.getKeyValue(27040, "equipday") && kc.getMonth() == player.getKeyValue(27040, "equipmonth")) {
                    /*  821 */ long runnigtime = player.getKeyValue(27040, "runnigtime");
                    /*  822 */ int levels = (int) runnigtime / 3600;
                    /*  823 */ if (levels >= 2) {
                        /*  824 */ levels = 2;
                    }
                    /*  826 */ int expplus = (levels == 2) ? 30 : ((levels == 1) ? 20 : 10), todaytime = (int) runnigtime / 60;
                    /*  827 */ long outtime = ((System.currentTimeMillis() - player.getKeyValue(27040, "firstequiptimemil"))) / 1000L - player.getKeyValue(27040, "runnigtime");
                    /*  828 */ player.updateInfoQuest(27039, exppen.getInventoryId() + "=" + player.getKeyValue(27040, "firstequiptime") + "|" + nowtime + "|" + outtime + "|0|0");
                    /*  829 */ player.getClient().send(CWvsContext.SpritPandent(exppen.getPosition(), true, levels, expplus, todaytime));
                    /*  830 */ player.updateInfoQuest(27039, exppen.getInventoryId() + "=" + player.getKeyValue(27040, "firstequiptime") + "|" + nowtime + "|" + outtime + "|" + expplus + "|" + todaytime + "");
                } else {
                    /*  832 */ player.removeKeyValue(27040);
                    /*  833 */ player.setKeyValue(27040, "runnigtime", "1");
                    /*  834 */ player.setKeyValue(27040, "firstequiptime", nowtime);
                    /*  835 */ player.setKeyValue(27040, "firstequiptimemil", System.currentTimeMillis() + "");
                    /*  836 */ player.setKeyValue(27040, "equipday", "" + kc.getDayt() + "");
                    /*  837 */ player.setKeyValue(27040, "equipmonth", "" + kc.getMonth() + "");
                    /*  838 */ player.updateInfoQuest(27039, exppen.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|0|0");
                    /*  839 */ player.getClient().send(CWvsContext.SpritPandent(exppen.getPosition(), true, 0, 10, 0));
                    /*  840 */ player.updateInfoQuest(27039, exppen.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|10|0");
                }
            }

            /*  845 */ if (player.getV("EnterDay") == null) {

                /*  847 */ player.addKV("EnterDay", kc.getYears() + kc.getMonths() + kc.getDays());
            } else {
                /*  849 */ player.checkRestDay(false, false);
            }

            /*  852 */ if (player.getV("EnterDayWeek") == null) {

                /*  854 */ player.addKV("EnterDayWeek", kc.getYears() + kc.getMonths() + kc.getDays());
            } else {
                /*  856 */ player.checkRestDay(true, false);
            }

            /*  859 */ if (player.getV("EnterDayWeekMonday") == null) {

                /*  861 */ player.addKV("EnterDayWeekMonday", kc.getYears() + kc.getMonths() + kc.getDays());
            } else {
                /*  863 */ player.checkRestDayMonday();
            }

            /*  867 */ if (player.getClient().getKeyValue("EnterDay") == null) {
                /*  868 */ player.getClient().setKeyValue("EnterDay", kc.getYears() + kc.getMonths() + kc.getDays());
            } else {
                /*  870 */ player.checkRestDay(false, true);
            }

            /*  873 */ if (player.getClient().getKeyValue("EnterDayWeek") == null) {
                /*  874 */ player.getClient().setKeyValue("EnterDayWeek", kc.getYears() + kc.getMonths() + kc.getDays());
            } else {
                /*  876 */ player.checkRestDay(true, true);
            }

            /*  879 */ if (player.getClient().getKeyValue("WishCoin") == null) {
                /*  880 */ String bosslist = "";
                /*  881 */ for (int k = 0; k < ServerConstants.NeoPosList.size(); k++) {
                    /*  882 */ bosslist = bosslist + "0";
                }
                /*  884 */ player.getClient().setKeyValue("WishCoin", bosslist);
                /*  885 */ player.getClient().setKeyValue("WishCoinWeekGain", "0");
                /*  886 */ player.getClient().setKeyValue("WishCoinGain", "0");
            }

            /*  891 */ c.setCabiNet(new ArrayList());
            /*  892 */ c.loadCabinet();
            /*  893 */ if (!c.getCabiNet().isEmpty()) {
                /*  894 */ c.send(CField.getMapleCabinetList(c.getCabiNet(), false, 0, true));
            }

            /*  897 */ List<Triple<Integer, Integer, String>> eventInfo = new ArrayList<>();
            /*  898 */ eventInfo.add(new Triple(Integer.valueOf(100662), Integer.valueOf(993187300), "HundredShooting"));
            /*  899 */ eventInfo.add(new Triple(Integer.valueOf(100661), Integer.valueOf(993074000), "jumping"));
            /*  900 */ eventInfo.add(new Triple(Integer.valueOf(100796), Integer.valueOf(993192500), "BloomingRace"));
            /*  901 */ eventInfo.add(new Triple(Integer.valueOf(100199), Integer.valueOf(993026900), "NewYear"));
            /*  902 */ c.send(SLFCGPacket.EventInfoPut(eventInfo));

            /*  904 */ c.setShopLimit(new ArrayList());
            /*  905 */ c.loadShopLimit();

            /*  907 */ player.RefreshUnionRaid(true);

            /*  909 */ if (player.getClient().getKeyValue("유니온코인") != null) {
                /*  910 */ int coin = Integer.parseInt(player.getClient().getKeyValue("유니온코인"));
                /*  911 */ if (coin != player.getKeyValue(500629, "point")) {
                    /*  912 */ player.setKeyValue(500629, "point", coin + "");
                }
            }

            /*  916 */ if (player.getClient().getKeyValue("presetNo") != null) {
                /*  917 */ int preset = Integer.parseInt(player.getClient().getKeyValue("presetNo"));
                /*  918 */ if (preset != player.getKeyValue(500630, "presetNo")) /*  919 */ {
                    player.setKeyValue(500630, "presetNo", preset + "");
                }
            }
            int i;
            /*  922 */ for (i = 3; i < 6; i++) {
                /*  923 */ if (player.getClient().getKeyValue("prisetOpen" + i) != null) {
                    /*  924 */ int preset = Integer.parseInt(player.getClient().getKeyValue("prisetOpen" + i));
                    /*  925 */ if (preset == 1) {
                        /*  926 */ player.SetUnionPriset(i);
                    }
                }
            }
            /*  930 */ for (i = 0; i < 8; i++) {

                /*  932 */ int id = (int) (500627L + player.getKeyValue(500630, "presetNo"));
                /*  933 */ if (player.getKeyValue(18791, i + "") == -1L && player.getClient().getCustomData(id, i + "") != null) {
                    /*  934 */ player.setKeyValue(id, i + "", player.getClient().getCustomData(id, i + ""));
                }
            }

            /*  941 */ player.gainEmoticon(1008);
            /*  942 */ player.gainEmoticon(1009);
            /*  943 */ player.gainEmoticon(1010);

            /*  945 */ if (player.getKeyValue(51351, "startquestid") > 0L) {
                /*  946 */ player.gainSuddenMission((int) player.getKeyValue(51351, "startquestid"), (int) player.getKeyValue(51351, "midquestid"), false);
            }

           

            /*  949 */ if (player.getV("GuildBless") != null) {
                /*  950 */ long keys = Long.parseLong(player.getV("GuildBless"));
                /*  951 */ Calendar clear = new GregorianCalendar((int) keys / 10000, (int) (keys % 10000L / 100L) - 1, (int) keys % 100);
                /*  952 */ Calendar ocal = Calendar.getInstance();
                /*  953 */ int yeal = clear.get(1), days = clear.get(5), day2 = clear.get(7), maxday = clear.getMaximum(5), month = clear.get(2);
                /*  954 */ int check = (day2 == 7) ? 2 : ((day2 == 6) ? 3 : ((day2 == 5) ? 4 : ((day2 == 4) ? 5 : ((day2 == 3) ? 6 : ((day2 == 2) ? 7 : ((day2 == 1) ? 1 : 0))))));
                /*  955 */ int afterday = days + check;
                /*  956 */ if (afterday > maxday) {
                    /*  957 */ afterday -= maxday;
                    /*  958 */ month++;
                }
                /*  960 */ if (month > 12) {
                    /*  961 */ yeal++;
                    /*  962 */ month = 1;
                }
                /*  964 */ Calendar after = new GregorianCalendar(yeal, month, afterday);
                /*  965 */ if (after.getTimeInMillis() < System.currentTimeMillis()) {
                    /*  966 */ MapleQuest quest = MapleQuest.getInstance(26000);
                    /*  967 */ player.getQuest_Map().remove(quest);
                    /*  968 */ MapleQuestStatus queststatus = new MapleQuestStatus(quest, 0);
                    /*  969 */ queststatus.setStatus((byte) 0);
                    /*  970 */ queststatus.setCustomData("");
                    /*  971 */ player.getClient().send(CWvsContext.InfoPacket.updateQuest(queststatus));
                }
            }

            /*  975 */ if (ServerConstants.feverTime || Calendar.getInstance().get(7) == 7) {
                /*  976 */ ServerConstants.feverTime = true;
                /*  977 */ player.FeverTime(true, false);
            } else {
                /*  979 */ ServerConstants.feverTime = false;
            }

            /*  983 */ if (player.getClient().getCustomData(501368, "spoint") != null) {
                /*  984 */ int coin = Integer.parseInt(player.getClient().getCustomData(501368, "spoint"));
                /*  985 */ if (coin != player.getKeyValue(501368, "point")) {
                    /*  986 */ player.setKeyValue(501368, "point", coin + "");
                }
            }

            /*  990 */ c.getPlayer().loadPremium();
            /*  991 */ StringBuilder sb3 = new StringBuilder();
            /*  992 */ sb3.append(CurrentTime.getYear());
            /*  993 */ sb3.append(StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2));
            /*  994 */ sb3.append(StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getDate()), '0', 2));

            /*  996 */ if (player.haveItem(2438697)) {
                /*  997 */ if (player.getV("d_day_t") == null) {
                    /*  998 */ player.addKV("d_day_t", "0");
                }
                /* 1000 */ if (player.getV("d_daycheck") == null) {
                    /* 1001 */ player.addKV("d_daycheck", "0");
                }
                /* 1003 */ if (Long.parseLong(player.getV("d_day_t")) < Long.parseLong(sb3.toString())) {
                    /* 1004 */ player.addKV("d_day_t", sb3.toString());
                    /* 1005 */ player.addKV("d_daycheck", "" + (Long.parseLong(player.getV("d_daycheck")) + 1L));
                }
            }

            /* 1009 */ if (GameConstants.isYeti(player.getJob()) || GameConstants.isPinkBean(player.getJob())) {
                /* 1010 */ MapleQuest quest = MapleQuest.getInstance(7291);
                /* 1011 */ MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);
                /* 1012 */ String skinString = String.valueOf(GameConstants.isPinkBean(player.getJob()) ? 292 : 293);
                /* 1013 */ queststatus.setCustomData((skinString == null) ? "0" : skinString);
                /* 1014 */ player.updateQuest(queststatus, true);
                /* 1015 */ player.setKeyValue(7293, "damage_skin", GameConstants.isPinkBean(player.getJob()) ? "2633220" : "2633218");
                /* 1016 */ if (player.getSkillLevel(80000602) <= 0) {
                    /* 1017 */ player.changeSkillLevel(80000602, (byte) 1, (byte) 1);
                }
            }
            /* 1020 */ Item weapon = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);

            /* 1022 */ if (weapon != null && player.getBuffedEffect(SecondaryStat.SoulMP) == null) {
                /* 1023 */ player.setSoulMP((Equip) weapon);
            }

            /* 1026 */ boolean cgr = false;
            /* 1027 */ for (MapleGuild mg : World.Guild.getGuilds(1, 999, 1, 999, 1, 999)) {
                /* 1028 */ if (mg.getRequest(player.getId()) != null) {
                    /* 1029 */ c.getPlayer().setKeyValue(26015, "name", mg.getName());
                    /* 1030 */ cgr = true;
                }
            }
            /* 1033 */ if (!cgr || c.getPlayer().getGuild() != null) {
                /* 1034 */ c.getPlayer().setKeyValue(26015, "name", "");
            }

            /* 1037 */ if (player.getKeyValue(333333, "quick0") > 0L) {
                /* 1038 */ c.getSession().writeAndFlush(CField.quickSlot(player));
            }

            /* 1042 */ if (c.getCustomData(252, "count") == null) {
                /* 1043 */ c.setCustomData(252, "count", "0");
            }
            /* 1045 */ if (c.getCustomData(252, "T") == null || c.getCustomData(252, "T").equals("0")) {
                /* 1046 */ c.setCustomData(252, "count", "0");
                /* 1047 */ c.setCustomData(252, "T", GameConstants.getCurrentFullDate());
            } else {

                /* 1050 */ String bTime = c.getCustomData(252, "T");
                /* 1051 */ String cTime = GameConstants.getCurrentFullDate();
                /* 1052 */ int bH = Integer.parseInt(bTime.substring(8, 10));
                /* 1053 */ int bM = Integer.parseInt(bTime.substring(10, 12));
                /* 1054 */ int cH = Integer.parseInt(cTime.substring(8, 10));
                /* 1055 */ int cM = Integer.parseInt(cTime.substring(10, 12));
                /* 1056 */ if ((cH - bH == 1 && cM >= bM) || cH - bH > 1) {
                    /* 1057 */ c.setCustomData(252, "count", "3600");
                }
            }

            /* 1061 */ if (c.getCustomData(253, "day") == null) {
                /* 1062 */ c.setCustomData(253, "day", "0");
            }

            /* 1065 */ if (c.getCustomData(253, "complete") == null) {
                /* 1066 */ c.setCustomData(253, "complete", "0");
            }

            /* 1069 */ if (c.getCustomData(253, "bMaxDay") == null) {
                /* 1070 */ c.setCustomData(253, "bMaxDay", "135");
            }

            /* 1073 */ if (c.getCustomData(253, "cMaxDay") == null) {
                /* 1074 */ c.setCustomData(253, "cMaxDay", "135");
            }

            /* 1077 */ if (c.getCustomData(253, "lastDate") == null) {
                /* 1078 */ c.setCustomData(253, "lastDate", "20/12/31");
            }

            /* 1081 */ if (c.getCustomData(253, "passCount") == null) {
                /* 1082 */ c.setCustomData(253, "passCount", "135");
            }

       if (player.getKeyValue(0, "Boss_Level") < 0) {
                player.setKeyValue(0, "Boss_Level", "0");
            }

            /* 1085 */ if (c.getCustomData(254, "passDate") == null) {
                /* 1086 */ StringBuilder str = new StringBuilder();
                /* 1087 */ for (int k = 0; k < 63; k++) {
                    /* 1088 */ str.append("0");
                }
                /* 1090 */ c.setCustomData(254, "passDate", str.toString());
            }

            /* 1096 */ MatrixHandler.calcSkillLevel(player, -1);

            /* 1098 */ int linkSkill = GameConstants.getMyLinkSkill(player.getJob());
            /* 1099 */ if (linkSkill > 0 && player.getSkillLevel(linkSkill) != ((player.getLevel() >= 120) ? 2 : 1)) {
                /* 1100 */ player.changeSkillLevel(linkSkill, (byte) ((player.getLevel() >= 120) ? 2 : 1), (byte) 2);
            }

            /* 1103 */ if (c.getCustomData(238, "T") == null || c.getCustomData(238, "T").equals("0")) {
                /* 1104 */ c.setCustomData(238, "count", "0");
                /* 1105 */ c.setCustomData(238, "T", GameConstants.getCurrentFullDate());
            }

            /* 1109 */ if (GameConstants.isWildHunter(player.getJob())) {
                /* 1110 */ boolean change = false;
                /* 1111 */ for (int a = 9304000; a <= 9304008; a++) {
                    /* 1112 */ int jaguarid = GameConstants.getJaguarType(a);
                    /* 1113 */ String info = player.getInfoQuest(23008);
                    /* 1114 */ for (int k = 0; k <= 8; k++) {
                        /* 1115 */ if (!info.contains(k + "=1")) {
                            /* 1117 */ if (k == jaguarid) /* 1118 */ {
                                info = info + k + "=1;";
                            }
                        }
                    }
                    /* 1121 */ player.updateInfoQuest(23008, info);
                    /* 1122 */ player.updateInfoQuest(123456, String.valueOf(jaguarid * 10));
                    /* 1123 */ change = true;
                }
                /* 1125 */ if (change) {
                    /* 1126 */ c.getSession().writeAndFlush(CWvsContext.updateJaguar(player));
                }
                
                            if (player.getKeyValue(190823, "grade") == 0) {
                player.setKeyValue(190823, "grade", "1");
            }
            }

            /* 1130 */ MapleQuestStatus stat = player.getQuestNoAdd(MapleQuest.getInstance(122700));
            /* 1131 */ c.getSession().writeAndFlush(CWvsContext.pendantSlot(true));
            /* 1132 */ c.getSession().writeAndFlush(CWvsContext.temporaryStats_Reset());
            /* 1133 */ player.getMap().addPlayer(player);
            /* 1134 */ c.getSession().writeAndFlush(CWvsContext.setBossReward(player));
            /* 1135 */ c.getSession().writeAndFlush(CWvsContext.onSessionValue("kill_count", "0"));
            /* 1136 */ c.getSession().writeAndFlush(CWvsContext.updateDailyGift("count=" + c.getKeyValue("dailyGiftComplete") + ";day=" + c.getKeyValue("dailyGiftDay") + ";date=" + player.getKeyValue(501385, "date")));
            /* 1137 */ c.getSession().writeAndFlush(CField.dailyGift(player, 1, 0));

            
            try {
                /* 1142 */ int[] buddyIds = player.getBuddylist().getBuddyIds();
                /* 1143 */ World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), c.getAccID(), buddyIds);
                /* 1144 */ if (player.getParty() != null) {
                    /* 1145 */ MapleParty party = player.getParty();
                    /* 1146 */ World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
                }

                /* 1149 */ AccountIdChannelPair[] onlineBuddies = World.Find.multiBuddyFind(player.getBuddylist(), player.getId(), buddyIds);
                /* 1150 */ for (AccountIdChannelPair onlineBuddy : onlineBuddies) {
                    /* 1151 */ player.getBuddylist().get(onlineBuddy.getAcountId()).setChannel(onlineBuddy.getChannel());
                }
                /* 1153 */ player.getBuddylist().setChanged(true);
                /* 1154 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.updateBuddylist(player.getBuddylist().getBuddies(), null, (byte) 20));

                /* 1157 */ MapleMessenger messenger = player.getMessenger();
                /* 1158 */ if (messenger != null) {
                    /* 1159 */ World.Messenger.silentJoinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()));
                    /* 1160 */ World.Messenger.updateMessenger(messenger.getId(), c.getPlayer().getName(), c.getChannel());
                }

                /* 1164 */ if (player.getGuildId() > 0) {
                    /* 1165 */ MapleGuild gs = World.Guild.getGuild(player.getGuildId());
                    /* 1166 */ if (gs != null) {
                        /* 1167 */ if (gs.getLastResetDay() == 0) {
                            /* 1168 */ gs.setLastResetDay(Integer.parseInt(kc.getYears() + kc.getMonths() + kc.getDays()));
                        }
                        /* 1170 */ World.Guild.setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                        /* 1171 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(player));
                        /* 1172 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.guildLoadAattendance());
                        /* 1173 */ List<byte[]> packetList = World.Alliance.getAllianceInfo(gs.getAllianceId(), true);
                        /* 1174 */ if (packetList != null) {
                            /* 1175 */ for (byte[] pack : packetList) {
                                /* 1176 */ if (pack != null) {
                                    /* 1177 */ c.getSession().writeAndFlush(pack);
                                }
                            }
                        }
                    } else {
                        /* 1182 */ player.setGuildId(0);
                        /* 1183 */ player.setGuildRank((byte) 5);
                        /* 1184 */ player.setAllianceRank((byte) 5);
                        /* 1185 */ player.saveGuildStatus();
                    }
                }
                /* 1188 */            } catch (Exception e) {
                /* 1189 */ e.printStackTrace();
            }

            /* 1192 */ CharacterNameAndId pendingBuddyRequest = player.getBuddylist().pollPendingRequest();
            /* 1193 */ if (pendingBuddyRequest != null) {
                /* 1194 */ player.getBuddylist().put(new BuddylistEntry(pendingBuddyRequest.getName(), pendingBuddyRequest.getRepName(), pendingBuddyRequest.getAccId(), pendingBuddyRequest.getId(), pendingBuddyRequest.getGroupName(), -1, false, pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob(), pendingBuddyRequest.getMemo()));
                /* 1195 */ c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.requestBuddylistAdd(pendingBuddyRequest.getId(), pendingBuddyRequest.getAccId(), pendingBuddyRequest.getName(), pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob(), c, pendingBuddyRequest.getGroupName(), pendingBuddyRequest.getMemo()));
            }

            /* 1198 */ player.getClient().getSession().writeAndFlush(CWvsContext.serverMessage("", channelServer.getServerMessage()));
            /* 1199 */ player.sendMacros();
            /* 1200 */ player.updatePartyMemberHP();
            /* 1201 */ player.startFairySchedule(false);
            player.gainDonationSkills();
            /* 1202 */ c.getSession().writeAndFlush(CField.getKeymap(player.getKeyLayout()));
            /* 1203 */ c.getSession().writeAndFlush(CWvsContext.OnClaimSvrStatusChanged(true));
            /* 1204 */ player.updatePetAuto();
            /* 1206 */ player.expirationTask(true, (transfer == null));

            /* 1208 */ c.getSession().writeAndFlush(CWvsContext.setUnion(c));
            /* 1209 */ player.getStat().recalcLocalStats(player);
            /* 1210 */ for (int j = 0; j < 5; j++) {
                /* 1211 */ c.getSession().writeAndFlush(CWvsContext.unionFreeset(c, j));
            }
            /* 1213 */ c.getPlayer().updateSingleStat(MapleStat.FATIGUE, c.getPlayer().getFatigue());
            /* 1214 */ if ((player.getStat()).equippedSummon > 0) {
                /* 1215 */ SkillFactory.getSkill((player.getStat()).equippedSummon).getEffect(1).applyTo(player, true);
            }

            /* 1218 */ c.getSession().writeAndFlush(CField.HeadTitle(player.HeadTitle()));

            /* 1220 */ PetHandler.updatePetSkills(player, null);

            /* 1222 */ c.getSession().writeAndFlush(CWvsContext.initSecurity());
            /* 1223 */ c.getSession().writeAndFlush(CWvsContext.updateSecurity());

            /* 1225 */ String towerchair = c.getPlayer().getInfoQuest(7266);
            /* 1226 */ if (!towerchair.equals("")) {
                /* 1227 */ c.getPlayer().updateInfoQuest(7266, towerchair);
            }
            if (ServerConstants.Event_Blooming) {
            /* 1229 */ if (player.getKeyValue(100794, "point") < 0L) {
                /* 1230 */ player.setKeyValue(100790, "lv", "1");
                /* 1231 */ player.setKeyValue(100794, "point", "0");
                /* 1232 */ player.setKeyValue(100794, "sum", "0");
                /* 1233 */ player.setKeyValue(100794, "date", kc.getYears() + kc.getMonths() + kc.getDays());
                /* 1234 */ player.setKeyValue(100794, "today", "0");
            }
            /* 1236 */ c.getSession().writeAndFlush(SLFCGPacket.StarDustUI("UI/UIWindowEvent.img/starDust_18th", player.getKeyValue(100794, "sum"), player.getKeyValue(100794, "point"), (player.getKeyValue(100794, "lock") == 1L)));
            
            
            }
            
            for (int ho : ServerConstants.hour) {
                if (new Date().getMinutes() >= 50 && new Date().getMinutes() <= 59 && new Date().getHours() == (ho - 1)) {
                    player.dropMessage(-8, (60 - new Date().getMinutes()) + "분후 월드 보스가 시작됩니다. 광장에 월드보스 NPC 를 통해 어서 입장하세요.");
                    player.sethottimeboss(true);
                }
            }
            
            if (player.getClient().isFirstLogin() && !player.isGM() && !player.getName().equals("\uc624\ube0c")) {
                World.Broadcast.broadcastMessage(CField.UIPacket.detailShowInfo(player.getName() + "\ub2d8, \uc811\uc18d\uc744 \ud658\uc601\ud569\ub2c8\ub2e4. \uc624\ub298\ub3c4 " + LoginServer.getServerName() + "\uc5d0\uc11c \uc990\uac70\uc6b4 \uc2dc\uac04 \ub418\uc138\uc694.", false));
                player.getClient().setLogin(false);
            }
            /* 1238 */ if (c.getPlayer().getPremiumPeriod().longValue() > LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond()) {
                /* 1239 */ Skill skill = SkillFactory.getSkill(c.getPlayer().getPremiumBuff());
                /* 1240 */ if (skill != null) {
                    /* 1241 */ if (player.getSkillLevel(skill) < 1) {
                        /* 1242 */ c.getPlayer().changeSingleSkillLevel(skill, skill.getMaxLevel(), (byte) skill.getMasterLevel());
                    }

                    /* 1245 */ long td = (c.getPlayer().getPremiumPeriod().longValue() - LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond()) / 86400000L;
                    /* 1246 */ c.getPlayer().dropMessage(5, "프리미엄 버프가 " + td + "일 남았습니다.");
                    /* 1247 */ SkillFactory.getSkill(c.getPlayer().getPremiumBuff()).getEffect(1).applyTo(player, false);
                }
            }

            /* 1251 */ for (int sm = 0; sm <= 7; sm++) {
                /* 1252 */ if (c.getPlayer().getKeyValue(2018207, "medalSkill_" + sm) == 1L) {
                    /* 1253 */ Skill skill = SkillFactory.getSkill(80001535 + sm);
                    /* 1254 */ if (skill != null) {
                        /* 1255 */ if (player.getSkillLevel(skill) < 1) {
                            /* 1256 */ c.getPlayer().changeSingleSkillLevel(skill, skill.getMaxLevel(), (byte) skill.getMasterLevel());
                        }
                        /* 1258 */ SkillFactory.getSkill(80001535 + sm).getEffect(1).applyTo(player, false);
                    }
                }
            }

            /* 1263 */ int[][] medals = {{1143507, 80001543}, {1143508, 80001544}, {1143509, 80001545}};
            /* 1264 */ for (int m = 0; m < medals.length; m++) {
                /* 1265 */ if (c.getPlayer().haveItem(medals[m][0])) {
                    /* 1266 */ Skill skill = SkillFactory.getSkill(medals[m][1]);
                    /* 1267 */ if (skill != null) {
                        /* 1268 */ if (player.getSkillLevel(skill) < 1) {
                            /* 1269 */ c.getPlayer().changeSingleSkillLevel(skill, skill.getMaxLevel(), (byte) skill.getMasterLevel());
                        }
                        /* 1271 */ SkillFactory.getSkill(medals[m][1]).getEffect(1).applyTo(player, false);
                    }
                }
            }

            /* 1276 */ if (ServerConstants.ServerTest) {
                /* 1277 */ c.send(CField.NPCPacket.getNPCTalk(9010061, (byte) 0, "#e더 블랙 테스트 월드에 오신 것을 환영합니다#n\r\n이 곳은 블랙 페스티벌 정식 오픈 전에 각종 버그, 오류들을 미리 테스트 하기 위해 만들어 진 곳이며 다음과 같은 사항이 제한됩니다.\r\n\r\n#b1. 테스트 월드에서는 테스트를 위하여 캐릭터 성장이 쉽게 설정되어있습니다. 이러한 모든 정보는 정식 월드에는 적용되지 않습니다.\r\n2. 테스트 월드의 캐릭터 정보는 정식 월드와 절대 공유되지 않습니다.\r\n3. 테스트 월드의 캐릭터 정보는 이용자의 동의를 받지 않고 아무런 공지없이 리셋될 수 있습니다.\r\n4. 테스트 월드의 서비스가 아무런 공지없이 중단될 수 있습니다.\r\n5. 이 곳에서 테스트 된 내용이 정식 월드에 반드시 적용되지는 않습니다.\r\n6. 테스트 된 내용이 정식 월드에 추가될 때에는 세부 수치가 변경될 수 있습니다.", "00 00", (byte) 0, c.getPlayer().getId()));
                /* 1278 */ if (!player.haveItem(2431138)) {
                    /* 1279 */ player.gainItem(2431138, 1);
                }
            }

            /* 1283 */        } catch (Exception e) {
            /* 1284 */ e.printStackTrace();
        }
    }

    public static final void ChangeChannel(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr, boolean room) {
        try {
            if (chr == null || chr.getEventInstance() != null || chr.getMap() == null || FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit())) {
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (World.getPendingCharacterSize() >= 10) {
                chr.dropMessage(1, "채널 이동중인 사람이 많습니다. 잠시 후 시도해주세요.");
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            int chc = slea.readByte() + 1;
            int mapid = 0;
            if (room) {
                mapid = slea.readInt();
            }
            slea.readInt();
            if (!World.isChannelAvailable(chc)) {
                chr.dropMessage(1, "현재 해당 채널이 혼잡하여 이동하실 수 없습니다.");
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (room && (mapid < 910000001 || mapid > 910000022)) {
                chr.dropMessage(1, "현재 해당 채널이 혼잡하여 이동하실 수 없습니다.");
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                return;
            }
            if (room) {
                if (chr.getMapId() == mapid) {
                    if (c.getChannel() == chc) {
                        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    } else {
                        chr.changeChannel(chc);
                    }
                } else {
                    if (c.getChannel() != chc) {
                        chr.changeChannel(chc);
                    }
                    MapleMap warpz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(mapid);
                    if (warpz != null) {
                        chr.changeMap(warpz, warpz.getPortal("out00"));
                    } else {
                        chr.dropMessage(1, "현재 해당 채널이 혼잡하여 이동하실 수 없습니다.");
                        c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    }
                }
            } else {
                chr.changeChannel(chc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getGameQuitRequest(RecvPacketOpcode header, LittleEndianAccessor rh, MapleClient c) {
        String account = null;
        if (header == RecvPacketOpcode.GAME_EXIT) {
            rh.skip(8);
        } else {
            account = rh.readMapleAsciiString();
        }
        if (account == null || account.equals("")) {
            account = c.getAccountName();
        }
        if (c == null) {
            return;
        }
        if (account == null || header == RecvPacketOpcode.GAME_EXIT) {
            c.disconnect(true, false, false);
            c.getSession().close();
            return;
        }
        if (!c.isLoggedIn() && !c.getAccountName().equals(account)) {
            c.disconnect(true, false, false);
            c.getSession().close();
            return;
        }
        c.disconnect(true, false, false);
        c.getSession().writeAndFlush(LoginPacket.getKeyGuardResponse(account + "," + c.getPassword(account)));
    }
}
