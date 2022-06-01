package handling.world;

import client.AvatarLook;
import client.BuddylistEntry;
import client.CharacterNameAndId;
import client.Core;
import client.MapleCharacter;
import client.MapleMannequin;
import client.MapleQuestStatus;
import client.MapleTrait;
import client.MapleUnion;
import client.SecondaryStat;
import client.Skill;
import client.SkillEntry;
import client.VMatrix;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleImp;
import client.inventory.MapleMount;
import client.inventory.MaplePet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import server.MapleChatEmoticon;
import server.MapleSavedEmoticon;
import server.SecondaryStatEffect;
import server.SkillCustomInfo;
import server.quest.MapleQuest;
import tools.Pair;
import tools.Triple;

public class CharacterTransfer implements Externalizable {
  public int characterid;
  
  public int accountid;
  
  public int fame;
  
  public int pvpExp;
  
  public int pvpPoints;
  
  public int energy;
  
  public int hair;
  
  public int secondhair;
  
  public int face;
  
  public int secondface;
  
  public int demonMarking;
  
  public int mapid;
  
  public int guildid;
  
  public int partyid;
  
  public int messengerid;
  
  public int nxCredit;
  
  public int ACash;
  
  public int MaplePoints;
  
  public int honourexp;
  
  public int honourlevel;
  
  public int itcafetime;
  
  public int mount_itemid;
  
  public int mount_exp;
  
  public int points;
  
  public int vpoints;
  
  public int marriageId;
  
  public int LinkMobCount;
  
  public int lastCharGuildId;
  
  public int betaclothes;
  
  public int returnSc;
  
  public int familyid;
  
  public int seniorid;
  
  public int junior1;
  
  public int junior2;
  
  public int currentrep;
  
  public int totalrep;
  
  public int battleshipHP;
  
  public int guildContribution;
  
  public int lastattendance;
  
  public int totalWins;
  
  public int totalLosses;
  
  public int basecolor;
  
  public int addcolor;
  
  public int baseprob;
  
  public int secondbasecolor;
  
  public int secondaddcolor;
  
  public int secondbaseprob;
  
  public int SpectorGauge;
  
  public byte channel;
  
  public byte gender;
  
  public byte secondgender;
  
  public byte gmLevel;
  
  public byte guildrank;
  
  public byte alliancerank;
  
  public byte clonez;
  
  public byte fairyExp;
  
  public byte cardStack;
  
  public byte buddysize;
  
  public byte world;
  
  public byte initialSpawnPoint;
  
  public byte skinColor;
  
  public byte secondSkinColor;
  
  public byte mount_level;
  
  public byte mount_Fatigue;
  
  public byte subcategory;
  
  public long meso;
  
  public long maxhp;
  
  public long maxmp;
  
  public long hp;
  
  public long mp;
  
  public long exp;
  
  public long lastfametime;
  
  public long TranferTime;
  
  public String name;
  
  public String accountname;
  
  public String secondPassword;
  
  public String BlessOfFairy;
  
  public String BlessOfEmpress;
  
  public String chalkboard;
  
  public String tempIP;
  
  public String auth;
  
  public short level;
  
  public short str;
  
  public short dex;
  
  public short int_;
  
  public short luk;
  
  public short remainingAp;
  
  public short hpApUsed;
  
  public short job;
  
  public short fatigue;
  
  public short soulCount;
  
  public Object inventorys;
  
  public Object skillmacro;
  
  public Object storage;
  
  public Object cs;
  
  public Object anticheat;
  
  public Object innerSkills;
  
  public Object choicepotential;
  
  public Object returnscroll;
  
  public Object memorialcube;
  
  public int[] savedlocation;
  
  public int[] wishlist;
  
  public int[] rocks;
  
  public int[] remainingSp;
  
  public int[] regrocks;
  
  public int[] hyperrocks;
  
  public MapleImp[] imps;
  
  public MaplePet[] pets = new MaplePet[3];
  
