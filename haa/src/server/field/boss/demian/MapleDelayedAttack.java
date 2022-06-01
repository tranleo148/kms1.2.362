package server.field.boss.demian;

import client.MapleClient;
import java.awt.Point;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;

public class MapleDelayedAttack extends MapleMapObject {
  private boolean isfacingLeft;
  
  private int angle;
  
  private Point pos;
  
  private MapleMonster owner;
  
  public MapleDelayedAttack(MapleMonster owner, Point pos, boolean facingleft) {
    this.owner = owner;
    setPos(pos);
    this.isfacingLeft = facingleft;
  }
  
  public MapleDelayedAttack(MapleMonster owner, Point pos, int angle, boolean facingleft) {
    this.owner = owner;
    setPos(pos);
    this.angle = angle;
    this.isfacingLeft = facingleft;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.ATTACK;
  }
  
  public void sendSpawnData(MapleClient client) {}
  
  public void sendDestroyData(MapleClient client) {}
  
  public MapleMonster getOwner() {
    return this.owner;
  }
  
  public void setOwner(MapleMonster owner) {
    this.owner = owner;
  }
  
  public Point getPos() {
    return this.pos;
  }
  
  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public int getAngle() {
    return this.angle;
  }
  
  public void setAngle(int angle) {
    this.angle = angle;
  }
  
  public boolean isIsfacingLeft() {
    return this.isfacingLeft;
  }
  
  public void setIsfacingLeft(boolean isfacingLeft) {
    this.isfacingLeft = isfacingLeft;
  }
}
