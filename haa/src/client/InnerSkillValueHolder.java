package client;

public class InnerSkillValueHolder {
  private int skillId = 0;
  
  private byte skillLevel = 0;
  
  private byte maxLevel = 0;
  
  private byte rank = 0;
  
  public InnerSkillValueHolder(int skillId, byte skillLevel, byte maxLevel, byte rank) {
    this.skillId = skillId;
    this.skillLevel = skillLevel;
    this.maxLevel = maxLevel;
    this.rank = rank;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public byte getSkillLevel() {
    return this.skillLevel;
  }
  
  public byte getMaxLevel() {
    return this.maxLevel;
  }
  
  public byte getRank() {
    return this.rank;
  }
}
