package server;

import client.inventory.Item;
import java.util.ArrayList;
import java.util.List;

public class MerchItemPackage {
  private long lastsaved;
  
  private int mesos = 0;
  
  private int packageid;
  
  private List<Item> items = new ArrayList<>();
  
  public void setItems(List<Item> items) {
    this.items = items;
  }
  
  public List<Item> getItems() {
    return this.items;
  }
  
  public void setSavedTime(long lastsaved) {
    this.lastsaved = lastsaved;
  }
  
  public long getSavedTime() {
    return this.lastsaved;
  }
  
  public int getMesos() {
    return this.mesos;
  }
  
  public void setMesos(int set) {
    this.mesos = set;
  }
  
  public int getPackageid() {
    return this.packageid;
  }
  
  public void setPackageid(int packageid) {
    this.packageid = packageid;
  }
}
