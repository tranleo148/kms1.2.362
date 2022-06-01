package client.inventory;

import constants.GameConstants;
import java.io.Serializable;
import server.enchant.StarForceStats;
import server.maps.BossReward;

public class Item implements Comparable<Item>, Serializable {
  private int id;
  
  private short position;
  
  private short quantity;
  
  private int flag;
  
  private long expiration = -1L;
  
  private long inventoryitemid = 0L;
  
  private MaplePet pet = null;
  
  private MapleAndroid android = null;
  
  private long uniqueid;
  
  private String owner = "";
  
  private String GameMaster_log = "";
  
  private String giftFrom = "";
  
  private MapleRing ring = null;
  
  private int marriageId = 0, bossid = 0;
  
  private BossReward reward;
  
  private StarForceStats showScrollOption = null;
  
  public Item(int id, short position, short quantity, int flag, long uniqueid) {
    this.id = id;
    this.position = position;
    this.quantity = quantity;
    this.flag = flag;
    this.uniqueid = uniqueid;
  }
  
  public Item(int id, short position, short quantity, int flag) {
    this.id = id;
    this.position = position;
    this.quantity = quantity;
    this.flag = flag;
    this.uniqueid = -1L;
  }
  
  public Item(int id, short position, short quantity) {
    this.id = id;
    this.position = position;
    this.quantity = quantity;
    this.uniqueid = -1L;
  }
  
  public Item copy() {
    Item ret = new Item(this.id, this.position, this.quantity, this.flag, this.uniqueid);
    ret.inventoryitemid = this.inventoryitemid;
    ret.pet = this.pet;
    ret.android = this.android;
    ret.owner = this.owner;
    ret.GameMaster_log = this.GameMaster_log;
    ret.expiration = this.expiration;
    ret.giftFrom = this.giftFrom;
    ret.marriageId = this.marriageId;
    ret.reward = this.reward;
    return ret;
  }
  
  public Item copyWithQuantity(short qq) {
    Item ret = new Item(this.id, this.position, qq, this.flag, this.uniqueid);
    ret.pet = this.pet;
    ret.owner = this.owner;
    ret.GameMaster_log = this.GameMaster_log;
    ret.expiration = this.expiration;
    ret.marriageId = this.marriageId;
    ret.giftFrom = this.giftFrom;
    ret.reward = this.reward;
    return ret;
  }
  
  public final void setPosition(short position) {
    this.position = position;
    if (this.pet != null)
      this.pet.setInventoryPosition(position); 
  }
  
  public void setQuantity(short quantity) {
    this.quantity = quantity;
  }
  
  public final int getItemId() {
    return this.id;
  }
  
  public final int setItemId(int id) {
    this.id = id;
    return id;
  }
  
  public final short getPosition() {
    return this.position;
  }
  
  public final int getFlag() {
    return this.flag;
  }
  
  public void setMarriageId(int m) {
    this.marriageId = m;
  }
  
  public int getMarriageId() {
    return this.marriageId;
  }
  
  public final short getQuantity() {
    return this.quantity;
  }
  
  public byte getType() {
    return 2;
  }
  
  public final String getOwner() {
    return this.owner;
  }
  
  public final void setOwner(String owner) {
    this.owner = owner;
  }
  
  public final void setFlag(int flag) {
    this.flag = flag;
  }
  
  public final long getExpiration() {
    return this.expiration;
  }
  
  public final void setExpiration(long expire) {
    this.expiration = expire;
  }
  
  public final String getGMLog() {
    return this.GameMaster_log;
  }
  
  public void setGMLog(String GameMaster_log) {
    this.GameMaster_log = GameMaster_log;
  }
  
  public final long getUniqueId() {
    return this.uniqueid;
  }
  
  public void setUniqueId(long ui) {
    this.uniqueid = ui;
  }
  
  public MapleRing getRing() {
    if (!GameConstants.isEffectRing(getItemId()) || getUniqueId() <= 0L)
      return null; 
    if (this.ring == null)
      this.ring = MapleRing.loadFromDb(getUniqueId(), (getPosition() < 0)); 
    return this.ring;
  }
  
  public void setRing(MapleRing ring) {
    this.ring = ring;
  }
  
  public final long getInventoryId() {
    return this.inventoryitemid;
  }
  
  public void setInventoryId(long ui) {
    this.inventoryitemid = ui;
  }
  
  public final MaplePet getPet() {
    return this.pet;
  }
  
  public final void setPet(MaplePet pet) {
    this.pet = pet;
    if (pet != null)
      this.uniqueid = pet.getUniqueId(); 
  }
  
  public final MapleAndroid getAndroid() {
    if (getItemId() / 10000 != 166 || getUniqueId() <= 0L)
      return null; 
    if (this.android == null)
      this.android = MapleAndroid.loadFromDb(getItemId(), getUniqueId()); 
    return this.android;
  }
  
  public final void setAndroid(MapleAndroid android) {
    this.android = android;
  }
  
  public void setGiftFrom(String gf) {
    this.giftFrom = gf;
  }
  
  public String getGiftFrom() {
    return this.giftFrom;
  }
  
  public StarForceStats getShowScrollOption() {
    return this.showScrollOption;
  }
  
  public void setShowScrollOption(StarForceStats showScrollOption) {
    this.showScrollOption = showScrollOption;
  }
  
  public int compareTo(Item other) {
    if (Math.abs(this.position) < Math.abs(other.getPosition()))
      return -1; 
    if (Math.abs(this.position) == Math.abs(other.getPosition()))
      return 0; 
    return 1;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof Item))
      return false; 
    Item ite = (Item)obj;
    return (this.uniqueid == ite.getUniqueId() && this.id == ite.getItemId() && this.quantity == ite.getQuantity() && Math.abs(this.position) == Math.abs(ite.getPosition()));
  }
  
  public String toString() {
    return "Item: " + this.id + " quantity: " + this.quantity;
  }
  
    public BossReward getReward() {
         return this.reward;
    }

    public void setReward(BossReward reward) {
         this.reward = reward;
    }

    public int getBossid() {
        return bossid;
    }

    public void setBossid(int bossid) {
        this.bossid = bossid;
    }
}
