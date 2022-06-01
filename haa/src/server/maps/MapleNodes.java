package server.maps;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import tools.Pair;

public class MapleNodes {
  private Map<Integer, MapleNodeInfo> nodes;
  
  private final List<Rectangle> areas;
  
  private List<MaplePlatform> platforms;
  
  private List<MonsterPoint> monsterPoints;
  
  private List<Integer> skillIds;
  
  private List<Pair<Integer, Integer>> mobsToSpawn;
  
  private List<Pair<Point, Integer>> guardiansToSpawn;
  
  private List<Pair<String, Integer>> flags;
  
  private List<DirectionInfo> directionInfo;
  
  private List<Environment> environments;
  
  private int nodeStart = -1;
  
  private int mapid;
  
  private boolean firstHighest = true;
  
  public MapleNodes(int mapid) {
    this.nodes = new LinkedHashMap<>();
    this.areas = new ArrayList<>();
    this.platforms = new ArrayList<>();
    this.skillIds = new ArrayList<>();
    this.directionInfo = new ArrayList<>();
    this.monsterPoints = new ArrayList<>();
    this.mobsToSpawn = new ArrayList<>();
    this.guardiansToSpawn = new ArrayList<>();
    this.flags = new ArrayList<>();
    this.environments = new ArrayList<>();
    this.mapid = mapid;
  }
  
  public void setNodeStart(int ns) {
    this.nodeStart = ns;
  }
  
  public static class MapleNodeInfo {
    public int node;
    
    public int key;
    
    public int x;
    
    public int y;
    
    public int attr;
    
    public int nextNode = -1;
    
    public List<Integer> edge;
    
    public MapleNodeInfo(int node, int key, int x, int y, int attr, List<Integer> edge) {
      this.node = node;
      this.key = key;
      this.x = x;
      this.y = y;
      this.attr = attr;
      this.edge = edge;
    }
  }
  
  public static class DirectionInfo {
    public int x;
    
    public int y;
    
    public int key;
    
    public boolean forcedInput;
    
    public List<String> eventQ = new ArrayList<>();
    
    public DirectionInfo(int key, int x, int y, boolean forcedInput) {
      this.key = key;
      this.x = x;
      this.y = y;
      this.forcedInput = forcedInput;
    }
  }
  
  public static class Environment {
    private int x;
    
    private int y;
    
    private boolean show;
    
    private String name;
    
    public Environment(String name, int x, int y) {
      setName(name);
      setX(x);
      setY(y);
      setShow(false);
    }
    
    public int getX() {
      return this.x;
    }
    
    public void setX(int x) {
      this.x = x;
    }
    
    public int getY() {
      return this.y;
    }
    
    public void setY(int y) {
      this.y = y;
    }
    
    public boolean isShow() {
      return this.show;
    }
    
    public void setShow(boolean show) {
      this.show = show;
    }
    
    public String getName() {
      return this.name;
    }
    
    public void setName(String name) {
      this.name = name;
    }
  }
  
  public void addDirection(int key, DirectionInfo d) {
    this.directionInfo.add(key, d);
  }
  
  public DirectionInfo getDirection(int key) {
    if (key >= this.directionInfo.size())
      return null; 
    return this.directionInfo.get(key);
  }
  
  public List<Pair<String, Integer>> getFlags() {
    return this.flags;
  }
  
  public void addFlag(Pair<String, Integer> f) {
    this.flags.add(f);
  }
  
  public void addNode(MapleNodeInfo mni) {
    this.nodes.put(Integer.valueOf(mni.key), mni);
  }
  
  public Collection<MapleNodeInfo> getNodes() {
    return new ArrayList<>(this.nodes.values());
  }
  
  public MapleNodeInfo getNode(int index) {
    int i = 1;
    for (MapleNodeInfo x : getNodes()) {
      if (i == index)
        return x; 
      i++;
    } 
    return null;
  }
  
  public boolean isLastNode(int index) {
    return (index == this.nodes.size());
  }
  
