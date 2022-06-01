package handling.channel.handler;

import java.awt.Rectangle;

public class PsychicGrabEntry {
  private long mobhp;
  
  private long mobmaxhp;
  
  private int a;
  
  private int b;
  
  private int mobid;
  
  private int unk2;
  
  private short secondsize;
  
  private byte firstsize;
  
  private byte unk;
  
  private Rectangle rect;
  
  public PsychicGrabEntry(byte firstsize, int a, int b, int mobid, short secondsize, long mobhp, long mobmaxhp, byte unk, Rectangle rect, int unk2) {
    this.firstsize = firstsize;
    this.a = a;
    this.b = b;
    this.mobid = mobid;
    this.secondsize = secondsize;
    this.mobhp = mobhp;
    this.mobmaxhp = mobmaxhp;
    this.unk = unk;
    this.rect = rect;
    this.unk2 = unk2;
  }
  
  public byte getFirstSize() {
    return this.firstsize;
  }
  
  public byte getUnk() {
    return this.unk;
  }
  
  public int getUnk2() {
    return this.unk2;
  }
  
  public short getSecondSize() {
    return this.secondsize;
  }
  
  public int getA() {
    return this.a;
  }
  
  public int getB() {
    return this.b;
  }
  
  public int getMobId() {
    return this.mobid;
  }
  
  public Rectangle getRect() {
    return this.rect;
  }
  
  public long getMobHp() {
    return this.mobhp;
  }
  
  public long getMobMaxHp() {
    return this.mobmaxhp;
  }
}
