package server.field.boss.lotus;

import client.MapleClient;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;

public class MapleEnergySphere extends MapleMapObject {
  private int x;
  
  private int y;
  
  private int size;
  
  private int retitution;
  
  private int destroyDelay;
  
  private int startDelay;
  
  private int density;
  
  private int friction;
  
  private int num;
  
  private int customx;
  
  private boolean ok;
  
  private boolean noGravity;
  
  private boolean noDeleteFromOthers;
  
  private boolean isDelayed;
  
  private List<Integer> pointx;
  
  private ScheduledFuture<?> schedule;
  
  public MapleEnergySphere(boolean isDelayed, int size, int y, int density, int friction, int destroyDelay, int startDelay) {
    setOk(true);
    setDelayed(isDelayed);
    setY(y);
    setDensity(density);
    setFriction(friction);
    this.destroyDelay = destroyDelay;
    this.startDelay = startDelay;
    this.size = size;
  }
  
  public MapleEnergySphere(boolean isDelayed, int size, int y, int density, int friction, int destroyDelay, int startDelay, List<Integer> pointx, int num) {
    setOk(true);
    setDelayed(isDelayed);
    setY(y);
    setDensity(density);
    setFriction(friction);
    this.destroyDelay = destroyDelay;
    this.startDelay = startDelay;
    this.size = size;
    this.pointx = pointx;
    this.num = num;
  }
  
  public MapleEnergySphere(int x, int retitution, int destroyDelay, int startDelay, boolean noGravity, boolean noDeleteFromOthers) {
    setOk(false);
    setX(x);
    setRetitution(retitution);
    setDestroyDelay(destroyDelay);
    setStartDelay(startDelay);
    setNoGravity(noGravity);
    setNoDeleteFromOthers(noDeleteFromOthers);
  }
  
  public int getX() {
    return this.x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getRetitution() {
    return this.retitution;
  }
  
  public void setRetitution(int retitution) {
    this.retitution = retitution;
  }
  
  public int getDestroyDelay() {
    return this.destroyDelay;
  }
  
  public void setDestroyDelay(int destroyDelay) {
    this.destroyDelay = destroyDelay;
  }
  
  public int getStartDelay() {
    return this.startDelay;
  }
  
  public void setStartDelay(int startDelay) {
    this.startDelay = startDelay;
  }
  
  public boolean isNoGravity() {
    return this.noGravity;
  }
  
  public void setNoGravity(boolean noGravity) {
    this.noGravity = noGravity;
  }
  
  public boolean isNoDeleteFromOthers() {
    return this.noDeleteFromOthers;
  }
  
  public void setNoDeleteFromOthers(boolean noDeleteFromOthers) {
    this.noDeleteFromOthers = noDeleteFromOthers;
  }
  
  public boolean isOk() {
    return this.ok;
  }
  
  public void setOk(boolean ok) {
    this.ok = ok;
  }
  
  public int getDensity() {
    return this.density;
  }
  
  public void setDensity(int density) {
    this.density = density;
  }
  
  public boolean isDelayed() {
    return this.isDelayed;
  }
  
  public void setDelayed(boolean isDelayed) {
    this.isDelayed = isDelayed;
  }
  
  public int getY() {
    return this.y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public int getFriction() {
    return this.friction;
  }
  
  public void setFriction(int friction) {
    this.friction = friction;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.ENERGY;
  }
  
  public void sendSpawnData(MapleClient client) {}
  
  public void sendDestroyData(MapleClient client) {}
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
  
  public int getSize() {
    return this.size;
  }
  
  public void setSize(int size) {
    this.size = size;
  }
  
  public int getNum() {
    return this.num;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public int getCustomx() {
    return this.customx;
  }
  
  public void setCustomx(int customx) {
    this.customx = customx;
  }
  
  public boolean isIsDelayed() {
    return this.isDelayed;
  }
  
  public void setIsDelayed(boolean isDelayed) {
    this.isDelayed = isDelayed;
  }
  
  public List<Integer> getPointx() {
    return this.pointx;
  }
  
  public void setPointx(List<Integer> pointx) {
    this.pointx = pointx;
  }
}
