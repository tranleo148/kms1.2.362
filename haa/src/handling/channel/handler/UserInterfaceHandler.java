package handling.channel.handler;

import client.MapleClient;
import scripting.NPCScriptManager;

public class UserInterfaceHandler {
  public static final void CygnusSummon_NPCRequest(MapleClient c) {
    if (c.getPlayer().getJob() == 2000) {
      NPCScriptManager.getInstance().start(c, 1202000);
    } else if (c.getPlayer().getJob() == 1000) {
      NPCScriptManager.getInstance().start(c, 1101008);
    } 
  }
  
  public static final void ShipObjectRequest(int mapid, MapleClient c) {}
}
