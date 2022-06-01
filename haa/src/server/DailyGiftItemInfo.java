package server;

public class DailyGiftItemInfo {
  private int id;
  
  private int itemId;
  
  private int quantity;
  
  private int SN;
  
  public DailyGiftItemInfo(int id, int itemId, int quantity, int SN) {
    this.id = id;
    this.itemId = itemId;
    this.quantity = quantity;
    this.SN = SN;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getItemId() {
    return this.itemId;
  }
  
  public void setItemId(int itemId) {
    this.itemId = itemId;
  }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public int getSN() {
    return this.SN;
  }
  
  public void setSN(int sN) {
    this.SN = sN;
  }
}
