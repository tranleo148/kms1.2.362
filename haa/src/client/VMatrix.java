package client;

public class VMatrix {
  private boolean isUnLock = false;
  
  private int id;
  
  private int position;
  
  private int level;
  
  public VMatrix(int id, int position, int level, boolean isUnLock) {
    this.id = id;
    this.position = position;
    this.level = level;
    this.isUnLock = isUnLock;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getMaxLevel() {
    if (this.id == -1 || this.id == 0)
      return 0; 
    Skill skill = SkillFactory.getSkill(this.id);
    return (skill == null) ? 0 : skill.getMaxLevel();
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public void setPosition(int position) {
    this.position = position;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public boolean isUnLock() {
    return this.isUnLock;
  }
  
  public void setUnLock(boolean isUnLock) {
    this.isUnLock = isUnLock;
  }
}
