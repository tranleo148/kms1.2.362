package server.field.skill;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.util.concurrent.ScheduledFuture;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.CField;

public class MapleFieldAttackObj extends MapleMapObject {
  private boolean facingleft;
  
  private MapleCharacter chr;
  
  private int sourceid;
  
  private int duration;
  
  private ScheduledFuture<?> schedule = null;
  
  public MapleFieldAttackObj(MapleCharacter chr, int sourceid, boolean facingleft, Point pos, int duration) {
    setChr(chr);
    setSourceid(sourceid);
    setDuration(duration);
    setPosition(pos);
    setFacingleft(facingleft);
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.FIELD;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getPlayer().getMap().broadcastMessage(CField.AttackObjPacket.ObjCreatePacket(this));
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
  
  public void onSetAttack(MapleClient client) {
    client.getSession().writeAndFlush(CField.AttackObjPacket.OnSetAttack(this));
  }
  
  public boolean isFacingleft() {
    return this.facingleft;
  }
  
  public void setFacingleft(boolean facingleft) {
    this.facingleft = facingleft;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
}
