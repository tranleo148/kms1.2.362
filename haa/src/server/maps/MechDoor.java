package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.util.concurrent.ScheduledFuture;
import tools.packet.CField;

public class MechDoor extends MapleMapObject {
  private int owner;
  
  private int partyid;
  
  private int id;
  
  private int duration;
  
  private ScheduledFuture<?> schedule = null;
  
  public MechDoor(MapleCharacter owner, Point pos, int id, int newDuration) {
    this.owner = owner.getId();
    this.partyid = (owner.getParty() == null) ? 0 : owner.getParty().getId();
    setPosition(pos);
    this.id = id;
    this.duration = newDuration;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.spawnMechDoor(this, false));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.removeMechDoor(this, false));
  }
  
  public int getOwnerId() {
    return this.owner;
  }
  
  public int getPartyId() {
    return this.partyid;
  }
  
  public int getId() {
    return this.id;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.DOOR;
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
