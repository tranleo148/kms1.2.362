package client;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DreamBreakerRank {
  public static Map<String, Integer> Rank = new LinkedHashMap<>();
  
  public static void LoadRank() throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM dreambreaker");
      rs = ps.executeQuery();
      rs.last();
      rs.beforeFirst();
      while (rs.next()) {
        String Name = rs.getString("name");
        int Point = rs.getInt("floor") * 1000 + 180 - rs.getInt("time");
        Rank.put(Name, Integer.valueOf(Point));
      } 
      Rank = sortByValue(Rank);
      ps.close();
      rs.close();
    } catch (SQLException ex) {
      Logger.getLogger(DreamBreakerRank.class.getName()).log(Level.SEVERE, (String)null, ex);
    } finally {
      if (con != null)
        con.close(); 
      if (ps != null)
        ps.close(); 
      if (rs != null)
        rs.close(); 
    } 
  }
  
  public static void SaveRank() throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = null;
      for (Map.Entry<String, Integer> info : Rank.entrySet()) {
        ps = con.prepareStatement("SELECT * FROM dreambreaker WHERE name = ?");
        int floor = ((Integer)info.getValue()).intValue() / 1000;
        int time = 180 - ((Integer)info.getValue()).intValue() % 1000;
        ps.setString(1, info.getKey());
        rs = ps.executeQuery();
        if (rs.next()) {
          ps = con.prepareStatement("UPDATE dreambreaker SET floor = ?, time = ? WHERE name = ?");
          ps.setInt(1, floor);
          ps.setInt(2, time);
          ps.setString(3, info.getKey());
          ps.executeUpdate();
          ps.close();
        } else {
          SaveNewRecord(con, ps, info.getKey(), floor, time);
        } 
        ps.close();
        rs.close();
      } 
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (con != null)
        con.close(); 
      if (ps != null)
        ps.close(); 
      if (rs != null)
        rs.close(); 
    } 
  }
  
  public static void WipeRecord() throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM dreambreaker");
      rs = ps.executeQuery();
      rs.last();
      rs.beforeFirst();
      rs = ps.executeQuery();
      while (rs.next()) {
        int cid = getCid(rs.getString("name"));
        PreparedStatement ps1 = con.prepareStatement("SELECT * FROM questinfo WHERE characterid = ? AND quest = ?");
        ps1.setInt(1, cid);
        ps1.setInt(2, 20190131);
        ResultSet rs2 = ps1.executeQuery();
        PreparedStatement ps2 = con.prepareStatement("INSERT INTO keyvalue (`id`, `key`, `value`) VALUES (?, ?, ?)");
        String[] temp = rs2.getString("custumData").split(";");
        String temp2 = temp[0] + "lastweek=" + getRank(rs.getString("name")) + temp[2];
        ps2.setInt(1, cid);
        ps2.setString(2, "db_lastweek");
        ps2.setString(3, temp2);
        ps2.execute();
        ps1.close();
        ps2.close();
        rs2.close();
      } 
      ps.close();
      rs.close();
      ps = con.prepareStatement("TRUNCATE dreambreaker");
      ps.executeQuery();
      ps.close();
      con.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      if (con != null)
        con.close(); 
      if (ps != null)
        ps.close(); 
      if (rs != null)
        rs.close(); 
    } 
    Rank.clear();
  }
  
  public static int getCid(String name) throws SQLException {
    int ret = 0;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM characters WHERE name = ?");
      ps.setString(1, name);
      rs = ps.executeQuery();
      while (rs.next())
        ret = rs.getInt("id"); 
      ps.close();
      rs.close();
      con.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      if (con != null)
        con.close(); 
      if (ps != null)
        ps.close(); 
      if (rs != null)
        rs.close(); 
    } 
    return ret;
  }
  
  public static void SaveNewRecord(Connection con, PreparedStatement ps, String name, int floor, int time) {
    try {
      ps = con.prepareStatement("INSERT INTO dreambreaker (name, floor, time) VALUES (?, ?, ?)");
      ps.setString(1, name);
      ps.setInt(2, floor);
      ps.setInt(3, time);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  public static void EditRecord(String name, long floor, long time) throws SQLException {
    Rank.put(name, Integer.valueOf((int)floor * 1000 + 180 - (int)time));
    Rank = sortByValue(Rank);
    SaveRank();
  }
  
  public static int getRank(String name) {
    int index = 1;
    if (!Rank.containsKey(name))
      return 0; 
    for (Map.Entry<String, Integer> info : Rank.entrySet()) {
      if (((String)info.getKey()).equals(name))
        break; 
      index++;
    } 
    return index;
  }
  
    public static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
