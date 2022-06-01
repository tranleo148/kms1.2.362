package handling.world;

import client.MapleCharacter;
import java.awt.Point;
import java.io.Serializable;
import java.util.List;
import server.maps.MapleDoor;

public class MaplePartyCharacter implements Serializable {
  private static final long serialVersionUID = 6215463252132450750L;
  
  private String name;
  
  private int id;
  
  private int level;
  
  private int channel;
  
  private int jobid;
  
  private int mapid;
  
  private int doorTown = 999999999;
  
  private int doorTarget = 999999999;
  
  private int doorSkill = 0;
  
  private Point doorPosition = new Point(0, 0);
  
  private boolean online;
  
  private MapleCharacter chr;
  
  public MaplePartyCharacter(MapleCharacter maplechar) {
    this.name = maplechar.getName();
    this.level = maplechar.getLevel();
    this.channel = maplechar.getClient().getChannel();
    this.id = maplechar.getId();
    this.jobid = maplechar.getJob();
    this.mapid = maplechar.getMapId();
    this.chr = maplechar;
    this.online = true;
    List<MapleDoor> doors = maplechar.getDoors();
    if (doors.size() > 0) {
      MapleDoor door = doors.get(0);
      this.doorTown = door.getTown().getId();
      this.doorTarget = door.getTarget().getId();
      this.doorSkill = door.getSkill();
      this.doorPosition = door.getTargetPosition();
    } else {
      this.doorPosition = maplechar.getPosition();
    } 
  }
  
  public MaplePartyCharacter() {
    this.name = "";
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public int getChannel() {
    return this.channel;
  }
  
  public boolean isOnline() {
    return this.online;
  }
  
  public void setOnline(boolean online) {
    this.online = online;
  }
  
  public MapleCharacter getPlayer() {
    return this.chr;
  }
  
  public int getMapid() {
    return this.mapid;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getJobId() {
    return this.jobid;
  }
  
  public int getDoorTown() {
    return this.doorTown;
  }
  
  public int getDoorTarget() {
    return this.doorTarget;
  }
  
  public int getDoorSkill() {
    return this.doorSkill;
  }
  
  public Point getDoorPosition() {
    return this.doorPosition;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    MaplePartyCharacter other = (MaplePartyCharacter)obj;
    if (this.name == null) {
      if (other.name != null)
        return false; 
    } else if (!this.name.equals(other.name)) {
      return false;
    } 
    return true;
  }
}
