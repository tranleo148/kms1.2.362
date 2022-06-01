package client;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SecondAtom2 {
  private int dataIndex;
  
  private int target;
  
  private int createDelay;
  
  private int enableDelay;
  
  private int expire;
  
  private int enableCustom;
  
  private int attackableCount;
  
  private int rotate;
  
  private int sourceId;
  
  private int localOnly;
  
  private Point pos;
  
  private List<Point> extraPos;
  
  private List<Integer> custom;
  
  public SecondAtom2(SecondAtom2 sa) {
    this.dataIndex = sa.getDataIndex();
    this.target = sa.getTarget();
    this.createDelay = sa.getCreateDelay();
    this.enableDelay = sa.getEnableDelay();
    this.expire = sa.getExpire();
    this.enableCustom = sa.getEnableCustom();
    this.attackableCount = sa.getAttackableCount();
    this.pos = sa.getPos();
    this.rotate = sa.getRotate();
    this.extraPos = sa.getExtraPos();
    this.custom = sa.getCustom();
    this.sourceId = sa.getSourceId();
    this.localOnly = sa.getLocalOnly();
  }
  
  public SecondAtom2(int dataIndex, int target, int createDelay, int enableDelay, int expire, int enableCustom, int attackableCount, Point pos, int rotate, List<Point> extraPos, List<Integer> custom, int localOnly, int sourceId) {
    this.dataIndex = dataIndex;
    this.target = target;
    this.createDelay = createDelay;
    this.enableDelay = enableDelay;
    this.expire = expire;
    this.enableCustom = enableCustom;
    this.attackableCount = attackableCount;
    this.pos = pos;
    this.rotate = rotate;
    if (extraPos == null) {
      this.extraPos = new ArrayList<>();
    } else {
      this.extraPos = extraPos;
    } 
    if (custom == null) {
      this.custom = new ArrayList<>();
    } else {
      this.custom = custom;
    } 
    this.localOnly = localOnly;
    this.sourceId = sourceId;
  }
  
  public int getDataIndex() {
    return this.dataIndex;
  }
  
  public void setDataIndex(int dataIndex) {
    this.dataIndex = dataIndex;
  }
  
  public int getTarget() {
    return this.target;
  }
  
  public void setTarget(int target) {
    this.target = target;
  }
  
  public int getCreateDelay() {
    return this.createDelay;
  }
  
  public void setCreateDelay(int createDelay) {
    this.createDelay = createDelay;
  }
  
  public int getEnableDelay() {
    return this.enableDelay;
  }
  
  public void setEnableDelay(int enableDelay) {
    this.enableDelay = enableDelay;
  }
  
  public int getExpire() {
    return this.expire;
  }
  
  public void setExpire(int expire) {
    this.expire = expire;
  }
  
  public int getEnableCustom() {
    return this.enableCustom;
  }
  
  public void setEnableCustom(int enableCustom) {
    this.enableCustom = enableCustom;
  }
  
  public int getAttackableCount() {
    return this.attackableCount;
  }
  
  public void setAttackableCount(int attackableCount) {
    this.attackableCount = attackableCount;
  }
  
  public int getRotate() {
    return this.rotate;
  }
  
  public void setRotate(int rotate) {
    this.rotate = rotate;
  }
  
  public int getSourceId() {
    return this.sourceId;
  }
  
  public void setSourceId(int sourceId) {
    this.sourceId = sourceId;
  }
  
  public Point getPos() {
    return this.pos;
  }
  
  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public List<Point> getExtraPos() {
    return this.extraPos;
  }
  
  public void setExtraPos(List<Point> extraPos) {
    this.extraPos = extraPos;
  }
  
  public List<Integer> getCustom() {
    return this.custom;
  }
  
  public void setCustom(List<Integer> custom) {
    this.custom = custom;
  }
  
  public int getLocalOnly() {
    return this.localOnly;
  }
  
  public void setLocalOnly(int localOnly) {
    this.localOnly = localOnly;
  }
}
