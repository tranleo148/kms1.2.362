package server.events;

import client.MapleCharacter;
import java.util.LinkedList;
import java.util.List;
import server.Timer;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleCoconut extends MapleEvent {
  private List<MapleCoconuts> coconuts = new LinkedList<>();
  
  private int[] coconutscore = new int[2];
  
  private int countBombing = 0;
  
  private int countFalling = 0;
  
  private int countStopped = 0;
  
  public MapleCoconut(int channel, MapleEventType type) {
    super(channel, type);
  }
  
  public void finished(MapleCharacter chr) {}
  
  public void reset() {
    super.reset();
    resetCoconutScore();
  }
  
  public void unreset() {
    super.unreset();
    resetCoconutScore();
    setHittable(false);
  }
  
  public void onMapLoad(MapleCharacter chr) {
    super.onMapLoad(chr);
  }
  
  public MapleCoconuts getCoconut(int id) {
    if (id >= this.coconuts.size())
      return null; 
    return this.coconuts.get(id);
  }
  
  public List<MapleCoconuts> getAllCoconuts() {
    return this.coconuts;
  }
  
  public void setHittable(boolean hittable) {
    for (MapleCoconuts nut : this.coconuts)
      nut.setHittable(hittable); 
  }
  
  public int getBombings() {
    return this.countBombing;
  }
  
  public void bombCoconut() {
    this.countBombing--;
  }
  
  public int getFalling() {
    return this.countFalling;
  }
  
  public void fallCoconut() {
    this.countFalling--;
  }
  
  public int getStopped() {
    return this.countStopped;
  }
  
  public void stopCoconut() {
    this.countStopped--;
  }
  
  public int[] getCoconutScore() {
    return this.coconutscore;
  }
  
  public int getMapleScore() {
    return this.coconutscore[0];
  }
  
  public int getStoryScore() {
    return this.coconutscore[1];
  }
  
  public void addMapleScore() {
    this.coconutscore[0] = this.coconutscore[0] + 1;
  }
  
  public void addStoryScore() {
    this.coconutscore[1] = this.coconutscore[1] + 1;
  }
  
  public void resetCoconutScore() {
    this.coconutscore[0] = 0;
    this.coconutscore[1] = 0;
    this.countBombing = 80;
    this.countFalling = 401;
    this.countStopped = 20;
    this.coconuts.clear();
    for (int i = 0; i < 506; i++)
      this.coconuts.add(new MapleCoconuts()); 
  }
  
  public void startEvent() {
    reset();
    setHittable(true);
    getMap(0).broadcastMessage(CWvsContext.serverNotice(5, "", "The event has started!!"));
    getMap(0).broadcastMessage(CField.getClock(300));
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            if (MapleCoconut.this.getMapleScore() == MapleCoconut.this.getStoryScore()) {
              MapleCoconut.this.bonusTime();
            } else {
              for (MapleCharacter chr : MapleCoconut.this.getMap(0).getCharactersThreadsafe()) {
                if (chr.getTeam() == ((MapleCoconut.this.getMapleScore() > MapleCoconut.this.getStoryScore()) ? 0 : 1)) {
                  chr.getClient().getSession().writeAndFlush(CField.showEffect("event/coconut/victory"));
                  chr.getClient().getSession().writeAndFlush(CField.playSound("Coconut/Victory"));
                  continue;
                } 
                chr.getClient().getSession().writeAndFlush(CField.showEffect("event/coconut/lose"));
                chr.getClient().getSession().writeAndFlush(CField.playSound("Coconut/Failed"));
              } 
              MapleCoconut.this.warpOut();
            } 
          }
        },  300000L);
  }
  
  public void bonusTime() {
    getMap(0).broadcastMessage(CField.getClock(60));
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            if (MapleCoconut.this.getMapleScore() == MapleCoconut.this.getStoryScore()) {
              for (MapleCharacter chr : MapleCoconut.this.getMap(0).getCharactersThreadsafe()) {
                chr.getClient().getSession().writeAndFlush(CField.showEffect("event/coconut/lose"));
                chr.getClient().getSession().writeAndFlush(CField.playSound("Coconut/Failed"));
              } 
              MapleCoconut.this.warpOut();
            } else {
              for (MapleCharacter chr : MapleCoconut.this.getMap(0).getCharactersThreadsafe()) {
                if (chr.getTeam() == ((MapleCoconut.this.getMapleScore() > MapleCoconut.this.getStoryScore()) ? 0 : 1)) {
                  chr.getClient().getSession().writeAndFlush(CField.showEffect("event/coconut/victory"));
                  chr.getClient().getSession().writeAndFlush(CField.playSound("Coconut/Victory"));
                  continue;
                } 
                chr.getClient().getSession().writeAndFlush(CField.showEffect("event/coconut/lose"));
                chr.getClient().getSession().writeAndFlush(CField.playSound("Coconut/Failed"));
              } 
              MapleCoconut.this.warpOut();
            } 
          }
        },  60000L);
  }
  
  public void warpOut() {
    setHittable(false);
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (MapleCharacter chr : MapleCoconut.this.getMap(0).getCharactersThreadsafe()) {
              if ((MapleCoconut.this.getMapleScore() > MapleCoconut.this.getStoryScore() && chr.getTeam() == 0) || (MapleCoconut.this.getStoryScore() > MapleCoconut.this.getMapleScore() && chr.getTeam() == 1))
                MapleEvent.givePrize(chr); 
              MapleCoconut.this.warpBack(chr);
            } 
            MapleCoconut.this.unreset();
          }
        },  10000L);
  }
  
  public static class MapleCoconuts {
    private int hits = 0;
    
    private boolean hittable = false;
    
    private boolean stopped = false;
    
    private long hittime = System.currentTimeMillis();
    
    public void hit() {
      this.hittime = System.currentTimeMillis() + 1000L;
      this.hits++;
    }
    
    public int getHits() {
      return this.hits;
    }
    
    public void resetHits() {
      this.hits = 0;
    }
    
    public boolean isHittable() {
      return this.hittable;
    }
    
    public void setHittable(boolean hittable) {
      this.hittable = hittable;
    }
    
    public boolean isStopped() {
      return this.stopped;
    }
    
    public void setStopped(boolean stopped) {
      this.stopped = stopped;
    }
    
    public long getHitTime() {
      return this.hittime;
    }
  }
}
