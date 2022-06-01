package server.maps;

import client.MapleClient;
import tools.packet.CField;

public class MapleRune extends MapleMapObject {
  private final int type;
  
  private final int posX;
  
  private final int posY;
  
  private int spawnPointNum;
  
  private long createTimeMills;
  
  private MapleMap map;
  
  public MapleRune(int type, int posX, int posY, MapleMap map) {
    this.type = type;
    this.posX = posX;
    this.posY = posY;
    this.map = map;
    this.createTimeMills = System.currentTimeMillis();
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.RUNE;
  }
  
  public void setMap(MapleMap map) {
    this.map = map;
  }
  
  public MapleMap getMap() {
    return this.map;
  }
  
  public int getRuneType() {
    return this.type;
  }
  
  public int getPositionX() {
    return this.posX;
  }
  
  public int getPositionY() {
    return this.posY;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.spawnRune(this, true));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.removeRune(this, client.getPlayer(), 0));
  }
  
  public long getCreateTimeMills() {
    return this.createTimeMills;
  }
  
  public void setCreateTimeMills(long createTimeMills) {
    this.createTimeMills = createTimeMills;
  }
  
  public int getSpawnPointNum() {
    return this.spawnPointNum;
  }
  
  public void setSpawnPointNum(int spawnPointNum) {
    this.spawnPointNum = spawnPointNum;
  }
}
