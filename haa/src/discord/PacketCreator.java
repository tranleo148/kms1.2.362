package discord;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import java.util.ArrayList;
import java.util.List;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;

public class PacketCreator {
  public static final byte[] sendBasicInfo() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write((byte)SendPacketOpcode.RpBasicInfo.getValue());
    mplew.writeMapleAsciiString2("Island 유저들을 도와주는 중");
    return mplew.getPacket();
  }
  
  public static final byte[] sendPlayerList(LittleEndianAccessor slea) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    List<String> list = new ArrayList<>();
    boolean isGM = false;
    mplew.write((byte)SendPacketOpcode.RpPlayerList.getValue());
    mplew.writeLong(slea.readLong());
    isGM = (slea.readByte() == 1);
    for (ChannelServer cs : ChannelServer.getAllInstances())
      list.addAll(cs.getPlayerStorage().getOnlinePlayers2(isGM)); 
    mplew.writeShort(list.size());
    list.forEach(s -> mplew.writeMapleAsciiString2(s));
    return mplew.getPacket();
  }
  
  public static final byte[] sendMegaphone(MapleCharacter chr, String msg) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write((byte)SendPacketOpcode.MegaPhone.getValue());
    mplew.writeInt(chr.getId());
    mplew.writeInt((chr.getClient().getChannel() == 0) ? 1 : chr.getClient().getChannel());
    mplew.writeMapleAsciiString2(chr.getName());
    mplew.writeMapleAsciiString2(msg);
    mplew.writeLong(System.currentTimeMillis());
    return mplew.getPacket();
  }
  
  public static final byte[] sendGmResult(LittleEndianAccessor slea) {
    boolean isSuccess = false;
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write((byte)SendPacketOpcode.RpGiveGM.getValue());
    mplew.writeLong(slea.readLong());
    String name = slea.readMapleAsciiString2();
    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
      MapleCharacter player = null;
      player = cserv.getPlayerStorage().getCharacterByName(name);
      if (player != null) {
        player.setGMLevel((byte)10);
        isSuccess = true;
      } 
    } 
    mplew.write(isSuccess);
    return mplew.getPacket();
  }
  
  public static final byte[] sendMessageToChannel(String text) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write((byte)SendPacketOpcode.SendMessage.getValue());
    mplew.write(1);
    mplew.writeMapleAsciiString2(text);
    return mplew.getPacket();
  }
  
  public static final byte[] sendEmbedMessageToChannel(String title, String message, String thumbnailUrl, byte r, byte g, byte b) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write((byte)SendPacketOpcode.SendMessage.getValue());
    mplew.write(2);
    mplew.writeMapleAsciiString2(title);
    mplew.writeMapleAsciiString2(message);
    mplew.writeMapleAsciiString2(thumbnailUrl);
    mplew.write(r);
    mplew.write(g);
    mplew.write(b);
    return mplew.getPacket();
  }
  
  public static final byte[] sendMessageToUser(long userId, String text) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write((byte)SendPacketOpcode.SendMessage.getValue());
    mplew.write(3);
    mplew.writeLong(userId);
    mplew.writeMapleAsciiString2(text);
    return mplew.getPacket();
  }
  
  public static final byte[] sendEmbedMessageToUser(long userId, String title, String message, String thumbnailUrl, byte r, byte g, byte b) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.write((byte)SendPacketOpcode.SendMessage.getValue());
    mplew.write(4);
    mplew.writeLong(userId);
    mplew.writeMapleAsciiString2(title);
    mplew.writeMapleAsciiString2(message);
    mplew.writeMapleAsciiString2(thumbnailUrl);
    mplew.write(r);
    mplew.write(g);
    mplew.write(b);
    return mplew.getPacket();
  }
}
