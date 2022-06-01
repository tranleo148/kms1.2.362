package server.field.boss.demian;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import server.Randomizer;
import server.Timer;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import tools.packet.MobPacket;

public class MapleFlyingSword extends MapleMapObject {
  private boolean stop;
  
  private int objectType;
  
  private int count = 0;
  
  private MapleMonster owner;
  
  private MapleCharacter target;
  
  private List<FlyingSwordNode> nodes = new ArrayList<>();
  
  public MapleFlyingSword(int objectType, MapleMonster owner) {
    setObjectType(objectType);
    setOwner(owner);
    setStop(false);
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.SWORD;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(MobPacket.FlyingSword(this, true));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(MobPacket.FlyingSword(this, false));
  }
  
  public int getObjectType() {
    return this.objectType;
  }
  
  public void setObjectType(int objectType) {
    this.objectType = objectType;
  }
  
  public MapleMonster getOwner() {
    return this.owner;
  }
  
  public void setOwner(MapleMonster owner) {
    this.owner = owner;
  }
  
  public MapleCharacter getTarget() {
    return this.target;
  }
  
  public void setTarget(MapleCharacter target) {
    this.target = target;
  }
  
  public boolean isStop() {
    return this.stop;
  }
  
  public void setStop(boolean stop) {
    this.stop = stop;
  }
  
  public List<FlyingSwordNode> getNodes() {
    return this.nodes;
  }
  
  public void setNodes(List<FlyingSwordNode> nodes) {
    this.nodes = nodes;
  }
  
  public void updateFlyingSwordNode(MapleMap map) {
    this.count++;
    getNodes().clear();
    List<FlyingSwordNode> node = setNodes();
    setNodes(node);
    map.broadcastMessage(MobPacket.FlyingSwordNode(this));
  }
  
