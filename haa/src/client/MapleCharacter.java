// 
// Decompiled by Procyon v0.5.36
// 
package client;

import client.custom.inventory.CustomItem;
import handling.Buffstat;
import server.field.boss.MapleBossManager;
import java.util.GregorianCalendar;
import constants.KoreaCalendar;
import server.events.MapleBattleGroundCharacter;
import tools.packet.PacketHelper;
import provider.MapleDataTool;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import server.field.skill.MapleMagicWreck;
import server.maps.FieldLimitType;
import scripting.NPCScriptManager;
import handling.login.LoginServer;
import handling.world.PlayerBuffStorage;
import handling.world.MapleMessengerCharacter;
import server.maps.MapleFoothold;
import java.util.Comparator;
import client.inventory.MapleRing;
import tools.packet.PlayerShopPacket;
import tools.packet.CSPacket;
import handling.world.guild.MapleGuild;
import tools.packet.PetPacket;
import java.sql.Timestamp;
import java.util.Date;
import client.inventory.ItemFlag;
import server.MapleInventoryManipulator;
import tools.StringUtil;
import handling.world.PartyOperation;
import server.MapleItemInformationProvider;
import server.life.MapleLifeFactory;
import server.field.boss.lucid.FieldLucid;
import handling.channel.handler.AttackInfo;
import handling.world.PlayerBuffValueHolder;
import server.field.skill.MapleSecondAtom;
import tools.packet.SkillPacket;
import tools.packet.BattleGroundPacket;
import tools.packet.SLFCGPacket;
import tools.data.MaplePacketLittleEndianWriter;
import server.life.PlayerNPC;
import java.util.concurrent.locks.ReentrantLock;
import database.DatabaseException;
import tools.FileoutputUtil;
import handling.channel.handler.MatrixHandler;
import java.util.Calendar;
import client.inventory.ItemLoader;
import server.ChatEmoticon;
import server.MaplePortal;
import server.maps.MapleMapFactory;
import handling.world.World;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import handling.world.CharacterTransfer;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import database.DatabaseConnection;
import handling.login.LoginInformationProvider;
import java.util.Collections;
import server.maps.SummonMovementType;
import server.life.AffectedOtherSkillInfo;
import client.status.MonsterStatusEffect;
import client.status.MonsterStatus;
import server.maps.MapleMist;
import server.life.MobSkill;
import java.awt.Rectangle;
import server.SecondaryStatEffect;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import server.maps.MapleMapObjectType;
import server.life.MobSkillFactory;
import tools.packet.MobPacket;
import tools.data.LittleEndianAccessor;
import handling.channel.handler.PlayerHandler;
import server.maps.ForceAtom;
import server.Randomizer;
import server.maps.MapleAtom;
import tools.packet.CField;
import constants.GameConstants;
import handling.world.MaplePartyCharacter;
import server.life.Ignition;
import tools.packet.CWvsContext;
import server.maps.SavedLocationType;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.LinkedList;
import client.inventory.MapleInventoryType;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.TimerTask;
import server.MapleChatEmoticon;
import server.MapleSavedEmoticon;
import server.marriage.MarriageMiniBox;
import server.events.MapleTyoonKitchen;
import server.polofritto.FrittoDancing;
import server.polofritto.FrittoEgg;
import server.polofritto.FrittoEagle;
import server.polofritto.BountyHunting;
import server.polofritto.DefenseTowerWave;
import server.quest.party.MapleNettPyramid;
import server.field.skill.SecondAtom;
import java.util.EnumMap;
import client.inventory.MapleInventory;
import scripting.EventInstanceManager;
import handling.world.guild.MapleGuildCharacter;
import server.shops.IMaplePlayerShop;
import handling.world.MapleMessenger;
import client.inventory.MapleMount;
import server.MapleTrade;
import server.MapleStorage;
import server.maps.MapleExtractor;
import server.maps.MapleDragon;
import server.shops.MapleShop;
import server.maps.MapleMap;
import handling.world.MapleParty;
import server.CashShop;
import server.maps.MapleSummon;
import server.SkillCustomInfo;
import tools.Triple;
import server.quest.MapleQuest;
import server.life.MapleHaku;
import client.inventory.MapleAndroid;
import server.maps.MapleMapObject;
import java.util.Set;
import client.inventory.Equip;
import client.inventory.MapleImp;
import client.inventory.Item;
import client.inventory.MaplePet;
import server.maps.MechDoor;
import server.maps.MapleDoor;
import server.movement.LifeMovementFragment;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ScheduledFuture;
import tools.Pair;
import server.field.skill.MapleFieldAttackObj;
import server.maps.MapleMapItem;
import java.util.Map;
import server.life.MapleMonster;
import client.damage.CalcDamage;
import java.util.Timer;
import java.util.List;
import java.awt.Point;
import server.games.MonsterPyramid;
import server.games.ColorInvitationCard;
import server.games.MultiYutGame;
import server.games.OneCardGame;
import server.games.BattleReverse;
import server.games.DetectiveGame;
import server.games.OXQuizGame;
import server.games.BingoGame;
import java.io.Serializable;
import server.MapleDonationSkill;
import server.maps.AnimatedMapleMapObject;

public class MapleCharacter extends AnimatedMapleMapObject implements Serializable {

    private static final long serialVersionUID = 845748950829L;
    private int HowlingGaleCount, YoyoCount, WildGrenadierCount, VerseOfRelicsCount,
            BHGCCount, createDate, RandomPortal, fwolfattackcount, BlockCount, BlockCoin, MesoChairCount, tempmeso, eventcount, duskGauge;
    private long fwolfdamage, LastMovement, PlatformerStageEnter, AggressiveDamage;
    private boolean hasfwolfportal, isfwolfkiller, isWatchingWeb, oneMoreChance, isDuskBlind, eventkillmode;
    private boolean hottimeboss = false, hottimebosslastattack = false, hottimebossattackcheck = false;//월드보스
    private String chairtext;
    private BingoGame BingoInstance;
    private OXQuizGame OXInstance;
    private DetectiveGame DetectiveGameInstance;
    private BattleReverse BattleReverseInstance;
    private OneCardGame oneCardInstance;
    private MultiYutGame multiYutInstance;
    private ColorInvitationCard ColorCardInstance;
    private MonsterPyramid monsterPyramidInstance;
    private List<PlatformerRecord> PfRecords;
    private int blackmagewb;
    private Point Resolution;
    private long rainbowrushStartTime;
    private int RainBowRushTime;
    private boolean israinbow;
    private boolean isnodeadRush;
    public long lastSaveTime, lastReportTime, lastMacroTime;
    public Timer ConstentTimer;
    private int SkillId;
    private int SkillId2;
    private int SkillId3;
    private int SkillId4;
    private int SkillId5;
    private int SkillId6;
    public int 에테르소드, 에테르, 활성화된소드;
    public int 홀리워터, 홀리워터스택, 서펜트스톤, 서펜트스크류;
    public int 발할라검격;
    public int 리인카네이션;
    public int 플레시미라주스택, 스킬카운트;
    public long lastConcentrationTime, lastSilhouetteMirageCreateTime, lastSilhouetteMirageAttackTime, lastVerseOfRelicsTime, lastTimeleapTime, lastDemonicFrenzyTime,
            lastChainArtsFuryTime, lastFireArrowTime, lastThunderTime, lastChairPointTime, lastVamTime, lastAltergoTime, lastButterflyTime, lastUnionRaidTime;
    private String name, chalktext;
    private String BlessOfFairy_Origin, BlessOfEmpress_Origin;
    private String teleportname;
    private long exp, meso, lastCombo, lastfametime, keydown_skill, nextConsume, pqStartTime, lastBerserkTime, lastRecoveryTime, lastSummonTime, mapChangeTime, lastFairyTime, lastExceedTime,
            lastHPTime, lastMPTime, lastDOTTime, monsterComboTime, lastBulletUsedTime, lastCreationTime;
    private byte deathcount, gmLevel, gender, secondgender, initialSpawnPoint, skinColor, secondSkinColor, guildrank, allianceRank, cardStack, wolfscore, sheepscore,
            pandoraBoxFever, world, fairyExp, numClones, subcategory;
    public byte RapidTimeDetect;
    public byte RapidTimeStrength;
    public byte acaneAim;
    private short level;
    private short mulung_energy;
    private short combo;
    private short force;
    private short availableCP;
    private short fatigue;
    private short totalCP;
    private short hpApUsed;
    private short job;
    private short remainingAp;
    private short scrolledPosition;
    private short xenonSurplus;
    private short kaiserCombo;
    private short monsterCombo;
    private short forcingItem;
    private int betaclothes;
    private int zeroCubePosition;
    private int moonGauge;
    private int overloadCount;
    private int exceed;
    private int accountid;
    private int id;
    private transient CalcDamage calcDamage;
    public int batt, clearWeb, forceBlood, fightJazzSkill, nextBlessSkill, empiricalStack, adelResonance, silhouetteMirage, repeatingCrossbowCatridge, dojoCoolTime, dojoStartTime;
    public MapleMonster empiricalKnowledge;
    private long dojoStopTime;
    private boolean deadEffect, noneDestroy;
    private boolean dojoStop;
    private int hair, basecolor, addcolor, baseprob, secondbasecolor, secondaddcolor, secondbaseprob, secondhair, face, secondface, demonMarking, mapid,
            fame, pvpExp, pvpPoints, totalWins, totalLosses, guildid, fallcounter, maplepoints, nxcredit, acash, chair, itemEffect, points, vpoints, itcafetime,
            rank, rankMove, jobRank, jobRankMove, marriageId, marriageItemId, dotHP, honourExp, honorLevel,
            ignitionstack, arcaneAim, listonation, elementalCharge, lastElementalCharge, reinCarnation, transformCooldown;
    private byte poisonStack;
    private byte unityofPower;
    private byte concentration;
    private byte mortalBlow;
    private byte death;
    private byte royalStack;
    private int beholderSkill1;
    private int beholderSkill2;
    private int barrier;
    private int energyBurst;
    private int trinity;
    private int blitzShield;
    private byte infinity;
    private byte holyPountin;
    private byte blessingAnsanble;
    private byte quiverType;
    private byte flip;
    private byte holyMagicShell;
    private byte antiMagicShell;
    private byte blessofDarkness;
    private int currentrep;
    private int dice;
    private int holyPountinOid;
    private int blackMagicAlter;
    private int judgementType;
    private int[] RestArrow;
    private int[] deathCounts;
    private List<Integer> weaponChanges, weaponChanges2, posionNovas, exceptionList;
    public Map<Integer, Integer> weaponChanges1;
    public Map<Integer, Long> CheckAttackTime;
    private List<MapleMapItem> pickPocket;
    private int mparkkillcount, mparkcount, mparkexp, markofPhantom, ultimateDriverCount, markOfPhantomOid, rhoAias, perfusion, spiritGuard;
    private boolean mparkCharged;
    private MapleFieldAttackObj fao;
    public int throwBlasting, striker4thAttack, striker4thSkill, revenant, revenantCount, photonRay, weaponVarietyFinaleStack, weaponVarietyFinale, lawOfGravity;
    public long lastInstallMahaTime, lastRecoverScrollGauge, cooldownforceBlood, cooldownEllision, lastDistotionTime, lastNemeaAttackTime, lastGerionAttackTime, lastBonusAttckTime, lastElementalGhostTime,
            lastDrainAuraTime, lastShardTime, lastPinPointRocketTime, lastDeathAttackTime, lastChargeEnergyTime, lastThrowBlastingTime, lastBlizzardTempestTime, lastRoyalKnightsTime, lastCrystalGateTime, lastSungiAttackTime, lastDisconnectTime;
    public int unstableMemorize, ignoreDraco, lastHowlingGaleObjectId, scrollGauge, shadowBite, curseBound,
            editionalTransitionAttack, lastCardinalForce, cardinalMark, flameDischargeRegen, striker3rdStack, mascotFamilier,
            shadowerDebuff, shadowerDebuffOid, maelstrom, lastPoseType, energy, serpent, blessMark, blessMarkSkill, fightJazz, guidedBullet, graveObjectId;
    public int 렐릭게이지;
    public int 에인션트가이던스;
    public int 문양, Serpent, Serpent2;
    public boolean useChun;
    public boolean useJi;
    public boolean useIn;
    public boolean wingDagger;
    public boolean canUseMortalWingBeat;
    public boolean gagenominus;
    public String guildName;
    private String BattleGrondJobName;
    private Pair<Integer, Integer> recipe;
    private ScheduledFuture<?> PlatformerTimer;
    private ScheduledFuture<?> MesoChairTimer;
    private ScheduledFuture<?> secondaryStatEffectTimer;
    private ScheduledFuture<?> EventSkillTimer;
    public ScheduledFuture<?> revenantTimer;
    private int totalrep;
    private int coconutteam;
    private int followid;
    private int battleshipHP;
    private int challenge;
    private int guildContribution;
    private int lastattendance;
    private int storageNpc;
    private int lastBossId;
    private int TouchedRune;
    private boolean luminusMorph;
    private boolean useBuffFreezer;
    private boolean extremeMode;
    private Point flameHeiz;
    private int lumimorphuse;
    private long lastTouchedRuneTime;
    private Point old;
    private long DotDamage;
    public long lastHowlingGaleTime;
    public long lastYoyoTime;
    public long lastWildGrenadierTime;
    public long lastRandomAttackTime;
    public long lastWeaponVarietyFinaleTime;
    private int[] rocks;
    private int[] savedLocations;
    private int[] regrocks;
    private int[] hyperrocks;
    private int[] remainingSp;
    private int[] wishlist;
    private transient AtomicInteger inst;
    private transient AtomicInteger insd;
    private transient List<LifeMovementFragment> lastres;
    private Map<String, String> keyValues;
    private List<Integer> lastmonthfameids;
    private List<Integer> lastmonthbattleids;
    private List<Integer> extendedSlots;
    private List<Integer> cashwishlist;
    private List<MapleDoor> doors;
    private List<MechDoor> mechDoors;
    public MaplePet[] pets;
    private Item attackitem;
    private List<Item> rebuy;
    private List<InnerSkillValueHolder> innerSkills;
    public List<InnerSkillValueHolder> innerCirculator;
    private MapleImp[] imps;
    private List<Equip> symbol;
    private List<Pair<Integer, Boolean>> stolenSkills;
    private transient List<MapleMonster> controlled;
    private transient Set<MapleMapObject> visibleMapObjects;
    private transient MapleAndroid android;
    private transient MapleHaku haku;
    private Map<MapleQuest, MapleQuestStatus> quests;
    private Map<Integer, String> questinfo;
    private Map<Skill, SkillEntry> skills;
    private List<Triple<Skill, SkillEntry, Integer>> linkskills;
    private transient Map<Integer, Integer> customValue;
    private transient Map<Integer, SkillCustomInfo> customInfo;
    private List<Pair<SecondaryStat, SecondaryStatValueHolder>> effects;
    private transient List<MapleSummon> summons;
    private transient Map<Integer, MapleCoolDownValueHolder> coolDowns;
    private transient Map<SecondaryStat, MapleDiseases> diseases;
    private CashShop cs;
    private BuddyList buddylist;
    private UnionList unions;
    public MapleClient client;
    private int bufftest;
    private transient MapleParty party;
    private PlayerStats stats;
    private transient MapleMap map;
    private transient MapleShop shop;
    private transient MapleDragon dragon;
    private transient MapleExtractor extractor;
    private List<Core> cores;
    private List<VMatrix> matrixs;
    private List<MapleMannequin> hairRoom, faceRoom, skinRoom;
    private MapleStorage storage;
    private transient MapleTrade trade;
    private MapleMount mount;
    private MapleMessenger messenger;
    private transient IMaplePlayerShop playerShop;
    private boolean invincible, canTalk, followinitiator, followon, smega;
    public boolean petLoot;
    public boolean shield;
    private MapleGuildCharacter mgc;
    private transient EventInstanceManager eventInstance;
    private MapleInventory[] inventory;
    private List<AvatarLook> coodination;
    private SkillMacro[] skillMacros;
    private EnumMap<MapleTrait.MapleTraitType, MapleTrait> traits;
    private MapleKeyLayout keylayout;
    public long lastBHGCGiveTime, lastDrainTime, lastFreezeTime, lastHealTime, lastAngelTime, lastInfinityTime, lastCygnusTime, lastSpiritGateTime, lastDriveTime, TriumphTime;
    private transient ScheduledFuture<?> itcafetimer;
    private transient ScheduledFuture<?> diabolicRecoveryTask;
    private transient ScheduledFuture<?> LastTouchedRune;
    public static transient ScheduledFuture<?> XenonSupplyTask;
    public boolean vh;
    List<Integer> allpetbufflist;
    private List<SecondAtom> SaList;
    private transient List<Integer> pendingExpiration;
    private transient Map<Skill, SkillEntry> pendingSkills;
    private transient Map<Integer, Integer> linkMobs;
    private boolean changed_wishlist;
    private boolean changed_trocklocations;
    private boolean changed_regrocklocations;
    private boolean changed_hyperrocklocations;
    private boolean changed_skillmacros;
    private boolean changed_savedlocations;
    private boolean changed_questinfo;
    private boolean changed_skills;
    private boolean changed_reports;
    private boolean changed_extendedSlots;
    private boolean innerskill_changed;
    private boolean fishing;
    private int premiumbuff;
    private long premiumPeriod;
    public long DamageMeter = 0;
    private String premium;
    private int reborns;
    private int apstorage;
    public boolean pvp, isTrade, isCatching, isCatched, isWolfShipWin, isVoting, isDead, isMapiaVote, isDrVote, isPoliceVote;
    private List<Item> auctionitems;
    public String mapiajob;
    public short blackRebirthPos;
    public int voteamount, getmapiavote, getpolicevote, getdrvote, mbating, CrystalCharge, returnSc, peaceMaker;
    public long blackRebirth;
    public Equip blackRebirthScroll, returnscroll, choicepotential;
    public Item memorialcube;
    private int slowAttackCount;
    public boolean isdressup;
    public boolean useBlackJack;
    private int LinkMobCount;
    private int lastCharGuildId;
    private int weddingGiftGive;
    private Point specialChairPoint;
    private boolean useTruthDoor;
    public int nettDifficult;
    private transient MapleNettPyramid NettPyramid;
    private DefenseTowerWave defenseTowerWave;
    private BountyHunting bountyHunting;
    private FrittoEagle frittoEagle;
    private FrittoEgg frittoEgg;
    private FrittoDancing frittoDancing;
    private MapleTyoonKitchen Mtk;
    private MarriageMiniBox mg;
    private int graveTarget;
    public int bullet;
    public int SerenStunGauge, cylindergauge;
    private List<MapleSavedEmoticon> savedEmoticon;
    private List<MapleChatEmoticon> emoticonTabs;
    private List<Triple<Long, Integer, Short>> emoticons;
    private List<Pair<Integer, Short>> emoticonBookMarks;
    private int spAttackCountMobId;
    private int spCount;
    private long spLastValidTime;
    private Pair<Integer, Integer> eqpSpCore;
    boolean dominant;
    public ScheduledFuture<?> rapidtimer1;
    public ScheduledFuture<?> rapidtimer2;
    public int trueSniping, shadowAssault, transformEnergyOrb;
    private long damageMeter;
    private transient ScheduledFuture<?> mapTimeLimitTask;
    public boolean isMegaSmasherCharging;
    public boolean memoraizecheck = false;
    public long megaSmasherChargeStartTime;
    public long lastWindWallTime, lastArrowRain;
    public byte battAttackCount, mCount;
    public boolean energyCharge;
    public int lightning, siphonVitality, armorSplit, PPoint, combination, killingpoint, stackbuff, combinationBuff, BULLET_SKILL_ID,
            SpectorGauge, LinkofArk, FlowofFight, Stigma, bulletParty;
    public int criticalGrowing, criticalDamageGrowing, bodyOfSteal;
    public Timer MulungTimer;
    public TimerTask MulungTimerTask;
    public boolean Lotus;

    private MapleCharacter(final boolean ChannelServer, final boolean firstIngame) {
        
        this.HowlingGaleCount = 0;
        this.YoyoCount = 0;
        this.WildGrenadierCount = 0;
        this.VerseOfRelicsCount = 0;
        this.BHGCCount = 0;
        this.createDate = 0;
        this.RandomPortal = 0;
        this.fwolfattackcount = 0;
        this.BlockCount = 0;
        this.BlockCoin = 0;
        this.MesoChairCount = 0;
        this.tempmeso = 0;
        this.eventcount = 0;
        this.eventkillmode = false;
        this.duskGauge = 0;
        this.fwolfdamage = 0L;
        this.LastMovement = 0L;
        this.PlatformerStageEnter = 0L;
        this.AggressiveDamage = 0L;
        this.Lotus = false;
        this.hasfwolfportal = false;
        this.isfwolfkiller = false;
        this.isWatchingWeb = false;
        this.oneMoreChance = false;
        this.isDuskBlind = false;
        this.BingoInstance = null;
        this.OXInstance = null;
        this.DetectiveGameInstance = null;
        this.BattleReverseInstance = null;
        this.oneCardInstance = null;
        this.multiYutInstance = null;
        this.ColorCardInstance = null;
        this.blackmagewb = 1;
        this.monsterPyramidInstance = null;
        this.Resolution = new Point(0, 0);
        this.rainbowrushStartTime = 0L;
        this.RainBowRushTime = 0;
        this.israinbow = false;
        this.isnodeadRush = false;
        this.PfRecords = new ArrayList<PlatformerRecord>();
        this.lastSaveTime = 0L;
        this.lastReportTime = 0L;
        this.lastMacroTime = System.currentTimeMillis();
        this.에테르소드 = 0;
        this.에테르 = 0;
        this.활성화된소드 = 0;
        this.홀리워터 = 0;
        this.홀리워터스택 = 0;
        this.서펜트스톤 = 0;
        this.서펜트스크류 = 0;
        this.발할라검격 = 12;
        this.리인카네이션 = 0;
        this.플레시미라주스택 = 1;
        this.스킬카운트 = 0;
        this.TriumphTime = 0;
        this.lastVamTime = 0L;
        this.lastAltergoTime = 0L;
        this.lastButterflyTime = 0L;
        this.lastUnionRaidTime = 0L;
        this.lastExceedTime = System.currentTimeMillis();
        this.monsterComboTime = 0L;
        this.lastBulletUsedTime = 0L;
        this.lastCreationTime = 0L;
        this.guildrank = 5;
        this.allianceRank = 5;
        this.RapidTimeDetect = 0;
        this.RapidTimeStrength = 0;
        this.acaneAim = 0;
        this.xenonSurplus = 0;
        this.monsterCombo = 0;
        this.forcingItem = 0;
        this.betaclothes = 0;
        this.zeroCubePosition = 0;
        this.moonGauge = 0;
        this.overloadCount = 0;
        this.exceed = 0;
        this.batt = 0;
        this.clearWeb = 0;
        this.forceBlood = 0;
        this.fightJazzSkill = 0;
        this.nextBlessSkill = 0;
        this.empiricalStack = 0;
        this.adelResonance = 0;
        this.dojoCoolTime = 0;
        this.dojoStartTime = 0;
        this.empiricalKnowledge = null;
        this.dojoStopTime = 0L;
        this.deadEffect = false;
        this.noneDestroy = false;
        this.dojoStop = false;
        this.basecolor = -1;
        this.secondbasecolor = -1;
        this.guildid = 0;
        this.rank = 1;
        this.rankMove = 0;
        this.jobRank = 1;
        this.jobRankMove = 0;
        this.ignitionstack = 0;
        this.arcaneAim = 0;
        this.listonation = 0;
        this.elementalCharge = 0;
        this.lastElementalCharge = 0;
        this.reinCarnation = 0;
        this.transformCooldown = 0;
        this.poisonStack = 0;
        this.unityofPower = 0;
        this.concentration = 0;
        this.mortalBlow = 0;
        this.death = 0;
        this.royalStack = 0;
        this.beholderSkill1 = 0;
        this.beholderSkill2 = 0;
        this.barrier = 0;
        this.energyBurst = 0;
        this.trinity = 0;
        this.blitzShield = 0;
        this.infinity = 1;
        this.holyPountin = 0;
        this.blessingAnsanble = 0;
        this.quiverType = 0;
        this.flip = 0;
        this.holyMagicShell = 0;
        this.antiMagicShell = 0;
        this.blessofDarkness = 0;
        this.dice = 0;
        this.holyPountinOid = 0;
        this.blackMagicAlter = 0;
        this.judgementType = 0;
        this.RestArrow = new int[2];
        this.deathCounts = new int[5];
        this.weaponChanges = new ArrayList<Integer>();
        this.weaponChanges2 = new ArrayList<Integer>();
        this.posionNovas = new ArrayList<Integer>();
        this.exceptionList = new ArrayList<Integer>();
        this.weaponChanges1 = new LinkedHashMap<Integer, Integer>();
        this.CheckAttackTime = new LinkedHashMap<Integer, Long>();
        this.pickPocket = new ArrayList<MapleMapItem>();
        this.mparkkillcount = 0;
        this.mparkcount = 0;
        this.mparkexp = 0;
        this.markofPhantom = 0;
        this.ultimateDriverCount = 0;
        this.markOfPhantomOid = 0;
        this.rhoAias = 0;
        this.perfusion = 0;
        this.spiritGuard = 0;
        this.mparkCharged = false;
        this.fao = null;
        this.throwBlasting = 0;
        this.striker4thAttack = 0;
        this.striker4thSkill = 0;
        this.revenant = 0;
        this.revenantCount = 0;
        this.photonRay = 0;
        this.weaponVarietyFinaleStack = 0;
        this.weaponVarietyFinale = 0;
        this.lawOfGravity = 0;
        this.lastInstallMahaTime = 0L;
        this.lastRecoverScrollGauge = 0L;
        this.cooldownforceBlood = 0L;
        this.cooldownEllision = 0L;
        this.lastDistotionTime = 0L;
        this.lastNemeaAttackTime = 0L;
        this.lastGerionAttackTime = 0L;
        this.lastBonusAttckTime = 0L;
        this.lastElementalGhostTime = 0L;
        this.lastDrainAuraTime = 0L;
        this.lastShardTime = 0L;
        this.lastPinPointRocketTime = 0L;
        this.lastDeathAttackTime = 0L;
        this.lastChargeEnergyTime = 0L;
        this.lastThrowBlastingTime = 0L;
        this.lastBlizzardTempestTime = 0L;
        this.lastRoyalKnightsTime = 0L;
        this.lastCrystalGateTime = 0L;
        this.lastSungiAttackTime = 0L;
        this.lastDisconnectTime = 0L;
        this.unstableMemorize = 0;
        this.ignoreDraco = 0;
        this.lastHowlingGaleObjectId = -1;
        this.scrollGauge = 0;
        this.shadowBite = 0;
        this.curseBound = 0;
        this.editionalTransitionAttack = 0;
        this.lastCardinalForce = 0;
        this.cardinalMark = 0;
        this.flameDischargeRegen = 0;
        this.striker3rdStack = 0;
        this.mascotFamilier = 0;
        this.shadowerDebuff = 0;
        this.shadowerDebuffOid = 0;
        this.maelstrom = 0;
        this.lastPoseType = 0;
        this.energy = 0;
        this.blessMark = 0;
        this.blessMarkSkill = 0;
        this.fightJazz = 0;
        this.guidedBullet = 0;
        this.graveObjectId = 0;
        this.렐릭게이지 = 0;
        this.에인션트가이던스 = 0;
        this.문양 = 0;
        this.Serpent = 0;
        this.Serpent2 = 0;
        this.useChun = false;
        this.useJi = false;
        this.useIn = false;
        this.wingDagger = false;
        this.canUseMortalWingBeat = false;
        this.gagenominus = false;
        this.recipe = new Pair<Integer, Integer>(0, 0);
        this.revenantTimer = null;
        this.guildContribution = 0;
        this.lastattendance = 0;
        this.storageNpc = 0;
        this.lastBossId = 0;
        this.luminusMorph = false;
        this.useBuffFreezer = false;
        this.extremeMode = false;
        this.flameHeiz = null;
        this.lumimorphuse = 500000; // 와드 lumimorphuse
        this.lastTouchedRuneTime = 0L;
        this.DotDamage = 0L;
        this.lastRandomAttackTime = 0L;
        this.remainingSp = new int[10];
        this.wishlist = new int[12];
        this.keyValues = new HashMap<String, String>();
        this.cashwishlist = new ArrayList<Integer>();
        this.pets = new MaplePet[3];
        this.innerCirculator = new ArrayList<InnerSkillValueHolder>();
        this.imps = new MapleImp[3];
        this.stolenSkills = new ArrayList<Pair<Integer, Boolean>>();
        this.questinfo = new ConcurrentHashMap<Integer, String>();
        this.customValue = null;
        this.customInfo = new LinkedHashMap<Integer, SkillCustomInfo>();
        this.coolDowns = new LinkedHashMap<Integer, MapleCoolDownValueHolder>();
        this.bufftest = 0;
        this.cores = new ArrayList<Core>();
        this.matrixs = new ArrayList<VMatrix>();
        this.shield = false;
        this.coodination = new ArrayList<AvatarLook>();
        this.skillMacros = new SkillMacro[5];
        this.keylayout = new MapleKeyLayout();
        this.lastBHGCGiveTime = System.currentTimeMillis();
        this.lastDrainTime = System.currentTimeMillis();
        this.lastFreezeTime = System.currentTimeMillis();
        this.lastHealTime = System.currentTimeMillis();
        this.lastAngelTime = System.currentTimeMillis();
        this.lastInfinityTime = System.currentTimeMillis();
        this.lastCygnusTime = System.currentTimeMillis();
        this.lastSpiritGateTime = System.currentTimeMillis();
        this.lastDriveTime = System.currentTimeMillis();
        this.LastTouchedRune = null;
        this.allpetbufflist = new ArrayList<Integer>(3);
        this.SaList = new ArrayList<SecondAtom>();
        this.pendingExpiration = null;
        this.pendingSkills = null;
        this.innerskill_changed = true;
        this.fishing = false;
        this.premiumbuff = 0;
        this.premiumPeriod = 0L;
        this.premium = "";
        this.pvp = false;
        this.isTrade = false;
        this.isCatching = false;
        this.isCatched = false;
        this.isWolfShipWin = false;
        this.isVoting = false;
        this.isDead = false;
        this.isMapiaVote = false;
        this.isDrVote = false;
        this.isPoliceVote = false;
        this.mapiajob = "";
        this.blackRebirthPos = 0;
        this.voteamount = 0;
        this.getmapiavote = 0;
        this.getpolicevote = 0;
        this.getdrvote = 0;
        this.mbating = 0;
        this.CrystalCharge = 0;
        this.returnSc = 0;
        this.peaceMaker = 0;
        this.blackRebirth = 0L;
        this.choicepotential = null;
        this.returnscroll = null;
        this.blackRebirthScroll = null;
        this.memorialcube = null;
        this.slowAttackCount = 0;
        this.isdressup = false;
        this.useBlackJack = false;
        this.LinkMobCount = 0;
        this.lastCharGuildId = 0;
        this.specialChairPoint = new Point();
        this.useTruthDoor = false;
        this.nettDifficult = 0;
        this.NettPyramid = null;
        this.defenseTowerWave = null;
        this.bountyHunting = null;
        this.frittoEagle = null;
        this.frittoEgg = null;
        this.frittoDancing = null;
        this.Mtk = null;
        this.mg = null;
        this.bullet = 0;
        this.cylindergauge = 0;
        this.SerenStunGauge = 0;
        this.savedEmoticon = new CopyOnWriteArrayList<MapleSavedEmoticon>();
        this.emoticonTabs = new CopyOnWriteArrayList<MapleChatEmoticon>();
        this.emoticons = new CopyOnWriteArrayList<Triple<Long, Integer, Short>>();
        this.emoticonBookMarks = new CopyOnWriteArrayList<Pair<Integer, Short>>();
        this.spAttackCountMobId = 0;
        this.spCount = 0;
        this.spLastValidTime = 0L;
        this.eqpSpCore = null;
        this.dominant = false;
        this.rapidtimer1 = null;
        this.rapidtimer2 = null;
        this.trueSniping = 0;
        this.shadowAssault = 0;
        this.transformEnergyOrb = 0;
        this.isMegaSmasherCharging = false;
        this.megaSmasherChargeStartTime = 0L;
        this.lastWindWallTime = 0L;
        this.lastArrowRain = 0L;
        this.battAttackCount = 0;
        this.criticalGrowing = 0;
        this.criticalDamageGrowing = 0;
        this.bodyOfSteal = 0;
        this.mCount = 0;
        this.energyCharge = false;
        this.lightning = 0;
        this.siphonVitality = 0;
        this.armorSplit = 0;
        this.PPoint = 0;
        this.combination = 0;
        this.killingpoint = 0;
        this.stackbuff = 0;
        this.combinationBuff = 0;
        this.BULLET_SKILL_ID = 0;
        this.SpectorGauge = 0;
        this.LinkofArk = 0;
        this.FlowofFight = 0;
        this.Stigma = 0;
        this.setStance(this.bulletParty = 0);
        this.setPosition(new Point(0, 0));
        this.lastSaveTime = System.currentTimeMillis();
        this.inventory = new MapleInventory[MapleInventoryType.values().length];
        for (final MapleInventoryType type : MapleInventoryType.values()) {
            this.inventory[type.ordinal()] = new MapleInventory(type);
        }
        this.quests = new ConcurrentHashMap<MapleQuest, MapleQuestStatus>();
        this.skills = new ConcurrentHashMap<Skill, SkillEntry>();
        this.linkskills = new ArrayList<Triple<Skill, SkillEntry, Integer>>();
        this.stats = new PlayerStats();
        this.innerSkills = new LinkedList<InnerSkillValueHolder>();
        this.setHairRoom(new ArrayList<MapleMannequin>());
        this.setFaceRoom(new ArrayList<MapleMannequin>());
        this.skinRoom = new ArrayList<MapleMannequin>();
        for (int i = 0; i < this.remainingSp.length; ++i) {
            this.remainingSp[i] = 0;
        }
        this.traits = new EnumMap<MapleTrait.MapleTraitType, MapleTrait>(MapleTrait.MapleTraitType.class);
        for (final MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
            this.traits.put(t, new MapleTrait(t));
        }
        if (ChannelServer) {
            this.changed_reports = false;
            this.changed_skills = false;
            this.changed_wishlist = false;
            this.changed_trocklocations = false;
            this.changed_regrocklocations = false;
            this.changed_hyperrocklocations = false;
            this.changed_skillmacros = false;
            this.changed_savedlocations = false;
            this.changed_extendedSlots = false;
            this.changed_questinfo = false;
            this.canTalk = true;
            this.scrolledPosition = 0;
            this.lastCombo = 0L;
            this.mulung_energy = 0;
            this.combo = 0;
            this.force = 0;
            this.keydown_skill = 0L;
            this.nextConsume = 0L;
            this.pqStartTime = 0L;
            this.fairyExp = 0;
            this.cardStack = 0;
            this.mapChangeTime = 0L;
            this.lastRecoveryTime = 0L;
            this.lastBerserkTime = 0L;
            this.lastFairyTime = 0L;
            this.lastHPTime = 0L;
            this.lastMPTime = 0L;
            this.old = new Point(0, 0);
            this.coconutteam = 0;
            this.followid = 0;
            this.battleshipHP = 0;
            this.marriageItemId = 0;
            this.fallcounter = 0;
            this.challenge = 0;
            this.dotHP = 0;
            this.itcafetime = 0;
            this.lastSummonTime = 0L;
            this.invincible = false;
            this.followinitiator = false;
            this.followon = false;
            this.rebuy = new ArrayList<Item>();
            this.symbol = new ArrayList<Equip>();
            this.setAuctionitems(new ArrayList<Item>());
            this.linkMobs = new HashMap<Integer, Integer>();
            this.teleportname = "";
            this.smega = true;
            this.wishlist = new int[12];
            this.rocks = new int[10];
            this.regrocks = new int[5];
            this.hyperrocks = new int[13];
            this.extendedSlots = new ArrayList<Integer>();
            this.effects = new CopyOnWriteArrayList<Pair<SecondaryStat, SecondaryStatValueHolder>>();
            this.diseases = new ConcurrentHashMap<SecondaryStat, MapleDiseases>();
            this.inst = new AtomicInteger(0);
            this.insd = new AtomicInteger(-1);
            this.doors = new ArrayList<MapleDoor>();
            this.mechDoors = new ArrayList<MechDoor>();
            this.controlled = new CopyOnWriteArrayList<MapleMonster>();
            this.summons = new CopyOnWriteArrayList<MapleSummon>();
            this.visibleMapObjects = new CopyOnWriteArraySet<MapleMapObject>();
            this.savedLocations = new int[SavedLocationType.values().length];
            for (int i = 0; i < SavedLocationType.values().length; ++i) {
                this.savedLocations[i] = -1;
            }
            this.customValue = new HashMap<Integer, Integer>();
            this.deathcount = -1;
            this.secondaryStatEffectTimer = server.Timer.BuffTimer.getInstance().register(new MapleCharacterManagement(), 1000L);
        }
    }

    public void handleAdditionalSkills(long time) {
        MapleSummon summon;
        SecondaryStatEffect eff;
        HashMap<SecondaryStat, Pair<Integer, Integer>> statups;
        HashMap<SecondaryStat, MapleDiseases> remover = new HashMap<SecondaryStat, MapleDiseases>();
        for (Map.Entry<SecondaryStat, MapleDiseases> disease : this.getDiseases().entrySet()) {
            if (System.currentTimeMillis() - disease.getValue().getStartTime() < (long) disease.getValue().getDuration()) {
                continue;
            }
            remover.put(disease.getKey(), disease.getValue());
        }
        if (!remover.isEmpty()) {
            for (Map.Entry<SecondaryStat, MapleDiseases> remove : this.getDiseases().entrySet()) {
                this.cancelDisease(remove.getKey());
            }
        }
        if (this.getSkillLevel(22170074) > 0) {
            SecondaryStatEffect dragonFury = SkillFactory.getSkill(22170074).getEffect(this.getSkillLevel(22170074));
            if (this.getStat().getMPPercent() >= dragonFury.getX() && this.getStat().getMPPercent() <= dragonFury.getY()) {
                if (!this.getBuffedValue(22170074)) {
                    HashMap<SecondaryStat, Pair<Integer, Integer>> statups2 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups2.put(SecondaryStat.IndieMadR, new Pair<Integer, Integer>(Integer.valueOf(dragonFury.getDamage()), 0));
                    this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups2, dragonFury, this));
                    this.getEffects().add(new Pair<SecondaryStat, SecondaryStatValueHolder>(SecondaryStat.IndieMadR, new SecondaryStatValueHolder(dragonFury, -1L, dragonFury.getDamage(), 0, this.getId(), new ArrayList<Pair<Integer, Integer>>(), new ArrayList<Pair<Integer, Integer>>())));
                }
            } else if (this.getBuffedValue(22170074)) {
                this.cancelEffectFromBuffStat(SecondaryStat.IndieMadR, 22170074);
            }
        }
        /*
        if (this.getBuffedValue(5121054) && time - this.lastChargeEnergyTime >= 8000L) {
            if (this.getBuffedEffect(SecondaryStat.EnergyCharged) == null) {
                if (this.getSkillLevel(5120018) > 0) {
                    SkillFactory.getSkill(5120018).getEffect(this.getSkillLevel(5120018)).applyTo(this, false);
                } else if (this.getSkillLevel(5110014) > 0) {
                    SkillFactory.getSkill(5110014).getEffect(this.getSkillLevel(5110014)).applyTo(this, false);
                } else {
                    SkillFactory.getSkill(5100015).getEffect(this.getSkillLevel(5100015)).applyTo(this, false);
                }
            }
            statups = new HashMap();
            SecondaryStatEffect energyCharge = this.getBuffedEffect(SecondaryStat.EnergyCharged);
            int max = energyCharge.getZ();
            this.lastChargeEnergyTime = time;
            this.energy = Math.min(max, this.energy + this.getBuffedEffect(5121054).getX());
            if (this.energy == max && !this.energyCharge) {
                this.energyCharge = true;
            }
            statups.put(SecondaryStat.EnergyCharged, new Pair<Integer, Integer>(this.energy, 0));
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, this.getBuffedEffect(SecondaryStat.EnergyCharged), this));
            this.getMap().broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(this, statups, this.getBuffedEffect(SecondaryStat.EnergyCharged)), false);
        }
         */

        if (this.getBuffedValue(400001043) && time - this.lastCygnusTime >= 4000L) {
            eff = this.getBuffedEffect(400001043);
            int value = this.getBuffedValue(SecondaryStat.IndieDamR, 400001043);
            if (value < eff.getW()) {
                this.setBuffedValue(SecondaryStat.Infinity, 400001043, value + eff.getDamage());
                this.addHP(this.getStat().getMaxHp() * (long) eff.getY() / 100L);
                this.lastCygnusTime = System.currentTimeMillis();
                HashMap<SecondaryStat, Pair<Integer, Integer>> statups3 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                statups3.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(value + eff.getDamage(), (int) this.getBuffLimit(400001043)));
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups3, eff, this));
            }
        }
        if (this.getBuffedValue(400001044) && time - this.lastCygnusTime >= 4000L) {
            eff = this.getBuffedEffect(400001044);
            int value = this.getBuffedValue(SecondaryStat.IndieDamR, 400001044);
            if (value < eff.getW()) {
                this.setBuffedValue(SecondaryStat.IndieDamR, 400001044, value + eff.getDamage());
                this.addHP(this.getStat().getMaxHp() * (long) eff.getY() / 100L);
                this.lastCygnusTime = System.currentTimeMillis();
                HashMap<SecondaryStat, Pair<Integer, Integer>> statups4 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                statups4.put(SecondaryStat.IndieDamR, new Pair<Integer, Integer>(value + eff.getDamage(), (int) this.getBuffLimit(400001044)));
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups4, eff, this));
            }
        }
        if (!this.getBuffedValue(33110014) && this.getSkillLevel(33110014) > 0) {
            SkillFactory.getSkill(33110014).getEffect(this.getSkillLevel(33110014)).applyTo(this, true);
        }
        if (this.getSkillLevel(2100009) > 0) {
            byte stack = 0;
            SecondaryStatEffect drain = SkillFactory.getSkill(2100009).getEffect(1);
            for (MapleMonster mob : this.map.getAllMonstersThreadsafe()) {
                for (Ignition zz : mob.getIgnitions()) {
                    if (zz.getOwnerId() == this.getId() && stack < 5) {
                        stack = (byte) (stack + 1);
                    }
                    if (stack != 5) {
                        continue;
                    }
                    break;
                }
                if (stack != 5) {
                    continue;
                }
                break;
            }
            this.setPoisonStack(stack);
            HashMap<SecondaryStat, Pair<Integer, Integer>> statups5 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
            statups5.put(SecondaryStat.DotBasedBuff, new Pair<Integer, Integer>(Integer.valueOf(stack), 0));
            drain.setDuration(0);
            if (stack > 0) {
                this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups5, drain, this));
            } else {
                this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(statups5, this));
            }
        }
        if (this.getSkillLevel(2300009) > 0) {
            int size = 0;
            if (this.getParty() != null) {
                for (MaplePartyCharacter chr1 : this.getParty().getMembers()) {
                    MapleCharacter curChar = this.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr1.getId());
                    if (curChar == null || !chr1.isOnline() || curChar.getId() == this.getId() || !curChar.getBuffedValue(2321005) && !curChar.getBuffedValue(2301004) && !curChar.getBuffedValue(2311003) && !curChar.getBuffedValue(2311009) && !curChar.getBuffedValue(2311009)) {
                        continue;
                    }
                    ++size;
                }
            }
            if (this.getBuffedValue(2321005) || this.getBuffedValue(2301004) || this.getBuffedValue(2311003) || this.getBuffedValue(2311009) || this.getBuffedValue(2311009)) {
                ++size;
            }
            int x = SkillFactory.getSkill(2300009).getEffect(1).getX();
            if (this.getSkillLevel(2320013) > 0) {
                x = SkillFactory.getSkill(2320013).getEffect(1).getX();
            }
            if (this.getBuffedEffect(SecondaryStat.BlessingAnsanble) == null) {
                if (size > 0) {
                    this.setSkillCustomInfo(2320013, size * x, 0L);
                    SkillFactory.getSkill(2300009).getEffect(1).applyTo(this);
                }
            } else if (this.getBuffedValue(SecondaryStat.BlessingAnsanble) != size * x) {
                this.setSkillCustomInfo(2320013, size * x, 0L);
                if (this.getBuffedEffect(SecondaryStat.BlessingAnsanble) != null) {
                    this.cancelEffect(this.getBuffedEffect(SecondaryStat.BlessingAnsanble));
                }
                if (size > 0) {
                    SkillFactory.getSkill(2300009).getEffect(1).applyTo(this);
                }
            }
        }
        if (this.getBuffedValue(51111008)) {
            if (this.getBuffedOwner(51111008) == this.getId()) {
                short needmp = this.getBuffedEffect(51111008).getMPCon();
                if (this.getStat().getMp() > (long) needmp) {
                    this.addMP(-this.getBuffedEffect(51111008).getMPCon());
                    this.PartyBuffCheck(SecondaryStat.MichaelSoulLink, 51111008);
                } else {
                    this.cancelEffect(this.getBuffedEffect(51111008));
                }
            } else {
                MapleCharacter owner = this.getMap().getCharacter(this.getBuffedOwner(51111008));
                if (owner == null) {
                    this.cancelEffect(this.getBuffedEffect(51111008));
                } else if (!owner.getBuffedValue(51111008)) {
                    this.cancelEffect(this.getBuffedEffect(51111008));
                }
            }
        }
        if (GameConstants.isMichael(this.getJob()) && this.getBuffedValue(400011011) && this.getRhoAias() > 0 && this.getParty() != null) {
            SecondaryStatEffect effect = this.getBuffedEffect(SecondaryStat.RhoAias);
            HashMap statups6 = new HashMap();
            if (effect != null) {
                for (MaplePartyCharacter pc : this.getParty().getMembers()) {
                    MapleCharacter victim;
                    if (!pc.isOnline() || pc.getMapid() != this.getMapId() || pc.getChannel() != this.getClient().getChannel() || (victim = this.getClient().getChannelServer().getPlayerStorage().getCharacterByName(pc.getName())) == null) {
                        continue;
                    }
                    if (this.getTruePosition().x + effect.getLt().x < victim.getTruePosition().x && this.getTruePosition().x - effect.getLt().x > victim.getTruePosition().x && this.getTruePosition().y + effect.getLt().y < victim.getTruePosition().y && this.getTruePosition().y - effect.getLt().y > victim.getTruePosition().y) {
                        if (victim.getBuffedValue(400011011)) {
                            continue;
                        }
                        long duration = this.getBuffLimit(400011011);
                        effect.applyTo(this, victim, false, this.getPosition(), (int) duration, (byte) 0, false);
                        continue;
                    }
                    if (!victim.getBuffedValue(400011011)) {
                        continue;
                    }
                    victim.cancelEffect(victim.getBuffedEffect(400011011));
                }
            }
        }
        if (this.getBuffedValue(400011127) && this.getBuffedValue(SecondaryStat.IndieBarrier) != null) {
            int shiled = (int) this.getSkillCustomValue0(400011127);
            long du = this.getBuffLimit(SecondaryStat.IndieBarrier, 400011127);
            this.setSkillCustomInfo(400011127, this.getSkillCustomValue0(400011127) - (long) (shiled / 100 * 7), 0L);
            this.getBuffedEffect(400011127).applyTo(this, false, (int) du);
        }
        if (this.getSkillCustomValue(15003) == null) {
            this.에테르핸들러(this, 5, 0, false);
            this.setSkillCustomInfo(15003, 0L, 10000L);
        }
        if (this.getBuffedValue(151111005) && this.getParty() != null) {
            SecondaryStatEffect effect = this.getBuffedEffect(SecondaryStat.Novility);
            for (MaplePartyCharacter pc : this.getParty().getMembers()) {
                MapleCharacter victim;
                if (!pc.isOnline() || pc.getMapid() != this.getMapId() || pc.getChannel() != this.getClient().getChannel() || (victim = this.getClient().getChannelServer().getPlayerStorage().getCharacterByName(pc.getName())) == null) {
                    continue;
                }
                if (this.getTruePosition().x + effect.getLt().x < victim.getTruePosition().x && this.getTruePosition().x - effect.getLt().x > victim.getTruePosition().x && this.getTruePosition().y + effect.getLt().y < victim.getTruePosition().y && this.getTruePosition().y - effect.getLt().y > victim.getTruePosition().y) {
                    if (victim.getBuffedValue(151111005)) {
                        continue;
                    }
                    long duration = this.getBuffLimit(151111005);
                    effect.applyTo(this, victim, false, this.getPosition(), (int) duration, (byte) 0, false);
                    continue;
                }
                if (!victim.getBuffedValue(151111005)) {
                    continue;
                }
                victim.cancelEffect(victim.getBuffedEffect(151111005));
            }
        }
        if (GameConstants.isAdel(this.getJob())) {
            if (this.getSkillCustomValue0(151121004) > 0L) {
                this.addSkillCustomInfo(151121004, -1L);
            }
        } else if (GameConstants.isLara(this.getJob()) && this.getSkillCustomValue0(162121022) > 0L) {
            this.addSkillCustomInfo(162121022, -1L);
        }
        if (this.getBuffedEffect(SecondaryStat.IndieReduceCooltime) != null) {
            for (MapleCoolDownValueHolder cooldown : this.getCooldowns()) {
                if (SkillFactory.getSkill(cooldown.skillId).isNotCooltimeReset()) {
                    continue;
                }
                this.changeCooldown(cooldown.skillId, -this.getBuffedValue(SecondaryStat.IndieReduceCooltime).intValue() * 100);
            }
        }
        if (this.getBuffedEffect(SecondaryStat.DotHealHPPerSecond) != null) {
            this.addHP(this.getStat().getCurrentMaxHp() / 100L * (long) this.getBuffedValue(SecondaryStat.DotHealHPPerSecond).intValue());
        }
        if (this.getBuffedEffect(SecondaryStat.DotHealMPPerSecond) != null) {
            this.addMP(this.getStat().getCurrentMaxMp(this) / 100L * (long) this.getBuffedValue(SecondaryStat.DotHealMPPerSecond).intValue());
        }
        if (this.getBuffedValue(152101000) && (summon = this.getSummon(152101000)) != null) {
            this.client.send(CField.SummonPacket.ElementalRadiance(summon, 3));
        }
        if (this.getBuffedValue(152111003) && this.getSkillLevel(152120008) > 0 && this.getSkillCustomValue(152111003) == null) {
            ArrayList<Integer> monsters = new ArrayList<Integer>();
            MapleAtom atom = new MapleAtom(false, this.getId(), 40, false, 152120008, this.getTruePosition().x, this.getTruePosition().y);
            SecondaryStatEffect effect = SkillFactory.getSkill(152120008).getEffect(this.getSkillLevel(152120008));
            int givebuffsize = 0;
            if (this.getParty() != null) {
                for (MaplePartyCharacter pc : this.getParty().getMembers()) {
                    MapleCharacter victim;
                    if (!pc.isOnline() || pc.getMapid() != this.getMapId() || pc.getChannel() != this.getClient().getChannel() || (victim = this.getClient().getChannelServer().getPlayerStorage().getCharacterByName(pc.getName())) == null || victim.getId() == this.getId() || this.getTruePosition().x + effect.getLt().x >= victim.getTruePosition().x || this.getTruePosition().x - effect.getLt().x <= victim.getTruePosition().x || this.getTruePosition().y + effect.getLt().y >= victim.getTruePosition().y || this.getTruePosition().y - effect.getLt().y <= victim.getTruePosition().y) {
                        continue;
                    }
                    ForceAtom forceAtom = new ForceAtom(1, Randomizer.rand(45, 49), Randomizer.rand(5, 6), Randomizer.rand(54, 184), 0);
                    monsters.add(victim.getId());
                    atom.addForceAtom(forceAtom);
                    ++givebuffsize;
                }
                if (!atom.getForceAtoms().isEmpty()) {
                    atom.setDwTargets(monsters);
                    this.getMap().spawnMapleAtom(atom);
                }
            }
            monsters = new ArrayList();
            atom = new MapleAtom(false, this.getId(), 40, true, 152120008, this.getTruePosition().x, this.getTruePosition().y);
            for (MapleMonster monster : this.getMap().getAllMonster()) {
                if (this.getTruePosition().x + effect.getLt().x >= monster.getTruePosition().x || this.getTruePosition().x - effect.getLt().x <= monster.getTruePosition().x || this.getTruePosition().y + effect.getLt().y >= monster.getTruePosition().y || this.getTruePosition().y - effect.getLt().y <= monster.getTruePosition().y) {
                    continue;
                }
                ForceAtom forceAtom = new ForceAtom(1, Randomizer.rand(45, 49), Randomizer.rand(5, 6), Randomizer.rand(54, 184), 0);
                monsters.add(monster.getObjectId());
                atom.addForceAtom(forceAtom);
                if (++givebuffsize < effect.getMobCount()) {
                    continue;
                }
                break;
            }
            if (!atom.getForceAtoms().isEmpty()) {
                atom.setDwTargets(monsters);
                this.getMap().spawnMapleAtom(atom);
            }
            this.setSkillCustomInfo(152111003, 0L, 2000L);
        }
        if (GameConstants.isKadena(this.getJob()) && this.getSkillLevel(400041074) > 0 && this.getSkillCustomValue(400441774) == null) {
            SecondaryStatEffect effect = SkillFactory.getSkill(400041074).getEffect(this.getSkillLevel(400041074));
            PlayerHandler.Vmatrixstackbuff(this.getClient(), false, null);
            this.setSkillCustomInfo(400441774, 0L, effect.getX() * 1000);
        }
        if (GameConstants.isAngelicBuster(this.getJob())) {
            if (this.getBuffedValue(400051046) && this.getSkillCustomValue(400151046) == null) {
                SecondaryStatEffect effect = this.getBuffedEffect(400051046);
                MapleSummon summon2 = this.getSummon(400051046);
                if (summon2 != null) {
                    if (!summon2.isSpecialSkill() && summon2.getEnergy() < 8) {
                        summon2.setEnergy(summon2.getEnergy() + 1);
                        this.getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon2, 2));
                        this.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon2, 2));
                    }
                    this.setSkillCustomInfo(400151046, 0L, effect.getU() * 1000);
                }
            }
        } else if (GameConstants.isZero(this.getJob())) {
            if (this.getSkillCustomValue0(101000201) > 0L) {
                int a = this.getSkillLevel(80000406) > 0 ? this.getSkillLevel(80000406) : 0;
                int maxTf = (this.getGender() == 1 ? 100 : 100) + a * 10 + (this.getGender() == 1 && this.getSkillLevel(101100203) > 0 ? 30 : 0);
                this.setSkillCustomInfo(101000201, this.getSkillCustomValue0(101000201) + 20L, 0L);
                if (this.getSkillCustomValue0(101000201) > (long) maxTf) {
                    this.setSkillCustomInfo(101000201, maxTf, 0L);
                }
            }
            if (this.getSkillCustomValue(1011135) == null) {
                if (this.getSkillCustomValue0(101112) > 0L && this.getGender() == 0 && this.getSkillCustomValue0(101112) < this.getStat().getCurrentMaxHp()) {
                    int hp = (int) (this.getSkillCustomValue0(101114) / 100L * 16L);
                    this.setSkillCustomInfo(101112, this.getSkillCustomValue0(101112) + (long) hp, 0L);
                    if (this.getSkillCustomValue0(101112) >= this.getStat().getCurrentMaxHp()) {
                        this.setSkillCustomInfo(101112, (int) this.getStat().getCurrentMaxHp(), 0L);
                    }
                    this.getClient().send(CField.ZeroTag(this, this.getGender(), (int) this.getSkillCustomValue0(101112), (int) this.getSkillCustomValue0(101115)));
                }
                if (this.getSkillCustomValue0(101113) > 0L && this.getGender() == 1 && this.getSkillCustomValue0(101113) < this.getStat().getCurrentMaxHp()) {
                    int hp = (int) (this.getSkillCustomValue0(101115) / 100L * 16L);
                    this.setSkillCustomInfo(101113, this.getSkillCustomValue0(101113) + (long) hp, 0L);
                    if (this.getSkillCustomValue0(101113) >= this.getStat().getCurrentMaxHp()) {
                        this.setSkillCustomInfo(101113, (int) this.getStat().getCurrentMaxHp(), 0L);
                    }
                    this.getClient().send(CField.ZeroTag(this, this.getGender(), (int) this.getSkillCustomValue0(101113), (int) this.getSkillCustomValue0(101114)));
                }
                this.setSkillCustomInfo(1011135, 0L, 4000L);
            }
        }
        if (this.getSkillCustomValue0(24209) > 0L && (this.getMapId() == 450008950 || this.getMapId() == 450008350)) {
            this.getMap().broadcastMessage(MobPacket.BossWill.AttackPoison(this, (int) this.getSkillCustomValue0(24219), 0, 0, 0));
            for (MapleCharacter achr : this.getMap().getAllChracater()) {
                if (this.getPosition().x - 150 >= achr.getPosition().x || this.getPosition().x + 150 <= achr.getPosition().x || achr.getId() == this.getId() || !achr.isAlive() || achr.getBuffedEffect(SecondaryStat.NotDamaged) != null || achr.getBuffedEffect(SecondaryStat.IndieNotDamaged) != null) {
                    continue;
                }
                achr.addHP(-(achr.getStat().getCurrentMaxHp() / 100L) * 44L);
                achr.getMap().broadcastMessage(CField.playerDamaged(achr.getId(), (int) (achr.getStat().getCurrentMaxHp() / 100L) * 44));
            }
        }
        if (this.getBuffedValue(2311003)) {
            boolean active = this.getSkillCustomValue0(2311004) == 1L;
            boolean givebuff = false;
            int ownerid = (int) this.getSkillCustomValue0(2311003);
            if (ownerid != this.getId()) {
                MapleCharacter owner = this.getMap().getCharacterById(ownerid);
                SecondaryStatEffect effect = SkillFactory.getSkill(2311003).getEffect(20);
                if (owner != null && owner.getParty().getId() == this.getParty().getId()) {
                    Rectangle box = this.getBuffedEffect(2311003).calculateBoundingBox(owner.getPosition(), owner.isFacingLeft());
                    if (!(owner.getMapId() == this.getMapId() && box.contains(this.getPosition()) || this.getSkillCustomValue0(2311004) != 0L)) {
                        givebuff = true;
                        this.setSkillCustomInfo(2311004, 1L, 0L);
                        this.dropMessage(5, "홀리 심볼을 시전한 캐릭터의 근처를 벗어났습니다.");
                    } else if (owner.getMapId() == this.getMapId() && box.contains(this.getPosition()) && this.getSkillCustomValue0(2311004) == 1L) {
                        this.dropMessage(5, "홀리 심볼을 시전한 캐릭터의 근처를 들어왔습니다.");
                        this.setSkillCustomInfo(2311004, 0L, 0L);
                        givebuff = true;
                    }
                } else if ((this.getParty() == null || owner == null) && this.getSkillCustomValue0(2311004) == 0L) {
                    givebuff = true;
                    this.setSkillCustomInfo(2311004, 1L, 0L);
                    this.dropMessage(5, "홀리 심볼을 시전한 캐릭터 근처를 벗어났습니다.");
                }
                if (givebuff) {
                    int du = (int) this.getBuffLimit(2311003);
                    if (owner == null) {
                        effect.applyTo(this, this, this.getSkillCustomValue0(2311004) == 0L, this.getPosition(), du, (byte) 0, false);
                    } else {
                        effect.applyTo(owner, this, this.getSkillCustomValue0(2311004) == 0L, this.getPosition(), du, (byte) 0, false);
                    }
                }
            }
        }
        if (this.getBuffedValue(400011021)) {
            MapleCharacter owner = this.getMap().getCharacterById(this.getBuffedOwner(400011021));
            boolean givebuff = false;
            SecondaryStatEffect effect = SkillFactory.getSkill(400011003).getEffect(owner.getSkillLevel(400011003));
            if (owner != null && owner.getParty().getId() == this.getParty().getId()) {
                if (owner.getMapId() != this.getMapId() || owner.getTruePosition().x + effect.getLt().x >= this.getTruePosition().x || owner.getTruePosition().x - effect.getLt().x <= this.getTruePosition().x) {
                    givebuff = true;
                }
            } else if (this.getParty() == null || owner == null) {
                givebuff = true;
            }
            if (givebuff) {
                owner.removeSkillCustomInfo(400011003);
                HashMap<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                localstatups.clear();
                localstatups.put(SecondaryStat.HolyUnity, new Pair<Integer, Integer>(0, (int) owner.getBuffLimit(400011003)));
                owner.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, effect, owner));
                while (this.getBuffedValue(400011021)) {
                    this.cancelEffect(this.getBuffedEffect(400011021));
                }
            }
        }
        if (this.getBuffedValue(400011003) && this.getParty() != null) {
            boolean connect;
            boolean bl = connect = this.getSkillCustomValue0(400011003) > 0L;
            if (!connect) {
                this.ReHolyUnityBuff(this.getBuffedEffect(400011003));
            }
        }
        if (this.getBuffedValue(31121054) && this.getBuffedValue(31121007) && this.getSkillCustomValue(31121007) == null && this.skillisCooling(31121054)) {
            this.changeCooldown(31121054, -2000);
            this.setSkillCustomInfo(31121007, 0L, 3000L);
        }
        if (this.getBuffedValue(400011123) && this.getSkillCustomValue(400011123) == null) {
            ArrayList<Triple<Integer, Integer, Integer>> list = new ArrayList<Triple<Integer, Integer, Integer>>();
            SecondaryStatEffect effect = SkillFactory.getSkill(400011121).getEffect(this.getSkillLevel(400011121));
            for (MapleMonster monster : this.getMap().getAllMonster()) {
                if (this.getTruePosition().x + effect.getLt().x >= monster.getTruePosition().x || this.getTruePosition().x - effect.getLt().x <= monster.getTruePosition().x || this.getTruePosition().y + effect.getLt().y >= monster.getTruePosition().y || this.getTruePosition().y - effect.getLt().y <= monster.getTruePosition().y) {
                    continue;
                }
                int size = (int) monster.getCustomValue0(400011121);
                if (size < 6) {
                    monster.setCustomInfo(400011121, size + 1, 0);
                }
                if (monster.getCustomValue(400011122) == null) {
                    monster.setCustomInfo(400011122, 0, 10000);
                }
                list.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), (int) monster.getCustomValue0(400011121), monster.getCustomTime(400011122)));
            }
            if (!list.isEmpty()) {
                this.setSkillCustomInfo(400011123, 0L, 3000L);
                this.getMap().broadcastMessage(CField.getBlizzardTempest(list));
            }
        }
        if (this.Stigma >= 7 && this.getBuffedValue(SecondaryStat.NotDamaged) == null && this.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
            for (MapleMonster monster : this.getMap().getAllMonster()) {
                if (monster.getId() != 8880100 && monster.getId() != 8880110 && monster.getId() != 8880101 && monster.getId() != 8880111) {
                    continue;
                }
                MobSkill ms = MobSkillFactory.getMobSkill(237, 1);
                ms.applyEffect(this, monster, true, monster.isFacingLeft());
                break;
            }
        }
        if (this.getBuffedValue(SecondaryStat.RandAreaAttack) != null && this.getSkillCustomValue(80001762) == null) {
            List<MapleMapObject> objs = this.getMap().getMapObjectsInRange(this.getTruePosition(), 100000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
            if (objs != null && objs.size() >= 1) {
                MapleMonster mob__ = this.getMap().getAllMonster().get(Randomizer.rand(0, objs.size() - 1));
                this.getMap().broadcastMessage(CField.thunderAttack(mob__.getPosition().x, mob__.getPosition().y, mob__.getId()));
                this.setSkillCustomInfo(80001762, 0L, 10000L);
            }
        }
        if (GameConstants.isWildHunter(this.getJob())) {
            if (this.getSkillLevel(400031032) > 0 && this.getSkillCustomValue(400031132) == null) {
                PlayerHandler.Vmatrixstackbuff(this.getClient(), false, null);
                this.setSkillCustomInfo(400031132, 0L, 4500L);
            }
        } else if (GameConstants.isXenon(this.getJob()) && this.getSkillCustomValue(30020232) == null) {
            short maxSupply;
            short s = this.level >= 100 ? (short) 20 : (this.level >= 60 ? (short) 15 : (maxSupply = this.level >= 30 ? (short) 10 : 5));
            if (this.getBuffedValue(SecondaryStat.Overload) != null) {
                maxSupply = 40;
            }
            if (s > this.getXenonSurplus()) {
                this.gainXenonSurplus((short) 1, SkillFactory.getSkill(30020232));
            }
            if (this.getBuffedValue(SecondaryStat.Overload) == null) {
                this.setSkillCustomInfo(30020232, 0L, 4000L);
            } else {
                this.setSkillCustomInfo(30020232, 0L, 2000L);
            }
        } else if (this.getBuffedValue(37000006) && this.getSkillCustomValue(37000007) == null) {
            int 감소량 = (int) (this.getSkillCustomValue0(37000006) / 100L * (long) this.getBuffedEffect(37000006).getY() + (long) this.getBuffedEffect(37000006).getZ());
            this.setSkillCustomInfo(37000006, this.getSkillCustomValue0(37000006) - (long) 감소량, 0L);
            this.setSkillCustomInfo(37000007, 0L, 3000L);
            if (this.getSkillCustomValue0(37000006) <= (long) 감소량) {
                this.cancelEffectFromBuffStat(SecondaryStat.RwBarrier, 37000006);
                this.removeSkillCustomInfo(37000006);
            } else {
                HashMap<SecondaryStat, Pair<Integer, Integer>> statups7 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                statups7.put(SecondaryStat.RwBarrier, new Pair<Integer, Integer>((int) this.getSkillCustomValue0(37000006), 0));
                this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups7, this.getBuffedEffect(37000006), this.getPlayer()));
            }
            if (this.getSkillLevel(37120049) > 0) {
                this.addHP(감소량 / 2);
            }
        }
        if ((this.getSkillLevel(4200013) > 0 || this.getSkillLevel(422015) > 0) && this.getSkillCustomValue(4220015) == null) {
            SecondaryStatEffect criticalGrowing2 = SkillFactory.getSkill(4200013).getEffect(this.getSkillLevel(4200013));
            if (this.getSkillLevel(4220015) > 0) {
                criticalGrowing2 = SkillFactory.getSkill(4220015).getEffect(this.getSkillLevel(4220015));
            }
            this.criticalGrowing += criticalGrowing2.getX();
            if (this.criticalGrowing + this.getStat().critical_rate >= 100) {
                this.criticalGrowing = 100;
            }
            this.criticalDamageGrowing = Math.min(this.criticalDamageGrowing + criticalGrowing2.getW(), criticalGrowing2.getQ());
            criticalGrowing2.applyTo(this, false, 0);
            if (this.criticalGrowing + this.getStat().critical_rate >= 100 && this.criticalDamageGrowing >= criticalGrowing2.getQ()) {
                this.criticalGrowing = 0;
                this.criticalDamageGrowing = 0;
            }
            this.setSkillCustomInfo(4220015, 0L, 4000L);
        }
        if (GameConstants.isCain(this.getJob())) {
            if (this.getJob() >= 6310 && this.getSkillCustomValue(6310) == null) {
                this.handlePossession(1);
                this.setSkillCustomInfo(6310, 0L, 5000L);
            }
            ArrayList<Integer> stackskill = new ArrayList<Integer>(Arrays.asList(63101004, 63111003, 63121002, 63121040));
            for (Integer skillid : stackskill) {
                if (this.getSkillLevel(skillid) <= 0) {
                    continue;
                }
                this.handleStackskill(skillid, false);
            }
        } else if (GameConstants.isLara(this.getJob())) {
            if (this.getSkillLevel(162101012) > 0) {
                SecondaryStatEffect effect = SkillFactory.getSkill(162101012).getEffect(this.getSkillLevel(162101012));
                if (this.getSkillCustomValue(162101112) == null) {
                    if (this.getSkillCustomValue0(162101012) < (long) effect.getW2()) {
                        this.addSkillCustomInfo(162101012, 1L);
                        this.setSkillCustomInfo(162101112, 0L, effect.getZ() * 1000);
                    }
                    HashMap<SecondaryStat, Pair<Integer, Integer>> statups8 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups8.put(SecondaryStat.산의씨앗, new Pair<Integer, Integer>((int) this.getSkillCustomValue0(162101012), 0));
                    this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups8, effect, this));
                }
            }
            if (this.getSkillLevel(162121042) > 0) {
                SecondaryStatEffect effect = SkillFactory.getSkill(162121042).getEffect(this.getSkillLevel(162121042));
                if (this.getSkillCustomValue(162121142) == null) {
                    if (this.getSkillCustomValue0(162121042) < (long) effect.getU()) {
                        this.addSkillCustomInfo(162121042, 1L);
                        this.setSkillCustomInfo(162121142, 0L, effect.getW() * 1000);
                    }
                    HashMap<SecondaryStat, Pair<Integer, Integer>> statups9 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups9.put(SecondaryStat.자유로운용맥, new Pair<Integer, Integer>((int) this.getSkillCustomValue0(162121042), 0));
                    this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups9, effect, this));
                }
            }
        }
    }

    public void checkMistStatus(final MapleMist mist, final List<MapleMonster> arrays, final long time) {
        if (mist.getSource() != null) {
            switch (mist.getSource().getSourceId()) {
                case 36121007: {
                    for (final MapleCharacter chr : this.getMap().getAllChracater()) {
                        if (mist.getBox().contains(chr.getTruePosition())) {
                            if (mist.getOwner().getId() == chr.getId()) {
                                for (final MapleCoolDownValueHolder cooldown : chr.getCooldowns()) {
                                    if (cooldown.skillId != 36121007 && !SkillFactory.getSkill(cooldown.skillId).isHyper() && cooldown.skillId < 400000000) {
                                        chr.changeCooldown(cooldown.skillId, -2000);
                                    }
                                }
                            } else {
                                if (mist.getOwner().getParty() == null || mist.getOwner().getParty().getMemberById(chr.getId()) == null || chr.getBuffedValue(4221006)) {
                                    continue;
                                }
                                for (final MapleCoolDownValueHolder cooldown : chr.getCooldowns()) {
                                    if (cooldown.skillId != 36121007 && !SkillFactory.getSkill(cooldown.skillId).isHyper() && cooldown.skillId < 400000000) {
                                        chr.changeCooldown(cooldown.skillId, -2000);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 2111003: {
                    for (final MapleMonster mob : arrays) {
                        if (!mob.isBuffed(2111003)) {
                            if (mist.getBox().contains(mob.getTruePosition())) {
                                if (mist.getOwner().getId() != this.getId()) {
                                    continue;
                                }
                                final List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                                final SecondaryStatEffect effect = SkillFactory.getSkill(2111003).getEffect(this.getSkillLevel(2111003));
                                SecondaryStatEffect bonusTime = null;
                                SecondaryStatEffect bonusDam = null;
                                if (this.getSkillLevel(2120044) > 0) {
                                    bonusTime = SkillFactory.getSkill(2120044).getEffect(this.getSkillLevel(2120044));
                                }
                                if (this.getSkillLevel(2120045) > 0) {
                                    bonusDam = SkillFactory.getSkill(2120045).getEffect(this.getSkillLevel(2120045));
                                }
                                applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(2111003, effect.getDOTTime() + ((bonusTime != null) ? bonusTime.getDOTTime() : 0), 15000L)));
                                mob.applyStatus(this.getClient(), applys, mist.getSource());
                            } else {
                                mob.cancelSingleStatus(mob.getBuff(2111003));
                            }
                        }
                    }
                    break;
                }
                case 4121015: {
                    final Iterator<MapleMonster> mobs = arrays.iterator();
                    final List<Pair<MonsterStatus, MonsterStatusEffect>> applys2 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                    if (this.getSkillLevel(4120046) > 0) {
                        applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(4121015, mist.getSource().getDuration(), -mist.getSource().getW() - SkillFactory.getSkill(4120046).getEffect(1).getV())));
                        applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Pad, new MonsterStatusEffect(4121015, mist.getSource().getDuration(), -mist.getSource().getW() - SkillFactory.getSkill(4120046).getEffect(1).getV())));
                    } else {
                        applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(4121015, mist.getSource().getDuration(), -mist.getSource().getW())));
                        applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Pad, new MonsterStatusEffect(4121015, mist.getSource().getDuration(), -mist.getSource().getW())));
                    }
                    if (this.getSkillLevel(4120047) > 0) {
                        applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed, new MonsterStatusEffect(4121015, mist.getSource().getDuration(), mist.getSource().getY() - SkillFactory.getSkill(4120047).getEffect(1).getS())));
                    } else {
                        applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed, new MonsterStatusEffect(4121015, mist.getSource().getDuration(), mist.getSource().getY())));
                    }
                    while (mobs.hasNext()) {
                        final MapleMonster mob2 = mobs.next();
                        if (!mob2.isBuffed(4121015) && ((mob2.getStats().isBoss() && this.getSkillLevel(4120048) > 0) || !mob2.getStats().isBoss())) {
                            if (mist.getBox().contains(mob2.getTruePosition())) {
                                if (mist.getOwner().getId() != this.getId() || mob2.isBuffed(4121015)) {
                                    continue;
                                }
                                mob2.applyStatus(this.getClient(), applys2, mist.getSource());
                            } else {
                                if (!mob2.isBuffed(4121015)) {
                                    continue;
                                }
                                mob2.cancelStatus(applys2);
                            }
                        }
                    }
                    break;
                }
                case 4221006: {
                    for (final MapleMonster mob : arrays) {
                        if (!mob.isBuffed(4221006)) {
                            if (mist.getBox().contains(mob.getTruePosition())) {
                                if (mist.getOwner().getId() != this.getId()) {
                                    continue;
                                }
                                mob.applyStatus(this.getClient(), MonsterStatus.MS_HitCritDamR, new MonsterStatusEffect(4221006, mist.getSource().getDuration()), mist.getSource().getX(), mist.getSource());
                            } else {
                                mob.cancelSingleStatus(mob.getBuff(4221006));
                            }
                        }
                    }
                    if (!mist.getBox().contains(this.getTruePosition())) {
                        break;
                    }
                    if (mist.getOwner().getId() == this.getId() && !this.getBuffedValue(4221006)) {
                        SkillFactory.getSkill(4221006).getEffect(mist.getSkillLevel()).applyTo(this, false);
                        break;
                    }
                    if (mist.getOwner().getParty() != null && mist.getOwner().getParty().getMemberById(this.getId()) != null && !this.getBuffedValue(4221006)) {
                        SkillFactory.getSkill(4221006).getEffect(mist.getSkillLevel()).applyTo(this, false);
                        break;
                    }
                    break;
                }
                case 21121057: {
                    final SecondaryStatEffect effect2 = SkillFactory.getSkill(mist.getSource().getSourceId()).getEffect(1);
                    for (final MapleCharacter chr2 : this.getMap().getAllChracater()) {
                        if (mist.getOwner().getParty() != null) {
                            for (final MaplePartyCharacter chr3 : mist.getOwner().getParty().getMembers()) {
                                final MapleCharacter chr4 = mist.getOwner().getClient().getChannelServer().getPlayerStorage().getCharacterByName(chr3.getName());
                                if (chr4 != null && mist.getBox().contains(chr4.getTruePosition())) {
                                    chr4.addMPHP(chr4.getStat().getCurrentMaxHp() / 100L * effect2.getW(), chr4.getStat().getCurrentMaxMp(chr4) / 100L * effect2.getW());
                                    chr4.dispelDebuffs();
                                }
                            }
                        } else {
                            if (mist.getOwner().getId() != chr2.getId() || !mist.getBox().contains(chr2.getTruePosition())) {
                                continue;
                            }
                            chr2.addMPHP(chr2.getStat().getCurrentMaxHp() / 100L * effect2.getW(), chr2.getStat().getCurrentMaxMp(chr2) / 100L * effect2.getW());
                            chr2.dispelDebuffs();
                        }
                    }
                    break;
                }
                case 12121005: {
                    for (final MapleCharacter chr : this.getMap().getAllChracater()) {
                        if (mist.getBox().contains(chr.getTruePosition())) {
                            if (mist.getOwner().getId() == chr.getId()) {
                                SkillFactory.getSkill(mist.getSource().getSourceId()).getEffect(mist.getSkillLevel()).applyTo(chr, false, 4000);
                            } else {
                                if (mist.getOwner().getParty() == null || mist.getOwner().getParty().getMemberById(chr.getId()) == null) {
                                    continue;
                                }
                                SkillFactory.getSkill(mist.getSource().getSourceId()).getEffect(mist.getSkillLevel()).applyTo(chr, false, 4000);
                            }
                        } else {
                            if (!chr.getBuffedValue(mist.getSource().getSourceId())) {
                                continue;
                            }
                            while (chr.getBuffedValue(mist.getSource().getSourceId())) {
                                chr.cancelEffect(chr.getBuffedEffect(mist.getSource().getSourceId()));
                            }
                        }
                    }
                    break;
                }
                case 162111000: {
                    final SecondaryStatEffect eff = SkillFactory.getSkill(162111001).getEffect(mist.getSkillLevel());
                    for (final MapleCharacter chr2 : this.getMap().getAllChracater()) {
                        if (mist.getBox().contains(chr2.getTruePosition())) {
                            if (mist.getOwner().getId() == chr2.getId()) {
                                eff.applyTo(chr2, false, mist.getSource().getU2() * 1000);
                            } else {
                                if (mist.getOwner().getParty() == null || mist.getOwner().getParty().getMemberById(chr2.getId()) == null) {
                                    continue;
                                }
                                eff.applyTo(chr2, false, mist.getSource().getX() * 1000);
                            }
                        }
                    }
                    break;
                }
                case 162111003: {
                    final SecondaryStatEffect eff = SkillFactory.getSkill(162111004).getEffect(mist.getSkillLevel());
                    for (final MapleCharacter chr2 : this.getMap().getAllChracater()) {
                        boolean heal = false;
                        boolean active = false;
                        if (mist.getBufftime() <= System.currentTimeMillis()) {
                            heal = true;
                            mist.setBufftime(System.currentTimeMillis() + mist.getSource().getZ() * 1000);
                        }
                        if (mist.getBox().contains(chr2.getTruePosition())) {
                            if (mist.getOwner().getId() == chr2.getId()) {
                                eff.applyTo(chr2, false, mist.getSource().getW() * 1000);
                                active = true;
                            } else if (mist.getOwner().getParty() != null && mist.getOwner().getParty().getMemberById(chr2.getId()) != null) {
                                eff.applyTo(chr2, false, mist.getSource().getX() * 1000);
                                active = true;
                            }
                            if (!heal || !active || chr2.getStat().getCurrentMaxHp() <= chr2.getStat().getHp()) {
                                continue;
                            }
                            chr2.addHP(chr2.getStat().getCurrentMaxHp() * eff.getHp() / 100L);
                            chr2.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr2, 0, 162111003, 10, 0, 0, (byte) (chr2.isFacingLeft() ? 1 : 0), true, chr2.getTruePosition(), null, null));
                            chr2.getMap().broadcastMessage(chr2, CField.EffectPacket.showEffect(chr2, 0, 162111003, 10, 0, 0, (byte) (chr2.isFacingLeft() ? 1 : 0), false, chr2.getTruePosition(), null, null), false);
                        }
                    }
                    break;
                }
                case 162121043: {
                    final SecondaryStatEffect eff = SkillFactory.getSkill(162121043).getEffect(mist.getSkillLevel());
                    for (final MapleCharacter chr2 : this.getMap().getAllChracater()) {
                        boolean heal = false;
                        boolean active = false;
                        if (mist.getBufftime() <= System.currentTimeMillis()) {
                            heal = true;
                            mist.setBufftime(System.currentTimeMillis() + 2000L);
                        }
                        if (mist.getBox().contains(chr2.getTruePosition())) {
                            if (mist.getOwner().getId() == chr2.getId()) {
                                if (!chr2.getBuffedValue(eff.getSourceId())) {
                                    eff.applyTo(chr2, false, 0);
                                }
                                active = true;
                            }
                            if (heal && active) {
                                continue;
                            }
                            continue;
                        } else {
                            if (!chr2.getBuffedValue(eff.getSourceId())) {
                                continue;
                            }
                            chr2.cancelEffect(eff);
                        }
                    }
                    for (final MapleMonster mob2 : arrays) {
                        if (!mob2.isBuffed(mist.getSource().getSourceId())) {
                            if (mist.getBox().contains(mob2.getTruePosition())) {
                                if (mist.getOwner().getId() != this.getId()) {
                                    continue;
                                }
                                final List<Pair<MonsterStatus, MonsterStatusEffect>> applys3 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                                applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Unk5, new MonsterStatusEffect(mist.getSource().getSourceId(), mist.getDuration(), -mist.getSource().getS())));
                                mob2.applyStatus(this.getClient(), applys3, mist.getSource());
                            } else {
                                mob2.cancelSingleStatus(mob2.getBuff(mist.getSource().getSourceId()));
                            }
                        }
                    }
                    break;
                }
                case 400001017: {
                    if (mist.getBox().contains(this.getTruePosition())) {
                        if (mist.getOwner().getId() == this.getId()) {
                            SkillFactory.getSkill(400001017).getEffect(mist.getSkillLevel()).applyTo(this, false);
                        } else if (mist.getOwner().getParty() != null && mist.getOwner().getParty().getMemberById(this.getId()) != null) {
                            SkillFactory.getSkill(400001017).getEffect(mist.getSkillLevel()).applyTo(this, false);
                        }
                    }
                    for (final MapleMonster mob : arrays) {
                        if (!mob.isBuffed(mist.getSource().getSourceId())) {
                            if (mist.getBox().contains(mob.getTruePosition())) {
                                if (mist.getOwner().getId() != this.getId()) {
                                    continue;
                                }
                                final List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                                applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(400001017, 4000, -mist.getSource().getZ())));
                                applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(400001017, 4000, -mist.getSource().getZ())));
                                mob.applyStatus(this.getClient(), applys, mist.getSource());
                            } else {
                                mob.cancelSingleStatus(mob.getBuff(400001017));
                            }
                        }
                    }
                    break;
                }
                case 400031039:
                case 400031040: {
                    for (final MapleCharacter chr : this.getMap().getAllChracater()) {
                        if (mist.getBox().contains(chr.getTruePosition())) {
                            if (mist.getOwner().getId() == chr.getId() && !chr.getBuffedValue(mist.getSource().getSourceId())) {
                                SkillFactory.getSkill(mist.getSource().getSourceId()).getEffect(mist.getSkillLevel()).applyTo(chr, false, 4000);
                            } else {
                                if (mist.getOwner().getParty() == null || mist.getOwner().getParty().getMemberById(chr.getId()) == null || chr.getBuffedValue(mist.getSource().getSourceId())) {
                                    continue;
                                }
                                SkillFactory.getSkill(mist.getSource().getSourceId()).getEffect(mist.getSkillLevel()).applyTo(chr, false, 4000);
                            }
                        } else {
                            if (!chr.getBuffedValue(mist.getSource().getSourceId())) {
                                continue;
                            }
                            chr.cancelEffectFromBuffStat(SecondaryStat.IndieUnkIllium);
                        }
                    }
                    break;
                }
                case 100001261: {
                    for (final MapleCharacter chr : this.getMap().getAllChracater()) {
                        if (mist.getBox().contains(chr.getTruePosition())) {
                            if (mist.getOwner().getId() == chr.getId() && !chr.getBuffedValue(mist.getSource().getSourceId())) {
                                SkillFactory.getSkill(mist.getSource().getSourceId()).getEffect(mist.getSkillLevel()).applyTo(chr, false, 4000);
                                if (time - chr.lastDistotionTime < 4000L) {
                                    continue;
                                }
                                chr.lastDistotionTime = time;
                                chr.dispelDebuffs();
                                SkillFactory.getSkill(100001261).getEffect(mist.getSkillLevel()).applyTo(chr, false);
                            } else {
                                if (mist.getOwner().getParty() == null || mist.getOwner().getParty().getMemberById(chr.getId()) == null || chr.getBuffedValue(mist.getSource().getSourceId())) {
                                    continue;
                                }
                                SkillFactory.getSkill(mist.getSource().getSourceId()).getEffect(mist.getSkillLevel()).applyTo(chr, false, 4000);
                                if (time - chr.lastDistotionTime < 4000L) {
                                    continue;
                                }
                                chr.lastDistotionTime = time;
                                chr.dispelDebuffs();
                                SkillFactory.getSkill(100001261).getEffect(mist.getSkillLevel()).applyTo(chr, false);
                            }
                        }
                    }
                    for (final MapleMonster mob : arrays) {
                        if (mist.getBox().contains(mob.getTruePosition()) && time - mob.lastDistotionTime >= mist.getSource().getSubTime()) {
                            mob.lastDistotionTime = time;
                            mob.dispels();
                            mob.applyStatus(this.getClient(), MonsterStatus.MS_AdddamSkill, new MonsterStatusEffect(100001261, mist.getSource().getSubTime()), mist.getSource().getX(), mist.getSource());
                            if (mob.getStats().isBoss()) {
                                continue;
                            }
                            mob.applyStatus(this.getClient(), MonsterStatus.MS_Freeze, new MonsterStatusEffect(100001261, mist.getSource().getSubTime()), mist.getSource().getSubTime(), mist.getSource());
                        }
                    }
                    break;
                }
                case 80001431: {
                    for (final MapleMonster mob : arrays) {
                        if (!mob.isBuffed(mist.getSource().getSourceId()) && mist.getBox().contains(mob.getTruePosition()) && !mob.getStats().isBoss() && mist.getOwner().getId() == this.getId()) {
                            final List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                            applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(80001431, mist.getDuration(), mob.getStats().getHp() / 100L * 10L)));
                            mob.applyStatus(this.getClient(), applys, mist.getSource());
                        }
                    }
                    break;
                }
            }
        } else if (mist.getMobSkill() != null) {
            if (mist.getMobSkill().getSkillId() == 191) {
                MapleMonster mob3 = this.getMap().getMonsterById(8910000);
                if (mob3 == null) {
                    mob3 = this.getMap().getMonsterById(8910100);
                }
                if (mob3 != null && mob3.getPosition().x <= mist.getPosition().x + 200 && mob3.getPosition().x >= mist.getPosition().x - 200) {
                    final EventInstanceManager eim = this.getEventInstance();
                    if (eim != null) {
                        if (mist.getMobSkill().getSkillLevel() == 2) {
                            if (eim.getTimeLeft() < 595000L) {
                                eim.restartEventTimer(eim.getTimeLeft() + 5000L, 4);
                            }
                        } else if (mist.getMobSkill().getSkillLevel() == 1 && eim.getTimeLeft() > 5000L) {
                            eim.restartEventTimer(eim.getTimeLeft() - 5000L, 5);
                        }
                    }
                }
            } else if (mist.getMobSkill().getSkillId() == 211) {
                for (final MapleCharacter chr : this.getMap().getAllChracater()) {
                    if (mist.getBox().contains(chr.getTruePosition())) {
                        for (final Map.Entry<Integer, AffectedOtherSkillInfo> ao : AffectedOtherSkillInfo.getMistAffectedInfo().entrySet()) {
                            if (ao.getKey() == mist.getMobSkill().getSkillLevel()) {
                                final MobSkill msi = MobSkillFactory.getMobSkill(ao.getValue().getAffectedOtherSkillID(), ao.getValue().getAffectedOtherSkillLev());
                                if (msi == null) {
                                    continue;
                                }
                                msi.applyEffect(chr, mist.getMob(), true, mist.getMob().isFacingLeft());
                            }
                        }
                    }
                }
            } else if (mist.getMobSkill().getSkillId() == 217) {
                final Rectangle rec = new Rectangle(mist.getBox().x + 64, mist.getBox().y + 50, 128, 60);
                for (final MapleCharacter chr2 : this.getMap().getAllChracater()) {
                    if (rec.contains(chr2.getTruePosition())) {
                        final MobSkill msi2 = MobSkillFactory.getMobSkill(122, 1);
                        msi2.applyEffect(chr2, mist.getMob(), true, mist.getMob().isFacingLeft());
                    }
                }
            }
        }
    }

    public void handleSummons(final long time) {
        MapleSummon summon;
        if ((summon = this.getSummon(400051022)) != null) {
            int size = 0;
            for (final MapleSummon sum : this.getMap().getAllSummonsThreadsafe()) {
                if (sum.getOwner().getId() == this.getId() && summon.getSkill() == 400051023) {
                    ++size;
                }
            }
            final SecondaryStatEffect bir = SkillFactory.getSkill(400051022).getEffect(summon.getSkillLevel());
            final SecondaryStatEffect birz = SkillFactory.getSkill(400051023).getEffect(summon.getSkillLevel());
            if (size < bir.getY()) {
                for (int i = 0; i < bir.getX(); ++i) {
                    final MapleSummon bird = new MapleSummon(this, birz, summon.getTruePosition(), SummonMovementType.BIRD_FOLLOW2);
                    this.getMap().spawnSummon(bird, bir.getW() * 1000);
                    this.addSummon(bird);
                }
            }
        }
        if ((summon = this.getSummon(400041044)) != null && summon.getOwner().getParty() != null && this.getParty() != null && summon.getOwner().getParty().getId() == this.getParty().getId()) {
            final SecondaryStatEffect effect = SkillFactory.getSkill(400041047).getEffect(summon.getSkillLevel());
            final Rectangle box = new Rectangle(summon.getTruePosition().x - 320, summon.getTruePosition().y - 490, 640, 530);
            if (box.contains(this.getTruePosition()) && this.getBuffedValue(SecondaryStat.IndieDamR, 400041047) == null) {
                effect.applyTo(this, false);
            }
        }
        if (this.getBuffedValue(400011077)) {
            final SecondaryStatEffect effect = SkillFactory.getSkill(400011077).getEffect(this.getSkillLevel(400011077));
            if (this.lastNemeaAttackTime == 0L) {
                this.lastNemeaAttackTime = System.currentTimeMillis();
            }
            if ((summon = this.getSummon(400011077)) != null && time - this.lastNemeaAttackTime >= effect.getX() * 1000) {
                this.lastNemeaAttackTime = System.currentTimeMillis();
                this.getMap().broadcastMessage(CField.SummonPacket.DeathAttack(summon));
            } else if ((summon = this.getSummon(400011078)) != null && time - this.lastGerionAttackTime >= effect.getZ() * 1000) {
                this.lastGerionAttackTime = System.currentTimeMillis();
                this.getMap().broadcastMessage(CField.SummonPacket.DeathAttack(summon));
            }
        }
    }

    public void handleHealSkills(final long time) {
        if (this.getSkillLevel(31110009) > 0 && time - this.lastDrainAuraTime >= 4000L) {
            this.lastDrainAuraTime = time;
            this.addMP(SkillFactory.getSkill(31110009).getEffect(this.getSkillLevel(31110009)).getY(), true);
        }
        if (this.getSkillLevel(32101009) > 0 && time - this.lastDrainAuraTime >= 4000L) {
            this.lastDrainAuraTime = time;
            this.addHP(this.getStat().getCurrentMaxHp() * SkillFactory.getSkill(32101009).getEffect(this.getSkillLevel(32101009)).getY() / 100L + SkillFactory.getSkill(32101009).getEffect(this.getSkillLevel(32101009)).getHp());
        }
        if (this.getSkillLevel(5100013) > 0) {
            final SecondaryStatEffect endurance = SkillFactory.getSkill(5100013).getEffect(this.getSkillLevel(5100013));
            if (this.lastHealTime == 0L) {
                this.lastHealTime = System.currentTimeMillis();
            }
            if (time - this.lastHealTime >= endurance.getW() * 1000) {
                this.lastHealTime = System.currentTimeMillis();
                this.addMPHP(this.getStat().getCurrentMaxHp() * endurance.getX() / 100L, this.getStat().getCurrentMaxMp(this) * endurance.getX() / 100L);
            }
        } else if (this.getSkillLevel(11110025) > 0) {
            final SecondaryStatEffect willofSteal = SkillFactory.getSkill(11110025).getEffect(this.getSkillLevel(11110025));
            if (this.lastHealTime == 0L) {
                this.lastHealTime = System.currentTimeMillis();
            }
            if (time - this.lastHealTime >= willofSteal.getW() * 1000) {
                this.lastHealTime = System.currentTimeMillis();
                this.addHP(this.getStat().getCurrentMaxHp() * willofSteal.getY() / 100L);
            }
        } else if (this.getSkillLevel(51110000) > 0) {
            final SecondaryStatEffect selfRecovery = SkillFactory.getSkill(51110000).getEffect(this.getSkillLevel(51110000));
            if (this.lastHealTime == 0L) {
                this.lastHealTime = System.currentTimeMillis();
            }
            if (time - this.lastHealTime >= 4000L) {
                this.lastHealTime = System.currentTimeMillis();
                this.addMPHP(selfRecovery.getHp(), selfRecovery.getMp());
            }
        } else if (this.getSkillLevel(61110006) > 0) {
            final SecondaryStatEffect selfRecovery = SkillFactory.getSkill(61110006).getEffect(this.getSkillLevel(61110006));
            if (this.lastHealTime == 0L) {
                this.lastHealTime = System.currentTimeMillis();
            }
            if (time - this.lastHealTime >= selfRecovery.getW() * 1000) {
                this.lastHealTime = System.currentTimeMillis();
                this.addMPHP(this.getStat().getCurrentMaxHp() * selfRecovery.getX() / 100L, this.getStat().getCurrentMaxMp(this) * selfRecovery.getX() / 100L);
            }
        }
        if (this.getBuffedValue(400041029)) {
            final SecondaryStatEffect effect = SkillFactory.getSkill(400041029).getEffect(this.getSkillLevel(400041029));
            final int consumeMP = (int) (this.getStat().getMaxMp() * (effect.getQ() / 100.0)) + effect.getY();
            if (this.getStat().getMp() < consumeMP) {
                this.cancelEffectFromBuffStat(SecondaryStat.Overload);
            } else {
                this.addMP(-consumeMP);
            }
        }
        if ((this.getBuffedValue(32111012) || this.getBuffedValue(400021006)) && this.getSkillCustomValue(32111112) == null) {
            final int activeSkillid = this.getBuffedValue(400021006) ? 400021006 : 32111012;
            if (this.getBuffedOwner(activeSkillid) == this.getId() && this.getSkillLevel(32120062) > 0) {
                if (this.getParty() != null) {
                    for (final MaplePartyCharacter chr1 : this.getParty().getMembers()) {
                        final MapleCharacter curChar = this.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr1.getId());
                        if (curChar != null && curChar.getBuffedValue(activeSkillid)) {
                            final Map<SecondaryStat, Pair<Integer, Integer>> statupz = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                            final Iterator<Map.Entry<SecondaryStat, MapleDiseases>> iterator2 = curChar.getDiseases().entrySet().iterator();
                            if (iterator2.hasNext()) {
                                final Map.Entry<SecondaryStat, MapleDiseases> d = iterator2.next();
                                curChar.dispelDebuff(d);
                                statupz.put(d.getKey(), new Pair<Integer, Integer>(d.getValue().getValue(), d.getValue().getDuration()));
                            }
                            if (statupz.isEmpty()) {
                                continue;
                            }
                            curChar.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(curChar, 0, 32120062, 4, 0, 0, (byte) (curChar.isFacingLeft() ? 1 : 0), true, curChar.getTruePosition(), null, null));
                            curChar.getMap().broadcastMessage(curChar, CField.EffectPacket.showEffect(curChar, 0, 32120062, 4, 0, 0, (byte) (curChar.isFacingLeft() ? 1 : 0), false, curChar.getTruePosition(), null, null), false);
                            curChar.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(statupz, curChar));
                            curChar.getMap().broadcastMessage(curChar, CWvsContext.BuffPacket.cancelForeignBuff(curChar, statupz), false);
                        }
                    }
                } else {
                    final Map<SecondaryStat, Pair<Integer, Integer>> statupz2 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    final Iterator<Map.Entry<SecondaryStat, MapleDiseases>> iterator3 = this.getDiseases().entrySet().iterator();
                    if (iterator3.hasNext()) {
                        final Map.Entry<SecondaryStat, MapleDiseases> d2 = iterator3.next();
                        this.dispelDebuff(d2);
                        statupz2.put(d2.getKey(), new Pair<Integer, Integer>(d2.getValue().getValue(), d2.getValue().getDuration()));
                    }
                    if (!statupz2.isEmpty()) {
                        this.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(this, 0, 32120062, 4, 0, 0, (byte) (this.isFacingLeft() ? 1 : 0), true, this.getTruePosition(), null, null));
                        this.getMap().broadcastMessage(this, CField.EffectPacket.showEffect(this, 0, 32120062, 4, 0, 0, (byte) (this.isFacingLeft() ? 1 : 0), false, this.getTruePosition(), null, null), false);
                        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(statupz2, this));
                        this.getMap().broadcastMessage(this, CWvsContext.BuffPacket.cancelForeignBuff(this, statupz2), false);
                    }
                }
                this.setSkillCustomInfo(32111112, 0L, 5000L);
            }
        }
        if (this.getBuffedValue(400021006)) {
            this.addMP(-this.getBuffedEffect(400021006).getMPCon());
        }
        final SecondaryStat[] array;
        final SecondaryStat[] mpCons = array = new SecondaryStat[]{SecondaryStat.YellowAura, SecondaryStat.DrainAura, SecondaryStat.BlueAura, SecondaryStat.DarkAura, SecondaryStat.DebuffAura, SecondaryStat.IceAura, SecondaryStat.FireAura};
        for (final SecondaryStat mpCon : array) {
            if (this.getBuffedValue(mpCon) != null && this.getBuffedOwner(this.getBuffedEffect(mpCon).getSourceId()) == this.getId() && !this.getBuffedValue(400021006)) {
                this.addMP(-this.getBuffedEffect(mpCon).getMPCon());
            }
        }
    }

    public void handleSecondaryStats(final long time, final boolean force) {
        final Map<SecondaryStatEffect, List<SecondaryStat>> stats = new ConcurrentHashMap<SecondaryStatEffect, List<SecondaryStat>>();
        final Iterator<Pair<SecondaryStat, SecondaryStatValueHolder>> iter = this.effects.iterator();
        while (iter.hasNext()) {
            final Pair<SecondaryStat, SecondaryStatValueHolder> effect = iter.next();
            final SecondaryStat stat = effect.left;
            final SecondaryStatValueHolder vh = effect.right;
            if (stat != null && vh != null) {
                final SecondaryStatEffect eff = vh.effect;
                final long remainDuration = vh.localDuration - (time - vh.startTime);
                if (!force && (remainDuration > 0L || vh.localDuration == 0)) {
                    continue;
                }
                if (stats.containsKey(eff)) {
                    SecondaryStatEffect sf = null;
                    for (final Map.Entry<SecondaryStatEffect, List<SecondaryStat>> z : stats.entrySet()) {
                        if (eff == z.getKey()) {
                            sf = z.getKey();
                            break;
                        }
                    }
                    if (sf == null || sf.getSourceId() != eff.getSourceId()) {
                        continue;
                    }
                    stats.get(eff).add(stat);
                } else {
                    stats.put(eff, new ArrayList<SecondaryStat>(Collections.singleton(stat)));
                }
            } else {
                iter.remove();
            }
        }
        if (!stats.isEmpty()) {
            for (final Map.Entry<SecondaryStatEffect, List<SecondaryStat>> stat2 : stats.entrySet()) {
                if (this.client.getChannelServer() != null && this.client.getChannelServer().getPlayerStorage().getCharacterById(this.id) != null) {
                    this.cancelEffect(stat2.getKey(), stat2.getValue(), false, true);
                }
            }
        }
    }

    public MapleCharacter getPlayer() {
        return this;
    }

    public static MapleCharacter getDefault(final MapleClient client, final LoginInformationProvider.JobType type) {
        final MapleCharacter ret = new MapleCharacter(false, false);
        ret.client = client;
        ret.map = null;
        ret.exp = 0L;
        ret.gmLevel = 0;
        ret.job = (short) type.id;
        ret.meso = 0L;
        ret.level = 1;
        ret.remainingAp = 0;
        ret.fame = 0;
        ret.accountid = client.getAccID();
        ret.buddylist = new BuddyList((byte) 20);
        ret.unions = new UnionList();
        ret.stats.str = 12;
        ret.stats.dex = 5;
        ret.stats.int_ = 4;
        ret.stats.luk = 4;
        ret.stats.maxhp = 50L;
        ret.stats.hp = 50L;
        ret.stats.maxmp = 50L;
        ret.stats.mp = 50L;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, ret.accountid);
            rs = ps.executeQuery();
            if (rs.next()) {
                ret.client.setAccountName(rs.getString("name"));
                ret.nxcredit = rs.getInt("nxCredit");
                ret.acash = 0;
                ret.maplepoints = rs.getInt("mPoints");
                ret.points = rs.getInt("points");
                ret.vpoints = rs.getInt("vpoints");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
        return ret;
    }

    public static final MapleCharacter ReconstructChr(final CharacterTransfer ct, final MapleClient client, final boolean isChannel) {
        final MapleCharacter ret = new MapleCharacter(true, false);
        ret.client = client;
        if (!isChannel) {
            ret.client.setChannel(ct.channel);
        }
        ret.id = ct.characterid;
        ret.name = ct.name;
        ret.level = ct.level;
        ret.fame = ct.fame;
        ret.setCalcDamage(new CalcDamage());
        ret.stats.str = ct.str;
        ret.stats.dex = ct.dex;
        ret.stats.int_ = ct.int_;
        ret.stats.luk = ct.luk;
        ret.stats.maxhp = ct.maxhp;
        ret.stats.maxmp = ct.maxmp;
        ret.stats.hp = ct.hp;
        ret.stats.mp = ct.mp;
        ret.customValue.putAll(ct.customValue);
        ret.customInfo.putAll(ct.customInfo);
        ret.chalktext = ct.chalkboard;
        ret.gmLevel = ct.gmLevel;
        ret.LinkMobCount = ct.LinkMobCount;
        ret.exp = ct.exp;
        ret.hpApUsed = ct.hpApUsed;
        ret.remainingSp = ct.remainingSp;
        ret.remainingAp = ct.remainingAp;
        ret.meso = ct.meso;
        ret.stolenSkills = ct.stolenSkills;
        ret.skinColor = ct.skinColor;
        ret.secondSkinColor = ct.secondSkinColor;
        ret.gender = ct.gender;
        ret.secondgender = ct.secondgender;
        ret.job = ct.job;
        ret.hair = ct.hair;
        ret.secondhair = ct.secondhair;
        ret.face = ct.face;
        ret.secondface = ct.secondface;
        ret.demonMarking = ct.demonMarking;
        ret.accountid = ct.accountid;
        ret.totalWins = ct.totalWins;
        ret.totalLosses = ct.totalLosses;
        client.setAccID(ct.accountid);
        ret.mapid = ct.mapid;
        ret.initialSpawnPoint = ct.initialSpawnPoint;
        ret.world = ct.world;
        ret.guildid = ct.guildid;
        ret.guildrank = ct.guildrank;
        ret.guildContribution = ct.guildContribution;
        ret.lastattendance = ct.lastattendance;
        ret.allianceRank = ct.alliancerank;
        ret.points = ct.points;
        ret.vpoints = ct.vpoints;
        ret.fairyExp = ct.fairyExp;
        ret.cardStack = ct.cardStack;
        ret.marriageId = ct.marriageId;
        ret.currentrep = ct.currentrep;
        ret.totalrep = ct.totalrep;
        ret.pvpExp = ct.pvpExp;
        ret.pvpPoints = ct.pvpPoints;
        ret.reborns = ct.reborns;
        ret.apstorage = ct.apstorage;
        if (ret.guildid > 0) {
            ret.mgc = new MapleGuildCharacter(ret);
        }
        ret.fatigue = ct.fatigue;
        ret.buddylist = new BuddyList(ct.buddysize);
        ret.setUnions(new UnionList());
        ret.subcategory = ct.subcategory;
        ret.keyValues.putAll(ct.keyValues);
        ret.emoticons = ct.emoticons;
        ret.emoticonTabs = ct.emoticonTabs;
        ret.savedEmoticon = ct.savedEmoticon;
        if (isChannel) {
            final MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
            ret.map = mapFactory.getMap(ret.mapid);
            if (ret.map == null) {
                ret.map = mapFactory.getMap(ServerConstants.warpMap);
            } else if (ret.map.getForcedReturnId() != 999999999 && ret.map.getForcedReturnMap() != null) {
                ret.map = ret.map.getForcedReturnMap();
            }
            MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
            if (portal == null) {
                portal = ret.map.getPortal(0);
                ret.initialSpawnPoint = 0;
            }
            ret.setPosition(portal.getPosition());
            final int messengerid = ct.messengerid;
            if (messengerid > 0) {
                ret.messenger = World.Messenger.getMessenger(messengerid);
            }
        } else {
            ret.messenger = null;
        }
        final int partyid = ct.partyid;
        if (partyid >= 0) {
            final MapleParty party = World.Party.getParty(partyid);
            if (party != null && party.getMemberById(ret.id) != null) {
                ret.party = party;
            }
        }
        for (Map.Entry<Integer, Object> qs : ct.Quest.entrySet()) {
            MapleQuestStatus queststatus_from = (MapleQuestStatus) qs.getValue();
            queststatus_from.setQuest(((Integer) qs.getKey()).intValue());
            ret.quests.put(queststatus_from.getQuest(), queststatus_from);
        }
        for (final Map.Entry<Integer, SkillEntry> qs2 : ct.Skills.entrySet()) {
            ret.skills.put(SkillFactory.getSkill(qs2.getKey()), qs2.getValue());
        }
        for (final Map.Entry<MapleTrait.MapleTraitType, Integer> t : ct.traits.entrySet()) {
            ret.traits.get(t.getKey()).setExp(t.getValue());
        }
        ret.inventory = (MapleInventory[]) ct.inventorys;
        ret.BlessOfFairy_Origin = ct.BlessOfFairy;
        ret.BlessOfEmpress_Origin = ct.BlessOfEmpress;
        ret.skillMacros = (SkillMacro[]) ct.skillmacro;
        ret.coodination = ct.coodination;
        ret.keylayout = new MapleKeyLayout(ct.keymap);
        ret.questinfo = ct.InfoQuest;
        ret.savedLocations = ct.savedlocation;
        ret.wishlist = ct.wishlist;
        ret.rocks = ct.rocks;
        ret.regrocks = ct.regrocks;
        ret.hyperrocks = ct.hyperrocks;
        ret.buddylist.loadFromTransfer(ct.buddies);
        ret.unions.loadFromTransfer(ct.unions);
        ret.keydown_skill = 0L;
        ret.lastfametime = ct.lastfametime;
        ret.lastmonthfameids = ct.famedcharacters;
        ret.lastmonthbattleids = ct.battledaccs;
        ret.extendedSlots = ct.extendedSlots;
        ret.itcafetime = ct.itcafetime;
        ret.storage = (MapleStorage) ct.storage;
        ret.cs = (CashShop) ct.cs;
        client.setAccountName(ct.accountname);
        client.setSecondPassword(ct.secondPassword);
        ret.nxcredit = ct.nxCredit;
        ret.acash = ct.ACash;
        ret.maplepoints = ct.MaplePoints;
        ret.numClones = ct.clonez;
        ret.pets = ct.pets;
        ret.imps = ct.imps;
        ret.rebuy = ct.rebuy;
        ret.cores = ct.cores;
        ret.matrixs = ct.matrixs;
        ret.symbol = ct.symbol;
        ret.setAuctionitems(ct.auctionitems);
        ret.basecolor = ct.basecolor;
        ret.addcolor = ct.addcolor;
        ret.baseprob = ct.baseprob;
        ret.secondbasecolor = ct.secondbasecolor;
        ret.secondaddcolor = ct.secondaddcolor;
        ret.secondbaseprob = ct.secondbaseprob;
        ret.linkskills = ct.linkskills;
        ret.mount = new MapleMount(ret, ct.mount_itemid, PlayerStats.getSkillByJob(1004, ret.job), ct.mount_Fatigue, ct.mount_level, ct.mount_exp);
        ret.honourExp = ct.honourexp;
        ret.honorLevel = ct.honourlevel;
        ret.innerSkills = (List<InnerSkillValueHolder>) ct.innerSkills;
        ret.returnscroll = (Equip) ct.returnscroll;
        ret.choicepotential = (Equip) ct.choicepotential;
        ret.memorialcube = (Item) ct.memorialcube;
        ret.returnSc = ct.returnSc;
        ret.lastCharGuildId = ct.lastCharGuildId;
        ret.betaclothes = ct.betaclothes;
        ret.energy = ct.energy;
        ret.energyCharge = ct.energycharge;
        ret.hairRoom = ct.hairRoom;
        ret.faceRoom = ct.faceRoom;
        ret.skinRoom = ct.skinRoom;
        ret.expirationTask(false, false);
        ret.stats.recalcLocalStats(true, ret);
        client.setTempIP(ct.tempIP);
        return ret;
    }

    public static MapleCharacter loadCharFromDB(int charid, MapleClient client, boolean channelserver) {
        /*  2483 */
        MapleCharacter ret = new MapleCharacter(channelserver, channelserver);
        /*  2484 */
        ret.client = client;
        /*  2485 */
        ret.id = charid;
        /*  2486 */
        PreparedStatement ps = null;
        /*  2487 */
        PreparedStatement pse = null;
        /*  2488 */
        ResultSet rs = null;
        /*  2489 */
        Connection con = null;

        try {
            /*  2492 */
            con = DatabaseConnection.getConnection();
            /*  2493 */
            ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
            /*  2494 */
            ps.setInt(1, charid);
            /*  2495 */
            rs = ps.executeQuery();
            /*  2496 */
            if (!rs.next()) {
                /*  2497 */
                rs.close();
                /*  2498 */
                ps.close();
                /*  2499 */
                throw new RuntimeException("Loading the Char Failed (char not found)");
            }
            /*  2501 */
            ret.name = rs.getString("name");
            /*  2502 */
            ret.level = rs.getShort("level");
            /*  2503 */
            ret.fame = rs.getInt("fame");
            /*  2504 */
            ret.MesoChairCount = rs.getInt("mesochair");
            /*  2505 */
            ret.stats.str = rs.getShort("str");
            /*  2506 */
            ret.stats.dex = rs.getShort("dex");
            /*  2507 */
            ret.stats.int_ = rs.getShort("int");
            /*  2508 */
            ret.stats.luk = rs.getShort("luk");
            /*  2509 */
            ret.stats.maxhp = rs.getInt("maxhp");
            /*  2510 */
            ret.stats.maxmp = rs.getInt("maxmp");
            /*  2511 */
            ret.stats.hp = rs.getInt("hp");
            /*  2512 */
            ret.stats.mp = rs.getInt("mp");
            /*  2513 */
            ret.job = rs.getShort("job");
            /*  2514 */
            ret.gmLevel = rs.getByte("gm");
            /*  2515 */
            ret.LinkMobCount = rs.getInt("LinkMobCount");
            /*  2516 */
            ret.exp = rs.getLong("exp");
            /*  2517 */
            ret.hpApUsed = rs.getShort("hpApUsed");
            /*  2518 */
            String[] sp = rs.getString("sp").split(",");
            /*  2519 */
            for (int i = 0; i < ret.remainingSp.length; i++) {
                /*  2520 */
                ret.remainingSp[i] = 0;
            }
            /*  2522 */
            ret.remainingAp = rs.getShort("ap");
            /*  2523 */
            ret.meso = rs.getLong("meso");
            /*  2524 */
            ret.skinColor = rs.getByte("skincolor");
            /*  2525 */
            ret.secondSkinColor = rs.getByte("secondSkincolor");
            /*  2526 */
            ret.gender = rs.getByte("gender");
            /*  2527 */
            ret.secondgender = rs.getByte("secondgender");
            /*  2528 */
            ret.hair = rs.getInt("hair");
            /*  2529 */
            ret.basecolor = rs.getInt("basecolor");
            /*  2530 */
            ret.addcolor = rs.getInt("addcolor");
            /*  2531 */
            ret.baseprob = rs.getInt("baseprob");

            /*  2533 */
            ret.secondbasecolor = rs.getInt("secondbasecolor");
            /*  2534 */
            ret.secondaddcolor = rs.getInt("secondaddcolor");
            /*  2535 */
            ret.secondbaseprob = rs.getInt("secondbaseprob");

            /*  2537 */
            ret.secondhair = rs.getInt("secondhair");
            /*  2538 */
            ret.face = rs.getInt("face");
            /*  2539 */
            ret.secondface = rs.getInt("secondface");
            /*  2540 */
            ret.demonMarking = rs.getInt("demonMarking");
            /*  2541 */
            ret.accountid = rs.getInt("accountid");
            /*  2542 */
            client.setAccID(ret.accountid);
            /*  2543 */
            ret.mapid = rs.getInt("map");
            /*  2544 */
            ret.initialSpawnPoint = rs.getByte("spawnpoint");
            /*  2545 */
            ret.world = rs.getByte("world");
            /*  2546 */
            ret.guildid = rs.getInt("guildid");
            /*  2547 */
            ret.guildrank = rs.getByte("guildrank");
            /*  2548 */
            ret.allianceRank = rs.getByte("allianceRank");
            /*  2549 */
            ret.guildContribution = rs.getInt("guildContribution");
            /*  2550 */
            ret.lastattendance = rs.getInt("lastattendance");
            /*  2551 */
            ret.totalWins = rs.getInt("totalWins");
            /*  2552 */
            ret.totalLosses = rs.getInt("totalLosses");
            /*  2553 */
            ret.currentrep = rs.getInt("currentrep");
            /*  2554 */
            ret.totalrep = rs.getInt("totalrep");
            /*  2555 */
            if (ret.guildid > 0) {
                /*  2556 */
                ret.mgc = new MapleGuildCharacter(ret);
            }
            /*  2558 */
            ret.buddylist = new BuddyList(rs.getByte("buddyCapacity"));
            /*  2559 */
            ret.setUnions(new UnionList());
            /*  2560 */
            ret.honourExp = rs.getInt("honourExp");
            /*  2561 */
            ret.honorLevel = rs.getInt("honourLevel");
            /*  2562 */
            ret.subcategory = rs.getByte("subcategory");
            /*  2563 */
            ret.mount = new MapleMount(ret, 0, PlayerStats.getSkillByJob(1004, ret.job), (byte) 0, (byte) 1, 0);
            /*  2564 */
            ret.rank = rs.getInt("rank");
            /*  2565 */
            ret.rankMove = rs.getInt("rankMove");
            /*  2566 */
            ret.jobRank = rs.getInt("jobRank");
            /*  2567 */
            ret.jobRankMove = rs.getInt("jobRankMove");
            /*  2568 */
            ret.marriageId = rs.getInt("marriageId");
            /*  2569 */
            ret.fatigue = rs.getShort("fatigue");
            /*  2570 */
            ret.pvpExp = rs.getInt("pvpExp");
            /*  2571 */
            ret.pvpPoints = rs.getInt("pvpPoints");
            /*  2572 */
            ret.itcafetime = rs.getInt("itcafetime");

            /*  2574 */
            ret.reborns = rs.getInt("reborns");
            /*  2575 */
            ret.apstorage = rs.getInt("apstorage");
            /*  2576 */
            ret.betaclothes = rs.getInt("betaclothes");

            /*  2578 */
            long choiceId = rs.getLong("choicepotential"), memorialId = rs.getLong("memorialcube"), returnscroll = rs.getLong("returnscroll");

            /*  2580 */
            ret.returnSc = rs.getInt("returnsc");
            /*  2581 */
            if (rs.getString("exceptionlist").length() > 0) {
                /*  2582 */
                String[] exceptionList = rs.getString("exceptionlist").split(",");
                /*  2583 */
                for (String str : exceptionList) {
                    /*  2584 */
                    ret.getExceptionList().add(Integer.valueOf(Integer.parseInt(str)));
                }
            }

            /*  2588 */
            for (MapleTrait t : ret.traits.values()) {
                /*  2589 */
                t.setExp(rs.getInt(t.getType().name()));
            }

            /*  2592 */
            if (channelserver) {
                /*  2593 */
                ChatEmoticon.LoadChatEmoticonTabs(ret);
                /*  2594 */
                ChatEmoticon.LoadSavedChatEmoticon(ret);
                /*  2595 */
                ChatEmoticon.LoadChatEmoticons(ret, ret.getEmoticonTabs());
                /*  2596 */
                ret.setCalcDamage(new CalcDamage());
                /*  2597 */
                MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
                /*  2598 */
                ret.map = mapFactory.getMap(ret.mapid);
                /*  2599 */
                if (ret.map == null) {
                    /*  2600 */
                    ret.map = mapFactory.getMap(ServerConstants.warpMap);
                }
                /*  2602 */
                MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
                /*  2603 */
                if (portal == null) {
                    /*  2604 */
                    portal = ret.map.getPortal(0);
                    /*  2605 */
                    ret.initialSpawnPoint = 0;
                }
                /*  2607 */
                ret.setPosition(portal.getPosition());

                /*  2609 */
                int partyid = rs.getInt("party");
                /*  2610 */
                if (partyid >= 0) {
                    /*  2611 */
                    MapleParty party = World.Party.getParty(partyid);
                    /*  2612 */
                    if (party != null && party.getMemberById(ret.id) != null) {
                        /*  2613 */
                        ret.party = party;
                    }
                }

                /*  2618 */
                String[] pets = rs.getString("pets").split(",");

                /*  2620 */
                ps.close();
                /*  2621 */
                rs.close();

                /*  2623 */
                ps = con.prepareStatement("SELECT * FROM inventoryitemscash WHERE uniqueid = ?");
                /*  2624 */
                for (int next = 0; next < 3; next++) {
                    /*  2625 */
                    if (!pets[next].equals("-1")) {
                        /*  2626 */
                        int petid = Integer.parseInt(pets[next]);
                        /*  2627 */
                        ps.setInt(1, petid);
                        /*  2628 */
                        rs = ps.executeQuery();
                        /*  2629 */
                        if (rs.next()) {
                            /*  2630 */
                            MaplePet pet = MaplePet.loadFromDb(rs.getInt("itemid"), petid, rs.getShort("position"));
                            /*  2631 */
                            ret.addPetBySlotId(pet, (byte) next);
                        }
                    }
                }
            }

            /*  2637 */
            ps.close();
            /*  2638 */
            rs.close();

            /*  2640 */
            if (channelserver) {

                /*  2642 */
                ps = con.prepareStatement("SELECT * FROM queststatus WHERE characterid = ?");
                /*  2643 */
                ps.setInt(1, charid);
                /*  2644 */
                rs = ps.executeQuery();

                /*  2646 */
                while (rs.next()) {
                    /*  2647 */
                    int id = rs.getInt("quest");
                    /*  2648 */
                    MapleQuest q = MapleQuest.getInstance(id);
                    /*  2649 */
                    byte stat = rs.getByte("status");

                    /*  2656 */
                    MapleQuestStatus status = new MapleQuestStatus(q, stat);
                    /*  2657 */
                    long cTime = rs.getLong("time");
                    /*  2658 */
                    if (cTime > -1L) {
                        /*  2659 */
                        status.setCompletionTime(cTime * 1000L);
                    }
                    /*  2661 */
                    status.setForfeited(rs.getInt("forfeited"));
                    /*  2662 */
                    status.setCustomData(rs.getString("customData"));
                    /*  2663 */
                    ret.quests.put(q, status);

                    /*  2665 */
                    pse = con.prepareStatement("SELECT * FROM queststatusmobs WHERE queststatusid = ?");
                    /*  2666 */
                    pse.setInt(1, rs.getInt("queststatusid"));
                    /*  2667 */
                    final ResultSet rsMobs = pse.executeQuery(); //CPU렉 1위

                    /*  2669 */
                    if (rsMobs.next()) {
                        /*  2670 */
                        status.setMobKills(rsMobs.getInt("mob"), rsMobs.getInt("count"));
                    }
                    /*  2672 */
                    pse.close();
                    /*  2673 */
                    rsMobs.close();
                }

                /*  2676 */
                ps.close();
                /*  2677 */
                rs.close();

                /*  2679 */
                ps = con.prepareStatement("SELECT * FROM inventoryslot where characterid = ?");
                /*  2680 */
                ps.setInt(1, charid);
                /*  2681 */
                rs = ps.executeQuery();

                /*  2683 */
                if (!rs.next()) {
                    /*  2684 */
                    rs.close();
                    /*  2685 */
                    ps.close();
                    /*  2686 */
                    throw new RuntimeException("No Inventory slot column found in SQL. [inventoryslot]");
                }
                /*  2688 */
                ret.getInventory(MapleInventoryType.EQUIP).setSlotLimit(rs.getShort("equip"));
                /*  2689 */
                ret.getInventory(MapleInventoryType.USE).setSlotLimit(rs.getShort("use"));
                /*  2690 */
                ret.getInventory(MapleInventoryType.SETUP).setSlotLimit(rs.getShort("setup"));
                /*  2691 */
                ret.getInventory(MapleInventoryType.ETC).setSlotLimit(rs.getShort("etc"));
                /*  2692 */
                ret.getInventory(MapleInventoryType.CASH).setSlotLimit(rs.getShort("cash"));
                /*  2693 */
                ret.getInventory(MapleInventoryType.CODY).setSlotLimit(rs.getShort("cody"));

                /*  2695 */
                ps.close();
                /*  2696 */
                rs.close();

                /*  2698 */
                for (MapleInventoryType type : MapleInventoryType.values()) {
                    /*  2699 */
                    if (type.getType() != 0) {
                        /*  2700 */
                        for (Map.Entry<Long, Item> mit : (Iterable<Map.Entry<Long, Item>>) ItemLoader.INVENTORY.loadItems(false, charid, type).entrySet()) {
                            /*  2701 */
                            if (((Item) mit.getValue()).getInventoryId() == choiceId && choiceId > 0L) {
                                /*  2702 */
                                ret.choicepotential = (Equip) mit.getValue();
                                /*  2703 */
                            } else if (((Item) mit.getValue()).getInventoryId() == memorialId && memorialId > 0L) {
                                /*  2704 */
                                ret.memorialcube = mit.getValue();
                                /*  2705 */
                            } else if (((Item) mit.getValue()).getInventoryId() == returnscroll && returnscroll > 0L) {
                                /*  2706 */
                                ret.returnscroll = (Equip) mit.getValue();
                            } else {
                                /*  2708 */
                                ret.getInventory(type.getType()).addFromDB(mit.getValue());
                            }
                            /*  2710 */
                            if (((Item) mit.getValue()).getPet() != null) ;
                        }
                    }
                }

                /*  2717 */
                ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
                /*  2718 */
                ps.setInt(1, ret.accountid);
                /*  2719 */
                rs = ps.executeQuery();
                /*  2720 */
                if (rs.next()) {
                    /*  2721 */
                    ret.getClient().setAccountName(rs.getString("name"));
                    /*  2722 */
                    ret.getClient().setSecondPassword(rs.getString("2ndpassword"));
                    /*  2723 */
                    ret.nxcredit = rs.getInt("nxCredit");
                    /*  2724 */
                    ret.acash = 0;
                    /*  2725 */
                    ret.maplepoints = rs.getInt("mPoints");
                    /*  2726 */
                    ret.points = rs.getInt("points");
                    /*  2727 */
                    ret.vpoints = rs.getInt("vpoints");

                    /*  2729 */
                    if (rs.getTimestamp("lastlogon") != null) {
                        /*  2730 */
                        Calendar cal = Calendar.getInstance();
                        /*  2731 */
                        cal.setTimeInMillis(rs.getTimestamp("lastlogon").getTime());
                    }

                    /*  2736 */
                    if (rs.getInt("banned") > 0) {
                        /*  2737 */
                        rs.close();
                        /*  2738 */
                        ps.close();
                        /*  2739 */
                        ret.getClient().getSession().close();
                        /*  2740 */
                        throw new RuntimeException("Loading a banned character");
                    }
                    /*  2742 */
                    rs.close();
                    /*  2743 */
                    ps.close();

                    /*  2745 */
                    ps = con.prepareStatement("UPDATE accounts SET lastlogon = CURRENT_TIMESTAMP() WHERE id = ?");
                    /*  2746 */
                    ps.setInt(1, ret.accountid);
                    /*  2747 */
                    ps.executeUpdate();
                } else {
                    /*  2749 */
                    rs.close();
                }
                /*  2751 */
                ps.close();

                try {
                    /*  2754 */
                    ps = con.prepareStatement("SELECT * FROM questinfo WHERE characterid = ?");
                    /*  2755 */
                    ps.setInt(1, charid);
                    /*  2756 */
                    rs = ps.executeQuery();

                    /*  2758 */
                    while (rs.next()) {
                        /*  2759 */
                        ret.questinfo.put(Integer.valueOf(rs.getInt("quest")), rs.getString("customData"));
                    }
                } finally {
                    /*  2762 */
                    rs.close();
                    /*  2763 */
                    ps.close();
                }

                /*  2766 */
                ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = ? AND level >= 70");
                /*  2767 */
                ps.setInt(1, ret.getAccountID());
                /*  2768 */
                rs = ps.executeQuery();
                /*  2769 */
                while (rs.next()) {
                    /*  2770 */
                    int skid = GameConstants.getLinkedSkillByJob(rs.getShort("job"));
                    /*  2771 */
                    if (skid == 0 || ret.getName().equals(rs.getString("name"))) {
                        continue;
                    }

                    /*  2775 */
                    Skill skil = SkillFactory.getSkill(skid);
                    /*  2776 */
                    int skl = (skil.getId() == 80000110) ? ((rs.getInt("level") >= 200) ? 5 : ((rs.getInt("level") >= 180) ? 4 : ((rs.getInt("level") >= 160) ? 3 : ((rs.getInt("level") >= 140) ? 2 : 1)))) : ((rs.getInt("level") >= 120) ? 2 : 1);
                    /*  2777 */
                    boolean pass = false;
                    /*  2778 */
                    for (Triple<Skill, SkillEntry, Integer> a : ret.linkskills) {
                        /*  2779 */
                        if (((Skill) a.getLeft()).getId() == skid
                                && /*  2780 */ ((SkillEntry) a.getMid()).skillevel >= skl) {
                            /*  2781 */
                            pass = true;
                        }
                    }

                    /*  2785 */
                    int max = (skil.getId() == 80000110) ? 5 : 2;

                    /*  2787 */
                    if (!pass) {
                        /*  2788 */
                        ret.linkskills.add(new Triple(skil, new SkillEntry(skl, (byte) max, -1L), Integer.valueOf(rs.getInt("id"))));
                    }
                }
                /*  2791 */
                rs.close();
                /*  2792 */
                ps.close();

                /*  2794 */
                ps = con.prepareStatement("SELECT skillid, skilllevel, masterlevel, expiration FROM skills WHERE characterid = ?");
                /*  2795 */
                ps.setInt(1, charid);
                /*  2796 */
                rs = ps.executeQuery();

                /*  2798 */
                while (rs.next()) {
                    /*  2799 */
                    int skid = rs.getInt("skillid");
                    /*  2800 */
                    Skill skil = SkillFactory.getSkill(skid);
                    /*  2801 */
                    int skl = rs.getInt("skilllevel");
                    /*  2802 */
                    byte msl = rs.getByte("masterlevel");
                    /*  2803 */
                    if (skil != null) {
                        /*  2804 */
                        if (skl > skil.getMaxLevel() && skid < 92000000) {
                            /*  2805 */
                            if (!skil.isBeginnerSkill() && skil.canBeLearnedBy(ret) && !skil.isSpecialSkill()) {
                                /*  2806 */
                                ret.remainingSp[GameConstants.getSkillBookForSkill(skid)] = ret.remainingSp[GameConstants.getSkillBookForSkill(skid)] + skl - skil.getMaxLevel();
                            }
                            /*  2808 */
                            skl = (byte) skil.getMaxLevel();
                        }
                        /*  2810 */
                        if (msl > skil.getMaxLevel()) {
                            /*  2811 */
                            msl = (byte) skil.getMaxLevel();
                        }
                        /*  2813 */
                        ret.skills.put(skil, new SkillEntry(skl, msl, rs.getLong("expiration")));
                        continue;
                        /*  2814 */
                    }
                    if (skil == null
                            && /*  2815 */ !GameConstants.isBeginnerJob(skid / 10000) && skid / 10000 != 900 && skid / 10000 != 800 && skid / 10000 != 9000) {
                        /*  2816 */
                        ret.remainingSp[GameConstants.getSkillBookForSkill(skid)] = ret.remainingSp[GameConstants.getSkillBookForSkill(skid)] + skl;
                    }
                }

                /*  2820 */
                rs.close();
                /*  2821 */
                ps.close();

                try {
                    /*  2824 */
                    ps = con.prepareStatement("SELECT * FROM core WHERE charid = ?", 1);
                    /*  2825 */
                    ps.setInt(1, ret.id);
                    /*  2826 */
                    rs = ps.executeQuery();
                    /*  2827 */
                    while (rs.next()) {
                        /*  2828 */
                        int coreid = rs.getInt("coreid");
                        /*  2829 */
 /*  2830 */
                        SpecialCoreOption spCore = null;
                        /*  2831 */
 /*  2834 */
                        Core core = new Core(rs.getLong("crcid"), coreid, ret.id, rs.getInt("level"), rs.getInt("exp"), rs.getInt("state"), rs.getInt("maxlevel"), rs.getInt("skill1"), rs.getInt("skill2"), rs.getInt("skill3"), rs.getInt("position"), spCore);
                        /*  2835 */
                        ret.cores.add(core);
                        /*  2836 */
                        core.setId(ret.cores.indexOf(core));
                    }
                    /*  2838 */
                    ps.close();
                    /*  2839 */
                    rs.close();
                    /*  2840 */
                } catch (Exception e) {
                    /*  2841 */
                    e.printStackTrace();
                }

                try {
                    /*  2845 */
                    ps = con.prepareStatement("SELECT * FROM matrix WHERE charid = ?", 1);
                    /*  2846 */
                    ps.setInt(1, ret.id);
                    /*  2847 */
                    rs = ps.executeQuery();
                    /*  2848 */
                    while (rs.next()) {
                        /*  2849 */
                        VMatrix matrix = new VMatrix(rs.getInt("id"), rs.getInt("position"), rs.getInt("level"), (rs.getByte("unlock") == 1));
                        /*  2850 */
                        ret.matrixs.add(matrix);
                    }
                    /*  2852 */
                    ps.close();
                    /*  2853 */
                    rs.close();
                    /*  2854 */
                } catch (Exception e) {
                    /*  2855 */
                    e.printStackTrace();
                }

                try {
                    /*  2859 */
                    ps = con.prepareStatement("SELECT * FROM coodination WHERE playerid = ? ORDER BY position ASC", 1);
                    /*  2860 */
                    ps.setInt(1, ret.id);
                    /*  2861 */
                    rs = ps.executeQuery();
                    /*  2862 */
                    while (rs.next()) {
                        /*  2863 */
                        AvatarLook a = AvatarLook.init(rs);
                        /*  2864 */
                        ret.coodination.add(a);
                    }
                    /*  2866 */
                    ps.close();
                    /*  2867 */
                    rs.close();
                    /*  2868 */
                } catch (Exception e) {
                    /*  2869 */
                    e.printStackTrace();
                }

                /*  2872 */
                ret.expirationTask(false, true);

                /*  2875 */
                ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = ? ORDER BY level DESC");
                /*  2876 */
                ps.setInt(1, ret.accountid);
                /*  2877 */
                rs = ps.executeQuery();
                /*  2878 */
                int maxlevel_ = 0, maxlevel_2 = 0;
                /*  2879 */
                while (rs.next()) {
                    /*  2880 */
                    if (rs.getInt("id") != charid) {
                        /*  2881 */
                        if (GameConstants.isKOC(rs.getShort("job"))) {
                            /*  2882 */
                            int k = rs.getShort("level") / 5;

                            /*  2884 */
                            if (k > 24) {
                                /*  2885 */
                                k = 24;
                            }
                            /*  2887 */
                            if (k > maxlevel_2 || maxlevel_2 == 0) {
                                /*  2888 */
                                maxlevel_2 = k;
                                /*  2889 */
                                ret.BlessOfEmpress_Origin = rs.getString("name");
                            }
                        }
                        /*  2892 */
                        int maxlevel = rs.getShort("level") / 10;

                        /*  2894 */
                        if (maxlevel > 20) {
                            /*  2895 */
                            maxlevel = 20;
                        }
                        /*  2897 */
                        if (maxlevel > maxlevel_ || maxlevel_ == 0) {
                            /*  2898 */
                            maxlevel_ = maxlevel;
                            /*  2899 */
                            ret.BlessOfFairy_Origin = rs.getString("name");
                        }
                    }
                }

                /*  2913 */
                if (ret.BlessOfFairy_Origin == null) {
                    /*  2914 */
                    ret.BlessOfFairy_Origin = ret.name;
                }
                /*  2916 */
                ret.skills.put(SkillFactory.getSkill(GameConstants.getBOF_ForJob(ret.job)), new SkillEntry(maxlevel_, (byte) 0, -1L));
                /*  2917 */
                if (SkillFactory.getSkill(GameConstants.getEmpress_ForJob(ret.job)) != null) {
                    /*  2918 */
                    if (ret.BlessOfEmpress_Origin == null) {
                        /*  2919 */
                        ret.BlessOfEmpress_Origin = ret.BlessOfFairy_Origin;
                    }
                    /*  2921 */
                    ret.skills.put(SkillFactory.getSkill(GameConstants.getEmpress_ForJob(ret.job)), new SkillEntry(maxlevel_2, (byte) 0, -1L));
                }
                /*  2923 */
                ps.close();
                /*  2924 */
                rs.close();

                /*  2927 */
                ps = con.prepareStatement("SELECT skill_id, skill_level, max_level, rank FROM inner_ability_skills WHERE player_id = ?");
                /*  2928 */
                ps.setInt(1, charid);
                /*  2929 */
                rs = ps.executeQuery();
                /*  2930 */
                while (rs.next()) {
                    /*  2931 */
                    ret.innerSkills.add(new InnerSkillValueHolder(rs.getInt("skill_id"), rs.getByte("skill_level"), rs.getByte("max_level"), rs.getByte("rank")));
                }
                /*  2933 */
                ps.close();
                /*  2934 */
                rs.close();

                /*  2936 */
                ps = con.prepareStatement("SELECT * FROM skillmacros WHERE characterid = ?");
                /*  2937 */
                ps.setInt(1, charid);
                /*  2938 */
                rs = ps.executeQuery();

                /*  2940 */
                while (rs.next()) {
                    /*  2941 */
                    int position = rs.getInt("position");
                    /*  2942 */
                    SkillMacro macro = new SkillMacro(rs.getInt("skill1"), rs.getInt("skill2"), rs.getInt("skill3"), rs.getString("name"), rs.getInt("shout"), position);
                    /*  2943 */
                    ret.skillMacros[position] = macro;
                }
                /*  2945 */
                rs.close();
                /*  2946 */
                ps.close();

                /*  2948 */
                ps = con.prepareStatement("SELECT * FROM mannequins WHERE characterid = ?");
                /*  2949 */
                ps.setInt(1, charid);
                /*  2950 */
                rs = ps.executeQuery();
                /*  2951 */
                while (rs.next()) {
                    /*  2952 */
                    int type = rs.getInt("type");
                    /*  2953 */
                    MapleMannequin mq = new MapleMannequin(rs.getInt("value"), rs.getInt("baseProb"), rs.getInt("baseColor"), rs.getInt("addColor"));
                    /*  2954 */
                    switch (type) {
                        case 0:
                            /*  2956 */
                            ret.hairRoom.add(mq);

                        case 1:
                            /*  2959 */
                            ret.faceRoom.add(mq);

                        case 2:
                            /*  2962 */
                            ret.skinRoom.add(mq);
                    }

                }
                /*  2966 */
                rs.close();
                /*  2967 */
                ps.close();

                /*  2969 */
                ps = con.prepareStatement("SELECT `key`,`type`,`action` FROM keymap WHERE characterid = ?");
                /*  2970 */
                ps.setInt(1, charid);
                /*  2971 */
                rs = ps.executeQuery();

                /*  2973 */
                Map<Integer, Pair<Byte, Integer>> keyb = ret.keylayout.Layout();
                /*  2974 */
                while (rs.next()) {
                    /*  2975 */
                    keyb.put(Integer.valueOf(rs.getInt("key")), new Pair(Byte.valueOf(rs.getByte("type")), Integer.valueOf(rs.getInt("action"))));
                }
                /*  2977 */
                rs.close();
                /*  2978 */
                ps.close();
                /*  2979 */
                ret.keylayout.unchanged();

                /*  2981 */
                ps = con.prepareStatement("SELECT `locationtype`,`map` FROM savedlocations WHERE characterid = ?");
                /*  2982 */
                ps.setInt(1, charid);
                /*  2983 */
                rs = ps.executeQuery();
                /*  2984 */
                while (rs.next()) {
                    /*  2985 */
                    ret.savedLocations[rs.getInt("locationtype")] = rs.getInt("map");
                }
                /*  2987 */
                rs.close();
                /*  2988 */
                ps.close();

                /*  2990 */
                ps = con.prepareStatement("SELECT `characterid_to`,`when` FROM famelog WHERE characterid = ? AND DATEDIFF(NOW(),`when`) < 30");
                /*  2991 */
                ps.setInt(1, charid);
                /*  2992 */
                rs = ps.executeQuery();
                /*  2993 */
                ret.lastfametime = 0L;
                /*  2994 */
                ret.lastmonthfameids = new ArrayList<>(31);
                /*  2995 */
                while (rs.next()) {
                    /*  2996 */
                    ret.lastfametime = Math.max(ret.lastfametime, rs.getTimestamp("when").getTime());
                    /*  2997 */
                    ret.lastmonthfameids.add(Integer.valueOf(rs.getInt("characterid_to")));
                }
                /*  2999 */
                rs.close();
                /*  3000 */
                ps.close();

                /*  3002 */
                ps = con.prepareStatement("SELECT `accid_to`,`when` FROM battlelog WHERE accid = ? AND DATEDIFF(NOW(),`when`) < 30");
                /*  3003 */
                ps.setInt(1, ret.accountid);
                /*  3004 */
                rs = ps.executeQuery();
                /*  3005 */
                ret.lastmonthbattleids = new ArrayList<>();
                /*  3006 */
                while (rs.next()) {
                    /*  3007 */
                    ret.lastmonthbattleids.add(Integer.valueOf(rs.getInt("accid_to")));
                }
                /*  3009 */
                rs.close();
                /*  3010 */
                ps.close();

                /*  3012 */
                ps = con.prepareStatement("SELECT * FROM keyvalue WHERE id = ?");
                /*  3013 */
                ps.setInt(1, charid);
                /*  3014 */
                rs = ps.executeQuery();
                /*  3015 */
                while (rs.next()) {
                    /*  3016 */
                    ret.keyValues.put(rs.getString("key"), rs.getString("value"));
                }
                /*  3018 */
                rs.close();
                /*  3019 */
                ps.close();

                /*  3021 */
                ps = con.prepareStatement("SELECT `itemId` FROM extendedSlots WHERE characterid = ?");
                /*  3022 */
                ps.setInt(1, charid);
                /*  3023 */
                rs = ps.executeQuery();
                /*  3024 */
                while (rs.next()) {
                    /*  3025 */
                    ret.extendedSlots.add(Integer.valueOf(rs.getInt("itemId")));
                }
                /*  3027 */
                rs.close();
                /*  3028 */
                ps.close();

                /*  3030 */
                ret.buddylist.loadFromDb(ret.accountid);
                /*  3031 */
                ret.storage = MapleStorage.loadStorage(ret.accountid);
                /*  3032 */
                ret.getUnions().loadFromDb(ret.accountid);

                /*  3034 */
                ret.cs = new CashShop(ret.accountid, charid, ret.getJob());

                /*  3036 */
                ps = con.prepareStatement("SELECT sn FROM wishlist WHERE characterid = ?");
                /*  3037 */
                ps.setInt(1, charid);
                /*  3038 */
                rs = ps.executeQuery();
                /*  3039 */
                int j = 0;
                /*  3040 */
                while (rs.next()) {
                    /*  3041 */
                    ret.wishlist[j] = rs.getInt("sn");
                    /*  3042 */
                    j++;
                }
                /*  3044 */
                while (j < 12) {
                    /*  3045 */
                    ret.wishlist[j] = 0;
                    /*  3046 */
                    j++;
                }
                /*  3048 */
                rs.close();
                /*  3049 */
                ps.close();

                /*  3051 */
                ps = con.prepareStatement("SELECT mapid FROM trocklocations WHERE characterid = ?");
                /*  3052 */
                ps.setInt(1, charid);
                /*  3053 */
                rs = ps.executeQuery();
                /*  3054 */
                int r = 0;
                /*  3055 */
                while (rs.next()) {
                    /*  3056 */
                    ret.rocks[r] = rs.getInt("mapid");
                    /*  3057 */
                    r++;
                }
                /*  3059 */
                while (r < 10) {
                    /*  3060 */
                    ret.rocks[r] = 999999999;
                    /*  3061 */
                    r++;
                }
                /*  3063 */
                rs.close();
                /*  3064 */
                ps.close();

                /*  3066 */
                ps = con.prepareStatement("SELECT mapid FROM regrocklocations WHERE characterid = ?");
                /*  3067 */
                ps.setInt(1, charid);
                /*  3068 */
                rs = ps.executeQuery();
                /*  3069 */
                r = 0;
                /*  3070 */
                while (rs.next()) {
                    /*  3071 */
                    ret.regrocks[r] = rs.getInt("mapid");
                    /*  3072 */
                    r++;
                }
                /*  3074 */
                while (r < 5) {
                    /*  3075 */
                    ret.regrocks[r] = 999999999;
                    /*  3076 */
                    r++;
                }
                /*  3078 */
                rs.close();
                /*  3079 */
                ps.close();

                /*  3081 */
                ps = con.prepareStatement("SELECT mapid FROM hyperrocklocations WHERE characterid = ?");
                /*  3082 */
                ps.setInt(1, charid);
                /*  3083 */
                rs = ps.executeQuery();
                /*  3084 */
                r = 0;
                /*  3085 */
                while (rs.next()) {
                    /*  3086 */
                    ret.hyperrocks[r] = rs.getInt("mapid");
                    /*  3087 */
                    r++;
                }
                /*  3089 */
                while (r < 13) {
                    /*  3090 */
                    ret.hyperrocks[r] = 999999999;
                    /*  3091 */
                    r++;
                }
                /*  3093 */
                rs.close();
                /*  3094 */
                ps.close();

                /*  3096 */
                ps = con.prepareStatement("SELECT * from stolen WHERE characterid = ?");
                /*  3097 */
                ps.setInt(1, charid);
                /*  3098 */
                rs = ps.executeQuery();
                /*  3099 */
                while (rs.next()) {
                    /*  3100 */
                    ret.stolenSkills.add(new Pair(Integer.valueOf(rs.getInt("skillid")), Boolean.valueOf((rs.getInt("chosen") > 0))));
                }
                /*  3102 */
                rs.close();
                /*  3103 */
                ps.close();

                /*  3105 */
                ps = con.prepareStatement("SELECT * FROM imps WHERE characterid = ?");
                /*  3106 */
                ps.setInt(1, charid);
                /*  3107 */
                rs = ps.executeQuery();
                /*  3108 */
                r = 0;
                /*  3109 */
                while (rs.next()) {
                    /*  3110 */
                    ret.imps[r] = new MapleImp(rs.getInt("itemid"));
                    /*  3111 */
                    ret.imps[r].setLevel(rs.getByte("level"));
                    /*  3112 */
                    ret.imps[r].setState(rs.getByte("state"));
                    /*  3113 */
                    ret.imps[r].setCloseness(rs.getShort("closeness"));
                    /*  3114 */
                    ret.imps[r].setFullness(rs.getShort("fullness"));
                    /*  3115 */
                    r++;
                }
                /*  3117 */
                rs.close();
                /*  3118 */
                ps.close();

                /*  3120 */
                ps = con.prepareStatement("SELECT * FROM mountdata WHERE characterid = ?");
                /*  3121 */
                ps.setInt(1, charid);
                /*  3122 */
                rs = ps.executeQuery();
                /*  3123 */
                if (!rs.next()) {
                    /*  3124 */
                    throw new RuntimeException("No mount data found on SQL column");
                }
                /*  3126 */
                Item mount = ret.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -23);
                /*  3127 */
                ret.mount = new MapleMount(ret, (mount != null) ? mount.getItemId() : 0, PlayerStats.getSkillByJob(1004, ret.job), rs.getByte("Fatigue"), rs.getByte("Level"), rs.getInt("Exp"));
                /*  3128 */
                ps.close();
                /*  3129 */
                rs.close();

                /*  3131 */
                ret.stats.recalcLocalStats(true, ret);
            } else {
                /*  3133 */
                for (MapleInventoryType type : MapleInventoryType.values()) {
                    /*  3134 */
                    if (type.getType() != 0) {
                        /*  3135 */
                        for (Map.Entry<Long, Item> mit : (Iterable<Map.Entry<Long, Item>>) ItemLoader.INVENTORY.loadItems(false, charid, type).entrySet()) {
                            /*  3136 */
                            ret.getInventory(type.getType()).addFromDB(mit.getValue());
                            /*  3137 */
                            if (((Item) mit.getValue()).getPet() != null) ;
                        }
                    }
                }

                /*  3144 */
                ret.stats.recalcPVPRank(ret);
            }
            /*  3146 */
        } catch (SQLException ess) {
            /*  3147 */
            ess.printStackTrace();
            /*  3148 */
            //System.out.println("Failed to load character..");
            /*  3149 */
            FileoutputUtil.outputFileError("Log_Packet_Except.rtf", ess);
        } finally {
            try {
                /*  3152 */
                if (ps != null) {
                    /*  3153 */
                    ps.close();
                }
                /*  3155 */
                if (rs != null) {
                    /*  3156 */
                    rs.close();
                }
                /*  3158 */
                if (con != null) {
                    /*  3159 */
                    con.close();
                }
                /*  3161 */
            } catch (SQLException sQLException) {
            }
        }

        /*  3164 */
        return ret;
    }

    public static void saveNewCharToDB(final MapleCharacter chr, final LoginInformationProvider.JobType type, short db) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pse = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("INSERT INTO characters (level, str, dex, luk, `int`, hp, mp, maxhp, maxmp, sp, ap, skincolor, secondSkinColor, gender, secondgender, job, hair, secondhair, face, secondface, demonMarking, map, meso, party, buddyCapacity, subcategory, accountid, name, world, itcafetime, basecolor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 1);
            ps.setInt(1, chr.level);
            final PlayerStats stat = chr.stats;
            ps.setShort(2, stat.getStr());
            ps.setShort(3, stat.getDex());
            ps.setShort(4, stat.getInt());
            ps.setShort(5, stat.getLuk());
            ps.setLong(6, stat.getHp());
            ps.setLong(7, stat.getMp());
            ps.setLong(8, stat.getMaxHp());
            ps.setLong(9, stat.getMaxMp());
            final StringBuilder sps = new StringBuilder();
            for (int i = 0; i < chr.remainingSp.length; ++i) {
                sps.append(chr.remainingSp[i]);
                sps.append(",");
            }
            final String sp = sps.toString();
            ps.setString(10, sp.substring(0, sp.length() - 1));
            ps.setShort(11, chr.remainingAp);
            ps.setByte(12, chr.skinColor);
            ps.setByte(13, chr.secondSkinColor);
            ps.setByte(14, chr.gender);
            ps.setByte(15, chr.secondgender);
            ps.setShort(16, chr.job);
            ps.setInt(17, chr.hair);
            ps.setInt(18, chr.secondhair);
            ps.setInt(19, chr.face);
            ps.setInt(20, chr.secondface);
            ps.setInt(21, chr.demonMarking);
            if (db < 0 || db > 2) {
                db = 0;
            }
            ps.setInt(22, type.map);
            ps.setLong(23, chr.meso);
            ps.setInt(24, -1);
            ps.setByte(25, chr.buddylist.getCapacity());
            ps.setInt(26, db);
            ps.setInt(27, chr.getAccountID());
            ps.setString(28, chr.name);
            ps.setByte(29, chr.world);
            ps.setInt(30, chr.getInternetCafeTime());
            ps.setInt(31, -1);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                ps.close();
                rs.close();
                throw new DatabaseException("Inserting char failed.");
            }
            chr.id = rs.getInt(1);
            ps.close();
            rs.close();
            for (final MapleQuestStatus q : chr.quests.values()) {
                ps = con.prepareStatement("INSERT INTO queststatus (`queststatusid`, `characterid`, `quest`, `status`, `time`, `forfeited`, `customData`) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", 1);
                ps.setInt(1, chr.id);
                ps.setInt(2, q.getQuest().getId());
                ps.setInt(3, q.getStatus());
                ps.setInt(4, (int) (q.getCompletionTime() / 1000L));
                ps.setInt(5, q.getForfeited());
                ps.setString(6, q.getCustomData());
                ps.execute();
                rs = ps.getGeneratedKeys();
                if (q.hasMobKills()) {
                    rs.next();
                    for (final int mob : q.getMobKills().keySet()) {
                        pse = con.prepareStatement("INSERT INTO queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
                        pse.setInt(1, rs.getInt(1));
                        pse.setInt(2, mob);
                        pse.setInt(3, q.getMobKills(mob));
                        pse.execute();
                        pse.close();
                    }
                }
                ps.close();
                rs.close();
            }
            ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, chr.id);
            for (final Map.Entry<Skill, SkillEntry> skill : chr.skills.entrySet()) {
                ps.setInt(2, skill.getKey().getId());
                ps.setInt(3, skill.getValue().skillevel);
                ps.setByte(4, skill.getValue().masterlevel);
                ps.setLong(5, skill.getValue().expiration);
                ps.execute();
            }
            ps.close();
            ps = con.prepareStatement("INSERT INTO inventoryslot (characterid, `equip`, `use`, `setup`, `etc`, `cash`, `cody`) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, chr.id);
            ps.setShort(2, (short) 128); // Eq
            ps.setShort(3, (short) 128); // Use
            ps.setShort(4, (short) 128); // Setup
            ps.setShort(5, (short) 128); // ETC
            ps.setShort(6, (short) 128);
            ps.setShort(7, (short) 128);
            ps.execute();
            ps.close();
            ps = con.prepareStatement("INSERT INTO mountdata (characterid, `Level`, `Exp`, `Fatigue`) VALUES (?, ?, ?, ?)");
            ps.setInt(1, chr.id);
            ps.setByte(2, (byte) 1);
            ps.setInt(3, 0);
            ps.setByte(4, (byte) 0);
            ps.execute();
            ps.close();
            final int[] array1 = {2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 29, 31, 33, 34, 35, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 50, 51, 56, 57, 59, 60, 61, 62, 63, 64, 65, 83, 1, 70};
            final int[] array2 = {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 4, 4, 5, 5, 6, 6, 6, 6, 6, 6, 6, 0, 4, 4};
            final int[] array3 = {10, 12, 13, 18, 23, 28, 8, 5, 0, 4, 27, 30, 32, 1, 24, 19, 14, 15, 52, 2, 25, 17, 11, 3, 20, 26, 16, 22, 9, 50, 51, 6, 31, 29, 7, 33, 53, 54, 100, 101, 102, 103, 104, 105, 106, 52, 46, 47};
            for (int j = 0; j < array1.length; ++j) {
                ps = con.prepareStatement("INSERT INTO keymap (characterid, `key`, `type`, `action`) VALUES (?, ?, ?, ?)");
                ps.setInt(1, chr.id);
                ps.setInt(2, array1[j]);
                ps.setInt(3, array2[j]);
                ps.setInt(4, array3[j]);
                ps.execute();
                ps.close();
            }
            chr.saveInventory(con, true);
        } catch (Exception e) {
            FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e);
            e.printStackTrace();
            System.err.println("[charsave] Error saving character data");
            try {
                if (pse != null) {
                    pse.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e2) {
                FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e2);
                e2.printStackTrace();
                System.err.println("[charsave] Error going back to autocommit mode");
            }
        } finally {
            try {
                if (pse != null) {
                    pse.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e3) {
                FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e3);
                e3.printStackTrace();
                System.err.println("[charsave] Error going back to autocommit mode");
            }
        }
    }

    public void saveMannequinToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM mannequins WHERE characterid = ?");
            for (final MapleMannequin hair : this.getHairRoom()) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO mannequins (value, baseprob, basecolor, addcolor, characterid, type) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, hair.getValue());
                ps.setInt(2, hair.getBaseProb());
                ps.setInt(3, hair.getBaseColor());
                ps.setInt(4, hair.getAddColor());
                ps.setInt(5, this.id);
                ps.setInt(6, 0);
                ps.execute();
                ps.close();
            }
            for (final MapleMannequin face : this.getFaceRoom()) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO mannequins (value, baseprob, basecolor, addcolor, characterid, type) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, face.getValue());
                ps.setInt(2, face.getBaseProb());
                ps.setInt(3, face.getBaseColor());
                ps.setInt(4, face.getAddColor());
                ps.setInt(5, this.id);
                ps.setInt(6, 1);
                ps.execute();
                ps.close();
            }
            for (final MapleMannequin skin : this.skinRoom) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO mannequins (value, baseprob, basecolor, addcolor, characterid, type) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, skin.getValue());
                ps.setInt(2, skin.getBaseProb());
                ps.setInt(3, skin.getBaseColor());
                ps.setInt(4, skin.getAddColor());
                ps.setInt(5, this.id);
                ps.setInt(6, 2);
                ps.execute();
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDummyToDB(final Connection con) {
        try {
            final PreparedStatement ps = con.prepareStatement("UPDATE characters SET choicepotential = ?, memorialcube = ?, returnscroll = ?, returnsc = ? WHERE id = ?", 1);
            if (this.choicepotential != null) {
                ps.setLong(1, this.choicepotential.getInventoryId());
            } else {
                ps.setLong(1, 0L);
            }
            if (this.memorialcube != null) {
                ps.setLong(2, this.memorialcube.getInventoryId());
            } else {
                ps.setLong(2, 0L);
            }
            if (this.returnscroll != null) {
                ps.setLong(3, this.returnscroll.getInventoryId());
            } else {
                ps.setLong(3, 0L);
            }
            ps.setInt(4, this.returnSc);
            ps.setInt(5, this.id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean saveCharToDB(final Connection con) {
        synchronized (this) {
            try {
                final PreparedStatement ps = con.prepareStatement("UPDATE characters SET level = ?, fame = ?, str = ?, dex = ?, luk = ?, `int` = ?, exp = ?, hp = ?, mp = ?, maxhp = ?, maxmp = ?, sp = ?, ap = ?, gm = ?, skincolor = ?, secondSkincolor = ?, gender = ?, secondgender = ?, job = ?, hair = ?, basecolor = ?, addcolor = ?, baseprob = ?, secondhair = ?, face = ?, secondface = ?, demonMarking = ?, map = ?, meso = ?, hpApUsed = ?, spawnpoint = ?, party = ?, buddyCapacity = ?, subcategory = ?, marriageId = ?, currentrep = ?, totalrep = ?, fatigue = ?, charm = ?, charisma = ?, craft = ?, insight = ?, sense = ?, will = ?, totalwins = ?, totallosses = ?, pvpExp = ?, pvpPoints = ?, reborns = ?, apstorage = ?, name = ?, honourExp = ?, honourLevel = ?, soulcount = ?, itcafetime = ?, pets = ?, LinkMobCount = ?, secondbasecolor = ?, secondaddcolor = ?, secondbaseprob = ?, mesochair = ?, betaclothes = ?, exceptionlist = ? WHERE id = ?", 1);
                ps.setInt(1, this.level);
                ps.setInt(2, this.fame);
                ps.setShort(3, this.stats.getStr());
                ps.setShort(4, this.stats.getDex());
                ps.setShort(5, this.stats.getLuk());
                ps.setShort(6, this.stats.getInt());
                ps.setLong(7, this.exp);
                ps.setLong(8, (this.stats.getHp() < 1L) ? 50L : this.stats.getHp());
                ps.setLong(9, this.stats.getMp());
                ps.setLong(10, this.stats.getMaxHp());
                ps.setLong(11, this.stats.getMaxMp());
                final StringBuilder sps = new StringBuilder();
                for (int i = 0; i < this.remainingSp.length; ++i) {
                    sps.append(this.remainingSp[i]);
                    sps.append(",");
                }
                String sp = sps.toString();
                ps.setString(12, sp.substring(0, sp.length() - 1));
                ps.setShort(13, this.remainingAp);
                ps.setByte(14, this.gmLevel);
                ps.setByte(15, this.skinColor);
                ps.setByte(16, this.secondSkinColor);
                ps.setByte(17, this.gender);
                ps.setByte(18, this.secondgender);
                ps.setShort(19, this.job);
                ps.setInt(20, this.hair);
                ps.setInt(21, this.basecolor);
                ps.setInt(22, this.addcolor);
                ps.setInt(23, this.baseprob);
                ps.setInt(24, this.secondhair);
                ps.setInt(25, this.face);
                ps.setInt(26, this.secondface);
                ps.setInt(27, this.demonMarking);
                if (this.map != null) {
                    if (this.map.getForcedReturnId() != 999999999 && this.map.getForcedReturnMap() != null) {
                        ps.setInt(28, this.map.getForcedReturnId());
                    } else {
                        ps.setInt(28, (this.stats.getHp() < 1L) ? this.map.getReturnMapId() : this.map.getId());
                    }
                } else {
                    ps.setInt(28, this.mapid);
                }
                ps.setLong(29, this.meso);
                ps.setShort(30, this.hpApUsed);
                if (this.map == null) {
                    ps.setByte(31, (byte) 0);
                } else {
                    final MaplePortal closest = this.map.findClosestSpawnpoint(this.getTruePosition());
                    ps.setByte(31, (byte) ((closest != null) ? closest.getId() : 0));
                }
                ps.setInt(32, (this.party == null) ? -1 : this.party.getId());
                ps.setShort(33, (short) ((this.buddylist == null) ? 20 : ((short) this.buddylist.getCapacity())));
                ps.setByte(34, this.subcategory);
                ps.setInt(35, this.marriageId);
                ps.setInt(36, this.currentrep);
                ps.setInt(37, this.totalrep);
                ps.setShort(38, this.fatigue);
                ps.setInt(39, this.traits.get(MapleTrait.MapleTraitType.charm).getTotalExp());
                ps.setInt(40, this.traits.get(MapleTrait.MapleTraitType.charisma).getTotalExp());
                ps.setInt(41, this.traits.get(MapleTrait.MapleTraitType.craft).getTotalExp());
                ps.setInt(42, this.traits.get(MapleTrait.MapleTraitType.insight).getTotalExp());
                ps.setInt(43, this.traits.get(MapleTrait.MapleTraitType.sense).getTotalExp());
                ps.setInt(44, this.traits.get(MapleTrait.MapleTraitType.will).getTotalExp());
                ps.setInt(45, this.totalWins);
                ps.setInt(46, this.totalLosses);
                ps.setInt(47, this.pvpExp);
                ps.setInt(48, this.pvpPoints);
                ps.setInt(49, this.reborns);
                ps.setInt(50, this.apstorage);
                ps.setString(51, this.name);
                ps.setInt(52, this.honourExp);
                ps.setInt(53, this.honorLevel);
                ps.setInt(54, 0);
                ps.setInt(55, this.itcafetime);
                sps.delete(0, sps.toString().length());
                for (int j = 0; j < 3; ++j) {
                    if (this.pets[j] != null) {
                        sps.append(this.pets[j].getUniqueId());
                    } else {
                        sps.append("-1");
                    }
                    sps.append(",");
                }
                sp = sps.toString();
                ps.setString(56, sp.substring(0, sp.length() - 1));
                ps.setInt(57, this.LinkMobCount);
                ps.setInt(58, this.secondbasecolor);
                ps.setInt(59, this.secondaddcolor);
                ps.setInt(60, this.secondbaseprob);
                ps.setInt(61, this.MesoChairCount);
                ps.setInt(62, this.betaclothes);
                final StringBuilder str = new StringBuilder();
                for (final Integer excep : this.getExceptionList()) {
                    sps.append(excep);
                    sps.append(",");
                }
                final String exp = str.toString();
                if (exp.length() > 0) {
                    ps.setString(63, exp.substring(0, exp.length() - 1));
                } else {
                    ps.setString(63, exp);
                }
                ps.setInt(64, this.id);
                if (ps.executeUpdate() < 1) {
                    ps.close();
                    return false;
                }
                ps.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public void savePetToDB(final Connection con) {
        for (int i = 0; i < this.pets.length; ++i) {
            if (this.pets[i] != null) {
                this.pets[i].saveToDb(con);
            }
        }
    }

    public void saveMatrixToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM matrix WHERE charid = ?");
            for (final VMatrix matrix : this.matrixs) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO matrix (`level`, `position`, `id`, `unlock`, `charid`) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, matrix.getLevel());
                ps.setInt(2, matrix.getPosition());
                ps.setInt(3, matrix.getId());
                ps.setByte(4, (byte) (matrix.isUnLock() ? 1 : 0));
                ps.setInt(5, this.id);
                ps.execute();
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCoreToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM core WHERE charid = ?");
            for (final Core core : this.cores) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO core (crcid, coreid, level, exp, state, maxlevel, skill1, skill2, skill3, position, islock, charid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setLong(1, core.getCrcId());
                ps.setInt(2, core.getCoreId());
                ps.setInt(3, core.getLevel());
                ps.setInt(4, core.getExp());
                ps.setInt(5, core.getState());
                ps.setInt(6, core.getMaxlevel());
                ps.setInt(7, core.getSkill1());
                ps.setInt(8, core.getSkill2());
                ps.setInt(9, core.getSkill3());
                ps.setInt(10, core.getPosition());
                ps.setInt(11, core.isLock() ? 1 : 0);
                ps.setInt(12, this.id);
                ps.execute();
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSteelToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM stolen WHERE characterid = ?");
            for (final Pair<Integer, Boolean> st : this.stolenSkills) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO stolen (characterid, skillid, chosen) VALUES (?, ?, ?)");
                ps.setInt(1, this.id);
                ps.setInt(2, st.left);
                ps.setInt(3, ((boolean) st.right) ? 1 : 0);
                ps.execute();
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMacroToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM skillmacros WHERE characterid = ?");
            for (int i = 0; i < 5; ++i) {
                final SkillMacro macro = this.skillMacros[i];
                if (macro != null) {
                    final PreparedStatement ps = con.prepareStatement("INSERT INTO skillmacros (characterid, skill1, skill2, skill3, name, shout, position) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, this.id);
                    ps.setInt(2, macro.getSkill1());
                    ps.setInt(3, macro.getSkill2());
                    ps.setInt(4, macro.getSkill3());
                    ps.setString(5, macro.getName());
                    ps.setInt(6, macro.getShout());
                    ps.setInt(7, i);
                    ps.execute();
                    ps.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSlotToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM inventoryslot WHERE characterid = ?");
            final PreparedStatement ps = con.prepareStatement("INSERT INTO inventoryslot (characterid, `equip`, `use`, `setup`, `etc`, `cash`, `cody`) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, this.id);
            ps.setShort(2, this.getInventory(MapleInventoryType.EQUIP).getSlotLimit());
            ps.setShort(3, this.getInventory(MapleInventoryType.USE).getSlotLimit());
            ps.setShort(4, this.getInventory(MapleInventoryType.SETUP).getSlotLimit());
            ps.setShort(5, this.getInventory(MapleInventoryType.ETC).getSlotLimit());
            ps.setShort(6, this.getInventory(MapleInventoryType.CASH).getSlotLimit());
            ps.setShort(7, this.getInventory(MapleInventoryType.CODY).getSlotLimit());
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveQuestInfoToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM questinfo WHERE characterid = ?");
            final PreparedStatement ps = con.prepareStatement("INSERT INTO questinfo (`characterid`, `quest`, `customData`) VALUES (?, ?, ?)");
            ps.setInt(1, this.id);
            for (final Map.Entry<Integer, String> q : this.questinfo.entrySet()) {
                ps.setInt(2, q.getKey());
                ps.setString(3, q.getValue());
                ps.execute();
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveQuestStatusToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM queststatus WHERE characterid = ?");
            final PreparedStatement ps = con.prepareStatement("INSERT INTO queststatus (`queststatusid`, `characterid`, `quest`, `status`, `time`, `forfeited`, `customData`) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", 1);
            ps.setInt(1, this.id);
            for (final MapleQuestStatus q : this.quests.values()) {
                ps.setInt(2, q.getQuest().getId());
                ps.setInt(3, q.getStatus());
                ps.setInt(4, (int) (q.getCompletionTime() / 1000L));
                ps.setInt(5, q.getForfeited());
                ps.setString(6, q.getCustomData());
                ps.execute();
                final ResultSet rs = ps.getGeneratedKeys();
                if (q.hasMobKills()) {
                    rs.next();
                    for (final int mob : q.getMobKills().keySet()) {
                        final PreparedStatement pse = con.prepareStatement("INSERT INTO queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
                        pse.setInt(1, rs.getInt(1));
                        pse.setInt(2, mob);
                        pse.setInt(3, q.getMobKills(mob));
                        pse.execute();
                        pse.close();
                    }
                }
                rs.close();
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSkillToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM skills WHERE characterid = ?");
            final PreparedStatement ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, this.id);
            for (final Map.Entry<Skill, SkillEntry> skill : this.skills.entrySet()) {
                if (GameConstants.isApplicableSkill(skill.getKey().getId()) || SkillFactory.getSkill(skill.getKey().getId()).getName().startsWith("\uc4f8\ub9cc\ud55c")) {
                    ps.setInt(2, skill.getKey().getId());
                    ps.setInt(3, skill.getValue().skillevel);
                    ps.setByte(4, skill.getValue().masterlevel);
                    ps.setLong(5, skill.getValue().expiration);
                    ps.execute();
                }
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveInnerToDB(final Connection con) {
        try {
            if (this.innerSkills != null) {
                this.deleteWhereCharacterId(con, "DELETE FROM inner_ability_skills WHERE player_id = ?");
                final PreparedStatement ps = con.prepareStatement("INSERT INTO inner_ability_skills (player_id, skill_id, skill_level, max_level, rank) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, this.id);
                for (int i = 0; i < this.innerSkills.size(); ++i) {
                    ps.setInt(2, this.innerSkills.get(i).getSkillId());
                    ps.setInt(3, this.innerSkills.get(i).getSkillLevel());
                    ps.setInt(4, this.innerSkills.get(i).getMaxLevel());
                    ps.setInt(5, this.innerSkills.get(i).getRank());
                    ps.execute();
                }
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCSToDB(final Connection con) {
        try {
            final PreparedStatement ps = con.prepareStatement("UPDATE accounts SET nxCredit = ?, ACash = ?, mPoints = ? WHERE `id` = ?");
            ps.setInt(1, this.nxcredit);
            ps.setInt(2, 0);
            ps.setInt(3, this.maplepoints);
            ps.setInt(4, this.getAccountID());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCoodinationToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM coodination WHERE `playerid` = ?");
            final PreparedStatement ps = con.prepareStatement("INSERT INTO coodination (`playerid`, `position`, `gender`, `skin`, `face`, `hair`, `equip1`, `equip2`, `equip3`, `equip4`, `equip5`, `equip6`, `equip7`, `equip8`, `equip9`, `weaponstickerid`, `weaponid`, `subweaponid`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, this.id);
            for (int i = 0; i < this.coodination.size(); ++i) {
                final AvatarLook a = this.coodination.get(i);
                if (a != null) {
                    a.save(i, ps);
                    ps.execute();
                }
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveKeyValueToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM keyvalue WHERE `id` = ?");
            final PreparedStatement ps = con.prepareStatement("INSERT INTO keyvalue (`id`, `key`, `value`) VALUES (?, ?, ?)");
            ps.setInt(1, this.id);
            for (final Map.Entry<String, String> keyValue : this.keyValues.entrySet()) {
                ps.setString(2, keyValue.getKey());
                ps.setString(3, keyValue.getValue());
                ps.execute();
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCooldownToDB(final Connection con, final boolean dc) {
        try {
            final List<MapleCoolDownValueHolder> cd = this.getCooldowns();
            this.deleteWhereCharacterId(con, "DELETE FROM skills_cooldowns WHERE charid = ?");
            if (dc && cd.size() > 0) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO skills_cooldowns (charid, SkillID, StartTime, length) VALUES (?, ?, ?, ?)");
                ps.setInt(1, this.getId());
                for (final MapleCoolDownValueHolder cooling : cd) {
                    ps.setInt(2, cooling.skillId);
                    ps.setLong(3, cooling.startTime);
                    ps.setLong(4, cooling.length);
                    ps.execute();
                }
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveRockToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM savedlocations WHERE characterid = ?");
            final PreparedStatement ps = con.prepareStatement("INSERT INTO savedlocations (characterid, `locationtype`, `map`) VALUES (?, ?, ?)");
            ps.setInt(1, this.id);
            for (final SavedLocationType savedLocationType : SavedLocationType.values()) {
                if (this.savedLocations[savedLocationType.getValue()] != -1) {
                    ps.setInt(2, savedLocationType.getValue());
                    ps.setInt(3, this.savedLocations[savedLocationType.getValue()]);
                    ps.execute();
                }
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveExtendedSlotsToDB(final Connection con) {
        try {
            if (this.changed_extendedSlots) {
                this.deleteWhereCharacterId(con, "DELETE FROM extendedSlots WHERE characterid = ?");
                for (final int i : this.extendedSlots) {
                    if (this.getInventory(MapleInventoryType.ETC).findById(i) != null) {
                        final PreparedStatement ps = con.prepareStatement("INSERT INTO extendedSlots(characterid, itemId) VALUES(?, ?) ");
                        ps.setInt(1, this.getId());
                        ps.setInt(2, i);
                        ps.execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBuddyToDB(final Connection con) {
        try {
            deleteWhereCharacterId(con, "DELETE FROM buddies WHERE accid = ? AND pending = 0", this.client.getAccID());
            final PreparedStatement ps = con.prepareStatement("INSERT INTO buddies (accid, `buddyaccid`, `repname`, `pending`, `groupname`, `memo`) VALUES (?, ?, ?, 0, ?, ?)");
            ps.setInt(1, this.client.getAccID());
            for (final BuddylistEntry entry : this.buddylist.getBuddies()) {
                if (entry.isVisible()) {
                    ps.setInt(2, entry.getAccountId());
                    ps.setString(3, entry.getRepName());
                    ps.setString(4, entry.getGroupName());
                    ps.setString(5, (entry.getMemo() == null) ? "" : entry.getMemo());
                    ps.execute();
                }
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveEmoticonToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM emoticon WHERE charid = ?");
            for (final MapleChatEmoticon em : this.emoticonTabs) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO emoticon (charid, emoticonid, time, bookmarks) VALUES (?, ?, ?, ?)");
                ps.setInt(1, this.id);
                ps.setInt(2, em.getEmoticonid());
                ps.setLong(3, em.getTime());
                ps.setString(4, em.getBookmark());
                ps.execute();
                ps.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveSavedEmoticonToDB(final Connection con) {
        try {
            this.deleteWhereCharacterId(con, "DELETE FROM emoticon_saved WHERE charid = ?");
            for (final MapleSavedEmoticon em : this.savedEmoticon) {
                final PreparedStatement ps = con.prepareStatement("INSERT INTO emoticon_saved (charid, emoticonid, chat) VALUES (?, ?, ?)");
                ps.setInt(1, this.id);
                ps.setInt(2, em.getEmoticonid());
                ps.setString(3, em.getText());
                ps.execute();
                ps.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteWhereCharacterId(final Connection con, final String sql) throws SQLException {
        deleteWhereCharacterId(con, sql, this.id);
    }

    public static void deleteWhereCharacterId(final Connection con, final String sql, final int id) throws SQLException {
        final PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }

    public void BlackMage3thDamage() {
        this.addHP(-this.getStat().getMaxHp());
        this.getMap().broadcastMessage(CField.EffectPacket.showFieldSkillEffect(this, 100006, 2));
        this.getMap().broadcastMessage(CField.EffectPacket.showFieldSkillEffect(this, -593945, (byte) 0));
    }

    public void saveInventory(Connection con, boolean dc) throws SQLException {
        for (MapleInventoryType type : MapleInventoryType.values()) {
            if (type.getType() < 2 || type.getType() >= 6) {
                continue;
            }
            MapleInventory mapleInventory = this.getInventory(type.getType());
            synchronized (mapleInventory) {
                List<Item> items = this.getInventory(type.getType()).newList();
                if (this.memorialcube != null && type.getType() == 5) {
                    items.add(this.memorialcube);
                }
                if (this.returnscroll != null && type.getType() == 2) {
                    items.add(this.returnscroll);
                }
                if (con != null) {
                    ItemLoader.INVENTORY.saveItems(items, con, this.id, type, dc);
                } else {
                    ItemLoader.INVENTORY.saveItems(items, this.id, type, dc);
                }
            }
        }
        ArrayList<Item> equips = new ArrayList<Item>();
        for (Item item : this.getInventory(MapleInventoryType.EQUIP).newList()) {
            equips.add(item);
        }
        for (Item item : this.getInventory(MapleInventoryType.EQUIPPED).newList()) {
            equips.add(item);
        }
        if (this.choicepotential != null) {
            equips.add(this.choicepotential);
        }
        if (con != null) {
            ItemLoader.INVENTORY.saveItems(equips, con, this.id, MapleInventoryType.EQUIP, dc);
        } else {
            ItemLoader.INVENTORY.saveItems(equips, this.id, MapleInventoryType.EQUIP, dc);
        }
        equips.clear();
        for (Item item : this.getInventory(MapleInventoryType.CODY).newList()) {
            equips.add(item);
        }
        if (con != null) {
            ItemLoader.INVENTORY.saveItems(equips, con, this.id, MapleInventoryType.CODY, dc);
        } else {
            ItemLoader.INVENTORY.saveItems(equips, this.id, MapleInventoryType.CODY, dc);
        }
    }

    public void saveToDB(final boolean dc, final boolean fromcs) {
        Connection con = null;
        final ReentrantLock LockObj = new ReentrantLock();
        LockObj.lock();
        try {
            con = DatabaseConnection.getConnection();
            con.setTransactionIsolation(1);
            con.setAutoCommit(false);
            if (this.saveCharToDB(con)) {
                this.saveCoreToDB(con);
                this.saveMatrixToDB(con);
                this.saveQuestInfoToDB(con);
                this.saveQuestStatusToDB(con);
                this.saveSkillToDB(con);
                this.saveInnerToDB(con);
                this.savePetToDB(con);
                this.saveSteelToDB(con);
                this.saveMacroToDB(con);
                this.saveSlotToDB(con);
                this.saveInventory(con, dc);
                this.saveCooldownToDB(con, dc);
                this.saveRockToDB(con);
                this.saveKeyValueToDB(con);
                if (fromcs) {
                    this.saveCoodinationToDB(con);
                }
                this.saveDummyToDB(con);
                this.saveMannequinToDB(con);
                this.saveEmoticonToDB(con);
                this.saveSavedEmoticonToDB(con);

                PlayerNPC.updateByCharId(this);
                this.keylayout.saveKeys(con, this.id);
                this.mount.saveMount(con, this.id);
            }
            if (this.getUnions() != null) {
                this.getUnions().savetoDB(con, this.getAccountID());
            }
            if (this.storage != null) {
                this.storage.saveToDB(con);
            }
            if (this.client != null) {
                this.saveBuddyToDB(con);
                this.saveCSToDB(con);
                this.client.saveKeyValueToDB(con);
                this.client.saveCustomDataToDB(con);
                this.client.saveCustomKeyValueToDB(con);
                this.client.SaveQuest(con);
            }
            con.commit();
            this.lastSaveTime = System.currentTimeMillis();
        } catch (DatabaseException es) {
            this.lastSaveTime = System.currentTimeMillis();
            FileoutputUtil.outputFileError("Log_Packet_Except.rtf", es);
            es.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e);
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    con.setTransactionIsolation(4);
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e2) {
                FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e2);
                e2.printStackTrace();
            }
            LockObj.unlock();
        }
    }

    public final PlayerStats getStat() {
        return this.stats;
    }

    public final void QuestInfoPacket(final MaplePacketLittleEndianWriter mplew) {
        mplew.writeShort(this.questinfo.size() + this.getClient().getCustomKeyValue().size());
        for (final Map.Entry<Integer, String> q : this.questinfo.entrySet()) {
            mplew.writeInt(q.getKey());
            mplew.writeMapleAsciiString((q.getValue() == null) ? "" : q.getValue());
        }
        for (final Map.Entry<Integer, String> q : this.getClient().getCustomKeyValue().entrySet()) {
            mplew.writeInt(q.getKey());
            mplew.writeMapleAsciiString((q.getValue() == null) ? "" : q.getValue());
        }
    }

    public final void specialQustInfoPacket(final MaplePacketLittleEndianWriter mplew) {
        final Map<Integer, String> customQuestInfo = new HashMap<Integer, String>();
        customQuestInfo.put(15, "count=" + this.client.getKeyValue("dailyGiftComplete") + ";day=" + this.client.getKeyValue("dailyGiftDay") + ";date=" + this.getKeyValue(16700, "date"));
        mplew.writeInt(customQuestInfo.size());
        for (final Map.Entry<Integer, String> q : customQuestInfo.entrySet()) {
            mplew.writeInt(q.getKey());
            mplew.writeMapleAsciiString((q.getValue() == null) ? "" : q.getValue());
        }
    }

    public final void updateInfoQuest(final int questid, final String data) {
        this.questinfo.put(questid, data);
        this.changed_questinfo = true;
        this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.updateInfoQuest(questid, data));
    }

    public final String getInfoQuest(final int questid) {
        if (this.questinfo.containsKey(questid)) {
            return this.questinfo.get(questid);
        }
        return "";
    }

    public final int getNumQuest() {
        int i = 0;
        for (final MapleQuestStatus q : this.quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom()) {
                ++i;
            }
        }
        return i;
    }

    public final byte getQuestStatus(final int quest) {
        final MapleQuest qq = MapleQuest.getInstance(quest);
        if (this.getQuestNoAdd(qq) == null) {
            return 0;
        }
        return this.getQuestNoAdd(qq).getStatus();
    }

    public final MapleQuestStatus getQuest(final MapleQuest quest) {
        if (!this.quests.containsKey(quest)) {
            return new MapleQuestStatus(quest, 0);
        }
        return this.quests.get(quest);
    }

    public final void setQuestAdd(final MapleQuest quest, final byte status, final String customData) {
        if (!this.quests.containsKey(quest)) {
            final MapleQuestStatus stat = new MapleQuestStatus(quest, status);
            stat.setCustomData(customData);
            this.quests.put(quest, stat);
        }
    }

    public final MapleQuestStatus getQuestNAdd(final MapleQuest quest) {
        if (!this.quests.containsKey(quest)) {
            final MapleQuestStatus status = new MapleQuestStatus(quest, 0);
            this.quests.put(quest, status);
            return status;
        }
        return this.quests.get(quest);
    }

    public final MapleQuestStatus getQuestNoAdd(final MapleQuest quest) {
        if (!this.quests.containsKey(quest)) {
            return null;
        }
        return this.quests.get(quest);
    }

    public final void updateQuest(final MapleQuestStatus quest) {
        this.updateQuest(quest, false);
    }

    public final void updateQuest(final MapleQuestStatus quest, final boolean update) {
        this.quests.put(quest.getQuest(), quest);
        if (update) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.updateQuest(quest));
        }
    }

    public final Map<Integer, String> getInfoQuest_Map() {
        return this.questinfo;
    }

    public final Map<MapleQuest, MapleQuestStatus> getQuest_Map() {
        return this.quests;
    }

    public SecondaryStatEffect getBuffedEffect(final SecondaryStat effect) {
        return this.getBuffedEffect(effect, this.getBuffSource(effect));
    }

    public boolean checkBuffStat(final SecondaryStat stat) {
        if (stat == null) {
            return false;
        }
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> eff : this.effects) {
            if (stat == eff.left) {
                return true;
            }
        }
        return false;
    }

    public SecondaryStatValueHolder checkBuffStatValueHolder(final SecondaryStat stat) {
        if (stat == null || this.effects == null) {
            return null;
        }
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> eff : this.effects) {
            if (stat == eff.left) {
                return eff.right;
            }
        }
        return null;
    }

    public SecondaryStatValueHolder checkBuffStatValueHolder(final SecondaryStatEffect ef, final Map.Entry<SecondaryStat, Pair<Integer, Integer>> stat) {
        if (stat == null || this.effects == null) {
            return null;
        }
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> eff : this.effects) {
            if (stat.getKey() == eff.getLeft() && ef.getSourceId() == eff.getRight().effect.getSourceId()) {
                return eff.right;
            }
        }
        return null;
    }

    public SecondaryStatValueHolder checkBuffStatValueHolder(final SecondaryStat stat, final int skillId) {
        if (stat == null || this.effects == null) {
            return null;
        }
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> eff : this.effects) {
            if (stat == eff.left && skillId == eff.right.effect.getSourceId()) {
                return eff.right;
            }
        }
        return null;
    }

    public SecondaryStatEffect getBuffedEffect(final int skillId) {
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> eff : this.effects) {
            if (skillId == eff.right.effect.getSourceId()) {
                return eff.right.effect;
            }
        }
        return null;
    }

    public SecondaryStatEffect getBuffedEffect(final SecondaryStat effect, final int skillid) {
        if (!this.checkBuffStat(effect)) {
            return null;
        }
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect, skillid);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect;
    }

    public Integer getBuffedValue(final SecondaryStat effect) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        return (mbsvh == null) ? null : Integer.valueOf(mbsvh.value);
    }

    public Integer getBuffedValue(final SecondaryStat effect, final int skillId) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect, skillId);
        return (mbsvh == null) ? null : Integer.valueOf(mbsvh.value);
    }

    public int getBuffedSkill(final SecondaryStat effect) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        return (mbsvh == null) ? 0 : mbsvh.value;
    }

    public boolean getBuffedValue(final int skillid) {
        if (SkillFactory.getSkill(skillid) == null) {
            return false;
        }
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> eff : this.effects) {
            if (skillid == eff.right.effect.getSourceId()) {
                return true;
            }
        }
        return false;
    }

    public final Integer getBuffedSkill_X(final SecondaryStat effect) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getX();
    }

    public final Integer getBuffedSkill_Y(final SecondaryStat effect) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getY();
    }

    public void setDressup(final boolean isdress) {
        this.isdressup = isdress;
    }

    public boolean getDressup() {
        return this.isdressup;
    }

    public int getBuffSource(final SecondaryStat stat) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(stat);
        return (mbsvh == null) ? 0 : mbsvh.effect.getSourceId();
    }

    public int getItemQuantity(final int itemid, final boolean checkEquipped) {
        int possesed = this.inventory[GameConstants.getInventoryType(itemid).ordinal()].countById(itemid);
        if (checkEquipped) {
            possesed += this.inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        return possesed;
    }

    public void setBuffedValue(final SecondaryStat effect, final int value) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        if (mbsvh == null) {
            return;
        }
        mbsvh.value = value;
        this.getStat().recalcLocalStats(this);
    }

    public void setBuffedValue(final SecondaryStat effect, final int skillid, final int value) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        if (mbsvh == null || !this.checkBuffStat(effect)) {
            return;
        }
        if (skillid == -1) {
            if (this.checkBuffStatValueHolder(effect) != null) {
                this.checkBuffStatValueHolder(effect).value = value;
            }
        } else if (mbsvh.effect.getSourceId() == skillid) {
            mbsvh.value = value;
        }
        this.getStat().recalcLocalStats(this);
    }

    public Long getBuffedStarttime(final SecondaryStat effect) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        return (mbsvh == null) ? null : Long.valueOf(mbsvh.startTime);
    }

    public void doRecovery() {
        final SecondaryStatEffect bloodEffect = this.getBuffedEffect(SecondaryStat.Recovery);
        if (bloodEffect == null) {
            this.lastRecoveryTime = 0L;
            return;
        }
        this.prepareRecovery();
        if (this.stats.getHp() >= this.stats.getCurrentMaxHp()) {
            this.cancelEffectFromBuffStat(SecondaryStat.Recovery);
        } else {
            this.healHP(bloodEffect.getX());
        }
    }

    public final boolean canRecover(final long now) {
        return this.lastRecoveryTime > 0L && this.lastRecoveryTime + 5000L < now;
    }

    private void prepareRecovery() {
        this.lastRecoveryTime = System.currentTimeMillis();
    }

    public boolean canDOT(final long now) {
        return this.lastDOTTime > 0L && this.lastDOTTime + 8000L < now;
    }

    public boolean hasDOT() {
        return this.dotHP > 0;
    }

    public void doDOT() {
        this.addHP(-(this.dotHP * 4));
        this.dotHP = 0;
        this.lastDOTTime = 0L;
    }

    public long getNeededExp() {
        return GameConstants.getExpNeededForLevel(this.level);
    }

    public void registerEffect(final SecondaryStatEffect effect, final long starttime, final Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup, final boolean silent, final int cid) {
        this.registerEffect(effect, starttime, statup, silent, cid, new ArrayList<Pair<Integer, Integer>>(), new ArrayList<Pair<Integer, Integer>>());
    }

    public void registerEffect(final SecondaryStatEffect effect, final long starttime, final Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup, final boolean silent, final int cid, final List<Pair<Integer, Integer>> list1, final List<Pair<Integer, Integer>> list2) {
        if (effect.isRecovery()) {
            this.prepareRecovery();
        } else if (effect.isMonsterRiding_()) {
            this.getMount().startSchedule();
        }
        final int value = statup.getValue().left;
        if (statup.getKey() != null && statup.getValue() != null) {
            this.effects.add(new Pair<SecondaryStat, SecondaryStatValueHolder>(statup.getKey(), new SecondaryStatValueHolder(effect, starttime, value, statup.getValue().right, cid, list1, list2)));
        } else {
            System.out.println("NULL EFFECT : " + effect.getSourceId());
        }
        if (!silent) {
            this.stats.recalcLocalStats(this);
        }
    }

    public void updateEffect(final SecondaryStatEffect effect, final SecondaryStat stat, final int addDuration) {
        if (stat == null || effect == null) {
            return;
        }
        final SecondaryStatValueHolder vh = this.checkBuffStatValueHolder(stat, effect.getSourceId());
        if (vh != null && this.getBuffedValue(stat, effect.getSourceId()) != null) {
            final SecondaryStatValueHolder secondaryStatValueHolder = vh;
            secondaryStatValueHolder.localDuration += addDuration;
            final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
            statups.put(stat, new Pair<Integer, Integer>(this.getBuffedValue(stat, effect.getSourceId()), (int) this.getBuffLimit(effect.getSourceId())));
            this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
        }
    }

    public void cancelBuffStat(int sourceId, List<Pair<SecondaryStat, SecondaryStatValueHolder>> statups, List<MapleSummon> summons, boolean overwrite) {
        int allSize = this.effects.size();
        this.effects.removeAll(statups);
        if (!overwrite && !statups.isEmpty()) {
            this.dropMessageGM(-8, "현재 버프 사이즈 : (" + allSize + " - " + statups.size() + " = " + this.effects.size() + ")");
            this.getStat().recalcLocalStats(this);
        }

        Iterator var6 = summons.iterator();

        while (var6.hasNext()) {
            MapleSummon remove = (MapleSummon) var6.next();
            if (!GameConstants.isAfterRemoveSummonSkill(sourceId) && remove.getLastAttackTime() <= 0L) {
                remove.removeSummon(this.map, false);
            }
        }

    }

    public void setAttackerSkill(final int attack) {
        this.setKeyValue(53714, "atk", attack + "");
        if (this.getBuffedEffect(80002924) != null) {
            this.autoDeregisterBuffStats(80002924, true);
        }
        SkillFactory.getSkill(80002924).getEffect(1).applyTo(this.getPlayer(), 0);
    }

    public void addAttackerSkill(final int attack) {
        if (this.getKeyValue(53714, "atk") == -1L) {
            this.setKeyValue(53714, "atk", "0");
        }
        final long k = this.getKeyValue(53714, "atk");
        this.setKeyValue(53714, "atk", attack + k + "");
        if (this.getBuffedEffect(80002924) != null) {
            this.autoDeregisterBuffStats(80002924, true);
        }
        SkillFactory.getSkill(80002924).getEffect(1).applyTo(this.getPlayer(), 0);
    }

    public Map<SecondaryStat, Pair<Integer, Integer>> autoDeregisterBuffStats(int sourceId, boolean overwrite) {
        HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        ArrayList<Pair<SecondaryStat, SecondaryStatValueHolder>> removes = new ArrayList<Pair<SecondaryStat, SecondaryStatValueHolder>>();
        ArrayList<MapleSummon> toRemove = new ArrayList<MapleSummon>();
        this.dropMessageGM(6, "cancelAutoBuff : " + sourceId);
        if (sourceId == 33001007) {
            sourceId = GameConstants.getSelectJaguarSkillId(GameConstants.getMountItem(33001001, this));
        }
        for (Pair<SecondaryStat, SecondaryStatValueHolder> effect : this.effects) {
            List<MapleSummon> summon;
            SecondaryStatValueHolder vh = (SecondaryStatValueHolder) effect.right;
            if (vh != null && vh.effect.getSourceId() == sourceId) {
                removes.add(new Pair(effect.left, vh));
                statups.put((SecondaryStat) effect.left, new Pair<Integer, Integer>(vh.value, vh.localDuration));
            }
            if (GameConstants.isAfterRemoveSummonSkill(sourceId) || sourceId == 400021047 || (summon = this.getSummons(sourceId)) == null) {
                continue;
            }
            toRemove.addAll(summon);
        }
        this.cancelBuffStat(sourceId, removes, toRemove, overwrite);
        return statups;
    }

    public Map<SecondaryStat, Pair<Integer, Integer>> deregisterBuffStats(int sourceId, List<SecondaryStat> stats, boolean overwrite, boolean auto) {
        HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        ArrayList<Pair<SecondaryStat, SecondaryStatValueHolder>> removes = new ArrayList<Pair<SecondaryStat, SecondaryStatValueHolder>>();
        ArrayList<MapleSummon> toRemove = new ArrayList<MapleSummon>();
        this.dropMessageGM(6, "cancelBuff : " + sourceId);
        if (sourceId == 33001007) {
            sourceId = GameConstants.getSelectJaguarSkillId(GameConstants.getMountItem(33001001, this));
        }
        for (Pair<SecondaryStat, SecondaryStatValueHolder> effect : this.effects) {
            List<MapleSummon> summon;
            SecondaryStatValueHolder vh = (SecondaryStatValueHolder) effect.right;
            if ((stats.contains(effect.left) || vh != null && ((SecondaryStat) effect.left).isStacked() && vh.effect.getSourceId() == sourceId && !overwrite && !auto) && vh.effect.getSourceId() == sourceId) {
                removes.add(new Pair(effect.left, vh));
                statups.put((SecondaryStat) effect.left, new Pair<Integer, Integer>(vh.value, vh.localDuration));
            }
            if (GameConstants.isAfterRemoveSummonSkill(sourceId) || sourceId == 400021047 || (summon = this.getSummons(sourceId)) == null) {
                continue;
            }
            toRemove.addAll(summon);
        }
        this.cancelBuffStat(sourceId, removes, toRemove, overwrite);
        return statups;
    }

    /*public Map<SecondaryStat, Pair<Integer, Integer>> deregisterBuffStats(int sourceId, SecondaryStat effect) {
        Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();

        List<MapleSummon> toRemove = new ArrayList<>();

        //autoCancel이면 지속시간 지난 애들만, autoCancel아니면 해당 스킬 ID에 해당하는 모든 버프 전부 다.
        dropMessageGM(6, "cancelBuff : " + sourceId);
        for (Pair<SecondaryStat,SecondaryStatValueHolder> eff : effects) {
            if (eff.right.effect.getSourceId() == sourceId) {
//                if ((autoCancel && (time - eff.right.startTime >= eff.right.localDuration)) || !autoCancel) {
                dropMessageGM(-8, "addCancelBuff : " + eff.left.name());
                statups.put(eff.left, new Pair<>(eff.right.value, eff.right.localDuration));

                if (eff.right.schedule != null && !eff.right.schedule.isCancelled()) {
                    eff.right.schedule.cancel(false);
                }

                MapleSummon summon = getSummon(effect.getSourceId());
                if (summon != null) {
                    toRemove.add(summon);
                    dropMessageGM(-8, "Add CancelSummon : " + effect.getSourceId());
                }
//                }
            } else {
                if (effect.getSourceId() == 33001007 || effect.getSourceId() == 400031005) {
                    for (int z = 33001007; z <= 33001015; ++z) {
                        if (eff.right.effect.getSourceId() == z) {
                            dropMessageGM(-8, "addCancelBuff : " + eff.left.name());
                            statups.put(eff.left, new Pair<>(eff.right.value, eff.right.localDuration));

                            if (eff.right.schedule != null && !eff.right.schedule.isCancelled()) {
                                eff.right.schedule.cancel(false);
                            }

                            MapleSummon summon = getSummon(z);
                            if (summon != null) {
                                toRemove.add(summon);
                                dropMessageGM(-8, "Add CancelSummon : " + z);
                            }
                        }
                    }
                }
            }
        }

        cancelBuffStat(effect, statups, toRemove);

        return statups;
    }*/
    public void cancelEffect(final SecondaryStatEffect effect) {
        this.cancelEffect(effect, null, false);
    }

    public void cancelEffect(final SecondaryStatEffect effect, final List<SecondaryStat> stats) {
        this.cancelEffect(effect, stats, false);
    }

    // 얘 체크
    public void cancelEffect(final SecondaryStatEffect effect, final List<SecondaryStat> stats, final boolean overwrite) {
        this.cancelEffect(effect, stats, overwrite, false);
    }

    // 버프 해제될 때 호출
    public void cancelEffect(final SecondaryStatEffect effect, final List<SecondaryStat> stats, final boolean overwrite, final boolean auto) {
        if (effect == null) {
            return;
        }
        Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();

        if (stats == null || stats.isEmpty()) {
            statups = this.autoDeregisterBuffStats(effect.getSourceId(), overwrite);
        } else {
            statups = this.deregisterBuffStats(effect.getSourceId(), stats, overwrite, auto);
        }

        if (effect.isMonsterRiding()) {
            statups.put(SecondaryStat.RideVehicle, new Pair<Integer, Integer>(SecondaryStatEffect.parseMountInfo(this, effect.getSourceId()), effect.getDuration()));
        }
        if (effect.getSourceId() == 12101024) {
            this.setIgnition(0);
        }
        // 블리츠 실드
        if (effect.getSourceId() == 400001010) {
            final List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
            this.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400001011, mobList, true, 0, new int[0]));
        }
        if (effect.getSourceId() == 80002633) {
            final List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
            this.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(80002634, mobList, true, 0, new int[0]));
        }
        if (effect.isMagicDoor()) {
            if (!this.getDoors().isEmpty()) {
                this.removeDoor();
                this.silentPartyUpdate();
            }
        } else if (effect.isMechDoor()) {
            if (!this.getMechDoors().isEmpty()) {
                this.removeMechDoor();
            }
        } else if (statups.containsKey(SecondaryStat.Reincarnation)) {
            if (this.getReinCarnation() <= 0) {
                this.changeCooldown(1320019, -SkillFactory.getSkill(1320019).getEffect(this.getSkillLevel(1320019)).getY() * 1000);
            }
            this.setReinCarnation(0);
        } else if (effect.isMonsterRiding_()) {
            this.getMount().cancelSchedule();
        } else if (statups.containsKey(SecondaryStat.BulletParty)) {
            this.bulletParty = 0;
        } else if (effect.getSourceId() == 400041029) {
            this.setXenonSurplus((short) ((this.getXenonSurplus() >= 20) ? 20 : this.getXenonSurplus()), SkillFactory.getSkill(30020232));
        } else if (effect.getSourceId() == 400051002) {
            this.transformEnergyOrb = 0;
        } else if (statups.containsKey(SecondaryStat.ElementalCharge) && !overwrite) {
            this.elementalCharge = 0;
            this.lastElementalCharge = 0;
            if (this.getSkillLevel(400011052) > 0 && this.getBuffedValue(SecondaryStat.BlessedHammer) != null) {
                this.cancelEffectFromBuffStat(SecondaryStat.BlessedHammer);
            }
            if (this.getSkillLevel(400011053) > 0 && this.getBuffedValue(SecondaryStat.BlessedHammer2) != null) {
                this.cancelEffectFromBuffStat(SecondaryStat.BlessedHammer2);
            }
        } else if (effect.getSourceId() == 400011052 && !overwrite) {
            this.elementalCharge = 0;
            this.lastElementalCharge = 0;
            statups.put(SecondaryStat.BlessedHammer, new Pair<Integer, Integer>(0, 0));
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
            this.getMap().broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(this, statups, effect), false);
        } else if (effect.getSourceId() == 400011053 && !overwrite) {
            this.elementalCharge = 0;
            this.lastElementalCharge = 0;
            statups.put(SecondaryStat.BlessedHammer2, new Pair<Integer, Integer>(0, 0));
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
            this.getMap().broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(this, statups, effect), false);
        }
        if (statups.containsKey(SecondaryStat.WeaponVariety) && !overwrite) {
            this.weaponChanges1.clear();
        }
        if (statups.containsKey(SecondaryStat.RideVehicle) && this.getSkillCustomValue(this.getMapId()) != null && this.getMapId() == ServerConstants.warpMap) {
            this.client.send(CField.UIPacket.detailShowInfo("휴식 포인트 적립을 그만둡니다.", 3, 20, 20));
            this.removeSkillCustomInfo(this.getMapId());
        }
        final Iterator<MapleSummon> summons1 = this.getMap().getAllSummonsThreadsafe().iterator();
        switch (effect.getSourceId()) {
            case 162121044: {
                if (this.getBuffedValue(162121043)) {
                    this.cancelEffect(this.getBuffedEffect(162121043));
                }
                for (final MapleMonster mob : this.getMap().getAllMonster()) {
                    if (mob.getBuff(162121043) != null) {
                        mob.cancelSingleStatus(mob.getBuff(162121043));
                    }
                }
                break;
            }
            case 162111002: {
                for (final SecondAtom sa : this.getMap().getAllSecondAtomsThread()) {
                    if (sa.getOwnerId() == this.getId() && sa.getSkillId() == effect.getSourceId()) {
                        this.getMap().broadcastMessage(CField.removeSecondAtom(this.getId(), sa.getObjectId()));
                        this.getMap().removeMapObject(sa);
                    }
                }
                break;
            }
            case 162101000: {
                for (final SecondAtom sa : this.SaList) {
                    this.client.send(CField.removeSecondAtom(this.getId(), sa.getObjectId()));
                }
                this.SaList.clear();
                this.removeSkillCustomInfo(9877654);
                break;
            }
            case 400001025:
            case 400001026:
            case 400001027:
            case 400001028:
            case 400001029:
            case 400001030: {
                if (!overwrite) {
                    final SecondaryStatEffect effz = SkillFactory.getSkill(400001024).getEffect(this.getSkillLevel(400001024));
                    this.client.send(CField.skillCooldown(effz.getSourceId(), effz.getY() * 1000));
                    this.addCooldown(effz.getSourceId(), System.currentTimeMillis(), effz.getY() * 1000);
                    break;
                }
                break;
            }
            case 400011010: {
                if (!this.skillisCooling(effect.getSourceId()) && !overwrite) {
                    this.client.send(CField.skillCooldown(effect.getSourceId(), SkillFactory.getSkill(GameConstants.getLinkedSkill(effect.getSourceId())).getEffect(this.getSkillLevel(effect.getSourceId())).getZ() * 1000));
                    this.addCooldown(effect.getSourceId(), System.currentTimeMillis(), SkillFactory.getSkill(GameConstants.getLinkedSkill(effect.getSourceId())).getEffect(this.getSkillLevel(effect.getSourceId())).getZ() * 1000);
                    break;
                }
                break;
            }
            case 80003046: {
                this.getClient().send(CField.UIPacket.closeUI(1297));
                this.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062524, 0));
                this.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062525, 0));
                this.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062526, 0));
                break;
            }
            case 80001733: {//싸전귀
                if (this.getBattleGroundChr() != null) {
                    this.getBattleGroundChr().setAttackSpeed(this.getBattleGroundChr().getAttackSpeed() + 60);
                    this.getBattleGroundChr().setTeam(2);
                    this.getMap().broadcastMessage(this, BattleGroundPacket.UpdateAvater(this.getBattleGroundChr(), GameConstants.BattleGroundJobType(this.getBattleGroundChr())), false);
                    this.getBattleGroundChr().setTeam(1);
                    this.getClient().send(BattleGroundPacket.UpdateAvater(this.getBattleGroundChr(), GameConstants.BattleGroundJobType(this.getBattleGroundChr())));
                    break;
                }
                break;
            }
            case 400011015: {
                this.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400011025, Arrays.asList(new RangeAttack(400011025, this.getPosition(), 0, 0, 7))));
                break;
            }
            case 400001045: {
                if (this.getBuffedValue(100000276)) {
                    this.cancelEffect(this.getBuffedEffect(100000276));
                    this.RapidTimeDetect = 10;
                    SkillFactory.getSkill(100000276).getEffect(this.getSkillLevel(100000276)).applyTo(this);
                }
                if (this.getBuffedValue(100000277)) {
                    this.cancelEffect(this.getBuffedEffect(100000277));
                    this.RapidTimeStrength = 10;
                    SkillFactory.getSkill(100000277).getEffect(this.getSkillLevel(100000277)).applyTo(this);
                }
                if (this.getBuffedValue(101120109)) {
                    this.cancelEffect(this.getBuffedEffect(101120109));
                    SkillFactory.getSkill(101120109).getEffect(this.getSkillLevel(101120109)).applyTo(this);
                    break;
                }
                break;
            }
            case 65121101: {
                if (!overwrite) {
                    this.removeSkillCustomInfo(effect.getSourceId());
                    break;
                }
                break;
            }
            case 61111008:
            case 61120008:
            case 61121053: {
                if (this.getBuffedValue(SecondaryStat.StopForceAtominfo) != null) {
                    int[] skilllist;
                    int[] sa = skilllist = new int[]{61101002, 61110211, 61120007, 61121217};
                    int n = sa.length;
                    for (int i = 0; i < n; ++i) {
                        Integer skill = sa[i];
                        if (!this.getBuffedValue(skill)) {
                            continue;
                        }
                        this.cancelEffect(this.getBuffedEffect(skill));
                    }
                    if (effect.getSourceId() == 61111008) {
                        SkillFactory.getSkill(61110211).getEffect(this.getSkillLevel(61101002)).applyTo(this);
                    } else {
                        SkillFactory.getSkill(61120007).getEffect(this.getSkillLevel(61120007)).applyTo(this);
                    }
                }
                this.resetKaiserCombo();
                break;
            }
            case 51111008: {
                if (!overwrite && this.getParty() != null) {
                    for (final MaplePartyCharacter partychar : this.party.getMembers()) {
                        if (partychar != null && partychar.getMapid() == this.getMapId() && partychar.getChannel() == this.client.getChannel()) {
                            final MapleCharacter other = this.client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                            if (other == null || !other.getBuffedValue(effect.getSourceId())) {
                                continue;
                            }
                            other.cancelEffect(effect);
                        }
                    }
                    break;
                }
                break;
            }
            case 135001005: {
                if (!statups.containsKey(SecondaryStat.YetiAngerMode)) {
                    break;
                }
                this.removeSkillCustomInfo(135001005);
                SkillFactory.getSkill(135001005).getEffect(1).applyTo(this, false);
                if (this.getSkillCustomValue0(135001007) > 2L) {
                    this.setSkillCustomInfo(135001007, 2L, 0L);
                    SkillFactory.getSkill(135001007).getEffect(1).applyTo(this);
                    break;
                }
                break;
            }
            case 21111030: {
                if (!overwrite) {
                    this.setCombo((short) 800);
                    SkillFactory.getSkill(21000000).getEffect(this.getSkillLevel(21000000)).applyTo(this, false);
                    this.getClient().getSession().writeAndFlush((Object) CField.aranCombo(this.getCombo()));
                    break;
                }
                break;
            }
            case 152111003: {
                this.cancelEffect(this.getBuffedEffect(152000009), null, true);
                this.blessMark = 10;
                SkillFactory.getSkill(152000009).getEffect(this.getSkillLevel(152000009)).applyTo(this);
                SkillFactory.getSkill(152101000).getEffect(this.getSkillLevel(152101000)).applyTo(this);
                SkillFactory.getSkill(152101008).getEffect(this.getSkillLevel(152101008)).applyTo(this);
                break;
            }
            case 152121005: {
                SkillFactory.getSkill(152001003).getEffect(this.getSkillLevel(152001003)).applyTo(this);
                SkillFactory.getSkill(152101008).getEffect(this.getSkillLevel(152101008)).applyTo(this);
                while (summons1.hasNext()) {
                    final MapleSummon summon = summons1.next();
                    if (summon.getOwner().getId() == this.getId() && summon.getSkill() == 152121006) {
                        summon.removeSummon(this.getMap(), false);
                        final MapleSummon su2 = this.getSummon(summon.getSkill());
                        if (su2 == null) {
                            continue;
                        }
                        this.removeSummon(summon);
                    }
                }
                break;
            }
            case 152120014: {
                final MapleSummon summon = this.getSummon(152101000);
                if (summon != null) {
                    summon.setEnergy(0);
                    summon.getCrystalSkills().clear();
                    this.getMap().broadcastMessage(CField.SummonPacket.transformSummon(summon, 2));
                    this.getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon, 2));
                    this.getMap().broadcastMessage(CField.SummonPacket.specialSummon(summon, 3));
                    break;
                }
                break;
            }
            case 151101006: {
                for (int i = 1; i <= 6; ++i) {
                    this.getMap().broadcastMessage(SkillPacket.RemoveSubObtacle(this, i * 10));
                }
                break;
            }
            case 37121004: {
                for (final MapleMonster mob : this.getMap().getAllMonster()) {
                    if (mob.getBuff(37121004) != null) {
                        mob.cancelStatus(MonsterStatus.MS_Freeze, mob.getBuff(37121004));
                        break;
                    }
                }
                break;
            }
            case 35001002: {
                if (overwrite) {
                    statups.clear();
                    statups.put(SecondaryStat.IndieSpeed, new Pair<Integer, Integer>(30, 0));
                    statups.put(SecondaryStat.IndieBooster, new Pair<Integer, Integer>(-1, 0));
                    break;
                }
                break;
            }
            case 35111003: {
                if (overwrite) {
                    statups.clear();
                    statups.put(SecondaryStat.CriticalIncrease, new Pair<Integer, Integer>(1, 0));
                    break;
                }
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
            case 33001015:
            case 400031005: {
                while (summons1.hasNext()) {
                    final MapleSummon summon = summons1.next();
                    if (summon.getOwner().getId() == this.getId() && summon.getSkill() >= 33001007 && summon.getSkill() <= 33001015) {
                        final boolean remove = (effect.getSourceId() == 400031005) ? (summon.getSkill() != GameConstants.getSelectJaguarSkillId(GameConstants.getMountItem(33001001, this))) : (summon.getSkill() == GameConstants.getSelectJaguarSkillId(GameConstants.getMountItem(33001001, this)));
                        if (!remove) {
                            continue;
                        }
                        summon.removeSummon(this.getMap(), false);
                        final MapleSummon su3 = this.getSummon(summon.getSkill());
                        if (su3 == null) {
                            continue;
                        }
                        this.removeSummon(summon);
                    }
                }
                break;
            }
            case 400011012: {
                while (summons1.hasNext()) {
                    final MapleSummon summon = summons1.next();
                    if (summon.getOwner().getId() == this.getId() && (summon.getSkill() == 400011013 || summon.getSkill() == 400011014)) {
                        summon.removeSummon(this.getMap(), false);
                        final MapleSummon su2 = this.getSummon(summon.getSkill());
                        if (su2 == null) {
                            continue;
                        }
                        this.removeSummon(summon);
                    }
                }
                break;
            }
            case 400021087: {
                if (!overwrite) {
                    SkillFactory.getSkill(400021088).getEffect(this.getSkillLevel(400021087)).applyTo(this, false);
                    break;
                }
                break;
            }
            case 400011112: {
                if (this.getSkillCustomValue0(400011112) > 0L) {
                    this.setSkillCustomInfo(400011129, SkillFactory.getSkill(400011112).getEffect(this.getSkillLevel(400011112)).getU(), 0L);
                    SkillFactory.getSkill(400011129).getEffect(this.getSkillLevel(400011112)).applyTo(this, false);
                    break;
                }
                break;
            }
            case 27101003: {
                this.setBlessofDarkness((byte) 0);
                break;
            }
            case 400021105: {
                if (overwrite) {
                    this.removeSkillCustomInfo(400021105);
                    this.removeSkillCustomInfo(400021107);
                    this.removeSkillCustomInfo(400021108);
                    break;
                }
            }
            case 400011123: {
                if (!overwrite) {
                    for (final MapleMonster monster : this.getMap().getAllMonster()) {
                        final int size = (int) monster.getCustomValue0(400011121);
                        if (size > 0) {
                            monster.removeCustomInfo(400011121);
                        }
                        if (monster.getCustomValue(400011122) != null) {
                            monster.removeCustomInfo(400011122);
                        }
                    }
                    break;
                }
                break;
            }
            case 400011011: {
                if (this.getRhoAias() > 0 && !overwrite) {
                    SkillFactory.getSkill(400011011).getEffect(this.getSkillLevel(400011011)).applyTo(this, false);
                    this.setRhoAias(0);
                    break;
                }
                break;
            }
            case 15001022: {
                if (!overwrite && statups.containsKey(SecondaryStat.CygnusElementSkill)) {
                    while (this.getBuffedValue(effect.getSourceId())) {
                        this.cancelEffect(this.getBuffedEffect(effect.getSourceId()));
                    }
                    break;
                }
            }
            case 14121054: {
                final List<MapleSummon> sum = new ArrayList<MapleSummon>();
                while (summons1.hasNext()) {
                    final MapleSummon summon2 = summons1.next();
                    if (summon2.getOwner().getId() == this.getId() && (summon2.getSkill() == 14121054 || summon2.getSkill() == 14121055 || summon2.getSkill() == 14121056)) {
                        sum.add(summon2);
                    }
                }
                for (final MapleSummon s : sum) {
                    s.removeSummon(this.map, false);
                }
                break;
            }
            case 25121133: {
                if (!overwrite) {
                    try {
                        while (summons1.hasNext()) {
                            final MapleSummon summon = summons1.next();
                            if (summon.getOwner().getId() == this.getId() && summon.getSkill() == effect.getSourceId()) {
                                summon.removeSummon(this.getMap(), false);
                                final MapleSummon su2 = this.getSummon(summon.getSkill());
                                if (su2 == null) {
                                    continue;
                                }
                                this.removeSummon(summon);
                            }
                        }
                        if (effect.getSourceId() == 25121133) {
                            final List<MapleMapItem> items = this.getMap().getAllItemsThreadsafe();
                            for (final MapleMapItem j : items) {
                                if (j.getItemId() == 4001847) {
                                    j.expire(this.getMap());
                                }
                            }
                        }
                    } catch (Throwable t) {
                    }
                    break;
                }
                break;
            }
            case 5220023:
            case 5220024:
            case 5220025:
            case 5221022:
            case 12120013:
            case 12120014:
            case 14111024:
            case 35111002:
            case 152101000:
            case 162101003:
            case 162101006:
            case 162121012:
            case 162121015: {
                final List<MapleSummon> sum = new ArrayList<MapleSummon>();
                while (summons1.hasNext()) {
                    final MapleSummon summon2 = summons1.next();
                    if (summon2.getOwner().getId() == this.getId() && summon2.getSkill() == effect.getSourceId()) {
                        sum.add(summon2);
                    }
                }
                for (final MapleSummon s : sum) {
                    s.removeSummon(this.map, false);
                }
                break;
            }
            case 1211014:
            case 2221054:
            case 32001016:
            case 32101009:
            case 32111012:
            case 32121017:
            case 32121018:
            case 63121044:
            case 100001263:
            case 100001264:
            case 400021006: {
                if (this.getSkillCustomValue0(effect.getSourceId()) == this.getId() && this.getParty() != null) {
                    for (final MaplePartyCharacter partychar : this.party.getMembers()) {
                        if (partychar != null && partychar.getMapid() == this.getMapId() && partychar.getChannel() == this.client.getChannel()) {
                            final MapleCharacter other = this.client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                            if (other == null || !other.getBuffedValue(effect.getSourceId())) {
                                continue;
                            }
                            other.cancelEffect(effect);
                        }
                    }
                    break;
                }
                break;
            }
            case 63101005: {
                final List<MapleSecondAtom> remove2 = new ArrayList<MapleSecondAtom>();
                for (final MapleSecondAtom at : this.getMap().getAllSecondAtoms()) {
                    if (at.getSourceId() == 63101006 && at.getChr().getId() == this.getId()) {
                        remove2.add(at);
                    }
                }
                for (final MapleSecondAtom re : remove2) {
                    this.getMap().removeSecondAtom(this, re.getObjectId());
                }
                this.removeSkillCustomInfo(effect.getSourceId());
                this.removeSkillCustomInfo(effect.getSourceId() + 1);
                break;
            }
            case 400001012: {
                int skillid = 0;
                if (GameConstants.isBowMaster(this.job)) {
                    skillid = 3111005;
                } else if (GameConstants.isMarksMan(this.job)) {
                    skillid = 3211005;
                } else if (GameConstants.isPathFinder(this.job)) {
                    skillid = 3311009;
                }
                if (skillid > 0) {
                    SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)).applyTo(this);
                    break;
                }
                break;
            }
            case 2321054: {
                if (this.getBuffedEffect(400021032) == null && this.getBuffedEffect(400021033) == null) {
                    break;
                }
                for (final MapleSummon sum2 : this.getMap().getAllSummonsThreadsafe()) {
                    if (sum2.getOwner().getId() == this.getId() && (sum2.getSkill() == 400021032 || sum2.getSkill() == 400021033)) {
                        sum2.removeSummon(this.getMap(), false);
                        this.removeSummon(sum2);
                    }
                }
                if (this.getBuffedEffect(400021032) != null) {
                    final long bufftime = this.getBuffLimit(400021032);
                    while (this.getBuffedValue(400021032)) {
                        this.cancelEffect(this.getBuffedEffect(400021032));
                    }
                    SkillFactory.getSkill(400021033).getEffect(this.getSkillLevel(400021032)).applyTo(this, false, (int) bufftime);
                    break;
                }
                if (this.getBuffedEffect(400021033) != null) {
                    final long bufftime = this.getBuffLimit(400021033);
                    while (this.getBuffedValue(400021033)) {
                        this.cancelEffect(this.getBuffedEffect(400021033));
                    }
                    SkillFactory.getSkill(400021032).getEffect(this.getSkillLevel(400021032)).applyTo(this, false, (int) bufftime);
                    break;
                }
                break;
            }
            case 400011021: {
                final int[] array2;
                final int[] activeBuff = array2 = new int[]{1221015, 1221054};
                for (final int skillid2 : array2) {
                    if (this.getBuffedValue(skillid2)) {
                        this.cancelEffect(this.getBuffedEffect(skillid2));
                    }
                }
                break;
            }
            case 11121005: {
                if (overwrite) {
                    break;
                }
                if (this.getBuffedValue(11121012)) {
                    while (this.getBuffedValue(11121012)) {
                        this.cancelEffect(this.getBuffedEffect(11121012));
                    }
                    SkillFactory.getSkill(11101022).getEffect(this.getSkillLevel(11101022)).applyTo(this);
                    break;
                }
                if (this.getBuffedValue(11121011)) {
                    while (this.getBuffedValue(11121011)) {
                        this.cancelEffect(this.getBuffedEffect(11121011));
                    }
                    SkillFactory.getSkill(11111022).getEffect(this.getSkillLevel(11111022)).applyTo(this);
                    break;
                }
                break;
            }
            default: {
                if (GameConstants.isAfterRemoveSummonSkill(effect.getSourceId()) && !overwrite) {
                    final List<MapleSummon> sum = new ArrayList<MapleSummon>();
                    while (summons1.hasNext()) {
                        final MapleSummon summon2 = summons1.next();
                        if (summon2.getOwner().getId() == this.getId() && summon2.getSkill() == effect.getSourceId()) {
                            sum.add(summon2);
                        }
                    }
                    for (final MapleSummon s : sum) {
                        s.removeSummon(this.map, false);
                    }
                    break;
                }
                break;
            }
        }
        if (!overwrite) {
            if (statups.containsKey(SecondaryStat.Infinity)) {
                this.infinity = 0;
            }
            if (statups.containsKey(SecondaryStat.TimeFastABuff)) {
                this.RapidTimeDetect = 0;
            }
            if (statups.containsKey(SecondaryStat.TimeFastBBuff)) {
                this.RapidTimeStrength = 0;
            }
            if (statups.containsKey(SecondaryStat.ArcaneAim)) {
                this.arcaneAim = 0;
            }
            if (statups.containsKey(SecondaryStat.StackBuff)) {
                this.stackbuff = 0;
            }
            if (statups.containsKey(SecondaryStat.BlessMark)) {
                this.blessMark = 0;
            }
            if (statups.containsKey(SecondaryStat.RwBarrier) || statups.containsKey(SecondaryStat.PowerTransferGauge)) {
                this.barrier = 0;
            }
            if (statups.containsKey(SecondaryStat.OverloadCount)) {
                this.overloadCount = 0;
            }
            if (statups.containsKey(SecondaryStat.Exceed)) {
                this.exceed = 0;
            }
            if (statups.containsKey(SecondaryStat.GloryWing)) {
                this.canUseMortalWingBeat = false;
            }
            if (effect.getSourceId() == 2311009) {
                this.holyMagicShell = 0;
            }
            if (effect.getSourceId() == 3110012) {
                this.concentration = 0;
            }
            if (effect.getSourceId() == 4221054) {
                this.flip = 0;
            }
            if (statups.containsKey(SecondaryStat.UnityOfPower)) {
                this.unityofPower = 0;
            }
            if (statups.containsKey(SecondaryStat.BlitzShield)) {
                this.blitzShield = 0;
            }
            if (statups.containsKey(SecondaryStat.IgnoreTargetDEF)) {
                this.lightning = 0;
            }
            if (effect.getSourceId() == 15111022) {
                this.cancelEffectFromBuffStat(SecondaryStat.IndieDamR, 15120003);
            }
            if (effect.getSourceId() == 36121007) {
                this.getMap().removeMist(36121007);
            }
            if (effect.getSourceId() == 15120003) {
                this.cancelEffectFromBuffStat(SecondaryStat.IndieDamR, 15111022);
            }
            if (effect.getSourceId() == 51001005 && !overwrite) {
                boolean givebuff = false;
                --this.royalStack;
                if (this.royalStack > 0) {
                    givebuff = true;
                    SkillFactory.getSkill(51001005).getEffect(this.getSkillLevel(51001005)).applyTo(this, false, SkillFactory.getSkill(51001005).getEffect(this.getSkillLevel(51001005)).getX() * 1000);
                }
                if (this.getParty() != null) {
                    for (final MaplePartyCharacter chr1 : this.getParty().getMembers()) {
                        final MapleCharacter curChar = this.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr1.getId());
                        if (curChar != null && curChar.getBuffedValue(51111008)) {
                            curChar.cancelEffect(curChar.getBuffedEffect(51111008));
                            SkillFactory.getSkill(51111008).getEffect(this.getSkillLevel(51111008)).applyTo(this, curChar);
                        }
                    }
                }
                if (givebuff) {
                    return;
                }
            }
            if (effect.getSourceId() == 51111004 && this.getParty() != null) {
                for (final MaplePartyCharacter chr2 : this.getParty().getMembers()) {
                    final MapleCharacter curChar2 = this.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr2.getId());
                    if (curChar2 != null && curChar2.getBuffedValue(51111008)) {
                        curChar2.cancelEffectFromBuffStat(SecondaryStat.MichaelSoulLink);
                        SkillFactory.getSkill(51111008).getEffect(this.getSkillLevel(51111008)).applyTo(this, curChar2);
                    }
                }
            }
            if (effect.getSourceId() == 80002544 && !this.getBuffedValue(80002543)) {
                SkillFactory.getSkill(80002543).getEffect(1).applyTo(this, false);
            }
            if (effect.getSourceId() == 14120009) {
                this.siphonVitality = 0;
            }
            if (statups.containsKey(SecondaryStat.AdrenalinBoost)) {
                this.combo = 500;
            }
            if (effect.getSourceId() == 4221016) {
                this.shadowerDebuff = 0;
                this.shadowerDebuffOid = 0;
            }
            if (effect.getSourceId() == 80000268) {
                this.FlowofFight = 0;
            }
            if (effect.getSourceId() == 80000514) {
                this.LinkofArk = 0;
            }
            if (statups.containsKey(SecondaryStat.WeaponVariety)) {
                this.weaponChanges.clear();
            }
            if (statups.containsKey(SecondaryStat.Trinity)) {
                this.trinity = 0;
            }
            if (statups.containsKey(SecondaryStat.EnergyBurst)) {
                effect.applyTo(this, false);
                this.energyBurst = 0;
            }
            if (statups.containsKey(SecondaryStat.AntiMagicShell)) {
                this.antiMagicShell = 0;
            }
            if (statups.containsKey(SecondaryStat.EmpiricalKnowledge)) {
                this.empiricalKnowledge = null;
                this.empiricalStack = 0;
            }
            if (statups.containsKey(SecondaryStat.FightJazz)) {
                this.fightJazz = 0;
            }
            if (statups.containsKey(SecondaryStat.AdelResonance)) {
                this.adelResonance = 0;
            }
            if (statups.containsKey(SecondaryStat.WillofSwordStrike)) {
                this.ignoreDraco = 0;
            }
            if (effect.getSourceId() == 1301006) {
                this.cancelEffectFromBuffStat(SecondaryStat.DarknessAura);
            }
            if (effect.getSourceId() == 400021092 && !overwrite) {
                SkillFactory.getSkill(400021093).getEffect(effect.getLevel()).applyTo(this, false);
                this.removeSkillCustomInfo(400021092);
                this.removeSkillCustomInfo(400021093);
            }
            if (effect.getSourceId() == 400011047) {
                this.removeSkillCustomInfo(400011047);
                this.removeSkillCustomInfo(400011048);
                this.client.getSession().writeAndFlush((Object) CField.rangeAttack(400011047, Arrays.asList(new RangeAttack(400011085, this.getTruePosition(), 0, 1, 0))));
            }
            if (statups.containsKey(SecondaryStat.Revenant)) {
                SkillFactory.getSkill(400011129).getEffect(effect.getLevel()).applyTo(this, false);
            }
            if (statups.containsKey(SecondaryStat.RoyalKnights)) {
                effect.applyTo(this, false);
            }
            if (GameConstants.getLinkedSkill(effect.getSourceId()) == 400001024 && this.getCooldownLimit(400001024) == 0L) {
                this.addCooldown(400001024, System.currentTimeMillis(), 240000L);
                this.client.getSession().writeAndFlush((Object) CField.skillCooldown(400001024, 240000));
            }
        }
        boolean noPacket = false;
        switch (effect.getSourceId()) {
            case 20040216:
            case 20040217: {
                noPacket = true;
                break;
            }
            case 162120038:
            case 400011127: {
                if (overwrite) {
                    if (statups.containsKey(SecondaryStat.IndieBarrier)) {
                        noPacket = true;
                        break;
                    }
                    break;
                } else {
                    if (statups.containsKey(SecondaryStat.IndieBarrier)) {
                        this.removeSkillCustomInfo(effect.getSourceId());
                        break;
                    }
                    break;
                }
            }
            case 101120109: {
                if (overwrite && statups.containsKey(SecondaryStat.ImmuneBarrier)) {
                    noPacket = true;
                    break;
                }
                break;
            }
            case 152000009:
            case 152120003:
            case 400021099:
            case 400051058: {
                if (overwrite) {
                    noPacket = true;
                    break;
                }
                break;
            }
        }
        if (!noPacket) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(statups, this));
            if (this.map != null) {
                this.map.broadcastMessage(this, CWvsContext.BuffPacket.cancelForeignBuff(this, statups), false);
                if (effect.isHide()) {
                    this.map.broadcastMessage(this, CField.spawnPlayerMapobject(this), false);
                }
            }
        }
        if (effect.getSourceId() == 35121013) {
            SkillFactory.getSkill(35121005).getEffect(this.getTotalSkillLevel(35121005)).applyTo(this, false);
        } else if (effect.getSourceId() == 400031017) {
            this.setSkillCustomInfo(23110005, 10L, 0L);
            SkillFactory.getSkill(23110004).getEffect(this.getSkillLevel(23110004)).applyTo(this);
        }
        if (statups.containsKey(SecondaryStat.RideVehicle)) {
            this.equipChanged();
            if (this.getBuffedEffect(80001242) != null) {
                this.cancelEffect(this.getBuffedEffect(80001242));
            }
        }
        if (statups.containsKey(SecondaryStat.SpectorTransForm)) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this));
        }
        if (effect.getSourceId() == 20040219) {
            if (this.getLuminusMorph()) {
                this.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                this.setLuminusMorph(false);
                SkillFactory.getSkill(20040217).getEffect(1).applyTo(this, false);
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(this.getLuminusMorphUse(), this.getLuminusMorph()));
                this.setUseTruthDoor(false);
            } else {
                this.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                this.setLuminusMorph(true);
                SkillFactory.getSkill(20040216).getEffect(1).applyTo(this, false);
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(this.getLuminusMorphUse(), this.getLuminusMorph()));
                this.setUseTruthDoor(false);
            }
        } else if (effect.getSourceId() == 20040220) {
            if (this.getLuminusMorph()) {
                this.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                this.setLuminusMorph(false);
                SkillFactory.getSkill(20040217).getEffect(1).applyTo(this, false);
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(this.getLuminusMorphUse(), this.getLuminusMorph()));
                this.setUseTruthDoor(false);
            } else {
                this.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                this.setLuminusMorph(true);
                SkillFactory.getSkill(20040216).getEffect(1).applyTo(this, false);
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(this.getLuminusMorphUse(), this.getLuminusMorph()));
                this.setUseTruthDoor(false);
            }
        }
    }

    public void cancelEffectFromBuffStat(final SecondaryStat stat) {
        if (this.checkBuffStatValueHolder(stat) != null) {
            this.cancelEffect(this.checkBuffStatValueHolder(stat).effect, Arrays.asList(stat));
        }
    }

    public void cancelEffectFromBuffStat(final SecondaryStat stat, final int from) {
        if (this.checkBuffStatValueHolder(stat, from) != null && this.checkBuffStatValueHolder(stat, from).effect.getSourceId() == from) {
            this.cancelEffect(this.checkBuffStatValueHolder(stat, from).effect, Arrays.asList(stat));
        }
    }

    public void dispel() {
        if (!this.isHidden()) {
            for (final Pair<SecondaryStat, SecondaryStatValueHolder> data : this.effects) {
                final SecondaryStatValueHolder mbsvh = data.right;
                if (mbsvh.effect.isSkill() && !mbsvh.effect.isMorph() && !mbsvh.effect.isMonsterRiding() && !mbsvh.effect.isMechChange()) {
                    this.cancelEffect(mbsvh.effect, Arrays.asList(data.left));
                }
            }
        }
    }

    public void dispelSkill(final int skillid) {
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> mbsvh : this.effects) {
            if (mbsvh.right.effect.isSkill() && mbsvh.right.effect.getSourceId() == skillid) {
                this.cancelEffect(mbsvh.right.effect, Arrays.asList(mbsvh.left));
                break;
            }
        }
    }

    public void dispelSummons() {
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> mbsvh : this.effects) {
            if (mbsvh.right.effect.getSummonMovementType() != null) {
                boolean cancel = true;
                switch (mbsvh.right.effect.getSourceId()) {
                    case 32001014:
                    case 32100010:
                    case 32110017:
                    case 32120019:
                    case 152101000: {
                        cancel = false;
                        break;
                    }
                }
                if (!cancel) {
                    continue;
                }
                this.cancelEffect(mbsvh.right.effect, Arrays.asList(mbsvh.left));
            }
        }
    }

    public void cancelAllBuffs_() {
        this.getEffects().clear();
    }

    public void cancelAllBuffs() {
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> data : this.effects) {
            final SecondaryStatValueHolder mbsvh = data.right;
            this.cancelEffect(mbsvh.effect, Arrays.asList(data.left));
        }
    }

    public void cancelMorphs() {//mush
        for (Pair<SecondaryStat, SecondaryStatValueHolder> mbsvh : this.effects) {
            switch (((SecondaryStatValueHolder) mbsvh.right).effect.getSourceId()) {
                case 5111005:
                case 5121003:
                case 13111005:
                case 15111002:
                case 61111008:
                case 61120008:
                case 61121053: {
                    return;
                }
            }
            if (!((SecondaryStatValueHolder) mbsvh.right).effect.isMorph()) {
                continue;
            }
            this.cancelEffect(((SecondaryStatValueHolder) mbsvh.right).effect, Arrays.asList((SecondaryStat) mbsvh.left));
        }
    }

    public int getMorphState() {//mush
        for (Pair<SecondaryStat, SecondaryStatValueHolder> mbsvh : this.effects) {
            if (!((SecondaryStatValueHolder) mbsvh.right).effect.isMorph()) {
                continue;
            }
            return ((SecondaryStatValueHolder) mbsvh.right).effect.getSourceId();
        }
        return -1;
    }

    public void silentGiveBuffs(final List<PlayerBuffValueHolder> buffs) {
        if (buffs == null) {
            return;
        }
        for (final PlayerBuffValueHolder mbsvh : buffs) {
            mbsvh.effect.silentApplyBuff(this, mbsvh.startTime, mbsvh.statup, mbsvh.cid);
        }
    }

    public List<PlayerBuffValueHolder> getAllBuffs() {
        final List<PlayerBuffValueHolder> ret = new ArrayList<PlayerBuffValueHolder>();
        final Map<Pair<Integer, Byte>, Integer> alreadyDone = new HashMap<Pair<Integer, Byte>, Integer>();
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> mbsvh : this.effects) {
            final Pair<Integer, Byte> key = new Pair<Integer, Byte>(mbsvh.getRight().effect.getSourceId(), mbsvh.getRight().effect.getLevel());
            if (alreadyDone.containsKey(key)) {
                ret.get(alreadyDone.get(key)).statup.put(mbsvh.getLeft(), new Pair<Integer, Integer>(mbsvh.getRight().value, mbsvh.getRight().localDuration));
            } else {
                alreadyDone.put(key, ret.size());
                final EnumMap<SecondaryStat, Pair<Integer, Integer>> list = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
                list.put(mbsvh.getLeft(), new Pair<Integer, Integer>(mbsvh.getRight().value, mbsvh.right.localDuration));
                ret.add(new PlayerBuffValueHolder(mbsvh.getRight().startTime, mbsvh.getRight().effect, list, mbsvh.getRight().localDuration, mbsvh.getRight().cid));
            }
        }
        return ret;
    }

    public int getSkillLevel(final int skillid) {
        return this.getSkillLevel(SkillFactory.getSkill(skillid));
    }

    public int getTotalSkillLevel(final int skillid) {
        return this.getTotalSkillLevel(SkillFactory.getSkill(skillid));
    }

    public final void handleOrbgain(final AttackInfo attack, final int skillid) {
        int 현재콤보어택개수 = this.getBuffedValue(SecondaryStat.ComboCounter);
        final Skill combo = SkillFactory.getSkill(1101013);
        final Skill advcombo = SkillFactory.getSkill(1120003);
        final SecondaryStatEffect ceffect = SkillFactory.getSkill(1101013).getEffect(this.getTotalSkillLevel(1101013));
        final int advComboSkillLevel = this.getTotalSkillLevel(advcombo);
        int 플러스콤보어택 = 1;
        int suc = 0;
        if (this.getSkillLevel(1110013) > 0) {
            suc = 80;
        } else {
            suc = 40;
        }
        if (this.getTotalSkillLevel(advcombo) > 0 && Randomizer.isSuccess((this.getSkillLevel(1120044) > 0) ? 100 : 80)) {
            ++플러스콤보어택;
        }
        if (this.getBuffedValue(400011073)) {
            suc /= 2;
        }
        if (Randomizer.isSuccess(suc)) {
            if (skillid == 1120013 || skillid == 400011073 || skillid == 400011074 || skillid == 400011075 || skillid == 400011076) {
                return;
            }
            현재콤보어택개수 += 플러스콤보어택;
            if (현재콤보어택개수 >= 11) {
                현재콤보어택개수 = 11;
            }
            final EnumMap<SecondaryStat, Pair<Integer, Integer>> stat = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
            stat.put(SecondaryStat.ComboCounter, new Pair<Integer, Integer>(현재콤보어택개수, 0));
            this.setBuffedValue(SecondaryStat.ComboCounter, 현재콤보어택개수);
            this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(stat, combo.getEffect(this.getTotalSkillLevel(combo)), this));
            this.map.broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(this, stat, combo.getEffect(this.getTotalSkillLevel(combo))), false);
        }
    }

    public void handleOrbconsume(final int skillId) {
        int howmany = 0;
        switch (skillId) {
            case 1111008: {
                howmany = 1;
                break;
            }
            case 1111003: {
                howmany = 2;
                if (this.getBuffedValue(SecondaryStat.ComboCostInc) != null) {
                    howmany += (int) this.getSkillCustomValue0(1111003);
                    break;
                }
                break;
            }
            case 400011027: {
                howmany = 6;
                break;
            }
        }
        final Skill combo = SkillFactory.getSkill(1101013);
        if (this.getSkillLevel(combo) <= 0) {
            return;
        }
        final SecondaryStatEffect ceffect = this.getBuffedEffect(SecondaryStat.ComboCounter);
        if (ceffect == null) {
            return;
        }
        final EnumMap<SecondaryStat, Pair<Integer, Integer>> stat = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
        stat.put(SecondaryStat.ComboCounter, new Pair<Integer, Integer>(Math.max(1, this.getBuffedValue(SecondaryStat.ComboCounter) - howmany), 0));
        this.setBuffedValue(SecondaryStat.ComboCounter, Math.max(1, this.getBuffedValue(SecondaryStat.ComboCounter) - howmany));
        this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(stat, ceffect, this));
        this.map.broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(this, stat, ceffect), false);
    }

    public void silentEnforceMaxHpMp() {
        this.stats.setMp(this.stats.getMp(), this);
        this.stats.setHp(this.stats.getHp(), true, this, false);
    }

    public void enforceMaxHpMp() {
        final Map<MapleStat, Long> statups = new EnumMap<MapleStat, Long>(MapleStat.class);
        if (this.stats.getMp() > this.stats.getCurrentMaxMp(this)) {
            this.stats.setMp(this.stats.getMp(), this);
            statups.put(MapleStat.MP, this.stats.getMp());
        }
        if (this.stats.getHp() > this.stats.getCurrentMaxHp()) {
            this.stats.setHp(this.stats.getHp(), this);
            statups.put(MapleStat.HP, this.stats.getHp());
        }
        if (statups.size() > 0) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(statups, this));
        }
    }

    public MapleMap getMap() {
        return this.map;
    }

    public void setMap(final MapleMap newmap) {
        this.map = newmap;
    }

    public void setMap(final int PmapId) {
        this.mapid = PmapId;
    }

    public int getMapId() {
        if (this.map != null) {
            return this.map.getId();
        }
        return this.mapid;
    }

    public byte getInitialSpawnpoint() {
        return this.initialSpawnPoint;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public final String getBlessOfFairyOrigin() {
        return this.BlessOfFairy_Origin;
    }

    public final String getBlessOfEmpressOrigin() {
        return this.BlessOfEmpress_Origin;
    }

    public final short getLevel() {
        return this.level;
    }

    public final int getFame() {
        return this.fame;
    }

    public final int getFallCounter() {
        return this.fallcounter;
    }

    public final MapleClient getClient() {
        return this.client;
    }

    public final void setClient(final MapleClient client) {
        this.client = client;
    }

    public long getExp() {
        return this.exp;
    }

    public short getRemainingAp() {
        return this.remainingAp;
    }

    public int getRemainingSp() {
        return this.remainingSp[GameConstants.getSkillBook(this.job, 0)];
    }

    public int getRemainingSp(final int skillbook) {
        return this.remainingSp[skillbook];
    }

    public int[] getRemainingSps() {
        return this.remainingSp;
    }

    public int getRemainingSpSize() {
        int ret = 0;
        for (int i = 0; i < this.remainingSp.length; ++i) {
            if (this.remainingSp[i] > 0) {
                ++ret;
            }
        }
        return ret;
    }

    public short getHpApUsed() {
        return this.hpApUsed;
    }

    public boolean isHidden() {
        return this.getBuffedValue(9001004);
    }

    public boolean isDominant() {
        return this.dominant;
    }

    public void setDominant(final boolean active) {
        this.dominant = active;
    }

    public void setHpApUsed(final short hpApUsed) {
        this.hpApUsed = hpApUsed;
    }

    public byte getSkinColor() {
        return this.skinColor;
    }

    public void setSkinColor(final byte skinColor) {
        this.skinColor = skinColor;
    }

    public byte getSecondSkinColor() {
        return this.secondSkinColor;
    }

    public void setSecondSkinColor(final byte secondSkinColor) {
        this.secondSkinColor = secondSkinColor;
    }

    public short getJob() {
        return this.job;
    }

    public byte getGender() {
        return this.gender;
    }

    public byte getSecondGender() {
        return this.secondgender;
    }

    public int getHair() {
        return this.hair;
    }

    public int getSecondHair() {
        return this.secondhair;
    }

    public int getFace() {
        return this.face;
    }

    public int getSecondFace() {
        return this.secondface;
    }

    public int getDemonMarking() {
        return this.demonMarking;
    }

    public void setDemonMarking(final int mark) {
        this.demonMarking = mark;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setExp(final long exp) {
        this.exp = exp;
    }

    public void setHair(final int hair) {
        this.hair = hair;
    }

    public void setSecondHair(final int secondhair) {
        this.secondhair = secondhair;
    }

    public void setFace(final int face) {
        this.face = face;
    }

    public void setSecondFace(final int secondface) {
        this.secondface = secondface;
    }

    public void setFame(final int fame) {
        this.fame = fame;
    }

    public void setFallCounter(final int fallcounter) {
        this.fallcounter = fallcounter;
    }

    public Point getOldPosition() {
        return this.old;
    }

    public void setOldPosition(final Point x) {
        this.old = x;
    }

    public void setRemainingAp(final short remainingAp) {
        this.remainingAp = remainingAp;
    }

    public void setRemainingSp(final int remainingSp) {
        this.remainingSp[GameConstants.getSkillBook(this.job, 0)] = remainingSp;
    }

    public void setRemainingSp(final int remainingSp, final int skillbook) {
        this.remainingSp[skillbook] = remainingSp;
    }

    public void setGender(final byte gender) {
        this.gender = gender;
    }

    public void setSecondGender(final byte secondgender) {
        this.secondgender = secondgender;
    }

    public void setInvincible(final boolean invinc) {
        this.invincible = invinc;
    }

    public boolean isInvincible() {
        return this.invincible;
    }

    public BuddyList getBuddylist() {
        return this.buddylist;
    }

    public void addFame(final int famechange) {
        this.fame += famechange;
        this.getTrait(MapleTrait.MapleTraitType.charm).addLocalExp(famechange);
    }

    public void updateFame() {
        this.updateSingleStat(MapleStat.FAME, this.fame);
    }

    public void changeMapBanish(final int mapid, final String portal, final String msg) {
        final MapleMap map = this.client.getChannelServer().getMapFactory().getMap(mapid);
        this.changeMap(map, map.getPortal(portal), true);
    }

    public void warp(final int Mapid) {
        final ChannelServer cserv = this.getClient().getChannelServer();
        final MapleMap target = cserv.getMapFactory().getMap(Mapid);
        this.changeMap(target, target.getPortal(0));
    }

    public void warpdelay(int Mapid, int Delay) {
        server.Timer.MapTimer.getInstance().schedule(() -> {
            ChannelServer cserv = getClient().getChannelServer();
            MapleMap target = cserv.getMapFactory().getMap(Mapid);
            changeMap(target, target.getPortal(0));
        }, (Delay * 1000));
    }

    public void changeMap(final int Mapid, final Point pos) {
        final MapleMap to = this.getClient().getChannelServer().getMapFactory().getMap(Mapid);
        this.changeMapInternal(to, pos, CField.getWarpToMap(to, 129, this), null, false);
    }

    public void changeMap(final int Mapid, final int portalid) {
        final MapleMap to = this.getClient().getChannelServer().getMapFactory().getMap(Mapid);
        final MaplePortal pto = to.getPortal(portalid);
        this.changeMapInternal(to, pto.getPosition(), CField.getWarpToMap(to, pto.getId(), this), null, false);
    }

    public void changeMap(final MapleMap to, final Point pos) {
        this.changeMapInternal(to, pos, CField.getWarpToMap(to, 129, this), null, false);
    }

    public void changeMap(final MapleMap to, final MaplePortal maplePortal, final boolean banish) {
        this.changeMapInternal(to, to.getPortal(0).getPosition(), CField.getWarpToMap(to, 0, this), to.getPortal(0), banish);
    }

    public void changeMap(final MapleMap to, final MaplePortal pto) {
        this.changeMapInternal(to, pto.getPosition(), CField.getWarpToMap(to, pto.getId(), this), null, false);
    }

    public void changeMapPortal(final MapleMap to, final MaplePortal pto) {
        this.changeMapInternal(to, pto.getPosition(), CField.getWarpToMap(to, pto.getId(), this), pto, false);
    }

    public void changeMapChannel(final MapleMap to, final int channel) {
        this.changeMapChannel(to, to.getPortal(0), channel);
    }

    private void changeMapChannel(final MapleMap to, final MaplePortal pto, final int channel) {
        this.changeChannel(channel);
        this.changeMap(to, pto);
    }

    private void changeMapInternal(final MapleMap to, final Point pos, final byte[] warpPacket, final MaplePortal pto, final boolean banish) {
        if (to == null) {
            return;
        }
        final int nowmapid = this.map.getId();
        if (this.eventInstance != null) {
            this.eventInstance.changedMap(this, to.getId());
        }
        if ((to.getId() == 800000000 || to.getId() == 740000000 || to.getId() == 500000000 || to.getId() == 270051100) && this.getV("d_map_" + to.getId()) == "0") {
            this.addKV("d_map_" + to.getId(), "1");
        }
        if (to.getId() == 450004550) {
            to.broadcastMessage(MobPacket.BossLucid.setStainedGlassOnOff(true, FieldLucid.STAINED_GLASS));
        }
        if (this.map.getId() == nowmapid) {
            if ((this.getDeathCount() > 0 || this.liveCounts() > 0) && !banish && to.getId() == this.map.getId()) {
                switch (this.map.getId()) {
                    case 450004150:
                    case 450004250:
                    case 450004450:
                    case 450004550:
                    case 450004750:
                    case 450004850: {
                        final int x = (this.map.getId() == 450004150 || this.map.getId() == 450004450 || this.map.getId() == 450004750) ? 157 : (Randomizer.nextBoolean() ? 316 : 1027);
                        final int y = (this.map.getId() == 450004150 || this.map.getId() == 450004450 || this.map.getId() == 450004750) ? 48 : (Randomizer.nextBoolean() ? -855 : -842);
                        this.getMap().broadcastMessage(CField.Respawn(this.getId(), (int) this.getStat().getHp()));
                        this.client.getSession().writeAndFlush((Object) CField.onUserTeleport(x, y));
                        break;
                    }
                    case 450008150:
                    case 450008250:
                    case 450008350:
                    case 450008750:
                    case 450008850:
                    case 450008950:
                    case 450010500: {
                        this.getTruePosition().x = 0;
                        this.getMap().broadcastMessage(CField.Respawn(this.getId(), (int) this.getStat().getHp()));
                        this.client.getSession().writeAndFlush((Object) CField.onUserTeleport(0, this.getTruePosition().y));
                        break;
                    }
                    case 450013700: {
                        this.giveBlackMageBuff();
                    }
                    case 450013100:
                    case 450013300:
                    case 450013500: {
                        this.getTruePosition().x = 0;
                        this.getMap().broadcastMessage(CField.Respawn(this.getId(), (int) this.getStat().getHp()));
                        this.client.getSession().writeAndFlush((Object) CField.onUserTeleport(0, this.getTruePosition().y));
                        break;
                    }
                    default: {
                        this.client.getSession().writeAndFlush((Object) warpPacket);
                        break;
                    }
                }
            } else {
                this.client.getSession().writeAndFlush((Object) warpPacket);
            }
            final boolean shouldChange = this.client.getChannelServer().getPlayerStorage().getCharacterById(this.getId()) != null;
            final boolean shouldState = this.map.getId() == to.getId();
            if (shouldChange && shouldState) {
                to.setCheckStates(false);
            }
            if (shouldChange) {
                this.map.removePlayer(this);
                this.map = to;
                this.setPosition(pos);
                final Map<MapleStat, Long> updates = new EnumMap<MapleStat, Long>(MapleStat.class);
                if (this.stats.getMp() == 0L) {
                    this.stats.setMp(this.stats.getCurrentMaxMp(this), this);
                    updates.put(MapleStat.MP, this.stats.getMp());
                }
                if (this.stats.getHp() == 0L) {
                    this.stats.setHp(this.stats.getCurrentMaxHp(), this);
                    updates.put(MapleStat.HP, this.stats.getHp());
                }
                if (!updates.isEmpty()) {
                    this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(updates, this));
                }
                to.addPlayer(this);
                this.stats.relocHeal(this);
                if (shouldState) {
                    to.setCheckStates(true);
                }
                if (this.getBuffedValue(400051068)) {
                    this.MechCarrier(1000, true);
                }
                if (this.getSkillCustomValue(8910000) != null) {
                    this.client.send(CField.VonVonStopWatch(this.getSkillCustomTime(8910000)));
                    if (((to.getId() >= 105200110 && to.getId() <= 105200119) || (to.getId() >= 105200510 && to.getId() <= 105200519)) && to.getCustomValue(8910000) != null) {
                        final int type = to.getCustomValue0(8910000);
                        this.client.send(CField.environmentChange("Pt0" + type + "gate", 2));
                    }
                }
                if (to.getId() == 272020300 || to.getId() == 272020310 || to.getId() == 272020500 || to.getId() == 272020600 || to.getId() == 272030410) {
                    this.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8860003), new Point(73, 95));
                    this.getClient().getSession().writeAndFlush((Object) CField.startMapEffect("자신 속의 추악한 내면을 마주한 기분이 어떠신지요?", 5120056, true));
                } else if (to.getId() == 450008750) {
                    this.getClient().getSession().writeAndFlush((Object) CField.portalTeleport(Randomizer.nextBoolean() ? "ptup" : "ptdown"));
                }
                if (to.getId() == 280030100 || to.getId() == 280030000 || to.getId() == 262031300 || to.getId() == 105200610) {
                    for (final MapleMonster monster : this.getMap().getAllMonster()) {
                        if ((monster.getId() == 8800002 || monster.getId() == 8800102) && monster.getSpecialtxt() != null) {
                            to.broadcastMessage(MobPacket.setMonsterProPerties(monster.getObjectId(), 1, 0, 0));
                        } else if (monster.getId() == 8870100) {
                            if (monster.getPhase() != 2) {
                                continue;
                            }
                            to.broadcastMessage(MobPacket.showMonsterHP(monster.getObjectId(), 30));
                        } else {
                            if ((monster.getId() != 8900002 && monster.getId() != 8900102) || !monster.getSpecialtxt().equals(this.getName())) {
                                continue;
                            }
                            monster.switchController(this, true);
                            this.getMap().broadcastMessage(MobPacket.ShowPierreEffect(this, monster));
                        }
                    }
                }
            }
        }
    }

    public void cancelChallenge() {
        if (this.challenge != 0 && this.client.getChannelServer() != null) {
            final MapleCharacter chr = this.client.getChannelServer().getPlayerStorage().getCharacterById(this.challenge);
            if (chr != null) {
                chr.dropMessage(6, this.getName() + " has denied your request.");
                chr.setChallenge(0);
            }
            this.dropMessage(6, "Denied the challenge.");
            this.challenge = 0;
        }
    }

    public void leaveMap(final MapleMap map) {
        for (final MapleMonster mons : this.controlled) {
            if (mons != null) {
                mons.setController(null);
                mons.setControllerHasAggro(false);
                map.updateMonsterController(mons);
            }
        }
        this.controlled.clear();
        this.visibleMapObjects.clear();
        if (this.chair != 0) {
            this.chair = 0;
        }
        this.clearLinkMid();
        this.cancelChallenge();
        if (!this.getMechDoors().isEmpty()) {
            this.removeMechDoor();
        }
        if (this.getTrade() != null) {
            MapleTrade.cancelTrade(this.getTrade(), this.client, this);
        }
    }

    public void changeJob(final int newJob) {
        try {
            this.cancelEffectFromBuffStat(SecondaryStat.ShadowPartner);
            this.job = (short) newJob;
            this.updateSingleStat(MapleStat.JOB, newJob);
            if (!GameConstants.isBeginnerJob(newJob)) {
                if (GameConstants.isEvan(newJob) || GameConstants.isResist(newJob) || GameConstants.isMercedes(newJob)) {
                    int changeSp = (newJob == 2200 || newJob == 2210 || newJob == 2211 || newJob == 2213) ? 3 : 5;
                    if (GameConstants.isResist(this.job) && newJob != 3100 && newJob != 3200 && newJob != 3300 && newJob != 3500) {
                        changeSp = 3;
                    }
                    final int[] remainingSp = this.remainingSp;
                    final int skillBook = GameConstants.getSkillBook(newJob, 0);
                    remainingSp[skillBook] += changeSp;
                    this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getSPMsg((byte) changeSp, (short) newJob));
                } else if (GameConstants.isPhantom(this.job)) {
                    if (this.job == 2412) {
                        final Skill skil1 = SkillFactory.getSkill(20031209);
                        this.changeSingleSkillLevel(skil1, 0, (byte) 0);
                        final Skill skil2 = SkillFactory.getSkill(20031210);
                        this.changeSingleSkillLevel(skil2, 1, (byte) skil2.getMaxLevel());
                    }
                    this.client.getSession().writeAndFlush((Object) CField.updateCardStack(false, 0));
                } else {
                    final int[] remainingSp2 = this.remainingSp;
                    final int skillBook2 = GameConstants.getSkillBook(newJob, 0);
                    ++remainingSp2[skillBook2];
                    if (newJob % 10 >= 2) {
                        final int[] remainingSp3 = this.remainingSp;
                        final int skillBook3 = GameConstants.getSkillBook(newJob, 0);
                        remainingSp3[skillBook3] += 2;
                    }
                }
                if (newJob % 10 >= 1 && this.level >= 60) {
                    this.remainingAp += 5;
                    this.updateSingleStat(MapleStat.AVAILABLEAP, this.remainingAp);
                }
                if (!this.isGM()) {
                    this.resetStatsByJob(true);
                    if (!GameConstants.isEvan(newJob) && this.getLevel() > ((newJob == 200) ? 8 : 10) && newJob % 100 == 0 && newJob % 1000 / 100 > 0) {
                        final int[] remainingSp4 = this.remainingSp;
                        final int skillBook4 = GameConstants.getSkillBook(newJob, 0);
                        remainingSp4[skillBook4] += 3 * (this.getLevel() - ((newJob == 200) ? 8 : 10));
                    }
                }
                this.updateSingleStat(MapleStat.AVAILABLESP, 0L);
            }
            if (GameConstants.isDemonAvenger(this.job)) {
                this.changeSkillLevel(30010230, (byte) 1, (byte) 1);
                this.changeSkillLevel(30010231, (byte) 1, (byte) 1);
                this.changeSkillLevel(30010232, (byte) 1, (byte) 1);
                this.changeSkillLevel(30010242, (byte) 1, (byte) 1);
            }
            if (GameConstants.isDemonSlayer(this.job)) {
                this.changeSkillLevel(30010111, (byte) 1, (byte) 1);
            }
            for (final MapleUnion union : this.getUnions().getUnions()) {
                if (union.getCharid() == this.id) {
                    union.setJob(newJob);
                }
            }
            long maxhp = this.stats.getMaxHp();
            long maxmp = this.stats.getMaxMp();
            if (maxhp >= 500000L) {
                maxhp = 500000L;
            }
            if (maxmp >= 500000L) {
                maxmp = 500000L;
            }
            if (GameConstants.isDemonSlayer(this.job)) {
                maxmp = GameConstants.getMPByJob(this);
            }
            if (this.job == 410) {
                this.changeSingleSkillLevel(SkillFactory.getSkill(4101011), 1, (byte) 1);
            }
            if (this.job == 15510) {
                this.changeSingleSkillLevel(SkillFactory.getSkill(155101006), 1, (byte) 1);
            }
            this.stats.setInfo(maxhp, maxmp, maxhp, maxmp);
            this.stats.recalcLocalStats(this);
            final Map<MapleStat, Long> statup = new EnumMap<MapleStat, Long>(MapleStat.class);
            statup.put(MapleStat.MAXHP, this.stats.getCurrentMaxHp());
            statup.put(MapleStat.MAXMP, this.stats.getCurrentMaxMp(this));
            statup.put(MapleStat.HP, this.stats.getCurrentMaxHp());
            statup.put(MapleStat.MP, this.stats.getCurrentMaxMp(this));
            this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(statup, this));
            this.map.broadcastMessage(this, CField.EffectPacket.showNormalEffect(this, 14, false), false);
            this.silentPartyUpdate();
            this.guildUpdate();
            this.AutoTeachSkill();
            if (this.dragon != null) {
                this.map.broadcastMessage(CField.removeDragon(this.id));
                this.dragon = null;
            }
            if (newJob >= 2200 && newJob <= 2218) {
                if (this.getBuffedValue(SecondaryStat.RideVehicle) != null) {
                    this.cancelEffectFromBuffStat(SecondaryStat.RideVehicle);
                }
                this.makeDragon();
            }
        } catch (Exception e) {
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", e);
        }
    }

    public void setSkillBuffTest(final int buf) {
        this.bufftest = buf;
    }

    public int getSkillBuffTest() {
        return this.bufftest;
    }

    public void makeDragon() {
        this.dragon = new MapleDragon(this);
        this.map.broadcastMessage(CField.spawnDragon(this.dragon));
    }

    public MapleDragon getDragon() {
        return this.dragon;
    }

    public short getAp() {
        return this.remainingAp;
    }

    public void gainAp(final short ap) {
        this.remainingAp += ap;
        this.updateSingleStat(MapleStat.AVAILABLEAP, this.remainingAp);
    }

    public void gainSP(final int sp) {
        final int[] remainingSp = this.remainingSp;
        final int skillBook = GameConstants.getSkillBook(this.job, 0);
        remainingSp[skillBook] += sp;
        this.updateSingleStat(MapleStat.AVAILABLESP, 0L);
        this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getSPMsg((byte) sp, this.job));
    }

    public void gainSP(final int sp, final int skillbook) {
        final int[] remainingSp = this.remainingSp;
        remainingSp[skillbook] += sp;
        this.updateSingleStat(MapleStat.AVAILABLESP, 0L);
        this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getSPMsg((byte) sp, (short) 0));
    }

    public void resetSP(final int sp) {
        for (int i = 0; i < this.remainingSp.length; ++i) {
            this.remainingSp[i] = sp;
        }
        this.updateSingleStat(MapleStat.AVAILABLESP, 0L);
    }

    public void resetAPSP() {
        this.resetSP(0);
        this.gainAp((short) (-this.remainingAp));
    }

    public List<Integer> getProfessions() {
        final List<Integer> prof = new ArrayList<Integer>();
        for (int i = 9200; i <= 9204; ++i) {
            if (this.getProfessionLevel(this.id * 10000) > 0) {
                prof.add(i);
            }
        }
        return prof;
    }

    public int getProfessionLevel(final int id) {
        final String key = id + "lv";
        if (this.getV(key) == null) {
            this.addKV(key, "0");
        }
        if (Integer.parseInt(this.getV(id + "lv")) >= 13) {
            this.addKV(id + "lv", "12");
        }
        return Integer.parseInt(this.getV(key));
    }

    public int getProfessionExp(final int id) {
        final String key = id + "EXP";
        if (this.getV(key) == null) {
            this.addKV(key, "0");
        }
        return Integer.parseInt(this.getV(key));
    }

    public boolean addProfessionExp(final int id, final int expGain) {
        final int ret = this.getProfessionLevel(id);
        if (ret <= 0) {
            return false;
        }
        final int newExp = this.getProfessionExp(id) + expGain;
        if (newExp >= GameConstants.getProfessionEXP(ret)) {
            if (this.getV(id + "lv") != null) {
                this.addKV(id + "lv", Integer.parseInt(this.getV(id + "lv")) + 1 + "");
                if (Integer.parseInt(this.getV(id + "lv")) >= 13) {
                    this.addKV(id + "lv", "12");
                }
            }
            this.changeProfessionLevelExp(id, Integer.parseInt(this.getV(id + "lv")), newExp - GameConstants.getProfessionEXP(ret));
            final int traitGain = (int) Math.pow(2.0, ret + 1);
            switch (id) {
                case 92000000: {
                    this.traits.get(MapleTrait.MapleTraitType.sense).addExp(traitGain, this);
                    break;
                }
                case 92010000: {
                    this.traits.get(MapleTrait.MapleTraitType.will).addExp(traitGain, this);
                    break;
                }
                case 92020000:
                case 92030000:
                case 92040000: {
                    this.traits.get(MapleTrait.MapleTraitType.craft).addExp(traitGain, this);
                    break;
                }
            }
            return true;
        }
        System.out.println("\uacbd\ud5d8\uce58 \uc90c : " + newExp + " : " + ret);
        if (this.getV(id + "EXP") != null) {
            this.addKV(id + "EXP", Integer.parseInt(this.getV(id + "EXP")) + expGain + "");
        }
        this.changeProfessionLevelExp(id, ret, newExp);
        return false;
    }

    public void changeProfessionLevelExp(final int id, final int level, final int exp) {
        int total = 0;
        if (exp >= 65536) {
            final int plus = exp - 65535;
            total = ((level & 0xFFFF) << 24) + 65535;
            total += plus;
        } else {
            total = ((level & 0xFFFF) << 24) + (exp & 0xFFFF);
        }
        this.changeSingleSkillLevel(SkillFactory.getSkill(id), total, (byte) 30);
    }

    public void changeSingleSkillLevel(final Skill skill, final int newLevel, final byte newMasterlevel) {
        if (skill == null) {
            return;
        }
        this.changeSingleSkillLevel(skill, newLevel, newMasterlevel, SkillFactory.getDefaultSExpiry(skill));
    }

    public void changeSingleSkillLevel(final Skill skill, final int newLevel, final byte newMasterlevel, final long expiration) {
        final Map<Skill, SkillEntry> list = new HashMap<Skill, SkillEntry>();
        boolean hasRecovery = false;
        boolean recalculate = false;
        if (this.changeSkillData(skill, newLevel, newMasterlevel, expiration)) {
            list.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
            if (GameConstants.isRecoveryIncSkill(skill.getId())) {
                hasRecovery = true;
            }
            if (skill.getId() < 80000000) {
                recalculate = true;
            }
        }
        if (list.isEmpty()) {
            return;
        }
        this.client.getSession().writeAndFlush((Object) CWvsContext.updateSkills(list));
        this.reUpdateStat(hasRecovery, recalculate);
    }

    public void changeSkillsLevel(final Map<Skill, SkillEntry> ss) {
        if (ss.isEmpty()) {
            return;
        }
        final Map<Skill, SkillEntry> list = new HashMap<Skill, SkillEntry>();
        boolean hasRecovery = false;
        boolean recalculate = false;
        for (final Map.Entry<Skill, SkillEntry> data : ss.entrySet()) {
            if (this.changeSkillData(data.getKey(), data.getValue().skillevel, data.getValue().masterlevel, data.getValue().expiration)) {
                list.put(data.getKey(), data.getValue());
                if (GameConstants.isRecoveryIncSkill(data.getKey().getId())) {
                    hasRecovery = true;
                }
                if (data.getKey().getId() >= 90000000 && !data.getKey().isVMatrix()) {
                    continue;
                }
                recalculate = true;
            }
        }
        if (list.isEmpty()) {
            return;
        }
        this.client.getSession().writeAndFlush((Object) CWvsContext.updateSkills(list));
        this.reUpdateStat(hasRecovery, recalculate);
    }

    private void reUpdateStat(final boolean hasRecovery, final boolean recalculate) {
        this.changed_skills = true;
        if (hasRecovery) {
            this.stats.relocHeal(this);
        }
        if (recalculate) {
            this.stats.recalcLocalStats(this);
        }
    }

    public boolean changeSkillData(final Skill skill, final int newLevel, byte newMasterlevel, final long expiration) {
        if (skill == null) {
            return false;
        }
        if (newLevel < newMasterlevel) {
            newMasterlevel = (byte) newLevel;
        }
        if (newLevel == 0 && newMasterlevel == 0) {
            if (!this.skills.containsKey(skill)) {
                return false;
            }
            this.skills.remove(skill);
        } else {
            this.skills.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
        }
        return true;
    }

    public void changeSkillLevel(final int skill, final byte newLevel, final byte newMasterLevel) {
        this.changeSkillLevel(SkillFactory.getSkill(skill), newLevel, newMasterLevel);
    }

    public void changeSkillLevel(final Skill skill, final byte newLevel, final byte newMasterlevel) {
        this.changeSkillLevel_Skip(skill, newLevel, newMasterlevel);
    }

    public void changeSkillLevel_Skip(final Skill skil, final int skilLevel, final byte masterLevel) {
        final Map<Skill, SkillEntry> enry = new HashMap<Skill, SkillEntry>(1);
        enry.put(skil, new SkillEntry(skilLevel, masterLevel, -1L));
        this.changeSkillLevel_Skip(enry, true);
    }

    public void changeSkillLevel_Skip(final Map<Skill, SkillEntry> skill, final boolean write) {
        if (skill.isEmpty()) {
            return;
        }
        final Map<Skill, SkillEntry> newL = new HashMap<Skill, SkillEntry>();
        for (final Map.Entry<Skill, SkillEntry> z : skill.entrySet()) {
            if (z.getKey() == null) {
                continue;
            }
            newL.put(z.getKey(), z.getValue());
            if (z.getValue().skillevel <= 0 && z.getValue().masterlevel == 0) {
                if (!this.skills.containsKey(z.getKey())) {
                    continue;
                }
                this.skills.remove(z.getKey());
            } else {
                this.skills.put(z.getKey(), z.getValue());
            }
        }
        if (write && !newL.isEmpty()) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.updateSkills(newL));
            this.getStat().recalcLocalStats(this);
        }
    }

    public void playerDead() {
        if (this.getEventInstance() != null) {
            this.getEventInstance().playerKilled(this);
        }
        this.checkSpecialCoreSkills("die", 0, null);
        this.dispelSummons();
        this.checkFollow();
        while (this.getBuffedValue(SecondaryStat.NotDamaged) != null) {
            this.cancelEffect(this.getBuffedEffect(SecondaryStat.NotDamaged));
        }
        while (this.getBuffedValue(SecondaryStat.IndieNotDamaged) != null) {
            this.cancelEffect(this.getBuffedEffect(SecondaryStat.IndieNotDamaged));
        }
        while (this.getBuffedValue(400011010)) {
            this.getClient().send(CField.skillCooldown(400011010, this.getBuffedEffect(400011010).getZ() * 1000));
            this.addCooldown(400011010, System.currentTimeMillis(), this.getBuffedEffect(400011010).getZ() * 1000);
            this.cancelEffect(this.getBuffedEffect(400011010));
        }
        while (this.hasDisease(SecondaryStat.CapDebuff)) {
            this.cancelDisease(SecondaryStat.CapDebuff);
        }
        final int[] array;
        final int[] FreudsProtections = array = new int[]{400001025, 400001026, 400001027, 400001028, 400001029, 400001030};
        for (final int FreudsProtection : array) {
            if (this.getBuffedValue(FreudsProtection)) {
                this.cancelEffect(this.getBuffedEffect(FreudsProtection));
            }
        }
        this.dotHP = 0;
        this.lastDOTTime = 0L;
        this.BHGCCount = 0;
        this.HowlingGaleCount = 0;
        this.WildGrenadierCount = 0;
        this.useBuffFreezer = false;
        MapleMonster will = this.map.getMonsterById(8880300);
        if (will == null) {
            will = this.map.getMonsterById(8880340);
            if (will == null) {
                will = this.map.getMonsterById(8880301);
                if (will == null) {
                    will = this.map.getMonsterById(8880341);
                }
            }
        }
        if (will != null) {
            if (will.isWillSpecialPattern()) {
                will.setWillSpecialPattern(false);
            }
            if (will.isUseSpecialSkill()) {
                will.setUseSpecialSkill(false);
            }
        }
        if ((this.getMapId() == 450008950 || this.getMapId() == 450008350) && this.getSkillCustomValue0(24209) > 0L) {
            for (final MapleCharacter achr : this.getMap().getAllChracater()) {
                if (achr.getId() != this.getId()) {
                    final Map<SecondaryStat, Pair<Integer, Integer>> diseases = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
                    diseases.put(SecondaryStat.WillPoison, new Pair<Integer, Integer>(1, 7000));
                    this.giveDebuff(diseases, MobSkillFactory.getMobSkill(242, 9));
                    achr.setSkillCustomInfo(24219, Randomizer.rand(1, Integer.MAX_VALUE), 0L);
                    achr.setSkillCustomInfo(24209, 1L, 0L);
                    achr.setSkillCustomInfo(24220, 0L, 3000L);
                    achr.getMap().broadcastMessage(MobPacket.BossWill.posion(achr, (int) achr.getSkillCustomValue0(24219), 0, 0, 0));
                    final MapleCharacter player;
                    server.Timer.MapTimer.getInstance().schedule(() -> {
                        achr.getMap().broadcastMessage(MobPacket.BossWill.removePoison(achr, (int) achr.getSkillCustomValue0(24219)));
                        achr.removeSkillCustomInfo(24219);
                        achr.removeSkillCustomInfo(24209);
                        achr.removeSkillCustomInfo(24220);
                        return;
                    }, 7000L);
                    break;
                }
            }
            this.getMap().broadcastMessage(MobPacket.BossWill.removePoison(this, (int) this.getSkillCustomValue0(24219)));
            this.removeSkillCustomInfo(24219);
            this.removeSkillCustomInfo(24209);
            this.removeSkillCustomInfo(24220);
            final int rand = Randomizer.rand(1, Integer.MAX_VALUE);
            final Point pos = this.getPosition();
            this.getMap().broadcastMessage(MobPacket.BossWill.removePoison(this, (int) this.getSkillCustomValue0(24219)));
            this.getMap().broadcastMessage(MobPacket.BossWill.posion(this, rand, 1, pos.x, pos.y));
            this.getMap().addWillPoison(pos);
            final int objid;
            final Point pos2;
            server.Timer.MapTimer.getInstance().schedule(() -> {
                this.getMap().broadcastMessage(MobPacket.BossWill.removePoison(this, rand));
                this.getMap().removeWillPosion(pos);
                return;
            }, 7000L);
        }
        if (this.getMapId() == 863010240 || this.getMapId() == 863010330 || this.getMapId() == 863010430 || this.getMapId() == 863010600) {
            final MapleCharacter pchr = this.getClient().getChannelServer().getPlayerStorage().getCharacterById(this.getParty().getLeader().getId());
            if (pchr == null) {
                dropMessage(5, "파티의 상태가 변경되어 골럭스 원정대가 해체됩니다.");
                this.setKeyValue(200106, "golrux_in", "0");
            } else if (pchr.getKeyValue(200106, "golrux_in") == 1L) {
                if (pchr.getKeyValue(200106, "golrux_dc") <= 0L) {
                    pchr.setKeyValue(200106, "golrux_in", "0");
                    this.warp(ServerConstants.warpMap);
                    if (pchr.getId() != this.getId()) {
                        dropMessage(5, "데스카운트가 모두 소모되어 골럭스 원정대가 해체됩니다.");
                        pchr.dropMessage(5, "데스카운트가 모두 소모되어 골럭스 원정대가 해체됩니다.");
                    } else {
                        dropMessage(5, "데스카운트가 모두 소모되어 골럭스 원정대가 해체됩니다.");
                    }
                } else if (pchr.getId() != this.getId()) {
                    pchr.setKeyValue(200106, "golrux_dc", String.valueOf(getKeyValue(200106, "golrux_dc") - 1L));
                    pchr.dropMessage(5, "데스카운트가 " + getKeyValue(200106, "golrux_dc") + "만큼 남았습니다.");
                    dropMessage(5, "데스카운트가 " + getKeyValue(200106, "golrux_dc") + "만큼 남았습니다.");
                } else {
                    this.setKeyValue(200106, "golrux_dc", String.valueOf(this.getKeyValue(200106, "golrux_dc") - 1L));
                    dropMessage(5, "데스카운트가 " + getKeyValue(200106, "golrux_dc") + "만큼 남았습니다.");
                }
            }
        }
        if (!this.inPVP()) {
            if (this.deathcount > 0) {
                this.setDeathCount((byte) (this.getDeathCount() - 1));
            } else {
                for (int i = 4; i >= 0; --i) {
                    if (this.deathCounts[i] != 2) {
                        this.deathCounts[i] = 2;
                        this.client.send(CField.JinHillah(3, this, this.getMap()));
                        this.getMap().broadcastMessage(CField.JinHillah(10, this, this.getMap()));
                        break;
                    }
                }
            }
            if (this.deadEffect) {
                this.setDeadEffect(false);
                this.client.getSession().writeAndFlush((Object) CField.setPlayerDead());
            }
            this.client.getSession().writeAndFlush((Object) CField.OpenDeadUI(this, 1));
            if (this.eventInstance == null && this.deathcount < 0) {
                this.setKeyValue(210416, "TotalDeadTime", "1800");
                this.setKeyValue(210416, "NowDeadTime", "1800");
                this.setKeyValue(210416, "ExpDrop", "80");
                this.client.send(CField.ExpDropPenalty(true, 1800, 1800, 80, 80));
            }
        }
        if (!this.stats.checkEquipDurabilitys(this, -100)) {
            this.dropMessage(5, "An item has run out of durability but has no inventory room to go to.");
        }
    }

    public void updatePartyMemberHP() {
        if (this.party != null && this.client.getChannelServer() != null) {
            if (this.deathcount > 0) {
                this.getMap().broadcastMessage(CField.showDeathCount(this, this.deathcount));
            }
            final int channel = this.client.getChannel();
            for (final MaplePartyCharacter partychar : this.party.getMembers()) {
                if (partychar != null && partychar.getMapid() == this.getMapId() && partychar.getChannel() == channel) {
                    final MapleCharacter other = this.client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                    if (other == null) {
                        continue;
                    }
                    other.getClient().getSession().writeAndFlush((Object) CField.updatePartyMemberHP(this.getId(), (int) this.stats.getHp(), (int) this.stats.getCurrentMaxHp()));
                }
            }
        }
    }

    public void receivePartyMemberHP() {
        if (this.party == null) {
            return;
        }
        if (this.deathcount > 0) {
            this.getMap().broadcastMessage(CField.showDeathCount(this, this.deathcount));
        }
        final int channel = this.client.getChannel();
        for (final MaplePartyCharacter partychar : this.party.getMembers()) {
            if (partychar != null && partychar.getMapid() == this.getMapId() && partychar.getChannel() == channel) {
                final MapleCharacter other = this.client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                if (other == null) {
                    continue;
                }
                this.client.getSession().writeAndFlush((Object) CField.updatePartyMemberHP(other.getId(), (int) other.getStat().getHp(), (int) other.getStat().getCurrentMaxHp()));
            }
        }
    }

    public void healHP(final long delta) {
        this.addHP(delta);
        this.client.getSession().writeAndFlush((Object) CField.EffectPacket.showHealEffect(this, (int) delta, true));
        this.getMap().broadcastMessage(this, CField.EffectPacket.showHealEffect(this, (int) delta, false), false);
    }

    public void healMP(final long delta) {
        this.addMP(delta);
        this.client.getSession().writeAndFlush((Object) CField.EffectPacket.showHealEffect(this, (int) delta, true));
        this.getMap().broadcastMessage(this, CField.EffectPacket.showHealEffect(this, (int) delta, false), false);
    }

    public void playerIGDead() {
        this.stats.setHp(0L, this, true);
        this.updateSingleStat(MapleStat.HP, this.stats.getHp());
    }

    public void addHP(final long delta) {
        this.addHP(delta, false, false);
    }

    public void addHP(long delta, final int skillid) {
        if (!this.isAlive() || this.getBattleGroundChr() != null) {
            return;
        }
        if (delta > 0L && (this.getBuffedValue(SecondaryStat.StopPortion) != null || this.hasDisease(SecondaryStat.StopPortion) || this.getBuffedEffect(SecondaryStat.DebuffIncHp) != null)) {
            delta = 0L;
        }
        if (this.getBuffedEffect(SecondaryStat.NotDamaged) == null && this.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null && skillid == 400011129) {
            this.stats.setHp((this.stats.getHp() + delta <= 0L) ? 1L : (this.stats.getHp() + delta), this);
            this.updateSingleStat(MapleStat.HP, this.stats.getHp());
        }
    }

    public void addHP(long delta, final boolean ign, final boolean show) {
        if (!this.isAlive() || this.getBattleGroundChr() != null) {
            return;
        }
        if (!ign) {
            if (this.getBuffedValue(SecondaryStat.DemonFrenzy) != null && delta > 0L && this.getBuffedEffect(SecondaryStat.DemonFrenzy).getQ2() < this.getStat().getHPPercent()) {
                final int w = SkillFactory.getSkill(400011010).getEffect(this.getSkillLevel(400011010)).getW();
                delta = delta / 10L * w;
            }
            if (this.getSkillCustomValue0(143143) == 1L) {
                delta /= 10L;
            }
            if (this.getSkillCustomValue0(143145) == 1L) {
                delta /= 2L;
            }
            if (delta > 0L && (this.getBuffedValue(SecondaryStat.StopPortion) != null || this.hasDisease(SecondaryStat.StopPortion) || this.getBuffedEffect(SecondaryStat.DebuffIncHp) != null)) {
                delta = 0L;
            }
            if (this.getBuffedValue(SecondaryStat.HolyBlood) != null && delta > 0) {
                delta -= delta / 100 * 99;
            }
        }
        if (delta < 0L && this.getSkillLevel(101120109) > 0 && this.getGender() == 1) {
            final SecondaryStatEffect immuneBarrier = SkillFactory.getSkill(101120109).getEffect(this.getSkillLevel(101120109));
            boolean destory = false;
            if (this.getBuffedValue(SecondaryStat.ImmuneBarrier) != null) {
                final long duration = this.getBuffLimit(SecondaryStat.ImmuneBarrier, 101120109);
                if (this.getSkillCustomValue0(101120109) < delta) {
                    delta -= this.getSkillCustomValue0(101120109);
                    destory = true;
                } else {
                    this.setSkillCustomInfo(101120109, this.getSkillCustomValue0(101120109) - delta, 0L);
                    delta = 0L;
                }
                if (destory) {
                    while (this.getBuffedValue(101120109)) {
                        this.cancelEffect(this.getBuffedEffect(101120109));
                    }
                } else {
                    immuneBarrier.applyTo(this, true, (int) duration);
                }
            } else if (Randomizer.isSuccess(immuneBarrier.getX())) {
                this.setSkillCustomInfo(101120109, (int) (this.getStat().getCurrentMaxHp() / 100L * immuneBarrier.getX()), 0L);
                if (this.getSkillCustomValue0(101120109) < delta) {
                    delta -= this.getSkillCustomValue0(101120109);
                    destory = true;
                } else {
                    this.setSkillCustomInfo(101120109, this.getSkillCustomValue0(101120109) - delta, 0L);
                    delta = 0L;
                }
                immuneBarrier.applyTo(this, true);
                if (!destory) {
                    immuneBarrier.applyTo(this, false);
                }
            }
        }
        if (this.getBuffedEffect(SecondaryStat.NotDamaged) == null && this.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null && delta <= 0L) {
            this.stats.setHp(this.stats.getHp() + delta, this);
            this.updateSingleStat(MapleStat.HP, this.stats.getHp());
        } else {
            this.stats.setHp(this.stats.getHp() + delta, this);
            this.updateSingleStat(MapleStat.HP, this.stats.getHp());
        }
        if (show && this.getStat().getHp() != this.getStat().getMaxHp()) {
            this.client.getSession().writeAndFlush((Object) CField.EffectPacket.showHealEffect(this.client.getPlayer(), (int) delta, true));
        }
    }

    public void addMP(final long delta) {
        this.addMP(delta, false);
    }

    public void reloadChar() {
        this.getClient().getSession().writeAndFlush((Object) CField.getCharInfo(this));
        this.getMap().removePlayer(this);
        this.getMap().addPlayer(this);
    }

    public void addMP(long delta, final boolean ignore) {
        if (this.getBattleGroundChr() != null) {
            return;
        }
        if ((delta < 0L && GameConstants.isDemonSlayer(this.getJob())) || !GameConstants.isDemonSlayer(this.getJob()) || ignore) {
            if (this.getBuffedValue(SecondaryStat.Overload) != null && delta > 0L) {
                delta = 0L;
            }
            if (this.stats.setMp(this.stats.getMp() + delta, this)) {
                this.updateSingleStat(MapleStat.MP, this.stats.getMp());
            }
        }
    }

    public void addMPHP(long hpDiff, long mpDiff) {
        final Map<MapleStat, Long> statups = new EnumMap<MapleStat, Long>(MapleStat.class);
        if (this.getBattleGroundChr() != null) {
            return;
        }
        if (this.getBuffedValue(SecondaryStat.DemonFrenzy) != null && hpDiff > 0L && this.getBuffedEffect(SecondaryStat.DemonFrenzy).getQ2() < this.getStat().getHPPercent()) {
            final int w = SkillFactory.getSkill(400011010).getEffect(this.getSkillLevel(400011010)).getW();
            hpDiff = hpDiff / 10L * w;
        }
        if (this.stats.setHp(this.stats.getHp() + hpDiff, this)) {
            statups.put(MapleStat.HP, this.stats.getHp());
        }
        if ((mpDiff < 0L && GameConstants.isDemonSlayer(this.getJob())) || !GameConstants.isDemonSlayer(this.getJob())) {
            if (this.getBuffedValue(SecondaryStat.Overload) != null && mpDiff > 0L) {
                mpDiff = 0L;
            }
            if (this.stats.setMp(this.stats.getMp() + mpDiff, this)) {
                statups.put(MapleStat.MP, this.stats.getMp());
            }
        }
        if (statups.size() > 0) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(statups, this));
        }
    }

    public void updateZeroStats() {
        this.client.getSession().writeAndFlush(CWvsContext.updateZeroSecondStats(this));
    }

    public void updateAngelicStats() {
        this.client.getSession().writeAndFlush(CWvsContext.updateAngelicBusterInfo(this));
    }

    public void updateSingleStat(final MapleStat stat, final long newval) {
        this.updateSingleStat(stat, newval, false);
    }

    public void updateSingleStat(final MapleStat stat, final long newval, final boolean itemReaction) {
        final Map<MapleStat, Long> statup = new EnumMap<MapleStat, Long>(MapleStat.class);
        statup.put(stat, newval);
        this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(statup, itemReaction, this));
    }

    public void gainExp(long total, final boolean show, final boolean inChat, final boolean white) {
        try {
            final long prevexp = this.getExp();
            long needed = this.getNeededExp();
            if (total > 0L) {
                this.stats.checkEquipLevels(this, (int) total);
            }
            if (this.level >= 300) {
                this.setExp(0L);
                total = 0L;
            } else {
                boolean leveled = false;
                final long tot = this.exp + total;
                if (tot >= needed) {
                    this.exp += total;
                    leveled = true;
                    while (this.exp >= needed && this.level < 300) {
                        this.levelUp();
                        needed = this.getNeededExp();
                        if (this.level >= 300) {
                            this.setExp(0L);
                        }
                    }
                } else {
                    this.exp += total;
                }
            }
            if (total != 0L) {
                if (this.exp < 0L) {
                    if (total > 0L) {
                        this.setExp(needed);
                    } else if (total < 0L) {
                        this.setExp(0L);
                    }
                }
                this.updateSingleStat(MapleStat.EXP, this.getExp());
                if (show) {
                    this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.GainEXP_Others(total, inChat, white));
                }
            }
        } catch (Exception e) {
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", e);
        }
    }

    public void gainExpMonster(final long gain, final boolean show, final boolean white) {
        long total = gain;
        int flag = 0;
        final int eventBonusExp = (int) (ServerConstants.EventBonusExp / 100.0 * gain);
        final int weddingExp = (int) (ServerConstants.WeddingExp / 100.0 * gain);
        final int partyExp = (int) (ServerConstants.PartyExp / 100.0 * gain);
        int itemEquipExp = 0;
        if (this.getKeyValue(27040, "runnigtime") > 0L) {
            boolean equip = false;
            if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-31)) != null && MapleItemInformationProvider.getInstance().getName(this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-31)).getItemId()).startsWith("\uc815\ub839\uc758")) {
                equip = true;
            }
            if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-17)) != null && MapleItemInformationProvider.getInstance().getName(this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-17)).getItemId()).startsWith("\uc815\ub839\uc758")) {
                equip = true;
            }
            if (equip) {
                final long runnigtime = this.getKeyValue(27040, "runnigtime");
                int level = (int) runnigtime / 3600;
                if (level >= 2) {
                    level = 2;
                }
                final int expplus = (level == 2) ? 30 : ((level == 1) ? 20 : 10);
                itemEquipExp += (int) (gain * (expplus / 100.0));
            }
        }
        final int pcExp = (int) (ServerConstants.PcRoomExp / 100.0 * gain);
        int rainbowWeekExp = (int) (ServerConstants.RainbowWeekExp / 100.0 * gain);
        final int boomupExp = (int) (ServerConstants.BoomupExp / 100.0 * gain);
        final int portionExp = (int) (ServerConstants.PortionExp / 100.0 * gain);
        int skillExp = 0;
        if (Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) {
            rainbowWeekExp = (int) (0.15 * gain);
        }
        if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-21)) != null) {
            final int itemid = this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-21)).getItemId();
            if (itemid >= 1143800 && itemid <= 1143814) {
                final int exp = 5 * (itemid - 1143800 + 1);
                itemEquipExp += (int) (gain * (exp / 100.0));
            }
        }
        if (this.getSkillLevel(20021110) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(20021110).getEffect(this.getSkillLevel(20021110)).getEXPRate() / 100L);
        }
        if (this.getStat().expBuffZero > 0.0) {
            skillExp += (int) (gain * this.getStat().expBuffZero / 100.0);
        }
        if (this.getStat().expBuffUnion > 0.0) {
            skillExp += (int) (gain * this.getStat().expBuffUnion / 100.0);
        }
        if (this.getSkillLevel(4120045) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(4120045).getEffect(this.getSkillLevel(4120045)).getX() / 100L);
        }
        if (this.getSkillLevel(80001040) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(80001040).getEffect(this.getSkillLevel(80001040)).getEXPRate() / 100L);
        }
        if (this.getSkillLevel(91000001) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(91000001).getEffect(this.getSkillLevel(91000001)).getEXPRate() / 100L);
        }
        if (this.getSkillLevel(131000016) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(131000016).getEffect(this.getSkillLevel(131000016)).getEXPRate() / 100L);
        }
        if (this.getSkillLevel(135000021) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(135000021).getEffect(this.getSkillLevel(135000021)).getEXPRate() / 100L);
        }
        if (this.getSkillLevel(80000602) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(80000602).getEffect(this.getSkillLevel(80000602)).getEXPRate() / 100L);
        }
        if (this.getSkillLevel(80000420) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(80000420).getEffect(this.getSkillLevel(80000420)).getEXPRate() / 100L);
        }
        if (this.getSkillLevel(80003045) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(80003045).getEffect(this.getSkillLevel(80003045)).getExpRPerM() / 100.0);
        }
        if (this.getSkillLevel(80000577) > 0) {
            skillExp += (int) (gain * SkillFactory.getSkill(80000577).getEffect(this.getSkillLevel(80000577)).getEXPRate() / 100L);
        }
        if (this.getSkillLevel(80000589) > 0) {
            skillExp += (int) (gain * 20L / 100L);
        }
        if (this.getKeyValue(19019, "id") >= 1L) {
            final int headId = (int) this.getKeyValue(19019, "id");
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (ii.getItemInformation(headId) != null) {
                final int nickSkill = ii.getItemInformation(headId).nickSkill;
                if (SkillFactory.getSkill(nickSkill) != null) {
                    final SecondaryStatEffect nickEffect = SkillFactory.getSkill(nickSkill).getEffect(1);
                    skillExp += (int) (gain * nickEffect.getIndieExp() / 100L);
                }
            }
        }
        int buffExp = 0;
        if (this.getBuffedEffect(SecondaryStat.DiceRoll) != null) {
            final SecondaryStatEffect effect = this.getBuffedEffect(SecondaryStat.DiceRoll);
            int thirddice;
            int doubledice;
            int dice;
            if (this.getDice() >= 100) {
                thirddice = this.getDice() / 100;
                doubledice = (this.getDice() - thirddice * 100) / 10;
                dice = this.getDice() - this.getDice() / 10 * 10;
            } else {
                thirddice = 1;
                doubledice = this.getDice() / 10;
                dice = this.getDice() - doubledice * 10;
            }
            int value;
            if (dice == 6 || doubledice == 6 || thirddice == 6) {
                if (dice == 6 && doubledice == 6 && thirddice == 6) {
                    value = effect.getEXPRate() + 15;
                } else if ((dice == 6 && doubledice == 6) || (dice == 6 && thirddice == 6) || (thirddice == 6 && doubledice == 6)) {
                    value = effect.getEXPRate() + 10;
                } else {
                    value = effect.getEXPRate();
                }
            } else {
                value = 0;
            }
            buffExp += (int) (gain * value / 100L);
        }
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> buff : this.effects) {
            if (buff.left == SecondaryStat.IndieExp) {
                buffExp += (int) (gain * buff.right.value / 100L);
            }
            if (buff.left == SecondaryStat.HolySymbol) {
                buffExp += (int) (gain * buff.right.value / 100L);
            }
        }
        final int restExp = (int) (ServerConstants.RestExp / 100.0 * gain);
        final int itemExp = (int) (ServerConstants.ItemExp / 100.0 * gain);
        final int valueExp = (int) (ServerConstants.ValueExp / 100.0 * gain);
        final int bonusExp = (int) (this.getStat().expBuff / 100.0 * gain);
        int bloodExp = 0;
        if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-12)) != null) {
            if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-12)).getItemId() == 1114000) {
                bloodExp = (int) (gain * 0.1);
            }
        } else if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-13)) != null) {
            if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-13)).getItemId() == 1114000) {
                bloodExp = (int) (gain * 0.1);
            }
        } else if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-15)) != null) {
            if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-15)).getItemId() == 1114000) {
                bloodExp = (int) (gain * 0.1);
            }
        } else if (this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-16)) != null && this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-16)).getItemId() == 1114000) {
            bloodExp = (int) (gain * 0.1);
        }
        final int iceExp = (int) (ServerConstants.IceExp / 100.0 * gain);
        Pair<Integer, Integer> burningExp = null;
        if (this.getMap().getAllNormalMonstersThreadsafe().size() > 0 && !this.getMap().isTown() && !GameConstants.로미오줄리엣(this.getMap().getId()) && !GameConstants.사냥컨텐츠맵(this.getId()) && this.getMap().isSpawnPoint()) {
            burningExp = new Pair<Integer, Integer>((int) (gain * this.getMap().getBurning() / 10L), this.getMap().getBurning() * 10);
        }
        final int hpLiskExp = (int) (ServerConstants.HpLiskExp / 100.0 * gain);
        final int fieldBonusExp = (int) (ServerConstants.FieldBonusExp / 100.0 * gain);
        final int eventBonusExp2 = (int) (ServerConstants.EventBonusExp / 100.0 * gain);
        final int fieldBonusExp2 = (int) (ServerConstants.FieldBonusExp2 / 100.0 * gain);
        if (eventBonusExp > 0) {
            ++flag;
            total += eventBonusExp;
        }
        if (weddingExp > 0) {
            flag += 16;
            total += weddingExp;
        }
        if (partyExp > 0) {
            flag += 32;
            total += partyExp;
        }
        if (itemEquipExp > 0) {
            flag += 64;
            total += itemEquipExp;
        }
        if (pcExp > 0) {
            flag += 128;
            total += pcExp;
        }
        if (rainbowWeekExp > 0) {
            flag += 256;
            total += rainbowWeekExp;
        }
        if (boomupExp > 0) {
            flag += 512;
            total += boomupExp;
        }
        if (portionExp > 0) {
            flag += 1024;
            total += portionExp;
        }
        if (skillExp > 0) {
            flag += 2048;
            total += skillExp;
        }
        if (buffExp > 0) {
            flag += 4096;
            total += buffExp;
        }
        if (restExp > 0) {
            flag += 8192;
            total += restExp;
        }
        if (itemExp > 0) {
            flag += 16384;
            total += itemExp;
        }
        if (valueExp > 0) {
            flag += 131072;
            total += valueExp;
        }
        if (bonusExp > 0) {
            flag += 524288;
            total += bonusExp;
        }
        if (bloodExp > 0) {
            flag += 1048576;
            total += bloodExp;
        }
        if (iceExp > 0) {
            flag += 2097152;
            total += iceExp;
        }
        if (burningExp != null && burningExp.left > 0) {
            flag += 4194304;
            total += burningExp.left;
        }
        if (hpLiskExp > 0) {
            flag += 8388608;
            total += hpLiskExp;
        }
        if (fieldBonusExp > 0) {
            flag += 16777216;
            total += fieldBonusExp;
        }
        if (eventBonusExp2 > 0) {
            flag += 67108864;
            total += eventBonusExp2;
        }
        if (fieldBonusExp2 > 0) {
            flag += 268435456;
            total += fieldBonusExp2;
        }
        if (gain > 0L && total < gain) {
            total = 2147483647L;
        }
        final long arcaneStone = (this.getKeyValue(1472, "exp") == -1L) ? 0L : this.getKeyValue(1472, "exp");
        if (arcaneStone + total >= 20000000000000L) {
            this.setKeyValue(1472, "exp", "20000000000000");
        } else {
            this.setKeyValue(1472, "exp", arcaneStone + total + "");
        }
        if (total > 0L) {
            this.stats.checkEquipLevels(this, total);
        }
        final long needed = this.getNeededExp();
        if (this.level >= 300) {
            this.setExp(0L);
            total = 0L;
        } else {
            boolean leveled = false;
            if (this.exp + total >= needed) {
                this.exp += total;
                while (this.exp >= needed) {
                    this.levelUp();
                    leveled = true;
                    if (this.level >= 300) {
                        this.setExp(0L);
                        total = 0L;
                    }
                }
            } else {
                this.exp += total;
            }
        }
        if (gain != 0L) {
            if (this.exp < 0L) {
                if (gain > 0L) {
                    this.setExp(this.getNeededExp());
                } else if (gain < 0L) {
                    this.setExp(0L);
                }
            }
            this.updateSingleStat(MapleStat.EXP, this.getExp());
            if (show) {
                this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.GainEXP_Monster(this, gain, white, flag, eventBonusExp, weddingExp, partyExp, itemEquipExp, pcExp, rainbowWeekExp, boomupExp, portionExp, skillExp, buffExp, restExp, itemExp, valueExp, bonusExp, bloodExp, iceExp, burningExp, hpLiskExp, fieldBonusExp, eventBonusExp2, fieldBonusExp2));
            }
        }
    }

    public void forceReAddItem_NoUpdate(final Item item, final MapleInventoryType type) {
        this.getInventory(type).removeSlot(item.getPosition());
        this.getInventory(type).addFromDB(item);
    }

    public void forceReAddItem(final Item item, final MapleInventoryType type) {
        this.forceReAddItem_NoUpdate(item, type);
        if (type != MapleInventoryType.UNDEFINED) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, type, item));
        }
    }

    public void silentPartyUpdate() {
        if (this.party != null) {
            World.Party.updateParty(this.party.getId(), PartyOperation.SILENT_UPDATE, new MaplePartyCharacter(this));
        }
    }

    public boolean isSuperGM() {
        return this.gmLevel >= ServerConstants.PlayerGMRank.SUPERGM.getLevel();
    }

    public boolean isIntern() {
        return this.gmLevel >= ServerConstants.PlayerGMRank.INTERN.getLevel();
    }

    public boolean isGM() {
        return this.gmLevel >= ServerConstants.PlayerGMRank.GM.getLevel();
    }

    public boolean isAdmin() {
        return this.gmLevel >= ServerConstants.PlayerGMRank.ADMIN.getLevel();
    }

    public int getGMLevel() {
        return this.gmLevel;
    }

    public boolean hasGmLevel(final int level) {
        return this.gmLevel >= level;
    }

    public void setGMLevel(final byte level) {
        this.gmLevel = level;
    }

    public int getLinkMobCount() {
        return this.LinkMobCount;
    }

    public void setLinkMobCount(final int count) {
        this.LinkMobCount = ((count > 9999) ? 9999 : count);
    }

    public void gainItem(final int code, final int quantity) {
        if (quantity >= 0) {
            MapleInventoryManipulator.addById(this.client, code, (short) quantity, StringUtil.getAllCurrentTime() + "\uc5d0 " + "gainItem\ub85c \uc5bb\uc740 \uc544\uc774\ud15c");
        } else {
            MapleInventoryManipulator.removeById(this.client, GameConstants.getInventoryType(this.id), this.id, -quantity, true, false);
        }
    }

    public void gainSpecialItem(final int code, final int quantity) {
        if (quantity >= 0) {
            MapleInventoryManipulator.addById(this.client, code, (short) quantity, StringUtil.getAllCurrentTime() + "에 " + "gainSpecialItem로 얻은 아이템", true);
        } else {
            MapleInventoryManipulator.removeById(this.client, GameConstants.getInventoryType(this.id), this.id, -quantity, true, false);
        }
    }

    public final Equip gainItem(final int id, final short quantity, final boolean randomStats, final long period, final String gm_log) {
        final Equip equip = null;
        if (quantity >= 0) {
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final MapleInventoryType type = GameConstants.getInventoryType(id);
            if (!MapleInventoryManipulator.checkSpace(this.client, id, quantity, "")) {
                return equip;
            }
            if ((type.equals(MapleInventoryType.EQUIP) || type.equals(MapleInventoryType.CODY)) && !GameConstants.isThrowingStar(id) && !GameConstants.isBullet(id)) {
                final Equip item = (Equip) ii.getEquipById(id);
                if (period > 0L) {
                    item.setExpiration(System.currentTimeMillis() + period * 24L * 60L * 60L * 1000L);
                }
                item.setGMLog(StringUtil.getAllCurrentTime() + "에 " + gm_log);
                MapleInventoryManipulator.addbyItem(this.client, item);
                return item;
            }
            MapleInventoryManipulator.addById(this.client, id, quantity, "", null, period, StringUtil.getAllCurrentTime() + "에 " + gm_log);
        } else {
            MapleInventoryManipulator.removeById(this.client, GameConstants.getInventoryType(id), id, -quantity, true, false);
        }
        this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, quantity, true));
        return equip;
    }

    public final MapleInventory getInventory(final MapleInventoryType type) {
        return this.inventory[type.ordinal()];
    }

    public final MapleInventory getInventory(final byte type) {
        return this.inventory[MapleInventoryType.getByType(type).ordinal()];
    }

    public final MapleInventory[] getInventorys() {
        return this.inventory;
    }

    public final void expirationTask(final boolean pending, final boolean firstLoad) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (pending) {
            if (this.pendingExpiration != null) {
                for (final Integer z : this.pendingExpiration) {
                    this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.itemExpired(z));
                    if (!firstLoad) {
                        final Pair<Integer, String> replace = ii.replaceItemInfo(z);
                        if (replace == null || replace.left <= 0 || replace.right.length() <= 0) {
                            continue;
                        }
                        this.dropMessageGM(5, replace.right);
                    }
                }
            }
            this.pendingExpiration = null;
            if (this.pendingSkills != null) {
                this.client.getSession().writeAndFlush((Object) CWvsContext.updateSkills(this.pendingSkills));
                for (final Skill z2 : this.pendingSkills.keySet()) {
                    if (SkillFactory.getSkillName(z2.getId()).equals("")) {
                        this.client.getSession().writeAndFlush((Object) CWvsContext.serverNotice(5, this.name, "[" + MapleItemInformationProvider.getInstance().getName(z2.getId()) + "] 스킬이 기간이 다 되어 사라졌습니다."));
                    } else {
                        this.client.getSession().writeAndFlush((Object) CWvsContext.serverNotice(5, this.name, "[" + SkillFactory.getSkillName(z2.getId()) + "] 스킬이 기간이 다 되어 사라졌습니다."));
                    }
                }
            }
            this.pendingSkills = null;
            return;
        }
        final MapleQuestStatus stat = this.getQuestNoAdd(MapleQuest.getInstance(122700));
        final List<Integer> ret = new ArrayList<Integer>();
        final long currenttime = System.currentTimeMillis();
        final List<Triple<MapleInventoryType, Item, Boolean>> toberemove = new ArrayList<Triple<MapleInventoryType, Item, Boolean>>();
        final List<Item> tobeunlock = new ArrayList<Item>();
        for (final MapleInventoryType inv : MapleInventoryType.values()) {
            for (final Item item : this.getInventory(inv)) {
                final long expiration = item.getExpiration();
                if ((expiration != -1L && !GameConstants.isPet(item.getItemId()) && currenttime > expiration) || (firstLoad && ii.isLogoutExpire(item.getItemId()))) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        tobeunlock.add(item);
                    } else {
                        if (currenttime <= expiration) {
                            continue;
                        }
                        toberemove.add(new Triple<MapleInventoryType, Item, Boolean>(inv, item, false));
                    }
                } else if (item.getItemId() == 5000054 && item.getPet() != null && item.getPet().getSecondsLeft() <= 0) {
                    toberemove.add(new Triple<MapleInventoryType, Item, Boolean>(inv, item, false));
                } else {
                    if (item.getPosition() != -59 || (stat != null && stat.getCustomData() != null && Long.parseLong(stat.getCustomData()) >= currenttime)) {
                        continue;
                    }
                    toberemove.add(new Triple<MapleInventoryType, Item, Boolean>(inv, item, true));
                }
            }
        }
        for (final Triple<MapleInventoryType, Item, Boolean> itemz : toberemove) {
            final Item item2 = itemz.getMid();
            this.getInventory(itemz.getLeft()).removeItem(item2.getPosition(), item2.getQuantity(), false);
            if (itemz.getRight() && this.getInventory(GameConstants.getInventoryType(item2.getItemId())).getNextFreeSlot() > -1) {
                item2.setPosition(this.getInventory(GameConstants.getInventoryType(item2.getItemId())).getNextFreeSlot());
                this.getInventory(GameConstants.getInventoryType(item2.getItemId())).addFromDB(item2);
            } else {
                ret.add(item2.getItemId());
            }
            if (!firstLoad) {
                final Pair<Integer, String> replace2 = ii.replaceItemInfo(item2.getItemId());
                if (replace2 == null || replace2.left <= 0) {
                    continue;
                }
                Item theNewItem = null;
                if (GameConstants.getInventoryType(replace2.left) == MapleInventoryType.EQUIP) {
                    theNewItem = ii.getEquipById(replace2.left);
                    theNewItem.setPosition(item2.getPosition());
                } else {
                    theNewItem = new Item(replace2.left, item2.getPosition(), (short) 1, 0);
                }
                this.getInventory(itemz.getLeft()).addFromDB(theNewItem);
            }
        }
        for (final Item itemz2 : tobeunlock) {
            itemz2.setExpiration(-1L);
            itemz2.setFlag(itemz2.getFlag() - ItemFlag.LOCK.getValue());
        }
        this.pendingExpiration = ret;
        final Map<Skill, SkillEntry> skilz = new HashMap<Skill, SkillEntry>();
        final List<Skill> toberem = new ArrayList<Skill>();
        for (final Map.Entry<Skill, SkillEntry> skil : this.skills.entrySet()) {
            if (skil.getValue().expiration != -1L && currenttime > skil.getValue().expiration) {
                toberem.add(skil.getKey());
            }
        }
        for (final Skill skil2 : toberem) {
            skilz.put(skil2, new SkillEntry(0, (byte) 0, -1L));
            this.skills.remove(skil2);
            this.changed_skills = true;
        }
        this.pendingSkills = skilz;
        if (stat != null && stat.getCustomData() != null && Long.parseLong(stat.getCustomData()) < currenttime) {
            this.quests.remove(MapleQuest.getInstance(7830));
            this.quests.remove(MapleQuest.getInstance(122700));
        }
    }

    public MapleShop getShop() {
        return this.shop;
    }

    public void setShop(final MapleShop shop) {
        this.shop = shop;
    }

    public long getMeso() {
        return this.meso;
    }

    public final int[] getSavedLocations() {
        return this.savedLocations;
    }

    public int getSavedLocation(final SavedLocationType type) {
        return this.savedLocations[type.getValue()];
    }

    public void saveLocation(final SavedLocationType type) {
        this.savedLocations[type.getValue()] = this.getMapId();
        this.changed_savedlocations = true;
    }

    public void saveLocation(final SavedLocationType type, final int mapz) {
        this.savedLocations[type.getValue()] = mapz;
        this.changed_savedlocations = true;
    }

    public void clearSavedLocation(final SavedLocationType type) {
        this.savedLocations[type.getValue()] = -1;
        this.changed_savedlocations = true;
    }

    public void gainMeso(final long gain, final boolean show) {
        this.gainMeso(gain, show, false);
    }

    public void gainMeso(final long gain, final boolean show, final boolean inChat) {
        this.gainMeso(gain, show, inChat, false, false);
    }

    public void gainMeso(final long gain, final boolean show, final boolean inChat, final boolean isPet, final boolean monster) {
        if (this.meso + gain < 0L) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this));
            return;
        }
        this.meso += gain;
        final int jan = 0;
        if (!monster || this.getGuild() != null) {
        }
        final Map<MapleStat, Long> statup = new EnumMap<MapleStat, Long>(MapleStat.class);
        statup.put(MapleStat.MESO, this.meso);
        if (isPet) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(statup, false, false, this, true));
            this.client.getSession().writeAndFlush((Object) CWvsContext.onMesoPickupResult((int) gain));
        } else {
            this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(statup, true, false, this, false));
        }
        if (show) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.showMesoGain(gain, isPet, inChat));
        }
        if (inChat) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.showMesoGain(gain, inChat, jan));
        }
    }

    public void controlMonster(final MapleMonster monster, final boolean aggro) {
        if (monster == null) {
            return;
        }
        monster.setController(this);
        this.controlled.add(monster);
        if (monster.getOwner() == -1) {
            this.client.getSession().writeAndFlush((Object) MobPacket.controlMonster(monster, false, aggro));
        } else if (monster.getOwner() == this.getId()) {
            this.client.getSession().writeAndFlush((Object) MobPacket.controlMonster(monster, false, aggro));
        }
    }

    public void stopControllingMonster(final MapleMonster monster) {
        if (monster == null) {
            return;
        }
        if (this.controlled.contains(monster)) {
            this.controlled.remove(monster);
        }
    }

    public void checkMonsterAggro(final MapleMonster monster) {
        if (monster == null) {
            return;
        }
        if (monster.getController() == this) {
            monster.setControllerHasAggro(true);
        } else {
            monster.switchController(this, true);
        }
    }

    public int getControlledSize() {
        return this.controlled.size();
    }

    public int getAccountID() {
        return this.accountid;
    }

    public void mobKilled(final int id, final int skillID) {
        for (final MapleQuestStatus q : this.quests.values()) {
            if (q.getStatus() == 1) {
                if (!q.hasMobKills()) {
                    continue;
                }
                if ((q.getQuest().getId() >= 100829 && q.getQuest().getId() <= 100861) || (q.getQuest().getId() >= 49000 && q.getQuest().getId() <= 49018) || !q.mobKilled(id, skillID, this)) {
                    continue;
                }
                this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.updateQuestMobKills(q));
                if (!q.getQuest().canComplete(this, null)) {
                    continue;
                }
                this.client.getSession().writeAndFlush((Object) CWvsContext.getShowQuestCompletion(q.getQuest().getId()));
            }
        }
        for (final MapleQuestStatus q : this.client.getQuests().values()) {
            if (q.getStatus() == 1) {
                if (!q.hasMobKills()) {
                    continue;
                }
                if ((q.getQuest().getId() >= 100829 && q.getQuest().getId() <= 100861) || (q.getQuest().getId() >= 49000 && q.getQuest().getId() <= 49018) || !q.mobKilled(id, skillID, this)) {
                    continue;
                }
                this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.updateQuestMobKills(q));
                if (!q.getQuest().canComplete(this, null)) {
                    continue;
                }
                this.client.getSession().writeAndFlush((Object) CWvsContext.getShowQuestCompletion(q.getQuest().getId()));
            }
        }
    }

    public final List<MapleQuestStatus> getStartedQuests() {
        final List<MapleQuestStatus> ret = new LinkedList<MapleQuestStatus>();
        for (final MapleQuestStatus q : this.quests.values()) {
            if (q.getStatus() == 1 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<MapleQuestStatus> getCompletedQuests() {
        final List<MapleQuestStatus> ret = new LinkedList<MapleQuestStatus>();
        for (final MapleQuestStatus q : this.quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<Pair<Integer, Long>> getCompletedMedals() {
        final List<Pair<Integer, Long>> ret = new ArrayList<Pair<Integer, Long>>();
        for (final MapleQuestStatus q : this.quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom() && !q.getQuest().isBlocked() && q.getQuest().getMedalItem() > 0 && GameConstants.getInventoryType(q.getQuest().getMedalItem()) == MapleInventoryType.EQUIP) {
                ret.add(new Pair<Integer, Long>(q.getQuest().getId(), q.getCompletionTime()));
            }
        }
        return ret;
    }

    public Map<Skill, SkillEntry> getSkills() {
        return Collections.unmodifiableMap((Map<? extends Skill, ? extends SkillEntry>) this.skills);
    }

    public int getTotalSkillLevel(Skill skill) {
        if (skill == null) {
            return 0;
        }
        if (skill.getId() == 5011007 || skill.getId() == 400051038 || skill.getId() == 33000036 || skill.getId() == 80001770 || skill.getId() == 80002887 || skill.getId() == 80001242 || skill.getId() == 80001965 || skill.getId() == 80001966 || skill.getId() == 80001967 || skill.getId() == 155001205) {
            return 1;
        }
        if (GameConstants.isAngelicBlessSkill(skill)) {
            return 1;
        }
        if (GameConstants.isSaintSaverSkill(skill.getId())) {
            return 1;
        }
        if (skill.getId() == 155101204) {
            skill = SkillFactory.getSkill(155101104);
        }
        if (skill.getId() == 155111202) {
            skill = SkillFactory.getSkill(155111102);
        }
        if (skill.getId() == 11121014) {
            skill = SkillFactory.getSkill(11121005);
        }
        if (skill.getId() == 400011089) {
            skill = SkillFactory.getSkill(400011088);
        }
        if (skill.getId() == 4221019) {
            skill = SkillFactory.getSkill(4211006);
        }
        if (skill.getId() == 2311014 || skill.getId() == 2311015 || skill.getId() == 2321016) {
            return 10;
        }
        final SkillEntry ret = this.skills.get(skill);
        if (ret == null || ret.skillevel <= 0) {
            return 0;
        }
        return Math.min(skill.getTrueMax(), ret.skillevel + (skill.isBeginnerSkill() ? 0 : (this.stats.combatOrders + ((skill.getMaxLevel() > 10) ? this.stats.incAllskill : 0) + this.stats.getSkillIncrement(skill.getId()))));
    }

    public long getSkillExpiry(final Skill skill) {
        if (skill == null) {
            return 0L;
        }
        final SkillEntry ret = this.skills.get(skill);
        if (ret == null || ret.skillevel <= 0) {
            return 0L;
        }
        return ret.expiration;
    }

    public int getSkillLevel(final Skill skill) {
        if (skill == null) {
            return 0;
        }
        final SkillEntry ret = this.skills.get(skill);
        if (ret == null || ret.skillevel <= 0) {
            return 0;
        }
        int skilllv = ret.skillevel;
        if (skill.combatOrders()) {
            final int up = this.getBuffedValue(400001004) ? 1 : (this.getBuffedValue(1211011) ? 2 : 0);
            if (up > 0) {
                skilllv += up;
            }
        }
        return skilllv;
    }

    public byte getMasterLevel(final int skill) {
        return this.getMasterLevel(SkillFactory.getSkill(skill));
    }

    public byte getMasterLevel(final Skill skill) {
        final SkillEntry ret = this.skills.get(skill);
        if (ret == null) {
            return 0;
        }
        return ret.masterlevel;
    }

    public int getStarForce() {
        int force = 0;
        for (final Item iv : this.getInventory(MapleInventoryType.EQUIPPED)) {
            if (iv instanceof Equip) {
                force += ((Equip) iv).getEnhance();
            }
        }
        return force;
    }

    public void levelUp() {
        this.remainingAp += 5;
        this.stats.recalcLocalStats(this);
        long maxhp = this.stats.getMaxHp();
        long maxmp = this.stats.getMaxMp();
        if (GameConstants.isWarrior(this.job)) {
            if (GameConstants.isDemonAvenger(this.job)) {
                maxhp += Randomizer.rand(110, 120);
            } else if (GameConstants.isZero(this.job)) {
                maxhp += Randomizer.rand(80, 90);
            } else {
                maxhp += Randomizer.rand(55, 60);
                maxmp += Randomizer.rand(8, 10);
            }
        } else if (GameConstants.isMagician(this.job)) {
            if (GameConstants.isBattleMage(this.job)) {
                maxhp += Randomizer.rand(40, 50);
                maxmp += Randomizer.rand(17, 20);
            } else if (GameConstants.isLara(this.job)) {
                maxhp += Randomizer.rand(20, 23);
                maxmp += Randomizer.rand(33, 40);
            } else {
                maxhp += Randomizer.rand(10, 13);
                maxmp += Randomizer.rand(43, 50);
                if (this.getSkillLevel(20040221) > 0) {
                    maxhp += 190L;
                }
            }
        } else {
            maxhp += Randomizer.rand(20, 30);
            maxmp += Randomizer.rand(15, 20);
        }
        this.exp -= this.getNeededExp();
        ++this.level;
        if (this.level >= 200) {
            FileoutputUtil.log(FileoutputUtil.레벨업로그, "[레벨업] 계정번호 : " + this.getClient().getAccID() + " | " + this.getName() + "이 " + this.level + "로 레벨업.");
        }
        boolean unionz = false;
        for (final MapleUnion union : this.getUnions().getUnions()) {
            if (union.getCharid() == this.id) {
                union.setLevel(this.level);
                unionz = true;
            }
        }
        if (!unionz) {
            if (this.level >= 60 && !GameConstants.isZero(this.job)) {
                this.getUnions().getUnions().add(new MapleUnion(this.id, this.level, this.job, 0, 0, -1, 0, this.name, this.getStarForce(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            } else if (this.level >= 130 && GameConstants.isZero(this.job)) {
                this.getUnions().getUnions().add(new MapleUnion(this.id, this.level, this.job, 0, 0, -1, 0, this.name, this.getStarForce(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            }
        }
        final int linkSkill = GameConstants.getMyLinkSkill(this.getJob());
        if (linkSkill > 0 && this.getSkillLevel(linkSkill) != ((this.getLevel() >= 120) ? 2 : 1)) {
            this.changeSkillLevel(linkSkill, (byte) ((this.getLevel() >= 120) ? 2 : 1), (byte) 2);
        }
        if (GameConstants.isYeti(this.job) || GameConstants.isPinkBean(this.job)) {
            this.AutoTeachSkill();
        }
        if (this.level == 300) {
            this.exp = 0L;
        }
        if (this.level <= 100 && !GameConstants.isYeti(this.job) && !GameConstants.isPinkBean(this.job)) {
            this.autoJob();
        }
        if (this.level == 140) {
            this.getClient().getSession().writeAndFlush(CWvsContext.updateHyperPresets(this, 0, (byte) 0));
            this.setKeyValue(2498, "hyperstats", "" + 0);
        }
        if (GameConstants.isZero(this.job)) {
            if (this.level == 100) {
                this.changeSkillLevel(SkillFactory.getSkill(100000267), (byte) 1, (byte) 1);
            }
            if (this.level == 110) {
                this.changeSkillLevel(SkillFactory.getSkill(100001261), (byte) 1, (byte) 1);
            }
            if (this.level == 120) {
                this.changeSkillLevel(SkillFactory.getSkill(100001274), (byte) 1, (byte) 1);
            }
            if (this.level == 140) {
                this.changeSkillLevel(SkillFactory.getSkill(100001272), (byte) 1, (byte) 1);
            }
            if (this.level == 160) {
                this.changeSkillLevel(SkillFactory.getSkill(100001283), (byte) 1, (byte) 1);
            }
            if (this.level == 200) {
                this.changeSkillLevel(SkillFactory.getSkill(100001005), (byte) 1, (byte) 1);
            }
        }
        if (this.level >= 200) {
            if (this.getQuestStatus(1465) != 2) {
                this.forceCompleteQuest(1465);
                if (!GameConstants.isPinkBean(this.job) && !GameConstants.isYeti(this.job)) {
                    MatrixHandler.gainMatrix(this);
                    MatrixHandler.gainVCoreLevel(this);
                    this.client.getSession().writeAndFlush((Object) CField.environmentChange("Effect/5skill.img/screen", 16));
                    this.client.getSession().writeAndFlush((Object) CField.playSound("Sound/SoundEff.img/5thJob"));
                }
                if (GameConstants.isZero(this.job)) {
                    this.changeSingleSkillLevel(SkillFactory.getSkill(100001005), 1, (byte) 1);
                }
            }
            if (this.getQuestStatus(1466) != 2) {
                this.forceCompleteQuest(1466);
            }
        }
        if (this.level >= 250 && this.level % 25 == 0 && !this.isGM()) {
            final StringBuilder sb = new StringBuilder("[공지] ");
            final Item medal = this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-21));
            if (medal != null) {
                sb.append("<");
                sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
                sb.append("> ");
            }
            sb.append(this.getName());
            sb.append("님이 " + this.level + "레벨을 달성했습니다! 모두 축하해 주세요!");
            World.Broadcast.broadcastMessage(CField.getGameMessage(8, sb.toString()));
        }
        maxhp = Math.min(500000L, Math.abs(maxhp));
        maxmp = Math.min(500000L, Math.abs(maxmp));
        int itemid = 0;
        boolean change = false;
        if (this.level == 20 && GameConstants.isDualBlade(this.job)) {
            itemid = 1342000;
            change = true;
        }
        if (this.level == 100 || this.level == 10 || this.level == 30 || this.level == 60) {
            if (GameConstants.isDemonSlayer(this.job) || GameConstants.isDemonAvenger(this.job)) {
                itemid = 1099004;
                change = (this.level >= 100);
            } else if (GameConstants.isKaiser(this.job)) {
                itemid = 1352503;
                change = (this.level >= 100);
            } else if (GameConstants.isAngelicBuster(this.job)) {
                itemid = 1352604;
                change = (this.level >= 100);
            } else if (GameConstants.isCain(this.job)) {
                itemid = 1354013;
                change = (this.level >= 100);
            } else if (GameConstants.isMichael(this.job)) {
                itemid = 1098003;
                change = (this.level >= 100);
            } else if (GameConstants.isMercedes(this.job)) {
                itemid = 1352003;
                change = (this.level >= 100);
            } else if (GameConstants.isEunWol(this.job)) {
                itemid = 1353103;
                change = (this.level >= 100);
            } else if (GameConstants.isPhantom(this.job)) {
                itemid = 1352103;
                change = (this.level >= 100);
            } else if (GameConstants.isPathFinder(this.job)) {
                itemid = 1353703;
                change = (this.level >= 100);
            } else if (GameConstants.isHoyeong(this.job)) {
                itemid = 1353803;
                change = (this.level >= 100);
            } else if (GameConstants.isArk(this.job)) {
                itemid = 1353603;
                change = (this.level >= 100);
            } else if (GameConstants.isArk(this.job)) {
                itemid = 1353603;
                change = (this.level >= 100);
            } else if (GameConstants.isIllium(this.job)) {
                itemid = 1353503;
                change = (this.level >= 100);
            } else if (GameConstants.isBlaster(this.job)) {
                itemid = 1353403;
                change = (this.level >= 100);
            } else if (GameConstants.isKadena(this.job)) {
                itemid = 1353303;
                change = (this.level >= 100);
            } else if (GameConstants.isLuminous(this.job)) {
                itemid = 1352403;
                change = (this.level >= 100);
            } else if (GameConstants.isKinesis(this.job)) {
                itemid = 1353203;
                change = (this.level >= 100);
            } else if (GameConstants.isAdel(this.job)) {
                itemid = 1354003;
                change = (this.level >= 100);
            } else if (GameConstants.isLara(this.job)) {
                itemid = 1354023;
                change = (this.level >= 100);
            }
            if (this.level == 10 && itemid != 0) {
                if (GameConstants.isAngelicBuster(this.job)) {
                    itemid = 1352600;
                } else if (GameConstants.isDemonAvenger(this.job) || GameConstants.isDemonSlayer(this.job)) {
                    itemid = 1099000;
                } else {
                    itemid -= 3;
                }
                change = true;
            } else if (this.level == 30 && itemid != 0) {
                if (GameConstants.isAngelicBuster(this.job)) {
                    itemid = 1352602;
                } else if (GameConstants.isDemonAvenger(this.job) || GameConstants.isDemonSlayer(this.job)) {
                    itemid = 1099002;
                } else {
                    itemid -= 3;
                    ++itemid;
                }
                change = true;
            } else if (this.level == 60 && itemid != 0) {
                if (GameConstants.isAngelicBuster(this.job)) {
                    itemid = 1352603;
                } else if (GameConstants.isDemonAvenger(this.job) || GameConstants.isDemonSlayer(this.job)) {
                    itemid = 1099003;
                } else {
                    itemid -= 3;
                    itemid += 2;
                }
                change = true;
            }
        }
        if (change) {
            final MapleInventory equip = this.getInventory(MapleInventoryType.EQUIPPED);
            final Item ii = MapleItemInformationProvider.getInstance().getEquipById(itemid);
            ii.setPosition((short) (-10));
            final Equip equiped = (Equip) this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
            if (equiped != null) {
                this.getInventory(MapleInventoryType.EQUIPPED).removeSlot((short) (-10));
            }
            equip.addFromDB(ii);
            this.client.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIPPED, ii));
        }
        if (GameConstants.isDemonSlayer(this.job)) {
            int force = 30;
            switch (this.job) {
                case 3110: {
                    force = 50;
                    break;
                }
                case 3111: {
                    force = 100;
                    break;
                }
                case 3112: {
                    force = 120;
                    break;
                }
            }
            maxmp = force;
        } else if (GameConstants.isZero(this.job)) {
            maxmp = 100L;
        }
        final Map<MapleStat, Long> statup = new EnumMap<MapleStat, Long>(MapleStat.class);
        this.stats.setInfo(maxhp, maxmp, this.getStat().getCurrentMaxHp(), this.getStat().getCurrentMaxMp(this));
        statup.put(MapleStat.MAXHP, maxhp);
        statup.put(MapleStat.MAXMP, maxmp);
        statup.put(MapleStat.HP, this.getStat().getHp());
        statup.put(MapleStat.MP, this.getStat().getMp());
        statup.put(MapleStat.EXP, this.exp);
        statup.put(MapleStat.LEVEL, (long) this.level);
        switch (this.level) { // 레벨업보상
            case 200: // 200레벨 달성시 아래아이템 지급(보관함으로 지급되며 계정당 1회지급됨)
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(500000000L, true, false);                 
                    gainCabinetItem(2048717, 30);
                    gainCabinetItem(5062009, 100);
                    gainCabinetItem(5062500, 100);
                    gainCabinetItem(2633202, 1);
                    gainCabinetItem(2630437, 30);
                    gainCabinetItem(1712001, 1);
                    gainCabinetItem(1712002, 1);
                    gainCabinetItem(1712003, 1);
                    gainCabinetItem(1712004, 1);
                    gainCabinetItem(1712005, 1);
                    gainCabinetItem(1712006, 1);
                    gainCabinetItem(2434006, 2);
                    gainCabinetItem(5060048, 5);
                    gainCabinetItem(2435719, 20);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 210: // 210레벨
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(300000000L, true, false);
                    gainCabinetItem(5068300, 3);
                    gainCabinetItem(5069100, 2);
                    gainCabinetItem(5062006, 5);
                    gainCabinetItem(4001716, 1);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 220:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(400000000L, true, false);
                    gainCabinetItem(5068300, 4);
                    gainCabinetItem(5069100, 2);
                    gainCabinetItem(5062006, 8);
                    gainCabinetItem(4001716, 2);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 230:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(500000000L, true, false);
                    gainCabinetItem(5068300, 6);
                    gainCabinetItem(5069100, 2);
                    gainCabinetItem(5062006, 10);
                    gainCabinetItem(4001716, 3);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 240:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(1000000000L, true, false);
                    gainCabinetItem(5068300, 10);
                    gainCabinetItem(5069100, 2);
                    gainCabinetItem(5062006, 12);
                    gainCabinetItem(4001716, 4);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 250:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(1500000000L, true, false);
                    gainCabinetItem(2049361, 5);
                    gainCabinetItem(5060048, 3);
                    gainCabinetItem(5062006, 15);
                    gainCabinetItem(2049361, 5);
                    gainCabinetItem(4001716, 5);
                    gainCabinetItem(2049704, 10);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 260:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(2000000000L, true, false);
                    gainCabinetItem(5060048, 8);
                    gainCabinetItem(5062006, 5);
                    gainCabinetItem(1713000, 10);
                    gainCabinetItem(1713001, 10);
                    gainCabinetItem(2049704, 10);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 270:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(3000000000L, true, false);
                    gainCabinetItem(5060048, 8);
                    gainCabinetItem(5062006, 10);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 280:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(4000000000L, true, false);
                    gainCabinetItem(5060048, 10);
                    gainCabinetItem(5062006, 30);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 290:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(5000000000L, true, false);
                    gainCabinetItem(5060048, 25);
                    gainCabinetItem(5062006, 50);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
            case 300:
                if (this.client.getKeyValue("level" + this.level) == null) {
                    gainMeso(10000000000L, true, false);
                    gainCabinetItem(5060048, 50);
                    gainCabinetItem(5062006, 100);
                    gainCabinetItem(2049361, 8);
                    this.client.setKeyValue("level" + this.level, "true");
                }
                break;
        }
        if (this.level <= 10) {
            final PlayerStats stats = this.stats;
            stats.str += this.remainingAp;
            this.remainingAp = 0;
            statup.put(MapleStat.STR, (long) this.stats.getStr());
        }
        statup.put(MapleStat.AVAILABLEAP, (long) this.remainingAp);
        statup.put(MapleStat.AVAILABLESP, (long) this.remainingSp[GameConstants.getSkillBook(this.job, this.level)]);
        this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(statup, this));
        this.map.broadcastMessage(this, CField.EffectPacket.showNormalEffect(this, 0, false), false);
        this.silentPartyUpdate();
        this.guildUpdate();
        this.getStat().recalcLocalStats(this);
        this.getStat().heal(this);
    }

    public boolean existPremium() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean ret = false;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM premium WHERE accid = ?");
            ps.setInt(1, this.getAccountID());
            rs = ps.executeQuery();
            ret = rs.next();
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ex2) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex3) {
                }
            }
        }
        return ret;
    }

    public long getRemainPremium() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        long ret = 0L;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM premium WHERE accid = ?");
            ps.setInt(1, this.getAccountID());
            rs = ps.executeQuery();
            if (rs.next()) {
                ret = rs.getLong("period");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ex2) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex3) {
                }
            }
        }
        return ret;
    }

    public void forMatrix() {//mush
        MatrixHandler.gainMatrix(this);
    }

    public void loadPremium() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM premium WHERE accid = ?");
            ps.setInt(1, this.getAccountID());
            rs = ps.executeQuery();
            if (rs.next()) {
                this.premium = rs.getString("name");
                this.premiumbuff = rs.getInt("buff");
                this.premiumPeriod = rs.getLong("period");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ex2) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex3) {
                }
            }
        }
    }

    public void gainPremium(final int v3) {
        final Date adate = new Date();
        final Date bdate = new Date();
        if (this.premiumPeriod != 0L) {
            bdate.setTime(this.premiumPeriod + v3 * 24 * 60 * 60 * 1000);
            if (adate.getTime() > bdate.getTime()) {
                this.premiumPeriod = adate.getTime() + v3 * 24 * 60 * 60 * 1000;
                this.premium = "\uc77c\ubc18";
                this.premiumbuff = 80001535;
            } else {
                this.premiumPeriod = bdate.getTime() + v3 * 24 * 60 * 60 * 1000;
            }
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            if (this.existPremium()) {
                if (this.getRemainPremium() > adate.getTime()) {
                    con = DatabaseConnection.getConnection();
                    ps = con.prepareStatement("UPDATE premium SET period = ? WHERE accid = ?");
                    ps.setLong(1, this.getRemainPremium() + v3 * 24 * 60 * 60 * 1000);
                    ps.setInt(2, this.getAccountID());
                    ps.executeUpdate();
                    ps.close();
                    con.close();
                } else {
                    con = DatabaseConnection.getConnection();
                    ps = con.prepareStatement("UPDATE premium SET period = ? and `name` = ? and `buff` = ? WHERE accid = ?");
                    ps.setLong(1, adate.getTime() + v3 * 24 * 60 * 60 * 1000);
                    ps.setString(2, "일반");
                    ps.setInt(3, 80001535);
                    ps.setInt(4, this.getAccountID());
                    ps.executeUpdate();
                    ps.close();
                    con.close();
                }
            } else {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("INSERT INTO premium(accid, name, buff, period) VALUES (?, ?, ?, ?)");
                ps.setInt(1, this.getAccountID());
                ps.setString(2, "일반");
                ps.setInt(3, 80001535);
                ps.setLong(4, v3);
                ps.executeUpdate();
                ps.close();
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ex) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex2) {
                }
            }
        }
    }

    public void setPremium(final String v1, final int v2, final long v3) {
        if (SkillFactory.getSkill(this.premiumbuff) != null) {
            this.changeSingleSkillLevel(SkillFactory.getSkill(this.premiumbuff), 0, (byte) 0);
        }
        this.premium = v1;
        this.premiumbuff = v2;
        this.premiumPeriod = v3;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            if (this.existPremium()) {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("UPDATE premium SET name = ?, buff = ?, period = ? WHERE accid = ?");
                ps.setString(1, v1);
                ps.setInt(2, v2);
                ps.setLong(3, v3);
                ps.setInt(4, this.getAccountID());
                ps.executeUpdate();
                ps.close();
                con.close();
            } else {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("INSERT INTO premium(accid, name, buff, period) VALUES (?, ?, ?, ?)");
                ps.setInt(1, this.getAccountID());
                ps.setString(2, this.premium);
                ps.setInt(3, this.premiumbuff);
                ps.setLong(4, this.premiumPeriod);
                ps.executeUpdate();
                ps.close();
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ex) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex2) {
                }
            }
        }
    }

    public String getPremium() {
        return this.premium;
    }

    public int getPremiumBuff() {
        return this.premiumbuff;
    }

    public Long getPremiumPeriod() {
        return this.premiumPeriod;
    }

    public void autoJob() {
        if (GameConstants.isZero(this.getJob())) {
            this.dropMessage(-1, "[알림] 시간의 초월자 '제로'로 전직하였습니다.");
            if (this.getLevel() >= 160 && this.getJob() == 10111) {
                this.changeJob(10112);
            } else if (this.getLevel() >= 140 && this.getJob() == 10110) {
                this.changeJob(10111);
            } else if (this.getLevel() >= 120 && this.getJob() == 10100) {
                this.changeJob(10110);
            }
        }
        if (this.getAutoJob() != null) {
            if (this.level == 20) {
                final String autoJob = this.getAutoJob();
                switch (autoJob) {
                    case "430": {
                        this.dropMessage(-1, "[암흑을 기억하는자] 세미듀어러로 전직하였습니다.");
                        this.changeJob(430);
                        break;
                    }
                    case "2210": {
                        this.changeJob(2210);
                        break;
                    }
                }
            } else if (this.level == 30) {
                final String autoJob2 = this.getAutoJob();
                switch (autoJob2) {
                    case "110": {
                        this.dropMessage(-1, "[양손검술의 기사] 파이터로 전직하였습니다.");
                        this.changeJob(110);
                        break;
                    }
                    case "120": {
                        this.dropMessage(-1, "[한손검술의 기사] 페이지로 전직하였습니다.");
                        this.changeJob(120);
                        break;
                    }
                    case "130": {
                        this.dropMessage(-1, "[창술의 기사] 스피어맨로 전직하였습니다.");
                        this.changeJob(130);
                        break;
                    }
                    case "210": {
                        this.dropMessage(-1, "[불*독] 위자드로 전직하였습니다.");
                        this.changeJob(210);
                        break;
                    }
                    case "220": {
                        this.dropMessage(-1, "[얼음*번개] 위자드로 전직하였습니다.");
                        this.changeJob(220);
                        break;
                    }
                    case "230": {
                        this.dropMessage(-1, "[힐*버프] 클레릭으로 전직하였습니다.");
                        this.changeJob(230);
                        break;
                    }
                    case "310": {
                        this.dropMessage(-1, "[사격수] 헌터로 전직하였습니다.");
                        this.changeJob(310);
                        break;
                    }
                    case "320": {
                        this.dropMessage(-1, "[명사수] 사수로 전직하였습니다.");
                        this.changeJob(320);
                        break;
                    }
                    case "330": {
                        this.dropMessage(-1, "[저주와 고대의 힘] 패스파인더로 전직하였습니다.");
                        this.changeJob(330);
                        break;
                    }
                    case "410": {
                        this.dropMessage(-1, "[표창 암살 입문기] 어쌔신로 전직하였습니다.");
                        this.changeJob(410);
                        break;
                    }
                    case "420": {
                        this.dropMessage(-1, "[단도 암살 입문기] 시프로 전직하였습니다.");
                        this.changeJob(420);
                        break;
                    }
                    case "510": {
                        this.dropMessage(-1, "[너클 입문기] 인파이터로 전직하였습니다.");
                        this.changeJob(510);
                        break;
                    }
                    case "520": {
                        this.dropMessage(-1, "[건 입문기] 건슬링거로 전직하였습니다.");
                        this.changeJob(520);
                        break;
                    }
                    case "430": {
                        this.dropMessage(-1, "[암흑 속의 과거] 듀어러로 전직하였습니다.");
                        this.changeJob(431);
                        break;
                    }
                    case "530": {
                        this.dropMessage(-1, "[캐논 입문기] 캐논슈터로 전직하였습니다.");
                        this.changeJob(530);
                        break;
                    }
                    case "1110": {
                        this.dropMessage(-1, "[시그너스 입문기] 빛의 기사로 전직하였습니다.");
                        this.changeJob(1110);
                        break;
                    }
                    case "1210": {
                        this.dropMessage(-1, "[시그너스 입문기] 불의 기사로 전직하였습니다.");
                        this.changeJob(1210);
                        break;
                    }
                    case "1310": {
                        this.dropMessage(-1, "[시그너스 입문기] 바람의 기사로 전직하였습니다.");
                        this.changeJob(1310);
                        break;
                    }
                    case "1410": {
                        this.dropMessage(-1, "[시그너스 입문기] 어둠의 기사로 전직하였습니다.");
                        this.changeJob(1410);
                        break;
                    }
                    case "1510": {
                        this.dropMessage(-1, "[시그너스 입문기] 번개의 기사로 전직하였습니다.");
                        this.changeJob(1510);
                        break;
                    }
                    case "2110": {
                        this.dropMessage(-1, "[영웅의 본능] 아란으로 전직하였습니다.");
                        this.changeJob(2110);
                        break;
                    }
                    case "2210": {
                        this.dropMessage(-1, "[세번째 걸음] 에반으로 전직하였습니다.");
                        this.changeJob(2211);
                        break;
                    }
                    case "2310": {
                        this.dropMessage(-1, "[영웅의 본능] 메르세데스로 전직하였습니다.");
                        this.changeJob(2310);
                        break;
                    }
                    case "2410": {
                        this.dropMessage(-1, "[영웅의 본능] 팬텀으로 전직하였습니다.");
                        this.changeJob(2410);
                        break;
                    }
                    case "2510": {
                        this.dropMessage(-1, "[영웅의 본능] 은월으로 전직하였습니다.");
                        this.changeJob(2510);
                        break;
                    }
                    case "2710": {
                        this.dropMessage(-1, "[영웅의 본능] 루미너스로 전직하였습니다.");
                        this.changeJob(2710);
                        break;
                    }
                    case "3110": {
                        this.dropMessage(-1, "[레지스탕스 입문기] 데몬슬레이어로 전직하였습니다.");
                        this.changeJob(3110);
                        break;
                    }
                    case "3120": {
                        this.dropMessage(-1, "[레지스탕스 입문기] 데몬어벤져로 전직하였습니다.");
                        this.changeJob(3120);
                        break;
                    }
                    case "3210": {
                        this.dropMessage(-1, "[레지스탕스 입문기] 배틀메이지로 전직하였습니다.");
                        this.changeJob(3210);
                        break;
                    }
                    case "3310": {
                        this.dropMessage(-1, "[레지스탕스 입문기] 와일드헌터로 전직하였습니다.");
                        this.changeJob(3310);
                        break;
                    }
                    case "3510": {
                        this.dropMessage(-1, "[레지스탕스 입문기] 메카닉으로 전직하였습니다.");
                        this.changeJob(3510);
                        break;
                    }
                    case "3610": {
                        this.dropMessage(-1, "[레지스탕스 입문기] 제논으로 전직하였습니다.");
                        this.changeJob(3610);
                        break;
                    }
                    case "3710": {
                        this.dropMessage(-1, "[레지스탕스 입문기] 블래스터로 전직하였습니다.");
                        this.changeJob(3710);
                        break;
                    }
                    case "5110": {
                        this.dropMessage(-1, "[시그너스 단장] 빛의 기사로 전직하였습니다.");
                        this.changeJob(5110);
                        break;
                    }
                    case "6110": {
                        this.dropMessage(-1, "[노바 수련생] 카이저로 전직하였습니다.");
                        this.changeJob(6110);
                        break;
                    }
                    case "6510": {
                        this.dropMessage(-1, "[노바 수련생] 엔젤릭버스터로 전직하였습니다.");
                        this.changeJob(6510);
                        break;
                    }
                    case "14210": {
                        this.dropMessage(-1, "[염동력 황제] 키네시스로 전직하였습니다.");
                        this.changeJob(14210);
                        break;
                    }
                    case "6310": {
                        this.dropMessage(-1, "[밤의 추적자] 카인으로 전직하였습니다.");
                        this.changeJob(6310);
                        break;
                    }
                    case "6410": {
                        this.dropMessage(-1, "[노바의 귀재] 카데나로 전직하였습니다.");
                        this.changeJob(6410);
                        break;
                    }
                    case "15110": {
                        this.dropMessage(-1, "[검의 지휘자] 아델로 전직하였습니다.");
                        this.changeJob(15110);
                        break;
                    }
                    case "15210": {
                        this.dropMessage(-1, "[고대의 크리스탈] 일리움으로 전직하였습니다.");
                        this.changeJob(15210);
                        break;
                    }
                    case "15510": {
                        this.dropMessage(-1, "[심연의 분노] 아크로 전직하였습니다.");
                        this.changeJob(15510);
                        break;
                    }
                    case "16210": {
                        this.dropMessage(-1, "[낭만 풍수사] 라라로 전직하였습니다.");
                        this.changeJob(16210);
                        break;
                    }
                    case "16410": {
                        this.dropMessage(-1, "[천방지축 도사] 호영으로 전직하였습니다.");
                        this.changeJob(16410);
                        break;
                    }
                }
            } else if (this.level == 40) {
                final String autoJob3 = this.getAutoJob();
                switch (autoJob3) {
                    case "2210": {
                        this.changeJob(2212);
                        break;
                    }
                }
            } else if (this.level == 50) {
                final String autoJob4 = this.getAutoJob();
                switch (autoJob4) {
                    case "2210": {
                        this.changeJob(2213);
                        break;
                    }
                }
            } else if (this.level == 55) {
                final String autoJob5 = this.getAutoJob();
                switch (autoJob5) {
                    case "430": {
                        this.changeJob(432);
                        break;
                    }
                }
            } else if (this.level == 60) {
                final String autoJob6 = this.getAutoJob();
                switch (autoJob6) {
                    case "110": {
                        this.dropMessage(-1, "[영혼 검술의 기사] 크루세이더로 전직하였습니다.");
                        this.changeJob(111);
                        break;
                    }
                    case "120": {
                        this.dropMessage(-1, "[속성 검술의 기사] 나이트로 전직하였습니다.");
                        this.changeJob(121);
                        break;
                    }
                    case "130": {
                        this.dropMessage(-1, "[드래곤 창술의 기사] 드래곤 나이트로 전직하였습니다.");
                        this.changeJob(131);
                        break;
                    }
                    case "210": {
                        this.dropMessage(-1, "[불*독] 메이지로 전직하였습니다.");
                        this.changeJob(211);
                        break;
                    }
                    case "220": {
                        this.dropMessage(-1, "[얼음*번개] 메이지로 전직하였습니다.");
                        this.changeJob(221);
                        break;
                    }
                    case "230": {
                        this.dropMessage(-1, "[힐*버프] 프리스트로 전직하였습니다.");
                        this.changeJob(231);
                        break;
                    }
                    case "310": {
                        this.dropMessage(-1, "[연쇄 사격수] 레인저로 전직하였습니다.");
                        this.changeJob(311);
                        break;
                    }
                    case "320": {
                        this.dropMessage(-1, "[백발백중 명사수] 저격수로 전직하였습니다.");
                        this.changeJob(321);
                        break;
                    }
                    case "330": {
                        this.dropMessage(-1, "[체이서의 길] 패스파인더로 전직하였습니다.");
                        this.changeJob(331);
                        break;
                    }
                    case "410": {
                        this.dropMessage(-1, "[암살 전문가] 허밋로 전직하였습니다.");
                        this.changeJob(411);
                        break;
                    }
                    case "420": {
                        this.dropMessage(-1, "[암흑자] 시프 마스터로 전직하였습니다.");
                        this.changeJob(421);
                        break;
                    }
                    case "510": {
                        this.dropMessage(-1, "[드래곤 너클 파이터] 버커니어로 전직하였습니다.");
                        this.changeJob(511);
                        break;
                    }
                    case "520": {
                        this.dropMessage(-1, "[건 마스터리] 발키리로 전직하였습니다.");
                        this.changeJob(521);
                        break;
                    }
                    case "430": {
                        this.dropMessage(-1, "[암흑을 알아버린자] 슬래셔로 전직하였습니다.");
                        this.changeJob(433);
                        break;
                    }
                    case "530": {
                        this.dropMessage(-1, "[캐논 마스터리] 캐논슈터로 전직하였습니다.");
                        this.changeJob(531);
                        break;
                    }
                    case "2110": {
                        this.dropMessage(-1, "[영웅의 깨달음] 아란으로 전직하였습니다.");
                        this.changeJob(2111);
                        break;
                    }
                    case "2210": {
                        this.dropMessage(-1, "[진화의 드래곤] 에반으로 전직하였습니다.");
                        this.changeJob(2214);
                        break;
                    }
                    case "2310": {
                        this.dropMessage(-1, "[영웅의 깨달음] 메르세데스로 전직하였습니다.");
                        this.changeJob(2311);
                        break;
                    }
                    case "2410": {
                        this.dropMessage(-1, "[영웅의 깨달음] 팬텀으로 전직하였습니다.");
                        this.changeJob(2411);
                        break;
                    }
                    case "2510": {
                        this.dropMessage(-1, "[영웅의 깨달음] 은월으로 전직하였습니다.");
                        this.changeJob(2511);
                        break;
                    }
                    case "2710": {
                        this.dropMessage(-1, "[영웅의 깨달음] 루미너스로 전직하였습니다.");
                        this.changeJob(2711);
                        break;
                    }
                    case "3110": {
                        this.dropMessage(-1, "[부활한 마족] 데몬슬레이어로 전직하였습니다.");
                        this.changeJob(3111);
                        break;
                    }
                    case "3120": {
                        this.dropMessage(-1, "[분노의 화신] 데몬어벤져로 전직하였습니다.");
                        this.changeJob(3121);
                        break;
                    }
                    case "3210": {
                        this.dropMessage(-1, "[레지스탕스 요원] 배틀메이지로 전직하였습니다.");
                        this.changeJob(3211);
                        break;
                    }
                    case "3310": {
                        this.dropMessage(-1, "[레지스탕스 요원] 와일드헌터로 전직하였습니다.");
                        this.changeJob(3311);
                        break;
                    }
                    case "3510": {
                        this.dropMessage(-1, "[레지스탕스 요원] 메카닉으로 전직하였습니다.");
                        this.changeJob(3511);
                        break;
                    }
                    case "3610": {
                        this.dropMessage(-1, "[레지스탕스 요원] 제논으로 전직하였습니다.");
                        this.changeJob(3611);
                        break;
                    }
                    case "3710": {
                        this.dropMessage(-1, "[레지스탕스 요원] 블래스터로 전직하였습니다.");
                        this.changeJob(3711);
                        break;
                    }
                    case "5110": {
                        this.dropMessage(-1, "[시그너스 단장] 빛의 기사로 전직하였습니다.");
                        this.changeJob(5111);
                        break;
                    }
                    case "6110": {
                        this.dropMessage(-1, "[노바의 수호자] 카이저로 전직하였습니다.");
                        this.changeJob(6111);
                        break;
                    }
                    case "6310": {
                        this.dropMessage(-1, "[밤의 추적자] 카인으로 전직하였습니다.");
                        this.changeJob(6311);
                        break;
                    }
                    case "6510": {
                        this.dropMessage(-1, "[노바의 수호자] 엔젤릭버스터로 전직하였습니다.");
                        this.changeJob(6511);
                        break;
                    }
                    case "1110": {
                        this.dropMessage(-1, "[시그너스 정식 기사] 소울 마스터로 전직하였습니다.");
                        this.changeJob(1111);
                        break;
                    }
                    case "1210": {
                        this.dropMessage(-1, "[시그너스 정식 기사] 플레임 위자드로 전직하였습니다.");
                        this.changeJob(1211);
                        break;
                    }
                    case "1310": {
                        this.dropMessage(-1, "[시그너스 정식 기사] 윈드 브레이커로 전직하였습니다.");
                        this.changeJob(1311);
                        break;
                    }
                    case "1410": {
                        this.dropMessage(-1, "[시그너스 정식 기사] 나이트 워커로 전직하였습니다.");
                        this.changeJob(1411);
                        break;
                    }
                    case "1510": {
                        this.dropMessage(-1, "[시그너스 정식 기사] 스트라이커로 전직하였습니다.");
                        this.changeJob(1511);
                        break;
                    }
                    case "14210": {
                        this.dropMessage(-1, "[염동력의 황제] 키네시스로 전직하였습니다.");
                        this.changeJob(14211);
                        break;
                    }
                    case "6410": {
                        this.dropMessage(-1, "[노바의 귀재] 카데나로 전직하였습니다.");
                        this.changeJob(6411);
                        break;
                    }
                    case "15110": {
                        this.dropMessage(-1, "[검의 지휘자] 아델로 전직하였습니다.");
                        this.changeJob(15111);
                        break;
                    }
                    case "15210": {
                        this.dropMessage(-1, "[고대의 크리스탈] 일리움으로 전직하였습니다.");
                        this.changeJob(15211);
                        break;
                    }
                    case "15510": {
                        this.dropMessage(-1, "[심연의 분노] 아크로 전직하였습니다.");
                        this.changeJob(15511);
                        break;
                    }
                    case "16210": {
                        this.dropMessage(-1, "[낭만 풍수사] 라라로 전직하였습니다.");
                        this.changeJob(16211);
                        break;
                    }
                    case "16410": {
                        this.dropMessage(-1, "[천방지축 도사] 호영으로 전직하였습니다.");
                        this.changeJob(16411);
                        break;
                    }
                }
            } else if (this.level == 80) {
                final String autoJob7 = this.getAutoJob();
                switch (autoJob7) {
                    case "2210": {
                        this.changeJob(2215);
                        break;
                    }
                }
            } else if (this.level == 100) {
                final String autoJob8 = this.getAutoJob();
                switch (autoJob8) {
                    case "110": {
                        this.dropMessage(-1, "[연쇄 검술의 마스터] 히어로로 전직하였습니다.");
                        this.changeJob(112);
                        break;
                    }
                    case "120": {
                        this.dropMessage(-1, "[환상 검술의 마스터] 팔라딘로 전직하였습니다.");
                        this.changeJob(122);
                        break;
                    }
                    case "130": {
                        this.dropMessage(-1, "[다크 드래곤 창술의 마스터] 다크 나이트로 전직하였습니다.");
                        this.changeJob(132);
                        break;
                    }
                    case "210": {
                        this.dropMessage(-1, "[불*독 마스터] 아크메이지로 전직하였습니다.");
                        this.changeJob(212);
                        break;
                    }
                    case "220": {
                        this.dropMessage(-1, "[얼음*번개 마스터] 아크메이지로 전직하였습니다.");
                        this.changeJob(222);
                        break;
                    }
                    case "230": {
                        this.dropMessage(-1, "[힐*버프 마스터] 비숍으로 전직하였습니다.");
                        this.changeJob(232);
                        break;
                    }
                    case "310": {
                        this.dropMessage(-1, "[화살 연사의 마스터] 보우 마스터로 전직하였습니다.");
                        this.changeJob(312);
                        break;
                    }
                    case "320": {
                        this.dropMessage(-1, "[화살 파워의 마스터] 신궁로 전직하였습니다.");
                        this.changeJob(322);
                        break;
                    }
                    case "330": {
                        this.dropMessage(-1, "[에인션트 보우의 달인] 패스파인더로 전직하였습니다.");
                        this.changeJob(332);
                        break;
                    }
                    case "410": {
                        this.dropMessage(-1, "[연쇄 암살의 마스터] 나이트 로드로 전직하였습니다.");
                        this.changeJob(412);
                        break;
                    }
                    case "420": {
                        this.dropMessage(-1, "[암흑의 암살 마스터] 섀도우로 전직하였습니다.");
                        this.changeJob(422);
                        break;
                    }
                    case "510": {
                        this.dropMessage(-1, "[정령의 너클 파이터] 바이퍼로 전직하였습니다.");
                        this.changeJob(512);
                        break;
                    }
                    case "520": {
                        this.dropMessage(-1, "[배틀 건 마스터리] 캡틴으로 전직하였습니다.");
                        this.changeJob(522);
                        break;
                    }
                    case "430": {
                        this.dropMessage(-1, "[암흑을 조정하는자] 듀얼블레이드로 전직하였습니다.");
                        this.changeJob(434);
                        break;
                    }
                    case "530": {
                        this.dropMessage(-1, "[파괴의 캐논 마스터리] 캐논슈터로 전직하였습니다.");
                        this.changeJob(532);
                        break;
                    }
                    case "2110": {
                        this.dropMessage(-1, "[영웅의 부활] 아란으로 전직하였습니다.");
                        this.changeJob(2112);
                        break;
                    }
                    case "2210": {
                        this.dropMessage(-1, "[강인한 드래곤] 에반으로 전직하였습니다.");
                        this.changeJob(2217);
                        break;
                    }
                    case "2310": {
                        this.dropMessage(-1, "[영웅의 부활] 메르세데스로 전직하였습니다.");
                        this.changeJob(2312);
                        break;
                    }
                    case "2410": {
                        this.dropMessage(-1, "[영웅의 부활] 팬텀으로 전직하였습니다.");
                        this.changeJob(2412);
                        this.changeSkillLevel(20031210, (byte) 1, (byte) 1);
                        this.client.getSession().writeAndFlush((Object) CField.updateCardStack(false, this.cardStack));
                        break;
                    }
                    case "2510": {
                        this.dropMessage(-1, "[영웅의 부활] 은월으로 전직하였습니다.");
                        this.changeJob(2512);
                        break;
                    }
                    case "2710": {
                        this.dropMessage(-1, "[영웅의 부활] 루미너스로 전직하였습니다.");
                        this.changeJob(2712);
                        break;
                    }
                    case "3110": {
                        this.dropMessage(-1, "[레지스탕스의 영웅] 데몬슬레이어로 전직하였습니다.");
                        this.changeJob(3112);
                        break;
                    }
                    case "3120": {
                        this.dropMessage(-1, "[레지스탕스 영웅] 데몬어벤져로 전직하였습니다.");
                        this.changeJob(3122);
                        break;
                    }
                    case "3210": {
                        this.dropMessage(-1, "[레지스탕스의 영웅] 배틀메이지로 전직하였습니다.");
                        this.changeJob(3212);
                        break;
                    }
                    case "3310": {
                        this.dropMessage(-1, "[레지스탕스의 영웅] 와일드헌터로 전직하였습니다.");
                        this.changeJob(3312);
                        break;
                    }
                    case "3510": {
                        this.dropMessage(-1, "[레지스탕스의 영웅] 메카닉으로 전직하였습니다.");
                        this.changeJob(3512);
                        break;
                    }
                    case "3610": {
                        this.dropMessage(-1, "[레지스탕스의 영웅] 제논으로 전직하였습니다.");
                        this.changeJob(3612);
                        break;
                    }
                    case "3710": {
                        this.dropMessage(-1, "[레지스탕스의 영웅] 블래스터로 전직하였습니다.");
                        this.changeJob(3712);
                        break;
                    }
                    case "5110": {
                        this.dropMessage(-1, "[시그너스 단장] 빛의 기사로 전직하였습니다.");
                        this.changeJob(5112);
                        break;
                    }
                    case "6110": {
                        this.dropMessage(-1, "[용의 기사] 카이저로 전직하였습니다.");
                        this.changeJob(6112);
                        break;
                    }
                    case "6310": {
                        this.dropMessage(-1, "[밤의 추적자] 카인으로 전직하였습니다.");
                        this.changeJob(6312);
                        break;
                    }
                    case "6510": {
                        this.dropMessage(-1, "[전장의 아이돌] 엔젤릭버스터로 전직하였습니다.");
                        this.changeJob(6512);
                        break;
                    }
                    case "1110": {
                        this.dropMessage(-1, "[시그너스 영웅] 빛의 대정령으로 전직하였습니다.");
                        this.changeJob(1112);
                        break;
                    }
                    case "1210": {
                        this.dropMessage(-1, "[시그너스 영웅] 불의 대정령으로 전직하였습니다.");
                        this.changeJob(1212);
                        break;
                    }
                    case "1310": {
                        this.dropMessage(-1, "[시그너스 영웅] 바람의 대정령으로 전직하였습니다.");
                        this.changeJob(1312);
                        break;
                    }
                    case "1410": {
                        this.dropMessage(-1, "[시그너스 영웅] 어둠의 대정령으로 전직하였습니다.");
                        this.changeJob(1412);
                        break;
                    }
                    case "1510": {
                        this.dropMessage(-1, "[시그너스 영웅] 번개의 대정령으로 전직하였습니다.");
                        this.changeJob(1512);
                        break;
                    }
                    case "14210": {
                        this.dropMessage(-1, "[염동력의 황제] 키네시스로 전직하였습니다.");
                        this.changeJob(14212);
                        break;
                    }
                    case "6410": {
                        this.dropMessage(-1, "[노바의 귀재] 카데나로 전직하였습니다.");
                        this.changeJob(6412);
                        break;
                    }
                    case "15110": {
                        this.dropMessage(-1, "[검의 지휘자] 아델로 전직하였습니다.");
                        this.changeJob(15112);
                        break;
                    }
                    case "15210": {
                        this.dropMessage(-1, "[고대의 크리스탈] 일리움으로 전직하였습니다.");
                        this.changeJob(15212);
                        break;
                    }
                    case "15510": {
                        this.dropMessage(-1, "[심연의 분노] 아크로 전직하였습니다.");
                        this.changeJob(15512);
                        break;
                    }
                    case "16210": {
                        this.dropMessage(-1, "[낭만 풍수사] 라라로 전직하였습니다.");
                        this.changeJob(16212);
                        break;
                    }
                    case "16410": {
                        this.dropMessage(-1, "[천방지축 도사] 호영으로 전직하였습니다.");
                        this.changeJob(16412);
                        break;
                    }
                }
            }
        }
    }

    public void setAutoJob(final int jobid) {
        this.getQuestNAdd(MapleQuest.getInstance(111113)).setCustomData(String.valueOf(jobid));
    }

    public String getAutoJob() {
        if (this.getQuestNoAdd(MapleQuest.getInstance(111113)) == null) {
            return null;
        }
        return this.getQuestNoAdd(MapleQuest.getInstance(111113)).getCustomData();
    }

    public void changeKeybinding(final int key, final byte type, final int action) {
        if (type != 0) {
            this.keylayout.Layout().put(key, new Pair<Byte, Integer>(type, action));
        } else {
            this.keylayout.Layout().remove(key);
        }
    }

    public void sendMacros() {
        this.client.getSession().writeAndFlush((Object) CField.getMacros(this.skillMacros));
    }

    public void updateMacros(final int position, final SkillMacro updateMacro) {
        this.skillMacros[position] = updateMacro;
        this.changed_skillmacros = true;
    }

    public final SkillMacro[] getMacros() {
        return this.skillMacros;
    }

    public final List<AvatarLook> getCoodination() {
        return this.coodination;
    }

    public void setMarriage(final MarriageMiniBox mgs) {
        this.mg = mgs;
    }

    public MarriageMiniBox getMarriage() {
        return this.mg;
    }

    public void tempban(final String reason, final Calendar duration, final int greason, final boolean IPMac) {
        if (IPMac) {
            this.client.banMacs();
        }
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            if (IPMac) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, this.client.getSession().remoteAddress().toString().split(":")[0]);
                ps.execute();
                ps.close();
            }
            this.client.getSession().close();
            ps = con.prepareStatement("UPDATE accounts SET tempban = ?, banreason = ?, greason = ? WHERE id = ?");
            final Timestamp TS = new Timestamp(duration.getTimeInMillis());
            ps.setTimestamp(1, TS);
            ps.setString(2, reason);
            ps.setInt(3, greason);
            ps.setInt(4, this.accountid);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error while tempbanning" + ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex2) {
            }
        }
    }

    public final boolean ban(final String reason, final boolean IPMac, final boolean autoban, final boolean hellban) {
        if (this.lastmonthfameids == null) {
            throw new RuntimeException("Trying to ban a non-loaded character (testhack)");
        }
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE id = ?");
            ps.setInt(1, autoban ? 2 : 1);
            ps.setString(2, reason);
            ps.setInt(3, this.accountid);
            ps.execute();
            ps.close();
            if (IPMac) {
                this.client.banMacs();
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, this.client.getSessionIPAddress());
                ps.execute();
                ps.close();
                if (hellban) {
                    final PreparedStatement psa = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
                    psa.setInt(1, this.accountid);
                    final ResultSet rsa = psa.executeQuery();
                    if (rsa.next()) {
                        final PreparedStatement pss = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE email = ? OR SessionIP = ?");
                        pss.setInt(1, autoban ? 2 : 1);
                        pss.setString(2, reason);
                        pss.setString(3, rsa.getString("email"));
                        pss.setString(4, this.client.getSessionIPAddress());
                        pss.execute();
                        pss.close();
                    }
                    rsa.close();
                    psa.close();
                }
            }
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error while banning" + ex);
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex2) {
            }
        }
        this.client.getSession().close();
        return true;
    }

    public static boolean ban(final String id, final String reason, final boolean accountId, final int gmlevel, final boolean hellban) {
        final Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (id.matches("/[0-9]{1,3}\\..*")) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, id);
                ps.execute();
                ps.close();
                return true;
            }
            if (accountId) {
                ps = con.prepareStatement("SELECT id FROM accounts WHERE name = ?");
            } else {
                ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            }
            boolean ret = false;
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                final int z = rs.getInt(1);
                final PreparedStatement psb = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ? AND gm < ?");
                psb.setString(1, reason);
                psb.setInt(2, z);
                psb.setInt(3, gmlevel);
                psb.execute();
                psb.close();
                if (gmlevel > 100) {
                    final PreparedStatement psa = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
                    psa.setInt(1, z);
                    final ResultSet rsa = psa.executeQuery();
                    if (rsa.next()) {
                        final String sessionIP = rsa.getString("sessionIP");
                        if (sessionIP != null && sessionIP.matches("/[0-9]{1,3}\\..*")) {
                            final PreparedStatement psz = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                            psz.setString(1, sessionIP);
                            psz.execute();
                            psz.close();
                        }
                        if (rsa.getString("macs") != null) {
                            final String[] macData = rsa.getString("macs").split(", ");
                            if (macData.length > 0) {
                                MapleClient.banMacs(macData);
                            }
                        }
                        if (hellban) {
                            final PreparedStatement pss = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE email = ?" + ((sessionIP == null) ? "" : " OR SessionIP = ?"));
                            pss.setString(1, reason);
                            pss.setString(2, rsa.getString("email"));
                            if (sessionIP != null) {
                                pss.setString(3, sessionIP);
                            }
                            pss.execute();
                            pss.close();
                        }
                    }
                    rsa.close();
                    psa.close();
                }
                ret = true;
            }
            rs.close();
            ps.close();
            con.close();
            return ret;
        } catch (SQLException ex) {
            System.err.println("Error while banning" + ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex2) {
            }
        }
        return false;
    }

    @Override
    public int getObjectId() {
        return this.getId();
    }

    @Override
    public void setObjectId(final int id) {
        throw new UnsupportedOperationException();
    }

    public MapleStorage getStorage() {
        return this.storage;
    }

    public void addVisibleMapObject(final MapleMapObject mo) {
        this.getVisibleMapObjects().add(mo);
    }

    public void removeVisibleMapObject(final MapleMapObject mo) {
        this.getVisibleMapObjects().remove(mo);
    }

    public boolean isMapObjectVisible(final MapleMapObject mo) {
        return this.getVisibleMapObjects().contains(mo);
    }

    public String getChairText() {
        return this.chairtext;
    }

    public void setChairText(final String chairtext) {
        this.chairtext = chairtext;
    }

    public ScheduledFuture<?> getMesoChairTimer() {
        return this.MesoChairTimer;
    }

    public void setMesoChairTimer(final ScheduledFuture<?> a1) {
        this.MesoChairTimer = a1;
        this.tempmeso = 0;
    }

    public int getMesoChairCount() {
        return (this.MesoChairCount > 999999999) ? 999999999 : this.MesoChairCount;
    }

    public ScheduledFuture<?> getEventSkillTimer() {
        return this.EventSkillTimer;
    }

    public void setEventSkillTimer(final ScheduledFuture<?> a1) {
        this.EventSkillTimer = a1;
    }

    public void UpdateMesoChairCount(final int a1) {
        if (this.tempmeso >= a1) {
            this.MesoChairTimer.cancel(true);
            this.MesoChairTimer = null;
            this.setChair(0);
            this.setChairText("");
            this.getClient().getSession().writeAndFlush((Object) CField.cancelChair(-1, this));
            this.getMap().broadcastMessage(this, CField.showChair(this, 0), true);
            return;
        }
        this.MesoChairCount += 500;
        this.gainMeso(-500L, false);
        this.tempmeso += 500;
        this.getMap().broadcastMessage(SLFCGPacket.MesoChairPacket(this.getId(), 500, this.getChair()));
    }

    public long getStarDustPoint2() {
        return Long.valueOf(getKeyValue(501661, "e"));
    }

    public long getStarDustCoin2() {
        return Long.valueOf(getKeyValue(501661, "point"));
    }

    public void AddStarDustPoint2(int a) {
        int add = 0;
        if (getKeyValue(501661, "e") + a >= 100) {
            if (getKeyValue(501661, "point") == Integer.MAX_VALUE) {
                return;
            }

            setKeyValue(501661, "e", String.valueOf(getKeyValue(501661, "e") + a));

            while (getKeyValue(501661, "e") >= 100) {
                add++;
                setKeyValue(501661, "point", String.valueOf(getKeyValue(501661, "point") + 1));
                setKeyValue(501661, "e", String.valueOf(getKeyValue(501661, "e") - 100));
            }
            client.getSession().writeAndFlush(SLFCGPacket.StarDustIncrease((int) getKeyValue(501661, "e"), a, false, (int) getKeyValue(501661, "point"), add, getPosition()));
        } else {
            setKeyValue(501661, "e", String.valueOf(getKeyValue(501661, "e") + a));
            client.getSession().writeAndFlush(SLFCGPacket.StarDustIncrease((int) getKeyValue(501661, "e"), a, false, (int) getKeyValue(501661, "point"), add, getPosition()));
        }
    }

    public void AddStarDustCoin2(int a) {
        setKeyValue(501661, "point", String.valueOf(getKeyValue(501661, "point") + a));
        if (a < 0) {
            getClient().getSession().writeAndFlush(SLFCGPacket.StarDustIncrease((int) getKeyValue(501661, "e"), 0, false, (int) getKeyValue(501661, "point"), 0, getPosition()));
        } else {
            if (getKeyValue(501661, "point") == Integer.MAX_VALUE) {
                return;
            }
            getClient().getSession().writeAndFlush(SLFCGPacket.StarDustIncrease((int) getKeyValue(501661, "e"), 0, false, (int) getKeyValue(501661, "point"), 0, getPosition()));
        }
    }

    public long getStarDustPoint(final int type) {
        switch (type) {
            case 1: {
                return this.getKeyValue(501661, "total");
            }
            case 2: {
                return this.getKeyValue(501661, "total");
            }
            default: {
                return this.getKeyValue(501661, "total");
            }
        }
    }

    public long getStarDustCoin(final int type) {
        switch (type) {
            case 1: {
                return this.getKeyValue(501661, "point");
            }
            case 2: {
                return this.getKeyValue(501661, "point");
            }
            default: {
                return this.getKeyValue(501661, "point");
            }
        }
    }

    public void AddStarDustCoin(final int type, int a) {
        int key = 100711;
        int max = 1000000;
        switch (type) {
            case 1: {
                if (this.getKeyValue(501661, "today") >= 1000000L) {
                    return;
                }
                if (this.getKeyValue(501661, "today") + a >= 1000000L) {
                    a = 1000000 - (int) (this.getKeyValue(100711, "today") + a);
                    return;
                }
                key = 100711;
                max = 1000000;
                break;
            }
            case 2: {
                key = 100712;
                max = 1000;
                break;
            }
            case 3: {
                if (this.getKeyValue(501661, "week") >= 10000L) {
                    return;
                }
                if (this.getKeyValue(501661, "week") + a >= 10000L) {
                    a = 10000 - (int) (this.getKeyValue(501215, "week") + a);
                    return;
                }
                key = 501215;
                max = 10000;
                break;
            }
        }
        this.setKeyValue(key, "point", String.valueOf(this.getKeyValue(key, "point") + a));
        this.setKeyValue(key, (type == 3) ? "week" : "today", String.valueOf(this.getKeyValue(key, (type == 3) ? "week" : "today") + a));
        if (a >= 0) {
            if (this.getKeyValue(key, "point") + a > 2147483647L) {
                this.setKeyValue(key, "point", "2147483647");
            }
        }
    }

    public void AddStarDustPoint(int type, int a, Point point) {
        int key = 100711;
        int max = 1000000;
        switch (type) {
            case 1:
                key = 100711;
                max = 1000000;
                break;
            case 2:
                key = 100712;
                max = 1000;
                break;
            case 3:
                key = 501215;
                max = 10000;
                break;
        }
        if (getKeyValue(key, (type == 3) ? "week" : "today") == -1L) {
            setKeyValue(key, (type == 3) ? "week" : "today", "0");
        }
        if (getKeyValue(key, "point") == -1L) {
            setKeyValue(key, "point", "0");
        }
        if (getKeyValue(key, "total") == -1L) {
            setKeyValue(key, "total", "0");
        }
        if (getKeyValue(key, "sum") == -1L) {
            setKeyValue(key, "sum", "0");
        }
        if (getKeyValue(key, "total") + a >= ((type == 1) ? 100L : 1L)) {
            if (getKeyValue(key, "point") == 2147483647L || getKeyValue(key, "point") < 0L || getKeyValue(key, (type == 3) ? "week" : "today") >= max) {
                return;
            }
            if (getKeyValue(key, (type == 3) ? "week" : "today") + a >= max) {
                a = max - a;
            }
            if (a <= 0) {
                return;
            }
            setKeyValue(key, "point", String.valueOf(getKeyValue(key, "point") + (a / ((type == 1) ? 100 : 1))));
            setKeyValue(key, (type == 3) ? "week" : "today", String.valueOf(getKeyValue(key, (type == 3) ? "week" : "today") + (a / ((type == 1) ? 100 : 1))));
            setKeyValue(key, "sum", String.valueOf(getKeyValue(key, "sum") + (a / ((type == 1) ? 100 : 1))));
            setKeyValue(key, "total", String.valueOf(getKeyValue(key, "total") + a));
        } else {
            setKeyValue(key, "total", String.valueOf(getKeyValue(key, "total") + a));
        }
    }

    public void AddBloomingCoin(int add, Point point) {
        if (getKeyValue(100794, "point") < 0L) {
            setKeyValue(100794, "point", "0");
        }
        if (getKeyValue(100794, "today") < 0L) {
            setKeyValue(100794, "today", "0");
        }
        boolean lock = false;
        if (add > 0) {
            if (getKeyValue(100794, "today") >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000L : 3000L)) {
                lock = true;
            }
            if (getKeyValue(100794, "today") + add >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000L : 3000L)) {
                lock = true;
                int minus = (int) (getKeyValue(100794, "today") + add - ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000L : 3000L));
                add -= minus;
                setKeyValue(100794, "point", (getKeyValue(100794, "point") + add) + "");
            }
            if (lock) {
                setKeyValue(100794, "today", "" + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000 : 3000) + "");
                setKeyValue(100794, "lock", "1");
                getClient().send(SLFCGPacket.StarDustUI("UI/UIWindowEvent.img/starDust_18th", getKeyValue(100794, "sum"), getKeyValue(100794, "point"), (getKeyValue(100794, "lock") == 1L)));
                return;
            }
            if (point != null) {
                this.client.getSession().writeAndFlush(SLFCGPacket.StarDustIncrease(0, 100, (getKeyValue(100794, "today") >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000L : 3000L)), (int) getKeyValue(100794, "point"), 1, new Point(0, 0)));
            }
        }
        setKeyValue(100794, "point", (getKeyValue(100794, "point") + add) + "");
        if (add > 0) {
            setKeyValue(100794, "today", (getKeyValue(100794, "today") + add) + "");
        }
    }

    public int getBloomingCoin() {
        return (int) this.getKeyValue(100794, "point");
    }


    public void AddCoin(final int questid, final int add) {
        this.AddCoin(questid, add, false);
    }

    public void AddCoin(final int questid, int add, final boolean ig) {
        if (this.getKeyValue(questid, "point") < 0L) {
            this.setKeyValue(questid, "point", "0");
        }
        if (this.getKeyValue(questid, "today") < 0L) {
            this.setKeyValue(questid, "today", "0");
        }
        boolean lock = false;
        if (add > 0 && !ig) {
            if (this.getKeyValue(questid, "lock") == 1L) {
                return;
            }
            if (this.getKeyValue(questid, "today") >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 600 : 300)) {
                lock = true;
            }
            if (this.getKeyValue(questid, "today") + add >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 600 : 300)) {
                lock = true;
                final int minus = (int) (this.getKeyValue(questid, "today") + add - ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 600 : 300));
                add -= minus;
                this.setKeyValue(questid, "point", this.getKeyValue(questid, "point") + add + "");
            }
            if (lock) {
                this.setKeyValue(questid, "today", "" + ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 600 : 300) + "");
                this.setKeyValue(questid, "lock", "1");
                return;
            }
        }
        this.setKeyValue(questid, "point", this.getKeyValue(questid, "point") + add + "");
        if (add > 0 && !ig) {
            this.setKeyValue(questid, "today", this.getKeyValue(questid, "today") + add + "");
        }
    }

    public void AddCoinAcc(final int questid, final int add) {
        this.AddCoinAcc(questid, add, false);
    }

    public void AddCoinAcc(final int questid, int add, final boolean ig) {
        if (this.client.getCustomKeyValue(questid, "point") < 0L) {
            this.client.setCustomKeyValue(questid, "point", "0");
        }
        if (this.client.getCustomKeyValue(questid, "week") < 0L) {
            this.client.setCustomKeyValue(questid, "week", "0");
        }
        boolean lock = false;
        if (add > 0 && !ig) {
            if (this.client.getCustomKeyValue(questid, "lock") == 1L) {
                return;
            }
            if (this.client.getCustomKeyValue(questid, "week") >= 400L) {
                lock = true;
            }
            if (this.client.getCustomKeyValue(questid, "week") + add >= 400L) {
                lock = true;
                final int minus = (int) (this.client.getCustomKeyValue(questid, "week") + add - 400L);
                add -= minus;
                this.client.setCustomKeyValue(questid, "point", this.client.getCustomKeyValue(questid, "point") + add + "");
            }
            if (lock) {
                this.client.setCustomKeyValue(questid, "week", "400");
                this.client.setCustomKeyValue(questid, "lock", "1");
                return;
            }
        }
        this.client.setCustomKeyValue(questid, "point", this.client.getCustomKeyValue(questid, "point") + add + "");
        if (add > 0 && !ig) {
            this.client.setCustomKeyValue(questid, "week", this.client.getCustomKeyValue(questid, "week") + add + "");
        }
    }

    public int getCoin(final int questid) {
        return (int) this.getKeyValue(questid, "point");
    }

    public void addDojoCoin(final int a) {
        this.setKeyValue(3887, "point", String.valueOf(this.getKeyValue(3887, "point") + a));
    }

    public Point getResolution() {
        return this.Resolution;
    }

    public void setResolution(final int Width, final int Height) {
        this.Resolution = new Point(Width, Height);
    }

    public DetectiveGame getDetectiveGame() {
        return this.DetectiveGameInstance;
    }

    public void setDetectiveGame(final DetectiveGame a1) {
        this.DetectiveGameInstance = a1;
    }

    public OXQuizGame getOXGame() {
        return this.OXInstance;
    }

    public void setOXGame(final OXQuizGame a1) {
        this.OXInstance = a1;
    }

    public BingoGame getBingoGame() {
        return this.BingoInstance;
    }

    public void setBingoGame(final BingoGame a1) {
        this.BingoInstance = a1;
    }

    public BattleReverse getBattleReverseInstance() {
        return this.BattleReverseInstance;
    }

    public void setBattleReverseInstance(final BattleReverse a1) {
        this.BattleReverseInstance = a1;
    }

    public boolean isAFK(final long currenttick) {
        if (this.LastMovement == 0L || this.getMap().getId() != ServerConstants.fishMap || this.getChair() == 0) {
            return false;
        }
        final long temp = currenttick - this.LastMovement;
        return temp / 1000L >= 60L;
    }

    public void setLastMovement(final long a) {
        this.LastMovement = a;
    }

    public void setPlatformerStageEnter(final long a) {
        this.PlatformerStageEnter = a;
    }

    public long getPlatformerStageEnter() {
        return this.PlatformerStageEnter;
    }

    public void setPlatformerTimer(final ScheduledFuture<?> a) {
        this.PlatformerTimer = a;
    }

    public ScheduledFuture<?> getPlatformerTimer() {
        return this.PlatformerTimer;
    }

    public int getBlockCount() {
        return this.BlockCount;
    }

    public void setBlockCount(final int a1) {
        this.BlockCount = a1;
    }

    public void setBlockCoin(final int a1) {
        this.BlockCoin = a1;
    }

    public int getBlockCoin() {
        return this.BlockCoin;
    }

    public void addBlockCoin(final int a1) {
        this.BlockCoin += a1;
    }

    public int getRandomPortal() {
        return this.RandomPortal;
    }

    public void setRandomPortal(final int a1) {
        this.RandomPortal = a1;
    }

    public boolean hasFWolfPortal() {
        return this.hasfwolfportal;
    }

    public void setFWolfPortal(final boolean a1) {
        this.hasfwolfportal = a1;
    }

    public boolean isWatchingWeb() {
        return this.isWatchingWeb;
    }

    public void setWatchingWeb(final boolean a1) {
        this.isWatchingWeb = a1;
    }

    public boolean isFWolfKiller() {
        return this.isfwolfkiller;
    }

    public void setFWolfKiller(final boolean a1) {
        this.isfwolfkiller = a1;
    }

    public long getFWolfDamage() {
        return this.fwolfdamage;
    }

    public void setFWolfDamage(final long a1) {
        this.fwolfdamage = a1;
    }

    public int getFWolfAttackCount() {
        return this.fwolfattackcount;
    }

    public void setFWolfAttackCount(final int a1) {
        this.fwolfattackcount = a1;
    }

    public boolean isAlive() {
        return this.stats.getHp() > 0L;
    }

    @Override
    public void sendDestroyData(final MapleClient client) {
        client.getSession().writeAndFlush((Object) CField.removePlayerFromMap(this.getObjectId()));
    }

    @Override
    public void sendSpawnData(final MapleClient client) {
        if (client.getPlayer().allowedToTarget(this)) {
            if (this.getMapId() != 921172000 && this.getMapId() != 921172100) {
                client.getSession().writeAndFlush((Object) CField.spawnPlayerMapobject(this));
            }
            if (getKeyValue(190823, "grade") > 0) {
                client.getSession().writeAndFlush(SLFCGPacket.SetupZodiacInfo());
                client.getSession().writeAndFlush(SLFCGPacket.ZodiacRankInfo(getId(), (int) getKeyValue(190823, "grade")));
            }
            
            client.getPlayer().receivePartyMemberHP();
            if (this.dragon != null) {
                client.getSession().writeAndFlush((Object) CField.spawnDragon(this.dragon));
            }
            if (this.android != null) {
                client.getSession().writeAndFlush((Object) CField.spawnAndroid(this, this.android));
            }
            if (this.getGuild() != null && this.getGuild().getCustomEmblem() != null && client.getAccID() != this.getAccountID()) {
                client.getSession().writeAndFlush((Object) CField.loadGuildIcon(this));
            }
            for (final MapleSummon summon : this.summons) {
                if (summon.getMovementType() != SummonMovementType.STATIONARY) {
                    client.getSession().writeAndFlush((Object) CField.SummonPacket.spawnSummon(summon, true));
                    switch (summon.getSkill()) {
                        case 5201012:
                        case 5201013:
                        case 5201014:
                        case 5210015:
                        case 5210016:
                        case 5210017:
                        case 5210018: {
                            if (summon.getDebuffshell() <= 0) {
                                this.getMap().broadcastMessage(CField.SummonPacket.summonDebuff(summon.getOwner().getId(), summon.getObjectId()));
                                continue;
                            }
                            continue;
                        }
                    }
                }
            }
            if (GameConstants.isAdel(client.getPlayer().getJob()) && client.getPlayer().getBuffedValue(151101006)) {
                client.getPlayer().에테르핸들러(client.getPlayer(), 0, 0, true);
            }
            if (GameConstants.isAdel(this.getJob()) && this.getBuffedValue(151101006)) {
                if (this.에테르소드 > 0 && this.에테르소드 <= 2) {
                    this.getMap().broadcastMessage(this, SkillPacket.CreateSworldReadyObtacle(this, 15112, 2), false);
                } else if (this.에테르소드 > 0 && this.에테르소드 <= 4) {
                    this.getMap().broadcastMessage(this, SkillPacket.CreateSworldReadyObtacle(this, 15112, 2), false);
                    this.getMap().broadcastMessage(this, SkillPacket.CreateSworldReadyObtacle(this, 15112, 4), false);
                } else if (this.에테르소드 > 0 && this.에테르소드 <= 6) {
                    this.getMap().broadcastMessage(this, SkillPacket.CreateSworldReadyObtacle(this, 15112, 2), false);
                    this.getMap().broadcastMessage(this, SkillPacket.CreateSworldReadyObtacle(this, 15112, 4), false);
                    this.getMap().broadcastMessage(this, SkillPacket.CreateSworldReadyObtacle(this, 15112, 6), false);
                }
            }
            if (this.getBuffedValue(SecondaryStat.RepeatEffect) != null) {
                final int skillid = this.getBuffedEffect(SecondaryStat.RepeatEffect).getSourceId();
                if (GameConstants.isAngelicBlessBuffEffectItem(skillid)) {
                    final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
                    statups.put(SecondaryStat.RepeatEffect, new Pair<Integer, Integer>(1, 0));
                    final SecondaryStatEffect effect = MapleItemInformationProvider.getInstance().getItemEffect(skillid);
                    this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
                }
            }
        }
    }

    public final void equipChanged() {
        if (this.map == null) {
            return;
        }
        boolean second = false;
        if (GameConstants.isAngelicBuster(this.getJob())) {
            second = this.getDressup();
        }
        if (GameConstants.isZero(this.getJob())) {
            second = (this.getGender() == 1);
        }
        this.map.broadcastMessage(this, CField.updateCharLook(this, second), false);
        this.stats.recalcLocalStats(this);
        if (this.getMessenger() != null) {
            World.Messenger.updateMessenger(this.getMessenger().getId(), this.getName(), this.client.getChannel());
        }
    }

    public final MaplePet getPet(final long index) {
        return this.pets[(int) index];
    }

    public void updatePet() {
        for (int i = 0; i < 3; ++i) {
            if (this.pets[i] != null) {
                this.getClient().getSession().writeAndFlush((Object) PetPacket.updatePet(this, this.pets[i], this.getInventory(MapleInventoryType.CASH).getItem(this.pets[i].getInventoryPosition()), false, this.petLoot));
            }
        }
    }

    public void addPet(final MaplePet pet) {
        for (int i = 0; i < 3; ++i) {
            if (this.pets[i] == null) {
                this.pets[i] = pet;
                return;
            }
        }
    }

    public void addPetBySlotId(final MaplePet pet, final int slotid) {
        if (this.pets[slotid] == null) {
            (this.pets[slotid] = pet).setPos(this.getPosition());
        }
    }

    public void setDotDamage(final long dmg) {
        this.DotDamage = dmg;
    }

    public long getDotDamage() {
        return this.DotDamage;
    }

    public Point getFlameHeiz() {
        return this.flameHeiz;
    }

    public void setFlameHeiz(final Point flameHeiz) {
        this.flameHeiz = flameHeiz;
    }

    public void removePet(final MaplePet pet, final boolean shiftLeft) {
        int slot = -1;
        for (int i = 0; i < 3; ++i) {
            if (this.pets[i] != null && this.pets[i].getUniqueId() == pet.getUniqueId()) {
                this.pets[i] = null;
                slot = i;
                break;
            }
        }
        if (shiftLeft && slot > -1) {
            for (int i = slot; i < 3; ++i) {
                if (i != 2) {
                    this.pets[i] = this.pets[i + 1];
                } else {
                    this.pets[i] = null;
                }
            }
        }
    }

    public final int getPetIndex(final MaplePet pet) {
        for (int i = 0; i < 3; ++i) {
            if (this.pets[i] != null && this.pets[i].getUniqueId() == pet.getUniqueId()) {
                return i;
            }
        }
        return -1;
    }

    public final int getPetIndex(final long petId) {
        for (int i = 0; i < 3; ++i) {
            if (this.pets[i] != null && this.pets[i].getUniqueId() == petId) {
                return i;
            }
        }
        return -1;
    }

    public final List<MaplePet> getSummonedPets() {
        final List<MaplePet> ret = new ArrayList<MaplePet>();
        for (final MaplePet pet : this.pets) {
            if (pet.getSummoned()) {
                ret.add(pet);
            }
        }
        return ret;
    }

    public final byte getPetById(final int petId) {
        byte count = 0;
        for (final MaplePet pet : this.pets) {
            if (pet.getSummoned()) {
                if (pet.getPetItemId() == petId) {
                    return count;
                }
                ++count;
            }
        }
        return -1;
    }

    public final MaplePet[] getPets() {
        return this.pets;
    }

    public final void unequipAllPets() {
        for (final MaplePet pet : this.pets) {
            if (pet != null) {
                this.unequipPet(pet, true, false);
            }
        }
    }

    public void unequipPet(final MaplePet pet, final boolean shiftLeft, final boolean hunger) {
        pet.setSummoned((byte) 0);
        this.client.getSession().writeAndFlush((Object) PetPacket.updatePet(this, pet, this.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), true, this.petLoot));
        if (this.map != null) {
            this.map.broadcastMessage(this, PetPacket.showPet(this, pet, true, hunger), true);
        }
        this.removePet(pet, shiftLeft);
        this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this));
    }

    public final long getLastFameTime() {
        return this.lastfametime;
    }

    public final List<Integer> getFamedCharacters() {
        return this.lastmonthfameids;
    }

    public final List<Integer> getBattledCharacters() {
        return this.lastmonthbattleids;
    }

    public FameStatus canGiveFame(final MapleCharacter from) {
        if (this.lastfametime >= System.currentTimeMillis() - 86400000L) {
            return FameStatus.NOT_TODAY;
        }
        if (from == null || this.lastmonthfameids == null || this.lastmonthfameids.contains(from.getId())) {
            return FameStatus.NOT_THIS_MONTH;
        }
        return FameStatus.OK;
    }

    public void hasGivenFame(final MapleCharacter to) {
        this.lastfametime = System.currentTimeMillis();
        this.lastmonthfameids.add(to.getId());
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("INSERT INTO famelog (characterid, characterid_to) VALUES (?, ?)");
            ps.setInt(1, this.getId());
            ps.setInt(2, to.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ERROR writing famelog for char " + this.getName() + " to " + to.getName() + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public boolean canBattle(final MapleCharacter to) {
        return to != null && this.lastmonthbattleids != null && !this.lastmonthbattleids.contains(to.getAccountID());
    }

    public void hasBattled(final MapleCharacter to) {
        this.lastmonthbattleids.add(to.getAccountID());
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("INSERT INTO battlelog (accid, accid_to) VALUES (?, ?)");
            ps.setInt(1, this.getAccountID());
            ps.setInt(2, to.getAccountID());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ERROR writing battlelog for char " + this.getName() + " to " + to.getName() + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public final MapleKeyLayout getKeyLayout() {
        return this.keylayout;
    }

    public MapleParty getParty() {
        if (this.party == null) {
            return null;
        }
        if (this.party.isDisbanded()) {
            this.party = null;
        }
        return this.party;
    }

    public byte getWorld() {
        return this.world;
    }

    public void setWorld(final byte world) {
        this.world = world;
    }

    public void setParty(final MapleParty party) {
        this.party = party;
    }

    public MapleTrade getTrade() {
        return this.trade;
    }

    public void setTrade(final MapleTrade trade) {
        this.trade = trade;
    }

    public EventInstanceManager getEventInstance() {
        return this.eventInstance;
    }

    public void setEventInstance(final EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public void addDoor(final MapleDoor door) {
        this.doors.add(door);
    }

    public void clearDoors() {
        this.doors.clear();
    }

    public List<MapleDoor> getDoors() {
        return new ArrayList<MapleDoor>(this.doors);
    }

    public void addMechDoor(final MechDoor door) {
        this.mechDoors.add(door);
    }

    public void clearMechDoors() {
        this.mechDoors.clear();
    }

    public List<MechDoor> getMechDoors() {
        return new ArrayList<MechDoor>(this.mechDoors);
    }

    public void setSmega() {
        if (this.smega) {
            this.smega = false;
            this.dropMessage(5, "You have set megaphone to disabled mode");
        } else {
            this.smega = true;
            this.dropMessage(5, "You have set megaphone to enabled mode");
        }
    }

    public boolean getSmega() {
        return this.smega;
    }

    public List<MapleSummon> getSummons() {
        return this.summons;
    }

    public MapleSummon getSummon(final int skillId) {
        for (final MapleSummon s : this.summons) {
            if (s.getSkill() == skillId) {
                return s;
            }
        }
        return null;
    }

    public List<MapleSummon> getSummons(final int skillId) {
        final List<MapleSummon> arr = new ArrayList<MapleSummon>();
        for (final MapleSummon s : this.summons) {
            if (GameConstants.getLinkedSkill(s.getSkill()) == skillId) {
                arr.add(s);
            }
        }
        return arr;
    }

    public int getSummonsSize() {
        return this.summons.size();
    }

    public void addSummon(final MapleSummon s) {
        this.summons.add(s);
    }

    public void removeSummon(final MapleSummon s) {
        this.summons.remove(s);
        if (s.getSkill() == 400011065) {
            this.cooldownEllision = System.currentTimeMillis();
        }
    }

    public int getChair() {
        return this.chair;
    }

    public int getItemEffect() {
        return this.itemEffect;
    }

    public void setChair(final int chair) {
        this.chair = chair;
        this.stats.relocHeal(this);
    }

    public void setItemEffect(final int itemEffect) {
        this.itemEffect = itemEffect;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.PLAYER;
    }

    public int getCurrentRep() {
        return this.currentrep;
    }

    public int getTotalRep() {
        return this.totalrep;
    }

    public int getTotalWins() {
        return this.totalWins;
    }

    public int getTotalLosses() {
        return this.totalLosses;
    }

    public void increaseTotalWins() {
        ++this.totalWins;
    }

    public void increaseTotalLosses() {
        ++this.totalLosses;
    }

    public int getGuildId() {
        return this.guildid;
    }

    public byte getGuildRank() {
        return this.guildrank;
    }

    public int getGuildContribution() {
        return this.guildContribution;
    }

    public int getLastAttendance() {
        return this.lastattendance;
    }

    public void setLastAttendance(final int _c) {
        this.lastattendance = _c;
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE characters SET lastattendance = ? WHERE id = ?");
            ps.setInt(1, this.lastattendance);
            ps.setInt(2, this.id);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex2) {
            }
        }
        if (this.mgc != null) {
            this.mgc.setLastAttendance(_c);
        }
    }

    public void setGuildId(final int _id) {
        this.guildid = _id;
        if (this.guildid > 0) {
            if (this.mgc == null) {
                this.mgc = new MapleGuildCharacter(this);
            } else {
                this.mgc.setGuildId(this.guildid);
            }
        } else {
            this.mgc = null;
            this.guildContribution = 0;
        }
    }

    public void setGuildRank(final byte _rank) {
        this.guildrank = _rank;
        if (this.mgc != null) {
            this.mgc.setGuildRank(_rank);
        }
    }

    public void setGuildContribution(final int _c) {
        this.guildContribution = _c;
        if (this.mgc != null) {
            this.mgc.setGuildContribution(_c);
        }
    }

    public MapleGuildCharacter getMGC() {
        return this.mgc;
    }

    public void setAllianceRank(final byte rank) {
        this.allianceRank = rank;
        if (this.mgc != null) {
            this.mgc.setAllianceRank(rank);
        }
    }

    public byte getAllianceRank() {
        return this.allianceRank;
    }

    public MapleGuild getGuild() {
        if (this.getGuildId() <= 0) {
            return null;
        }
        return World.Guild.getGuild(this.getGuildId());
    }

    public void setJob(final int j) {
        this.job = (short) j;
    }

    public void guildUpdate() {
        if (this.guildid <= 0) {
            return;
        }
        this.mgc.setLevel(this.level);
        this.mgc.setJobId(this.job);
        if (this.level != 256) {
            World.Guild.memberLevelJobUpdate(this.mgc);
        }
    }

    public void runRainBowRush() {
        this.RainBowRushTime = 0;
        this.israinbow = true;
        this.warp(993190000);
        this.getClient().getSession().writeAndFlush((Object) CField.UIPacket.getRainBowRushSetting());
    }

    public void setRainbowRushStart(final long time) {
        this.rainbowrushStartTime = time;
    }

    public int getRainbowRushTime() {
        return this.RainBowRushTime;
    }

    public void setRainbowRushTime(final int time) {
        this.RainBowRushTime = time;
    }

    public void setRainBowRush(final boolean b) {
        this.israinbow = b;
    }

    public boolean isRainBowRush() {
        return this.israinbow;
    }

    public boolean isNoDeadRush() {
        return this.isnodeadRush;
    }

    public void setNoDeadRush(final boolean b) {
        this.isnodeadRush = b;
    }

    public void saveGuildStatus() {
        MapleGuild.setOfflineGuildStatus(this.guildid, this.guildrank, this.guildContribution, this.allianceRank, this.id);
    }

    public void modifyCSPoints(final int type, final int quantity) {
        this.modifyCSPoints(type, quantity, false);
    }

    public void setNXcredit(int nxcredit) {
        this.nxcredit = nxcredit;
    }

    public void modifyCSPoints(final int type, final int quantity, final boolean show) {
        switch (type) {
            case 1: {
                if (this.nxcredit + quantity < 0) {
                    if (show) {
                        this.dropMessage(-1, "You have gained the max cash. No cash will be awarded.");
                    }
                    return;
                }
                this.nxcredit += quantity;
                break;
            }
            case 2: {
                if (this.maplepoints + quantity < 0) {
                    if (show) {
                        this.dropMessage(-1, "You have gained the max cash. No cash will be awarded.");
                    }
                    return;
                }
                this.maplepoints += quantity;
                break;
            }
            case 4: {
                if (this.acash + quantity < 0) {
                    if (show) {
                        this.dropMessage(-1, "You have gained the max cash. No cash will be awarded.");
                    }
                    return;
                }
                this.acash += quantity;
                break;
            }
        }
        if (!show || quantity != 0) {
        }
    }

    public int getCSPoints(final int type) {
        switch (type) {
            case 1: {
                return this.nxcredit;
            }
            case 2: {
                return this.maplepoints;
            }
            case 4: {
                return this.acash;
            }
            default: {
                return 0;
            }
        }
    }

    public final boolean hasEquipped(final int itemid) {
        return this.inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid) >= 1;
    }

    public final boolean haveItem(final int itemid, final int quantity, final boolean checkEquipped, final boolean greaterOrEquals) {
        final MapleInventoryType type = GameConstants.getInventoryType(itemid);
        int possesed = this.inventory[type.ordinal()].countById(itemid);
        if (checkEquipped && type == MapleInventoryType.EQUIP) {
            possesed += this.inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        if (greaterOrEquals) {
            return possesed >= quantity;
        }
        return possesed == quantity;
    }

    public final boolean haveItem(final int itemid, final int quantity) {
        return this.haveItem(itemid, quantity, true, true);
    }

    public final boolean haveItem(final int itemid) {
        return this.haveItem(itemid, 1, true, true);
    }

    public byte getBuddyCapacity() {
        return this.buddylist.getCapacity();
    }

    public void setBuddyCapacity(final byte capacity) {
        this.buddylist.setCapacity(capacity);
        this.client.getSession().writeAndFlush((Object) CWvsContext.BuddylistPacket.updateBuddyCapacity(capacity));
    }

    public MapleMessenger getMessenger() {
        return this.messenger;
    }

    public void setMessenger(final MapleMessenger messenger) {
        this.messenger = messenger;
    }

    public void addCooldown(final int skillId, final long startTime, final long length) {
        this.coolDowns.put(skillId, new MapleCoolDownValueHolder(skillId, startTime, length));
    }

    public void removeCooldown(final int skillId) {
        if (this.coolDowns.containsKey(skillId)) {
            if (skillId == 400051047 || skillId == 400051048) {
                if (skillId == 400051047) {
                    this.getWeaponChanges().clear();
                } else if (skillId == 400051048) {
                    this.getWeaponChanges2().clear();
                }
            }
            this.coolDowns.remove(skillId);
            this.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(skillId, 0));
        }
    }

    public void changeCooldown(final int skillId, final int reduce) {
        if (this.coolDowns.containsKey(skillId)) {
            final MapleCoolDownValueHolder mapleCoolDownValueHolder = this.coolDowns.get(skillId);
            mapleCoolDownValueHolder.length += reduce;
            this.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(skillId, (int) Math.max(0L, this.coolDowns.get(skillId).length - (System.currentTimeMillis() - this.coolDowns.get(skillId).startTime))));
            if (System.currentTimeMillis() - this.coolDowns.get(skillId).startTime >= this.coolDowns.get(skillId).length) {
                this.removeCooldown(skillId);
            }
        }
    }

    public boolean skillisCooling(final int skillId) {
        return (!this.coolDowns.containsKey(skillId) || this.coolDowns.get(skillId).startTime + this.coolDowns.get(skillId).length - System.currentTimeMillis() >= 0L) && this.coolDowns.containsKey(skillId);
    }

    public long skillcool(final int skillId) {
        return this.coolDowns.get(skillId).startTime + this.coolDowns.get(skillId).length - System.currentTimeMillis();
    }

    public void giveCoolDowns(final int skillid, final long starttime, final long length) {
        this.addCooldown(skillid, starttime, length);
    }

    public void giveCoolDowns(final List<MapleCoolDownValueHolder> cooldowns) {
        if (cooldowns != null) {
            for (final MapleCoolDownValueHolder cooldown : cooldowns) {
                this.coolDowns.put(cooldown.skillId, cooldown);
            }
        } else {
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT SkillID,StartTime,length FROM skills_cooldowns WHERE charid = ?");
                ps.setInt(1, this.getId());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getLong("length") + rs.getLong("StartTime") - System.currentTimeMillis() <= 0L) {
                        continue;
                    }
                    this.giveCoolDowns(rs.getInt("SkillID"), rs.getLong("StartTime"), rs.getLong("length"));
                }
                ps.close();
                rs.close();
                this.deleteWhereCharacterId(con, "DELETE FROM skills_cooldowns WHERE charid = ?");
                con.close();
            } catch (SQLException e) {
                System.err.println("Error while retriving cooldown from SQL storage");
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException ex) {
                }
            }
        }
    }

    public int getCooldownSize() {
        return this.coolDowns.size();
    }

    public int getDiseaseSize() {
        return this.getDiseases().size();
    }

    public List<MapleCoolDownValueHolder> getCooldowns() {
        final List<MapleCoolDownValueHolder> ret = new ArrayList<MapleCoolDownValueHolder>();
        for (final MapleCoolDownValueHolder mc : this.coolDowns.values()) {
            if (mc != null) {
                ret.add(mc);
            }
        }
        return ret;
    }

    public MapleDiseases getDisease(final SecondaryStat d) {
        return this.getDiseases().get(d);
    }

    public final boolean hasDisease(final SecondaryStat dis) {
        return this.getDiseases().containsKey(dis);
    }

    public boolean hasDisease(final SecondaryStat dis, final int skillid, final int skilllv) {
        for (final Map.Entry<SecondaryStat, MapleDiseases> disease : this.getDiseases().entrySet()) {
            if (disease.getKey().getFlag() == dis.getFlag() && disease.getValue().getMobskill().getSkillId() == skillid && disease.getValue().getMobskill().getSkillLevel() == skilllv) {
                return true;
            }
        }
        return false;
    }

    public void disease(final int skillId, final int mobSkillLevel) {
        final MobSkill ms = MobSkillFactory.getMobSkill(skillId, mobSkillLevel);
        final SecondaryStat disease = SecondaryStat.getBySkill(skillId);
        if (disease != null) {
            this.giveDebuff(disease, ms);
        }
    }

    public Integer getDebuffValue(final SecondaryStat stat) {
        for (final Map.Entry<SecondaryStat, MapleDiseases> disease : this.getDiseases().entrySet()) {
            if (disease.getKey() == stat) {
                return disease.getValue().getValue();
            }
        }
        return -1;
    }

    public void giveDebuff(final SecondaryStat disease, final MobSkill skill) {
        final Map<SecondaryStat, Pair<Integer, Integer>> diseases = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        diseases.put(disease, new Pair<Integer, Integer>(skill.getX(), (int) skill.getDuration()));
        this.giveDebuff(diseases, skill);
    }

    public void giveDebuff(final Map<SecondaryStat, Pair<Integer, Integer>> diseases, final MobSkill skill) {
        if (this.map != null && skill != null) {
            final Iterator<Map.Entry<SecondaryStat, Pair<Integer, Integer>>> diseasez = diseases.entrySet().iterator();
            if (this.getBuffedValue(3211011)) {
                return;
            }
            boolean noshell = false;
            MapleDiseases md = null;
            while (diseasez.hasNext()) {
                final Map.Entry<SecondaryStat, Pair<Integer, Integer>> disease = diseasez.next();
                md = new MapleDiseases(disease.getValue().left, disease.getValue().right, System.currentTimeMillis());
                switch (disease.getKey()) {
                    case Stigma:
                    case StopPortion: {
                        noshell = true;
                        break;
                    }
                }
                if (this.hasDisease(disease.getKey())) {
                    diseasez.remove();
                } else if (this.getBuffedValue(2211012) && this.antiMagicShell > 0 && !noshell) {
                    if (this.antiMagicShell == 1) {
                        diseasez.remove();
                        this.addCooldown(2211012, System.currentTimeMillis(), SkillFactory.getSkill(2211012).getEffect(this.getSkillLevel(2211012)).getCooldown(this));
                        this.client.getSession().writeAndFlush((Object) CField.skillCooldown(2211012, SkillFactory.getSkill(2211012).getEffect(this.getSkillLevel(2211012)).getCooldown(this)));
                        this.cancelEffect(this.getBuffedEffect(2211012));
                        SkillFactory.getSkill(2211012).getEffect(this.getSkillLevel(2211012)).applyTo(this, false);
                    } else if (this.antiMagicShell >= 10) {
                        diseasez.remove();
                    } else {
                        md.setMobskill(skill);
                        this.getDiseases().put(disease.getKey(), md);
                    }
                } else if (this.getBuffedValue(400001050) && this.getSkillCustomValue0(400001050) == 400001054L && !noshell) {
                    diseases.remove(disease.getKey());
                    final SecondaryStatEffect effect6 = SkillFactory.getSkill(400001050).getEffect(this.getSkillLevel(400001050));
                    this.removeSkillCustomInfo(400001050);
                    final long duration = this.getBuffLimit(400001050);
                    effect6.applyTo(this, (int) duration);
                } else if (this.antiMagicShell > 0 && this.getBuffedEffect(SecondaryStat.AntiMagicShell) != null && !noshell) {
                    diseasez.remove();
                    --this.antiMagicShell;
                    if (this.antiMagicShell == 0) {
                        this.cancelEffectFromBuffStat(SecondaryStat.AntiMagicShell);
                    } else {
                        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                        statups.put(SecondaryStat.AntiMagicShell, new Pair<Integer, Integer>((int) this.antiMagicShell, (int) this.getBuffLimit(this.getBuffSource(SecondaryStat.AntiMagicShell))));
                        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, this.getBuffedEffect(SecondaryStat.AntiMagicShell), this));
                    }
                } else if (this.getSpiritGuard() > 0 && !noshell) {
                    diseasez.remove();
                    this.setSpiritGuard(this.getSpiritGuard() - 1);
                    if (this.getSpiritGuard() == 0) {
                        this.cancelEffectFromBuffStat(SecondaryStat.SpiritGuard);
                    } else {
                        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                        statups.put(SecondaryStat.SpiritGuard, new Pair<Integer, Integer>(this.getSpiritGuard(), (int) this.getBuffLimit(this.getBuffSource(SecondaryStat.SpiritGuard))));
                        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, this.getBuffedEffect(SecondaryStat.SpiritGuard), this));
                    }
                } else {
                    if (skill.getDuration() <= 0L && disease.getKey() != SecondaryStat.Stigma) {
                        continue;
                    }
                    if (this.getBuffedValue(5220019) && !noshell) {
                        for (final MapleSummon sum : this.getSummons()) {
                            switch (sum.getSkill()) {
                                case 5201012:
                                case 5201013:
                                case 5201014:
                                case 5210015:
                                case 5210016:
                                case 5210017:
                                case 5210018: {
                                    if (sum.getDebuffshell() > 0) {
                                        diseasez.remove();
                                        sum.setDebuffshell(0);
                                        this.getMap().broadcastMessage(CField.SummonPacket.summonDebuff(this.getId(), sum.getObjectId()));
                                        return;
                                    }
                                    continue;
                                }
                            }
                        }
                    }
                    md.setMobskill(skill);
                    this.getDiseases().put(disease.getKey(), md);
                }
            }
            this.dropMessageGM(6, "Debuff" + diseases);
            if (!diseases.isEmpty()) {
                this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveDisease(diseases, skill, this));
                this.map.broadcastMessage(this, CWvsContext.BuffPacket.giveForeignDeBuff(this, diseases), false);
                if (skill.getDuration() > 0L) {
                    final SecondaryStatEffect.CancelDiseaseAction ca = new SecondaryStatEffect.CancelDiseaseAction(this, diseases);
                    md.setSchedule(server.Timer.BuffTimer.getInstance().schedule(() -> ca.run(), skill.getDuration()));
                } else {
                    this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(diseases, this));
                    this.map.broadcastMessage(this, CWvsContext.BuffPacket.cancelForeignBuff(this, diseases), false);
                }
                if (this.curseBound < 5 && this.getBuffedEffect(SecondaryStat.StopPortion) != null) {
                    ++this.curseBound;
                    final Map<SecondaryStat, Pair<Integer, Integer>> statups2 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups2.put(SecondaryStat.StopPortion, new Pair<Integer, Integer>(this.curseBound, (int) this.getBuffLimit(this.getBuffSource(SecondaryStat.StopPortion))));
                    this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups2, this.getBuffedEffect(SecondaryStat.StopPortion), this));
                }
            }
        }
    }

    public void cancelDisease(final SecondaryStat debuff) {
        if (this.diseases.containsKey(debuff)) {
            final MobSkill msi = this.diseases.get(debuff).getMobskill();
            final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
            statups.put(debuff, new Pair<Integer, Integer>(msi.getX(), (int) msi.getDuration()));
            this.cancelDisease(statups, false);
        }
    }

    public void cancelDisease(final Map<SecondaryStat, Pair<Integer, Integer>> statups, final boolean autocancel) {
        final Map<SecondaryStat, Pair<Integer, Integer>> cancelList = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        int skillid = 0;
        int skilllv = 0;
        if (statups != null) {
            for (final Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup : statups.entrySet()) {
                if (!this.getDiseases().containsKey(statup.getKey())) {
                    continue;
                }
                skillid = this.getDiseases().get(statup.getKey()).getMobskill().getSkillId();
                skilllv = this.getDiseases().get(statup.getKey()).getMobskill().getSkillLevel();
                if (this.getDiseases().get(statup.getKey()).getSchedule() != null) {
                    this.getDiseases().get(statup.getKey()).getSchedule().cancel(true);
                    this.getDiseases().get(statup.getKey()).setSchedule(null);
                }
                this.getDiseases().remove(statup.getKey());
                cancelList.put(statup.getKey(), statup.getValue());
            }
        }
        this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(cancelList, this));
        this.map.broadcastMessage(this, CWvsContext.BuffPacket.cancelForeignBuff(this, cancelList), false);
        if (autocancel) {
            if (cancelList.containsKey(SecondaryStat.PapulCuss)) {
                for (final MapleMonster monster : this.getMap().getAllMonster()) {
                    if (monster != null && (monster.getId() == 8500021 || monster.getId() == 8500011 || monster.getId() == 8500001) && monster.getCustomValue(2412) != null && monster.getBuff(MonsterStatus.MS_PopulatusTimer) != null) {
                        long duration = monster.getCustomTime(2412) - this.getSkillCustomTime(241);
                        if (duration > 30000L) {
                            duration = 30000L;
                        }
                        if (duration > 0L) {
                            final MobSkill ms = MobSkillFactory.getMobSkill(241, (monster.getHPPercent() >= 50) ? 2 : 1);
                            ms.setDuration(duration);
                            this.giveDebuff((monster.getHPPercent() >= 50) ? SecondaryStat.Seal : SecondaryStat.Stun, ms);
                            break;
                        }
                        break;
                    }
                }
            } else if (cancelList.containsKey(SecondaryStat.Contagion) || cancelList.containsKey(SecondaryStat.PapulBomb)) {
                int hit = 1;
                MapleMonster mob = null;
                for (final MapleMonster monster2 : this.getMap().getAllMonster()) {
                    if (monster2 != null && (monster2.getId() == 8880142 || monster2.getId() == 8880140 || monster2.getId() == 8880141 || monster2.getId() == 8880150 || monster2.getId() == 8880151 || monster2.getId() == 8880155 || monster2.getId() == 8500001 || monster2.getId() == 8500002 || monster2.getId() == 8500011 || monster2.getId() == 8500012 || monster2.getId() == 8500021 || monster2.getId() == 8500022)) {
                        mob = monster2;
                        break;
                    }
                }
                if (mob != null && skillid > 0 && skilllv > 0) {
                    boolean solo = true;
                    for (final MaplePartyCharacter chr1 : this.getParty().getMembers()) {
                        final MapleCharacter achr = this.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr1.getId());
                        if (achr != null && achr.getId() != this.getId() && this.getPosition().x - 150 <= achr.getPosition().x && this.getPosition().x + 150 >= achr.getPosition().x) {
                            ++hit;
                            solo = false;
                        }
                    }
                    if (solo) {
                        for (final Point p : this.getMap().getLucidDream()) {
                            if (p.x - 50 <= this.getPosition().x && p.x + 50 >= this.getPosition().x) {
                                ++hit;
                                break;
                            }
                        }
                    }
                    this.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(this, (skillid == 238) ? 176 : skillid, (skillid == 238) ? 38 : skilllv, 45, 0, 0, (byte) 0, true, null, null, null));
                    this.getMap().broadcastMessage(this, CField.EffectPacket.showEffect(this, (skillid == 238) ? 176 : skillid, (skillid == 238) ? 38 : skilllv, 45, 0, 0, (byte) 0, false, null, null, null), false);
                    for (final MaplePartyCharacter chr1 : this.getParty().getMembers()) {
                        final MapleCharacter achr = this.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr1.getId());
                        if (achr != null && this.getPosition().x - 150 <= achr.getPosition().x && this.getPosition().x + 150 >= achr.getPosition().x) {
                            int percent = cancelList.containsKey(SecondaryStat.Contagion) ? 180 : (cancelList.containsKey(SecondaryStat.PapulBomb) ? 80 : 0);
                            percent /= hit;
                            achr.getPercentDamage(mob, skillid, skilllv, percent, true);
                        }
                    }
                }
            }
        }
        if (cancelList.containsKey(SecondaryStat.VampDeath)) {
            this.addHP(-this.getStat().getCurrentMaxHp());
        }
        if (cancelList.containsKey(SecondaryStat.Lapidification) && autocancel && skilllv == 999) {
            this.giveDebuff(SecondaryStat.Stun, MobSkillFactory.getMobSkill(123, 57));
        }
    }

    public void dispelDebuff(Map.Entry<SecondaryStat, MapleDiseases> d) {
        if (this.hasDisease(d.getKey())) {
            this.cancelDisease(d.getKey());
        }
    }

    public void dispelDebuffs() {
        HashMap<SecondaryStat, Pair<Integer, Integer>> statupz = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        for (Map.Entry<SecondaryStat, MapleDiseases> d : this.getDiseases().entrySet()) {
            this.dispelDebuff(d);
            statupz.put(d.getKey(), new Pair<Integer, Integer>(d.getValue().getValue(), d.getValue().getDuration()));
        }
        this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(statupz, this));
        this.map.broadcastMessage(this, CWvsContext.BuffPacket.cancelForeignBuff(this, statupz), false);
        this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this));
    }

    public void dispelDebuffs(MapleCharacter owner, int skillid) {
        HashMap<SecondaryStat, Pair<Integer, Integer>> statupz = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        for (Map.Entry<SecondaryStat, MapleDiseases> d : this.getDiseases().entrySet()) {
            if (skillid == 2001556) {
                switch (d.getKey()) {
                    case Seal:
                    case Curse:
                    case Poison:
                    case Darkness:
                    case Weakness: {
                        this.dispelDebuff(d);
                        statupz.put(d.getKey(), new Pair<Integer, Integer>(d.getValue().getValue(), d.getValue().getDuration()));
                    }
                }
                continue;
            }
            this.dispelDebuff(d);
            statupz.put(d.getKey(), new Pair<Integer, Integer>(d.getValue().getValue(), d.getValue().getDuration()));
        }
        if (skillid == 2311001 && statupz.size() > 0) {
            SecondaryStatEffect effect = SkillFactory.getSkill(2311001).getEffect(owner.getSkillLevel(2311001));
            if (owner.skillisCooling(2311001)) {
                owner.changeCooldown(2311001, -(effect.getY() * 1000));
            }
            if (owner.skillisCooling(2311012)) {
                owner.changeCooldown(2311012, -effect.getDuration());
            }
        }
        if (!statupz.isEmpty()) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.cancelBuff(statupz, this));
            this.map.broadcastMessage(this, CWvsContext.BuffPacket.cancelForeignBuff(this, statupz), false);
            this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this));
        }
    }

    public void cancelAllDebuffs() {
        this.getDiseases().clear();
    }

    public void setLevel(final short level) {
        this.level = (short) (level - 1);
    }

    public void sendNote(final String to, final String msg, final int type, final int senderid) {
        this.sendNote(to, msg, 0, type, senderid);
    }

    public void sendNote(final String to, final String msg, final int fame, final int type, final int senderid) {
        MapleCharacterUtil.sendNote(to, this.getName(), msg, fame, type, senderid);
    }

    public void sendNote(final String to, final String from, final String msg, final int fame, final int type, final int senderid) {
        MapleCharacterUtil.sendNote(to, from, msg, fame, type, senderid);
    }

    public void showNote() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM notes WHERE `to`= ? AND `type` = 6", 1005, 1008);
            ps.setString(1, this.getName());
            rs = ps.executeQuery();
            rs.last();
            final int count = rs.getRow();
            rs.first();
            this.client.getSession().writeAndFlush((Object) CSPacket.showNotes(rs, count));
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Unable to show note" + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
        this.showsendNote();
    }

    public void showsendNote() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM `notes` WHERE `from`= ? AND `type` = 7", 1005, 1008);
            ps.setString(1, this.getName());
            rs = ps.executeQuery();
            rs.last();
            final int count = rs.getRow();
            rs.first();
            this.client.getSession().writeAndFlush((Object) CSPacket.showsendNotes(this, rs, count));
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Unable to show note" + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void deleteNote(final int id, final int fame) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT gift FROM notes WHERE `id`=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt("gift") == fame && fame > 0) {
                this.addFame(fame);
                this.updateSingleStat(MapleStat.FAME, this.getFame());
                this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowFameGain(fame));
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("DELETE FROM notes WHERE `id`=?");
            ps.setInt(1, id);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Unable to delete note" + e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public int getMulungEnergy() {
        return this.mulung_energy;
    }

    public final short getCombo() {
        return this.combo;
    }

    public void setCombo(final short combo) {
        this.combo = combo;
    }

    public final long getLastCombo() {
        return this.lastCombo;
    }

    public void setLastCombo(final long combo) {
        this.lastCombo = combo;
    }

    public final boolean getUseTruthDoor() {
        return this.useTruthDoor;
    }

    public void setUseTruthDoor(final boolean used) {
        this.useTruthDoor = used;
    }

    public final long getKeyDownSkill_Time() {
        return this.keydown_skill;
    }

    public void setKeyDownSkill_Time(final long keydown_skill) {
        this.keydown_skill = keydown_skill;
    }

    public void setChalkboard(final String text) {
        this.chalktext = text;
        if (this.map != null) {
            this.map.broadcastMessage(CSPacket.useChalkboard(this.getId(), text));
        }
    }

    public String getChalkboard() {
        return this.chalktext;
    }

    public MapleMount getMount() {
        return this.mount;
    }

    public int[] getWishlist() {
        return this.wishlist;
    }

    public void clearWishlist() {
        for (int i = 0; i < 12; ++i) {
            this.wishlist[i] = 0;
        }
        this.changed_wishlist = true;
    }

    public int getWishlistSize() {
        int ret = 0;
        for (int i = 0; i < 12; ++i) {
            if (this.wishlist[i] > 0) {
                ++ret;
            }
        }
        return ret;
    }

    public void setWishlist(final int[] wl) {
        this.wishlist = wl;
        this.changed_wishlist = true;
    }

    public int[] getRocks() {
        return this.rocks;
    }

    public int getRockSize() {
        int ret = 0;
        for (int i = 0; i < 10; ++i) {
            if (this.rocks[i] != 999999999) {
                ++ret;
            }
        }
        return ret;
    }

    public void deleteFromRocks(final int map) {
        for (int i = 0; i < 10; ++i) {
            if (this.rocks[i] == map) {
                this.rocks[i] = 999999999;
                this.changed_trocklocations = true;
                break;
            }
        }
    }

    public void addRockMap() {
        if (this.getRockSize() >= 10) {
            return;
        }
        this.rocks[this.getRockSize()] = this.getMapId();
        this.changed_trocklocations = true;
    }

    public boolean isRockMap(final int id) {
        for (int i = 0; i < 10; ++i) {
            if (this.rocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getRegRocks() {
        return this.regrocks;
    }

    public int getRegRockSize() {
        int ret = 0;
        for (int i = 0; i < 5; ++i) {
            if (this.regrocks[i] != 999999999) {
                ++ret;
            }
        }
        return ret;
    }

    public void deleteFromRegRocks(final int map) {
        for (int i = 0; i < 5; ++i) {
            if (this.regrocks[i] == map) {
                this.regrocks[i] = 999999999;
                this.changed_regrocklocations = true;
                break;
            }
        }
    }

    public void addRegRockMap() {
        if (this.getRegRockSize() >= 5) {
            return;
        }
        this.regrocks[this.getRegRockSize()] = this.getMapId();
        this.changed_regrocklocations = true;
    }

    public boolean isRegRockMap(final int id) {
        for (int i = 0; i < 5; ++i) {
            if (this.regrocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getHyperRocks() {
        return this.hyperrocks;
    }

    public int getHyperRockSize() {
        int ret = 0;
        for (int i = 0; i < 13; ++i) {
            if (this.hyperrocks[i] != 999999999) {
                ++ret;
            }
        }
        return ret;
    }

    public void deleteFromHyperRocks(final int map) {
        for (int i = 0; i < 13; ++i) {
            if (this.hyperrocks[i] == map) {
                this.hyperrocks[i] = 999999999;
                this.changed_hyperrocklocations = true;
                break;
            }
        }
    }

    public void addHyperRockMap() {
        if (this.getRegRockSize() >= 13) {
            return;
        }
        this.hyperrocks[this.getHyperRockSize()] = this.getMapId();
        this.changed_hyperrocklocations = true;
    }

    public boolean isHyperRockMap(final int id) {
        for (int i = 0; i < 13; ++i) {
            if (this.hyperrocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public List<LifeMovementFragment> getLastRes() {
        return this.lastres;
    }

    public void setLastRes(final List<LifeMovementFragment> lastres) {
        this.lastres = lastres;
    }

    public void dropMessageGM(final int type, final String message) {
        if (this.isGM()) {
            this.dropMessage(type, message);
        }
    }

    public void dropMessage(final int type, final String message) {
        if (type == -1) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.getTopMsg(message));
        } else if (type == -2) {
            this.client.getSession().writeAndFlush((Object) PlayerShopPacket.shopChat(this, this.name, this.id, message, 0));
        } else if (type == -3) {
            this.client.getSession().writeAndFlush((Object) CField.getChatText(this, message, this.isSuperGM(), 0, null));
        } else if (type == -4) {
            this.client.getSession().writeAndFlush((Object) CField.getChatText(this, message, this.isSuperGM(), 1, null));
        } else if (type == -5) {
            this.client.getSession().writeAndFlush((Object) CField.getGameMessage(6, message));
        } else if (type == -6) {
            this.client.getSession().writeAndFlush((Object) CField.getGameMessage(11, message));
        } else if (type == -7) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.getMidMsg(message, 0));
        } else if (type == -8) {
            this.client.getSession().writeAndFlush((Object) CField.getGameMessage(8, message));
        } else {
            this.client.getSession().writeAndFlush((Object) CWvsContext.serverNotice(type, this.name, message));
        }
    }

    public IMaplePlayerShop getPlayerShop() {
        return this.playerShop;
    }

    public void setPlayerShop(final IMaplePlayerShop playerShop) {
        this.playerShop = playerShop;
    }

    public int getConversation() {
        return this.inst.get();
    }

    public void setConversation(final int inst) {
        this.inst.set(inst);
    }

    public int getDirection() {
        return this.insd.get();
    }

    public void setDirection(final int inst) {
        this.insd.set(inst);
    }

    public void addCP(final int ammount) {
        this.totalCP += (short) ammount;
        this.availableCP += (short) ammount;
    }

    public void useCP(final int ammount) {
        this.availableCP -= (short) ammount;
    }

    public int getAvailableCP() {
        return this.availableCP;
    }

    public int getTotalCP() {
        return this.totalCP;
    }

    public void resetCP() {
        this.totalCP = 0;
        this.availableCP = 0;
    }

    public boolean getCanTalk() {
        return this.canTalk;
    }

    public void canTalk(final boolean talk) {
        this.canTalk = talk;
    }

    public double getEXPMod() {
        return this.stats.expMod;
    }

    public int getDropMod() {
        return this.stats.dropMod;
    }

    public int getCashMod() {
        return this.stats.cashMod;
    }

    public void setPoints(final int p) {
        this.points = p;
    }

    public int getPoints() {
        return this.points;
    }

    public void setVPoints(final int p) {
        this.vpoints = p;
    }

    public int getVPoints() {
        return this.vpoints;
    }

    public void gainVPoints(final int vpoints) {
        this.vpoints += vpoints;
    }

    public CashShop getCashInventory() {
        return this.cs;
    }

    public void removeItem(final int id, final int quantity) {
        MapleInventoryManipulator.removeById(this.client, GameConstants.getInventoryType(id), id, -quantity, true, false);
        this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) quantity, true));
    }

    public void removeAll(final int id) {
        this.removeAll(id, true);
    }

    public void removeAll(final int id, final boolean show) {
        MapleInventoryType type = GameConstants.getInventoryType(id);
        int possessed = this.getInventory(type).countById(id);
        if (possessed > 0) {
            MapleInventoryManipulator.removeById(this.getClient(), type, id, possessed, true, false);
            if (show) {
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) (-possessed), true));
            }
        }
        if (type == MapleInventoryType.EQUIP) {
            type = MapleInventoryType.EQUIPPED;
            possessed = this.getInventory(type).countById(id);
            if (possessed > 0) {
                MapleInventoryManipulator.removeById(this.getClient(), type, id, possessed, true, false);
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) (-possessed), true));
            }
        }
    }

    public Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> getRings(final boolean equip) {
        MapleInventory iv = this.getInventory(MapleInventoryType.EQUIPPED);
        final List<Item> equipped = iv.newList();
        Collections.sort(equipped);
        final List<MapleRing> crings = new ArrayList<MapleRing>();
        final List<MapleRing> frings = new ArrayList<MapleRing>();
        final List<MapleRing> mrings = new ArrayList<MapleRing>();
        for (final Item ite : equipped) {
            final Equip item = (Equip) ite;
            if (item.getRing() != null) {
                final MapleRing ring = item.getRing();
                ring.setEquipped(true);
                if (!GameConstants.isEffectRing(item.getItemId())) {
                    continue;
                }
                if (equip) {
                    if (GameConstants.isCrushRing(item.getItemId())) {
                        crings.add(ring);
                    } else if (GameConstants.isFriendshipRing(item.getItemId())) {
                        frings.add(ring);
                    } else {
                        if (!GameConstants.isMarriageRing(item.getItemId())) {
                            continue;
                        }
                        mrings.add(ring);
                    }
                } else if (crings.size() == 0 && GameConstants.isCrushRing(item.getItemId())) {
                    crings.add(ring);
                } else if (frings.size() == 0 && GameConstants.isFriendshipRing(item.getItemId())) {
                    frings.add(ring);
                } else {
                    if (mrings.size() != 0 || !GameConstants.isMarriageRing(item.getItemId()) || this.getMarriageId() <= 0) {
                        continue;
                    }
                    mrings.add(ring);
                }
            }
        }
        if (equip) {
            iv = this.getInventory(MapleInventoryType.EQUIP);
            for (final Item ite : iv.list()) {
                final Equip item = (Equip) ite;
                if (item.getRing() != null) {
                    final MapleRing ring = item.getRing();
                    ring.setEquipped(false);
                    if (GameConstants.isFriendshipRing(item.getItemId())) {
                        frings.add(ring);
                    } else if (GameConstants.isCrushRing(item.getItemId())) {
                        crings.add(ring);
                    } else {
                        if (!GameConstants.isMarriageRing(item.getItemId()) || this.getMarriageId() <= 0) {
                            continue;
                        }
                        mrings.add(ring);
                    }
                }
            }
        }
        Collections.sort(frings, new MapleRing.RingComparator());
        Collections.sort(crings, new MapleRing.RingComparator());
        Collections.sort(mrings, new MapleRing.RingComparator());
        return new Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>>(crings, frings, mrings);
    }

    public int getFH() {
        final MapleFoothold fh = this.getMap().getFootholds().findBelow(this.getTruePosition());
        if (fh != null) {
            return fh.getId();
        }
        return 0;
    }

    public void startFairySchedule(final boolean exp) {
        this.startFairySchedule(exp, false);
    }

    public void startFairySchedule(final boolean exp, final boolean equipped) {
        this.cancelFairySchedule(exp || this.stats.equippedFairy == 0);
        if (this.fairyExp <= 0) {
            this.fairyExp = (byte) this.stats.equippedFairy;
        }
        if (equipped && this.fairyExp < this.stats.equippedFairy * 3 && this.stats.equippedFairy > 0) {
            this.dropMessage(5, "\uc815\ub839\uc758 \ud39c\ub358\ud2b8\ub97c \ucc29\uc6a9\ud55c\uc9c0 1\uc2dc\uac04\uc774 \uc9c0\ub098 " + (this.fairyExp + this.stats.equippedFairy) + "%\uc758 \ucd94\uac00 \uacbd\ud5d8\uce58\ub97c \ud68d\ub4dd\ud569\ub2c8\ub2e4.");
        }
        this.lastFairyTime = System.currentTimeMillis();
    }

    public final boolean canFairy(final long now) {
        return this.lastFairyTime > 0L && this.lastFairyTime + 3600000L < now;
    }

    public final boolean canHP(final long now) {
        if (this.lastHPTime + 5000L < now) {
            this.lastHPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canMP(final long now) {
        if (this.lastMPTime + 5000L < now) {
            this.lastMPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canHPRecover(final long now) {
        if (this.stats.hpRecoverTime > 0 && this.lastHPTime + this.stats.hpRecoverTime < now) {
            this.lastHPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canMPRecover(final long now) {
        if (this.stats.mpRecoverTime > 0 && this.lastMPTime + this.stats.mpRecoverTime < now) {
            this.lastMPTime = now;
            return true;
        }
        return false;
    }

    public void cancelFairySchedule(final boolean exp) {
        this.lastFairyTime = 0L;
        if (exp) {
            this.fairyExp = 0;
        }
    }

    public void doFairy() {
        if (this.fairyExp < this.stats.equippedFairy * 3 && this.stats.equippedFairy > 0) {
            this.fairyExp += (byte) this.stats.equippedFairy;
            this.dropMessage(5, "\uc815\ub839\uc758 \ud39c\ub358\ud2b8\ub97c \ud1b5\ud574 " + this.fairyExp + "%\uc758 \ucd94\uac00\uacbd\ud5d8\uce58\ub97c \ud68d\ub4dd\ud569\ub2c8\ub2e4.");
        }
        this.traits.get(MapleTrait.MapleTraitType.will).addExp(5, this);
        this.startFairySchedule(false, true);
    }

    public byte getFairyExp() {
        return this.fairyExp;
    }

    public int getTeam() {
        return this.coconutteam;
    }

    public void setTeam(final int v) {
        this.coconutteam = v;
    }

    public void clearLinkMid() {
        this.linkMobs.clear();
        this.cancelEffectFromBuffStat(SecondaryStat.ArcaneAim);
    }

    public int getFirstLinkMid() {
        final Iterator<Integer> iterator = this.linkMobs.keySet().iterator();
        if (iterator.hasNext()) {
            final Integer lm = iterator.next();
            return lm;
        }
        return 0;
    }

    public Map<Integer, Integer> getAllLinkMid() {
        return this.linkMobs;
    }

    public void setLinkMid(final int lm, final int x) {
        this.linkMobs.put(lm, x);
    }

    public int getDamageIncrease(final int lm) {
        if (this.linkMobs.containsKey(lm)) {
            return this.linkMobs.get(lm);
        }
        return 0;
    }

    public void setDragon(final MapleDragon d) {
        this.dragon = d;
    }

    public MapleExtractor getExtractor() {
        return this.extractor;
    }

    public void setExtractor(final MapleExtractor me) {
        this.removeExtractor();
        this.extractor = me;
    }

    public void removeExtractor() {
        if (this.extractor != null) {
            this.map.broadcastMessage(CField.removeExtractor(this.id));
            this.map.removeMapObject(this.extractor);
            this.extractor = null;
        }
    }

    public void setBlackMageWB(final int v) {
        this.blackmagewb = v;
    }

    public int getBlackMageWB() {
        return this.blackmagewb;
    }

    public void giveBlackMageBuff() {
        this.getClient().send(CField.getSelectPower(5, 39));
        this.setSkillCustomInfo(80002625, 1L, 0L);
        SkillFactory.getSkill(80002625).getEffect(1).applyTo(this);
    }

    public void resetStatsN(final int str, final int dex, final int int_, final int luk) {
        final Map<MapleStat, Long> stat = new EnumMap<MapleStat, Long>(MapleStat.class);
        this.stats.str = (short) str;
        this.stats.dex = (short) dex;
        this.stats.int_ = (short) int_;
        this.stats.luk = (short) luk;
        this.stats.recalcLocalStats(this);
        stat.put(MapleStat.STR, (long) str);
        stat.put(MapleStat.DEX, (long) dex);
        stat.put(MapleStat.INT, (long) int_);
        stat.put(MapleStat.LUK, (long) luk);
        this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(stat, false, this));
    }

    public void resetStats(final int str, final int dex, final int int_, final int luk) {
        final Map<MapleStat, Long> stat = new EnumMap<MapleStat, Long>(MapleStat.class);
        this.stats.recalcLocalStats(this);
        this.stats.str = 4;
        this.stats.dex = 4;
        this.stats.int_ = 4;
        this.stats.luk = 4;
        stat.put(MapleStat.STR, (long) str);
        stat.put(MapleStat.DEX, (long) dex);
        stat.put(MapleStat.INT, (long) int_);
        stat.put(MapleStat.LUK, (long) luk);
        stat.put(MapleStat.AVAILABLEAP, this.getLevel() * 5L + 18L);
        this.setRemainingAp((short) (this.getLevel() * 5 + 18));
        this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(stat, false, this));
    }

    public void resetStatDonation(final int tf, final int tft) {
        final Map<MapleStat, Long> stat = new EnumMap<MapleStat, Long>(MapleStat.class);
        int total = this.stats.getStr() + this.stats.getDex() + this.stats.getLuk() + this.stats.getInt() + this.getRemainingAp();
        int tstat = (tft == 1) ? (tf * 200) : ((tft == 2) ? (tf * 100) : 4);
        if (tft == 2 && tf == 6) {
            tstat += 100;
        }
        total -= tstat;
        this.stats.str = (short) tstat;
        total -= tstat;
        this.stats.dex = (short) tstat;
        total -= tstat;
        this.stats.int_ = (short) tstat;
        total -= tstat;
        this.stats.luk = (short) tstat;
        this.setRemainingAp((short) total);
        this.stats.recalcLocalStats(this);
        stat.put(MapleStat.STR, (long) tstat);
        stat.put(MapleStat.DEX, (long) tstat);
        stat.put(MapleStat.INT, (long) tstat);
        stat.put(MapleStat.LUK, (long) tstat);
        stat.put(MapleStat.AVAILABLEAP, (long) total);
        this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(stat, false, this));
    }

    public void resetStatsDV() {
        final Map<MapleStat, Long> stat = new EnumMap<MapleStat, Long>(MapleStat.class);
        final int apss = (this.getLevel() - 10) * 5 * 15;
        final int total = (this.getLevel() - 10) * 5 + this.getRemainingAp();
        this.stats.setMaxHp(this.getStat().getMaxHp() - apss, this);
        this.setRemainingAp((short) total);
        this.stats.recalcLocalStats(this);
        stat.put(MapleStat.MAXHP, this.getStat().getMaxHp());
        stat.put(MapleStat.AVAILABLEAP, (long) total);
        this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(stat, false, this));
    }

    public byte getSubcategory() {
        if (this.job >= 430 && this.job <= 434) {
            return 1;
        }
        if (GameConstants.isCannon(this.job)) {
            return 2;
        }
        if (this.job != 0 && this.job != 400) {
            return 0;
        }
        return this.subcategory;
    }

    public void setSubcategory(final int z) {
        this.subcategory = (byte) z;
    }

    public int itemQuantity(final int itemid) {
        return this.getInventory(GameConstants.getInventoryType(itemid)).countById(itemid);
    }

    public long getNextConsume() {
        return this.nextConsume;
    }

    public void setNextConsume(final long nc) {
        this.nextConsume = nc;
    }

    public int getRank() {
        return this.rank;
    }

    public int getRankMove() {
        return this.rankMove;
    }

    public int getJobRank() {
        return this.jobRank;
    }

    public int getJobRankMove() {
        return this.jobRankMove;
    }

    public void changeChannelMap(final int channel, final int map) {
        final ChannelServer toch = ChannelServer.getInstance(channel);
        if (channel == this.client.getChannel() || toch == null || toch.isShutdown()) {
            return;
        }
        this.changeRemoval();
        final ChannelServer ch = ChannelServer.getInstance(this.client.getChannel());
        if (this.getMessenger() != null) {
            World.Messenger.silentLeaveMessenger(this.getMessenger().getId(), new MapleMessengerCharacter(this));
        }
        PlayerBuffStorage.addBuffsToStorage(this.getId(), this.getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(this.getId(), this.getCooldowns());
        this.getMap().removePlayer(this);
        this.map = toch.getMapFactory().getMap(map);
        World.ChannelChange_Data(new CharacterTransfer(this), this.getId(), channel);
        ch.removePlayer(this);
        this.client.updateLoginState(3, this.client.getSessionIPAddress());
        final String s = this.client.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf(47) + 1, s.length()));
        this.client.getSession().writeAndFlush((Object) CField.getChannelChange(this.client, Integer.parseInt(toch.getIP().split(":")[1])));
        this.saveToDB(true, false);
        this.client.setPlayer(null);
        if (OneCardGame.oneCardMatchingQueue.contains(this)) {
            OneCardGame.oneCardMatchingQueue.remove(this);
        }
        if (BattleReverse.BattleReverseMatchingQueue.contains(this)) {
            BattleReverse.BattleReverseMatchingQueue.remove(this);
        }
    }

    public void changeChannel(final int channel) {
        final ChannelServer toch = ChannelServer.getInstance(channel);
        if (channel == this.client.getChannel() || toch == null || toch.isShutdown()) {
            return;
        }
        this.changeRemoval();
        final ChannelServer ch = ChannelServer.getInstance(this.client.getChannel());
        if (this.getMessenger() != null) {
            World.Messenger.silentLeaveMessenger(this.getMessenger().getId(), new MapleMessengerCharacter(this));
        }
        if (this.getBuffedValue(SecondaryStat.EventSpecialSkill) != null) {
            this.cancelEffect(this.getBuffedEffect(SecondaryStat.EventSpecialSkill));
        }
        PlayerBuffStorage.addBuffsToStorage(this.getId(), this.getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(this.getId(), this.getCooldowns());
        World.ChannelChange_Data(new CharacterTransfer(this), this.getId(), channel);
        ch.removePlayer(this);
        this.client.setChannel(channel);
        this.client.updateLoginState(3, this.client.getSessionIPAddress());
        final String s = this.client.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf(47) + 1, s.length()));
        this.client.getSession().writeAndFlush((Object) CField.getChannelChange(this.client, Integer.parseInt(toch.getIP().split(":")[1])));
        this.saveToDB(true, false);
        this.getMap().removePlayer(this);
        this.client.setPlayer(null);
        if (OneCardGame.oneCardMatchingQueue.contains(this)) {
            OneCardGame.oneCardMatchingQueue.remove(this);
        }
        if (BattleReverse.BattleReverseMatchingQueue.contains(this)) {
            BattleReverse.BattleReverseMatchingQueue.remove(this);
        }
    }

    public void expandInventory(final byte type, final int amount) {
        final MapleInventory inv = this.getInventory(MapleInventoryType.getByType(type));
        inv.addSlot((byte) amount);
        this.client.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getSlotUpdate(type, (byte) inv.getSlotLimit()));
    }

    public boolean allowedToTarget(final MapleCharacter other) {
        return other != null && (!other.isHidden() || this.getGMLevel() >= other.getGMLevel());
    }

    public int getFollowId() {
        return this.followid;
    }

    public void setFollowId(final int fi) {
        this.followid = fi;
        if (fi == 0) {
            this.followinitiator = false;
            this.followon = false;
        }
    }

    public void setFollowInitiator(final boolean fi) {
        this.followinitiator = fi;
    }

    public void setFollowOn(final boolean fi) {
        this.followon = fi;
    }

    public boolean isFollowOn() {
        return this.followon;
    }

    public boolean isFollowInitiator() {
        return this.followinitiator;
    }

    public void checkFollow() {
        if (this.followid <= 0) {
            return;
        }
        if (this.followon) {
            this.map.broadcastMessage(CField.followEffect(this.id, 0, null));
            this.map.broadcastMessage(CField.followEffect(this.followid, 0, null));
        }
        final MapleCharacter target = this.map.getCharacter(this.followid);
        this.client.getSession().writeAndFlush((Object) CField.getGameMessage(11, "\ub530\ub77c\uac00\uae30\uac00 \ud574\uc81c\ub418\uc5c8\uc2b5\ub2c8\ub2e4."));
        if (target != null) {
            target.setFollowId(0);
            target.getClient().getSession().writeAndFlush((Object) CField.getGameMessage(11, "\ub530\ub77c\uac00\uae30\uac00 \ud574\uc81c\ub418\uc5c8\uc2b5\ub2c8\ub2e4."));
            this.setFollowId(0);
        }
    }

    public int getMarriageId() {
        return this.marriageId;
    }

    public void setMarriageId(final int mi) {
        this.marriageId = mi;
    }

    public int getMarriageItemId() {
        return this.marriageItemId;
    }

    public void setMarriageItemId(final int mi) {
        this.marriageItemId = mi;
    }

    public boolean isStaff() {
        return this.gmLevel >= ServerConstants.PlayerGMRank.INTERN.getLevel();
    }

    public boolean isDonator() {
        return this.gmLevel >= ServerConstants.PlayerGMRank.DONATOR.getLevel();
    }

    public boolean startPartyQuest(final int questid) {
        boolean ret = false;
        final MapleQuest q = MapleQuest.getInstance(questid);
        if (q == null || !q.isPartyQuest()) {
            return false;
        }
        if (!this.quests.containsKey(q) || !this.questinfo.containsKey(questid)) {
            final MapleQuestStatus status = this.getQuestNAdd(q);
            status.setStatus((byte) 1);
            this.updateQuest(status);
            switch (questid) {
                case 1300:
                case 1301:
                case 1302: {
                    this.updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0;gvup=0;vic=0;lose=0;draw=0");
                    break;
                }
                case 1303: {
                    this.updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;have1=0;rank=F;try=0;cmp=0;CR=0;VR=0;vic=0;lose=0");
                    break;
                }
                case 1204: {
                    this.updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;have2=0;have3=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                }
                case 1206: {
                    this.updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                }
                default: {
                    this.updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                }
            }
            ret = true;
        }
        return ret;
    }

    public String getOneInfo(final int questid, final String key) {
        if (!this.questinfo.containsKey(questid) || key == null || MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return null;
        }
        final String[] split3;
        final String[] split = split3 = this.questinfo.get(questid).split(";");
        for (final String x : split3) {
            final String[] split2 = x.split("=");
            if (split2.length == 2 && split2[0].equals(key)) {
                return split2[1];
            }
        }
        return null;
    }

    public void updateOneInfo(final int questid, final String key, final String value) {
        if (!this.questinfo.containsKey(questid) || key == null || value == null || MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        final String[] split = this.questinfo.get(questid).split(";");
        boolean changed = false;
        final StringBuilder newQuest = new StringBuilder();
        for (final String x : split) {
            final String[] split2 = x.split("=");
            if (split2.length == 2) {
                if (split2[0].equals(key)) {
                    newQuest.append(key).append("=").append(value);
                } else {
                    newQuest.append(x);
                }
                newQuest.append(";");
                changed = true;
            }
        }
        this.updateInfoQuest(questid, changed ? newQuest.toString().substring(0, newQuest.toString().length() - 1) : newQuest.toString());
    }

    public void updateSkillPacket() {
        this.client.getSession().writeAndFlush((Object) CWvsContext.updateSkills(this.getSkills()));
    }

    public void updateLinkSkillPacket() {
        this.changeSingleSkillLevel(SkillFactory.getSkill(80000055), 0, (byte) 10);
        this.changeSingleSkillLevel(SkillFactory.getSkill(80000329), 0, (byte) 8);
        this.changeSingleSkillLevel(SkillFactory.getSkill(80002758), 0, (byte) 6);
        this.changeSingleSkillLevel(SkillFactory.getSkill(80002762), 0, (byte) 6);
        this.changeSingleSkillLevel(SkillFactory.getSkill(80002766), 0, (byte) 6);
        this.changeSingleSkillLevel(SkillFactory.getSkill(80002770), 0, (byte) 6);
        this.changeSingleSkillLevel(SkillFactory.getSkill(80002774), 0, (byte) 6);
        final List<Triple<Skill, SkillEntry, Integer>> skills = this.getLinkSkills();
        for (final Triple<Skill, SkillEntry, Integer> linkskil : skills) {
            if (this.getSkillLevel(linkskil.getLeft().getId()) != 0) {
                int totalskilllv = 0;
                if (linkskil.getLeft().getId() >= 80000066 && linkskil.getLeft().getId() <= 80000070) {
                    totalskilllv = linkskil.getMid().skillevel + this.getSkillLevel(80000055);
                    this.changeSingleSkillLevel(SkillFactory.getSkill(80000055), totalskilllv, (byte) 10);
                }
                if ((linkskil.getLeft().getId() >= 80000333 && linkskil.getLeft().getId() <= 80000335) || linkskil.getLeft().getId() == 80000378) {
                    totalskilllv = linkskil.getMid().skillevel + this.getSkillLevel(80000329);
                    this.changeSingleSkillLevel(SkillFactory.getSkill(80000329), totalskilllv, (byte) 8);
                }
                if (linkskil.getLeft().getId() >= 80002759 && linkskil.getLeft().getId() <= 80002761) {
                    totalskilllv = linkskil.getMid().skillevel + this.getSkillLevel(80002758);
                    this.changeSingleSkillLevel(SkillFactory.getSkill(80002758), totalskilllv, (byte) 6);
                }
                if (linkskil.getLeft().getId() >= 80002763 && linkskil.getLeft().getId() <= 80002765) {
                    totalskilllv = linkskil.getMid().skillevel + this.getSkillLevel(80002762);
                    this.changeSingleSkillLevel(SkillFactory.getSkill(80002762), totalskilllv, (byte) 6);
                }
                if (linkskil.getLeft().getId() >= 80002767 && linkskil.getLeft().getId() <= 80002769) {
                    totalskilllv = linkskil.getMid().skillevel + this.getSkillLevel(80002766);
                    this.changeSingleSkillLevel(SkillFactory.getSkill(80002766), totalskilllv, (byte) 6);
                }
                if (linkskil.getLeft().getId() >= 80002771 && linkskil.getLeft().getId() <= 80002773) {
                    totalskilllv = linkskil.getMid().skillevel + this.getSkillLevel(80002770);
                    this.changeSingleSkillLevel(SkillFactory.getSkill(80002770), totalskilllv, (byte) 6);
                }
                if ((linkskil.getLeft().getId() >= 80002775 && linkskil.getLeft().getId() <= 80002776) || linkskil.getLeft().getId() == 80000000) {
                    totalskilllv = linkskil.getMid().skillevel + this.getSkillLevel(80002774);
                    this.changeSingleSkillLevel(SkillFactory.getSkill(80002774), totalskilllv, (byte) 6);
                }
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.Linkskill(linkskil.getLeft().getId(), linkskil.getRight(), this.getId(), linkskil.getMid().skillevel, totalskilllv));
            }
        }
    }

    public void recalcPartyQuestRank(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        if (!this.startPartyQuest(questid)) {
            final String oldRank = this.getOneInfo(questid, "rank");
            if (oldRank == null || oldRank.equals("S")) {
                return;
            }
            String newRank = null;
            if (oldRank.equals("A")) {
                newRank = "S";
            } else if (oldRank.equals("B")) {
                newRank = "A";
            } else if (oldRank.equals("C")) {
                newRank = "B";
            } else if (oldRank.equals("D")) {
                newRank = "C";
            } else {
                if (!oldRank.equals("F")) {
                    return;
                }
                newRank = "D";
            }
            final List<Pair<String, Pair<String, Integer>>> questInfo = MapleQuest.getInstance(questid).getInfoByRank(newRank);
            if (questInfo == null) {
                return;
            }
            for (final Pair<String, Pair<String, Integer>> q : questInfo) {
                boolean found = false;
                final String val = this.getOneInfo(questid, q.right.left);
                if (val == null) {
                    return;
                }
                int vall = 0;
                try {
                    vall = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return;
                }
                if (q.left.equals("less")) {
                    found = (vall < q.right.right);
                } else if (q.left.equals("more")) {
                    found = (vall > q.right.right);
                } else if (q.left.equals("equal")) {
                    found = (vall == q.right.right);
                }
                if (!found) {
                    return;
                }
            }
            this.updateOneInfo(questid, "rank", newRank);
        }
    }

    public int getIgnition() {
        return this.ignitionstack;
    }

    public void setIgnition(final int stack) {
        if (stack > 0 && this.skillisCooling(400021042)) {
            return;
        }
        this.ignitionstack = stack;
        final SecondaryStatEffect effect = SkillFactory.getSkill(12101024).getEffect(this.getTotalSkillLevel(12101024));
        final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
        statups.put(SecondaryStat.Ember, new Pair<Integer, Integer>(1, (int) this.getBuffLimit(12101024)));
        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
    }

    public void gainIgnition() {
        if (this.skillisCooling(400021042)) {
            return;
        }
        if (this.ignitionstack < 6) {
            ++this.ignitionstack;
        }
        final SecondaryStatEffect effect = SkillFactory.getSkill(12101024).getEffect(this.getTotalSkillLevel(12101024));
        final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
        statups.put(SecondaryStat.Ember, new Pair<Integer, Integer>(1, (int) this.getBuffLimit(12101024)));
        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
    }

    public void gainfamilier() {
        if (this.ignitionstack < 6) {
            ++this.ignitionstack;
        }
        final SecondaryStatEffect effect = SkillFactory.getSkill(12101024).getEffect(this.getTotalSkillLevel(12101024));
        final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
        statups.put(SecondaryStat.Ember, new Pair<Integer, Integer>(1, (int) this.getBuffLimit(12101024)));
        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
    }

    public void tryPartyQuest(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        try {
            this.startPartyQuest(questid);
            this.pqStartTime = System.currentTimeMillis();
            this.updateOneInfo(questid, "try", String.valueOf(Integer.parseInt(this.getOneInfo(questid, "try")) + 1));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("tryPartyQuest error");
        }
    }

    public void endPartyQuest(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        try {
            this.startPartyQuest(questid);
            if (this.pqStartTime > 0L) {
                final long changeTime = System.currentTimeMillis() - this.pqStartTime;
                final int mins = (int) (changeTime / 1000L / 60L);
                final int secs = (int) (changeTime / 1000L % 60L);
                final int mins2 = Integer.parseInt(this.getOneInfo(questid, "min"));
                if (mins2 <= 0 || mins < mins2) {
                    this.updateOneInfo(questid, "min", String.valueOf(mins));
                    this.updateOneInfo(questid, "sec", String.valueOf(secs));
                    this.updateOneInfo(questid, "date", FileoutputUtil.CurrentReadable_Date());
                }
                final int newCmp = Integer.parseInt(this.getOneInfo(questid, "cmp")) + 1;
                this.updateOneInfo(questid, "cmp", String.valueOf(newCmp));
                this.updateOneInfo(questid, "CR", String.valueOf((int) Math.ceil(newCmp * 100.0 / Integer.parseInt(this.getOneInfo(questid, "try")))));
                this.recalcPartyQuestRank(questid);
                this.pqStartTime = 0L;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("endPartyQuest error");
        }
    }

    public void havePartyQuest(final int itemId) {
        int questid = 0;
        int index = -1;
        switch (itemId) {
            case 1002798: {
                questid = 1200;
                break;
            }
            case 1072369: {
                questid = 1201;
                break;
            }
            case 1022073: {
                questid = 1202;
                break;
            }
            case 1082232: {
                questid = 1203;
                break;
            }
            case 1002571:
            case 1002572:
            case 1002573:
            case 1002574: {
                questid = 1204;
                index = itemId - 1002571;
                break;
            }
            case 1102226: {
                questid = 1303;
                break;
            }
            case 1102227: {
                questid = 1303;
                index = 0;
                break;
            }
            case 1122010: {
                questid = 1205;
                break;
            }
            case 1032060:
            case 1032061: {
                questid = 1206;
                index = itemId - 1032060;
                break;
            }
            case 3010018: {
                questid = 1300;
                break;
            }
            case 1122007: {
                questid = 1301;
                break;
            }
            case 1122058: {
                questid = 1302;
                break;
            }
            default: {
                return;
            }
        }
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        this.startPartyQuest(questid);
        this.updateOneInfo(questid, "have" + ((index == -1) ? "" : Integer.valueOf(index)), "1");
    }

    public void resetStatsByJob(final boolean beginnerJob) {
        final int baseJob = beginnerJob ? (this.job % 1000) : (this.job % 1000 / 100 * 100);
        final boolean UA = this.getQuestNoAdd(MapleQuest.getInstance(111111)) != null;
        if (baseJob == 100) {
            this.resetStats(UA ? 4 : 35, 4, 4, 4);
        } else if (baseJob == 200) {
            this.resetStats(4, 4, UA ? 4 : 20, 4);
        } else if (baseJob == 300 || baseJob == 400) {
            this.resetStats(4, UA ? 4 : 25, 4, 4);
        } else if (baseJob == 500) {
            this.resetStats(4, UA ? 4 : 20, 4, 4);
        } else if (baseJob == 0) {
            this.resetStats(4, 4, 4, 4);
        }
    }

    public boolean hasSummon(final int sourceid) {
        for (final MapleSummon summon : this.summons) {
            if (summon.getSkill() == sourceid) {
                return true;
            }
        }
        return false;
    }

    public void removeDoor() {
        final MapleDoor door = this.getDoors().iterator().next();
        for (final MapleCharacter chr : door.getTarget().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleCharacter chr : door.getTown().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleDoor destroyDoor : this.getDoors()) {
            door.getTarget().removeMapObject(destroyDoor);
            door.getTown().removeMapObject(destroyDoor);
        }
        this.clearDoors();
    }

    public void removeMechDoor() {
        for (final MechDoor destroyDoor : this.getMechDoors()) {
            for (final MapleCharacter chr : this.getMap().getCharactersThreadsafe()) {
                destroyDoor.sendDestroyData(chr.getClient());
            }
            this.getMap().removeMapObject(destroyDoor);
        }
        this.clearMechDoors();
    }

    public void changeRemoval() {
        this.changeRemoval(false);
    }

    public void changeRemoval(final boolean dc) {
        this.dispelSummons();
        if (!dc) {
            this.cancelEffectFromBuffStat(SecondaryStat.Recovery);
        }
        if (this.playerShop != null && !dc) {
            this.playerShop.removeVisitor(this);
            if (this.playerShop.isOwner(this)) {
                this.playerShop.setOpen(true);
            }
        }
        if (!this.getDoors().isEmpty()) {
            this.removeDoor();
        }
        if (!this.getMechDoors().isEmpty()) {
            this.removeMechDoor();
        }
        NPCScriptManager.getInstance().dispose(this.client);
        this.cancelFairySchedule(false);
    }

    public String getTeleportName() {
        return this.teleportname;
    }

    public void setTeleportName(final String tname) {
        this.teleportname = tname;
    }

    public int maxBattleshipHP(final int skillid) {
        return this.getTotalSkillLevel(skillid) * 5000 + (this.getLevel() - 120) * 3000;
    }

    public int currentBattleshipHP() {
        return this.battleshipHP;
    }

    public void setBattleshipHP(final int v) {
        this.battleshipHP = v;
    }

    public void decreaseBattleshipHP() {
        --this.battleshipHP;
    }

    public boolean isInTownMap() {
        if (!this.getMap().isTown() || FieldLimitType.VipRock.check(this.getMap().getFieldLimit()) || this.getEventInstance() != null) {
            return false;
        }
        for (final int i : GameConstants.blockedMaps) {
            if (this.getMapId() == i) {
                return false;
            }
        }
        return true;
    }

    public void startPartySearch(final List<Integer> jobs, final int maxLevel, final int minLevel, final int membersNeeded) {
        for (final MapleCharacter chr : this.map.getCharacters()) {
            if (chr.getId() != this.id && chr.getParty() == null && chr.getLevel() >= minLevel && chr.getLevel() <= maxLevel && (jobs.isEmpty() || jobs.contains((int) chr.getJob())) && (this.isGM() || !chr.isGM())) {
                if (this.party == null || this.party.getMembers().size() >= 6 || this.party.getMembers().size() >= membersNeeded) {
                    break;
                }
                chr.setParty(this.party);
                World.Party.updateParty(this.party.getId(), PartyOperation.JOIN, new MaplePartyCharacter(chr));
                chr.receivePartyMemberHP();
                chr.updatePartyMemberHP();
            }
        }
    }

    public int getChallenge() {
        return this.challenge;
    }

    public void setChallenge(final int c) {
        this.challenge = c;
    }

    public short getFatigue() {
        return this.fatigue;
    }

    public void setFatigue(final int j) {
        this.fatigue = (short) Math.max(0, j);
        this.updateSingleStat(MapleStat.FATIGUE, this.fatigue);
    }

    public void updateDamageSkin() {
        this.client.getSession().writeAndFlush((Object) CWvsContext.updateDamageSkin(this));
    }

    public void fakeRelog() {
        this.client.getSession().writeAndFlush((Object) CField.getCharInfo(this));
        final MapleMap mapp = this.getMap();
        mapp.setCheckStates(false);
        mapp.removePlayer(this);
        mapp.addPlayer(this);
        mapp.setCheckStates(true);
    }

    public boolean canSummon() {
        return this.canSummon(5000);
    }

    public boolean canSummon(final int g) {
        if (this.lastSummonTime + g < System.currentTimeMillis()) {
            this.lastSummonTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public int getIntNoRecord(final int questID) {
        final MapleQuestStatus stat = this.getQuestNoAdd(MapleQuest.getInstance(questID));
        if (stat == null || stat.getCustomData() == null) {
            return 0;
        }
        return Integer.parseInt(stat.getCustomData());
    }

    public int getIntRecord(final int questID) {
        final MapleQuestStatus stat = this.getQuestNAdd(MapleQuest.getInstance(questID));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Integer.parseInt(stat.getCustomData());
    }

    public void updatePetAuto() {
        if (this.getIntNoRecord(122221) > 0) {
            this.client.getSession().writeAndFlush((Object) CField.petAutoHP(this.getIntRecord(122221)));
        }
        if (this.getIntNoRecord(122223) > 0) {
            this.client.getSession().writeAndFlush((Object) CField.petAutoMP(this.getIntRecord(122223)));
        }
        if (this.getKeyValue(9999, "skillid") == -1L) {
        this.setKeyValue(9999, "skillid" ,"0");
        }
        if (this.getKeyValue(9999, "skillid2") == -1L) {
        this.setKeyValue(9999, "skillid2" ,"0");
        }
        if (this.getKeyValue(9999, "skillid3") == -1L) {
        this.setKeyValue(9999, "skillid3" ,"0");
        }
        if (this.getKeyValue(9999, "skillid4") == -1L) {
        this.setKeyValue(9999, "skillid4" ,"0");
        }
        if (this.getKeyValue(9999, "skillid5") == -1L) {
        this.setKeyValue(9999, "skillid5" ,"0");
        }
        if (this.getKeyValue(9999, "skillid6") == -1L) {
        this.setKeyValue(9999, "skillid6" ,"0");
        }
        this.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(0, "0=" + this.getKeyValue(9999, "skillid") + ";1="+this.getKeyValue(9999, "skillid2")));
        this.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(1, "10=" + this.getKeyValue(9999, "skillid3") + ";11="+this.getKeyValue(9999, "skillid4")));
        this.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(2, "20=" + this.getKeyValue(9999, "skillid5") + ";21="+this.getKeyValue(9999, "skillid6")));
    }

    public void sendEnglishQuiz(final String msg) {
    }

    public void setChangeTime() {
        this.mapChangeTime = System.currentTimeMillis();
    }

    public long getChangeTime() {
        return this.mapChangeTime;
    }

    public short getScrolledPosition() {
        return this.scrolledPosition;
    }

    public void setScrolledPosition(final short s) {
        this.scrolledPosition = s;
    }

    public MapleTrait getTrait(final MapleTrait.MapleTraitType t) {
        return this.traits.get(t);
    }

    public void forceCompleteQuest(final int id) {
        MapleQuest.getInstance(id).forceComplete(this, 2007);
    }

    public List<Integer> getExtendedSlots() {
        return this.extendedSlots;
    }

    public int getExtendedSlot(final int index) {
        if (this.extendedSlots.size() <= index || index < 0) {
            return -1;
        }
        return this.extendedSlots.get(index);
    }

    public void changedExtended() {
        this.changed_extendedSlots = true;
    }

    public MapleAndroid getAndroid() {
        return this.android;
    }

    public void removeAndroid() {
        if (this.map != null) {
            this.map.broadcastMessage(CField.deactivateAndroid(this.id));
        }
        this.android = null;
    }

    public void setAndroid(final MapleAndroid and) {
        this.android = and;
        if (this.map != null && and != null) {
            this.android.setStance(0);
            this.android.setPos(this.getPosition());
            this.map.broadcastMessage(this, CField.spawnAndroid(this, this.android), true);
            this.map.broadcastMessage(this, CField.showAndroidEmotion(this.getId(), Randomizer.nextInt(17) + 1), true);
        } else if (this.map != null && and == null) {
            this.map.broadcastMessage(this, CField.deactivateAndroid(this.getId()), true);
        }
    }

    public void updateAndroid() {
        if (this.map != null && this.android != null) {
            this.map.broadcastMessage(this, CField.spawnAndroid(this, this.android), true);
        } else if (this.map != null && this.android == null) {
            this.map.broadcastMessage(this, CField.deactivateAndroid(this.getId()), true);
        }
    }

    public List<Item> getRebuy() {
        return this.rebuy;
    }

    public MapleImp[] getImps() {
        return this.imps;
    }

    public int getBattlePoints() {
        return this.pvpPoints;
    }

    public int getTotalBattleExp() {
        return this.pvpExp;
    }

    public void setBattlePoints(final int p) {
        if (p != this.pvpPoints) {
            this.client.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getBPMsg(p - this.pvpPoints));
            this.updateSingleStat(MapleStat.BATTLE_POINTS, p);
        }
        this.pvpPoints = p;
    }

    public void setTotalBattleExp(final int p) {
        final int previous = this.pvpExp;
        this.pvpExp = p;
        if (p != previous) {
            this.stats.recalcPVPRank(this);
            this.updateSingleStat(MapleStat.BATTLE_EXP, this.stats.pvpExp);
            this.updateSingleStat(MapleStat.BATTLE_RANK, this.stats.pvpRank);
        }
    }

    public boolean inPVP() {
        return this.eventInstance != null && this.eventInstance.getName().startsWith("PVP");
    }

    public void clearCooldowns(final List<MapleCoolDownValueHolder> cooldowns) {
        final Map<Integer, Integer> datas = new HashMap<Integer, Integer>();
        for (final MapleCoolDownValueHolder m : cooldowns) {
            final int skil = m.skillId;
            this.removeCooldown(skil);
            datas.put(skil, 0);
        }
        this.client.getSession().writeAndFlush((Object) CField.skillCooldown(datas));
    }

    public void clearAllCooldowns() {
        final Map<Integer, Integer> datas = new HashMap<Integer, Integer>();
        for (final MapleCoolDownValueHolder m : this.getCooldowns()) {
            final int skil = m.skillId;
            if (skil != 80002282) {
                this.removeCooldown(skil);
                datas.put(skil, 0);
            }
        }
        this.client.getSession().writeAndFlush((Object) CField.skillCooldown(datas));
    }

    public void clearAllCooldowns(final int skillid) {
        for (final MapleCoolDownValueHolder m : this.getCooldowns()) {
            final int skil = m.skillId;
            if (skil != skillid && skil != 80002282) {
                this.removeCooldown(skil);
                this.client.getSession().writeAndFlush((Object) CField.skillCooldown(skil, 0));
            }
        }
    }

    public void handleForceGain(final int oid, final int skillid) {
        if (this.getSkillCustomValue(31101002) == null) {
            this.handleForceGain(oid, skillid, 0);
        }
    }

    public void handleForceGain(final int oid, final int skillid, int extraForce) {
        int forceGain = 0;
        if ((skillid >= 31001006 && skillid <= 31001008) || skillid == 31000004 || (skillid >= 400011007 && skillid <= 400011009)) {
            if (this.getLevel() >= 30 && this.getLevel() < 60) {
                forceGain = 3;
            } else if (this.getLevel() >= 60 && this.getLevel() < 100) {
                forceGain = 4;
            } else if (this.getLevel() >= 100) {
                forceGain = 5;
            }
            if (extraForce > 0) {
                forceGain *= 2;
            }
            if (this.getSkillLevel(31110009) > 0 && Randomizer.isSuccess(75)) {
                forceGain += 6;
            }
        }
        if (skillid == 30010111) {
            forceGain = 3;
        } else if (skillid == 31110008) {
            forceGain = 5;
        } else if (skillid == 31121052) {
            forceGain = 50;
            extraForce = 1;
        }
        if ((skillid == 400011077 || skillid == 400011078) && extraForce > 0) {
            forceGain = extraForce;
            extraForce = 0;
        }
        if (this.getCooldownLimit(31121054) > 0L) {
            this.forceBlood += ((extraForce > 0) ? extraForce : forceGain);
            if (this.forceBlood >= 50) {
                this.changeCooldown(31121054, -3000);
                this.forceBlood -= 50;
            }
        }
        if (forceGain > 0) {
            ++this.force;
            final MapleAtom atom = new MapleAtom(true, oid, 0, true, skillid, this.getTruePosition().x, this.getTruePosition().y);
            atom.setDwUserOwner(this.id);
            final ForceAtom at = new ForceAtom((extraForce > 0) ? 12 : 5, Randomizer.rand(35, 45), Randomizer.rand(4, 6), Randomizer.rand(35, 45), 0);
            at.setnAttackCount(forceGain);
            atom.addForceAtom(at);
            this.getClient().send(CField.createAtom(atom));
        }
    }

    public void afterAttack(final AttackInfo attack) {
        final int skillid = attack.skill;
        switch (this.getJob()) {
            case 110:
            case 111:
            case 112: {
                if (!PlayerHandler.isFinisher(skillid) & this.getBuffedValue(SecondaryStat.ComboCounter) != null) {
                    this.handleOrbgain(attack, skillid);
                    break;
                }
                break;
            }
        }
        if (!this.isIntern()) {
            this.cancelEffectFromBuffStat(SecondaryStat.WindWalk);
            this.cancelEffectFromBuffStat(SecondaryStat.Infiltrate);
        }
    }

    public void applyIceGage(final int x) {
        this.updateSingleStat(MapleStat.ICE_GAGE, x);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.getTruePosition().x - 25, this.getTruePosition().y - 75, 50, 75);
    }

    public Map<Short, Integer> getEquips() {
        final Map<Short, Integer> eq = new HashMap<Short, Integer>();
        for (final Item item : this.inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            final int itemId = item.getItemId();
            eq.put(item.getPosition(), itemId);
        }
        return eq;
    }

    public Map<Short, Integer> getSecondEquips() {
        final Map<Short, Integer> eq = new HashMap<Short, Integer>();
        for (final Item item : this.inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            final int itemId = item.getItemId();
            if (item instanceof Equip) {
            }
            eq.put(item.getPosition(), itemId);
        }
        return eq;
    }

    public int getReborns() {
        return this.reborns;
    }

    public void setReborns(final int data) {
        this.reborns = data;
    }

    public int getAPS() {
        return this.apstorage;
    }

    public void gainAPS(final int aps) {
        this.apstorage += aps;
    }

    public void doReborn() {
        final Map<MapleStat, Long> stat = new EnumMap<MapleStat, Long>(MapleStat.class);
        ++this.reborns;
        this.setLevel((short) 12);
        this.setExp(0L);
        this.setRemainingAp((short) 0);
        final int oriStats = this.stats.getStr() + this.stats.getDex() + this.stats.getLuk() + this.stats.getInt();
        final int str = Randomizer.rand(25, this.stats.getStr());
        final int dex = Randomizer.rand(25, this.stats.getDex());
        final int int_ = Randomizer.rand(25, this.stats.getInt());
        final int luk = Randomizer.rand(25, this.stats.getLuk());
        final int afterStats = str + dex + int_ + luk;
        final int MAS = oriStats - afterStats + this.getRemainingAp();
        this.client.getPlayer().gainAPS(MAS);
        this.stats.recalcLocalStats(this);
        this.stats.setStr((short) str, this.client.getPlayer());
        this.stats.setDex((short) dex, this.client.getPlayer());
        this.stats.setInt((short) int_, this.client.getPlayer());
        this.stats.setLuk((short) luk, this.client.getPlayer());
        stat.put(MapleStat.STR, (long) str);
        stat.put(MapleStat.DEX, (long) dex);
        stat.put(MapleStat.INT, (long) int_);
        stat.put(MapleStat.LUK, (long) luk);
        stat.put(MapleStat.AVAILABLEAP, 0L);
        this.updateSingleStat(MapleStat.LEVEL, 11L);
        this.updateSingleStat(MapleStat.JOB, 0L);
        this.updateSingleStat(MapleStat.EXP, 0L);
        this.client.getSession().writeAndFlush((Object) CWvsContext.updatePlayerStats(stat, false, this));
    }

    public List<InnerSkillValueHolder> getInnerSkills() {
        return this.innerSkills;
    }

    public int getHonourExp() {
        return this.honourExp;
    }

    public void setHonourExp(final int exp) {
        this.honourExp = exp;
    }

    public int getHonorLevel() {
        if (this.honorLevel == 0) {
            ++this.honorLevel;
        }
        return this.honorLevel;
    }

    public int getHonourNextExp() {
        if (this.getHonorLevel() == 0) {
            return 0;
        }
        return (this.getHonorLevel() + 1) * 500;
    }

    public void setCardStack(final byte amount) {
        this.cardStack = amount;
    }

    public byte getCardStack() {
        return this.cardStack;
    }

    public void setPetLoot(final boolean status) {
        this.petLoot = status;
    }

    public boolean getPetLoot() {
        return this.petLoot;
    }

    public int getStorageNPC() {
        return this.storageNpc;
    }

    public void setStorageNPC(final int id) {
        this.storageNpc = id;
    }

    public boolean getPvpStatus() {
        return this.pvp;
    }

    public void togglePvP() {
        this.pvp = !this.pvp;
    }

    public void enablePvP() {
        this.pvp = true;
    }

    public void disablePvP() {
        this.pvp = false;
    }

    public void addHonorExp(final int amount) {
        this.setHonourExp(this.getHonourExp() + amount);
        this.client.getSession().writeAndFlush((Object) CWvsContext.updateAzwanFame(this.getHonourExp()));
    }

    public void gainHonor(final int honor) {
        this.addHonorExp(honor);
        if (this.getKeyValue(5, "show_honor") > 0L) {
            this.dropMessage(5, "명성치 " + honor + "을 얻었습니다.");
        }
    }

    public List<Integer> HeadTitle() {
        final List<Integer> num_ = new ArrayList<Integer>();
        num_.add(0);
        num_.add(0);
        num_.add(0);
        num_.add(0);
        num_.add(0);
        return num_;
    }

    public int getInternetCafeTime() {
        return this.itcafetime;
    }

    public void setInternetCafeTime(final int itcafetime) {
        this.itcafetime = itcafetime;
    }

    public void InternetCafeTimer() {
        if (this.itcafetimer != null) {
            this.itcafetimer.cancel(false);
        }
        this.itcafetimer = server.Timer.CloneTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (MapleCharacter.this.getInternetCafeTime() < 1) {
                    MapleCharacter.this.client.getSession().writeAndFlush((Object) CField.getInternetCafe((byte) 4, 0));
                    return;
                }
                MapleCharacter.this.setInternetCafeTime(MapleCharacter.this.getInternetCafeTime() - 1);
            }
        }, 60000L);
    }

    public short getMonsterCombo() {
        return this.monsterCombo;
    }

    public void setMonsterCombo(final short count) {
        this.monsterCombo = count;
    }

    public void addMonsterCombo(final short amount) {
        this.monsterCombo += amount;
    }

    public long getMonsterComboTime() {
        return this.monsterComboTime;
    }

    public void setMonsterComboTime(final long count) {
        this.monsterComboTime = count;
    }

    public long getRuneTimeStamp() {
        return this.lastTouchedRuneTime;
    }

    public void setRuneTimeStamp(final long lastTouchedRuneTime) {
        this.lastTouchedRuneTime = lastTouchedRuneTime;
    }

    public int getTouchedRune() {
        return this.TouchedRune;
    }

    public void setTouchedRune(final int type) {
        this.TouchedRune = type;
    }

    public void cancelRapidTime(final byte type) {
        if (type == 1) {
            if (this.rapidtimer1 != null) {
                this.rapidtimer1.cancel(false);
            }
            this.rapidtimer1 = server.Timer.BuffTimer.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    MapleCharacter.this.changeSkillLevel(SkillFactory.getSkill(100000276), (byte) 0, (byte) 0);
                }
            }, 20000L);
        } else if (type == 2) {
            if (this.rapidtimer2 != null) {
                this.rapidtimer2.cancel(false);
            }
            this.rapidtimer2 = server.Timer.BuffTimer.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    MapleCharacter.this.changeSkillLevel(SkillFactory.getSkill(100000277), (byte) 0, (byte) 0);
                }
            }, 20000L);
        }
    }

    public long getnHPoint() {
        try {
            return Long.parseLong(this.client.getKeyValue("nHpoint"));
        } catch (Exception e) {
            return 0L;
        }
    }

        public long getnPPoint() {
        try {
            return Long.parseLong(this.client.getKeyValue("nPpoint"));
        } catch (Exception e) {
            return 0L;
        }
    }
    
    public int getHgrade() {
        try {
            return Integer.parseInt(this.client.getKeyValue("hGrade"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setHgrade(final int a) {
        this.client.setKeyValue("hGrade", String.valueOf(a));
    }

    public String getHgrades() {
        switch (this.getHgrade()) {
            case 1: {
                return "골드";
            }
            case 2: {
                return "다이아몬드";
            }
            case 3: {
                return "블루";
            }
            case 4: {
                return "레드";
            }
            case 5: {
                return "블랙";
            }
            case 6: {
                return "크라운";
            }
            case 7: {
                return "vip";
            }
            case 8: {
                return "vvip";
            }
            case 9: {
                return "rvip";
            }
            default: {
                return "일반";
            }
        }
    }

    public int getPgrade() {
        try {
            return Integer.parseInt(this.client.getKeyValue("pGrade"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setPgrade(final int a) {
        this.client.setKeyValue("pGrade", String.valueOf(a));
    }

    public String getPgrades() {
        final String keyValue = this.client.getKeyValue("pGrade");
        switch (keyValue) {
            case "1": {
                return "비기닝";
            }
            case "2": {
                return "라이징";
            }
            case "3": {
                return "플라잉";
            }
            case "4": {
                return "샤이닝";
            }
            case "5": {
                return "아이돌";
            }
            case "6": {
                return "슈퍼스타";
            }
            default: {
                return "일반";
            }
        }
    }

    public void gainnHPoint(final int a) {
        this.client.setKeyValue("nHpoint", String.valueOf(this.getHPoint() + a));
    }

    public void setnHPoint(final int a) {
        this.client.setKeyValue("nHpoint", String.valueOf(a));
    }
    
    public void gainnPPoint(final int a) {
        this.client.setKeyValue("nPpoint", String.valueOf(this.getPPoint() + a));
    }

    public void setnPPoint(final int a) {
        this.client.setKeyValue("nPpoint", String.valueOf(a));
    }

    public long getnDonationPoint() {
        try {
            return Long.parseLong(this.client.getKeyValue("nDpoint"));
        } catch (Exception e) {
            return 0L;
        }
    }

    public void gainnDonationPoint(final int a) {
        this.client.setKeyValue("nDpoint", String.valueOf(this.getnDonationPoint() + a));
    }

    public void setnDonationPoint(final int a) {
        this.client.setKeyValue("nDpoint", String.valueOf(a));
    }

    public long getHPoint() {
        try {
            return Long.parseLong(this.client.getKeyValue("HPoint"));
        } catch (Exception e) {
            return 0L;
        }
    }
    
        public long getPPoint() {
        try {
            return Long.parseLong(this.client.getKeyValue("PPoint"));
        } catch (Exception e) {
            return 0L;
        }
    }

    public void setEventMobCount(final int a) {
        this.eventcount = a;
    }

    public int getEventMobCount() {
        return this.eventcount;
    }

    public void setEventKillingMode(final boolean a) {
        this.eventkillmode = a;
    }

    public boolean getEventKillingMode() {
        return this.eventkillmode;
    }

    public void gainHPoint(final int a) {
        if (a > 0) {
            this.gainnHPoint(a);
        }
        this.client.setKeyValue("HPoint", String.valueOf(this.getHPoint() + a));
    }

    public void setHPoint(final int a) {
        this.client.setKeyValue("HPoint", String.valueOf(a));
    }
    
        public void gainPPoint(final int a) {
        if (a > 0) {
            this.gainnPPoint(a);
        }
        this.client.setKeyValue("PPoint", String.valueOf(this.getPPoint() + a));
    }

    public void setPPoint(final int a) {
        this.client.setKeyValue("PPoint", String.valueOf(a));
    }

    public long getDonationPoint() {
        try {
            return Long.parseLong(this.client.getKeyValue("DPoint"));
        } catch (Exception e) {
            return 0L;
        }
    }

    public void gainDonationPoint(final int a) {
        if (a > 0) {
            this.gainnDonationPoint(a);
        }
        this.client.setKeyValue("DPoint", String.valueOf(this.getDonationPoint() + a));
        this.updateDonationPoint();
    }

    public void setDonationPoint(final int a) {
        this.client.setKeyValue("DPoint", String.valueOf(a));
        this.updateDonationPoint();
    }

    public void updateDonationPoint() {
        this.client.getSession().writeAndFlush((Object) CWvsContext.updateMaplePoint(this));
    }

    public int getBetaClothes() {
        return this.betaclothes;
    }

    public void pBetaClothes(final int value) {
        this.betaclothes += value;
    }

    public void mBetaClothes(final int value) {
        this.betaclothes -= value;
    }

    public int getArcaneAim() {
        return this.arcaneAim;
    }

    public void setArcaneAim(final int a) {
        this.arcaneAim = a;
    }

    public static boolean updateNameChangeCoupon(final MapleClient c) {
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET nameChange = ? WHERE id = ?");
            ps.setByte(1, c.getNameChangeEnable());
            ps.setInt(2, c.getAccID());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
        return true;
    }

    public static boolean saveNameChange(final String name, final int cid) {
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE characters SET name = ? WHERE id = ?");
            ps.setString(1, name);
            ps.setInt(2, cid);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
        return true;
    }

    public Map<Integer, Integer> getSkillCustomValues2() {
        return this.customValue;
    }

    public void unchooseStolenSkill(final int skillID) {
        if (this.skillisCooling(20031208) || this.stolenSkills == null) {
            this.dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        final int stolenjob = GameConstants.getJobNumber(skillID);
        boolean changed = false;
        for (final Pair<Integer, Boolean> sk : this.stolenSkills) {
            if (sk.right && GameConstants.getJobNumber(sk.left) == stolenjob) {
                this.cancelStolenSkill(sk.left);
                sk.right = false;
                changed = true;
            }
        }
        if (changed) {
            final Skill skil = SkillFactory.getSkill(skillID);
            this.changeSkillLevel_Skip(skil, this.getSkillLevel(skil), (byte) 0);
            this.client.getSession().writeAndFlush((Object) CField.replaceStolenSkill(GameConstants.getStealSkill(stolenjob), 0));
        }
    }

    public void cancelStolenSkill(final int skillID) {
        final Skill skk = SkillFactory.getSkill(skillID);
        final SecondaryStatEffect eff = skk.getEffect(this.getTotalSkillLevel(skk));
        if (eff.getDuration() > 0 && !eff.getStatups().isEmpty()) {
            boolean party = false;
            switch (skillID) {
                case 1101006:
                case 1211011:
                case 1301006:
                case 1301007:
                case 2101001:
                case 2201001:
                case 2301002:
                case 2301004:
                case 2311001:
                case 2311003:
                case 2311009:
                case 2321005:
                case 2321007:
                case 2321055:
                case 3121002:
                case 3221002:
                case 3321022:
                case 4101004:
                case 4111001:
                case 4201003:
                case 5121009:
                case 5301003:
                case 5320008:
                case 12101000:
                case 13121005:
                case 14101003:
                case 15121005:
                case 21111012:
                case 22151003:
                case 22171054:
                case 27111006:
                case 27111101:
                case 32001003:
                case 32001016:
                case 32101003:
                case 32101009:
                case 32111012:
                case 32121017:
                case 33101005:
                case 33121004:
                case 51101004:
                case 51111008:
                case 61121009:
                case 100001263:
                case 100001264:
                case 131001009:
                case 131001013:
                case 131001113:
                case 152121043:
                case 400011003:
                case 400041011:
                case 400041012:
                case 400041013:
                case 400041014:
                case 400041015: {
                    party = true;
                    break;
                }
            }
            if (party) {
                for (final MapleCharacter chr : this.map.getCharactersThreadsafe()) {
                    if (chr.getBuffedValue(skillID)) {
                        chr.getClient().getSession().writeAndFlush((Object) CField.getGameMessage(6, SkillFactory.getSkillName(skillID) + " 스킬을 사용 할 수 없어 효과가 제거되었습니다."));
                        chr.cancelEffect(eff);
                    }
                }
            } else if (this.getBuffedValue(skillID)) {
                this.getClient().getSession().writeAndFlush((Object) CField.getGameMessage(6, SkillFactory.getSkillName(skillID) + " 스킬을 사용 할 수 없어 효과가 제거되었습니다."));
                this.cancelEffect(eff);
            }
        }
    }

    public void chooseStolenSkill(final int skillID) {
        if (this.skillisCooling(20031208) || this.stolenSkills == null) {
            this.dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        final Pair<Integer, Boolean> dummy = new Pair<Integer, Boolean>(skillID, false);
        if (this.stolenSkills.contains(dummy)) {
            this.unchooseStolenSkill(skillID);
            this.stolenSkills.get(this.stolenSkills.indexOf(dummy)).right = true;
            this.addCooldown(GameConstants.getStealSkill(GameConstants.getJobNumber(skillID)), System.currentTimeMillis(), 30000L);
            this.client.getSession().writeAndFlush((Object) CField.skillCooldown(GameConstants.getStealSkill(GameConstants.getJobNumber(skillID)), 30000));
            this.client.getSession().writeAndFlush((Object) CField.replaceStolenSkill(GameConstants.getStealSkill(GameConstants.getJobNumber(skillID)), skillID));
        }
    }

    public void addStolenSkill(int skillID, int skillLevel) {
        if (this.skillisCooling(20031208) || this.stolenSkills == null) {
            this.dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        Pair<Integer, Boolean> dummy = new Pair<Integer, Boolean>(skillID, true);
        Skill skil = SkillFactory.getSkill(skillID);
        if (!this.stolenSkills.contains(dummy)) {
            dummy.right = false;
            skillLevel = Math.min(skil.getMaxLevel(), skillLevel);
            int jobid = GameConstants.getJobNumber(skillID);
            if (!this.stolenSkills.contains(dummy)) {
                int count = 0;
                skillLevel = Math.min(this.getSkillLevel(GameConstants.getStealSkill(jobid)), skillLevel);
                for (Pair<Integer, Boolean> sk : this.stolenSkills) {
                    if (GameConstants.getJobNumber((Integer) sk.left) != jobid) {
                        continue;
                    }
                    ++count;
                }
                if (count < GameConstants.getNumSteal(jobid)) {
                    this.stolenSkills.add(dummy);
                    this.changed_skills = true;
                    this.changeSkillLevel_Skip(skil, skillLevel, (byte) skillLevel);
                    this.client.getSession().writeAndFlush((Object) CField.addStolenSkill(jobid, count, skillID, skillLevel));
                }
            }
        }
    }

    public void removeStolenSkill(int skillID) {
        if (this.skillisCooling(20031208) || this.stolenSkills == null) {
            this.dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        int jobid = GameConstants.getJobNumber(skillID);
        Pair<Integer, Boolean> dummy = new Pair<Integer, Boolean>(skillID, false);
        int count = -1;
        int cc = 0;
        for (int i = 0; i < this.stolenSkills.size(); ++i) {
            if ((Integer) this.stolenSkills.get((int) i).left == skillID) {
                if (((Boolean) this.stolenSkills.get((int) i).right).booleanValue()) {
                    this.unchooseStolenSkill(skillID);
                }
                count = cc;
                break;
            }
            if (GameConstants.getJobNumber((Integer) this.stolenSkills.get((int) i).left) != jobid) {
                continue;
            }
            ++cc;
        }
        if (count >= 0) {
            this.cancelStolenSkill(skillID);
            this.stolenSkills.remove(dummy);
            dummy.right = true;
            this.stolenSkills.remove(dummy);
            this.changed_skills = true;
            this.changeSkillLevel_Skip(SkillFactory.getSkill(skillID), 0, (byte) 0);
            this.client.getSession().writeAndFlush((Object) CField.removeStolenSkill(jobid, count));
            this.saveToDB(false, false);
        }
    }

    public List<Pair<Integer, Boolean>> getStolenSkills() {
        return this.stolenSkills;
    }

    public final void startDiabolicRecovery(final SecondaryStatEffect eff) {
        final server.Timer.BuffTimer tMan = server.Timer.BuffTimer.getInstance();
        final int regenHP = (int) (this.getStat().getCurrentMaxHp() * (eff.getX() / 100.0));
        if (this.diabolicRecoveryTask != null) {
            this.diabolicRecoveryTask.cancel(true);
            this.diabolicRecoveryTask = null;
        }
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if (MapleCharacter.this.isAlive()) {
                    MapleCharacter.this.addHP(regenHP);
                    if (MapleCharacter.this.getStat().getCurrentMaxHp() - regenHP > 0L) {
                        MapleCharacter.this.client.getSession().writeAndFlush((Object) CField.EffectPacket.showHealEffect(MapleCharacter.this.client.getPlayer(), (int) Math.min(MapleCharacter.this.getStat().getCurrentMaxHp() - regenHP, regenHP), true));
                    }
                }
            }
        };
        this.diabolicRecoveryTask = tMan.register(r, eff.getW() * 1000);
        tMan.schedule(new Runnable() {
            @Override
            public void run() {
                if (MapleCharacter.this.diabolicRecoveryTask != null) {
                    MapleCharacter.this.diabolicRecoveryTask.cancel(true);
                    MapleCharacter.this.diabolicRecoveryTask = null;
                }
            }
        }, eff.getDuration());
    }

    public short getXenonSurplus() {
        return this.xenonSurplus;
    }

    public void setXenonSurplus(final short amount, final Skill skill) {
        int maxSupply = (this.level >= 100) ? 20 : ((this.level >= 60) ? 15 : ((this.level >= 30) ? 10 : 5));
        if (this.getBuffedValue(SecondaryStat.Overload) != null) {
            maxSupply = 40;
        }
        if (this.xenonSurplus + amount > maxSupply) {
            this.updateXenonSurplus(this.xenonSurplus = (short) maxSupply, skill);
            return;
        }
        this.updateXenonSurplus(this.xenonSurplus = amount, skill);
    }

    public void gainXenonSurplus(final short amount, final Skill skill) {
        int maxSupply = (this.level >= 100) ? 20 : ((this.level >= 60) ? 15 : ((this.level >= 30) ? 10 : 5));
        if (this.getBuffedValue(SecondaryStat.Overload) != null) {
            maxSupply = 40;
        }
        if (this.xenonSurplus + amount > maxSupply) {
            this.updateXenonSurplus(this.xenonSurplus = (short) maxSupply, skill);
            return;
        }
        this.updateXenonSurplus(this.xenonSurplus += amount, skill);
    }

    public void updateXenonSurplus(short amount, final Skill skill) {
        int maxSupply = (this.level >= 100) ? 20 : ((this.level >= 60) ? 15 : ((this.level >= 30) ? 10 : 5));
        if (this.getBuffedValue(SecondaryStat.Overload) != null) {
            maxSupply = 40;
        }
        if (amount > maxSupply) {
            amount = (short) maxSupply;
        }
        final SecondaryStatEffect effect = SkillFactory.getSkill(30020232).getEffect(this.getTotalSkillLevel(skill));
        final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
        statups.put(SecondaryStat.SurplusSupply, new Pair<Integer, Integer>((int) amount, 0));
        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
    }

    public final void startXenonSupply(final Skill skill) {
        final server.Timer.BuffTimer tMan = server.Timer.BuffTimer.getInstance();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                int maxSupply = (MapleCharacter.this.level >= 100) ? 20 : ((MapleCharacter.this.level >= 60) ? 15 : ((MapleCharacter.this.level >= 30) ? 10 : 5));
                if (MapleCharacter.this.getBuffedValue(SecondaryStat.Overload) != null) {
                    maxSupply = 40;
                }
                if (maxSupply > MapleCharacter.this.getXenonSurplus()) {
                    MapleCharacter.this.gainXenonSurplus((short) 1, skill);
                }
            }
        };
        if (this.client.isLoggedIn()) {
            MapleCharacter.XenonSupplyTask = tMan.register(r, 4000L);
        }
    }

    public void handleExceedAttack(final int skillid) {
        int ownskillid = 0;
        this.setSkillCustomInfo(30010231, 0L, 10000L);
        switch (skillid) {
            case 31011000:
            case 31011004:
            case 31011005:
            case 31011006:
            case 31011007: {
                ownskillid = 31011000;
                break;
            }
            case 31201000:
            case 31201007:
            case 31201008:
            case 31201009:
            case 31201010: {
                ownskillid = 31201000;
                break;
            }
            case 31211000:
            case 31211007:
            case 31211008:
            case 31211009:
            case 31211010: {
                ownskillid = 31211000;
                break;
            }
            case 31221000:
            case 31221009:
            case 31221010:
            case 31221011:
            case 31221012: {
                ownskillid = 31221000;
                break;
            }
        }
        if (this.getSkillCustomValue0(30010232) != ownskillid && this.getSkillCustomValue0(30010232) != 0L) {
            if (this.getSkillLevel(31220044) > 0) {
                if (this.getExceed() < 19) {
                    this.gainExceed((short) 1);
                }
            } else if (this.getExceed() < 20) {
                this.gainExceed((short) 1);
            }
        }
        this.setSkillCustomInfo(30010232, ownskillid, 0L);
        final SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(this.getTotalSkillLevel(skillid));
        final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
        statups.put(SecondaryStat.ExceedOverload, new Pair<Integer, Integer>(1, 10000));
        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, this));
    }

    public int getExceed() {
        return this.exceed;
    }

    public void setExceed(final short amount) {
        this.exceed = amount;
        if (this.getSkillLevel(31220044) > 0 && this.exceed > 18) {
            this.exceed = 18;
        }
    }

    public void gainExceed(final short amount) {
        this.exceed += amount;
        if (this.getSkillLevel(31220044) > 0 && this.exceed >= 18) {
            this.exceed = 18;
        }
        this.updateExceed(this.exceed);
    }

    public void updateExceed(final int amount) {
        if (amount > 0) {
            SkillFactory.getSkill(30010230).getEffect(1).applyTo(this);
        }
    }

    public void setLuminusMorph(final boolean morph) {
        this.luminusMorph = morph;
    }

    public boolean getLuminusMorph() {
        return this.luminusMorph;
    }

    public void setLuminusMorphUse(final int use) {
        this.lumimorphuse = use;
    }

    public int getLuminusMorphUse() {
        return this.lumimorphuse;
    }

    public int getForcingItem() {
        return this.forcingItem;
    }

    public void setForcingItem(final short forcingItem) {
        this.forcingItem = forcingItem;
    }

    public long getCooldownLimit(final int skillid) {
        for (final MapleCoolDownValueHolder mcdvh : this.getCooldowns()) {
            if (mcdvh.skillId == skillid) {
                return mcdvh.length - (System.currentTimeMillis() - mcdvh.startTime);
            }
        }
        return 0L;
    }

    public Long getBuffedStarttime(final SecondaryStat effect, final int skillid) {
        final SecondaryStatValueHolder mbsvh = this.checkBuffStatValueHolder(effect);
        if (mbsvh == null) {
            return null;
        }
        if (skillid == -1) {
            if (mbsvh.effect != null) {
                return mbsvh.startTime;
            }
        } else {
            for (final Pair<SecondaryStat, SecondaryStatValueHolder> buff : this.getEffects()) {
                if (buff.right.effect.getSourceId() == skillid) {
                    return buff.getRight().startTime;
                }
            }
        }
        return null;
    }

    public long getBuffLimit(final int skillid) {
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> mcdvh : this.effects) {
            if (mcdvh.right.effect.getSourceId() == skillid) {
                return mcdvh.right.localDuration - (System.currentTimeMillis() - mcdvh.right.startTime);
            }
        }
        return 0L;
    }

    public long getBuffLimit(final SecondaryStat s, final int skillid) {
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> mcdvh : this.effects) {
            if (s == mcdvh.left && mcdvh.right.effect.getSourceId() == skillid) {
                return mcdvh.right.localDuration - (System.currentTimeMillis() - mcdvh.right.startTime);
            }
        }
        return 0L;
    }

    public void setFishing(final boolean a) {
        this.fishing = a;
    }

    public boolean Fishing() {
        return this.fishing;
    }

    public byte getWolfScore() {
        return this.wolfscore;
    }

    public void setWolfScore(final byte farmscore) {
        this.wolfscore = farmscore;
    }

    public byte getSheepScore() {
        return this.sheepscore;
    }

    public void setSheepScore(final byte farmscore) {
        this.sheepscore = farmscore;
    }

    public void addWolfScore() {
        ++this.wolfscore;
    }

    public void addSheepScore() {
        --this.sheepscore;
    }

    public byte getPandoraBoxFever() {
        return this.pandoraBoxFever;
    }

    public void setPandoraBoxFever(final byte pandoraBoxFever) {
        this.pandoraBoxFever = pandoraBoxFever;
    }

    public void addPandoraBoxFever(final byte pandoraBoxFever) {
        this.pandoraBoxFever += pandoraBoxFever;
    }

    public void handleKaiserCombo(int skillid) {
        if (this.getKaiserCombo() < 1000) {
            int count = Randomizer.rand(2, 3);
            switch (skillid) {
                case 61101002:
                case 61110211: {
                    count = 6;
                    break;
                }
                case 61120007:
                case 61121217:
                case 400011058:
                case 400011059:
                case 400011060:
                case 400011061: {
                    count = 8;
                    break;
                }
                case 61121052:
                case 61121105:
                case 61121222: {
                    count = 35;
                    break;
                }
                case 400011118:
                case 400011119:
                case 400011130: {
                    count = 0;
                    break;
                }
                case 61111100: {
                    count = 1;
                }
            }
            this.setKaiserCombo((short) (this.getKaiserCombo() + count));
        }
        SkillFactory.getSkill(61111008).getEffect(1).applyKaiserCombo(this, this.getKaiserCombo());
    }

    public void resetKaiserCombo() {
        this.setKaiserCombo((short) 0);
        SkillFactory.getSkill(61111008).getEffect(1).applyKaiserCombo(this, this.getKaiserCombo());
    }

    public List<Core> getCore() {
        return this.cores;
    }

    public int getBaseColor() {
        return this.basecolor;
    }

    public void setBaseColor(final int basecolor) {
        this.basecolor = basecolor;
    }

    public int getAddColor() {
        return this.addcolor;
    }

    public void setAddColor(final int addcolor) {
        this.addcolor = addcolor;
    }

    public int getBaseProb() {
        return this.baseprob;
    }

    public void setBaseProb(final int baseprob) {
        this.baseprob = baseprob;
    }

    public int getSecondBaseColor() {
        return this.secondbasecolor;
    }

    public void setSecondBaseColor(final int basecolor) {
        this.secondbasecolor = basecolor;
    }

    public int getSecondAddColor() {
        return this.secondaddcolor;
    }

    public void setSecondAddColor(final int addcolor) {
        this.secondaddcolor = addcolor;
    }

    public int getSecondBaseProb() {
        return this.secondbaseprob;
    }

    public void setSecondBaseProb(final int baseprob) {
        this.secondbaseprob = baseprob;
    }

    public List<Integer> getCashWishList() {
        return this.cashwishlist;
    }

    public void addCashWishList(final int id) {
        this.cashwishlist.add(id);
    }

    public void removeCashWishList(final int id) {
        this.cashwishlist.remove(id);
    }

    public void giveHoyoungGauge(final int skillid) {
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        final Skill sk = SkillFactory.getSkill(skillid);
        if (sk != null) {
            final SecondaryStatEffect effect = SkillFactory.getSkill(164000010).getEffect(1);
            SecondaryStatEffect advance = null;
            if (this.job == 16411 || this.job == 16412) {
                advance = SkillFactory.getSkill(164110014).getEffect(1);
            }
            final int max = 100;
            int add = 0;
            switch (skillid) {
                case 164111000:
                case 164121000:
                case 400041064: {
                    if (this.useChun) {
                        add = 0;
                    } else {
                        this.useChun = true;
                        add = effect.getU();
                    }
                    this.scrollGauge += 15;
                    this.energy += 10;
                    break;
                }
                case 164101000:
                case 164111003:
                case 400041065: {
                    if (this.useJi) {
                        add = 0;
                    } else {
                        this.useJi = true;
                        add = effect.getV();
                    }
                    this.scrollGauge += 15;
                    this.energy += 10;
                    break;
                }
                case 164001000:
                case 164121003:
                case 400041066: {
                    if (this.useIn) {
                        add = 0;
                    } else {
                        this.useIn = true;
                        add = effect.getW();
                    }
                    this.scrollGauge += 15;
                    this.energy += 10;
                    break;
                }
                case 164001001:
                case 164101003:
                case 164111007:
                case 164121006: {
                    this.energy = 0;
                    if (advance != null) {
                        this.scrollGauge = Math.min(this.scrollGauge + 200, 900);
                        break;
                    }
                    break;
                }
                case 164111008:
                case 164121007:
                case 164121008:
                case 400041050: {
                    this.scrollGauge = 0;
                    break;
                }
            }
            this.energy = Math.min(max, this.energy + add);
            if (this.useChun && this.useJi && this.useIn) {
                this.useChun = false;
                this.useJi = false;
                this.useIn = false;
                this.addHP(this.getStat().getCurrentMaxHp() / 100L * 3L);
                this.addMP(this.getStat().getCurrentMaxMp(this) / 100L * 3L);
                if (this.getSkillCustomValue(400041051) == null) {
                    for (final MapleSummon s : this.getMap().getAllSummonsThreadsafe()) {
                        if (s.getOwner().getId() == this.getId() && (s.getSkill() == 400041050 || s.getSkill() == 400041051)) {
                            this.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.summonRangeAttack(s, 400041051));
                            this.setSkillCustomInfo(400041051, 0L, 3000L);
                            break;
                        }
                    }
                }
                if (this.getBuffedEffect(SecondaryStat.SageElementalClone) != null) {
                    final List<Integer> skilllist = new ArrayList<Integer>();
                    skilllist.add(164111000);
                    skilllist.add(164121000);
                    skilllist.add(164101000);
                    skilllist.add(164111003);
                    skilllist.add(164001000);
                    skilllist.add(164121003);
                    Collections.addAll(skilllist, new Integer[0]);
                    Collections.shuffle(skilllist);
                    for (final Integer skill : skilllist) {
                        if (this.skillisCooling(skill)) {
                            this.removeCooldown(skill);
                            break;
                        }
                    }
                }
            }
            statups.put(SecondaryStat.HoyoungThirdProperty, new Pair<Integer, Integer>((int) (this.useChun ? 1 : 0), 0));
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, null, this));
            statups.clear();
            statups.put(SecondaryStat.TidalForce, new Pair<Integer, Integer>(this.energy, 0));
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, null, this));
        }
    }

    public long getLastCreationTime() {
        return this.lastCreationTime;
    }

    public void setLastCreationTime(final long time) {
        this.lastCreationTime = time;
    }

    public void 에테르핸들러(final MapleCharacter chr, int gain, final int skillid, final boolean refresh) {
        if (refresh) {
            if (chr.에테르소드 > 0 && chr.에테르소드 <= 2) {
                chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, 2));
            } else if (chr.에테르소드 > 0 && chr.에테르소드 <= 4) {
                chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, 2));
                chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, 4));
            } else if (chr.에테르소드 > 0 && chr.에테르소드 <= 6) {
                chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, 2));
                chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, 4));
                chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, 6));
            }
        }
        if (chr.getSkillLevel(151100017) > 0) {
            final int max = (chr.getSkillLevel(151120012) > 0) ? 400 : 300;
            if (gain < 0) {
                chr.에테르 += gain;
                final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
                statups.put(SecondaryStat.AdelGauge, new Pair<Integer, Integer>(chr.에테르, 0));
                final SecondaryStatEffect effect = SkillFactory.getSkill(151100017).getEffect(chr.getTotalSkillLevel(151100017));
                chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, chr));
            } else if (chr.에테르 < max) {
                if (chr.getBuffedValue(400011109)) {
                    final SecondaryStatEffect effect2 = SkillFactory.getSkill(400011109).getEffect(chr.getTotalSkillLevel(400011109));
                    final int plus = gain * effect2.getX() / 100;
                    gain += plus;
                }
                chr.에테르 += gain;
                if (chr.에테르 >= max) {
                    chr.에테르 = max;
                }
                if ((chr.에테르 >= 100 && chr.에테르소드 < 2) || (chr.에테르 >= 200 && chr.에테르소드 < 4) || (chr.에테르 >= 300 && chr.에테르소드 < 6)) {
                    chr.에테르소드 += 2;
                    if (chr.getJob() != 15112 && chr.에테르소드 > 4) {
                        chr.에테르소드 = 4;
                    }
                    if (chr.getBuffedValue(151101006)) {
                        if (chr.에테르소드 == 1 || chr.에테르소드 < 0) {
                            chr.에테르소드 = 2;
                            chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, 2));
                        } else {
                            chr.getMap().broadcastMessage(SkillPacket.CreateSworldReadyObtacle(chr, 15112, chr.에테르소드));
                        }
                    }
                }
                final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
                statups.put(SecondaryStat.AdelGauge, new Pair<Integer, Integer>(chr.에테르, 0));
                final SecondaryStatEffect effect = SkillFactory.getSkill(151100017).getEffect(chr.getTotalSkillLevel(151100017));
                chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, chr));
            }
        }
    }

    public void 에테르결정(final MapleCharacter chr, final Point pos, final boolean nocooltime) {
        int size = 0;
        boolean spawn = true;
        for (final MapleSummon summon : new ArrayList<MapleSummon>(chr.summons)) {
            if (summon.getSkill() == 151100002 && summon.getPosition().x - 350 <= pos.x && summon.getPosition().x + 350 >= pos.x && summon.getPosition().y - 70 <= pos.y && summon.getPosition().y + 70 >= pos.y) {
                spawn = false;
            }
        }
        if ((chr.getSkillCustomValue(151100002) == null || nocooltime) && spawn) {
            for (final MapleSummon summon : new ArrayList<MapleSummon>(chr.summons)) {
                if (summon.getSkill() == 151100002) {
                    ++size;
                }
                if (size > 6) {
                    summon.removeSummon(chr.getMap(), false);
                }
            }
            final MapleSummon tosummon = new MapleSummon(chr, 151100002, pos, SummonMovementType.STATIONARY, (byte) 0, 30000);
            tosummon.setPosition(pos);
            chr.addSummon(tosummon);
            tosummon.addHP(10000);
            chr.getMap().spawnSummon(tosummon, 30000);
            if (!nocooltime) {
                chr.setSkillCustomInfo(151100002, 0L, 4000L);
            }
        }
    }

    public static void 렐릭게이지(final MapleClient c, final int skillid) {
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        if (c.getPlayer().렐릭게이지 <= 1000 && c.getPlayer().getSkillCustomValue(3321035) == null) {
            switch (skillid) {
                case 3011004:
                case 3300002:
                case 3321003: {
                    if (c.getPlayer().getSkillLevel(3320000) < 1) {
                        final MapleCharacter player = c.getPlayer();
                        player.렐릭게이지 += 5;
                        if (c.getPlayer().렐릭게이지 < 1000) {
                            final MapleCharacter player2 = c.getPlayer();
                            player2.에인션트가이던스 += 5;
                            break;
                        }
                        break;
                    } else {
                        final MapleCharacter player3 = c.getPlayer();
                        player3.렐릭게이지 += 10;
                        if (c.getPlayer().렐릭게이지 < 1000) {
                            final MapleCharacter player4 = c.getPlayer();
                            player4.에인션트가이던스 += 10;
                            break;
                        }
                        break;
                    }
                }
                case 3301003:
                case 3301004:
                case 3321004:
                case 3321005: {
                    if (c.getPlayer().getSkillLevel(3320000) < 1) {
                        final MapleCharacter player5 = c.getPlayer();
                        player5.렐릭게이지 += 10;
                        if (c.getPlayer().렐릭게이지 < 1000) {
                            final MapleCharacter player6 = c.getPlayer();
                            player6.에인션트가이던스 += 10;
                            break;
                        }
                        break;
                    } else {
                        final MapleCharacter player7 = c.getPlayer();
                        player7.렐릭게이지 += 20;
                        if (c.getPlayer().렐릭게이지 < 1000) {
                            final MapleCharacter player8 = c.getPlayer();
                            player8.에인션트가이던스 += 20;
                            break;
                        }
                        break;
                    }
                }
                case 3311002:
                case 3311003:
                case 3321006:
                case 3321007: {
                    if (c.getPlayer().getSkillLevel(3320000) < 1) {
                        final MapleCharacter player9 = c.getPlayer();
                        player9.렐릭게이지 += 10;
                        if (c.getPlayer().렐릭게이지 < 1000) {
                            final MapleCharacter player10 = c.getPlayer();
                            player10.에인션트가이던스 += 10;
                            break;
                        }
                        break;
                    } else {
                        final MapleCharacter player11 = c.getPlayer();
                        player11.렐릭게이지 += 20;
                        if (c.getPlayer().렐릭게이지 < 1000) {
                            final MapleCharacter player12 = c.getPlayer();
                            player12.에인션트가이던스 += 20;
                            break;
                        }
                        break;
                    }
                }
                case 3311009: {
                    if (c.getPlayer().getSkillLevel(3320000) <= 0) {
                        break;
                    }
                    final MapleCharacter player13 = c.getPlayer();
                    player13.렐릭게이지 += 10;
                    if (c.getPlayer().렐릭게이지 < 1000) {
                        final MapleCharacter player14 = c.getPlayer();
                        player14.에인션트가이던스 += 10;
                        break;
                    }
                    break;
                }
                case 3301008:
                case 3311010: {
                    final MapleCharacter player15 = c.getPlayer();
                    player15.렐릭게이지 -= 50;
                    break;
                }
                case 3321012: {
                    final MapleCharacter player16 = c.getPlayer();
                    player16.렐릭게이지 -= 100;
                    break;
                }
                case 3321015:
                case 3321017:
                case 3321019:
                case 3321021: {
                    final MapleCharacter player17 = c.getPlayer();
                    player17.렐릭게이지 -= 150;
                    break;
                }
                case 3321035:
                case 3321036:
                case 3321037:
                case 3321038:
                case 3321039:
                case 3321040: {
                    final MapleCharacter player18 = c.getPlayer();
                    player18.렐릭게이지 -= 65;
                    c.getPlayer().setSkillCustomInfo(3321035, 0L, 1000L);
                    break;
                }
                case 400031036: {
                    if (c.getPlayer().getSkillCustomValue(400031036) != 0L) {
                        final MapleCharacter player19 = c.getPlayer();
                        player19.렐릭게이지 -= 300;
                        c.getPlayer().setSkillCustomInfo(400031036, 0L, 0L);
                        break;
                    }
                    final MapleCharacter player20 = c.getPlayer();
                    player20.렐릭게이지 += 10;
                    if (c.getPlayer().렐릭게이지 < 1000) {
                        final MapleCharacter player21 = c.getPlayer();
                        player21.에인션트가이던스 += 10;
                        break;
                    }
                    break;
                }
                case 400031034: {
                    c.getPlayer().렐릭게이지 = 0;
                    break;
                }
                case 400031037:
                case 400031038:
                case 400031039:
                case 400031040: {
                    final MapleCharacter player22 = c.getPlayer();
                    player22.렐릭게이지 -= 500;
                    break;
                }
                case 400031047:
                case 400031049:
                case 400031051: {
                    final MapleCharacter player23 = c.getPlayer();
                    player23.렐릭게이지 -= 250;
                    break;
                }
            }
        }
        if (c.getPlayer().렐릭게이지 >= 1000) {
            c.getPlayer().렐릭게이지 = 1000;
        } else if (c.getPlayer().렐릭게이지 <= 0) {
            c.getPlayer().렐릭게이지 = 0;
        }
        if (c.getPlayer().에인션트가이던스 >= 1000) {
            c.getPlayer().에인션트가이던스 = 0;
            SkillFactory.getSkill(3310006).getEffect(c.getPlayer().getSkillLevel(3310006)).applyTo(c.getPlayer());
        }
        statups.put(SecondaryStat.RelikGauge, new Pair<Integer, Integer>(c.getPlayer().렐릭게이지, 0));
        statups.put(SecondaryStat.AncientGuidance, new Pair<Integer, Integer>(c.getPlayer().에인션트가이던스, 0));
        c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, null, c.getPlayer()));
    }

    public static void 문양(final MapleClient c, final int skillid) {
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        final int 이전문양 = c.getPlayer().문양;
        switch (skillid) {
            case 3011004:
            case 3300002:
            case 3321003: {
                c.getPlayer().문양 = 1;
                break;
            }
            case 3321005: {
                c.getPlayer().문양 = 2;
                break;
            }
            case 3311002:
            case 3311003:
            case 3321006:
            case 3321007: {
                c.getPlayer().문양 = 3;
                break;
            }
            case 3321014:
            case 3321015:
            case 3321016:
            case 3321017:
            case 3321018:
            case 3321019:
            case 3321020:
            case 3321021:
            case 3321035:
            case 3321036:
            case 3321037:
            case 3321038:
            case 3321039:
            case 3321040:
            case 400031034:
            case 400031037:
            case 400031038:
            case 400031039:
            case 400031040:
            case 400031047:
            case 400031049:
            case 400031051: {
                c.getPlayer().문양 = 0;
                break;
            }
        }
        statups.put(SecondaryStat.CardinalMark, new Pair<Integer, Integer>(c.getPlayer().문양, 0));
        if (이전문양 == 3 && 이전문양 != c.getPlayer().문양 && c.getPlayer().getSkillLevel(3320008) > 0) {
            c.getPlayer().setSkillCustomInfo(3320008, 6L, 0L);
            SkillFactory.getSkill(3320008).getEffect(c.getPlayer().getSkillLevel(3320008)).applyTo(c.getPlayer(), 7000);
        }
        if (이전문양 != c.getPlayer().문양) {
            final int 쿨감량 = (c.getPlayer().getSkillLevel(3320000) > 0) ? 1000 : 500;
            int targetskill = 0;
            for (int i = 0; i < 5; ++i) {
                switch (i) {
                    case 0: {
                        targetskill = 3321036;
                        break;
                    }
                    case 1: {
                        targetskill = 3321014;
                        break;
                    }
                    case 2: {
                        targetskill = 3321012;
                        break;
                    }
                    case 3: {
                        targetskill = 3301008;
                        break;
                    }
                    case 4: {
                        targetskill = 3311010;
                        break;
                    }
                }
                if (c.getPlayer().skillisCooling(targetskill)) {
                    c.getPlayer().changeCooldown(targetskill, -쿨감량);
                }
            }
        }
        c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, null, c.getPlayer()));
    }

    public void handleRemainIncense(final int skillid, final boolean install1) {
        final MapleCharacter chr = this;
        if (chr.getBuffedValue(63111009)) {
            final int createcount = (skillid == 63001100 || skillid == 63101100 || skillid == 400031061) ? 1 : ((skillid == 63101104 || skillid == 400031064) ? 2 : ((skillid == 63111105) ? 6 : ((skillid == 63121102 || skillid == 63121103 || skillid == 63121141) ? 8 : 0)));
            final SecondaryStatEffect effect2 = SkillFactory.getSkill(63111009).getEffect(chr.getSkillLevel(63111009));
            int install2 = 0;
            final List<MapleMagicWreck> remove = new ArrayList<MapleMagicWreck>();
            for (final MapleMagicWreck mw : chr.getMap().getWrecks()) {
                if (mw.getChr().getId() == chr.getId() && mw.getSourceid() == 63111010) {
                    ++install2;
                    final Rectangle bounds = SkillFactory.getSkill(63111010).getEffect(chr.getSkillLevel(63111010)).calculateBoundingBox(mw.getTruePosition(), chr.isFacingLeft());
                    for (final MapleMonster monster2 : chr.getMap().getAllMonster()) {
                        if (bounds.contains(monster2.getPosition())) {
                            remove.add(mw);
                            break;
                        }
                    }
                }
            }
            if (install2 > 0 && !SkillFactory.getSkill(63111009).getSkillList2().contains(skillid)) {
                if (install2 > 0) {
                    chr.getMap().broadcastMessage(CField.getWreckAttack(chr, remove));
                    for (final MapleMagicWreck mw : remove) {
                        chr.getMap().RemoveMagicWreck(mw);
                    }
                }
            } else if (!install1 && !chr.skillisCooling(63111009) && createcount > 0) {
                final Rectangle bounds2 = SkillFactory.getSkill(63111010).getEffect(chr.getSkillLevel(63111010)).calculateBoundingBox(chr.getTruePosition(), chr.isFacingLeft());
                for (int i = 0; i < createcount; ++i) {
                    MapleFoothold fh = null;
                    int i2 = 0;
                    while (fh == null) {
                        fh = chr.getMap().getFootholds().getAllRelevants().get(Randomizer.rand(0, chr.getMap().getFootholds().getAllRelevants().size() - 1));
                        if (!bounds2.contains(fh.getPoint1()) && !bounds2.contains(fh.getPoint2())) {
                            fh = null;
                        }
                        if (++i2 >= 100) {
                            fh = chr.getMap().getFootholds().getAllRelevants().get(Randomizer.rand(0, chr.getMap().getFootholds().getAllRelevants().size() - 1));
                            break;
                        }
                    }
                    if (fh != null) {
                        final MapleMagicWreck mw2 = new MapleMagicWreck(chr, 63111010, new Point(Randomizer.rand(fh.getPoint1().x, fh.getPoint2().x), fh.getPoint1().y), effect2.getW() * 1000);
                        chr.getMap().spawnMagicWreck(mw2);
                    }
                }
                chr.addCooldown(63111009, System.currentTimeMillis(), 300L);
            }
        }
    }

    public void handlePossession(final int type) {
        SecondaryStatEffect effect = SkillFactory.getSkill(63101001).getEffect(this.getSkillLevel(63101001));
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        boolean givebuff = true;
        if (this.getSkillLevel(63120000) > 0) {
            effect = SkillFactory.getSkill(63120000).getEffect(this.getSkillLevel(63120000));
        }
        int add = (type == 1) ? effect.getY() : ((type == 2) ? effect.getX() : -100);
        if (this.getBuffedValue(400031062)) {
            add = ((type == 1) ? this.getBuffedEffect(400031062).getY() : ((type == 2) ? this.getBuffedEffect(400031062).getX() : -100));
        }
        if (this.getSkillCustomValue0(63101001) == (effect.getV() + 1) * 100 && type != 3) {
            givebuff = false;
        }
        if (givebuff) {
            this.addSkillCustomInfo(63101001, add);
            if (this.getSkillCustomValue0(63101001) > (effect.getV() + 1) * 100) {
                this.setSkillCustomInfo(63101001, (effect.getV() + 1) * 100, 0L);
            } else if (this.getSkillCustomValue0(63101001) < 0L) {
                this.setSkillCustomInfo(63101001, 0L, 0L);
            }
            statups.put(SecondaryStat.Malice, new Pair<Integer, Integer>((int) this.getSkillCustomValue0(63101001), 0));
            this.getClient().send(CWvsContext.BuffPacket.giveBuff(statups, null, this));
        }
    }

    public void handleStackskill(final int skillid, final boolean use) {
        final SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid));
        if (use) {
            this.addSkillCustomInfo(skillid, -1L);
            this.setSkillCustomInfo(skillid - 1, 0L, effect.getU() * 1000);
        } else if (this.getSkillCustomValue(skillid - 1) == null && this.getSkillCustomValue0(skillid) < effect.getW()) {
            this.addSkillCustomInfo(skillid, 1L);
            this.setSkillCustomInfo(skillid - 1, 0L, effect.getU() * 1000);
        }
        this.getClient().send(CField.getScatteringShot(effect.getSourceId(), (int) this.getSkillCustomValue0(skillid), effect.getW(), effect.getU() * 1000));
    }

    public boolean handleCainSkillCooldown(final int skillid) {
        boolean cool = true;
        int cskillid = 0;
        switch (skillid) {
            case 63100100: {
                cskillid = 63100002;
                break;
            }
            case 63101100: {
                cskillid = 63110001;
                break;
            }
            case 63101104: {
                cskillid = 63101004;
                break;
            }
            case 63121102:
            case 63121103: {
                cskillid = 63121002;
                break;
            }
            case 63110103:
            case 63111103: {
                cskillid = 63111003;
                break;
            }
            case 63121041: {
                return false;
            }
            case 63121141: {
                cskillid = 63121040;
                break;
            }
        }
        if (cskillid > 0) {
            if (this.getBuffedEffect(63101001) != null) {
                this.cancelEffect(this.getBuffedEffect(63101001));
            }
            cool = false;
            int cooltime = SkillFactory.getSkill(cskillid).getEffect(this.getSkillLevel(cskillid)).getCooldown(this);
            if (cooltime == 0) {
                cooltime = SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(cskillid)).getCooldown(this);
            }
            if (!this.skillisCooling(skillid) && !this.skillisCooling(cskillid)) {
                this.addCooldown(skillid, System.currentTimeMillis(), cooltime);
                this.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(skillid, cooltime));
            }
        }
        return cool;
    }

    public void handleNatureFriend() {
        final int skilllv = (this.getSkillLevel(80003070) > 0) ? this.getSkillLevel(80003070) : this.getSkillLevel(160010001);
        final int skillld = (this.getSkillLevel(80003070) > 0) ? 80003058 : 160010001;
        if (this.skillisCooling(skillld)) {
            return;
        }
        final SecondaryStatEffect effect = SkillFactory.getSkill(skillld).getEffect(skilllv);
        if (!this.getBuffedValue(80003070)) {
            this.setSkillCustomInfo(80003070, 0L, 0L);
        } else {
            this.addSkillCustomInfo(80003070, 1L);
        }
        if (this.getSkillCustomValue0(80003070) >= effect.getX()) {
            this.addCooldown(skillld, System.currentTimeMillis(), effect.getCooldown(this));
            this.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(skillld, effect.getCooldown(this)));
            this.cancelEffect(this.getBuffedEffect(80003070));
            SkillFactory.getSkill(skillld).getEffect(skilllv).applyTo(this);
        } else {
            SkillFactory.getSkill(80003070).getEffect(skilllv).applyTo(this);
        }
    }

    public void handleSerpent(final int type) {
        if (type == 1) {
            this.cancelEffectFromBuffStat(SecondaryStat.SerpentScrew, 400051015);
        }
    }

    public void handlePriorPrepaRation(final int skillid, final int type) {
        final SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid));
        if (!this.getBuffedValue(skillid) && !this.skillisCooling(skillid)) {
            if (type == 1) {
                this.addSkillCustomInfo(80003016, 1L);
                if (this.getSkillCustomValue0(80003016) >= effect.getX()) {
                    this.removeSkillCustomInfo(80003016);
                    this.addSkillCustomInfo(80003018, 1L);
                    SkillFactory.getSkill(80003018).getEffect(this.getSkillLevel(skillid)).applyTo(this);
                }
            } else if (type == 2) {
                this.addSkillCustomInfo(80003017, 1L);
                if (this.getSkillCustomValue0(80003017) >= effect.getY()) {
                    this.removeSkillCustomInfo(80003017);
                    this.addSkillCustomInfo(80003018, 1L);
                    SkillFactory.getSkill(80003018).getEffect(this.getSkillLevel(skillid)).applyTo(this);
                }
            }
            if (this.getSkillCustomValue0(80003018) >= effect.getW()) {
                this.removeSkillCustomInfo(80003016);
                this.removeSkillCustomInfo(80003017);
                this.removeSkillCustomInfo(80003018);
                this.cancelEffect(this.getBuffedEffect(80003018));
                SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)).applyTo(this);
                this.addCooldown(skillid, System.currentTimeMillis(), SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)).getCooldown(this));
                this.client.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)).getCooldown(this)));
            }
        }
    }

    public int getBullet() {
        return this.bullet;
    }

    public int getCylinderGauge() {
        return this.cylindergauge;
    }

    public void Cylinder(final int skillid) {
        final int MaxB = (this.getJob() == 3712) ? 6 : ((this.getJob() == 3711) ? 5 : ((this.getJob() == 3710) ? 4 : ((this.getJob() == 3700) ? 3 : 0)));
        if (MaxB != 0) {
            if (skillid == 0) {
                this.bullet = MaxB;
                this.cylindergauge = 0;
            } else {
                if (skillid == 37000010 || skillid == 37121004) {
                    this.bullet = MaxB;
                } else if (this.bullet > 0 && (skillid == 37111006 || skillid == 400011019 || skillid == 400011103 || skillid == 400011091 || skillid == 400011019 || skillid == 37001004 || skillid == 37000005 || skillid == 37000009 || skillid == 37100008 || (skillid >= 37120014 && skillid <= 37120019))) {
                    --this.bullet;
                } else if (this.bullet < MaxB && (skillid == 37100002 || skillid == 37110001 || skillid == 37110004 || skillid == 37101000 || skillid == 37100002 || skillid == 37110001 || skillid == 37110004 || skillid == 37101000)) {
                    if ((skillid == 37100002 || skillid == 37110004 || skillid == 37110001) && this.getSkillLevel(37120011) > 0) {
                        this.bullet += 2;
                    } else {
                        ++this.bullet;
                    }
                }
                if ((this.cylindergauge < MaxB && (skillid == 400011019 || skillid == 37000009 || skillid == 37100008)) || (skillid >= 37120014 && skillid <= 37120019) || skillid == 37111006) {
                    ++this.cylindergauge;
                } else if (skillid == 37001002 || skillid == 37000011 || skillid == 37000012 || skillid == 37000013) {
                    this.cylindergauge = 0;
                }
            }
            if (this.getBuffedValue(SecondaryStat.RWOverHeat) != null) {
                this.cylindergauge = 0;
            }
            if (this.cylindergauge > 6) {
                this.cylindergauge = 6;
            } else if (this.cylindergauge < 0) {
                this.cylindergauge = 0;
            }
            if (this.bullet > 6) {
                this.bullet = 6;
            } else if (this.bullet < 0) {
                this.bullet = 0;
            }
            this.CylinderBuff(skillid, false);
            if (skillid == 37001002 || skillid == 37000011 || skillid == 37000012 || skillid == 37000013) {
                SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)).applyTo(this);
            }
            if (this.bullet == 0) {
                this.Cylinder(37000010);
            }
        }
    }

    public void CylinderBuff(int skillid, final boolean gaugereset) {
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        if (gaugereset) {
            this.cylindergauge = 0;
        }
        if (skillid == 37121004 && !gaugereset) {
            SkillFactory.getSkill(37121004).getEffect(this.getSkillLevel(37121004)).applyTo(this);
        } else if (skillid == 400011103) {
            statups.put(SecondaryStat.RWCylinder, new Pair<Integer, Integer>(1, 0));
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)), this));
        } else {
            if (skillid == 0) {
                skillid = 37121004;
            }
            statups.put(SecondaryStat.RWCylinder, new Pair<Integer, Integer>(1, 0));
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)), this));
        }
    }

    public void givePPoint(final int skillid) {
        if (GameConstants.isKinesis(this.getJob())) {
            int MaxPpoint = (this.getJob() == 14200) ? 10 : ((this.getJob() == 14210) ? 15 : ((this.getJob() == 14211) ? 20 : 30));
            if (this.getSkillLevel(80000406) > 0) {
                MaxPpoint += this.getSkillLevel(80000406);
            }
            if (skillid <= 30) {
                this.PPoint += skillid;
            } else if (skillid != 142121032) {
                SecondaryStatEffect effects = SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid));
                if (effects.getPPCon() > 0) {
                    if (this.getBuffedValue(SecondaryStat.KinesisPsychicOver) != null) {
                        this.PPoint -= ((effects.getPPCon() <= 1) ? effects.getPPCon() : (effects.getPPCon() / 2));
                    } else {
                        this.PPoint -= effects.getPPCon();
                    }
                } else if (effects.getPPRecovery() > 0) {
                    this.PPoint += effects.getPPRecovery();
                } else if (skillid == 142120003 || skillid == 142101009 || skillid == 142120015 || skillid == 142001001) {
                    ++this.PPoint;
                } else if (skillid == 142001007) {
                    --this.PPoint;
                } else if (skillid == 400021048) {
                    this.PPoint -= 10;
                } else if (skillid == 142121030) {
                    this.PPoint = MaxPpoint;
                } else if (skillid == 142121008) {
                    this.PPoint += (MaxPpoint - this.PPoint) / 2;
                } else if (skillid == 400021074) {
                    this.PPoint -= 3;
                } else if (skillid == 400001051) {
                    effects = SkillFactory.getSkill(400001050).getEffect(this.getSkillLevel(400001050));
                    final int a = 3000 * effects.getY() / 10000;
                    this.PPoint += a;
                }
            }
            this.givePPoint((byte) 0);
        }
    }

    public void givePPoint(final byte count) {
        int MaxPpoint = (this.getJob() == 14200) ? 10 : ((this.getJob() == 14210) ? 15 : ((this.getJob() == 14211) ? 20 : 30));
        if (this.getSkillLevel(80000406) > 0) {
            MaxPpoint += this.getSkillLevel(80000406);
        }
        this.PPoint += count;
        if (this.PPoint < 0) {
            this.PPoint = 0;
        }
        if (MaxPpoint < this.PPoint) {
            this.PPoint = MaxPpoint;
        }
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        statups.put(SecondaryStat.KinesisPsychicPoint, new Pair<Integer, Integer>(this.PPoint, 0));
        this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, null, this));
    }

    public byte getDeathCount() {
        return this.deathcount;
    }

    public void setDeathCount(final byte de) {
        this.deathcount = de;
        this.getMap().broadcastMessage(CField.setDeathCount(this, this.deathcount));
        if (this.getMapId() == 450010500) {
            this.getClient().getSession().writeAndFlush((Object) CField.JinHillah(3, this, this.getMap()));
        } else if (this.getMapId() != 262031310 && this.getMapId() != 262030310 && this.getMapId() != 262031300 && this.getMapId() != 262030100 && this.getMapId() != 262031100 && this.getMapId() != 262030200 && this.getMapId() != 262031200) {
            this.getClient().getSession().writeAndFlush((Object) CField.getDeathCount(this.getDeathCount()));
        } else if (this.getMapId() == 262031300) {
            final int deathcouint = 15 - this.getDeathCount();
            this.getClient().send(CWvsContext.onFieldSetVariable("TotalDeathCount", "15"));
            this.getClient().send(CWvsContext.onFieldSetVariable("DeathCount", deathcouint + ""));
            final MapleMonster hardHilla = this.getMap().getMonsterById(8870100);
            if (hardHilla != null) {
                hardHilla.addSkillCustomInfo(8877100, 1L);
                if (hardHilla.getCustomValue0(8877100) >= 6L) {
                    hardHilla.setCustomInfo(8877100, 6, 0);
                }
                final List<Pair<MonsterStatus, MonsterStatusEffect>> stats = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                stats.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_HillaCount, new MonsterStatusEffect(114, 210000000, hardHilla.getCustomValue0(8877100))));
                hardHilla.applyMonsterBuff(hardHilla.getMap(), stats, MobSkillFactory.getMobSkill(114, 57));
            }
        }
        if (de > 0) {
            this.getMap().broadcastMessage(CField.showDeathCount(this, this.getDeathCount()));
        }
    }

    public List<Equip> getSymbol() {
        return this.symbol;
    }

    public void setSymbol(final List<Equip> symbol) {
        this.symbol = symbol;
    }

    public List<Triple<Skill, SkillEntry, Integer>> getLinkSkills() {
        return this.linkskills;
    }

    public List<Pair<SecondaryStat, SecondaryStatValueHolder>> getEffects() {
        return this.effects;
    }

    public void setEffects(final List<Pair<SecondaryStat, SecondaryStatValueHolder>> effects) {
        this.effects = effects;
    }

    public void elementalChargeHandler(final int attack, final int count) {
        if (this.elementalCharge < 5) {
            this.elementalCharge += count;
        }
        this.lastElementalCharge = attack;
        final Skill skill = SkillFactory.getSkill(1200014);
        final int skillLevel = this.getTotalSkillLevel(skill);
        SecondaryStatEffect effect = this.getBuffedEffect(SecondaryStat.ElementalCharge);
        if (effect == null) {
            effect = skill.getEffect(skillLevel);
        }
        if (effect.getSourceId() != 1220010 && this.getSkillLevel(1220010) > 0) {
            effect = SkillFactory.getSkill(1220010).getEffect(this.getSkillLevel(1220010));
        }
        effect.applyTo(this);
        if (this.getSkillLevel(400011052) > 0) {
            SkillFactory.getSkill(400011052).getEffect(this.getSkillLevel(400011052)).applyTo(this, false);
        }
    }

    public int getMparkexp() {
        return this.mparkexp;
    }

    public void setMparkexp(final int mparkexp) {
        this.mparkexp = mparkexp;
    }

    public int getMparkcount() {
        return this.mparkcount;
    }

    public void setMparkcount(final int mparkcount) {
        this.mparkcount = mparkcount;
    }

    public int getMparkkillcount() {
        return this.mparkkillcount;
    }

    public void setMparkkillcount(final int mparkkillcount) {
        this.mparkkillcount = mparkkillcount;
    }

    public boolean isMparkCharged() {
        return this.mparkCharged;
    }

    public void setMparkCharged(final boolean mparkCharged) {
        this.mparkCharged = mparkCharged;
    }

    public void removeKeyValue(final int type, final String key) {
        final String questInfo = this.getInfoQuest(type);
        if (questInfo == null) {
            return;
        }
        final String[] split;
        final String[] data = split = questInfo.split(";");
        for (final String s : split) {
            if (s.startsWith(key + "=")) {
                final String newkey = questInfo.replace(s + ";", "");
                this.updateInfoQuest(type, newkey);
                return;
            }
        }
        this.updateInfoQuest(type, questInfo);
    }

    public void removeKeyValue(final int type) {
        final MapleQuest quest = MapleQuest.getInstance(type);
        if (quest == null) {
            return;
        }
        this.updateInfoQuest(type, "");
        this.questinfo.remove(quest);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("DELETE FROM questinfo WHERE characterid = " + this.getId() + " AND quest = ?");
            ps.setInt(1, type);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex2) {
            }
        }
    }

    public void setKeyValue(final int type, final String key, final String value) {
        final String questInfo = this.getInfoQuest(type);
        if (questInfo == null) {
            this.updateInfoQuest(type, key + "=" + value + ";");
            return;
        }
        final String[] split;
        final String[] data = split = questInfo.split(";");
        for (final String s : split) {
            if (s.startsWith(key + "=")) {
                final String newkey = questInfo.replace(s, key + "=" + value);
                this.updateInfoQuest(type, newkey);
                return;
            }
        }
        this.updateInfoQuest(type, questInfo + key + "=" + value + ";");
    }

    public String getKeyValueStr(final int type, final String key) {
        final String questInfo = this.getInfoQuest(type);
        if (questInfo == null) {
            return null;
        }
        final String[] split;
        final String[] data = split = questInfo.split(";");
        for (final String s : split) {
            if (s.startsWith(key + "=")) {
                final String newkey = s.replace(key + "=", "");
                final String newkey2 = newkey.replace(";", "");
                return newkey2;
            }
        }
        return null;
    }

    public long getKeyValue(final int type, final String key) {
        final String questInfo = this.getInfoQuest(type);
        if (questInfo == null) {
            return -1L;
        }
        final String[] split;
        final String[] data = split = questInfo.split(";");
        for (final String s : split) {
            if (s.startsWith(key + "=")) {
                final String newkey = s.replace(key + "=", "");
                final String newkey2 = newkey.replace(";", "");
                final long dd = Long.valueOf(newkey2);
                return dd;
            }
        }
        return -1L;
    }

    public int getBHGCCount() {
        return this.BHGCCount;
    }

    public void setBHGCCount(final int bHGCCount) {
        this.BHGCCount = bHGCCount;
    }

    public int getHowlingGaleCount() {
        return this.HowlingGaleCount;
    }

    public void setHowlingGaleCount(final int howlingGaleCount) {
        this.HowlingGaleCount = howlingGaleCount;
    }

    public List<Item> getAuctionitems() {
        return this.auctionitems;
    }

    public void setAuctionitems(final List<Item> auctionitems) {
        this.auctionitems = auctionitems;
    }

    public int getSlowAttackCount() {
        return this.slowAttackCount;
    }

    public void setSlowAttackCount(final int slowAttackCount) {
        this.slowAttackCount = slowAttackCount;
    }

    public int getYoyoCount() {
        return this.YoyoCount;
    }

    public void setYoyoCount(final int yoyoCount) {
        this.YoyoCount = yoyoCount;
    }

    public MapleHaku getHaku() {
        return this.haku;
    }

    public void setHaku(final MapleHaku haku) {
        this.haku = haku;
    }

    public int getListonation() {
        return this.listonation;
    }

    public void setListonation(final int listonation) {
        this.listonation = listonation;
    }

    public int getBeholderSkill1() {
        return this.beholderSkill1;
    }

    public void setBeholderSkill1(final int beholderSkill1) {
        this.beholderSkill1 = beholderSkill1;
    }

    public int getBeholderSkill2() {
        return this.beholderSkill2;
    }

    public void setBeholderSkill2(final int beholderSkill2) {
        this.beholderSkill2 = beholderSkill2;
    }

    public int getReinCarnation() {
        return this.reinCarnation;
    }

    public void setReinCarnation(final int reinCarnation) {
        this.reinCarnation = reinCarnation;
    }

    public byte getPoisonStack() {
        return this.poisonStack;
    }

    public void setPoisonStack(final byte poisonStack) {
        this.poisonStack = poisonStack;
    }

    public byte getInfinity() {
        return this.infinity;
    }

    public void setInfinity(final byte infinity) {
        this.infinity = infinity;
    }

    public byte getHolyPountin() {
        return this.holyPountin;
    }

    public void setHolyPountin(final byte holyPountin) {
        this.holyPountin = holyPountin;
    }

    public int getHolyPountinOid() {
        return this.holyPountinOid;
    }

    public void setHolyPountinOid(final int holyPountinOid) {
        this.holyPountinOid = holyPountinOid;
    }

    public byte getBlessingAnsanble() {
        return this.blessingAnsanble;
    }

    public void setBlessingAnsanble(final byte blessingAnsanble) {
        this.blessingAnsanble = blessingAnsanble;
    }

    public byte getQuiverType() {
        return this.quiverType;
    }

    public void setQuiverType(final byte quiverType) {
        this.quiverType = quiverType;
    }

    public int[] getRestArrow() {
        return this.RestArrow;
    }

    public int getBarrier() {
        return this.barrier;
    }

    public void setBarrier(final int barrier) {
        this.barrier = barrier;
    }

    public byte getFlip() {
        return this.flip;
    }

    public void setFlip(final byte flip) {
        this.flip = flip;
    }

    public int getDice() {
        return this.dice;
    }

    public void setDice(final int dice) {
        this.dice = dice;
    }

    public byte getUnityofPower() {
        return this.unityofPower;
    }

    public void setUnityofPower(final byte unityofPower) {
        this.unityofPower = unityofPower;
    }

    public byte getConcentration() {
        return this.concentration;
    }

    public void setConcentration(final byte concentration) {
        this.concentration = concentration;
    }

    public byte getMortalBlow() {
        return this.mortalBlow;
    }

    public void setMortalBlow(final byte mortalBlow) {
        this.mortalBlow = mortalBlow;
    }

    public List<MapleMapItem> getPickPocket() {
        return this.pickPocket;
    }

    public void addPickPocket(final MapleMapItem pickPocket) {
        this.pickPocket.add(pickPocket);
    }

    public void RemovePickPocket(final MapleMapItem pickPocket) {
        this.pickPocket.remove(pickPocket);
    }

    public byte getHolyMagicShell() {
        return this.holyMagicShell;
    }

    public void setHolyMagicShell(final byte holyMagicShell) {
        this.holyMagicShell = holyMagicShell;
    }

    public byte getAntiMagicShell() {
        return this.antiMagicShell;
    }

    public void setAntiMagicShell(final byte antiMagicShell) {
        this.antiMagicShell = antiMagicShell;
    }

    public byte getBlessofDarkness() {
        return this.blessofDarkness;
    }

    public void setBlessofDarkness(final byte blessofDarkness) {
        this.blessofDarkness = blessofDarkness;
    }

    public byte getDeath() {
        return this.death;
    }

    public void setDeath(final byte death) {
        this.death = death;
    }

    public byte getRoyalStack() {
        return this.royalStack;
    }

    public void setRoyalStack(final byte royalStack) {
        this.royalStack = royalStack;
    }

    public short getKaiserCombo() {
        return this.kaiserCombo;
    }

    public void setKaiserCombo(final short kaiserCombo) {
        this.kaiserCombo = (short) Math.min(1000, kaiserCombo);
    }

    public List<Integer> getWeaponChanges() {
        return this.weaponChanges;
    }

    public void setWeaponChanges(final List<Integer> weaponChanges) {
        this.weaponChanges = weaponChanges;
    }

    public List<Integer> getWeaponChanges2() {
        return this.weaponChanges2;
    }

    public void setWeaponChanges2(final List<Integer> weaponChanges2) {
        this.weaponChanges2 = weaponChanges2;
    }

    public CalcDamage getCalcDamage() {
        return this.calcDamage;
    }

    public void setCalcDamage(final CalcDamage calcDamage) {
        this.calcDamage = calcDamage;
    }

    public int getEnergyBurst() {
        return this.energyBurst;
    }

    public void setEnergyBurst(final int energyBurst) {
        this.energyBurst = energyBurst;
    }

    public int getMarkofPhantom() {
        return this.markofPhantom;
    }

    public void setMarkofPhantom(final int markofPhantom) {
        this.markofPhantom = markofPhantom;
    }

    public int getUltimateDriverCount() {
        return this.ultimateDriverCount;
    }

    public void setUltimateDriverCount(final int ultimateDriverCount) {
        this.ultimateDriverCount = ultimateDriverCount;
    }

    public int getMarkOfPhantomOid() {
        return this.markOfPhantomOid;
    }

    public void setMarkOfPhantomOid(final int markOfPhantomOid) {
        this.markOfPhantomOid = markOfPhantomOid;
    }

    public int getRhoAias() {
        return this.rhoAias;
    }

    public void setRhoAias(final int rhoAias) {
        this.rhoAias = rhoAias;
    }

    public int getPerfusion() {
        return this.perfusion;
    }

    public void setPerfusion(final int perfusion) {
        this.perfusion = perfusion;
    }

    public MapleFieldAttackObj getFao() {
        return this.fao;
    }

    public void setFao(final MapleFieldAttackObj fao) {
        this.fao = fao;
    }

    public int getBlackMagicAlter() {
        return this.blackMagicAlter;
    }

    public void setBlackMagicAlter(final int blackMagicAlter) {
        this.blackMagicAlter = blackMagicAlter;
    }

    public int getWildGrenadierCount() {
        return this.WildGrenadierCount;
    }

    public void setWildGrenadierCount(final int wildGrenadierCount) {
        this.WildGrenadierCount = wildGrenadierCount;
    }

    public int getVerseOfRelicsCount() {
        return this.VerseOfRelicsCount;
    }

    public void setVerseOfRelicsCount(final int VerseOfRelics) {
        this.VerseOfRelicsCount = VerseOfRelics;
    }

    public int getJudgementType() {
        return this.judgementType;
    }

    public void setJudgementType(final int judgementType) {
        this.judgementType = judgementType;
    }

    public int getAllUnion() {
        int ret = 0;
        for (final MapleUnion union : this.getUnions().getUnions()) {
            ret += union.getLevel();
        }
        return ret;
    }

    public int getAllStarForce() {
        int starforce = 0;
        for (final Item item : this.getInventory(MapleInventoryType.EQUIPPED)) {
            final Equip eq = (Equip) item;
            starforce += eq.getEnhance();
            if (GameConstants.isLongcoat(item.getItemId())) {
                starforce += eq.getEnhance();
            }
        }
        return starforce;
    }

    public long getUnionDamage() {
        long rate = 0L;
        for (final MapleUnion union : this.getUnions().getUnions()) {
            if (union.getName().contains(union.getName()) && union.getPosition() != -1) {
                int hob = 0;
                final double rate2 = GameConstants.UnionAttackerRate(union.getLevel());
                final int a55 = union.getLevel() * union.getLevel() * union.getLevel();
                final int ab = (int) (rate2 * a55) + 12500;
                final int a56 = union.getStarForce() * union.getStarForce() * union.getStarForce();
                final int a57 = union.getStarForce() * union.getStarForce();
                final int abc = (int) (GameConstants.UnionStarForceRate(union.getStarForce()) * a56 + GameConstants.UnionStarForceRate2(union.getStarForce()) * a57 + (GameConstants.UnionStarForceRate3(union.getStarForce()) * union.getStarForce() + GameConstants.UnionStarForceRate4(union.getStarForce())));
                hob = ab + abc;
                rate += hob;
            }
        }
        return rate;
    }

    public int getUnionCoin() {
        if (this.client.getKeyValue("UnionCoin") == null) {
            this.client.setKeyValue("UnionCoin", "0");
        }
        return Integer.parseInt(this.client.getKeyValue("UnionCoin"));
    }

    public void setUnionCoin(final int coin) {
        this.client.setKeyValue("UnionCoin", String.valueOf(coin));
    }

    public int getUnionCoinNujuk() {
        if (this.client.getKeyValue("UnionCoinNujuk") == null) {
            this.client.setKeyValue("UnionCoinNujuk", "0");
        }
        return Integer.parseInt(this.client.getKeyValue("UnionCoinNujuk"));
    }

    public void setUnionCoinNujuk(final long coin) {
        this.client.setKeyValue("UnionCoinNujuk", String.valueOf(coin));
    }

    public long getUnionAllNujuk() {
        if (this.client.getKeyValue("UnionAllNujuk") == null) {
            this.client.setKeyValue("UnionAllNujuk", "0");
        }
        return Long.parseLong(this.client.getKeyValue("UnionAllNujuk"));
    }

    public void setUnionAllNujuk(final long Nujuk) {
        this.client.setKeyValue("UnionAllNujuk", String.valueOf(Nujuk));
    }

    public long getUnionNujuk() {
        if (this.client.getKeyValue("UnionNujuk") == null) {
            this.client.setKeyValue("UnionNujuk", "0");
        }
        return Long.parseLong(this.client.getKeyValue("UnionNujuk"));
    }

    public void setUnionNujuk(long Nujuk) {
        if (Nujuk >= 2500000000000L) {
            Nujuk = 2500000000000L;
        }
        this.client.setKeyValue("UnionNujuk", String.valueOf(Nujuk));
    }

    public long getUnionEndTime() {
        if (this.client.getKeyValue("UnionEndTime") == null) {
            this.client.setKeyValue("UnionEndTime", "0");
        }
        return Long.parseLong(this.client.getKeyValue("UnionEndTime"));
    }

    public void setUnionEndTime(final long time) {
        this.client.setKeyValue("UnionEndTime", String.valueOf(time));
    }

    public long getUnionEnterTime() {
        if (this.client.getKeyValue("UnionEnterTime") == null) {
            this.client.setKeyValue("UnionEnterTime", "0");
        }
        return Long.parseLong(this.client.getKeyValue("UnionEnterTime"));
    }

    public void setUnionEnterTime(final long time) {
        this.client.setKeyValue("UnionEnterTime", String.valueOf(time));
    }

    public int getAllUnionCoin() {
        if (this.client.getKeyValue("유니온코인") == null) {
            this.client.setKeyValue("유니온코인", "0");
        }
        return Integer.parseInt(this.client.getKeyValue("유니온코인"));
    }

    public void setAllUnionCoin(final int a) {
        this.setKeyValue(500629, "point", "" + a + "");
        this.client.setKeyValue("유니온코인", a + "");
    }

    public void AddAllUnionCoin(final int a) {
        if (this.client.getKeyValue("유니온코인") == null) {
            this.client.setKeyValue("유니온코인", "0");
        }
        this.setKeyValue(500629, "point", "" + (Integer.parseInt(this.client.getKeyValue("유니온코인")) + a) + "");
        this.client.setKeyValue("유니온코인", Integer.parseInt(this.client.getKeyValue("유니온코인")) + a + "");
    }

    public void RefreshUnionRaid(final boolean enter) {
        final Calendar ocal = Calendar.getInstance();
        String years = "" + ocal.get(1) + "";
        String months = "" + (ocal.get(2) + 1) + "";
        String days = "" + ocal.get(5) + "";
        String hours = "" + ocal.get(11) + "";
        String mins = "" + ocal.get(12) + "";
        String secs = "" + ocal.get(13) + "";
        final int yeal = ocal.get(1);
        final int month = ocal.get(2) + 1;
        final int dayt = ocal.get(5);
        final int hour = ocal.get(11);
        final int min = ocal.get(12);
        final int sec = ocal.get(13);
        if (month < 10) {
            months = "0" + month;
        }
        if (dayt < 10) {
            days = "0" + dayt;
        }
        if (hour < 10) {
            hours = "0" + hour;
        }
        if (min < 10) {
            mins = "0" + min;
        }
        if (sec < 10) {
            secs = "0" + sec;
        }
        if (this.getUnionEndTime() > 0L) {
            final long time = (System.currentTimeMillis() - this.getUnionEndTime()) / 1000L;
            final long attackrate = time * this.getUnionDamage();
            final long allnujuk = this.getUnionAllNujuk();
            final long nujuk = this.getUnionNujuk();
            final int coin = this.getUnionCoin();
            final int coing = (int) this.getUnionEnterTime();
            if (nujuk < 2500000000000L) {
                this.setUnionNujuk(nujuk + attackrate);
            }
            this.setUnionAllNujuk(allnujuk + attackrate);
            if ((allnujuk + nujuk) / 100000000000L - coing != 0L) {
                final int a = (int) ((allnujuk + nujuk) / 100000000000L - coing);
                this.setUnionCoin(coin + a);
                this.setUnionEnterTime(coing + a);
            }
            if (nujuk + allnujuk >= 10000000000000L) {
                final int a = (int) ((allnujuk + nujuk) / 100000000000L - this.getUnionEnterTime());
                this.setUnionCoin(this.getUnionCoin() + a);
                this.setUnionNujuk(0L);
                this.setUnionAllNujuk(0L);
                this.setUnionEnterTime(0L);
                if (Integer.parseInt(this.client.getKeyValue("UnionLaidLevel")) < 5) {
                    this.client.setKeyValue("UnionLaidLevel", "" + (Integer.parseInt(this.client.getKeyValue("UnionLaidLevel")) + 1) + "");
                } else {
                    this.client.setKeyValue("UnionLaidLevel", "1");
                }
            }
            years = "" + yeal % 100 + "";
            this.setUnionEndTime(System.currentTimeMillis());
            if (this.getUnionCoin() >= GameConstants.UnionMaxCoin(this)) {
                if (enter) {
                    this.updateInfoQuest(18098, "lastTime=" + years + months + days + hours + mins + secs + ";coin=" + GameConstants.UnionMaxCoin(this) + "");
                    this.client.send(SLFCGPacket.UnionCoinMax(GameConstants.UnionMaxCoin(this)));
                }
                this.setUnionCoin(GameConstants.UnionMaxCoin(this));
            }
        }
    }

    public void setLastCharGuildId(final int a) {
        this.lastCharGuildId = a;
    }

    public int getLastCharGuildId() {
        return this.lastCharGuildId;
    }

    public void removeAllEquip(final int id, final boolean show) {
        MapleInventoryType type = GameConstants.getInventoryType(id);
        int possessed = this.getInventory(type).countById(id);
        if (possessed > 0) {
            MapleInventoryManipulator.removeById(this.getClient(), type, id, possessed, true, false);
            if (show) {
                this.getClient().getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) (-possessed), true));
            }
        }
        if (type == MapleInventoryType.EQUIP) {
            type = MapleInventoryType.EQUIPPED;
            possessed = this.getInventory(type).countById(id);
            if (possessed > 0) {
                final Item equip = this.getInventory(type).findById(id);
                if (equip != null) {
                    this.getInventory(type).removeSlot(equip.getPosition());
                    this.equipChanged();
                    this.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.clearInventoryItem(MapleInventoryType.EQUIP, equip.getPosition(), false));
                }
            }
        }
    }

    public void LoadPlatformerRecords() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        this.PfRecords.clear();
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM platformerreocrd WHERE cid = ? ORDER BY stage ASC");
            ps.setInt(1, this.id);
            rs = ps.executeQuery();
            while (rs.next()) {
                final int Stage = rs.getInt("stage");
                final int ClearTime = rs.getInt("cleartime");
                final int Stars = rs.getInt("star");
                this.PfRecords.add(new PlatformerRecord(Stage, ClearTime, Stars));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex2) {
            }
        }
    }

    public void SavePlatformerRecords() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            for (final PlatformerRecord rec : this.PfRecords) {
                ps = con.prepareStatement("SELECT * FROM platformerreocrd WHERE cid = ? AND stage = ?");
                ps.setInt(1, this.id);
                ps.setInt(2, rec.getStage());
                rs = ps.executeQuery();
                if (rs.next()) {
                    ps = con.prepareStatement("UPDATE platformerreocrd SET cleartime = ?, star = ? WHERE stage = ?");
                    ps.setInt(1, rec.getClearTime());
                    ps.setInt(2, rec.getStars());
                    ps.setInt(3, rec.getStage());
                    ps.executeUpdate();
                } else {
                    this.SaveNewRecord(con, ps, rec);
                }
                ps.close();
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void setElementalCharge(final int elementalCharge) {
        this.elementalCharge = elementalCharge;
    }

    public int getElementalCharge() {
        return this.elementalCharge;
    }

    public int getLastElementalCharge() {
        return this.lastElementalCharge;
    }

    public List<PlatformerRecord> getPlatformerRecords() {
        return this.PfRecords;
    }

    public void SaveNewRecord(final Connection con, PreparedStatement ps, final PlatformerRecord rec) {
        try {
            ps = con.prepareStatement("INSERT INTO platformerreocrd (cid, stage, cleartime, star) VALUES (?, ?, ?, ?)");
            ps.setInt(1, this.id);
            ps.setInt(2, rec.getStage());
            ps.setInt(3, rec.getClearTime());
            ps.setInt(4, rec.getStars());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void RegisterPlatformerRecord(final int Stage) {
        final int time = (int) ((System.currentTimeMillis() - this.PlatformerStageEnter) / 1000L);
        int star = 0;
        if (time <= GameConstants.StarInfo[Stage - 1][0]) {
            star = 3;
        } else if (time <= GameConstants.StarInfo[Stage - 1][1]) {
            star = 2;
        } else if (time <= GameConstants.StarInfo[Stage - 1][2]) {
            star = 1;
        }
        if (this.PfRecords.size() < Stage) {
            this.PfRecords.add(new PlatformerRecord(Stage, time, star));
        } else {
            for (final PlatformerRecord record : this.PfRecords) {
                if (record.getStage() == Stage && (record.getClearTime() > time || record.getStars() < star)) {
                    record.setClearTime(time);
                    record.setStars(star);
                }
            }
        }
        if (star > 0) {
            for (final MapleCharacter chr : this.map.getAllCharactersThreadsafe()) {
                chr.dropMessage(-1, this.name + "님이 별 " + star + "개로 스테이지를 클리어하였습니다!");
            }
            if (this.getKeyValue(20190409, "Stage_" + Stage + "_Received") == -1L) {
                this.setKeyValue(20190409, "Stage_" + Stage + "_Received", "0");
            }
            if (this.getKeyValue(20190409, "Stage_" + Stage + "_Received") == 0L) {
                this.setKeyValue(20190409, "Stage_" + Stage + "_Received", "1");
            }
        }
        this.SavePlatformerRecords();
    }

    public void setWeddingGive(final int l) {
        this.weddingGiftGive = l;
    }

    public void cancelTimer() {
        if (this.MulungTimer != null) {
            this.MulungTimerTask.cancel();
            this.MulungTimer.cancel();
            this.MulungTimer = null;
            this.MulungTimerTask = null;
        }
    }

    public int getDojoStartTime() {
        return this.dojoStartTime;
    }

    public void setDojoStartTime(final int dojoStartTime) {
        this.dojoStartTime = dojoStartTime;
    }

    public boolean getDojoStop() {
        return this.dojoStop;
    }

    public void setDojoStop(final boolean stop) {
        this.dojoStop = stop;
    }

    public int getWeddingGive() {
        return this.weddingGiftGive;
    }

    public long getDojoStopTime() {
        return this.dojoStopTime;
    }

    public void setDojoStopTime(final long dojoStopTime) {
        this.dojoStopTime = dojoStopTime;
    }

    public long getDojoCoolTime() {
        return this.dojoCoolTime;
    }

    public void setDojoCoolTime(final int dojoCoolTime) {
        this.dojoCoolTime = dojoCoolTime;
    }

    public boolean isDeadEffect() {
        return this.deadEffect;
    }

    public void setDeadEffect(final boolean deadEffect) {
        this.deadEffect = deadEffect;
    }

    public void applySkill(final int skillid, final int level) {
        SkillFactory.getSkill(skillid).getEffect(level).applyTo(this, true);
    }

    public int getZeroCubePosition() {
        return this.zeroCubePosition;
    }

    public void setZeroCubePosition(final int zeroCubePosition) {
        this.zeroCubePosition = zeroCubePosition;
    }

    public Pair<Integer, Integer> getRecipe() {
        return this.recipe;
    }

    public void setRecipe(final Pair<Integer, Integer> recipe) {
        this.recipe = recipe;
    }

    public int getTransformCooldown() {
        return this.transformCooldown;
    }

    public void setTransformCooldown(final int transformCooldown) {
        this.transformCooldown = transformCooldown;
    }

    public Set<MapleMapObject> getVisibleMapObjects() {
        return this.visibleMapObjects;
    }

    public void setVisibleMapObjects(final Set<MapleMapObject> visibleMapObjects) {
        this.visibleMapObjects = visibleMapObjects;
    }

    public Map<SecondaryStat, MapleDiseases> getDiseases() {
        return this.diseases;
    }

    public void setDiseases(final Map<SecondaryStat, MapleDiseases> diseases) {
        this.diseases = diseases;
    }

    public boolean isNoneDestroy() {
        return this.noneDestroy;
    }

    public void setNoneDestroy(final boolean noneDestroy) {
        this.noneDestroy = noneDestroy;
    }

    public List<Integer> getPosionNovas() {
        return this.posionNovas;
    }
    
    public long getDamageMeter() {
        return damageMeter;
    }

    public void setDamageMeter(long damageMeter) {
        this.damageMeter = damageMeter;
    }

    public void setPosionNovas(final List<Integer> posionNovas) {
        this.posionNovas = posionNovas;
    }

    public int getLastBossId() {
        return this.lastBossId;
    }

    public void setLastBossId(final int lastBossId) {
        this.lastBossId = lastBossId;
    }

    public int getMoonGauge() {
        return this.moonGauge;
    }

    public void setMoonGauge(final int lunaGauge) {
        this.moonGauge = lunaGauge;
    }

    public OneCardGame getOneCardInstance() {
        return this.oneCardInstance;
    }

    public void setOneCardInstance(final OneCardGame oneCardInstance) {
        this.oneCardInstance = oneCardInstance;
    }

    public MultiYutGame getMultiYutInstance() {
        return this.multiYutInstance;
    }

    public void setMultiYutInstance(final MultiYutGame multiYutInstance) {
        this.multiYutInstance = multiYutInstance;
    }

    public ColorInvitationCard getColorCardInstance() {
        return this.ColorCardInstance;
    }

    public void setColorCardInstance(final ColorInvitationCard ColorCardInstance) {
        this.ColorCardInstance = ColorCardInstance;
    }

    public boolean isOneMoreChance() {
        return this.oneMoreChance;
    }

    public void setOneMoreChance(final boolean oneMoreChance) {
        this.oneMoreChance = oneMoreChance;
    }

    public List<Integer> getExceptionList() {
        return this.exceptionList;
    }

    public void setExceptionList(final List<Integer> exceptionList) {
        this.exceptionList = exceptionList;
    }

    public int getTrinity() {
        return this.trinity;
    }

    public void setTrinity(final int trinity) {
        this.trinity = trinity;
    }

    public UnionList getUnions() {
        return this.unions;
    }

    public void setUnions(final UnionList unions) {
        this.unions = unions;
    }

    public Map<String, String> getKeyValues() {
        return this.keyValues;
    }

    public String getV(final String k) {
        if (this.keyValues.containsKey(k)) {
            return this.keyValues.get(k);
        }
        return null;
    }

    public void addKV(final String k, final String v) {
        this.keyValues.put(k, v);
    }

    public void removeV(final String k) {
        this.keyValues.remove(k);
    }

    public Point getSpecialChairPoint() {
        return this.specialChairPoint;
    }

    public void setSpecialChairPoint(final Point point) {
        this.specialChairPoint = point;
    }

    public void setNettPyramid(final MapleNettPyramid mnp) {
        this.NettPyramid = mnp;
    }

    public MapleNettPyramid getNettPyramid() {
        return this.NettPyramid;
    }

    public final boolean isLeader() {
        return this.getParty() != null && this.getParty().getLeader().getId() == this.getId();
    }

    public void Message(final String msg) {
        this.client.getSession().writeAndFlush((Object) CField.getGameMessage(8, msg));
    }

    public void changeMap(final MapleMap to) {
        this.changeMapInternal(to, to.getPortal(0).getPosition(), CField.getWarpToMap(to, 0, this), to.getPortal(0), false);
    }

    public void message(final String msg) {
        this.client.getSession().writeAndFlush((Object) CWvsContext.serverNotice(5, this.name, msg));
    }

    public final MapleMap getWarpMap(final int map) {
        if (this.getEventInstance() != null) {
            return this.getEventInstance().getMapFactory().getMap(map);
        }
        return ChannelServer.getInstance(this.client.getChannel()).getMapFactory().getMap(map);
    }

    public int getSpiritGuard() {
        return this.spiritGuard;
    }

    public void setSpiritGuard(final int spiritGuard) {
        this.spiritGuard = spiritGuard;
    }

    public List<MapleMannequin> getHairRoom() {
        return this.hairRoom;
    }

    public void setHairRoom(final List<MapleMannequin> hairRoom) {
        this.hairRoom = hairRoom;
    }

    public List<MapleMannequin> getFaceRoom() {
        return this.faceRoom;
    }

    public void setFaceRoom(final List<MapleMannequin> faceRoom) {
        this.faceRoom = faceRoom;
    }

    public List<VMatrix> getMatrixs() {
        return this.matrixs;
    }

    public void setMatrixs(final List<VMatrix> matrixs) {
        this.matrixs = matrixs;
    }

    public DefenseTowerWave getDefenseTowerWave() {
        return this.defenseTowerWave;
    }

    public void setDefenseTowerWave(final DefenseTowerWave defenseTowerWave) {
        this.defenseTowerWave = defenseTowerWave;
    }

    public BountyHunting getBountyHunting() {
        return this.bountyHunting;
    }

    public void setBountyHunting(final BountyHunting bountyhunting) {
        this.bountyHunting = bountyhunting;
    }

    public int getBlitzShield() {
        return this.blitzShield;
    }

    public void setBlitzShield(final int blitzShield) {
        this.blitzShield = blitzShield;
    }

    public int[] getDeathCounts() {
        return this.deathCounts;
    }

    public void setDeathCounts(final int[] deathCounts) {
        this.deathCounts = deathCounts;
    }

    public void resetDeathCounts() {
        for (int i = 0; i < this.deathCounts.length; ++i) {
            this.deathCounts[i] = 1;
        }
        this.getMap().broadcastMessage(CField.JinHillah(10, this, this.getMap()));
    }

    public int liveCounts() {
        int c = 0;
        for (int i = 0; i < this.deathCounts.length; ++i) {
            if (this.deathCounts[i] == 1) {
                ++c;
            }
        }
        return c;
    }

    public int DeadCounts() {
        int c = 0;
        for (int i = 0; i < this.deathCounts.length; ++i) {
            if (this.deathCounts[i] == 2 || this.deathCounts[i] == 0) {
                ++c;
            }
        }
        return c;
    }

    public int getCreateDate() {
        if (this.createDate > 0) {
            return this.createDate;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM characters where `id` = ?");
            ps.setInt(1, this.id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("createdate");
                if (ts != null) {
                    this.createDate = ts.toLocalDateTime().getYear() * 1000 + ts.toLocalDateTime().getMonth().getValue() * 100 + ts.toLocalDateTime().getDayOfMonth();
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return this.createDate;
    }

    public void setCreateDate(final int createDate) {
        this.createDate = createDate;
    }

    public boolean isUseBuffFreezer() {
        return this.useBuffFreezer;
    }

    public void setUseBuffFreezer(final boolean useBuffFreezer) {
        this.useBuffFreezer = useBuffFreezer;
    }

    public void setSoulMP(final Equip weapon) {
        if (weapon != null) {
            final int soulSkillID = weapon.getSoulSkill();
            final Skill soulSkill = SkillFactory.getSkill(soulSkillID);
            if (soulSkill != null && soulSkillID != 0) {
                if (this.getSkillLevel(soulSkillID) == 0) {
                    this.changeSkillLevel(soulSkill, (byte) 1, (byte) 1);
                }
                final SecondaryStatEffect effect = soulSkill.getEffect(1);
                if (this.getBuffedEffect(SecondaryStat.SoulMP) != null) {
                    final int soulCount = this.getBuffedValue(SecondaryStat.SoulMP);
                    if (soulCount < 1000) {
                        this.setBuffedValue(SecondaryStat.SoulMP, soulCount + Randomizer.rand(1, 3));
                        final Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                        localstatups.put(SecondaryStat.SoulMP, new Pair<Integer, Integer>(soulCount + Randomizer.rand(1, 3), 0));
                        this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, effect, this));
                        this.map.broadcastMessage(CWvsContext.BuffPacket.giveForeignBuff(this, localstatups, effect));
                    }
                    if (this.getBuffedValue(SecondaryStat.SoulMP) >= effect.getSoulMPCon() && this.getCooldownLimit(soulSkillID) == 0L && this.getBuffedEffect(SecondaryStat.FullSoulMP) == null) {
                        final Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                        localstatups.put(SecondaryStat.FullSoulMP, new Pair<Integer, Integer>(0, 0));
                        this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, effect, this));
                        this.map.broadcastMessage(CWvsContext.BuffPacket.giveForeignBuff(this, localstatups, effect));
                        for (final Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup : localstatups.entrySet()) {
                            this.effects.add(new Pair<SecondaryStat, SecondaryStatValueHolder>(statup.getKey(), new SecondaryStatValueHolder(effect, System.currentTimeMillis(), this.getBuffedValue(SecondaryStat.SoulMP), 0, this.getId(), new ArrayList<Pair<Integer, Integer>>(), new ArrayList<Pair<Integer, Integer>>())));
                        }
                        this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this, true, false));
                        this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this, false, true));
                    }
                } else {
                    final Map<SecondaryStat, Pair<Integer, Integer>> localstatups2 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    localstatups2.put(SecondaryStat.SoulMP, new Pair<Integer, Integer>(0, 0));
                    this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups2, effect, this));
                    this.map.broadcastMessage(CWvsContext.BuffPacket.giveForeignBuff(this, localstatups2, effect));
                    for (final Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup2 : localstatups2.entrySet()) {
                        this.effects.add(new Pair<SecondaryStat, SecondaryStatValueHolder>(statup2.getKey(), new SecondaryStatValueHolder(effect, System.currentTimeMillis(), 0, 0, this.getId(), new ArrayList<Pair<Integer, Integer>>(), new ArrayList<Pair<Integer, Integer>>())));
                    }
                    this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this, true, false));
                    this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this, false, true));
                }
            }
        } else {
            this.cancelEffectFromBuffStat(SecondaryStat.SoulMP);
            this.cancelEffectFromBuffStat(SecondaryStat.FullSoulMP);
        }
    }

    public void useSoulSkill() {
        if (this.getBuffedEffect(SecondaryStat.SoulMP) != null) {
            final SecondaryStatEffect effect = this.getBuffedEffect(SecondaryStat.SoulMP);
            if (effect.getSoulMPCon() <= this.getBuffedValue(SecondaryStat.SoulMP) && this.getBuffedEffect(SecondaryStat.FullSoulMP) != null) {
                this.setBuffedValue(SecondaryStat.SoulMP, this.getBuffedValue(SecondaryStat.SoulMP) - effect.getSoulMPCon());
                final Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                localstatups.put(SecondaryStat.SoulMP, new Pair<Integer, Integer>(this.getBuffedValue(SecondaryStat.SoulMP), 0));
                this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, effect, this));
                this.map.broadcastMessage(CWvsContext.BuffPacket.giveForeignBuff(this, localstatups, effect));
                this.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, effect.getSourceId());
                this.cancelEffectFromBuffStat(SecondaryStat.FullSoulMP);
                this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this, true, false));
                this.client.getSession().writeAndFlush((Object) CWvsContext.enableActions(this, false, true));
            }
        }
    }

    public List<MapleMannequin> getSkinRoom() {
        return this.skinRoom;
    }

    public void setSkinRoom(final List<MapleMannequin> skinRoom) {
        this.skinRoom = skinRoom;
    }

    public int getOverloadCount() {
        return this.overloadCount;
    }

    public void setOverloadCount(final int overloadCount) {
        this.overloadCount = overloadCount;
    }

    public MonsterPyramid getMonsterPyramidInstance() {
        return this.monsterPyramidInstance;
    }

    public void setMonsterPyramidInstance(final MonsterPyramid monsterPyramidInstance) {
        this.monsterPyramidInstance = monsterPyramidInstance;
    }

    public FrittoEagle getFrittoEagle() {
        return this.frittoEagle;
    }

    public void setFrittoEagle(final FrittoEagle frittoEagle) {
        this.frittoEagle = frittoEagle;
    }

    public FrittoEgg getFrittoEgg() {
        return this.frittoEgg;
    }

    public void setFrittoEgg(final FrittoEgg frittoEgg) {
        this.frittoEgg = frittoEgg;
    }

    public FrittoDancing getFrittoDancing() {
        return this.frittoDancing;
    }

    public void setFrittoDancing(final FrittoDancing frittoDancing) {
        this.frittoDancing = frittoDancing;
    }

    public boolean isDuskBlind() {
        return this.isDuskBlind;
    }

    public void setDuskBlind(final boolean duskBlind) {
        this.isDuskBlind = duskBlind;
    }

    public int getDuskGauge() {
        return this.duskGauge;
    }

    public void setDuskGauge(final int duskGauge) {
        this.duskGauge = duskGauge;
    }

    public boolean isExtremeMode() {
        return this.extremeMode;
    }

    public void setExtremeMode(final boolean extremeMode) {
        this.extremeMode = extremeMode;
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        statups.put(SecondaryStat.PmdReduce, new Pair<Integer, Integer>(90, 0));
        if (extremeMode) {
        }
    }

    public ScheduledFuture<?> getSecondaryStatEffectTimer() {
        return this.secondaryStatEffectTimer;
    }

    public void getWorldGMMsg(final MapleCharacter chr, final String text) {
        for (final ChannelServer cs : ChannelServer.getAllInstances()) {
            for (final MapleCharacter achr : cs.getPlayerStorage().getAllCharacters().values()) {
                if (achr.isGM()) {
                    achr.dropMessageGM(6, "[GM알림] " + chr.getName() + "(이)가 " + text);
                }
            }
        }
    }

    public void AutoTeachSkillZero() {
        if (this.getJob() > 99) {
            final String job = String.valueOf(this.getJob());
            final int minus = Integer.parseInt(job.substring(job.length() - this.minusValue(), job.length()));
            this.TeachSkill(this.getJob() - minus + this.plusAlpha());
            if (minus > 0 && this.getJob() - this.plusAlpha() != 2000 && this.getJob() - this.plusAlpha() != 3000) {
                this.TeachSkillZero(this.getJob() - Integer.parseInt(job.substring(job.length() - 2, job.length())));
                for (int i = 0; i < this.getJob() % 10 + 1; ++i) {
                    this.TeachSkillZero(this.getJob() - this.getJob() % 10 + i);
                }
            }
        }
    }

    public void TeachSkillZero(final int job) {
        final MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(StringUtil.getLeftPaddedStr("" + job, '0', 3) + ".img");
        byte maxLevel = 0;
        for (final MapleData skill : data) {
            if (skill != null) {
                for (final MapleData skillId : skill.getChildren()) {
                    if (!skillId.getName().equals("icon") && !skillId.getName().equals("maxLevel")) {
                        maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                        final int skillid = Integer.parseInt(skillId.getName());
                        if (skillid == 100001005) {
                            maxLevel = 1;
                        }
                        if ((MapleDataTool.getIntConvert("invisible", skillId, 0) != 0 && skillid != 27001100 && skillid != 27001201 && skillid != 51121005 && skillid != 11121000 && skillid != 12121000 && skillid != 13121000 && skillid != 14121000 && skillid != 15121000) || this.getLevel() < MapleDataTool.getIntConvert("reqLev", skillId, 0) || SkillFactory.getSkill(skillid) == null) {
                            continue;
                        }
                        this.changeSkillLevel(SkillFactory.getSkill(skillid), (byte) 0, (byte) (SkillFactory.getSkill(skillid).isFourthJob() ? ((maxLevel == 180) ? 1 : maxLevel) : ((byte) SkillFactory.getSkill(skillid).getMasterLevel())));
                        if (!GameConstants.isPathFinder(this.getJob())) {
                            continue;
                        }
                        this.changeSkillLevel(SkillFactory.getSkill(3001007), (byte) 0, (byte) 0);
                        this.changeSkillLevel(SkillFactory.getSkill(3011004), (byte) 0, (byte) 20);
                        this.changeSkillLevel(SkillFactory.getSkill(3011005), (byte) 0, (byte) 10);
                        this.changeSkillLevel(SkillFactory.getSkill(3011006), (byte) 0, (byte) 10);
                        this.changeSkillLevel(SkillFactory.getSkill(3011007), (byte) 0, (byte) 10);
                        this.changeSkillLevel(SkillFactory.getSkill(3011008), (byte) 0, (byte) 10);
                        this.changeSkillLevel(SkillFactory.getSkill(3010002), (byte) 0, (byte) 20);
                        this.changeSkillLevel(SkillFactory.getSkill(1298), (byte) 0, (byte) 4);
                    }
                }
            }
        }
    }

    public void AutoTeachSkill() {
        if (this.getJob() > 99) {
            final String job = String.valueOf(this.getJob());
            final int minus = Integer.parseInt(job.substring(job.length() - this.minusValue(), job.length()));
            this.TeachSkill(this.getJob() - minus + this.plusAlpha());
            if (minus > 0 && this.getJob() - this.plusAlpha() != 2000 && this.getJob() - this.plusAlpha() != 3000) {
                this.TeachSkill(this.getJob() - Integer.parseInt(job.substring(job.length() - 2, job.length())));
                for (int i = 0; i < this.getJob() % 10 + 1; ++i) {
                    this.TeachSkill(this.getJob() - this.getJob() % 10 + i);
                }
            }
            if (GameConstants.isPathFinder(this.getJob())) {
                this.changeSkillLevel(SkillFactory.getSkill(3001007), (byte) 0, (byte) 0);
                this.changeSkillLevel(SkillFactory.getSkill(3011004), (byte) 20, (byte) 20);
                this.changeSkillLevel(SkillFactory.getSkill(3011005), (byte) 10, (byte) 10);
                this.changeSkillLevel(SkillFactory.getSkill(3011006), (byte) 10, (byte) 10);
                this.changeSkillLevel(SkillFactory.getSkill(3011007), (byte) 10, (byte) 10);
                this.changeSkillLevel(SkillFactory.getSkill(3011008), (byte) 10, (byte) 10);
                this.changeSkillLevel(SkillFactory.getSkill(3010002), (byte) 20, (byte) 20);
                this.changeSkillLevel(SkillFactory.getSkill(3010003), (byte) 20, (byte) 20);
                this.changeSkillLevel(SkillFactory.getSkill(1298), (byte) 4, (byte) 4);
            } else if (GameConstants.isAdel(this.getJob())) {
                if (this.getJob() == 15112) {
                    this.changeSkillLevel(SkillFactory.getSkill(150021251), (byte) 1, (byte) 1);
                    this.changeSkillLevel(SkillFactory.getSkill(150020079), (byte) 1, (byte) 1);
                    this.changeSkillLevel(SkillFactory.getSkill(151001004), (byte) 1, (byte) 1);
                }
            } else if (GameConstants.isSoulMaster(this.getJob()) || GameConstants.isFlameWizard(this.getJob()) || GameConstants.isWindBreaker(this.getJob()) || GameConstants.isNightWalker(this.getJob()) || GameConstants.isStriker(this.getJob())) {
                this.changeSkillLevel(SkillFactory.getSkill(10001244), (byte) 1, (byte) 1);
                this.changeSkillLevel(SkillFactory.getSkill(10000252), (byte) 1, (byte) 1);
                this.changeSkillLevel(SkillFactory.getSkill(10001253), (byte) 1, (byte) 1);
                this.changeSkillLevel(SkillFactory.getSkill(10001254), (byte) 1, (byte) 1);
                this.changeSkillLevel(SkillFactory.getSkill(10000250), (byte) 1, (byte) 1);
            } else if (GameConstants.isMichael(this.getJob())) {
                this.changeSkillLevel(SkillFactory.getSkill(50000250), (byte) 1, (byte) 1);
            } else if (GameConstants.isDemonAvenger(this.getJob())) {
                this.changeSkillLevel(SkillFactory.getSkill(31011000), (byte) 20, (byte) 20);
                this.changeSkillLevel(SkillFactory.getSkill(31010002), (byte) 10, (byte) 10);
                this.changeSkillLevel(SkillFactory.getSkill(31010003), (byte) 15, (byte) 15);
                this.changeSkillLevel(SkillFactory.getSkill(31011001), (byte) 20, (byte) 20);
            } else if (GameConstants.isEvan(this.getJob()) && this.getLevel() >= 30) {
                this.changeSkillLevel(SkillFactory.getSkill(22110018), (byte) 10, (byte) 10);
            } else if (GameConstants.isZero(this.getJob())) {
                if (this.level >= 100) {
                    this.changeSkillLevel(SkillFactory.getSkill(100000267), (byte) 1, (byte) 1);
                }
                if (this.level >= 110) {
                    this.changeSkillLevel(SkillFactory.getSkill(100001261), (byte) 1, (byte) 1);
                }
                if (this.level >= 120) {
                    this.changeSkillLevel(SkillFactory.getSkill(100001274), (byte) 1, (byte) 1);
                }
                if (this.level >= 140) {
                    this.changeSkillLevel(SkillFactory.getSkill(100001272), (byte) 1, (byte) 1);
                }
                if (this.level >= 160) {
                    this.changeSkillLevel(SkillFactory.getSkill(100001283), (byte) 1, (byte) 1);
                }
                if (this.level >= 200) {
                    this.changeSkillLevel(SkillFactory.getSkill(100001005), (byte) 1, (byte) 1);
                }
            }
        }
    }

    public int minusValue() {
        if (GameConstants.isEvan(this.getJob()) || GameConstants.isMercedes(this.getJob()) || GameConstants.isPhantom(this.getJob()) || GameConstants.isLuminous(this.getJob()) || GameConstants.isEunWol(this.getJob()) || GameConstants.isAran(this.getJob()) || GameConstants.isAngelicBuster(this.getJob()) || GameConstants.isDemonSlayer(this.getJob()) || GameConstants.isDemonAvenger(this.getJob()) || GameConstants.isXenon(this.getJob()) || GameConstants.isZero(this.getJob()) || GameConstants.isKinesis(this.getJob()) || GameConstants.isPinkBean(this.getJob()) || GameConstants.isKaiser(this.getJob())) {
            return 3;
        }
        return 2;
    }

    public int plusAlpha() {
        if (GameConstants.isDemonAvenger(this.getJob()) || GameConstants.isAngelicBuster(this.getJob()) || GameConstants.isCannon(this.getJob()) || GameConstants.isEvan(this.getJob()) || GameConstants.isDemonSlayer(this.getJob())) {
            return 1;
        }
        if (GameConstants.isXenon(this.getJob()) || GameConstants.isMercedes(this.getJob())) {
            return 2;
        }
        if (GameConstants.isPhantom(this.getJob())) {
            return 3;
        }
        if (GameConstants.isLuminous(this.getJob())) {
            return 4;
        }
        if (GameConstants.isEunWol(this.getJob())) {
            return 5;
        }
        return 0;
    }

    public void TeachSkill(final int job) {
        final MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(StringUtil.getLeftPaddedStr("" + job, '0', 3) + ".img");
        byte maxLevel = 0;
        for (final MapleData skill : data) {
            if (skill != null) {
                for (final MapleData skillId : skill.getChildren()) {
                    if (!skillId.getName().equals("icon") && !skillId.getName().equals("maxLevel")) {
                        boolean learn = true;
                        maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                        final int skillid = Integer.parseInt(skillId.getName());
                        if (skillid == 100001005) {
                            maxLevel = 1;
                        }
                        if (!PacketHelper.jobskill(this, skillid) || skillid == 22110016 || SkillFactory.getSkill(skillid).isHyper()) {
                            learn = false;
                            break;
                        }
                        if (GameConstants.isCannon(this.getJob())) {
                            switch (skillid) {
                                case 5000000:
                                case 5000007:
                                case 5001002:
                                case 5001003:
                                case 5001005:
                                case 5001010: {
                                    this.changeSkillData(SkillFactory.getSkill(skillid), 0, (byte) 0, 0L);
                                    learn = false;
                                    break;
                                }
                            }
                        }
                        switch (skillid) {
                            case 3321003:
                            case 100000267:
                            case 100001005:
                            case 100001261:
                            case 100001272:
                            case 100001274:
                            case 100001283: {
                                learn = false;
                                break;
                            }
                        }
                        if ((MapleDataTool.getIntConvert("invisible", skillId, 0) != 0 && skillid != 27001100 && skillid != 27001201 && skillid != 51121005 && skillid != 11121000 && skillid != 12121000 && skillid != 13121000 && skillid != 14121000 && skillid != 15121000) || this.getLevel() < MapleDataTool.getIntConvert("reqLev", skillId, 0) || SkillFactory.getSkill(skillid) == null || !learn) {
                            continue;
                        }
                        this.changeSkillLevel(SkillFactory.getSkill(skillid), (byte) ((maxLevel == 180) ? 1 : maxLevel), (byte) (SkillFactory.getSkill(skillid).isFourthJob() ? ((maxLevel == 180) ? 1 : maxLevel) : maxLevel));
                    }
                }
            }
        }
        for (final Map.Entry<Skill, SkillEntry> skill2 : this.skills.entrySet()) {
            if (!PacketHelper.jobskill(this, skill2.getKey().getId()) && skill2.getKey().getId() < 80000000 && skill2.getKey().getId() < 400000000) {
                this.changeSkillLevel(skill2.getKey(), (byte) 0, (byte) 0);
            }
        }
    }

    public Long getSkillCustomValue(final int skillid) {
        if (!this.customInfo.containsKey(skillid)) {
            return null;
        }
        if (skillid == 63111009 && this.customInfo.get(skillid).getValue() <= 0L) {
            return null;
        }
        if (this.customInfo.get(skillid).getValue() < 0L) {
            return null;
        }
        return this.customInfo.get(skillid).getValue();
    }

    public Integer getSkillCustomTime(final int skillid) {
        if (this.customInfo.containsKey(skillid)) {
            return (int) (this.customInfo.get(skillid).getEndTime() - System.currentTimeMillis());
        }
        return null;
    }

    public long getSkillCustomValue0(final int skillid) {
        if (this.customInfo.containsKey(skillid)) {
            return this.customInfo.get(skillid).getValue();
        }
        return 0L;
    }

    public void removeSkillCustomInfo(int skillid) {
        /* 16162 */
        if (skillid == 993192800) {
            /* 16164 */
            if (getMapId() == 993192800) {
                /* 16165 */
                getClient().send(CField.PunchKingPacket(this, 0, new int[]{4}));
                /* 16166 */
                server.Timer.EventTimer.getInstance().schedule(() -> warp(993192701), 1000L);
            }
        }

        /* 16171 */
        this.customInfo.remove(Integer.valueOf(skillid));
        /* 16172 */
        if (ServerConstants.warpMap == skillid) {
            /* 16174 */
            if (getChair() > 0 || getBuffedEffect(SecondaryStat.RideVehicle) != null) {
                /* 16175 */
                boolean give = false;
                /* 16176 */
                if (getBuffedEffect(SecondaryStat.RideVehicle) != null
                        && /* 16177 */ getBuffedEffect(SecondaryStat.RideVehicle).getSourceId() / 10000 == 8000) {
                    /* 16178 */
                    give = true;
                }

                /* 16181 */
                if (getChair() > 0) {
                    /* 16182 */
                    give = true;
                }
                /* 16184 */
                if (getMapId() != ServerConstants.warpMap) {
                    give = false;
                }
                if (give) {
                    getClient().send(CField.UIPacket.detailShowInfo("휴식 포인트 1 획득!", 3, 20, 20));
                    setSkillCustomInfo(ServerConstants.warpMap, 0L, 60000L);
                    setKeyValue(100161, "point", (getKeyValue(100161, "point") + 2L) + "");
                }
            }
        }
    }

    public void setSkillCustomInfo(final int skillid, final long value, final long time) {
        if (this.getSkillCustomValue(skillid) != null) {
            this.removeSkillCustomInfo(skillid);
        }
        this.customInfo.put(skillid, new SkillCustomInfo(value, time));
    }

    public void addSkillCustomInfo(final int skillid, final long value) {
        this.customInfo.put(skillid, new SkillCustomInfo(this.getSkillCustomValue0(skillid) + value, 0L));
    }

    public Map<Integer, SkillCustomInfo> getSkillCustomValues() {
        return this.customInfo;
    }

    public final void createSecondAtom(List<SecondAtom2> atoms, Point pos) {
        for (SecondAtom2 atom : atoms) {
            if (this.getGraveTarget() > 0) {
                atom.setTarget(this.getGraveTarget());
            }
            this.getMap().spawnSecondAtom(this, new MapleSecondAtom(this, atom, pos));
        }
    }

    public final void createSecondAtom(List<SecondAtom2> atoms, Point pos, boolean left) {
        for (SecondAtom2 atom : atoms) {
            if (this.getGraveTarget() > 0) {
                atom.setTarget(this.getGraveTarget());
            }
            this.getMap().spawnSecondAtom(this, new MapleSecondAtom(this, atom, pos), left);
        }
    }

    public final void createSecondAtom(int skillid, Point pos, int num) {
        for (SecondAtom2 atom : SkillFactory.getSkill(skillid).getSecondAtoms()) {
            if (this.getGraveTarget() > 0) {
                atom.setTarget(this.getGraveTarget());
            }
            MapleSecondAtom msa = new MapleSecondAtom(this, atom, pos);
            msa.setNumuse(true);
            msa.setNum(num);
            this.getMap().spawnSecondAtom(this, msa);
        }
    }

    public int getGraveTarget() {
        return this.graveTarget;
    }
    
        public int getCustomItem(int id) {
        int size = (int) getKeyValue(100000, id+"");
        if (size == -1) {
            size = 0;
            setKeyValue(100000, id+"", "0");
        }
        return size;
    }

    public List<Integer> getCustomInventory() {
        List<Integer> inventory = new ArrayList<>();
        List<CustomItem> list = GameConstants.customItems;
        for (CustomItem item : list) {
            int size = getCustomItem(item.getId());
            inventory.add(size);
        }

        return inventory;
    }

    public void addCustomItem(int id) {
        int size = getCustomItem(id);
        setKeyValue(100000, id+"", ++size+"");
    }

    public int equippedCustomItem(CustomItem.CustomItemType type) {
        int id = (int) getKeyValue(100000 + type.ordinal(), "equip");
        return id;
    }

    public void equipCustomItem(int id) {
        List<CustomItem> list = GameConstants.customItems;
        CustomItem ci = list.get(id);
        setKeyValue(100000 + ci.getType().ordinal(), "equip", id+"");
    }

    public void unequipCustomItem(int id) {
        List<CustomItem> list = GameConstants.customItems;
        CustomItem ci = list.get(id);
        setKeyValue(100000 + ci.getType().ordinal(), "equip", "-1");
    }

    public void setGraveTarget(final int graveTarget) {
        this.graveTarget = graveTarget;
    }

    public void GiveHolyUnityBuff(final SecondaryStatEffect effect, final int skillid) {
        final SecondaryStatEffect effect2 = SkillFactory.getSkill(400011003).getEffect(this.getSkillLevel(400011003));
        int duration = effect.getDuration();
        final int skilly = effect2.getY();
        final int skillx = this.getStat().getTotalStr() / effect2.getW();
        if (skilly + skillx >= 100) {
            duration = 100;
        } else {
            duration = skilly + skillx;
        }
        if (this.getBuffedValue(400011003) && this.getSkillCustomValue0(400011003) > 0L) {
            for (final MapleCharacter chr2 : this.getMap().getAllCharactersThreadsafe()) {
                if (chr2.getId() == this.getSkillCustomValue0(400011003)) {
                    SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid)).applyTo(chr2, effect.getDuration() / 100 * duration);
                    break;
                }
            }
        }
    }

    public int getOrder() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int order = 0;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps.setInt(1, this.id);
            rs = ps.executeQuery();
            if (rs.next()) {
                order = rs.getInt("order");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }
        return order;
    }

    public int getBuffedOwner(final int skillid) {
        if (SkillFactory.getSkill(skillid) == null) {
            return 0;
        }
        for (final Pair<SecondaryStat, SecondaryStatValueHolder> eff : this.effects) {
            if (skillid == eff.right.effect.getSourceId()) {
                return eff.right.cid;
            }
        }
        return 0;
    }

    public final void PartyBuffCheck(final SecondaryStat stat, final int skillid) {
        boolean give = false;
        final MapleCharacter chr = this;
        final MapleCharacter buffowner = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getBuffedOwner(skillid));
        if (buffowner != null) {
            if (this.getMapId() == 450013700) {
                if (this.getBuffedValue(skillid)) {
                    chr.cancelEffectFromBuffStat(stat);
                }
                return;
            }
            final SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(buffowner.getSkillLevel(skillid));
            if (!buffowner.getBuffedValue(skillid) || buffowner.getMapId() != chr.getMapId()) {
                chr.cancelEffectFromBuffStat(stat);
            }
            if (chr.getParty() == null && chr.getBuffedOwner(skillid) != chr.getId()) {
                chr.cancelEffectFromBuffStat(stat);
            }
            if (chr.getBuffedValue(skillid)) {
                if (skillid == 51111008 && GameConstants.isMichael(chr.getJob()) && chr.getSkillCustomValue(51111010) == null) {
                    chr.addHP(chr.getStat().getCurrentMaxHp() / 100L);
                    chr.setSkillCustomInfo(51111010, 0L, 4000L);
                }
                if (buffowner.getParty() != null && chr.getParty() != null) {
                    if (buffowner.getParty().getId() != chr.getParty().getId()) {
                        chr.cancelEffectFromBuffStat(stat);
                    }
                    if (skillid == 51111008) {
                        int size = 0;
                        for (final MaplePartyCharacter chr2 : chr.getParty().getMembers()) {
                            if (chr2.isOnline()) {
                                ++size;
                            }
                        }
                        if (size != chr.getSkillCustomValue0(51111009) && size <= 1) {
                            chr.cancelEffectFromBuffStat(stat);
                            chr.removeSkillCustomInfo(51111009);
                            SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid)).applyTo(chr);
                        }
                    }
                    for (final MaplePartyCharacter chr3 : buffowner.getParty().getMembers()) {
                        if (chr3.getMapid() == buffowner.getMapId()) {
                            final MapleCharacter chr4 = chr.getClient().getChannelServer().getPlayerStorage().getCharacterByName(chr3.getName());
                            if (chr4 == null || !chr3.isOnline() || chr.getId() == chr4.getId()) {
                                continue;
                            }
                            if (buffowner.getTruePosition().x + effect.getLt().x < chr4.getTruePosition().x && buffowner.getTruePosition().x - effect.getLt().x > chr4.getTruePosition().x && buffowner.getTruePosition().y + effect.getLt().y < chr4.getTruePosition().y && buffowner.getTruePosition().y - effect.getLt().y > chr4.getTruePosition().y) {
                                if (chr4.getBuffedValue(skillid)) {
                                    continue;
                                }
                                SkillFactory.getSkill(skillid).getEffect(buffowner.getSkillLevel(skillid)).applyTo(buffowner, chr4);
                                if (skillid != 51111008) {
                                    continue;
                                }
                                give = true;
                            } else {
                                if (chr4.getBuffedOwner(skillid) == chr4.getId() || !chr4.getBuffedValue(skillid)) {
                                    continue;
                                }
                                chr4.cancelEffect(chr4.getBuffedEffect(skillid));
                                if (skillid != 51111008) {
                                    continue;
                                }
                                give = true;
                            }
                        }
                    }
                    if (give) {
                        buffowner.cancelEffect(buffowner.getBuffedEffect(skillid));
                        SkillFactory.getSkill(skillid).getEffect(buffowner.getSkillLevel(skillid)).applyTo(buffowner);
                    }
                }
            }
        } else if (chr.getBuffedValue(skillid) && chr.getBuffedOwner(skillid) != chr.getId()) {
            chr.cancelEffect(chr.getBuffedEffect(skillid));
        }
    }

    public final void MechCarrier(final int delay, final boolean reset) {
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!MapleCharacter.this.getMap().getAllMonster().isEmpty()) {
                    final List<Triple<Integer, Integer, Integer>> a = new ArrayList<Triple<Integer, Integer, Integer>>();
                    if (reset) {
                        MapleCharacter.this.setSkillCustomInfo(400051069, 0L, 0L);
                    }
                    for (final MapleSummon sum : MapleCharacter.this.getMap().getAllSummonsThreadsafe()) {
                        if (sum.getOwner().getId() == MapleCharacter.this.getId() && sum.getSkill() == 400051068) {
                            for (final MapleMonster mob : MapleCharacter.this.getMap().getAllMonster()) {
                                if (sum.getTruePosition().x + MapleCharacter.this.getBuffedEffect(400051068).getLt().x < mob.getTruePosition().x && sum.getTruePosition().x - MapleCharacter.this.getBuffedEffect(400051068).getLt().x > mob.getTruePosition().x && sum.getTruePosition().y + MapleCharacter.this.getBuffedEffect(400051068).getLt().y < mob.getTruePosition().y && sum.getTruePosition().y - MapleCharacter.this.getBuffedEffect(400051068).getLt().y > mob.getTruePosition().y) {
                                    a.add(new Triple<Integer, Integer, Integer>(mob.getObjectId(), 400051069, 6000));
                                    if (a.size() == MapleCharacter.this.getSkillCustomValue0(400051068)) {
                                        break;
                                    }
                                    continue;
                                }
                            }
                            while (a.size() < MapleCharacter.this.getSkillCustomValue0(400051068)) {
                                for (final MapleMonster mob : MapleCharacter.this.getMap().getAllMonster()) {
                                    if (sum.getTruePosition().x + MapleCharacter.this.getBuffedEffect(400051068).getLt().x < mob.getTruePosition().x && sum.getTruePosition().x - MapleCharacter.this.getBuffedEffect(400051068).getLt().x > mob.getTruePosition().x && sum.getTruePosition().y + MapleCharacter.this.getBuffedEffect(400051068).getLt().y < mob.getTruePosition().y && sum.getTruePosition().y - MapleCharacter.this.getBuffedEffect(400051068).getLt().y > mob.getTruePosition().y) {
                                        a.add(new Triple<Integer, Integer, Integer>(mob.getObjectId(), 400051069, 6000));
                                        if (a.size() == MapleCharacter.this.getSkillCustomValue0(400051068)) {
                                            break;
                                        }
                                        continue;
                                    }
                                }
                            }
                            MapleCharacter.this.getMap().broadcastMessage(SkillPacket.CreateSubObtacle(MapleCharacter.this.getClient().getPlayer(), sum, a, 10));
                            break;
                        }
                    }
                }
            }
        }, delay);
    }

    public int getSpellCount(final int type) {
        int count = 0;
        if (type == 0 || type == 1) {
            count += (int) this.getSkillCustomValue0(155001100);
        }
        if (type == 0 || type == 2) {
            count += (int) this.getSkillCustomValue0(155101100);
        }
        if (type == 0 || type == 3) {
            count += (int) this.getSkillCustomValue0(155111102);
        }
        if (type == 0 || type == 4) {
            count += (int) this.getSkillCustomValue0(155121102);
        }
        return count;
    }

    public void addSpell(final int skillid) {
        int spell = (int) this.getSkillCustomValue0(skillid);
        if (this.getSpellCount(0) < 5) {
            if (skillid == 155001100) {
                if (this.getBuffedValue(400051036)) {
                    for (int i = spell; i < 5; ++i) {
                        ++spell;
                        this.setSkillCustomInfo(155001100, spell, 0L);
                        this.setSkillCustomInfo(155001101, spell, 0L);
                    }
                    this.onPacket(1);
                } else {
                    ++spell;
                    this.setSkillCustomInfo(155001100, spell, 0L);
                    this.setSkillCustomInfo(155001101, spell, 0L);
                    this.onPacket(1);
                }
            } else if (skillid == 155101100 || skillid == 155101101 || skillid == 155101112) {
                if (spell < 1) {
                    ++spell;
                    this.setSkillCustomInfo(155101100, spell, 0L);
                    this.setSkillCustomInfo(155101101, spell, 0L);
                    this.onPacket(2);
                }
            } else if (skillid == 155111102 || skillid == 155111111) {
                if (spell < 1) {
                    ++spell;
                    this.setSkillCustomInfo(155111102, spell, 0L);
                    this.setSkillCustomInfo(155111103, spell, 0L);
                    this.onPacket(4);
                }
            } else if (skillid == 155121102 && spell < 1) {
                ++spell;
                this.setSkillCustomInfo(155121102, spell, 0L);
                this.setSkillCustomInfo(155121103, spell, 0L);
                this.onPacket(8);
            }
        }
    }

    public void onPacket(final int flag) {
        final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        if ((flag & 0x1) != 0x0) {
            statups.put(SecondaryStat.PlainBuff, new Pair<Integer, Integer>(1, 0));
        }
        if ((flag & 0x2) != 0x0) {
            statups.put(SecondaryStat.ScarletBuff, new Pair<Integer, Integer>(1, 0));
        }
        if ((flag & 0x4) != 0x0) {
            statups.put(SecondaryStat.GustBuff, new Pair<Integer, Integer>(1, 0));
        }
        if ((flag & 0x8) != 0x0) {
            statups.put(SecondaryStat.AbyssBuff, new Pair<Integer, Integer>(1, 0));
        }
        final SecondaryStatEffect effects = SkillFactory.getSkill(155000007).getEffect(this.getSkillLevel(155000007));
        if ((flag & 0x10) != 0x0) {
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effects, this));
        } else {
            this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effects, this));
        }
    }

    public void useSpell() {
        int flag = 0;
        if (this.getSkillCustomValue0(155001100) > 0L) {
            this.setSkillCustomInfo(155001101, this.getSkillCustomValue0(155001100), 0L);
            this.setSkillCustomInfo(155001100, 0L, 0L);
            flag |= 0x1;
        }
        if (this.getSkillCustomValue0(155101100) > 0L) {
            this.setSkillCustomInfo(155101101, this.getSkillCustomValue0(155101100), 0L);
            this.setSkillCustomInfo(155101100, 0L, 0L);
            flag |= 0x2;
        }
        if (this.getSkillCustomValue0(155111102) > 0L) {
            this.setSkillCustomInfo(155111103, this.getSkillCustomValue0(155111102), 0L);
            this.setSkillCustomInfo(155111102, 0L, 0L);
            flag |= 0x4;
        }
        if (this.getSkillCustomValue0(155121102) > 0L) {
            this.setSkillCustomInfo(155121103, this.getSkillCustomValue0(155121102), 0L);
            this.setSkillCustomInfo(155121102, 0L, 0L);
            flag |= 0x8;
        }
        this.onPacket(flag);
    }

    public int getMwSize(final int skillid) {
        int size = 0;
        for (final MapleMagicWreck mw : this.getMap().getAllFieldThreadsafe()) {
            if (mw.getChr().getId() == this.getId() && skillid == mw.getSourceid()) {
                ++size;
            }
        }
        return size;
    }

    public void MonsterQuest(final int quest, final int maxmob) {
        String q = "";
        int count = 0;
        if (this.getQuest(MapleQuest.getInstance(quest)).getCustomData() == null) {
            MapleQuest.getInstance(quest).forceStart(this, 0, "1");
        } else {
            count = Integer.parseInt(this.getQuest(MapleQuest.getInstance(quest)).getCustomData());
            if (++count < 10) {
                q = "00" + count;
            } else if (count < 100) {
                q = "0" + count;
            } else {
                q = "" + count;
            }
            if (count >= maxmob) {
                if (maxmob < 10) {
                    q = "00" + maxmob;
                } else if (maxmob < 100) {
                    q = "0" + maxmob;
                } else {
                    q = "" + maxmob + "";
                }
            }
            MapleQuest.getInstance(quest).forceStart(this, 0, q);
        }
    }

    public void EnterMultiYutGame() {
        if (MultiYutGame.multiYutMagchingQueue.contains(this)) {
            if (this.ConstentTimer != null) {
                this.ConstentTimer.cancel();
                this.ConstentTimer = null;
            }
            MultiYutGame.multiYutMagchingQueue2.add(this);
            if (MultiYutGame.multiYutMagchingQueue2.size() == 2) {
                List<Integer> id = new ArrayList<>();
                for (MapleCharacter chr : MultiYutGame.multiYutMagchingQueue2) {
                    if (chr.getClient().getChannel() != 1) {
                        MultiYutGame.multiYutMagchingQueue2.remove(chr);
                        chr.changeChannel(1);
                        id.add(Integer.valueOf(chr.getId()));
                    }
                }
                server.Timer.EtcTimer.getInstance().schedule(() -> {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
                            if (player != null) {
                                for (Integer idc : id) {
                                    if (idc.intValue() == player.getId() && !MultiYutGame.multiYutMagchingQueue.contains(player)) {
                                        MultiYutGame.multiYutMagchingQueue.add(player);
                                    }
                                }
                            }
                        }
                    }
                    MultiYutGame.multiYutMagchingQueue2.clear();
                    for (MapleCharacter chrs : MultiYutGame.multiYutMagchingQueue) {
                        chrs.getClient().send(SLFCGPacket.ContentsWaiting(chrs, 0, new int[]{11, 5, 1, 18}));
                        chrs.warp(993189800);
                        chrs.getClient().send(CField.getClock(5));
                    }

                }, 4000L);
            }
        }
    }

    public void EnterBattleReverse() {
        if (BattleReverse.BattleReverseMatchingQueue.contains(this)) {
            if (this.ConstentTimer != null) {
                this.ConstentTimer.cancel();
                this.ConstentTimer = null;
            }
            BattleReverse.BattleReverseMatchingQueue2.add(this);
            if (BattleReverse.BattleReverseMatchingQueue2.size() == 2) {
                List<Integer> id = new ArrayList<>();
                for (MapleCharacter chr : BattleReverse.BattleReverseMatchingQueue2) {
                    if (chr.getClient().getChannel() != 1) {
                        BattleReverse.BattleReverseMatchingQueue.remove(chr);
                        chr.changeChannel(1);
                        id.add(Integer.valueOf(chr.getId()));
                    }
                }
                server.Timer.EtcTimer.getInstance().schedule(() -> {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
                            if (player != null) {
                                for (Integer idc : id) {
                                    if (idc.intValue() == player.getId() && !BattleReverse.BattleReverseMatchingQueue.contains(player)) {
                                        BattleReverse.BattleReverseMatchingQueue.add(player);
                                    }
                                }
                            }
                        }
                    }
                    BattleReverse.BattleReverseMatchingQueue2.clear();
                    BattleReverse.StartGame2();
                },
                        4000L);
            }
        }
    }

    public void EnterMonsterPyramid() {
        if (MonsterPyramid.monsterPyramidMatchingQueue.contains(this)) {
            if (this.ConstentTimer != null) {
                this.ConstentTimer.cancel();
                this.ConstentTimer = null;
            }
            MonsterPyramid.monsterPyramidMatchingQueue2.add(this);
            if (MonsterPyramid.monsterPyramidMatchingQueue2.size() == 3) {
                List<Integer> id = new ArrayList<>();
                for (MapleCharacter chr : MonsterPyramid.monsterPyramidMatchingQueue2) {
                    if (chr.getClient().getChannel() != 1) {
                        MonsterPyramid.monsterPyramidMatchingQueue.remove(chr);
                        chr.changeChannel(1);
                        id.add(Integer.valueOf(chr.getId()));
                    }
                }
                server.Timer.EtcTimer.getInstance().schedule(() -> {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
                            if (player != null) {
                                for (Integer idc : id) {
                                    if (idc.intValue() == player.getId() && !MonsterPyramid.monsterPyramidMatchingQueue.contains(player)) {
                                        MonsterPyramid.monsterPyramidMatchingQueue.add(player);
                                    }
                                }
                            }
                        }
                    }
                    MonsterPyramid.monsterPyramidMatchingQueue2.clear();
                    MonsterPyramid.StartGame(3);
                },
                        4000L);
            }
        }
    }

    public void CancelWating(final MapleCharacter chr, final int type) {
        final List<MapleCharacter> remover = new ArrayList<MapleCharacter>();
        if (type == 18) {
            MultiYutGame.multiYutMagchingQueue.remove(chr);
            for (final MapleCharacter chr2 : MultiYutGame.multiYutMagchingQueue) {
                if (chr.getId() != chr2.getId()) {
                    remover.add(chr2);
                    chr2.getClient().send(SLFCGPacket.ContentsWaiting(chr2, 0, 11, 5, 1, type));
                }
            }
            for (final MapleCharacter chr2 : remover) {
                MultiYutGame.multiYutMagchingQueue.remove(chr2);
                MultiYutGame.multiYutMagchingQueue.remove(chr2);
            }
            chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, 11, 5, 1, type));
        } else if (type == 24) {
            MonsterPyramid.monsterPyramidMatchingQueue.remove(chr);
            for (final MapleCharacter chr2 : MonsterPyramid.monsterPyramidMatchingQueue) {
                if (chr.getId() != chr2.getId()) {
                    remover.add(chr2);
                    chr2.getClient().send(SLFCGPacket.ContentsWaiting(chr2, 0, 11, 5, 1, type));
                }
            }
            for (final MapleCharacter chr2 : remover) {
                MonsterPyramid.monsterPyramidMatchingQueue.remove(chr2);
                MonsterPyramid.monsterPyramidMatchingQueue2.remove(chr2);
            }
            chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, 11, 5, 1, type));
        } else if (type == 23) {
            BattleReverse.BattleReverseMatchingQueue.remove(chr);
            for (final MapleCharacter chr2 : BattleReverse.BattleReverseMatchingQueue) {
                if (chr.getId() != chr2.getId()) {
                    remover.add(chr2);
                    chr2.getClient().send(SLFCGPacket.ContentsWaiting(chr2, 0, 11, 5, 1, type));
                }
            }
            for (final MapleCharacter chr2 : remover) {
                BattleReverse.BattleReverseMatchingQueue.remove(chr2);
                BattleReverse.BattleReverseMatchingQueue2.remove(chr2);
            }
            chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, 11, 5, 1, type));
        }
    }

    public void startSound() {
        this.client.getSession().writeAndFlush((Object) SLFCGPacket.playSE("MiniGame.img/multiBingo/3"));
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleCharacter.this.client.getSession().writeAndFlush((Object) SLFCGPacket.playSE("MiniGame.img/multiBingo/2"));
            }
        }, 1000L);
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleCharacter.this.client.getSession().writeAndFlush((Object) SLFCGPacket.playSE("MiniGame.img/multiBingo/1"));
            }
        }, 2000L);
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleCharacter.this.client.getSession().writeAndFlush((Object) SLFCGPacket.playSE("MiniGame.img/multiBingo/start"));
            }
        }, 3000L);
    }

    public void setGuildName(final String name) {
        this.guildName = name;
    }

    public String getGuildName() {
        return this.guildName;
    }

    public long getLastDisconnectTime() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps.setInt(1, this.id);
            rs = ps.executeQuery();
            if (rs.next()) {
                this.lastDisconnectTime = rs.getLong("lastDisconnectTime");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }
        return this.lastDisconnectTime;
    }

    public void setLastDisconnectTime(final long lastDisconnectTime) {
        this.lastDisconnectTime = lastDisconnectTime;
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE characters SET lastDisconnectTime = ? WHERE id = ?");
            ps.setLong(1, this.lastDisconnectTime);
            ps.setInt(2, this.id);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex2) {
            }
        }
    }

    public void ReHolyUnityBuff(final SecondaryStatEffect effect) {
        final Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
        final Rectangle bounds = effect.calculateBoundingBox(this.getTruePosition(), this.isFacingLeft());
        final List<MapleMapObject> affecteds = this.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.PLAYER));
        if (this.getParty() != null) {
            for (final MapleMapObject affectedmo : affecteds) {
                final MapleCharacter affected = (MapleCharacter) affectedmo;
                if (affected.getParty() != null && this.getId() != affected.getId() && this.getParty().getId() == affected.getParty().getId()) {
                    localstatups.clear();
                    localstatups.put(SecondaryStat.HolyUnity, new Pair<Integer, Integer>(affected.getId(), (int) this.getBuffLimit(400011003)));
                    this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, effect, this));
                    this.getMap().broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(this, localstatups, effect), false);
                    affected.getMap().broadcastMessage(affected, CField.EffectPacket.showEffect(affected, 0, 400011003, 1, 0, 0, (byte) ((affected.getTruePosition().x > affected.getPosition().x) ? 1 : 0), false, affected.getPosition(), null, null), false);
                    this.setSkillCustomInfo(400011003, affected.getId(), 0L);
                    SkillFactory.getSkill(400011021).getEffect(this.getSkillLevel(400011003)).applyTo(this, affected, false, affected.getPosition(), (int) this.getBuffLimit(400011003), (byte) 0, false);
                    break;
                }
            }
        }
    }

    public Item getAttackitem() {
        return this.attackitem;
    }

    public void setAttackitem(final Item attackitem) {
        this.attackitem = attackitem;
    }

    public void getAttackDealy(final AttackInfo attack, final boolean spskill) {
    }

    public void getYetiGauge(final int skillid, final int type) {
        if (this.getBuffedEffect(SecondaryStat.YetiAngerMode) == null && skillid != 999) {
            final SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(this.getSkillLevel(skillid));
            int up = 0;
            switch (skillid) {
                case 135001000:
                case 135001001:
                case 135001002: {
                    up = ((type == 1) ? 1 : ((type == 2) ? 3 : 0));
                    break;
                }
                case 135001008:
                case 135001011:
                case 135001015:
                case 135001016:
                case 135001019: {
                    if (type != 1) {
                        up = eff.getX();
                    }
                    if (this.getBuffedEffect(SecondaryStat.YetiSpicy) != null) {
                        up += 10;
                        break;
                    }
                    break;
                }
            }
            if (skillid == 999) {
                up += 2;
            }
            if (up > 0) {
                this.addSkillCustomInfo(135001005, up);
                if (this.getSkillCustomValue0(135001005) >= 300L) {
                    this.setSkillCustomInfo(135001005, 300L, 0L);
                }
                SkillFactory.getSkill(135001005).getEffect(1).applyTo(this, false);
            }
        }
    }

    public void Recharge(int skillid) {
        switch (skillid) {
            case 65001100:
            case 65101100:
            case 65111007:
            case 65111100:
            case 65111101:
            case 65121008:
            case 65121100:
            case 65121101: {
                if (skillid == 65111007) {
                    skillid = 65111100;
                }
                if (skillid != 65111100) {
                    this.getClient().send(CField.lockSkill(skillid));
                }
                int lv = this.getSkillLevel(65000003);
                int lv2 = this.getSkillLevel(65100005);
                int lv3 = this.getSkillLevel(65110006);
                int lv4 = this.getSkillLevel(65120006);
                SecondaryStatEffect eff = null;
                SecondaryStatEffect eff2 = null;
                SecondaryStatEffect eff3 = null;
                SecondaryStatEffect eff4 = null;
                if (lv > 0) {
                    eff = SkillFactory.getSkill(65000003).getEffect(lv);
                }
                if (lv2 > 0) {
                    eff2 = SkillFactory.getSkill(65100005).getEffect(lv2);
                }
                if (lv3 > 0) {
                    eff3 = SkillFactory.getSkill(65110006).getEffect(lv3);
                }
                if (lv4 > 0) {
                    eff4 = SkillFactory.getSkill(65120006).getEffect(lv4);
                }
                int prop = SkillFactory.getSkill(skillid).getEffect(this.getTotalSkillLevel(GameConstants.getLinkedSkill(skillid))).getOnActive();
                if (lv > 0) {
                    prop += eff.getX();
                }
                if (skillid == 65111100) {
                    prop = 30;
                }
                if (Randomizer.isSuccess(prop)) {
                    this.getClient().getSession().writeAndFlush((Object) CField.unlockSkill());
                    this.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showNormalEffect(this, 49, true));
                    this.getMap().broadcastMessage(this, CField.EffectPacket.showNormalEffect(this, 49, false), false);
                    this.removeSkillCustomInfo(65110006);
                    if (lv4 <= 0 || !Randomizer.isSuccess(50)) {
                        break;
                    }
                    eff4.applyTo(this);
                    break;
                }
                if (skillid == 65111100) {
                    break;
                }
                if (lv3 > 0) {
                    this.setSkillCustomInfo(65110006, this.getSkillCustomValue0(65110006) + 1L, 0L);
                    if (this.getSkillCustomValue0(65110006) == 2L) {
                        this.getClient().getSession().writeAndFlush((Object) CField.unlockSkill());
                        this.removeSkillCustomInfo(65110006);
                    }
                }
                if (lv4 <= 0) {
                    break;
                }
                this.Recharge(skillid);
            }
        }
    }

    public MapleBattleGroundCharacter getBattleGroundChr() {
        for (final MapleBattleGroundCharacter gchr : MapleBattleGroundCharacter.bchr) {
            if (gchr.getId() == this.getId()) {
                return gchr;
            }
        }
        return null;
    }

    public String getBattleGrondJobName() {
        return this.BattleGrondJobName;
    }

    public void setBattleGrondJobName(final String BattleGrondJobName) {
        this.BattleGrondJobName = BattleGrondJobName;
    }

    public void getPercentDamage(final MapleMonster monster, final int skillid, final int skillLevel, final int percent, final boolean show) {
        final MapleCharacter chr = this;
        if (chr.isAlive() && chr.getBuffedEffect(SecondaryStat.TrueSniping) == null && chr.getBuffedEffect(SecondaryStat.Etherealform) == null && chr.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null && chr.getBuffedEffect(SecondaryStat.NotDamaged) == null) {
            if (chr.getBuffedEffect(SecondaryStat.HolyMagicShell) != null) {
                if (chr.getHolyMagicShell() > 1) {
                    chr.setHolyMagicShell((byte) (chr.getHolyMagicShell() - 1));
                    final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups.put(SecondaryStat.HolyMagicShell, new Pair<Integer, Integer>((int) chr.getHolyMagicShell(), (int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.HolyMagicShell))));
                    chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(SecondaryStat.HolyMagicShell), chr));
                    chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte) 0, true, null, null, null));
                    chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte) 0, false, null, null, null), false);
                } else {
                    chr.cancelEffectFromBuffStat(SecondaryStat.HolyMagicShell);
                }
            } else if (chr.getBuffedValue(4341052)) {
                chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte) 0, true, null, null, null));
                chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte) 0, false, null, null, null), false);
            } else if (chr.getBuffedEffect(SecondaryStat.WindWall) != null) {
                final int windWall = Math.max(0, chr.getBuffedValue(SecondaryStat.WindWall) - 100 * chr.getBuffedEffect(SecondaryStat.WindWall).getZ());
                if (windWall > 1) {
                    chr.setBuffedValue(SecondaryStat.WindWall, windWall);
                    final Map<SecondaryStat, Pair<Integer, Integer>> statups2 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups2.put(SecondaryStat.WindWall, new Pair<Integer, Integer>(windWall, (int) chr.getBuffLimit(400031030)));
                    chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups2, chr.getBuffedEffect(SecondaryStat.WindWall), chr));
                } else {
                    chr.cancelEffectFromBuffStat(SecondaryStat.WindWall);
                }
            } else {
                int reduce = 0;
                if (chr.getBuffedEffect(SecondaryStat.IndieDamageReduce) != null) {
                    reduce = chr.getBuffedValue(SecondaryStat.IndieDamageReduce);
                } else if (chr.getBuffedEffect(SecondaryStat.IndieDamReduceR) != null) {
                    reduce = -chr.getBuffedValue(SecondaryStat.IndieDamReduceR);
                }
                final int minushp = -(int) (chr.getStat().getCurrentMaxHp() * (percent - reduce) / 100L);
                if (show) {
                    chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, skillid, skillLevel, 45, 0, 0, (byte) 0, true, null, null, null));
                    chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, skillid, skillLevel, 45, 0, 0, (byte) 0, false, null, null, null), false);
                }
                chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, minushp, 36, 0, 0, (byte) 0, true, null, null, null));
                chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, minushp, 36, 0, 0, (byte) 0, false, null, null, null), false);
                chr.addHP(minushp);
            }
        }
    }

    public long getAggressiveDamage() {
        return this.AggressiveDamage;
    }

    public void setAggressiveDamage(final long AggressiveDamage) {
        this.AggressiveDamage = AggressiveDamage;
    }

    public void startLotteryretry() {
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleCharacter.this.client.send(CField.showEffect("defense/count"));
                MapleCharacter.this.startSound();
            }
        }, 3000L);
    }

    public void startLottery() {
        this.client.send(CField.showEffect("Map/Effect2.img/starplanet/default"));
        this.client.send(CField.showEffect("defense/count"));
        this.startSound();
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleCharacter.this.client.send(CField.showEffect("Map/Effect2.img/starplanet/1_1"));
                MapleCharacter.this.client.send(CField.ImageTalkNpc(9000198, 2500, "#b#h ##k! 자네 운이 상당히 좋구만! 축하한다네! 바로 다음것을 긁어보게나!"));
                MapleCharacter.this.startLotteryretry();
                MapleCharacter.this.startLottery2();
            }
        }, 3100L);
    }

    public void startLottery2() {
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleCharacter.this.client.send(CField.showEffect("Map/Effect2.img/starplanet/1_2"));
                MapleCharacter.this.client.send(CField.ImageTalkNpc(9000198, 2500, "#b#h ##k! 자네 운이 상당히 좋구만! 축하한다네! 바로 다음것을 긁어보게나!"));
                MapleCharacter.this.startLottery3();
                MapleCharacter.this.startLotteryretry();
            }
        }, 6200L);
    }

    public void startLottery3() {
        server.Timer.EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleCharacter.this.client.send(CField.showEffect("Map/Effect2.img/starplanet/1_3"));
                MapleCharacter.this.client.send(CField.ImageTalkNpc(9000198, 2500, "#b#h ##k! 자네 운이 상당히 좋구만! 축하한다네! 바로 다음것을 긁어보게나!"));
            }
        }, 6200L);
    }

    public void showNotes(final int id) {
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE `notes` SET `show` = ? WHERE `id` = ?");
            ps.setInt(1, 0);
            ps.setInt(2, id);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException ex) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex2) {
            }
        }
    }

    public void gainCabinetItem(final int itemid, final int count) {
        final MapleCabinet cb = new MapleCabinet(this.client.getAccID(), itemid, count, "[Happy ! ]", "운영자가 보낸 선물입니다.보관 기간 내에 수령하지 않을 시 보관함에서 사라집니다.", PacketHelper.getKoreanTimestamp(System.currentTimeMillis() + 86400000L), 0);
        this.client.getCabiNet().add(cb);
        this.client.saveCabiNet(this.client.getCabiNet());
        this.client.send(CField.getMapleCabinetList(this.client.getCabiNet(), false, 0, true));
    }

    public void gainCabinetItemPlayer(final int itemid, final int count, final int prioed, final String st) {
        final MapleCabinet cb = new MapleCabinet(this.client.getAccID(), itemid, count, "[Happy ! ]", st, PacketHelper.getKoreanTimestamp(System.currentTimeMillis() + prioed * 24L * 60L * 60L * 1000L), 0);
        cb.setPlayerid(this.id);
        cb.setName(this.name);
        this.client.getCabiNet().add(cb);
        this.client.saveCabiNet(this.client.getCabiNet());
        this.client.send(CField.getMapleCabinetList(this.client.getCabiNet(), false, 0, true));
    }

    public int getSerenStunGauge() {
        return this.SerenStunGauge;
    }

    public void setSerenStunGauge(final int SerenStunGauge) {
        this.SerenStunGauge = SerenStunGauge;
    }

    public void addSerenGauge(int add) {
        if (!this.hasDisease(SecondaryStat.SerenDebuff)) {
            this.SerenStunGauge += add;
            if (this.SerenStunGauge >= 1000) {
                MapleMonster seren;
                this.SerenStunGauge = 0;
                EnumMap<SecondaryStat, Pair<Integer, Integer>> diseases = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
                diseases.put(SecondaryStat.GiveMeHeal, new Pair<Integer, Integer>(1, 5000));
                diseases.put(SecondaryStat.SerenDebuff, new Pair<Integer, Integer>(1, 5000));
                diseases.put(SecondaryStat.SerenDebuffUnk, new Pair<Integer, Integer>(1, 5000));
                this.client.send(SLFCGPacket.playSound("Sound/Field.img/SerenDeath/effect"));//이쪽아님
                this.client.send(SLFCGPacket.PoloFrittoEffect(4, "UI/UIWindow7.img/SerenDeath"));
                this.giveDebuff(SecondaryStat.GiveMeHeal, MobSkillFactory.getMobSkill(182, 3));
                if (this.getMapId() == 410002060 && (seren = this.getMap().getMonsterById(8880602)) != null) {
                    seren.setSerenMidNightSetTotalTime(seren.getSerenMidNightSetTotalTime() - 1);
                    if (seren.getSerenMidNightSetTotalTime() <= 0) {
                        this.getMap().broadcastMessage(MobPacket.BossSeren.SerenTimer(1, 120, 120, 0, 120, seren.getSerenTimetype() == 4 ? -1 : 1));
                        this.getMap().killAllMonsters(false);
                        this.getMap().broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e태양이 지지 않는다면 누구도 나에게 대항할 수 없다.", 100, 1500, 4, 0, 0, 1, 4));
                        for (MapleCharacter chr : this.getMap().getAllCharactersThreadsafe()) {
                            if (chr == null) {
                                continue;
                            }
                            chr.warpdelay(410000670, 7);
                        }
                    } else {
                        switch (seren.getSerenTimetype()) {
                            case 1: {
                                seren.setSerenNoonNowTime(seren.getSerenNoonNowTime() + 1);
                                seren.setSerenNoonTotalTime(seren.getSerenNoonTotalTime() + 1);
                                break;
                            }
                            case 2: {
                                seren.setSerenSunSetNowTime(seren.getSerenSunSetNowTime() + 1);
                                seren.setSerenSunSetTotalTime(seren.getSerenSunSetTotalTime() + 1);
                                break;
                            }
                            case 4: {
                                seren.setSerenDawnSetNowTime(seren.getSerenDawnSetNowTime() + 1);
                                seren.setSerenDawnSetTotalTime(seren.getSerenDawnSetTotalTime() + 1);
                            }
                        }
                        if (seren.getSerenTimetype() != 3) {
                            seren.AddSerenTotalTimeHandler(seren.getSerenTimetype(), 1, seren.getSerenTimetype() == 4 ? -1 : 1);
                        }
                    }
                }
            }
            if (this.SerenStunGauge < 0) {
                this.SerenStunGauge = 0;
            }
            this.client.send(MobPacket.BossSeren.SerenUserStunGauge(1000, this.SerenStunGauge));
        }
    }
    
        public void setEffect(int index, int value) {
        setKeyValue(0, "effect-" + index, String.valueOf(value));
    }

    public int getEffectValue(int index) {
        int value = (int) getKeyValue(0, "effect-" + index);
        if (value == -1) {
            setEffect(index, 0);
            return 0;
        }
        return value;
    }

    public List<Integer> getPrevBonusEffect() {
        List<Integer> effects = new ArrayList<>();

        for (int i = 0; i < 8; i++)
            effects.add(getEffectValue(i));

        return effects;
    }
    
     public List<Integer> getBonusEffect() {
        List<Integer> effects = new ArrayList<>();
        int damR = 0;
        int expR = 0;
        int dropR = 0;
        int mesoR = 0;
        int crD = 0;
        int bdR = 0;
        int allStatR = 0;
        int pmdR = 0;
        int Pad = 0;
        int Mad = 0;

        long zodiacRank = getKeyValue(190823, "grade");
        if (zodiacRank > 0) {
            dropR += 10 * zodiacRank;
            mesoR += 10 * zodiacRank;
            crD += 5 * zodiacRank;
            bdR += 5 * zodiacRank;
        }

        for (CustomItem.CustomItemType type : CustomItem.CustomItemType.values()) {
            if (type.ordinal() == 0)
                continue;

            int id = equippedCustomItem(type);
        }

        if (getKeyValue(999, "DamageTear") > 0) {
            int tear = (int) getKeyValue(999, "DamageTear");
            damR += tear == 8 ? 80 : tear == 7 ? 60 : tear == 6 ? 40 : tear == 5 ? 30 : tear == 4 ? 25 : tear == 3 ? 20 : tear == 2 ? 15 : 10;
        }
        if (getKeyValue(999, "ExpTear") > 0) {
            int tear = (int) getKeyValue(999, "ExpTear");
            expR += tear == 8 ? 50 : tear == 7 ? 40 : tear == 6 ? 30 : tear == 5 ? 23 : tear == 4 ? 18 : tear == 3 ? 13 : tear == 2 ? 7 : 3;
        }
        if (getKeyValue(999, "DropTear") > 0) {
            int tear = (int) getKeyValue(999, "DropTear");
            dropR += tear == 8 ? 300 : tear == 7 ? 180 : tear == 6 ? 120 : tear == 5 ? 80 : tear == 4 ? 60 : tear == 3 ? 40 : tear == 2 ? 20 : 10;
        }
        if (getKeyValue(999, "MesoTear") > 0) {
            int tear = (int) getKeyValue(999, "MesoTear");
            mesoR += tear == 8 ? 120 : tear == 7 ? 100 : tear == 6 ? 90 : tear == 5 ? 80 : tear == 4 ? 60 : tear == 3 ? 40 : tear == 2 ? 20 : 10;
        }
        if (getKeyValue(999, "CridamTear") > 0) {
            int tear = (int) getKeyValue(999, "CridamTear");
            crD += tear == 8 ? 100 : tear == 7 ? 70 : tear == 6 ? 50 : tear == 5 ? 30 : tear == 4 ? 20 : tear == 3 ? 15 : tear == 2 ? 10 : 5;
        }
        if (getKeyValue(999, "BossdamTear") > 0) {
            int tear = (int) getKeyValue(999, "BossdamTear");
            bdR += tear == 8 ? 150 : tear == 7 ? 120 : tear == 6 ? 100 : tear == 5 ? 70 : tear == 4 ? 50 : tear == 3 ? 30 : tear == 2 ? 20 : 10;
        }

        int hGrade = getHgrade();

        if (hGrade == 1) {
            damR += 5;
            bdR += 10;
            crD += 5;
            allStatR += 10;
        } else if (hGrade == 2) {
            expR += 10;
            damR += 10;
            bdR += 20;
            crD += 10;
            allStatR += 15;
        } else if (hGrade == 3) {
            expR += 20;
            damR += 20;
            bdR += 30;
            crD += 15;
            allStatR += 25;
        } else if (hGrade == 4) {
            expR += 30;
            pmdR += 20;
            damR += 30;
            bdR += 40;
            crD += 30;
            allStatR += 40;
        } else if (hGrade == 5) {
            expR += 40;
            pmdR += 30;
            damR += 40;
            bdR += 70;
            crD += 50;
            allStatR += 50;
        } else if (hGrade == 6) {
            expR += 50;
            pmdR += 40;
            damR += 50;
            bdR += 100;
            crD += 70;
            allStatR += 60;
        }

        effects.add(damR);
        effects.add(expR);
        effects.add(dropR);
        effects.add(mesoR);
        effects.add(crD);
        effects.add(bdR);
        effects.add(allStatR);
        effects.add(pmdR);

        return effects;
    }

    public void SetUnionPriset(final int priset) {
        final int quest = (priset == 3) ? 500011 : ((priset == 4) ? 500012 : ((priset == 5) ? 500013 : 0));
        if (quest > 0) {
            this.client.setKeyValue("prisetOpen" + priset, "1");
            if (this.getKeyValue(quest + 10, "endDate") != 1L) {
                this.setKeyValue(quest + 10, "endDate", "1");
            }
            this.updateInfoQuest(quest, "endDate=99/12/31/12/59");
        }
    }

    public void checkRestDayMonday() {
        final KoreaCalendar kc = new KoreaCalendar();
        final long keys = Long.parseLong(this.getV("EnterDayWeekMonday"));
        final Calendar clear = new GregorianCalendar((int) keys / 10000, (int) (keys % 10000L / 100L) - 1, (int) keys % 100);
        final Calendar ocal = Calendar.getInstance();
        int yeal = clear.get(1);
        final int days = clear.get(5);
        final int day2 = clear.get(7);
        final int maxday = clear.getMaximum(5);
        int month = clear.get(2);
        final int check = (day2 == 7) ? 2 : ((day2 == 6) ? 3 : ((day2 == 5) ? 4 : ((day2 == 4) ? 5 : ((day2 == 3) ? 6 : ((day2 == 2) ? 7 : ((day2 == 1) ? 1 : 0))))));
        int afterday = days + check;
        if (afterday > maxday) {
            afterday -= maxday;
            ++month;
        }
        if (month > 12) {
            ++yeal;
            month = 1;
        }
        final Calendar after = new GregorianCalendar(yeal, month, afterday);
        if (after.getTimeInMillis() < System.currentTimeMillis()) {
            this.removeV("EnterDayWeekMonday");
            this.addKV("EnterDayWeekMonday", kc.getYears() + kc.getMonths() + kc.getDays());
            this.removeKeyValue(34151);
            this.removeV("ArcQuest6");
            this.removeV("ArcQuest7");
            this.setKeyValue(100466, "Score", "0");
            this.setKeyValue(100466, "Floor", "0");
            for (final Map.Entry<MapleQuest, MapleQuestStatus> quest : this.getQuest_Map().entrySet()) {
                if (quest != null && quest.getKey().getName().contains("[주간 퀘스트]")) {
                    quest.getValue().setStatus((byte) 0);
                    quest.getValue().setCustomData("");
                    this.client.send(CWvsContext.InfoPacket.updateQuest(quest.getValue()));
                }
            }
        }
    }

    public boolean checkRestDay(final boolean weekly, final boolean acc) {
        final MapleCharacter chr = this;
        final KoreaCalendar kc = new KoreaCalendar();
        boolean checkd = true;
        final String key = weekly ? "EnterDayWeek" : "EnterDay";
        final List<Core> del = new ArrayList<Core>();
        for (final Core m : chr.getCore()) {
            if (m.getPeriod() > 0L && m.getPeriod() <= System.currentTimeMillis()) {
                del.add(m);
            }
        }
        if (!del.isEmpty()) {
            for (final Core m : del) {
                this.getCore().remove(m);
            }
            this.dropMessage(5, "특수 코어가 기간이 지나 삭제 되었습니다.");
            this.client.send(CWvsContext.UpdateCore(this));
        }
        if (weekly) {
            long keys = Long.parseLong(chr.getV(key));
            if (acc) {
                keys = Long.parseLong(this.client.getKeyValue(key));
            }
            final Calendar clear = new GregorianCalendar((int) keys / 10000, (int) (keys % 10000L / 100L) - 1, (int) keys % 100);
            final Calendar ocal = Calendar.getInstance();
            int yeal = clear.get(1);
            final int days = clear.get(5);
            final int day = ocal.get(7);
            final int day2 = clear.get(7);
            final int maxday = clear.getMaximum(5);
            int month = clear.get(2);
            int check = (day2 == 5) ? 7 : ((day2 == 6) ? 6 : ((day2 == 7) ? 5 : 0));
            if (check == 0) {
                for (int i = day2; i < 5; ++i) {
                    ++check;
                }
            }
            int afterday = days + check;
            if (afterday > maxday) {
                afterday -= maxday;
                ++month;
            }
            if (month > 12) {
                ++yeal;
                month = 1;
            }
            final Calendar after = new GregorianCalendar(yeal, month, afterday);
            if (after.getTimeInMillis() > System.currentTimeMillis()) {
                checkd = false;
            }
        } else if (acc) {
            if (this.client.getKeyValue(key).equals(kc.getYears() + kc.getMonths() + kc.getDays())) {
                checkd = false;
            }
        } else if (this.getV(key).equals(kc.getYears() + kc.getMonths() + kc.getDays())) {
            checkd = false;
        }
        if (checkd) {
            if (!weekly && Long.parseLong(chr.getV(key)) % 10000L / 100L != kc.getMonth() && ServerConstants.Event_MapleLive) {
                for (int j = 501497; j <= 501522; ++j) {
                }
            }
            this.ResetData(weekly, acc);
        }
        return checkd;
    }

    public void ResetData(boolean weekly, boolean acc) {
        KoreaCalendar kc = new KoreaCalendar();
        String key = weekly ? "EnterDayWeek" : "EnterDay";
        String[] clientDateWeekKeyValues = {"UnionCoinNujuk", "WishCoin", "WishCoinWeekGain"};
        List<Pair<Integer, String>> DateWeekKeyValues = new ArrayList<>(Arrays.asList((Pair<Integer, String>[]) new Pair[]{new Pair<>(Integer.valueOf(1068), "count")}));
        List<Pair<Integer, String>> clientCustomDatasWeek = new ArrayList<>(Arrays.asList((Pair<Integer, String>[]) new Pair[]{new Pair<>(Integer.valueOf(100795), "weekspoint")}));
        List<Pair<Integer, String>> clientCustomKeyValuesWeek = new ArrayList<>(Arrays.asList((Pair<Integer, String>[]) new Pair[]{new Pair<>(Integer.valueOf(501468), "week"), new Pair<>(Integer.valueOf(501470), "weeklyF"), new Pair<>(Integer.valueOf(501468), "reward")}));
        String[] clientDateKeyValues = {
            "Tester0", "Tester1", "Tester2", "Tester3", "Tester4", "Tester5", "Tester6", "Tester7", "Tester8", "Tester9",
            "Tester10", "Tester11", "Tester12", "Tester13", "Tester14", "Tester15", "Tester9", "jump_1", "jump_2", "jump_3",
            "jump_4", "jump_5", "jump_6", "dailyGiftComplete", "mPark", "mpark_t", "BloomingReward", "TyKitchenReward", "minigame"};
        List<Pair<Integer, String>> DateKeyValues = new ArrayList<>(Arrays.asList((Pair<Integer, String>[]) new Pair[]{new Pair(Integer.valueOf(100794), "today"), new Pair(Integer.valueOf(100794), "lock"), new Pair(Integer.valueOf(501367), "reward"), new Pair(Integer.valueOf(210302), "GP")}));
        List<Pair<Integer, String>> clientCustomDatas = new ArrayList<>(Arrays.asList((Pair<Integer, String>[]) new Pair[]{new Pair<>(Integer.valueOf(238), "count"), new Pair<>(Integer.valueOf(238), "T")}));
        List<Pair<Integer, String>> clientCustomKeyValues = new ArrayList<>(Arrays.asList((Pair<Integer, String>[]) new Pair[]{new Pair<>(Integer.valueOf(501470), "dailyF")}));
        String[] ResetKeyValue = {"ArcQuest0", "ArcQuest1", "ArcQuest2", "ArcQuest3", "ArcQuest4", "ArcQuest5", "ArcQuest8", "AthQuest1", "DojoCount", "TyKitchenReward", "follower"};
        if (acc) {
            this.client.removeKeyValue(key);
            this.client.setKeyValue(key, kc.getYears() + kc.getMonths() + kc.getDays());
        } else {
            removeV(key);
            addKV(key, kc.getYears() + kc.getMonths() + kc.getDays());
        }
        if (weekly) {
            if (acc) {
                for (Map.Entry<Integer, String> q : getClient().getCustomKeyValue().entrySet()) {
                    if (((Integer) q.getKey()).intValue() == 501367) {
                        String bosslist = "";
                        for (int i = 0; i < ServerConstants.NeoPosList.size(); i++) {
                            bosslist = bosslist + "0";
                        }
                        this.client.setCustomKeyValue(((Integer) q.getKey()).intValue(), "reward", bosslist);
                        continue;
                    }
                    if (((Integer) q.getKey()).intValue() >= 501470 && ((Integer) q.getKey()).intValue() <= 501496) {
                        this.client.setCustomKeyValue(((Integer) q.getKey()).intValue(), "state", "0");
                        this.client.setCustomKeyValue(((Integer) q.getKey()).intValue(), "count", "0");
                    }
                }
                for (Pair<Integer, String> keyz : clientCustomDatasWeek) {
                    if (this.client.getCustomData(((Integer) keyz.getLeft()).intValue(), keyz.getRight()) != null) {
                        this.client.setCustomData(((Integer) keyz.getLeft()).intValue(), keyz.getRight(), "0");
                    }
                }
                for (String keyValue : clientDateWeekKeyValues) {
                    if (this.client.getKeyValue(keyValue) != null) {
                        if (keyValue.equals("WishCoin")) {
                            String bosslist = "";
                            for (int i = 0; i < ServerConstants.NeoPosList.size(); i++) {
                                bosslist = bosslist + "0";
                            }
                            this.client.setKeyValue("WishCoin", bosslist);
                        } else {
                            this.client.setKeyValue(keyValue, "0");
                        }
                    }
                }
                for (Pair<Integer, String> keyz : clientCustomKeyValuesWeek) {
                    if (this.client.getCustomKeyValueStr(((Integer) keyz.getLeft()).intValue(), keyz.getRight()) != null) {
                        if (((Integer) keyz.getLeft()).intValue() == 501468 && ((String) keyz.getRight()).equals("reward")) {
                            String bosslist = "";
                            for (int i = 0; i < ServerConstants.NeoPosList.size(); i++) {
                                bosslist = bosslist + "0";
                            }
                            this.client.setCustomKeyValue(((Integer) keyz.getLeft()).intValue(), keyz.getRight(), bosslist);
                            continue;
                        }
                        this.client.setCustomKeyValue(((Integer) keyz.getLeft()).intValue(), keyz.getRight(), "0");
                    }
                }
            } else {
                for (Pair<Integer, String> keyz : DateWeekKeyValues) {
                    if (getKeyValue(((Integer) keyz.getLeft()).intValue(), keyz.getRight()) >= 0L) {
                        setKeyValue(((Integer) keyz.getLeft()).intValue(), keyz.getRight(), (((Integer) keyz.getLeft()).intValue() == 1068) ? "60" : "0");
                    }
                }
            }
        } else if (acc) {

            for (String keyValue : clientDateKeyValues) {
                if (this.client.getKeyValue(keyValue) != null) {
                    this.client.setKeyValue(keyValue, "0");
                }
                if (keyValue.equals("dailyGiftComplete")) {
                    this.client.send(CWvsContext.updateDailyGift("count=0;day=" + this.client.getKeyValue("dailyGiftDay") + ";date=" + GameConstants.getCurrentDate_NoTime()));
                    this.client.send(CField.dailyGift(this.client.getPlayer(), 1, 0));
                    if (kc.getDayt() == 1) {
                        this.client.setKeyValue("dailyGiftDay", "0");
                    }
                }
            }
            for (Pair<Integer, String> keyz : clientCustomDatas) {
                if (this.client.getCustomData(((Integer) keyz.getLeft()).intValue(), keyz.getRight()) != null) {
                    this.client.setCustomData(((Integer) keyz.getLeft()).intValue(), keyz.getRight(), "0");
                    if (((Integer) keyz.getLeft()).intValue() == 238 && ((String) keyz.getRight()).equals("T")) {
                        this.client.setCustomData(238, "T", GameConstants.getCurrentFullDate());
                    }
                }
            }
            for (Map.Entry<MapleQuest, MapleQuestStatus> quest : this.client.getQuests().entrySet()) {
                if (((MapleQuest) quest.getKey()).getId() == 16011 || ((MapleQuest) quest.getKey()).getId() == 16012) {
                    ((MapleQuestStatus) quest.getValue()).setStatus((byte) 0);
                    ((MapleQuestStatus) quest.getValue()).setCustomData("");
                    this.client.send(CWvsContext.InfoPacket.updateQuest(quest.getValue()));
                }
            }
            for (Pair<Integer, String> keyz : clientCustomKeyValues) {
                if (this.client.getCustomKeyValueStr(((Integer) keyz.getLeft()).intValue(), keyz.getRight()) != null) {
                    this.client.setCustomKeyValue(((Integer) keyz.getLeft()).intValue(), keyz.getRight(), "0");
                }
            }
        } else {
            for (int i = 100829; i <= 100853; i++) {
                removeKeyValue(i);
            }
            for (Pair<Integer, String> keyz : DateKeyValues) {
                if (getKeyValue(((Integer) keyz.getLeft()).intValue(), keyz.getRight()) > 0L) {
                    setKeyValue(((Integer) keyz.getLeft()).intValue(), keyz.getRight(), "0");
                }
            }
            for (String keyz : ResetKeyValue) {
                if (getV(keyz) != null) {
                    removeV(keyz);
                }
            }
        }
        this.client.send(SLFCGPacket.StarDustUI("UI/UIWindowEvent.img/starDust_18th", getKeyValue(100794, "sum"), getKeyValue(100794, "point"), (getKeyValue(100794, "lock") == 1L)));
    }

    public List<Triple<String, String, String>> FuckingRanking(int type) {
        List<Triple<String, String, String>> data = new ArrayList<>();
        data.add(new Triple<>("", "", ""));
        String dbline = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            if (type == 0) {
                dbline = "SELECT * FROM characters WHERE gm = 0 ORDER BY bounsfish DESC LIMIT 10";
                ps = con.prepareStatement(dbline);
                rs = ps.executeQuery();
                while (rs.next()) {
                    data.add(new Triple<>(String.valueOf(rs.getInt("bounsfish")), rs.getString("name"), ""));
                }
            } else if (type == 1) {
                dbline = "SELECT * FROM characters WHERE gm = 0 ORDER BY bounsvdance DESC LIMIT 10";
                ps = con.prepareStatement(dbline);
                rs = ps.executeQuery();
                while (rs.next()) {
                    data.add(new Triple<>(HelpTools.CalcComa(rs.getInt("bounsvdance")), rs.getString("name"), ""));
                }
            } else if (type == 2) {
                dbline = "SELECT * FROM characters WHERE gm = 0 ORDER BY meso DESC LIMIT 10";
                ps = con.prepareStatement(dbline);
                rs = ps.executeQuery();
                while (rs.next()) {
                    data.add(new Triple<>(String.valueOf(rs.getInt("job")), rs.getString("name"), HelpTools.CalcComa(rs.getLong("meso"))));
                }
            } else if (type == 3) {
                dbline = "SELECT * FROM characters WHERE gm = 0 ORDER BY fame DESC LIMIT 10";
                ps = con.prepareStatement(dbline);
                rs = ps.executeQuery();
                while (rs.next()) {
                    data.add(new Triple<>(String.valueOf(rs.getInt("job")), rs.getString("name"), HelpTools.CalcComa(rs.getInt("fame"))));
                }
            } else if (type == 4) {
                dbline = "SELECT * FROM characters WHERE gm = 0 ORDER BY level DESC LIMIT 10";
                ps = con.prepareStatement(dbline);
                rs = ps.executeQuery();
                while (rs.next()) {
                    data.add(new Triple<>(String.valueOf(rs.getInt("job")), rs.getString("name"), String.valueOf(rs.getInt("level"))));
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public Pair<Integer, Integer> getEquippedSpecialCore() {
        for (final Core core : this.getCore()) {
            if (core.getState() == 2 && core.getSpCoreOption() != null && core.getSpCoreOption().getSkillid() >= 400007000 && core.getSpCoreOption().getSkillid() <= 400007999) {
                return new Pair<Integer, Integer>(core.getCoreId(), core.getSpCoreOption().getSkillid());
            }
        }
        return null;
    }

    public void resetSpecialCoreStat() {
        this.setSpLastValidTime(0L);
        this.setSpCount(0);
        this.setSpAttackCountMobId(0);
    }

    public void applySpecialCoreSkills(final SpecialCoreOption spOption) {
        final long time = System.currentTimeMillis();
        final String effectType = spOption.getEffectType();
        switch (effectType) {
            case "selfbuff": {
                final SecondaryStatEffect effect = SkillFactory.getSkill(spOption.getSkillid()).getEffect(spOption.getSkilllevel());
                if (effect != null) {
                    effect.applyTo(this);
                    break;
                }
                break;
            }
            case "heal": {
                this.addHP(this.getStat().getCurrentMaxHp() * spOption.getHeal_percent() / 100L);
                break;
            }
            case "reduceCooltime": {
                for (final MapleCoolDownValueHolder a : this.getCooldowns()) {
                    final Skill skill = SkillFactory.getSkill(a.skillId);
                    if (skill != null && skill.isHyper() && a.skillId >= 400001000 && a.skillId <= 400059999) {
                        continue;
                    }
                    final int reduc = (int) ((System.currentTimeMillis() - a.startTime) * spOption.getReducePercent() / 100L);
                    this.changeCooldown(a.skillId, -reduc);
                }
                break;
            }
        }
        this.addCooldown(spOption.getSkillid(), time, spOption.getCooltime());
        this.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(this, "Effect/CharacterEff.img/VMatrixSP"));
    }

    public void checkSpecialCoreSkills(final String condType, final int mobObjectId, final SecondaryStatEffect effect) {
        try {
            final long time = System.currentTimeMillis();
            final Pair<Integer, Integer> coredata = this.getEqpSpCore();
            if (coredata == null) {
                return;
            }
            final int coreId = coredata.left;
            final int coreSkillId = coredata.right;
            final SpecialCoreOption spOption = MatrixHandler.getCores().get(coreId).left.getSpCoreOption();
            if (spOption == null || !spOption.getCondType().equals(condType)) {
                return;
            }
            final String condType2 = spOption.getCondType();
            switch (condType2) {
                case "hitCount":
                case "attackCount":
                case "killCount": {
                    if (spOption.getValidTime() > 0 && time > this.getSpLastValidTime() + spOption.getValidTime()) {
                        this.setSpCount(0);
                    }
                    if (!this.skillisCooling(coreSkillId)) {
                        this.gainSpCount(1);
                        if (this.getSpCount() >= spOption.getCount()) {
                            this.applySpecialCoreSkills(spOption);
                            this.gainSpCount(-spOption.getCount());
                        }
                    }
                    this.setSpLastValidTime(time);
                    break;
                }
                case "attackCountMob": {
                    if (spOption.getValidTime() > 0 && time > this.getSpLastValidTime() + spOption.getValidTime()) {
                        this.setSpCount(0);
                        this.setSpAttackCountMobId(0);
                    }
                    if (mobObjectId != this.getSpAttackCountMobId()) {
                        this.setSpAttackCountMobId(mobObjectId);
                        this.setSpCount(0);
                    } else if (!this.skillisCooling(coreSkillId)) {
                        this.gainSpCount(1);
                        if (this.getSpCount() >= spOption.getCount()) {
                            this.applySpecialCoreSkills(spOption);
                            this.gainSpCount(-spOption.getCount());
                        }
                    }
                    this.setSpLastValidTime(time);
                    break;
                }
                case "rune":
                case "cooltime": {
                    if (!this.skillisCooling(coreSkillId) && effect != null) {
                        this.applySpecialCoreSkills(spOption);
                        break;
                    }
                    break;
                }
                case "combokill": {
                    if (this.getMonsterCombo() > 0 && this.getMonsterCombo() % spOption.getCount() == 0 && !this.skillisCooling(coreSkillId)) {
                        this.applySpecialCoreSkills(spOption);
                    }
                    this.setSpLastValidTime(time);
                    break;
                }
                case "die": {
                    if (!this.skillisCooling(coreSkillId)) {
                        this.applySpecialCoreSkills(spOption);
                        break;
                    }
                    break;
                }
                case "prob": {
                    if (spOption.getValidTime() > 0 && time > this.getSpLastValidTime() + spOption.getValidTime()) {
                        this.setSpLastValidTime(time);
                    }
                    if (this.skillisCooling(coreSkillId)) {
                        break;
                    }
                    final int succ = (int) (1.0 / spOption.getProb());
                    if (Randomizer.isSuccess(succ, 1000000)) {
                        this.applySpecialCoreSkills(spOption);
                        break;
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getSpAttackCountMobId() {
        return this.spAttackCountMobId;
    }

    public void setSpAttackCountMobId(final int spAttackCountMobId) {
        this.spAttackCountMobId = spAttackCountMobId;
    }

    public int getSpCount() {
        return this.spCount;
    }

    public void setSpCount(final int spCount) {
        this.spCount = spCount;
    }

    public void gainSpCount(final int spCount) {
        this.spCount += spCount;
    }

    public long getSpLastValidTime() {
        return this.spLastValidTime;
    }

    public void setSpLastValidTime(final long spLastValidTime) {
        this.spLastValidTime = spLastValidTime;
    }

    public Pair<Integer, Integer> getEqpSpCore() {
        return this.eqpSpCore;
    }

    public void setEqpSpCore(final Pair<Integer, Integer> eqpSpCore) {
        if (this.eqpSpCore != eqpSpCore) {
            this.resetSpecialCoreStat();
            this.eqpSpCore = eqpSpCore;
        }
    }

    public List<MapleSavedEmoticon> getSavedEmoticon() {
        return this.savedEmoticon;
    }

    public void setSavedEmoticon(final List<MapleSavedEmoticon> savedEmoticon) {
        this.savedEmoticon = savedEmoticon;
    }

    public List<MapleChatEmoticon> getEmoticonTabs() {
        return this.emoticonTabs;
    }

    public void setEmoticonTabs(final List<MapleChatEmoticon> emoticons) {
        this.emoticonTabs = emoticons;
    }

    public List<Triple<Long, Integer, Short>> getEmoticons() {
        return this.emoticons;
    }

    public void setEmoticons(final List<Triple<Long, Integer, Short>> emoticons) {
        this.emoticons = emoticons;
    }

    public void gainEmoticon(final int tab) {
        if (!this.hasEmoticon(tab)) {
            final short slot = (short) (this.getEmoticonTabs().size() + 1);
            final MapleChatEmoticon em = new MapleChatEmoticon(this.getId(), tab, PacketHelper.getTime(-2L), null);
            this.getEmoticonTabs().add(em);
            this.client.send(CField.getChatEmoticon((byte) 0, slot, (short) 0, tab, ""));
            this.getEmoticons().clear();
            ChatEmoticon.LoadChatEmoticons(this, this.getEmoticonTabs());
        }
    }

    public boolean hasEmoticon(final int tab) {
        for (final MapleChatEmoticon em : this.getEmoticonTabs()) {
            if (em.getEmoticonid() == tab) {
                return true;
            }
        }
        return false;
    }

    public List<Pair<Integer, Short>> getEmoticonBookMarks() {
        return this.emoticonBookMarks;
    }

    public void setEmoticonBookMarks(final List<Pair<Integer, Short>> emoticonBookMarks) {
        this.emoticonBookMarks = emoticonBookMarks;
    }

    public short getEmoticonFreeSlot() {
        final List<Short> slots = new ArrayList<Short>();
        for (final Triple<Long, Integer, Short> a : this.getEmoticons()) {
            slots.add(a.right);
        }
        for (short i = 1; i <= this.getEmoticons().size(); ++i) {
            if (!slots.contains(i)) {
                return i;
            }
        }
        return 1;
    }

    public void gainSuddenMission(final int startquestid, final int midquestid, final boolean first) {
        final long nowtime = first ? PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) : this.getKeyValue(51351, "starttime");
        final long endtime = first ? (nowtime + 17980000000L) : this.getKeyValue(51351, "endtime");
        if (first) {
            this.removeKeyValue(51351);
            this.setKeyValue(51351, "starttime", nowtime + "");
            this.setKeyValue(51351, "endtime", endtime + "");
            this.setKeyValue(51351, "startquestid", startquestid + "");
            this.setKeyValue(51351, "midquestid", midquestid + "");
            this.setKeyValue(51351, "queststat", "2");
            final int[] array;
            final int[] questdel = array = new int[]{49001, 49002, 49003, 49012, 49013, 49014, 49016};
            for (final Integer qid : array) {
                MapleQuest.getInstance(qid).forfeit(this);
            }
            switch (startquestid) {
                case 49001:
                case 49002:
                case 49003:
                case 49012:
                case 49013:
                case 49014:
                case 49016: {
                    MapleQuest.getInstance(startquestid).forceStart(this, 0, "0");
                    break;
                }
            }
        }
        this.getClient().send(CWvsContext.updateSuddenQuest(startquestid, true, endtime, ""));
        this.getClient().send(CWvsContext.updateSuddenQuest(midquestid, false, nowtime, "count=0;Quest=" + startquestid + ";state=1"));
        this.getClient().send(CWvsContext.updateSuddenQuest(midquestid, false, nowtime, "count=0;Quest=" + startquestid + ";state=2"));
    }

    public void CombokillHandler(final MapleMonster monster, final int type, final int multikill) {
        final MapleCharacter player = this;
        if (monster == null) {
            return;
        }
        if (multikill >= 3) {
            final long comboexp = monster.getStats().getExp() * multikill;
            float n22 = 0.0f;
            switch (multikill) {
                case 3: {
                    n22 = 0.03f;
                    break;
                }
                case 4: {
                    n22 = 0.08f;
                    break;
                }
                case 5: {
                    n22 = 0.15f;
                    break;
                }
                case 6: {
                    n22 = 0.198f;
                    break;
                }
                case 7: {
                    n22 = 0.252f;
                    break;
                }
                case 8: {
                    n22 = 0.312f;
                    break;
                }
                case 9: {
                    n22 = 0.378f;
                    break;
                }
                default: {
                    n22 = 0.45f;
                    break;
                }
            }
            if (this.getKeyValue(51351, "startquestid") == 49007L || this.getKeyValue(51351, "startquestid") == 49008L || (this.getKeyValue(51351, "startquestid") == 49009L && this.getKeyValue(51351, "queststat") == 2L)) {
                if (this.getKeyValue(51351, "multikill") < 0L) {
                    this.setKeyValue(51351, "multikill", "0");
                }
                this.setKeyValue(51351, "multikill", this.getKeyValue(51351, "multikill") + 1L + "");
                final int completecombo = (this.getKeyValue(51351, "startquestid") == 49007L) ? 15 : ((this.getKeyValue(51351, "startquestid") == 49008L) ? 25 : ((this.getKeyValue(51351, "startquestid") == 49009L) ? 50 : 0));
                if (this.getKeyValue(51351, "multikill") >= completecombo) {
                    this.setKeyValue(51351, "queststat", "3");
                    this.getClient().send(CWvsContext.updateSuddenQuest((int) this.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + this.getKeyValue(51351, "startquestid") + ";state=3;"));
                } else {
                    this.getClient().send(CWvsContext.updateSuddenQuest((int) this.getKeyValue(51351, "startquestid"), false, this.getKeyValue(51351, "endtime"), "MultiKC=" + this.getKeyValue(51351, "multikill") + ";"));
                }
            }
            player.getClient().getSession().writeAndFlush((Object) CWvsContext.InfoPacket.multiKill((multikill > 10) ? 10 : multikill, comboexp));
            player.gainExp((long) (comboexp * n22), false, false, false);
        }
        if (System.currentTimeMillis() - player.getMonsterComboTime() > 8000L) {
            player.setMonsterCombo((short) 0);
        } else if (!GameConstants.isContentsMap(player.getMapId())) {
            if (player.getMonsterCombo() < 50000) {
                player.addMonsterCombo((short) 1);
            }
            if (player.getV("d_combo") == null) {
                player.addKV("d_combo", "" + player.getMonsterCombo());
            } else if (player.getMonsterCombo() > Long.parseLong(player.getV("d_combo"))) {
                player.addKV("d_combo", "" + player.getMonsterCombo());
            }
            if (this.getKeyValue(51351, "startquestid") == 49004L || this.getKeyValue(51351, "startquestid") == 49005L || (this.getKeyValue(51351, "startquestid") == 49006L && this.getKeyValue(51351, "queststat") == 2L)) {
                final int completecombo2 = (this.getKeyValue(51351, "startquestid") == 49006L) ? 300 : ((this.getKeyValue(51351, "startquestid") == 49005L) ? 200 : ((this.getKeyValue(51351, "startquestid") == 49004L) ? 100 : 0));
                if (this.monsterCombo >= completecombo2) {
                    this.setKeyValue(51351, "queststat", "3");
                    this.getClient().send(CWvsContext.updateSuddenQuest((int) this.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + this.getKeyValue(51351, "startquestid") + ";state=3;"));
                } else {
                    this.getClient().send(CWvsContext.updateSuddenQuest((int) this.getKeyValue(51351, "startquestid"), false, this.getKeyValue(51351, "endtime"), "ComboK=" + (this.monsterCombo + 1) + ";"));
                }
            }
            player.checkSpecialCoreSkills("combokill", monster.getObjectId(), null);
            player.getClient().getSession().writeAndFlush((Object) CWvsContext.InfoPacket.comboKill(player.getMonsterCombo(), monster.getObjectId()));
            player.setMonsterComboTime(System.currentTimeMillis());
            if (player.getMonsterCombo() % 50 == 0) {
                int itemId;
                if (player.getMonsterCombo() < 350) {
                    itemId = 2023484;
                } else if (player.getMonsterCombo() < 750) {
                    itemId = 2023494;
                } else if (player.getMonsterCombo() < 2000) {
                    itemId = 2023495;
                } else {
                    itemId = 2023669;
                }
                player.getMap().spawnMobPublicDrop(new Item(itemId, (short) 0, (short) 1, 0), monster.getTruePosition(), monster, player, (byte) 0, 0);
            }
        }
    }
    
    public void ZeroSkillCooldown(final int skillid) {
        int nocool = 0;
        switch (skillid) {
            case 101100201:
            case 101101200: {
                nocool = 101101200;
                break;
            }
            case 101120201:
            case 101120204: {
                nocool = 101121200;
                break;
            }
            case 101110200:
            case 101110203: {
                nocool = 101110200;
                break;
            }
            case 101120100:
            case 101120101:
            case 101120104:
            case 101120105: {
                nocool = 101121100;
                break;
            }
            case 101100101: {
                nocool = 101101100;
                break;
            }
            case 101110102:
            case 101110104: {
                nocool = 101111100;
                break;
            }
        }
        final int[] array;
        final int[] cooling = array = new int[]{101121200, 101110200, 101101200, 101101100, 101121100, 101111100};
        for (final int cool : array) {
            if (this.skillisCooling(cool) && cool != nocool) {
                this.changeCooldown(cool, -4000);
            }
        }
    }

    public void FeverTime(final boolean start, final boolean delay) {
        if (start && delay) {
            this.getClient().getSession().writeAndFlush((Object) CField.FeverMessage((byte) 1));
        } else if (start && !delay) {
            this.getClient().getSession().writeAndFlush((Object) CField.FeverMessage((byte) 2));
        } else if (!start && delay) {
            this.getClient().getSession().writeAndFlush((Object) CField.FeverMessage((byte) 3));
        } else if (!start && !delay) {
            this.getClient().getSession().writeAndFlush((Object) CField.FeverMessage((byte) 4));
        }
    }

    public Map<MapleQuest, MapleQuestStatus> getQuests() {
        return this.quests;
    }

    public void setQuests(final Map<MapleQuest, MapleQuestStatus> quests) {
        this.quests = quests;
    }

    public MapleTyoonKitchen getMtk() {
        return this.Mtk;
    }

    public void setMtk(final MapleTyoonKitchen Mtk) {
        this.Mtk = Mtk;
    }

    public List<SecondAtom> getSaList() {
        return this.SaList;
    }

    public void setSaList(final List<SecondAtom> SaList) {
        this.SaList = SaList;
    }

    public void spawnSecondAtom(final List<SecondAtom> list) {
        for (final SecondAtom a : list) {
            if (!this.SaList.contains(a)) {
                this.setSkillCustomInfo(9877654, this.getSkillCustomValue0(9877654) + 1L, 0L);
                a.setObjectId((int) this.getSkillCustomValue0(9877654));
                this.SaList.add(a);
            }
        }
        this.client.send(CField.spawnSecondAtoms(this.getId(), list, 0));
    }

    public void removeSecondAtom(final int objid) {
        for (final SecondAtom sa : this.SaList) {
            if (sa != null && objid == sa.getObjectId()) {
                this.SaList.remove(sa);
                break;
            }
        }
        this.client.send(CField.removeSecondAtom(this.getId(), objid));
    }

    public SecondAtom getSecondAtom(final int objid) {
        for (final SecondAtom sa : this.SaList) {
            if (objid == sa.getObjectId()) {
                return sa;
            }
        }
        return null;
    }

    static {
        MapleCharacter.XenonSupplyTask = null;
    }

    public void dropShowInfo(String msg) {
        getClient().send(CWvsContext.getTopMsg(msg));
    }

    public final boolean getMonster(final int mobid) {
        for (MapleMapObject obj : this.getMap().getAllMonster()) {
            final MapleMonster mob = (MapleMonster) obj;
            if (mob.getId() == mobid) {
                return true;
            }
        }
        return false;
    }

    public final void spawnMob(final int id, final int x, final int y) {
        spawnMob(id, 1, new Point(x, y));
    }

    private final void spawnMob(final int id, final int qty, final Point pos) {
        for (int i = 0; i < qty; i++) {
            getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(id), pos);
        }
    }

    public void gainItemAllStat(int itemid, short quantity, short allstat, short wmtk) {
        Equip equip = new Equip(itemid, quantity, (byte) 0);
        equip.setStr(allstat);
        equip.setDex(allstat);
        equip.setInt(allstat);
        equip.setLuk(allstat);
        if (wmtk != -1) {
            equip.setWatk(wmtk);
            equip.setMatk(wmtk);
        }
        MapleInventoryManipulator.addFromDrop(client, equip, true);
    }

    public Map<Byte, Integer> getTotems() {
        final Map<Byte, Integer> eq = new HashMap<>();
        for (final Item item : inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            eq.put((byte) (item.getPosition() + 5000), item.getItemId());
        }
        return eq;
    }

    private class MapleCharacterManagement implements Runnable {

        public void handleMobs(final long time) {
            MapleMonster mob;
            if ((mob = MapleCharacter.this.getMap().getMonsterById(8870100)) != null && MapleCharacter.this.getDisease(SecondaryStat.VampDeath) != null) {
                final MapleDiseases skill = MapleCharacter.this.getDisease(SecondaryStat.VampDeath);
                if (skill != null) {
                    mob.heal(63000000L, 0L, true);
                }
            }
            if (MapleCharacter.this.getMap().getId() / 10000 == 92507) {
                if (MapleCharacter.this.getDojoStartTime() > 0 && MapleCharacter.this.getBuffedEffect(SecondaryStat.MobZoneState) == null) {
                    final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups.put(SecondaryStat.MobZoneState, new Pair<Integer, Integer>(1, 0));
                    MapleCharacter.this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, null, MapleCharacter.this.getPlayer()));
                }
                if (!MapleCharacter.this.getDojoStop()) {
                    MapleCharacter.this.setDojoStartTime(MapleCharacter.this.getDojoStartTime() + 1);
                    if (MapleCharacter.this.getDojoStartTime() >= 900) {
                        MapleCharacter.this.getClient().getSession().writeAndFlush((Object) CField.environmentChange("dojang/timeOver", 19));
                        MapleCharacter.this.MulungTimer = new Timer();
                        MapleCharacter.this.MulungTimerTask = new TimerTask() {
                            @Override
                            public void run() {
                                MapleCharacter.this.warp(925020002);
                                MapleCharacter.this.dropMessage(5, "시간이 초과하여 무릉도장에서 퇴장합니다.");
                                MapleCharacter.this.cancelTimer();
                            }
                        };
                        MapleCharacter.this.MulungTimer.schedule(MapleCharacter.this.MulungTimerTask, 1000L);
                    }
                }
            }
            for (final MapleMonster mons : MapleCharacter.this.getMap().getAllMonster()) {
                if ((mons.getId() >= 8220106 && mons.getId() <= 8220108) || mons.getId() == 8220124) {
                    MapleCharacter.this.getMap().broadcastMessage(MobPacket.monsterForceMove(mons, mons.getController().getPosition()));
                }
            }
            if ((mob = MapleCharacter.this.getMap().getMonsterById(8880000)) != null || (mob = MapleCharacter.this.getMap().getMonsterById(8880002)) != null || (mob = MapleCharacter.this.getMap().getMonsterById(8880010)) != null) {
                int pix;
                if (mob.getHPPercent() <= 25) {
                    pix = 190;
                } else if (mob.getHPPercent() <= 50) {
                    pix = 290;
                } else if (mob.getHPPercent() <= 75) {
                    pix = 330;
                } else {
                    pix = 370;
                }
                boolean damaged = false;
                if (MapleCharacter.this.getTruePosition().getX() <= mob.getTruePosition().getX() - pix || MapleCharacter.this.getTruePosition().getX() >= mob.getTruePosition().getX() + pix) {
                    damaged = true;
                }
                final Map<SecondaryStat, Pair<Integer, Integer>> statups2 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                statups2.put(SecondaryStat.MobZoneState, new Pair<Integer, Integer>((int) (damaged ? 0 : 1), mob.getObjectId()));
                MapleCharacter.this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups2, null, MapleCharacter.this.getPlayer()));
                MapleBossManager.changePhase(mob);
                if (damaged && MapleCharacter.this.getBuffedEffect(SecondaryStat.NotDamaged) == null && MapleCharacter.this.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null && !MapleCharacter.this.isGM()) {
                    final int minushp = (int) (-MapleCharacter.this.getStat().getCurrentMaxHp() / 10L);
                    MapleCharacter.this.addHP(-MapleCharacter.this.getStat().getCurrentMaxHp() / 10L);
                    MapleCharacter.this.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(MapleCharacter.this.client.getPlayer(), 0, minushp, 36, 0, 0, (byte) 0, true, null, null, null));
                    MapleCharacter.this.getMap().broadcastMessage(MapleCharacter.this.client.getPlayer(), CField.EffectPacket.showEffect(MapleCharacter.this.client.getPlayer(), 0, minushp, 36, 0, 0, (byte) 0, false, null, null, null), false);
                }
            }
        }

        public void handleEtcs(final long time) {
            if (time - MapleCharacter.this.lastSaveTime >= 600000L && MapleCharacter.this.choicepotential == null && MapleCharacter.this.returnscroll == null && MapleCharacter.this.memorialcube == null) {
                MapleCharacter.this.saveToDB(false, false);
            }
            if (MapleCharacter.this.getKeyValue(51384, "ww_buck") != -1L && MapleCharacter.this.getKeyValue(51384, "ww_buck") > 0L && MapleCharacter.this.getStat().getHp() > 0L) {
                if (MapleCharacter.this.getSkillLevel(5321054) < 1) {
                    MapleCharacter.this.changeSingleSkillLevel(SkillFactory.getSkill(5321054), 1, (byte) 1);
                }
                if (!MapleCharacter.this.getBuffedValue(5321054)) {
                    SkillFactory.getSkill(5321054).getEffect(MapleCharacter.this.getSkillLevel(5321054)).applyTo(MapleCharacter.this.getPlayer(), false);
                }
            }
            if (MapleCharacter.this.getKeyValue(51384, "ww_crsca") != -1L && MapleCharacter.this.getKeyValue(51384, "ww_crsca") > 0L && MapleCharacter.this.getStat().getHp() > 0L) {
                if (MapleCharacter.this.getSkillLevel(1311015) < 1) {
                    MapleCharacter.this.changeSingleSkillLevel(SkillFactory.getSkill(1311015), 1, (byte) 1);
                }
                if (!MapleCharacter.this.getBuffedValue(1311015)) {
                    SkillFactory.getSkill(1311015).getEffect(MapleCharacter.this.getSkillLevel(1311015)).applyTo(MapleCharacter.this.getPlayer(), false);
                }
            }

            if (MapleCharacter.this.getKeyValue(51384, "ww_magu") != -1L && MapleCharacter.this.getKeyValue(51384, "ww_magu") > 0L && MapleCharacter.this.getStat().getHp() > 0L) {
                if (MapleCharacter.this.getSkillLevel(2001002) < 10) {
                    MapleCharacter.this.changeSingleSkillLevel(SkillFactory.getSkill(2001002), 10, (byte) 10);
                }
                if (!MapleCharacter.this.getBuffedValue(2001002)) {
                    SkillFactory.getSkill(2001002).getEffect(MapleCharacter.this.getSkillLevel(2001002)).applyTo(MapleCharacter.this.getPlayer(), false);
                }
            }
            if (MapleCharacter.this.getKeyValue(51384, "ww_holy") != -1L && MapleCharacter.this.getKeyValue(51384, "ww_holy") > 0L && MapleCharacter.this.getStat().getHp() > 0L) {
                if (MapleCharacter.this.getSkillLevel(2311003) < 20) {
                    MapleCharacter.this.changeSingleSkillLevel(SkillFactory.getSkill(2311003), 20, (byte) 20);
                }
                if (!MapleCharacter.this.getBuffedValue(2311003)) {
                    SkillFactory.getSkill(2311003).getEffect(MapleCharacter.this.getSkillLevel(2311003)).applyTo(MapleCharacter.this.getPlayer());
                }
            }
            if (MapleCharacter.this.getKeyValue(51384, "ww_winb") != -1L && MapleCharacter.this.getKeyValue(51384, "ww_winb") > 0L && MapleCharacter.this.getStat().getHp() > 0L) {
                if (MapleCharacter.this.getSkillLevel(5121009) < 20) {
                    MapleCharacter.this.changeSingleSkillLevel(SkillFactory.getSkill(5121009), 20, (byte) 20);
                }
                if (!MapleCharacter.this.getBuffedValue(5121009)) {
                    SkillFactory.getSkill(5121009).getEffect(MapleCharacter.this.getSkillLevel(5121009)).applyTo(MapleCharacter.this.getPlayer(), false);
                }
            }
            if (MapleCharacter.this.getKeyValue(51384, "ww_sharp") != -1L && MapleCharacter.this.getKeyValue(51384, "ww_sharp") > 0L && MapleCharacter.this.getStat().getHp() > 0L) {
                if (MapleCharacter.this.getSkillLevel(3121002) < 30) {
                    MapleCharacter.this.changeSingleSkillLevel(SkillFactory.getSkill(3121002), 30, (byte) 30);
                }
                if (!MapleCharacter.this.getBuffedValue(3121002)) {
                    SkillFactory.getSkill(3121002).getEffect(MapleCharacter.this.getSkillLevel(3121002)).applyTo(MapleCharacter.this.getPlayer(), false);
                }
            }
            if (MapleCharacter.this.getKeyValue(53714, "atk") != -1L && MapleCharacter.this.getStat().getHp() > 0L) {
                if (MapleCharacter.this.getSkillLevel(80002924) != 1) {
                    MapleCharacter.this.changeSingleSkillLevel(SkillFactory.getSkill(80002924), 1, (byte) 1);
                }
                if (!MapleCharacter.this.getBuffedValue(80002924)) {
                    SkillFactory.getSkill(80002924).getEffect(1).applyTo(MapleCharacter.this.getPlayer(), 0);
                }
            }
            if (MapleCharacter.this.getKeyValue(210416, "TotalDeadTime") > 0L) {
                MapleCharacter.this.setKeyValue(210416, "NowDeadTime", "" + (MapleCharacter.this.getKeyValue(210416, "NowDeadTime") - 1L));
                MapleCharacter.this.client.send(CField.ExpDropPenalty(false, (int) MapleCharacter.this.getKeyValue(210416, "TotalDeadTime"), (int) MapleCharacter.this.getKeyValue(210416, "NowDeadTime"), 80, 80));
                if (MapleCharacter.this.getKeyValue(210416, "NowDeadTime") <= 0L) {
                    MapleCharacter.this.client.send(CField.ExpDropPenalty(false, 0, 0, 0, 0));
                    MapleCharacter.this.removeKeyValue(210416);
                }
            }
            if (MapleCharacter.this.getKeyValue(27040, "runnigtime") > 0L) {
                final KoreaCalendar kc = new KoreaCalendar();
                final String date = kc.getYeal() % 100 + "/" + kc.getMonths() + "/" + kc.getDays() + "";
                final long runnigtime = MapleCharacter.this.getKeyValue(27040, "runnigtime");
                int level = (int) runnigtime / 3600;
                if (level >= 2) {
                    level = 2;
                }
                final Item item = MapleCharacter.this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-17));
                final Item item2 = MapleCharacter.this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-31));
                boolean itemexppendent = false;
                Item exppen = null;
                if (item != null) {
                    itemexppendent = (item.getItemId() == 1122017 || item.getItemId() == 1122155 || item.getItemId() == 1122215);
                    if (itemexppendent) {
                        exppen = item;
                    }
                }
                if (item2 != null && !itemexppendent) {
                    itemexppendent = (item2.getItemId() == 1122017 || item2.getItemId() == 1122155 || item2.getItemId() == 1122215);
                    if (itemexppendent) {
                        exppen = item2;
                    }
                }
                if (kc.getDayt() == MapleCharacter.this.getKeyValue(27040, "equipday") && kc.getMonth() == MapleCharacter.this.getKeyValue(27040, "equipmonth") && exppen != null) {
                    final int expplus = (level == 2) ? 30 : ((level == 1) ? 20 : 10);
                    final int todaytime = (int) runnigtime / 60;
                    MapleCharacter.this.setKeyValue(27040, "runnigtime", "" + (runnigtime + 1L) + "");
                    if (MapleCharacter.this.getKeyValue(27040, "runnigtime") % 60L == 0L) {
                        MapleCharacter.this.getClient().send(CWvsContext.SpritPandent(exppen.getPosition(), false, level, expplus, todaytime));
                    }
                } else {
                    MapleCharacter.this.removeKeyValue(27040);
                    if (itemexppendent) {
                        final String nowtime = kc.getYeal() % 100 + kc.getMonths() + kc.getDays() + kc.getHours() + kc.getMins() + kc.getMins();
                        MapleCharacter.this.setKeyValue(27040, "runnigtime", "1");
                        MapleCharacter.this.setKeyValue(27040, "firstequiptime", nowtime);
                        MapleCharacter.this.setKeyValue(27040, "firstequiptimemil", System.currentTimeMillis() + "");
                        MapleCharacter.this.setKeyValue(27040, "equipday", "" + kc.getDayt() + "");
                        MapleCharacter.this.setKeyValue(27040, "equipmonth", "" + kc.getMonths() + "");
                        MapleCharacter.this.updateInfoQuest(27039, exppen.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|0|0");
                        MapleCharacter.this.getClient().send(CWvsContext.SpritPandent(exppen.getPosition(), true, 0, 10, 0));
                        MapleCharacter.this.updateInfoQuest(27039, exppen.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|10|0");
                    }
                }
            }
            if ((MapleCharacter.this.getMap().isSpawnPoint() || MapleCharacter.this.getMap().isTown()) && !GameConstants.isContentsMap(MapleCharacter.this.getMap().getId()) && !GameConstants.보스맵(MapleCharacter.this.getMap().getId()) && !GameConstants.사냥컨텐츠맵(MapleCharacter.this.getMap().getId()) && !GameConstants.튜토리얼(MapleCharacter.this.getMap().getId()) && !GameConstants.로미오줄리엣(MapleCharacter.this.getMap().getId()) && !GameConstants.피라미드(MapleCharacter.this.getMap().getId())) {
                if (MapleCharacter.this.getV("lastSudden") == null) {
                    MapleCharacter.this.addKV("lastSudden", System.currentTimeMillis() + "");
                }
                if (Randomizer.isSuccess(1, 1000) && Long.parseLong(MapleCharacter.this.getV("lastSudden")) < System.currentTimeMillis()) {
                    int questid = 0;
                    do {
                        questid = Randomizer.rand(49001, 49018);
                    } while (questid == 49015);
                    MapleCharacter.this.gainSuddenMission(questid, 49000, true);
                    MapleCharacter.this.addKV("lastSudden", System.currentTimeMillis() + 1000000000L + "");
                }
            }
            if (MapleCharacter.this.getKeyValue(51351, "startquestid") == 49011L && MapleCharacter.this.getMap().getBurning() > 0 && MapleCharacter.this.getKeyValue(51351, "queststat") != 3L && !MapleCharacter.this.getMap().isTown() && !GameConstants.로미오줄리엣(MapleCharacter.this.getMapId()) && !GameConstants.사냥컨텐츠맵(MapleCharacter.this.getMapId()) && MapleCharacter.this.getMap().isSpawnPoint() && !GameConstants.isContentsMap(MapleCharacter.this.getMapId())) {
                MapleCharacter.this.setKeyValue(51351, "queststat", "3");
                MapleCharacter.this.getClient().send(CWvsContext.updateSuddenQuest((int) MapleCharacter.this.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + MapleCharacter.this.getKeyValue(51351, "startquestid") + ";state=3;"));
                MapleCharacter.this.getClient().send(CWvsContext.updateSuddenQuest((int) MapleCharacter.this.getKeyValue(51351, "startquestid"), false, MapleCharacter.this.getKeyValue(51351, "endtime"), "BTField=1;"));
            }
            if (MapleCharacter.this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-26)) != null) {
                final int itemid = MapleCharacter.this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-26)).getItemId();
                if (itemid == 1162000 || itemid == 1162001 || itemid == 1162002 || itemid == 1162004) {
                    final int skillid = (itemid == 1162000) ? 80001544 : ((itemid == 1162001) ? 80001545 : 80001546);
                    if (SkillFactory.getSkill(skillid) != null && !MapleCharacter.this.getBuffedValue(skillid)) {
                        if (MapleCharacter.this.getSkillLevel(skillid) <= 0) {
                            MapleCharacter.this.changeSkillLevel(skillid, (byte) 1, (byte) 1);
                        }
                        SkillFactory.getSkill(skillid).getEffect(1).applyTo(MapleCharacter.this);
                    }
                } else {
                    final int[] array;
                    final int[] skillids = array = new int[]{80001544, 80001545, 80001546};
                    for (final int skill : array) {
                        if (MapleCharacter.this.getBuffedValue(skill)) {
                            MapleCharacter.this.cancelEffect(MapleCharacter.this.getBuffedEffect(skill));
                        }
                        if (MapleCharacter.this.getSkillLevel(skill) > 0) {
                            MapleCharacter.this.changeSkillLevel(skill, (byte) 0, (byte) 0);
                        }
                    }
                }
            } else {
                final int[] array2;
                final int[] skillids2 = array2 = new int[]{80001544, 80001545, 80001546};
                for (final int skill2 : array2) {
                    if (MapleCharacter.this.getBuffedValue(skill2)) {
                        MapleCharacter.this.cancelEffect(MapleCharacter.this.getBuffedEffect(skill2));
                    }
                    if (MapleCharacter.this.getSkillLevel(skill2) > 0) {
                        MapleCharacter.this.changeSkillLevel(skill2, (byte) 0, (byte) 0);
                    }
                }
            }
            if (MapleCharacter.this.getSkillLevel(80001535) > 0) {
                if (!MapleCharacter.this.getBuffedValue(80001535)) {
                    SkillFactory.getSkill(80001535).getEffect(1).applyTo(MapleCharacter.this);
                }
            } else if (MapleCharacter.this.getBuffedValue(80001535)) {
                MapleCharacter.this.cancelEffect(MapleCharacter.this.getBuffedEffect(80001535));
            }
            final int[] array3;
            final int[] skillids2 = array3 = new int[]{80001537, 80001538, 80001539};
            for (final int skill2 : array3) {
                if (MapleCharacter.this.getSkillLevel(skill2) > 0) {
                    if (!MapleCharacter.this.getBuffedValue(skill2)) {
                        SkillFactory.getSkill(skill2).getEffect(1).applyTo(MapleCharacter.this);
                    }
                } else if (MapleCharacter.this.getBuffedValue(skill2)) {
                    MapleCharacter.this.cancelEffect(MapleCharacter.this.getBuffedEffect(skill2));
                }
            }
            if (MapleCharacter.this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-29)) != null) {
                final int itemid2 = MapleCharacter.this.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-29)).getItemId();
                if (itemid2 >= 1182001 && itemid2 <= 1182005) {
                    if (MapleCharacter.this.getV("DawnItem") == null) {
                        MapleCharacter.this.addKV("DawnItem", itemid2 + "");
                    }
                    if (!MapleCharacter.this.getBuffedValue(80001809) || Integer.parseInt(MapleCharacter.this.getV("DawnItem")) != itemid2) {
                        if (Integer.parseInt(MapleCharacter.this.getV("DawnItem")) != itemid2) {
                            MapleCharacter.this.addKV("DawnItem", itemid2 + "");
                        }
                        if (MapleCharacter.this.getBuffedValue(80001809)) {
                            MapleCharacter.this.cancelEffect(MapleCharacter.this.getBuffedEffect(80001809));
                        }
                        SkillFactory.getSkill(80001809).getEffect(1).applyTo(MapleCharacter.this);
                    }
                }
            } else if (MapleCharacter.this.getBuffedValue(80001809)) {
                MapleCharacter.this.cancelEffect(MapleCharacter.this.getBuffedEffect(80001809));
            }
            if (MapleCharacter.this.getClient().getKeyValue("UnionCoin") == null) {
                MapleCharacter.this.getClient().setKeyValue("UnionCoin", "0");
            }
            if (MapleCharacter.this.getClient().getKeyValue("UnionCoinNujuk") == null) {
                MapleCharacter.this.getClient().setKeyValue("UnionCoinNujuk", "0");
            }
            if (MapleCharacter.this.getClient().getKeyValue("UnionAllNujuk") == null) {
                MapleCharacter.this.getClient().setKeyValue("UnionAllNujuk", "0");
            }
            if (MapleCharacter.this.getClient().getKeyValue("UnionNujuk") == null) {
                MapleCharacter.this.getClient().setKeyValue("UnionNujuk", "0");
            }
            if (MapleCharacter.this.getClient().getKeyValue("UnionEndTime") == null) {
                MapleCharacter.this.getClient().setKeyValue("UnionEndTime", "0");
            }
            if (MapleCharacter.this.getClient().getKeyValue("UnionEnterTime") == null) {
                MapleCharacter.this.getClient().setKeyValue("UnionEnterTime", "0");
            }
            if (GameConstants.isUnionRaid(MapleCharacter.this.getMapId())) {
                final long allnujuk = MapleCharacter.this.getUnionAllNujuk();
                final long nujuk = MapleCharacter.this.getUnionNujuk();
                final long attackrate = MapleCharacter.this.getUnionDamage();
                final int coin = MapleCharacter.this.getUnionCoin();
                final int coing = (int) MapleCharacter.this.getUnionEnterTime();
                MapleCharacter.this.setUnionAllNujuk(allnujuk + attackrate);
                if (nujuk < 2500000000000L) {
                    MapleCharacter.this.setUnionNujuk(nujuk + attackrate);
                }
                if ((allnujuk + nujuk) / 100000000000L - coing != 0L) {
                    final int a = (int) ((allnujuk + nujuk) / 100000000000L - coing);
                    MapleCharacter.this.setUnionCoin(coin + a);
                    MapleCharacter.this.setUnionEnterTime(coing + a);
                    MapleCharacter.this.getClient().send(CField.setUnionRaidCoinNum(0, false));
                    MapleCharacter.this.getClient().send(CField.setUnionRaidCoinNum(coin + 1, true));
                }
                MapleCharacter.this.getClient().send(CField.setUnionRaidScore(allnujuk + nujuk));
                long hp = 0L;
                long maxhp = 0L;
                long hp2 = 0L;
                long maxhp2 = 0L;
                int mobid = (Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 1) ? 9833101 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 2) ? 9833102 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 3) ? 9833103 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 4) ? 9833104 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 5) ? 9833105 : 0))));
                final int mobid2 = mobid + 100;
                int mobsize = 0;
                int flyingmob = 0;
                for (final MapleMonster monster : MapleCharacter.this.getMap().getAllMonster()) {
                    if (monster.getOwner() == MapleCharacter.this.getId()) {
                        if (monster.getId() >= 9833106 && monster.getId() <= 9833111) {
                            if (monster.getId() == 9833110 || monster.getId() == 9833111) {
                                ++flyingmob;
                            } else {
                                ++mobsize;
                            }
                        }
                        if (monster.getId() == mobid) {
                            hp = monster.getMobMaxHp() - (allnujuk + nujuk);
                            maxhp = monster.getMobMaxHp();
                            if (hp > 0L) {
                                continue;
                            }
                            MapleCharacter.this.getMap().killMonster(monster, -1);
                            MapleCharacter.this.setUnionAllNujuk(0L);
                            MapleCharacter.this.setUnionNujuk(0L);
                            MapleCharacter.this.setUnionEnterTime(0L);
                            if (Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) < 5) {
                                MapleCharacter.this.getClient().setKeyValue("UnionLaidLevel", "" + (Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) + 1) + "");
                            } else {
                                MapleCharacter.this.getClient().setKeyValue("UnionLaidLevel", "1");
                            }
                            mobid = ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 1) ? 9833101 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 2) ? 9833102 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 3) ? 9833103 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 4) ? 9833104 : ((Integer.parseInt(MapleCharacter.this.getClient().getKeyValue("UnionLaidLevel")) == 5) ? 9833105 : 0)))));
                            MapleMonster m = MapleLifeFactory.getMonster(mobid);
                            m.setHp(10000000000000L);
                            m.getStats().setHp(10000000000000L);
                            m.setOwner(MapleCharacter.this.getId());
                            MapleCharacter.this.getMap().spawnMonsterOnGroundBelow(m, new Point(2320, 17));
                            m = MapleLifeFactory.getMonster(mobid + 100);
                            m.setHp(2500000000000L);
                            m.getStats().setHp(2500000000000L);
                            m.setOwner(MapleCharacter.this.getId());
                            MapleCharacter.this.getMap().spawnMonsterOnGroundBelow(m, new Point(2320, 17));
                        } else {
                            if (monster.getId() != mobid2) {
                                continue;
                            }
                            hp2 = monster.getMobMaxHp() - nujuk;
                            maxhp2 = monster.getMobMaxHp();
                            if (hp2 > 0L) {
                                continue;
                            }
                            hp2 = 1L;
                            if (monster.getBuff(MonsterStatus.MS_PowerImmune) != null) {
                                continue;
                            }
                            monster.damage(MapleCharacter.this.getClient().getPlayer(), maxhp2, false);
                        }
                    }
                }
                if (MapleCharacter.this.getSkillCustomValue(1598857) == null) {
                    MapleCharacter.this.setSkillCustomInfo(1598857, 0L, 4400L);
                    MapleCharacter.this.getClient().send(CField.setUnionRaidCoinNum(coin, true));
                }
                Point pos44 = null;
                if (mobsize < 8) {
                    for (int a2 = mobsize; a2 < 8; ++a2) {
                        final int type = Randomizer.rand(0, 10);
                        if (type == 0) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -596);
                        } else if (type == 1) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -440);
                        } else if (type == 2) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -284);
                        } else if (type == 3) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -128);
                        } else if (type == 4) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -596);
                        } else if (type == 5) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -440);
                        } else if (type == 6) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -284);
                        } else if (type == 7) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -128);
                        } else {
                            pos44 = new Point(Randomizer.rand(1597, 3199), 17);
                        }
                        final MapleMonster i = MapleLifeFactory.getMonster(Randomizer.rand(9833106, 9833109));
                        i.getStats().setLevel((short) 200);
                        i.setHp(10000000L);
                        i.getStats().setHp(10000000L);
                        i.setOwner(MapleCharacter.this.getId());
                        if (MapleCharacter.this.getMapId() == 921172000 || MapleCharacter.this.getMapId() == 921172100) {
                            MapleCharacter.this.getMap().spawnMonsterOnGroundBelow(i, pos44);
                        }
                    }
                }
                if (flyingmob < 3) {
                    for (int a2 = flyingmob; a2 < 4; ++a2) {
                        final int type = Randomizer.rand(0, 10);
                        if (type == 0) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -596);
                        } else if (type == 1) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -440);
                        } else if (type == 2) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -284);
                        } else if (type == 3) {
                            pos44 = new Point(Randomizer.rand(1452, 1921), -128);
                        } else if (type == 4) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -596);
                        } else if (type == 5) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -440);
                        } else if (type == 6) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -284);
                        } else if (type == 7) {
                            pos44 = new Point(Randomizer.rand(2737, 3208), -128);
                        } else {
                            pos44 = new Point(Randomizer.rand(1597, 3199), 17);
                        }
                        final MapleMonster i = MapleLifeFactory.getMonster(Randomizer.isSuccess(40) ? 9833111 : 9833110);
                        i.getStats().setLevel((short) 200);
                        i.setHp(20000000L);
                        i.getStats().setHp(20000000L);
                        i.setOwner(MapleCharacter.this.getId());
                        if (MapleCharacter.this.getMapId() == 921172000 || MapleCharacter.this.getMapId() == 921172100) {
                            MapleCharacter.this.getMap().spawnMonsterOnGroundBelow(i, pos44);
                        }
                    }
                }
                MapleCharacter.this.getClient().send(CField.showUnionRaidHpUI(mobid, hp2, maxhp2, mobid2, hp, maxhp));
            }
        }

        public void handleInventorys(final long time) {
            for (final MapleInventory inv : MapleCharacter.this.getInventorys()) {
                for (final Item item : inv.list()) {
                    if (item.getExpiration() != -1L && item.getExpiration() <= time && !GameConstants.isPet(item.getItemId())) {
                        if (item.getPosition() < 0) {
                            MapleInventoryManipulator.unequip(MapleCharacter.this.getClient(), item.getPosition(), MapleCharacter.this.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot(), MapleInventoryType.EQUIP);
                        }
                        MapleInventoryManipulator.removeFromSlot(MapleCharacter.this.getClient(), GameConstants.getInventoryType(item.getItemId()), item.getPosition(), item.getQuantity(), false);
                        MapleCharacter.this.getPlayer().dropMessage(5, "아이템 [" + MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "]의 사용기간이 지나서 아이템이 제거되었습니다.");
                    }
                }
            }
        }

        public void handleCooldowns(final long now) {
            final List<MapleCoolDownValueHolder> cooldowns = new ArrayList<MapleCoolDownValueHolder>();
            if (MapleCharacter.this.getCooldownSize() > 0) {
                for (final MapleCoolDownValueHolder m : MapleCharacter.this.getCooldowns()) {
                    if (m.startTime + m.length < now) {
                        cooldowns.add(m);
                    }
                }
            }
            if (!cooldowns.isEmpty()) {
                MapleCharacter.this.clearCooldowns(cooldowns);
            }
            if (MapleCharacter.this.getCooldownLimit(31121054) > 0L && now - MapleCharacter.this.cooldownforceBlood >= 3000L) {
                MapleCharacter.this.changeCooldown(31121054, -2000);
            }
        }

        public void handleSkillOptions(final long time) {
            if (MapleCharacter.this.getSkillLevel(27110007) > 0 && MapleCharacter.this.getSkillCustomValue(27110007) == null) {
                final MapleCharacter chr = MapleCharacter.this.client.getPlayer();
                final SecondaryStatEffect lifetidal = SkillFactory.getSkill(27110007).getEffect(MapleCharacter.this.getSkillLevel(27110007));
                final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                statups.put(SecondaryStat.LifeTidal, new Pair<Integer, Integer>(1, 0));
                if (MapleCharacter.this.getStat().getHPPercent() > MapleCharacter.this.getStat().getMPPercent()) {
                    MapleCharacter.this.addMP((int) (MapleCharacter.this.getStat().getCurrentMaxMp(chr) / 100L * lifetidal.getT()));
                } else {
                    MapleCharacter.this.addHP((int) (MapleCharacter.this.getStat().getCurrentMaxHp() / 100L * lifetidal.getT()));
                }
                if (!statups.isEmpty()) {
                    MapleCharacter.this.client.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, lifetidal, MapleCharacter.this.client.getPlayer()));
                }
                MapleCharacter.this.setSkillCustomInfo(27110007, 0L, 5000L);
            }
            if (MapleCharacter.this.getBuffedValue(32121018) || MapleCharacter.this.getBuffedValue(400021006)) {
                boolean read = true;
                if (MapleCharacter.this.getBuffedValue(400021006) && MapleCharacter.this.getBuffedOwner(400021006) != MapleCharacter.this.id) {
                    read = false;
                }
                if (read) {
                    final SecondaryStatEffect effect = SkillFactory.getSkill(32121018).getEffect(MapleCharacter.this.getSkillLevel(32121018));
                    if (!MapleCharacter.this.getBuffedValue(400021006)) {
                        MapleCharacter.this.addMP(-effect.getMPCon());
                    }
                    if (time - (MapleCharacter.this.getBuffedValue(400021006) ? MapleCharacter.this.checkBuffStatValueHolder(SecondaryStat.UnionAura, 400021006).startTime : MapleCharacter.this.checkBuffStatValueHolder(SecondaryStat.DebuffAura, 32121018).startTime) >= 2000L) {
                        for (final MapleMonster monster : MapleCharacter.this.getMap().getAllMonster()) {
                            if (MapleCharacter.this.getTruePosition().x + effect.getLt().x < monster.getTruePosition().x && MapleCharacter.this.getTruePosition().x - effect.getLt().x > monster.getTruePosition().x && MapleCharacter.this.getTruePosition().y + effect.getLt().y < monster.getTruePosition().y && MapleCharacter.this.getTruePosition().y - effect.getLt().y > monster.getTruePosition().y && monster.getBuff(MonsterStatus.MS_TrueSight) == null) {
                                final List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                                applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_BMageDebuff, new MonsterStatusEffect(effect.getSourceId(), effect.getDuration(), effect.getX())));
                                if (MapleCharacter.this.getSkillLevel(32120061) > 0) {
                                    final SecondaryStatEffect effect2 = SkillFactory.getSkill(32120061).getEffect(1);
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_TrueSight, new MonsterStatusEffect(effect2.getSourceId(), effect.getDuration(), -effect2.getS())));
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieUNK, new MonsterStatusEffect(effect2.getSourceId(), effect.getDuration(), -effect2.getX())));
                                }
                                monster.applyStatus(MapleCharacter.this.getClient(), applys, effect);
                            }
                        }
                    }
                }
            }
            if (MapleCharacter.this.getBuffedEffect(SecondaryStat.IceAura) != null && MapleCharacter.this.getJob() == 222) {
                MapleCharacter.this.addMP(-60L);
                final SecondaryStatEffect eff = SkillFactory.getSkill(2221054).getEffect(1);
                for (final MapleMonster mob : MapleCharacter.this.getMap().getAllMonster()) {
                    if (MapleCharacter.this.getTruePosition().x + eff.getLt().x < mob.getTruePosition().x && MapleCharacter.this.getTruePosition().x - eff.getLt().x > mob.getTruePosition().x && MapleCharacter.this.getTruePosition().y + eff.getLt().y < MapleCharacter.this.getTruePosition().y && MapleCharacter.this.getTruePosition().y - eff.getLt().y > mob.getTruePosition().y && mob.isAlive()) {
                        if (mob.getBuff(MonsterStatus.MS_Speed) == null && mob.getFreezingOverlap() > 0) {
                            mob.setFreezingOverlap(0);
                            if (mob.getFreezingOverlap() <= 0) {
                                mob.cancelStatus(MonsterStatus.MS_Speed, mob.getBuff(2221054));
                            }
                        }
                        if (mob.getFreezingOverlap() < 5) {
                            mob.setFreezingOverlap((byte) (mob.getFreezingOverlap() + 1));
                        }
                        final MonsterStatusEffect effect3 = new MonsterStatusEffect(2221054, 8000);
                        mob.applyStatus(MapleCharacter.this.getClient(), MonsterStatus.MS_Speed, effect3, eff.getV(), eff);
                    }
                }
            }
            if (MapleCharacter.this.getBuffedValue(SecondaryStat.Infinity) != null && time - MapleCharacter.this.lastInfinityTime >= 4000L && MapleCharacter.this.getInfinity() < 25) {
                MapleCharacter.this.setInfinity((byte) (MapleCharacter.this.getInfinity() + 1));
                MapleCharacter.this.setBuffedValue(SecondaryStat.Infinity, MapleCharacter.this.getInfinity());
                final SecondaryStatEffect effect4 = SkillFactory.getSkill(MapleCharacter.this.getBuffSource(SecondaryStat.Infinity)).getEffect(MapleCharacter.this.getSkillLevel(MapleCharacter.this.getBuffSource(SecondaryStat.Infinity)));
                MapleCharacter.this.addHP((long) (MapleCharacter.this.getStat().getMaxHp() * 0.1));
                MapleCharacter.this.addMP((long) (MapleCharacter.this.getStat().getMaxMp() * 0.1));
                MapleCharacter.this.lastInfinityTime = System.currentTimeMillis();
                final Map<SecondaryStat, Pair<Integer, Integer>> statups2 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                statups2.put(SecondaryStat.Infinity, new Pair<Integer, Integer>((int) MapleCharacter.this.getInfinity(), (int) MapleCharacter.this.getBuffLimit(MapleCharacter.this.getBuffSource(SecondaryStat.Infinity))));
                MapleCharacter.this.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups2, effect4, MapleCharacter.this.client.getPlayer()));
            }
        }

        @Override
        public void run() {
            final long time = System.currentTimeMillis();
//            dropMessageGM(6,"MapleCharacter.java : time = System.currentTimeMillis() : "+time);   // 1,639,401,553,304
            MapleCharacter.this.handleSecondaryStats(time, false);
            this.handleCooldowns(time);
            if (MapleCharacter.this.isAlive()) {
                if (MapleCharacter.this.canFairy(time)) {
                    MapleCharacter.this.doFairy();
                }
                if (MapleCharacter.this.canDOT(time) && MapleCharacter.this.hasDOT()) {
                    MapleCharacter.this.doDOT();
                }
                if (MapleCharacter.this.canRecover(time)) {
                    MapleCharacter.this.doRecovery();
                }
                this.handleSkillOptions(time);
                MapleCharacter.this.handleAdditionalSkills(time);
                MapleCharacter.this.handleHealSkills(time);
                MapleCharacter.this.handleSummons(time);
                this.handleMobs(time);
                this.handleInventorys(time);
                this.handleEtcs(time);
            }
        }
    }

    public enum FameStatus {
        OK,
        NOT_TODAY,
        NOT_THIS_MONTH;
    }

    public void SoulLevelLoad(int accId, int chrId) throws SQLException {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE `level` > 0 AND `accountid` = " + accId + " AND `id` != " + chrId + " ORDER BY `level` DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            int wskill = 0;
            int wlevel = 0;
            while (rs.next()) {
                wlevel = rs.getInt("level");
            }
            if (wlevel < 10) {
                wskill = 0;
            } else if (wlevel > 200) {
                wskill = 20;
            } else {
                wskill = wlevel / 10;
            }
            int skillid = 0;
            if (GameConstants.blessSkillJob(getJob()) != -1) {
                skillid = GameConstants.blessSkillJob(getJob()) + 12;
                changeSingleSkillLevel(SkillFactory.getSkill(skillid), wskill, (byte) wskill);
            }
            rs.close();
            ps.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public void EmpressLevelLoad(int accId, int chrId) throws SQLException {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE `level` > 0 AND `accountid` = " + accId + " AND `id` != " + chrId + " AND `job` >= 1000 AND `job` <= 1512 ORDER BY `level` DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            int wskill = 0;
            int wlevel = 0;
            while (rs.next()) {
                wlevel = rs.getInt("level");
            }
            if (wlevel < 5) {
                wskill = 0;
            } else if (wlevel > 150) {
                wskill = 30;
            } else {
                wskill = wlevel / 5;
            }
            PreparedStatement pss = con.prepareStatement("SELECT * FROM characters WHERE `level` > 0 AND `accountid` = " + accId + " AND `id` != " + chrId + " AND `job` >= 5000 AND `job` <= 5112 ORDER BY `level` DESC LIMIT 1");
            ResultSet rss = pss.executeQuery();
            int wskills = 0;
            int wlevels = 0;
            while (rss.next()) {
                wlevels = rss.getInt("level");
            }
            if (wlevels < 5) {
                wskills = 0;
            } else if (wlevels > 150) {
                wskills = 30;
            } else {
                wskills = wlevels / 5;
            }
            if (wskill < wskills) {
                wskill = wskills;
            }
            int skillid = 0;
            if (GameConstants.blessSkillJob(getJob()) != -1) {
                skillid = GameConstants.blessSkillJob(getJob()) + 73;
                changeSingleSkillLevel(SkillFactory.getSkill(skillid), wskill, (byte) wskill);
            }
            rs.close();
            ps.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public boolean hasDonationSkill(int skillid) {
        if (this.getKeyValue(201910, "DonationSkill") < 0) {
            this.setKeyValue(201910, "DonationSkill", "0");
        }

        MapleDonationSkill dskill = MapleDonationSkill.getBySkillId(skillid);
        if (dskill == null) {
            return false;
        } else if ((this.getKeyValue(201910, "DonationSkill") & dskill.getValue()) == 0) {
            return false;
        }
        return true;
    }

    public void gainDonationSkills() {
        if (getKeyValue(201910, "DonationSkill") > 0) {
            for (final MapleDonationSkill stat : MapleDonationSkill.values()) {

                //  if (stat.getSkillId() != 5321054) {
                if ((getKeyValue(201910, "DonationSkill") & stat.getValue()) != 0) {
                    getStat().setMp(getStat().getCurrentMaxMp(this), this);
                    if (!getBuffedValue(stat.getSkillId())) {
                        SkillFactory.getSkill(stat.getSkillId()).getEffect(SkillFactory.getSkill(stat.getSkillId()).getMaxLevel()).applyTo(this, 0);
                    }
                    //      }
                }
            }
        }
    }

    public void gainDonationSkill(int skillid) {
        if (this.getKeyValue(201910, "DonationSkill") < 0) {
            this.setKeyValue(201910, "DonationSkill", "0");
        }

        MapleDonationSkill dskill = MapleDonationSkill.getBySkillId(skillid);
        if (dskill != null && (this.getKeyValue(201910, "DonationSkill") & dskill.getValue()) == 0) {
            int data = (int) this.getKeyValue(201910, "DonationSkill");
            data |= dskill.getValue();
            this.setKeyValue(201910, "DonationSkill", data + "");
            SkillFactory.getSkill(skillid).getEffect(SkillFactory.getSkill(skillid).getMaxLevel()).applyTo(this, 0);
        }
    }

    public void teachSkill(final int id, final int level, final byte masterlevel) {

        this.changeSingleSkillLevel(SkillFactory.getSkill(id), level, masterlevel);
    }

    public void teachSkill(final int id, int level) {
        final Skill skil = SkillFactory.getSkill(id);
        if (this.getSkillLevel(skil) > level) {
            level = this.getSkillLevel(skil);
        }
        this.changeSingleSkillLevel(skil, level, (byte) skil.getMaxLevel());
    }

    public void maxskill(int i) {

        if (GameConstants.isHoyeong(i)) {
            if (getSkillLevel(160000076) < 10) {
                changeSkillLevel(SkillFactory.getSkill(160000076), (byte) 10, (byte) 10);
            }
        }
        MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(StringUtil.getLeftPaddedStr("" + i, '0', 3) + ".img");
        byte maxLevel = 0;
        for (MapleData skill : data) {
            if (skill != null) {
                for (MapleData skillId : skill.getChildren()) {
                    if (!skillId.getName().equals("icon")) {
                        maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                        if (maxLevel > 30) {
                            maxLevel = 30;
                        }
                        if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0) { //스킬창에 안보이는 스킬은 올리지않음
                            if (getLevel() >= MapleDataTool.getIntConvert("reqLev", skillId, 0)) {
                                try {
                                    changeSkillLevel(SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel, maxLevel);
                                } catch (NumberFormatException ex) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void skillMaster() {
        MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(StringUtil.getLeftPaddedStr("" + getJob(), '0', 3) + ".img");
        dropMessage(5, "스킬마스터가 완료되었습니다.");
        if (getLevel() < 10) {
            dropMessage(1, "레벨 10 이상 부터 사용 할 수 있습니다.");
            return;
        }
        for (int i = 0; i < (getJob() % 10) + 1; i++) {
            maxskill(((i + 1) == ((getJob() % 10) + 1)) ? getJob() - (getJob() % 100) : getJob() - (i + 1));
        }
        maxskill(getJob());
        if (GameConstants.isDemonAvenger(getJob())) {
            maxskill(3101);
        }

        if (GameConstants.isZero(getJob())) {
            int jobs[] = {10000, 10100, 10110, 10111, 10112};
            for (int job : jobs) {
                data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(job + ".img");
                for (MapleData skill : data) {
                    if (skill != null) {
                        for (MapleData skillId : skill.getChildren()) {
                            if (!skillId.getName().equals("icon")) {
                                byte maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                                if (maxLevel < 0) { // 배틀메이지 데스는 왜 만렙이 250이지?
                                    maxLevel = 1;
                                }
                                if (maxLevel > 30) { // 배틀메이지 데스는 왜 만렙이 250이지?
                                    maxLevel = 30;
                                }
                                if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0) { //스킬창에 안보이는 스킬은 올리지않음
                                    if (getLevel() >= MapleDataTool.getIntConvert("reqLev", skillId, 0)) {
                                        changeSingleSkillLevel(SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel, maxLevel);
                                    }
                                }
                            }
                        }
                    }
                }
                if (getLevel() >= 200) {
                    changeSingleSkillLevel(SkillFactory.getSkill(100001005), (byte) 1, (byte) 1);
                }
            }
        }
        if (GameConstants.isKOC(getJob()) && getLevel() >= 100) {
            changeSkillLevel(11121000, (byte) 30, (byte) 30);
            changeSkillLevel(12121000, (byte) 30, (byte) 30);
            changeSkillLevel(13121000, (byte) 30, (byte) 30);
            changeSkillLevel(14121000, (byte) 30, (byte) 30);
            changeSkillLevel(15121000, (byte) 30, (byte) 30);
        }
    }

    public void changeSkillLevel_Inner(final int skill, byte newLevel, byte newMasterLevel) {
        changeSkillLevel_Inner(SkillFactory.getSkill(skill), newLevel, newMasterLevel);
    }

    public void changeSkillLevel_Inner(Skill skil, int skilLevel, byte masterLevel) {
        final Map<Skill, SkillEntry> enry = new HashMap<>(1);
        enry.put(skil, new SkillEntry(skilLevel, masterLevel, -1L));
        changeSkillLevel_Skip(enry, false);
    }

    public List<MapleHyperStats> loadHyperStats(int pos) {
        List<MapleHyperStats> mhp = new LinkedList<MapleHyperStats>();
        try {

            Connection con = DatabaseConnection.getConnection(); // Get a connection to the database
            PreparedStatement ps = con.prepareStatement("SELECT * FROM hyperstats WHERE charid = ? AND position = ?"); // Get details..
            ps.setInt(1, getId());
            ps.setInt(2, pos);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mhp.add(new MapleHyperStats(pos, rs.getInt("skillid"), rs.getInt("skilllevel")));
            }
            rs.close();
            ps.close();
            con.close();
            return mhp;
        } catch (SQLException ex) {
            ex.printStackTrace();

            return null;
        }
    }

    public MapleHyperStats addHyperStats(int position, int skillid, int skilllevel) {
        try { // Commit to db first
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pse;
            pse = con.prepareStatement("INSERT INTO hyperstats (charid, position, skillid, skilllevel) VALUES (?, ?, ?, ?)");
            pse.setInt(1, getId());
            pse.setInt(2, position);
            pse.setInt(3, skillid);
            pse.setInt(4, skilllevel);
            pse.executeUpdate();
            pse.close();
            con.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        final MapleHyperStats mhs = new MapleHyperStats(position, skillid, skilllevel);
        mhs.setPosition(position);
        mhs.setSkillid(skillid);
        mhs.setSkillLevel(skilllevel);
        return mhs;
    }

    public MapleHyperStats UpdateHyperStats(int position, int skillid, int skilllevel) {
        try { // Commit to db first
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ps = con.prepareStatement("UPDATE hyperstats SET skilllevel = ? WHERE charid = ? AND position = ? AND skillid = ?");
            ps.setInt(1, skilllevel);
            ps.setInt(2, getId());
            ps.setInt(3, position);
            ps.setInt(4, skillid);
            ps.executeUpdate(); // Execute statement
            ps.close();
            con.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        final MapleHyperStats mhs = new MapleHyperStats(position, skillid, skilllevel);
        mhs.setPosition(position);
        mhs.setSkillid(skillid);
        mhs.setSkillLevel(skilllevel);
        return mhs;
    }

    
       public void sethottimeboss(boolean check) {
        this.hottimeboss = check;
    }

    public boolean gethottimeboss() {
        return hottimeboss;
    }

    public void sethottimebosslastattack(boolean check) {
        if (check) {
            World.Broadcast.broadcastMessage(CWvsContext.yellowChat(this.getName() + "님께서 DIA 월드보스 막타보상을 획득하셨습니다. 축하합니다!!"));
        }
        this.hottimebosslastattack = check;
    }

    public boolean gethottimebosslastattack() {
        return hottimebosslastattack;
    }

    public void sethottimebossattackcheck(boolean check) {
        this.hottimebossattackcheck = check;
    }

    public boolean gethottimebossattackcheck() {
        return hottimebossattackcheck;
    }

    
        public void startMapTimeLimitTask(int time, final MapleMap to) {
        if (time <= 0) {
            time = 1;
        }
        client.getSession().write(CField.getClock(time));
        final MapleMap ourMap = getMap();
        time *= 1000;
        mapTimeLimitTask = server.Timer.MapTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (ourMap.getId() == GameConstants.JAIL) {
                    getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_TIME)).setCustomData(String.valueOf(System.currentTimeMillis()));
                    getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST)).setCustomData("0");
                }
                changeMap(to, to.getPortal(0));
            }
        }, time, time);
    }

     public void 혁신의룰렛() {
        server.Timer.EtcTimer tm = server.Timer.EtcTimer.getInstance();
        final int f = (Randomizer.nextInt(4));
        final int s = (Randomizer.rand(1, 2));
        final int t = (Randomizer.nextInt(5));
        client.getSession().writeAndFlush(CField.showEffect("miro/frame"));
        client.getSession().writeAndFlush(CField.showEffect("miro/RR1/" + f));
        client.getSession().writeAndFlush(CField.showEffect("miro/RR2/" + s));
        client.getSession().writeAndFlush(CField.showEffect("miro/RR3/" + t));
        int ring[] = {1112585,1112586,1112663,1112318,1112319,1112320};
        int pendent[] = {1123007,1123008,1123009,1123010,1123011,1123012};
        final int itemid = s == 1 ? pendent[t] : ring[t];
        tm.schedule(new Runnable() {
            @Override
            public void run() {
                아이템지급(f,t,itemid);
                client.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(9000134, (byte) 0, "#fn나눔고딕 Extrabold#아래에서 당신의 내면의 결과를 확인해보세요.\r\n\r\n#b혁신의 룰렛#k 에서 [#i" + itemid + "# #d#t" + itemid + "##k](이)가 소환 되었습니다.", "00 00", (byte) 0));
            }
        }, 6000);
    }

    public void 아이템지급(int f,int t,int itemid) {
        Equip 장비 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
        short str = 0;
        short dex = 0;
        short int_ = 0;
        short luk = 0;
        short unlimited = 0;
        switch (t) {
            case 0:
                switch(f) {
                    case 0:
                        str = 5;
                        break;
                    case 1:
                        int_ = 5;
                        break;
                    case 2:
                        dex = 5;
                        break;
                    case 3:
                        luk = 5;
                        break;
                    case 4:
                        if(getJob() >= 510 || getJob() <= 512) {
                            str = 5;
                        } else if (getJob() >= 520 || getJob() <= 522) {
                            dex = 5;
                        } else {
                            str = 5;
                        }
                        break;
                }
                unlimited = 3;
            case 1:
                switch(f) {
                    case 0:
                        str = 10;
                        break;
                    case 1:
                        dex = 10;
                        break;
                    case 2:
                        int_ = 10;
                        break;
                    case 3:
                        luk = 10;
                        break;
                }
                unlimited = 5;
                break;
            case 2:
                switch(f) {
                    case 0:
                        str = 15;
                        break;
                    case 1:
                        int_ = 15;
                        break;
                    case 2:
                        dex = 15;
                        break;
                    case 3:
                        luk = 15;
                        break;
                    case 4:
                        if(getJob() >= 510 || getJob() <= 512) {
                            str = 15;
                        } else if (getJob() >= 520 || getJob() <= 522) {
                            dex = 15;
                        } else {
                            str = 15;
                        }
                        break;
                }
                unlimited = 7;
                break;
            case 3:
                switch(f) {
                    case 0:
                        str = 20;
                        break;
                    case 1:
                        int_ = 20;
                        break;
                    case 2:
                        dex = 20;
                        break;
                    case 3:
                        luk = 20;
                        break;
                    case 4:
                        if(getJob() >= 510 || getJob() <= 512) {
                            str = 20;
                        } else if (getJob() >= 520 || getJob() <= 522) {
                            dex = 20;
                        } else {
                            str = 20;
                        }
                        break;
                }
                unlimited = 9;
            case 4:
                switch(f) {
                    case 0:
                        str = 25;
                        break;
                    case 1:
                        int_ = 25;
                        break;
                    case 2:
                        dex = 25;
                        break;
                    case 3:
                        luk = 25;
                        break;
                    case 4:
                        if(getJob() >= 510 || getJob() <= 512) {
                            str = 25;
                        } else if (getJob() >= 520 || getJob() <= 522) {
                            dex = 25;
                        } else {
                            str = 25;
                        }
                        break;
                }
                unlimited = 11;
                break;
            case 5:
                switch(f) {
                    case 0:
                        str = 30;
                        break;
                    case 1:
                        int_ = 30;
                        break;
                    case 2:
                        dex = 30;
                        break;
                    case 3:
                        luk = 30;
                        break;
                    case 4:
                        if(getJob() >= 510 || getJob() <= 512) {
                            str = 30;
                        } else if (getJob() >= 520 || getJob() <= 522) {
                            dex = 30;
                        } else {
                            str = 30;
                        }
                        break;
                }
                unlimited = 15;
                break;
        }
        장비.setStr(str);
        장비.setDex(dex);
        장비.setInt(int_);
        장비.setLuk(luk);
        장비.setBossDamage((byte)unlimited);
        장비.setTotalDamage((byte)unlimited);
        장비.setAllStat((byte)unlimited);
        MapleInventoryManipulator.addbyItem(client, 장비, false);
    }
        
    
    public void resetHyperStats(int position, int skillid) {
        try { // Commit to db first
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM hyperstats WHERE charid = ? AND position = ?");
            ps.setInt(1, id);
            ps.setInt(2, position);
            ps.execute();
            ps.close();
            con.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

}
