// 
// Decompiled by Procyon v0.5.36
// 

package client;

import java.awt.Point;
import constants.GameConstants;
import server.Randomizer;
import java.util.Iterator;
import tools.StringUtil;
import provider.MapleDataTool;
import provider.MapleData;
import java.util.ArrayList;
import server.life.Element;
import tools.Pair;
import server.SecondaryStatEffect;
import java.util.List;

public class Skill
{
    private String name;
    private String psdDamR;
    private String desc;
    private final List<SecondaryStatEffect> effects;
    private List<SecondaryStatEffect> pvpEffects;
    private List<Integer> animation;
    private List<Integer> psdSkills;
    private final List<Pair<String, Integer>> requiredSkill;
    private Element element;
    private int id;
    private int animationTime;
    private int type;
    private int masterLevel;
    private int maxLevel;
    private int delay;
    private int trueMax;
    private int eventTamingMob;
    private int skillType;
    private int psd;
    private int weaponIdx;
    private int finalAttackId;
    private int vehicleID;
    private int categoryIndex;
    private boolean invisible;
    private boolean chargeskill;
    private boolean timeLimited;
    private boolean combatOrders;
    private boolean pvpDisabled;
    private boolean magic;
    private boolean casterMove;
    private boolean pushTarget;
    private boolean pullTarget;
    private boolean hyper;
    private boolean chainAttack;
    private boolean finalAttack;
    private boolean notCooltimeReset;
    private boolean vSkill;
    private boolean notIncBuffDuration;
    private boolean encode4Byte;
    private boolean ignoreCounter;
    private List<Integer> skillList;
    private List<Integer> skillList2;
    private List<SecondAtom2> secondAtoms;
    private List<RandomSkillEntry> randomSkills;
    
