package server;

import client.MapleCharacter;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import tools.Pair;
import tools.Triple;

public class ChatEmoticon {
  private static Map<Integer, List<Integer>> emoticons = new ConcurrentHashMap<>();
  
  public static void LoadEmoticon() {
    MapleData effdata = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("UI.wz")).getData("ChatEmoticon.img");
    for (MapleData data : effdata) {
      int type = Integer.parseInt(data.getName());
      List<Integer> e_list = new ArrayList<>();
      for (MapleData dat : data) {
        if (isNumeric(dat.getName()))
          e_list.add(Integer.valueOf(Integer.parseInt(dat.getName()))); 
      } 
      getEmoticons().put(Integer.valueOf(type), e_list);
    } 
  }
  
  public static void LoadChatEmoticonTabs(MapleCharacter chr) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM emoticon WHERE charid = ?");
      ps.setInt(1, chr.getId());
      rs = ps.executeQuery();
      while (rs.next()) {
        int emoticonId = rs.getInt("emoticonid");
        long time = rs.getLong("time");
        String bookmarks = rs.getString("bookmarks");
        MapleChatEmoticon em = new MapleChatEmoticon(chr.getId(), emoticonId, time, bookmarks);
        chr.getEmoticonTabs().add(em);
      } 
      ps.close();
      rs.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException sQLException) {} 
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException sQLException) {} 
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public static void LoadSavedChatEmoticon(MapleCharacter chr) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM emoticon_saved WHERE charid = ?");
      ps.setInt(1, chr.getId());
      rs = ps.executeQuery();
      while (rs.next()) {
        int emoticonId = rs.getInt("emoticonid");
        String chat = rs.getString("chat");
        MapleSavedEmoticon em = new MapleSavedEmoticon(chr.getId(), emoticonId, chat);
        chr.getSavedEmoticon().add(em);
      } 
      ps.close();
      rs.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException sQLException) {} 
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException sQLException) {} 
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public static void LoadChatEmoticons(MapleCharacter chr, List<MapleChatEmoticon> ems) {
    for (MapleChatEmoticon em : ems) {
      List<Integer> e_list = getEmoticons().get(Integer.valueOf(em.getEmoticonid()));
      for (Iterator<Integer> iterator = e_list.iterator(); iterator.hasNext(); ) {
        int e = ((Integer)iterator.next()).intValue();
        short slot = 0;
        for (Pair<Integer, Short> a : em.getBookmarks()) {
          if (((Integer)a.left).intValue() == e && ((Short)a.right).shortValue() > 0)
            slot = ((Short)a.right).shortValue(); 
        } 
        Triple<Long, Integer, Short> p = new Triple<>(Long.valueOf(em.getTime()), Integer.valueOf(e), Short.valueOf(slot));
        chr.getEmoticons().add(p);
      } 
    } 
  }
  
  public static boolean isNumeric(String input) {
    try {
      Double.parseDouble(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    } 
  }
  
  public static Map<Integer, List<Integer>> getEmoticons() {
    return emoticons;
  }
}
