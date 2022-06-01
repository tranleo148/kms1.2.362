package server.maps;

import client.MapleClient;
import constants.GameConstants;
import java.awt.Point;

public abstract class MapleMapObject {
  private Point position = new Point();
  
  private int objectId;
  
  public Point getPosition() {
    return new Point(this.position);
  }
  
  public Point getTruePosition() {
    return this.position;
  }
  
  public void setPosition(Point position) {
    this.position.x = position.x;
    this.position.y = position.y;
  }
  
  public int getObjectId() {
    return this.objectId;
  }
  
  public void setObjectId(int id) {
    this.objectId = id;
  }
  
  public double getRange() {
    return GameConstants.maxViewRangeSq();
  }
  
  public abstract MapleMapObjectType getType();
  
  public abstract void sendSpawnData(MapleClient paramMapleClient);
  
  public abstract void sendDestroyData(MapleClient paramMapleClient);
}
