package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import tools.packet.CField;

public class MapleExtractor extends MapleMapObject {
  public int owner;
  
  public int timeLeft;
  
  public int itemId;
  
  public int fee;
  
  public long startTime;
  
  public String ownerName;
  
  public MapleExtractor(MapleCharacter owner, int itemId, int fee, int timeLeft) {
    this.owner = owner.getId();
    this.itemId = itemId;
    this.fee = fee;
    this.ownerName = owner.getName();
    this.startTime = System.currentTimeMillis();
    this.timeLeft = timeLeft;
    setPosition(owner.getPosition());
  }
  
  public int getTimeLeft() {
    return this.timeLeft;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.makeExtractor(this.owner, this.ownerName, getTruePosition(), getTimeLeft(), this.itemId, this.fee));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.removeExtractor(this.owner));
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.EXTRACTOR;
  }
}
