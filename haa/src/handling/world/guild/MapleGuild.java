package handling.world.guild;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.channel.ChannelServer;
import handling.channel.handler.GuildHandler;
import handling.world.World;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.sql.rowset.serial.SerialBlob;
import log.DBLogger;
import log.LogType;
import server.SecondaryStatEffect;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PacketHelper;

public class MapleGuild implements Comparable<MapleGuild> {

    public static final long serialVersionUID = 6322150443228168192L;
    private final List<MapleGuildCharacter> members = new CopyOnWriteArrayList<MapleGuildCharacter>();
    private final List<MapleGuildCharacter> requests = new ArrayList<MapleGuildCharacter>();
    private final Map<Integer, MapleGuildSkill> guildSkills = new HashMap<Integer, MapleGuildSkill>();
    private final String[] rankTitles = new String[5];
    private final int[] rankRoles = new int[5];
    private String name;
    private String notice;
    private double guildScore = 0.0;
    private int id;
    private int gp;
    private int logo;
    private int logoColor;
    private int leader;
    private int capacity;
    private int logoBG;
    private int logoBGColor;
    private int signature;
    private int level;
    private int noblessskillpoint;
    private int guildlevel;
    private int fame;
    private int beforeattance;
    private int afterattance;
    private int lastResetDay;
    private int weekReputation;
    private boolean bDirty = true;
    private boolean proper = true;
    private int allianceid = 0;
    private int invitedid = 0;
    private byte[] customEmblem;
    private boolean init = false;
    private boolean changed_skills = false;
    private boolean changed_requests = false;

    private enum BCOp {
        /*   55 */ NONE, DISBAND, EMBELMCHANGE;
    }

