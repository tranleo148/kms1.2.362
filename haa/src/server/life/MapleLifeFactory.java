package server.life;

import constants.GameConstants;
import constants.ServerConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import provider.MapleDataType;
import server.Randomizer;
import tools.Pair;
import tools.StringUtil;

public class MapleLifeFactory {
  private static final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/Mob.wz"));
  
  private static final MapleDataProvider npcData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/Npc.wz"));
  
  private static final MapleDataProvider stringDataWZ = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/String.wz"));
  
  private static final MapleDataProvider etcDataWZ = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/Etc.wz"));
  
  private static final MapleData mobStringData = stringDataWZ.getData("Mob.img");
  
  private static final MapleData npcStringData = stringDataWZ.getData("Npc.img");
  
  private static final MapleData npclocData = etcDataWZ.getData("NpcLocation.img");
  
  private static Map<Integer, String> npcNames = new HashMap<>();
  
  private static Map<Integer, String> npcScripts = new HashMap<>();
  
  private static Map<Integer, MapleMonsterStats> monsterStats = new HashMap<>();
  
  private static Map<Integer, Integer> NPCLoc = new HashMap<>();
  
  private static Map<Integer, List<Integer>> questCount = new HashMap<>();
  
  public static AbstractLoadedMapleLife getLife(int id, String type) {
    if (type.equalsIgnoreCase("n"))
      return getNPC(id); 
    if (type.equalsIgnoreCase("m"))
      return getMonster(id); 
    System.err.println("Unknown Life type: " + type + "");
    return null;
  }
  
  public static int getNPCLocation(int npcid) {
    if (NPCLoc.containsKey(Integer.valueOf(npcid)))
      return ((Integer)NPCLoc.get(Integer.valueOf(npcid))).intValue(); 
    int map = MapleDataTool.getIntConvert(Integer.toString(npcid) + "/0", npclocData, -1);
    NPCLoc.put(Integer.valueOf(npcid), Integer.valueOf(map));
    return map;
  }
  
  public static final void loadQuestCounts() {
    if (questCount.size() > 0)
      return; 
    for (MapleDataDirectoryEntry mapz : data.getRoot().getSubdirectories()) {
      if (mapz.getName().equals("QuestCountGroup"))
        for (MapleDataFileEntry entry : mapz.getFiles()) {
          int id = Integer.parseInt(entry.getName().substring(0, entry.getName().length() - 4));
          MapleData dat = data.getData("QuestCountGroup/" + entry.getName());
          if (dat != null && dat.getChildByPath("info") != null) {
            List<Integer> z = new ArrayList<>();
            for (MapleData da : dat.getChildByPath("info"))
              z.add(Integer.valueOf(MapleDataTool.getInt(da, 0))); 
            questCount.put(Integer.valueOf(id), z);
            continue;
          } 
          System.out.println("null questcountgroup");
        }  
    } 
    for (MapleData c : npcStringData) {
      int nid;
      try {
        nid = Integer.parseInt(c.getName());
      } catch (Exception e) {
        continue;
      } 
      String n = StringUtil.getLeftPaddedStr(nid + ".img", '0', 11);
      try {
        if (npcData.getData(n) != null) {
          String name = MapleDataTool.getString("name", c, "MISSINGNO");
          if (name.contains("Maple TV") || name.contains("Baby Moon Bunny"))
            continue; 
          npcNames.put(Integer.valueOf(nid), name);
        } 
      } catch (NullPointerException nullPointerException) {
      
      } catch (RuntimeException runtimeException) {}
    } 
  }
  
