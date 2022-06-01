package client.inventory;

import constants.GameConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapleInventory implements Iterable<Item>, Serializable {
  private Map<Short, Item> inventory;
  
  private short slotLimit = 0;
  
  private MapleInventoryType type;
  
  public MapleInventory(MapleInventoryType type) {
    this.inventory = new LinkedHashMap<>();
    this.type = type;
  }
  
  public void addSlot(short slot) {
    this.slotLimit = (short)(this.slotLimit + slot);
    if (this.slotLimit > 128)
      this.slotLimit = 128; 
  }
  
  public short getSlotLimit() {
    return this.slotLimit;
  }
  
  public void setSlotLimit(short slot) {
    if (slot > 128)
      slot = 128; 
    this.slotLimit = slot;
  }
  
  public Item findById(int itemId) {
    for (Item item : this.inventory.values()) {
      if (item.getItemId() == itemId)
        return item; 
    } 
    return null;
  }
  
  public Item findByUniqueId(long uniqueId) {
    for (Item item : this.inventory.values()) {
      if (item.getUniqueId() == uniqueId)
        return item; 
    } 
    return null;
  }
  
  public Item findByInventoryId(long itemId, int itemI) {
    for (Item item : this.inventory.values()) {
      if (item.getInventoryId() == itemId && item.getItemId() == itemI)
        return item; 
    } 
    return findById(itemI);
  }
  
  public Item findByInventoryIdOnly(long itemId, int itemI) {
    for (Item item : this.inventory.values()) {
      if (item.getInventoryId() == itemId && item.getItemId() == itemI)
        return item; 
    } 
    return null;
  }
  
  public int countById(int itemId) {
    int possesed = 0;
    for (Item item : this.inventory.values()) {
      if (item.getItemId() == itemId)
        possesed += item.getQuantity(); 
    } 
    return possesed;
  }
  
  public List<Item> listById(int itemId) {
    List<Item> ret = new ArrayList<>();
    for (Item item : this.inventory.values()) {
      if (item.getItemId() == itemId)
        ret.add(item); 
    } 
    if (ret.size() > 1)
      Collections.sort(ret); 
    return ret;
  }
  
  public Collection<Item> list() {
    return this.inventory.values();
  }
  
  public Map<Short, Item> lists() {
    return this.inventory;
  }
  
  public List<Item> newList() {
    if (this.inventory.size() <= 0)
      return Collections.emptyList(); 
    return new LinkedList<>(this.inventory.values());
  }
  
  public List<Integer> listIds() {
    List<Integer> ret = new ArrayList<>();
    for (Item item : this.inventory.values()) {
      if (!ret.contains(Integer.valueOf(item.getItemId())))
        ret.add(Integer.valueOf(item.getItemId())); 
    } 
    if (ret.size() > 1)
      Collections.sort(ret); 
    return ret;
  }
  
  public short addItem(Item item) {
    short slotId = getNextFreeSlot();
    if (slotId < 0)
      return -1; 
    this.inventory.put(Short.valueOf(slotId), item);
    item.setPosition(slotId);
    return slotId;
  }
  
  public void addFromDB(Item item) {
    if (item.getPosition() < 0 && !this.type.equals(MapleInventoryType.EQUIPPED))
      return; 
    if (item.getPosition() > 0 && this.type.equals(MapleInventoryType.EQUIPPED))
      return; 
    this.inventory.put(Short.valueOf(item.getPosition()), item);
  }
  
  public void move(short sSlot, short dSlot, short slotMax) {
    Item source = this.inventory.get(Short.valueOf(sSlot));
    Item target = this.inventory.get(Short.valueOf(dSlot));
    if (source == null)
      throw new InventoryException("Trying to move empty slot"); 
    if (target == null) {
      if (dSlot < 0 && !this.type.equals(MapleInventoryType.EQUIPPED))
        return; 
      if (dSlot > 0 && this.type.equals(MapleInventoryType.EQUIPPED))
        return; 
      source.setPosition(dSlot);
      this.inventory.put(Short.valueOf(dSlot), source);
      this.inventory.remove(Short.valueOf(sSlot));
    } else if (target.getItemId() == source.getItemId() && !GameConstants.isThrowingStar(source.getItemId()) && !GameConstants.isBullet(source.getItemId()) && target.getOwner().equals(source.getOwner()) && target.getExpiration() == source.getExpiration()) {
      if (this.type.getType() == MapleInventoryType.EQUIP.getType() || this.type.getType() == MapleInventoryType.CASH.getType()) {
        swap(target, source);
      } else if (source.getQuantity() + target.getQuantity() > slotMax) {
        source.setQuantity((short)(source.getQuantity() + target.getQuantity() - slotMax));
        target.setQuantity(slotMax);
      } else {
        target.setQuantity((short)(source.getQuantity() + target.getQuantity()));
        this.inventory.remove(Short.valueOf(sSlot));
      } 
    } else {
      swap(target, source);
    } 
  }
  
  private void swap(Item source, Item target) {
    this.inventory.remove(Short.valueOf(source.getPosition()));
    this.inventory.remove(Short.valueOf(target.getPosition()));
    short swapPos = source.getPosition();
    source.setPosition(target.getPosition());
    target.setPosition(swapPos);
    this.inventory.put(Short.valueOf(source.getPosition()), source);
    this.inventory.put(Short.valueOf(target.getPosition()), target);
  }
  
  public Item getItem(short slot) {
    return this.inventory.get(Short.valueOf(slot));
  }
  
  public void removeItem(short slot) {
    removeItem(slot, (short)1, false);
  }
  
  public void removeItem(short slot, short quantity, boolean allowZero) {
    Item item = this.inventory.get(Short.valueOf(slot));
    if (item == null)
      return; 
    item.setQuantity((short)(item.getQuantity() - quantity));
    if (item.getQuantity() < 0)
      item.setQuantity((short)0); 
    if (item.getQuantity() == 0 && !allowZero)
      removeSlot(slot); 
  }
  
  public void removeSlot(short slot) {
    this.inventory.remove(Short.valueOf(slot));
  }
  
  public boolean isFull() {
    return (this.inventory.size() >= this.slotLimit);
  }
  
  public boolean isFull(int margin) {
    return (this.inventory.size() + margin >= this.slotLimit);
  }
  
  public short getNextFreeSlot() {
    if (isFull())
      return -1; 
    for (short i = 1; i <= this.slotLimit; i = (short)(i + 1)) {
      if (!this.inventory.containsKey(Short.valueOf(i)))
        return i; 
    } 
    return -1;
  }
  
  public short getNumFreeSlot() {
    if (isFull())
      return 0; 
    short free = 0;
    for (short i = 1; i <= this.slotLimit; i = (short)(i + 1)) {
      if (!this.inventory.containsKey(Short.valueOf(i)))
        free = (short)(free + 1); 
    } 
    return free;
  }
  
  public short getNextItemSlot(short k) {
    if (isFull())
      return -1; 
    for (short i = k; i <= this.slotLimit; i = (short)(i + 1)) {
      if (!this.inventory.containsKey(Short.valueOf(i)))
        return i; 
    } 
    return -1;
  }
  
  public MapleInventoryType getType() {
    return this.type;
  }
  
  public Iterator<Item> iterator() {
    return Collections.<Item>unmodifiableCollection(this.inventory.values()).iterator();
  }
}
