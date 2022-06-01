package tools.wztosql;

import database.DatabaseConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.quest.MapleQuestActionType;
import server.quest.MapleQuestRequirementType;
import tools.Pair;

public class DumpQuests {
  private MapleDataProvider quest;
  
  protected boolean hadError = false;
  
  protected boolean update = false;
  
  protected int id = 0;
  
  private Connection con = DatabaseConnection.getConnection();
  
  public DumpQuests(boolean update) throws Exception {
    this.update = update;
    this.quest = MapleDataProviderFactory.getDataProvider(new File("Wz/Quest.wz"));
    if (this.quest == null)
      this.hadError = true; 
  }
  
  public boolean isHadError() {
    return this.hadError;
  }
  
  public void dumpQuests() throws Exception {
    if (!this.hadError) {
      PreparedStatement psai = this.con.prepareStatement("INSERT INTO wz_questactitemdata(uniqueid, itemid, count, period, gender, job, jobEx, prop) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      PreparedStatement psas = this.con.prepareStatement("INSERT INTO wz_questactskilldata(uniqueid, skillid, skillLevel, masterLevel) VALUES (?, ?, ?, ?)");
      PreparedStatement psaq = this.con.prepareStatement("INSERT INTO wz_questactquestdata(uniqueid, quest, state) VALUES (?, ?, ?)");
      PreparedStatement ps = this.con.prepareStatement("INSERT INTO wz_questdata(questid, name, autoStart, autoPreComplete, viewMedalItem, selectedSkillID, blocked, autoAccept, autoComplete, autoCompleteAction) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      PreparedStatement psr = this.con.prepareStatement("INSERT INTO wz_questreqdata(questid, type, name, stringStore, intStoresFirst, intStoresSecond) VALUES (?, ?, ?, ?, ?, ?)");
      PreparedStatement psq = this.con.prepareStatement("INSERT INTO wz_questpartydata(questid, rank, mode, property, value) VALUES(?,?,?,?,?)");
      PreparedStatement psa = this.con.prepareStatement("INSERT INTO wz_questactdata(questid, type, name, intStore, applicableJobs, uniqueid) VALUES (?, ?, ?, ?, ?, ?)");
      try {
        dumpQuests(psai, psas, psaq, ps, psr, psq, psa);
      } catch (Exception e) {
        System.out.println(this.id + " quest.");
        e.printStackTrace();
        this.hadError = true;
      } finally {
        psai.executeBatch();
        psai.close();
        psas.executeBatch();
        psas.close();
        psaq.executeBatch();
        psaq.close();
        psa.executeBatch();
        psa.close();
        psr.executeBatch();
        psr.close();
        psq.executeBatch();
        psq.close();
        ps.executeBatch();
        ps.close();
      } 
    } 
  }
  
  public void delete(String sql) throws Exception {
    PreparedStatement ps = this.con.prepareStatement(sql);
    ps.executeUpdate();
    ps.close();
  }
  
  public boolean doesExist(String sql) throws Exception {
    PreparedStatement ps = this.con.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    boolean ret = rs.next();
    rs.close();
    ps.close();
    return ret;
  }
  
  public void dumpQuests(PreparedStatement psai, PreparedStatement psas, PreparedStatement psaq, PreparedStatement ps, PreparedStatement psr, PreparedStatement psq, PreparedStatement psa) throws Exception {
    if (!this.update) {
      delete("DELETE FROM wz_questdata");
      delete("DELETE FROM wz_questactdata");
      delete("DELETE FROM wz_questactitemdata");
      delete("DELETE FROM wz_questactskilldata");
      delete("DELETE FROM wz_questactquestdata");
      delete("DELETE FROM wz_questreqdata");
      delete("DELETE FROM wz_questpartydata");
      System.out.println("Deleted wz_questdata successfully.");
    } 
    MapleData checkz = this.quest.getData("Check.img");
    MapleData actz = this.quest.getData("Act.img");
    MapleData infoz = this.quest.getData("QuestInfo.img");
    MapleData pinfoz = this.quest.getData("PQuest.img");
    System.out.println("Adding into wz_questdata.....");
    int uniqueid = 0;
    for (MapleData qz : checkz.getChildren()) {
      this.id = Integer.parseInt(qz.getName());
      if (this.update && doesExist("SELECT * FROM wz_questdata WHERE questid = " + this.id))
        continue; 
      ps.setInt(1, this.id);
      for (int i = 0; i < 2; i++) {
        MapleData reqData = qz.getChildByPath(String.valueOf(i));
        if (reqData != null) {
          psr.setInt(1, this.id);
          psr.setInt(2, i);
          for (MapleData req : reqData.getChildren()) {
            if (MapleQuestRequirementType.getByWZName(req.getName()) == MapleQuestRequirementType.UNDEFINED)
              continue; 
            psr.setString(3, req.getName());
            if (req.getName().equals("fieldEnter")) {
              psr.setString(4, String.valueOf(MapleDataTool.getIntConvert("0", req, 0)));
            } else if (req.getName().equals("end") || req.getName().equals("startscript") || req.getName().equals("endscript")) {
              psr.setString(4, MapleDataTool.getString(req, ""));
            } else {
              psr.setString(4, String.valueOf(MapleDataTool.getInt(req, 0)));
            } 
            StringBuilder intStore1 = new StringBuilder();
            StringBuilder intStore2 = new StringBuilder();
            List<Pair<Integer, Integer>> dataStore = new LinkedList<>();
            if (req.getName().equals("job")) {
              List<MapleData> child = req.getChildren();
              for (int x = 0; x < child.size(); x++)
                dataStore.add(new Pair<>(Integer.valueOf(i), Integer.valueOf(MapleDataTool.getInt(child.get(x), -1)))); 
            } else if (req.getName().equals("skill")) {
              List<MapleData> child = req.getChildren();
              for (int x = 0; x < child.size(); x++) {
                MapleData childdata = child.get(x);
                if (childdata != null)
                  dataStore.add(new Pair<>(Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("id"), 0)), 
                        Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("acquire"), 0)))); 
              } 
            } else if (req.getName().equals("quest")) {
              List<MapleData> child = req.getChildren();
              for (int x = 0; x < child.size(); x++) {
                MapleData childdata = child.get(x);
                if (childdata != null)
                  dataStore.add(new Pair<>(Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("id"), 0)), Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("state"), 0)))); 
              } 
            } else if (req.getName().equals("item") || req.getName().equals("mob")) {
              List<MapleData> child = req.getChildren();
              for (int x = 0; x < child.size(); x++) {
                MapleData childdata = child.get(x);
                if (childdata != null)
                  dataStore.add(new Pair<>(Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("id"), 0)), Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("count"), 0)))); 
              } 
            } else if (req.getName().equals("mbcard")) {
              List<MapleData> child = req.getChildren();
              for (int x = 0; x < child.size(); x++) {
                MapleData childdata = child.get(x);
                if (childdata != null)
                  dataStore.add(new Pair<>(Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("id"), 0)), 
                        Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("min"), 0)))); 
              } 
            } else if (req.getName().equals("pet")) {
              List<MapleData> child = req.getChildren();
              for (int x = 0; x < child.size(); x++) {
                MapleData childdata = child.get(x);
                if (childdata != null)
                  dataStore.add(new Pair<>(Integer.valueOf(i), 
                        Integer.valueOf(MapleDataTool.getInt(childdata.getChildByPath("id"), 0)))); 
              } 
            } 
            for (Pair<Integer, Integer> data : dataStore) {
              if (intStore1.length() > 0) {
                intStore1.append(", ");
                intStore2.append(", ");
              } 
              intStore1.append(data.getLeft());
              intStore2.append(data.getRight());
            } 
            psr.setString(5, intStore1.toString());
            psr.setString(6, intStore2.toString());
            psr.addBatch();
          } 
        } 
        MapleData actData = actz.getChildByPath(this.id + "/" + i);
        if (actData != null) {
          psa.setInt(1, this.id);
          psa.setInt(2, i);
          for (MapleData act : actData.getChildren()) {
            if (MapleQuestActionType.getByWZName(act.getName()) == MapleQuestActionType.UNDEFINED)
              continue; 
            psa.setString(3, act.getName());
            if (act.getName().equals("sp")) {
              psa.setInt(4, MapleDataTool.getIntConvert("0/sp_value", act, 0));
            } else {
              psa.setInt(4, MapleDataTool.getInt(act, 0));
            } 
            StringBuilder applicableJobs = new StringBuilder();
            if (act.getName().equals("sp") || act.getName().equals("skill")) {
              int index = 0;
              while (act.getChildByPath(index + "/job") != null) {
                for (MapleData d : act.getChildByPath(index + "/job")) {
                  if (applicableJobs.length() > 0)
                    applicableJobs.append(", "); 
                  applicableJobs.append(MapleDataTool.getInt(d, 0));
                } 
                index++;
              } 
            } else if (act.getChildByPath("job") != null) {
              for (MapleData d : act.getChildByPath("job")) {
                if (applicableJobs.length() > 0)
                  applicableJobs.append(", "); 
                applicableJobs.append(MapleDataTool.getInt(d, 0));
              } 
            } 
            psa.setString(5, applicableJobs.toString());
            psa.setInt(6, -1);
            if (act.getName().equals("item")) {
              uniqueid++;
              psa.setInt(6, uniqueid);
              psai.setInt(1, uniqueid);
              for (MapleData iEntry : act.getChildren()) {
                psai.setInt(2, MapleDataTool.getInt("id", iEntry, 0));
                psai.setInt(3, MapleDataTool.getInt("count", iEntry, 0));
                psai.setInt(4, MapleDataTool.getInt("period", iEntry, 0));
                psai.setInt(5, MapleDataTool.getInt("gender", iEntry, 2));
                psai.setInt(6, MapleDataTool.getInt("job", iEntry, -1));
                psai.setInt(7, MapleDataTool.getInt("jobEx", iEntry, -1));
                if (iEntry.getChildByPath("prop") == null) {
                  psai.setInt(8, -2);
                } else {
                  psai.setInt(8, MapleDataTool.getInt("prop", iEntry, -1));
                } 
                psai.addBatch();
              } 
            } else if (act.getName().equals("skill")) {
              uniqueid++;
              psa.setInt(6, uniqueid);
              psas.setInt(1, uniqueid);
              for (MapleData sEntry : act) {
                psas.setInt(2, MapleDataTool.getInt("id", sEntry, 0));
                psas.setInt(3, MapleDataTool.getInt("skillLevel", sEntry, 0));
                psas.setInt(4, MapleDataTool.getInt("masterLevel", sEntry, 0));
                psas.addBatch();
              } 
            } else if (act.getName().equals("quest")) {
              uniqueid++;
              psa.setInt(6, uniqueid);
              psaq.setInt(1, uniqueid);
              for (MapleData sEntry : act) {
                psaq.setInt(2, MapleDataTool.getInt("id", sEntry, 0));
                psaq.setInt(3, MapleDataTool.getInt("state", sEntry, 0));
                psaq.addBatch();
              } 
            } 
            psa.addBatch();
          } 
        } 
      } 
      MapleData infoData = infoz.getChildByPath(String.valueOf(this.id));
      if (infoData != null) {
        ps.setString(2, MapleDataTool.getString("name", infoData, ""));
        ps.setInt(3, (MapleDataTool.getInt("autoStart", infoData, 0) > 0) ? 1 : 0);
        ps.setInt(4, (MapleDataTool.getInt("autoPreComplete", infoData, 0) > 0) ? 1 : 0);
        ps.setInt(5, MapleDataTool.getInt("viewMedalItem", infoData, 0));
        ps.setInt(6, MapleDataTool.getInt("selectedSkillID", infoData, 0));
        ps.setInt(7, MapleDataTool.getInt("blocked", infoData, 0));
        ps.setInt(8, MapleDataTool.getInt("autoAccept", infoData, 0));
        ps.setInt(9, MapleDataTool.getInt("autoComplete", infoData, 0));
        ps.setInt(10, MapleDataTool.getInt("autoCompleteAction", infoData, 0));
      } else {
        ps.setString(2, "");
        ps.setInt(3, 0);
        ps.setInt(4, 0);
        ps.setInt(5, 0);
        ps.setInt(6, 0);
        ps.setInt(7, 0);
        ps.setInt(8, 0);
        ps.setInt(9, 0);
        ps.setInt(10, 0);
      } 
      ps.addBatch();
      MapleData pinfoData = pinfoz.getChildByPath(String.valueOf(this.id));
      if (pinfoData != null && pinfoData.getChildByPath("rank") != null) {
        psq.setInt(1, this.id);
        for (MapleData d : pinfoData.getChildByPath("rank")) {
          psq.setString(2, d.getName());
          for (MapleData c : d) {
            psq.setString(3, c.getName());
            for (MapleData b : c) {
              psq.setString(4, b.getName());
              psq.setInt(5, MapleDataTool.getInt(b, 0));
              psq.addBatch();
            } 
          } 
        } 
      } 
      System.out.println("Added quest: " + this.id);
    } 
    System.out.println("Done wz_questdata...");
  }
  
  public int currentId() {
    return this.id;
  }
  
  public static void main(String[] args) {
    try {
      DatabaseConnection.init();
      boolean hadError = false;
      boolean update = false;
      long startTime = System.currentTimeMillis();
      for (String file : args) {
        if (file.equalsIgnoreCase("-update"))
          update = true; 
      } 
      int currentQuest = 0;
      try {
        DumpQuests dq = new DumpQuests(update);
        System.out.println("Dumping quests");
        dq.dumpQuests();
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
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
