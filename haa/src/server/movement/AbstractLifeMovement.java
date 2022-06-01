package server.movement;

import java.awt.Point;

public abstract class AbstractLifeMovement implements LifeMovement {
  private final Point position;
  
  private final int duration;
  
  private final int newstate;
  
  private final int type;
  
  private final short foodholds;
  
  private final byte unk;
  
  public AbstractLifeMovement(int type, Point position, int duration, int newstate, short FH, byte unk) {
    this.type = type;
    this.position = position;
    this.duration = duration;
    this.newstate = newstate;
    this.foodholds = FH;
    this.unk = unk;
  }
  
  public int getType() {
    return this.type;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public int getNewstate() {
    return this.newstate;
  }
  
  public Point getPosition() {
    return this.position;
  }
  
  public short getFootHolds() {
    return this.foodholds;
  }
  
  public byte getUnk() {
    return this.unk;
  }
}
