package server.shops;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.Pair;
import tools.packet.MarriageEXPacket;
import tools.packet.PlayerShopPacket;

public abstract class AbstractPlayerStore extends MapleMapObject implements IMaplePlayerShop {
  protected boolean open = false;
  
  protected boolean available = false;
  
  protected String ownerName;
  
  protected String des;
  
  protected String pass;
  
  protected int ownerId;
  
  protected int owneraccount;
  
  protected int itemId;
  
  protected int channel;
  
  protected int map;
  
  protected AtomicLong meso = new AtomicLong(0L);
  
  protected WeakReference<MapleCharacter>[] chrs;
  
  protected List<String> visitors = new LinkedList<>();
  
  protected List<BoughtItem> bought = new LinkedList<>();
  
  protected List<MaplePlayerShopItem> items = new LinkedList<>();
  
  public AbstractPlayerStore(MapleCharacter owner, int itemId, String desc, String pass, int slots) {
    setPosition(owner.getTruePosition());
    this.ownerName = owner.getName();
    this.ownerId = owner.getId();
    this.owneraccount = owner.getAccountID();
    this.itemId = itemId;
    this.des = desc;
    this.pass = pass;
    this.map = owner.getMapId();
    this.channel = owner.getClient().getChannel();
    this.chrs = (WeakReference<MapleCharacter>[])new WeakReference[slots];
    for (int i = 0; i < this.chrs.length; i++)
      this.chrs[i] = new WeakReference<>(null); 
  }
  
  public int getMaxSize() {
    return this.chrs.length + 1;
  }
  
  public int getSize() {
    return (getFreeSlot() == -1) ? getMaxSize() : getFreeSlot();
  }
  
  public void broadcastToVisitors(byte[] packet) {
    broadcastToVisitors(packet, true);
  }
  
  public void broadcastToVisitors(byte[] packet, boolean owner) {
    for (WeakReference<MapleCharacter> chr : this.chrs) {
      if (chr != null && chr.get() != null)
        ((MapleCharacter)chr.get()).getClient().getSession().writeAndFlush(packet); 
    } 
    if (getShopType() != 1 && owner && getMCOwner() != null)
      getMCOwner().getClient().getSession().writeAndFlush(packet); 
  }
  
  public void broadcastToVisitors(byte[] packet, int exception) {
    for (WeakReference<MapleCharacter> chr : this.chrs) {
      if (chr != null && chr.get() != null && getVisitorSlot(chr.get()) != exception)
        ((MapleCharacter)chr.get()).getClient().getSession().writeAndFlush(packet); 
    } 
    if (getShopType() != 1 && getMCOwner() != null && exception != this.ownerId)
      getMCOwner().getClient().getSession().writeAndFlush(packet); 
  }
  
  public long getMeso() {
    return this.meso.get();
  }
  
  public void setMeso(long gainmeso) {
    this.meso.set(gainmeso);
  }
  
  public void setOpen(boolean open) {
    this.open = open;
  }
  
  public boolean isOpen() {
    return this.open;
  }
  
  public boolean saveItems() {
    if (getShopType() != 1)
      return false; 
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("DELETE FROM hiredmerch WHERE accountid = ? OR characterid = ?");
      ps.setInt(1, this.owneraccount);
      ps.setInt(2, this.ownerId);
      ps.executeUpdate();
      ps.close();
      ps = con.prepareStatement("INSERT INTO hiredmerch (characterid, accountid, Mesos, time) VALUES (?, ?, ?, ?)", 1);
      ps.setInt(1, this.ownerId);
      ps.setInt(2, this.owneraccount);
      ps.setLong(3, this.meso.get());
      ps.setLong(4, System.currentTimeMillis());
      ps.executeUpdate();
      rs = ps.getGeneratedKeys();
      if (!rs.next()) {
        rs.close();
        ps.close();
        throw new RuntimeException("Error, adding merchant to DB");
      } 
      int packageid = rs.getInt(1);
      rs.close();
      ps.close();
      Map<MapleInventoryType, List<Item>> iters = new HashMap<>();
      MapleInventoryType[] types = { MapleInventoryType.EQUIPPED, MapleInventoryType.EQUIP, MapleInventoryType.USE, MapleInventoryType.SETUP, MapleInventoryType.ETC, MapleInventoryType.CASH };
      for (MapleInventoryType type : types)
        iters.put(type, new ArrayList<>()); 
      for (MaplePlayerShopItem pItems : this.items) {
        if (pItems.item == null || pItems.bundles <= 0)
          continue; 
        if (pItems.item.getQuantity() <= 0 && !GameConstants.isRechargable(pItems.item.getItemId()))
          continue; 
        Item item = pItems.item.copy();
        item.setQuantity((short)(item.getQuantity() * pItems.bundles));
        ((List<Item>)iters.get(GameConstants.getInventoryType(item.getItemId()))).add(item);
      } 
      for (Map.Entry<MapleInventoryType, List<Item>> iter : iters.entrySet())
        ItemLoader.HIRED_MERCHANT.saveItems(iter.getValue(), packageid, iter.getKey(), false); 
      return true;
    } catch (SQLException se) {
      se.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
    return false;
  }
  
  public MapleCharacter getVisitor(int num) {
    return this.chrs[num].get();
  }
  
  public void update() {
    if (isAvailable() && 
      getMCOwner() != null)
      getMap().broadcastMessage(PlayerShopPacket.sendPlayerShopBox(getMCOwner())); 
  }
  