  public Map<Byte, Integer> reports = new LinkedHashMap<>();
  
  public Map<SecondaryStat, List<Pair<Integer, SecondaryStatEffect>>> indietemp = new HashMap<>();
  
  public List<Pair<Integer, Boolean>> stolenSkills;
  
  public Map<Integer, Pair<Byte, Integer>> keymap;
  
  public List<Integer> famedcharacters = null;
  
  public List<Integer> battledaccs = null;
  
  public List<Integer> extendedSlots = null;
  
  public List<Integer> exceptionList = null;
  
  public List<Item> rebuy = null;
  
  public List<Item> auctionitems = null;
  
  public List<Core> cores = null;
  
  public List<AvatarLook> coodination = null;
  
  public List<VMatrix> matrixs = null;
  
  public List<Equip> symbol = null;
  
  public List<Triple<Skill, SkillEntry, Integer>> linkskills = null;
  
  public final Map<MapleTrait.MapleTraitType, Integer> traits = new EnumMap<>(MapleTrait.MapleTraitType.class);
  
  public final Map<CharacterNameAndId, Boolean> buddies = new LinkedHashMap<>();
  
  public final List<MapleUnion> unions = new ArrayList<>();
  
  public final Map<Integer, Object> Quest = new LinkedHashMap<>();
  
  public Map<Integer, String> InfoQuest;
  
  public ScheduledFuture<?> secondaryStatEffectTimer;
  
  public final Map<Integer, SkillEntry> Skills = new LinkedHashMap<>();
  
  public final Map<Integer, Integer> customValue = new HashMap<>();
  
  public Map<String, String> keyValues = new HashMap<>();
  
  public Timer DFRecoveryTimer;
  
  public int reborns;
  
  public int apstorage;
  
  public boolean login;
  
  public boolean energycharge;
  
  public List<MapleMannequin> hairRoom;
  
  public List<MapleMannequin> faceRoom;
  
  public List<MapleMannequin> skinRoom;
  
  public final Map<Integer, SkillCustomInfo> customInfo = new LinkedHashMap<>();
  
  public List<MapleSavedEmoticon> savedEmoticon;
  
  public List<MapleChatEmoticon> emoticonTabs;
  
  public List<Triple<Long, Integer, Short>> emoticons;
  
  private List<Pair<Integer, Short>> emoticonBookMarks;
  
  public CharacterTransfer() {
    this.famedcharacters = new ArrayList<>();
    this.battledaccs = new ArrayList<>();
    this.extendedSlots = new ArrayList<>();
    this.exceptionList = new ArrayList<>();
    this.rebuy = new ArrayList<>();
    this.cores = new ArrayList<>();
    this.coodination = new ArrayList<>();
    this.matrixs = new ArrayList<>();
    this.symbol = new ArrayList<>();
    this.auctionitems = new ArrayList<>();
    this.linkskills = new ArrayList<>();
    this.InfoQuest = new LinkedHashMap<>();
    this.keymap = new LinkedHashMap<>();
    this.hairRoom = new ArrayList<>();
    this.faceRoom = new ArrayList<>();
    this.skinRoom = new ArrayList<>();
    this.savedEmoticon = new CopyOnWriteArrayList<>();
    this.emoticonTabs = new CopyOnWriteArrayList<>();
    this.emoticons = new CopyOnWriteArrayList<>();
    this.emoticonBookMarks = new CopyOnWriteArrayList<>();
  }
  
