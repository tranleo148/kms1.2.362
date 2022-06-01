package client;

public class PlatformerRecord {
  private int Stage;
  
  private int ClearTime;
  
  private int Stars;
  
  public PlatformerRecord(int stage, int cleartime, int stars) {
    this.Stage = stage;
    this.ClearTime = cleartime;
    this.Stars = stars;
  }
  
  public void setStage(int a) {
    this.Stage = a;
  }
  
  public void setClearTime(int a1) {
    this.ClearTime = a1;
  }
  
  public void setStars(int a1) {
    this.Stars = a1;
  }
  
  public int getStage() {
    return this.Stage;
  }
  
  public int getClearTime() {
    return this.ClearTime;
  }
  
  public int getStars() {
    return this.Stars;
  }
}
