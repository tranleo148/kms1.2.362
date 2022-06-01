package server.life;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleDiseases;
import client.MapleTrait;
import client.SecondaryStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import scripting.EventInstanceManager;
import server.MapleItemInformationProvider;
import server.Obstacle;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.SkillCustomInfo;
import server.Timer;
import server.field.boss.FieldSkillFactory;
import server.field.boss.lotus.MapleEnergySphere;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleNodes;
import server.polofritto.MapleRandomPortal;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SLFCGPacket;

public class MapleMonster extends AbstractLoadedMapleLife {//옹우ㅠㅇ 

    private MapleMonsterStats stats;

    private ChangeableStats ostats = null;

    private long hp;

    private long nextKill = 0L;

    private long lastDropTime = 0L;

    private long barrier = 0L;

    private int mp;

    private byte carnivalTeam = -1;

    private byte phase = 0;

    private byte bigbangCount = 0;

    private MapleMap map;

    private WeakReference<MapleMonster> sponge = new WeakReference<>(null);

    private int linkoid = 0;

    private int lastNode = -1;

    private int highestDamageChar = 0;

    private int linkCID = 0;

    private WeakReference<MapleCharacter> controller = new WeakReference<>(null);

    private boolean fake = false;

    private boolean dropsDisabled = false;

    private boolean controllerHasAggro = false;

    private boolean demianChangePhase = false;

    private boolean extreme = false;

    private final List<AttackerEntry> attackers = new CopyOnWriteArrayList<>();

    private EventInstanceManager eventInstance;

    private MonsterListener listener = null;

    private byte[] reflectpack = null;

    private byte[] nodepack = null;

    private List<Pair<MonsterStatus, MonsterStatusEffect>> stati = new CopyOnWriteArrayList<>();

    private final LinkedList<MonsterStatusEffect> poisons = new LinkedList<>();

    private Map<MobSkill, Long> usedSkills = new HashMap<>();

    private int stolen = -1;

    private int seperateSoul = 0;

    private int airFrame = 0;

    private boolean shouldDropItem = false;

    private boolean killed = false;

    private boolean isseperated = false;

    private boolean isMobGroup = false;

    private boolean isSkillForbid = false;

    private boolean useSpecialSkill = false;

    private long lastReceivedMovePacket = System.currentTimeMillis();

    private long spawnTime = 0L, shield = 0L, shieldmax = 0L;

    private long lastBindTime = 0L;

    private long lastCriticalBindTime = 0L;

    private long elitehp = 0L;

    public int blizzardTempest = 0;

    private long lastSpecialAttackTime = System.currentTimeMillis();

    private long lastSeedCountedTime = System.currentTimeMillis();

    public long lastDistotionTime = System.currentTimeMillis();

    public long lastCapTime = 0L;

    public long astObstacleTime = System.currentTimeMillis();

    public long lastLaserTime = System.currentTimeMillis();

    public long lastObstacleTime = System.currentTimeMillis();

    public long lastRedObstacleTime = System.currentTimeMillis();

    public long lastChainTime = System.currentTimeMillis();

    public long lastSpearTime = System.currentTimeMillis();

    public long lastThunderTime = System.currentTimeMillis();

    public long lastEyeTime = System.currentTimeMillis();

    public long lastBWThunder = System.currentTimeMillis();

    public long lastBWBliThunder = System.currentTimeMillis();

    private int nextSkill = 0, nextSkillLvl = 0, freezingOverlap = 0, curseBound = 0;

    private int patten;

    private int owner = -1;

    private int scale = 100;

    private int eliteGrade = -1;

    private int eliteType = 0;

    private int spiritGate = 0;

    private int anotherByte = 0;

    private List<Integer> spawnList = new ArrayList<>();

    private List<Integer> willHplist = new ArrayList<>();

    private List<Ignition> ignitions = new CopyOnWriteArrayList<>();

    private List<MapleEnergySphere> spheres = new ArrayList<>();

    private List<Pair<Integer, Integer>> eliteGradeInfo = new ArrayList<>();

    private Map<Integer, Rectangle> rectangles = new LinkedHashMap<>();

    private ScheduledFuture<?> schedule = null;

    private transient Map<Integer, SkillCustomInfo> customInfo = new LinkedHashMap<>();

    private List<MonsterStatusEffect> indielist = new ArrayList<>();

    private boolean elitemonster;

    private boolean elitechmp;

    private boolean eliteboss;

    private boolean userunespawn;

    private String specialtxt;

    private int energycount;

    private int energyspeed;

    private int StigmaType;

    private int TotalStigma;

    private int SerenTimetype;

    private int SerenNoonTotalTime;

    private int SerenSunSetTotalTime;

    private int SerenMidNightSetTotalTime;

    private int SerenDawnSetTotalTime;

    private int SerenNoonNowTime;

    private int SerenSunSetNowTime;

    private int SerenMidNightSetNowTime;

    private int SerenDawnSetNowTime;

    private boolean energyleft;

    private boolean willSpecialPattern = false;

    public MapleMonster(int id, MapleMonsterStats stats) {
        super(id);
        initWithStats(stats);
    }

    public MapleMonster(int id, MapleMonsterStats stats, boolean extreme) {
        super(id);
        initWithStats(stats, extreme);
    }

    public MapleMonster(MapleMonster monster) {
        super(monster);
        initWithStats(monster.stats);
    }

    public double bonusHp() {
        int level = this.stats.getLevel();
        double bonus = 1.0D;
        if (level >= 200 && level <= 210) {
            bonus = 1.5D;
        } else if (level >= 211 && level <= 220) {
            bonus = 2.0D;
        } else if (level >= 221 && level <= 230) {
            bonus = 2.5D;
        } else if (level >= 231 && level <= 240) {
            bonus = 3.0D;
        } else if (level >= 241) {
            bonus = 3.5D;
        }
        if (this.stats.getId() >= 9833070 && this.stats.getId() <= 9833099) {
            bonus = 1.0D;
        }
        if (this.stats.isBoss()) {
            switch (this.stats.getId()) {
                case 8644650:
                case 8645009:
                    bonus *= 15.0D;
                    break;
                case 8880405:
                case 8880408:
                case 8880409:
                    bonus *= 10.0D;
                    break;
                default:
                    bonus *= 2.0D;
                    break;
            }
        }
        if (this.extreme) {
            switch (this.stats.getId()) {
                case 8880100:
                case 8880101:
                case 8950000:
                case 8950001:
                case 8950002:
                    bonus *= 6.0D;
                    break;
                case 8880141:
                case 8880151:
                case 8880153:
                case 8880300:
                case 8880301:
                case 8880302:
                case 8880303:
                case 8880304:
                    bonus *= 4.5D;
                    break;
            }
        }
        return 1.0D;
    }

    private final void initWithStats(MapleMonsterStats stats) {
        setStance(5);
        this.stats = stats;
        this.hp = (long) (stats.getHp() * bonusHp());
        this.mp = stats.getMp();
    }

    private final void initWithStats(MapleMonsterStats stats, boolean extreme) {
        setStance(5);
        this.stats = stats;
        this.extreme = extreme;
        this.hp = (long) (stats.getHp() * bonusHp());
        this.mp = stats.getMp();
    }

    public final List<AttackerEntry> getAttackers() {
        if (this.attackers == null || this.attackers.size() <= 0) {
            return new ArrayList<>();
        }
        List<AttackerEntry> ret = new ArrayList<>();
        for (AttackerEntry e : this.attackers) {
            if (e != null) {
                ret.add(e);
            }
        }
        return ret;
    }

    public long getLastReceivedMovePacket() {
        return this.lastReceivedMovePacket;
    }

    public void receiveMovePacket() {
        this.lastReceivedMovePacket = System.currentTimeMillis();
    }

    public final MapleMonsterStats getStats() {
        return this.stats;
    }

    public final void disableDrops() {
        this.dropsDisabled = true;
    }

    public final boolean dropsDisabled() {
        return this.dropsDisabled;
    }

    public final void setSponge(MapleMonster mob) {
        this.sponge = new WeakReference<>(mob);
        if (this.linkoid <= 0) {
            this.linkoid = mob.getObjectId();
        }
    }

    public final void setMap(MapleMap map) {
        this.map = map;
        startDropItemSchedule();
    }

    public int getOwner() {
        return this.owner;
    }

    public void setOwner(int id) {
        this.owner = id;
    }

    public final long getHp() {
        return this.hp;
    }

    public final void setHp(long hp) {
        this.hp = hp;
    }

    public final void addHp(long hp, boolean brodcast) {
        this.hp = getHp() + hp;
        if (this.hp > getStats().getHp()) {
            this.hp = getStats().getHp();
        }
        if (brodcast) {
            getMap().broadcastMessage(MobPacket.showBossHP(this));
        }
        if (this.hp <= 0L) {
            this.map.killMonster(this, this.controller.get(), true, false, (byte) 1, 0);
        }
    }

    public final ChangeableStats getChangedStats() {
        return this.ostats;
    }

    public final long getMobMaxHp() {
        return this.stats.getHp();
    }

    public final int getMp() {
        return this.mp;
    }

