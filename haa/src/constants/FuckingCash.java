package constants;

import client.inventory.MapleInventoryIdentifier;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scripting.NPCConversationManager;
import server.CashItemFactory;
import server.MapleItemInformationProvider;

public class FuckingCash {
  public static String getNameById(int id) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String ret = "";
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM characters WHERE `id` = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();
      if (rs.next())
        ret = rs.getString("name"); 
      ps.close();
      rs.close();
      con.close();
    } catch (SQLException se) {
      se.printStackTrace();
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } 
    return ret;
  }
  
  public static void main(String[] args) {
    System.setProperty("wz", "wz");
    DatabaseConnection.init();
    MapleInventoryIdentifier.getInstance();
    CashItemFactory.getInstance().initialize();
    MapleItemInformationProvider.getInstance().runEtc();
    MapleItemInformationProvider.getInstance().runItems();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE `inventorytype` = -1 AND (`position` <= -100 AND `position` > -200)");
      rs = ps.executeQuery();
      while (rs.next()) {
        boolean isCash = MapleItemInformationProvider.getInstance().isCash(rs.getInt("itemid"));
        if (!isCash) {
          String data = "캐릭터명 : " + getNameById(rs.getInt("characterid")) + " (AccountID : " + rs.getInt("accountid") + ", itemID : " + rs.getInt("itemid") + ") 이 검출됨.\r\n";
          data = data + "포지션 : " + rs.getInt("position") + "\r\n\r\n";
          NPCConversationManager.writeLog("Log/CashFucker.log", data, true);
        } 
      } 
      ps.close();
      rs.close();
      con.close();
    } catch (SQLException se) {
      se.printStackTrace();
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } 
  }
}
