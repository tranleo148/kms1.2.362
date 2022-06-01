package tools.wztosql;

import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import provider.MapleDataType;
import tools.Pair;

public class DumpItems {
  private final MapleDataProvider item;
  
  private final MapleDataProvider string = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"));
  
  private final MapleDataProvider character;
  
  protected final MapleData cashStringData = this.string.getData("Cash.img");
  
  protected final MapleData consumeStringData = this.string.getData("Consume.img");
  
  protected final MapleData eqpStringData = this.string.getData("Eqp.img");
  
  protected final MapleData etcStringData = this.string.getData("Etc.img");
  
  protected final MapleData insStringData = this.string.getData("Ins.img");
  
  protected final MapleData petStringData = this.string.getData("Pet.img");
  
  protected final Set<Integer> doneIds = new LinkedHashSet<>();
  
  protected boolean hadError = false;
  
  protected boolean update = false;
  
  protected int id = 0;
  
  private final Connection con = DatabaseConnection.getConnection();
  
  private final List<String> subCon = new LinkedList<>();
  
  private final List<String> subMain = new LinkedList<>();
  
  public DumpItems(boolean update) throws Exception {
    this.update = update;
    this.item = MapleDataProviderFactory.getDataProvider(new File("wz/Item.wz"));
    this.character = MapleDataProviderFactory.getDataProvider(new File("wz/Character.wz"));
    if (this.item == null || this.string == null || this.character == null)
      this.hadError = true; 
  }
  
  public boolean isHadError() {
    return this.hadError;
  }
  
