package server.polofritto;

import client.MapleClient;
import java.awt.Point;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;

public class MapleRandomPortal extends MapleMapObject {
  public static final int MAP_PORTAL = 2;
  
  public static final int DOOR_PORTAL = 6;
  
  private boolean polo;
  
  private int type;
  
  private int mapId;
  
  private int charId;
  
  private Point pos;
  
  public MapleRandomPortal(int type, Point pos, int mapId, int charId, boolean isPolo) {
    this.type = type;
    this.pos = pos;
    this.mapId = mapId;
    this.charId = charId;
    this.polo = isPolo;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.RANDOM_PORTAL;
  }
  
  public void sendSpawnData(MapleClient client) {}
  
  public void sendDestroyData(MapleClient client) {}
  
  public int getPortalType() {
    return this.type;
  }
  
  public void setPortalType(int type) {
    this.type = type;
  }
  
  public Point getPos() {
    return this.pos;
  }
  
  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public int getMapId() {
    return this.mapId;
  }
  
  public void setMapId(int mapId) {
    this.mapId = mapId;
  }
  
  public int getCharId() {
    return this.charId;
  }
  
  public void setCharId(int charId) {
    this.charId = charId;
  }
  
  public boolean ispolo() {
    return this.polo;
  }
  
  public void setpolo(boolean polo) {
    this.polo = polo;
  }
}
