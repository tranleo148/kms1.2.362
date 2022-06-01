package server.field.skill;

import client.MapleClient;
import java.awt.Point;
import java.util.Arrays;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.CField;

public class MapleOrb extends MapleMapObject {
  private int cid;
  
  private int type;
  
  private int skillId;
  
  private int attackCount;
  
  private int subTime;
  
  private int duration;
  
  private int delay;
  
  private int unk1;
  
  private int unk2;
  
  private int facing;
  
  private int unk3;
  
  private Point pos;
  
  public MapleOrb(int cid, int type, int skillId, int attackCount, int subTime, int duration, int delay, int unk1, int unk2, Point pos, int facing, int unk3) {
    this.cid = cid;
    this.type = type;
    this.skillId = skillId;
    this.attackCount = attackCount;
    this.subTime = subTime;
    this.duration = duration;
    this.delay = delay;
    this.unk1 = unk1;
    this.unk2 = unk2;
    this.pos = pos;
    this.facing = facing;
    this.unk3 = unk3;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.ORB;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.spawnOrb(this.cid, Arrays.asList(new MapleOrb[] { this })));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.removeOrb(this.cid, Arrays.asList(new MapleOrb[] { this })));
  }
  
  public int getPlayerId() {
    return this.cid;
  }
  
  public int getOrbType() {
    return this.type;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public int getAttackCount() {
    return this.attackCount;
  }
  
  public int getSubTime() {
    return this.subTime;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public int getDelay() {
    return this.delay;
  }
  
  public int getUnk1() {
    return this.unk1;
  }
  
  public int getUnk2() {
    return this.unk2;
  }
  
  public Point getPos() {
    return this.pos;
  }
  
  public int getFacing() {
    return this.facing;
  }
  
  public int getUnk3() {
    return this.unk3;
  }
}
