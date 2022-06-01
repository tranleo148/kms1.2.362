package client;

import constants.GameConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import tools.Triple;
import tools.packet.CSPacket;

public class MapleCharacterUtil {
  private static final Pattern namePattern = Pattern.compile("[a-zA-Z0-9]{4,12}");
  
  private static final Pattern name2Pattern = Pattern.compile("[가-힣a-zA-Z0-9\\w\\s]{4,12}");
  
  private static final Pattern petPattern = Pattern.compile("[a-zA-Z0-9]{4,12}");
  
  public static final boolean canCreateChar(String name, boolean gm) {
    if ((name.getBytes()).length < 4 || (name.getBytes()).length > 13 || getIdByName(name) != -1)
      return false; 
    return true;
  }
  
  public static final boolean canCreateChar(String name) {
    if ((name.getBytes()).length < 2 || (name.getBytes()).length > 13)
      return false; 
    return true;
  }
  
  public static final boolean isEligibleCharName(String name, boolean gm) {
    if (name.length() > 12)
      return false; 
    if (gm)
      return true; 
    if (name.length() < 3 || !namePattern.matcher(name).matches())
      return false; 
    for (String z : GameConstants.RESERVED) {
      if (name.indexOf(z) != -1)
        return false; 
    } 
    return true;
  }
  
  public static final boolean isEligibleCharNameTwo(String name, boolean gm) {
    if (name.length() > 12)
      return false; 
    if (gm)
      return true; 
    for (String z : GameConstants.RESERVED) {
      if (name.indexOf(z) != -1)
        return false; 
    } 
    return true;
  }
  
  public static final boolean canChangePetName(String name) {
    if ((name.getBytes(Charset.forName("MS949"))).length > 12)
      return false; 
    if ((name.getBytes(Charset.forName("MS949"))).length < 3)
      return false; 
    if (petPattern.matcher(name).matches()) {
      for (String z : GameConstants.RESERVED) {
        if (name.indexOf(z) != -1)
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  public static final String makeMapleReadable(String in) {
    String wui = in.replace('I', 'i');
    wui = wui.replace('l', 'L');
    wui = wui.replace("rn", "Rn");
    wui = wui.replace("vv", "Vv");
    wui = wui.replace("VV", "Vv");
    return wui;
  }
  
  public static final int getIdByName(String name) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT id FROM characters WHERE name = ?");
      ps.setString(1, name);
      rs = ps.executeQuery();
      if (!rs.next()) {
        rs.close();
        ps.close();
        return -1;
      } 
      int id = rs.getInt("id");
      rs.close();
      ps.close();
      return id;
    } catch (SQLException e) {
      System.err.println("error 'getIdByName' " + e);
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
    return -1;
  }
  
  public static final int getAccByName(String name) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT accountid FROM characters WHERE name LIKE ?");
      ps.setString(1, name);
      rs = ps.executeQuery();
      if (!rs.next()) {
        rs.close();
        ps.close();
        return -1;
      } 
      int id = rs.getInt("accountid");
      rs.close();
      ps.close();
      return id;
    } catch (SQLException e) {
      System.err.println("error 'getIdByName' " + e);
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
    return -1;
  }
  
  public static final int Change_SecondPassword(int accid, String password, String newpassword) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String SHA1hashedsecond;
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * from accounts where id = ?");
      ps.setInt(1, accid);
      rs = ps.executeQuery();
      if (!rs.next()) {
        rs.close();
        ps.close();
        return -1;
      } 
      String secondPassword = rs.getString("2ndpassword");
      String salt2 = rs.getString("salt2");
      if (secondPassword != null && salt2 != null) {
        secondPassword = LoginCrypto.rand_r(secondPassword);
      } else if (secondPassword == null && salt2 == null) {
        rs.close();
        ps.close();
        return 0;
      } 
      if (!check_ifPasswordEquals(secondPassword, password, salt2)) {
        rs.close();
        ps.close();
        return 1;
      } 
      rs.close();
      ps.close();
      try {
        SHA1hashedsecond = LoginCryptoLegacy.encodeSHA1(newpassword);
      } catch (Exception e) {
        return -2;
      } 
      ps = con.prepareStatement("UPDATE accounts set 2ndpassword = ?, salt2 = ? where id = ?");
      ps.setString(1, SHA1hashedsecond);
      ps.setString(2, (String)null);
      ps.setInt(3, accid);
      if (!ps.execute()) {
        ps.close();
        return 2;
      } 
      ps.close();
      return -2;
    } catch (SQLException e) {
      System.err.println("error 'getIdByName' " + e);
      return -2;
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
  
  private static final boolean check_ifPasswordEquals(String passhash, String pwd, String salt) {
    if (LoginCryptoLegacy.isLegacyPassword(passhash) && LoginCryptoLegacy.checkPassword(pwd, passhash))
      return true; 
    if (salt == null && LoginCrypto.checkSha1Hash(passhash, pwd))
      return true; 
    if (LoginCrypto.checkSaltedSha512Hash(passhash, pwd, salt))
      return true; 
    return false;
  }
  
  public static Triple<Integer, Integer, Integer> getInfoByName(String name, int world) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM characters WHERE name = ? AND world = ?");
      ps.setString(1, name);
      ps.setInt(2, world);
      rs = ps.executeQuery();
      if (!rs.next()) {
        rs.close();
        ps.close();
        return null;
      } 
      Triple<Integer, Integer, Integer> id = new Triple<>(Integer.valueOf(rs.getInt("id")), Integer.valueOf(rs.getInt("accountid")), Integer.valueOf(rs.getInt("gender")));
      rs.close();
      ps.close();
      con.close();
      return id;
    } catch (Exception e) {
      e.printStackTrace();
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
    return null;
  }
  
  public static void setNXCodeUsed(String name, String code) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = con.prepareStatement("UPDATE nxcode SET `user` = ?, `valid` = 0 WHERE code = ?");
      ps.setString(1, name);
      ps.setString(2, code);
      ps.execute();
      ps.close();
      con.close();
    } catch (Exception exception) {
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
  
  public static void sendNote(String to, String name, String msg, int fame, int type, int senderid) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`, `gift`, `show`, `type`, `senderid`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, to);
      ps.setString(2, name);
      ps.setString(3, msg);
      ps.setLong(4, System.currentTimeMillis());
      ps.setInt(5, fame);
      ps.setInt(6, 1);
      ps.setInt(7, type);
      ps.setInt(8, senderid);
      ps.executeUpdate();
      ps.close();
      con.close();
    } catch (SQLException e) {
      System.err.println("Unable to send note" + e);
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
      } catch (SQLException sQLException) {}
    } 
    if (World.Find.findChannel(to) >= 0) {
      MapleCharacter chr = ChannelServer.getInstance(World.Find.findChannel(to)).getPlayerStorage().getCharacterByName(to);
      if (chr != null)
        chr.getClient().send(CSPacket.NoteHandler(16, 0)); 
    } 
  }
  
  public static Triple<Boolean, Integer, Integer> getNXCodeInfo(String code) throws SQLException {
    Triple<Boolean, Integer, Integer> ret = null;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT `valid`, `type`, `item` FROM nxcode WHERE code LIKE ?");
      ps.setString(1, code);
      rs = ps.executeQuery();
      if (rs.next())
        ret = new Triple<>(Boolean.valueOf((rs.getInt("valid") > 0)), Integer.valueOf(rs.getInt("type")), Integer.valueOf(rs.getInt("item"))); 
      rs.close();
      ps.close();
      con.close();
    } catch (Exception exception) {
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
}
