package ellesia.connector.netty;

import ellesia.connector.EllesiaPacketOpcode;
import tools.data.MaplePacketLittleEndianWriter;

public class EllesiaPacketCreator {

    public static byte[] Login(boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(EllesiaPacketOpcode.LOGIN.getValue());
        mplew.write(success);
        return mplew.getPacket();
    }

    public static byte[] RegisterResult(boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(EllesiaPacketOpcode.REGISTER.getValue());
        mplew.write(success);
        return mplew.getPacket();
    }

    public static byte[] Version(int version) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(EllesiaPacketOpcode.VERSION.getValue());
        mplew.writeInt(version);
        return mplew.getPacket();
    }

    public static byte[] FileList(String[] files) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(EllesiaPacketOpcode.FILE_LIST.getValue());
        System.out.println(files.length);
        mplew.writeInt(files.length);
        for(String file : files) {
            mplew.writeMapleAsciiString(file);
        }
        return mplew.getPacket();
    }
    public static byte[] WzResult(String wz, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(EllesiaPacketOpcode.CHECKSUM.getValue());
        mplew.writeMapleAsciiString(wz);
        mplew.write(success);
        return mplew.getPacket();
    }
}
