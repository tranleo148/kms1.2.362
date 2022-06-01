package handling.world.guild;

import client.MapleCharacter;
import database.DatabaseConnection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapleGuildCharacter implements Serializable {
  public static final long serialVersionUID = 2058609046116597760L;
  
  private MapleCharacter chr;
  
  private byte channel = -1;
  
  private byte guildrank;
  
  private byte allianceRank;
  
  private short level;
  
  private int id;
  
  private int jobid;
  
  private int guildid;
  
  private int guildContribution;
  
  private int lastattendance;
  
  private long lastDisconnectTime;
  
  private boolean online;
  
  private String name;
  
  private String request;
  
  public MapleGuildCharacter(MapleCharacter c) {
    this.name = c.getName();
    this.level = c.getLevel();
    this.id = c.getId();
    this.channel = (byte)c.getClient().getChannel();
    this.jobid = c.getJob();
    this.guildrank = c.getGuildRank();
    this.guildid = c.getGuildId();
    this.guildContribution = c.getGuildContribution();
    this.allianceRank = c.getAllianceRank();
    this.lastattendance = c.getLastAttendance();
    this.online = true;
  }
  
  public MapleGuildCharacter(int id, short lv, String name, byte channel, int job, byte rank, int guildContribution, byte allianceRank, int gid, boolean on, int lastattendance) {
    this.level = lv;
    this.id = id;
    this.name = name;
    if (on)
      this.channel = channel; 
    this.jobid = job;
    this.online = on;
    this.guildrank = rank;
    this.allianceRank = allianceRank;
    this.guildContribution = guildContribution;
    this.lastattendance = lastattendance;
    this.guildid = gid;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public void setLevel(short l) {
    this.level = l;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setChannel(byte ch) {
    this.channel = ch;
  }
  
  public int getChannel() {
    return this.channel;
  }
  
  public int getJobId() {
    return this.jobid;
  }
  
  public void setJobId(int job) {
    this.jobid = job;
  }
  
  public int getGuildId() {
    return this.guildid;
  }
  
  public void setGuildId(int gid) {
    this.guildid = gid;
  }
  
  public void setGuildRank(byte rank) {
    this.guildrank = rank;
  }
  
  public byte getGuildRank() {
    return this.guildrank;
  }
  
  public void setGuildContribution(int c) {
    this.guildContribution = c;
  }
  
  public int getGuildContribution() {
    return this.guildContribution;
  }
  
  public boolean isOnline() {
    return this.online;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setOnline(boolean f) {
    this.online = f;
  }
  
  public void setAllianceRank(byte rank) {
    this.allianceRank = rank;
  }
  
  public byte getAllianceRank() {
    return this.allianceRank;
  }
  
  public void setLastAttendance(int c) {
    this.lastattendance = c;
  }
  
  public int getLastAttendance(int cid) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
      ps.setInt(1, cid);
      rs = ps.executeQuery();
      if (rs.next())
        this.lastattendance = rs.getInt("lastattendance"); 
      rs.close();
      ps.close();
      con.close();
    } catch (SQLException e) {
      System.err.println("Error getting character default" + e);
    } finally {
      try {
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
        if (con != null)
          con.close(); 
      } catch (Exception exception) {}
    } 
    return this.lastattendance;
  }
  
  public long getLastDisconnectTime() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
      ps.setInt(1, this.id);
      rs = ps.executeQuery();
      if (rs.next())
        this.lastDisconnectTime = rs.getLong("lastDisconnectTime"); 
      rs.close();
      ps.close();
      con.close();
    } catch (SQLException e) {
      System.err.println("Error getting character default" + e);
    } finally {
      try {
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
        if (con != null)
          con.close(); 
      } catch (Exception exception) {}
    } 
    return this.lastDisconnectTime;
  }
  
  public void setLastDisconnectTime(long lastDisconnectTime) {
    this.lastDisconnectTime = lastDisconnectTime;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("UPDATE characters SET lastDisconnectTime = ? WHERE id = ?");
      ps.setLong(1, this.lastDisconnectTime);
      ps.setInt(2, this.id);
      ps.execute();
      ps.close();
      con.close();
    } catch (SQLException sQLException) {
      try {
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
        if (con != null)
          con.close(); 
      } catch (Exception exception) {}
    } finally {
      try {
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
        if (con != null)
          con.close(); 
      } catch (Exception exception) {}
    } 
  }
  
  public String getRequest() {
    return this.request;
  }
  
  public void setRequest(String request) {
    this.request = request;
  }
}
