package server.shops;

import client.inventory.Item;

public class MaplePlayerShopItem {
  public Item item;
  
  public short bundles;
  
  public long price;
  
  public MaplePlayerShopItem(Item item, short bundles, long price) {
    this.item = item;
    this.bundles = bundles;
    this.price = price;
  }
}
