package server.field.skill;

import client.MapleClient;
import java.util.Arrays;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.CField;

public class SpecialPortal extends MapleMapObject {
  private int ownerId;
  
  private int type;
  
  private int skillId;
  
  private int mapId;
  
  private int pointX;
  
  private int pointY;
  
  private int duration;
  
  public SpecialPortal(int ownerId, int type, int skillId, int mapId, int pointX, int pointY, int duration) {
    this.ownerId = ownerId;
    this.type = type;
    this.skillId = skillId;
    this.mapId = mapId;
    this.pointX = pointX;
    this.pointY = pointY;
    this.duration = duration;
  }
  
  public int getOwnerId() {
    return this.ownerId;
  }
  
  public int getSkillType() {
    return this.type;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public int getMapId() {
    return this.mapId;
  }
  
  public int getPointX() {
    return this.pointX;
  }
  
  public int getPointY() {
    return this.pointY;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.SPECIAL_PORTAL;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.createSpecialPortal(this.ownerId, Arrays.asList(new SpecialPortal[] { this })));
  }
  
  public void sendDestroyData(MapleClient client) {}
}