  private int getNextNode(MapleNodeInfo mni) {
    if (mni == null)
      return -1; 
    addNode(mni);
    int ret = -1;
    for (Iterator<Integer> iterator = mni.edge.iterator(); iterator.hasNext(); ) {
      int i = ((Integer)iterator.next()).intValue();
      if (!this.nodes.containsKey(Integer.valueOf(i))) {
        if (ret != -1 && (this.mapid / 100 == 9211204 || this.mapid / 100 == 9320001 || this.mapid / 100 == 9211202)) {
          if (!this.firstHighest) {
            ret = Math.min(ret, i);
            continue;
          } 
          this.firstHighest = false;
          ret = Math.max(ret, i);
          break;
        } 
        ret = i;
      } 
    } 
    mni.nextNode = ret;
    return ret;
  }
  
  public void sortNodes() {
    if (this.nodes.size() <= 0 || this.nodeStart < 0)
      return; 
    Map<Integer, MapleNodeInfo> unsortedNodes = new HashMap<>(this.nodes);
    int nodeSize = unsortedNodes.size();
    this.nodes.clear();
    int nextNode = getNextNode(unsortedNodes.get(Integer.valueOf(this.nodeStart)));
    while (this.nodes.size() != nodeSize && nextNode >= 0)
      nextNode = getNextNode(unsortedNodes.get(Integer.valueOf(nextNode))); 
  }
  
  public final void addMapleArea(Rectangle rec) {
    this.areas.add(rec);
  }
  
  public final List<Rectangle> getAreas() {
    return new ArrayList<>(this.areas);
  }
  
  public final Rectangle getArea(int index) {
    return getAreas().get(index);
  }
  
  public static class MaplePlatform {
    public String name;
    
    public int start;
    
    public int speed;
    
    public int x1;
    
    public int y1;
    
    public int x2;
    
    public int y2;
    
    public int r;
    
    public List<Integer> SN;
    
    public MaplePlatform(String name, int start, int speed, int x1, int y1, int x2, int y2, int r, List<Integer> SN) {
      this.name = name;
      this.start = start;
      this.speed = speed;
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
      this.r = r;
      this.SN = SN;
    }
  }
  
  public final void addPlatform(MaplePlatform mp) {
    this.platforms.add(mp);
  }
  
  public final List<MaplePlatform> getPlatforms() {
    return new ArrayList<>(this.platforms);
  }
  
  public static class MonsterPoint {
    public int x;
    
    public int y;
    
    public int fh;
    
    public int cy;
    
    public int team;
    
    public MonsterPoint(int x, int y, int fh, int cy, int team) {
      this.x = x;
      this.y = y;
      this.fh = fh;
      this.cy = cy;
      this.team = team;
    }
  }
  
  public final List<MonsterPoint> getMonsterPoints() {
    return this.monsterPoints;
  }
  
  public final void addMonsterPoint(int x, int y, int fh, int cy, int team) {
    this.monsterPoints.add(new MonsterPoint(x, y, fh, cy, team));
  }
  
  public final void addMobSpawn(int mobId, int spendCP) {
    this.mobsToSpawn.add(new Pair<>(Integer.valueOf(mobId), Integer.valueOf(spendCP)));
  }
  
  public final List<Pair<Integer, Integer>> getMobsToSpawn() {
    return this.mobsToSpawn;
  }
  
  public final void addGuardianSpawn(Point guardian, int team) {
    this.guardiansToSpawn.add(new Pair<>(guardian, Integer.valueOf(team)));
  }
  
  public final List<Pair<Point, Integer>> getGuardians() {
    return this.guardiansToSpawn;
  }
  
  public final List<Integer> getSkillIds() {
    return this.skillIds;
  }
  
  public final void addSkillId(int z) {
    this.skillIds.add(Integer.valueOf(z));
  }
  
  public List<Environment> getEnvironments() {
    return this.environments;
  }
  
  public void setEnvironments(List<Environment> environments) {
    this.environments = environments;
  }
  
  public final void addEnvironment(Environment mp) {
    this.environments.add(mp);
  }
}
