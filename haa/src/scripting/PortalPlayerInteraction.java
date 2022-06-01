package scripting;

import client.MapleClient;
import server.MaplePortal;

public class PortalPlayerInteraction extends AbstractPlayerInteraction {
  private final MaplePortal portal;
  
  public PortalPlayerInteraction(MapleClient c, MaplePortal portal) {
    super(c, portal.getId(), c.getPlayer().getMapId());
    this.portal = portal;
  }
  
  public final MaplePortal getPortal() {
    return this.portal;
  }
  
  public final void inFreeMarket() {
    if (getMapId() != 910000000) {
      saveLocation("FREE_MARKET");
      playPortalSE();
      warp(910000000, "st00");
    } 
  }
  
  public final void inArdentmill() {
    if (getMapId() != 910001000)
      if (getPlayer().getLevel() >= 30) {
        saveLocation("ARDENTMILL");
        playPortalSE();
        warp(910001000, "st00");
      } else {
        playerMessage(5, "?덈꺼 30 ?댁긽 ?섏뼱??留덉씠?ㅽ꽣 鍮뚮줈 ?대룞?섏떎 ???덉뒿?덈떎.");
      }  
  }
  
  public void spawnMonster(int id) {
    spawnMonster(id, 1, this.portal.getPosition());
  }
  
  public void spawnMonster(int id, int qty) {
    spawnMonster(id, qty, this.portal.getPosition());
  }
}