    public MapleGuild(int guildid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid = ?");
            ps.setInt(1, guildid);
            rs = ps.executeQuery();
            if (!rs.first()) {
                rs.close();
                ps.close();
                this.id = -1;
                return;
            }
            this.id = guildid;
            this.name = rs.getString("name");
            this.gp = rs.getInt("GP");
            this.fame = rs.getInt("fame");
            this.guildlevel = rs.getInt("level");
            this.logo = rs.getInt("logo");
            this.logoColor = rs.getInt("logoColor");
            this.logoBG = rs.getInt("logoBG");
            this.logoBGColor = rs.getInt("logoBGColor");
            this.capacity = rs.getInt("capacity");
            this.rankTitles[0] = rs.getString("rank1title");
            this.rankTitles[1] = rs.getString("rank2title");
            this.rankTitles[2] = rs.getString("rank3title");
            this.rankTitles[3] = rs.getString("rank4title");
            this.rankTitles[4] = rs.getString("rank5title");
            this.rankRoles[0] = rs.getInt("rank1role");
            this.rankRoles[1] = rs.getInt("rank2role");
            this.rankRoles[2] = rs.getInt("rank3role");
            this.rankRoles[3] = rs.getInt("rank4role");
            this.rankRoles[4] = rs.getInt("rank5role");
            this.leader = rs.getInt("leader");
            this.notice = rs.getString("notice");
            this.signature = rs.getInt("signature");
            this.allianceid = rs.getInt("alliance");
            this.guildScore = rs.getInt("score");
            this.beforeattance = rs.getInt("beforeattance");
            this.afterattance = rs.getInt("afterattance");
            this.lastResetDay = rs.getInt("lastResetDay");
            this.weekReputation = rs.getInt("weekReputation");
            this.noblessskillpoint = rs.getInt("noblesspoint");
            Blob custom = rs.getBlob("customEmblem");
            if (custom != null) {
                this.customEmblem = custom.getBytes(1L, (int) custom.length());
            }
            rs.close();
            ps.close();
            MapleGuildAlliance alliance = World.Alliance.getAlliance(this.allianceid);
            if (alliance == null) {
                this.allianceid = 0;
            }
            ps = con.prepareStatement("SELECT id, name, level, job, guildrank, guildContribution, alliancerank, lastattendance FROM characters WHERE guildid = ? ORDER BY guildrank ASC, name ASC", 1008);
            ps.setInt(1, guildid);
            rs = ps.executeQuery();
            if (!rs.first()) {
                //System.err.println("No members in guild " + this.id + ".  Impossible... guild is disbanding");
                rs.close();
                ps.close();
                this.writeToDB(true);
                this.proper = false;
                return;
            }
            boolean leaderCheck = false;
            int gFix = 0;
            int aFix = 0;
            do {
                int cid = rs.getInt("id");
                byte gRank = rs.getByte("guildrank");
                byte by = rs.getByte("alliancerank");
                if (cid == this.leader) {
                    leaderCheck = true;
                    if (gRank != 1) {
                        gRank = 1;
                        gFix = 1;
                    }
                    if (alliance != null) {
                        if (alliance.getLeaderId() == cid && by != 1) {
                            by = 1;
                            aFix = 1;
                        } else if (alliance.getLeaderId() != cid && by != 2) {
                            by = 2;
                            aFix = 2;
                        }
                    }
                } else {
                    if (gRank == 1) {
                        gRank = 2;
                        gFix = 2;
                    }
                    if (by < 3) {
                        by = 3;
                        aFix = 3;
                    }
                }
                this.members.add(new MapleGuildCharacter(cid, rs.getShort("level"), rs.getString("name"), (byte) -1, rs.getInt("job"), gRank, rs.getInt("guildContribution"), by, guildid, false, rs.getInt("lastattendance")));
            } while (rs.next());
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM guildsrequest WHERE gid = ?", 1008);
            ps.setInt(1, guildid);
            rs = ps.executeQuery();
            ArrayList<Pair<Integer, Integer>> request = new ArrayList<Pair<Integer, Integer>>();
            while (rs.next()) {
                request.add(new Pair<Integer, Integer>(rs.getInt("cid"), rs.getInt("gid")));
            }
            rs.close();
            ps.close();
            for (Pair pair : request) {
                ps = con.prepareStatement("SELECT id, name, level, job, guildrank, guildContribution, alliancerank FROM characters WHERE id = ?", 1008);
                ps.setInt(1, (Integer) pair.left);
                rs = ps.executeQuery();
                while (rs.next()) {
                    byte gRank = rs.getByte("guildrank");
                    byte aRank = rs.getByte("alliancerank");
                    if ((Integer) pair.left == this.leader) {
                        leaderCheck = true;
                        if (gRank != 1) {
                            gRank = 1;
                            gFix = 1;
                        }
                        if (alliance != null) {
                            if (alliance.getLeaderId() == ((Integer) pair.left).intValue() && aRank != 1) {
                                aRank = 1;
                                aFix = 1;
                            } else if (alliance.getLeaderId() != ((Integer) pair.left).intValue() && aRank != 2) {
                                aRank = 2;
                                aFix = 2;
                            }
                        }
                    } else {
                        if (gRank == 1) {
                            gRank = 2;
                            gFix = 2;
                        }
                        if (aRank < 3) {
                            aRank = 3;
                            aFix = 3;
                        }
                    }
                    this.requests.add(new MapleGuildCharacter((Integer) pair.left, rs.getShort("level"), rs.getString("name"), (byte) -1, rs.getInt("job"), gRank, rs.getInt("guildContribution"), aRank, (Integer) pair.right, false, 0));
                }
                rs.close();
                ps.close();
            }
            if (!leaderCheck) {
                //System.err.println("Leader " + this.leader + " isn't in guild " + this.id + ".  Impossible... guild is disbanding.");
                this.writeToDB(true);
                this.proper = false;
                return;
            }
            if (gFix > 0) {
                ps = con.prepareStatement("UPDATE characters SET guildrank = ? WHERE id = ?");
                ps.setByte(1, (byte) gFix);
                ps.setInt(2, this.leader);
                ps.executeUpdate();
                ps.close();
            }
            if (aFix > 0) {
                ps = con.prepareStatement("UPDATE characters SET alliancerank = ? WHERE id = ?");
                ps.setByte(1, (byte) aFix);
                ps.setInt(2, this.leader);
                ps.executeUpdate();
                ps.close();
            }
            ps = con.prepareStatement("SELECT * FROM guildskills WHERE guildid = ?");
            ps.setInt(1, guildid);
            rs = ps.executeQuery();
            while (rs.next()) {
                int sid = rs.getInt("skillid");
                if (sid < 91000000) {
                    rs.close();
                    ps.close();
                    //System.err.println("Skill " + sid + " is in guild " + this.id + ".  Impossible... guild is disbanding.");
                    this.writeToDB(true);
                    this.proper = false;
                    return;
                }
                this.guildSkills.put(sid, new MapleGuildSkill(sid, rs.getInt("level"), rs.getLong("timestamp"), rs.getString("purchaser"), ""));
            }
            rs.close();
            ps.close();
            this.level = this.calculateLevel();
        } catch (SQLException se) {
            //System.err.println("unable to read guild information from sql");
            se.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public boolean isProper() {
        /*  290 */ return this.proper;
    }

    public final void setGuildLevel(int level) {
        /*  294 */ this.guildlevel = level;
        /*  295 */ Connection con = null;
        /*  296 */ PreparedStatement ps = null;
        /*  297 */ ResultSet rs = null;

        /*  299 */ try {
            con = DatabaseConnection.getConnection();
            /*  300 */ ps = con.prepareStatement("UPDATE guilds SET level = ? WHERE guildid = ?");
            /*  301 */ ps.setInt(1, this.guildlevel);
            /*  302 */ ps.setInt(2, this.id);
            /*  303 */ ps.execute();
            /*  304 */ ps.close();
            /*  305 */ con.close();
        } /*  306 */ catch (SQLException sQLException) {
            try {
                /*  309 */ if (ps != null) {
                    /*  310 */ ps.close();
                }
                /*  312 */ if (rs != null) {
                    /*  313 */ rs.close();
                }
                /*  315 */ if (con != null) {
                    /*  316 */ con.close();
                }
                /*  318 */            } catch (Exception exception) {
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

    }

    public final void setBeforeAttance(int attance) {
        /*  325 */ this.beforeattance = attance;
        /*  326 */ Connection con = null;
        /*  327 */ PreparedStatement ps = null;
        /*  328 */ ResultSet rs = null;

        /*  330 */ try {
            con = DatabaseConnection.getConnection();
            /*  331 */ ps = con.prepareStatement("UPDATE guilds SET beforeattance = ? WHERE guildid = ?");
            /*  332 */ ps.setInt(1, this.beforeattance);
            /*  333 */ ps.setInt(2, this.id);
            /*  334 */ ps.execute();
            /*  335 */ ps.close();
            /*  336 */ con.close();
        } /*  337 */ catch (SQLException sQLException) {
            try {
                /*  340 */ if (ps != null) {
                    /*  341 */ ps.close();
                }
                /*  343 */ if (rs != null) {
                    /*  344 */ rs.close();
                }
                /*  346 */ if (con != null) {
                    /*  347 */ con.close();
                }
                /*  349 */            } catch (Exception exception) {
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

    }

    public final void setAfterAttance(int attance) {
        /*  356 */ this.afterattance = attance;
        /*  357 */ Connection con = null;
        /*  358 */ PreparedStatement ps = null;
        /*  359 */ ResultSet rs = null;

        /*  362 */ try {
            con = DatabaseConnection.getConnection();
            /*  363 */ ps = con.prepareStatement("UPDATE guilds SET afterattance = ? WHERE guildid = ?");
            /*  364 */ ps.setInt(1, this.afterattance);
            /*  365 */ ps.setInt(2, this.id);
            /*  366 */ ps.execute();
            /*  367 */ ps.close();
            /*  368 */ con.close();
        } /*  369 */ catch (SQLException sQLException) {
            try {
                /*  372 */ if (ps != null) {
                    /*  373 */ ps.close();
                }
                /*  375 */ if (rs != null) {
                    /*  376 */ rs.close();
                }
                /*  378 */ if (con != null) {
                    /*  379 */ con.close();
                }
                /*  381 */            } catch (Exception exception) {
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

    }

    public final void setGuildFame(int fame) {
        /*  388 */ this.fame = fame;
        /*  389 */ Connection con = null;
        /*  390 */ PreparedStatement ps = null;
        /*  391 */ ResultSet rs = null;

        /*  394 */ try {
            con = DatabaseConnection.getConnection();
            /*  395 */ ps = con.prepareStatement("UPDATE guilds SET fame = ? WHERE guildid = ?");
            /*  396 */ ps.setInt(1, this.fame);
            /*  397 */ ps.setInt(2, this.id);
            /*  398 */ ps.execute();
            /*  399 */ ps.close();
            /*  400 */ con.close();
        } /*  401 */ catch (SQLException sQLException) {
            try {
                /*  404 */ if (ps != null) {
                    /*  405 */ ps.close();
                }
                /*  407 */ if (rs != null) {
                    /*  408 */ rs.close();
                }
                /*  410 */ if (con != null) {
                    /*  411 */ con.close();
                }
                /*  413 */            } catch (Exception exception) {
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

    }

    public final void setGuildGP(int gp) {
        /*  420 */ this.gp = gp;
        /*  421 */ Connection con = null;
        /*  422 */ PreparedStatement ps = null;
        /*  423 */ ResultSet rs = null;

        /*  425 */ try {
            con = DatabaseConnection.getConnection();
            /*  426 */ ps = con.prepareStatement("UPDATE guilds SET GP = ? WHERE guildid = ?");
            /*  427 */ ps.setInt(1, gp);
            /*  428 */ ps.setInt(2, this.id);
            /*  429 */ ps.execute();
            /*  430 */ ps.close();
            /*  431 */ con.close();
        } /*  432 */ catch (SQLException sQLException) {
            try {
                /*  435 */ if (ps != null) {
                    /*  436 */ ps.close();
                }
                /*  438 */ if (rs != null) {
                    /*  439 */ rs.close();
                }
                /*  441 */ if (con != null) {
                    /*  442 */ con.close();
                }
                /*  444 */            } catch (Exception exception) {
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

        /*  448 */ broadcast(CWvsContext.GuildPacket.guildUpdateOnlyGP(this.id, this.gp));
    }

    public final void writeToDB(boolean bDisband) {
        /*  452 */ Connection con = null;
        /*  453 */ PreparedStatement ps = null;
        /*  454 */ ResultSet rs = null;
        try {
            /*  456 */ if (!bDisband) {
                /*  457 */ StringBuilder buf = new StringBuilder("UPDATE guilds SET GP = ?, logo = ?, logoColor = ?, logoBG = ?, logoBGColor = ?, ");
                /*  458 */ for (int i = 1; i < 6; i++) {
                    /*  459 */ buf.append("rank").append(i).append("title = ?, ");
                    /*  460 */ buf.append("rank").append(i).append("role = ?, ");
                }
                /*  462 */ buf.append("capacity = ?, notice = ?, alliance = ?, leader = ?, customEmblem = ?, noblesspoint = ?, score = ?, weekReputation = ? WHERE guildid = ?");
                /*  463 */ con = DatabaseConnection.getConnection();
                /*  464 */ ps = con.prepareStatement(buf.toString());
                /*  465 */ ps.setInt(1, this.gp);
                /*  466 */ ps.setInt(2, this.logo);
                /*  467 */ ps.setInt(3, this.logoColor);
                /*  468 */ ps.setInt(4, this.logoBG);
                /*  469 */ ps.setInt(5, this.logoBGColor);
                /*  470 */ ps.setString(6, this.rankTitles[0]);
                /*  471 */ ps.setInt(7, this.rankRoles[0]);
                /*  472 */ ps.setString(8, this.rankTitles[1]);
                /*  473 */ ps.setInt(9, this.rankRoles[1]);
                /*  474 */ ps.setString(10, this.rankTitles[2]);
                /*  475 */ ps.setInt(11, this.rankRoles[2]);
                /*  476 */ ps.setString(12, this.rankTitles[3]);
                /*  477 */ ps.setInt(13, this.rankRoles[3]);
                /*  478 */ ps.setString(14, this.rankTitles[4]);
                /*  479 */ ps.setInt(15, this.rankRoles[4]);
                /*  480 */ ps.setInt(16, this.capacity);
                /*  481 */ ps.setString(17, this.notice);
                /*  482 */ ps.setInt(18, this.allianceid);
                /*  483 */ ps.setInt(19, this.leader);
                /*  484 */ Blob blob = null;
                /*  485 */ if (this.customEmblem != null) {
                    /*  486 */ blob = new SerialBlob(this.customEmblem);
                }
                /*  488 */ ps.setBlob(20, blob);
                /*  489 */ ps.setInt(21, this.noblessskillpoint);
                /*  490 */ ps.setInt(22, (int) this.guildScore);
                /*  491 */ ps.setInt(23, this.weekReputation);
                /*  492 */ ps.setInt(24, this.id);
                /*  493 */ ps.executeUpdate();
                /*  494 */ ps.close();

                /*  496 */ if (this.changed_skills) {
                    /*  497 */ ps = con.prepareStatement("DELETE FROM guildskills WHERE guildid = ?");
                    /*  498 */ ps.setInt(1, this.id);
                    /*  499 */ ps.execute();
                    /*  500 */ ps.close();

                    /*  502 */ ps = con.prepareStatement("INSERT INTO guildskills(`guildid`, `skillid`, `level`, `timestamp`, `purchaser`) VALUES(?, ?, ?, ?, ?)");
                    /*  503 */ ps.setInt(1, this.id);
                    /*  504 */ for (MapleGuildSkill mapleGuildSkill : this.guildSkills.values()) {
                        /*  505 */ ps.setInt(2, mapleGuildSkill.skillID);
                        /*  506 */ ps.setByte(3, (byte) mapleGuildSkill.level);
                        /*  507 */ ps.setLong(4, mapleGuildSkill.timestamp);
                        /*  508 */ ps.setString(5, mapleGuildSkill.purchaser);
                        /*  509 */ ps.execute();
                    }
                    /*  511 */ ps.close();
                }
                /*  513 */ this.changed_skills = false;

                /*  515 */ if (this.changed_requests) {
                    /*  516 */ ps = con.prepareStatement("DELETE FROM guildsrequest WHERE gid = ?");
                    /*  517 */ ps.setInt(1, this.id);
                    /*  518 */ ps.execute();
                    /*  519 */ ps.close();

                    /*  521 */ ps = con.prepareStatement("INSERT INTO guildsrequest(`gid`, `cid`) VALUES(?, ?)");
                    /*  522 */ for (MapleGuildCharacter mgc : this.requests) {
                        /*  523 */ ps.setInt(1, mgc.getGuildId());
                        /*  524 */ ps.setInt(2, mgc.getId());
                        /*  525 */ ps.execute();
                    }
                    /*  527 */ ps.close();
                }
                /*  529 */ this.changed_requests = false;
            } else {
                /*  531 */ con = DatabaseConnection.getConnection();
                try {
                    /*  533 */ ps = con.prepareStatement("DELETE FROM guildskills WHERE guildid = ?");
                    /*  534 */ ps.setInt(1, this.id);
                    /*  535 */ ps.executeUpdate();
                    /*  536 */ ps.close();
                    /*  537 */                } catch (Exception ex) {
                    /*  538 */ ex.printStackTrace();
                }
                /*  540 */ ps = con.prepareStatement("DELETE FROM guilds WHERE guildid = ?");
                /*  541 */ ps.setInt(1, this.id);
                /*  542 */ ps.executeUpdate();
                /*  543 */ ps.close();
                /*  544 */ if (this.allianceid > 0) {
                    /*  545 */ MapleGuildAlliance alliance = World.Alliance.getAlliance(this.allianceid);
                    /*  546 */ if (alliance != null) {
                        /*  547 */ alliance.removeGuild(this.id, false);
                    }
                }

                /*  551 */ broadcast(CWvsContext.GuildPacket.guildDisband(this.id));
                /*  552 */ broadcast(CWvsContext.GuildPacket.guildDisband2());
            }
            /*  554 */        } catch (SQLException se) {
            /*  555 */ //System.err.println("Error saving guild to SQL");
            /*  556 */ se.printStackTrace();
        } finally {
            try {
                /*  559 */ if (con != null) {
                    /*  560 */ con.close();
                }
                /*  562 */ if (ps != null) {
                    /*  563 */ ps.close();
                }
                /*  565 */ if (rs != null) {
                    /*  566 */ rs.close();
                }
                /*  568 */            } catch (SQLException se) {
                /*  569 */ se.printStackTrace();
            }
        }
    }

    public final int getId() {
        /*  575 */ return this.id;
    }

    public final int getLeaderId() {
        /*  579 */ return this.leader;
    }

    public final MapleCharacter getLeader(MapleClient c) {
        /*  583 */ return c.getChannelServer().getPlayerStorage().getCharacterById(this.leader);
    }

    public final int getGP() {
        /*  587 */ return this.gp;
    }

    public final int getLogo() {
        /*  591 */ return this.logo;
    }

    public final void setLogo(int l) {
        /*  595 */ this.logo = l;
    }

    public final int getLogoColor() {
        /*  599 */ return this.logoColor;
    }

    public final void setLogoColor(int c) {
        /*  603 */ this.logoColor = c;
    }

    public final int getLogoBG() {
        /*  607 */ return this.logoBG;
    }

    public final void setLogoBG(int bg) {
        /*  611 */ this.logoBG = bg;
    }

    public final int getLogoBGColor() {
        /*  615 */ return this.logoBGColor;
    }

    public final void setLogoBGColor(int c) {
        /*  619 */ this.logoBGColor = c;
    }

    public final String getNotice() {
        /*  623 */ if (this.notice == null) {
            /*  624 */ return "";
        }
        /*  626 */ return this.notice;
    }

    public final String getName() {
        /*  630 */ return this.name;
    }

    public final int getCapacity() {
        /*  634 */ return this.capacity;
    }

    public final int getSignature() {
        /*  638 */ return this.signature;
    }

    public final void RankBroadCast(byte[] packet, int rank) {
        /*  643 */ for (MapleGuildCharacter mgc : this.members) {
            /*  644 */ if (mgc.isOnline() && mgc.getGuildRank() == rank) {
                /*  645 */ int ch = World.Find.findChannel(mgc.getId());
                /*  646 */ if (ch < 0) {
                    return;
                }
                /*  649 */ MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(mgc.getId());
                /*  650 */ if (c != null && c.getGuildId() == mgc.getGuildId()) {
                    /*  651 */ c.getClient().getSession().writeAndFlush(packet);
                }
            }
        }
    }

    public final void broadcast(byte[] packet) {
        /*  658 */ broadcast(packet, -1, BCOp.NONE);
    }

    public final void broadcast(byte[] packet, int exception) {
        /*  662 */ broadcast(packet, exception, BCOp.NONE);
    }

    public final void broadcast(byte[] packet, int exceptionId, BCOp bcop) {
        /*  667 */ broadcast(packet, exceptionId, bcop, null);
    }

    public final void broadcast(byte[] packet, int exceptionId, BCOp bcop, MapleCharacter chr) {
        /*  671 */ buildNotifications();

        /*  673 */ for (MapleGuildCharacter mgc : this.members) {
            /*  674 */ if (bcop == BCOp.DISBAND) {
                /*  675 */ if (mgc.isOnline()) {
                    /*  676 */ World.Guild.setGuildAndRank(mgc.getId(), 0, 5, 0, 5);
                    continue;
                }
                /*  678 */ setOfflineGuildStatus(0, (byte) 5, 0, (byte) 5, mgc.getId());
                continue;
            }
            /*  680 */ if (mgc.isOnline() && mgc.getId() != exceptionId) {
                /*  681 */ if (bcop == BCOp.EMBELMCHANGE) {
                    /*  682 */ World.Guild.changeEmblem(chr, mgc.getId(), this);
                    continue;
                }
                /*  684 */ World.Broadcast.sendGuildPacket(mgc.getId(), packet, exceptionId, this.id);
            }
        }
    }

    private final void buildNotifications() {
        /*  691 */ if (!this.bDirty) {
            return;
        }
        /*  694 */ List<Integer> mem = new LinkedList<>();
        /*  695 */ Iterator<MapleGuildCharacter> toRemove = this.members.iterator();
        /*  696 */ while (toRemove.hasNext()) {
            /*  697 */ MapleGuildCharacter mgc = toRemove.next();
            /*  698 */ if (!mgc.isOnline()) {
                continue;
            }
            /*  701 */ if (mem.contains(Integer.valueOf(mgc.getId())) || mgc.getGuildId() != this.id) {
                /*  702 */ this.members.remove(mgc);
                continue;
            }
            /*  705 */ mem.add(Integer.valueOf(mgc.getId()));
        }
        /*  707 */ this.bDirty = false;
    }

    public final void setOnline(int cid, boolean online, int channel) {
        /*  711 */ boolean bBroadcast = true;
        /*  712 */ for (MapleGuildCharacter mgc : this.members) {
            /*  713 */ if (mgc.getGuildId() == this.id && mgc.getId() == cid) {
                /*  714 */ if (mgc.isOnline() == online) {
                    /*  715 */ bBroadcast = false;
                }
                /*  717 */ mgc.setOnline(online);
                /*  718 */ mgc.setChannel((byte) channel);
                break;
            }
        }
        /*  722 */ if (bBroadcast) {
            /*  723 */ broadcast(CWvsContext.GuildPacket.guildMemberOnline(this.id, cid, online), cid);
            /*  724 */ if (this.allianceid > 0) {
                /*  725 */ World.Alliance.sendGuild(CWvsContext.AlliancePacket.allianceMemberOnline(this.allianceid, this.id, cid, online), this.id, this.allianceid);
            }
        }
        /*  728 */ this.bDirty = true;
        /*  729 */ this.init = true;
    }

    public final void guildChat(MapleCharacter player, String msg, LittleEndianAccessor slea, RecvPacketOpcode recv) {
        /*  733 */ Item item = null;
        /*  734 */ if (recv == RecvPacketOpcode.PARTYCHATITEM) {
            /*  735 */ byte invType = (byte) slea.readInt();
            /*  736 */ byte pos = (byte) slea.readInt();
            /*  737 */ item = player.getInventory(MapleInventoryType.getByType((pos > 0) ? invType : -1)).getItem((short) pos);
        }
        /*  739 */ DBLogger.getInstance().logChat(LogType.Chat.Guild, player.getId(), this.name, msg, "[" + getName() + "]");

        /*  741 */ broadcast(CField.multiChat(player, msg, 2, item), player.getId());
    }

    public final void allianceChat(MapleCharacter player, String msg, LittleEndianAccessor slea, RecvPacketOpcode recv) {
        /*  745 */ Item item = null;
        /*  746 */ if (recv == RecvPacketOpcode.PARTYCHATITEM) {
            /*  747 */ byte invType = (byte) slea.readInt();
            /*  748 */ byte pos = (byte) slea.readInt();
            /*  749 */ item = player.getInventory(MapleInventoryType.getByType((pos > 0) ? invType : -1)).getItem((short) pos);
        }
        /*  751 */ broadcast(CField.multiChat(player, msg, 3, item), player.getId());
    }

    public final String getRankTitle(int rank) {
        /*  755 */ return this.rankTitles[rank - 1];
    }

    public final int getRankRole(int role) {
        /*  759 */ return this.rankRoles[role - 1];
    }

    public final byte[] getCustomEmblem() {
        /*  763 */ return this.customEmblem;
    }

    public final void setCustomEmblem(byte[] emblem) {
        /*  767 */ this.customEmblem = emblem;
        /*  768 */ Connection con = null;
        /*  769 */ PreparedStatement ps = null;
        /*  770 */ ResultSet rs = null;
        try {
            /*  772 */ Blob blob = null;
            /*  773 */ if (emblem != null) {
                /*  774 */ blob = new SerialBlob(emblem);
            }
            /*  776 */ con = DatabaseConnection.getConnection();
            /*  777 */ ps = con.prepareStatement("UPDATE guilds SET customEmblem = ? WHERE guildid = ?");
            /*  778 */ ps.setBlob(1, blob);
            /*  779 */ ps.setInt(2, this.id);
            /*  780 */ ps.execute();
            /*  781 */ ps.close();
            /*  782 */ con.close();
            /*  783 */        } catch (SQLException e) {
            /*  784 */ e.printStackTrace();
        } finally {
            try {
                /*  787 */ if (con != null) {
                    /*  788 */ con.close();
                }
                /*  790 */ if (ps != null) {
                    /*  791 */ ps.close();
                }
                /*  793 */ if (rs != null) {
                    /*  794 */ rs.close();
                }
                /*  796 */            } catch (SQLException se) {
                /*  797 */ se.printStackTrace();
            }
        }
    }

    public int getAllianceId() {
        /*  804 */ return this.allianceid;
    }

    public int getInvitedId() {
        /*  808 */ return this.invitedid;
    }

    public void setInvitedId(int iid) {
        /*  812 */ this.invitedid = iid;
    }

    public void setAllianceId(int a) {
        /*  816 */ this.allianceid = a;
        /*  817 */ Connection con = null;
        /*  818 */ PreparedStatement ps = null;
        /*  819 */ ResultSet rs = null;
        try {
            /*  821 */ con = DatabaseConnection.getConnection();
            /*  822 */ ps = con.prepareStatement("UPDATE guilds SET alliance = ? WHERE guildid = ?");
            /*  823 */ ps.setInt(1, a);
            /*  824 */ ps.setInt(2, this.id);
            /*  825 */ ps.execute();
            /*  826 */ ps.close();
            /*  827 */ con.close();
            /*  828 */        } catch (SQLException e) {
            /*  829 */ //System.err.println("Saving allianceid ERROR" + e);
        } finally {
            try {
                /*  832 */ if (con != null) {
                    /*  833 */ con.close();
                }
                /*  835 */ if (ps != null) {
                    /*  836 */ ps.close();
                }
                /*  838 */ if (rs != null) {
                    /*  839 */ rs.close();
                }
                /*  841 */            } catch (SQLException se) {
                /*  842 */ se.printStackTrace();
            }
        }
    }

    public static final int createGuild(int leaderId, String name) {
        /*  849 */ if (name.length() > 12) {
            /*  850 */ return 0;
        }
        /*  852 */ Connection con = null;
        /*  853 */ PreparedStatement ps = null;
        /*  854 */ ResultSet rs = null;
        try {
            /*  856 */ con = DatabaseConnection.getConnection();
            /*  857 */ ps = con.prepareStatement("SELECT guildid FROM guilds WHERE name = ?");
            /*  858 */ ps.setString(1, name);
            /*  859 */ rs = ps.executeQuery();

            /*  861 */ if (rs.first()) {
                /*  862 */ rs.close();
                /*  863 */ ps.close();
                /*  864 */ con.close();
                /*  865 */ return 0;
            }
            /*  867 */ ps.close();
            /*  868 */ rs.close();

            /*  870 */ ps = con.prepareStatement("INSERT INTO guilds (`leader`, `name`, `signature`, `alliance`) VALUES (?, ?, ?, 0)", 1);
            /*  871 */ ps.setInt(1, leaderId);
            /*  872 */ ps.setString(2, name);
            /*  873 */ ps.setInt(3, (int) (System.currentTimeMillis() / 1000L));
            /*  874 */ ps.executeUpdate();
            /*  875 */ rs = ps.getGeneratedKeys();
            /*  876 */ int ret = 0;
            /*  877 */ if (rs.next()) {
                /*  878 */ ret = rs.getInt(1);
            }
            /*  880 */ rs.close();
            /*  881 */ ps.close();
            /*  882 */ con.close();
            /*  883 */ return ret;
            /*  884 */        } catch (SQLException se) {
            /*  885 */ //System.err.println("SQL THROW");
            /*  886 */ se.printStackTrace();
            /*  887 */ return 0;
        } finally {
            try {
                /*  890 */ if (con != null) {
                    /*  891 */ con.close();
                }
                /*  893 */ if (ps != null) {
                    /*  894 */ ps.close();
                }
                /*  896 */ if (rs != null) {
                    /*  897 */ rs.close();
                }
                /*  899 */            } catch (SQLException se) {
                /*  900 */ se.printStackTrace();
            }
        }
    }

    public final int addGuildMember(MapleGuildCharacter mgc) {
        /*  907 */ if (this.members.size() >= this.capacity) {
            /*  908 */ return 0;
        }
        /*  910 */ for (int i = this.members.size() - 1; i >= 0; i--) {
            /*  911 */ if (((MapleGuildCharacter) this.members.get(i)).getGuildRank() < 5 || ((MapleGuildCharacter) this.members.get(i)).getName().compareTo(mgc.getName()) < 0) {
                /*  912 */ this.members.add(i + 1, mgc);
                /*  913 */ this.bDirty = true;

                break;
            }
        }
        /*  918 */ broadcast(CWvsContext.GuildPacket.newGuildMember(mgc));
        /*  919 */ if (this.allianceid > 0) {
            /*  920 */ World.Alliance.sendGuild(this.allianceid);
        }
        /*  922 */ return 1;
    }

    public final int addGuildMember(MapleCharacter chr, MapleGuild guild) {
        /*  926 */ MapleGuildCharacter chrs = new MapleGuildCharacter(chr);
        /*  927 */ this.members.add(chrs);
        /*  928 */ broadcast(CWvsContext.GuildPacket.newGuildMember(chrs));
        /*  929 */ return 1;
    }

    public final void leaveGuild(MapleGuildCharacter mgc) {
        /*  933 */ Iterator<MapleGuildCharacter> itr = this.members.iterator();
        /*  934 */ while (itr.hasNext()) {
            /*  935 */ MapleGuildCharacter mgcc = itr.next();

            /*  937 */ if (mgcc.getId() == mgc.getId()) {
                /*  938 */ for (int i = 1; i <= 5; i++) {
                    /*  939 */ RankBroadCast(CWvsContext.GuildPacket.memberLeft(mgcc, (i != 5)), i);
                }

                /*  943 */ this.bDirty = true;
                /*  944 */ this.members.remove(mgcc);
                /*  945 */ if (mgc.isOnline()) {
                    /*  946 */ World.Guild.setGuildAndRank(mgcc.getId(), 0, 5, 0, 5);
                    break;
                }
                /*  948 */ setOfflineGuildStatus(0, (byte) 5, 0, (byte) 5, mgcc.getId());

                break;
            }
        }
        /*  953 */ if (this.bDirty && this.allianceid > 0) {
            /*  954 */ World.Alliance.sendGuild(this.allianceid);
        }
    }

    public final void expelMember(MapleGuildCharacter initiator, String name, int cid) {
        /*  959 */ Iterator<MapleGuildCharacter> itr = this.members.iterator();
        /*  960 */ while (itr.hasNext()) {
            /*  961 */ MapleGuildCharacter mgc = itr.next();

            /*  963 */ if (mgc.getId() == cid && initiator.getGuildRank() < mgc.getGuildRank()) {
                /*  964 */ broadcast(CWvsContext.GuildPacket.memberLeft(mgc, true));

                /*  966 */ this.bDirty = true;

                /*  968 */ if (mgc.isOnline()) {
                    /*  969 */ World.Guild.setGuildAndRank(cid, 0, 5, 0, 5);
                } else {
                    /*  971 */ MapleCharacterUtil.sendNote(mgc.getName(), initiator.getName(), "길드에서 강퇴당했습니다.", 0, 6, initiator.getId());
                    /*  972 */ setOfflineGuildStatus(0, (byte) 5, 0, (byte) 5, cid);
                }
                /*  974 */ this.members.remove(mgc);
                break;
            }
        }
        /*  978 */ if (this.bDirty && this.allianceid > 0) {
            /*  979 */ World.Alliance.sendGuild(this.allianceid);
        }
    }

    public final void changeARank() {
        /*  984 */ changeARank(false);
    }

    public final void changeARank(boolean leader) {
        /*  988 */ if (this.allianceid <= 0) {
            return;
        }
        /*  991 */ for (MapleGuildCharacter mgc : this.members) {
            /*  992 */ byte newRank = 3;
            /*  993 */ if (this.leader == mgc.getId()) {
                /*  994 */ newRank = (byte) (leader ? 1 : 2);
            }
            /*  996 */ if (mgc.isOnline()) {
                /*  997 */ World.Guild.setGuildAndRank(mgc.getId(), this.id, mgc.getGuildRank(), mgc.getGuildContribution(), newRank);
            } else {
                /*  999 */ setOfflineGuildStatus(this.id, mgc.getGuildRank(), mgc.getGuildContribution(), newRank, mgc.getId());
            }
            /* 1001 */ mgc.setAllianceRank(newRank);
        }
        /* 1003 */ World.Alliance.sendGuild(this.allianceid);
    }

    public final void changeARank(int newRank) {
        /* 1007 */ if (this.allianceid <= 0) {
            return;
        }
        /* 1010 */ for (MapleGuildCharacter mgc : this.members) {
            /* 1011 */ if (mgc.isOnline()) {
                /* 1012 */ World.Guild.setGuildAndRank(mgc.getId(), this.id, mgc.getGuildRank(), mgc.getGuildContribution(), newRank);
            } else {
                /* 1014 */ setOfflineGuildStatus(this.id, mgc.getGuildRank(), mgc.getGuildContribution(), (byte) newRank, mgc.getId());
            }
            /* 1016 */ mgc.setAllianceRank((byte) newRank);
        }
        /* 1018 */ World.Alliance.sendGuild(this.allianceid);
    }

    public final boolean changeARank(int cid, int newRank) {
        /* 1022 */ if (this.allianceid <= 0) {
            /* 1023 */ return false;
        }
        /* 1025 */ for (MapleGuildCharacter mgc : this.members) {
            /* 1026 */ if (cid == mgc.getId()) {
                /* 1027 */ if (mgc.isOnline()) {
                    /* 1028 */ World.Guild.setGuildAndRank(cid, this.id, mgc.getGuildRank(), mgc.getGuildContribution(), newRank);
                } else {
                    /* 1030 */ setOfflineGuildStatus(this.id, mgc.getGuildRank(), mgc.getGuildContribution(), (byte) newRank, cid);
                }
                /* 1032 */ mgc.setAllianceRank((byte) newRank);
                /* 1033 */ World.Alliance.sendGuild(this.allianceid);
                /* 1034 */ return true;
            }
        }
        /* 1037 */ return false;
    }

    public final void changeGuildLeader(int cid) {
        /* 1041 */ if (changeRank(cid, 1) && changeRank(this.leader, 2)) {
            /* 1042 */ if (this.allianceid > 0) {
                /* 1043 */ int aRank = getMGC(this.leader).getAllianceRank();
                /* 1044 */ if (aRank == 1) {
                    /* 1045 */ World.Alliance.changeAllianceLeader(this.allianceid, cid, true);
                } else {
                    /* 1047 */ changeARank(cid, aRank);
                }
                /* 1049 */ changeARank(this.leader, 3);
            }
            /* 1051 */ broadcast(CWvsContext.GuildPacket.guildLeaderChanged(this.id, this.leader, cid, this.allianceid));
            /* 1052 */ this.leader = cid;
            /* 1053 */ Connection con = null;
            /* 1054 */ PreparedStatement ps = null;
            /* 1055 */ ResultSet rs = null;
            try {
                /* 1057 */ con = DatabaseConnection.getConnection();
                /* 1058 */ ps = con.prepareStatement("UPDATE guilds SET leader = ? WHERE guildid = ?");
                /* 1059 */ ps.setInt(1, cid);
                /* 1060 */ ps.setInt(2, this.id);
                /* 1061 */ ps.execute();
                /* 1062 */ ps.close();
                /* 1063 */ con.close();
                /* 1064 */            } catch (SQLException e) {
                /* 1065 */ //System.err.println("Saving leaderid ERROR" + e);
            } finally {
                try {
                    /* 1068 */ if (con != null) {
                        /* 1069 */ con.close();
                    }
                    /* 1071 */ if (ps != null) {
                        /* 1072 */ ps.close();
                    }
                    /* 1074 */ if (rs != null) {
                        /* 1075 */ rs.close();
                    }
                    /* 1077 */                } catch (SQLException se) {
                    /* 1078 */ se.printStackTrace();
                }
            }
        }
    }

    public final boolean changeRank(int cid, int newRank) {
        /* 1085 */ for (MapleGuildCharacter mgc : this.members) {
            /* 1086 */ if (cid == mgc.getId()) {
                /* 1087 */ if (mgc.isOnline()) {
                    /* 1088 */ World.Guild.setGuildAndRank(cid, this.id, newRank, mgc.getGuildContribution(), mgc.getAllianceRank());
                } else {
                    /* 1090 */ setOfflineGuildStatus(this.id, (byte) newRank, mgc.getGuildContribution(), mgc.getAllianceRank(), cid);
                }
                /* 1092 */ mgc.setGuildRank((byte) newRank);
                /* 1093 */ broadcast(CWvsContext.GuildPacket.changeRank(mgc));
                /* 1094 */ return true;
            }
        }

        /* 1098 */ return false;
    }

    public final void setGuildNotice(MapleCharacter chr, String notice) {
        /* 1102 */ this.notice = notice;
        /* 1103 */ Connection con = null;
        /* 1104 */ PreparedStatement ps = null;
        /* 1105 */ ResultSet rs = null;
        try {
            /* 1107 */ con = DatabaseConnection.getConnection();
            /* 1108 */ ps = con.prepareStatement("UPDATE guilds SET notice = ? WHERE guildid = ?");
            /* 1109 */ ps.setString(1, notice);
            /* 1110 */ ps.setInt(2, this.id);
            /* 1111 */ ps.execute();
            /* 1112 */ ps.close();
            /* 1113 */ con.close();
            /* 1114 */        } catch (SQLException e) {
            /* 1115 */ //System.err.println("Saving guild notice ERROR");
            /* 1116 */ e.printStackTrace();
        } finally {
            try {
                /* 1119 */ if (con != null) {
                    /* 1120 */ con.close();
                }
                /* 1122 */ if (ps != null) {
                    /* 1123 */ ps.close();
                }
                /* 1125 */ if (rs != null) {
                    /* 1126 */ rs.close();
                }
                /* 1128 */            } catch (SQLException se) {
                /* 1129 */ se.printStackTrace();
            }
        }
        /* 1132 */ broadcast(CWvsContext.GuildPacket.guildNotice(chr, notice));
    }

    public final void memberLevelJobUpdate(MapleGuildCharacter mgc) {
        /* 1136 */ for (MapleGuildCharacter member : this.members) {
            /* 1137 */ if (member.getId() == mgc.getId()) {
                /* 1138 */ int old_level = member.getLevel();
                /* 1139 */ int old_job = member.getJobId();
                /* 1140 */ member.setJobId(mgc.getJobId());
                /* 1141 */ member.setLevel((short) mgc.getLevel());
                /* 1142 */ if (old_level != mgc.getLevel() && mgc.getLevel() >= 200) {
                    /* 1143 */ broadcast(CWvsContext.sendLevelup(false, mgc.getLevel(), mgc.getName()), mgc.getId());
                }
                /* 1145 */ if (old_job != mgc.getJobId()) {
                    /* 1146 */ broadcast(CWvsContext.sendJobup(false, mgc.getJobId(), mgc.getName()), mgc.getId());
                }
                /* 1148 */ broadcast(CWvsContext.GuildPacket.guildMemberLevelJobUpdate(mgc));
                /* 1149 */ if (this.allianceid > 0) {
                    /* 1150 */ World.Alliance.sendGuild(CWvsContext.AlliancePacket.updateAlliance(mgc, this.allianceid), this.id, this.allianceid);
                }
                break;
            }
        }
    }

    public void setRankTitle(String[] ranks) {
        /* 1158 */ for (int i = 0; i < 5; i++) {
            /* 1159 */ this.rankTitles[i] = ranks[i];
        }
    }

    public final void changeRankTitle(MapleCharacter chr, String[] ranks) {
        /* 1164 */ int[] roles = this.rankRoles;
        /* 1165 */ for (int i = 0; i < 5; i++) {
            /* 1166 */ this.rankTitles[i] = ranks[i];
        }
        /* 1168 */ updateRankRole();
        /* 1169 */ broadcast(CWvsContext.GuildPacket.rankTitleChange(125, chr, ranks, roles));
    }

    public final void changeRankRole(MapleCharacter chr, int[] roles) {
        /* 1173 */ String[] ranks = this.rankTitles;
        /* 1174 */ for (int i = 0; i < 5; i++) {
            /* 1175 */ this.rankRoles[i] = roles[i];
        }
        /* 1177 */ updateRankRole();
        /* 1178 */ broadcast(CWvsContext.GuildPacket.rankTitleChange(127, chr, ranks, roles));
    }

    public final void changeRankTitleRole(MapleCharacter chr, String ranks, int roles, byte type) {
        String[] ranks1 = rankTitles;
        int[] roles1 = rankRoles;

        rankRoles[type] = roles;
        rankTitles[type] = ranks;
        /* 1186 */ updateRankRole();
        /* 1187 */ broadcast(CWvsContext.GuildPacket.rankTitleChange(122, chr, ranks1, roles1));
    }

    public final void updateRankRole() {
        /* 1191 */ Connection con = null;
        /* 1192 */ PreparedStatement ps = null;
        /* 1193 */ ResultSet rs = null;
        try {
            /* 1195 */ con = DatabaseConnection.getConnection();
            /* 1196 */ StringBuilder buf = new StringBuilder("UPDATE guilds SET ");
            /* 1197 */ for (int i = 1; i < 6; i++) {
                /* 1198 */ buf.append("rank").append(i).append("title = ?, ");
                /* 1199 */ buf.append("rank").append(i).append("role = ?");
                /* 1200 */ if (i != 5) {
                    /* 1201 */ buf.append(", ");
                }
            }
            /* 1204 */ buf.append(" WHERE guildid = ?");
            /* 1205 */ ps = con.prepareStatement(buf.toString());
            /* 1206 */ ps.setString(1, this.rankTitles[0]);
            /* 1207 */ ps.setInt(2, this.rankRoles[0]);
            /* 1208 */ ps.setString(3, this.rankTitles[1]);
            /* 1209 */ ps.setInt(4, this.rankRoles[1]);
            /* 1210 */ ps.setString(5, this.rankTitles[2]);
            /* 1211 */ ps.setInt(6, this.rankRoles[2]);
            /* 1212 */ ps.setString(7, this.rankTitles[3]);
            /* 1213 */ ps.setInt(8, this.rankRoles[3]);
            /* 1214 */ ps.setString(9, this.rankTitles[4]);
            /* 1215 */ ps.setInt(10, this.rankRoles[4]);
            /* 1216 */ ps.setInt(11, this.id);
            /* 1217 */ ps.execute();
            /* 1218 */ ps.close();
            /* 1219 */ con.close();
            /* 1220 */        } catch (SQLException e) {
            /* 1221 */ //System.err.println("Saving guild rank / roles ERROR");
            /* 1222 */ e.printStackTrace();
        } finally {
            try {
                /* 1225 */ if (con != null) {
                    /* 1226 */ con.close();
                }
                /* 1228 */ if (ps != null) {
                    /* 1229 */ ps.close();
                }
                /* 1231 */ if (rs != null) {
                    /* 1232 */ rs.close();
                }
                /* 1234 */            } catch (SQLException se) {
                /* 1235 */ se.printStackTrace();
            }
        }
    }

    public final void disbandGuild() {
        /* 1241 */ writeToDB(true);
        /* 1242 */ broadcast(null, -1, BCOp.DISBAND);
    }

    public final void setGuildEmblem(MapleCharacter chr, short bg, byte bgcolor, short logo, byte logocolor) {
        /* 1246 */ this.logoBG = bg;
        /* 1247 */ this.logoBGColor = bgcolor;
        /* 1248 */ this.logo = logo;
        /* 1249 */ this.logoColor = logocolor;
        /* 1250 */ setCustomEmblem(null);
        /* 1251 */ broadcast(null, -1, BCOp.EMBELMCHANGE, chr);

        /* 1253 */ Connection con = null;
        /* 1254 */ PreparedStatement ps = null;
        /* 1255 */ ResultSet rs = null;

        try {
            /* 1258 */ con = DatabaseConnection.getConnection();
            /* 1259 */ ps = con.prepareStatement("UPDATE guilds SET logo = ?, logoColor = ?, logoBG = ?, logoBGColor = ? WHERE guildid = ?");
            /* 1260 */ ps.setInt(1, logo);
            /* 1261 */ ps.setInt(2, this.logoColor);
            /* 1262 */ ps.setInt(3, this.logoBG);
            /* 1263 */ ps.setInt(4, this.logoBGColor);
            /* 1264 */ ps.setInt(5, this.id);
            /* 1265 */ ps.execute();
            /* 1266 */ ps.close();
            /* 1267 */ con.close();
            /* 1268 */        } catch (SQLException e) {
            /* 1269 */ //System.err.println("Saving guild logo / BG colo ERROR");
            /* 1270 */ e.printStackTrace();
        } finally {
            try {
                /* 1273 */ if (con != null) {
                    /* 1274 */ con.close();
                }
                /* 1276 */ if (ps != null) {
                    /* 1277 */ ps.close();
                }
                /* 1279 */ if (rs != null) {
                    /* 1280 */ rs.close();
                }
                /* 1282 */            } catch (SQLException se) {
                /* 1283 */ se.printStackTrace();
            }
        }
    }

    public final void setGuildCustomEmblem(MapleCharacter chr, byte[] imgdata) {
        /* 1289 */ this.logoBG = 0;
        /* 1290 */ this.logoBGColor = 0;
        /* 1291 */ this.logo = 0;
        /* 1292 */ this.logoColor = 0;
        /* 1293 */ setCustomEmblem(imgdata);
        /* 1294 */ broadcast(CWvsContext.GuildPacket.changeCustomGuildEmblem(chr, imgdata));
        /* 1295 */ Connection con = null;
        /* 1296 */ PreparedStatement ps = null;
        /* 1297 */ ResultSet rs = null;

        try {
            /* 1300 */ con = DatabaseConnection.getConnection();
            /* 1301 */ ps = con.prepareStatement("UPDATE guilds SET logo = ?, logoColor = ?, logoBG = ?, logoBGColor = ? WHERE guildid = ?");
            /* 1302 */ ps.setInt(1, this.logo);
            /* 1303 */ ps.setInt(2, this.logoColor);
            /* 1304 */ ps.setInt(3, this.logoBG);
            /* 1305 */ ps.setInt(4, this.logoBGColor);
            /* 1306 */ ps.setInt(5, this.id);
            /* 1307 */ ps.execute();
            /* 1308 */ ps.close();
            /* 1309 */ con.close();
            /* 1310 */        } catch (SQLException e) {
            /* 1311 */ //System.err.println("Saving guild logo / BG colo ERROR");
            /* 1312 */ e.printStackTrace();
        } finally {
            try {
                /* 1315 */ if (con != null) {
                    /* 1316 */ con.close();
                }
                /* 1318 */ if (ps != null) {
                    /* 1319 */ ps.close();
                }
                /* 1321 */ if (rs != null) {
                    /* 1322 */ rs.close();
                }
                /* 1324 */            } catch (SQLException se) {
                /* 1325 */ se.printStackTrace();
            }
        }
    }

    public final MapleGuildCharacter getMGC(int cid) {
        /* 1331 */ for (MapleGuildCharacter mgc : this.members) {
            /* 1332 */ if (mgc.getId() == cid) {
                /* 1333 */ return mgc;
            }
        }
        /* 1336 */ return null;
    }

    public final boolean increaseCapacity(boolean trueMax) {
        /* 1340 */ if (this.capacity >= (trueMax ? 200 : 100) || this.capacity + 10 > (trueMax ? 200 : 100)) {
            /* 1341 */ return false;
        }
        /* 1343 */ this.capacity += 10;
        /* 1344 */ broadcast(CWvsContext.GuildPacket.guildCapacityChange(this.id, this.capacity));

        /* 1346 */ Connection con = null;
        /* 1347 */ PreparedStatement ps = null;
        /* 1348 */ ResultSet rs = null;
        try {
            /* 1350 */ con = DatabaseConnection.getConnection();
            /* 1351 */ ps = con.prepareStatement("UPDATE guilds SET capacity = ? WHERE guildid = ?");
            /* 1352 */ ps.setInt(1, this.capacity);
            /* 1353 */ ps.setInt(2, this.id);
            /* 1354 */ ps.execute();
            /* 1355 */ ps.close();
            /* 1356 */ con.close();
            /* 1357 */        } catch (SQLException e) {
            /* 1358 */ //System.err.println("Saving guild capacity ERROR");
            /* 1359 */ e.printStackTrace();
        } finally {
            try {
                /* 1362 */ if (con != null) {
                    /* 1363 */ con.close();
                }
                /* 1365 */ if (ps != null) {
                    /* 1366 */ ps.close();
                }
                /* 1368 */ if (rs != null) {
                    /* 1369 */ rs.close();
                }
                /* 1371 */            } catch (SQLException se) {
                /* 1372 */ se.printStackTrace();
            }
        }
        /* 1375 */ return true;
    }

    public final void gainGP(int amount) {
        /* 1379 */ gainGP(amount, true, -1);
    }

    public final void gainGP(int amount, boolean broadcast) {
        /* 1383 */ gainGP(amount, broadcast, -1);
    }

    public final void gainGP(int amount, boolean broadcast, int cid) {
        /* 1387 */ MapleGuildCharacter mg = getMGC(cid);
        /* 1388 */ MapleCharacter chr = null;
        /* 1389 */ for (ChannelServer cs : ChannelServer.getAllInstances()) {
            /* 1390 */ for (MapleCharacter chr2 : cs.getPlayerStorage().getAllCharacters().values()) {
                /* 1391 */ if (chr2.getId() == cid) {
                    /* 1392 */ chr = chr2;
                }
            }
        }

        /* 1397 */ if (amount == 0 || chr == null) {
            return;
        }
        /* 1400 */ if (chr.getKeyValue(210302, "GP") < 10000L) {
            /* 1401 */ chr.setKeyValue(210302, "GP", "" + (chr.getKeyValue(210302, "GP") + amount));
        } else {
            return;
        }
        /* 1405 */ mg.setGuildContribution(mg.getGuildContribution() + amount);
        /* 1406 */ chr.setGuildContribution(mg.getGuildContribution() + amount);
        /* 1407 */ chr.saveGuildStatus();
        /* 1408 */ setGuildFame(getFame() + amount);
        /* 1409 */ int gp = 0;
        /* 1410 */ if (amount < 100) {
            /* 1411 */ float gp2 = amount;
            /* 1412 */ gp = (int) (gp2 / 100.0F * 30.0F);
        } else {
            /* 1414 */ gp = amount / 100 * 30;
        }
        /* 1416 */ setGuildGP(getGP() + gp);
        /* 1417 */ setWeekReputation(getWeekReputation() + gp);
        /* 1418 */ if (getFame() >= GameConstants.getGuildExpNeededForLevel(getLevel())) {
            /* 1419 */ setGuildLevel(getLevel() + 1);
            /* 1420 */ broadcast(CWvsContext.GuildPacket.showGuildInfo(chr));
            /* 1421 */ broadcast(CWvsContext.serverNotice(5, "", "<길드> 길드의 레벨이 상승 하였습니다."));
        }
        /* 1423 */ broadcast(CWvsContext.GuildPacket.updateGP(this.id, getFame(), getGP(), getLevel()));
        /* 1424 */ broadcast(CWvsContext.GuildPacket.GainGP(this, chr, mg.getGuildContribution()));
        /* 1425 */ if (broadcast) {
            /* 1426 */ broadcast(CWvsContext.InfoPacket.getGPMsg(amount));
            /* 1427 */ chr.getClient().send(CWvsContext.InfoPacket.getGPContribution(amount));
        }
    }

    public Collection<MapleGuildSkill> getSkills() {
        /* 1432 */ return this.guildSkills.values();
    }

    public Map<Integer, MapleGuildSkill> getGuildSkills() {
        /* 1436 */ return this.guildSkills;
    }

    public int getSkillLevel(int sid) {
        /* 1440 */ if (!this.guildSkills.containsKey(Integer.valueOf(sid))) {
            /* 1441 */ return 0;
        }
        /* 1443 */ return ((MapleGuildSkill) this.guildSkills.get(Integer.valueOf(sid))).level;
    }

    public boolean activateSkill(int skill, String name) {
        /* 1447 */ if (!this.guildSkills.containsKey(Integer.valueOf(skill))) {
            /* 1448 */ return false;
        }
        /* 1450 */ MapleGuildSkill ourSkill = this.guildSkills.get(Integer.valueOf(skill));
        /* 1451 */ SecondaryStatEffect skillid = SkillFactory.getSkill(skill).getEffect(ourSkill.level);
        /* 1452 */ if (ourSkill.timestamp > System.currentTimeMillis() || skillid.getPeriod() <= 0) {
            /* 1453 */ return false;
        }
        /* 1455 */ ourSkill.timestamp = System.currentTimeMillis() + skillid.getPeriod() * 60000L;
        /* 1456 */ ourSkill.activator = name;
        /* 1457 */ writeToDB(false);
        /* 1458 */ broadcast(CWvsContext.GuildPacket.guildSkillPurchased(this.id, skill, ourSkill.level, ourSkill.timestamp, ourSkill.purchaser, name));
        /* 1459 */ return true;
    }

    public boolean purchaseSkill(int skill, String name, int cid) {
        /* 1463 */ SecondaryStatEffect skillid = SkillFactory.getSkill(skill).getEffect(getSkillLevel(skill) + 1);
        /* 1464 */ if (skillid.getReqGuildLevel() > getLevel() || skillid.getLevel() <= getSkillLevel(skill)) {
            /* 1465 */ return false;
        }
        /* 1467 */ MapleGuildSkill ourSkill = this.guildSkills.get(Integer.valueOf(skill));
        /* 1468 */ if (ourSkill == null) {
            /* 1469 */ ourSkill = new MapleGuildSkill(skill, skillid.getLevel(), 0L, name, name);
            /* 1470 */ this.guildSkills.put(Integer.valueOf(skill), ourSkill);
        } else {
            /* 1472 */ ourSkill.level = skillid.getLevel();
            /* 1473 */ ourSkill.purchaser = name;
            /* 1474 */ ourSkill.activator = name;
        }
        /* 1476 */ if (skillid.getPeriod() <= 0) {
            /* 1477 */ ourSkill.timestamp = -1L;
        } else {
            /* 1479 */ ourSkill.timestamp = System.currentTimeMillis() + skillid.getPeriod() * 60000L;
        }
        /* 1481 */ this.changed_skills = true;
        /* 1482 */ writeToDB(false);
        /* 1483 */ broadcast(CWvsContext.GuildPacket.guildSkillPurchased(this.id, skill, ourSkill.level, ourSkill.timestamp, name, name));
        /* 1484 */ return true;
    }

    public boolean removeSkill(int skill, String name) {
        /* 1488 */ if (this.guildSkills.containsKey(Integer.valueOf(skill))) {
            /* 1489 */ this.guildSkills.remove(Integer.valueOf(skill));
        }
        /* 1491 */ this.changed_skills = true;
        /* 1492 */ writeToDB(false);
        /* 1493 */ broadcast(CWvsContext.GuildPacket.guildSkillPurchased(this.id, skill, 0, -1L, name, name));
        /* 1494 */ return true;
    }

    public int getLevel() {
        /* 1498 */ return this.guildlevel;
    }

    public final int calculateLevel() {
        /* 1502 */ for (int i = 1; i < 30; i++) {
            /* 1503 */ if (this.gp < GameConstants.getGuildExpNeededForLevel(i)) {
                /* 1504 */ return i;
            }
        }
        /* 1507 */ return 30;
    }

    public final int calculateGuildPoints() {
        /* 1511 */ int rgp = this.gp;
        /* 1512 */ for (int i = 1; i < 30; i++) {
            /* 1513 */ if (rgp < GameConstants.getGuildExpNeededForLevel(i)) {
                /* 1514 */ return rgp;
            }
            /* 1516 */ rgp -= GameConstants.getGuildExpNeededForLevel(i);
        }
        /* 1518 */ return rgp;
    }

    public final void addMemberData(MaplePacketLittleEndianWriter mplew) {
        mplew.writeShort(this.members.size());

        for (MapleGuildCharacter mgc : this.members) {
            mplew.writeInt(mgc.getId());
        }
        for (MapleGuildCharacter mgc : this.members) {
            mplew.writeInt(mgc.getId());
            mplew.writeAsciiString(mgc.getName(), 13);
            mplew.writeInt(mgc.getJobId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getGuildRank());
            mplew.writeInt(mgc.isOnline() ? 1 : 0);
            mplew.writeLong(PacketHelper.getTime(mgc.getLastDisconnectTime()));
            mplew.writeInt(mgc.getAllianceRank());
            mplew.writeInt(mgc.getGuildContribution());
            mplew.writeInt(mgc.getGuildContribution());
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

            mplew.writeInt(mgc.getLastAttendance(mgc.getId()));
            mplew.writeLong(PacketHelper.getTime(-2L));
            mplew.writeLong(PacketHelper.getTime(-2L));
        }
    }

    public final void addRequestMemberData(MaplePacketLittleEndianWriter mplew) {
        /* 1548 */ mplew.writeShort(this.requests.size());

        /* 1550 */ for (MapleGuildCharacter mgc : this.requests) {
            /* 1551 */ mplew.writeInt(mgc.getId());
        }
        /* 1553 */ for (MapleGuildCharacter mgc : this.requests) {
            mplew.writeInt(mgc.getId());
            /* 1554 */ mplew.writeAsciiString(mgc.getName(), 13);
            /* 1555 */ mplew.writeInt(mgc.getJobId());
            /* 1556 */ mplew.writeInt(mgc.getLevel());
            /* 1557 */ mplew.writeInt(mgc.getGuildRank());
            /* 1558 */ mplew.writeInt(mgc.isOnline() ? 1 : 0);
            /* 1559 */ mplew.writeLong(PacketHelper.getTime(mgc.getLastDisconnectTime()));
            /* 1560 */ mplew.writeInt(mgc.getAllianceRank());
            /* 1561 */ mplew.writeInt(mgc.getGuildContribution());
            /* 1562 */ mplew.writeInt(0);
            /* 1563 */ mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

            /* 1566 */ mplew.writeInt(0);
            /* 1567 */ mplew.writeLong(PacketHelper.getTime(-2L));
            /* 1568 */ mplew.writeLong(PacketHelper.getTime(-2L));
        }
        
        mplew.writeInt(requests.size());
        for (final MapleGuildCharacter mgc : requests) {
            mplew.writeShort(0);
        }
    }

    public static final MapleGuildResponse sendInvite(MapleClient c, String targetName) {
        /* 1573 */ MapleCharacter mc = c.getChannelServer().getPlayerStorage().getCharacterByName(targetName);
        /* 1574 */ if (mc == null) {
            /* 1575 */ return MapleGuildResponse.NOT_IN_CHANNEL;
        }
        /* 1577 */ if (mc.getGuildId() > 0) {
            /* 1578 */ return MapleGuildResponse.ALREADY_IN_GUILD;
        }
   //     GuildHandler.DenyGuildRequest(targetName, c);
        /* 1580 */ mc.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.guildInvite(c.getPlayer().getGuildId(), c.getPlayer().getGuild().getName(), c.getPlayer()));
        /* 1581 */ return null;
    }

    public Collection<MapleGuildCharacter> getMembers() {
        /* 1585 */ return Collections.unmodifiableCollection(this.members);
    }

    public final boolean isInit() {
        /* 1589 */ return this.init;
    }

    public boolean hasSkill(int id) {
        /* 1593 */ return this.guildSkills.containsKey(Integer.valueOf(id));
    }

    public static void setOfflineGuildStatus(int guildid, byte guildrank, int contribution, byte alliancerank, int cid) {
        /* 1597 */ Connection con = null;
        /* 1598 */ PreparedStatement ps = null;
        /* 1599 */ ResultSet rs = null;
        try {
            /* 1601 */ con = DatabaseConnection.getConnection();
            /* 1602 */ ps = con.prepareStatement("UPDATE characters SET guildid = ?, guildrank = ?, guildContribution = ?, alliancerank = ? WHERE id = ?");
            /* 1603 */ ps.setInt(1, guildid);
            /* 1604 */ ps.setInt(2, guildrank);
            /* 1605 */ ps.setInt(3, contribution);
            /* 1606 */ ps.setInt(4, alliancerank);
            /* 1607 */ ps.setInt(5, cid);
            /* 1608 */ ps.executeUpdate();
            /* 1609 */ ps.close();
            /* 1610 */ con.close();
            /* 1611 */        } catch (SQLException se) {
            /* 1612 */ System.out.println("SQLException: " + se.getLocalizedMessage());
            /* 1613 */ se.printStackTrace();
        } finally {
            try {
                /* 1616 */ if (con != null) {
                    /* 1617 */ con.close();
                }
                /* 1619 */ if (ps != null) {
                    /* 1620 */ ps.close();
                }
                /* 1622 */ if (rs != null) {
                    /* 1623 */ rs.close();
                }
                /* 1625 */            } catch (SQLException se) {
                /* 1626 */ se.printStackTrace();
            }
        }
    }

    public int avergeMemberLevel() {
        /* 1632 */ int mean = 0, size = 0;
        /* 1633 */ for (MapleGuildCharacter gc : this.members) {
            /* 1634 */ mean += gc.getLevel();
            /* 1635 */ size++;
        }
        /* 1637 */ if (mean == 0 || size == 0) {
            /* 1638 */ return 0;
        }
        /* 1640 */ return mean / size;
    }

    public String getLeaderName() {
        /* 1644 */ for (MapleGuildCharacter gc : this.members) {
            /* 1645 */ if (gc.getId() == this.leader) {
                /* 1646 */ return gc.getName();
            }
        }
        /* 1649 */ return "없음";
    }

    public boolean addRequest(MapleGuildCharacter mgc) {
        /* 1653 */ this.changed_requests = true;
        /* 1654 */ Iterator<MapleGuildCharacter> toRemove = this.requests.iterator();
        /* 1655 */ while (toRemove.hasNext()) {
            /* 1656 */ MapleGuildCharacter mgc2 = toRemove.next();

            /* 1658 */ if (mgc2.getId() == mgc.getId()) {
                /* 1659 */ return false;
            }
        }
        /* 1662 */ this.requests.add(mgc);
        /* 1663 */ return true;
    }

    public void removeRequest(int cid) {
        /* 1667 */ Iterator<MapleGuildCharacter> toRemove = this.requests.iterator();
        /* 1668 */ while (toRemove.hasNext()) {
            /* 1669 */ MapleGuildCharacter mgc = toRemove.next();
            /* 1670 */ if (mgc.getId() == cid) {
                /* 1671 */ this.requests.remove(mgc);
                /* 1672 */ this.changed_requests = true;
                break;
            }
        }
    }

    public MapleGuildCharacter getRequest(int cid) {
        /* 1679 */ Iterator<MapleGuildCharacter> toRemove = this.requests.iterator();
        /* 1680 */ while (toRemove.hasNext()) {
            /* 1681 */ MapleGuildCharacter mgc = toRemove.next();
            /* 1682 */ if (mgc.getId() == cid) {
                /* 1683 */ return mgc;
            }
        }
        /* 1686 */ return null;
    }

    public void setNoblessSkillPoint(int point) {
        /* 1690 */ this.noblessskillpoint = point;
        /* 1691 */ writeToDB(false);
    }

    public int getNoblessSkillPoint() {
        /* 1695 */ return this.noblessskillpoint;
    }

    public void updateGuildScore(long totDamageToOneMonster) {
        /* 1699 */ double guildScore = this.guildScore;
        /* 1700 */ double add = totDamageToOneMonster / 1.0E12D;
        /* 1701 */ this.guildScore += add;
        /* 1702 */ if (guildScore != this.guildScore) {
            /* 1703 */ broadcast(CField.updateGuildScore((int) this.guildScore));
        }
    }

    public double getGuildScore() {
        /* 1708 */ Connection con = null;
        /* 1709 */ PreparedStatement ps = null;
        /* 1710 */ ResultSet rs = null;
        try {
            /* 1712 */ con = DatabaseConnection.getConnection();
            /* 1713 */ ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid = ?");
            /* 1714 */ ps.setInt(1, this.id);
            /* 1715 */ rs = ps.executeQuery();

            /* 1717 */ if (rs.next()) {
                /* 1718 */ this.guildScore = rs.getInt("score");
            }
            /* 1720 */ rs.close();
            /* 1721 */ ps.close();
            /* 1722 */ con.close();
            /* 1723 */        } catch (SQLException e) {
            /* 1724 */ //System.err.println("Error getting character default" + e);
        } finally {
            try {
                /* 1727 */ if (ps != null) {
                    /* 1728 */ ps.close();
                }
                /* 1730 */ if (rs != null) {
                    /* 1731 */ rs.close();
                }
                /* 1733 */ if (con != null) {
                    /* 1734 */ con.close();
                }
                /* 1736 */            } catch (Exception exception) {
            }
        }

        /* 1740 */ return this.guildScore;
    }

    public void setGuildScore(double guildScore) {
        /* 1744 */ this.guildScore = guildScore;
        /* 1745 */ writeToDB(false);
    }

    public final int getFame() {
        /* 1749 */ return this.fame;
    }

    public final int getBeforeAttance() {
        /* 1753 */ return this.beforeattance;
    }

    public final int getAfterAttance() {
        /* 1757 */ return this.afterattance;
    }

    public final void setLevel(int level) {
        /* 1761 */ this.guildlevel = level;
        /* 1762 */ Connection con = null;
        /* 1763 */ PreparedStatement ps = null;
        /* 1764 */ ResultSet rs = null;

        /* 1767 */ try {
            con = DatabaseConnection.getConnection();
            /* 1768 */ ps = con.prepareStatement("UPDATE guilds SET level = ? WHERE guildid = ?");
            /* 1769 */ ps.setInt(1, this.guildlevel);
            /* 1770 */ ps.setInt(2, this.id);
            /* 1771 */ ps.execute();
            /* 1772 */ ps.close();
            /* 1773 */ con.close();
        } /* 1774 */ catch (SQLException sQLException) {
            try {
                /* 1777 */ if (ps != null) {
                    /* 1778 */ ps.close();
                }
                /* 1780 */ if (rs != null) {
                    /* 1781 */ rs.close();
                }
                /* 1783 */ if (con != null) {
                    /* 1784 */ con.close();
                }
                /* 1786 */            } catch (Exception exception) {
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

    }

    public int getLastResetDay() {
        /* 1793 */ return this.lastResetDay;
    }

    public void setLastResetDay(int lastResetDay) {
        /* 1797 */ this.lastResetDay = lastResetDay;
        /* 1798 */ Connection con = null;
        /* 1799 */ PreparedStatement ps = null;
        /* 1800 */ ResultSet rs = null;

        /* 1803 */ try {
            con = DatabaseConnection.getConnection();
            /* 1804 */ ps = con.prepareStatement("UPDATE guilds SET lastresetday = ? WHERE guildid = ?");
            /* 1805 */ ps.setInt(1, this.lastResetDay);
            /* 1806 */ ps.setInt(2, this.id);
            /* 1807 */ ps.execute();
            /* 1808 */ ps.close();
            /* 1809 */ con.close();
        } /* 1810 */ catch (SQLException sQLException) {
            try {
                /* 1813 */ if (ps != null) {
                    /* 1814 */ ps.close();
                }
                /* 1816 */ if (rs != null) {
                    /* 1817 */ rs.close();
                }
                /* 1819 */ if (con != null) {
                    /* 1820 */ con.close();
                }
                /* 1822 */            } catch (Exception exception) {
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }

    }

    public int getWeekReputation() {
        /* 1829 */ return this.weekReputation;
    }

    public void setWeekReputation(int weekReputation) {
        /* 1833 */ this.weekReputation = weekReputation;
    }

    public int compareTo(MapleGuild o) {
        /* 1838 */ if (getGuildScore() < o.getGuildScore()) /* 1839 */ {
            return 1;
        }
        /* 1840 */ if (getGuildScore() > o.getGuildScore()) {
            /* 1841 */ return -1;
        }
        /* 1843 */ return 0;
    }
}


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\handling\world\guild\MapleGuild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
