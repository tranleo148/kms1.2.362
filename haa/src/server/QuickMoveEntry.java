package server;

public class QuickMoveEntry {
  private int id;
  
  private int icon;
  
  private int level;
  
  private int type;
  
  private String desc;
  
  public QuickMoveEntry(int type, int id, int icon, int level, String desc) {
    this.type = type;
    this.id = id;
    this.icon = icon;
    this.level = level;
    this.desc = desc;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getIcon() {
    return this.icon;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public int getType() {
    return this.type;
  }
  
  public String getDesc() {
    return this.desc;
  }
}
