package handling.farm.handler;

import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;

public class FarmHandler {
  public static void leaveFarm(MapleClient c, MapleCharacter chr) {
    FarmServer.getPlayerStorage().deregisterPlayer(chr);
    c.updateLoginState(1, c.getSessionIPAddress());
    try {
      PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
      PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
      World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
      c.getSession().writeAndFlush(CField.getChannelChange(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
    } finally {
      String s = c.getSessionIPAddress();
      LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
      chr.saveToDB(true, false);
      c.setPlayer(null);
      c.setFarm(false);
    } 
  }
  
  public static void enterFarm(MapleCharacter chr, MapleClient c) {
    if (c.getPlayer().getMapId() != ServerConstants.warpMap) {
      c.getPlayer().dropMessage(1, "농장은 Happy !  마을에서만 이용 가능합니다.");
      return;
    } 
    ChannelServer ch = ChannelServer.getInstance(c.getChannel());
    chr.changeRemoval();
    ch.removePlayer(chr);
    World.isCharacterListConnected(c.getPlayer().getName(), c.loadCharacterNames(c.getWorld()));
    FarmServer.getPlayerStorage().registerPlayer(chr);
    chr.saveToDB(true, false);
    chr.getMap().removePlayer(chr);
    c.getSession().writeAndFlush(CField.FarmPacket.onEnterFarm(chr));
    c.getSession().writeAndFlush(CField.FarmPacket.onSetFarmUser(chr));
    c.getSession().writeAndFlush(CField.FarmPacket.onFarmSetInGameInfo(chr));
    c.getSession().writeAndFlush(CField.FarmPacket.onFarmRequestSetInGameInfo(chr));
    if (c.getFarmImg() != null)
      c.getSession().writeAndFlush(CField.FarmPacket.onFarmImgUpdate(c, (c.getFarmImg()).length, c.getFarmImg())); 
    c.getSession().writeAndFlush(CField.FarmPacket.onFarmNotice("지금은 이미지 수정만 가능합니다."));
    c.setFarm(true);
  }
  
  public static void updateFarmImg(LittleEndianAccessor slea, MapleClient c) {
    int length = slea.readInt();
    byte[] img = slea.read(length);
    c.setFarmImg(img);
    c.getSession().writeAndFlush(CField.FarmPacket.onFarmImgUpdate(c, length, img));
  }
}
