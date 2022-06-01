package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.Skill;
import client.SkillFactory;
import constants.GameConstants;
import constants.KoreaCalendar;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.world.World;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuildResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.SecondaryStatEffect;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class GuildHandler {

    public static final void DenyGuildRequest(String from, MapleClient c) {
        MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterByName(from);
        if (cfrom != null) {
            cfrom.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.denyGuildInvitation(c.getPlayer().getName()));
        }
    }

    private static boolean isGuildNameAcceptable(String name) throws UnsupportedEncodingException {
        if ((name.getBytes("EUC-KR")).length < 2 || (name.getBytes("EUC-KR")).length > 12) {
            return false;
        }
        return true;
    }

    private static void respawnPlayer(MapleCharacter mc) {
        if (mc.getMap() == null) {
            return;
        }
        mc.getMap().broadcastMessage(CField.loadGuildIcon(mc));
    }

    public static final void GuildCancelRequest(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        MapleGuild guild = World.Guild.getGuild(slea.readInt());
        if (c == null || chr == null || guild == null) {
            return;
        }
        chr.setKeyValue(26015, "name", "");
        chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
        guild.removeRequest(chr.getId());
        c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDeny(chr, guild));
        List<MapleGuild> g = new ArrayList<>();
        for (MapleGuild guilds : World.Guild.getGuilds()) {
            if (guilds.getRequest(c.getPlayer().getId()) != null) {
                g.add(guilds);
            }
        }
        c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestListGuild(g));
        c.getSession().writeAndFlush(CWvsContext.GuildPacket.RecruitmentGuild(c.getPlayer()));
    }

    public static final void GuildJoinRequest(LittleEndianAccessor slea, MapleCharacter chr) {
        int gid = slea.readInt();
        System.out.println(slea);
        String requestss = slea.readMapleAsciiString();
        slea.skip(10);
        if (chr == null || gid <= 0) {
            return;
        }
        if (chr.getKeyValue(26015, "time") + 60000L >= System.currentTimeMillis()) {
            chr.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.DelayRequest());
            return;
        }
        MapleGuild g = World.Guild.getGuild(gid);
        MapleGuildCharacter mgc2 = new MapleGuildCharacter(chr);
        mgc2.setGuildId(gid);
        mgc2.setRequest(requestss);
        if (request.get(Integer.valueOf(chr.getId())) == null) {
            if (g.addRequest(mgc2)) {
                g.broadcast(CWvsContext.GuildPacket.addRegisterRequest(mgc2));
                chr.dropMessage(5, "[" + g.getName() + "] 길드 가입 요청이 성공 하였습니다.");
            }
            chr.setKeyValue(26015, "name", g.getName());
            chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
        } else {
            request.remove(Integer.valueOf(chr.getId()));
            if (g.addRequest(mgc2)) {
                chr.dropMessage(5, "[" + g.getName() + "] 길드 가입 요청이 성공 하였습니다.");
                g.broadcast(CWvsContext.GuildPacket.addRegisterRequest(mgc2));
            }
            chr.setKeyValue(26015, "name", "");
            chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
        }
        List<MapleGuild> gs = new ArrayList<>();
        for (MapleGuild guilds : World.Guild.getGuilds()) {
            if (guilds.getRequest(chr.getId()) != null) {
                gs.add(guilds);
            }
        }
        chr.getClient().send(CWvsContext.GuildPacket.RequestListGuild(gs));
        chr.getClient().send(CWvsContext.GuildPacket.RecruitmentGuild(chr));
        g.writeToDB(false);
    }

    public static final void GuildJoinDeny(LittleEndianAccessor slea, MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        byte action = slea.readByte();
        for (int i = 0; i < action; i++) {
            int cid = slea.readInt();
            if (chr.getGuildId() > 0 && chr.getGuildRank() <= 2) {
                MapleGuild g = chr.getGuild();
                if (chr.getGuildRank() <= 2) {
                    g.removeRequest(cid);
                    int ch = World.Find.findChannel(cid);
                    if (ch < 0) {
                        return;
                    }
                    MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(cid);
                    c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDeny(c, g));
                    chr.setKeyValue(26015, "name", "");
                    request.put(Integer.valueOf(cid), Long.valueOf(System.currentTimeMillis()));
                } else {
                    chr.dropMessage(6, "길드 권한이 부족합니다.");
                }
            }
        }
    }

    public static final void GuildRegisterAccept(LittleEndianAccessor slea, MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        byte action = slea.readByte();
        for (int i = 0; i < action; i++) {
            int cid = slea.readInt();
            if (chr.getGuildId() > 0 && chr.getGuildRank() <= 2) {
                MapleGuild g = chr.getGuild();
                if (chr.getGuildRank() <= 2 && g != null) {
                    MapleCharacter c = null;
                    for (ChannelServer cs : ChannelServer.getAllInstances()) {
                        c = cs.getPlayerStorage().getCharacterById(cid);
                        if (c != null) {
                            MapleGuildCharacter temp = g.getRequest(cid);
                            g.addGuildMember(temp);
                            c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(chr));
                            g.removeRequest(cid);
                            c.setGuildId(g.getId());
                            c.setGuildRank((byte) 5);
                            c.saveGuildStatus();
                            c.setKeyValue(26015, "name", "");
                            c.getClient().send(CWvsContext.GuildPacket.guildLoadAattendance());
                            c.dropMessage(5, "`" + g.getName() + "` 길드에 가입 되었습니다.");
                            for (MapleGuild guilds : World.Guild.getGuilds()) {
                                if (guilds.getRequest(c.getId()) != null) {
                                    guilds.removeRequest(c.getId());
                                }
                            }
                            respawnPlayer(c);
                            break;
                        }
                    }
                    if (c == null) {
                        MapleGuildCharacter temp = OfflineMapleGuildCharacter(cid, chr.getGuildId());
                        if (temp != null) {
                            temp.setOnline(false);
                            g.addGuildMember(temp);
                            MapleGuild.setOfflineGuildStatus(g.getId(), (byte) 5, 0, (byte) 5, cid);
                            g.removeRequest(cid);
                            for (MapleGuild guilds : World.Guild.getGuilds()) {
                                if (guilds.getRequest(temp.getId()) != null) {
                                    guilds.removeRequest(temp.getId());
                                }
                            }
                        } else {
                            chr.dropMessage(5, "존재하지 않는 캐릭터입니다.");
                        }
                    }
                } else {
                    chr.dropMessage(6, "길드 권한이 부족합니다.");
                }
            }
        }
    }

    public static final MapleGuildCharacter OfflineMapleGuildCharacter(int cid, int gid) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM characters where id = ?");
            ps.setInt(1, cid);
            rs = ps.executeQuery();
            if (rs.next()) {
                byte gRank = rs.getByte("guildrank"), aRank = rs.getByte("alliancerank");
                return new MapleGuildCharacter(cid, rs.getShort("level"), rs.getString("name"), (byte) -1, rs.getInt("job"), gRank, rs.getInt("guildContribution"), aRank, gid, false, 0);
            }
            ps.close();
            rs.close();
        } catch (SQLException se) {
            System.err.println("Error Laod Offline MapleGuildCharacter");
            se.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GuildHandler.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GuildHandler.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static final void GuildRequest(int guildid, MapleCharacter player) {
        player.dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
    }

    public static void cancelGuildRequest(MapleClient c, MapleCharacter player) {
        player.dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
    }

    public static void SendGuild(LittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
    }

    private static class Invited {

        public String name;

        public int gid;

        public long expiration;

        public Invited(String n, int id) {
            this.name = n.toLowerCase();
            this.gid = id;
            this.expiration = System.currentTimeMillis() + 3600000L;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Invited)) {
                return false;
            }
            Invited oth = (Invited) other;
            return (this.gid == oth.gid && this.name.equals(oth));
        }
    }

    private static List<Invited> invited = new LinkedList<>();

    private static Map<Integer, Long> request = new LinkedHashMap<>();

    private static long nextPruneTime = System.currentTimeMillis() + 300000L;

    public static final void Guild(LittleEndianAccessor slea, MapleClient c) {
        /* 329 */ if (System.currentTimeMillis() >= nextPruneTime) {
            /* 330 */ Iterator<Invited> itr = invited.iterator();

            /* 332 */ while (itr.hasNext()) {
                /* 333 */ Invited inv = itr.next();
                /* 334 */ if (System.currentTimeMillis() >= inv.expiration) {
                    /* 335 */ itr.remove();
                }
            }
            /* 338 */ nextPruneTime += 300000L;
        }
        try {
            String str1;
            int cid;
            String[] arrayOfString1;
            int[] roles;
            String ranks[], notice;
            MapleGuild mapleGuild1;
            List<MapleGuild> g;
            String name;
            MapleGuild guild;
            short mode;
            Skill skilli;
            int sid;
            String guildName, str2;
            byte newRank;
            int i, arrayOfInt1[];
            byte isCustomImage;
            MapleGuildResponse mgr;
            int size;
            String text;
            int eff, guildId, j;
            Invited inv;
            int option;
            SecondaryStatEffect skillid;
            MapleGuild mapleGuild2;
            String[] arrayOfString2;
            int a, k;
            KoreaCalendar kc;
            /* 341 */ int action = slea.readByte();
            /* 342 */ switch (action) {
                case 1:
                    /* 344 */ str1 = slea.readMapleAsciiString();
                    /* 345 */ c.getPlayer().setGuildName(str1);
                    /* 346 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.genericGuildMessage(42, str1));
                    break;

                case 6:
                    /* 350 */ cid = slea.readInt();
                    /* 351 */ str2 = slea.readMapleAsciiString();

                    /* 353 */ if (cid != c.getPlayer().getId() || !str2.equals(c.getPlayer().getName()) || c.getPlayer().getGuildId() <= 0) {
                        return;
                    }
                    /* 356 */ World.Guild.leaveGuild(c.getPlayer().getMGC());
                    break;

                case 7:
                    /* 360 */ cid = slea.readInt();
                    /* 361 */ str2 = slea.readMapleAsciiString();

                    /* 363 */ if (c.getPlayer().getGuildRank() > 2 || c.getPlayer().getGuildId() <= 0) {
                        return;
                    }
                    /* 366 */ World.Guild.expelMember(c.getPlayer().getMGC(), str2, cid);
                    /* 367 */ respawnPlayer(c.getPlayer());
                    break;

                case 10:
                    /* 371 */ cid = slea.readInt();
                    /* 372 */ newRank = slea.readByte();

                    /* 374 */ if (newRank <= 1 || newRank > 5 || c.getPlayer().getGuildRank() > 2 || (newRank <= 2 && c.getPlayer().getGuildRank() != 1) || c.getPlayer().getGuildId() <= 0) {
                        return;
                    }

                    /* 378 */ World.Guild.changeRank(c.getPlayer().getGuildId(), cid, newRank);
                    break;

                case 12: //361 ok
                    /* 382 */ if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
                        return;
                    }
                    byte type = (byte) (slea.readByte() - 1);
                    String ranks1 = slea.readMapleAsciiString();
                    int role = type != 0 ? slea.readInt() : -1;
                    World.Guild.changeRankTitleRole(c.getPlayer(), ranks1, role, type);
                    break;
                case 17: // 362 ok
                    /* 417 */ notice = slea.readMapleAsciiString();
                    /* 418 */ if (notice.length() > 100 || c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
                        return;
                    }
                    /* 421 */ World.Guild.setGuildNotice(c.getPlayer(), notice);
                    break;

                case 16: // 355 +1
                    /* 425 */ mapleGuild1 = World.Guild.getGuild(c.getPlayer().getGuildId());
                    /* 426 */ if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
                        /* 427 */ c.getPlayer().dropMessage(1, "길드가 없거나 마스터가 아닙니다.");

                        return;
                    }
                    /* 431 */ isCustomImage = slea.readByte();
                    /* 432 */ if (isCustomImage == 0) {
                        /*
                        if (mapleGuild1.getGP() >= 150000 && mapleGuild1.getLevel() >= 2) {
                            mapleGuild1.setGuildGP(mapleGuild1.getGP() - 150000);
                        } else {
                            c.getPlayer().getClient().send(CWvsContext.GuildPacket.genericGuildMessage(136));
                            return;
                        }
                         */
 /* 439 */ short bg = slea.readShort();
                        /* 440 */ byte bgcolor = slea.readByte();
                        /* 441 */ short logo = slea.readShort();
                        /* 442 */ byte logocolor = slea.readByte();

                        /* 444 */ World.Guild.setGuildEmblem(c.getPlayer(), bg, bgcolor, logo, logocolor);
                    } else {
                        /* 446 */ if (mapleGuild1.getGP() >= 250000) {
                            /* 447 */ mapleGuild1.setGuildGP(mapleGuild1.getGP() - 250000);
                        } else {
                            /* 449 */ c.getPlayer().dropMessage(1, "[알림] GP가 부족합니다.");
                            /* 450 */ c.getPlayer().getClient().send(CWvsContext.enableActions(c.getPlayer()));
                            return;
                        }
                        /* 453 */ int m = slea.readInt();
                        /* 454 */ byte[] imgdata = new byte[m];
                        /* 455 */ for (int n = 0; n < m; n++) {
                            /* 456 */ imgdata[n] = slea.readByte();
                        }

                        /* 459 */ World.Guild.setGuildCustomEmblem(c.getPlayer(), imgdata);
                    }

                    /* 462 */ respawnPlayer(c.getPlayer());
                    break;

                case 29: //아직
                    /* 466 */ mapleGuild1 = World.Guild.getGuild(c.getPlayer().getGuildId());
                    /* 467 */ if (mapleGuild1 == null) {
                        return;
                    }
                    /* 470 */ if (mapleGuild1.getGP() < 50000) {
                        /* 471 */ c.getPlayer().dropMessage(1, "GP가 부족합니다.");
                        /* 472 */ c.send(CWvsContext.enableActions(c.getPlayer()));
                        return;
                    }
                    /* 475 */ mapleGuild1.setGuildGP(mapleGuild1.getGP() - 50000);
                    /* 476 */ mapleGuild1.getSkills().clear();
                    /* 477 */ mapleGuild1.broadcast(CWvsContext.GuildPacket.showGuildInfo(mapleGuild1));
                    /* 478 */ c.getPlayer().dropMessage(1, "길드스킬 초기화가 완료 되었습니다. 길드창을 닫았다 열어주세요.");
                    break;
                case 30: //355 OK
                    cid = slea.readInt();
                    if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 1) {
                        return;
                    }
                    World.Guild.setGuildLeader(c.getPlayer().getGuildId(), cid);
                    break;
                case 32:
                    /* 482 */ mapleGuild1 = World.Guild.getGuild(slea.readInt());
                    /* 483 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.LooksGuildInformation(mapleGuild1));
                    break;

                case 33:
                    /* 487 */ g = new ArrayList<>();
                    /* 488 */ for (MapleGuild mapleGuild : World.Guild.getGuilds()) {
                        /* 489 */ if (mapleGuild.getRequest(c.getPlayer().getId()) != null) {
                            /* 490 */ g.add(mapleGuild);
                        }
                    }
                    /* 493 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestListGuild(g));
                    break;

                case 34: //355 -3
                    /* 497 */ if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
                        return;
                    }
                    /* 500 */ name = slea.readMapleAsciiString();
                    /* 501 */ mgr = MapleGuild.sendInvite(c, name);

                    /* 503 */ if (mgr != null) {
                        /* 504 */ c.getSession().writeAndFlush(mgr.getPacket());
                        break;
                    }
                    /* 506 */ inv = new Invited(name, c.getPlayer().getGuildId());
                    /* 507 */ if (!invited.contains(inv)) {
                        /* 508 */ invited.add(inv);
                    }
                    break;

                case 35: // 362 ok
                    /* 514 */ guild = World.Guild.getGuild(c.getPlayer().getGuildId());
                    /* 515 */ size = 0;
                    /* 516 */ for (MapleGuildCharacter member : guild.getMembers()) {
                        /* 517 */ if (member.getLastAttendance(member.getId()) == GameConstants.getCurrentDateday()) {
                            /* 518 */ size++;
                        }
                    }
                    /* 521 */ c.getPlayer().setLastAttendance(GameConstants.getCurrentDateday());
                    /* 522 */ guild.setAfterAttance(guild.getAfterAttance() + 30);
                    /* 523 */ if (size == 10 || size == 30 || size == 60 || size == 100) {
                        /* 524 */ int point = (size == 100) ? 2000 : ((size == 60) ? 1000 : ((size == 30) ? 100 : 50));
                        /* 525 */ guild.setAfterAttance(guild.getAfterAttance() + point);
                        /* 526 */ guild.setGuildFame(guild.getFame() + point);
                        /* 527 */ guild.setGuildGP(guild.getGP() + point / 100 * 30);
                    }
                    /* 529 */ c.getPlayer().saveGuildStatus();
                    /* 530 */ World.Guild.gainContribution(guild.getId(), 30, c.getPlayer().getId());
                    /* 531 */ GuildBroadCast(CWvsContext.GuildPacket.guildAattendance(guild, c.getPlayer()), guild);
                    /* 532 */ if (guild.getFame() >= GameConstants.getGuildExpNeededForLevel(guild.getLevel())) {
                        /* 533 */ guild.setGuildLevel(guild.getLevel() + 1);
                        /* 534 */ GuildBroadCast(CWvsContext.serverNotice(5, "", "<길드> 길드의 레벨이 상승 하였습니다."), guild);
                    }
                    break;

                case 39: //355 -10
                    /* 539 */ mode = slea.readShort();
                    /* 540 */ text = slea.readMapleAsciiString();
                    /* 541 */ if (mode == 4) {
                        /* 542 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.RecruitmentGuild(c.getPlayer()));
                        break;
                    }
                    /* 544 */ option = slea.readShort();
                    /* 545 */ slea.skip(2);
                    /* 546 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.showSearchGuildInfo(c.getPlayer(), World.Guild.getGuildsByName(text, (option == 1), (byte) mode), text, (byte) mode, option));
                    break;

                case 44: // 355 -10
                    /* 552 */ skilli = SkillFactory.getSkill(slea.readInt());
                    /* 553 */ if (c.getPlayer().getGuildId() <= 0 || skilli == null || skilli.getId() < 91000000) {
                        return;
                    }
                    /* 556 */ eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId()) + 1;
                    /* 557 */ if (eff > skilli.getMaxLevel()) {
                        return;
                    }
                    /* 560 */ skillid = skilli.getEffect(eff);
                    /* 561 */ if (skillid.getReqGuildLevel() < 0 || c.getPlayer().getMeso() < skillid.getPrice()) {
                        return;
                    }
                    /* 564 */ if (World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(), c.getPlayer().getId()));
                    break;

                case 45:
                    /* 570 */ if (c.getPlayer().getGuildId() <= 0) {
                        return;
                    }
                    /* 573 */ sid = slea.readInt();
                    /* 574 */ eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), sid);
                    /* 575 */ SkillFactory.getSkill(sid).getEffect(eff).applyTo(c.getPlayer());
                    /* 576 */ c.getSession().writeAndFlush(CField.skillCooldown(sid, 3600000));
                    /* 577 */ c.getPlayer().addCooldown(sid, System.currentTimeMillis(), 3600000L);
                    break;

                case 59: // 355 -10
                    /* 581 */ if (c.getPlayer().getGuildId() > 0) {
                        /* 582 */ c.getPlayer().dropMessage(1, "이미 길드에 가입되어 있어 길드를 만들 수 없습니다.");
                        return;
                    }
                    /* 584 */ if (c.getPlayer().getMeso() < 5000000L) {
                        /* 585 */ c.getPlayer().dropMessage(1, "길드 제작에 필요한 메소 [500만 메소] 가 충분하지 않습니다.");
                        return;
                    }
                    /* 588 */ guildName = c.getPlayer().getGuildName();
                    /* 589 */ if (!isGuildNameAcceptable(guildName)) {
                        /* 590 */ c.getPlayer().dropMessage(1, "해당 길드 이름은 만들 수 없습니다.");
                        return;
                    }
                    /* 593 */ guildId = World.Guild.createGuild(c.getPlayer().getId(), guildName);
                    /* 594 */ if (guildId == 0) {
                        /* 595 */ c.getPlayer().dropMessage(1, "잠시후에 다시 시도 해주세요.");
                        return;
                    }
                    /* 598 */ c.getPlayer().gainMeso(-5000000L, true, true);
                    /* 599 */ c.getPlayer().setGuildId(guildId);
                    /* 600 */ c.getPlayer().setGuildRank((byte) 1);
                    /* 601 */ c.getPlayer().saveGuildStatus();
                    /* 602 */ mapleGuild2 = World.Guild.getGuild(guildId);
                    /* 603 */ arrayOfString2 = new String[5];
                    /* 604 */ arrayOfString2[0] = "마스터";
                    /* 605 */ arrayOfString2[1] = "부마스터";
                    /* 606 */ a = 1;
                    /* 607 */ for (k = 2; k < 5; k++) {
                        /* 608 */ arrayOfString2[k] = "길드원" + a;
                        /* 609 */ a++;
                    }
                    /* 611 */ mapleGuild2.changeRankTitle(c.getPlayer(), arrayOfString2);
                    /* 612 */ mapleGuild2.setLevel(1);
                    /* 613 */ kc = new KoreaCalendar();
                    /* 614 */ mapleGuild2.setLastResetDay(Integer.parseInt(kc.getYears() + kc.getMonths() + kc.getDays()));
                    /* 615 */ World.Guild.setGuildMemberOnline(c.getPlayer().getMGC(), true, c.getChannel());
                    /* 616 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildInfo(c.getPlayer()));
                    /* 617 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(c.getPlayer()));

                    /* 619 */ respawnPlayer(c.getPlayer());
                    break;
            }

            /* 697 */        } catch (Exception e) {
            /* 698 */ e.printStackTrace();
        }
    }

    public static void guildRankingRequest(byte type, MapleClient c) {
        /* 703 */ c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildRanks((byte) type, c, MapleGuildRanking.getInstance()));
    }

    public static void GuildBroadCast(byte[] packet, MapleGuild guild) {
        /* 707 */ for (ChannelServer cs : ChannelServer.getAllInstances()) {
            /* 708 */ for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
                /* 709 */ if (chr.getGuildId() == guild.getId()) /* 710 */ {
                    chr.getClient().getSession().writeAndFlush(packet);
                }
            }
        }
    }
}
