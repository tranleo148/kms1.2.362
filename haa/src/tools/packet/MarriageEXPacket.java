package tools.packet;

import client.AvatarLook;
import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import handling.SendPacketOpcode;
import handling.channel.handler.PlayerInteractionHandler;
import server.marriage.MarriageMiniBox;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class MarriageEXPacket {
  public static byte[] MarriageRoom(MapleClient c, MarriageMiniBox marriage) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(20);
    mplew.write(8);
    mplew.write(marriage.getMaxSize());
    mplew.writeShort(marriage.getVisitorSlot(c.getPlayer()));
    AvatarLook.encodeAvatarLook(mplew, marriage.getMCOwner(), false, (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1));
    mplew.writeMapleAsciiString(marriage.getOwnerName());
    mplew.writeShort(marriage.getMCOwner().getJob());
    for (Pair<Byte, MapleCharacter> visitorz : marriage.getVisitors()) {
      mplew.write(((Byte)visitorz.getLeft()).byteValue());
      AvatarLook.encodeAvatarLook(mplew, visitorz.getRight(), false, (GameConstants.isZero(((MapleCharacter)visitorz.right).getJob()) && ((MapleCharacter)visitorz.right).getGender() == 1));
      mplew.writeMapleAsciiString(((MapleCharacter)visitorz.getRight()).getName());
      mplew.writeShort(((MapleCharacter)visitorz.getRight()).getJob());
    } 
    mplew.write(-1);
    mplew.writeZeroBytes(10);
    return mplew.getPacket();
  }
  
  public static final byte[] MarriageVisit(MapleCharacter chr, int slot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
    mplew.write(PlayerInteractionHandler.Interaction.VISIT.action);
    mplew.write(slot);
    AvatarLook.encodeAvatarLook(mplew, chr, false, (GameConstants.isZero(chr.getJob()) && chr.getGender() == 1));
    mplew.writeMapleAsciiString(chr.getName());
    mplew.writeShort(chr.getJob());
    mplew.writeInt(0);
    return mplew.getPacket();
  }
}
