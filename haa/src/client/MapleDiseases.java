package client;

import java.util.concurrent.ScheduledFuture;
import server.life.MobSkill;

public class MapleDiseases {
  private MobSkill mobskill;
  
  private ScheduledFuture schedule;
  
  private int value;
  
  private int duration;
  
  private long startTime;
  
  public MapleDiseases(int value, int duration, long startTime) {
    this.value = value;
    this.duration = duration;
    this.startTime = startTime;
  }
  
  public MobSkill getMobskill() {
    return this.mobskill;
  }
  
  public void setMobskill(MobSkill mobskill) {
    this.mobskill = mobskill;
  }
  
  public ScheduledFuture getSchedule() {
    return this.schedule;
  }
  
  public void setSchedule(ScheduledFuture schedule) {
    this.schedule = schedule;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public long getStartTime() {
    return this.startTime;
  }
  
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public void setValue(int value) {
    this.value = value;
  }
}
