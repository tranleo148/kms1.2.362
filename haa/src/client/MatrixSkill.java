package client;

import java.awt.Point;

public class MatrixSkill {
  private short unk2;
  
  private int skill;
  
  private int level;
  
  private int unk1;
  
  private int unk3;
  
  private int unk4;
  
  private int unk5;
  
  private int unk6;
  
  private int x;
  
  private int y;
  
  private int x2;
  
  private int y2;
  
  private Point angle;
  
  public MatrixSkill(int skill, int level, int unk1, short unk2, Point angle, int unk3, int unk4) {
    setSkill(skill);
    setLevel(level);
    setUnk1(unk1);
    setUnk2(unk2);
    setAngle(angle);
    setUnk3(unk3);
    setUnk4(unk4);
    setUnk5(0, 0, 0);
    setUnk6(0, 0, 0);
  }
  
  public int getSkill() {
    return this.skill;
  }
  
  public void setSkill(int skill) {
    this.skill = skill;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public int getUnk1() {
    return this.unk1;
  }
  
  public void setUnk1(int unk1) {
    this.unk1 = unk1;
  }
  
  public short getUnk2() {
    return this.unk2;
  }
  
  public void setUnk2(short unk2) {
    this.unk2 = unk2;
  }
  
  public Point getAngle() {
    return this.angle;
  }
  
  public void setAngle(Point angle) {
    this.angle = angle;
  }
  
  public int getUnk3() {
    return this.unk3;
  }
  
  public void setUnk3(int unk3) {
    this.unk3 = unk3;
  }
  
  public int getUnk4() {
    return this.unk4;
  }
  
  public void setUnk4(int unk4) {
    this.unk4 = unk4;
  }
  
  public int getUnk5() {
    return this.unk5;
  }
  
  public void setUnk5(int unk5, int x, int y) {
    this.unk5 = unk5;
    setX(x);
    setY(y);
  }
  
  public int getX() {
    return this.x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public int getUnk6() {
    return this.unk6;
  }
  
  public void setUnk6(int unk6, int x2, int y2) {
    this.unk6 = unk6;
    this.x2 = x2;
    this.y2 = y2;
  }
  
  public int getX2() {
    return this.x2;
  }
  
  public void setX2(int x2) {
    this.x2 = x2;
  }
  
  public int getY2() {
    return this.y2;
  }
  
  public void setY2(int y2) {
    this.y2 = y2;
  }
}
