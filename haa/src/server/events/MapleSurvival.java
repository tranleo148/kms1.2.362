package server.events;

import client.MapleCharacter;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleSurvival extends MapleEvent {
  protected long time = 360000L;
  
  protected long timeStarted = 0L;
  
  protected ScheduledFuture<?> olaSchedule;
  
  public MapleSurvival(int channel, MapleEventType type) {
    super(channel, type);
  }
  
  public void finished(MapleCharacter chr) {
    givePrize(chr);
  }
  
  public void onMapLoad(MapleCharacter chr) {
    super.onMapLoad(chr);
    if (isTimerStarted())
      chr.getClient().getSession().writeAndFlush(CField.getClock((int)(getTimeLeft() / 1000L))); 
  }
  
  public void startEvent() {
    unreset();
    super.reset();
    broadcast(CField.getClock((int)(this.time / 1000L)));
    this.timeStarted = System.currentTimeMillis();
    this.olaSchedule = Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (int i = 0; i < MapleSurvival.this.type.mapids.length; i++) {
              for (MapleCharacter chr : MapleSurvival.this.getMap(i).getCharactersThreadsafe())
                MapleSurvival.this.warpBack(chr); 
              MapleSurvival.this.unreset();
            } 
          }
        },this.time);
    broadcast(CWvsContext.serverNotice(0, "", "The portal has now opened. Press the up arrow key at the portal to enter."));
    broadcast(CWvsContext.serverNotice(0, "", "Fall down once, and never get back up again! Get to the top without falling down!"));
  }
  
  public boolean isTimerStarted() {
    return (this.timeStarted > 0L);
  }
  
  public long getTime() {
    return this.time;
  }
  
  public void resetSchedule() {
    this.timeStarted = 0L;
    if (this.olaSchedule != null)
      this.olaSchedule.cancel(false); 
    this.olaSchedule = null;
  }
  
  public void reset() {
    super.reset();
    resetSchedule();
    getMap(0).getPortal("join00").setPortalState(false);
  }
  
  public void unreset() {
    super.unreset();
    resetSchedule();
    getMap(0).getPortal("join00").setPortalState(true);
  }
  
  public long getTimeLeft() {
    return this.time - System.currentTimeMillis() - this.timeStarted;
  }
}
