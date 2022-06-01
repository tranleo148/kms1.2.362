package server.life;

import server.Randomizer;

public class Ignition implements Comparable<Ignition> {
  private int ownerId;
  
  private int skill;
  
  private int interval;
  
  private int duration;
  
  private long damage;
  
  private long startTime;
  
  private int IgnitionKey;
  
  public Ignition(int ownerId, int skill, long damage, int interval, int duration) {
    this.ownerId = ownerId;
    this.skill = skill;
    this.damage = damage;
    this.interval = interval;
    this.duration = duration;
    this.startTime = System.currentTimeMillis();
    this.IgnitionKey = Randomizer.nextInt();
  }
  
  public int getIgnitionKey() {
    return this.IgnitionKey;
  }
  
  public int getOwnerId() {
    return this.ownerId;
  }
  
  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }
  
  public int getSkill() {
    return this.skill;
  }
  
  public void setSkill(int skill) {
    this.skill = skill;
  }
  
  public long getDamage() {
    return this.damage;
  }
  
  public void setDamage(long damage) {
    this.damage = damage;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public int getInterval() {
    return this.interval;
  }
  
  public void setInterval(int interval) {
    this.interval = interval;
  }
  
  public long getStartTime() {
    return this.startTime;
  }
  
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  public int compareTo(Ignition o) {
    Ignition other = o;
    if (this.damage < other.getDamage())
      return -1; 
    if (this.damage > other.getDamage())
      return 1; 
    if (this.damage == 0L)
      return 0; 
    return 0;
  }
}
