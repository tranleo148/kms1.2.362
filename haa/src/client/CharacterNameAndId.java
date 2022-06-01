package client;

public class CharacterNameAndId {
  private int id;
  
  private int accId;
  
  private int level;
  
  private int job;
  
  private String name;
  
  private String repName;
  
  private String groupname;
  
  private String memo;
  
  public CharacterNameAndId(int id, int accId, String name, String repName, int level, int job, String groupname, String memo) {
    this.id = id;
    this.accId = accId;
    this.name = name;
    this.repName = repName;
    this.level = level;
    this.job = job;
    this.groupname = groupname;
    this.memo = memo;
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public int getJob() {
    return this.job;
  }
  
  public String getGroupName() {
    return this.groupname;
  }
  
  public String getMemo() {
    return this.memo;
  }
  
  public int getAccId() {
    return this.accId;
  }
  
  public void setAccId(int accId) {
    this.accId = accId;
  }
  
  public String getRepName() {
    return this.repName;
  }
  
  public void setRepName(String repName) {
    this.repName = repName;
  }
}
