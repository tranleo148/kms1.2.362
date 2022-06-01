package server.life;

import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapleMonsterInformationProvider {
  private static final MapleMonsterInformationProvider instance = new MapleMonsterInformationProvider();
  
  private final Map<Integer, ArrayList<MonsterDropEntry>> drops = new HashMap<>();
  
  public static MapleMonsterInformationProvider getInstance() {
    return instance;
  }
  
  public ArrayList<MonsterDropEntry> retrieveDrop(int monsterId) {
    if (this.drops.containsKey(Integer.valueOf(monsterId)))
      return this.drops.get(Integer.valueOf(monsterId)); 
    ArrayList<MonsterDropEntry> ret = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection con = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM drop_data WHERE dropperid = ?");
      ps.setInt(1, monsterId);
      rs = ps.executeQuery();
      while (rs.next())
        ret.add(new MonsterDropEntry(rs
              .getInt("itemid"), rs
              .getInt("chance"), rs
              .getInt("minimum_quantity"), rs
              .getInt("maximum_quantity"), rs
              .getInt("questid"), rs.getInt("private"))); 
      ps.close();
      rs.close();
      ps = con.prepareStatement("SELECT * FROM drop_data_global WHERE chance > 0");
      rs = ps.executeQuery();
      while (rs.next())
        ret.add(new MonsterDropEntry(rs
              
              .getInt("itemid"), rs
              .getInt("chance"), rs

              
              .getInt("minimum_quantity"), rs
              .getInt("maximum_quantity"), rs
              .getInt("questid"))); 
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
        if (con != null)
          con.close(); 
      } catch (SQLException ignore) {
        ignore.printStackTrace();
        return ret;
      } 
    } 
    this.drops.put(Integer.valueOf(monsterId), ret);
    return ret;
  }
  
  public void clearDrops() {
    this.drops.clear();
  }
  
  public boolean contains(ArrayList<MonsterDropEntry> e, int toAdd) {
    for (MonsterDropEntry f : e) {
      if (f.itemId == toAdd)
        return true; 
    } 
    return false;
  }
  
  public int chanceLogic(int itemId) {
    if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP)
      return 50000; 
    if (GameConstants.getInventoryType(itemId) == MapleInventoryType.SETUP || GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH)
      return 500; 
    switch (itemId / 10000) {
      case 204:
      case 207:
      case 229:
      case 233:
        return 500;
      case 401:
      case 402:
        return 5000;
      case 403:
        return 5000;
    } 
    return 20000;
  }
}