  public void addVisitor(MapleCharacter visitor) {
    int i = getFreeSlot();
    if (i > 0) {
      if (getShopType() >= 3) {
        if (getShopType() == 8) {
          broadcastToVisitors(MarriageEXPacket.MarriageVisit(visitor, i));
        } else {
          broadcastToVisitors(PlayerShopPacket.getMiniGameNewVisitor(visitor, i, (MapleMiniGame)this));
        } 
      } else {
        broadcastToVisitors(PlayerShopPacket.shopVisitorAdd(visitor, i));
      } 
      this.chrs[i - 1] = new WeakReference<>(visitor);
      if (!isOwner(visitor))
        this.visitors.add(visitor.getName()); 
      if (getItemId() >= 4080000 && getItemId() <= 4080100) {
        if (i == 1)
          update(); 
      } else if (i == 3) {
        update();
      } 
    } 
  }
  
  public void removeVisitor(MapleCharacter visitor) {
    byte slot = getVisitorSlot(visitor);
    boolean shouldUpdate = (getFreeSlot() == -1);
    if (slot > 0) {
      broadcastToVisitors(PlayerShopPacket.shopVisitorLeave(slot), slot);
      this.chrs[slot - 1] = new WeakReference<>(null);
      if (shouldUpdate)
        update(); 
    } 
  }
  
  public byte getVisitorSlot(MapleCharacter visitor) {
    for (byte i = 0; i < this.chrs.length; i = (byte)(i + 1)) {
      if (this.chrs[i] != null && this.chrs[i].get() != null && ((MapleCharacter)this.chrs[i].get()).getId() == visitor.getId())
        return (byte)(i + 1); 
    } 
    if (visitor.getId() == this.ownerId)
      return 0; 
    return -1;
  }
  
  public void removeAllVisitors(int error, int type) {
    for (int i = 0; i < this.chrs.length; i++) {
      MapleCharacter visitor = getVisitor(i);
      if (visitor != null) {
        if (type != -1)
          visitor.getClient().getSession().writeAndFlush(PlayerShopPacket.shopErrorMessage(error, type)); 
        broadcastToVisitors(PlayerShopPacket.shopVisitorLeave(getVisitorSlot(visitor)), getVisitorSlot(visitor));
        visitor.setPlayerShop(null);
        this.chrs[i] = new WeakReference<>(null);
      } 
    } 
    update();
  }
  
  public String getOwnerName() {
    return this.ownerName;
  }
  
  public int getOwnerId() {
    return this.ownerId;
  }
  
  public int getOwnerAccId() {
    return this.owneraccount;
  }
  
  public String getDescription() {
    if (this.des == null)
      return ""; 
    return this.des;
  }
  
  public List<Pair<Byte, MapleCharacter>> getVisitors() {
    List<Pair<Byte, MapleCharacter>> chrz = new LinkedList<>();
    for (byte i = 0; i < this.chrs.length; i = (byte)(i + 1)) {
      if (this.chrs[i] != null && this.chrs[i].get() != null)
        chrz.add(new Pair<>(Byte.valueOf((byte)(i + 1)), this.chrs[i].get())); 
    } 
    return chrz;
  }
  
  public List<MaplePlayerShopItem> getItems() {
    return this.items;
  }
  
  public void addItem(MaplePlayerShopItem item) {
    this.items.add(item);
  }
  
  public boolean removeItem(int item) {
    return false;
  }
  
  public void removeFromSlot(int slot) {
    this.items.remove(slot);
  }
  
  public byte getFreeSlot() {
    for (byte i = 0; i < this.chrs.length; i = (byte)(i + 1)) {
      if (this.chrs[i] == null || this.chrs[i].get() == null)
        return (byte)(i + 1); 
    } 
    return -1;
  }
  
  public int getItemId() {
    return this.itemId;
  }
  
  public boolean isOwner(MapleCharacter chr) {
    return (chr.getId() == this.ownerId && chr.getName().equals(this.ownerName));
  }
  
  public String getPassword() {
    if (this.pass == null)
      return ""; 
    return this.pass;
  }
  
  public void sendDestroyData(MapleClient client) {}
  
  public void sendSpawnData(MapleClient client) {}
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.SHOP;
  }
  
  public MapleCharacter getMCOwnerWorld() {
    int ourChannel = World.Find.findChannel(this.ownerId);
    if (ourChannel <= 0)
      return null; 
    return ChannelServer.getInstance(ourChannel).getPlayerStorage().getCharacterById(this.ownerId);
  }
  
  public MapleCharacter getMCOwnerChannel() {
    return ChannelServer.getInstance(this.channel).getPlayerStorage().getCharacterById(this.ownerId);
  }
  
  public MapleCharacter getMCOwner() {
    return getMap().getCharacterById(this.ownerId);
  }
  
  public MapleMap getMap() {
    return ChannelServer.getInstance(this.channel).getMapFactory().getMap(this.map);
  }
  
  public int getGameType() {
    if (getShopType() == 1)
      return 6; 
    if (getShopType() == 2)
      return 5; 
    if (getShopType() == 3)
      return 1; 
    if (getShopType() == 4)
      return 2; 
    if (getShopType() == 8)
      return 8; 
    return 0;
  }
  
  public boolean isAvailable() {
    return this.available;
  }
  
  public void setAvailable(boolean b) {
    this.available = b;
  }
  
  public List<BoughtItem> getBoughtItems() {
    return this.bought;
  }
  
  public String getMemberNames() {
    String ret = "";
    for (WeakReference<MapleCharacter> chr : this.chrs) {
      if (chr != null && chr.get() != null)
        ret = ret + ((MapleCharacter)chr.get()).getName() + ", "; 
    } 
    return ret;
  }
  
  public static final class BoughtItem {
    public int id;
    
    public int quantity;
    
    public long totalPrice;
    
    public String buyer;
    
    public BoughtItem(int id, int quantity, long totalPrice, String buyer) {
      this.id = id;
      this.quantity = quantity;
      this.totalPrice = totalPrice;
      this.buyer = buyer;
    }
  }
}
