package server.maps;

import constants.GameConstants;
import database.DatabaseConnection;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MaplePortal;
import server.Randomizer;
import server.life.AbstractLoadedMapleLife;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.life.Spawns;
import tools.Pair;
import tools.StringUtil;

public class MapleMapFactory {
  private final MapleDataProvider source = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/Map.wz"));
  
  private final MapleData nameData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wz") + "/String.wz")).getData("Map.img");
  
  private final HashMap<Integer, MapleMap> maps = new HashMap<>();
  
  private final HashMap<Integer, MapleMap> instanceMap = new HashMap<>();
  
  private final ReentrantLock lock = new ReentrantLock();
  
  private int channel;
  
  private int barrierArc;
  
  private int barrierAut;
  
  public MapleMapFactory(int channel) {
    this.channel = channel;
  }
  
  public final MapleMap getMap(int mapid) {
    MapleMap asdf = null;
    try {
      asdf = getMap(mapid, true, true, true);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
    return asdf;
  }
  
  public final MapleMap getMap(int mapid, boolean respawns, boolean npcs) {
    MapleMap asdf = null;
    try {
      asdf = getMap(mapid, respawns, npcs, true);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
    return asdf;
  }
  
  public final MapleMap getMap(int mapid, boolean respawns, boolean npcs, boolean reactors) {
    Integer omapid = Integer.valueOf(mapid);
    MapleMap map = this.maps.get(omapid);
    if (map == null) {
      this.lock.lock();
      try {
        map = this.maps.get(omapid);
        if (map != null)
          return map; 
        MapleData mapData = null;
        try {
          mapData = this.source.getData(getMapName(mapid));
        } catch (Exception e) {
          return null;
        } 
        if (mapData == null)
          return null; 
        MapleData link = mapData.getChildByPath("info/link");
        if (link != null)
          mapData = this.source.getData(getMapName(MapleDataTool.getIntConvert("info/link", mapData))); 
        float monsterRate = 0.0F;
        if (respawns) {
          MapleData mobRate = mapData.getChildByPath("info/mobRate");
          if (mobRate != null)
            monsterRate = ((Float)mobRate.getData()).floatValue(); 
        } 
        int asdf = MapleDataTool.getInt("info/returnMap", mapData);
        map = new MapleMap(mapid, this.channel, asdf, monsterRate);
        loadPortals(map, mapData.getChildByPath("portal"));
        map.setTop(MapleDataTool.getInt(mapData.getChildByPath("info/VRTop"), 0));
        map.setLeft(MapleDataTool.getInt(mapData.getChildByPath("info/VRLeft"), 0));
        map.setBottom(MapleDataTool.getInt(mapData.getChildByPath("info/VRBottom"), 0));
        map.setRight(MapleDataTool.getInt(mapData.getChildByPath("info/VRRight"), 0));
        map.setBarrierArc(MapleDataTool.getInt(mapData.getChildByPath("info/barrierArc"), 0));
        map.setBarrierAut(MapleDataTool.getInt(mapData.getChildByPath("info/barrierAut"), 0));
        List<MapleFoothold> allFootholds = new LinkedList<>();
        Point lBound = new Point();
        Point uBound = new Point();
        for (MapleData footRoot : mapData.getChildByPath("foothold")) {
          for (MapleData footCat : footRoot) {
            for (MapleData footHold : footCat) {
              MapleFoothold fh = new MapleFoothold(new Point(MapleDataTool.getInt(footHold.getChildByPath("x1"), 0), MapleDataTool.getInt(footHold.getChildByPath("y1"), 0)), new Point(MapleDataTool.getInt(footHold.getChildByPath("x2"), 0), MapleDataTool.getInt(footHold.getChildByPath("y2"), 0)), Integer.parseInt(footHold.getName()));
              fh.setPrev((short)MapleDataTool.getInt(footHold.getChildByPath("prev"), 0));
              fh.setNext((short)MapleDataTool.getInt(footHold.getChildByPath("next"), 0));
              if (fh.getX1() < lBound.x)
                lBound.x = fh.getX1(); 
              if (fh.getX2() > uBound.x)
                uBound.x = fh.getX2(); 
              if (fh.getY1() < lBound.y)
                lBound.y = fh.getY1(); 
              if (fh.getY2() > uBound.y)
                uBound.y = fh.getY2(); 
              allFootholds.add(fh);
            } 
          } 
        } 
        MapleFootholdTree fTree = new MapleFootholdTree(lBound, uBound);
        for (MapleFoothold foothold : allFootholds)
          fTree.insert(foothold); 
        map.setFootholds(fTree);
        if (map.getTop() == 0)
          map.setTop(lBound.y); 
        if (map.getBottom() == 0)
          map.setBottom(uBound.y); 
        if (map.getLeft() == 0)
          map.setLeft(lBound.x); 
        if (map.getRight() == 0)
          map.setRight(uBound.x); 
        int bossid = -1;
        String msg = null;
        if (mapData.getChildByPath("info/timeMob") != null) {
          bossid = MapleDataTool.getInt(mapData.getChildByPath("info/timeMob/id"), 0);
          msg = MapleDataTool.getString(mapData.getChildByPath("info/timeMob/message"), (String)null);
        } 
        List<Point> herbRocks = new ArrayList<>();
        int lowestLevel = 200, highestLevel = 0;
        for (MapleData life : mapData.getChildByPath("life")) {
          String type = MapleDataTool.getString(life.getChildByPath("type"));
          String limited = MapleDataTool.getString("limitedname", life, "");
          if ((npcs || !type.equals("n")) && !limited.equals("Stage0")) {
            AbstractLoadedMapleLife myLife = loadLife(life, MapleDataTool.getString(life.getChildByPath("id")), type);
            if (myLife instanceof MapleMonster && !GameConstants.isNoSpawn(mapid)) {
              MapleMonster mob = (MapleMonster)myLife;
              herbRocks.add(map.addMonsterSpawn(mob, 
                    MapleDataTool.getInt("mobTime", life, 0), 
                    (byte)MapleDataTool.getInt("team", life, -1), 
                    (mob.getId() == bossid) ? msg : null).getPosition());
              if (mob.getStats().getLevel() > highestLevel && !mob.getStats().isBoss())
                highestLevel = mob.getStats().getLevel(); 
              if (mob.getStats().getLevel() < lowestLevel && !mob.getStats().isBoss())
                lowestLevel = mob.getStats().getLevel(); 
              continue;
            } 
            if (myLife instanceof MapleNPC)
              map.addMapObject(myLife); 
          } 
        } 
        addAreaBossSpawn(map);
        map.setCreateMobInterval((short)MapleDataTool.getInt(mapData.getChildByPath("info/createMobInterval"), 9000));
        map.setFixedMob(MapleDataTool.getInt(mapData.getChildByPath("info/fixedMobCapacity"), 0));
        map.setPartyBonusRate(GameConstants.getPartyPlay(mapid, MapleDataTool.getInt(mapData.getChildByPath("info/partyBonusR"), 0)));
        map.loadMonsterRate(true);
        map.setNodes(loadNodes(mapid, mapData));
        List<MapleMonster> monsters = map.getAllMonster();
        if (!map.isTown() && map.getAllMonster().size() > 0 && map.isSpawnPoint() && !GameConstants.보스맵(map.getId()) && !GameConstants.사냥컨텐츠맵(map.getId()) && !GameConstants.튜토리얼(map.getId()) && !GameConstants.로미오줄리엣(map.getId()) && !GameConstants.피라미드(map.getId()) && !GameConstants.isContentsMap(map.getId())) {
          int spawnpoint = Randomizer.rand(0, map.monsterSpawn.size() - 1);
          Point poss = ((Spawns)map.monsterSpawn.get(spawnpoint)).getPosition();
          MapleRune rune = new MapleRune(Randomizer.rand(0, 10), poss.x, poss.y, map);
          rune.setSpawnPointNum(spawnpoint);
          map.spawnRune(rune);
        } 
        if (reactors && mapData.getChildByPath("reactor") != null)
          for (MapleData reactor : mapData.getChildByPath("reactor")) {
            String id = MapleDataTool.getString(reactor.getChildByPath("id"));
            if (id != null)
              map.spawnReactor(loadReactor(reactor, id, (byte)MapleDataTool.getInt(reactor.getChildByPath("f"), 0))); 
          }  
        map.setFirstUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onFirstUserEnter"), ""));
        map.setUserEnter((mapid == 180000002) ? "jail" : MapleDataTool.getString(mapData.getChildByPath("info/onUserEnter"), ""));
        if (reactors && map.monsterSpawn.size() > 0 && map.isSpawnPoint() && !GameConstants.보스맵(map.getId()) && !GameConstants.사냥컨텐츠맵(map.getId()) && !GameConstants.튜토리얼(map.getId()) && !GameConstants.로미오줄리엣(map.getId()) && !GameConstants.피라미드(map.getId()) && !GameConstants.isContentsMap(map.getId())) {
          List<Integer> allowedSpawn = new ArrayList<>(24);
          allowedSpawn.add(Integer.valueOf(100011));
          allowedSpawn.add(Integer.valueOf(100012));
          allowedSpawn.add(Integer.valueOf(100013));
          allowedSpawn.add(Integer.valueOf(200011));
          allowedSpawn.add(Integer.valueOf(200012));
          allowedSpawn.add(Integer.valueOf(200013));
          for (int i = 0; i < 1 && !herbRocks.isEmpty(); i++) {
            int idd = ((Integer)allowedSpawn.get(Randomizer.nextInt(allowedSpawn.size()))).intValue();
            int theSpawn = Randomizer.nextInt(herbRocks.size());
            int sppoint = Randomizer.rand(0, map.monsterSpawn.size() - 1);
            MapleRune rune = map.getRune();
            if (rune != null)
              while (sppoint == rune.getSpawnPointNum())
                sppoint = Randomizer.rand(0, map.monsterSpawn.size() - 1);  
            Point poss = ((Spawns)map.monsterSpawn.get(sppoint)).getPosition();
            MapleReactor myReactor = new MapleReactor(MapleReactorFactory.getReactor(idd), idd);
            myReactor.setPosition(poss);
            myReactor.setDelay(Randomizer.rand(7200000, 10000000));
            myReactor.setSpawnPointNum(sppoint);
            map.spawnReactor(myReactor);
            map.destroyReactor(myReactor.getReactorId());
            herbRocks.remove(theSpawn);
          } 
        } 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
          con = DatabaseConnection.getConnection();
          String sql = "SELECT * FROM `spawn` WHERE mapid = " + mapid + " AND type = 'r'";
          if (con != null) {
            ps = con.prepareStatement(sql);
            if (ps != null) {
              rs = ps.executeQuery();
              while (rs.next()) {
                int life = rs.getInt("lifeid");
                MapleReactor reactor = new MapleReactor(MapleReactorFactory.getReactor(life), life);
                if (reactor == null) {
                  System.out.println("리엑터 null 발생!! " + reactor.getName() + ", " + life);
                  continue;
                } 
                reactor.setDelay(rs.getInt("mobTime"));
                reactor.setPosition(new Point(rs.getInt("rx0"), rs.getInt("cy")));
                map.addMapObject(reactor);
              } 
              rs.close();
            } 
            ps.close();
          } 
          con.close();
        } catch (Exception e) {
          System.err.println("[오류] 리엑터를 DB로부터 불러오는데 오류가 발생했습니다.");
          e.printStackTrace();
        } finally {
          try {
            if (con != null)
              con.close(); 
            if (ps != null)
              ps.close(); 
            if (rs != null)
              rs.close(); 
          } catch (SQLException se) {
            se.printStackTrace();
          } 
        } 
        try {
          con = DatabaseConnection.getConnection();
          String sql = "SELECT * FROM `spawn` WHERE mapid = " + mapid + " AND type = 'n'";
          if (con != null) {
            ps = con.prepareStatement(sql);
            if (ps != null) {
              rs = ps.executeQuery();
              while (rs.next()) {
                MapleNPC npc = MapleLifeFactory.getNPC(rs.getInt("lifeid"));
                if (npc == null) {
                  System.out.println("NPC Null 발생!! " + npc + ", " + rs.getInt("lifeid"));
                  continue;
                } 
                npc.setRx0(rs.getInt("rx0"));
                npc.setRx1(rs.getInt("rx1"));
                npc.setCy(rs.getInt("cy"));
                npc.setF(rs.getInt("dir"));
                npc.setFh(rs.getInt("fh"));
                npc.setPosition(new Point(npc.getRx0(), npc.getCy()));
                map.addMapObject(npc);
              } 
              rs.close();
            } 
            ps.close();
          } 
          con.close();
        } catch (Exception e) {
          System.err.println("[오류] 엔피시를 DB로부터 불러오는데 오류가 발생했습니다.");
          e.printStackTrace();
        } finally {
          try {
            if (con != null)
              con.close(); 
            if (ps != null)
              ps.close(); 
            if (rs != null)
              rs.close(); 
          } catch (SQLException se) {
            se.printStackTrace();
          } 
        } 
        try {
          map.setMapName(MapleDataTool.getString("mapName", this.nameData.getChildByPath(getMapStringName(omapid.intValue())), ""));
          map.setStreetName(MapleDataTool.getString("streetName", this.nameData.getChildByPath(getMapStringName(omapid.intValue())), ""));
        } catch (Exception e) {
          map.setMapName("");
          map.setStreetName("");
        } 
        map.setClock((mapData.getChildByPath("clock") != null));
        map.setEverlast((MapleDataTool.getInt(mapData.getChildByPath("info/everlast"), 0) > 0));
        map.setTown((MapleDataTool.getInt(mapData.getChildByPath("info/town"), 0) > 0));
        map.setSoaring((MapleDataTool.getInt(mapData.getChildByPath("info/needSkillForFly"), 0) > 0));
        map.setPersonalShop((MapleDataTool.getInt(mapData.getChildByPath("info/personalShop"), 0) > 0));
        map.setForceMove(MapleDataTool.getInt(mapData.getChildByPath("info/lvForceMove"), 0));
        map.setHPDec(MapleDataTool.getInt(mapData.getChildByPath("info/decHP"), 0));
        map.setHPDecInterval(MapleDataTool.getInt(mapData.getChildByPath("info/decHPInterval"), 10000));
        map.setHPDecProtect(MapleDataTool.getInt(mapData.getChildByPath("info/protectItem"), 0));
        map.setForcedReturnMap((mapid == 0) ? 999999999 : MapleDataTool.getInt(mapData.getChildByPath("info/forcedReturn"), 999999999));
        map.setTimeLimit(MapleDataTool.getInt(mapData.getChildByPath("info/timeLimit"), -1));
        map.setFieldLimit(MapleDataTool.getInt(mapData.getChildByPath("info/fieldLimit"), 0));
        map.setFieldType(MapleDataTool.getString(mapData.getChildByPath("info/fieldType"), ""));
        map.setRecoveryRate(MapleDataTool.getFloat(mapData.getChildByPath("info/recovery"), 1.0F));
        map.setFixedMob(MapleDataTool.getInt(mapData.getChildByPath("info/fixedMobCapacity"), 0));
        map.setPartyBonusRate(GameConstants.getPartyPlay(mapid, MapleDataTool.getInt(mapData.getChildByPath("info/partyBonusR"), 0)));
        map.setConsumeItemCoolTime(MapleDataTool.getInt(mapData.getChildByPath("info/consumeItemCoolTime"), 0));
        if (mapData.getChildByPath("monsterDefense/wave") != null) {
          Map<Integer, List<Integer>> monster = new LinkedHashMap<>();
          MapleData wavedata = mapData.getChildByPath("monsterDefense/wave");
          int w = 0;
          for (MapleData wave : wavedata.getChildren()) {
            int i = 0;
            List<Integer> mobs = new ArrayList<>();
            for (MapleData mid : wave.getChildren()) {
              if ((Integer.parseInt(wave.getName()) != 20 && (i == 0 || i % 4 == 0)) || (Integer.parseInt(wave.getName()) == 20 && (i == 0 || i % 2 == 0))) {
                if (monster.get(Integer.valueOf(Integer.parseInt(wave.getName()))) == null)
                  monster.put(Integer.valueOf(Integer.parseInt(wave.getName())), new ArrayList<>()); 
                ((List<Integer>)monster.get(Integer.valueOf(Integer.parseInt(wave.getName())))).add(Integer.valueOf(MapleDataTool.getInt(mid.getChildByPath("mobID"), 0)));
              } 
              i++;
            } 
          } 
          map.addmonsterDefense(monster);
        } 
        this.maps.put(omapid, map);
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        this.lock.unlock();
      } 
    } 
    return map;
  }
  
  public MapleMap getInstanceMap(int instanceid) {
    return this.instanceMap.get(Integer.valueOf(instanceid));
  }
  
  public void removeInstanceMap(int instanceid) {
    this.lock.lock();
    try {
      if (isInstanceMapLoaded(instanceid)) {
        getInstanceMap(instanceid).checkStates("");
        this.instanceMap.remove(Integer.valueOf(instanceid));
      } 
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void removeMap(int instanceid) {
    this.lock.lock();
    try {
      if (isMapLoaded(instanceid)) {
        getMap(instanceid).checkStates("");
        this.maps.remove(Integer.valueOf(instanceid));
      } 
    } finally {
      this.lock.unlock();
    } 
  }
  
  public MapleMap CreateInstanceMap(int mapid, boolean respawns, boolean npcs, boolean reactors, int instanceid) {
    this.lock.lock();
    try {
      if (isInstanceMapLoaded(instanceid))
        return getInstanceMap(instanceid); 
    } finally {
      this.lock.unlock();
    } 
    MapleData mapData = null;
    try {
      mapData = this.source.getData(getMapName(mapid));
    } catch (Exception e) {
      return null;
    } 
    if (mapData == null)
      return null; 
    MapleData link = mapData.getChildByPath("info/link");
    if (link != null)
      mapData = this.source.getData(getMapName(MapleDataTool.getIntConvert("info/link", mapData))); 
    float monsterRate = 0.0F;
    if (respawns) {
      MapleData mobRate = mapData.getChildByPath("info/mobRate");
      if (mobRate != null)
        monsterRate = ((Float)mobRate.getData()).floatValue(); 
    } 
    MapleMap map = new MapleMap(mapid, this.channel, MapleDataTool.getInt("info/returnMap", mapData), monsterRate);
    loadPortals(map, mapData.getChildByPath("portal"));
    map.setTop(MapleDataTool.getInt(mapData.getChildByPath("info/VRTop"), 0));
    map.setLeft(MapleDataTool.getInt(mapData.getChildByPath("info/VRLeft"), 0));
    map.setBottom(MapleDataTool.getInt(mapData.getChildByPath("info/VRBottom"), 0));
    map.setRight(MapleDataTool.getInt(mapData.getChildByPath("info/VRRight"), 0));
    List<MapleFoothold> allFootholds = new LinkedList<>();
    Point lBound = new Point();
    Point uBound = new Point();
    for (MapleData footRoot : mapData.getChildByPath("foothold")) {
      for (MapleData footCat : footRoot) {
        for (MapleData footHold : footCat) {
          MapleFoothold fh = new MapleFoothold(new Point(MapleDataTool.getInt(footHold.getChildByPath("x1")), MapleDataTool.getInt(footHold.getChildByPath("y1"))), new Point(MapleDataTool.getInt(footHold.getChildByPath("x2")), MapleDataTool.getInt(footHold.getChildByPath("y2"))), Integer.parseInt(footHold.getName()));
          fh.setPrev((short)MapleDataTool.getInt(footHold.getChildByPath("prev")));
          fh.setNext((short)MapleDataTool.getInt(footHold.getChildByPath("next")));
          if (fh.getX1() < lBound.x)
            lBound.x = fh.getX1(); 
          if (fh.getX2() > uBound.x)
            uBound.x = fh.getX2(); 
          if (fh.getY1() < lBound.y)
            lBound.y = fh.getY1(); 
          if (fh.getY2() > uBound.y)
            uBound.y = fh.getY2(); 
          allFootholds.add(fh);
        } 
      } 
    } 
    MapleFootholdTree fTree = new MapleFootholdTree(lBound, uBound);
    for (MapleFoothold fh : allFootholds)
      fTree.insert(fh); 
    map.setFootholds(fTree);
    if (map.getTop() == 0)
      map.setTop(lBound.y); 
    if (map.getBottom() == 0)
      map.setBottom(uBound.y); 
    if (map.getLeft() == 0)
      map.setLeft(lBound.x); 
    if (map.getRight() == 0)
      map.setRight(uBound.x); 
    int bossid = -1;
    String msg = null;
    if (mapData.getChildByPath("info/timeMob") != null) {
      bossid = MapleDataTool.getInt(mapData.getChildByPath("info/timeMob/id"), 0);
      msg = MapleDataTool.getString(mapData.getChildByPath("info/timeMob/message"), (String)null);
    } 
    for (MapleData life : mapData.getChildByPath("life")) {
      String type = MapleDataTool.getString(life.getChildByPath("type"));
      String limited = MapleDataTool.getString("limitedname", life, "");
      if ((npcs || !type.equals("n")) && limited.equals("")) {
        AbstractLoadedMapleLife myLife = loadLife(life, MapleDataTool.getString(life.getChildByPath("id")), type);
        if (myLife instanceof MapleMonster && !GameConstants.isNoSpawn(mapid)) {
          MapleMonster mob = (MapleMonster)myLife;
          map.addMonsterSpawn(mob, 
              MapleDataTool.getInt("mobTime", life, 0), 
              (byte)MapleDataTool.getInt("team", life, -1), 
              (mob.getId() == bossid) ? msg : null);
          if (map.getBurning() == 0)
            map.setBurning(Randomizer.rand(5, 8)); 
          continue;
        } 
        if (myLife instanceof MapleNPC)
          map.addMapObject(myLife); 
      } 
    } 
    addAreaBossSpawn(map);
    map.setCreateMobInterval((short)MapleDataTool.getInt(mapData.getChildByPath("info/createMobInterval"), 9000));
    map.setFixedMob(MapleDataTool.getInt(mapData.getChildByPath("info/fixedMobCapacity"), 0));
    map.setPartyBonusRate(GameConstants.getPartyPlay(mapid, MapleDataTool.getInt(mapData.getChildByPath("info/partyBonusR"), 0)));
    map.loadMonsterRate(true);
    map.setNodes(loadNodes(mapid, mapData));
    if (reactors && mapData.getChildByPath("reactor") != null)
      for (MapleData reactor : mapData.getChildByPath("reactor")) {
        String id = MapleDataTool.getString(reactor.getChildByPath("id"));
        if (id != null)
          map.spawnReactor(loadReactor(reactor, id, (byte)MapleDataTool.getInt(reactor.getChildByPath("f"), 0))); 
      }  
    try {
      map.setMapName(MapleDataTool.getString("mapName", this.nameData.getChildByPath(getMapStringName(mapid)), ""));
      map.setStreetName(MapleDataTool.getString("streetName", this.nameData.getChildByPath(getMapStringName(mapid)), ""));
    } catch (Exception e) {
      map.setMapName("");
      map.setStreetName("");
    } 
    map.setClock((MapleDataTool.getInt(mapData.getChildByPath("info/clock"), 0) > 0));
    map.setEverlast((MapleDataTool.getInt(mapData.getChildByPath("info/everlast"), 0) > 0));
    map.setTown((MapleDataTool.getInt(mapData.getChildByPath("info/town"), 0) > 0));
    map.setSoaring((MapleDataTool.getInt(mapData.getChildByPath("info/needSkillForFly"), 0) > 0));
    map.setForceMove(MapleDataTool.getInt(mapData.getChildByPath("info/lvForceMove"), 0));
    map.setHPDec(MapleDataTool.getInt(mapData.getChildByPath("info/decHP"), 0));
    map.setHPDecInterval(MapleDataTool.getInt(mapData.getChildByPath("info/decHPInterval"), 10000));
    map.setHPDecProtect(MapleDataTool.getInt(mapData.getChildByPath("info/protectItem"), 0));
    map.setForcedReturnMap(MapleDataTool.getInt(mapData.getChildByPath("info/forcedReturn"), 999999999));
    map.setTimeLimit(MapleDataTool.getInt(mapData.getChildByPath("info/timeLimit"), -1));
    map.setFieldLimit(MapleDataTool.getInt(mapData.getChildByPath("info/fieldLimit"), 0));
    map.setFirstUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onFirstUserEnter"), ""));
    map.setUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onUserEnter"), ""));
    map.setFieldType(MapleDataTool.getString(mapData.getChildByPath("info/fieldType"), ""));
    map.setRecoveryRate(MapleDataTool.getFloat(mapData.getChildByPath("info/recovery"), 1.0F));
    map.setConsumeItemCoolTime(MapleDataTool.getInt(mapData.getChildByPath("info/consumeItemCoolTime"), 0));
    map.setInstanceId(instanceid);
    this.lock.lock();
    try {
      this.instanceMap.put(Integer.valueOf(instanceid), map);
    } finally {
      this.lock.unlock();
    } 
    return map;
  }
  
  public int getLoadedMaps() {
    return this.maps.size();
  }
  
  public boolean isMapLoaded(int mapId) {
    return this.maps.containsKey(Integer.valueOf(mapId));
  }
  
  public boolean isInstanceMapLoaded(int instanceid) {
    return this.instanceMap.containsKey(Integer.valueOf(instanceid));
  }
  
  public void clearLoadedMap() {
    this.lock.lock();
    try {
      this.maps.clear();
    } finally {
      this.lock.unlock();
    } 
  }
  
  public List<MapleMap> getAllLoadedMaps() {
    List<MapleMap> ret = new ArrayList<>();
    this.lock.lock();
    try {
      ret.addAll(this.maps.values());
      ret.addAll(this.instanceMap.values());
    } finally {
      this.lock.unlock();
    } 
    return ret;
  }
  
  public Collection<MapleMap> getAllMaps() {
    return this.maps.values();
  }
  
  private AbstractLoadedMapleLife loadLife(MapleData life, String id, String type) {
    AbstractLoadedMapleLife myLife = MapleLifeFactory.getLife(Integer.parseInt(id), type);
    if (myLife == null)
      return null; 
    myLife.setCy(MapleDataTool.getInt(life.getChildByPath("cy")));
    MapleData dF = life.getChildByPath("f");
    if (dF != null)
      myLife.setF(MapleDataTool.getInt(dF)); 
    myLife.setFh(MapleDataTool.getInt(life.getChildByPath("fh")));
    myLife.setRx0(MapleDataTool.getInt(life.getChildByPath("rx0")));
    myLife.setRx1(MapleDataTool.getInt(life.getChildByPath("rx1")));
    myLife.setPosition(new Point(MapleDataTool.getInt(life.getChildByPath("x")), MapleDataTool.getInt(life.getChildByPath("y"))));
    if (MapleDataTool.getInt("hide", life, 0) == 1 && myLife instanceof MapleNPC)
      myLife.setHide(true); 
    return myLife;
  }
  
  private final MapleReactor loadReactor(MapleData reactor, String id, byte FacingDirection) {
    MapleReactor myReactor = new MapleReactor(MapleReactorFactory.getReactor(Integer.parseInt(id)), Integer.parseInt(id));
    myReactor.setFacingDirection(FacingDirection);
    myReactor.setPosition(new Point(MapleDataTool.getInt(reactor.getChildByPath("x")), MapleDataTool.getInt(reactor.getChildByPath("y"))));
    myReactor.setDelay(MapleDataTool.getInt(reactor.getChildByPath("reactorTime")) * 1000);
    myReactor.setState((byte)0);
    myReactor.setName(MapleDataTool.getString(reactor.getChildByPath("name"), ""));
    return myReactor;
  }
  
  private String getMapName(int mapid) {
    String mapName = StringUtil.getLeftPaddedStr(Integer.toString(mapid), '0', 9);
    StringBuilder builder = new StringBuilder("Map/Map");
    builder.append(mapid / 100000000);
    builder.append("/");
    builder.append(mapName);
    builder.append(".img");
    mapName = builder.toString();
    return mapName;
  }
  
  private String getMapStringName(int mapid) {
    StringBuilder builder = new StringBuilder();
    if (mapid < 100000000) {
      builder.append("maple");
    } else if ((mapid >= 100000000 && mapid < 200000000) || mapid / 100000 == 5540) {
      builder.append("victoria");
    } else if (mapid >= 200000000 && mapid < 300000000) {
      builder.append("ossyria");
    } else if (mapid >= 300000000 && mapid < 400000000) {
      builder.append("3rd");
    } else if (mapid >= 500000000 && mapid < 510000000) {
      builder.append("TH");
    } else if (mapid >= 555000000 && mapid < 556000000) {
      builder.append("SG");
    } else if (mapid >= 540000000 && mapid < 600000000) {
      builder.append("SG");
    } else if (mapid >= 682000000 && mapid < 683000000) {
      builder.append("GL");
    } else if (mapid >= 600000000 && mapid < 670000000) {
      builder.append("GL");
    } else if (mapid >= 677000000 && mapid < 678000000) {
      builder.append("GL");
    } else if (mapid >= 670000000 && mapid < 682000000) {
      builder.append("GL");
    } else if (mapid >= 687000000 && mapid < 688000000) {
      builder.append("Gacha_GL");
    } else if (mapid >= 689000000 && mapid < 690000000) {
      builder.append("CTF_GL");
    } else if (mapid >= 683000000 && mapid < 684000000) {
      builder.append("event");
    } else if (mapid >= 684000000 && mapid < 685000000) {
      builder.append("event_5th");
    } else if (mapid >= 700000000 && mapid < 700000300) {
      builder.append("wedding");
    } else if (mapid >= 701000000 && mapid < 701020000) {
      builder.append("china");
    } else if ((mapid >= 702090000 && mapid <= 702100000) || (mapid >= 740000000 && mapid < 741000000)) {
      builder.append("TW");
    } else if (mapid >= 702000000 && mapid < 742000000) {
      builder.append("CN");
    } else if (mapid >= 800000000 && mapid < 900000000) {
      builder.append("JP");
    } else {
      builder.append("etc");
    } 
    builder.append("/");
    builder.append(mapid);
    return builder.toString();
  }
  
  public void setChannel(int channel) {
    this.channel = channel;
  }
  
  private void addAreaBossSpawn(MapleMap map) {
    int monsterid = -1;
    int mobtime = -1;
    String msg = null;
    boolean shouldSpawn = true;
    Point pos1 = null, pos2 = null, pos3 = null;
    switch (map.getId()) {
      case 104010200:
        mobtime = 1200;
        monsterid = 2220000;
        msg = "A cool breeze was felt when Mano appeared.";
        pos1 = new Point(189, 2);
        pos2 = new Point(478, 250);
        pos3 = new Point(611, 489);
        break;
      case 102020500:
        mobtime = 1200;
        monsterid = 3220000;
        msg = "Stumpy has appeared with a stumping sound that rings the Stone Mountain.";
        pos1 = new Point(1121, 2130);
        pos2 = new Point(483, 2171);
        pos3 = new Point(1474, 1706);
        break;
      case 100020101:
        mobtime = 1200;
        monsterid = 6130101;
        msg = "Mushmom has appeared.";
        pos1 = new Point(-311, 201);
        pos2 = new Point(-903, 197);
        pos3 = new Point(-568, 196);
        break;
      case 100020301:
        mobtime = 1200;
        monsterid = 8220007;
        msg = "Blue Mushmom has appeared.";
        pos1 = new Point(-188, -657);
        pos2 = new Point(625, -660);
        pos3 = new Point(508, -648);
        break;
      case 100020401:
        mobtime = 1200;
        monsterid = 6300005;
        msg = "Zombie Mushmom has appeared.";
        pos1 = new Point(-130, -773);
        pos2 = new Point(504, -760);
        pos3 = new Point(608, -641);
        break;
      case 120030500:
        mobtime = 1200;
        monsterid = 5220001;
        msg = "A strange turban shell has appeared on the beach.";
        pos1 = new Point(-355, 179);
        pos2 = new Point(-1283, -113);
        pos3 = new Point(-571, -593);
        break;
      case 250010304:
        mobtime = 2100;
        monsterid = 7220000;
        msg = "Tae Roon appeared with a loud growl.";
        pos1 = new Point(-210, 33);
        pos2 = new Point(-234, 393);
        pos3 = new Point(-654, 33);
        break;
      case 200010300:
        mobtime = 1200;
        monsterid = 8220000;
        msg = "Eliza has appeared with a black whirlwind.";
        pos1 = new Point(665, 83);
        pos2 = new Point(672, -217);
        pos3 = new Point(-123, -217);
        break;
      case 250010503:
        mobtime = 1800;
        monsterid = 7220002;
        msg = "The area fills with an unpleasant force of evil.. even the occasional ones of the cats sound disturbing";
        pos1 = new Point(-303, 543);
        pos2 = new Point(227, 543);
        pos3 = new Point(719, 543);
        break;
      case 222010310:
        mobtime = 2700;
        monsterid = 7220001;
        msg = "As the moon light dims,a long fox cry can be heard and the presence of the old fox can be felt.";
        pos1 = new Point(-169, -147);
        pos2 = new Point(-517, 93);
        pos3 = new Point(247, 93);
        break;
      case 103030400:
        mobtime = 1800;
        monsterid = 6220000;
        msg = "The huge crocodile Dyle has come out from the swamp.";
        pos1 = new Point(-831, 109);
        pos2 = new Point(1525, -75);
        pos3 = new Point(-511, 107);
        break;
      case 101040300:
        mobtime = 1800;
        monsterid = 5220002;
        msg = "The blue fog became darker when Faust appeared.";
        pos1 = new Point(600, -600);
        pos2 = new Point(600, -800);
        pos3 = new Point(600, -300);
        break;
      case 220050100:
        mobtime = 1500;
        monsterid = 5220003;
        msg = "Click clock! Timer has appeared with an irregular clock sound.";
        pos1 = new Point(-467, 1032);
        pos2 = new Point(532, 1032);
        pos3 = new Point(-47, 1032);
        break;
      case 221040301:
        mobtime = 2400;
        monsterid = 6220001;
        msg = "Zeno has appeared with a heavy sound of machinery.";
        pos1 = new Point(-4134, 416);
        pos2 = new Point(-4283, 776);
        pos3 = new Point(-3292, 776);
        break;
      case 240040401:
        mobtime = 7200;
        monsterid = 8220003;
        msg = "Leviathan has appeared with a cold wind from over the gorge.";
        pos1 = new Point(-15, 2481);
        pos2 = new Point(127, 1634);
        pos3 = new Point(159, 1142);
        break;
      case 260010201:
        mobtime = 3600;
        monsterid = 3220001;
        msg = "Deo slowly appeared out of the sand dust.";
        pos1 = new Point(-215, 275);
        pos2 = new Point(298, 275);
        pos3 = new Point(592, 275);
        break;
      case 251010102:
        mobtime = 3600;
        monsterid = 5220004;
        msg = "A giant centipede appeared.";
        pos1 = new Point(-41, 124);
        pos2 = new Point(-173, 126);
        pos3 = new Point(79, 118);
        break;
      case 261030000:
        mobtime = 2700;
        monsterid = 8220002;
        msg = "Chimera has appeared out of the darkness of the underground with a glitter in her eyes.";
        pos1 = new Point(-1094, -405);
        pos2 = new Point(-772, -116);
        pos3 = new Point(-108, 181);
        break;
      case 230020100:
        mobtime = 2700;
        monsterid = 4220000;
        msg = "A strange shell has appeared from a grove of seaweed.";
        pos1 = new Point(-291, -20);
        pos2 = new Point(-272, -500);
        pos3 = new Point(-462, 640);
        break;
      case 103020320:
        mobtime = 1800;
        monsterid = 5090000;
        msg = "Shade has appeared.";
        pos1 = new Point(79, 174);
        pos2 = new Point(-223, 296);
        pos3 = new Point(80, 275);
        break;
      case 103020420:
        mobtime = 1800;
        monsterid = 5090000;
        msg = "Shade has appeared.";
        pos1 = new Point(2241, 301);
        pos2 = new Point(1990, 301);
        pos3 = new Point(1684, 307);
        break;
      case 261020300:
        mobtime = 2700;
        monsterid = 7090000;
        msg = "A camera has appeared.";
        pos1 = new Point(312, 157);
        pos2 = new Point(539, 136);
        pos3 = new Point(760, 141);
        break;
      case 261020401:
        mobtime = 2700;
        monsterid = 8090000;
        msg = "Deet and Roi has appeared.";
        pos1 = new Point(-263, 155);
        pos2 = new Point(-436, 122);
        pos3 = new Point(22, 144);
        break;
      case 250020300:
        mobtime = 2700;
        monsterid = 5090001;
        msg = "Master Dummy has appeared.";
        pos1 = new Point(1208, 27);
        pos2 = new Point(1654, 40);
        pos3 = new Point(927, -502);
        break;
      case 211050000:
        mobtime = 2700;
        monsterid = 6090001;
        msg = "The witch of snow has appeared.";
        pos1 = new Point(-233, -431);
        pos2 = new Point(-370, -426);
        pos3 = new Point(-526, -420);
        break;
      case 261010003:
        mobtime = 2700;
        monsterid = 6090004;
        msg = "Rurumo has appeared.";
        pos1 = new Point(-861, 301);
        pos2 = new Point(-703, 301);
        pos3 = new Point(-426, 287);
        break;
      case 222010300:
        mobtime = 2700;
        monsterid = 6090003;
        msg = "A wise ghost has appeared.";
        pos1 = new Point(1300, -400);
        pos2 = new Point(1100, -100);
        pos3 = new Point(1100, 100);
        break;
      case 251010101:
        mobtime = 2700;
        monsterid = 6090002;
        msg = "A warrior with bamboo has appeared.";
        pos1 = new Point(-15, -449);
        pos2 = new Point(-114, -442);
        pos3 = new Point(-255, -446);
        break;
      case 211041400:
        mobtime = 2700;
        monsterid = 6090000;
        msg = "Riche has appeared.";
        pos1 = new Point(1672, 82);
        pos2 = new Point(2071, 10);
        pos3 = new Point(1417, 57);
        break;
      case 105030500:
        mobtime = 2700;
        monsterid = 8130100;
        msg = "Jr. Balrog has appeared.";
        pos1 = new Point(1275, -399);
        pos2 = new Point(1254, -412);
        pos3 = new Point(1058, -427);
        break;
      case 105020400:
        mobtime = 2700;
        monsterid = 8220008;
        msg = "A mysterious shop appeared.";
        pos1 = new Point(-163, 82);
        pos2 = new Point(958, 107);
        pos3 = new Point(706, -206);
        break;
      case 211040101:
        mobtime = 3600;
        monsterid = 8220001;
        msg = "A snowman covered in ice has appeared.";
        pos1 = new Point(485, 244);
        pos2 = new Point(-60, 249);
        pos3 = new Point(208, 255);
        break;
      case 910000000:
        if (this.channel == 7) {
          mobtime = 3600;
          monsterid = 9420015;
          msg = "NooNoo has appeared out of anger.";
          pos1 = new Point(498, 4);
          pos2 = new Point(498, 4);
          pos3 = new Point(498, 4);
          break;
        } 
        if (this.channel == 8) {
          mobtime = 3600;
          monsterid = 9400700;
          msg = "Giant Tomato has appeared.";
          pos1 = new Point(498, 4);
          pos2 = new Point(498, 4);
          pos3 = new Point(498, 4);
          break;
        } 
        if (this.channel == 9) {
          mobtime = 3600;
          monsterid = 9400734;
          msg = "Giant Tomato has appeared.";
          pos1 = new Point(498, 4);
          pos2 = new Point(498, 4);
          pos3 = new Point(498, 4);
        } 
        break;
      case 209000000:
        mobtime = 300;
        monsterid = 9500317;
        msg = "Little Snowman has appeared!";
        pos1 = new Point(-115, 154);
        pos2 = new Point(-115, 154);
        pos3 = new Point(-115, 154);
        break;
      case 677000001:
        mobtime = 60;
        monsterid = 9400612;
        msg = "Marbas has appeared.";
        pos1 = new Point(99, 60);
        pos2 = new Point(99, 60);
        pos3 = new Point(99, 60);
        break;
      case 677000003:
        mobtime = 60;
        monsterid = 9400610;
        msg = "Amdusias has appeared.";
        pos1 = new Point(6, 35);
        pos2 = new Point(6, 35);
        pos3 = new Point(6, 35);
        break;
      case 677000005:
        mobtime = 60;
        monsterid = 9400609;
        msg = "Andras has appeared.";
        pos1 = new Point(-277, 78);
        pos2 = new Point(547, 86);
        pos3 = new Point(-347, 80);
        break;
      case 677000007:
        mobtime = 60;
        monsterid = 9400611;
        msg = "Crocell has appeared.";
        pos1 = new Point(117, 73);
        pos2 = new Point(117, 73);
        pos3 = new Point(117, 73);
        break;
      case 677000009:
        mobtime = 60;
        monsterid = 9400613;
        msg = "Valefor has appeared.";
        pos1 = new Point(85, 66);
        pos2 = new Point(85, 66);
        pos3 = new Point(85, 66);
        break;
      case 931000500:
        mobtime = 108000;
        monsterid = 9304005;
        msg = "Jaira has appeared.";
        pos1 = new Point(-872, -332);
        pos2 = new Point(409, -572);
        pos3 = new Point(-131, 0);
        shouldSpawn = false;
        break;
    } 
    if (monsterid > 0)
      map.addAreaMonsterSpawn(MapleLifeFactory.getMonster(monsterid), pos1, pos2, pos3, mobtime, msg, shouldSpawn); 
  }
  
  private void loadPortals(MapleMap map, MapleData port) {
    if (port == null)
      return; 
    int nextDoorPortal = 128;
    for (MapleData portal : port.getChildren()) {
      MaplePortal myPortal = new MaplePortal(MapleDataTool.getInt(portal.getChildByPath("pt")));
      myPortal.setName(MapleDataTool.getString(portal.getChildByPath("pn")));
      myPortal.setTarget(MapleDataTool.getString(portal.getChildByPath("tn")));
      myPortal.setTargetMapId(MapleDataTool.getInt(portal.getChildByPath("tm")));
      myPortal.setPosition(new Point(MapleDataTool.getInt(portal.getChildByPath("x")), MapleDataTool.getInt(portal.getChildByPath("y"))));
      String script = MapleDataTool.getString("script", portal, null);
      if (script != null && script.equals(""))
        script = null; 
      myPortal.setScriptName(script);
      if (myPortal.getType() == 6) {
        myPortal.setId(nextDoorPortal);
        nextDoorPortal++;
      } else {
        myPortal.setId(Integer.parseInt(portal.getName()));
      } 
      map.addPortal(myPortal);
    } 
  }
  
  private MapleNodes loadNodes(int mapid, MapleData mapData) {
    MapleNodes nodeInfo = new MapleNodes(mapid);
    if (mapData.getChildByPath("nodeInfo") != null) {
      for (MapleData node : mapData.getChildByPath("nodeInfo")) {
        try {
          if (node.getName().equals("start")) {
            nodeInfo.setNodeStart(MapleDataTool.getInt(node, 0));
            continue;
          } 
          List<Integer> edges = new ArrayList<>();
          if (node.getChildByPath("edge") != null)
            for (MapleData edge : node.getChildByPath("edge"))
              edges.add(Integer.valueOf(MapleDataTool.getInt(edge, -1)));  
          MapleNodes.MapleNodeInfo mni = new MapleNodes.MapleNodeInfo(Integer.parseInt(node.getName()), MapleDataTool.getIntConvert("key", node, 0), MapleDataTool.getIntConvert("x", node, 0), MapleDataTool.getIntConvert("y", node, 0), MapleDataTool.getIntConvert("attr", node, 0), edges);
          nodeInfo.addNode(mni);
        } catch (NumberFormatException numberFormatException) {}
      } 
      nodeInfo.sortNodes();
    } 
    for (int i = 0; i <= 10; i++) {
      if (mapData.getChildByPath(String.valueOf(i)) != null && mapData.getChildByPath(i + "/obj") != null)
        for (MapleData node : mapData.getChildByPath(i + "/obj")) {
          if (node.getChildByPath("SN_count") != null && node.getChildByPath("speed") != null) {
            int sn_count = MapleDataTool.getIntConvert("SN_count", node, 0);
            String name = MapleDataTool.getString("name", node, "");
            int speed = MapleDataTool.getIntConvert("speed", node, 0);
            if (sn_count <= 0 || speed <= 0 || name.equals(""))
              continue; 
            List<Integer> SN = new ArrayList<>();
            for (int x = 0; x < sn_count; x++)
              SN.add(Integer.valueOf(MapleDataTool.getIntConvert("SN" + x, node, 0))); 
            MapleNodes.MaplePlatform mni = new MapleNodes.MaplePlatform(name, MapleDataTool.getIntConvert("start", node, 2), speed, MapleDataTool.getIntConvert("x1", node, 0), MapleDataTool.getIntConvert("y1", node, 0), MapleDataTool.getIntConvert("x2", node, 0), MapleDataTool.getIntConvert("y2", node, 0), MapleDataTool.getIntConvert("r", node, 0), SN);
            nodeInfo.addPlatform(mni);
            continue;
          } 
          if (node.getChildByPath("SN_count") != null) {
            String name = MapleDataTool.getString("name", node, "");
            MapleNodes.Environment ev = new MapleNodes.Environment(name, MapleDataTool.getIntConvert("x", node, 0), MapleDataTool.getIntConvert("y", node, 0));
            nodeInfo.addEnvironment(ev);
            continue;
          } 
          if (node.getChildByPath("tags") != null) {
            String name = MapleDataTool.getString("tags", node, "");
            nodeInfo.addFlag(new Pair<>(name, Integer.valueOf(name.endsWith("3") ? 1 : 0)));
          } 
        }  
    } 
    if (mapData.getChildByPath("area") != null)
      for (MapleData area : mapData.getChildByPath("area")) {
        int x1 = MapleDataTool.getInt(area.getChildByPath("x1"));
        int y1 = MapleDataTool.getInt(area.getChildByPath("y1"));
        int x2 = MapleDataTool.getInt(area.getChildByPath("x2"));
        int y2 = MapleDataTool.getInt(area.getChildByPath("y2"));
        Rectangle mapArea = new Rectangle(x1, y1, x2 - x1, y2 - y1);
        nodeInfo.addMapleArea(mapArea);
      }  
    if (mapData.getChildByPath("CaptureTheFlag") != null) {
      MapleData mc = mapData.getChildByPath("CaptureTheFlag");
      for (MapleData area : mc)
        nodeInfo.addGuardianSpawn(new Point(MapleDataTool.getInt(area.getChildByPath("FlagPositionX")), MapleDataTool.getInt(area.getChildByPath("FlagPositionY"))), area.getName().startsWith("Red") ? 0 : 1); 
    } 
    if (mapData.getChildByPath("directionInfo") != null) {
      MapleData mc = mapData.getChildByPath("directionInfo");
      for (MapleData area : mc) {
        MapleNodes.DirectionInfo di = new MapleNodes.DirectionInfo(Integer.parseInt(area.getName()), MapleDataTool.getInt("x", area, 0), MapleDataTool.getInt("y", area, 0), (MapleDataTool.getInt("forcedInput", area, 0) > 0));
        MapleData mc2 = area.getChildByPath("eventQ");
        if (mc2 != null)
          for (MapleData event : mc2)
            di.eventQ.add(MapleDataTool.getString(event));  
        nodeInfo.addDirection(Integer.parseInt(area.getName()), di);
      } 
    } 
    if (mapData.getChildByPath("monsterCarnival") != null) {
      MapleData mc = mapData.getChildByPath("monsterCarnival");
      if (mc.getChildByPath("mobGenPos") != null)
        for (MapleData area : mc.getChildByPath("mobGenPos"))
          nodeInfo.addMonsterPoint(MapleDataTool.getInt(area.getChildByPath("x")), 
              MapleDataTool.getInt(area.getChildByPath("y")), 
              MapleDataTool.getInt(area.getChildByPath("fh")), 
              MapleDataTool.getInt(area.getChildByPath("cy")), 
              MapleDataTool.getInt("team", area, -1));  
      if (mc.getChildByPath("mob") != null)
        for (MapleData area : mc.getChildByPath("mob"))
          nodeInfo.addMobSpawn(MapleDataTool.getInt(area.getChildByPath("id")), MapleDataTool.getInt(area.getChildByPath("spendCP")));  
      if (mc.getChildByPath("guardianGenPos") != null)
        for (MapleData area : mc.getChildByPath("guardianGenPos"))
          nodeInfo.addGuardianSpawn(new Point(MapleDataTool.getInt(area.getChildByPath("x")), MapleDataTool.getInt(area.getChildByPath("y"))), MapleDataTool.getInt("team", area, -1));  
      if (mc.getChildByPath("skill") != null)
        for (MapleData area : mc.getChildByPath("skill"))
          nodeInfo.addSkillId(MapleDataTool.getInt(area));  
    } 
    return nodeInfo;
  }
}
