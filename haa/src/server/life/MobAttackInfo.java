package server.life;

import java.awt.Point;

public class MobAttackInfo {
  private int mobId;
  
  private int attackId;
  
  private boolean isDeadlyAttack;
  
  private int mpBurn;
  
  private int mpCon;
  
  private int fixDamR;
  
  private int diseaseSkill;
  
  private int diseaseLevel;
  
  public int PADamage;
  
  public int MADamage;
  
  public int attackAfter;
  
  public int range = 0;
  
  public Point lt = null;
  
  public Point rb = null;
  
  public boolean magic = false;
  
  public boolean isElement = false;
  
  private MobSkillData skill;
  
  public MobAttackInfo(int mobId, int attackId) {
    setMobId(mobId);
    setAttackId(attackId);
  }
  
  public void setDeadlyAttack(boolean isDeadlyAttack) {
    this.isDeadlyAttack = isDeadlyAttack;
  }
  
  public boolean isDeadlyAttack() {
    return this.isDeadlyAttack;
  }
  
  public void setMpBurn(int mpBurn) {
    this.mpBurn = mpBurn;
  }
  
  public int getMpBurn() {
    return this.mpBurn;
  }
  
  public void setDiseaseSkill(int diseaseSkill) {
    this.diseaseSkill = diseaseSkill;
  }
  
  public int getDiseaseSkill() {
    return this.diseaseSkill;
  }
  
  public void setDiseaseLevel(int diseaseLevel) {
    this.diseaseLevel = diseaseLevel;
  }
  
  public int getDiseaseLevel() {
    return this.diseaseLevel;
  }
  
  public void setMpCon(int mpCon) {
    this.mpCon = mpCon;
  }
  
  public int getMpCon() {
    return this.mpCon;
  }
  
  public int getRange() {
    int maxX = Math.max(Math.abs((this.lt == null) ? 0 : this.lt.x), Math.abs((this.rb == null) ? 0 : this.rb.x));
    int maxY = Math.max(Math.abs((this.lt == null) ? 0 : this.lt.y), Math.abs((this.rb == null) ? 0 : this.rb.y));
    return Math.max(maxX * maxX + maxY * maxY, this.range);
  }
  
  public int getMobId() {
    return this.mobId;
  }
  
  public void setMobId(int mobId) {
    this.mobId = mobId;
  }
  
  public int getAttackId() {
    return this.attackId;
  }
  
  public void setAttackId(int attackId) {
    this.attackId = attackId;
  }
  
  public MobSkillData getSkill() {
    return this.skill;
  }
  
  public void setSkill(MobSkillData skill) {
    this.skill = skill;
  }
  
  public int getFixDamR() {
    return this.fixDamR;
  }
  
  public void setFixDamR(int fixDamR) {
    this.fixDamR = fixDamR;
  }
  
  public static class MobSkillData {
    int delay;
    
    private int level;
    
    private int skill;
    
    public MobSkillData(int skill, int level, int delay) {
      setSkill(skill);
      setLevel(level);
      this.delay = delay;
    }
    
    public int getSkill() {
      return this.skill;
    }
    
    public void setSkill(int skill) {
      this.skill = skill;
    }
    
    public int getLevel() {
      return this.level;
    }
    
    public void setLevel(int level) {
      this.level = level;
    }
  }
}
