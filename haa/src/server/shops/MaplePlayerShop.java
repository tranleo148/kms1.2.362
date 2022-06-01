package server.shops;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.ItemFlag;
import java.util.ArrayList;
import java.util.List;
import server.MapleInventoryManipulator;
import tools.packet.PlayerShopPacket;

public class MaplePlayerShop extends AbstractPlayerStore {
  private int boughtnumber = 0;
  
  private List<String> bannedList = new ArrayList<>();
  
  public MaplePlayerShop(MapleCharacter owner, int itemId, String desc) {
    super(owner, itemId, desc, "", 3);
  }
  
  public void buy(MapleClient c, int item, short quantity) {
    MaplePlayerShopItem pItem = this.items.get(item);
    if (pItem.bundles > 0) {
      Item newItem = pItem.item.copy();
      newItem.setQuantity((short)(quantity * newItem.getQuantity()));
      int flag = newItem.getFlag();
      if (ItemFlag.KARMA_EQUIP.check(flag)) {
        newItem.setFlag(flag - ItemFlag.KARMA_EQUIP.getValue());
      } else if (ItemFlag.KARMA_USE.check(flag)) {
        newItem.setFlag(flag - ItemFlag.KARMA_USE.getValue());
      } 
      long gainmeso = pItem.price * quantity;
      if (c.getPlayer().getMeso() >= gainmeso) {
        if (getMCOwner().getMeso() + gainmeso > 0L && MapleInventoryManipulator.checkSpace(c, newItem.getItemId(), newItem.getQuantity(), newItem.getOwner()) && MapleInventoryManipulator.addFromDrop(c, newItem, false)) {
          pItem.bundles = (short)(pItem.bundles - quantity);
          this.bought.add(new AbstractPlayerStore.BoughtItem(newItem.getItemId(), quantity, gainmeso, c.getPlayer().getName()));
          c.getPlayer().gainMeso(-gainmeso, false);
          getMCOwner().gainMeso(gainmeso, false);
          if (pItem.bundles <= 0) {
            this.boughtnumber++;
            if (this.boughtnumber == this.items.size()) {
              closeShop(false, true);
              return;
            } 
          } 
        } else {
          c.getPlayer().dropMessage(1, "Your inventory is full.");
        } 
      } else {
        c.getPlayer().dropMessage(1, "You do not have enough mesos.");
      } 
      getMCOwner().getClient().getSession().writeAndFlush(PlayerShopPacket.shopItemUpdate(this));
    } 
  }
  
  public byte getShopType() {
    return 2;
  }
  
  public void closeShop(boolean saveItems, boolean remove) {
    MapleCharacter owner = getMCOwner();
    removeAllVisitors(10, 1);
    getMap().removeMapObject(this);
    for (MaplePlayerShopItem items : getItems()) {
      if (items.bundles > 0) {
        Item newItem = items.item.copy();
        newItem.setQuantity((short)(items.bundles * newItem.getQuantity()));
        if (MapleInventoryManipulator.addFromDrop(owner.getClient(), newItem, false)) {
          items.bundles = 0;
          continue;
        } 
        saveItems();
        break;
      } 
    } 
    owner.setPlayerShop(null);
    update();
    getMCOwner().getClient().getSession().writeAndFlush(PlayerShopPacket.shopErrorMessage(10, 0));
  }
  
  public void banPlayer(String name) {
    if (!this.bannedList.contains(name))
      this.bannedList.add(name); 
    for (int i = 0; i < 3; i++) {
      MapleCharacter chr = getVisitor(i);
      if (chr.getName().equals(name)) {
        chr.getClient().getSession().writeAndFlush(PlayerShopPacket.shopErrorMessage(5, 1));
        chr.setPlayerShop(null);
        removeVisitor(chr);
      } 
    } 
  }
  
  public boolean isBanned(String name) {
    if (this.bannedList.contains(name))
      return true; 
    return false;
  }
}
