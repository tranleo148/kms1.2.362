package server.life;

import java.util.ArrayList;
import java.util.List;
import tools.Pair;

public class Transform {
  private int type1;
  
  private int type2;
  
  private int cooldown;
  
  private int off;
  
  private int on;
  
  private int duration;
  
  private int withMob;
  
  private List<Pair<Integer, Integer>> skills = new ArrayList<>();
  
  public Transform(int type1, int type2, int cooldown, int off, int on, int duration, int withMob) {
    this.type1 = type1;
    this.type2 = type2;
    this.cooldown = cooldown;
    this.off = off;
    this.on = on;
    this.duration = duration;
    this.withMob = withMob;
  }
  
  public int getType1() {
    return this.type1;
  }
  
  public void setType1(int type1) {
    this.type1 = type1;
  }
  
  public int getType2() {
    return this.type2;
  }
  
  public void setType2(int type2) {
    this.type2 = type2;
  }
  
  public int getCooldown() {
    return this.cooldown;
  }
  
  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
  }
  
  public int getOff() {
    return this.off;
  }
  
  public void setOff(int off) {
    this.off = off;
  }
  
  public int getOn() {
    return this.on;
  }
  
  public void setOn(int on) {
    this.on = on;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public List<Pair<Integer, Integer>> getSkills() {
    return this.skills;
  }
  
  public void setSkills(List<Pair<Integer, Integer>> skills) {
    this.skills = skills;
  }
  
  public int getWithMob() {
    return this.withMob;
  }
  
  public void setWithMob(int withMob) {
    this.withMob = withMob;
  }
}
