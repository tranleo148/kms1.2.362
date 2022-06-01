package server.maps;

import client.MapleClient;
import java.awt.Rectangle;
import scripting.ReactorScriptManager;
import server.Timer;
import tools.Pair;
import tools.packet.CField;

public class MapleReactor extends MapleMapObject {
  private int rid;
  
  private MapleReactorStats stats;
  
  private byte state = 0;
  
  private byte facingDirection = 0;
  
  private int delay = -1;
  
  private int rank = 0;
  
  private int spawnPointNum;
  
  private MapleMap map;
  
  private String name = "";
  
  private boolean timerActive = false, alive = true, custom = false;
  
  public MapleReactor(MapleReactorStats stats, int rid) {
    this.stats = stats;
    this.rid = rid;
  }
  
  public void setCustom(boolean c) {
    this.custom = c;
  }
  
  public boolean isCustom() {
    return this.custom;
  }
  
  public final void setFacingDirection(byte facingDirection) {
    this.facingDirection = facingDirection;
  }
  
  public final byte getFacingDirection() {
    return this.facingDirection;
  }
  
  public void setTimerActive(boolean active) {
    this.timerActive = active;
  }
  
  public boolean isTimerActive() {
    return this.timerActive;
  }
  
  public int getReactorId() {
    return this.rid;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public byte getState() {
    return this.state;
  }
  
  public boolean isAlive() {
    return this.alive;
  }
  
  public void setAlive(boolean alive) {
    this.alive = alive;
  }
  
  public void setDelay(int delay) {
    this.delay = delay;
  }
  
  public int getDelay() {
    return this.delay;
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.REACTOR;
  }
  
  public int getReactorType() {
    return this.stats.getType(this.state);
  }
  
  public byte getTouch() {
    return this.stats.canTouch(this.state);
  }
  
  public void setMap(MapleMap map) {
    this.map = map;
  }
  
  public MapleMap getMap() {
    return this.map;
  }
  
  public Pair<Integer, Integer> getReactItem() {
    return this.stats.getReactItem(this.state);
  }
  
  public void sendDestroyData(MapleClient client) {
    client.getSession().writeAndFlush(CField.destroyReactor(this));
  }
  
  public void sendSpawnData(MapleClient client) {
    client.getSession().writeAndFlush(CField.spawnReactor(this));
  }
  
  public void forceStartReactor(MapleClient c) {
    ReactorScriptManager.getInstance().act(c, this);
  }
  
  public void forceHitReactor(byte newState, int cid) {
    setState(newState);
        setTimerActive(false);
    this.map.broadcastMessage(CField.triggerReactor(this, 0, cid));
    }
  
  public void forceHitReactor1 (final byte newState) {
        setState((byte) newState);
        setTimerActive(false);
        map.broadcastMessage(CField.triggerReactor1(this, (short) 0));
    }
  
  public void hitReactor(MapleClient c) {
    hitReactor(0, (short)0, c);
  }
  
  public void delayedDestroyReactor(long delay) {
    Timer.MapTimer.getInstance().schedule(new Runnable() {
          public void run() {
            MapleReactor.this.map.destroyReactor(MapleReactor.this.getObjectId());
          }
        },  delay);
  }
  
  public void hitReactor(int charPos, short stance, MapleClient c) {
    if (this.stats.getType(this.state) < 999 && this.stats.getType(this.state) != -1) {
      byte oldState = this.state;
      if (this.stats.getType(this.state) != 2 || (charPos != 0 && charPos != 2)) {
        if (this.rid == 2618000)
          c.getPlayer().removeItem(4001132, -1); 
        this.state = this.stats.getNextState(this.state);
        if (this.stats.getNextState(this.state) == -1 || this.stats.getType(this.state) == 999) {
          if ((this.stats.getType(this.state) < 100 || this.stats.getType(this.state) == 999) && this.delay > 0) {
            if (this.delay > 0) {
              this.map.destroyReactor(getObjectId());
            } else {
              this.map.broadcastMessage(CField.triggerReactor(this, stance, c.getPlayer().getId()));
            } 
          } else {
            this.map.broadcastMessage(CField.triggerReactor(this, stance, c.getPlayer().getId()));
          } 
          if (this.rid > 200015)
            ReactorScriptManager.getInstance().act(c, this); 
        } else {
          boolean done = false;
          this.map.broadcastMessage(CField.triggerReactor(this, stance, c.getPlayer().getId()));
          if (this.state == this.stats.getNextState(this.state) || this.rid == 2618000 || this.rid == 2309000) {
            if (this.rid > 200015)
              ReactorScriptManager.getInstance().act(c, this); 
            done = true;
          } 
          /* 191 */           if (this.rid == 3009000) {
/* 192 */             c.getPlayer().removeItem(4001162, -1);
/*     */           }
          
                            if (this.rid == 3002000) {
/* 192 */             c.getPlayer().removeItem(4001161, -1);
                      c.getPlayer().gainItem(4001162, 1);
/*     */           }
                            
                            if (this.rid == 3002001) {
                      c.getPlayer().gainItem(4001163, 1);
/*     */           }
          
          if (this.stats.getTimeOut(this.state) > 0) {
            if (!done && this.rid > 200015) {
              ReactorScriptManager.getInstance().act(c, this);
              setDelay(5000);
              this.map.destroyReactor(getObjectId());
            } 
            scheduleSetState(this.state, oldState, this.stats.getTimeOut(this.state), c);
          } 
        } 
      } 
    } 
  }
  
  public Rectangle getArea() {
    int height = (this.stats.getBR()).y - (this.stats.getTL()).y;
    int width = (this.stats.getBR()).x - (this.stats.getTL()).x;
    int origX = (getTruePosition()).x + (this.stats.getTL()).x;
    int origY = (getTruePosition()).y + (this.stats.getTL()).y;
    return new Rectangle(origX, origY, width, height);
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setRank(int r) {
    this.rank = r;
  }
  
  public int getRank() {
    return this.rank;
  }
  
  public int getSpawnPointNum() {
    return this.spawnPointNum;
  }
  
  public void setSpawnPointNum(int spawnPointNum) {
    this.spawnPointNum = spawnPointNum;
  }
  
  public String toString() {
    return "Reactor " + getObjectId() + " of id " + this.rid + " at position " + getPosition().toString() + " state" + this.state + " type " + this.stats.getType(this.state);
  }
  
  public void delayedHitReactor(final MapleClient c, long delay) {
    Timer.MapTimer.getInstance().schedule(new Runnable() {
          public void run() {
            MapleReactor.this.hitReactor(c);
          }
        },  delay);
  }
  
  public void scheduleSetState(final byte oldState, final byte newState, long delay, final MapleClient c) {
    Timer.MapTimer.getInstance().schedule(new Runnable() {
          public void run() {
            if (MapleReactor.this.state == oldState)
              MapleReactor.this.forceHitReactor(newState, c.getPlayer().getId()); 
          }
        },delay);
  }
}
