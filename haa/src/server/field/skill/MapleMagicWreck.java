package server.field.skill;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.util.concurrent.ScheduledFuture;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.CField;

public class MapleMagicWreck extends MapleMapObject {
  private MapleCharacter chr;
  
  private int sourceid;
  
  private int duration;
  
  private ScheduledFuture<?> schedule = null;
  
  public MapleMagicWreck(MapleCharacter chr, int sourceid, Point pos, int duration) {
    setChr(chr);
    setSourceid(sourceid);
    setDuration(duration);
    setPosition(pos);
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.WRECK;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getPlayer().getMap().broadcastMessage(CField.createMagicWreck(this));
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
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
}
