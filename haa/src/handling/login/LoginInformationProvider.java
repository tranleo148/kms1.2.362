package handling.login;

import constants.GameConstants;
import constants.ServerConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Triple;

public class LoginInformationProvider {
  public enum JobType {
    UltimateAdventurer(-1, 0, 100000000, false, false, true, false),
    Resistance(0, 3000, 931000000, false, false, false, false),
    Adventurer(1, 0, 4000000, false, false, false, false),
    Cygnus(2, 1000, 130030000, false, false, false, true),
    Aran(3, 2000, 914000000, false, false, true, false),
    Evan(4, 2001, 900010000, false, false, true, false),
    Mercedes(5, 2002, 910150000, false, false, false, false),
    Demon(6, 3001, 931050310, true, false, false, false),
    Phantom(7, 2003, 915000000, false, false, false, true),
    DualBlade(8, 0, 103050900, false, false, false, false),
    Mihile(9, 5000, 913070000, false, false, true, false),
    Luminous(10, 2004, 101000000, false, false, false, true),
    Kaiser(11, 6000, 0, false, false, false, false),
    AngelicBuster(12, 6001, 940011000, false, false, false, false),
    Cannoneer(13, 1, 0, false, false, true, false),
    Xenon(14, 3002, 931050920, true, false, false, false),
    Zero(15, 10100, 100000000, false, false, false, true),
    EunWol(16, 2005, 552000050, false, false, true, true),
    PinkBean(17, 13000, 0, false, false, false, false),
    Kinesis(18, 14000, 0, false, false, false, false),
    Kadena(19, 6002, 940200405, false, false, false, false),
    Iliume(20, 15000, 940200405, false, false, false, false),
    ark(21, 15001, 940200405, true, false, false, false),
    pathFinder(22, 0, 100000000, false, true, false, false),
    Hoyeong(23, 16000, 100000000, true, false, false, true),
    Adel(24, 15002, 100000000, false, false, false, false),
    Cain(25, 6003, 100000000, false, false, false, false),
    Yeti(26, 13500, 100000000, false, false, false, false),
    Lara(27, 16001, 100000000, false, false, false, false);
    
    public int type;
    
    public int id;
    
    public int map;
    
    public boolean hairColor;
    
    public boolean skinColor;
    
    public boolean faceMark;
    
    public boolean hat;
    
    public boolean bottom;
    
    public boolean cape;
    
    JobType(int type, int id, int map, boolean faceMark, boolean hat, boolean bottom, boolean cape) {
      this.type = type;
      this.id = id;
      this.map = ServerConstants.StartMap;
      this.faceMark = faceMark;
      this.hat = hat;
      this.bottom = bottom;
      this.cape = cape;
    }
    
    public static JobType getByType(int g) {
      if (g == Cannoneer.type)
        return Adventurer; 
      for (JobType e : values()) {
        if (e.type == g)
          return e; 
      } 
      return null;
    }
    
    public static JobType getById(int g) {
      if (g == Adventurer.id)
        return Adventurer; 
      for (JobType e : values()) {
        if (e.id == g)
          return e; 
      } 
      return null;
    }
  }
  
  private static final LoginInformationProvider instance = new LoginInformationProvider();
  
  protected final List<String> ForbiddenName = new ArrayList<>();
  
  protected final Map<Triple<Integer, Integer, Integer>, List<Integer>> makeCharInfo = new HashMap<>();
  
  public static LoginInformationProvider getInstance() {
    return instance;
  }
  
