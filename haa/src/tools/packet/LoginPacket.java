package tools.packet;

import client.AvatarLook;
import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import constants.JobConstants;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tools.HexTool;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class LoginPacket {

    private static final String version;

    static {
        int ret = 0;
        ret ^= (ServerConstants.MAPLE_VERSION & 0x7FFF);
        ret ^= (1 << 15);
        ret ^= ((ServerConstants.MAPLE_PATCH & 0xFF) << 16);
        version = String.valueOf(ret);
    }

    public static final byte[] initializeConnection(final short mapleVersion, final byte[] sendIv, final byte[] recvIv, final boolean ingame) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        int ret = 0;
        ret ^= (mapleVersion & 0x7FFF);
        ret ^= (ServerConstants.check << 15);
        ret ^= ((ServerConstants.MAPLE_PATCH & 0xFF) << 16);
        String version = String.valueOf(ret);

        int packetsize = ingame ? 16 : 49;

        mplew.writeShort(packetsize);

        if (!ingame) {
            mplew.writeShort(291);
            mplew.writeMapleAsciiString("65898");
            mplew.write(recvIv);
            mplew.write(sendIv);
            mplew.write(1); // locale
            mplew.write(0); // single thread loading
        }

        mplew.writeShort(291);
        mplew.writeInt(mapleVersion);
        mplew.write(recvIv);
        mplew.write(sendIv);
        mplew.write(1); // locale

        if (!ingame) {
            mplew.writeInt(mapleVersion * 100 + ServerConstants.MAPLE_PATCH);
            mplew.writeInt(mapleVersion * 100 + ServerConstants.MAPLE_PATCH);
            mplew.writeInt(0); // unknown
            mplew.write(false);
            mplew.write(false);
        }
        mplew.write(0);
        return mplew.getPacket();
    }

    public static final byte[] getHotfix() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HOTFIX.getValue());
        mplew.write(0);
        return mplew.getPacket();
    }

    public static final byte[] SessionCheck(int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(6);
        mplew.writeShort(SendPacketOpcode.SESSION_CHECK.getValue());
        mplew.writeInt(value);
        return mplew.getPacket();
    }

    public static final byte[] debugClient() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DEBUG_CLIENT.getValue());
        int size1 = 12;
        mplew.write(HexTool.getByteArrayFromHexString("1C 00 00 00 01 00 00 00 6C 00 00 00 05 00 00 00 03 00 41 6C 6C 06 00 00 00 91 00 00 00 00 00 00 00 03 00 41 6C 6C 0A 00 00 00 9B 00 00 00 B4 1B 32 01 03 00 41 6C 6C 0B 00 00 00 9D 00 00 00 01 00 00 00 03 00 41 6C 6C 0F 00 00 00 A3 00 00 00 E8 80 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 30 10 00 00 00 A3 00 00 00 E8 80 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 33 11 00 00 00 A3 00 00 00 E8 80 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 35 12 00 00 00 A3 00 00 00 E8 80 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 31 36 13 00 00 00 A4 00 00 00 78 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 30 14 00 00 00 A4 00 00 00 78 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 33 15 00 00 00 A4 00 00 00 96 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 35 16 00 00 00 A4 00 00 00 96 00 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 31 36 17 00 00 00 AF 00 00 00 00 00 00 00 03 00 41 6C 6C 18 00 00 00 B1 00 00 00 00 00 00 00 03 00 41 6C 6C 1A 00 00 00 6F 00 00 00 01 00 00 00 03 00 41 6C 6C 1B 00 00 00 AC 00 00 00 01 00 00 00 03 00 41 6C 6C 1C 00 00 00 B0 00 00 00 00 00 00 00 03 00 41 6C 6C 1D 00 00 00 B2 00 00 00 1E 00 00 00 03 00 41 6C 6C 1E 00 00 00 B9 00 00 00 00 00 00 00 03 00 41 6C 6C 2A 00 00 00 BA 00 00 00 05 00 00 00 03 00 41 6C 6C 2B 00 00 00 A2 00 00 00 01 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 30 2C 00 00 00 A2 00 00 00 01 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 33 2F 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 39 30 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 38 31 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 35 32 35 00 00 00 C5 00 00 00 88 13 00 00 03 00 41 6C 6C 38 00 00 00 C6 00 00 00 00 00 00 00 03 00 41 6C 6C 39 00 00 00 CB 00 00 00 00 00 00 00 03 00 41 6C 6C"));
        return mplew.getPacket();
    }

    public static final byte[] HackShield() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HACKSHIELD.getValue());
        mplew.write(1);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static final byte[] enableLogin() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendPacketOpcode.ENABLE_LOGIN.getValue());
        return mplew.getPacket();
    }

    public static final byte[] checkLogin() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHECK_LOGIN.getValue());
        mplew.write(0);
        mplew.write(1);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static final byte[] successLogin() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SUCCESS_LOGIN.getValue());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(1);
        mplew.writeInt(1);
        return mplew.getPacket();
    }

    public static final byte[] getPing() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(2);
        mplew.writeShort(SendPacketOpcode.PING.getValue());
        return mplew.getPacket();
    }

    public static final byte[] getAuthSuccessRequest(MapleClient client, String id, String pwd) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(0);
        mplew.writeShort(0);
        mplew.writeMapleAsciiString(id);
        mplew.writeLong(772063122L);
        mplew.writeInt(client.getAccID());
        mplew.write(0);
        mplew.writeInt(130);
        mplew.writeInt(0);
        mplew.writeInt(22);
        mplew.write(3);
        mplew.write((client.getChatBlockedTime() > 0L) ? 1 : 0);
        mplew.writeLong(client.getChatBlockedTime());
        mplew.write(1);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.write(true);
        mplew.write(35);
        for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
            mplew.write(j.getFlag());
            mplew.writeShort(j.getFlag());
        }
        mplew.write(0);
        mplew.writeInt(-1);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static final byte[] getLoginOtp(int what) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.writeInt(23);
        mplew.writeShort(what);
        return mplew.getPacket();
    }

    public static final byte[] getLoginFailed(int reason) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.writeInt(reason);
        mplew.writeShort(0);
        return mplew.getPacket();
    }

    public static final byte[] getPermBan(byte reason) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.writeShort(2);
        mplew.writeInt(0);
        mplew.writeShort(reason);
        mplew.write(HexTool.getByteArrayFromHexString("01 01 01 01 00"));
        return mplew.getPacket();
    }

    public static final byte[] getTempBan(long timestampTill, byte reason) {
        MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter(17);
        w.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        w.write(2);
        w.write(HexTool.getByteArrayFromHexString("00 00 00 00 00"));
        w.write(reason);
        w.writeLong(timestampTill);
        return w.getPacket();
    }

    public static final byte[] deleteCharResponse(int cid, int state) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DELETE_CHAR_RESPONSE.getValue());
        mplew.writeInt(cid);
        mplew.write(state);
        if (state == 69) {
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeInt(0);
        } else if (state == 71) {
            mplew.write(0);
        }
        mplew.write(0);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static final byte[] secondPwError(byte mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendPacketOpcode.SECONDPW_ERROR.getValue());
        mplew.write(mode);
        return mplew.getPacket();
    }

    public static byte[] enableRecommended() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENABLE_RECOMMENDED.getValue());
        mplew.writeInt(47);
        return mplew.getPacket();
    }

    public static byte[] sendRecommended(int world, String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SEND_RECOMMENDED.getValue());
        mplew.write((message != null) ? 1 : 0);
        if (message != null) {
            mplew.writeInt(world);
            mplew.writeMapleAsciiString(message);
        }
        return mplew.getPacket();
    }

    public static final byte[] getServerList(int serverId, Map<Integer, Integer> channelLoad) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
        mplew.write(serverId);
        String worldName = LoginServer.getServerName();
        mplew.writeMapleAsciiString(worldName);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.write(1);
        mplew.writeMapleAsciiString(LoginServer.getEventMessage());
        int lastChannel = 1;
        Set<Integer> channels = channelLoad.keySet();
        for (int i = 30; i > 0; i--) {
            if (channels.contains(Integer.valueOf(i))) {
                lastChannel = i;
                break;
            }
        }
        mplew.write(0);
        mplew.write(lastChannel);
        for (int j = 1; j <= lastChannel; j++) {
            int load;
            if (ChannelServer.getInstance(j) != null) {
                load = Math.max(1, ChannelServer.getInstance(j).getPlayerStorage().getAllCharacters().size());
            } else {
                load = 1;
            }
            mplew.writeMapleAsciiString(worldName + ((j == 1) ? ("-" + j) : ((j == 2) ? "- 20세이상" : ("-" + (j - 1)))));
            mplew.writeInt(load);
            mplew.write(serverId);
            mplew.write(j - 1);
            mplew.write(0);
        }
        mplew.writeShort(0);
        mplew.writeInt(0);
        mplew.write(1);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static final byte[] LeavingTheWorld() {
        MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
        w.writeShort(SendPacketOpcode.LEAVING_WORLD.getValue());
        w.write(3);
        w.writeMapleAsciiString("main");
        w.write(1);
        w.writeZeroBytes(8);
        w.writeMapleAsciiString("sub");
        w.writeZeroBytes(9);
        w.writeMapleAsciiString("sub_2");
        w.writeZeroBytes(9);
        w.write(1);
        return w.getPacket();
    }

    public static final byte[] getEndOfServerList() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
        mplew.write(255);
        int advertisement = 0;
        mplew.write(advertisement);
        for (int i = 0; i < advertisement; i++) {
            mplew.writeMapleAsciiString("");
            mplew.writeMapleAsciiString("");
            mplew.writeInt(5000);
            mplew.writeInt(310);
            mplew.writeInt(60);
            mplew.writeInt(235);
            mplew.writeInt(538);
        }
        mplew.write(0);
        mplew.writeInt(-1);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static final byte[] getServerStatus(int status) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERSTATUS.getValue());
        mplew.writeShort(status);
        return mplew.getPacket();
    }

    public static final byte[] checkOTP(int status) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHECK_OTP.getValue());
        mplew.write(status);
        return mplew.getPacket();
    }

    public static final byte[] getCharList(MapleClient c, String secondpw, List<MapleCharacter> chars, int charslots, byte nameChange) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHARLIST.getValue());
        mplew.write(0);
        mplew.writeMapleAsciiString("");
        mplew.writeInt(1);
        mplew.writeInt(1);
        mplew.writeInt(1);
        mplew.writeInt(charslots);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));
        mplew.write(0);
        mplew.writeInt(chars.size());

        List<Pair<Integer, Integer>> a2 = new ArrayList<>();
        for (MapleCharacter chr : chars) {
            a2.add(new Pair<>(Integer.valueOf(chr.getId()), Integer.valueOf(chr.getOrder())));
        }
        for (int i = 0; i < a2.size() - 1; i++) {
            for (int j = 0; j < a2.size() - i - 1; j++) {
                if (((Integer) ((Pair) a2.get(j)).getRight()).intValue() > ((Integer) ((Pair) a2.get(j + 1)).getRight()).intValue()) {
                    int chridtmp = ((Integer) ((Pair) a2.get(j + 1)).getLeft()).intValue();
                    int chrpointtmp = ((Integer) ((Pair) a2.get(j + 1)).getRight()).intValue();
                    a2.set(j + 1, a2.get(j));
                    a2.set(j, new Pair<>(Integer.valueOf(chridtmp), Integer.valueOf(chrpointtmp)));
                }
            }
        }

        for (Pair<Integer, Integer> p : a2) {
            mplew.writeInt(((Integer) p.getLeft()).intValue());
        }
        mplew.write(chars.size());
        for (MapleCharacter chr : chars) {
            addCharEntry(mplew, chr, (!chr.isGM() && chr.getLevel() >= 30), false);
        }

        mplew.write(((secondpw != null && secondpw.length() > 0) || c.getSecondPw() == 1) ? 1 : ((secondpw != null && secondpw.length() <= 0) ? 2 : 0));
        mplew.write(c.getSecondPw());
        mplew.write((c.getSecondPw() == 1) ? 0 : 1);
        mplew.writeInt(charslots);
        mplew.writeInt(0);
        mplew.writeInt(-1);
        mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis()));
        mplew.write(1);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0); // 360 ++
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(1);
        return mplew.getPacket();
    }

    public static final byte[] addNewCharEntry(MapleCharacter chr, boolean worked) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ADD_NEW_CHAR_ENTRY.getValue());
        mplew.write(worked ? 0 : 1);
        mplew.writeInt(0);
        addCharEntry(mplew, chr, false, false);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static final byte[] charNameResponse(String charname, boolean nameUsed) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHAR_NAME_RESPONSE.getValue());
        mplew.writeMapleAsciiString(charname);
        mplew.write(nameUsed ? 1 : 0);
        return mplew.getPacket();
    }

    private static final void addCharEntry(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean ranking, boolean viewAll) {
        PacketHelper.addCharStats(mplew, chr);
        mplew.writeInt(107);
        mplew.writeLong(220L);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        if (GameConstants.isZero(chr.getJob())) {
            byte gender = chr.getGender(), secondGender = chr.getSecondGender();
            chr.setGender((byte) 0);
            chr.setSecondGender((byte) 1);
            AvatarLook.encodeAvatarLook(mplew, chr, true, false);
            chr.setGender((byte) 1);
            chr.setSecondGender((byte) 0);
            AvatarLook.encodeAvatarLook(mplew, chr, true, true);
            chr.setGender(gender);
            chr.setSecondGender(secondGender);
        } else {
            AvatarLook.encodeAvatarLook(mplew, chr, true, false);
        }
    }

    public static final byte[] getSecondPasswordConfirm(byte op) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.AUTH_STATUS_WITH_SPW.getValue());
        mplew.write(op);
        if (op == 0) {
            mplew.write(1);
            mplew.write(35);
            for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
                mplew.write(j.getFlag());
                mplew.writeShort(j.getFlag());
            }
        }
        return mplew.getPacket();
    }

    public static byte[] NewSendPasswordWay(MapleClient c) {
        MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
        w.writeShort(SendPacketOpcode.NEW_PASSWORD_CHECK.getValue());
        int a = (c.getSecondPassword() == null || c.getSecondPassword().equals("초기화")) ? 0 : ((c.getSecondPassword() != null) ? 1 : 0);
        w.write(a);
        w.write(0);
        return w.getPacket();
    }

    public static byte[] skipNewPasswordCheck(MapleClient c) {
        MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
        w.writeShort(SendPacketOpcode.SKIP_NEW_PASSWORD_CHECK.getValue());
        w.write(1);
        return w.getPacket();
    }

    public static final byte[] getSecondPasswordResult(boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.AUTH_STATUS_WITH_SPW_RESULT.getValue());
        mplew.write(success ? 0 : 20);
        return mplew.getPacket();
    }

    public static final byte[] MapleExit() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MAPLE_EXIT.getValue());
        return mplew.getPacket();
    }

    public static byte[] ChannelBackImg(boolean isSunday) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHANNEL_BACK_IMG.getValue());
        mplew.write(1);
        if (isSunday) {
            mplew.writeMapleAsciiString("sundayMaple");
        } else {
            mplew.writeMapleAsciiString("2022superHaste");
        }
        mplew.writeInt(1);
        mplew.writeInt(0);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] getSelectedChannelFailed(byte data, int ch) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SELECT_CHANNEL_LIST.getValue());
        mplew.write(data);
        mplew.writeShort(0);
        mplew.writeInt(ch);
        mplew.writeInt(-1);
        return mplew.getPacket();
    }

    public static byte[] getCharacterLoad() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHARACTER_LOAD.getValue());
        mplew.writeInt(10);
        mplew.writeInt(483224);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(3);
        return mplew.getPacket();
    }

    public static byte[] getSelectedChannelResult(int ch) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SELECT_CHANNEL_LIST.getValue());
        mplew.write(0);
        mplew.writeShort(0);
        mplew.writeInt(ch);
        mplew.writeInt((ch == 47) ? 1 : -1);
        return mplew.getPacket();
    }

    public static byte[] getSelectedWorldResult(int world) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SELECTED_WORLD.getValue());
        mplew.writeInt(world);
        return mplew.getPacket();
    }

    public static final byte[] getKeyGuardResponse(String Key) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LOG_OUT.getValue());
        mplew.writeMapleAsciiString(Key);
        return mplew.getPacket();
    }

    public static final byte[] OTPChange(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.OTP_CHANGE.getValue());
        mplew.write(3);
        mplew.write(type);
        return mplew.getPacket();
    }

    public static final byte[] getAuthSuccessRequest(MapleClient client) {
        MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
        w.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        w.write(0);
        w.writeMapleAsciiString(client.getAccountName());
        w.writeLong(-1L);
        w.writeInt(client.getAccID());
        w.write(client.isGm() ? 1 : 0);
        w.writeInt(client.isGm() ? 512 : 0);
        w.writeInt(10);
        w.writeInt(20);
        w.write(99);
        w.write((client.getChatBlockedTime() > 0L) ? 1 : 0);
        w.writeLong(client.getChatBlockedTime());
        w.write(1);
        w.write(0);
        w.writeMapleAsciiString("");
        w.write(true);
        w.write(35);
        for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
            w.write(j.getFlag());
            w.writeShort(j.getFlag());
        }
        w.write(0);
        w.writeInt(-1);
        return w.getPacket();
    }

    public static final byte[] getCharEndRequest(MapleClient client, String Acc, String Pwd, boolean Charlist) {
        MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
        w.writeShort(SendPacketOpcode.CHAR_END_REQUEST.getValue());
        w.write(0);
        w.writeInt(client.getAccID());
        w.write(client.isGm() ? 1 : 0);
        w.writeInt(client.isGm() ? 32 : 0);
        w.writeInt(10);
        w.writeInt(20);
        w.write(99);
        w.write((client.getChatBlockedTime() > 0L) ? 1 : 0);
        w.writeLong(client.getChatBlockedTime());
        w.writeMapleAsciiString(Pwd);
        w.writeMapleAsciiString(Acc);
        w.writeMapleAsciiString("");
        w.write(true);
        w.write(35);
        for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
            w.write(j.getFlag());
            w.writeShort(j.getFlag());
        }
        w.write(0);
        w.writeInt(-1);
        w.write(Charlist);
        w.write(0);
        return w.getPacket();
    }
}
