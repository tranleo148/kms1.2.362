package server.life;

import constants.GameConstants;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tools.Pair;
import tools.Triple;

public class MapleMonsterStats {
  private byte cp;
  
  private byte selfDestruction_action;
  
  private byte tagColor;
  
  private byte tagBgColor;
  
  private byte rareItemDropLevel;
  
  private byte HPDisplayType;
  
  private byte summonType;
  
  private byte category;
  
  private short level;
  
  private short charismaEXP;
  
  private long hp;
  
  private long exp;
  
  private int id;
  
  private int mp;
  
  private int removeAfter;
  
  private int buffToGive;
  
  private int fixedDamage;
  
  private int selfDestruction_hp;
  
  private int dropItemPeriod;
  
  private int point;
  
  private int eva;
  
  private int acc;
  
  private int PhysicalAttack;
  
  private int MagicAttack;
  
  private int speed;
  
  private int partyBonusR;
  
  private int pushed;
  
  private int HpLinkMob;
  
  private int PDRate;
  
  private int MDRate;
  
  private int ignoreMovable;
  
  private boolean mobZone;
  
  private boolean boss;
  
  private boolean undead;
  
  private boolean firstAttack;
  
  private boolean isExplosiveReward;
  
  private boolean mobile;
  
  private boolean fly;
  
  private boolean onlyNormalAttack;
  
  private boolean friendly;
  
  private boolean noDoom;
  
  private boolean invincible;
  
  private boolean partyBonusMob;
  
  private boolean changeable;
  
  private boolean escort;
  
  private boolean publicReward;
  
  private boolean notSeperateSoul;
  
  private String name;
  
  private String mobType;
  
  private String ignoreMoveableMsg;
  
  private EnumMap<Element, ElementalEffectiveness> resistance = new EnumMap<>(Element.class);
  
  private List<Integer> revives = new ArrayList<>();
  
  private List<MobSkill> skills = new ArrayList<>();
  
  private List<MobAttack> attacks = new ArrayList<>();
  
  private Map<Pair<Integer, Integer>, Integer> afterAttack = new HashMap<>();
  
  private BanishInfo banish;
  
  private Transform trans;
  
  private List<Triple<String, Integer, Integer>> skeleton = new ArrayList<>();
  
  public MapleMonsterStats(int id) {
    this.id = id;
  }
  
  public int getId() {
    return this.id;
  }
  
  public long getExp() {
    return this.exp;
  }
  
  public void setExp(long exp) {
    this.exp = exp;
  }
  
  public long getHp() {
    return this.hp;
  }
  
  public void setHp(long hp) {
    this.hp = hp;
  }
  
  public int getMp() {
    return this.mp;
  }
  
  public void setMp(int mp) {
    this.mp = mp;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public short getCharismaEXP() {
    return this.charismaEXP;
  }
  
  public void setCharismaEXP(short leve) {
    this.charismaEXP = leve;
  }
  
  public void setSelfD(byte selfDestruction_action) {
    this.selfDestruction_action = selfDestruction_action;
  }
  
  public byte getSelfD() {
    return this.selfDestruction_action;
  }
  
  public void setSelfDHP(int selfDestruction_hp) {
    this.selfDestruction_hp = selfDestruction_hp;
  }
  
  public int getSelfDHp() {
    return this.selfDestruction_hp;
  }
  
  public void setFixedDamage(int damage) {
    this.fixedDamage = damage;
  }
  
  public int getFixedDamage() {
    return this.fixedDamage;
  }
  
  public void setPushed(int damage) {
    this.pushed = damage;
  }
  
  public int getPushed() {
    return this.pushed;
  }
  
  public void setPhysicalAttack(int PhysicalAttack) {
    this.PhysicalAttack = PhysicalAttack;
  }
  
  public int getPhysicalAttack() {
    return this.PhysicalAttack;
  }
  
  public final void setMagicAttack(int MagicAttack) {
    this.MagicAttack = MagicAttack;
  }
  
  public final int getMagicAttack() {
    return this.MagicAttack;
  }
  
  public final void setEva(int eva) {
    this.eva = eva;
  }
  
  public final int getEva() {
    return this.eva;
  }
  
  public final void setAcc(int acc) {
    this.acc = acc;
  }
  
  public final int getAcc() {
    return this.acc;
  }
  
  public final void setSpeed(int speed) {
    this.speed = speed;
  }
  
  public final int getSpeed() {
    return this.speed;
  }
  
  public final void setPartyBonusRate(int speed) {
    this.partyBonusR = speed;
  }
  
  public final int getPartyBonusRate() {
    return this.partyBonusR;
  }
  
  public void setOnlyNormalAttack(boolean onlyNormalAttack) {
    this.onlyNormalAttack = onlyNormalAttack;
  }
  
  public boolean getOnlyNoramlAttack() {
    return this.onlyNormalAttack;
  }
  
  public BanishInfo getBanishInfo() {
    return this.banish;
  }
  
  public void setBanishInfo(BanishInfo banish) {
    this.banish = banish;
  }
  
  public int getRemoveAfter() {
    return this.removeAfter;
  }
  
  public void setRemoveAfter(int removeAfter) {
    this.removeAfter = removeAfter;
  }
  
  public byte getrareItemDropLevel() {
    return this.rareItemDropLevel;
  }
  
  public void setrareItemDropLevel(byte rareItemDropLevel) {
    this.rareItemDropLevel = rareItemDropLevel;
  }
  
  public void setBoss(boolean boss) {
    this.boss = boss;
  }
  
  public boolean isBoss() {
    return this.boss;
  }
  
  public void setEscort(boolean ffaL) {
    this.escort = ffaL;
  }
  
  public boolean isEscort() {
    return this.escort;
  }
  
  public void setExplosiveReward(boolean isExplosiveReward) {
    this.isExplosiveReward = isExplosiveReward;
  }
  
  public boolean isExplosiveReward() {
    return this.isExplosiveReward;
  }
  
  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }
  
