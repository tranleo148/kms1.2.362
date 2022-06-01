package server.events;

import client.MapleCharacter;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleFitness extends MapleEvent {
  private static final long serialVersionUID = 845748950824L;
  
  private long time = 600000L;
  
  private long timeStarted = 0L;
  
  private ScheduledFuture<?> fitnessSchedule;
  
  private ScheduledFuture<?> msgSchedule;
  
  public MapleFitness(int channel, MapleEventType type) {
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
    checkAndMessage();
    this.fitnessSchedule = Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            for (int i = 0; i < MapleFitness.this.type.mapids.length; i++) {
              for (MapleCharacter chr : MapleFitness.this.getMap(i).getCharactersThreadsafe())
                MapleFitness.this.warpBack(chr); 
            } 
            MapleFitness.this.unreset();
          }
        },this.time);
    broadcast(CWvsContext.serverNotice(0, "", "The portal has now opened. Press the up arrow key at the portal to enter."));
  }
  
  public boolean isTimerStarted() {
    return (this.timeStarted > 0L);
  }
  
  public long getTime() {
    return this.time;
  }
  
  public void resetSchedule() {
    this.timeStarted = 0L;
    if (this.fitnessSchedule != null)
      this.fitnessSchedule.cancel(false); 
    this.fitnessSchedule = null;
    if (this.msgSchedule != null)
      this.msgSchedule.cancel(false); 
    this.msgSchedule = null;
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
  
  public void checkAndMessage() {
    this.msgSchedule = Timer.EventTimer.getInstance().register(new Runnable() {
          public void run() {
            long timeLeft = MapleFitness.this.getTimeLeft();
            if (timeLeft > 9000L && timeLeft < 11000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "You have 10 sec left. Those of you unable to beat the game, we hope you beat it next time! Great job everyone!! See you later~"));
            } else if (timeLeft > 11000L && timeLeft < 101000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "Alright, you don't have much time remaining. Please hurry up a little!"));
            } else if (timeLeft > 101000L && timeLeft < 201000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "The 4th stage is the last one for [The Maple Physical Fitness Test]. Please don't give up at the last minute and try your best. The reward is waiting for you at the very top!"));
            } else if (timeLeft > 201000L && timeLeft < 301000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "The 3rd stage offers traps where you may see them, but you won't be able to step on them. Please be careful of them as you make your way up."));
            } else if (timeLeft > 301000L && timeLeft < 361000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "For those who have heavy lags, please make sure to move slowly to avoid falling all the way down because of lags."));
            } else if (timeLeft > 361000L && timeLeft < 501000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "Please remember that if you die during the event, you'll be eliminated from the game. If you're running out of HP, either take a potion or recover HP first before moving on."));
            } else if (timeLeft > 501000L && timeLeft < 601000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "The most important thing you'll need to know to avoid the bananas thrown by the monkeys is *Timing* Timing is everything in this!"));
            } else if (timeLeft > 601000L && timeLeft < 661000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "The 2nd stage offers monkeys throwing bananas. Please make sure to avoid them by moving along at just the right timing."));
            } else if (timeLeft > 661000L && timeLeft < 701000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "Please remember that if you die during the event, you'll be eliminated from the game. You still have plenty of time left, so either take a potion or recover HP first before moving on."));
            } else if (timeLeft > 701000L && timeLeft < 781000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "Everyone that clears [The Maple Physical Fitness Test] on time will be given an item, regardless of the order of finish, so just relax, take your time, and clear the 4 stages."));
            } else if (timeLeft > 781000L && timeLeft < 841000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "There may be a heavy lag due to many users at stage 1 all at once. It won't be difficult, so please make sure not to fall down because of heavy lag."));
            } else if (timeLeft > 841000L) {
              MapleFitness.this.broadcast(CWvsContext.serverNotice(0, "", "[MapleStory Physical Fitness Test] consists of 4 stages, and if you happen to die during the game, you'll be eliminated from the game, so please be careful of that."));
            } 
          }
        },60000L);
  }
}
