package client;

import client.inventory.Equip;
import client.inventory.MapleInventoryType;
import client.inventory.MapleWeaponType;
import constants.GameConstants;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import server.Randomizer;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;

public class AvatarLook {

    private final Map<Byte, Integer> items = new HashMap<>();

    private final Map<Byte, Integer> codyItems = new HashMap<>();

    private final int[] pets = new int[3];

    private int gender, skin, face, hair, job, mega, weaponStickerID, weaponID, subWeaponID, drawElfEar, pinkbeanChangeColor,
            demonSlayerDefFaceAcc, xenonDefFaceAcc, arkDefFaceAcc, hoyeongDefFaceAcc, isZeroBetaLook, addColor, baseProb, hoyeongAvater;

    public static AvatarLook makeRandomAvatar() {
        int[] hair;
        AvatarLook a = new AvatarLook();
        int[] skin = {15, 16, 18, 19};
        int[] face = {20061, 25017, 21045, 25099, 25050};
        a.gender = Randomizer.nextInt(2);
        if (a.gender == 1) {
            hair = new int[]{43980, 40670, 38620, 48370, 41750};
        } else {
            hair = new int[]{43330, 35660, 46340, 33150, 43320};
        }
        a.skin = skin[Randomizer.nextInt(skin.length)];
        a.face = face[Randomizer.nextInt(face.length)] + Randomizer.nextInt(8) * 100;
        a.job = 222;
        a.mega = 0;
        a.hair = hair[Randomizer.nextInt(hair.length)] + Randomizer.nextInt(8);
        int[][] items = {{1002186, 1003250, 1000599}, {1012104, 1012379}, {1022079, 1022285}, {1032024}, {1052975, 1053257, 1053351}, {1062183}, {1072153}, {1082102}, {1103068}};
        for (int i = 0; i < items.length; i++) {
            a.items.put(Byte.valueOf((byte) (i + 1)), Integer.valueOf(items[i][Randomizer.nextInt((items[i]).length)]));
        }
        int[] weapons = {1702174, 1702549, 1702945};
        a.weaponStickerID = weapons[Randomizer.nextInt(weapons.length)];
        a.weaponID = Randomizer.nextBoolean() ? 1442000 : 1302000;
        a.subWeaponID = 1092056;
        a.drawElfEar = 0;
        a.pinkbeanChangeColor = 0;
        for (int j = 0; j < 3; j++) {
            a.pets[j] = 0;
        }
        return a;
    }

    public void save(int position, PreparedStatement ps) throws SQLException {
        ps.setInt(2, position);
        ps.setInt(3, this.gender);
        ps.setInt(4, this.skin);
        ps.setInt(5, this.face);
        ps.setInt(6, this.hair);
        for (int i = 1; i <= 9; i++) {
            if (this.items.containsKey(Byte.valueOf((byte) i))) {
                ps.setInt(6 + i, ((Integer) this.items.get(Byte.valueOf((byte) i))).intValue());
            } else {
                ps.setInt(6 + i, -1);
            }
        }
        ps.setInt(16, this.weaponStickerID);
        ps.setInt(17, this.weaponID);
        ps.setInt(18, this.subWeaponID);
    }

    public static AvatarLook init(ResultSet rs) throws SQLException {
        AvatarLook a = new AvatarLook();
        a.gender = rs.getInt("gender");
        a.skin = rs.getInt("skin");
        a.face = rs.getInt("face");
        a.hair = rs.getInt("hair");
        a.addColor = rs.getInt("addColor");
        a.baseProb = rs.getInt("baseProb");
        for (int i = 1; i <= 9; i++) {
            a.items.put(Byte.valueOf((byte) i), Integer.valueOf(rs.getInt("equip" + i)));
        }
        a.weaponStickerID = rs.getInt("weaponstickerid");
        a.weaponID = rs.getInt("weaponid");
        a.subWeaponID = rs.getInt("subweaponid");
        return a;
    }

    public int getHairEquip(int pos) {
        if (this.codyItems.containsKey(Byte.valueOf((byte) pos))) {
            return ((Integer) this.codyItems.get(Byte.valueOf((byte) pos))).intValue();
        }
        if (this.items.containsKey(Byte.valueOf((byte) pos))) {
            return ((Integer) this.items.get(Byte.valueOf((byte) pos))).intValue();
        }
        return -1;
    }

