package server.life;

import client.MapleCharacter;
import client.MapleClient;
import server.maps.MapleMapObjectType;
import server.shops.MapleShopFactory;
import tools.packet.CField;

public class MapleNPC extends AbstractLoadedMapleLife {
  private MapleCharacter owner = null;
  
  private boolean left = false;
  
  private String name = "MISSINGNO";
  
  private boolean custom = false;
  
  public MapleNPC(int id, String name) {
    super(id);
    this.name = name;
  }
  
  public final boolean hasShop() {
    return (MapleShopFactory.getInstance().getShopForNPC(getId()) != null);
  }
  
  public final void sendShop(MapleClient c) {
    MapleShopFactory.getInstance().getShopForNPC(getId()).sendShop(c);
  }
  
  public void sendSpawnData(MapleClient client) {
    if (getId() >= 9901000)
      return; 
    if (this.owner != null) {
      if (client.getPlayer().getId() == this.owner.getId()) {
        client.getSession().writeAndFlush(CField.NPCPacket.spawnNPC(this, true));
        client.getSession().writeAndFlush(CField.NPCPacket.spawnNPCRequestController(this, true));
      } 
    } else {
      client.getSession().writeAndFlush(CField.NPCPacket.spawnNPC(this, true));
      client.getSession().writeAndFlush(CField.NPCPacket.spawnNPCRequestController(this, true));
    } 
  }
  
  public final void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.NPCPacket.removeNPCController(getObjectId()));
    client.getSession().writeAndFlush(CField.NPCPacket.removeNPC(getObjectId()));
  }
  
  public final MapleMapObjectType getType() {
    return MapleMapObjectType.NPC;
  }
  
  public final String getName() {
    return this.name;
  }
  
  public void setName(String n) {
    this.name = n;
  }
  
  public final boolean isCustom() {
    return this.custom;
  }
  
  public final void setCustom(boolean custom) {
    this.custom = custom;
  }
  
  public MapleCharacter getOwner() {
    return this.owner;
  }
  
  public void setOwner(MapleCharacter owner) {
    this.owner = owner;
  }
  
  public boolean isLeft() {
    return this.left;
  }
  
  public void setLeft(boolean left) {
    this.left = left;
  }
}