  public static final void loadNpcScripts() {
    for (MapleData c : npcStringData) {
      int nid;
      try {
        nid = Integer.parseInt(c.getName());
      } catch (Exception e) {
        continue;
      } 
      String n = StringUtil.getLeftPaddedStr(nid + ".img", '0', 11);
      try {
        if (npcData.getData(n) != null)
          for (MapleData d : npcData.getData(n)) {
            if (d.getName().equals("info"))
              for (MapleData e : d) {
                if (e.getName().equals("script"))
                  for (MapleData f : e) {
                    if (e.getName().equals("script"))
                      for (MapleData scripts : f) {
                        if (scripts.getType() != MapleDataType.STRING);
                        npcScripts.put(Integer.valueOf(nid), (String)scripts.getData());
                      }  
                  }  
              }  
          }  
      } catch (NullPointerException e) {
        System.out.println(c.getName());
        e.printStackTrace();
      } catch (RuntimeException runtimeException) {}
    } 
    npcScripts.put(Integer.valueOf(9000216), "mannequin_manage");
  }
  
  public static final List<Integer> getQuestCount(int id) {
    return questCount.get(Integer.valueOf(id));
  }
  
  public static MapleMonster getMonster(int mid) {
    MapleMonsterStats stats = getMonsterStats(mid);
    if (stats == null)
      return null; 
    return new MapleMonster(mid, stats);
  }
  
  public static MapleMonster getMonster(int mid, boolean extreme) {
    MapleMonsterStats stats = getMonsterStats(mid);
    if (stats == null)
      return null; 
    return new MapleMonster(mid, stats, extreme);
  }
  
