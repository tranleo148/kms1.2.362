package server.maps;

import java.awt.Point;

public class ForceAtom {
  private int nAttackCount;
  
  private int nInc;
  
  private int nFirstImpact;
  
  private int nSecondImpact;
  
  private int nAngle;
  
  private int nStartDelay;
  
  private int nStartX;
  
  private int nStartY;
  
  private int nMaxHitCount;
  
  private int nEffectIdx;
  
  private long dwCreateTime;
  
  public ForceAtom(int nInc, int nFirstImpact, int nSecondImpact, int nAngle, int nStartDelay) {
    this.nAttackCount = 1;
    this.nInc = nInc;
    this.nFirstImpact = nFirstImpact;
    this.nSecondImpact = nSecondImpact;
    this.nAngle = nAngle;
    this.nStartDelay = nStartDelay;
    this.nStartX = 0;
    this.nStartY = 0;
    this.dwCreateTime = System.currentTimeMillis();
    this.nMaxHitCount = 0;
    this.nEffectIdx = 0;
  }
  
  public ForceAtom(int nInc, int nFirstImpact, int nSecondImpact, int nAngle, int nStartDelay, Point pos) {
    this.nAttackCount = 1;
    this.nInc = nInc;
    this.nFirstImpact = nFirstImpact;
    this.nSecondImpact = nSecondImpact;
    this.nAngle = nAngle;
    this.nStartDelay = nStartDelay;
    this.nStartX = pos.x;
    this.nStartY = pos.y;
    this.dwCreateTime = System.currentTimeMillis();
    this.nMaxHitCount = 0;
    this.nEffectIdx = 0;
  }
  
  public ForceAtom(int nInc, int nFirstImpact, int nSecondImpact, int nAngle, int nStartDelay, int nStartX, int nStartY, long dwCreateTime, int nMaxHitCount, int nEffectIdx) {
    this.nAttackCount = 1;
    this.nInc = nInc;
    this.nFirstImpact = nFirstImpact;
    this.nSecondImpact = nSecondImpact;
    this.nAngle = nAngle;
    this.nStartDelay = nStartDelay;
    this.nStartX = nStartX;
    this.nStartY = nStartY;
    this.dwCreateTime = dwCreateTime;
    this.nMaxHitCount = nMaxHitCount;
    this.nEffectIdx = nEffectIdx;
  }
  
  public int getnInc() {
    return this.nInc;
  }
  
  public void setnInc(int nInc) {
    this.nInc = nInc;
  }
  
  public int getnFirstImpact() {
    return this.nFirstImpact;
  }
  
  public void setnFirstImpact(int nFirstImpact) {
    this.nFirstImpact = nFirstImpact;
  }
  
  public int getnSecondImpact() {
    return this.nSecondImpact;
  }
  
  public void setnSecondImpact(int nSecondImpact) {
    this.nSecondImpact = nSecondImpact;
  }
  
  public int getnAngle() {
    return this.nAngle;
  }
  
  public void setnAngle(int nAngle) {
    this.nAngle = nAngle;
  }
  
  public int getnStartDelay() {
    return this.nStartDelay;
  }
  
  public void setnStartDelay(int nStartDelay) {
    this.nStartDelay = nStartDelay;
  }
  
  public int getnStartX() {
    return this.nStartX;
  }
  
  public void setnStartX(int nStartX) {
    this.nStartX = nStartX;
  }
  
  public int getnStartY() {
    return this.nStartY;
  }
  
  public void setnStartY(int nStartY) {
    this.nStartY = nStartY;
  }
  
  public long getDwCreateTime() {
    return this.dwCreateTime;
  }
  
  public void setDwCreateTime(long dwCreateTime) {
    this.dwCreateTime = dwCreateTime;
  }
  
  public int getnMaxHitCount() {
    return this.nMaxHitCount;
  }
  
  public void setnMaxHitCount(int nMaxHitCount) {
    this.nMaxHitCount = nMaxHitCount;
  }
  
  public int getnEffectIdx() {
    return this.nEffectIdx;
  }
  
  public void setnEffectIdx(int nEffectIdx) {
    this.nEffectIdx = nEffectIdx;
  }
  
  public int getnAttackCount() {
    return this.nAttackCount;
  }
  
  public void setnAttackCount(int nAttackCount) {
    this.nAttackCount = nAttackCount;
  }
}