  public boolean getMobile() {
    return this.mobile;
  }
  
  public void setFly(boolean fly) {
    this.fly = fly;
  }
  
  public boolean getFly() {
    return this.fly;
  }
  
  public List<Integer> getRevives() {
    return this.revives;
  }
  
  public void setRevives(List<Integer> revives) {
    this.revives = revives;
  }
  
  public void setUndead(boolean undead) {
    this.undead = undead;
  }
  
  public boolean getUndead() {
    return this.undead;
  }
  
  public void setSummonType(byte selfDestruction) {
    this.summonType = selfDestruction;
  }
  
  public byte getSummonType() {
    return this.summonType;
  }
  
  public void setCategory(byte selfDestruction) {
    this.category = selfDestruction;
  }
  
  public byte getCategory() {
    return this.category;
  }
  
  public void setPDRate(int selfDestruction) {
    this.PDRate = selfDestruction;
  }
  
  public int getPDRate() {
    return this.PDRate;
  }
  
  public void setMDRate(int selfDestruction) {
    this.MDRate = selfDestruction;
  }
  
  public int getMDRate() {
    return this.MDRate;
  }
  
  public EnumMap<Element, ElementalEffectiveness> getElements() {
    return this.resistance;
  }
  
  public void setEffectiveness(Element e, ElementalEffectiveness ee) {
    this.resistance.put(e, ee);
  }
  
  public void removeEffectiveness(Element e) {
    this.resistance.remove(e);
  }
  