  public static MapleMonsterStats getMonsterStats(int mid) {
    MapleMonsterStats stats = monsterStats.get(Integer.valueOf(mid));
    if (stats != null && 
      stats.getHp() <= 0L) {
      monsterStats.remove(Integer.valueOf(mid));
      stats = null;
    } 
    if (stats == null) {
      MapleData monsterData = null;
      try {
        monsterData = data.getData(StringUtil.getLeftPaddedStr(Integer.toString(mid) + ".img", '0', 11));
      } catch (RuntimeException e) {
        return null;
      } 
      if (monsterData == null)
        return null; 
      MapleData monsterInfoData = monsterData.getChildByPath("info");
      stats = new MapleMonsterStats(mid);
      long maxpHP = 0L;
      if (mid == 8645123)
        System.out.println("체크"); 
      try {
        if (monsterInfoData.getChildByPath("maxHP").getType() == MapleDataType.INT) {
          maxpHP = MapleDataTool.getIntConvert("maxHP", monsterInfoData);
        } else if (monsterInfoData.getChildByPath("maxHP").getType() == MapleDataType.LONG) {
          maxpHP = MapleDataTool.getLongConvert("maxHP", monsterInfoData, 0);
        } 
      } catch (Exception exception) {}
      List<Pair<Integer, Long>> list = ServerConstants.boss;
      boolean boss = false;
      for (int i = 0; i < list.size(); i++) {
        if (mid == ((Integer)((Pair)list.get(i)).getLeft()).intValue()) {
          long hp = ((Long)((Pair)list.get(i)).getRight()).longValue();
          stats = new MapleMonsterStats(((Integer)((Pair)list.get(i)).getLeft()).intValue());
          stats.setHp(hp);
          boss = true;
        } 
      } 
      List<Pair<Integer, Long>> list2 = ServerConstants.boss2;
      for (int j = 0; j < list2.size(); j++) {
        if (mid == ((Integer)((Pair)list2.get(j)).getLeft()).intValue()) {
          long hp = ((Long)((Pair)list2.get(j)).getRight()).longValue();
          stats = new MapleMonsterStats(((Integer)((Pair)list2.get(j)).getLeft()).intValue());
          stats.setHp(hp);
          boss = true;
        } 
      } 
      if (!boss)
        switch (mid) {
          case 8880100:
            stats.setHp(24850000000000L);
            break;
          case 8880101:
            stats.setHp(10650000000000L);
            break;
          case 8880110:
            stats.setHp(840000000000L);
            break;
          case 8880111:
            stats.setHp(360000000000L);
            break;
          case 8880141:
            stats.setHp(54000000000000L);
            break;
          case 8880140:
            stats.setHp(12000000000000L);
            break;
          case 8880151:
            stats.setHp(54000000000000L);
            break;
          case 8880150:
            stats.setHp(12000000000000L);
            break;
          case 8880153:
            stats.setHp(15750000000000L);
            break;
          case 8950000:
            stats.setHp(1400000000000L);
            break;
          case 8950001:
            stats.setHp(7000000000000L);
            break;
          case 8950002:
            stats.setHp(24125000000000L);
            break;
          case 8950100:
            stats.setHp(400000000000L);
            break;
          case 8950101:
            stats.setHp(400000000000L);
            break;
          case 8950102:
            stats.setHp(700000000000L);
            break;
          case 8880181:
          case 8880183:
          case 8880184:
          case 8880185:
            stats.setHp(600000000000L);
            break;
          case 8880190:
            stats.setHp(13500000000L);
            break;
          case 8880191:
            stats.setHp(6500000000L);
            break;
          case 8880340:
          case 8880343:
          case 8880344:
            stats.setHp(8400000000000L);
            break;
          case 8880341:
            stats.setHp(6300000000000L);
            break;
          case 8880342:
            stats.setHp(10500000000000L);
            break;
          case 8880300:
          case 8880303:
          case 8880304:
            stats.setHp(42000000000000L);
            break;
          case 8880301:
            stats.setHp(31500000000000L);
            break;
          case 8880302:
            stats.setHp(52500000000000L);
            break;
          case 8880500:
          case 8880501:
            stats.setHp(65000000000000L);
            break;
          case 8880502:
            stats.setHp(135000000000000L);
            break;
          case 8880503:
            stats.setHp(200000000000000L);
            break;
          case 8880504:
            stats.setHp(100000000000000L);
            break;
          case 8880505:
            stats.setHp(5000000000L);
            break;
          case 8220110:
            stats.setHp(999625223624000L);
            break;
          case 8881000:
            stats.setHp(2625223624000L);
            break;
          case 8644611:
            stats.setHp(21000000000L);
            break;
          case 8644612:
          case 8644650:
          case 8645009:
            stats.setHp(26000000000000L);
            break;
          case 8880405:
            stats.setHp(176000000000000L);
            break;
          case 8880408:
            stats.setHp(15000000000000L);
            break;
          case 8880409:
            stats.setHp(15000000000000L);
            break;
          case 9500650:
            stats.setHp(648000000000L);
            break;
          case 9500651:
            stats.setHp(1620000000000L);
            break;
          case 9500652:
            stats.setHp(162000000000000L);
            break;
          case 9500653:
            stats.setHp(1134000000000000L);
            break;
          case 9101078:
          case 9500654:
          case 9833885:
            stats.setHp(2982480000L);
            break;
          case 9833886:
            stats.setHp(6957192000L);
            break;
          case 9833887:
            stats.setHp(11784960000L);
            break;
          case 9833888:
            stats.setHp(29322734000L);
            break;
          case 9833889:
            stats.setHp(100445184000L);
            break;
          case 9833890:
            stats.setHp(171057744000L);
            break;
          case 9833891:
            stats.setHp(1197404208000L);
            break;
          case 9833892:
            stats.setHp(59870210400000L);
            break;
          case 9833898:
            stats.setHp(3162360000L);
            break;
          case 9833899:
            stats.setHp(4910400000L);
            break;
          case 9833900:
            stats.setHp(11277974400L);
            break;
          case 9833901:
            stats.setHp(23915520000L);
            break;
          case 9833902:
            stats.setHp(38012832000L);
            break;
          case 9833903:
            stats.setHp(266089824000L);
            break;
          case 9833904:
            stats.setHp(13304491200000L);
            break;
          case 9833376:
            stats.setHp(133044912000000000L);
            break;
          case 8120105: //중급피로도
                    stats.setHp(3216291020L);
                    break;
          default:
            stats.setHp((GameConstants.getPartyPlayHP(mid) > 0) ? GameConstants.getPartyPlayHP(mid) : ((monsterInfoData.getChildByPath("finalmaxHP") != null) ? (maxpHP + MapleDataTool.getLongConvert("finalmaxHP", monsterInfoData, 0)) : maxpHP));
            break;
        }  
      stats.setMp(MapleDataTool.getIntConvert("maxMP", monsterInfoData, 0));
      stats.setExp((mid == 9300027) ? 0L : ((GameConstants.getPartyPlayEXP(mid) > 0) ? GameConstants.getPartyPlayEXP(mid) : MapleDataTool.getIntConvert("exp", monsterInfoData, 0)));
      stats.setLevel((short)MapleDataTool.getIntConvert("level", monsterInfoData, 1));
      stats.setCharismaEXP((short)MapleDataTool.getIntConvert("charismaEXP", monsterInfoData, 0));
      stats.setRemoveAfter(MapleDataTool.getIntConvert("removeAfter", monsterInfoData, 0));
      stats.setrareItemDropLevel((byte)MapleDataTool.getIntConvert("rareItemDropLevel", monsterInfoData, 0));
      stats.setFixedDamage(MapleDataTool.getIntConvert("fixedDamage", monsterInfoData, -1));
      stats.setOnlyNormalAttack((MapleDataTool.getIntConvert("onlyNormalAttack", monsterInfoData, 0) > 0));
      stats.setBoss((GameConstants.getPartyPlayHP(mid) > 0 || MapleDataTool.getIntConvert("boss", monsterInfoData, 0) > 0 || mid == 8810018 || mid == 9410066 || (mid >= 8810118 && mid <= 8810122)));
      stats.setNotSeperateSoul((MapleDataTool.getIntConvert("notSeperateSoul", monsterInfoData, 0) > 0));
      stats.setExplosiveReward((MapleDataTool.getIntConvert("explosiveReward", monsterInfoData, 0) > 0));
      stats.setUndead((MapleDataTool.getIntConvert("undead", monsterInfoData, 0) > 0));
      stats.setEscort((MapleDataTool.getIntConvert("escort", monsterInfoData, 0) > 0));
      stats.setPartyBonus((GameConstants.getPartyPlayHP(mid) > 0 || MapleDataTool.getIntConvert("partyBonusMob", monsterInfoData, 0) > 0));
      stats.setPartyBonusRate(MapleDataTool.getIntConvert("partyBonusR", monsterInfoData, 0));
      if (mobStringData.getChildByPath(String.valueOf(mid)) != null)
        stats.setName(MapleDataTool.getString("name", mobStringData.getChildByPath(String.valueOf(mid)), "MISSINGNO")); 
      stats.setBuffToGive(MapleDataTool.getIntConvert("buff", monsterInfoData, -1));
      stats.setChange((MapleDataTool.getIntConvert("changeableMob", monsterInfoData, 0) > 0));
      stats.setFriendly((MapleDataTool.getIntConvert("damagedByMob", monsterInfoData, 0) > 0));
      stats.setNoDoom((MapleDataTool.getIntConvert("noDoom", monsterInfoData, 0) > 0));
      stats.setCP((byte)MapleDataTool.getIntConvert("getCP", monsterInfoData, 0));
      stats.setPoint(MapleDataTool.getIntConvert("point", monsterInfoData, 0));
      stats.setDropItemPeriod(MapleDataTool.getIntConvert("dropItemPeriod", monsterInfoData, 0));
      stats.setPhysicalAttack(MapleDataTool.getIntConvert("PADamage", monsterInfoData, 0));
      stats.setMagicAttack(MapleDataTool.getIntConvert("MADamage", monsterInfoData, 0));
      stats.setPDRate(MapleDataTool.getIntConvert("PDRate", monsterInfoData, 0));
      stats.setMDRate(MapleDataTool.getIntConvert("MDRate", monsterInfoData, 0));
      stats.setAcc(MapleDataTool.getIntConvert("acc", monsterInfoData, 0));
      stats.setEva(MapleDataTool.getIntConvert("eva", monsterInfoData, 0));
      stats.setSummonType((byte)MapleDataTool.getIntConvert("summonType", monsterInfoData, 0));
      stats.setHpLinkMob(MapleDataTool.getIntConvert("HpLinkMob", monsterInfoData, 0));
      if (mid == 8880512)
        stats.setSummonType((byte)1); 
      stats.setCategory((byte)MapleDataTool.getIntConvert("category", monsterInfoData, 0));
      stats.setSpeed(MapleDataTool.getIntConvert("speed", monsterInfoData, 0));
      stats.setPushed(MapleDataTool.getIntConvert("pushed", monsterInfoData, 0));
      stats.setPublicReward((MapleDataTool.getIntConvert("publicReward", monsterInfoData, 0) > 0 || MapleDataTool.getIntConvert("individualReward", monsterInfoData, 0) > 0));
      stats.setIgnoreMovable(MapleDataTool.getIntConvert("ignoreMovable", monsterInfoData, 0));
      stats.setIgnoreMoveableMsg(MapleDataTool.getString("ignoreMoveableMsg", monsterInfoData, ""));
      boolean hideHP = (MapleDataTool.getIntConvert("HPgaugeHide", monsterInfoData, 0) > 0 || MapleDataTool.getIntConvert("hideHP", monsterInfoData, 0) > 0);
      MapleData selfd = monsterInfoData.getChildByPath("selfDestruction");
      if (selfd != null) {
        stats.setSelfDHP(MapleDataTool.getIntConvert("hp", selfd, 0));
        stats.setRemoveAfter(MapleDataTool.getIntConvert("removeAfter", selfd, stats.getRemoveAfter()));
        stats.setSelfD((byte)MapleDataTool.getIntConvert("action", selfd, -1));
      } else {
        stats.setSelfD((byte)-1);
      } 
      MapleData firstAttackData = monsterInfoData.getChildByPath("firstAttack");
      if (firstAttackData != null)
        if (firstAttackData.getType() == MapleDataType.FLOAT) {
          stats.setFirstAttack((Math.round(MapleDataTool.getFloat(firstAttackData)) > 0));
        } else {
          stats.setFirstAttack((MapleDataTool.getInt(firstAttackData) > 0));
        }  
      if (stats.isBoss() || isDmgSponge(mid))
        if (monsterInfoData.getChildByPath("hpTagColor") == null || monsterInfoData.getChildByPath("hpTagBgcolor") == null) {
          stats.setTagColor(0);
          stats.setTagBgColor(0);
        } else {
          stats.setTagColor(MapleDataTool.getIntConvert("hpTagColor", monsterInfoData));
          stats.setTagBgColor(MapleDataTool.getIntConvert("hpTagBgcolor", monsterInfoData));
        }  
      MapleData banishData = monsterInfoData.getChildByPath("ban");
      if (banishData != null)
        stats.setBanishInfo(new BanishInfo(
              MapleDataTool.getString("banMsg", banishData), 
              MapleDataTool.getInt("banMap/0/field", banishData, -1), 
              MapleDataTool.getString("banMap/0/portal", banishData, "sp"))); 
      if (mid == 8860000 || mid == 8860001 || mid == 8860005 || mid == 8860007)
        stats.setBanishInfo(new BanishInfo("자신 속의 추악한 내면을 마주한 기분이 어떠신지요?", 272020300, "0")); 
      MapleData reviveInfo = monsterInfoData.getChildByPath("revive");
      if (reviveInfo != null) {
        List<Integer> revives = new LinkedList<>();
        for (MapleData bdata : reviveInfo)
          revives.add(Integer.valueOf(MapleDataTool.getInt(bdata))); 
        stats.setRevives(revives);
      } 
      MapleData skeletonData = monsterData.getChildByPath("HitParts");
      if (skeletonData != null)
        for (MapleData skeleton : skeletonData) {
          int durability = Integer.valueOf(MapleDataTool.getInt("0/stat/durability", skeleton, 0)).intValue();
          stats.addSkeleton(skeleton.getName(), Integer.valueOf(0), Integer.valueOf(durability));
        }  
      MapleData trans = monsterInfoData.getChildByPath("trans");
      if (trans != null) {
        Transform transform = new Transform(MapleDataTool.getInt("0", trans, 0), MapleDataTool.getInt("1", trans, 0), MapleDataTool.getInt("cooltime", trans, 0), MapleDataTool.getInt("hpTriggerOff", trans, 0), MapleDataTool.getInt("hpTriggerOn", trans, 0), MapleDataTool.getInt("time", trans, 0), MapleDataTool.getInt("withMob", trans, 0));
        List<Pair<Integer, Integer>> skills = new ArrayList<>();
        MapleData transSkills = trans.getChildByPath("skill");
        if (transSkills != null)
          for (MapleData transSkill : transSkills.getChildren())
            skills.add(new Pair<>(Integer.valueOf(MapleDataTool.getInt("skill", transSkill, 0)), Integer.valueOf(MapleDataTool.getInt("level", transSkill, 0))));  
        transform.setSkills(skills);
        stats.setTrans(transform);
      } 
      MapleData monsterSkillData = monsterInfoData.getChildByPath("skill");
      if (monsterSkillData != null) {
        int k = 0;
        List<MobSkill> skills = new ArrayList<>();
        while (monsterSkillData.getChildByPath(Integer.toString(k)) != null) {
          int onlyFsm = Integer.valueOf(MapleDataTool.getInt(k + "/onlyFsm", monsterSkillData, 0)).intValue();
          int onlyOtherSkill = Integer.valueOf(MapleDataTool.getInt(k + "/onlyOtherSkill", monsterSkillData, 0)).intValue();
          MobSkill ms = MobSkillFactory.getMobSkill(Integer.valueOf(MapleDataTool.getInt(k + "/skill", monsterSkillData, 0)).intValue(), Integer.valueOf(MapleDataTool.getInt(k + "/level", monsterSkillData, 0)).intValue());
          if (ms != null) {
            ms.setOnlyFsm((ms.getSkillId() == 215 && ms.getSkillLevel() == 4) ? false : ((onlyFsm > 0)));
            ms.setAction(Integer.valueOf(MapleDataTool.getInt(k + "/action", monsterSkillData, 0)));
            int skillAfter = Integer.valueOf(MapleDataTool.getInt(k + "/skillAfter", monsterSkillData, 0)).intValue();
            if (skillAfter > ms.getSkillAfter())
              ms.setSkillAfter(skillAfter); 
            ms.setOnlyOtherSkill((onlyOtherSkill > 0));
            ms.setSkillForbid(Integer.valueOf(MapleDataTool.getInt(k + "/skillForbid", monsterSkillData, 0)).intValue());
            ms.setAfterAttack(Integer.valueOf(MapleDataTool.getInt(k + "/afterAttack", monsterSkillData, -1)).intValue());
            ms.setAfterAttackCount(Integer.valueOf(MapleDataTool.getInt(k + "/afterAttackCount", monsterSkillData, 0)).intValue());
            ms.setAfterDead(Integer.valueOf(MapleDataTool.getInt(k + "/afterDead", monsterSkillData, 0)).intValue());
            skills.add(ms);
          } 
          k++;
        } 
        stats.setSkills(skills);
      } 
      decodeElementalString(stats, MapleDataTool.getString("elemAttr", monsterInfoData, ""));
      int link = MapleDataTool.getIntConvert("link", monsterInfoData, 0);
      if (link != 0)
        monsterData = data.getData(StringUtil.getLeftPaddedStr(link + ".img", '0', 11)); 
      for (MapleData idata : monsterData) {
        if (idata.getName().equals("fly")) {
          stats.setFly(true);
          stats.setMobile(true);
          break;
        } 
        if (idata.getName().equals("move"))
          stats.setMobile(true); 
      } 
      boolean mobZone = (monsterInfoData.getChildByPath("mobZone") != null);
      stats.setMobZone(mobZone);
      MapleData monsterAtt = monsterInfoData.getChildByPath("attack");
      if (monsterAtt != null) {
        int k = 0;
        List<MobAttack> attacks = new ArrayList<>();
        while (monsterAtt.getChildByPath(Integer.toString(k)) != null) {
          MobAttack attack = new MobAttack(MapleDataTool.getInt(k + "/action", monsterAtt, -1), MapleDataTool.getInt(k + "/afterAttack", monsterAtt, -1), MapleDataTool.getInt(k + "/fixAttack", monsterAtt, -1), MapleDataTool.getInt(k + "/onlyAfterAttack", monsterAtt, -1), MapleDataTool.getInt(k + "/cooltime", monsterAtt, -1), MapleDataTool.getInt(k + "/afterAttackCount", monsterAtt, -1));
          if (monsterAtt.getChildByPath(Integer.toString(k) + "/callSkill") != null) {
            MapleData callSkillData = monsterAtt.getChildByPath(Integer.toString(k) + "/callSkill");
            int m = 0;
            while (callSkillData.getChildByPath(String.valueOf(m)) != null) {
              MapleData callSkillIdxData = callSkillData.getChildByPath(String.valueOf(m));
              attack.addSkill(MapleDataTool.getInt("skill", callSkillIdxData, 0), MapleDataTool.getInt("level", callSkillIdxData, 0), MapleDataTool.getInt("delay", callSkillIdxData, 0));
              m++;
            } 
          } 
          if (monsterAtt.getChildByPath(Integer.toString(k) + "/callSkillWithData") != null) {
            MapleData callSkillData = monsterAtt.getChildByPath(Integer.toString(k) + "/callSkillWithData");
            attack.addSkill(MapleDataTool.getInt("skill", callSkillData, 0), MapleDataTool.getInt("level", callSkillData, 0), MapleDataTool.getInt("delay", callSkillData, 0));
          } 
          attacks.add(attack);
          k++;
        } 
        stats.setAttacks(attacks);
      } 
      byte hpdisplaytype = -1;
      if (stats.getTagColor() > 0) {
        hpdisplaytype = 0;
      } else if (stats.isFriendly()) {
        hpdisplaytype = 1;
      } else if (mid >= 9300184 && mid <= 9300215) {
        hpdisplaytype = 2;
      } else if (!stats.isBoss() || mid == 9410066 || stats.isPartyBonus()) {
        hpdisplaytype = 3;
      } 
      stats.setHPDisplayType(hpdisplaytype);
      monsterStats.put(Integer.valueOf(mid), stats);
    } 
    return stats;
  }
  
