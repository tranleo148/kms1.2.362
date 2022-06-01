package client.inventory;

import client.MapleCharacter;
import client.inventory.EquipSpecialStat;
import client.inventory.EquipStat;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import server.MapleItemInformationProvider;
import server.Randomizer;

public class Equip
        extends Item
        implements Serializable {
  public static final int ARMOR_RATIO = 350000;
  public static final int WEAPON_RATIO = 700000;
  private byte state = 0;
  private byte lines = 0;
  private byte upgradeSlots = 0;
  private byte level = 0;
  private byte vicioushammer = 0;
  private byte enhance = 0;
  private byte reqLevel = 0;
  private byte yggdrasilWisdom = 0;
  private byte totalDamage = 0;
  private byte allStat = 0;
  private byte karmaCount = (byte)-1;
  private short str = 0;
  private short dex = 0;
  private short _int = 0;
  private short luk = 0;
  private short arc = 0;
  private short hp = 0;
  private short mp = 0;
  private short watk = 0;
  private short matk = 0;
  private short wdef = 0;
  private short mdef = 0;
  private short acc = 0;
  private short avoid = 0;
  private short hands = 0;
  private short speed = 0;
  private short jump = 0;
  private short charmExp = 0;
  private short pvpDamage = 0;
  private short bossDamage = 0;
  private short ignorePDR = 0;
  private short soulname = 0;
  private short soulenchanter = 0;
  private short soulpotential = 0;
  private short enchantBuff = 0;
  private short enchantStr = 0;
  private short enchantDex = 0;
  private short enchantInt = 0;
  private short enchantLuk = 0;
  private short enchantHp = 0;
  private short enchantMp = 0;
  private short enchantWdef = 0;
  private short enchantMdef = 0;
  private short enchantAcc = 0;
  private short enchantAvoid = 0;
  private short enchantWatk = 0;
  private short enchantMatk = 0;
  private int arcexp = 0;
  private int arclevel = 0;
  private int itemEXP = 0;
  private int durability = -1;
  private int incSkill = -1;
  private int potential1 = 0;
  private int potential2 = 0;
  private int potential3 = 0;
  private int potential4 = 0;
  private int potential5 = 0;
  private int potential6 = 0;
  private int soulskill = 0;
  private int moru = 0;
  private int equipmentType = 4352;
  private int attackSpeed = 0;
  private int Coption1 = 0;
  private int Coption2 = 0;
  private int Coption3 = 0;
  private long fire = 0L;
  private long optionexpiration = 0L;
  private boolean finalStrike = false;
  private List<EquipStat> stats = new LinkedList<EquipStat>();
  private List<EquipSpecialStat> specialStats = new LinkedList<EquipSpecialStat>();

  public Equip(int id, short position, int flag) {
    super(id, position, (short)1, flag);
  }

  public Equip(int id, short position, long uniqueid, int flag) {
    super(id, position, (short)1, flag, uniqueid);
  }

  public void set(Equip set) {
    this.str = set.str;
    this.dex = set.dex;
    this._int = set._int;
    this.luk = set.luk;
    this.arc = set.arc;
    this.arclevel = set.arclevel;
    this.arcexp = set.arcexp;
    this.hp = set.hp;
    this.mp = set.mp;
    this.matk = set.matk;
    this.mdef = set.mdef;
    this.watk = set.watk;
    this.wdef = set.wdef;
    this.acc = set.acc;
    this.avoid = set.avoid;
    this.hands = set.hands;
    this.speed = set.speed;
    this.jump = set.jump;
    this.enhance = set.enhance;
    this.upgradeSlots = set.upgradeSlots;
    this.level = set.level;
    this.itemEXP = set.itemEXP;
    this.durability = set.durability;
    this.vicioushammer = set.vicioushammer;
    this.potential1 = set.potential1;
    this.potential2 = set.potential2;
    this.potential3 = set.potential3;
    this.potential4 = set.potential4;
    this.potential5 = set.potential5;
    this.potential6 = set.potential6;
    this.charmExp = set.charmExp;
    this.pvpDamage = set.pvpDamage;
    this.incSkill = set.incSkill;
    this.enchantBuff = set.enchantBuff;
    this.reqLevel = set.reqLevel;
    this.yggdrasilWisdom = set.yggdrasilWisdom;
    this.finalStrike = set.finalStrike;
    this.bossDamage = set.bossDamage;
    this.ignorePDR = set.ignorePDR;
    this.totalDamage = set.totalDamage;
    this.allStat = set.allStat;
    this.karmaCount = set.karmaCount;
    this.soulname = set.soulname;
    this.soulenchanter = set.soulenchanter;
    this.soulpotential = set.soulpotential;
    this.soulskill = set.soulskill;
    this.stats = set.stats;
    this.specialStats = set.specialStats;
    this.state = set.state;
    this.lines = set.lines;
    this.fire = set.fire;
    this.moru = set.moru;
    this.enchantStr = set.enchantStr;
    this.enchantDex = set.enchantDex;
    this.enchantInt = set.enchantInt;
    this.enchantLuk = set.enchantLuk;
    this.enchantHp = set.enchantHp;
    this.enchantMp = set.enchantMp;
    this.enchantAcc = set.enchantAcc;
    this.enchantAvoid = set.enchantAvoid;
    this.enchantWatk = set.enchantWatk;
    this.enchantMatk = set.enchantMatk;
    this.enchantWdef = set.enchantWdef;
    this.enchantMdef = set.enchantMdef;
    this.attackSpeed = set.attackSpeed;
    this.optionexpiration = set.optionexpiration;
    this.Coption1 = set.Coption1;
    this.Coption2 = set.Coption2;
    this.Coption3 = set.Coption3;
  }

  @Override
  public Item copy() {
    Equip ret = new Equip(this.getItemId(), this.getPosition(), this.getUniqueId(), this.getFlag());
    ret.str = this.str;
    ret.dex = this.dex;
    ret._int = this._int;
    ret.luk = this.luk;
    ret.arc = this.arc;
    ret.arcexp = this.arcexp;
    ret.arclevel = this.arclevel;
    ret.hp = this.hp;
    ret.mp = this.mp;
    ret.matk = this.matk;
    ret.mdef = this.mdef;
    ret.watk = this.watk;
    ret.wdef = this.wdef;
    ret.acc = this.acc;
    ret.avoid = this.avoid;
    ret.hands = this.hands;
    ret.speed = this.speed;
    ret.jump = this.jump;
    ret.enhance = this.enhance;
    ret.upgradeSlots = this.upgradeSlots;
    ret.level = this.level;
    ret.itemEXP = this.itemEXP;
    ret.durability = this.durability;
    ret.vicioushammer = this.vicioushammer;
    ret.potential1 = this.potential1;
    ret.potential2 = this.potential2;
    ret.potential3 = this.potential3;
    ret.potential4 = this.potential4;
    ret.potential5 = this.potential5;
    ret.potential6 = this.potential6;
    ret.charmExp = this.charmExp;
    ret.pvpDamage = this.pvpDamage;
    ret.incSkill = this.incSkill;
    ret.enchantBuff = this.enchantBuff;
    ret.reqLevel = this.reqLevel;
    ret.yggdrasilWisdom = this.yggdrasilWisdom;
    ret.finalStrike = this.finalStrike;
    ret.bossDamage = this.bossDamage;
    ret.ignorePDR = this.ignorePDR;
    ret.totalDamage = this.totalDamage;
    ret.allStat = this.allStat;
    ret.karmaCount = this.karmaCount;
    ret.soulname = this.soulname;
    ret.soulenchanter = this.soulenchanter;
    ret.soulpotential = this.soulpotential;
    ret.setInventoryId(this.getInventoryId());
    ret.setGiftFrom(this.getGiftFrom());
    ret.setOwner(this.getOwner());
    ret.setQuantity(this.getQuantity());
    ret.setExpiration(this.getExpiration());
    ret.stats = this.stats;
    ret.specialStats = this.specialStats;
    ret.state = this.state;
    ret.lines = this.lines;
    ret.fire = this.fire;
    ret.soulskill = this.soulskill;
    ret.equipmentType = this.equipmentType;
    ret.moru = this.moru;
    ret.enchantStr = this.enchantStr;
    ret.enchantDex = this.enchantDex;
    ret.enchantInt = this.enchantInt;
    ret.enchantLuk = this.enchantLuk;
    ret.enchantHp = this.enchantHp;
    ret.enchantMp = this.enchantMp;
    ret.enchantAcc = this.enchantAcc;
    ret.enchantAvoid = this.enchantAvoid;
    ret.enchantWatk = this.enchantWatk;
    ret.enchantMatk = this.enchantMatk;
    ret.enchantWdef = this.enchantWdef;
    ret.enchantMdef = this.enchantMdef;
    ret.attackSpeed = this.attackSpeed;
    ret.optionexpiration = this.optionexpiration;
    ret.Coption1 = this.Coption1;
    ret.Coption2 = this.Coption2;
    ret.Coption3 = this.Coption3;
    return ret;
  }

  @Override
  public byte getType() {
    return 1;
  }

  public byte getUpgradeSlots() {
    return this.upgradeSlots;
  }

  public short getStr() {
    return this.str;
  }

  public short getDex() {
    return this.dex;
  }

  public short getInt() {
    return this._int;
  }

  public short getLuk() {
    return this.luk;
  }

  public short getArc() {
    return this.arc;
  }

  public short getHp() {
    return this.hp;
  }

  public short getMp() {
    return this.mp;
  }

  public short getWatk() {
    return this.watk;
  }

  public short getMatk() {
    return this.matk;
  }

  public short getWdef() {
    return this.wdef;
  }

  public short getMdef() {
    return this.mdef;
  }

  public short getAcc() {
    return this.acc;
  }

  public short getAvoid() {
    return this.avoid;
  }

  public short getHands() {
    return this.hands;
  }

  public short getSpeed() {
    return this.speed;
  }

  public short getJump() {
    return this.jump;
  }

  public void setStr(short str) {
    if (str < 0) {
      str = 0;
    }
    this.str = str;
  }

  public void addStr(short str) {
    if (this.str + str < 0) {
      this.str = 0;
    }
    this.str = (short)(this.str + str);
  }

  public void setDex(short dex) {
    if (dex < 0) {
      dex = 0;
    }
    this.dex = dex;
  }

  public void addDex(short dex) {
    if (this.dex + dex < 0) {
      this.dex = 0;
    }
    this.dex = (short)(this.dex + dex);
  }

  public void setInt(short _int) {
    if (_int < 0) {
      _int = 0;
    }
    this._int = _int;
  }

  public void addInt(short dex) {
    if (this._int + dex < 0) {
      this._int = 0;
    }
    this._int = (short)(this._int + dex);
  }

  public void setLuk(short luk) {
    if (luk < 0) {
      luk = 0;
    }
    this.luk = luk;
  }

  public void addLuk(short dex) {
    if (this.luk + dex < 0) {
      this.luk = 0;
    }
    this.luk = (short)(this.luk + dex);
  }

  public void setArc(short arc) {
    if (arc < 0) {
      arc = 0;
    }
    this.arc = arc;
  }

  public void setHp(short hp) {
    if (hp < 0) {
      hp = 0;
    }
    this.hp = hp;
  }

  public void addHp(short dex) {
    if (this.hp + dex < 0) {
      this.hp = 0;
    }
    this.hp = (short)(this.hp + dex);
  }

  public void setMp(short mp) {
    if (mp < 0) {
      mp = 0;
    }
    this.mp = mp;
  }

  public void addMp(short dex) {
    if (this.mp + dex < 0) {
      this.mp = 0;
    }
    this.mp = (short)(this.mp + dex);
  }

  public void setWatk(short watk) {
    if (watk < 0) {
      watk = 0;
    }
    this.watk = watk;
  }

  public void addWatk(short watk) {
    if (this.watk + watk < 0) {
      this.watk = 0;
    }
    this.watk = (short)(this.watk + watk);
  }

  public void setMatk(short matk) {
    if (matk < 0) {
      matk = 0;
    }
    this.matk = matk;
  }

  public void addMatk(short watk) {
    if (this.matk + watk < 0) {
      this.matk = 0;
    }
    this.matk = (short)(this.matk + watk);
  }

  public void setWdef(short wdef) {
    if (wdef < 0) {
      wdef = 0;
    }
    this.wdef = wdef;
  }

  public void addWdef(short wdef) {
    if (wdef + this.wdef < 0) {
      this.wdef = 0;
    }
    this.wdef = (short)(this.wdef + wdef);
  }

  public void setMdef(short mdef) {
    if (mdef < 0) {
      mdef = 0;
    }
    this.mdef = mdef;
  }

  public void addMdef(short mdef) {
    if (mdef + this.mdef < 0) {
      this.mdef = 0;
    }
    this.mdef = (short)(this.mdef + mdef);
  }

  public void setAcc(short acc) {
    if (acc < 0) {
      acc = 0;
    }
    this.acc = acc;
  }

  public void addAcc(short acc) {
    if (acc + this.acc < 0) {
      this.acc = 0;
    }
    this.acc = (short)(this.acc + acc);
  }

  public void setAvoid(short avoid) {
    if (avoid < 0) {
      avoid = 0;
    }
    this.avoid = avoid;
  }

  public void setHands(short hands) {
    if (hands < 0) {
      hands = 0;
    }
    this.hands = hands;
  }

  public void setSpeed(short speed) {
    if (speed < 0) {
      speed = 0;
    }
    this.speed = speed;
  }

  public void addSpeed(short speed) {
    if (speed + this.speed < 0) {
      this.speed = 0;
    }
    this.speed = (short)(this.speed + speed);
  }

  public void addJump(short jump) {
    if (jump + this.jump < 0) {
      this.jump = 0;
    }
    this.jump = (short)(this.jump + jump);
  }

  public void setJump(short speed) {
    if (speed < 0) {
      speed = 0;
    }
    this.jump = speed;
  }

  public void setUpgradeSlots(byte upgradeSlots) {
    this.upgradeSlots = upgradeSlots;
  }

  public void addUpgradeSlots(byte upgradeSlots) {
    this.upgradeSlots = (byte)(this.upgradeSlots + upgradeSlots);
  }

  public byte getLevel() {
    if (this.getItemId() >= 1113098 && this.getItemId() <= 1113128) {
      return 0;
    }
    return this.level;
  }

  public void setLevel(byte level) {
    this.level = level;
  }

  public byte getViciousHammer() {
    return this.vicioushammer;
  }

  public void setViciousHammer(byte ham) {
    this.vicioushammer = ham;
  }

  public int getItemEXP() {
    return this.itemEXP;
  }

  public void setItemEXP(int itemEXP) {
    if (itemEXP < 0) {
      itemEXP = 0;
    }
    this.itemEXP = itemEXP;
  }

  public int getEquipExp() {
    if (this.itemEXP <= 0) {
      return 0;
    }
    if (GameConstants.isWeapon(this.getItemId())) {
      return this.itemEXP / 700000;
    }
    return this.itemEXP / 350000;
  }

  public int getEquipExpForLevel() {
    if (this.getEquipExp() <= 0) {
      return 0;
    }
    int expz = this.getEquipExp();
    for (int i = this.getBaseLevel(); i <= GameConstants.getMaxLevel(this.getItemId()) && expz >= GameConstants.getExpForLevel(i, this.getItemId()); expz -= GameConstants.getExpForLevel(i, this.getItemId()), ++i) {
    }
    return expz;
  }

  public int getExpPercentage() {
    if (this.getEquipLevel() < this.getBaseLevel() || this.getEquipLevel() > GameConstants.getMaxLevel(this.getItemId()) || GameConstants.getExpForLevel(this.getEquipLevel(), this.getItemId()) <= 0) {
      return 0;
    }
    return this.getEquipExpForLevel() * 100 / GameConstants.getExpForLevel(this.getEquipLevel(), this.getItemId());
  }

  public int getEquipLevel() {
    if (GameConstants.getMaxLevel(this.getItemId()) <= 0) {
      return 0;
    }
    if (this.getEquipExp() <= 0) {
      return this.getBaseLevel();
    }
    int levelz = this.getBaseLevel();
    int expz = this.getEquipExp();
    for (int i = levelz; i <= GameConstants.getMaxLevel(this.getItemId()) && expz >= GameConstants.getExpForLevel(i, this.getItemId()); expz -= GameConstants.getExpForLevel(i, this.getItemId()), ++i) {
      ++levelz;
    }
    return levelz;
  }

  public int getBaseLevel() {
    if (this.getItemId() >= 1113098 && this.getItemId() <= 1113128) {
      return this.level;
    }
    return 0;
  }

  @Override
  public void setQuantity(short quantity) {
    if (quantity < 0 || quantity > 1) {
      throw new RuntimeException("Setting the quantity to " + quantity + " on an equip (itemid: " + this.getItemId() + ")");
    }
    super.setQuantity(quantity);
  }

  public int getDurability() {
    return this.durability;
  }

  public void setDurability(int dur) {
    this.durability = dur;
  }

  public byte getEnhance() {
    return this.enhance;
  }

  public void setEnhance(byte en) {
    this.enhance = en;
  }

  public int getPotential1() {
    return this.potential1;
  }

  public void setPotential1(int en) {
    this.potential1 = en;
  }

  public int getPotential2() {
    return this.potential2;
  }

  public void setPotential2(int en) {
    this.potential2 = en;
  }

  public int getPotential3() {
    return this.potential3;
  }

  public void setPotential3(int en) {
    this.potential3 = en;
  }

  public int getPotential4() {
    return this.potential4;
  }

  public void setPotential4(int en) {
    this.potential4 = en;
  }

  public int getPotential5() {
    return this.potential5;
  }

  public void setPotential5(int en) {
    this.potential5 = en;
  }

  public int getPotential6() {
    return this.potential6;
  }

  public void setPotential6(int en) {
    this.potential6 = en;
  }

  public byte getState() {
    return this.state;
  }

  public void setState(byte state) {
    this.state = state;
  }

  public byte getLines() {
    return this.lines;
  }

  public void setLines(byte lines) {
    this.lines = lines;
  }

  public void resetPotential_Fuse(boolean half, int potentialState) {
    potentialState = -potentialState;
    if (Randomizer.nextInt(100) < 4) {
      potentialState -= Randomizer.nextInt(100) < 4 ? 2 : 1;
    }
    this.setPotential1(potentialState);
    this.setPotential2(Randomizer.nextInt(half ? 5 : 10) == 0 ? potentialState : 0);
    this.setPotential3(0);
    this.setPotential4(0);
    this.setPotential5(0);
  }

  public void resetPotential() {
    int rank = Randomizer.nextInt(100) < 4 ? (Randomizer.nextInt(100) < 4 ? -19 : -18) : -17;
    this.setPotential1(rank);
    this.setPotential2(Randomizer.nextInt(10) == 0 ? rank : 0);
    this.setPotential3(0);
    this.setPotential4(0);
    this.setPotential5(0);
  }

  public void renewPotential() {
    int epic = 7;
    int unique = 5;
    if (this.getState() == 17 && Randomizer.nextInt(100) <= epic) {
      this.setState((byte)2);
      return;
    }
    if (this.getState() == 18 && Randomizer.nextInt(100) <= unique) {
      this.setState((byte)3);
      return;
    }
    if (this.getState() == 19 && Randomizer.nextInt(100) <= 2) {
      this.setState((byte)4);
      return;
    }
    this.setState((byte)(this.getState() - 16));
  }

  public long getFire() {
    return this.fire;
  }

  public void setFire(long fire) {
    this.fire = fire;
  }

  public int getIncSkill() {
    return this.incSkill;
  }

  public void setIncSkill(int inc) {
    this.incSkill = inc;
  }

  public short getCharmEXP() {
    return this.charmExp;
  }

  public short getPVPDamage() {
    return this.pvpDamage;
  }

  public void setCharmEXP(short s) {
    this.charmExp = s;
  }

  public void setPVPDamage(short p) {
    this.pvpDamage = p;
  }

  public short getEnchantBuff() {
    return this.enchantBuff;
  }

  public void setEnchantBuff(short enchantBuff) {
    this.enchantBuff = enchantBuff;
  }

  public byte getReqLevel() {
    return this.reqLevel;
  }

  public void setReqLevel(byte reqLevel) {
    this.reqLevel = reqLevel;
  }

  public byte getYggdrasilWisdom() {
    return this.yggdrasilWisdom;
  }

  public void setYggdrasilWisdom(byte yggdrasilWisdom) {
    this.yggdrasilWisdom = yggdrasilWisdom;
  }

  public boolean getFinalStrike() {
    return this.finalStrike;
  }

  public void setFinalStrike(boolean finalStrike) {
    this.finalStrike = finalStrike;
  }

  public short getBossDamage() {
    return this.bossDamage;
  }

  public void setBossDamage(short bossDamage) {
    this.bossDamage = bossDamage;
  }

  public void addBossDamage(byte dmg) {
    this.bossDamage = (short)(this.bossDamage + dmg);
  }

  public short getIgnorePDR() {
    return this.ignorePDR;
  }

  public void setIgnorePDR(short ignorePDR) {
    this.ignorePDR = ignorePDR;
  }

  public void addIgnoreWdef(short ignorePDR) {
    this.ignorePDR = (short)(this.ignorePDR + ignorePDR);
  }

  public byte getTotalDamage() {
    return this.totalDamage;
  }

  public void setTotalDamage(byte totalDamage) {
    this.totalDamage = totalDamage;
  }

  public void addTotalDamage(byte totalDamage) {
    this.totalDamage = (byte)(this.totalDamage + totalDamage);
  }

  public byte getAllStat() {
    return this.allStat;
  }

  public void setAllStat(byte allStat) {
    this.allStat = allStat;
  }

  public void addAllStat(byte allStat) {
    this.allStat = (byte)(this.allStat + allStat);
  }

  public byte getKarmaCount() {
    return this.karmaCount;
  }

  public void setKarmaCount(byte karmaCount) {
    this.karmaCount = karmaCount;
  }

  public short getSoulName() {
    return this.soulname;
  }

  public void setSoulName(short soulname) {
    this.soulname = soulname;
  }

  public short getSoulEnchanter() {
    return this.soulenchanter;
  }

  public void setSoulEnchanter(short soulenchanter) {
    this.soulenchanter = soulenchanter;
  }

  public short getSoulPotential() {
    return this.soulpotential;
  }

  public void setSoulPotential(short soulpotential) {
    this.soulpotential = soulpotential;
  }

  public int getSoulSkill() {
    return this.soulskill;
  }

  public void setSoulSkill(int skillid) {
    this.soulskill = skillid;
  }

  public List<EquipStat> getStats() {
    return this.stats;
  }

  public List<EquipSpecialStat> getSpecialStats() {
    return this.specialStats;
  }

  public static Equip calculateEquipStats(Equip eq) {
    eq.getStats().clear();
    eq.getSpecialStats().clear();
    if (eq.getUpgradeSlots() > 0) {
      eq.getStats().add(EquipStat.SLOTS);
    }
    if (eq.getLevel() > 0) {
      eq.getStats().add(EquipStat.LEVEL);
    }
    if (eq.getTotalStr() > 0) {
      eq.getStats().add(EquipStat.STR);
    }
    if (eq.getTotalDex() > 0) {
      eq.getStats().add(EquipStat.DEX);
    }
    if (eq.getTotalInt() > 0) {
      eq.getStats().add(EquipStat.INT);
    }
    if (eq.getTotalLuk() > 0) {
      eq.getStats().add(EquipStat.LUK);
    }
    if (eq.getTotalHp() > 0) {
      eq.getStats().add(EquipStat.MHP);
    }
    if (eq.getTotalMp() > 0) {
      eq.getStats().add(EquipStat.MMP);
    }
    if (eq.getTotalWatk() > 0) {
      eq.getStats().add(EquipStat.WATK);
    }
    if (eq.getTotalMatk() > 0) {
      eq.getStats().add(EquipStat.MATK);
    }
    if (eq.getTotalWdef() > 0) {
      eq.getStats().add(EquipStat.WDEF);
    }
    if (eq.getHands() > 0) {
      eq.getStats().add(EquipStat.HANDS);
    }
    if (eq.getSpeed() > 0) {
      eq.getStats().add(EquipStat.SPEED);
    }
    if (eq.getJump() > 0) {
      eq.getStats().add(EquipStat.JUMP);
    }
    if (eq.getFlag() != 0) {
      eq.getStats().add(EquipStat.FLAG);
    }
    if (eq.getIncSkill() > 0) {
      eq.getStats().add(EquipStat.INC_SKILL);
    }
    if (eq.getEquipLevel() > 0 || eq.getBaseLevel() > 0) {
      eq.getStats().add(EquipStat.ITEM_LEVEL);
    }
    if (eq.getItemEXP() > 0) {
      eq.getStats().add(EquipStat.ITEM_EXP);
    }
    if (eq.getDurability() > -1) {
      eq.getStats().add(EquipStat.DURABILITY);
    }
    if (eq.getViciousHammer() > 0) {
      eq.getStats().add(EquipStat.VICIOUS_HAMMER);
    }
    if (eq.getPVPDamage() > 0) {
      eq.getStats().add(EquipStat.PVP_DAMAGE);
    }
    if (eq.getEnchantBuff() > 0) {
      eq.getStats().add(EquipStat.ENHANCT_BUFF);
    }
    if (eq.getReqLevel() > 0) {
      eq.getStats().add(EquipStat.REQUIRED_LEVEL);
    } else if (eq.getReqLevel() < 0) {
      eq.getStats().add(EquipStat.DOWNLEVEL);
    }
    if (eq.getYggdrasilWisdom() > 0) {
      eq.getStats().add(EquipStat.YGGDRASIL_WISDOM);
    }
    if (eq.getFinalStrike()) {
      eq.getStats().add(EquipStat.FINAL_STRIKE);
    }
    if (eq.getBossDamage() > 0) {
      eq.getStats().add(EquipStat.IndieBdr);
    }
    if (eq.getIgnorePDR() > 0) {
      eq.getStats().add(EquipStat.IGNORE_PDR);
    }
    if (eq.getTotalDamage() > 0) {
      eq.getSpecialStats().add(EquipSpecialStat.TOTAL_DAMAGE);
    }
    if (eq.getAllStat() > 0) {
      eq.getSpecialStats().add(EquipSpecialStat.ALL_STAT);
    }
    if (eq.getFire() >= -1L) {
      eq.getSpecialStats().add(EquipSpecialStat.KARMA_COUNT);
    }
    if (eq.getFire() > 0L) {
      eq.getSpecialStats().add(EquipSpecialStat.REBIRTH_FIRE);
    }
    if (eq.getEquipmentType() > 0) {
      eq.getSpecialStats().add(EquipSpecialStat.EQUIPMENT_TYPE);
    }
    return (Equip)eq.copy();
  }

  public int getArcEXP() {
    return this.arcexp;
  }

  public int getArcLevel() {
    return this.arclevel;
  }

  public void setArcEXP(int exp) {
    this.arcexp = exp;
  }

  public void setArcLevel(int lv) {
    this.arclevel = lv;
  }

  public void resetRebirth(int reqLevel) {
    if (GameConstants.isRing(this.getItemId()) || this.getItemId() / 1000 == 1092 || this.getItemId() / 1000 == 1342 || this.getItemId() / 1000 == 1713 || this.getItemId() / 1000 == 1712 || this.getItemId() / 1000 == 1152 || this.getItemId() / 1000 == 1143 || this.getItemId() / 1000 == 1672 || GameConstants.isSecondaryWeapon(this.getItemId()) || this.getItemId() / 1000 == 1190 || this.getItemId() / 1000 == 1191 || this.getItemId() / 1000 == 1182 || this.getItemId() / 1000 == 1662 || this.getItemId() / 1000 == 1802) {
      return;
    }
    if (this.getFire() == 0L) {
      return;
    }
    Equip ordinary = (Equip)MapleItemInformationProvider.getInstance().getEquipById(this.getItemId(), false);
    short ordinaryPad = ordinary.watk > 0 ? ordinary.watk : ordinary.matk;
    short ordinaryMad = ordinary.matk > 0 ? ordinary.matk : ordinary.watk;
    int[] rebirth = new int[4];
    String fire = String.valueOf(this.getFire());
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
    } else {
      return;
    }
    block37: for (int i = 0; i < 4; ++i) {
      int randomOption = rebirth[i] / 10;
      int randomValue = rebirth[i] - rebirth[i] / 10 * 10;
      switch (randomOption) {
        case 0: {
          this.addStr((short)(-((reqLevel / 20 + 1) * randomValue)));
          continue block37;
        }
        case 1: {
          this.addDex((short)(-((reqLevel / 20 + 1) * randomValue)));
          continue block37;
        }
        case 2: {
          this.addInt((short)(-((reqLevel / 20 + 1) * randomValue)));
          continue block37;
        }
        case 3: {
          this.addLuk((short)(-((reqLevel / 20 + 1) * randomValue)));
          continue block37;
        }
        case 4: {
          this.addStr((short)(-((reqLevel / 40 + 1) * randomValue)));
          this.addDex((short)(-((reqLevel / 40 + 1) * randomValue)));
          continue block37;
        }
        case 5: {
          this.addStr((short)(-((reqLevel / 40 + 1) * randomValue)));
          this.addInt((short)(-((reqLevel / 40 + 1) * randomValue)));
          continue block37;
        }
        case 6: {
          this.addStr((short)(-((reqLevel / 40 + 1) * randomValue)));
          this.addLuk((short)(-((reqLevel / 40 + 1) * randomValue)));
          continue block37;
        }
        case 7: {
          this.addDex((short)(-((reqLevel / 40 + 1) * randomValue)));
          this.addInt((short)(-((reqLevel / 40 + 1) * randomValue)));
          continue block37;
        }
        case 8: {
          this.addDex((short)(-((reqLevel / 40 + 1) * randomValue)));
          this.addLuk((short)(-((reqLevel / 40 + 1) * randomValue)));
          continue block37;
        }
        case 9: {
          this.addInt((short)(-((reqLevel / 40 + 1) * randomValue)));
          this.addLuk((short)(-((reqLevel / 40 + 1) * randomValue)));
          continue block37;
        }
        case 10: {
          this.addHp((short)(-(reqLevel / 10 * 10 * 3 * randomValue)));
          continue block37;
        }
        case 11: {
          this.addMp((short)(-(reqLevel / 10 * 10 * 3 * randomValue)));
          continue block37;
        }
        case 13: {
          this.addWdef((short)(-((reqLevel / 20 + 1) * randomValue)));
          continue block37;
        }
        case 17: {
          if (GameConstants.isWeapon(this.getItemId())) {
            switch (randomValue) {
              case 3: {
                if (reqLevel <= 150) {
                  this.addWatk((short)(-(ordinaryPad * 1200 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addWatk((short)(-(ordinaryPad * 1500 / 10000 + 1)));
                  continue block37;
                }
                this.addWatk((short)(-(ordinaryPad * 1800 / 10000 + 1)));
                continue block37;
              }
              case 4: {
                if (reqLevel <= 150) {
                  this.addWatk((short)(-(ordinaryPad * 1760 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addWatk((short)(-(ordinaryPad * 2200 / 10000 + 1)));
                  continue block37;
                }
                this.addWatk((short)(-(ordinaryPad * 2640 / 10000 + 1)));
                continue block37;
              }
              case 5: {
                if (reqLevel <= 150) {
                  this.addWatk((short)(-(ordinaryPad * 2420 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addWatk((short)(-(ordinaryPad * 3025 / 10000 + 1)));
                  continue block37;
                }
                this.addWatk((short)(-(ordinaryPad * 3630 / 10000 + 1)));
                continue block37;
              }
              case 6: {
                if (reqLevel <= 150) {
                  this.addWatk((short)(-(ordinaryPad * 3200 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addWatk((short)(-(ordinaryPad * 4000 / 10000 + 1)));
                  continue block37;
                }
                this.addWatk((short)(-(ordinaryPad * 4800 / 10000 + 1)));
                continue block37;
              }
              case 7: {
                if (reqLevel <= 150) {
                  this.addWatk((short)(-(ordinaryPad * 4100 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addWatk((short)(-(ordinaryPad * 5125 / 10000 + 1)));
                  continue block37;
                }
                this.addWatk((short)(-(ordinaryPad * 6150 / 10000 + 1)));
              }
            }
            continue block37;
          }
          this.addWatk((short)(-randomValue));
          continue block37;
        }
        case 18: {
          if (GameConstants.isWeapon(this.getItemId())) {
            switch (randomValue) {
              case 3: {
                if (reqLevel <= 150) {
                  this.addMatk((short)(-(ordinaryMad * 1200 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addMatk((short)(-(ordinaryMad * 1500 / 10000 + 1)));
                  continue block37;
                }
                this.addMatk((short)(-(ordinaryMad * 1800 / 10000 + 1)));
                continue block37;
              }
              case 4: {
                if (reqLevel <= 150) {
                  this.addMatk((short)(-(ordinaryMad * 1760 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addMatk((short)(-(ordinaryMad * 2200 / 10000 + 1)));
                  continue block37;
                }
                this.addMatk((short)(-(ordinaryMad * 2640 / 10000 + 1)));
                continue block37;
              }
              case 5: {
                if (reqLevel <= 150) {
                  this.addMatk((short)(-(ordinaryMad * 2420 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addMatk((short)(-(ordinaryMad * 3025 / 10000 + 1)));
                  continue block37;
                }
                this.addMatk((short)(-(ordinaryMad * 3630 / 10000 + 1)));
                continue block37;
              }
              case 6: {
                if (reqLevel <= 150) {
                  this.addMatk((short)(-(ordinaryMad * 3200 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addMatk((short)(-(ordinaryMad * 4000 / 10000 + 1)));
                  continue block37;
                }
                this.addMatk((short)(-(ordinaryMad * 4800 / 10000 + 1)));
                continue block37;
              }
              case 7: {
                if (reqLevel <= 150) {
                  this.addMatk((short)(-(ordinaryMad * 4100 / 10000 + 1)));
                  continue block37;
                }
                if (reqLevel <= 160) {
                  this.addMatk((short)(-(ordinaryMad * 5125 / 10000 + 1)));
                  continue block37;
                }
                this.addMatk((short)(-(ordinaryMad * 6150 / 10000 + 1)));
              }
            }
            continue block37;
          }
          this.addMatk((short)(-randomValue));
          continue block37;
        }
        case 19: {
          this.addSpeed((short)(-randomValue));
          continue block37;
        }
        case 20: {
          this.addJump((short)(-randomValue));
          continue block37;
        }
        case 21: {
          this.addBossDamage((byte)(-(randomValue * 2)));
          continue block37;
        }
        case 22: {
          this.setReqLevel((byte)0);
          continue block37;
        }
        case 23: {
          this.addTotalDamage((byte)(-randomValue));
          continue block37;
        }
        case 24: {
          this.addAllStat((byte)(-randomValue));
        }
      }
    }
    this.setFire(0L);
  }

  public void refreshFire(Equip item, long fire4, boolean reset) {
    int reqLevel = MapleItemInformationProvider.getInstance().getReqLevel(item.getItemId());
    if (reset) {
      item.resetRebirth(reqLevel);
    }
    short ordinaryPad = item.getWatk() > 0 ? item.getWatk() : item.getMatk();
    short ordinaryMad = item.getMatk() > 0 ? item.getMatk() : item.getWatk();
    int[] rebirth = new int[4];
    String fire = String.valueOf(fire4);
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
    if (fire.length() >= 10) {
      for (int i = 0; i < 4; ++i) {
        int randomOption = rebirth[i] / 10;
        int randomValue = rebirth[i] - rebirth[i] / 10 * 10;
        item.setFireOption(randomOption, reqLevel, randomValue, ordinaryPad, ordinaryMad);
      }
    }
    item.setFire(fire4);
  }

  public long newRebirth(int reqLevel, int scrollId, boolean update) {
    if (GameConstants.isRing(this.getItemId()) || this.getItemId() / 1000 == 1092 || this.getItemId() / 1000 == 1342 || this.getItemId() / 1000 == 1713 || this.getItemId() / 1000 == 1712 || this.getItemId() / 1000 == 1152 || this.getItemId() / 1000 == 1142 || this.getItemId() / 1000 == 1143 || this.getItemId() / 1000 == 1672 || GameConstants.isSecondaryWeapon(this.getItemId()) || this.getItemId() / 1000 == 1190 || this.getItemId() / 1000 == 1191 || this.getItemId() / 1000 == 1182 || this.getItemId() / 1000 == 1662 || this.getItemId() / 1000 == 1802) {
      return 0L;
    }
    int maxValue = 5;
    if (MapleItemInformationProvider.getInstance().getName(scrollId) != null) {
      if (MapleItemInformationProvider.getInstance().getName(scrollId).contains("강력한 환생의")) {
        maxValue = 6;
      }
      if (MapleItemInformationProvider.getInstance().getName(scrollId).contains("영원한 환생의") || MapleItemInformationProvider.getInstance().getName(scrollId).contains("검은 환생의")) {
        maxValue = 7;
      }
    }
    Equip ordinary = (Equip)MapleItemInformationProvider.getInstance().getEquipById(this.getItemId(), false);
    short ordinaryPad = ordinary.watk > 0 ? ordinary.watk : ordinary.matk;
    short ordinaryMad = ordinary.matk > 0 ? ordinary.matk : ordinary.watk;
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    if ((ii.isKarmaEnabled(this.getItemId()) || ii.isPKarmaEnabled(this.getItemId())) && this.getKarmaCount() < 0) {
      this.setKarmaCount((byte)10);
    }
    long[] rebirth = new long[]{-1L, -1L, -1L, -1L};
    int[] rebirthOptions = new int[]{-1, -1, -1, -1};
    for (int i = 0; i < 4; ++i) {
      int randomOption = Randomizer.nextInt(25);
      while (rebirthOptions[0] == randomOption || rebirthOptions[1] == randomOption || rebirthOptions[2] == randomOption || rebirthOptions[3] == randomOption || randomOption == 12 || randomOption == 14 || randomOption == 15 || randomOption == 16 || !GameConstants.isWeapon(this.getItemId()) && (randomOption == 21 || randomOption == 23)) {
        randomOption = Randomizer.nextInt(25);
      }
      rebirthOptions[i] = randomOption;
      int randomValue = 0;
      randomValue = (randomOption == 17 || randomOption == 18) && !GameConstants.isWeapon(this.getItemId()) || randomOption == 22 ? Randomizer.rand(1, maxValue) : Randomizer.rand(3, maxValue);
      rebirth[i] = randomOption * 10 + randomValue;
      for (int j = 0; j < i; ++j) {
        int n = i;
        rebirth[n] = rebirth[n] * 1000L;
      }
      if (!update) continue;
      this.setFireOption(randomOption, reqLevel, randomValue, ordinaryPad, ordinaryMad);
    }
    return rebirth[0] + rebirth[1] + rebirth[2] + rebirth[3];
  }

  public void setZeroRebirth(MapleCharacter chr, int reqLevel, int scrollId) {
    if (GameConstants.isRing(this.getItemId()) || this.getItemId() / 1000 == 1092 || this.getItemId() / 1000 == 1342 || this.getItemId() / 1000 == 1713 || this.getItemId() / 1000 == 1712 || this.getItemId() / 1000 == 1152 || this.getItemId() / 1000 == 1142 || this.getItemId() / 1000 == 1143 || this.getItemId() / 1000 == 1672 || GameConstants.isSecondaryWeapon(this.getItemId()) || this.getItemId() / 1000 == 1190 || this.getItemId() / 1000 == 1191 || this.getItemId() / 1000 == 1182 || this.getItemId() / 1000 == 1662 || this.getItemId() / 1000 == 1802) {
      return;
    }
    Equip nEquip2 = (Equip)chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short)-11);
    int maxValue = 5;
    if (MapleItemInformationProvider.getInstance().getName(scrollId) != null) {
      if (MapleItemInformationProvider.getInstance().getName(scrollId).contains("\uac15\ub825\ud55c \ud658\uc0dd\uc758")) {
        maxValue = 6;
      }
      if (MapleItemInformationProvider.getInstance().getName(scrollId).contains("\uc601\uc6d0\ud55c \ud658\uc0dd\uc758") || MapleItemInformationProvider.getInstance().getName(scrollId).contains("\uac80\uc740 \ud658\uc0dd\uc758")) {
        maxValue = 7;
      }
    }
    Equip ordinary = (Equip)MapleItemInformationProvider.getInstance().getEquipById(this.getItemId(), false);
    Equip ordinary2 = (Equip)MapleItemInformationProvider.getInstance().getEquipById(nEquip2.getItemId(), false);
    short ordinaryPad = ordinary.watk > 0 ? ordinary.watk : ordinary.matk;
    short ordinaryMad = ordinary.matk > 0 ? ordinary.matk : ordinary.watk;
    short ordinaryPad2 = ordinary2.watk > 0 ? ordinary2.watk : ordinary2.matk;
    short ordinaryMad2 = ordinary2.matk > 0 ? ordinary2.matk : ordinary2.watk;
    long[] rebirth = new long[]{-1L, -1L, -1L, -1L};
    int[] rebirthOptions = new int[]{-1, -1, -1, -1};
    for (int i = 0; i < 4; ++i) {
      int randomOption = Randomizer.nextInt(25);
      while (rebirthOptions[0] == randomOption || rebirthOptions[1] == randomOption || rebirthOptions[2] == randomOption || rebirthOptions[3] == randomOption || randomOption == 12 || randomOption == 14 || randomOption == 15 || randomOption == 16 || !GameConstants.isWeapon(this.getItemId()) && (randomOption == 21 || randomOption == 23)) {
        randomOption = Randomizer.nextInt(25);
      }
      rebirthOptions[i] = randomOption;
      int randomValue = 0;
      randomValue = (randomOption == 17 || randomOption == 18) && !GameConstants.isWeapon(this.getItemId()) || randomOption == 22 ? Randomizer.rand(1, maxValue) : Randomizer.rand(3, maxValue);
      rebirth[i] = randomOption * 10 + randomValue;
      for (int j = 0; j < i; ++j) {
        int n = i;
        rebirth[n] = rebirth[n] * 1000L;
      }
      this.setFireOption(randomOption, reqLevel, randomValue, ordinaryPad, ordinaryMad);
      if (nEquip2 == null) continue;
      nEquip2.setFireOption(randomOption, reqLevel, randomValue, ordinaryPad2, ordinaryMad2);
    }
    this.setFire(rebirth[0] + rebirth[1] + rebirth[2] + rebirth[3]);
    if (nEquip2 != null) {
      nEquip2.setFire(rebirth[0] + rebirth[1] + rebirth[2] + rebirth[3]);
    }
  }

  public int getMoru() {
    return this.moru;
  }

  public void setMoru(int moru) {
    this.moru = moru;
  }

  public void setFireOption(int randomOption, int reqLevel, int randomValue, int ordinaryPad, int ordinaryMad) {
    block0 : switch (randomOption) {
      case 0: {
        this.addStr((short)((reqLevel / 20 + 1) * randomValue));
        break;
      }
      case 1: {
        this.addDex((short)((reqLevel / 20 + 1) * randomValue));
        break;
      }
      case 2: {
        this.addInt((short)((reqLevel / 20 + 1) * randomValue));
        break;
      }
      case 3: {
        this.addLuk((short)((reqLevel / 20 + 1) * randomValue));
        break;
      }
      case 4: {
        this.addStr((short)((reqLevel / 40 + 1) * randomValue));
        this.addDex((short)((reqLevel / 40 + 1) * randomValue));
        break;
      }
      case 5: {
        this.addStr((short)((reqLevel / 40 + 1) * randomValue));
        this.addInt((short)((reqLevel / 40 + 1) * randomValue));
        break;
      }
      case 6: {
        this.addStr((short)((reqLevel / 40 + 1) * randomValue));
        this.addLuk((short)((reqLevel / 40 + 1) * randomValue));
        break;
      }
      case 7: {
        this.addDex((short)((reqLevel / 40 + 1) * randomValue));
        this.addInt((short)((reqLevel / 40 + 1) * randomValue));
        break;
      }
      case 8: {
        this.addDex((short)((reqLevel / 40 + 1) * randomValue));
        this.addLuk((short)((reqLevel / 40 + 1) * randomValue));
        break;
      }
      case 9: {
        this.addInt((short)((reqLevel / 40 + 1) * randomValue));
        this.addLuk((short)((reqLevel / 40 + 1) * randomValue));
        break;
      }
      case 10: {
        this.addHp((short)(reqLevel / 10 * 10 * 3 * randomValue));
        break;
      }
      case 11: {
        this.addMp((short)(reqLevel / 10 * 10 * 3 * randomValue));
        break;
      }
      case 13: {
        this.addWdef((short)((reqLevel / 20 + 1) * randomValue));
        break;
      }
      case 17: {
        if (GameConstants.isWeapon(this.getItemId())) {
          switch (randomValue) {
            case 3: {
              if (reqLevel <= 150) {
                this.addWatk((short)(ordinaryPad * 1200 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addWatk((short)(ordinaryPad * 1500 / 10000 + 1));
                break block0;
              }
              this.addWatk((short)(ordinaryPad * 1800 / 10000 + 1));
              break block0;
            }
            case 4: {
              if (reqLevel <= 150) {
                this.addWatk((short)(ordinaryPad * 1760 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addWatk((short)(ordinaryPad * 2200 / 10000 + 1));
                break block0;
              }
              this.addWatk((short)(ordinaryPad * 2640 / 10000 + 1));
              break block0;
            }
            case 5: {
              if (reqLevel <= 150) {
                this.addWatk((short)(ordinaryPad * 2420 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addWatk((short)(ordinaryPad * 3025 / 10000 + 1));
                break block0;
              }
              this.addWatk((short)(ordinaryPad * 3630 / 10000 + 1));
              break block0;
            }
            case 6: {
              if (reqLevel <= 150) {
                this.addWatk((short)(ordinaryPad * 3200 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addWatk((short)(ordinaryPad * 4000 / 10000 + 1));
                break block0;
              }
              this.addWatk((short)(ordinaryPad * 4800 / 10000 + 1));
              break block0;
            }
            case 7: {
              if (reqLevel <= 150) {
                this.addWatk((short)(ordinaryPad * 4100 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addWatk((short)(ordinaryPad * 5125 / 10000 + 1));
                break block0;
              }
              this.addWatk((short)(ordinaryPad * 6150 / 10000 + 1));
            }
          }
          break;
        }
        this.addWatk((short)randomValue);
        break;
      }
      case 18: {
        if (GameConstants.isWeapon(this.getItemId())) {
          switch (randomValue) {
            case 3: {
              if (reqLevel <= 150) {
                this.addMatk((short)(ordinaryMad * 1200 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addMatk((short)(ordinaryMad * 1500 / 10000 + 1));
                break block0;
              }
              this.addMatk((short)(ordinaryMad * 1800 / 10000 + 1));
              break block0;
            }
            case 4: {
              if (reqLevel <= 150) {
                this.addMatk((short)(ordinaryMad * 1760 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addMatk((short)(ordinaryMad * 2200 / 10000 + 1));
                break block0;
              }
              this.addMatk((short)(ordinaryMad * 2640 / 10000 + 1));
              break block0;
            }
            case 5: {
              if (reqLevel <= 150) {
                this.addMatk((short)(ordinaryMad * 2420 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addMatk((short)(ordinaryMad * 3025 / 10000 + 1));
                break block0;
              }
              this.addMatk((short)(ordinaryMad * 3630 / 10000 + 1));
              break block0;
            }
            case 6: {
              if (reqLevel <= 150) {
                this.addMatk((short)(ordinaryMad * 3200 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addMatk((short)(ordinaryMad * 4000 / 10000 + 1));
                break block0;
              }
              this.addMatk((short)(ordinaryMad * 4800 / 10000 + 1));
              break block0;
            }
            case 7: {
              if (reqLevel <= 150) {
                this.addMatk((short)(ordinaryMad * 4100 / 10000 + 1));
                break block0;
              }
              if (reqLevel <= 160) {
                this.addMatk((short)(ordinaryMad * 5125 / 10000 + 1));
                break block0;
              }
              this.addMatk((short)(ordinaryMad * 6150 / 10000 + 1));
            }
          }
          break;
        }
        this.addMatk((short)randomValue);
        break;
      }
      case 19: {
        this.addSpeed((short)randomValue);
        break;
      }
      case 20: {
        this.addJump((short)randomValue);
        break;
      }
      case 21: {
        this.addBossDamage((byte)(randomValue * 2));
        break;
      }
      case 22: {
        this.setReqLevel((byte)(-5 * randomValue));
        break;
      }
      case 23: {
        this.addTotalDamage((byte)randomValue);
        break;
      }
      case 24: {
        this.addAllStat((byte)randomValue);
      }
    }
  }

  public int getEquipmentType() {
    return this.equipmentType;
  }

  public void setEquipmentType(int equipmentType) {
    this.equipmentType = equipmentType;
  }

  public short getEnchantStr() {
    return this.enchantStr;
  }

  public void setEnchantStr(short enchantStr) {
    this.enchantStr = enchantStr;
  }

  public short getEnchantDex() {
    return this.enchantDex;
  }

  public void setEnchantDex(short enchantDex) {
    this.enchantDex = enchantDex;
  }

  public short getEnchantInt() {
    return this.enchantInt;
  }

  public void setEnchantInt(short enchantInt) {
    this.enchantInt = enchantInt;
  }

  public short getEnchantLuk() {
    return this.enchantLuk;
  }

  public void setEnchantLuk(short enchantLuk) {
    this.enchantLuk = enchantLuk;
  }

  public short getEnchantHp() {
    return this.enchantHp;
  }

  public void setEnchantHp(short enchantHp) {
    this.enchantHp = enchantHp;
  }

  public short getEnchantMp() {
    return this.enchantMp;
  }

  public void setEnchantMp(short enchantMp) {
    this.enchantMp = enchantMp;
  }

  public short getEnchantAcc() {
    return this.enchantAcc;
  }

  public void setEnchantAcc(short enchantAcc) {
    this.enchantAcc = enchantAcc;
  }

  public short getEnchantAvoid() {
    return this.enchantAvoid;
  }

  public void setEnchantAvoid(short enchantAvoid) {
    this.enchantAvoid = enchantAvoid;
  }

  public short getEnchantWatk() {
    return this.enchantWatk;
  }

  public void setEnchantWatk(short enchantWatk) {
    this.enchantWatk = enchantWatk;
  }

  public short getEnchantMatk() {
    return this.enchantMatk;
  }

  public void setEnchantMatk(short enchantMatk) {
    this.enchantMatk = enchantMatk;
  }

  public short getTotalStr() {
    return (short)(this.str + this.enchantStr);
  }

  public short getTotalDex() {
    return (short)(this.dex + this.enchantDex);
  }

  public short getTotalInt() {
    return (short)(this._int + this.enchantInt);
  }

  public short getTotalLuk() {
    return (short)(this.luk + this.enchantLuk);
  }

  public short getTotalHp() {
    return (short)(this.hp + this.enchantHp);
  }

  public short getTotalMp() {
    return (short)(this.mp + this.enchantMp);
  }

  public short getTotalAcc() {
    return (short)(this.acc + this.enchantAcc);
  }

  public short getTotalAvoid() {
    return (short)(this.avoid + this.enchantAvoid);
  }

  public short getTotalWatk() {
    return (short)(this.watk + this.enchantWatk);
  }

  public short getTotalMatk() {
    return (short)(this.matk + this.enchantMatk);
  }

  public short getTotalWdef() {
    return (short)(this.wdef + this.enchantWdef);
  }

  public short getTotalMdef() {
    return (short)(this.mdef + this.enchantMdef);
  }

  public short getEnchantWdef() {
    return this.enchantWdef;
  }

  public void setEnchantWdef(short enchantWdef) {
    this.enchantWdef = enchantWdef;
  }

  public short getEnchantMdef() {
    return this.enchantMdef;
  }

  public void setEnchantMdef(short enchantMdef) {
    this.enchantMdef = enchantMdef;
  }

  public int getAttackSpeed() {
    return this.attackSpeed;
  }

  public void setAttackSpeed(int attackSpeed) {
    this.attackSpeed = attackSpeed;
  }

  public long getOptionExpiration() {
    return this.optionexpiration;
  }

  public void setOptionExpiration(long a) {
    this.optionexpiration = a;
  }

  public int getCoption1() {
    return this.Coption1;
  }

  public void setCoption1(int a) {
    this.Coption1 = a;
  }

  public int getCoption2() {
    return this.Coption2;
  }

  public void setCoption2(int a) {
    this.Coption2 = a;
  }

  public int getCoption3() {
    return this.Coption3;
  }

  public void setCoption3(int a) {
    this.Coption3 = a;
  }

  public static enum ScrollResult {
    SUCCESS,
    FAIL,
    CURSE;

  }
}