  public ElementalEffectiveness getEffectiveness(Element e) {
    ElementalEffectiveness elementalEffectiveness = this.resistance.get(e);
    if (elementalEffectiveness == null)
      return ElementalEffectiveness.NORMAL; 
    return elementalEffectiveness;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getType() {
    return this.mobType;
  }
  
  public void setType(String mobt) {
    this.mobType = mobt;
  }
  
  public byte getTagColor() {
    return this.tagColor;
  }
  
  public void setTagColor(int tagColor) {
    this.tagColor = (byte)tagColor;
  }
  
  public byte getTagBgColor() {
    return this.tagBgColor;
  }
  
  public void setTagBgColor(int tagBgColor) {
    this.tagBgColor = (byte)tagBgColor;
  }
  
  public void setSkills(List<MobSkill> skills2) {
    for (MobSkill skill : skills2)
      this.skills.add(skill); 
  }
  
  public List<MobSkill> getSkills() {
    return this.skills;
  }
  
  public MobSkill getSkill(int skillId, int level) {
    for (MobSkill skill : this.skills) {
      if (skill.getSkillId() == skillId && skill.getSkillLevel() == level)
        return skill; 
    } 
    return null;
  }
  
  public byte getNoSkills() {
    return (byte)this.skills.size();
  }
  
  public boolean hasSkill(int skillId, int level) {
    for (MobSkill skill : this.skills) {
      if (skill.getSkillId() == skillId && skill.getSkillLevel() == level)
        return true; 
    } 
    return false;
  }
  
  public void setFirstAttack(boolean firstAttack) {
    this.firstAttack = firstAttack;
  }
  
  public boolean isFirstAttack() {
    return this.firstAttack;
  }
  
  public void setCP(byte cp) {
    this.cp = cp;
  }
  
  public byte getCP() {
    return this.cp;
  }
  
  public void setPoint(int cp) {
    this.point = cp;
  }
  
  public int getPoint() {
    return this.point;
  }
  
  public void setFriendly(boolean friendly) {
    this.friendly = friendly;
  }
  
  public boolean isFriendly() {
    return this.friendly;
  }
  
  public void setInvincible(boolean invin) {
    this.invincible = invin;
  }
  
  public boolean isInvincible() {
    return this.invincible;
  }
  
  public void setChange(boolean invin) {
    this.changeable = invin;
  }
  
  public boolean isChangeable() {
    return this.changeable;
  }
  
  public void setPartyBonus(boolean invin) {
    this.partyBonusMob = invin;
  }
  
  public boolean isPartyBonus() {
    return this.partyBonusMob;
  }
  
  public void setNoDoom(boolean doom) {
    this.noDoom = doom;
  }
  
  public boolean isNoDoom() {
    return this.noDoom;
  }
  
  public void setBuffToGive(int buff) {
    this.buffToGive = buff;
  }
  
  public int getBuffToGive() {
    return this.buffToGive;
  }
  
  public byte getHPDisplayType() {
    return this.HPDisplayType;
  }
  
  public void setHPDisplayType(byte HPDisplayType) {
    this.HPDisplayType = HPDisplayType;
  }
  
  public int getDropItemPeriod() {
    return this.dropItemPeriod;
  }
  
  public void setDropItemPeriod(int d) {
    this.dropItemPeriod = d;
  }
  
  public int dropsMeso() {
    if (getRemoveAfter() != 0 || isInvincible() || getOnlyNoramlAttack() || getDropItemPeriod() > 0 || getCP() > 0 || getPoint() > 0 || getFixedDamage() > 0 || getSelfD() != -1 || getPDRate() <= 0 || getMDRate() <= 0)
      return 0; 
    int mobId = getId() / 100000;
    if (GameConstants.getPartyPlayHP(getId()) > 0 || mobId == 97 || mobId == 95 || mobId == 93 || mobId == 91 || mobId == 90)
      return 0; 
    if (isExplosiveReward())
      return 7; 
    if (isBoss())
      return 2; 
    return 1;
  }
  
  public boolean isMobZone() {
    return this.mobZone;
  }
  
  public void setMobZone(boolean mobZone) {
    this.mobZone = mobZone;
  }
  
  public List<MobAttack> getAttacks() {
    return this.attacks;
  }
  
  public void setAttacks(List<MobAttack> attacks) {
    this.attacks = attacks;
  }
  
  public Map<Pair<Integer, Integer>, Integer> getAfterAttack() {
    return this.afterAttack;
  }
  
  public void setAfterAttack(Map<Pair<Integer, Integer>, Integer> afterAttack) {
    this.afterAttack = afterAttack;
  }
  
  public List<Triple<String, Integer, Integer>> getSkeleton() {
    return this.skeleton;
  }
  
  public void setSkeleton(List<Triple<String, Integer, Integer>> skeleton) {
    this.skeleton = skeleton;
  }
  
  public void addSkeleton(String key, Integer mid, Integer value) {
    this.skeleton.add(new Triple<>(key, mid, value));
  }
  
  public Transform getTrans() {
    return this.trans;
  }
  
  public void setTrans(Transform trans) {
    this.trans = trans;
  }
  
  public boolean isPublicReward() {
    return this.publicReward;
  }
  
  public void setPublicReward(boolean publicReward) {
    this.publicReward = publicReward;
  }
  
  public int getHpLinkMob() {
    return this.HpLinkMob;
  }
  
  public void setHpLinkMob(int hpLinkMob) {
    this.HpLinkMob = hpLinkMob;
  }
  
  public boolean isNotSeperateSoul() {
    return this.notSeperateSoul;
  }
  
  public void setNotSeperateSoul(boolean notSeperateSoul) {
    this.notSeperateSoul = notSeperateSoul;
  }
  
  public int getIgnoreMovable() {
    return this.ignoreMovable;
  }
  
  public void setIgnoreMovable(int ignoreMovable) {
    this.ignoreMovable = ignoreMovable;
  }
  
  public String getIgnoreMoveableMsg() {
    return this.ignoreMoveableMsg;
  }
  
  public void setIgnoreMoveableMsg(String ignoreMoveableMsg) {
    this.ignoreMoveableMsg = ignoreMoveableMsg;
  }
}