    public boolean compare(AvatarLook a) {
        if (a.gender != this.gender || a.skin != this.skin || a.face != this.face || a.hair != this.hair || a.subWeaponID != this.subWeaponID || a.weaponStickerID != this.weaponStickerID) {
            return false;
        }
        for (int i = 1; i <= 9; i++) {
            if (a.getHairEquip(i) != getHairEquip(i)) {
                return false;
            }
        }
        return true;
    }

    public static void decodeUnpackAvatarLook(AvatarLook a, LittleEndianAccessor slea) throws IOException {
        a.gender = slea.readBit(1);
        a.skin = slea.readBit(10);
        boolean is5thFace = (slea.readBit(1) == 1);
        int face1 = slea.readBit(10), face2 = slea.readBit(4);
        if (face1 == 1023) {
            a.face = 0;
        } else {
            a.face = face1 + 1000 * (face2 + (is5thFace ? 50 : 20));
        }
        boolean is4thHair = (slea.readBit(1) == 1);
        int hair1 = slea.readBit(10), hair2 = slea.readBit(4);
        if (hair1 == 1023) {
            a.hair = 0;
        } else {
            a.hair = hair1 + 1000 * (hair2 + 30) + (is4thHair ? 10000 : 0);
        }
        int equip1_1 = slea.readBit(10), equip1_2 = slea.readBit(3);
        if (equip1_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 1), Integer.valueOf(equip1_1 + 1000 * (equip1_2 + 1000)));
        }
        int equip2_1 = slea.readBit(10), equip2_2 = slea.readBit(2);
        if (equip2_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 2), Integer.valueOf(equip2_1 + 1000 * (equip2_2 + 1010)));
        }
        int equip3_1 = slea.readBit(10), equip3_2 = slea.readBit(2);
        if (equip3_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 3), Integer.valueOf(equip3_1 + 1000 * (equip3_2 + 1020)));
        }
        int equip4_1 = slea.readBit(10), equip4_2 = slea.readBit(2);
        if (equip4_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 4), Integer.valueOf(equip4_1 + 1000 * (equip4_2 + 1030)));
        }
        boolean equip5_check = (slea.readBit(1) == 1);
        int equip5_1 = slea.readBit(10), equip5_2 = slea.readBit(4);
        if (equip5_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 5), Integer.valueOf(equip5_1 + 1000 * (equip5_2 + 10 * (equip5_check ? 105 : 104))));
        }
        int equip6_1 = slea.readBit(10), equip6_2 = slea.readBit(2);
        if (equip6_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 6), Integer.valueOf(equip6_1 + 1000 * (equip6_2 + 1060)));
        }
        int equip7_1 = slea.readBit(10), equip7_2 = slea.readBit(2);
        if (equip7_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 7), Integer.valueOf(equip7_1 + 1000 * (equip7_2 + 1070)));
        }
        int equip8_1 = slea.readBit(10), equip8_2 = slea.readBit(2);
        if (equip8_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 8), Integer.valueOf(equip8_1 + 1000 * (equip8_2 + 1080)));
        }
        int equip9_1 = slea.readBit(10), equip9_2 = slea.readBit(2);
        if (equip9_1 != 1023) {
            a.items.put(Byte.valueOf((byte) 9), Integer.valueOf(equip9_1 + 1000 * (equip9_2 + 1100)));
        }
        int equip10_check = slea.readBit(2);
        int equip10_1 = slea.readBit(10), equip10_2 = slea.readBit(4);
        int v53 = (equip10_check == 3) ? 135 : ((equip10_check == 2) ? 134 : ((equip10_check == 1) ? 109 : 0));
        if (equip10_1 != 1023) {
            a.subWeaponID = equip10_1 + 1000 * (equip10_2 + 2 * 5 * v53);
        }
        boolean weaponStickerCheck = (slea.readBit(1) == 1);
        int equip11_1 = slea.readBit(10), equip11_2 = slea.readBit(2);
        if (weaponStickerCheck) {
            a.weaponStickerID = 1000 * (equip11_2 + 1700) + equip11_1;
        }
    }

    public void encodeUnpackAvatarLook(MaplePacketLittleEndianWriter mplew) {
        mplew.writeBit(this.gender, 1);
        mplew.writeBit(this.skin, 10);
        if (this.face >= 100000) {
            this.face /= 1000;
        }
        mplew.writeBit((this.face / 10000 == 5) ? 1 : 0, 1);
        mplew.writeBit(this.face % 1000, 10);
        mplew.writeBit(this.face / 1000 % 10, 4);
        mplew.writeBit(this.hair / 10000, 4);
        mplew.writeBit(this.hair % 1000, 10);
        mplew.writeBit(this.hair / 1000 % 10, 4);
        mplew.writeBit(getHairEquip(1) % 1000, 10);
        mplew.writeBit(getHairEquip(1) / 1000 % 10, 3);
        mplew.writeBit(getHairEquip(2) % 1000, 10);
        mplew.writeBit(getHairEquip(2) / 1000 % 10, 2);
        mplew.writeBit(getHairEquip(3) % 1000, 10);
        mplew.writeBit(getHairEquip(3) / 1000 % 10, 2);
        mplew.writeBit(getHairEquip(4) % 1000, 10);
        mplew.writeBit(getHairEquip(4) / 1000 % 10, 2);
        mplew.writeBit((Integer.toUnsignedLong(getHairEquip(5) - 1050000) < 10000L) ? 1 : 0, 1);
        mplew.writeBit(getHairEquip(5) % 1000, 10);
        mplew.writeBit(getHairEquip(5) / 1000 % 10, 4);
        mplew.writeBit(getHairEquip(6) % 1000, 10);
        mplew.writeBit(getHairEquip(6) / 1000 % 10, 2);
        mplew.writeBit(getHairEquip(7) % 1000, 10);
        mplew.writeBit(getHairEquip(7) / 1000 % 10, 2);
        mplew.writeBit(getHairEquip(8) % 1000, 10);
        mplew.writeBit(getHairEquip(8) / 1000 % 10, 2);
        mplew.writeBit(getHairEquip(9) % 1000, 10);
        mplew.writeBit(getHairEquip(9) / 1000 % 10, 2);
        int v39 = 0;
        if (this.subWeaponID > 0) {
            if (this.subWeaponID / 10000 == 109) {
                v39 = 1;
            } else {
                v39 = (Integer.toUnsignedLong(this.subWeaponID - 1340000) < 10000L) ? 2 : 3;
            }
        }
        mplew.writeBit(v39, 2);
        mplew.writeBit(this.subWeaponID % 1000, 10);
        mplew.writeBit(this.subWeaponID / 1000 % 10, 4);
        mplew.writeBit((this.weaponStickerID > 0) ? 1 : 0, 1);
        mplew.writeBit(this.weaponStickerID % 1000, 10);
        mplew.writeBit(this.weaponStickerID / 1000 % 10, 2);
        mplew.writeBit(this.weaponID % 1000, 10);
        mplew.writeBit(this.weaponID / 1000 % 10, 2);
        int weaponType = this.weaponID / 10000 % 100;
        if (GameConstants.getWeaponType(this.weaponID) == MapleWeaponType.TUNER) {
            weaponType = 213;
        }
        Integer[] wt = {
            Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43),
            Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(39), Integer.valueOf(34), Integer.valueOf(52), Integer.valueOf(53),
            Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(56), Integer.valueOf(57), Integer.valueOf(26), Integer.valueOf(58),
            Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(59), Integer.valueOf(29), Integer.valueOf(213), Integer.valueOf(214), Integer.valueOf(0)};
        int index = Arrays.<Integer>asList(wt).indexOf(Integer.valueOf(weaponType));
        mplew.writeBit((index == -1) ? wt.length : (index + 1), 8);
        mplew.writeBit(this.drawElfEar & 0x1, 4);
        mplew.writeBit(this.addColor, 4);
        mplew.writeBit(this.baseProb, 8);
        mplew.writeBit(this.hoyeongDefFaceAcc, 8);
        mplew.writeBit((GameConstants.isPinkBean(this.job) || GameConstants.isYeti(this.job)) ? 1 : 0, 1);
        mplew.writeBit(0, 18);
        mplew.writeZeroBytes(88);
        mplew.write(21);
    }

    public void encodeAvatarLook(MaplePacketLittleEndianWriter mplew) {
        mplew.write(this.gender);
        mplew.write(this.skin);
        mplew.writeInt(this.face);
        mplew.writeInt(this.job);
        mplew.write(this.mega);
        mplew.writeInt(this.hair);
        for (Map.Entry<Byte, Integer> entry : this.items.entrySet()) {
            mplew.write(((Byte) entry.getKey()).byteValue());
            mplew.writeInt(((Integer) entry.getValue()).intValue());
        }
        mplew.write(-1);
        for (Map.Entry<Byte, Integer> entry : this.codyItems.entrySet()) {
            mplew.write(((Byte) entry.getKey()).byteValue());
            mplew.writeInt(((Integer) entry.getValue()).intValue());
        }
        mplew.write(-1);
        mplew.writeInt(this.weaponStickerID);
        mplew.writeInt(this.weaponID);
        mplew.writeInt(this.subWeaponID);
        mplew.writeInt(this.drawElfEar);
        mplew.writeInt(this.pinkbeanChangeColor);
        mplew.write(this.hoyeongAvater);
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(this.pets[i]);
        }
        if (GameConstants.isDemonSlayer(this.job) || GameConstants.isDemonAvenger(this.job)) {
            mplew.writeInt(this.demonSlayerDefFaceAcc);
        } else if (GameConstants.isXenon(this.job)) {
            mplew.writeInt(this.xenonDefFaceAcc);
        } else if (GameConstants.isArk(this.job)) {
            mplew.writeInt(this.arkDefFaceAcc);
        } else if (GameConstants.isHoyeong(this.job)) {
            mplew.writeInt(this.hoyeongDefFaceAcc);
        } else if (GameConstants.isZero(this.job)) {
            mplew.write(this.isZeroBetaLook);
        }
        mplew.write(this.addColor);
        mplew.write(this.baseProb);
        mplew.writeInt(153525);
    }

    public static final void encodeAvatarLook(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr, final boolean mega, boolean second) {
        boolean isAlpha = GameConstants.isZero(chr.getJob()) && chr.getGender() == 0 && chr.getSecondGender() == 1;
        boolean isBeta = GameConstants.isZero(chr.getJob()) && chr.getGender() == 1 && chr.getSecondGender() == 0;
        mplew.write(second || isBeta ? chr.getSecondGender() : chr.getGender());
        mplew.write(second || isBeta ? chr.getSecondSkinColor() : chr.getSkinColor());
        mplew.writeInt(second || isBeta ? chr.getSecondFace() : chr.getFace());
        mplew.writeInt(chr.getJob());
        mplew.write(mega ? 0 : 1);
        if (second || isBeta) {
            int hair = chr.getSecondHair();
            if (chr.getSecondBaseColor() != -1) {
                hair = chr.getSecondHair() / 10 * 10 + chr.getSecondBaseColor();
            }
            mplew.writeInt(hair);
        } else {
            int hair = chr.getHair();
            if (chr.getBaseColor() != -1) {
                hair = chr.getHair() / 10 * 10 + chr.getBaseColor();
            }
            mplew.writeInt(hair);
        }
        final Map<Byte, Integer> myEquip = new LinkedHashMap<>();
        final Map<Byte, Integer> maskedEquip = new LinkedHashMap<>();
        final Map<Byte, Integer> totemEquip = new LinkedHashMap<>();
        final Map<Short, Integer> equip = second ? chr.getSecondEquips() : chr.getEquips();
        for (final Map.Entry<Short, Integer> item : equip.entrySet()) {
            if (item.getKey() < -2000) {
                continue;
            }
            short pos = (short) (item.getKey() * -1);
            Equip item_ = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -pos);
            if (item_ == null) {
                continue;
            }
            if (GameConstants.isAngelicBuster(chr.getJob()) && second) {
                if ((pos >= 1300) && (pos < 1400)) {
                    pos = (short) (pos - 1300);
                    switch (pos) {
                        case 0:
                            pos = 1;
                            break;
                        case 1:
                            pos = 9;
                            break;
                        case 4:
                            pos = 8;
                            break;
                        case 5:
                            pos = 3;
                            break;
                        case 6:
                            pos = 4;
                            break;
                        case 7:
                            pos = 5;
                            break;
                        case 8:
                            pos = 6;
                            break;
                        case 9:
                            pos = 7;
                            break;
                    }
                    if (myEquip.get((byte) pos) != null) {
                        maskedEquip.put((byte) pos, myEquip.get((byte) pos));
                    }
                    String lol = ((Integer) item.getValue()).toString();
                    String ss = lol.substring(0, 3);
                    int moru = Integer.parseInt(ss + ((Integer) item_.getMoru()).toString());
                    myEquip.put((byte) pos, item_.getMoru() != 0 ? moru : item.getValue());
                } else if (((pos > 100) && (pos < 200)) && (pos != 111)) {
                    pos = (short) (pos - 100);
                    switch (pos) {
                        case 10:
                        case 12:
                        case 13:
                        case 15:
                        case 16:
                            if (myEquip.get((byte) pos) != null) {
                                maskedEquip.put((byte) pos, myEquip.get((byte) pos));
                            }
                            String lol = ((Integer) item.getValue()).toString();
                            String ss = lol.substring(0, 3);
                            int moru = Integer.parseInt(ss + ((Integer) item_.getMoru()).toString());
                            myEquip.put((byte) pos, item_.getMoru() != 0 ? moru : item.getValue());
                            break;
                    }
                }
                if ((pos < 100)) {
                    if (myEquip.get((byte) pos) == null) {
                        String lol = ((Integer) item.getValue()).toString();
                        String ss = lol.substring(0, 3);
                        int moru = Integer.parseInt(ss + ((Integer) item_.getMoru()).toString());
                        myEquip.put((byte) pos, item_.getMoru() != 0 ? moru : item.getValue());
                    } else {
                        maskedEquip.put((byte) pos, item.getValue());
                    }
                }
            } else if (isBeta) {
                //제로이면서 베타일 때
                if ((pos < 100) && (myEquip.get((byte) pos) == null)) {
                    String lol = ((Integer) item.getValue()).toString();
                    String ss = lol.substring(0, 3);
                    int moru = Integer.parseInt(ss + ((Integer) item_.getMoru()).toString());
                    myEquip.put((byte) pos, item_.getMoru() != 0 ? moru : item.getValue());
                } else if (pos > 1500 && pos != 1511) {
                    if (pos > 1500) {
                        pos = (short) (pos - 1500);
                    }
                    myEquip.put((byte) pos, item.getValue());
                }

            } else if (isAlpha || (GameConstants.isAngelicBuster(chr.getJob()) && !second) || (!GameConstants.isZero(chr.getJob()) && !GameConstants.isAngelicBuster(chr.getJob()))) {
                //엔버 드레스업이 아니거나, 제로 알파이거나, 나머지 직업일 때
                if ((pos < 100) && (myEquip.get((byte) pos) == null)) {
                    String lol = ((Integer) item.getValue()).toString();
                    String ss = lol.substring(0, 3);
                    int moru = Integer.parseInt(ss + ((Integer) item_.getMoru()).toString());
                    myEquip.put((byte) pos, item_.getMoru() != 0 ? moru : item.getValue());
                    //myEquip.put((byte) pos, item.getValue());
                } else if ((pos > 100) && (pos != 111)) {

                    pos -= 100;
                    if (myEquip.get((byte) pos) != null) {
                        maskedEquip.put((byte) pos, myEquip.get((byte) pos));
                    }
                    String lol = ((Integer) item.getValue()).toString();
                    String ss = lol.substring(0, 3);
                    int moru = Integer.parseInt(ss + ((Integer) item_.getMoru()).toString());
                    myEquip.put((byte) pos, item_.getMoru() != 0 ? moru : item.getValue());

                    /*pos = (byte) (pos - 100);
                     if (myEquip.get(pos) != null) {
                     maskedEquip.put((byte) pos, myEquip.get(pos));
                     }
                     myEquip.put((byte) pos, item.getValue());*/
                } else if (myEquip.get((byte) pos) != null) {
                    maskedEquip.put((byte) pos, item.getValue());
                }
            }
        }
        for (final Map.Entry<Byte, Integer> totem : chr.getTotems().entrySet()) {
            byte pos = (byte) ((totem.getKey()).byteValue() * -1);
            if (pos < 0 || pos > 2) { //3 totem slots
                continue;
            }
            if (totem.getValue() < 1200000 || totem.getValue() >= 1210000) {
                continue;
            }
            totemEquip.put(Byte.valueOf(pos), totem.getValue());
        }

        for (Map.Entry<Byte, Integer> entry : myEquip.entrySet()) {
            int weapon = ((Integer) entry.getValue()).intValue();

            if (isAlpha && (GameConstants.getWeaponType(weapon) == MapleWeaponType.BIG_SWORD)) {
                continue;
            } else if (isBeta && (GameConstants.getWeaponType(weapon) == MapleWeaponType.LONG_SWORD)) {
                continue;
            } else if (isBeta && (GameConstants.getWeaponType(weapon) == MapleWeaponType.BIG_SWORD)) {
                mplew.write(11);
                mplew.writeInt(((Integer) entry.getValue()).intValue());
            } else {
                mplew.write(((Byte) entry.getKey()).byteValue());
                mplew.writeInt(((Integer) entry.getValue()).intValue());
            }
        }
        mplew.write(-1);

        for (Map.Entry<Byte, Integer> entry : maskedEquip.entrySet()) {
            mplew.write(((Byte) entry.getKey()).byteValue());
            mplew.writeInt(((Integer) entry.getValue()).intValue());
        }
        mplew.write(-1);

        if (isBeta) {
            Integer cWeapon = equip.get((short) -1511);
            mplew.writeInt(cWeapon != null ? cWeapon.intValue() : 0);
            Integer Weapon = equip.get((short) -11);
            mplew.writeInt(Weapon != null ? Weapon.intValue() : 0);
            mplew.writeInt(0);
        } else {
            Integer cWeapon = equip.get((short) -111);
            mplew.writeInt(cWeapon != null ? cWeapon.intValue() : 0);
            Integer Weapon = equip.get((short) -11);
            mplew.writeInt(Weapon != null ? Weapon.intValue() : 0);
            Integer Shield = equip.get((short) -10);
            if (GameConstants.isZero(chr.getJob()) || Shield == null) {
                mplew.writeInt(0);
            } else {
                mplew.writeInt(Shield.intValue());
            }
        }

        mplew.writeInt(0);//엘프귀
        mplew.writeInt(chr.getKeyValue(100229, "hue")); //324++ 핑크빈 색 바꾸기
        mplew.write(second ? chr.getSecondBaseColor() : chr.getBaseColor());//
        mplew.write(0); //361 new
        
        for (int i = 0; i < 3; i++) {
            if (chr.getPet(i) != null) {
                mplew.writeInt(chr.getPet(i).getPetItemId());
            } else {
                mplew.writeInt(0);
            }
        }

        if (GameConstants.isDemonSlayer(chr.getJob()) || GameConstants.isXenon(chr.getJob()) || GameConstants.isDemonAvenger(chr.getJob())
                || GameConstants.isArk(chr.getJob())) {
            mplew.writeInt(chr.getDemonMarking());
        } else if (GameConstants.isHoyeong(chr.getJob())) {
            mplew.writeInt(chr.getDemonMarking());
        } else if (GameConstants.isZero(chr.getJob())) {
            mplew.write(second || isBeta ? chr.getSecondGender() : chr.getGender());
        }

        mplew.write(second || isBeta ? chr.getSecondAddColor() : chr.getAddColor());
        mplew.write(second || isBeta ? chr.getSecondBaseProb() : chr.getBaseProb());
        mplew.writeInt(0);
    }

    public static void decodeAvatarLook(AvatarLook a, LittleEndianAccessor slea) {
        a.gender = slea.readByte();
        a.skin = slea.readByte();
        a.face = slea.readInt();
        a.job = slea.readInt();
        a.mega = slea.readByte();
        a.hair = slea.readInt();
        byte pos = slea.readByte();
        while (pos != -1) {
            int itemId = slea.readInt();
            a.items.put(Byte.valueOf(pos), Integer.valueOf(itemId));
            pos = slea.readByte();
        }
        byte pos2 = slea.readByte();
        while (pos2 != -1) {
            int itemId = slea.readInt();
            a.codyItems.put(Byte.valueOf(pos2), Integer.valueOf(itemId));
            pos2 = slea.readByte();
        }
        a.weaponStickerID = slea.readInt();
        a.weaponID = slea.readInt();
        a.subWeaponID = slea.readInt();
        a.drawElfEar = slea.readInt();
        a.pinkbeanChangeColor = slea.readInt();
        slea.readByte();
        for (int i = 0; i < 3; i++) {
            a.pets[i] = slea.readInt();
        }
        a.addColor = slea.readByte();
        a.baseProb = slea.readByte();
    }
}