  public static final void decodeElementalString(MapleMonsterStats stats, String elemAttr) {
    for (int i = 0; i < elemAttr.length(); i += 2)
      stats.setEffectiveness(
          Element.getFromChar(elemAttr.charAt(i)), 
          ElementalEffectiveness.getByNumber(Integer.valueOf(String.valueOf(elemAttr.charAt(i + 1))).intValue())); 
  }
  
  private static final boolean isDmgSponge(int mid) {
    switch (mid) {
      case 8810018:
      case 8810119:
      case 8810120:
      case 8810121:
      case 8810122:
      case 8820009:
      case 8820010:
      case 8820011:
      case 8820012:
      case 8820013:
      case 8820014:
      case 8820110:
      case 8820111:
      case 8820112:
      case 8820113:
      case 8820114:
        return true;
    } 
    return false;
  }
  
  public static MapleNPC getNPC(int nid) {
    String name = npcNames.get(Integer.valueOf(nid));
    if (name == null)
      return null; 
    return new MapleNPC(nid, name);
  }
  
  public static int getRandomNPC() {
    List<Integer> vals = new ArrayList<>(npcNames.keySet());
    int ret = 0;
    while (ret <= 0) {
      ret = ((Integer)vals.get(Randomizer.nextInt(vals.size()))).intValue();
      if (((String)npcNames.get(Integer.valueOf(ret))).contains("MISSINGNO"))
        ret = 0; 
    } 
    return ret;
  }
  
  public static Map<Integer, String> getNpcScripts() {
    return npcScripts;
  }
  
  public static void setNpcScripts(Map<Integer, String> npcScripts) {
    MapleLifeFactory.npcScripts = npcScripts;
  }
  
  public static Map<Integer, MapleMonsterStats> getMonsterStats() {
    return monsterStats;
  }
}