    public Skill(final int id) {
        this.name = "";
        this.psdDamR = "";
        this.desc = "";
        this.effects = new ArrayList<SecondaryStatEffect>();
        this.pvpEffects = null;
        this.animation = null;
        this.psdSkills = new ArrayList<Integer>();
        this.requiredSkill = new ArrayList<Pair<String, Integer>>();
        this.element = Element.NEUTRAL;
        this.animationTime = 0;
        this.type = 0;
        this.masterLevel = 0;
        this.maxLevel = 0;
        this.delay = 0;
        this.trueMax = 0;
        this.eventTamingMob = 0;
        this.skillType = 0;
        this.psd = 0;
        this.weaponIdx = 0;
        this.finalAttackId = 0;
        this.vehicleID = 0;
        this.categoryIndex = -1;
        this.invisible = false;
        this.chargeskill = false;
        this.timeLimited = false;
        this.combatOrders = false;
        this.pvpDisabled = false;
        this.magic = false;
        this.casterMove = false;
        this.pushTarget = false;
        this.pullTarget = false;
        this.hyper = false;
        this.chainAttack = false;
        this.finalAttack = false;
        this.notCooltimeReset = false;
        this.vSkill = false;
        this.notIncBuffDuration = false;
        this.encode4Byte = false;
        this.ignoreCounter = false;
        this.skillList = new ArrayList<Integer>();
        this.skillList2 = new ArrayList<Integer>();
        this.secondAtoms = new ArrayList<SecondAtom2>();
        this.randomSkills = new ArrayList<RandomSkillEntry>();
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static final Skill loadFromData(final int id, final MapleData data, final MapleData delayData) {
        final Skill ret = new Skill(id);
        boolean isBuff = false;
        final int skillType = MapleDataTool.getInt("skillType", data, -1);
        final String elem = MapleDataTool.getString("elemAttr", data, null);
        if (elem != null) {
            ret.element = Element.getFromChar(elem.charAt(0));
        }
        ret.skillType = skillType;
        ret.invisible = (MapleDataTool.getInt("invisible", data, 0) > 0);
        ret.timeLimited = (MapleDataTool.getInt("timeLimited", data, 0) > 0);
        ret.combatOrders = (MapleDataTool.getInt("combatOrders", data, 0) > 0);
        ret.masterLevel = MapleDataTool.getInt("masterLevel", data, 0);
        ret.vehicleID = MapleDataTool.getInt("vehicleID", data, 0);
        ret.hyper = (data.getChildByPath("hyper") != null);
        ret.vSkill = (data.getChildByPath("vSkill") != null);
        ret.categoryIndex = MapleDataTool.getInt("categoryIndex", data, 0);
        final MapleData additional_process = data.getChildByPath("additional_process");
        if (additional_process != null) {
            for (final MapleData dat : additional_process.getChildren()) {
                final int process = MapleDataTool.getInt(dat.getName(), additional_process, 0);
                if (process == 11) {
                    ret.encode4Byte = (MapleDataTool.getInt("processtype", data, 0) != 0);
                    break;
                }
            }
        }
        ret.psd = MapleDataTool.getInt("psd", data, 0);
        final MapleData psdskill = data.getChildByPath("psdSkill");
        if (psdskill != null) {
            for (final MapleData d : data.getChildByPath("psdSkill").getChildren()) {
                ret.psdSkills.add(Integer.parseInt(d.getName()));
            }
            for (final int pskill : ret.psdSkills) {
                final Skill skil = SkillFactory.getSkill(pskill);
                if (skil != null) {
                    skil.getPsdSkills().add(id);
                }
            }
        }
        if (id == 22140000 || id == 22141002) {
            ret.masterLevel = 5;
        }
        ret.notCooltimeReset = (data.getChildByPath("notCooltimeReset") != null);
        ret.notIncBuffDuration = (data.getChildByPath("notIncBuffDuration") != null);
        ret.eventTamingMob = MapleDataTool.getInt("eventTamingMob", data, 0);
        final MapleData inf = data.getChildByPath("info");
        if (inf != null) {
            ret.type = MapleDataTool.getInt("type", inf, 0);
            ret.pvpDisabled = (MapleDataTool.getInt("pvp", inf, 1) <= 0);
            ret.magic = (MapleDataTool.getInt("magicDamage", inf, 0) > 0);
            ret.casterMove = (MapleDataTool.getInt("casterMove", inf, 0) > 0);
            ret.pushTarget = (MapleDataTool.getInt("pushTarget", inf, 0) > 0);
            ret.pullTarget = (MapleDataTool.getInt("pullTarget", inf, 0) > 0);
            ret.chainAttack = (MapleDataTool.getInt("chainAttack", inf, 0) > 0);
            ret.finalAttack = (MapleDataTool.getInt("finalAttack", inf, 0) > 0);
        }
        final MapleData info2 = data.getChildByPath("info2");
        if (inf != null) {
            ret.ignoreCounter = (MapleDataTool.getInt("ignoreCounter", info2, 0) > 0);
        }
        final MapleData SecondAtom = data.getChildByPath("SecondAtom");
        if (SecondAtom != null) {
            final MapleData atom = SecondAtom.getChildByPath("atom");
            if (atom != null) {
                for (final MapleData d2 : atom.getChildren()) {
                    ret.secondAtoms.add(parseSecondAtom(d2, id));
                }
            }
            else {
                ret.secondAtoms.add(parseSecondAtom(SecondAtom, id));
            }
        }
        final MapleData listinfo2 = data.getChildByPath("skillList");
        if (listinfo2 != null) {
            for (final MapleData list : listinfo2.getChildren()) {
                ret.skillList.add(MapleDataTool.getInt(list.getName(), listinfo2, -1));
            }
        }
        final MapleData listinfo3 = data.getChildByPath("skillList2");
        if (listinfo3 != null) {
            for (final MapleData list2 : listinfo3.getChildren()) {
                ret.skillList2.add(MapleDataTool.getInt(list2.getName(), listinfo3, -1));
            }
        }
        ret.weaponIdx = MapleDataTool.getInt("weapon", data, 0);
        final MapleData f_Data = data.getChildByPath("finalAttack");
        if (f_Data != null) {
            for (final MapleData f_skillc : f_Data.getChildren()) {
                final int skillId_f = Integer.parseInt(f_skillc.getName());
                if (skillId_f > 0) {
                    ret.finalAttackId = skillId_f;
                    break;
                }
            }
        }
        final MapleData effect = data.getChildByPath("effect");
        if (skillType == 1) {
            isBuff = false;
        }
        else if (skillType == 2) {
            isBuff = true;
        }
        else if (skillType == 3) {
            (ret.animation = new ArrayList<Integer>()).add(0);
            isBuff = (effect != null);
        }
        else {
            MapleData action_ = data.getChildByPath("action");
            final MapleData hit = data.getChildByPath("hit");
            final MapleData ball = data.getChildByPath("ball");
            boolean action = false;
            if (action_ == null && data.getChildByPath("prepare/action") != null) {
                action_ = data.getChildByPath("prepare/action");
                action = true;
            }
            isBuff = (effect != null && hit == null && ball == null);
            if (action_ != null) {
                String d3 = null;
                if (action) {
                    d3 = MapleDataTool.getString(action_, null);
                }
                else {
                    d3 = MapleDataTool.getString("0", action_, null);
                }
                if (d3 != null) {
                    isBuff |= d3.equals("alert2");
                    final MapleData dd = delayData.getChildByPath(d3);
                    if (dd != null) {
                        for (final MapleData del : dd) {
                            int a = Math.abs(MapleDataTool.getInt("delay", del, 0));
                            if (a < 0) {
                                a *= -1;
                            }
                            final Skill skill = ret;
                            skill.delay += a;
                        }
                    }
                    if (SkillFactory.getDelay(d3) != null) {
                        (ret.animation = new ArrayList<Integer>()).add(SkillFactory.getDelay(d3));
                        if (!action) {
                            for (final MapleData ddc : action_) {
                                if (!MapleDataTool.getString(ddc, d3).equals(d3)) {
                                    final String c = MapleDataTool.getString(ddc);
                                    if (SkillFactory.getDelay(c) == null) {
                                        continue;
                                    }
                                    ret.animation.add(SkillFactory.getDelay(c));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (StringUtil.getLeftPaddedStr(String.valueOf(id / 10000), '0', 3).equals("8000")) {
            isBuff = true;
        }
        ret.chargeskill = (data.getChildByPath("keydown") != null);
        final MapleData level = data.getChildByPath("common");
        if (level != null) {
            ret.maxLevel = MapleDataTool.getInt("maxLevel", level, 1);
            ret.psdDamR = MapleDataTool.getString("damR", level, "");
            ret.trueMax = ret.maxLevel + (ret.combatOrders ? 2 : 0);
            for (int i = 1; i <= ret.trueMax; ++i) {
                ret.getEffects().add(SecondaryStatEffect.loadSkillEffectFromData(level, id, isBuff, i, "x"));
            }
        }
        else {
            for (final MapleData leve : data.getChildByPath("level")) {
                ret.getEffects().add(SecondaryStatEffect.loadSkillEffectFromData(leve, id, isBuff, Byte.parseByte(leve.getName()), null));
            }
            ret.maxLevel = ret.getEffects().size();
            ret.trueMax = ret.getEffects().size();
        }
        final MapleData level2 = data.getChildByPath("PVPcommon");
        if (level2 != null) {
            ret.pvpEffects = new ArrayList<SecondaryStatEffect>();
            for (int j = 1; j <= ret.trueMax; ++j) {
                ret.pvpEffects.add(SecondaryStatEffect.loadSkillEffectFromData(level2, id, isBuff, j, "x"));
            }
        }
        final MapleData reqDataRoot = data.getChildByPath("req");
        if (reqDataRoot != null) {
            for (final MapleData reqData : reqDataRoot.getChildren()) {
                ret.requiredSkill.add(new Pair<String, Integer>(reqData.getName(), MapleDataTool.getInt(reqData, 1)));
            }
        }
        ret.animationTime = 0;
        if (effect != null) {
            for (final MapleData effectEntry : effect) {
                final Skill skill2 = ret;
                skill2.animationTime += MapleDataTool.getIntConvert("delay", effectEntry, 0);
            }
        }
        final MapleData randomSkill = data.getChildByPath("randomSkill");
        if (randomSkill != null) {
            for (final MapleData rs : randomSkill.getChildren()) {
                final MapleData info3 = randomSkill.getChildByPath(rs.getName());
                if (info3 != null) {
                    final List<Pair<Integer, Integer>> skilllist = new ArrayList<Pair<Integer, Integer>>();
                    final int skillid = MapleDataTool.getInt("skillID", info3, -1);
                    final int prob = MapleDataTool.getInt("prob", info3, -1);
                    final MapleData listinfo4 = info3.getChildByPath("skillList");
                    if (listinfo4 != null) {
                        for (final MapleData list3 : listinfo4.getChildren()) {
                            skilllist.add(new Pair<Integer, Integer>(Integer.parseInt(list3.getName()), MapleDataTool.getInt(list3.getName(), listinfo4, -1)));
                        }
                    }
                    ret.randomSkills.add(new RandomSkillEntry(skillid, prob, skilllist));
                }
            }
        }
        return ret;
    }

    // 제로꺼 가져옴
    public SecondaryStatEffect getEffect(final int level) {
        if (getEffects().size() < level) {
            if (getEffects().size() > 0) { //incAllskill
                return getEffects().get(getEffects().size() - 1);
            }
            return null;

        } else if (level <= 0) {
            return getEffects().get(0);
        }
        return getEffects().get(level - 1);
    }

    /*public SecondaryStatEffect getEffect(final int level) {
        if (this.getEffects().size() < level) {
            if (this.getEffects().size() > 0) {
                return this.getEffects().get(this.getEffects().size() - 1);
            }
            return null;
        }
        else {
            if (level <= 0) {
                return this.getEffects().get(0);
            }
            return this.getEffects().get(level - 1);
        }
    }*/
    
    public SecondaryStatEffect getPVPEffect(final int level) {
        if (this.pvpEffects == null) {
            return this.getEffect(level);
        }
        if (this.pvpEffects.size() < level) {
            if (this.pvpEffects.size() > 0) {
                return this.pvpEffects.get(this.pvpEffects.size() - 1);
            }
            return null;
        }
        else {
            if (level <= 0) {
                return this.pvpEffects.get(0);
            }
            return this.pvpEffects.get(level - 1);
        }
    }
    
    public int getSkillType() {
        return this.skillType;
    }
    
    public List<Integer> getAllAnimation() {
        return this.animation;
    }
    
    public int getAnimation() {
        if (this.animation == null) {
            return -1;
        }
        return this.animation.get(Randomizer.nextInt(this.animation.size()));
    }
    
    public boolean isPVPDisabled() {
        return this.pvpDisabled;
    }
    
    public boolean isChargeSkill() {
        return this.chargeskill;
    }
    
    public boolean isInvisible() {
        return this.invisible;
    }
    
    public boolean isEncode4Byte() {
        return this.encode4Byte;
    }
    
    public boolean hasRequiredSkill() {
        return this.requiredSkill.size() > 0;
    }
    
    public List<Pair<String, Integer>> getRequiredSkills() {
        return this.requiredSkill;
    }
    
    public int getMaxLevel() {
        return this.maxLevel;
    }
    
    public int getTrueMax() {
        return this.trueMax;
    }
    
    public boolean combatOrders() {
        return this.combatOrders;
    }
    
    public boolean canBeLearnedBy(final MapleCharacter chr) {
        final int jid;
        final short job = (short)(jid = chr.getJob());
        final int skillForJob = this.id / 10000;
        if (skillForJob == 2001) {
            return GameConstants.isEvan(job);
        }
        if (chr.getSubcategory() == 1) {
            return GameConstants.isDualBlade(job);
        }
        if (chr.getSubcategory() == 2) {
            return GameConstants.isCannon(job);
        }
        if (skillForJob == 0) {
            return GameConstants.isAdventurer(job);
        }
        if (skillForJob == 1000) {
            return GameConstants.isKOC(job);
        }
        if (skillForJob == 2000) {
            return GameConstants.isAran(job);
        }
        if (skillForJob == 3000) {
            return GameConstants.isResist(job);
        }
        if (skillForJob == 3001) {
            return GameConstants.isDemonSlayer(job);
        }
        if (skillForJob == 2002) {
            return GameConstants.isMercedes(job);
        }
        return jid / 100 == skillForJob / 100 && jid / 1000 == skillForJob / 1000 && (!GameConstants.isCannon(skillForJob) || GameConstants.isCannon(job)) && (!GameConstants.isDemonSlayer(skillForJob) || GameConstants.isDemonSlayer(job)) && (!GameConstants.isAdventurer(skillForJob) || GameConstants.isAdventurer(job)) && (!GameConstants.isKOC(skillForJob) || GameConstants.isKOC(job)) && (!GameConstants.isAran(skillForJob) || GameConstants.isAran(job)) && (!GameConstants.isEvan(skillForJob) || GameConstants.isEvan(job)) && (!GameConstants.isMercedes(skillForJob) || GameConstants.isMercedes(job)) && (!GameConstants.isResist(skillForJob) || GameConstants.isResist(job)) && (jid / 10 % 10 != 0 || skillForJob / 10 % 10 <= jid / 10 % 10) && (skillForJob / 10 % 10 == 0 || skillForJob / 10 % 10 == jid / 10 % 10) && skillForJob % 10 <= jid % 10;
    }
    
    public boolean isTimeLimited() {
        return this.timeLimited;
    }
    
    public boolean sub_4FD900(final int a1) {
        if (a1 <= 5320007) {
            if (a1 == 5320007) {
                return true;
            }
            if (a1 > 4210012) {
                if (a1 > 5220012) {
                    if (a1 == 5220014) {
                        return true;
                    }
                    final boolean v1 = a1 == 5221022;
                }
                else {
                    if (a1 == 5220012) {
                        return true;
                    }
                    if (a1 > 4340012) {
                        return a1 >= 5120011 && a1 <= 5120012;
                    }
                    if (a1 == 4340012) {
                        return true;
                    }
                    final boolean v1 = a1 == 4340010;
                }
            }
            else {
                if (a1 == 4210012) {
                    return true;
                }
                if (a1 > 2221009) {
                    if (a1 == 2321010 || a1 == 3210015) {
                        return true;
                    }
                    final boolean v1 = a1 == 4110012;
                }
                else {
                    if (a1 == 2221009 || a1 == 1120012 || a1 == 1320011) {
                        return true;
                    }
                    final boolean b = a1 == 2121009;
                }
            }
        }
        if (a1 > 23120011) {
            boolean v1;
            if (a1 > 35120014) {
                if (a1 == 51120000) {
                    return true;
                }
                v1 = (a1 == 80001913);
            }
            else {
                if (a1 == 35120014 || a1 == 23120013 || a1 == 23121008) {
                    return true;
                }
                v1 = (a1 == 33120010);
            }
            return v1;
        }
        if (a1 == 23120011) {
            return true;
        }
        if (a1 <= 21120014) {
            if (a1 == 21120014 || a1 == 5321004 || a1 - 5321003 + 1 == 2) {
                return true;
            }
            final boolean v1 = a1 - 5321003 + 1 - 2 == 15799005;
            return v1;
        }
        else {
            if (a1 > 21121008) {
                final boolean v1 = a1 == 22171069;
                return v1;
            }
            return a1 == 21121008 || (a1 >= 21120020 && a1 <= 21120021);
        }
    }
    
    public boolean sub_4FDA20(final int a1) {
        boolean result = false;
        if (a1 - 92000000 >= 1000000 || a1 % 10000 != 0) {
            final int v1 = 10000 * (a1 / 10000);
            if (v1 - 92000000 < 1000000 && v1 % 10000 == 0) {
                result = true;
            }
        }
        return result;
    }
    
    public boolean sub_4FD870(final int a1) {
        int v1 = a1 / 10000;
        if (a1 / 10000 == 8000) {
            v1 = a1 / 100;
        }
        return v1 - 800000 <= 99;
    }
    
    public boolean sub_48AEF0(final int a1) {
        int v1 = a1 / 10000;
        if (a1 / 10000 == 8000) {
            v1 = a1 / 100;
        }
        final boolean result = v1 - 40000 > 5 && this.sub_48A360(v1);
        return result;
    }
    
    public boolean sub_48A360(final int a1) {
        boolean v2;
        if (a1 > 6001) {
            if (a1 == 13000) {
                return true;
            }
            v2 = (a1 == 14000);
        }
        else {
            if (a1 >= 6000) {
                return true;
            }
            if (a1 <= 3002) {
                if (a1 >= 3001 || (a1 >= 2001 && a1 <= 2005)) {
                    return true;
                }
                if (a1 - 40000 <= 5) {
                    return false;
                }
                if (a1 % 1000 == 0) {
                    return true;
                }
            }
            v2 = (a1 == 5000);
        }
        return v2 || a1 - 800000 < 100;
    }
    
    public boolean sub_4FD8B0(final int a1) {
        boolean result;
        if (a1 >= 0) {
            int v1 = a1 / 10000;
            if (a1 / 10000 == 8000) {
                v1 = a1 / 100;
            }
            result = (v1 == 9500);
        }
        else {
            result = false;
        }
        return result;
    }
    
    public int sub_48A160(final int a1) {
        int result = a1 / 10000;
        if (a1 / 10000 == 8000) {
            result = a1 / 100;
        }
        return result;
    }
    
    public int sub_489A10(final int a1) {
        int result = 0;
        if (this.sub_48A360(a1) || a1 % 100 == 0 || a1 == 501 || a1 == 3101) {
            result = 1;
        }
        else if (a1 - 2200 < 100 || a1 == 2001) {
            switch (a1) {
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
        }
        else if (a1 / 10 == 43) {
            result = 0;
            if ((a1 - 430) / 2 <= 2) {
                result = (a1 - 430) / 2 + 2;
            }
        }
        else {
            result = 0;
            if (a1 % 10 <= 2) {
                result = a1 % 10 + 2;
            }
        }
        return result;
    }
    
    public boolean sub_4FD7F0(final int a1) {
        boolean v1;
        if (a1 > 101100101) {
            if (a1 > 101110203) {
                if (a1 == 101120104) {
                    return true;
                }
                v1 = (a1 - 101120104 == 100);
            }
            else {
                if (a1 == 101110203 || a1 == 101100201 || a1 == 101110102) {
                    return true;
                }
                v1 = (a1 - 101110102 == 98);
            }
        }
        else {
            if (a1 == 101100101) {
                return true;
            }
            if (a1 > 4331002) {
                if (a1 == 4340007 || a1 == 4341004) {
                    return true;
                }
                v1 = (a1 == 101000101);
            }
            else {
                if (a1 == 4331002 || a1 == 4311003 || a1 == 4321006) {
                    return true;
                }
                v1 = (a1 == 4330009);
            }
        }
        return v1;
    }
    
    public boolean isFourthJob() {
        final int a1 = this.id;
        boolean result;
        if (this.sub_4FD900(a1) || (a1 - 92000000 < 1000000 && a1 % 10000 == 0) || this.sub_4FDA20(a1) || this.sub_4FD870(a1) || this.sub_48AEF0(a1) || this.sub_4FD8B0(a1)) {
            result = false;
        }
        else {
            final int v2 = this.sub_48A160(a1);
            final int v3 = this.sub_489A10(v2);
            result = (v2 - 40000 > 5 && (this.sub_4FD7F0(a1) || (v3 == 4 && !GameConstants.isZero(v2))));
        }
        return result;
    }
    
    public Element getElement() {
        return this.element;
    }
    
    public int getAnimationTime() {
        return this.animationTime;
    }
    
    public int getMasterLevel() {
        return this.masterLevel;
    }
    
    public int getDelay() {
        return this.delay;
    }
    
    public int getTamingMob() {
        return this.eventTamingMob;
    }
    
    public boolean isBeginnerSkill() {
        final int jobId = this.id / 10000;
        return GameConstants.isBeginnerJob(jobId);
    }
    
    public boolean isMagic() {
        return this.magic;
    }
    
    public boolean isMovement() {
        return this.casterMove;
    }
    
    public boolean isPush() {
        return this.pushTarget;
    }
    
    public boolean isPull() {
        return this.pullTarget;
    }
    
    public int getPsd() {
        return this.psd;
    }
    
    public String getPsdDamR() {
        return this.psdDamR;
    }
    
    public boolean isHyper() {
        return this.hyper;
    }
    
    public boolean isVMatrix() {
        return this.vSkill;
    }
    
    public boolean isNotCooltimeReset() {
        return this.notCooltimeReset;
    }
    
    public boolean isNotIncBuffDuration() {
        return this.notIncBuffDuration;
    }
    
    public boolean isSpecialSkill() {
        final int jobId = this.id / 10000;
        return jobId == 900 || jobId == 800 || jobId == 9000 || jobId == 9200 || jobId == 9201 || jobId == 9202 || jobId == 9203 || jobId == 9204;
    }
    
    public List<SecondaryStatEffect> getEffects() {
        return this.effects;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public List<Integer> getPsdSkills() {
        return this.psdSkills;
    }
    
    public void setPsdSkills(final List<Integer> psdSkills) {
        this.psdSkills = psdSkills;
    }
    
    public boolean isChainAttack() {
        return this.chainAttack;
    }
    
    public void setChainAttack(final boolean chainAttack) {
        this.chainAttack = chainAttack;
    }
    
    public boolean isFinalAttack() {
        return this.finalAttack;
    }
    
    public List<Integer> getSkillList() {
        return this.skillList;
    }
    
    public List<Integer> getSkillList2() {
        return this.skillList2;
    }
    
    public boolean isIgnoreCounter() {
        return this.ignoreCounter;
    }
    
    public static SecondAtom2 parseSecondAtom(final MapleData d, final int id) {
        final List<Point> aExtraPos = new ArrayList<Point>();
        final List<Integer> aCustom = new ArrayList<Integer>();
        final int dataIndex = MapleDataTool.getInt("dataIndex", d, 0);
        final int createDelay = MapleDataTool.getInt("createDelay", d, 0);
        final int enableDelay = MapleDataTool.getInt("enableDelay", d, 0);
        final int expire = MapleDataTool.getInt("expire", d, 0);
        final int attackableCount = MapleDataTool.getInt("attackableCount", d, 1);
        final Point pos = MapleDataTool.getPoint("pos", d, new Point(0, 0));
        final int rotate = MapleDataTool.getInt("rotate", d, 0);
        final int localOnly = MapleDataTool.getInt("localOnly", d, 0);
        final MapleData extraPos = d.getChildByPath("extraPos");
        if (extraPos != null) {
            for (final MapleData ep : extraPos.getChildren()) {
                aExtraPos.add(MapleDataTool.getPoint(ep));
            }
        }
        final MapleData custom = d.getChildByPath("custom");
        if (custom != null) {
            for (final MapleData c : custom.getChildren()) {
                aCustom.add(MapleDataTool.getInt(c));
            }
        }
        return new SecondAtom2(dataIndex, 0, createDelay, enableDelay, expire, 0, attackableCount, pos, rotate, aExtraPos, aCustom, localOnly, id);
    }
    
    public List<SecondAtom2> getSecondAtoms() {
        return this.secondAtoms;
    }
    
    public void setSecondAtoms(final List<SecondAtom2> secondAtoms) {
        this.secondAtoms = secondAtoms;
    }
    
    public int getFinalAttackIdx() {
        return this.finalAttackId;
    }
    
    public int getWeaponIdx() {
        return this.weaponIdx;
    }
    
    public List<RandomSkillEntry> getRSE() {
        return this.randomSkills;
    }
    
    public int getVehicleID() {
        return this.vehicleID;
    }
    
    public int getCategoryIndex() {
        return this.categoryIndex;
    }
    
    public void setCategoryIndex(final int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}