  protected LoginInformationProvider() {
    String WZpath = System.getProperty("wz");
    MapleDataProvider prov = MapleDataProviderFactory.getDataProvider(new File(WZpath + "/Etc.wz"));
    MapleData nameData = prov.getData("ForbiddenName.img");
    for (MapleData data : nameData.getChildren())
      this.ForbiddenName.add(MapleDataTool.getString(data)); 
    nameData = prov.getData("Curse.img");
    for (MapleData data : nameData.getChildren())
      this.ForbiddenName.add(MapleDataTool.getString(data).split(",")[0]); 
    MapleData infoData = prov.getData("MakeCharInfo.img");
    for (MapleData dat : infoData) {
      try {
        int type;
        if (dat.getName().equals("000_1")) {
          type = JobType.DualBlade.type;
        } else if (dat.getName().equals("000_3")) {
          type = JobType.pathFinder.type;
        } else if (dat.getName().equals("10112")) {
          type = JobType.Zero.type;
        } else {
          type = (JobType.getById(Integer.parseInt(dat.getName()))).type;
        } 
        for (MapleData d : dat) {
          int val;
          if (d.getName().contains("female")) {
            val = 1;
          } else if (d.getName().contains("male")) {
            val = 0;
          } else {
            continue;
          } 
          for (MapleData da : d) {
            int index = Integer.parseInt(da.getName());
            Triple<Integer, Integer, Integer> key = new Triple<>(Integer.valueOf(val), Integer.valueOf(index), Integer.valueOf(type));
            List<Integer> our = this.makeCharInfo.get(key);
            if (our == null) {
              our = new ArrayList<>();
              this.makeCharInfo.put(key, our);
            } 
            for (MapleData dd : da) {
              if (dd.getName().equalsIgnoreCase("color")) {
                for (MapleData dda : dd) {
                  for (MapleData ddd : dda)
                    our.add(Integer.valueOf(MapleDataTool.getInt(ddd, -1))); 
                } 
                continue;
              } 
              try {
                our.add(Integer.valueOf(MapleDataTool.getInt(dd, -1)));
              } catch (Exception ex) {
                for (MapleData dda : dd) {
                  for (MapleData ddd : dda)
                    our.add(Integer.valueOf(MapleDataTool.getInt(ddd, -1))); 
                } 
              } 
            } 
          } 
        } 
      } catch (NumberFormatException|NullPointerException numberFormatException) {}
    } 
    MapleData uA = infoData.getChildByPath("UltimateAdventurer");
    for (MapleData dat : uA) {
      Triple<Integer, Integer, Integer> key = new Triple<>(Integer.valueOf(-1), Integer.valueOf(Integer.parseInt(dat.getName())), Integer.valueOf(JobType.UltimateAdventurer.type));
      List<Integer> our = this.makeCharInfo.get(key);
      if (our == null) {
        our = new ArrayList<>();
        this.makeCharInfo.put(key, our);
      } 
      for (MapleData d : dat)
        our.add(Integer.valueOf(MapleDataTool.getInt(d, -1))); 
    } 
  }
  
  public static boolean isExtendedSpJob(int jobId) {
    return GameConstants.isSeparatedSp((short)jobId);
  }
  
  public final boolean isForbiddenName(String in) {
    for (String name : this.ForbiddenName) {
      if (in.toLowerCase().contains(name.toLowerCase()))
        return true; 
    } 
    return false;
  }
  
  public final boolean isEligibleItem(int gender, int val, int job, int item) {
    if (item < 0) {
      System.out.println(String.format("item is <0, isEligibleItem(%d, %d, %d, %d)", new Object[] { Integer.valueOf(gender), Integer.valueOf(val), Integer.valueOf(job), Integer.valueOf(item) }));
      return false;
    } 
    Triple<Integer, Integer, Integer> key = new Triple<>(Integer.valueOf(gender), Integer.valueOf(val), Integer.valueOf(job));
    List<Integer> our = this.makeCharInfo.get(key);
    if (our == null) {
      System.out.println(String.format("isEligibleItem(%d, %d, %d, %d)", new Object[] { Integer.valueOf(gender), Integer.valueOf(val), Integer.valueOf(job), Integer.valueOf(item) }));
      return false;
    } 
    return our.contains(Integer.valueOf(item));
  }
}
