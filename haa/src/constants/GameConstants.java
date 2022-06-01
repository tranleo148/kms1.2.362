package constants;

import client.MapleCharacter;
import client.MapleClient;
import client.MatrixSkill;
import client.PlayerStats;
import client.SecondaryStat;
import client.Skill;
import client.SkillFactory;
import client.custom.inventory.CustomItem;
import client.inventory.Equip;
import client.inventory.MapleInventoryType;
import client.inventory.MapleWeaponType;
import client.status.MonsterStatus;
import handling.channel.handler.AttackInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import server.DailyGiftItemInfo;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.events.MapleBattleGroundCharacter;
import server.life.MapleLifeFactory;
import server.maps.MapleMap;
import server.maps.MapleMapObjectType;
import tools.FileoutputUtil;
import tools.Pair;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class GameConstants {

    public static final List<MapleMapObjectType> rangedMapobjectTypes = Collections.unmodifiableList(Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.ITEM, MapleMapObjectType.MONSTER, MapleMapObjectType.DOOR, MapleMapObjectType.REACTOR, MapleMapObjectType.SUMMON, MapleMapObjectType.NPC, MapleMapObjectType.MIST, MapleMapObjectType.EXTRACTOR}));

    public static final long[] exp = new long[1000];

    public static final long[] exp2 = new long[20];

    public static List<CustomItem> customItems = new ArrayList<>();
    
    private static final int[] closeness = new int[]{
        0, 1, 3, 6, 14, 31, 60, 108, 181, 287,
        434, 632, 891, 1224, 1642, 2161, 2793, 3557, 4467, 5542,
        6801, 8263, 9950, 11882, 14084, 16578, 19391, 22547, 26074, 30000};

    private static final int[] setScore = new int[]{0, 10, 100, 300, 600, 1000, 2000, 4000, 7000, 10000};

    private static final int[] cumulativeTraitExp = new int[]{
        0, 20, 46, 80, 124, 181, 255, 351, 476, 639,
        851, 1084, 1340, 1622, 1932, 2273, 2648, 3061, 3515, 4014,
        4563, 5128, 5710, 6309, 6926, 7562, 8217, 8892, 9587, 10303,
        11040, 11788, 12547, 13307, 14089, 14883, 15689, 16507, 17337, 18179,
        19034, 19902, 20783, 21677, 22584, 23505, 24440, 25399, 26362, 27339,
        28331, 29338, 30360, 31397, 32450, 33519, 34604, 35705, 36823, 37958,
        39110, 40279, 41466, 32671, 43894, 45135, 46395, 47674, 48972, 50289,
        51626, 52967, 54312, 55661, 57014, 58371, 59732, 61097, 62466, 63839,
        65216, 66597, 67982, 69371, 70764, 72161, 73562, 74967, 76376, 77789,
        79206, 80627, 82052, 83481, 84914, 86351, 87792, 89237, 90686, 92139,
        93596, 96000};

    private static final int[] mobHpVal = new int[]{
        0, 15, 20, 25, 35, 50, 65, 80, 95, 110,
        125, 150, 175, 200, 225, 250, 275, 300, 325, 350,
        375, 405, 435, 465, 495, 525, 580, 650, 720, 790,
        900, 990, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800,
        1900, 2000, 2100, 2200, 2300, 2400, 2520, 2640, 2760, 2880,
        3000, 3200, 3400, 3600, 3800, 4000, 4300, 4600, 4900, 5200,
        5500, 5900, 6300, 6700, 7100, 7500, 8000, 8500, 9000, 9500,
        10000, 11000, 12000, 13000, 14000, 15000, 17000, 19000, 21000, 23000,
        25000, 27000, 29000, 31000, 33000, 35000, 37000, 39000, 41000, 43000,
        45000, 47000, 49000, 51000, 53000, 55000, 57000, 59000, 61000, 63000,
        65000, 67000, 69000, 71000, 73000, 75000, 77000, 79000, 81000, 83000,
        85000, 89000, 91000, 93000, 95000, 97000, 99000, 101000, 103000, 105000,
        107000, 109000, 111000, 113000, 115000, 118000, 120000, 125000, 130000, 135000,
        140000, 145000, 150000, 155000, 160000, 165000, 170000, 175000, 180000, 185000,
        190000, 195000, 200000, 205000, 210000, 215000, 220000, 225000, 230000, 235000,
        240000, 250000, 260000, 270000, 280000, 290000, 300000, 310000, 320000, 330000,
        340000, 350000, 360000, 370000, 380000, 390000, 400000, 410000, 420000, 430000,
        440000, 450000, 460000, 470000, 480000, 490000, 500000, 510000, 520000, 530000,
        550000, 570000, 590000, 610000, 630000, 650000, 670000, 690000, 710000, 730000,
        750000, 770000, 790000, 810000, 830000, 850000, 870000, 890000, 910000};

    private static final int[] pvpExp = new int[]{0, 3000, 6000, 12000, 24000, 48000, 960000, 192000, 384000, 768000};

    private static final int[] guildexp = new int[]{
        0, 15000, 60000, 135000, 240000, 375000, 540000, 735000, 960000, 1215000,
        1500000, 1815000, 2160000, 2535000, 2940000, 3375000, 3840000, 4335000, 4860000, 5415000,
        6000000, 6615000, 7260000, 7935000, 8640000, 12528000, 18165600, 26340120, 38193170, 68747700};

    private static final int[] mountexp = new int[]{
        0, 6, 25, 50, 105, 134, 196, 254, 263, 315,
        367, 430, 543, 587, 679, 725, 897, 1146, 1394, 1701,
        2247, 2543, 2898, 3156, 3313, 3584, 3923, 4150, 4305, 4550};

    public static final int[] itemBlock = new int[0];

    public static final Integer[] cashBlock = new Integer[0];

    public static final int JAIL = 180000002;

    public static final int MAX_BUFFSTAT = 31;

    public static final int MAX_MOB_BUFFSTAT = 4;

    public static final int AUTOJOB = 111113;

    public static final int[] blockedSkills = new int[]{4341003};

    public static final String[] RESERVED = new String[]{"Rental", "Donor", "MapleNews"};

    public static final List<Pair<String, String>> dList = new ArrayList<>();

    public static final String[] stats = new String[]{
        "tuc", "reqLevel", "reqJob", "reqSTR", "reqDEX", "reqINT", "reqLUK", "reqPOP", "cash", "cursed",
        "success", "setItemID", "equipTradeBlock", "durability", "randOption", "randStat", "masterLevel", "reqSkillLevel", "elemDefault", "incRMAS",
        "incRMAF", "incRMAI", "incRMAL", "canLevel", "skill", "charmEXP", "bdR", "imdR", "onlyEquip", "jokerToSetItem",
        "android", "attackSpeed"};

    public static final int[] hyperTele = new int[]{
        310000000, 220000000, 100000000, 250000000, 240000000, 104000000, 103000000, 102000000, 101000000, 120000000,
        260000000, 200000000, 230000000};

    public static final int[] rankC = new int[]{
        70000000, 70000001, 70000002, 70000003, 70000004, 70000005, 70000006, 70000007, 70000008, 70000009,
        70000010, 70000011, 70000012, 70000013};

    public static final int[] rankB = new int[]{
        70000014, 70000015, 70000016, 70000017, 70000018, 70000021, 70000022, 70000023, 70000024, 70000025,
        70000026};

    public static final int[] rankA = new int[]{
        70000027, 70000028, 70000029, 70000030, 70000031, 70000032, 70000033, 70000034, 70000035, 70000036,
        70000039, 70000040, 70000041, 70000042};

    public static final int[] rankS = new int[]{
        70000043, 70000044, 70000045, 70000047, 70000048, 70000049, 70000050, 70000051, 70000052, 70000053,
        70000054, 70000055, 70000056, 70000057, 70000058, 70000059, 70000060, 70000061, 70000062};

    public static final int[] showEffectDropItems = new int[]{
        1022231, 1012478, 1132296, 1113149, 1032241, 1122000, 1122076, 1022232, 1132272, 1162025,
        1122254, 1122150, 1162009, 1032136, 1152170, 1182087, 1113282, 1022277, 1672076, 1012632,
        1022278, 1132308, 1162080, 1162082, 1162081, 1162083, 1122430, 1182285};

    public static final int[][] StarInfo = new int[][]{
        {20, 30, 50}, {20, 30, 50}, {20, 30, 50}, {10, 30, 50}, {20, 60, 100}, {15, 45, 75}, {30, 90, 180}, {15, 45, 75}, {20, 60, 100}, {35, 105, 175},
        {30, 90, 150}, {20, 60, 100}, {25, 75, 125}, {15, 45, 75}, {15, 45, 75}, {20, 60, 100}, {20, 60, 100}, {120, 200, 300}, {40, 120, 200}, {50, 150, 250},
        {30, 90, 150}, {30, 90, 150}, {30, 90, 150}, {30, 90, 150}, {40, 120, 200}, {40, 120, 200}, {60, 180, 300}, {60, 180, 300}, {35, 105, 175}, {30, 90, 150},
        {30, 90, 150}, {15, 45, 75}, {60, 180, 300}, {15, 45, 75}, {60, 180, 300}, {40, 120, 200}, {40, 120, 200}, {45, 135, 225}, {35, 105, 175}, {540, 570, 600}};

    public static List<Integer> questReader = new ArrayList<>();

    public static final Map<Integer, Integer> price = new HashMap<>();

    public static final List<DailyGiftItemInfo> dailyItems = new ArrayList<>();

    public static final List<Triple<Integer, Integer, Integer>> chariotItems = new ArrayList<>();

    static {
        chariotItems.add(new Triple<>(Integer.valueOf(9), Integer.valueOf(5062503), Integer.valueOf(300)));
        chariotItems.add(new Triple<>(Integer.valueOf(18), Integer.valueOf(2430007), Integer.valueOf(2)));
        chariotItems.add(new Triple<>(Integer.valueOf(27), Integer.valueOf(4310016), Integer.valueOf(10)));
        chariotItems.add(new Triple<>(Integer.valueOf(36), Integer.valueOf(2048717), Integer.valueOf(100)));
        chariotItems.add(new Triple<>(Integer.valueOf(45), Integer.valueOf(2048753), Integer.valueOf(50)));
        chariotItems.add(new Triple<>(Integer.valueOf(54), Integer.valueOf(5069001), Integer.valueOf(10)));
        chariotItems.add(new Triple<>(Integer.valueOf(63), Integer.valueOf(5068301), Integer.valueOf(2)));
        chariotItems.add(new Triple<>(Integer.valueOf(72), Integer.valueOf(5069100), Integer.valueOf(2)));
        chariotItems.add(new Triple<>(Integer.valueOf(81), Integer.valueOf(2049360), Integer.valueOf(3)));
        chariotItems.add(new Triple<>(Integer.valueOf(90), Integer.valueOf(2049136), Integer.valueOf(7)));
        chariotItems.add(new Triple<>(Integer.valueOf(99), Integer.valueOf(5060048), Integer.valueOf(5)));
        chariotItems.add(new Triple<>(Integer.valueOf(108), Integer.valueOf(2049376), Integer.valueOf(1)));
        chariotItems.add(new Triple<>(Integer.valueOf(117), Integer.valueOf(5062005), Integer.valueOf(15)));
        chariotItems.add(new Triple<>(Integer.valueOf(126), Integer.valueOf(5062006), Integer.valueOf(10)));
        GameConstants.dList.add(new Pair("mirrorD_322_0_", ""));
        GameConstants.dList.add(new Pair("mirrorD_322_1_", ""));
        GameConstants.dList.add(new Pair("mirrorD_322_2_", ""));
        GameConstants.dList.add(new Pair("mirrorD_322_3_", ""));
        GameConstants.dList.add(new Pair("mirrorD_323_0_", ""));
        /*   169 */ dailyItems.add(new DailyGiftItemInfo(1, 4310017, 10, 0));
        /*   170 */ dailyItems.add(new DailyGiftItemInfo(2, 4310017, 10, 0));
        /*   171 */ dailyItems.add(new DailyGiftItemInfo(3, 4310017, 10, 0));
        /*   172 */ dailyItems.add(new DailyGiftItemInfo(4, 4310017, 10, 0));
        /*   173 */ dailyItems.add(new DailyGiftItemInfo(5, 4310017, 10, 0));
        /*   174 */ dailyItems.add(new DailyGiftItemInfo(6, 4310017, 10, 0));
        /*   175 */ dailyItems.add(new DailyGiftItemInfo(7, 4310017, 20, 0));

        /*   177 */ dailyItems.add(new DailyGiftItemInfo(8, 4310017, 10, 0));
        /*   178 */ dailyItems.add(new DailyGiftItemInfo(9, 4310017, 10, 0));
        /*   179 */ dailyItems.add(new DailyGiftItemInfo(10, 4310017, 10, 0));
        /*   180 */ dailyItems.add(new DailyGiftItemInfo(11, 4310017, 10, 0));
        /*   181 */ dailyItems.add(new DailyGiftItemInfo(12, 4310017, 10, 0));
        /*   182 */ dailyItems.add(new DailyGiftItemInfo(13, 4310017, 10, 0));
        /*   183 */ dailyItems.add(new DailyGiftItemInfo(14, 4310017, 20, 0));

        /*   185 */ dailyItems.add(new DailyGiftItemInfo(15, 4310017, 10, 0));
        /*   186 */ dailyItems.add(new DailyGiftItemInfo(16, 4310017, 10, 0));
        /*   187 */ dailyItems.add(new DailyGiftItemInfo(17, 4310017, 10, 0));
        /*   188 */ dailyItems.add(new DailyGiftItemInfo(18, 4310017, 10, 0));
        /*   189 */ dailyItems.add(new DailyGiftItemInfo(19, 4310017, 10, 0));
        /*   190 */ dailyItems.add(new DailyGiftItemInfo(20, 4310017, 10, 0));
        /*   191 */ dailyItems.add(new DailyGiftItemInfo(21, 4310017, 20, 0));

        /*   193 */ dailyItems.add(new DailyGiftItemInfo(22, 4310017, 10, 0));
        /*   194 */ dailyItems.add(new DailyGiftItemInfo(23, 4310017, 10, 0));
        /*   195 */ dailyItems.add(new DailyGiftItemInfo(24, 4310017, 10, 0));
        /*   196 */ dailyItems.add(new DailyGiftItemInfo(25, 4310017, 10, 0));
        /*   197 */ dailyItems.add(new DailyGiftItemInfo(26, 4310017, 10, 0));
        /*   198 */ dailyItems.add(new DailyGiftItemInfo(27, 4310017, 10, 0));
        /*   199 */ dailyItems.add(new DailyGiftItemInfo(28, 4310017, 20, 0));
        price.put(Integer.valueOf(8800002), Integer.valueOf(175));
        price.put(Integer.valueOf(8880010), Integer.valueOf(190));
        price.put(Integer.valueOf(8500012), Integer.valueOf(185));
        price.put(Integer.valueOf(8870000), Integer.valueOf(200));
        price.put(Integer.valueOf(8810214), Integer.valueOf(210));
        price.put(Integer.valueOf(8920106), Integer.valueOf(220));
        price.put(Integer.valueOf(8900103), Integer.valueOf(220));
        price.put(Integer.valueOf(8910100), Integer.valueOf(220));
        price.put(Integer.valueOf(8930100), Integer.valueOf(220));
        price.put(Integer.valueOf(8810018), Integer.valueOf(225));
        price.put(Integer.valueOf(8840007), Integer.valueOf(230));
        price.put(Integer.valueOf(8860005), Integer.valueOf(240));
        price.put(Integer.valueOf(8880200), Integer.valueOf(250));
        price.put(Integer.valueOf(8810122), Integer.valueOf(260));
        price.put(Integer.valueOf(8820001), Integer.valueOf(265));
        price.put(Integer.valueOf(8840000), Integer.valueOf(270));
        price.put(Integer.valueOf(8840014), Integer.valueOf(350));
        price.put(Integer.valueOf(8860000), Integer.valueOf(355));
        price.put(Integer.valueOf(8880002), Integer.valueOf(360));
        price.put(Integer.valueOf(8500012), Integer.valueOf(365));
        price.put(Integer.valueOf(8850111), Integer.valueOf(675));
        price.put(Integer.valueOf(8870100), Integer.valueOf(750));
        price.put(Integer.valueOf(8820101), Integer.valueOf(800));
        price.put(Integer.valueOf(8850011), Integer.valueOf(850));
        price.put(Integer.valueOf(8800102), Integer.valueOf(900));
        price.put(Integer.valueOf(8920006), Integer.valueOf(900));
        price.put(Integer.valueOf(8900003), Integer.valueOf(900));
        price.put(Integer.valueOf(8910000), Integer.valueOf(900));
        price.put(Integer.valueOf(8880000), Integer.valueOf(975));
        price.put(Integer.valueOf(8930000), Integer.valueOf(1025));
        price.put(Integer.valueOf(8500022), Integer.valueOf(1150));
        price.put(Integer.valueOf(8950102), Integer.valueOf(1275));
        price.put(Integer.valueOf(8880111), Integer.valueOf(1300));
        price.put(Integer.valueOf(8880156), Integer.valueOf(1325));
        price.put(Integer.valueOf(8880167), Integer.valueOf(1425));
        price.put(Integer.valueOf(8880342), Integer.valueOf(1525));
        price.put(Integer.valueOf(8644650), Integer.valueOf(1575));
        price.put(Integer.valueOf(8880101), Integer.valueOf(1875));
        price.put(Integer.valueOf(8950002), Integer.valueOf(1925));
        price.put(Integer.valueOf(8880177), Integer.valueOf(2000));
        price.put(Integer.valueOf(8880302), Integer.valueOf(2100));
        price.put(Integer.valueOf(8644655), Integer.valueOf(2150));
        price.put(Integer.valueOf(8645066), Integer.valueOf(2200));
        price.put(Integer.valueOf(8880405), Integer.valueOf(2350));
        price.put(Integer.valueOf(8880504), Integer.valueOf(6000));
        price.put(Integer.valueOf(8880614), Integer.valueOf(8000));
        exp2[0] = 15L;
        exp2[1] = 34L;
        exp2[2] = 57L;
        exp2[3] = 92L;
        exp2[4] = 135L;
        exp2[5] = 372L;
        exp2[6] = 560L;
        exp2[7] = 840L;
        exp2[8] = 1242L;
        int a;
        for (a = 1; a < 15; a++) {
            exp[a] = exp2[a - 1];
            exp2[a - 1] = exp[a];
            if (a >= 9) {
                exp2[a] = exp[a];
            }
        }
        for (a = 15; a < 30; a++) {
            if (a == 15) {
                exp[15] = (long) Math.floor(exp[14] * 1.2D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.2D);
            }
        }
        for (a = 30; a < 35; a++) {
            if (a == 30) {
                exp[30] = exp[29];
            } else {
                exp[a] = exp[a - 1];
            }
        }
        for (a = 35; a < 40; a++) {
            if (a == 35) {
                exp[35] = (long) Math.floor(exp[34] * 1.2D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.2D);
            }
        }
        for (a = 40; a < 60; a++) {
            if (a == 40) {
                exp[40] = (long) Math.floor(exp[39] * 1.08D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.08D);
            }
        }
        for (a = 60; a < 65; a++) {
            if (a == 60) {
                exp[60] = exp[59];
            } else {
                exp[a] = exp[a - 1];
            }
        }
        for (a = 65; a < 75; a++) {
            if (a == 65) {
                exp[65] = (long) Math.floor(exp[64] * 1.075D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.075D);
            }
        }
        for (a = 75; a < 90; a++) {
            if (a == 75) {
                exp[75] = (long) Math.floor(exp[74] * 1.07D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.07D);
            }
        }
        for (a = 90; a < 100; a++) {
            if (a == 90) {
                exp[90] = (long) Math.floor(exp[89] * 1.065D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.065D);
            }
        }
        for (a = 100; a < 105; a++) {
            if (a == 100) {
                exp[100] = exp[99];
            } else {
                exp[a] = exp[a - 1];
            }
        }
        for (a = 105; a < 140; a++) {
            if (a == 105) {
                exp[105] = (long) Math.floor(exp[104] * 1.065D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.065D);
            }
        }
        for (a = 140; a < 170; a++) {
            if (a == 140) {
                exp[140] = (long) Math.floor(exp[139] * 1.0625D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.0625D);
            }
        }
        for (a = 170; a < 200; a++) {
            if (a == 170) {
                exp[170] = (long) Math.floor(exp[169] * 1.05D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.05D);
            }
        }
        for (a = 200; a < 210; a++) {
            if (a == 200 || a == 201) {
                exp[200] = 2207026470L;
                exp[201] = (long) Math.floor(exp[200] * 1.12D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.12D);
            }
        }
        for (a = 210; a < 215; a++) {
            if (a == 210 || a == 211) {
                exp[210] = (long) Math.floor(exp[209] * 1.6D);
                exp[211] = (long) Math.floor(exp[210] * 1.11D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.11D);
            }
        }
        for (a = 215; a < 220; a++) {
            if (a == 215 || a == 211) {
                exp[215] = (long) Math.floor(exp[214] * 1.3D);
                exp[211] = (long) Math.floor(exp[210] * 1.09D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.09D);
            }
        }
        for (a = 220; a < 225; a++) {
            if (a == 220 || a == 221) {
                exp[220] = (long) Math.floor(exp[219] * 1.6D);
                exp[221] = (long) Math.floor(exp[220] * 1.07D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.07D);
            }
        }
        for (a = 225; a < 230; a++) {
            if (a == 225 || a == 226) {
                exp[225] = (long) Math.floor(exp[224] * 1.3D);
                exp[226] = (long) Math.floor(exp[225] * 1.05D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.05D);
            }
        }
        for (a = 230; a < 235; a++) {
            if (a == 230) {
                exp[230] = (long) Math.floor(exp[229] * 1.6D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.03D);
            }
        }
        for (a = 235; a < 240; a++) {
            if (a == 235) {
                exp[235] = (long) Math.floor(exp[234] * 1.3D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.03D);
            }
        }
        for (a = 240; a < 245; a++) {
            if (a == 240) {
                exp[240] = (long) Math.floor(exp[239] * 1.6D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.03D);
            }
        }
        for (a = 245; a < 250; a++) {
            if (a == 245) {
                exp[245] = (long) Math.floor(exp[244] * 1.3D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.03D);
            }
        }
        for (a = 250; a < 260; a++) {
            if (a == 250) {
                exp[250] = 1313764762354L;
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.01D);
            }
        }
        for (a = 260; a < 275; a++) {
            if (a == 260 || a == 270) {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.01D * 2.0D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.01D);
            }
        }
        for (a = 275; a < 300; a++) {
            if (a == 275 || a == 280 || a == 285 || a == 290 || a == 295 || a == 300) {
                exp[a] = (long) Math.floor(exp[a - 1] * 2.02D);
            } else {
                exp[a] = (long) Math.floor(exp[a - 1] * 1.1D);
            }
        }
        exp[300] = 1L;
    }
    
        static {
        // 추가 장비
        CustomItem ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "호그의 이빨"); //0
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 5);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 3);
        customItems.add(ci);

        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "루칸의 등껍질"); //1
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 10);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 6);
        customItems.add(ci);

        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "골드리치의 이어링"); //2
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 15);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 9);
        customItems.add(ci);

        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "어둠의 보석"); //3
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 20);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 12);
        customItems.add(ci);
        
        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "이계의 보석"); //4
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 25);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 15);
        customItems.add(ci);
        
        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "전설의 증표"); //5
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 30);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 18);
        customItems.add(ci);
        
        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "정복자의 영광"); //6
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 35);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 21);
        customItems.add(ci);
        
        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "초월자의 이어링"); //7
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 40);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 24);
        customItems.add(ci);
        
        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.보조장비, "정복자의 펜던트"); //8
        ci.addEffects(CustomItem.CustomItemEffect.CrD, 10);
        ci.addEffects(CustomItem.CustomItemEffect.BdR, 30);
        ci.addEffects(CustomItem.CustomItemEffect.AllStatR, 30);
        ci.addEffects(CustomItem.CustomItemEffect.MesoR, 20);
        ci.addEffects(CustomItem.CustomItemEffect.DropR, 20);
        customItems.add(ci);

        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.각인석, "흔한 각인석"); //9
        ci.addEffects(CustomItem.CustomItemEffect.CrD, 8);
        ci.addEffects(CustomItem.CustomItemEffect.DropR, 10);
        customItems.add(ci);

        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.각인석, "희귀한 각인석"); //10
        ci.addEffects(CustomItem.CustomItemEffect.CrD, 16);
        ci.addEffects(CustomItem.CustomItemEffect.DropR, 20);
        customItems.add(ci);
        
        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.각인석, "레전더리 각인석"); //11
        ci.addEffects(CustomItem.CustomItemEffect.CrD, 24);
        ci.addEffects(CustomItem.CustomItemEffect.DropR, 30);
        customItems.add(ci);
        
        ci = new CustomItem(customItems.size(), CustomItem.CustomItemType.각인석, "에픽 각인석"); //12
        ci.addEffects(CustomItem.CustomItemEffect.CrD, 40);
        ci.addEffects(CustomItem.CustomItemEffect.DropR, 50);
        customItems.add(ci);
    }

    public static long getExpNeededForLevel(int level) {
        return exp[level];
    }

    public static int getGuildExpNeededForLevel(int level) {
        if (level < 0 || level >= guildexp.length) {
            return Integer.MAX_VALUE;
        }
        return guildexp[level];
    }

    public static int getPVPExpNeededForLevel(int level) {
        if (level < 0 || level >= pvpExp.length) {
            return Integer.MAX_VALUE;
        }
        return pvpExp[level];
    }

    public static int getClosenessNeededForLevel(int level) {
        return closeness[level - 1];
    }

    public static int getMountExpNeededForLevel(int level) {
        return mountexp[level - 1];
    }

    public static int getTraitExpNeededForLevel(int level) {
        if (level < 0 || level >= cumulativeTraitExp.length) {
            return Integer.MAX_VALUE;
        }
        return cumulativeTraitExp[level];
    }

    public static int getSetExpNeededForLevel(int level) {
        if (level < 0 || level >= setScore.length) {
            return Integer.MAX_VALUE;
        }
        return setScore[level];
    }

    public static int getMonsterHP(int level) {
        if (level < 0 || level >= mobHpVal.length) {
            return Integer.MAX_VALUE;
        }
        return mobHpVal[level];
    }

    public static int getBookLevel(int level) {
        return 5 * level * (level + 1);
    }

    public static int getTimelessRequiredEXP(int level) {
        return 70 + level * 10;
    }

    public static int getReverseRequiredEXP(int level) {
        return 60 + level * 5;
    }

    public static int getProfessionEXP(int level) {
        if (level >= 10) {
            switch (level) {
                case 10:
                    return 45000;
                case 11:
                    return 160000;
            }
        }
        return (100 * level * level + level * 400) / 2;
    }

    public static boolean isHarvesting(int itemId) {
        return (itemId >= 1500000 && itemId < 1520000);
    }

    public static final double maxViewRangeSq() {
        return Double.POSITIVE_INFINITY;
    }

    public static int maxViewRangeSq_Half() {
        return 500000;
    }

    public static boolean isJobFamily(int baseJob, int currentJob) {
        return (currentJob >= baseJob && currentJob / 100 == baseJob / 100);
    }

    public static boolean isGM(int job) {
        return (job == 800 || job == 900);
    }

    public static boolean isKOC(int job) {
        return (job >= 1000 && job < 2000);
    }

    public static final boolean isNightWalker(int job) {
        return (job == 1400 || (job >= 1400 && job <= 1412));
    }

    public static boolean isEvan(int job) {
        return (job == 2001 || (job >= 2200 && job <= 2218));
    }

    public static boolean isMichael(int job) {
        return (job == 5000 || (job >= 5000 && job <= 5112));
    }

    public static boolean isMercedes(int job) {
        return (job == 2002 || (job >= 2300 && job <= 2312));
    }

    public static boolean isEunWol(int job) {
        return (job == 2005 || (job >= 2500 && job <= 2512));
    }

    public static boolean isKinesis(int job) {
        return (job == 14000 || (job >= 14200 && job <= 14212));
    }

    public static boolean isDemonSlayer(int job) {
        return (job == 3001 || (job >= 3100 && job <= 3112));
    }

    public static boolean isDemonAvenger(int job) {
        return (job == 3101 || (job >= 3120 && job <= 3122));
    }

    public static final boolean isArk(int job) {
        return (job == 15001 || (job >= 15500 && job <= 15512));
    }

    public static final boolean isHoyeong(int job) {
        return (job == 16000 || (job >= 16400 && job <= 16412));
    }

    public static final boolean isLara(int job) {
        return (job == 16001 || (job >= 16200 && job <= 16212));
    }

    public static final boolean isAdel(int job) {
        return (job == 15002 || (job >= 15100 && job <= 15112));
    }

    public static final boolean isCain(int job) {
        return (job == 6003 || (job >= 6300 && job <= 6312));
    }

    public static boolean isXenon(int job) {
        return (job == 3002 || (job >= 3600 && job <= 3612));
    }

    public static boolean isAngelicBuster(int job) {
        return (job == 6001 || (job >= 6500 && job <= 6512));
    }

    public static boolean isAran(int job) {
        return (job == 2000 || (job >= 2100 && job <= 2112));
    }

    public static boolean isKadena(int job) {
        return (job == 6002 || (job >= 6400 && job <= 6412));
    }

    public static boolean isArc(int job) {
        return (job == 15001 || (job >= 15500 && job <= 15512));
    }

    public static boolean isIllium(int job) {
        return (job == 15000 || (job >= 15200 && job <= 15212));
    }

    public static boolean isResist(int job) {
        return (job >= 3000 && job <= 3712);
    }

    public static boolean isAdventurer(int job) {
        return (job >= 0 && job < 1000);
    }

    public static boolean isPhantom(int job) {
        return (job == 2003 || job / 100 == 24);
    }

    public static boolean isBattleMage(int job) {
        return (job >= 3200 && job <= 3212);
    }

    public static boolean isWildHunter(int job) {
        return (job >= 3300 && job <= 3312);
    }

    public static boolean isMechanic(int job) {
        return (job >= 3500 && job <= 3512);
    }

    public static boolean isLuminous(int job) {
        return (job == 2004 || (job >= 2700 && job <= 2712));
    }

    public static boolean isKaiser(int job) {
        return (job == 6000 || (job >= 6100 && job <= 6112));
    }

    public static boolean isSoulMaster(int job) {
        return (job >= 1100 && job <= 1112);
    }

    public static boolean isFlameWizard(int job) {
        return (job >= 1200 && job <= 1212);
    }

    public static boolean isWindBreaker(int job) {
        return (job >= 1300 && job <= 1312);
    }

    public static boolean isStriker(int job) {
        return (job >= 1500 && job <= 1512);
    }

    public static boolean isZero(int job) {
        return (job == 10000 || (job >= 10100 && job <= 10112));
    }

    public static boolean isPathFinder(int job) {
        return (job == 301 || (job >= 330 && job <= 332));
    }

    public static boolean JobCodeCheck(int firstjob, int secondjob) {
        if (isHero(firstjob) && isHero(secondjob)) {
            return true;
        }
        if (isPaladin(firstjob) && isPaladin(secondjob)) {
            return true;
        }
        if (isDarkKnight(firstjob) && isDarkKnight(secondjob)) {
            return true;
        }
        if (isFPMage(firstjob) && isFPMage(secondjob)) {
            return true;
        }
        if (isILMage(firstjob) && isILMage(secondjob)) {
            return true;
        }
        if (isBishop(firstjob) && isBishop(secondjob)) {
            return true;
        }
        if (isPathFinder(firstjob) && isPathFinder(secondjob)) {
            return true;
        }
        if (isBowMaster(firstjob) && isBowMaster(secondjob)) {
            return true;
        }
        if (isMarksMan(firstjob) && isMarksMan(secondjob)) {
            return true;
        }
        if (isNightLord(firstjob) && isNightLord(secondjob)) {
            return true;
        }
        if (isShadower(firstjob) && isShadower(secondjob)) {
            return true;
        }
        if (isDualBlade(firstjob) && isDualBlade(secondjob)) {
            return true;
        }
        if (isViper(firstjob) && isViper(secondjob)) {
            return true;
        }
        if (isCaptain(firstjob) && isCaptain(secondjob)) {
            return true;
        }
        if (isCannon(firstjob) && isCannon(secondjob)) {
            return true;
        }
        if (isSoulMaster(firstjob) && isSoulMaster(secondjob)) {
            return true;
        }
        if (isFlameWizard(firstjob) && isFlameWizard(secondjob)) {
            return true;
        }
        if (isWindBreaker(firstjob) && isWindBreaker(secondjob)) {
            return true;
        }
        if (isNightWalker(firstjob) && isNightWalker(secondjob)) {
            return true;
        }
        if (isStriker(firstjob) && isStriker(secondjob)) {
            return true;
        }
        if (isAran(firstjob) && isAran(secondjob)) {
            return true;
        }
        if (isMercedes(firstjob) && isMercedes(secondjob)) {
            return true;
        }
        if (isEvan(firstjob) && isEvan(secondjob)) {
            return true;
        }
        if (isPhantom(firstjob) && isPhantom(secondjob)) {
            return true;
        }
        if (isEunWol(firstjob) && isEunWol(secondjob)) {
            return true;
        }
        if (isLuminous(firstjob) && isLuminous(secondjob)) {
            return true;
        }
        if (isBlaster(firstjob) && isBlaster(secondjob)) {
            return true;
        }
        if (isMechanic(firstjob) && isMechanic(secondjob)) {
            return true;
        }
        if (isBattleMage(firstjob) && isBattleMage(secondjob)) {
            return true;
        }
        if (isWildHunter(firstjob) && isWildHunter(secondjob)) {
            return true;
        }
        if (isDemonSlayer(firstjob) && isDemonSlayer(secondjob)) {
            return true;
        }
        if (isDemonAvenger(firstjob) && isDemonAvenger(secondjob)) {
            return true;
        }
        if (isXenon(firstjob) && isXenon(secondjob)) {
            return true;
        }
        if (isMichael(firstjob) && isMichael(secondjob)) {
            return true;
        }
        if (isKaiser(firstjob) && isKaiser(secondjob)) {
            return true;
        }
        if (isCain(firstjob) && isCain(secondjob)) {
            return true;
        }
        if (isKadena(firstjob) && isKadena(secondjob)) {
            return true;
        }
        if (isAngelicBuster(firstjob) && isAngelicBuster(secondjob)) {
            return true;
        }
        if (isZero(firstjob) && isZero(secondjob)) {
            return true;
        }
        if (isKinesis(firstjob) && isKinesis(secondjob)) {
            return true;
        }
        if (isIllium(firstjob) && isIllium(secondjob)) {
            return true;
        }
        if (isArc(firstjob) && isArc(secondjob)) {
            return true;
        }
        if (isAdel(firstjob) && isAdel(secondjob)) {
            return true;
        }
        if (isHoyeong(firstjob) && isHoyeong(secondjob)) {
            return true;
        }
        if (isLara(firstjob) && isLara(secondjob)) {
            return true;
        }
        return false;
    }

    public static boolean isFourthJob(int job) {
        switch (job) {
            case 112:
            case 122:
            case 132:
            case 212:
            case 222:
            case 232:
            case 312:
            case 322:
            case 412:
            case 422:
            case 512:
            case 522:
                return true;
        }
        return false;
    }

    public static boolean isRecoveryIncSkill(int id) {
        switch (id) {
            case 1110000:
            case 1210000:
            case 2000000:
            case 4100002:
            case 4200001:
            case 11110000:
                return true;
        }
        return false;
    }

    public static boolean isFusionSkill(int skill) {
        switch (skill) {
            case 22110014:
            case 22110024:
            case 22110025:
            case 22140014:
            case 22140015:
            case 22140023:
            case 22140024:
            case 22170065:
            case 22170066:
            case 22170067:
            case 22170094:
            case 400020046:
                return true;
        }
        return false;
    }

    public static boolean isAngelicBlessSkill(Skill skill) {
        if (!skill.isVMatrix()) {
            switch (skill.getId() % 10000) {
                case 1085:
                case 1087:
                case 1090:
                case 1179:
                    return true;
            }
        }
        return false;
    }

    public static boolean isSaintSaverSkill(int skill) {
        switch (skill) {
            case 80001034:
            case 80001035:
            case 80001036:
                return true;
        }
        return false;
    }

    public static boolean isLinkedSkill(int id) {
        return (getLinkedSkill(id) != id);
    }

    public static int getLinkedSkill(int id) {
        switch (id) {
            case 162101006:
            case 162101007:
                return 162100005;
            case 162101003:
            case 162101004:
                return 162100002;
            case 162121044:
                return 162121043;
            case 400021131:
                return 400021130;
            case 162121003:
            case 162121004:
                return 162120002;
            case 162121006:
            case 162121007:
                return 162120005;
            case 162121009:
            case 162121010:
                return 162120008;
            case 162111010:
                return 162111002;
            case 162101009:
            case 162101010:
            case 162101011:
                return 162100008;
            case 162121012:
            case 162121013:
            case 162121014:
            case 162121015:
            case 162121016:
            case 162121017:
            case 162121018:
            case 162121019:
                return 162120011;
            case 135001004:
            case 135003003:
            case 135003004:
                return 135001003;
            case 400041023:
            case 400041024:
            case 400041080:
                return 400041022;
            case 400001052:
                return 400001007;
            case 131002025:
                return 131001025;
            case 131002026:
            case 131003026:
                return 131001026;
            case 131002015:
                return 131001015;
            case 131002023:
            case 131003023:
            case 131004023:
            case 131005023:
            case 131006023:
                return 131001023;
            case 131002022:
            case 131003022:
            case 131004022:
            case 131005022:
            case 131006022:
                return 131001022;
            case 131001113:
            case 131001213:
            case 131001313:
                return 131001013;
            case 132001017:
            case 133001017:
                return 131001017;
            case 63001003:
            case 63001005:
                return 63001002;
            case 135002018:
                return 135001018;
            case 400001060:
                return 400001059;
            case 14001031:
                return 14001023;
            case 155121004:
                return 155121102;
            case 400011065:
                return 400011055;
            case 400011092:
            case 400011093:
            case 400011094:
            case 400011095:
            case 400011096:
            case 400011097:
            case 400011103:
                return 400011091;
            case 37120055:
            case 37120056:
            case 37120057:
            case 37120058:
            case 37120059:
                return 37121052;
            case 37110001:
            case 37110002:
                return 37111000;
            case 14001030:
                return 14001026;
            case 2121055:
                return 2121052;
            case 400001011:
                return 400001010;
            case 23111009:
            case 23111010:
                return 23111008;
            case 400041056:
                return 400041055;
            case 400021100:
            case 400021111:
                return 400021099;
            case 400041058:
                return 400041057;
            case 400011135:
                return 400011134;
            case 400021097:
            case 400021098:
            case 400021104:
                return 400021096;
            case 400011119:
            case 400011120:
                return 400011118;
            case 400051069:
                return 400051068;
            case 400011111:
                return 400011110;
            case 400021088:
            case 400021089:
                return 400021087;
            case 400011113:
            case 400011114:
            case 400011115:
            case 400011129:
                return 400011112;
            case 400021112:
                return 400021094;
            case 400031045:
                return 400031044;
            case 400011085:
                return 400011047;
            case 400051079:
                return 400051078;
            case 400051075:
            case 400051076:
            case 400051077:
                return 400051074;
            case 400011132:
                return 400011131;
            case 400011122:
                return 400011121;
            case 400031059:
                return 400031058;
            case 400041060:
                return 400041059;
            case 400051059:
            case 400051060:
            case 400051061:
            case 400051062:
            case 400051063:
            case 400051064:
            case 400051065:
            case 400051066:
            case 400051067:
                return 400051058;
            case 400031047:
            case 400031048:
            case 400031049:
            case 400031050:
            case 400031051:
                return 400031057;
            case 400051071:
                return 400051070;
            case 400041070:
            case 400041071:
            case 400041072:
            case 400041073:
                return 400041069;
            case 400041076:
            case 400041077:
            case 400041078:
                return 400041075;
            case 400041062:
            case 400041079:
                return 400041061;
            case 5120021:
                return 5121013;
            case 25111211:
                return 25111209;
            case 400031031:
                return 400031030;
            case 400031054:
                return 400031053;
            case 400031056:
                return 400031055;
            case 30001078:
            case 30001079:
            case 30001080:
                return 30001068;
            case 61121026:
                return 61121102;
            case 400001040:
            case 400001041:
                return 400001039;
            case 400041051:
                return 400041050;
            case 400001044:
                return 400001043;
            case 151101004:
            case 151101010:
                return 151101003;
            case 131001001:
            case 131001002:
            case 131001003:
                return 131001000;
            case 131001106:
            case 131001206:
            case 131001306:
            case 131001406:
            case 131001506:
                return 131001006;
            case 131001107:
            case 131001207:
            case 131001307:
                return 131001007;
            case 24121010:
                return 24121003;
            case 24111008:
                return 24111006;
            case 151101007:
            case 151101008:
                return 151101006;
            case 142120001:
                return 142120000;
            case 142110003:
                return 142111002;
            case 400041049:
                return 400041048;
            case 400041053:
                return 400041052;
            case 37000009:
                return 37001001;
            case 37100008:
                return 37100007;
            case 151001003:
                return 151001002;
            case 400001051:
            case 400001053:
            case 400001054:
            case 400001055:
                return 400001050;
            case 95001000:
                return 3111013;
            case 400031018:
            case 400031019:
                return 400031017;
            case 164111016:
                return 164111003;
            case 164111001:
            case 164111002:
            case 164111009:
            case 164111010:
            case 164111011:
                return 164110000;
            case 400001047:
            case 400001048:
            case 400001049:
                return 400001046;
            case 164001002:
                return 164001001;
            case 151121011:
                return 151121004;
            case 164101001:
            case 164101002:
                return 164100000;
            case 164101004:
                return 164101003;
            case 164121001:
            case 164121002:
            case 164121014:
                return 164120000;
            case 164121004:
                return 164121003;
            case 164121015:
                return 164121008;
            case 164120007:
                return 164121007;
            case 164121044:
                return 164121043;
            case 164121011:
            case 164121012:
                return 164121006;
            case 164111004:
            case 164111005:
            case 164111006:
                return 164111003;
            case 400031035:
                return 400031034;
            case 400031038:
            case 400031039:
            case 400031040:
            case 400031041:
            case 400031042:
            case 400031043:
                return 400031037;
            case 31011004:
            case 31011005:
            case 31011006:
            case 31011007:
                return 31011000;
            case 31201007:
            case 31201008:
            case 31201009:
            case 31201010:
                return 31201000;
            case 31211007:
            case 31211008:
            case 31211009:
            case 31211010:
                return 31211000;
            case 31221009:
            case 31221010:
            case 31221011:
            case 31221012:
                return 31221000;
            case 3311011:
                return 3311010;
            case 3011006:
            case 3011007:
            case 3011008:
                return 3011005;
            case 3301009:
                return 3301008;
            case 3301004:
                return 3301003;
            case 3321003:
            case 3321004:
            case 3321005:
            case 3321006:
            case 3321007:
                return 3320002;
            case 3321036:
            case 3321037:
            case 3321038:
            case 3321039:
            case 3321040:
                return 3321035;
            case 3321016:
            case 3321017:
            case 3321018:
            case 3321019:
            case 3321020:
            case 3321021:
                return 3321014;
            case 21000004:
                return 21001009;
            case 142100010:
                return 142101009;
            case 142100008:
                return 142101002;
            case 27120211:
                return 27121201;
            case 33121255:
                return 33121155;
            case 33101115:
                return 33101215;
            case 37000005:
                return 37001004;
            case 400011074:
            case 400011075:
            case 400011076:
                return 400011073;
            case 33001202:
                return 33001102;
            case 152000009:
                return 152000007;
            case 152001005:
                return 152001004;
            case 152120002:
                return 152120001;
            case 152101000:
            case 152101004:
                return 152101003;
            case 152121006:
                return 152121005;
            case 400051019:
            case 400051020:
                return 400051018;
            case 152110004:
            case 152120016:
            case 152120017:
                return 152001001;
            case 400021064:
            case 400021065:
                return 400021063;
            case 1100012:
                return 1101012;
            case 1111014:
                return 1111008;
            case 2100010:
                return 2101010;
            case 61111114:
            case 61111221:
                return 61111008;
            case 14121055:
            case 14121056:
                return 14121054;
            case 61121220:
                return 61121015;
            case 400031008:
            case 400031009:
                return 400031007;
            case 142120030:
                return 142121030;
            case 400051039:
            case 400051052:
            case 400051053:
                return 400051038;
            case 400021043:
            case 400021044:
            case 400021045:
                return 400021042;
            case 400051049:
            case 400051050:
                return 400051040;
            case 400040006:
                return 400041006;
            case 155001204:
                return 155001104;
            case 400031026:
            case 400031027:
                return 400031025;
            case 61121222:
                return 61121105;
            case 400020046:
            case 400020051:
            case 400021013:
            case 400021014:
            case 400021015:
            case 400021016:
                return 400021012;
            case 61121116:
            case 61121124:
            case 61121221:
            case 61121223:
            case 61121225:
                return 61121104;
            case 400011002:
                return 400011001;
            case 400010030:
                return 400011031;
            case 400051051:
                return 400051041;
            case 400021077:
                return 400021070;
            case 2120013:
                return 2121007;
            case 2220014:
                return 2221007;
            case 32121011:
                return 32121004;
            case 400011059:
            case 400011060:
            case 400011061:
                return 400011058;
            case 400021075:
            case 400021076:
                return 400021074;
            case 400011033:
            case 400011034:
            case 400011035:
            case 400011036:
            case 400011037:
            case 400011067:
                return 400011032;
            case 400011080:
            case 400011081:
            case 400011082:
                return 400011079;
            case 400011084:
                return 400011083;
            case 21120026:
                return 21120019;
            case 400020009:
            case 400020010:
            case 400020011:
            case 400021010:
            case 400021011:
                return 400021008;
            case 400041026:
            case 400041027:
                return 400041025;
            case 400040008:
            case 400041019:
                return 400041008;
            case 400041003:
            case 400041004:
            case 400041005:
                return 400041002;
            case 400051045:
                return 400051044;
            case 400011078:
                return 400011077;
            case 400031016:
                return 400031015;
            case 400031013:
            case 400031014:
                return 400031012;
            case 400011102:
                return 400011090;
            case 400020002:
                return 400021002;
            case 22140023:
                return 22140014;
            case 22140024:
                return 22140015;
            case 22141012:
                return 22140022;
            case 22110014:
            case 22110025:
                return 22110014;
            case 22170061:
                return 22170060;
            case 22170093:
                return 22170064;
            case 22171083:
                return 22171080;
            case 22170094:
                return 22170065;
            case 400011069:
                return 400011068;
            case 400031033:
                return 400031032;
            case 25121133:
                return 25121131;
            case 23121015:
                return 23121014;
            case 24120055:
                return 24121052;
            case 31221014:
                return 31221001;
            case 400021031:
            case 400021040:
                return 400021030;
            case 4120019:
                return 4120018;
            case 37000010:
                return 37001001;
            case 155001000:
                return 155001001;
            case 155001009:
                return 155001104;
            case 155100009:
                return 155101008;
            case 155101002:
                return 155101003;
            case 155101013:
            case 155101015:
            case 155101101:
            case 155101112:
                return 155101100;
            case 155101114:
                return 155101104;
            case 155101214:
                return 155101204;
            case 155101201:
            case 155101212:
                return 155101200;
            case 155111002:
            case 155111111:
                return 155111102;
            case 155111103:
            case 155111104:
                return 155111105;
            case 155111106:
                return 155111102;
            case 155111211:
            case 155111212:
                return 155111202;
            case 155121002:
                return 155121102;
            case 155121003:
                return 155121005;
            case 155121006:
            case 155121007:
                return 155121306;
            case 155121215:
                return 155121202;
            case 400041010:
            case 400041011:
            case 400041012:
            case 400041013:
            case 400041014:
            case 400041015:
                return 400041009;
            case 400011099:
                return 400011098;
            case 400011101:
                return 400011100;
            case 400011053:
                return 400011052;
            case 400001016:
                return 400001013;
            case 400021029:
                return 400021028;
            case 400030002:
                return 400031002;
            case 400021049:
            case 400021050:
                return 400021041;
            case 14000027:
            case 14000028:
            case 14000029:
                return 14001027;
            case 4100011:
            case 4100012:
                return 4101011;
            case 5211015:
            case 5211016:
                return 5211011;
            case 5220023:
            case 5220024:
            case 5220025:
                return 5221022;
            case 51001006:
            case 51001007:
            case 51001008:
            case 51001009:
            case 51001010:
            case 51001011:
            case 51001012:
            case 51001013:
                return 51001005;
            case 25120115:
                return 25120110;
            case 5201005:
                return 5201011;
            case 5320011:
                return 5321004;
            case 33001008:
            case 33001009:
            case 33001010:
            case 33001011:
            case 33001012:
            case 33001013:
            case 33001014:
            case 33001015:
                return 33001007;
            case 65120011:
                return 65121011;
            case 400041034:
                return 400041033;
            case 400041036:
                return 400041035;
            case 21110027:
            case 21110028:
            case 21111021:
                return 21110020;
            case 100000276:
            case 100000277:
                return 100000267;
            case 400001025:
            case 400001026:
            case 400001027:
            case 400001028:
            case 400001029:
            case 400001030:
                return 400001024;
            case 400001015:
                return 400001014;
            case 400011013:
            case 400011014:
                return 400011012;
            case 400001022:
                return 400001019;
            case 400021033:
            case 400021052:
                return 400021032;
            case 400041016:
                return 4001344;
            case 400041017:
                return 4111010;
            case 400041018:
                return 4121013;
            case 400051003:
            case 400051004:
            case 400051005:
                return 400051002;
            case 400051025:
            case 400051026:
                return 400051024;
            case 400051023:
                return 400051022;
            case 2321055:
                return 2321052;
            case 5121055:
                return 5121052;
            case 61111220:
                return 61111002;
            case 12001027:
            case 12001028:
                return 12000023;
            case 36121013:
            case 36121014:
                return 36121002;
            case 36121011:
            case 36121012:
                return 36121001;
            case 400010010:
                return 400011010;
            case 10001253:
            case 10001254:
            case 14001026:
                return 10000252;
            case 142000006:
                return 142001004;
            case 4321001:
                return 4321000;
            case 33101006:
            case 33101007:
                return 33101005;
            case 33101008:
                return 33101004;
            case 35101009:
            case 35101010:
                return 35100008;
            case 35111009:
            case 35111010:
                return 35111001;
            case 35121013:
                return 35111005;
            case 35121011:
                return 35121009;
            case 3000008:
            case 3000009:
            case 3000010:
                return 3001007;
            case 32001007:
            case 32001008:
            case 32001009:
            case 32001010:
            case 32001011:
                return 32001001;
            case 64001007:
            case 64001008:
            case 64001009:
            case 64001010:
            case 64001011:
            case 64001012:
                return 64001000;
            case 64001013:
                return 64001002;
            case 64100001:
                return 64100000;
            case 64001006:
                return 64001001;
            case 64101008:
                return 64101002;
            case 64111012:
                return 64111004;
            case 64121012:
            case 64121013:
            case 64121014:
            case 64121015:
            case 64121017:
            case 64121018:
            case 64121019:
                return 64121001;
            case 64121022:
            case 64121023:
            case 64121024:
                return 64121021;
            case 64121016:
                return 64121003;
            case 64121055:
                return 64121053;
            case 5300007:
                return 5301001;
            case 23101007:
                return 23101001;
            case 31001006:
            case 31001007:
            case 31001008:
                return 31000004;
            case 30010183:
            case 30010184:
            case 30010186:
                return 30010110;
            case 25000001:
                return 25001000;
            case 25000003:
                return 25001002;
            case 25100001:
            case 25100002:
                return 25101000;
            case 25100010:
                return 25100009;
            case 25110001:
            case 25110002:
            case 25110003:
                return 25111000;
            case 25120001:
            case 25120002:
            case 25120003:
                return 25121000;
            case 101000102:
                return 101000101;
            case 101000202:
                return 101000201;
            case 101100202:
                return 101100201;
            case 101110201:
            case 101110204:
                return 101110203;
            case 101120101:
                return 101120100;
            case 101120103:
                return 101120102;
            case 101120105:
            case 101120106:
                return 101120104;
            case 101120203:
                return 101120202;
            case 400031021:
                return 400031020;
            case 101120205:
            case 101120206:
                return 101120204;
            case 101120200:
                return 101121200;
            case 100001266:
            case 100001269:
                return 100001265;
            case 1111002:
                return 1101013;
            case 3120019:
                return 3111009;
            case 5201013:
            case 5201014:
                return 5201012;
            case 5210016:
            case 5210017:
            case 5210018:
                return 5210015;
            case 11121055:
                return 11121052;
            case 12120011:
                return 12121001;
            case 12121055:
                return 12121054;
            case 12120013:
            case 12120014:
                return 12121004;
            case 14111023:
                return 14111022;
            case 61110211:
            case 61120007:
            case 61121217:
                return 61101002;
            case 61120008:
                return 61111008;
            case 61121201:
                return 61121100;
            case 65111007:
                return 65111100;
            case 36111009:
            case 36111010:
                return 36111000;
        }
        if (id == 155101204) {
            return 155101104;
        }
        return id;
    }

    public static final boolean isDemonSlash(int skillid) {
        switch (skillid) {
            case 31000004:
            case 31001006:
            case 31001007:
            case 31001008:
            case 31100007:
            case 31110010:
            case 31120011:
                return true;
        }
        return false;
    }

    public static final boolean isAfterCooltimeSkill(int skillid) {
        switch (skillid) {
            case 2221052:
            case 2301002:
            case 4001003:
            case 12121054:
            case 14001023:
            case 20031205:
            case 24111002:
            case 24121005:
            case 25111005:
            case 31101002:
            case 33111013:
            case 142121005:
            case 151101006:
            case 151101007:
            case 151101008:
            case 151101009:
            case 400011038:
            case 400021072:
            case 400041003:
            case 400041004:
            case 400041005:
            case 400041007:
            case 400001063:
                return true;
        }
        return false;
    }

    public static final boolean isForceIncrease(int skillid) {
        if (isDemonSlash(skillid)) {
            return true;
        }
        switch (skillid) {
            case 400011007:
            case 400011008:
            case 400011009:
            case 400011018:
                return true;
        }
        return false;
    }

    public static int getBOF_ForJob(int job) {
        return PlayerStats.getSkillByJob(12, job);
    }

    public static int getEmpress_ForJob(int job) {
        return PlayerStats.getSkillByJob(73, job);
    }

    public static boolean isElementAmp_Skill(int skill) {
        switch (skill) {
            case 2110001:
            case 2210001:
            case 12110001:
            case 22150000:
                return true;
        }
        return false;
    }

    public static int getJobShortValue(int job) {
        if (job >= 1000) {
            job -= job / 1000 * 1000;
        }
        job /= 100;
        if (job == 4) {
            job *= 2;
        } else if (job == 3) {
            job++;
        } else if (job == 5) {
            job += 11;
        }
        return job;
    }

    public static boolean isPyramidSkill(int skill) {
        return (isBeginnerJob(skill / 10000) && skill % 10000 == 1020);
    }

    public static boolean isInflationSkill(int skill) {
        return (isBeginnerJob(skill / 10000) && (skill % 10000 == 1092 || skill % 10000 == 1094 || skill % 10000 == 1095));
    }

    public static boolean isMulungSkill(int skill) {
        return (isBeginnerJob(skill / 10000) && (skill % 10000 == 1009 || skill % 10000 == 1010 || skill % 10000 == 1011));
    }

    public static boolean isThrowingStar(int itemId) {
        return (itemId / 10000 == 207);
    }

    public static boolean isArcaneSymbol(int itemid) {
        return (itemid >= 1712000 && itemid <= 1712006);
    }

    public static boolean isAuthenticSymbol(int itemid) {
        return (itemid >= 1713000 && itemid <= 1713002);
    }

    public static boolean isBullet(int itemId) {
        return (itemId / 10000 == 233);
    }

    public static boolean isRechargable(int itemId) {
        return (isThrowingStar(itemId) || isBullet(itemId));
    }

    public static boolean isOverall(int itemId) {
        return (itemId / 10000 == 105);
    }

    public static boolean isPet(int itemId) {
        return (itemId / 10000 == 500);
    }

    public static boolean isArrowForCROSSBOW(int itemId) {
        return (itemId >= 2061000 && itemId < 2062000);
    }

    public static boolean isArrowForBow(int itemId) {
        return (itemId >= 2060000 && itemId < 2061000);
    }

    public static boolean isMagicWeapon(int itemId) {
        int s = itemId / 10000;
        return (s == 137 || s == 138 || s == 121 || s == 126);
    }

    public static boolean isWeapon(int itemId) {
        if (itemId >= 1660000 && itemId <= 1680000) {
            return false;
        }
        if (itemId >= 1200000 && itemId < 1800000 && !isArcaneSymbol(itemId) && !isAuthenticSymbol(itemId)) {
            return true;
        }
        return false;
    }

    public static boolean isSecondaryWeapon(int itemId) {
        return (itemId / 10000 == 135 || itemId / 1000 == 1098 || itemId / 1000 == 1099);
    }

    public static boolean isCap(int itemId) {
        return (itemId / 1000 == 1000 || itemId / 1000 == 1001 || itemId / 1000 == 1002 || itemId / 1000 == 1003 || itemId / 1000 == 1004 || itemId / 1000 == 1005);
    }

    public static boolean isLongcoat(int itemId) {
        return (itemId / 1000 == 1050 || itemId / 1000 == 1051 || itemId / 1000 == 1052 || itemId / 1000 == 1053 || itemId == 1042264);
    }

    public static boolean isCape(int itemId) {
        return (itemId / 10000 == 109 || itemId / 10000 == 110 || itemId / 10000 == 113);
    }

    public static MapleInventoryType getInventoryType(int itemId) {
        byte type = (byte) (itemId / 1000000);
        if (type == 1 && MapleItemInformationProvider.getInstance().isCash(itemId)) {
            return MapleInventoryType.CODY;
        }
        if (type < 1 || type > 5) {
            return MapleInventoryType.UNDEFINED;
        }
        return MapleInventoryType.getByType(type);
    }

    public static boolean isInBag(int slot, byte type) {
        return (slot >= 256 && slot <= 512 && type == MapleInventoryType.ETC.getType());
    }

    public static MapleWeaponType getWeaponType(int itemId) {
        int cat = itemId / 10000;
        cat %= 100;
        switch (cat) {
            case 21:
                if (itemId < 1213000) {
                    return MapleWeaponType.PLANE;
                }
                return MapleWeaponType.TUNER;
            case 22:
                return MapleWeaponType.SOULSHOOTER;
            case 23:
                return MapleWeaponType.DESPERADO;
            case 24:
                return MapleWeaponType.ENERGYSWORD;
            case 26:
                return MapleWeaponType.ESPLIMITER;
            case 27:
                return MapleWeaponType.CHAIN;
            case 28:
                return MapleWeaponType.MAGICGUNTLET;
            case 29:
                return MapleWeaponType.FAN;
            case 30:
                return MapleWeaponType.SWORD1H;
            case 31:
                return MapleWeaponType.AXE1H;
            case 32:
                return MapleWeaponType.BLUNT1H;
            case 33:
                return MapleWeaponType.DAGGER;
            case 34:
                return MapleWeaponType.KATARA;
            case 35:
                return MapleWeaponType.MAGIC_ARROW;
            case 36:
                return MapleWeaponType.CANE;
            case 37:
                return MapleWeaponType.WAND;
            case 38:
                return MapleWeaponType.STAFF;
            case 40:
                return MapleWeaponType.SWORD2H;
            case 41:
                return MapleWeaponType.AXE2H;
            case 42:
                return MapleWeaponType.BLUNT2H;
            case 43:
                return MapleWeaponType.SPEAR;
            case 44:
                return MapleWeaponType.POLE_ARM;
            case 45:
                return MapleWeaponType.BOW;
            case 46:
                return MapleWeaponType.CROSSBOW;
            case 47:
                return MapleWeaponType.CLAW;
            case 48:
                return MapleWeaponType.KNUCKLE;
            case 49:
                return MapleWeaponType.GUN;
            case 52:
                return MapleWeaponType.DUAL_BOW;
            case 53:
                return MapleWeaponType.HANDCANNON;
            case 56:
                return MapleWeaponType.BIG_SWORD;
            case 57:
                return MapleWeaponType.LONG_SWORD;
            case 58:
                return MapleWeaponType.GUNTLETREVOLVER;
            case 59:
                return MapleWeaponType.ACIENTBOW;
        }
        return MapleWeaponType.NOT_A_WEAPON;
    }

    public static boolean isShield(int itemId) {
        int cat = itemId / 10000;
        cat %= 100;
        return (cat == 9);
    }

    public static boolean isEquip(int itemId) {
        return (itemId / 1000000 == 1);
    }

    public static boolean isCleanSlate(int itemId) {
        return (itemId / 100 == 20490);
    }

    public static boolean isAccessoryScroll(int itemId) {
        return (itemId / 100 == 20492);
    }

    public static int isRandStat(List<Pair<Integer, Integer>> list, int succ) {
        if (list.isEmpty()) {
            return 999;
        }
        int randomstat = 999;
        while (randomstat == 999) {
            int random = Randomizer.rand(0, list.size() - 1);
            Pair<Integer, Integer> test = list.get(random);
            if (Randomizer.isSuccess(((Integer) test.right).intValue(), succ)) {
                randomstat = ((Integer) test.left).intValue();
                break;
            }
        }
        return randomstat;
    }

    public static boolean isChaosScroll(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getName(itemId).contains("혼돈")) {
            return true;
        }
        return false;
    }

    public static int getChaosNumber(int itemId) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        int randomstat = 999;
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getName(itemId).contains("놀라운 긍정의")) {
            list.add(new Pair<>(Integer.valueOf(0), Integer.valueOf(1838)));
            list.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(3301)));
            list.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(2387)));
            list.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(1387)));
            list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(494)));
            list.add(new Pair<>(Integer.valueOf(6), Integer.valueOf(593)));
        } else if (ii.getName(itemId).contains("놀라운")) {
            list.add(new Pair<>(Integer.valueOf(-1), Integer.valueOf(137)));
            list.add(new Pair<>(Integer.valueOf(-2), Integer.valueOf(8)));
            list.add(new Pair<>(Integer.valueOf(-3), Integer.valueOf(365)));
            list.add(new Pair<>(Integer.valueOf(-4), Integer.valueOf(297)));
            list.add(new Pair<>(Integer.valueOf(-6), Integer.valueOf(494)));
            list.add(new Pair<>(Integer.valueOf(0), Integer.valueOf(1838)));
            list.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(1931)));
            list.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(1587)));
            list.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(1021)));
            list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(198)));
            list.add(new Pair<>(Integer.valueOf(6), Integer.valueOf(99)));
        } else if (ii.getName(itemId).contains("긍정의")) {
            list.add(new Pair<>(Integer.valueOf(0), Integer.valueOf(1838)));
            list.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(3301)));
            list.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(2387)));
            list.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(1387)));
            list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(494)));
            list.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(593)));
        } else if (ii.getName(itemId).contains("혼돈의")) {
            list.add(new Pair<>(Integer.valueOf(-1), Integer.valueOf(137)));
            list.add(new Pair<>(Integer.valueOf(-2), Integer.valueOf(8)));
            list.add(new Pair<>(Integer.valueOf(-3), Integer.valueOf(365)));
            list.add(new Pair<>(Integer.valueOf(-4), Integer.valueOf(297)));
            list.add(new Pair<>(Integer.valueOf(-5), Integer.valueOf(494)));
            list.add(new Pair<>(Integer.valueOf(0), Integer.valueOf(1838)));
            list.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(1931)));
            list.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(1587)));
            list.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(1021)));
            list.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(198)));
            list.add(new Pair<>(Integer.valueOf(6), Integer.valueOf(99)));
        } else {
            return 999;
        }
        while (randomstat == 999) {
            int potentialidt = getWeightedRandom(list);
            if (potentialidt != 0) {
                randomstat = potentialidt;
            }
        }
        return randomstat;
    }

    public static int getWeightedRandom(List<Pair<Integer, Integer>> weights) {
        int potentialid = 0;
        double bestValue = Double.MAX_VALUE;
        for (Pair<Integer, Integer> element : weights) {
            double a = ((Integer) element.getRight()).intValue();
            double r = a / 10000.0D;
            double value = -Math.log(Randomizer.nextDouble()) / r;
            if (value < bestValue) {
                bestValue = value;
                potentialid = ((Integer) element.getLeft()).intValue();
            }
        }
        return potentialid;
    }

    public static int isStarForceScroll(int scrollId) {
        int max = -1;
        switch (scrollId) {
            case 2049370:
            case 2644017:
                max = 12;
                break;
            case 2049371:
            case 2644002:
            case 2644004:
                max = 17;
                break;
            case 2049372:
            case 2644001:
                max = 15;
                break;
            case 2049376:
                max = 20;
                break;
            case 2049377:
                max = 25;
                break;
        }
        return max;
    }

    public static boolean isEquipScroll(int scrollId) {
        return (scrollId / 100 == 20493);
    }

    public static boolean isAlphaWeapon(int itemId) {
        return (itemId / 10000 == 157);
    }

    public static boolean isBetaWeapon(int itemId) {
        return (itemId / 10000 == 156);
    }

    public static boolean isZeroWeapon(int itemId) {
        return (isAlphaWeapon(itemId) || isBetaWeapon(itemId));
    }

    public static boolean isLuckyScroll(int scrollId) {
        return (scrollId / 100 == 20489);
    }

    public static boolean isPotentialScroll(int scrollId) {
        return (scrollId / 100 == 20494 || scrollId / 100 == 20497 || scrollId == 5534000);
    }

    public static boolean isProstyScroll(int scrollId) {
        switch (scrollId) {
            case 2046841:
            case 2046842:
            case 2046964:
            case 2046965:
            case 2046967:
            case 2046971:
            case 2047801:
            case 2047803:
            case 2047914:
            case 2047915:
            case 2047917:
                return true;
        }
        return false;
    }

    public static boolean isEightRockScroll(int scrollId) {
        return (scrollId / 1000 == 2046);
    }

    public static boolean isRebirthFireScroll(int scrollId) {
        return (scrollId / 100 == 20487);
    }

    public static boolean isSpecialScroll(int scrollId) {
        switch (scrollId) {
            case 2040727:
            case 2041058:
            case 2530000:
            case 2530001:
            case 2531000:
            case 2532000:
            case 2533000:
            case 2720000:
            case 2720001:
            case 5063000:
            case 5064000:
            case 5064100:
            case 5064200:
            case 5064300:
            case 5064400:
                return true;
        }
        return false;
    }

    public static boolean isTwoHanded(final int itemId) {
        switch (getWeaponType(itemId)) {
            case SWORD2H:
            case AXE2H:
            case BLUNT2H:
            case SPEAR:
            case POLE_ARM:
            case BIG_SWORD:
            case LONG_SWORD:
            case GUNTLETREVOLVER:
            case BOW:
            case CROSSBOW:
            case DUAL_BOW:
            case CLAW:
            case GUN:
            case KNUCKLE:
            case HANDCANNON:
            case ACIENTBOW: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isSpecialShield(int itemid) {
        return (itemid / 1000 == 1098 || itemid / 1000 == 1099 || itemid / 10000 == 135);
    }

    public static boolean isTownScroll(int id) {
        return (id >= 2030000 && id < 2040000);
    }

    public static boolean isUpgradeScroll(int id) {
        return (id >= 2040000 && id < 2050000);
    }

    public static boolean isGun(int id) {
        return (id >= 1492000 && id < 1500000);
    }

    public static boolean isUse(int id) {
        return (id >= 2000000 && id < 3000000);
    }

    public static boolean isSummonSack(int id) {
        return (id / 10000 == 210);
    }

    public static boolean isMonsterCard(int id) {
        return (id / 10000 == 238);
    }

    public static boolean isSpecialCard(int id) {
        return (id / 1000 >= 2388);
    }

    public static int getCardShortId(int id) {
        return id % 10000;
    }

    public static boolean isGem(int id) {
        return (id >= 4250000 && id <= 4251402);
    }

    public static boolean isOtherGem(int id) {
        switch (id) {
            case 1032062:
            case 1142156:
            case 1142157:
            case 2040727:
            case 2041058:
            case 4001174:
            case 4001175:
            case 4001176:
            case 4001177:
            case 4001178:
            case 4001179:
            case 4001180:
            case 4001181:
            case 4001182:
            case 4001183:
            case 4001184:
            case 4001185:
            case 4001186:
            case 4031980:
            case 4032312:
            case 4032334:
                return true;
        }
        return false;
    }

    public static boolean isCustomQuest(int id) {
        return false;
    }

    public static long getTaxAmount(long meso) {
        if (meso >= 100000000L) {
            return Math.round(0.06D * meso);
        }
        if (meso >= 25000000L) {
            return Math.round(0.05D * meso);
        }
        if (meso >= 10000000L) {
            return Math.round(0.04D * meso);
        }
        if (meso >= 5000000L) {
            return Math.round(0.03D * meso);
        }
        if (meso >= 1000000L) {
            return Math.round(0.018D * meso);
        }
        if (meso >= 100000L) {
            return Math.round(0.008D * meso);
        }
        return 0L;
    }

    public static int EntrustedStoreTax(long theQuantity) {
        if (theQuantity >= 100000000L) {
            return (int) Math.round(0.03D * theQuantity);
        }
        if (theQuantity >= 25000000L) {
            return (int) Math.round(0.025D * theQuantity);
        }
        if (theQuantity >= 10000000L) {
            return (int) Math.round(0.02D * theQuantity);
        }
        if (theQuantity >= 5000000L) {
            return (int) Math.round(0.015D * theQuantity);
        }
        if (theQuantity >= 1000000L) {
            return (int) Math.round(0.009D * theQuantity);
        }
        if (theQuantity >= 100000L) {
            return (int) Math.round(0.004D * theQuantity);
        }
        return 0;
    }

    public static int getAttackDelay(int id, Skill skill) {
        switch (id) {
            case 3121004:
            case 5201006:
            case 5221004:
            case 13111002:
            case 23121000:
            case 33121009:
            case 35111004:
            case 35121005:
            case 35121013:
                return 40;
            case 4121007:
            case 5221007:
            case 14111005:
                return 99;
            case 0:
                return 570;
        }
        if (skill != null && skill.getSkillType() == 3) {
            return 0;
        }
        if (skill != null && skill.getDelay() > 0 && !isNoDelaySkill(id)) {
            return skill.getDelay();
        }
        return 330;
    }

    public static byte gachaponRareItem(int id) {
        switch (id) {
            case 2040006:
            case 2040007:
            case 2040303:
            case 2040403:
            case 2040506:
            case 2040507:
            case 2040603:
            case 2040709:
            case 2040710:
            case 2040711:
            case 2040806:
            case 2040903:
            case 2041024:
            case 2041025:
            case 2043003:
            case 2043103:
            case 2043203:
            case 2043303:
            case 2043703:
            case 2043803:
            case 2044003:
            case 2044019:
            case 2044103:
            case 2044203:
            case 2044303:
            case 2044403:
            case 2044503:
            case 2044603:
            case 2044703:
            case 2044815:
            case 2044908:
            case 2049000:
            case 2049001:
            case 2049002:
            case 2049100:
            case 2340000:
                return 2;
        }
        return 0;
    }

    public static final int[] goldrewards = new int[]{
        2049400, 1, 2049401, 2, 2049301, 2, 2340000, 1, 2070007, 2,
        2070016, 1, 2330007, 1, 2070018, 1, 1402037, 1, 2290096, 1,
        2290049, 1, 2290041, 1, 2290047, 1, 2290095, 1, 2290017, 1,
        2290075, 1, 2290085, 1, 2290116, 1, 1302059, 3, 2049100, 1,
        1092049, 1, 1102041, 1, 1432018, 3, 1022047, 3, 3010051, 1,
        3010020, 1, 2040914, 1, 1432011, 3, 1442020, 3, 1382035, 3,
        1372010, 3, 1332027, 3, 1302056, 3, 1402005, 3, 1472053, 3,
        1462018, 3, 1452017, 3, 1422013, 3, 1322029, 3, 1412010, 3,
        1472051, 1, 1482013, 1, 1492013, 1, 1382049, 1, 1382050, 1,
        1382051, 1, 1382052, 1, 1382045, 1, 1382047, 1, 1382048, 1,
        1382046, 1, 1372035, 1, 1372036, 1, 1372037, 1, 1372038, 1,
        1372039, 1, 1372040, 1, 1372041, 1, 1372042, 1, 1332032, 8,
        1482025, 7, 4001011, 8, 4001010, 8, 4001009, 8, 2047000, 1,
        2047001, 1, 2047002, 1, 2047100, 1, 2047101, 1, 2047102, 1,
        2047200, 1, 2047201, 1, 2047202, 1, 2047203, 1, 2047204, 1,
        2047205, 1, 2047206, 1, 2047207, 1, 2047208, 1, 2047300, 1,
        2047301, 1, 2047302, 1, 2047303, 1, 2047304, 1, 2047305, 1,
        2047306, 1, 2047307, 1, 2047308, 1, 2047309, 1, 2046004, 1,
        2046005, 1, 2046104, 1, 2046105, 1, 2046208, 1, 2046209, 1,
        2046210, 1, 2046211, 1, 2046212, 1, 1132014, 3, 1132015, 2,
        1132016, 1, 1002801, 2, 1102205, 2, 1332079, 2, 1332080, 2,
        1402048, 2, 1402049, 2, 1402050, 2, 1402051, 2, 1462052, 2,
        1462054, 2, 1462055, 2, 1472074, 2, 1472075, 2, 1332077, 1,
        1382082, 1, 1432063, 1, 1452087, 1, 1462053, 1, 1472072, 1,
        1482048, 1, 1492047, 1, 2030008, 5, 1442018, 3, 2040900, 4,
        2049100, 10, 2000005, 10, 2000004, 10, 4280000, 8, 2430144, 10,
        2290285, 10, 2028061, 10, 2028062, 10, 2530000, 5, 2531000, 5};

    public static final int[] silverrewards = new int[]{
        2049401, 2, 2049301, 2, 3010041, 1, 1002452, 6, 1002455, 6,
        2290084, 1, 2290048, 1, 2290040, 1, 2290046, 1, 2290074, 1,
        2290064, 1, 2290094, 1, 2290022, 1, 2290056, 1, 2290066, 1,
        2290020, 1, 1102082, 1, 1302049, 1, 2340000, 1, 1102041, 1,
        1452019, 2, 4001116, 3, 4001012, 3, 1022060, 2, 2430144, 5,
        2290285, 5, 2028062, 5, 2028061, 5, 2530000, 1, 2531000, 1,
        2041100, 1, 2041101, 1, 2041102, 1, 2041103, 1, 2041104, 1,
        2041105, 1, 2041106, 1, 2041107, 1, 2041108, 1, 2041109, 1,
        2041110, 1, 2041111, 1, 2041112, 1, 2041113, 1, 2041114, 1,
        2041115, 1, 2041116, 1, 2041117, 1, 2041118, 1, 2041119, 1,
        2041300, 1, 2041301, 1, 2041302, 1, 2041303, 1, 2041304, 1,
        2041305, 1, 2041306, 1, 2041307, 1, 2041308, 1, 2041309, 1,
        2041310, 1, 2041311, 1, 2041312, 1, 2041313, 1, 2041314, 1,
        2041315, 1, 2041316, 1, 2041317, 1, 2041318, 1, 2041319, 1,
        2049200, 1, 2049201, 1, 2049202, 1, 2049203, 1, 2049204, 1,
        2049205, 1, 2049206, 1, 2049207, 1, 2049208, 1, 2049209, 1,
        2049210, 1, 2049211, 1, 1432011, 3, 1442020, 3, 1382035, 3,
        1372010, 3, 1332027, 3, 1302056, 3, 1402005, 3, 1472053, 3,
        1462018, 3, 1452017, 3, 1422013, 3, 1322029, 3, 1412010, 3,
        1002587, 3, 1402044, 1, 2101013, 4, 1442046, 1, 1422031, 1,
        1332054, 3, 1012056, 3, 1022047, 3, 3012002, 1, 1442012, 3,
        1442018, 3, 1432010, 3, 1432036, 1, 2000005, 10, 2049100, 10,
        2000004, 10, 4280001, 8};

    public static final int[] peanuts = new int[]{
        2430091, 200, 2430092, 200, 2430093, 200, 2430101, 200, 2430102, 200,
        2430136, 200, 2430149, 200, 2340000, 1, 1152000, 5, 1152001, 5,
        1152004, 5, 1152005, 5, 1152006, 5, 1152007, 5, 1152008, 5,
        1152064, 5, 1152065, 5, 1152066, 5, 1152067, 5, 1152070, 5,
        1152071, 5, 1152072, 5, 1152073, 5, 3010019, 2, 1001060, 10,
        1002391, 10, 1102004, 10, 1050039, 10, 1102040, 10, 1102041, 10,
        1102042, 10, 1102043, 10, 1082145, 5, 1082146, 5, 1082147, 5,
        1082148, 5, 1082149, 5, 1082150, 5, 2043704, 10, 2040904, 10,
        2040409, 10, 2040307, 10, 2041030, 10, 2040015, 10, 2040109, 10,
        2041035, 10, 2041036, 10, 2040009, 10, 2040511, 10, 2040408, 10,
        2043804, 10, 2044105, 10, 2044903, 10, 2044804, 10, 2043009, 10,
        2043305, 10, 2040610, 10, 2040716, 10, 2041037, 10, 2043005, 10,
        2041032, 10, 2040305, 10, 2040211, 5, 2040212, 5, 1022097, 10,
        2049000, 10, 2049001, 10, 2049002, 10, 2049003, 10, 1012058, 5,
        1012059, 5, 1012060, 5, 1012061, 5, 1332100, 10, 1382058, 10,
        1402073, 10, 1432066, 10, 1442090, 10, 1452058, 10, 1462076, 10,
        1472069, 10, 1482051, 10, 1492024, 10, 1342009, 10, 2049400, 1,
        2049401, 2, 2049301, 2, 2049100, 10, 2430144, 10, 2290285, 10,
        2028062, 10, 2028061, 10, 2530000, 5, 2531000, 5, 1032080, 5,
        1032081, 4, 1032082, 3, 1032083, 2, 1032084, 1, 1112435, 5,
        1112436, 4, 1112437, 3, 1112438, 2, 1112439, 1, 1122081, 5,
        1122082, 4, 1122083, 3, 1122084, 2, 1122085, 1, 1132036, 5,
        1132037, 4, 1132038, 3, 1132039, 2, 1132040, 1, 1092070, 5,
        1092071, 4, 1092072, 3, 1092073, 2, 1092074, 1, 1092075, 5,
        1092076, 4, 1092077, 3, 1092078, 2, 1092079, 1, 1092080, 5,
        1092081, 4, 1092082, 3, 1092083, 2, 1092084, 1, 1092087, 1,
        1092088, 1, 1092089, 1, 1302143, 5, 1302144, 4, 1302145, 3,
        1302146, 2, 1302147, 1, 1312058, 5, 1312059, 4, 1312060, 3,
        1312061, 2, 1312062, 1, 1322086, 5, 1322087, 4, 1322088, 3,
        1322089, 2, 1322090, 1, 1332116, 5, 1332117, 4, 1332118, 3,
        1332119, 2, 1332120, 1, 1332121, 5, 1332122, 4, 1332123, 3,
        1332124, 2, 1332125, 1, 1342029, 5, 1342030, 4, 1342031, 3,
        1342032, 2, 1342033, 1, 1372074, 5, 1372075, 4, 1372076, 3,
        1372077, 2, 1372078, 1, 1382095, 5, 1382096, 4, 1382097, 3,
        1382098, 2, 1392099, 1, 1402086, 5, 1402087, 4, 1402088, 3,
        1402089, 2, 1402090, 1, 1412058, 5, 1412059, 4, 1412060, 3,
        1412061, 2, 1412062, 1, 1422059, 5, 1422060, 4, 1422061, 3,
        1422062, 2, 1422063, 1, 1432077, 5, 1432078, 4, 1432079, 3,
        1432080, 2, 1432081, 1, 1442107, 5, 1442108, 4, 1442109, 3,
        1442110, 2, 1442111, 1, 1452102, 5, 1452103, 4, 1452104, 3,
        1452105, 2, 1452106, 1, 1462087, 5, 1462088, 4, 1462089, 3,
        1462090, 2, 1462091, 1, 1472113, 5, 1472114, 4, 1472115, 3,
        1472116, 2, 1472117, 1, 1482075, 5, 1482076, 4, 1482077, 3,
        1482078, 2, 1482079, 1, 1492075, 5, 1492076, 4, 1492077, 3,
        1492078, 2, 1492079, 1, 1132012, 2, 1132013, 1, 1942002, 2,
        1952002, 2, 1962002, 2, 1972002, 2, 1612004, 2, 1622004, 2,
        1632004, 2, 1642004, 2, 1652004, 2, 2047000, 1, 2047001, 1,
        2047002, 1, 2047100, 1, 2047101, 1, 2047102, 1, 2047200, 1,
        2047201, 1, 2047202, 1, 2047203, 1, 2047204, 1, 2047205, 1,
        2047206, 1, 2047207, 1, 2047208, 1, 2047300, 1, 2047301, 1,
        2047302, 1, 2047303, 1, 2047304, 1, 2047305, 1, 2047306, 1,
        2047307, 1, 2047308, 1, 2047309, 1, 2046004, 1, 2046005, 1,
        2046104, 1, 2046105, 1, 2046208, 1, 2046209, 1, 2046210, 1,
        2046211, 1, 2046212, 1, 2049200, 1, 2049201, 1, 2049202, 1,
        2049203, 1, 2049204, 1, 2049205, 1, 2049206, 1, 2049207, 1,
        2049208, 1, 2049209, 1, 2049210, 1, 2049211, 1, 1372035, 1,
        1372036, 1, 1372037, 1, 1372038, 1, 1382045, 1, 1382046, 1,
        1382047, 1, 1382048, 1, 1382049, 1, 1382050, 1, 1382051, 1,
        1382052, 1, 1372039, 1, 1372040, 1, 1372041, 1, 1372042, 1,
        2070016, 1, 2070007, 2, 2330007, 1, 2070018, 1, 2330008, 1,
        2070023, 1, 2070024, 1, 2028062, 5, 2028061, 5};

    public static int[] eventCommonReward = new int[]{
        0, 10, 1, 10, 4, 5, 5060004, 25, 4170024, 25,
        4280000, 5, 4280001, 6, 5490000, 5, 5490001, 6};

    public static int[] theSeedBoxReward = new int[]{
        0, 1, 1, 1, 4310034, 1, 4310014, 1, 4310016, 1,
        4001208, 1, 4001547, 1, 4001548, 1, 4001549, 1, 4001550, 1,
        4001551, 1};

    public static int[] eventUncommonReward = new int[]{
        1, 4, 2, 8, 3, 8, 2022179, 5, 5062000, 20,
        2430082, 20, 2430092, 20, 2022459, 2, 2022460, 1, 2022462, 1,
        2430103, 2, 2430117, 2, 2430118, 2, 2430201, 4, 2430228, 4,
        2430229, 4, 2430283, 4, 2430136, 4, 2430476, 4, 2430511, 4,
        2430206, 4, 2430199, 1, 1032062, 5, 5220000, 28, 2022459, 5,
        2022460, 5, 2022461, 5, 2022462, 5, 2022463, 5, 5050000, 2,
        4080100, 10, 4080000, 10, 2049100, 10, 2430144, 10, 2290285, 10,
        2028062, 10, 2028061, 10, 2530000, 5, 2531000, 5, 2041100, 1,
        2041101, 1, 2041102, 1, 2041103, 1, 2041104, 1, 2041105, 1,
        2041106, 1, 2041107, 1, 2041108, 1, 2041109, 1, 2041110, 1,
        2041111, 1, 2041112, 1, 2041113, 1, 2041114, 1, 2041115, 1,
        2041116, 1, 2041117, 1, 2041118, 1, 2041119, 1, 2041300, 1,
        2041301, 1, 2041302, 1, 2041303, 1, 2041304, 1, 2041305, 1,
        2041306, 1, 2041307, 1, 2041308, 1, 2041309, 1, 2041310, 1,
        2041311, 1, 2041312, 1, 2041313, 1, 2041314, 1, 2041315, 1,
        2041316, 1, 2041317, 1, 2041318, 1, 2041319, 1, 2049200, 1,
        2049201, 1, 2049202, 1, 2049203, 1, 2049204, 1, 2049205, 1,
        2049206, 1, 2049207, 1, 2049208, 1, 2049209, 1, 2049210, 1,
        2049211, 1};

    public static int[] eventRareReward = new int[]{
        2049100, 5, 2430144, 5, 2290285, 5, 2028062, 5, 2028061, 5,
        2530000, 2, 2531000, 2, 2049116, 1, 2049401, 10, 2049301, 20,
        2049400, 3, 2340000, 1, 3010130, 5, 3010131, 5, 3010132, 5,
        3010133, 5, 3010136, 5, 3010116, 5, 3010117, 5, 3010118, 5,
        1112405, 1, 1112445, 1, 1022097, 1, 2040211, 1, 2040212, 1,
        2049000, 2, 2049001, 2, 2049002, 2, 2049003, 2, 1012058, 2,
        1012059, 2, 1012060, 2, 1012061, 2, 2022460, 4, 2022461, 3,
        2022462, 4, 2022463, 3, 2040041, 1, 2040042, 1, 2040334, 1,
        2040430, 1, 2040538, 1, 2040539, 1, 2040630, 1, 2040740, 1,
        2040741, 1, 2040742, 1, 2040829, 1, 2040830, 1, 2040936, 1,
        2041066, 1, 2041067, 1, 2043023, 1, 2043117, 1, 2043217, 1,
        2043312, 1, 2043712, 1, 2043812, 1, 2044025, 1, 2044117, 1,
        2044217, 1, 2044317, 1, 2044417, 1, 2044512, 1, 2044612, 1,
        2044712, 1, 2046000, 1, 2046001, 1, 2046004, 1, 2046005, 1,
        2046100, 1, 2046101, 1, 2046104, 1, 2046105, 1, 2046200, 1,
        2046201, 1, 2046202, 1, 2046203, 1, 2046208, 1, 2046209, 1,
        2046210, 1, 2046211, 1, 2046212, 1, 2046300, 1, 2046301, 1,
        2046302, 1, 2046303, 1, 2047000, 1, 2047001, 1, 2047002, 1,
        2047100, 1, 2047101, 1, 2047102, 1, 2047200, 1, 2047201, 1,
        2047202, 1, 2047203, 1, 2047204, 1, 2047205, 1, 2047206, 1,
        2047207, 1, 2047208, 1, 2047300, 1, 2047301, 1, 2047302, 1,
        2047303, 1, 2047304, 1, 2047305, 1, 2047306, 1, 2047307, 1,
        2047308, 1, 2047309, 1, 1112427, 5, 1112428, 5, 1112429, 5,
        1012240, 10, 1022117, 10, 1032095, 10, 1112659, 10, 2070007, 10,
        2330007, 5, 2070016, 5, 2070018, 5, 1152038, 1, 1152039, 1,
        1152040, 1, 1152041, 1, 1122090, 1, 1122094, 1, 1122098, 1,
        1122102, 1, 1012213, 1, 1012219, 1, 1012225, 1, 1012231, 1,
        1012237, 1, 2070023, 5, 2070024, 5, 2330008, 5, 2003516, 5,
        2003517, 1, 1132052, 1, 1132062, 1, 1132072, 1, 1132082, 1,
        1112585, 1, 1072502, 1, 1072503, 1, 1072504, 1, 1072505, 1,
        1072506, 1, 1052333, 1, 1052334, 1, 1052335, 1, 1052336, 1,
        1052337, 1, 1082305, 1, 1082306, 1, 1082307, 1, 1082308, 1,
        1082309, 1, 1003197, 1, 1003198, 1, 1003199, 1, 1003200, 1,
        1003201, 1, 1662000, 1, 1662001, 1, 1672000, 1, 1672001, 1,
        1672002, 1, 1112583, 1, 1032092, 1, 1132084, 1, 2430290, 1,
        2430292, 1, 2430294, 1, 2430296, 1, 2430298, 1, 2430300, 1,
        2430302, 1, 2430304, 1, 2430306, 1, 2430308, 1, 2430310, 1,
        2430312, 1, 2430314, 1, 2430316, 1, 2430318, 1, 2430320, 1,
        2430322, 1, 2430324, 1, 2430326, 1, 2430328, 1, 2430330, 1,
        2430332, 1, 2430334, 1, 2430336, 1, 2430338, 1, 2430340, 1,
        2430342, 1, 2430344, 1, 2430347, 1, 2430349, 1, 2430351, 1,
        2430353, 1, 2430355, 1, 2430357, 1, 2430359, 1, 2430361, 1,
        2430392, 1, 2430512, 1, 2430536, 1, 2430477, 1, 2430146, 1,
        2430148, 1, 2430137, 1};

    public static int[] eventSuperReward = new int[]{
        2022121, 10, 4031307, 50, 3010127, 10, 3010128, 10, 3010137, 10,
        3010157, 10, 2049300, 10, 2040758, 10, 1442057, 10, 2049402, 10,
        2049304, 1, 2049305, 1, 2040759, 7, 2040760, 5, 2040125, 10,
        2040126, 10, 1012191, 5, 1112514, 1, 1112531, 1, 1112629, 1,
        1112646, 1, 1112515, 1, 1112532, 1, 1112630, 1, 1112647, 1,
        1112516, 1, 1112533, 1, 1112631, 1, 1112648, 1, 2040045, 10,
        2040046, 10, 2040333, 10, 2040429, 10, 2040542, 10, 2040543, 10,
        2040629, 10, 2040755, 10, 2040756, 10, 2040757, 10, 2040833, 10,
        2040834, 10, 2041068, 10, 2041069, 10, 2043022, 12, 2043120, 12,
        2043220, 12, 2043313, 12, 2043713, 12, 2043813, 12, 2044028, 12,
        2044120, 12, 2044220, 12, 2044320, 12, 2044520, 12, 2044513, 12,
        2044613, 12, 2044713, 12, 2044817, 12, 2044910, 12, 2046002, 5,
        2046003, 5, 2046102, 5, 2046103, 5, 2046204, 10, 2046205, 10,
        2046206, 10, 2046207, 10, 2046304, 10, 2046305, 10, 2046306, 10,
        2046307, 10, 2040006, 2, 2040007, 2, 2040303, 2, 2040403, 2,
        2040506, 2, 2040507, 2, 2040603, 2, 2040709, 2, 2040710, 2,
        2040711, 2, 2040806, 2, 2040903, 2, 2040913, 2, 2041024, 2,
        2041025, 2, 2044815, 2, 2044908, 2, 1152046, 1, 1152047, 1,
        1152048, 1, 1152049, 1, 1122091, 1, 1122095, 1, 1122099, 1,
        1122103, 1, 1012214, 1, 1012220, 1, 1012226, 1, 1012232, 1,
        1012238, 1, 1032088, 1, 1032089, 1, 1032090, 1, 1032091, 1,
        1132053, 1, 1132063, 1, 1132073, 1, 1132083, 1, 1112586, 1,
        1112593, 1, 1112597, 1, 1662002, 1, 1662003, 1, 1672003, 1,
        1672004, 1, 1672005, 1, 1092088, 1, 1092089, 1, 1092087, 1,
        1102275, 1, 1102276, 1, 1102277, 1, 1102278, 1, 1102279, 1,
        1102280, 1, 1102281, 1, 1102282, 1, 1102283, 1, 1102284, 1,
        1082295, 1, 1082296, 1, 1082297, 1, 1082298, 1, 1082299, 1,
        1082300, 1, 1082301, 1, 1082302, 1, 1082303, 1, 1082304, 1,
        1072485, 1, 1072486, 1, 1072487, 1, 1072488, 1, 1072489, 1,
        1072490, 1, 1072491, 1, 1072492, 1, 1072493, 1, 1072494, 1,
        1052314, 1, 1052315, 1, 1052316, 1, 1052317, 1, 1052318, 1,
        1052319, 1, 1052329, 1, 1052321, 1, 1052322, 1, 1052323, 1,
        1003172, 1, 1003173, 1, 1003174, 1, 1003175, 1, 1003176, 1,
        1003177, 1, 1003178, 1, 1003179, 1, 1003180, 1, 1003181, 1,
        1302152, 1, 1302153, 1, 1312065, 1, 1312066, 1, 1322096, 1,
        1322097, 1, 1332130, 1, 1332131, 1, 1342035, 1, 1342036, 1,
        1372084, 1, 1372085, 1, 1382104, 1, 1382105, 1, 1402095, 1,
        1402096, 1, 1412065, 1, 1412066, 1, 1422066, 1, 1422067, 1,
        1432086, 1, 1432087, 1, 1442116, 1, 1442117, 1, 1452111, 1,
        1452112, 1, 1462099, 1, 1462100, 1, 1472122, 1, 1472123, 1,
        1482084, 1, 1482085, 1, 1492085, 1, 1492086, 1, 1532017, 1,
        1532018, 1, 2430291, 1, 2430293, 1, 2430295, 1, 2430297, 1,
        2430299, 1, 2430301, 1, 2430303, 1, 2430305, 1, 2430307, 1,
        2430309, 1, 2430311, 1, 2430313, 1, 2430315, 1, 2430317, 1,
        2430319, 1, 2430321, 1, 2430323, 1, 2430325, 1, 2430327, 1,
        2430329, 1, 2430331, 1, 2430333, 1, 2430335, 1, 2430337, 1,
        2430339, 1, 2430341, 1, 2430343, 1, 2430345, 1, 2430348, 1,
        2430350, 1, 2430352, 1, 2430354, 1, 2430356, 1, 2430358, 1,
        2430360, 1, 2430362, 1, 1012239, 1, 1122104, 1, 1112584, 1,
        1032093, 1, 1132085, 1};

    public static int[] tenPercent = new int[]{
        2040002, 2040005, 2040026, 2040031, 2040100, 2040105, 2040200, 2040205, 2040302, 2040310,
        2040318, 2040323, 2040328, 2040329, 2040330, 2040331, 2040402, 2040412, 2040419, 2040422,
        2040427, 2040502, 2040505, 2040514, 2040517, 2040534, 2040602, 2040612, 2040619, 2040622,
        2040627, 2040702, 2040705, 2040708, 2040727, 2040802, 2040805, 2040816, 2040825, 2040902,
        2040915, 2040920, 2040925, 2040928, 2040933, 2041002, 2041005, 2041008, 2041011, 2041014,
        2041017, 2041020, 2041023, 2041058, 2041102, 2041105, 2041108, 2041111, 2041302, 2041305,
        2041308, 2041311, 2043002, 2043008, 2043019, 2043102, 2043114, 2043202, 2043214, 2043302,
        2043402, 2043702, 2043802, 2044002, 2044014, 2044015, 2044102, 2044114, 2044202, 2044214,
        2044302, 2044314, 2044402, 2044414, 2044502, 2044602, 2044702, 2044802, 2044809, 2044902,
        2045302, 2048002, 2048005};

    public static int[] fishingReward = new int[]{
        0, 100, 1, 100, 2022179, 1, 1302021, 5, 1072238, 1,
        1072239, 1, 2049100, 2, 2430144, 1, 2290285, 1, 2028062, 1,
        2028061, 1, 2049301, 1, 2049401, 1, 1302000, 3, 1442011, 1,
        4000517, 8, 4000518, 10, 4031627, 2, 4031628, 1, 4031630, 1,
        4031631, 1, 4031632, 1, 4031633, 2, 4031634, 1, 4031635, 1,
        4031636, 1, 4031637, 2, 4031638, 2, 4031639, 1, 4031640, 1,
        4031641, 2, 4031642, 2, 4031643, 1, 4031644, 1, 4031645, 2,
        4031646, 2, 4031647, 1, 4031648, 1, 4001187, 20, 4001188, 20,
        4001189, 20, 4031629, 1};

    public static int[] randomReward = new int[]{
        2000005, 5, 2000005, 10, 2000004, 5, 2000004, 10, 2001554, 5,
        2001554, 10, 2001555, 3, 2001555, 5, 2001556, 5, 2001556, 10,
        2002000, 3, 2002000, 5, 2002001, 3, 2002001, 5, 2002002, 3,
        2002002, 5, 2002003, 3, 2002003, 5, 2002004, 3, 2002004, 5,
        2002005, 3, 2002005, 5};

    public static boolean isReverseItem(int itemId) {
        switch (itemId) {
            case 1002790:
            case 1002791:
            case 1002792:
            case 1002793:
            case 1002794:
            case 1052160:
            case 1052161:
            case 1052162:
            case 1052163:
            case 1052164:
            case 1072361:
            case 1072362:
            case 1072363:
            case 1072364:
            case 1072365:
            case 1082239:
            case 1082240:
            case 1082241:
            case 1082242:
            case 1082243:
            case 1302086:
            case 1312038:
            case 1322061:
            case 1332075:
            case 1332076:
            case 1342012:
            case 1372045:
            case 1382059:
            case 1402047:
            case 1412034:
            case 1422038:
            case 1432049:
            case 1442067:
            case 1452059:
            case 1462051:
            case 1472071:
            case 1482024:
            case 1492025:
            case 1522017:
            case 1532016:
            case 1942002:
            case 1952002:
            case 1962002:
            case 1972002:
                return true;
        }
        return false;
    }

    public static boolean isTimelessItem(int itemId) {
        switch (itemId) {
            case 1002776:
            case 1002777:
            case 1002778:
            case 1002779:
            case 1002780:
            case 1032031:
            case 1052155:
            case 1052156:
            case 1052157:
            case 1052158:
            case 1052159:
            case 1072355:
            case 1072356:
            case 1072357:
            case 1072358:
            case 1072359:
            case 1082234:
            case 1082235:
            case 1082236:
            case 1082237:
            case 1082238:
            case 1092057:
            case 1092058:
            case 1092059:
            case 1102172:
            case 1122011:
            case 1122012:
            case 1302081:
            case 1312037:
            case 1322060:
            case 1332073:
            case 1332074:
            case 1342011:
            case 1372044:
            case 1382057:
            case 1402046:
            case 1412033:
            case 1422037:
            case 1432047:
            case 1442063:
            case 1452057:
            case 1462050:
            case 1472068:
            case 1482023:
            case 1492023:
            case 1522016:
            case 1532015:
                return true;
        }
        return false;
    }

    public static boolean isRing(int itemId) {
        return (itemId >= 1112000 && itemId < 1120000);
    }

    public static boolean isEffectRing(int itemid) {
        return (isFriendshipRing(itemid) || isCrushRing(itemid) || isMarriageRing(itemid));
    }

    public static boolean isMarriageRing(int itemId) {
        switch (itemId) {
            case 1112300:
            case 1112301:
            case 1112302:
            case 1112303:
            case 1112304:
            case 1112305:
            case 1112306:
            case 1112307:
            case 1112308:
            case 1112309:
            case 1112310:
            case 1112311:
            case 1112744:
            case 4210000:
            case 4210001:
            case 4210002:
            case 4210003:
            case 4210004:
            case 4210005:
            case 4210006:
            case 4210007:
            case 4210008:
            case 4210009:
            case 4210010:
            case 4210011:
                return true;
        }
        return false;
    }

    public static boolean isFriendshipRing(int itemId) {
        switch (itemId) {
            case 1049000:
            case 1112800:
            case 1112801:
            case 1112802:
            case 1112810:
            case 1112811:
            case 1112812:
            case 1112816:
            case 1112817:
                return true;
        }
        return false;
    }

    public static boolean isCrushRing(int itemId) {
        switch (itemId) {
            case 1048000:
            case 1048001:
            case 1048002:
            case 1112001:
            case 1112002:
            case 1112003:
            case 1112005:
            case 1112006:
            case 1112007:
            case 1112012:
            case 1112015:
                return true;
        }
        return false;
    }

    public static int[] Equipments_Bonus = new int[]{1122017};

    public static int Equipment_Bonus_EXP(int itemid) {
        switch (itemid) {
            case 1122017:
                return 10;
        }
        return 0;
    }

    public static int[] blockedMaps = new int[]{180000001, 180000002, 109050000, 280030000, 240060200, 280090000, 280030001, 240060201, 950101100, 950101010};

    public static int getExpForLevel(int i, int itemId) {
        if (isReverseItem(itemId)) {
            return getReverseRequiredEXP(i);
        }
        if (getMaxLevel(itemId) > 0) {
            return getTimelessRequiredEXP(i);
        }
        return 0;
    }

    public static int getMaxLevel(int itemId) {
        if (itemId >= 1113098 && itemId <= 1113128) {
            return 4;
        }
        Map<Integer, Map<String, Integer>> inc = MapleItemInformationProvider.getInstance().getEquipIncrements(itemId);
        return (inc != null) ? inc.size() : 0;
    }

    public static int getStatChance() {
        return 25;
    }

    public static int getXForStat(final MonsterStatus stat) {
        switch (stat) {
            case MS_Darkness: {
                return -70;
            }
            case MS_Speed: {
                return -50;
            }
            default: {
                return 0;
            }
        }
    }

    public static int getSkillForStat(final MonsterStatus stat) {
        switch (stat) {
            case MS_Darkness: {
                return 1111003;
            }
            case MS_Speed: {
                return 3121007;
            }
            default: {
                return 0;
            }
        }
    }

    public static final int[] normalDrops = new int[]{
        4001009, 4001010, 4001011, 4001012, 4001013, 4001014, 4001021, 4001038, 4001039, 4001040,
        4001041, 4001042, 4001043, 4001038, 4001039, 4001040, 4001041, 4001042, 4001043, 4001038,
        4001039, 4001040, 4001041, 4001042, 4001043, 4000164, 2000000, 2000003, 2000004, 2000005,
        4000019, 4000000, 4000016, 4000006, 2100121, 4000029, 4000064, 5110000, 4000306, 4032181,
        4006001, 4006000, 2050004, 3994102, 3994103, 3994104, 3994105, 2430007, 4000164, 2000000,
        2000003, 2000004, 2000005, 4000019, 4000000, 4000016, 4000006, 2100121, 4000029, 4000064,
        5110000, 4000306, 4032181, 4006001, 4006000, 2050004, 3994102, 3994103, 3994104, 3994105,
        2430007, 4000164, 2000000, 2000003, 2000004, 2000005, 4000019, 4000000, 4000016, 4000006,
        2100121, 4000029, 4000064, 5110000, 4000306, 4032181, 4006001, 4006000, 2050004, 3994102,
        3994103, 3994104, 3994105, 2430007};

    public static final int[] rareDrops = new int[]{
        2022179, 2049100, 2049100, 2430144, 2028062, 2028061, 2290285, 2049301, 2049401, 2022326,
        2022193, 2049000, 2049001, 2049002};

    public static final int[] superDrops = new int[]{
        2040804, 2049400, 2028062, 2028061, 2430144, 2430144, 2430144, 2430144, 2290285, 2049100,
        2049100, 2049100, 2049100};

    public static int getSkillBook(int job) {
        if (job >= 2210 && job <= 2218) {
            switch (job) {
                case 2211:
                    return 1;
                case 2214:
                    return 2;
                case 2217:
                    return 3;
            }
            return 0;
        }
        if (job == 434) {
            return 5;
        }
        switch (job % 100) {
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
                return 1;
            case 11:
            case 21:
            case 31:
            case 41:
            case 51:
                return 2;
            case 12:
            case 22:
            case 32:
            case 42:
            case 52:
                return 3;
            case 33:
                return 4;
        }
        return 0;
    }

    public static int getSkillBook(int job, int skill) {
        if (job >= 2210 && job <= 2218) {
            return job - 2209;
        }
        if (isZero(job)) {
            if (skill > 0) {
                int type = skill % 1000 / 100;
                return (type == 1) ? 1 : 0;
            }
            return 0;
        }
        switch (job) {
            case 110:
            case 120:
            case 130:
            case 210:
            case 220:
            case 230:
            case 310:
            case 320:
            case 410:
            case 420:
            case 510:
            case 520:
            case 570:
            case 1110:
            case 1310:
            case 1510:
            case 2310:
            case 2410:
            case 2510:
            case 2710:
            case 3110:
            case 3120:
            case 3210:
            case 3310:
            case 3510:
            case 3610:
            case 4110:
            case 4210:
            case 5110:
            case 6110:
            case 6510:
                return 1;
            case 111:
            case 121:
            case 131:
            case 211:
            case 221:
            case 231:
            case 311:
            case 321:
            case 411:
            case 421:
            case 511:
            case 521:
            case 571:
            case 1111:
            case 1311:
            case 1511:
            case 2311:
            case 2411:
            case 2511:
            case 2711:
            case 3111:
            case 3121:
            case 3211:
            case 3311:
            case 3511:
            case 3611:
            case 4111:
            case 4211:
            case 5111:
            case 6111:
            case 6511:
                return 2;
            case 112:
            case 122:
            case 132:
            case 212:
            case 222:
            case 232:
            case 312:
            case 322:
            case 412:
            case 422:
            case 512:
            case 522:
            case 572:
            case 1112:
            case 1312:
            case 1512:
            case 2312:
            case 2412:
            case 2512:
            case 2712:
            case 3112:
            case 3122:
            case 3212:
            case 3312:
            case 3512:
            case 3612:
            case 4112:
            case 4212:
            case 5112:
            case 6112:
            case 6512:
                return 3;
            case 508:
                return 0;
        }
        if (isSeparatedSp((short) job)) {
            if (job % 10 > 4) {
                return 0;
            }
            return job % 10;
        }
        return 0;
    }

    public static int getSkillBook(int job, int level, int skill) {
        if (job >= 2210 && job <= 2218) {
            return job - 2209;
        }
        if (isSeparatedSp((short) job)) {
            return (level <= 30) ? 0 : ((level >= 31 && level <= 60) ? 1 : ((level >= 61 && level <= 100) ? 2 : ((level >= 100) ? 3 : 0)));
        }
        return 0;
    }

    public static int getSkillBookForSkill(int skillid) {
        return getSkillBook(skillid / 10000, skillid);
    }

    public static int getMountItemEx(int itemid) {
        switch (itemid) {
            case 2430932:
                return 80001183;
            case 2430934:
                return 80001190;
            case 2430935:
                return 80001191;
            case 2430936:
                return 80001187;
            case 2430937:
                return 80001193;
            case 2430938:
                return 80001194;
            case 2430939:
                return 80001195;
            case 2430948:
                return 80001190;
            case 2430968:
                return 80001196;
            case 2431073:
                return 80001199;
            case 2431135:
                return 80001220;
            case 2431136:
                return 80001221;
            case 2431137:
                return 80001222;
            case 2431267:
            case 2431268:
                return 80001228;
            case 2341422:
            case 2431353:
            case 2433460:
                return 80001237;
            case 2431362:
                return 80001240;
            case 2431415:
                return 80001241;
            case 2431473:
                return 80001257;
            case 2431474:
                return 80001258;
            case 2431745:
                return 80001277;
            case 2431797:
                return 80001300;
            case 2431799:
                return 80001302;
            case 2431898:
                return 80001324;
            case 2431915:
                return 80001327;
            case 2431949:
                return 80001336;
            case 2432030:
                return 80001347;
            case 2432031:
                return 80001348;
            case 2432078:
                return 80001353;
            case 2432085:
                return 80001356;
            case 2432149:
                return 80001398;
            case 2432151:
                return 80001400;
            case 2432216:
                return 80001412;
            case 2432218:
                return 80001413;
            case 2432291:
                return 80001420;
            case 2432293:
                return 80001421;
            case 2432295:
                return 80001423;
            case 2432309:
                return 80001404;
            case 2432347:
                return 80001440;
            case 2432348:
                return 80001441;
            case 2432349:
                return 80001442;
            case 2432351:
                return 80001444;
            case 2432359:
                return 80001445;
            case 2432361:
                return 80001447;
            case 2432431:
                return 80001480;
            case 2432433:
                return 80001482;
            case 2432449:
                return 80001484;
            case 2432498:
                return 80001508;
            case 2432500:
                return 80001510;
            case 2432552:
                return 80001492;
            case 2432580:
                return 80001503;
            case 2432582:
            case 2631898:
                return 80001505;
            case 2432645:
                return 80001532;
            case 2432653:
            case 2631897:
                return 80001533;
            case 2432733:
                return 80001552;
            case 2432735:
                return 80001550;
            case 2432751:
                return 80001554;
            case 2432806:
                return 80001557;
            case 2433051:
                return 80001582;
            case 2433053:
                return 80001584;
            case 2433272:
                return 80001617;
            case 2433274:
                return 80001620;
            case 2433276:
                return 80001621;
            case 2433345:
                return 80001623;
            case 2433347:
                return 80001625;
            case 2433349:
                return 80001627;
            case 2433499:
                return 80001671;
            case 2433501:
                return 80001673;
            case 2433718:
                return 80001019;
            case 2433735:
                return 80001709;
            case 2433736:
                return 80001708;
            case 2433809:
                return 80001783;
            case 2433946:
                return 80001764;
            case 2433948:
                return 80001766;
            case 2433992:
                return 80001769;
            case 2434077:
                return 80001776;
            case 2434079:
                return 80001778;
            case 2434275:
                return 80001784;
            case 2434277:
            case 2632079:
                return 80001786;
            case 2434377:
                return 80001792;
            case 2434379:
                return 80001790;
            case 2434477:
                return 80001300;
            case 2434515:
            case 2631895:
                return 80001812;
            case 2434517:
                return 80001813;
            case 2434649:
            case 2631896:
                return 80001918;
            case 2434735:
                return 80001921;
            case 2434737:
                return 80001924;
            case 2434697:
                return 80001942;
            case 2435089:
                return 80001956;
            case 2435091:
                return 80001958;
            case 2435112:
                return 80001953;
            case 2435113:
                return 80001954;
            case 2435203:
            case 2632078:
                return 80001975;
            case 2435205:
                return 80001977;
            case 2435296:
                return 80001980;
            case 2435298:
                return 80001982;
            case 2435441:
                return 80001989;
            case 2435476:
                return 80001991;
            case 2435517:
                return 80001993;
            case 2435720:
                return 80001996;
            case 2435722:
                return 80001997;
            case 2435842:
                return 80002219;
            case 2435843:
                return 80002220;
            case 2435844:
                return 80002221;
            case 2435845:
                return 80002222;
            case 2435965:
                return 80002223;
            case 2435967:
                return 80002225;
            case 2435986:
                return 80002202;
            case 2436030:
                return 80002204;
            case 2436031:
                return 80002229;
            case 2436079:
                return 80001246;
            case 2436080:
                return 80002233;
            case 2436081:
                return 80002234;
            case 2436183:
                return 80002236;
            case 2436185:
                return 80002238;
            case 2436292:
                return 80002240;
            case 2436294:
                return 80002242;
            case 2436405:
                return 80002248;
            case 2436407:
                return 80002250;
            case 2436523:
                return 80002270;
            case 2436524:
                return 80002259;
            case 2436525:
                return 80002258;
            case 2436526:
                return 80002257;
            case 2436597:
                return 80002260;
            case 2436599:
                return 80002262;
            case 2436714:
                return 80002270;
            case 2436715:
                return 80002271;
            case 2436716:
                return 80002272;
            case 2436728:
                return 80002278;
            case 2436730:
                return 80002276;
            case 2436778:
                return 80002287;
            case 2436780:
                return 80002289;
            case 2436837:
                return 80002294;
            case 2436839:
                return 80002296;
            case 2436957:
                return 80002302;
            case 2437026:
                return 80002304;
            case 2437040:
            case 2632162:
                return 80002305;
            case 2437042:
                return 80002307;
            case 2437123:
                return 80002313;
            case 2437125:
                return 80002316;
            case 2437240:
                return 80002318;
            case 2437259:
                return 80002319;
            case 2437261:
                return 80002321;
            case 2437497:
                return 80002335;
            case 2437623:
                return 80002345;
            case 2437625:
                return 80002348;
            case 2437719:
                return 80002358;
            case 2437721:
                return 80002356;
            case 2437737:
                return 80002354;
            case 2437738:
                return 80002361;
            case 2437794:
                return 80002355;
            case 2437809:
                return 80002368;
            case 2437923:
            case 2632159:
                return 80002369;
            case 2438136:
                return 80002382;
            case 2438137:
                return 80002383;
            case 2438138:
                return 80002384;
            case 2438139:
                return 80002385;
            case 2438340:
                return 80002375;
            case 2438373:
                return 80002392;
            case 2438380:
                return 80002400;
            case 2438382:
            case 2632160:
                return 80002402;
            case 2438408:
                return 80002417;
            case 2438409:
                return 80002418;
            case 2438486:
                return 80002429;
            case 2438488:
                return 80002432;
            case 2438493:
                return 80002427;
            case 2438494:
                return 80002425;
            case 2438638:
                return 80002433;
            case 2438640:
                return 80002436;
            case 2438657:
            case 2632161:
                return 80002437;
            case 2438715:
                return 80002439;
            case 2438743:
                return 80002441;
            case 2438745:
                return 80002443;
            case 2438882:
                return 80002446;
            case 2438886:
                return 80002447;
            case 2439034:
                return 80002448;
            case 2439036:
                return 80002450;
            case 2439127:
                return 80002454;
            case 2439144:
                return 80002424;
            case 2439266:
                return 80002545;
            case 2439329:
                return 80002572;
            case 2439331:
                return 80002573;
            case 2439443:
                return 80002585;
            case 2439484:
                return 80002628;
            case 2439486:
                return 80002630;
            case 2439666:
                return 80002594;
            case 2439667:
                return 80002595;
            case 2439675:
                return 80002648;
            case 2439677:
                return 80002650;
            case 2439694:
                return 80002439;
            case 2439808:
                return 80002655;
            case 2439909:
                return 80002660;
            case 2439911:
                return 80002661;
            case 2439913:
                return 80002663;
            case 2439915:
                return 80002665;
            case 2439933:
            case 2632842:
                return 80002667;
            case 2630240:
                return 80002698;
            case 2630261:
                return 80002699;
            case 2630279:
                return 80002702;
            case 2630386:
                return 80002712;
            case 2630387:
                return 80002713;
            case 2630448:
                return 80002714;
            case 2630451:
                return 80002716;
            case 2630476:
                return 80002735;
            case 2630488:
                return 80002738;
            case 2630563:
            case 2632301:
                return 80002740;
            case 2630570:
                return 80002752;
            case 2630573:
                return 80002754;
            case 2630575:
                return 80002756;
            case 2630763:
                return 80002742;
            case 2630764:
                return 80002743;
            case 2630765:
                return 80002744;
            case 2630913:
                return 80002844;
            case 2630917:
                return 80002831;
            case 2630918:
                return 80002845;
            case 2630919:
                return 80002846;
            case 2630971:
                return 80002853;
            case 2631136:
                return 80002858;
            case 2631140:
                return 80002632;
            case 2631191:
                return 80002860;
            case 2631413:
                return 80002862;
            case 2631448:
                return 80002869;
            case 2631460:
                return 80002870;
            case 2631518:
                return 80002872;
            case 2631520:
                return 80002875;
            case 2631561:
                return 80002881;
            case 2631563:
                return 80002884;
            case 2631710:
                return 80002920;
            case 2631800:
                return 80002922;
            case 2631890:
                return 80002936;
            case 2631914:
                return 80002938;
            case 2632120:
                return 80002944;
            case 2632275:
                return 80002980;
            case 2632283:
                return 80002979;
            case 2632352:
                return 80002986;
            case 2632353:
                return 80002987;
            case 2632361:
                return 80002985;
            case 2632445:
                return 80002994;
            case 2632713:
                return 80002990;
            case 2632820:
                return 80002991;
            case 2632821:
                return 80002993;
            case 2632885:
                return 80002997;
            case 2632887:
                return 80002999;
            case 2632913:
                return 80003024;
        }
        return 0;
    }

    public static int getMountItem(int sourceid, MapleCharacter player) {
        if (sourceid == 33001001) {
            switch (Integer.parseInt(player.getInfoQuest(123456))) {
                case 10:
                    return 1932015;
                case 20:
                    return 1932030;
                case 30:
                    return 1932031;
                case 40:
                    return 1932032;
                case 50:
                    return 1932033;
                case 60:
                    return 1932036;
                case 70:
                    return 1932100;
                case 80:
                    return 1932149;
                case 90:
                    return 1932215;
            }
            return 0;
        }
        return getMountItemEx(sourceid);
    }

    public static int getJaguarSummonId(int sourceid) {
        switch (sourceid - 33001007) {
            case 0:
                return 1932015;
            case 1:
                return 1932030;
            case 2:
                return 1932031;
            case 3:
                return 1932032;
            case 4:
                return 1932033;
            case 5:
                return 1932036;
            case 6:
                return 1932100;
            case 7:
                return 1932149;
            case 8:
                return 1932215;
        }
        return 0;
    }

    public static int getSelectJaguarSkillId(int mountid) {
        switch (mountid) {
            case 1932015:
                return 33001007;
            case 1932030:
                return 33001008;
            case 1932031:
                return 33001009;
            case 1932032:
                return 33001010;
            case 1932033:
                return 33001011;
            case 1932036:
                return 33001012;
            case 1932100:
                return 33001013;
            case 1932149:
                return 33001014;
            case 1932215:
                return 33001015;
        }
        return 0;
    }

    public static boolean isKatara(int itemId) {
        return (itemId / 10000 == 134);
    }

    public static boolean isDagger(int itemId) {
        return (itemId / 10000 == 133);
    }

    public static boolean isApplicableSkill(int skil) {
        return (((skil < 80000000 || skil >= 100000000) && (skil % 10000 < 8000 || skil % 10000 > 8006) && !isAngel(skil)) || skil >= 92000000 || (skil >= 80000000 && skil < 80010000));
    }

    public static boolean isApplicableSkill_(int skil) {
        for (int i : PlayerStats.pvpSkills) {
            if (skil == i) {
                return true;
            }
        }
        return ((skil >= 90000000 && skil < 92000000) || (skil % 10000 >= 8000 && skil % 10000 <= 8003) || isAngel(skil));
    }

    public static boolean isTablet(int itemId) {
        return (itemId >= 2047000 && itemId <= 2047309);
    }

    public static boolean isGeneralScroll(int itemId) {
        return (itemId / 1000 == 2046);
    }

    public static int getSuccessTablet(int scrollId, int level) {
        if (scrollId % 1000 / 100 == 2) {
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 55;
                case 2:
                    return 43;
                case 3:
                    return 33;
                case 4:
                    return 26;
                case 5:
                    return 20;
                case 6:
                    return 16;
                case 7:
                    return 12;
                case 8:
                    return 10;
            }
            return 7;
        }
        if (scrollId % 1000 / 100 == 3) {
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 35;
                case 2:
                    return 18;
                case 3:
                    return 12;
            }
            return 7;
        }
        switch (level) {
            case 0:
                return 70;
            case 1:
                return 50;
            case 2:
                return 36;
            case 3:
                return 26;
            case 4:
                return 19;
            case 5:
                return 14;
            case 6:
                return 10;
        }
        return 7;
    }

    public static int getCurseTablet(int scrollId, int level) {
        if (scrollId % 1000 / 100 == 2) {
            switch (level) {
                case 0:
                    return 10;
                case 1:
                    return 12;
                case 2:
                    return 16;
                case 3:
                    return 20;
                case 4:
                    return 26;
                case 5:
                    return 33;
                case 6:
                    return 43;
                case 7:
                    return 55;
                case 8:
                    return 70;
            }
            return 100;
        }
        if (scrollId % 1000 / 100 == 3) {
            switch (level) {
                case 0:
                    return 12;
                case 1:
                    return 18;
                case 2:
                    return 35;
                case 3:
                    return 70;
            }
            return 100;
        }
        switch (level) {
            case 0:
                return 10;
            case 1:
                return 14;
            case 2:
                return 19;
            case 3:
                return 26;
            case 4:
                return 36;
            case 5:
                return 50;
            case 6:
                return 70;
        }
        return 100;
    }

    public static boolean isAccessory(int itemId) {
        return (itemId / 10000 == 101 || itemId / 10000 == 102 || itemId / 10000 == 103 || itemId / 10000 == 111 || itemId / 10000 == 112 || itemId / 10000 == 113 || itemId / 10000 == 114 || itemId / 10000 == 115);
    }

    public static boolean isMedal(int itemId) {
        return (itemId / 10000 == 114);
    }

    public static boolean potentialIDFits(int potentialID, int newstate, int i) {
        if (newstate == 20) {
            return (i == 0 || Randomizer.nextInt(10) == 0) ? ((potentialID >= 40000)) : ((potentialID >= 30000 && potentialID < 60004));
        }
        if (newstate == 19) {
            return (i == 0 || Randomizer.nextInt(10) == 0) ? ((potentialID >= 30000)) : ((potentialID >= 20000 && potentialID < 30000));
        }
        if (newstate == 18) {
            return (i == 0 || Randomizer.nextInt(10) == 0) ? ((potentialID >= 20000 && potentialID < 30000)) : ((potentialID >= 10000 && potentialID < 20000));
        }
        if (newstate == 17) {
            return (i == 0 || Randomizer.nextInt(10) == 0) ? ((potentialID >= 10000 && potentialID < 20000)) : ((potentialID < 10000));
        }
        return false;
    }

    public static boolean optionTypeFits(int optionType, int itemId) {
        switch (optionType) {
            case 10:
                return isWeapon(itemId);
            case 11:
                return !isWeapon(itemId);
            case 20:
                return (!isAccessory(itemId) && !isWeapon(itemId));
            case 40:
                return isAccessory(itemId);
            case 51:
                return (itemId / 10000 == 100);
            case 52:
                return (itemId / 10000 == 104 || itemId / 10000 == 105);
            case 53:
                return (itemId / 10000 == 106 || itemId / 10000 == 105);
            case 54:
                return (itemId / 10000 == 108);
            case 55:
                return (itemId / 10000 == 107);
        }
        return true;
    }

    public static final boolean isMountItemAvailable(int mountid, int jobid) {
        if (jobid != 900 && mountid / 10000 == 190) {
            switch (mountid) {
                case 1902000:
                case 1902001:
                case 1902002:
                    return isAdventurer(jobid);
                case 1902005:
                case 1902006:
                case 1902007:
                    return isKOC(jobid);
                case 1902015:
                case 1902016:
                case 1902017:
                case 1902018:
                    return isAran(jobid);
                case 1902040:
                case 1902041:
                case 1902042:
                    return isEvan(jobid);
                case 1932016:
                    return isMechanic(jobid);
                case 1932417:
                    return isMercedes(jobid);
            }
            if (isResist(jobid)) {
                return false;
            }
        }
        if (mountid / 10000 != 190) {
            return false;
        }
        return true;
    }

    public static boolean isMechanicItem(int itemId) {
        return (itemId >= 1610000 && itemId < 1660000);
    }

    public static boolean isEvanDragonItem(int itemId) {
        return (itemId >= 1940000 && itemId < 1980000);
    }

    public static boolean canScroll(int itemId) {
        return (itemId / 100000 != 19 && itemId / 100000 != 16);
    }

    public static boolean canHammer(int itemId) {
        switch (itemId) {
            case 1122000:
            case 1122076:
                return false;
        }
        if (!canScroll(itemId)) {
            return false;
        }
        return true;
    }

    public static int[] owlItems = new int[]{1002357, 1112585, 1032022, 1082002, 5062000, 2049100, 1050018, 1112400, 5062009, 1112748};

    public static int[] royalstyle = new int[]{
        1042290, 1042290, 1003910, 1003910, 1003859, 1003859, 1012371, 1012371, 1022183, 1022183,
        1112258, 1112258, 1112146, 1112146, 5065100, 5065100, 5281003, 5281003, 5030028, 5030028};

    public static int[] masterpiece = new int[]{5069000, 5069001};

    public static int getMasterySkill(int job) {
        if (job >= 1410 && job <= 1412) {
            return 14100000;
        }
        if (job >= 410 && job <= 412) {
            return 4100000;
        }
        if (job >= 520 && job <= 522) {
            return 5200000;
        }
        return 0;
    }

    public static int getExpRate_Below10(int job) {
        if (isEvan(job)) {
            return 1;
        }
        if (isAran(job) || isKOC(job) || isResist(job)) {
            return 1;
        }
        return 1;
    }

    public static int getExpRate_Quest(int level) {
        return (level >= 30) ? ((level >= 70) ? ((level >= 120) ? 10 : 5) : 2) : 1;
    }

    public static String getCashBlockedMsg(int id) {
        switch (id) {
            case 5050000:
            case 5062000:
            case 5062001:
                return "This item may only be purchased at the PlayerNPC in FM.";
        }
        return "This item is blocked from the Cash Shop.";
    }

    public static int getCustomReactItem(int rid, int original) {
        if (rid == 2008006) {
            return Calendar.getInstance().get(7) + 4001055;
        }
        return original;
    }

    public static int getJobNumber(int skill) {
        int jobz, job = (jobz = skill / 10000) % 1000;
        if (SkillFactory.getSkill(skill) != null && SkillFactory.getSkill(skill).isHyper()) {
            return 5;
        }
        if (job == 301) {
            return 1;
        }
        if (job / 100 == 0 || isBeginnerJob(jobz)) {
            return 0;
        }
        if (job / 10 % 10 == 0 || job == 501 || job == 430) {
            return 1;
        }
        if (job == 431 || job == 432) {
            return 2;
        }
        if (job == 433) {
            return 3;
        }
        if (job == 434) {
            return 4;
        }
        return 2 + job % 10;
    }

    public static int getOrdinaryJobNumber(int job) {
        if (isAdventurer(job)) {
            return 0;
        }
        if (isKOC(job)) {
            return 1000;
        }
        if (isAran(job)) {
            return 2000;
        }
        if (isEvan(job)) {
            return 2001;
        }
        if (isMercedes(job)) {
            return 2002;
        }
        if (isPhantom(job)) {
            return 2003;
        }
        if (isLuminous(job)) {
            return 2004;
        }
        if (isEunWol(job)) {
            return 2005;
        }
        if (isDemonSlayer(job) || isDemonAvenger(job)) {
            return 3001;
        }
        if (isXenon(job)) {
            return 3002;
        }
        if (isResist(job)) {
            return 3000;
        }
        if (isMichael(job)) {
            return 5000;
        }
        if (isKaiser(job)) {
            return 6000;
        }
        if (isAngelicBuster(job)) {
            return 6001;
        }
        if (isKadena(job)) {
            return 6002;
        }
        if (isZero(job)) {
            return 10000;
        }
        if (isPinkBean(job)) {
            return 13000;
        }
        if (isKinesis(job)) {
            return 14000;
        }
        if (isIllium(job)) {
            return 15000;
        }
        if (isArk(job)) {
            return 15001;
        }
        return -1;
    }

    public static boolean isBeginnerJob(int job) {
        return (job == 0 || job == 1 || job == 301 || job == 1000 || job == 2000 || job == 2001 || job == 3000 || job == 3001 || job == 2002 || job == 2003 || job == 5000 || job == 2004 || job == 2005 || job == 6000 || job == 6001 || job == 3002 || job == 10000 || job == 13000 || job == 14000 || job == 6002 || job == 15000 || job == 15001 || job == 15002);
    }

    public static boolean isForceRespawn(int mapid) {
        switch (mapid) {
            case 103000800:
            case 925100100:
                return true;
        }
        return false;
    }

    public static int getCustomSpawnID(int summoner, int def) {
        switch (summoner) {
            case 9400589:
            case 9400748:
                return 9400706;
        }
        return def;
    }

    public static boolean canForfeit(int questid) {
        switch (questid) {
            case 20000:
            case 20010:
            case 20015:
            case 20020:
                return false;
        }
        return true;
    }

    public static double getAttackRange(SecondaryStatEffect def, int rangeInc) {
        double defRange = (400.0D + rangeInc) * (400.0D + rangeInc);
        if (def != null) {
            defRange += def.getMaxDistanceSq() + (def.getRange() * def.getRange());
        }
        return defRange + 200000.0D;
    }

    public static double getAttackRange(Point lt, Point rb) {
        double defRange = 160000.0D;
        int maxX = Math.max(Math.abs((lt == null) ? 0 : lt.x), Math.abs((rb == null) ? 0 : rb.x));
        int maxY = Math.max(Math.abs((lt == null) ? 0 : lt.y), Math.abs((rb == null) ? 0 : rb.y));
        defRange += (maxX * maxX + maxY * maxY);
        return defRange + 120000.0D;
    }

    public static int getLowestPrice(int itemId) {
        switch (itemId) {
            case 2340000:
            case 2530000:
            case 2531000:
                return 50000000;
        }
        return -1;
    }

    public static boolean isNoDelaySkill(int skillId) {
        return (skillId == 3301008 || skillId == 3311010 || skillId == 5100015 || skillId == 5110014 || skillId == 5120018 || skillId == 21101003 || skillId == 15100004 || skillId == 33101004 || skillId == 32111010 || skillId == 2111007 || skillId == 2211007 || skillId == 2311007 || skillId == 32121003 || skillId == 35121005 || skillId == 35111004 || skillId == 35121013 || skillId == 35121003 || skillId == 22150004 || skillId == 22181004 || skillId == 11101002 || skillId == 13101002 || skillId == 25100001 || skillId == 25100002 || skillId == 3121013 || skillId == 95001000 || skillId == 152120003);
    }

    public static boolean isNoSpawn(int mapID) {
        return (mapID == 809040100 || mapID == 925020010 || mapID == 925020011 || mapID == 925020012 || mapID == 925020013 || mapID == 925020014 || mapID == 980010000 || mapID == 980010100 || mapID == 980010200 || mapID == 980010300 || mapID == 980010020);
    }

    public static int getExpRate(int job, int def) {
        return def;
    }

    public static int getModifier(int itemId, int up) {
        if (up <= 0) {
            return 0;
        }
        switch (itemId) {
            case 2022459:
            case 2860179:
            case 2860193:
            case 2860207:
                return 130;
            case 2022460:
            case 2022462:
            case 2022730:
                return 150;
            case 2860181:
            case 2860195:
            case 2860209:
                return 200;
        }
        return 200;
    }

    public static short getSlotMax(int itemId) { //합치고싶은거 있으면 이렇게 추가해주셈 그리고 빌드ㄱㄱ 이렇게 추가해주고빌드하면됨 되있네요?
        switch (itemId) {
            case 5062005:
            case 5062006:
            case 5062009:
            case 5062010:
            case 5062500:
            case 5062503:
            case 2437760:
            case 2437761:
            case 4001168:
            case 4031306:
            case 4031307:
            case 3993000:
            case 3993002:
            case 3993003:
            case 2048716:
            case 2049370:
            case 2450042:
            case 2049153:
            case 2048226:
            case 4310086:
            case 5220010:
            case 5220013:
            case 2434851:
            case 2048724:
            case 2048745:
            case 5220020:
            case 2432423:
            case 2711003:
            case 2711004:
            case 2630281:
            case 2711005:
            case 2711012:
            case 2435748:
            case 2437158:
            case 4036444:
            case 4310113:
            case 4310029:
            case 4310065:
            case 4310229:
            case 4021031:
            case 4000094:
            case 4001832:
            case 4031213:
            case 5220000:
            case 4310261:
            case 4033114:
            case 4000178:
            case 4000620:
            case 4000006:
            case 4031831:
            case 4001715:
            case 4033884:
            case 4033885:
            case 4033891:
            case 4033892:
            case 4003002:
            case 2437121:
            case 4034181:
            case 2430026:
            case 2430027:
            case 2450064:
            case 2450134:
            case 2431940:
            case 4310291:
            case 4036068:
            case 4310027:
            case 4310034:
            case 4310036:
            case 4310038:
            case 4310048:
            case 4310057:
            case 4310059:
            case 4310063:
            case 4310085:
            case 4310153:
            case 2048753:
            case 3996007:
            case 4031788:
            case 2430016:
            case 2431341:
            case 2438145:
            case 4033338:
            case 4009155:
            case 4000190:
            case 4000965:
            case 4010000:
            case 4010001:
            case 2435719:
            case 5068300:
            case 5068301:
            case 5068302:
            case 4260003:
            case 4260004:
            case 4260005:
            case 4021016:
            case 4031457:
            case 5068303:
            case 5068304:
            case 4031466:
            case 4162009:
            case 4009239:
            case 4310061:
            case 4036573:
            case 4034271:
            case 4310001:
            case 4031569:
            case 4031838:
            case 2433979:
            case 2437157:
            case 2450054:
            case 2049372:
            case 4034803:
            case 4033151:
            case 4036531:
            case 4021037:
            case 2000019:
            case 2023072:
            case 5060048:
            case 4001878:
            case 2003550:
            case 2003551:
            case 2003575:
            case 2630127:
            case 4310129:
            case 2049704:
            case 4001842:
            case 4001843:
            case 4001868:
            case 4001869:
            case 2048717:
            case 2630755:
            case 2439653:
            case 4000101:
            case 4034809:
            case 4000220:
            case 4000439:
            case 4000896:
            case 4000979:
            case 4031311:
            case 4310198:
            case 4001786:
                return 30000;
            case 40:
                return 30000;
        }
        return 0;
    }

    public static boolean isDropRestricted(int itemId) {
        return (itemId == 3012000 || itemId == 4030004 || itemId == 1052098 || itemId == 1052202);
    }

    public static boolean isPickupRestricted(int itemId) {
        return (itemId == 4030003 || itemId == 4030004);
    }

    public static short getStat(int itemId, int def) {
        switch (itemId) {
            case 1002419:
                return 5;
            case 1002959:
                return 25;
            case 1142002:
                return 10;
            case 1122121:
                return 7;
        }
        return (short) def;
    }

    public static short getHpMp(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 500;
            case 1002959:
            case 1142002:
                return 1000;
        }
        return (short) def;
    }

    public static short getATK(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 3;
            case 1002959:
                return 4;
            case 1142002:
                return 9;
        }
        return (short) def;
    }

    public static short getDEF(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 250;
            case 1002959:
                return 500;
        }
        return (short) def;
    }

    public static boolean isDojo(int mapId) {
        return (mapId >= 925020100 && mapId <= 925023814);
    }

    public static int getPartyPlayHP(int mobID) {
        switch (mobID) {
            case 4250000:
                return 836000;
            case 4250001:
                return 924000;
            case 5250000:
                return 1100000;
            case 5250001:
                return 1276000;
            case 5250002:
                return 1452000;
            case 9400661:
                return 15000000;
            case 9400660:
                return 30000000;
            case 9400659:
                return 45000000;
            case 9400658:
                return 20000000;
        }
        return 0;
    }

    public static int getPartyPlayEXP(int mobID) {
        switch (mobID) {
            case 4250000:
                return 5770;
            case 4250001:
                return 6160;
            case 5250000:
                return 7100;
            case 5250001:
                return 7975;
            case 5250002:
                return 8800;
            case 9400661:
                return 40000;
            case 9400660:
                return 70000;
            case 9400659:
                return 90000;
            case 9400658:
                return 50000;
        }
        return 0;
    }

    public static int getPartyPlay(int mapId) {
        switch (mapId) {
            case 300010000:
            case 300010100:
            case 300010200:
            case 300010300:
            case 300010400:
            case 300020000:
            case 300020100:
            case 300020200:
            case 300030000:
            case 683070400:
            case 683070401:
            case 683070402:
                return 25;
        }
        return 0;
    }

    public static int getPartyPlay(int mapId, int def) {
        int dd = getPartyPlay(mapId);
        if (dd > 0) {
            return dd;
        }
        return def / 2;
    }

    public static boolean isHyperTeleMap(int mapId) {
        for (int i : hyperTele) {
            if (i == mapId) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentFullDate() {
        String time = FileoutputUtil.CurrentReadable_Time();
        return time.substring(0, 4) + time.substring(5, 7) + time.substring(8, 10) + time.substring(11, 13) + time.substring(14, 16) + time.substring(17, 19);
    }

    public static int getCurrentDate() {
        String time = FileoutputUtil.CurrentReadable_Time();
        return Integer.parseInt(time.substring(0, 4) + time.substring(5, 7) + time.substring(8, 10) + time.substring(11, 13));
    }

    public static int getCurrentDateYesterday() {
        Calendar ocal = Calendar.getInstance();
        String years = "" + ocal.get(1) + "";
        String months = "" + (ocal.get(2) + 1) + "";
        String days = "" + ocal.get(5) + "";
        int month = ocal.get(2) + 1;
        int day = ocal.get(5) - 1;
        if (month < 10) {
            months = "0" + month;
        } else {
            months = "" + month;
        }
        if (day < 10) {
            days = "0" + day;
        } else {
            days = "" + day;
        }
        return Integer.parseInt(years + months + days);
    }

    public static int getCurrentDateday() {
        Calendar ocal = Calendar.getInstance();
        String years = "" + ocal.get(1) + "";
        String months = "" + (ocal.get(2) + 1) + "";
        String days = "" + ocal.get(5) + "";
        int month = ocal.get(2) + 1;
        int day = ocal.get(5);
        if (month < 10) {
            months = "0" + month;
        } else {
            months = "" + month;
        }
        if (day < 10) {
            days = "0" + day;
        } else {
            days = "" + day;
        }
        return Integer.parseInt(years + months + days);
    }

    public static int getCurrentDate_NoTime() {
        String time = FileoutputUtil.CurrentReadable_Time();
        return Integer.parseInt(time.substring(0, 4) + time.substring(5, 7) + time.substring(8, 10));
    }

    public static void achievementRatio(MapleClient c, int mapId) {
        switch (mapId) {
            case 240080600:
            case 910010000:
            case 910340100:
            case 920010000:
            case 921120005:
            case 921160100:
            case 922010100:
            case 923040100:
            case 925100000:
            case 926100000:
            case 926110000:
            case 930000000:
            case 930000100:
            case 932000100:
                c.getSession().writeAndFlush(CField.achievementRatio(0));
                break;
            case 921160200:
            case 922010200:
            case 922010300:
            case 922010400:
            case 922010401:
            case 922010402:
            case 922010403:
            case 922010404:
            case 922010405:
            case 925100100:
            case 926100001:
            case 926110001:
            case 930000200:
                c.getSession().writeAndFlush(CField.achievementRatio(10));
                break;
            case 910340200:
            case 921120100:
            case 921160300:
            case 921160310:
            case 921160320:
            case 921160330:
            case 921160340:
            case 921160350:
            case 922010500:
            case 922010600:
            case 923040200:
            case 925100200:
            case 925100201:
            case 925100202:
            case 926100100:
            case 926110100:
            case 930000300:
            case 932000200:
                c.getSession().writeAndFlush(CField.achievementRatio(25));
                break;
            case 921160400:
            case 926100200:
            case 926100201:
            case 926100202:
            case 926110200:
            case 926110201:
            case 926110202:
            case 930000400:
                c.getSession().writeAndFlush(CField.achievementRatio(35));
                break;
            case 240080700:
            case 240080800:
            case 910340300:
            case 921120200:
            case 921160500:
            case 922010700:
            case 923040300:
            case 925100300:
            case 925100301:
            case 925100302:
            case 926100203:
            case 926110203:
            case 930000500:
            case 932000300:
                c.getSession().writeAndFlush(CField.achievementRatio(50));
                break;
            case 910340400:
            case 921120300:
            case 921160600:
            case 922010800:
            case 923040400:
            case 925100400:
            case 926100300:
            case 926100301:
            case 926100302:
            case 926100303:
            case 926100304:
            case 926110300:
            case 926110301:
            case 926110302:
            case 926110303:
            case 926110304:
            case 930000600:
            case 932000400:
                c.getSession().writeAndFlush(CField.achievementRatio(70));
                break;
            case 910340500:
            case 920010800:
            case 921120400:
            case 921160700:
            case 922010900:
            case 925100500:
            case 926100400:
            case 926100401:
            case 926110400:
            case 926110401:
            case 930000700:
                c.getSession().writeAndFlush(CField.achievementRatio(85));
                break;
            case 920011000:
            case 920011100:
            case 920011200:
            case 920011300:
            case 921120500:
            case 921120600:
            case 922011000:
            case 922011100:
            case 925100600:
            case 926100500:
            case 926100600:
            case 926110500:
            case 926110600:
            case 930000800:
                c.getSession().writeAndFlush(CField.achievementRatio(100));
                break;
        }
    }

    public static boolean isAngel(int sourceid) {
        return (isBeginnerJob(sourceid / 10000) && (sourceid % 10000 == 1085 || sourceid % 10000 == 1087 || sourceid % 10000 == 1090 || sourceid % 10000 == 1179));
    }

    public static int getRewardPot(int itemid, int closeness) {
        switch (itemid) {
            case 2440000:
                switch (closeness / 10) {
                    case 0:
                    case 1:
                    case 2:
                        return 2028041 + closeness / 10;
                    case 3:
                    case 4:
                    case 5:
                        return 2028046 + closeness / 10;
                    case 6:
                    case 7:
                    case 8:
                        return 2028049 + closeness / 10;
                }
                return 2028057;
            case 2440001:
                switch (closeness / 10) {
                    case 0:
                    case 1:
                    case 2:
                        return 2028044 + closeness / 10;
                    case 3:
                    case 4:
                    case 5:
                        return 2028049 + closeness / 10;
                    case 6:
                    case 7:
                    case 8:
                        return 2028052 + closeness / 10;
                }
                return 2028060;
            case 2440002:
                return 2028069;
            case 2440003:
                return 2430278;
            case 2440004:
                return 2430381;
            case 2440005:
                return 2430393;
        }
        return 0;
    }

    public static boolean isMagicChargeSkill(int skillid) {
        switch (skillid) {
            case 2121001:
            case 2221001:
            case 2321001:
                return true;
        }
        return false;
    }

    public static boolean isTeamMap(int mapid) {
        return (mapid == 109080000 || mapid == 109080001 || mapid == 109080002 || mapid == 109080003 || mapid == 109080010 || mapid == 109080011 || mapid == 109080012 || mapid == 109090301 || mapid == 109090302 || mapid == 109090303 || mapid == 109090304 || mapid == 910040100 || mapid == 960020100 || mapid == 960020101 || mapid == 960020102 || mapid == 960020103 || mapid == 960030100 || mapid == 689000000 || mapid == 689000010);
    }

    public static int getStatDice(int stat) {
        switch (stat) {
            case 2:
                return 30;
            case 3:
                return 20;
            case 4:
                return 15;
            case 5:
                return 20;
            case 6:
                return 30;
        }
        return 0;
    }

    public static int getDiceStat(int buffid, int stat) {
        if (buffid == stat || buffid % 10 == stat || buffid / 10 == stat) {
            return getStatDice(stat);
        }
        if (buffid == stat * 100) {
            return getStatDice(stat) + 10;
        }
        return 0;
    }

    public static int getMPByJob(MapleCharacter chr) {
        int force = 30;
        switch (chr.getJob()) {
            case 3110:
                force = 50;
                break;
            case 3111:
                force = 100;
                break;
            case 3112:
                force = 120;
                break;
        }
        Equip shield = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
        if (chr.getSkillLevel(80000406) > 0) {
            force += SkillFactory.getSkill(80000406).getEffect(chr.getSkillLevel(80000406)).getMaxDemonForce();
        }
        if (shield != null) {
            force += shield.getMp();
        }
        return force;
    }

    public static int getSkillLevel(int level) {
        if (level >= 70 && level < 120) {
            return 2;
        }
        if (level >= 120 && level < 200) {
            return 3;
        }
        if (level >= 200) {
            return 4;
        }
        return 1;
    }

    public static int[] getInnerSkillbyRank(int rank) {
        if (rank == 0) {
            return rankC;
        }
        if (rank == 1) {
            return rankB;
        }
        if (rank == 2) {
            return rankA;
        }
        if (rank == 3) {
            return rankS;
        }
        return null;
    }

    public static int ArcaneNextUpgrade(int level) {
        int lev = 12;
        for (int i = 1; i < level; i++) {
            lev += i + i + 1;
        }
        return lev;
    }

    public static int AutNextUpgrade(int level) {
        int totalexp = 9 * level * level + 20 * level;
        return totalexp;
    }

    public static long NeedAutSymbolMeso(int level) {
        long meso = (99000000 + 88500000 * level);
        return meso;
    }

    public static int getCubeMeso(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (!ii.getEquipStats(itemId).containsKey("reqLevel")) {
            return -1;
        }
        int level = ((Integer) ii.getEquipStats(itemId).get("reqLevel")).intValue();
        int meso = 0;
        switch (level) {
            case 35:
                meso = 612;
                break;
            case 38:
                meso = 722;
                break;
            case 40:
                meso = 800;
                break;
            case 43:
                meso = 924;
                break;
            case 45:
                meso = 1012;
                break;
            case 48:
                meso = 1152;
                break;
            case 50:
                meso = 1250;
                break;
            case 55:
                meso = 1512;
                break;
            case 58:
                meso = 1682;
                break;
            case 60:
                meso = 1800;
                break;
            case 64:
                meso = 2048;
                break;
            case 65:
                meso = 2112;
                break;
            case 68:
                meso = 2312;
                break;
            case 70:
                meso = 2450;
                break;
            case 75:
                meso = 14062;
                break;
            case 77:
                meso = 14822;
                break;
            case 78:
                meso = 15210;
                break;
            case 80:
                meso = 16000;
                break;
            case 85:
                meso = 18062;
                break;
            case 90:
                meso = 20250;
                break;
            case 95:
                meso = 22562;
                break;
            case 100:
                meso = 25000;
                break;
            case 105:
                meso = 27562;
                break;
            case 110:
                meso = 30250;
                break;
            case 115:
                meso = 33062;
                break;
            case 120:
                meso = 36000;
                break;
            case 125:
                meso = 312500;
                break;
            case 130:
                meso = 338000;
                break;
            case 135:
                meso = 364500;
                break;
            case 140:
                meso = 392000;
                break;
            case 145:
                meso = 420500;
                break;
            case 150:
                meso = 450000;
                break;
            case 160:
                meso = 512000;
                break;
            case 200:
                meso = 800000;
                break;
        }
        return meso;
    }

    public static long getMagnifyPrice(Equip eq) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (!ii.getEquipStats(eq.getItemId()).containsKey("reqLevel")) {
            return -1L;
        }
        int level = ((Integer) ii.getEquipStats(eq.getItemId()).get("reqLevel")).intValue();
        int v1 = 0;
        if (level > 120) {
            v1 = 20;
        } else if (level > 70) {
            v1 = 5;
        } else if (level > 30) {
            v1 = 1;
        }
        double v2 = level;
        int v3 = 2;
        double v4 = 0.5D;
        while (true) {
            if ((v3 & 0x1) != 0) {
                v4 *= v2;
            }
            v3 >>= 1;
            if (v3 == 0) {
                break;
            }
            v2 *= v2;
        }
        int v5 = (int) Math.ceil(v4);
        long price = (((v1 * v5 <= 0) ? 1 : 0) - 1 & v1 * v5);
        return price;
    }

    public static final boolean isSuperior(int itemId) {
        return ((itemId >= 1102471 && itemId <= 1102485) || (itemId >= 1072732 && itemId <= 1072747) || (itemId >= 1132164 && itemId <= 1132178) || (itemId >= 1122241 && itemId <= 1122245) || (itemId >= 1082543 && itemId <= 1082547));
    }

    public static int getOptionType(int itemId) {
        int id = itemId / 10000;
        if (isWeapon(itemId) || itemId / 1000 == 1099) {
            return 10;
        }
        if (id == 109 || id == 110 || id == 113) {
            return 20;
        }
        if (isAccessory(itemId)) {
            return 40;
        }
        if (id == 100) {
            return 51;
        }
        if (id == 104 || id == 106) {
            return 52;
        }
        if (id == 105) {
            return 53;
        }
        if (id == 108) {
            return 54;
        }
        if (id == 107) {
            return 55;
        }
        return 0;
    }

    public static int getLuckyInfofromItemId(int itemid) {
        switch (itemid) {
            case 2048900:
                return 6;
            case 2048901:
                return 45;
            case 2048902:
                return 25;
            case 2048903:
                return 69;
            case 2048904:
                return 214;
            case 2048905:
                return 247;
            case 2048906:
                return 104;
            case 2048907:
                return 303;
            case 2048912:
                return 452;
            case 2048913:
                return 457;
            case 2048915:
                return 504;
            case 2048918:
                return 617;
        }
        return 0;
    }

    public static boolean isAngelicBlessBuffEffectItem(int skill) {
        switch (skill) {
            case 2022746:
            case 2022747:
            case 2022764:
            case 2022823:
                return true;
        }
        return false;
    }

    public static int getRandomProfessionReactorByRank(int rank) {
        int base1 = 100000;
        int base2 = 200000;
        if (Randomizer.nextBoolean()) {
            if (rank == 1) {
                base1 += Randomizer.rand(0, 7);
            } else if (rank == 2) {
                base1 += Randomizer.rand(4, 9);
            } else if (rank == 3) {
                if (Randomizer.rand(0, 4) == 1) {
                    base1 += 11;
                } else {
                    base1 += Randomizer.rand(0, 9);
                }
            }
            return base1;
        }
        if (rank == 1) {
            base2 += Randomizer.rand(0, 7);
        } else if (rank == 2) {
            base2 += Randomizer.rand(4, 9);
        } else if (rank == 3) {
            if (Randomizer.rand(0, 6) == 1) {
                base2 += 11;
            } else {
                base2 += Randomizer.rand(0, 9);
            }
        }
        return base2;
    }

    public static int getTriFling(int job) {
        switch (job) {
            case 1310:
                return 13100022;
            case 1311:
                return 13110022;
            case 1312:
                return 13120003;
        }
        return 0;
    }

    public static int getStealSkill(int job) {
        switch (job) {
            case 1:
                return 24001001;
            case 2:
                return 24101001;
            case 3:
                return 24111001;
            case 4:
                return 24121001;
            case 5:
                return 24121054;
        }
        return 0;
    }

    public static int getNumSteal(int jobNum) {
        switch (jobNum) {
            case 1:
            case 2:
                return 4;
            case 3:
            case 4:
                return 3;
            case 5:
                return 2;
        }
        return 0;
    }

    public static int getAndroidType(int itemid) {
        if (MapleItemInformationProvider.getInstance().getEquipStats(itemid).containsKey("android")) {
            return ((Integer) MapleItemInformationProvider.getInstance().getEquipStats(itemid).get("android")).intValue();
        }
        return 1;
    }

    public static boolean isExceedAttack(int id) {
        switch (id) {
            case 31011000:
            case 31011004:
            case 31011005:
            case 31011006:
            case 31011007:
            case 31201000:
            case 31201007:
            case 31201008:
            case 31201009:
            case 31201010:
            case 31211000:
            case 31211007:
            case 31211008:
            case 31211009:
            case 31211010:
            case 31221000:
            case 31221009:
            case 31221010:
            case 31221011:
            case 31221012:
                return true;
        }
        return false;
    }

    public static int isLightSkillsGaugeCheck(int skillid) {
        switch (skillid) {
            case 27001100:
                return 123;
            case 27101100:
                return 236;
            case 27111100:
                return 267;
            case 27111101:
                return 105;
            case 27121100:
                return 409;
        }
        return 0;
    }

    public static int isDarkSkillsGaugeCheck(MapleCharacter chr, int skillid) {
        switch (skillid) {
            case 27001201:
                return 252;
            case 27101202:
                return 181;
            case 27111202:
                return 325;
            case 27121201:
                return 10;
            case 27121202:
                if (chr.getSkillLevel(27120047) > 0) {
                    return 645;
                }
                return 430;
        }
        return 0;
    }

    public static boolean isLightSkills(int skillid) {
        switch (skillid) {
            case 20041226:
            case 27001100:
            case 27101100:
            case 27111100:
            case 27121100:
                return true;
        }
        return false;
    }

    public static boolean isDarkSkills(int skillid) {
        switch (skillid) {
            case 27001201:
            case 27101202:
            case 27111202:
            case 27121201:
            case 27121202:
                return true;
        }
        return false;
    }

    public static final String getJobNameById(int job) {
        switch (job) {
            case 0:
                return "초보자";
            case 100:
                return "검사";
            case 110:
                return "파이터";
            case 111:
                return "크루세이더";
            case 112:
                return "히어로";
            case 120:
                return "페이지";
            case 121:
                return "나이트";
            case 122:
                return "팔라딘";
            case 130:
                return "스피어맨";
            case 131:
                return "버서커";
            case 132:
                return "다크나이트";
            case 200:
                return "마법사";
            case 210:
                return "위자드(불,독)";
            case 211:
                return "메이지(불,독)";
            case 212:
                return "아크메이지(불,독)";
            case 220:
                return "위자드(썬,콜)";
            case 221:
                return "메이지(썬,콜)";
            case 222:
                return "아크메이지(썬,콜)";
            case 230:
                return "클레릭";
            case 231:
                return "프리스트";
            case 232:
                return "비숍";
            case 300:
                return "아처";
            case 310:
                return "헌터";
            case 311:
                return "레인저";
            case 312:
                return "보우마스터";
            case 320:
                return "사수";
            case 321:
                return "저격수";
            case 322:
                return "신궁";
            case 400:
                return "로그";
            case 410:
                return "어쌔신";
            case 411:
                return "허밋";
            case 412:
                return "나이트로드";
            case 420:
                return "시프";
            case 421:
                return "시프마스터";
            case 422:
                return "섀도어";
            case 430:
                return "세미듀어러";
            case 431:
                return "듀어러";
            case 432:
                return "듀얼마스터";
            case 433:
                return "슬래셔";
            case 434:
                return "듀얼블레이더";
            case 500:
                return "해적";
            case 510:
                return "인파이터";
            case 511:
                return "버커니어";
            case 512:
                return "바이퍼";
            case 520:
                return "건슬링거";
            case 521:
                return "발키리";
            case 522:
                return "캡틴";
            case 800:
                return "매니저";
            case 900:
                return "운영자";
            case 1000:
                return "노블레스";
            case 1100:
            case 1110:
            case 1111:
            case 1112:
                return "소울마스터";
            case 1200:
            case 1210:
            case 1211:
            case 1212:
                return "플레임위자드";
            case 1300:
            case 1310:
            case 1311:
            case 1312:
                return "윈드브레이커";
            case 1400:
            case 1410:
            case 1411:
            case 1412:
                return "나이트워커";
            case 1500:
            case 1510:
            case 1511:
            case 1512:
                return "스트라이커";
            case 2000:
                return "레전드";
            case 2100:
            case 2110:
            case 2111:
            case 2112:
                return "아란";
            case 2001:
            case 2200:
            case 2210:
            case 2211:
            case 2212:
            case 2213:
            case 2214:
            case 2215:
            case 2216:
            case 2217:
            case 2218:
                return "에반";
            case 3000:
                return "시티즌";
            case 3200:
            case 3210:
            case 3211:
            case 3212:
                return "배틀메이지";
            case 3300:
            case 3310:
            case 3311:
            case 3312:
                return "와일드헌터";
            case 3500:
            case 3510:
            case 3511:
            case 3512:
                return "메카닉";
            case 501:
                return "해적(캐논슈터)";
            case 530:
                return "캐논슈터";
            case 531:
                return "캐논블래스터";
            case 532:
                return "캐논마스터";
            case 2002:
            case 2300:
            case 2310:
            case 2311:
            case 2312:
                return "메르세데스";
            case 3001:
            case 3100:
            case 3110:
            case 3111:
            case 3112:
                return "데몬슬레이어";
            case 2003:
            case 2400:
            case 2410:
            case 2411:
            case 2412:
                return "팬텀";
            case 2004:
            case 2700:
            case 2710:
            case 2711:
            case 2712:
                return "루미너스";
            case 5000:
            case 5100:
            case 5110:
            case 5111:
            case 5112:
                return "미하일";
            case 6000:
            case 6100:
            case 6110:
            case 6111:
            case 6112:
                return "카이저";
            case 6001:
            case 6500:
            case 6510:
            case 6511:
            case 6512:
                return "엔젤릭버스터";
            case 3101:
            case 3120:
            case 3121:
            case 3122:
                return "데몬어벤져";
            case 3002:
            case 3600:
            case 3610:
            case 3611:
            case 3612:
                return "제논";
            case 10000:
                return "제로JR";
            case 10100:
                return "제로10100";
            case 10110:
                return "제로10110";
            case 10111:
                return "제로10111";
            case 10112:
                return "제로";
            case 2005:
                return "???";
            case 2500:
            case 2510:
            case 2511:
            case 2512:
                return "은월";
            case 14000:
            case 14200:
            case 14210:
            case 14211:
            case 14212:
                return "키네시스";
        }
        if (isAdel(job)) {
            return "아델";
        }
        if (isCain(job)) {
            return "카인";
        }
        if (isKadena(job)) {
            return "카데나";
        }
        return "알수없음";
    }

    public static short getSoulName(int soulid) {
        return (short) (soulid - 2591000 + 1);
    }

    public static int[] soulItemid = new int[]{
        2591010, 2591011, 2591012, 2591013, 2591014, 2591015, 2591016, 2591017, 2591018, 2591019,
        2591020, 2591021, 2591022, 2591023, 2591024, 2591025, 2591026, 2591027, 2591028, 2591029,
        2591030, 2591031, 2591032, 2591033, 2591034, 2591035, 2591036, 2591037, 2591038, 2591039,
        2591040, 2591041, 2591042, 2591043, 2591044, 2591045, 2591046, 2591047, 2591048, 2591049,
        2591050, 2591051, 2591052, 2591053, 2591054, 2591055, 2591056, 2591057, 2591058, 2591059,
        2591060, 2591061, 2591062, 2591063, 2591064, 2591065, 2591066, 2591067, 2591068, 2591069,
        2591070, 2591071, 2591072, 2591073, 2591074, 2591075, 2591076, 2591077, 2591078, 2591079,
        2591080, 2591081, 2591082, 2591085, 2591086, 2591087, 2591088, 2591089, 2591090, 2591091,
        2591092, 2591093, 2591094, 2591095, 2591096, 2591097, 2591098, 2591099, 2591100, 2591101,
        2591102, 2591103, 2591104, 2591105, 2591106, 2591107, 2591108, 2591109, 2591110, 2591111,
        2591112, 2591113, 2591114, 2591115, 2591116, 2591117, 2591118, 2591119, 2591120, 2591121,
        2591122, 2591123, 2591124, 2591125, 2591126, 2591127, 2591128, 2591129, 2591130, 2591131,
        2591132, 2591133, 2591134, 2591135, 2591136, 2591137, 2591138, 2591139, 2591140, 2591141,
        2591142, 2591143, 2591144, 2591145, 2591146, 2591147, 2591148, 2591149, 2591150, 2591151,
        2591152, 2591153, 2591154, 2591155, 2591156, 2591157, 2591158, 2591159, 2591160, 2591161,
        2591162, 2591163, 2591164, 2591165, 2591166, 2591167, 2591168, 2591169, 2591170, 2591171,
        2591172, 2591173, 2591174, 2591175, 2591176, 2591177, 2591178, 2591179, 2591180, 2591181,
        2591182, 2591183, 2591184, 2591185, 2591186, 2591187, 2591188, 2591189, 2591190, 2591191,
        2591192, 2591193, 2591194, 2591195, 2591196, 2591197, 2591198, 2591199, 2591200, 2591201,
        2591202, 2591203, 2591204, 2591205, 2591206, 2591207, 2591208, 2591209, 2591210, 2591211,
        2591212, 2591213, 2591214, 2591215, 2591216, 2591217, 2591218, 2591219, 2591220, 2591221,
        2591222, 2591223, 2591224, 2591225, 2591226, 2591227, 2591228, 2591229, 2591230, 2591231,
        2591232, 2591233, 2591234, 2591235, 2591236, 2591237, 2591238, 2591239, 2591240, 2591241,
        2591242, 2591243, 2591244, 2591245, 2591246, 2591247, 2591248, 2591249, 2591250, 2591251,
        2591252, 2591253, 2591254, 2591255, 2591256, 2591257, 2591258, 2591259, 2591260, 2591261,
        2591262, 2591263, 2591264, 2591265, 2591266, 2591267, 2591268, 2591269, 2591270, 2591271,
        2591272, 2591273, 2591274, 2591275, 2591276, 2591277, 2591278, 2591279, 2591288, 2591289,
        2591290, 2591291, 2591292, 2591293, 2591294, 2591295, 2591296, 2591297, 2591298, 2591299,
        2591300, 2591301, 2591302, 2591303, 2591304, 2591305, 2591306, 2591307, 2591308, 2591309,
        2591310, 2591311, 2591312, 2591313, 2591314, 2591315, 2591316, 2591317, 2591318, 2591319,
        2591320, 2591321, 2591322, 2591323, 2591324, 2591325, 2591326, 2591327, 2591328, 2591329,
        2591330, 2591331, 2591332, 2591333, 2591334, 2591335, 2591336, 2591337, 2591338, 2591339,
        2591340, 2591341, 2591342, 2591343, 2591344, 2591345, 2591346, 2591347, 2591348, 2591349,
        2591350, 2591351, 2591352, 2591353, 2591354, 2591355, 2591356, 2591357, 2591358, 2591359,
        2591360, 2591361, 2591362, 2591363, 2591364, 2591365, 2591366, 2591367, 2591368, 2591369,
        2591370, 2591371, 2591372, 2591373, 2591374, 2591375, 2591376, 2591377, 2591378, 2591379,
        2591380, 2591381};

    public static short[] soulPotentials = new short[]{
        177, 102, 103, 104, 131, 132, 201, 101, 102, 103,
        104, 131, 132, 201, 105, 106, 107, 108, 133, 134,
        202, 105, 106, 107, 108, 133, 134, 202, 109, 110,
        111, 112, 135, 136, 203, 113, 114, 115, 116, 204,
        151, 152, 137, 403, 603, 121, 122, 123, 124, 206,
        155, 156, 139, 403, 603, 117, 118, 119, 120, 207,
        153, 154, 138, 403, 603, 167, 168, 169, 170, 208,
        171, 172, 177, 0, 0, 0, 0, 101, 102, 103,
        104, 131, 132, 201, 101, 102, 103, 104, 131, 132,
        201, 105, 106, 107, 108, 133, 134, 202, 105, 106,
        107, 108, 133, 134, 202, 109, 110, 111, 112, 135,
        136, 203, 113, 114, 115, 116, 204, 151, 152, 137,
        117, 118, 119, 120, 207, 153, 154, 138, 121, 122,
        123, 124, 206, 155, 156, 139, 101, 102, 103, 104,
        131, 132, 201, 163, 164, 165, 166, 210, 151, 152,
        175, 0, 101, 102, 103, 104, 131, 132, 201, 163,
        164, 165, 166, 210, 151, 152, 175, 167, 168, 169,
        170, 208, 171, 172, 177, 179, 180, 181, 182, 183,
        184, 201, 185, 186, 187, 188, 205, 153, 154, 189,
        0, 179, 180, 181, 182, 183, 184, 201, 185, 186,
        187, 188, 205, 153, 154, 189, 109, 110, 111, 112,
        135, 136, 203, 117, 118, 119, 120, 207, 153, 154,
        138, 0, 109, 110, 111, 112, 135, 136, 203, 117,
        118, 119, 120, 205, 153, 154, 138, 101, 102, 103,
        104, 131, 132, 201, 167, 168, 169, 170, 208, 173,
        172, 177, 0, 101, 102, 103, 104, 131, 132, 201,
        167, 168, 169, 170, 208, 173, 172, 177, 167, 168,
        169, 170, 208, 171, 172, 177, 0, 121, 186, 187,
        188, 205, 153, 154, 189, 0, 185, 186, 187, 188,
        207, 153, 154, 189, 0, 185, 186, 187, 188, 205,
        153, 154, 189, 0, 185, 186, 187, 188, 207, 153,
        154, 189, 0, 185, 186, 187, 188, 206, 153, 154,
        189, 0, 121, 186, 187, 188, 205, 153, 154, 189,
        185, 186, 187, 188, 205, 153, 154, 189, 185, 186,
        187, 188, 205, 153, 154, 189, 185, 186, 187, 188,
        207, 153, 154, 189, 185, 186, 187, 188, 206, 153,
        154, 189};

    public static short getSoulPotential(int soulid) {
        short potential = 0;
        for (int i = 0; i < soulItemid.length; i++) {
            if (soulItemid[i] == soulid) {
                potential = soulPotentials[i];
                break;
            }
        }
        return potential;
    }

    public static boolean isSoulSummonSkill(int skillid) {
        switch (skillid) {
            case 80001266:
            case 80001269:
            case 80001270:
            case 80001322:
            case 80001323:
            case 80001341:
            case 80001395:
            case 80001396:
            case 80001493:
            case 80001494:
            case 80001495:
            case 80001496:
            case 80001497:
            case 80001498:
            case 80001499:
            case 80001500:
            case 80001501:
            case 80001502:
            case 80001681:
            case 80001682:
            case 80001683:
            case 80001685:
            case 80001690:
            case 80001691:
            case 80001692:
            case 80001693:
            case 80001695:
            case 80001696:
            case 80001697:
            case 80001698:
            case 80001700:
            case 80001804:
            case 80001806:
            case 80001807:
            case 80001808:
            case 80001984:
            case 80001985:
            case 80002230:
            case 80002231:
            case 80002405:
            case 80002406:
            case 80002639:
                return true;
        }
        return false;
    }

    private static int[] dmgskinitem = new int[]{
        2431965, 2431966, 2431967, 2432084, 2432131, 2432153, 2432154, 2432207, 2432354, 2432355,
        2432465, 2432479, 2432526, 2432532, 2432592, 2432637, 2432638, 2432639, 2432640, 2432658,
        2432659, 2432660, 2432661, 2432710, 2432836, 2432972, 2432973, 2433063, 2433178, 2433456,
        2433631, 2433655, 2433715, 2433804, 2433913, 2433919, 2433980, 2433981, 2433990, 2434248,
        2434273, 2434274, 2434289, 2434390, 2434391, 2434528, 2434529, 2434530, 2434542, 2434546,
        2434574, 2434575, 2434654, 2434655, 2434661, 2434710, 2434734, 2434824, 2434950, 2434951,
        2435023, 2435024, 2435025, 2435026, 2435027, 2435028, 2435029, 2435030, 2435043, 2435044,
        2435045, 2435046, 2435047, 2435140, 2435141, 2435157, 2435158, 2435159, 2435160, 2435161,
        2435162, 2435166, 2435168, 2435169, 2435170, 2435171, 2435172, 2435173, 2435174, 2435175,
        2435176, 2435177, 2435179, 2435182, 2435184, 2435222, 2435293, 2435313, 2435316, 2435325,
        2435326, 2435331, 2435332, 2435333, 2435334, 2435408, 2435424, 2435425, 2435427, 2435428,
        2435429, 2435430, 2435431, 2435432, 2435433, 2435456, 2435461, 2435473, 2435474, 2435477,
        2435478, 2435490, 2435491, 2435493, 2435516, 2435521, 2435522, 2435523, 2435524, 2435538,
        2435972, 2436023, 2436024, 2436026, 2436027, 2436028, 2436029, 2435832, 2435833, 2435839,
        2435840, 2435841, 2436045, 2436083, 2436084, 2436085, 2436098, 2436103, 2436140, 2436182,
        2436206, 2436212, 2436215, 2435905, 2435906, 2435948, 2435949, 2435955, 2435956, 2436132,
        2436258, 2436259, 2436268, 2436400, 2436437, 2436521, 2436522, 2436528, 2436529, 2436553,
        2436560, 2436578, 2436596, 2436611, 2436612, 2436679, 2436680, 2436681, 2436682, 2436683,
        2436684, 2436785, 2436810, 2436951, 2436952, 2436953, 2437009, 2437022, 2437023, 2437024,
        2437164, 2437238, 2437239, 2437243, 2437482, 2437495, 2437496, 2437498, 2437515, 2437691,
        2437877, 2438143, 2438144, 2438352, 2438378, 2438379, 2438413, 2438415, 2438417, 2438419,
        2438460};

    private static int[] dmgskinnum = new int[]{
        0, 1, 2, 1, 3, 4, 5, 6, 7, 8,
        9, 10, 11, 12, 13, 5, 4, 11, 14, 5,
        4, 11, 14, 15, 16, 8, 17, 18, 20, 19,
        22, 22, 23, 24, 26, 15, 27, 28, 29, 34,
        35, 36, 37, 38, 39, 41, 42, 43, 47, 44,
        45, 46, 48, 49, 50, 51, 52, 53, 74, 75,
        7, 23, 26, 27, 34, 35, 36, 76, 77, 78,
        5, 79, 80, 81, 82, 85, 86, 87, 88, 89,
        84, 91, 24, 20, 15, 46, 1, 8, 11, 14,
        16, 17, 83, 90, 92, 93, 94, 95, 100, 48,
        49, 96, 97, 98, 99, 101, 109, 110, 102, 103,
        104, 112, 111, 113, 114, 105, 106, 96, 99, 34,
        12, 36, 35, 106, 115, 116, 117, 118, 119, 120,
        127, 128, 129, 130, 131, 132, 133, 121, 122, 123,
        124, 125, 134, 136, 137, 135, 1, 138, 140, 142,
        141, 143, 144, 1114, 1115, 1118, 1119, 1125, 1126, 1133,
        146, 147, 145, 148, 149, 150, 154, 152, 153, 155,
        156, 157, 160, 158, 159, 161, 162, 163, 164, 165,
        166, 167, 168, 169, 170, 171, 175, 172, 173, 174,
        176, 177, 179, 178, 184, 180, 181, 182, 183, 185,
        190, 191, 192, 193, 194, 195, 196, 197, 198, 199,
        200};

    public static boolean isOpen = false;

    public static int getDamageSkinNumberByItem(int itemid) {
        for (int i = 0; i < dmgskinitem.length; i++) {
            if (dmgskinitem[i] == itemid) {
                return dmgskinnum[i];
            }
        }
        return -1;
    }

    public static int getMonsterId(int mobid) {
        int ret = mobid;
        switch (mobid) {
            case 8641004:
            case 9800191:
                ret = 8641004;
                break;
            case 8641005:
            case 9010173:
            case 9800192:
                ret = 8641005;
                break;
            case 8641006:
            case 8641014:
            case 9101086:
            case 9800193:
                ret = 8641006;
                break;
            case 8641007:
            case 9800194:
                ret = 8641007;
                break;
            case 8642000:
            case 9010174:
            case 9800198:
            case 9833030:
            case 9833050:
            case 9833245:
                ret = 8642000;
                break;
            case 9800201:
            case 9833039:
            case 9833059:
            case 9833254:
                ret = 9800201;
                break;
            case 8642006:
            case 9800204:
            case 9833033:
            case 9833053:
            case 9833248:
                ret = 8642006;
                break;
            case 8642017:
            case 9800205:
            case 9833041:
            case 9833061:
            case 9833256:
                ret = 8642017;
                break;
            case 8642004:
            case 9800202:
            case 9833032:
            case 9833052:
                ret = 8642004;
                break;
            case 8642005:
            case 8642018:
            case 9800203:
            case 9833040:
                ret = 8642005;
                break;
            case 8642008:
            case 9800206:
            case 9833034:
            case 9833054:
            case 9833249:
                ret = 8642008;
                break;
            case 8642009:
            case 9800207:
            case 9833042:
            case 9833062:
            case 9833257:
                ret = 8642009;
                break;
            case 8642012:
            case 9800210:
            case 9833036:
                ret = 8642012;
                break;
            case 8642014:
            case 8642023:
            case 8642024:
            case 9800212:
            case 9833037:
            case 9833057:
            case 9833252:
                ret = 8642014;
                break;
            case 8642015:
            case 8642022:
            case 9800213:
            case 9833045:
            case 9833065:
                ret = 8642015;
                break;
            case 8642013:
            case 8642021:
            case 9800211:
            case 9833044:
                ret = 8642013;
                break;
            case 8643001:
            case 9010176:
                ret = 8643001;
                break;
            case 8643007:
            case 8643015:
            case 9010177:
                ret = 8643007;
                break;
            case 8644405:
            case 8644428:
            case 8644439:
                ret = 8644405;
                break;
            case 8644406:
            case 8644429:
            case 8644440:
            case 9010180:
                ret = 8644406;
                break;
            case 8644408:
            case 8644438:
            case 8644442:
                ret = 8644408;
                break;
            case 8644409:
            case 8644421:
            case 8644431:
            case 8644443:
                ret = 8644409;
                break;
            case 8644410:
            case 8644422:
            case 8644432:
            case 8644444:
                ret = 8644410;
                break;
            case 8644411:
            case 8644423:
            case 8644433:
            case 8644445:
                ret = 8644411;
                break;
            case 8644412:
            case 8644424:
            case 8644434:
            case 8644446:
            case 9010181:
                ret = 8644412;
                break;
            case 8644508:
            case 8644510:
                ret = 8644508;
                break;
            case 8644509:
            case 8644511:
            case 8880315:
            case 8880316:
            case 8880317:
            case 8880318:
            case 8880319:
            case 8880346:
            case 8880347:
            case 8880348:
            case 8880349:
            case 9010186:
                ret = 8644509;
                break;
            case 8644614:
            case 8644622:
            case 8644632:
            case 8644640:
                ret = 8644614;
                break;
            case 8644615:
            case 8644623:
            case 8644633:
            case 8644641:
                ret = 8644615;
                break;
            case 8644616:
            case 8644624:
            case 8644634:
            case 8644642:
                ret = 8644616;
                break;
            case 8644617:
            case 8644625:
            case 8644635:
            case 8644643:
                ret = 8644617;
                break;
            case 8644618:
            case 8644626:
            case 8644636:
            case 8644644:
                ret = 8644618;
                break;
            case 8644619:
            case 8644627:
            case 8644637:
            case 8644645:
                ret = 8644619;
                break;
            case 8644704:
            case 8644727:
            case 8644757:
            case 8644807:
            case 8644823:
            case 8644825:
            case 8644835:
            case 9833299:
            case 9833318:
                ret = 8644704;
                break;
            case 8644705:
            case 8644728:
            case 8644758:
            case 8644808:
            case 8644824:
            case 8644826:
            case 8644836:
            case 9833300:
            case 9833319:
                ret = 8644705;
                break;
            case 8644700:
            case 8644720:
            case 8644750:
            case 8644800:
            case 8644828:
            case 9833293:
            case 9833312:
                ret = 8644700;
                break;
            case 8644701:
            case 8644721:
            case 8644751:
            case 8644801:
            case 8644829:
            case 9833291:
            case 9833292:
            case 9833311:
                ret = 8644701;
                break;
            case 8645010:
            case 8645030:
                ret = 8645010;
                break;
            case 8645011:
            case 8645014:
            case 8645032:
            case 8645040:
                ret = 8645011;
                break;
        }
        return ret;
    }

    public static int getDamageSkinIdByNumber(int number) {
        for (int i = 0; i < dmgskinitem.length; i++) {
            if (dmgskinnum[i] == number) {
                return dmgskinitem[i];
            }
        }
        return 0;
    }

    public static int getItemIdbyNum(int num) {
        int ret = -1;
        switch (num) {
            case 230:
                ret = 2630214;
                break;
            case 229:
                ret = 2630178;
                break;
            case 228:
                ret = 2630222;
                break;
            case 227:
                ret = 2630137;
                break;
            case 225:
                ret = 2439927;
                break;
            case 224:
                ret = 2439925;
                break;
            case 223:
                ret = 2439769;
                break;
            case 222:
                ret = 2439686;
                break;
            case 221:
                ret = 2439684;
                break;
            case 219:
                ret = 2439665;
                break;
            case 218:
                ret = 2439617;
                break;
            case 217:
                ret = 2439572;
                break;
            case 216:
                ret = 2439408;
                break;
            case 215:
                ret = 2439395;
                break;
            case 214:
                ret = 2439393;
                break;
            case 213:
                ret = 2439381;
                break;
            case 212:
                ret = 2439338;
                break;
            case 211:
                ret = 2439337;
                break;
            case 210:
                ret = 2439336;
                break;
            case 209:
                ret = 2439298;
                break;
            case 208:
                ret = 2438871;
                break;
            case 207:
                ret = 2438885;
                break;
            case 206:
                ret = 2438881;
                break;
            case 205:
                ret = 2438713;
                break;
            case 204:
                ret = 2438672;
                break;
            case 203:
                ret = 2438637;
                break;
            case 202:
                ret = 2438530;
                break;
            case 201:
                ret = 2438492;
                break;
            case 200:
                ret = 2438485;
                break;
            case 199:
                ret = 2438419;
                break;
            case 198:
                ret = 2438417;
                break;
            case 197:
                ret = 2438415;
                break;
            case 196:
                ret = 2438413;
                break;
            case 195:
                ret = 2438379;
                break;
            case 194:
                ret = 2438378;
                break;
            case 193:
                ret = 2438353;
                break;
            case 192:
                ret = 2438147;
                break;
            case 191:
                ret = 2438146;
                break;
            case 190:
                ret = 2437877;
                break;
            case 189:
                ret = 2438315;
                break;
            case 188:
                ret = 2438314;
                break;
            case 187:
                ret = 2438313;
                break;
            case 186:
                ret = 2438312;
                break;
            case 185:
                ret = 2438311;
                break;
            case 184:
                ret = 2438310;
                break;
            case 183:
                ret = 2438309;
                break;
            case 182:
                ret = 2438308;
                break;
            case 181:
                ret = 2438307;
                break;
            case 180:
                ret = 2438306;
                break;
            case 179:
                ret = 2438305;
                break;
            case 178:
                ret = 2438304;
                break;
            case 177:
                ret = 2438303;
                break;
            case 176:
                ret = 2438302;
                break;
            case 175:
                ret = 2438301;
                break;
            case 174:
                ret = 2438300;
                break;
            case 173:
                ret = 2438299;
                break;
            case 172:
                ret = 2438298;
                break;
            case 171:
                ret = 2438297;
                break;
            case 170:
                ret = 2438296;
                break;
            case 169:
                ret = 2438295;
                break;
            case 168:
                ret = 2438294;
                break;
            case 167:
                ret = 2438293;
                break;
            case 166:
                ret = 2438292;
                break;
            case 165:
                ret = 2438291;
                break;
            case 164:
                ret = 2438290;
                break;
            case 163:
                ret = 2438289;
                break;
            case 162:
                ret = 2438288;
                break;
            case 161:
                ret = 2438287;
                break;
            case 160:
                ret = 2436596;
                break;
            case 159:
                ret = 2438286;
                break;
            case 158:
                ret = 2438285;
                break;
            case 157:
                ret = 2438284;
                break;
            case 156:
                ret = 2438283;
                break;
            case 155:
                ret = 2438282;
                break;
            case 154:
                ret = 2438281;
                break;
            case 153:
                ret = 2438280;
                break;
            case 152:
                ret = 2438279;
                break;
            case 150:
                ret = 2438278;
                break;
            case 149:
                ret = 2438277;
                break;
            case 148:
                ret = 2438276;
                break;
            case 147:
                ret = 2438275;
                break;
            case 146:
                ret = 2438274;
                break;
            case 145:
                ret = 2438273;
                break;
            case 144:
                ret = 2438272;
                break;
            case 143:
                ret = 2438271;
                break;
            case 142:
                ret = 2436182;
                break;
            case 141:
                ret = 2438270;
                break;
            case 140:
                ret = 2438269;
                break;
            case 139:
                ret = 2438268;
                break;
            case 138:
                ret = 2438267;
                break;
            case 137:
                ret = 2438266;
                break;
            case 136:
                ret = 2438265;
                break;
            case 135:
                ret = 2438264;
                break;
            case 134:
                ret = 2438263;
                break;
            case 133:
                ret = 2438262;
                break;
            case 132:
                ret = 2438261;
                break;
            case 131:
                ret = 2438260;
                break;
            case 130:
                ret = 2438259;
                break;
            case 129:
                ret = 2438258;
                break;
            case 128:
                ret = 2438257;
                break;
            case 127:
                ret = 2438256;
                break;
            case 126:
                ret = 2438255;
                break;
            case 125:
                ret = 2438254;
                break;
            case 124:
                ret = 2438253;
                break;
            case 123:
                ret = 2438252;
                break;
            case 122:
                ret = 2438251;
                break;
            case 121:
                ret = 2438250;
                break;
            case 120:
                ret = 2438249;
                break;
            case 119:
                ret = 2435524;
                break;
            case 118:
                ret = 2435523;
                break;
            case 117:
                ret = 2435521;
                break;
            case 115:
                ret = 2435516;
                break;
            case 114:
                ret = 2438248;
                break;
            case 113:
                ret = 2438247;
                break;
            case 112:
                ret = 2438246;
                break;
            case 111:
                ret = 2438245;
                break;
            case 110:
                ret = 2438244;
                break;
            case 109:
                ret = 2438243;
                break;
            case 108:
                ret = 2438242;
                break;
            case 107:
                ret = 2438241;
                break;
            case 106:
                ret = 2438240;
                break;
            case 105:
                ret = 2438239;
                break;
            case 104:
                ret = 2438238;
                break;
            case 103:
                ret = 2438237;
                break;
            case 102:
                ret = 2438236;
                break;
            case 101:
                ret = 2438235;
                break;
            case 100:
                ret = 2438234;
                break;
            case 99:
                ret = 2438233;
                break;
            case 98:
                ret = 2438232;
                break;
            case 97:
                ret = 2438231;
                break;
            case 96:
                ret = 2438230;
                break;
            case 95:
                ret = 2438229;
                break;
            case 94:
                ret = 2438228;
                break;
            case 93:
                ret = 2438227;
                break;
            case 92:
                ret = 2438226;
                break;
            case 91:
                ret = 2438225;
                break;
            case 90:
                ret = 2438224;
                break;
            case 89:
                ret = 2438223;
                break;
            case 88:
                ret = 2438222;
                break;
            case 87:
                ret = 2438221;
                break;
            case 86:
                ret = 2438220;
                break;
            case 85:
                ret = 2438219;
                break;
            case 84:
                ret = 2438218;
                break;
            case 83:
                ret = 2438217;
                break;
            case 82:
                ret = 2438216;
                break;
            case 81:
                ret = 2438215;
                break;
            case 80:
                ret = 2438214;
                break;
            case 79:
                ret = 2438213;
                break;
            case 78:
                ret = 2438212;
                break;
            case 77:
                ret = 2438211;
                break;
            case 76:
                ret = 2438210;
                break;
            case 75:
                ret = 2438209;
                break;
            case 74:
                ret = 2438208;
                break;
            case 73:
                ret = 2438200;
                break;
            case 72:
                ret = 2438199;
                break;
            case 71:
                ret = 2438198;
                break;
            case 70:
                ret = 2438197;
                break;
            case 69:
                ret = 2438196;
                break;
            case 68:
                ret = 2438195;
                break;
            case 67:
                ret = 2438194;
                break;
            case 66:
                ret = 2438192;
                break;
            case 65:
                ret = 2438190;
                break;
            case 64:
                ret = 2438189;
                break;
            case 63:
                ret = 2438188;
                break;
            case 62:
                ret = 2438185;
                break;
            case 61:
                ret = 2438184;
                break;
            case 60:
                ret = 2438183;
                break;
            case 59:
                ret = 2438181;
                break;
            case 58:
                ret = 2438171;
                break;
            case 57:
                ret = 2438166;
                break;
            case 56:
                ret = 2438164;
                break;
            case 55:
                ret = 2438161;
                break;
            case 54:
                ret = 2435172;
                break;
            case 53:
                ret = 2438207;
                break;
            case 52:
                ret = 2438206;
                break;
            case 51:
                ret = 2438205;
                break;
            case 50:
                ret = 2438204;
                break;
            case 49:
                ret = 2438203;
                break;
            case 48:
                ret = 2438202;
                break;
            case 47:
                ret = 2438201;
                break;
            case 39:
                ret = 2438193;
                break;
            case 37:
                ret = 2438191;
                break;
            case 33:
                ret = 2438167;
                break;
            case 32:
                ret = 2438174;
                break;
            case 31:
                ret = 2438170;
                break;
            case 30:
                ret = 2438163;
                break;
            case 29:
                ret = 2438187;
                break;
            case 28:
                ret = 2438186;
                break;
            case 24:
                ret = 2438182;
                break;
            case 22:
                ret = 2438180;
                break;
            case 21:
                ret = 2438179;
                break;
            case 20:
                ret = 2438178;
                break;
            case 18:
                ret = 2438177;
                break;
            case 17:
                ret = 2438176;
                break;
            case 16:
                ret = 2438175;
                break;
            case 14:
                ret = 2438173;
                break;
            case 13:
                ret = 2438172;
                break;
            case 10:
                ret = 2438169;
                break;
            case 9:
                ret = 2438168;
                break;
            case 6:
                ret = 2438165;
                break;
            case 3:
                ret = 2438162;
                break;
            case 1:
                ret = 2438160;
                break;
            case 0:
                ret = 2438159;
                break;
        }
        return ret;
    }

    public static int getRidingNum(int itemid) {
        int ret = -1;
        switch (itemid) {
            case 2432149:
                ret = 80001398;
                return ret;
            case 2432151:
                ret = 80001400;
                return ret;
            case 2432170:
                ret = 80001262;
                return ret;
            case 2432218:
                ret = 80001413;
                return ret;
            case 2432295:
                ret = 80001423;
                return ret;
            case 2432309:
                ret = 80001404;
                return ret;
            case 2432328:
                ret = 80001435;
                return ret;
            case 2432347:
                ret = 80001440;
                return ret;
            case 2432348:
                ret = 80001441;
                return ret;
            case 2432349:
                ret = 80001442;
                return ret;
            case 2432350:
                ret = 80001443;
                return ret;
            case 2432351:
                ret = 80001444;
                return ret;
            case 2432359:
                ret = 80001445;
                return ret;
            case 2432361:
                ret = 80001447;
                return ret;
            case 2432418:
                ret = 80001454;
                return ret;
            case 2432431:
                ret = 80001480;
                return ret;
            case 2432433:
                ret = 80001482;
                return ret;
            case 2432449:
                ret = 80001484;
                return ret;
            case 2432527:
                ret = 80001490;
                return ret;
            case 2432528:
                ret = 80001491;
                return ret;
            case 2432552:
                ret = 80001492;
                return ret;
            case 2432580:
                ret = 80001503;
                return ret;
            case 2432582:
                ret = 80001505;
                return ret;
            case 2432635:
                ret = 80001517;
                return ret;
            case 2432645:
                ret = 80001531;
                return ret;
            case 2432653:
                ret = 80001533;
                return ret;
            case 2432724:
                ret = 80001549;
                return ret;
            case 2432733:
                ret = 80001552;
                return ret;
            case 2432735:
                ret = 80001550;
                return ret;
            case 2432751:
                ret = 80001554;
                return ret;
            case 2432806:
                ret = 80001557;
                return ret;
            case 2432994:
                ret = 80001561;
                return ret;
            case 2432995:
                ret = 80001562;
                return ret;
            case 2432996:
                ret = 80001563;
                return ret;
            case 2432997:
                ret = 80001564;
                return ret;
            case 2432998:
                ret = 80001565;
                return ret;
            case 2432999:
                ret = 80001566;
                return ret;
            case 2433000:
                ret = 80001567;
                return ret;
            case 2433001:
                ret = 80001568;
                return ret;
            case 2433002:
                ret = 80001569;
                return ret;
            case 2433003:
                ret = 80001570;
                return ret;
            case 2431949:
                ret = 80001336;
                return ret;
            case 2433051:
                ret = 80001582;
                return ret;
            case 2433053:
                ret = 80001584;
                return ret;
            case 2433499:
                ret = 80001639;
                return ret;
            case 2433501:
                ret = 80001640;
                return ret;
            case 2433458:
                ret = 80001642;
                return ret;
            case 2433459:
                ret = 80001643;
                return ret;
            case 2433460:
                ret = 80001644;
                return ret;
            case 2430633:
                ret = 80001001;
                return ret;
            case 2431424:
                ret = 80001244;
                return ret;
            case 2433658:
                ret = 80001703;
                return ret;
            case 2433718:
                ret = 80001019;
                return ret;
            case 2433735:
                ret = 80001707;
                return ret;
            case 2433736:
                ret = 80001708;
                return ret;
            case 2433809:
                ret = 80001711;
                return ret;
            case 2433811:
                ret = 80001713;
                return ret;
            case 2433932:
                ret = 80001763;
                return ret;
            case 2433946:
                ret = 80001764;
                return ret;
            case 2433948:
                ret = 80001766;
                return ret;
            case 2433992:
                ret = 80001769;
                return ret;
            case 2434013:
                ret = 80001775;
                return ret;
            case 2431473:
                ret = 80001257;
                return ret;
            case 2434077:
                ret = 80001776;
                return ret;
            case 2434275:
                ret = 80001785;
                return ret;
            case 2434277:
                ret = 80001786;
                return ret;
            case 2434377:
                ret = 80001792;
                return ret;
            case 2434379:
                ret = 80001790;
                return ret;
            case 2434515:
                ret = 80001811;
                return ret;
            case 2434517:
                ret = 80001813;
                return ret;
            case 2434525:
                ret = 80001814;
                return ret;
            case 2434526:
                ret = 80001867;
                return ret;
            case 2434527:
                ret = 80001868;
                return ret;
            case 2434580:
                ret = 80001870;
                return ret;
            case 2431415:
                ret = 80001872;
                return ret;
            case 2434649:
                ret = 80001918;
                return ret;
            case 2434674:
                ret = 80001920;
                return ret;
            case 2434728:
                ret = 80001933;
                return ret;
            case 2434735:
                ret = 80001921;
                return ret;
            case 2434737:
                ret = 80001923;
                return ret;
            case 2434761:
                ret = 80001934;
                return ret;
            case 2434762:
                ret = 80001935;
                return ret;
            case 2434967:
                ret = 80001942;
                return ret;
            case 2435089:
                ret = 80001956;
                return ret;
            case 2435091:
                ret = 80001958;
                return ret;
            case 2435112:
                ret = 80001953;
                return ret;
            case 2435113:
                ret = 80001954;
                return ret;
            case 2435114:
                ret = 80001955;
                return ret;
            case 2435203:
                ret = 80001975;
                return ret;
            case 2435205:
                ret = 80001977;
                return ret;
            case 2435296:
                ret = 80001980;
                return ret;
            case 2435298:
                ret = 80001982;
                return ret;
            case 2435440:
                ret = 80001988;
                return ret;
            case 2435441:
                ret = 80001989;
                return ret;
            case 2435442:
                ret = 80001990;
                return ret;
            case 2435476:
                ret = 80001991;
                return ret;
            case 2435517:
                ret = 80001993;
                return ret;
            case 2435720:
                ret = 80001995;
                return ret;
            case 2435722:
                ret = 80001997;
                return ret;
            case 2435842:
                ret = 80002219;
                return ret;
            case 2435843:
                ret = 80002220;
                return ret;
            case 2435844:
                ret = 80002221;
                return ret;
            case 2435845:
                ret = 80002222;
                return ret;
            case 2435965:
                ret = 80002223;
                return ret;
            case 2435967:
                ret = 80002225;
                return ret;
            case 2435986:
                ret = 80002202;
                return ret;
            case 2436030:
                ret = 80002204;
                return ret;
            case 2436031:
                ret = 80002229;
                return ret;
            case 2436079:
                ret = 80001246;
                return ret;
            case 2436080:
                ret = 80002233;
                return ret;
            case 2436081:
                ret = 80002234;
                return ret;
            case 2436126:
                ret = 80002235;
                return ret;
            case 2436183:
                ret = 80002236;
                return ret;
            case 2436185:
                ret = 80002238;
                return ret;
            case 2436292:
                ret = 80002240;
                return ret;
            case 2436294:
                ret = 80002242;
                return ret;
            case 2436405:
                ret = 80002248;
                return ret;
            case 2436407:
                ret = 80002250;
                return ret;
            case 2436523:
                ret = 80002270;
                return ret;
            case 2436524:
                ret = 80002259;
                return ret;
            case 2436525:
                ret = 80002258;
                return ret;
            case 2436597:
                ret = 80002261;
                return ret;
            case 2436599:
                ret = 80002262;
                return ret;
            case 2436610:
                ret = 80002265;
                return ret;
            case 2436648:
                ret = 80002266;
                return ret;
            case 2436714:
                ret = 80002270;
                return ret;
            case 2436715:
                ret = 80002271;
                return ret;
            case 2436716:
                ret = 80002272;
                return ret;
            case 2436728:
                ret = 80002278;
                return ret;
            case 2436730:
                ret = 80002277;
                return ret;
            case 2436778:
                ret = 80002287;
                return ret;
            case 2436780:
                ret = 80002289;
                return ret;
            case 2436837:
                ret = 80002295;
                return ret;
            case 2436839:
                ret = 80002297;
                return ret;
            case 2436957:
                ret = 80002302;
                return ret;
            case 2437026:
                ret = 80002304;
                return ret;
            case 2437040:
                ret = 80002305;
                return ret;
            case 2437042:
                ret = 80002307;
                return ret;
            case 2437123:
                ret = 80002314;
                return ret;
            case 2437125:
                ret = 80002315;
                return ret;
            case 2437240:
                ret = 80002318;
                return ret;
            case 2437259:
                ret = 80002319;
                return ret;
            case 2437261:
                ret = 80002321;
                return ret;
            case 2437497:
                ret = 80002335;
                return ret;
            case 2437623:
                ret = 80002345;
                return ret;
            case 2437625:
                ret = 80002347;
                return ret;
            case 2437719:
                ret = 80002358;
                return ret;
            case 2437721:
                ret = 80002356;
                return ret;
            case 2437737:
                ret = 80002354;
                return ret;
            case 2437738:
                ret = 80002361;
                return ret;
            case 2437794:
                ret = 80002355;
                return ret;
            case 2437809:
                ret = 80002367;
                return ret;
            case 2437852:
                ret = 80001002;
                return ret;
            case 2437923:
                ret = 80002369;
                return ret;
            case 2438136:
                ret = 80002382;
                return ret;
            case 2438137:
                ret = 80002383;
                return ret;
            case 2438138:
                ret = 80002384;
                return ret;
            case 2438139:
                ret = 80001005;
                return ret;
            case 2438340:
                ret = 80002375;
                return ret;
            case 2438380:
                ret = 80002400;
                return ret;
            case 2438382:
                ret = 80002402;
                return ret;
            case 2438373:
                ret = 80002392;
                return ret;
            case 2430935:
                ret = 80001186;
                return ret;
            case 2430906:
                ret = 80001173;
                return ret;
            case 2430908:
                ret = 80001175;
                return ret;
            case 2431541:
                ret = 80001243;
                return ret;
            case 2431528:
                ret = 80001244;
                return ret;
            case 2431529:
                ret = 80001245;
                return ret;
            case 2431474:
                ret = 80001258;
                return ret;
            case 2431073:
                ret = 80001452;
                return ret;
            case 2430190:
                ret = 80001021;
                return ret;
            case 2430634:
                ret = 80001006;
                return ret;
            case 2431494:
                ret = 80001051;
                return ret;
            case 2431495:
                ret = 80001001;
                return ret;
            case 2430053:
                ret = 80001004;
                return ret;
            case 2431498:
                ret = 80001007;
                return ret;
            case 2430057:
                ret = 80001009;
                return ret;
            case 2431500:
                ret = 80001450;
                return ret;
            case 2431501:
                ret = 80001003;
                return ret;
            case 2431503:
                ret = 80001030;
                return ret;
            case 2431504:
                ret = 80001031;
                return ret;
            case 2431505:
                ret = 80001032;
                return ret;
            case 2430149:
                ret = 80001020;
                return ret;
            case 2431745:
                ret = 80001278;
                return ret;
            case 2431757:
                ret = 80001285;
                return ret;
            case 2431760:
                ret = 80001291;
                return ret;
            case 2431764:
                ret = 80001289;
                return ret;
            case 2431765:
                ret = 80001290;
                return ret;
            case 2431797:
                ret = 80001039;
                return ret;
            case 2431799:
                ret = 80001302;
                return ret;
            case 2431898:
                ret = 80001324;
                return ret;
            case 2430521:
                ret = 80001044;
                return ret;
            case 2431915:
                ret = 80001327;
                return ret;
            case 2432003:
                ret = 80001344;
                return ret;
            case 2432007:
                ret = 80001345;
                return ret;
            case 2432015:
                ret = 80001333;
                return ret;
            case 2432029:
                ret = 80001346;
                return ret;
            case 2432030:
                ret = 80001347;
                return ret;
            case 2432031:
                ret = 80001348;
                return ret;
            case 2432078:
                ret = 80001353;
                return ret;
            case 2432085:
                ret = 80001355;
                return ret;
            case 2430091:
                ret = 80001014;
                return ret;
            case 2430506:
                ret = 80001082;
                return ret;
            case 2430610:
                ret = 80001022;
                return ret;
            case 2430937:
                ret = 80001144;
                return ret;
            case 2430938:
                ret = 80001148;
                return ret;
            case 2430939:
                ret = 80001149;
                return ret;
            case 2430794:
                ret = 80001163;
                return ret;
            case 2430907:
                ret = 80001174;
                return ret;
            case 2430932:
                ret = 80001183;
                return ret;
            case 2430933:
                ret = 80001184;
                return ret;
            case 2430934:
                ret = 80001185;
                return ret;
            case 2430936:
                ret = 80001187;
                return ret;
            case 2431137:
                ret = 80001198;
                return ret;
            case 2431135:
                ret = 80001220;
                return ret;
            case 2431136:
                ret = 80001221;
                return ret;
            case 2431267:
                ret = 80001228;
                return ret;
            case 2434079:
                ret = 80001778;
                return ret;
            case 2433454:
                ret = 80001023;
                return ret;
            case 2433349:
                ret = 80001628;
                return ret;
            case 2433347:
                ret = 80001625;
                return ret;
            case 2630261:
                ret = 80002699;
                return ret;
            case 2439909:
                ret = 80002660;
                return ret;
            case 2439913:
                ret = 80002664;
                return ret;
            case 2630240:
                ret = 80002698;
                return ret;
            case 2630116:
                ret = 80002668;
                return ret;
            case 2439933:
                ret = 80002667;
                return ret;
            case 2433345:
                ret = 80001623;
                return ret;
            case 2433276:
                ret = 80001621;
                return ret;
            case 2433274:
                ret = 80001620;
                return ret;
            case 2433272:
                ret = 80001617;
                return ret;
            case 2432500:
                ret = 80001510;
                return ret;
            case 2432498:
                ret = 80001508;
                return ret;
            case 2432293:
                ret = 80001421;
                return ret;
            case 2432291:
                ret = 80001420;
                return ret;
            case 2432216:
                ret = 80001412;
                return ret;
            case 2438408:
                ret = 80002417;
                return ret;
            case 2438409:
                ret = 80002418;
                return ret;
            case 2438486:
                ret = 80002429;
                return ret;
            case 2438488:
                ret = 80002432;
                return ret;
            case 2438493:
                ret = 80002427;
                return ret;
            case 2438494:
                ret = 80002425;
                return ret;
            case 2438638:
                ret = 80002433;
                return ret;
            case 2438640:
                ret = 80002436;
                return ret;
            case 2438657:
                ret = 80002437;
                return ret;
            case 2438715:
                ret = 80002439;
                return ret;
            case 2438743:
                ret = 80002441;
                return ret;
            case 2438745:
                ret = 80002443;
                return ret;
            case 2438882:
                ret = 80002446;
                return ret;
            case 2438886:
                ret = 80002447;
                return ret;
            case 2439034:
                ret = 80002448;
                return ret;
            case 2439036:
                ret = 80002450;
                return ret;
            case 2439127:
                ret = 80002454;
                return ret;
            case 2439144:
                ret = 80002424;
                return ret;
            case 2439266:
                ret = 80002545;
                return ret;
            case 2439278:
                ret = 80002546;
                return ret;
            case 2439295:
                ret = 80002547;
                return ret;
            case 2439329:
                ret = 80002572;
                return ret;
            case 2439331:
                ret = 80002573;
                return ret;
            case 2439406:
                ret = 80002622;
                return ret;
            case 2439443:
                ret = 80002585;
                return ret;
            case 2439484:
                ret = 80002628;
                return ret;
            case 2439486:
                ret = 80002630;
                return ret;
            case 2439666:
                ret = 80002594;
                return ret;
            case 2439667:
                ret = 80002595;
                return ret;
            case 2439675:
                ret = 80002648;
                return ret;
            case 2439677:
                ret = 80002650;
                return ret;
            case 2630279:
                ret = 80002702;
                return ret;
            case 2630386:
                ret = 80002712;
                return ret;
            case 2630387:
                ret = 80002713;
                return ret;
            case 2630448:
                ret = 80002714;
                return ret;
            case 2630451:
                ret = 80002715;
                return ret;
            case 2630452:
                ret = 80002716;
                return ret;
            case 2630476:
                ret = 80002735;
                return ret;
            case 2630488:
                ret = 80002738;
                return ret;
            case 2630563:
                ret = 80002740;
                return ret;
            case 2630763:
                ret = 80002742;
                return ret;
            case 2630764:
                ret = 80002743;
                return ret;
            case 2630765:
                ret = 80002744;
                return ret;
            case 263076:
                ret = 80002748;
                return ret;
            case 2630570:
                ret = 80002752;
                return ret;
            case 2630573:
                ret = 80002754;
                return ret;
            case 2630575:
                ret = 80002756;
                return ret;
            case 2630576:
                ret = 80002757;
                return ret;
            case 2430039:
                ret = 80002824;
                return ret;
            case 2630917:
                ret = 80002831;
                return ret;
            case 2630913:
                ret = 80002843;
                return ret;
            case 2630914:
                ret = 80002844;
                return ret;
            case 2630918:
                ret = 80002845;
                return ret;
            case 2630919:
                ret = 80002846;
                return ret;
            case 2630971:
                ret = 80002853;
                return ret;
            case 5120198:
                ret = 80002854;
                return ret;
            case 2631140:
                ret = 80002856;
                return ret;
            case 2631136:
                ret = 80002858;
                return ret;
            case 2631190:
                ret = 80002859;
                return ret;
            case 2631191:
                ret = 80002860;
                return ret;
        }
        ret = -1;
        return ret;
    }

    public static int getRidingItemIdbyNum(int num) {
        int ret = -1;
        switch (num) {
            case 80001398:
                ret = 2432149;
                break;
            case 80001400:
                ret = 2432151;
                break;
            case 80001262:
                ret = 2432170;
                break;
            case 80001413:
                ret = 2432218;
                break;
            case 80001423:
                ret = 2432295;
                break;
            case 80001404:
                ret = 2432309;
                break;
            case 80001435:
                ret = 2432328;
                break;
            case 80001440:
                ret = 2432347;
                break;
            case 80001441:
                ret = 2432348;
                break;
            case 80001442:
                ret = 2432349;
                break;
            case 80001443:
                ret = 2432350;
                break;
            case 80001444:
                ret = 2432351;
                break;
            case 80001445:
                ret = 2432359;
                break;
            case 80001447:
                ret = 2432361;
                break;
            case 80001454:
                ret = 2432418;
                break;
            case 80001480:
                ret = 2432431;
                break;
            case 80001482:
                ret = 2432433;
                break;
            case 80001484:
                ret = 2432449;
                break;
            case 80001490:
                ret = 2432527;
                break;
            case 80001491:
                ret = 2432528;
                break;
            case 80001492:
                ret = 2432552;
                break;
            case 80001503:
                ret = 2432580;
                break;
            case 80001505:
                ret = 2432582;
                break;
            case 80001517:
                ret = 2432635;
                break;
            case 80001531:
                ret = 2432645;
                break;
            case 80002699:
                ret = 2630261;
                break;
            case 80002660:
                ret = 2439909;
                break;
            case 80002664:
                ret = 2439913;
                break;
            case 80002698:
                ret = 2630240;
                break;
            case 80002668:
                ret = 2630116;
                break;
            case 80002667:
                ret = 2439933;
                break;
            case 80001533:
                ret = 2432653;
                break;
            case 80001549:
                ret = 2432724;
                break;
            case 80001552:
                ret = 2432733;
                break;
            case 80001550:
                ret = 2432735;
                break;
            case 80001554:
                ret = 2432751;
                break;
            case 80001557:
                ret = 2432806;
                break;
            case 80001561:
                ret = 2432994;
                break;
            case 80001562:
                ret = 2432995;
                break;
            case 80001563:
                ret = 2432996;
                break;
            case 80001564:
                ret = 2432997;
                break;
            case 80001565:
                ret = 2432998;
                break;
            case 80001566:
                ret = 2432999;
                break;
            case 80001567:
                ret = 2433000;
                break;
            case 80001568:
                ret = 2433001;
                break;
            case 80001569:
                ret = 2433002;
                break;
            case 80001570:
                ret = 2433003;
                break;
            case 80001336:
                ret = 2431949;
                break;
            case 80001582:
                ret = 2433051;
                break;
            case 80001584:
                ret = 2433053;
                break;
            case 80001639:
                ret = 2433499;
                break;
            case 80001640:
                ret = 2433501;
                break;
            case 80001642:
                ret = 2433458;
                break;
            case 80001643:
                ret = 2433459;
                break;
            case 80001644:
                ret = 2433460;
                break;
            case 80001001:
                ret = 2430633;
                break;
            case 80001244:
                ret = 2431424;
                break;
            case 80001703:
                ret = 2433658;
                break;
            case 80001019:
                ret = 2433718;
                break;
            case 80001707:
                ret = 2433735;
                break;
            case 80001708:
                ret = 2433736;
                break;
            case 80001711:
                ret = 2433809;
                break;
            case 80001713:
                ret = 2433811;
                break;
            case 80001763:
                ret = 2433932;
                break;
            case 80001764:
                ret = 2433946;
                break;
            case 80001766:
                ret = 2433948;
                break;
            case 80001769:
                ret = 2433992;
                break;
            case 80001775:
                ret = 2434013;
                break;
            case 80001257:
                ret = 2431473;
                break;
            case 80001776:
                ret = 2434077;
                break;
            case 80001785:
                ret = 2434275;
                break;
            case 80001786:
                ret = 2434277;
                break;
            case 80001792:
                ret = 2434377;
                break;
            case 80001790:
                ret = 2434379;
                break;
            case 80001811:
                ret = 2434515;
                break;
            case 80001813:
                ret = 2434517;
                break;
            case 80001814:
                ret = 2434525;
                break;
            case 80001867:
                ret = 2434526;
                break;
            case 80001868:
                ret = 2434527;
                break;
            case 80001870:
                ret = 2434580;
                break;
            case 80001872:
                ret = 2431415;
                break;
            case 80001918:
                ret = 2434649;
                break;
            case 80001920:
                ret = 2434674;
                break;
            case 80001933:
                ret = 2434728;
                break;
            case 80001921:
                ret = 2434735;
                break;
            case 80001923:
                ret = 2434737;
                break;
            case 80001934:
                ret = 2434761;
                break;
            case 80001935:
                ret = 2434762;
                break;
            case 80001942:
                ret = 2434967;
                break;
            case 80001956:
                ret = 2435089;
                break;
            case 80001958:
                ret = 2435091;
                break;
            case 80001953:
                ret = 2435112;
                break;
            case 80001954:
                ret = 2435113;
                break;
            case 80001955:
                ret = 2435114;
                break;
            case 80001975:
                ret = 2435203;
                break;
            case 80001977:
                ret = 2435205;
                break;
            case 80001980:
                ret = 2435296;
                break;
            case 80001982:
                ret = 2435298;
                break;
            case 80001988:
                ret = 2435440;
                break;
            case 80001989:
                ret = 2435441;
                break;
            case 80001990:
                ret = 2435442;
                break;
            case 80001991:
                ret = 2435476;
                break;
            case 80001993:
                ret = 2435517;
                break;
            case 80001995:
                ret = 2435720;
                break;
            case 80001997:
                ret = 2435722;
                break;
            case 80002219:
                ret = 2435842;
                break;
            case 80002220:
                ret = 2435843;
                break;
            case 80002221:
                ret = 2435844;
                break;
            case 80002222:
                ret = 2435845;
                break;
            case 80002223:
                ret = 2435965;
                break;
            case 80002225:
                ret = 2435967;
                break;
            case 80002202:
                ret = 2435986;
                break;
            case 80002204:
                ret = 2436030;
                break;
            case 80002229:
                ret = 2436031;
                break;
            case 80001246:
                ret = 2436079;
                break;
            case 80002233:
                ret = 2436080;
                break;
            case 80002234:
                ret = 2436081;
                break;
            case 80002235:
                ret = 2436126;
                break;
            case 80002236:
                ret = 2436183;
                break;
            case 80002238:
                ret = 2436185;
                break;
            case 80002240:
                ret = 2436292;
                break;
            case 80002242:
                ret = 2436294;
                break;
            case 80002248:
                ret = 2436405;
                break;
            case 80002250:
                ret = 2436407;
                break;
            case 80002270:
                ret = 2436523;
                break;
            case 80002259:
                ret = 2436524;
                break;
            case 80002258:
                ret = 2436525;
                break;
            case 80002261:
                ret = 2436597;
                break;
            case 80002262:
                ret = 2436599;
                break;
            case 80002265:
                ret = 2436610;
                break;
            case 80002266:
                ret = 2436648;
                break;
            case 80002271:
                ret = 2436715;
                break;
            case 80002272:
                ret = 2436716;
                break;
            case 80002278:
                ret = 2436728;
                break;
            case 80002277:
                ret = 2436730;
                break;
            case 80002287:
                ret = 2436778;
                break;
            case 80002289:
                ret = 2436780;
                break;
            case 80002295:
                ret = 2436837;
                break;
            case 80002297:
                ret = 2436839;
                break;
            case 80002302:
                ret = 2436957;
                break;
            case 80002304:
                ret = 2437026;
                break;
            case 80002305:
                ret = 2437040;
                break;
            case 80002307:
                ret = 2437042;
                break;
            case 80002314:
                ret = 2437123;
                break;
            case 80002315:
                ret = 2437125;
                break;
            case 80002318:
                ret = 2437240;
                break;
            case 80002319:
                ret = 2437259;
                break;
            case 80002321:
                ret = 2437261;
                break;
            case 80002335:
                ret = 2437497;
                break;
            case 80002345:
                ret = 2437623;
                break;
            case 80002347:
                ret = 2437625;
                break;
            case 80002358:
                ret = 2437719;
                break;
            case 80002356:
                ret = 2437721;
                break;
            case 80002354:
                ret = 2437737;
                break;
            case 80002361:
                ret = 2437738;
                break;
            case 80002355:
                ret = 2437794;
                break;
            case 80002367:
                ret = 2437809;
                break;
            case 80001002:
                ret = 2437852;
                break;
            case 80002369:
                ret = 2437923;
                break;
            case 80002382:
                ret = 2438136;
                break;
            case 80002383:
                ret = 2438137;
                break;
            case 80002384:
                ret = 2438138;
                break;
            case 80001005:
                ret = 2438139;
                break;
            case 80002375:
                ret = 2438340;
                break;
            case 80002400:
                ret = 2438380;
                break;
            case 80002402:
                ret = 2438382;
                break;
            case 80002392:
                ret = 2438373;
                break;
            case 80001186:
                ret = 2430935;
                break;
            case 80001173:
                ret = 2430906;
                break;
            case 80001175:
                ret = 2430908;
                break;
            case 80001243:
                ret = 2431541;
                break;
            case 80001245:
                ret = 2431529;
                break;
            case 80001258:
                ret = 2431474;
                break;
            case 80001452:
                ret = 2431073;
                break;
            case 80001021:
                ret = 2430190;
                break;
            case 80001006:
                ret = 2430634;
                break;
            case 80001051:
                ret = 2431494;
                break;
            case 80001004:
                ret = 2430053;
                break;
            case 80001007:
                ret = 2431498;
                break;
            case 80001009:
                ret = 2430057;
                break;
            case 80001450:
                ret = 2431500;
                break;
            case 80001003:
                ret = 2431501;
                break;
            case 80001030:
                ret = 2431503;
                break;
            case 80001031:
                ret = 2431504;
                break;
            case 80001032:
                ret = 2431505;
                break;
            case 80001020:
                ret = 2430149;
                break;
            case 80001278:
                ret = 2431745;
                break;
            case 80001285:
                ret = 2431757;
                break;
            case 80001291:
                ret = 2431760;
                break;
            case 80001289:
                ret = 2431764;
                break;
            case 80001290:
                ret = 2431765;
                break;
            case 80001039:
                ret = 2431797;
                break;
            case 80001302:
                ret = 2431799;
                break;
            case 80001324:
                ret = 2431898;
                break;
            case 80001044:
                ret = 2430521;
                break;
            case 80001327:
                ret = 2431915;
                break;
            case 80001344:
                ret = 2432003;
                break;
            case 80001345:
                ret = 2432007;
                break;
            case 80001333:
                ret = 2432015;
                break;
            case 80001346:
                ret = 2432029;
                break;
            case 80001347:
                ret = 2432030;
                break;
            case 80001348:
                ret = 2432031;
                break;
            case 80001353:
                ret = 2432078;
                break;
            case 80001355:
                ret = 2432085;
                break;
            case 80001014:
                ret = 2430091;
                break;
            case 80001082:
                ret = 2430506;
                break;
            case 80001022:
                ret = 2430610;
                break;
            case 80001144:
                ret = 2430937;
                break;
            case 80001148:
                ret = 2430938;
                break;
            case 80001149:
                ret = 2430939;
                break;
            case 80001163:
                ret = 2430794;
                break;
            case 80001174:
                ret = 2430907;
                break;
            case 80001183:
                ret = 2430932;
                break;
            case 80001184:
                ret = 2430933;
                break;
            case 80001185:
                ret = 2430934;
                break;
            case 80001187:
                ret = 2430936;
                break;
            case 80001198:
                ret = 2431137;
                break;
            case 80001220:
                ret = 2431135;
                break;
            case 80001221:
                ret = 2431136;
                break;
            case 80001228:
                ret = 2431267;
                break;
            case 80001778:
                ret = 2434079;
                break;
            case 80001023:
                ret = 2433454;
                break;
            case 80001628:
                ret = 2433349;
                break;
            case 80001625:
                ret = 2433347;
                break;
            case 80001623:
                ret = 2433345;
                break;
            case 80001621:
                ret = 2433276;
                break;
            case 80001620:
                ret = 2433274;
                break;
            case 80001617:
                ret = 2433272;
                break;
            case 80001510:
                ret = 2432500;
                break;
            case 80001508:
                ret = 2432498;
                break;
            case 80001421:
                ret = 2432293;
                break;
            case 80001420:
                ret = 2432291;
                break;
            case 80001412:
                ret = 2432216;
                break;
            case 80002417:
                ret = 2438408;
                break;
            case 80002418:
                ret = 2438409;
                break;
            case 80002429:
                ret = 2438486;
                break;
            case 80002432:
                ret = 2438488;
                break;
            case 80002427:
                ret = 2438493;
                break;
            case 80002425:
                ret = 2438494;
                break;
            case 80002433:
                ret = 2438638;
                break;
            case 80002436:
                ret = 2438640;
                break;
            case 80002437:
                ret = 2438657;
                break;
            case 80002439:
                ret = 2438715;
                break;
            case 80002441:
                ret = 2438743;
                break;
            case 80002443:
                ret = 2438745;
                break;
            case 80002446:
                ret = 2438882;
                break;
            case 80002447:
                ret = 2438886;
                break;
            case 80002448:
                ret = 2439034;
                break;
            case 80002450:
                ret = 2439036;
                break;
            case 80002454:
                ret = 2439127;
                break;
            case 80002424:
                ret = 2439144;
                break;
            case 80002545:
                ret = 2439266;
                break;
            case 80002546:
                ret = 2439278;
                break;
            case 80002547:
                ret = 2439295;
                break;
            case 80002572:
                ret = 2439329;
                break;
            case 80002573:
                ret = 2439331;
                break;
            case 80002622:
                ret = 2439406;
                break;
            case 80002585:
                ret = 2439443;
                break;
            case 80002628:
                ret = 2439484;
                break;
            case 80002630:
                ret = 2439486;
                break;
            case 80002594:
                ret = 2439666;
                break;
            case 80002595:
                ret = 2439667;
                break;
            case 80002648:
                ret = 2439675;
                break;
            case 80002650:
                ret = 2439677;
                break;
            case 80002702:
                ret = 2630279;
                break;
            case 80002712:
                ret = 2630386;
                break;
            case 80002713:
                ret = 2630387;
                break;
            case 80002714:
                ret = 2630448;
                break;
            case 80002715:
                ret = 2630451;
                break;
            case 80002716:
                ret = 2630452;
                break;
            case 80002735:
                ret = 2630476;
                break;
            case 80002738:
                ret = 2630488;
                break;
            case 80002740:
                ret = 2630563;
                break;
            case 80002742:
                ret = 2630763;
                break;
            case 80002743:
                ret = 2630764;
                break;
            case 80002744:
                ret = 2630765;
                break;
            case 80002748:
                ret = 2630766;
                break;
            case 80002752:
                ret = 2630570;
                break;
            case 80002754:
                ret = 2630573;
                break;
            case 80002756:
                ret = 2630575;
                break;
            case 80002757:
                ret = 2630576;
                break;
            case 80002824:
                ret = 2430039;
                break;
            case 80002831:
                ret = 2630917;
                break;
            case 80002843:
                ret = 2630913;
                break;
            case 80002844:
                ret = 2630914;
                break;
            case 80002845:
                ret = 2630918;
                break;
            case 80002846:
                ret = 2630919;
                break;
            case 80002853:
                ret = 2630971;
                break;
            case 80002854:
                ret = 5120198;
                break;
            case 80002632:
                ret = 2631140;
                break;
            case 80002858:
                ret = 2631136;
                break;
            case 80002859:
                ret = 2631190;
                break;
            case 80002860:
                ret = 2631191;
                break;
        }
        return ret;
    }

    public static int getDSkinNum(int itemid) {
        int ret = -1;
        switch (itemid) {
            case 2431965:
                ret = 0;
                break;
            case 2438159:
                ret = 0;
                break;
            case 2431966:
                ret = 1;
                break;
            case 2432084:
                ret = 1;
                break;
            case 2438160:
                ret = 1;
                break;
            case 2431967:
                ret = 2;
                break;
            case 2438161:
                ret = 2;
                break;
            case 2432131:
                ret = 3;
                break;
            case 2438162:
                ret = 3;
                break;
            case 2432153:
                ret = 4;
                break;
            case 2432638:
                ret = 4;
                break;
            case 2436688:
                ret = 4;
                break;
            case 2438163:
                ret = 4;
                break;
            case 2432154:
                ret = 5;
                break;
            case 2432637:
                ret = 5;
                break;
            case 2435045:
                ret = 5;
                break;
            case 2438164:
                ret = 5;
                break;
            case 2432207:
                ret = 6;
                break;
            case 2438165:
                ret = 6;
                break;
            case 2432354:
                ret = 7;
                break;
            case 2435023:
                ret = 7;
                break;
            case 2438157:
                ret = 7;
                break;
            case 2438166:
                ret = 7;
                break;
            case 2432355:
                ret = 8;
                break;
            case 2438167:
            case 2632882:
                ret = 8;
                break;
            case 2432465:
                ret = 9;
                break;
            case 2438168:
                ret = 9;
                break;
            case 2432479:
                ret = 10;
                break;
            case 2438169:
                ret = 10;
                break;
            case 2432526:
                ret = 11;
                break;
            case 2432639:
                ret = 11;
                break;
            case 2438170:
                ret = 11;
                break;
            case 2432532:
                ret = 12;
                break;
            case 2435478:
                ret = 12;
                break;
            case 2438171:
                ret = 12;
                break;
            case 2432592:
            case 2633215:
                ret = 13;
                break;
            case 2432640:
                ret = 14;
                break;
            case 2438461:
                ret = 14;
                break;
            case 2432710:
                ret = 15;
                break;
            case 2433919:
                ret = 15;
                break;
            case 2435170:
                ret = 15;
                break;
            case 2432836:
                ret = 16;
                break;
            case 2432973:
                ret = 17;
                break;
            case 2435834:
                ret = 17;
                break;
            case 2433063:
                ret = 18;
                break;
            case 2438177:
                ret = 18;
                break;
            case 2433456:
                ret = 19;
                break;
            case 2433178:
                ret = 20;
                break;
            case 2435169:
                ret = 20;
                break;
            case 2631138:
                ret = 20;
                break;
            case 2433631:
                ret = 22;
                break;
            case 2433655:
                ret = 22;
                break;
            case 2438180:
                ret = 22;
                break;
            case 2433715:
                ret = 23;
                break;
            case 2435024:
                ret = 23;
                break;
            case 2433804:
            case 2633216:
                ret = 24;
                break;
            case 2435168:
                ret = 24;
                break;
            case 2433913:
                ret = 26;
                break;
            case 2435025:
                ret = 26;
                break;
            case 2433980:
                ret = 27;
                break;
            case 2435026:
                ret = 27;
                break;
            case 2437527:
                ret = 27;
                break;
            case 2433981:
                ret = 28;
                break;
            case 2438421:
                ret = 28;
                break;
            case 2433990:
                ret = 29;
                break;
            case 2434248:
                ret = 34;
                break;
            case 2435027:
                ret = 34;
                break;
            case 2434273:
                ret = 35;
                break;
            case 2435028:
                ret = 35;
                break;
            case 2434274:
                ret = 36;
                break;
            case 2435029:
                ret = 36;
                break;
            case 2435490:
                ret = 36;
                break;
            case 2434289:
                ret = 37;
                break;
            case 2434390:
                ret = 38;
                break;
            case 2436099:
                ret = 38;
                break;
            case 2434391:
                ret = 39;
                break;
            case 2436034:
                ret = 39;
                break;
            case 2434528:
                ret = 41;
                break;
            case 2434529:
                ret = 42;
                break;
            case 2434530:
                ret = 43;
                break;
            case 2434546:
                ret = 44;
                break;
            case 2436531:
                ret = 44;
                break;
            case 2434574:
                ret = 45;
                break;
            case 2434575:
                ret = 46;
                break;
            case 2435171:
                ret = 46;
                break;
            case 2434542:
                ret = 47;
                break;
            case 2434654:
                ret = 48;
                break;
            case 2435325:
                ret = 48;
                break;
            case 2434655:
                ret = 49;
                break;
            case 2435326:
                ret = 49;
                break;
            case 2434661:
                ret = 50;
                break;
            case 2437878:
                ret = 50;
                break;
            case 2631137:
                ret = 50;
                break;
            case 2434710:
                ret = 51;
                break;
            case 2434734:
                ret = 52;
                break;
            case 2434824:
                ret = 53;
                break;
            case 2434950:
                ret = 74;
                break;
            case 2435837:
                ret = 74;
                break;
            case 2434951:
                ret = 75;
                break;
            case 2435030:
                ret = 76;
                break;
            case 2435043:
                ret = 77;
                break;
            case 2436041:
                ret = 77;
                break;
            case 2435044:
                ret = 78;
                break;
            case 2436042:
                ret = 78;
                break;
            case 2435046:
                ret = 79;
                break;
            case 2436097:
                ret = 79;
                break;
            case 2435047:
                ret = 80;
                break;
            case 2435140:
                ret = 81;
                break;
            case 2435836:
                ret = 81;
                break;
            case 2435141:
                ret = 82;
                break;
            case 2437244:
                ret = 82;
                break;
            case 2436096:
                ret = 83;
                break;
            case 2435162:
                ret = 84;
                break;
            case 2435724:
                ret = 84;
                break;
            case 2435157:
                ret = 85;
                break;
            case 2436098:
                ret = 85;
                break;
            case 2435158:
                ret = 86;
                break;
            case 2435835:
                ret = 86;
                break;
            case 2435159:
                ret = 87;
                break;
            case 2436687:
                ret = 160;
                break;
            case 2435160:
                ret = 88;
                break;
            case 2436044:
                ret = 88;
                break;
            case 2435161:
                ret = 89;
                break;
            case 2436043:
                ret = 89;
                break;
            case 2435182:
                ret = 90;
                break;
            case 2435725:
                ret = 90;
                break;
            case 2435166:
                ret = 91;
                break;
            case 2435850:
                ret = 91;
                break;
            case 2435184:
                ret = 92;
                break;
            case 2435222:
                ret = 93;
                break;
            case 2436530:
                ret = 93;
                break;
            case 2435293:
                ret = 94;
                break;
            case 2435313:
                ret = 95;
                break;
            case 2435331:
                ret = 96;
                break;
            case 2435473:
                ret = 96;
                break;
            case 2435332:
                ret = 97;
                break;
            case 2435333:
                ret = 98;
                break;
            case 2435849:
                ret = 98;
                break;
            case 2435334:
                ret = 99;
                break;
            case 2435474:
                ret = 99;
                break;
            case 2435316:
                ret = 100;
                break;
            case 2435408:
                ret = 101;
                break;
            case 2435427:
                ret = 102;
                break;
            case 2435428:
                ret = 103;
                break;
            case 2435429:
                ret = 104;
                break;
            case 2435456:
                ret = 105;
                break;
            case 2435493:
                ret = 106;
                break;
            case 2435424:
                ret = 109;
                break;
            case 2435425:
                ret = 110;
                break;
            case 2435431:
                ret = 111;
                break;
            case 2435430:
                ret = 112;
                break;
            case 2435432:
                ret = 113;
                break;
            case 2435433:
                ret = 114;
                break;
            case 2435516:
                ret = 115;
                break;
            case 2435521:
                ret = 116;
                break;
            case 2435522:
                ret = 117;
                break;
            case 2435523:
                ret = 118;
                break;
            case 2435524:
                ret = 119;
                break;
            case 2436561:
                ret = 119;
                break;
            case 2435538:
                ret = 120;
                break;
            case 2435832:
                ret = 121;
                break;
            case 2435833:
                ret = 122;
                break;
            case 2435839:
                ret = 123;
                break;
            case 2435840:
                ret = 124;
                break;
            case 2435841:
                ret = 125;
                break;
            case 2435972:
                ret = 127;
                break;
            case 2436023:
                ret = 128;
                break;
            case 2436024:
                ret = 129;
                break;
            case 2436026:
                ret = 130;
                break;
            case 2436027:
                ret = 131;
                break;
            case 2436028:
                ret = 132;
                break;
            case 2436029:
                ret = 133;
                break;
            case 2436045:
                ret = 134;
                break;
            case 2436085:
                ret = 135;
                break;
            case 2436083:
                ret = 136;
                break;
            case 2436084:
                ret = 137;
                break;
            case 2436103:
                ret = 138;
                break;
            case 2436131:
                ret = 139;
                break;
            case 2436140:
                ret = 140;
                break;
            case 2436206:
                ret = 141;
                break;
            case 2436182:
                ret = 142;
                break;
            case 2436212:
                ret = 143;
                break;
            case 2437851:
                ret = 143;
                break;
            case 2436215:
                ret = 144;
                break;
            case 2436268:
                ret = 145;
                break;
            case 2436258:
                ret = 146;
                break;
            case 2436259:
                ret = 147;
                break;
            case 2436400:
                ret = 148;
                break;
            case 2436437:
                ret = 149;
                break;
            case 2436511:
                ret = 150;
                break;
            case 2436528:
                ret = 152;
                break;
            case 2436529:
                ret = 153;
                break;
            case 2436522:
                ret = 154;
                break;
            case 2436553:
                ret = 155;
                break;
            case 2437697:
                ret = 155;
                break;
            case 2436560:
                ret = 156;
                break;
            case 2436578:
                ret = 157;
                break;
            case 2437767:
                ret = 157;
                break;
            case 2436611:
                ret = 158;
                break;
            case 2436612:
                ret = 159;
                break;
            case 2436596:
                ret = 160;
                break;
            case 2436679:
                ret = 161;
                break;
            case 2436680:
                ret = 162;
                break;
            case 2436681:
                ret = 163;
                break;
            case 2436682:
                ret = 164;
                break;
            case 2436683:
                ret = 165;
                break;
            case 2436684:
                ret = 166;
                break;
            case 2436785:
                ret = 167;
                break;
            case 2436810:
                ret = 168;
                break;
            case 2436951:
                ret = 169;
                break;
            case 2436952:
                ret = 170;
                break;
            case 2436953:
                ret = 171;
                break;
            case 2437022:
                ret = 172;
                break;
            case 2437023:
                ret = 173;
                break;
            case 2437024:
                ret = 174;
                break;
            case 2437009:
                ret = 175;
                break;
            case 2437164:
                ret = 176;
                break;
            case 2438925:
                ret = 176;
                break;
            case 2437238:
                ret = 177;
                break;
            case 2437243:
                ret = 178;
                break;
            case 2437239:
                ret = 179;
                break;
            case 2437495:
                ret = 180;
                break;
            case 2437496:
                ret = 181;
                break;
            case 2437498:
                ret = 182;
                break;
            case 2437515:
                ret = 183;
                break;
            case 2437482:
                ret = 184;
                break;
            case 2438926:
                ret = 184;
                break;
            case 2437691:
                ret = 185;
                break;
            case 2437716:
                ret = 186;
                break;
            case 2437735:
                ret = 187;
                break;
            case 2437736:
                ret = 188;
                break;
            case 2437854:
                ret = 189;
                break;
            case 2437877:
                ret = 190;
                break;
            case 2438143:
                ret = 191;
                break;
            case 2438146:
                ret = 191;
                break;
            case 2438144:
                ret = 192;
                break;
            case 2438147:
                ret = 192;
                break;
            case 2438352:
                ret = 193;
                break;
            case 2438353:
                ret = 193;
                break;
            case 2438924:
                ret = 193;
                break;
            case 2438378:
                ret = 194;
                break;
            case 2438379:
                ret = 195;
                break;
            case 2438413:
                ret = 196;
                break;
            case 2438414:
                ret = 196;
                break;
            case 2438415:
                ret = 197;
                break;
            case 2438416:
                ret = 197;
                break;
            case 2438417:
                ret = 198;
                break;
            case 2438418:
                ret = 198;
                break;
            case 2438419:
                ret = 199;
                break;
            case 2438420:
                ret = 199;
                break;
            case 2438460:
                ret = 200;
                break;
            case 2438485:
                ret = 200;
                break;
            case 2438491:
                ret = 201;
                break;
            case 2438492:
                ret = 201;
                break;
            case 2438529:
                ret = 202;
                break;
            case 2438530:
                ret = 202;
                break;
            case 2438637:
                ret = 203;
                break;
            case 2438672:
                ret = 204;
                break;
            case 2438676:
                ret = 205;
                break;
            case 2438713:
                ret = 205;
                break;
            case 2438880:
                ret = 206;
                break;
            case 2438881:
                ret = 206;
                break;
            case 2438884:
                ret = 207;
                break;
            case 2438885:
                ret = 207;
                break;
            case 2438871:
                ret = 208;
                break;
            case 2438872:
                ret = 208;
                break;
            case 2439256:
                ret = 209;
                break;
            case 2439298:
                ret = 209;
                break;
            case 2439264:
                ret = 210;
                break;
            case 2439336:
                ret = 210;
                break;
            case 2439265:
                ret = 211;
                break;
            case 2439337:
                ret = 211;
                break;
            case 2439277:
                ret = 212;
                break;
            case 2439338:
                ret = 212;
                break;
            case 2439381:
                ret = 213;
                break;
            case 2439392:
                ret = 214;
                break;
            case 2439393:
                ret = 214;
                break;
            case 2439394:
                ret = 215;
                break;
            case 2439395:
                ret = 215;
                break;
            case 2439407:
                ret = 216;
                break;
            case 2439408:
                ret = 216;
                break;
            case 2439572:
                ret = 217;
                break;
            case 2439616:
                ret = 218;
                break;
            case 2439617:
                ret = 218;
                break;
            case 2439652:
                ret = 219;
                break;
            case 2439665:
                ret = 219;
                break;
            case 2439683:
                ret = 221;
                break;
            case 2439684:
                ret = 221;
                break;
            case 2439685:
                ret = 222;
                break;
            case 2439686:
                ret = 222;
                break;
            case 2439768:
                ret = 223;
                break;
            case 2439769:
                ret = 223;
                break;
            case 2439925:
                ret = 224;
                break;
            case 2439926:
                ret = 224;
                break;
            case 2439927:
                ret = 225;
                break;
            case 2439928:
                ret = 225;
                break;
            case 2630104:
                ret = 225;
                break;
            case 2630132:
                ret = 227;
                break;
            case 2630137:
                ret = 227;
                break;
            case 2630010:
                ret = 228;
                break;
            case 2603222:
                ret = 228;
                break;
            case 2630178:
                ret = 229;
                break;
            case 2630179:
                ret = 229;
                break;
            case 2630213:
                ret = 230;
                break;
            case 2630214:
                ret = 230;
                break;
            case 2630380:
                ret = 231;
                break;
            case 2630381:
                ret = 231;
                break;
            case 2630235:
                ret = 232;
                break;
            case 2630236:
                ret = 232;
                break;
            case 2630224:
                ret = 233;
                break;
            case 2630225:
                ret = 233;
                break;
            case 2630766:
                ret = 233;
                break;
            case 2630262:
                ret = 234;
                break;
            case 2630263:
                ret = 234;
                break;
            case 2630264:
                ret = 235;
                break;
            case 2630265:
                ret = 235;
                break;
            case 2630266:
                ret = 236;
                break;
            case 2630267:
                ret = 236;
                break;
            case 2630384:
                ret = 237;
                break;
            case 2630385:
                ret = 237;
                break;
            case 2630400:
                ret = 238;
                break;
            case 2630421:
                ret = 239;
                break;
            case 2630434:
                ret = 239;
                break;
            case 2630435:
                ret = 240;
                break;
            case 2630436:
                ret = 240;
                break;
            case 2630477:
                ret = 241;
                break;
            case 2630478:
                ret = 241;
                break;
            case 2630479:
                ret = 242;
                break;
            case 2630480:
                ret = 242;
                break;
            case 2630481:
                ret = 243;
                break;
            case 2630482:
                ret = 243;
                break;
            case 2630483:
                ret = 244;
                break;
            case 2630484:
                ret = 244;
                break;
            case 2630485:
                ret = 245;
                break;
            case 2630486:
                ret = 245;
                break;
            case 2630552:
                ret = 246;
                break;
            case 2630553:
                ret = 246;
                break;
            case 2630554:
                ret = 247;
                break;
            case 2630555:
                ret = 247;
                break;
            case 2630556:
                ret = 248;
                break;
            case 2630557:
                ret = 248;
                break;
            case 2630558:
                ret = 249;
                break;
            case 2630559:
                ret = 249;
                break;
            case 2630560:
                ret = 250;
                break;
            case 2630561:
                ret = 250;
                break;
            case 2630652:
                ret = 251;
                break;
            case 2630653:
                ret = 251;
                break;
            case 2630743:
                ret = 252;
                break;
            case 2630744:
                ret = 252;
                break;
            case 2630745:
                ret = 253;
                break;
            case 2630746:
                ret = 253;
                break;
            case 2630747:
                ret = 254;
                break;
            case 2630748:
                ret = 254;
                break;
            case 2630749:
                ret = 255;
                break;
            case 2630750:
                ret = 255;
                break;
            case 2630751:
                ret = 256;
                break;
            case 2630752:
                ret = 256;
                break;
            case 2630753:
                ret = 257;
                break;
            case 2630754:
                ret = 257;
                break;
            case 2630780:
                ret = 258;
                break;
            case 2630804:
                ret = 258;
                break;
            case 2630969:
                ret = 259;
                break;
            case 2630970:
                ret = 259;
                break;
            case 2631090:
                ret = 260;
                break;
            case 2631094:
                ret = 260;
                break;
            case 2631091:
                ret = 261;
                break;
            case 2631095:
                ret = 261;
                break;
            case 2631097:
                ret = 262;
                break;
            case 2631098:
                ret = 262;
                break;
            case 2631134:
                ret = 263;
                break;
            case 2631135:
                ret = 263;
                break;
            case 2631189:
                ret = 264;
                break;
            case 2631183:
                ret = 265;
                break;
            case 2631184:
                ret = 265;
                break;
            case 2631401:
                ret = 266;
                break;
            case 2631402:
                ret = 266;
                break;
            case 2631451:
                ret = 267;
                break;
            case 2631452:
                ret = 267;
                break;
            case 2631471:
                ret = 268;
                break;
            case 2631472:
                ret = 268;
                break;
            case 2631491:
                ret = 269;
                break;
            case 2631492:
                ret = 269;
                break;
            case 2631610:
                ret = 270;
                break;
            case 2631611:
                ret = 270;
                break;
            case 3130001:
                ret = 271;
                break;
            case 2631797:
                ret = 273;
                break;
            case 2631798:
                ret = 273;
                break;
            case 2631814:
                ret = 274;
                break;
            case 2631815:
                ret = 274;
                break;
            case 2631884:
                ret = 275;
                break;
            case 2631885:
                ret = 275;
                break;
            case 2631892:
                ret = 276;
                break;
            case 2631893:
                ret = 276;
                break;
            case 2632123:
                ret = 277;
                break;
            case 2632124:
                ret = 277;
                break;
            case 2632281:
                ret = 278;
                break;
            case 2632282:
                ret = 279;
                break;
            case 2632287:
                ret = 279;
                break;
            case 2632288:
                ret = 279;
                break;
            case 2632348:
                ret = 280;
                break;
            case 2632350:
                ret = 280;
                break;
            case 2632429:
            case 2632430:
                ret = 281;
                break;
            case 2632452:
            case 2632544:
                ret = 282;
                break;
            case 2632497:
            case 2632498:
                ret = 283;
                break;
            case 2632711:
            case 2632712:
                ret = 284;
                break;
            case 2632815:
            case 2632816:
                ret = 286;
                break;
            case 2632888:
                ret = 287;
                break;
            case 2632975:
            case 2632976:
                ret = 288;
                break;
            case 2633045:
            case 2633046:
                ret = 289;
                break;
            case 2633047:
            case 2633048:
                ret = 290;
                break;
            case 2633073:
            case 2633074:
                ret = 291;
                break;
            case 2633218:
            case 2633219:
                ret = 292;
                break;
            case 2633220:
            case 2633221:
                ret = 293;
                break;
            case 2439396:
                ret = 1010;
                break;
            case 2439397:
                ret = 1010;
                break;
            case 2439398:
                ret = 1017;
                break;
            case 2439399:
                ret = 1017;
                break;
            case 2630268:
                ret = 1030;
                break;
            case 2630269:
                ret = 1030;
                break;
            case 2438148:
                ret = 1287;
                break;
            case 2438149:
                ret = 1287;
                break;
            case 2439681:
                ret = 1290;
                break;
            case 2439682:
                ret = 1290;
                break;
            case 2438150:
                ret = 1302;
                break;
            case 2438151:
                ret = 1302;
                break;
            case 2439400:
                ret = 1322;
                break;
            case 2439401:
                ret = 1322;
                break;
            case 2630516:
                ret = 1343;
                break;
            case 2630517:
                ret = 1343;
                break;
            case 2634176:
            case 2634177:
                ret = 304;
                break;
            case 2634250:
            case 2634251:
                ret = 305;
                break;
            case 2634267:
            case 2634268:
                ret = 307;
                break;
            case 2634276:
            case 2634277:
                ret = 308;
                break;
            case 2634279:
            case 2634280:
                ret = 309;
                break;
        }
        return ret;
    }

    public static final int[] publicNpcIds = new int[]{9270035, 9070004, 9010022, 9071003, 9000087, 9000088, 9010000, 9000085, 9000018, 9000000};

    public static final String[] publicNpcs = new String[]{"#cUniversal NPC#", "Move to the #cBattle Square# to fight other players", "Move to a variety of #cparty quests#.", "Move to #cMonster Park# to team up to defeat monsters.", "Move to #cFree Market# to trade items with players.", "Move to #cArdentmill#, the crafting town.", "Check #cdrops# of any monster in the map.", "Review #cPokedex#.", "Review #cPokemon#.", "Join an #cevent# in progress."};

    public static final int OMOK_SCORE = 122200;

    public static final int MATCH_SCORE = 122210;

    public static final int HP_ITEM = 122221;

    public static final int MP_ITEM = 122223;

    public static final int JAIL_TIME = 123455;

    public static final int JAIL_QUEST = 123456;

    public static final int REPORT_QUEST = 123457;

    public static final int ULT_EXPLORER = 111111;

    public static final int ENERGY_DRINK = 122500;

    public static final int HARVEST_TIME = 122501;

    public static final int PENDANT_SLOT = 122700;

    public static final int BOSS_PQ = 150001;

    public static final int DOJO = 150100;

    public static final int DOJO_RECORD = 150101;

    public static final int PARTY_REQUEST = 122900;

    public static final int PARTY_INVITE = 122901;

    public static final boolean GMS = false;

    public static boolean isBlaster(int job) {
        return (job >= 3700 && job <= 3712);
    }

    public static boolean isPinkBean(int job) {
        return (job == 13000 || job == 13100);
    }

    public static boolean isYeti(int job) {
        return (job == 13001 || job == 13500);
    }

    public static boolean isDefaultWarrior(int job) {
        return (job == 100);
    }

    public static boolean isHero(int job) {
        return (job >= 110 && job <= 112);
    }

    public static boolean isPaladin(int job) {
        return (job >= 120 && job <= 122);
    }

    public static boolean isDarkKnight(int job) {
        return (job >= 130 && job <= 132);
    }

    public static boolean isDefaultMagician(int job) {
        return (job == 200);
    }

    public static boolean isFPMage(int job) {
        return (job >= 210 && job <= 212);
    }

    public static boolean isILMage(int job) {
        return (job >= 220 && job <= 222);
    }

    public static boolean isBishop(int job) {
        return (job >= 230 && job <= 232);
    }

    public static boolean isDefaultArcher(int job) {
        return (job == 300);
    }

    public static boolean isBowMaster(int job) {
        return (job >= 310 && job <= 312);
    }

    public static boolean isMarksMan(int job) {
        return (job >= 320 && job <= 322);
    }

    public static boolean isDefaultThief(int job) {
        return (job == 400);
    }

    public static boolean isNightLord(int job) {
        return (job >= 410 && job <= 412);
    }

    public static boolean isShadower(int job) {
        return (job >= 420 && job <= 422);
    }

    public static boolean isDualBlade(int job) {
        return (job >= 430 && job <= 434);
    }

    public static boolean isDefaultPirate(int job) {
        return (job == 500 || job == 501);
    }

    public static boolean isViper(int job) {
        return (job >= 510 && job <= 512);
    }

    public static boolean isCaptain(int job) {
        return (job >= 520 && job <= 522);
    }

    public static boolean isCannon(int job) {
        return (job == 1 || job == 501 || (job >= 530 && job <= 532));
    }

    public static boolean isWarrior(int job) {
        if (isPinkBean(job) || isDefaultWarrior(job) || isHero(job) || isPaladin(job) || isDarkKnight(job) || isSoulMaster(job) || isAran(job) || isBlaster((short) job) || isDemonSlayer(job) || isDemonAvenger(job) || isMichael(job) || isKaiser(job) || isZero(job) || isAdel(job)) {
            return true;
        }
        return false;
    }

    public static boolean isMagician(int job) {
        if (isDefaultMagician(job) || isFPMage(job) || isILMage(job) || isBishop(job) || isFlameWizard(job) || isEvan(job) || isLuminous(job) || isBattleMage(job) || isKinesis(job) || isIllium(job) || isLara(job)) {
            return true;
        }
        return false;
    }

    public static boolean isArcher(int job) {
        if (isDefaultArcher(job) || isPathFinder(job) || isBowMaster(job) || isMarksMan(job) || isWindBreaker(job) || isMercedes(job) || isWildHunter(job) || isCain(job)) {
            return true;
        }
        return false;
    }

    public static boolean isThief(int job) {
        if (isDefaultThief(job) || isHoyeong(job) || isNightLord(job) || isShadower(job) || isDualBlade(job) || isNightWalker(job) || isPhantom(job) || isKadena(job) || isXenon(job)) {
            return true;
        }
        return false;
    }

    public static boolean isPirate(int job) {
        if (isYeti(job) || isDefaultPirate(job) || isViper(job) || isCaptain(job) || isCannon(job) || isStriker(job) || isEunWol(job) || isMechanic(job) || isAngelicBuster(job) || isArc(job) || isXenon(job)) {
            return true;
        }
        return false;
    }

    public static int MatrixExp(int level) {
        switch (level) {
            case 1:
                return 50;
            case 2:
                return 89;
            case 3:
                return 149;
            case 4:
                return 221;
            case 5:
                return 306;
            case 6:
                return 404;
            case 7:
                return 514;
            case 8:
                return 638;
            case 9:
                return 774;
            case 10:
                return 922;
            case 11:
                return 1084;
            case 12:
                return 1258;
            case 13:
                return 1445;
            case 14:
                return 1645;
            case 15:
                return 1857;
            case 16:
                return 2083;
            case 17:
                return 2321;
            case 18:
                return 2571;
            case 19:
                return 2835;
            case 20:
                return 3111;
            case 21:
                return 3400;
            case 22:
                return 3702;
            case 23:
                return 4016;
            case 24:
                return 4344;
        }
        return 4684;
    }

    public static int HyperStatSp(int curLevel) {
        switch (curLevel) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 10;
            case 5:
                return 15;
            case 6:
                return 20;
            case 7:
                return 25;
            case 8:
                return 30;
            case 9:
                return 35;
        }
        return 0;
    }

    public static int getMyLinkSkill(short job) {
        if (isMercedes(job)) {
            return 20021110;
        }
        if (isCannon(job)) {
            return 110;
        }
        if (isDemonSlayer(job)) {
            return 30010112;
        }
        if (isPhantom(job)) {
            return 20030204;
        }
        if (isLuminous(job)) {
            return 20040218;
        }
        if (isKaiser(job)) {
            return 60000222;
        }
        if (isXenon(job)) {
            return 30020233;
        }
        if (isDemonAvenger(job)) {
            return 30010241;
        }
        if (isSoulMaster(job)) {
            return 10000255;
        }
        if (isFlameWizard(job)) {
            return 10000256;
        }
        if (isWindBreaker(job)) {
            return 10000257;
        }
        if (isNightWalker(job)) {
            return 10000258;
        }
        if (isStriker(job)) {
            return 10000259;
        }
        if (isZero(job)) {
            return 100000271;
        }
        if (isEunWol(job)) {
            return 20050286;
        }
        if (isKinesis(job)) {
            return 140000292;
        }
        if (isAngelicBuster(job)) {
            return 60011219;
        }
        if (isEvan(job)) {
            return 20010294;
        }
        if (isAran(job)) {
            return 20000297;
        }
        if (isMichael(job)) {
            return 50001214;
        }
        if (isBattleMage(job)) {
            return 30000074;
        }
        if (isWildHunter(job)) {
            return 30000075;
        }
        if (isMechanic(job)) {
            return 30000076;
        }
        if (isBlaster(job)) {
            return 30000077;
        }
        if (isKadena(job)) {
            return 60020218;
        }
        if (isIllium(job)) {
            return 150000017;
        }
        if (isArk(job)) {
            return 150010241;
        }
        if (isHero(job)) {
            return 252;
        }
        if (isPaladin(job)) {
            return 253;
        }
        if (isDarkKnight(job)) {
            return 254;
        }
        if (isFPMage(job)) {
            return 255;
        }
        if (isILMage(job)) {
            return 256;
        }
        if (isBishop(job)) {
            return 257;
        }
        if (isBowMaster(job)) {
            return 258;
        }
        if (isMarksMan(job)) {
            return 259;
        }
        if (isPathFinder(job)) {
            return 260;
        }
        if (isNightLord(job)) {
            return 261;
        }
        if (isShadower(job)) {
            return 262;
        }
        if (isDualBlade(job)) {
            return 263;
        }
        if (isViper(job)) {
            return 264;
        }
        if (isCaptain(job)) {
            return 265;
        }
        if (isHoyeong(job)) {
            return 160000001;
        }
        if (isAdel(job)) {
            return 150020241;
        }
        if (isCain(job)) {
            return 60030241;
        }
        if (isLara(job)) {
            return 160010001;
        }
        return 0;
    }

    public static int getLinkedSkillByJob(short job) {
        if (isMercedes(job)) {
            return 80001040;
        }
        if (isCannon(job)) {
            return 80000000;
        }
        if (isDemonSlayer(job)) {
            return 80000001;
        }
        if (isPhantom(job)) {
            return 80000002;
        }
        if (isLuminous(job)) {
            return 80000005;
        }
        if (isKaiser(job)) {
            return 80000006;
        }
        if (isXenon(job)) {
            return 80000047;
        }
        if (isDemonAvenger(job)) {
            return 80000050;
        }
        if (isSoulMaster(job)) {
            return 80000066;
        }
        if (isFlameWizard(job)) {
            return 80000067;
        }
        if (isWindBreaker(job)) {
            return 80000068;
        }
        if (isNightWalker(job)) {
            return 80000069;
        }
        if (isStriker(job)) {
            return 80000070;
        }
        if (isZero(job)) {
            return 80000110;
        }
        if (isEunWol(job)) {
            return 80000169;
        }
        if (isKinesis(job)) {
            return 80000188;
        }
        if (isAngelicBuster(job)) {
            return 80001155;
        }
        if (isEvan(job)) {
            return 80000369;
        }
        if (isAran(job)) {
            return 80000370;
        }
        if (isMichael(job)) {
            return 80001140;
        }
        if (isBattleMage(job)) {
            return 80000333;
        }
        if (isWildHunter(job)) {
            return 80000334;
        }
        if (isMechanic(job)) {
            return 80000335;
        }
        if (isBlaster(job)) {
            return 80000378;
        }
        if (isKadena(job)) {
            return 80000261;
        }
        if (isIllium(job)) {
            return 80000268;
        }
        if (isArk(job)) {
            return 80000514;
        }
        if (isHero(job)) {
            return 80002759;
        }
        if (isPaladin(job)) {
            return 80002760;
        }
        if (isDarkKnight(job)) {
            return 80002761;
        }
        if (isFPMage(job)) {
            return 80002763;
        }
        if (isILMage(job)) {
            return 80002764;
        }
        if (isBishop(job)) {
            return 80002765;
        }
        if (isBowMaster(job)) {
            return 80002767;
        }
        if (isMarksMan(job)) {
            return 80002768;
        }
        if (isPathFinder(job)) {
            return 80002769;
        }
        if (isNightLord(job)) {
            return 80002771;
        }
        if (isShadower(job)) {
            return 80002772;
        }
        if (isDualBlade(job)) {
            return 80002773;
        }
        if (isViper(job)) {
            return 80002775;
        }
        if (isCaptain(job)) {
            return 80002776;
        }
        if (isHoyeong(job)) {
            return 80000609;
        }
        if (isAdel(job)) {
            return 80002857;
        }
        if (isCain(job)) {
            return 80003015;
        }
        if (isLara(job)) {
            return 80003058;
        }
        return 0;
    }

    public static boolean MovementAffectingStat(SecondaryStat stat) {
        switch (stat) {
            case IndieJump:
            case IndieSpeed:
            case Stun:
            case Weakness:
            case Slow:
            case Morph:
            case Ghost:
            case BasicStatUp:
            case Attract:
            case DashSpeed:
            case DashJump:
            case Flying:
            case KeyDownMoving:
            case Frozen:
            case Frozen2:
            case Speed:
            case Jump:
            case Mechanic:
            case Magnet:
            case MagnetArea:
            case DarkTornado:
            case NewFlying:
            case NaviFlying:
            case Dance:
            case RWCylinder:
            case RideVehicle:
            case RideVehicleExpire:
            case DemonFrenzy:
            case ShadowSpear:
            case VampDeath:
            case Lapidification:
            case SelfWeakness:
                return true;
        }
        return false;
    }

    public static boolean isStrangeCube(int itemid) {
        switch (itemid) {
            case 2436499:
            case 2710000:
            case 2711000:
            case 2711001:
            case 2711009:
            case 2711011:
                return true;
        }
        return false;
    }

    public static boolean isTryFling(int skillid) {
        switch (skillid) {
            case 13100022:
            case 13100027:
            case 13101022:
            case 13110022:
            case 13110027:
            case 13120003:
            case 13120010:
                return true;
        }
        return false;
    }

    public static boolean sub_57D400(int a1) {
        boolean v1;
        if (a1 > 12110028) {
            v1 = (a1 == 12120010);
        } else {
            if (a1 == 12110028 || a1 == 12000026) {
                return true;
            }
            v1 = (a1 == 12100028);
        }
        return v1;
    }

    public static boolean is_keydown_skill(int a1) {
        if (a1 <= 33121009) {
            boolean v1;
            if (a1 == 33121009) {
                return true;
            }
            if (a1 > 14121004) {
                if (a1 > 25121030) {
                    if (a1 > 31001000) {
                        if (a1 == 31101000 || a1 == 31111005) {
                            return true;
                        }
                        v1 = (a1 == 31211001);
                    } else {
                        if (a1 == 31001000 || a1 == 27101202 || a1 == 27111100) {
                            return true;
                        }
                        v1 = (a1 == 30021238);
                    }
                } else {
                    if (a1 == 25121030) {
                        return true;
                    }
                    if (a1 > 23121000) {
                        if (a1 == 24121000 || a1 - 24121000 == 5) {
                            return true;
                        }
                        v1 = (a1 - 24121000 == 990005);
                    } else {
                        if (a1 == 23121000) {
                            return true;
                        }
                        if (a1 > 21120019) {
                            v1 = (a1 == 22171083);
                        } else {
                            if (a1 >= 21120018) {
                                return true;
                            }
                            v1 = (a1 == 20041226);
                        }
                    }
                }
            } else {
                if (a1 == 14121004) {
                    return true;
                }
                if (a1 > 5221004) {
                    if (a1 > 12121054) {
                        if (a1 == 13111020 || a1 == 13121001) {
                            return true;
                        }
                        v1 = (a1 == 14111006);
                    } else {
                        if (a1 == 12121054 || a1 == 5311002 || a1 == 11121052) {
                            return true;
                        }
                        v1 = (a1 - 11121052 == 3);
                    }
                } else {
                    if (a1 == 5221004) {
                        return true;
                    }
                    if (a1 > 3101008) {
                        if (a1 == 3111013 || a1 == 3121020) {
                            return true;
                        }
                        v1 = (a1 == 4341002);
                    } else {
                        if (a1 == 3101008 || a1 == 1311011 || a1 == 2221011) {
                            return true;
                        }
                        v1 = (a1 - 2221011 == 41);
                    }
                }
            }
            if (!v1) {
                if (Integer.toUnsignedLong(a1 - 80001389) <= 3L || sub_815760(a1)) {
                    return true;
                }
                return isEncodeKeyDownSkill(a1);
            }
            return true;
        }
        if (a1 <= 80002780) {
            boolean v1;
            if (a1 == 80002780) {
                return true;
            }
            if (a1 > 60011216) {
                if (a1 > 65121003) {
                    if (a1 == 80001836 || a1 == 80001887) {
                        return true;
                    }
                    v1 = (a1 - 80001887 == 798);
                } else {
                    if (a1 == 65121003) {
                        return true;
                    }
                    if (a1 > 64001008) {
                        v1 = (a1 == 64121002);
                    } else {
                        if (a1 >= 64001007) {
                            return true;
                        }
                        v1 = (a1 == 64001000);
                    }
                }
            } else {
                if (a1 == 60011216) {
                    return true;
                }
                if (a1 > 36101001) {
                    if (a1 == 36121000 || a1 - 36121000 == 1000003) {
                        return true;
                    }
                    v1 = (a1 - 36121000 == 1000052);
                } else {
                    if (a1 == 36101001 || a1 == 33121114 || a1 - 33121114 == 100) {
                        return true;
                    }
                    v1 = (a1 - 33121114 == 1999901);
                }
            }
            if (!v1) {
                if (Integer.toUnsignedLong(a1 - 80001389) <= 3L || sub_815760(a1)) {
                    return true;
                }
                return isEncodeKeyDownSkill(a1);
            }
            return true;
        }
        if (a1 > 400011028) {
            boolean v1;
            if (a1 > 400031046) {
                if (a1 == 400041006 || a1 == 400041009) {
                    return true;
                }
                v1 = (a1 - 400041009 == 10015);
            } else {
                if (a1 == 400031046 || a1 == 400011072 || a1 == 400011091) {
                    return true;
                }
                v1 = (a1 - 400011091 == 9970);
            }
            if (!v1) {
                if (Integer.toUnsignedLong(a1 - 80001389) <= 3L || sub_815760(a1)) {
                    return true;
                }
                return isEncodeKeyDownSkill(a1);
            }
            return true;
        }
        if (a1 == 400011028) {
            return true;
        }
        if (a1 > 131001004) {
            boolean v1;
            if (a1 > 131001021) {
                v1 = (a1 == 142111010);
            } else {
                if (a1 >= 131001020) {
                    return true;
                }
                v1 = (a1 == 131001008);
            }
            if (!v1) {
                if (Integer.toUnsignedLong(a1 - 80001389) <= 3L || sub_815760(a1)) {
                    return true;
                }
                return isEncodeKeyDownSkill(a1);
            }
            return true;
        }
        if (a1 == 131001004) {
            return true;
        }
        if (a1 <= 95001001) {
            if (a1 == 95001001) {
                return true;
            }
            boolean v1 = (a1 == 80002785);
            if (!v1) {
                if (Integer.toUnsignedLong(a1 - 80001389) <= 3L || sub_815760(a1)) {
                    return true;
                }
                return isEncodeKeyDownSkill(a1);
            }
            return true;
        }
        if (a1 >= 101110101 && a1 <= 101110102) {
            return true;
        }
        if (Integer.toUnsignedLong(a1 - 80001389) <= 3L || sub_815760(a1)) {
            return true;
        }
        return isEncodeKeyDownSkill(a1);
    }

    public static boolean isEncodeKeyDownSkill(int a1) {
        return (SkillFactory.getSkill(a1) != null && SkillFactory.getSkill(a1).isEncode4Byte());
    }

    public static boolean sub_815760(int a1) {
        return (a1 == 80001587 || a1 == 80001629 || a1 == 80002458);
    }

    public static boolean isZeroSkill(int a1) {
        int v1 = a1 / 10000;
        if (a1 / 10000 == 8000) {
            v1 = a1 / 100;
        }
        return (v1 == 10000 || v1 == 10100 || v1 == 10110 || v1 == 10111 || v1 == 10112);
    }

    public static boolean sub_57DCA0(int a1) {
        if (a1 > 23120013) {
            if (a1 > 131001108) {
                if (a1 > 131001313) {
                    if (a1 > 400031024) {
                        if (Integer.toUnsignedLong(a1 - 400041059) > 1L) {
                            return false;
                        }
                    } else if (a1 != 400031024 && a1 != 131002010) {
                        return false;
                    }
                } else if (a1 != 131001313) {
                    switch (a1) {
                        case 131001113:
                        case 131001201:
                        case 131001202:
                        case 131001203:
                        case 131001208:
                        case 131001213:
                            return true;
                    }
                    return false;
                }
            } else if (a1 != 131001108) {
                if (a1 > 131001005) {
                    switch (a1) {
                        case 131001008:
                        case 131001010:
                        case 131001011:
                        case 131001012:
                        case 131001013:
                        case 131001101:
                        case 131001102:
                        case 131001103:
                        case 131001104:
                            return true;
                    }
                    return false;
                }
                if (a1 < 131001000) {
                    switch (a1) {
                        case 23121000:
                        case 23121002:
                        case 23121003:
                        case 23121011:
                        case 23121052:
                            return true;
                    }
                    return false;
                }
            }
            return true;
        }
        if (a1 == 23120013) {
            return true;
        }
        if (a1 <= 14111023) {
            if (a1 >= 14111020) {
                return true;
            }
            if (a1 > 11121103) {
                boolean bool;
                if (a1 > 14001020) {
                    if (a1 < 14101020) {
                        return false;
                    }
                    bool = (a1 == 14101021);
                } else {
                    if (a1 == 14001020) {
                        return true;
                    }
                    if (a1 < 11121201) {
                        return false;
                    }
                    bool = (a1 == 11121203);
                }
                return bool;
            }
            if (a1 >= 11121101) {
                return true;
            }
            if (a1 <= 11111121) {
                if (a1 < 11111120) {
                    switch (a1) {
                        case 11101120:
                        case 11101121:
                        case 11101220:
                        case 11101221:
                            return true;
                    }
                    return false;
                }
                return true;
            }
            if (a1 >= 11111220) {
                boolean bool = (a1 == 11111221);
                return bool;
            }
            return false;
        }
        if (a1 <= 23101001) {
            if (a1 >= 23101000) {
                return true;
            }
            if (a1 > 23001000) {
                if (a1 != 23100004) {
                    return false;
                }
                return true;
            }
            if (a1 == 23001000 || a1 == 14120045) {
                return true;
            }
            if (a1 <= 14121000) {
                return false;
            }
            boolean bool = (a1 == 14121002);
            return bool;
        }
        if (a1 <= 23110006) {
            if (a1 != 23110006 && a1 != 23101007) {
                return false;
            }
            return true;
        }
        if (a1 < 23111000) {
            return false;
        }
        boolean v1 = (a1 <= 23111003);
        return v1;
    }

    public static boolean is_super_nova_skill(int a1) {
        return (a1 == 4221052 || a1 == 65121052);
    }

    public static boolean sub_84ABA0(int a1) {
        return (a1 == 152110004 || a1 == 152120016 || a1 == 155121003);
    }

    public static boolean sub_849720(int a1) {
        if (a1 <= 64111012) {
            boolean v1;
            if (a1 == 64111012) {
                return true;
            }
            if (a1 > 3311013) {
                if (a1 == 3321005) {
                    return true;
                }
                v1 = (a1 == 3321039);
            } else {
                if (a1 == 3311013 || a1 == 3301004) {
                    return true;
                }
                v1 = (a1 == 3311011);
            }
            if (!v1) {
                return false;
            }
            return true;
        }
        if (a1 > 400021053) {
            boolean v1 = (a1 == 400031035);
            if (!v1) {
                return false;
            }
            return true;
        }
        if (a1 != 400021053) {
            if (a1 < 400020009) {
                return false;
            }
            if (a1 > 400020011) {
                boolean v1 = (a1 == 400021029);
                if (!v1) {
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    public static boolean sub_883680(int a1) {
        boolean v1;
        if (a1 > 61111113) {
            if (a1 > 101120203) {
                if (a1 > 400031004) {
                    v1 = (a1 == 400031036);
                } else {
                    if (a1 >= 400031003 || a1 == 101120205) {
                        return true;
                    }
                    v1 = (a1 == 400001018);
                }
            } else {
                if (a1 == 101120203) {
                    return true;
                }
                if (a1 > 80002247) {
                    if (a1 == 80002300) {
                        return true;
                    }
                    v1 = (a1 == 101120200);
                } else {
                    if (a1 == 80002247 || a1 == 61111218) {
                        return true;
                    }
                    v1 = (a1 == 64101002);
                }
            }
        } else {
            if (a1 == 61111113) {
                return true;
            }
            if (a1 > 14111022) {
                if (a1 > 27121201) {
                    if (a1 == 31201001) {
                        return true;
                    }
                    v1 = (a1 == 61111100);
                } else {
                    if (a1 == 27121201 || a1 == 22140015) {
                        return true;
                    }
                    v1 = (a1 - 22140015 == 9);
                }
            } else {
                if (a1 == 14111022) {
                    return true;
                }
                if (a1 > 5101014) {
                    if (a1 == 5301001) {
                        return true;
                    }
                    v1 = (a1 == 12121001);
                } else {
                    if (a1 == 5101014 || a1 == 2221012) {
                        return true;
                    }
                    v1 = false;
                    //  v1 = (a1 == 5101012);
                }
            }
        }
        return v1;
    }

    public static int sub_7F9870(int a1) {
        boolean v1;
        if (a1 > 37110001) {
            if (a1 == 37110004 || a1 - 37110004 == 996) {
                return 1;
            }
            v1 = (a1 - 37110004 == 999);
        } else {
            if (a1 == 37110001) {
                return 1;
            }
            if (a1 > 37100002) {
                v1 = (a1 == 37101001);
            } else {
                if (a1 == 37100002 || a1 == 37000010) {
                    return 1;
                }
                v1 = (a1 == 37001001);
            }
        }
        if (!v1) {
            return 0;
        }
        return 1;
    }

    public static boolean sub_8242D0(int a1) {
        boolean result;
        if (a1 <= 0) {
            return result = (a1 - 90000000 >= 0 && a1 - 90000000 < 12);
        }
        int v1 = a1 / 10000;
        if (a1 / 10000 == 8000) {
            v1 = a1 / 100;
        }
        if (v1 == 9500) {
            result = false;
        } else {
            result = (a1 - 90000000 >= 0 && a1 - 90000000 < 12);
        }
        return result;
    }

    public static int sub_846930(int a1) {
        if (a1 > 21110026) {
            if (a1 == 21110028 || a1 == 21120025) {
                return 1;
            }
        } else {
            if (a1 == 21110026) {
                return 1;
            }
            if (a1 > 21001010) {
                if (a1 >= 21110022 && a1 <= 21110023) {
                    return 1;
                }
            } else if (a1 == 21001010 || (a1 >= 21000006 && a1 <= 21000007)) {
                return 1;
            }
        }
        switch (a1) {
            case 80001925:
            case 80001926:
            case 80001927:
            case 80001936:
            case 80001937:
            case 80001938:
                return 1;
        }
        int result = 0;
        return result;
    }

    public static boolean sub_847580(int a1) {
        return (a1 == 37100002 || a1 - 37100002 == 9999 || a1 - 37100002 == 10002);
    }

    public static boolean is_shadow_assult(int a1) {
        return (Integer.toUnsignedLong(a1 - 400041002) <= 3L);
    }

    public static boolean sub_8327B0(int skill) {
        return (skill == 13111020);
    }

    public static boolean is_pathfinder_blast_skill(int a1) {
        boolean v1;
        if (a1 > 3321005) {
            if (a1 == 3321039) {
                return true;
            }
            v1 = (a1 == 400031035);
        } else {
            if (a1 == 3321005 || a1 == 3301004 || a1 == 3311011) {
                return true;
            }
            v1 = (a1 == 3311013);
        }
        if (!v1) {
            return false;
        }
        return true;
    }

    public static boolean sub_7FB860(int a1) {
        if (a1 <= 64111012) {
            boolean v1;
            if (a1 == 64111012) {
                return true;
            }
            if (a1 > 3311013) {
                if (a1 == 3321005) {
                    return true;
                }
                v1 = (a1 == 3321039);
            } else {
                if (a1 == 3311013 || a1 == 3301004) {
                    return true;
                }
                v1 = (a1 == 3311011);
            }
            return v1;
        }
        if (a1 > 400021053) {
            boolean v1 = (a1 == 400031035);
            return v1;
        }
        if (a1 != 400021053) {
            if (a1 < 400020009) {
                return false;
            }
            if (a1 > 400020011) {
                boolean v1 = (a1 == 400021029);
                return v1;
            }
        }
        return true;
    }

    public static boolean sub_6F5530(int a1) {
        boolean v1;
        if (a1 > 400021028) {
            if (a1 > 400041034) {
                if (a1 == 400051003 || a1 == 400051008) {
                    return true;
                }
                v1 = (a1 == 400051016);
                return v1;
            }
            if (a1 != 400041034) {
                if (a1 > 400041018) {
                    v1 = (a1 == 400041020);
                    return v1;
                }
                if (a1 < 400041016) {
                    switch (a1) {
                        case 400021047:
                        case 400021048:
                        case 400021064:
                        case 400021065:
                            return true;
                    }
                    return false;
                }
            }
            return true;
        }
        if (a1 == 400021028) {
            return true;
        }
        if (a1 > 152120003) {
            if (a1 <= 400021004) {
                if (a1 == 400021004 || a1 == 152121004) {
                    return true;
                }
                v1 = (a1 == 400011004);
                return v1;
            }
            if (a1 < 400021009 || a1 > 400021011) {
                return false;
            }
            return true;
        }
        if (a1 == 152120003) {
            return true;
        }
        if (a1 > 80002834) {
            v1 = (a1 == 152001002);
        } else {
            if (a1 == 80002834 || a1 == 80002691) {
                return true;
            }
            v1 = (a1 == 80002832);
        }
        return v1;
    }

    public static boolean is_screen_attack(int a1) {
        boolean v1;
        if (a1 > 21121057) {
            if (a1 == 80001431) {
                return true;
            }
            v1 = (a1 == 100001283);
        } else {
            if (a1 == 21121057 || a1 == 13121052 || a1 == 14121052) {
                return true;
            }
            v1 = (a1 == 15121052);
        }
        if (!v1) {
            return false;
        }
        return true;
    }

    public static boolean is_thunder_rune(int a1) {
        return (a1 == 80001762 || a1 == 80002212 || a1 == 80002463);
    }

    public static int get_evan_job_level(final int job) {
        int result = 0;
        switch (job) {
            case 2200:
            case 2210: {
                result = 1;
                break;
            }
            case 2211:
            case 2212:
            case 2213: {
                result = 2;
                break;
            }
            case 2214:
            case 2215:
            case 2216: {
                result = 3;
                break;
            }
            case 2217:
            case 2218: {
                result = 4;
                break;
            }
            default: {
                result = 0;
                break;
            }
        }
        return result;
    }

    public static int getJaguarIdByMob(int mob) {
        switch (mob) {
            case 9304004:
                return 1932033;
            case 9304003:
                return 1932032;
            case 9304002:
                return 1932031;
            case 9304001:
                return 1932030;
            case 9304000:
                return 1932015;
            case 9304005:
                return 1932036;
            case 9304006:
                return 1932100;
        }
        return 0;
    }

    public static int getJaguarType(int mobid) {
        return mobid - 9303999;
    }

    public static boolean is_evan_force_skill(int a1) {
        if (a1 > 22141012) {
            boolean v2;
            if (a1 > 400021012) {
                v2 = (a1 == 400021046);
            } else {
                if (a1 == 400021012) {
                    return true;
                }
                if (a1 < 22171062) {
                    return false;
                }
                if (a1 <= 22171063) {
                    return true;
                }
                v2 = (a1 == 80001894);
            }
            if (!v2) {
                return false;
            }
            return true;
        }
        if (a1 >= 22141011) {
            return true;
        }
        if (a1 > 22111017) {
            boolean v2 = (a1 == 22140022);
            if (!v2) {
                return false;
            }
            return true;
        }
        if (a1 == 22111017 || (a1 <= 22111012 && (a1 >= 22111011 || (a1 >= 22110022 && a1 <= 22110023)))) {
            return true;
        }
        return false;
    }

    public static int bullet_count_bonus(int a1) {
        if (a1 == 4121013) {
            return 4120051;
        }
        if (a1 == 5321012) {
            return 5320051;
        }
        if (a1 == 14121002) {
            return 14120045;
        }
        return 0;
    }

    public static int attack_count_bonus(int a1) {
        int v1 = 0;
        if (a1 <= 15120003) {
            if (a1 != 15120003) {
                if (a1 <= 5121020) {
                    if (a1 != 5121020) {
                        if (a1 <= 3121020) {
                            if (a1 == 3121020) {
                                return 3120051;
                            }
                            if (a1 > 1221011) {
                                if (a1 == 2121006) {
                                    return 2120048;
                                }
                                if (a1 == 2221006) {
                                    return 2220048;
                                }
                                if (a1 == 3121015) {
                                    return 3120048;
                                }
                            } else {
                                if (a1 == 1221011) {
                                    return 1220050;
                                }
                                if (a1 == 1120017 || a1 == 1121008) {
                                    return 1120051;
                                }
                                if (a1 == 1221009) {
                                    return 1220048;
                                }
                            }
                            if (sub_833A80(a1)) {
                                v1 = 3320030;
                            }
                            return v1;
                        }
                        if (a1 <= 4341009) {
                            if (a1 == 4341009) {
                                return 4340048;
                            }
                            if (a1 == 3221017) {
                                return 3220048;
                            }
                            if (a1 == 4221007) {
                                return 4220048;
                            }
                            if (a1 == 4331000) {
                                return 4340045;
                            }
                            if (sub_833A80(a1)) {
                                v1 = 3320030;
                            }
                            return v1;
                        }
                        if (a1 != 5121007) {
                            if (Integer.toUnsignedLong(a1 - 5121016) <= 1L) {
                                return 5120051;
                            }
                            if (sub_833A80(a1)) {
                                v1 = 3320030;
                            }
                            return v1;
                        }
                    }
                    return 5120048;
                }
                if (a1 <= 12100028) {
                    if (a1 != 12100028) {
                        if (a1 <= 5321004) {
                            if (a1 == 5321001) {
                                return 5320043;
                            }
                            if (a1 == 5221016) {
                                return 5220047;
                            }
                            if (a1 == 5320011) {
                                return 5320043;
                            }
                            if (a1 == 5321000) {
                                return 5320048;
                            }
                            if (sub_833A80(a1)) {
                                v1 = 3320030;
                            }
                            return v1;
                        }
                        if (a1 == 11121103 || a1 - 11121103 == 100) {
                            return 11120048;
                        }
                        if (a1 - 11121103 != 878923) {
                            if (sub_833A80(a1)) {
                                v1 = 3320030;
                            }
                            return v1;
                        }
                    }
                    return 12120045;
                }
                if (a1 <= 13121002) {
                    if (a1 == 13121002) {
                        return 13120048;
                    }
                    if (a1 != 12110028 && a1 - 12110028 != 9982) {
                        if (a1 - 12110028 == 9983) {
                            return 12120046;
                        }
                        if (sub_833A80(a1)) {
                            v1 = 3320030;
                        }
                        return v1;
                    }
                    return 12120045;
                }
                if (a1 == 14121002) {
                    return 14120045;
                }
                if (sub_833A80(a1)) {
                    v1 = 3320030;
                }
                return v1;
            }
            return 15120045;
        }
        if (a1 <= 51121008) {
            if (a1 == 51121008) {
                return 51120048;
            }
            if (a1 <= 25121005) {
                if (a1 != 25121005) {
                    if (a1 <= 21120006) {
                        if (a1 == 21120006) {
                            return 21120049;
                        }
                        if (a1 == 15121002) {
                            return 15120048;
                        }
                        if (a1 == 21110020 || a1 == 21111021) {
                            return 21120047;
                        }
                        if (sub_833A80(a1)) {
                            v1 = 3320030;
                        }
                        return v1;
                    }
                    if (a1 > 21121017) {
                        if (a1 == 22140023) {
                            return 22170086;
                        }
                        if (sub_833A80(a1)) {
                            v1 = 3320030;
                        }
                        return v1;
                    }
                    if (a1 >= 21121016 || a1 == 21120022) {
                        return 21120066;
                    }
                    if (sub_833A80(a1)) {
                        v1 = 3320030;
                    }
                    return v1;
                }
                return 25120148;
            }
            if (a1 <= 37120001) {
                if (a1 == 37120001) {
                    return 37120045;
                }
                if (a1 == 31121001) {
                    return 31120050;
                }
                if (a1 == 35121016) {
                    return 35120051;
                }
                if (a1 == 37110002) {
                    return 37120045;
                }
                if (sub_833A80(a1)) {
                    v1 = 3320030;
                }
                return v1;
            }
            if (a1 != 51120057) {
                if (a1 == 51121007) {
                    return 51120051;
                }
                if (sub_833A80(a1)) {
                    v1 = 3320030;
                }
                return v1;
            }
            return 51120058;
        }
        if (a1 <= 152110004) {
            if (a1 != 152110004) {
                if (a1 <= 65121008) {
                    if (a1 < 65121007) {
                        if (a1 == 51121009) {
                            return 51120058;
                        }
                        if (a1 - 51121009 != 10000091 && a1 - 51121009 - 10000091 != 101) {
                            if (sub_833A80(a1)) {
                                v1 = 3320030;
                            }
                            return v1;
                        }
                        return 61120045;
                    }
                    return 65120051;
                }
                if (a1 == 65121101) {
                    return 65120051;
                }
                if (a1 != 152001001) {
                    if (sub_833A80(a1)) {
                        v1 = 3320030;
                    }
                    return v1;
                }
            }
            return 152120032;
        }
        if (a1 > 400010070) {
            if (a1 < 400011079) {
                if (sub_833A80(a1)) {
                    v1 = 3320030;
                }
                return v1;
            }
            if (a1 > 400011082) {
                if (a1 != 400051043) {
                    if (sub_833A80(a1)) {
                        v1 = 3320030;
                    }
                    return v1;
                }
                return 25120148;
            }
            return 61120045;
        }
        if (a1 == 400010070) {
            return 21120066;
        }
        if (a1 > 152121004) {
            if (a1 <= 152121006) {
                return 152120038;
            }
            if (sub_833A80(a1)) {
                v1 = 3320030;
            }
            return v1;
        }
        if (a1 == 152121004) {
            return 152120035;
        }
        if (a1 == 152120001) {
            return 152120032;
        }
        if (sub_833A80(a1)) {
            v1 = 3320030;
        }
        return v1;
    }

    public static boolean sub_833A80(int a1) {
        return (a1 == 3011004 || a1 == 3300002 || a1 == 3321003 || a1 == 3301003 || a1 == 3310001 || a1 == 3321004 || a1 == 3301004 || a1 == 3311013 || a1 == 3321005 || a1 == 3311002 || a1 == 3321006 || a1 == 3311003 || a1 == 3321007);
    }

    public static int checkMountItem(int sourceid) {
        return getMountItemEx(sourceid);
    }

    public static boolean isSeparatedSp(int a1) {
        if (a1 == 0 || a1 == 100
                || isHero(a1) || isPaladin(a1) || isDarkKnight(a1) || a1 == 200
                || isFPMage(a1) || isILMage(a1) || isBishop(a1) || a1 == 300
                || isBowMaster(a1) || isMarksMan(a1) || isPathFinder(a1) || a1 == 400
                || isNightLord(a1) || isShadower(a1) || isDualBlade(a1) || a1 == 500
                || isViper(a1) || isCaptain(a1) || isCannon(a1)
                || isIllium(a1)
                || isArk(a1)
                || isKOC(a1)
                || isResist(a1)
                || isEvan(a1)
                || isMercedes(a1)
                || isPhantom(a1)
                || isMichael(a1)
                || isLuminous(a1)
                || isKaiser(a1) || isKadena(a1) || isAngelicBuster(a1)
                || isAdel(a1)
                || isZero(a1)
                || isEunWol(a1)
                || isAran(a1)
                || isKinesis(a1)
                || isHoyeong(a1)
                || isCain(a1)
                || isLara(a1)) {
            return true;
        }
        return false;
    }

    public static byte[] getServerIp(String ip) {
        return new byte[]{-81, -49, 0, 33};
    }

    public static boolean sub_1F04F40(int a1) {
        boolean v1;
        if (a1 > 13121009) {
            if (a1 == 36110005) {
                return true;
            }
            v1 = (a1 == 65101006);
        } else {
            if (a1 == 13121009 || a1 == 11121013) {
                return true;
            }
            v1 = (a1 == 12100029);
        }
        return v1;
    }

    public static boolean isLinkMap(int mapid) {
        switch (mapid) {
            case 940711300:
            case 993014200:
            case 993018200:
            case 993021200:
            case 993029200:
                return true;
        }
        return false;
    }

    public static double UnionAttackerRate(int level) {
        if (level >= 240) {
            return 1.25D;
        }
        if (level >= 230) {
            return 1.2D;
        }
        if (level >= 220) {
            return 1.15D;
        }
        if (level >= 210) {
            return 1.1D;
        }
        if (level >= 200) {
            return 1.0D;
        }
        if (level >= 180) {
            return 0.8D;
        }
        if (level >= 140) {
            return 0.7D;
        }
        if (level >= 100) {
            return 0.4D;
        }
        if (level >= 60) {
            return 0.5D;
        }
        return 0.0D;
    }

    public static double UnionStarForceRate(int starforce) {
        if (starforce >= 350) {
            return 0.18D;
        }
        if (starforce >= 320) {
            return 0.17D;
        }
        if (starforce >= 290) {
            return 0.16D;
        }
        if (starforce >= 260) {
            return 0.15D;
        }
        if (starforce >= 230) {
            return 0.14D;
        }
        if (starforce >= 180) {
            return 0.13D;
        }
        if (starforce >= 120) {
            return 0.12D;
        }
        if (starforce >= 60) {
            return 0.11D;
        }
        if (starforce >= 0) {
            return 0.1D;
        }
        return 0.0D;
    }

    public static double UnionStarForceRate2(int starforce) {
        if (starforce >= 350) {
            return 27.0D;
        }
        if (starforce >= 320) {
            return 25.5D;
        }
        if (starforce >= 290) {
            return 24.0D;
        }
        if (starforce >= 260) {
            return 22.5D;
        }
        if (starforce >= 230) {
            return 21.0D;
        }
        if (starforce >= 180) {
            return 19.5D;
        }
        if (starforce >= 120) {
            return 18.0D;
        }
        if (starforce >= 60) {
            return 16.5D;
        }
        if (starforce >= 0) {
            return 15.0D;
        }
        return 0.0D;
    }

    public static int UnionStarForceRate3(int starforce) {
        if (starforce >= 350) {
            return 1350;
        }
        if (starforce >= 320) {
            return 1275;
        }
        if (starforce >= 290) {
            return 1200;
        }
        if (starforce >= 260) {
            return 1125;
        }
        if (starforce >= 230) {
            return 1050;
        }
        if (starforce >= 180) {
            return 975;
        }
        if (starforce >= 120) {
            return 900;
        }
        if (starforce >= 60) {
            return 825;
        }
        if (starforce >= 0) {
            return 750;
        }
        return 0;
    }

    public static int UnionStarForceRate4(int starforce) {
        if (starforce >= 350) {
            return 10000;
        }
        if (starforce >= 320) {
            return 8750;
        }
        if (starforce >= 290) {
            return 7500;
        }
        if (starforce >= 260) {
            return 6250;
        }
        if (starforce >= 230) {
            return 5000;
        }
        if (starforce >= 180) {
            return 3750;
        }
        if (starforce >= 120) {
            return 2500;
        }
        if (starforce >= 60) {
            return 1250;
        }
        if (starforce >= 0) {
            return 0;
        }
        return 0;
    }

    public static boolean isUnionRaid(int mapid) {
        switch (mapid) {
            case 921172000:
            case 921172100:
                return true;
        }
        return false;
    }

    public static boolean isUnionAccountMob(int mobid) {
        switch (mobid) {
            case 9833101:
            case 9833102:
            case 9833103:
            case 9833104:
            case 9833105:
                return true;
        }
        return false;
    }

    public static boolean isTowerChair(int chairid) {
        return (chairid / 1000 == 3017);
    }

    public static boolean isTextChair(int chairid) {
        return (chairid / 1000 == 3014);
    }

    public static boolean isCooltimeKeyDownSkill(int skillid) {
        if (is_keydown_skill(skillid) || is_super_nova_skill(skillid)) {
            return true;
        }
        switch (skillid) {
            case 2221011:
            case 3301008:
            case 3321035:
            case 3321036:
            case 3321037:
            case 3321038:
            case 3321039:
            case 3321040:
            case 4221052:
            case 5311010:
            case 11121014:
            case 11121052:
            case 11121056:
            case 12121054:
            case 12121055:
            case 14121003:
            case 14121004:
            case 21120018:
            case 21120019:
            case 21120023:
            case 21120026:
            case 21120027:
            case 22110014:
            case 22110023:
            case 22110025:
            case 22140022:
            case 22171063:
            case 22171080:
            case 24120055:
            case 24121005:
            case 25121030:
            case 31211001:
            case 37120059:
            case 63121042:
            case 64111004:
            case 64111012:
            case 64121002:
            case 64121003:
            case 64121011:
            case 65121003:
            case 65121052:
            case 101110201:
            case 131001004:
            case 131001113:
            case 131003023:
            case 131004023:
            case 131005023:
            case 131006023:
            case 135001008:
            case 135001016:
            case 135001019:
            case 151111002:
            case 151121003:
            case 152121004:
            case 155101104:
            case 155101114:
            case 155101204:
            case 155101214:
            case 155111202:
            case 155111211:
            case 155111212:
            case 155111306:
            case 155121341:
            case 400011028:
            case 400011030:
            case 400011031:
            case 400011038:
            case 400011039:
            case 400011047:
            case 400011048:
            case 400011049:
            case 400011068:
            case 400011069:
            case 400011089:
            case 400011091:
            case 400011092:
            case 400011093:
            case 400011094:
            case 400011095:
            case 400011096:
            case 400011097:
            case 400011103:
            case 400011108:
            case 400011109:
            case 400011110:
            case 400011111:
            case 400011119:
            case 400011120:
            case 400011131:
            case 400011132:
            case 400011134:
            case 400011135:
            case 400021004:
            case 400021012:
            case 400021031:
            case 400021040:
            case 400021046:
            case 400021061:
            case 400021086:
            case 400021092:
            case 400021094:
            case 400021095:
            case 400021129:
            case 400021130:
            case 400021131:
            case 400031000:
            case 400031001:
            case 400031024:
            case 400031045:
            case 400031058:
            case 400031064:
            case 400031066:
            case 400040006:
            case 400041002:
            case 400041003:
            case 400041004:
            case 400041005:
            case 400041006:
            case 400041007:
            case 400041021:
            case 400041055:
            case 400041059:
            case 400041069:
            case 400041080:
            case 400051006:
            case 400051016:
            case 400051017:
            case 400051040:
            case 400051041:
            case 400051070:
            case 400051073:
            case 400051074:
            case 400051075:
            case 400051078:
            case 400051079:
                return true;
        }
        return false;
    }

    public static boolean isAutoAttackSkill(int skillid) {
        switch (skillid) {
            case 3111013:
            case 11121052:
            case 31121005:
            case 31221001:
            case 142100010:
            case 151101006:
            case 151101007:
            case 151101008:
            case 151101009:
            case 151111003:
            case 400011052:
            case 400011118:
            case 400011131:
            case 400011132:
            case 400021086:
            case 400031000:
            case 400031064:
            case 400031066:
            case 400041002:
            case 400041061:
            case 400041062:
            case 400041079:
            case 400051015:
                return true;
        }
        return false;
    }

    public static boolean isNoApplySkill(int skillid) {
        if (isTryFling(skillid)) {
            return true;
        }
        switch (skillid) {
            case 0:
            case 2120013:
            case 2121006:
            case 2121007:
            case 2121011:
            case 2121052:
            case 2121054:
            case 2220014:
            case 2221012:
            case 2321007:
            case 2321055:
            case 3011004:
            case 3100010:
            case 3101008:
            case 3111013:
            case 3300002:
            case 3300005:
            case 3301004:
            case 3301009:
            case 3310004:
            case 3311003:
            case 3311011:
            case 3311013:
            case 3321003:
            case 3321005:
            case 3321007:
            case 3321015:
            case 3321017:
            case 3321019:
            case 3321021:
            case 3321035:
            case 3321036:
            case 3321037:
            case 3321038:
            case 3321039:
            case 3321040:
            case 4100012:
            case 4120019:
            case 4210014:
            case 4221013:
            case 4221052:
            case 4321002:
            case 4341052:
            case 4341054:
            case 5101012:
            case 5211014:
            case 5221015:
            case 5300007:
            case 5301001:
            case 12100029:
            case 12111003:
            case 12120011:
            case 12121055:
            case 13100022:
            case 13100027:
            case 13110022:
            case 13110027:
            case 13120003:
            case 13120010:
            case 13121054:
            case 14000028:
            case 14000029:
            case 14111022:
            case 14111023:
            case 14121004:
            case 15111022:
            case 15120003:
            case 21001008:
            case 21100015:
            case 21120021:
            case 21120024:
            case 22140024:
            case 22171083:
            case 24100003:
            case 24111008:
            case 24120002:
            case 24120055:
            case 24121010:
            case 25111005:
            case 25111209:
            case 25111211:
            case 27101202:
            case 27111100:
            case 27120211:
            case 27121201:
            case 31101002:
            case 31121005:
            case 31201001:
            case 31221014:
            case 32111016:
            case 32120052:
            case 32120055:
            case 32121011:
            case 35001002:
            case 35101002:
            case 35110017:
            case 35111003:
            case 35120017:
            case 35121003:
            case 36001005:
            case 36101001:
            case 36110004:
            case 37000009:
            case 37111006:
            case 37120055:
            case 37120056:
            case 37120057:
            case 37120058:
            case 37120059:
            case 37121004:
            case 37121052:
            case 61101002:
            case 61110211:
            case 61111100:
            case 64101002:
            case 64101008:
            case 64101009:
            case 64111013:
            case 64121012:
            case 64121013:
            case 64121014:
            case 64121015:
            case 64121017:
            case 64121018:
            case 64121019:
            case 64121020:
            case 64121022:
            case 64121023:
            case 64121024:
            case 64121055:
            case 65111007:
            case 65120011:
            case 65121052:
            case 80001762:
            case 80001770:
            case 80002890:
            case 95001000:
            case 101120205:
            case 131001023:
            case 135001007:
            case 142100010:
            case 142101009:
            case 142110003:
            case 142110011:
            case 142111006:
            case 142111007:
            case 142120001:
            case 142120002:
            case 142120003:
            case 142121030:
            case 151001001:
            case 151101001:
            case 151101006:
            case 151101010:
            case 151111003:
            case 151121001:
            case 152120001:
            case 155001000:
            case 155100009:
            case 155101002:
            case 155111003:
            case 155111207:
            case 155121003:
            case 164001002:
            case 164101004:
            case 164120007:
            case 164121004:
            case 400001010:
            case 400001018:
            case 400001038:
            case 400001055:
            case 400010010:
            case 400010030:
            case 400011001:
            case 400011002:
            case 400011004:
            case 400011019:
            case 400011031:
            case 400011047:
            case 400011050:
            case 400011052:
            case 400011053:
            case 400011058:
            case 400011059:
            case 400011072:
            case 400011074:
            case 400011075:
            case 400011076:
            case 400011080:
            case 400011081:
            case 400011082:
            case 400011085:
            case 400011087:
            case 400011099:
            case 400011101:
            case 400011102:
            case 400011106:
            case 400011107:
            case 400011108:
            case 400011109:
            case 400011110:
            case 400011111:
            case 400011113:
            case 400011114:
            case 400011115:
            case 400011118:
            case 400011119:
            case 400011120:
            case 400011122:
            case 400011130:
            case 400011131:
            case 400011136:
            case 400020009:
            case 400020010:
            case 400020011:
            case 400020051:
            case 400021001:
            case 400021004:
            case 400021008:
            case 400021009:
            case 400021010:
            case 400021011:
            case 400021028:
            case 400021029:
            case 400021041:
            case 400021045:
            case 400021047:
            case 400021048:
            case 400021049:
            case 400021050:
            case 400021053:
            case 400021061:
            case 400021063:
            case 400021064:
            case 400021066:
            case 400021070:
            case 400021075:
            case 400021076:
            case 400021077:
            case 400021088:
            case 400021092:
            case 400021094:
            case 400021096:
            case 400021097:
            case 400021098:
            case 400021100:
            case 400021111:
            case 400021112:
            case 400031000:
            case 400031001:
            case 400031003:
            case 400031004:
            case 400031012:
            case 400031013:
            case 400031014:
            case 400031015:
            case 400031016:
            case 400031017:
            case 400031018:
            case 400031019:
            case 400031020:
            case 400031021:
            case 400031022:
            case 400031025:
            case 400031026:
            case 400031031:
            case 400031032:
            case 400031033:
            case 400031035:
            case 400031036:
            case 400031048:
            case 400031050:
            case 400031054:
            case 400031056:
            case 400031059:
            case 400031064:
            case 400041002:
            case 400041003:
            case 400041004:
            case 400041005:
            case 400041007:
            case 400041010:
            case 400041016:
            case 400041017:
            case 400041018:
            case 400041020:
            case 400041022:
            case 400041023:
            case 400041024:
            case 400041034:
            case 400041036:
            case 400041037:
            case 400041038:
            case 400041039:
            case 400041040:
            case 400041043:
            case 400041045:
            case 400041046:
            case 400041053:
            case 400041058:
            case 400041060:
            case 400041062:
            case 400041064:
            case 400041065:
            case 400041066:
            case 400041070:
            case 400041071:
            case 400041072:
            case 400041073:
            case 400041074:
            case 400041076:
            case 400041077:
            case 400041078:
            case 400041079:
            case 400051003:
            case 400051004:
            case 400051005:
            case 400051006:
            case 400051007:
            case 400051008:
            case 400051013:
            case 400051015:
            case 400051018:
            case 400051019:
            case 400051020:
            case 400051025:
            case 400051026:
            case 400051027:
            case 400051035:
            case 400051039:
            case 400051041:
            case 400051044:
            case 400051045:
            case 400051047:
            case 400051048:
            case 400051049:
            case 400051050:
            case 400051059:
            case 400051060:
            case 400051061:
            case 400051062:
            case 400051063:
            case 400051064:
            case 400051065:
            case 400051066:
            case 400051067:
            case 400051069:
            case 400051071:
            case 400051072:
            case 400051074:
            case 400051075:
            case 400051078:
            case 400051079:
            case 400051080:
            case 400051334:
                return true;
        }
        return false;
    }

    public static long getDreamBreakerHP(int stage) {
        if (stage < 10) {
            return 220000000L;
        }
        if (stage >= 10 && stage < 20) {
            return 500000000L;
        }
        if (stage >= 20 && stage < 30) {
            return 1200000000L;
        }
        if (stage >= 30 && stage < 40) {
            return 2300000000L;
        }
        if (stage >= 40 && stage < 50) {
            return 5400000000L;
        }
        if (stage >= 50 && stage < 60) {
            return 9750000000L;
        }
        if (stage >= 60 && stage < 70) {
            return 15250000000L;
        }
        if (stage >= 70 && stage < 80) {
            return 24700000000L;
        }
        if (stage >= 80 && stage < 90) {
            return 36000000000L;
        }
        if (stage == 90) {
            return 50000000000L;
        }
        if (stage >= 91 && stage < 100) {
            return 87000000000L;
        }
        if (stage == 100) {
            return 135000000000L;
        }
        if (stage >= 101 && stage < 110) {
            return 335000000000L;
        }
        if (stage >= 110 && stage < 120) {
            return 373000000000L;
        }
        if (stage >= 120 && stage < 130) {
            return 403000000000L;
        }
        if (stage >= 130 && stage < 140) {
            return 435000000000L;
        }
        if (stage >= 140 && stage < 150) {
            return 469000000000L;
        }
        if (stage >= 150 && stage < 160) {
            return 503000000000L;
        }
        if (stage >= 160 && stage < 170) {
            return 533000000000L;
        }
        if (stage >= 170 && stage < 180) {
            return 569000000000L;
        }
        if (stage >= 180 && stage < 190) {
            return 603000000000L;
        }
        if (stage >= 190 && stage < 200) {
            return 635000000000L;
        }
        if (stage == 200) {
            return 669000000000L;
        }
        if (stage == 201) {
            return 4700000000000L;
        }
        return Math.max(1L, 4700000000000L * (stage - 201));
    }

    public static boolean isNoReflectDamageSkill(Skill skill) {
        if (skill.isVMatrix() || is_forceAtom_attack_skill(skill.getId())) {
            return true;
        }
        switch (skill.getId()) {
            case 2100010:
            case 2101010:
            case 2121052:
            case 2121054:
            case 2121055:
            case 2201009:
            case 2221012:
            case 2221052:
            case 4111003:
            case 4341052:
            case 5121013:
            case 5221013:
            case 5321001:
            case 11121052:
            case 11121055:
            case 12120011:
            case 14121003:
            case 14121004:
            case 22110014:
            case 22110022:
            case 22110023:
            case 22110024:
            case 22110025:
            case 22111012:
            case 22140014:
            case 22140015:
            case 22140022:
            case 22140023:
            case 22140024:
            case 22141012:
            case 22170065:
            case 22170066:
            case 22170067:
            case 22171063:
            case 22171081:
            case 23111008:
            case 23111009:
            case 23111010:
            case 24120055:
            case 25100010:
            case 25120115:
            case 25121006:
            case 31220013:
            case 31221001:
            case 31221014:
            case 32001014:
            case 32121004:
            case 32121011:
            case 33001016:
            case 33101115:
            case 33111013:
            case 33111015:
            case 33120056:
            case 33121012:
            case 33121017:
            case 33121255:
            case 35101002:
            case 35101012:
            case 35111002:
            case 35121009:
            case 35121052:
            case 36001005:
            case 36110004:
            case 36110005:
            case 36111004:
            case 36121002:
            case 36121013:
            case 37001002:
            case 37001004:
            case 37111000:
            case 51001005:
            case 61111002:
            case 61111100:
            case 61111113:
            case 61111218:
            case 61111220:
            case 64101009:
            case 64111012:
            case 64111013:
            case 64121020:
            case 65111007:
            case 65111100:
            case 65121052:
            case 101100100:
            case 101100101:
            case 101100201:
            case 101100202:
            case 101110200:
            case 101110201:
            case 101110204:
            case 101120101:
            case 101120103:
            case 101120105:
            case 101120106:
            case 101120203:
            case 101120205:
            case 101120206:
            case 131001004:
            case 131001007:
            case 131001008:
            case 131001011:
            case 142101009:
            case 151111003:
            case 152001003:
            case 152101008:
            case 152121005:
            case 152121006:
            case 155001000:
            case 155101002:
            case 155101008:
            case 155111003:
            case 155111207:
            case 155111306:
            case 155121003:
            case 155121306:
                return true;
        }
        return false;
    }

    public static boolean isMagneticPet(int id) {
        switch (id) {
            case 5000930:
            case 5000931:
            case 5000932:
                return true;
        }
        return false;
    }

    public static void attackBonusRecv(LittleEndianAccessor lea, AttackInfo ret) {
        /* 13200 */ lea.readByte();
        /* 13201 */ lea.readByte();
        /* 13202 */ ret.slot = lea.readShort();
        /* 13203 */ ret.item = lea.readInt();
        /* 13204 */ lea.skip(1);
        /* 13205 */ lea.skip(1);
        /* 13206 */ lea.skip(1);
        /* 13207 */ lea.skip(1);
        /* 13208 */ lea.skip(4);
        /* 13209 */ ret.position = new Point(lea.readInt(), lea.readInt());
        /* 13210 */ lea.skip(4);
        /* 13211 */ lea.skip(4);
        /* 13212 */ int c = lea.readInt();
        lea.skip(2);
        /* 13213 */ for (int i = 0; i < c; i++) {
            /* 13214 */ lea.readInt();
        }
    }
    
        public static void TangyoonCookingClass(final MapleClient c, int number) {
//         // 0 : 돼지고기볶음, 1 : 달팽이요리, 2 : 해파리냉채, 3 : 버섯칼국수, 4 : 슬라임푸딩
         MapleCharacter chr = c.getPlayer();

         int ratio = 0;
         if (number == 0) {
             chr.setKeyValue(2498, "TangyoonCooking", "" + Randomizer.rand(0, 4));
         }
         int random = Randomizer.rand(0, 4);
         int[] Ingredients = null;
         int[] failure = null;
         int[] mobList = null;
         switch ((int) chr.getKeyValue(2498, "TangyoonCooking")) { //  0부터 19개 랜덤돌리면됨
             case 0:
                 Ingredients = new int[]{9300654, 9300655, 9300656, 9300657, 9300658};
                 failure = new int[]{9300659, 9300660, 9300661, 9300662, 9300663, 9300664, 9300665, 9300666, 9300667, 9300668, 9300669, 9300670, 9300671, 9300672, 9300673, 9300674, 9300675, 9300676, 9300677, 9300678};
                 break;
             case 1:
                 Ingredients = new int[]{9300659, 9300660, 9300661, 9300662, 9300663};
                 failure = new int[]{9300654, 9300655, 9300656, 9300657, 9300658, 9300664, 9300665, 9300666, 9300667, 9300668, 9300669, 9300670, 9300671, 9300672, 9300673, 9300674, 9300675, 9300676, 9300677, 9300678};
                 break;
             case 2:
                 Ingredients = new int[]{9300664, 9300665, 9300666, 9300667, 9300668};
                 failure = new int[]{9300654, 9300655, 9300656, 9300657, 9300658, 9300659, 9300660, 9300661, 9300662, 9300663, 9300669, 9300670, 9300671, 9300672, 9300673, 9300674, 9300675, 9300676, 9300677, 9300678};
                 break;
             case 3:
                 Ingredients = new int[]{9300669, 9300670, 9300671, 9300672, 9300673};
                 failure = new int[]{9300654, 9300655, 9300656, 9300657, 9300658, 9300659, 9300660, 9300661, 9300662, 9300663, 9300664, 9300665, 9300666, 9300667, 9300668, 9300674, 9300675, 9300676, 9300677, 9300678};
                 break;
             case 4:
                 Ingredients = new int[]{9300674, 9300675, 9300676, 9300677, 9300678};
                 failure = new int[]{9300654, 9300655, 9300656, 9300657, 9300658, 9300659, 9300660, 9300661, 9300662, 9300663, 9300664, 9300665, 9300666, 9300667, 9300668, 9300669, 9300670, 9300671, 9300672, 9300673};
                 break;
         }
         int rd1=Randomizer.rand(0, 19),rd2=Randomizer.rand(0, 19),rd3=Randomizer.rand(0, 19),rd4=Randomizer.rand(0, 19);
         while(rd1==rd2||rd1==rd3||rd1==rd4||rd2==rd3||rd2==rd4||rd3==rd4) {
             rd1=Randomizer.rand(0, 19); 
             rd2=Randomizer.rand(0, 19);
             rd3=Randomizer.rand(0, 19);
             rd4=Randomizer.rand(0, 19);
         }
         if (number < 5) {
             if(random == 0) {
                 mobList = new int[]{Ingredients[number], failure[rd1], failure[rd2], failure[rd3], failure[rd4]};
             } else if (random == 1) {
                 mobList = new int[]{failure[rd1], Ingredients[number], failure[rd2], failure[rd3], failure[rd4]};
             } else if (random == 2) {
                 mobList = new int[]{failure[rd1], failure[rd2], Ingredients[number], failure[rd3], failure[rd4]};
             } else if (random == 3) {
                 mobList = new int[]{failure[rd1], failure[rd2], failure[rd3], Ingredients[number], failure[rd4]};
             } else if (random == 4) {
                 mobList = new int[]{failure[rd1], failure[rd2], failure[rd3], failure[rd4], Ingredients[number]};
             }
         }
         
         switch (number) {
             case 0:
                 chr.setKeyValue(2498, "TangyoonCookingClass", "1");
                 break;
             case 1:
                 chr.setKeyValue(2498, "TangyoonCookingClass", "2");
                 ratio = 20;
                 break;
             case 2:
                 chr.setKeyValue(2498, "TangyoonCookingClass", "3");
                 ratio = 40;
                 break;
             case 3:
                 chr.setKeyValue(2498, "TangyoonCookingClass", "4");
                 ratio = 60;
                 break;
             case 4:
                 chr.setKeyValue(2498, "TangyoonCookingClass", "5");
                 ratio = 80;
                 break;
             case 5:
                 chr.setKeyValue(2498, "TangyoonCookingClass", "6");
                 break;
         }
         
         if (number < 5) {
             c.getSession().writeAndFlush(CField.achievementRatio(ratio));
             c.getSession().writeAndFlush(CField.UIPacket.openUIOption(122,(int) chr.getKeyValue(2498, "TangyoonCooking"),0));//TangyunMobList
             c.getSession().writeAndFlush(CField.TangyoonMobList(mobList[0],mobList[1],mobList[2],mobList[3],mobList[4]));
         } else {
             c.getSession().writeAndFlush(CField.UIPacket.openUIOption(123,Randomizer.rand(0, 10),0));//solt
         }
    }
    
    public static void TangyoonMob(MapleClient c, int mobid) {
        int a = 0;
        MapleMap map = c.getChannelServer().getMapFactory().getMap(912080100);
        for (int i=0;i<10;i++) {
            a = Randomizer.rand(220, 1500);
            if (c.getPlayer().getMapId() == 912080100) {
                map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(a, -69));
            }
        } 
    }
    
    public static int tangyoonloop;
    public static void TangyoonMobSpawn(MapleClient c, int mobid, boolean cancel) {
        if (cancel) {
            c.getPlayer().getMap().resetFully();
        }
        tangyoonloop = 0;
        Timer tangTimer = new Timer();
        TimerTask tangTask = new TimerTask() {
            @Override
            public void run() {
                if(cancel || c.getPlayer().getMapId() != 912080100) {
                    tangTimer.cancel();
                    TangYoonMobDelete(c, mobid, true, false, false);
                    return;
                }
                if (tangyoonloop < 9) {
                    TangyoonMob(c,mobid);
                    tangyoonloop++;
                } else {
                    TangYoonMobDelete(c, mobid, false, false, false);
                    tangTimer.cancel();
                }
                
            }
        };
        tangTimer.schedule(tangTask, 1, 8000);
    }
    
    public static void TangYoonMobDelete(MapleClient c, int mobid, boolean cancel, boolean fire, boolean boss) {
        
        Timer tangTimer = new Timer();
        TimerTask tangTask = new TimerTask() {
            @Override
            public void run() {
                if(cancel ||c.getPlayer().getMapId() != 912080100) {
                    tangTimer.cancel();
                    return;
                }
                if (c.getPlayer().getMap().getNumMonsters() <= 0 && !fire && !boss) {
                    TangyoonCookingClass(c, (int) c.getPlayer().getKeyValue(2498, "TangyoonCookingClass"));
                    tangTimer.cancel();
                } else if (c.getPlayer().getMap().getNumMonsters() <= 0 && fire && !boss) {
                    GameConstants.TangyoonMobSpawn(c, mobid, false);
                    tangTimer.cancel();
                }
                if (c.getPlayer().getMap().getNumMonsters() <= 0 && boss) {
                    if ((int) c.getPlayer().getKeyValue(2498, "TangyoonBoss") == 1) {
                        c.getPlayer().warp(912080120);
                    } else {
                        c.getPlayer().warp(912080110);
                    }
                    c.getPlayer().removeKeyValue(2498, "TangyoonBoss");
                    c.getPlayer().removeKeyValue(2498, "TangyoonCooking");
                    c.getPlayer().removeKeyValue(2498, "TangyoonCookingClass");      
                    String talk = "요리를 모두 마쳤습니다. 잠시 후 퇴장맵으로 이동됩니다.";
                    c.getSession().writeAndFlush(CField.getGameMessage(11, talk)); //pink
                    c.getSession().writeAndFlush(CWvsContext.getTopMsg(talk));
                    
                    tangTimer.cancel();
                }
            }
        };
        tangTimer.schedule(tangTask, 1, 500);
    }

    public static void calcAttackPosition(LittleEndianAccessor lea, AttackInfo ret) {
       // System.err.println(lea);
        /* 13219 */ lea.skip(4);
        /* 13220 */ byte aa = lea.readByte();
        /* 13221 */ if (aa != 0) {
            /* 13222 */ int k = -1;
            lea.skip(4);
            do {
                byte x;
                /* 13225 */ k = lea.readInt();

                /* 13227 */ switch (k) {
                    case 1:
                        /* 13229 */ x = lea.readByte();
                        /* 13230 */ if (x > 0) {
                            /* 13231 */ lea.readInt();
                            /* 13232 */ lea.readInt();
                            /* 13233 */ lea.readInt();
                        }
                        break;

                    case 2:
                        /* 13238 */ x = lea.readByte();
                        /* 13239 */ if (x > 0) {
                            /* 13240 */ lea.skip(19);
                        }
                        break;

                    case 3:
                        /* 13245 */ if (lea.readByte() == 1) {
                            /* 13246 */ ret.isLink = (lea.readByte() == 1);
                            /* 13247 */ lea.readInt();
                        }
                        break;

                    case 4:
                        /* 13252 */ x = lea.readByte();
                        /* 13253 */ if (x > 0) {
                            /* 13254 */ lea.skip(28);
                        }
                        break;

                    case 5:
                        /* 13259 */ if (ret.skill != 400021042 && ret.skill != 400021043 && ret.skill != 400021044 && ret.skill != 400021045) {
                            /* 13260 */ lea.readByte();
                        }
                        break;

                    case 6:
                        /* 13265 */ if (ret.skill != 400021042 && ret.skill != 400021043 && ret.skill != 400021044 && ret.skill != 400021045) {
                            /* 13266 */ lea.readByte();
                        }
                        break;

                    case 7:
                        /* 13271 */ x = lea.readByte();
                        /* 13272 */ if (x > 0) {
                            /* 13273 */ ret.plusPosition2 = new Point(lea.readInt(), lea.readInt());
                            /* 13274 */ ret.rlType = lea.readByte();
                            lea.skip(4); // 355 new
                        }
                        break;

                    case 8:
                        /* 13279 */ ret.across = (lea.readByte() != 0);
                        /* 13280 */ if (ret.across) {
                            /* 13281 */ ret.acrossPosition = new Rectangle(lea.readInt(), lea.readInt(), lea.readInt(), lea.readInt());
                        }
                        break;

                    case 9:
                        /* 13286 */ ret.across = (lea.readByte() != 0);
                        /* 13287 */ if (ret.across) {
                            /* 13288 */ lea.skip(4);
                            /* 13289 */ ret.acrossPosition = new Rectangle(lea.readInt(), lea.readInt(), lea.readInt(), lea.readInt());
                        }
                        break;

                    case 10:
                        /* 13299 */ lea.readByte();
                        break;

                    case 11:
                        /* 13303 */ lea.readInt();
                        break;

                    case 12:
                        /* 13307 */ lea.readByte();
                        break;

                    case 13:
                        /* 13311 */ lea.readByte();
                        break;

                    case 14:
                        /* 13315 */ lea.readByte();
                        break;

                    case 15:
                        /* 13319 */ x = lea.readByte();
                        /* 13320 */ if (x > 0) {
                            /* 13321 */ int size = lea.readInt();

                            /* 13323 */ for (int i = 0; i < size; i++) {
                                /* 13324 */ lea.skip(16);
                            }
                            /* 13326 */ lea.skip(16);
                        }
                        break;

                    case 19:
                        /* 13331 */ x = lea.readByte();
                        /* 13332 */ if (x > 0) {
                            /* 13333 */ lea.skip(8);
                            lea.skip(4);
                        }
                        break;

                    case 20:
                        /* 13338 */ lea.readByte();
                        break;

                    case 21:
                        /* 13342 */ x = lea.readByte();
                        /* 13343 */ if (x > 0) {
                            /* 13344 */ lea.skip(32);
                        }
                        break;

                    case 24: //361 1++
                        /* 13349 */ x = lea.readByte();
                        /* 13350 */ if (x > 0) {
                            /* 13351 */ int size = lea.readInt();
                            /* 13352 */ for (int i = 0; i < size; i++) {
                                /* 13353 */ lea.readInt();
                                /* 13354 */ ret.mistPoints.add(new Point(lea.readInt(), lea.readInt()));
                            }
                        }
                        break;

                    case 28: //361 1++
                        /* 13360 */ x = lea.readByte();
                        /* 13361 */ if (x > 0) {
                            /* 13362 */ lea.readInt();
                        }
                        break;

                    case 33: //361 1++
                        /* 13367 */ x = lea.readByte();
                        /* 13368 */ if (x > 0) {
                            /* 13369 */ int size = lea.readInt();

                            /* 13371 */ for (int i = 0; i < size; i++) {
                                /* 13372 */ ret.attackObjects.add(new Pair(Integer.valueOf(lea.readInt()), Integer.valueOf(lea.readInt())));
                            }
                        }
                        break;

                    case 36: //361 1++
                        /* 13378 */ x = lea.readByte();
                        /* 13379 */ if (x > 0) {
                            /* 13380 */ lea.readInt();
                            /* 13381 */ lea.skip(39);
                        }
                        break;

                    case 38: //361  +1
                        /* 13386 */ x = lea.readByte();
                        /* 13387 */ if (x > 0) {
                            /* 13388 */ ret.count = lea.readInt();
                        }
                        break;
                }

                /* 13393 */            } while (k != -1);
        }
    }

    public static void attackSkeletonImage(LittleEndianAccessor lea, AttackInfo ret) {
        byte unk = lea.readByte();
        if (unk == 1) {
            lea.readMapleAsciiString();
            lea.readInt();
            int a = lea.readInt();
            if (a > 0) {
                for (int k = 0; k < a; k++) {
                    lea.readMapleAsciiString();
                }
            }
        } else if (unk == 2) {
            lea.readMapleAsciiString();
            lea.readInt();
        }
        lea.skip(1);
        ret.attackPosition = lea.readPos();
        ret.attackPosition2 = lea.readPos();
        ret.attackPosition3 = lea.readPos();
        lea.skip(1);
        lea.skip(4);
        lea.skip(1); // new 361
    }

    public static void sendFireOption(MaplePacketLittleEndianWriter mplew, long newRebirth, Equip equip) {
        int[] rebirth = new int[4];
        String fire = String.valueOf(newRebirth);
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip ordinary = (Equip) MapleItemInformationProvider.getInstance().getEquipById(equip.getItemId(), false);
        int ordinaryPad = (ordinary.getWatk() > 0) ? ordinary.getWatk() : ordinary.getMatk();
        int ordinaryMad = (ordinary.getMatk() > 0) ? ordinary.getMatk() : ordinary.getWatk();
        if (fire.length() == 12) {
            rebirth[0] = Integer.parseInt(fire.substring(0, 3));
            rebirth[1] = Integer.parseInt(fire.substring(3, 6));
            rebirth[2] = Integer.parseInt(fire.substring(6, 9));
            rebirth[3] = Integer.parseInt(fire.substring(9));
        } else if (fire.length() == 11) {
            rebirth[0] = Integer.parseInt(fire.substring(0, 2));
            rebirth[1] = Integer.parseInt(fire.substring(2, 5));
            rebirth[2] = Integer.parseInt(fire.substring(5, 8));
            rebirth[3] = Integer.parseInt(fire.substring(8));
        } else if (fire.length() == 10) {
            rebirth[0] = Integer.parseInt(fire.substring(0, 1));
            rebirth[1] = Integer.parseInt(fire.substring(1, 4));
            rebirth[2] = Integer.parseInt(fire.substring(4, 7));
            rebirth[3] = Integer.parseInt(fire.substring(7));
        }
        Map<Integer, Integer> zz = new HashMap<>();
        for (int i = 0; i < rebirth.length; i++) {
            int value = rebirth[i] - rebirth[i] / 10 * 10;
            fireValue(rebirth[i] / 10, ii.getReqLevel(equip.getItemId()), value, ordinaryPad, ordinaryMad, equip.getItemId(), zz);
        }
        mplew.writeInt(zz.size());
        for (Map.Entry<Integer, Integer> z : zz.entrySet()) {
            mplew.writeInt((((Integer) z.getKey()).intValue() == 22) ? 12 : ((Integer) z.getKey()).intValue());
            mplew.writeInt((((Integer) z.getKey()).intValue() == 22) ? -((Integer) z.getValue()).intValue() : ((Integer) z.getValue()).intValue());
        }
    }

    public static void fireValue(int randomOption, int reqLevel, int randomValue, int ordinaryPad, int ordinaryMad, int itemId, Map<Integer, Integer> zz) {
        switch (randomOption) {
            case 0:
            case 1:
            case 2:
            case 3:
                if (zz.containsKey(Integer.valueOf(randomOption))) {
                    zz.put(Integer.valueOf(randomOption), Integer.valueOf(((Integer) zz.get(Integer.valueOf(randomOption))).intValue() + (reqLevel / 20 + 1) * randomValue));
                    break;
                }
                zz.put(Integer.valueOf(randomOption), Integer.valueOf((reqLevel / 20 + 1) * randomValue));
                break;
            case 4:
                if (zz.containsKey(Integer.valueOf(0))) {
                    zz.put(Integer.valueOf(0), Integer.valueOf(((Integer) zz.get(Integer.valueOf(0))).intValue() + (reqLevel / 40 + 1) * randomValue));
                } else {
                    zz.put(Integer.valueOf(0), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                }
                if (zz.containsKey(Integer.valueOf(1))) {
                    zz.put(Integer.valueOf(1), Integer.valueOf(((Integer) zz.get(Integer.valueOf(1))).intValue() + (reqLevel / 40 + 1) * randomValue));
                    break;
                }
                zz.put(Integer.valueOf(1), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                break;
            case 5:
                if (zz.containsKey(Integer.valueOf(0))) {
                    zz.put(Integer.valueOf(0), Integer.valueOf(((Integer) zz.get(Integer.valueOf(0))).intValue() + (reqLevel / 40 + 1) * randomValue));
                } else {
                    zz.put(Integer.valueOf(0), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                }
                if (zz.containsKey(Integer.valueOf(2))) {
                    zz.put(Integer.valueOf(2), Integer.valueOf(((Integer) zz.get(Integer.valueOf(2))).intValue() + (reqLevel / 40 + 1) * randomValue));
                    break;
                }
                zz.put(Integer.valueOf(2), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                break;
            case 6:
                if (zz.containsKey(Integer.valueOf(0))) {
                    zz.put(Integer.valueOf(0), Integer.valueOf(((Integer) zz.get(Integer.valueOf(0))).intValue() + (reqLevel / 40 + 1) * randomValue));
                } else {
                    zz.put(Integer.valueOf(0), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                }
                if (zz.containsKey(Integer.valueOf(3))) {
                    zz.put(Integer.valueOf(3), Integer.valueOf(((Integer) zz.get(Integer.valueOf(3))).intValue() + (reqLevel / 40 + 1) * randomValue));
                    break;
                }
                zz.put(Integer.valueOf(3), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                break;
            case 7:
                if (zz.containsKey(Integer.valueOf(1))) {
                    zz.put(Integer.valueOf(1), Integer.valueOf(((Integer) zz.get(Integer.valueOf(1))).intValue() + (reqLevel / 40 + 1) * randomValue));
                } else {
                    zz.put(Integer.valueOf(1), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                }
                if (zz.containsKey(Integer.valueOf(2))) {
                    zz.put(Integer.valueOf(2), Integer.valueOf(((Integer) zz.get(Integer.valueOf(2))).intValue() + (reqLevel / 40 + 1) * randomValue));
                    break;
                }
                zz.put(Integer.valueOf(2), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                break;
            case 8:
                if (zz.containsKey(Integer.valueOf(1))) {
                    zz.put(Integer.valueOf(1), Integer.valueOf(((Integer) zz.get(Integer.valueOf(1))).intValue() + (reqLevel / 40 + 1) * randomValue));
                } else {
                    zz.put(Integer.valueOf(1), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                }
                if (zz.containsKey(Integer.valueOf(3))) {
                    zz.put(Integer.valueOf(3), Integer.valueOf(((Integer) zz.get(Integer.valueOf(3))).intValue() + (reqLevel / 40 + 1) * randomValue));
                    break;
                }
                zz.put(Integer.valueOf(3), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                break;
            case 9:
                if (zz.containsKey(Integer.valueOf(2))) {
                    zz.put(Integer.valueOf(2), Integer.valueOf(((Integer) zz.get(Integer.valueOf(2))).intValue() + (reqLevel / 40 + 1) * randomValue));
                } else {
                    zz.put(Integer.valueOf(2), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                }
                if (zz.containsKey(Integer.valueOf(3))) {
                    zz.put(Integer.valueOf(3), Integer.valueOf(((Integer) zz.get(Integer.valueOf(3))).intValue() + (reqLevel / 40 + 1) * randomValue));
                    break;
                }
                zz.put(Integer.valueOf(3), Integer.valueOf((reqLevel / 40 + 1) * randomValue));
                break;
            case 10:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(reqLevel * 3 * randomValue));
                break;
            case 11:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(reqLevel * 3 * randomValue));
                break;
            case 13:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf((reqLevel / 20 + 1) * randomValue));
                break;
            case 17:
                if (isWeapon(itemId)) {
                    switch (randomValue) {
                        case 3:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 1200 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 1500 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 1800 / 10000 + 1));
                            break;
                        case 4:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 1760 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 2200 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 2640 / 10000 + 1));
                            break;
                        case 5:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 2420 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 3025 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 3630 / 10000 + 1));
                            break;
                        case 6:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 3200 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 4000 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 4800 / 10000 + 1));
                            break;
                        case 7:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 4100 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 5125 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryPad * 6150 / 10000 + 1));
                            break;
                    }
                    break;
                }
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(randomValue));
                break;
            case 18:
                if (isWeapon(itemId)) {
                    switch (randomValue) {
                        case 3:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 1200 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 1500 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 1800 / 10000 + 1));
                            break;
                        case 4:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 1760 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 2200 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 2640 / 10000 + 1));
                            break;
                        case 5:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 2420 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 3025 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 3630 / 10000 + 1));
                            break;
                        case 6:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 3200 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 4000 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 4800 / 10000 + 1));
                            break;
                        case 7:
                            if (reqLevel <= 150) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 4100 / 10000 + 1));
                                break;
                            }
                            if (reqLevel <= 160) {
                                zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 5125 / 10000 + 1));
                                break;
                            }
                            zz.put(Integer.valueOf(randomOption), Integer.valueOf(ordinaryMad * 6150 / 10000 + 1));
                            break;
                    }
                    break;
                }
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(randomValue));
                break;
            case 19:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(randomValue));
                break;
            case 20:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(randomValue));
                break;
            case 21:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(randomValue * 2));
                break;
            case 22:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(-5 * randomValue));
                break;
            case 23:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(randomValue));
                break;
            case 24:
                zz.put(Integer.valueOf(randomOption), Integer.valueOf(randomValue));
                break;
        }
    }

    public static List<MatrixSkill> matrixSkills(LittleEndianAccessor slea) {
        int count = slea.readInt();
        ArrayList<MatrixSkill> skills = new ArrayList<MatrixSkill>();
        for (int i = 0; i < count; ++i) {
            MatrixSkill skill = new MatrixSkill(slea.readInt(), slea.readInt(), slea.readInt(), slea.readShort(), slea.readPos(), slea.readInt(), slea.readByte());
            byte unk5 = slea.readByte();
            int x = 0;
            int y = 0;
            if (unk5 > 0) {
                x = slea.readInt();
                y = slea.readInt();
            }
            skill.setUnk5(unk5, x, y);
            byte unk6 = slea.readByte();
            int x2 = 0;
            int y2 = 0;
            if (unk6 > 0) {
                x2 = slea.readInt();
                y2 = slea.readInt();
            }
            skill.setUnk6(unk6, x2, y2);
            skills.add(skill);
        }
        return skills;
    }

    public static boolean sub_884580(int a1) {
        boolean v1;
        if (a1 > 155111003) {
            v1 = (a1 == 155121003);
        } else {
            if (a1 == 155111003 || a1 == 155001000) {
                return true;
            }
            v1 = (a1 == 155101002);
        }
        return v1;
    }

    public static boolean sub_5B9DA0(int a1) {
        return (a1 == 22141017 || a1 == 22170070 || a1 == 155111207);
    }

    public static boolean sub_86B470(int a1) {
        return (a1 == 3011004 || a1 == 3300002 || a1 == 3321003);
    }

    public static boolean is_forceAtom_attack_skill(int a1) {
        boolean v2;
        if (a1 > 61120007) {
            if (a1 == 61121217 || (a1 > 400011057 && a1 <= 400011059)) {
                return true;
            }
        } else if (a1 == 61120007 || a1 == 61101002 || a1 == 61110211) {
            return true;
        }
        if (a1 > 36110012) {
            v2 = (a1 == 36120015);
        } else {
            if (a1 == 36110012 || a1 == 36001005) {
                return true;
            }
            v2 = (a1 == 36100010);
        }
        if (v2 || a1 == 4100012 || a1 == 4120019 || a1 == 35101002 || a1 == 35110017
                || sub_84ABA0(a1)
                || sub_884580(a1)
                || sub_5B9DA0(a1)
                || Integer.toUnsignedLong(a1 - 80002602) <= 19L
                || sub_86B470(a1)) {
            return true;
        }
        if (a1 <= 36110004) {
            boolean v3;
            if (a1 == 36110004) {
                return true;
            }
            if (a1 > 13101022) {
                if (a1 > 14000029) {
                    if (a1 > 25100010) {
                        if (a1 == 25120115) {
                            return true;
                        }
                        v3 = (a1 == 31221014);
                    } else {
                        if (a1 == 25100010 || a1 == 24100003) {
                            return true;
                        }
                        v3 = (a1 == 24120002);
                    }
                } else {
                    if (a1 >= 14000028) {
                        return true;
                    }
                    if (a1 > 13120003) {
                        if (a1 == 13120010) {
                            return true;
                        }
                        v3 = (a1 == 13121054);
                    } else {
                        if (a1 == 13120003 || a1 == 13110022) {
                            return true;
                        }
                        v3 = (a1 - 13110022 == 5);
                    }
                }
            } else {
                if (a1 == 13101022) {
                    return true;
                }
                if (a1 > 4210014) {
                    if (a1 > 12110028) {
                        if (a1 == 12120010) {
                            return true;
                        }
                        v3 = (a1 == 13100027);
                    } else {
                        if (a1 == 12110028 || a1 == 12000026) {
                            return true;
                        }
                        v3 = (a1 == 12100028);
                    }
                } else {
                    if (a1 == 4210014) {
                        return true;
                    }
                    if (a1 > 3300005) {
                        if (a1 == 3301009) {
                            return true;
                        }
                        v3 = (a1 == 3321037);
                    } else {
                        if (a1 == 3300005 || a1 == 2121055 || a1 == 3100010) {
                            return true;
                        }
                        v3 = (a1 == 3120017);
                    }
                }
            }
            return v3;
        }
        if (a1 <= 164120007) {
            boolean v3;
            if (a1 == 164120007) {
                return true;
            }
            if (a1 > 131003016) {
                if (a1 > 152120002) {
                    if (a1 == 155100009) {
                        return true;
                    }
                    v3 = (a1 == 164101004);
                } else {
                    if (a1 >= 152120001 || a1 == 142110011) {
                        return true;
                    }
                    v3 = (a1 == 152001001);
                }
            } else {
                if (a1 == 131003016) {
                    return true;
                }
                if (a1 > 80001588) {
                    if (a1 == 80001890) {
                        return true;
                    }
                    v3 = (a1 == 80002811);
                } else {
                    if (a1 == 80001588) {
                        return true;
                    }
                    v3 = (a1 == 65120011);
                }
            }
            return v3;
        }
        if (a1 > 400031031) {
            boolean v3;
            if (a1 > 400041038) {
                if (a1 == 400041049) {
                    return true;
                }
                v3 = (a1 == 400051017);
            } else {
                if (a1 == 400041038 || a1 == 400041010) {
                    return true;
                }
                v3 = (a1 - 400041010 == 13);
            }
            return v3;
        }
        if (a1 == 400031031) {
            return true;
        }
        if (a1 <= 400031000) {
            if (a1 == 400031000 || a1 == 400021001) {
                return true;
            }
            boolean v3 = (a1 - 400021001 == 44);
            return v3;
        }
        if (a1 >= 400031020) {
            if (a1 <= 400031022) {
                return true;
            }
            boolean v3 = (a1 == 400031029);
            return v3;
        }
        return false;
    }

    public static boolean isHolyAttack(int skillid) {
        boolean a = false;
        switch (skillid) {
            case 1221004:
            case 1221011:
            case 2301005:
            case 2311004:
            case 2320011:
            case 2321007:
            case 2321008:
            case 27111101:
                a = true;
                break;
        }
        return a;
    }

    public static boolean isDarkSightDispelSkill(int skill) {
        switch (skill) {
            case 164101002:
            case 164111006:
            case 164111010:
            case 164121000:
                return true;
        }
        return false;
    }

    public static boolean isExecutionSkill(int skill) {
        switch (skill) {
            case 63111007:
            case 63120140:
            case 63121004:
            case 63121005:
            case 63121006:
            case 63121007:
            case 63121140:
            case 63121141:
            case 400031065:
                return true;
        }
        return false;
    }

    public static boolean 로미오줄리엣(int mapid) {
        return ((mapid >= 926110000 && mapid <= 926110600) || (mapid >= 926100000 && mapid <= 926100600));
    }

    public static boolean 피라미드(int mapid) {
        return ((mapid >= 926010300 && mapid < 926010320) || mapid == 926010001 || mapid == 926010002 || mapid == 926010003);
    }

    public static boolean 튜토리얼(int mapid) {
        switch (mapid) {
            case 1000003:
            case 4000000:
            case 4000001:
            case 100000049:
            case 100000050:
            case 100000051:
            case 100000052:
            case 105010200:
            case 324040000:
            case 327090040:
            case 913070001:
            case 913070010:
            case 927020090:
            case 940205800:
            case 970060500:
            case 980031300:
                return true;
        }
        return false;
    }

    public static boolean 보스맵(int mapid) {
        switch (mapid) {
            case 105200100:
            case 105200110:
            case 105200200:
            case 105200210:
            case 105200300:
            case 105200310:
            case 105200400:
            case 105200410:
            case 105200500:
            case 105200510:
            case 105200600:
            case 105200610:
            case 105200700:
            case 105200710:
            case 105200800:
            case 105200810:
            case 211070100:
            case 211070102:
            case 211070104:
            case 220080100:
            case 220080200:
            case 220080300:
            case 221030910:
            case 240060000:
            case 240060001:
            case 240060100:
            case 240060101:
            case 240060200:
            case 240060201:
            case 262030100:
            case 262030200:
            case 262030300:
            case 262031100:
            case 262031200:
            case 262031300:
            case 270050100:
            case 270051100:
            case 271040100:
            case 271041100:
            case 272020200:
            case 272020210:
            case 280030000:
            case 280030100:
            case 350060400:
            case 350060500:
            case 350060600:
            case 350060700:
            case 350060800:
            case 350060900:
            case 350160100:
            case 350160140:
            case 350160200:
            case 350160220:
            case 350160240:
            case 350160260:
            case 401060100:
            case 401060200:
            case 401060300:
            case 410002000:
            case 410002020:
            case 410002040:
            case 410002060:
            case 410002080:
            case 450003800:
            case 450003840:
            case 450003880:
            case 450003920:
            case 450003960:
            case 450004100:
            case 450004150:
            case 450004200:
            case 450004250:
            case 450004300:
            case 450004400:
            case 450004450:
            case 450004500:
            case 450004550:
            case 450004600:
            case 450008100:
            case 450008150:
            case 450008200:
            case 450008250:
            case 450008300:
            case 450008350:
            case 450008700:
            case 450008750:
            case 450008800:
            case 450008850:
            case 450008900:
            case 450008950:
            case 450009400:
            case 450009450:
            case 450010400:
            case 450010500:
            case 450012210:
            case 450012600:
            case 450013100:
            case 450013200:
            case 450013300:
            case 450013400:
            case 450013500:
            case 450013600:
            case 450013700:
            case 450013750:
                return true;
        }
        return false;
    }

    public static boolean 사냥컨텐츠맵(int mapid) {
        switch (mapid) {
            case 100000030:
            case 109040001:
            case 109040002:
            case 109040003:
            case 109040004:
            case 280020000:
            case 280020001:
            case 910130001:
            case 910360000:
            case 910360001:
            case 910360002:
            case 910530202:
            case 912080100:
            case 921170050:
            case 921170100:
            case 921171000:
            case 921171100:
            case 921171200:
            case 921172300:
            case 921172400:
            case 921174100:
            case 993000000:
            case 993000100:
            case 993000200:
            case 993000300:
            case 993000400:
            case 993000500:
            case 993000600:
            case 993000601:
            case 993074000:
            case 993074001:
            case 993074800:
            case 993186001:
            case 993186100:
            case 993186101:
            case 993186200:
            case 993186300:
            case 993186400:
            case 993186500:
            case 993186600:
            case 993186700:
            case 993186800:
            case 993186900:
            case 993187300:
            case 993187400:
            case 993187500:
                return true;
        }
        return false;
    }

    public static boolean MovedSkill(int skillid) {
        switch (skillid) {
            case 1001008:
            case 3000008:
            case 3000009:
            case 3000010:
            case 3001007:
            case 3011005:
            case 3011006:
            case 3011007:
            case 3011008:
            case 4001011:
            case 4001014:
            case 5001010:
            case 5011006:
            case 10000252:
            case 10001253:
            case 10001254:
            case 12000023:
            case 12001027:
            case 12001028:
            case 13001021:
            case 14001024:
            case 14001026:
            case 20001295:
            case 20020111:
            case 22110019:
            case 23001002:
            case 24001002:
            case 25001204:
            case 30001079:
            case 30010110:
            case 30010184:
            case 33001102:
            case 36001001:
            case 37001003:
            case 51001003:
            case 61001002:
            case 61111221:
            case 63001002:
            case 64001003:
            case 65001001:
            case 100001265:
            case 100001266:
            case 142000006:
            case 142001004:
            case 151001002:
            case 151001003:
            case 155001009:
            case 155001104:
            case 164001003:
            case 400031060:
                return true;
        }
        return false;
    }

    public static boolean isPollingmoonAttackskill(int skillId) {
        return (skillId == 11101120 || skillId == 11101121 || skillId == 11111120 || skillId == 11111121 || skillId == 11121101 || skillId == 11121102 || skillId == 11121103);
    }

    public static boolean isRisingsunAttackskill(int skillId) {
        return (skillId == 11101220 || skillId == 11101221 || skillId == 11111220 || skillId == 11111221 || skillId == 11121201 || skillId == 11121202 || skillId == 11121203);
    }

    public static boolean isDarkAtackSkill(int skillId) {
        return (skillId == 400041059 || skillId == 14101021 || skillId == 14111021 || skillId == 14111022 || skillId == 14111023 || skillId == 14121002 || skillId == 14000027 || skillId == 14000028 || skillId == 14000029 || skillId == 14001027);
    }

    public static boolean isBMDarkAtackSkill(int skillid) {
        return (skillid == 32121011 || skillid == 32110020 || skillid == 32111016 || skillid == 400021007 || skillid == 400021069 || skillid == 400021047 || skillid == 400021088 || skillid == 400021113);
    }

    public static boolean isAfterRemoveSummonSkill(int skillid) {
        switch (skillid) {
            case 12000022:
            case 12100026:
            case 12110024:
            case 12120007:
            case 25121133:
            case 35111002:
            case 400021068:
            case 400021069:
                return true;
        }
        return false;
    }

    public static int getFallingTime(int skillid) {
        switch (skillid) {
            case 21120025:
                return 300;
            case 21110026:
                return 360;
            case 21110028:
                return 648;
        }
        return -1;
    }

    public static double getJobInt(int jobid) {
        if (GameConstants.isFPMage(jobid) || GameConstants.isBishop(jobid) || GameConstants.isILMage(jobid) || GameConstants.isFlameWizard(jobid)) {
            return 1.2;
        }
        if (GameConstants.isXenon(jobid)) {
            return 0.875;
        }
        if (GameConstants.isHero(jobid)) {
            return 1.08;
        }
        return 1.0;
    }

    public static double getWeponInt(int itemid) {
        int id = itemid / 1000;
        switch (id) {
            case 1372:
            case 1382: {
                return 1.0;
            }
            case 1212:
            case 1262:
            case 1282:
            case 1302:
            case 1312:
            case 1322: {
                return 1.2;
            }
            case 1214:
            case 1232:
            case 1272:
            case 1292:
            case 1332:
            case 1342:
            case 1362:
            case 1452:
            case 1522:
            case 1592: {
                return 1.3;
            }
            case 1213:
            case 1402:
            case 1412:
            case 1422:
            case 1562:
            case 1572: {
                return 1.34;
            }
            case 1462: {
                return 1.35;
            }
            case 1432:
            case 1442: {
                return 1.49;
            }
            case 1242:
            case 1492:
            case 1532: {
                return 1.5;
            }
            case 1222:
            case 1482:
            case 1582: {
                return 1.7;
            }
            case 1472: {
                return 1.75;
            }
        }
        return 0.0;
    }


    public static int getJobAttackSpeed(MapleCharacter chr) {
        int speed = 0;
        Equip weapon = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
        if (weapon != null) {
            speed = weapon.getAttackSpeed();
        }
        speed += chr.getStat().getAttackSpeed();
        if (speed < 2) {
            speed = 2;
        }
        return speed;
    }

    public static boolean getPotentialCheck(int potentialid1, int potentialid2, int potentialid3) {
        if ((potentialid1 == 30601 || potentialid1 == 30055 || potentialid1 == 40043 || potentialid1 == 40601 || potentialid1 == 40602 || potentialid1 == 40603) && (potentialid2 == 30601 || potentialid2 == 30055 || potentialid2 == 40043 || potentialid2 == 40601 || potentialid2 == 40602 || potentialid2 == 40603) && (potentialid3 == 30601 || potentialid3 == 30055 || potentialid3 == 40043 || potentialid3 == 40601 || potentialid3 == 40602 || potentialid3 == 40603)) {
            return false;
        }
        if ((potentialid1 == 10291 || potentialid1 == 30291 || potentialid1 == 40291 || potentialid1 == 40292) && (potentialid2 == 10291 || potentialid2 == 30291 || potentialid2 == 40291 || potentialid2 == 40292) && (potentialid3 == 10291 || potentialid3 == 30291 || potentialid3 == 40291 || potentialid3 == 40292)) {
            return false;
        }
        if ((potentialid1 == 30356 || potentialid1 == 30357 || potentialid1 == 40356 || potentialid1 == 40357) && (potentialid2 == 30356 || potentialid2 == 30357 || potentialid2 == 40356 || potentialid2 == 40357) && (potentialid3 == 30356 || potentialid3 == 30357 || potentialid3 == 40356 || potentialid3 == 40357)) {
            return false;
        }
        if (potentialid1 == 40656
                && potentialid2 == 40656 && potentialid3 == 40656) {
            return false;
        }
        return true;
    }

    public static int BattleGroundJobType(MapleBattleGroundCharacter chr) {
        int t = chr.getJobType();
        switch (chr.getJobType()) {
            case 6:
            case 7:
                t++;
                break;
            case 9:
                t = 14;
                break;
            case 10:
                t = 16;
                break;
            case 11:
                t = 23;
                break;
            case 12:
                t = 24;
                break;
        }
        return t;
    }

    public static boolean 남자한벌(int itemId) {
        return (itemId / 1000 == 1050);
    }

    public static boolean 여자한벌(int itemId) {
        return (itemId / 1000 == 1051);
    }

    public static boolean 남자신발(int itemId) {
        return (itemId / 1000 == 1070);
    }

    public static boolean 여자신발(int itemId) {
        return (itemId / 1000 == 1071);
    }

    public static boolean 남자망토(int itemId) {
        return (itemId / 1000 == 1100);
    }

    public static boolean 여자망토(int itemId) {
        return (itemId / 1000 == 1101);
    }

    public static boolean 남자모자(int itemId) {
        return (itemId / 1000 == 1000);
    }

    public static boolean 여자모자(int itemId) {
        return (itemId / 1000 == 1001);
    }

    public static boolean isGlove(int itemId) {
        return (itemId / 10000 == 108);
    }

    public static boolean isShoes(int itemId) {
        return (itemId / 10000 == 107);
    }

    public static boolean isAggressIveMonster(int mobid) {
        switch (mobid) {
            case 8840000:
            case 8840007:
            case 8840014:
            case 8850011:
            case 8880100:
            case 8880101:
            case 8880110:
            case 8880111:
            case 8880140:
            case 8880141:
            case 8880142:
            case 8880150:
            case 8880151:
            case 8880155:
            case 8880200:
                return true;
        }
        return false;
    }

    public static int ExpPocket(int level) {
        int exp = 0;
        switch (level) {
            case 1:
            case 2:
                exp = 5;
                break;
            case 3:
                exp = 7;
                break;
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
                exp = 47312;
                break;
            case 105:
                exp = 48449;
                break;
            case 106:
                exp = 51598;
                break;
            case 107:
                exp = 54952;
                break;
            case 108:
                exp = 60865;
                break;
            case 109:
                exp = 64821;
                break;
            case 110:
                exp = 69034;
                break;
            case 111:
                exp = 60865;
                break;
            case 112:
                exp = 78301;
                break;
            case 113:
                exp = 83390;
                break;
            case 114:
                exp = 88811;
                break;
            case 115:
                exp = 94583;
                break;
            case 116:
                exp = 100731;
                break;
            case 117:
                exp = 107279;
                break;
            case 118:
                exp = 114252;
                break;
            case 119:
                exp = 121678;
                break;
            case 120:
                exp = 122252;
                break;
            case 121:
                exp = 130199;
                break;
            case 122:
                exp = 138662;
                break;
            case 123:
                exp = 147675;
                break;
            case 124:
                exp = 157274;
                break;
            case 125:
                exp = 167496;
                break;
            case 126:
                exp = 178384;
                break;
            case 127:
                exp = 189979;
                break;
            case 128:
                exp = 202327;
                break;
            case 129:
                exp = 215479;
                break;
            case 130:
                exp = 221140;
                break;
            case 131:
                exp = 235514;
                break;
            case 132:
                exp = 250822;
                break;
            case 133:
                exp = 267126;
                break;
            case 134:
                exp = 284489;
                break;
            case 135:
                exp = 302981;
                break;
            case 136:
                exp = 305985;
                break;
            case 137:
                exp = 325874;
                break;
            case 138:
                exp = 347055;
                break;
            case 139:
                exp = 369614;
                break;
            case 140:
                exp = 379624;
                break;
            case 141:
                exp = 396739;
                break;
            case 142:
                exp = 408153;
                break;
            case 143:
                exp = 420319;
                break;
            case 144:
                exp = 426886;
                break;
            case 145:
                exp = 440608;
                break;
            case 146:
                exp = 448907;
                break;
            case 147:
                exp = 464244;
                break;
            case 148:
                exp = 474288;
                break;
            case 149:
                exp = 491333;
                break;
            case 150:
                exp = 522041;
                break;
            case 151:
                exp = 541140;
                break;
            case 152:
                exp = 561272;
                break;
            case 153:
                exp = 589336;
                break;
            case 154:
                exp = 591382;
                break;
            case 155:
                exp = 621439;
                break;
            case 156:
                exp = 646079;
                break;
            case 157:
                exp = 679156;
                break;
            case 158:
                exp = 699286;
                break;
            case 159:
                exp = 727981;
                break;
            case 160:
                exp = 765745;
                break;
            case 161:
                exp = 774861;
                break;
            case 162:
                exp = 800421;
                break;
            case 163:
                exp = 834985;
                break;
            case 164:
                exp = 848599;
                break;
            case 165:
                exp = 864068;
                break;
            case 166:
                exp = 881349;
                break;
            case 167:
                exp = 900417;
                break;
            case 168:
                exp = 956693;
                break;
            case 169:
                exp = 978839;
                break;
            case 170:
                exp = 991074;
                break;
            case 171:
                exp = 1004744;
                break;
            case 172:
                exp = 1019815;
                break;
            case 173:
                exp = 1036264;
                break;
            case 174:
                exp = 1054075;
                break;
            case 175:
                exp = 1073240;
                break;
            case 176:
                exp = 1093757;
                break;
            case 177:
                exp = 1115633;
                break;
            case 178:
                exp = 1138875;
                break;
            case 179:
                exp = 1163499;
                break;
            case 180:
                exp = 1189525;
                break;
            case 181:
                exp = 1216976;
                break;
            case 182:
                exp = 1245879;
                break;
            case 183:
                exp = 1276266;
                break;
            case 184:
                exp = 1308173;
                break;
            case 185:
                exp = 1373581;
                break;
            case 186:
                exp = 1408719;
                break;
            case 187:
                exp = 1514373;
                break;
            case 188:
                exp = 1553113;
                break;
            case 189:
                exp = 1593706;
                break;
            case 190:
                exp = 1636205;
                break;
            case 191:
                exp = 1644908;
                break;
            case 192:
                exp = 1691171;
                break;
            case 193:
                exp = 1739490;
                break;
            case 194:
                exp = 1789935;
                break;
            case 195:
                exp = 1879432;
                break;
            case 196:
                exp = 1897504;
                break;
            case 197:
                exp = 1918587;
                break;
            case 198:
                exp = 1942569;
                break;
            case 199:
                exp = 1969364;
                break;
            case 200:
                exp = 2207026;
                break;
            case 201:
                exp = 2354161;
                break;
            case 202:
                exp = 2516812;
                break;
            case 203:
                exp = 2696272;
                break;
            case 204:
                exp = 2893999;
                break;
            case 205:
                exp = 2991949;
                break;
            case 206:
                exp = 3004330;
                break;
            case 207:
                exp = 3052688;
                break;
            case 208:
                exp = 3075322;
                break;
            case 209:
                exp = 3067290;
                break;
            case 210:
                exp = 3070157;
                break;
            case 211:
                exp = 3083641;
                break;
            case 212:
                exp = 3089513;
                break;
            case 213:
                exp = 3094100;
                break;
            case 214:
                exp = 3102108;
                break;
            case 215:
                exp = 3167815;
                break;
            case 216:
                exp = 3215124;
                break;
            case 217:
                exp = 3224981;
                break;
            case 218:
                exp = 3298424;
                break;
            case 219:
                exp = 3321329;
                break;
            case 220:
                exp = 3358456;
                break;
            case 221:
                exp = 3546113;
                break;
            case 222:
                exp = 3565436;
                break;
            case 223:
                exp = 3754722;
                break;
            case 224:
                exp = 3723423;
                break;
            case 225:
                exp = 3823521;
                break;
            case 226:
                exp = 3951212;
                break;
            case 227:
                exp = 4032421;
                break;
            case 228:
                exp = 4324256;
                break;
            case 229:
                exp = 4542342;
                break;
            case 230:
                exp = 4715113;
                break;
            case 231:
                exp = 4815112;
                break;
            case 232:
                exp = 4891251;
                break;
            case 233:
                exp = 4956342;
                break;
            case 234:
                exp = 4991515;
                break;
            case 235:
                exp = 5014425;
                break;
            case 236:
                exp = 5153321;
                break;
            case 237:
                exp = 5353241;
                break;
            case 238:
                exp = 5584565;
                break;
            case 239:
                exp = 5658436;
                break;
            case 240:
                exp = 6013412;
                break;
            case 241:
                exp = 6123412;
                break;
            case 242:
                exp = 6234464;
                break;
            case 243:
                exp = 6365123;
                break;
            case 244:
                exp = 6546477;
                break;
            case 245:
                exp = 6656745;
                break;
            case 246:
                exp = 6764654;
                break;
            case 247:
                exp = 6912233;
                break;
            case 248:
                exp = 7013242;
                break;
            case 249:
                exp = 7123466;
                break;
            case 250:
                exp = 7325315;
                break;
            case 251:
                exp = 7952511;
                break;
            case 252:
                exp = 7991221;
                break;
            case 253:
                exp = 8121212;
                break;
            case 254:
                exp = 8156453;
                break;
            case 255:
                exp = 8192511;
                break;
            case 256:
                exp = 8235003;
                break;
            case 257:
                exp = 8281512;
                break;
            case 258:
                exp = 8300657;
                break;
            case 259:
                exp = 8393163;
                break;
            case 260:
                exp = 8592322;
                break;
            case 261:
                exp = 8812342;
                break;
            case 262:
                exp = 8925111;
                break;
            case 263:
                exp = 8993151;
                break;
            case 264:
                exp = 9105413;
                break;
            case 265:
                exp = 9155322;
                break;
            case 266:
                exp = 9352165;
                break;
            case 267:
                exp = 9435235;
                break;
            case 268:
                exp = 9506346;
                break;
            case 269:
                exp = 9634212;
                break;
            case 270:
                exp = 9764532;
                break;
            case 271:
                exp = 9841511;
                break;
            case 272:
                exp = 10231545;
                break;
            case 273:
                exp = 11144656;
                break;
            case 274:
                exp = 13536563;
                break;
        }
        return exp;
    }

    public static int UnionMaxCoin(MapleCharacter chr) {
        int union = (int) chr.getKeyValue(18771, "rank");
        if (union >= 101 && union <= 105) {
            return 200;
        }
        if (union >= 201 && union <= 205) {
            return 300;
        }
        if (union >= 301 && union <= 305) {
            return 500;
        }
        if (union >= 401 && union <= 405) {
            return 700;
        }
        return 0;
    }

    public static boolean isContentsMap(int mapid) {
        switch (mapid) {
            case 450001400:
            case 921174002:
            case 921174100:
            case 921174101:
            case 921174102:
            case 921174103:
            case 921174104:
            case 921174105:
            case 921174106:
            case 921174107:
            case 921174108:
            case 921174109:
            case 921174110:
            case 993026900:
            case 993192600:
            case 993192601:
            case 993192800:
                return true;
        }
        if (mapid / 10000000 == 95) {
            return true;
        }
        if (mapid / 10000 == 92507) {
            return true;
        }
        return false;
    }

    public static int GPboss(int mobid, boolean party, boolean chaos) {
        int gp = 0;
        switch (mobid) {
            case 8800002:
            case 8850011:
                gp = 50;
                if (party) {
                    gp += 300;
                }
                break;
            case 8800102:
                gp = 500;
                if (party) {
                    gp += 1500;
                }
                break;
            case 8880002:
                gp = 250;
                if (party) {
                    gp += 1000;
                }
                break;
            case 8880000:
                if (chaos) {
                    gp = 700;
                    if (party) {
                        gp += 1700;
                    }
                    break;
                }
                gp = 700;
                if (party) {
                    gp += 1700;
                }
                break;
            case 8870000:
                gp = 150;
                if (party) {
                    gp += 500;
                }
                break;
            case 8870100:
                gp = 400;
                if (party) {
                    gp += 1400;
                }
                break;
            case 8500012:
                gp = 300;
                if (party) {
                    gp += 1000;
                }
                break;
            case 8500022:
                if (chaos) {
                    gp = 1200;
                    if (party) {
                        gp += 1800;
                    }
                    break;
                }
                gp = 1200;
                if (party) {
                    gp += 1800;
                }
                break;
            case 8900100:
            case 8900103:
            case 8900106:
            case 8910100:
                gp = 200;
                if (party) {
                    gp += 1000;
                }
                break;
            case 8900000:
            case 8900003:
            case 8910000:
            case 8920006:
                if (chaos) {
                    gp = 800;
                    if (party) {
                        gp += 1800;
                    }
                    break;
                }
                gp = 800;
                if (party) {
                    gp += 1800;
                }
                break;
            case 8840000:
            case 8880200:
                gp = 200;
                if (party) {
                    gp += 800;
                }
                break;
            case 8840014:
            case 8860000:
                gp = 400;
                if (party) {
                    gp += 1400;
                }
                break;
            case 8810018:
                gp = 150;
                if (party) {
                    gp += 800;
                }
                break;
            case 8810122:
                gp = 500;
                if (party) {
                    gp += 1500;
                }
                break;
            case 8850111:
                gp = 400;
                if (party) {
                    gp += 1400;
                }
                break;
            case 8820001:
                gp = 250;
                if (party) {
                    gp += 1250;
                }
                break;
            case 8880101:
            case 8880111:
            case 8880150:
            case 8880153:
            case 8880302:
            case 8950002:
            case 8950102:
                gp = 1000;
                if (party) {
                    gp += 2000;
                }
                break;
        }
        return gp;
    }

    public static boolean 소멸의여로(int mapid) {
        switch (mapid) {
            case 450001010:
            case 450001011:
            case 450001012:
            case 450001013:
            case 450001014:
            case 450001015:
            case 450001016:
            case 450001110:
            case 450001111:
            case 450001112:
            case 450001113:
            case 450001114:
            case 450001230:
            case 450001260:
            case 450001261:
            case 450001262:
                return true;
        }
        return false;
    }

    public static boolean 츄츄아일랜드(int mapid) {
        switch (mapid) {
            case 450002001:
            case 450002002:
            case 450002003:
            case 450002004:
            case 450002005:
            case 450002006:
            case 450002007:
            case 450002008:
            case 450002009:
            case 450002010:
            case 450002011:
            case 450002012:
            case 450002013:
            case 450002014:
            case 450002015:
            case 450002016:
            case 450002017:
            case 450002018:
            case 450002019:
            case 450002020:
            case 450002300:
            case 450002301:
            case 450002302:
                return true;
        }
        return false;
    }

    public static boolean 레헬른(int mapid) {
        switch (mapid) {
            case 450003200:
            case 450003210:
            case 450003220:
            case 450003300:
            case 450003310:
            case 450003320:
            case 450003340:
            case 450003350:
            case 450003360:
            case 450003400:
            case 450003410:
            case 450003420:
            case 450003440:
            case 450003450:
            case 450003460:
            case 450003500:
            case 450003510:
            case 450003520:
            case 450003530:
            case 450003540:
            case 450003560:
                return true;
        }
        return false;
    }

    public static boolean 아르카나(int mapid) {
        switch (mapid) {
            case 450005110:
            case 450005120:
            case 450005121:
            case 450005130:
            case 450005131:
            case 450005210:
            case 450005220:
            case 450005221:
            case 450005222:
            case 450005230:
            case 450005240:
            case 450005241:
            case 450005242:
            case 450005410:
            case 450005411:
            case 450005412:
            case 450005420:
            case 450005430:
            case 450005431:
            case 450005432:
            case 450005440:
            case 450005500:
            case 450005510:
            case 450005520:
            case 450005530:
            case 450005550:
                return true;
        }
        return false;
    }

    public static boolean 모라스(int mapid) {
        switch (mapid) {
            case 450006010:
            case 450006020:
            case 450006030:
            case 450006110:
            case 450006120:
            case 450006140:
            case 450006150:
            case 450006160:
            case 450006210:
            case 450006220:
            case 450006230:
            case 450006300:
            case 450006310:
            case 450006320:
            case 450006410:
            case 450006420:
            case 450006430:
                return true;
        }
        return false;
    }

    public static boolean 에스페라(int mapid) {
        switch (mapid) {
            case 450007010:
            case 450007020:
            case 450007030:
            case 450007050:
            case 450007060:
            case 450007070:
            case 450007110:
            case 450007120:
            case 450007130:
            case 450007140:
            case 450007150:
            case 450007160:
            case 450007210:
            case 450007220:
            case 450007230:
                return true;
        }
        return false;
    }

    public static boolean 문브릿지(int mapid) {
        switch (mapid) {
            case 450009100:
            case 450009101:
            case 450009110:
            case 450009120:
            case 450009130:
            case 450009140:
            case 450009150:
            case 450009160:
            case 450009200:
            case 450009201:
            case 450009210:
            case 450009220:
            case 450009230:
            case 450009240:
            case 450009250:
            case 450009260:
            case 450009300:
            case 450009301:
            case 450009310:
            case 450009320:
            case 450009330:
            case 450009340:
            case 450009350:
            case 450009360:
            case 450009400:
            case 450009500:
                return true;
        }
        return false;
    }

    public static boolean 고통의미궁(int mapid) {
        switch (mapid) {
            case 450010000:
            case 450010100:
            case 450010200:
            case 450010300:
            case 450010400:
            case 450010500:
            case 450010600:
            case 450010700:
            case 450010800:
            case 450011120:
            case 450011220:
            case 450011320:
            case 450011400:
            case 450011410:
            case 450011420:
            case 450011430:
            case 450011440:
            case 450011450:
            case 450011500:
            case 450011510:
            case 450011520:
            case 450011530:
            case 450011540:
            case 450011550:
            case 450011560:
            case 450011570:
            case 450011600:
            case 450011610:
            case 450011620:
            case 450011630:
            case 450011640:
            case 450011650:
            case 450011990:
                return true;
        }
        return false;
    }

    public static boolean 리멘(int mapid) {
        switch (mapid) {
            case 450012000:
            case 450012010:
            case 450012020:
            case 450012030:
            case 450012040:
            case 450012100:
            case 450012110:
            case 450012120:
            case 450012200:
            case 450012210:
            case 450012300:
            case 450012310:
            case 450012320:
            case 450012330:
            case 450012340:
            case 450012350:
            case 450012400:
            case 450012410:
            case 450012420:
            case 450012430:
            case 450012440:
            case 450012500:
            case 450013000:
            case 450013100:
            case 450013200:
            case 450013300:
            case 450013400:
            case 450013500:
            case 450013600:
            case 450013700:
            case 450013750:
            case 450013780:
            case 450013800:
            case 450013810:
            case 450013820:
            case 450013830:
            case 450013840:
            case 450013850:
            case 450013860:
            case 450013870:
                return true;
        }
        return false;
    }

    public static long pow(int a, int b) {
        long c = 1L;
        for (int i = 0; i < b; i++) {
            c *= a;
        }
        return c;
    }

    public static int getWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, 8 - cal.get(7));
        int week = cal.get(3);
        return week;
    }

    public static int getWeek_WeekendMaple() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, 8 - cal.get(7));
        int week = cal.get(3);
        return week % 2;
    }

    public static int getDate_WeekendMaple() {
        int date = (new Date()).getDay();
        int a = (date == 6) ? 0 : ((date == 0) ? 1 : -1);
        return a;
    }

    public static long MatrixSlotAddMeso(int level, int slotpos) {
        long meso = 0L;
        switch (slotpos) {
            case 5:
                if (level == 200) {
                    meso = 76730000L;
                    break;
                }
                if (level == 201) {
                    meso = 60370000L;
                    break;
                }
                if (level == 202) {
                    meso = 46730000L;
                    break;
                }
                if (level == 203) {
                    meso = 35370000L;
                    break;
                }
                if (level == 204) {
                    meso = 25900000L;
                }
                break;
            case 6:
                if (level == 200) {
                    meso = 278440000L;
                    break;
                }
                if (level == 201) {
                    meso = 222750000L;
                    break;
                }
                if (level == 202) {
                    meso = 178200000L;
                    break;
                }
                if (level == 203) {
                    meso = 142560000L;
                    break;
                }
                if (level == 204) {
                    meso = 114050000L;
                    break;
                }
                if (level == 205) {
                    meso = 91240000L;
                    break;
                }
                if (level == 206) {
                    meso = 71790000L;
                    break;
                }
                if (level == 207) {
                    meso = 55570000L;
                    break;
                }
                if (level == 208) {
                    meso = 42060000L;
                    break;
                }
                if (level == 209) {
                    meso = 30800000L;
                }
                break;
            case 7:
                if (level == 205) {
                    meso = 329070000L;
                    break;
                }
                if (level == 206) {
                    meso = 263250000L;
                    break;
                }
                if (level == 207) {
                    meso = 210600000L;
                    break;
                }
                if (level == 208) {
                    meso = 168480000L;
                    break;
                }
                if (level == 209) {
                    meso = 134790000L;
                    break;
                }
                if (level == 210) {
                    meso = 107830000L;
                    break;
                }
                if (level == 211) {
                    meso = 84840000L;
                    break;
                }
                if (level == 212) {
                    meso = 65680000L;
                    break;
                }
                if (level == 213) {
                    meso = 49710000L;
                    break;
                }
                if (level == 214) {
                    meso = 36400000L;
                }
                break;
            case 8:
                if (level == 210) {
                    meso = 392350000L;
                    break;
                }
                if (level == 211) {
                    meso = 313880000L;
                    break;
                }
                if (level == 212) {
                    meso = 251100000L;
                    break;
                }
                if (level == 213) {
                    meso = 200880000L;
                    break;
                }
                if (level == 214) {
                    meso = 160710000L;
                    break;
                }
                if (level == 215) {
                    meso = 128570000L;
                    break;
                }
                if (level == 216) {
                    meso = 101150000L;
                    break;
                }
                if (level == 217) {
                    meso = 78310000L;
                    break;
                }
                if (level == 218) {
                    meso = 59270000L;
                    break;
                }
                if (level == 219) {
                    meso = 43400000L;
                }
                break;
            case 9:
                if (level == 215) {
                    meso = 468290000L;
                    break;
                }
                if (level == 216) {
                    meso = 374630000L;
                    break;
                }
                if (level == 217) {
                    meso = 299700000L;
                    break;
                }
                if (level == 218) {
                    meso = 239760000L;
                    break;
                }
                if (level == 219) {
                    meso = 191810000L;
                    break;
                }
                if (level == 220) {
                    meso = 153450000L;
                    break;
                }
                if (level == 221) {
                    meso = 120730000L;
                    break;
                }
                if (level == 222) {
                    meso = 934600000L;
                    break;
                }
                if (level == 223) {
                    meso = 70740000L;
                    break;
                }
                if (level == 224) {
                    meso = 51800000L;
                }
                break;
            case 10:
                if (level == 220) {
                    meso = 550550000L;
                    break;
                }
                if (level == 221) {
                    meso = 440440000L;
                    break;
                }
                if (level == 222) {
                    meso = 352350000L;
                    break;
                }
                if (level == 223) {
                    meso = 281880000L;
                    break;
                }
                if (level == 224) {
                    meso = 225510000L;
                    break;
                }
                if (level == 225) {
                    meso = 180410000L;
                    break;
                }
                if (level == 226) {
                    meso = 141940000L;
                    break;
                }
                if (level == 227) {
                    meso = 109880000L;
                    break;
                }
                if (level == 228) {
                    meso = 83170000L;
                    break;
                }
                if (level == 229) {
                    meso = 60900000L;
                }
                break;
            case 11:
                if (level == 225) {
                    meso = 651800000L;
                    break;
                }
                if (level == 226) {
                    meso = 521440000L;
                    break;
                }
                if (level == 227) {
                    meso = 417150000L;
                    break;
                }
                if (level == 228) {
                    meso = 333720000L;
                    break;
                }
                if (level == 229) {
                    meso = 266980000L;
                    break;
                }
                if (level == 230) {
                    meso = 213590000L;
                    break;
                }
                if (level == 231) {
                    meso = 168040000L;
                    break;
                }
                if (level == 232) {
                    meso = 130090000L;
                    break;
                }
                if (level == 233) {
                    meso = 98460000L;
                    break;
                }
                if (level == 234) {
                    meso = 72100000L;
                }
                break;
            case 12:
                if (level == 230) {
                    meso = 772040000L;
                    break;
                }
                if (level == 231) {
                    meso = 617630000L;
                    break;
                }
                if (level == 232) {
                    meso = 494100000L;
                    break;
                }
                if (level == 233) {
                    meso = 395280000L;
                    break;
                }
                if (level == 234) {
                    meso = 316230000L;
                    break;
                }
                if (level == 235) {
                    meso = 252980000L;
                    break;
                }
                if (level == 236) {
                    meso = 199040000L;
                    break;
                }
                if (level == 237) {
                    meso = 154080000L;
                    break;
                }
                if (level == 238) {
                    meso = 116620000L;
                    break;
                }
                if (level == 239) {
                    meso = 85400000L;
                }
                break;
            case 13:
                if (level == 235) {
                    meso = 917580000L;
                    break;
                }
                if (level == 236) {
                    meso = 734070000L;
                    break;
                }
                if (level == 237) {
                    meso = 587250000L;
                    break;
                }
                if (level == 238) {
                    meso = 469800000L;
                    break;
                }
                if (level == 239) {
                    meso = 375840000L;
                    break;
                }
                if (level == 240) {
                    meso = 300680000L;
                    break;
                }
                if (level == 241) {
                    meso = 236560000L;
                    break;
                }
                if (level == 242) {
                    meso = 183130000L;
                    break;
                }
                if (level == 243) {
                    meso = 138610000L;
                    break;
                }
                if (level == 244) {
                    meso = 101500000L;
                }
                break;
            case 14:
                if (level == 240) {
                    meso = 1082110000L;
                    break;
                }
                if (level == 241) {
                    meso = 865690000L;
                    break;
                }
                if (level == 242) {
                    meso = 692550000L;
                    break;
                }
                if (level == 243) {
                    meso = 554040000L;
                    break;
                }
                if (level == 244) {
                    meso = 443240000L;
                    break;
                }
                if (level == 245) {
                    meso = 354590000L;
                    break;
                }
                if (level == 246) {
                    meso = 278980000L;
                    break;
                }
                if (level == 247) {
                    meso = 215970000L;
                    break;
                }
                if (level == 248) {
                    meso = 163460000L;
                    break;
                }
                if (level == 249) {
                    meso = 119700000L;
                }
                break;
            case 15:
                if (level == 245) {
                    meso = 1284610000L;
                    break;
                }
                if (level == 246) {
                    meso = 1027690000L;
                    break;
                }
                if (level == 247) {
                    meso = 822150000L;
                    break;
                }
                if (level == 248) {
                    meso = 657720000L;
                    break;
                }
                if (level == 249) {
                    meso = 526180000L;
                    break;
                }
                if (level == 250) {
                    meso = 420950000L;
                    break;
                }
                if (level == 251) {
                    meso = 331180000L;
                    break;
                }
                if (level == 252) {
                    meso = 256380000L;
                    break;
                }
                if (level == 253) {
                    meso = 194050000L;
                    break;
                }
                if (level == 254) {
                    meso = 142100000L;
                }
                break;
            case 16:
                if (level == 250) {
                    meso = 1518750000L;
                    break;
                }
                if (level == 251) {
                    meso = 1215000000L;
                    break;
                }
                if (level == 252) {
                    meso = 972000000L;
                    break;
                }
                if (level == 253) {
                    meso = 777600000L;
                    break;
                }
                if (level == 254) {
                    meso = 622080000L;
                    break;
                }
                if (level == 255) {
                    meso = 497670000L;
                    break;
                }
                if (level == 256) {
                    meso = 391550000L;
                    break;
                }
                if (level == 257) {
                    meso = 303110000L;
                    break;
                }
                if (level == 258) {
                    meso = 229420000L;
                    break;
                }
                if (level == 259) {
                    meso = 168000000L;
                }
                break;
            case 17:
                if (level == 255) {
                    meso = 1797190000L;
                    break;
                }
                if (level == 256) {
                    meso = 1437750000L;
                    break;
                }
                if (level == 257) {
                    meso = 1150200000L;
                    break;
                }
                if (level == 258) {
                    meso = 920160000L;
                    break;
                }
                if (level == 259) {
                    meso = 736130000L;
                    break;
                }
                if (level == 260) {
                    meso = 588910000L;
                    break;
                }
                if (level == 261) {
                    meso = 463330000L;
                    break;
                }
                if (level == 262) {
                    meso = 358680000L;
                    break;
                }
                if (level == 263) {
                    meso = 271480000L;
                    break;
                }
                if (level == 264) {
                    meso = 198800000L;
                }
                break;
            case 18:
                if (level == 260) {
                    meso = 2126250000L;
                    break;
                }
                if (level == 261) {
                    meso = 1701000000L;
                    break;
                }
                if (level == 262) {
                    meso = 1360800000L;
                    break;
                }
                if (level == 263) {
                    meso = 1088640000L;
                    break;
                }
                if (level == 264) {
                    meso = 870920000L;
                    break;
                }
                if (level == 265) {
                    meso = 696730000L;
                    break;
                }
                if (level == 266) {
                    meso = 548160000L;
                    break;
                }
                if (level == 267) {
                    meso = 424360000L;
                    break;
                }
                if (level == 268) {
                    meso = 321180000L;
                    break;
                }
                if (level == 269) {
                    meso = 235200000L;
                }
                break;
            case 19:
                if (level == 265) {
                    meso = 2518600000L;
                    break;
                }
                if (level == 266) {
                    meso = 2014880000L;
                    break;
                }
                if (level == 267) {
                    meso = 1611900000L;
                    break;
                }
                if (level == 268) {
                    meso = 1289520000L;
                    break;
                }
                if (level == 269) {
                    meso = 1031620000L;
                    break;
                }
                if (level == 270) {
                    meso = 8253000000L;
                    break;
                }
                if (level == 271) {
                    meso = 649310000L;
                    break;
                }
                if (level == 272) {
                    meso = 502660000L;
                    break;
                }
                if (level == 273) {
                    meso = 380450000L;
                    break;
                }
                if (level == 274) {
                    meso = 278600000L;
                }
                break;
            case 20:
                if (level == 270) {
                    meso = 2986880000L;
                    break;
                }
                if (level == 271) {
                    meso = 2389500000L;
                    break;
                }
                if (level == 272) {
                    meso = 1911600000L;
                    break;
                }
                if (level == 273) {
                    meso = 1529280000L;
                    break;
                }
                if (level == 274) {
                    meso = 1223430000L;
                    break;
                }
                if (level == 275) {
                    meso = 978740000L;
                    break;
                }
                if (level == 276) {
                    meso = 770040000L;
                    break;
                }
                if (level == 277) {
                    meso = 596120000L;
                    break;
                }
                if (level == 278) {
                    meso = 451180000L;
                    break;
                }
                if (level == 279) {
                    meso = 330400000L;
                }
                break;
            case 21:
                if (level == 275) {
                    meso = 3537430000L;
                    break;
                }
                if (level == 276) {
                    meso = 2829940000L;
                    break;
                }
                if (level == 277) {
                    meso = 2263950000L;
                    break;
                }
                if (level == 278) {
                    meso = 1811160000L;
                    break;
                }
                if (level == 279) {
                    meso = 1448930000L;
                    break;
                }
                if (level == 280) {
                    meso = 1159150000L;
                    break;
                }
                if (level == 281) {
                    meso = 911970000L;
                    break;
                }
                if (level == 282) {
                    meso = 705990000L;
                    break;
                }
                if (level == 283) {
                    meso = 534350000L;
                    break;
                }
                if (level == 284) {
                    meso = 391300000L;
                }
                break;
            case 22:
                if (level == 280) {
                    meso = 4189220000L;
                    break;
                }
                if (level == 281) {
                    meso = 3351380000L;
                    break;
                }
                if (level == 282) {
                    meso = 2681100000L;
                    break;
                }
                if (level == 283) {
                    meso = 2144880000L;
                    break;
                }
                if (level == 284) {
                    meso = 1715910000L;
                    break;
                }
                if (level == 285) {
                    meso = 1372730000L;
                    break;
                }
                if (level == 286) {
                    meso = 1080010000L;
                    break;
                }
                if (level == 287) {
                    meso = 836080000L;
                    break;
                }
                if (level == 288) {
                    meso = 632800000L;
                    break;
                }
                if (level == 289) {
                    meso = 463400000L;
                }
                break;
            case 23:
                if (level == 285) {
                    meso = 4954930000L;
                    break;
                }
                if (level == 286) {
                    meso = 3963940000L;
                    break;
                }
                if (level == 287) {
                    meso = 3171150000L;
                    break;
                }
                if (level == 288) {
                    meso = 2536920000L;
                    break;
                }
                if (level == 289) {
                    meso = 2029540000L;
                    break;
                }
                if (level == 290) {
                    meso = 1623630000L;
                    break;
                }
                if (level == 291) {
                    meso = 1277410000L;
                    break;
                }
                if (level == 292) {
                    meso = 988900000L;
                    break;
                }
                if (level == 293) {
                    meso = 748460000L;
                    break;
                }
                if (level == 294) {
                    meso = 548100000L;
                }
                break;
            case 24:
                if (level == 290) {
                    meso = 5872500000L;
                    break;
                }
                if (level == 291) {
                    meso = 4698000000L;
                    break;
                }
                if (level == 292) {
                    meso = 3758400000L;
                    break;
                }
                if (level == 293) {
                    meso = 3006720000L;
                    break;
                }
                if (level == 294) {
                    meso = 2405380000L;
                    break;
                }
                if (level == 295) {
                    meso = 1924310000L;
                    break;
                }
                if (level == 296) {
                    meso = 1513970000L;
                    break;
                }
                if (level == 297) {
                    meso = 1172020000L;
                    break;
                }
                if (level == 298) {
                    meso = 887070000L;
                    break;
                }
                if (level == 299) {
                    meso = 649600000L;
                }
                break;
        }
        return meso;
    }

    public static String stringToHex(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            result = result + String.format("%02X ", new Object[]{Integer.valueOf(s.charAt(i))});
        }
        return result;
    }

    public static int blessSkillJob(final int job) { // 정축,여축
        if (job >= 0 && job <= 910) { // 모험가
            return 0;
        }
        if (job >= 1000 && job <= 1512) { // 시그너스
            return 10000000;
        }
        if (job >= 3000 && job <= 3512) { // 레지스탕스
            return 30000000;
        }
        if (isKaiser(job)) { // 카이저
            return 60000000;
        }
        if (isLara(job)) { // 라라
            return 160010000;
        }
        if (isLuminous(job)) { // 루미너스
            return 20040000;
        }
        if (isXenon(job)) { // 제논
            return 30020000;
        }
        if (isEvan(job)) { // 에반
            return 20010000;
        }
        if (isKadena(job)) { // 카데나
            return 60020000;
        }
        if (isAdel(job)) { // 아델
            return 150020000;
        }
        if (isIllium(job)) { // 일리움
            return 150000000;
        }
        if (isHoyeong(job)) { // 호영
            return 160000000;
        }
        if (isPhantom(job)) { // 팬텀
            return 20030000;
        }
        if (isPinkBean(job)) { // 핑크빈
            return 130000000;
        }
        if (isYeti(job)) { // 예티
            return 130010000;
        }
        if (isZero(job)) { // 제로
            return 100000000;
        }
        if (isDemonSlayer(job) || isDemonAvenger(job)) { // 데몬슬레이어, 어벤저
            return 30010000;
        }
        if (isAran(job)) { // 아란
            return 20000000;
        }
        if (isAngelicBuster(job)) { // 엔젤릭 버스터
            return 60010000;
        }
        if (isArk(job)) { // 아크
            return 150010000;
        }
        if (isEunWol(job)) { // 은월
            return 20050000;
        }
        if (isMichael(job)) { // 미하일
            return 50000000;
        }
        if (isKinesis(job)) { // 키네시스
            return 140000000;
        }
        if (isMercedes(job)) { // 메르세데스
            return 20020000;
        }
        if (isCain(job)) { // 카인
            return 60030000;
        }
        return -1;
    }
}
