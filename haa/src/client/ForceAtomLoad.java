package client;

import java.util.LinkedHashMap;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class ForceAtomLoad {
  private static Map<Integer, ForceAtomLoad> ForceAtomlist = new LinkedHashMap<>();
  
  private int firstImpactMin;
  
  private int firstImpactMax;
  
  private int secondImpactMin;
  
  private int secondImpactMax;
  
  public ForceAtomLoad(int firstImpactMin, int firstImpactMax, int secondImpactMin, int secondImpactMax) {
    this.firstImpactMin = firstImpactMin;
    this.firstImpactMax = firstImpactMax;
    this.secondImpactMin = secondImpactMin;
    this.secondImpactMax = secondImpactMax;
  }
  
  public static Map<Integer, ForceAtomLoad> getForceAtomlist() {
    return ForceAtomlist;
  }
  
  public void setForceAtomlist(Map<Integer, ForceAtomLoad> ForceAtomlist) {
    ForceAtomlist = ForceAtomlist;
  }
  
  public int getFirstImpactMin() {
    return this.firstImpactMin;
  }
  
  public void setFirstImpactMin(int firstImpactMin) {
    this.firstImpactMin = firstImpactMin;
  }
  
  public int getFirstImpactMax() {
    return this.firstImpactMax;
  }
  
  public void setFirstImpactMax(int firstImpactMax) {
    this.firstImpactMax = firstImpactMax;
  }
  
  public int getSecondImpactMin() {
    return this.secondImpactMin;
  }
  
  public void setSecondImpactMin(int secondImpactMin) {
    this.secondImpactMin = secondImpactMin;
  }
  
  public int getSecondImpactMax() {
    return this.secondImpactMax;
  }
  
  public void setSecondImpactMax(int secondImpactMax) {
    this.secondImpactMax = secondImpactMax;
  }
  
  public static void loadFromWZData() {
    ForceAtomlist.clear();
    MapleData effdata = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Effect.wz")).getData("CharacterEff.img");
    for (MapleData dataType : effdata.getChildren()) {
      if (dataType.getName().equals("forceAtom"))
        for (MapleData ForceAtomInfo : dataType.getChildren()) {
          int type = Integer.parseInt(ForceAtomInfo.getName());
          for (MapleData ForceAtomTypeInfo : ForceAtomInfo.getChildren()) {
            if (ForceAtomTypeInfo.getName().equals("info")) {
              int firstImpactMin = MapleDataTool.getInt("firstImpactMin", ForceAtomTypeInfo, 0);
              int firstImpactMax = MapleDataTool.getInt("firstImpactMax", ForceAtomTypeInfo, 0);
              int secondImpactMin = MapleDataTool.getInt("secondImpactMin", ForceAtomTypeInfo, 0);
              int secondImpactMax = MapleDataTool.getInt("secondImpactMax", ForceAtomTypeInfo, 0);
              ForceAtomLoad fal = new ForceAtomLoad(firstImpactMin, firstImpactMax, secondImpactMin, secondImpactMax);
              ForceAtomlist.put(Integer.valueOf(type), fal);
            } 
          } 
        }  
    } 
  }
}