  public void tryAttack(final MapleMap map, Point pos) {
    setStop(true);
    MapleCharacter target = map.getCharacter(getTarget().getId());
    if (target != null) {
      MobSkill skill = MobSkillFactory.getMobSkill(131, 28);
      MapleMist mist = new MapleMist(new Rectangle(pos.x - 195, -169, 390, 185), this.owner, skill, 10000);
      mist.setDelay(0);
      mist.setPosition(new Point(pos.x, 16));
      setPosition(new Point(pos.x, 16));
      if (mist != null)
        map.spawnMist(mist, false); 
    } 
    if (this != null) {
      Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
              MapleFlyingSword.this.updateTarget(map);
            }
          },  1000L);
      Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
              if (MapleFlyingSword.this.count >= 5 && MapleFlyingSword.this.objectType == 1) {
                MapleFlyingSword.this.removeSword(map);
              } else {
                MapleFlyingSword.this.updateFlyingSwordNode(map);
              } 
            }
          },  11000L);
    } 
  }
  
  public void removeSword(MapleMap map) {
    map.broadcastMessage(MobPacket.FlyingSword(this, false));
    map.removeMapObject(this);
  }
  
  public void updateTarget(MapleMap map) {
    if (map.getCharactersThreadsafe().size() > 0) {
      MapleCharacter target = map.getCharactersThreadsafe().get(Randomizer.nextInt(map.getCharactersThreadsafe().size()));
      if (target != null) {
        setTarget(target);
        map.broadcastMessage(MobPacket.FlyingSwordTarget(this));
      } 
    } 
  }
  
  public int getCount() {
    return this.count;
  }
  
  public void setCount(int count) {
    this.count = count;
  }
  
  public List<FlyingSwordNode> setNodes() {
    List<FlyingSwordNode> nodes = new ArrayList<>();
    setStop(false);
    int type = Randomizer.rand(0, 5);
    if (type == 0) {
      nodes.add(new FlyingSwordNode(1, 2, 0, 35, 0, 0, 0, false, 0, new Point(895, -300)));
      nodes.add(new FlyingSwordNode(1, 2, 1, 35, 0, 0, 0, false, 0, new Point(1530, -100)));
      nodes.add(new FlyingSwordNode(1, 2, 2, 35, 0, 0, 0, false, 0, new Point(1290, 100)));
      nodes.add(new FlyingSwordNode(1, 2, 3, 35, 0, 0, 0, false, 0, new Point(740, -200)));
      nodes.add(new FlyingSwordNode(1, 2, 4, 35, 0, 0, 0, false, 0, new Point(240, 50)));
      nodes.add(new FlyingSwordNode(1, 2, 5, 35, 0, 0, 0, false, 0, new Point(-60, -100)));
      nodes.add(new FlyingSwordNode(1, 2, 6, 35, 0, 0, 0, false, 0, new Point(90, -300)));
      nodes.add(new FlyingSwordNode(1, 2, 7, 35, 0, 0, 0, false, 0, new Point(590, -25)));
      nodes.add(new FlyingSwordNode(1, 2, 8, 35, 0, 0, 0, false, 0, new Point(1690, -25)));
      nodes.add(new FlyingSwordNode(1, 2, 9, 35, 0, 0, 0, false, 0, new Point(1290, -25)));
      nodes.add(new FlyingSwordNode(1, 2, 10, 35, 0, 0, 0, false, 0, new Point(1040, 100)));
      nodes.add(new FlyingSwordNode(1, 2, 11, 35, 0, 0, 0, false, 0, new Point(540, -200)));
      nodes.add(new FlyingSwordNode(2, 2, 12, 60, 500, 0, 0, false, 0, new Point(1600, -400)));
      nodes.add(new FlyingSwordNode(2, 2, 13, 35, 0, 11000, 0, true, 0, new Point(1600, -400)));
    } else if (type == 1) {
      nodes.add(new FlyingSwordNode(1, 4, 0, 35, 0, 0, 0, false, 0, new Point(895, -300)));
      nodes.add(new FlyingSwordNode(1, 4, 1, 35, 0, 0, 0, false, 0, new Point(410, 100)));
      nodes.add(new FlyingSwordNode(1, 4, 2, 35, 0, 0, 0, false, 0, new Point(40, -200)));
      nodes.add(new FlyingSwordNode(1, 4, 3, 35, 0, 0, 0, false, 0, new Point(-60, 100)));
      nodes.add(new FlyingSwordNode(1, 4, 4, 35, 0, 0, 0, false, 0, new Point(360, -150)));
      nodes.add(new FlyingSwordNode(1, 4, 5, 35, 0, 0, 0, false, 0, new Point(810, 50)));
      nodes.add(new FlyingSwordNode(1, 4, 6, 35, 0, 0, 0, false, 0, new Point(1440, -200)));
      nodes.add(new FlyingSwordNode(1, 4, 7, 35, 0, 0, 0, false, 0, new Point(1690, -50)));
      nodes.add(new FlyingSwordNode(1, 4, 8, 35, 0, 0, 0, false, 0, new Point(1540, 100)));
      nodes.add(new FlyingSwordNode(1, 4, 9, 35, 0, 0, 0, false, 0, new Point(840, -150)));
      nodes.add(new FlyingSwordNode(2, 4, 10, 35, 500, 0, 0, false, 0, new Point(840, -150)));
      nodes.add(new FlyingSwordNode(2, 4, 11, 35, 500, 0, 0, false, 0, new Point(840, -150)));
      nodes.add(new FlyingSwordNode(2, 4, 12, 35, 500, 0, 0, false, 0, new Point(840, -150)));
      nodes.add(new FlyingSwordNode(1, 4, 13, 35, 0, 0, 0, false, 0, new Point(895, -300)));
      nodes.add(new FlyingSwordNode(2, 4, 14, 60, 500, 0, 0, false, 0, new Point(840, -150)));
      nodes.add(new FlyingSwordNode(2, 4, 15, 35, 0, 11000, 0, true, 0, new Point(0, 0)));
    } else if (type == 2) {
      nodes.add(new FlyingSwordNode(1, 6, 0, 35, 0, 0, 0, false, 0, new Point(895, -300)));
      nodes.add(new FlyingSwordNode(1, 6, 1, 35, 0, 0, 0, false, 0, new Point(1290, 50)));
      nodes.add(new FlyingSwordNode(1, 6, 2, 35, 0, 0, 0, false, 0, new Point(1690, -150)));
      nodes.add(new FlyingSwordNode(1, 6, 3, 35, 0, 0, 0, false, 0, new Point(1640, 50)));
      nodes.add(new FlyingSwordNode(1, 6, 4, 35, 0, 0, 0, false, 0, new Point(1140, -150)));
      nodes.add(new FlyingSwordNode(1, 6, 5, 35, 0, 0, 0, false, 0, new Point(490, 50)));
      nodes.add(new FlyingSwordNode(1, 6, 6, 35, 0, 0, 0, false, 0, new Point(190, -100)));
      nodes.add(new FlyingSwordNode(1, 6, 7, 35, 0, 0, 0, false, 0, new Point(-60, -25)));
      nodes.add(new FlyingSwordNode(1, 6, 8, 35, 0, 0, 0, false, 0, new Point(740, -25)));
      nodes.add(new FlyingSwordNode(1, 6, 9, 35, 0, 0, 0, false, 0, new Point(890, 100)));
      nodes.add(new FlyingSwordNode(1, 6, 10, 35, 0, 0, 0, false, 0, new Point(1490, -200)));
      nodes.add(new FlyingSwordNode(1, 6, 11, 35, 0, 0, 0, false, 0, new Point(1690, -25)));
      nodes.add(new FlyingSwordNode(1, 6, 12, 35, 0, 0, 0, false, 0, new Point(890, -25)));
      nodes.add(new FlyingSwordNode(1, 6, 13, 35, 0, 0, 0, false, 0, new Point(540, -300)));
      nodes.add(new FlyingSwordNode(1, 6, 14, 35, 0, 0, 0, false, 0, new Point(90, 100)));
      nodes.add(new FlyingSwordNode(1, 6, 15, 35, 0, 0, 0, false, 0, new Point(-60, -100)));
      nodes.add(new FlyingSwordNode(1, 6, 16, 35, 0, 0, 0, false, 0, new Point(890, 50)));
      nodes.add(new FlyingSwordNode(2, 6, 17, 35, 500, 0, 0, false, 0, new Point(890, 50)));
      nodes.add(new FlyingSwordNode(1, 6, 18, 35, 0, 0, 0, false, 0, new Point(895, -300)));
      nodes.add(new FlyingSwordNode(2, 6, 19, 60, 500, 0, 0, false, 0, new Point(890, 50)));
      nodes.add(new FlyingSwordNode(2, 6, 20, 35, 0, 11000, 0, true, 0, new Point(0, 0)));
    } else if (type == 3) {
      nodes.add(new FlyingSwordNode(1, 1, 0, 35, 0, 0, 0, false, 0, new Point(100, -100)));
      nodes.add(new FlyingSwordNode(1, 1, 1, 35, 0, 0, 0, false, 0, new Point(340, 100)));
      nodes.add(new FlyingSwordNode(1, 1, 2, 35, 0, 0, 0, false, 0, new Point(890, -200)));
      nodes.add(new FlyingSwordNode(1, 1, 3, 35, 0, 0, 0, false, 0, new Point(1390, 50)));
      nodes.add(new FlyingSwordNode(1, 1, 4, 35, 0, 0, 0, false, 0, new Point(1690, -100)));
      nodes.add(new FlyingSwordNode(1, 1, 5, 35, 0, 0, 0, false, 0, new Point(1540, -300)));
      nodes.add(new FlyingSwordNode(1, 1, 6, 35, 0, 0, 0, false, 0, new Point(1040, -25)));
      nodes.add(new FlyingSwordNode(1, 1, 7, 35, 0, 0, 0, false, 0, new Point(-60, -25)));
      nodes.add(new FlyingSwordNode(1, 1, 8, 35, 0, 0, 0, false, 0, new Point(340, -25)));
      nodes.add(new FlyingSwordNode(1, 1, 9, 35, 0, 0, 0, false, 0, new Point(590, 100)));
      nodes.add(new FlyingSwordNode(1, 1, 10, 35, 0, 0, 0, false, 0, new Point(1090, -200)));
      nodes.add(new FlyingSwordNode(2, 1, 11, 60, 500, 0, 0, false, 0, new Point(1600, -400)));
      nodes.add(new FlyingSwordNode(2, 1, 12, 35, 0, 11000, 0, true, 0, new Point(0, 0)));
    } else if (type == 4) {
      nodes.add(new FlyingSwordNode(1, 3, 0, 35, 0, 0, 0, false, 0, new Point(895, -500)));
      nodes.add(new FlyingSwordNode(1, 3, 1, 35, 0, 0, 0, false, 0, new Point(350, 0)));
      nodes.add(new FlyingSwordNode(1, 3, 2, 35, 0, 0, 0, false, 0, new Point(140, -150)));
      nodes.add(new FlyingSwordNode(1, 3, 3, 35, 0, 0, 0, false, 0, new Point(-60, 100)));
      nodes.add(new FlyingSwordNode(1, 3, 4, 35, 0, 0, 0, false, 0, new Point(840, -50)));
      nodes.add(new FlyingSwordNode(1, 3, 5, 35, 0, 0, 0, false, 0, new Point(1140, 100)));
      nodes.add(new FlyingSwordNode(1, 3, 6, 35, 0, 0, 0, false, 0, new Point(1690, -25)));
      nodes.add(new FlyingSwordNode(1, 3, 7, 35, 0, 0, 0, false, 0, new Point(940, -25)));
      nodes.add(new FlyingSwordNode(1, 3, 8, 35, 0, 0, 0, false, 0, new Point(1240, -250)));
      nodes.add(new FlyingSwordNode(1, 3, 9, 35, 0, 0, 0, false, 0, new Point(1590, 100)));
      nodes.add(new FlyingSwordNode(1, 3, 10, 35, 0, 0, 0, false, 0, new Point(1690, -100)));
      nodes.add(new FlyingSwordNode(1, 3, 11, 35, 0, 0, 0, false, 0, new Point(990, 50)));
      nodes.add(new FlyingSwordNode(1, 3, 12, 35, 0, 0, 0, false, 0, new Point(740, -50)));
      nodes.add(new FlyingSwordNode(1, 3, 13, 35, 0, 0, 0, false, 0, new Point(490, 100)));
      nodes.add(new FlyingSwordNode(1, 3, 14, 35, 0, 0, 0, false, 0, new Point(-60, -25)));
      nodes.add(new FlyingSwordNode(1, 3, 15, 35, 0, 0, 0, false, 0, new Point(690, -25)));
      nodes.add(new FlyingSwordNode(2, 3, 16, 35, 500, 0, 0, false, 0, new Point(690, -25)));
      nodes.add(new FlyingSwordNode(1, 3, 17, 35, 0, 0, 0, false, 0, new Point(895, -300)));
      nodes.add(new FlyingSwordNode(2, 3, 18, 60, 500, 0, 0, false, 0, new Point(690, -25)));
      nodes.add(new FlyingSwordNode(2, 3, 19, 35, 0, 11000, 0, true, 0, new Point(0, 0)));
    } else if (type == 5) {
      nodes.add(new FlyingSwordNode(1, 5, 0, 35, 0, 0, 0, false, 0, new Point(100, 100)));
      nodes.add(new FlyingSwordNode(1, 5, 1, 35, 0, 0, 0, false, 0, new Point(490, -400)));
      nodes.add(new FlyingSwordNode(1, 5, 2, 35, 0, 0, 0, false, 0, new Point(840, 75)));
      nodes.add(new FlyingSwordNode(1, 5, 3, 35, 0, 0, 0, false, 0, new Point(1190, -450)));
      nodes.add(new FlyingSwordNode(1, 5, 4, 35, 0, 0, 0, false, 0, new Point(1500, 50)));
      nodes.add(new FlyingSwordNode(1, 5, 5, 35, 0, 0, 0, false, 0, new Point(1700, -150)));
      nodes.add(new FlyingSwordNode(1, 5, 6, 35, 0, 0, 0, false, 0, new Point(1040, 100)));
      nodes.add(new FlyingSwordNode(1, 5, 7, 35, 0, 0, 0, false, 0, new Point(440, -150)));
      nodes.add(new FlyingSwordNode(1, 5, 8, 35, 0, 0, 0, false, 0, new Point(-70, 50)));
      nodes.add(new FlyingSwordNode(1, 5, 9, 35, 0, 0, 0, false, 0, new Point(140, -300)));
      nodes.add(new FlyingSwordNode(1, 5, 10, 35, 0, 0, 0, false, 0, new Point(1240, 100)));
      nodes.add(new FlyingSwordNode(1, 5, 11, 35, 0, 0, 0, false, 0, new Point(1640, -350)));
      nodes.add(new FlyingSwordNode(1, 5, 12, 35, 0, 0, 0, false, 0, new Point(690, 75)));
      nodes.add(new FlyingSwordNode(1, 5, 13, 35, 0, 0, 0, false, 0, new Point(-60, -25)));
      nodes.add(new FlyingSwordNode(1, 5, 14, 35, 0, 0, 0, false, 0, new Point(895, -25)));
      nodes.add(new FlyingSwordNode(1, 5, 15, 35, 0, 0, 0, false, 0, new Point(1690, -25)));
      nodes.add(new FlyingSwordNode(1, 5, 16, 35, 0, 0, 0, false, 0, new Point(1540, -400)));
      nodes.add(new FlyingSwordNode(2, 5, 17, 60, 500, 0, 0, false, 0, new Point(800, -200)));
      nodes.add(new FlyingSwordNode(2, 5, 18, 35, 0, 11000, 0, true, 0, new Point(0, 0)));
    } 
    return nodes;
  }
}
