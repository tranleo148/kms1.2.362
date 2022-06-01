package scripting;

import client.InnerSkillValueHolder;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleQuestStatus;
import client.MapleShopLimit;
import client.MapleStat;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import constants.GameConstants;
import constants.KoreaCalendar;
import constants.ServerConstants;
import database.DatabaseConnection;
import discord.DiscordClient;
import discord.DiscordServer;
import discord.PacketCreator;
import handling.channel.ChannelServer;
import handling.channel.handler.InterServerHandler;
import handling.channel.handler.PlayersHandler;
import handling.login.LoginInformationProvider;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import javax.script.Invocable;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.InnerAbillity;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.*;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MonsterDropEntry;
import server.maps.Event_DojoAgent;
import server.maps.MapleMap;
import server.marriage.MarriageDataEntry;
import server.quest.MapleQuest;
import server.quest.party.MapleNettPyramid;
import server.shops.MapleShopFactory;
import server.shops.MapleShopItem;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PacketHelper;
import tools.packet.SLFCGPacket;

public class NPCConversationManager extends AbstractPlayerInteraction {

    private String getText;
    /*   85 */    private byte lastMsg = -1;
    private String script;
    private byte type;
    public boolean pendingDisposal = false;
    private Invocable iv;

    public NPCConversationManager(MapleClient c, int npc, int questid, byte type, Invocable iv, String script) {
        /*   90 */ super(c, npc, questid);
        /*   91 */ this.type = type;
        /*   92 */ this.iv = iv;
        /*   93 */ this.script = script;
    }

    public void sendConductExchange(String text) {
        /*   97 */ if (this.lastMsg > -1) {
            return;
        }
        /*  100 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCConductExchangeTalk(this.id, text));
        /*  101 */ this.lastMsg = 0;
    }

    public void sendPacket(short a, String b) {
        /*  105 */ this.c.getSession().writeAndFlush(SLFCGPacket.SendPacket(a, b));
    }

    public Invocable getIv() {
        /*  109 */ return this.iv;
    }

    public String getScript() {
        /*  113 */ return this.script;
    }

    public int getNpc() {
        /*  117 */ return this.id;
    }

    public int getQuest() {
        /*  121 */ return this.id2;
    }

    public byte getType() {
        /*  125 */ return this.type;
    }

    public void safeDispose() {
        /*  129 */ this.pendingDisposal = true;
    }

    public void dispose() {
        /*  133 */ NPCScriptManager.getInstance().dispose(this.c);
    }

    public void askMapSelection(String sel) {
        /*  137 */ if (this.lastMsg > -1) {
            return;
        }
        /*  140 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getMapSelection(this.id, sel));
        /*  141 */ this.lastMsg = 17;
    }

    public void sendNext(String text) {
        /*  145 */ sendNext(text, this.id);
    }

    public void sendNext(String text, int id) {
        /*  149 */ if (this.lastMsg > -1) {
            return;
        }
        /*  152 */ if (text.contains("#L")) {
            /*  153 */ sendSimple(text);
            return;
        }
        /*  156 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", (byte) 0));
        /*  157 */ this.lastMsg = 0;
    }

    public void sendPlayerToNpc(String text) {
        /*  161 */ sendNextS(text, (byte) 3, this.id);
    }

    public void StartSpiritSavior() {
    }

    public void StartBlockGame() {
        /*  169 */ MapleClient c = getClient();
        /*  170 */ c.getSession().writeAndFlush(CField.onUserTeleport(c.getPlayer(), 65535, 0));
        /*  171 */ c.getSession().writeAndFlush(CField.UIPacket.IntroDisableUI(true));
        /*  172 */ c.getSession().writeAndFlush(CField.UIPacket.IntroLock(true));
        /*  173 */ server.Timer.EventTimer.getInstance().schedule(() -> {
            c.getSession().writeAndFlush(SLFCGPacket.BlockGameCommandPacket(1));
            c.getSession().writeAndFlush(SLFCGPacket.BlockGameCommandPacket(2));
            c.getSession().writeAndFlush(SLFCGPacket.BlockGameControlPacket(100, 10));
        },
    

    2000L);
   }
 
 
     public boolean setZodiacGrade(int grade) {
        if (c.getPlayer().getKeyValue(190823, "grade") >= grade) {
            return false;
        }
        c.getPlayer().setKeyValue(190823, "grade", String.valueOf(grade));
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), SLFCGPacket.ZodiacRankInfo(c.getPlayer().getId(), grade), true);
        c.getSession().writeAndFlush(SLFCGPacket.playSE("Sound/MiniGame.img/Result_Yut"));
        showEffect(false, "Effect/CharacterEff.img/gloryonGradeup");
