package server.polofritto;

import client.MapleClient;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.Randomizer;
import server.Timer;
import server.life.MapleLifeFactory;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class BountyHunting {
  private int stage;
  
  private ScheduledFuture<?> sc;
  
  private ScheduledFuture<?> sch;
  
  private List<List<Integer>> waveData = new ArrayList<>();
  
  public BountyHunting(int stage) {
    this.stage = stage;
  }
  
  public int getStage() {
    return this.stage;
  }
  
  public void setStage(int wave) {
    this.stage = wave;
  }
  
  public void updateDefenseWave(MapleClient c) {
    c.getSession().writeAndFlush(SLFCGPacket.setBountyHuntingStage(this.stage));
  }
  
  public void updateNewWave(final MapleClient c) {
    c.getSession().writeAndFlush(CField.environmentChange("defense/count", 16));
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            if (c.getPlayer().getMapId() == 993000000) {
              c.getSession().writeAndFlush(CField.environmentChange("defense/wave/" + BountyHunting.this.stage, 16));
              c.getSession().writeAndFlush(CField.environmentChange("killing/first/start", 16));
              for (Iterator<Integer> iterator = ((List)BountyHunting.this.waveData.get(BountyHunting.this.stage - 1)).iterator(); iterator.hasNext(); ) {
                int wave = ((Integer)iterator.next()).intValue();
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(wave), new Point(Randomizer.rand(500, 700) * -1, 126));
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(wave), new Point(Randomizer.rand(500, 700), 126));
              } 
            } 
          }
        },3000L);
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            BountyHunting.this.checkFinish(c);
          }
        },  8000L);
  }
  
  public void checkFinish(final MapleClient c) {
    this.sch = Timer.EventTimer.getInstance().register(new Runnable() {
          public void run() {
            if (c != null && c.getPlayer() != null && c.getPlayer().getMap() != null && 
              c.getPlayer().getMap().getNumMonsters() == 0)
              if (BountyHunting.this.stage < 5 && c.getPlayer().getMapId() == 993000000) {
                BountyHunting.this.stage++;
                if (BountyHunting.this.sch != null)
                  BountyHunting.this.sch.cancel(true); 
                BountyHunting.this.updateDefenseWave(c);
                BountyHunting.this.updateNewWave(c);
              } else {
                if (BountyHunting.this.sc != null)
                  BountyHunting.this.sc.cancel(true); 
                if (BountyHunting.this.sch != null)
                  BountyHunting.this.sch.cancel(true); 
                c.getSession().writeAndFlush(CField.environmentChange("killing/clear", 16));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                      public void run() {
                        if (c != null && c.getPlayer() != null && c.getPlayer().getMapId() == 993000000)
                          c.getPlayer().warp(993000600); 
                      }
                    },  2000L);
              }  
          }
        },1000L);
  }
  
  public void insertWaveData() {
    List<Integer> waves = new ArrayList<>();
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830001));
    waves.add(Integer.valueOf(9830001));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    waves.add(Integer.valueOf(9830000));
    this.waveData.add(waves);
    List<Integer> waves2 = new ArrayList<>();
    waves2.add(Integer.valueOf(9830002));
    waves2.add(Integer.valueOf(9830002));
    waves2.add(Integer.valueOf(9830002));
    waves2.add(Integer.valueOf(9830002));
    waves2.add(Integer.valueOf(9830002));
    waves2.add(Integer.valueOf(9830004));
    waves2.add(Integer.valueOf(9830004));
    waves2.add(Integer.valueOf(9830003));
    waves2.add(Integer.valueOf(9830003));
    waves2.add(Integer.valueOf(9830003));
    waves2.add(Integer.valueOf(9830003));
    waves2.add(Integer.valueOf(9830003));
    this.waveData.add(waves2);
    List<Integer> waves3 = new ArrayList<>();
    waves3.add(Integer.valueOf(9830005));
    waves3.add(Integer.valueOf(9830005));
    waves3.add(Integer.valueOf(9830005));
    waves3.add(Integer.valueOf(9830005));
    waves3.add(Integer.valueOf(9830005));
    waves3.add(Integer.valueOf(9830006));
    waves3.add(Integer.valueOf(9830006));
    waves3.add(Integer.valueOf(9830007));
    waves3.add(Integer.valueOf(9830007));
    waves3.add(Integer.valueOf(9830007));
    waves3.add(Integer.valueOf(9830007));
    waves3.add(Integer.valueOf(9830007));
    waves3.add(Integer.valueOf(9830008));
    this.waveData.add(waves3);
    List<Integer> waves4 = new ArrayList<>();
    waves4.add(Integer.valueOf(9830009));
    waves4.add(Integer.valueOf(9830009));
    waves4.add(Integer.valueOf(9830009));
    waves4.add(Integer.valueOf(9830009));
    waves4.add(Integer.valueOf(9830009));
    waves4.add(Integer.valueOf(9830010));
    waves4.add(Integer.valueOf(9830010));
    waves4.add(Integer.valueOf(9830010));
    waves4.add(Integer.valueOf(9830010));
    waves4.add(Integer.valueOf(9830010));
    waves4.add(Integer.valueOf(9830011));
    waves4.add(Integer.valueOf(9830011));
    waves4.add(Integer.valueOf(9830011));
    waves4.add(Integer.valueOf(9830011));
    waves4.add(Integer.valueOf(9830011));
    waves4.add(Integer.valueOf(9830012));
    waves4.add(Integer.valueOf(9830012));
    waves4.add(Integer.valueOf(9830012));
    waves4.add(Integer.valueOf(9830012));
    waves4.add(Integer.valueOf(9830012));
    waves4.add(Integer.valueOf(9830013));
    this.waveData.add(waves4);
    List<Integer> waves5 = new ArrayList<>();
    waves5.add(Integer.valueOf(9830014));
    waves5.add(Integer.valueOf(9830014));
    waves5.add(Integer.valueOf(9830014));
    waves5.add(Integer.valueOf(9830014));
    waves5.add(Integer.valueOf(9830014));
    waves5.add(Integer.valueOf(9830015));
    waves5.add(Integer.valueOf(9830015));
    waves5.add(Integer.valueOf(9830015));
    waves5.add(Integer.valueOf(9830015));
    waves5.add(Integer.valueOf(9830015));
    waves5.add(Integer.valueOf(9830016));
    waves5.add(Integer.valueOf(9830016));
    waves5.add(Integer.valueOf(9830016));
    waves5.add(Integer.valueOf(9830016));
    waves5.add(Integer.valueOf(9830016));
    waves5.add(Integer.valueOf(9830017));
    waves5.add(Integer.valueOf(9830017));
    waves5.add(Integer.valueOf(9830017));
    waves5.add(Integer.valueOf(9830017));
    waves5.add(Integer.valueOf(9830017));
    waves5.add(Integer.valueOf(9830018));
    this.waveData.add(waves5);
  }
  
  public void start(final MapleClient c) {
    updateDefenseWave(c);
    insertWaveData();
    c.getSession().writeAndFlush(CField.startMapEffect("놈들이 사방에서 몰려오는군! 녀석들을 처치하면 막대한 경험치를 얻을 수 있다!", 5120159, true));
    c.getSession().writeAndFlush(CField.getClock(180));
    updateNewWave(c);
    this.sc = Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            if (BountyHunting.this.sc != null)
              BountyHunting.this.sc.cancel(true); 
            if (BountyHunting.this.sch != null)
              BountyHunting.this.sch.cancel(true); 
            if (c.getPlayer().getMapId() == 993000000)
              c.getPlayer().warp(993000600); 
          }
        },  180000L);
  }
  
  public ScheduledFuture<?> getSch() {
    return this.sch;
  }
  
  public void setSch(ScheduledFuture<?> sch) {
    this.sch = sch;
  }
  
  public List<List<Integer>> getWaveData() {
    return this.waveData;
  }
  
  public void setWaveData(List<List<Integer>> waveData) {
    this.waveData = waveData;
  }
  
  public ScheduledFuture<?> getSc() {
    return this.sc;
  }
  
  public void setSc(ScheduledFuture<?> sc) {
    this.sc = sc;
  }
}
