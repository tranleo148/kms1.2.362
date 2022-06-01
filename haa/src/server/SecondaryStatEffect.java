/*
 * Decompiled with CFR 0.150.
 */
package server;

import client.MapleCharacter;
import client.MapleCoolDownValueHolder;
import client.MapleStat;
import client.MapleTrait;
import client.PlayerStats;
import client.RangeAttack;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import handling.world.MaplePartyCharacter;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataTool;
import provider.MapleDataType;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.Timer;
import server.field.skill.MapleMagicSword;
import server.life.MapleMonster;
import server.maps.ForceAtom;
import server.maps.MapleAtom;
import server.maps.MapleDoor;
import server.maps.MapleExtractor;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import server.maps.MapleSummon;
import server.maps.MechDoor;
import server.maps.SummonMovementType;
import tools.CaltechEval;
import tools.FileoutputUtil;
import tools.Pair;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.SLFCGPacket;

public class SecondaryStatEffect
        implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private byte mastery;
    private byte mobCount;
    private byte attackCount;
    private byte bulletCount;
    private byte reqGuildLevel;
    private byte period;
    private byte expR;
    private byte iceGageCon;
    private byte recipeUseCount;
    private byte recipeValidDay;
    private byte reqSkillLevel;
    private byte slotCount;
    private byte effectedOnAlly;
    private byte effectedOnEnemy;
    private byte type;
    private byte preventslip;
    private byte immortal;
    private byte bs;
    private byte powerCon;
    private short hp;
    private short hpFX;
    private short hcHp;
    private short mp;
    private short mhpR;
    private short mmpR;
    private short pad;
    private short padR;
    private short mad;
    private short madR;
    private short pdd;
    private short mdef;
    private short acc;
    private short avoid;
    private short hands;
    private short speed;
    private short jump;
    private short psdSpeed;
    private short psdJump;
    private short mdf;
    private short mpRCon;
    private short mpCon;
    private short hpCon;
    private short forceCon;
    private short comboConAran;
    private short bdR;
    private short damage;
    private short prop;
    private short subprop;
    private short emhp;
    private short emmp;
    private short epad;
    private short emad;
    private short epdd;
    private short emdd;
    private short ignoreMobpdpR;
    private short ignoreMobDamR;
    private short dot;
    private short dotTime;
    private short dotInterval;
    private short dotSuperpos;
    private short criticaldamage;
    private short pddX;
    private short mddX;
    private short pddR;
    private short mddR;
    private short asrR;
    private short terR;
    private short er;
    private short padX;
    private short madX;
    private short mesoR;
    private short thaw;
    private short selfDestruction;
    private short PVPdamage;
    private short indiePad;
    private short indiePadR;
    private short indieMad;
    private short indieDamReduceR;
    private short indieMadR;
    private short indiePMd;
    private short fatigueChange;
    private short onActive;
    private short str;
    private short dex;
    private short int_;
    private short luk;
    private short strX;
    private short dexX;
    private short intX;
    private short lukX;
    private short strFX;
    private short dexFX;
    private short intFX;
    private short lukFX;
    private short lifeId;
    private short imhp;
    private short immp;
    private short inflation;
    private short useLevel;
    private short mpConReduce;
    private short soulmpCon;
    private short indieDEX;
    private short indieCr;
    private short indieMhp;
    private short indieMmp;
    private short indieStance;
    private short indieAllStat;
    private short indieSpeed;
    private short indieBooster;
    private short indieJump;
    private short indieAcc;
    private short indieEva;
    private short indieEvaR;
    private short indiePdd;
    private short indieMdd;
    private short incPVPdamage;
    private short indieMhpR;
    private short indieMmpR;
    private short indieAsrR;
    private short indieTerR;
    private short indieDamR;
    private short indieBDR;
    private short indieCD;
    private short indieIgnoreMobpdpR;
    private short indiePddR;
    private short IndieExp;
    private short indieStatRBasic;
    private short indieCooltimeReduce;
    private short mobSkill;
    private short mobSkillLevel;
    private short indiePmdR;
    private short morph;
    private short lv2mhp;
    private short lv2mmp;
    private short bufftimeR;
    private short summonTimeR;
    private short killRecoveryR;
    private short dotHealHPPerSecondR;
    private short targetPlus;
    private short targetPlus_5th;
    private short pdR;
    private short arcX;
    private short nbdR;
    private short starX;
    private short mdR;
    private short strR;
    private short dexR;
    private short intR;
    private short lukR;
    private short dropR;
    private short lv2pad;
    private short lv2mad;
    private double hpR;
    private double hpRCon;
    private double mpR;
    private double expRPerM;
    private double t;
    private Map<MapleTrait.MapleTraitType, Integer> traits = new HashMap<MapleTrait.MapleTraitType, Integer>();
    private int duration;
    private int subTime;
    private int ppcon;
    private int ppReq;
    private int ppRecovery;
    private int sourceid;
    private int recipe;
    private int moveTo;
    private int stanceProp;
    private int u;
    private int u2;
    private int v;
    private int v2;
    private int w;
    private int w2;
    private int x;
    private int y;
    private int z;
    private int s;
    private int s2;
    private int q;
    private int q2;
    private int cr;
    private int itemCon;
    private int itemConNo;
    private int bulletConsume;
    private int moneyCon;
    private int damR;
    private int speedMax;
    private int accX;
    private int mhpX;
    private int mmpX;
    private int cooltime;
    private int cooltimeMS;
    private int coolTimeR;
    private int morphId = 0;
    private int expinc;
    private int exp;
    private int monsterRidingId;
    private int consumeOnPickup;
    private int range;
    private int price;
    private int extendPrice;
    private int charColor;
    private int interval;
    private int rewardMeso;
    private int totalprob;
    private int cosmetic;
    private int kp;
    private int damAbsorbShieldR;
    private int damPlus;
    private int nocoolProps;
    private boolean skill;
    private Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
    private ArrayList<Pair<Integer, Integer>> availableMap;
    private Point lt;
    private Point rb;
    private Point lt2;
    private Point rb2;
    private Point lt3;
    private Point rb3;
    private boolean energyChargeCooling = false;
    private boolean energyChargeActived = false;
    private int expBuff;
    private int itemup;
    private int mesoup;
    private int cashup;
    private int berserk;
    private int illusion;
    private int berserk2;
    private int cp;
    private int nuffSkill;
    private int eqskill1;
    private int eqskill2;
    private int eqskill3;
    private long starttime;
    private byte level;
    private List<Integer> petsCanConsume;
    private List<Integer> randomPickup;
    private List<Triple<Integer, Integer, Integer>> rewardItem;

    public static final SecondaryStatEffect loadSkillEffectFromData(MapleData source, int skillid, boolean overtime, int level, String variables) {
        return SecondaryStatEffect.loadFromData(source, skillid, true, overtime, level, variables);
    }

    public static final SecondaryStatEffect loadItemEffectFromData(MapleData source, int itemid) {
        return SecondaryStatEffect.loadFromData(source, itemid, false, false, 1, null);
    }

    private static final Point parsePoint(String path, MapleData source, Point def, String variables, int level) {
        if (variables == null) {
            return MapleDataTool.getPoint(path, source, def);
        }
        MapleData dd = source.getChildByPath(path);
        if (dd == null) {
            return def;
        }
        if (dd.getType() != MapleDataType.STRING) {
            return MapleDataTool.getPoint(path, source, def);
        }
        System.out.println("DATA : " + MapleDataTool.getString(dd));
        return null;
    }

    public static int parseEval(String data, int level) {
        String variables = "x";
        String dddd = data.replace(variables, String.valueOf(level));
        if (dddd.substring(0, 1).equals("-")) {
            dddd = dddd.substring(1, 2).equals("u") || dddd.substring(1, 2).equals("d") ? "n(" + dddd.substring(1, dddd.length()) + ")" : "n" + dddd.substring(1, dddd.length());
        } else if (dddd.substring(0, 1).equals("=")) {
            dddd = dddd.substring(1, dddd.length());
        }
        return (int) new CaltechEval(dddd.replace("\\r\\n", "")).evaluate();
    }

    private static final int parseEval(String path, MapleData source, int def, String variables, int level) {
        if (variables == null) {
            return MapleDataTool.getIntConvert(path, source, def);
        }
        MapleData dd = source.getChildByPath(path);
        if (dd == null) {
            return def;
        }
        if (dd.getType() != MapleDataType.STRING) {
            return MapleDataTool.getIntConvert(path, source, def);
        }
        String ddd = MapleDataTool.getString(dd).replace("y", "x").replace("X", "x");
        String dddd = ddd.replace(variables, String.valueOf(level));
        if (dddd.length() >= 3 && dddd.substring(0, 3).equals("log")) {
            dddd = dddd.replaceAll("\\(", "").replaceAll("\\)", "");
            double base = SecondaryStatEffect.baseLog(Double.parseDouble(dddd.substring(5, level >= 10 ? 7 : 6)), Double.parseDouble(dddd.substring(3, 5)));
            dddd = String.valueOf(base) + dddd.substring(level >= 10 ? 7 : 6);
        } else if (dddd.substring(0, 1).equals("-")) {
            dddd = dddd.substring(1, 2).equals("u") || dddd.substring(1, 2).equals("d") ? "n(" + dddd.substring(1, dddd.length()) + ")" : "n" + dddd.substring(1, dddd.length());
        } else if (dddd.substring(0, 1).equals("=")) {
            dddd = dddd.substring(1, dddd.length());
        }
        if (dddd.equals("2*u") || dddd.equals("n2*u")) {
            dddd = "2*0";
        }
        return (int) new CaltechEval(dddd.replace("\\r\\n", "")).evaluate();
    }

    private static double baseLog(double x, double base) {
        return Math.log10(x) / Math.log10(base);
    }

    // 와드 loadfromdata
    private static SecondaryStatEffect loadFromData(MapleData source, int sourceid, boolean skill, boolean overTime, int level, String variables) {
        SecondaryStatEffect ret = new SecondaryStatEffect();
        try {
            MapleData ltc;
            MapleData ltd;
            int dd;
            ret.sourceid = sourceid;
            ret.skill = skill;
            ret.level = (byte) level;
            if (source == null) {
                return ret;
            }
            ret.duration = SecondaryStatEffect.parseEval("time", source, -1, variables, level);
            ret.subTime = SecondaryStatEffect.parseEval("subTime", source, -1, variables, level);
            ret.hp = (short) SecondaryStatEffect.parseEval("hp", source, 0, variables, level);
            ret.hpFX = (short) SecondaryStatEffect.parseEval("hpFX", source, 0, variables, level);
            ret.hcHp = (short) SecondaryStatEffect.parseEval("hchp", source, 0, variables, level);
            ret.hpR = (double) SecondaryStatEffect.parseEval("hpR", source, 0, variables, level) / 100.0;
            ret.hpRCon = (double) SecondaryStatEffect.parseEval("hpRCon", source, 0, variables, level) / 100.0;
            ret.mp = (short) SecondaryStatEffect.parseEval("mp", source, 0, variables, level);
            ret.mpR = (double) SecondaryStatEffect.parseEval("mpR", source, 0, variables, level) / 100.0;
            ret.ppRecovery = (short) SecondaryStatEffect.parseEval("ppRecovery", source, 0, variables, level);
            ret.mhpR = (short) SecondaryStatEffect.parseEval("mhpR", source, 0, variables, level);
            ret.mmpR = (short) SecondaryStatEffect.parseEval("mmpR", source, 0, variables, level);
            ret.pddR = (short) SecondaryStatEffect.parseEval("pddR", source, 0, variables, level);
            ret.mddR = (short) SecondaryStatEffect.parseEval("mddR", source, 0, variables, level);
            ret.pdR = (short) SecondaryStatEffect.parseEval("pdR", source, 0, variables, level);
            ret.strR = (short) SecondaryStatEffect.parseEval("strR", source, 0, variables, level);
            ret.dexR = (short) SecondaryStatEffect.parseEval("dexR", source, 0, variables, level);
            ret.intR = (short) SecondaryStatEffect.parseEval("intR", source, 0, variables, level);
            ret.lukR = (short) SecondaryStatEffect.parseEval("lukR", source, 0, variables, level);
            ret.arcX = (short) SecondaryStatEffect.parseEval("arcX", source, 0, variables, level);
            ret.nbdR = (short) SecondaryStatEffect.parseEval("nbdR", source, 0, variables, level);
            ret.starX = (short) SecondaryStatEffect.parseEval("starX", source, 0, variables, level);
            ret.ignoreMobpdpR = (short) SecondaryStatEffect.parseEval("ignoreMobpdpR", source, 0, variables, level);
            ret.ignoreMobDamR = (short) SecondaryStatEffect.parseEval("ignoreMobDamR", source, 0, variables, level);
            ret.asrR = (short) SecondaryStatEffect.parseEval("asrR", source, 0, variables, level);
            ret.terR = (short) SecondaryStatEffect.parseEval("terR", source, 0, variables, level);
            ret.setBdR((short) SecondaryStatEffect.parseEval("bdR", source, 0, variables, level));
            ret.damR = SecondaryStatEffect.parseEval("damR", source, 0, variables, level);
            ret.mesoR = (short) SecondaryStatEffect.parseEval("mesoR", source, 0, variables, level);
            ret.thaw = (short) SecondaryStatEffect.parseEval("thaw", source, 0, variables, level);
            ret.padX = (short) SecondaryStatEffect.parseEval("padX", source, 0, variables, level);
            ret.pddX = (short) SecondaryStatEffect.parseEval("pddX", source, 0, variables, level);
            ret.mddX = (short) SecondaryStatEffect.parseEval("mddX", source, 0, variables, level);
            ret.madX = (short) SecondaryStatEffect.parseEval("madX", source, 0, variables, level);
            ret.dot = (short) SecondaryStatEffect.parseEval("dot", source, 0, variables, level);
            ret.dotTime = (short) SecondaryStatEffect.parseEval("dotTime", source, 0, variables, level);
            ret.dotInterval = (short) SecondaryStatEffect.parseEval("dotInterval", source, 1, variables, level);
            ret.setDotSuperpos((short) SecondaryStatEffect.parseEval("dotSuperpos", source, 0, variables, level));
            ret.criticaldamage = (short) SecondaryStatEffect.parseEval("criticaldamage", source, 0, variables, level);
            ret.mpConReduce = (short) SecondaryStatEffect.parseEval("mpConReduce", source, 0, variables, level);
            ret.soulmpCon = (short) SecondaryStatEffect.parseEval("soulmpCon", source, 0, variables, level);
            ret.setForceCon((short) SecondaryStatEffect.parseEval("forceCon", source, 0, variables, level));
            ret.mpCon = (short) SecondaryStatEffect.parseEval("mpCon", source, 0, variables, level);
            ret.mpRCon = (short) SecondaryStatEffect.parseEval("mpRCon", source, 0, variables, level);
            ret.hpCon = (short) SecondaryStatEffect.parseEval("hpCon", source, 0, variables, level);
            ret.comboConAran = (short) SecondaryStatEffect.parseEval("comboConAran", source, 0, variables, level);
            ret.prop = (short) SecondaryStatEffect.parseEval("prop", source, 100, variables, level);
            ret.subprop = (short) SecondaryStatEffect.parseEval("subProp", source, 100, variables, level);
            ret.mdR = (short) SecondaryStatEffect.parseEval("mdR", source, 100, variables, level);
            ret.cooltime = Math.max(0, SecondaryStatEffect.parseEval("cooltime", source, 0, variables, level));
            ret.cooltimeMS = Math.max(0, SecondaryStatEffect.parseEval("cooltimeMS", source, 0, variables, level));
            ret.coolTimeR = Math.max(0, SecondaryStatEffect.parseEval("coolTimeR", source, 0, variables, level));
            ret.interval = SecondaryStatEffect.parseEval("interval", source, 0, variables, level);
            ret.expinc = SecondaryStatEffect.parseEval("expinc", source, 0, variables, level);
            ret.exp = SecondaryStatEffect.parseEval("exp", source, 0, variables, level);
            ret.range = SecondaryStatEffect.parseEval("range", source, 0, variables, level);
            ret.morphId = SecondaryStatEffect.parseEval("morph", source, 0, variables, level);
            ret.cp = SecondaryStatEffect.parseEval("cp", source, 0, variables, level);
            ret.cosmetic = SecondaryStatEffect.parseEval("cosmetic", source, 0, variables, level);
            ret.er = (short) SecondaryStatEffect.parseEval("er", source, 0, variables, level);
            ret.ppcon = SecondaryStatEffect.parseEval("ppCon", source, 0, variables, level);
            ret.ppReq = SecondaryStatEffect.parseEval("ppReq", source, 0, variables, level);
            ret.ppRecovery = (short) SecondaryStatEffect.parseEval("ppRecovery", source, 0, variables, level);
            ret.slotCount = (byte) SecondaryStatEffect.parseEval("slotCount", source, 0, variables, level);
            ret.preventslip = (byte) SecondaryStatEffect.parseEval("preventslip", source, 0, variables, level);
            ret.useLevel = (short) SecondaryStatEffect.parseEval("useLevel", source, 0, variables, level);
            ret.nuffSkill = SecondaryStatEffect.parseEval("nuffSkill", source, 0, variables, level);
            ret.mobCount = (byte) SecondaryStatEffect.parseEval("mobCount", source, 1, variables, level);
            ret.immortal = (byte) SecondaryStatEffect.parseEval("immortal", source, 0, variables, level);
            ret.iceGageCon = (byte) SecondaryStatEffect.parseEval("iceGageCon", source, 0, variables, level);
            ret.expR = (byte) SecondaryStatEffect.parseEval("expR", source, 0, variables, level);
            ret.expRPerM = (double) SecondaryStatEffect.parseEval("expRPerM", source, 0, variables, level) / 100.0;
            ret.dropR = (short) SecondaryStatEffect.parseEval("expR", source, 0, variables, level);
            ret.reqGuildLevel = (byte) SecondaryStatEffect.parseEval("reqGuildLevel", source, 0, variables, level);
            ret.period = (byte) SecondaryStatEffect.parseEval("period", source, 0, variables, level);
            ret.type = (byte) SecondaryStatEffect.parseEval("type", source, 0, variables, level);
            ret.bs = (byte) SecondaryStatEffect.parseEval("bs", source, 0, variables, level);
            ret.mdf = (byte) SecondaryStatEffect.parseEval("MDF", source, 0, variables, level);
            ret.attackCount = (byte) SecondaryStatEffect.parseEval("attackCount", source, 1, variables, level);
            ret.bulletCount = (byte) SecondaryStatEffect.parseEval("bulletCount", source, 1, variables, level);
            ret.speedMax = SecondaryStatEffect.parseEval("speedMax", source, 0, variables, level);
            ret.accX = SecondaryStatEffect.parseEval("accX", source, 0, variables, level);
            ret.setMhpX(SecondaryStatEffect.parseEval("mhpX", source, 0, variables, level));
            ret.mmpX = SecondaryStatEffect.parseEval("mmpX", source, 0, variables, level);
            int priceUnit = SecondaryStatEffect.parseEval("priceUnit", source, 0, variables, level);
            ret.indieDamReduceR = (short) SecondaryStatEffect.parseEval("indieDamReduceR", source, 0, variables, level);
            ret.lv2mhp = (short) SecondaryStatEffect.parseEval("lv2mhp", source, 0, variables, level);
            ret.lv2mmp = (short) SecondaryStatEffect.parseEval("lv2mmp", source, 0, variables, level);
            ret.lv2pad = (short) SecondaryStatEffect.parseEval("lv2pad", source, 0, variables, level);
            ret.lv2mad = (short) SecondaryStatEffect.parseEval("lv2mad", source, 0, variables, level);
            ret.lt = SecondaryStatEffect.parsePoint("lt", source, new Point(0, 0), variables, level);
            ret.rb = SecondaryStatEffect.parsePoint("rb", source, new Point(0, 0), variables, level);
            ret.lt2 = SecondaryStatEffect.parsePoint("lt2", source, new Point(0, 0), variables, level);
            ret.rb2 = SecondaryStatEffect.parsePoint("rb2", source, new Point(0, 0), variables, level);
            ret.lt3 = SecondaryStatEffect.parsePoint("lt3", source, new Point(0, 0), variables, level);
            ret.rb3 = SecondaryStatEffect.parsePoint("rb3", source, new Point(0, 0), variables, level);
            ret.setBufftimeR((short) SecondaryStatEffect.parseEval("bufftimeR", source, 0, variables, level));
            ret.summonTimeR = (short) SecondaryStatEffect.parseEval("summonTimeR", source, 0, variables, level);
            ret.stanceProp = SecondaryStatEffect.parseEval("stanceProp", source, 0, variables, level);
            ret.damAbsorbShieldR = SecondaryStatEffect.parseEval("damAbsorbShieldR", source, 0, variables, level);
            ret.damPlus = SecondaryStatEffect.parseEval("damPlus", source, 0, variables, level);
            ret.nocoolProps = SecondaryStatEffect.parseEval("nocoolProps", source, 0, variables, level);
            ret.setKillRecoveryR((short) SecondaryStatEffect.parseEval("killRecoveryR", source, 0, variables, level));
            ret.dotHealHPPerSecondR = (short) SecondaryStatEffect.parseEval("dotHealHPPerSecondR", source, 0, variables, level);
            ret.targetPlus = (short) SecondaryStatEffect.parseEval("targetPlus", source, 0, variables, level);
            ret.targetPlus_5th = (short) SecondaryStatEffect.parseEval("targetPlus_5th", source, 0, variables, level);
            if (priceUnit > 0) {
                ret.price = SecondaryStatEffect.parseEval("price", source, 0, variables, level) * priceUnit;
                ret.extendPrice = SecondaryStatEffect.parseEval("extendPrice", source, 0, variables, level) * priceUnit;
            } else {
                ret.price = 0;
                ret.extendPrice = 0;
            }
            if (ret.skill || ret.duration <= -1) {
                ret.duration *= 1000;
                ret.subTime *= 1000;
            }
            ret.cooltime *= 1000;
            ret.dotTime = (short) (ret.dotTime * 1000);
            ret.dotInterval = (short) (ret.dotInterval * 1000);
            ret.mastery = (byte) SecondaryStatEffect.parseEval("mastery", source, 0, variables, level);
            ret.pad = (short) SecondaryStatEffect.parseEval("pad", source, 0, variables, level);
            ret.padR = (short) SecondaryStatEffect.parseEval("padR", source, 0, variables, level);
            ret.setPdd((short) SecondaryStatEffect.parseEval("pdd", source, 0, variables, level));
            ret.mad = (short) SecondaryStatEffect.parseEval("mad", source, 0, variables, level);
            ret.madR = (short) SecondaryStatEffect.parseEval("madR", source, 0, variables, level);
            ret.mdef = (short) SecondaryStatEffect.parseEval("mdd", source, 0, variables, level);
            ret.emhp = (short) SecondaryStatEffect.parseEval("emhp", source, 0, variables, level);
            ret.emmp = (short) SecondaryStatEffect.parseEval("emmp", source, 0, variables, level);
            ret.epad = (short) SecondaryStatEffect.parseEval("epad", source, 0, variables, level);
            ret.emad = (short) SecondaryStatEffect.parseEval("emad", source, 0, variables, level);
            ret.epdd = (short) SecondaryStatEffect.parseEval("epdd", source, 0, variables, level);
            ret.emdd = (short) SecondaryStatEffect.parseEval("emdd", source, 0, variables, level);
            ret.acc = (short) SecondaryStatEffect.parseEval("acc", source, 0, variables, level);
            ret.avoid = (short) SecondaryStatEffect.parseEval("eva", source, 0, variables, level);
            ret.speed = (short) SecondaryStatEffect.parseEval("speed", source, 0, variables, level);
            ret.jump = (short) SecondaryStatEffect.parseEval("jump", source, 0, variables, level);
            ret.psdSpeed = (short) SecondaryStatEffect.parseEval("psdSpeed", source, 0, variables, level);
            ret.psdJump = (short) SecondaryStatEffect.parseEval("psdJump", source, 0, variables, level);
            ret.indieDEX = (short) SecondaryStatEffect.parseEval("indieDEX", source, 0, variables, level);
            ret.indieCr = (short) SecondaryStatEffect.parseEval("indieCr", source, 0, variables, level);
            ret.indiePad = (short) SecondaryStatEffect.parseEval("indiePad", source, 0, variables, level);
            ret.indiePadR = (short) SecondaryStatEffect.parseEval("indiePadR", source, 0, variables, level);
            ret.indieMad = (short) SecondaryStatEffect.parseEval("indieMad", source, 0, variables, level);
            ret.indieMadR = (short) SecondaryStatEffect.parseEval("indieMadR", source, 0, variables, level);
            ret.indiePMd = (short) SecondaryStatEffect.parseEval("indiePMd", source, 0, variables, level);
            ret.indieMhp = (short) SecondaryStatEffect.parseEval("indieMhp", source, 0, variables, level);
            ret.indieMmp = (short) SecondaryStatEffect.parseEval("indieMmp", source, 0, variables, level);
            ret.indieBooster = (short) SecondaryStatEffect.parseEval("indieBooster", source, 0, variables, level);
            ret.indieSpeed = (short) SecondaryStatEffect.parseEval("indieSpeed", source, 0, variables, level);
            ret.indieJump = (short) SecondaryStatEffect.parseEval("indieJump", source, 0, variables, level);
            ret.indieAcc = (short) SecondaryStatEffect.parseEval("indieAcc", source, 0, variables, level);
            ret.indieEva = (short) SecondaryStatEffect.parseEval("indieEva", source, 0, variables, level);
            ret.indieEvaR = (short) SecondaryStatEffect.parseEval("indieEvaR", source, 0, variables, level);
            ret.indiePdd = (short) SecondaryStatEffect.parseEval("indiePdd", source, 0, variables, level);
            ret.indieMdd = (short) SecondaryStatEffect.parseEval("indieMdd", source, 0, variables, level);
            ret.indieDamR = (short) SecondaryStatEffect.parseEval("indieDamR", source, 0, variables, level);
            ret.indieBDR = (short) SecondaryStatEffect.parseEval("indieBDR", source, 0, variables, level);
            ret.indieCD = (short) SecondaryStatEffect.parseEval("indieCD", source, 0, variables, level);
            ret.indieIgnoreMobpdpR = (short) SecondaryStatEffect.parseEval("indieIgnoreMobpdpR", source, 0, variables, level);
            ret.indiePddR = (short) SecondaryStatEffect.parseEval("indiePddR", source, 0, variables, level);
            ret.IndieExp = (short) SecondaryStatEffect.parseEval("IndieExp", source, 0, variables, level);
            ret.indieStatRBasic = (short) SecondaryStatEffect.parseEval("indieStatRBasic", source, 0, variables, level);
            ret.indieCooltimeReduce = (short) SecondaryStatEffect.parseEval("indieCooltimeReduce", source, 0, variables, level);
            ret.indieAllStat = (short) SecondaryStatEffect.parseEval("indieAllStat", source, 0, variables, level);
            ret.indieStance = (short) SecondaryStatEffect.parseEval("indieStance", source, 0, variables, level);
            ret.setIndieMhpR((short) SecondaryStatEffect.parseEval("indieMhpR", source, 0, variables, level));
            ret.indieMmpR = (short) SecondaryStatEffect.parseEval("indieMmpR", source, 0, variables, level);
            ret.indieAsrR = (short) SecondaryStatEffect.parseEval("indieAsrR", source, 0, variables, level);
            ret.indieTerR = (short) SecondaryStatEffect.parseEval("indieTerR", source, 0, variables, level);
            ret.onActive = (short) SecondaryStatEffect.parseEval("onActive", source, 0, variables, level);
            ret.str = (short) SecondaryStatEffect.parseEval("str", source, 0, variables, level);
            ret.dex = (short) SecondaryStatEffect.parseEval("dex", source, 0, variables, level);
            ret.int_ = (short) SecondaryStatEffect.parseEval("int", source, 0, variables, level);
            ret.luk = (short) SecondaryStatEffect.parseEval("luk", source, 0, variables, level);
            ret.strX = (short) SecondaryStatEffect.parseEval("strX", source, 0, variables, level);
            ret.dexX = (short) SecondaryStatEffect.parseEval("dexX", source, 0, variables, level);
            ret.intX = (short) SecondaryStatEffect.parseEval("intX", source, 0, variables, level);
            ret.lukX = (short) SecondaryStatEffect.parseEval("lukX", source, 0, variables, level);
            ret.strFX = (short) SecondaryStatEffect.parseEval("strFX", source, 0, variables, level);
            ret.dexFX = (short) SecondaryStatEffect.parseEval("dexFX", source, 0, variables, level);
            ret.intFX = (short) SecondaryStatEffect.parseEval("intFX", source, 0, variables, level);
            ret.lukFX = (short) SecondaryStatEffect.parseEval("lukFX", source, 0, variables, level);
            ret.expBuff = SecondaryStatEffect.parseEval("expBuff", source, 0, variables, level);
            ret.cashup = SecondaryStatEffect.parseEval("cashBuff", source, 0, variables, level);
            ret.itemup = SecondaryStatEffect.parseEval("itemupbyitem", source, 0, variables, level);
            ret.mesoup = SecondaryStatEffect.parseEval("mesoupbyitem", source, 0, variables, level);
            ret.berserk = SecondaryStatEffect.parseEval("berserk", source, 0, variables, level);
            ret.berserk2 = SecondaryStatEffect.parseEval("berserk2", source, 0, variables, level);
            ret.lifeId = (short) SecondaryStatEffect.parseEval("lifeId", source, 0, variables, level);
            ret.inflation = (short) SecondaryStatEffect.parseEval("inflation", source, 0, variables, level);
            ret.imhp = (short) SecondaryStatEffect.parseEval("imhp", source, 0, variables, level);
            ret.immp = (short) SecondaryStatEffect.parseEval("immp", source, 0, variables, level);
            ret.illusion = SecondaryStatEffect.parseEval("illusion", source, 0, variables, level);
            ret.consumeOnPickup = SecondaryStatEffect.parseEval("consumeOnPickup", source, 0, variables, level);
            ret.setIndiePmdR((short) SecondaryStatEffect.parseEval("indiePMdR", source, 0, variables, level));
            ret.morph = (short) SecondaryStatEffect.parseEval("morph", source, 0, variables, level);
            ret.kp = SecondaryStatEffect.parseEval("kp", source, 0, variables, level);
            if (ret.consumeOnPickup == 1 && SecondaryStatEffect.parseEval("party", source, 0, variables, level) > 0) {
                ret.consumeOnPickup = 2;
            }
            ret.charColor = 0;
            String cColor = MapleDataTool.getString("charColor", source, null);
            if (cColor != null) {
                ret.charColor |= Integer.parseInt("0x" + cColor.substring(0, 2));
                ret.charColor |= Integer.parseInt("0x" + cColor.substring(2, 4) + "00");
                ret.charColor |= Integer.parseInt("0x" + cColor.substring(4, 6) + "0000");
                ret.charColor |= Integer.parseInt("0x" + cColor.substring(6, 8) + "000000");
            }
            ret.traits = new EnumMap<MapleTrait.MapleTraitType, Integer>(MapleTrait.MapleTraitType.class);
            for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
                int expz = SecondaryStatEffect.parseEval(t.name() + "EXP", source, 0, variables, level);
                if (expz == 0) {
                    continue;
                }
                ret.traits.put(t, expz);
            }
            ret.recipe = SecondaryStatEffect.parseEval("recipe", source, 0, variables, level);
            ret.recipeUseCount = (byte) SecondaryStatEffect.parseEval("recipeUseCount", source, 0, variables, level);
            ret.recipeValidDay = (byte) SecondaryStatEffect.parseEval("recipeValidDay", source, 0, variables, level);
            ret.reqSkillLevel = (byte) SecondaryStatEffect.parseEval("reqSkillLevel", source, 0, variables, level);
            ret.powerCon = (byte) SecondaryStatEffect.parseEval("powerCon", source, 0, variables, level);
            ret.effectedOnAlly = (byte) SecondaryStatEffect.parseEval("effectedOnAlly", source, 0, variables, level);
            ret.effectedOnEnemy = (byte) SecondaryStatEffect.parseEval("effectedOnEnemy", source, 0, variables, level);
            ret.petsCanConsume = new ArrayList<Integer>();
            int i = 0;
            while ((dd = SecondaryStatEffect.parseEval(String.valueOf(i), source, 0, variables, level)) > 0) {
                ret.petsCanConsume.add(dd);
                ++i;
            }
            MapleData mdd = source.getChildByPath("0");
            if (mdd != null && mdd.getChildren().size() > 0) {
                ret.mobSkill = (short) SecondaryStatEffect.parseEval("mobSkill", mdd, 0, variables, level);
                ret.mobSkillLevel = (short) SecondaryStatEffect.parseEval("level", mdd, 0, variables, level);
            } else {
                ret.mobSkill = 0;
                ret.mobSkillLevel = 0;
            }
            MapleData pd = source.getChildByPath("randomPickup");
            if (pd != null) {
                ret.randomPickup = new ArrayList<Integer>();
                for (MapleData p : pd) {
                    ret.randomPickup.add(MapleDataTool.getInt(p));
                }
            }
            if ((ltd = source.getChildByPath("lt")) != null) {
                ret.setLt((Point) ltd.getData());
                ret.rb = (Point) source.getChildByPath("rb").getData();
            }
            if ((ltc = source.getChildByPath("con")) != null) {
                ret.availableMap = new ArrayList();
                for (MapleData ltb : ltc) {
                    ret.availableMap.add(new Pair<Integer, Integer>(MapleDataTool.getInt("sMap", ltb, 0), MapleDataTool.getInt("eMap", ltb, 999999999)));
                }
            }
            ret.fatigueChange = 0;
            int totalprob = 0;
            MapleData lta = source.getChildByPath("reward");
            if (lta != null) {
                ret.rewardMeso = SecondaryStatEffect.parseEval("meso", lta, 0, variables, level);
                MapleData ltz = lta.getChildByPath("case");
                if (ltz != null) {
                    ret.rewardItem = new ArrayList<Triple<Integer, Integer, Integer>>();
                    for (MapleData lty : ltz) {
                        ret.rewardItem.add(new Triple<Integer, Integer, Integer>(MapleDataTool.getInt("id", lty, 0), MapleDataTool.getInt("count", lty, 0), MapleDataTool.getInt("prop", lty, 0)));
                        totalprob += MapleDataTool.getInt("prob", lty, 0);
                    }
                }
            } else {
                ret.rewardMeso = 0;
            }
            ret.totalprob = totalprob;
            ret.cr = SecondaryStatEffect.parseEval("cr", source, 0, variables, level);
            ret.t = SecondaryStatEffect.parseEval("t", source, 0, variables, level);
            ret.u = SecondaryStatEffect.parseEval("u", source, 0, variables, level);
            ret.setU2(SecondaryStatEffect.parseEval("u2", source, 0, variables, level));
            ret.v = SecondaryStatEffect.parseEval("v", source, 0, variables, level);
            ret.v2 = SecondaryStatEffect.parseEval("v2", source, 0, variables, level);
            ret.w = SecondaryStatEffect.parseEval("w", source, 0, variables, level);
            ret.setW2(SecondaryStatEffect.parseEval("w2", source, 0, variables, level));
            ret.x = SecondaryStatEffect.parseEval("x", source, 0, variables, level);
            ret.y = SecondaryStatEffect.parseEval("y", source, 0, variables, level);
            ret.z = SecondaryStatEffect.parseEval("z", source, 0, variables, level);
            ret.s = SecondaryStatEffect.parseEval("s", source, 0, variables, level);
            ret.setS2(SecondaryStatEffect.parseEval("s2", source, 0, variables, level));
            ret.q = SecondaryStatEffect.parseEval("q", source, 0, variables, level);
            ret.q2 = SecondaryStatEffect.parseEval("q2", source, 0, variables, level);
            ret.damage = (short) SecondaryStatEffect.parseEval("damage", source, 0, variables, level);
            ret.PVPdamage = (short) SecondaryStatEffect.parseEval("PVPdamage", source, 0, variables, level);
            ret.incPVPdamage = (short) SecondaryStatEffect.parseEval("incPVPDamage", source, 0, variables, level);
            ret.selfDestruction = (short) SecondaryStatEffect.parseEval("selfDestruction", source, 0, variables, level);
            ret.bulletConsume = SecondaryStatEffect.parseEval("bulletConsume", source, 0, variables, level);
            ret.moneyCon = SecondaryStatEffect.parseEval("moneyCon", source, 0, variables, level);
            ret.itemCon = SecondaryStatEffect.parseEval("itemCon", source, 0, variables, level);
            ret.itemConNo = SecondaryStatEffect.parseEval("itemConNo", source, 0, variables, level);
            ret.moveTo = SecondaryStatEffect.parseEval("moveTo", source, -1, variables, level);
            if (ret.skill) {
                switch (sourceid) {
                    case 0x10CCCC:
                    case 1201004:
                    case 1301004:
                    case 2101008:
                    case 2201010:
                    case 2301008:
                    case 3101002:
                    case 3201002:
                    case 3301010:
                    case 4101003:
                    case 4201002:
                    case 4301002:
                    case 4311009:
                    case 5101006:
                    case 5201003:
                    case 5301002:
                    case 11101024:
                    case 12101004:
                    case 13101023:
                    case 14101022:
                    case 15101002:
                    case 15101022:
                    case 22111020:
                    case 23101002:
                    case 24101005:
                    case 27101004:
                    case 31001001:
                    case 31201002:
                    case 32101005:
                    case 33101012:
                    case 35101006:
                    case 36101004:
                    case 37101003:
                    case 51101003:
                    case 63101010:
                    case 64101003:
                    case 151101005:
                    case 152101007:
                    case 155101005:
                    case 162101013:
                    case 164101005: {
                        ret.statups.put(SecondaryStat.Booster, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 1121000:
                    case 1221000:
                    case 1321000:
                    case 2121000:
                    case 2221000:
                    case 2321000:
                    case 3121000:
                    case 3221000:
                    case 3321023:
                    case 4121000:
                    case 4221000:
                    case 4341000:
                    case 5121000:
                    case 5221000:
                    case 5321005:
                    case 11121000:
                    case 12121000:
                    case 13121000:
                    case 14121000:
                    case 15121000:
                    case 21121000:
                    case 22171068:
                    case 23121005:
                    case 24121008:
                    case 25121108:
                    case 27121009:
                    case 31121004:
                    case 31221008:
                    case 32121007:
                    case 33121007:
                    case 35121007:
                    case 36121008:
                    case 37121006:
                    case 51121005:
                    case 61121014:
                    case 63121009:
                    case 64121004:
                    case 65121009:
                    case 100001268:
                    case 142121016:
                    case 151121005:
                    case 152121009:
                    case 155121008:
                    case 162121023:
                    case 164121009: {
                        ret.statups.put(SecondaryStat.BasicStatUp, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 0x111B1D:
                    case 1221053:
                    case 1321053:
                    case 2121053:
                    case 2221053:
                    case 2321053:
                    case 3121053:
                    case 3221053:
                    case 3321041:
                    case 4121053:
                    case 4221053:
                    case 4341053:
                    case 5121053:
                    case 5221053:
                    case 5321053:
                    case 11121053:
                    case 12121053:
                    case 13121053:
                    case 14121053:
                    case 15121053:
                    case 21121053:
                    case 22171082:
                    case 23121053:
                    case 24121053:
                    case 25121132:
                    case 27121053:
                    case 31121053:
                    case 31221053:
                    case 32121053:
                    case 33121053:
                    case 35121053:
                    case 37121053:
                    case 51121053:
                    case 151121042:
                    case 152121042:
                    case 155121042: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 1005:
                    case 10001005:
                    case 10001215:
                    case 20001005:
                    case 20011005:
                    case 20021005:
                    case 20031005:
                    case 20041005:
                    case 20051005:
                    case 30001005:
                    case 30011005:
                    case 30021005:
                    case 50001005:
                    case 50001215:
                    case 60001005:
                    case 60011005:
                    case 60021005:
                    case 60031005:
                    case 100001005:
                    case 140001005:
                    case 150001005:
                    case 150011005:
                    case 150021005:
                    case 160001005: {
                        ret.statups.put(SecondaryStat.MaxLevelBuff, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 1101006: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.PowerGaurd, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 1101013: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ComboCounter, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 1121010: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Enrage, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.EnrageCrDamMin, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 1121054: { // 값
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCr), ret.duration));
                        ret.statups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(100, ret.duration));
                        ret.statups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 1200014:
                    case 1220010: {
                        ret.statups.put(SecondaryStat.ElementalCharge, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 1210016: {
                        ret.statups.put(SecondaryStat.BlessingArmor, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.BlessingArmorIncPad, new Pair<Integer, Integer>(Integer.valueOf(ret.epad), ret.duration));
                        break;
                    }
                    case 1211010: {
                        ret.statups.put(SecondaryStat.Listonation, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 1211011: {
                        ret.statups.put(SecondaryStat.CombatOrders, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 1221015: {
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        break;
                    }
                    case 1221016: {
                        ret.statups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(ret.duration, ret.duration));
                        break;
                    }
                    case 1221054: {
                        ret.statups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 1301006: {
                        ret.statups.put(SecondaryStat.Pdd, new Pair<Integer, Integer>(Integer.valueOf(ret.pdd), ret.duration));
                        break;
                    }
                    case 1301007: {
                        ret.statups.put(SecondaryStat.MaxHP, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.MaxMP, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 1311015: {
                        ret.statups.put(SecondaryStat.CrossOverChain, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 1321015: {
                        ret.hpR = (double) ret.y / 100.0;
                        ret.statups.put(SecondaryStat.IgnoreTargetDEF, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        break;
                    }
                    case 1320019: {
                        ret.statups.put(SecondaryStat.Reincarnation, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 1321054: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.AuraRecovery, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 2001002:
                    case 12001001:
                    case 22001012: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.MagicGaurd, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 2111008:
                    case 2211008:
                    case 12101005:
                    case 22141016: {
                        ret.statups.put(SecondaryStat.ElementalReset, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 2111007:
                    case 2211007:
                    case 2311007:
                    case 22161005:
                    case 32111010: {
                        ret.mpCon = (short) ret.y;
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.TeleportMastery, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2101001: {
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        break;
                    }
                    case 2101010: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.WizardIgnite, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2121054: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.FireAura, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2201001: {
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        break;
                    }
                    case 2221011: {
                        ret.duration = 1000;
                        ret.statups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2221045:
                    case 2211017:
                    case 2311016:
                    case 2111016:
                    case 32111021:
                    case 152111014:
                    case 27111008: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.TeleportMasteryRange, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 2321054: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.VengeanceOfAngel, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2311015: {
                        ret.statups.put(SecondaryStat.Triumph, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 13121005:
                    case 33121004:
                    case 400001002: {
                        ret.statups.put(SecondaryStat.SharpEyes, new Pair<Integer, Integer>((ret.x << 8) + ret.y, ret.duration));
                        break;
                    }
                    case 3101004: {
                        ret.statups.put(SecondaryStat.SoulArrow, new Pair<Integer, Integer>(Integer.valueOf(ret.epad), ret.duration));
                        ret.statups.put(SecondaryStat.EnhancedPad, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.Concentration, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 3111011: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ExtremeArchery, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        break;
                    }
                    case 3121016: {
                        ret.statups.put(SecondaryStat.AdvancedQuiver, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 3121007: {
                        ret.statups.put(SecondaryStat.IndieDex, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDEX), ret.duration));
                        ret.statups.put(SecondaryStat.Eva, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 3121054: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.Preparation, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 3201004: {
                        ret.statups.put(SecondaryStat.SoulArrow, new Pair<Integer, Integer>(Integer.valueOf(ret.epad), ret.duration));
                        ret.statups.put(SecondaryStat.Concentration, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.EnhancedPad, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 3211011: { // 값
                        ret.statups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(Integer.valueOf(ret.asrR), ret.duration));
                        break;
                    }
                    case 3211012: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ExtremeArchery, new Pair<Integer, Integer>(ret.z, ret.duration));
                        break;
                    }
                    case 3221006: {
                        ret.statups.put(SecondaryStat.IndieDex, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDEX), ret.duration));
                        ret.statups.put(SecondaryStat.Eva, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 3321036:
                    case 3321038: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.IndieDamageReduce, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamReduceR), ret.duration));
                        break;
                    }
                    case 3221054: {
                        ret.statups.put(SecondaryStat.IgnoreTargetDEF, new Pair<Integer, Integer>(0, ret.duration));
                        ret.statups.put(SecondaryStat.BullsEye, new Pair<Integer, Integer>((ret.x << 8) + ret.y, ret.duration));
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieIgnoreMobpdpR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 3310006: {
                        ret.statups.put(SecondaryStat.AncientGuidance, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        break;
                    }
                    case 3311012: {
                        ret.statups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(ret.s, ret.duration));
                        break;
                    }
                    case 3321040: {
                        ret.duration = ret.u * 1000;
                        ret.statups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 14001023: {
                        ret.statups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 4001005:
                    case 14001022: {
                        ret.statups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(Integer.valueOf(ret.speed), ret.duration));
                        ret.statups.put(SecondaryStat.Jump, new Pair<Integer, Integer>(Integer.valueOf(ret.jump), ret.duration));
                        break;
                    }
                    case 4111002:
                    case 0x404140:
                    case 4331002: {
                        ret.statups.put(SecondaryStat.ShadowPartner, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 0x3E9393: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.MarkofNightLord, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 4121054: {
                        ret.statups.put(SecondaryStat.BleedingToxin, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        break;
                    }
                    case 4201017: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Steal, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 4221018:
                    case 4211003: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.PickPocket, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 4221020: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Murderous, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 4341052: {
                        ret.statups.put(SecondaryStat.Asura, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 4341054: {
                        ret.statups.put(SecondaryStat.WindBreakerFinal, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 5001005: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.DashSpeed, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.DashJump, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 5120011:
                    case 5220012: {
                        ret.statups.put(SecondaryStat.DamageReduce, new Pair<Integer, Integer>(ret.y, ret.duration));
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 5100015:
                    case 5110014:
                    case 5120018: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.EnergyCharged, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 5121009: {
                        ret.statups.put(SecondaryStat.PartyBooster, new Pair<Integer, Integer>(-2, ret.duration));
                        break;
                    }
                    case 5121015: {
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePadR), ret.duration));
                        break;
                    }
                    case 5221015: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.GuidedBullet, new Pair<Integer, Integer>(1, 0));
                        break;
                    }
                    case 5221018: {
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePadR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.Eva, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAsrR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieTerR), ret.duration));
                        break;
                    }
                    case 5301003:
                    case 5320008: {
                        ret.statups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMhp), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMp, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMmp), ret.duration));
                        ret.statups.put(SecondaryStat.IndieJump, new Pair<Integer, Integer>(Integer.valueOf(ret.indieJump), ret.duration));
                        ret.statups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(ret.indieSpeed), ret.duration));
                        ret.statups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAllStat), ret.duration));
                        break;
                    }
                    case 5321010: {
                        ret.statups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 5321054: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(-ret.y, ret.duration));
                        ret.statups.put(SecondaryStat.Buckshot, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 9001004: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.IndieEva, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 11001022: {
                        ret.statups.put(SecondaryStat.CygnusElementSkill, new Pair<Integer, Integer>(1, 0));
                        break;
                    }
                    case 11121054: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.LightOfSpirit, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.AccR, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 12101022: {
                        ret.mpR = (double) ret.x / 100.0;
                        break;
                    }
                    case 12101023: {
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-ret.x, ret.duration));
                        break;
                    }
                    case 12101024: {
                        ret.statups.put(SecondaryStat.Ember, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 12121003: {
                        ret.statups.put(SecondaryStat.DamageReduce, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 12121043: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.AddRange, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 13001022: {
                        ret.statups.put(SecondaryStat.CygnusElementSkill, new Pair<Integer, Integer>(1, 0));
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), 0));
                        break;
                    }
                    case 13100022:
                    case 13100027:
                    case 13101022:
                    case 13110022:
                    case 13110027:
                    case 13120003:
                    case 13120010: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.TryflingWarm, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 13101024: {
                        ret.statups.put(SecondaryStat.SoulArrow, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.CriticalIncrease, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 13110026: {
                        ret.statups.put(SecondaryStat.IndieEvaR, new Pair<Integer, Integer>(Integer.valueOf(ret.er), ret.duration));
                        ret.statups.put(SecondaryStat.Pdd, new Pair<Integer, Integer>(Integer.valueOf(ret.pddX), ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        break;
                    }
                    case 13111023:
                    case 13120008: {
                        ret.statups.put(SecondaryStat.Albatross, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 13121004: {
                        ret.statups.put(SecondaryStat.IndieEvaR, new Pair<Integer, Integer>(Integer.valueOf(ret.prop), ret.duration));
                        break;
                    }
                    case 13121054: {
                        ret.statups.put(SecondaryStat.StormBringer, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 14001021: {
                        ret.statups.put(SecondaryStat.ElementDarkness, new Pair<Integer, Integer>(1, 0));
                        break;
                    }
                    case 14001027: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ShadowBatt, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 14111024: {
                        ret.statups.put(SecondaryStat.ShadowServant, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 14121054: {
                        ret.statups.put(SecondaryStat.ShadowIllusion, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 15121004: {
                        ret.statups.put(SecondaryStat.ShadowPartner, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 15121005: {
                        ret.statups.put(SecondaryStat.PartyBooster, new Pair<Integer, Integer>(-2, ret.duration));
                        break;
                    }
                    case 15121054: {
                        ret.statups.put(SecondaryStat.StrikerHyperElectric, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 21001003: {
                        ret.statups.put(SecondaryStat.Booster, new Pair<Integer, Integer>(-ret.y, ret.duration));
                        break;
                    }
                    case 21001008: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.BodyPressure, new Pair<Integer, Integer>(Integer.valueOf(ret.damage), ret.duration));
                        break;
                    }
                    case 21101005: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.AranDrain, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 21101006: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.SnowCharge, new Pair<Integer, Integer>(ret.w, ret.duration));
                        break;
                    }
                    case 21111012: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        break;
                    }
                    case 21120022: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.BeyondNextAttackProb, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 21121016: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.BeyondNextAttackProb, new Pair<Integer, Integer>(2, ret.duration));
                        break;
                    }
                    case 21120026: {
                        ret.duration = 10000;
                        ret.statups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 22140013: {
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, ret.duration));
                        break;
                    }
                    case 22171073: {
                        ret.statups.put(SecondaryStat.EnhancedMad, new Pair<Integer, Integer>(Integer.valueOf(ret.emad), ret.duration));
                        ret.statups.put(SecondaryStat.EnhancedPdd, new Pair<Integer, Integer>(Integer.valueOf(ret.epdd), ret.duration));
                        break;
                    }
                    case 23121004: {
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePadR), ret.duration));
                        ret.statups.put(SecondaryStat.EnhancedMaxHp, new Pair<Integer, Integer>(Integer.valueOf(ret.emhp), ret.duration));
                        break;
                    }
                    case 23121054: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 20040216:
                    case 20040217:
                    case 20040219:
                    case 20040220: {
                        ret.duration = 0;
                        break;
                    }
                    case 27101202: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.KeyDownAreaMoving, new Pair<Integer, Integer>(16, ret.duration));
                        break;
                    }
                    case 27111005: {
                        ret.statups.put(SecondaryStat.IndiePdd, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePdd), ret.duration));
                        break;
                    }
                    case 27111006: {
                        ret.statups.put(SecondaryStat.EnhancedMad, new Pair<Integer, Integer>(Integer.valueOf(ret.emad), ret.duration));
                        break;
                    }
                    case 27121006: {
                        ret.statups.put(SecondaryStat.ElementalReset, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 20050286: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.PreReviveOnce, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 25101009: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.HiddenPossession, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 25121030: {
                        ret.statups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 0));
                        break;
                    }
                    case 25121131:
                    case 25121133: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieIgnoreMobpdpR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, ret.duration));
                        break;
                    }
                    case 31101003: {
                        ret.statups.put(SecondaryStat.PowerGaurd, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 31111003: {
                        ret.hpR = ret.x;
                        break;
                    }
                    case 0x1DADAAD: {
                        ret.statups.put(SecondaryStat.NextAttackEnhance, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 31121002: {
                        ret.statups.put(SecondaryStat.DrainHp, new Pair<Integer, Integer>(3, ret.duration));
                        break;
                    }
                    case 31121007: {
                        ret.statups.put(SecondaryStat.InfinityForce, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 31121054: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ShadowPartner, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 31211004: {
                        ret.statups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(ret.getIndieMhpR()), ret.duration));
                        ret.statups.put(SecondaryStat.DiabloicRecovery, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 31221054: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 32001014:
                    case 32100010:
                    case 32110017:
                    case 32120019: {
                        ret.duration = 0;
                        break;
                    }
                    case 32120044: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.TeleportMasteryRange, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 32121010: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Enrage, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.EnrageCr, new Pair<Integer, Integer>(ret.z, ret.duration));
                        ret.statups.put(SecondaryStat.EnrageCrDamMin, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 32121056: {
                        ret.statups.put(SecondaryStat.AttackCountX, new Pair<Integer, Integer>(Integer.valueOf(ret.attackCount), ret.duration));
                        break;
                    }
                    case 33001007:
                    case 33001008:
                    case 33001009:
                    case 33001010:
                    case 33001011:
                    case 33001012:
                    case 33001013:
                    case 33001014:
                    case 33001015: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.JaguarSummoned, new Pair<Integer, Integer>((ret.criticaldamage << 8) + ret.asrR, ret.duration));
                        break;
                    }
                    case 33101003: {
                        ret.statups.put(SecondaryStat.SoulArrow, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        break;
                    }
                    case 33101005: {
                        ret.statups.put(SecondaryStat.HowlingParty, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 33111011: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.DrawBack, new Pair<Integer, Integer>(0, ret.duration));
                        break;
                    }
                    case 33121054: {
                        ret.statups.put(SecondaryStat.FinalAttackProp, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 35001002: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.EnhancedPad, new Pair<Integer, Integer>(Integer.valueOf(ret.epad), ret.duration));
                        ret.statups.put(SecondaryStat.EnhancedPdd, new Pair<Integer, Integer>(Integer.valueOf(ret.epdd), ret.duration));
                        ret.statups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(30, ret.duration));
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, ret.duration));
                        ret.statups.put(SecondaryStat.Mechanic, new Pair<Integer, Integer>(30, ret.duration));
                        break;
                    }
                    case 35101007: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.PerfectArmor, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 35111003: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.EnhancedPad, new Pair<Integer, Integer>(Integer.valueOf(ret.epad), ret.duration));
                        ret.statups.put(SecondaryStat.EnhancedPdd, new Pair<Integer, Integer>(Integer.valueOf(ret.epdd), ret.duration));
                        ret.statups.put(SecondaryStat.CriticalIncrease, new Pair<Integer, Integer>(ret.cr, ret.duration));
                        ret.statups.put(SecondaryStat.Mechanic, new Pair<Integer, Integer>(30, ret.duration));
                        ret.statups.put(SecondaryStat.RideVehicle, new Pair<Integer, Integer>(1932016, ret.duration));
                        break;
                    }
                    case 35121055: {
                        ret.statups.put(SecondaryStat.BombTime, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 36001002: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        break;
                    }
                    case 36001005: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.PinPointRocket, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 36101003: {
                        ret.statups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(ret.getIndieMhpR()), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMpR, new Pair<Integer, Integer>(Integer.valueOf(ret.getIndieMhpR()), ret.duration));
                        break;
                    }
                    case 36111004: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.AegisSystem, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 36111006: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ShadowPartner, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 36121003: {
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        break;
                    }
                    case 36121007: {
                        ret.statups.put(SecondaryStat.OnCapsule, new Pair<Integer, Integer>(20, ret.duration));
                        break;
                    }
                    case 37121054: {
                        ret.statups.put(SecondaryStat.RWMaximizeCannon, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 51101004: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        break;
                    }
                    case 51120003: {
                        ret.statups.put(SecondaryStat.DamageDecreaseWithHP, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 51121006: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Enrage, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.EnrageCrDamMin, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 51121054: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(ret.getIndieMhpR()), ret.duration));
                        break;
                    }
                    case 60001216:
                    case 60001217: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ReshuffleSwitch, new Pair<Integer, Integer>(0, ret.duration));
                        break;
                    }
                    case 61101004: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.Booster, new Pair<Integer, Integer>(-2, ret.duration));
                        break;
                    }
                    case 61101002:
                    case 61110211:
                    case 61120007:
                    case 61121217: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.StopForceAtominfo, new Pair<Integer, Integer>(ret.cooltime / 1000, ret.duration));
                        break;
                    }
                    case 61111003: {
                        ret.statups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(Integer.valueOf(ret.asrR), ret.duration));
                        ret.statups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(Integer.valueOf(ret.terR), ret.duration));
                        break;
                    }
                    case 61111008:
                    case 61120008:
                    case 61121053: {
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        ret.statups.put(SecondaryStat.CriticalIncrease, new Pair<Integer, Integer>(ret.cr, ret.duration));
                        ret.statups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(100, ret.duration));
                        ret.statups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(Integer.valueOf(ret.speed), ret.duration));
                        ret.statups.put(SecondaryStat.Jump, new Pair<Integer, Integer>(Integer.valueOf(ret.jump), ret.duration));
                        break;
                    }
                    case 64001007:
                    case 64001008:
                    case 64001009:
                    case 64001010:
                    case 64001011:
                    case 64001012: {
                        ret.duration = 2000;
                        ret.statups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(10, ret.duration));
                        break;
                    }
                    case 64121053: {
                        ret.statups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 64121054: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCr), ret.duration));
                        break;
                    }
                    case 60011219: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 65001002: {
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-2, ret.duration));
                        break;
                    }
                    case 65121003: {
                        ret.duration = 8000;
                        ret.statups.put(SecondaryStat.SoulResonance, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 65121004: {
                        ret.statups.put(SecondaryStat.SoulGazeCriDamR, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 65121011: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.SoulSeekerExpert, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 65121053: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.CriticalIncrease, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(Integer.valueOf(ret.asrR), ret.duration));
                        ret.statups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(Integer.valueOf(ret.terR), ret.duration));
                        break;
                    }
                    case 65121054: {
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieIgnoreMobpdpR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        ret.statups.put(SecondaryStat.SoulExalt, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 80000169: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.PreReviveOnce, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 80001140: {
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 80001155: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 80001242: {
                        ret.duration = Integer.MAX_VALUE;
                        ret.statups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 80001427: {
                        ret.statups.put(SecondaryStat.IndieJump, new Pair<Integer, Integer>(130, ret.duration));
                        ret.statups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(150, ret.duration));
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-3, ret.duration));
                        break;
                    }
                    case 80001432: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 80001455: {
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePadR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMadR), ret.duration));
                        break;
                    }
                    case 80001456: {
                        ret.statups.put(SecondaryStat.SetBaseDamageByBuff, new Pair<Integer, Integer>(2000000, ret.duration));
                        break;
                    }
                    case 80001457: {
                        ret.statups.put(SecondaryStat.LimitMP, new Pair<Integer, Integer>(500, ret.duration));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        break;
                    }
                    case 80001458: {
                        ret.statups.put(SecondaryStat.MHPCutR, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        break;
                    }
                    case 80001459: {
                        ret.statups.put(SecondaryStat.MMPCutR, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieIgnoreMobpdpR), ret.duration));
                        break;
                    }
                    case 80001460: {
                        ret.statups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMhpR), ret.duration));
                        break;
                    }
                    case 80001461: {
                        ret.statups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 80001757: {
                        ret.statups.put(SecondaryStat.IndieJump, new Pair<Integer, Integer>(100, ret.duration));
                        ret.statups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(100, ret.duration));
                        ret.statups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.Inflation, new Pair<Integer, Integer>(500, ret.duration));
                        break;
                    }
                    case 80002404: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.DebuffIncHp, new Pair<Integer, Integer>(50, ret.duration));
                        break;
                    }
                    case 80002280: {
                        ret.duration = 180000;
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 80002281: {
                        ret.statups.put(SecondaryStat.RuneOfGreed, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 80002282: {
                        ret.duration = 900000;
                        ret.statups.put(SecondaryStat.CooldownRune, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 80002888: {
                        ret.statups.put(SecondaryStat.RuneOfPure, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 80002890: {
                        ret.statups.put(SecondaryStat.RuneOfTransition, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 80002544: {
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        break;
                    }
                    case 91001022: {
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        break;
                    }
                    case 91001023: {
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieIgnoreMobpdpR), ret.duration));
                        break;
                    }
                    case 91001024: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 91001025: {
                        ret.statups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCD), ret.duration));
                        break;
                    }
                    case 131001000:
                    case 131001001:
                    case 131001002:
                    case 131001003: {
                        ret.statups.put(SecondaryStat.PinkbeanAttackBuff, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 131001015: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.PinkbeanMinibeenMove, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 131001021: {
                        ret.statups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 131001106: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(50, ret.duration));
                        break;
                    }
                    case 131001206: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(50, ret.duration));
                        ret.statups.put(SecondaryStat.DotHealHPPerSecond, new Pair<Integer, Integer>(Integer.valueOf(ret.dotHealHPPerSecondR), ret.duration));
                        break;
                    }
                    case 131001306: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(50, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePadR), ret.duration));
                        break;
                    }
                    case 131001406: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(50, ret.duration));
                        ret.statups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAsrR), ret.duration));
                        break;
                    }
                    case 131001506: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(50, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePadR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAsrR), ret.duration));
                        break;
                    }
                    case 142001003: {
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBooster), ret.duration));
                        break;
                    }
                    case 142001007: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.KinesisPsychicEnergeShield, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 142111010: {
                        ret.statups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, 900));
                        break;
                    }
                    case 142121032: {
                        ret.statups.put(SecondaryStat.KinesisPsychicOver, new Pair<Integer, Integer>(50, ret.duration));
                        break;
                    }
                    case 151101006: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Creation, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 151101013: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Wonder, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 151111005: {
                        ret.statups.put(SecondaryStat.Novility, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 151121001: {
                        ret.duration = 30000;
                        ret.statups.put(SecondaryStat.Grave, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 155101008: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.ComingDeath, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400001003: {
                        ret.statups.put(SecondaryStat.MaxHP, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.MaxMP, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 400001004: {
                        ret.statups.put(SecondaryStat.CombatOrders, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400001005: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(ret.y, ret.duration));
                        ret.statups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMhp), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMp, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMmp), ret.duration));
                        break;
                    }
                    case 400001006: {
                        ret.statups.put(SecondaryStat.PartyBooster, new Pair<Integer, Integer>(-1, ret.duration));
                        break;
                    }
                    case 400001010: {
                        ret.statups.put(SecondaryStat.HiddenPossession, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 400001020: {
                        ret.statups.put(SecondaryStat.HolySymbol, new Pair<Integer, Integer>(ret.x, ret.duration));
                        ret.statups.put(SecondaryStat.DropRate, new Pair<Integer, Integer>(ret.v, ret.duration));
                        break;
                    }
                    case 400001023: {
                        ret.statups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 400001025: {
                        ret.statups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCooltimeReduce), ret.duration));
                        ret.statups.put(SecondaryStat.FreudsProtection, new Pair<Integer, Integer>(sourceid - 400001024, ret.duration));
                        break;
                    }
                    case 400001026: {
                        ret.statups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCooltimeReduce), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.FreudsProtection, new Pair<Integer, Integer>(sourceid - 400001024, ret.duration));
                        break;
                    }
                    case 400001027: {
                        ret.statups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCooltimeReduce), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAllStat), ret.duration));
                        ret.statups.put(SecondaryStat.FreudsProtection, new Pair<Integer, Integer>(sourceid - 400001024, ret.duration));
                        break;
                    }
                    case 400001028: {
                        ret.statups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCooltimeReduce), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAllStat), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.FreudsProtection, new Pair<Integer, Integer>(sourceid - 400001024, ret.duration));
                        break;
                    }
                    case 400001029: {
                        ret.statups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCooltimeReduce), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAllStat), ret.duration));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.FreudsProtection, new Pair<Integer, Integer>(sourceid - 400001024, ret.duration));
                        break;
                    }
                    case 400001030: {
                        ret.statups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCooltimeReduce), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(Integer.valueOf(ret.indieAllStat), ret.duration));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.FreudsProtection, new Pair<Integer, Integer>(sourceid - 400001024, ret.duration));
                        break;
                    }
                    case 400001037: {
                        ret.statups.put(SecondaryStat.MagicCircuitFullDrive, new Pair<Integer, Integer>(ret.y, ret.duration));
                        break;
                    }
                    case 400001043: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(ret.q, ret.duration));
                        ret.statups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400001044: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(ret.q, ret.duration));
                        ret.statups.put(SecondaryStat.IndieDamageReduce, new Pair<Integer, Integer>(ret.z, ret.duration));
                        ret.statups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400001047: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400001048: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePad), ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(ret.indieMad), ret.duration));
                        ret.statups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400001049: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 400011000: {
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieIgnoreMobpdpR), ret.duration));
                        ret.statups.put(SecondaryStat.AuraWeapon, new Pair<Integer, Integer>(ret.z, ret.duration));
                        break;
                    }
                    case 400011015: {
                        ret.statups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(ret.q, ret.duration));
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-2, ret.duration));
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        break;
                    }
                    case 400011017: {
                        ret.statups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400011039: {
                        ret.duration = 5000;
                        ret.statups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 5000));
                        ret.statups.put(SecondaryStat.IndieJointAttack, new Pair<Integer, Integer>(1, 5000));
                        break;
                    }
                    case 400011055: {
                        ret.statups.put(SecondaryStat.Ellision, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400011066: {
                        ret.statups.put(SecondaryStat.IndieSuperStance, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(ret.asrR), ret.duration));
                        ret.statups.put(SecondaryStat.BodyOfSteal, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400011072: {
                        ret.duration = 10000;
                        ret.statups.put(SecondaryStat.GrandCrossSize, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(-60, ret.duration));
                        break;
                    }
                    case 400011073: {
                        ret.statups.put(SecondaryStat.ComboInstict, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400011109: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(ret.y, ret.duration));
                        ret.statups.put(SecondaryStat.Restore, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400021000: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.OverloadMana, new Pair<Integer, Integer>(ret.z, ret.duration));
                        break;
                    }
                    case 400021003: {
                        ret.statups.put(SecondaryStat.Pray, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400021060: {
                        ret.statups.put(SecondaryStat.Etherealform, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndieShotDamage, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400031000: {
                        ret.statups.put(SecondaryStat.GuidedArrow, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400031002: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 400031015: {
                        ret.statups.put(SecondaryStat.SplitArrow, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400031017: {
                        ret.statups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(ret.indieStance), ret.duration));
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePadR), ret.duration));
                        ret.statups.put(SecondaryStat.IndieDamageReduce, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamReduceR), ret.duration));
                        break;
                    }
                    case 400031020: {
                        ret.statups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400031021: {
                        ret.statups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(2, ret.duration));
                        break;
                    }
                    case 400031023: {
                        ret.statups.put(SecondaryStat.CriticalReinForce, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 80003016: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.EventSpecialSkill, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400041001: {
                        ret.statups.put(SecondaryStat.SpreadThrow, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400041029: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.Overload, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 400041035: {
                        ret.statups.put(SecondaryStat.ChainArtsFury, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400041052: {
                        ret.statups.put(SecondaryStat.SageWrathOfGods, new Pair<Integer, Integer>(1, ret.duration));
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 400051006: {
                        ret.statups.put(SecondaryStat.BulletParty, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400051007: {
                        ret.statups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(ret.indiePmdR), ret.duration));
                        ret.statups.put(SecondaryStat.Striker1st, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400051009: {
                        ret.statups.put(SecondaryStat.MultipleOption, new Pair<Integer, Integer>(ret.q2, ret.duration));
                        break;
                    }
                    case 400051015: {
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.SerpentScrew, new Pair<Integer, Integer>(ret.damage / 10, ret.duration));
                        break;
                    }
                    case 400051018: {
                        ret.statups.put(SecondaryStat.Spotlight, new Pair<Integer, Integer>(Integer.valueOf(ret.level), ret.duration));
                        break;
                    }
                    case 400051033: {
                        ret.statups.put(SecondaryStat.OverDrive, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 400051036: {
                        ret.statups.put(SecondaryStat.InfinitySpell, new Pair<Integer, Integer>(ret.x, ret.duration));
                        break;
                    }
                    case 400007000:
                    case 400007001:
                    case 400007002:
                    case 400007009:
                    case 400007010: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieDamR), ret.duration));
                        break;
                    }
                    case 400007003: {
                        ret.statups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCr), ret.duration));
                        break;
                    }
                    case 400007004: {
                        ret.statups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(Integer.valueOf(ret.indieCD), ret.duration));
                        break;
                    }
                    case 400007005: {
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieIgnoreMobpdpR), ret.duration));
                        break;
                    }
                    case 400007007:
                    case 400007011: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(Integer.valueOf(ret.IndieExp), ret.duration));
                        break;
                    }
                    case 400007006: {
                        ret.statups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 400007008: {
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(ret.indieBDR), ret.duration));
                        break;
                    }
                }
                if (GameConstants.isBeginnerJob(sourceid / 10000)) {
                    switch (sourceid % 10000) {
                        case 1001: {
                            if (sourceid / 10000 == 3001 || sourceid / 10000 == 3000) {
                                ret.statups.put(SecondaryStat.Infiltrate, new Pair<Integer, Integer>(ret.x, ret.duration));
                                break;
                            }
                            ret.statups.put(SecondaryStat.Recovery, new Pair<Integer, Integer>(ret.x, ret.duration));
                            break;
                        }
                        case 1002: {
                            ret.statups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(ret.x, ret.duration));
                            break;
                        }
                        case 8000: {
                            ret.statups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(Integer.valueOf(ret.speed), ret.duration));
                            ret.statups.put(SecondaryStat.Jump, new Pair<Integer, Integer>(Integer.valueOf(ret.jump), ret.duration));
                            break;
                        }
                        case 8002: {
                            ret.statups.put(SecondaryStat.SharpEyes, new Pair<Integer, Integer>(2568, ret.duration));
                            break;
                        }
                        case 8003: {
                            ret.statups.put(SecondaryStat.MaxHP, new Pair<Integer, Integer>(40, ret.duration));
                            ret.statups.put(SecondaryStat.MaxMP, new Pair<Integer, Integer>(40, ret.duration));
                            break;
                        }
                        case 8004: {
                            ret.statups.put(SecondaryStat.CombatOrders, new Pair<Integer, Integer>(1, ret.duration));
                            break;
                        }
                        case 8005: {
                            ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(20, ret.duration));
                            ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(20, ret.duration));
                            ret.statups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(475, ret.duration));
                            ret.statups.put(SecondaryStat.IndieMp, new Pair<Integer, Integer>(475, ret.duration));
                            break;
                        }
                        case 8006: {
                            ret.statups.put(SecondaryStat.PartyBooster, new Pair<Integer, Integer>(-1, ret.duration));
                        }
                    }
                } else if (sourceid < 400000000) {
                    switch (sourceid % 10000) {
                        case 1085:
                        case 1087:
                        case 1090:
                        case 1179: {
                            ret.duration = 0;
                        }
                    }
                }
            } else {
                switch (sourceid) {
                    case 2022125: {
                        ret.statups.put(SecondaryStat.Pdd, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2022126: {
                        ret.statups.put(SecondaryStat.Pdd, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2022127: {
                        ret.statups.put(SecondaryStat.Acc, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2022128: {
                        ret.statups.put(SecondaryStat.Eva, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2022129: {
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2022747: {
                        ret.statups.clear();
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(10, ret.duration));
                        ret.statups.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(10, ret.duration));
                        ret.statups.put(SecondaryStat.RepeatEffect, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2022746:
                    case 2022764: {
                        ret.statups.clear();
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(5, ret.duration));
                        ret.statups.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(5, ret.duration));
                        ret.statups.put(SecondaryStat.RepeatEffect, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2022823: {
                        ret.statups.clear();
                        ret.duration = 0;
                        ret.statups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(12, ret.duration));
                        ret.statups.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(12, ret.duration));
                        ret.statups.put(SecondaryStat.RepeatEffect, new Pair<Integer, Integer>(1, ret.duration));
                        break;
                    }
                    case 2003516:
                    case 2003517:
                    case 2003518:
                    case 2003519:
                    case 2003520:
                    case 2003552:
                    case 2003553:
                    case 2003561:
                    case 2003566:
                    case 2003568:
                    case 2003570:
                    case 2003571:
                    case 2003572:
                    case 2003576:
                    case 2003591: {
                        ret.statups.put(SecondaryStat.Inflation, new Pair<Integer, Integer>(Integer.valueOf(ret.inflation), ret.duration));
                        break;
                    }
                    case 2450064: {
                        ret.statups.put(SecondaryStat.ExpBuffRate, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 2450134: {
                        ret.statups.put(SecondaryStat.ExpBuffRate, new Pair<Integer, Integer>(200, ret.duration));
                        break;
                    }
                    case 2023072: {
                        ret.statups.put(SecondaryStat.ItemUpByItem, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 2450054: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(300, ret.duration));
                        break;
                    }
                    case 2450124: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(150, ret.duration));
                        break;
                    }
                    case 2002093: {
                        ret.hpR = 100.0;
                        ret.mpR = 100.0;
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(30, ret.duration));
                        break;
                    }
                    case 2003550: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(10, ret.duration));
                        break;
                    }
                    case 2003551: {
                        ret.statups.put(SecondaryStat.DropItemRate, new Pair<Integer, Integer>(20, ret.duration));
                        break;
                    }
                    case 2003596: {
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(20, ret.duration));
                        break;
                    }
                    case 2003597: {
                        ret.statups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(10, ret.duration));
                        break;
                    }
                    case 2003598: {
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(20, ret.duration));
                        break;
                    }
                    case 2003599: {
                        ret.statups.put(SecondaryStat.IndieAllStatR, new Pair<Integer, Integer>(10, ret.duration));
                        break;
                    }
                    case 2023520: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(30, ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(30, ret.duration));
                        break;
                    }
                    case 2023553: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(30, ret.duration));
                        break;
                    }
                    case 2023554: {
                        ret.statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, ret.duration));
                        break;
                    }
                    case 2023555: {
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(30, ret.duration));
                        break;
                    }
                    case 2023556: {
                        ret.statups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(2000, ret.duration));
                        ret.statups.put(SecondaryStat.IndieMp, new Pair<Integer, Integer>(2000, ret.duration));
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(10, ret.duration));
                        break;
                    }
                    case 2023558: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(50, ret.duration));
                        break;
                    }
                    case 2004019: // 향상된 힘의 물약 10단계
                        ret.statups.put(SecondaryStat.IndieStr, new Pair<>(30, ret.duration));
                        break;
                    case 2004039: // 향상된 민첩의 물약 10단계
                        ret.statups.put(SecondaryStat.IndieDex, new Pair<>(30, ret.duration));
                        break;
                    case 2004059: // 향상된 지능의 물약 10단계
                        ret.statups.put(SecondaryStat.IndieInt, new Pair<>(30, ret.duration));
                        break;
                    case 2004079: // 향상된 운의 물약 10단계
                        ret.statups.put(SecondaryStat.IndieLuk, new Pair<>(30, ret.duration));
                        break;
                    case 2023658:
                    case 2023659:
                    case 2023660: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(30, ret.duration));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(30, ret.duration));
                        break;
                    }
                    case 2023661:
                    case 2023662:
                    case 2023663: {
                        ret.statups.put(SecondaryStat.ItemUpByItem, new Pair<Integer, Integer>(50, ret.duration));
                        break;
                    }
                    case 2023664:
                    case 2023665:
                    case 2023666: {
                        ret.statups.put(SecondaryStat.WealthOfUnion, new Pair<Integer, Integer>(50, ret.duration));
                        break;
                    }
                    case 2450147:
                    case 2450148:
                    case 2450149: {
                        ret.statups.put(SecondaryStat.ExpBuffRate, new Pair<Integer, Integer>(100, ret.duration));
                        break;
                    }
                    case 2024011:
                    case 2024017: {
                        ret.statups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(15, 1800000));
                        break;
                    }
                    case 2024012:
                    case 2024018: {
                        ret.statups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(15, 1800000));
                        ret.statups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(1500, 1800000));
                        ret.statups.put(SecondaryStat.IndieMp, new Pair<Integer, Integer>(1500, 1800000));
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(15, 1800000));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(15, 1800000));
                        ret.statups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(15, 1800000));
                        ret.statups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(15, 1800000));
                        break;
                    }
                    case 2023300: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(20, 1800000));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(20, 1800000));
                        break;
                    }
                    case 2023912: {
                        ret.statups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(30, 1800000));
                        ret.statups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(30, 1800000));
                    }
                }
            }
            if (ret.isMorph()) {
                ret.statups.put(SecondaryStat.Morph, new Pair<Integer, Integer>(ret.morphId, ret.duration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public final boolean applyTo(MapleCharacter chr) {
        return this.applyTo(chr, chr, true, chr.getTruePosition(), this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyToBuff(MapleCharacter chr) {
        return this.applyTo(chr, chr, true, chr.getTruePosition(), this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), true);
    }

    public final boolean applyToBuff(MapleCharacter chr, int duration) {
        return this.applyTo(chr, chr, true, chr.getTruePosition(), duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter applyfrom, MapleCharacter applyto) {
        return this.applyTo(applyfrom, applyto, true, applyto.getTruePosition(), this.duration, (byte) (applyfrom.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter applyfrom, MapleCharacter applyto, boolean primary) {
        return this.applyTo(applyfrom, applyto, primary, applyto.getTruePosition(), this.duration, (byte) (applyfrom.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter applyfrom, MapleCharacter applyto, boolean primary, int duration) {
        return this.applyTo(applyfrom, applyto, primary, applyto.getTruePosition(), duration, (byte) (applyfrom.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, boolean primary) {
        return this.applyTo(chr, chr, primary, chr.getTruePosition(), this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, boolean primary, boolean showEffect) {
        return this.applyTo(chr, chr, primary, chr.getTruePosition(), this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), showEffect);
    }

    public final boolean applyTo(MapleCharacter chr, int duration) {
        return this.applyTo(chr, chr, true, chr.getTruePosition(), duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, boolean primary, int duration) {
        return this.applyTo(chr, chr, primary, chr.getTruePosition(), duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, boolean primary, int duration, boolean showEffect) {
        return this.applyTo(chr, chr, primary, chr.getTruePosition(), duration, (byte) (chr.isFacingLeft() ? 1 : 0), showEffect);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos, boolean primary, int duration) {
        return this.applyTo(chr, chr, primary, pos, duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos, boolean primary) {
        return this.applyTo(chr, chr, primary, pos, this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos, boolean primary, boolean showEffect) {
        return this.applyTo(chr, chr, primary, pos, this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), showEffect);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos) {
        return this.applyTo(chr, chr, true, pos, this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, boolean primary, Point pos) {
        return this.applyTo(chr, chr, primary, pos, this.duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos, int duration) {
        return this.applyTo(chr, chr, true, pos, duration, (byte) (chr.isFacingLeft() ? 1 : 0), false);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos, byte rltype) {
        return this.applyTo(chr, chr, true, pos, this.duration, rltype, false);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos, byte rltype, boolean showEffect) {
        return this.applyTo(chr, chr, true, pos, this.duration, rltype, showEffect);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos, boolean primary, byte rltype) {
        return this.applyTo(chr, chr, primary, pos, this.duration, rltype, false);
    }

    public final boolean applyTo(MapleCharacter applyfrom, MapleCharacter applyto, boolean primary, Point pos, int localDuration, byte rltype, boolean showEffect) {
        block196:
        {
            block197:
            {
                block195:
                {
                    SummonMovementType summonMovementType;
                    if (applyfrom.getMapId() == ServerConstants.warpMap && this.skill && !applyfrom.isGM() && (this.getSummonMovementType() != null || this.isMist())) {
                        applyfrom.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                        return false;
                    }
                    if (this.isHeal() && (applyfrom.getMapId() == 749040100 || applyto.getMapId() == 749040100)) {
                        applyfrom.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                        return false;
                    }
                    if (this.sourceid == 4341006 && applyfrom.getBuffedValue(SecondaryStat.ShadowPartner) == null) {
                        applyfrom.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                        return false;
                    }
                    if (!(this.sourceid != 33101008 || applyfrom.getBuffedValue(SecondaryStat.IndieSummon) == null && applyfrom.canSummon())) {
                        applyfrom.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                        return false;
                    }
                    if (this.sourceid == 33101004 && applyfrom.getMap().isTown()) {
                        applyfrom.dropMessage(5, "You may not use this skill in towns.");
                        applyfrom.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                        return false;
                    }
                    EnumMap<MapleStat, Long> hpmpupdate = new EnumMap<MapleStat, Long>(MapleStat.class);
                    long hpchange = this.calcHPChange(applyfrom, primary);
                    long mpchange = this.calcMPChange(applyfrom, this.sourceid == 36101001 ? true : primary);
                    int powerchange = this.calcPowerChange(applyfrom, primary);
                    PlayerStats stat = applyto.getStat();
                    boolean noCon = false;
                    if (this.sourceid == 400011010 || this.sourceid == 400021006) {
                        if (applyto.getBuffedValue(this.sourceid) && !primary) {
                            noCon = true;
                        }
                    } else if (this.sourceid == 400011055) {
                        noCon = true;
                    } else if (this.sourceid == 36101001) {
                        primary = true;
                    }
                    if (applyto.getSkillLevel(4110012) > 0 && localDuration < 0 && applyto.getSkillCustomValue0(4110012) > 0L) {
                        applyto.removeSkillCustomInfo(4110012);
                        mpchange = 1L;
                    }
                    if (primary && this.itemConNo != 0 && !applyto.inPVP()) {
                        if (!applyto.haveItem(this.itemCon, this.itemConNo, false, true)) {
                            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                            return false;
                        }
                        MapleInventoryManipulator.removeById(applyto.getClient(), GameConstants.getInventoryType(this.itemCon), this.itemCon, this.itemConNo, false, true);
                    }
                    if (this.isResurrection() && applyto.getId() != applyfrom.getId() && primary) {
                        hpchange = stat.getCurrentMaxHp();
                        applyto.setStance(0);
                        stat.setHp(stat.getHp() + hpchange, applyto);
                        hpmpupdate.put(MapleStat.HP, stat.getHp());
                        SkillFactory.getSkill(2321006).getEffect(applyfrom.getSkillLevel(2321006)).applyTo(applyfrom, applyto, false);
                        SkillFactory.getSkill(2321006).getEffect(applyfrom.getSkillLevel(2321006)).applyTo(applyfrom, false);
                    }
                    if (this.isDispel() && this.makeChanceResult() || this.sourceid == 2001556) {
                        applyto.dispelDebuffs(applyfrom, this.sourceid);
                    } else if (this.isHeroWill() || this.sourceid == 80001478 && this.makeChanceResult()) {
                        applyto.dispelDebuffs();
                        if (this.isHeroWill()) {
                            applyto.cancelAllDebuffs();
                        }
                    } else if (this.isMPRecovery()) {
                        long toDecreaseHP = stat.getMaxHp() / 100L * 10L;
                        if (stat.getHp() > toDecreaseHP) {
                            hpchange += -toDecreaseHP;
                            mpchange += toDecreaseHP / 100L * (long) this.getY();
                        } else {
                            long l = hpchange = stat.getHp() == 1L ? 0L : stat.getHp() - 1L;
                        }
                    }
                    if (applyfrom.getId() != applyto.getId()) {
                        mpchange = 0L;
                        if (!this.isHeal() && !this.isResurrection()) {
                            hpchange = 0L;
                        }
                    }
                    if (GameConstants.isZero(applyto.getJob()) && this.sourceid == 1221054) {
                        noCon = true;
                    }
                    if (hpchange != 0L && applyto.isAlive() && !noCon) {
                        if (hpchange < 0L && -hpchange > stat.getHp() && !applyto.hasDisease(SecondaryStat.Undead)) {
                            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                            return false;
                        }
                        if (this.skill) {
                            if (stat.getHp() + hpchange > 0L) {
                                stat.setHp(stat.getHp() + hpchange, applyto);
                            }
                        } else if (stat.getHp() + hpchange <= 0L) {
                            stat.setHp(1L, applyto);
                        } else {
                            stat.setHp(stat.getHp() + hpchange, applyto);
                        }
                        hpmpupdate.put(MapleStat.HP, stat.getHp());
                    }
                    if (mpchange != 0L && applyto.isAlive() && !noCon && applyto.getBattleGroundChr() == null) {
                        if (mpchange < 0L && -mpchange > stat.getMp()) {
                            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                            return false;
                        }
                        if (mpchange < 0L && GameConstants.isDemonSlayer(applyto.getJob()) || !GameConstants.isDemonSlayer(applyto.getJob())) {
                            stat.setMp(stat.getMp() + mpchange, applyto);
                        }
                        hpmpupdate.put(MapleStat.MP, stat.getMp());
                    }
                    if (applyto.getMapId() == 993192600 && this.skill) {
                        return false;
                    }
                    if (primary || applyfrom.getId() != applyto.getId()) {
                        applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(hpmpupdate, !this.skill, false, applyto, false));
                    }
                    if (!applyto.isAlive() && this.sourceid != 1320019) {
                        return false;
                    }
                    if (powerchange != 0) {
                        if (applyto.getXenonSurplus() - powerchange < 0) {
                            return false;
                        }
                        applyto.gainXenonSurplus((short) (-powerchange), SkillFactory.getSkill(this.getSourceId()));
                    }
                    if (this.expinc != 0) {
                        applyto.gainExp(this.expinc, true, true, false);
                        applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showNormalEffect(applyto, 24, true));
                    } else if (this.isReturnScroll()) {
                        this.applyReturnScroll(applyto);
                    } else if (this.useLevel > 0 && !this.skill) {
                        applyto.setExtractor(new MapleExtractor(applyto, this.sourceid, this.useLevel * 50, 1440));
                        applyto.getMap().spawnExtractor(applyto.getExtractor());
                    } else if (this.cosmetic > 0) {
                        if (this.cosmetic >= 30000) {
                            applyto.setHair(this.cosmetic);
                            applyto.updateSingleStat(MapleStat.HAIR, this.cosmetic);
                        } else if (this.cosmetic >= 20000) {
                            applyto.setFace(this.cosmetic);
                            applyto.updateSingleStat(MapleStat.FACE, this.cosmetic);
                        } else if (this.cosmetic < 100) {
                            applyto.setSkinColor((byte) this.cosmetic);
                            applyto.updateSingleStat(MapleStat.SKIN, this.cosmetic);
                        }
                        applyto.equipChanged();
                    } else if (this.recipe > 0) {
                        if (applyto.getSkillLevel(this.recipe) > 0 || applyto.getProfessionLevel(this.recipe / 10000 * 10000) < this.reqSkillLevel) {
                            return false;
                        }
                        applyto.changeSingleSkillLevel(SkillFactory.getCraft(this.recipe), Integer.MAX_VALUE, this.recipeUseCount, this.recipeValidDay > 0 ? System.currentTimeMillis() + (long) this.recipeValidDay * 24L * 60L * 60L * 1000L : -1L);
                    }
                    for (Map.Entry<MapleTrait.MapleTraitType, Integer> t : this.traits.entrySet()) {
                        applyto.getTrait(t.getKey()).addExp(t.getValue(), applyto);
                    }
                    if (this.sourceid == 2121003 && !applyto.getPosionNovas().isEmpty()) {
                        applyto.getClient().getSession().writeAndFlush((Object) CField.poisonNova(applyto, applyto.getPosionNovas()));
                        applyto.setPosionNovas(new ArrayList<Integer>());
                    } else if ((this.sourceid == 12120013 || this.sourceid == 12120014) && primary) {
                        if (applyfrom.getBuffedValue(12120013)) {
                            applyfrom.cancelEffect(applyfrom.getBuffedEffect(12120013));
                        }
                        if (applyfrom.getBuffedValue(12120014)) {
                            applyfrom.cancelEffect(applyfrom.getBuffedEffect(12120014));
                        }
                    }
                    if ((this.sourceid == 12120013 || this.sourceid == 12120014) && primary) {
                        if (applyfrom.getBuffedValue(12120013)) {
                            applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 12120013);
                        }
                        if (applyfrom.getBuffedValue(12120014)) {
                            applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 12120014);
                        }
                    }
                    if ((summonMovementType = this.getSummonMovementType()) != null && ((this.sourceid == 400021071 || this.sourceid == 35111008 || this.sourceid == 35120002 || this.sourceid == 400021092) && primary || this.sourceid != 400021071 && this.sourceid != 35111008 && this.sourceid != 35120002 && this.sourceid != 400021092)) {
                        if (this.sourceid == 400011001) {
                            MapleSummon summon = applyto.getSummon(400011001);
                            if (summon != null) {
                                summon.setSkill(400011002);
                                summon.setMovementType(SummonMovementType.STATIONARY);
                                applyto.getMap().broadcastMessage(CField.SummonPacket.removeSummon(summon, true));
                                applyto.getMap().broadcastMessage(CField.SummonPacket.spawnSummon(summon, true, summon.getDuration()));
                                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, true, false));
                                return true;
                            }
                            summon = applyto.getSummon(400011002);
                            if (summon != null) {
                                summon.setSkill(400011001);
                                summon.setMovementType(SummonMovementType.FOLLOW);
                                applyto.getMap().broadcastMessage(CField.SummonPacket.removeSummon(summon, true));
                                applyto.getMap().broadcastMessage(CField.SummonPacket.spawnSummon(summon, true, summon.getDuration()));
                                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, true, false));
                                return true;
                            }
                        } else if (this.sourceid != 35111002 && this.sourceid != 400021047 && this.sourceid != 400011065 && this.sourceid != 14000027 && this.sourceid != 14100027 && this.sourceid != 0xD74D4D && this.sourceid != 14120008 && this.sourceid != 151100002 && primary) {
                            if (applyto.getBuffedValue(this.sourceid)) {
                                boolean cancels = true;
                                if (applyto.getBuffedEffect(SecondaryStat.SoulMP) != null && applyto.getBuffedEffect(SecondaryStat.SoulMP).getSourceId() == this.sourceid) {
                                    cancels = false;
                                }
                                if (cancels) {
                                    applyto.cancelEffect(this);
                                }
                            }
                            ArrayList<MapleSummon> toRemove = new ArrayList<MapleSummon>();
                            int delcount = 1;
                            int counting = 0;
                            switch (this.sourceid) {
                                case 14121003: {
                                    delcount = 2;
                                    break;
                                }
                                case 162101012:
                                case 400021071: {
                                    delcount = 4;
                                }
                            }
                            if (!applyto.getSummons().isEmpty()) {
                                for (MapleSummon summon : applyto.getSummons()) {
                                    if (summon.getSkill() != this.sourceid || delcount > ++counting) {
                                        continue;
                                    }
                                    toRemove.add(summon);
                                }
                            }
                            for (MapleSummon summon : toRemove) {
                                summon.removeSummon(applyto.getMap(), false);
                            }
                            if (this.sourceid == 35111008) {
                                applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 35111008);
                                applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 35120002);
                            }
                        }
                        boolean spawn = true;
                        boolean aftercancel = false;
                        if (this.sourceid == 400051011) {
                            spawn = showEffect;
                        } else if (this.sourceid == 400031051) {
                            spawn = !primary;
                        } else if (this.sourceid == 400021071 || this.sourceid == 36121014) {
                            spawn = primary;
                        } else if (this.sourceid == 400011055) {
                            if (applyfrom.getBuffedValue(this.sourceid)) {
                                spawn = false;
                            }
                        } else if (GameConstants.isAfterRemoveSummonSkill(this.sourceid)) {
                            aftercancel = true;
                            if (applyfrom.getBuffedValue(this.sourceid)) {
                                spawn = false;
                            }
                        }
                        if (spawn) {
                            Skill elite;
                            MapleSummon summon;
                            int summId = this.sourceid;
                            applyto.dropMessageGM(-8, "summon sourceId : " + summId);
                            if (this.sourceid == 3111002 ? applyfrom.getTotalSkillLevel(elite = SkillFactory.getSkill(3120012)) > 0 : this.sourceid == 3211002 && applyfrom.getTotalSkillLevel(elite = SkillFactory.getSkill(3220012)) > 0) {
                                return elite.getEffect(applyfrom.getTotalSkillLevel(elite)).applyTo(applyfrom, applyto, primary, pos, localDuration, rltype, showEffect);
                            }
                            if (this.sourceid == 400011012 || this.sourceid == 400011013 || this.sourceid == 400011014) {
                                pos = applyfrom.getTruePosition();
                            } else if ((this.sourceid == 36121002 || this.sourceid == 36121013 || this.sourceid == 36121014 || this.sourceid == 400041044 || this.sourceid == 400041047) && applyto.getSkillLevel(36120051) > 0) {
                                localDuration += SkillFactory.getSkill(36120051).getEffect(applyto.getSkillLevel(36120051)).getDuration();
                            }
                            if (summId == 400001039) {
                                summId = 400001040;
                                localDuration = SkillFactory.getSkill(summId).getEffect(this.level).getDuration();
                            } else if (summId == 400001059) {
                                summId = 400001060;
                                localDuration = SkillFactory.getSkill(summId).getEffect(this.level).getDuration();
                            } else if (summId == 152101000) {
                                localDuration = Integer.MAX_VALUE;
                            }
                            if (this.sourceid == 5321004) {
                                pos = new Point(pos.x + (applyto.isFacingLeft() ? -45 : 45), pos.y);
                            }
                            MapleSummon tosummon = new MapleSummon(applyfrom, summId, new Point(pos == null ? applyfrom.getTruePosition() : pos), summonMovementType, rltype, aftercancel ? Integer.MAX_VALUE : localDuration);
                            switch (this.sourceid) {
                                case 5201012:
                                case 5201013:
                                case 5201014:
                                case 5210015:
                                case 5210016:
                                case 5210017:
                                case 5210018: {
                                    tosummon.setDebuffshell(1);
                                    break;
                                }
                                case 151111001: {
                                    if (applyto.getSkillLevel(151120035) <= 0) {
                                        break;
                                    }
                                    localDuration += SkillFactory.getSkill((int) 151120035).getEffect((int) 1).duration;
                                    break;
                                }
                                case 400051046: {
                                    applyfrom.setSkillCustomInfo(this.sourceid, 0L, 3000L);
                                }
                            }
                            if (localDuration != 0 && localDuration < 2100000000 && this.sourceid < 400000000 && this.sourceid != 35111002) {
                                localDuration = this.alchemistModifyVal(applyfrom, localDuration, false);
                            }
                            applyfrom.dropMessageGM(-8, "spawn summon : " + summId + " / duration : " + (aftercancel ? Integer.MAX_VALUE : localDuration) + " / pos : " + tosummon.getTruePosition());
                            applyfrom.getMap().spawnSummon(tosummon, aftercancel ? Integer.MAX_VALUE : localDuration);
                            applyfrom.addSummon(tosummon);
                            tosummon.addHP(this.x);
                            if (this.sourceid == 131003023 || this.sourceid == 131004023 || this.sourceid == 131005023 || this.sourceid == 131006023) {
                                tosummon.setLastAttackTime(System.currentTimeMillis());
                            }
                            if (this.sourceid == 12111022) {
                                applyfrom.maelstrom = 0;
                            } else if (this.sourceid == 400041028) {
                                applyto.getClient().getSession().writeAndFlush((Object) CField.ShadowServentRefresh(applyto, tosummon, 3));
                            } else if (this.sourceid == 5220025 || this.sourceid == 5220024 || this.sourceid == 5220023 || this.sourceid == 5221022) {
                                ArrayList<Pair<Integer, Integer>> suminfo = new ArrayList<Pair<Integer, Integer>>();
                                int size = 0;
                                for (MapleSummon sum : applyfrom.getMap().getAllSummonsThreadsafe()) {
                                    if (sum == null || sum.getOwner().getId() != applyfrom.getId() || (sum.getSkill() < 5220023 || sum.getSkill() > 5220025) && sum.getSkill() != 5221022) {
                                        continue;
                                    }
                                    ++size;
                                    suminfo.add(new Pair<Integer, Integer>(sum.getSkill(), (int) applyfrom.getBuffLimit(sum.getSkill())));
                                }
                                if (size >= 2) {
                                    int B_duration;
                                    int A_duration = (Integer) ((Pair) suminfo.get(0)).getRight();
                                    if (A_duration > (B_duration = ((Integer) ((Pair) suminfo.get(1)).getRight()).intValue())) {
                                        while (applyfrom.getBuffedValue((Integer) ((Pair) suminfo.get(1)).getLeft())) {
                                            applyfrom.cancelEffect(applyfrom.getBuffedEffect((Integer) ((Pair) suminfo.get(1)).getLeft()));
                                        }
                                    } else {
                                        while (applyfrom.getBuffedValue((Integer) ((Pair) suminfo.get(0)).getLeft())) {
                                            applyfrom.cancelEffect(applyfrom.getBuffedEffect((Integer) ((Pair) suminfo.get(0)).getLeft()));
                                        }
                                    }
                                }
                            }
                            if (this.sourceid == 400011077) {
                                SkillFactory.getSkill(400011078).getEffect(this.level).applyTo(applyto, false, localDuration);
                            } else if (this.sourceid == 131001022) {
                                SkillFactory.getSkill(131002022).getEffect(this.level).applyTo(applyto, false, localDuration);
                                SkillFactory.getSkill(131003022).getEffect(this.level).applyTo(applyto, false, localDuration);
                                SkillFactory.getSkill(131004022).getEffect(this.level).applyTo(applyto, false, localDuration);
                                SkillFactory.getSkill(131005022).getEffect(this.level).applyTo(applyto, false, localDuration);
                                SkillFactory.getSkill(131006022).getEffect(this.level).applyTo(applyto, false, localDuration);
                            } else if (this.sourceid == 400031007) {
                                SkillFactory.getSkill(400031008).getEffect(this.level).applyTo(applyto, false, localDuration);
                            } else if (this.sourceid == 400031008) {
                                SkillFactory.getSkill(400031009).getEffect(this.level).applyTo(applyto, false, localDuration);
                            } else if (this.sourceid == 5210015 && primary) {
                                if (applyfrom.getBuffedValue(5210015)) {
                                    applyfrom.cancelEffect(applyfrom.getBuffedEffect(5210015));
                                }
                                if (applyto.getSkillLevel(5210015) > 0) {
                                    SkillFactory.getSkill(5220019).getEffect(applyto.getSkillLevel(5220019)).applyTo(applyto, false, false);
                                    summon = applyto.getSummon(5211019);
                                    if (summon != null) {
                                        summon.removeSummon(applyto.getMap(), false);
                                    };
                                    MapleSummon summon4 = new MapleSummon(applyto, 5211019, applyto.getTruePosition(), SummonMovementType.FLAME_SUMMON, (byte) 0, 120000);
                                    applyto.getMap().spawnSummon(summon4, 120000);
                                    applyto.addSummon(summon4);
                                }
                            } else if (this.sourceid == 400051038) {
                                summon = applyto.getSummon(400051052);
                                if (summon != null) {
                                    summon.removeSummon(applyto.getMap(), false);
                                }
                                if ((summon = applyto.getSummon(400051053)) != null) {
                                    summon.removeSummon(applyto.getMap(), false);
                                }
                                if ((summon = applyto.getSummon(400051038)) != null) {
                                    summon.removeSummon(applyto.getMap(), false);
                                }
                                try {
                                    MapleSummon tosummon1 = new MapleSummon(applyfrom, 400051038, new Point(applyfrom.getTruePosition().x, applyfrom.getTruePosition().y), summonMovementType, rltype, localDuration);
                                    applyfrom.getMap().spawnSummon(tosummon1, localDuration);
                                    applyfrom.addSummon(tosummon1);
                                    tosummon1.addHP(this.x);
                                    MapleSummon tosummon2 = new MapleSummon(applyfrom, 400051052, new Point(applyfrom.getTruePosition().x + 100, applyfrom.getTruePosition().y), summonMovementType, rltype, localDuration);
                                    applyfrom.getMap().spawnSummon(tosummon2, localDuration);
                                    applyfrom.addSummon(tosummon2);
                                    tosummon2.addHP(this.x);
                                    MapleSummon tosummon3 = new MapleSummon(applyfrom, 400051053, new Point(applyfrom.getTruePosition().x + 200, applyfrom.getTruePosition().y), summonMovementType, rltype, localDuration);
                                    applyfrom.getMap().spawnSummon(tosummon3, localDuration);
                                    applyfrom.addSummon(tosummon3);
                                    tosummon3.addHP(this.x);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (this.sourceid == 5321004) {
                                if (applyfrom.getSkillLevel(5320044) > 0) {
                                    localDuration += SkillFactory.getSkill(5320044).getEffect(1).getDuration();
                                }
                                if ((summon = applyto.getSummon(5320011)) != null) {
                                    summon.removeSummon(applyto.getMap(), false);
                                }
                                try {
                                    Point pos1 = new Point(pos.x + (applyto.isFacingLeft() ? 90 : -90), pos.y);
                                    MapleSummon tosummon2 = new MapleSummon(applyfrom, 5320011, pos1, summonMovementType, rltype, localDuration);
                                    applyfrom.getMap().spawnSummon(tosummon2, localDuration);
                                    applyfrom.addSummon(tosummon2);
                                    tosummon2.addHP(this.x);
                                    if (applyfrom.getSkillLevel(5320045) > 0) {
                                        pos1 = new Point(pos.x + (applyto.isFacingLeft() ? 180 : -180), pos.y);
                                        MapleSummon tosummon3 = new MapleSummon(applyfrom, 5320011, pos1, summonMovementType, rltype, localDuration);
                                        applyfrom.getMap().spawnSummon(tosummon3, localDuration);
                                        applyfrom.addSummon(tosummon3);
                                        tosummon3.addHP(this.x);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (this.sourceid == 4341006) {
                                applyfrom.cancelEffectFromBuffStat(SecondaryStat.ShadowPartner);
                            } else if (this.sourceid == 35111002) {
                                ArrayList<Integer> count = new ArrayList<Integer>();
                                for (MapleSummon s : applyfrom.getMap().getAllSummonsThreadsafe()) {
                                    if (s.getSkill() != this.sourceid || s.getOwner().getId() != applyfrom.getId()) {
                                        continue;
                                    }
                                    count.add(s.getObjectId());
                                }
                                if (count.size() == 3) {
                                    applyfrom.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(this.sourceid, this.getCooldown(applyfrom)));
                                    applyfrom.addCooldown(this.sourceid, System.currentTimeMillis(), this.getCooldown(applyfrom));
                                    applyfrom.getMap().broadcastMessage(CField.teslaTriangle(applyfrom.getId(), (Integer) count.get(0), (Integer) count.get(1), (Integer) count.get(2)));
                                }
                            } else if (this.sourceid == 35121003) {
                                applyfrom.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyfrom));
                            } else if (this.sourceid == 400051017) {
                                MapleAtom atom = new MapleAtom(false, applyfrom.getId(), 30, true, this.sourceid, 0, 0);
                                List<MapleMapObject> objs = applyfrom.getMap().getMapObjectsInRange(applyto.getTruePosition(), 500000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
                                ArrayList<Integer> monsters = new ArrayList<Integer>();
                                MapleMonster mob = null;
                                for (MapleMapObject obj : objs) {
                                    atom.addForceAtom(new ForceAtom(1, Randomizer.rand(52, 72), Randomizer.rand(5, 6), Randomizer.rand(33, 72), 1440, applyfrom.getTruePosition()));
                                    monsters.add(obj.getObjectId());
                                }
                                for (int i = atom.getForceAtoms().size(); i < this.bulletCount; ++i) {
                                    mob = (MapleMonster) objs.get(Randomizer.rand(0, objs.size() - 1));
                                    atom.addForceAtom(new ForceAtom(1, Randomizer.rand(52, 72), Randomizer.rand(5, 6), Randomizer.rand(33, 72), 1440, applyfrom.getTruePosition()));
                                    monsters.add(mob.getObjectId());
                                }
                                atom.setDwTargets(monsters);
                                ForceAtom forceAtom = new ForceAtom(1, 49, 5, Randomizer.rand(45, 90), 1440);
                                atom.addForceAtom(forceAtom);
                                applyfrom.getMap().spawnMapleAtom(atom);
                            }
                        }
                    } else if (this.isMechDoor()) {
                        int newId = 0;
                        boolean applyBuff = false;
                        if (applyto.getMechDoors().size() >= 2) {
                            MechDoor remove = applyto.getMechDoors().remove(0);
                            newId = remove.getId();
                            applyto.getMap().broadcastMessage(CField.removeMechDoor(remove, true));
                            applyto.getMap().removeMapObject(remove);
                        } else {
                            for (MechDoor d : applyto.getMechDoors()) {
                                if (d.getId() != newId) {
                                    continue;
                                }
                                applyBuff = true;
                                newId = 1;
                                break;
                            }
                        }
                        MechDoor door = new MechDoor(applyto, new Point(pos == null ? applyto.getTruePosition() : pos), newId, localDuration);
                        applyto.getMap().spawnMechDoor(door);
                        applyto.addMechDoor(door);
                        if (!applyBuff) {
                            return true;
                        }
                    }
                    if (primary && this.availableMap != null) {
                        for (Pair<Integer, Integer> e : this.availableMap) {
                            if (applyto.getMapId() >= (Integer) e.left && applyto.getMapId() <= (Integer) e.right) {
                                continue;
                            }
                            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                            return true;
                        }
                    }
                    this.applyBuffEffect(applyfrom, applyto, primary, localDuration, pos, showEffect);
                    if (applyfrom.getId() == applyto.getId() && applyfrom.getParty() != null) {
                        Rectangle bounds = this.calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
                        List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.PLAYER}));
                        for (MapleMapObject affectedmo : affecteds) {
                            MapleCharacter affected = (MapleCharacter) affectedmo;
                            if (affected.getParty() == null || applyfrom.getId() == affected.getId() || !this.isPartyBuff(applyfrom, affected) || !this.calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft()).contains(applyto.getTruePosition()) || applyfrom.getParty().getId() != affected.getParty().getId()) {
                                continue;
                            }
                            this.applyTo(applyto, affected, primary, pos, localDuration, (byte) 0, this.sourceid == 2311003);
                            affected.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(affected, 0, this.sourceid, 4, 0, 0, (byte) (affected.getTruePosition().x > pos.x ? 1 : 0), true, pos, null, null));
                            affected.getMap().broadcastMessage(affected, CField.EffectPacket.showEffect(affected, 0, this.sourceid, 4, 0, 0, (byte) (affected.getTruePosition().x > pos.x ? 1 : 0), false, pos, null, null), false);
                        }
                    }
                    if (!this.isMagicDoor()) {
                        break block195;
                    }
                    MapleDoor door = new MapleDoor(applyto, new Point(pos == null ? applyto.getTruePosition() : pos), this.sourceid);
                    if (door.getTownPortal() != null) {
                        applyto.getMap().spawnDoor(door);
                        applyto.addDoor(door);
                        MapleDoor townDoor = new MapleDoor(door);
                        applyto.addDoor(townDoor);
                        door.getTown().spawnDoor(townDoor);
                        if (applyto.getParty() != null) {
                            applyto.silentPartyUpdate();
                        }
                    } else {
                        applyto.dropMessage(5, "You may not spawn a door because all doors in the town are taken.");
                    }
                    break block196;
                }
                if (!this.isMist()) {
                    break block197;
                }
                if (this.sourceid == 33111013 || this.sourceid == 21121057) {
                    pos = null;
                }
                if (this.sourceid == 400011098 || this.sourceid == 400011100) {
                    localDuration = this.cooltime;
                }
                if (this.sourceid == 4121015) {
                    applyfrom.getMap().removeMist(4121015);
                }
                if (this.sourceid == 61121116) {
                    localDuration = 750;
                }
                if (this.sourceid == 101120104) {
                    localDuration = 9000;
                }
                if (this.sourceid == 400031012) {
                    localDuration = 10000;
                }
                if (this.sourceid == 400011135) {
                    localDuration = 1800;
                }
                if (this.sourceid == 61121105 && applyto.getSkillLevel(61120047) > 0) {
                    localDuration += SkillFactory.getSkill(61120047).getEffect(1).getDuration();
                }
                if (this.sourceid == 32121006 && applyto.getSkillLevel(32120064) > 0) {
                    localDuration += SkillFactory.getSkill(32120064).getEffect(1).getDuration();
                }
                boolean spawn = false;
                if ((this.sourceid == 400031039 || this.sourceid == 400031040) && primary || (this.sourceid == 400040008 || this.sourceid == 400041008) && !primary || this.sourceid == 2111003 || (this.sourceid == 12121005 || this.sourceid == 400001017 || this.sourceid == 100001261 || this.sourceid == 151121041 || this.sourceid == 4221006) && primary || this.sourceid != 400031039 && this.sourceid != 400031040 && this.sourceid != 12121005 && this.sourceid != 400001017 && this.sourceid != 100001261 && this.sourceid != 151121041 && this.sourceid != 400040008 && this.sourceid != 400041008 && this.sourceid != 4221006) {
                    spawn = true;
                }
                if (!spawn) {
                    break block196;
                }
                if (this.sourceid == 400051025 || this.sourceid == 400051026) {
                    pos = new Point(pos.x, applyto.getMap().getFootholds().findBelow(pos).getY1() + 18);
                }
                Rectangle bounds = this.calculateBoundingBox(pos != null ? pos : applyfrom.getTruePosition(), applyfrom.isFacingLeft());
                MapleMist mist = new MapleMist(bounds, applyfrom, this, this.sourceid == 12121005 ? this.alchemistModifyVal(applyfrom, localDuration, false) : localDuration, rltype);
                if (this.sourceid == 101120104) {
                    mist.setEndTime(9000);
                }
                if (this.sourceid == 151121041) {
                    mist.setDuration(1050);
                    mist.setEndTime(1050);
                }
                mist.setPosition(pos == null ? applyto.getTruePosition() : pos);
                if (this.sourceid == 400051025 || this.sourceid == 400051026 || this.sourceid == 400031012 || this.sourceid == 22170093 || this.sourceid == 400041041) {
                    mist.setDelay(0);
                }
                applyfrom.getMap().spawnMist(mist, false);
                if (applyfrom.isGM()) {
                    applyfrom.dropMessage(6, "spawn Mist : " + localDuration);
                }
                if (sourceid == 400051025) {
                    applyfrom.getMap().broadcastMessage(CField.ICBM(true, sourceid, calculateBoundingBox(pos, applyfrom.isFacingLeft())));
                }
            }

        }
        if (isTimeLeap() && (System.currentTimeMillis() - applyto.lastTimeleapTime) >= duration) {
            for (MapleCoolDownValueHolder i : applyto.getCooldowns()) {
                if (i.skillId != 5121010 && !SkillFactory.getSkill(i.skillId).isHyper() && i.skillId / 10000 <= applyto.getJob()) {
                    applyto.lastTimeleapTime = System.currentTimeMillis();
                    applyto.removeCooldown(i.skillId);
                    applyto.getClient().getSession().writeAndFlush(CField.skillCooldown(i.skillId, 0));
                }
            }
        }
        if (this.rewardMeso != 0) {
            applyto.gainMeso(this.rewardMeso, false);
        }
        if (this.rewardItem != null && this.totalprob > 0) {
            for (Triple<Integer, Integer, Integer> reward : this.rewardItem) {
                if (!MapleInventoryManipulator.checkSpace(applyto.getClient(), (Integer) reward.left, (Integer) reward.mid, "") || (Integer) reward.right <= 0 || Randomizer.nextInt(this.totalprob) >= (Integer) reward.right) {
                    continue;
                }
                if (GameConstants.getInventoryType((Integer) reward.left) == MapleInventoryType.EQUIP) {
                    Item item = MapleItemInformationProvider.getInstance().getEquipById((Integer) reward.left);
                    item.setGMLog("Reward item (effect): " + this.sourceid + " on " + FileoutputUtil.CurrentReadable_Date());
                    MapleInventoryManipulator.addbyItem(applyto.getClient(), item);
                    continue;
                }
                MapleInventoryManipulator.addById(applyto.getClient(), (Integer) reward.left, ((Integer) reward.mid).shortValue(), "Reward item (effect): " + this.sourceid + " on " + FileoutputUtil.CurrentReadable_Date());
            }
        }
        return true;
    }

    public final boolean applyReturnScroll(MapleCharacter applyto) {
        if (this.moveTo != -1 && (applyto.getMap().getReturnMapId() != applyto.getMapId() || this.sourceid == 2031010 || this.sourceid == 2030021)) {
            MapleMap target;
            if (this.moveTo == 999999999) {
                target = applyto.getMap().getReturnMap();
            } else {
                target = ChannelServer.getInstance(applyto.getClient().getChannel()).getMapFactory().getMap(this.moveTo);
                if (target.getId() / 10000000 != 60 && applyto.getMapId() / 10000000 != 61 && target.getId() / 10000000 != 21 && applyto.getMapId() / 10000000 != 20 && target.getId() / 10000000 != applyto.getMapId() / 10000000) {
                    return false;
                }
            }
            applyto.changeMap(target, target.getPortal(0));
            return true;
        }
        return false;
    }

    public final Rectangle calculateBoundingBox(int skillid, int level, Point posFrom, boolean facingLeft) {
        return SecondaryStatEffect.calculateBoundingBox(posFrom, facingLeft, SkillFactory.getSkill((int) skillid).getEffect((int) level).lt, SkillFactory.getSkill((int) skillid).getEffect((int) level).rb, this.range);
    }

    public final Rectangle calculateBoundingBox(Point posFrom, boolean facingLeft) {
        return SecondaryStatEffect.calculateBoundingBox(posFrom, facingLeft, this.lt, this.rb, this.range);
    }

    public final Rectangle calculateBoundingBox(Point posFrom, boolean facingLeft, int addedRange) {
        return SecondaryStatEffect.calculateBoundingBox(posFrom, facingLeft, this.lt, this.rb, this.range + addedRange);
    }

    public static Rectangle calculateBoundingBox(Point posFrom, boolean facingLeft, Point lt, Point rb2, int range) {
        Point myrb;
        Point mylt;
        if (lt == null || rb2 == null) {
            return new Rectangle((facingLeft ? -200 - range : 0) + posFrom.x, -100 - range + posFrom.y, 200 + range, 100 + range);
        }
        if (facingLeft) {
            mylt = new Point(lt.x + posFrom.x - range, lt.y + posFrom.y);
            myrb = new Point(rb2.x + posFrom.x, rb2.y + posFrom.y);
        } else {
            myrb = new Point(lt.x * -1 + posFrom.x + range, rb2.y + posFrom.y);
            mylt = new Point(rb2.x * -1 + posFrom.x, lt.y + posFrom.y);
        }
        return new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
    }

    public final double getMaxDistanceSq() {
        int maxX = Math.max(Math.abs(this.getLt() == null ? 0 : this.getLt().x), Math.abs(this.rb == null ? 0 : this.rb.x));
        int maxY = Math.max(Math.abs(this.getLt() == null ? 0 : this.getLt().y), Math.abs(this.rb == null ? 0 : this.rb.y));
        return maxX * maxX + maxY * maxY;
    }

    public final void setDuration(int d) {
        this.duration = d;
    }

    public final void silentApplyBuff(MapleCharacter chr, long starttime, Map<SecondaryStat, Pair<Integer, Integer>> statup, int cid) {
        HashMap<SecondaryStat, Pair<Integer, Integer>> cancelStats = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> statupz : statup.entrySet()) {
            MapleSummon tosummon;
            SummonMovementType summonMovementType;
            long remainDuration = (long) ((Integer) statupz.getValue().right).intValue() - (System.currentTimeMillis() - starttime);
            if (remainDuration > 0L) {
                chr.registerEffect(this, starttime, statupz, true, cid);
                summonMovementType = this.getSummonMovementType();
                if (summonMovementType == null || (tosummon = new MapleSummon(chr, this, chr.getTruePosition(), summonMovementType)).isPuppet()) {
                    continue;
                }
                chr.getMap().spawnSummon(tosummon, (int) remainDuration);
                chr.addSummon(tosummon);
                tosummon.addHP(this.x);
                continue;
            }
            if ((Integer) statupz.getValue().right == 0) {
                chr.registerEffect(this, starttime, statupz, true, cid);
                summonMovementType = this.getSummonMovementType();
                if (summonMovementType == null || (tosummon = new MapleSummon(chr, this, chr.getTruePosition(), summonMovementType)).isPuppet()) {
                    continue;
                }
                chr.getMap().spawnSummon(tosummon, (Integer) statupz.getValue().right);
                chr.addSummon(tosummon);
                tosummon.addHP(this.x);
                continue;
            }
            cancelStats.put(statupz.getKey(), new Pair<Integer, Integer>(0, 0));
        }
        if (!cancelStats.isEmpty()) {
            chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(cancelStats, chr));
        }
    }

    public final void applyKaiserCombo(MapleCharacter applyto, short combo) {
        EnumMap<SecondaryStat, Pair<Integer, Integer>> stat = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
        stat.put(SecondaryStat.SmashStack, new Pair<Integer, Integer>(Integer.valueOf(combo), 0));
        applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(stat, null, applyto));
    }

    /*
     * Opcode count of 19699 triggered aggressive code reduction.  Override with --aggressivesizethreshold.
     * WARNING - void declaration
     */
    // 버프 걸리면 호출
    private final void applyBuffEffect(MapleCharacter applyfrom, MapleCharacter applyto, boolean primary, int newDuration, Point pos, boolean showEffect) {
        int[] FreudsProtections;
        int localDuration = newDuration;
        if (pos == null) {
            pos = applyto.getTruePosition() == null ? applyto.getPosition() : applyto.getTruePosition();
        }
        HashMap<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        ArrayList<Pair<Integer, Integer>> indieList1 = new ArrayList<Pair<Integer, Integer>>();
        ArrayList<Pair<Integer, Integer>> indieList2 = new ArrayList<Pair<Integer, Integer>>();
        boolean aftercancel = false;
        boolean bufftimeR = true;
        boolean isPetBuff = false;
        boolean cancel = true;
        for (MaplePet pet : applyto.getPets()) {
            if (pet == null || pet.getBuffSkillId() != this.sourceid || pet.getBuffSkillId2() != this.sourceid) {
                continue;
            }
            isPetBuff = true;
        }
        if (GameConstants.isPhantom(applyto.getJob())) {
            for (Pair pair : applyto.getStolenSkills()) {
                if ((Integer) pair.left != this.sourceid || !((Boolean) pair.right).booleanValue()) {
                    continue;
                }
                switch ((Integer) pair.left) {
                    case 2121054:
                    case 2221054:
                    case 2321054:
                    case 3121054:
                    case 4121054:
                    //  case 4221054:
                    case 5121054:
                    case 5221054: {
                        localDuration = 30000;
                        break;
                    }
                }
                break;
            }
        }
        block3:
        switch (this.sourceid) {
            case 1121010: {
                applyfrom.handleOrbconsume(1121010);
                break;
            }
            case 1200014:
            case 1220010: {
                if (applyfrom.getElementalCharge() <= 0 || applyfrom.getElementalCharge() > this.getZ()) {
                    break;
                }
                localstatups.clear();
                int u = this.u;
                if (applyfrom.getSkillLevel(1220010) > 0) {
                    u = SkillFactory.getSkill(1220010).getEffect(applyfrom.getSkillLevel(1220010)).getX();
                }
                localstatups.put(SecondaryStat.ElementalCharge, new Pair<Integer, Integer>(u * applyfrom.getElementalCharge(), localDuration));
                break;
            }
            case 1211010: {
                bufftimeR = false;
                if (applyfrom.getListonation() < 5) {
                    applyfrom.setListonation(applyfrom.getListonation() + 1);
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.Listonation, new Pair<Integer, Integer>(applyfrom.getListonation() * this.y, localDuration));
                break;
            }
            case 1211014: {
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                if (!primary) {
                    localstatups.put(SecondaryStat.KnightsAura, new Pair<Integer, Integer>(this.y, 0));
                    localstatups.put(SecondaryStat.IndiePddR, new Pair<Integer, Integer>(-this.z, 0));
                    break;
                }
                localstatups.put(SecondaryStat.KnightsAura, new Pair<Integer, Integer>(this.y, 0));
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(this.indiePad), 0));
                localstatups.put(SecondaryStat.IndiePddR, new Pair<Integer, Integer>(this.z, 0));
                break;
            }
            case 1301013: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.Beholder, new Pair<Integer, Integer>(1, 0));
                if (applyfrom.getSkillLevel(1310013) <= 0) {
                    applyfrom.setBeholderSkill1(1301013);
                    break;
                }
                applyfrom.setBeholderSkill1(1310013);
            }
            case 1310016: {
                int n = 0;
                int epad = this.epad;
                short s = this.epdd;
                int indieCr = this.indieCr;
                if (applyfrom.getSkillLevel(1320044) > 0) {
                    epad += SkillFactory.getSkill(1320044).getEffect(1).getX();
                }
                if (applyfrom != applyto) {
                    epad /= 2;
                    n = s / 2; //수정
                    indieCr /= 2;
                }
                localstatups.put(SecondaryStat.EnhancedPad, new Pair<Integer, Integer>(epad, 0));
                localstatups.put(SecondaryStat.EnhancedPdd, new Pair<Integer, Integer>(n, 0));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(indieCr, 0));
                break;
            }
            case 1320019: {
                int data = this.z;
                if (applyfrom.getSkillLevel(1320047) > 0) {
                    data -= this.z * SkillFactory.getSkill(1320047).getEffect(1).getX() / 100;
                }
                applyfrom.setReinCarnation(data);
                applyfrom.removeCooldown(1321013);
                break;
            }
            case 2121004:
            case 2221004:
            case 2321004: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Infinity, new Pair<Integer, Integer>(1, localDuration));
                applyfrom.setInfinity((byte) 0);
                break;
            }
            case 2111011:
            case 2311012: {
                aftercancel = true;
                localstatups.put(SecondaryStat.AntiMagicShell, new Pair<Integer, Integer>(1, 0));
                applyfrom.setAntiMagicShell((byte) 1);
                break;
            }
            case 2120010:
            case 2220010:
            case 2320011: {
                bufftimeR = false;
                localstatups.clear();
                localstatups.put(SecondaryStat.ArcaneAim, new Pair<Integer, Integer>(applyto.getArcaneAim(), 5000));
                break;
            }
            case 2201009: {
                if (!applyfrom.getBuffedValue(2201009)) {
                    localstatups.put(SecondaryStat.ChillingStep, new Pair<Integer, Integer>(1, 0));
                    bufftimeR = false;
                    aftercancel = true;
                    break;
                }
                return;
            }
            case 2221054: {
                localDuration = 0;
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), localDuration > 0 ? (long) localDuration : 0L);
                localstatups.put(SecondaryStat.IceAura, new Pair<Integer, Integer>(1, localDuration > 0 ? localDuration : 0));
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(this.z, localDuration > 0 ? localDuration : 0));
                localstatups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(this.w, localDuration > 0 ? localDuration : 0));
                break;
            }
            case 2301004: {
                if (applyto.getBuffedValue(2321005)) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.AdvancedBless, 2321005);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndiePad, 2321005);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndieMad, 2321005);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndieHp, 2321005);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndieMp, 2321005);
                }
                localstatups.put(SecondaryStat.Bless, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(this.x, localDuration));
                localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(this.y, localDuration));
                localstatups.put(SecondaryStat.IndiePdd, new Pair<Integer, Integer>(this.z, localDuration));
                break;
            }
            case 2310013: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.CooltimeHolyMagicShell, new Pair<Integer, Integer>(1, SkillFactory.getSkill(2311009).getEffect(this.level).getY() * 1000));
                break;
            }
            case 2311003: {
                if (primary) {
                    applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                    applyto.setSkillCustomInfo(2311004, 0L, 0L);
                }
                if (!showEffect) {
                    showEffect = true;
                    bufftimeR = false;
                }
                int asr = SkillFactory.getSkill((int) 2320047).getEffect((int) 1).asrR;
                if (applyfrom.getId() == applyto.getId() && applyfrom.getSkillLevel(2320046) > 0) {
                    localstatups.put(SecondaryStat.HolySymbol, new Pair<Integer, Integer>(this.x + SkillFactory.getSkill(2320046).getEffect(1).getY(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.HolySymbol, new Pair<Integer, Integer>(primary ? this.x : this.x / 2, localDuration));
                }
                if (applyfrom.getSkillLevel(2320047) > 0) {
                    localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(primary ? asr : asr / 2, localDuration));
                    localstatups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(primary ? asr : asr / 2, localDuration));
                }
                if (applyfrom.getSkillLevel(2320048) <= 0) {
                    break;
                }
                applyto.setSkillCustomInfo(2320048, SkillFactory.getSkill((int) 2320048).getEffect((int) 1).v, 0L);
                localstatups.put(SecondaryStat.DropRate, new Pair<Integer, Integer>(SkillFactory.getSkill((int) 2320048).getEffect((int) 1).v, localDuration));
                break;
            }
            case 2311009: {
                if (applyto.getBuffedEffect(SecondaryStat.CooltimeHolyMagicShell) != null) {
                    break;
                }
                byte data = (byte) this.x;
                if (applyfrom.getSkillLevel(2320043) > 0) {
                    data = (byte) (data + SkillFactory.getSkill(2320043).getEffect(1).getX());
                }
                applyto.setHolyMagicShell(data);
                this.hpR = (double) this.z / 100.0;
                SkillFactory.getSkill(2310013).getEffect(this.level).applyTo(applyfrom, applyto);
                if (applyfrom.getSkillLevel(2320044) > 0) {
                    localDuration += SkillFactory.getSkill(2320044).getEffect(applyfrom.getSkillLevel(2320044)).getDuration();
                }
                localstatups.put(SecondaryStat.HolyMagicShell, new Pair<Integer, Integer>(Integer.valueOf(applyto.getHolyMagicShell()), localDuration));
                break;
            }
            case 2321001: {
                localstatups.clear();
                if (!primary) {
                    if (localDuration == this.duration) {
                        break;
                    }
                    bufftimeR = false;
                    break;
                }
                return;
            }
            case 2321054: {
                for (MapleSummon mapleSummon : applyto.getMap().getAllSummonsThreadsafe()) {
                    if (mapleSummon.getOwner().getId() != applyto.getId() || mapleSummon.getSkill() != 400021032 && mapleSummon.getSkill() != 400021033) {
                        continue;
                    }
                    mapleSummon.removeSummon(applyto.getMap(), false);
                    applyto.removeSummon(mapleSummon);
                }
                if (applyto.getBuffedEffect(400021032) != null) {
                    long bufftime = applyto.getBuffLimit(400021032);
                    while (applyto.getBuffedValue(400021032)) {
                        applyto.cancelEffect(applyto.getBuffedEffect(400021032));
                    }
                    SkillFactory.getSkill(400021033).getEffect(applyto.getSkillLevel(400021032)).applyTo(applyto, false, (int) bufftime);
                    break;
                }
                if (applyto.getBuffedEffect(400021033) == null) {
                    break;
                }
                long bufftime = applyto.getBuffLimit(400021033);
                while (applyto.getBuffedValue(400021033)) {
                    applyto.cancelEffect(applyto.getBuffedEffect(400021033));
                }
                SkillFactory.getSkill(400021032).getEffect(applyto.getSkillLevel(400021032)).applyTo(applyto, false, (int) bufftime);
                break;
            }
            case 2321016: {
                localDuration = this.w;
                int int_ = applyto.getStat().getTotalInt() / this.s;
                localstatups.put(SecondaryStat.HolyBlood, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieShotDamage, new Pair<Integer, Integer>(this.v, localDuration));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Math.min(this.s2, this.u + (this.u * int_)), localDuration));
                break;
            }
            case 3121002:
            case 3221002:
            case 3321022: {
                localstatups.clear();
                if (applyfrom.getSkillLevel(3120043) > 0) {
                    localDuration += SkillFactory.getSkill(3120043).getEffect(1).getDuration();
                }
                if (applyfrom.getSkillLevel(3220043) > 0) {
                    localDuration += SkillFactory.getSkill(3220043).getEffect(1).getDuration();
                }
                if (applyfrom.getSkillLevel(3320025) > 0) {
                    localDuration += SkillFactory.getSkill(3320025).getEffect(1).getDuration();
                }
                if (applyfrom.getSkillLevel(3120044) > 0) {
                    localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 3120044).getEffect((int) 1).ignoreMobpdpR), localDuration));
                }
                if (applyfrom.getSkillLevel(3220044) > 0) {
                    localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 3220044).getEffect((int) 1).ignoreMobpdpR), localDuration));
                }
                if (applyfrom.getSkillLevel(3320026) > 0) {
                    localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 3320026).getEffect((int) 1).ignoreMobpdpR), localDuration));
                }
                localstatups.put(SecondaryStat.SharpEyes, new Pair<Integer, Integer>(((applyfrom.getSkillLevel(3120045) > 0 ? 5 : (applyfrom.getSkillLevel(3220045) > 0 ? 5 : (applyfrom.getSkillLevel(3320027) > 0 ? 5 : 0))) + this.x << 8) + this.y, localDuration));
                break;
            }
            case 3320008: {
                showEffect = false;
                localstatups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(3320008), localDuration));
                break;
            }
            case 3321034: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                if (!GameConstants.isPathFinder(applyto.getJob())) {
                    break;
                }
                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                statups.put(SecondaryStat.AncientGuidance, new Pair<Integer, Integer>(-1, 0));
                applyto.렐릭게이지 = 1000;
                applyto.에인션트가이던스 = 0;
                statups.put(SecondaryStat.RelikGauge, new Pair<Integer, Integer>(applyto.렐릭게이지, 0));
                SkillFactory.getSkill(3310006).getEffect(applyto.getSkillLevel(3310006)).applyTo(applyto);
                MapleCharacter.렐릭게이지(applyfrom.getClient(), 0);
                break;
            }
            case 3310006: {
                if (applyto.getBuffedValue(this.sourceid)) {
                    localDuration = (int) ((long) localDuration + applyto.getBuffLimit(this.sourceid));
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.AncientGuidance, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                applyto.addHP(applyto.getStat().getCurrentMaxHp() / 100L * (long) this.y);
                applyto.addMP(applyto.getStat().getCurrentMaxMp(applyto) / 100L * (long) this.y);
                applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(applyto, 0, 3310006, 1, 0, 0, (byte) (applyto.isFacingLeft() ? 1 : 0), true, applyto.getTruePosition(), null, null));
                applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showEffect(applyto, 0, 3310006, 1, 0, 0, (byte) (applyto.isFacingLeft() ? 1 : 0), false, applyto.getTruePosition(), null, null), false);
                break;
            }
            case 400011038: {
                localstatups.clear();
                localstatups.put(SecondaryStat.BloodFist, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 2321005: {
                if (applyto.getBuffedValue(2301004)) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.Bless, 2301004);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndiePad, 2301004);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndieMad, 2301004);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndiePdd, 2301004);
                }
                if (applyto.getBuffedValue(400001005)) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndieMp, 400001005);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndiePad, 400001005);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndieMad, 400001005);
                    applyto.cancelEffectFromBuffStat(SecondaryStat.IndieHp, 400001005);
                }
                localstatups.put(SecondaryStat.AdvancedBless, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                if (applyfrom.getSkillLevel(2320051) > 0) {
                    localstatups.put(SecondaryStat.IndieMp, new Pair<Integer, Integer>(this.indieMmp + SkillFactory.getSkill((int) 2320051).getEffect((int) 1).indieMmp, localDuration));
                    localstatups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(this.indieMhp + SkillFactory.getSkill((int) 2320051).getEffect((int) 1).indieMhp, localDuration));
                } else {
                    localstatups.put(SecondaryStat.IndieMp, new Pair<Integer, Integer>(Integer.valueOf(this.indieMmp), localDuration));
                    localstatups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(Integer.valueOf(this.indieMhp), localDuration));
                }
                if (applyfrom.getSkillLevel(2320049) > 0) {
                    localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(this.y + SkillFactory.getSkill(2320049).getEffect(1).getX(), localDuration));
                    localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(this.x + SkillFactory.getSkill(2320049).getEffect(1).getX(), localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(this.y, localDuration));
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 2321052: {
                bufftimeR = false;
                if (applyto.getBuffedEffect(SecondaryStat.CooldownHeavensDoor) != null || applyto.getBuffedValue(2321052)) {
                    break;
                }
                localstatups.put(SecondaryStat.HeavensDoor, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 2321055: {
                bufftimeR = false;
                if (applyto.getBuffedEffect(SecondaryStat.CooldownHeavensDoor) == null) {
                    localstatups.put(SecondaryStat.CooldownHeavensDoor, new Pair<Integer, Integer>(1, 600000));
                    break;
                }
                return;
            }
            case 3101009: {
                aftercancel = true;
                if (primary) {
                    applyto.setQuiverType((byte) 1);
                    applyto.getRestArrow()[0] = 1;
                    applyto.getRestArrow()[1] = 2;

                }
                switch (applyto.getQuiverType()) {
                    case 1: {
                        localstatups.put(SecondaryStat.QuiverCatridge, new Pair<Integer, Integer>(applyto.getRestArrow()[0], 0));
                        break block3;
                    }
                    case 2: {
                        localstatups.put(SecondaryStat.QuiverCatridge, new Pair<Integer, Integer>(applyto.getRestArrow()[1], 0));
                        break block3;
                    }
                }
                break;
            }
            case 3110001: {
                bufftimeR = false;
                aftercancel = true;
                if (applyto.getBuffedEffect(SecondaryStat.IndieDamR, this.sourceid) != null) {
                    break;
                }
                if (applyto.getMortalBlow() == this.x) {
                    applyto.setMortalBlow((byte) 0);
                    applyto.cancelEffect(this);
                    localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.y, localDuration));
                    break;
                }
                applyto.setMortalBlow((byte) (applyto.getMortalBlow() + 1));
                localstatups.put(SecondaryStat.MortalBlow, new Pair<Integer, Integer>(Integer.valueOf(applyto.getMortalBlow()), 0));
                break;
            }
            case 3210001: {
                localstatups.clear();
                if (applyto.getBuffedValue(this.sourceid)) {
                    applyto.cancelEffect(this);
                    break;
                }
                if (!Randomizer.isSuccess(this.x)) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.y, 0));
                applyto.addHP(applyto.getStat().getCurrentMaxHp() / 100L * (long) this.z);
                applyto.addMP(applyto.getStat().getCurrentMaxHp() / 100L * (long) this.z);
                break;
            }
            case 3110012: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.BowMasterConcentration, new Pair<Integer, Integer>(applyto.getConcentration() * 5, localDuration));
                break;
            }
            case 3210013: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.PowerTransferGauge, new Pair<Integer, Integer>(applyto.getBarrier(), localDuration));
                break;
            }
            case 4001003: {
                localstatups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 4111009:
            case 14110031:
            case 5201008:
            case 14111025: {
                localDuration = this.duration;
                if (newDuration / 10000 == 207 && this.sourceid != 5201008) {
                    localstatups.put(SecondaryStat.NoBulletConsume, new Pair<Integer, Integer>(newDuration - 2070000 + 1, localDuration));
                    break;
                }
                if (newDuration / 10000 != 233 || this.sourceid != 5201008) {
                    break;
                }
                localstatups.put(SecondaryStat.NoBulletConsume, new Pair<Integer, Integer>(newDuration - 2330000 + 1, localDuration));
                break;
            }
            /*
            case 4200013:
            case 4220015: {
                aftercancel = true;
                localstatups.put(SecondaryStat.CriticalGrowing, new Pair<Integer, Integer>(applyto.criticalGrowing, 0));
                break;
            }
             */
            case 4211016: {
                applyto.addCooldown(4001003, System.currentTimeMillis(), 3000L);
                applyto.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(4001003, 3000));
                applyto.addCooldown(4211016, System.currentTimeMillis(), 3000L);
                applyto.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(4211016, 3000));
                if (applyto.getSkillLevel(4001003) <= 0) {
                    break;
                }
                SkillFactory.getSkill(4001003).getEffect(applyto.getSkillLevel(4001003)).applyTo(applyto, false);
                break;
            }
            case 4221006: {
                localstatups.clear();
                if (primary) {
                    break;
                }
                localstatups.put(SecondaryStat.DamageDecreaseWithHP, new Pair<Integer, Integer>(this.y, 1800));
                break;
            }
            case 4221013: {
                localstatups.clear();
                if (localDuration == 0) {
                    aftercancel = true;
                    localstatups.put(SecondaryStat.KillingPoint, new Pair<Integer, Integer>(applyto.killingpoint, 0));
                    break;
                }
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(40 + applyto.killingpoint * this.kp, localDuration));
                if (applyto.getCooldownLimit(400041039) > 0L) {
                    applyto.changeCooldown(400041039, -500 * applyto.killingpoint);
                }
                if (applyto.killingpoint <= 0) {
                    break;
                }
                applyto.killingpoint = 0;
                this.applyTo(applyto, false, 0);
                break;
            }
            case 4221016: {
                if (applyto.getSkillLevel(400041025) <= 0 || applyto.shadowerDebuffOid == 0) {
                    break;
                }
                aftercancel = true;
                localstatups.put(SecondaryStat.ShadowerDebuff, new Pair<Integer, Integer>(applyto.shadowerDebuff, 10000));
                break;
            }
            case 4221054: {
                bufftimeR = false;
                aftercancel = true;
                localDuration = 0;
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.indieDamR * applyto.getFlip(), localDuration));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(this.x * applyto.getFlip(), localDuration));
                localstatups.put(SecondaryStat.FlipTheCoin, new Pair<Integer, Integer>(Integer.valueOf(applyto.getFlip()) + 1, localDuration));
                break;
            }
            case 4331006: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 1500));
                break;
            }
            case 4330009: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(this.indiePad), localDuration));
                break;
            }
            case 4341002: {
                localstatups.clear();
                if (primary) {
                    bufftimeR = true;
                    localstatups.put(SecondaryStat.FinalCut, new Pair<Integer, Integer>(this.y, localDuration));
                    break;
                }
                bufftimeR = false;
                localDuration = 3000;
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 3000));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 3000));
                break;
            }
            case 400051001: {
                localstatups.clear();
                if (applyto.getBuffedValue(SecondaryStat.SelectDice) != null) {
                    applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 0, 5111007, applyto.getBuffedValue(SecondaryStat.SelectDice), -1, false), false);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 0, 5111007, applyto.getBuffedValue(SecondaryStat.SelectDice), -1, true));
                    localstatups.put(SecondaryStat.DiceRoll, new Pair<Integer, Integer>(applyto.getBuffedValue(SecondaryStat.SelectDice), localDuration));
                    break;
                }
                applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 0, 5111007, 2, -1, false), false);
                applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 0, 5111007, 2, -1, true));
                applyto.dropMessage(5, "로디드 다이스가 적용되어 있지 않아 2번 효과가 적용 됩니다.");
                localstatups.put(SecondaryStat.DiceRoll, new Pair<Integer, Integer>(2, localDuration));
                break;
            }
            case 5111007:
            case 5120012:
            case 5211007:
            case 5220014:
            case 5311005:
            case 5320007:
            case 35111013:
            case 35120014: {
                int sk;
                boolean extra = false;
                if (applyfrom.getSkillLevel(5120044) > 0 || applyfrom.getSkillLevel(5220044) > 0) {
                    extra = true;
                }
                boolean bl = applyto.getBuffedEffect(SecondaryStat.SelectDice) != null;
                int thirddice = 0;
                if (bl) {
                    thirddice = applyto.getBuffedValue(SecondaryStat.SelectDice);
                }
                byte dice = (byte) Randomizer.rand(1, extra ? 7 : 6);
                byte doubledice = 1;
                if (this.isDoubleDice() && this.makeChanceResult()) {
                    doubledice = (byte) Randomizer.rand(2, extra ? 7 : 6);
                }
                if (applyto.isOneMoreChance()) {
                    applyto.setOneMoreChance(false);
                    dice = (byte) Randomizer.rand(4, extra ? 7 : 6);
                    if (doubledice > 1) {
                        doubledice = (byte) Randomizer.rand(4, extra ? 7 : 6);
                    }
                }
                applyto.setDice(thirddice * 100 + doubledice * 10 + dice);
                if (applyfrom.getSkillLevel(5120043) > 0 && applyto.getDice() == 11) {
                    applyfrom.setOneMoreChance(true);
                    if (SkillFactory.getSkill(5120043).getEffect(1).makeChanceResult()) {
                        applyfrom.removeCooldown(this.sourceid);
                        applyfrom.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(this.sourceid, 0));
                    }
                }
                if (applyfrom.getSkillLevel(5220043) > 0 && applyto.getDice() == 11) {
                    applyfrom.setOneMoreChance(true);
                    if (SkillFactory.getSkill(5220043).getEffect(1).makeChanceResult()) {
                        applyfrom.removeCooldown(this.sourceid);
                        applyfrom.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(this.sourceid, 0));
                    }
                }
                localstatups.put(SecondaryStat.DiceRoll, new Pair<Integer, Integer>(thirddice * 100 + doubledice * 10 + dice, localDuration));
                int n = bl ? applyto.getBuffSource(SecondaryStat.SelectDice) : (sk = this.sourceid);
                if (bl) {
                    applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 0, n, -1, 1, false), false);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 0, n, -1, 1, true));
                }
                if (this.isDoubleDice() && doubledice > 0) {
                    applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 0, n, dice, -1, false), false);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 0, n, dice, -1, true));
                    applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 1, n, doubledice, -1, false), false);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 1, n, doubledice, -1, true));
                    if (dice == 1 && doubledice == 1) {
                        applyto.dropMessage(5, "더블 럭키 다이스 스킬이 [" + dice + "], [" + doubledice + "]번이 나와 아무런 효과를 받지 못했습니다.");
                        if (thirddice == 1) {
                            return;
                        }
                    } else if (dice == 1) {
                        applyto.dropMessage(5, "더블 럭키 다이스 스킬이 [" + doubledice + "]번 효과를 발동 시켰습니다.");
                    } else if (doubledice == 1) {
                        applyto.dropMessage(5, "더블 럭키 다이스 스킬이 [" + dice + "]번 효과를 발동 시켰습니다.");
                    } else {
                        applyto.dropMessage(5, "더블 럭키 다이스 스킬이 [" + dice + "], [" + doubledice + "]번 효과를 발동 시켰습니다.");
                    }
                } else {
                    applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 0, n, dice, -1, false), false);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 0, n, dice, -1, true));
                    if (dice == 1) {
                        applyto.dropMessage(5, "럭키 다이스 스킬이 [" + dice + "]번이 나와 아무런 효과를 받지 못했습니다.");
                        if (thirddice == 1) {
                            return;
                        }
                    } else {
                        applyto.dropMessage(5, "럭키 다이스 스킬이 [" + dice + "]번 효과를 발동 시켰습니다.");
                    }
                }
                int[] arrn = new int[]{dice, doubledice};
                int i = 0;
                for (int repeat2 : arrn) {
                    if (repeat2 == 1) {
                        applyto.dropMessage(5, "더블 럭키 다이스 스킬이 [" + repeat2 + "]번이 나와 아무런 효과를 받지 못했습니다.");
                        if (++i >= 2) {
                            applyto.removeCooldown(this.sourceid);
                            continue;
                        }
                        applyto.changeCooldown(this.sourceid, -this.getDuration() / 2);
                        continue;
                    }
                    if (repeat2 > 1) {
                        continue;
                    }
                }
                if (applyto.getBuffedEffect(SecondaryStat.SelectDice) != null) {
                    applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 1, n, thirddice, 0, false), false);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 1, n, thirddice, 0, true));
                }
                applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, 1, n, -1, 2, false), false);
                applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showDiceEffect(applyto, 1, n, -1, 2, true));
                break;
            }
            case 5121055: {
                bufftimeR = false;
                if (applyto.getUnityofPower() < 4) {
                    applyto.setUnityofPower((byte) (applyto.getUnityofPower() + 1));
                }
                localstatups.put(SecondaryStat.UnityOfPower, new Pair<Integer, Integer>(Integer.valueOf(applyto.getUnityofPower()), localDuration));
                break;
            }
            case 5121054: {
                localstatups.put(SecondaryStat.Stimulate, new Pair<Integer, Integer>(1, this.duration));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>((int) this.indieDamR, this.duration));
                break;
            }
            case 5220055: {
                aftercancel = false;
                bufftimeR = false;
                localstatups.put(SecondaryStat.QuickDraw, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 5311004: {
                int zz = Randomizer.nextInt(4) + 1;
                applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(applyto, 0, this.sourceid, 6, zz, -1, (byte) (applyto.isFacingLeft() ? 1 : 0), true, pos, null, null));
                applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showEffect(applyto, 0, this.sourceid, 6, zz, -1, (byte) (applyto.isFacingLeft() ? 1 : 0), false, pos, null, null), false);
                localstatups.put(SecondaryStat.Roulette, new Pair<Integer, Integer>(zz, localDuration));
                if (zz == 2) {
                    localstatups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(this.s, localDuration));
                }
                applyto.setSkillCustomInfo(this.sourceid, zz, 0L);
                break;
            }
            case 5310008: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.KeyDownTimeIgnore, new Pair<Integer, Integer>(1, 15000));
                break;
            }
            case 5311002: {
                localstatups.put(SecondaryStat.HitCriDamR, new Pair<Integer, Integer>(this.x, this.subTime));
                break;
            }
            case 11101022: {
                short cr = this.indieCr;
                if (applyto.getSkillLevel(11120009) > 0) {
                    SecondaryStatEffect secondaryStatEffect = SkillFactory.getSkill(11120009).getEffect(applyto.getSkillLevel(11120009));
                    cr = secondaryStatEffect.indieCr;
                }
                localstatups.put(SecondaryStat.PoseType, new Pair<Integer, Integer>(1, 0));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(Integer.valueOf(cr), 0));
                localstatups.put(SecondaryStat.Buckshot, new Pair<Integer, Integer>(this.x, 0));
                if (!applyto.getBuffedValue(11111022)) {
                    break;
                }
                applyto.cancelEffect(applyto.getBuffedEffect(11111022));
                break;
            }
            case 11111022: {
                int n = 0;
                int booster = this.indieBooster;
                short s = this.indiePmdR;
                int watk = 0;
                if (applyto.getSkillLevel(11120009) > 0) {
                    SecondaryStatEffect ef = SkillFactory.getSkill(11120009).getEffect(applyto.getSkillLevel(11120009));
                    booster = -2;
                    n = ef.getV();
                    watk = ef.getS();
                }
                localstatups.put(SecondaryStat.PoseType, new Pair<Integer, Integer>(2, 0));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(booster, 0));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(n, 0));
                if (watk > 0) {
                    localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(watk, 0));
                }
                if (!applyto.getBuffedValue(11101022)) {
                    break;
                }
                applyto.cancelEffect(applyto.getBuffedEffect(11101022));
                break;
            }
            case 11121005: {
                localstatups.clear();
                localstatups.put(SecondaryStat.GlimmeringTime, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-2, localDuration));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(35, localDuration));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(25, localDuration));
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(45, localDuration));
                break;
            }
            case 11121012: {
                aftercancel = true;
                localstatups.clear();
                localstatups.put(SecondaryStat.PoseType, new Pair<Integer, Integer>(1, 0));
                localstatups.put(SecondaryStat.Buckshot, new Pair<Integer, Integer>(SkillFactory.getSkill((int) 11101022).getEffect((int) applyto.getSkillLevel((int) 11101022)).x, 0));
                break;
            }
            case 11121011: {
                aftercancel = true;
                localstatups.clear();
                localstatups.put(SecondaryStat.PoseType, new Pair<Integer, Integer>(2, 0));
                localstatups.put(SecondaryStat.Buckshot, new Pair<Integer, Integer>(SkillFactory.getSkill((int) 11101022).getEffect((int) applyto.getSkillLevel((int) 11101022)).x, 0));
                break;
            }
            case 11121014: {
                showEffect = false;
                break;
            }
            case 12000022:
            case 12100026:
            case 0xB8C8C8:
            case 12120007: {
                localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 12111023: {
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.FlareTrick, new Pair<Integer, Integer>(this.y, localDuration));
                    break;
                }
                bufftimeR = false;
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, this.x * 1000));
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, this.x * 1000));
                break;
            }
            case 12121005: {
                localstatups.clear();
                if (primary) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(Integer.valueOf(this.indieBooster), localDuration));
                break;
            }
            case 12121052: {
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 3000));
                break;
            }
            case 14110030: {
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.ReviveOnce, new Pair<Integer, Integer>(this.x, 0));
                    break;
                }
                aftercancel = true;
                bufftimeR = false;
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 7000));
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 7000));
                break;
            }
            case 14120009: {
                if (applyto.siphonVitality == this.x) {
                    localstatups.put(SecondaryStat.SiphonVitality, new Pair<Integer, Integer>(applyto.siphonVitality, this.subTime));
                    applyto.siphonVitality = 0;
                    SkillFactory.getSkill(14120011).getEffect(this.level).applyTo(applyto);
                    break;
                }
                ++applyto.siphonVitality;
                localstatups.put(SecondaryStat.SiphonVitality, new Pair<Integer, Integer>(applyto.siphonVitality, this.subTime));
                break;
            }
            case 14120011: {
                if (applyto.getBuffedValue(SecondaryStat.Protective) == null) {
                    applyto.setSkillCustomInfo(14120011, SkillFactory.getSkill((int) 14120009).getEffect((int) applyfrom.getSkillLevel((int) 14120009)).y, 0L);
                    if (applyto.getSkillLevel(14120049) > 0) {
                        applyto.setSkillCustomInfo(14120011, applyto.getSkillCustomValue0(14120011) + (long) SkillFactory.getSkill(14120049).getEffect(1).getX(), 0L);
                    }
                }
                localstatups.put(SecondaryStat.Protective, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(14120011), localDuration));
                if (applyto.getSkillLevel(14120050) > 0) {
                    localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(SkillFactory.getSkill(14120050).getEffect(1).getX(), localDuration));
                }
                if (applyto.getSkillLevel(14120051) <= 0) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(SkillFactory.getSkill(14120051).getEffect(1).getX(), localDuration));
                break;
            }
            case 14121004: {
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(100, this.x * 1000));
                break;
            }
            case 14121052: {
                bufftimeR = false;
                localstatups.clear();
                if (primary) {
                    aftercancel = true;
                    localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 3500));
                    localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 3500));
                    this.applyTo(applyto, false);
                    break;
                }
                aftercancel = true;
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(20, localDuration));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(100, localDuration));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(100, localDuration));
                localstatups.put(SecondaryStat.Dominion, new Pair<Integer, Integer>(700, localDuration));
                break;
            }
            case 15111022: {
                if (applyto.lightning < 0) {
                    applyto.lightning = 0;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(applyto.lightning * this.y, localDuration));
                break;
            }
            case 15120003: {
                if (applyto.lightning < 0) {
                    applyto.lightning = 0;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(applyto.lightning * this.y, localDuration));
                break;
            }
            case 15121052: {
                localDuration = 2000;
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 15001022: {
                bufftimeR = false;
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.CygnusElementSkill, new Pair<Integer, Integer>(1, 0));
                    break;
                }
                localstatups.put(SecondaryStat.IgnoreTargetDEF, new Pair<Integer, Integer>((applyto.getSkillLevel(15121054) > 0 ? 9 : this.x) * applyto.lightning, this.y * 1000));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>((applyto.getSkillLevel(15121054) > 0 ? 5 : 0) * applyto.lightning, this.y * 1000));
                break;
            }
            case 21100015:
            case 21120021: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.AranSmashSwing, new Pair<Integer, Integer>(983040, 4999));
                break;
            }
            case 21121017: {
                applyto.cancelEffectFromBuffStat(SecondaryStat.BeyondNextAttackProb);
                break;
            }
            case 21110016: {
                applyto.setCombo((short) 500);
                SkillFactory.getSkill(21000000).getEffect(applyfrom.getSkillLevel(21000000)).applyTo(applyfrom, false);
                localDuration = 15000;
                if (applyfrom.getSkillLevel(21120064) > 0) {
                    localDuration += SkillFactory.getSkill(21120064).getEffect(1).getDuration();
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.AdrenalinBoost, new Pair<Integer, Integer>(150, localDuration));
                localstatups.put(SecondaryStat.AranBoostEndHunt, new Pair<Integer, Integer>(1, localDuration));
                if (primary) {
                    applyto.setSkillCustomInfo(21110016, 1L, 0L);
                    break;
                }
                applyto.setSkillCustomInfo(21110016, 0L, 0L);
                break;
            }
            case 21111030: {
                localstatups.clear();
                if (applyto.getBuffedEffect(SecondaryStat.AdrenalinBoostActive) == null) {
                    localstatups.put(SecondaryStat.AdrenalinBoostActive, new Pair<Integer, Integer>(1, this.y * 1000));
                    break;
                }
                applyto.cancelEffect(this, null, true);
                SkillFactory.getSkill(21110016).getEffect(applyto.getSkillLevel(21110016)).applyTo(applyto);
                break;
            }
            case 21121058: {
                bufftimeR = false;
                applyto.setCombo((short) 500);
                localDuration = 15000;
                localstatups.clear();
                localstatups.put(SecondaryStat.AdrenalinBoost, new Pair<Integer, Integer>(150, localDuration));
                localstatups.put(SecondaryStat.AranBoostEndHunt, new Pair<Integer, Integer>(1, localDuration));
                if (primary) {
                    applyto.setSkillCustomInfo(21110016, 1L, 0L);
                    break;
                }
                applyto.setSkillCustomInfo(21110016, 0L, 0L);
                break;
            }
            case 30021237: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, this.x * 1000));
                break;
            }
            case 22171080: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 10000));
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, 10000));
                localstatups.put(SecondaryStat.RideVehicleExpire, new Pair<Integer, Integer>(1939007, 10000));
                break;
            }
            case 23110004: {
                bufftimeR = false;
                if (primary) {
                    localDuration = 15000;
                } else if (!primary && applyto.getBuffedValue(400031017)) {
                    localDuration = 0;
                } else if (!primary && !applyto.getBuffedValue(400031017)) {
                    long time = applyto.getBuffLimit(23110004);
                    localDuration = (int) time + 100;
                }
                if (localDuration > 15000) {
                    localDuration = 15000;
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.IgnisRore, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(23110005), localDuration));
                break;
            }
            case 23111005: {
                if (applyfrom.getSkillLevel(23120046) > 0) {
                    localstatups.put(SecondaryStat.DamAbsorbShield, new Pair<Integer, Integer>(this.x + SkillFactory.getSkill(23120046).getEffect(1).getX(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.DamAbsorbShield, new Pair<Integer, Integer>(this.x, localDuration));
                }
                if (applyfrom.getSkillLevel(23120047) > 0) {
                    localstatups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(this.asrR + SkillFactory.getSkill(23120047).getEffect(1).getX(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(Integer.valueOf(this.asrR), localDuration));
                }
                if (applyfrom.getSkillLevel(23120048) > 0) {
                    localstatups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(this.terR + SkillFactory.getSkill(23120048).getEffect(1).getX(), localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(Integer.valueOf(this.terR), localDuration));
                break;
            }
            case 20031205: {
                bufftimeR = false;
                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, true, false));
                localstatups.put(SecondaryStat.Invisible, new Pair<Integer, Integer>(this.z * 100, localDuration));
                break;
            }
            case 20031209:
            case 20031210: {
                showEffect = false;
                bufftimeR = false;
                int judgement = Randomizer.rand(1, this.sourceid == 20031210 ? 4 : 2);
                applyto.cancelEffect(this);
                if (judgement == 4) {
                    ++judgement;
                }
                switch (judgement) {
                    case 1: {
                        localstatups.put(SecondaryStat.Judgement, new Pair<Integer, Integer>(judgement, localDuration));
                        localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(this.v, localDuration));
                        break;
                    }
                    case 2: {
                        localstatups.put(SecondaryStat.Judgement, new Pair<Integer, Integer>(judgement, localDuration));
                        localstatups.put(SecondaryStat.DropRIncrease, new Pair<Integer, Integer>(this.w, localDuration));
                        break;
                    }
                    case 3: {
                        localstatups.put(SecondaryStat.Judgement, new Pair<Integer, Integer>(judgement, localDuration));
                        localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(this.x, localDuration));
                        localstatups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(this.y, localDuration));
                        break;
                    }
                    case 5: {
                        localstatups.put(SecondaryStat.Judgement, new Pair<Integer, Integer>(judgement, localDuration));
                        localstatups.put(SecondaryStat.DrainHp, new Pair<Integer, Integer>(this.z, localDuration));
                        break;
                    }
                }
                applyto.setCardStack((byte) 0);
                applyto.getClient().getSession().writeAndFlush((Object) CField.updateCardStack(false, applyto.getCardStack()));
                applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(applyto, 0, this.sourceid, 6, judgement - 1, -1, (byte) 0, true, null, null, null));
                MapleAtom atom = new MapleAtom(false, applyto.getId(), 1, true, applyto.getTotalSkillLevel(SkillFactory.getSkill(24120002)) > 0 ? 24120002 : 24100003, applyto.getTruePosition().x, applyto.getTruePosition().y);
                int i = 0;
                while (true) {
                    if (i >= (this.sourceid == 24100003 ? 5 : 10)) {
                        break;
                    }
                    atom.addForceAtom(new ForceAtom(2, Randomizer.rand(15, 29), Randomizer.rand(7, 11), Randomizer.rand(0, 9), 0));
                    ++i;
                }
                applyto.getMap().spawnMapleAtom(atom);
                break;
            }
            case 20040216:
            case 20040217: {
                localstatups.put(SecondaryStat.Larkness, new Pair<Integer, Integer>(applyto.getLuminusMorphUse(), localDuration));
                break;
            }
            case 20040219:
            case 20040220: {
                if (!primary) {
                    bufftimeR = false;
                }
                if (applyto.getLuminusMorph()) {
                    applyto.setLuminusMorphUse(1);
                } else {
                    applyto.setLuminusMorphUse(9999);
                }
                for (MapleCoolDownValueHolder i : applyto.getCooldowns()) {
                    if (i.skillId != 27111303 && i.skillId != 27121303) {
                        continue;
                    }
                    applyto.removeCooldown(i.skillId);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(i.skillId, 0));
                }
                localstatups.clear();
                if (!showEffect) {
                    localDuration = 10000 + SkillFactory.getSkill((int) 27120008).getEffect((int) applyto.getSkillLevel((int) 27120008)).duration;
                }
                localstatups.put(SecondaryStat.Larkness, new Pair<Integer, Integer>(applyto.getLuminusMorphUse(), localDuration));
                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(applyto.getLuminusMorphUse(), applyto.getLuminusMorph()));
                break;
            }
            case 21000000: {
                aftercancel = true;
                localstatups.put(SecondaryStat.AranCombo, new Pair<Integer, Integer>(Integer.valueOf(applyto.getCombo()), 0));
                break;
            }
            case 24111002: {
                localstatups.clear();
                if (primary) {
                    aftercancel = true;
                    localstatups.put(SecondaryStat.ReviveOnce, new Pair<Integer, Integer>(this.x, 0));
                    break;
                }
                bufftimeR = false;
                applyto.getStat().heal(applyto);
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 4000));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 4000));
                break;
            }
            case 24111003: {
                if (applyfrom.getSkillLevel(24120049) > 0) {
                    localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(this.x + 5, localDuration));
                    localstatups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(this.y + SkillFactory.getSkill(24120049).getEffect(1).getX(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(this.x, localDuration));
                    localstatups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(this.y, localDuration));
                }
                if (applyfrom.getSkillLevel(24120050) > 0) {
                    localstatups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(this.indieMhpR + SkillFactory.getSkill(24120050).getEffect(1).getX(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(this.indieMhpR), localDuration));
                }
                if (applyfrom.getSkillLevel(24120051) > 0) {
                    localstatups.put(SecondaryStat.IndieMpR, new Pair<Integer, Integer>(this.indieMmpR + SkillFactory.getSkill(24120051).getEffect(1).getX(), localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.IndieMpR, new Pair<Integer, Integer>(Integer.valueOf(this.indieMmpR), localDuration));
                break;
            }
            case 25111209: {
                bufftimeR = false;
                localstatups.clear();
                if (primary) {
                    showEffect = false;
                    aftercancel = true;
                    localstatups.put(SecondaryStat.ReviveOnce, new Pair<Integer, Integer>(100, 0));
                    break;
                }
                applyto.cancelEffect(this);
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 1000));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 1000));
                break;
            }
            case 25121209: {
                applyto.setSpiritGuard(3);
                localstatups.put(SecondaryStat.SpiritGuard, new Pair<Integer, Integer>(3, localDuration));
                break;
            }
            case 27100003: {
                localstatups.put(SecondaryStat.BlessOfDarkness, new Pair<Integer, Integer>(Integer.valueOf(applyto.getBlessofDarkness()), 0));
                aftercancel = true;
                break;
            }
            case 27111004: {
                localstatups.put(SecondaryStat.AntiMagicShell, new Pair<Integer, Integer>(3, 0));
                aftercancel = true;
                applyto.setAntiMagicShell((byte) 3);
                break;
            }
            case 27120005: {
                if (applyto.stackbuff == 0) {
                    ++applyto.stackbuff;
                }
                localstatups.put(SecondaryStat.StackBuff, new Pair<Integer, Integer>(applyto.stackbuff * this.damR, localDuration));
                break;
            }
            case 27121054: {
                bufftimeR = false;
                if (applyto.getBuffedValue(20040219) || applyto.getBuffedValue(20040220)) {
                    applyto.dropMessage(5, "\uc774\ud004\ub9ac\ube0c\ub9ac\uc5c4 \uc0c1\ud0dc\uc5d0\uc11c\ub294 \uc0ac\uc6a9\ud558\uc2e4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                    applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto));
                    applyto.removeCooldown(27121054);
                } else if (applyto.getBuffedValue(20040216)) {
                    SkillFactory.getSkill(20040220).getEffect(1).applyTo(applyto, false);
                    applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(applyto.getLuminusMorphUse(), applyto.getLuminusMorph()));
                } else {
                    SkillFactory.getSkill(20040219).getEffect(1).applyTo(applyto, false);
                    applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(applyto.getLuminusMorphUse(), applyto.getLuminusMorph()));
                }
                return;
            }
            case 30010230: {
                aftercancel = true;
                localstatups.put(SecondaryStat.OverloadCount, new Pair<Integer, Integer>(applyto.getExceed(), 0));
                break;
            }
            case 31011001: {
                int IndiePmdR = 0;
                int maxcount = applyto.getSkillLevel(31220044) > 0 ? 18 : 20;
                int heal = (int) Math.floor((double) applyto.getExceed() / (double) maxcount * 100.0);
                double d = applyto.getSkillLevel(31210006) > 0 ? 1.25 : 0.625;
                IndiePmdR = (int) Math.floor((double) applyto.getExceed() * d);
                if (maxcount == applyto.getExceed()) {
                    heal = 100;
                    IndiePmdR = applyto.getSkillLevel(31210006) > 0 ? 25 : 15;
                }
                applyto.setExceed((short) 0);
                applyto.cancelEffectFromBuffStat(SecondaryStat.OverloadCount);
                HashMap<SecondaryStat, Pair<Integer, Integer>> cancelList = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                cancelList.put(SecondaryStat.ExceedOverload, new Pair<Integer, Integer>(1, 0));
                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(cancelList, applyto));
                long healhp = applyto.getStat().getCurrentMaxHp() / 100L * (long) heal;
                if (applyto.getBuffedEffect(SecondaryStat.DemonFrenzy) != null) {
                    healhp = healhp / 100L * (long) this.y;
                }
                applyto.setSkillCustomInfo(30010232, 0L, 0L);
                applyto.addHP(healhp, applyto.getBuffedEffect(SecondaryStat.DemonFrenzy) != null, false);
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(IndiePmdR, localDuration));
                break;
            }
            case 31111003: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 2000));
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 2000));
                break;
            }
            case 31120046: {
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, localDuration * this.y / 100));
                localstatups.put(SecondaryStat.IgnorePImmune, new Pair<Integer, Integer>(1, localDuration * this.y / 100));
                break;
            }
            case 31121005: {
                localstatups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(this.getIndieMhpR()), localDuration));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                localstatups.put(SecondaryStat.DevilishPower, new Pair<Integer, Integer>(this.damage / 10, localDuration));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 3000));
                localstatups.put(SecondaryStat.IgnoreAllImmune, new Pair<Integer, Integer>(1, 3000));
                if (applyfrom.getSkillLevel(31120046) <= 0) {
                    break;
                }
                SkillFactory.getSkill(31120046).getEffect(1).applyTo(applyfrom, localDuration);
                break;
            }
            case 31211003: {
                if (applyfrom.getSkillLevel(31220046) > 0) {
                    localstatups.put(SecondaryStat.DamAbsorbShield, new Pair<Integer, Integer>(this.x + SkillFactory.getSkill(31220046).getEffect(1).getX(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.DamAbsorbShield, new Pair<Integer, Integer>(this.x, localDuration));
                }
                if (applyfrom.getSkillLevel(31220047) > 0) {
                    localstatups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(this.y + SkillFactory.getSkill(31220047).getEffect(1).getX(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(this.y, localDuration));
                }
                if (applyfrom.getSkillLevel(31220048) > 0) {
                    localstatups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(this.z + SkillFactory.getSkill(31220048).getEffect(1).getX(), localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(this.z, localDuration));
                break;
            }
            case 32001014:
            case 32100010:
            case 32110017:
            case 32120019: {
                aftercancel = true;
                localstatups.put(SecondaryStat.BMageDeath, new Pair<Integer, Integer>(Integer.valueOf(applyto.getDeath()), localDuration));
                break;
            }
            case 32001016: {
                localDuration = 0;
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                localstatups.put(SecondaryStat.YellowAura, new Pair<Integer, Integer>(Integer.valueOf(this.level), 0));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(this.indieSpeed), localDuration));
                if (!primary) {
                    break;
                }
                if (applyto.getBuffedValue(32101009) && applyto.getBuffedOwner(32101009) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DrainAura, 32101009);
                }
                if (applyto.getBuffedValue(32111012) && applyto.getBuffedOwner(32111012) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.BlueAura, 32111012);
                }
                if (applyto.getBuffedValue(32121017) && applyto.getBuffedOwner(32121017) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DarkAura, 32121017);
                }
                if (applyto.getBuffedValue(32121018) && applyto.getBuffedOwner(32121018) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DebuffAura, 32121018);
                }
                if (!applyto.getBuffedValue(400021006) || applyto.getBuffedOwner(400021006) != applyto.getId()) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.UnionAura, 400021006);
                break;
            }
            case 32101009: {
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                localstatups.put(SecondaryStat.DrainAura, new Pair<Integer, Integer>(Integer.valueOf(this.level), 0));
                if (!primary) {
                    break;
                }
                if (applyto.getBuffedValue(32001016) && applyto.getBuffedOwner(32001016) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.YellowAura, 32001016);
                }
                if (applyto.getBuffedValue(32111012) && applyto.getBuffedOwner(32111012) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.BlueAura, 32111012);
                }
                if (applyto.getBuffedValue(32121017) && applyto.getBuffedOwner(32121017) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DarkAura, 32121017);
                }
                if (applyto.getBuffedValue(32121018) && applyto.getBuffedOwner(32121018) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DebuffAura, 32121018);
                }
                if (!applyto.getBuffedValue(400021006) || applyto.getBuffedOwner(400021006) != applyto.getId()) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.UnionAura, 400021006);
                break;
            }
            case 32111012: {
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                localDuration = 0;
                localstatups.put(SecondaryStat.BlueAura, new Pair<Integer, Integer>(Integer.valueOf(this.level), 0));
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(this.indieAsrR), localDuration));
                if (!primary) {
                    break;
                }
                if (applyto.getBuffedValue(32001016) && applyto.getBuffedOwner(32001016) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.YellowAura, 32001016);
                }
                if (applyto.getBuffedValue(32101009) && applyto.getBuffedOwner(32101009) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DrainAura, 32101009);
                }
                if (applyto.getBuffedValue(32121017) && applyto.getBuffedOwner(32121017) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DarkAura, 32121017);
                }
                if (applyto.getBuffedValue(32121018) && applyto.getBuffedOwner(32121018) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DebuffAura, 32121018);
                }
                if (!applyto.getBuffedValue(400021006) || applyto.getBuffedOwner(400021006) != applyto.getId()) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.UnionAura, 400021006);
                break;
            }
            case 32111016: {
                localDuration = 0;
                localstatups.put(SecondaryStat.DarkLighting, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 32121017: {
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                localDuration = 0;
                localstatups.put(SecondaryStat.DarkAura, new Pair<Integer, Integer>(Integer.valueOf(this.level), 0));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                if (applyfrom.getId() == applyto.getId() && applyto.getSkillLevel(32120060) > 0) {
                    localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 32120060).getEffect((int) 1).indieBDR), localDuration));
                }
                if (!primary) {
                    break;
                }
                if (applyto.getBuffedValue(32001016) && applyto.getBuffedOwner(32001016) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.YellowAura, 32001016);
                }
                if (applyto.getBuffedValue(32101009) && applyto.getBuffedOwner(32101009) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DrainAura, 32101009);
                }
                if (applyto.getBuffedValue(32111012) && applyto.getBuffedOwner(32111012) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.BlueAura, 32111012);
                }
                if (applyto.getBuffedValue(32121018) && applyto.getBuffedOwner(32121018) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DebuffAura, 32121018);
                }
                if (!applyto.getBuffedValue(400021006) || applyto.getBuffedOwner(400021006) != applyto.getId()) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.UnionAura, 400021006);
                break;
            }
            case 32121018: {
                localstatups.put(SecondaryStat.DebuffAura, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                if (!primary) {
                    break;
                }
                if (applyto.getBuffedValue(32001016) && applyto.getBuffedOwner(32001016) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.YellowAura, 32001016);
                }
                if (applyto.getBuffedValue(32101009) && applyto.getBuffedOwner(32101009) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DrainAura, 32101009);
                }
                if (applyto.getBuffedValue(32121017) && applyto.getBuffedOwner(32121017) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DarkAura, 32121017);
                }
                if (applyto.getBuffedValue(32111012) && applyto.getBuffedOwner(32111012) == applyto.getId()) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.BlueAura, 32111012);
                }
                if (!applyto.getBuffedValue(400021006) || applyto.getBuffedOwner(400021006) != applyto.getId()) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.UnionAura, 400021006);
                break;
            }
            case 32120045: {
                if (applyfrom.getBuffedValue(SecondaryStat.TeleportMastery) != null) {
                    applyfrom.cancelEffectFromBuffStat(SecondaryStat.TeleportMastery);
                }
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, this.w * 1000));
                break;
            }
            case 33001001: {
                if (!primary) {
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(this.x, 10000));
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.JaguarSummoned);
                break;
            }
            case 33111007: {
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(this.x, localDuration));
                if (applyfrom.getSkillLevel(33120043) > 0) {
                    localstatups.put(SecondaryStat.BeastFormDamage, new Pair<Integer, Integer>(this.z + SkillFactory.getSkill(33120043).getEffect(1).getZ(), localDuration));
                } else {
                    localstatups.put(SecondaryStat.BeastFormDamage, new Pair<Integer, Integer>(this.z, localDuration));
                }
                if (applyfrom.getSkillLevel(33120044) > 0) {
                    localstatups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 33120044).getEffect((int) 1).mhpR), localDuration));
                }
                if (applyfrom.getSkillLevel(33120045) > 0) {
                    localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-this.w - SkillFactory.getSkill(33120045).getEffect(1).getW(), localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-this.w, localDuration));
                break;
            }
            case 33110014: {
                aftercancel = true;
                int size = 0;
                for (final String str : applyto.getInfoQuest(23008).split(";")) {
                    if (str.contains("=1")) {
                        ++size;
                    }
                }
                localstatups.put(SecondaryStat.JaguarCount, new Pair<Integer, Integer>(size * (this.y << 8) + this.z * size, 0));
                break;
            }
            case 35001002: {
                localstatups.clear();
                short pad = this.epad;
                short pdd = this.epdd;
                if (applyfrom.getSkillLevel(35120000) > 0) {
                    pad = SkillFactory.getSkill((int) 35120000).getEffect((int) applyto.getSkillLevel((int) 35120000)).epad;
                    pdd = SkillFactory.getSkill((int) 35120000).getEffect((int) applyto.getSkillLevel((int) 35120000)).epdd;
                }
                localstatups.put(SecondaryStat.EnhancedPad, new Pair<Integer, Integer>(Integer.valueOf(pad), localDuration));
                localstatups.put(SecondaryStat.EnhancedPdd, new Pair<Integer, Integer>(Integer.valueOf(pdd), localDuration));
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(30, localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                localstatups.put(SecondaryStat.Mechanic, new Pair<Integer, Integer>(30, localDuration));
                if (!applyto.getBuffedValue(35111003)) {
                    break;
                }
                applyto.cancelEffect(SkillFactory.getSkill(35111003).getEffect(1), null, true);
                break;
            }
            case 35111002: {
                bufftimeR = false;
                if (applyto.getCooldownLimit(35111002) <= 0L) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 35111003: {
                localstatups.clear();
                short pad = this.epad;
                short pdd = this.epdd;
                if (applyfrom.getSkillLevel(35120000) > 0) {
                    pad = SkillFactory.getSkill((int) 35120000).getEffect((int) applyto.getSkillLevel((int) 35120000)).epad;
                    pdd = SkillFactory.getSkill((int) 35120000).getEffect((int) applyto.getSkillLevel((int) 35120000)).epdd;
                }
                localstatups.put(SecondaryStat.EnhancedPad, new Pair<Integer, Integer>(Integer.valueOf(pad), localDuration));
                localstatups.put(SecondaryStat.EnhancedPdd, new Pair<Integer, Integer>(Integer.valueOf(pdd), localDuration));
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(30, localDuration));
                localstatups.put(SecondaryStat.CriticalIncrease, new Pair<Integer, Integer>(this.cr, localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                localstatups.put(SecondaryStat.Mechanic, new Pair<Integer, Integer>(30, localDuration));
                if (!applyto.getBuffedValue(35001002)) {
                    break;
                }
                applyto.cancelEffect(SkillFactory.getSkill(35001002).getEffect(1), null, true);
                break;
            }
            case 35120002: {
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(this.z, localDuration));
                break;
            }
            case 36111003: {
                applyto.stackbuff = !primary ? --applyto.stackbuff : this.x;
                localstatups.put(SecondaryStat.DamAbsorbShield, new Pair<Integer, Integer>(this.z, primary ? this.duration : (int) applyto.getBuffLimit(this.sourceid)));
                localstatups.put(SecondaryStat.StackBuff, new Pair<Integer, Integer>(this.x, primary ? this.duration : (int) applyto.getBuffLimit(this.sourceid)));
                break;
            }
            case 36121052: {
                localstatups.clear();
                bufftimeR = false;
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 2000));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 2000));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.w, this.y * 1000));
                break;
            }
            case 36121054: {
                bufftimeR = false;
                if (applyto.getXenonSurplus() < 20) {
                    applyto.setXenonSurplus((short) 20, SkillFactory.getSkill(this.sourceid));
                    applyto.updateXenonSurplus((short) 20, SkillFactory.getSkill(this.sourceid));
                }
                localstatups.put(SecondaryStat.AmaranthGenerator, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 37000006: {
                aftercancel = true;
                localstatups.clear();
                localstatups.put(SecondaryStat.RwBarrier, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(37000006), 0));
                localDuration = 0;
                break;
            }
            case 37100002:
            case 37110004: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.RWMovingEvar, new Pair<Integer, Integer>(this.x, 1500));
                break;
            }
            case 37110009: {
                if (applyto.getBuffedValue(SecondaryStat.RWCombination) == null) {
                    applyto.combinationBuff = 0;
                }
                if (applyto.combinationBuff < this.x) {
                    ++applyto.combinationBuff;
                }
                localstatups.put(SecondaryStat.RWCombination, new Pair<Integer, Integer>(applyto.combinationBuff, localDuration));
                if (applyto.combinationBuff < this.z) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                break;
            }
            case 37120012: {
                if (applyto.getBuffedValue(SecondaryStat.RWCombination) == null && primary) {
                    applyto.combinationBuff = 0;
                }
                if (applyto.combinationBuff < this.x) {
                    ++applyto.combinationBuff;
                }
                if (applyto.combinationBuff == 15) {
                    applyto.combinationBuff = 10;
                    localDuration = 0;
                }
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(this.q * applyto.combinationBuff, localDuration));
                localstatups.put(SecondaryStat.RWCombination, new Pair<Integer, Integer>(applyto.combinationBuff, localDuration));
                if (applyto.combinationBuff < this.z) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                break;
            }
            case 37121005: {
                localstatups.put(SecondaryStat.RWBarrierHeal, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 37121055: {
                localstatups.put(SecondaryStat.RwMagnumBlow, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 37121056: {
                localstatups.put(SecondaryStat.RwMagnumBlow, new Pair<Integer, Integer>(2, localDuration));
                break;
            }
            case 37121057: {
                localstatups.put(SecondaryStat.RwMagnumBlow, new Pair<Integer, Integer>(3, localDuration));
                break;
            }
            case 37121058: {
                localstatups.put(SecondaryStat.RwMagnumBlow, new Pair<Integer, Integer>(4, localDuration));
                break;
            }
            case 37120059: {
                localstatups.put(SecondaryStat.RwMagnumBlow, new Pair<Integer, Integer>(4, localDuration));
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 2000));
                break;
            }
            case 51001005: {
                int pad = 0;
                if (applyto.getRoyalStack() <= 0 || applyto.getRoyalStack() > 5) {
                    applyto.setRoyalStack((byte) 1);
                }
                switch (applyto.getRoyalStack()) {
                    case 1: {
                        pad = 10;
                        break;
                    }
                    case 2: {
                        pad = 15;
                        break;
                    }
                    case 3: {
                        pad = 20;
                        break;
                    }
                    case 4: {
                        pad = 30;
                        break;
                    }
                    case 5: {
                        pad = 45;
                        break;
                    }
                }
                bufftimeR = false;
                if (primary) {
                    localDuration = this.x * 1000;
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(pad, localDuration));
                localstatups.put(SecondaryStat.RoyalGuardState, new Pair<Integer, Integer>(Integer.valueOf(applyto.getRoyalStack()), localDuration));
                if (!primary) {
                    break;
                }
                SkillFactory.getSkill(51001006).getEffect(1).applyTo(applyto, false);
                break;
            }
            case 51001009:
            case 51001007:
            case 51001008:
            case 51001010: {
                bufftimeR = false;
                aftercancel = true;
                int duration1 = (applyto.getRoyalStack() != 5) ? ((applyto.getRoyalStack() != 4) ? ((applyto.getRoyalStack() != 3) ? ((applyto.getRoyalStack() != 2) ? 1420 : 1330) : 1280) : 1230) : 150;
                if (applyto.getBuffedValue(400011083)) {
                    duration1 += 500;
                }
                if (applyto.getBuffedValue(51121054)) {
                    duration1 += 500;
                }
                localstatups.put(SecondaryStat.RoyalGuardPrepare, new Pair<Integer, Integer>(1, duration1));
                break;
            }
            case 51001006: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 5000));
                break;
            }
            case 51001011:
            case 51001012:
            case 51001013: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 4000));
                break;
            }
            case 12120013:
            case 12120014: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IgnoreTargetDEF, new Pair<Integer, Integer>(this.y, localDuration));
                break;
            }
            case 51111004: {
                int asr = this.y;
                int ter = this.z;
                if (!primary) {
                    asr = (int) ((double) asr * 0.2);
                    ter = (int) ((double) ter * 0.2);
                }
                int dfr = this.x;
                if (applyfrom.getSkillLevel(51120044) > 0 && primary) {
                    dfr += SkillFactory.getSkill(51120044).getEffect(1).getX();
                }
                if (!primary) {
                    dfr = (int) ((double) dfr * 0.3);
                }
                if (applyfrom.getSkillLevel(51120043) > 0) {
                    localDuration += SkillFactory.getSkill(51120043).getEffect(1).getDuration();
                }
                if (applyfrom.getSkillLevel(51120045) > 0) {
                    int n = SkillFactory.getSkill(51120045).getEffect(1).getY();
                    ter += n;
                }
                localstatups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(ter, localDuration));
                localstatups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(asr, localDuration));
                localstatups.put(SecondaryStat.IncDefenseR, new Pair<Integer, Integer>(dfr, localDuration));
                break;
            }
            case 51110009: {
                int pad = 0;
                switch (applyto.getRoyalStack()) {
                    case 4: {
                        pad = this.getW2();
                        break;
                    }
                    case 5: {
                        pad = this.v;
                        break;
                    }
                }
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(pad, localDuration));
                localstatups.put(SecondaryStat.RoyalGuardState, new Pair<Integer, Integer>(Integer.valueOf(applyto.getRoyalStack()), localDuration));
                break;
            }
            case 51111008: {
                localDuration = 0;
                int pad2 = 0;
                localstatups.clear();
                if (applyfrom.getId() == applyto.getId()) {
                    int size = 1;
                    if (applyto.getParty() != null) {
                        for (MaplePartyCharacter maplePartyCharacter : applyto.getParty().getMembers()) {
                            MapleCharacter chr2;
                            if (!maplePartyCharacter.isOnline() || !(chr2 = applyfrom.getClient().getChannelServer().getPlayerStorage().getCharacterByName(maplePartyCharacter.getName())).getBuffedValue(51111008) && (applyfrom.getTruePosition().x + this.getLt().x >= chr2.getTruePosition().x || applyfrom.getTruePosition().x - this.getLt().x <= chr2.getTruePosition().x || applyfrom.getTruePosition().y + this.getLt().y >= chr2.getTruePosition().y || applyfrom.getTruePosition().y - this.getLt().y <= chr2.getTruePosition().y)) {
                                continue;
                            }
                            ++size;
                        }
                        applyfrom.setSkillCustomInfo(51111009, size, 0L);
                    }
                    localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.indieDamR * (applyto.getParty() == null ? 1 : size), 0));
                } else {
                    aftercancel = true;
                    MapleCharacter leader = null;
                    for (MapleCharacter mapleCharacter : applyto.getMap().getAllChracater()) {
                        if (mapleCharacter.getId() != applyfrom.getId()) {
                            continue;
                        }
                        leader = mapleCharacter;
                        break;
                    }
                    if (leader != null) {
                        if (leader.getBuffedValue(51111004)) {
                            localstatups.put(SecondaryStat.Asr, new Pair<Integer, Integer>(20, 0));
                            localstatups.put(SecondaryStat.Ter, new Pair<Integer, Integer>(20, 0));
                            localstatups.put(SecondaryStat.IncDefenseR, new Pair<Integer, Integer>(30, 0));
                        }
                        if (leader.getBuffedValue(51001005)) {
                            switch (leader.getRoyalStack()) {
                                case 1: {
                                    pad2 = 5;
                                    break;
                                }
                                case 2: {
                                    pad2 = 8;
                                    break;
                                }
                                case 3: {
                                    pad2 = 10;
                                    break;
                                }
                                case 4: {
                                    pad2 = 15;
                                    break;
                                }
                                case 5: {
                                    pad2 = 23;
                                    break;
                                }
                            }
                            localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(pad2, 0));
                        }
                    }
                }
                localstatups.put(SecondaryStat.MichaelSoulLink, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 61111008:
            case 61120008:
            case 61121053: {
                if (this.sourceid == 61121053) {
                    SkillFactory.getSkill(61111008).getEffect(1).applyKaiserCombo(applyto, (short) 1000);
                    bufftimeR = false;
                }
                if (applyto.getBuffedValue(SecondaryStat.StopForceAtominfo) == null) {
                    break;
                }
                final int[] array2;
                final int[] skilllist = array2 = new int[]{61101002, 61110211, 61120007, 61121217};
                for (final Integer skill : array2) {
                    if (applyto.getBuffedValue(skill)) {
                        applyto.cancelEffect(applyto.getBuffedEffect(skill));
                    }
                }
                if (this.sourceid != 61120008) {
                    if (this.sourceid != 61121053) {
                        SkillFactory.getSkill(61110211).getEffect(applyto.getSkillLevel(61101002)).applyTo(applyto);
                        break;
                    }
                }
                SkillFactory.getSkill(61121217).getEffect(applyto.getSkillLevel(61120007)).applyTo(applyto);
                break;
            }
            case 61121052: {
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 2000));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 2000));
                break;
            }
            case 61121009: {
                localstatups.put(SecondaryStat.RoburstArmor, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 61121054: {
                bufftimeR = false;
                for (MapleCoolDownValueHolder i : applyto.getCooldowns()) {
                    if (SkillFactory.getSkill(i.skillId) == null || i.skillId == 61121054 || SkillFactory.getSkill(i.skillId).isHyper() || !GameConstants.isKaiser(i.skillId / 10000)) {
                        continue;
                    }
                    applyto.removeCooldown(i.skillId);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(i.skillId, 0));
                }
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(this.indiePad), localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IgnorePImmune, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 64100004:
            case 64110005:
            case 64120006: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.WeaponVariety, new Pair<Integer, Integer>(applyto.weaponChanges1.size(), localDuration));
                break;
            }
            case 64121001: {
                if (primary) {
                    localDuration = 14000;
                }
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 65101002: {
                if (primary) {
                    applyto.setBarrier(1000);
                } else {
                    bufftimeR = false;
                }
                localstatups.put(SecondaryStat.PowerTransferGauge, new Pair<Integer, Integer>(applyto.getBarrier(), localDuration));
                break;
            }
            case 65111004: {
                localstatups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(Integer.valueOf(this.prop), localDuration));
                break;
            }
            case 65120006: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.AffinitySlug, new Pair<Integer, Integer>(this.y, 5000));
                break;
            }
            case 65121101: {
                localstatups.clear();
                int value = (int) (applyto.getSkillCustomValue(65121101) == null ? 0L : applyto.getSkillCustomValue0(65121101));
                if (value >= 3) {
                    break;
                }
                applyto.setSkillCustomInfo(65121101, value + 1, 0L);
                if (applyto.getSkillCustomValue0(65121101) > 3L) {
                    applyto.setSkillCustomInfo(65121101, 3L, 0L);
                    value = (int) applyto.getSkillCustomValue0(65121101);
                }
                localstatups.put(SecondaryStat.Trinity, new Pair<Integer, Integer>(this.x * (value + 1), 7000));
                localstatups.put(SecondaryStat.DamR, new Pair<Integer, Integer>(this.x * (value + 1), 7000));
                localstatups.put(SecondaryStat.IgnoreMobPdpR, new Pair<Integer, Integer>(this.x * (value + 1), 7000));
                localDuration = 7000;
                break;
            }
            case 80000268:
            case 150000017: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.y * applyto.FlowofFight, localDuration));
                localstatups.put(SecondaryStat.FlowOfFight, new Pair<Integer, Integer>(applyto.FlowofFight, localDuration));
                break;
            }
            case 80000514:
            case 150010241: {
                bufftimeR = false;
                if (!applyto.getBuffedValue(this.sourceid)) {
                    applyto.LinkofArk = 0;
                }
                if (applyto.LinkofArk < 5) {
                    ++applyto.LinkofArk;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.y * applyto.LinkofArk, localDuration));
                localstatups.put(SecondaryStat.LinkOfArk, new Pair<Integer, Integer>(applyto.LinkofArk, localDuration));
                break;
            }
            case 80000329: {
                bufftimeR = false;
                int skilllevel = 0;
                if (applyto.getSkillLevel(30000074) > 0) {
                    skilllevel += applyto.getSkillLevel(30000074);
                } else if (applyto.getSkillLevel(30000075) > 0) {
                    skilllevel += applyto.getSkillLevel(30000075);
                } else if (applyto.getSkillLevel(30000076) > 0) {
                    skilllevel += applyto.getSkillLevel(30000076);
                } else if (applyto.getSkillLevel(30000077) > 0) {
                    skilllevel += applyto.getSkillLevel(30000077);
                }
                if (applyto.getSkillLevel(80000329) > 0) {
                    skilllevel += applyto.getSkillLevel(80000329);
                }
                if (skilllevel > 8) {
                    skilllevel = 8;
                }
                if (skilllevel <= 0) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, skilllevel * 1000));
                break;
            }
            case 80001428: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(20, localDuration));
                localstatups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(20, localDuration));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(100, localDuration));
                localstatups.put(SecondaryStat.DotHealHPPerSecond, new Pair<Integer, Integer>(10, localDuration));
                localstatups.put(SecondaryStat.DotHealMPPerSecond, new Pair<Integer, Integer>(10, localDuration));
                break;
            }
            case 80001462: {
                localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(applyto.getStat().critical_rate * this.x / 100, localDuration));
                break;
            }
            case 80001463: {
                bufftimeR = false;
                int value = 0;
                if (GameConstants.isXenon(applyto.getJob())) {
                    value = applyto.getStat().getStr() + applyto.getStat().getDex() + applyto.getStat().getLuk();
                } else if (GameConstants.isDemonAvenger(applyto.getJob())) {
                    value = (int) applyto.getStat().getCurrentMaxHp();
                } else if (GameConstants.isWarrior(applyto.getJob())) {
                    value = applyto.getStat().getStr();
                } else if (GameConstants.isMagician(applyto.getJob())) {
                    value = applyto.getStat().getInt();
                } else if (GameConstants.isArcher(applyto.getJob()) || GameConstants.isCaptain(applyto.getJob()) || GameConstants.isMechanic(applyto.getJob()) || GameConstants.isAngelicBuster(applyto.getJob())) {
                    value = applyto.getStat().getDex();
                } else if (GameConstants.isThief(applyto.getJob())) {
                    value = applyto.getStat().getLuk();
                } else if (GameConstants.isPirate(applyto.getJob())) {
                    value = applyto.getStat().getStr();
                }
                localstatups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(value * this.x / 100, localDuration));
                break;
            }
            case 80001464: {
                bufftimeR = false;
                int value = 0;
                if (GameConstants.isXenon(applyto.getJob())) {
                    value = applyto.getStat().getStr() + applyto.getStat().getDex() + applyto.getStat().getLuk();
                } else if (GameConstants.isDemonAvenger(applyto.getJob())) {
                    value = (int) applyto.getStat().getCurrentMaxHp();
                } else if (GameConstants.isWarrior(applyto.getJob())) {
                    value = applyto.getStat().getStr();
                } else if (GameConstants.isMagician(applyto.getJob())) {
                    value = applyto.getStat().getInt();
                } else if (GameConstants.isArcher(applyto.getJob()) || GameConstants.isCaptain(applyto.getJob()) || GameConstants.isMechanic(applyto.getJob()) || GameConstants.isAngelicBuster(applyto.getJob())) {
                    value = applyto.getStat().getDex();
                } else if (GameConstants.isThief(applyto.getJob())) {
                    value = applyto.getStat().getLuk();
                } else if (GameConstants.isPirate(applyto.getJob())) {
                    value = applyto.getStat().getStr();
                }
                localstatups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(value * this.x / 100, localDuration));
                break;
            }
            case 80001465: {
                bufftimeR = false;
                int value = this.x * (applyto.getStat().getStr() + applyto.getStat().getDex() + applyto.getStat().getInt() + applyto.getStat().getLuk()) / 100;
                if (GameConstants.isXenon(applyto.getJob())) {
                    localstatups.put(SecondaryStat.IndieStr, new Pair<Integer, Integer>(value / 3, localDuration));
                    localstatups.put(SecondaryStat.IndieDex, new Pair<Integer, Integer>(value / 3, localDuration));
                    localstatups.put(SecondaryStat.IndieLuk, new Pair<Integer, Integer>(value / 3, localDuration));
                    break;
                }
                if (GameConstants.isDemonAvenger(applyto.getJob())) {
                    localstatups.put(SecondaryStat.IndieHp, new Pair<Integer, Integer>(value, localDuration));
                    break;
                }
                if (GameConstants.isWarrior(applyto.getJob())) {
                    localstatups.put(SecondaryStat.IndieStr, new Pair<Integer, Integer>(value, localDuration));
                    break;
                }
                if (GameConstants.isMagician(applyto.getJob())) {
                    localstatups.put(SecondaryStat.IndieInt, new Pair<Integer, Integer>(value, localDuration));
                    break;
                }
                if (GameConstants.isArcher(applyto.getJob()) || GameConstants.isCaptain(applyto.getJob()) || GameConstants.isMechanic(applyto.getJob()) || GameConstants.isAngelicBuster(applyto.getJob())) {
                    localstatups.put(SecondaryStat.IndieDex, new Pair<Integer, Integer>(value, localDuration));
                    break;
                }
                if (GameConstants.isThief(applyto.getJob())) {
                    localstatups.put(SecondaryStat.IndieLuk, new Pair<Integer, Integer>(value, localDuration));
                    break;
                }
                if (!GameConstants.isPirate(applyto.getJob())) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieStr, new Pair<Integer, Integer>(value, localDuration));
                break;
            }
            case 80001466: {
                localstatups.put(SecondaryStat.IndieStr, new Pair<Integer, Integer>((applyto.getLevel() + 1) * this.x / 100, localDuration));
                break;
            }
            case 80001467: {
                localstatups.put(SecondaryStat.IndieDex, new Pair<Integer, Integer>((applyto.getLevel() + 1) * this.x / 100, localDuration));
                break;
            }
            case 80001468: {
                localstatups.put(SecondaryStat.IndieInt, new Pair<Integer, Integer>((applyto.getLevel() + 1) * this.x / 100, localDuration));
                break;
            }
            case 80001469: {
                localstatups.put(SecondaryStat.IndieLuk, new Pair<Integer, Integer>((applyto.getLevel() + 1) * this.x / 100, localDuration));
                break;
            }
            case 80001470: {
                Equip eq = (Equip) applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                if (eq == null) {
                    System.out.println("\ubb34\uae30 \uc5c6\uc774 \ubc84\ud504\ub97c \uc5b4\ub5bb\uac8c \uc368 \u3161\u3161 " + applyto.getName());
                    break;
                }
                eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(eq.getItemId());
                localstatups.put(SecondaryStat.IndieStr, new Pair<Integer, Integer>(eq.getWatk() * this.x / 100, localDuration));
                break;
            }
            case 80001471: {
                Equip eq = (Equip) applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                if (eq == null) {
                    System.out.println("\ubb34\uae30 \uc5c6\uc774 \ubc84\ud504\ub97c \uc5b4\ub5bb\uac8c \uc368 \u3161\u3161 " + applyto.getName());
                    break;
                }
                eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(eq.getItemId());
                localstatups.put(SecondaryStat.IndieDex, new Pair<Integer, Integer>(eq.getWatk() * this.x / 100, localDuration));
                break;
            }
            case 80001472: {
                Equip eq = (Equip) applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                if (eq == null) {
                    System.out.println("\ubb34\uae30 \uc5c6\uc774 \ubc84\ud504\ub97c \uc5b4\ub5bb\uac8c \uc368 \u3161\u3161 " + applyto.getName());
                    break;
                }
                eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(eq.getItemId());
                localstatups.put(SecondaryStat.IndieInt, new Pair<Integer, Integer>(eq.getMatk() * this.x / 100, localDuration));
                break;
            }
            case 80001473: {
                Equip eq = (Equip) applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                if (eq == null) {
                    System.out.println("\ubb34\uae30 \uc5c6\uc774 \ubc84\ud504\ub97c \uc5b4\ub5bb\uac8c \uc368 \u3161\u3161 " + applyto.getName());
                    break;
                }
                eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(eq.getItemId());
                localstatups.put(SecondaryStat.IndieLuk, new Pair<Integer, Integer>(eq.getWatk() * this.x / 100, localDuration));
                break;
            }
            case 80001474: {
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-2, localDuration));
                break;
            }
            case 80001475: {
                localstatups.put(SecondaryStat.IgnoreAllCounter, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IgnoreAllImmune, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80001476: {
                localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(this.indieIgnoreMobpdpR), localDuration));
                localstatups.put(SecondaryStat.IndiePddR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePddR), localDuration));
                break;
            }
            case 80001477: {
                localstatups.put(SecondaryStat.ReflectDamR, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 80001479: {
                localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                localstatups.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(Integer.valueOf(this.indieMadR), localDuration));
                break;
            }
            case 80001543: {
                aftercancel = true;
                localDuration = 0;
                localstatups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(500, 0));
                localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(300, 0));
                localstatups.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(300, 0));
                localstatups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(20, 0));
                localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(20, 0));
                localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(20, 0));
                localstatups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(10, 0));
                break;
            }
            case 80002543: {
                localDuration = 0;
                localstatups.put(SecondaryStat.DebuffIncHp, new Pair<Integer, Integer>(50, localDuration));
                break;
            }
            case 80002758: {
                for (int i = 1; i <= 3; ++i) {
                    Timer.BuffTimer.getInstance().schedule(() -> applyto.addHP(this.y), 1000 * i);
                }
                break;
            }
            case 80002762: {
                if (applyto.empiricalStack < this.x) {
                    ++applyto.empiricalStack;
                }
                localstatups.put(SecondaryStat.EmpiricalKnowledge, new Pair<Integer, Integer>(applyto.empiricalStack, localDuration));
                break;
            }
            case 80002770: {
                if (applyto.skillisCooling(80002770)) {
                    break;
                }
                applyto.addCooldown(80002770, System.currentTimeMillis(), 20000L);
                applyto.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(80002770, 20000));
                SkillFactory.getSkill(261).getEffect(this.level).applyTo(applyto);
                break;
            }
            case 261: {
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                break;
            }
            case 100001261: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, 5000));
                break;
            }
            case 0x5F5E5EF: {
                if (primary) {
                    localDuration = 0;
                    localstatups.put(SecondaryStat.ZeroAuraStr, new Pair<Integer, Integer>(1, 0));
                }
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(this.indiePad), localDuration));
                localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(this.indieMad), localDuration));
                localstatups.put(SecondaryStat.IndiePdd, new Pair<Integer, Integer>(Integer.valueOf(this.indiePdd), localDuration));
                localstatups.put(SecondaryStat.IndieTerR, new Pair<Integer, Integer>(Integer.valueOf(this.indieTerR), localDuration));
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(this.indieAsrR), localDuration));
                if (applyto.getBuffedValue(SecondaryStat.ZeroAuraSpd) == null) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.ZeroAuraSpd, 100001264);
                break;
            }
            case 100001264: {
                if (primary) {
                    localDuration = 0;
                    localstatups.put(SecondaryStat.ZeroAuraSpd, new Pair<Integer, Integer>(1, 0));
                }
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(this.indieSpeed), localDuration));
                localstatups.put(SecondaryStat.IndieJump, new Pair<Integer, Integer>(Integer.valueOf(this.indieJump), localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                if (applyto.getBuffedValue(SecondaryStat.ZeroAuraStr) == null) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.ZeroAuraStr, 0x5F5E5EF);
                break;
            }
            case 100000276: {
                bufftimeR = false;
                aftercancel = true;
                localstatups.clear();
                localDuration = applyto.getBuffedValue(400001045) ? 0 : 20000;
                localstatups.put(SecondaryStat.TimeFastABuff, new Pair<Integer, Integer>(Integer.valueOf(applyto.RapidTimeDetect), localDuration));
                break;
            }
            case 100000277: {
                bufftimeR = false;
                aftercancel = true;
                localstatups.clear();
                localDuration = applyto.getBuffedValue(400001045) ? 0 : 20000;
                localstatups.put(SecondaryStat.TimeFastBBuff, new Pair<Integer, Integer>(Integer.valueOf(applyto.RapidTimeStrength), localDuration));
                if (applyto.RapidTimeStrength != 10) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                break;
            }
            case 100001272: {
                bufftimeR = false;
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.ReviveOnce, new Pair<Integer, Integer>(100, 0));
                    break;
                }
                localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 2000));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 2000));
                break;
            }
            case 100001274: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieBlockSkill, new Pair<Integer, Integer>(1, localDuration));
                for (MapleCoolDownValueHolder m : applyto.getCooldowns()) {
                    int skil = m.skillId;
                    if ((skil / 10000 == 40001 || skil / 10000 == 10000) && skil != 100001005 && skil != 100001261 && skil != 100001283 || skil == 100001274) {
                        continue;
                    }
                    applyto.removeCooldown(skil);
                    applyto.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(skil, 0));
                }
                if (applyto.getLevel() < 200) {
                    break;
                }
                SkillFactory.getSkill(100001281).getEffect(1).applyTo(applyto, false);
                break;
            }
            case 100001281: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                break;
            }
            case 101120109: {
                bufftimeR = false;
                localstatups.clear();
                if (!primary) {
                    aftercancel = true;
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 500));
                    break;
                }
                if (applyto.getBuffedValue(400001045)) {
                    localDuration = 0;
                }
                localstatups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(100, localDuration));
                localstatups.put(SecondaryStat.ImmuneBarrier, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(101120109), localDuration));
                break;
            }
            case 131001004: {
                if (primary) {
                    localstatups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(350, 0));
                    break;
                }
                localstatups.put(SecondaryStat.PinkbeanRollingGrade, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 131001306: {
                applyto.getStat().heal(applyto);
                break;
            }
            case 131001020: {
                if (primary) {
                    applyto.setSkillCustomInfo(this.sourceid, 0L, 0L);
                }
                applyto.addSkillCustomInfo(this.sourceid, 1L);
                localstatups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(200, localDuration));
                break;
            }
            case 131001113: {
                localstatups.put(SecondaryStat.MaxLevelBuff, new Pair<Integer, Integer>(Integer.valueOf(this.indieMadR), localDuration));
                break;
            }
            case 131001009: {
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(this.indieSpeed), localDuration));
                localstatups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(30, localDuration));
                localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                localstatups.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                localstatups.put(SecondaryStat.PinkBeanFighting, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 131001010:
            case 131001011: {
                if (!applyto.getBuffedValue(131001010)) {
                    applyto.removeSkillCustomInfo(this.sourceid + 100);
                }
                if (applyto.getSkillCustomValue0(this.sourceid + 100) < (long) this.u2) {
                    applyto.addSkillCustomInfo(this.sourceid + 100, 1L);
                }
                localstatups.put(SecondaryStat.PinkbeanYoYoAttackStack, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(this.sourceid + 100), localDuration));
                break;
            }
            case 131001018: {
                localstatups.put(SecondaryStat.IndieStatR, new Pair<Integer, Integer>(applyto.getLevel() / this.y, localDuration));
                break;
            }
            case 142101004: {
                if (applyto.getSkillLevel(142110009) > 0) {
                    SkillFactory.getSkill(142110009).getEffect(applyto.getSkillLevel(142110009)).applyTo(applyto, false);
                    break;
                }
                localstatups.put(SecondaryStat.IndiePdd, new Pair<Integer, Integer>(Integer.valueOf(this.indiePdd), localDuration));
                break;
            }
            case 142121030: {
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 151001004: {
                localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, localDuration / 1000));
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, localDuration / 1000));
                break;
            }
            case 151101010: {
                if (!applyto.getBuffedValue(this.sourceid)) {
                    applyto.adelResonance = 0;
                }
                if (applyto.adelResonance < this.x) {
                    ++applyto.adelResonance;
                }
                localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(this.y * applyto.adelResonance, localDuration));
                localstatups.put(SecondaryStat.AdelResonance, new Pair<Integer, Integer>(applyto.adelResonance, localDuration));
                break;
            }
            case 151121004: {
                localDuration = 8000;
                if (applyfrom.getSkillLevel(151120039) > 0) {
                    localDuration += 2000;
                }
                localstatups.put(SecondaryStat.IndieSuperStance, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieDamReduceR, new Pair<Integer, Integer>(-this.x, localDuration));
                localstatups.put(SecondaryStat.AntiMagicShell, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.DreamDowon, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieUnk1, new Pair<Integer, Integer>(1, 390));
                localstatups.put(SecondaryStat.Dike, new Pair<Integer, Integer>(1, 390));
                break;
            }
            case 151121011: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 3000 + (applyto.getSkillLevel(151120039) > 0 ? 1000 : 0)));
                break;
            }
            case 142110009: {
                localstatups.put(SecondaryStat.IndiePdd, new Pair<Integer, Integer>(Integer.valueOf(this.indiePdd), 180000));
                localstatups.put(SecondaryStat.Stance, new Pair<Integer, Integer>(this.stanceProp, 180000));
                break;
            }
            case 152001005: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, 930));
                localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, 930));
                break;
            }
            case 152100010:
            case 152110008:
            case 152120014: {
                aftercancel = true;
                bufftimeR = false;
                localstatups.put(SecondaryStat.CrystalBattery, new Pair<Integer, Integer>(1, 10000));
                break;
            }
            case 152000009: {
                bufftimeR = false;
                int max = 0;
                switch (applyto.blessMarkSkill) {
                    case 152000007: {
                        max = 3;
                        break;
                    }
                    case 152110009: {
                        max = 6;
                        break;
                    }
                    case 152120012: {
                        max = 10;
                        break;
                    }
                }
                if (primary && applyto.blessMark < max) {
                    ++applyto.blessMark;
                }
                if (applyto.blessMark <= 0) {
                    break;
                }
                int up = 0;
                for (int i = 0; i < applyto.blessMark; ++i) {
                    int n = 0;
                    if (applyto.blessMarkSkill == 152000007) {
                        n = 2;
                    } else if (applyto.blessMarkSkill == 152110009) {
                        n = i < 3 ? 2 : 4;
                    } else if (applyto.blessMarkSkill == 152120012) {
                        n = i < 3 ? 2 : (i < 6 ? 4 : (i < 9 ? 6 : 10));
                    }
                    up += n;
                }
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(up, localDuration));
                localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(up, localDuration));
                localstatups.put(SecondaryStat.BlessMark, new Pair<Integer, Integer>(applyto.blessMark, localDuration));
                break;
            }
            case 152111003: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(this.indieBDR), localDuration));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(this.indieStance), localDuration));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.GloryWing, new Pair<Integer, Integer>(1, localDuration));
                int size = applyfrom.getSummons(400021068).size();
                if (size > 0) {
                    localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(SkillFactory.getSkill(400021068).getEffect(applyfrom.getSkillLevel(400021068)).getU() * size, localDuration));
                }
                ArrayList<MapleSummon> remove = new ArrayList<MapleSummon>();
                for (MapleSummon mapleSummon : applyfrom.getSummons(400021068)) {
                    remove.add(mapleSummon);
                }
                for (MapleSummon mapleSummon : remove) {
                    mapleSummon.removeSummon(applyfrom.getMap(), false);
                }
                applyto.blessMark = 10;
                SkillFactory.getSkill(152000009).getEffect(applyto.getSkillLevel(152000009)).applyTo(applyto, Integer.MAX_VALUE);
                applyto.canUseMortalWingBeat = true;
                break;
            }
            case 152111007: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.HarmonyLink, new Pair<Integer, Integer>(this.x, 15000));
                break;
            }
            case 152001002:
            case 152120003: {
                bufftimeR = false;
                showEffect = false;
                localstatups.put(SecondaryStat.IncreaseJabelinDam, new Pair<Integer, Integer>(this.y, 2000));
                localstatups.put(SecondaryStat.IndieUnk1, new Pair<Integer, Integer>(1, 2000));
                if (applyto.getSkillLevel(152120012) > 0) {
                    applyto.blessMarkSkill = 152120012;
                } else if (applyto.getSkillLevel(152110009) > 0) {
                    applyto.blessMarkSkill = 152110009;
                } else if (applyto.getSkillLevel(152000007) > 0) {
                    applyto.blessMarkSkill = 152000007;
                }
                if (applyto.blessMarkSkill == 0) {
                    break;
                }
                SkillFactory.getSkill(152000009).getEffect(applyto.getSkillLevel(152000009)).applyTo(applyto);
                break;
            }
            case 152121043: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, Math.max(1, 4000 + applyfrom.blessMark * 600)));
                break;
            }
            case 152121011: {
                localstatups.put(SecondaryStat.IndieUnkIllium, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.FastCharge, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 155001103: {
                if (localDuration <= 1) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>((localDuration - 1) * this.y, this.z * 1000));
                break;
            }
            case 155001205: {
                localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, localDuration / 1000));
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, localDuration / 1000));
                break;
            }
            case 155000007: {
                localDuration = 0;
                localstatups.put(SecondaryStat.SpectorTransForm, new Pair<Integer, Integer>(1, 0));
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(30, 0));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(100, 0));
                break;
            }
            case 155001001: {
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(this.speed * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(this.indieStance * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                break;
            }
            case 155101003: {
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(this.indiePad * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(this.indieCr * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                break;
            }
            case 155111005: {
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(applyto.getBuffedValue(155121043) ? -2 : -1, localDuration));
                localstatups.put(SecondaryStat.IndieEvaR, new Pair<Integer, Integer>(this.indieEvaR * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                break;
            }
            case 155111306: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 155120014: {
                localstatups.put(SecondaryStat.FightJazz, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(155120015), localDuration));
                break;
            }
            case 155121005: {
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.indieDamR * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(this.indieBDR * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(SkillFactory.getSkill((int) 155121102).getEffect((int) applyto.getSkillLevel((int) 155121102)).s2 * (applyto.getBuffedValue(155121043) ? 2 : 1), localDuration));
                break;
            }
            case 155121043: {
                localstatups.put(SecondaryStat.ChargeSpellAmplification, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 164001004: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, 650));
                localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, 650));
                break;
            }
            case 164101003: {
                applyto.giveHoyoungGauge(this.sourceid);
                localstatups.put(SecondaryStat.Alterego, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 164111007:
            case 164121006: {
                applyto.giveHoyoungGauge(this.sourceid);
                break;
            }
            case 164121007: {
                applyto.giveHoyoungGauge(this.sourceid);
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                localstatups.put(SecondaryStat.ButterflyDream, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 164121008:
            case 400041050: {
                applyto.giveHoyoungGauge(this.sourceid);
                break;
            }
            case 164121041: {
                localstatups.put(SecondaryStat.Sungi, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 164121042: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, this.y * 1000));
                localstatups.put(SecondaryStat.DreamDowon, new Pair<Integer, Integer>(1, this.y * 1000));
                break;
            }
            case 400001010: {
                applyto.setBlitzShield((int) (applyto.getStat().getCurrentMaxHp() * (long) this.x / 100L));
                localstatups.put(SecondaryStat.BlitzShield, new Pair<Integer, Integer>(applyto.getBlitzShield(), localDuration));
                break;
            }
            case 400001014:
            case 400001015: {
                localstatups.clear();
                localstatups.put(SecondaryStat.HeavensDoor, new Pair<Integer, Integer>(1, this.x * 1000));
                break;
            }
            case 400001017: {
                localstatups.clear();
                if (primary) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieAllStatR, new Pair<Integer, Integer>(Integer.valueOf(this.indieStatRBasic), 1500));
                break;
            }
            case 400001045: {
                for (MapleCoolDownValueHolder cooldown : applyto.getCooldowns()) {
                    if (cooldown.skillId == this.sourceid || !GameConstants.isZero(cooldown.skillId / 10000) || SkillFactory.getSkill(cooldown.skillId).isHyper() || !applyto.skillisCooling(cooldown.skillId)) {
                        continue;
                    }
                    applyto.removeCooldown(cooldown.skillId);
                }
                localstatups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(this.x, localDuration));
                localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(Integer.valueOf(this.indiePad), localDuration));
                break;
            }
            case 400001050: {
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                localstatups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400011010: {
                if (!primary) {
                    aftercancel = true;
                }
                int indiePmdR = Math.round((100 - applyto.getStat().getHPPercent()) / this.u) * this.x;
                localDuration = 0;
                if (indiePmdR <= 0) {
                    indiePmdR = 1;
                }
                localstatups.put(SecondaryStat.DemonFrenzy, new Pair<Integer, Integer>(indiePmdR, localDuration));
                break;
            }
            case 400011011: {
                localstatups.clear();
                if (applyto.getId() == applyfrom.getId()) {
                    if (applyfrom.getBuffedEffect(SecondaryStat.RhoAias) != null || !primary) {
                        applyfrom.cancelEffect(applyfrom.getBuffedEffect(400011011), null, true);
                        localstatups.put(SecondaryStat.RhoAias, new Pair<Integer, Integer>(0, this.q2 * 1000));
                        localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(this.getW2() + applyfrom.getRhoAias() / this.q, this.q2 * 1000));
                        applyfrom.setRhoAias(0);
                        if (applyto.getParty() == null) {
                            break;
                        }
                        for (MaplePartyCharacter pc : applyto.getParty().getMembers()) {
                            MapleCharacter victim;
                            if (!pc.isOnline() || pc.getMapid() != applyto.getMapId() || pc.getChannel() != applyto.getClient().getChannel() || (victim = applyto.getClient().getChannelServer().getPlayerStorage().getCharacterByName(pc.getName())) == null || !victim.getBuffedValue(400011011)) {
                                continue;
                            }
                            victim.cancelEffect(victim.getBuffedEffect(400011011));
                        }
                        break;
                    }
                    applyfrom.setRhoAias(this.y + this.w + this.z);
                    localstatups.put(SecondaryStat.RhoAias, new Pair<Integer, Integer>(this.x, localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.RhoAias, new Pair<Integer, Integer>(SkillFactory.getSkill(400011011).getEffect(applyfrom.getSkillLevel(400011011)).getX(), localDuration));
                break;
            }
            case 400011016: {
                long curr = System.currentTimeMillis();
                applyfrom.setCombo((short) Math.min(999, applyfrom.getCombo() + this.z));
                applyfrom.setLastCombo(curr);
                short combo = applyfrom.getCombo();
                int n = combo / 50;
                applyfrom.getClient().getSession().writeAndFlush((Object) CField.aranCombo(combo));
                if (applyfrom.getSkillLevel(21000000) > 0 && n != combo / 50) {
                    SkillFactory.getSkill(21000000).getEffect(applyfrom.getSkillLevel(21000000)).applyTo(applyfrom, false);
                }
                localstatups.put(SecondaryStat.InstallMaha, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                break;
            }
            case 400011052: {
                aftercancel = true;
                localstatups.put(SecondaryStat.BlessedHammer, new Pair<Integer, Integer>(applyto.getElementalCharge(), 0));
                break;
            }
            case 400011083: {
                if (!applyto.getBuffedValue(400011083)) {
                    localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                    localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(Integer.valueOf(this.indieCr), localDuration));
                    localstatups.put(SecondaryStat.IndieIgnoreMobPdpR, new Pair<Integer, Integer>(Integer.valueOf(this.indieIgnoreMobpdpR), localDuration));
                    localstatups.put(SecondaryStat.SwordOfSoulLight, new Pair<Integer, Integer>(2, localDuration));
                    break;
                }
                localstatups.clear();
                aftercancel = true;
                localstatups.put(SecondaryStat.SwordOfSoulLight, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400011116: {
                localstatups.put(SecondaryStat.AfterImageShock, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(400011116), localDuration));
                break;
            }
            case 400011127: {
                Rectangle bounds;
                if (primary) {
                    applyto.setSkillCustomInfo(400011127, (int) (applyto.getStat().getCurrentMaxHp() / 100L * (long) this.x), 0L);
                    localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.z, this.y * 1000));
                    localstatups.put(SecondaryStat.IndieBarrier, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(400011127), localDuration));
                    SkillFactory.getSkill(400011127).getEffect(applyto.getSkillLevel(400011127)).applyTo(applyto, false);
                    bounds = this.calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
                    List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.PLAYER}));
                    if (applyfrom.getParty() == null) {
                        break;
                    }
                    for (MapleMapObject mapleMapObject : affecteds) {
                        MapleCharacter affected = (MapleCharacter) mapleMapObject;
                        if (affected.getParty() == null || applyfrom.getId() == affected.getId() || applyfrom.getParty().getId() != affected.getParty().getId()) {
                            continue;
                        }
                        affected.setSkillCustomInfo(400011127, (int) (affected.getStat().getCurrentMaxHp() / 100L * (long) this.x), 0L);
                        SkillFactory.getSkill(400011127).getEffect(applyto.getSkillLevel(400011127)).applyTo(applyto, affected, false, pos, newDuration, (byte) 0, false);
                    }
                    break;
                }
                aftercancel = true;
                if (applyto.getBuffedEffect(SecondaryStat.IndieBarrier) == null) {
                    localDuration = 10000;
                }
                showEffect = false;
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieBarrier, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(400011127), localDuration));
                break;
            }
            case 400031030: {
                localstatups.clear();
                localstatups.put(SecondaryStat.WindWall, new Pair<Integer, Integer>(this.w, localDuration));
                break;
            }
            case 80002644: {
                localstatups.clear();
                break;
            }
            case 80002632: {
                localstatups.clear();
                localstatups.put(SecondaryStat.YalBuff, new Pair<Integer, Integer>(15, localDuration));
                break;
            }
            case 80002633: {
                localstatups.clear();
                if (!applyto.getBuffedValue(80002633)) {
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                    localstatups.put(SecondaryStat.NotDamaged, new Pair<>(1, localDuration));
                    localstatups.put(SecondaryStat.IonBuff, new Pair<Integer, Integer>(1, localDuration));
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.IndieNotDamaged, 80002633);
                applyto.cancelEffectFromBuffStat(SecondaryStat.NotDamaged, 80002633);
                applyto.cancelEffectFromBuffStat(SecondaryStat.IonBuff, 80002633);
                break;
            }

            case 80002393: {
                localstatups.clear();
                if (!applyto.getBuffedValue(80002393)) {
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                    localstatups.put(SecondaryStat.NotDamaged, new Pair<>(1, localDuration));
                    break;
                }
            }

            case 80002093: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<>(1, localDuration));
                break;
            }
            case 1221016: {
                bufftimeR = false;
                break;
            }
            case 400011003: {
                Rectangle bounds = this.calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
                List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.PLAYER}));
                boolean active = false;
                if (applyfrom.getParty() != null) {
                    for (MapleMapObject affectedmo : affecteds) {
                        MapleCharacter affected = (MapleCharacter) affectedmo;
                        if (affected.getParty() == null || applyfrom.getId() == affected.getId() || applyfrom.getParty().getId() != affected.getParty().getId()) {
                            continue;
                        }
                        localstatups.clear();
                        localstatups.put(SecondaryStat.HolyUnity, new Pair<Integer, Integer>(affected.getId(), localDuration));
                        affected.getMap().broadcastMessage(affected, CField.EffectPacket.showEffect(affected, 0, this.sourceid, 1, 0, 0, (byte) (affected.getTruePosition().x > pos.x ? 1 : 0), false, pos, null, null), false);
                        applyto.setSkillCustomInfo(400011003, affected.getId(), 0L);
                        SkillFactory.getSkill(400011021).getEffect(applyto.getSkillLevel(400011003)).applyTo(applyfrom, affected, primary, pos, newDuration, (byte) 0, false);
                        active = true;
                        break;
                    }
                }
                if (active) {
                    break;
                }
                applyto.setSkillCustomInfo(400011003, 0L, 0L);
                localstatups.clear();
                localstatups.put(SecondaryStat.HolyUnity, new Pair<Integer, Integer>(applyfrom.getId(), localDuration));
                break;
            }
            case 400011021: {
                localstatups.clear();
                localstatups.put(SecondaryStat.HolyUnity, new Pair<Integer, Integer>(applyfrom.getId(), localDuration));
                break;
            }
            case 400011027: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 3000));
                break;
            }
            case 400011047: {
                indieList1.add(new Pair<Integer, Integer>(applyto.getBuffedValue(1301006) ? 1 : 0, applyto.getBuffedValue(1301007) ? 1 : 0));
                if (primary) {
                    applyto.removeSkillCustomInfo(400011047);
                    applyto.removeSkillCustomInfo(400011048);
                    localstatups.put(SecondaryStat.IndieBarrier, new Pair<Integer, Integer>(0, localDuration));
                    localstatups.put(SecondaryStat.DarknessAura, new Pair<Integer, Integer>(this.w, localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.IndieBarrier, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(400011048), (int) applyto.getBuffLimit(400011047)));
                localstatups.put(SecondaryStat.DarknessAura, new Pair<Integer, Integer>(this.w, (int) applyto.getBuffLimit(400011047)));
                break;
            }
            case 400011053: {
                localstatups.put(SecondaryStat.BlessedHammer2, new Pair<Integer, Integer>(applyto.getElementalCharge(), SkillFactory.getSkill(400011052).getEffect(this.level).getV() * 1000));
                break;
            }
            case 400011058: {
                applyto.ignoreDraco = this.q2;
                localDuration = 30000;
                applyto.removeCooldown(400011079);
                localstatups.put(SecondaryStat.WillofSwordStrike, new Pair<Integer, Integer>(applyto.ignoreDraco, localDuration));
                break;
            }
            case 400011088: {
                SkillFactory.getSkill(400011089).getEffect(1).applyTo(applyto);
                break;
            }
            case 400011089: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 1500));
                if (!applyto.getBuffedValue(400011088)) {
                    break;
                }
                applyto.cancelEffect(applyto.getBuffedEffect(400011088));
                break;
            }
            case 400011102: {
                localstatups.put(SecondaryStat.DevilishPower, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                break;
            }
            case 400011108: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, this.x));
                break;
            }
            case 400011111: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 3000));
                break;
            }
            case 400011112: {
                localstatups.put(SecondaryStat.Revenant, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400001061:
                localstatups.put(SecondaryStat.Lotus, new Pair<>(1, localDuration));
                localstatups.put(SecondaryStat.HeavensDoor, new Pair<>(1, localDuration));
                break;
            case 400001062:
                bufftimeR = false;
                cancel = false;
                localDuration = 3500;
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<>(1, localDuration));
                break;
            case 400011129: {
                aftercancel = true;
                localstatups.put(SecondaryStat.RevenantDamage, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 400011118: {
                localstatups.put(SecondaryStat.DevilishPower, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400011121: {
                localstatups.put(SecondaryStat.BlizzardTempest, new Pair<Integer, Integer>(1, this.q * 1000));
                break;
            }
            case 400011123: {
                localstatups.put(SecondaryStat.BlizzardTempest, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400011134: {
                localstatups.put(SecondaryStat.EgoWeapon, new Pair<Integer, Integer>(1, 2000));
                break;
            }
            case 400011136: {
                localstatups.put(SecondaryStat.DevilishPower, new Pair<Integer, Integer>(applyto.활성화된소드, localDuration));
                for (MapleMagicSword mSword : applyto.getMap().getAllMagicSword()) {
                    if (mSword.getChr().getId() != applyto.getId()) {
                        continue;
                    }
                    applyto.getMap().removeMapObject(mSword);
                    if (mSword.getSchedule() == null) {
                        continue;
                    }
                    mSword.getSchedule().cancel(true);
                    mSword.setSchedule(null);
                }
                applyto.활성화된소드 = 0;
                break;
            }
            case 400020009: {
                applyto.cancelEffectFromBuffStat(SecondaryStat.PsychicTornado);
                showEffect = false;
                break;
            }
            case 400021006: {
                localstatups.clear();
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                localstatups.put(SecondaryStat.UnionAura, new Pair<Integer, Integer>(applyfrom.getSkillLevel(400021006), localDuration));
                localstatups.put(SecondaryStat.YellowAura, new Pair<Integer, Integer>(applyfrom.getSkillLevel(32001016), localDuration));
                localstatups.put(SecondaryStat.DrainAura, new Pair<Integer, Integer>(applyfrom.getSkillLevel(32101009), localDuration));
                localstatups.put(SecondaryStat.BlueAura, new Pair<Integer, Integer>(applyfrom.getSkillLevel(32111012), localDuration));
                localstatups.put(SecondaryStat.DarkAura, new Pair<Integer, Integer>(applyfrom.getSkillLevel(32121017), localDuration));
                if (primary) {
                    localstatups.put(SecondaryStat.DebuffAura, new Pair<Integer, Integer>(applyfrom.getSkillLevel(32121018), localDuration));
                    localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(this.indieMad), localDuration));
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 32121017).getEffect((int) applyfrom.getSkillLevel((int) 32121017)).indieDamR), localDuration));
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 32001016).getEffect((int) applyfrom.getSkillLevel((int) 32001016)).indieSpeed), localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 32120060).getEffect((int) applyfrom.getSkillLevel((int) 32120060)).indieBDR), localDuration));
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(SkillFactory.getSkill((int) 32111012).getEffect((int) applyfrom.getSkillLevel((int) 32111012)).asrR), localDuration));
                if (applyto.getBuffedValue(32001016)) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.YellowAura, 32001016);
                }
                if (applyto.getBuffedValue(32101009)) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DrainAura, 32101009);
                }
                if (applyto.getBuffedValue(32111012)) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.BlueAura, 32111012);
                }
                if (applyto.getBuffedValue(32121017)) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.DarkAura, 32121017);
                }
                if (!applyto.getBuffedValue(32121018) || applyto.getBuffedOwner(32121018) == applyto.getId()) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.DebuffAura, 32121018);
                break;
            }
            case 400021008: {
                if (!applyto.getBuffedValue(400021008)) {
                    applyto.setSkillCustomInfo(400021009, 1L, 0L);
                    applyto.setSkillCustomInfo(400021008, 0L, 0L);
                }
                if (applyto.getSkillCustomValue0(400021009) >= 3L) {
                    applyto.setSkillCustomInfo(400021009, 3L, 0L);
                }
                localstatups.put(SecondaryStat.PsychicTornado, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(400021009), localDuration));
                break;
            }
            case 400021012: {
                localDuration = 10000;
                SecondaryStatValueHolder mbsvh = applyto.checkBuffStatValueHolder(SecondaryStat.IndiePmdR, 400021012);
                if (mbsvh != null) {
                    localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(mbsvh.value + 5, localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(5, localDuration));
                break;
            }
            case 400021032:
            case 400021033: {
                if (!applyto.getBuffedValue(2321003)) {
                    break;
                }
                applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 2321003);
                break;
            }
            case 400021047: {
                aftercancel = true;
                break;
            }
            case 400021052: {
                localstatups.put(SecondaryStat.DamR, new Pair<Integer, Integer>(Math.min(this.w + applyto.getStat().getTotalInt() / this.x, this.z), localDuration));
                break;
            }
            case 400021071: {
                if (!primary) {
                    if (applyto.getPerfusion() < this.x - 1) {
                        applyto.setPerfusion(applyto.getPerfusion() + 1);
                        aftercancel = true;
                        localstatups.put(SecondaryStat.LuminousPerfusion, new Pair<Integer, Integer>(applyto.getPerfusion(), 0));
                        break;
                    }
                    if (applyto.getPerfusion() != this.x - 1) {
                        break;
                    }
                    localstatups.clear();
                    applyto.setPerfusion(0);
                    applyto.cancelEffect(this);
                    if (applyto.getCooldownLimit(400021071) <= 0L) {
                        break;
                    }
                    applyto.removeCooldown(400021071);
                    break;
                }
                aftercancel = true;
                localstatups.put(SecondaryStat.LuminousPerfusion, new Pair<Integer, Integer>(applyto.getPerfusion(), 0));
                break;
            }
            case 400021070: {
                applyfrom.peaceMaker = primary ? this.w : --applyfrom.peaceMaker;
                applyto.addHP(applyfrom.getStat().getCurrentMaxHp() * 100L / (long) this.hp);
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.q2 + applyfrom.peaceMaker * this.w2, localDuration));
                break;
            }
            case 400021077: {
                break;
            }
            case 400021087: {
                localstatups.clear();
                localstatups.put(SecondaryStat.AbyssalLightning, new Pair<Integer, Integer>(1, localDuration));
                SkillFactory.getSkill(400021088).getEffect(this.level).applyTo(applyto, false);
                break;
            }
            case 400021088: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 2000));
                break;
            }
            case 400021092: {
                localstatups.clear();
                localstatups.put(SecondaryStat.SalamanderMischief, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400021093: {
                localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>((int) ((long) SkillFactory.getSkill((int) 400021092).getEffect((int) applyfrom.getSkillLevel((int) 400021092)).dot + applyto.getSkillCustomValue0(400021093) * (long) SkillFactory.getSkill((int) 400021092).getEffect((int) applyfrom.getSkillLevel((int) 400021092)).w2), localDuration));
                break;
            }
            case 400021096: {
                localstatups.put(SecondaryStat.LawOfGravity, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400021099: {
                if (!applyto.getBuffedValue(400021099)) {
                    localstatups.put(SecondaryStat.CrystalGate, new Pair<Integer, Integer>(1, localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.CrystalGate, new Pair<Integer, Integer>(1, (int) applyto.getBuffLimit(400021099)));
                break;
            }
            case 400021100: {
                localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(Integer.valueOf(this.indieMad), localDuration));
                break;
            }
            case 400031005: {
                localstatups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400031006: {
                if (primary) {
                    applyto.trueSniping = this.x;
                }
                localstatups.put(SecondaryStat.TrueSniping, new Pair<Integer, Integer>(applyto.trueSniping, primary ? localDuration : (int) applyto.getBuffLimit(this.sourceid)));
                break;
            }
            case 400031017: {
                localstatups.put(SecondaryStat.RideVehicleExpire, new Pair<Integer, Integer>(SecondaryStatEffect.parseMountInfo(applyto, this.sourceid), localDuration));
                localstatups.put(SecondaryStat.IgnisRore, new Pair<Integer, Integer>(10, localDuration));
                break;
            }
            case 400031012: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(this.x, 10000));
                break;
            }
            case 400031014: {
                if (applyto.getBuffedEffect(SecondaryStat.RideVehicle) != null) {
                    break;
                }
                SkillFactory.getSkill(33001001).getEffect(1).applyTo(applyto, 0);
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(this.x, 2000));
                break;
            }
            case 400031028: {
                if (applyto.getBuffedEffect(SecondaryStat.AdvancedQuiver) != null) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.AdvancedQuiver);
                }
                localstatups.put(SecondaryStat.QuiverFullBurst, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 400031034: {
                if (applyto.getCooldownLimit(400031034) == 0L) {
                    applyto.setSkillCustomInfo(this.sourceid, applyto.렐릭게이지, 0L);
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 1200));
                    localstatups.put(SecondaryStat.IgnorePCounter, new Pair<Integer, Integer>(1, 1200));
                    MapleCharacter.렐릭게이지(applyto.getClient(), this.sourceid);
                    break;
                }
                if (applyto.getBuffedEffect(SecondaryStat.NextAttackEnhance) != null) {
                    break;
                }
                int gauge = (int) applyto.getSkillCustomValue0(this.sourceid);
                applyto.removeSkillCustomInfo(this.sourceid);
                if (gauge == 1000) {
                    localstatups.put(SecondaryStat.NextAttackEnhance, new Pair<Integer, Integer>(100, 1000));
                    break;
                }
                if (gauge >= 750) {
                    localstatups.put(SecondaryStat.NextAttackEnhance, new Pair<Integer, Integer>(75, 1000));
                    break;
                }
                if (gauge >= 500) {
                    localstatups.put(SecondaryStat.NextAttackEnhance, new Pair<Integer, Integer>(50, 1000));
                    break;
                }
                if (gauge < 250) {
                    break;
                }
                localstatups.put(SecondaryStat.NextAttackEnhance, new Pair<Integer, Integer>(25, 1000));
                break;
            }
            case 400031036:
            case 400031037:
            case 400031038:
            case 400031039:
            case 400031040:
            case 400031041:
            case 400031042:
            case 400031043: {
                if (this.sourceid == 400031038) {
                    localstatups.put(SecondaryStat.IndieUnkIllium, new Pair<Integer, Integer>(1, localDuration));
                    localstatups.put(SecondaryStat.IndieDamReduceR, new Pair<Integer, Integer>(-this.x, localDuration));
                }
                if (this.sourceid == 400031042) {
                    localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                    localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, localDuration));
                } else if (this.sourceid == 400031039) {
                    localstatups.put(SecondaryStat.IndieUnkIllium, new Pair<Integer, Integer>(1, localDuration));
                } else if (this.sourceid == 400031040) {
                    localstatups.put(SecondaryStat.IndieUnkIllium, new Pair<Integer, Integer>(1, localDuration));
                }
                if (applyfrom.getId() != applyto.getId()) {
                    break;
                }
                MapleCharacter.렐릭게이지(applyto.getClient(), this.sourceid);
                MapleCharacter.문양(applyto.getClient(), this.sourceid);
                break;
            }
            case 400031044: {
                showEffect = false;
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 2600));
                    localstatups.put(SecondaryStat.RoyalKnights, new Pair<Integer, Integer>(1, localDuration));
                    break;
                }
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 1600));
                break;
            }
            case 400031047:
            case 400031049: {
                MapleCharacter.렐릭게이지(applyto.getClient(), this.sourceid);
                MapleCharacter.문양(applyto.getClient(), this.sourceid);
                break;
            }
            case 400031051: {
                if (primary) {
                    MapleCharacter.렐릭게이지(applyto.getClient(), this.sourceid);
                    MapleCharacter.문양(applyto.getClient(), this.sourceid);
                    break;
                }
                return;
            }
            case 400031048: {
                localDuration = 4000;
                showEffect = false;
                localstatups.put(SecondaryStat.RelikUnboundDischarge, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400031050: {
                showEffect = false;
                break;
            }
            case 400031053: {
                localDuration = 0;
                aftercancel = true;
                localstatups.put(SecondaryStat.SilhouetteMirage, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 400031055: {
                applyto.repeatingCrossbowCatridge = this.x;
                localstatups.put(SecondaryStat.RepeatingCrossbowCatridge, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 400041002: {
                aftercancel = true;
                showEffect = false;
                localstatups.put(SecondaryStat.ShadowAssult, new Pair<Integer, Integer>(3, 0));
                if (applyfrom.getSkillCustomValue0(400041002) > 0L) {
                    applyfrom.removeCooldown(400041002);
                }
                applyfrom.setSkillCustomInfo(400041002, 3L, 0L);
                break;
            }
            case 400041003: {
                aftercancel = true;
                showEffect = false;
                localstatups.put(SecondaryStat.ShadowAssult, new Pair<Integer, Integer>(2, 0));
                applyfrom.setSkillCustomInfo(400041002, 2L, 0L);
                break;
            }
            case 400041004: {
                aftercancel = true;
                showEffect = false;
                localstatups.put(SecondaryStat.ShadowAssult, new Pair<Integer, Integer>(1, 0));
                applyfrom.setSkillCustomInfo(400041002, 1L, 0L);
                break;
            }
            case 400041005: {
                showEffect = false;
                applyfrom.setSkillCustomInfo(400041002, 0L, 0L);
                applyto.cancelEffectFromBuffStat(SecondaryStat.ShadowAssult);
                break;
            }
            case 400041008: {
                if (!applyto.getBuffedValue(400041008)) {
                    localstatups.put(SecondaryStat.ShadowSpear, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                    break;
                }
                return;
            }
            case 400041009: {
                if (primary) {
                    localstatups.put(SecondaryStat.IndieDamageReduce, new Pair<Integer, Integer>(this.y, 0));
                    localstatups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(80, 5500));
                    break;
                }
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, 3000));
                break;
            }
            case 400041011: {
                localstatups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(this.getIndieMhpR()), localDuration));
                break;
            }
            case 400041012: {
                localstatups.put(SecondaryStat.DamageDecreaseWithHP, new Pair<Integer, Integer>(this.z, localDuration));
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(this.indieAsrR), localDuration));
                break;
            }
            case 400041013: {
                localstatups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 400041014: {
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                break;
            }
            case 400041015: {
                localstatups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(this.getIndieMhpR()), localDuration));
                localstatups.put(SecondaryStat.DamageDecreaseWithHP, new Pair<Integer, Integer>(this.z, localDuration));
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(Integer.valueOf(this.indieAsrR), localDuration));
                localstatups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(this.x, localDuration));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                break;
            }
            case 400041025:
            case 400041026:
            case 400041027: {
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, this.s * 1000));
                break;
            }
            case 400041032: {
                localstatups.clear();
                if (applyto.getBuffedValue(400041032)) {
                    localstatups.put(SecondaryStat.IndieEvasion, new Pair<Integer, Integer>(this.w, (int) (applyto.getBuffLimit(400041032) * (long) this.u / 100L)));
                    localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(this.q, (int) (applyto.getBuffLimit(400041032) * (long) this.u / 100L)));
                    localstatups.put(SecondaryStat.IndieShotDamage, new Pair<Integer, Integer>(this.s, (int) (applyto.getBuffLimit(400041032) * (long) this.u / 100L)));
                    localstatups.put(SecondaryStat.ReadyToDie, new Pair<Integer, Integer>(2, (int) (applyto.getBuffLimit(400041032) * (long) this.u / 100L)));
                    break;
                }
                localstatups.put(SecondaryStat.IndieEvasion, new Pair<Integer, Integer>(this.x, localDuration));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(this.y, localDuration));
                localstatups.put(SecondaryStat.IndieShotDamage, new Pair<Integer, Integer>(this.z, localDuration));
                localstatups.put(SecondaryStat.ReadyToDie, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400041037: {
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(applyto.shadowBite, localDuration));
                applyto.shadowBite = 0;
                break;
            }
            case 400041040: {
                localstatups.clear();
                applyto.cancelEffect(this);
                if (primary) {
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 2000));
                    MapleMonster mob = applyto.getMap().getMonsterByOid(applyto.getMarkOfPhantomOid());
                    applyto.setMarkofPhantom(0);
                    applyto.setMarkOfPhantomOid(0);
                    aftercancel = true;
                    pos = mob != null ? mob.getTruePosition() : applyto.getTruePosition();
                    ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
                    skills.add(new RangeAttack(400041045, pos, 0, 0, 0));
                    skills.add(new RangeAttack(400041045, pos, 0, 0, 0));
                    skills.add(new RangeAttack(400041045, pos, 0, 0, 0));
                    skills.add(new RangeAttack(400041045, pos, 0, 0, 0));
                    skills.add(new RangeAttack(400041045, pos, 0, 0, 0));
                    skills.add(new RangeAttack(400041045, pos, 0, 0, 0));
                    skills.add(new RangeAttack(400041045, pos, 0, 0, 0));
                    skills.add(new RangeAttack(400041046, pos, 0, 0, 0));
                    applyto.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400041040, skills));
                    break;
                }
                aftercancel = true;
                if (applyto.getMarkofPhantom() < this.x) {
                    applyto.setMarkofPhantom(applyto.getMarkofPhantom() + 1);
                }
                localstatups.put(SecondaryStat.MarkOfPhantomDebuff, new Pair<Integer, Integer>(applyto.getMarkofPhantom(), 0));
                localstatups.put(SecondaryStat.MarkOfPhantomStack, new Pair<Integer, Integer>(applyto.getMarkofPhantom(), 0));
                break;
            }
            case 400041047: {
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(SkillFactory.getSkill(400041044).getEffect(applyfrom.getSkillLevel(400041044)).getS(), localDuration));
                break;
            }
            case 400041048: {
                localstatups.put(SecondaryStat.AltergoReinforce, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400041053: {
                applyto.cancelEffectFromBuffStat(SecondaryStat.SageWrathOfGods, 400041052);
                applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 400041052);
                aftercancel = false;
                if (primary) {
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 5000));
                    this.applyTo(applyto, false);
                    break;
                }
                localstatups.put(SecondaryStat.AdventOfGods, new Pair<Integer, Integer>(1, 30000));
                break;
            }
            case 400041057: {
                localstatups.put(SecondaryStat.PhotonRay, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400041061: {
                applyto.throwBlasting = this.x;
                localstatups.put(SecondaryStat.ThrowBlasting, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 400041063: {
                localstatups.put(SecondaryStat.SageElementalClone, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400051000: {
                if (primary) {
                    break;
                }
                aftercancel = true;
                localstatups.put(SecondaryStat.SelectDice, new Pair<Integer, Integer>(localDuration, 0));
                break;
            }
            case 400051003: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 1500));
                break;
            }
            case 5120028: {
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>((int) this.indieDamR, 10000));
                break;
            }
            case 400051002: {
                applyto.transformEnergyOrb = this.w;
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                localstatups.put(SecondaryStat.Transform, new Pair<Integer, Integer>(this.w, localDuration));
                break;
            }
            case 400051010: {
                for (MapleCoolDownValueHolder cooldown : applyto.getCooldowns()) {
                    if (SkillFactory.getSkill(cooldown.skillId) == null || SkillFactory.getSkill(cooldown.skillId).isHyper() || !GameConstants.isEunWol(cooldown.skillId / 10000) || SkillFactory.getSkill(cooldown.skillId).isNotCooltimeReset()) {
                        continue;
                    }
                    applyto.removeCooldown(cooldown.skillId);
                }
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                localstatups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                break;
            }
            case 400051011: {
                localstatups.clear();
                if (showEffect) {
                    localstatups.put(SecondaryStat.EnergyBurst, new Pair<Integer, Integer>(1, localDuration));
                    break;
                }
                if (applyfrom.getBuffedValue(SecondaryStat.IndieNotDamaged) != null) {
                    break;
                }
                int up = applyto.getEnergyBurst() >= 50 ? 2 : (applyto.getEnergyBurst() >= 25 ? 1 : 0);
                localDuration = 6000 + this.s * up * 1000;
                applyto.setEnergyBurst(0);
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400051024: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 750));
                break;
            }
            case 400051025: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 1800));
                break;
            }
            case 400051027: {
                int stack = localDuration;
                localstatups.put(SecondaryStat.IndieAsrR, new Pair<Integer, Integer>(this.indieAsrR * stack, 10000));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(this.indieCr * stack, 10000));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(this.indieStance * stack, 10000));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(this.indiePmdR * stack, 10000));
                localstatups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(stack, 10000));
                break;
            }
            case 400051334: {
                localstatups.clear();
                aftercancel = false;
                if (primary) {
                    localstatups.put(SecondaryStat.MemoryOfSource, new Pair<Integer, Integer>(Integer.valueOf(this.level), 30000));
                    SkillFactory.getSkill(this.sourceid).getEffect(this.level).applyTo(applyto, false);
                    break;
                }
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 10000));
                break;
            }
            case 400051044: {
                if (applyto.striker3rdStack < 8) {
                    aftercancel = true;
                    bufftimeR = false;
                    ++applyto.striker3rdStack;
                    localstatups.put(SecondaryStat.Striker3rd, new Pair<Integer, Integer>(applyto.striker3rdStack, 0));
                    break;
                }
                if (applyto.getSkillCustomValue(400051044) != null) {
                    break;
                }
                ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
                skills.add(new RangeAttack(400051044, applyto.getPosition(), applyto.isFacingLeft() ? -1 : 0, 0, 0));
                skills.add(new RangeAttack(400051045, applyto.getPosition(), applyto.isFacingLeft() ? -1 : 0, 0, 0));
                skills.add(new RangeAttack(400051045, applyto.getPosition(), applyto.isFacingLeft() ? -1 : 0, 0, 0));
                skills.add(new RangeAttack(400051045, applyto.getPosition(), applyto.isFacingLeft() ? -1 : 0, 0, 0));
                skills.add(new RangeAttack(400051045, applyto.getPosition(), applyto.isFacingLeft() ? -1 : 0, 0, 0));
                applyto.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400051044, skills));
                applyto.setSkillCustomInfo(400051044, 0L, this.getCooldown(applyto));
                applyto.striker3rdStack = 0;
                applyto.cancelEffect(this);
                break;
            }
            case 400051045: {
                break;
            }
            case 400051058: {
                if (!applyto.getBuffedValue(400051058)) {
                    applyto.striker4thAttack = this.x;
                    localstatups.put(SecondaryStat.Striker4th, new Pair<Integer, Integer>(applyto.striker4thAttack, localDuration));
                    break;
                }
                aftercancel = true;
                --applyto.striker4thAttack;
                if (applyto.striker4thAttack <= 1) {
                    applyto.cancelEffectFromBuffStat(SecondaryStat.Striker4th);
                    break;
                }
                localstatups.put(SecondaryStat.Striker4th, new Pair<Integer, Integer>(applyto.striker4thAttack, (int) applyto.getBuffLimit(400051058)));
                break;
            }
            case 400051072: {
                localstatups.put(SecondaryStat.IndieDamReduceR, new Pair<Integer, Integer>(-this.w, 1000));
                localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, 1000));
                break;
            }
            case 400051077: {
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                break;
            }
            case 400051078: {
                if (applyto.getBuffedValue(400051078)) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieKeyDownMoving, new Pair<Integer, Integer>(this.w, 3320));
                break;
            }
            case 80001965: {
                localstatups.put(SecondaryStat.DashSpeed, new Pair<Integer, Integer>(300, localDuration));
                localstatups.put(SecondaryStat.IndieForceSpeed, new Pair<Integer, Integer>(300, localDuration));
                localstatups.put(SecondaryStat.DashJump, new Pair<Integer, Integer>(3, localDuration));
                localstatups.put(SecondaryStat.IndieForceJump, new Pair<Integer, Integer>(3, localDuration));
                break;
            }
            case 400001020: {
                applyto.setSkillCustomInfo(2320048, this.v, 0L);
                break;
            }
            case 80001535: {
                localstatups.put(SecondaryStat.MesoUp, new Pair<Integer, Integer>(20, 0));
                break;
            }
            case 162001001: {
                showEffect = false;
                break;
            }
            case 80003058:
            case 160010001: {
                localstatups.put(SecondaryStat.IndieNDR, new Pair<Integer, Integer>(this.w, localDuration));
                break;
            }
            case 80003070: {
                aftercancel = true;
                localstatups.put(SecondaryStat.NatureFriend, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(this.sourceid), 0));
                break;
            }
            case 162120038: {
                if (primary) {
                    applyto.setSkillCustomInfo(162120038, applyto.getStat().getCurrentMaxHp() * (long) this.x / 100L, 0L);
                    localDuration += this.u * 1000;
                }
                localstatups.put(SecondaryStat.IndieUnk1, new Pair<Integer, Integer>(2, localDuration));
                localstatups.put(SecondaryStat.IndieBarrier, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(162120038), localDuration));
                break;
            }
            case 162121043: {
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(this.indieStance), localDuration));
                localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(Integer.valueOf(this.indieBDR), localDuration));
                localstatups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 162121044: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 162121022: {
                localstatups.put(SecondaryStat.IndieDamReduceR, new Pair<Integer, Integer>(-this.x, this.q * 1000));
                localstatups.put(SecondaryStat.IndieUnk1, new Pair<Integer, Integer>(1, this.q * 1000));
                localstatups.put(SecondaryStat.AntiMagicShell, new Pair<Integer, Integer>(1, this.q * 1000));
                localstatups.put(SecondaryStat.DreamDowon, new Pair<Integer, Integer>(1, this.q * 1000));
                if (applyto.getSkillLevel(162120038) <= 0) {
                    break;
                }
                SkillFactory.getSkill(162120038).getEffect(1).applyTo(applyto, this.q * 1000);
                break;
            }
            case 162121003: {
                localstatups.put(SecondaryStat.흡수_강, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 162121006: {
                localstatups.put(SecondaryStat.흡수_바람, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 162121009: {
                localstatups.put(SecondaryStat.흡수_해, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 162111004: {
                SecondaryStatEffect eff = SkillFactory.getSkill(162111003).getEffect(this.getLevel());
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(eff.indieDamR), localDuration));
                break;
            }
            case 162111001: {
                SecondaryStatEffect eff = SkillFactory.getSkill(162111000).getEffect(this.getLevel());
                localstatups.put(SecondaryStat.IndieJump, new Pair<Integer, Integer>(Integer.valueOf(eff.indieJump), localDuration));
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(eff.indieSpeed), localDuration));
                localstatups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, localDuration));
                break;
            }
            case 80003059: {
                SecondaryStatEffect eff = SkillFactory.getSkill(162111000).getEffect(applyfrom.getSkillLevel(162111000));
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, (int) eff.getT() * 1000));
                break;
            }
            //리마스터
            case 5111017: {
                bufftimeR = false;
                aftercancel = true;
                localDuration = 0;
                localstatups.put(SecondaryStat.SerpentStone, new Pair<Integer, Integer>(applyto.서펜트스톤, localDuration));
                break;
            }
            case 5110020: {
                localDuration = 16000;
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 5121052: {
                localDuration = 91000;
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 2111013: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 5220019: {
                localDuration = 120000;
                localstatups.put(SecondaryStat.SpiritLink, new Pair<Integer, Integer>(5211019, localDuration));
                break;
            }
            case 3111015: {
                bufftimeR = false;
                //aftercancel = true;
                localstatups.put(SecondaryStat.FlashMirage, new Pair<Integer, Integer>(applyto.플레시미라주스택, 0));
                break;
            }
            case 1320016:
                localDuration = 261000;
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            case 1321020: {
                localstatups.put(SecondaryStat.ReincarnationAccept, new Pair<Integer, Integer>(applyto.리인카네이션, 0));
                break;
            }
            case 5101017: {
                localstatups.put(SecondaryStat.SeaSerpent, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 2311014: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 3111017:
            case 3211019: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 5210015: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 5201012: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 5221029: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 5221022: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 4121017: {
                localDuration = 5000;
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 162111002: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, this.s2 * 1000));
                break;
            }
            case 162110007: {
                applyto.addHP(applyto.getStat().getCurrentMaxHp() * (long) this.u / 100L);
                applyto.addMP(applyto.getStat().getCurrentMaxMp(applyto) * (long) this.u / 100L);
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(this.x, localDuration));
                if (applyto.getSkillLevel(162120037) <= 0) {
                    break;
                }
                SecondaryStatEffect eff = SkillFactory.getSkill(162120037).getEffect(1);
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(eff.x, localDuration));
                localstatups.put(SecondaryStat.IndieJump, new Pair<Integer, Integer>(eff.y, localDuration));
                break;
            }
            case 162001005: {
                localstatups.put(SecondaryStat.산_무등, new Pair<Integer, Integer>(this.x, 0));
                break;
            }
            case 162101000: {
                localstatups.put(SecondaryStat.용맥_읽기, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 400001037: {
                localstatups.put(SecondaryStat.MagicCircuitFullDrive, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.y, localDuration));
                break;
            }
            case 80003064: {
                if (applyto.getKeyValue(100857, "feverCnt") < 0L) {
                    applyto.setKeyValue(100857, "feverCnt", "0");
                    break;
                }
                if (applyto.getKeyValue(100857, "feverCnt") >= 10L) {
                    applyto.dropMessage(5, "오늘은 이미 <리액션 팡팡>을 10번 완료하였습니다.");
                    break;
                }
                localstatups.put(SecondaryStat.EventSpecialSkill, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400021005: {
                int skillid = applyto.getBuffedValue(20040220) ? 20040220 : 20040219;
                long duration = applyto.getBuffLimit(skillid);
                while (applyto.getBuffedEffect(SecondaryStat.Larkness) != null) {
                    applyto.cancelEffect(applyto.getBuffedEffect(SecondaryStat.Larkness));
                }
                applyto.setUseTruthDoor(true);
                SkillFactory.getSkill(20040220).getEffect(1).applyTo(applyfrom, applyto, false, pos, (int) duration, (byte) 0, true);
                break;
            }
            case 11001022: {
                localstatups.put(SecondaryStat.CygnusElementSkill, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 13001022: {
                localstatups.put(SecondaryStat.CygnusElementSkill, new Pair<Integer, Integer>(1, 0));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), 0));
                break;
            }
            case 14001021: {
                localstatups.put(SecondaryStat.ElementDarkness, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 2211012: {
                if (primary) {
                    localstatups.put(SecondaryStat.AntiMagicShell, new Pair<Integer, Integer>(0, 0));
                    applyto.setAntiMagicShell((byte) 1);
                    break;
                }
                localstatups.put(SecondaryStat.AntiMagicShell, new Pair<Integer, Integer>(1, localDuration));
                applyto.setAntiMagicShell((byte) 100);
                break;
            }
            case 80002421: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 3000));
                break;
            }
            case 80001809: {
                int itemid;
                if (applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -29) == null || (itemid = applyto.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -29).getItemId()) < 1182001 || itemid > 1182005) {
                    break;
                }
                if (itemid >= 1182001) {
                    localstatups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(30, 0));
                }
                if (itemid >= 1182002) {
                    localstatups.put(SecondaryStat.IndiePad, new Pair<Integer, Integer>(30, 0));
                    localstatups.put(SecondaryStat.IndieMad, new Pair<Integer, Integer>(30, 0));
                }
                if (itemid >= 1182003) {
                    localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(15, 0));
                }
                if (itemid >= 1182004) {
                    localstatups.put(SecondaryStat.IndieCD, new Pair<Integer, Integer>(8, 0));
                }
                if (itemid < 1182005) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 80001537: {
                localstatups.put(SecondaryStat.IndieAllStatR, new Pair<Integer, Integer>(20, 0));
                break;
            }
            case 80001538: {
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(20, 0));
                break;
            }
            case 80001539: {
                localstatups.put(SecondaryStat.IndieBDR, new Pair<Integer, Integer>(40, 0));
                break;
            }
            case 80001544:
            case 80001545:
            case 80001546: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 4301003: {
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(this.indieSpeed), localDuration));
                localstatups.put(SecondaryStat.IndieJump, new Pair<Integer, Integer>(Integer.valueOf(this.indieJump), localDuration));
                break;
            }
            case 400011055: {
                if (!applyfrom.getBuffedValue(this.sourceid)) {
                    break;
                }
                return;
            }
            case 80002280: {
                bufftimeR = false;
                if (applyfrom.getSkillLevel(20010294) <= 0) {
                    if (applyfrom.getSkillLevel(80000369) <= 0) {
                        break;
                    }
                }
                final int skilllv = (applyfrom.getSkillLevel(20010294) <= 0) ? applyfrom.getSkillLevel(80000369) : applyfrom.getSkillLevel(20010294);
                final int plus = (skilllv != 1) ? 50 : 30;
                localDuration += localDuration / 100 * plus;
                break;
            }
            case 80001762: {
                bufftimeR = false;
                localstatups.put(SecondaryStat.RandAreaAttack, new Pair<Integer, Integer>(1, 30000));
                break;
            }
            case 400001052: {
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, (int) SkillFactory.getSkill((int) 400001007).getEffect((int) this.level).t * 1000));
                break;
            }
            case 80003046: {
                if (applyfrom.getKeyValue(100794, "today") >= (long) (Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1 ? 6000 : 3000)) {
                    return;
                }
                if (applyfrom.getBuffedValue(this.sourceid)) {
                    return;
                }
                applyfrom.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062524, 0));
                applyfrom.getClient().send(SLFCGPacket.FollowNpctoSkill(true, 9062524, 80003051));
                if (applyfrom.getQuestStatus(100801) == 2) {
                    applyfrom.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062525, 0));
                    applyfrom.getClient().send(SLFCGPacket.FollowNpctoSkill(true, 9062525, 80003052));
                }
                if (applyfrom.getQuestStatus(100801) == 3) {
                    applyfrom.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062526, 0));
                    applyfrom.getClient().send(SLFCGPacket.FollowNpctoSkill(true, 9062526, 80003053));
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.EventSpecialSkill, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 80002625: {
                localstatups.clear();
                int type = 0;
                type = applyto.getSkillCustomValue0(80002625) == 1L ? 2 : 1;
                applyto.setSkillCustomInfo(80002625, type, 0L);
                localstatups.put(SecondaryStat.BlackMageDebuff, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(80002625), 0x200B20));
                break;
            }
            case 80002902: {
                bufftimeR = false;
                localstatups.clear();
                localstatups.put(SecondaryStat.DuskDarkness, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 80002751: {
                localstatups.put(SecondaryStat.WillPoison, new Pair<Integer, Integer>(1, this.duration));
                break;
            }
            case 80002255: {
                localstatups.put(SecondaryStat.StopPortion, new Pair<Integer, Integer>(1, 6000));
                break;
            }
            case 80002282: {
                bufftimeR = false;
                break;
            }
            case 131001019: {
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 131001026: {
                aftercancel = true;
                if (primary && applyto.getSkillCustomValue0(131001026) < (long) this.x) {
                    applyto.addSkillCustomInfo(131001026, 1L);
                }
                localstatups.put(SecondaryStat.PinkBeanMagicShow, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(131001026), 0));
                break;
            }
            case 131001023: {
                localstatups.put(SecondaryStat.PinkBeanMatroCyca, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80001740: {
                localstatups.clear();
                localstatups.put(SecondaryStat.BattlePvP_LangE_Protection, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(80001740), localDuration));
                break;
            }
            case 80002673: {
                localstatups.clear();
                localstatups.put(SecondaryStat.ReverseInput, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80002344: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Slow, new Pair<Integer, Integer>(80, localDuration));
                localstatups.put(SecondaryStat.ReverseInput, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80002338: {
                localstatups.clear();
                if (!applyto.getBuffedValue(this.sourceid) && applyto.getSkillCustomValue0(this.sourceid) > 0L) {
                    applyto.removeSkillCustomInfo(this.sourceid);
                }
                applyto.setSkillCustomInfo(this.sourceid, applyto.getSkillCustomValue0(this.sourceid) + 1L, 0L);
                if (applyto.getSkillCustomValue0(this.sourceid) >= 3L) {
                    applyto.removeSkillCustomInfo(this.sourceid);
                    localstatups.put(SecondaryStat.Stun, new Pair<Integer, Integer>(1, 1000));
                    break;
                }
                localDuration = 6000;
                localstatups.put(SecondaryStat.BattlePvP_Rude_Stack, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(this.sourceid), localDuration));
                break;
            }
            case 80002342: {
                localstatups.clear();
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80002340: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Slow, new Pair<Integer, Integer>(50, localDuration));
                break;
            }
            case 80001735: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Slow, new Pair<Integer, Integer>(30, localDuration));
                break;
            }
            case 80002341: {
                localstatups.clear();
                localDuration = 7000;
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(20, localDuration));
                localstatups.put(SecondaryStat.Jump, new Pair<Integer, Integer>(10, localDuration));
                localstatups.put(SecondaryStat.NoDebuff, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80001668: {
                localstatups.clear();
                localDuration = 2000;
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(20, localDuration));
                localstatups.put(SecondaryStat.NoDebuff, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80001733: {
                localstatups.clear();
                localDuration = 4000;
                localstatups.put(SecondaryStat.BattlePvP_Helena_WindSpirit, new Pair<Integer, Integer>(30, localDuration));
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(15, localDuration));
                break;
            }
            case 80001675: {
                int dam = applyfrom.getBattleGroundChr().getLevel() >= 11 ? 60 : (applyfrom.getBattleGroundChr().getLevel() >= 9 ? 50 : (applyfrom.getBattleGroundChr().getLevel() >= 7 ? 40 : (applyfrom.getBattleGroundChr().getLevel() >= 5 ? 30 : 20)));
                localstatups.clear();
                localDuration = 10000;
                localstatups.put(SecondaryStat.Poison, new Pair<Integer, Integer>(dam, localDuration));
                break;
            }
            case 80001658: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Weakness, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(-100, localDuration));
                break;
            }
            case 80001676:
            case 80003003:
            case 80003005:
            case 80003012: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Stun, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80001732: {
                localstatups.clear();
                localstatups.put(SecondaryStat.BattlePvP_Helena_Mark, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80001649: {
                localstatups.clear();
                localDuration = 1000;
                localstatups.put(SecondaryStat.Stun, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 80003004: {
                localstatups.clear();
                localDuration = 10000;
                localstatups.put(SecondaryStat.Slow, new Pair<Integer, Integer>(applyto.getBattleGroundChr().getSpeed() - 50, localDuration));
                break;
            }
            case 80001651: {
                localstatups.clear();
                localDuration = 15000;
                localstatups.put(SecondaryStat.Pad, new Pair<Integer, Integer>(Integer.valueOf(this.pad), localDuration));
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(Integer.valueOf(this.speed), localDuration));
                localstatups.put(SecondaryStat.Jump, new Pair<Integer, Integer>(Integer.valueOf(this.jump), localDuration));
                localstatups.put(SecondaryStat.Recovery, new Pair<Integer, Integer>(100, localDuration));
                break;
            }
            case 80001654: {
                localstatups.clear();
                localDuration = 6000;
                localstatups.put(SecondaryStat.DefUp, new Pair<Integer, Integer>(100, localDuration));
                break;
            }
            case 80001655: {
                localstatups.clear();
                localDuration = 6000;
                localstatups.put(SecondaryStat.BattlePvP_Mike_Shield, new Pair<Integer, Integer>(500, localDuration));
                break;
            }
            case 80002670: {
                localstatups.clear();
                localDuration = 600000;
                localstatups.put(SecondaryStat.BattlePvP_Wonky_ChargeA, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(-30, localDuration));
                break;
            }
            case 80002674: {
                localstatups.clear();
                localDuration = 600000;
                localstatups.put(SecondaryStat.NoDebuff, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.BattlePvP_Wonky_ChargeA, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(20, localDuration));
                break;
            }
            case 80002671: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Flying, new Pair<Integer, Integer>(1, localDuration - 700));
                localstatups.put(SecondaryStat.Speed, new Pair<Integer, Integer>(-60, localDuration - 700));
                localstatups.put(SecondaryStat.BattlePvP_Wonky_Charge, new Pair<Integer, Integer>(8, localDuration));
                localDuration = 4000;
                break;
            }
            case 80002672: {
                localstatups.clear();
                localstatups.put(SecondaryStat.BattlePvP_Wonky_Awesome, new Pair<Integer, Integer>(10, localDuration));
                localDuration = 10000;
                break;
            }
            case 400021068: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400021061: {
                localstatups.clear();
                localstatups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(100, 0));
                break;
            }
            case 142111010: {
                localstatups.clear();
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, 900));
                break;
            }
            case 142121004: {
                if (applyto.getSkillCustomValue0(142121004) <= 0L) {
                    break;
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(142121004), localDuration));
                break;
            }
            case 60001216:
            case 60001217: {
                localstatups.clear();
                localstatups.put(SecondaryStat.ReshuffleSwitch, new Pair<Integer, Integer>(0, 0));
                while (applyfrom.getBuffedEffect(SecondaryStat.ReshuffleSwitch) != null) {
                    applyfrom.cancelEffect(applyfrom.getBuffedEffect(SecondaryStat.ReshuffleSwitch));
                }
                break;
            }
            case 135001017: {
                localstatups.clear();
                localstatups.put(SecondaryStat.YetiSpicy, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 135001013: {
                localstatups.clear();
                int up = applyfrom.getLevel() / this.y;
                localstatups.put(SecondaryStat.IndieStatR, new Pair<Integer, Integer>(up, localDuration));
                break;
            }
            case 135001012: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieUnk1, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 135001007: {
                aftercancel = true;
                localstatups.clear();
                localstatups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) applyfrom.getSkillCustomValue0(this.sourceid), 0));
                break;
            }
            case 135001015: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(50, localDuration));
                localstatups.put(SecondaryStat.YetiFriendsPePe, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 135001009: {
                localstatups.clear();
                localstatups.put(SecondaryStat.LuckOfUnion, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(30, localDuration));
                localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                break;
            }
            case 135001005: {
                localstatups.clear();
                if (!primary) {
                    aftercancel = true;
                    localstatups.put(SecondaryStat.YetiAnger, new Pair<Integer, Integer>(1, 0));
                    break;
                }
                if (applyfrom.getBuffedEffect(SecondaryStat.YetiAngerMode) == null) {
                    while (applyfrom.getBuffedValue(135001005)) {
                        applyfrom.cancelEffect(this);
                    }
                    applyfrom.setSkillCustomInfo(13500, 1L, 0L);
                    applyfrom.setSkillCustomInfo(135001007, 3L, 0L);
                    SkillFactory.getSkill(135001007).getEffect(1).applyTo(applyfrom);
                }
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(Integer.valueOf(this.indieSpeed), localDuration));
                localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                localstatups.put(SecondaryStat.YetiAngerMode, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400051041: {
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 0));
                    break;
                }
                localstatups.put(SecondaryStat.IndieBlockSkill, new Pair<Integer, Integer>(1, 2500));
                break;
            }
            case 14001031: {
                localstatups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(Integer.valueOf(this.level), 2260));
                break;
            }
            case 400051040: {
                localDuration = 1320;
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400051021: {
                localDuration = 2000;
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 3101008: {
                localstatups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(10, 0));
                break;
            }
            case 2300009: {
                localstatups.put(SecondaryStat.BlessingAnsanble, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(2320013), 0));
                break;
            }
            case 2321006: {
                localstatups.clear();
                if (!primary) {
                    bufftimeR = false;
                    aftercancel = true;
                    int plus = applyfrom.getStat().getTotalInt() / this.y * this.w;
                    localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.x + plus, this.subTime));
                    break;
                }
                aftercancel = true;
                localstatups.put(SecondaryStat.NotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400001042: {
                localstatups.clear();
                double d = (double) applyto.getBuffedValue(SecondaryStat.BasicStatUp).intValue() / 100.0;
                double up = Math.floor(d * (double) applyto.getStat().getStr());
                int a = (int) (up / 100.0 * (double) this.x);
                localstatups.put(SecondaryStat.IndieAllStat, new Pair<Integer, Integer>(a, localDuration));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                break;
            }
            case 80002419: {//` 버프 아 버프가 따로있었네요 그래서 351을 봐야할것같다고 말씀드린거..ㅇㅂㅇ...잘몰라서 아까 버프있냐고 물어봤는데 ㄷㄷ; 버프인지 는 몰랐어요 ㅇㅋ
                localstatups.clear();

                List<Integer> effects = applyfrom.getBonusEffect();
                SecondaryStat[] stats = { SecondaryStat.IndieDamR, SecondaryStat.IndieExp, SecondaryStat.DropRate, SecondaryStat.MesoUp, SecondaryStat.IndieCD, SecondaryStat.IndieBDR, SecondaryStat.IndieAllStatR, SecondaryStat.IndiePmdR };

                for (int i = 0; i < 8; i++) {
                    int value = effects.get(i);
                    applyfrom.setEffect(i, effects.get(i));
                    if (value > 0)
                        localstatups.put(stats[i], new Pair<>(value, 0));
                }

                break;
            }
            
            case 1111003: {
                localstatups.clear();
                if (applyto.getBuffedValue(1111003)) {
                    applyto.setSkillCustomInfo(1111003, applyto.getSkillCustomValue0(1111003) + 1L, 0L);
                    if (applyto.getSkillCustomValue0(1111003) > 8L) {
                        applyto.setSkillCustomInfo(1111003, 8L, 0L);
                    }
                } else {
                    applyto.setSkillCustomInfo(1111003, 1L, 0L);
                }
                localstatups.put(SecondaryStat.ComboCostInc, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(1111003), 10000));
                bufftimeR = false;
                break;
            }
            case 80003025: {
                localstatups.put(SecondaryStat.EventSpecialSkill, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 64001001: {
                localstatups.put(SecondaryStat.BeyondNextAttackProb, new Pair<Integer, Integer>(10, localDuration));
                break;
            }
            case 64001007:
            case 64001008:
            case 64001009:
            case 64001010:
            case 64001011:
            case 64001012: {
                localstatups.clear();
                localstatups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                break;
            }
            case 152121041: {
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 2500));
                break;
            }
            case 150011077: {
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(SkillFactory.getSkill(150011074).getEffect(1).getQ(), localDuration));
                applyto.getClient().getSession().writeAndFlush((Object) SLFCGPacket.OnYellowDlg(3001534, 5000, "헤이! 왓썹 브로. 롱타임노씨이지만 유와 내 하트가 뜨겁게 뛰고있으니 전혀 걱정하지 않았돠아!\r\n말이 나왔으니 오랜만에 내 뮤직을 리슨.", ""));
                break;
            }
            case 150011076: {
                localstatups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(SkillFactory.getSkill(150011074).getEffect(1).getW(), localDuration));
                applyto.getClient().getSession().writeAndFlush((Object) SLFCGPacket.OnYellowDlg(3001533, 5000, "이전 너와 교신한 후 1년 이상 지난 것이다, 치르.\n연락이 너무 뜸하다는 생각이 드는 것이다, 치르.\n바크바크 녀석처럼 마음만 있으면 된다는 생각을 하는 것은 아니라고 믿는다, 치르.", ""));
                break;
            }
            case 150011075: {
                localstatups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(SkillFactory.getSkill(150011074).getEffect(1).getS() / 2, localDuration));
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(SkillFactory.getSkill(150011074).getEffect(1).getQ() / 2, localDuration));
                localstatups.put(SecondaryStat.IndieReduceCooltime, new Pair<Integer, Integer>(SkillFactory.getSkill(150011074).getEffect(1).getW() / 2, localDuration));
                applyto.getClient().getSession().writeAndFlush((Object) SLFCGPacket.OnYellowDlg(3001532, 5000, "\uc548\ub155! \uc6b0\ub9ac\ub294 \uc0c8\ub85c\uc6b4 \uacf3\uc744 \ubc1c\uacac\ud574\uc11c \uac00\ubcf4\ub824\uace0 \ud558\ub294 \uc911\uc774\uc57c. \uc5b4\ub5a4 \uacf3\uc77c\uc9c0 \ubaa8\ub450\ub4e4 \uae30\ub300\ud558\uace0 \uc788\uc5b4...\uadf8\ub7f0\ub370 \ub610 \ubcc4\ubcfc\uc77c \uc5c6\ub294 \uacf3\uc774\uba74 \uc5b4\uca4c\uc9c0.....\ubaa8\ub450\uac00 \uc2e4\ub9dd\ud558\ub294 \uac78 \ubcf4\uae34 \uc2eb\uc740\ub370... \uadf8\ub807\ub2e4\uace0 \uac00\uc9c0 \uc54a\uc744 \uc218\ub294 \uc5c6\uace0...", ""));
                break;
            }
            case 150011078: {
                localstatups.put(SecondaryStat.IndieExp, new Pair<Integer, Integer>(SkillFactory.getSkill(150011074).getEffect(1).getS(), localDuration));
                applyto.getClient().getSession().writeAndFlush((Object) SLFCGPacket.OnYellowDlg(3001535, 3000, "\uc557, \ubc18\uc9dd\ubc18\uc9dd\ud55c \uac78 \uc900 \uc0ac\ub78c, \uc798 \uc788\uc5c8\ub0d0! \ub098 \uc694\uc998 \ubc25 \ub9ce\uc774 \uba39\uc5c8\ub354\ub2c8 \uc774\ub9cc\ud07c \ucef8\ub2e4! \uc5b4\uc81c\ub294 \ub118\uc5b4\uc9c0\uace0 \uc6b8\uc9c0\ub3c4 \uc54a\uc74c! \ud5e4\ud5e4\u2026..(\uc774\uac74 \ube44\ubc00\uc778\ub370 \uc774 \uadfc\ucc98\uc5d0\uc11c \ubc18\uc9dd\ubc18\uc9dd\ud558\ub294 \uac83\ub3c4 \ubc1c\uacac\ud588\ub2e4!)", ""));
                break;
            }
            case 164101006: {
                localstatups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(Integer.valueOf(this.level), 2000));
                break;
            }
            case 151111005: {
                bufftimeR = false;
                localstatups.clear();
                localstatups.put(SecondaryStat.Novility, new Pair<Integer, Integer>(Integer.valueOf(this.level), localDuration));
                if (applyfrom.getSkillLevel(151120038) > 0) {
                    localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(SkillFactory.getSkill(151120038).getEffect(1).getX(), localDuration));
                }
                if (primary) {
                    applyto.setSkillCustomInfo(151111005, applyto.getId(), 0L);
                    break;
                }
                applyto.setSkillCustomInfo(151111005, applyfrom.getId(), 0L);
                break;
            }
            case 151111003: {
                showEffect = false;
                break;
            }
            case 400011091: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 37121004: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 37121052: {
                aftercancel = true;
                localstatups.clear();
                localstatups.put(SecondaryStat.RwMagnumBlow, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(37121052), 0));
                bufftimeR = false;
                break;
            }
            case 37000011:
            case 37000012:
            case 37000013:
            case 37001002: {
                localstatups.clear();
                if (applyto.getBuffedValue(SecondaryStat.RWMaximizeCannon) != null) {
                    localDuration = 1000;
                }
                localstatups.put(SecondaryStat.RWOverHeat, new Pair<Integer, Integer>(1, localDuration));
                bufftimeR = false;
                break;
            }
            case 400041007: {
                localstatups.clear();
                if (primary) {
                    localstatups.put(SecondaryStat.MegaSmasher, new Pair<Integer, Integer>(-1, 90000));
                    break;
                }
                localstatups.put(SecondaryStat.MegaSmasher, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 36121014: {
                if (primary) {
                    break;
                }
                aftercancel = false;
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieHpR, new Pair<Integer, Integer>(Integer.valueOf(this.indieMhpR), 3000));
                break;
            }
            case 30000227: {
                localstatups.clear();
                localstatups.put(SecondaryStat.HiddenPieceOn, new Pair<Integer, Integer>((int) (applyto.getKeyValue(19752, "hiddenpiece") <= 0L ? 0L : applyto.getKeyValue(19752, "hiddenpiece")), 0));
                break;
            }
            case 30001080: {
                showEffect = false;
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieFloating, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.NewFlying, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400011006: {
                localstatups.clear();
                localstatups.put(SecondaryStat.BonusAttack, new Pair<Integer, Integer>(1, localDuration));
                localstatups.put(SecondaryStat.IndieCr, new Pair<Integer, Integer>(Integer.valueOf(this.indieCr), localDuration));
                localstatups.put(SecondaryStat.IndiePmdR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePmdR), localDuration));
                break;
            }
            case 400001016: {
                localstatups.clear();
                localstatups.put(SecondaryStat.DemonDamageAbsorbShield, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(400001016), localDuration));
                bufftimeR = false;
                break;
            }
            case 400021105: {
                localstatups.clear();
                int whitestack = (int) applyto.getSkillCustomValue0(400021107) * 1000;
                int blackstack = (int) applyto.getSkillCustomValue0(400021108) * 1000;
                int finalstack = 1 + (whitestack + blackstack);
                if (whitestack == blackstack) {
                    ++finalstack;
                }
                HashMap<SecondaryStat, Pair<Integer, Integer>> hashMap = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                hashMap.put(SecondaryStat.LiberationOrb, new Pair<Integer, Integer>(1, 0));
                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(hashMap, applyto));
                applyto.setSkillCustomInfo(400021105, finalstack % 10, 0L);
                applyto.setSkillCustomInfo(400021110, this.v2, 0L);
                applyto.removeSkillCustomInfo(400021107);
                applyto.removeSkillCustomInfo(400021108);
                localstatups.put(SecondaryStat.LiberationOrbActive, new Pair<Integer, Integer>(finalstack, localDuration));
                break;
            }
            case 27101003: {
                localstatups.put(SecondaryStat.BlessOfDarkness, new Pair<Integer, Integer>(1, 0));
                localDuration = 0;
                break;
            }
            case 22110016: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                break;
            }
            case 50001214: {
                localstatups.clear();
                if (!applyto.getBuffedValue(50001214)) {
                    applyto.setSkillCustomInfo(50001214, this.y, 0L);
                } else {
                    applyto.addSkillCustomInfo(50001214, -1L);
                }
                if (applyto.getSkillCustomValue0(50001214) <= 0L) {
                    applyto.removeSkillCustomInfo(50001214);
                    applyto.cancelEffect(this);
                    break;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                localstatups.put(SecondaryStat.MichaelProtectofLight, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(50001214), localDuration));
                break;
            }
            case 51121054: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                localstatups.put(SecondaryStat.DamAbsorbShield, new Pair<Integer, Integer>(this.x, localDuration));
                break;
            }
            case 400001043: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>((int) ((long) this.q + applyfrom.getSkillCustomValue0(400001043)), localDuration));
                localstatups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 5221054: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IgnoreMobDamR, new Pair<Integer, Integer>(this.w, localDuration));
                applyto.addHP(applyto.getStat().getCurrentMaxHp() / 100L * (long) this.z);
                break;
            }
            case 80001878: {
                localstatups.clear();
                localstatups.put(SecondaryStat.FixCooltime, new Pair<Integer, Integer>(5, 60000));
                break;
            }
            case 63121044: {
                applyto.setSkillCustomInfo(this.sourceid, applyfrom.getId(), 0L);
                localstatups.clear();
                localstatups.put(SecondaryStat.IncarnationAura, new Pair<Integer, Integer>(1, localDuration));
                if (!primary) {
                    break;
                }
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(this.indieStance), localDuration));
                localstatups.put(SecondaryStat.IndiePadR, new Pair<Integer, Integer>(Integer.valueOf(this.indiePadR), localDuration));
                break;
            }
            case 400031066: {
                aftercancel = true;
                localstatups.clear();
                localstatups.put(SecondaryStat.GripOfAgony, new Pair<Integer, Integer>((int) applyfrom.getSkillCustomValue0(400031066), 0));
                break;
            }
            case 400031062: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(Integer.valueOf(this.indieDamR), localDuration));
                localstatups.put(SecondaryStat.IndieStance, new Pair<Integer, Integer>(Integer.valueOf(this.indieStance), localDuration));
                localstatups.put(SecondaryStat.ThanatosDescent, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 400031064: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 6720));
                break;
            }
            case 63121008: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieNotDamaged, new Pair<Integer, Integer>(1, 3660));
                localstatups.put(SecondaryStat.KeyDownMoving, new Pair<Integer, Integer>(100, 3660));
                break;
            }
            case 63111013: {
                localstatups.clear();
                localstatups.put(SecondaryStat.DeathBlessing, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 63111009: {
                localstatups.clear();
                localstatups.put(SecondaryStat.RemainIncense, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 60030241:
            case 80003015: {
                localstatups.clear();
                localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(this.y, localDuration));
                break;
            }
            case 80003018: {
                aftercancel = true;
                localstatups.clear();
                localstatups.put(SecondaryStat.PriorPryperation, new Pair<Integer, Integer>((int) applyto.getSkillCustomValue0(this.sourceid), 0));
                break;
            }
            case 410115:
            case 63001002: {
                localstatups.clear();
                localstatups.put(SecondaryStat.DarkSight, new Pair<Integer, Integer>(Integer.valueOf(this.level), 420));
                break;
            }
            case 63101005: {
                localstatups.clear();
                localstatups.put(SecondaryStat.DragonPang, new Pair<Integer, Integer>(1, 0));
                break;
            }
            case 63101001: {
                localstatups.clear();
                localstatups.put(SecondaryStat.Possession, new Pair<Integer, Integer>(1, localDuration));
                break;
            }
            case 1210016: {
                localstatups.clear();
                localstatups.put(SecondaryStat.BlessingArmor, new Pair<Integer, Integer>((int) applyfrom.getSkillCustomValue0(this.sourceid), localDuration));
                localstatups.put(SecondaryStat.BlessingArmorIncPad, new Pair<Integer, Integer>(Integer.valueOf(this.epad), localDuration));
                break;
            }
            default: {
                if (localDuration != Integer.MAX_VALUE) {
                    break;
                }
                localDuration = 0;
                break;
            }
        }
        if (this.getSummonMovementType() != null) {
            switch (this.sourceid) {
                case 1301013:
                case 5321003:
                case 14000027:
                case 14121003:
                case 32001014:
                case 32100010:
                case 32110017:
                case 32120019:
                case 33001007:
                case 33001008:
                case 33001009:
                case 33001010:
                case 33001011:
                case 33001012:
                case 33001013:
                case 33001014:
                case 33001015:
                case 35111002:
                case 35120002:
                case 80002888:
                case 101100101:
                case 131002015:
                case 131002022:
                case 131003022:
                case 131004022:
                case 131005022:
                case 131006022:
                case 151100002:
                case 162101012:
                case 164121011:
                case 400011013:
                case 400011014:
                case 400011065:
                case 400011078:
                case 400021071:
                case 400021095:
                case 400031008:
                case 400031009:
                case 400041033:
                case 400051011:
                case 400051017: {
                    break;
                }
                case 36121014: {
                    if (!primary) {
                        break;
                    }
                    localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                    break;
                }
                default: {
                    if (!GameConstants.isAfterRemoveSummonSkill(this.sourceid) && this.sourceid != 400021092) {
                        if (this.sourceid == 400021047) {
                            if (applyto.getBuffedValue(this.sourceid)) {
                                break;
                            }
                            localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                            break;
                        }
                        localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                        break;
                    }
                    if (this.sourceid != 400021069) {
                        break;
                    }
                    localstatups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, localDuration));
                    break;
                }
            }
        }
        if (GameConstants.MovedSkill(this.sourceid) || this.sourceid == 400001050 || this.sourceid == 400051000 || this.sourceid == 15001021 || this.sourceid == 63001004 || this.sourceid == 63001006 || localstatups.containsKey(SecondaryStat.ReviveOnce)) {
            showEffect = false;
        }
        for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> entry : this.statups.entrySet()) {
            if (localstatups.containsKey(entry.getKey())) {
                continue;
            }
            localstatups.put(entry.getKey(), new Pair(entry.getValue().left, localDuration));
        }
        if (this.sourceid == 5321054 && applyto.getKeyValue(51384, "ww_buck") != -1L) {
            Pair vz = (Pair) localstatups.get(SecondaryStat.IndiePmdR);
            localstatups.remove(SecondaryStat.IndiePmdR);
            vz.left = (Integer) vz.left * -1 + 100;
            localstatups.put(SecondaryStat.IndiePmdR, vz);
        }
        if (this.sourceid == 80002924 && applyto.getKeyValue(53714, "atk") != -1L) {
            localstatups.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>((int) applyto.getKeyValue(53714, "atk"), 0));
        }
        if (this.sourceid == 400001012) {
            applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 3111005);
            applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 3211005);
            applyto.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 3311009);
        }
        if (this.isMonsterRiding() && !localstatups.containsKey(SecondaryStat.RideVehicle)) {
            if (localDuration >= 2100000000 || localDuration < 0) {
                localDuration = 0;
            }
            localstatups.put(SecondaryStat.RideVehicle, new Pair<Integer, Integer>(SecondaryStatEffect.parseMountInfo(applyto, this.sourceid), 0));
        } else if (SkillFactory.getSkill(this.sourceid) != null && this.sourceid != 22171080 && SkillFactory.getSkill(this.sourceid).getVehicleID() > 0) {
            if (localDuration >= 2100000000 || localDuration < 0) {
                localDuration = 0;
            }
            localstatups.put(SecondaryStat.RideVehicle, new Pair<Integer, Integer>(SkillFactory.getSkill(this.sourceid).getVehicleID(), localDuration));
            if (applyfrom.getMapId() == ServerConstants.warpMap && this.sourceid / 10000 == 8000) {
                applyfrom.getClient().send(CField.UIPacket.detailShowInfo("휴식 포인트 적립을 시작합니다.", 3, 20, 20));
                applyfrom.setSkillCustomInfo(applyfrom.getMapId(), 0L, 60000L);
            }
        }
        if (this.skill && !applyto.isHidden() && !isPetBuff && applyfrom.getId() == applyto.getId()) {
            if (this.sourceid >= 400041009 && this.sourceid <= 400041015 && showEffect && applyfrom.getId() == applyto.getId()) {
                applyto.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(applyto, 0, this.sourceid, 1, 0, 0, (byte) (applyto.getTruePosition().x > pos.x ? 1 : 0), true, pos, null, null));
                applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showEffect(applyto, 0, this.sourceid, 1, 0, 0, (byte) (applyto.getTruePosition().x > pos.x ? 1 : 0), true, pos, null, null), false);
            }
            if (!GameConstants.isLinkMap(applyto.getMapId()) && showEffect) {
                if (this.sourceid != 400051334 && (this.sourceid < 400041009 || this.sourceid > 400041015)) {
                    applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showEffect(applyto, 0, this.sourceid, 1, 0, 0, (byte) (applyto.getTruePosition().x > pos.x ? 1 : 0), false, pos, null, null), false);
                }
            } else if (this.isPartyBuff(applyfrom, applyto)) {
                Rectangle bounds = this.calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
                if (this.sourceid == 155001001 || this.sourceid == 155101003 || this.sourceid == 155111005 || this.sourceid == 155121005) {
                    bounds = SkillFactory.getSkill(155121043).getEffect(1).calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
                }
                List<MapleMapObject> list = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.PLAYER}));
                for (MapleMapObject affectedmo : list) {
                    MapleCharacter affected = (MapleCharacter) affectedmo;
                    if (applyfrom.getId() == affected.getId()) {
                        continue;
                    }
                    this.applyBuffEffect(applyto, affected, primary, localDuration, pos, showEffect);
                    affected.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(affected, 0, this.sourceid, 4, 0, 0, (byte) (affected.getTruePosition().x > pos.x ? 1 : 0), true, pos, null, null));
                    affected.getMap().broadcastMessage(affected, CField.EffectPacket.showEffect(affected, 0, this.sourceid, 4, 0, 0, (byte) (affected.getTruePosition().x > pos.x ? 1 : 0), false, pos, null, null), false);
                }
            }
        }
        if (localstatups.containsKey(SecondaryStat.JaguarSummoned)) {
            applyto.cancelEffectFromBuffStat(SecondaryStat.RideVehicle);
        }
        for (int FreudsProtection : FreudsProtections = new int[]{400001025, 400001026, 400001027, 400001028, 400001029, 400001030}) {
            if (this.sourceid != FreudsProtection) {
                continue;
            }
            for (int s : FreudsProtections) {
                if (!applyto.getBuffedValue(s)) {
                    continue;
                }
                applyto.cancelEffect(applyto.getBuffedEffect(s), null, true);
            }
            break;
        }

        // BroadCast Effect to Self
        long starttime = System.currentTimeMillis();
        boolean exit = false;
        this.setStarttime((int) (starttime % 1000000000L));
        List<Pair<SecondaryStat, SecondaryStatValueHolder>> addV = new ArrayList<>();
        for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup : localstatups.entrySet()) {
            if (applyto.getBuffedEffect(statup.getKey(), this.sourceid) != null) {
                applyto.cancelEffect(this, Arrays.asList(statup.getKey()), true);

                if (statup.getValue().right == 0 && !aftercancel) {
                    exit = true;
                    continue;
                }
            }

            if (statup.getValue().right > 0) {
                aftercancel = true;
                if (this.skill && SkillFactory.getSkill(this.sourceid) != null && !SkillFactory.getSkill(this.sourceid).isHyper() && bufftimeR && this.sourceid < 400000000 && statup.getKey() != SecondaryStat.IndieNotDamaged && statup.getKey() != SecondaryStat.NotDamaged && this.getSummonMovementType() == null) {
                    statup.getValue().right = this.alchemistModifyVal(applyfrom, statup.getValue().right, false);
                }
//                System.out.println("버프 시작점 : " + SkillFactory.getSkill(this.sourceid).getName() + " / " + SkillFactory.getSkill(this.sourceid).getId());
                applyto.registerEffect(this, starttime, statup, false, applyfrom.getId(), indieList1, indieList2);
            } else {
//                System.out.println("버프 종착점 : " + SkillFactory.getSkill(this.sourceid).getName() + " / " + SkillFactory.getSkill(this.sourceid).getId());
                addV.add(new Pair(statup.getKey(), new SecondaryStatValueHolder(this, starttime, (statup.getValue()).left, statup.getValue().right, applyfrom.getId(), indieList1, indieList2)));
            }
        }
        if (exit) {
            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, true, false));
            return;
        }
        if (!addV.isEmpty()) {
            applyto.getEffects().addAll(addV);
        }
        if (this.isHide()) {
            applyto.getMap().broadcastMessage(applyto, CField.removePlayerFromMap(applyto.getId()), false);
        }
        if (this.sourceid >= 400041002 && this.sourceid <= 400041005 || this.sourceid == 11121014) {
            showEffect = true;
        }
        if (showEffect && primary && this.getSummonMovementType() == null) {
            if (SkillFactory.getSkill(this.sourceid) != null && (this.sourceid == 24121003 || this.sourceid == 30010186 || this.isHeroWill() || localstatups.size() > 0 || this.isMist() || this.damage > 0 || SkillFactory.getSkill(this.sourceid).getType() == 41 || SkillFactory.getSkill(this.sourceid).getType() == 51)) {
                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, true, false));
            } else {
                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, false, false));
            }
        }
        if (localstatups.size() > 0) {
            switch (this.sourceid) {
                case 2003550:
                case 2003551:
                case 2023556:
                case 2023558:
                case 2023661:
                case 2023662:
                case 2023663:
                case 2023664:
                case 2023665:
                case 2023666:
                case 2024017:
                case 2024018:
                case 2450038:
                case 2450064:
                case 2450124:
                case 2450134:
                case 2450147:
                case 2450148:
                case 2450149: {
                    if (!primary) {
                        break;
                    }
                    applyto.addCooldown(this.sourceid, System.currentTimeMillis(), localDuration);
                    break;
                }
            }
            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, this, applyto));
            applyto.getMap().broadcastMessage(applyto, CWvsContext.BuffPacket.giveForeignBuff(applyto, localstatups, this), false);
            if (showEffect || this.sourceid == 3110001 || this.sourceid == 3210001 || this.sourceid == 3110012 || this.sourceid == 3101009) {
                applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, false, true));
            }
            if (this.sourceid != 21110016) {
                if (this.sourceid != 21121058) {
                    if (this.sourceid != 400031005) {
                        return;
                    }
                    int size3 = 0;
                    for (int i2 = 33001007; i2 < 33001015; ++i2) {
                        if (GameConstants.getJaguarSummonId(i2) > 0) {
                            if (GameConstants.getSelectJaguarSkillId(GameConstants.getMountItem(33001001, applyto)) != i2) {
                                final MapleSummon tosummon = new MapleSummon(applyfrom, i2, new Point(applyto.getPosition().x + Randomizer.rand(-400, 400), applyto.getPosition().y), SummonMovementType.SUMMON_JAGUAR, (byte) 0, localDuration);
                                tosummon.setPosition(new Point(applyto.getPosition().x + Randomizer.rand(-400, 400), applyto.getPosition().y));
                                applyfrom.getMap().spawnSummon(tosummon, localDuration);
                                applyfrom.addSummon(tosummon);
                                ++size3;
                            }
                        }
                        if (size3 == 6) {
                            return;
                        }
                    }
                }
                applyto.getClient().send(CField.aranCombo(500));
                return;
            }
            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, true, false));
            return;
        }
    }

    //와드
    public void giveAnotherBuff(MapleCharacter applyto, MapleCharacter applyfrom, Map<SecondaryStat, Pair<Integer, Integer>> localstatups, boolean BuffTimeR) {
        ArrayList<Pair<SecondaryStat, SecondaryStatValueHolder>> addV = new ArrayList<Pair<SecondaryStat, SecondaryStatValueHolder>>();
        ArrayList<Pair<Integer, Integer>> indieList1 = new ArrayList<Pair<Integer, Integer>>();
        ArrayList<Pair<Integer, Integer>> indieList2 = new ArrayList<Pair<Integer, Integer>>();
        long starttime = System.currentTimeMillis();
        boolean exit = false;
        for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup : localstatups.entrySet()) {
            if ((Integer) statup.getValue().right > 0) {
                if (this.skill && SkillFactory.getSkill(this.sourceid) != null && !SkillFactory.getSkill(this.sourceid).isHyper() && BuffTimeR && this.sourceid < 400000000 && statup.getKey() != SecondaryStat.IndieNotDamaged && statup.getKey() != SecondaryStat.NotDamaged) {
                    statup.getValue().right = this.alchemistModifyVal(applyfrom, (Integer) statup.getValue().right, false);
                }
                applyto.registerEffect(this, starttime, statup, false, applyfrom.getId(), indieList1, indieList2);
                continue;
            }
            addV.add(new Pair<SecondaryStat, SecondaryStatValueHolder>(statup.getKey(), new SecondaryStatValueHolder(this, starttime, (Integer) statup.getValue().left, (Integer) statup.getValue().right, applyfrom.getId(), indieList1, indieList2)));
        }
        if (exit) {
            applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(applyto, true, false));
            return;
        }
        applyto.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, this, applyto));
        applyto.getMap().broadcastMessage(applyto, CWvsContext.BuffPacket.giveForeignBuff(applyto, localstatups, this), false);
    }

    public int getMonsterRidingId() {
        return this.monsterRidingId;
    }

    public static final int parseMountInfo(MapleCharacter player, int skillid) {
        switch (skillid) {
            case 1004:
            case 10001004:
            case 20001004:
            case 20011004:
            case 20021004:
            case 20031004:
            case 30001004:
            case 30011004:
            case 50001004: {
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -122) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -122).getItemId();
                }
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -22) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -22).getItemId();
                }
                return 0;
            }
            case 35001002:
            case 35111003:
            case 35120000: {
                return 1932016;
            }
            case 400031017: {
                return 1932417;
            }
            case 80002996: {
                return 1932691;
            }
        }
        return GameConstants.getMountItem(skillid, player);
    }

    private final int calcHPChange(MapleCharacter applyfrom, boolean primary) {
        MapleMonster seren;
        int hpchange = 0;
        if (this.hp != 0 && applyfrom.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
            if (!this.skill) {
                hpchange = primary ? (hpchange += this.alchemistModifyVal(applyfrom, this.hp, true)) : (hpchange += this.hp);
                if (applyfrom.hasDisease(SecondaryStat.Undead)) {
                    hpchange = applyfrom.getDisease(SecondaryStat.Undead).getMobskill().getSkillLevel() == 17 ? (hpchange *= -1) : (hpchange /= 2);
                }
            } else {
                hpchange += SecondaryStatEffect.makeHealHP((double) this.hp / 100.0, applyfrom.getStat().getTotalMagic(), 3.0, 5.0);
                if (applyfrom.hasDisease(SecondaryStat.Undead)) {
                    hpchange = -hpchange;
                }
            }
        }
        if (this.hpR != 0.0 && applyfrom.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
            hpchange += (int) ((double) applyfrom.getStat().getCurrentMaxHp() * this.hpR) / (applyfrom.hasDisease(SecondaryStat.Undead) ? 2 : 1);
            if (applyfrom.hasDisease(SecondaryStat.Undead) && applyfrom.getDisease(SecondaryStat.Undead).getMobskill().getSkillLevel() == 17) {
                hpchange *= -1;
            }
        }
        if (applyfrom.getBuffedValue(SecondaryStat.DemonFrenzy) != null) {
            hpchange /= 100;
        }
        if (this.hpRCon != 0.0) {
            hpchange -= (int) ((double) applyfrom.getStat().getCurrentMaxHp() * this.hpRCon);
        }
        if (applyfrom.getSkillCustomValue0(143143) == 1L) {
            hpchange /= 10;
        }
        if (hpchange > 0 && applyfrom.getMapId() == 410002060 && (seren = applyfrom.getMap().getMonsterById(8880602)) != null && seren.getSerenTimetype() == 2) {
            hpchange = hpchange * 20 / 100;
        }
        if (primary && this.hpCon != 0) {
            hpchange -= this.hpCon;
        }
        if (applyfrom.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
            switch (this.sourceid) {
                case 1211010: {
                    hpchange = (int) (applyfrom.getStat().getCurrentMaxHp() * (long) Math.max(10, this.x - applyfrom.getListonation() * 10) / 100L);
                    break;
                }
                case 1320019: {
                    hpchange = 0;
                }
            }
        }
        if (applyfrom.getSkillLevel(30010242) > 0 && hpchange < 0 && (applyfrom.getBuffedEffect(SecondaryStat.DebuffIncHp) != null || applyfrom.getBuffedValue(SecondaryStat.DemonFrenzy) != null && applyfrom.getBuffedEffect(SecondaryStat.DemonFrenzy).getQ2() < applyfrom.getStat().getHPPercent()) && this.skill) {
            hpchange = hpchange / 100 * (100 - SkillFactory.getSkill(30010242).getEffect(1).getX());
        }
        if (hpchange < 0 && applyfrom.getBuffedValue(31221054)) {
            hpchange = 0;
        }
        return hpchange;
    }

    private static final int makeHealHP(double rate, double stat, double lowerfactor, double upperfactor) {
        return (int) (Math.random() * (double) ((int) (stat * upperfactor * rate) - (int) (stat * lowerfactor * rate) + 1) + (double) ((int) (stat * lowerfactor * rate)));
    }

    private final int calcMPChange(MapleCharacter applyfrom, boolean primary) {
        int mpchange = 0;
        if (this.mp != 0) {
            mpchange = primary ? (mpchange += this.alchemistModifyVal(applyfrom, this.mp, false)) : (mpchange += this.mp);
        }
        if (this.mpR != 0.0) {
            mpchange += (int) ((double) applyfrom.getStat().getCurrentMaxMp(applyfrom) * this.mpR);
        }
        if (GameConstants.isDemonSlayer(applyfrom.getJob())) {
            mpchange = 0;
        }
        if (primary) {
            if (this.mpCon != 0 && !GameConstants.isDemonSlayer(applyfrom.getJob())) {
                mpchange = applyfrom.getBuffedValue(SecondaryStat.InfinityForce) != null || applyfrom.energyCharge ? 0 : (int) ((double) mpchange - (double) (this.mpCon - this.mpCon * applyfrom.getStat().mpconReduce / 100) * ((double) applyfrom.getStat().mpconPercent / 100.0));
            } else if (this.getForceCon() != 0 && GameConstants.isDemonSlayer(applyfrom.getJob())) {
                mpchange = applyfrom.getBuffedValue(SecondaryStat.InfinityForce) != null ? 0 : (mpchange -= this.getForceCon());
                if (applyfrom.getSkillLevel(31120048) > 0 && this.sourceid == 31121005) {
                    mpchange /= 2;
                }
                if (applyfrom.getSkillLevel(31120051) > 0 && this.sourceid == 31121001) {
                    mpchange /= 2;
                }
                if (applyfrom.getSkillLevel(31121054) > 0) {
                    mpchange = mpchange * 8 / 10;
                }
            }
        }
        if (applyfrom.getBuffedValue(SecondaryStat.Overload) != null) {
            mpchange = 0;
        }
        if ((applyfrom.getBuffedValue(20040217) || applyfrom.getBuffedValue(20040219) || applyfrom.getBuffedValue(20040220)) && (GameConstants.isDarkSkills(this.sourceid) || (applyfrom.getBuffedValue(20040219) || applyfrom.getBuffedValue(20040220)) && (this.sourceid == 27121303 || this.sourceid == 27111303))) {
            mpchange = 0;
        }
        switch (this.sourceid) {
            case 1320019: {
                mpchange = 0;
            }
        }
        return mpchange;
    }

    public final int alchemistModifyVal(MapleCharacter chr, int val, boolean withX) {
        if (!this.skill) {
            return val * (100 + (withX ? chr.getStat().RecoveryUP : chr.getStat().BuffUP)) / 100;
        }
        if (this.getSummonMovementType() == null) {
            int bufftime = chr.getStat().BuffUP_Skill;
            if (GameConstants.isPhantom(chr.getJob()) && chr.getSkillLevel(24120050) > 0) {
                for (Pair<Integer, Boolean> sk : chr.getStolenSkills()) {
                    if ((Integer) sk.left != this.sourceid) {
                        continue;
                    }
                    bufftime += 10;
                    break;
                }
            }
            return val * (100 + bufftime) / 100;
        }
        return val * (100 + chr.getStat().BuffUP_Summon) / 100;
    }

    public final int calcPowerChange(MapleCharacter applyfrom, boolean primary) {
        int powerchange = 0;
        if (!primary) {
            return 0;
        }
        if (this.powerCon != 0 && GameConstants.isXenon(applyfrom.getJob())) {
            powerchange = applyfrom.getBuffedValue(SecondaryStat.AmaranthGenerator) != null || applyfrom.getBuffedValue(SecondaryStat.Overload) != null ? 0 : (int) this.powerCon;
        }
        return powerchange;
    }

    public final void setSourceId(int newid) {
        this.sourceid = newid;
    }

    public final boolean isInflation() {
        return this.inflation > 0;
    }

    public final int getInflation() {
        return this.inflation;
    }

    private boolean isPartyBuff(MapleCharacter applyfrom, MapleCharacter applyto) {
        SecondaryStat[] partybuff;
        if (this.lt == null || this.rb == null || applyfrom.getMapId() == 450013700 || applyto.getMapId() == 450013700) {
            return false;
        }
        for (SecondaryStat buff : partybuff = new SecondaryStat[]{SecondaryStat.BasicStatUp, SecondaryStat.MaxLevelBuff}) {
            if (!this.statups.containsKey(buff)) {
                continue;
            }
            return true;
        }
        if (SkillFactory.getSkill(this.sourceid).isHyper() && (this.sourceid % 100 == 53 || this.sourceid == 25121132)) {
            int job = this.sourceid / 10000;
            if (job < 1000 && applyfrom.getJob() < 1000 && applyto.getJob() < 1000) {
                return true;
            }
            if (job >= 1000 && job < 2000 && applyfrom.getJob() >= 1000 && applyfrom.getJob() < 2000 && applyto.getJob() >= 1000 && applyto.getJob() < 2000) {
                return true;
            }
            if (job >= 2000 && job < 3000 && applyfrom.getJob() >= 1000 && applyfrom.getJob() < 3000 && applyto.getJob() >= 2000 && applyto.getJob() < 3000) {
                return true;
            }
            if (job >= 3000 && job < 4000 && applyfrom.getJob() >= 1000 && applyfrom.getJob() < 4000 && applyto.getJob() >= 3000 && applyto.getJob() < 4000) {
                return true;
            }
        }
        switch (this.sourceid) {
            case 1101006:
            case 1211011:
            case 1301006:
            case 1301007:
            case 1310016:
            case 2101001:
            case 2201001:
            case 2301002:
            case 2301004:
            case 2311001:
            case 2311003:
            case 2311009:
            case 2321005:
            case 2321007:
            case 2321052:
            case 2321055:
            case 3121002:
            case 3221002:
            case 3321022:
            case 4001005:
            case 4101004:
            case 4111001:
            case 4201003:
            case 4301003:
            case 5121009:
            case 5301003:
            case 5320008:
            case 12101000:
            case 13121005:
            case 14001022:
            case 14101003:
            case 15121005:
            case 21111012:
            case 22151003:
            case 22171054:
            case 27111006:
            case 27111101:
            case 32001003:
            case 32101003:
            case 33101005:
            case 33121004:
            case 51101004:
            case 51111008:
            case 131001009:
            case 131001013:
            case 131001113:
            case 135001009:
            case 152121043:
            case 400021077:
            case 400031038:
            case 400041011:
            case 400041012:
            case 400041013:
            case 400041014:
            case 400041015: {
                return true;
            }
            case 155001001:
            case 155101003:
            case 155111005:
            case 155121005: {
                return applyfrom.getBuffedValue(155121043);
            }
        }
        if (this.isHeal() || this.isResurrection() || this.isTimeLeap()) {
            return !this.isResurrection() && !this.isHeal() || !GameConstants.보스맵(applyfrom.getMapId()) || applyto.getDeathCount() > 0;
        }
        return false;
    }

    public final boolean isHeal() {
        return this.skill && (this.sourceid == 9101000 || this.sourceid == 9001000);
    }

    public final boolean isResurrection() {
        return this.skill && (this.sourceid == 9001005 || this.sourceid == 9101005 || this.sourceid == 2321006 || this.sourceid == 1221016);
    }

    public final boolean isTimeLeap() {
        return this.skill && this.sourceid == 5121010;
    }

    public final short getHp() {
        return this.hp;
    }

    public final short getMp() {
        return this.mp;
    }

    public final double getHpR() {
        return this.hpR;
    }

    public final double getMpR() {
        return this.mpR;
    }

    public final byte getMastery() {
        return this.mastery;
    }

    public final short getWatk() {
        return this.pad;
    }

    public final short getMatk() {
        return this.mad;
    }

    public final short getMdef() {
        return this.mdef;
    }

    public final short getAcc() {
        return this.acc;
    }

    public final short getAvoid() {
        return this.avoid;
    }

    public final short getHands() {
        return this.hands;
    }

    public final short getSpeed() {
        return this.speed;
    }

    public final short getJump() {
        return this.jump;
    }

    public final short getPassiveSpeed() {
        return this.psdSpeed;
    }

    public final short getPassiveJump() {
        return this.psdJump;
    }

    public final int getDuration() {
        return this.duration;
    }

    public final int getSubTime() {
        return this.subTime;
    }

    public final Map<SecondaryStat, Pair<Integer, Integer>> getStatups() {
        return this.statups;
    }

    public final boolean sameSource(SecondaryStatEffect effect) {
        boolean sameSrc = this.sourceid == effect.sourceid;
        switch (this.sourceid) {
            case 32120013: {
                sameSrc = effect.sourceid == 32001003;
                break;
            }
            case 32120015: {
                sameSrc = effect.sourceid == 32111012;
                break;
            }
            case 32120014: {
                sameSrc = effect.sourceid == 32101003;
                break;
            }
            case 35120000: {
                sameSrc = effect.sourceid == 35001002;
                break;
            }
            case 35121013: {
                sameSrc = effect.sourceid == 35111004;
            }
        }
        return effect != null && sameSrc && this.skill == effect.skill;
    }

    public final int getCr() {
        return this.cr;
    }

    public final double getT() {
        return this.t;
    }

    public final int getU() {
        return this.u;
    }

    public final int getV() {
        return this.v;
    }

    public final void setV(int newvalue) {
        this.v = newvalue;
    }

    public final int getW() {
        return this.w;
    }

    public final int getX() {
        return this.x;
    }

    public final int addX(int b) {
        return this.x + b;
    }

    public final int getY() {
        return this.y;
    }

    public final void setY(int newvalue) {
        this.y = newvalue;
    }

    public final int getZ() {
        return this.z;
    }

    public final int getS() {
        return this.s;
    }

    public final short getDamage() {
        return this.damage;
    }

    public final short getPVPDamage() {
        return this.PVPdamage;
    }

    public final byte getAttackCount() {
        return this.attackCount;
    }

    public final byte getBulletCount() {
        return this.bulletCount;
    }

    public final int getBulletConsume() {
        return this.bulletConsume;
    }

    public final byte getMobCount() {
        return this.mobCount;
    }

    public final int getMoneyCon() {
        return this.moneyCon;
    }

    public boolean cantIgnoreCooldown() {
        switch (this.sourceid) {
            case 1320016:
            case 1320019:
            case 2121004:
            case 2221004:
            case 2321004:
            case 2321055:
            case 5121010:
            case 12111023:
            case 12111029:
            case 14110030:
            case 21121058:
            case 24111002:
            case 30001062:
            case 64121001:
            case 64121053:
            case 80000299:
            case 80000300:
            case 80000301:
            case 80000302:
            case 80000303:
            case 80001455:
            case 80001456:
            case 80001457:
            case 80001458:
            case 80001459:
            case 80001460:
            case 80001461:
            case 80001462:
            case 80001463:
            case 80001464:
            case 80001465:
            case 80001466:
            case 80001467:
            case 80001468:
            case 80001469:
            case 80001470:
            case 80001471:
            case 80001472:
            case 80001473:
            case 80001474:
            case 80001475:
            case 80001476:
            case 80001477:
            case 80001478:
            case 80001479:
            case 100001272:
            case 100001274:
            case 100001281:
            case 150011074:
            case 155111306:
            case 155121306:
            case 155121341:
            case 164121000:
            case 164121041:
            case 164121042: {
                return true;
            }
        }
        return false;
    }

    public boolean ignoreCooldown(MapleCharacter chra) {
        Skill skill = SkillFactory.getSkill(this.sourceid);
        if (skill.isHyper() || skill.isVMatrix() || skill.isNotCooltimeReset() || this.cantIgnoreCooldown()) {
            return false;
        }
        return Randomizer.isSuccess(chra.getStat().randCooldown);
    }

    // 쿨타임
    /*public final int getCooldown(MapleCharacter chra) {
        int localCooltime = 0;
//        int n = this.cooltime > 0 ? this.cooltime : (localCooltime = this.cooltimeMS > 0 ? this.cooltimeMS : 0);
        if (chra.getStat().getReduceCooltime().size() > 0 && localCooltime > 0) {
            for (Integer cool : chra.getStat().getReduceCooltime()) {
                if (cool <= 0) continue;
                if (localCooltime > 10000) {
                    localCooltime -= cool * 1000;
                    continue;
                }
                localCooltime -= localCooltime / 100 * 10;
            }
        }
        if (this.sourceid == 31121003 && chra.getBuffedEffect(SecondaryStat.InfinityForce) != null) {
            localCooltime /= 2;
        }
        if (chra.getSkillLevel(71000231) > 0) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)71000231).getEffect((int)chra.getSkillLevel((int)71000231)).coolTimeR / 100;
//            localCooltime -= localCooltime * SkillFactory.getSkill(71000231).getEffect(chra.getSkillLevel(71000231)).coolTimeR / 100;
        }
        if (chra.getSkillLevel(1220051) > 0 && this.sourceid == 1221011) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)1220051).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(2120051) > 0 && this.sourceid == 2121003) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)2120051).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(14120046) > 0 && this.sourceid == 14121003) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)14120046).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(22170087) > 0 && (this.sourceid == 22141012 || this.sourceid == 22140022)) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)22170087).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(22170090) > 0 && this.sourceid == 22171063) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)22170090).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(22170084) > 0 && this.sourceid == 22110023) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)22170084).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(24120044) > 0 && this.sourceid == 24121005) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)24120044).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(32120057) > 0 && this.sourceid == 32121004) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)32120057).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(32120063) > 0 && this.sourceid == 32121006) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)32120063).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(33120048) > 0 && (this.sourceid == 33111006 || this.sourceid == 33101215 || this.sourceid == 33121002)) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)33120048).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(35120045) > 0 && this.sourceid == 35111002) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)35120045).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(64120051) > 0 && this.sourceid == 64121001) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)64120051).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(65120048) > 0 && this.sourceid == 65121002) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)65120048).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(142120040) > 0 && this.sourceid == 142121004) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)142120040).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(152120036) > 0 && this.sourceid == 152121004) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)152120036).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(155120038) > 0 && this.sourceid == 155111306) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)155120038).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(151120036) > 0 && this.sourceid == 151121003) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)151120036).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getSkillLevel(162120035) > 0 && this.sourceid == 162111005) {
            localCooltime -= localCooltime * SkillFactory.getSkill((int)162120035).getEffect((int)1).coolTimeR / 100;
        }
        if (chra.getBuffedEffect(SecondaryStat.IndieSummon, 80001809) != null) {
            localCooltime -= 2000;
        }
        if (localCooltime > 0 && chra.getBuffedValue(SecondaryStat.FixCooltime) != null && this.sourceid / 10000 != 8000 && this.sourceid / 10000 <= chra.getJob()) {
            localCooltime = chra.getBuffedValue(SecondaryStat.FixCooltime) * 1000;
        } else if ((this.cooltime > 5000 || this.cooltimeMS > 5000) && localCooltime < 5000) {
            localCooltime = 50000;
        }
        return Math.max(0, localCooltime);
    }*/
    public final int getCooldown(final MapleCharacter chra) { // 쿨타임
        int localCooltime = 0;
        int ItemCooltime = 0;
        int minusCooltime = 0;

        if (cooltime == 0 && cooltimeMS == 0) {
            return 0;
        }

        if (cooltime > 0) {
            if (cooltime < 5000) {
                return cooltime;
            }
            /* if (cooltime > 10) {
                localCooltime = (int) (cooltime - (chra.getStat().reduceCooltime * 1000));
            } else {
                localCooltime = Math.max(5, (cooltime * (100 - (chra.getStat().reduceCooltime * 5)) / 100));
            }*/

            localCooltime = cooltime;

            ItemCooltime = (int) chra.getStat().reduceCooltime * 1000;
            minusCooltime = 0;

            if (chra.getBuffedEffect(SecondaryStat.IndieReduceCooltime) != null && !GameConstants.isBeginnerJob(sourceid / 10000) && !SkillFactory.getSkill(sourceid).isVMatrix() && ((sourceid / 1000) != 8000)) {
                localCooltime -= localCooltime * chra.getBuffedValue(SecondaryStat.IndieReduceCooltime) / 100;
            }
        } else if (cooltimeMS > 0) {
            if (cooltimeMS < 5000) {
                return cooltimeMS;
            }
            if (cooltimeMS > 10000) {
                localCooltime = (int) (cooltimeMS - (chra.getStat().reduceCooltime * 1000));
            } else {
                localCooltime = Math.max(5, (cooltimeMS * (100 - (chra.getStat().reduceCooltime * 5)) / 100));
            }
            if (chra.getBuffedEffect(SecondaryStat.IndieReduceCooltime) != null && !GameConstants.isBeginnerJob(sourceid / 10000) && !SkillFactory.getSkill(sourceid).isVMatrix() && ((sourceid / 1000) != 8000)) {
                localCooltime -= localCooltime * chra.getBuffedValue(SecondaryStat.IndieReduceCooltime) / 100;
            }
        }

        if (sourceid == 31121003 && chra.getBuffedEffect(SecondaryStat.InfinityForce) != null) {
            localCooltime /= 2;
        }

        if (chra.getSkillLevel(71000231) > 0) {
            localCooltime -= localCooltime * SkillFactory.getSkill(71000231).getEffect(chra.getSkillLevel(71000231)).coolTimeR / 100;
        }

        if (chra.getSkillLevel(1220051) > 0 && sourceid == 1221011) {
            localCooltime -= localCooltime * SkillFactory.getSkill(1220051).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(2120051) > 0 && sourceid == 2121003) {
            localCooltime -= localCooltime * SkillFactory.getSkill(2120051).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(14120046) > 0 && sourceid == 14121003) {
            localCooltime -= localCooltime * SkillFactory.getSkill(14120046).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(22170087) > 0 && (sourceid == 22141012 || sourceid == 22140022)) {
            localCooltime -= localCooltime * SkillFactory.getSkill(22170087).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(22170090) > 0 && sourceid == 22171063) {
            localCooltime -= localCooltime * SkillFactory.getSkill(22170090).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(22170084) > 0 && sourceid == 22110023) {
            localCooltime -= localCooltime * SkillFactory.getSkill(22170084).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(24120044) > 0 && sourceid == 24121005) {
            localCooltime -= localCooltime * SkillFactory.getSkill(24120044).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(32120057) > 0 && sourceid == 32121004) {
            localCooltime -= localCooltime * SkillFactory.getSkill(32120057).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(32120063) > 0 && sourceid == 32121006) {
            localCooltime -= localCooltime * SkillFactory.getSkill(32120063).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(33120048) > 0 && (sourceid == 33111006 || sourceid == 33101215 || sourceid == 33121002)) {
            localCooltime -= localCooltime * SkillFactory.getSkill(33120048).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(35120045) > 0 && sourceid == 35111002) {
            localCooltime -= localCooltime * SkillFactory.getSkill(35120045).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(64120051) > 0 && sourceid == 64121001) {
            localCooltime -= localCooltime * SkillFactory.getSkill(64120051).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(65120048) > 0 && sourceid == 65121002) {
            localCooltime -= localCooltime * SkillFactory.getSkill(65120048).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(142120040) > 0 && sourceid == 142121004) {
            localCooltime -= localCooltime * SkillFactory.getSkill(142120040).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(152120036) > 0 && sourceid == 152121004) {
            localCooltime -= localCooltime * SkillFactory.getSkill(152120036).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(155120038) > 0 && sourceid == 155111306) {
            localCooltime -= localCooltime * SkillFactory.getSkill(155120038).getEffect(1).coolTimeR / 100;
        }

        if (chra.getSkillLevel(151120036) > 0 && sourceid == 151121003) {
            localCooltime -= localCooltime * SkillFactory.getSkill(151120036).getEffect(1).coolTimeR / 100;
        }

        if (localCooltime > 0 && chra.getBuffedValue(SecondaryStat.FixCooltime) != null && sourceid / 10000 != 8000 && sourceid / 10000 <= chra.getJob()) {
            localCooltime = chra.getBuffedValue(SecondaryStat.FixCooltime) * 1000;
        }

        if (chra.getSkillLevel(162120035) > 0 && sourceid == 162111005) {
            localCooltime -= (int) (localCooltime * (SkillFactory.getSkill(162120035).getEffect(1).coolTimeR / 100.0));
        }

        if (localCooltime <= 10000) {
            if (ItemCooltime > 0) {
                localCooltime -= ItemCooltime * 0.5;
            }
        } else {
            minusCooltime = localCooltime - 10000;
            if (ItemCooltime <= minusCooltime) {
                localCooltime -= ItemCooltime;
            } else {
                ItemCooltime -= minusCooltime;
                localCooltime -= minusCooltime;
                if (ItemCooltime > 0) {
                    localCooltime -= ItemCooltime * 0.5;
                }
            }
        }
        if (localCooltime <= 5000) {
            localCooltime = Math.max(5000, localCooltime);
        }

        if (chra.getStat().coolTimeR > 0) {
            localCooltime -= (int) (cooltime * (chra.getStat().coolTimeR / 100.0));
        }

        return Math.max(0, localCooltime); //왜 누가 5로해둠?
    }

    public final int getBerserk() {
        return this.berserk;
    }

    public final boolean isHide() {
        return this.skill && (this.sourceid == 9001004 || this.sourceid == 9101004);
    }

    public final boolean isRecovery() {
        return this.skill && (this.sourceid == 1001 || this.sourceid == 10001001 || this.sourceid == 20001001 || this.sourceid == 20011001 || this.sourceid == 20021001 || this.sourceid == 11001 || this.sourceid == 35121005);
    }

    public final boolean isBerserk() {
        return this.skill && this.sourceid == 1320006;
    }

    public final boolean isMPRecovery() {
        return this.skill && this.sourceid == 5101005;
    }

    public final boolean isMonsterRiding_() {
        return this.skill && (this.sourceid == 1004 || this.sourceid == 10001004 || this.sourceid == 20001004 || this.sourceid == 20011004 || this.sourceid == 30001004 && this.sourceid >= 80001000 && this.sourceid <= 80001033 || this.sourceid == 80001037 || this.sourceid == 80001038 || this.sourceid == 80001039 || this.sourceid == 80001044 || this.sourceid >= 80001082 && this.sourceid <= 80001090 || this.sourceid == 30011159 || this.sourceid == 30011109 || this.sourceid == 1004 || this.sourceid == 10001004 || this.sourceid == 20001004 || this.sourceid == 20011004 || this.sourceid == 30001004 || this.sourceid == 20021004 || this.sourceid == 20031004 || this.sourceid == 30011004 || this.sourceid == 50001004 || this.sourceid == 35120000 || this.sourceid == 33001001 || this.sourceid == 35001002 || this.sourceid == 35111003);
    }

    public final boolean isMonsterRiding() {
        return this.skill && (this.isMonsterRiding_() || GameConstants.checkMountItem(this.sourceid) != 0);
    }

    public final boolean isMagicDoor() {
        return this.skill && (this.sourceid == 2311002 || this.sourceid % 10000 == 8001 || this.sourceid == 400001001);
    }

    public final boolean isMechDoor() {
        return this.skill && this.sourceid == 35101005;
    }

    public final boolean isCharge() {
        switch (this.sourceid) {
            case 1211003:
            case 1211008:
            case 11111007:
            case 12101005:
            case 15101006:
            case 21111005: {
                return this.skill;
            }
        }
        return false;
    }

    public final boolean isPoison() {
        return this.dot > 0 && this.dotTime > 0;
    }

    public final boolean isMist() {
        if (this.skill) {
            switch (this.sourceid) {
                case 1076:
                case 11076:
                case 2100010:
                case 2111003:
                case 2201009:
                case 2311011:
                case 4121015:
                case 4221006:
                case 12111005:
                case 12121005:
                case 21121057:
                case 22161003:
                case 22170093:
                case 24121052:
                case 25111206:
                case 32121006:
                case 35120002:
                case 35121052:
                case 36121007:
                case 37110002:
                case 61121105:
                case 61121116:
                case 100001261:
                case 101120206:
                case 80001455:
                case 151121041:
                case 152121041:
                case 155121006:
                case 162101010:
                case 162111000:
                case 162111003:
                case 162121018:
                case 400001017:
                case 400010010:
                case 400011098:
                case 400011100:
                case 400011135:
                case 400020002:
                case 400020046:
                case 400020051:
                case 400021041:
                case 400021049:
                case 400021050:
                case 400021104:
                case 400030002:
                case 400031012:
                case 400031039:
                case 400031040:
                case 400040008:
                case 400041008:
                case 400041041:
                case 400051025:
                case 400051076: {
                    return true;
                }
            }
        }
        return false;
    }

    private final boolean isDispel() {
        return this.skill && (this.sourceid == 2311001 || this.sourceid == 9001000 || this.sourceid == 9101000);
    }

    private final boolean isHeroWill() {
        switch (this.sourceid) {
            case 1121011:
            case 1221012:
            case 1321010:
            case 2121008:
            case 2221008:
            case 2321009:
            case 3121009:
            case 3221008:
            case 3321024:
            case 4121009:
            case 4221008:
            case 4341008:
            case 5121008:
            case 5221010:
            case 5321008:
            case 21121008:
            case 22171004:
            case 22171069:
            case 23121008:
            case 24121009:
            case 25121211:
            case 27121010:
            case 32121008:
            case 33121008:
            case 35121008:
            case 36121009:
            case 37121007:
            case 61121015:
            case 61121220:
            case 64121005:
            case 65121010:
            case 151121006:
            case 152121010:
            case 155121009:
            case 164121010:
            case 400001009: {
                return this.skill;
            }
        }
        return false;
    }

    public final boolean isCombo() {
        switch (this.sourceid) {
            case 1101013:
            case 11111001: {
                return this.skill;
            }
        }
        return false;
    }

    public final boolean isMorph() {
        return this.morphId > 0;
    }

    public final byte getLevel() {
        return this.level;
    }

    public final SummonMovementType getSummonMovementType() {
        if (!this.skill || SkillFactory.getSkill(this.sourceid) == null) {
            return null;
        }
        if (GameConstants.isAngelicBlessSkill(SkillFactory.getSkill(this.sourceid)) || GameConstants.isAngelicBlessBuffEffectItem(this.sourceid)) {
            return SummonMovementType.FOLLOW;
        }
        switch (this.sourceid) {
            case 1121055:
            case 2311014:
            case 3111002:
            case 3111017:
            case 3120012:
            case 3211002:
            case 3211019:
            case 3220012:
            case 3221014:
            case 4111007:
            case 4211007:
            case 4341006:
            case 5211001:
            case 5211014:
            case 5220002:
            case 5220023:
            case 5220024:
            case 5220025:
            case 5221022:
            case 5221029:
            case 5320011:
            case 5321003:
            case 5321004:
            case 5321052:
            case 12111022:
            case 13111004:
            case 13111024:
            case 13120007:
            case 14121003:
            case 22171081:
            case 33101008:
            case 33111003:
            case 35101012:
            case 35111002:
            case 35111005:
            case 35111008:
            case 35111011:
            case 35120002:
            case 35121003:
            case 35121009:
            case 35121010:
            case 35121011:
            case 36121002:
            case 36121013:
            case 36121014:
            case 61111002:
            case 61111220:
            case 80002888:
            case 80002889:
            case 131001007:
            case 131001022:
            case 131001025:
            case 131001307:
            case 131002022:
            case 131003022:
            case 131003023:
            case 131004022:
            case 131004023:
            case 131005022:
            case 131005023:
            case 131006022:
            case 131006023:
            case 151100002:
            case 151111001:
            case 162101003:
            case 162101006:
            case 162121012:
            case 162121015:
            case 164121006:
            case 164121008:
            case 164121011:
            case 400001019:
            case 400001022:
            case 400001039:
            case 400001059:
            case 400011002:
            case 400011057:
            case 400011065:
            case 400021005:
            case 400021047:
            case 400021063:
            case 400021067:
            case 400021069:
            case 400021071:
            case 400021073:
            case 400021095:
            case 400031047:
            case 400031049:
            case 400031051:
            case 400041033:
            case 400041038:
            case 400041044:
            case 400041050:
            case 400041052:
            case 400051011:
            case 400051017:
            case 400051022: {
                return SummonMovementType.STATIONARY;
            }
            case 2111010:
            case 32111006:
            case 33001007:
            case 33001008:
            case 33001010:
            case 33001011:
            case 33001012:
            case 33001015:
            case 162101012:
            case 400011012:
            case 400011013:
            case 400011014: {
                return SummonMovementType.WALK_STATIONARY;
            }
            case 1301013:
            case 2121005:
            case 2211011:
            case 2221005:
            case 2321003:
            case 3111005:
            case 3211005:
            case 11001004:
            case 12000022:
            case 12001004:
            case 12100026:
            case 12110024:
            case 12111004:
            case 12120007:
            case 13001004:
            case 14001005:
            case 15001004:
            case 25121133:
            case 32001014:
            case 32100010:
            case 32110017:
            case 32120019:
            case 35111001:
            case 35111009:
            case 35111010:
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
            case 80002641:
            case 131003026:
            case 152001003:
            case 152121006:
            case 400001013:
            case 400011001:
            case 400011077:
            case 400011078:
            case 400011090:
            case 400021032:
            case 400021033:
            case 400021092:
            case 400031001:
            case 400051009:
            case 400051046: {
                return SummonMovementType.FOLLOW;
            }
            case 400051038:
            case 400051052:
            case 400051053: {
                return SummonMovementType.WALK_FOLLOWER;
            }
            case 101100100:
            case 101100101:
            case 400011006: {
                return SummonMovementType.ZEROWEAPON;
            }
            case 23111008:
            case 23111009:
            case 23111010: {
                return SummonMovementType.CIRCLE_FOLLOW;
            }
            case 5201012:
            case 5201013:
            case 5201014:
            case 5210015:
            case 5210016:
            case 5210017:
            case 5210018:
            case 5211019:
            case 12120013:
            case 12120014: {
                return SummonMovementType.FLAME_SUMMON;
            }
            case 2311006:
            case 3121006:
            case 3221005:
            case 3311009:
            case 5211002:
            case 14000027:
            case 14100027:
            case 14110029:
            case 14120008:
            case 33111005:
            case 131002015:
            case 152101008:
            case 152121005:
            case 164111007:
            case 400001012: {
                return SummonMovementType.BIRD_FOLLOW;
            }
            case 14111024:
            case 14121055:
            case 14121056:
            case 131001017:
            case 131002017:
            case 131003017:
            case 400011005:
            case 400031007:
            case 400031008:
            case 400031009: {
                return SummonMovementType.ShadowServant;
            }
            case 33001009:
            case 33001013:
            case 33001014: {
                return SummonMovementType.SUMMON_JAGUAR;
            }
            case 152101000:
            case 400011088:
            case 400041028: {
                return SummonMovementType.ShadowServantExtend;
            }
            case 400051068: {
                return SummonMovementType.FLY;
            }
            default: {
                if (this.isAngel()) {
                    return SummonMovementType.FOLLOW;
                }
                return null;
            }
        }
    }

    public final boolean isAngel() {
        return GameConstants.isAngel(this.sourceid);
    }

    public final boolean isSkill() {
        return this.skill;
    }

    public final int getSourceId() {
        return this.sourceid;
    }

    public final boolean isSoaring() {
        return this.isSoaring_Normal() || this.isSoaring_Mount();
    }

    public final boolean isSoaring_Normal() {
        return this.skill && GameConstants.isBeginnerJob(this.sourceid / 10000) && this.sourceid % 10000 == 1026;
    }

    public final boolean isSoaring_Mount() {
        return this.skill && (GameConstants.isBeginnerJob(this.sourceid / 10000) && this.sourceid % 10000 == 1142 || this.sourceid == 80001089);
    }

    public final boolean makeChanceResult() {
        if (this.subprop != 100) {
            return this.subprop >= 100 || Randomizer.nextInt(100) < this.subprop;
        }
        return this.prop >= 100 || Randomizer.nextInt(100) < this.prop;
    }

    public final short getProp() {
        return this.prop;
    }

    public final short getIgnoreMob() {
        return this.ignoreMobpdpR;
    }

    public final int getEnhancedHP() {
        return this.emhp;
    }

    public final int getEnhancedMP() {
        return this.emmp;
    }

    public final int getEnhancedWatk() {
        return this.epad;
    }

    public final int getEnhancedWdef() {
        return this.epdd;
    }

    public final int getEnhancedMdef() {
        return this.emdd;
    }

    public final short getDOT() {
        return this.dot;
    }

    public final short getDOTTime() {
        return this.dotTime;
    }

    public final short getCriticalDamage() {
        return this.criticaldamage;
    }

    public final short getASRRate() {
        return this.asrR;
    }

    public final short getTERRate() {
        return this.terR;
    }

    public final int getDAMRate() {
        return this.damR;
    }

    public final short getMesoRate() {
        return this.mesoR;
    }

    public final int getEXP() {
        return this.exp;
    }

    public final short getAttackX() {
        return this.padX;
    }

    public final short getMagicX() {
        return this.madX;
    }

    public final int getPercentHP() {
        return this.mhpR;
    }

    public final int getPercentMP() {
        return this.mmpR;
    }

    public final int getConsume() {
        return this.consumeOnPickup;
    }

    public final int getSelfDestruction() {
        return this.selfDestruction;
    }

    public final int getCharColor() {
        return this.charColor;
    }

    public final int getSpeedMax() {
        return this.speedMax;
    }

    public final int getAccX() {
        return this.accX;
    }

    public final int getMaxHpX() {
        return this.getMhpX();
    }

    public final int getMaxMpX() {
        return this.mmpX;
    }

    public short getIndieDamR() {
        return this.indieDamR;
    }

    public final List<Integer> getPetsCanConsume() {
        return this.petsCanConsume;
    }

    public final boolean isReturnScroll() {
        return this.skill && (this.sourceid == 80001040 || this.sourceid == 20021110);
    }

    public final boolean isMechChange() {
        switch (this.sourceid) {
            default:
        }
        return false;
    }

    public final int getRange() {
        return this.range;
    }

    public final short getER() {
        return this.er;
    }

    public final int getPrice() {
        return this.price;
    }

    public final int getExtendPrice() {
        return this.extendPrice;
    }

    public final byte getPeriod() {
        return this.period;
    }

    public final byte getReqGuildLevel() {
        return this.reqGuildLevel;
    }

    public final byte getEXPRate() {
        return this.expR;
    }

    public final short getLifeID() {
        return this.lifeId;
    }

    public final short getUseLevel() {
        return this.useLevel;
    }

    public final byte getSlotCount() {
        return this.slotCount;
    }

    public final short getStr() {
        return this.str;
    }

    public final short getStrX() {
        return this.strX;
    }

    public final short getDex() {
        return this.dex;
    }

    public final short getDexX() {
        return this.dexX;
    }

    public final short getInt() {
        return this.int_;
    }

    public final short getIntX() {
        return this.intX;
    }

    public final short getLuk() {
        return this.luk;
    }

    public final short getLukX() {
        return this.lukX;
    }

    public final short getComboConAran() {
        return this.comboConAran;
    }

    public final short getMPCon() {
        return this.mpCon;
    }

    public final short getMPConReduce() {
        return this.mpConReduce;
    }

    public final int getSoulMPCon() {
        return this.soulmpCon;
    }

    public final short getIndieMHp() {
        return this.indieMhp;
    }

    public final short getIndieMMp() {
        return this.indieMmp;
    }

    public final short getIndieAllStat() {
        return this.indieAllStat;
    }

    public final byte getType() {
        return this.type;
    }

    public int getBossDamage() {
        return this.getBdR();
    }

    public int getInterval() {
        return this.interval;
    }

    public ArrayList<Pair<Integer, Integer>> getAvailableMaps() {
        return this.availableMap;
    }

    public short getWDEFRate() {
        return this.pddR;
    }

    public short getMDEFRate() {
        return this.mddR;
    }

    public short getOnActive() {
        return this.onActive;
    }

    public boolean isDoubleDice() {
        switch (this.sourceid) {
            case 5120012:
            case 5220014:
            case 5320007:
            case 35120014: {
                return true;
            }
        }
        return false;
    }

    public int getPPCon() {
        return this.ppcon;
    }

    public int getPPRecovery() {
        return this.ppRecovery;
    }

    public int getEqskill1() {
        return this.eqskill1;
    }

    public void setEqskill1(int eqskill1) {
        this.eqskill1 = eqskill1;
    }

    public int getEqskill2() {
        return this.eqskill2;
    }

    public void setEqskill2(int eqskill2) {
        this.eqskill2 = eqskill2;
    }

    public int getEqskill3() {
        return this.eqskill3;
    }

    public void setEqskill3(int eqskill3) {
        this.eqskill3 = eqskill3;
    }

    public void setMaxHpX(int hp2) {
        this.setMhpX(hp2);
    }

    public short getPdd() {
        return this.pdd;
    }

    public void setPdd(short pdd) {
        this.pdd = pdd;
    }

    public short getIndiePmdR() {
        return this.indiePmdR;
    }

    public void setIndiePmdR(short IndiePmdR) {
        this.indiePmdR = IndiePmdR;
    }

    public int getQ() {
        return this.q;
    }

    public int getQ2() {
        return this.q2;
    }

    public int getV2() {
        return this.v2;
    }

    public int getMhpX() {
        return this.mhpX;
    }

    public void setMhpX(int mhpX) {
        this.mhpX = mhpX;
    }

    public short getLv2mhp() {
        return this.lv2mhp;
    }

    public void setLv2mhp(short lv2mhp) {
        this.lv2mhp = lv2mhp;
    }

    public Point getLt() {
        return this.lt;
    }

    public void setLt(Point lt) {
        this.lt = lt;
    }

    public long getStarttime() {
        return this.starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public short getBufftimeR() {
        return this.bufftimeR;
    }

    public void setBufftimeR(short bufftimeR) {
        this.bufftimeR = bufftimeR;
    }

    public short getLv2mmp() {
        return this.lv2mmp;
    }

    public void setLv2mmp(short lv2mmp) {
        this.lv2mmp = lv2mmp;
    }

    public short getIndieMhpR() {
        return this.indieMhpR;
    }

    public void setIndieMhpR(short indieMhpR) {
        this.indieMhpR = indieMhpR;
    }

    public int getS2() {
        return this.s2;
    }

    public void setS2(int s2) {
        this.s2 = s2;
    }

    public int getPPReq() {
        return this.ppReq;
    }

    public void setPPReq(int ppReq) {
        this.ppReq = ppReq;
    }

    public short getDotInterval() {
        return this.dotInterval;
    }

    public void setDotInterval(short dotInterval) {
        this.dotInterval = dotInterval;
    }

    public short getDotSuperpos() {
        return this.dotSuperpos;
    }

    public void setDotSuperpos(short dotSuperpos) {
        this.dotSuperpos = dotSuperpos;
    }

    public int getU2() {
        return this.u2;
    }

    public void setU2(int u2) {
        this.u2 = u2;
    }

    public short getIgnoreMobDamR() {
        return this.ignoreMobDamR;
    }

    public void setIgnoreMobDamR(short ignoreMobDamR) {
        this.ignoreMobDamR = ignoreMobDamR;
    }

    public short getKillRecoveryR() {
        return this.killRecoveryR;
    }

    public void setKillRecoveryR(short killRecoveryR) {
        this.killRecoveryR = killRecoveryR;
    }

    public int getW2() {
        return this.w2;
    }

    public void setW2(int w2) {
        this.w2 = w2;
    }

    public short getMaxDemonForce() {
        return this.mdf;
    }

    public short getForceCon() {
        return this.forceCon;
    }

    public void setForceCon(short forceCon) {
        this.forceCon = forceCon;
    }

    public short getBdR() {
        return this.bdR;
    }

    public void setBdR(short bdR) {
        this.bdR = bdR;
    }

    public double getExpRPerM() {
        return this.expRPerM;
    }

    public void setExpRPerM(double expRPerM) {
        this.expRPerM = expRPerM;
    }

    public short getHpFX() {
        return this.hpFX;
    }

    public void setHpFX(short hpFX) {
        this.hpFX = hpFX;
    }

    public short getSummonTimeR() {
        return this.summonTimeR;
    }

    public void setSummonTimeR(short summonTimeR) {
        this.summonTimeR = summonTimeR;
    }

    public short getTargetPlus() {
        return this.targetPlus;
    }

    public void setTargetPlus(short targetPlus) {
        this.targetPlus = targetPlus;
    }

    public short getTargetPlus_5th() {
        return this.targetPlus_5th;
    }

    public void setTargetPlus_5th(short targetPlus_5th) {
        this.targetPlus_5th = targetPlus_5th;
    }

    public short getIndieExp() {
        return this.IndieExp;
    }

    public short getSubprop() {
        return this.subprop;
    }

    public int getCoolRealTime() {
        return this.cooltime;
    }

    public final short getMPRCon() {
        return this.mpRCon;
    }

    public short getPsdSpeed() {
        return this.psdSpeed;
    }

    public void setPsdSpeed(short psdSpeed) {
        this.psdSpeed = psdSpeed;
    }

    public short getPsdJump() {
        return this.psdJump;
    }

    public void setPsdJump(short psdJump) {
        this.psdJump = psdJump;
    }

    public int getStanceProp() {
        return this.stanceProp;
    }

    public void setStanceProp(int stanceProp) {
        this.stanceProp = stanceProp;
    }

    public int getDamAbsorbShieldR() {
        return this.damAbsorbShieldR;
    }

    public void setDamAbsorbShieldR(int damAbsorbShieldR) {
        this.damAbsorbShieldR = damAbsorbShieldR;
    }

    public short getPdR() {
        return this.pdR;
    }

    public void setPdR(short pdR) {
        this.pdR = pdR;
    }

    public short getStrFX() {
        return this.strFX;
    }

    public void setStrFX(short strFX) {
        this.strFX = strFX;
    }

    public short getDexFX() {
        return this.dexFX;
    }

    public void setDexFX(short dexFX) {
        this.dexFX = dexFX;
    }

    public short getIntFX() {
        return this.intFX;
    }

    public void setIntFX(short intFX) {
        this.intFX = intFX;
    }

    public short getLukFX() {
        return this.lukFX;
    }

    public void setLukFX(short lukFX) {
        this.lukFX = lukFX;
    }

    public int getDamPlus() {
        return this.damPlus;
    }

    public short getPddX() {
        return this.pddX;
    }

    public short getMddX() {
        return this.mddX;
    }

    public short getArcX() {
        return this.arcX;
    }

    public short getNbdR() {
        return this.nbdR;
    }

    public short getStarX() {
        return this.starX;
    }

    public short getMdR() {
        return this.mdR;
    }

    public short getPadR() {
        return this.padR;
    }

    public short getMadR() {
        return this.madR;
    }

    public short getStrR() {
        return this.strR;
    }

    public short getDexR() {
        return this.dexR;
    }

    public short getIntR() {
        return this.intR;
    }

    public short getLukR() {
        return this.lukR;
    }

    public short getDropR() {
        return this.dropR;
    }

    public void setDropR(short dropR) {
        this.dropR = dropR;
    }

    public Point getRb() {
        return this.rb;
    }

    public void setRb(Point rb2) {
        this.rb = rb2;
    }

    public short getLv2pad() {
        return this.lv2pad;
    }

    public void setLv2pad(short lv2pad) {
        this.lv2pad = lv2pad;
    }

    public short getLv2mad() {
        return this.lv2mad;
    }

    public void setLv2mad(short lv2mad) {
        this.lv2mad = lv2mad;
    }

    public short getPddR() {
        return this.pddR;
    }

    public int getNocoolProps() {
        return this.nocoolProps;
    }

    public Point getLt2() {
        return this.lt2;
    }

    public void setLt2(Point lt2) {
        this.lt2 = lt2;
    }

    public Point getLt3() {
        return this.lt3;
    }

    public void setLt3(Point lt3) {
        this.lt3 = lt3;
    }

    public Point getRb2() {
        return this.rb2;
    }

    public void setRb2(Point rb2) {
        this.rb2 = rb2;
    }

    public Point getRb3() {
        return this.rb3;
    }

    public void setRb3(Point rb3) {
        this.rb3 = rb3;
    }

    public static class CancelDiseaseAction
            implements Runnable {

        private final WeakReference<MapleCharacter> target;
        private final Map<SecondaryStat, Pair<Integer, Integer>> statup;

        public CancelDiseaseAction(MapleCharacter target, Map<SecondaryStat, Pair<Integer, Integer>> statup) {
            this.target = new WeakReference<MapleCharacter>(target);
            this.statup = statup;
        }

        @Override
        public void run() {
            MapleCharacter realTarget = (MapleCharacter) this.target.get();
            if (realTarget != null) {
                realTarget.cancelDisease(this.statup, true);
            }
        }
    }
}