//        MapleStatEffect eff = SkillFactory.getSkill(80002419).getEffect(1);
//        c.getPlayer().cancelEffect(eff, true, -1);
//        eff.applyTo(c.getPlayer());
//        c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
        return true;
    }
 
    
   
   public void sendNextNoESC(String text) {
        /*  181 */ sendNextS(text, (byte) 1, this.id);
    }

    public void sendNextNoESC(String text, int id) {
        /*  185 */ sendNextS(text, (byte) 1, id);
    }

    public void sendNextS(String text, byte type) {
        /*  189 */ sendNextS(text, type, 0);
    }

    public void sendNextS(String text, byte type, int id, int idd) {
        /*  193 */ if (this.lastMsg > -1) {
            return;
        }
        /*  196 */ if (text.contains("#L")) {
            /*  197 */ sendSimpleS(text, type);
            return;
        }
        /*  200 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", type, idd));
        /*  201 */ this.lastMsg = 0;
    }

    public void sendNextS(String text, byte type, int idd) {
        /*  205 */ if (this.lastMsg > -1) {
            return;
        }
        /*  208 */ if (text.contains("#L")) {
            /*  209 */ sendSimpleS(text, type);
            return;
        }
        /*  212 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 0, text, "00 01", type, idd));
        /*  213 */ this.lastMsg = 0;
    }

    public void sendCustom(String text, byte type, int idd) {
        /*  217 */ if (this.lastMsg > -1) {
            return;
        }
        /*  220 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalks(this.id, (byte) 0, text, "00 01", type, idd));
        /*  221 */ this.lastMsg = 0;
    }

    public void sendPrev(String text) {
        /*  225 */ sendPrev(text, this.id);
    }

    public void sendPrev(String text, int id) {
        /*  229 */ if (this.lastMsg > -1) {
            return;
        }
        /*  232 */ if (text.contains("#L")) {
            /*  233 */ sendSimple(text);
            return;
        }
        /*  236 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "01 00", (byte) 0));
        /*  237 */ this.lastMsg = 0;
    }

    public void sendPrevS(String text, byte type) {
        /*  241 */ sendPrevS(text, type, 0);
    }

    public void sendPrevS(String text, byte type, int idd) {
        /*  245 */ if (this.lastMsg > -1) {
            return;
        }
        /*  248 */ if (text.contains("#L")) {
            /*  249 */ sendSimpleS(text, type);
            return;
        }
        /*  252 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 0, text, "01 00", type, idd));
        /*  253 */ this.lastMsg = 0;
    }

    public void sendNextPrev(String text) {
        /*  257 */ sendNextPrev(text, this.id);
    }

    public void sendNextPrev(String text, int id) {
        /*  261 */ if (this.lastMsg > -1) {
            return;
        }
        /*  264 */ if (text.contains("#L")) {
            /*  265 */ sendSimple(text);
            return;
        }
        /*  268 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "01 01", (byte) 0));
        /*  269 */ this.lastMsg = 0;
    }

    public void EnterCS() {
        /*  273 */ InterServerHandler.EnterCS(this.c, this.c.getPlayer(), false);
    }

    public void PlayerToNpc(String text) {
        /*  277 */ sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text) {
        /*  281 */ sendNextPrevS(text, (byte) 3);
    }

    public String getMobName(int mobid) {
        /*  285 */ MapleData data = null;
        /*  286 */ MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/String.wz"));
        /*  287 */ String ret = "";
        /*  288 */ List<String> retMobs = new ArrayList<>();

        /*  290 */ data = dataProvider.getData("Mob.img");
        /*  291 */ List<Pair<Integer, String>> mobPairList = new LinkedList<>();
        /*  292 */ for (MapleData mobIdData : data.getChildren()) {
            /*  293 */ mobPairList.add(new Pair(Integer.valueOf(Integer.parseInt(mobIdData.getName())), MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME")));
        }
        /*  295 */ for (Pair<Integer, String> mobPair : mobPairList) {
            /*  296 */ if (((Integer) mobPair.getLeft()).intValue() == mobid) {
                /*  297 */ ret = (String) mobPair.getRight();
            }
        }
        /*  300 */ return ret;
    }

    public void sendNextPrevS(String text, byte type) {
        /*  304 */ sendNextPrevS(text, type, 0);
    }

    public void sendNextPrevS(String text, byte type, int id, int idd) {
        /*  308 */ if (this.lastMsg > -1) {
            return;
        }
        /*  311 */ if (text.contains("#L")) {
            /*  312 */ sendSimpleS(text, type);
            return;
        }
        /*  315 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "01 01", type, idd));
        /*  316 */ this.lastMsg = 0;
    }

    public void sendNextPrevS(String text, byte type, int idd) {
        /*  320 */ if (this.lastMsg > -1) {
            return;
        }
        /*  323 */ if (text.contains("#L")) {
            /*  324 */ sendSimpleS(text, type);
            return;
        }
        /*  327 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 0, text, "01 01", type, idd));
        /*  328 */ this.lastMsg = 0;
    }

    public void sendDimensionGate(String text) {
        /*  332 */ if (this.lastMsg > -1) {
            return;
        }
        /*  335 */ if (text.contains("#L")) {
            /*  336 */ sendSimple(text);
            return;
        }
        /*  339 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 19, text, "00 00", (byte) 0));
        /*  340 */ this.lastMsg = 0;
    }

    public void sendOk(String text) {
        /*  344 */ sendOk(text, this.id);
    }

    public void sendOk(String text, int id) {
        /*  348 */ if (this.lastMsg > -1) {
            return;
        }
        /*  351 */ if (text.contains("#L")) {
            /*  352 */ sendSimple(text);
            return;
        }
        /*  355 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "00 00", (byte) 0));
        /*  356 */ this.lastMsg = 0;
    }

    public void sendOkS(String text, byte type) {
        /*  360 */ sendOkS(text, type, 0);
    }

    public void sendOkS(String text, byte type, int idd) {
        /*  364 */ if (this.lastMsg > -1) {
            return;
        }
        /*  367 */ if (text.contains("#L")) {
            /*  368 */ sendSimpleS(text, type);
            return;
        }
        /*  371 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(idd, (byte) 0, text, "00 00", type, idd));
        /*  372 */ this.lastMsg = 0;
    }

    public void sendYesNo(String text) {
        /*  376 */ sendYesNo(text, this.id);
    }

    public void sendYesNo(String text, int id) {
        /*  380 */ if (this.lastMsg > -1) {
            return;
        }
        /*  383 */ if (text.contains("#L")) {
            /*  384 */ sendSimple(text);
            return;
        }
        /*  387 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 3, text, "", (byte) 0));
        /*  388 */ this.lastMsg = 2;
    }

    public void sendYesNoS(String text, byte type) {
        /*  392 */ sendYesNoS(text, type, 0);
    }

    public void sendYesNoS(String text, byte type, int idd) {
        /*  396 */ if (this.lastMsg > -1) {
            return;
        }
        /*  399 */ if (text.contains("#L")) {
            /*  400 */ sendSimpleS(text, type);
            return;
        }
        /*  403 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 3, text, "", type, idd));
        /*  404 */ this.lastMsg = 2;
    }

    public void sendAcceptDecline(String text) {
        /*  408 */ askAcceptDecline(text);
    }

    public void sendAcceptDeclineNoESC(String text) {
        /*  412 */ askAcceptDeclineNoESC(text);
    }

    public void askAcceptDecline(String text) {
        /*  416 */ askAcceptDecline(text, this.id);
    }

    public void askAcceptDecline(String text, int id) {
        /*  420 */ if (this.lastMsg > -1) {
            return;
        }
        /*  423 */ if (text.contains("#L")) {
            /*  424 */ sendSimple(text);
            return;
        }
        /*  427 */ this.lastMsg = 16;
        /*  428 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, this.lastMsg, text, "", (byte) 0));
    }

    public void askPraticeReplace(String text) {
        /*  432 */ askPraticeReplace(text, this.id);
    }

    public void askPraticeReplace(String text, int id) {
        /*  436 */ if (this.lastMsg > -1) {
            return;
        }
        /*  439 */ if (text.contains("#L")) {
            /*  440 */ sendSimple(text);
            return;
        }
        /*  443 */ this.lastMsg = 3;
        /*  444 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getPraticeReplace(id, this.lastMsg, text, "", (byte) 0, 1));
    }

    public void askAcceptDeclineNoESC(String text) {
        /*  448 */ askAcceptDeclineNoESC(text, this.id);
    }

    public void askAcceptDeclineNoESC(String text, int id) {
        /*  452 */ if (this.lastMsg > -1) {
            return;
        }
        /*  455 */ if (text.contains("#L")) {
            /*  456 */ sendSimple(text);
            return;
        }
        /*  459 */ this.lastMsg = 16;
        /*  460 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, this.lastMsg, text, "", (byte) 1));
    }

    public void sendStyle(String text, int... args) {
        /*  464 */ askAvatar(text, args);
    }

    public void askCustomMixHairAndProb(String text) {
        /*  468 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkMixStyle(this.id, text, GameConstants.isZero(this.c.getPlayer().getJob()) ? ((this.c.getPlayer().getGender() == 1)) : false, GameConstants.isAngelicBuster(this.c.getPlayer().getJob()) ? (this.c.getPlayer().getDressup()) : false));
        /*  469 */ this.lastMsg = 44;
    }

    public void askAvatar(String text, int... args) {
        /*  473 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.c.getPlayer(), this.id, text, args));
        /*  474 */ this.lastMsg = 9;
    }

    public void askAvatar(String text, int[] args1, int[] args2) {
        /*  478 */ if (this.lastMsg > -1) {
            return;
        }
        /*  481 */ if (GameConstants.isZero(this.c.getPlayer().getJob())) {
            /*  482 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyleZero(this.id, text, args1, args2));
        } else {
            /*  484 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.c.getPlayer(), this.id, text, args1));
        }
        /*  486 */ this.lastMsg = 9;
    }

    public void askCoupon(int itemid, int... args) {
        /*  490 */ this.c.send(CWvsContext.UseMakeUpCoupon(this.c.getPlayer(), itemid, args));
    }

    public void askAvatarAndroid(String text, int... args) {
        /*  494 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyleAndroid(this.id, text, args));
    }

    public void sendSimple(String text) {
        /*  498 */ sendSimple(text, this.id);
    }

    public void sendSimple(String text, int id) {
        /*  502 */ if (this.lastMsg > -1) {
            return;
        }
        /*  505 */ if (!text.contains("#L")) {
            /*  506 */ sendNext(text);
            return;
        }
        /*  509 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 6, text, "", (byte) 0));
        /*  510 */ this.lastMsg = 5;
    }

    public void sendSimpleS(String text, byte type) {
        /*  514 */ sendSimpleS(text, type, 0);
    }

    public void sendSimpleS(String text, byte type, int idd) {
        /*  518 */ if (this.lastMsg > -1) {
            return;
        }
        /*  521 */ if (!text.contains("#L")) {
            /*  522 */ sendNextS(text, type);
            return;
        }
        /*  525 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 6, text, "", type, idd));
        /*  526 */ this.lastMsg = 5;
    }

    public void sendSimpleS(String text, byte type, int id, int idd) {
        /*  530 */ if (this.lastMsg > -1) {
            return;
        }
        /*  533 */ if (!text.contains("#L")) {
            /*  534 */ sendNextS(text, type);
            return;
        }
        /*  537 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 6, text, "", type, idd));
        /*  538 */ this.lastMsg = 5;
    }

    public void sendStyle(String text, int[] styles1, int[] styles2) {
        /*  542 */ if (this.lastMsg > -1) {
            return;
        }
        /*  545 */ if (GameConstants.isZero(this.c.getPlayer().getJob())) {
            /*  546 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyleZero(this.id, text, styles1, styles2));
            /*  547 */ this.lastMsg = 32;
        } else {
            /*  549 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.c.getPlayer(), this.id, text, styles1));
            /*  550 */ this.lastMsg = 9;
        }
    }

    public void sendIllustYesNo(String text, int face, boolean isLeft) {
        /*  555 */ if (this.lastMsg > -1) {
            return;
        }
        /*  558 */ if (text.contains("#L")) {
            /*  559 */ sendIllustSimple(text, face, isLeft);
            return;
        }
        /*  562 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 28, text, "", (byte) 0, face, true, isLeft));
        /*  563 */ this.lastMsg = 28;
    }

    public void sendIllustSimple(String text, int face, boolean isLeft) {
        /*  567 */ if (this.lastMsg > -1) {
            return;
        }
        /*  570 */ if (!text.contains("#L")) {
            /*  571 */ sendIllustNext(text, face, isLeft);
            return;
        }
        /*  574 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 30, text, "", (byte) 0, face, true, isLeft));
        /*  575 */ this.lastMsg = 30;
    }

    public void sendIllustNext(String text, int face, boolean isLeft) {
        /*  579 */ if (this.lastMsg > -1) {
            return;
        }
        /*  582 */ if (text.contains("#L")) {
            /*  583 */ sendIllustSimple(text, face, isLeft);
            return;
        }
        /*  586 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "00 01", (byte) 0, face, true, isLeft));
        /*  587 */ this.lastMsg = 26;
    }

    public void sendIllustPrev(String text, int face, boolean isLeft) {
        /*  591 */ if (this.lastMsg > -1) {
            return;
        }
        /*  594 */ if (text.contains("#L")) {
            /*  595 */ sendIllustSimple(text, face, isLeft);
            return;
        }
        /*  598 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "01 00", (byte) 0, face, true, isLeft));
        /*  599 */ this.lastMsg = 26;
    }

    public void sendIllustNextPrev(String text, int face, boolean isLeft) {
        /*  603 */ if (this.lastMsg > -1) {
            return;
        }
        /*  606 */ if (text.contains("#L")) {
            /*  607 */ sendIllustSimple(text, face, isLeft);
            return;
        }
        /*  610 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "01 01", (byte) 0, face, true, isLeft));
        /*  611 */ this.lastMsg = 26;
    }

    public void sendIllustOk(String text, int face, boolean isLeft) {
        /*  615 */ if (this.lastMsg > -1) {
            return;
        }
        /*  618 */ if (text.contains("#L")) {
            /*  619 */ sendIllustSimple(text, face, isLeft);
            return;
        }
        /*  622 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "00 00", (byte) 0, face, true, isLeft));
        /*  623 */ this.lastMsg = 26;
    }

    public void sendGetNumber(int id, String text, int def, int min, int max) {
        /*  627 */ if (this.lastMsg > -1) {
            return;
        }
        /*  630 */ if (text.contains("#L")) {
            /*  631 */ sendSimple(text);
            return;
        }
        /*  634 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkNum(id, text, def, min, max));
        /*  635 */ this.lastMsg = 4;
    }

    public void sendFriendsYesNo(String text, int id) {
        /*  639 */ if (this.lastMsg > -1) {
            return;
        }
        /*  642 */ if (text.contains("#L")) {
            /*  643 */ sendSimple(text);
            return;
        }
        /*  646 */ this.c.send(CField.NPCPacket.getNPCTalk(id, (byte) 3, text, "", (byte) 36));
        /*  647 */ this.lastMsg = 3;
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        /*  651 */ if (this.lastMsg > -1) {
            return;
        }
        /*  654 */ if (text.contains("#L")) {
            /*  655 */ sendSimple(text);
            return;
        }
        /*  658 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkNum(this.id, text, def, min, max));
        /*  659 */ this.lastMsg = 4;
    }

    public void sendGetText(String text) {
        /*  663 */ sendGetText(text, this.id);
    }

    public void sendGetText(String text, int id) {
        /*  667 */ if (this.lastMsg > -1) {
            return;
        }
        /*  670 */ if (text.contains("#L")) {
            /*  671 */ sendSimple(text);
            return;
        }
        /*  674 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkText(id, text));
        /*  675 */ this.lastMsg = 3;
    }

    public void setGetText(String text) {
        /*  679 */ this.getText = text;
    }

    public String getText() {
        /*  683 */ return this.getText;
    }

    public void setZeroSecondHair(int hair) {
        /*  687 */ getPlayer().setSecondHair(hair);
        /*  688 */ getPlayer().updateZeroStats();
        /*  689 */ getPlayer().equipChanged();
    }

    public void setZeroSecondFace(int face) {
        /*  693 */ getPlayer().setSecondFace(face);
        /*  694 */ getPlayer().updateZeroStats();
        /*  695 */ getPlayer().equipChanged();
    }

    public void setZeroSecondSkin(int color) {
        /*  699 */ getPlayer().setSecondSkinColor((byte) color);
        /*  700 */ getPlayer().updateZeroStats();
        /*  701 */ getPlayer().equipChanged();
    }

    public void setAngelicSecondHair(int hair) {
        /*  705 */ getPlayer().setSecondHair(hair);
        /*  706 */ getPlayer().updateAngelicStats();
        /*  707 */ getPlayer().equipChanged();
    }

    public void setAngelicSecondFace(int face) {
        /*  711 */ getPlayer().setSecondFace(face);
        /*  712 */ getPlayer().updateAngelicStats();
        /*  713 */ getPlayer().equipChanged();
    }

    public void setAngelicSecondSkin(int color) {
        /*  717 */ getPlayer().setSecondSkinColor((byte) color);
        /*  718 */ getPlayer().updateAngelicStats();
        /*  719 */ getPlayer().equipChanged();
    }

    public void setHair(int hair) {
        /*  723 */ getPlayer().setHair(hair);
        /*  724 */ getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        /*  725 */ getPlayer().equipChanged();
    }

    public void setFace(int face) {
        /*  729 */ getPlayer().setFace(face);
        /*  730 */ getPlayer().updateSingleStat(MapleStat.FACE, face);
        /*  731 */ getPlayer().equipChanged();
    }

    public void setSkin(int color) {
        /*  735 */ getPlayer().setSkinColor((byte) color);
        /*  736 */ getPlayer().updateSingleStat(MapleStat.SKIN, color);
        /*  737 */ getPlayer().equipChanged();
    }

    public int setRandomAvatar(int ticket, int... args_all) {
        /*  741 */ gainItem(ticket, (short) -1);

        /*  743 */ int args = args_all[Randomizer.nextInt(args_all.length)];
        /*  744 */ if (args < 100) {
            /*  745 */ this.c.getPlayer().setSkinColor((byte) args);
            /*  746 */ this.c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
            /*  747 */        } else if (args < 30000) {
            /*  748 */ this.c.getPlayer().setFace(args);
            /*  749 */ this.c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            /*  751 */ this.c.getPlayer().setHair(args);
            /*  752 */ this.c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        /*  754 */ this.c.getPlayer().equipChanged();

        /*  756 */ return 1;
    }

    /*  759 */    public static Map<Integer, String> hairlist = new HashMap<>();
    /*  760 */    public static Map<Integer, String> facelist = new HashMap<>();

    public int setAvatar(int ticket, int args) {
        /*  763 */ if (hairlist.isEmpty() || facelist.isEmpty()) {
            /*  764 */ for (Pair<Integer, String> itemPair : (Iterable<Pair<Integer, String>>) MapleItemInformationProvider.getInstance().getAllItems()) {
                /*  765 */ if (((String) itemPair.getRight()).toLowerCase().contains("헤어") || ((String) itemPair.getRight()).toLowerCase().contains("머리")) {
                    /*  766 */ hairlist.put(itemPair.getLeft(), itemPair.getRight());
                    continue;
                    /*  767 */                }
                if (((String) itemPair.getRight()).toLowerCase().contains("얼굴")) {
                    /*  768 */ facelist.put(itemPair.getLeft(), itemPair.getRight());
                }
            }
        }

        /*  773 */ int mixranze = 0;
        /*  774 */ if (args > 99999) {

            /*  776 */ mixranze = args % 1000;
            /*  777 */ args /= 1000;
        }
        /*  779 */ if (hairlist.containsKey(Integer.valueOf(args))) {
            /*  780 */ if (this.c.getPlayer().getDressup() == true) {
                /*  781 */ setAngelicSecondHair(args);
            } else {
                /*  783 */ this.c.getPlayer().setHair(args);
                /*  784 */ this.c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
            }
            /*  786 */        } else if (facelist.containsKey(Integer.valueOf(args))) {
            /*  787 */ if (mixranze > 0) {
                /*  788 */ String sum = args + "" + mixranze;
                /*  789 */ args = Integer.parseInt(sum);
            }
            /*  791 */ if (this.c.getPlayer().getDressup() == true) {
                /*  792 */ setAngelicSecondFace(args);
            } else {
                /*  794 */ this.c.getPlayer().setFace(args);
                /*  795 */ this.c.getPlayer().updateSingleStat(MapleStat.FACE, args);
            }
        } else {
            /*  798 */ if (args < 0) {
                /*  799 */ args = 0;
            }
            /*  801 */ if (this.c.getPlayer().getDressup() == true) {
                /*  802 */ setAngelicSecondSkin(args);
            } else {
                /*  804 */ this.c.getPlayer().setSkinColor((byte) args);
                /*  805 */ this.c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
            }
        }
        /*  808 */ this.c.getPlayer().equipChanged();

        /*  810 */ return 1;
    }

    public int setZeroAvatar(int ticket, int args1, int args2) {
        /*  814 */ int mixranze = 0, mixranze2 = 0;
        /*  815 */ if (args1 > 99999) {

            /*  817 */ mixranze = args1 % 1000;
            /*  818 */ args1 /= 1000;
        }
        /*  820 */ if (args2 > 99999) {

            /*  822 */ mixranze2 = args2 % 1000;
            /*  823 */ args2 /= 1000;
        }

        /*  826 */ if (hairlist.containsKey(Integer.valueOf(args1)) || hairlist.containsKey(Integer.valueOf(args2))) {
            /*  827 */ if (ticket == 0) {
                /*  828 */ this.c.getPlayer().setHair(args1);
                /*  829 */ this.c.getPlayer().updateSingleStat(MapleStat.HAIR, args1);
            } else {
                /*  831 */ this.c.getPlayer().setSecondHair(args2);
                /*  832 */ this.c.getPlayer().updateSingleStat(MapleStat.HAIR, args2);
                /*  833 */ this.c.getPlayer().fakeRelog();
            }
            /*  835 */        } else if (facelist.containsKey(Integer.valueOf(args1)) || facelist.containsKey(Integer.valueOf(args2))) {
            /*  836 */ if (mixranze > 0) {
                /*  837 */ String sum = args1 + "" + mixranze;
                /*  838 */ args1 = Integer.parseInt(sum);
            }
            /*  840 */ if (mixranze2 > 0) {
                /*  841 */ String sum = args2 + "" + mixranze;
                /*  842 */ args2 = Integer.parseInt(sum);
            }
            /*  844 */ if (ticket == 0) {
                /*  845 */ this.c.getPlayer().setFace(args1);
                /*  846 */ this.c.getPlayer().updateSingleStat(MapleStat.FACE, args1);
            } else {
                /*  848 */ this.c.getPlayer().setSecondFace(args2);
                /*  849 */ this.c.getPlayer().updateSingleStat(MapleStat.FACE, args2);
                /*  850 */ this.c.getPlayer().fakeRelog();
            }

            /*  853 */        } else if (ticket == 0) {
            /*  854 */ this.c.getPlayer().setSkinColor((byte) args1);
            /*  855 */ this.c.getPlayer().updateSingleStat(MapleStat.SKIN, args1);
        } else {
            /*  857 */ this.c.getPlayer().setSecondSkinColor((byte) args2);
            /*  858 */ this.c.getPlayer().updateSingleStat(MapleStat.SKIN, args2);
            /*  859 */ this.c.getPlayer().fakeRelog();
        }

        /*  862 */ this.c.getPlayer().equipChanged();
        /*  863 */ return 1;
    }

    public void setFaceAndroid(int faceId) {
        /*  867 */ this.c.getPlayer().getAndroid().setFace(faceId);
        /*  868 */ this.c.getPlayer().updateAndroid();
    }

    public void setHairAndroid(int hairId) {
        /*  872 */ this.c.getPlayer().getAndroid().setHair(hairId);
        /*  873 */ this.c.getPlayer().updateAndroid();
    }

    public void setSkinAndroid(int color) {
        /*  877 */ this.c.getPlayer().getAndroid().setSkin(color);
        /*  878 */ this.c.getPlayer().updateAndroid();
    }

    public void sendStorage() {
        /*  882 */ this.c.getPlayer().setStorageNPC(this.id);
        /*  883 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getStorage((byte) 0));
    }

    public void openShop(int idd) {
        /*  887 */ if (MapleShopFactory.getInstance().getShop(idd).getRechargeShop() == 1) {
            /*  888 */ boolean active = false, save = false;
            /*  889 */ Calendar ocal = Calendar.getInstance();
            /*  890 */ for (MapleShopItem item : MapleShopFactory.getInstance().getShop(idd).getItems()) {
                /*  891 */ int maxday = ocal.getActualMaximum(5);
                /*  892 */ int month = ocal.get(2) + 1;
                /*  893 */ int day = ocal.get(5);
                /*  894 */ if (item.getReCharge() > 0) {
                    /*  895 */ for (MapleShopLimit shl : this.c.getShopLimit()) {
                        /*  896 */ if (shl.getLastBuyMonth() > 0 && shl.getLastBuyDay() > 0) {
                            /*  897 */ Calendar baseCal = new GregorianCalendar(ocal.get(1), shl.getLastBuyMonth(), shl.getLastBuyDay());
                            /*  898 */ Calendar targetCal = new GregorianCalendar(ocal.get(1), month, day);
                            /*  899 */ long diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000L;
                            /*  900 */ long diffDays = diffSec / 86400L;
                            /*  901 */ if (shl.getItemid() == item.getItemId() && shl.getShopId() == MapleShopFactory.getInstance().getShop(idd).getId() && shl.getPosition() == item.getPosition() && diffDays >= 0L) {
                                /*  902 */ shl.setLastBuyMonth(0);
                                /*  903 */ shl.setLastBuyDay(0);
                                /*  904 */ shl.setLimitCountAcc(0);
                                /*  905 */ shl.setLimitCountChr(0);
                                /*  906 */ save = true;

                                break;
                            }
                        }
                    }

                    /*  913 */ if (item.getReChargeDay() <= day && item.getReChargeMonth() <= month) {
                        /*  914 */ active = true;
                        /*  915 */ int afterday = day + item.getReCharge();
                        /*  916 */ if (afterday > maxday) {
                            /*  917 */ afterday -= maxday;
                            /*  918 */ month++;
                            /*  919 */ if (month > 12) {
                                /*  920 */ month = 1;
                            }
                        }
                        /*  923 */ Connection con = null;
                        /*  924 */ PreparedStatement ps = null;
                        try {
                            /*  926 */ con = DatabaseConnection.getConnection();
                            /*  927 */ ps = con.prepareStatement("UPDATE shopitems SET rechargemonth = ?, rechargeday = ?, resetday = ? WHERE position = ? AND itemid = ? AND tab = ?");
                            /*  928 */ ps.setInt(1, month);
                            /*  929 */ ps.setInt(2, afterday);
                            /*  930 */ ps.setInt(3, day);
                            /*  931 */ ps.setInt(4, item.getPosition());
                            /*  932 */ ps.setInt(5, item.getItemId());
                            /*  933 */ ps.setByte(6, item.getTab());
                            /*  934 */ ps.executeUpdate();
                            /*  935 */ ps.close();
                            /*  936 */ con.close();
                            /*  937 */                        } catch (SQLException e) {
                            /*  938 */ e.printStackTrace();
                        } finally {
                            try {
                                /*  941 */ if (con != null) {
                                    /*  942 */ con.close();
                                }
                                /*  944 */ if (ps != null) {
                                    /*  945 */ ps.close();
                                }
                                /*  947 */                            } catch (SQLException e) {
                                /*  948 */ e.printStackTrace();
                            }
                        }
                    }
                }
            }
            /*  954 */ if (active) {
                /*  955 */ MapleShopFactory.getInstance().clear();
                /*  956 */ MapleShopFactory.getInstance().getShop(this.id);
                /*  957 */            } else if (save) {
                /*  958 */ this.c.saveShopLimit(this.c.getShopLimit());
            }
        }
        /*  961 */ MapleShopFactory.getInstance().getShop(idd).sendShop(this.c);
    }

    public int gainGachaponItem(int id, int quantity) {
        /*  965 */ return gainGachaponItem(id, quantity, this.c.getPlayer().getMap().getStreetName());
    }

    public int gainGachaponItem(int id, int quantity, String msg) {
        try {
            /*  970 */ if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                /*  971 */ return -1;
            }
            /*  973 */ Item item = MapleInventoryManipulator.addbyId_Gachapon(this.c, id, (short) quantity);

            /*  975 */ if (item == null) {
                /*  976 */ return -1;
            }
            /*  978 */ byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            /*  979 */ if (rareness > 0) {
                /*  980 */ World.Broadcast.broadcastMessage(CWvsContext.getGachaponMega(this.c.getPlayer().getName(), " : got a(n)", item, rareness, msg));
            }
            /*  982 */ this.c.getSession().writeAndFlush(CField.EffectPacket.showCharmEffect(this.c.getPlayer(), id, 1, true, ""));
            /*  983 */ return item.getItemId();
            /*  984 */        } catch (Exception e) {
            /*  985 */ e.printStackTrace();

            /*  987 */ return -1;
        }
    }

    public void changeJob(int job) {
        /*  991 */ this.c.getPlayer().changeJob(job);
    }

    public void startQuest(int idd) {
        /*  995 */ MapleQuest.getInstance(idd).start(getPlayer(), this.id);
    }

    public void completeQuest(int idd) {
        /*  999 */ MapleQuest.getInstance(idd).complete(getPlayer(), this.id);
    }

    public void forfeitQuest(int idd) {
        /* 1003 */ MapleQuest.getInstance(idd).forfeit(getPlayer());
    }

    public void forceStartQuest() {
        /* 1007 */ MapleQuest.getInstance(this.id2).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(int idd) {
        /* 1011 */ MapleQuest.getInstance(idd).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(String customData) {
        /* 1015 */ MapleQuest.getInstance(this.id2).forceStart(getPlayer(), getNpc(), customData);
    }

    public void forceCompleteQuest() {
        /* 1019 */ MapleQuest.getInstance(this.id2).forceComplete(getPlayer(), getNpc());
    }

    public void forceCompleteQuest(int idd) {
        /* 1023 */ MapleQuest.getInstance(idd).forceComplete(getPlayer(), getNpc());
    }

    public String getQuestCustomData(int id2) {
        /* 1027 */ return this.c.getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).getCustomData();
    }

    public void setQuestCustomData(int id2, String customData) {
        /* 1031 */ getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).setCustomData(customData);
    }

    public long getMeso() {
        /* 1035 */ return getPlayer().getMeso();
    }

    public void gainAp(int amount) {
        /* 1039 */ this.c.getPlayer().gainAp((short) amount);
    }

    public void expandInventory(byte type, int amt) {
        /* 1043 */ this.c.getPlayer().expandInventory(type, amt);
    }

    public void gainItemInStorages(int id) {
        /* 1048 */ Connection con = null;
        /* 1049 */ PreparedStatement ps = null;
        /* 1050 */ PreparedStatement ps2 = null;
        /* 1051 */ ResultSet rs = null;

        /* 1053 */ int itemid = 0;
        /* 1054 */ int str = 0, dex = 0, int_ = 0, luk = 0, watk = 0, matk = 0;
        /* 1055 */ int hp = 0, upg = 0, slot = 0;
        try {
            /* 1057 */ con = DatabaseConnection.getConnection();
            /* 1058 */ ps = con.prepareStatement("SELECT * FROM cashstorages WHERE id = ? and charid = ?");
            /* 1059 */ ps.setInt(1, id);
            /* 1060 */ ps.setInt(2, this.c.getPlayer().getId());
            /* 1061 */ rs = ps.executeQuery();

            /* 1063 */ if (rs.next()) {
                /* 1064 */ itemid = rs.getInt("itemid");
                /* 1065 */ str = rs.getInt("str");
                /* 1066 */ dex = rs.getInt("dex");
                /* 1067 */ int_ = rs.getInt("int_");
                /* 1068 */ luk = rs.getInt("luk");
                /* 1069 */ watk = rs.getInt("watk");
                /* 1070 */ matk = rs.getInt("matk");
                /* 1071 */ hp = rs.getInt("maxhp");
                /* 1072 */ upg = rs.getInt("upg");
                /* 1073 */ slot = rs.getInt("slot");
            }
            /* 1075 */ ps.close();
            /* 1076 */ rs.close();

            /* 1078 */ ps2 = con.prepareStatement("DELETE FROM cashstorages WHERE id = ?");
            /* 1079 */ ps2.setInt(1, id);
            /* 1080 */ ps2.executeUpdate();
            /* 1081 */ ps2.close();
            /* 1082 */ con.close();
            /* 1083 */        } catch (SQLException e) {
            /* 1084 */ e.printStackTrace();
        } finally {
            /* 1086 */ if (ps != null) {
                try {
                    /* 1088 */ ps.close();
                    /* 1089 */                } catch (SQLException e) {
                    /* 1090 */ e.printStackTrace();
                }
            }
            /* 1093 */ if (ps2 != null) {
                try {
                    /* 1095 */ ps2.close();
                    /* 1096 */                } catch (SQLException e) {
                    /* 1097 */ e.printStackTrace();
                }
            }
            /* 1100 */ if (rs != null) {
                try {
                    /* 1102 */ ps.close();
                    /* 1103 */                } catch (SQLException e) {
                    /* 1104 */ e.printStackTrace();
                }
            }
            /* 1107 */ if (con != null) {
                try {
                    /* 1109 */ con.close();
                    /* 1110 */                } catch (SQLException e) {
                    /* 1111 */ e.printStackTrace();
                }
            }
        }
        /* 1115 */ MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        /* 1116 */ Equip item = (Equip) ii.getEquipById(itemid);
        /* 1117 */ item.setStr((short) str);
        /* 1118 */ item.setDex((short) dex);
        /* 1119 */ item.setInt((short) int_);
        /* 1120 */ item.setLuk((short) luk);
        /* 1121 */ item.setWatk((short) watk);
        /* 1122 */ item.setMatk((short) matk);
        /* 1123 */ item.setHp((short) hp);
        /* 1124 */ item.setUpgradeSlots((byte) slot);
        /* 1125 */ item.setLevel((byte) upg);
        /* 1126 */ MapleInventoryManipulator.addbyItem(this.c, (Item) item);
    }

    public void StoreInStorages(int charid, int itemid, int str, int dex, int int_, int luk, int watk, int matk) {
        /* 1132 */ Connection con = null;
        /* 1133 */ PreparedStatement ps = null;

        try {
            /* 1136 */ con = DatabaseConnection.getConnection();
            /* 1137 */ ps = con.prepareStatement("INSERT INTO cashstorages (charid, itemid, str, dex, int_, luk, watk, matk) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            /* 1138 */ ps.setInt(1, charid);
            /* 1139 */ ps.setInt(2, itemid);
            /* 1140 */ ps.setInt(3, str);
            /* 1141 */ ps.setInt(4, dex);
            /* 1142 */ ps.setInt(5, int_);
            /* 1143 */ ps.setInt(6, luk);
            /* 1144 */ ps.setInt(7, watk);
            /* 1145 */ ps.setInt(8, matk);
            /* 1146 */ ps.executeUpdate();
            /* 1147 */ ps.close();
            /* 1148 */ con.close();
            /* 1149 */        } catch (SQLException e) {
            /* 1150 */ e.printStackTrace();
        } finally {
            /* 1152 */ if (ps != null) {
                try {
                    /* 1154 */ ps.close();
                    /* 1155 */                } catch (SQLException e) {
                    /* 1156 */ e.printStackTrace();
                }
            }
            /* 1159 */ if (con != null) {
                try {
                    /* 1161 */ con.close();
                    /* 1162 */                } catch (SQLException e) {
                    /* 1163 */ e.printStackTrace();
                }
            }
        }
    }

    public String getCashStorages(int charid) {
        /* 1170 */ String ret = "";

        /* 1172 */ Connection con = null;
        /* 1173 */ PreparedStatement ps = null;
        /* 1174 */ ResultSet rs = null;

        try {
            /* 1177 */ con = DatabaseConnection.getConnection();
            /* 1178 */ ps = con.prepareStatement("SELECT * FROM cashstorages WHERE charid = ?");
            /* 1179 */ ps.setInt(1, charid);
            /* 1180 */ rs = ps.executeQuery();

            /* 1182 */ if (rs.next()) {
                /* 1183 */ ret = ret + "#L" + rs.getInt("id") + "##i" + rs.getInt("itemid") + "##z" + rs.getInt("itemid") + "#\r\n";
            }
            /* 1185 */ rs.close();
            /* 1186 */ ps.close();
            /* 1187 */ con.close();
            /* 1188 */        } catch (SQLException e) {
            /* 1189 */ e.printStackTrace();
        } finally {
            /* 1191 */ if (rs != null) {
                try {
                    /* 1193 */ rs.close();
                    /* 1194 */                } catch (SQLException e) {
                    /* 1195 */ e.printStackTrace();
                }
            }
            /* 1198 */ if (ps != null) {
                try {
                    /* 1200 */ ps.close();
                    /* 1201 */                } catch (SQLException e) {
                    /* 1202 */ e.printStackTrace();
                }
            }
            /* 1205 */ if (con != null) {
                try {
                    /* 1207 */ con.close();
                    /* 1208 */                } catch (SQLException e) {
                    /* 1209 */ e.printStackTrace();
                }
            }
        }
        /* 1213 */ return ret;
    }

    public String getCharacterList(int accountid) {
        /* 1217 */ String ret = "";

        /* 1219 */ Connection con = null;
        /* 1220 */ PreparedStatement ps = null;
        /* 1221 */ ResultSet rs = null;

        try {
            /* 1224 */ con = DatabaseConnection.getConnection();
            /* 1225 */ ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = ?");
            /* 1226 */ ps.setInt(1, accountid);
            /* 1227 */ rs = ps.executeQuery();
            /* 1228 */ while (rs.next()) {
                /* 1229 */ ret = ret + "#L" + rs.getInt("id") + "#" + rs.getString("name") + "\r\n";
            }
            /* 1231 */ rs.close();
            /* 1232 */ ps.close();
            /* 1233 */ con.close();
            /* 1234 */        } catch (SQLException e) {
            /* 1235 */ e.printStackTrace();
        } finally {
            /* 1237 */ if (rs != null) {
                try {
                    /* 1239 */ rs.close();
                    /* 1240 */                } catch (SQLException e) {
                    /* 1241 */ e.printStackTrace();
                }
            }
            /* 1244 */ if (ps != null) {
                try {
                    /* 1246 */ ps.close();
                    /* 1247 */                } catch (SQLException e) {
                    /* 1248 */ e.printStackTrace();
                }
            }
            /* 1251 */ if (con != null) {
                try {
                    /* 1253 */ con.close();
                    /* 1254 */                } catch (SQLException e) {
                    /* 1255 */ e.printStackTrace();
                }
            }
        }
        /* 1259 */ return ret;
    }

    public final void clearSkills() {
        /* 1263 */ Map<Skill, SkillEntry> skills = new HashMap<>(getPlayer().getSkills());
        /* 1264 */ Map<Skill, SkillEntry> newList = new HashMap<>();
        /* 1265 */ for (Map.Entry<Skill, SkillEntry> skill : skills.entrySet()) {
            /* 1266 */ newList.put(skill.getKey(), new SkillEntry(0, (byte) 0, -1L));
        }
        /* 1268 */ getPlayer().changeSkillsLevel(newList);
        /* 1269 */ newList.clear();
        /* 1270 */ skills.clear();
    }

    public final void skillmaster() {
        /* 1274 */ MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(StringUtil.getLeftPaddedStr("" + this.c.getPlayer().getJob(), '0', 3) + ".img");
        /* 1275 */ for (MapleData skill : data) {
            /* 1276 */ if (skill != null) {
                /* 1277 */ for (MapleData skillId : skill.getChildren()) {
                    /* 1278 */ if (!skillId.getName().equals("icon")) {
                        /* 1279 */ byte maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                        /* 1280 */ if (maxLevel < 0) {
                            /* 1281 */ maxLevel = 1;
                        }
                        /* 1283 */ if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0
                                && /* 1284 */ this.c.getPlayer().getLevel() >= MapleDataTool.getIntConvert("reqLev", skillId, 0)) {
                            /* 1285 */ this.c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel, maxLevel);
                        }
                    }
                }
            }
        }

        /* 1292 */ if (GameConstants.isZero(this.c.getPlayer().getJob())) {
            /* 1293 */ int[] jobs = {10000, 10100, 10110, 10111, 10112};
            /* 1294 */ for (int job : jobs) {
                /* 1295 */ data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(job + ".img");
                /* 1296 */ for (MapleData skill : data) {
                    /* 1297 */ if (skill != null) {
                        /* 1298 */ for (MapleData skillId : skill.getChildren()) {
                            /* 1299 */ if (!skillId.getName().equals("icon")) {
                                /* 1300 */ byte maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                                /* 1301 */ if (maxLevel < 0) {
                                    /* 1302 */ maxLevel = 1;
                                }
                                /* 1304 */ if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0
                                        && /* 1305 */ this.c.getPlayer().getLevel() >= MapleDataTool.getIntConvert("reqLev", skillId, 0)) {
                                    /* 1306 */ this.c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel, maxLevel);
                                }
                            }
                        }
                    }
                }

                /* 1313 */ if (this.c.getPlayer().getLevel() >= 200) {
                    /* 1314 */ this.c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(100001005), 1, (byte) 1);
                }
            }
        }
        /* 1318 */ if (GameConstants.isKOC(this.c.getPlayer().getJob()) && this.c.getPlayer().getLevel() >= 100) {
            /* 1319 */ this.c.getPlayer().changeSkillLevel(11121000, (byte) 30, (byte) 30);
            /* 1320 */ this.c.getPlayer().changeSkillLevel(12121000, (byte) 30, (byte) 30);
            /* 1321 */ this.c.getPlayer().changeSkillLevel(13121000, (byte) 30, (byte) 30);
            /* 1322 */ this.c.getPlayer().changeSkillLevel(14121000, (byte) 30, (byte) 30);
            /* 1323 */ this.c.getPlayer().changeSkillLevel(15121000, (byte) 30, (byte) 30);
        }
    }

    public static void writeLog(String path, String data, boolean writeafterend) {
        try {
            /* 1329 */ File fFile = new File(path);
            /* 1330 */ if (!fFile.exists()) {
                /* 1331 */ fFile.createNewFile();
            }
            /* 1333 */ FileOutputStream out = new FileOutputStream(path, true);
            /* 1334 */ long time = System.currentTimeMillis();
            /* 1335 */ SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            /* 1336 */ String str = dayTime.format(new Date(time));
            /* 1337 */ String msg = "\r\n" + str + " | " + data;
            /* 1338 */ out.write(msg.getBytes());
            /* 1339 */ out.close();
            /* 1340 */ out.flush();
            /* 1341 */        } catch (IOException e) {
            /* 1342 */ e.printStackTrace();
        }
    }

    public void addBoss(String boss) {
        /* 1347 */ if (this.c.getPlayer().getParty() != null) {
            /* 1348 */ this.c.getPlayer().removeV("bossPractice");
            /* 1349 */ KoreaCalendar kc = new KoreaCalendar();
            /* 1350 */ String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
            /* 1351 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
                /* 1352 */ MapleCharacter ch = this.c.getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                /* 1353 */ if (ch != null) {

                    /* 1355 */ ch.removeV("bossPractice");
                    /* 1356 */ ch.removeV(boss);
                    /* 1357 */ ch.addKV(boss, (System.currentTimeMillis() + 1800000L) + "");
                    /* 1358 */ FileoutputUtil.log(FileoutputUtil.보스입장, "[입장] 계정 번호 : " + this.c.getAccID() + " | 파티번호 : " + chr.getPlayer().getParty().getId() + " | 캐릭터 : " + ch.getName() + "(" + ch.getId() + ") | 입장 보스 : " + boss);
                    /* 1359 */ switch (boss) {
                        case "Normal_Zakum":
                            /* 1361 */ MapleQuest.getInstance(7003).forceStart(ch, 0, today);
                            /* 1362 */ MapleQuest.getInstance(7004).forceStart(ch, 0, "1");
                        break; 
                        case "Normal_Hillah":
                            /* 1365 */ ch.updateInfoQuest(3981, "eNum=1;lastDate=" + today);
                        break; 
                        case "Normal_Kawoong":
                            /* 1368 */ MapleQuest.getInstance(3590).forceStart(ch, 0, today);
                            /* 1369 */ MapleQuest.getInstance(3591).forceStart(ch, 0, "1");
break; 
                        case "Easy_Populatus":
                        case "Normal_Populatus":
                            /* 1373 */ MapleQuest.getInstance(7200).forceStart(ch, 0, today);
                            /* 1374 */ MapleQuest.getInstance(7201).forceStart(ch, 0, "1");
break; 
                        case "Easy_VonLeon":
                        case "Normal_VonLeon":
                        case "Hard_VonLeon":
                            /* 1379 */ ch.updateInfoQuest(7850, "eNum=1;lastDate=" + today);
break; 
                        case "Normal_Horntail":
                        case "Chaos_Horntail":
                            /* 1383 */ ch.updateInfoQuest(7312, "eNum=1;lastDate=" + today);
break; 
                        case "Easy_Arkarium":
                        case "Normal_Arkarium":
                            /* 1387 */ ch.updateInfoQuest(7851, "eNum=1;lastDate=" + today);
break; 
                        case "Normal_Pinkbean":
                            /* 1390 */ ch.setKeyValue(7403, "eNum", "1");
                            /* 1391 */ ch.setKeyValue(7403, "lastDate", today);
break; 
                        case "Chaos_Pinkbean":
                            /* 1394 */ ch.setKeyValue(7403, "eNumC", "1");
                            /* 1395 */ ch.setKeyValue(7403, "lastDateC", today);
break; 
                        case "Hard_Lotus":
                            /* 1398 */ ch.setKeyValue(33126, "lastDate", today);
break; 
                        case "Hard_Demian":
                            /* 1401 */ ch.setKeyValue(34016, "lastDate", today);
break; 
                        case "Easy_Lucid":
                            /* 1404 */ ch.setKeyValue(34364, "eNumE", "1");
                            /* 1405 */ ch.setKeyValue(34364, "lastDateE", today);
break; 
                        case "Normal_Lucid":
                            /* 1408 */ ch.setKeyValue(34364, "eNum", "1");
                            /* 1409 */ ch.setKeyValue(34364, "lastDate", today);
break; 
                        case "Hard_Lucid":
                            /* 1412 */ ch.setKeyValue(34364, "eNumH", "1");
                            /* 1413 */ ch.setKeyValue(34364, "lastDateH", today);
break; 
                        case "Normal_Will":
                            /* 1416 */ ch.setKeyValue(35100, "lastDateN", today);
break; 
                        case "Hard_Will":
                            /* 1419 */ ch.setKeyValue(35100, "lastDate", today);
break; 
                        case "Normal_Dusk":
                            /* 1422 */ ch.setKeyValue(35137, "lastDateN", today);
                            /* 1423 */ ch.setKeyValue(35139, "lastDateH", today);
break; 
                        case "Chaos_Dusk":
                            /* 1426 */ ch.setKeyValue(35137, "lastDateN", today);
                            /* 1427 */ ch.setKeyValue(35139, "lastDateH", today);
break; 
                        case "Normal_JinHillah":
                            /* 1430 */ ch.setKeyValue(35260, "lastDate", today);
break; 
                        case "Normal_Dunkel":
                            /* 1433 */ ch.setKeyValue(35138, "lastDateN", today);
                            /* 1434 */ ch.setKeyValue(35140, "lastDateH", today);
break; 
                        case "Hard_Dunkel":
                            /* 1437 */ ch.setKeyValue(35138, "lastDateN", today);
                            /* 1438 */ ch.setKeyValue(35140, "lastDateH", today);
break; 
                        case "Black_Mage":
                            /* 1441 */ ch.setKeyValue(35377, "lastDate", today);
break; 
                        case "Hard_Seren":
                            /* 1444 */ ch.setKeyValue(39932, "lastDate", today);
                            break; 
                    }
                }
            }
        }
    }

    public void addBossPractice(String boss) {
        /* 1453 */ if (this.c.getPlayer().getParty() != null) {
            /* 1454 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
                /* 1455 */ MapleCharacter ch = this.c.getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                /* 1456 */ if (ch != null) {
                    /* 1457 */ ch.addKV("bossPractice", "1");
                    /* 1458 */ FileoutputUtil.log(FileoutputUtil.보스입장, "[연습모드 입장] 계정 번호 : " + this.c.getAccID() + " | 파티번호 : " + chr.getPlayer().getParty().getId() + " | 캐릭터 : " + this.c.getPlayer().getName() + "(" + this.c.getPlayer().getId() + ") | 입장 보스 : " + boss);
                }
            }
        }
    }

    public Object[] BossNotAvailableChrList(String boss, int limit) {
        /* 1465 */ Object[] arr = new Object[0];
        /* 1466 */ if (this.c.getPlayer().getParty() != null) {
            /* 1467 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
                /* 1468 */ for (ChannelServer channel : ChannelServer.getAllInstances()) {
                    /* 1469 */ MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                    /* 1470 */ if (ch != null && ch.getGMLevel() < 6) {
                        /* 1471 */ String k = ch.getV(boss);
                        /* 1472 */ int key = (k == null) ? 0 : Integer.parseInt(ch.getV(boss));
                        /* 1473 */ if (key >= limit - 1) {
                            /* 1474 */ arr = add(arr, new Object[]{ch.getName()});
                        }
                    }
                }
            }
        }
        /* 1480 */ return arr;
    }

    public Object[] LevelNotAvailableChrList(int level) {
        /* 1484 */ Object[] arr = new Object[0];
        /* 1485 */ if (this.c.getPlayer().getParty() != null) {
            /* 1486 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
                /* 1487 */ for (ChannelServer channel : ChannelServer.getAllInstances()) {
                    /* 1488 */ MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                    /* 1489 */ if (ch != null && ch.getGMLevel() < 6
                            && /* 1490 */ ch.getLevel() < level) {
                        /* 1491 */ arr = add(arr, new Object[]{ch.getName()});
                    }
                }
            }
        }

        /* 1497 */ return arr;
    }

    public static Object[] add(Object[] arr, Object... elements) {
        /* 1501 */ Object[] tempArr = new Object[arr.length + elements.length];
        /* 1502 */ System.arraycopy(arr, 0, tempArr, 0, arr.length);

        /* 1504 */ for (int i = 0; i < elements.length; i++) {
            /* 1505 */ tempArr[arr.length + i] = elements[i];
        }
        /* 1507 */ return tempArr;
    }

    public boolean partyhaveItem(int itemid, int qty) {
        /* 1512 */ if (this.c.getPlayer().getParty() != null) {
            /* 1513 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
                /* 1514 */ for (ChannelServer channel : ChannelServer.getAllInstances()) {
                    /* 1515 */ MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                    /* 1516 */ if (ch != null && ch.getGMLevel() <= 6) {
                        /* 1517 */ int getqty = itemQuantity(itemid);
                        /* 1518 */ if (getqty < qty) {
                            /* 1519 */ return false;
                        }
                    }
                }
            }
            /* 1524 */ return true;
        }
        /* 1526 */ return false;
    }

    public boolean isBossAvailable(String boss, int limit) {
        /* 1530 */ if (this.c.getPlayer().getParty() != null) {
            /* 1531 */ KoreaCalendar kc = new KoreaCalendar();
            /* 1532 */ String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
            /* 1533 */ Iterator<MaplePartyCharacter> iterator = getPlayer().getParty().getMembers().iterator();
            if (iterator.hasNext()) {
                MaplePartyCharacter chr = iterator.next();
                /* 1534 */ for (ChannelServer channel : ChannelServer.getAllInstances()) {
                    /* 1535 */ MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                    /* 1536 */ if (ch != null && !ch.isGM()) {
                        /* 1537 */ boolean enter = false, weekly = false;
                        /* 1538 */ String CheckS = "lastDate";
                        /* 1539 */ int checkenterquestid = 0, checkclearquestid = 0;
                        /* 1540 */ switch (boss) {
                            case "Normal_Zakum":
                                /* 1542 */ checkenterquestid = 7003;
                                break;
                            case "Chaos_Zakum":
                                /* 1545 */ checkclearquestid = 15166;
                                /* 1546 */ weekly = true;
                                break;
                            case "Easy_Magnus":
                            case "Normal_Magnus":
                                /* 1550 */ checkclearquestid = 3993;
                                break;
                            case "Hard_Magnus":
                                /* 1553 */ checkclearquestid = 3992;
                                /* 1554 */ weekly = true;
                                break;
                            case "Normal_Hillah":
                                /* 1557 */ checkenterquestid = 3981;
                                break;
                            case "Hard_Hillah":
                                /* 1560 */ checkclearquestid = 3650;
                                /* 1561 */ weekly = true;
                                break;
                            case "Normal_Kawoong":
                                /* 1564 */ checkenterquestid = 3590;
                                break;
                            case "Easy_Populatus":
                            case "Normal_Populatus":
                                /* 1568 */ checkenterquestid = 7200;
                                break;
                            case "Chaos_Populatus":
                                /* 1571 */ checkclearquestid = 3657;
                                /* 1572 */ weekly = true;
                                break;
                            case "Normal_Pierre":
                                /* 1575 */ checkclearquestid = 30032;
                                break;
                            case "Chaos_Pierre":
                                /* 1578 */ checkclearquestid = 30043;
                                /* 1579 */ weekly = true;
                                break;
                            case "Normal_VonBon":
                                /* 1582 */ checkclearquestid = 30039;
                                break;
                            case "Chaos_VonBon":
                                /* 1585 */ checkclearquestid = 30044;
                                /* 1586 */ weekly = true;
                                break;
                            case "Normal_BloodyQueen":
                                /* 1589 */ checkclearquestid = 30033;
                                break;
                            case "Chaos_BloodyQueen":
                                /* 1592 */ checkclearquestid = 30045;
                                /* 1593 */ weekly = true;
                                break;
                            case "Normal_Vellum":
                                /* 1596 */ checkclearquestid = 30041;
                                break;
                            case "Chaos_Vellum":
                                /* 1599 */ checkclearquestid = 30046;
                                /* 1600 */ weekly = true;
                                break;
                            case "Easy_VonLeon":
                            case "Normal_VonLeon":
                            case "Hard_VonLeon":
                                /* 1605 */ checkenterquestid = 7850;
                                break;
                            case "Normal_Horntail":
                            case "Chaos_Horntail":
                                /* 1609 */ checkenterquestid = 7312;
                                break;
                            case "Easy_Arkarium":
                            case "Normal_Arkarium":
                                /* 1613 */ checkenterquestid = 7851;
                                break;
                            case "Normal_Pinkbean":
                                /* 1616 */ checkenterquestid = 7403;
                                /* 1617 */ checkclearquestid = 3652;
                                break;
                            case "Chaos_Pinkbean":
                                /* 1620 */ checkenterquestid = 7403;
                                /* 1621 */ checkclearquestid = 3653;
                                /* 1622 */ weekly = true;
                                /* 1623 */ CheckS = "lastDateC";
                                break;
                            case "Easy_Cygnus":
                            case "Normal_Cygnus":
                                /* 1627 */ checkclearquestid = 31199;
                                /* 1628 */ weekly = true;
                                break;
                            case "Normal_Lotus":
                                /* 1631 */ checkclearquestid = 33303;
                                /* 1632 */ weekly = true;
                                break;
                            case "Hard_Lotus":
                                /* 1635 */ checkenterquestid = 33126;
                                /* 1636 */ checkclearquestid = 33303;
                                /* 1637 */ weekly = true;
                                break;
                            case "Normal_Demian":
                                /* 1640 */ checkclearquestid = 34017;
                                /* 1641 */ weekly = true;
                                break;
                            case "Hard_Demian":
                                /* 1644 */ checkenterquestid = 34016;
                                /* 1645 */ checkclearquestid = 34017;
                                /* 1646 */ weekly = true;
                                break;
                            case "Easy_Lucid":
                                /* 1649 */ checkenterquestid = 34364;
                                /* 1650 */ checkclearquestid = 3685;
                                /* 1651 */ CheckS = "lastDateE";
                                /* 1652 */ weekly = true;
                                break;
                            case "Normal_Lucid":
                                /* 1655 */ checkenterquestid = 34364;
                                /* 1656 */ checkclearquestid = 3685;
                                /* 1657 */ weekly = true;
                                break;
                            case "Hard_Lucid":
                                /* 1660 */ checkenterquestid = 34364;
                                /* 1661 */ checkclearquestid = 3685;
                                /* 1662 */ CheckS = "lastDateH";
                                /* 1663 */ weekly = true;
                                break;
                            case "Normal_Will":
                                /* 1666 */ checkenterquestid = 35100;
                                /* 1667 */ checkclearquestid = 3658;
                                /* 1668 */ weekly = true;
                                break;
                            case "Hard_Will":
                                /* 1671 */ checkenterquestid = 35100;
                                /* 1672 */ checkclearquestid = 3658;
                                /* 1673 */ CheckS = "lastDateN";
                                /* 1674 */ weekly = true;
                                break;
                            case "Normal_Dusk":
                                /* 1677 */ checkenterquestid = 35137;
                                /* 1678 */ checkclearquestid = 3680;
                                /* 1679 */ CheckS = "lastDateN";
                                /* 1680 */ weekly = true;
                                break;
                            case "Chaos_Dusk":
                                /* 1683 */ checkenterquestid = 35139;
                                /* 1684 */ checkclearquestid = 3680;
                                /* 1685 */ CheckS = "lastDateH";
                                /* 1686 */ weekly = true;
                                break;
                            case "Normal_JinHillah":
                                /* 1689 */ checkenterquestid = 35260;
                                /* 1690 */ checkclearquestid = 3673;
                                /* 1691 */ weekly = true;
                                break;
                            case "Normal_Dunkel":
                                /* 1694 */ checkenterquestid = 35138;
                                /* 1695 */ checkclearquestid = 3681;
                                /* 1696 */ CheckS = "lastDateN";
                                /* 1697 */ weekly = true;
                                break;
                            case "Hard_Dunkel":
                                /* 1700 */ checkenterquestid = 35140;
                                /* 1701 */ checkclearquestid = 3681;
                                /* 1702 */ CheckS = "lastDateH";
                                /* 1703 */ weekly = true;
                                break;
                            case "Black_Mage":
                                /* 1706 */ checkenterquestid = 35377;
                                /* 1707 */ checkclearquestid = 3679;
                                /* 1708 */ weekly = true;
                                break;
                            case "Hard_Seren":
                                /* 1711 */ checkenterquestid = 39932;
                                /* 1712 */ checkclearquestid = 3687;
                                /* 1713 */ weekly = true;
                                break;
                        }
                        /* 1716 */ if (checkenterquestid > 0) {
                            /* 1718 */ if (chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS) != null
                                    && /* 1719 */ chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS).equals(today)) {
                                /* 1721 */ enter = true;
                            }
                        }

                        /* 1725 */ if (!enter) {
                            /* 1726 */ MapleQuestStatus quests = (MapleQuestStatus) chr.getPlayer().getQuest_Map().get(MapleQuest.getInstance(checkenterquestid));
                            /* 1727 */ if (quests != null
                                    && /* 1728 */ quests.getCustomData() != null
                                    && /* 1729 */ quests.getCustomData().equals(today)) {
                                /* 1731 */ enter = true;
                            }
                        }

                        /* 1737 */ if (checkclearquestid > 0 && !enter) {

                            /* 1739 */ if (weekly) {

                                /* 1741 */ if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null) {
                                    /* 1742 */ String[] array = chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime").split("/");
                                    /* 1743 */ Calendar clear = new GregorianCalendar(Integer.parseInt("20" + array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
                                    /* 1744 */ Calendar ocal = Calendar.getInstance();
                                    /* 1745 */ int yeal = clear.get(1), days = clear.get(5), day = ocal.get(7), day2 = clear.get(7), maxday = clear.getMaximum(5), month = clear.get(2);
                                    /* 1746 */ int check = (day2 == 5) ? 7 : ((day2 == 6) ? 6 : ((day2 == 7) ? 5 : 0));
                                    /* 1747 */ if (check == 0) {
                                        /* 1748 */ for (int i = day2; i < 5; i++) {
                                            /* 1749 */ check++;
                                        }
                                    }
                                    /* 1752 */ int afterday = days + check;
                                    /* 1753 */ if (afterday > maxday) {
                                        /* 1754 */ afterday -= maxday;
                                        /* 1755 */ month++;
                                    }
                                    /* 1757 */ if (month > 12) {
                                        /* 1758 */ yeal++;
                                        /* 1759 */ month = 1;
                                    }
                                    /* 1761 */ Calendar after = new GregorianCalendar(yeal, month, afterday);
                                    /* 1762 */ if (after.getTimeInMillis() > System.currentTimeMillis()) {
                                        /* 1763 */ enter = true;
                                    }
                                }

                            } /* 1768 */ else if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null
                                    && /* 1769 */ chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime").equals(today)) {
                                /* 1770 */ enter = true;
                            }

                            /* 1775 */ if (!enter) {
                                /* 1777 */ if (ch.getV(boss) != null
                                        && /* 1778 */ Long.parseLong(ch.getV(boss)) - System.currentTimeMillis() >= 0L) {
                                    /* 1779 */ enter = true;
                                }
                            }
                        }

                        /* 1785 */ if (enter) {
                            /* 1786 */ return false;
                        }
                    }
                }
                /* 1790 */ return true;
            }

        }
        /* 1793 */ return false;
    }

    public String isBossString(String boss) {
        /* 1797 */ String txt = "파티원 중 #r입장 조건#k을 충족하지 못하는 파티원이 있습니다.\r\n모든 파티원이 조건을 충족해야 입장이 가능합니다.\r\n\r\n";
        /* 1798 */ if (this.c.getPlayer().getParty() != null) {
            /* 1799 */ KoreaCalendar kc = new KoreaCalendar();
            /* 1800 */ String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
            /* 1801 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
                /* 1802 */ for (ChannelServer channel : ChannelServer.getAllInstances()) {
                    /* 1803 */ MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                    /* 1804 */ if (ch != null && !ch.isGM()) {
                        /* 1805 */ boolean enter = false, weekly = false;
                        /* 1806 */ String CheckS = "lastDate";
                        /* 1807 */ int checkenterquestid = 0, checkclearquestid = 0;
                        /* 1808 */ switch (boss) {
                            case "Normal_Zakum":
                                /* 1810 */ checkenterquestid = 7003;
                                break;
                            case "Chaos_Zakum":
                                /* 1813 */ checkclearquestid = 15166;
                                /* 1814 */ weekly = true;
                                break;
                            case "Easy_Magnus":
                            case "Normal_Magnus":
                                /* 1818 */ checkclearquestid = 3993;
                                break;
                            case "Hard_Magnus":
                                /* 1821 */ checkclearquestid = 3992;
                                /* 1822 */ weekly = true;
                                break;
                            case "Normal_Hillah":
                                /* 1825 */ checkenterquestid = 3981;
                                break;
                            case "Hard_Hillah":
                                /* 1828 */ checkclearquestid = 3650;
                                /* 1829 */ weekly = true;
                                break;
                            case "Normal_Kawoong":
                                /* 1832 */ checkenterquestid = 3590;
                                break;
                            case "Easy_Populatus":
                            case "Normal_Populatus":
                                /* 1836 */ checkenterquestid = 7200;
                                break;
                            case "Chaos_Populatus":
                                /* 1839 */ checkclearquestid = 3657;
                                /* 1840 */ weekly = true;
                                break;
                            case "Normal_Pierre":
                                /* 1843 */ checkclearquestid = 30032;
                                break;
                            case "Chaos_Pierre":
                                /* 1846 */ checkclearquestid = 30043;
                                /* 1847 */ weekly = true;
                                break;
                            case "Normal_VonBon":
                                /* 1850 */ checkclearquestid = 30039;
                                break;
                            case "Chaos_VonBon":
                                /* 1853 */ checkclearquestid = 30044;
                                /* 1854 */ weekly = true;
                                break;
                            case "Normal_BloodyQueen":
                                /* 1857 */ checkclearquestid = 30033;
                                break;
                            case "Chaos_BloodyQueen":
                                /* 1860 */ checkclearquestid = 30045;
                                /* 1861 */ weekly = true;
                                break;
                            case "Normal_Vellum":
                                /* 1864 */ checkclearquestid = 30041;
                                break;
                            case "Chaos_Vellum":
                                /* 1867 */ checkclearquestid = 30046;
                                /* 1868 */ weekly = true;
                                break;
                            case "Easy_VonLeon":
                            case "Normal_VonLeon":
                            case "Hard_VonLeon":
                                /* 1873 */ checkenterquestid = 7850;
                                break;
                            case "Normal_Horntail":
                            case "Chaos_Horntail":
                                /* 1877 */ checkenterquestid = 7312;
                                break;
                            case "Easy_Arkarium":
                            case "Normal_Arkarium":
                                /* 1881 */ checkenterquestid = 7851;
                                break;
                            case "Normal_Pinkbean":
                                /* 1884 */ checkenterquestid = 7403;
                                /* 1885 */ checkclearquestid = 3652;
                                break;
                            case "Chaos_Pinkbean":
                                /* 1888 */ checkenterquestid = 7403;
                                /* 1889 */ checkclearquestid = 3653;
                                /* 1890 */ weekly = true;
                                /* 1891 */ CheckS = "lastDateC";
                                break;
                            case "Easy_Cygnus":
                            case "Normal_Cygnus":
                                /* 1895 */ checkclearquestid = 31199;
                                /* 1896 */ weekly = true;
                                break;
                            case "Normal_Lotus":
                                /* 1899 */ checkclearquestid = 33303;
                                /* 1900 */ weekly = true;
                                break;
                            case "Hard_Lotus":
                                /* 1903 */ checkenterquestid = 33126;
                                /* 1904 */ checkclearquestid = 33303;
                                /* 1905 */ weekly = true;
                                break;
                            case "Normal_Demian":
                                /* 1908 */ checkclearquestid = 34017;
                                /* 1909 */ weekly = true;
                                break;
                            case "Hard_Demian":
                                /* 1912 */ checkenterquestid = 34016;
                                /* 1913 */ checkclearquestid = 34017;
                                /* 1914 */ weekly = true;
                                break;
                            case "Easy_Lucid":
                                /* 1917 */ checkenterquestid = 34364;
                                /* 1918 */ checkclearquestid = 3685;
                                /* 1919 */ CheckS = "lastDateE";
                                /* 1920 */ weekly = true;
                                break;
                            case "Normal_Lucid":
                                /* 1923 */ checkenterquestid = 34364;
                                /* 1924 */ checkclearquestid = 3685;
                                /* 1925 */ weekly = true;
                                break;
                            case "Hard_Lucid":
                                /* 1928 */ checkenterquestid = 34364;
                                /* 1929 */ checkclearquestid = 3685;
                                /* 1930 */ CheckS = "lastDateH";
                                /* 1931 */ weekly = true;
                                break;
                            case "Normal_Will":
                                /* 1934 */ checkenterquestid = 35100;
                                /* 1935 */ checkclearquestid = 3658;
                                /* 1936 */ CheckS = "lastDateN";
                                /* 1937 */ weekly = true;
                                break;
                            case "Hard_Will":
                                /* 1940 */ checkenterquestid = 35100;
                                /* 1941 */ checkclearquestid = 3658;
                                /* 1942 */ weekly = true;
                                break;
                            case "Normal_Dusk":
                                /* 1945 */ checkenterquestid = 35137;
                                /* 1946 */ checkclearquestid = 3680;
                                /* 1947 */ CheckS = "lastDateN";
                                /* 1948 */ weekly = true;
                                break;
                            case "Chaos_Dusk":
                                /* 1951 */ checkenterquestid = 35139;
                                /* 1952 */ checkclearquestid = 3680;
                                /* 1953 */ CheckS = "lastDateH";
                                /* 1954 */ weekly = true;
                                break;
                            case "Normal_JinHillah":
                                /* 1957 */ checkenterquestid = 35260;
                                /* 1958 */ checkclearquestid = 3673;
                                /* 1959 */ weekly = true;
                                break;
                            case "Normal_Dunkel":
                                /* 1962 */ checkenterquestid = 35138;
                                /* 1963 */ checkclearquestid = 3681;
                                /* 1964 */ CheckS = "lastDateN";
                                /* 1965 */ weekly = true;
                                break;
                            case "Hard_Dunkel":
                                /* 1968 */ checkenterquestid = 35140;
                                /* 1969 */ checkclearquestid = 3681;
                                /* 1970 */ CheckS = "lastDateH";
                                /* 1971 */ weekly = true;
                                break;
                            case "Black_Mage":
                                /* 1974 */ checkenterquestid = 35377;
                                /* 1975 */ checkclearquestid = 3679;
                                /* 1976 */ weekly = true;
                                break;
                            case "Hard_Seren":
                                /* 1979 */ checkenterquestid = 39932;
                                /* 1980 */ checkclearquestid = 3687;
                                /* 1981 */ weekly = true;
                                break;
                        }
                        /* 1984 */ if (checkenterquestid > 0) {

                            /* 1986 */ if (chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS) != null
                                    && /* 1987 */ chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS).equals(today)) {

                                /* 1989 */ enter = true;
                                /* 1990 */ txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
                            }

                            /* 1993 */ if (!enter) {
                                /* 1994 */ MapleQuestStatus quests = (MapleQuestStatus) chr.getPlayer().getQuest_Map().get(MapleQuest.getInstance(checkenterquestid));
                                /* 1995 */ if (quests != null
                                        && /* 1996 */ quests.getCustomData() != null
                                        && /* 1997 */ quests.getCustomData().equals(today)) {

                                    /* 1999 */ enter = true;
                                    /* 2000 */ txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
                                }
                            }
                        }

                        /* 2007 */ if (checkclearquestid > 0 && !enter) {

                            /* 2009 */ if (weekly) {

                                /* 2011 */ if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null) {
                                    /* 2012 */ String[] array = chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime").split("/");
                                    /* 2013 */ Calendar clear = new GregorianCalendar(Integer.parseInt("20" + array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
                                    /* 2014 */ Calendar ocal = Calendar.getInstance();
                                    /* 2015 */ int yeal = clear.get(1), days = clear.get(5), day = ocal.get(7), day2 = clear.get(7), maxday = clear.getMaximum(5), month = clear.get(2);
                                    /* 2016 */ int check = (day2 == 5) ? 7 : ((day2 == 6) ? 6 : ((day2 == 7) ? 5 : 0));
                                    /* 2017 */ if (check == 0) {
                                        /* 2018 */ for (int i = day2; i < 5; i++) {
                                            /* 2019 */ check++;
                                        }
                                    }
                                    /* 2022 */ int afterday = days + check;
                                    /* 2023 */ if (afterday > maxday) {
                                        /* 2024 */ afterday -= maxday;
                                        /* 2025 */ month++;
                                    }
                                    /* 2027 */ if (month > 12) {
                                        /* 2028 */ yeal++;
                                        /* 2029 */ month = 1;
                                    }
                                    /* 2031 */ Calendar after = new GregorianCalendar(yeal, month, afterday);
                                    /* 2032 */ if (after.getTimeInMillis() > System.currentTimeMillis()) {
                                        /* 2033 */ enter = true;
                                        /* 2034 */ txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
                                    }

                                }

                                /* 2039 */                            } else if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null
                                    && /* 2040 */ chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime").equals(today)) {
                                /* 2041 */ enter = true;
                                /* 2042 */ txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
                            }

                            /* 2047 */ if (!enter) {
                                /* 2049 */ if (Long.parseLong(ch.getV(boss)) - System.currentTimeMillis() >= 0L) {
                                    /* 2050 */ enter = true;
                                    /* 2051 */ txt = txt + "#b" + chr.getName() + "#k님 #e#r" + ((Long.parseLong(ch.getV(boss)) - System.currentTimeMillis()) / 1000L / 60L) + "분" + ((Long.parseLong(ch.getV(boss)) - System.currentTimeMillis()) / 1000L % 60L) + "초#k#n 뒤에 입장 가능합니다.\r\n";
                                }
                            }
                        }
                    }
                }
            }
        }
        /* 2059 */ return txt;
    }

    public boolean isLevelAvailable(int level) {
        /* 2063 */ if (this.c.getPlayer().getParty() != null) {
            /* 2064 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
                /* 2065 */ for (ChannelServer channel : ChannelServer.getAllInstances()) {
                    /* 2066 */ MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                    /* 2067 */ if (ch != null && ch.getGMLevel() <= 6
                            && /* 2068 */ ch.getLevel() < level) {
                        /* 2069 */ return false;
                    }
                }
            }

            /* 2074 */ return true;
        }
        /* 2076 */ return false;
    }

    public boolean hasSkill(int skillid) {
        /* 2080 */ Skill theSkill = SkillFactory.getSkill(skillid);
        /* 2081 */ if (theSkill != null) {
            /* 2082 */ return (this.c.getPlayer().getSkillLevel(theSkill) > 0);
        }
        /* 2084 */ return false;
    }

    public void showEffect(boolean broadcast, String effect) {
        /* 2088 */ if (broadcast) {
            /* 2089 */ this.c.getPlayer().getMap().broadcastMessage(CField.showEffect(effect));
        } else {
            /* 2091 */ this.c.getSession().writeAndFlush(CField.showEffect(effect));
        }
    }

    public void playSound(boolean broadcast, String sound) {
        /* 2096 */ if (broadcast) {
            /* 2097 */ this.c.getPlayer().getMap().broadcastMessage(CField.playSound(sound));
        } else {
            /* 2099 */ this.c.getSession().writeAndFlush(CField.playSound(sound));
        }
    }

    public void environmentChange(boolean broadcast, String env) {
        /* 2104 */ if (broadcast) {
            /* 2105 */ this.c.getPlayer().getMap().broadcastMessage(CField.environmentChange(env, 2));
        } else {
            /* 2107 */ this.c.getSession().writeAndFlush(CField.environmentChange(env, 2));
        }
    }

    public void updateBuddyCapacity(int capacity) {
        /* 2112 */ this.c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    public int getBuddyCapacity() {
        /* 2116 */ return this.c.getPlayer().getBuddyCapacity();
    }

    public int partyMembersInMap() {
        /* 2120 */ int inMap = 0;
        /* 2121 */ if (getPlayer().getParty() == null) {
            /* 2122 */ return inMap;
        }
        /* 2124 */ for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            /* 2125 */ if (char2.getParty() != null && char2.getParty().getId() == getPlayer().getParty().getId()) {
                /* 2126 */ inMap++;
            }
        }
        /* 2129 */ return inMap;
    }

    public List<MapleCharacter> getPartyMembers() {
        /* 2133 */ if (getPlayer().getParty() == null) {
            /* 2134 */ return null;
        }
        /* 2136 */ List<MapleCharacter> chars = new LinkedList<>();
        /* 2137 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            /* 2138 */ for (ChannelServer channel : ChannelServer.getAllInstances()) {
                /* 2139 */ MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                /* 2140 */ if (ch != null) {
                    /* 2141 */ chars.add(ch);
                }
            }
        }
        /* 2145 */ return chars;
    }

    public void warpPartyWithExp(int mapId, int exp) {
        /* 2149 */ if (getPlayer().getParty() == null) {
            /* 2150 */ warp(mapId, 0);
            /* 2151 */ gainExp(exp);
            return;
        }
        /* 2154 */ MapleMap target = getMap(mapId);
        /* 2155 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            /* 2156 */ MapleCharacter curChar = this.c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            /* 2157 */ if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                /* 2158 */ curChar.changeMap(target, target.getPortal(0));
                /* 2159 */ curChar.gainExp(exp, true, false, true);
            }
        }
    }

    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
        /* 2165 */ if (getPlayer().getParty() == null) {
            /* 2166 */ warp(mapId, 0);
            /* 2167 */ gainExp(exp);
            /* 2168 */ gainMeso(meso);
            return;
        }
        /* 2171 */ MapleMap target = getMap(mapId);
        /* 2172 */ for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            /* 2173 */ MapleCharacter curChar = this.c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            /* 2174 */ if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                /* 2175 */ curChar.changeMap(target, target.getPortal(0));
                /* 2176 */ curChar.gainExp(exp, true, false, true);
                /* 2177 */ curChar.gainMeso(meso, true);
            }
        }
    }

    public MapleCharacter getChar(int id) {
        /* 2183 */ MapleCharacter chr = null;
        /* 2184 */ for (ChannelServer cs : ChannelServer.getAllInstances()) {
            /* 2185 */ chr = cs.getPlayerStorage().getCharacterById(id);
            /* 2186 */ if (chr != null) {
                /* 2187 */ return chr;
            }
        }
        /* 2190 */ return null;
    }

    public void makeRing(int itemid, MapleCharacter chr) {
        try {
            /* 2195 */ MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            /* 2196 */ Item item = ii.getEquipById(itemid);
            /* 2197 */ Item item1 = ii.getEquipById(itemid);
            /* 2198 */ item.setUniqueId(MapleInventoryIdentifier.getInstance());
            /* 2199 */ item1.setUniqueId(MapleInventoryIdentifier.getInstance());
            /* 2200 */ MapleRing.makeRing(itemid, chr, item.getUniqueId(), item1.getUniqueId());
            /* 2201 */ MapleRing.makeRing(itemid, getPlayer(), item1.getUniqueId(), item.getUniqueId());
            /* 2202 */ MapleInventoryManipulator.addbyItem(getClient(), item);
            /* 2203 */ MapleInventoryManipulator.addbyItem(chr.getClient(), item1);
            /* 2204 */ chr.reloadChar();
            /* 2205 */ this.c.getPlayer().reloadChar();
            /* 2206 */ sendOk("선택하신 반지를 제작 완료 하였습니다. 인벤토리를 확인해 봐주시길 바랍니다.");
            /* 2207 */ chr.dropMessage(5, getPlayer().getName() + "님으로 부터 반지가 도착 하였습니다. 인벤토리를 확인해 주시길 바랍니다.");
            /* 2208 */        } catch (Exception ex) {
            /* 2209 */ sendOk("반지를 제작하는데 오류가 발생 하였습니다.");
        }
    }

    public void makeRingRC(int itemid, MapleCharacter chr) {
        try {
            /* 2215 */ MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            /* 2216 */ Item item = ii.getEquipById(itemid);
            /* 2217 */ Item item1 = ii.getEquipById(itemid);
            /* 2218 */ item.setUniqueId(MapleInventoryIdentifier.getInstance());
            /* 2219 */ Equip eitem = (Equip) item;
            /* 2220 */ eitem.setStr((short) 300);
            /* 2221 */ eitem.setDex((short) 300);
            /* 2222 */ eitem.setInt((short) 300);
            /* 2223 */ eitem.setLuk((short) 300);
            /* 2224 */ eitem.setWatk((short) 300);
            /* 2225 */ eitem.setMatk((short) 300);
            /* 2226 */ item1.setUniqueId(MapleInventoryIdentifier.getInstance());
            /* 2227 */ Equip eitem1 = (Equip) item1;
            /* 2228 */ eitem1.setStr((short) 300);
            /* 2229 */ eitem1.setDex((short) 300);
            /* 2230 */ eitem1.setInt((short) 300);
            /* 2231 */ eitem1.setLuk((short) 300);
            /* 2232 */ eitem1.setWatk((short) 300);
            /* 2233 */ eitem1.setMatk((short) 300);
            /* 2234 */ MapleRing.makeRing(itemid, chr, eitem.getUniqueId(), eitem1.getUniqueId());
            /* 2235 */ MapleRing.makeRing(itemid, getPlayer(), eitem1.getUniqueId(), eitem.getUniqueId());
            /* 2236 */ MapleInventoryManipulator.addbyItem(getClient(), item);
            /* 2237 */ MapleInventoryManipulator.addbyItem(chr.getClient(), item1);
            /* 2238 */ chr.reloadChar();
            /* 2239 */ this.c.getPlayer().reloadChar();
            /* 2240 */ sendOk("선택하신 반지를 제작 완료 하였습니다. 인벤토리를 확인해 봐주시길 바랍니다.");
            /* 2241 */ chr.dropMessage(5, getPlayer().getName() + "님으로 부터 반지가 도착 하였습니다. 인벤토리를 확인해 주시길 바랍니다.");
            /* 2242 */        } catch (Exception ex) {
            /* 2243 */ sendOk("반지를 제작하는데 오류가 발생 하였습니다.");
        }
    }

    public void makeRingHB(int itemid, MapleCharacter chr) {
        try {
            /* 2249 */ int asd = 300;
            /* 2250 */ int asd2 = 300;
            /* 2251 */ MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            /* 2252 */ Item item = ii.getEquipById(itemid);
            /* 2253 */ Item item1 = ii.getEquipById(itemid);
            /* 2254 */ item.setUniqueId(MapleInventoryIdentifier.getInstance());
            /* 2255 */ Equip eitem = (Equip) item;
            /* 2256 */ eitem.setStr((short) asd);
            /* 2257 */ eitem.setDex((short) asd);
            /* 2258 */ eitem.setInt((short) asd);
            /* 2259 */ eitem.setLuk((short) asd);
            /* 2260 */ eitem.setWatk((short) asd2);
            /* 2261 */ eitem.setMatk((short) asd2);
            /* 2262 */ item1.setUniqueId(MapleInventoryIdentifier.getInstance());
            /* 2263 */ Equip eitem1 = (Equip) item1;
            /* 2264 */ eitem1.setStr((short) asd);
            /* 2265 */ eitem1.setDex((short) asd);
            /* 2266 */ eitem1.setInt((short) asd);
            /* 2267 */ eitem1.setLuk((short) asd);
            /* 2268 */ eitem1.setWatk((short) asd2);
            /* 2269 */ eitem1.setMatk((short) asd2);
            /* 2270 */ MapleRing.makeRing(itemid, chr, eitem.getUniqueId(), eitem1.getUniqueId());
            /* 2271 */ MapleRing.makeRing(itemid, getPlayer(), eitem1.getUniqueId(), eitem.getUniqueId());
            /* 2272 */ MapleInventoryManipulator.addbyItem(getClient(), item);
            /* 2273 */ MapleInventoryManipulator.addbyItem(chr.getClient(), item1);
            /* 2274 */ chr.reloadChar();
            /* 2275 */ this.c.getPlayer().reloadChar();
            /* 2276 */ sendOk("선택하신 반지를 제작 완료 하였습니다. 인벤토리를 확인해 봐주시길 바랍니다.");
            /* 2277 */ chr.dropMessage(5, getPlayer().getName() + "님으로 부터 반지가 도착 하였습니다. 인벤토리를 확인해 주시길 바랍니다.");
            /* 2278 */        } catch (Exception ex) {
            /* 2279 */ sendOk("반지를 제작하는데 오류가 발생 하였습니다.");
        }
    }

    public void MapiaStart(final MapleCharacter player, int time, final int morningmap, final int citizenmap1, final int citizenmap2, final int citizenmap3, final int citizenmap4, final int citizenmap5, final int citizenmap6, final int mapiamap, final int policemap, final int drmap, final int after, final int night, final int vote, int bating) {
        /* 2284 */ String[] job = {"시민", "마피아", "경찰", "의사", "시민", "시민", "마피아", "경찰", "시민", "마피아"};
        /* 2285 */ String name = "";
        /* 2286 */ String mapia = "";
        /* 2287 */ String police = "";
        /* 2288 */ int playernum = 0;
        /* 2289 */ int citizennumber = 0;

        /* 2292 */ final MapleMap map = ChannelServer.getInstance(getClient().getChannel()).getMapFactory().getMap(morningmap);
        /* 2293 */ for (MapleCharacter chr : player.getMap().getCharacters()) {
            /* 2294 */ playernum++;
        }
        /* 2296 */ int[] iNumber = new int[playernum];
        int i;
        /* 2297 */ for (i = 1; i <= iNumber.length; i++) {
            /* 2298 */ iNumber[i - 1] = i;
        }
        /* 2300 */ for (i = 0; i < iNumber.length; i++) {
            /* 2301 */ int iRandom = (int) (Math.random() * playernum);
            /* 2302 */ int t = iNumber[0];
            /* 2303 */ iNumber[0] = iNumber[iRandom];
            /* 2304 */ iNumber[iRandom] = t;
        }
        /* 2306 */ for (i = 0; i < iNumber.length; i++) {
            /* 2307 */ System.out.print(iNumber[i] + ",");
        }
        /* 2309 */ int jo = 0;
        /* 2310 */ map.names = "";
        /* 2311 */ map.mbating = bating * playernum;
        /* 2312 */ for (MapleCharacter chr : player.getMap().getCharacters()) {
            /* 2313 */ chr.warp(morningmap);
            /* 2314 */ map.names += chr.getName() + ",";
            /* 2315 */ chr.mapiajob = job[iNumber[jo] - 1];
            /* 2316 */ if (chr.mapiajob.equals("마피아")) {
                /* 2317 */ mapia = mapia + chr.getName() + ",";
                /* 2318 */            } else if (chr.mapiajob.equals("경찰")) {
                /* 2319 */ police = police + chr.getName() + ",";
                /* 2320 */            } else if (chr.mapiajob.equals("시민")) {
                /* 2321 */ citizennumber++;
            }
            /* 2323 */ chr.dropMessage(5, "잠시 후 마피아 게임이 시작됩니다. 총 배팅금은 " + (bating * playernum) + "메소 입니다.");
            /* 2324 */ chr.dropMessage(5, "당신의 직업은 " + job[iNumber[jo] - 1] + " 입니다.");
            /* 2325 */ chr.dropMessage(-1, time + "초 후 마피아 게임이 시작됩니다.");
            /* 2326 */ jo++;
        }
        /* 2328 */ final String mapialist = mapia;
        /* 2329 */ final String policelist = police;
        /* 2330 */ int citizennum = citizennumber;
        /* 2331 */ final int playernuma = playernum;
        /* 2332 */ final Timer m_timer = new Timer();
        /* 2333 */ TimerTask m_task = new TimerTask() {
            public void run() {
                /* 2335 */ for (MapleCharacter chr : player.getMap().getCharacters()) {
                    /* 2336 */ if (chr.mapiajob == "마피아") {
                        /* 2337 */ chr.isMapiaVote = true;
                        /* 2338 */ chr.dropMessage(6, "마피아인 당신 동료는 " + mapialist + " 들이 있습니다. 밤이되면 같이 의논하여 암살할 사람을 선택해 주시기 바랍니다.");
                        /* 2339 */                    } else if (chr.mapiajob == "경찰") {
                        /* 2340 */ chr.isPoliceVote = true;
                        /* 2341 */ chr.dropMessage(6, "경찰인 당신 동료는 " + policelist + " 들이 있습니다. 밤이되면 마피아같다는 사람을 지목하면 마피아인지 아닌지를 알 수 있습니다.");
                        /* 2342 */                    } else if (chr.mapiajob == "의사") {
                        /* 2343 */ chr.isDrVote = true;
                        /* 2344 */ chr.dropMessage(6, "당신은 하나밖에 없는 의사입니다. 당신에게 부여된 임무는 시민과 경찰을 살리는 것입니다. 밤이되면 마피아가 지목했을것 같은 사람을 선택하면 살리실 수 있습니다.");
                        /* 2345 */                    } else if (chr.mapiajob == "시민") {
                        /* 2346 */ chr.dropMessage(6, "당신은 시민입니다. 낮이되면 대화를 통해 마피아를 찾아내 투표로 처형시키면 됩니다.");
                    }
                    /* 2348 */ chr.getmapiavote = 0;
                    /* 2349 */ chr.voteamount = 0;
                    /* 2350 */ chr.getpolicevote = 0;
                    /* 2351 */ chr.isDead = false;
                    /* 2352 */ chr.isDrVote = true;
                    /* 2353 */ chr.isMapiaVote = true;
                    /* 2354 */ chr.isPoliceVote = true;
                    /* 2355 */ chr.getdrvote = 0;
                    /* 2356 */ chr.isVoting = false;
                }
                /* 2358 */ map.broadcastMessage(CWvsContext.serverNotice(1, "", "진행자>>낮이 되었습니다. 마피아를 찾아내 모두 처형하면 시민의 승리이며, 마피아가 경찰 또는 시민을 모두 죽일시 마피아의 승리입니다.(직업 : 시민,경찰,마피아,의사)"));
                /* 2359 */ map.playern = playernuma;

                /* 2361 */ map.morningmap = morningmap;
                /* 2362 */ map.aftertime = after;
                /* 2363 */ map.nighttime = night;
                /* 2364 */ map.votetime = vote;
                /* 2365 */ map.citizenmap1 = citizenmap1;
                /* 2366 */ map.citizenmap2 = citizenmap2;
                /* 2367 */ map.citizenmap3 = citizenmap3;
                /* 2368 */ map.citizenmap4 = citizenmap4;
                /* 2369 */ map.citizenmap5 = citizenmap5;
                /* 2370 */ map.citizenmap6 = citizenmap6;
                /* 2371 */ map.MapiaIng = true;

                /* 2373 */ map.mapiamap = mapiamap;
                /* 2374 */ map.policemap = policemap;
                /* 2375 */ map.drmap = drmap;
                /* 2376 */ m_timer.cancel();
                /* 2377 */ map.MapiaMorning(player);
                /* 2378 */ map.MapiaChannel = player.getClient().getChannel();
            }
        };

        /* 2382 */ m_timer.schedule(m_task, (time * 1000));
    }

    public void resetReactors() {
        /* 2386 */ getPlayer().getMap().resetReactors(this.c);
    }

    public void genericGuildMessage(int code) {
        /* 2390 */ this.c.getSession().writeAndFlush(CWvsContext.GuildPacket.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {
        /* 2394 */ int gid = this.c.getPlayer().getGuildId();
        /* 2395 */ if (gid <= 0 || this.c.getPlayer().getGuildRank() != 1) {
            return;
        }
        /* 2398 */ World.Guild.disbandGuild(gid);
    }

    public void increaseGuildCapacity(boolean trueMax) {
        if (c.getPlayer().getMeso() < 500000 && !trueMax) {
            c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "500,000 메소가 필요합니다."));
            return;
        }
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0) {
            return;
        }
        if (World.Guild.increaseGuildCapacity(gid, trueMax)) {
            if (!trueMax) {
                c.getPlayer().gainMeso(-500000, true, true);
            }
            sendNext("증가되었습니다.");
        } else if (!trueMax) {
            sendNext("이미 한계치입니다. (Limit: 100)");
        } else {
            sendNext("이미 한계치입니다. (Limit: 200)");
        }
    }

    public void displayGuildRanks() {
        /* 2432 */ this.c.getSession().writeAndFlush(CWvsContext.GuildPacket.guildRankingRequest());
    }

    public boolean removePlayerFromInstance() {
        /* 2436 */ if (this.c.getPlayer().getEventInstance() != null) {
            /* 2437 */ this.c.getPlayer().getEventInstance().removePlayer(this.c.getPlayer());
            /* 2438 */ return true;
        }
        /* 2440 */ return false;
    }

    public boolean isPlayerInstance() {
        /* 2444 */ if (this.c.getPlayer().getEventInstance() != null) {
            /* 2445 */ return true;
        }
        /* 2447 */ return false;
    }

    public void changeStat(byte slot, int type, int amount) {
        /* 2451 */ Equip sel = (Equip) this.c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) slot);
        /* 2452 */ switch (type) {
            case 0:
                /* 2454 */ sel.setStr((short) amount);
                break;
            case 1:
                /* 2457 */ sel.setDex((short) amount);
                break;
            case 2:
                /* 2460 */ sel.setInt((short) amount);
                break;
            case 3:
                /* 2463 */ sel.setLuk((short) amount);
                break;
            case 4:
                /* 2466 */ sel.setHp((short) amount);
                break;
            case 5:
                /* 2469 */ sel.setMp((short) amount);
                break;
            case 6:
                /* 2472 */ sel.setWatk((short) amount);
                break;
            case 7:
                /* 2475 */ sel.setMatk((short) amount);
                break;
            case 8:
                /* 2478 */ sel.setWdef((short) amount);
                break;
            case 9:
                /* 2481 */ sel.setMdef((short) amount);
                break;
            case 10:
                /* 2484 */ sel.setAcc((short) amount);
                break;
            case 11:
                /* 2487 */ sel.setAvoid((short) amount);
                break;
            case 12:
                /* 2490 */ sel.setHands((short) amount);
                break;
            case 13:
                /* 2493 */ sel.setSpeed((short) amount);
                break;
            case 14:
                /* 2496 */ sel.setJump((short) amount);
                break;
            case 15:
                /* 2499 */ sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                /* 2502 */ sel.setViciousHammer((byte) amount);
                break;
            case 17:
                /* 2505 */ sel.setLevel((byte) amount);
                break;
            case 18:
                /* 2508 */ sel.setEnhance((byte) amount);
                break;
            case 19:
                /* 2511 */ sel.setPotential1(amount);
                break;
            case 20:
                /* 2514 */ sel.setPotential2(amount);
                break;
            case 21:
                /* 2517 */ sel.setPotential3(amount);
                break;
            case 22:
                /* 2520 */ sel.setPotential4(amount);
                break;
            case 23:
                /* 2523 */ sel.setPotential5(amount);
                break;
            case 24:
                /* 2526 */ sel.setOwner(getText());
                break;
        }

        /* 2531 */ this.c.getPlayer().equipChanged();
        /* 2532 */ this.c.getPlayer().fakeRelog();
    }

    public String searchCashItem(String t) {
        /* 2536 */ Pattern name2Pattern = Pattern.compile("^[가-힣a-zA-Z0-9]*$");
        /* 2537 */ if (!name2Pattern.matcher(t).matches()) {
            /* 2538 */ return "검색할 수 없는 아이템입니다.";
        }
        /* 2540 */ StringBuilder sb = new StringBuilder();
        /* 2541 */ for (Pair<Integer, String> item : (Iterable<Pair<Integer, String>>) MapleItemInformationProvider.getInstance().getAllEquips()) {

            /* 2543 */ if (((String) item.right).contains(t)
                    && /* 2544 */ MapleItemInformationProvider.getInstance().isCash(((Integer) item.left).intValue())) {
                /* 2545 */ sb.append("#b#L" + item.left + "# #i" + item.left + "##t" + item.left + "##l\r\n");
            }
        }

        /* 2549 */ return sb.toString();
    }

    public void changeDamageSkin(int skinnum) {
        /* 2553 */ MapleQuest quest = MapleQuest.getInstance(7291);
        /* 2554 */ MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);
        /* 2555 */ String skinString = String.valueOf(skinnum);
        /* 2556 */ queststatus.setCustomData((skinString == null) ? "0" : skinString);
        /* 2557 */ getPlayer().updateQuest(queststatus, true);
        /* 2558 */ getPlayer().dropMessage(5, "데미지 스킨이 변경되었습니다.");
        /* 2559 */ getPlayer().getMap().broadcastMessage(getPlayer(), CField.showForeignDamageSkin(getPlayer(), skinnum), false);
    }

    public void openDuey() {
        /* 2563 */ this.c.getPlayer().setConversation(2);
        /* 2564 */ this.c.getSession().writeAndFlush(CField.sendDuey((byte) 9, null, null));
    }

    public void sendUI(int op) {
        /* 2568 */ this.c.getSession().writeAndFlush(CField.UIPacket.openUI(op));
    }

    public void sendRepairWindow() {
        /* 2572 */ this.c.getSession().writeAndFlush(CField.UIPacket.openUIOption(33, this.id));
    }

    public void sendNameChangeWindow() {
        /* 2576 */ this.c.getSession().writeAndFlush(CField.UIPacket.openUIOption(1110, 4034803));
    }

    public void sendProfessionWindow() {
        /* 2580 */ this.c.getSession().writeAndFlush(CField.UIPacket.openUI(42));
    }

    public final int getDojoPoints() {
        /* 2584 */ return dojo_getPts();
    }

    public final int getDojoRecord() {
        /* 2588 */ return this.c.getPlayer().getIntNoRecord(150101);
    }

    public void setDojoRecord(boolean reset) {
        /* 2592 */ if (reset) {
            /* 2593 */ this.c.getPlayer().getQuestNAdd(MapleQuest.getInstance(150101)).setCustomData("0");
            /* 2594 */ this.c.getPlayer().getQuestNAdd(MapleQuest.getInstance(150100)).setCustomData("0");
        } else {
            /* 2596 */ this.c.getPlayer().getQuestNAdd(MapleQuest.getInstance(150101)).setCustomData(String.valueOf(this.c.getPlayer().getIntRecord(150101) + 1));
        }
    }

    public boolean start_DojoAgent(boolean dojo, boolean party) {
        /* 2601 */ if (dojo) {
            /* 2602 */ return Event_DojoAgent.warpStartDojo(this.c.getPlayer(), party);
        }
        /* 2604 */ return Event_DojoAgent.warpStartAgent(this.c.getPlayer(), party);
    }

    public final short getKegs() {
        /* 2608 */ return this.c.getChannelServer().getFireWorks().getKegsPercentage();
    }

    public void giveKegs(int kegs) {
        /* 2612 */ this.c.getChannelServer().getFireWorks().giveKegs(this.c.getPlayer(), kegs);
    }

    public final short getSunshines() {
        /* 2616 */ return this.c.getChannelServer().getFireWorks().getSunsPercentage();
    }

    public void addSunshines(int kegs) {
        /* 2620 */ this.c.getChannelServer().getFireWorks().giveSuns(this.c.getPlayer(), kegs);
    }

    public final short getDecorations() {
        /* 2624 */ return this.c.getChannelServer().getFireWorks().getDecsPercentage();
    }

    public void addDecorations(int kegs) {
        try {
            /* 2629 */ this.c.getChannelServer().getFireWorks().giveDecs(this.c.getPlayer(), kegs);
            /* 2630 */        } catch (Exception e) {
            /* 2631 */ e.printStackTrace();
        }
    }

    public void maxStats() {
        /* 2636 */ Map<MapleStat, Long> statup = new EnumMap<>(MapleStat.class);

        /* 2638 */ (this.c.getPlayer().getStat()).str = Short.MAX_VALUE;
        /* 2639 */ (this.c.getPlayer().getStat()).dex = Short.MAX_VALUE;
        /* 2640 */ (this.c.getPlayer().getStat()).int_ = Short.MAX_VALUE;
        /* 2641 */ (this.c.getPlayer().getStat()).luk = Short.MAX_VALUE;

        /* 2643 */ int overrDemon = GameConstants.isDemonSlayer(this.c.getPlayer().getJob()) ? GameConstants.getMPByJob(this.c.getPlayer()) : 500000;
        /* 2644 */ (this.c.getPlayer().getStat()).maxhp = 500000L;
        /* 2645 */ (this.c.getPlayer().getStat()).maxmp = overrDemon;
        /* 2646 */ this.c.getPlayer().getStat().setHp(500000L, this.c.getPlayer());
        /* 2647 */ this.c.getPlayer().getStat().setMp(overrDemon, this.c.getPlayer());

        /* 2649 */ statup.put(MapleStat.STR, Long.valueOf(32767L));
        /* 2650 */ statup.put(MapleStat.DEX, Long.valueOf(32767L));
        /* 2651 */ statup.put(MapleStat.LUK, Long.valueOf(32767L));
        /* 2652 */ statup.put(MapleStat.INT, Long.valueOf(32767L));
        /* 2653 */ statup.put(MapleStat.HP, Long.valueOf(500000L));
        /* 2654 */ statup.put(MapleStat.MAXHP, Long.valueOf(500000L));
        /* 2655 */ statup.put(MapleStat.MP, Long.valueOf(overrDemon));
        /* 2656 */ statup.put(MapleStat.MAXMP, Long.valueOf(overrDemon));
        /* 2657 */ this.c.getPlayer().getStat().recalcLocalStats(this.c.getPlayer());
        /* 2658 */ this.c.getSession().writeAndFlush(CWvsContext.updatePlayerStats(statup, this.c.getPlayer()));
    }

    public boolean getSR(Triple<String, Map<Integer, String>, Long> ma, int sel) {
        /* 2662 */ if (((Map) ma.mid).get(Integer.valueOf(sel)) == null || ((String) ((Map) ma.mid).get(Integer.valueOf(sel))).length() <= 0) {
            /* 2663 */ dispose();
            /* 2664 */ return false;
        }
        /* 2666 */ sendOk((String) ((Map) ma.mid).get(Integer.valueOf(sel)));
        /* 2667 */ return true;
    }

    public String getAllItem() {
        /* 2671 */ StringBuilder string = new StringBuilder();
        /* 2672 */ for (Item item : this.c.getPlayer().getInventory(MapleInventoryType.EQUIP).list()) {
            /* 2673 */ string.append("#L" + item.getUniqueId() + "##i " + item.getItemId() + "#\r\n");
        }
        /* 2675 */ return string.toString();
    }

    public Equip getEquip(int itemid) {
        /* 2679 */ return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    public void setExpiration(Object statsSel, long expire) {
        /* 2683 */ if (statsSel instanceof Equip) {
            /* 2684 */ ((Equip) statsSel).setExpiration(System.currentTimeMillis() + expire * 24L * 60L * 60L * 1000L);
        }
    }

    public void setLock(Object statsSel) {
        /* 2689 */ if (statsSel instanceof Equip) {
            /* 2690 */ Equip eq = (Equip) statsSel;
            /* 2691 */ if (eq.getExpiration() == -1L) {
                /* 2692 */ eq.setFlag(eq.getFlag() | ItemFlag.LOCK.getValue());
            } else {
                /* 2694 */ eq.setFlag(eq.getFlag() | ItemFlag.UNTRADEABLE.getValue());
            }
        }
    }

    public boolean addFromDrop(Object statsSel) {
        /* 2700 */ if (statsSel instanceof Item) {
            /* 2701 */ Item it = (Item) statsSel;
            /* 2702 */ return (MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false));
        }
        /* 2704 */ return false;
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        /* 2708 */ return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        /* 2712 */ MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        /* 2713 */ if (inv == null) {
            /* 2714 */ return false;
        }
        /* 2716 */ Item item = getPlayer().getInventory(inv).getItem((short) slot);
        /* 2717 */ if (item == null || statsSel instanceof Item) {
            /* 2718 */ item = (Item) statsSel;
        }
        /* 2720 */ if (offset > 0) {
            /* 2721 */ if (inv != MapleInventoryType.EQUIP) {
                /* 2722 */ return false;
            }
            /* 2724 */ Equip eq = (Equip) item;
            /* 2725 */ if (takeSlot) {
                /* 2726 */ if (eq.getUpgradeSlots() < 1) {
                    /* 2727 */ return false;
                }
                /* 2729 */ eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));

                /* 2731 */ if (eq.getExpiration() == -1L) {
                    /* 2732 */ eq.setFlag(eq.getFlag() | ItemFlag.LOCK.getValue());
                } else {
                    /* 2734 */ eq.setFlag(eq.getFlag() | ItemFlag.UNTRADEABLE.getValue());
                }
            }
            /* 2737 */ if (type.equalsIgnoreCase("Slots")) {
                /* 2738 */ eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
                /* 2739 */ eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
                /* 2740 */            } else if (type.equalsIgnoreCase("Level")) {
                /* 2741 */ eq.setLevel((byte) (eq.getLevel() + offset));
                /* 2742 */            } else if (type.equalsIgnoreCase("Hammer")) {
                /* 2743 */ eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
                /* 2744 */            } else if (type.equalsIgnoreCase("STR")) {
                /* 2745 */ eq.setStr((short) (eq.getStr() + offset));
                /* 2746 */            } else if (type.equalsIgnoreCase("DEX")) {
                /* 2747 */ eq.setDex((short) (eq.getDex() + offset));
                /* 2748 */            } else if (type.equalsIgnoreCase("INT")) {
                /* 2749 */ eq.setInt((short) (eq.getInt() + offset));
                /* 2750 */            } else if (type.equalsIgnoreCase("LUK")) {
                /* 2751 */ eq.setLuk((short) (eq.getLuk() + offset));
                /* 2752 */            } else if (type.equalsIgnoreCase("HP")) {
                /* 2753 */ eq.setHp((short) (eq.getHp() + offset));
                /* 2754 */            } else if (type.equalsIgnoreCase("MP")) {
                /* 2755 */ eq.setMp((short) (eq.getMp() + offset));
                /* 2756 */            } else if (type.equalsIgnoreCase("WATK")) {
                /* 2757 */ eq.setWatk((short) (eq.getWatk() + offset));
                /* 2758 */            } else if (type.equalsIgnoreCase("MATK")) {
                /* 2759 */ eq.setMatk((short) (eq.getMatk() + offset));
                /* 2760 */            } else if (type.equalsIgnoreCase("WDEF")) {
                /* 2761 */ eq.setWdef((short) (eq.getWdef() + offset));
                /* 2762 */            } else if (type.equalsIgnoreCase("MDEF")) {
                /* 2763 */ eq.setMdef((short) (eq.getMdef() + offset));
                /* 2764 */            } else if (type.equalsIgnoreCase("ACC")) {
                /* 2765 */ eq.setAcc((short) (eq.getAcc() + offset));
                /* 2766 */            } else if (type.equalsIgnoreCase("Avoid")) {
                /* 2767 */ eq.setAvoid((short) (eq.getAvoid() + offset));
                /* 2768 */            } else if (type.equalsIgnoreCase("Hands")) {
                /* 2769 */ eq.setHands((short) (eq.getHands() + offset));
                /* 2770 */            } else if (type.equalsIgnoreCase("Speed")) {
                /* 2771 */ eq.setSpeed((short) (eq.getSpeed() + offset));
                /* 2772 */            } else if (type.equalsIgnoreCase("Jump")) {
                /* 2773 */ eq.setJump((short) (eq.getJump() + offset));
                /* 2774 */            } else if (type.equalsIgnoreCase("ItemEXP")) {
                /* 2775 */ eq.setItemEXP(eq.getItemEXP() + offset);
                /* 2776 */            } else if (type.equalsIgnoreCase("Expiration")) {
                /* 2777 */ eq.setExpiration(eq.getExpiration() + offset);
                /* 2778 */            } else if (type.equalsIgnoreCase("Flag")) {
                /* 2779 */ eq.setFlag(eq.getFlag() + offset);
            }
            /* 2781 */ item = eq.copy();
        }
        /* 2783 */ MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        /* 2784 */ return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        /* 2788 */ return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    public boolean isCash(int itemId) {
        /* 2792 */ return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    public int getTotalStat(int itemId) {
        /* 2796 */ return MapleItemInformationProvider.getInstance().getTotalStat((Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId));
    }

    public int getReqLevel(int itemId) {
        /* 2800 */ return MapleItemInformationProvider.getInstance().getReqLevel(itemId);
    }

    public SecondaryStatEffect getEffect(int buff) {
        /* 2804 */ return MapleItemInformationProvider.getInstance().getItemEffect(buff);
    }

    public void giveBuff(int skillid) {
        /* 2808 */ SkillFactory.getSkill(skillid).getEffect(1).applyTo(this.c.getPlayer());
    }

    public void buffGuild(int buff, int duration, String msg) {
        /* 2812 */ MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        /* 2813 */ if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            /* 2814 */ SecondaryStatEffect mse = ii.getItemEffect(buff);
            /* 2815 */ for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                /* 2816 */ for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters().values()) {
                    /* 2817 */ if (chr.getGuildId() == getPlayer().getGuildId()) {
                        /* 2818 */ mse.applyTo(chr, chr, true, chr.getTruePosition(), duration, (byte) 0, true);
                        /* 2819 */ chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
                    }
                }
            }
        }
    }

    public long getRemainPremium(int accid) {
        /* 2827 */ Connection con = null;
        /* 2828 */ ResultSet rs = null;
        /* 2829 */ PreparedStatement ps = null;
        /* 2830 */ long ret = 0L;
        try {
            /* 2832 */ con = DatabaseConnection.getConnection();
            /* 2833 */ ps = con.prepareStatement("SELECT * FROM premium WHERE accid = ?");
            /* 2834 */ ps.setInt(1, accid);
            /* 2835 */ rs = ps.executeQuery();
            /* 2836 */ if (rs.next()) {
                /* 2837 */ ret = rs.getLong("period");
            }

            /* 2840 */ rs.close();
            /* 2841 */ ps.close();
            /* 2842 */ con.close();
            /* 2843 */        } catch (SQLException e) {
            /* 2844 */ e.printStackTrace();
        } finally {
            /* 2846 */ if (rs != null) {
                try {
                    /* 2848 */ rs.close();
                    /* 2849 */                } catch (Exception exception) {
                }
            }

            /* 2852 */ if (ps != null) {
                try {
                    /* 2854 */ ps.close();
                    /* 2855 */                } catch (Exception exception) {
                }
            }

            /* 2858 */ if (con != null) {
                try {
                    /* 2860 */ con.close();
                    /* 2861 */                } catch (Exception exception) {
                }
            }
        }

        /* 2865 */ return ret;
    }

    public boolean existPremium(int aci) {
        /* 2869 */ Connection con = null;
        /* 2870 */ ResultSet rs = null;
        /* 2871 */ PreparedStatement ps = null;
        /* 2872 */ boolean ret = false;
        try {
            /* 2874 */ con = DatabaseConnection.getConnection();
            /* 2875 */ ps = con.prepareStatement("SELECT * FROM premium WHERE accid = ?");
            /* 2876 */ ps.setInt(1, aci);
            /* 2877 */ rs = ps.executeQuery();
            /* 2878 */ ret = rs.next();

            /* 2880 */ rs.close();
            /* 2881 */ ps.close();
            /* 2882 */ con.close();
            /* 2883 */        } catch (SQLException e) {
            /* 2884 */ e.printStackTrace();
        } finally {
            /* 2886 */ if (rs != null) {
                try {
                    /* 2888 */ rs.close();
                    /* 2889 */                } catch (Exception exception) {
                }
            }

            /* 2892 */ if (ps != null) {
                try {
                    /* 2894 */ ps.close();
                    /* 2895 */                } catch (Exception exception) {
                }
            }

            /* 2898 */ if (con != null) {
                try {
                    /* 2900 */ con.close();
                    /* 2901 */                } catch (Exception exception) {
                }
            }
        }

        /* 2905 */ return ret;
    }

    public void gainAllAccountPremium(int v3, int v4) {
        /* 2909 */ Connection con = null;
        /* 2910 */ PreparedStatement ps = null;
        /* 2911 */ ResultSet rs = null;
        /* 2912 */ ArrayList<Integer> chrs = new ArrayList<>();
        /* 2913 */ Date adate = new Date();
        try {
            /* 2915 */ con = DatabaseConnection.getConnection();
            /* 2916 */ ps = con.prepareStatement("SELECT * FROM accounts");
            /* 2917 */ rs = ps.executeQuery();
            /* 2918 */ while (rs.next()) {
                /* 2919 */ chrs.add(Integer.valueOf(rs.getInt("id")));
            }
            /* 2921 */ rs.close();
            /* 2922 */ ps.close();

            /* 2924 */ for (int i = 0; i < chrs.size(); i++) {
                /* 2925 */ if (existPremium(((Integer) chrs.get(i)).intValue())) {
                    /* 2926 */ if (getRemainPremium(((Integer) chrs.get(i)).intValue()) > adate.getTime()) {
                        /* 2927 */ ps = con.prepareStatement("UPDATE premium SET period = ? WHERE accid = ?");
                        /* 2928 */ ps.setLong(1, getRemainPremium(((Integer) chrs.get(i)).intValue()) + (v3 * 24 * 60 * 60 * 1000));
                        /* 2929 */ ps.setInt(2, ((Integer) chrs.get(i)).intValue());
                        /* 2930 */ ps.executeUpdate();

                        /* 2932 */ ps.close();
                    } else {
                        /* 2934 */ ps = con.prepareStatement("UPDATE premium SET period = ? and `name` = ? and `buff` = ? WHERE accid = ?");
                        /* 2935 */ ps.setLong(1, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
                        /* 2936 */ ps.setString(2, "일반");
                        /* 2937 */ ps.setInt(3, 80001535);
                        /* 2938 */ ps.setInt(4, ((Integer) chrs.get(i)).intValue());
                        /* 2939 */ ps.executeUpdate();

                        /* 2941 */ ps.close();
                    }
                } else {

                    /* 2945 */ ps = con.prepareStatement("INSERT INTO premium(accid, name, buff, period) VALUES (?, ?, ?, ?)");
                    /* 2946 */ ps.setInt(1, ((Integer) chrs.get(i)).intValue());
                    /* 2947 */ ps.setString(2, "일반");
                    /* 2948 */ ps.setInt(3, 80001535);
                    /* 2949 */ ps.setLong(4, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
                    /* 2950 */ ps.executeUpdate();
                    /* 2951 */ ps.close();
                }
            }
            /* 2954 */ rs.close();
            /* 2955 */ ps.close();
            /* 2956 */ con.close();
            /* 2957 */        } catch (SQLException e) {
            /* 2958 */ e.printStackTrace();
        } finally {
            /* 2960 */ if (rs != null) {
                try {
                    /* 2962 */ rs.close();
                    /* 2963 */                } catch (Exception exception) {
                }
            }

            /* 2966 */ if (ps != null) {
                try {
                    /* 2968 */ ps.close();
                    /* 2969 */                } catch (Exception exception) {
                }
            }

            /* 2972 */ if (con != null) {
                try {
                    /* 2974 */ con.close();
                    /* 2975 */                } catch (Exception exception) {
                }
            }
        }
    }

    public void gainAccountPremium(String acc, int v3, boolean v4) {
        /* 2982 */ Connection con = null;
        /* 2983 */ PreparedStatement ps = null;
        /* 2984 */ ResultSet rs = null;
        /* 2985 */ Date adate = new Date();
        /* 2986 */ int accid = 0;

        try {
            /* 2989 */ con = DatabaseConnection.getConnection();
            /* 2990 */ ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
            /* 2991 */ ps.setString(1, acc);
            /* 2992 */ rs = ps.executeQuery();
            /* 2993 */ if (rs.next()) {
                /* 2994 */ accid = rs.getInt("id");
            }
            /* 2996 */ rs.close();
            /* 2997 */ ps.close();

            /* 2999 */ if (existPremium(accid)) {
                /* 3000 */ if (getRemainPremium(accid) > adate.getTime()) {
                    /* 3001 */ ps = con.prepareStatement("UPDATE premium SET period = ? WHERE accid = ?");
                    /* 3002 */ if (v4) {
                        /* 3003 */ ps.setLong(1, getRemainPremium(accid) + (v3 * 24 * 60 * 60 * 1000));
                    } else {
                        /* 3005 */ ps.setLong(1, getRemainPremium(accid) - (v3 * 24 * 60 * 60 * 1000));
                    }
                    /* 3007 */ ps.setInt(2, accid);
                    /* 3008 */ ps.executeUpdate();

                    /* 3010 */ ps.close();
                } /* 3012 */ else if (v4) {
                    /* 3013 */ ps = con.prepareStatement("UPDATE premium SET period = ? and `name` = ? and `buff` = ? WHERE accid = ?");
                    /* 3014 */ ps.setLong(1, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
                    /* 3015 */ ps.setString(2, "일반");
                    /* 3016 */ ps.setInt(3, 80001535);
                    /* 3017 */ ps.setInt(4, accid);
                    /* 3018 */ ps.executeUpdate();

                    /* 3020 */ ps.close();
                }

            } /* 3024 */ else if (v4) {
                /* 3025 */ ps = con.prepareStatement("INSERT INTO premium(accid, name, buff, period) VALUES (?, ?, ?, ?)");
                /* 3026 */ ps.setInt(1, accid);
                /* 3027 */ ps.setString(2, "일반");
                /* 3028 */ ps.setInt(3, 80001535);
                /* 3029 */ ps.setLong(4, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
                /* 3030 */ ps.executeUpdate();
                /* 3031 */ ps.close();
            }

            /* 3034 */ rs.close();
            /* 3035 */ ps.close();
            /* 3036 */ con.close();
            /* 3037 */        } catch (SQLException e) {
            /* 3038 */ e.printStackTrace();
        } finally {
            /* 3040 */ if (rs != null) {
                try {
                    /* 3042 */ rs.close();
                    /* 3043 */                } catch (Exception exception) {
                }
            }

            /* 3046 */ if (ps != null) {
                try {
                    /* 3048 */ ps.close();
                    /* 3049 */                } catch (Exception exception) {
                }
            }

            /* 3052 */ if (con != null) {
                try {
                    /* 3054 */ con.close();
                    /* 3055 */                } catch (Exception exception) {
                }
            }
        }
    }

    public boolean createAlliance(String alliancename) {
        /* 3062 */ MapleParty pt = this.c.getPlayer().getParty();
        /* 3063 */ MapleCharacter otherChar = this.c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        /* 3064 */ if (otherChar == null || otherChar.getId() == this.c.getPlayer().getId()) {
            /* 3065 */ return false;
        }
        try {
            /* 3068 */ return World.Alliance.createAlliance(alliancename, this.c.getPlayer().getId(), otherChar.getId(), this.c.getPlayer().getGuildId(), otherChar.getGuildId());
            /* 3069 */        } catch (Exception re) {
            /* 3070 */ re.printStackTrace();
            /* 3071 */ return false;
        }
    }

    public boolean addCapacityToAlliance() {
        try {
            /* 3077 */ MapleGuild gs = World.Guild.getGuild(this.c.getPlayer().getGuildId());
            /* 3078 */ if (gs != null && this.c.getPlayer().getGuildRank() == 1 && this.c.getPlayer().getAllianceRank() == 1
                    && /* 3079 */ World.Alliance.getAllianceLeader(gs.getAllianceId()) == this.c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                /* 3080 */ gainMeso(-10000000L);
                /* 3081 */ return true;
            }

            /* 3084 */        } catch (Exception re) {
            /* 3085 */ re.printStackTrace();
        }
        /* 3087 */ return false;
    }

    public boolean disbandAlliance() {
        try {
            /* 3092 */ MapleGuild gs = World.Guild.getGuild(this.c.getPlayer().getGuildId());
            /* 3093 */ if (gs != null && this.c.getPlayer().getGuildRank() == 1 && this.c.getPlayer().getAllianceRank() == 1
                    && /* 3094 */ World.Alliance.getAllianceLeader(gs.getAllianceId()) == this.c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                /* 3095 */ return true;
            }
        } /* 3098 */ catch (Exception re) {
            /* 3099 */ re.printStackTrace();
        }
        /* 3101 */ return false;
    }

    public byte getLastMsg() {
        /* 3105 */ return this.lastMsg;
    }

    public final void setLastMsg(byte last) {
        /* 3109 */ this.lastMsg = last;
    }

    public final void maxAllSkills() {
        /* 3113 */ HashMap<Skill, SkillEntry> sa = new HashMap<>();
        /* 3114 */ for (Skill skil : SkillFactory.getAllSkills()) {
            /* 3115 */ if (GameConstants.isApplicableSkill(skil.getId()) && skil.getId() < 90000000) {
                /* 3116 */ sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        /* 3119 */ getPlayer().changeSkillsLevel(sa);
    }

    public final void resetStats(int str, int dex, int z, int luk) {
        /* 3123 */ this.c.getPlayer().resetStats(str, dex, z, luk);
    }

    public final boolean dropItem(int slot, int invType, int quantity) {
        /* 3127 */ MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        /* 3128 */ if (inv == null) {
            /* 3129 */ return false;
        }
        /* 3131 */ return MapleInventoryManipulator.drop(this.c, inv, (short) slot, (short) quantity, true);
    }

    public final void setQuestRecord(Object ch, int questid, String data) {
        /* 3135 */ ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    public final void doWeddingEffect(Object ch) {
        /* 3139 */ final MapleCharacter chr = (MapleCharacter) ch;
        /* 3140 */ final MapleCharacter player = getPlayer();
        /* 3141 */ getMap().broadcastMessage(CWvsContext.yellowChat(player.getName() + ", do you take " + chr.getName() + " as your wife and promise to stay beside her through all downtimes, crashes, and lags?"));
        /* 3142 */ server.Timer.CloneTimer.getInstance().schedule(new Runnable() {
            public void run() {
                /* 3145 */ if (chr == null || player == null) {
                    /* 3146 */ NPCConversationManager.this.warpMap(680000500, 0);
                } else {
                    /* 3148 */ chr.getMap().broadcastMessage(CWvsContext.yellowChat(chr.getName() + ", do you take " + player.getName() + " as your husband and promise to stay beside him through all downtimes, crashes, and lags?"));
                }
            }
        },
        10000L);
/* 3152 */     server.Timer.CloneTimer.getInstance().schedule(new Runnable() {
            public void run() {
                /* 3155 */ if (chr == null || player == null) {
                    /* 3156 */ if (player != null) {
                        /* 3157 */ NPCConversationManager.this.setQuestRecord(player, 160001, "3");
                        /* 3158 */ NPCConversationManager.this.setQuestRecord(player, 160002, "0");
                        /* 3159 */                    } else if (chr != null) {
                        /* 3160 */ NPCConversationManager.this.setQuestRecord(chr, 160001, "3");
                        /* 3161 */ NPCConversationManager.this.setQuestRecord(chr, 160002, "0");
                    }
                    /* 3163 */ NPCConversationManager.this.warpMap(680000500, 0);
                } else {
                    /* 3165 */ NPCConversationManager.this.setQuestRecord(player, 160001, "2");
                    /* 3166 */ NPCConversationManager.this.setQuestRecord(chr, 160001, "2");
                    /* 3167 */ NPCConversationManager.this.sendNPCText(player.getName() + " and " + chr.getName() + ", I wish you two all the best on your " + chr.getClient().getChannelServer().getServerName() + " journey together!", 9201002);
                    /* 3168 */ chr.getMap().startExtendedMapEffect("You may now kiss the bride, " + player.getName() + "!", 5120006);
                    /* 3169 */ if (chr.getGuildId() > 0) {
                        /* 3170 */ World.Guild.guildPacket(chr.getGuildId(), CWvsContext.sendMarriage(false, chr.getName()));
                    }
                    /* 3172 */ if (player.getGuildId() > 0) {
                        /* 3173 */ World.Guild.guildPacket(player.getGuildId(), CWvsContext.sendMarriage(false, player.getName()));
                    }
                }
            }
        },
    

    20000L);
   }
 
   
   public void putKey(int key, int type, int action) {
        /* 3182 */ getPlayer().changeKeybinding(key, (byte) type, action);
        /* 3183 */ getClient().getSession().writeAndFlush(CField.getKeymap(getPlayer().getKeyLayout()));
    }

    public void logDonator(String log, int previous_points) {
        /* 3187 */ StringBuilder logg = new StringBuilder();
        /* 3188 */ logg.append(MapleCharacterUtil.makeMapleReadable(getPlayer().getName()));
        /* 3189 */ logg.append(" [CID: ").append(getPlayer().getId()).append("] ");
        /* 3190 */ logg.append(" [Account: ").append(MapleCharacterUtil.makeMapleReadable(getClient().getAccountName())).append("] ");
        /* 3191 */ logg.append(log);
        /* 3192 */ logg.append(" [Previous: " + previous_points + "] [Now: " + getPlayer().getPoints() + "]");

        /* 3194 */ Connection con = null;
        /* 3195 */ PreparedStatement ps = null;
        /* 3196 */ ResultSet rs = null;
        try {
            /* 3198 */ con = DatabaseConnection.getConnection();
            /* 3199 */ ps = con.prepareStatement("INSERT INTO donorlog VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)");
            /* 3200 */ ps.setString(1, MapleCharacterUtil.makeMapleReadable(getClient().getAccountName()));
            /* 3201 */ ps.setInt(2, getClient().getAccID());
            /* 3202 */ ps.setString(3, MapleCharacterUtil.makeMapleReadable(getPlayer().getName()));
            /* 3203 */ ps.setInt(4, getPlayer().getId());
            /* 3204 */ ps.setString(5, log);
            /* 3205 */ ps.setString(6, FileoutputUtil.CurrentReadable_Time());
            /* 3206 */ ps.setInt(7, previous_points);
            /* 3207 */ ps.setInt(8, getPlayer().getPoints());
            /* 3208 */ ps.executeUpdate();
            /* 3209 */ ps.close();
            /* 3210 */ con.close();
            /* 3211 */        } catch (SQLException e) {
            /* 3212 */ e.printStackTrace();
        } finally {
            try {
                /* 3215 */ if (con != null) {
                    /* 3216 */ con.close();
                }
                /* 3218 */ if (ps != null) {
                    /* 3219 */ ps.close();
                }
                /* 3221 */ if (rs != null) {
                    /* 3222 */ rs.close();
                }
                /* 3224 */            } catch (SQLException se) {
                /* 3225 */ se.printStackTrace();
            }
        }
        /* 3228 */ FileoutputUtil.log("Log_Donator.rtf", logg.toString());
    }

    public void doRing(String name, int itemid) {
        /* 3232 */ PlayersHandler.DoRing(getClient(), name, itemid);
    }

    public int getNaturalStats(int itemid, String it) {
        /* 3236 */ Map<String, Integer> eqStats = MapleItemInformationProvider.getInstance().getEquipStats(itemid);
        /* 3237 */ if (eqStats != null && eqStats.containsKey(it)) {
            /* 3238 */ return ((Integer) eqStats.get(it)).intValue();
        }
        /* 3240 */ return 0;
    }

    public boolean isEligibleName(String t) {
        /* 3244 */ return (MapleCharacterUtil.canCreateChar(t, getPlayer().isGM()) && (!LoginInformationProvider.getInstance().isForbiddenName(t) || getPlayer().isGM()));
    }

    public String checkDrop(int mobId) {
        /* 3248 */ List<MonsterDropEntry> ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        /* 3249 */ if (ranks != null && ranks.size() > 0) {
            /* 3250 */ int num = 0, itemId = 0, ch = 0;

            /* 3252 */ StringBuilder name = new StringBuilder();
            /* 3253 */ for (int i = 0; i < ranks.size(); i++) {
                /* 3254 */ MonsterDropEntry de = ranks.get(i);
                /* 3255 */ if (de.chance > 0 && (de.questid <= 0 || (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
                    /* 3256 */ itemId = de.itemId;
                    /* 3257 */ if (num == 0) {
                        /* 3258 */ name.append("Drops for #o" + mobId + "#\r\n");
                        /* 3259 */ name.append("--------------------------------------\r\n");
                    }
                    /* 3261 */ String namez = "#z" + itemId + "#";
                    /* 3262 */ if (itemId == 0) {
                        /* 3263 */ itemId = 4031041;
                        /* 3264 */ namez = (de.Minimum * getClient().getChannelServer().getMesoRate()) + " to " + (de.Maximum * getClient().getChannelServer().getMesoRate()) + " meso";
                    }
                    /* 3266 */ ch = de.chance * getClient().getChannelServer().getDropRate();
                    /* 3267 */ name.append((num + 1) + ") #v" + itemId + "#" + namez + " - " + (Integer.valueOf((ch >= 999999) ? 1000000 : ch).doubleValue() / 10000.0D) + "% chance. " + ((de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0) ? ("Requires quest " + MapleQuest.getInstance(de.questid).getName() + " to be started.") : "") + "\r\n");
                    /* 3268 */ num++;
                }
            }
            /* 3271 */ if (name.length() > 0) {
                /* 3272 */ return name.toString();
            }
        }

        /* 3276 */ return "No drops was returned.";
    }

    public String getLeftPadded(String in, char padchar, int length) {
        /* 3280 */ return StringUtil.getLeftPaddedStr(in, padchar, length);
    }

    public void handleDivorce() {
        /* 3284 */ if (getPlayer().getMarriageId() <= 0) {
            /* 3285 */ sendNext("Please make sure you have a marriage.");
            return;
        }
        /* 3288 */ int chz = World.Find.findChannel(getPlayer().getMarriageId());
        /* 3289 */ if (chz == -1) {

            /* 3291 */ Connection con = null;
            /* 3292 */ PreparedStatement ps = null;
            /* 3293 */ ResultSet rs = null;
            try {
                /* 3295 */ con = DatabaseConnection.getConnection();
                /* 3296 */ ps = con.prepareStatement("UPDATE queststatus SET customData = ? WHERE characterid = ? AND (quest = ? OR quest = ?)");
                /* 3297 */ ps.setString(1, "0");
                /* 3298 */ ps.setInt(2, getPlayer().getMarriageId());
                /* 3299 */ ps.setInt(3, 160001);
                /* 3300 */ ps.setInt(4, 160002);
                /* 3301 */ ps.executeUpdate();
                /* 3302 */ ps.close();

                /* 3304 */ ps = con.prepareStatement("UPDATE characters SET marriageid = ? WHERE id = ?");
                /* 3305 */ ps.setInt(1, 0);
                /* 3306 */ ps.setInt(2, getPlayer().getMarriageId());
                /* 3307 */ ps.executeUpdate();
                /* 3308 */ ps.close();
                /* 3309 */ con.close();
                /* 3310 */            } catch (SQLException e) {
                /* 3311 */ outputFileError(e);
                return;
            } finally {
                try {
                    /* 3315 */ if (con != null) {
                        /* 3316 */ con.close();
                    }
                    /* 3318 */ if (ps != null) {
                        /* 3319 */ ps.close();
                    }
                    /* 3321 */ if (rs != null) {
                        /* 3322 */ rs.close();
                    }
                    /* 3324 */                } catch (SQLException se) {
                    /* 3325 */ se.printStackTrace();
                }
            }
            /* 3328 */ setQuestRecord(getPlayer(), 160001, "0");
            /* 3329 */ setQuestRecord(getPlayer(), 160002, "0");
            /* 3330 */ getPlayer().setMarriageId(0);
            /* 3331 */ sendNext("You have been successfully divorced...");
            return;
        }
        /* 3333 */ if (chz < -1) {
            /* 3334 */ sendNext("Please make sure your partner is logged on.");
            return;
        }
        /* 3337 */ MapleCharacter cPlayer = ChannelServer.getInstance(chz).getPlayerStorage().getCharacterById(getPlayer().getMarriageId());
        /* 3338 */ if (cPlayer != null) {
            /* 3339 */ cPlayer.dropMessage(1, "Your partner has divorced you.");
            /* 3340 */ cPlayer.setMarriageId(0);
            /* 3341 */ setQuestRecord(cPlayer, 160001, "0");
            /* 3342 */ setQuestRecord(getPlayer(), 160001, "0");
            /* 3343 */ setQuestRecord(cPlayer, 160002, "0");
            /* 3344 */ setQuestRecord(getPlayer(), 160002, "0");
            /* 3345 */ getPlayer().setMarriageId(0);
            /* 3346 */ sendNext("You have been successfully divorced...");
        } else {
            /* 3348 */ sendNext("An error occurred...");
        }
    }

    public String getReadableMillis(long startMillis, long endMillis) {
        /* 3353 */ return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public void sendUltimateExplorer() {
        /* 3357 */ getClient().getSession().writeAndFlush(CWvsContext.ultimateExplorer());
    }

    public void sendPendant(boolean b) {
        /* 3361 */ this.c.getSession().writeAndFlush(CWvsContext.pendantSlot(b));
    }

    public int getCompensation(String id) {
        /* 3365 */ Connection con = null;
        /* 3366 */ PreparedStatement ps = null;
        /* 3367 */ ResultSet rs = null;
        try {
            /* 3369 */ con = DatabaseConnection.getConnection();
            /* 3370 */ ps = con.prepareStatement("SELECT * FROM compensationlog_confirmed WHERE chrname = ?");
            /* 3371 */ ps.setString(1, id);
            /* 3372 */ rs = ps.executeQuery();
            /* 3373 */ if (rs.next()) {
                /* 3374 */ return rs.getInt("value");
            }
            /* 3376 */ rs.close();
            /* 3377 */ ps.close();
            /* 3378 */ con.close();
            /* 3379 */        } catch (SQLException e) {
            /* 3380 */ FileoutputUtil.outputFileError("Log_Script_Except.rtf", e);
        } finally {
            try {
                /* 3383 */ if (con != null) {
                    /* 3384 */ con.close();
                }
                /* 3386 */ if (ps != null) {
                    /* 3387 */ ps.close();
                }
                /* 3389 */ if (rs != null) {
                    /* 3390 */ rs.close();
                }
                /* 3392 */            } catch (SQLException se) {
                /* 3393 */ se.printStackTrace();
            }
        }
        /* 3396 */ return 0;
    }

    public boolean deleteCompensation(String id) {
        /* 3400 */ Connection con = null;
        /* 3401 */ PreparedStatement ps = null;
        /* 3402 */ ResultSet rs = null;
        try {
            /* 3404 */ con = DatabaseConnection.getConnection();
            /* 3405 */ ps = con.prepareStatement("DELETE FROM compensationlog_confirmed WHERE chrname = ?");
            /* 3406 */ ps.setString(1, id);
            /* 3407 */ ps.executeUpdate();
            /* 3408 */ ps.close();
            /* 3409 */ con.close();
            /* 3410 */ return true;
            /* 3411 */        } catch (SQLException e) {
            /* 3412 */ FileoutputUtil.outputFileError("Log_Script_Except.rtf", e);
            /* 3413 */ return false;
        } finally {
            try {
                /* 3416 */ if (con != null) {
                    /* 3417 */ con.close();
                }
                /* 3419 */ if (ps != null) {
                    /* 3420 */ ps.close();
                }
                /* 3422 */ if (rs != null) {
                    /* 3423 */ rs.close();
                }
                /* 3425 */            } catch (SQLException se) {
                /* 3426 */ se.printStackTrace();
            }
        }
    }

    public void gainAPS(int gain) {
        /* 3433 */ getPlayer().gainAPS(gain);
    }

    public void forceCompleteQuest(MapleCharacter chr, int idd) {
        /* 3437 */ MapleQuest.getInstance(idd).forceComplete(chr, getNpc());
    }

    public void setInnerStats(MapleCharacter chr, int line) {
        /* 3442 */ InnerSkillValueHolder isvh = InnerAbillity.getInstance().renewSkill(0, false);
        /* 3443 */ chr.getInnerSkills().add(isvh);
        /* 3444 */ chr.changeSkillLevel(SkillFactory.getSkill(isvh.getSkillId()), isvh.getSkillLevel(), isvh.getSkillLevel());
        /* 3445 */ chr.getClient().getSession().writeAndFlush(CField.updateInnerPotential((byte) line, isvh.getSkillId(), isvh.getSkillLevel(), isvh.getRank()));
    }

    public void setInnerStats(int line) {
        /* 3450 */ InnerSkillValueHolder isvh = InnerAbillity.getInstance().renewSkill(0, false);
        /* 3451 */ this.c.getPlayer().getInnerSkills().add(isvh);
        /* 3452 */ this.c.getPlayer().changeSkillLevel(SkillFactory.getSkill(isvh.getSkillId()), isvh.getSkillLevel(), isvh.getSkillLevel());
        /* 3453 */ this.c.getSession().writeAndFlush(CField.updateInnerPotential((byte) line, isvh.getSkillId(), isvh.getSkillLevel(), isvh.getRank()));
    }

    public void openAuctionUI() {
        /* 3457 */ this.c.getSession().writeAndFlush(CField.UIPacket.openUI(161));
    }

    public void gainSponserItem(int item, String name, short allstat, short damage, byte upgradeslot) {
        /* 3461 */ if (GameConstants.isEquip(item)) {
            /* 3462 */ Equip Item = (Equip) MapleItemInformationProvider.getInstance().getEquipById(item);
            /* 3463 */ Item.setOwner(name);
            /* 3464 */ Item.setStr(allstat);
            /* 3465 */ Item.setDex(allstat);
            /* 3466 */ Item.setInt(allstat);
            /* 3467 */ Item.setLuk(allstat);
            /* 3468 */ Item.setWatk(damage);
            /* 3469 */ Item.setMatk(damage);
            /* 3470 */ Item.setUpgradeSlots(upgradeslot);
            /* 3471 */ MapleInventoryManipulator.addFromDrop(this.c, (Item) Item, false);
        } else {
            /* 3473 */ gainItem(item, allstat, damage);
        }
    }

    public void askAvatar(String text, List<Integer> args) {
        /* 3478 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.id, text, args));
        /* 3479 */ this.lastMsg = 9;
    }

    public void SearchItem(String text, int type) {
        /* 3483 */ NPCConversationManager cm = this;
        /* 3484 */ if ((text.getBytes()).length < 4) {
            /* 3485 */ cm.sendOk("검색어는 두글자 이상으로 해주세요.");
            /* 3486 */ cm.dispose();
            return;
        }
        /* 3489 */ if (text.contains("헤어") || text.contains("얼굴")) {
            /* 3490 */ cm.sendOk("헤어, 얼굴 단어는 생략하고 검색해주세요.");
            /* 3491 */ cm.dispose();
            return;
        }
        /* 3494 */ String kk = "";
        /* 3495 */ String chat = "";
        /* 3496 */ String nchat = "";
        /* 3497 */ MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        /* 3498 */ int i = 0;
        /* 3499 */ for (Pair<Integer, String> item : (Iterable<Pair<Integer, String>>) ii.getAllEquips()) {
            /* 3500 */ if (((String) item.getRight()).toLowerCase().contains(text.toLowerCase())) {
                /* 3501 */ String color = "#b";
                /* 3502 */ String isuse = "";
                /* 3503 */ if (cm.getPlayer().getCashWishList().contains(item.getLeft())) {
                    /* 3504 */ color = "#Cgray#";
                    /* 3505 */ isuse = " (선택된 항목)";
                }
                /* 3507 */ if (type == 1 && ii.isCash(((Integer) item.getLeft()).intValue()) && ((Integer) item.getLeft()).intValue() >= 1000000 && ((Integer) item.getLeft()).intValue() / 1000000 == 1) {
                    /* 3508 */ chat = chat + "\r\n" + color + "#L" + item.getLeft() + "##i" + item.getLeft() + " ##z" + item.getLeft() + "#" + isuse;
                    /* 3509 */ i++;
                    continue;
                    /* 3510 */                }
                if (type == 0 && ((Integer) item.getLeft()).intValue() / 10000 >= 2 && ((Integer) item.getLeft()).intValue() / 10000 < 3) {
                    /* 3511 */ chat = chat + "\r\n" + color + "#L" + item.getLeft() + "##i" + item.getLeft() + " ##z" + item.getLeft() + "#" + isuse;
                    /* 3512 */ i++;
                    continue;
                    /* 3513 */                }
                if (type == 2 && ((Integer) item.getLeft()).intValue() / 10000 >= 3 && ((Integer) item.getLeft()).intValue() / 10000 <= 5) {
                    /* 3514 */ chat = chat + "\r\n" + color + "#L" + item.getLeft() + "##i" + item.getLeft() + " ##z" + item.getLeft() + "#" + isuse;
                    /* 3515 */ i++;
                }
            }
        }
        /* 3519 */ if (i != 0) {
            /* 3520 */ kk = kk + "총 " + i + "개 검색되었습니다. 추가 하실 항목을 선택해주세요.";
            /* 3521 */ kk = kk + "\r\n#L0#항목 선택을 마칩니다.  \r\n#L1#항목을 재검색합니다.";
            /* 3522 */ nchat = kk + chat;
            /* 3523 */ cm.sendSimple(nchat);
        } else {
            /* 3525 */ kk = kk + "검색된 아이템이 없습니다.";
            /* 3526 */ cm.sendOk(kk);
            /* 3527 */ cm.dispose();
        }
    }

    public void sendPacket(String args) {
        /* 3532 */ this.c.getSession().writeAndFlush(PacketHelper.sendPacket(args));
    }

    public void enableMatrix() {
        /* 3536 */ MapleQuest quest = MapleQuest.getInstance(1465);
        /* 3537 */ MapleQuestStatus qs = this.c.getPlayer().getQuest(quest);
        /* 3538 */ if (quest != null && qs.getStatus() != 2) {
            /* 3539 */ qs.setStatus((byte) 2);
            /* 3540 */ this.c.getPlayer().updateQuest(this.c.getPlayer().getQuest(quest), true);
        }
    }

    public void gainCorebit(int g) {
        /* 3545 */ getPlayer().setKeyValue(1477, "count", String.valueOf(getPlayer().getKeyValue(1477, "count") + g));
    }

    public long getCorebit() {
        /* 3549 */ return getPlayer().getKeyValue(1477, "count");
    }

    public void setDeathcount(byte de) {
        /* 3553 */ this.c.getPlayer().setDeathCount(de);
        /* 3554 */ this.c.getSession().writeAndFlush(CField.getDeathCount(de));
    }

    public void UserSoulHandle(int selection) {
        /* 3558 */ for (List<Pair<Integer, MapleCharacter>> souls : (Iterable<List<Pair<Integer, MapleCharacter>>>) this.c.getChannelServer().getSoulmatch()) {
            /* 3559 */ this.c.getPlayer().dropMessageGM(6, "1");
            /* 3560 */ if (souls.size() == 1 && ((Integer) ((Pair) souls.get(0)).left).intValue() == 0 && selection == 0) {
                /* 3561 */ souls.add(new Pair(Integer.valueOf(selection), this.c.getPlayer()));
                /* 3562 */ this.c.getPlayer().dropMessageGM(6, "2 : " + souls.size());
                /* 3563 */ this.c.getSession().writeAndFlush(CWvsContext.onUserSoulMatching(selection, souls));
                return;
            }
        }
        /* 3567 */ this.c.getPlayer().dropMessageGM(6, "3");
        /* 3568 */ List<Pair<Integer, MapleCharacter>> chrs = new ArrayList<>();
        /* 3569 */ chrs.add(new Pair(Integer.valueOf(selection), this.c.getPlayer()));
        /* 3570 */ this.c.getSession().writeAndFlush(CWvsContext.onUserSoulMatching(selection, chrs));
        /* 3571 */ if (selection == 0) {
            /* 3572 */ this.c.getPlayer().dropMessageGM(6, "4");
            /* 3573 */ this.c.getChannelServer().getSoulmatch().add(chrs);
        }
    }

    public void startExpRate(int hour) {
        /* 3578 */ this.c.getSession().writeAndFlush(CField.getClock(hour * 60 * 60));
        /* 3579 */ ExpRating();

        /* 3581 */ server.Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                /* 3584 */ NPCConversationManager.this.warp(1000000);
            }
        }, (hour * 60 * 60 * 1000));
    }

    public void ExpRating() {
        /* 3590 */ server.Timer.BuffTimer.getInstance().schedule(new Runnable() {
            public void run() {
                /* 3593 */ if (NPCConversationManager.this.c.getPlayer().getMapId() == 925080000) {
                    /* 3594 */ NPCConversationManager.this.c.getPlayer().gainExp(GameConstants.getExpNeededForLevel(NPCConversationManager.this.c.getPlayer().getLevel()) / 100L, true, false, false);
                    /* 3595 */ NPCConversationManager.this.ExpRating();
                } else {
                    /* 3597 */ NPCConversationManager.this.stopExpRate();
                }
            }
        },
    

    6000L);
   }
   
   public void stopExpRate() {
        /* 3604 */ this.c.getSession().writeAndFlush(CField.getClock(-1));
    }

    public int getFrozenMobCount() {
        /* 3608 */ return getPlayer().getLinkMobCount();
    }

    public void addFrozenMobCount(int a1) {
        /* 3612 */ int val = (getFrozenMobCount() + a1 > 9999) ? 9999 : (getFrozenMobCount() + a1);
        /* 3613 */ getPlayer().setLinkMobCount(val);
        /* 3614 */ getClient().getSession().writeAndFlush(SLFCGPacket.FrozenLinkMobCount(val));
        /* 3615 */ getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(1052230, 3500, "#face1# 몬스터수를 충전했어!", ""));
    }

    public long getStarDustCoin(int type) {
        /* 3619 */ return getPlayer().getStarDustCoin(type);
    }

    public void addStarDustCoin(int type, int a) {
        /* 3623 */ getPlayer().AddStarDustCoin(type, a);
    }

    public void openWeddingPresent(int type, int gender) {
        /* 3627 */ MarriageDataEntry dataEntry = getMarriageAgent().getDataEntry();
        /* 3628 */ if (dataEntry != null) {
            /* 3629 */ if (type == 1) {
                /* 3630 */ List<String> wishes;
                this.c.getPlayer().setWeddingGive(gender);

                /* 3632 */ if (gender == 0) {
                    /* 3633 */ wishes = dataEntry.getGroomWishList();
                } else {
                    /* 3635 */ wishes = dataEntry.getBrideWishList();
                }
                /* 3637 */ this.c.getSession().writeAndFlush(CWvsContext.showWeddingWishGiveDialog(wishes));
                /* 3638 */            } else if (type == 2) {
                List<Item> gifts;
                /* 3640 */ if (gender == 0) {
                    /* 3641 */ gifts = dataEntry.getGroomPresentList();
                } else {
                    /* 3643 */ gifts = dataEntry.getBridePresentList();
                }
                /* 3645 */ this.c.getSession().writeAndFlush(CWvsContext.showWeddingWishRecvDialog(gifts));
            }
        }
    }

    public void ShowDreamBreakerRanking() {
        /* 3651 */ this.c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerRanking(this.c.getPlayer().getName()));
    }
    
        public void gainDonationSkill(int skillid) {
        if (c.getPlayer().getKeyValue(201910, "DonationSkill") < 0) {
            c.getPlayer().setKeyValue(201910, "DonationSkill", "0");
        }

        MapleDonationSkill dskill = MapleDonationSkill.getBySkillId(skillid);
        if (dskill != null && (c.getPlayer().getKeyValue(201910, "DonationSkill") & dskill.getValue()) == 0) {
            int data = (int) c.getPlayer().getKeyValue(201910, "DonationSkill");
            data |= dskill.getValue();
            c.getPlayer().setKeyValue(201910, "DonationSkill", data + "");
            SkillFactory.getSkill(skillid).getEffect(SkillFactory.getSkill(skillid).getMaxLevel()).applyTo(c.getPlayer(), 0);
        }
    }

    public boolean hasDonationSkill(int skillid) {
        if (c.getPlayer().getKeyValue(201910, "DonationSkill") < 0) {
            c.getPlayer().setKeyValue(201910, "DonationSkill", "0");
        }

        MapleDonationSkill dskill = MapleDonationSkill.getBySkillId(skillid);
        if (dskill == null) {
            return false;
        } else if ((c.getPlayer().getKeyValue(201910, "DonationSkill") & dskill.getValue()) == 0) {
            return false;
        }
        return true;
    }

    public String getItemNameById(int itemid) {
        /* 3655 */ String itemname = "";
        /* 3656 */ for (Pair<Integer, String> itemPair : (Iterable<Pair<Integer, String>>) MapleItemInformationProvider.getInstance().getAllItems()) {
            /* 3657 */ if (((Integer) itemPair.getLeft()).intValue() == itemid) {
                /* 3658 */ itemname = (String) itemPair.getRight();
            }
        }
        /* 3661 */ return itemname;
    }

    public long getFWolfMeso() {
        /* 3668 */ if (this.c.getPlayer().getFWolfAttackCount() > 15) {
            /* 3669 */ long BaseMeso = 10000000L;
            /* 3670 */ long FWolfMeso = 0L;

            /* 3672 */ if (this.c.getPlayer().getFWolfDamage() >= 900000000000L) {
                /* 3673 */ FWolfMeso = BaseMeso * 100L;
            } else {
                /* 3675 */ float ratio = (float) (900000000000L / this.c.getPlayer().getFWolfDamage() * 100L);
                /* 3676 */ FWolfMeso = (long) ((float) BaseMeso * ratio);
            }
            /* 3678 */ return FWolfMeso;
        }
        /* 3680 */ return (100000 * this.c.getPlayer().getFWolfAttackCount());
    }

    public long getFWolfEXP() {
        /* 3685 */ long expneed = GameConstants.getExpNeededForLevel(this.c.getPlayer().getLevel());
        /* 3686 */ long exp = 0L;
        /* 3687 */ if (this.c.getPlayer().getFWolfDamage() >= 37500000000000L) {
            /* 3688 */ exp = (long) (expneed * 0.25D);
            /* 3689 */        } else if (this.c.getPlayer().getFWolfDamage() >= 6250000000000L) {
            /* 3690 */ exp = (long) (expneed * 0.2D);
            /* 3691 */        } else if (this.c.getPlayer().getFWolfDamage() >= 625000000000L) {
            /* 3692 */ exp = (long) (expneed * 0.15D);
        } else {
            /* 3694 */ exp = (long) (expneed * 0.1D);
        }

        /* 3697 */ if (this.c.getPlayer().isFWolfKiller()) {
            /* 3698 */ exp = (long) (expneed * 0.5D);
        }
        /* 3700 */ return exp;
    }

    public void showDimentionMirror() {
        /* 3704 */ this.c.getSession().writeAndFlush(CField.dimentionMirror(ServerConstants.mirrors));
    }

    public void warpNettPyramid(boolean hard) {
        /* 3708 */ MapleNettPyramid.warpNettPyramid(this.c.getPlayer(), hard);
    }
   

    public void startDamageMeter() {
        c.getPlayer().setDamageMeter(0);
        MapleMap map = c.getChannelServer().getMapFactory().getMap(120000102);
        map.killAllMonsters(false);
        warp(120000102);
        c.getSession().writeAndFlush(CField.getClock(30));
        c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9063152, 3000, "20초에 허수아비가 소환되고 측정이 시작됩니다.", ""));

        MapleMonster mob = MapleLifeFactory.getMonster(9305653);
        server.Timer.MapTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                map.spawnMonsterOnGroundBelow(mob, new Point(-140, 150));
            }
        }, 5 * 1000);
        server.Timer.MapTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                c.getPlayer().dropMessage(5, "누적 데미지 : " + c.getPlayer().getDamageMeter());
                updateDamageMeter(c.getPlayer(), c.getPlayer().getDamageMeter());
                warp(123456788);
            }
        }, 25 * 1000);
    }

    public static void updateDamageMeter(MapleCharacter chr, long damage) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM damagemeter WHERE cid = ?");
            ps.setInt(1, chr.getId());
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("INSERT INTO damagemeter(cid, name, damage) VALUES (?, ?, ?)");
            ps.setInt(1, chr.getId());
            ps.setString(2, chr.getName());
            ps.setLong(3, damage);
            ps.executeUpdate();
            ps.close();
            con.close();
            chr.setDamageMeter(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getDamageMeterRank(int limit) {
        String text = "#fn나눔고딕 Extrabold##fs13# ";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT " + limit);
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                text += (i != 10 ? " " : "") + i + "위 " + rs.getString("name") + " #r" + Comma(rs.getLong("damage")) + "#e\r\n";
                i++;
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (text.equals("#b")) {
            text = "#r아직까지 딜량 미터기를 갱신한 유저가 없습니다.";
        }
        return text;
    }

    public String DamageMeterRank() {
        String text = "#b";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                text += "#r#e" + (i != 10 ? "0" : "") + i + "#n#b위 #r닉네임#b " + rs.getString("name") + " #r누적 데미지#b " + Comma(rs.getLong("damage")) + "\r\n";
                i++;
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (text.equals("#b")) {
            text = "#r아직까지 딜량 미터기를 갱신한 유저가 없습니다.";
        }
        return text;
    }

    public boolean isDamageMeterRanker(int cid) {
        boolean value = false;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cid") == cid) {
                    value = true;
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    
    public String Comma(long r) {
        String re = "";
        for (int i = String.valueOf(r).length(); i >= 1; i--) {
            if (i != 1 && i != String.valueOf(r).length() && i % 3 == 0) {
                re += ",";
            }
            re += String.valueOf(r).charAt(i - 1);

        }
        return new StringBuilder().append(re).reverse().toString();
    }
    
    public int getDamageMeterRankerId() {
        int value = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                value = rs.getInt("cid");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public final long getDiscordId() {
        /* 3712 */ return this.c.getDiscordId();
    }

    public final void sendMessageToChannel(String text) {
        /* 3716 */ DiscordServer.getInstance().getClientStorage().getClients().forEach(cli -> cli.sendPacket(PacketCreator.sendMessageToChannel(text)));
    }

    public final void sendMessageToUser(long userId, String text) {
        /* 3722 */ DiscordServer.getInstance().getClientStorage().getClients().forEach(cli -> cli.sendPacket(PacketCreator.sendMessageToUser(userId, text)));
    }

    public final void sendEmbeddedMessageToChannel(String title, String message, String thumbnailUrl, byte r, byte g, byte b) {
        /* 3729 */ DiscordServer.getInstance().getClientStorage().getClients().forEach(cli -> cli.sendPacket(PacketCreator.sendEmbedMessageToChannel(title, message, thumbnailUrl, r, g, b)));
    }

    public final void sendEmbeddedMessageToUser(long userId, String title, String message, String thumbnailUrl, byte r, byte g, byte b) {
        /* 3735 */ DiscordServer.getInstance().getClientStorage().getClients().forEach(cli -> cli.sendPacket(PacketCreator.sendEmbedMessageToUser(userId, title, message, thumbnailUrl, r, g, b)));
    }

    public void Entertuto(boolean black) {
        /* 3741 */ Entertuto(black, true, false);
    }

    public void Entertuto(boolean black, boolean effect) {
        /* 3745 */ Entertuto(black, effect, false);
    }

    public void Entertuto(boolean black, boolean effect, boolean blackflame) {
        this.c.send(CField.UIPacket.getDirectionStatus(true));
        this.c.send(SLFCGPacket.SetIngameDirectionMode(true, blackflame, false, false));
        if (effect) {
            server.Timer.EtcTimer.getInstance().schedule(() -> this.c.getSession().writeAndFlush(CField.showSpineScreen(false, false, false, "Effect/Direction18.img/effect/adele/spine/etc/7/skeleton", "new", 0, true, "00")), 1000L);
        }
        server.Timer.EtcTimer.getInstance().schedule(() -> {
            if (black) {
                this.c.send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 1000, 0));
            } else {
                this.c.send(SLFCGPacket.MakeBlind(1, 200, 0, 0, 0, 1000, 0));
            }
        }, effect ? 4300L : 1000L);
        server.Timer.EtcTimer.getInstance().schedule(() -> {
            if (effect) {
                this.c.send(CField.showSpineScreen(false, true, false, "Effect/Direction18.img/effect/adele/spine/etc/5/skeleton", "new", 0, true, "5"));
                this.c.send(CField.showSpineScreen(false, true, false, "Effect/Direction18.img/effect/adele/spine/etc/6/skeleton", "new", 0, true, "6"));
            }
            this.c.send(SLFCGPacket.InGameDirectionEvent("", 1, 1000));
        }, effect ? 5000L : 1000L);
    }
 
 
 
 
 
 
   
   public void Endtuto() {
        /* 3773 */ Endtuto(true);
    }

    public void Endtuto(boolean effect) {
        if (effect) {
            this.c.send(CField.endscreen("5"));
            this.c.send(CField.endscreen("6"));
            this.c.getSession().writeAndFlush(CField.showSpineScreen(false, false, false, "Effect/Direction18.img/effect/adele/spine/etc/7/skeleton", "new", 0, true, "00"));
        }
        server.Timer.EtcTimer.getInstance().schedule(() -> this.c.send(SLFCGPacket.MakeBlind(1, 0, 0, 0, 0, 1300, 0)), effect ? 3500L : 1000L);
        server.Timer.EtcTimer.getInstance().schedule(() -> {
            this.c.send(CField.UIPacket.getDirectionStatus(false));
            this.c.send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
        }, effect ? 5000L : 2000L);
    }
 
 
 
   
   public void sendScreenText(String str, boolean newwrite) {
        /* 3792 */ this.c.send(SLFCGPacket.InGameDirectionEvent(str, new int[]{12, (newwrite == true) ? 1 : 0}));
    }

    public void EnterMonsterPark(int mapid) {
        /* 3796 */ int count = 0;
        /* 3797 */ for (int i = mapid; i < mapid + 500; i += 100) {
            /* 3798 */ count += this.c.getChannelServer().getMapFactory().getMap(i).getNumSpawnPoints();
        }
        /* 3800 */ this.c.getPlayer().setMparkcount(count);
    }

    public void moru(Equip item, Equip item2) {
        /* 3804 */ if (item.getMoru() != 0) {
            /* 3805 */ item2.setMoru(item.getMoru());
        } else {
            /* 3807 */ String lol = Integer.valueOf(item.getItemId()).toString();
            /* 3808 */ String ss = lol.substring(3, 7);
            /* 3809 */ item2.setMoru(Integer.parseInt(ss));
        }
        /* 3811 */ this.c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getFusionAnvil(true, 5062400, 2028093));
        /* 3812 */ this.c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateEquipSlot((Item) item2));
    }

    public String EqpItem() {
        /* 3816 */ String info = "";
        /* 3817 */ int i = 0;
        /* 3818 */ for (Item item : this.c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
            /* 3819 */ Equip Eqp = (Equip) item;
            /* 3820 */ if (Eqp != null) {
                /* 3821 */ if (Eqp.getMoru() > 0) {
                    /* 3822 */ int itemid = Eqp.getItemId() / 10000 * 10000 + Eqp.getMoru();
                    /* 3823 */ info = info + "#L" + Eqp.getItemId() + "# #i" + Eqp.getItemId() + "#  [ #i" + itemid + "# ]  #t" + Eqp.getItemId() + "# #r(모루)#k#b\r\n";
                }

                /* 3827 */ i++;
            }
        }
        /* 3830 */ return info;
    }

    public void sendJobIlust(int type, boolean lumi) {
        /* 3834 */ if (this.lastMsg > -1) {
            return;
        }
        /* 3837 */ this.c.getSession().writeAndFlush(CField.NPCPacket.getIlust(this.id, type, lumi));
    }

    public boolean checkDayItem(String s, int type) {
        /* 3841 */ MapleCharacter chr = this.c.getPlayer();
        /* 3842 */ KoreaCalendar kc = new KoreaCalendar();
        /* 3843 */ String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
        /* 3844 */ if (type == 0) {

            /* 3846 */ chr.addKV(s, today);
            /* 3847 */ return true;
            /* 3848 */        }
        if (type == 1) {
            /* 3850 */ if (chr.getV(s) != null) {
                /* 3851 */ String[] array = chr.getV(s).split("/");
                /* 3852 */ Calendar clear = new GregorianCalendar(Integer.parseInt("20" + array[0]), Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
                /* 3853 */ Calendar ocal = Calendar.getInstance();
                /* 3854 */ int yeal = clear.get(1), days = clear.get(5), day = ocal.get(7), day2 = clear.get(7), maxday = clear.getMaximum(5), month = clear.get(2);
                /* 3855 */ int check = (day2 == 5) ? 7 : ((day2 == 6) ? 6 : ((day2 == 7) ? 5 : 0));
                /* 3856 */ if (check == 0) {
                    /* 3857 */ for (int i = day2; i < 5; i++) {
                        /* 3858 */ check++;
                    }
                }
                /* 3861 */ int afterday = days + check;
                /* 3862 */ if (afterday > maxday) {
                    /* 3863 */ afterday -= maxday;
                    /* 3864 */ month++;
                }
                /* 3866 */ if (month > 12) {
                    /* 3867 */ yeal++;
                    /* 3868 */ month = 1;
                }
                /* 3870 */ Calendar after = new GregorianCalendar(yeal, month, afterday);
                /* 3871 */ if (after.getTimeInMillis() > System.currentTimeMillis()) {
                    /* 3873 */ return false;
                }
            }
        }
        /* 3877 */ return true;
    }

    public long ExpPocket(int type) {
        /* 3881 */ long t = 0L;
        /* 3882 */ long time = (System.currentTimeMillis() - Long.parseLong(this.c.getCustomData(247, "lastTime"))) / 1000L;
        /* 3883 */ if (time > 43200L) {
            /* 3884 */ time = 43200L;
        }
        /* 3886 */ long gainexp = time / 10L * GameConstants.ExpPocket(this.c.getPlayer().getLevel());
        /* 3887 */ if (type == 1) {
            /* 3888 */ t = time;
        } else {
            /* 3890 */ t = gainexp;
        }
        /* 3892 */ return t;
    }

    public void SelectQuest(String quest, int quest1, int quest2, int count) {
        /* 3896 */ List<Integer> QuestList = new ArrayList<>();
        /* 3897 */ List<Integer> SelectQuest = new ArrayList<>();
        /* 3898 */ for (int i = quest1; i < quest2; i++) {
            /* 3899 */ QuestList.add(Integer.valueOf(i));
        }
        /* 3901 */ while (SelectQuest.size() < count) {
            /* 3902 */ int questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
            /* 3903 */ boolean no = false;
            /* 3904 */ switch (questid) {
                case 35566:
                case 35567:
                case 35568:
                case 35569:
                case 35583:
                case 35584:
                case 35585:
                case 35586:
                case 35587:
                case 35588:
                case 35589:
                case 39109:
                case 39110:
                case 39120:
                case 39128:
                case 39129:
                case 39130:
                case 39137:
                case 39138:
                case 39139:
                case 39140:
                    /* 3926 */ no = true;
                    break;
                default:
                    /* 3929 */ no = false;
                    break;
            }
            /* 3932 */ while (SelectQuest.contains(Integer.valueOf(questid)) || no) {
                /* 3933 */ questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
                /* 3934 */ switch (questid) {
                    case 35566:
                    case 35567:
                    case 35568:
                    case 35569:
                    case 35583:
                    case 35584:
                    case 35585:
                    case 35586:
                    case 35587:
                    case 35588:
                    case 35589:
                    case 39109:
                    case 39110:
                    case 39120:
                    case 39128:
                    case 39129:
                    case 39130:
                    case 39137:
                    case 39138:
                    case 39139:
                    case 39140:
                        /* 3956 */ no = true;
                        continue;
                }
                /* 3959 */ no = false;
            }

            /* 3963 */ SelectQuest.add(Integer.valueOf(questid));
        }
        /* 3965 */ String q = "";
        /* 3966 */ for (int j = 0; j < SelectQuest.size(); j++) {
            /* 3967 */ q = q + SelectQuest.get(j) + "";
            /* 3968 */ if (j != SelectQuest.size() - 1) {
                /* 3969 */ q = q + ",";
            }
        }
        /* 3972 */ this.c.getPlayer().addKV(quest, q);
    }

    public int ReplaceQuest(String quest, int quest1, int quest2, int anotherquest) {
        /* 3976 */ List<Integer> QuestList = new ArrayList<>();
        /* 3977 */ List<Integer> SelectQuest = new ArrayList<>();
        /* 3978 */ List<Integer> MyQuest = new ArrayList<>();
        /* 3979 */ for (int i = quest1; i < quest2; i++) {
            /* 3980 */ QuestList.add(Integer.valueOf(i));
        }

        /* 3983 */ String[] KeyValue = this.c.getPlayer().getV(quest).split(",");
        int j;
        /* 3984 */ for (j = 0; j < KeyValue.length; j++) {
            /* 3985 */ MyQuest.add(Integer.valueOf(Integer.parseInt(KeyValue[j])));
        }

        /* 3988 */ for (j = 0; j < KeyValue.length; j++) {
            /* 3989 */ if (Integer.parseInt(KeyValue[j]) != anotherquest) {
                /* 3990 */ SelectQuest.add(Integer.valueOf(Integer.parseInt(KeyValue[j])));
            }
        }
        /* 3993 */ int questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
        while (true) {
            /* 3995 */ questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
            /* 3996 */ boolean no = false;
            /* 3997 */ switch (questid) {
                case 35566:
                case 35567:
                case 35568:
                case 35569:
                case 35583:
                case 35584:
                case 35585:
                case 35586:
                case 35587:
                case 35588:
                case 35589:
                case 39109:
                case 39110:
                case 39120:
                case 39128:
                case 39129:
                case 39130:
                case 39137:
                case 39138:
                case 39139:
                case 39140:
                    /* 4019 */ no = true;
                    break;
                default:
                    /* 4022 */ no = false;
                    break;
            }
            /* 4025 */ if (questid != anotherquest && !SelectQuest.contains(Integer.valueOf(questid)) && !no) {
                /* 4026 */ SelectQuest.add(Integer.valueOf(questid));

                /* 4030 */ String q = "";
                /* 4031 */ for (int k = 0; k < SelectQuest.size(); k++) {
                    /* 4032 */ q = q + SelectQuest.get(k) + "";
                    /* 4033 */ if (k != SelectQuest.size() - 1) {
                        /* 4034 */ q = q + ",";
                    }
                }
                /* 4037 */ this.c.getPlayer().addKV(quest, q);
                /* 4038 */ return questid;
            }
        }
    }

    public void cancelSkillsbuff() {
        /* 4042 */ for (Pair<SecondaryStat, SecondaryStatValueHolder> data : (Iterable<Pair<SecondaryStat, SecondaryStatValueHolder>>) this.c.getPlayer().getEffects()) {
            /* 4043 */ SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder) data.right;
            /* 4044 */ if (SkillFactory.getSkill(mbsvh.effect.getSourceId()) != null
                    && /* 4045 */ mbsvh.effect.getSourceId() != 80002282 && mbsvh.effect.getSourceId() != 2321055) /* 4046 */ {
                this.c.getPlayer().cancelEffect(mbsvh.effect, Arrays.asList(new SecondaryStat[]{(SecondaryStat) data.left}));
            }
        }
    }
}


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\scripting\NPCConversationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
