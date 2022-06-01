package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import client.SecondaryStat;
import client.inventory.Equip;
import client.inventory.Item;
import java.awt.Point;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import server.SecondaryStatEffect;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleMapItem extends MapleMapObject {
  protected Item item;
  
  protected MapleMapObject dropper;
  
  protected MapleCharacter owner;
  
  protected int meso = 0;
  
  protected int questid = -1;
  
  private int flyingSpeed = 0;
  
  private int flyingAngle = 0;
  
  private int publicDropId = 0;
  
  protected byte type;
  
  protected boolean pickedUp = false;
  
  protected boolean playerDrop;
  
  protected boolean randDrop = false;
  
  private boolean flyingDrop = false;
  
  private boolean touchDrop = false;
  
  private boolean pickpoket = false;
  
  protected long nextExpiry = 0L;
  
  protected long nextFFA = 0L;
  
  protected Equip equip;
  
  private ReentrantLock lock = new ReentrantLock();
  
  public MapleMapItem(Item item, Point position, MapleMapObject dropper, MapleCharacter owner, byte type, boolean playerDrop) {
    setPosition(position);
    this.item = item;
    this.dropper = dropper;
    this.owner = owner;
    this.type = type;
    this.playerDrop = playerDrop;
  }
  
  public MapleMapItem(Item item, Point position, MapleMapObject dropper, MapleCharacter owner, byte type, boolean playerDrop, Equip equip) {
    setPosition(position);
    this.item = item;
    this.dropper = dropper;
    this.owner = owner;
    this.type = type;
    this.playerDrop = playerDrop;
    this.equip = equip;
  }
  
  public MapleMapItem(Item item, Point position, MapleMapObject dropper, MapleCharacter owner, byte type, boolean playerDrop, int questid) {
    setPosition(position);
    this.item = item;
    this.dropper = dropper;
    this.owner = owner;
    this.type = type;
    this.playerDrop = playerDrop;
    this.questid = questid;
  }
  
  public MapleMapItem(int meso, Point position, MapleMapObject dropper, MapleCharacter owner, byte type, boolean playerDrop) {
    setPosition(position);
    this.item = null;
    this.dropper = dropper;
    this.owner = owner;
    this.meso = meso;
    this.type = type;
    this.playerDrop = playerDrop;
  }
  
  public MapleMapItem(Point position, Item item) {
    setPosition(position);
    this.item = item;
    this.owner = null;
    this.type = 2;
    this.playerDrop = false;
    this.randDrop = true;
  }
  
  public final Item getItem() {
    return this.item;
  }
  
  public void setItem(Item z) {
    this.item = z;
  }
  
  public final int getQuest() {
    return this.questid;
  }
  
  public final int getItemId() {
    if (getMeso() > 0)
      return this.meso; 
    return this.item.getItemId();
  }
  
  public final MapleMapObject getDropper() {
    return this.dropper;
  }
  
  public final int getOwner() {
    return this.owner.getId();
  }
  
  public final int getMeso() {
    return this.meso;
  }
  
  public final boolean isPlayerDrop() {
    return this.playerDrop;
  }
  
  public final boolean isPickedUp() {
    return this.pickedUp;
  }
  
  public void setPickedUp(boolean pickedUp) {
    this.pickedUp = pickedUp;
  }
  
  public byte getDropType() {
    return this.type;
  }
  
  public void setDropType(byte z) {
    this.type = z;
  }
  
  public final boolean isRandDrop() {
    return this.randDrop;
  }
  
  public final MapleMapObjectType getType() {
    return MapleMapObjectType.ITEM;
  }
  
  public void sendSpawnData(MapleClient client) {
    if ((this.questid <= 0 || client.getPlayer().getQuestStatus(this.questid) == 1) && (
      this.publicDropId <= 0 || (this.publicDropId > 0 && client.getAccID() == this.publicDropId)))
      client.getSession().writeAndFlush(CField.dropItemFromMapObject(client.getPlayer().getMap(), this, null, getTruePosition(), (byte)2, (client.getPlayer().getBuffedEffect(SecondaryStat.PickPocket) != null))); 
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.removeItemFromMap(getObjectId(), 1, 0));
  }
  
  public Lock getLock() {
    return this.lock;
  }
  
  public void registerExpire(long time) {
    this.nextExpiry = System.currentTimeMillis() + time;
  }
  
  public void registerFFA(long time) {
    this.nextFFA = System.currentTimeMillis() + time;
  }
  
  public boolean shouldExpire(long now) {
    return (!this.pickedUp && this.nextExpiry > 0L && this.nextExpiry < now);
  }
  
  public boolean shouldFFA(long now) {
    return (!this.pickedUp && this.type < 2 && this.nextFFA > 0L && this.nextFFA < now);
  }
  
  public boolean hasFFA() {
    return (this.nextFFA > 0L);
  }
  
  public void expire(MapleMap map) {
    this.pickedUp = true;
    map.broadcastMessage(CField.removeItemFromMap(getObjectId(), 0, 0));
    map.removeMapObject(this);
    if (this.randDrop)
      map.spawnRandDrop(); 
    if (this.owner != null) {
      SecondaryStatEffect pickPocket = this.owner.getBuffedEffect(SecondaryStat.PickPocket);
      if (pickPocket != null) {
        this.owner.RemovePickPocket(this);
        this.owner.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(pickPocket.getStatups(), pickPocket, this.owner));
      } 
    } 
  }
  
  public void setEquip(Equip equip) {
    this.equip = equip;
  }
  
  public final Equip getEquip() {
    return this.equip;
  }
  
  public boolean isFlyingDrop() {
    return this.flyingDrop;
  }
  
  public void setFlyingDrop(boolean flyingDrop) {
    this.flyingDrop = flyingDrop;
  }
  
  public int getFlyingSpeed() {
    return this.flyingSpeed;
  }
  
  public void setFlyingSpeed(int flyingSpeed) {
    this.flyingSpeed = flyingSpeed;
  }
  
  public int getFlyingAngle() {
    return this.flyingAngle;
  }
  
  public void setFlyingAngle(int flyingAngle) {
    this.flyingAngle = flyingAngle;
  }
  
  public boolean isTouchDrop() {
    return this.touchDrop;
  }
  
  public void setTouchDrop(boolean touchDrop) {
    this.touchDrop = touchDrop;
  }
  
  public int getPublicDropId() {
    return this.publicDropId;
  }
  
  public void setPublicDropId(int publicDropId) {
    this.publicDropId = publicDropId;
  }
  
  public boolean isPickpoket() {
    return this.pickpoket;
  }
  
  public void setPickpoket(boolean pickpoket) {
    this.pickpoket = pickpoket;
  }
}
