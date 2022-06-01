package constants;

import database.DatabaseConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Pair;
import tools.StringUtil;

public class AddMesoDropData {
  public static void main(String[] args) {
    List<String> strings = new ArrayList<>();
    String WZpath = System.getProperty("wz");
    MapleDataProvider stringData = MapleDataProviderFactory.getDataProvider(new File(WZpath + "/String.wz"));
    MapleData mobStringData = stringData.getData("Mob.img");
    List<Pair<Integer, Integer>> mobMesoDatas = new ArrayList<>();
    for (MapleData ms : mobStringData) {
      try {
        strings.add(ms.getName());
      } catch (Exception e) {}
    } 
    MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(WZpath + "/Mob.wz"));
    for (String string : strings) {
      try {
        if (mobStringData.getChildByPath(string) != null) {
          MapleData mobData = data.getData(StringUtil.getLeftPaddedStr(string + ".img", '0', 11));
          if (mobData != null) {
            MapleData leveldata = mobData.getChildByPath("info/level");
            if (leveldata != null) {
              int level = MapleDataTool.getInt("info/level", mobData, 0);
              System.out.println(string + " 몬스터의 레벨 : " + level);
              mobMesoDatas.add(new Pair<>(Integer.valueOf(Integer.parseInt(string)), Integer.valueOf(level)));
            } 
          } 
        } 
      } catch (Exception e) {}
    } 
    DatabaseConnection.init();
    int i = 1000;
    try {
      Connection con = DatabaseConnection.getConnection();
      for (Pair<Integer, Integer> db : mobMesoDatas) {
        PreparedStatement ps = con.prepareStatement("INSERT INTO `drop_data` VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 1);
        ps.setInt(1, i);
        ps.setInt(2, ((Integer)db.left).intValue());
        ps.setInt(3, 0);
        ps.setInt(4, ((Integer)db.right).intValue() * 25);
        ps.setInt(5, ((Integer)db.right).intValue() * 100);
        ps.setInt(6, 0);
        ps.setInt(7, 1000000);
        ps.setInt(8, 0);
        ps.executeUpdate();
        ps.close();
        i++;
      } 
      con.close();
    } catch (SQLException se) {
      se.printStackTrace();
    } 
  }
}
