package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.SecondaryStat;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import constants.KoreaCalendar;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import handling.channel.handler.PlayerHandler;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.Obstacle;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.SkillCustomInfo;
import server.Timer;
import server.events.MapleBattleGroundCharacter;
import server.field.boss.MapleBossManager;
import server.field.boss.demian.FlyingSwordNode;
import server.field.boss.demian.MapleDelayedAttack;
import server.field.boss.demian.MapleFlyingSword;
import server.field.boss.demian.MapleIncinerateObject;
import server.field.boss.lotus.MapleEnergySphere;
import server.field.boss.will.SpiderWeb;
import server.field.skill.MapleFieldAttackObj;
import server.field.skill.MapleMagicSword;
import server.field.skill.MapleMagicWreck;
import server.field.skill.MapleOrb;
import server.field.skill.MapleSecondAtom;
import server.field.skill.SecondAtom;
import server.field.skill.SpecialPortal;
import server.games.BattleGroundGameHandler;
import server.games.BloomingRace;
import server.life.EliteMonsterGradeInfo;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MapleNPC;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.life.MonsterDropEntry;
import server.life.OverrideMonsterStats;
import server.life.SpawnPoint;
import server.life.SpawnPointAreaBoss;
import server.life.Spawns;
import server.polofritto.FrittoDancing;
import server.polofritto.MapleRandomPortal;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.packet.BattleGroundPacket;
import tools.packet.BossRewardMeso;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.PacketHelper;
import tools.packet.PetPacket;
import tools.packet.SLFCGPacket;
import tools.packet.SkillPacket;

public final class MapleMap {

    private final Map<MapleMapObjectType, ConcurrentHashMap<Integer, MapleMapObject>> mapobjects;

    private final List<MapleCharacter> characters = new CopyOnWriteArrayList<>();

    private ScheduledFuture<?> schedule;

    private int runningOid = 1;

    private final Lock runningOidLock = new ReentrantLock();

    public final List<Spawns> monsterSpawn = new ArrayList<>();

    private final Map<Integer, MaplePortal> portals = new HashMap<>();

    private MapleFootholdTree footholds = null;

    private final AtomicInteger spawnedMonstersOnMap = new AtomicInteger(0);

    private List<MapleMonster> RealSpawns = new ArrayList<>();

    private float monsterRate;

    private float recoveryRate;

    private MapleMapEffect mapEffect;

    private String fieldType = "";

    private byte channel;

    private boolean isBlackMage3thSkilled = false;

    private short decHP = 0;

    private short createMobInterval = 3000;

    private short top = 0;

    private short bottom = 0;

    private short left = 0;

    private short right = 0;

    private int consumeItemCoolTime = 0;

    private int protectItem = 0;

    private int decHPInterval = 10000;

    private int mapid;

    private int returnMapId;

    private int timeLimit;

    private int lucidCount = 0;

    private int lucidUseCount = 0;

    private int fieldLimit;

    private int maxRegularSpawn = 0;

    private int fixedMob;

    private int forcedReturnMap = 999999999;

    private int instanceid = -1;

    private int candles = 0;

    private int lightCandles = 0;

    private int reqTouched = 0;

    private int lvForceMove = 0;

    private int lvLimit = 0;

    private int permanentWeather = 0;

    private int partyBonusRate = 0;

    private int burning = 10;

    private int burningDecreasetime = 0;

    private int runeCurse = 0;

    private int stigmaDeath = 0;

    private int BattleGroundTimer = 0;

    private int BattleGroundMainTimer = 0;

    private boolean town;

    private boolean clock;

    private boolean personalShop;

    private boolean everlast = false;

    private boolean dropsDisabled = false;

    private boolean gDropsDisabled = false;

    private boolean soaring = false;

    private boolean squadTimer = false;

    private boolean isSpawns = true;

    private boolean checkStates = true;

    private boolean firstUserEnter = true;

    private boolean bingoGame = false;

    private boolean isEliteField = false;

    private String mapName;

    private String streetName;

    private String onUserEnter;

    private String onFirstUserEnter;

    private String speedRunLeader = "";

    private List<Integer> dced = new ArrayList<>();

    private ScheduledFuture<?> squadSchedule;

    private ScheduledFuture<?> catchstart = null;

    private ScheduledFuture<?> eliteBossSchedule;

    private long speedRunStart = 0L;

    private long lastSpawnTime = 0L;

    private long lastHurtTime = 0L;

    private long timer = 0L;

    private long sandGlassTime = 0L;

    public long lastStigmaTime = 0L;

    public long lastIncinerateTime = 0L;

    public long burningIncreasetime = System.currentTimeMillis();

    private MapleNodes nodes;

    private List<MapleMagicWreck> wrecks = new ArrayList<>();

    private MapleRune rune;

    private Map<Integer, List<Integer>> monsterDefense = new LinkedHashMap<>();

    public String[] name = new String[10];

    public int voteamount = 0;

    public int runeCurseTime = 0;

    public boolean dead = false;

    public boolean MapiaIng = false;

    public boolean eliteBossAppeared = false;

    public String names = "";

    public String deadname = "";

    public int MapiaChannel;

    public int aftertime;

    public int nighttime;

    public int votetime;

    public int nightnumber = 0;

    public int eliteRequire = 0;

    public int killCount = 0;

    public int eliteCount = 0;

    public int citizenmap1;

    public int citizenmap2;

    public int citizenmap3;

    public int citizenmap4;

    public int citizenmap5;

    public int citizenmap6;

    public int mapiamap;

    public int policemap;

    public int drmap;

    public int morningmap;

    public int playern;

    public int mbating;

    private transient Map<Integer, SkillCustomInfo> customInfo = new LinkedHashMap<>();

    public static List<Pair<Integer, Point>> uniconflower = new ArrayList<>();

    private List<Point> willpoison = new ArrayList<>();

    private List<Point> LucidDream = new ArrayList<>();

    public int elitetime = 0;

    public int EliteMobCount = 0;

    public int EliteMobCommonCount = 0;

    public int elitebossrewardtype = 0;

    public int elitechmpcount = 0;

    public int elitechmptype = 0;

    public int PapulratusTime = 0;

    public int PapulratusPatan = 0;

    public int Papullatushour = 0;

    public int Papullatusminute = 0;

    public int Mapcoltime = 0;

    public int barrierArc = 0;

    public int barrierAut = 0;

    private boolean elitebossmap;

    private boolean elitebossrewardmap;

    private boolean eliteChmpmap;

    private boolean elitechmpfinal;
    
    /*  165 */   public int partyquest = 0;
/*  166 */   public int moonstak = 0, Monstermarble = 0;
/*  167 */   public int mooncake = 0;
/*  169 */   public int rpportal = 0, RPTicket = 0;
/*  168 */   public int KerningPQ = 0;

    public MapleMap(int mapid, int channel, int returnMapId, float monsterRate) {
        this.mapid = mapid;
        this.channel = (byte) channel;
        this.returnMapId = returnMapId;
        this.eliteRequire = Randomizer.rand(500, 1500);
        if (this.returnMapId == 999999999) {
            this.returnMapId = mapid;
        }
        if (GameConstants.getPartyPlay(mapid) > 0) {
            this.monsterRate = (monsterRate - 1.0F) * 2.5F + 1.0F;
        } else {
            this.monsterRate = monsterRate;
        }
        this.monsterRate *= 2.0F;
        Map<MapleMapObjectType, ConcurrentHashMap<Integer, MapleMapObject>> objsMap = new ConcurrentHashMap<>();
        for (MapleMapObjectType type : MapleMapObjectType.values()) {
            objsMap.put(type, new ConcurrentHashMap<>());
        }
        this.mapobjects = Collections.unmodifiableMap(objsMap);
        this.schedule = Timer.BuffTimer.getInstance().register(new MapleMapManagement(), 1000L);
    }

    private class MapleMapManagement implements Runnable {

        private MapleMapManagement() {
        }

        public void handleBurningStatus(long time) {
            if (MapleMap.this.getAllNormalMonstersThreadsafe().size() > 0 && !MapleMap.this.isTown() && !GameConstants.로미오줄리엣(MapleMap.this.getId()) && !GameConstants.사냥컨텐츠맵(MapleMap.this.getId()) && MapleMap.this.isSpawnPoint() && !GameConstants.보스맵(MapleMap.this.getId()) && !GameConstants.isContentsMap(MapleMap.this.getId())) {
                if (MapleMap.this.getAllCharactersThreadsafe().size() == 0) {
                    if (MapleMap.this.getBurning() < 10) {
                        if (time - MapleMap.this.getBurningIncreasetime() > 500000L) {
                            MapleMap.this.setBurningIncreasetime(time);
                            MapleMap.this.setBurning(MapleMap.this.getBurning() + 1);
                        }
                    } else if (MapleMap.this.getBurning() == 10) {
                        MapleMap.this.setBurningIncreasetime(time);
                    }
                } else if (MapleMap.this.getBurning() > 0
                        && time - MapleMap.this.getBurningIncreasetime() > 1200000L) {
                    MapleMap.this.setBurningIncreasetime(time);
                    MapleMap.this.setBurning(MapleMap.this.getBurning() - 1);
                    if (MapleMap.this.getBurning() > 0) {
                        MapleMap.this.broadcastMessage(CField.EffectPacket.showBurningFieldEffect("#fn나눔고딕 ExtraBold##fs26#          버닝 " + MapleMap.this.burning + "단계 : 경험치 " + (MapleMap.this.burning * 10) + "% 추가지급!!          "));
                    } else {
                        MapleMap.this.broadcastMessage(CField.EffectPacket.showBurningFieldEffect("#fn나눔고딕 ExtraBold##fs26#          버닝필드 소멸!          "));
                    }
                }
            }
        }

        public void handleCharacters(long time) {
            Iterator<MapleCharacter> chrs = MapleMap.this.getAllCharactersThreadsafe().iterator();
            while (chrs.hasNext()) {
                MapleCharacter chr = chrs.next();
                if (chr.isAlive()) {
                    if (MapleMap.this.getId() >= 105200520 && MapleMap.this.getId() < 105200530
                            && chr.getBuffedEffect(SecondaryStat.NotDamaged) == null && chr.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
                        int minushp = (int) -(chr.getStat().getCurrentMaxHp() / 5L);
                        chr.addHP(minushp);
                        chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, minushp, 36, 0, 0, (byte) 0, true, null, null, null));
                        chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, minushp, 36, 0, 0, (byte) 0, false, null, null, null), false);
                    }
                    if (chr.getMapId() > 450008000 && chr.getMapId() < 450009000 && MapleMap.this.getNumMonsters() > 0 && chr.getMoonGauge() < 100 && chr.getSkillCustomValue(8880302) == null) {
                        int webSize = chr.getMap().getAllSpiderWeb().size();
                        if (webSize < 67) {
                            chr.setMoonGauge(Math.min(chr.getMoonGauge() + (chr.isGM() ? 100 : 2), 100));
                        }
                        chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.addMoonGauge(chr.getMoonGauge()));
                    }
                    if (chr.getBuffedValue(13111024) || chr.getBuffedValue(13120007)) {
                        List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<>();
                        int skillid = chr.getBuffedValue(13120007) ? 13120007 : 13111024;
                        MapleSummon summon = chr.getSummon(skillid);
                        Rectangle bounds = chr.getBuffedEffect(skillid).calculateBoundingBox(summon.getTruePosition(), summon.isFacingLeft());
                        if (summon != null) {
                            for (MapleMonster mob : chr.getMap().getAllMonster()) {
                                if (bounds.contains(mob.getTruePosition())) {
                                    if (skillid == 13120007) {
                                        mob.applyStatus(chr.getClient(), MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(skillid, 4000), -chr.getBuffedEffect(skillid).getW(), chr.getBuffedEffect(skillid));
                                    }
                                    mob.applyStatus(chr.getClient(), MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, 4000), -100, chr.getBuffedEffect(skillid));
                                }
                            }
                        }
                    }
                }
                if (GameConstants.isDemonAvenger(chr.getJob()) && chr.getSkillCustomValue(30010231) == null) {
                    Map<SecondaryStat, Pair<Integer, Integer>> cancelList = new HashMap<>();
                    cancelList.put(SecondaryStat.ExceedOverload, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
                    chr.getClient().send(CWvsContext.BuffPacket.cancelBuff(cancelList, chr));
                }
                if (chr.getSkillCustomValues().size() > 0) {
                    Map<Integer, SkillCustomInfo> customInfo = new LinkedHashMap<>();
                    customInfo.putAll(chr.getSkillCustomValues());
                    for (Map.Entry<Integer, SkillCustomInfo> sci : customInfo.entrySet()) {
                        if (((SkillCustomInfo) sci.getValue()).canCancel(System.currentTimeMillis())) {
                            chr.removeSkillCustomInfo(((Integer) sci.getKey()).intValue());
                            if (((Integer) sci.getKey()).intValue() == 33001001) {
                                chr.getClient().send(CField.SummonPacket.JaguarAutoAttack(false));
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 9110 && (chr.getMapId() / 100000 == 9530 || chr.getMapId() / 100000 == 9540)) {
                                chr.warp(951000000);
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 24220 && (chr.getMapId() == 450008950 || chr.getMapId() == 450008350)) {
                                chr.playerIGDead();
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 450001401) {
                                if (MapleMap.this.getId() == 450001400) {
                                    chr.getClient().send(SLFCGPacket.ErdaSpectrumArea(new int[]{33, 22962, 0, 2500, -120, -128, 120, 5, Randomizer.rand(-250, 1200), 47}));
                                    chr.setSkillCustomInfo(450001401, 0L, Randomizer.rand(5000, 7000));
                                }
                            }
                        }
                    }
                }
            }
        }

        public void handleMists(long time) {
            List<MapleMist> toRemove = new ArrayList<>();
            Iterator<MapleMist> mists = MapleMap.this.getAllMistsThreadsafe().iterator();
            while (mists.hasNext()) {
                MapleMist mist = mists.next();
                int endTime = mist.getEndTime();
                int duration = mist.getDuration();
                boolean isEnd = false;
                if (endTime > 0) {
                    isEnd = (time - mist.getStartTime() >= endTime);
                } else if (duration > 0) {
                    isEnd = (time - mist.getStartTime() >= duration);
                }
                if (isEnd) {
                    if (mist.getSourceSkill() != null) {
                        if (mist.getSourceSkill().getId() == 400041008 && mist.getDuration() != 1800) {
                            int spearSize = 0;
                            for (MapleMist mistz : MapleMap.this.getAllMistsThreadsafe()) {
                                if (mistz.getSourceSkill().getId() == 400040008 || mistz.getSourceSkill().getId() == 400041008) {
                                    spearSize++;
                                }
                            }
                            MapleMist spear = new MapleMist(new Rectangle((mist.getBox()).x, (mist.getBox()).y + 25, (mist.getBox()).width, (mist.getBox()).height + 25), mist.getOwner(), SkillFactory.getSkill((spearSize >= 5) ? 400041008 : 400040008).getEffect(mist.getSkillLevel()), 1800, (byte) 0);
                            spear.setPosition(mist.getTruePosition());
                            MapleMap.this.spawnMist(spear, false);
                        } else if (mist.getSourceSkill().getId() == 400051025) {
                            MapleMist icbm = new MapleMist(new Rectangle((mist.getBox()).x, (mist.getBox()).y, (mist.getBox()).width, (mist.getBox()).height), mist.getOwner(), SkillFactory.getSkill(400051026).getEffect(mist.getSkillLevel()), 15000, (byte) 0);
                            icbm.setPosition(mist.getTruePosition());
                            icbm.setDelay(0);
                            MapleMap.this.spawnMist(icbm, false);
                        }
                    }
                    toRemove.add(mist);
                } else if (mist.getOwner() != null) {
                    if (MapleMap.this.getCharacter(mist.getOwner().getId()) == null) {
                        toRemove.add(mist);
                    }
                } else if (mist.getMob() != null
                        && MapleMap.this.getMonsterByOid(mist.getMob().getObjectId()) == null) {
                    toRemove.add(mist);
                }
                if (mist.getOwner() != null && !toRemove.contains(mist)) {
                    mist.getOwner().checkMistStatus(mist, MapleMap.this.getAllMonstersThreadsafe(), time);
                    continue;
                }
                if (mist.isMobMist()
                        && mist.getMob() != null) {
                    MapleCharacter chrs = mist.getMob().getController();
                    if (chrs != null) {
                        chrs.checkMistStatus(mist, MapleMap.this.getAllMonstersThreadsafe(), time);
                    }
                }
            }
            for (MapleMist mist : toRemove) {
                MapleMap.this.broadcastMessage(CField.removeMist(mist));
                MapleMap.this.removeMapObject(mist);
            }
        }

        public void handleMobs(long time) {
            if (MapleMap.this.canSpawn(time)) {
                MapleMap.this.respawn(false, time);
            }
            if (MapleMap.this.getElitebossrewardtype() == 2 && MapleMap.this.isElitebossrewardmap()
                    && MapleMap.this.getCustomValue(81111111) == null) {
                MapleMap.this.setCustomInfo(81111111, 0, Randomizer.rand(200, 1000));
                MapleCharacter chr = null;
                final Iterator<MapleCharacter> iterator = MapleMap.this.getAllChracater().iterator();
                if (iterator.hasNext()) {
                    MapleCharacter chr2 = iterator.next();
                    chr = chr2;
                }
                for (MapleMonster mapleMonster : MapleMap.this.getAllMonster()) {
                    if (mapleMonster.getId() == 8220028) {
                        int[] itemlist = {2432391, 2432392, 2432393, 2432394, 2432395, 2432396, 2432397};
                        int Random = 0;
                        for (int i = 0; i < 4; i++) {
                            Random = (int) Math.floor(Math.random() * itemlist.length);
                            int itemid = itemlist[Random];
                            Item toDrop = new Item(itemid, (short) 0, (short) 1, 0);
                            MapleMap.this.spawnFlyingDrop(chr, mapleMonster.getPosition(), mapleMonster.getPosition(), toDrop);
                            if (Randomizer.isSuccess(5)) {
                                toDrop = new Item(2432398, (short) 0, (short) 1, 0);
                                MapleMap.this.spawnFlyingDrop(chr, mapleMonster.getPosition(), mapleMonster.getPosition(), toDrop);
                            }
                        }
                    }
                }
            }
            if (MapleMap.this.getElitebossrewardtype() == 3 && MapleMap.this.isElitebossrewardmap()
                    && MapleMap.this.getCustomValue(81111111) == null) {
                MapleMap.this.setCustomInfo(81111111, 0, Randomizer.rand(200, 1000));
                int min_x = MapleMap.this.getLeft();
                int min_y = MapleMap.this.getBottom();
                int max_x = MapleMap.this.getRight();
                int max_y = MapleMap.this.getTop();
                Iterator<MapleCharacter> iterator = MapleMap.this.getAllChracater().iterator();
                if (iterator.hasNext()) {
                    MapleCharacter chr = iterator.next();
                    int[] itemlist = {2432391, 2432392, 2432393, 2432394, 2432395, 2432396, 2432397};
                    int Random = 0;
                    for (int i = 0; i < Randomizer.rand(30, 60); i++) {
                        Point pos = new Point(Randomizer.rand(min_x, max_x), max_y - 500);
                        Random = (int) Math.floor(Math.random() * itemlist.length);
                        int itemid = itemlist[Random];
                        Item toDrop = new Item(itemid, (short) 0, (short) 1, 0);
                        MapleMap.this.spawnFlyingDrop(chr, pos, pos, toDrop);
                        if (Randomizer.isSuccess(4)) {
                            toDrop = new Item(2432398, (short) 0, (short) 1, 0);
                            MapleMap.this.spawnFlyingDrop(chr, pos, pos, toDrop);
                        }
                    }
                }
            }
            if (MapleMap.this.getId() == 450008350 || MapleMap.this.getId() == 450008950) {
                for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                    chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.setMoonGauge(100, 25));
                    Iterator<Point> pos2 = chr.getMap().getWillPoison().iterator();
                    if (!chr.getMap().getWillPoison().isEmpty()) {
                        while (pos2.hasNext()) {
                            Point pos = pos2.next();
                            if (pos.x - 100 < (chr.getPosition()).x && pos.x + 100 > (chr.getPosition()).x
                                    && chr.isAlive()
                                    && chr.getBuffedEffect(SecondaryStat.NotDamaged) == null && chr.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
                                chr.addHP(-(chr.getStat().getCurrentMaxHp() / 100L) * 44L);
                                chr.getMap().broadcastMessage(CField.playerDamaged(chr.getId(), (int) (chr.getStat().getCurrentMaxHp() / 100L) * 44));
                            }
                        }
                    }
                }
            }
            if (MapleMap.this.rune != null && !MapleMap.this.getAllCharactersThreadsafe().isEmpty() && MapleMap.this.isSpawnPoint() && !GameConstants.보스맵(MapleMap.this.mapid) && !GameConstants.isContentsMap(MapleMap.this.mapid)
                    && MapleMap.this.getRuneCurse() < 4) {
                MapleMap.this.runeCurseTime++;
                if (MapleMap.this.runeCurseTime >= 120) {
                    MapleMap.this.runeCurseTime = 0;
                    MapleMap.this.setRuneCurse(MapleMap.this.getRuneCurse() + 1);
                    for (MapleCharacter allchr : MapleMap.this.getAllCharactersThreadsafe()) {
                        if (!allchr.getBuffedValue(80002282)) {
                            allchr.getClient().send(CField.runeCurse("룬을 해방하여 엘리트 보스의 저주를 풀어야 합니다!!\\n저주 " + MapleMap.this.getRuneCurse() + "단계 :  경험치 획득, 드롭률 " + MapleMap.this.getRuneCurseDecrease() + "% 감소 효과 적용 중", true));
                        }
                    }
                }
            }
            MapleMonster monster;
            if ((monster = MapleMap.this.getMonsterById(8220028)) != null && !MapleMap.this.isElitebossrewardmap()) {
                int[] itemlist = {2432391, 2432392, 2432393, 2432394, 2432395, 2432396, 2432397};
                int Random = 0;
                for (int i = 0; i < 3; i++) {
                    Random = (int) Math.floor(Math.random() * itemlist.length);
                    int itemid = itemlist[Random];
                    Item toDrop = new Item(itemid, (short) 0, (short) 1, 0);
                    MapleMap.this.spawnFlyingDrop(monster.getController(), monster.getPosition(), monster.getPosition(), toDrop);
                }
                monster.addSkillCustomInfo(8220028, -1L);
                if (monster.getCustomValue0(8220028) <= 0L) {
                    MapleMap.this.killMonsterType(monster, 1);
                }
            }
            if (((monster = MapleMap.this.getMonsterById(8880100)) != null || (monster = MapleMap.this.getMonsterById(8880101)) != null || (monster = MapleMap.this.getMonsterById(8880110)) != null || (monster = MapleMap.this.getMonsterById(8880111)) != null)
                    && time - MapleMap.this.lastIncinerateTime >= 30000L) {
                if (MapleMap.this.lastIncinerateTime != 0L) {
                    MapleMap.this.spawnIncinerateObject(new MapleIncinerateObject(Randomizer.nextInt(MapleMap.this.getRight() - MapleMap.this.getLeft()) + MapleMap.this.getLeft(), 16));
                }
                MapleMap.this.lastIncinerateTime = time;
            }
            Iterator<MapleMonster> lifes = MapleMap.this.getAllMonstersThreadsafe().iterator();
            while (lifes.hasNext()) {
                MapleMonster life = lifes.next();
                if (life.getCustomValues().size() > 0) {
                    Map<Integer, SkillCustomInfo> customInfo = new LinkedHashMap<>();
                    customInfo.putAll(life.getCustomValues());
                    for (Map.Entry<Integer, SkillCustomInfo> sci : customInfo.entrySet()) {
                        if (((SkillCustomInfo) sci.getValue()).canCancel(System.currentTimeMillis())) {
                            life.removeCustomInfo(((Integer) sci.getKey()).intValue());
                            if (((Integer) sci.getKey()).intValue() == 400011122) {
                                life.removeCustomInfo(400011121);
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 24205) {
                                MobSkill msi = MobSkillFactory.getMobSkill(242, 5);
                                msi.applyEffect(null, life, true, life.isFacingLeft());
                                life.setCustomInfo(24205, 0, 120000);
                            }
                        }
                    }
                }
                List<Pair<MonsterStatus, MonsterStatusEffect>> cancelsf = new ArrayList<>();
                for (Pair<MonsterStatus, MonsterStatusEffect> cancel : life.getStati()) {
                    if (!MapleMap.this.getAllChracater().contains(((MonsterStatusEffect) cancel.getRight()).getChr()) && !((MonsterStatusEffect) cancel.getRight()).isMobskill()) {
                        cancelsf.add(new Pair<>(cancel.getLeft(), cancel.getRight()));
                        break;
                    }
                }
                if (!cancelsf.isEmpty()) {
                    life.cancelStatus(cancelsf);
                }
                if ((life.getId() == 8950100 || life.getId() == 8950000)
                        && life.getBuff(MonsterStatus.MS_Laser) != null
                        && !life.isSkillForbid()) {
                    if (Randomizer.isSuccess(1) && life.getCustomValue(2287) == null && life.getCustomValue(22878) == null) {
                        MobSkillFactory.getMobSkill(228, 7).applyEffect(life.getController(), life, true, true);
                    }
                    if (Randomizer.isSuccess(3) && life.getCustomValue(2286) == null && life.getCustomValue(2287) == null) {
                        MobSkillFactory.getMobSkill(228, 6).applyEffect(life.getController(), life, true, true);
                        life.setCustomInfo(2286, 0, 60000);
                    } else {
                        int plus = 9;
                        plus *= life.getEnergyspeed();
                        if (life.isEnergyleft()
                                && plus > 0) {
                            plus *= -1;
                        }
                        life.setEnergycount(life.getEnergycount() + plus);
                        if (life.isEnergyleft()) {
                            if (life.getEnergycount() <= 0) {
                                life.setEnergycount(360 - life.getEnergycount());
                            }
                        } else if (life.getEnergycount() >= 360) {
                            life.setEnergycount(life.getEnergycount() - 360);
                        }
                        life.getMap().broadcastMessage(MobPacket.LaserHandler(life.getObjectId(), life.getEnergycount(), life.getEnergyspeed(), life.isEnergyleft() ? 0 : 1));
                    }
                }
                if (life.getId() == 8880503) {
                    for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                        if (chr != null) {
                            int pix;
                            boolean inside = false;
                            int phase = 0;
                            if (life.getHPPercent() <= 33) {
                                phase = 3;
                            } else if (life.getHPPercent() <= 66) {
                                phase = 2;
                            } else {
                                phase = 1;
                            }
                            if (life.getPhase() != phase) {
                                life.setPhase((byte) phase);
                                MapleMap.this.broadcastMessage(MobPacket.changePhase(life));
                                MapleMap.this.broadcastMessage(MobPacket.changeMobZone(life));
                            }
                            if (phase == 3) {
                                pix = 160;
                            } else if (phase == 2) {
                                pix = 275;
                            } else {
                                pix = 410;
                            }
                            if (chr.getSkillCustomValue(143144) == null) {
                                if (chr.getTruePosition().getX() > life.getTruePosition().getX() - pix && chr.getTruePosition().getX() < life.getTruePosition().getX() + pix) {
                                    inside = true;
                                }
                                Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
                                statups.put(SecondaryStat.MobZoneState, new Pair<>(Integer.valueOf(inside ? 1 : 0), Integer.valueOf(life.getObjectId())));
                                chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, chr.getPlayer()));
                                chr.setSkillCustomInfo(143145, !inside ? 1L : 0L, 0L);
                                if (!inside) {
                                    chr.setSkillCustomInfo(143144, 0L, 100L);
                                }
                            }
                        }
                    }
                }
                if (life.getId() == 8870100) {
                    if (life.getCustomValue(8870100) != null
                            && life.getCustomValue0(8870100) > 0L) {
                        life.addSkillCustomInfo(8870100, -1L);
                        List<Integer> list = new ArrayList<>();
                        for (MapleMonster monsters1 : MapleMap.this.getAllMonster()) {
                            if (monsters1.getId() != 8870100) {
                                list.add(Integer.valueOf(monsters1.getObjectId()));
                            }
                        }
                        if (list.size() > 0) {
                            long healhp = life.getStats().getHp() / 100L * 5L * list.size();
                            if (healhp + life.getHp() >= life.getStats().getHp()) {
                                life.setHp(life.getStats().getHp());
                            } else {
                                life.setHp(life.getHp() + healhp);
                            }
                            MapleMap.this.broadcastMessage(MobPacket.HillaDrainEffect(life.getObjectId(), list));
                            MapleMap.this.broadcastMessage(MobPacket.HillaDrainActive(life.getObjectId()));
                            MapleMap.this.broadcastMessage(MobPacket.showBossHP(life));
                        }
                    }
                    int size = 0;
                    MapleMonster hardhilla = null;
                    for (MapleMonster m : MapleMap.this.getAllMonster()) {
                        if (m.getId() == 8870107) {
                            size++;
                            continue;
                        }
                        if (m.getId() == 8870100) {
                            hardhilla = m;
                        }
                    }
                    if (size <= 0 && hardhilla != null) {
                        hardhilla.setLastSkillUsed(MobSkillFactory.getMobSkill(200, 251), System.currentTimeMillis(), (Randomizer.rand(30, 40) * 1000));
                        while (hardhilla.getBuff(MonsterStatus.MS_ExchangeAttack) != null) {
                            hardhilla.cancelStatus(MonsterStatus.MS_ExchangeAttack, hardhilla.getBuff(MonsterStatus.MS_ExchangeAttack));
                        }
                    }
                }
                if (MapleMap.this.getId() == 240060200 || MapleMap.this.getId() == 240060201) {
                    MapleMonster hontail = null;
                    boolean clear = false;
                    for (MapleMonster m : MapleMap.this.getAllMonster()) {
                        if ((m.getId() >= 8810002 && m.getId() <= 8810009) || (m.getId() >= 8810102 && m.getId() <= 8810109)) {
                            clear = false;
                            break;
                        }
                        if (m.getId() == 8810018 || m.getId() == 8810122) {
                            hontail = m;
                            clear = true;
                        }
                    }
                    if (clear) {
                        MapleMap.this.killMonster(hontail, hontail.getController(), true, true, (byte) 0);
                    }
                }
                if (MapleMap.this.getId() == 450008750) {
                    for (MapleMonster m : MapleMap.this.getAllMonster()) {
                        if (m.getId() != 8880315 && m.getId() != 8880316) {
                            if ((m.getPosition()).y > 0) {
                                if ((m.getController().getPosition()).y < 0) {
                                    for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                        if ((chr.getPosition()).y > 0) {
                                            m.switchController(chr, true);
                                        }
                                    }
                                }
                                continue;
                            }
                            if ((m.getController().getPosition()).y > 0) {
                                for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                    if ((chr.getPosition()).y < 0) {
                                        m.switchController(chr, true);
                                    }
                                }
                            }
                        }
                    }
                }
                if (life.getId() == 8880315 || life.getId() == 8880316) {
                    if ((life.getPosition()).y > -2300 && (life.getPosition()).y < -2020) {
                        if ((life.getController().getPosition()).y > 0) {
                            for (MapleCharacter achr : life.getMap().getAllChracater()) {
                                if (achr != null
                                        && (achr.getPosition()).y < -2000) {
                                    life.switchController(achr, false);
                                    break;
                                }
                            }
                        }
                    } else if ((life.getPosition()).y < 300 && (life.getPosition()).y > -500
                            && (life.getController().getPosition()).y < 0) {
                        for (MapleCharacter achr : life.getMap().getAllChracater()) {
                            if (achr != null
                                    && (achr.getPosition()).y > 0) {
                                life.switchController(achr, false);
                                break;
                            }
                        }
                    }
                }
                if (life.getId() == 8880102) {
                    if (life.getCustomValue(8880102) == null
                            && life.getController() != null && MapleMap.this.getAllChracater().size() > 1) {
                        MapleCharacter other = null;
                        for (MapleCharacter chrs : MapleMap.this.getAllChracater()) {
                            if (chrs.getId() != life.getId()) {
                                other = chrs;
                                break;
                            }
                        }
                        if (other != null) {
                            life.setCustomInfo(8880102, 0, 10000);
                            life.switchController(other, true);
                        }
                    }
                    MapleMonster demian = null;
                    for (MapleMonster monster1 : MapleMap.this.getAllMonster()) {
                        if (monster1.getId() == 8880101 || monster1.getId() == 8880111) {
                            demian = monster1;
                            break;
                        }
                    }
                    if (demian != null) {
                        int pix;
                        if (demian.getPhase() == 2) {
                            pix = 100;
                        } else if (demian.getPhase() == 3) {
                            pix = 130;
                        } else if (demian.getPhase() == 4) {
                            pix = 150;
                        } else {
                            pix = 50;
                        }
                        for (MapleCharacter chr : MapleMap.this.getAllCharactersThreadsafe()) {
                            if (chr.getSkillCustomValue(143144) == null) {
                                boolean damaged = false;
                                if (chr.getTruePosition().getX() > life.getTruePosition().getX() - pix && chr.getTruePosition().getX() < life.getTruePosition().getX() + pix) {
                                    damaged = true;
                                }
                                Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
                                statups.put(SecondaryStat.MobZoneState, new Pair<>(Integer.valueOf(damaged ? 1 : 0), Integer.valueOf(demian.getObjectId())));
                                chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, chr.getPlayer()));
                                chr.setSkillCustomInfo(143143, damaged ? 1L : 0L, 0L);
                                if (damaged) {
                                    chr.getPercentDamage(demian, 999, 999, 5, true);
                                    chr.setSkillCustomInfo(143144, 0L, 100L);
                                }
                            }
                        }
                    }
                }
                if ((life.getId() == 8500001 || life.getId() == 8500002 || life.getId() == 8500011 || life.getId() == 8500012 || life.getId() == 8500021 || life.getId() == 8500022)
                        && life != null) {
                    if (life.getPatten() > 0) {
                        life.SetPatten(life.getPatten() - 1);
                        if (MapleMap.this.PapulratusPatan == 0) {
                            if (life.getPatten() <= 0) {
                                MapleMap.this.PapulratusPatan = 1;
                                MapleMap.this.PapulratusTime = 0;
                                life.SetPatten(30);
                                MapleMap.this.broadcastMessage(CField.ActivePotionCooldown(6));
                                MapleMap.this.broadcastMessage(MobPacket.SpeakingMonster(life, (life.getId() == 8500001 || life.getId() == 8500011 || life.getId() == 8500021) ? 1 : 2, 0));
                                MapleMap.this.broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 0));
                                MapleMap.this.broadcastMessage(MobPacket.setAttackZakumArm(life.getObjectId(), 4));
                                MapleMap.this.broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTime(life.getPatten() * 1000, true, false));
                            }
                        } else if (MapleMap.this.PapulratusPatan == 1) {
                            if (life.getPatten() <= 0) {
                                MapleMap.this.PapulratusPatan = 0;
                                MapleMap.this.PapulratusTime = 50;
                                life.SetPatten(120);
                                MapleMap.this.broadcastMessage(CField.ActivePotionCooldown(0));
                                MapleMap.this.broadcastMessage(MobPacket.setAttackZakumArm(life.getObjectId(), 5));
                                int typeed = life.getId() % 100 / 10;
                                String difical = (typeed == 0) ? "Easy" : ((typeed == 1) ? "Normal" : "Chaos");
                                int rand = difical.equals("Easy") ? Randomizer.rand(1, 2) : Randomizer.rand(1, 3);
                                MapleMap.this.broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(false, rand));
                                MapleMap.this.broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTime(life.getPatten() * 1000, false, false));
                            } else if (life.getSeperateSoul() <= 0
                                    && (life.getMap()).PapulratusPatan == 1 && life.getCustomValue(8500001) == null) {
                                MapleMap.this.broadcastMessage(MobPacket.BossPapuLatus.dropPaPul());
                                life.setCustomInfo(8500001, 0, 2000);
                            }
                        }
                    }
                    if (MapleMap.this.getId() == 105200120 || MapleMap.this.getId() == 105200510 || MapleMap.this.getId() == 105200510 || MapleMap.this.PapulratusPatan == 1) {
                        for (MapleCharacter chr : MapleMap.this.getAllCharactersThreadsafe()) {
                            if (chr.isAlive() && chr.getBuffedValue(SecondaryStat.NotDamaged) == null && chr.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
                                chr.addHP(-(chr.getStat().getCurrentMaxHp() * 10L) / 100L);
                                chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, (int) -(chr.getStat().getCurrentMaxHp() * 10L) / 100, 36, 0, 0, (byte) 0, true, null, null, null));
                                chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, (int) -(chr.getStat().getCurrentMaxHp() * 10L) / 100, 36, 0, 0, (byte) 0, false, null, null, null), false);
                            }
                        }
                    }
                }
                if (life.isBuffed(MonsterStatus.MS_Burned)) {
                    MonsterStatusEffect buff = life.getBuff(MonsterStatus.MS_Burned);
                    if (buff != null && life.getHp() > 1L && buff.getInterval() > 0 && buff.getChr() != null) {
                        boolean damage = true;
                        if (buff.getInterval() > 1000
                                && time - buff.getLastPoisonTime() < buff.getInterval()) {
                            damage = false;
                        }
                        if (damage) {
                            buff.setLastPoisonTime(time);
                            life.damage(buff.getChr(), Math.min(life.getHp() - 1L, buff.getValue()), true);
                            if (buff.getSkill() == 25121006 && buff.getChr().getSkillLevel(25121006) > 0
                                    && buff.getChr().isAlive()) {
                                buff.getChr().getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(buff.getChr(), 0, 25121006, 4, 0, 0, (byte) (buff.getChr().isFacingLeft() ? 1 : 0), true, buff.getChr().getPosition(), null, null));
                                buff.getChr().getMap().broadcastMessage(buff.getChr(), CField.EffectPacket.showEffect(buff.getChr(), 0, 25121006, 4, 0, 0, (byte) (buff.getChr().isFacingLeft() ? 1 : 0), false, buff.getChr().getPosition(), null, null), false);
                                buff.getChr().addHP(buff.getValue() * SkillFactory.getSkill(buff.getSkill()).getEffect(buff.getChr().getSkillLevel(25121006)).getX() / 100L);
                            }
                        }
                    }
                }
            }
        }

        public void run() {
            long time = System.currentTimeMillis();
            if (MapleMap.this.characterSize() > 0) {
                MapleMap.this.checkDropItems(time);
                handleMobs(time);
                handleCharacters(time);
                handleMists(time);
                if (MapleMap.this.BattleGroundMainTimer > 0) {
                    MapleMap.this.BattleGroundMainTimer--;
                    if (MapleMap.this.BattleGroundMainTimer == 539) {
                        MapleMap.this.broadcastMessage(CField.ImageTalkNpc(9001153, 5000, "무적 상태가 해제 되었습니다. 플레이어들끼리 전투가 가능합니다!"));
                        BattleGroundGameHandler.setNotDamage(false);
                    } else if (MapleMap.this.BattleGroundMainTimer == 330) {
                        MapleMap.this.broadcastMessage(CField.environmentChange("Map/Effect2.img/PvP/Boss", 16));
                        MapleMap.this.broadcastMessage(CField.ImageTalkNpc(9001153, 5000, "보스존에 #r홀로 드래곤#k이 스폰 되었습니다. 처치시 막대한 #b경험치와 골드#k를 얻을 수 있습니다!"));
                        MapleMap.this.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9303206), new Point(-380, -58));
                    } else if (MapleMap.this.BattleGroundMainTimer == 4) {
                        MapleMap.this.broadcastMessage(CField.environmentChange("Map/Effect2.img/PvP/Start", 16));
                        MapleMap.this.broadcastMessage(SLFCGPacket.playSE("Sound/MiniGame.img/BattlePvp/Start"));
                    }
                    if (MapleMap.this.BattleGroundMainTimer <= 0) {
                        MapleMap.this.BattleGroundMainTimer = 0;
                        BattleGroundGameHandler.EndPlayGamez(MapleMap.this);
                    }
                }
            }
            handleBurningStatus(time);
            if (MapleMap.this.elitetime > 0 && MapleMap.this.isElitebossmap()) {
                MapleMap.this.elitetime--;
                if (MapleMap.this.elitetime % 10 == 0) {
                    MapleMonster mob = null;
                    for (MapleMonster mobs : MapleMap.this.getAllMonster()) {
                        if (mobs.getId() == 8220022 || mobs.getId() == 8220023 || mobs.getId() == 8220024 || mobs.getId() == 8220025 || mobs.getId() == 8220026) {
                            mob = mobs;
                            break;
                        }
                    }
                    if (mob != null) {
                        World.Broadcast.broadcastMessage(CWvsContext.eliteWarning(MapleMap.this.getId(), mob.getId(), MapleMap.this.getChannel() - 1));
                    }
                }
                if (MapleMap.this.elitetime <= 0) {
                    MapleMap.this.stopEliteBossMap();
                }
            }
            if (MapleMap.this.PapulratusTime > 0) {
                MapleMap.this.PapulratusTime--;
                if (MapleMap.this.PapulratusTime == 10) {
                    MapleMap.this.broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 0));
                }
                if (MapleMap.this.PapulratusTime <= 0) {
                    MapleMap.this.PapulratusTime = 0;
                    if (MapleMap.this.getAllMonster().size() > 0
                            && MapleMap.this.PapulratusPatan == 0) {
                        MapleMap.this.PapulratusTime = 50;
                        MapleMap.this.broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(false, Randomizer.rand(1, 3)));
                    }
                }
            }
            if (MapleMap.this.Mapcoltime > 0) {
                MapleMap.this.Mapcoltime--;
                if (MapleMap.this.Mapcoltime <= 0) {
                    MapleMap.this.Mapcoltime = 0;
                }
            }
            if (MapleMap.this.BattleGroundTimer > 0) {
                MapleMap.this.BattleGroundTimer--;
                if (MapleMap.this.BattleGroundTimer <= 0) {
                    MapleMap.this.BattleGroundTimer = 0;
                    BattleGroundGameHandler.StartGame(MapleMap.this);
                }
                MapleMap.this.broadcastMessage(BattleGroundPacket.SelectAvaterClock(MapleMap.this.BattleGroundTimer));
            }
            if (ServerConstants.Event_Blooming
                    && MapleMap.this.getId() == 993192000) {
                if (((new Date()).getHours() >= 10 && (new Date()).getHours() <= 24) || (new Date()).getHours() < 2) {
                    if ((new Date()).getMinutes() >= 30 && (new Date()).getMinutes() < 40) {
                        if (MapleMap.this.getCustomValue(993192000) == null) {
                            MapleMap.this.BloomingMoment(true, false);
                        }
                    } else {
                        MapleMap.this.removeCustomInfo(993192000);
                    }
                    MapleMap.this.removeCustomInfo(993192000);
                }
            }
            if (ServerConstants.Event_MapleLive
                    && MapleMap.this.getId() == 993194000) {
                if (((new Date()).getHours() >= 10 && (new Date()).getHours() <= 24) || (new Date()).getHours() < 2) {
                    if ((new Date()).getMinutes() >= 30 && (new Date()).getMinutes() < 40) {
                        if (MapleMap.this.getCustomValue(993194000) == null) {
                            MapleMap.this.LiveShow(true, false);
                        }
                    } else {
                        MapleMap.this.removeCustomInfo(993194000);
                    }
                }
            }
            if (MapleMap.this.getCustomValues().size() > 0) {
                Map<Integer, SkillCustomInfo> customInfo = new LinkedHashMap<>();
                customInfo.putAll(MapleMap.this.getCustomValues());
                for (Iterator<Map.Entry<Integer, SkillCustomInfo>> iterator = customInfo.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<Integer, SkillCustomInfo> sci = iterator.next();
                    if (((SkillCustomInfo) sci.getValue()).canCancel(System.currentTimeMillis())) {
                        MapleMap.this.removeCustomInfo(((Integer) sci.getKey()).intValue());
                        if (MapleMap.this.getChannel() == 1) {
                            if (((Integer) sci.getKey()).intValue() == 921174101) {
                                if (MapleMap.this.getCustomValue0(921174102) < 4) {
                                    List<Point> pt = new ArrayList<>();
                                    int basex1 = -1401;
                                    int basex2 = 380;
                                    int x = basex1 + MapleMap.this.getCustomValue0(921174102) * 165;
                                    int x1 = basex2 - MapleMap.this.getCustomValue0(921174102) * 165;
                                    MapleMap.this.broadcastMessage(CField.getFieldSkillAdd(100008, 1, true));
                                    MapleMap.this.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880507), new Point(basex1 + MapleMap.this.getCustomValue0(921174102) * 165, -1423));
                                    MapleMap.this.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880508), new Point(basex2 - MapleMap.this.getCustomValue0(921174102) * 165, -1423));
                                    MapleMap.this.setCustomInfo(921174102, MapleMap.this.getCustomValue0(921174102) + 1, 0);
                                    for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                        if (x > (chr.getPosition()).x || x1 < (chr.getPosition()).x) {
                                            chr.getClient().send(CField.fireBlink(chr.getId(), new Point(-530, -1423)));
                                        }
                                    }
                                    if (MapleMap.this.getCustomValue0(921174102) < 4) {
                                        pt.add(new Point(basex1 + MapleMap.this.getCustomValue0(921174102) * 165, -1423));
                                        pt.add(new Point(basex2 - MapleMap.this.getCustomValue0(921174102) * 165, -1423));
                                        MapleMap.this.broadcastMessage(CField.getFieldSkillEffectAdd(100008, 1, pt));
                                        MapleMap.this.setCustomInfo(921174101, 0, 15000);
                                    }
                                }
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 993192000) {
                                List<Pair<String, Integer>> eff = new ArrayList<>();
                                eff.add(new Pair<>("bloomingSun", Integer.valueOf(0)));
                                eff.add(new Pair<>("bloomingWind", Integer.valueOf(0)));
                                MapleMap.this.broadcastMessage(CField.ChangeSpecialMapEffect(eff));
                                MapleMap.this.broadcastMessage(CField.removeMapEffect());
                                MapleMap.this.broadcastMessage(CField.setSpecialMapEffect("bloomingSun", 0, 0));
                                MapleMap.this.broadcastMessage(CField.setSpecialMapEffect("bloomingWind", 0, 0));
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 993192500) {
                                if (MapleMap.this.getCharactersSize() < 5) {
                                    for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                        chr.warp((Integer.parseInt(chr.getPlayer().getV("returnM")) > 0) ? Integer.parseInt(chr.getPlayer().getV("returnM")) : 993192000);
                                        chr.dropMessage(5, "<블루밍 레이스> 게임 시작에 필요한 인원이 부족하여 게임이 종료 됩니다.");
                                    }
                                    return;
                                }
                                BloomingRace.ReadyRace();
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 993192600) {
                                MapleMap.this.broadcastMessage(SLFCGPacket.BloomingRaceHandler(3, new int[0]));
                                Timer.EventTimer.getInstance().schedule(() -> BloomingRace.StartRace(), 4000L);
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 993192601) {
                                for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                    chr.warp(993192501);
                                }
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 993192602 || ((Integer) sci.getKey()).intValue() == 993192603 || ((Integer) sci.getKey()).intValue() == 993192604) {
                                if (MapleMap.this.getCharactersSize() > 0) {
                                    String name = (((Integer) sci.getKey()).intValue() == 993192602) ? "trick01" : ((((Integer) sci.getKey()).intValue() == 993192603) ? "trick02" : "trick03");
                                    List<String> str = Arrays.asList(new String[]{name});
                                    List<Integer> foothold = new ArrayList<>();
                                    int cooltime = (((Integer) sci.getKey()).intValue() == 993192602) ? 4300 : ((((Integer) sci.getKey()).intValue() == 993192603) ? 3500 : 3500);
                                    switch (((Integer) sci.getKey()).intValue()) {
                                        case 993192602:
                                            foothold.add(Integer.valueOf(726));
                                            foothold.add(Integer.valueOf(727));
                                            foothold.add(Integer.valueOf(728));
                                            foothold.add(Integer.valueOf(729));
                                            foothold.add(Integer.valueOf(736));
                                            foothold.add(Integer.valueOf(737));
                                            break;
                                        case 993192603:
                                            foothold.add(Integer.valueOf(681));
                                            foothold.add(Integer.valueOf(682));
                                            foothold.add(Integer.valueOf(683));
                                            break;
                                        case 993192604:
                                            foothold.add(Integer.valueOf(649));
                                            foothold.add(Integer.valueOf(650));
                                            foothold.add(Integer.valueOf(656));
                                            foothold.add(Integer.valueOf(716));
                                            foothold.add(Integer.valueOf(717));
                                            foothold.add(Integer.valueOf(718));
                                            break;
                                    }
                                    int endtime = (((Integer) sci.getKey()).intValue() == 993192602) ? 2500 : ((((Integer) sci.getKey()).intValue() == 993192603) ? 3500 : 3500);
                                    MapleMap.this.broadcastMessage(SLFCGPacket.FootHoldOnOff(foothold, true));
                                    MapleMap.this.broadcastMessage(SLFCGPacket.FootHoldOnOffEffect(str, true));
                                    Timer.EventTimer.getInstance().schedule(() -> {
                                        MapleMap.this.setCustomInfo(((Integer) sci.getKey()).intValue(), 0, endtime);
                                        MapleMap.this.broadcastMessage(SLFCGPacket.FootHoldOnOff(foothold, false));
                                        MapleMap.this.broadcastMessage(SLFCGPacket.FootHoldOnOffEffect(str, false));
                                    }, cooltime);
                                }
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 993026900) {
                                if (MapleMap.this.getCharactersSize() < 4) {
                                    for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                        chr.warp(Integer.parseInt(chr.getPlayer().getV("returnM")));
                                        chr.dropMessage(5, "<싸워라! 전설의 귀환> 게임 시작에 필요한 인원이 부족하여 게임이 종료 됩니다.");
                                    }
                                    return;
                                }
                                BattleGroundGameHandler.Ready();
                                continue;
                            }
                            if (((Integer) sci.getKey()).intValue() == 921174100) {
                                List<MapleBattleGroundCharacter> remove = new ArrayList<>();
                                Timer.EtcTimer.getInstance().schedule(() -> {
                                    List<MapleBattleGroundCharacter> list = new ArrayList<>();
                                    for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                        if (chr.getBattleGroundChr() != null) {
                                            MapleBattleGroundCharacter a = chr.getBattleGroundChr();
                                            if (a.isAlive()) {
                                                list.add(a);
                                            }
                                            remove.add(a);
                                        }
                                    }
                                    Collections.sort(list);
                                    int i = 1;
                                    for (MapleBattleGroundCharacter chrs : list) {
                                        chrs.getChr().addKV("BattlePVPRank", i + "");
                                        chrs.getChr().addKV("BattlePVPLevel", chrs.getLevel() + "");
                                        chrs.getChr().addKV("BattlePVPKill", chrs.getKill() + "");
                                        i++;
                                    }
                                    MapleMap.this.broadcastMessage(CField.UIPacket.detailShowInfo("게임이 종료 되었습니다 잠시 후 퇴장맵으로 이동 됩니다.", 3, 20, 20));
                                    MapleMap.this.broadcastMessage(CField.getClock(5));
                                }, 2000L);
                                Timer.EtcTimer.getInstance().schedule(() -> {
                                    MapleMap.this.resetFully();
                                    for (MapleCharacter chr : MapleMap.this.getAllChracater()) {
                                        chr.dispel();
                                        chr.warp(921174002);
                                    }
                                    MapleBattleGroundCharacter.bchr.clear();
                                }, 4000L);
                            }
                        }
                    }
                }
            }
        }
    }

    public void checkDropItems(long time) {
        if (getAllItemsThreadsafe().size() > 0) {
            Iterator<MapleMapItem> items = getAllItemsThreadsafe().iterator();
            while (items.hasNext()) {
                MapleMapItem item = items.next();
                if (item.shouldExpire(time)) {
                    item.expire(this);
                    continue;
                }
                if (item.shouldFFA(time)) {
                    item.setDropType((byte) 2);
                }
            }
        }
    }

    public final void setSpawns(boolean fm) {
        this.isSpawns = fm;
    }

    public final boolean getSpawns() {
        return this.isSpawns;
    }

    public final void setFixedMob(int fm) {
        this.fixedMob = fm;
    }

    public final void setForceMove(int fm) {
        this.lvForceMove = fm;
    }

    public final int getForceMove() {
        return this.lvForceMove;
    }

    public final void setLevelLimit(int fm) {
        this.lvLimit = fm;
    }

    public final int getLevelLimit() {
        return this.lvLimit;
    }

    public final void setReturnMapId(int rmi) {
        this.returnMapId = rmi;
    }

    public final void setSoaring(boolean b) {
        this.soaring = b;
    }

    public void MapiaMorning(final MapleCharacter player) {
        broadcastMessage(CField.getClock(this.aftertime));
        final java.util.Timer m_timer = new java.util.Timer();
        TimerTask m_task = new TimerTask() {
            public void run() {
                m_timer.cancel();
                MapleMap.this.MapiaVote(player);
            }
        };
        m_timer.schedule(m_task, (this.aftertime * 1000));
    }

    public void MapiaVote(final MapleCharacter player) {
        if (this.nightnumber == 0) {
            MapiaCompare(player);
        } else {
            broadcastMessage(CField.musicChange("Wps.img/VOTE"));
            broadcastMessage(CField.getClock(this.votetime));
            broadcastMessage(CWvsContext.serverNotice(5, "", "투표를 진행하시기 바랍니다. 제한시간은 30초 입니다."));
            this.names = "";
            for (MapleCharacter chr : getCharacters()) {
                this.names += chr.getName() + ",";
                chr.isVoting = true;
            }
            int i = 0;
            final java.util.Timer m_timer = new java.util.Timer();
            TimerTask m_task = new TimerTask() {
                public void run() {
                    m_timer.cancel();
                    MapleMap.this.MapiaCompare(player);
                }
            };
            m_timer.schedule(m_task, (this.votetime * 1000));
        }
    }

    public void MapiaComparable(final MapleCharacter player) {
        int playernum = 0;
        for (MapleCharacter chr : getCharacters()) {
            playernum++;
        }
        int i = 0;
        int ii = 0;
        int iii = 0;
        int citizen = 0;
        String deadname = "";
        String deadjob = "";
        String guessname = "";
        for (MapleCharacter chr : getCharacters()) {
            if (chr.getpolicevote == 1 && !chr.isDead
                    && chr.mapiajob == "마피아") {
                iii++;
            }
            if (chr.getmapiavote == 1 && !chr.isDead) {
                if (chr.getdrvote < 1 && !chr.isDead) {
                    chr.isDead = true;
                    deadname = chr.getName();
                    deadjob = chr.mapiajob;
                    chr.warp(910141020);
                    chr.dropMessage(1, "당신은 마피아에게 암살 당하였습니다.");
                    i++;
                } else {
                    chr.dropMessage(6, "의사가 당신을 살렸습니다.");
                    ii++;
                }
            }
            if (chr.mapiajob == "시민" && !chr.isDead) {
                citizen++;
            }
        }
        for (MapleCharacter chr : getCharacters()) {
            if (iii > 0) {
                chr.dropMessage(6, "경찰은 마피아를 찾았습니다.");
            } else {
                chr.dropMessage(5, "경찰은 마피아를 찾지 못하였습니다.");
            }
            if (i == 0) {
                if (ii > 0) {
                    chr.dropMessage(6, "의사는 마피아가 암살하려던 사람을 살렸습니다.");
                    continue;
                }
                chr.dropMessage(5, "마피아는 아무도 죽이지 못하였습니다.");
                continue;
            }
            chr.dropMessage(5, "의사는 아무도 살리지 못했습니다.");
            chr.dropMessage(5, "마피아는 " + deadname + "님을 죽였습니다. 그의 직업은 " + deadjob + " 이었습니다.");
        }
        if (citizen == 0) {
            final java.util.Timer m_timer = new java.util.Timer();
            TimerTask m_task = new TimerTask() {
                public void run() {
                    m_timer.cancel();
                    MapleMap.this.MapiaWin(player);
                }
            };
            m_timer.schedule(m_task, 15000L);
        } else {
            MapiaMorning(player);
        }
    }

    public void MapiaWin(MapleCharacter player) {
        long fuck = (ChannelServer.getInstance(player.getClient().getChannel()).getMapFactory().getMap(234567899)).mbating;
        int fuckingmapia = 0;
        (ChannelServer.getInstance(player.getClient().getChannel()).getMapFactory().getMap(234567899)).mbating = 0;
        this.MapiaIng = false;
        this.nightnumber = 0;
        if (this.MapiaChannel == 1) {
            int chan = 20;
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(8, "", "[마피아 알림] " + chan + "세이상 채널에서 마피아의 승리로 게임이 종료 되었습니다."));
        } else {
            int chan = this.MapiaChannel + 1;
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(8, "", "[마피아 알림] " + chan + "채널에서 마피아의 승리로 게임이 종료 되었습니다."));
        }
        int rand = Randomizer.rand(50, 100);
        for (MapleCharacter chr : getCharacters()) {
            if (chr.mapiajob.equals("마피아")) {
                fuckingmapia++;
            }
            chr.isDead = false;
            chr.isDrVote = false;
            chr.isMapiaVote = false;
            chr.isPoliceVote = false;
            chr.getdrvote = 0;
            chr.getmapiavote = 0;
            chr.getpolicevote = 0;
            chr.voteamount = 0;
            chr.dropMessage(5, "수고하셨습니다. 이번 게임은 마피아의 승리입니다!!");
        }
        for (MapleCharacter chr : getCharacters()) {
            if (chr.mapiajob.equals("마피아")) {
                chr.gainMeso(fuck / fuckingmapia, false);
                chr.dropMessage(6, "마피아 게임 승리 보상으로 " + (fuck / fuckingmapia) + "메소를 지급해드렸습니다.");
            }
            chr.warp(910141020);
        }
    }

    public void CitizenWin(MapleCharacter player) {
        long fuck = (ChannelServer.getInstance(player.getClient().getChannel()).getMapFactory().getMap(234567899)).mbating;
        int fucks = 0;
        (ChannelServer.getInstance(player.getClient().getChannel()).getMapFactory().getMap(234567899)).mbating = 0;
        this.MapiaIng = false;
        if (this.MapiaChannel == 1) {
            int chan = 20;
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(8, "", "[마피아 알림] " + chan + "세이상 채널에서 시민의 승리로 게임이 종료 되었습니다."));
        } else {
            int chan = this.MapiaChannel + 1;
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(8, "", "[마피아 알림] " + chan + " 채널에서 시민의 승리로 게임이 종료 되었습니다."));
        }
        int rand = Randomizer.rand(10, 80);
        int rand2 = Randomizer.rand(30, 100);
        for (MapleCharacter chr : getCharacters()) {
            if (!chr.mapiajob.equals("마피아")) {
                fuck += chr.mbating;
            }
            if (!chr.isDead) {
                fucks++;
            }
            chr.isDead = false;
            chr.isDrVote = false;
            chr.isMapiaVote = false;
            chr.isPoliceVote = false;
            chr.getdrvote = 0;
            chr.getmapiavote = 0;
            chr.getpolicevote = 0;
            chr.voteamount = 0;
            chr.dropMessage(5, "수고하셨습니다. 이번 게임은 시민의 승리입니다!!");
        }
        for (MapleCharacter chr : getCharacters()) {
            if (!chr.mapiajob.equals("마피아") && !chr.isDead) {
                chr.gainMeso(fuck / fucks, false);
                chr.dropMessage(6, "마피아 게임 승리 보상으로 " + (fuck / fucks) + "메소를 지급해드렸습니다.");
            }
            chr.warp(910141020);
        }
        this.nightnumber = 0;
    }

    public void MapiaCompare(MapleCharacter player) {
        int[] voteamount = new int[this.playern];
        String[] charinfo = new String[2];
        int j = 0;
        for (MapleCharacter chr : getCharacters()) {
            if (!chr.isDead) {
                voteamount[j] = chr.voteamount;
                j++;
            }
        }
        int mapia = 0;
        Arrays.sort(voteamount);
        try {
            for (MapleCharacter chr : getCharacters()) {
                if (chr.voteamount == voteamount[this.playern - 1]) {
                    charinfo[0] = chr.getName();
                    charinfo[1] = chr.mapiajob;
                }
            }
            if (voteamount[this.playern - 1] == voteamount[this.playern - 2]) {
                for (MapleCharacter chr : getCharacters()) {
                    if (this.nightnumber == 0) {
                        chr.dropMessage(6, "첫째날 낮이 지나고 밤이 찾아옵니다.");
                    } else {
                        chr.dropMessage(6, "투표 결과 아무도 죽지 않았습니다.");
                    }
                    chr.dropMessage(5, "잠시 후 밤이 됩니다.");
                }
                MapiaNight(player);
            } else {
                for (MapleCharacter chr : getCharacters()) {
                    if (charinfo[0] == chr.getName()) {
                        chr.dropMessage(1, "진행자>>당신은 투표 결과로 인해 처형당하였습니다.");
                        chr.isDead = true;
                    } else {
                        chr.dropMessage(6, "투표 결과 " + charinfo[0] + " 님이 처형당했습니다.");
                        chr.dropMessage(6, charinfo[0] + " 님의 직업은 " + charinfo[1] + " 입니다.");
                        chr.dropMessage(5, "잠시 후 밤이 됩니다.");
                    }
                    if (chr.mapiajob == "마피아" && !chr.isDead) {
                        mapia++;
                    }
                }
                if (mapia == 0) {
                    CitizenWin(player);
                } else {
                    MapiaNight(player);
                }
            }
        } catch (Exception e) {
            if (this.MapiaChannel == 1) {
                int chana = 20;
                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(8, "", "[마피아 알림] " + chana + "세이상 채널에서 게임이 다시 활성화 되었습니다."));
            } else {
                int chana = this.MapiaChannel + 1;
                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(8, "", "[마피아 알림] " + chana + " 채널에서 게임이 다시 활성화 되었습니다."));
            }
            this.MapiaIng = false;
            this.nightnumber = 0;
            for (MapleCharacter chr : getCharacters()) {
                chr.warp(ServerConstants.warpMap);
                chr.dropMessage(1, "오류 입니다. 운영자에게 문의 해 주세요.");
            }
            return;
        }
    }

    public void MapiaNight(final MapleCharacter player) {
        final int[] maps = {this.citizenmap1, this.citizenmap2, this.citizenmap3, this.citizenmap4, this.citizenmap5, this.citizenmap6};
        this.nightnumber++;
        final java.util.Timer m_timer = new java.util.Timer();
        final List<MapleCharacter> chars = new ArrayList<>();
        TimerTask m_task = new TimerTask() {
            int status = 0;

            public void run() {
                int citizen = 0;
                if (this.status == 0) {
                    MapleMap.this.names = "";
                    for (MapleCharacter chr : MapleMap.this.getCharacters()) {
                        if (!chr.isDead) {
                            chars.add(chr);
                            MapleMap.this.names += chr.getName() + ",";
                            chr.isDrVote = false;
                            chr.isMapiaVote = false;
                            chr.isPoliceVote = false;
                            chr.getdrvote = 0;
                            chr.getmapiavote = 0;
                            chr.getpolicevote = 0;
                            chr.voteamount = 0;
                            if (chr.mapiajob == "시민") {
                                chr.warp(maps[citizen]);
                                chr.dropMessage(5, MapleMap.this.nightnumber + "번째 밤이 되었습니다. 마피아, 경찰, 의사가 투표를 모두 할때까지 잠시만 기다려 주세요.");
                                citizen++;
                            } else if (chr.mapiajob == "마피아") {
                                chr.warp(MapleMap.this.mapiamap);
                                chr.isMapiaVote = true;
                                chr.dropMessage(5, MapleMap.this.nightnumber + "번째 밤이 되었습니다. 바로 옆의 엔피시를 통해 암살할 사람을 지목해 주세요. 제한시간은 " + MapleMap.this.nighttime + "초 입니다.");
                            } else if (chr.mapiajob == "경찰") {
                                chr.warp(MapleMap.this.policemap);
                                chr.isPoliceVote = true;
                                chr.dropMessage(5, MapleMap.this.nightnumber + "번째 밤이 되었습니다. 바로 옆의 엔피시를 통해 마피아 일것 같다는 사람을 지목 해 주세요. 제한시간은 " + MapleMap.this.nighttime + "초 입니다.");
                            } else if (chr.mapiajob == "의사") {
                                chr.warp(MapleMap.this.drmap);
                                chr.isDrVote = true;
                                chr.dropMessage(5, MapleMap.this.nightnumber + "번째 밤이 되었습니다. 바로 옆의 엔피시를 통해 살리고 싶은 사람을 지목 해 주세요. 제한시간은 " + MapleMap.this.nighttime + "초 입니다.");
                            }
                            chr.getClient().getSession().writeAndFlush(CField.getClock(MapleMap.this.nighttime));
                        }
                    }
                    this.status = 1;
                } else if (this.status == 1) {
                    for (MapleCharacter chr : chars) {
                        if (!chr.isDead) {
                            chr.isVoting = false;
                            chr.warp(MapleMap.this.morningmap);
                            chr.dropMessage(6, "아침이 되었습니다. 투표 결과를 발표하겠습니다.");
                        }
                    }
                    m_timer.cancel();
                    chars.clear();
                    MapleMap.this.MapiaComparable(player);
                }
            }
        };
        m_timer.schedule(m_task, 3000L, (this.nighttime * 1000));
    }

    public final boolean canSoar() {
        return this.soaring;
    }

    public final void toggleDrops() {
        this.dropsDisabled = !this.dropsDisabled;
    }

    public final void setDrops(boolean b) {
        this.dropsDisabled = b;
    }

    public final void toggleGDrops() {
        this.gDropsDisabled = !this.gDropsDisabled;
    }

    public final int getId() {
        return this.mapid;
    }

    public final MapleMap getReturnMap() {
        return ChannelServer.getInstance(this.channel).getMapFactory().getMap(this.returnMapId);
    }

    public final int getReturnMapId() {
        return this.returnMapId;
    }

    public boolean isBlackMage3thSkill() {
        return this.isBlackMage3thSkilled;
    }

    public void setBlackMage3thSkill(boolean f) {
        this.isBlackMage3thSkilled = f;
    }

    public final int getForcedReturnId() {
        return this.forcedReturnMap;
    }

    public final MapleMap getForcedReturnMap() {
        return ChannelServer.getInstance(this.channel).getMapFactory().getMap(this.forcedReturnMap);
    }

    public final void setForcedReturnMap(int map) {
        this.forcedReturnMap = map;
    }

    public final float getRecoveryRate() {
        return this.recoveryRate;
    }

    public final void setRecoveryRate(float recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public final int getFieldLimit() {
        return this.fieldLimit;
    }

    public final void setFieldLimit(int fieldLimit) {
        this.fieldLimit = fieldLimit;
    }

    public final String getFieldType() {
        return this.fieldType;
    }

    public final void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public final void setCreateMobInterval(short createMobInterval) {
        this.createMobInterval = createMobInterval;
    }

    public final void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public final void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public final String getMapName() {
        return this.mapName;
    }

    public final String getStreetName() {
        return this.streetName;
    }

    public final void setFirstUserEnter(String onFirstUserEnter) {
        this.onFirstUserEnter = onFirstUserEnter;
    }

    public final void setUserEnter(String onUserEnter) {
        this.onUserEnter = onUserEnter;
    }

    public final String getFirstUserEnter() {
        return this.onFirstUserEnter;
    }

    public final String getUserEnter() {
        return this.onUserEnter;
    }

    public final boolean hasClock() {
        return this.clock;
    }

    public final void setClock(boolean hasClock) {
        this.clock = hasClock;
    }

    public final boolean isTown() {
        return this.town;
    }

    public final boolean isLevelMob(MapleCharacter chr) {
        boolean already = false;
        for (MapleMonster monster : getAllMonster()) {
            if (chr.isGM()) {
                return true;
            }
            if (!monster.getStats().isBoss() && monster.getStats().getLevel() - 21 <= chr.getLevel() && chr.getLevel() <= monster.getStats().getLevel() + 21) {
                already = true;
                break;
            }
        }
        return already;
    }

    public final void setTown(boolean town) {
        this.town = town;
    }

    public final boolean allowPersonalShop() {
        return this.personalShop;
    }

    public final void setPersonalShop(boolean personalShop) {
        this.personalShop = personalShop;
    }

    public final void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public final void setEverlast(boolean everlast) {
        this.everlast = everlast;
    }

    public final boolean getEverlast() {
        return this.everlast;
    }

    public final int getHPDec() {
        return this.decHP;
    }

    public final void setHPDec(int delta) {
        if (delta > 0 || this.mapid == 749040100) {
            this.lastHurtTime = System.currentTimeMillis();
        }
        this.decHP = (short) delta;
    }

    public final int getHPDecInterval() {
        return this.decHPInterval;
    }

    public final void setHPDecInterval(int delta) {
        this.decHPInterval = delta;
    }

    public final int getHPDecProtect() {
        return this.protectItem;
    }

    public final void setHPDecProtect(int delta) {
        this.protectItem = delta;
    }

    public final int getCurrentPartyId() {
        Iterator<MapleCharacter> ltr = this.characters.iterator();
        while (ltr.hasNext()) {
            MapleCharacter chr = ltr.next();
            if (chr.getParty() != null) {
                return chr.getParty().getId();
            }
        }
        return -1;
    }

    public final void addMapObject(MapleMapObject mapobject) {
        int newOid;
        this.runningOidLock.lock();
        try {
            newOid = ++this.runningOid;
        } finally {
            this.runningOidLock.unlock();
        }
        mapobject.setObjectId(newOid);
        ((ConcurrentHashMap<Integer, MapleMapObject>) this.mapobjects.get(mapobject.getType())).put(Integer.valueOf(newOid), mapobject);
    }

    private void spawnAndAddRangedMapObject(MapleMapObject mapobject, DelayedPacketCreation packetbakery) {
        addMapObject(mapobject);
        Iterator<MapleCharacter> itr = this.characters.iterator();
        while (itr.hasNext()) {
            MapleCharacter chr = itr.next();
            if (mapobject.getType() == MapleMapObjectType.MIST || chr.getTruePosition().distanceSq(mapobject.getTruePosition()) <= GameConstants.maxViewRangeSq()) {
                if (mapobject.getType() == MapleMapObjectType.MONSTER) {
                    MapleMonster mob = (MapleMonster) mapobject;
                    if (mob.getOwner() != -1) {
                        if (mob.getOwner() == chr.getId()) {
                            packetbakery.sendPackets(chr.getClient());
                        }
                    } else {
                        packetbakery.sendPackets(chr.getClient());
                    }
                } else {
                    packetbakery.sendPackets(chr.getClient());
                }
                chr.addVisibleMapObject(mapobject);
            }
        }
    }

    public final void removeMapObject(MapleMapObject obj) {
        ((ConcurrentHashMap) this.mapobjects.get(obj.getType())).remove(Integer.valueOf(obj.getObjectId()));
    }

    public final Point calcPointBelow(Point initial) {
        MapleFoothold fh = this.footholds.findBelow(initial);
        if (fh == null) {
            return null;
        }
        int dropY = fh.getY1();
        if (!fh.isWall() && fh.getY1() != fh.getY2()) {
            double s1 = Math.abs(fh.getY2() - fh.getY1());
            double s2 = Math.abs(fh.getX2() - fh.getX1());
            if (fh.getY2() < fh.getY1()) {
                dropY = fh.getY1() - (int) (Math.cos(Math.atan(s2 / s1)) * Math.abs(initial.x - fh.getX1()) / Math.cos(Math.atan(s1 / s2)));
            } else {
                dropY = fh.getY1() + (int) (Math.cos(Math.atan(s2 / s1)) * Math.abs(initial.x - fh.getX1()) / Math.cos(Math.atan(s1 / s2)));
            }
        }
        if (initial.x < getLeft()) {
            initial.x = getLeft() + 100;
        } else if (initial.x > getRight()) {
            initial.x = getRight() - 100;
        }
        return new Point(initial.x, dropY);
    }

    public final Point calcDropPos(Point initial, Point fallback) {
        Point ret = calcPointBelow(new Point(initial.x, initial.y - 50));
        if (ret == null) {
            return fallback;
        }
        return ret;
    }

    private void dropFromMonster(MapleCharacter chr, MapleMonster mob, boolean instanced) {
        if (mob == null || chr == null || ChannelServer.getInstance(this.channel) == null) {
            return;
        }
        byte d = 1;
        Point pos = new Point(0, (mob.getTruePosition()).y);
        double showdown = 100.0D;
        MonsterStatusEffect mse = mob.getBuff(MonsterStatus.MS_Showdown);
        if (mse != null) {
            showdown += mse.getValue();
        }
        MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();
        List<MonsterDropEntry> derp = new ArrayList<>();
        derp.addAll(mi.retrieveDrop(mob.getId()));
        List<MonsterDropEntry> customs = new ArrayList<>();
        if (mob.getId() == 9390612 || mob.getId() == 9390610 || mob.getId() == 9390611 || mob.getId() == 8645066) {
            MapleCharacter pchr = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getParty().getLeader().getId());
            if (pchr == null) {
                chr.dropMessage(5, "파티의 상태가 변경되어 골럭스 원정대가 해체됩니다.");
                chr.setKeyValue(200106, "golrux_in", "0");
            } else {
                switch ((int) pchr.getKeyValue(200106, "golrux_diff")) {
                    case 1:
                        customs.add(new MonsterDropEntry(4000000, 499999, 1, 5, 0));
                        customs.add(new MonsterDropEntry(4000001, 499999, 2, 6, 0));
                        customs.add(new MonsterDropEntry(4000002, 499999, 3, 7, 0));
                        break;
                    case 2:
                        customs.add(new MonsterDropEntry(4000000, 499999, 1, 5, 0));
                        customs.add(new MonsterDropEntry(4000001, 499999, 2, 6, 0));
                        customs.add(new MonsterDropEntry(4000002, 499999, 3, 7, 0));
                        break;
                    case 3:
                        customs.add(new MonsterDropEntry(4000000, 499999, 1, 5, 0));
                        customs.add(new MonsterDropEntry(4000001, 499999, 2, 6, 0));
                        customs.add(new MonsterDropEntry(4000002, 499999, 3, 7, 0));
                        break;
                    case 4:
                        customs.add(new MonsterDropEntry(4000000, 499999, 1, 5, 0));
                        customs.add(new MonsterDropEntry(4000001, 499999, 2, 6, 0));
                        customs.add(new MonsterDropEntry(4000002, 499999, 3, 7, 0));
                        break;
                }
            }
        }
        if (mob.isEliteboss()) {
            customs.add(new MonsterDropEntry(2432398, 1000000, 1, 15, 0));
        }
        if (mob.isElitemonster() && !mob.isUserunespawn()) {
            customs.add(new MonsterDropEntry(5062009, 1000000, 1, 15, 0));
            customs.add(new MonsterDropEntry(5062010, 1000000, 1, 5, 0));
            customs.add(new MonsterDropEntry(5062500, 1000000, 1, 5, 0));
            customs.add(new MonsterDropEntry(4001832, 500000, 10, 100, 0));
            customs.add(new MonsterDropEntry(2435719, 250000, 1, 2, 0));
            customs.add(new MonsterDropEntry(4021031, 100000, 1, 10, 0));
            customs.add(new MonsterDropEntry(4310012, 100000, 5, 30, 0));
        }
        if (mob.getCustomValue0(1) == 1L) {
            customs.add(new MonsterDropEntry(5062009, 1000000, 10, 30, 0));
            customs.add(new MonsterDropEntry(5062010, 1000000, 10, 30, 0));
            customs.add(new MonsterDropEntry(5062500, 1000000, 10, 30, 0));
            customs.add(new MonsterDropEntry(5062503, 500000, 5, 10, 0));
            customs.add(new MonsterDropEntry(5069000, 250000, 1, 1, 0));
            customs.add(new MonsterDropEntry(5069001, 100000, 1, 1, 0));
            customs.add(new MonsterDropEntry(4310012, 100000, 50, 100, 0));
            customs.add(new MonsterDropEntry(4001832, 1000000, 50, 300, 0));
            customs.add(new MonsterDropEntry(4021031, 1000000, 5, 30, 0));
            customs.add(new MonsterDropEntry(4310016, 1000000, 1, 1, 0));
            customs.add(new MonsterDropEntry(5064400, 500000, 1, 1, 0));
            customs.add(new MonsterDropEntry(4310005, 250000, 1, 3, 0));
            customs.add(new MonsterDropEntry(2049752, 100000, 1, 1, 0));
            customs.add(new MonsterDropEntry(2048716, 100000, 1, 5, 0));
            customs.add(new MonsterDropEntry(2048717, 250000, 1, 5, 0));
            customs.add(new MonsterDropEntry(2048753, 100000, 1, 2, 0));
        }
        if (chr.getBuffedValue(80003046)) {
            customs.add(new MonsterDropEntry(2633343, chr.isGM() ? 9999999 : 50000, 1, 1, 0));
        }
        
        /*  838 */     switch (mob.getId()) {
/*      */       case 9300452:
/*      */       case 9300453:
/*  841 */         customs.add(new MonsterDropEntry(4001528, 50000, 1, 1, 0));
/*      */         break;
/*      */       case 9300173:
/*  844 */         customs.add(new MonsterDropEntry(4001161, 50000, 1, 1, 0));
/*      */         break;
/*      */       case 9300175:
/*  847 */         customs.add(new MonsterDropEntry(4001169, 10000000, 1, 1, 0));
/*      */         break;
/*      */     } 
        
        if (chr.getV("arcane_quest_2") != null && Integer.parseInt(chr.getV("arcane_quest_2")) >= 0 && Integer.parseInt(chr.getV("arcane_quest_2")) < 6) {
            switch (mob.getId()) {
                case 8641000:
                    customs.add(new MonsterDropEntry(4034922, 1000000, 1, 1, 0));
                    break;
                case 8641001:
                    customs.add(new MonsterDropEntry(4034923, 1000000, 1, 1, 0));
                    break;
                case 8641002:
                    customs.add(new MonsterDropEntry(4034924, 1000000, 1, 1, 0));
                    break;
                case 8641003:
                    customs.add(new MonsterDropEntry(4034925, 1000000, 1, 1, 0));
                    break;
                case 8641004:
                    customs.add(new MonsterDropEntry(4034926, 1000000, 1, 1, 0));
                    break;
                case 8641005:
                    customs.add(new MonsterDropEntry(4034927, 1000000, 1, 1, 0));
                    break;
                case 8641006:
                    customs.add(new MonsterDropEntry(4034928, 1000000, 1, 1, 0));
                    break;
                case 8641007:
                    customs.add(new MonsterDropEntry(4034929, 1000000, 1, 1, 0));
                    break;
                case 8641008:
                    customs.add(new MonsterDropEntry(4034930, 1000000, 1, 1, 0));
                    break;
            }
        }
        if (chr.getV("arcane_quest_3") != null && Integer.parseInt(chr.getV("arcane_quest_3")) >= 0 && Integer.parseInt(chr.getV("arcane_quest_3")) < 4) {
            switch (mob.getId()) {
                case 8642000:
                case 8642001:
                case 8642002:
                case 8642003:
                case 8642004:
                case 8642005:
                case 8642006:
                case 8642007:
                case 8642008:
                case 8642009:
                case 8642010:
                case 8642011:
                case 8642012:
                case 8642013:
                case 8642014:
                case 8642015:
                    customs.add(new MonsterDropEntry(4036571, 1000000, 1, 1, 0));
                    break;
            }
        }
        if (chr.getV("arcane_quest_4") != null && Integer.parseInt(chr.getV("arcane_quest_4")) >= 0 && Integer.parseInt(chr.getV("arcane_quest_4")) < 4) {
            switch (mob.getId()) {
                case 8643000:
                case 8643001:
                case 8643002:
                case 8643003:
                case 8643004:
                case 8643005:
                case 8643006:
                case 8643007:
                case 8643008:
                case 8643009:
                case 8643010:
                case 8643011:
                case 8643012:
                    customs.add(new MonsterDropEntry(4036572, 1000000, 1, 1, 0));
                    break;
            }
        }
        if (chr.getV("arcane_quest_5") != null && Integer.parseInt(chr.getV("arcane_quest_5")) >= 0 && Integer.parseInt(chr.getV("arcane_quest_5")) < 4) {
            switch (mob.getId()) {
                case 8644000:
                case 8644001:
                case 8644002:
                case 8644003:
                case 8644004:
                case 8644005:
                case 8644006:
                    customs.add(new MonsterDropEntry(4036573, 1000000, 1, 1, 0));
                    break;
                case 8644007:
                case 8644008:
                case 8644009:
                case 8644010:
                    customs.add(new MonsterDropEntry(4036574, 1000000, 1, 1, 0));
                    break;
            }
        }
        if (chr.getV("arcane_quest_6") != null && Integer.parseInt(chr.getV("arcane_quest_6")) >= 0 && Integer.parseInt(chr.getV("arcane_quest_6")) < 4) {
            switch (mob.getId()) {
                case 8644400:
                    customs.add(new MonsterDropEntry(4036333, 1000000, 1, 1, 0));
                    break;
                case 8644401:
                    customs.add(new MonsterDropEntry(4036333, 1000000, 1, 1, 0));
                    break;
                case 8644402:
                    customs.add(new MonsterDropEntry(4036329, 1000000, 1, 1, 0));
                    customs.add(new MonsterDropEntry(4036330, 1000000, 1, 1, 0));
                    break;
                case 8644403:
                    customs.add(new MonsterDropEntry(4036330, 1000000, 1, 1, 0));
                    customs.add(new MonsterDropEntry(4036331, 1000000, 1, 1, 0));
                    break;
                case 8644404:
                    customs.add(new MonsterDropEntry(4036330, 1000000, 1, 1, 0));
                    break;
                case 8644405:
                    customs.add(new MonsterDropEntry(4036332, 1000000, 1, 1, 0));
                    break;
                case 8644406:
                    customs.add(new MonsterDropEntry(4036332, 1000000, 1, 1, 0));
                    break;
                case 8644407:
                    customs.add(new MonsterDropEntry(4036334, 1000000, 1, 1, 0));
                    break;
                case 8644410:
                    customs.add(new MonsterDropEntry(4036335, 1000000, 1, 1, 0));
                    break;
                case 8644412:
                    customs.add(new MonsterDropEntry(4036336, 1000000, 1, 1, 0));
                    break;
            }
        }
        if (chr.getV("arcane_quest_7") != null && Integer.parseInt(chr.getV("arcane_quest_7")) >= 0 && Integer.parseInt(chr.getV("arcane_quest_7")) < 4) {
            switch (mob.getId()) {
                case 8644500:
                    customs.add(new MonsterDropEntry(4036398, 1000000, 1, 1, 0));
                    break;
                case 8644501:
                    customs.add(new MonsterDropEntry(4036399, 1000000, 1, 1, 0));
                    break;
                case 8644502:
                    customs.add(new MonsterDropEntry(4036400, 1000000, 1, 1, 0));
                    break;
                case 8644503:
                    customs.add(new MonsterDropEntry(4036401, 1000000, 1, 1, 0));
                    break;
                case 8644504:
                    customs.add(new MonsterDropEntry(4036402, 1000000, 1, 1, 0));
                    break;
                case 8644505:
                    customs.add(new MonsterDropEntry(4036403, 1000000, 1, 1, 0));
                    break;
                case 8644506:
                    customs.add(new MonsterDropEntry(4036404, 1000000, 1, 1, 0));
                    break;
                case 8644507:
                    customs.add(new MonsterDropEntry(4036405, 1000000, 1, 1, 0));
                    break;
                case 8644508:
                    customs.add(new MonsterDropEntry(4036406, 1000000, 1, 1, 0));
                    break;
                case 8644509:
                    customs.add(new MonsterDropEntry(4036407, 1000000, 1, 1, 0));
                    break;
                case 8644510:
                    customs.add(new MonsterDropEntry(4036406, 1000000, 1, 1, 0));
                    break;
                case 8644511:
                    customs.add(new MonsterDropEntry(4036407, 1000000, 1, 1, 0));
                    break;
            }
        }
        int[] items = {
            1004422, 1004423, 1004424, 1004425, 1004426, 1052882, 1052887, 1052888, 1052889, 1052890,
            1073030, 1073035, 1073032, 1073033, 1073034, 1082636, 1082637, 1082638, 1082639, 1082640,
            1102775, 1102794, 1102795, 1102796, 1102797, 1152174, 1152179, 1152176, 1152177, 1152178,
            1212115, 1213017, 1222109, 1232109, 1242116, 1242120, 1262017, 1272016, 1282016, 1292017,
            1302333, 1312199, 1322250, 1332274, 1342101, 1362135, 1372222, 1382259, 1402251, 1412177,
            1422184, 1432214, 1442268, 1452252, 1462239, 1472261, 1482216, 1492231, 1522138, 1532144,
            1582017, 1592019};
        int[] items2 = {
            1004808, 1004809, 1004810, 1004811, 1004812, 1053063, 1053064, 1053065, 1053066, 1053067,
            1073158, 1073159, 1073160, 1073161, 1073162, 1082695, 1082696, 1082697, 1082698, 1082699,
            1102940, 1102941, 1102942, 1102943, 1102944, 1152196, 1152197, 1152198, 1152199, 1152200,
            1212120, 1213018, 1222113, 1232113, 1242121, 1242122, 1262039, 1272017, 1282017, 1292018,
            1302343, 1312203, 1322255, 1332279, 1342104, 1362140, 1372228, 1382265, 1402259, 1412181,
            1422189, 1432218, 1442274, 1452257, 1462243, 1472265, 1482221, 1492235, 1522143, 1532150,
            1582023, 1592020};
        if (mob.isExtreme()) {
            switch (mob.getId()) {
                case 8880177:
                case 8880302:
                    for (int item : items2) {
                        customs.add(new MonsterDropEntry(item, 125, 1, 1, 0));
                    }
                case 8880101:
                case 8950002:
                    for (int item : items) {
                        customs.add(new MonsterDropEntry(item, 250, 1, 1, 0));
                    }
                    break;
            }
        }
        for (Pair<Integer, Integer> list : ServerConstants.NeoPosList) {
            if (((Integer) list.getLeft()).intValue() == mob.getId()) {
                if (ServerConstants.Event_Blooming) {
                    customs.add(new MonsterDropEntry(2633304, 9999999, ((Integer) list.getRight()).intValue(), ((Integer) list.getRight()).intValue(), 0, 0));
                    break;
                }
                if (ServerConstants.Event_MapleLive) {
                    customs.add(new MonsterDropEntry(2633609, 9999999, ((Integer) list.getRight()).intValue(), ((Integer) list.getRight()).intValue(), 0, 0));
                }
                break;
            }
        }
        List<MonsterDropEntry> finals = new ArrayList<>();
        List<MonsterDropEntry> realfinals = new ArrayList<>();
        finals.addAll(derp);
        finals.addAll(customs);
        if (GameConstants.isContentsMap(getId()) || chr.getMapId() / 100000 == 9530 || chr.getMapId() / 100000 == 9540 || (mob.getId() >= 9833935 && mob.getId() <= 9833946) || (mob.getId() >= 9833947 && mob.getId() <= 9833958)) {
            finals.clear();
        }
        double dropBuff = (chr.getStat()).dropBuff;
        if (mob.getStats().isBoss()) {
            dropBuff = Math.min(dropBuff, 300.0D);
        } else {
            dropBuff = Math.min(dropBuff, 200.0D);
        }
        if (Calendar.getInstance().get(7) == 7) {
            dropBuff += 15.0D;
        }
        if (chr.getSkillLevel(80001536) > 0) {
            dropBuff += 20.0D;
        }
        for (MonsterDropEntry de : finals) {
            double d1;
            if (de.itemId == mob.getStolen()) {
                continue;
            }
            if (GameConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP && mob.isExtreme()) {
                d1 = (de.chance < 250) ? de.chance : (250.0D * dropBuff / 100.0D * showdown / 100.0D);
            } else {
                d1 = de.chance * dropBuff / 100.0D * showdown / 100.0D;
            }
            if (chr.getParty() != null) {
                for (MaplePartyCharacter pc : chr.getParty().getMembers()) {
                    if (pc.isOnline() && pc.getId() != chr.getId()) {
                        MapleCharacter player = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(pc.getId());
                        if (player != null) {
                            if (GameConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP && mob.isExtreme()) {
                                double d2 = (de.chance < 250) ? de.chance : (250.0D * dropBuff / 100.0D * showdown / 100.0D);
                                continue;
                            }
                            d1 = de.chance * dropBuff / 100.0D * showdown / 100.0D;
                        }
                    }
                }
            }
            if (GameConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP && mob.isExtreme()) {
                d1 = (de.chance < 250) ? de.chance : (250.0D * dropBuff / 100.0D * showdown / 100.0D);
            } else {
                d1 = de.chance * dropBuff / 100.0D * showdown / 100.0D;
            }
            if (chr.getKeyValue(210416, "TotalDeadTime") > 0L) {
                d1 = (long) (d1 * 0.2D);
            }
            if (Randomizer.nextInt(999999) < (int) d1) {
                realfinals.add(de);
            }
        }
        if (realfinals != null && !realfinals.isEmpty()) {
            Collections.shuffle(realfinals);
            boolean mesoDropped = false;
            for (MonsterDropEntry de : realfinals) {
                if (de.itemId == mob.getStolen()) {
                    continue;
                }
                if (de.privated == 0 && (mob.getStats().isBoss() || de.itemId == 4001847 || de.itemId == 4001849 || de.itemId == 2434851)) {
                    if (chr.getParty() != null) {
                        for (MaplePartyCharacter pc : chr.getParty().getMembers()) {
                            if (pc.isOnline() && pc.getId() != chr.getId()) {
                                MapleCharacter player = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(pc.getId());
                                if (player != null) {
                                    drop(mesoDropped, de, mob, player, pos, d, realfinals.size());
                                }
                            }
                        }
                    }
                    drop(mesoDropped, de, mob, chr, pos, d, realfinals.size());
                    d = (byte) (d + 1);
                    continue;
                }
                drop(mesoDropped, de, mob, chr, pos, d, realfinals.size());
                d = (byte) (d + 1);
            }
        }
    }

    public void drop(boolean mesoDropped, MonsterDropEntry de, MapleMonster mob, MapleCharacter chr, Point pos, byte d) {
        drop(mesoDropped, de, mob, chr, pos, d, 0);
    }

    public void drop(boolean mesoDropped, MonsterDropEntry de, MapleMonster mob, MapleCharacter chr, Point pos, byte d, int total) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        byte droptype = (byte) ((mob.getStats().isExplosiveReward() && mob.getStats().isBoss()) ? 3 : ((chr.getParty() != null) ? 1 : 0));
        int mobpos = (mob.getTruePosition()).x, cmServerrate = ChannelServer.getInstance(this.channel).getMesoRate();
        if (GameConstants.isYeti(chr.getJob()) || GameConstants.isPinkBean(chr.getJob())) {
            cmServerrate = 1;
        }
        if (mesoDropped && droptype != 3 && de.itemId == 0) {
            return;
        }
        if (de.questid > 0 && chr.getQuestStatus(de.questid) != 1) {
            return;
        }
        if (de.itemId == 2633304) {
            if (Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) >= 400) {
                return;
            }
            String[] wishCoinCheck = chr.getClient().getKeyValue("WishCoin").split("");
            int i = 0;
            for (Pair<Integer, Integer> list : ServerConstants.NeoPosList) {
                if (((Integer) list.getLeft()).intValue() == mob.getId()
                        && Integer.parseInt(wishCoinCheck[i]) == 1) {
                    return;
                }
                i++;
            }
        }
        if (de.itemId == 2633609) {
            if (chr.getClient().getCustomKeyValueStr(501468, "reward") == null) {
                String bosslist = "";
                for (int j = 0; j < ServerConstants.NeoPosList.size(); j++) {
                    bosslist = bosslist + "0";
                }
                chr.getClient().setCustomKeyValue(501468, "reward", bosslist);
            }
            String[] wishCoinCheck = chr.getClient().getCustomKeyValueStr(501468, "reward").split("");
            int i = 0;
            for (Pair<Integer, Integer> list : ServerConstants.NeoPosList) {
                if (((Integer) list.getLeft()).intValue() == mob.getId()
                        && Integer.parseInt(wishCoinCheck[i]) == 1) {
                    return;
                }
                i++;
            }
        }
        Point poss = new Point((mob.getTruePosition()).x, (mob.getTruePosition()).y);
        if (total > 5) {
            int a = total / 2;
            if (d < a) {
                poss.x -= 30 * (a - d);
            } else if (d >= a) {
                poss.x += 30 * (d - a);
            }
            pos = poss;
        } else if (droptype == 3) {
            pos.x = mobpos + ((d % 2 == 0) ? (40 * (d + 1) / 2) : -(40 * d / 2));
        } else {
            pos.x = mobpos + ((d % 2 == 0) ? (20 * (d + 1) / 2) : -(20 * d / 2));
        }
        MapleFoothold fh2 = this.footholds.findBelow(pos);
        if (fh2 != null) {
            if (fh2.getX1() > pos.x) {
                pos.x = fh2.getX1();
            } else if (fh2.getX2() < pos.x) {
                pos.x = fh2.getX2();
            }
        }
        if (de.itemId == 0) {
            int mesos = Randomizer.nextInt(1 + Math.abs(de.Maximum - de.Minimum)) + de.Minimum;
            if (mesos > 0) {
                if (GameConstants.isLinkMap(chr.getMapId())) {
                    chr.gainMeso(mesos, true);
                } else {
                    double mesobuff = Math.min((chr.getStat()).mesoBuff, 300.0D);
                    if (Calendar.getInstance().get(7) == 1) {
                        mesobuff += 15.0D;
                    }
                    if (chr.getSkillLevel(80001535) > 0) {
                        mesobuff += 20.0D;
                    }
                    spawnMobMesoDrop((int) (mesos * mesobuff / 100.0D * chr.getDropMod() * cmServerrate), calcDropPos(pos, mob.getTruePosition()), mob, chr, false, droptype);
                }
                mesoDropped = true;
            }
        } else if (!GameConstants.isLinkMap(chr.getMapId())) {
            Item idrop;
            if (GameConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP) {
                idrop = ii.getEquipById(de.itemId);
                if (mob.isExtreme()) {
                    int[] items = {
                        1004422, 1004423, 1004424, 1004425, 1004426, 1052882, 1052887, 1052888, 1052889, 1052890,
                        1073030, 1073035, 1073032, 1073033, 1073034, 1082636, 1082637, 1082638, 1082639, 1082640,
                        1102775, 1102794, 1102795, 1102796, 1102797, 1152174, 1152179, 1152176, 1152177, 1152178,
                        1212115, 1213017, 1222109, 1232109, 1242116, 1242120, 1262017, 1272016, 1282016, 1292017,
                        1302333, 1312199, 1322250, 1332274, 1342101, 1362135, 1372222, 1382259, 1402251, 1412177,
                        1422184, 1432214, 1442268, 1452252, 1462239, 1472261, 1482216, 1492231, 1522138, 1532144,
                        1582017, 1592019};
                    int[] items2 = {
                        1004808, 1004809, 1004810, 1004811, 1004812, 1053063, 1053064, 1053065, 1053066, 1053067,
                        1073158, 1073159, 1073160, 1073161, 1073162, 1082695, 1082696, 1082697, 1082698, 1082699,
                        1102940, 1102941, 1102942, 1102943, 1102944, 1152196, 1152197, 1152198, 1152199, 1152200,
                        1212120, 1213018, 1222113, 1232113, 1242121, 1242122, 1262039, 1272017, 1282017, 1292018,
                        1302343, 1312203, 1322255, 1332279, 1342104, 1362140, 1372228, 1382265, 1402259, 1412181,
                        1422189, 1432218, 1442274, 1452257, 1462243, 1472265, 1482221, 1492235, 1522143, 1532150,
                        1582023, 1592020};
                    for (int item : items) {
                        if (item == idrop.getItemId()) {
                            Equip equip = (Equip) idrop;
                            equip.addTotalDamage((byte) 10);
                        }
                    }
                    for (int item : items2) {
                        if (item == idrop.getItemId()) {
                            Equip equip = (Equip) idrop;
                            equip.addTotalDamage((byte) 10);
                        }
                    }
                    switch (mob.getId()) {
                        case 8950002:
                            if (idrop.getItemId() == 1012632) {
                                Equip equip = (Equip) idrop;
                                equip.addTotalDamage((byte) 10);
                            }
                            break;
                        case 8880101:
                            if (idrop.getItemId() == 1022278 || idrop.getItemId() == 1672077) {
                                Equip equip = (Equip) idrop;
                                equip.addTotalDamage((byte) 10);
                            }
                            break;
                        case 8880177:
                            if (idrop.getItemId() == 1132308) {
                                Equip equip = (Equip) idrop;
                                equip.addTotalDamage((byte) 10);
                            }
                            break;
                        case 8880302:
                            if (idrop.getItemId() == 1162080 || idrop.getItemId() == 1162081 || idrop.getItemId() == 1162082 || idrop.getItemId() == 1162083) {
                                Equip equip = (Equip) idrop;
                                equip.addTotalDamage((byte) 15);
                            }
                            break;
                    }
                }
            } else {
                int range = Math.abs(de.Maximum - de.Minimum);
                idrop = new Item(de.itemId, (short) 0, (short) ((de.Maximum != 1) ? (Randomizer.nextInt((range <= 0) ? 1 : range) + de.Minimum) : 1), 0);
                if (mob.isExtreme()) {
                    switch (mob.getId()) {
                        case 8880101:
                        case 8880177:
                        case 8880302:
                        case 8950002:
                            idrop.setQuantity((short) (idrop.getQuantity() * 2));
                            break;
                    }
                }
            }
            idrop.setGMLog(StringUtil.getAllCurrentTime() + "에 " + this.mapid + "맵에서 " + mob.getId() + "를 잡고 얻은 아이템.");
            if ((mob.getId() == 8820212 || mob.getId() == 8880302 || mob.getId() == 8880342 || mob.getId() == 8880150 || mob.getId() == 8880151 || mob.getId() == 8880155 || mob.getId() == 8644650 || mob.getId() == 8644655 || mob.getId() == 8880405 || mob.getId() == 8645009 || mob.getId() == 8645039 || mob.getId() == 8880504 || mob.getId() == 8880602 || mob
                    .getId() == 8880614 || mob.getId() == 8820101 || mob.getId() == 8820001 || mob.getId() == 8860005 || mob.getId() == 8860000 || mob.getId() == 8810018 || mob.getId() == 8810122 || mob.getId() == 8840007 || mob.getId() == 8840000 || mob.getId() == 8840014 || mob.getId() == 8800102 || mob.getId() == 8500012 || mob.getId() == 8500002 || mob.getId() == 8880200 || mob.getId() == 8870000 || mob.getId() == 8800002 || mob.getId() == 8500022 || mob.getId() == 8870100 || mob.getId() == 8910100 || mob.getId() == 8930100 || mob.getId() == 8910000 || mob.getId() == 8930000 || mob.getId() == 8850011 || mob.getId() == 8850111 || mob.getId() == 8950102 || mob.getId() == 8950002 || mob.getId() == 8880111 || mob.getId() == 8880101 || (mob
                    .getId() >= 8900100 && mob.getId() <= 8900102) || (mob.getId() >= 8900000 && mob.getId() <= 8900002) || (mob
                    .getId() >= 8920000 && mob.getId() <= 8920003) || (mob.getId() >= 8920100 && mob.getId() <= 8920103) || mob
                    .getId() == 8920106 || mob.getId() == 8900103 || mob.getId() == 8880157 || mob.getId() == 8880167 || mob.getId() == 8880177 || mob.getId() == 8880614)
                    && chr.getPlayer() != null) {
                FileoutputUtil.log(FileoutputUtil.보스획득아이템, "[획득] 계정 번호 : " + chr.getClient().getAccID() + " | 파티번호 : " + chr.getPlayer().getParty().getId() + " | 캐릭터 : " + chr.getName() + "(" + chr.getId() + ") | 보스 클리어 " + MapleLifeFactory.getMonster(mob.getId()).getStats().getName() + "(" + mob.getId() + ") | 드롭 : " + MapleItemInformationProvider.getInstance().getName(idrop.getItemId()) + "(" + idrop.getItemId() + ")를 [" + idrop.getQuantity() + "]개 획득");
            }
            if (idrop.getItemId() == 4001886) {
                idrop.setBossid(mob.getId());
            }
            if (de.privated == 0 && (mob.getStats().isBoss() || idrop.getItemId() == 4001847 || idrop.getItemId() == 4001849 || idrop.getItemId() == 2434851 || mob.getScale() > 100)) {
                spawnMobPublicDrop(idrop, calcDropPos(pos, mob.getTruePosition()), mob, chr, droptype, de.questid);
            } else {
                spawnMobDrop(idrop, calcDropPos(pos, mob.getTruePosition()), mob, chr, droptype, de.questid);
            }
        }
    }

    public void removeMonster(MapleMonster monster) {
        if (monster == null) {
            return;
        }
        if (this.RealSpawns.contains(monster)) {
            this.RealSpawns.remove(monster);
        }
        broadcastMessage(MobPacket.killMonster(monster.getObjectId(), 0));
        removeMapObject(monster);
        this.spawnedMonstersOnMap.decrementAndGet();
        monster.killed();
    }

    public void killMonster(MapleMonster monster, int effect) {
        if (monster == null) {
            return;
        }
        monster.setHp(0L);
        if (monster.getLinkCID() <= 0 && !GameConstants.isContentsMap(getId())) {
            monster.spawnRevives(this);
        }
        if (this.RealSpawns.contains(monster)) {
            this.RealSpawns.remove(monster);
        }
        broadcastMessage(MobPacket.killMonster(monster.getObjectId(), effect));
        removeMapObject(monster);
        this.spawnedMonstersOnMap.decrementAndGet();
        monster.killed();
    }

    public void killMonster(MapleMonster monster) {
        if (monster == null) {
            return;
        }
        monster.setHp(0L);
        if (this.RealSpawns.contains(monster)) {
            this.RealSpawns.remove(monster);
        }
        if (monster.getLinkCID() <= 0 && !GameConstants.isContentsMap(getId())) {
            monster.spawnRevives(this);
        }
        broadcastMessage(MobPacket.killMonster(monster.getObjectId(), (monster.getStats().getSelfD() < 0) ? 1 : monster.getStats().getSelfD()));
        removeMapObject(monster);
        this.spawnedMonstersOnMap.decrementAndGet();
        monster.killed();
    }

    public void killAllMonster(MapleCharacter chr) {
        for (MapleMonster mob : getAllMonstersThreadsafe()) {
            killMonster(mob, chr, false, false, (byte) 0);
        }
    }

    public final void killMonster(MapleMonster monster, MapleCharacter chr, boolean withDrops, boolean second, byte animation) {
        killMonster(monster, chr, withDrops, second, animation, 0);
    }

    public final void killMonster(final MapleMonster monster, final MapleCharacter chr, boolean withDrops, boolean second, byte animation, int lastSkill) {
        boolean norespawn = false;
        if (chr.getBattleGroundChr() != null) {
            int point = 0, exp = 0;
            if (monster.getStats().getName().contains("버섯")) {
                point = 30;
                exp = 25;
                norespawn = true;
            } else if (monster.getStats().getName().contains("돼지")) {
                point = 60;
                exp = 38;
                norespawn = true;
            } else if (monster.getStats().getName().contains("이블아이")) {
                point = 75;
                exp = 45;
                norespawn = true;
            } else if (monster.getStats().getName().contains("도라지")) {
                point = 100;
                exp = 80;
                norespawn = true;
            } else if (monster.getStats().getName().contains("천록")) {
                point = 140;
                exp = 120;
                norespawn = true;
            } else if (monster.getStats().getName().contains("크로노스")) {
                point = 200;
                exp = 170;
                norespawn = true;
            } else if (monster.getStats().getName().contains("파이렛")) {
                point = 260;
                exp = 220;
                norespawn = true;
            } else if (monster.getStats().getName().contains("요괴선사")) {
                point = 276;
                exp = 142;
                norespawn = true;
            } else if (monster.getStats().getName().contains("데스테니")) {
                point = 698;
                exp = 340;
                norespawn = true;
            } else if (monster.getStats().getName().contains("드래곤킹")) {
                point = 4000;
                exp = 1500;
                norespawn = true;
            }
            
            /* 1661 */     EventManager em2 = chr.getClient().getChannelServer().getEventSM().getEventManager("KerningPQ");
/* 1662 */     if (monster.getId() == 9300912) {
/* 1663 */       em2.setProperty("stage5", "clear");
/* 1664 */       broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 1665 */       broadcastMessage(CField.achievementRatio(100));
/*      */     } 
/* 1667 */     if (monster.getId() == 9300008 || monster.getId() == 9300014) {
/* 1668 */       MapleMapFactory mf = chr.getClient().getChannelServer().getMapFactory();
/* 1669 */       int q = 0;
/* 1670 */       for (int i = 0; i < 5; i++) {
/* 1671 */         q += mf.getMap(922010401 + i).getNumMonsters();
/*      */       }
/* 1673 */       if (q == 0) {
/* 1674 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 1675 */         broadcastMessage(CField.achievementRatio(25));
/* 1676 */         broadcastMessage(CField.startMapEffect("다크아이와 쉐도우 아이를 모두 퇴치하였습니다. 엘로그린 벌룬에게 말을 걸어 다음 스테이지로 이동해주세요!", 5120018, true));
/*      */       } 
/*      */     } 
/*      */     
/* 1680 */     if (monster.getId() == 9300010) {
/* 1681 */       this.RPTicket++;
/* 1682 */       if (this.RPTicket == 4) {
/* 1683 */         resetFully();
/* 1684 */       } else if (this.RPTicket == 8) {
/* 1685 */         resetFully();
/* 1686 */       } else if (this.RPTicket == 12) {
/* 1687 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 1688 */         broadcastMessage(CField.achievementRatio(50));
/*      */       } 
/*      */     } 
/*      */     
/* 1692 */     if (monster.getId() == 9300012) {
/* 1693 */       broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 1694 */       broadcastMessage(CField.achievementRatio(100));
/*      */     } 
            
            
            Timer.MapTimer.getInstance().schedule(new Runnable() {
                public void run() {
                    if (!monster.getStats().getName().contains("드래곤킹")) {
                        MapleMap.this.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(monster.getId()), new Point((monster.getPosition()).x, (monster.getStats().getName().contains("도라지") || monster.getStats().getName().contains("요괴선사")) ? ((monster.getPosition()).y - 300) : (monster.getPosition()).y));
                    }
                }
            }, 7000L);
            chr.getBattleGroundChr().setExp(chr.getBattleGroundChr().getExp() + exp);
            chr.getBattleGroundChr().setMoney(chr.getBattleGroundChr().getMoney() + point);
            int t = chr.getBattleGroundChr().getJobType();
            switch (chr.getBattleGroundChr().getJobType()) {
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
            chr.updateSingleStat(MapleStat.EXP, chr.getBattleGroundChr().getExp());
            chr.getBattleGroundChr().setTeam(2);
            broadcastMessage(chr, BattleGroundPacket.UpdateAvater(chr.getBattleGroundChr(), t), false);
            chr.getBattleGroundChr().setTeam(1);
            chr.getClient().send(BattleGroundPacket.UpdateAvater(chr.getBattleGroundChr(), t));
            if (point > 0) {
                chr.getClient().send(BattleGroundPacket.ShowPoint(monster, point));
            }
        }
        if (monster.getBuff(MonsterStatus.MS_Treasure) != null) {
            MapleCharacter buffowner = null;
            for (MapleCharacter chr1 : getAllCharactersThreadsafe()) {
                if (chr.getBuffedValue(400021096)) {
                    buffowner = chr1;
                    break;
                }
            }
            if (buffowner != null) {
                SecondaryStatEffect a = SkillFactory.getSkill(400021104).getEffect(buffowner.getSkillLevel(400021096));
                MapleMist newmist = new MapleMist(a.calculateBoundingBox(buffowner.getPosition(), buffowner.isFacingLeft()), buffowner, a, (int) buffowner.getBuffLimit(400021096), (byte) (buffowner.isFacingLeft() ? 1 : 0));
                newmist.setPosition(monster.getPosition());
                newmist.setDelay(0);
                buffowner.getMap().spawnMist(newmist, false);
            }
        }
        
        /* 2044 */       if (this.mapid == 930000100 && getAllMonstersThreadsafe().size() == 0) {
/* 2045 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 2046 */         broadcastMessage(CField.achievementRatio(15));
/*      */       } 
/*      */       
/* 2049 */       if (this.Monstermarble == 20) {
/* 2050 */         killAllMonsters(true);
/* 2051 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 2052 */         broadcastMessage(CField.startMapEffect("모든 몬스터 구슬을 획득하였습니다. 파티장이 엘린에게 말을 걸어 다음 스테이지로 이동하여 주세요.", 5120023, true));
/* 2053 */         broadcastMessage(CField.achievementRatio(50));
/*      */       } 
/* 2055 */       if (monster.getId() == 9300182) {
/* 2056 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 2057 */         broadcastMessage(CField.startMapEffect("포이즌 골렘을 퇴치하였습니다. 오른쪽의 포탈로 퇴장하실 수 있습니다.", 5120023, true));
/* 2058 */         broadcastMessage(CField.achievementRatio(100));
/*      */       } 
/*      */       
/* 2061 */       if (this.mapid == 921160200 && getAllMonstersThreadsafe().size() == 0) {
/* 2062 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 2063 */         broadcastMessage(CField.achievementRatio(20));
/* 2064 */       } else if (this.mapid == 921160400 && getAllMonstersThreadsafe().size() == 0) {
/* 2065 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 2066 */         broadcastMessage(CField.achievementRatio(50));
/*      */       } 
/* 2068 */       if (monster.getId() == 9300454) {
/* 2069 */         broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
/* 2070 */         broadcastMessage(CField.achievementRatio(100));
/* 2071 */         spawnNpc(9020006, new Point(-1639, -181));
/*      */       } 
        //파티퀘스트
             if(partyquest == 1){
                int Mmobid[] = {9300900, 9300901};
                int Mmobx[] = {-902, -759, -553, -416, -185, -42, 155, 329, 544, 700};
                int Mmoby[] = {213, 273};
                if(getAllMonstersThreadsafe().size() == 0){
                 for (int i = 0; i < Mmobx.length; i++) {
                    for (int g = 0; g < Mmoby.length-1; g++) {
                        MapleMonster mob = MapleLifeFactory.getMonster(Mmobid[0]);
                        MapleMonster mob2 = MapleLifeFactory.getMonster(Mmobid[1]);
                        //mob.setOwner(c.getPlayer().getId());
                        spawnMonsterOnGroundBelow(mob, new Point(Mmobx[i], Mmoby[g]));
                        spawnMonsterOnGroundBelow(mob2, new Point(Mmobx[i], Mmoby[g]));
                    }
                }
                }
            } else if(partyquest == 2){                            
                int Mmobid[] = {9300903, 9300904, 9300905};
                int Mmobid1x[] = {585, -919, 599, -879};
                int Mmobid2x[] = {-838, 693, -835, 568};
                int Mmobid3x[] = {659, -925, -947, 587};
                
                int Mmobid1y[] = {-356, -267, -536, -539};
                int Mmobid2y[] = {-448, -374, -640, -627};
                int Mmobid3y[] = {-444, -495, -387, -263};
                
                if(getAllMonstersThreadsafe().size() == 2){  
                    for (int i = 0; i < 4; i++) {
                        MapleMonster mob = MapleLifeFactory.getMonster(Mmobid[0]);
                        MapleMonster mob1 = MapleLifeFactory.getMonster(Mmobid[1]);
                        MapleMonster mob2 = MapleLifeFactory.getMonster(Mmobid[2]);
                            spawnMonsterOnGroundBelow(mob, new Point(Mmobid1x[i], Mmobid1y[i]));
                            spawnMonsterOnGroundBelow(mob1, new Point(Mmobid2x[i], Mmobid2y[i]));
                            spawnMonsterOnGroundBelow(mob2, new Point(Mmobid3x[i], Mmobid3y[i]));
                    }
                }
                
                if(monster.getId() == 9300903 || monster.getId() == 9300904 || monster.getId() == 9300905){
                    moonstak++;
                }
                
                if(getMoonCake() > 30 && getMoonCake() < 40){
                    broadcastMessage(CField.startMapEffect("의 떡 찧기가 탄력을 받기 시작했군!", 5120016, true));
                } else if (getMoonCake() > 60 && getMoonCake() < 70){
                     broadcastMessage(CField.startMapEffect("오오, 의 떡 찧기에 불이 붙었군!", 5120016, true));
                } else if (getMoonCake() > 79 && getMoonCake() < 100){
                    partyquest++;
                }
                
                if(moonstak % 2 == 0){
                    moonstak = 0;
                    MapleMonster mob4 = MapleLifeFactory.getMonster(9300906);
                    Item item = new Item(4001101, (byte) 0, (short) 3, (byte) 0);
                    spawnMobDrop(item, new Point(-213, -192), mob4, chr, (byte) 1, 0);
                    spawnMobPublicDrop(item, new Point(-213, -192), mob4, chr, (byte) 2, 0);
                    //위 두줄 같음
                    MapleMonster mob;
                    for (MapleMapObject monstermo : getMapObjectsInRange(chr.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER))) {
                        mob = (MapleMonster) monstermo;
                        if(mob.getId() == mob4.getId()){
                            killMonster(mob, chr, true, false, (byte) 1);
                            break;
                        }
                    }               
                } else if(partyquest == 3){
                    killAllMonsters(true);
                    broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
                    broadcastMessage(CField.startMapEffect("의 떡 80개를 다 모았군, 냠냠 정말 맛있군. 어흥이님을 찾아와.", 5120016, true));
                }
                
                if(monster.getId() == 9300906){
                    MapleMonster mob5 = MapleLifeFactory.getMonster(9300906);
                    spawnMonsterOnGroundBelow(mob5, new Point(-213, -192));
                } 
            }
        
        if (monster.getId() == 9500006 || monster.getId() == 9500007) {
            broadcastMessage(CWvsContext.getTopMsg("대박 보스를 처치 하였습니다 !!!"));
            broadcastMessage(CWvsContext.getTopMsg("잠시 후 보상이 지급됩니다 !"));
            server.Timer.EtcTimer tMan = server.Timer.EtcTimer.getInstance();
            tMan.schedule(new Runnable() {
                @Override
                public void run() {
                    for (MapleCharacter player : chr.getClient().getChannelServer().getMapFactory().getMap(chr.getMapId()).getCharacters()) {
                        if (Randomizer.isSuccess(20)) {
                            player.gainItem(2435719, (short) Randomizer.rand(1, 3), false, 0, "대박 보스 고정 보상 4");
                        } else {
                            player.gainItem(2435719, (short) Randomizer.rand(3, 3), false, 0, "대박 보스 고정 보상 4");
                            player.gainItem(5068300, (short) Randomizer.rand(1, 1), false, 0, "대박 보스 히든 보상 1");
                            player.dropMessage(1, "[대박 보스]\r\n히든 보상이 지급 되었습니다.");
                        }
                        player.dropMessage(1, "[대박 보스]\r\n대박 보스 처치 보상이 지급 되었습니다. 앞으로도 많은 도전 부탁드립니다.");
                    }
                }
            }, 10);
        }

        if (monster.getId() == 9500319) {
            broadcastMessage(CWvsContext.getTopMsg("거대 눈사람을 처치 하였습니다 !!!"));
            broadcastMessage(CWvsContext.getTopMsg("잠시 후 보상이 지급됩니다 !"));
            server.Timer.EtcTimer tMan = server.Timer.EtcTimer.getInstance();
            tMan.schedule(new Runnable() {
                @Override
                public void run() {
                    for (MapleCharacter player : chr.getClient().getChannelServer().getMapFactory().getMap(chr.getMapId()).getCharacters()) {
                        if (Randomizer.isSuccess(20)) {
                            player.gainItem(2435719, (short) Randomizer.rand(1, 3), false, 0, "대박 보스 고정 보상 4");
                        } else {
                            player.gainItem(2435719, (short) Randomizer.rand(3, 3), false, 0, "대박 보스 고정 보상 4");
                            player.gainItem(5068300, (short) Randomizer.rand(1, 1), false, 0, "대박 보스 히든 보상 1");
                            player.dropMessage(1, "[이벤트 보스]\r\n스페셜 보상이 지급 되었습니다.");
                        }
                        player.dropMessage(1, "[이벤트 보스]\r\n거대 눈사람 처치 보상이 지급 되었습니다. 즐거운 크리스마스 보내세요!");
                    }
                }
            }, 10);
        }
        if (getId() == 450001400) {
            chr.addSkillCustomInfo(450001400, 1L);
            chr.getClient().send(SLFCGPacket.ErdaSpectrumGauge((int) chr.getSkillCustomValue0(450001400), (int) chr.getSkillCustomValue0(8641018), (int) chr.getSkillCustomValue0(450001402)));
            chr.getClient().send(SLFCGPacket.EventSkillOnEffect((monster.getPosition()).x, (monster.getPosition()).y, 2, 1));
        }
        if (this.RealSpawns.contains(monster)) {
            this.RealSpawns.remove(monster);
        }
        if (chr.getSkillLevel(160010001) > 0 || chr.getSkillLevel(80003058) > 0) {
            chr.handleNatureFriend();
        }
        removeMapObject(monster);
        monster.killed();
        this.spawnedMonstersOnMap.decrementAndGet();
        chr.mobKilled(monster.getId(), lastSkill);
        boolean instanced = (monster.getEventInstance() != null || getEMByMap() != null);
        try {
            int dropOwner = monster.killBy(chr, lastSkill);
            if (animation >= 0) {
                if (monster.getId() == 8220110) {
                    broadcastMessage(MobPacket.killMonster(monster.getObjectId(), 80, 1));
                } else {
                    broadcastMessage(MobPacket.killMonster(monster.getObjectId(), animation));
                    if (monster.getSeperateSoul() > 0) {
                        return;
                    }
                }
            }
            if (GameConstants.isLinkMap(this.mapid)) {
                int count = chr.getLinkMobCount();
                chr.setLinkMobCount(count - 1);
                chr.getClient().getSession().writeAndFlush(SLFCGPacket.FrozenLinkMobCount(count - 1));
                if (count <= 0) {
                    chr.setLinkMobCount(0);
                    for (MapleMapObject monstermo : getAllMonstersThreadsafe()) {
                        MapleMonster mob = (MapleMonster) monstermo;
                        if (mob.getOwner() == chr.getId()) {
                            mob.setHp(0L);
                            broadcastMessage(MobPacket.killMonster(mob.getObjectId(), 1));
                            removeMapObject(mob);
                            mob.killed();
                        }
                    }
                }
            }
            if (monster.getId() == 9300166) {
                animation = 4;
            }
            chr.checkSpecialCoreSkills("killCount", monster.getObjectId(), (SecondaryStatEffect) null);
            if ((monster.getId() >= 8850000 && monster.getId() <= 8850004) || (monster.getId() >= 8850100 && monster.getId() <= 8850104)) {
                if (getMonsterById(8850011) != null) {
                    MapleMonster cygnus = getMonsterById(8850011);
                    broadcastMessage(CWvsContext.getTopMsg("시그너스가 자신의 심복이 당한것에 분노하여 모든것을 파괴하려 합니다."));
                    broadcastMessage(MobPacket.setAfterAttack(cygnus.getObjectId(), 4, 1, 17, cygnus.isFacingLeft()));
                } else if (getMonsterById(8850111) != null) {
                    MapleMonster cygnus = getMonsterById(8850111);
                    broadcastMessage(CWvsContext.getTopMsg("시그너스가 자신의 심복이 당한것에 분노하여 모든것을 파괴하려 합니다."));
                    broadcastMessage(MobPacket.setAfterAttack(cygnus.getObjectId(), 4, 1, 17, cygnus.isFacingLeft()));
                }
            }
                       String[] qlist = {"TangYoons", "river", "chewchew", "rehelen", "arcana", "moras", "esfera"};

            for (String qlis : qlist) {
                if (chr.getV(qlis + "_" + monster.getId() + "_isclear") != null && chr.getV(qlis + "_" + monster.getId() + "_count") != null && chr.getV(qlis + "_" + monster.getId() + "_mobq") != null) {
                    if (chr.getV(qlis + "_" + monster.getId() + "_isclear").equals("0") && Integer.parseInt(chr.getV(qlis + "_" + monster.getId() + "_count")) < Integer.parseInt(chr.getV(qlis + "_" + monster.getId() + "_mobq"))) {
                        int count = Integer.parseInt(chr.getV(qlis + "_" + monster.getId() + "_count")) + 1;
                        chr.addKV(qlis + "_" + monster.getId() + "_count", "" + count);
                        chr.dropMessage(-1, monster.getStats().getName() + " " + chr.getV(qlis + "_" + monster.getId() + "_count") + " / " + chr.getV(qlis + "_" + monster.getId() + "_mobq"));
                        if (Integer.parseInt(chr.getV(qlis + "_" + monster.getId() + "_count")) >= Integer.parseInt(chr.getV(qlis + "_" + monster.getId() + "_mobq"))) {
                            chr.addKV(qlis + "_" + monster.getId() + "_isclear", "1");
                        }
                    }
                }
            }
            
            if (chr.getGuild() != null && chr.getGuildId() > 0) {
                boolean party = false;
                if (chr.getParty() != null) {
                    if (chr.getParty().getMembers().size() >= 2) {
                        for (MaplePartyCharacter partychar : chr.getParty().getMembers()) {
                            if (partychar != null && partychar.getMapid() == chr.getMapId() && partychar.getChannel() == this.channel) {
                                MapleCharacter other = chr.getClient().getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                                if (other.getId() != chr.getId()
                                        && other != null
                                        && other.getGuild() != null
                                        && other.getMapId() == chr.getMapId()) {
                                    if (other.getGuildId() == chr.getGuildId()) {
                                        party = true;
                                        World.Guild.gainContribution(other.getGuildId(), GameConstants.GPboss(monster.getId(), party, false), other.getId());
                                        continue;
                                    }
                                    World.Guild.gainContribution(other.getGuildId(), GameConstants.GPboss(monster.getId(), party, false), other.getId());
                                }
                            }
                        }
                    }
                    World.Guild.gainContribution(chr.getGuildId(), GameConstants.GPboss(monster.getId(), party, false), chr.getId());
                } else {
                    World.Guild.gainContribution(chr.getGuildId(), GameConstants.GPboss(monster.getId(), party, false), chr.getId());
                }
            }
            if (isEliteChmpmap()) {
                boolean end = false;
                boolean next = isElitechmpfinal();
                List<Integer> killmoblist = new ArrayList<>();
                if (getElitechmptype() == 0) {
                    for (int i = 0; i <= 5; i++) {
                        killmoblist.add(Integer.valueOf(8220100 + i));
                    }
                } else if (getElitechmptype() == 1) {
                    for (int i = 0; i <= 2; i++) {
                        killmoblist.add(Integer.valueOf(8220106 + i));
                    }
                } else if (getElitechmptype() == 2) {
                    for (int i = 0; i <= 1; i++) {
                        killmoblist.add(Integer.valueOf(8220122 + i));
                    }
                } else if (getElitechmptype() == 3) {
                    for (int i = 0; i <= 1; i++) {
                        killmoblist.add(Integer.valueOf(8220110 + i));
                    }
                } else if (getElitechmptype() == 4) {
                    for (int i = 0; i <= 1; i++) {
                        killmoblist.add(Integer.valueOf(8220124 + i));
                    }
                }
                for (Integer list : killmoblist) {
                    if (list.intValue() == monster.getId()) {
                        setElitechmpcount(getElitechmpcount() - 1);
                        if (getElitechmpcount() <= 0) {
                            end = true;
                            break;
                        }
                    }
                }
                if (!next && end) {
                    if (getElitechmptype() == 2) {
                        setElitechmpcount(1);
                        setElitechmpfinal(true);
                        int i = 8220123;
                        MapleMonster EliteChmp = MapleLifeFactory.getMonster(i);
                        EliteChmp.setEliteGrade(1);
                        EliteChmp.setEliteType(4);
                        EliteChmp.getStats().setLevel(chr.getLevel());
                        EliteChmp.setHp(monster.getStats().getHp() * 3L);
                        EliteChmp.setElitechmp(true);
                        spawnMonsterOnGroundBelow(EliteChmp, monster.getPosition());
                        broadcastMessage(CField.startMapEffect("숨어있던 어둠 늑대가 나타났습니다.", 5120124, true));
                    } else if (getElitechmptype() == 4) {
                        setElitechmpcount(10);
                        setElitechmpfinal(true);
                        for (int i = 0; i < 10; i++) {
                            int j = 8220124;
                            MapleMonster EliteChmp = MapleLifeFactory.getMonster(j);
                            EliteChmp.setEliteGrade(1);
                            EliteChmp.setEliteType(4);
                            EliteChmp.getStats().setLevel(chr.getLevel());
                            EliteChmp.setScale(50);
                            EliteChmp.setHp(monster.getStats().getHp() / 2L);
                            EliteChmp.setElitechmp(true);
                            spawnMonsterOnGroundBelow(EliteChmp, new Point((monster.getPosition()).x + Randomizer.rand(-150, 150), (monster.getPosition()).y));
                        }
                        broadcastMessage(CField.startMapEffect("다크 가고일이 미니 다크 가고일로 분리되었습니다.", 5120124, true));
                    }
                } else if (end && next) {
                    setEliteChmpmap(false);
                    setElitechmpcount(0);
                    setElitechmptype(0);
                    this.eliteCount++;
                    for (MapleMonster mob : getAllMonster()) {
                        if (monster.getId() != mob.getId() && (mob.getId() == 8220123 || mob.getId() == 8220122 || mob.getId() == 8220124 || mob.getId() == 8220125 || (mob.getId() >= 8220100 && mob.getId() <= 8220108))) {
                            killMonster(mob.getId());
                            continue;
                        }
                        if (mob.getId() == 8220110) {
                            mob.setHp(0L);
                            if (this.RealSpawns.contains(monster)) {
                                this.RealSpawns.remove(monster);
                            }
                            broadcastMessage(MobPacket.killMonster(mob.getObjectId(), 80, 1));
                            removeMapObject(mob);
                            mob.killed();
                            this.spawnedMonstersOnMap.decrementAndGet();
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        int x = (monster.getPosition()).x + Randomizer.rand(-300, 300);
                        int y = (monster.getPosition()).y;
                        MapleFoothold fh = getFootholds().findBelow(new Point(x, y));
                        if (fh != null) {
                            spawnMobPublicDrop(new Item(2023927, (short) 0, (short) 1, 0), new Point(x, y), monster, chr, (byte) 0, 0);
                        }
                    }
                    monster.setCustomInfo(1, 1, 0);
                    if (!monster.isElitemonster()) {
                        //chr.checkLiveQuest(1, false);
                        if (chr.getKeyValue(51351, "startquestid") == 49012L || chr.getKeyValue(51351, "startquestid") == 49013L || chr.getKeyValue(51351, "startquestid") == 49014L) {
                            int maxmob = (chr.getKeyValue(51351, "startquestid") == 49012L) ? 1 : ((chr.getKeyValue(51351, "startquestid") == 49013L) ? 2 : 3);
                            String q = "";
                            int count = Integer.parseInt(chr.getQuest(MapleQuest.getInstance((int) chr.getKeyValue(51351, "startquestid"))).getCustomData());
                            count++;
                            if (count < 10) {
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
                            MapleQuest.getInstance((int) chr.getKeyValue(51351, "startquestid")).forceStart(chr, 0, q);
                            if (count >= maxmob) {
                                chr.setKeyValue(51351, "queststat", "3");
                                chr.getClient().send(CWvsContext.updateSuddenQuest((int) chr.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + chr.getKeyValue(51351, "startquestid") + ";state=3;"));
                            }
                        }
                    }
                    broadcastMessage(CField.startMapEffect((this.eliteCount <= 15) ? "어두운 기운이 사라지지 않아 이곳을 음산하게 만들고 있습니다." : "이곳이 어둠으로 가득차 곧 무슨일이 일어날 듯 합니다.", 5120124, true));
                }
            }
            if (chr.getBuffedEffect(SecondaryStat.SoulMP) != null) {
                Item toDrop = new Item(4001536, (short) 0, (short) Randomizer.rand(1, 5), 0);
                Item weapon = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                if (weapon != null) {
                    spawnSoul(monster, chr, toDrop, monster.getPosition(), weapon);
                }
            }
            if ((monster.getId() == 8500003 || monster.getId() == 8500004 || monster.getId() == 8500007 || monster.getId() == 8500008) && Randomizer.isSuccess(100)) {
                int itemcode = 0;
                if (monster.getId() == 8500007 || monster.getId() == 8500008) {
                    itemcode = Randomizer.rand(2437606, 2437607);
                } else {
                    itemcode = Randomizer.rand(2437659, 2437664);
                }
                Item toDrop = new Item(itemcode, (short) 0, (short) 1, 0);
                spawnItemDrop(monster, chr, toDrop, new Point((monster.getTruePosition()).x, (monster.getTruePosition()).y), true, false);
            }
            if (chr.getClient().getQuestStatus(100825) == 2) {
                if (chr.isGM() || (monster.getStats().getLevel() - 21 <= chr.getLevel() && chr.getLevel() <= monster.getStats().getLevel() + 21 && isSpawnPoint() && !GameConstants.isContentsMap(getId()) && !GameConstants.보스맵(getId()) && !GameConstants.사냥컨텐츠맵(getId()) && !GameConstants.튜토리얼(getId()) && !GameConstants.로미오줄리엣(getId()) && !GameConstants.피라미드(getId()))) {
                    if (!monster.getStats().isBoss() && !monster.isElitemonster() && !monster.isElitechmp() && !monster.isEliteboss()) {
                        //  chr.checkLiveQuest(0, true);
                        //  chr.checkLiveQuest(0, false);
                    }
                    if (chr.getBuffedValue(80003064) && chr.getSkillCustomValue0(100857) <= 0L) {
                        if (chr.getKeyValue(100857, "feverCnt") >= 10L) {
                            while (chr.getBuffedValue(80003064)) {
                                chr.cancelEffect(chr.getBuffedEffect(80003064));
                            }
                        } else if (10L - chr.getKeyValue(100857, "feverCnt") > 0L || chr.isGM()) {
                            if (chr.getKeyValue(100857, "feverCnt") > 3L) {
                                chr.setKeyValue(100857, "state", "1");
                            }
                            if (chr.getKeyValue(100857, "count") <= 0L) {
                                chr.setKeyValue(100857, "count", "0");
                            }
                            chr.setKeyValue(100857, "count", (chr.getKeyValue(100857, "count") + (chr.isGM() ? 1L : 1L)) + "");
                            if (chr.getKeyValue(100857, "count") == 250L) {
                                if (Randomizer.isSuccess(50)) {
                                    chr.getClient().send(CField.enforceMsgNPC(9062549, 3000, "신나는 #r팡팡 리액션#k을 하기까지\r\n절반 정도 왔어! 준비하라고!"));
                                } else {
                                    chr.getClient().send(CField.enforceMsgNPC(9062549, 3000, "벌써 #r250마리#k의 몬스터를 처치!\r\n나한테 사냥을 배워서 그런가?\r\n정말 빠른걸!"));
                                }
                            } else if (chr.getKeyValue(100857, "count") >= 500L) {
                                if (chr.getClient().getCustomKeyValue(501567, "fever") < 0L) {
                                    chr.getClient().setCustomKeyValue(501567, "fever", "0");
                                }
                                if (chr.getClient().getCustomKeyValue(501567, "fever") < 200L) {
                                    chr.getClient().setCustomKeyValue(501567, "fever", chr.getClient().getCustomKeyValue(501567, "fever") + 1L + "");
                                }
                                chr.getClient().send(CField.enforceMSG("\uba54\uc774\ud50c \ud321\ud321 \ub9ac\uc561\uc158 \ud0c0\uc784! \uc5b4\uc11c \ud6c4\uc6d0\ubc1b\uc740 \ud321\ud321 \uc8fc\uba38\ub2c8\ub97c \ud130\ub728\ub824!", 341, 20000));
                                chr.getClient().send(CField.enforceMsgNPC(9062549, 3000, "\ud6c4\uc6d0\uc774 \ud130\uc9c0\ub294 \uc774 \uc21c\uac04!\r\n\uba4b\uc9c4 #r\ud321\ud321 \ub9ac\uc561\uc158#k\uc744 \ubcf4\uc5ec\uc918!"));
                                chr.setKeyValue(100857, "count", "0");
                                chr.setKeyValue(100857, "feverCnt", chr.getKeyValue(100857, "feverCnt") + 1L + "");
                                chr.getClient().send(CField.PangPangReactionReady(80003064, 2900));
                                Timer.EtcTimer.getInstance().schedule(() -> {
                                    chr.getClient().send(CField.PangPangReactionAct(60000));
                                    chr.setSkillCustomInfo(100857, 35L, 60000L);
                                    Timer.EtcTimer.getInstance().schedule(() -> {
                                        if (chr.getSkillCustomValue(100857) != null) {
                                            chr.getClient().send(CField.enforceMsgNPC(9062549, 3000, "\uc815\ub9d0 \ubaa8\ub450\uc758 \uc18d\uc774 \ub2e4 \uc2dc\uc6d0\ud574\uc9c0\ub294 #r\ud321\ud321 \ub9ac\uc561\uc158#k\uc774\uc5c8\uc5b4!"));
                                            chr.getClient().send(CField.UIPacket.detailShowInfo("\uc624\ub298\uc758 \ub9ac\uc561\uc158 \ud69f\uc218 : " + chr.getKeyValue(100857, "feverCnt") + "/10\ud68c", 3, 20, 20));
                                            chr.dropMessage(5, "\uc624\ub298\uc758 \ub9ac\uc561\uc158 \ud69f\uc218 : " + chr.getKeyValue(100857, "feverCnt") + "/10\ud68c");
                                            chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.EventSpecialSkill));
                                            SkillFactory.getSkill(80003064).getEffect(1).applyTo(chr);
                                            chr.removeSkillCustomInfo(100857);
                                            for (MapleMonster mons : this.getAllMonster()) {
                                                if (mons.getId() != 9833965 || mons.getOwner() != chr.getId()) {
                                                    continue;
                                                }
                                                this.killMonsterType(mons, 1);
                                            }
                                        }
                                    }, 60000L);
                                }, 2800L);
                                for (int i = 1; i <= 5; ++i) {
                                    Timer.EtcTimer.getInstance().schedule(() -> {
                                        int a = 0;
                                        for (Spawns s : this.monsterSpawn) {
                                            MapleMonster mon = MapleLifeFactory.getMonster(9833965);
                                            mon.getStats().setLevel(monster.getStats().getLevel());
                                            mon.setHp(monster.getStats().getHp() * 2L);
                                            mon.getStats().setExp(monster.getStats().getExp() * 2L);
                                            mon.setOwner(chr.getId());
                                            this.spawnMonsterOnGroundBelow(mon, s.getPosition());
                                            if (++a != 7) {
                                                continue;
                                            }
                                            break;
                                        }
                                    }, 3500 * i);
                                }
                            }
                        }
                    }
                }
            }
            if (monster.getId() == 9833965
                    && chr.getSkillCustomValue0(100857) > 0L) {
                chr.addSkillCustomInfo(100857, -1L);
                if (chr.getSkillCustomValue0(100857) <= 0L) {
                    chr.getClient().send(CField.enforceMsgNPC(9062549, 3000, "정말 모두의 속이 다 시원해지는 #r팡팡 리액션#k이었어!"));
                    chr.getClient().send(CField.UIPacket.detailShowInfo("오늘의 리액션 횟수 : " + chr.getKeyValue(100857, "feverCnt") + "/10회", 3, 20, 20));
                    chr.dropMessage(5, "오늘의 리액션 횟수 : " + chr.getKeyValue(100857, "feverCnt") + "/10회");
                    while (chr.getBuffedValue(80003064)) {
                        chr.cancelEffect(chr.getBuffedEffect(80003064));
                    }
                    SkillFactory.getSkill(80003064).getEffect(1).applyTo(chr);
                    for (MapleMonster mons : getAllMonster()) {
                        if (mons.getId() == 9833965 && mons.getOwner() == chr.getId()) {
                            killMonsterType(mons, 1);
                        }
                    }
                    chr.removeSkillCustomInfo(100857);
                    chr.getClient().send(CField.PangPangReactionEnd(5000));
                }
            }
            if (chr.getKeyValue(51351, "startquestid") >= 49001L && chr.getKeyValue(51351, "startquestid") <= 49003L && monster.getStats().getLevel() - 21 <= chr.getLevel() && chr.getLevel() <= monster.getStats().getLevel() + 21 && isSpawnPoint() && !GameConstants.isContentsMap(getId()) && !GameConstants.보스맵(getId()) && !GameConstants.사냥컨텐츠맵(getId()) && !GameConstants.튜토리얼(getId()) && !GameConstants.로미오줄리엣(getId()) && !GameConstants.피라미드(getId())) {
                int maxmob = (chr.getKeyValue(51351, "startquestid") == 49001L) ? 100 : ((chr.getKeyValue(51351, "startquestid") == 49002L) ? 200 : 300);
                String q = "";
                int count = Integer.parseInt(chr.getQuest(MapleQuest.getInstance((int) chr.getKeyValue(51351, "startquestid"))).getCustomData());
                count++;
                if (count < 10) {
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
                MapleQuest.getInstance((int) chr.getKeyValue(51351, "startquestid")).forceStart(chr, 0, q);
                if (count >= maxmob) {
                    chr.setKeyValue(51351, "queststat", "3");
                    chr.getClient().send(CWvsContext.updateSuddenQuest((int) chr.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + chr.getKeyValue(51351, "startquestid") + ";state=3;"));
                }
            }
            String name = null;
            if (monster.getBuff(2111010) != null) {
                for (int i = 0; i < 2; i++) {
                    MapleSummon tosummon = new MapleSummon(chr, 2111010, monster.getTruePosition(), SummonMovementType.WALK_STATIONARY, (byte) 0, (int) chr.getBuffLimit(2111010));
                    if (chr.getSummons(2111010).size() < 10) {
                        chr.getMap().spawnSummon(tosummon, (int) chr.getBuffLimit(2111010));
                        chr.addSummon(tosummon);
                    }
                }
            }
            if (monster.getId() == 8500001 || monster.getId() == 8500002 || monster.getId() == 8500011 || monster.getId() == 8500012 || monster.getId() == 8500021 || monster.getId() == 8500022) {
                int typeed = monster.getId() % 10;
                if (typeed == 2) {
                    broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 0));
                    broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTime(0, false, true));
                }
            }
            if (chr.getV("bossPractice") != null && chr.getV("bossPractice").equals("1")) {
                withDrops = false;
            }
            if (monster.isExtreme() && name != null) {
                name = "익스트림" + name;
            }
            if (isElitebossrewardmap() && getElitebossrewardtype() == 1 && monster.getId() == 8220027
                    && Randomizer.isSuccess(chr.isGM() ? 100 : 60)) {
                Item toDrop = new Item(2432398, (short) 0, (short) 1, 0);
                spawnItemDrop(monster, chr, toDrop, new Point((monster.getTruePosition()).x + 25, (monster.getTruePosition()).y), true, false);
            }
            if (!isElitebossmap() && !isEliteChmpmap() && !monster.isElitemonster() && !isElitebossrewardmap() && !monster.isEliteboss() && !monster.getStats().isBoss() && monster.getStats().getLevel() > 100 && monster
                    .getStats().getLevel() - 21 <= chr.getLevel() && chr.getLevel() <= monster.getStats().getLevel() + 21 && isSpawnPoint()) {
                setEliteMobCommonCount(this.EliteMobCommonCount + 1);
            } else if (!isElitebossmap() && !isElitebossrewardmap() && monster.isElitemonster()) {
                if (!monster.isUserunespawn()) {
                    setEliteCount(this.eliteCount + 1);
                }
//                chr.checkLiveQuest(1, false);
                if (chr.getKeyValue(51351, "startquestid") == 49012L || chr.getKeyValue(51351, "startquestid") == 49013L || chr.getKeyValue(51351, "startquestid") == 49014L) {
                    int maxmob = (chr.getKeyValue(51351, "startquestid") == 49012L) ? 1 : ((chr.getKeyValue(51351, "startquestid") == 49013L) ? 2 : 3);
                    String q = "";
                    int count = Integer.parseInt(chr.getQuest(MapleQuest.getInstance((int) chr.getKeyValue(51351, "startquestid"))).getCustomData());
                    count++;
                    if (count < 10) {
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
                    MapleQuest.getInstance((int) chr.getKeyValue(51351, "startquestid")).forceStart(chr, 0, q);
                    if (count >= maxmob) {
                        chr.setKeyValue(51351, "queststat", "3");
                        chr.getClient().send(CWvsContext.updateSuddenQuest((int) chr.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + chr.getKeyValue(51351, "startquestid") + ";state=3;"));
                    }
                }
                if (getEliteCount() < 5) {
                    broadcastMessage(CField.startMapEffect("어두운 기운이 사라지지 않아 이곳을 음산하게 만들고 있습니다.", 5120124, true));
                } else if (getEliteCount() < 10) {
                    broadcastMessage(CField.startMapEffect("이곳이 어두운 기운으로 가득차 곧 무슨 일이 일어날 듯 합니다.", 5120124, true));
                }
            }

            if (monster.getId() == 8860000) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 노말 아카이럼을 격파하였습니다!"));
            }

            if (monster.getId() == 8880000) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 하드 매그너스를 격파하였습니다!"));
            }

            if (monster.getId() == 8500022) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 카오스 파풀라투스를 격파하였습니다!"));
            }

            if (monster.getId() == 8950102) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 노말 스우를 격파하였습니다!"));
            }

            if (monster.getId() == 8950002) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 하드 스우를 격파하였습니다!"));
            }

            if (monster.getId() == 8880111) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 노말 데미안을 격파하였습니다!"));
            }

            if (monster.getId() == 8880101) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 하드 데미안을 격파하였습니다!"));
            }

            if (monster.getId() == 8880151) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 노말 루시드를 격파하였습니다!"));
            }
            if (monster.getId() == 8880153) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 하드 루시드를 격파하였습니다!"));
            }
            if (monster.getId() == 8880302) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 하드 윌을 격파하였습니다!"));
            }

            if (monster.getId() == 8644655) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 카오스더스크를 격파하였습니다."));
            }

            if (monster.getId() == 8880405) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 고통의 미궁 : 진힐라를 격파하였습니다."));
            }

            if (monster.getId() == 8880504) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 검은 마법사를 격파하였습니다."));
            }

            if (monster.getId() == 8880602) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[" + chr.getParty().getLeader().getName() + "]님의 파티가 선택받은 세렌을 격파하였습니다."));
            }

            if (getCustomValue(8222222) == null && this.EliteMobCommonCount >= 700) {
                this.EliteMobCommonCount = 0;
                if (getEliteCount() == 5 && monster.getEliteGrade() <= -1 && isSpawnPoint() && !isEliteChmpmap() && !isElitebossmap() && !isElitebossrewardmap()) {
                    MapleMonster mapleMonster1;
                    int i, Rand;
                    MapleMonster EliteChmp, Flower;
                    int k, type = Randomizer.rand(0, 4);
                    int j = 0;
                    String txt = "";
                    setEliteChmpmap(true);
                    setElitechmptype(type);
                    setCustomInfo(8222222, 0, 600000);
                    switch (type) {
                        case 0:
                            j = 8220100;
                            txt = "어둠 속에서 점차 성장하는 블랙 크레센도 슬라임이 나타납니다.";
                            mapleMonster1 = MapleLifeFactory.getMonster(j);
                            setElitechmpfinal(true);
                            mapleMonster1.setEliteGrade(1);
                            mapleMonster1.setEliteType(4);
                            mapleMonster1.getStats().setLevel(chr.getLevel());
                            mapleMonster1.setHp((long) ((monster.getStats().getHp() * 25L) * mapleMonster1.bonusHp()));
                            mapleMonster1.setElitehp(mapleMonster1.getHp());
                            setElitechmpcount(3);
                            spawnMonsterOnGroundBelow(mapleMonster1, monster.getTruePosition());
                            break;
                        case 1:
                            for (i = 0; i < 20; i++) {
                                int m = Randomizer.rand(0, this.monsterSpawn.size() - 1);
                                j = Randomizer.rand(8220106, 8220108);
                                MapleMonster mapleMonster = MapleLifeFactory.getMonster(j);
                                mapleMonster.setEliteGrade(1);
                                mapleMonster.setEliteType(4);
                                mapleMonster.getStats().setLevel(chr.getLevel());
                                mapleMonster.setHp((long) ((monster.getStats().getHp() * 10L) * mapleMonster.bonusHp()));
                                mapleMonster.setElitehp(mapleMonster.getHp());
                                spawnMonsterOnGroundBelow(mapleMonster, ((Spawns) this.monsterSpawn.get(m)).getPosition());
                            }
                            setElitechmpfinal(true);
                            setElitechmpcount(20);
                            txt = "어둠 속에서 그림자 나비 떼들이 몰려옵니다.";
                            break;
                        case 2:
                            for (i = 0; i < 5; i++) {
                                int m = Randomizer.rand(0, this.monsterSpawn.size() - 1);
                                j = 8220122;
                                MapleMonster mapleMonster = MapleLifeFactory.getMonster(j);
                                mapleMonster.setEliteGrade(1);
                                mapleMonster.setEliteType(4);
                                mapleMonster.getStats().setLevel(chr.getLevel());
                                mapleMonster.setHp((long) ((monster.getStats().getHp() * 30L) * mapleMonster.bonusHp()));
                                mapleMonster.setElitehp(mapleMonster.getHp());
                                spawnMonsterOnGroundBelow(mapleMonster, ((Spawns) this.monsterSpawn.get(m)).getPosition());
                            }
                            setElitechmpfinal(false);
                            setElitechmpcount(5);
                            txt = "어둠의 기운을 간직한 구체가 생겨났습니다. 모두 격파하고 숨어있는 어둠 늑대를 처치하세요.";
                            break;
                        case 3:
                            Rand = Randomizer.rand(0, this.monsterSpawn.size() - 1);
                            Flower = MapleLifeFactory.getMonster(8220110);
                            Flower.setEliteGrade(1);
                            Flower.setEliteType(4);
                            Flower.setHp(15L);
                            Flower.getStats().setLevel(chr.getLevel());
                            spawnMonsterOnGroundBelow(Flower, ((Spawns) this.monsterSpawn.get(Rand)).getPosition());
                            for (k = 0; k < 15; k++) {
                                j = 8220111;
                                Rand = Randomizer.rand(0, this.monsterSpawn.size() - 1);
                                MapleMonster mapleMonster = MapleLifeFactory.getMonster(j);
                                mapleMonster.setEliteGrade(1);
                                mapleMonster.setEliteType(4);
                                mapleMonster.getStats().setLevel(chr.getLevel());
                                mapleMonster.setHp((long) ((monster.getStats().getHp() * 20L) * mapleMonster.bonusHp()));
                                mapleMonster.setElitehp(mapleMonster.getHp());
                                spawnMonsterOnGroundBelow(mapleMonster, ((Spawns) this.monsterSpawn.get(Rand)).getPosition());
                            }
                            setElitechmpfinal(true);
                            setElitechmpcount(15);
                            txt = "환상의 꽃이 피어났습니다. 꽃을 공격하는 킬러 비를 모두 처치하세요.";
                            break;
                        case 4:
                            j = 8220125;
                            EliteChmp = MapleLifeFactory.getMonster(j);
                            EliteChmp.setEliteGrade(1);
                            EliteChmp.setEliteType(4);
                            EliteChmp.getStats().setLevel(chr.getLevel());
                            EliteChmp.setHp((long) ((monster.getStats().getHp() * 50L) * EliteChmp.bonusHp()));
                            EliteChmp.setElitehp(EliteChmp.getHp());
                            spawnMonsterOnGroundBelow(EliteChmp, monster.getTruePosition());
                            setElitechmpcount(1);
                            setElitechmpfinal(false);
                            txt = "어둠 속에서 나타난 다크 가고일을 퇴치하고 분리되는 미니 다크 가고일도 모두 처치하세요. ";
                            break;
                    }
                    broadcastMessage(CField.startMapEffect(txt, 5120124, true));
                    Timer.MapTimer.getInstance().schedule(() -> {
                        if (isEliteChmpmap()) {
                            String failtxt = "";
                            if (getElitechmptype() == 0) {
                                failtxt = "블랙 크레센도 슬라임 처치에 실패하였습니다. 슬라임이 어둠의 그림자에 스며듭니다.";
                            } else if (getElitechmptype() == 1) {
                                failtxt = "그림자 나비 처치에 실패하였습니다. 나비들이 어둠의 그림자에 스며듭니다.";
                            } else if (getElitechmptype() == 2) {
                                failtxt = "어둠 늑대 처치에 실패하였습니다. 늑대가 어둠의 그림자에 스며듭니다.";
                            } else if (getElitechmptype() == 3) {
                                failtxt = "환상의 꽃 보호에 실패하였습니다. 킬러 비가 환상의 꽃의 기운을 빼앗고 사라집니다.";
                            } else if (getElitechmptype() == 4) {
                                failtxt = "다크 가고일 처치에 실패하였습니다. 가고일들이 어둠의 그림자에 스며듭니다";
                            }
                            for (MapleMonster mob : getAllMonster()) {
                                if (mob.getId() == 8220123 || mob.getId() == 8220111 || mob.getId() == 8220122 || mob.getId() == 8220124 || mob.getId() == 8220125 || (mob.getId() >= 8220100 && mob.getId() <= 8220108)) {
                                    killMonster(mob.getId());
                                    continue;
                                }
                                if (mob.getId() == 8220110) {
                                    mob.setHp(0L);
                                    if (this.RealSpawns.contains(monster)) {
                                        this.RealSpawns.remove(monster);
                                    }
                                    broadcastMessage(MobPacket.killMonster(mob.getObjectId(), 80, 1));
                                    removeMapObject(mob);
                                    mob.killed();
                                    this.spawnedMonstersOnMap.decrementAndGet();
                                }
                            }
                            broadcastMessage(CField.startMapEffect(failtxt, 5120124, true));
                            setEliteChmpmap(false);
                            setElitechmpcount(0);
                            setElitechmptype(0);
                        }
                    }, 60000L);
                } else if (getEliteCount() < 20 && monster.getStats().getLevel() >= 100 && isSpawnPoint() && !isElitebossmap() && !isElitebossrewardmap() && !isEliteChmpmap() && !monster.isElitemonster() && monster.getEliteGrade() < 0) {
                    boolean alreadyspawn = false;
                    for (MapleMonster mob : chr.getMap().getAllMonster()) {
                        if (mob.isElitemonster() || mob.isEliteboss()) {
                            alreadyspawn = true;
                            break;
                        }
                    }
                    if (!alreadyspawn) {
                        if (getEliteCount() >= 10) {
                            if (!isElitebossmap() && !isElitebossrewardmap()) {
                                startEliteBossMap();
                                broadcastMessage(CField.specialMapEffect(2, false, "Bgm36.img/RoyalGuard", "Effect/EliteMobEff.img/eliteMonsterFrame", "Effect/EliteMobEff.img/eliteMonsterEffect", "", ""));
                                setEliteMobCommonCount(0);
                                setEliteCount(0);
                                setEliteMobCount(0);
                                setElitebossmap(true);
                                final int RandomI = Randomizer.rand(0, 4);
                                final int BossID = 8220020 + RandomI + 2;
                                final int EffectID = 5120120 + RandomI + 5;
                                final String[] EffectMsgs = {"검은 기사 모카딘 : 위대한 분을 위하여 너를 처단하겠다.", "미친 마법사 카리아인 : 미천한 것들이 날뛰고 있구나. 크크크크...", "돌격형 CQ57 : 목표발견. 제거 행동을 시작한다.", "인간사냥꾼 줄라이 : 사냥감이 나타났군.", "싸움꾼 플레드 : 재미 있겠군. 어디 한 번 놀아볼까."};
                                killAllMonsters(true);
                                Timer.MapTimer.getInstance().schedule(new Runnable() {
                                    public void run() {
                                        MapleMonster eliteBoss = MapleLifeFactory.getMonster(BossID);
                                        int rand = Randomizer.rand(0, EliteMonsterGradeInfo.getThirdGradeInfo().size() - 1);
                                        eliteBoss.getEliteGradeInfo().add(new Pair<>(Integer.valueOf(((EliteMonsterGradeInfo) EliteMonsterGradeInfo.getThirdGradeInfo().get(Integer.valueOf(rand))).getSkillid()), Integer.valueOf(((EliteMonsterGradeInfo) EliteMonsterGradeInfo.getThirdGradeInfo().get(Integer.valueOf(rand))).getSkilllv())));
                                        eliteBoss.setEliteType(3);
                                        eliteBoss.setEliteGrade(RandomI);
                                        eliteBoss.getStats().setLevel(monster.getStats().getLevel());
                                        eliteBoss.setHp(monster.getStats().getHp() * 750L);
                                        eliteBoss.setEliteboss(true);
                                        eliteBoss.setElitehp(eliteBoss.getHp());
                                        MapleMap.this.makeEliteMonster(monster.getId(), Randomizer.rand(0, 2), ((Spawns) MapleMap.this.monsterSpawn.get(Randomizer.rand(0, MapleMap.this.monsterSpawn.size() - 1))).getPosition(), false, true);
                                        MapleMap.this.makeEliteMonster(monster.getId(), Randomizer.rand(0, 2), ((Spawns) MapleMap.this.monsterSpawn.get(Randomizer.rand(0, MapleMap.this.monsterSpawn.size() - 1))).getPosition(), false, true);
                                        MapleMap.this.spawnMonsterOnGroundBelow(eliteBoss, monster.getTruePosition());
                                        MapleMap.this.broadcastMessage(CField.startMapEffect(EffectMsgs[RandomI], EffectID, true));
                                        World.Broadcast.broadcastMessage(CWvsContext.eliteWarning(MapleMap.this.getId(), eliteBoss.getId(), MapleMap.this.getChannel()));
                                    }
                                }, 6000L);
                            }
                        } else {
                            this.EliteMobCommonCount = 0;
                            makeEliteMonster(monster.getId(), Randomizer.rand(0, 2), monster.getPosition(), false, false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            FileoutputUtil.log("Log_Kill.txt", "" + e);
        }
        if (monster.isEliteboss()) {
            stopEliteBossMap();
            setElitebossrewardmap(true);
            startEliteBossReward();
            //   chr.checkLiveQuest(2, false);
        }
        int map = 0, nextmob = 0;
        Point poz = null;
        if (monster.getId() == 8910001) {
            MapleMap warp = ChannelServer.getInstance(chr.getClient().getChannel()).getMapFactory().getMap(chr.getMapId() - 10);
            broadcastMessage(CWvsContext.getTopMsg("시공간 붕괴 실패! 잠시 후, 원래 세계로 돌아갑니다."));
            if (chr.getParty() != null) {
                for (MaplePartyCharacter chrs : chr.getParty().getMembers()) {
                    chrs.getPlayer().getClient().send(CField.VonVonStopWatch(0));
                    chrs.getPlayer().removeSkillCustomInfo(8910000);
                }
            }
            Timer.MapTimer.getInstance().schedule(new Runnable() {
                public void run() {
                    for (MapleCharacter chr : MapleMap.this.getAllCharactersThreadsafe()) {
                        MapleMap zz = ChannelServer.getInstance(chr.getClient().getChannel()).getMapFactory().getMap(chr.getMapId() - 10);
                        chr.changeMap(zz, zz.getPortal(0));
                    }
                }
            }, 2000L);
        }
        if (monster.getId() / 1000 == 9800 && monster.getMobExp() == 0L) {
            DecimalFormat formatter = new DecimalFormat("###,###");
            chr.setMparkkillcount(chr.getMparkkillcount() + 1);
            int oneexp = chr.getMparkexp() / chr.getMparkcount() * chr.getMparkkillcount();
            chr.getClient().getSession().writeAndFlush(CField.removeMapEffect());
            chr.getClient().getSession().writeAndFlush(CField.startMapEffect("경험치 보상 " + formatter.format(oneexp) + " 누적!", 5120162, true));
        }
        boolean dispel = false, killall = false;
        KoreaCalendar kc = new KoreaCalendar();
        String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
        if (monster.getId() == 8820212 || monster.getId() == 8880302 || monster.getId() == 8880342 || monster.getId() == 8880150 || monster.getId() == 8880151 || monster.getId() == 8880155 || monster.getId() == 8644650 || monster.getId() == 8644655 || monster.getId() == 8880405 || monster.getId() == 8645009 || monster.getId() == 8645039 || monster.getId() == 8880504 || monster.getId() == 8880602 || monster
                .getId() == 8880614 || monster.getId() == 8820101 || monster.getId() == 8820001 || monster.getId() == 8860005 || monster.getId() == 8860000 || monster.getId() == 8810018 || monster.getId() == 8810122 || monster.getId() == 8840007 || monster.getId() == 8840000 || monster.getId() == 8840014 || monster.getId() == 8800102 || monster.getId() == 8500012 || monster.getId() == 8500002 || monster.getId() == 8880200 || monster.getId() == 8870000 || monster.getId() == 8800002 || monster.getId() == 8500022 || monster.getId() == 8870100 || monster.getId() == 8910100 || monster.getId() == 8930100 || monster.getId() == 8910000 || monster.getId() == 8930000 || monster.getId() == 8850011 || monster.getId() == 8850111 || monster.getId() == 8950102 || monster.getId() == 8950002 || monster.getId() == 8880111 || monster.getId() == 8880101 || (monster
                .getId() >= 8900100 && monster.getId() <= 8900102) || (monster.getId() >= 8900000 && monster.getId() <= 8900002) || (monster
                .getId() >= 8920000 && monster.getId() <= 8920003) || (monster.getId() >= 8920100 && monster.getId() <= 8920103) || monster
                .getId() == 8920106 || monster.getId() == 8900103) {
            dispel = true;
            killall = true;
            if (chr.getParty() != null) {
                int quest = 0;
                switch (monster.getId()) {
                    case 8820001:
                        quest = 3652;
                        break;
                    case 8820101:
                        quest = 3653;
                        break;
                    case 8860000:
                    case 8860005:
                        quest = 3792;
                        break;
                    case 8810018:
                    case 8810122:
                        quest = 3789;
                        break;
                    case 8840000:
                    case 8840007:
                    case 8840014:
                        quest = 3793;
                        break;
                    case 8800102:
                        quest = 15166;
                        break;
                    case 8500002:
                    case 8500012:
                        quest = 3655;
                        break;
                    case 8800002:
                        quest = 3654;
                        break;
                    case 8880200:
                        quest = 3591;
                        break;
                    case 8500022:
                        quest = 3657;
                        break;
                    case 8870000:
                        quest = 3649;
                        break;
                    case 8870100:
                        quest = 3650;
                        break;
                    case 8900103:
                        quest = 30032;
                        break;
                    case 8920106:
                        quest = 30033;
                        break;
                    case 8910100:
                        quest = 30039;
                        break;
                    case 8930100:
                        quest = 30041;
                        break;
                    case 8900000:
                    case 8900001:
                    case 8900002:
                        quest = 30043;
                        break;
                    case 8910000:
                        quest = 30044;
                        break;
                    case 8920000:
                    case 8920001:
                    case 8920002:
                    case 8920003:
                        quest = 30045;
                        break;
                    case 8930000:
                        quest = 30046;
                        break;
                    case 8850011:
                    case 8850111:
                        quest = 31199;
                        break;
                    case 8950002:
                    case 8950102:
                        quest = 33303;
                        break;
                    case 8880101:
                    case 8880111:
                        quest = 34017;
                        break;
                    case 8820212:
                        quest = 3653;
                        break;
                    case 8880302:
                    case 8880342:
                        quest = 3658;
                        break;
                    case 8880150:
                    case 8880151:
                    case 8880155:
                        quest = 3685;
                        break;
                    case 8644650:
                    case 8644655:
                        quest = 3680;
                        break;
                    case 8880405:
                        quest = 3673;
                        break;
                    case 8645009:
                    case 8645039:
                        quest = 3681;
                        break;
                    case 8880504:
                        quest = 3679;
                        break;
                    case 8880614:
                        quest = 3687;
                        break;
                }
                broadcastMessage(SLFCGPacket.ClearObstacles());
                for (MaplePartyCharacter chrs : chr.getParty().getMembers()) {
                    if (chrs.getPlayer() != null
                            && chr.getMapId() == chrs.getPlayer().getMapId() && chrs.isOnline()) {
                        FileoutputUtil.log(FileoutputUtil.보스클리어, "[클리어] 계정 번호 : " + chrs.getPlayer().getClient().getAccID() + " | 파티번호 : " + chrs.getPlayer().getParty().getId() + " | 캐릭터 : " + chrs.getPlayer().getName() + "(" + chrs.getPlayer().getId() + ") | 클리어 보스 : " + MapleLifeFactory.getMonster(monster.getId()).getStats().getName() + "(" + monster.getId() + ")");
                        if (quest != 0) {
                            if (quest == 34017) {
                                (chrs.getPlayer()).Stigma = 0;
                                Map<SecondaryStat, Pair<Integer, Integer>> dds = new HashMap<>();
                                dds.put(SecondaryStat.Stigma, new Pair<>(Integer.valueOf((chrs.getPlayer()).Stigma), Integer.valueOf(0)));
                                chrs.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(dds, chrs.getPlayer()));
                                chrs.getPlayer().getMap().broadcastMessage(chrs.getPlayer(), CWvsContext.BuffPacket.cancelForeignBuff(chrs.getPlayer(), dds), false);
                            }
                            if (chrs.getPlayer().getClient().getQuestStatus(50007) == 1) {
                                if (quest == 30033) {
                                    chrs.getPlayer().getClient().setCustomKeyValue(50007, "m1", "1");
                                } else if (quest == 30032) {
                                    chrs.getPlayer().getClient().setCustomKeyValue(50007, "m2", "1");
                                } else if (quest == 30039) {
                                    chrs.getPlayer().getClient().setCustomKeyValue(50007, "m3", "1");
                                } else if (quest == 30041) {
                                    chrs.getPlayer().getClient().setCustomKeyValue(50007, "m4", "1");
                                }
                            }
                            chrs.getPlayer().removeSkillCustomInfo(143143);
                            if (withDrops) {
                                chrs.getPlayer().updateInfoQuest(quest, "count=1;lasttime=" + today + "");
                            }
                        }
                    }
                }
            }
        } else if (monster.getId() == 8880010 || monster.getId() == 8880002 || monster.getId() == 8880000) {
            dispel = true;
            broadcastMessage(CWvsContext.getTopMsg("매그너스가 사망하여 더이상 구와르의 힘이 방출되지 않습니다."));
            if (chr.getParty() != null) {
                for (MaplePartyCharacter chrs : chr.getParty().getMembers()) {
                    //     chrs.getPlayer().checkLiveQuest(4, false);
                    chrs.getPlayer().updateInfoQuest((monster.getId() == 8880002) ? 3993 : 3992, "count=1;lasttime=" + today + "");
                }
            }
        }
        if (monster.getId() == 8880602 || monster.getId() == 8880140 || monster.getId() == 8880141 || monster.getId() == 8880142) {
            dispel = true;
            killAllMonsters(true);
        }
        if (monster.getId() == 8850111 || monster.getId() == 8850011) {
            broadcastMessage(CWvsContext.getTopMsg("시그너스를 퇴치하셨습니다. 좌측의 출구를 통해 퇴장하실 수 있습니다."));
        }
        if (monster.getId() == 8930000) {
            animation = 6;
            killMonster(8930001);
        }
        if (monster.getId() == 8880200) {
            killMonster(8880201);
            killMonster(8880202);
        }
        if (monster.getId() == 8880300 || monster.getId() == 8880303 || monster.getId() == 8880304 || monster.getId() == 8880340 || monster.getId() == 8880343 || monster.getId() == 8880344) {
            broadcastMessage(CField.enforceMSG("윌이 진지해졌네요. 거울 속 깊은 곳에 윌의 진심이 비춰질 것 같아요.", 245, 7000));
        }
        if (monster.getId() == 8880301 || monster.getId() == 8880341) {
            broadcastMessage(CField.enforceMSG("윌이 여유가 없어졌군요. 거울 세계의 가장 깊은 곳이 드러날 것 같아요.", 245, 7000));
        }
        
                for (MapleCharacter player : chr.getMap().getCharacters()) {
            if (monster.getId() == ServerConstants.worldboss) {
                player.gainItem(ServerConstants.worldreward, (short) 1);
                player.dropMessage(1, "[악용방지] : 월드보상 처치템이 지급되었습니다 엔피시를 통해 보상을 받으세요.");
            }
        }
        
        if (monster.getId() == 8644650 || monster.getId() == 8644655) {
            broadcastMessage(CField.getDestoryedBackImg("die", "dead"));
        }
        if (monster.getId() == 8880342 || monster.getId() == 8880302) {
            for (SpiderWeb web : getAllSpiderWeb()) {
                broadcastMessage(MobPacket.BossWill.willSpider(false, web));
                removeMapObject(web);
            }
            for (MapleMonster m : getAllMonster()) {
                m.killed();
            }
        }
        if (monster.getId() == 8880505) {
            killAllMonsters(true);
            broadcastMessage(CField.enforceMSG("창조와 파괴의 기사가 쓰러져 검은 마법사에게로 가는 길이 열린다.", 265, 3000));
        }
        if (monster.getId() == 8880502) {
            killAllMonsters(true);
            broadcastMessage(CField.enforceMSG("검은 마법사로부터 알 수 없는 기운이 뿜어져 나와 어둠의 왕좌를 삼킨다.", 265, 3000));
        }
        if (monster.getId() == 8880503) {
            killAllMonsters(false);
            broadcastMessage(CField.enforceMSG("압도적인 기운에 의해 주변의 모든 것이 순식간에 소멸해간다.", 265, 3000));
        }
        if (monster.getId() == 8880504) {
            killAllMonsters(false);
            broadcastMessage(SLFCGPacket.MakeBlind(new int[]{1, 255, 240, 240, 240, 1000, 0}));
        }
        if (monster.getId() == 8860002 || monster.getId() == 8860006) {
            Item toDrop = new Item(2002058, (short) 0, (short) 1, 0);
            spawnItemDrop(monster, chr, toDrop, new Point((monster.getTruePosition()).x, (monster.getTruePosition()).y), true, false);
        }
        if (monster.getId() >= 8920000 && monster.getId() <= 8920003 && withDrops) {
            RewardCheck(8920000, 8920003, 8920006, new Point(34, 135));
            dispel = true;
        } else if (monster.getId() >= 8900000 && monster.getId() <= 8900002 && withDrops) {
            RewardCheck(8900000, 8900002, 8900003, new Point(570, 551));
            dispel = true;
        } else if (monster.getId() >= 8920100 && monster.getId() <= 8920103 && withDrops) {
            RewardCheck(8920100, 8920103, 8920106, new Point(34, 135));
            dispel = true;
        } else if (monster.getId() >= 8900100 && monster.getId() <= 8900102 && withDrops) {
            RewardCheck(8900100, 8900102, 8900103, new Point(570, 551));
            dispel = true;
        } else if (monster.getId() == 8810122) {
            MapleMonster m = MapleLifeFactory.getMonster(8890000);
            spawnMonsterOnGroundBelow(m, new Point(106, 260));
            killMonsterDealy(m);
            m = MapleLifeFactory.getMonster(8890002);
            spawnMonsterOnGroundBelow(m, new Point(209, 260));
            killMonsterDealy(m);
            dispel = true;
        } else if (monster.getId() == 8820101) {
            int typeed = (int) monster.getCustomValue0(8820101) - 1;
            if (typeed > 0) {
                withDrops = false;
                MapleMonster m = MapleLifeFactory.getMonster(8820101);
                m.setCustomInfo(8820101, (int) monster.getCustomValue0(8820101) - 1, 0);
                m.setHp((typeed == 2) ? 23100000000L : ((typeed == 1) ? 33600000000L : 0L));
                m.getStats().setHp((typeed == 2) ? 23100000000L : ((typeed == 1) ? 33600000000L : 0L));
                m.setPosition(monster.getPosition());
                spawnMonsterDelay(m, -2, 2500);
            } else {
                dispel = true;
                withDrops = true;
            }
        } else if (getId() == 270050100 || getId() == 270051100) {
            int size = 0;
            for (MapleMonster m : getAllMonster()) {
                if (m.getId() != 8820014 && m.getId() != 8820000 && m.getId() != 8820009 && m.getId() != 8820114 && m.getId() != 8820100 && m.getId() != 8820109) {
                    size++;
                }
            }
            if (size == 0) {
                for (MapleMonster m : getAllMonster()) {
                    if (m.getId() == 8820008 || m.getId() == 8820000 || m.getId() == 8820014 || m.getId() == 8820108 || m.getId() == 8820100 || m.getId() == 8820114) {
                        killMonster(m, chr, withDrops, second, (byte) 1);
                    }
                }
                for (MapleMonster m : getAllMonster()) {
                    if (m.getId() == 8820008 || m.getId() == 8820000 || m.getId() == 8820014 || m.getId() == 8820108 || m.getId() == 8820100 || m.getId() == 8820114) {
                        killMonster(m, chr, withDrops, second, (byte) 1);
                    }
                }
            }
        } else if (monster.getId() == 8820002 || monster.getId() == 8860005) {
            dispel = true;
        } else if (monster.getId() == 8880111 || monster.getId() == 8880101) {
            dispel = true;
        }
        if (dispel) {
            for (MapleCharacter chr2 : getAllChracater()) {
                chr2.dispelDebuffs();
            }
        }
        if (monster.getId() == 9390612 || monster.getId() == 9390610 || monster.getId() == 9390611 || monster.getId() == 8645066) {
            MapleCharacter pchr = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getParty().getLeader().getId());
            if (pchr == null) {
                chr.dropMessage(5, "파티의 상태가 변경되어 골럭스 원정대가 해체됩니다.");
                chr.setKeyValue(200106, "golrux_in", "0");
            } else if (pchr.getKeyValue(200106, "golrux_in") == 1L && pchr.getKeyValue(200106, "golrux_enter") == 1L) {
                if (pchr.getId() != chr.getId()) {
                    chr.setKeyValue(200106, "golrux_clear", "1");
                    pchr.setKeyValue(200106, "golrux_clear", "1");
                } else {
                    chr.setKeyValue(200106, "golrux_clear", "1");
                }
                chr.dropMessage(5, "골럭스의 일부분을 처치하셨습니다.");
            }
        }
        int[] linkMobs = {
            9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161,
            9010162, 9010163, 9010164, 9010165, 9010166, 9010167, 9010168, 9010169, 9010170, 9010171,
            9010172, 9010173, 9010174, 9010175, 9010176, 9010177, 9010178, 9010179, 9010180, 9010181};
        for (int linkMob : linkMobs) {
            if (monster.getId() == linkMob) {
                if (chr.getLinkMobCount() <= 0) {
                    for (MapleMapObject monstermo : getAllMonstersThreadsafe()) {
                        MapleMonster mob = (MapleMonster) monstermo;
                        if (mob.getOwner() == chr.getId()) {
                            mob.setHp(0L);
                            broadcastMessage(MobPacket.killMonster(mob.getObjectId(), 1));
                            removeMapObject(mob);
                            mob.killed();
                        }
                    }
                    break;
                }
                MapleMonster newMob = MapleLifeFactory.getMonster(monster.getId());
                newMob.setOwner(chr.getId());
                spawnMonsterOnGroundBelow(newMob, monster.getTruePosition());
                break;
            }
        }
        if (chr.getMap().getId() / 10000 == 92507 && getAllMonster().size() <= 0) {
            int floor = (chr.getMapId() - 925070000) / 100;
            chr.removeSkillCustomInfo(92507);
            if (floor >= 31 && floor <= 39) {
                chr.setSkillCustomInfo(92507000, chr.getSkillCustomValue0(92507000) + 1L, 0L);
                if (chr.getSkillCustomValue0(92507000) >= ((floor == 37 || floor == 38 || floor == 39) ? 3L : 2L)) {
                    chr.removeSkillCustomInfo(92507000);
                    for (MapleMapObject reactor1l : chr.getMap().getAllReactorsThreadsafe()) {
                        MapleReactor reactor2l = (MapleReactor) reactor1l;
                        if (reactor2l.getReactorId() == 2508000 && reactor2l.getState() == 0) {
                            reactor2l.forceHitReactor((byte) 1, chr.getId());
                            break;
                        }
                    }
                    chr.setDojoStop(true);
                    chr.removeSkillCustomInfo(92507);
                    chr.setKeyValue(3, "dojo_time", String.valueOf(chr.getDojoStartTime()));
                    chr.getClient().getSession().writeAndFlush(CWvsContext.getTopMsg("상대를 격파하였습니다. 10초간 타이머가 정지됩니다."));
                    chr.getClient().getSession().writeAndFlush(CWvsContext.getTopMsg("포탈을 통해 다음 스테이지로 이동해야 클리어 기록이 랭킹에 등록 됩니다."));
                    chr.getClient().getSession().writeAndFlush(CField.getDojoClockStop(true, 900 - chr.getDojoStartTime()));
                    chr.getClient().getSession().writeAndFlush(CField.environmentChange("Dojang/cleard", 5));
                    chr.getClient().getSession().writeAndFlush(CField.environmentChange("dojang/end/clear", 19));
                    chr.getStat().heal(chr);
                    chr.MulungTimer = new java.util.Timer();
                    chr.MulungTimerTask = new TimerTask() {
                        public void run() {
                            chr.setDojoStop(false);
                            if (chr.getMap().getId() / 10000 == 92507) {
                                chr.getClient().getSession().writeAndFlush(CField.getDojoClockStop(false, 900 - chr.getDojoStartTime()));
                            }
                            chr.MulungTimer = null;
                            chr.cancelTimer();
                        }
                    };
                    chr.MulungTimer.schedule(chr.MulungTimerTask, 10000L);
                } else {
                    Timer.EtcTimer.getInstance().schedule(() -> {
                        MapleMonster mob2 = MapleLifeFactory.getMonster(monster.getId());
                        mob2.setHp(monster.getMobMaxHp());
                        mob2.getStats().setHp(monster.getMobMaxHp());
                        chr.getMap().spawnMonsterWithEffectBelow(mob2, monster.getPosition(), -1);
                    }, 1500L);
                }
            } else {
                for (MapleMapObject reactor1l : chr.getMap().getAllReactorsThreadsafe()) {
                    MapleReactor reactor2l = (MapleReactor) reactor1l;
                    if (reactor2l.getReactorId() == 2508000 && reactor2l.getState() == 0) {
                        reactor2l.forceHitReactor((byte) 1, chr.getId());
                        break;
                    }
                }
                if (floor == 1 || floor == 13 || floor == 34 || floor == 35 || floor == 38 || floor == 39 || floor == 43 || floor == 52 || floor == 54) {
                    int mapid = chr.getMapId();
                    chr.getClient().send(SLFCGPacket.OnBomb(176, 39, monster.getPosition()));
                    Timer.EtcTimer.getInstance().schedule(() -> {
                        if ((monster.getPosition()).x + 150 > (chr.getPosition()).x && (monster.getPosition()).x - 150 < (chr.getPosition()).x && mapid == chr.getMapId()) {
                            chr.addHP(-(chr.getStat().getCurrentMaxHp() / 100L) * 10L);
                            chr.getClient().send(CField.DamagePlayer2((int) (chr.getStat().getCurrentMaxHp() / 100L) * 10));
                            chr.getClient().send(CField.EffectPacket.showEffect(chr, 0, 176, 45, 39, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
                        }
                    }, 2300L);
                }
                chr.setDojoStop(true);
                chr.setKeyValue(3, "dojo_time", String.valueOf(chr.getDojoStartTime()));
                chr.getClient().getSession().writeAndFlush(CWvsContext.getTopMsg("포탈을 통해 다음 스테이지로 이동해야 클리어 기록이 랭킹에 등록 됩니다."));
                chr.getClient().getSession().writeAndFlush(CWvsContext.getTopMsg("상대를 격파하였습니다. 10초간 타이머가 정지됩니다."));
                chr.getClient().getSession().writeAndFlush(CField.getDojoClockStop(true, 900 - chr.getDojoStartTime()));
                chr.getClient().getSession().writeAndFlush(CField.environmentChange("Dojang/cleard", 5));
                chr.getClient().getSession().writeAndFlush(CField.environmentChange("dojang/end/clear", 19));
                chr.getStat().heal(chr);
                chr.MulungTimer = new java.util.Timer();
                chr.MulungTimerTask = new TimerTask() {
                    public void run() {
                        chr.setDojoStop(false);
                        if (chr.getMap().getId() / 10000 == 92507) {
                            chr.getClient().getSession().writeAndFlush(CField.getDojoClockStop(false, 900 - chr.getDojoStartTime()));
                        }
                        chr.MulungTimer = null;
                        chr.cancelTimer();
                    }
                };
                chr.MulungTimer.schedule(chr.MulungTimerTask, 10000L);
            }
        }
        if (chr.getParty() != null
                && GameConstants.price.containsKey(Integer.valueOf(monster.getId()))) {
            for (MaplePartyCharacter pc : chr.getParty().getMembers()) {
                MapleCharacter pz = chr.getClient().getChannelServer().getPlayerStorage().getCharacterByName(pc.getName());
                if (pz != null) {
                    pz.setLastBossId(monster.getId());
                }
            }
        }
        if (monster.getBuffToGive() > -1) {
            int buffid = monster.getBuffToGive();
            SecondaryStatEffect buff = MapleItemInformationProvider.getInstance().getItemEffect(buffid);
            Iterator<MapleCharacter> itr = this.characters.iterator();
            while (itr.hasNext()) {
                MapleCharacter mc = itr.next();
                if (mc.isAlive()) {
                    buff.applyTo(mc, true);
                    switch (monster.getId()) {
                        case 8810018:
                        case 8810122:
                        case 8810214:
                        case 8820001:
                            mc.getClient().getSession().writeAndFlush(CField.EffectPacket.showNormalEffect(mc, 14, true));
                            broadcastMessage(mc, CField.EffectPacket.showNormalEffect(mc, 14, false), false);
                    }
                }
            }
        }
        int mobid = monster.getId();
        
         if (mobid == ServerConstants.worldboss && chr.getMapId() == ServerConstants.worldbossmap) {
            chr.sethottimebosslastattack(true);
            MapleNPC npc = MapleLifeFactory.getNPC(ServerConstants.worldNpc);
            npc.setPosition(monster.getPosition());
            npc.setCy(monster.getPosition().y);
            npc.setRx0(monster.getPosition().x + 50);
            npc.setRx1(monster.getPosition().x - 50);
            npc.setFh(monster.getMap().getFootholds().findBelow(chr.getPosition()).getId());
            npc.setCustom(true);
            chr.getMap().addMapObject(npc);
            chr.getMap().broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
            chr.getMap().broadcastMessage(CWvsContext.serverNotice(6,"", "5분내로 엔피시를 눌러 보상을 받지않으면 보상을 받지 못합니다."));
            final MapleMap mapto = chr.getClient().getChannelServer().getMapFactory().getMap(ServerConstants.worldbossfirstmap);
            for (MapleCharacter cc : chr.getMap().getCharacters()) {
                cc.startMapTimeLimitTask(300, mapto);
            }
        }
                  
        
        if (!monster.getStats().isBoss() && chr.getBuffedValue(SecondaryStat.Reincarnation) != null
                && chr.getReinCarnation() > 0) {
            chr.setReinCarnation(chr.getReinCarnation() - 1);
            if (chr.getReinCarnation() == 0) {
                chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, 1320019, 1, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getPosition(), null, null));
                chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 1320019, 1, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
            }
            Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
            statups.put(SecondaryStat.Reincarnation, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.Reincarnation)))));
            chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(SecondaryStat.Reincarnation), chr));
        }
        if (mobid / 100000 == 98 && chr.getMapId() / 10000000 == 95 && getNumMonsters() == 0) {
            switch (chr.getMapId() % 1000 / 100) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    chr.getClient().getSession().writeAndFlush(CField.showEffect("monsterPark/clear"));
                    chr.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Party1/Cleard"));
                    break;
                case 5:
                    if (chr.getMapId() % 1000 == 500) {
                        chr.getClient().getSession().writeAndFlush(CField.showEffect("monsterPark/clearF"));
                        chr.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Party1/Cleard"));
                        chr.dropMessage(-1, "모든 스테이지를 클리어 하셨습니다. 포탈을 통해 밖으로 이동해주세요.");
                    }
                    break;
                case 6:
                    chr.getClient().getSession().writeAndFlush(CField.showEffect("monsterPark/clearF"));
                    break;
            }
        }
        if (this.rune == null && !chr.getBuffedValue(80002282) && !isTown() && getAllMonster().size() > 0 && isSpawnPoint() && !GameConstants.isContentsMap(getId()) && !GameConstants.보스맵(getId()) && !GameConstants.사냥컨텐츠맵(getId()) && !GameConstants.튜토리얼(getId()) && !GameConstants.로미오줄리엣(getId()) && !GameConstants.피라미드(getId())) {
            int sppoint = Randomizer.rand(0, this.monsterSpawn.size() - 1);
            MapleReactor ract = getAllReactor().get(0);
            if (ract != null) {
                while (sppoint == ract.getSpawnPointNum()) {
                    sppoint = Randomizer.rand(0, this.monsterSpawn.size() - 1);
                }
            }
            Point poss = ((Spawns) this.monsterSpawn.get(sppoint)).getPosition();
            MapleRune rune = new MapleRune(Randomizer.rand(0, 10), poss.x, poss.y, this);
            rune.setSpawnPointNum(sppoint);
            spawnRune(rune);
        }
        if (chr.getV("bossPractice") != null
                && Integer.parseInt(chr.getV("bossPractice")) == 1) {
            withDrops = false;
        }
        if (withDrops) {
            dropFromMonster(chr, monster, instanced);
        }
        if (monster.getEventInstance() != null) {
            monster.getEventInstance().monsterKilled(chr, monster);
        } else {
            EventInstanceManager em = chr.getEventInstance();
            if (em != null) {
                em.monsterKilled(chr, monster);
            }
        }
    }

    public List<MapleReactor> getAllReactor() {
        return this.getAllReactorsThreadsafe();
    }

    public List<MapleReactor> getAllReactorsThreadsafe() {
        ArrayList<MapleReactor> ret = new ArrayList<MapleReactor>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.REACTOR).values()) {
            ret.add((MapleReactor) mmo);
        }
        return ret;
    }

    public List<MapleSummon> getAllSummonsThreadsafe() {
        ArrayList<MapleSummon> ret = new ArrayList<MapleSummon>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.SUMMON).values()) {
            if (!(mmo instanceof MapleSummon)) {
                continue;
            }
            ret.add((MapleSummon) mmo);
        }
        return ret;
    }

    public List<MapleSummon> getAllSummons(int skillId) {
        ArrayList<MapleSummon> ret = new ArrayList<MapleSummon>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.SUMMON).values()) {
            if (!(mmo instanceof MapleSummon) || ((MapleSummon) mmo).getSkill() != skillId) {
                continue;
            }
            ret.add((MapleSummon) mmo);
        }
        return ret;
    }

    public List<MapleFlyingSword> getAllFlyingSwordsThreadsafe() {
        ArrayList<MapleFlyingSword> ret = new ArrayList<MapleFlyingSword>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.SWORD).values()) {
            ret.add((MapleFlyingSword) mmo);
        }
        return ret;
    }

    public List<MapleMapObject> getAllDoor() {
        return this.getAllDoorsThreadsafe();
    }

    public List<MapleMapObject> getAllDoorsThreadsafe() {
        ArrayList<MapleMapObject> ret = new ArrayList<MapleMapObject>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.DOOR).values()) {
            if (!(mmo instanceof MapleDoor)) {
                continue;
            }
            ret.add(mmo);
        }
        return ret;
    }

    public List<MapleMapObject> getAllMechDoorsThreadsafe() {
        ArrayList<MapleMapObject> ret = new ArrayList<MapleMapObject>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.DOOR).values()) {
            if (!(mmo instanceof MechDoor)) {
                continue;
            }
            ret.add(mmo);
        }
        return ret;
    }

    public List<MapleMapObject> getAllMerchant() {
        return this.getAllHiredMerchantsThreadsafe();
    }

    public List<MapleMapObject> getAllHiredMerchantsThreadsafe() {
        ArrayList<MapleMapObject> ret = new ArrayList<MapleMapObject>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.HIRED_MERCHANT).values()) {
            ret.add(mmo);
        }
        return ret;
    }

    public List<MapleSpecialChair> getAllSpecialChairs() {
        ArrayList<MapleSpecialChair> ret = new ArrayList<MapleSpecialChair>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.SPECIAL_CHAIR).values()) {
            ret.add((MapleSpecialChair) mmo);
        }
        return ret;
    }

    public List<MapleCharacter> getAllChracater() {
        return this.getAllCharactersThreadsafe();
    }

    public List<MapleCharacter> getAllCharactersThreadsafe() {
        ArrayList<MapleCharacter> ret = new ArrayList<MapleCharacter>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.PLAYER).values()) {
            ret.add((MapleCharacter) mmo);
        }
        return ret;
    }

    public List<MapleMagicSword> getAllMagicSword() {
        ArrayList<MapleMagicSword> ret = new ArrayList<MapleMagicSword>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.MagicSword).values()) {
            ret.add((MapleMagicSword) mmo);
        }
        return ret;
    }

    public MapleRandomPortal getPoloFrittoPortal() {
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.RANDOM_PORTAL).values()) {
            MapleRandomPortal p = (MapleRandomPortal) mmo;
            if (p.getPortalType() != 2) {
                continue;
            }
            return p;
        }
        return null;
    }

    public MapleRandomPortal getFireWolfPortal() {
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.RANDOM_PORTAL).values()) {
            MapleRandomPortal p = (MapleRandomPortal) mmo;
            if (p.getPortalType() != 3) {
                continue;
            }
            return p;
        }
        return null;
    }

    public SecondAtom getSecondAtom(int playerId, int skillId) {
        for (SecondAtom atom : this.getAllSecondAtomsThreadsafe()) {
            if (atom == null || atom.getOwnerId() != playerId || atom.getSkillId() != skillId) {
                continue;
            }
            return atom;
        }
        return null;
    }

    public SecondAtom getSecondAtomOid(int playerId, int oid) {
        for (SecondAtom atom : this.getAllSecondAtomsThreadsafe()) {
            if (atom == null || atom.getOwnerId() != playerId || atom.getObjectId() != oid) {
                continue;
            }
            return atom;
        }
        return null;
    }

    public List<SecondAtom> getSecondAtoms(int playerId, int skillId) {
        ArrayList<SecondAtom> ret = new ArrayList<SecondAtom>();
        for (SecondAtom atom : this.getAllSecondAtomsThreadsafe()) {
            if (atom == null || atom.getOwnerId() != playerId || atom.getSkillId() != skillId) {
                continue;
            }
            ret.add(atom);
        }
        return ret;
    }

    public List<SecondAtom> getAllSecondAtomsThreadsafe() {
        ArrayList<SecondAtom> ret = new ArrayList<SecondAtom>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.A_SECOND_ATOM).values()) {
            ret.add((SecondAtom) mmo);
        }
        return ret;
    }

    public List<SecondAtom> getAllSecondAtomsThread() {
        ArrayList<SecondAtom> ret = new ArrayList<SecondAtom>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.A_SECOND_ATOM).values()) {
            ret.add((SecondAtom) mmo);
        }
        return ret;
    }

    public List<MapleMonster> getAllMonster() {
        return this.getAllMonstersThreadsafe();
    }

    public List<MapleMonster> getAllMonstersThreadsafe() {
        ArrayList<MapleMonster> ret = new ArrayList<MapleMonster>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.MONSTER).values()) {
            ret.add((MapleMonster) mmo);
        }
        return ret;
    }

    public List<MapleMonster> getAllNormalMonstersThreadsafe() {
        ArrayList<MapleMonster> ret = new ArrayList<MapleMonster>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.MONSTER).values()) {
            MapleMonster monster = (MapleMonster) mmo;
            if (monster.getStats().isBoss()) {
                continue;
            }
            ret.add(monster);
        }
        return ret;
    }

    public List<Integer> getAllUniqueMonsters() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (MapleMapObject mmo : this.mapobjects.get((Object) MapleMapObjectType.MONSTER).values()) {
            int theId = ((MapleMonster) mmo).getId();
            if (ret.contains(theId)) {
                continue;
            }
            ret.add(theId);
        }
        return ret;
    }

    public final void killAllMonsters(boolean animate) {
        for (MapleMapObject mapleMapObject : this.getAllMonstersThreadsafe()) {
            MapleMonster monster = (MapleMonster) mapleMapObject;
            if (this.RealSpawns.contains(monster)) {
                this.RealSpawns.remove(monster);
            }
            monster.setHp(0L);
            this.broadcastMessage(MobPacket.killMonster(monster.getObjectId(), animate ? 1 : 0));
            this.broadcastMessage(MobPacket.stopControllingMonster(monster.getObjectId()));
            this.removeMapObject(monster);
            monster.killed();
            this.spawnedMonstersOnMap.decrementAndGet();
        }
    }

    public final void killAllMonsters(MapleCharacter chr) {
        for (MapleMapObject mapleMapObject : this.getAllMonstersThreadsafe()) {
            MapleMonster monster = (MapleMonster) mapleMapObject;
            if (monster.getOwner() != chr.getId()) {
                continue;
            }
            if (this.RealSpawns.contains(monster)) {
                this.RealSpawns.remove(monster);
            }
            monster.setHp(0L);
            this.broadcastMessage(MobPacket.killMonster(monster.getObjectId(), 0));
            this.broadcastMessage(MobPacket.stopControllingMonster(monster.getObjectId()));
            this.removeMapObject(monster);
            monster.killed();
            this.spawnedMonstersOnMap.decrementAndGet();
        }
    }

    public final void killMonster(int monsId) {
        for (MapleMapObject mapleMapObject : this.getAllMonstersThreadsafe()) {
            MapleMonster mob = (MapleMonster) mapleMapObject;
            if (mob.getId() != monsId) {
                continue;
            }
            if (this.RealSpawns.contains(mob)) {
                this.RealSpawns.remove(mob);
            }
            mob.setHp(0L);
            this.broadcastMessage(MobPacket.killMonster(mob.getObjectId(), 1));
            this.broadcastMessage(MobPacket.stopControllingMonster(mob.getObjectId()));
            this.removeMapObject(mob);
            mob.killed();
            this.spawnedMonstersOnMap.decrementAndGet();
        }
    }

    public final void killMonsterDealy(MapleMonster mob) {
        if (mob != null && mob.isAlive()) {
            if (this.RealSpawns.contains(mob)) {
                this.RealSpawns.remove(mob);
            }
            mob.setHp(0L);
            this.broadcastMessage(MobPacket.killMonster(mob.getObjectId(), 1));
            this.broadcastMessage(MobPacket.stopControllingMonster(mob.getObjectId()));
            this.removeMapObject(mob);
            mob.killed();
            this.spawnedMonstersOnMap.decrementAndGet();
        }
    }

    public final void killMonsterType(MapleMonster mob, int type) {
        if (mob != null && mob.isAlive()) {
            if (this.RealSpawns.contains(mob)) {
                this.RealSpawns.remove(mob);
            }
            this.removeMapObject(mob);
            mob.killed();
            this.spawnedMonstersOnMap.decrementAndGet();
            this.broadcastMessage(MobPacket.killMonster(mob.getObjectId(), type));
            this.broadcastMessage(MobPacket.stopControllingMonster(mob.getObjectId()));
        }
    }

    public final void limitReactor(final int rid, final int num) {
        final List<MapleReactor> toDestroy = new ArrayList<MapleReactor>();
        final Map<Integer, Integer> contained = new LinkedHashMap<Integer, Integer>();
        for (final MapleMapObject obj : this.mapobjects.get(MapleMapObjectType.REACTOR).values()) {
            final MapleReactor mr = (MapleReactor) obj;
            if (contained.containsKey(mr.getReactorId())) {
                if (contained.get(mr.getReactorId()) >= num) {
                    toDestroy.add(mr);
                } else {
                    contained.put(mr.getReactorId(), contained.get(mr.getReactorId()) + 1);
                }
            } else {
                contained.put(mr.getReactorId(), 1);
            }
        }
        for (final MapleReactor mr2 : toDestroy) {
            this.destroyReactor(mr2.getObjectId());
        }
    }

    public final void destroyReactors(int first, int last) {
        ArrayList<MapleReactor> toDestroy = new ArrayList<MapleReactor>();
        for (MapleMapObject obj : this.mapobjects.get((Object) MapleMapObjectType.REACTOR).values()) {
            MapleReactor mr = (MapleReactor) obj;
            if (mr.getReactorId() < first || mr.getReactorId() > last) {
                continue;
            }
            toDestroy.add(mr);
        }
        for (MapleReactor mr : toDestroy) {
            this.destroyReactor(mr.getObjectId());
        }
    }

    public final void destroyReactor(int oid) {
        final MapleReactor reactor = this.getReactorByOid(oid);
        if (reactor == null) {
            return;
        }
        this.broadcastMessage(CField.destroyReactor(reactor));
        reactor.setAlive(false);
        this.removeMapObject(reactor);
        reactor.setTimerActive(false);
        if (reactor.getDelay() > 0) {
            Timer.MapTimer.getInstance().schedule(new Runnable() {

                @Override
                public final void run() {
                    MapleMap.this.respawnReactor(reactor);
                }
            }, reactor.getDelay());
        }
    }

    public final void reloadReactors() {
        final List<MapleReactor> toSpawn = new ArrayList<MapleReactor>();
        for (final MapleMapObject obj : this.mapobjects.get(MapleMapObjectType.REACTOR).values()) {
            final MapleReactor reactor = (MapleReactor) obj;
            this.broadcastMessage(CField.destroyReactor(reactor));
            reactor.setAlive(false);
            reactor.setTimerActive(false);
            toSpawn.add(reactor);
        }
        for (final MapleReactor r : toSpawn) {
            this.removeMapObject(r);
            this.respawnReactor(r);
        }
    }

    public final void resetReactors(final MapleClient c) {
        this.setReactorState((byte) 0, c);
    }

    public final void setReactorState(final MapleClient c) {
        this.setReactorState((byte) 1, c);
    }

    public final void setReactorState(final byte state, final MapleClient c) {
        for (final MapleMapObject obj : this.mapobjects.get(MapleMapObjectType.REACTOR).values()) {
            ((MapleReactor) obj).forceHitReactor(state, (c == null) ? 0 : c.getPlayer().getId());
        }
    }

    public final void setReactorDelay(final int state) {
        for (final MapleMapObject obj : this.mapobjects.get(MapleMapObjectType.REACTOR).values()) {
            ((MapleReactor) obj).setDelay(state);
        }
    }

    public final void shuffleReactors() {
        shuffleReactors(0, 9999999);
    }

    public final void shuffleReactors(final int first, final int last) {
        final List<Point> points = new ArrayList<Point>();
        for (final MapleMapObject obj : this.mapobjects.get(MapleMapObjectType.REACTOR).values()) {
            final MapleReactor mr = (MapleReactor) obj;
            if (mr.getReactorId() >= first && mr.getReactorId() <= last) {
                points.add(mr.getPosition());
            }
        }
        Collections.shuffle(points);
        for (final MapleMapObject obj : this.mapobjects.get(MapleMapObjectType.REACTOR).values()) {
            final MapleReactor mr = (MapleReactor) obj;
            if (mr.getReactorId() >= first && mr.getReactorId() <= last) {
                mr.setPosition(points.remove(points.size() - 1));
            }
        }
    }

    public final void updateMonsterController(MapleMonster monster) {
        if (!monster.isAlive() || monster.getLinkCID() > 0 || monster.getStats().isEscort()) {
            return;
        }
        if (monster.getController() != null) {
            if (monster.getController().getMap() != this || monster.getController().getTruePosition().distanceSq(monster.getTruePosition()) > monster.getRange()) {
                monster.getController().stopControllingMonster(monster);
            } else {
                return;
            }
        }
        if (monster.getStats().isMobZone()) {
            byte phase;
            if (monster.getHPPercent() > 75) {
                phase = 1;
            } else if (monster.getHPPercent() > 50) {
                phase = 2;
            } else if (monster.getHPPercent() > 25) {
                phase = 3;
            } else {
                phase = 4;
            }
            if (monster.getPhase() != phase) {
                monster.setPhase(phase);
                broadcastMessage(MobPacket.changePhase(monster));
                broadcastMessage(MobPacket.changeMobZone(monster));
            }
        }
        int mincontrolled = -1;
        MapleCharacter newController = null;
        Iterator<MapleCharacter> ltr = this.characters.iterator();
        while (ltr.hasNext()) {
            MapleCharacter chr = ltr.next();
            if (!chr.isHidden() && (chr.getControlledSize() < mincontrolled || mincontrolled == -1) && chr.getTruePosition().distanceSq(monster.getTruePosition()) <= monster.getRange()) {
                if (monster.getOwner() == -1) {
                    mincontrolled = chr.getControlledSize();
                    newController = chr;
                    continue;
                }
                if (monster.getOwner() == chr.getId()) {
                    mincontrolled = chr.getControlledSize();
                    newController = chr;
                }
            }
        }
        if (newController != null) {
            if (monster.isFirstAttack()) {
                newController.controlMonster(monster, true);
                monster.setControllerHasAggro(true);
            } else {
                newController.controlMonster(monster, false);
            }
        }
    }

    public final MapleMapObject getMapObject(int oid, MapleMapObjectType type) {
        return (MapleMapObject) ((ConcurrentHashMap) this.mapobjects.get(type)).get(Integer.valueOf(oid));
    }

    public final boolean containsNPC(int npcid) {
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.NPC)).values().iterator();
        while (itr.hasNext()) {
            MapleNPC n = (MapleNPC) itr.next();
            if (n.getId() == npcid) {
                return true;
            }
        }
        return false;
    }

    public MapleNPC getNPCById(int id) {
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.NPC)).values().iterator();
        while (itr.hasNext()) {
            MapleNPC n = (MapleNPC) itr.next();
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    public MapleMonster getMonsterById(int id) {
        MapleMonster ret = null;
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.MONSTER)).values().iterator();
        while (itr.hasNext()) {
            MapleMonster n = (MapleMonster) itr.next();
            if (n.getId() == id) {
                ret = n;
                break;
            }
        }
        return ret;
    }

    public int countOrgelById(boolean purple) {
        int ret = 0;
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.MONSTER)).values().iterator();
        while (itr.hasNext()) {
            MapleMonster n = (MapleMonster) itr.next();
            if (n.getId() / 10 == (purple ? 983308 : 983307)) {
                ret++;
            }
        }
        return ret;
    }

    public int countMonsterById(int id) {
        int ret = 0;
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.MONSTER)).values().iterator();
        while (itr.hasNext()) {
            MapleMonster n = (MapleMonster) itr.next();
            if (n.getId() == id) {
                ret++;
            }
        }
        return ret;
    }

    public MapleReactor getReactorById(int id) {
        MapleReactor ret = null;
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.REACTOR)).values().iterator();
        while (itr.hasNext()) {
            MapleReactor n = (MapleReactor) itr.next();
            if (n.getReactorId() == id) {
                ret = n;
                break;
            }
        }
        return ret;
    }

    public final MapleMonster getMonsterByOid(int oid) {
        MapleMapObject mmo = getMapObject(oid, MapleMapObjectType.MONSTER);
        if (mmo == null) {
            return null;
        }
        return (MapleMonster) mmo;
    }

    public final MapleSummon getSummonByOid(int oid) {
        MapleMapObject mmo = getMapObject(oid, MapleMapObjectType.SUMMON);
        if (mmo == null) {
            return null;
        }
        return (MapleSummon) mmo;
    }

    public final MapleNPC getNPCByOid(int oid) {
        MapleMapObject mmo = getMapObject(oid, MapleMapObjectType.NPC);
        if (mmo == null) {
            return null;
        }
        return (MapleNPC) mmo;
    }

    public final MapleReactor getReactorByOid(int oid) {
        MapleMapObject mmo = getMapObject(oid, MapleMapObjectType.REACTOR);
        if (mmo == null) {
            return null;
        }
        return (MapleReactor) mmo;
    }

    public final MapleReactor getReactorByName(String name) {
        for (MapleMapObject obj : this.mapobjects.get((Object) MapleMapObjectType.REACTOR).values()) {
            MapleReactor mr = (MapleReactor) obj;
            if (!mr.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return mr;
        }
        return null;
    }

    public final void spawnNpc(int id, Point pos) {
        MapleNPC npc = MapleLifeFactory.getNPC(id);
        npc.setPosition(pos);
        npc.setCy(pos.y);
        npc.setRx0(pos.x + 50);
        npc.setRx1(pos.x - 50);
        npc.setFh(getFootholds().findBelow(pos).getId());
        npc.setCustom(true);
        addMapObject(npc);
        broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
    }

    public final void removeNpc(int npcid) {
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.NPC)).values().iterator();
        while (itr.hasNext()) {
            MapleNPC npc = (MapleNPC) itr.next();
            if (npc.isCustom() && (npcid == -1 || npc.getId() == npcid)) {
                broadcastMessage(CField.NPCPacket.removeNPCController(npc.getObjectId()));
                broadcastMessage(CField.NPCPacket.removeNPC(npc.getObjectId()));
                itr.remove();
            }
        }
    }

    public final void hideNpc(int npcid) {
        Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.NPC)).values().iterator();
        while (itr.hasNext()) {
            MapleNPC npc = (MapleNPC) itr.next();
            if (npcid == -1 || npc.getId() == npcid) {
                broadcastMessage(CField.NPCPacket.removeNPCController(npc.getObjectId()));
                broadcastMessage(CField.NPCPacket.removeNPC(npc.getObjectId()));
            }
        }
    }

    public final void spawnReactorOnGroundBelow(MapleReactor mob, Point pos) {
        mob.setPosition(pos);
        mob.setCustom(true);
        spawnReactor(mob);
    }

    public final void spawnMonster_sSack(MapleMonster mob, Point pos, int spawnType) {
        mob.setPosition((calcPointBelow(new Point(pos.x, pos.y - 1)) == null) ? new Point(pos.x, pos.y) : calcPointBelow(new Point(pos.x, pos.y - 1)));
        spawnMonster(mob, spawnType);
    }

    public final void spawnMonsterOnGroundBelow(MapleMonster mob, Point pos) {
        spawnMonster_sSack(mob, pos, (mob.getId() == 8880512) ? 1 : -2);
    }

    public final void spawnMonsterOnGroundBelowBlackMage(MapleMonster mob, Point pos) {
        mob.setFh(3);
        mob.setF(3);
        spawnMonster_sSack(mob, pos, (mob.getId() == 8880512) ? 1 : -2);
    }

    public final int spawnMonsterWithEffectBelow(MapleMonster mob, Point pos, int effect) {
        Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        return spawnMonsterWithEffect(mob, effect, spos);
    }

    public final void spawnZakum(MapleCharacter chr, int x, int y) {
        Point pos = new Point(x, y);
        MapleMonster mainb = MapleLifeFactory.getMonster(8800002);
        Point spos = calcPointBelow(new Point(pos.x, pos.y));
        EventInstanceManager eim = chr.getEventInstance();
        if (eim != null) {
            int[] zakpart = {8800003, 8800004, 8800005, 8800006, 8800007, 8800008, 8800009, 8800010};
            for (int i : zakpart) {
                MapleMonster part = MapleLifeFactory.getMonster(i);
                part.setF(1);
                part.setFh(1);
                part.setStance(5);
                part.setPhase((byte) 1);
                eim.registerMonster(part);
                eim.getMapInstance(0).spawnMonsterWithEffectBelow(part, spos, -2);
                part.getMap().broadcastMessage(MobPacket.changePhase(part));
            }
            mainb.setCustomInfo(8800002, 0, 3000);
            mainb.setCustomInfo(8800003, 2, 0);
            mainb.setPhase((byte) 1);
            mainb.setSpecialtxt("properties");
            eim.registerMonster(mainb);
            eim.getMapInstance(0).spawnMonsterWithEffectBelow(mainb, spos, -1);
            broadcastMessage(MobPacket.setMonsterProPerties(mainb.getObjectId(), 1, 0, 0));
            broadcastMessage(MobPacket.getSmartNotice(mainb.getId(), 0, 5, 1, "자쿰이 팔을 들어 내려칠 준비를 합니다"));
            mainb.getMap().broadcastMessage(MobPacket.changePhase(mainb));
        }
    }

    public final void spawnChaosZakum(MapleCharacter chr, int x, int y) {
        Point pos = new Point(x, y);
        MapleMonster mainb = MapleLifeFactory.getMonster(8800102);
        Point spos = calcPointBelow(new Point(pos.x, pos.y));
        EventInstanceManager eim = chr.getEventInstance();
        if (eim != null) {
            int[] zakpart = {8800103, 8800104, 8800105, 8800106, 8800107, 8800108, 8800109, 8800110};
            for (int i : zakpart) {
                MapleMonster part = MapleLifeFactory.getMonster(i);
                part.setF(1);
                part.setFh(1);
                part.setStance(5);
                part.setPhase((byte) 1);
                eim.registerMonster(part);
                eim.getMapInstance(0).spawnMonsterWithEffectBelow(part, spos, -2);
                part.getMap().broadcastMessage(MobPacket.changePhase(part));
            }
            mainb.setCustomInfo(8800002, 0, 3000);
            mainb.setCustomInfo(8800003, 2, 0);
            mainb.setPhase((byte) 1);
            mainb.setSpecialtxt("properties");
            eim.registerMonster(mainb);
            eim.getMapInstance(0).spawnMonsterWithEffectBelow(mainb, spos, -1);
            broadcastMessage(MobPacket.setMonsterProPerties(mainb.getObjectId(), 1, 0, 0));
            broadcastMessage(MobPacket.getSmartNotice(mainb.getId(), 0, 5, 1, "자쿰이 팔을 들어 내려칠 준비를 합니다"));
            mainb.getMap().broadcastMessage(MobPacket.changePhase(mainb));
        }
    }

    public final void spawnFakeMonsterOnGroundBelow(MapleMonster mob, Point pos) {
        Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        spos.y--;
        mob.setPosition(spos);
        spawnFakeMonster(mob);
    }

    private void checkRemoveAfter(MapleMonster monster) {
        int ra = monster.getStats().getRemoveAfter();
        if (ra > 0 && monster.getLinkCID() <= 0) {
            monster.registerKill((ra * 1000));
        }
    }

    public final void spawnRevives(final MapleMonster monster, final int oid) {
        monster.setMap(this);
        checkRemoveAfter(monster);
        monster.setLinkOid(oid);
        if (monster.getHp() <= 0L) {
            MapleMonster mob = MapleLifeFactory.getMonster(monster.getId());
            if (mob != null) {
                monster.setHp(mob.getStats().getHp());
                monster.getStats().setHp(mob.getStats().getHp());
            }
        }
        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {
            public final void sendPackets(MapleClient c) {
                if (monster.getOwner() == -1) {
                    c.getSession().writeAndFlush(MobPacket.spawnMonster(monster, (monster.getStats().getSummonType() <= 1) ? -3 : monster.getStats().getSummonType(), oid));
                } else if (monster.getOwner() == c.getPlayer().getId()) {
                    c.getSession().writeAndFlush(MobPacket.spawnMonster(monster, (monster.getStats().getSummonType() <= 1) ? -3 : monster.getStats().getSummonType(), oid));
                }
            }
        });
        updateMonsterController(monster);
        this.spawnedMonstersOnMap.incrementAndGet();
        if (monster.getId() >= 9833070 && monster.getId() <= 9833074) {
            Timer.MapTimer.getInstance().schedule(() -> {
                if (monster != null && monster.isAlive() && !getAllCharactersThreadsafe().isEmpty()) {
                    MapleCharacter player = getAllCharactersThreadsafe().get(0);
                    if (player != null) {
                        Point pos = monster.getTruePosition();
                        killMonster(monster, player, false, false, (byte) 1);
                        MapleMonster mob = MapleLifeFactory.getMonster(monster.getId() + 10);
                        mob.setHp(GameConstants.getDreamBreakerHP((int) player.getKeyValue(15901, "stage")));
                        spawnMonsterOnGroundBelow(mob, pos);
                    }
                }
            }, 35000L);
        }
        if (monster.getId() == 8500001 || monster.getId() == 8500011 || monster.getId() == 8500021) {
            monster.SetPatten(120);
            if (monster.getId() == 8500021) {
                monster.getStats().setHp(553000000000L);
                monster.setHp(553000000000L);
            }
            MobSkillFactory.getMobSkill(241, 7).setOnce(false);
            monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(false, 1));
            monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTime(monster.getPatten() * 1000, false, false));
            monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusPincers(true, 0, 0, 0));
            this.PapulratusTime = 50;
        } else if (monster.getId() == 8500002 || monster.getId() == 8500012 || monster.getId() == 8500022) {
            if (monster.getId() == 8500022) {
                monster.getStats().setHp(184600000000L);
                monster.setHp(184600000000L);
            }
            monster.SetPatten(60);
            monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTime(monster.getPatten() * 1000, false, false));
        } else if (monster.getId() == 8810001 || monster.getId() == 8810000 || monster.getId() == 8810100 || monster.getId() == 8810101) {
            broadcastMessage(CWvsContext.serverNotice(6, "", "깊은 동굴 속에서 거대한 생물체가 다가오고 있습니다."));
        }
    }

    public final void spawnMonsterDelay(MapleMonster monster, int spawnType, int delay) {
        Timer.MapTimer.getInstance().schedule(() -> spawnMonster(monster, spawnType, false), delay);
    }

    public final void spawnMonsterDelayid(int mobid, int spawnType, int delay, int x, int y) {
        Timer.MapTimer.getInstance().schedule(() -> {
            MapleMonster mob = MapleLifeFactory.getMonster(mobid);
            mob.setPosition(new Point(x, y));
            spawnMonster(mob, spawnType, false);
        }, delay);
    }

    public final void spawnMonster(MapleMonster monster, int spawnType) {
        spawnMonster(monster, spawnType, false);
    }

    public final void spawnMonster(final MapleMonster monster, final int spawnType, final boolean overwrite) {
        MapleMonster mob;
        if (monster.getId() == 8880101 || monster.getId() == 8880111) {
            boolean alreday = false;
            for (MapleMonster mapleMonster : this.getAllMonster()) {
                if (mapleMonster.getId() != 8880101 && mapleMonster.getId() != 8880111) {
                    continue;
                }
                alreday = true;
                break;
            }
            if (alreday) {
                return;
            }
        }
        monster.setMap(this);
        monster.setSpawnTime(System.currentTimeMillis());
        if (monster.getHp() <= 0L && (mob = MapleLifeFactory.getMonster(monster.getId())) != null) {
            monster.setHp(mob.getStats().getHp());
            monster.getStats().setHp(mob.getStats().getHp());
        }
        this.checkRemoveAfter(monster);
        ArrayList blocks = new ArrayList();
        this.spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {

            @Override
            public final void sendPackets(MapleClient c) {
                if (monster.getOwner() == -1) {
                    c.getSession().writeAndFlush((Object) MobPacket.spawnMonster(monster, monster.getStats().getSummonType() <= 1 || monster.getStats().getSummonType() == 27 || overwrite ? spawnType : (int) monster.getStats().getSummonType(), 0));
                } else if (monster.getOwner() == c.getPlayer().getId()) {
                    c.getSession().writeAndFlush((Object) MobPacket.spawnMonster(monster, monster.getStats().getSummonType() <= 1 || monster.getStats().getSummonType() == 27 || overwrite ? spawnType : (int) monster.getStats().getSummonType(), 0));
                }
            }
        });
        this.updateMonsterController(monster);
        if (monster.getOwner() < 0) {
            this.spawnedMonstersOnMap.incrementAndGet();
        }
        if (monster.getSeperateSoul() == 0) {
            if (monster.getId() == 8900000 || monster.getId() == 8900100) {
                monster.lastCapTime = 1L;
            }
            if (monster.getId() == 8880300 || monster.getId() == 8880340) {
                monster.getWillHplist().add(666);
                monster.getWillHplist().add(333);
                monster.getWillHplist().add(3);
                this.broadcastMessage(MobPacket.BossWill.setWillHp(monster.getWillHplist(), this, monster.getId(), monster.getId() + 3, monster.getId() + 4));
                monster.setCustomInfo(24205, 0, 61000);
                for (MobSkill mobSkill : monster.getStats().getSkills()) {
                    monster.setLastSkillUsed(mobSkill, System.currentTimeMillis(), 99999999L);
                }
            }
            if (monster.getId() == 8880301 || monster.getId() == 8880341) {
                monster.getWillHplist().add(500);
                monster.getWillHplist().add(3);
                this.broadcastMessage(MobPacket.BossWill.setWillHp(monster.getWillHplist()));
            }
            if (monster.getId() == 8880000 || monster.getId() == 8880002 || monster.getId() == 8880010) {
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    if (monster.getMap().getAllChracater().size() <= 0) {
                        monster.getSchedule().cancel(true);
                        monster.setSchedule(null);
                        monster.getMap().killMonsterType(monster, 0);
                        return;
                    }
                    MapleBossManager.magnusHandler(monster, 0, 0);
                }, 3000L));
            } else if (monster.getId() / 1000 == 8900 && monster.getId() % 10 < 3 && this.getNumMonsters() <= 2 && monster.getSeperateSoul() <= 0) {
                MapleBossManager.pierreHandler(monster);
            } else if (monster.getId() == 8950000 || monster.getId() == 8950001 || monster.getId() == 8950002 || monster.getId() == 8950100 || monster.getId() == 8950101 || monster.getId() == 8950102) {
                ArrayList stats = new ArrayList();
                for (MobSkill sk : monster.getSkills()) {
                    if (sk.getSkillId() != 223) {
                        continue;
                    }
                    monster.setLastSkillUsed(sk, System.currentTimeMillis(), 13000L);
                    Timer.MobTimer.getInstance().schedule(() -> {
                        monster.setCustomInfo(2286, 0, 60000);
                        monster.setCustomInfo(22878, 0, Randomizer.rand(60000, 70000));
                        monster.setEnergyspeed(1);
                        monster.setEnergyleft(false);
                        monster.setEnergycount(45);
                        stats.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Laser, new MonsterStatusEffect(223, (int) sk.getDuration())));
                        monster.applyMonsterBuff(this, stats, sk);
                    }, 3000L);
                    break;
                }
                monster.setSchedule(Timer.MapTimer.getInstance().register(() -> {
                    if (!(monster.getEventInstance() != null && monster.isAlive() || this.getAllChracater().size() > 0)) {
                        monster.getSchedule().cancel(true);
                        monster.setSchedule(null);
                        monster.getMap().killMonster(monster);
                        return;
                    }
                    MapleBossManager.lotusHandler(monster);
                }, 4000L));
            } else if (monster.getId() == 8880100 || monster.getId() == 8880110 || monster.getId() == 8880101 || monster.getId() == 8880111) {
                int time = 28000;
                if (monster.getId() == 8880101 || monster.getId() == 8880111) {
                    monster.setHp((long) ((double) monster.getHp() * 0.3));
                    this.broadcastMessage(MobPacket.CorruptionChange((byte) 0, this.getStigmaDeath()));
                    MapleMonster mapleMonster = MapleLifeFactory.getMonster(8880102);
                    mapleMonster.getStats().setSpeed(80);
                    this.spawnMonsterWithEffect(mapleMonster, MobSkillFactory.getMobSkill(201, 182).getSpawnEffect(), monster.getPosition());
                    time = 18000;
                } else {
                    MapleFlyingSword mapleFlyingSword = new MapleFlyingSword(0, monster);
                    monster.getMap().spawnFlyingSword(mapleFlyingSword);
                    monster.getMap().setNewFlyingSwordNode(mapleFlyingSword, monster.getTruePosition());
                }
                this.broadcastMessage(CField.StigmaTime(28000));
                this.broadcastMessage(CField.enforceMSG("데미안이 누구에게 낙인을 새길지 알 수 없습니다.", 216, 30000000));
                Timer.MapTimer.getInstance().schedule(() -> MapleBossManager.demianHandler(monster), time);
            } else if (monster.getId() == 8880512) {
                MapleBossManager.blackMageHandler(monster);
            } else if (monster.getId() == 8644650 || monster.getId() == 8644655) {
                monster.setCustomInfo(100023, 1, 0);
                this.setCustomInfo(8644650, 0, 55000);
                MapleBossManager.duskHandler(monster, this);
            } else if (monster.getId() == 8645009 || monster.getId() == 8645066) {
                Timer.MapTimer.getInstance().schedule(() -> MapleBossManager.dunkelHandler(monster, this), 2000L);
            } else if (monster.getStats().getName().contains("세렌") || monster.getId() == 8880605) {
                if (monster.getId() != 8880602) {
                    for (MobSkill mobSkill : monster.getSkills()) {
                        monster.setLastSkillUsed(mobSkill, System.currentTimeMillis(), 5000L);
                    }
                    Timer.MapTimer.getInstance().schedule(() -> MapleBossManager.SerenHandler(monster), 5000L);
                } else {
                    MapleBossManager.SerenHandler(monster);
                }
            } else if (monster.getId() == 8880140 || monster.getId() == 8880141 || monster.getId() == 8880143 || monster.getId() == 8880150 || monster.getId() == 8880151 || monster.getId() == 8880153 || monster.getId() == 8880155 || monster.getId() == 8880166 || monster.getId() == 8880158) {
                MapleBossManager.LucidHandler(monster);
            } else if (monster.getId() == 8880302 || monster.getId() == 8880342) {
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    if (!(monster.getEventInstance() != null && monster.isAlive() || this.getAllChracater().size() > 0)) {
                        monster.getSchedule().cancel(true);
                        monster.setSchedule(null);
                        monster.getMap().killMonster(monster);
                        return;
                    }
                    MobSkill web = MobSkillFactory.getMobSkill(242, 13);
                    for (int i = 0; i < Randomizer.rand(1, 2); ++i) {
                        web.applyEffect(monster.getController(), monster, true, monster.isFacingLeft());
                    }
                }, 6000L));
            }
            if (monster.getId() == 8880400 || monster.getId() == 8880405 || monster.getId() == 8880415) {
                MobSkill msi = MobSkillFactory.getMobSkill(247, 1);
                monster.setCustomInfo(24701, 1, 0);
                msi.applyEffect(monster.getController(), monster, true, true);
                monster.setLastSkillUsed(msi, System.currentTimeMillis(), 99999000L);
                MapleBossManager.JinHillaGlassTime(monster, 150);
            }
            if (monster.getId() == 8880500) {
                monster.setLastSkillUsed(MobSkillFactory.getMobSkill(170, 62), System.currentTimeMillis(), 40000L);
            }
            if (monster.getId() == 8880501) {
                monster.setLastSkillUsed(MobSkillFactory.getMobSkill(170, 64), System.currentTimeMillis(), 40000L);
            }
            if (monster.getId() == 8930000) {
                MobSkill msi = MobSkillFactory.getMobSkill(170, 13);
                msi.setHp(40);
            }
            if (monster.getId() == 8644658 || monster.getId() == 0x83E833) {
                monster.setCustomInfo(100020, 0, 12000);
                monster.setCustomInfo(100021, 0, 7000);
            }
            if (monster.getId() >= 9833935 && monster.getId() <= 9833946) {
                monster.setCustomInfo(monster.getId(), monster.getId() == 9833946 ? 250 : (monster.getId() == 9833944 || monster.getId() == 9833945 ? 100 : 50), 0);
            }
            if (monster.getId() >= 9833947 && monster.getId() <= 9833958) {
                monster.setCustomInfo(monster.getId(), 10, 0);
            }
            if (monster.getId() >= 9833070 && monster.getId() <= 9833074) {
                Timer.MapTimer.getInstance().schedule(() -> {
                    MapleCharacter player;
                    if (monster != null && monster.isAlive() && !this.getAllCharactersThreadsafe().isEmpty() && (player = this.getAllCharactersThreadsafe().get(0)) != null) {
                        Point pos = monster.getTruePosition();
                        this.killMonster(monster, player, false, false, (byte) 1);
                        MapleMonster mob2 = MapleLifeFactory.getMonster(monster.getId() + 10);
                        mob2.setHp(GameConstants.getDreamBreakerHP((int) player.getKeyValue(15901, "stage")));
                        this.spawnMonsterOnGroundBelow(mob2, pos);
                    }
                }, 35000L);
            }
        }
    }

    public final int spawnMonsterWithEffect(final MapleMonster monster, final int effect, Point pos) {
        try {
            monster.setMap(this);
            monster.setPosition(pos);
            if (monster.getHp() <= 0L) {
                MapleMonster mob = MapleLifeFactory.getMonster(monster.getId());
                if (mob != null) {
                    monster.setHp(mob.getStats().getHp());
                    monster.getStats().setHp(mob.getStats().getHp());
                }
            }
            spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {
                public final void sendPackets(MapleClient c) {
                    c.getSession().writeAndFlush(MobPacket.spawnMonster(monster, effect, 0));
                }
            });
            updateMonsterController(monster);
            this.spawnedMonstersOnMap.incrementAndGet();
            if (monster.getSeperateSoul() > 0) {
                return 0;
            }
            if ((monster.getId() >= 8900000 && monster.getId() <= 8900002) || (monster.getId() >= 8900100 && monster.getId() <= 8900103)) {
                MapleBossManager.pierreHandler(monster);
                if (monster.getId() == 8900002 || monster.getId() == 8900102) {
                    Iterator<MapleCharacter> iterator = getAllChracater().iterator();
                    if (iterator.hasNext()) {
                        MapleCharacter chr = iterator.next();
                        MapleCharacter chrs = chr.getClient().getRandomCharacter();
                        monster.switchController(chrs, true);
                        monster.setSpecialtxt(chrs.getName() + "");
                        String name = (monster.getId() == 8900102) ? "피에르" : "카오스 피에르";
                        chrs.getMap().broadcastMessage(CWvsContext.getTopMsg(name + "가 [" + chrs.getName() + "]를 추격합니다."));
                        broadcastMessage(MobPacket.ShowPierreEffect(chrs, monster));
                    }
                }
            } else if (monster.getId() == 8910000) {
                monster.setSchedule(Timer.MapTimer.getInstance().register(() -> {
                    if (monster.getHPPercent() <= 10) {
                        List<Obstacle> obs = new ArrayList<>();
                        for (int i = 0; i < Randomizer.rand(4, 7); i++) {
                            int key = Randomizer.rand(1, 6) + 21;
                            int x = Randomizer.rand(1, 1920) - 1140;
                            Obstacle ob = new Obstacle(key, new Point(x, -210), new Point(x, 820), 25, (key == 22 || key == 25) ? 100 : ((key == 23 || key == 26) ? 50 : 33), Randomizer.rand(1100, 1500), Randomizer.rand(0, 128), 3, 653);
                            obs.add(ob);
                        }
                        monster.getMap().broadcastMessage(MobPacket.createObstacle(monster, obs, (byte) 0));
                    }
                }, 3000L));
            } else if (monster.getId() == 8880102) {
                int[] demians = {8880101, 8880111};
                MapleMonster demian = null;
                for (int ids : demians) {
                    demian = getMonsterById(ids);
                    if (demian != null) {
                        break;
                    }
                }
                if (demian != null) {
                    broadcastMessage(MobPacket.DemianTranscendenTalSet(demian, monster));
                    broadcastMessage(MobPacket.DemianTranscendenTalSet2(monster));
                }
            }
            return monster.getObjectId();
        } catch (Exception e) {
            return -1;
        }
    }

    public final void spawnFakeMonster(final MapleMonster monster) {
        monster.setMap(this);
        monster.setFake(true);
        if (monster.getHp() <= 0L) {
            MapleMonster mob = MapleLifeFactory.getMonster(monster.getId());
            if (mob != null) {
                monster.setHp(mob.getStats().getHp());
                monster.getStats().setHp(mob.getStats().getHp());
            }
        }
        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {
            public final void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(MobPacket.spawnMonster(monster, -4, 0));
            }
        });
        updateMonsterController(monster);
        this.spawnedMonstersOnMap.incrementAndGet();
    }

    public final void spawnDelayedAttack(final MobSkill skill, final MapleDelayedAttack mda) {
        spawnAndAddRangedMapObject(mda, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(MobPacket.onDemianDelayedAttackCreate(skill.getSkillId(), skill.getSkillLevel(), mda));
            }
        });
    }

    public final void spawnDelayedAttack(MapleMonster mob, MobSkill skill, List<MapleDelayedAttack> mda) {
        for (MapleDelayedAttack att : mda) {
            addMapObject(att);
        }
        broadcastMessage(MobPacket.onDemianDelayedAttackCreate(mob, skill.getSkillId(), skill.getSkillLevel(), mda));
    }

    public final void spawnMapleAtom(MapleAtom atom) {
        broadcastMessage(CField.createAtom(atom));
    }

    public final void spawnRune(final MapleRune rune) {
        rune.setMap(this);
        this.rune = rune;
        spawnAndAddRangedMapObject(rune, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(CField.spawnRune(rune, false));
            }
        });
    }

    public final void spawnReactor(final MapleReactor reactor) {
        reactor.setMap(this);
        spawnAndAddRangedMapObject(reactor, new DelayedPacketCreation() {
            public final void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(CField.spawnReactor(reactor));
            }
        });
    }

    private void respawnReactor(MapleReactor reactor) {
        reactor.setState((byte) 0);
        reactor.setAlive(true);
        spawnReactor(reactor);
    }

    public final void spawnDoor(final MapleDoor door) {
        spawnAndAddRangedMapObject(door, new DelayedPacketCreation() {
            public final void sendPackets(MapleClient c) {
                door.sendSpawnData(c);
                c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
            }
        });
    }

    public final void spawnMechDoor(MechDoor door) {
        spawnAndAddRangedMapObject(door, c -> {
            c.getSession().writeAndFlush(CField.spawnMechDoor(door, true));
            c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
        });
        Timer.MapTimer tMan = Timer.MapTimer.getInstance();
        ScheduledFuture<?> schedule = null;
        door.setSchedule(schedule);
        if (door.getDuration() > 0) {
            door.setSchedule(tMan.schedule(() -> {
                broadcastMessage(CField.removeMechDoor(door, true));
                removeMapObject(door);
            }, door
                    .getDuration()));
        }
    }

    public final void spawnSummon(MapleSummon summon) {
        summon.updateMap(this);
        spawnAndAddRangedMapObject(summon, c -> {
            if (summon != null && c.getPlayer() != null && (!summon.isChangedMap() || summon.getOwner().getId() == c.getPlayer().getId())) {
                c.getSession().writeAndFlush(CField.SummonPacket.spawnSummon(summon, true));
            }
        });
    }

    public final void spawnOrb(MapleOrb orb) {
        spawnAndAddRangedMapObject(orb, c -> orb.sendSpawnData(c));
    }

    public final void removeOrb(int playerId, MapleOrb orb) {
        if (orb != null) {
            removeMapObject(orb);
        }
        broadcastMessage(CField.removeOrb(playerId, Arrays.asList(new MapleOrb[]{orb})));
    }

    public final void spawnSpecialPortal(MapleCharacter chr, List<SpecialPortal> objects) {
        for (SpecialPortal object : objects) {
            addMapObject(object);
            Timer.MapTimer.getInstance().schedule(() -> removeSpecialPortal(chr, object), object.getDuration());
        }
        broadcastMessage(CField.createSpecialPortal(chr.getId(), objects));
    }

    public final List<SpecialPortal> SpecialPortalSize(final int ownerId) {
        final List<SpecialPortal> ret = new ArrayList<SpecialPortal>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.SPECIAL_PORTAL).values()) {
            final SpecialPortal object = (SpecialPortal) mmo;
            if (object.getOwnerId() == ownerId) {
                ret.add(object);
            }
        }
        return ret;
    }

    public final void removeSpecialPortal(MapleCharacter chr, SpecialPortal object) {
        if (object != null) {
            removeMapObject(object);
            chr.addSkillCustomInfo(object.getSkillId(), -1L);
        }
        broadcastMessage(CField.removeSpecialPortal(chr.getId(), Arrays.asList(new SpecialPortal[]{object})));
    }

    public final void removeSpecialPortal(MapleCharacter chr, List<SpecialPortal> lists) {
        for (SpecialPortal object : lists) {
            removeMapObject(object);
        }
        broadcastMessage(CField.removeSpecialPortal(chr.getId(), lists));
    }

    public final void spawnSecondAtom(MapleCharacter chr, List<SecondAtom> tiles, int spawnType) {
        for (SecondAtom tile : tiles) {
            addMapObject(tile);
        }
        broadcastMessage(CField.spawnSecondAtoms(chr.getId(), tiles, spawnType));
    }

    public final void spawnSecondAtom(MapleCharacter chr, MapleSecondAtom atom) {
        List<MapleSecondAtom> at = Arrays.asList(new MapleSecondAtom[]{atom});
        spawnAndAddRangedMapObject(atom, c -> c.getSession().writeAndFlush(SkillPacket.createSecondAtom(at)));
    }

    public final void spawnSecondAtom(MapleCharacter chr, MapleSecondAtom atom, boolean left) {
        List<MapleSecondAtom> at = Arrays.asList(new MapleSecondAtom[]{atom});
        spawnAndAddRangedMapObject(atom, c -> c.getSession().writeAndFlush(SkillPacket.createSecondAtom(at, left)));
    }

    public List<MapleSecondAtom> getAllSecondAtoms() {
        final ArrayList<MapleSecondAtom> ret = new ArrayList<MapleSecondAtom>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.SECOND_ATOM).values()) {
            ret.add((MapleSecondAtom) mmo);
        }
        return ret;
    }

    public MapleSecondAtom getFindSecondAtoms(final int objectid) {
        MapleSecondAtom ret = null;
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.SECOND_ATOM).values()) {
            if (objectid == mmo.getObjectId()) {
                ret = (MapleSecondAtom) mmo;
                break;
            }
        }
        return ret;
    }

    public final void removeSecondAtom(MapleCharacter chr, int objectId) {
        MapleMapObject Aobject = getMapObject(objectId, MapleMapObjectType.A_SECOND_ATOM);
        if (Aobject != null) {
            SecondAtom sa = chr.getMap().getSecondAtomOid(chr.getId(), objectId);
            if (sa != null) {
                if (sa.getSkillId() == 162111002 && chr.getBuffedValue(162111002)) {
                    return;
                }
                removeMapObject(Aobject);
            }
        } else {
            MapleMapObject object = getMapObject(objectId, MapleMapObjectType.SECOND_ATOM);
            if (object != null) {
                MapleSecondAtom remover = chr.getMap().getFindSecondAtoms(objectId);
                if (remover != null) {
                    if (GameConstants.isDarkKnight(chr.getJob())) {
                        if (chr.getBuffedValue(400011047)) {
                            int healhp = (int) (chr.getStat().getCurrentMaxHp() / 100L * chr.getBuffedEffect(400011047).getX());
                            int makeshiled = healhp / 100 * chr.getBuffedEffect(400011047).getV();
                            int maxshiled = (int) (chr.getStat().getCurrentMaxHp() / 100L * chr.getBuffedEffect(400011047).getY());
                            chr.setSkillCustomInfo(400011047, chr.getSkillCustomValue0(400011047) + 1L, 0L);
                            if (chr.getSkillCustomValue0(400011047) > chr.getBuffedEffect(400011047).getS()) {
                                chr.setSkillCustomInfo(400011047, chr.getBuffedEffect(400011047).getS(), 0L);
                            }
                            if (chr.getStat().getHPPercent() == 100) {
                                chr.setSkillCustomInfo(400011048, chr.getSkillCustomValue0(400011048) + makeshiled, 0L);
                                if (chr.getSkillCustomValue0(400011048) > maxshiled) {
                                    chr.setSkillCustomInfo(400011048, maxshiled, 0L);
                                }
                            } else {
                                chr.addHP(healhp);
                            }
                            chr.getBuffedEffect(400011047).applyTo(chr, false);
                        }
                    } else if (GameConstants.isFlameWizard(chr.getJob())) {
                        if (remover.getSecondAtoms().getSourceId() == 400021092) {
                            MapleSummon sum = chr.getSummon(400021092);
                            if (sum != null) {
                                chr.getMap().broadcastMessage(CField.SummonPacket.updateSummon(sum, 0));
                                chr.removeSkillCustomInfo(400021092);
                            }
                        }
                    } else if (GameConstants.isCain(chr.getJob())
                            && remover.getSecondAtoms().getSourceId() == 63101006) {
                        chr.addSkillCustomInfo(63101006, -1L);
                    }
                    removeMapObject(object);
                    if (remover.getSchedule() != null) {
                        remover.getSchedule().cancel(true);
                        remover.setSchedule((ScheduledFuture<?>) null);
                    }
                }
            } else if (GameConstants.isMechanic(chr.getJob())) {
                chr.setSkillCustomInfo(400051069, chr.getSkillCustomValue0(400051069) + 1L, 0L);
                if (chr.getBuffedValue(400051068)
                        && chr.getSkillCustomValue0(400051068) == chr.getSkillCustomValue0(400051069)) {
                    chr.setSkillCustomInfo(400051068, chr.getSkillCustomValue0(400051068) + chr.getBuffedEffect(400051068).getY(), 0L);
                    chr.MechCarrier(1000, true);
                }
            }
        }
        broadcastMessage(CField.removeSecondAtom(chr.getId(), objectId));
    }

    public final void spawnRandomPortal(MapleRandomPortal portal) {
        spawnAndAddRangedMapObject(portal, c -> {
            c.getSession().writeAndFlush(CField.specialMapSound("Field.img/StarPlanet/cashTry"));
            if (portal.getPortalType() == 2) {
                c.getSession().writeAndFlush(CWvsContext.getTopMsg("현상금 사냥꾼의 포탈이 등장했습니다!"));
            } else if (portal.getPortalType() == 3) {
                c.getSession().writeAndFlush(CWvsContext.getTopMsg("불꽃늑대의 소굴로 향하는 포탈이 등장했습니다!"));
            }
            if (c.getPlayer().getId() == portal.getCharId()) {
                c.getSession().writeAndFlush(SLFCGPacket.PoloFrittoPortal(portal));
            }
        });
        Timer.MapTimer.getInstance().schedule(() -> {
            broadcastMessage(SLFCGPacket.RemovePoloFrittoPortal(portal));
            removeMapObject(portal);
        }, 60000L);
    }

    public final void spawnSpiderWeb(final SpiderWeb web) {
        spawnAndAddRangedMapObject(web, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                web.sendSpawnData(c);
            }
        });
    }

    public final void spawnSummon(final MapleSummon summon, final int duration) {
        summon.updateMap(this);
        summon.setStartTime(System.currentTimeMillis());
        spawnAndAddRangedMapObject(summon, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                if (summon != null && c.getPlayer() != null && (!summon.isChangedMap() || summon.getOwner().getId() == c.getPlayer().getId())) {
                    c.getSession().writeAndFlush(CField.SummonPacket.spawnSummon(summon, true, duration));
                }
            }
        });
        final MapleMap map = this;
        if (summon.getSkill() == 152101000) {
            if (summon.getOwner().getSkillCustomValue0(152101000) > 0L) {
                summon.setEnergy((int) summon.getOwner().getSkillCustomValue0(152101000));
            }
            summon.getOwner().getClient().getSession().writeAndFlush(CField.SummonPacket.transformSummon(summon, 2));
        }
        if ((duration > 0 && duration < Integer.MAX_VALUE && summon.getSkill() != 400051046 && summon.getSummonType() != 7) || (summon.getSkill() == 400021068 && summon.getMovementType() != SummonMovementType.SUMMON_JAGUAR)) {
            Timer.MapTimer.getInstance().schedule(new Runnable() {
                public void run() {
                    summon.removeSummon(map, false, false);
                }
            }, (duration + 1000));
        }
    }

    public final void spawnExtractor(final MapleExtractor ex) {
        spawnAndAddRangedMapObject(ex, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                ex.sendSpawnData(c);
            }
        });
    }

    public final void spawnSpecialChair(final MapleSpecialChair ex) {
        spawnAndAddRangedMapObject(ex, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                ex.sendSpawnData(c);
            }
        });
    }

    public final void spawnMagicWreck(final MapleMagicWreck mw) {
        final List<MapleMagicWreck> mws = new ArrayList<>();
        getWrecks().add(mw);
        spawnAndAddRangedMapObject(mw, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                mw.sendSpawnData(c);
            }
        });
        Timer.MapTimer tMan = Timer.MapTimer.getInstance();
        ScheduledFuture<?> schedule = null;
        mw.setSchedule(schedule);
        if (mw.getDuration() > 0) {
            mw.setSchedule(tMan.schedule(new Runnable() {
                public void run() {
                    MapleMap.this.broadcastMessage(CField.removeMagicWreck(mw.getChr(), mws));
                    MapleMap.this.removeMapObject(mw);
                    MapleMap.this.getWrecks().remove(mw);
                }
            }, mw.getDuration()));
        }
    }

    public final void RemoveMagicWreck(MapleMagicWreck mw) {
        List<MapleMagicWreck> mws = new ArrayList<>();
        mws.add(mw);
        getWrecks().remove(mw);
        broadcastMessage(CField.removeMagicWreck(mw.getChr(), mws));
        removeMapObject(mw);
    }

    public final void spawnFlyingSword(final MapleFlyingSword mfs) {
        spawnAndAddRangedMapObject(mfs, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                mfs.sendSpawnData(c);
            }
        });
    }

    public final void removeAllFlyingSword() {
        for (MapleFlyingSword sword : getAllFlyingSwordsThreadsafe()) {
            broadcastMessage(MobPacket.FlyingSword(sword, false));
            removeMapObject(sword);
        }
    }

    public final void setNewFlyingSwordNode(final MapleFlyingSword mfs, Point point) {
        FlyingSwordNode msn = new FlyingSwordNode(1, 0, 0, 30, 0, 0, 0, false, 0, new Point(point.x, -180));
        List<FlyingSwordNode> nodes = new ArrayList<>();
        nodes.add(msn);
        mfs.setNodes(nodes);
        broadcastMessage(MobPacket.FlyingSwordNode(mfs));
        mfs.updateTarget(this);
        final MapleMap map = this;
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                if (mfs != null) {
                    mfs.updateFlyingSwordNode(map);
                }
            }
        }, 3500L);
    }

    public final void spawnIncinerateObject(final MapleIncinerateObject mio) {
        spawnAndAddRangedMapObject(mio, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                mio.sendSpawnData(c);
            }
        });
        Timer.MapTimer tMan = Timer.MapTimer.getInstance();
        mio.setSchedule(tMan.schedule(new Runnable() {
            public void run() {
                MapleMap.this.broadcastMessage(MobPacket.incinerateObject(mio, false));
                MapleMap.this.removeMapObject(mio);
            }
        }, 10000L));
    }

    public final void spawnFieldAttackObj(final MapleFieldAttackObj fao) {
        spawnAndAddRangedMapObject(fao, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                fao.sendSpawnData(c);
                fao.onSetAttack(c);
            }
        });
        Timer.MapTimer tMan = Timer.MapTimer.getInstance();
        ScheduledFuture<?> schedule = null;
        fao.setSchedule(schedule);
        if (fao.getDuration() > 0) {
            fao.setSchedule(tMan.schedule(new Runnable() {
                public void run() {
                    MapleMap.this.broadcastMessage(CField.AttackObjPacket.ObjRemovePacketByOid(fao.getObjectId()));
                    MapleMap.this.removeMapObject(fao);
                }
            }, fao.getDuration()));
        }
    }

    public void spawnEnergySphere(final int objectId, final int skillLevel, final MapleEnergySphere sp) {
        spawnAndAddRangedMapObject(sp, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                MapleMap.this.broadcastMessage(MobPacket.createEnergySphere(objectId, skillLevel, sp));
            }
        });
    }

    public void spawnEnergySphereTimer(final int objectId, final int skillLevel, final MapleEnergySphere sp, int time) {
        Timer.MapTimer.getInstance().schedule(() -> spawnAndAddRangedMapObject(sp, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                MapleMap.this.broadcastMessage(MobPacket.createEnergySphere(objectId, skillLevel, sp));
            }
        }), time);
    }

    public void spawnEnergySphereListTimer(int objectId, int skillLevel, List<MapleEnergySphere> sps, int time) {
        Timer.MapTimer.getInstance().schedule(() -> broadcastMessage(MobPacket.createEnergySphere(objectId, skillLevel, sps)), time);
    }

    public final void spawnMist(final MapleMist mist, boolean fake) {
        spawnAndAddRangedMapObject(mist, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                mist.sendSpawnData(c);
            }
        });
        if (mist.getStartTime() == 0L) {
            mist.setStartTime(System.currentTimeMillis());
        }
    }

    public final MapleEnergySphere getEnergySphere(int objid) {
        for (MapleEnergySphere mse : getAllEnergySphere()) {
            if (mse.getObjectId() == objid) {
                return mse;
            }
        }
        return null;
    }

    public final MapleFlyingSword getFlyingSword(int objid) {
        for (MapleFlyingSword mfs : getAllFlyingSwordsThreadsafe()) {
            if (mfs.getObjectId() == objid) {
                return mfs;
            }
        }
        return null;
    }

    public final MapleMist getMist(int ownerId, int skillId) {
        for (MapleMist mist : getAllMistsThreadsafe()) {
            if (mist.getSourceSkill() != null
                    && mist.getSourceSkill().getId() == skillId && mist.getOwnerId() == ownerId) {
                return mist;
            }
        }
        return null;
    }

    public final void removeMist(int skillid) {
        for (MapleMist mist : getAllMistsThreadsafe()) {
            if (mist.getSourceSkill() != null
                    && mist.getSourceSkill().getId() == skillid) {
                broadcastMessage(CField.removeMist(mist));
                removeMapObject(mist);
            }
        }
    }

    public final void removeMistByOwner(MapleCharacter chr, int skillid) {
        for (MapleMist mist : getAllMistsThreadsafe()) {
            if (mist.getSourceSkill() != null) {
                if (mist.getOwnerId() == chr.getId()) {
                    if (mist.getSourceSkill().getId() == skillid) {
                        broadcastMessage(CField.removeMist(mist));
                        removeMapObject(mist);
                    }
                }
            }
        }
    }

    public final void removeMist(MapleMist mist) {
        if (getMapObject(mist.getObjectId(), MapleMapObjectType.MIST) != null) {
            broadcastMessage(CField.removeMist(mist));
            removeMapObject(mist);
        }
    }

    public final void disappearingItemDrop(MapleMapObject dropper, MapleCharacter owner, Item item, Point pos) {
        Point droppos = calcDropPos(pos, pos);
        MapleMapItem drop = new MapleMapItem(item, droppos, dropper, owner, (byte) 1, false);
        broadcastMessage(CField.dropItemFromMapObject(this, drop, dropper.getTruePosition(), droppos, (byte) 3, (owner.getBuffedEffect(SecondaryStat.PickPocket) != null)), drop.getTruePosition());
    }

    public final void spawnMesoDrop(int meso, Point position, MapleMapObject dropper, MapleCharacter owner, boolean playerDrop, byte droptype) {
        spawnMesoDrop(meso, position, dropper, owner, playerDrop, droptype, 0);
    }

    public final void spawnMesoDrop(int meso, Point position, final MapleMapObject dropper, final MapleCharacter owner, boolean playerDrop, byte droptype, final int delay) {
        final Point droppos = calcDropPos(position, position);
        final MapleMapItem mdrop = new MapleMapItem(meso, droppos, dropper, owner, droptype, playerDrop);
        if (delay > 0 && owner.getBuffedEffect(SecondaryStat.PickPocket) != null) {
            int max = SkillFactory.getSkill(4211006).getEffect(owner.getSkillLevel(4211006)).getBulletCount();
            if (owner.getSkillLevel(4220045) > 0) {
                max += SkillFactory.getSkill(4220045).getEffect(owner.getSkillLevel(4220045)).getBulletCount();
            }
            if (owner.getPickPocket().size() < max) {
                mdrop.setPickpoket(true);
                owner.addPickPocket(mdrop);
            }
        }
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(CField.dropItemFromMapObject(owner.getMap(), mdrop, dropper.getTruePosition(), droppos, (byte) 1, (owner.getBuffedEffect(SecondaryStat.PickPocket) != null), delay, (byte) (owner.getBuffedValue(4221018) ? 4 : 0)));
            }
        });
        if (!this.everlast) {
            mdrop.registerExpire(120000L);
            if (droptype == 0 || droptype == 1) {
                mdrop.registerFFA(30000L);
            }
        }
    }

    public final void spawnMobMesoDrop(int meso, final Point position, final MapleMapObject dropper, final MapleCharacter owner, boolean playerDrop, byte droptype) {
        final MapleMapItem mdrop = new MapleMapItem(meso, position, dropper, owner, droptype, playerDrop);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(CField.dropItemFromMapObject(owner.getMap(), mdrop, dropper.getTruePosition(), position, (byte) 1, (owner.getBuffedEffect(SecondaryStat.PickPocket) != null)));
            }
        });
        boolean magnetpet = false;
        for (int i = 0; i < (owner.getPets()).length; i++) {
            if (owner.getPets()[i] != null && (owner.getPets()[i].getPetItemId() == 5000930 || owner.getPets()[i].getPetItemId() == 5000931 || owner.getPets()[i].getPetItemId() == 5000932)) {
                magnetpet = true;
            }
        }
        mdrop.registerExpire(120000L);
        if (droptype == 0 || droptype == 1) {
            mdrop.registerFFA(30000L);
        }
    }

    public final void spawnFlyingDrop(final MapleCharacter chr, final Point startPos, final Point dropPos, Item idrop) {
        final MapleMapItem mdrop = new MapleMapItem(idrop, dropPos, null, chr, (byte) 2, false, 0);
        mdrop.setFlyingSpeed(Randomizer.rand(50, 150));
        mdrop.setFlyingAngle(Randomizer.rand(55, 199));
        mdrop.setFlyingDrop(true);
        mdrop.setTouchDrop(true);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(CField.dropItemFromMapObject(chr.getMap(), mdrop, startPos, dropPos, (byte) 1, false));
            }
        });
        mdrop.registerExpire(120000L);
        activateItemReactors(mdrop, chr.getClient());
    }

    public final void spawnMobFlyingDrop(final MapleCharacter chr, final MapleMonster mob, final Point dropPos, Item idrop) {
        final MapleMapItem mdrop = new MapleMapItem(idrop, dropPos, mob, chr, (byte) 2, false, 0);
        mdrop.setFlyingSpeed(150);
        mdrop.setFlyingAngle(Randomizer.rand(55, 199));
        mdrop.setTouchDrop(true);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                c.getSession().writeAndFlush(CField.dropItemFromMapObject(chr.getMap(), mdrop, mob.getTruePosition(), dropPos, (byte) 1, false));
            }
        });
        mdrop.registerExpire(120000L);
        activateItemReactors(mdrop, chr.getClient());
    }

    public final void spawnMobDrop(Item idrop, final Point dropPos, final MapleMonster mob, final MapleCharacter chr, byte droptype, final int questid) {
        final MapleMapItem mdrop = new MapleMapItem(idrop, dropPos, mob, chr, droptype, false, questid);
        if (idrop.getItemId() / 1000000 == 1 && !GameConstants.isArcaneSymbol(idrop.getItemId()) && !GameConstants.isAuthenticSymbol(idrop.getItemId()) && idrop.getItemId() / 1000 != 1162 && idrop.getItemId() / 1000 != 1182) {
            Equip eq = (Equip) idrop;
            List<Pair<Integer, Integer>> random = new ArrayList<>();
            random.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(6000)));
            random.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(1950)));
            random.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(50)));
            random.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(2000)));
            int type = GameConstants.getWeightedRandom(random);
            if (type != 4) {
                eq.setState((byte) type);
                eq.setLines((byte) (Randomizer.isSuccess(80) ? 2 : 3));
                mdrop.setEquip(eq);
            }
        }
        if (mdrop.getItemId() != 4001536) {
            spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
                public void sendPackets(MapleClient c) {
                    if (c != null && (questid <= 0 || c.getPlayer().getQuestStatus(questid) == 1) && mob != null && dropPos != null) {
                        c.getSession().writeAndFlush(CField.dropItemFromMapObject(chr.getMap(), mdrop, mob.getPosition(), dropPos, (byte) 1, (chr.getBuffedEffect(SecondaryStat.PickPocket) != null)));
                    }
                }
            });
        }
        mdrop.registerExpire(120000L);
        if (droptype == 0 || droptype == 1) {
            mdrop.registerFFA(30000L);
        }
        activateItemReactors(mdrop, chr.getClient());
    }

    public final void spawnMobPublicDrop(Item idrop, final Point dropPos, final MapleMonster mob, final MapleCharacter chr, byte droptype, final int questid) {
        final MapleMapItem mdrop = new MapleMapItem(idrop, dropPos, mob, chr, (byte) 3, false, questid);
        switch (idrop.getItemId()) {
            case 2023484:
            case 2023494:
            case 2023495:
            case 2023669:
            case 2023927:
                mdrop.setTouchDrop(true);
                break;
            case 2022570:
            case 2434851:
            case 4001847:
            case 4001849:
                mdrop.setTouchDrop(true);
                mdrop.setDropType((byte) 0);
                break;
        }
        if (idrop.getItemId() / 1000000 == 1 && !GameConstants.isArcaneSymbol(idrop.getItemId()) && !GameConstants.isAuthenticSymbol(idrop.getItemId()) && idrop.getItemId() / 1000 != 1162 && idrop.getItemId() / 1000 != 1182) {
            Equip eq = (Equip) idrop;
            List<Pair<Integer, Integer>> random = new ArrayList<>();
            random.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(6000)));
            random.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(1950)));
            random.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(50)));
            random.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(2000)));
            int type = GameConstants.getWeightedRandom(random);
            if (type != 4) {
                eq.setState((byte) type);
                eq.setLines((byte) (Randomizer.isSuccess(80) ? 2 : 3));
                mdrop.setEquip(eq);
            }
        }
        mdrop.setPublicDropId(chr.getClient().getAccID());
        if (mdrop.getItemId() != 4001536) {
            spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
                public void sendPackets(MapleClient c) {
                    if (c != null && (questid <= 0 || c.getPlayer().getQuestStatus(questid) == 1) && mob != null && dropPos != null && c.getAccID() == chr.getClient().getAccID()) {
                        c.getSession().writeAndFlush(CField.dropItemFromMapObject(chr.getMap(), mdrop, mob.getPosition(), dropPos, (byte) 1, (chr.getBuffedEffect(SecondaryStat.PickPocket) != null)));
                    }
                }
            });
        }
        mdrop.registerExpire(120000L);
        if (droptype == 0 || droptype == 1) {
            mdrop.registerFFA(30000L);
        }
        activateItemReactors(mdrop, chr.getClient());
    }

    public final void spawnRandDrop() {
        if (this.mapid != 910000000 || this.channel != 1) {
            return;
        }
        for (final MapleMapObject o : this.mapobjects.get(MapleMapObjectType.ITEM).values()) {
            if (((MapleMapItem) o).isRandDrop()) {
                return;
            }
        }
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                Point pos = new Point(Randomizer.nextInt(800) + 531, -806);
                int theItem = Randomizer.nextInt(1000);
                int itemid = 0;
                if (theItem < 950) {
                    itemid = GameConstants.normalDrops[Randomizer.nextInt(GameConstants.normalDrops.length)];
                } else if (theItem < 990) {
                    itemid = GameConstants.rareDrops[Randomizer.nextInt(GameConstants.rareDrops.length)];
                } else {
                    itemid = GameConstants.superDrops[Randomizer.nextInt(GameConstants.superDrops.length)];
                }
                MapleMap.this.spawnAutoDrop(itemid, pos);
            }
        }, 20000L);
    }

    public final void spawnAutoDrop(int itemid, Point pos) {
        Item idrop = null;
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
            idrop = ii.getEquipById(itemid);
        } else {
            idrop = new Item(itemid, (short) 0, (short) 1, 0);
        }
        idrop.setGMLog("Dropped from auto  on " + this.mapid);
        MapleMapItem mdrop = new MapleMapItem(pos, idrop);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
            }
        });
        if (itemid / 10000 != 291) {
            mdrop.registerExpire(120000L);
        }
    }

    public final void spawnItemDrop(final MapleMapObject dropper, final MapleCharacter owner, Item item, Point pos, boolean ffaDrop, boolean playerDrop) {
        final Point droppos = calcDropPos(pos, pos);
        Equip equip = null;
        if (item.getType() == 1) {
            equip = (Equip) item;
        }
        final MapleMapItem drop = new MapleMapItem(item, droppos, dropper, owner, (byte) 2, playerDrop, equip);
        if (item.getItemId() == 2434851 || item.getItemId() == 4001849 || item.getItemId() == 4001847 || item.getItemId() == 2023927 || item.getItemId() == 2022570) {
            drop.setTouchDrop(true);
        }
        try {
            spawnAndAddRangedMapObject(drop, new DelayedPacketCreation() {
                public void sendPackets(MapleClient c) {
                    c.getSession().writeAndFlush(CField.dropItemFromMapObject(owner.getMap(), drop, dropper.getTruePosition(), droppos, (byte) 1, false));
                }
            });
            broadcastMessage(CField.dropItemFromMapObject(owner.getMap(), drop, dropper.getTruePosition(), droppos, (byte) 0, false));
            if (!this.everlast) {
                drop.registerExpire(120000L);
                activateItemReactors(drop, owner.getClient());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void activateItemReactors(final MapleMapItem drop, final MapleClient c) {
        final Item item = drop.getItem();
        for (final MapleMapObject o : this.mapobjects.get(MapleMapObjectType.REACTOR).values()) {
            final MapleReactor react = (MapleReactor) o;
            if (react.getReactorType() == 100 && item.getItemId() == GameConstants.getCustomReactItem(react.getReactorId(), react.getReactItem().getLeft()) && react.getReactItem().getRight() == item.getQuantity() && react.getArea().contains(drop.getTruePosition()) && !react.isTimerActive()) {
                Timer.MapTimer.getInstance().schedule(new ActivateItemReactor(drop, react, c), 5000L);
                react.setTimerActive(true);
                break;
            }
        }
    }

    public int getItemsSize() {
        return ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.ITEM)).size();
    }

    public int getExtractorSize() {
        return ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.EXTRACTOR)).size();
    }

    public List<MapleMapItem> getAllItems() {
        return getAllItemsThreadsafe();
    }

    public List<MapleMapItem> getAllItemsThreadsafe() {
        final ArrayList<MapleMapItem> ret = new ArrayList<MapleMapItem>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.ITEM).values()) {
            ret.add((MapleMapItem) mmo);
        }
        return ret;
    }

    public Point getPointOfItem(final int itemid) {
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.ITEM).values()) {
            final MapleMapItem mm = (MapleMapItem) mmo;
            if (mm.getItem() != null && mm.getItem().getItemId() == itemid) {
                return mm.getPosition();
            }
        }
        return null;
    }

    public List<MapleEnergySphere> getAllEnergySphere() {
        final ArrayList<MapleEnergySphere> ret = new ArrayList<MapleEnergySphere>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.ENERGY).values()) {
            ret.add((MapleEnergySphere) mmo);
        }
        return ret;
    }

    public List<MapleMist> getAllMistsThreadsafe() {
        final ArrayList<MapleMist> ret = new ArrayList<MapleMist>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.MIST).values()) {
            ret.add((MapleMist) mmo);
        }
        return ret;
    }

    public final void returnEverLastItem(MapleCharacter chr) {
        for (MapleMapObject o : getAllItemsThreadsafe()) {
            MapleMapItem item = (MapleMapItem) o;
            if (item.getOwner() == chr.getId()) {
                item.setPickedUp(true);
                broadcastMessage(CField.removeItemFromMap(item.getObjectId(), 2, chr.getId()), item.getTruePosition());
                if (item.getMeso() > 0) {
                    chr.gainMeso(item.getMeso(), false);
                } else {
                    MapleInventoryManipulator.addFromDrop(chr.getClient(), item.getItem(), false);
                }
                removeMapObject(item);
            }
        }
        spawnRandDrop();
    }

    public final void talkMonster(String msg, int itemId, int objectid) {
        if (itemId > 0) {
            startMapEffect(msg, itemId, false);
        }
        broadcastMessage(MobPacket.talkMonster(objectid, itemId, msg));
        broadcastMessage(MobPacket.removeTalkMonster(objectid));
    }

    public final void startMapEffect(String msg, int itemId) {
        startMapEffect(msg, itemId, false, 30000);
    }

    public final void startMapEffect(String msg, int itemId, boolean jukebox) {
        startMapEffect(msg, itemId, jukebox, 30000);
    }

    public final void startMapEffect(String msg, int itemId, int time) {
        startMapEffect(msg, itemId, false, time);
    }

    public final void startMapEffect(String msg, int itemId, boolean jukebox, int time) {
        if (this.mapEffect != null) {
            return;
        }
        this.mapEffect = new MapleMapEffect(msg, itemId);
        this.mapEffect.setJukebox(jukebox);
        broadcastMessage(this.mapEffect.makeStartData());
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                if (MapleMap.this.mapEffect != null) {
                    MapleMap.this.broadcastMessage(MapleMap.this.mapEffect.makeDestroyData());
                    MapleMap.this.mapEffect = null;
                }
            }
        }, jukebox ? 300000L : time);
    }

    public final void startExtendedMapEffect(final String msg, final int itemId) {
        broadcastMessage(CField.startMapEffect(msg, itemId, true));
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                MapleMap.this.broadcastMessage(CField.removeMapEffect());
                MapleMap.this.broadcastMessage(CField.startMapEffect(msg, itemId, false));
            }
        }, 60000L);
    }

    public final void startSimpleMapEffect(String msg, int itemId) {
        broadcastMessage(CField.startMapEffect(msg, itemId, true));
    }

    public final void startJukebox(String msg, int itemId) {
        startMapEffect(msg, itemId, true);
    }

    public final void addPlayer(MapleCharacter chr) {
        int flower, npcid;
        boolean sun;
        int[] skilllist;
        String str;
        int questid, arrayOfInt1[];
        boolean bool1;
        Point pos;
        int j;
        boolean already;
        Iterator<MapleMapObject> itr;
        this.mapobjects.get(MapleMapObjectType.PLAYER).put(chr.getObjectId(), chr);
        this.characters.add(chr);
        chr.setChangeTime();
        if (GameConstants.isZero(chr.getJob())) {
            Item weapon = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
            Item subWeapon = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
            if (weapon != null && subWeapon != null
                    && weapon.getItemId() / 1000 == 1562 && subWeapon.getItemId() / 1000 == 1572) {
                chr.getInventory(MapleInventoryType.EQUIPPED).move((short) -10, (short) -11, (short) 1);
                chr.getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, weapon));
                chr.getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, subWeapon));
            }
        }
        chr.setSkillCustomInfo(201212, 0L, 3000L);
        if (chr.getSkillCustomValue0(400051074) > 0L) {
            chr.removeSkillCustomInfo(400051074);
            chr.getClient().send(CField.fullMaker(0, 0));
        }
        if (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -27) != null && chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -28) != null && chr.getMapId() != ServerConstants.warpMap) {
            if (chr.getAndroid() == null) {
                chr.setAndroid(chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -27).getAndroid());
            } else {
                chr.updateAndroid();
            }
        }
        if (GameConstants.isDemonAvenger(chr.getJob())) {
            EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<>(SecondaryStat.class);
            statups.put(SecondaryStat.LifeTidal, new Pair<>(Integer.valueOf(3), Integer.valueOf(0)));
            chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, chr));
        }
        if (GameConstants.isTeamMap(this.mapid) && !chr.inPVP()) {
            chr.setTeam(getAndSwitchTeam() ? 0 : 1);
        }
        for (MapleMagicSword mo : chr.getMap().getAllMagicSword()) {
            if (mo != null && mo.getChr().getId() == chr.getId()) {
                chr.활성화된소드--;
                mo.setSchedule(null);
                removeMapObject(mo);
            }
        }
        final byte[] packet = CField.spawnPlayerMapobject(chr);
        final byte[] packet2 = SLFCGPacket.SetupZodiacInfo();
        final byte[] packet3 = SLFCGPacket.ZodiacRankInfo(chr.getId(), (int) chr.getKeyValue(190823, "grade"));
        if (!chr.isHidden()) {
            broadcastMessage(chr, packet, false);
        if (chr.getKeyValue(190823, "grade") > 0) {
                broadcastMessage(chr, packet2, true);
                broadcastMessage(chr, packet3, true);
            }
        } else {
            broadcastGMMessage(chr, packet, false);
        }
        if (GameConstants.isPhantom(chr.getJob())) {
            chr.getClient().getSession().writeAndFlush(CField.updateCardStack(false, chr.getCardStack()));
        } else if (GameConstants.isCain(chr.getJob())) {
            if (chr.getBuffedValue(63101005)) {
                List<MapleSecondAtom> remove = new ArrayList<>();
                int stack = 0;
                for (MapleSecondAtom at : chr.getMap().getAllSecondAtoms()) {
                    if (at.getSourceId() == 63101006 && at.getChr().getId() == chr.getId()) {
                        int du = (int) (at.getSecondAtoms().getExpire() - System.currentTimeMillis() - at.getStartTime());
                        stack += (du > 0) ? (du / 5700) : 0;
                        if (stack >= 6) {
                            stack = 6;
                        }
                        remove.add(at);
                    }
                }
                for (MapleSecondAtom re : remove) {
                    chr.getMap().removeSecondAtom(chr, re.getObjectId());
                }
                chr.removeSkillCustomInfo(63101006);
                if (stack > 0) {
                    chr.setSkillCustomInfo(63101005, stack, 0L);
                }
            }
        } else if (!chr.getSaList().isEmpty()) {
            chr.getSaList().clear();
            chr.removeSkillCustomInfo(9877654);
        }
        sendObjectPlacement(chr);
        chr.getClient().getSession().writeAndFlush(packet);

        if (chr.getGuild() != null && chr.getGuild().getCustomEmblem() != null) {
            broadcastMessage(chr, CField.loadGuildIcon(chr), false);
        }

        if (chr.getDeathCount() > 0 && chr.getEventInstance() != null) {
            if (chr.getMapId() >= 450013000 && chr.getMapId() <= 450013800) {
                chr.getClient().getSession().writeAndFlush(CField.UIPacket.openUI(1204));
            } else if (chr.getMapId() == 450010500) {
                chr.getClient().getSession().writeAndFlush(CField.JinHillah(3, chr, this));
            } else if (chr.getMapId() != 262031310 && chr.getMapId() != 262030310 && chr.getMapId() != 262031300 && chr.getMapId() != 262030100 && chr.getMapId() != 262031100 && chr.getMapId() != 262030200 && chr.getMapId() != 262031200) {
                chr.getClient().getSession().writeAndFlush(CField.getDeathCount(chr.getDeathCount()));
            } else if (chr.getMapId() == 262031300) {
                int deathcouint = 15 - chr.getDeathCount();
                chr.getClient().send(CWvsContext.onFieldSetVariable("TotalDeathCount", "15"));
                chr.getClient().send(CWvsContext.onFieldSetVariable("DeathCount", deathcouint + ""));
            }
        }
        int eventskillid = (chr.getSkillLevel(80003064) > 0) ? 80003064 : ((chr.getSkillLevel(80003046) > 0) ? 80003046 : ((chr.getSkillLevel(80003025) > 0) ? 80003025 : ((chr.getSkillLevel(80003016) > 0) ? 80003016 : 0)));
        if (eventskillid > 0) {
            String skillname = SkillFactory.getSkillName(eventskillid);
            int ui = (eventskillid == 80003046) ? 1297 : ((eventskillid == 80003016) ? 1291 : 0);
            boolean cast = true;
            if (eventskillid == 80003046
                    && chr.getKeyValue(100794, "today") >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 6000L : 3000L)) {
                cast = false;
            }
            if (!chr.getMap().isSpawnPoint() && !chr.getMap().isLevelMob(chr) && chr.getBuffedValue(eventskillid)) {
                if (ui > 0) {
                    chr.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(ui));
                    if (eventskillid == 80003046) {
                        chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062524, 0));
                        chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062525, 0));
                        chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062526, 0));
                    }
                }
                chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.EventSpecialSkill));
                chr.dropMessage(5, "레벨 범위 몬스터가 없거나 " + skillname + "을 사용할 수 없는 곳입니다.");
            } else if (chr.getMap().isSpawnPoint() && chr.getMap().isLevelMob(chr) && cast) {
                SkillFactory.getSkill(eventskillid).getEffect(chr.getSkillLevel(1)).applyTo(chr, 0);
                if (ui > 0) {
                    chr.getClient().getSession().writeAndFlush(CField.UIPacket.openUI(ui));
                }
            }
        }
        chr.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(202));
        if (this.mapid != 993190000) {
            chr.getClient().getSession().writeAndFlush(CField.UIPacket.OnSetMirrorDungeonInfo(false));
        }
        if (isTown()) {
            chr.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(202));
        }
        GameConstants.achievementRatio(chr.getClient(), this.mapid);
        switch (this.mapid) {
            
              case 912080100:
                java.util.Timer tangTimer = new java.util.Timer();
                GameConstants.TangyoonMobSpawn(chr.getClient(), 0, true);
                chr.getClient().getSession().writeAndFlush(CField.getClock(1200));
                chr.removeKeyValue(2498, "TangyoonBoss");
                chr.removeKeyValue(2498, "TangyoonCooking");
                chr.removeKeyValue(2498, "TangyoonCookingClass");                
                TimerTask tangTask = new TimerTask() {
                    @Override
                    public void run() {
                        GameConstants.TangyoonCookingClass(chr.getClient(), 0);
                        tangTimer.cancel();
                    }
                };
                tangTimer.schedule(tangTask, 4000);
                break;
            case 809000101:
            case 809000201:
                chr.getClient().getSession().writeAndFlush(CField.showEquipEffect());
                break;
            case 109090300:
                chr.getClient().getSession().writeAndFlush(CField.showEquipEffect(chr.isCatching ? 1 : 0));
                break;
            case 450002011:
            case 450002012:
            case 450002013:
            case 450002014:
            case 450002015:
            case 450002021:
            case 450002200:
            case 450002201:
            case 450002301:
            case 921170050:
            case 921170100:
            case 921171200:
            case 954080200:
            case 954080300:
            case 993000868:
            case 993000869:
            case 993000870:
            case 993000871:
            case 993000872:
            case 993000873:
            case 993000874:
            case 993000875:
            case 993000877:
                chr.getClient().getSession().writeAndFlush(CField.momentAreaOnOffAll(Collections.singletonList("swim01")));
                break;
            case 350160100:
            case 350160200:
                chr.getClient().getSession().writeAndFlush(CField.UseSkillWithUI(13, 80001974, 1));
                chr.getClient().getSession().writeAndFlush(MobPacket.CorruptionChange((byte) 0, getStigmaDeath()));
                break;
            case 350160140:
            case 350160240:
                chr.getClient().getSession().writeAndFlush(CField.UseSkillWithUI(13, 80001974, 1));
                chr.getClient().getSession().writeAndFlush(MobPacket.CorruptionChange((byte) 0, getStigmaDeath()));
                break;
            case 450004150:
            case 450004250:
            case 450004450:
            case 450004550:
                chr.getClient().getSession().writeAndFlush(MobPacket.BossLucid.changeStatueState(false, getLucidCount(), false));
                break;
            case 450008150:
            case 450008750:
                chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.setMoonGauge(100, 45));
                chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.addMoonGauge(chr.getMoonGauge()));
                break;
            case 450008250:
            case 450008850:
                chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.setMoonGauge(100, 50));
                chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.addMoonGauge(chr.getMoonGauge()));
                break;
            case 450008350:
            case 450008950:
                chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.setMoonGauge(100, 25));
                chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.addMoonGauge(chr.getMoonGauge()));
                break;
            case 993000400:
                chr.setFrittoDancing(new FrittoDancing(2));
                chr.getFrittoDancing().start(chr.getClient());
                break;
            case 993192000:
                flower = (int) chr.getKeyValue(501387, "flower");
                if (flower < 0) {
                    flower = 0;
                }
                str = String.valueOf(flower);
                chr.getClient().send(CField.setMapOBJ("all", 0, 0, 0));
                chr.getClient().send(CField.setMapOBJ(str, 1, 0, 0));
                if (getCustomValue(993192000) == null) {
                    chr.getClient().send(CField.setSpecialMapEffect("bloomingSun", 0, 0));
                    chr.getClient().send(CField.setSpecialMapEffect("bloomingWind", 0, 0));
                    List<Pair<String, Integer>> eff = new ArrayList<>();
                    eff.add(new Pair<>("bloomingSun", Integer.valueOf(0)));
                    eff.add(new Pair<>("bloomingWind", Integer.valueOf(0)));
                    broadcastMessage(CField.ChangeSpecialMapEffect(eff));
                    break;
                }
                bool1 = ((new Date()).getHours() % 2 == 0);
                if (getCustomTime(993192000) != null) {
                    chr.getClient().send(CField.setSpecialMapEffect("bloomingSun", 1, 1));
                    chr.getClient().send(CField.setSpecialMapEffect("bloomingWind", 1, 1));
                    int buffid = bool1 ? 2024011 : 2024012;
                    MapleItemInformationProvider.getInstance().getItemEffect(buffid).applyTo(chr, true);
                    chr.getClient().send(CField.setSpecialMapEffect("bloomingSun", !bool1 ? 1 : 0, !bool1 ? 1 : 0));
                    chr.getClient().send(CField.setSpecialMapEffect("bloomingWind", !bool1 ? 1 : 0, !bool1 ? 1 : 0));
                    List<Pair<String, Integer>> eff = new ArrayList<>();
                    eff.add(new Pair<>("bloomingSun", Integer.valueOf(bool1 ? 1 : 0)));
                    eff.add(new Pair<>("bloomingWind", Integer.valueOf(bool1 ? 0 : 1)));
                    chr.getClient().send(CField.ChangeSpecialMapEffect(eff));
                    chr.getClient().send(CField.startMapEffect(bool1 ? "따사로운 봄 햇살이 쏟아져 내립니다." : "기분 좋게 시원한 봄바람이 살랑입니다.", bool1 ? 5121112 : 5121113, true));
                }
                break;
            case 993026900:
                chr.getClient().send(SLFCGPacket.ContentsWaiting(chr, 0, new int[]{11, 5, 1, 26}));
                if (chr.getMap().getCustomTime(chr.getMap().getId()) != null) {
                    chr.getClient().send(CField.getClock(chr.getMap().getCustomTime(chr.getMap().getId()).intValue() / 1000));
                }
                break;
            case 310070140:
            case 310070220:
            case 310070450:
                npcid = (getId() == 310070140) ? 2155116 : ((getId() == 310070220) ? 2155117 : 2155118);
                questid = (getId() == 310070140) ? 39116 : ((getId() == 310070220) ? 39125 : 39152);
                pos = (getId() == 310070140) ? new Point(1493, -62) : ((getId() == 310070220) ? new Point(1352, -459) : new Point(483, -573));
                already = false;
                itr = ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.NPC)).values().iterator();
                while (itr.hasNext()) {
                    MapleNPC n = (MapleNPC) itr.next();
                    if (n.getId() == npcid && n.getOwner() == chr) {
                        already = true;
                        break;
                    }
                }
                if (chr.getQuestStatus(questid) == 1 && !already) {
                    MapleNPC npc = MapleLifeFactory.getNPC(npcid);
                    npc.setPosition(pos);
                    npc.setCy(pos.y);
                    npc.setRx0(pos.x);
                    npc.setRx1(pos.x);
                    npc.setFh(getFootholds().findBelow(pos).getId());
                    npc.setCustom(true);
                    npc.setLeft(true);
                    npc.setOwner(chr);
                    addMapObject(npc);
                    chr.getClient().send(CField.NPCPacket.spawnNPCRequestController(npc, true));
                    break;
                }
                if (already && chr.getQuestStatus(questid) != 1) {
                    while (itr.hasNext()) {
                        MapleNPC npc = (MapleNPC) itr.next();
                        if (npc.isCustom() && (npcid == -1 || npc.getId() == npcid) && npc.getOwner().getId() == chr.getId()) {
                            broadcastMessage(CField.NPCPacket.removeNPCController(npc.getObjectId()));
                            broadcastMessage(CField.NPCPacket.removeNPC(npc.getObjectId()));
                            itr.remove();
                        }
                    }
                }
                break;
            case 993194000:
                if (getCustomValue(993194000) == null) {
                    chr.getClient().send(CField.setSpecialMapEffect("studioBlue", 0, 0));
                    chr.getClient().send(CField.setSpecialMapEffect("studioPink", 0, 0));
                    List<Pair<String, Integer>> eff = new ArrayList<>();
                    eff.add(new Pair<>("studioBlue", Integer.valueOf(0)));
                    eff.add(new Pair<>("studioPink", Integer.valueOf(0)));
                    broadcastMessage(CField.ChangeSpecialMapEffect(eff));
                    break;
                }
                sun = ((new Date()).getHours() % 2 == 0);
                if (getCustomTime(993194000) != null) {
                    chr.getClient().send(CField.setSpecialMapEffect("studioBlue", 1, 1));
                    chr.getClient().send(CField.setSpecialMapEffect("studioPink", 1, 1));
                    int buffid = sun ? 2024017 : 2024018;
                    MapleItemInformationProvider.getInstance().getItemEffect(buffid).applyTo(chr, true);
                    chr.getClient().send(CField.setSpecialMapEffect("studioBlue", !sun ? 1 : 0, !sun ? 1 : 0));
                    chr.getClient().send(CField.setSpecialMapEffect("studioPink", !sun ? 1 : 0, !sun ? 1 : 0));
                    List<Pair<String, Integer>> eff = new ArrayList<>();
                    eff.add(new Pair<>("studioBlue", Integer.valueOf(sun ? 1 : 0)));
                    eff.add(new Pair<>("studioPink", Integer.valueOf(sun ? 0 : 1)));
                    chr.getClient().send(CField.ChangeSpecialMapEffect(eff));
                    chr.getClient().send(CField.startMapEffect(sun ? "시청자로부터 블루 하트 선물이 쏟아집니다!" : "시청자로부터 핑크 하트 선물이 쏟아집니다!", sun ? 5121114 : 5121115, true));
                }
                break;
            default:
                final int[] array;
                skilllist = array = new int[]{80001427, 80001428, 80001432, 80001762, 80001757, 80001755, 80001878, 80002888, 80002889, 80002890};
                for (final Integer skillid : array) {
                    if (chr.getBuffedValue(skillid)) {
                        chr.cancelEffect(chr.getBuffedEffect(skillid));
                    }
                }
                if (GameConstants.isMechanic(chr.getJob())) {
                    List<MapleSummon> remove = new ArrayList<>();
                    for (MapleSummon mapleSummon : chr.getSummons(35111002)) {
                        remove.add(mapleSummon);
                    }
                    for (MapleSummon ss : remove) {
                        ss.removeSummon(this, false);
                    }
                }
                chr.getClient().getSession().writeAndFlush(CField.UseSkillWithUI(0, 0, 0));
                chr.getClient().getSession().writeAndFlush(MobPacket.CorruptionChange((byte) 0, 0));
                break;
        }
        if (chr.getV("bossPractice") != null
                && Integer.parseInt(chr.getV("bossPractice")) == 1) {
            chr.getClient().send(CField.getPracticeMode(true));
        }
        if (chr.getSkillCustomValue(9110) != null) {
            if (chr.getSkillCustomTime(9110).intValue() > 0 && (chr.getMapId() / 100000 == 9530 || chr.getMapId() / 100000 == 9540)) {
                chr.getClient().send(CField.getClock(chr.getSkillCustomTime(9110).intValue() / 1000));
            } else if (chr.getSkillCustomTime(9110).intValue() > 0) {
                chr.removeSkillCustomInfo(9110);
            }
        }
        if (chr.Stigma > 0) {
            if (!GameConstants.보스맵(getId())) {
                chr.Stigma = 0;
                Map<SecondaryStat, Pair<Integer, Integer>> dds = new HashMap<>();
                dds.put(SecondaryStat.Stigma, new Pair<>(Integer.valueOf(chr.Stigma), Integer.valueOf(0)));
                chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(dds, chr));
                broadcastMessage(chr, CWvsContext.BuffPacket.cancelForeignBuff(chr, dds), false);
            }
        }
        if (chr.getBuffedValue(80002404)) {
            if (getId() != 450008850 && getId() != 450008250) {
                chr.cancelEffect(chr.getBuffedEffect(80002404));
            }
        }
        for (int i = 0; i < 3; i++) {
            if (chr.getPet(i) != null) {
                chr.getClient().getSession().writeAndFlush(PetPacket.updatePet(chr, chr.getPet(i), chr.getInventory(MapleInventoryType.CASH).getItem(chr.getPet(i).getInventoryPosition()), false, chr.getPetLoot()));
                if (chr.getMapId() != ServerConstants.warpMap) {
                    chr.getPet(i).setPos(chr.getPosition());
                    broadcastMessage(chr, PetPacket.showPet(chr, chr.getPet(i), false, false), true);
                }
            }
        }
        if (chr.getAndroid() != null) {
            chr.getAndroid().setPos(chr.getPosition());
            broadcastMessage(CField.spawnAndroid(chr, chr.getAndroid()));
        }
        if (chr.getParty() != null) {
            chr.silentPartyUpdate();
            chr.getClient().getSession().writeAndFlush(CWvsContext.PartyPacket.updateParty(chr.getClient().getChannel(), chr.getParty(), PartyOperation.SILENT_UPDATE, null));
            chr.updatePartyMemberHP();
            chr.receivePartyMemberHP();
        }
        if (!this.onFirstUserEnter.isEmpty()
                && isFirstUserEnter()) {
            setFirstUserEnter(false);
            MapScriptMethods.startScript_FirstUser(chr.getClient(), this.onFirstUserEnter);
        }
        if (!this.onUserEnter.isEmpty()) {
            MapScriptMethods.startScript_User(chr.getClient(), this.onUserEnter);
        }
        List<MapleSummon> allSummons = chr.getSummons();
        Iterator<MapleSummon> s = allSummons.iterator();
        while (s.hasNext()) {
            MapleSummon summon = s.next();
            if (summon.getMovementType() != SummonMovementType.STATIONARY || summon.getSkill() == 152101000) {
                summon.setPosition(chr.getTruePosition());
                chr.addVisibleMapObject(summon);
                spawnSummon(summon);
            }
        }
        if (this.mapEffect != null) {
            this.mapEffect.sendStartData(chr.getClient());
        }
        if (chr.getBuffedValue(SecondaryStat.RideVehicle) != null && !GameConstants.isResist(chr.getJob())
                && FieldLimitType.Mount.check(this.fieldLimit)) {
            chr.cancelEffectFromBuffStat(SecondaryStat.RideVehicle);
        }
        if (chr.getEventInstance() != null && chr.getEventInstance().isTimerStarted()) {
            if (chr.inPVP()) {
                chr.getClient().getSession().writeAndFlush(CField.getPVPClock(Integer.parseInt(chr.getEventInstance().getProperty("type")), (int) (chr.getEventInstance().getTimeLeft() / 1000L)));
            } else {
                chr.getClient().getSession().writeAndFlush(CField.getClock((int) (chr.getEventInstance().getTimeLeft() / 1000L)));
            }
        }
        if (hasClock()) {
            Calendar cal = Calendar.getInstance();
            chr.getClient().getSession().writeAndFlush(CField.getClockTime(cal.get(11), cal.get(12), cal.get(13)));
        }
        if (getMapTimer() > 0L) {
            chr.getClient().getSession().writeAndFlush(CField.getClock((int) ((getMapTimer() - System.currentTimeMillis()) / 1000L)));
        }
        chr.getClient().getSession().writeAndFlush(CField.specialChair(chr, true, true, true, null));
        if (isElitebossmap()) {
            chr.getClient().send(CField.getClock(this.elitetime));
            broadcastMessage(CField.specialMapEffect(2, true, "Bgm36.img/RoyalGuard", "Effect/EliteMobEff.img/eliteMonsterFrame", "Effect/EliteMobEff.img/eliteMonsterEffect", "", ""));
        } else if (isElitebossrewardmap()) {
            if (getCustomValue(210403) != null) {
                broadcastMessage(SLFCGPacket.milliTimer(getCustomTime(210403).intValue()));
            }
            broadcastMessage(CField.specialMapEffect(3, true, "Bgm36.img/HappyTimeShort", "Map/Map/Map9/924050000.img/back", "Effect/EliteMobEff.img/eliteBonusStage", "", ""));
        }
        if (this.burning > 0 && getAllNormalMonstersThreadsafe().size() > 0 && !isTown() && !GameConstants.로미오줄리엣(getId()) && !GameConstants.사냥컨텐츠맵(getId()) && isSpawnPoint() && !GameConstants.isContentsMap(getId())) {
            chr.getClient().getSession().writeAndFlush(CField.playSound("Sound/FarmSE.img/boxResult"));
            chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showBurningFieldEffect("#fn나눔고딕 ExtraBold##fs26#          버닝 " + this.burning + "단계 : 경험치 " + (this.burning * 10) + "% 추가지급!!          "));
            if (chr.getKeyValue(51351, "startquestid") == 49011L) {
                chr.setKeyValue(51351, "queststat", "3");
                chr.getClient().send(CWvsContext.updateSuddenQuest((int) chr.getKeyValue(51351, "midquestid"), false, PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L, "count=1;Quest=" + chr.getKeyValue(51351, "startquestid") + ";state=3;"));
                chr.getClient().send(CWvsContext.updateSuddenQuest((int) chr.getKeyValue(51351, "startquestid"), false, chr.getKeyValue(51351, "endtime"), "BTField=1;"));
            }
        }
        if (this.rune != null && !chr.getBuffedValue(80002282) && this.runeCurse > 0 && isSpawnPoint() && !GameConstants.보스맵(this.mapid) && !GameConstants.isContentsMap(this.mapid)) {
            chr.getClient().getSession().writeAndFlush(CField.runeCurse("룬을 해방하여 엘리트 보스의 저주를 풀어야 합니다!!\\n저주 " + this.runeCurse + "단계 :  경험치 획득, 드롭률 " + getRuneCurseDecrease() + "% 감소 효과 적용 중", false));
        }
        if (chr.getKeyValue(210416, "TotalDeadTime") > 0L && isSpawnPoint()) {
            chr.getClient().getSession().writeAndFlush(CField.PenaltyMsg("경험치 획득, 드롭률 80% 감소 효과 적용 중!\r\n호신부적 아이템을 사용하면 즉시 해제할 수 있습니다.", 338, 10000, 180));
        }
        if (GameConstants.isYeti(chr.getJob())) {
            if (chr.getMapId() == 993191400) {
                NPCScriptManager.getInstance().start(chr.getClient(), 2007, "YetiTuto0");
            }
        } else if (GameConstants.isPinkBean(chr.getJob())
                && chr.getMapId() == 927030090) {
            NPCScriptManager.getInstance().start(chr.getClient(), 2007, "PinkBeanTuto0");
        }
        if (getNumMonsters() > 0 && (this.mapid == 280030001 || this.mapid == 240060201 || this.mapid == 280030000 || this.mapid == 240060200 || this.mapid == 220080001 || this.mapid == 541020800 || this.mapid == 541010100)) {
            String music = "Bgm09/TimeAttack";
            switch (this.mapid) {
                case 240060200:
                case 240060201:
                    music = "Bgm14/HonTale";
                    break;
                case 280030000:
                case 280030001:
                    music = "Bgm06/FinalFight";
                    break;
            }
            chr.getClient().getSession().writeAndFlush(CField.musicChange(music));
        }
        if (GameConstants.isEvan(chr.getJob()) && chr.getJob() >= 2200) {
            if (chr.getDragon() == null) {
                chr.makeDragon();
            } else {
                chr.getDragon().setPosition(chr.getPosition());
            }
            if (chr.getDragon() != null) {
                broadcastMessage(CField.spawnDragon(chr.getDragon()));
            }
        }
        if (this.permanentWeather > 0) {
            chr.getClient().getSession().writeAndFlush(CField.startMapEffect("", this.permanentWeather, false));
        }
        if (getNodez().getEnvironments().size() > 0 && this.mapid != 450004250 && this.mapid != 450004550 && this.mapid != 450003920) {
            chr.getClient().getSession().writeAndFlush(CField.getUpdateEnvironment(getNodez().getEnvironments()));
        }
        if (chr.getBuffedValue(SecondaryStat.RepeatEffect) != null) {
            int skillid = chr.getBuffedEffect(SecondaryStat.RepeatEffect).getSourceId();
            if (GameConstants.isAngelicBlessBuffEffectItem(skillid)) {
                EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<>(SecondaryStat.class);
                statups.put(SecondaryStat.RepeatEffect, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
                broadcastMessage(CWvsContext.BuffPacket.giveForeignBuff(chr, statups, chr.getBuffedEffect(SecondaryStat.RepeatEffect)));
            }
        }
        
            for (int i = 0; i < 8; i++) {
            chr.setEffect(i, 0);
        }
                
        if (chr.getSkillCustomValue0(60524) > 0L) {
            chr.setSkillCustomInfo(60524, 0L, 0L);
        }
    }

    public int getNumItems() {
        return ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.ITEM)).size();
    }

    public int getNumMonsters() {
        return ((ConcurrentHashMap) this.mapobjects.get(MapleMapObjectType.MONSTER)).size();
    }

    public final EventManager getEMByMap() {
        String em = null;
        switch (this.mapid) {
            case 105100400:
                em = "BossBalrog_EASY";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 105100300:
                em = "BossBalrog_NORMAL";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 280030000:
                em = "ZakumBattle";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 240060200:
                em = "HorntailBattle";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 280030001:
                em = "ChaosZakum";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 240060201:
                em = "ChaosHorntail";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 270050100:
                em = "PinkBeanBattle";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000111:
                em = "NamelessMagicMonster";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000211:
                em = "Vergamot";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000311:
                em = "2095_tokyo";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000411:
                em = "Dunas";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000611:
                em = "Nibergen";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000711:
                em = "Dunas2";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000801:
            case 802000802:
            case 802000803:
                em = "CoreBlaze";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 802000821:
            case 802000823:
                em = "Aufhaven";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 211070100:
            case 211070101:
            case 211070110:
                em = "VonLeonBattle";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 551030200:
                em = "ScarTarBattle";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
            case 271040100:
                em = "CygnusBattle";
                return ChannelServer.getInstance(this.channel).getEventSM().getEventManager(em);
        }
        return null;
    }

    public final void removePlayer(MapleCharacter chr) {
        if (this.everlast) {
            returnEverLastItem(chr);
        }
        this.characters.remove(chr);
        removeMapObject(chr);
        chr.checkFollow();
        chr.removeExtractor();
        broadcastMessage(CField.removePlayerFromMap(chr.getId()));
        if (this.characters.size() == 0) {
            setFirstUserEnter(true);
        }
        List<MapleSummon> allSummons = chr.getSummons();
        for (MapleSummon summon : allSummons) {
            if (summon.getSkill() == 152101000) {
                chr.CrystalCharge = summon.getEnergy();
                continue;
            }
            if (summon.getMovementType() != SummonMovementType.STATIONARY) {
                summon.removeSummon(this, true);
            }
        }
        checkStates(chr.getName());
        if (this.mapid == 109020001) {
            chr.canTalk(true);
        }
        chr.leaveMap(this);
    }

    public final void broadcastMessage(byte[] packet) {
        broadcastMessage(null, packet, Double.POSITIVE_INFINITY, null);
    }

    public final void broadcastMessage(MapleCharacter source, byte[] packet, boolean repeatToSource) {
        broadcastMessage(repeatToSource ? null : source, packet, Double.POSITIVE_INFINITY, source.getTruePosition());
    }

    public final void broadcastMessage(byte[] packet, Point rangedFrom) {
        broadcastMessage(null, packet, GameConstants.maxViewRangeSq(), rangedFrom);
    }

    public final void broadcastMessage(MapleCharacter source, byte[] packet, Point rangedFrom) {
        broadcastMessage(source, packet, GameConstants.maxViewRangeSq(), rangedFrom);
    }

    public void broadcastMessage(MapleCharacter source, byte[] packet, double rangeSq, Point rangedFrom) {
        Iterator<MapleCharacter> itr = this.characters.iterator();
        while (itr.hasNext()) {
            MapleCharacter chr = itr.next();
            if (chr != source) {
                if (rangeSq < Double.POSITIVE_INFINITY) {
                    if (rangedFrom.distanceSq(chr.getTruePosition()) <= rangeSq) {
                        chr.getClient().getSession().writeAndFlush(packet);
                    }
                    continue;
                }
                chr.getClient().getSession().writeAndFlush(packet);
            }
        }
    }

    private void sendObjectPlacement(MapleCharacter c) {
        if (c == null) {
            return;
        }
        for (MapleMapObject o : getMapObjectsInRange(c.getTruePosition(), c.getRange(), GameConstants.rangedMapobjectTypes)) {
            if (o.getType() == MapleMapObjectType.REACTOR
                    && !((MapleReactor) o).isAlive()) {
                continue;
            }
            o.sendSpawnData(c.getClient());
            c.addVisibleMapObject(o);
        }
    }

    public final List<MaplePortal> getPortalsInRange(Point from, double rangeSq) {
        List<MaplePortal> ret = new ArrayList<>();
        for (MaplePortal type : this.portals.values()) {
            if (from.distanceSq(type.getPosition()) <= rangeSq && type.getTargetMapId() != this.mapid && type.getTargetMapId() != 999999999) {
                ret.add(type);
            }
        }
        return ret;
    }

    public final List<MapleMapObject> getMapObjectsInRange(Point from, double rangeSq) {
        List<MapleMapObject> ret = new ArrayList<>();
        for (MapleMapObjectType type : MapleMapObjectType.values()) {
            Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(type)).values().iterator();
            while (itr.hasNext()) {
                MapleMapObject mmo = itr.next();
                if (from.distanceSq(mmo.getTruePosition()) <= rangeSq) {
                    ret.add(mmo);
                }
            }
        }
        return ret;
    }

    public List<MapleMapObject> getItemsInRange(Point from, double rangeSq) {
        return getMapObjectsInRange(from, rangeSq, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.ITEM}));
    }

    public final List<MapleMapObject> getMapObjectsInRange(Point from, double rangeSq, List<MapleMapObjectType> MapObject_types) {
        List<MapleMapObject> ret = new ArrayList<>();
        for (MapleMapObjectType type : MapObject_types) {
            Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(type)).values().iterator();
            while (itr.hasNext()) {
                MapleMapObject mmo = itr.next();
                if (from.distanceSq(mmo.getTruePosition()) <= rangeSq) {
                    ret.add(mmo);
                }
            }
        }
        return ret;
    }

    public final List<MapleMapObject> getMapObjectsInRect(Rectangle box, List<MapleMapObjectType> MapObject_types) {
        List<MapleMapObject> ret = new ArrayList<>();
        for (MapleMapObjectType type : MapObject_types) {
            Iterator<MapleMapObject> itr = ((ConcurrentHashMap) this.mapobjects.get(type)).values().iterator();
            while (itr.hasNext()) {
                MapleMapObject mmo = itr.next();
                if (box.contains(mmo.getTruePosition())) {
                    ret.add(mmo);
                }
            }
        }
        return ret;
    }

    public final List<MapleCharacter> getCharactersIntersect(Rectangle box) {
        List<MapleCharacter> ret = new ArrayList<>();
        Iterator<MapleCharacter> itr = this.characters.iterator();
        while (itr.hasNext()) {
            MapleCharacter chr = itr.next();
            if (chr.getBounds().intersects(box)) {
                ret.add(chr);
            }
        }
        return ret;
    }

    public final List<MapleCharacter> getPlayersInRectAndInList(Rectangle box, List<MapleCharacter> chrList) {
        List<MapleCharacter> character = new LinkedList<>();
        Iterator<MapleCharacter> ltr = this.characters.iterator();
        while (ltr.hasNext()) {
            MapleCharacter a = ltr.next();
            if (chrList.contains(a) && box.contains(a.getTruePosition())) {
                character.add(a);
            }
        }
        return character;
    }

    public final void addPortal(MaplePortal myPortal) {
        this.portals.put(Integer.valueOf(myPortal.getId()), myPortal);
    }

    public final MaplePortal getPortal(String portalname) {
        for (MaplePortal port : this.portals.values()) {
            if (port.getName().equals(portalname)) {
                return port;
            }
        }
        return null;
    }

    public final MaplePortal getPortal(int portalid) {
        return this.portals.get(Integer.valueOf(portalid));
    }

    public final List<MaplePortal> getPortalSP() {
        List<MaplePortal> res = new LinkedList<>();
        for (MaplePortal port : this.portals.values()) {
            if (port.getName().equals("sp")) {
                res.add(port);
            }
        }
        return res;
    }

    public final void resetPortals() {
        for (MaplePortal port : this.portals.values()) {
            port.setPortalState(true);
        }
    }

    public final void setFootholds(MapleFootholdTree footholds) {
        this.footholds = footholds;
    }

    public final MapleFootholdTree getFootholds() {
        return this.footholds;
    }

    public final int getNumSpawnPoints() {
        return this.monsterSpawn.size();
    }

    public final void loadMonsterRate(boolean first) {//몹 젠
        createMobInterval = 2000;
        int spawnSize = this.monsterSpawn.size();
        if (spawnSize >= 20 || this.partyBonusRate > 0) {
            this.maxRegularSpawn = Math.round(spawnSize / this.monsterRate);
        } else {
            this.maxRegularSpawn = (int) Math.ceil((spawnSize * this.monsterRate));
        }
        if (this.fixedMob > 0) {
            this.maxRegularSpawn = this.fixedMob;
        } else if (this.maxRegularSpawn <= 2) {
            this.maxRegularSpawn = 2;
        } else if (this.maxRegularSpawn > spawnSize) {
            this.maxRegularSpawn = Math.max(10, spawnSize);
        }
        Collection<Spawns> newSpawn = new LinkedList<>();
        Collection<Spawns> newBossSpawn = new LinkedList<>();
        for (Spawns s : this.monsterSpawn) {
            if (s.getCarnivalTeam() >= 2) {
                continue;
            }
            if (s.getMonster().isBoss()) {
                newBossSpawn.add(s);
                continue;
            }
            newSpawn.add(s);
        }
        this.monsterSpawn.clear();
        this.monsterSpawn.addAll(newBossSpawn);
        this.monsterSpawn.addAll(newSpawn);
        if (first && spawnSize > 0) {
            this.lastSpawnTime = System.currentTimeMillis();
            if (this.barrierArc == 0 && this.barrierAut == 0 && getId() != 310070200 && getId() != 310070210 && getId() != 310070220) {
                this.createMobInterval = (short) (this.createMobInterval / 3);
            } else if (getId() != 450006000 && getId() != 450006010 && getId() != 450006020 && getId() != 450006030 && getId() != 450006040) {
                this.createMobInterval = (short) (this.createMobInterval - 2000);
            } else if (getId() == 450006000 || getId() == 450006010 || getId() == 450006020 || getId() == 450006030 || getId() == 450006040) {
                this.createMobInterval = (short) (this.createMobInterval + 2000);
            }
            if (GameConstants.isForceRespawn(this.mapid)) {
                this.createMobInterval = 15000;
            }
            respawn(false);
        }
    }

    public final SpawnPoint addMonsterSpawn(MapleMonster monster, int mobTime, byte carnivalTeam, String msg) {
        Point newpos = calcPointBelow(monster.getPosition());
        newpos.y--;
        SpawnPoint sp = new SpawnPoint(monster, newpos, mobTime, carnivalTeam, msg);
        if (carnivalTeam > -1) {
            this.monsterSpawn.add(0, sp);
        } else {
            this.monsterSpawn.add(sp);
        }
        return sp;
    }

    public final void addAreaMonsterSpawn(MapleMonster monster, Point pos1, Point pos2, Point pos3, int mobTime, String msg, boolean shouldSpawn) {
        pos1 = calcPointBelow(pos1);
        pos2 = calcPointBelow(pos2);
        pos3 = calcPointBelow(pos3);
        if (monster == null) {
            System.out.println(this.mapid + "맵의 addAreaMonsterSpawn의 몹 데이터가 없음.");
            return;
        }
        if (pos1 != null) {
            pos1.y--;
        }
        if (pos2 != null) {
            pos2.y--;
        }
        if (pos3 != null) {
            pos3.y--;
        }
        if (pos1 == null && pos2 == null && pos3 == null) {
            System.out.println("WARNING: mapid " + this.mapid + ", monster " + monster.getId() + " could not be spawned.");
            return;
        }
        if (pos1 != null) {
            if (pos2 == null) {
                pos2 = new Point(pos1);
            }
            if (pos3 == null) {
                pos3 = new Point(pos1);
            }
        } else if (pos2 != null) {
            if (pos1 == null) {
                pos1 = new Point(pos2);
            }
            if (pos3 == null) {
                pos3 = new Point(pos2);
            }
        } else if (pos3 != null) {
            if (pos1 == null) {
                pos1 = new Point(pos3);
            }
            if (pos2 == null) {
                pos2 = new Point(pos3);
            }
        }
        this.monsterSpawn.add(new SpawnPointAreaBoss(monster, pos1, pos2, pos3, mobTime, msg, shouldSpawn));
    }

    public final List<MapleCharacter> getCharacters() {
        return getCharactersThreadsafe();
    }

    public final List<MapleCharacter> getCharactersThreadsafe() {
        List<MapleCharacter> chars = new ArrayList<>();
        Iterator<MapleCharacter> itr = this.characters.iterator();
        while (itr.hasNext()) {
            MapleCharacter chr = itr.next();
            chars.add(chr);
        }
        return chars;
    }

    public final MapleCharacter getCharacterByName(String id) {
        Iterator<MapleCharacter> itr = this.characters.iterator();
        while (itr.hasNext()) {
            MapleCharacter mc = itr.next();
            if (mc.getName().equalsIgnoreCase(id)) {
                return mc;
            }
        }
        return null;
    }

    public final MapleCharacter getCharacterById_InMap(int id) {
        return getCharacterById(id);
    }

    public final MapleCharacter getCharacterById(int id) {
        Iterator<MapleCharacter> itr = this.characters.iterator();
        while (itr.hasNext()) {
            MapleCharacter mc = itr.next();
            if (mc.getId() == id) {
                return mc;
            }
        }
        return null;
    }

    public final void updateMapObjectVisibility(MapleCharacter chr, MapleMapObject mo) {
        if (chr == null) {
            return;
        }
        if (!chr.isMapObjectVisible(mo)) {
            System.out.println(mo.getType());
            if (mo.getType() == MapleMapObjectType.MIST || mo.getType() == MapleMapObjectType.EXTRACTOR || mo.getType() == MapleMapObjectType.SUMMON || mo.getType() == MapleMapObjectType.RUNE || mo.getType() == MapleMapObjectType.MagicSword || mo instanceof MechDoor || mo.getTruePosition().distanceSq(chr.getTruePosition()) <= mo.getRange()) {
                chr.addVisibleMapObject(mo);
                mo.sendSpawnData(chr.getClient());
            }
        } else if (!(mo instanceof MechDoor) && mo.getType() != MapleMapObjectType.MIST && mo.getType() != MapleMapObjectType.EXTRACTOR && mo.getType() != MapleMapObjectType.SUMMON && mo.getType() != MapleMapObjectType.RUNE && mo.getType() != MapleMapObjectType.MagicSword && mo.getTruePosition().distanceSq(chr.getTruePosition()) > mo.getRange()) {
            chr.removeVisibleMapObject(mo);
            mo.sendDestroyData(chr.getClient());
        } else if (mo.getType() == MapleMapObjectType.MONSTER
                && chr.getPosition().distanceSq(mo.getPosition()) <= GameConstants.maxViewRangeSq()) {
            updateMonsterController((MapleMonster) mo);
        }
    }

    public void moveMonster(MapleMonster monster, Point reportedPos) {
        monster.setPosition(reportedPos);
        Iterator<MapleCharacter> itr = this.characters.iterator();
        while (itr.hasNext()) {
            MapleCharacter mc = itr.next();
            updateMapObjectVisibility(mc, monster);
        }
    }

    public void movePlayer(MapleCharacter player, Point newPosition) {//mush
        player.setPosition(newPosition);
        for (MapleMapObject mo : player.getVisibleMapObjects()) {
            if (mo != null && this.getMapObject(mo.getObjectId(), mo.getType()) == mo) {
                this.updateMapObjectVisibility(player, mo);
                continue;
            }
            if (mo == null) {
                continue;
            }
            player.getVisibleMapObjects().remove(mo);
        }
        for (MapleMapObject mo : this.getMapObjectsInRange(player.getTruePosition(), player.getRange())) {
            if (mo == null || player.getVisibleMapObjects().contains(mo) || mo.getType() == MapleMapObjectType.MagicSword) {
                continue;
            }
            mo.sendSpawnData(player.getClient());
            player.getVisibleMapObjects().add(mo);
        }
    }

    public MaplePortal findClosestSpawnpoint(Point from) {
        MaplePortal closest = getPortal(0);
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (MaplePortal portal : this.portals.values()) {
            double distance = portal.getPosition().distanceSq(from);
            if (portal.getType() >= 0 && portal.getType() <= 2 && distance < shortestDistance && portal.getTargetMapId() == 999999999) {
                closest = portal;
                shortestDistance = distance;
            }
        }
        return closest;
    }

    public MaplePortal findClosestPortal(Point from) {
        MaplePortal closest = getPortal(0);
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (MaplePortal portal : this.portals.values()) {
            double distance = portal.getPosition().distanceSq(from);
            if (distance < shortestDistance) {
                closest = portal;
                shortestDistance = distance;
            }
        }
        return closest;
    }

    public String spawnDebug() {
        StringBuilder sb = new StringBuilder("Mobs in map : ");
        sb.append(getNumMonsters());
        sb.append(" spawnedMonstersOnMap: ");
        sb.append(this.spawnedMonstersOnMap);
        sb.append(" spawnpoints: ");
        sb.append(this.monsterSpawn.size());
        sb.append(" maxRegularSpawn: ");
        sb.append(this.maxRegularSpawn);
        sb.append(" monster rate: ");
        sb.append(this.monsterRate);
        sb.append(" fixed: ");
        sb.append(this.fixedMob);
        return sb.toString();
    }

    public int characterSize() {
        return this.characters.size();
    }

    public final int getMapObjectSize() {
        return this.mapobjects.size() + getCharactersSize() - this.characters.size();
    }

    public final int getCharactersSize() {
        int ret = 0;
        Iterator<MapleCharacter> ltr = this.characters.iterator();
        while (ltr.hasNext()) {
            MapleCharacter chr = ltr.next();
            ret++;
        }
        return ret;
    }

    public MapleCharacter getCharacter(int cid) {
        MapleCharacter ret = null;
        Iterator<MapleCharacter> ltr = this.characters.iterator();
        while (ltr.hasNext()) {
            MapleCharacter chr = ltr.next();
            if (chr.getId() == cid) {
                return chr;
            }
        }
        return ret;
    }

    public Collection<MaplePortal> getPortals() {
        return Collections.unmodifiableCollection(this.portals.values());
    }

    private class ActivateItemReactor implements Runnable {

        private MapleMapItem mapitem;

        private MapleReactor reactor;

        private MapleClient c;

        public ActivateItemReactor(MapleMapItem mapitem, MapleReactor reactor, MapleClient c) {
            this.mapitem = mapitem;
            this.reactor = reactor;
            this.c = c;
        }

        public void run() {
            if (this.mapitem != null && this.mapitem == MapleMap.this.getMapObject(this.mapitem.getObjectId(), this.mapitem.getType()) && !this.mapitem.isPickedUp()) {
                this.mapitem.expire(MapleMap.this);
                this.reactor.hitReactor(this.c);
                this.reactor.setTimerActive(false);
                if (this.reactor.getDelay() > 0) {
                    Timer.MapTimer.getInstance().schedule(new Runnable() {
                        public void run() {
                            MapleMap.ActivateItemReactor.this.reactor.forceHitReactor((byte) 0, MapleMap.ActivateItemReactor.this.c.getPlayer().getId());
                        }
                    }, this.reactor.getDelay());
                }
            } else {
                this.reactor.setTimerActive(false);
            }
        }
    }

/*      */   public void setPartyCount(int count) {
/* 5944 */     this.partyquest = count;
/*      */   }
/*      */   
/*      */   public final int getPartyCount() {
/* 5948 */     return this.partyquest;
/*      */   }
/*      */   
/*      */   public void setMoonCake(int count) {
/* 5952 */     this.mooncake = count;
/*      */   }
/*      */   
/*      */   public final int getMoonCake() {
/* 5956 */     return this.mooncake;
/*      */   }
/*      */   
/*      */   public void setKerningPQ(int count) {
/* 6998 */     this.KerningPQ = count;
/*      */   }
/*      */   
/*      */   public final int getKerningPQ() {
/* 7002 */     return this.KerningPQ;
/*      */   }
/*      */   public void setRPTicket(int count) {
/* 7005 */     this.RPTicket = count;
/*      */   }
/*      */   
/*      */   public final int getRPTicket() {
/* 7009 */     return this.RPTicket;
/*      */   }
/*      */   
/*      */   public void setrpportal(int count) {
/* 7013 */     this.rpportal = count;
/*      */   }
/*      */   
/*      */   public final int getrpportal() {
/* 7017 */     return this.rpportal;
/*      */   }
/*      */   
/*      */   public void setMonstermarble(int count) {
/* 7021 */     this.Monstermarble = count;
/*      */   }
/*      */   
/*      */   public final int getMonstermarble() {
/* 7025 */     return this.Monstermarble;
/*      */   }


/*      */   public void partyrespawn() {
/* 5960 */     if (this.partyquest < 4) {
/* 5961 */       this.partyquest++;
/*      */     } else {
/* 5963 */       this.partyquest = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void respawn(boolean force) {
/* 5969 */     respawn(force, System.currentTimeMillis());
/*      */   }
/*      */   
/*      */   public void respawn(boolean force, long now) {
/* 5973 */     if (this.KerningPQ == 1) {
/*      */       return;
/*      */     }
/* 5976 */     if (this.eliteBossAppeared) {
/*      */       return;
/*      */     }
/*      */     
/* 5980 */     if (this.partyquest != 0) {
/*      */       return;
/*      */     }
/*      */     
/* 5984 */     this.lastSpawnTime = now;
/*      */     
/* 5986 */     int num = getNumMonsters();
/* 5987 */     if (this.spawnedMonstersOnMap.get() != num) {
/* 5988 */       this.spawnedMonstersOnMap.set(num);
/*      */     }
/*      */     
/* 5991 */     if (force) {
/* 5992 */       int numShouldSpawn = this.monsterSpawn.size() - this.spawnedMonstersOnMap.get();
/*      */       
/* 5994 */       if (numShouldSpawn > 0) {
/* 5995 */         int spawned = 0;
/*      */         
/* 5997 */         for (Spawns spawnPoint : this.monsterSpawn) {
/* 5998 */           if (spawnPoint.getMonster().getLevel() < 200 && 
/* 5999 */             this.createMobInterval != 8000) {
/* 6000 */             this.maxRegularSpawn = 200;
/* 6001 */             this.createMobInterval = 8000;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 6006 */         for (Spawns spawnPoint : this.monsterSpawn) {
/* 6007 */           spawnPoint.spawnMonster(this);
/* 6008 */           spawned++;
/* 6009 */           if (spawned >= numShouldSpawn) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/* 6015 */       int numShouldSpawn = (GameConstants.isForceRespawn(this.mapid) ? this.monsterSpawn.size() : this.maxRegularSpawn) - this.spawnedMonstersOnMap.get();
/* 6016 */       if (numShouldSpawn > 0) {
/* 6017 */         int spawned = 0;
/*      */         
/* 6019 */         List<Spawns> randomSpawn = new ArrayList<>(this.monsterSpawn);
/* 6020 */         Collections.shuffle(randomSpawn);
/*      */         
/* 6022 */         List<Spawns> realSpawn = new ArrayList<>();
/*      */         
/* 6024 */         for (Spawns spawnPoint : randomSpawn) {
/*      */ 
/*      */ 
/*      */           
/* 6028 */           if (spawnPoint.getMonster().getLevel() < 200 && 
/* 6029 */             this.createMobInterval != 8000) {
/* 6030 */             this.maxRegularSpawn = 200;
/* 6031 */             this.createMobInterval = 8000;
/*      */           } 
/*      */           
/* 6034 */           if (spawnPoint.shouldSpawn(this.lastSpawnTime)) {
/* 6035 */             realSpawn.add(spawnPoint);
/* 6036 */             spawnPoint.spawnMonster(this);
/* 6037 */             spawned++;
/* 6038 */             if (spawned >= numShouldSpawn) {
/*      */               break;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }

    public String getSnowballPortal() {
        int[] teamss = new int[2];
        for (MapleCharacter chr : this.characters) {
            if ((chr.getTruePosition()).y > -80) {
                teamss[0] = teamss[0] + 1;
                continue;
            }
            teamss[1] = teamss[1] + 1;
        }
        if (teamss[0] > teamss[1]) {
            return "st01";
        }
        return "st00";
    }

    public boolean isDisconnected(int id) {
        return this.dced.contains(Integer.valueOf(id));
    }

    public void addDisconnected(int id) {
        this.dced.add(Integer.valueOf(id));
    }

    public void resetDisconnected() {
        this.dced.clear();
    }

    public final void disconnectAll() {
        for (MapleCharacter chr : getCharactersThreadsafe()) {
            if (!chr.isGM()) {
                chr.getClient().disconnect(true, false);
                chr.getClient().getSession().close();
            }
        }
    }

    public List<MapleNPC> getAllNPCs() {
        return getAllNPCsThreadsafe();
    }

    public List<MapleNPC> getAllNPCsThreadsafe() {
        final ArrayList<MapleNPC> ret = new ArrayList<MapleNPC>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.NPC).values()) {
            ret.add((MapleNPC) mmo);
        }
        return ret;
    }

    public final void resetNPCs() {
        removeNpc(-1);
    }

    public final void resetPQ(int level) {
        resetFully();
        for (MapleMonster mons : getAllMonstersThreadsafe()) {
            mons.changeLevel(level, true);
        }
        resetSpawnLevel(level);
    }

    public final void resetSpawnLevel(int level) {
        for (Spawns spawn : this.monsterSpawn) {
            if (spawn instanceof SpawnPoint) {
                ((SpawnPoint) spawn).setLevel(level);
            }
        }
    }

    public final void resetFully() {
        resetFully(true);
    }

    public final void resetFully(boolean respawn) {
        killAllMonsters(true);
        reloadReactors();
        removeDrops();
        removeMists();
        resetNPCs();
        resetSpawns();
        resetDisconnected();
        cancelSquadSchedule(true);
        resetPortals();
        setFirstUserEnter(true);
        resetEnvironment();
        removeAllFlyingSword();
        setLucidCount(0);
        setLucidUseCount(0);
        setReqTouched(0);
        setStigmaDeath(0);
        this.PapulratusPatan = 0;
        this.PapulratusTime = 0;
        this.Papullatushour = 0;
        this.Papullatusminute = 0;
        this.Mapcoltime = 0;
        this.customInfo.clear();
        for (SpiderWeb web : getAllSpiderWeb()) {
            broadcastMessage(MobPacket.BossWill.willSpider(false, web));
            removeMapObject(web);
        }
        switch (getId()) {
            case 220080100:
            case 220080200:
            case 220080300:
                for (MapleReactor reactor : getAllReactor()) {
                    if (reactor != null
                            && reactor.getReactorId() != 2208011 && reactor.getReactorId() != 2201004) {
                        reactor.forceHitReactor((byte) 1, 0);
                    }
                }
                break;
        }
        if (respawn) {
            respawn(true);
        }
    }

    public void resetSpiderWeb() {
        for (SpiderWeb web : getAllSpiderWeb()) {
            broadcastMessage(MobPacket.BossWill.willSpider(false, web));
            removeMapObject(web);
        }
    }

    public final void cancelSquadSchedule(boolean interrupt) {
        this.squadTimer = false;
        this.checkStates = true;
        if (this.squadSchedule != null) {
            this.squadSchedule.cancel(interrupt);
            this.squadSchedule = null;
        }
    }

    public final void removeDrops() {
        List<MapleMapItem> items = getAllItemsThreadsafe();
        for (MapleMapItem i : items) {
            i.expire(this);
        }
    }

    public final void removeMists() {
        List<MapleMist> mists = getAllMistsThreadsafe();
        for (MapleMist m : mists) {
            broadcastMessage(CField.removeMist(m));
            removeMapObject(m);
        }
    }

    public final void resetAllSpawnPoint(int mobid, int mobTime) {
        Collection<Spawns> sss = new LinkedList<>(this.monsterSpawn);
        resetFully();
        this.monsterSpawn.clear();
        for (Spawns s : sss) {
            MapleMonster newMons = MapleLifeFactory.getMonster(mobid);
            newMons.setF(s.getF());
            newMons.setFh(s.getFh());
            newMons.setPosition(s.getPosition());
            addMonsterSpawn(newMons, mobTime, (byte) -1, null);
        }
        loadMonsterRate(true);
    }

    public final void resetSpawns() {
        boolean changed = false;
        Iterator<Spawns> sss = this.monsterSpawn.iterator();
        while (sss.hasNext()) {
            if (((Spawns) sss.next()).getCarnivalId() > -1) {
                sss.remove();
                changed = true;
            }
        }
        setSpawns(true);
        if (changed) {
            loadMonsterRate(true);
        }
    }

    public final boolean makeCarnivalSpawn(int team, MapleMonster newMons, int num) {
        MapleNodes.MonsterPoint ret = null;
        for (MapleNodes.MonsterPoint mp : getNodez().getMonsterPoints()) {
            if (mp.team == team || mp.team == -1) {
                Point newpos = calcPointBelow(new Point(mp.x, mp.y));
                newpos.y--;
                boolean found = false;
                for (Spawns s : this.monsterSpawn) {
                    if (s.getCarnivalId() > -1 && (mp.team == -1 || s.getCarnivalTeam() == mp.team) && (s.getPosition()).x == newpos.x && (s.getPosition()).y == newpos.y) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ret = mp;
                    break;
                }
            }
        }
        if (ret != null) {
            newMons.setCy(ret.cy);
            newMons.setF(0);
            newMons.setFh(ret.fh);
            newMons.setRx0(ret.x + 50);
            newMons.setRx1(ret.x - 50);
            newMons.setPosition(new Point(ret.x, ret.y));
            newMons.setHide(false);
            SpawnPoint sp = addMonsterSpawn(newMons, 1, (byte) team, null);
            sp.setCarnival(num);
        }
        return (ret != null);
    }

    public final boolean makeCarnivalReactor(int team, int num) {
        MapleReactor old = getReactorByName(team + "" + num);
        if (old != null && old.getState() < 5) {
            return false;
        }
        Point guardz = null;
        List<MapleReactor> react = getAllReactorsThreadsafe();
        for (Pair<Point, Integer> guard : getNodez().getGuardians()) {
            if (((Integer) guard.right).intValue() == team || ((Integer) guard.right).intValue() == -1) {
                boolean found = false;
                for (MapleReactor r : react) {
                    if ((r.getTruePosition()).x == ((Point) guard.left).x && (r.getTruePosition()).y == ((Point) guard.left).y && r.getState() < 5) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    guardz = (Point) guard.left;
                    break;
                }
            }
        }
        if (guardz != null) {
            MapleReactor my = new MapleReactor(MapleReactorFactory.getReactor(9980000 + team), 9980000 + team);
            my.setState((byte) 1);
            my.setName(team + "" + num);
            spawnReactorOnGroundBelow(my, guardz);
        }
        return (guardz != null);
    }

    public final void blockAllPortal() {
        for (MaplePortal p : this.portals.values()) {
            p.setPortalState(false);
        }
    }

    public boolean getAndSwitchTeam() {
        return (getCharactersSize() % 2 != 0);
    }

    public int getChannel() {
        return this.channel;
    }

    public int getConsumeItemCoolTime() {
        return this.consumeItemCoolTime;
    }

    public void setConsumeItemCoolTime(int ciit) {
        this.consumeItemCoolTime = ciit;
    }

    public void setPermanentWeather(int pw) {
        this.permanentWeather = pw;
    }

    public int getPermanentWeather() {
        return this.permanentWeather;
    }

    public void checkStates(String chr) {
        if (!this.checkStates) {
            return;
        }
        EventManager em = getEMByMap();
        int size = getCharactersSize();
        if (em != null && em.getProperty("state") != null && size == 0) {
            em.setProperty("state", "0");
            if (em.getProperty("leader") != null) {
                em.setProperty("leader", "true");
            }
        }
    }

    public void setCheckStates(boolean b) {
        this.checkStates = b;
    }

    public void setNodes(MapleNodes mn) {
        setNodez(mn);
    }

    public final List<MapleNodes.MaplePlatform> getPlatforms() {
        return getNodez().getPlatforms();
    }

    public Collection<MapleNodes.MapleNodeInfo> getNodes() {
        return getNodez().getNodes();
    }

    public MapleNodes.MapleNodeInfo getNode(int index) {
        return getNodez().getNode(index);
    }

    public boolean isLastNode(int index) {
        return getNodez().isLastNode(index);
    }

    public final List<Rectangle> getAreas() {
        return getNodez().getAreas();
    }

    public final Rectangle getArea(int index) {
        return getNodez().getArea(index);
    }

    public final void changeEnvironment(String ms, int type) {
        broadcastMessage(CField.environmentChange(ms, type));
    }

    public final int getNumPlayersInArea(int index) {
        return getNumPlayersInRect(getArea(index));
    }

    public final int getNumPlayersInRect(Rectangle rect) {
        int ret = 0;
        Iterator<MapleCharacter> ltr = this.characters.iterator();
        while (ltr.hasNext()) {
            if (rect.contains(((MapleCharacter) ltr.next()).getTruePosition())) {
                ret++;
            }
        }
        return ret;
    }

    public final int getNumPlayersItemsInArea(int index) {
        return getNumPlayersItemsInRect(getArea(index));
    }

    public final int getNumPlayersItemsInRect(final Rectangle rect) {
        int ret = this.getNumPlayersInRect(rect);
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.ITEM).values()) {
            if (rect.contains(mmo.getTruePosition())) {
                ++ret;
            }
        }
        return ret;
    }

    public void broadcastGMMessage(MapleCharacter source, byte[] packet, boolean repeatToSource) {
        broadcastGMMessage(repeatToSource ? null : source, packet);
    }

    private void broadcastGMMessage(MapleCharacter source, byte[] packet) {
        if (source == null) {
            Iterator<MapleCharacter> itr = this.characters.iterator();
            while (itr.hasNext()) {
                MapleCharacter chr = itr.next();
                if (source == null) {
                    if (chr.isStaff()) {
                        chr.getClient().getSession().writeAndFlush(packet);
                    }
                    continue;
                }
                if (chr != source && chr.getGMLevel() >= source.getGMLevel()) {
                    chr.getClient().getSession().writeAndFlush(packet);
                }
            }
        }
    }

    public final List<Pair<Integer, Integer>> getMobsToSpawn() {
        return getNodez().getMobsToSpawn();
    }

    public final List<Integer> getSkillIds() {
        return getNodez().getSkillIds();
    }

    public final boolean canSpawn(long now) {
        return (this.lastSpawnTime > 0L && this.lastSpawnTime + this.createMobInterval < now);
    }

    public final boolean canHurt(long now) {
        if (this.lastHurtTime > 0L && this.lastHurtTime + this.decHPInterval < now) {
            this.lastHurtTime = now;
            return true;
        }
        return false;
    }

    public final void resetShammos(final MapleClient c) {
        killAllMonsters(true);
        broadcastMessage(CWvsContext.serverNotice(5, "", "A player has moved too far from Shammos. Shammos is going back to the start."));
        Timer.EtcTimer.getInstance().schedule(new Runnable() {
            public void run() {
                if (c.getPlayer() != null) {
                    c.getPlayer().changeMap(MapleMap.this, MapleMap.this.getPortal(0));
                    if (MapleMap.this.getCharactersThreadsafe().size() > 1) {
                        MapScriptMethods.startScript_FirstUser(c, "shammos_Fenter");
                    }
                }
            }
        }, 500L);
    }

    public int getInstanceId() {
        return this.instanceid;
    }

    public void setInstanceId(int ii) {
        this.instanceid = ii;
    }

    public int getPartyBonusRate() {
        return this.partyBonusRate;
    }

    public void setPartyBonusRate(int ii) {
        this.partyBonusRate = ii;
    }

    public short getTop() {
        return this.top;
    }

    public short getBottom() {
        return this.bottom;
    }

    public short getLeft() {
        return this.left;
    }

    public short getRight() {
        return this.right;
    }

    public void setTop(int ii) {
        this.top = (short) ii;
    }

    public void setBottom(int ii) {
        this.bottom = (short) ii;
    }

    public void setLeft(int ii) {
        this.left = (short) ii;
    }

    public void setRight(int ii) {
        this.right = (short) ii;
    }

    public List<Pair<Point, Integer>> getGuardians() {
        return getNodez().getGuardians();
    }

    public MapleNodes.DirectionInfo getDirectionInfo(int i) {
        return getNodez().getDirection(i);
    }

    public Collection<MapleCharacter> getNearestPvpChar(Point attacker, double maxRange, double maxHeight, boolean isLeft, Collection<MapleCharacter> chr) {
        Collection<MapleCharacter> character = new LinkedList<>();
        for (MapleCharacter a : this.characters) {
            if (chr.contains(a.getClient().getPlayer())) {
                Point attackedPlayer = a.getPosition();
                MaplePortal Port = a.getMap().findClosestSpawnpoint(a.getPosition());
                Point nearestPort = Port.getPosition();
                double safeDis = attackedPlayer.distance(nearestPort);
                double distanceX = attacker.distance(attackedPlayer.getX(), attackedPlayer.getY());
                if (isLeft) {
                    if (attacker.x < attackedPlayer.x && distanceX < maxRange && distanceX > 1.0D && attackedPlayer.y >= attacker.y - maxHeight && attackedPlayer.y <= attacker.y + maxHeight) {
                        character.add(a);
                    }
                    continue;
                }
                if (attacker.x > attackedPlayer.x && distanceX < maxRange && distanceX > 1.0D && attackedPlayer.y >= attacker.y - maxHeight && attackedPlayer.y <= attacker.y + maxHeight) {
                    character.add(a);
                }
            }
        }
        return character;
    }

    public void startCatch() {
        if (this.catchstart == null) {
            broadcastMessage(CField.getClock(180));
            this.catchstart = Timer.MapTimer.getInstance().schedule(new Runnable() {
                public void run() {
                    MapleMap.this.broadcastMessage(CWvsContext.serverNotice(1, "", "[술래잡기 알림]\r\n제한시간 2분이 지나 양이 승리하였습니다!\r\n모든 분들은 게임 보상맵으로 이동됩니다."));
                    for (MapleCharacter chr : MapleMap.this.getCharacters()) {
                        chr.getStat().setHp(chr.getStat().getMaxHp(), chr);
                        chr.updateSingleStat(MapleStat.HP, chr.getStat().getMaxHp());
                        if (chr.isCatching) {
                            chr.changeMap(chr.getClient().getChannelServer().getMapFactory().getMap(910040005), chr.getClient().getChannelServer().getMapFactory().getMap(910040005).getPortalSP().get(0));
                            chr.isWolfShipWin = false;
                            continue;
                        }
                        chr.changeMap(chr.getClient().getChannelServer().getMapFactory().getMap(910040004), chr.getClient().getChannelServer().getMapFactory().getMap(910040004).getPortalSP().get(0));
                        chr.isWolfShipWin = true;
                    }
                    MapleMap.this.stopCatch();
                }
            }, 180000L);
        }
    }

    public void stopCatch() {
        if (this.catchstart != null) {
            this.catchstart.cancel(true);
            this.catchstart = null;
        }
    }

    public List<MapleMonster> getAllButterFly() {
        final ArrayList<MapleMonster> ret = new ArrayList<MapleMonster>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.MONSTER).values()) {
            final MapleMonster monster = (MapleMonster) mmo;
            if ((monster.getId() == 8880175 || monster.getId() == 8880178 || monster.getId() == 8880179) && (this.mapid == 450004250 || this.mapid == 450004550)) {
                ret.add(monster);
            } else {
                if ((monster.getId() != 8880165 && monster.getId() != 8880168 && monster.getId() != 8880169) || (this.mapid != 450004150 && this.mapid != 450004450)) {
                    continue;
                }
                ret.add(monster);
            }
        }
        return ret;
    }

    public final void killMonsters(List<MapleMonster> mon) {
        for (MapleMonster monster : mon) {
            if (this.RealSpawns.contains(monster)) {
                this.RealSpawns.remove(monster);
            }
            monster.setHp(0L);
            broadcastMessage(MobPacket.killMonster(monster.getObjectId(), 0));
            removeMapObject(monster);
            monster.killed();
            this.spawnedMonstersOnMap.decrementAndGet();
        }
    }

    public long getMapTimer() {
        return this.timer;
    }

    public void setMapTimer(long timer) {
        this.timer = timer;
    }

    public final void spawnSoul(MapleMapObject dropper, MapleCharacter chr, Item item, Point pos, Item weapon) {
        Point droppos = calcDropPos(pos, pos);
        MapleMapItem drop = new MapleMapItem(item, droppos, dropper, chr, (byte) 0, true);
        broadcastMessage(CField.dropItemFromMapObject(chr.getMap(), drop, dropper.getPosition(), droppos, (byte) 0, false));
        broadcastMessage(CField.removeItemFromMap(drop.getObjectId(), 2, chr.getId(), 0));
        chr.setSoulMP((Equip) weapon);
    }

    public int getBurning() {
        return this.burning;
    }

    public void setBurning(int burning) {
        this.burning = burning;
    }

    public long getBurningIncreasetime() {
        return this.burningIncreasetime;
    }

    public void setBurningIncreasetime(long burningtime) {
        this.burningIncreasetime = burningtime;
    }

    public int getBurningDecreasetime() {
        return this.burningDecreasetime;
    }

    public void setBurningDecreasetime(int burningtime) {
        this.burningDecreasetime = burningtime;
    }

    public List<Rectangle> makeRandomSplitAreas(Point position, Point lt, Point rb, int i, boolean b) {
        List<Rectangle> splitArea = new ArrayList<>();
        byte count;
        for (count = 0; count < i; count = (byte) (count + 1)) {
            splitArea.add(new Rectangle());
        }
        return splitArea;
    }

    public MapleNodes getNodez() {
        return this.nodes;
    }

    public void setNodez(MapleNodes nodes) {
        this.nodes = nodes;
    }

    public void updateEnvironment(List<String> updateLists) {
        for (MapleNodes.Environment ev : getNodez().getEnvironments()) {
            if (updateLists.contains(ev.getName())) {
                ev.setShow(true);
                continue;
            }
            ev.setShow(false);
        }
        broadcastMessage(CField.getUpdateEnvironment(getNodez().getEnvironments()));
    }

    public void resetEnvironment() {
        for (MapleNodes.Environment ev : getNodez().getEnvironments()) {
            ev.setShow(false);
        }
        broadcastMessage(CField.getUpdateEnvironment(getNodez().getEnvironments()));
    }

    public List<MapleMagicWreck> getAllFieldThreadsafe() {
        final ArrayList<MapleMagicWreck> ret = new ArrayList<MapleMagicWreck>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.WRECK).values()) {
            ret.add((MapleMagicWreck) mmo);
        }
        return ret;
    }

    public List<MapleMagicWreck> getWrecks() {
        return this.wrecks;
    }

    public void setWrecks(List<MapleMagicWreck> wrecks) {
        this.wrecks = wrecks;
    }

    public boolean isFirstUserEnter() {
        return this.firstUserEnter;
    }

    public void setFirstUserEnter(boolean firstUserEnter) {
        this.firstUserEnter = firstUserEnter;
    }

    public int getRuneCurse() {
        return this.runeCurse;
    }

    public void setRuneCurse(int runeCurse) {
        this.runeCurse = runeCurse;
    }

    public int getRuneCurseDecrease() {
        switch (this.runeCurse) {
            case 1:
                return 50;
            case 2:
                return 65;
            case 3:
                return 80;
            case 4:
                return 100;
        }
        return 0;
    }

    public boolean isBingoGame() {
        return this.bingoGame;
    }

    public void setBingoGame(boolean bingoGame) {
        this.bingoGame = bingoGame;
    }

    public boolean isEliteField() {
        return this.isEliteField;
    }

    public void setEliteField(boolean isEliteField) {
        this.isEliteField = isEliteField;
    }

    public MapleRune getRune() {
        return this.rune;
    }

    public void setRune(MapleRune rune) {
        this.rune = rune;
    }

    public MapleMonster makePyramidMonster(MapleMonster monster, long hp, int level, int exp) {
        MapleMonster mob = MapleLifeFactory.getMonster(monster.getId());
        OverrideMonsterStats ostats = new OverrideMonsterStats();
        ostats.setOHp(hp);
        ostats.setOMp(mob.getMobMaxMp());
        ostats.setOExp(exp);
        mob.setOverrideStats(ostats);
        mob.setPosition(monster.getTruePosition());
        mob.setFh(monster.getFh());
        mob.getStats().setLevel((short) level);
        return mob;
    }

    public void addmonsterDefense(Map<Integer, List<Integer>> info) {
        this.monsterDefense.putAll(info);
    }

    public Map<Integer, List<Integer>> getmonsterDefense() {
        return this.monsterDefense;
    }

    public ScheduledFuture<?> getEliteBossSchedule() {
        return this.eliteBossSchedule;
    }

    public void setEliteBossSchedule(ScheduledFuture<?> eliteBossSchedule) {
        this.eliteBossSchedule = eliteBossSchedule;
    }

    public int getStigmaDeath() {
        return this.stigmaDeath;
    }

    public void setStigmaDeath(int stigmaDeath) {
        this.stigmaDeath = stigmaDeath;
    }

    public int getCandles() {
        return this.candles;
    }

    public void setCandles(int candles) {
        this.candles = candles;
    }

    public int getLightCandles() {
        return this.lightCandles;
    }

    public void setLightCandles(int lightCandles) {
        this.lightCandles = lightCandles;
    }

    public int getReqTouched() {
        return this.reqTouched;
    }

    public void setReqTouched(int reqTouched) {
        this.reqTouched = reqTouched;
    }

    public long getSandGlassTime() {
        return this.sandGlassTime;
    }

    public void setSandGlassTime(long sandGlassTime) {
        this.sandGlassTime = sandGlassTime;
    }

    public int getLucidCount() {
        return this.lucidCount;
    }

    public void setLucidCount(int lucidCount) {
        this.lucidCount = lucidCount;
    }

    public int getLucidUseCount() {
        return this.lucidUseCount;
    }

    public void setLucidUseCount(int lucidUseCount) {
        this.lucidUseCount = lucidUseCount;
    }

    public boolean isSpawnPoint() {
        boolean 몹젠 = false;
        switch (this.getId()) {
            case 220080100:
            case 220080200:
            case 220080300: {
                return false;
            }
        }
        if (this.mapid == 220080200 || this.mapid == 105200300 || GameConstants.로미오줄리엣(this.mapid)) {
            return false;
        }
        int i = 0;
        for (Spawns spawnPoint : this.monsterSpawn) {
            if (++i != 1) {
                continue;
            }
            몹젠 = true;
            break;
        }
        return 몹젠;
    }

    public final void spawnMagicSword(final MapleMagicSword ms, final MapleCharacter chr, final boolean core) {
        spawnAndAddRangedMapObject(ms, new DelayedPacketCreation() {
            public void sendPackets(MapleClient c) {
                MapleMap.this.broadcastMessage(SkillPacket.CreateSworldObtacle(ms));
            }
        });
        Timer.MapTimer tMan = Timer.MapTimer.getInstance();
        ScheduledFuture<?> schedule = null;
        ms.setSchedule(schedule);
        ms.setSchedule(tMan.schedule(new Runnable() {
            public void run() {
                if (!core) {
                    chr.활성화된소드--;
                    if (chr.활성화된소드 < 0) {
                        chr.활성화된소드 = 0;
                    }
                }
                MapleMap.this.removeMapObject(ms);
            }
        }, (ms.getDuration() - 500)));
    }

    public Integer getCustomValue(int skillid) {
        if (this.customInfo.containsKey(Integer.valueOf(skillid))) {
            return Integer.valueOf((int) ((SkillCustomInfo) this.customInfo.get(Integer.valueOf(skillid))).getValue());
        }
        return null;
    }

    public Integer getCustomTime(int skillid) {
        if (this.customInfo.containsKey(Integer.valueOf(skillid))) {
            return Integer.valueOf((int) (((SkillCustomInfo) this.customInfo.get(Integer.valueOf(skillid))).getEndTime() - System.currentTimeMillis()));
        }
        return null;
    }

    public int getCustomValue0(int skillid) {
        if (this.customInfo.containsKey(Integer.valueOf(skillid))) {
            return (int) ((SkillCustomInfo) this.customInfo.get(Integer.valueOf(skillid))).getValue();
        }
        return 0;
    }

    public void removeCustomInfo(int skillid) {
        this.customInfo.remove(Integer.valueOf(skillid));
    }

    public void setCustomInfo(int skillid, int value, int time) {
        if (getCustomValue(skillid) != null) {
            removeCustomInfo(skillid);
        }
        this.customInfo.put(Integer.valueOf(skillid), new SkillCustomInfo(value, time));
    }

    public Map<Integer, SkillCustomInfo> getCustomValues() {
        return this.customInfo;
    }

    public void makeEliteMonster(int mobid, int grade, Point pos, boolean rune, boolean eliteboss) {
        int rand;
        MapleMonster monster = null;
        for (MapleMonster m : getAllMonster()) {
            if (m != null && !m.isElitemonster() && !m.isEliteboss() && !m.getStats().isBoss()) {
                monster = m;
                break;
            }
        }
        MapleMonster eliteMonster = MapleLifeFactory.getMonster(mobid);
        if (monster == null) {
            monster = MapleLifeFactory.getMonster(mobid);
        }
        eliteMonster.setScale(200);
        int scale = Randomizer.nextInt(3);
        eliteMonster.setEliteGrade(scale);
        eliteMonster.setEliteType(1);
        eliteMonster.getStats().setLevel(monster.getStats().getLevel());
        EliteMonsterGradeInfo eg = null;
        switch (scale) {
            case 0:
                rand = Randomizer.rand(0, EliteMonsterGradeInfo.getFirstGradeInfo().size() - 1);
                eg = EliteMonsterGradeInfo.getFirstGradeInfo().get(Integer.valueOf(rand));
                eliteMonster.setHp(monster.getStats().getHp() * 15L);
                break;
            case 1:
                rand = Randomizer.rand(0, EliteMonsterGradeInfo.getSecondGradeInfo().size() - 1);
                eg = EliteMonsterGradeInfo.getSecondGradeInfo().get(Integer.valueOf(rand));
                eliteMonster.setHp(monster.getStats().getHp() * 20L);
                break;
            case 2:
                rand = Randomizer.rand(0, EliteMonsterGradeInfo.getThirdGradeInfo().size() - 1);
                eg = EliteMonsterGradeInfo.getThirdGradeInfo().get(Integer.valueOf(rand));
                eliteMonster.setHp(monster.getStats().getHp() * 30L);
                break;
        }
        eliteMonster.getEliteGradeInfo().add(new Pair<>(Integer.valueOf(eg.getSkillid()), Integer.valueOf(eg.getSkilllv())));
        eliteMonster.setCustomInfo(9999, 0, Randomizer.rand(3000, 10000));
        if (rune) {
            eliteMonster.setUserunespawn(true);
        } else {
            eliteMonster.setUserunespawn(false);
            setCustomInfo(8222222, 0, 600000);
        }
        eliteMonster.setElitemonster(true);
        eliteMonster.setElitehp(eliteMonster.getHp());
        spawnMonsterOnGroundBelow(eliteMonster, pos);
        broadcastMessage(CField.startMapEffect("어두운 기운과 함께 강력한 몬스터가 출현합니다.", 5120124, true));
        broadcastMessage(CField.specialMapSound("Field.img/eliteMonster/Regen"));
    }

    public int getEliteRequire() {
        return this.eliteRequire;
    }

    public void setEliteRequire(int eliteRequire) {
        this.eliteRequire = eliteRequire;
    }

    public int getEliteCount() {
        return this.eliteCount;
    }

    public void setEliteCount(int eliteCount) {
        this.eliteCount = eliteCount;
    }

    public int getEliteMobCount() {
        return this.EliteMobCount;
    }

    public void setEliteMobCount(int EliteMobCount) {
        this.EliteMobCount = EliteMobCount;
    }

    public int getEliteMobCommonCount() {
        return this.EliteMobCommonCount;
    }

    public void setEliteMobCommonCount(int EliteMobCommonCount) {
        this.EliteMobCommonCount = EliteMobCommonCount;
    }

    public int getElitebossrewardtype() {
        return this.elitebossrewardtype;
    }

    public void setElitebossrewardtype(int elitebossrewardtype) {
        this.elitebossrewardtype = elitebossrewardtype;
    }

    public boolean isElitebossmap() {
        return this.elitebossmap;
    }

    public void setElitebossmap(boolean elitebossmap) {
        this.elitebossmap = elitebossmap;
    }

    public boolean isElitebossrewardmap() {
        return this.elitebossrewardmap;
    }

    public void setElitebossrewardmap(boolean elitebossrewardmap) {
        this.elitebossrewardmap = elitebossrewardmap;
    }

    public int getElitechmpcount() {
        return this.elitechmpcount;
    }

    public void setElitechmpcount(int elitechmpcount) {
        this.elitechmpcount = elitechmpcount;
    }

    public boolean isEliteChmpmap() {
        return this.eliteChmpmap;
    }

    public void setEliteChmpmap(boolean eliteChmpmap) {
        this.eliteChmpmap = eliteChmpmap;
    }

    public int getElitechmptype() {
        return this.elitechmptype;
    }

    public void setElitechmptype(int elitechmptype) {
        this.elitechmptype = elitechmptype;
    }

    public boolean isElitechmpfinal() {
        return this.elitechmpfinal;
    }

    public void setElitechmpfinal(boolean elitechmpfinal) {
        this.elitechmpfinal = elitechmpfinal;
    }

    public int getElitetime() {
        return this.elitetime;
    }

    public void setElitetime(int elitetime) {
        this.elitetime = elitetime;
    }

    public void startEliteBossMap() {
        this.elitetime = 1800;
        broadcastMessage(CField.getClock(1800));
    }

    public void stopEliteBossMap() {
        this.elitetime = 0;
        killAllMonsters(true);
        setElitebossmap(false);
        broadcastMessage(CField.stopClock());
        broadcastMessage(CField.specialMapEffect(0, false, "", "", "", "", ""));
    }

    public void 미믹보상() {
        setElitebossrewardtype(1);
        broadcastMessage(CField.enforceMSG("왜인지 일반 공격으로 맞으면 아이템을 떨어트릴 것 같은 기분이 드는걸. 아이템은 직접 주워야 해.", 145, 7000));
        int i = 0;
        for (Spawns spawnPoint : this.monsterSpawn) {
            MapleMonster monster = MapleLifeFactory.getMonster(8220027);
            monster.getStats().setFirstAttack(false);
            spawnMonsterOnGroundBelow(monster, spawnPoint.getPosition());
            i++;
            if (i == 5) {
                break;
            }
        }
    }

    public void 공중미믹보상() {
        setElitebossrewardtype(2);
        broadcastMessage(CField.enforceMSG("착한 모험가들에게 선물을 주지! 내가 던지는 아이템을 잘 받아 봐!", 146, 7000));
        int i = 0;
        for (Spawns spawnPoint : this.monsterSpawn) {
            MapleMonster monster = MapleLifeFactory.getMonster(8220028);
            monster.getStats().setFirstAttack(false);
            spawnMonsterOnGroundBelow(monster, spawnPoint.getPosition());
            i++;
            if (i == 4) {
                break;
            }
        }
    }

    public void 하늘보상() {
        setElitebossrewardtype(3);
        broadcastMessage(CField.enforceMSG("하늘에서 갑자기 아이템이 떨어지고 있어요! 닿으면 얻을 수 있을것 같아요!", 162, 7000));
    }

    public void 엘보보상맵캔슬() {
        final MapleMap map = this;
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public final void run() {
                List<MapleMapItem> items = MapleMap.this.getAllItemsThreadsafe();
                for (MapleMapItem i : items) {
                    if (i.getItemId() >= 2432391 && i.getItemId() <= 2432398) {
                        i.expire(map);
                    }
                }
                MapleMap.this.killAllMonsters(true);
                MapleMap.this.setElitebossmap(false);
                MapleMap.this.setElitebossrewardmap(false);
                MapleMap.this.setEliteMobCommonCount(0);
                MapleMap.this.setEliteMobCount(0);
                MapleMap.this.setElitebossrewardtype(0);
                MapleMap.this.broadcastMessage(CField.showEffect("Map/Effect2.img/event/gameover"));
                MapleMap.this.broadcastMessage(SLFCGPacket.playSE("MiniGame.img/multiBingo/gameover"));
                MapleMap.this.broadcastMessage(CField.stopClock());
                MapleMap.this.broadcastMessage(CField.specialMapEffect(0, false, "", "", "", "", ""));
            }
        }, 30000L);
    }

    public void startEliteBossReward() {
        this.elitetime = 0;
        broadcastMessage(CField.specialMapEffect(3, false, "Bgm36.img/HappyTimeShort", "Map/Map/Map9/924050000.img/back", "Effect/EliteMobEff.img/eliteBonusStage", "", ""));
        broadcastMessage(SLFCGPacket.playSE("MiniGame.img/multiBingo/3"));
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                MapleMap.this.broadcastMessage(SLFCGPacket.playSE("MiniGame.img/multiBingo/2"));
            }
        }, 3000L);
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                MapleMap.this.broadcastMessage(SLFCGPacket.playSE("MiniGame.img/multiBingo/1"));
            }
        }, 5000L);
        Timer.MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                int rand = Randomizer.rand(1, 3);
                if (rand == 1) {
                    MapleMap.this.미믹보상();
                } else if (rand == 2) {
                    MapleMap.this.공중미믹보상();
                } else if (rand == 3) {
                    MapleMap.this.하늘보상();
                }
                MapleMap.this.setCustomInfo(210403, 0, 30000);
                MapleMap.this.broadcastMessage(SLFCGPacket.milliTimer(30000));
                MapleMap.this.엘보보상맵캔슬();
                MapleMap.this.broadcastMessage(SLFCGPacket.playSE("MiniGame.img/multiBingo/start"));
            }
        }, 6000L);
    }

    public int getPapulratusTime() {
        return this.PapulratusTime;
    }

    public void setPapulratusTime(int PapulratusTime) {
        this.PapulratusTime = PapulratusTime;
    }

    public int getPapulratusPatan() {
        return this.PapulratusPatan;
    }

    public void setPapulratusPatan(int PapulratusPatan) {
        this.PapulratusPatan = PapulratusPatan;
    }

    public int getPapullatushour() {
        return this.Papullatushour;
    }

    public void setPapullatushour(int Papullatushour) {
        this.Papullatushour = Papullatushour;
    }

    public int getPapullatusminute() {
        return this.Papullatusminute;
    }

    public void setPapullatusminute(int Papullatusminute) {
        this.Papullatusminute = Papullatusminute;
    }

    public ScheduledFuture<?> getSchedule() {
        return this.schedule;
    }

    public void setSchedule(ScheduledFuture<?> schedule) {
        this.schedule = schedule;
    }

    public void RewardCheck(int monsterid, int monsterid2, int spawnmonsterid, Point pos) {
        int size = 0;
        for (MapleMonster m : getAllMonster()) {
            if (m.getId() >= monsterid && m.getId() <= monsterid2 && m.getSeperateSoul() <= 0) {
                size++;
            }
        }
        if (size == 0) {
            spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(spawnmonsterid), pos);
        }
    }

    public final void CreateObstacle(MapleMonster monster, List<Obstacle> obs) {
        for (Obstacle ob : obs) {
            addMapObject(ob);
        }
        broadcastMessage(MobPacket.createObstacle(monster, obs, (byte) 0));
    }

    public final void CreateObstacle2(MapleMonster monster, Obstacle ob, byte type) {
        addMapObject(ob);
        broadcastMessage(MobPacket.createObstacle2(monster, ob, type));
    }

    public List<Obstacle> getAllObstacle() {
        final ArrayList<Obstacle> ret = new ArrayList<Obstacle>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.OBSTACLE).values()) {
            ret.add((Obstacle) mmo);
        }
        return ret;
    }

    public List<SpiderWeb> getAllSpiderThreadsafe() {
        final ArrayList<SpiderWeb> ret = new ArrayList<SpiderWeb>();
        for (final MapleMapObject mmo : this.mapobjects.get(MapleMapObjectType.WEB).values()) {
            ret.add((SpiderWeb) mmo);
        }
        return ret;
    }

    public final List<SpiderWeb> getAllSpiderWeb() {
        return getWebInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.WEB}));
    }

    public final List<SpiderWeb> getWebInRange(Point from, double rangeSq, List<MapleMapObjectType> MapObject_types) {
        List<MapleMapObject> mapobject = getMapObjectsInRange(from, rangeSq);
        List<SpiderWeb> webs = new ArrayList<>();
        for (int i = 0; i < mapobject.size(); i++) {
            if (((MapleMapObject) mapobject.get(i)).getType() == MapleMapObjectType.WEB) {
                webs.add((SpiderWeb) mapobject.get(i));
            }
        }
        return webs;
    }

    public List<Point> getWillPoison() {
        return this.willpoison;
    }

    public void addWillPoison(Point pos) {
        this.willpoison.add(pos);
    }

    public void removeWillPosion(Point pos) {
        this.willpoison.remove(pos);
    }

    public void resetWillPosion() {
        this.willpoison.clear();
    }

    public void getDuskObtacles(MapleMonster monster, int rand) {
        List<Obstacle> obs = new ArrayList<>();
        Obstacle ob = null;
        if (rand == 0) {
            ob = new Obstacle(65, new Point(291, -1055), new Point(291, -157), 36, 15, 0, 164, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(412, -1055), new Point(412, -157), 36, 15, 0, 478, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-226, -1055), new Point(-226, -157), 36, 15, 0, 897, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(574, -1055), new Point(574, -157), 36, 15, 0, 1476, 400, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 1) {
            ob = new Obstacle(67, new Point(373, -1055), new Point(373, -157), 36, 15, 0, 294, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-139, -1055), new Point(-139, -157), 36, 15, 0, 866, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(554, -1055), new Point(554, -157), 36, 15, 0, 846, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-370, -1055), new Point(-370, -157), 36, 15, 0, 916, 400, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 2) {
            ob = new Obstacle(66, new Point(-187, -1055), new Point(-187, -157), 36, 15, 0, 151, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-569, -1055), new Point(-569, -157), 36, 15, 0, 1047, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-472, -1055), new Point(-472, -157), 36, 15, 0, 1333, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-206, -1055), new Point(-206, -157), 36, 15, 0, 1124, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-123, -1055), new Point(-123, -157), 36, 15, 0, 689, 401, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 3) {
            ob = new Obstacle(67, new Point(-545, -1055), new Point(-545, -157), 36, 15, 0, 352, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(563, -1055), new Point(563, -157), 36, 15, 0, 1407, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-151, -1055), new Point(-151, -157), 36, 15, 0, 407, 600, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 4) {
            ob = new Obstacle(65, new Point(-151, -1055), new Point(-151, -157), 36, 15, 0, 1133, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-69, -1055), new Point(-69, -157), 36, 15, 0, 641, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-7, -1055), new Point(-7, -157), 36, 15, 0, 1442, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(315, -1055), new Point(315, -157), 36, 15, 0, 1280, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(137, -1055), new Point(137, -157), 36, 15, 0, 1398, 601, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 5) {
            ob = new Obstacle(66, new Point(580, -1055), new Point(580, -157), 36, 15, 0, 787, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(230, -1055), new Point(230, -157), 36, 15, 0, 209, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-405, -1055), new Point(-405, -157), 36, 15, 0, 448, 801, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 6) {
            ob = new Obstacle(66, new Point(-318, -1055), new Point(-318, -157), 36, 15, 0, 662, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-438, -1055), new Point(-438, -157), 36, 15, 0, 1351, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-614, -1055), new Point(-614, -157), 36, 15, 0, 437, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(41, -1055), new Point(41, -157), 36, 15, 0, 794, 600, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 7) {
            ob = new Obstacle(66, new Point(-96, -1055), new Point(-96, -157), 36, 15, 0, 692, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-58, -1055), new Point(-58, -157), 36, 15, 0, 798, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(33, -1055), new Point(33, -157), 36, 15, 0, 330, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-37, -1055), new Point(-37, -157), 36, 15, 0, 1028, 800, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 8) {
            ob = new Obstacle(67, new Point(-358, -1055), new Point(-358, -157), 36, 15, 0, 323, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-395, -1055), new Point(-395, -157), 36, 15, 0, 263, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-391, -1055), new Point(-391, -157), 36, 15, 0, 908, 801, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 9) {
            ob = new Obstacle(65, new Point(-176, -1055), new Point(-176, -157), 36, 15, 0, 638, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(199, -1055), new Point(199, -157), 36, 15, 0, 603, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-128, -1055), new Point(-128, -157), 36, 15, 0, 577, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(35, -1055), new Point(35, -157), 36, 15, 0, 841, 600, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 10) {
            ob = new Obstacle(67, new Point(-25, -1055), new Point(-25, -157), 36, 15, 0, 156, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-416, -1055), new Point(-416, -157), 36, 15, 0, 1284, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(147, -1055), new Point(147, -157), 36, 15, 0, 261, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-273, -1055), new Point(-273, -157), 36, 15, 0, 1092, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(612, -1055), new Point(612, -157), 36, 15, 0, 323, 401, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 11) {
            ob = new Obstacle(65, new Point(-374, -1055), new Point(-374, -157), 36, 15, 0, 999, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-157, -1055), new Point(-157, -157), 36, 15, 0, 245, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(45, -1055), new Point(45, -157), 36, 15, 0, 283, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(376, -1055), new Point(376, -157), 36, 15, 0, 623, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(431, -1055), new Point(431, -157), 36, 15, 0, 1298, 401, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 12) {
            ob = new Obstacle(66, new Point(-41, -1055), new Point(-41, -157), 36, 15, 0, 1397, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-194, -1055), new Point(-194, -157), 36, 15, 0, 304, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-114, -1055), new Point(-114, -157), 36, 15, 0, 1057, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-495, -1055), new Point(-495, -157), 36, 15, 0, 1165, 800, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 13) {
            ob = new Obstacle(67, new Point(136, -1055), new Point(136, -157), 36, 15, 0, 795, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-162, -1055), new Point(-162, -157), 36, 15, 0, 999, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(578, -1055), new Point(578, -157), 36, 15, 0, 613, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(79, -1055), new Point(79, -157), 36, 15, 0, 474, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-614, -1055), new Point(-614, -157), 36, 15, 0, 215, 800, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 14) {
            ob = new Obstacle(65, new Point(-192, -1055), new Point(-192, -157), 36, 15, 0, 1015, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(548, -1055), new Point(548, -157), 36, 15, 0, 1160, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-180, -1055), new Point(-180, -157), 36, 15, 0, 528, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-614, -1055), new Point(-614, -157), 36, 15, 0, 1009, 801, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 15) {
            ob = new Obstacle(66, new Point(-243, -1055), new Point(-243, -157), 36, 15, 0, 740, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(372, -1055), new Point(372, -157), 36, 15, 0, 1289, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(14, -1055), new Point(14, -157), 36, 15, 0, 1386, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(612, -1055), new Point(612, -157), 36, 15, 0, 297, 801, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 16) {
            ob = new Obstacle(67, new Point(199, -1055), new Point(199, -157), 36, 15, 0, 873, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(248, -1055), new Point(248, -157), 36, 15, 0, 683, 800, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 17) {
            ob = new Obstacle(65, new Point(-411, -1055), new Point(-411, -157), 36, 15, 0, 733, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(432, -1055), new Point(432, -157), 36, 15, 0, 284, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-614, -1055), new Point(-614, -157), 36, 15, 0, 896, 601, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 18) {
            ob = new Obstacle(66, new Point(-211, -1055), new Point(-211, -157), 36, 15, 0, 1292, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(237, -1055), new Point(237, -157), 36, 15, 0, 606, 600, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-310, -1055), new Point(-310, -157), 36, 15, 0, 1002, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(338, -1055), new Point(338, -157), 36, 15, 0, 1087, 601, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 19) {
            ob = new Obstacle(65, new Point(-229, -1055), new Point(-229, -157), 36, 15, 0, 763, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-165, -1055), new Point(-165, -157), 36, 15, 0, 136, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-408, -1055), new Point(-408, -157), 36, 15, 0, 370, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-411, -1055), new Point(-411, -157), 36, 15, 0, 401, 600, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 20) {
            ob = new Obstacle(66, new Point(-246, -1055), new Point(-246, -157), 36, 15, 0, 528, 601, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(607, -1055), new Point(607, -157), 36, 15, 0, 1018, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-114, -1055), new Point(-114, -157), 36, 15, 0, 575, 801, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(65, new Point(-133, -1055), new Point(-133, -157), 36, 15, 0, 485, 800, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(-63, -1055), new Point(-63, -157), 36, 15, 0, 685, 600, 1, 898, 0);
            obs.add(ob);
        } else if (rand == 21) {
            ob = new Obstacle(67, new Point(570, -1055), new Point(570, -157), 36, 15, 0, 697, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(-256, -1055), new Point(-256, -157), 36, 15, 0, 1078, 401, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(67, new Point(361, -1055), new Point(361, -157), 36, 15, 0, 1285, 400, 1, 898, 0);
            obs.add(ob);
            ob = new Obstacle(66, new Point(612, -1055), new Point(612, -157), 36, 15, 0, 796, 601, 1, 898, 0);
            obs.add(ob);
        }
        if (!obs.isEmpty()) {
            CreateObstacle(monster, obs);
        }
    }

    public int getMobsSize(int mobid) {
        int number = 0;
        for (MapleMapObject obj : getAllMonstersThreadsafe()) {
            MapleMonster mob = (MapleMonster) obj;
            if (mob.getId() == mobid) {
                number++;
            }
        }
        return number;
    }

    public void getObtacles(int rand) {
        List<Obstacle> obs = new ArrayList<>();
        if (getId() == 450013300) {
            if (rand == 0) {
                Obstacle ob = new Obstacle(77, new Point(-987, -600), new Point(-983, -239), 95, 50, 0, 487, 1000, 123, 7, 360);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(-731, -600), new Point(-973, 88), 95, 50, 0, 923, 1000, 110, 5, 728);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(-296, -600), new Point(-961, 88), 95, 50, 0, 1388, 1000, 141, 7, 973);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(119, -600), new Point(-569, 88), 95, 50, 0, 1797, 1000, 132, 5, 973);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(486, -600), new Point(-202, 88), 95, 50, 0, 2277, 1000, 130, 5, 973);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(849, -600), new Point(161, 88), 95, 50, 0, 2770, 1000, 137, 6, 973);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
            } else {
                Obstacle ob = new Obstacle(77, new Point(323, -600), new Point(-365, 88), 95, 50, 0, 415, 1000, 85, 5, 973);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(-48, -600), new Point(-736, 88), 95, 50, 0, 878, 1000, 130, 5, 973);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(-481, -600), new Point(-966, 88), 95, 50, 0, 1302, 1000, 148, 6, 840);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
                ob = new Obstacle(77, new Point(-879, -600), new Point(-977, 88), 95, 50, 0, 1737, 1000, 112, 5, 694);
                obs.clear();
                obs.add(ob);
                broadcastMessage(MobPacket.CreateObstacle2(obs));
            }
        } else if (rand == 0) {
            Obstacle ob = new Obstacle(77, new Point(-853, -600), new Point(-976, 85), 95, 50, 0, 498, 1000, 129, 6, 695);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(-407, -600), new Point(-964, 85), 95, 50, 0, 977, 1000, 102, 5, 883);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(-102, -600), new Point(-787, 85), 95, 50, 0, 1397, 1000, 90, 6, 969);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(178, -600), new Point(-507, 85), 95, 50, 0, 1802, 1000, 111, 5, 969);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(569, -600), new Point(-116, 85), 95, 50, 0, 2299, 1000, 141, 6, 969);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(897, -600), new Point(482, -185), 95, 50, 0, 2714, 1000, 107, 7, 587);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
        } else {
            Obstacle ob = new Obstacle(77, new Point(-938, -600), new Point(-979, 85), 95, 50, 0, 425, 1000, 90, 6, 685);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(-594, -600), new Point(-969, 85), 95, 50, 0, 844, 1000, 130, 6, 779);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(-179, -600), new Point(-864, 85), 95, 50, 0, 1254, 1000, 101, 6, 969);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(245, -600), new Point(-440, 85), 95, 50, 0, 1669, 1000, 131, 7, 969);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(604, -600), new Point(94, -90), 95, 50, 0, 2128, 1000, 94, 6, 721);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
            ob = new Obstacle(77, new Point(908, -600), new Point(223, 85), 95, 50, 0, 2574, 1000, 144, 7, 969);
            obs.clear();
            obs.add(ob);
            broadcastMessage(MobPacket.CreateObstacle2(obs));
        }
    }

    public void BloomingMoment(boolean active, boolean justshow) {
        boolean sun = ((new Date()).getHours() % 2 == 0);
        if ((((new Date()).getMinutes() >= 30 && (new Date()).getMinutes() < 40) || justshow) && (getCustomValue(993192000) == null || justshow)) {
            int buffid = sun ? 2024011 : 2024012;
            if (!justshow) {
                setCustomInfo(993192000, 0, 600000);
            }
            if (getCharactersSize() > 0) {
                for (MapleCharacter chr : getAllChracater()) {
                    if (chr != null && !chr.getBuffedValue(buffid)) {
                        MapleItemInformationProvider.getInstance().getItemEffect(buffid).applyTo(chr, true);
                    }
                }
                if (!sun) {
                    broadcastMessage(CField.setSpecialMapEffect("bloomingSun", 1, 1));
                    broadcastMessage(CField.setSpecialMapEffect("bloomingWind", 1, 1));
                }
                List<Pair<String, Integer>> eff = new ArrayList<>();
                eff.add(new Pair<>("bloomingSun", Integer.valueOf(!active ? 0 : (sun ? 1 : 0))));
                eff.add(new Pair<>("bloomingWind", Integer.valueOf(!active ? 0 : (sun ? 0 : 1))));
                broadcastMessage(CField.ChangeSpecialMapEffect(eff));
                if (active) {
                    broadcastMessage(CField.startMapEffect(sun ? "따사로운 봄 햇살이 쏟아져 내립니다." : "기분 좋게 시원한 봄바람이 살랑입니다.", sun ? 5121112 : 5121113, true));
                }
            }
        }
    }

    public void LiveShow(boolean active, boolean justshow) {
        boolean sun = ((new Date()).getHours() % 2 == 0);
        if ((((new Date()).getMinutes() >= 30 && (new Date()).getMinutes() < 40) || justshow) && (getCustomValue(993194000) == null || justshow)) {
            int buffid = sun ? 2024017 : 2024018;
            if (!justshow) {
                setCustomInfo(993194000, 0, 600000);
            }
            if (getCharactersSize() > 0) {
                for (MapleCharacter chr : getAllChracater()) {
                    if (chr != null && !chr.getBuffedValue(buffid)) {
                        MapleItemInformationProvider.getInstance().getItemEffect(buffid).applyTo(chr, true);
                    }
                }
                if (!sun) {
                    broadcastMessage(CField.setSpecialMapEffect("studioBlue", 1, 1));
                    broadcastMessage(CField.setSpecialMapEffect("studioPink", 1, 1));
                }
                List<Pair<String, Integer>> eff = new ArrayList<>();
                eff.add(new Pair<>("studioBlue", Integer.valueOf(!active ? 0 : (sun ? 1 : 0))));
                eff.add(new Pair<>("studioPink", Integer.valueOf(!active ? 0 : (sun ? 0 : 1))));
                broadcastMessage(CField.ChangeSpecialMapEffect(eff));
                if (active) {
                    broadcastMessage(CField.startMapEffect(sun ? "시청자로부터 블루 하트 선물이 쏟아집니다!" : "시청자로부터 핑크 하트 선물이 쏟아집니다!", sun ? 5121114 : 5121115, true));
                }
            }
        }
    }

    public int getBattleGroundTimer() {
        return this.BattleGroundTimer;
    }

    public void setBattleGroundTimer(int BattleGroundTimer) {
        this.BattleGroundTimer = BattleGroundTimer;
    }

    public int getBattleGroundMainTimer() {
        return this.BattleGroundMainTimer;
    }

    public void setBattleGroundMainTimer(int BattleGroundMainTimer) {
        this.BattleGroundMainTimer = BattleGroundMainTimer;
    }

    public List<Point> getLucidDream() {
        return this.LucidDream;
    }

    public void setLucidDream(List<Point> LucidDream) {
        this.LucidDream = LucidDream;
    }

    public int getBarrierArc() {
        return this.barrierArc;
    }

    public void setBarrierArc(int barrierArc) {
        this.barrierArc = barrierArc;
    }

    public int getBarrierAut() {
        return this.barrierAut;
    }

    public void setBarrierAut(int barrierAut) {
        this.barrierAut = barrierAut;
    }

    public List<MapleMonster> getRealSpawns() {
        return this.RealSpawns;
    }

    public void setRealSpawns(List<MapleMonster> RealSpawns) {
        this.RealSpawns = RealSpawns;
    }

    private static interface DelayedPacketCreation {

        void sendPackets(MapleClient param1MapleClient);
    }
}
