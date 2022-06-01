package server;

import client.inventory.Item;

public class MapleDueyActions {
  private String sender = null;
  
  private Item item = null;
  
  private int mesos = 0;
  
  private int quantity = 1;
  
  private long sentTime;
  
  private int packageId = 0;
  
  private String content = null;
  
  private boolean quick = false;
  
  public MapleDueyActions(int pId, Item item) {
    this.item = item;
    this.quantity = item.getQuantity();
    this.packageId = pId;
  }
  
  public MapleDueyActions(int pId) {
    this.packageId = pId;
  }
  
  public void setContent(String s) {
    this.content = s;
  }
  
  public long getExpireTime() {
    if (isQuick())
      return getSentTime() + 2592000000L; 
    return getSentTime() + 43200000L + 2592000000L;
  }
  
  public boolean canReceive() {
    return (isQuick() || getSentTime() + 43200000L < System.currentTimeMillis());
  }
  
  public boolean isExpire() {
    if (isQuick())
      return (getSentTime() + 2592000000L < System.currentTimeMillis()); 
    return (getSentTime() + 43200000L + 2592000000L < System.currentTimeMillis());
  }
  
  public String getContent() {
    return this.content;
  }
  
  public void setQuick(boolean bln) {
    this.quick = bln;
  }
  
  public boolean isQuick() {
    return this.quick;
  }
  
  public String getSender() {
    return this.sender;
  }
  
  public void setSender(String name) {
    this.sender = name;
  }
  
  public Item getItem() {
    return this.item;
  }
  
  public int getMesos() {
    return this.mesos;
  }
  
  public void setMesos(int set) {
    this.mesos = set;
  }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  public int getPackageId() {
    return this.packageId;
  }
  
  public void setSentTime(long sentTime) {
    this.sentTime = sentTime;
  }
  
  public long getSentTime() {
    return this.sentTime;
  }
}
