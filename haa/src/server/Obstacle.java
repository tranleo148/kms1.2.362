package server;

import client.MapleClient;
import java.awt.Point;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;

public class Obstacle extends MapleMapObject {
  private Point oldPosition;
  
  private Point newPosition;
  
  private int key;
  
  private int range;
  
  private int trueDamage;
  
  private int delay;
  
  private int height;
  
  private int VperSec;
  
  private int maxP;
  
  private int length;
  
  private int angle;
  
  private int unk;
  
  private boolean effect = true;
  
  public Obstacle(int key, Point pos1, Point pos2, int range, int trueDamage, int height, int MaxP, int length, int angle, int unk) {
    this.key = key;
    this.oldPosition = pos1;
    this.newPosition = pos2;
    this.range = range;
    this.trueDamage = trueDamage;
    this.delay = 0;
    setHeight(height);
    this.maxP = MaxP;
    this.length = length;
    this.angle = angle;
    this.unk = unk;
  }
  
  public Obstacle(int key, Point pos1, Point pos2, int range, int trueDamage, int height, int MaxP, int length, int angle) {
    this.key = key;
    this.oldPosition = pos1;
    this.newPosition = pos2;
    this.range = range;
    this.trueDamage = trueDamage;
    this.delay = 0;
    setHeight(height);
    this.maxP = MaxP;
    this.length = length;
    this.angle = angle;
    this.unk = 0;
  }
  
  public Obstacle(int key, Point pos1, Point pos2, int range, int trueDamage, int dealy, int height, int MaxP, int length, int angle, int unk) {
    this.key = key;
    this.oldPosition = pos1;
    this.newPosition = pos2;
    this.range = range;
    this.trueDamage = trueDamage;
    this.delay = dealy;
    setHeight(height);
    this.maxP = MaxP;
    this.length = length;
    this.angle = angle;
    this.unk = unk;
  }
  
  public Point getOldPosition() {
    return this.oldPosition;
  }
  
  public void setOldPosition(Point oldPosition) {
    this.oldPosition = oldPosition;
  }
  
  public Point getNewPosition() {
    return this.newPosition;
  }
  
  public void setNewPosition(Point newPosition) {
    this.newPosition = newPosition;
  }
  
  public int getRangeed() {
    return this.range;
  }
  
  public void setRange(int range) {
    this.range = range;
  }
  
  public int getTrueDamage() {
    return this.trueDamage;
  }
  
  public void setTrueDamage(int trueDamage) {
    this.trueDamage = trueDamage;
  }
  
  public int getDelay() {
    return this.delay;
  }
  
  public void setDelay(int delay) {
    this.delay = delay;
  }
  
  public int getVperSec() {
    return this.VperSec;
  }
  
  public void setVperSec(int vperSec) {
    this.VperSec = vperSec;
  }
  
  public int getMaxP() {
    return this.maxP;
  }
  
  public void setMaxP(int maxP) {
    this.maxP = maxP;
  }
  
  public int getLength() {
    return this.length;
  }
  
  public void setLength(int length) {
    this.length = length;
  }
  
  public int getAngle() {
    return this.angle;
  }
  
  public void setAngle(int angle) {
    this.angle = angle;
  }
  
  public int getUnk() {
    return this.unk;
  }
  
  public void setUnk(int unk) {
    this.unk = unk;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public void setHeight(int height) {
    this.height = height;
  }
  
  public int getKey() {
    return this.key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public boolean isEffect() {
    return this.effect;
  }
  
  public void setEffect(boolean effect) {
    this.effect = effect;
  }
  
  public int getDistancePoints() {
    double x = Math.pow(this.oldPosition.getX() - this.newPosition.getX(), 2.0D);
    double y = Math.pow(this.oldPosition.getY() - this.newPosition.getY(), 2.0D);
    double distantce = Math.sqrt(x + y);
    return (int)Math.floor(distantce);
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.OBSTACLE;
  }
  
  public void sendSpawnData(MapleClient client) {}
  
  public void sendDestroyData(MapleClient client) {}
}
