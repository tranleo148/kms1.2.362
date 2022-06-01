package client;

import java.awt.Point;

public class ForceAtom {
  private short delay;
  private int oid, key, inc, firstimpact, secondimpact, angle;
  private Point pos;
  
  public ForceAtom(int oid, int key, int inc, int firstimpact, int secondimpact, int angle, short delay) {
    this.oid = oid;
    this.key = key;
    this.inc = inc;
    this.firstimpact = firstimpact;
    this.secondimpact = secondimpact;
    this.angle = angle;
    this.delay = delay;
    this.pos = new Point(0, 0);
  }
  
  public ForceAtom(int oid, int key, int inc, int firstimpact, int secondimpact, int angle, short delay, Point pos) {
    this.oid = oid;
    this.key = key;
    this.inc = inc;
    this.firstimpact = firstimpact;
    this.secondimpact = secondimpact;
    this.angle = angle;
    this.delay = delay;
    this.pos = pos;
  }
  
  public short getDelay() {
    return this.delay;
  }
  
  public void setDelay(short delay) {
    this.delay = delay;
  }
  
  public int getObjectId() {
    return this.oid;
  }
  
  public void setObjectId(int oid) {
    this.oid = oid;
  }
  
  public int getInc() {
    return this.inc;
  }
  
  public void setInc(int inc) {
    this.inc = inc;
  }
  
  public int getFirstimpact() {
    return this.firstimpact;
  }
  
  public void setFirstimpact(int firstimpact) {
    this.firstimpact = firstimpact;
  }
  
  public int getSecondimpact() {
    return this.secondimpact;
  }
  
  public void setSecondimpact(int secondimpact) {
    this.secondimpact = secondimpact;
  }
  
  public int getAngle() {
    return this.angle;
  }
  
  public void setAngle(int angle) {
    this.angle = angle;
  }
  
  public int getKey() {
    return this.key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public Point getPos() {
    return this.pos;
  }
  
  public void setPos(Point pos) {
    this.pos = pos;
  }
}