    public final void setMp(int mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public final int getMobMaxMp() {
        if (this.ostats != null) {
            return this.ostats.mp;
        }
        return this.stats.getMp();
    }

    public final long getMobExp() {
        if (this.ostats != null) {
            return this.ostats.exp;
        }
        return this.stats.getExp();
    }

    public final void setOverrideStats(OverrideMonsterStats ostats) {
        this.ostats = new ChangeableStats(this.stats, ostats);
        this.hp = ostats.getHp();
        this.mp = ostats.getMp();
    }

    public final void changeLevel(int newLevel) {
        changeLevel(newLevel, true);
    }

    public final void changeLevel(int newLevel, boolean pqMob) {
        if (!this.stats.isChangeable()) {
            return;
        }
        this.ostats = new ChangeableStats(this.stats, newLevel, pqMob);
        this.hp = this.ostats.getHp();
        this.mp = this.ostats.getMp();
    }

    public final MapleMonster getSponge() {
        return this.sponge.get();
    }

    public final void damage(MapleCharacter from, long damage, boolean updateAttackTime) {
        damage(from, damage, updateAttackTime, 0);
    }

    public final void damage(MapleCharacter from, long damage, boolean updateAttackTime, int lastSkill) {
        if (from == null || damage <= 0L || !isAlive()) {
            return;
        }
        if (getId() == 8880153) {
            if (getCustomValue(8881154) != null) {
                setHp(getStats().getHp() * 10L / 100L);
                this.map.broadcastMessage(MobPacket.showBossHP(this));
                return;
            }
        } else if (getId() == 8880303 || getId() == 8880304 || getId() == 8880343 || getId() == 8880344) {
            MapleMonster will = this.map.getMonsterById(8880300);
            if (will == null) {
                will = this.map.getMonsterById(8880340);
            }
            long nowhp = this.hp - damage;
            double hppercent = nowhp * 100.0D / getStats().getHp();
            if (hppercent <= 66.6D && will.getWillHplist().contains(Integer.valueOf(666))) {
                setHp((long) (getMobMaxHp() * 66.6D / 100.0D));
                this.map.broadcastMessage(MobPacket.showBossHP(this));
                return;
            }
            if (hppercent <= 33.3D && will.getWillHplist().contains(Integer.valueOf(333))) {
                setHp((long) (getMobMaxHp() * bonusHp() * 33.3D / 100.0D));
                this.map.broadcastMessage(MobPacket.showBossHP(this));
                return;
            }
            if (hppercent <= 0.3D && will.getWillHplist().contains(Integer.valueOf(3))) {
                setHp((long) (getMobMaxHp() * 0.3D / 100.0D));
                this.map.broadcastMessage(MobPacket.showBossHP(this));
                return;
            }
        } else if (getId() == 8880301 || getId() == 8880341) {
            long nowhp = this.hp - damage;
            double hppercent = nowhp * 100.0D / getStats().getHp();
            if (hppercent <= 50.0D && getWillHplist().contains(Integer.valueOf(500))) {
                setHp(getMobMaxHp() * 50L / 100L);
                this.map.broadcastMessage(MobPacket.showBossHP(this));
                if (getCustomValue(2420701) == null
                        && !this.isSkillForbid) {
                    if (this.schedule != null) {
                        this.schedule.cancel(true);
                    }
                    MobSkill msi = MobSkillFactory.getMobSkill(242, 7);
                    msi.applyEffect(null, this, true, isFacingLeft());
                    setCustomInfo(2420701, 0, 120000);
                }
                return;
            }
            if (hppercent <= 0.3D && getWillHplist().contains(Integer.valueOf(3))) {
                setHp((long) (getMobMaxHp() * bonusHp() * 0.3D / 100.0D));
                this.map.broadcastMessage(MobPacket.showBossHP(this));
                if (getCustomValue(2420702) == null
                        && !this.isSkillForbid) {
                    if (this.schedule != null) {
                        this.schedule.cancel(true);
                    }
                    MobSkill msi = MobSkillFactory.getMobSkill(242, 7);
                    msi.applyEffect(null, this, true, isFacingLeft());
                    setCustomInfo(2420702, 0, 120000);
                }
                return;
            }
        }
        if ((getId() == 8880342 || getId() == 8880302) && from.getSkillCustomValue0(24209) > 0L) {
            from.setSkillCustomInfo(24220, 0L, 3000L);
        }
        AttackerEntry attacker = null;
        MapleMonster linkMob = this.map.getMonsterById(this.stats.getHpLinkMob());
        if (linkMob != null) {
            linkMob.damage(from, damage, updateAttackTime, lastSkill);
            return;
        }
        if (from.getParty() != null) {
            attacker = new PartyAttackerEntry(from.getParty().getId());
        } else {
            attacker = new SingleAttackerEntry(from);
        }
        boolean replaced = false;
        for (AttackerEntry aentry : getAttackers()) {
            if (aentry != null && aentry.equals(attacker)) {
                attacker = aentry;
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            this.attackers.add(attacker);
        }
        if (GameConstants.isUnionRaid(from.getMapId())) {
            switch (getId()) {
                case 9833201:
                case 9833202:
                case 9833203:
                case 9833204:
                case 9833205:
                    this.hp = 1L;
                    for (MapleMonster monster : getMap().getAllMonster()) {
                        if (monster.getOwner() == from.getId()
                                && monster.getId() == getId() - 100
                                && monster.getBuff(MonsterStatus.MS_PowerImmune) == null) {
                            List<Pair<MonsterStatus, MonsterStatusEffect>> stats = new ArrayList<>();
                            stats.add(new Pair<>(MonsterStatus.MS_PowerImmune, new MonsterStatusEffect(146, 210000000, 1L)));
                            monster.applyMonsterBuff(monster.getMap(), stats, MobSkillFactory.getMobSkill(146, 13));
                            break;
                        }
                    }
                    return;
            }
        }
        if ((getId() == 9390612 || getId() == 9390610 || getId() == 9390911 || getId() == 8645066)
                && from.getKeyValue(200106, "golrux_in") == 1L) {
            from.setKeyValue(200106, "golrux_dmg", (from.getKeyValue(200106, "golrux_dmg") + damage) + "");
        }
        if (getId() == 8880305) {
            List<Obstacle> obs = new ArrayList<>();
            if (Randomizer.isSuccess(30)) {
                for (int i = 0; i < Randomizer.rand(1, 3); i++) {
                    int key = Randomizer.rand(63, 64);
                    int x = Randomizer.nextInt(1200) - 600;
                    int y = ((getTruePosition()).y > 0) ? -2020 : 159;
                    Obstacle ob = new Obstacle(key, new Point(x, y - 601), new Point(x, y), 40, (key == 64) ? 0 : 60, 1208, 111, 3, 599);
                    obs.add(ob);
                }
                this.map.CreateObstacle(this, obs);
            }
            return;
        }
        if (from.getBuffedEffect(SecondaryStat.Reincarnation) != null && this.stats.isBoss()
                && from.getReinCarnation() > 0) {
            from.setReinCarnation(from.getReinCarnation() - 1);
            if (from.getReinCarnation() == 0) {
                from.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(from, 0, 1320019, 1, 0, 0, (byte) (from.isFacingLeft() ? 1 : 0), true, from.getPosition(), null, null));
                from.getMap().broadcastMessage(from, CField.EffectPacket.showEffect(from, 0, 1320019, 1, 0, 0, (byte) (from.isFacingLeft() ? 1 : 0), false, from.getPosition(), null, null), false);
            }
            Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
            statups.put(SecondaryStat.Reincarnation, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) from.getBuffLimit(from.getBuffSource(SecondaryStat.Reincarnation)))));
            from.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, from.getBuffedEffect(SecondaryStat.Reincarnation), from));
        }
        if (getSeperateSoul() > 0) {
            if (from.getMap() != null
                    && from.getMap().getMonsterByOid(getSeperateSoul()) != null) {
                from.getMap().getMonsterByOid(getSeperateSoul()).damage(from, damage, updateAttackTime);
            }
            return;
        }
        if (from.getMap().getId() == 270051100 || from.getMap().getId() == 270050100) {
            boolean choas = !(from.getMap().getId() == 270050100);
            if (getId() >= 8820002 + (choas ? 100 : 0) && getId() <= 8820006 + (choas ? 100 : 0)
                    && from.getMap().getMonsterById(8820014 + (choas ? 100 : 0)) != null) {
                from.getMap().getMonsterById(8820014 + (choas ? 100 : 0)).getStats().setTagColor(4);
                from.getMap().getMonsterById(8820014 + (choas ? 100 : 0)).getStats().setTagBgColor(1);
                if (getHp() <= damage) {
                    from.getMap().getMonsterById(8820014 + (choas ? 100 : 0)).damage(from, getHp(), updateAttackTime);
                } else {
                    from.getMap().getMonsterById(8820014 + (choas ? 100 : 0)).damage(from, damage, updateAttackTime);
                }
            }
        }
        if (this.map.isElitebossrewardmap() && this.map.getElitebossrewardtype() == 1 && getId() == 8220027) {
            int[] itemlist = {2432391, 2432392, 2432393, 2432394, 2432395, 2432396, 2432397};
            int Random = 0;
            Random = (int) Math.floor(Math.random() * itemlist.length);
            for (int i = 0; i < Randomizer.rand(1, 3); i++) {
                Random = (int) Math.floor(Math.random() * itemlist.length);
                int itemid = itemlist[Random];
                Item toDrop = new Item(itemid, (short) 0, (short) 1, 0);
                this.map.spawnItemDrop(this, getController(), toDrop, new Point((getTruePosition()).x + ((i == 1) ? 25 : ((i == 2) ? 50 : 0)), (getTruePosition()).y), true, false);
            }
        }
        attacker.addDamage(from, damage, updateAttackTime);
        
                NumberFormat Number = NumberFormat.getInstance();
        if (getMobMaxHp() > Integer.MAX_VALUE) {
            if (getId() == 9833376) {
                from.DamageMeter += damage;
                from.dropMessage(-1, "누적 데미지 : " + from.DamageMeter);
                from.getClient().getSession().writeAndFlush(CField.getGameMessage(9, "누적 데미지 : " + from.DamageMeter));
            }
        }
        if (from.getMapId() == 120000102) {
            from.setDamageMeter(from.getDamageMeter() + damage);
        }
        MapleDiseases cap = from.getDisease(SecondaryStat.CapDebuff);
        if (cap != null && ((cap.getValue() == 100 && (getId() == 8900001 || getId() == 8900101)) || (cap.getValue() == 200 && (getId() == 8900002 || getId() == 8900102)))) {
            if (this.hp < this.stats.getHp() * bonusHp()) {
                this.hp = (long) Math.min((this.hp + damage), this.stats.getHp() * bonusHp());
                if (this.sponge.get() == null && this.hp > 0L) {
                    switch (this.stats.getHPDisplayType()) {
                        case 0:
                            this.map.broadcastMessage(MobPacket.showBossHP(this), getTruePosition());
                            break;
                        case 1:
                            this.map.broadcastMessage(from, MobPacket.damageFriendlyMob(this, 1L, true), false);
                            break;
                        case 2:
                            this.map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
                            break;
                        case 3:
                            for (AttackerEntry mattacker : getAttackers()) {
                                if (mattacker != null) {
                                    for (AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                                        if (cattacker != null && cattacker.getAttacker().getMap() == from.getMap()
                                                && cattacker.getLastAttackTime() >= System.currentTimeMillis() - 4000L) {
                                            cattacker.getAttacker().getClient().getSession().writeAndFlush(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            }
            return;
        }
        if (GameConstants.isAggressIveMonster(getId())) {
            from.setAggressiveDamage(from.getAggressiveDamage() + damage);
        }
        if (getId() == 8880504) {
            if (from.getMap().getMonsterById(8880519) != null) {
                from.getMap().getMonsterById(8880519).damage(from, damage, updateAttackTime);
            }
            return;
        }
        if (from.getMap().getId() / 10000 == 92507) {
            int stage = from.getMap().getId() % 92507 / 100;
            if (getCustomValue0(getId()) == 1L) {
                return;
            }
            if ((stage == 9 || stage == 13 || stage == 15 || stage == 45)
                    && damage >= getHp() && getCustomValue0(getId()) == 0L) {
                setCustomInfo(getId(), 1, 0);
                from.getMap().broadcastMessage(MobPacket.notDamage(getObjectId()));
                from.getMap().broadcastMessage(MobPacket.notDamageEffect(getObjectId(), 0));
                setHp(1L);
                from.getClient().getSession().writeAndFlush(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
                Timer.EtcTimer.getInstance().schedule(() -> this.map.killMonster(this, from, true, false, (byte) 1, lastSkill), 5000L);
                return;
            }
        }
        if (from.getBuffedValue(80002902) && !isAlive() && (getId() == 8644653 || getId() == 8644656 || getId() == 8644654 || getId() == 8644657)) {
            from.setSkillCustomInfo(8644651, ((int) from.getSkillCustomValue0(8644651) - 20), 0L);
            from.getClient().send(MobPacket.handleDuskGauge(true, (int) from.getSkillCustomValue0(8644651), 1000));
            if (from.getSkillCustomValue0(8644651) <= 0L) {
                from.setSkillCustomInfo(8644650, 1L, 0L);
                from.setSkillCustomInfo(8644651, 0L, 0L);
                while (from.getBuffedValue(80002902)) {
                    from.cancelEffect(from.getBuffedEffect(80002902));
                }
                for (MapleMonster m : getMap().getAllMonster()) {
                    if (m.getOwner() == from.getId() && (m.getId() == 8644653 || m.getId() == 8644656 || m.getId() == 8644654 || m.getId() == 8644657)) {
                        getMap().killMonster(m);
                    }
                }
            }
        }
        if (getId() == 8880111 || getId() == 8880101) {
            if (getCustomValue0(8880111) == 1L) {
                addSkillCustomInfo(8880112, damage);
                if (getCustomValue0(8880112) >= 1000000000L) {
                    getMap().broadcastMessage(MobPacket.demianRunaway(this, (byte) 1, MobSkillFactory.getMobSkill(214, 14), 0, false));
                    setCustomInfo(8880111, 2, 0);
                }
            }
        }
        if (getId() >= 9833101 && getId() <= 9833105) {
            from.setUnionNujuk(from.getUnionNujuk() + damage);
            return;
        }
        if (this.shield > 0L) {
            this.shield -= damage;
            this.map.broadcastMessage(MobPacket.showBossHP(this));
            getMap().broadcastMessage(MobPacket.mobBarrier(this));
            return;
        }
        if (this.map.getId() / 10000 == 92507) {
            this.map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
        }
        if (getHPPercent() <= 30 && (getId() == 8880100 || getId() == 8880110)) {
            DemainChangePhase(from);
            return;
        }
        if (getId() == 8880153) {
            if (getHPPercent() <= 10 && getCustomValue0(8881153) <= 0L) {
                setHp(getStats().getHp() * 10L / 100L);
                this.map.broadcastMessage(MobPacket.showBossHP(this));
                setCustomInfo(8881153, 1, 0);
                setCustomInfo(8881154, 0, 5000);
                getMap().broadcastMessage(CWvsContext.getTopMsg("루시드가 체력을 회복합니다."));
                Timer.MobTimer.getInstance().schedule(() -> {
                    addHp(getStats().getHp() * 5L / 100L, true);
                    getMap().broadcastMessage(MobPacket.healEffectMonster(getObjectId(), 114, 82));
                }, 5000L);
                return;
            }
        }
        if (getId() == 8641018 && getCustomValue(8641018) == null && !GameConstants.is_forceAtom_attack_skill(lastSkill)) {
            int mobid = (getController().getSkillCustomValue0(450001402) == 0L) ? 8641019 : ((getController().getSkillCustomValue0(450001402) == 1L) ? 8641020 : 8641021);
            MapleMonster mob = MapleLifeFactory.getMonster(mobid);
            getMap().spawnMonsterOnGroundBelow(mob, new Point((getPosition()).x + Randomizer.rand(-150, 150), (getPosition()).y));
            setCustomInfo(8641018, 0, 3000);
            return;
        }
        if (getId() == 8800002 || getId() == 8800102) {
            int mobsize = 0;
            for (MapleMonster mob : getMap().getAllMonster()) {
                if ((mob.getId() >= 8800003 && mob.getId() <= 8800010) || (mob.getId() >= 8800103 && mob.getId() <= 8800110)) {
                    mobsize++;
                }
            }
            if (getHPPercent() <= 20 || (mobsize <= 0 && getId() == 8800002)) {
                getMap().broadcastMessage(MobPacket.setMonsterProPerties(getObjectId(), 0, 0, 0));
            }
            if (getSpecialtxt() != null && (getHPPercent() <= 20 || (mobsize <= 0 && getId() == 8800002))) {
                setSpecialtxt((String) null);
                getMap().broadcastMessage(MobPacket.setMonsterProPerties(getObjectId(), 0, 0, 0));
                List<MapleNodes.Environment> envs = new ArrayList<>();
                for (MapleNodes.Environment env : getMap().getNodez().getEnvironments()) {
                    if (env.getName().contains("zdc")) {
                        env.setShow(false);
                        envs.add(env);
                    }
                }
                getMap().broadcastMessage(CField.getUpdateEnvironment(envs));
                for (MapleMonster mob : getMap().getAllMonster()) {
                    if (mob.getId() == 8800002 || mob.getId() == 8800102) {
                        mob.setPhase((byte) 3);
                    }
                    if ((mob.getId() >= 8800003 && mob.getId() <= 8800010) || (mob.getId() >= 8800103 && mob.getId() <= 8800110)) {
                        getMap().killMonster(mob.getId());
                        continue;
                    }
                    if (mob.getId() >= 8800130 && mob.getId() <= 8800137) {
                        getMap().killMonster(mob.getId());
                        continue;
                    }
                    if (mob.getId() == 8800117 || mob.getId() == 8800120) {
                        getMap().killMonster(mob.getId());
                    }
                }
            }
        }
        if (from.getMapId() == 993192800 && ((getId() >= 9833935 && getId() <= 9833946) || (getId() >= 9833947 && getId() <= 9833958))) {
            boolean nextStage = (from.getMap().getAllMonster().size() == 1 && getHp() - damage <= 0L);
            int stage = (int) from.getSkillCustomValue0(993192801);
            if (getHp() - damage <= 0L) {
                if (getCustomValue0(getId()) > 0L) {
                    from.getClient().setCustomData(100795, "point", "" + (Integer.parseInt(from.getClient().getCustomData(100795, "point")) + getCustomValue0(getId())) + "");
                    from.getClient().send(CField.PunchKingPacket(from, 3, new int[]{getObjectId(), (int) getCustomValue0(getId())}));
                    from.getClient().send(CField.PunchKingPacket(from, 2, new int[]{Integer.parseInt(from.getClient().getCustomData(100795, "point"))}));
                }
            } else {
                from.setSkillCustomInfo(getId(), from.getSkillCustomValue0(getId()) + damage, 0L);
                long totaldamage = from.getSkillCustomValue(getId()).longValue();
                if (totaldamage > getHp()) {
                    totaldamage = getHp();
                }
                if (totaldamage >= getStats().getHp() / 100L * 10L) {
                    int addpoint = 0;
                    while (totaldamage > 0L) {
                        totaldamage -= getStats().getHp() * 10L / 100L;
                        addpoint++;
                    }
                    from.removeSkillCustomInfo(getId());
                    int plus = (int) getCustomValue0(getId()) / 10;
                    if (plus <= 0) {
                        plus = 1;
                    }
                    int totaladdpoint = plus * addpoint;
                    if (totaladdpoint > getCustomValue0(getId())) {
                        totaladdpoint = (int) getCustomValue0(getId());
                    }
                    addSkillCustomInfo(getId(), -totaladdpoint);
                    from.getClient().setCustomData(100795, "point", "" + (Integer.parseInt(from.getClient().getCustomData(100795, "point")) + totaladdpoint) + "");
                    from.getClient().send(CField.PunchKingPacket(from, 3, new int[]{getObjectId(), totaladdpoint}));
                    from.getClient().send(CField.PunchKingPacket(from, 2, new int[]{Integer.parseInt(from.getClient().getCustomData(100795, "point"))}));
                }
            }
            if (nextStage) {
                if (stage + 1 > 12) {
                    from.getClient().send(CField.PunchKingPacket(from, 0, new int[]{4}));
                    Timer.EventTimer.getInstance().schedule(() -> {
                        if (from.getMapId() == 993192800) {
                            from.warp(993192701);
                        }
                    }, 3000L);
                } else {
                    from.getClient().send(SLFCGPacket.OnYellowDlg(9062507, 100, "#rSTAGE " + (stage + 1) + "#k 시작한담!", ""));
                    MapleMonster boss = MapleLifeFactory.getMonster(9833935 + stage);
                    boss.setOwner(from.getId());
                    getMap().spawnMonsterOnGroundBelow(boss, new Point(20, 581));
                    for (int i = -140; i <= 180; i += 80) {
                        MapleMonster mob = MapleLifeFactory.getMonster(9833947 + stage);
                        mob.setOwner(from.getId());
                        getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833947 + stage), new Point(i, 581));
                    }
                    stage++;
                    from.setSkillCustomInfo(993192801, stage, 0L);
                    from.getClient().send(CField.PunchKingPacket(from, 1, new int[]{stage}));
                }
            }
        }
        if (this.stats.getSelfD() != -1) {
            this.hp -= damage;
            if (this.hp > 0L) {
                if (this.hp < this.stats.getSelfDHp()) {
                    this.map.killMonster(this, from, false, false, this.stats.getSelfD(), lastSkill);
                } else {
                    for (AttackerEntry mattacker : getAttackers()) {
                        for (AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                            if (cattacker.getAttacker().getMap() == from.getMap()
                                    && cattacker.getLastAttackTime() >= System.currentTimeMillis() - 4000L) {
                                cattacker.getAttacker().getClient().getSession().writeAndFlush(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
                            }
                        }
                    }
                }
            } else {
                this.map.killMonster(this, from, true, false, (byte) 1, lastSkill);
            }
        } else {
            if (this.sponge.get() != null
                    && ((MapleMonster) this.sponge.get()).hp > 0L) {
                if (getStats().getHp() < damage) {
                    ((MapleMonster) this.sponge.get()).hp -= getStats().getHp();
                } else {
                    ((MapleMonster) this.sponge.get()).hp -= damage;
                }
                boolean send = true;
                if (((MapleMonster) this.sponge.get()).hp <= 0L && (((MapleMonster) this.sponge.get()).getId() == 8810122 || ((MapleMonster) this.sponge.get()).getId() == 8810018)) {
                    for (MapleMonster m : getMap().getAllMonster()) {
                        if (m.getId() != ((MapleMonster) this.sponge.get()).getId()) {
                            getMap().killMonsterType(m, 1);
                        }
                    }
                    send = false;
                }
                if (((MapleMonster) this.sponge.get()).hp <= 0L) {
                    ((MapleMonster) this.sponge.get()).hp = 1L;
                    this.map.broadcastMessage(MobPacket.showBossHP(((MapleMonster) this.sponge.get()).getId(), -1L, ((MapleMonster) this.sponge.get()).getMobMaxHp()));
                    if (send) {
                        this.map.killMonster(this.sponge.get(), from, true, false, (byte) 1, lastSkill);
                    }
                } else {
                    this.map.broadcastMessage(MobPacket.showBossHP(this.sponge.get()));
                }
            }
            if (this.hp > 0L) {
                if (this.barrier > 0L) {
                    if (this.barrier >= damage) {
                        this.barrier -= damage;
                    } else {
                        this.barrier = 0L;
                        this.hp -= damage - this.barrier;
                    }
                } else {
                    this.hp -= damage;
                }
                if (this.eventInstance != null) {
                    this.eventInstance.monsterDamaged(from, this, damage);
                } else {
                    EventInstanceManager em = from.getEventInstance();
                    if (em != null) {
                        em.monsterDamaged(from, this, damage);
                    }
                }
                if (getStats().isMobZone()) {
                    if (getId() == 8880101 || getId() == 8880111) {
                        byte phase;
                        if (getHPPercent() <= 10) {
                            phase = 4;
                        } else if (getHPPercent() <= 15) {
                            phase = 3;
                        } else if (getHPPercent() <= 20) {
                            phase = 2;
                        } else {
                            phase = 1;
                        }
                        int maxcount = (phase == 2) ? 2 : ((phase == 4) ? 3 : 1);
                        int size = 0;
                        for (MapleMonster m : getMap().getAllMonster()) {
                            if (m.getId() == 8880102) {
                                size++;
                            }
                        }
                        if (size < maxcount) {
                            MapleMonster m = MapleLifeFactory.getMonster(8880102);
                            m.getStats().setSpeed(80);
                            getMap().spawnMonsterWithEffect(m, MobSkillFactory.getMobSkill(201, 182).getSpawnEffect(), getPosition());
                        }
                        if (this.phase != phase) {
                            setPhase(phase);
                        }
                        this.map.broadcastMessage(MobPacket.changePhase(this));
                        this.map.broadcastMessage(MobPacket.changeMobZone(this));
                    } else if (getId() != 8644650 && getId() != 8644655 && getId() != 8880503) {
                        byte phase;
                        if (getHPPercent() > 75) {
                            phase = 1;
                        } else if (getHPPercent() > 50) {
                            phase = 2;
                        } else if (getHPPercent() > 25) {
                            phase = 3;
                        } else {
                            phase = 4;
                        }
                        if (this.phase != phase) {
                            setPhase(phase);
                        }
                        this.map.broadcastMessage(MobPacket.changePhase(this));
                        this.map.broadcastMessage(MobPacket.changeMobZone(this));
                    }
                }
                if (this.hp > 0L) {
                    MapleMonster will;
                    int count = (100 - getHPPercent()) / 10;
                    if (this.map.getLucidCount() + this.map.getLucidUseCount() < count && (getId() == 8880140 || getId() == 8880141 || getId() == 8880150 || getId() == 8880151)) {
                        if (this.map.getLucidCount() < 3) {
                            this.map.setLucidCount(this.map.getLucidCount() + 1);
                            this.map.broadcastMessage(CField.enforceMSG("나팔동상 근처에서 '채집'키를 눌러 사용하면 루시드의 힘을 억제할 수 있습니다!", 222, 2000));
                        }
                        this.map.broadcastMessage(MobPacket.BossLucid.changeStatueState(false, this.map.getLucidCount(), false));
                    }
                    switch (getId()) {
                        case 8880300:
                        case 8880303:
                        case 8880304:
                            will = this.map.getMonsterById(8880300);
                            if (will != null) {
                                List<Integer> hps = will.getWillHplist();
                                this.map.broadcastMessage(MobPacket.BossWill.setWillHp(hps, this.map, 8880300, 8880303, 8880304));
                            }
                            break;
                        case 8880340:
                        case 8880343:
                        case 8880344:
                            will = this.map.getMonsterById(8880340);
                            if (will != null) {
                                List<Integer> hps = will.getWillHplist();
                                this.map.broadcastMessage(MobPacket.BossWill.setWillHp(hps, this.map, 8880340, 8880343, 8880344));
                            }
                            break;
                        case 8880301:
                        case 8880341:
                            this.map.broadcastMessage(MobPacket.BossWill.setWillHp(getWillHplist()));
                            break;
                    }
                }
                if (this.sponge.get() == null && this.hp > 0L) {
                    switch (this.stats.getHPDisplayType()) {
                        case 0:
                            this.map.broadcastMessage(MobPacket.showBossHP(this), getTruePosition());
                            if (getId() == 8870100 && getHPPercent() <= 50) {
                                setPhase((byte) 2);
                                this.map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
                            }
                            break;
                        case 1:
                            this.map.broadcastMessage(from, MobPacket.damageFriendlyMob(this, 1L, true), false);
                            break;
                        case 2:
                            this.map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
                            break;
                        case 3:
                            for (AttackerEntry mattacker : getAttackers()) {
                                if (mattacker != null) {
                                    for (AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                                        if (cattacker != null && cattacker.getAttacker().getMap() == from.getMap()
                                                && cattacker.getLastAttackTime() >= System.currentTimeMillis() - 4000L) {
                                            cattacker.getAttacker().getClient().getSession().writeAndFlush(MobPacket.showMonsterHP(getObjectId(), getHPPercent()));
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
                if (getStats().isBoss() && from.getSkillLevel(131001026) > 0 && !from.getBuffedValue(131003026) && !from.skillisCooling(131001026)) {
                    SkillFactory.getSkill(131001026).getEffect(1).applyTo(from);
                }
                if (this.hp <= 0L) {
                    if (GameConstants.isExecutionSkill(lastSkill) && from.skillisCooling(63001002)) {
                        from.removeCooldown(63001002);
                    }
                    if (from.getSkillLevel(131001026) > 0 && !from.getBuffedValue(131003026) && !from.skillisCooling(131001026)) {
                        SkillFactory.getSkill(131001026).getEffect(1).applyTo(from);
                    }
                    if (lastSkill == 400011027 || lastSkill == 64121011) {
                        this.map.broadcastMessage(MobPacket.deathEffect(getObjectId(), lastSkill, from.getId()));
                    } else if (GameConstants.isYeti(from.getJob())) {
                        if (lastSkill == 135001000 || lastSkill == 135001001 || lastSkill == 135001002) {
                            from.getYetiGauge(lastSkill, 1);
                        } else if (getBuff(135001012) != null) {
                            from.getYetiGauge(999, 0);
                        }
                    }
                    if (this.stats.getHPDisplayType() == 0) {
                        this.map.broadcastMessage(MobPacket.showBossHP(getId(), -1L, (long) (getMobMaxHp() * bonusHp())), getTruePosition());
                    }
                    this.map.killMonster(this, from, true, false, (byte) 1, lastSkill);
                    if (from.getMonsterCombo() == 0) {
                        from.setMonsterComboTime(System.currentTimeMillis());
                    }
                    if (from.getKeyValue(16700, "count") < 300L) {
                        from.setKeyValue(16700, "count", String.valueOf(from.getKeyValue(16700, "count") + 1L));
                    }
                    if (GameConstants.isExecutionSkill(lastSkill) && from.skillisCooling(63001002)) {
                        from.removeCooldown(63001002);
                    }
                }
            }
        }
        startDropItemSchedule();
    }

    public int getHPPercent() {
        if (this.elitemonster || this.eliteboss) {
            return (int) Math.ceil(this.hp * 100.0D / this.elitehp);
        }
        return (int) Math.ceil(this.hp * 100.0D / getStats().getHp());
    }

    public double getHPPercentDouble() {
        return Math.ceil(this.hp * 100.0D / getMobMaxHp());
    }

    public final void heal(long hp, long mp, boolean broadcast) {
        if (getBuff(MonsterStatus.MS_DebuffHealing) != null) {
            hp -= hp / 100L * getBuff(MonsterStatus.MS_DebuffHealing).getValue();
        }
        if (hp < 0L) {
            hp = 0L;
        }
        long TotalHP = getHp() + hp;
        long TotalMP = getMp() + mp;
        if (TotalHP >= getMobMaxHp()) {
            setHp(getMobMaxHp());
        } else {
            setHp(TotalHP);
        }
        if (TotalMP >= getMp()) {
            setMp(getMp());
        } else {
            setMp((int) TotalMP);
        }
        if (broadcast) {
            this.map.broadcastMessage(MobPacket.healMonster(getObjectId(), hp));
        } else if (this.sponge.get() != null) {
            ((MapleMonster) this.sponge.get()).hp += hp;
        }
    }

    public final void killed() {
        for (Pair<MonsterStatus, MonsterStatusEffect> skill : this.stati) {
            if (((MonsterStatusEffect) skill.getRight()).getSchedule() != null && !((MonsterStatusEffect) skill.getRight()).getSchedule().isDone()) {
                ((MonsterStatusEffect) skill.getRight()).getSchedule().cancel(true);
            }
        }
        if (this.listener != null) {
            this.listener.monsterKilled();
        }
        if (getSchedule() != null) {
            getSchedule().cancel(true);
        }
        this.listener = null;
    }

    private final void giveExpToCharacter(MapleCharacter attacker, long exp, boolean highestDamage, int numExpSharers, byte pty, byte Class_Bonus_EXP_PERCENT, byte Premium_Bonus_EXP_PERCENT, int lastskillID) {
        int[] linkMobs = {
            9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161,
            9010162, 9010163, 9010164, 9010165, 9010166, 9010167, 9010168, 9010169, 9010170, 9010171,
            9010172, 9010173, 9010174, 9010175, 9010176, 9010177, 9010178, 9010179, 9010180, 9010181};
        for (int linkMob : linkMobs) {
            if (getId() == linkMob) {
                double plus = 1.0E-4D;
                plus *= (276 - attacker.getLevel()) * 0.025D;
                exp = (int) (GameConstants.getExpNeededForLevel(attacker.getLevel()) * plus);
            }
        }
        
       if (attacker.getMapId() == 1 || attacker.getMapId() == 2 || attacker.getMapId() == 3 ) {
              exp *= 100000L;
          }
        
        if (exp > 0L) {//레벨별 배율
            MonsterStatusEffect ms = getBuff(MonsterStatus.MS_Showdown);
            if (ms != null) {
                exp += (int) (exp * ms.getValue() / 100.0D);
            }
            if (attacker.hasDisease(SecondaryStat.Curse)) {
                exp /= 2L;
            }
            /* 1095 */ if (attacker.getLevel() <= 100) { // ~100까지
                /* 1096 */ exp *= 1000L; // 배율
                /* 1097 */            } else if (attacker.getLevel() <= 150) { // 101 ~ 150까지
                /* 1098 */ exp *= 1000L; // 배율
                /* 1099 */            } else if (attacker.getLevel() <= 200) {
                /* 1100 */ exp *= 1000L;
                /* 1101 */            } else if (attacker.getLevel() <= 210) {
                /* 1102 */ exp *= 70L;
                /* 1103 */            } else if (attacker.getLevel() <= 220) {
                /* 1104 */ exp *= 60L;
                /* 1105 */            } else if (attacker.getLevel() <= 230) {
                /* 1106 */ exp *= 50L;
                /* 1107 */            } else if (attacker.getLevel() <= 240) {
                /* 1108 */ exp *= 40L;
                /* 1109 */            } else if (attacker.getLevel() <= 250) {
                /* 1110 */ exp *= 30L;
                /* 1111 */            } else if (attacker.getLevel() <= 260) {
                /* 1112 */ exp *= 30L;
                /* 1113 */            } else if (attacker.getLevel() <= 270) {
                /* 1114 */ exp *= 20L;
                /* 1115 */            } else if (attacker.getLevel() <= 275) {
                /* 1116 */ exp *= 20L;
                /* 1117 */            } else if (attacker.getLevel() <= 285) {
                /* 1118 */ exp *= 15L;
                /* 1119 */            } else if (attacker.getLevel() <= 295) {
                /* 1120 */ exp *= 15L;
                /* 1121 */            } else if (attacker.getLevel() <= 300) {
                /* 1122 */ exp *= 10L;
            } else {
                /* 1124 */ exp *= 10L;
            }
            exp *= ((GameConstants.isPinkBean(attacker.getJob()) || GameConstants.isYeti(attacker.getJob())) ? 4L : 2L);
            if (!attacker.getBuffedValue(80002282) && attacker.getMap().getRuneCurse() > 0 && !GameConstants.보스맵(getMap().getId()) && !GameConstants.isContentsMap(getMap().getId())) {
                attacker.getClient().getSession().writeAndFlush(CField.runeCurse("룬을 해방하여 엘리트 보스의 저주를 풀어야 합니다!!\\n저주 " + attacker.getMap().getRuneCurse() + "단계 :  경험치 획득, 드롭률 " + attacker.getMap().getRuneCurseDecrease() + "% 감소 효과 적용 중", false));
                exp -= exp * attacker.getMap().getRuneCurseDecrease() / 100L;
            }
            if (attacker.getLevel() >= 200) {
                int level = 20;
                if (attacker.getMap().isSpawnPoint() && !getStats().isBoss() && (getStats().getLevel() - level > attacker.getLevel() || attacker.getLevel() > getStats().getLevel() + level)) {
                    exp -= exp * 80L / 100L;
                    if (attacker.getSkillCustomValue0(60524) == 0L) {
                        attacker.setSkillCustomInfo(60524, 1L, 0L);
                        attacker.getClient().getSession().writeAndFlush(CField.UIPacket.detailShowInfo("레벨 범위를 벗어난 몬스터를 사냥 시 경험치와 메소 획득량이 크게 감소합니다.", 3, 20, 20));
                    }
                }
            }
            if (isBuffed(MonsterStatus.MS_SeperateSoulP) || isBuffed(MonsterStatus.MS_SeperateSoulC)) {
                exp *= 2L;
            }
            if (attacker.getKeyValue(210416, "TotalDeadTime") > 0L) {
                exp = (long) (exp * 0.2D);
            }
            if ((getId() >= 9830000 && getId() <= 9830018) || (getId() >= 9831000 && getId() <= 9831014)) {
                if (attacker.getLevel() <= 210) {
                    exp = (int) (GameConstants.getExpNeededForLevel(attacker.getLevel()) * 0.005D);
                } else if (attacker.getLevel() <= 230) {
                    exp = (int) (GameConstants.getExpNeededForLevel(attacker.getLevel()) * 1.0E-4D);
                } else {
                    exp = (int) (GameConstants.getExpNeededForLevel(attacker.getLevel()) * 1.0E-5D);
                }
            }
            attacker.gainExpMonster(exp, true, highestDamage);
            attacker.getTrait(MapleTrait.MapleTraitType.charisma).addExp(this.stats.getCharismaEXP(), attacker);
        }
    }

    public final int killBy(MapleCharacter killer, int lastSkill) {
        if (this.killed) {
            return 1;
        }
        this.killed = true;
        long totalBaseExp = getMobExp();
        AttackerEntry highest = null;
        long highdamage = 0L;
        List<AttackerEntry> list = getAttackers();
        for (AttackerEntry attackEntry : list) {
            if (attackEntry != null && attackEntry.getDamage() > highdamage) {
                highest = attackEntry;
                highdamage = attackEntry.getDamage();
            }
        }
        for (AttackerEntry attackEntry : list) {
            if (attackEntry != null) {
                attackEntry.killedMob(getMap(), totalBaseExp, (attackEntry == highest), lastSkill);
            }
        }
        MapleCharacter controll = this.controller.get();
        if (controll != null) {
            controll.getClient().getSession().writeAndFlush(MobPacket.stopControllingMonster(getObjectId()));
            controll.stopControllingMonster(this);
        }
        if (killer != null && killer.getPosition() != null && killer.getKeyValue(501661, "point") < 9999 && !(getId() >= 8644101 && getId() <= 8644112)) {
            int temp = Randomizer.nextInt(2) + 1;
            int bonus = 1;
            killer.AddStarDustPoint2(temp * bonus);
        }
        if (killer != null && killer.getPosition() != null && (getId() < 8644101 || getId() > 8644112) && getStats().getLevel() >= killer.getLevel() - 20 && getStats().getLevel() <= killer.getLevel() + 20) {
            if (killer.getBuffedValue(80003025)) {
                int maximum = 2000;
                if (killer.getKeyValue(100722, "today") < maximum) {
                    boolean givecoin = Randomizer.isSuccess(10);
                    if (givecoin) {
                        killer.setKeyValue(100722, "today", "" + (killer.getKeyValue(100722, "today") + 1L));
                        killer.setKeyValue(100722, "cnt", "" + (killer.getKeyValue(100722, "cnt") + 1L));
                        killer.AddStarDustPoint(1, 100, getTruePosition());
                        killer.getClient().getSession().writeAndFlush(SLFCGPacket.SkillfromMonsterEffect(80003025, 0, (getTruePosition()).x, (getTruePosition()).y));
                        int nowcoin = (int) killer.getKeyValue(100722, "cnt");
                        if (nowcoin == 5) {
                            if (Randomizer.isSuccess(15)) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "나는 널 그리워 할꺼야.\r\n 항상..."));
                            } else if (Randomizer.isSuccess(15)) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "에르다를 조금만 더 모으면 거대한 마법의 종이 나타날 거야."));
                            } else if (Randomizer.isSuccess(15)) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "아아, 이 세계가 에르다로 가득해."));
                            } else if (Randomizer.isSuccess(15)) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "네오 캐슬을 지나~\r\n늪을 건너~"));
                            } else if (Randomizer.isSuccess(15)) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "네오 캐슬에 다시 오고 싶다면 9와 4분의 3번째 에르다를 찾으렴."));
                            } else {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "부디 눈꽃 순록들에게\r\n종소리가 닿기를..."));
                            }
                        } else if (nowcoin == 10) {
                            List<MapleMonster> monsters = getMap().getAllMonster();
                            killer.setKeyValue(100722, "cnt", "0");
                            killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 900, "에르다의 기운이 모여서\r\n거대한 #r마법의 종#k이 나타났어."));
                            killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 900, "어서 종을 울려서\r\n#b네오 스톤#k을 모아보렴."));
                            MapleMonster m2 = MapleLifeFactory.getMonster(9833905);
                            m2.setOwner(killer.getId());
                            getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9833905), getPosition());
                        }
                    }
                }
            }
            if (killer.getBuffedEffect(SecondaryStat.EventSpecialSkill, 80003016) != null) {
                if (killer.getEventKillingMode()) {
                    if (killer.getKeyValue(100711, "today") < 20000L) {
                        if (killer.getEventMobCount() > 0) {
                            killer.setEventMobCount(killer.getEventMobCount() - 1);
                            killer.AddStarDustPoint(1, 100, getTruePosition());
                            killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillSetCount(80003016, killer.getEventMobCount()));
                            killer.getClient().getSession().writeAndFlush(SLFCGPacket.SkillfromMonsterEffect(80003016, 0, (getTruePosition()).x, (getTruePosition()).y));
                            if (killer.getEventMobCount() <= 0) {
                                killer.setEventKillingMode(false);
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬의 힘으로 네오 스톤을\r\n#r모두#k 찾았어!"));
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "이것이 #b에르다#k로 빚어낸 마법!\r\n너무 아름다워..."));
                                killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEnd(80003016));
                                killer.getEventSkillTimer().cancel(true);
                                killer.setEventSkillTimer((ScheduledFuture<?>) null);
                            }
                        } else {
                            killer.setEventKillingMode(false);
                            killer.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("오늘 획득 가능한 네오 스톤을 모두 획득햇습니다."));
                            killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "후후, 오늘은 이 정도면 충분할 것 같아. 고마워!"));
                            killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEnd(80003016));
                            killer.cancelEffectFromBuffStat(SecondaryStat.EventSpecialSkill, 80003016);
                            killer.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(1291));
                            killer.dropMessage(5, "르네의 마법구슬이 비활성화되었습니다.");
                        }
                    } else {
                        killer.setEventKillingMode(false);
                        killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEnd(80003016));
                        killer.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("오늘 획득 가능한 네오 스톤을 모두 획득햇습니다."));
                        killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "후후, 오늘은 이 정도면 충분할 것 같아. 고마워!"));
                        killer.cancelEffectFromBuffStat(SecondaryStat.EventSpecialSkill, 80003016);
                        killer.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(1291));
                        killer.dropMessage(5, "르네의 마법구슬이 비활성화되었습니다.");
                    }
                } else {
                    boolean v = Randomizer.isSuccess2(30);
                    if ((Randomizer.isSuccess(5) || v)
                            && !getStats().isBoss()) {
                        if (killer.getKeyValue(100711, "today") < 30000L) {
                            killer.AddStarDustPoint(1, 100, getTruePosition());
                            if (killer.getKeyValue(100708, "fever") == -1L) {
                                killer.setKeyValue(100708, "fever", "0");
                            }
                            if (v) {
                                killer.getClient().getSession().writeAndFlush(SLFCGPacket.SkillfromMonsterEffect(80003016, 1, (getTruePosition()).x, (getTruePosition()).y));
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬에 #b에르다#k 급속충전된거같아! 준비해!"));
                                killer.setKeyValue(100708, "fever", "100");
                            } else {
                                killer.getClient().getSession().writeAndFlush(SLFCGPacket.SkillfromMonsterEffect(80003016, 0, (getTruePosition()).x, (getTruePosition()).y));
                                killer.setKeyValue(100708, "fever", (killer.getKeyValue(100708, "fever") + 5L) + "");
                            }
                            if (killer.getKeyValue(100708, "fever") == 25L) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬의 #b에르다#k가 차오르기 시작했어."));
                            } else if (killer.getKeyValue(100708, "fever") == 50L) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬에 #b에르다#k가 절반정도 찬거같아. 조금더 힘내!"));
                            } else if (killer.getKeyValue(100708, "fever") == 75L) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬에 #b에르다#k가 거의 차오른거같아. 화이팅!"));
                            } else if (killer.getKeyValue(100708, "fever") >= 100L) {
                                killer.setKeyValue(100708, "fever", "0");
                                if (!killer.getEventKillingMode()) {
                                    int count = 30;
                                    killer.setEventKillingMode(true);
                                    killer.setEventMobCount(count);
                                    killer.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("르네의 마법구슬이 빛을 발하며 온 몸에 에르다의 힘이 깃듭니다"));
                                    killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬이 가득 차오른 지금!\r\n#b네오 크리스탈 파워!#k"));
                                    killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEffect(80003016, 30000));
                                    killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillStart(80003016, 30000));
                                    killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillSetCount(80003016, count));
                                    if (killer.getEventSkillTimer() == null) {
                                        ScheduledFuture<?> qwer = Timer.ShowTimer.getInstance().schedule(() -> {
                                            if (killer != null) {
                                                killer.setEventKillingMode(false);
                                                killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEnd(80003016));
                                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬의 힘으로 네오 스톤을\r\n#r일부#k 찾았어!"));
                                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "이것이 #b에르다#k로 빚어낸 마법!\r\n너무 아름다워..."));
                                                killer.getEventSkillTimer().cancel(true);
                                                killer.setEventSkillTimer((ScheduledFuture<?>) null);
                                            }
                                        }, 30000L);
                                        killer.setEventSkillTimer(qwer);
                                    } else {
                                        killer.getEventSkillTimer().cancel(true);
                                        killer.setEventSkillTimer((ScheduledFuture<?>) null);
                                        ScheduledFuture<?> qwer = Timer.ShowTimer.getInstance().schedule(() -> {
                                            if (killer != null) {
                                                killer.setEventKillingMode(false);
                                                killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEnd(80003016));
                                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "마법구슬의 힘으로 네오 스톤을\r\n#r일부#k 찾았어!"));
                                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "이것이 #b에르다#k로 빚어낸 마법!\r\n너무 아름다워..."));
                                                killer.getEventSkillTimer().cancel(true);
                                                killer.setEventSkillTimer((ScheduledFuture<?>) null);
                                            }
                                        }, 30000L);
                                        killer.setEventSkillTimer(qwer);
                                    }
                                }
                            } else {
                                killer.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("에르다를 찾아 네오 스톤 1개를 찾았습니다. (구슬 게이지: " + killer.getKeyValue(100708, "fever") + "%)"));
                            }
                        } else {
                            killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEnd(80003016));
                            killer.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("오늘 획득 가능한 네오 스톤을 모두 획득햇습니다."));
                            killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "후후, 오늘은 이 정도면 충분할 것 같아. 고마워!"));
                            killer.cancelEffectFromBuffStat(SecondaryStat.EventSpecialSkill, 80003016);
                            killer.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(1291));
                            killer.dropMessage(11, "르네의 마법구슬이 비활성화되었습니다.");
                        }
                    }
                    if (killer.getKeyValue(100711, "today") >= 30000L) {
                        killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillEnd(80003016));
                        killer.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("오늘 획득 가능한 네오 스톤을 모두 획득햇습니다."));
                        killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "후후, 오늘은 이 정도면 충분할 것 같아. 고마워!"));
                        killer.cancelEffectFromBuffStat(SecondaryStat.EventSpecialSkill, 80003016);
                        killer.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(1291));
                        killer.dropMessage(11, "르네의 마법구슬이 비활성화되었습니다.");
                    }
                }
            }
        }
        if (killer.getBuffedValue(80003025)) {
            if (getId() == 9833905) {
                killer.setKeyValue(100722, "today", "" + (killer.getKeyValue(100722, "today") + 20L));
                killer.AddStarDustPoint(1, 2000, getTruePosition());
                if (Randomizer.isSuccess(5)) {
                    killer.getClient().getSession().writeAndFlush(SLFCGPacket.EventSkillOn(getId()));
                    killer.AddStarDustPoint(1, 2000, getTruePosition());
                    killer.setKeyValue(100722, "today", "" + (killer.getKeyValue(100722, "today") + 20L));
                    Timer.ShowTimer.getInstance().schedule(() -> {
                        if (killer != null) {
                            if (Randomizer.isSuccess(50)) {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 700, "와! 눈꽃 순록들이 달려오고 있어!"));
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "눈꽃 순록들이 달리는 모습!\r\n너무 아름다웠어!"));
                            } else {
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 700, "종 소리가 울려 퍼지는 지금!\r\n눈꽃 순록 러쉬!"));
                                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 2000, "고마워.\r\n눈꽃 순록들도 즐거워 하고 있어."));
                            }
                        }
                    }, 1500L);
                }
            }
            if (killer.getKeyValue(100722, "today") >= 2000L) {
                killer.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("오늘 획득 가능한 네오 스톤을 모두 획득햇습니다."));
                killer.getClient().getSession().writeAndFlush(CField.enforceMsgNPC(9062453, 1300, "후후, 오늘은 이 정도면 충분할 것 같아. 고마워!"));
                killer.cancelEffectFromBuffStat(SecondaryStat.EventSpecialSkill, 80003025);
                killer.dropMessage(5, "르네와 마법의 종 스킬이 비활성화되었습니다.");
            }
        }
        if (!FieldLimitType.Event.check(this.map.getFieldLimit()) && !this.map.isEliteField() && !this.map.isElitebossmap() && !this.map.isElitebossrewardmap() && !this.map.isElitechmpfinal() && !GameConstants.isContentsMap(getMap().getId()) && !GameConstants.보스맵(getMap().getId()) && !GameConstants.사냥컨텐츠맵(getMap().getId()) && !GameConstants.튜토리얼(getMap().getId()) && !GameConstants.로미오줄리엣(getMap().getId()) && !GameConstants.피라미드(getMap().getId()) && ((getStats().getLevel() >= killer.getLevel() - 20 && getStats().getLevel() <= killer.getLevel() + 20) || killer.isGM())) {
            this.map.setCustomInfo(9930005, this.map.getCustomValue0(9930005) + 1, 0);
            if (this.map.getCustomValue0(9930005) >= 5000) {
                this.map.setCustomInfo(9930005, 0, 0);
            }
            if (this.map.getCustomValue0(9930005) >= 3000) {
                if ((Randomizer.nextInt(10000) < 3 && this.map.getPoloFrittoPortal() == null) || killer.isGM()) {
                    MapleMap target = killer.getClient().getChannelServer().getMapFactory().getMap(993000000);
                    MapleMap target2 = killer.getClient().getChannelServer().getMapFactory().getMap(993000100);
                    if (target.characterSize() == 0 || target2.characterSize() == 0) {
                        MapleRandomPortal portal = new MapleRandomPortal(2, getTruePosition(), this.map.getId(), killer.getId(), Randomizer.nextBoolean());
                        this.map.spawnRandomPortal(portal);
                    } else {
                        MapleRandomPortal portal = new MapleRandomPortal(2, getTruePosition(), this.map.getId(), killer.getId(), false);
                        this.map.spawnRandomPortal(portal);
                    }
                }
                if (Randomizer.nextInt(10000) < 2 && this.map.getFireWolfPortal() == null) {
                    MapleRandomPortal portal = new MapleRandomPortal(3, getTruePosition(), this.map.getId(), killer.getId(), false);
                    portal.setPortalType(3);
                    this.map.spawnRandomPortal(portal);
                }
            }
        }
        if (killer.getMapId() == 993000500) {
            MapleMap target = ChannelServer.getInstance(killer.getClient().getChannel()).getMapFactory().getMap(993000600);
            for (MapleCharacter chr : killer.getMap().getAllChracater()) {
                chr.changeMap(target, target.getPortal(0));
                chr.setFWolfKiller(true);
                if (chr.getQuestStatus(16407) == 1) {
                    chr.forceCompleteQuest(16407);
                }
            }
        }
        if (getSeperateSoul() <= 0) {
            if (!GameConstants.isContentsMap(getMap().getId())) {
                spawnRevives(getMap());
            }
            if (this.eventInstance != null) {
                this.eventInstance.unregisterMonster(this);
                this.eventInstance = null;
            }
            this.hp = 0L;
            MapleMonster oldSponge = getSponge();
            this.sponge = new WeakReference<>(null);
            if (oldSponge != null && oldSponge.isAlive()) {
                boolean set = true;
                for (MapleMapObject mon : this.map.getAllMonstersThreadsafe()) {
                    MapleMonster mons = (MapleMonster) mon;
                    if (mons.isAlive() && mons.getObjectId() != oldSponge.getObjectId() && mons.getStats().getLevel() > 1 && mons.getObjectId() != getObjectId() && (mons.getSponge() == oldSponge || mons.getLinkOid() == oldSponge.getObjectId())) {
                        set = false;
                        break;
                    }
                }
                int dealy = 0;
                if (set) {
                    this.map.killMonster(oldSponge, killer, true, false, (byte) 1);
                }
            }
            this.reflectpack = null;
            this.nodepack = null;
            cancelDropItem();
        }
        int v1 = this.highestDamageChar;
        this.highestDamageChar = 0;
        return v1;
    }

    public final void spawnRevives(MapleMap map) { // 두마리 머쉬룸
        List<Integer> toSpawn = this.stats.getRevives();
        if (toSpawn == null || this.getLinkCID() > 0) {
            return;
        }
        AbstractLoadedMapleLife spongy = null;
        switch (this.getId()) {
            case 6160003:
            case 8820002:
            case 8820003:
            case 8820004:
            case 8820005:
            case 8820006:
            case 8820102:
            case 8820103:
            case 8820104:
            case 8820105:
            case 8820106:
            case 8840000:
            case 8850011: {
                break;
            }
            case 8810026:
            case 8810130:
            case 8820008:
            case 8820009:
            case 8820010:
            case 8820011:
            case 8820012:
            case 8820013:
            case 8820108:
            case 8820109:
            case 8820110:
            case 8820111:
            case 8820112:
            case 8820113: {
                ArrayList<MapleMonster> mobs = new ArrayList<MapleMonster>();
                block8:
                for (int i : toSpawn) {
                    MapleMonster mob = MapleLifeFactory.getMonster(i);
                    mob.setPosition(this.getTruePosition());
                    if (this.eventInstance != null) {
                        this.eventInstance.registerMonster(mob);
                    }
                    if (this.dropsDisabled()) {
                        mob.disableDrops();
                    }
                    switch (mob.getId()) {
                        case 8810018:
                        case 8810122:
                        case 8820009:
                        case 8820010:
                        case 8820011:
                        case 8820012:
                        case 8820013:
                        case 8820014:
                        case 8820109:
                        case 8820110:
                        case 8820111:
                        case 8820112:
                        case 8820113:
                        case 8820114: {
                            spongy = mob;
                            continue block8;
                        }
                    }
                    mobs.add(mob);
                }
                if (spongy == null || map.getMonsterById(spongy.getId()) != null) {
                    break;
                }
                map.spawnMonster((MapleMonster) spongy, -2);
                for (MapleMonster i : mobs) {
                    if (i.getId() == 8820000 || i.getId() == 8820100) {
                        map.spawnMonsterDelay(i, -1, 4300);
                    } else {
                        int type = i.getId() >= 8810002 && i.getId() <= 8810009 || i.getId() >= 8810102 && i.getId() <= 8810109 ? -2 : -1;
                        map.spawnMonster(i, type);
                    }
                    i.setSponge((MapleMonster) spongy);
                }
                break;
            }
            case 8820014:
            case 8820114: {
                for (int i : toSpawn) {
                    MapleMonster mob = MapleLifeFactory.getMonster(i);
                    if (this.eventInstance != null) {
                        this.eventInstance.registerMonster(mob);
                    }
                    mob.setPosition(this.getTruePosition());
                    if (this.dropsDisabled()) {
                        mob.disableDrops();
                    }
                    if (mob.getId() == 8820001 || mob.getId() == 8820101) {
                        if (mob.getId() == 8820101) {
                            mob.setHp(12600000000L);
                            mob.getStats().setHp(12600000000L);
                            mob.setCustomInfo(mob.getId(), 3, 0);
                        }
                        map.spawnMonsterDelay(mob, -2, 2300);
                        continue;
                    }
                    map.spawnMonster(mob, -2);
                }
                break;
            }
            default: {
                for (int i : toSpawn) {
                    MapleMonster mob = MapleLifeFactory.getMonster(i);
                    if (this.eventInstance != null) {
                        this.eventInstance.registerMonster(mob);
                    }
                    mob.setPosition(this.getTruePosition());
                    if (this.dropsDisabled()) {
                        mob.disableDrops();
                    }
                    if (mob.getId() == 8220102 || mob.getId() == 8220104) {
                        if (map.getAllMonster().size() > 0) {
                            Iterator<MapleMonster> iterator = map.getAllMonster().iterator();
                            if (iterator.hasNext()) {
                                MapleMonster mons = iterator.next();
                                mob.setHp((long) ((double) (mons.getStats().getHp() * (long) (mob.getId() == 8220102 ? 30 : 50)) * mob.bonusHp()));
                            }
                        } else {
                            mob.setHp((long) ((double) (mob.getStats().getHp() * (long) (mob.getId() == 8220102 ? 30 : 50)) * mob.bonusHp()));
                        }
                    }
                    map.spawnRevives(mob, this.getObjectId());
                    if (mob.getId() != 8880316 && mob.getId() != 8880318) {
                        continue;
                    }
                    mob.setDeadTimeKillmob(3000);
                }
            }
        }
    }

    public final boolean isAlive() {
        return (this.hp > 0L);
    }

    public final void setCarnivalTeam(byte team) {
        this.carnivalTeam = team;
    }

    public final byte getCarnivalTeam() {
        return this.carnivalTeam;
    }

    public final MapleCharacter getController() {
        return this.controller.get();
    }

    public final void setController(MapleCharacter controller) {
        this.controller = new WeakReference<>(controller);
    }

    public final void switchController(MapleCharacter newController, boolean immediateAggro) {
        MapleCharacter controllers = getController();
        if (controllers == newController) {
            return;
        }
        if (controllers != null) {
            controllers.stopControllingMonster(this);
            controllers.getClient().getSession().writeAndFlush(MobPacket.stopControllingMonster(getObjectId()));
        }
        newController.controlMonster(this, immediateAggro);
        setController(newController);
        if (immediateAggro) {
            setControllerHasAggro(true);
        }
    }

    public final void addListener(MonsterListener listener) {
        this.listener = listener;
    }

    public final boolean isControllerHasAggro() {
        return this.controllerHasAggro;
    }

    public final void setControllerHasAggro(boolean controllerHasAggro) {
        this.controllerHasAggro = controllerHasAggro;
    }

    public final void sendSpawnData(MapleClient client) {
        if (!isAlive() || (this.owner >= 0 && this.owner != client.getPlayer().getId())) {
            return;
        }
        if (getOwner() == -1) {
            client.getSession().writeAndFlush(MobPacket.spawnMonster(this, (this.fake && this.linkCID <= 0) ? -4 : -1, 0));
        } else if (getOwner() == client.getPlayer().getId()) {
            client.getSession().writeAndFlush(MobPacket.spawnMonster(this, (this.fake && this.linkCID <= 0) ? -4 : -1, 0));
        }
        if (this.map != null && !this.stats.isEscort() && client.getPlayer() != null && client.getPlayer().getTruePosition().distanceSq(getTruePosition()) <= GameConstants.maxViewRangeSq_Half()) {
            this.map.updateMonsterController(this);
        }
    }

    public final void sendDestroyData(MapleClient client) {
        if (this.stats.isEscort() && getEventInstance() != null && this.lastNode >= 0) {
            this.map.resetShammos(client);
        } else {
            client.getSession().writeAndFlush(MobPacket.killMonster(getObjectId(), 0));
            if (getController() != null && client.getPlayer() != null && client.getPlayer().getId() == getController().getId()) {
                client.getPlayer().stopControllingMonster(this);
            }
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.stats.getName());
        sb.append("(");
        sb.append(getId());
        sb.append(") (Level ");
        sb.append(this.stats.getLevel());
        sb.append(") at (X");
        sb.append((getTruePosition()).x);
        sb.append("/ Y");
        sb.append((getTruePosition()).y);
        sb.append(") with ");
        sb.append(getHp());
        sb.append("/ ");
        sb.append(getMobMaxHp());
        sb.append("hp, ");
        sb.append(getMp());
        sb.append("/ ");
        sb.append(getMobMaxMp());
        sb.append(" mp, oid: ");
        sb.append(getObjectId());
        sb.append(" || Controller : ");
        MapleCharacter chr = this.controller.get();
        sb.append((chr != null) ? chr.getName() : "none");
        return sb.toString();
    }

    public final MapleMapObjectType getType() {
        return MapleMapObjectType.MONSTER;
    }

    public final EventInstanceManager getEventInstance() {
        return this.eventInstance;
    }

    public final void setEventInstance(EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public final ElementalEffectiveness getEffectiveness(Element e) {
        return this.stats.getEffectiveness(e);
    }

    public final void setTempEffectiveness(final Element e, long milli) {
        this.stats.setEffectiveness(e, ElementalEffectiveness.WEAK);
        Timer.EtcTimer.getInstance().schedule(new Runnable() {
            public void run() {
                MapleMonster.this.stats.removeEffectiveness(e);
            }
        }, milli);
    }

    public final boolean isBuffed(MonsterStatus status) {
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (skill != null
                    && skill.getLeft() == status) {
                return true;
            }
        }
        return false;
    }

    public final boolean isBuffed(int skillid) {
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (skill != null && ((MonsterStatusEffect) skill.getRight()).getSkill() == skillid) {
                return true;
            }
        }
        return false;
    }

    public final MonsterStatusEffect getBuff(MonsterStatus status) {
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (skill.getLeft() == status) {
                return skill.getRight();
            }
        }
        return null;
    }

    public final MonsterStatusEffect getBuff(int skillid) {
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (((MonsterStatusEffect) skill.getRight()).getSkill() == skillid) {
                return skill.getRight();
            }
        }
        return null;
    }

    public final void cancelSingleStatus(MonsterStatusEffect stat, int skillid) {
        if (stat == null || !isAlive()) {
            return;
        }
        List<Pair<MonsterStatus, MonsterStatusEffect>> cancelsf = new ArrayList<>();
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (((MonsterStatusEffect) skill.getRight()).getSkill() == skillid) {
                cancelsf.add(new Pair<>(skill.getLeft(), skill.getRight()));
                cancelStatus(cancelsf);
                break;
            }
        }
    }

    public final int getBurnedBuffSize(int skillid) {
        int size = 0;
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (((MonsterStatusEffect) skill.getRight()).getSkill() == skillid && skill.getLeft() == MonsterStatus.MS_Burned) {
                size++;
            }
        }
        return size;
    }

    public final int getBurnedBuffSize() {
        int size = 0;
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (skill.getLeft() == MonsterStatus.MS_Burned) {
                size++;
            }
        }
        return size;
    }

    public final void setFake(boolean fake) {
        this.fake = fake;
    }

    public final boolean isFake() {
        return this.fake;
    }

    public final MapleMap getMap() {
        return this.map;
    }

    public final List<MobSkill> getSkills() {
        return this.stats.getSkills();
    }

    public final boolean hasSkill(int skillId, int level) {
        return this.stats.hasSkill(skillId, level);
    }

    public final long getLastSkillUsed(int skillId, int skillLevel) {
        for (Map.Entry<MobSkill, Long> kvp : this.usedSkills.entrySet()) {
            if (kvp.getKey().getSkillId() != skillId || kvp.getKey().getSkillLevel() != skillLevel) {
                continue;
            }
            return kvp.getValue();
        }
        return 0L;
    }

    public final void setLastSkillUsed(MobSkill msi, long now, long cooltime) {
        if ((this.getId() == 8880101 || this.getId() == 8880111) && msi.getSkillId() == 170) {
            cooltime *= 2L;
        }
        this.usedSkills.put(msi, now + cooltime);
    }

    public final byte getNoSkills() {
        return this.stats.getNoSkills();
    }

    public final boolean isFirstAttack() {
        return this.stats.isFirstAttack();
    }

    public final int getBuffToGive() {
        return this.stats.getBuffToGive();
    }

    public void applyStatus(MapleClient c, List<Pair<MonsterStatus, MonsterStatusEffect>> datas, SecondaryStatEffect effect) {
        int igkey = 0;
        boolean bind = false, alreadybind = false;
        RemoveStati((MonsterStatus) null, (MonsterStatusEffect) null, false);
        Pair<MonsterStatus, MonsterStatusEffect> re = null;
        for (Pair<MonsterStatus, MonsterStatusEffect> data : datas) {
            Ignition ig = null;
            int maxSuperPos = (((MonsterStatusEffect) data.getRight()).getSkill() == 4120011 || ((MonsterStatusEffect) data.getRight()).getSkill() == 4220011 || ((MonsterStatusEffect) data.getRight()).getSkill() == 4340012) ? 3 : effect.getDotSuperpos();
            int dotSuperpos = 0;
            for (Ignition ign : getIgnitions()) {
                if (ign.getSkill() == ((MonsterStatusEffect) data.getRight()).getSkill()) {
                    dotSuperpos++;
                }
            }
            if (data.getLeft() == MonsterStatus.MS_Freeze) {
                re = data;
                bind = true;
                if (getBuff(MonsterStatus.MS_Freeze) != null && effect.getSourceId() != 100001283 && effect.getSourceId() != 101120110) {
                    alreadybind = true;
                    continue;
                }
            }
            CancelStatusAction action = new CancelStatusAction(this, data.getLeft(), data.getRight());
            ScheduledFuture<?> schedule = Timer.BuffTimer.getInstance().schedule(() -> action.run(), ((MonsterStatusEffect) data
                    .getRight()).getDuration());
            ((MonsterStatusEffect) data.getRight()).setSchedule(schedule);
            ((MonsterStatusEffect) data.getRight()).setChr(c.getPlayer());
            ((MonsterStatusEffect) data.getRight()).setStati(data.getLeft());
            if (isBuffed(data.getLeft())) {
                if (data.getLeft() == MonsterStatus.MS_Burned) {
                    if (dotSuperpos >= maxSuperPos) {
                        cancelStatus(data.getLeft(), data.getRight());
                    }
                } else {
                    cancelStatus(data.getLeft(), data.getRight());
                }
            }
            if (((MonsterStatus) data.getLeft()).isStacked()) {
                MonsterStatusEffect already = null;
                for (MonsterStatusEffect alreadyindie : getIndielist()) {
                    if (alreadyindie.getSkill() == ((MonsterStatusEffect) data.getRight()).getSkill()) {
                        already = alreadyindie;
                        break;
                    }
                }
                if (already != null) {
                    getIndielist().remove(already);
                }
                getIndielist().add(data.getRight());
            }
            if (data.getLeft() == MonsterStatus.MS_Burned) {
                long value = 0L;
                dotSuperpos = 0;
                for (Ignition ign : getIgnitions()) {
                    if (ign.getSkill() == ((MonsterStatusEffect) data.getRight()).getSkill()) {
                        dotSuperpos++;
                        value = ign.getDamage();
                        break;
                    }
                }
                if ((effect.getDotSuperpos() == 0 && dotSuperpos <= 0) || dotSuperpos < maxSuperPos) {
                    int interval = effect.getDotInterval();
                    int du = ((MonsterStatusEffect) data.getRight()).getDuration();
                    if (c.getPlayer().getSkillLevel(2110000) > 0) {
                        SecondaryStatEffect extremeMagic = SkillFactory.getSkill(2110000).getEffect(c.getPlayer().getSkillLevel(2110000));
                        ((MonsterStatusEffect) data.getRight()).setDuration(((MonsterStatusEffect) data.getRight()).getDuration() * (100 + extremeMagic.getX()) / 100);
                    }
                    if (effect.getDotInterval() > 0 && ((MonsterStatusEffect) data.getRight()).getValue() > 0L) {
                        ((MonsterStatusEffect) data.getRight()).setLastPoisonTime(System.currentTimeMillis());
                        ((MonsterStatusEffect) data.getRight()).setInterval(interval);
                    }
                    ig = new Ignition(c.getPlayer().getId(), ((MonsterStatusEffect) data.getRight()).getSkill(), (value > 0L) ? value : ((MonsterStatusEffect) data.getRight()).getValue(), interval, du);
                    ((MonsterStatusEffect) data.getRight()).setKey(ig.getIgnitionKey());
                    igkey = ig.getIgnitionKey();
                }
            } else if (data.getLeft() == MonsterStatus.MS_SeperateSoulP && getStats().getCategory() != 1) {
                MapleMonster mob = MapleLifeFactory.getMonster(getId());
                mob.setSeperateSoul(getObjectId());
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, getPosition());
                mob.applyStatus(c, MonsterStatus.MS_SeperateSoulC, data.getRight(), (int) ((MonsterStatusEffect) data.getRight()).getValue() + 2000, effect);
                mob.applyStatus(c, MonsterStatus.MS_Stun, data.getRight(), (int) ((MonsterStatusEffect) data.getRight()).getValue() + 2000, effect);
                if (effect.getDuration() > 0) {
                    Timer.MobTimer.getInstance().schedule(() -> {
                        try {
                            mob.getMap().killMonster(mob, c.getPlayer(), false, false, (byte) 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, effect.getDuration());
                } else {
                    mob.getMap().killMonster(mob, c.getPlayer(), false, false, (byte) 0);
                }
            }
            if (getStats().isBoss() && data.getLeft() == MonsterStatus.MS_Stun) {
                re = data;
                setCustomInfo(1, 1, 0);
                continue;
            }
            if ((!isResist() || effect.getSourceId() == 100001283 || effect.getSourceId() == 101120110) && data.getLeft() == MonsterStatus.MS_Freeze) {
                this.stati.add(new Pair<>(data.getLeft(), data.getRight()));
                continue;
            }
            if (data.getLeft() != MonsterStatus.MS_Freeze) {
                this.stati.add(new Pair<>(data.getLeft(), data.getRight()));
                if (ig != null) {
                    getIgnitions().add(ig);
                }
            }
        }
        if (bind && effect.getSourceId() != 100001283 && effect.getSourceId() != 101120110) {
            if (alreadybind || getStats().getIgnoreMovable() > 0) {
                if (getStats().getIgnoreMovable() > 0) {
                    c.send(CWvsContext.serverNotice(5, "", getStats().getIgnoreMoveableMsg()));
                }
                datas.remove(re);
            } else if (!isResist()) {
                setResist(System.currentTimeMillis());
            } else {
                datas.remove(re);
                c.getSession().writeAndFlush(MobPacket.monsterResist(this, c.getPlayer(), (int) (90L - (System.currentTimeMillis() - this.lastBindTime) / 1000L), effect.getSourceId()));
                if (effect.getSourceId() == 64121001) {
                    c.getPlayer().cancelEffect(effect);
                    effect.applyTo(c.getPlayer(), false, 5000);
                }
            }
        }
        if (re != null && getCustomValue0(1) == 1L) {
            removeCustomInfo(1);
            datas.remove(re);
        }
        if (!datas.isEmpty()) {
            this.map.broadcastMessage(MobPacket.applyMonsterStatus(this, datas, false, effect));
            if (effect.getSourceId() == 37121004) {
                this.map.broadcastMessage(CField.RebolvingBunk(c.getPlayer().getId(), getObjectId(), getId(), getPosition()));
            }
        }
    }

    public void applyStatus(MapleClient c, MonsterStatus status, MonsterStatusEffect effect, int value, SecondaryStatEffect eff) {
        if (isBuffed(MonsterStatus.MS_PCounter) || isBuffed(MonsterStatus.MS_MCounter) || isBuffed(MonsterStatus.MS_PImmune) || isBuffed(MonsterStatus.MS_MImmune)) {
            return;
        }
        if (effect.getSkill() != 80001227 && (status == MonsterStatus.MS_Stun || status == MonsterStatus.MS_Seal) && getStats().isBoss() && !isBuffed(MonsterStatus.MS_SeperateSoulC)) {
            return;
        }
        if (c == null || effect == null || eff == null) {
            return;
        }
        int igkey = 0;
        int dotSuperpos = 0;
        int maxSuperPos = (effect.getSkill() == 4120011 || effect.getSkill() == 4220011 || effect.getSkill() == 4340012) ? 3 : eff.getDotSuperpos();
        RemoveStati((MonsterStatus) null, (MonsterStatusEffect) null, false);
        if (status == MonsterStatus.MS_Burned) {
            long value3 = 0L;
            for (Ignition ig : getIgnitions()) {
                if (ig.getSkill() == effect.getSkill()) {
                    dotSuperpos++;
                    value3 = ig.getDamage();
                }
            }
            if (dotSuperpos < eff.getDotSuperpos()) {
                if (eff.getDotInterval() > 0 && effect.getValue() > 0L) {
                    effect.setLastPoisonTime(System.currentTimeMillis());
                    effect.setInterval(eff.getDotInterval());
                }
                Ignition ig = new Ignition(c.getPlayer().getId(), effect.getSkill(), (value3 > 0L) ? value3 : effect.getValue(), eff.getDotInterval(), effect.getDuration());
                getIgnitions().add(ig);
                effect.setKey(ig.getIgnitionKey());
                igkey = ig.getIgnitionKey();
            }
        }
        if (status == MonsterStatus.MS_SeperateSoulP && getStats().getCategory() != 1) {
            MapleMonster mob = MapleLifeFactory.getMonster(getId());
            c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, getPosition());
            mob.setSeperateSoul(getObjectId());
            mob.applyStatus(c, MonsterStatus.MS_SeperateSoulC, effect, value, eff);
            mob.applyStatus(c, MonsterStatus.MS_Stun, effect, value, eff);
            if (effect.getDuration() > 0) {
                try {
                    Timer.MobTimer.getInstance().schedule(() -> {
                        try {
                            mob.getMap().killMonster(mob, c.getPlayer(), false, false, (byte) 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, effect.getDuration());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mob.getMap().killMonster(mob, c.getPlayer(), false, false, (byte) 0);
            }
        }
        if (isBuffed(status)) {
            if (status == MonsterStatus.MS_Burned) {
                if (dotSuperpos >= maxSuperPos) {
                    cancelStatus(status, effect);
                }
            } else {
                cancelStatus(status, effect);
            }
        }
        if (status.isStacked()) {
            MonsterStatusEffect already = null;
            for (MonsterStatusEffect alreadyindie : getIndielist()) {
                if (alreadyindie.getSkill() == effect.getSkill()) {
                    already = alreadyindie;
                    break;
                }
            }
            if (already != null) {
                getIndielist().remove(already);
            }
            getIndielist().add(effect);
        }
        CancelStatusAction action = new CancelStatusAction(this, status, effect);
        ScheduledFuture<?> schedule = Timer.BuffTimer.getInstance().schedule(() -> action.run(), effect
                .getDuration());
        effect.setSchedule(schedule);
        effect.setValue(value);
        effect.setStati(status);
        effect.setChr(c.getPlayer());
        effect.setCid(c.getPlayer().getId());
        this.stati.add(new Pair<>(status, effect));
        List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<>();
        applys.add(new Pair<>(status, effect));
        this.map.broadcastMessage(MobPacket.applyMonsterStatus(this, applys, false, eff));
        if (effect.getDuration() < 0) {
            cancelStatus(status, effect);
        }
    }

    public int getSeperateSoul() {
        return this.seperateSoul;
    }

    private void setSeperateSoul(int id) {
        this.seperateSoul = id;
    }

    public final void cancelStatus(List<Pair<MonsterStatus, MonsterStatusEffect>> cancels) {
        MapleCharacter con = getController();
        List<Pair<MonsterStatus, MonsterStatusEffect>> cancelsf = new ArrayList<>();
        List<MonsterStatusEffect> removeindie = new ArrayList<>();
        for (Pair<MonsterStatus, MonsterStatusEffect> cancel : cancels) {
            cancelsf.add(new Pair<>(cancel.getLeft(), cancel.getRight()));
            RemoveStati(cancel.getLeft(), cancel.getRight(), true);
            ScheduledFuture<?> schedule = ((MonsterStatusEffect) cancel.getRight()).getSchedule();
            if (schedule != null && !schedule.isCancelled()) {
                schedule.cancel(true);
            }
            if (((MonsterStatus) cancel.getLeft()).isStacked()) {
                removeindie.add(cancel.getRight());
                getIndielist().remove(cancel.getRight());
            }
            if (((MonsterStatusEffect) cancel.getRight()).getSkill() == 12101024 || ((MonsterStatusEffect) cancel.getRight()).getSkill() == 12121002) {
                this.map.broadcastMessage(CField.ignitionBomb(12100029, getObjectId(), getTruePosition()));
                continue;
            }
            if (((MonsterStatusEffect) cancel.getRight()).getSkill() == 11121004) {
                this.map.broadcastMessage(CField.ignitionBomb(11121013, getObjectId(), getTruePosition()));
            }
        }
        if (!cancelsf.isEmpty()) {
            this.map.broadcastMessage(MobPacket.cancelMonsterStatus(this, cancelsf, removeindie), getTruePosition());
            this.map.broadcastMessage(MobPacket.applyMonsterStatus(this, this.stati, false, null));
        }
    }

    public final void cancelStatus(MonsterStatus stat, MonsterStatusEffect effect) {
        cancelStatus(stat, effect, false);
    }

    public final void cancelStatus(MonsterStatus stat, MonsterStatusEffect effect, boolean autocancel) {
        MonsterStatusEffect mse = null;
        for (Pair<MonsterStatus, MonsterStatusEffect> ms : this.stati) {
            if (ms != null && effect != null
                    && ms.getLeft() == stat && ((MonsterStatusEffect) ms.getRight()).getSkill() == effect.getSkill()) {
                mse = ms.getRight();
                break;
            }
        }
        if (stat == null || this.stati == null || !isBuffed(stat)) {
            return;
        }
        if (mse == null || !isAlive()) {
            return;
        }
        MapleCharacter con = getController();
        List<Pair<MonsterStatus, MonsterStatusEffect>> cancels = new ArrayList<>();
        cancels.add(new Pair<>(stat, mse));
        for (Pair<MonsterStatus, MonsterStatusEffect> list : this.stati) {
            if (((MonsterStatusEffect) list.getRight()).getSkill() == mse.getSkill()) {
                cancels.add(new Pair<>(list.getLeft(), list.getRight()));
            }
        }
        ScheduledFuture<?> schedule = mse.getSchedule();
        if (schedule != null && !schedule.isCancelled()) {
            schedule.cancel(false);
        }
        List<MonsterStatusEffect> removeindie = new ArrayList<>();
        if (stat.isStacked()) {
            removeindie.add(mse);
            getIndielist().remove(mse);
        }
        if (removeindie.size() < 2) {
            removeindie.clear();
        }
        RemoveStati(stat, mse, true);
        if (!cancels.isEmpty()) {
            if (con != null) {
                this.map.broadcastMessage(con, MobPacket.cancelMonsterStatus(this, cancels, removeindie), getTruePosition());
                con.getClient().getSession().writeAndFlush(MobPacket.cancelMonsterStatus(this, cancels, removeindie));
            } else {
                this.map.broadcastMessage(MobPacket.cancelMonsterStatus(this, cancels), getTruePosition());
            }
            if (autocancel && mse.getSkill() == 241 && MonsterStatus.MS_PopulatusTimer.getFlag() == stat.getFlag()) {
                if (getId() == 8500021 || getId() == 8500011 || getId() == 8500001) {
                    MobSkill ms = MobSkillFactory.getMobSkill(241, (getHPPercent() >= 50) ? 2 : 1);
                    setLastSkillUsed(ms, System.currentTimeMillis(), ms.getInterval() + 20000L);
                    for (MapleCharacter chr : getMap().getAllChracater()) {
                        if (chr.hasDisease(SecondaryStat.PapulCuss) && chr.getSkillCustomValue(241) != null) {
                            chr.cancelDisease(SecondaryStat.PapulCuss);
                            int du = chr.getSkillCustomTime(241).intValue();
                            if (du > 30000) {
                                du = 30000;
                            }
                            ms.setDuration(du);
                            chr.giveDebuff((getHPPercent() >= 50) ? SecondaryStat.Seal : SecondaryStat.Stun, ms);
                        }
                    }
                }
            }
            if (con != null) {
                if (mse.getSkill() == 12101024 || mse.getSkill() == 12121002) {
                    this.map.broadcastMessage(CField.ignitionBomb(12100029, getObjectId(), getTruePosition()));
                    this.map.broadcastMessage(CField.EffectPacket.showEffect(con, 0, 12100029, 10, 0, 0, (byte) 0, true, getTruePosition(), null, null));
                } else if (mse.getSkill() == 11121004) {
                    this.map.broadcastMessage(CField.ignitionBomb(11121013, getObjectId(), getTruePosition()));
                    this.map.broadcastMessage(con, CField.EffectPacket.showEffect(con, 0, 11121013, 10, 0, 0, (byte) 0, true, getTruePosition(), null, null), false);
                    con.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(con, 0, 11121013, 10, 0, 0, (byte) 0, false, getTruePosition(), null, null));
                }
            }
        }
    }

    public final void cancelSingleStatus(MonsterStatusEffect stat) {
        if (stat == null || !isAlive()) {
            return;
        }
        cancelStatus(stat.getStati(), stat);
    }

    public final void dispels() {
        for (Pair<MonsterStatus, MonsterStatusEffect> stat : this.stati) {
            switch ((MonsterStatus) stat.getLeft()) {
                case MS_Pad:
                case MS_Pdr:
                case MS_Mad:
                case MS_Mdr:
                case MS_Acc:
                case MS_Eva:
                case MS_Speed:
                case MS_Powerup:
                case MS_Magicup:
                case MS_PGuardup:
                case MS_MGuardup:
                case MS_PImmune:
                case MS_MImmune:
                case MS_Hardskin:
                case MS_PowerImmune:
                    if (((MonsterStatusEffect) stat.getRight()).getValue() > 0L) {
                        cancelStatus(stat.getLeft(), stat.getRight());
                    }
            }
        }
    }

    private static class AttackingMapleCharacter {

        private MapleCharacter attacker;

        private long lastAttackTime;

        public AttackingMapleCharacter(MapleCharacter attacker, long lastAttackTime) {
            this.attacker = attacker;
            this.lastAttackTime = lastAttackTime;
        }

        public final long getLastAttackTime() {
            return this.lastAttackTime;
        }

        public final void setLastAttackTime(long lastAttackTime) {
            this.lastAttackTime = lastAttackTime;
        }

        public final MapleCharacter getAttacker() {
            return this.attacker;
        }
    }

    private final class SingleAttackerEntry implements AttackerEntry {

        private long damage = 0L;

        private int chrid;

        private long lastAttackTime;

        public SingleAttackerEntry(MapleCharacter from) {
            this.chrid = from.getId();
        }

        public void addDamage(MapleCharacter from, long damage, boolean updateAttackTime) {
            if (this.chrid == from.getId()) {
                this.damage += damage;
                if (updateAttackTime) {
                    this.lastAttackTime = System.currentTimeMillis();
                }
            }
        }

        public final List<MapleMonster.AttackingMapleCharacter> getAttackers() {
            MapleCharacter chr = MapleMonster.this.map.getCharacterById(this.chrid);
            if (chr != null) {
                return Collections.singletonList(new MapleMonster.AttackingMapleCharacter(chr, this.lastAttackTime));
            }
            return Collections.emptyList();
        }

        public boolean contains(MapleCharacter chr) {
            return (this.chrid == chr.getId());
        }

        public long getDamage() {
            return this.damage;
        }

        public void killedMob(MapleMap map, long baseExp, boolean mostDamage, int lastSkill) {
            MapleCharacter chr = map.getCharacterById(this.chrid);
            if (chr != null && chr.isAlive()) {
                MapleMonster.this.giveExpToCharacter(chr, baseExp, mostDamage, 1, (byte) 0, (byte) 0, (byte) 0, lastSkill);
            }
        }

        public int hashCode() {
            return this.chrid;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            SingleAttackerEntry other = (SingleAttackerEntry) obj;
            return (this.chrid == other.chrid);
        }
    }

    private static final class ExpMap {

        public final long exp;

        public final byte ptysize;

        public final byte Class_Bonus_EXP;

        public final byte Premium_Bonus_EXP;

        public ExpMap(long exp, byte ptysize, byte Class_Bonus_EXP, byte Premium_Bonus_EXP) {
            this.exp = exp;
            this.ptysize = ptysize;
            this.Class_Bonus_EXP = Class_Bonus_EXP;
            this.Premium_Bonus_EXP = Premium_Bonus_EXP;
        }
    }

    private static final class OnePartyAttacker {

        public MapleParty lastKnownParty;

        public long damage;

        public long lastAttackTime;

        public OnePartyAttacker(MapleParty lastKnownParty, long damage) {
            this.lastKnownParty = lastKnownParty;
            this.damage = damage;
            this.lastAttackTime = System.currentTimeMillis();
        }
    }

    private class PartyAttackerEntry implements AttackerEntry {

        private long totDamage = 0L;

        private final Map<Integer, MapleMonster.OnePartyAttacker> attackers = new HashMap<>(6);

        private int partyid;

        public PartyAttackerEntry(int partyid) {
            this.partyid = partyid;
        }

        public List<MapleMonster.AttackingMapleCharacter> getAttackers() {
            List<MapleMonster.AttackingMapleCharacter> ret = new ArrayList<>(this.attackers.size());
            for (Map.Entry<Integer, MapleMonster.OnePartyAttacker> entry : this.attackers.entrySet()) {
                MapleCharacter chr = MapleMonster.this.map.getCharacterById(((Integer) entry.getKey()).intValue());
                if (chr != null) {
                    ret.add(new MapleMonster.AttackingMapleCharacter(chr, ((MapleMonster.OnePartyAttacker) entry.getValue()).lastAttackTime));
                }
            }
            return ret;
        }

        private final Map<MapleCharacter, MapleMonster.OnePartyAttacker> resolveAttackers() {
            Map<MapleCharacter, MapleMonster.OnePartyAttacker> ret = new HashMap<>(this.attackers.size());
            for (Map.Entry<Integer, MapleMonster.OnePartyAttacker> aentry : this.attackers.entrySet()) {
                MapleCharacter chr = MapleMonster.this.map.getCharacterById(((Integer) aentry.getKey()).intValue());
                if (chr != null) {
                    ret.put(chr, aentry.getValue());
                }
            }
            return ret;
        }

        public final boolean contains(MapleCharacter chr) {
            return this.attackers.containsKey(Integer.valueOf(chr.getId()));
        }

        public final long getDamage() {
            return this.totDamage;
        }

        public void addDamage(MapleCharacter from, long damage, boolean updateAttackTime) {
            MapleMonster.OnePartyAttacker oldPartyAttacker = this.attackers.get(Integer.valueOf(from.getId()));
            if (oldPartyAttacker != null) {
                oldPartyAttacker.damage += damage;
                oldPartyAttacker.lastKnownParty = from.getParty();
                if (updateAttackTime) {
                    oldPartyAttacker.lastAttackTime = System.currentTimeMillis();
                }
            } else {
                MapleMonster.OnePartyAttacker onePartyAttacker = new MapleMonster.OnePartyAttacker(from.getParty(), damage);
                this.attackers.put(Integer.valueOf(from.getId()), onePartyAttacker);
                if (!updateAttackTime) {
                    onePartyAttacker.lastAttackTime = 0L;
                }
            }
            this.totDamage += damage;
        }

        public final void killedMob(MapleMap map, long baseExp, boolean mostDamage, int lastSkill) {
            MapleCharacter highest = null;
            long highestDamage = 0L;
            long iexp = 0L;
            Map<MapleCharacter, MapleMonster.ExpMap> expMap = new HashMap<>(6);
            for (Map.Entry<MapleCharacter, MapleMonster.OnePartyAttacker> attacker : resolveAttackers().entrySet()) {
                MapleParty party = ((MapleMonster.OnePartyAttacker) attacker.getValue()).lastKnownParty;
                double addedPartyLevel = 0.0D;
                byte added_partyinc = 0;
                byte Class_Bonus_EXP = 0;
                byte Premium_Bonus_EXP = 0;
                List<MapleCharacter> expApplicable = new ArrayList<>();
                for (MaplePartyCharacter partychar : party.getMembers()) {
                    if (((MapleCharacter) attacker.getKey()).getLevel() - partychar.getLevel() <= 5 || MapleMonster.this.stats.getLevel() - partychar.getLevel() <= 5) {
                        MapleCharacter pchr = map.getCharacterById(partychar.getId());
                        if (pchr != null && pchr.isAlive()) {
                            boolean enable = true;
                            int[] linkMobs = {
                                9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161,
                                9010162, 9010163, 9010164, 9010165, 9010166, 9010167, 9010168, 9010169, 9010170, 9010171,
                                9010172, 9010173, 9010174, 9010175, 9010176, 9010177, 9010178, 9010179, 9010180, 9010181};
                            for (int linkMob : linkMobs) {
                                if (MapleMonster.this.getId() == linkMob && pchr.getId() != ((MapleCharacter) attacker.getKey()).getId()) {
                                    enable = false;
                                }
                            }
                            if (enable) {
                                expApplicable.add(pchr);
                                addedPartyLevel += pchr.getLevel();
                                if ((pchr.getStat()).equippedWelcomeBackRing && Premium_Bonus_EXP == 0) {
                                    Premium_Bonus_EXP = 80;
                                }
                                if ((pchr.getStat()).hasPartyBonus && added_partyinc < 4 && map.getPartyBonusRate() <= 0) {
                                    added_partyinc = (byte) (added_partyinc + 1);
                                }
                            }
                        }
                    }
                }
                long iDamage = ((MapleMonster.OnePartyAttacker) attacker.getValue()).damage;
                if (iDamage > highestDamage) {
                    highest = attacker.getKey();
                    highestDamage = iDamage;
                }
                double innerBaseExp = baseExp * iDamage / this.totDamage;
                if (expApplicable.size() <= 1) {
                    Class_Bonus_EXP = 0;
                }
                for (MapleCharacter expReceiver : expApplicable) {
                    iexp = (expMap.get(expReceiver) == null) ? 0L : ((MapleMonster.ExpMap) expMap.get(expReceiver)).exp;
                    double levelMod = expReceiver.getLevel() / addedPartyLevel * 0.4D;
                    iexp += (int) Math.round((((((MapleCharacter) attacker.getKey()).getId() == expReceiver.getId()) ? 0.6D : 0.0D) + levelMod) * innerBaseExp);
                    expMap.put(expReceiver, new MapleMonster.ExpMap(iexp, (byte) (expApplicable.size() + added_partyinc), Class_Bonus_EXP, Premium_Bonus_EXP));
                }
            }
            for (Map.Entry<MapleCharacter, MapleMonster.ExpMap> expReceiver : expMap.entrySet()) {
                MapleMonster.ExpMap expmap = expReceiver.getValue();
                MapleMonster.this.giveExpToCharacter(expReceiver.getKey(), expmap.exp, mostDamage ? ((expReceiver.getKey() == highest)) : false, expMap.size(), expmap.ptysize, expmap.Class_Bonus_EXP, expmap.Premium_Bonus_EXP, lastSkill);
            }
        }

        public final int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + this.partyid;
            return result;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            PartyAttackerEntry other = (PartyAttackerEntry) obj;
            if (this.partyid != other.partyid) {
                return false;
            }
            return true;
        }
    }

    public int getLinkOid() {
        return this.linkoid;
    }

    public void setLinkOid(int lo) {
        this.linkoid = lo;
    }

    public final List<Pair<MonsterStatus, MonsterStatusEffect>> getStati() {
        return this.stati;
    }

    public final int getStolen() {
        return this.stolen;
    }

    public final void setStolen(int s) {
        this.stolen = s;
    }

    public final void setLastNode(int lastNode) {
        this.lastNode = lastNode;
    }

    public final int getLastNode() {
        return this.lastNode;
    }

    public final void cancelDropItem() {
        this.lastDropTime = 0L;
    }

    public final void startDropItemSchedule() {
        cancelDropItem();
        if (this.stats.getDropItemPeriod() <= 0 || !isAlive()) {
            return;
        }
        this.shouldDropItem = false;
        this.lastDropTime = System.currentTimeMillis();
    }

    public boolean shouldDrop(long now) {
        return (this.lastDropTime > 0L && this.lastDropTime + (this.stats.getDropItemPeriod() * 1000) < now);
    }

    public void doDropItem(long now) {
        int itemId;
        switch (getId()) {
            case 9300061:
                itemId = 4001101;
                break;
            default:
                cancelDropItem();
                return;
        }
        if (isAlive() && this.map != null) {
            if (this.shouldDropItem) {
                this.map.spawnAutoDrop(itemId, getTruePosition());
            } else {
                this.shouldDropItem = true;
            }
        }
        this.lastDropTime = now;
    }

    public byte[] getNodePacket() {
        return this.nodepack;
    }

    public void setNodePacket(byte[] np) {
        this.nodepack = np;
    }

    public void registerKill(long next) {
        this.nextKill = System.currentTimeMillis() + next;
    }

    public boolean shouldKill(long now) {
        return (this.nextKill > 0L && now > this.nextKill);
    }

    public int getLinkCID() {
        return this.linkCID;
    }

    public void setLinkCID(int lc) {
        this.linkCID = lc;
        if (lc > 0);
    }

    public void applyMonsterBuff(MapleMap map, List<Pair<MonsterStatus, MonsterStatusEffect>> stats, MobSkill mobSkill) {
        if (isBuffed(MonsterStatus.MS_PCounter) || isBuffed(MonsterStatus.MS_MCounter) || isBuffed(MonsterStatus.MS_PImmune) || isBuffed(MonsterStatus.MS_MImmune)) {
            return;
        }
        for (Pair<MonsterStatus, MonsterStatusEffect> e : stats) {
            ((MonsterStatusEffect) e.getRight()).setLevel(mobSkill.getSkillLevel());
            int time = 0;
            if (((MonsterStatus) e.getLeft()).getFlag() == MonsterStatus.MS_ExchangeAttack.getFlag() || ((MonsterStatus) e.getLeft()).getFlag() == MonsterStatus.MS_PopulatusTimer.getFlag()) {
                time = 60000;
            }
            CancelStatusAction action = new CancelStatusAction(this, e.getLeft(), e.getRight());
            ScheduledFuture<?> schedule = Timer.BuffTimer.getInstance().schedule(() -> action.run(), (time > 0) ? time : ((MonsterStatusEffect) e
                    .getRight()).getDuration());
            ((MonsterStatusEffect) e.getRight()).setMobskill(true);
            if (isBuffed(e.getLeft())) {
                cancelStatus(e.getLeft(), e.getRight());
            }
            ((MonsterStatusEffect) e.getRight()).setSchedule(schedule);
            this.stati.add(new Pair<>(e.getLeft(), e.getRight()));
        }
        map.broadcastMessage(MobPacket.applyMonsterStatus(this, stats, true, null));
        if (mobSkill.getDuration() < 0L) {
            cancelStatus(stats);
        }
    }

    public int getNextSkill() {
        return this.nextSkill;
    }

    public void setNextSkill(int nextSkill) {
        this.nextSkill = nextSkill;
    }

    public int getNextSkillLvl() {
        return this.nextSkillLvl;
    }

    public void setNextSkillLvl(int nextSkillLvl) {
        this.nextSkillLvl = nextSkillLvl;
    }

    public boolean isResist() {
        return (System.currentTimeMillis() - this.lastBindTime < 90000L);
    }

    public long getResist() {
        return this.lastBindTime;
    }

    public void setResist(long time) {
        this.lastBindTime = time;
    }

    public int getAirFrame() {
        return this.airFrame;
    }

    public void setAirFrame(int airFrame) {
        this.airFrame = airFrame;
    }

    public long getSpawnTime() {
        return this.spawnTime;
    }

    public void setSpawnTime(long spawnTime) {
        this.spawnTime = spawnTime;
    }

    public byte getPhase() {
        return this.phase;
    }

    public void setPhase(byte phase) {
        this.phase = phase;
    }

    public int getFreezingOverlap() {
        return this.freezingOverlap;
    }

    public void setFreezingOverlap(int freezingOverlap) {
        this.freezingOverlap = freezingOverlap;
    }

    public boolean isMobGroup() {
        return this.isMobGroup;
    }

    public void setMobGroup(boolean isMobGroup) {
        this.isMobGroup = isMobGroup;
    }

    public List<Integer> getSpawnList() {
        return this.spawnList;
    }

    public void setSpawnList(List<Integer> spawnList) {
        this.spawnList = spawnList;
    }

    public boolean isSkillForbid() {
        return this.isSkillForbid;
    }

    public void setSkillForbid(boolean isSkillForbid) {
        this.isSkillForbid = isSkillForbid;
    }

    public List<Integer> getWillHplist() {
        return this.willHplist;
    }

    public void setWillHplist(List<Integer> willHplist) {
        this.willHplist = willHplist;
    }

    public boolean isUseSpecialSkill() {
        return this.useSpecialSkill;
    }

    public void setUseSpecialSkill(boolean useSpecialSkill) {
        this.useSpecialSkill = useSpecialSkill;
    }

    public List<MapleEnergySphere> getSpheres() {
        return this.spheres;
    }

    public void setSpheres(List<MapleEnergySphere> spheres) {
        this.spheres = spheres;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getEliteGrade() {
        return this.eliteGrade;
    }

    public void setEliteGrade(int eliteGrade) {
        this.eliteGrade = eliteGrade;
    }

    public List<Pair<Integer, Integer>> getEliteGradeInfo() {
        return this.eliteGradeInfo;
    }

    public void setEliteGradeInfo(List<Pair<Integer, Integer>> eliteGradeInfo) {
        this.eliteGradeInfo = eliteGradeInfo;
    }

    public int getEliteType() {
        return this.eliteType;
    }

    public void setEliteType(int eliteType) {
        this.eliteType = eliteType;
    }

    public int getCurseBound() {
        return this.curseBound;
    }

    public void setCurseBound(int curseBound) {
        this.curseBound = curseBound;
    }

    public byte getBigbangCount() {
        return this.bigbangCount;
    }

    public void setBigbangCount(byte bigbangCount) {
        this.bigbangCount = bigbangCount;
    }

    public int getSpiritGate() {
        return this.spiritGate;
    }

    public void setSpiritGate(int spiritGate) {
        this.spiritGate = spiritGate;
    }

    public List<Ignition> getIgnitions() {
        return this.ignitions;
    }

    public void setIgnitions(List<Ignition> ignitions) {
        this.ignitions = ignitions;
    }

    public ScheduledFuture<?> getSchedule() {
        return this.schedule;
    }

    public void setSchedule(ScheduledFuture<?> schedule) {
        this.schedule = schedule;
    }

    public long getBarrier() {
        return this.barrier;
    }

    public void setBarrier(long barrier) {
        this.barrier = barrier;
    }

    public int getAnotherByte() {
        return this.anotherByte;
    }

    public void setAnotherByte(int anotherByte) {
        this.anotherByte = anotherByte;
    }

    public boolean isDemianChangePhase() {
        return this.demianChangePhase;
    }

    public void setDemianChangePhase(boolean demianChangePhase) {
        this.demianChangePhase = demianChangePhase;
    }

    public long getLastCriticalBindTime() {
        return this.lastCriticalBindTime;
    }

    public void setLastCriticalBindTime(long lastCriticalBindTime) {
        this.lastCriticalBindTime = lastCriticalBindTime;
    }

    public long getLastSpecialAttackTime() {
        return this.lastSpecialAttackTime;
    }

    public void setLastSpecialAttackTime(long lastSpecialAttackTime) {
        this.lastSpecialAttackTime = lastSpecialAttackTime;
    }

    public boolean isExtreme() {
        return this.extreme;
    }

    public void setExtreme(boolean extreme) {
        this.extreme = extreme;
    }

    public long getLastSeedCountedTime() {
        return this.lastSeedCountedTime;
    }

    public void setLastSeedCountedTime(long lastSeedCountedTime) {
        this.lastSeedCountedTime = lastSeedCountedTime;
    }

    public Long getCustomValue(int skillid) {
        if (this.customInfo.containsKey(Integer.valueOf(skillid))) {
            return Long.valueOf(((SkillCustomInfo) this.customInfo.get(Integer.valueOf(skillid))).getValue());
        }
        return null;
    }

    public Integer getCustomTime(int skillid) {
        if (this.customInfo.containsKey(Integer.valueOf(skillid))) {
            return Integer.valueOf((int) (((SkillCustomInfo) this.customInfo.get(Integer.valueOf(skillid))).getEndTime() - System.currentTimeMillis()));
        }
        return null;
    }

    public long getCustomValue0(int skillid) {
        if (this.customInfo.containsKey(Integer.valueOf(skillid))) {
            return ((SkillCustomInfo) this.customInfo.get(Integer.valueOf(skillid))).getValue();
        }
        return 0L;
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

    public void addSkillCustomInfo(int skillid, long value) {
        this.customInfo.put(Integer.valueOf(skillid), new SkillCustomInfo(getCustomValue0(skillid) + value, 0L));
    }

    public Map<Integer, SkillCustomInfo> getCustomValues() {
        return this.customInfo;
    }

    public final void RemoveStati(MonsterStatus stat, MonsterStatusEffect effect, boolean ig) {
        List<Pair<MonsterStatus, MonsterStatusEffect>> remove = new ArrayList<>();
        List<Ignition> removes = new ArrayList<>();
        Iterator<Pair<MonsterStatus, MonsterStatusEffect>> itr = this.stati.iterator();
        Iterator<Ignition> igitr = getIgnitions().iterator();
        if (ig && stat != null && effect != null) {
            while (itr.hasNext()) {
                Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
                if (stat == skill.getLeft() && ((MonsterStatusEffect) skill.getRight()).getSkill() == effect.getSkill()) {
                    remove.add(skill);
                    break;
                }
            }
            if (stat == MonsterStatus.MS_Burned) {
                while (igitr.hasNext()) {
                    Ignition ignition = igitr.next();
                    if (effect.getSkill() == ignition.getSkill()) {
                        removes.add(ignition);
                        break;
                    }
                }
            }
        }
        while (itr.hasNext()) {
            Pair<MonsterStatus, MonsterStatusEffect> skill = itr.next();
            if (System.currentTimeMillis() >= ((MonsterStatusEffect) skill.getRight()).getStartTime() + ((MonsterStatusEffect) skill.getRight()).getDuration()) {
                remove.add(new Pair<>(skill.getLeft(), skill.getRight()));
            }
        }
        while (igitr.hasNext()) {
            Ignition ignition = igitr.next();
            if (System.currentTimeMillis() >= ignition.getStartTime() + ignition.getDuration()) {
                removes.add(ignition);
            }
        }
        if (!remove.isEmpty()) {
            this.stati.removeAll(remove);
        }
        if (!removes.isEmpty()) {
            getIgnitions().removeAll(removes);
        }
    }

    public final void handleSteal(MapleCharacter chr) {
        double showdown = 100.0D;
        MonsterStatusEffect mse = getBuff(MonsterStatus.MS_Showdown);
        if (mse != null) {
            showdown += mse.getValue();
        }
        Skill steal = SkillFactory.getSkill(4201004);
        int level = chr.getTotalSkillLevel(steal), chServerrate = ChannelServer.getInstance(chr.getClient().getChannel()).getDropRate();
        if (level > 0 && !getStats().isBoss() && this.stolen == -1 && steal.getEffect(level).makeChanceResult()) {
            MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();
            List<MonsterDropEntry> de = mi.retrieveDrop(getId());
            if (de == null) {
                this.stolen = 0;
                return;
            }
            List<MonsterDropEntry> dropEntry = new ArrayList<>(de);
            Collections.shuffle(dropEntry);
            for (MonsterDropEntry d : dropEntry) {
                if (d.itemId > 0 && d.questid == 0 && d.itemId / 10000 != 238 && Randomizer.nextInt(999999) < (int) ((10 * d.chance * chServerrate * chr.getDropMod()) * (chr.getStat()).dropBuff / 100.0D * showdown / 100.0D)) {
                    Item idrop;
                    if (GameConstants.getInventoryType(d.itemId) == MapleInventoryType.EQUIP) {
                        idrop = MapleItemInformationProvider.getInstance().getEquipById(d.itemId);
                    } else {
                        idrop = new Item(d.itemId, (short) 0, (short) ((d.Maximum != 1) ? (Randomizer.nextInt(d.Maximum - d.Minimum) + d.Minimum) : 1), 0);
                    }
                    this.stolen = d.itemId;
                    this.map.spawnMobDrop(idrop, this.map.calcDropPos(getPosition(), getTruePosition()), this, chr, (byte) 0, 0);
                    break;
                }
            }
        } else {
            this.stolen = 0;
        }
    }

    public List<MonsterStatusEffect> getIndielist() {
        return this.indielist;
    }

    public void setIndielist(List<MonsterStatusEffect> indielist) {
        this.indielist = indielist;
    }

    public boolean isElitemonster() {
        return this.elitemonster;
    }

    public void setElitemonster(boolean elitemonster) {
        this.elitemonster = elitemonster;
    }

    public boolean isEliteboss() {
        return this.eliteboss;
    }

    public void setEliteboss(boolean eliteboss) {
        this.eliteboss = eliteboss;
    }

    public boolean isUserunespawn() {
        return this.userunespawn;
    }

    public void setUserunespawn(boolean userunespawn) {
        this.userunespawn = userunespawn;
    }

    public String getSpecialtxt() {
        return this.specialtxt;
    }

    public void setSpecialtxt(String specialtxt) {
        this.specialtxt = specialtxt;
    }

    public int getPatten() {
        return this.patten;
    }

    public void SetPatten(int a) {
        this.patten = a;
    }

    public void setDeadTime(int time) {
        Timer.MapTimer.getInstance().schedule(() -> {
            if (this != null) {
                getMap().killMonsterType(this, 0);
            }
        }, time);
    }

    public void setDeadTimeKillmob(int time) {
        Timer.MapTimer.getInstance().schedule(() -> {
            if (this != null) {
                getMap().killMonster(this);
            }
        }, time);
    }

    public int getEnergycount() {
        return this.energycount;
    }

    public void setEnergycount(int energycount) {
        this.energycount = energycount;
    }

    public boolean isEnergyleft() {
        return this.energyleft;
    }

    public void setEnergyleft(boolean energyleft) {
        this.energyleft = energyleft;
    }

    public int getEnergyspeed() {
        return this.energyspeed;
    }

    public void setEnergyspeed(int energyspeed) {
        this.energyspeed = energyspeed;
    }

    public Map<Integer, Rectangle> getRectangles() {
        return this.rectangles;
    }

    public void setRectangles(Map<Integer, Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public int getStigmaType() {
        return this.StigmaType;
    }

    public void setStigmaType(int StigmaType) {
        this.StigmaType = StigmaType;
    }

    public int getTotalStigma() {
        return this.TotalStigma;
    }

    public void setTotalStigma(int TotalStigma) {
        this.TotalStigma = TotalStigma;
    }

    public void DemainChangePhase(MapleCharacter from) {
        if (!this.demianChangePhase) {
            this.map.removeAllFlyingSword();
            this.map.broadcastMessage(CField.enforceMSG("데미안이 완전한 어둠을 손에 넣었습니다.", 216, 30000000));
            this.map.broadcastMessage(MobPacket.ChangePhaseDemian(this, 79));
            this.demianChangePhase = true;
            if (from.getEventInstance() != null) {
                from.getEventInstance().monsterKilled(from, this);
            }
            Timer.MapTimer.getInstance().schedule(() -> this.map.killMonsterType(this, 0), 6000L);
        }
    }

    public int getSerenNoonTotalTime() {
        return this.SerenNoonTotalTime;
    }

    public void setSerenNoonTotalTime(int SerenNoonTotalTime) {
        this.SerenNoonTotalTime = SerenNoonTotalTime;
    }

    public int getSerenSunSetTotalTime() {
        return this.SerenSunSetTotalTime;
    }

    public void setSerenSunSetTotalTime(int SerenSunSetTotalTime) {
        this.SerenSunSetTotalTime = SerenSunSetTotalTime;
    }

    public int getSerenMidNightSetTotalTime() {
        return this.SerenMidNightSetTotalTime;
    }

    public void setSerenMidNightSetTotalTime(int SerenMidNightSetTotalTime) {
        this.SerenMidNightSetTotalTime = SerenMidNightSetTotalTime;
    }

    public int getSerenDawnSetTotalTime() {
        return this.SerenDawnSetTotalTime;
    }

    public void setSerenDawnSetTotalTime(int SerenDawnSetTotalTime) {
        this.SerenDawnSetTotalTime = SerenDawnSetTotalTime;
    }

    public int getSerenNoonNowTime() {
        return this.SerenNoonNowTime;
    }

    public void setSerenNoonNowTime(int SerenNoonNowTime) {
        this.SerenNoonNowTime = SerenNoonNowTime;
    }

    public int getSerenSunSetNowTime() {
        return this.SerenSunSetNowTime;
    }

    public void setSerenSunSetNowTime(int SerenSunSetNowTime) {
        this.SerenSunSetNowTime = SerenSunSetNowTime;
    }

    public int getSerenMidNightSetNowTime() {
        return this.SerenMidNightSetNowTime;
    }

    public void setSerenMidNightSetNowTime(int SerenMidNightSetNowTime) {
        this.SerenMidNightSetNowTime = SerenMidNightSetNowTime;
    }

    public int getSerenDawnSetNowTime() {
        return this.SerenDawnSetNowTime;
    }

    public void setSerenDawnSetNowTime(int SerenDawnSetNowTime) {
        this.SerenDawnSetNowTime = SerenDawnSetNowTime;
    }

    public int getSerenTimetype() {
        return this.SerenTimetype;
    }

    public void setSerenTimetype(int SerenTimetype) {
        this.SerenTimetype = SerenTimetype;
    }

    public void ResetSerenTime(boolean show) {
        this.SerenTimetype = 1;
        this.SerenNoonNowTime = 110;
        this.SerenNoonTotalTime = 110;
        this.SerenSunSetNowTime = 110;
        this.SerenSunSetTotalTime = 110;
        this.SerenMidNightSetNowTime = 30;
        this.SerenMidNightSetTotalTime = 30;
        this.SerenDawnSetNowTime = 110;
        this.SerenDawnSetTotalTime = 110;
        if (show) {
            getMap().broadcastMessage(MobPacket.BossSeren.SerenTimer(0, new int[]{360000, this.SerenNoonTotalTime, this.SerenSunSetTotalTime, this.SerenMidNightSetTotalTime, this.SerenDawnSetTotalTime}));
        }
    }

    public void AddSerenTotalTimeHandler(int type, int add, int turn) {
        getMap().broadcastMessage(MobPacket.BossSeren.SerenTimer(1, new int[]{this.SerenNoonTotalTime, this.SerenSunSetTotalTime, this.SerenMidNightSetTotalTime, this.SerenDawnSetTotalTime, turn}));
    }

    public void AddSerenTimeHandler(int type, int add) {
        int nowtime = 0;
        switch (type) {
            case 1:
                this.SerenNoonNowTime += add;
                break;
            case 2:
                this.SerenSunSetNowTime += add;
                for (MapleCharacter chr : getMap().getAllChracater()) {
                    if (chr.isAlive()
                            && chr.getBuffedValue(SecondaryStat.NotDamaged) == null && chr.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
                        int minushp = (int) (-chr.getStat().getCurrentMaxHp() / 100L);
                        chr.addHP(minushp);
                        chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, minushp, 36, 0, 0, (byte) 0, true, null, null, null));
                    }
                }
                break;
            case 3:
                this.SerenMidNightSetNowTime += add;
                break;
            case 4:
                this.SerenDawnSetNowTime += add;
                break;
        }
        nowtime = (type == 4) ? this.SerenDawnSetNowTime : ((type == 3) ? this.SerenMidNightSetNowTime : ((type == 2) ? this.SerenSunSetNowTime : this.SerenNoonNowTime));
        MapleMonster seren = null;
        int[] serens = {8880603, 8880607, 8880609, 8880612};
        for (int ids : serens) {
            seren = getMap().getMonsterById(ids);
            if (seren != null) {
                break;
            }
        }
        if (nowtime == 3) {
            for (MapleMonster mob : getMap().getAllMonster()) {
                if (mob.getId() == seren.getId() + 1) {
                    getMap().broadcastMessage(MobPacket.ChangePhaseDemian(mob, 79));
                    getMap().killMonsterType(mob, 2);
                }
            }
        }
        if (nowtime <= 0) {
            if (seren != null) {
                Point pos = seren.getPosition();
                getMap().broadcastMessage(MobPacket.BossSeren.SerenTimer(2, new int[]{1}));
                setCustomInfo(8880603, 1, 0);
                getMap().broadcastMessage(MobPacket.BossSeren.SerenChangePhase("Mob/" + seren.getId() + ".img/skill3", 0, seren));
                for (MapleMonster mob : getMap().getAllMonster()) {
                    if (mob.getId() == seren.getId() || mob.getId() == 8880605 || mob.getId() == 8880606 || mob.getId() == 8880611) {
                        getMap().broadcastMessage(MobPacket.ChangePhaseDemian(mob, 79));
                        getMap().killMonsterType(mob, 2);
                    }
                }
                this.SerenTimetype++;
                if (this.SerenTimetype > 4) {
                    this.SerenTimetype = 1;
                }
                getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "시간이 흐르고 태양 또한 정해진 순환에 따라 변화합니다."));
                switch (this.SerenTimetype) {
                    case 1:
                        addHp(this.shield, false);
                        this.shield = -1L;
                        this.shieldmax = -1L;
                        getMap().broadcastMessage(MobPacket.showBossHP(this));
                        getMap().broadcastMessage(MobPacket.mobBarrier(this));
                        getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "정오가 시작됨과 동시에 남아있는 여명의 기운이 세렌을 회복시킵니다."));
                        this.SerenNoonNowTime = this.SerenNoonTotalTime;
                        break;
                    case 2:
                        getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "황혼의 불타는 듯한 석양이 회복 효율을 낮추고 지속적으로 피해를 입힙니다."));
                        this.SerenSunSetNowTime = this.SerenSunSetTotalTime;
                        break;
                    case 3:
                        getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "태양이 저물어 빛을 잃고 자정이 시작됩니다."));
                        this.SerenMidNightSetNowTime = this.SerenMidNightSetTotalTime;
                        break;
                    case 4:
                        getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "태양이 서서히 떠올라 빛과 희망이 시작되는 여명이 다가옵니다."));
                        this.SerenDawnSetNowTime = this.SerenDawnSetTotalTime;
                        break;
                }
                Timer.MapTimer.getInstance().schedule(() -> {
                    getMap().broadcastMessage(SLFCGPacket.ClearObstacles());
                    FieldSkillFactory.getInstance();
                    getMap().broadcastMessage(MobPacket.useFieldSkill(FieldSkillFactory.getFieldSkill(100024, 1)));
                }, 500L);
                Timer.MapTimer.getInstance().schedule(() -> {
                    int nextid = (type == 4) ? 8880607 : ((type == 3) ? 8880603 : ((type == 2) ? 8880612 : 8880609));
                    getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(nextid), pos);
                    getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(nextid + 1), new Point(-49, 305));
                    if (nextid == 8880603) {
                        getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880605), pos);
                        MapleMonster totalseren = getMap().getMonsterById(8880602);
                        if (totalseren != null) {
                            totalseren.gainShield(totalseren.getStats().getHp() * 15L / 100L, !(totalseren.getShield() > 0L), 0);
                        }
                    }
                    getMap().broadcastMessage(MobPacket.BossSeren.SerenTimer(2, new int[]{0}));
                    setCustomInfo(8880603, 0, 0);
                    getMap().broadcastMessage(MobPacket.BossSeren.SerenChangeBackground(this.SerenTimetype));
                }, 3560L);
            }
        }
    }

    public void gainShield(long energy, boolean first, int delayremove) {
        this.shield += energy;
        if (first) {
            this.shield = energy;
            this.shieldmax = energy;
            if (delayremove > 0) {
                Timer.EtcTimer.getInstance().schedule(() -> {
                    this.shield = 0L;
                    this.shieldmax = 0L;
                    getMap().broadcastMessage(MobPacket.mobBarrier(this));
                }, (delayremove * 1000));
            }
        }
        getMap().broadcastMessage(MobPacket.mobBarrier(this));
    }

    public long getShield() {
        return this.shield;
    }

    public void setShield(long shield) {
        this.shield = shield;
    }

    public long getShieldmax() {
        return this.shieldmax;
    }

    public void setShieldmax(long shieldmax) {
        this.shieldmax = shieldmax;
    }

    public int getShieldPercent() {
        return (int) Math.ceil(this.shield * 100.0D / this.shieldmax);
    }

    public boolean isWillSpecialPattern() {
        return this.willSpecialPattern;
    }

    public void setWillSpecialPattern(boolean willSpecialPattern) {
        this.willSpecialPattern = willSpecialPattern;
    }

    public long getElitehp() {
        return this.elitehp;
    }

    public void setElitehp(long elitehp) {
        this.elitehp = elitehp;
    }

    public boolean isElitechmp() {
        return this.elitechmp;
    }

    public void setElitechmp(boolean elitechmp) {
        this.elitechmp = elitechmp;
    }

    public static class CancelStatusAction implements Runnable {

        private final WeakReference<MapleMonster> target;

        private final MonsterStatus status;

        private final MonsterStatusEffect effect;

        public CancelStatusAction(MapleMonster target, MonsterStatus status, MonsterStatusEffect effect) {
            this.target = new WeakReference<>(target);
            this.status = status;
            this.effect = effect;
        }

        public void run() {
            MapleMonster realTarget = this.target.get();
            if (realTarget != null && realTarget.isAlive()) {
                realTarget.cancelStatus(this.status, this.effect, true);
            }
        }
    }

    private static interface AttackerEntry {

        List<MapleMonster.AttackingMapleCharacter> getAttackers();

        void addDamage(MapleCharacter param1MapleCharacter, long param1Long, boolean param1Boolean);

        long getDamage();

        boolean contains(MapleCharacter param1MapleCharacter);

        void killedMob(MapleMap param1MapleMap, long param1Long, boolean param1Boolean, int param1Int);
    }
}
