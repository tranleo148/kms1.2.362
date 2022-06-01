package server.field.boss.demian;

import client.MapleClient;
import java.util.concurrent.ScheduledFuture;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.MobPacket;

public class MapleIncinerateObject extends MapleMapObject {
  private int x;
  
  private int y;
  
  private ScheduledFuture<?> schedule;
  
  public MapleIncinerateObject(int x, int y) {
    setX(x);
    setY(y);
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
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.INCINERATE;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(MobPacket.incinerateObject(this, true));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(MobPacket.incinerateObject(this, false));
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
}
