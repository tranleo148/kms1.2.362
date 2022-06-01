package server;

import client.MapleClient;
import client.inventory.Item;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import database.DatabaseException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tools.packet.CField;

public class MapleStorage implements Serializable {
  private static final long serialVersionUID = 9179541993413738569L;
  
  private int id;
  
  private int accountId;
  
  private List<Item> items;
  
  private long meso;
  
  private int lastNPC = 0;
  
  private short slots = 128;
  
  private boolean changed = false;
  
  private Map<MapleInventoryType, List<Item>> typeItems = new EnumMap<>(MapleInventoryType.class);
  
  private MapleStorage(int id, short slots, long meso, int accountId) {
    this.id = id;
    this.slots = 128;
    this.items = new LinkedList<>();
    this.meso = meso;
    this.accountId = accountId;
  }
  
  public static int create(int id) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("INSERT INTO storages (accountid, slots, meso) VALUES (?, ?, ?)", 1);
      ps.setInt(1, id);
      ps.setInt(2, 4);
      ps.setLong(3, 0L);
      ps.executeUpdate();
      rs = ps.getGeneratedKeys();
      if (rs.next()) {
        int storageid = rs.getInt(1);
        ps.close();
        rs.close();
        con.close();
        return storageid;
      } 
      ps.close();
      rs.close();
      con.close();
      throw new DatabaseException("Inserting char failed.");
    } catch (Exception exception) {
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
    return 0;
  }
  
  public static MapleStorage loadStorage(int id) {
    MapleStorage ret = null;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM storages WHERE accountid = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();
      if (rs.next()) {
        int storeId = rs.getInt("storageid");
        ret = new MapleStorage(storeId, rs.getShort("slots"), rs.getLong("meso"), id);
        rs.close();
        ps.close();
        for (MapleInventoryType type : MapleInventoryType.values()) {
          if (type.getType() > 0)
            for (Map.Entry<Long, Item> mit : ItemLoader.STORAGE.loadItems(false, id, type).entrySet())
              ret.items.add(mit.getValue());  
        } 
      } else {
        int storeId = create(id);
        ret = new MapleStorage(storeId, (short)4, 0L, id);
        rs.close();
        ps.close();
      } 
      con.close();
    } catch (SQLException ex) {
      System.err.println("Error loading storage" + ex);
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
    return ret;
  }
  
  public void saveToDB(Connection con) {
    if (!this.changed)
      return; 
    try {
      PreparedStatement ps = con.prepareStatement("UPDATE storages SET slots = ?, meso = ? WHERE storageid = ?");
      ps.setInt(1, 128);
      ps.setLong(2, this.meso);
      ps.setInt(3, this.id);
      ps.executeUpdate();
      ps.close();
      Map<MapleInventoryType, List<Item>> listing = new HashMap<>();
      MapleInventoryType[] types = { MapleInventoryType.EQUIP, MapleInventoryType.USE, MapleInventoryType.SETUP, MapleInventoryType.ETC, MapleInventoryType.CASH, MapleInventoryType.CODY };
      for (MapleInventoryType type : types)
        listing.put(type, new ArrayList<>()); 
      for (Item item : this.items)
        ((List<Item>)listing.get(GameConstants.getInventoryType(item.getItemId()))).add(item); 
      for (Map.Entry<MapleInventoryType, List<Item>> iter : listing.entrySet()) {
        if (con != null) {
          ItemLoader.STORAGE.saveItems(iter.getValue(), con, this.accountId, iter.getKey(), false);
          continue;
        } 
        ItemLoader.STORAGE.saveItems(iter.getValue(), this.accountId, iter.getKey(), false);
      } 
      this.changed = false;
    } catch (SQLException ex) {
      System.err.println("Error saving storage" + ex);
    } 
  }
  
  public Item takeOut(byte slot) {
    if (slot >= this.items.size() || slot < 0)
      return null; 
    this.changed = true;
    Item ret = this.items.remove(slot);
    MapleInventoryType type = GameConstants.getInventoryType(ret.getItemId());
    this.typeItems.put(type, filterItems(type));
    return ret;
  }
  
  public void store(Item item) {
    this.changed = true;
    this.items.add(item);
    MapleInventoryType type = GameConstants.getInventoryType(item.getItemId());
    this.typeItems.put(type, filterItems(type));
  }
  
  public void arrange() {
    Collections.sort(this.items, (o1, o2) -> (o1.getItemId() < o2.getItemId()) ? -1 : ((o1.getItemId() == o2.getItemId()) ? 0 : 1));
    for (MapleInventoryType type : MapleInventoryType.values())
      this.typeItems.put(type, this.items); 
  }
  
  public List<Item> getItems() {
    return Collections.unmodifiableList(this.items);
  }
  
  private List<Item> filterItems(MapleInventoryType type) {
    List<Item> ret = new ArrayList<>();
    for (Item item : this.items) {
      if (GameConstants.getInventoryType(item.getItemId()) == type)
        ret.add(item); 
    } 
    return ret;
  }
  
  public byte getSlot(MapleInventoryType type, byte slot) {
    byte ret = 0;
    List<Item> it = this.typeItems.get(type);
    if (it == null || slot >= it.size() || slot < 0)
      return -1; 
    for (Item item : this.items) {
      if (item == it.get(slot))
        return ret; 
      ret = (byte)(ret + 1);
    } 
    return -1;
  }
  
  public void sendStorage(MapleClient c, int npcId) {
    this.lastNPC = npcId;
    Collections.sort(this.items, (o1, o2) -> (GameConstants.getInventoryType(o1.getItemId()).getType() < GameConstants.getInventoryType(o2.getItemId()).getType()) ? -1 : ((GameConstants.getInventoryType(o1.getItemId()) == GameConstants.getInventoryType(o2.getItemId())) ? 0 : 1));
    for (MapleInventoryType type : MapleInventoryType.values())
      this.typeItems.put(type, this.items); 
    c.getSession().writeAndFlush(CField.NPCPacket.getStorage(npcId, this.slots, this.items, this.meso));
  }
  
  public void update(MapleClient c) {
    c.getSession().writeAndFlush(CField.NPCPacket.arrangeStorage(this.slots, this.items, true));
  }
  
  public void sendStored(MapleClient c, MapleInventoryType type) {
    c.getSession().writeAndFlush(CField.NPCPacket.storeStorage(this.slots, type, this.typeItems.get(type)));
  }
  
  public void sendTakenOut(MapleClient c, MapleInventoryType type) {
    c.getSession().writeAndFlush(CField.NPCPacket.takeOutStorage(this.slots, type, this.typeItems.get(type)));
  }
  
  public long getMeso() {
    return this.meso;
  }
  
  public Item findById(int itemId) {
    for (Item item : this.items) {
      if (item.getItemId() == itemId)
        return item; 
    } 
    return null;
  }
  
  public void setMeso(long meso) {
    if (meso < 0L)
      return; 
    this.changed = true;
    this.meso = meso;
  }
  
  public void sendMeso(MapleClient c) {
    c.getSession().writeAndFlush(CField.NPCPacket.mesoStorage(this.slots, this.meso));
  }
  
  public boolean isFull() {
    return (this.items.size() >= this.slots);
  }
  
  public int getSlots() {
    return this.slots;
  }
  
  public void increaseSlots(byte gain) {
    this.changed = true;
    this.slots = (short)(this.slots + gain);
  }
  
  public void setSlots(byte set) {
    this.changed = true;
    this.slots = (short)set;
  }
  
  public void close() {
    this.typeItems.clear();
  }
}
