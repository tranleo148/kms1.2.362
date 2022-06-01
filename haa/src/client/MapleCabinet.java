package client;

public class MapleCabinet implements Comparable<MapleCabinet> {
  private int accountid;
  
  private int itemid;
  
  private int count;
  
  private int delete;
  
  private int playerid;
  
  private long savetime;
  
  private String Bigname;
  
  private String name;
  
  private String smallname;
  
  public MapleCabinet(int accountid, int itemid, int count, String Bigname, String smallname, long savetime, int delete) {
    this.accountid = accountid;
    this.itemid = itemid;
    this.count = count;
    this.Bigname = Bigname;
    this.smallname = smallname;
    this.savetime = savetime;
    this.delete = delete;
    this.playerid = 0;
    this.name = "없음";
  }
  
  public int getAccId() {
    return this.accountid;
  }
  
  public void setAccId(int accountid) {
    this.accountid = accountid;
  }
  
  public int getItemid() {
    return this.itemid;
  }
  
  public void setItemid(int itemid) {
    this.itemid = itemid;
  }
  
  public int getCount() {
    return this.count;
  }
  
  public void setCount(int count) {
    this.count = count;
  }
  
  public String getBigname() {
    return this.Bigname;
  }
  
  public void setBigname(String Bigname) {
    this.Bigname = Bigname;
  }
  
  public String getSmallname() {
    return this.smallname;
  }
  
  public void setSmallname(String smallname) {
    this.smallname = smallname;
  }
  
  public long getSaveTime() {
    return this.savetime;
  }
  
  public void setSaveTime(long savetime) {
    this.savetime = savetime;
  }
  
  public int getDelete() {
    return this.delete;
  }
  
  public void setDelete(int delete) {
    this.delete = delete;
  }
  
  public int getPlayerid() {
    return this.playerid;
  }
  
  public void setPlayerid(int playerid) {
    this.playerid = playerid;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public int compareTo(MapleCabinet o) {
    if (this.savetime < o.getSaveTime())
      return -1; 
    if (this.savetime > o.getSaveTime())
      return 1; 
    return 0;
  }
}
