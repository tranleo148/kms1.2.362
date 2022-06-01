package server.field.skill;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.util.concurrent.ScheduledFuture;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.SkillPacket;

public class MapleMagicSword extends MapleMapObject {
  private MapleCharacter chr;
  
  private int sourceid;
  
  private int swordcount;
  
  private int duration;
  
  private int type;
  
  private int delay;
  
  private int mobid;
  
  private boolean core = false;
  
  private ScheduledFuture<?> schedule = null;
  
  private Point pos;
  
  private Point pos2;
  
  public MapleMagicSword(MapleCharacter chr, int sourceid, int swordcount, int duration, boolean core) {
    setChr(chr);
    setSourceid(sourceid);
    setSwordCount(swordcount);
    setDuration(duration);
    setCore(core);
  }
  
  public MapleMagicSword(MapleCharacter chr, int type, int mobid, int delay, int sourceid, int duration, Point pos, Point pos2) {
    setChr(chr);
    setSType(type);
    setMobid(mobid);
    setDelay(delay);
    setSourceid(sourceid);
    setDuration(duration);
    setPos(pos);
    setPos2(pos2);
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.MagicSword;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.send(SkillPacket.CreateSworldObtacle(this));
  }
  
  public void sendDestroyData(MapleClient client) {}
  
  public MapleCharacter getChr() {
    return this.chr;
  }
  
  public void setChr(MapleCharacter chr) {
    this.chr = chr;
  }
  
  public int getSourceid() {
    return this.sourceid;
  }
  
  public void setSourceid(int sourceid) {
    this.sourceid = sourceid;
  }
  
  public int getSwordCount() {
    return this.swordcount;
  }
  
  public void setSwordCount(int swordcount) {
    this.swordcount = swordcount;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public boolean core() {
    return this.core;
  }
  
  public void setCore(boolean v) {
    this.core = v;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
  
  public int getSType() {
    return this.type;
  }
  
  public void setSType(int type) {
    this.type = type;
  }
  
  public int getMobid() {
    return this.mobid;
  }
  
  public void setMobid(int mobid) {
    this.mobid = mobid;
  }
  
  public int getDelay() {
    return this.delay;
  }
  
  public void setDelay(int delay) {
    this.delay = delay;
  }
  
  public Point getPos() {
    return this.pos;
  }
  
  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public Point getPos2() {
    return this.pos2;
  }
  
  public void setPos2(Point pos2) {
    this.pos2 = pos2;
  }
}
