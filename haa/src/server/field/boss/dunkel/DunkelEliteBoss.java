package server.field.boss.dunkel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DunkelEliteBoss {
  private byte b1;
  
  private byte b2;
  
  private short bosscode = 0;
  
  private short v1 = 0;
  
  private short Arrowvelocity;
  
  private short v2;
  
  private short v3;
  
  private short v4;
  
  private short v5;
  
  private int unk1;
  
  private int unk2;
  
  private int unk3;
  
  private int unk4;
  
  private int unk5;
  
  private int unk6;
  
  private int unk7;
  
  private int unk8;
  
  private int unk9;
  
  private int unk10;
  
  private int unk11;
  
  private int unk12;
  
  private int unk13;
  
  private List<Point> points = new ArrayList<>();
  
  private Point p1 = new Point(0, 0), p2 = new Point(0, 0), p3 = new Point(0, 0);
  
  public DunkelEliteBoss(short bosscode, short v1, int unk1, int unk2, int unk3, int unk4, int unk5, int unk6, int unk7, short v2, short Arrowvelocity, short v3, byte b1, byte b2, Point p1, Point p2, Point p3, short v4, short v5) {
    this.bosscode = bosscode;
    this.b1 = b1;
    this.b2 = b2;
    this.v1 = v1;
    this.v2 = v2;
    this.v3 = v3;
    this.v4 = v4;
    this.v5 = v5;
    this.Arrowvelocity = Arrowvelocity;
    this.p1 = p1;
    this.unk1 = unk1;
    this.unk2 = unk2;
    this.unk3 = unk3;
    this.unk4 = unk4;
    this.unk5 = unk5;
    this.unk6 = unk6;
    this.unk7 = unk7;
    this.p2 = p2;
    this.p3 = p3;
  }
  
  public byte getb1() {
    return this.b1;
  }
  
  public byte getb2() {
    return this.b2;
  }
  
  public short getbosscode() {
    return this.bosscode;
  }
  
  public short getv1() {
    return this.v1;
  }
  
  public short getv2() {
    return this.v2;
  }
  
  public short getv3() {
    return this.v3;
  }
  
  public short getv4() {
    return this.v4;
  }
  
  public short getv5() {
    return this.v5;
  }
  
  public short getArrowvelocity() {
    return this.Arrowvelocity;
  }
  
  public Point getP1() {
    return this.p1;
  }
  
  public Point getP2() {
    return this.p2;
  }
  
  public Point getP3() {
    return this.p3;
  }
  
  public int getUnk1() {
    return this.unk1;
  }
  
  public int getUnk2() {
    return this.unk2;
  }
  
  public int getUnk3() {
    return this.unk3;
  }
  
  public int getUnk4() {
    return this.unk4;
  }
  
  public int getUnk5() {
    return this.unk5;
  }
  
  public int getUnk6() {
    return this.unk6;
  }
  
  public int getUnk7() {
    return this.unk7;
  }
  
  public int getUnk8() {
    return this.unk8;
  }
  
  public int getUnk9() {
    return this.unk9;
  }
  
  public int getUnk10() {
    return this.unk10;
  }
  
  public int getUnk11() {
    return this.unk11;
  }
  
  public int getUnk12() {
    return this.unk12;
  }
  
  public int getUnk13() {
    return this.unk13;
  }
}
