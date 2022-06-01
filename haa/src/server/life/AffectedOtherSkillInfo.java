package server.life;

import java.util.LinkedHashMap;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class AffectedOtherSkillInfo {
  private static Map<Integer, AffectedOtherSkillInfo> MistAffectedInfo = new LinkedHashMap<>();
  
  private int num;
  
  private int affectedOtherSkillID;
  
  private int affectedOtherSkillLev;
  
  public AffectedOtherSkillInfo(int num, int affectedOtherSkillID, int affectedOtherSkillLev) {
    this.num = num;
    this.affectedOtherSkillID = affectedOtherSkillID;
    this.affectedOtherSkillLev = affectedOtherSkillLev;
  }
  
  public static Map<Integer, AffectedOtherSkillInfo> getMistAffectedInfo() {
    return MistAffectedInfo;
  }
  
  public static void setMistAffectedInfo(Map<Integer, AffectedOtherSkillInfo> MistAffectedInfo) {
    AffectedOtherSkillInfo.MistAffectedInfo = MistAffectedInfo;
  }
  
  public int getNum() {
    return this.num;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public int getAffectedOtherSkillID() {
    return this.affectedOtherSkillID;
  }
  
  public void setAffectedOtherSkillID(int affectedOtherSkillID) {
    this.affectedOtherSkillID = affectedOtherSkillID;
  }
  
  public int getAffectedOtherSkillLev() {
    return this.affectedOtherSkillLev;
  }
  
  public void setAffectedOtherSkillLev(int affectedOtherSkillLev) {
    this.affectedOtherSkillLev = affectedOtherSkillLev;
  }
  
  public static void loadFromWZData() {
    MistAffectedInfo.clear();
    MapleData effdata = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz/MobSkill")).getData("211.img");
    for (MapleData dataType : effdata.getChildren()) {
      for (MapleData info : dataType.getChildren()) {
        int number = Integer.parseInt(info.getName());
        for (MapleData infos : info.getChildren()) {
          if (infos.getName().equals("affectedOtherSkill"))
            for (MapleData infos1 : infos.getChildren()) {
              if (infos1.getName().equals("0") || infos1.getName().equals("1") || infos1.getName().equals("2")) {
                int number2 = Integer.parseInt(infos1.getName());
                for (MapleData infos2 : infos1.getChildren()) {
                  int i = MapleDataTool.getInt("affectedOtherSkillID", infos1, 0);
                  int j = MapleDataTool.getInt("affectedOtherSkillLev", infos1, 0);
                  AffectedOtherSkillInfo affectedOtherSkillInfo = new AffectedOtherSkillInfo(number2, i, j);
                  MistAffectedInfo.put(Integer.valueOf(number), affectedOtherSkillInfo);
                } 
                continue;
              } 
              int affectedOtherSkillID = MapleDataTool.getInt("affectedOtherSkillID", infos, 0);
              int affectedOtherSkillLev = MapleDataTool.getInt("affectedOtherSkillLev", infos, 0);
              AffectedOtherSkillInfo AOS = new AffectedOtherSkillInfo(-1, affectedOtherSkillID, affectedOtherSkillLev);
              MistAffectedInfo.put(Integer.valueOf(number), AOS);
            }  
        } 
      } 
    } 
  }
}