  public void dumpItems() throws Exception {
    System.setProperty("wz", "wz");
    if (!this.hadError) {
      PreparedStatement psa = this.con.prepareStatement("INSERT INTO wz_itemadddata(itemid, `key`, `subKey`, `value`) VALUES (?, ?, ?, ?)");
      PreparedStatement psr = this.con.prepareStatement("INSERT INTO wz_itemrewarddata(itemid, item, prob, quantity, period, worldMsg, effect) VALUES (?, ?, ?, ?, ?, ?, ?)");
      PreparedStatement ps = this.con.prepareStatement("INSERT INTO wz_itemdata(itemid, name, msg, `desc`, slotMax, price, wholePrice, stateChange, flags, karma, meso, itemMakeLevel, questId, scrollReqs, consumeItem, totalprob, incSkill, replaceId, replaceMsg, `create`, afterImage, `forceUpgrade`, `chairType`, `nickSkill`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      PreparedStatement pse = this.con.prepareStatement("INSERT INTO wz_itemequipdata(itemid, itemLevel, `key`, `value`) VALUES (?, ?, ?, ?)");
      try {
        dumpItems(psa, psr, ps, pse);
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(this.id + " quest.");
        this.hadError = true;
      } finally {
        psr.executeBatch();
        psr.close();
        psa.executeBatch();
        psa.close();
        pse.executeBatch();
        pse.close();
        ps.executeBatch();
        ps.close();
      } 
    } 
  }
  
  public void delete(String sql) throws Exception {
    try (PreparedStatement ps = this.con.prepareStatement(sql)) {
      ps.executeUpdate();
    } 
  }
  
  public boolean doesExist(String sql) throws Exception {
    boolean ret;
    try(PreparedStatement ps = this.con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
      ret = rs.next();
    } 
    return ret;
  }
  
  public void dumpItems(MapleDataProvider d, PreparedStatement psa, PreparedStatement psr, PreparedStatement ps, PreparedStatement pse, boolean charz) throws Exception {
    for (MapleDataDirectoryEntry topDir : d.getRoot().getSubdirectories()) {
      if (!topDir.getName().equalsIgnoreCase("Special") && !topDir.getName().equalsIgnoreCase("Hair") && !topDir.getName().equalsIgnoreCase("Face") && !topDir.getName().equalsIgnoreCase("Afterimage"))
        for (MapleDataFileEntry ifile : topDir.getFiles()) {
          MapleData iz = d.getData(topDir.getName() + "/" + ifile.getName());
          if (charz || topDir.getName().equalsIgnoreCase("Pet")) {
            dumpItem(psa, psr, ps, pse, iz);
            continue;
          } 
          for (MapleData itemData : iz)
            dumpItem(psa, psr, ps, pse, itemData); 
        }  
    } 
  }
  
  public void dumpItem(PreparedStatement psa, PreparedStatement psr, PreparedStatement ps, PreparedStatement pse, MapleData iz) throws Exception {
    short ret;
    double d;
    try {
      if (iz.getName().endsWith(".img")) {
        this.id = Integer.parseInt(iz.getName().substring(0, iz.getName().length() - 4));
      } else {
        this.id = Integer.parseInt(iz.getName());
      } 
    } catch (NumberFormatException nfe) {
      return;
    } 
    if (this.doneIds.contains(Integer.valueOf(this.id)) || GameConstants.getInventoryType(this.id) == MapleInventoryType.UNDEFINED)
      return; 
    this.doneIds.add(Integer.valueOf(this.id));
    if (this.update && doesExist("SELECT * FROM wz_itemdata WHERE itemid = " + this.id))
      return; 
    ps.setInt(1, this.id);
    MapleData stringData = getStringData(this.id);
    if (stringData == null) {
      ps.setString(2, "");
      ps.setString(3, "");
      ps.setString(4, "");
    } else {
      ps.setString(2, MapleDataTool.getString("name", stringData, ""));
      ps.setString(3, MapleDataTool.getString("msg", stringData, ""));
      ps.setString(4, MapleDataTool.getString("desc", stringData, ""));
    } 
    MapleData smEntry = iz.getChildByPath("info/slotMax");
    if (smEntry == null) {
      if (GameConstants.getInventoryType(this.id) == MapleInventoryType.EQUIP) {
        ret = 1;
      } else {
        ret = 100;
      } 
    } else {
      ret = (short)MapleDataTool.getIntConvert(smEntry, -1);
    } 
    ps.setInt(5, ret);
    MapleData pData = iz.getChildByPath("info/unitPrice");
    if (pData != null) {
      try {
        d = MapleDataTool.getDouble(pData);
      } catch (Exception e) {
        d = MapleDataTool.getIntConvert(pData, -1);
      } 
    } else {
      pData = iz.getChildByPath("info/price");
      if (pData == null) {
        d = -1.0D;
      } else {
        d = MapleDataTool.getIntConvert(pData, -1);
      } 
    } 
    if (this.id == 2070019 || this.id == 2330007)
      d = 1.0D; 
    ps.setString(6, String.valueOf(d));
    ps.setInt(7, MapleDataTool.getIntConvert("info/price", iz, -1));
    ps.setInt(8, MapleDataTool.getIntConvert("info/stateChangeItem", iz, 0));
    int flags = MapleDataTool.getIntConvert("info/bagType", iz, 0);
    if (MapleDataTool.getIntConvert("info/notSale", iz, 0) > 0)
      flags |= 0x10; 
    if (MapleDataTool.getIntConvert("info/expireOnLogout", iz, 0) > 0)
      flags |= 0x20; 
    if (MapleDataTool.getIntConvert("info/pickUpBlock", iz, 0) > 0)
      flags |= 0x40; 
    if (MapleDataTool.getIntConvert("info/only", iz, 0) > 0)
      flags |= 0x80; 
    if (MapleDataTool.getIntConvert("info/accountSharable", iz, 0) > 0)
      flags |= 0x100; 
    if (MapleDataTool.getIntConvert("info/quest", iz, 0) > 0)
      flags |= 0x200; 
    if (this.id != 4310008 && MapleDataTool.getIntConvert("info/tradeBlock", iz, 0) > 0)
      flags |= 0x400; 
    if (MapleDataTool.getIntConvert("info/accountShareTag", iz, 0) > 0)
      flags |= 0x800; 
    if (MapleDataTool.getIntConvert("info/mobHP", iz, 0) > 0 && MapleDataTool.getIntConvert("info/mobHP", iz, 0) < 100)
      flags |= 0x1000; 
    ps.setInt(9, flags);
    ps.setInt(10, MapleDataTool.getIntConvert("info/tradeAvailable", iz, 0));
    ps.setInt(11, MapleDataTool.getIntConvert("info/meso", iz, 0));
    ps.setInt(12, MapleDataTool.getIntConvert("info/lv", iz, 0));
    ps.setInt(13, MapleDataTool.getIntConvert("info/questId", iz, 0));
    int totalprob = 0;
    StringBuilder scrollReqs = new StringBuilder(), consumeItem = new StringBuilder(), incSkill = new StringBuilder();
    MapleData dat = iz.getChildByPath("req");
    if (dat != null)
      for (MapleData req : dat) {
        if (scrollReqs.length() > 0)
          scrollReqs.append(","); 
        scrollReqs.append(MapleDataTool.getIntConvert(req, 0));
      }  
    dat = iz.getChildByPath("consumeItem");
    if (dat != null)
      for (MapleData req : dat) {
        if (consumeItem.length() > 0)
          consumeItem.append(","); 
        consumeItem.append(MapleDataTool.getIntConvert(req, 0));
      }  
    ps.setString(14, scrollReqs.toString());
    ps.setString(15, consumeItem.toString());
    Map<Integer, Map<String, Integer>> equipStats = new HashMap<>();
    equipStats.put(Integer.valueOf(-1), new HashMap<>());
    dat = iz.getChildByPath("mob");
    if (dat != null)
      for (MapleData child : dat)
        ((Map<String, Integer>)equipStats.get(Integer.valueOf(-1))).put("mob" + MapleDataTool.getIntConvert("id", child, 0), Integer.valueOf(MapleDataTool.getIntConvert("prob", child, 0)));  
    dat = iz.getChildByPath("info/level/case");
    if (dat != null)
      for (MapleData info : dat) {
        for (MapleData data : info) {
          if (data.getName().length() == 1 && data.getChildByPath("Skill") != null)
            for (MapleData skil : data.getChildByPath("Skill")) {
              int incSkillz = MapleDataTool.getIntConvert("id", skil, 0);
              if (incSkillz != 0) {
                if (incSkill.length() > 0)
                  incSkill.append(","); 
                incSkill.append(incSkillz);
              } 
            }  
        } 
      }  
    dat = iz.getChildByPath("info/level/info");
    if (dat != null)
      for (MapleData info : dat) {
        if (MapleDataTool.getIntConvert("exp", info, 0) == 0)
          continue; 
        int lv = Integer.parseInt(info.getName());
        if (equipStats.get(Integer.valueOf(lv)) == null)
          equipStats.put(Integer.valueOf(lv), new HashMap<>()); 
        for (MapleData data : info) {
          if (data.getName().length() > 3 && data.getType() != MapleDataType.CANVAS)
            ((Map<String, Integer>)equipStats.get(Integer.valueOf(lv))).put(data.getName().substring(3), Integer.valueOf(MapleDataTool.getIntConvert(data, 0))); 
        } 
      }  
    dat = iz.getChildByPath("info");
    if (dat != null) {
      ps.setString(21, MapleDataTool.getString("afterImage", dat, ""));
      Map<String, Integer> rett = equipStats.get(Integer.valueOf(-1));
      for (MapleData data : dat.getChildren()) {
        if (data.getName().startsWith("inc")) {
          int gg = MapleDataTool.getIntConvert(data, 0);
          if (gg != 0)
            rett.put(data.getName().substring(3), Integer.valueOf(gg)); 
        } 
      } 
      for (String stat : GameConstants.stats) {
        MapleData mapleData = dat.getChildByPath(stat);
        if (stat.equals("canLevel")) {
          if (dat.getChildByPath("level") != null)
            rett.put(stat, Integer.valueOf(1)); 
        } else if (mapleData != null) {
          if (stat.equals("skill")) {
            for (int i = 0; i < mapleData.getChildren().size(); i++)
              rett.put("skillid" + i, Integer.valueOf(MapleDataTool.getIntConvert(Integer.toString(i), mapleData, 0))); 
          } else {
            int dd = MapleDataTool.getIntConvert(mapleData, 0);
            if (dd != 0)
              rett.put(stat, Integer.valueOf(dd)); 
          } 
        } 
      } 
    } else {
      ps.setString(21, "");
    } 
    ps.setInt(22, MapleDataTool.getIntConvert("info/forceUpgrade", iz, 0));
    String a = MapleDataTool.getString("info/customChair/type", iz, "");
    ps.setString(23, a);
    ps.setInt(24, MapleDataTool.getIntConvert("info/nickSkill", iz, 0));
    pse.setInt(1, this.id);
    for (Map.Entry<Integer, Map<String, Integer>> stats : equipStats.entrySet()) {
      pse.setInt(2, ((Integer)stats.getKey()).intValue());
      for (Map.Entry<String, Integer> stat : (Iterable<Map.Entry<String, Integer>>)((Map)stats.getValue()).entrySet()) {
        pse.setString(3, stat.getKey());
        pse.setLong(4, ((Integer)stat.getValue()).intValue());
        pse.addBatch();
      } 
    } 
    dat = iz.getChildByPath("info/addition");
    if (dat != null) {
      psa.setInt(1, this.id);
      for (MapleData mapleData : dat.getChildren()) {
        Pair<String, Integer> incs = null;
        switch (mapleData.getName()) {
          case "statinc":
          case "critical":
          case "skill":
          case "mobdie":
          case "hpmpchange":
          case "elemboost":
          case "elemBoost":
          case "mobcategory":
          case "boss":
            for (MapleData subKey : mapleData.getChildren()) {
              Integer data;
              if (subKey.getName().equals("con")) {
                for (MapleData conK : subKey.getChildren()) {
                  StringBuilder sbbb;
                  switch (conK.getName()) {
                    case "job":
                      sbbb = new StringBuilder();
                      if (conK.getData() == null) {
                        for (MapleData ids : conK.getChildren()) {
                          if (Integer.valueOf(ids.getData().toString()) != null) {
                            sbbb.append(ids.getData().toString());
                            sbbb.append(",");
                          } 
                        } 
                        sbbb.deleteCharAt(sbbb.length() - 1);
                      } else if (Integer.valueOf(conK.getData().toString()) != null) {
                        sbbb.append(conK.getData().toString());
                      } 
                      psa.setString(2, mapleData.getName().equals("elemBoost") ? "elemboost" : mapleData.getName());
                      psa.setString(3, "con:job");
                      psa.setString(4, sbbb.toString());
                      psa.addBatch();
                      continue;
                    case "weekDay":
                      continue;
                  } 
                  psa.setString(2, mapleData.getName().equals("elemBoost") ? "elemboost" : mapleData.getName());
                  psa.setString(3, "con:" + conK.getName());
                  Integer integer = Integer.valueOf(conK.getData().toString());
                  if (integer != null)
                    psa.setString(4, conK.getData().toString()); 
                  psa.addBatch();
                } 
                continue;
              } 
              psa.setString(2, mapleData.getName().equals("elemBoost") ? "elemboost" : mapleData.getName());
              psa.setString(3, subKey.getName());
              try {
                data = Integer.valueOf(Integer.parseInt(subKey.getData().toString()));
              } catch (Exception e) {
                data = Integer.valueOf(0);
              } 
              if (data.intValue() != 0)
                psa.setString(4, subKey.getData().toString()); 
              psa.addBatch();
            } 
            continue;
        } 
        System.out.println("UNKNOWN EQ ADDITION : " + mapleData.getName() + " from " + this.id);
      } 
    } 
    dat = iz.getChildByPath("reward");
    if (dat != null) {
      psr.setInt(1, this.id);
      for (MapleData reward : dat) {
        psr.setInt(2, MapleDataTool.getIntConvert("item", reward, 0));
        psr.setInt(3, MapleDataTool.getIntConvert("prob", reward, 0));
        psr.setInt(4, MapleDataTool.getIntConvert("count", reward, 0));
        psr.setInt(5, MapleDataTool.getIntConvert("period", reward, 0));
        psr.setString(6, MapleDataTool.getString("worldMsg", reward, ""));
        psr.setString(7, MapleDataTool.getString("Effect", reward, ""));
        psr.addBatch();
        totalprob += MapleDataTool.getIntConvert("prob", reward, 0);
      } 
    } 
    ps.setInt(16, totalprob);
    ps.setString(17, incSkill.toString());
    dat = iz.getChildByPath("replace");
    if (dat != null) {
      ps.setInt(18, MapleDataTool.getInt("itemid", dat, 0));
      ps.setString(19, MapleDataTool.getString("msg", dat, ""));
    } else {
      ps.setInt(18, 0);
      ps.setString(19, "");
    } 
    ps.setInt(20, MapleDataTool.getInt("info/create", iz, 0));
    ps.addBatch();
  }
  
  public void dumpItems(PreparedStatement psa, PreparedStatement psr, PreparedStatement ps, PreparedStatement pse) throws Exception {
    if (!this.update) {
      delete("DELETE FROM wz_itemdata");
      delete("DELETE FROM wz_itemequipdata");
      delete("DELETE FROM wz_itemadddata");
      delete("DELETE FROM wz_itemrewarddata");
      System.out.println("Deleted wz_itemdata successfully.");
    } 
    System.out.println("Adding into wz_itemdata.....");
    dumpItems(this.item, psa, psr, ps, pse, false);
    dumpItems(this.character, psa, psr, ps, pse, true);
    System.out.println("Done wz_itemdata...");
    if (!this.subMain.isEmpty())
      System.out.println(this.subMain.toString()); 
    if (!this.subCon.isEmpty())
      System.out.println(this.subCon.toString()); 
  }
  
  public int currentId() {
    return this.id;
  }
  
  public static void main(String[] args) {
    boolean hadError = false;
    boolean update = false;
    long startTime = System.currentTimeMillis();
    for (String file : args) {
      if (file.equalsIgnoreCase("-update"))
        update = true; 
    } 
    int currentQuest = 0;
    try {
      DatabaseConnection.init();
      DumpItems dq = new DumpItems(update);
      System.out.println("Dumping Items");
      dq.dumpItems();
      hadError |= dq.isHadError();
      currentQuest = dq.currentId();
    } catch (Exception e) {
      hadError = true;
      e.printStackTrace();
      System.out.println(currentQuest + " quest.");
    } 
    long endTime = System.currentTimeMillis();
    double elapsedSeconds = (endTime - startTime) / 1000.0D;
    int elapsedSecs = (int)elapsedSeconds % 60;
    int elapsedMinutes = (int)(elapsedSeconds / 60.0D);
    String withErrors = "";
    if (hadError)
      withErrors = " with errors"; 
    System.out.println("Finished" + withErrors + " in " + elapsedMinutes + " minutes " + elapsedSecs + " seconds");
  }
  
  protected final MapleData getStringData(int itemId) {
    MapleData data;
    String cat = null;
    if (itemId >= 5010000) {
      data = this.cashStringData;
    } else if (itemId >= 2000000 && itemId < 3000000) {
      data = this.consumeStringData;
    } else if ((itemId >= 1132000 && itemId < 1183000) || (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1123000)) {
      data = this.eqpStringData;
      cat = "Eqp/Accessory";
    } else if (itemId >= 1662000 && itemId < 1680000) {
      data = this.eqpStringData;
      cat = "Eqp/Android";
    } else if (itemId >= 1000000 && itemId < 1010000) {
      data = this.eqpStringData;
      cat = "Eqp/Cap";
    } else if (itemId >= 1102000 && itemId < 1105000) {
      data = this.eqpStringData;
      cat = "Eqp/Cape";
    } else if (itemId >= 1040000 && itemId < 1050000) {
      data = this.eqpStringData;
      cat = "Eqp/Coat";
    } else if (itemId >= 20000 && itemId < 22000) {
      data = this.eqpStringData;
      cat = "Eqp/Face";
    } else if (itemId >= 1080000 && itemId < 1090000) {
      data = this.eqpStringData;
      cat = "Eqp/Glove";
    } else if (itemId >= 30000 && itemId < 35000) {
      data = this.eqpStringData;
      cat = "Eqp/Hair";
    } else if (itemId >= 1050000 && itemId < 1060000) {
      data = this.eqpStringData;
      cat = "Eqp/Longcoat";
    } else if (itemId >= 1060000 && itemId < 1070000) {
      data = this.eqpStringData;
      cat = "Eqp/Pants";
    } else if (itemId >= 1610000 && itemId < 1660000) {
      data = this.eqpStringData;
      cat = "Eqp/Mechanic";
    } else if (itemId >= 1802000 && itemId < 1820000) {
      data = this.eqpStringData;
      cat = "Eqp/PetEquip";
    } else if (itemId >= 1920000 && itemId < 2000000) {
      data = this.eqpStringData;
      cat = "Eqp/Dragon";
    } else if (itemId >= 1112000 && itemId < 1120000) {
      data = this.eqpStringData;
      cat = "Eqp/Ring";
    } else if (itemId >= 1092000 && itemId < 1100000) {
      data = this.eqpStringData;
      cat = "Eqp/Shield";
    } else if (itemId >= 1070000 && itemId < 1080000) {
      data = this.eqpStringData;
      cat = "Eqp/Shoes";
    } else if (itemId >= 1900000 && itemId < 1920000) {
      data = this.eqpStringData;
      cat = "Eqp/Taming";
    } else if (itemId >= 1200000 && itemId < 1210000) {
      data = this.eqpStringData;
      cat = "Eqp/Totem";
    } else if (itemId >= 1210000 && itemId < 1800000) {
      data = this.eqpStringData;
      cat = "Eqp/Weapon";
    } else if (itemId >= 4000000 && itemId < 5000000) {
      data = this.etcStringData;
      cat = "Etc";
    } else if (itemId >= 3000000 && itemId < 4000000) {
      data = this.insStringData;
    } else if (itemId >= 5000000 && itemId < 5010000) {
      data = this.petStringData;
    } else {
      return null;
    } 
    if (cat == null)
      return data.getChildByPath(String.valueOf(itemId)); 
    return data.getChildByPath(cat + "/" + itemId);
  }
}
