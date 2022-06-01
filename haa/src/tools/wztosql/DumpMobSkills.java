package tools.wztosql;

import database.DatabaseConnection;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class DumpMobSkills {
  private MapleDataProvider skill;
  
  protected boolean hadError = false;
  
  protected boolean update = false;
  
  protected int id = 0;
  
  private Connection con = DatabaseConnection.getConnection();
  
  public DumpMobSkills(boolean update) throws Exception {
    this.update = update;
    this.skill = MapleDataProviderFactory.getDataProvider(new File("Wz/Skill.wz"));
    if (this.skill == null)
      this.hadError = true; 
  }
  
  public boolean isHadError() {
    return this.hadError;
  }
  
  public void dumpMobSkills() throws Exception {
    if (!this.hadError) {
      PreparedStatement ps = this.con.prepareStatement("INSERT INTO wz_mobskilldata(skillid, `level`, hp, mpcon, x, y, time, prop, `limit`, spawneffect,`interval`, ismobgroup, summons, ltx, lty, rbx, rby, once, otherSkillID, otherSkillLev, skillAfter, forced) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      try {
        dumpMobSkills(ps);
      } catch (Exception e) {
        System.out.println(this.id + " skill.");
        e.printStackTrace();
        this.hadError = true;
      } finally {
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
  
  public void dumpMobSkills(PreparedStatement ps) throws Exception {
    if (!this.update) {
      delete("DELETE FROM wz_mobskilldata");
      System.out.println("Deleted wz_mobskilldata successfully.");
    } 
    MapleDataProvider skill2 = MapleDataProviderFactory.getDataProvider(new File("Wz/Skill.wz/MobSkill"));
    for (File fileEntry : (new File("Wz/Skill.wz/MobSkill")).listFiles()) {
      MapleData skillz = skill2.getData(fileEntry.getName().replace(".xml", ""));
      System.out.println("Adding into wz_mobskilldata.....");
      for (MapleData lvlz : skillz.getChildByPath("level").getChildren()) {
        this.id = Integer.parseInt(fileEntry.getName().replace(".img.xml", ""));
        int lvl = Integer.parseInt(lvlz.getName());
        if (this.update && doesExist("SELECT * FROM wz_mobskilldata WHERE skillid = " + this.id + " AND level = " + lvl))
          continue; 
        ps.setInt(1, this.id);
        ps.setInt(2, lvl);
        ps.setInt(3, MapleDataTool.getInt("hp", lvlz, 100));
        ps.setInt(4, MapleDataTool.getInt("mpCon", lvlz, 0));
        ps.setInt(5, MapleDataTool.getInt("x", lvlz, 1));
        ps.setInt(6, MapleDataTool.getInt("y", lvlz, 1));
        ps.setInt(7, MapleDataTool.getInt("time", lvlz, 0));
        ps.setInt(8, MapleDataTool.getInt("prop", lvlz, 100));
        if (MapleDataTool.getInt("limitSummonedMobCount", lvlz, 0) > 0) {
          ps.setInt(9, MapleDataTool.getInt("limitSummonedMobCount", lvlz, 0));
        } else {
          ps.setInt(9, MapleDataTool.getInt("limit", lvlz, 0));
        } 
        ps.setInt(10, MapleDataTool.getInt("summonEffect", lvlz, 0));
        ps.setInt(11, MapleDataTool.getInt("interval", lvlz, 0));
        StringBuilder summ = new StringBuilder();
        List<Integer> toSummon = new ArrayList<>();
        for (int i = 0; i > -1 && 
          lvlz.getChildByPath(String.valueOf(i)) != null; i++)
          toSummon.add(Integer.valueOf(MapleDataTool.getInt(lvlz.getChildByPath(String.valueOf(i)), 0))); 
        MapleData mobGroupData = lvlz.getChildByPath("mobGroup");
        if (mobGroupData != null) {
          ps.setByte(12, (byte)1);
          for (int k = 0; k > -1; k++) {
            MapleData mobGroup = mobGroupData.getChildByPath(String.valueOf(k));
            if (mobGroup == null)
              break; 
            for (int m = 0; m > -1; m++) {
              MapleData mobData = mobGroup.getChildByPath(String.valueOf(m));
              if (mobData == null)
                break; 
              toSummon.add(Integer.valueOf(MapleDataTool.getInt(mobData, 0)));
            } 
          } 
        } else {
          ps.setByte(12, (byte)0);
        } 
        for (Integer summon : toSummon) {
          if (summ.length() > 0)
            summ.append(", "); 
          summ.append(String.valueOf(summon));
        } 
        ps.setString(13, summ.toString());
        if (lvlz.getChildByPath("lt") != null) {
          Point lt = (Point)lvlz.getChildByPath("lt").getData();
          ps.setInt(14, lt.x);
          ps.setInt(15, lt.y);
        } else {
          ps.setInt(14, 0);
          ps.setInt(15, 0);
        } 
        if (lvlz.getChildByPath("rb") != null) {
          Point rb = (Point)lvlz.getChildByPath("rb").getData();
          ps.setInt(16, rb.x);
          ps.setInt(17, rb.y);
        } else {
          ps.setInt(16, 0);
          ps.setInt(17, 0);
        } 
        ps.setByte(18, (byte)((MapleDataTool.getInt("summonOnce", lvlz, 0) > 0) ? 1 : 0));
        MapleData otherSkillData = lvlz.getChildByPath("otherSkill");
        if (otherSkillData != null) {
          int otherSkillID = Integer.valueOf(MapleDataTool.getInt("otherSkillID", otherSkillData, 0)).intValue();
          int otherSkillLev = Integer.valueOf(MapleDataTool.getInt("otherSkillLev", otherSkillData, 0)).intValue();
          ps.setInt(19, otherSkillID);
          ps.setInt(20, otherSkillLev);
        } else {
          ps.setInt(19, 0);
          ps.setInt(20, 0);
        } 
        int j = MapleDataTool.getInt("skillAfter", lvlz, 0);
        ps.setInt(21, j);
        int i2 = MapleDataTool.getInt("force", lvlz, 0);
        ps.setInt(22, i2);
        System.out.println("Added skill: " + this.id + " level " + lvl);
        ps.addBatch();
      } 
    } 
    System.out.println("Done wz_mobskilldata...");
  }
  
  public int currentId() {
    return this.id;
  }
  
  public static void main(String[] args) {
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
      DumpMobSkills dq = new DumpMobSkills(update);
      System.out.println("Dumping mobskills");
      dq.dumpMobSkills();
      hadError |= dq.isHadError();
      currentQuest = dq.currentId();
    } catch (Exception e) {
      hadError = true;
      e.printStackTrace();
      System.out.println(currentQuest + " skill.");
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
}
