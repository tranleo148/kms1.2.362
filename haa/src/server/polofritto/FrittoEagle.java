package server.polofritto;

import client.MapleClient;
import java.awt.Point;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class FrittoEagle {
  private ScheduledFuture<?> sc;
  
  private ScheduledFuture<?> sch;
  
  private int score;
  
  private int bullet;
  
  public FrittoEagle(int score, int bullet) {
    this.score = score;
    this.bullet = bullet;
  }
  
  public void createGun(MapleClient c) {
    c.getSession().writeAndFlush(SLFCGPacket.createGun());
    c.getSession().writeAndFlush(SLFCGPacket.setGun());
    c.getSession().writeAndFlush(SLFCGPacket.setAmmo(this.bullet));
  }
  
  public void checkFinish(final MapleClient c) {
    this.sch = Timer.EventTimer.getInstance().register(new Runnable() {
          public void run() {
            if (c != null && c.getPlayer() != null && c.getPlayer().getMap() != null) {
              int size = 0;
              for (MapleMonster m : c.getPlayer().getMap().getAllMonster()) {
                if (m != null && m.getId() >= 9833000 && m.getId() <= 9833002)
                  size++; 
              } 
              if (FrittoEagle.this.score >= 1000 || c.getPlayer().getMap().getNumMonsters() == 0 || size == 0) {
                if (FrittoEagle.this.sc != null)
                  FrittoEagle.this.sc.cancel(false); 
                if (FrittoEagle.this.sch != null)
                  FrittoEagle.this.sch.cancel(false); 
                c.getSession().writeAndFlush(CField.environmentChange("killing/clear", 16));
                c.getSession().writeAndFlush(SLFCGPacket.milliTimer(3000));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                      public void run() {
                        if (c != null && c.getPlayer() != null) {
                          c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
                          c.getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", new int[] { 10, 0 }));
                          c.getPlayer().warp(993000601);
                        } 
                      }
                    },3000L);
              } 
            } 
          }
        },1000L);
  }
  
  public void addScore(MapleMonster monster, MapleClient c) {
    int be = this.score;
    switch (monster.getId()) {
      case 9833000:
        this.score += 50;
        break;
      case 9833001:
        this.score += 100;
        break;
      case 9833002:
        this.score += 200;
        break;
      case 9833003:
        this.score -= 50;
        break;
    } 
    c.getPlayer().setKeyValue(15141, "point", String.valueOf(this.score));
    c.getSession().writeAndFlush(SLFCGPacket.deadOnFPSMode(monster.getObjectId(), this.score - be));
  }
  
  public void updateNewWave(final MapleClient c) {
    c.getSession().writeAndFlush(CField.environmentChange("killing/first/start", 16));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833000), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833000), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833000), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833000), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833000), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833000), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833001), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833001), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833001), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833002), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833002), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833003), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833003), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833003), new Point(0, 0));
    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833003), new Point(0, 0));
    Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            FrittoEagle.this.checkFinish(c);
          }
        },  2000L);
  }
  
  public void shootResult(MapleClient c) {
    if (this.bullet > 1) {
      c.getSession().writeAndFlush(SLFCGPacket.attackRes());
    } else {
      if (this.sc != null)
        this.sc.cancel(false); 
      if (this.sch != null)
        this.sch.cancel(false); 
      c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
      c.getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", new int[] { 10, 0 }));
      c.getPlayer().warp(993000601);
    } 
  }
  
  public void start(final MapleClient c) {
    createGun(c);
    c.getPlayer().setKeyValue(15141, "point", "0");
    c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    c.getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", new int[] { 10, 1 }));
    c.getSession().writeAndFlush(CField.environmentChange("PoloFritto/msg1", 20));
    c.getSession().writeAndFlush(CField.startMapEffect("독수리를 침착하게 한 마리씩 잡도록 해! 아참, 대머리 독수리는 아무 쓸모 없으니 잡지마!", 5120160, true));
    c.getSession().writeAndFlush(SLFCGPacket.milliTimer(30000));
    updateNewWave(c);
    this.sc = Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            if (c.getPlayer().getMapId() == 993000200) {
              c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
              c.getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", new int[] { 10, 0 }));
              c.getPlayer().warp(993000601);
            } 
          }
        },30000L);
  }
  
  public ScheduledFuture<?> getSch() {
    return this.sch;
  }
  
  public void setSch(ScheduledFuture<?> sch) {
    this.sch = sch;
  }
  
  public ScheduledFuture<?> getSc() {
    return this.sc;
  }
  
  public void setSc(ScheduledFuture<?> sc) {
    this.sc = sc;
  }
  
  public int getScore() {
    return this.score;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public int getBullet() {
    return this.bullet;
  }
  
  public void setBullet(int bullet) {
    this.bullet = bullet;
  }
}
