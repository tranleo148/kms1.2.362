package tools.packet;

import tools.data.MaplePacketLittleEndianWriter;

public class MarriagePacket {
  public static byte[] onMarriage(int type) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(1571);
    return mplew.getPacket();
  }
}
