package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import server.marriage.MarriageMiniBox;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MarriageHandler {
  public static final void UseItem(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
    slea.skip(4);
    int playerid = slea.readInt();
    short quantity = slea.readShort();
    c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
    MapleCharacter victim = chr.getMap().getCharacterById(playerid);
    if (victim != null && chr.getMap().getId() == 680000900 && victim.getMapId() == 680000900) {
      if (chr.getMarriageId() != 0 || victim.getMarriageId() != 0) {
        chr.dropMessage(6, "이미 결혼한 " + ((chr.getMarriageId() != 0) ? "상태입니다" : "상대와는 결혼하실수 없습니다"));
        return;
      } 
      String desc = chr.getName() + " ♥ " + victim.getName();
      MarriageMiniBox game = new MarriageMiniBox(chr, 5250500, desc, "", 7, victim.getId());
      game.setPlayer1(chr);
      chr.setPlayerShop(game);
      game.setAvailable(false);
      game.setOpen(true);
      game.send(c);
      chr.getMap().addMapObject(game);
      game.update();
      chr.setMarriage(game);
      victim.setMarriage(game);
      victim.getClient().getSession().writeAndFlush(CField.InteractionPacket.getMarriageInvite(chr));
    } 
  }
}
