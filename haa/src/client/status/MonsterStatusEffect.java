package client.status;

import client.MapleCharacter;
import java.util.concurrent.ScheduledFuture;

public class MonsterStatusEffect {
  private final int skill;
  
  private int duration;
  
  private int level;
  
  private int cid;
  
  private int interval;
  
  private int key;
  
  private long value;
  
  private long cancelTask;
  
  private long startTime;
  
  private long lastPoisonTime;
  
  private MonsterStatus ms;
  
  private MapleCharacter chr;
  
  private ScheduledFuture<?> schedule;
  
  private boolean mobskill;
  
  public MonsterStatusEffect(int skill, int duration) {
    this.skill = skill;
    this.duration = duration;
    setStartTime(System.currentTimeMillis());
    setCancelTask(duration);
  }
  
  public MonsterStatusEffect(int skill, int duration, long value) {
    this.skill = skill;
    this.duration = duration;
    this.value = value;
    setStartTime(System.currentTimeMillis());
    setCancelTask(duration);
  }
  
  public final boolean shouldCancel(long now) {
    return (this.cancelTask > 0L && this.cancelTask <= now);
  }
  
  public final void cancelTask() {
    this.cancelTask = 0L;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public int getSkill() {
    return this.skill;
  }
  
  public long getValue() {
    return this.value;
  }
  
  public void setValue(long value) {
    this.value = value;
  }
  
  public long getCancelTask() {
    return this.cancelTask;
  }
  
  public void setCancelTask(long cancelTask) {
    this.cancelTask = System.currentTimeMillis() + cancelTask;
  }
  
  public MonsterStatus getStati() {
    return this.ms;
  }
  
  public void setStati(MonsterStatus ms) {
    this.ms = ms;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public MapleCharacter getChr() {
    return this.chr;
  }
  
  public void setChr(MapleCharacter cid) {
    this.chr = cid;
  }
  
  public int getCid() {
    return this.cid;
  }
  
  public void setCid(int cid) {
    this.cid = cid;
  }
  
  public long getStartTime() {
    return this.startTime;
  }
  
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
  
  public int getInterval() {
    return this.interval;
  }
  
  public void setInterval(int interval) {
    this.interval = interval;
  }
  
  public long getLastPoisonTime() {
    return this.lastPoisonTime;
  }
  
  public void setLastPoisonTime(long lastPoisonTime) {
    this.lastPoisonTime = lastPoisonTime;
  }
  
  public int getKey() {
    return this.key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public boolean isMobskill() {
    return this.mobskill;
  }
  
  public void setMobskill(boolean mobskill) {
    this.mobskill = mobskill;
  }
}