  public CharacterTransfer(MapleCharacter chr) {
    this.characterid = chr.getId();
    this.accountid = chr.getAccountID();
    this.accountname = chr.getClient().getAccountName();
    this.secondPassword = chr.getClient().getSecondPassword();
    this.channel = (byte)chr.getClient().getChannel();
    this.nxCredit = chr.getCSPoints(1);
    this.ACash = 0;
    this.MaplePoints = chr.getCSPoints(2);
    this.vpoints = chr.getVPoints();
    this.stolenSkills = chr.getStolenSkills();
    this.name = chr.getName();
    this.fame = chr.getFame();
    this.gender = chr.getGender();
    this.secondgender = chr.getSecondGender();
    this.level = chr.getLevel();
    this.str = chr.getStat().getStr();
    this.dex = chr.getStat().getDex();
    this.int_ = chr.getStat().getInt();
    this.luk = chr.getStat().getLuk();
    this.hp = chr.getStat().getHp();
    this.mp = chr.getStat().getMp();
    this.maxhp = chr.getStat().getMaxHp();
    this.maxmp = chr.getStat().getMaxMp();
    this.exp = chr.getExp();
    this.hpApUsed = chr.getHpApUsed();
    this.remainingAp = chr.getRemainingAp();
    this.remainingSp = chr.getRemainingSps();
    this.meso = chr.getMeso();
    this.pvpExp = chr.getTotalBattleExp();
    this.pvpPoints = chr.getBattlePoints();
    this.itcafetime = chr.getInternetCafeTime();
    this.reborns = chr.getReborns();
    this.apstorage = chr.getAPS();
    this.skinColor = chr.getSkinColor();
    this.secondSkinColor = chr.getSecondSkinColor();
    this.job = chr.getJob();
    this.hair = chr.getHair();
    this.secondhair = chr.getSecondHair();
    this.face = chr.getFace();
    this.secondface = chr.getSecondFace();
    this.demonMarking = chr.getDemonMarking();
    this.mapid = chr.getMapId();
    this.initialSpawnPoint = chr.getInitialSpawnpoint();
    this.marriageId = chr.getMarriageId();
    this.world = chr.getWorld();
    this.guildid = chr.getGuildId();
    this.guildrank = chr.getGuildRank();
    this.guildContribution = chr.getGuildContribution();
    this.lastattendance = chr.getLastAttendance();
    this.alliancerank = chr.getAllianceRank();
    this.gmLevel = (byte)chr.getGMLevel();
    this.LinkMobCount = chr.getLinkMobCount();
    this.points = chr.getPoints();
    this.fairyExp = chr.getFairyExp();
    this.cardStack = chr.getCardStack();
    this.pets = chr.getPets();
    this.subcategory = chr.getSubcategory();
    this.imps = chr.getImps();
    this.fatigue = chr.getFatigue();
    this.currentrep = chr.getCurrentRep();
    this.totalrep = chr.getTotalRep();
    this.totalWins = chr.getTotalWins();
    this.totalLosses = chr.getTotalLosses();
    this.battleshipHP = chr.currentBattleshipHP();
    this.tempIP = chr.getClient().getTempIP();
    this.rebuy = chr.getRebuy();
    this.cores = chr.getCore();
    this.matrixs = chr.getMatrixs();
    this.symbol = chr.getSymbol();
    this.basecolor = chr.getBaseColor();
    this.addcolor = chr.getAddColor();
    this.baseprob = chr.getBaseProb();
    this.secondbasecolor = chr.getSecondBaseColor();
    this.secondaddcolor = chr.getSecondAddColor();
    this.secondbaseprob = chr.getSecondBaseProb();
    this.linkskills = chr.getLinkSkills();
    this.choicepotential = chr.choicepotential;
    this.returnscroll = chr.returnscroll;
    this.memorialcube = chr.memorialcube;
    this.returnSc = chr.returnSc;
    this.lastCharGuildId = chr.getLastCharGuildId();
    this.betaclothes = chr.getBetaClothes();
    this.energy = chr.energy;
    this.energycharge = chr.energyCharge;
    this.hairRoom = chr.getHairRoom();
    this.faceRoom = chr.getFaceRoom();
    this.skinRoom = chr.getSkinRoom();
    this.emoticons = chr.getEmoticons();
    this.emoticonTabs = chr.getEmoticonTabs();
    this.savedEmoticon = chr.getSavedEmoticon();
    this.SpectorGauge = chr.SpectorGauge;
    this.keyValues.putAll(chr.getKeyValues());
    chr.getSecondaryStatEffectTimer().cancel(true);
    for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values())
      this.traits.put(t, Integer.valueOf(chr.getTrait(t).getTotalExp())); 
    for (BuddylistEntry qs : chr.getBuddylist().getBuddies())
      this.buddies.put(new CharacterNameAndId(qs.getCharacterId(), qs.getAccountId(), qs.getName(), qs.getRepName(), qs.getLevel(), qs.getJob(), qs.getGroupName(), qs.getMemo()), Boolean.valueOf(qs.isVisible())); 
    for (MapleUnion union : chr.getUnions().getUnions())
      this.unions.add(union); 
    this.buddysize = chr.getBuddyCapacity();
    this.partyid = (chr.getParty() == null) ? -1 : chr.getParty().getId();
    if (chr.getMessenger() != null) {
      this.messengerid = chr.getMessenger().getId();
    } else {
      this.messengerid = 0;
    } 
    this.InfoQuest = chr.getInfoQuest_Map();
    for (Map.Entry<MapleQuest, MapleQuestStatus> qs : chr.getQuest_Map().entrySet())
      this.Quest.put(Integer.valueOf(((MapleQuest)qs.getKey()).getId()), qs.getValue()); 
    this.inventorys = chr.getInventorys();
    for (Map.Entry<Skill, SkillEntry> qs : chr.getSkills().entrySet())
      this.Skills.put(Integer.valueOf(((Skill)qs.getKey()).getId()), qs.getValue()); 
    for (Map.Entry<Integer, Integer> cv : chr.getSkillCustomValues2().entrySet())
      this.customValue.put(cv.getKey(), cv.getValue()); 
    for (Map.Entry<Integer, SkillCustomInfo> ci : chr.getSkillCustomValues().entrySet())
      this.customInfo.put(ci.getKey(), ci.getValue()); 
    this.BlessOfFairy = chr.getBlessOfFairyOrigin();
    this.BlessOfEmpress = chr.getBlessOfEmpressOrigin();
    this.chalkboard = chr.getChalkboard();
    this.skillmacro = chr.getMacros();
    this.coodination = chr.getCoodination();
    this.keymap = chr.getKeyLayout().Layout();
    this.savedlocation = chr.getSavedLocations();
    this.wishlist = chr.getWishlist();
    this.rocks = chr.getRocks();
    this.regrocks = chr.getRegRocks();
    this.hyperrocks = chr.getHyperRocks();
    this.famedcharacters = chr.getFamedCharacters();
    this.battledaccs = chr.getBattledCharacters();
    this.lastfametime = chr.getLastFameTime();
    this.storage = chr.getStorage();
    this.cs = chr.getCashInventory();
    this.honourexp = chr.getHonourExp();
    this.honourlevel = chr.getHonorLevel();
    this.innerSkills = chr.getInnerSkills();
    this.extendedSlots = chr.getExtendedSlots();
    this.exceptionList = chr.getExceptionList();
    MapleMount mount = chr.getMount();
    this.mount_itemid = mount.getItemId();
    this.mount_Fatigue = mount.getFatigue();
    this.mount_level = mount.getLevel();
    this.mount_exp = mount.getExp();
    this.TranferTime = System.currentTimeMillis();
    this.login = chr.getClient().isFirstlogin();
  }
  
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.characterid = in.readInt();
    this.accountid = in.readInt();
    this.accountname = in.readUTF();
    this.secondPassword = in.readUTF();
    this.channel = in.readByte();
    this.nxCredit = in.readInt();
    this.ACash = in.readInt();
    this.MaplePoints = in.readInt();
    this.name = in.readUTF();
    this.fame = in.readInt();
    this.gender = in.readByte();
    this.secondgender = in.readByte();
    this.level = in.readShort();
    this.str = in.readShort();
    this.dex = in.readShort();
    this.int_ = in.readShort();
    this.luk = in.readShort();
    this.hp = in.readInt();
    this.mp = in.readInt();
    this.maxhp = in.readInt();
    this.maxmp = in.readInt();
    this.exp = in.readLong();
    this.hpApUsed = in.readShort();
    this.remainingAp = in.readShort();
    this.remainingSp = new int[in.readByte()];
    for (int i = 0; i < this.remainingSp.length; i++)
      this.remainingSp[i] = in.readInt(); 
    this.meso = in.readLong();
    this.skinColor = in.readByte();
    this.secondSkinColor = in.readByte();
    this.job = in.readShort();
    this.hair = in.readInt();
    this.secondhair = in.readInt();
    this.face = in.readInt();
    this.secondface = in.readInt();
    this.demonMarking = in.readInt();
    this.mapid = in.readInt();
    this.initialSpawnPoint = in.readByte();
    this.world = in.readByte();
    this.guildid = in.readInt();
    this.guildrank = in.readByte();
    this.guildContribution = in.readInt();
    this.alliancerank = in.readByte();
    this.gmLevel = in.readByte();
    this.points = in.readInt();
    this.vpoints = in.readInt();
    if (in.readByte() == 1) {
      this.BlessOfFairy = in.readUTF();
    } else {
      this.BlessOfFairy = null;
    } 
    if (in.readByte() == 1) {
      this.BlessOfEmpress = in.readUTF();
    } else {
      this.BlessOfEmpress = null;
    } 
    if (in.readByte() == 1) {
      this.chalkboard = in.readUTF();
    } else {
      this.chalkboard = null;
    } 
    this.clonez = in.readByte();
    this.skillmacro = in.readObject();
    this.lastfametime = in.readLong();
    this.storage = in.readObject();
    this.cs = in.readObject();
    this.mount_itemid = in.readInt();
    this.mount_Fatigue = in.readByte();
    this.mount_level = in.readByte();
    this.mount_exp = in.readInt();
    this.partyid = in.readInt();
    this.messengerid = in.readInt();
    this.inventorys = in.readObject();
    this.fairyExp = in.readByte();
    this.cardStack = in.readByte();
    this.subcategory = in.readByte();
    this.fatigue = in.readShort();
    this.marriageId = in.readInt();
    this.familyid = in.readInt();
    this.seniorid = in.readInt();
    this.junior1 = in.readInt();
    this.junior2 = in.readInt();
    this.currentrep = in.readInt();
    this.totalrep = in.readInt();
    this.battleshipHP = in.readInt();
    this.totalWins = in.readInt();
    this.totalLosses = in.readInt();
    this.anticheat = in.readObject();
    this.tempIP = in.readUTF();
    this.honourexp = in.readInt();
    this.honourlevel = in.readInt();
    this.soulCount = (short)in.readInt();
    this.innerSkills = in.readObject();
    this.pvpExp = in.readInt();
    this.pvpPoints = in.readInt();
    this.itcafetime = in.readInt();
    this.reborns = in.readInt();
    this.apstorage = in.readInt();
    int skillsize = in.readShort();
    for (int j = 0; j < skillsize; j++)
      this.Skills.put(Integer.valueOf(in.readInt()), new SkillEntry(in.readInt(), in.readByte(), in.readLong())); 
    int customsize = in.readByte();
    for (int k = 0; k < customsize; k++)
      this.customValue.put(Integer.valueOf(in.readInt()), Integer.valueOf(in.readInt())); 
    int customsize2 = in.readByte();
    for (int m = 0; m < customsize2; m++)
      this.customInfo.put(Integer.valueOf(in.readInt()), new SkillCustomInfo(in.readInt(), in.readLong())); 
    this.buddysize = in.readByte();
    short addedbuddysize = in.readShort();
    for (int n = 0; n < addedbuddysize; n++)
      this.buddies.put(new CharacterNameAndId(in.readInt(), in.readInt(), in.readUTF(), in.readUTF(), in.readInt(), in.readInt(), in.readUTF(), in.readUTF()), Boolean.valueOf(in.readBoolean())); 
    int questsize = in.readShort();
    for (int i1 = 0; i1 < questsize; i1++)
      this.Quest.put(Integer.valueOf(in.readInt()), in.readObject()); 
    int rzsize = in.readByte();
    for (int i2 = 0; i2 < rzsize; i2++)
      this.reports.put(Byte.valueOf(in.readByte()), Integer.valueOf(in.readInt())); 
    int famesize = in.readByte();
    for (int i3 = 0; i3 < famesize; i3++)
      this.famedcharacters.add(Integer.valueOf(in.readInt())); 
    int battlesize = in.readInt();
    for (int i4 = 0; i4 < battlesize; i4++)
      this.battledaccs.add(Integer.valueOf(in.readInt())); 
    int esize = in.readByte();
    for (int i5 = 0; i5 < esize; i5++)
      this.extendedSlots.add(Integer.valueOf(in.readInt())); 
    int savesize = in.readByte();
    this.savedlocation = new int[savesize];
    for (int i6 = 0; i6 < savesize; i6++)
      this.savedlocation[i6] = in.readInt(); 
    int wsize = in.readByte();
    this.wishlist = new int[wsize];
    for (int i7 = 0; i7 < wsize; i7++)
      this.wishlist[i7] = in.readInt(); 
    int rsize = in.readByte();
    this.rocks = new int[rsize];
    for (int i8 = 0; i8 < rsize; i8++)
      this.rocks[i8] = in.readInt(); 
    int resize = in.readByte();
    this.regrocks = new int[resize];
    for (int i9 = 0; i9 < resize; i9++)
      this.regrocks[i9] = in.readInt(); 
    int hesize = in.readByte();
    this.hyperrocks = new int[resize];
    for (int i10 = 0; i10 < hesize; i10++)
      this.hyperrocks[i10] = in.readInt(); 
    int infosize = in.readShort();
    for (int i11 = 0; i11 < infosize; i11++)
      this.InfoQuest.put(Integer.valueOf(in.readInt()), in.readUTF()); 
    int keysize = in.readInt();
    for (int i12 = 0; i12 < keysize; i12++)
      this.keymap.put(Integer.valueOf(in.readInt()), new Pair<>(Byte.valueOf(in.readByte()), Integer.valueOf(in.readInt()))); 
    int rebsize = in.readShort();
    for (int i14 = 0; i14 < rebsize; i14++)
      this.rebuy.add((Item)in.readObject()); 
    this.imps = new MapleImp[in.readByte()];
    for (int x = 0; x < this.imps.length; x++) {
      if (in.readByte() > 0) {
        MapleImp mapleImp = new MapleImp(in.readInt());
        mapleImp.setFullness(in.readShort());
        mapleImp.setCloseness(in.readShort());
        mapleImp.setState(in.readByte());
        mapleImp.setLevel(in.readByte());
        this.imps[x] = mapleImp;
      } 
    } 
    for (int i13 = 0; i13 < (MapleTrait.MapleTraitType.values()).length; i13++)
      this.traits.put(MapleTrait.MapleTraitType.values()[in.readByte()], Integer.valueOf(in.readInt())); 
    this.TranferTime = System.currentTimeMillis();
  }
  
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(this.characterid);
    out.writeInt(this.accountid);
    out.writeUTF(this.accountname);
    out.writeUTF(this.secondPassword);
    out.writeByte(this.channel);
    out.writeInt(this.nxCredit);
    out.writeInt(0);
    out.writeInt(this.MaplePoints);
    out.writeUTF(this.name);
    out.writeInt(this.fame);
    out.writeByte(this.gender);
    out.writeByte(this.secondgender);
    out.writeShort(this.level);
    out.writeShort(this.str);
    out.writeShort(this.dex);
    out.writeShort(this.int_);
    out.writeShort(this.luk);
    out.writeLong(this.hp);
    out.writeLong(this.mp);
    out.writeLong(this.maxhp);
    out.writeLong(this.maxmp);
    out.writeLong(this.exp);
    out.writeShort(this.hpApUsed);
    out.writeShort(this.remainingAp);
    out.writeByte(this.remainingSp.length);
    int i;
    for (i = 0; i < this.remainingSp.length; i++)
      out.writeInt(this.remainingSp[i]); 
    out.writeLong(this.meso);
    out.writeByte(this.skinColor);
    out.writeByte(this.secondSkinColor);
    out.writeShort(this.job);
    out.writeInt(this.hair);
    out.writeInt(this.secondhair);
    out.writeInt(this.face);
    out.writeInt(this.secondface);
    out.writeInt(this.demonMarking);
    out.writeInt(this.mapid);
    out.writeByte(this.initialSpawnPoint);
    out.writeByte(this.world);
    out.writeInt(this.guildid);
    out.writeByte(this.guildrank);
    out.writeInt(this.guildContribution);
    out.writeByte(this.alliancerank);
    out.writeByte(this.gmLevel);
    out.writeInt(this.points);
    out.writeInt(this.vpoints);
    out.writeByte((this.BlessOfFairy == null) ? 0 : 1);
    if (this.BlessOfFairy != null)
      out.writeUTF(this.BlessOfFairy); 
    out.writeByte((this.BlessOfEmpress == null) ? 0 : 1);
    if (this.BlessOfEmpress != null)
      out.writeUTF(this.BlessOfEmpress); 
    out.writeByte((this.chalkboard == null) ? 0 : 1);
    if (this.chalkboard != null)
      out.writeUTF(this.chalkboard); 
    out.writeByte(this.clonez);
    out.writeObject(this.skillmacro);
    out.writeLong(this.lastfametime);
    out.writeObject(this.storage);
    out.writeObject(this.cs);
    out.writeInt(this.mount_itemid);
    out.writeByte(this.mount_Fatigue);
    out.writeByte(this.mount_level);
    out.writeInt(this.mount_exp);
    out.writeInt(this.partyid);
    out.writeInt(this.messengerid);
    out.writeObject(this.inventorys);
    out.writeByte(this.fairyExp);
    out.writeByte(this.cardStack);
    out.writeByte(this.subcategory);
    out.writeShort(this.fatigue);
    out.writeInt(this.marriageId);
    out.writeInt(this.familyid);
    out.writeInt(this.seniorid);
    out.writeInt(this.junior1);
    out.writeInt(this.junior2);
    out.writeInt(this.currentrep);
    out.writeInt(this.totalrep);
    out.writeInt(this.battleshipHP);
    out.writeInt(this.totalWins);
    out.writeInt(this.totalLosses);
    out.writeObject(this.anticheat);
    out.writeUTF(this.tempIP);
    out.writeInt(this.pvpExp);
    out.writeInt(this.pvpPoints);
    out.writeInt(this.itcafetime);
    out.writeInt(this.reborns);
    out.writeInt(this.apstorage);
    out.writeInt(this.honourexp);
    out.writeInt(this.honourlevel);
    out.writeObject(this.innerSkills);
    out.writeShort(this.Skills.size());
    for (Map.Entry<Integer, SkillEntry> qs : this.Skills.entrySet()) {
      out.writeInt(((Integer)qs.getKey()).intValue());
      out.writeInt(((SkillEntry)qs.getValue()).skillevel);
      out.writeByte(((SkillEntry)qs.getValue()).masterlevel);
      out.writeLong(((SkillEntry)qs.getValue()).expiration);
    } 
    out.writeByte(this.customValue.size());
    for (Map.Entry<Integer, Integer> cv : this.customValue.entrySet()) {
      out.writeInt(((Integer)cv.getKey()).intValue());
      out.writeInt(((Integer)cv.getValue()).intValue());
    } 
    out.writeByte(this.buddysize);
    out.writeShort(this.buddies.size());
    for (Map.Entry<CharacterNameAndId, Boolean> qs : this.buddies.entrySet()) {
      out.writeInt(((CharacterNameAndId)qs.getKey()).getId());
      out.writeUTF(((CharacterNameAndId)qs.getKey()).getName());
      out.writeInt(((CharacterNameAndId)qs.getKey()).getLevel());
      out.writeInt(((CharacterNameAndId)qs.getKey()).getJob());
      out.writeBoolean(((Boolean)qs.getValue()).booleanValue());
      out.writeUTF(((CharacterNameAndId)qs.getKey()).getMemo());
      out.writeUTF(((CharacterNameAndId)qs.getKey()).getGroupName());
    } 
    out.writeShort(this.Quest.size());
    for (Map.Entry<Integer, Object> qs : this.Quest.entrySet()) {
      out.writeInt(((Integer)qs.getKey()).intValue());
      out.writeObject(qs.getValue());
    } 
    out.writeByte(this.reports.size());
    for (Map.Entry<Byte, Integer> ss : this.reports.entrySet()) {
      out.writeByte(((Byte)ss.getKey()).byteValue());
      out.writeInt(((Integer)ss.getValue()).intValue());
    } 
    out.writeByte(this.famedcharacters.size());
    for (Integer zz : this.famedcharacters)
      out.writeInt(zz.intValue()); 
    out.writeInt(this.battledaccs.size());
    for (Integer zz : this.battledaccs)
      out.writeInt(zz.intValue()); 
    out.writeByte(this.extendedSlots.size());
    for (Integer zz : this.extendedSlots)
      out.writeInt(zz.intValue()); 
    out.writeByte(this.savedlocation.length);
    for (int zz : this.savedlocation)
      out.writeInt(zz); 
    out.writeByte(this.wishlist.length);
    for (int zz : this.wishlist)
      out.writeInt(zz); 
    out.writeByte(this.rocks.length);
    for (int zz : this.rocks)
      out.writeInt(zz); 
    out.writeByte(this.regrocks.length);
    for (int zz : this.regrocks)
      out.writeInt(zz); 
    out.writeByte(this.hyperrocks.length);
    for (int zz : this.hyperrocks)
      out.writeInt(zz); 
    out.writeShort(this.InfoQuest.size());
    for (Map.Entry<Integer, String> qs : this.InfoQuest.entrySet()) {
      out.writeInt(((Integer)qs.getKey()).intValue());
      out.writeUTF(qs.getValue());
    } 
    out.writeInt(this.keymap.size());
    for (Map.Entry<Integer, Pair<Byte, Integer>> qs : this.keymap.entrySet()) {
      out.writeInt(((Integer)qs.getKey()).intValue());
      out.writeByte(((Byte)((Pair)qs.getValue()).left).byteValue());
      out.writeInt(((Integer)((Pair)qs.getValue()).right).intValue());
    } 
    out.writeShort(this.rebuy.size());
    for (i = 0; i < this.rebuy.size(); i++)
      out.writeObject(this.rebuy.get(i)); 
    out.writeByte(this.imps.length);
    for (i = 0; i < this.imps.length; i++) {
      if (this.imps[i] != null) {
        out.writeByte(1);
        out.writeInt(this.imps[i].getItemId());
        out.writeShort(this.imps[i].getFullness());
        out.writeShort(this.imps[i].getCloseness());
        out.writeByte(this.imps[i].getState());
        out.writeByte(this.imps[i].getLevel());
      } else {
        out.writeByte(0);
      } 
    } 
    for (Map.Entry<MapleTrait.MapleTraitType, Integer> ts : this.traits.entrySet()) {
      out.writeByte(((MapleTrait.MapleTraitType)ts.getKey()).ordinal());
      out.writeInt(((Integer)ts.getValue()).intValue());
    } 
    out.writeInt(this.soulCount);
  }
}
