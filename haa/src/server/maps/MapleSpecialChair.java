package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import tools.packet.CField;

public class MapleSpecialChair extends MapleMapObject {
  private int itemId;
  
  private Rectangle rect;
  
  private Point point;
  
  private MapleCharacter owner;
  
  private List<MapleSpecialChairPlayer> players = new ArrayList<>();
  
  public MapleSpecialChair(int itemId, Rectangle rect, Point point, MapleCharacter owner, List<MapleSpecialChairPlayer> players) {
    this.itemId = itemId;
    this.rect = rect;
    this.point = point;
    this.owner = owner;
    this.players = players;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.SPECIAL_CHAIR;
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.specialChair(this.owner, true, false, true, this));
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.specialChair(this.owner, false, false, false, this));
  }
  
  public void addPlayer(MapleCharacter chr, int emotion) {
    this.players.add(new MapleSpecialChairPlayer(chr, emotion));
  }
  
  public void updatePlayer(MapleCharacter chr, int emotion) {
    for (int i = 0; i < this.players.size(); i++) {
      if ((this.players.get(i)).emotion == -1) {
        this.players.set(i, new MapleSpecialChairPlayer(chr, emotion));
        break;
      } 
    } 
  }
  
  public int getItemId() {
    return this.itemId;
  }
  
  public void setItemId(int itemId) {
    this.itemId = itemId;
  }
  
  public Rectangle getRect() {
    return this.rect;
  }
  
  public void setRect(Rectangle rect) {
    this.rect = rect;
  }
  
  public Point getPoint() {
    return this.point;
  }
  
  public void setPoint(Point point) {
    this.point = point;
  }
  
  public List<MapleSpecialChairPlayer> getPlayers() {
    return this.players;
  }
  
  public void setPlayers(List<MapleSpecialChairPlayer> players) {
    this.players = players;
  }
  
  public MapleCharacter getOwner() {
    return this.owner;
  }
  
  public void setOwner(MapleCharacter owner) {
    this.owner = owner;
  }
  
  public class MapleSpecialChairPlayer {
    private MapleCharacter chr;
    
    private int emotion;
    
    public MapleSpecialChairPlayer(MapleCharacter chr, int emotion) {
      this.chr = chr;
      this.emotion = emotion;
    }
    
    public MapleCharacter getPlayer() {
      return this.chr;
    }
    
    public void setPlayer(MapleCharacter chr) {
      this.chr = chr;
    }
    
    public int getEmotion() {
      return this.emotion;
    }
    
    public void setEmotion(int emotion) {
      this.emotion = emotion;
    }
  }
}
