package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import tools.packet.CField;

public class MapleDragon extends AnimatedMapleMapObject {
  private int owner;
  
  private int jobid;
  
  public MapleDragon(MapleCharacter owner) {
    this.owner = owner.getId();
    this.jobid = owner.getJob();
    if (this.jobid < 2200 || this.jobid > 2218)
      throw new RuntimeException("Trying to create a dragon for a non-Evan"); 
    setPosition(owner.getTruePosition());
    setStance(4);
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.spawnDragon(this));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.removeDragon(this.owner));
  }
  
  public int getOwner() {
    return this.owner;
  }
  
  public int getJobId() {
    return this.jobid;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.SUMMON;
  }
}
