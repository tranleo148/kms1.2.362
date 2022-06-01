package server.life;

import java.util.ArrayList;
import java.util.List;
import tools.Triple;

public class MobAttack {
  private final int afterAttack;
  
  private final int fixAttack;
  
  private final int action;
  
  private final int cooltime;
  
  private final int onlyAfterAttack;
  
  private final int afterAttackCount;
  
  private final List<Triple<Integer, Integer, Integer>> skills = new ArrayList<>();
  
  public MobAttack(int action, int afterAttack, int fixAttack, int onlyAfterAttack, int cooltime, int afterAttackCount) {
    this.action = action;
    this.afterAttack = afterAttack;
    this.fixAttack = fixAttack;
    this.onlyAfterAttack = onlyAfterAttack;
    this.cooltime = cooltime;
    this.afterAttackCount = afterAttackCount;
  }
  
  public List<Triple<Integer, Integer, Integer>> getSkills() {
    return this.skills;
  }
  
  public void addSkill(int skillId, int skillLevel, int delay) {
    this.skills.add(new Triple<>(Integer.valueOf(skillId), Integer.valueOf(skillLevel), Integer.valueOf(delay)));
  }
  
  public int getAction() {
    return this.action;
  }
  
  public int getAfterAttack() {
    return this.afterAttack;
  }
  
  public boolean isOnlyAfterAttack() {
    return (this.onlyAfterAttack == 1);
  }
  
  public int getFixAttack() {
    return this.fixAttack;
  }
  
  public int getCoolTime() {
    return this.cooltime;
  }
  
  public int getAfterAttackCount() {
    return this.afterAttackCount;
  }
}
