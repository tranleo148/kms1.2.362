package server;

public class SkillCustomInfo {
  private long value;
  
  private long endtime = 0L;
  
  public SkillCustomInfo(long value, long time) {
    this.value = value;
    if (time > 0L)
      this.endtime = System.currentTimeMillis() + time; 
  }
  
  public boolean canCancel(long now) {
    return (this.endtime > 0L && now >= this.endtime);
  }
  
  public long getValue() {
    return this.value;
  }
  
  public long getEndTime() {
    return this.endtime;
  }
}
