package discord;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import handling.world.World;
import java.util.ArrayList;
import java.util.List;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class DiscordServerHandler {
  public static final void HandlePacket(RecvPacketOpcode header, LittleEndianAccessor slea, DiscordClient c) throws InterruptedException {
    switch (header) {
      case RqBasicInfo:
        c.sendPacket(PacketCreator.sendBasicInfo());
        break;
      case RqPlayerList:
        c.sendPacket(PacketCreator.sendPlayerList(slea));
        break;
      case RqGiveGM:
        c.sendPacket(PacketCreator.sendGmResult(slea));
        break;
      case Megaphone:
        World.Broadcast.broadcastSmega(CWvsContext.itemMegaphone("", "[디코] " + slea.readMapleAsciiString2(), true, 0, null, 5076100));
        break;
      case RqWorldNotice:
        World.Broadcast.broadcastMessage(CField.getGameMessage(slea.readByte(), slea.readMapleAsciiString2()));
        break;
    } 
  }
  
  public static void AllMessage(byte[] data) {}
  
  public static List<String> userList() {
    List<String> list = new ArrayList<>();
    for (ChannelServer cs : ChannelServer.getAllInstances()) {
      for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
        if (chr.getName() != null)
          list.add(chr.getName()); 
      } 
    } 
    return list;
  }
}
