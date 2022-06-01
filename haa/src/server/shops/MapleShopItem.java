package server.shops;

public class MapleShopItem {
  private int id;
  
  private short buyable;
  
  private int itemId;
  
  private long price;
  
  private int pricequantity;
  
  private byte tab;
  
  private int quantity;
  
  private int period;
  
  private int position;
  
  private int itemRate;
  
  private int buyquantity;
  
  private int rechargemonth;
  
  private int recharge;
  
  private int rechargeday;
  
  private int resetday;
  
  private int rechargecount;
  
  public MapleShopItem(int id, short buyable, int itemId, long price, int pricequantity, byte tab, int quantity, int period, int position, int itemRate, int buyquantity, int rechargemonth, int rechargeday, int resetday, int recharge, int rechargecount) {
    this.id = id;
    this.buyable = buyable;
    this.itemId = itemId;
    this.price = price;
    this.pricequantity = pricequantity;
    this.tab = tab;
    this.quantity = quantity;
    this.period = period;
    this.position = position;
    this.itemRate = itemRate;
    this.buyquantity = buyquantity;
    this.rechargemonth = rechargemonth;
    this.rechargeday = rechargeday;
    this.resetday = resetday;
    this.recharge = recharge;
    this.rechargecount = rechargecount;
  }
  
  public int getShopItemId() {
    return this.id;
  }
  
  public int getPriceQuantity() {
    return this.pricequantity;
  }
  
  public int getBuyQuantity() {
    return this.buyquantity;
  }
  
  public byte getTab() {
    return this.tab;
  }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  public int getPeriod() {
    return this.period;
  }
  
  public short getBuyable() {
    return this.buyable;
  }
  
  public int getItemId() {
    return this.itemId;
  }
  
  public long getPrice() {
    return this.price;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public int getItemRate() {
    return this.itemRate;
  }
  
  public void setItemRate(int itemRate) {
    this.itemRate = itemRate;
  }
  
  public int getReChargeMonth() {
    return this.rechargemonth;
  }
  
  public int getReChargeDay() {
    return this.rechargeday;
  }
  
  public int getReCharge() {
    return this.recharge;
  }
  
  public int getReChargeCount() {
    return this.rechargecount;
  }
  
  public int getResetday() {
    return this.resetday;
  }
  
  public void setResetday(int resetday) {
    this.resetday = resetday;
  }
}
