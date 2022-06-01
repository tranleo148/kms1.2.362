package server.polofritto;

import client.MapleClient;
import java.util.concurrent.ScheduledFuture;
import scripting.NPCScriptManager;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

public class FrittoEgg {
  private ScheduledFuture<?> sc;
  
  private int stage;
  
  public FrittoEgg(int stage) {
    this.stage = stage;
  }
  
  public void finishStage(MapleClient c, int stage) {
    this.stage = stage;
    c.getSession().writeAndFlush(CField.environmentChange("killing/fail", 16));
    c.getPlayer().setKeyValue(15042, "Stage", String.valueOf(stage));
    NPCScriptManager.getInstance().start(c, 9001060, "frittoEggEnd");
  }
  
  public void updateStage(MapleClient c, int stage) {
    this.stage = stage;
    c.getPlayer().setKeyValue(15042, "Stage", String.valueOf(stage));
    c.getSession().writeAndFlush(CField.environmentChange("killing/clear", 16));
    if (stage == 5) {
      MapleNPC npc = MapleLifeFactory.getNPC(9001061);
      if (npc != null && !npc.getName().equals("MISSINGNO")) {
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy((c.getPlayer().getPosition()).y);
        npc.setRx0((c.getPlayer().getPosition()).x + 50);
        npc.setRx1((c.getPlayer().getPosition()).x - 50);
        npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        npc.setCustom(true);
        c.getPlayer().getMap().addMapObject(npc);
        c.getSession().writeAndFlush(CField.NPCPacket.spawnNPC(npc, true));
      } 
    } 
  }
  
  public void start(final MapleClient c) {
    c.getPlayer().setKeyValue(15142, "Stage", "0");
    c.getSession().writeAndFlush(CField.environmentChange("PoloFritto/msg2", 20));
    c.getSession().writeAndFlush(CField.startMapEffect("쉿! 이 둥지의 꼭대기에는 드래곤의 알이 숨겨져 있어. 꼭대기로 가는 길을 잘 찾아보라구!", 5120160, true));
    c.getSession().writeAndFlush(SLFCGPacket.milliTimer(60000));
    c.getPlayer().getMap().removeNpc(9001061);
    this.sc = Timer.EventTimer.getInstance().schedule(new Runnable() {
          public void run() {
            if (c.getPlayer().getMapId() == 993000300)
              c.getPlayer().warp(993000601); 
          }
        },  60000L);
  }
  
  public ScheduledFuture<?> getSc() {
    return this.sc;
  }
  
  public void setSc(ScheduledFuture<?> sc) {
    this.sc = sc;
  }
  
  public int getStage() {
    return this.stage;
  }
  
  public void setStage(int stage) {
    this.stage = stage;
  }
}
