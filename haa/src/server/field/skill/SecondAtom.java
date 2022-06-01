package server.field.skill;

import client.MapleClient;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.CField;

public class SecondAtom extends MapleMapObject {
  private int projectileType;
  
  private int ownerId;
  
  private int targetId;
  
  private int skillId;
  
  private int duration;
  
  private int custom;
  
  private int maxPerHit;
  
  private int delay;
  
  private int spawnType;
  
  private int LocalOnly;
  
  private Point point;
  
  private List<Integer> points;
  
  public SecondAtom(int projectileType, int ownerId, int targetId, int skillId, int duration, int custom, int maxPerHit, Point point, List<Integer> points) {
    this.projectileType = projectileType;
    this.ownerId = ownerId;
    this.targetId = targetId;
    this.skillId = skillId;
    this.duration = duration;
    this.custom = custom;
    this.maxPerHit = maxPerHit;
    this.point = point;
    this.points = points;
    this.spawnType = 0;
  }
  
  public SecondAtom(int projectileType, int ownerId, int targetId, int delay, int skillId, int duration, int custom, int maxPerHit, Point point, List<Integer> points) {
    this.projectileType = projectileType;
    this.ownerId = ownerId;
    this.targetId = targetId;
    this.delay = delay;
    this.skillId = skillId;
    this.duration = duration;
    this.custom = custom;
    this.maxPerHit = maxPerHit;
    this.point = point;
    this.points = points;
    this.spawnType = 0;
  }
  
  public SecondAtom(int projectileType, int ownerId, int targetId, int delay, int skillId, int duration, int custom, int maxPerHit, Point point, List<Integer> points, int spawnType) {
    this.projectileType = projectileType;
    this.ownerId = ownerId;
    this.targetId = targetId;
    this.delay = delay;
    this.skillId = skillId;
    this.duration = duration;
    this.custom = custom;
    this.maxPerHit = maxPerHit;
    this.point = point;
    this.points = points;
    this.spawnType = spawnType;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.A_SECOND_ATOM;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.spawnSecondAtoms(this.ownerId, Arrays.asList(new SecondAtom[] { this }), this.spawnType));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.removeSecondAtom(this.ownerId, getObjectId()));
  }
  
  public int getOwnerId() {
    return this.ownerId;
  }
  
  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }
  
  public int getLocalOnly() {
    return this.LocalOnly;
  }
  
  public void setLocalOnly(int LocalOnly) {
    this.LocalOnly = LocalOnly;
  }
  
  public int getTargetId() {
    return this.targetId;
  }
  
  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public int getProjectileType() {
    return this.projectileType;
  }
  
  public void setProjectileType(int projectileType) {
    this.projectileType = projectileType;
  }
  
  public List<Integer> getPoints() {
    return this.points;
  }
  
  public void setPoints(List<Integer> points) {
    this.points = points;
  }
  
  public Point getPoint() {
    return this.point;
  }
  
  public void setPoint(Point point) {
    this.point = point;
  }
  
  public int getDelay() {
    return this.delay;
  }
  
  public void setDelay(int delay) {
    this.delay = delay;
  }
  
  public int getMaxPerHit() {
    return this.maxPerHit;
  }
  
  public int getCustom() {
    return this.custom;
  }
  
  public void setSpawnType(int spawnType) {
    this.spawnType = spawnType;
  }
}
