package handling.world.guild;

import database.DatabaseConnection;
import handling.world.World;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import tools.packet.CWvsContext;

public class MapleGuildAlliance implements Serializable {
  public static final long serialVersionUID = 24081985245L;
  
  public static final int CHANGE_CAPACITY_COST = 10000000;
  
  private enum GAOp {
    NONE, DISBAND, NEWGUILD;
  }
  
  private final int[] guilds = new int[5];
  
  private int allianceid;
  
  private int leaderid;
  
  private int capacity;
  
  private String name;
  
  private String notice;
  
  private String[] ranks = new String[5];
  
  public MapleGuildAlliance(int id) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM alliances WHERE id = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();
      if (!rs.first()) {
        rs.close();
        ps.close();
        con.close();
        this.allianceid = -1;
        return;
      } 
      this.allianceid = id;
      this.name = rs.getString("name");
      this.capacity = rs.getInt("capacity");
      for (int i = 1; i < 6; i++) {
        this.guilds[i - 1] = rs.getInt("guild" + i);
        this.ranks[i - 1] = rs.getString("rank" + i);
      } 
      this.leaderid = rs.getInt("leaderid");
      this.notice = rs.getString("notice");
      rs.close();
      ps.close();
      con.close();
    } catch (SQLException se) {
      System.err.println("unable to read guild information from sql");
      se.printStackTrace();
      return;
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  public static final Collection<MapleGuildAlliance> loadAll() {
    Collection<MapleGuildAlliance> ret = new ArrayList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT id FROM alliances");
      rs = ps.executeQuery();
      while (rs.next()) {
        MapleGuildAlliance g = new MapleGuildAlliance(rs.getInt("id"));
        if (g.getId() > 0)
          ret.add(g); 
      } 
      rs.close();
      ps.close();
      con.close();
    } catch (SQLException se) {
      System.err.println("unable to read guild information from sql");
      se.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
    return ret;
  }
  
  public int getNoGuilds() {
    int ret = 0;
    for (int i = 0; i < this.capacity; i++) {
      if (this.guilds[i] > 0)
        ret++; 
    } 
    return ret;
  }
  
  public static final int createToDb(int leaderId, String name, int guild1, int guild2) {
    int ret = -1;
    if (name.length() > 12)
      return ret; 
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT id FROM alliances WHERE name = ?");
      ps.setString(1, name);
      rs = ps.executeQuery();
      if (rs.first()) {
        rs.close();
        ps.close();
        return ret;
      } 
      ps.close();
      rs.close();
      ps = con.prepareStatement("insert into alliances (name, guild1, guild2, leaderid) VALUES (?, ?, ?, ?)", 1);
      ps.setString(1, name);
      ps.setInt(2, guild1);
      ps.setInt(3, guild2);
      ps.setInt(4, leaderId);
      ps.execute();
      rs = ps.getGeneratedKeys();
      if (rs.next())
        ret = rs.getInt(1); 
      rs.close();
      ps.close();
    } catch (SQLException SE) {
      System.err.println("SQL THROW");
      SE.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
    return ret;
  }
  
  public final boolean deleteAlliance() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      for (int i = 0; i < getNoGuilds(); i++) {
        ps = con.prepareStatement("UPDATE characters SET alliancerank = 5 WHERE guildid = ?");
        ps.setInt(1, this.guilds[i]);
        ps.execute();
        ps.close();
      } 
      ps = con.prepareStatement("delete from alliances where id = ?");
      ps.setInt(1, this.allianceid);
      ps.execute();
      ps.close();
    } catch (SQLException SE) {
      System.err.println("SQL THROW" + SE);
      return false;
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
    return true;
  }
  
  public final void broadcast(byte[] packet) {
    broadcast(packet, -1, GAOp.NONE, false);
  }
  
  public final void broadcast(byte[] packet, int exception) {
    broadcast(packet, exception, GAOp.NONE, false);
  }
  
  public final void broadcast(byte[] packet, int exceptionId, GAOp op, boolean expelled) {
    if (op == GAOp.DISBAND) {
      World.Alliance.setOldAlliance(exceptionId, expelled, this.allianceid);
    } else if (op == GAOp.NEWGUILD) {
      World.Alliance.setNewAlliance(exceptionId, this.allianceid);
    } else {
      World.Alliance.sendGuild(packet, exceptionId, this.allianceid);
    } 
  }
  
  public final boolean disband() {
    boolean ret = deleteAlliance();
    if (ret)
      broadcast(null, -1, GAOp.DISBAND, false); 
    return ret;
  }
  
  public final void saveToDb() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("UPDATE alliances set guild1 = ?, guild2 = ?, guild3 = ?, guild4 = ?, guild5 = ?, rank1 = ?, rank2 = ?, rank3 = ?, rank4 = ?, rank5 = ?, capacity = ?, leaderid = ?, notice = ? where id = ?");
      for (int i = 0; i < 5; i++) {
        ps.setInt(i + 1, (this.guilds[i] < 0) ? 0 : this.guilds[i]);
        ps.setString(i + 6, this.ranks[i]);
      } 
      ps.setInt(11, this.capacity);
      ps.setInt(12, this.leaderid);
      ps.setString(13, this.notice);
      ps.setInt(14, this.allianceid);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException SE) {
      System.err.println("SQL THROW");
      SE.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  public void setRank(String[] ranks) {
    this.ranks = ranks;
    broadcast(CWvsContext.AlliancePacket.allianceRankChange(this.allianceid, ranks));
    saveToDb();
  }
  
  public String getRank(int rank) {
    return this.ranks[rank - 1];
  }
  
  public String[] getRanks() {
    return this.ranks;
  }
  
  public String getNotice() {
    return this.notice;
  }
  
  public void setNotice(String newNotice) {
    this.notice = newNotice;
    broadcast(CWvsContext.AlliancePacket.changeAllianceNotice(this.allianceid, newNotice));
    saveToDb();
  }
  
  public int getGuildId(int i) {
    return this.guilds[i];
  }
  
  public int getId() {
    return this.allianceid;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getCapacity() {
    return this.capacity;
  }
  
  public boolean setCapacity() {
    if (this.capacity >= 5)
      return false; 
    this.capacity++;
    broadcast(CWvsContext.AlliancePacket.getAllianceUpdate(this));
    saveToDb();
    return true;
  }
  
  public boolean addGuild(int guildid) {
    if (getNoGuilds() >= getCapacity())
      return false; 
    this.guilds[getNoGuilds()] = guildid;
    saveToDb();
    broadcast(null, guildid, GAOp.NEWGUILD, false);
    return true;
  }
  
  public boolean removeGuild(int guildid, boolean expelled) {
    return removeGuild(guildid, expelled, false);
  }
  
  public boolean removeGuild(int guildid, boolean expelled, boolean isNull) {
    for (int i = 0; i < getNoGuilds(); i++) {
      if (this.guilds[i] == guildid) {
        if (!isNull)
          broadcast(null, guildid, GAOp.DISBAND, expelled); 
        if (i > 0 && i != getNoGuilds() - 1) {
          for (int x = i + 1; x < getNoGuilds(); x++) {
            if (this.guilds[x] > 0) {
              this.guilds[x - 1] = this.guilds[x];
              if (x == getNoGuilds() - 1)
                this.guilds[x] = -1; 
            } 
          } 
        } else {
          this.guilds[i] = -1;
        } 
        if (i == 0)
          return disband(); 
        broadcast(CWvsContext.AlliancePacket.getAllianceUpdate(this));
        broadcast(CWvsContext.AlliancePacket.getGuildAlliance(this));
        saveToDb();
        return true;
      } 
    } 
    return false;
  }
  
  public int getLeaderId() {
    return this.leaderid;
  }
  
  public boolean setLeaderId(int c) {
    return setLeaderId(c, false);
  }
  
  public boolean setLeaderId(int c, boolean sameGuild) {
    if (this.leaderid == c)
      return false; 
    int g = -1;
    String leaderName = null;
    for (int i = 0; i < getNoGuilds(); i++) {
      MapleGuild g_ = World.Guild.getGuild(this.guilds[i]);
      if (g_ != null) {
        MapleGuildCharacter newLead = g_.getMGC(c);
        MapleGuildCharacter oldLead = g_.getMGC(this.leaderid);
        if (newLead != null && oldLead != null && !sameGuild)
          return false; 
        if (newLead != null && newLead.getGuildRank() == 1 && newLead.getAllianceRank() == 2) {
          g_.changeARank(c, 1);
          g = i;
          leaderName = newLead.getName();
        } 
        if (oldLead != null && oldLead.getGuildRank() == 1 && oldLead.getAllianceRank() == 1)
          g_.changeARank(this.leaderid, 2); 
      } 
    } 
    if (g == -1)
      return false; 
    int oldGuild = this.guilds[g];
    this.guilds[g] = this.guilds[0];
    this.guilds[0] = oldGuild;
    if (leaderName != null)
      broadcast(CWvsContext.serverNotice(5, "", leaderName + " has become the leader of the alliance.")); 
    broadcast(CWvsContext.AlliancePacket.changeAllianceLeader(this.allianceid, this.leaderid, c));
    broadcast(CWvsContext.AlliancePacket.updateAllianceLeader(this.allianceid, this.leaderid, c));
    broadcast(CWvsContext.AlliancePacket.getAllianceUpdate(this));
    broadcast(CWvsContext.AlliancePacket.getGuildAlliance(this));
    this.leaderid = c;
    saveToDb();
    return true;
  }
  
  public boolean changeAllianceRank(int cid, int change) {
    if (this.leaderid == cid || change < 0 || change > 1)
      return false; 
    for (int i = 0; i < getNoGuilds(); i++) {
      MapleGuild g_ = World.Guild.getGuild(this.guilds[i]);
      if (g_ != null) {
        MapleGuildCharacter chr = g_.getMGC(cid);
        if (chr != null && chr.getAllianceRank() > 2) {
          if ((change == 0 && chr.getAllianceRank() >= 5) || (change == 1 && chr.getAllianceRank() <= 3))
            return false; 
          g_.changeARank(cid, chr.getAllianceRank() + ((change == 0) ? 1 : -1));
          return true;
        } 
      } 
    } 
    return false;
  }
}
