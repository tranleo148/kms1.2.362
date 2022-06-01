package server.life;

import client.MapleCharacter;
import client.SecondaryStat;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import handling.channel.ChannelServer;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.Obstacle;
import server.Randomizer;
import server.Timer;
import server.field.boss.demian.MapleFlyingSword;
import server.field.boss.lotus.MapleEnergySphere;
import server.field.boss.lucid.Butterfly;
import server.field.boss.lucid.FairyDust;
import server.field.boss.lucid.FieldLucid;
import server.field.boss.will.SpiderWeb;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import server.maps.MapleNodes;
import server.maps.MapleReactor;
import tools.Pair;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SLFCGPacket;

public class MobSkill {

    private int skillId;

    private int skillLevel;

    private int mpCon;

    private int spawnEffect;

    private int hp;

    private int x;

    private int y;

    private int action;

    private long duration;

    private long interval;

    private long skillForbid;

    private float prop;

    private short limit;

    private List<Integer> toSummon = new ArrayList<>();

    private Point lt;

    private Point rb;

    private boolean summonOnce;

    private boolean onlyFsm;

    private boolean onlyOtherSkill;

    private boolean isMobGroup;

    private int skillAfter;

    private int otherSkillID;

    private int otherSkillLev;

    private int afterAttack;

    private int afterAttackCount;

    private int afterDead;

    private int force;

    public MobSkill(int skillId, int level) {
        this.skillId = skillId;
        this.skillLevel = level;
    }

    public void setOnce(boolean o) {
        this.summonOnce = o;
    }

    public boolean onlyOnce() {
        return this.summonOnce;
    }

    public void setMpCon(int mpCon) {
        this.mpCon = mpCon;
    }

    public void addSummons(List<Integer> toSummon) {
        this.toSummon = toSummon;
    }

    public void setSpawnEffect(int spawnEffect) {
        this.spawnEffect = spawnEffect;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setProp(float prop) {
        this.prop = prop;
    }

    public void setLtRb(Point lt, Point rb) {
        this.lt = lt;
        this.rb = rb;
    }

    public void setLimit(short limit) {
        this.limit = limit;
    }

    public boolean checkDealyBuff(MapleCharacter player, MapleMonster monster) {
        boolean use = true;
        switch (this.skillId) {
            case 133:
                if (this.skillLevel == 18
                        && monster.getId() == 8920103) {
                    use = false;
                }
                break;
            case 183:
                if (this.skillLevel == 2
                        && monster.getId() == 8920102) {
                    use = false;
                }
                break;
            case 186:
                if (this.skillLevel == 2
                        && monster.getId() == 8920101) {
                    use = false;
                }
                break;
            case 201:
                if (this.skillLevel == 60
                        && monster.getId() == 8920100) {
                    use = true;
                }
                break;
            case 252:
                if (monster.getId() == 8860000) {
                    use = false;
                }
                break;
        }
        if (monster.getCustomValue0(1234567) == 1L) {
            use = false;
        }
        return use;
    }

    public boolean checkCurrentBuff(MapleCharacter player, MapleMonster monster) {
        int mobId2;
        boolean stop = false;
        switch (this.skillId) {
            case 100:
            case 110:
            case 150:
                stop = monster.isBuffed(MonsterStatus.MS_Pad);
                break;
            case 101:
            case 111:
            case 151:
                stop = monster.isBuffed(MonsterStatus.MS_Mad);
                break;
            case 102:
            case 112:
            case 152:
                stop = monster.isBuffed(MonsterStatus.MS_Pdr);
                break;
            case 103:
            case 113:
            case 153:
                stop = monster.isBuffed(MonsterStatus.MS_Mdr);
                break;
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
                stop = (monster.isBuffed(MonsterStatus.MS_Hardskin) || monster.isBuffed(MonsterStatus.MS_PowerImmune) || monster.isBuffed(MonsterStatus.MS_MImmune) || monster.isBuffed(MonsterStatus.MS_PImmune));
                break;
            case 129:
                if (monster.getHPPercent() > 50) {
                    stop = true;
                }
                break;
            case 133:
                if (this.skillLevel == 18
                        && monster.getId() == 8920103) {
                    stop = true;
                }
                break;
            case 170:
                if ((monster.getId() == 8910000 || monster.getId() == 8910100)
                        && this.skillLevel == 11) {
                    int hp = (monster.getId() == 8910100) ? 30 : 10;
                    if (monster.getHPPercent() > hp) {
                        stop = true;
                    } else {
                        monster.setNextSkill(170);
                        monster.setNextSkillLvl(11);
                    }
                }
                if (this.skillLevel == 49 || this.skillLevel == 51) {
                    stop = true;
                }
                break;
            case 176:
                if (this.skillLevel >= 1 && this.skillLevel <= 4) {
                    stop = true;
                    for (MapleMapObject remo : monster.getMap().getAllReactorsThreadsafe()) {
                        MapleReactor react = (MapleReactor) remo;
                        if (react.getReactorId() >= 2708001 && react.getReactorId() <= 2708004 && react.getState() == 0) {
                            stop = false;
                            break;
                        }
                    }
                }
                break;
            case 184:
                if (monster.getId() == 8910000) {
                    if (this.skillLevel == 1 && (monster.getHPPercent() < 10 || (monster.getHPPercent() <= 100 && monster.getHPPercent() >= 70))) {
                        stop = true;
                    }
                    break;
                }
                if (monster.getId() == 8910100 && (monster.getHPPercent() < 10 || (monster.getHPPercent() <= 100 && monster.getHPPercent() >= 70))) {
                    stop = true;
                }
                break;
            case 183:
                if (this.skillLevel == 2
                        && monster.getId() == 8920102) {
                    stop = true;
                }
                break;
            case 186:
                if (this.skillLevel == 2
                        && monster.getId() == 8920101) {
                    stop = true;
                }
                break;
            case 191:
                if (monster.getHPPercent() <= 40 && monster.getHPPercent() >= 0) {
                    stop = true;
                }
                break;
            case 203:
                if (monster.getId() == 8910100) {
                    stop = true;
                    break;
                }
                if (monster.getHPPercent() > 40 || monster.getHPPercent() < 10) {
                    stop = true;
                }
                break;
            case 200:
                if (this.skillLevel == 251) {
                    if (monster.getCustomValue(8870100) != null
                            && monster.getCustomValue0(8870100) > 0L) {
                        stop = true;
                    }
                    break;
                }
                if (this.limit < 0) {
                    this.limit = 0;
                }
                for (Integer mobId : getSummons()) {
                    if (player.getMap().countMonsterById(mobId.intValue()) > this.limit) {
                        return true;
                    }
                }
                break;
            case 201:
                if (this.skillLevel == 60) {
                    if (monster.getId() == 8920100) {
                        stop = true;
                    }
                    break;
                }
                if (this.limit < 0) {
                    this.limit = 0;
                }
                if (this.skillLevel == 199) {
                    this.limit = 1;
                }
                mobId2 = 0;
                for (Integer mobId : getSummons()) {
                    mobId2 = mobId.intValue();
                    if (player.getMap().countMonsterById(mobId.intValue()) > this.limit) {
                        return true;
                    }
                }
                break;
            case 214:
                if (this.skillLevel == 14) {
                    stop = true;
                }
                break;
            case 238:
                if (monster.getCustomValue(23807) != null) {
                    stop = true;
                 break; //break
                }
            case 241:
                if (this.skillLevel == 1 || this.skillLevel == 2) {
                    stop = monster.isBuffed(MonsterStatus.MS_PopulatusTimer);
                }
                break;
            case 242:
                if (this.skillLevel == 5) {
                    stop = true;
                }
                break;
            case 252:
                if (monster.getId() == 8860000) {
                    stop = true;
                }
                break;
        }
        if (monster.getCustomValue0(1234567) == 1L) {
            stop = true;
        }
        return stop;
    }

    public void applyEffect(MapleCharacter player, MapleMonster monster, boolean skill, boolean isFacingLeft) {
        applyEffect(player, monster, skill, isFacingLeft, 0);
    }

    public void applyEffect(MapleCharacter player, final MapleMonster monster, boolean skill, boolean isFacingLeft, int RectCount) {
        try {
            BanishInfo info;
            MapleMist mist;
            List<Point> list;
            int x;
            Point pos;
            MapleCharacter rchar;
            Point pos2;
            int value;
            MapleCharacter mapleCharacter1;
            int rId, type;
            SecondaryStat m;
            int minx;
            Point clockPos;
            Rectangle box;
            MapleMist clock;
            int i22;
            MapleMap target;
            MapleMist mist3;
            int time, rand, z;
            MapleMist Swoomist;
            Map<SecondaryStat, Pair<Integer, Integer>> dds;
            List<FairyDust> fairyDust;
            MobSkill ms1;
            MapleCharacter chr3;
            final MobSkill ms;
            int id;
            List<Triple<Point, Integer, List<Rectangle>>> datas;
            List<Integer> laserIntervals;
            final int j, a, size, i, Glasstime;
            boolean isLeft;
            int abd, typeed;
            List<Triple<Integer, Integer, Integer>> idx;
            int sandCount;
            String difical;
            List<Integer> ids;
            MapleMist mapleMist1;
            int ab, bluecount, n, k, purplecount;
            boolean bool1;
            int reverge, i1;
            List<Obstacle> obs;
            int i2, size_;
            List<Integer> idss;
            int webSize, i4, i3;
            List<Integer> list1;
            int y, i5;
            Map<SecondaryStat, Pair<Integer, Integer>> diseases = new EnumMap<>(SecondaryStat.class);
            List<SecondaryStat> cancels = new ArrayList<>();
            List<Pair<MonsterStatus, MonsterStatusEffect>> stats = new ArrayList<>();
            boolean allchr = false;
            if (monster != null && player != null) {
                player.dropMessageGM(5, monster.getStats().getName() + "(" + monster.getId() + ")의 스킬 : " + this.skillId + " / " + this.skillLevel);
            }
            switch (this.skillId) {
                case 100:
                    stats.add(new Pair<>(MonsterStatus.MS_Pad, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 101:
                    stats.add(new Pair<>(MonsterStatus.MS_Mad, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 102:
                    stats.add(new Pair<>(MonsterStatus.MS_Pdr, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 103:
                    stats.add(new Pair<>(MonsterStatus.MS_Mdr, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 105:
                    if (monster.getId() == 8840000 || monster.getId() == 8840007 || monster.getId() == 8840014) {
                        int sizeed = 0;
                        for (MapleMonster mon : player.getMap().getAllMonster()) {
                            if (mon.getId() == 8840017) {
                                monster.getMap().killMonster(mon, player, false, false, (byte) 1);
                                sizeed++;
                            }
                        }
                        monster.addHp((getX() * sizeed), true);
                        break;
                    }
                    if (this.lt != null && this.rb != null && skill && monster != null) {
                        List<MapleMapObject> objects = getObjectsInRange(monster, MapleMapObjectType.MONSTER);
                        for (MapleMapObject mons : objects) {
                            if (mons.getObjectId() != monster.getObjectId()) {
                                player.getMap().killMonster((MapleMonster) mons, player, true, false, (byte) 1, 0);
                                monster.heal(getX(), getY(), true);
                                break;
                            }
                        }
                        break;
                    }
                    if (monster != null) {
                        monster.heal(getX(), getY(), true);
                    }
                    break;
                case 110:
                    stats.add(new Pair<>(MonsterStatus.MS_Pad, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 111:
                    stats.add(new Pair<>(MonsterStatus.MS_Mad, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 112:
                    stats.add(new Pair<>(MonsterStatus.MS_Pdr, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 113:
                    stats.add(new Pair<>(MonsterStatus.MS_Mdr, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 114:
                    if (this.lt != null && this.rb != null && skill && monster != null) {
                        List<MapleMapObject> objects = getObjectsInRange(monster, MapleMapObjectType.MONSTER);
                        int hp = getX() / 1000 * (int) (950.0D + 3300.0D * Math.random());
                        for (MapleMapObject mons : objects) {
                            ((MapleMonster) mons).heal(hp, getY(), true);
                            player.getMap().broadcastMessage(MobPacket.healEffectMonster(((MapleMonster) mons).getObjectId(), this.skillId, this.skillLevel));
                        }
                        break;
                    }
                    if (monster != null) {
                        monster.heal(getX(), getY(), true);
                        player.getMap().broadcastMessage(MobPacket.healEffectMonster(monster.getObjectId(), this.skillId, this.skillLevel));
                    }
                    break;
                case 115:
                    stats.add(new Pair<>(MonsterStatus.MS_Speed, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 120:
                    diseases.put(SecondaryStat.Seal, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    if (this.skillLevel == 37) {
                        diseases.put(SecondaryStat.KawoongDebuff, new Pair<>(Integer.valueOf(20), Integer.valueOf((int) this.duration)));
                    }
                    break;
                case 121:
                    diseases.put(SecondaryStat.Darkness, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 122:
                    diseases.put(SecondaryStat.Weakness, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    if (this.skillLevel == 20) {
                        diseases.put(SecondaryStat.Slow, new Pair<>(Integer.valueOf(60), Integer.valueOf((int) this.duration)));
                    }
                    break;
                case 123:
                    diseases.put(SecondaryStat.Stun, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) this.duration)));
                    break;
                case 124:
                    diseases.put(SecondaryStat.Curse, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 125:
                    diseases.put(SecondaryStat.Poison, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 126:
                    diseases.put(SecondaryStat.Slow, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    if (this.skillLevel == 46) {
                        allchr = true;
                    }
                    break;
                case 127:
                    if (this.lt != null && this.rb != null && skill && monster != null && player != null) {
                        for (MapleCharacter character : getPlayersInRange(monster, player)) {
                            character.dispel();
                        }
                        break;
                    }
                    if (player != null) {
                        player.dispel();
                    }
                    break;
                case 128:
                    diseases.put(SecondaryStat.Attract, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 129:
                    if (monster == null || (monster.getEventInstance() != null && monster.getEventInstance().getName().indexOf("BossQuest") != -1)) {
                        break;
                    }
                    info = monster.getStats().getBanishInfo();
                    if (info != null) {
                        if (this.lt != null && this.rb != null && skill && player != null) {
                            for (MapleCharacter mapleCharacter : getPlayersInRange(monster, player)) {
                                mapleCharacter.changeMapBanish(info.getMap(), info.getPortal(), info.getMsg());
                                mapleCharacter.dropMessage(5, monster.getStats().getName() + "의 힘에 의해 다른장소로 쫓겨 납니다.");
                            }
                            break;
                        }
                        if (player != null) {
                            player.changeMapBanish(info.getMap(), info.getPortal(), info.getMsg());
                        }
                    }
                    break;
                case 131:
                    mist = null;
                    if (monster.getId() / 1000 != 8950) {
                        mist = new MapleMist(calculateBoundingBox(monster.getTruePosition(), true), monster, this, (int) this.duration);
                    }
                    if (mist != null && monster != null && monster.getMap() != null) {
                        monster.getMap().spawnMist(mist, false);
                    }
                    break;
                case 132:
                    diseases.put(SecondaryStat.ReverseInput, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) this.duration)));
                    break;
                case 133:
                    diseases.put(SecondaryStat.Undead, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 134:
                    diseases.put(SecondaryStat.StopPortion, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 135:
                    diseases.put(SecondaryStat.StopMotion, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 136:
                    diseases.put(SecondaryStat.Fear, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 137:
                    diseases.put(SecondaryStat.Frozen, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 138:
                    diseases.put(SecondaryStat.DispelItemOption, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 140:
                    stats.add(new Pair<>(MonsterStatus.MS_PImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 141:
                    stats.add(new Pair<>(MonsterStatus.MS_MImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 142:
                    stats.add(new Pair<>(MonsterStatus.MS_Hardskin, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 143:
                    stats.add(new Pair<>(MonsterStatus.MS_PImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_PCounter, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 144:
                    stats.add(new Pair<>(MonsterStatus.MS_MImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_MCounter, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 145:
                    System.out.println(this.x);
                    stats.add(new Pair<>(MonsterStatus.MS_PImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_MImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_PCounter, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_MCounter, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    if (monster.getId() == 8840000 || monster.getId() == 8840007 || monster.getId() == 8840014) {
                        monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 1, 5, 0, "근 거리의 포위당한 반 레온이 반격을 시전합니다."));
                    }
                    break;
                case 146:
                    if (monster.getId() == 8850010 || monster.getId() == 8850110) {
                        MapleMonster cyguns = monster.getMap().getMonsterById(8850011);
                        if (cyguns == null) {
                            cyguns = monster.getMap().getMonsterById(8850111);
                        }
                        if (cyguns != null) {
                            stats.add(new Pair<>(MonsterStatus.MS_PowerImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                            cyguns.applyMonsterBuff(monster.getMap(), stats, this);
                            stats.clear();
                        }
                        break;
                    }
                    if (monster.getId() != 8850010) {
                        stats.add(new Pair<>(MonsterStatus.MS_PowerImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    }
                    break;
                case 150:
                    stats.add(new Pair<>(MonsterStatus.MS_Pad, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 151:
                    stats.add(new Pair<>(MonsterStatus.MS_Mad, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 152:
                    stats.add(new Pair<>(MonsterStatus.MS_Pdr, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 153:
                    stats.add(new Pair<>(MonsterStatus.MS_Mdr, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 154:
                    stats.add(new Pair<>(MonsterStatus.MS_Acc, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 155:
                    stats.add(new Pair<>(MonsterStatus.MS_Eva, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 156:
                    stats.add(new Pair<>(MonsterStatus.MS_Speed, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 157:
                    stats.add(new Pair<>(MonsterStatus.MS_Seal, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 170:
                    switch (this.skillLevel) {
                        case 1:
                        case 3:
                            if (monster.getId() == 8840000 || monster.getId() == 8840007 || monster.getId() == 8840014) {
                                monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 1, 5, 0, monster.getStats().getName() + "이 자신에게 가장 큰 위협을 준 적에게 순간이동하여 다가갑니다."));
                                monster.setPosition(player.getTruePosition());
                                player.getClient().getSession().writeAndFlush(MobPacket.TeleportMonster(monster, false, 2, monster.getTruePosition()));
                                break;
                            }
                            monster.setPosition(player.getTruePosition());
                            player.getClient().getSession().writeAndFlush(MobPacket.TeleportMonster(monster, false, 2, monster.getTruePosition()));
                            break;
                        case 10:
                            monster.setPosition(player.getTruePosition());
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 5, monster.getTruePosition()));
                            break;
                        case 13:
                            list = new ArrayList<>();
                            list.add(new Point(810, 443));
                            list.add(new Point(-2190, 443));
                            list.add(new Point(-1690, 443));
                            list.add(new Point(560, 443));
                            list.add(new Point(-190, 443));
                            list.add(new Point(-690, 443));
                            list.add(new Point(-1940, 443));
                            list.add(new Point(1310, 443));
                            list.add(new Point(-1190, 443));
                            list.add(new Point(1060, 443));
                            list.add(new Point(-940, 443));
                            list.add(new Point(-1440, 443));
                            list.add(new Point(1560, 443));
                            list.add(new Point(-440, 443));
                            monster.getMap().broadcastMessage(MobPacket.dropStone("DropStone", list));
                            if (isFacingLeft) {
                                monster.setPosition(new Point(965, 420));
                                monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 3, new Point(965, 420)));
                            } else {
                                monster.setPosition(new Point(-1750, 420));
                                monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 3, new Point(-1750, 420)));
                            }
                            monster.getMap().broadcastMessage(CWvsContext.getTopMsg("벨룸이 깊은 숨을 들이쉽니다."));
                            Timer.MobTimer.getInstance().schedule(() -> {
                                if (monster.isAlive()) {
                                    monster.setSkillForbid(false);
                                    monster.getMap().broadcastMessage(MobPacket.dropStone("DropStone", list));
                                    monster.getMap().broadcastMessage(MobPacket.setAfterAttack(monster.getObjectId(), 9, 1, 21, isFacingLeft));
                                }
                            }, getSkillForbid());
                            Timer.MobTimer.getInstance().schedule(() -> {
                                if (monster.isAlive()) {
                                    monster.getMap().broadcastMessage(MobPacket.dropStone("DropStone", list));
                                }
                            }, getSkillForbid() + 10000L);
                            break;
                        case 42:
                            x = isFacingLeft ? -900 : 900;
                            if (monster.getMap().getFootholds().findBelow(new Point((monster.getPosition()).x + x, (monster.getPosition()).y)) == null) {
                                x = 0;
                            }
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 10, new Point((monster.getPosition()).x + x, 17)));
                            break;
                        case 44:
                            if (monster.getId() == 8880409) {
                                monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 12, new Point(Randomizer.rand(-700, 700), 16)));
                                break;
                            }
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 12, new Point(Randomizer.rand(-40, 1400), 16)));
                            break;
                        case 45:
                            x = isFacingLeft ? -680 : 680;
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 12, new Point((monster.getPosition()).x + x, 16)));
                            break;
                        case 46:
                            x = isFacingLeft ? -482 : 482;
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 12, new Point((monster.getPosition()).x + x, 16)));
                            break;
                        case 50:
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, true, 1, new Point((monster.getPosition()).x, 16)));
                            monster.getMap().broadcastMessage(MobPacket.setAttackZakumArm(monster.getObjectId(), 7));
                            monster.getMap().broadcastMessage(CField.enforceMSG("데미안이 타락한 세계수의 힘을 폭주시키기 전에 큰 피해를 입혀 저지해야 합니다.", 216, 9500000));
                            monster.getMap().broadcastMessage(MobPacket.demianRunaway(monster, (byte) 0, MobSkillFactory.getMobSkill(214, 14), 10000, false));
                            monster.setCustomInfo(8880111, 1, 0);
                            monster.setNextSkill(0);
                            monster.setNextSkillLvl(0);
                            monster.setSkillForbid(true);
                            monster.setLastSkillUsed(MobSkillFactory.getMobSkill(214, 14), System.currentTimeMillis(), MobSkillFactory.getMobSkill(214, 14).getDuration());
                            diseases.put(SecondaryStat.Lapidification, new Pair<>(Integer.valueOf(16), Integer.valueOf(10000)));
                            allchr = true;
                            Timer.MobTimer.getInstance().schedule(() -> {
                                if (monster.isAlive()) {
                                    boolean suc = !(monster.getCustomValue0(8880111) == 2L);
                                    monster.removeCustomInfo(8880111);
                                    monster.removeCustomInfo(8880112);
                                    if (suc) {
                                            for (MapleCharacter chr : monster.getMap().getAllChracater()) {
                                                chr.getPercentDamage(monster, this.skillId, this.skillLevel, 200, false);
                                            }
                                            Timer.MobTimer.getInstance().schedule(() -> {
                                                if (monster.isAlive()) {
                                                    monster.getMap().broadcastMessage(MobPacket.setAttackZakumArm(monster.getObjectId(), 8));
                                                    monster.getMap().broadcastMessage(MobPacket.demianRunaway(monster, (byte)1, MobSkillFactory.getMobSkill(214, 14), 10000, true));
                                                    monster.setSkillForbid(false);
                                                }
                                            }, 500L);

                                    } else {
                                        monster.getMap().broadcastMessage(MobPacket.setAttackZakumArm(monster.getObjectId(), 8));
                                        monster.setSkillForbid(false);
                                    }
                                }
                            }, 10000L);
                            break;
                        case 51:
                            break;
                        case 57:
                            pos = new Point(monster.getPosition().x, monster.getPosition().y);
                            pos.x = Randomizer.rand(-623, 623);
                            if (monster.getId() == 8880343 || monster.getId() == 8880303) {
                                pos.y = 159;
                            } else if (monster.getId() == 8880344 || monster.getId() == 8880304) {
                                pos.y = -2020;
                            }
                            monster.setLastSkillUsed(this, System.currentTimeMillis(), 10000L);
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 12, pos));
                            break;
                        case 58:
                            monster.setPosition(player.getTruePosition());
                            monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 2, monster.getTruePosition()));
                            break;
                        case 62:
                            rchar = monster.getController().getClient().getRandomCharacter();
                            if (rchar != null) {
                                monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 2, rchar.getPosition()));
                            }
                            monster.setLastSkillUsed(this, System.currentTimeMillis(), 40000L);
                            break;
                        case 64:
                            monster.setLastSkillUsed(this, System.currentTimeMillis(), 40000L);
                            pos2 = null;
                            mapleCharacter1 = monster.getController().getClient().getRandomCharacter();
                            if (mapleCharacter1 != null) {
                                if (monster.getCustomValue0(8880501) == 1L) {
                                    int i6 = 350;
                                    if (monster.getId() == 8880500) {
                                        i6 *= -1;
                                    }
                                    pos2 = new Point(i6, 85);
                                    monster.removeCustomInfo(8880501);
                                }
                                monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 2, (pos2 != null) ? pos2 : mapleCharacter1.getPosition()));
                            }
                            break;
                        case 63:
                            monster.setPosition(player.getTruePosition());
                            player.getClient().getSession().writeAndFlush(MobPacket.TeleportMonster(monster, false, 2, monster.getTruePosition()));
                            break;
                        case 77:
                            monster.setPosition(monster.isFacingLeft() ? new Point((monster.getPosition()).x - 400, (monster.getPosition()).y) : new Point((monster.getPosition()).x + 400, (monster.getPosition()).y));
                            player.getClient().getSession().writeAndFlush(MobPacket.TeleportMonster(monster, false, 16, monster.getTruePosition()));
                            player.getMap().broadcastMessage(MobPacket.Monster_Attack(monster, false, 170, 77, 9));
                            break;
                        default: {
                            monster.setPosition(player.getTruePosition());
                            player.getClient().getSession().writeAndFlush(MobPacket.TeleportMonster(monster, false, 2, monster.getTruePosition()));
                            break;
                        }
                    }
                case 171:
                    diseases.put(SecondaryStat.TimeBomb, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 172:
                    diseases.put(SecondaryStat.Morph, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    if (monster.getId() == 8850011 || monster.getId() == 8850111) {
                        monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "시그너스가 버프 스킬을 비웃으며 적을 리본돼지로 변이하려 합니다."));
                    }
                    break;
                case 173:
                    diseases.put(SecondaryStat.DarkTornado, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 174:
                    if (this.skillLevel == 14) {
                        this.x = 6;
                    }
                    diseases.put(SecondaryStat.Lapidification, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 175:
                    cancels.add(SecondaryStat.DeathMark);
                    if (player.getDebuffValue(SecondaryStat.DeathMark).intValue() == 1) {
                        diseases.put(SecondaryStat.DeathMark, new Pair<>(Integer.valueOf(2), Integer.valueOf((int) this.duration)));
                        break;
                    }
                    if (player.getDebuffValue(SecondaryStat.DeathMark).intValue() == 2) {
                        diseases.put(SecondaryStat.DeathMark, new Pair<>(Integer.valueOf(3), Integer.valueOf((int) this.duration)));
                        break;
                    }
                    if (player.getDebuffValue(SecondaryStat.DeathMark).intValue() <= 2) {
                        diseases.put(SecondaryStat.DeathMark, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) this.duration)));
                    }
                    break;
                case 176:
                    value = 0;
                    rId = 0;
                    switch (this.skillLevel) {
                        case 6:
                            rId = 2708001;
                            break;
                        case 7:
                            rId = 2708002;
                            break;
                        case 8:
                            rId = 2708003;
                            break;
                        case 9:
                            rId = 2708004;
                            break;
                        case 5:
                            value = this.x;
                            break;
                        case 25:
                        case 26:
                        case 33:
                        case 34:
                            type = monster.getId() % 10;
                            minx = (type == 3) ? -85 : ((type == 4) ? -215 : ((type == 5) ? -340 : ((type == 6) ? -465 : ((type == 7) ? 60 : ((type == 8) ? 200 : ((type == 9) ? 350 : 500))))));
                            for (MapleCharacter mapleCharacter : player.getMap().getAllCharactersThreadsafe()) {
                                if (minx - 70 < (mapleCharacter.getPosition()).x && minx + 70 > (mapleCharacter.getPosition()).x && (mapleCharacter.getPosition()).y > -15
                                        && mapleCharacter.isAlive()) {
                                    int percent = (monster.getId() >= 8800023 && monster.getId() <= 8800030) ? 50 : ((monster.getId() >= 8800003 && monster.getId() <= 8800010) ? 90 : ((monster.getId() >= 8800103 && monster.getId() <= 8800110) ? 100 : 0));
                                    mapleCharacter.getPercentDamage(monster, this.skillId, this.skillLevel, percent, true);
                                }
                            }
                            break;
                        case 27:
                            type = monster.getId() % 10;
                            for (MapleCharacter mapleCharacter : player.getMap().getAllCharactersThreadsafe()) {
                                if (mapleCharacter.isAlive()) {
                                    boolean damage = false;
                                    if (type == 3 || type == 7) {
                                        if (mapleCharacter.getFH() == 20 || mapleCharacter.getFH() == 19 || mapleCharacter.getFH() == 18 || mapleCharacter.getFH() == 15 || mapleCharacter.getFH() == 16 || mapleCharacter.getFH() == 17) {
                                            damage = true;
                                        }
                                    } else if (type == 4 || type == 8) {
                                        if (mapleCharacter.getFH() == 14 || mapleCharacter.getFH() == 13 || mapleCharacter.getFH() == 12 || mapleCharacter.getFH() == 11 || mapleCharacter.getFH() == 10 || mapleCharacter.getFH() == 9) {
                                            damage = true;
                                        }
                                    } else if ((type == 5 || type == 9) && (mapleCharacter.getFH() == 5 || mapleCharacter.getFH() == 4 || mapleCharacter.getFH() == 3 || mapleCharacter.getFH() == 6 || mapleCharacter.getFH() == 7 || mapleCharacter.getFH() == 8)) {
                                        damage = true;
                                    }
                                    if (damage) {
                                        mapleCharacter.getPercentDamage(monster, this.skillId, this.skillLevel, 100, true);
                                    }
                                }
                            }
                            break;
                    }
                    if (rId != 0 && monster != null && player != null) {
                        for (MapleMapObject remo : monster.getMap().getAllReactorsThreadsafe()) {
                            MapleReactor react = (MapleReactor) remo;
                            if (react.getReactorId() == rId) {
                                react.forceHitReactor((byte) 1, player.getId());
                                break;
                            }
                        }
                    }
                    if ((monster.getId() == 8860000 || monster.getId() == 8860005)
                            && this.skillLevel >= 1 && this.skillLevel <= 4) {
                        value = this.x;
                        for (MapleMapObject reactor1l : player.getMap().getAllReactorsThreadsafe()) {
                            MapleReactor reactor2l = (MapleReactor) reactor1l;
                            if (reactor2l.getReactorId() == 2708001 && reactor2l.getState() == 0) {
                                reactor2l.forceHitReactor((byte) 1, player.getId());
                                break;
                            }
                            if (reactor2l.getReactorId() == 2708002 && reactor2l.getState() == 0) {
                                reactor2l.forceHitReactor((byte) 1, player.getId());
                                break;
                            }
                            if (reactor2l.getReactorId() == 2708003 && reactor2l.getState() == 0) {
                                reactor2l.forceHitReactor((byte) 1, player.getId());
                                break;
                            }
                            if (reactor2l.getReactorId() == 2708004 && reactor2l.getState() == 0) {
                                reactor2l.forceHitReactor((byte) 1, player.getId());
                                break;
                            }
                        }
                    }
                    if (value > 0) {
                        if (this.lt != null && this.rb != null && monster != null && player != null) {
                            Rectangle zone = calculateBoundingBox(new Point(0, 0), false);
                            for (MapleCharacter mapleCharacter : player.getMap().getAllCharactersThreadsafe()) {
                                if (zone.contains(mapleCharacter.getTruePosition())) {
                                    if (mapleCharacter.isAlive()) {
                                        if (mapleCharacter.getBuffedEffect(SecondaryStat.HolyMagicShell) != null) {
                                            if (mapleCharacter.getHolyMagicShell() > 1) {
                                                mapleCharacter.setHolyMagicShell((byte) (mapleCharacter.getHolyMagicShell() - 1));
                                                Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
                                                statups.put(SecondaryStat.HolyMagicShell, new Pair<>(Integer.valueOf(mapleCharacter.getHolyMagicShell()), Integer.valueOf((int) mapleCharacter.getBuffLimit(mapleCharacter.getBuffSource(SecondaryStat.HolyMagicShell)))));
                                                mapleCharacter.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, mapleCharacter.getBuffedEffect(SecondaryStat.HolyMagicShell), mapleCharacter));
                                                mapleCharacter.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(mapleCharacter, 1, 0, 36, 0, 0, (byte) 0, true, null, null, null));
                                                mapleCharacter.getMap().broadcastMessage(mapleCharacter, CField.EffectPacket.showEffect(mapleCharacter, 1, 0, 36, 0, 0, (byte) 0, false, null, null, null), false);
                                            } else {
                                                mapleCharacter.cancelEffectFromBuffStat(SecondaryStat.HolyMagicShell);
                                            }
                                        } else if (mapleCharacter.getBuffedValue(4341052)) {
                                            mapleCharacter.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(mapleCharacter, 1, 0, 36, 0, 0, (byte) 0, true, null, null, null));
                                            mapleCharacter.getMap().broadcastMessage(mapleCharacter, CField.EffectPacket.showEffect(mapleCharacter, 1, 0, 36, 0, 0, (byte) 0, false, null, null, null), false);
                                        } else if (mapleCharacter.getBuffedEffect(SecondaryStat.WindWall) != null) {
                                            int windWall = Math.max(0, mapleCharacter.getBuffedValue(SecondaryStat.WindWall).intValue() - 100 * mapleCharacter.getBuffedEffect(SecondaryStat.WindWall).getZ());
                                            if (windWall > 1) {
                                                mapleCharacter.setBuffedValue(SecondaryStat.WindWall, windWall);
                                                Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
                                                statups.put(SecondaryStat.WindWall, new Pair<>(Integer.valueOf(windWall), Integer.valueOf((int) mapleCharacter.getBuffLimit(400031030))));
                                                mapleCharacter.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, mapleCharacter.getBuffedEffect(SecondaryStat.WindWall), mapleCharacter));
                                            } else {
                                                mapleCharacter.cancelEffectFromBuffStat(SecondaryStat.WindWall);
                                            }
                                        } else if (mapleCharacter.getBuffedEffect(SecondaryStat.TrueSniping) == null && mapleCharacter.getBuffedEffect(SecondaryStat.Etherealform) == null && mapleCharacter.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null && mapleCharacter.getBuffedEffect(SecondaryStat.NotDamaged) == null) {
                                            int reduce = 0;
                                            if (mapleCharacter.getBuffedEffect(SecondaryStat.IndieDamageReduce) != null) {
                                                reduce = mapleCharacter.getBuffedValue(SecondaryStat.IndieDamageReduce).intValue();
                                            } else if (mapleCharacter.getBuffedEffect(SecondaryStat.IndieDamReduceR) != null) {
                                                reduce = -mapleCharacter.getBuffedValue(SecondaryStat.IndieDamReduceR).intValue();
                                            }
                                            System.out.println(value);
                                            if (reduce > 0) {
                                                value -= value * reduce / 100;
                                            }
                                            int minushp = -value;
                                            mapleCharacter.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(mapleCharacter, this.skillId, this.skillLevel, 45, 0, 0, (byte) 0, true, null, null, null));
                                            mapleCharacter.getMap().broadcastMessage(mapleCharacter, CField.EffectPacket.showEffect(mapleCharacter, this.skillId, this.skillLevel, 45, 0, 0, (byte) 0, false, null, null, null), false);
                                            mapleCharacter.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(mapleCharacter, 0, minushp, 36, 0, 0, (byte) 0, true, null, null, null));
                                            mapleCharacter.getMap().broadcastMessage(mapleCharacter, CField.EffectPacket.showEffect(mapleCharacter, 0, minushp, 36, 0, 0, (byte) 0, false, null, null, null), false);
                                            mapleCharacter.addHP(minushp);
                                        }
                                    }
                                    mapleCharacter.getClient().getSession().writeAndFlush(CField.screenAttack(monster.getId(), this.skillId, this.skillLevel, value));
                                }
                            }
                        }
                    }
                    break;
                case 177:
                    diseases.put(SecondaryStat.VenomSnake, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 179:
                    if (monster.getId() == 8870000) {
                        player.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 3, 0));
                    }
                    setDuration(10000L);
                    diseases.put(SecondaryStat.PainMark, new Pair<>(Integer.valueOf(this.skillLevel), Integer.valueOf((int) this.duration)));
                    break;
                case 180:
                    diseases.put(SecondaryStat.VampDeath, new Pair<>(Integer.valueOf((int) this.duration), Integer.valueOf((int) this.duration)));
                    diseases.put(SecondaryStat.VampDeathSummon, new Pair<>(Integer.valueOf((int) this.duration), Integer.valueOf((int) this.duration)));
                    break;
                case 181:
                    allchr = true;
                    m = SecondaryStat.Magnet;
                    m.setX((monster.getPosition()).x);
                    diseases.put(m, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    m = SecondaryStat.MagnetArea;
                    m.setX((monster.getPosition()).x);
                    diseases.put(m, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 182:
                    diseases.put(SecondaryStat.GiveMeHeal, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 183:
                    cancels.add(SecondaryStat.FireBomb);
                    if (player.getDebuffValue(SecondaryStat.FireBomb).intValue() == 1) {
                        diseases.put(SecondaryStat.FireBomb, new Pair<>(Integer.valueOf(2), Integer.valueOf((int) this.duration)));
                        break;
                    }
                    if (player.getDebuffValue(SecondaryStat.FireBomb).intValue() == 2) {
                        diseases.put(SecondaryStat.FireBomb, new Pair<>(Integer.valueOf(3), Integer.valueOf((int) this.duration)));
                        break;
                    }
                    if (player.getDebuffValue(SecondaryStat.FireBomb).intValue() <= 2) {
                        diseases.put(SecondaryStat.FireBomb, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) this.duration)));
                    }
                    break;
                case 184:
                    diseases.put(SecondaryStat.ReturnTeleport, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) this.duration)));
                    break;
                case 186:
                    if (this.skillLevel == 1 || this.skillLevel == 4) {
                        List<MapleCharacter> players = new ArrayList<>();
                        players.addAll(monster.getMap().getAllChracater());
                        Collections.addAll(players, new MapleCharacter[0]);
                        Collections.shuffle(players);
                        for (MapleCharacter chr5 : players) {
                            MapleMist mapleMist = new MapleMist(calculateBoundingBox(new Point((chr5.getPosition()).x, (monster.getPosition()).y), chr5.isFacingLeft()), monster, this, (int) getDuration());
                            mapleMist.setPosition(new Point((chr5.getPosition()).x, (monster.getPosition()).y));
                            if (mapleMist != null) {
                                monster.getMap().spawnMist(mapleMist, false);
                                break;
                            }
                        }
                        break;
                    }
                    if (this.skillLevel == 2) {
                        MapleMist mapleMist = new MapleMist(calculateBoundingBox(monster.getPosition(), monster.isFacingLeft()), monster, this, (int) getDuration());
                        mapleMist.setPosition(monster.getPosition());
                        if (mapleMist != null) {
                            monster.getMap().spawnMist(mapleMist, false);
                        }
                        break;
                    }
                    if (this.skillLevel == 3) {
                        MapleMist mapleMist = new MapleMist(new Rectangle(-1870, 133, 1150, 463), monster, this, (int) getDuration());
                        Point point = isFacingLeft ? new Point(965, 443) : new Point(-1750, 443);
                        mapleMist.setPosition(point);
                        mapleMist.setCustomx(isFacingLeft ? 645 : -1375);
                        mapleMist.setOwnerId(monster.getObjectId());
                        monster.getMap().spawnMist(mapleMist, false);
                        break;
                    }
                    if (this.skillLevel == 5) {
                        List<Point> list2 = new ArrayList<>();
                        list2.add(new Point(810, 443));
                        list2.add(new Point(-2190, 443));
                        list2.add(new Point(-1690, 443));
                        list2.add(new Point(560, 443));
                        list2.add(new Point(-190, 443));
                        list2.add(new Point(-690, 443));
                        list2.add(new Point(-1940, 443));
                        list2.add(new Point(1310, 443));
                        list2.add(new Point(-1190, 443));
                        list2.add(new Point(1060, 443));
                        list2.add(new Point(-940, 443));
                        list2.add(new Point(-1440, 443));
                        list2.add(new Point(1560, 443));
                        list2.add(new Point(-440, 443));
                        monster.getMap().broadcastMessage(MobPacket.dropStone("DropStone", list2));
                        MapleMist mapleMist = new MapleMist(new Rectangle(-1695, -357, 365, 463), monster, this, (int) getDuration());
                        Point pos3 = isFacingLeft ? new Point(965, 443) : new Point(-1750, 443);
                        mapleMist.setPosition(pos3);
                        mapleMist.setCustomx(isFacingLeft ? -1035 : 600);
                        mapleMist.setOwnerId(monster.getObjectId());
                        monster.getMap().spawnMist(mapleMist, false);
                        break;
                    }
                    if (this.skillLevel == 6) {
                        MapleMist mapleMist = new MapleMist(new Rectangle(-1870, 133, 1150, 463), monster, this, (int) getDuration());
                        Point point = isFacingLeft ? new Point(965, 443) : new Point(-1750, 443);
                        mapleMist.setPosition(point);
                        mapleMist.setCustomx(isFacingLeft ? -1035 : 600);
                        mapleMist.setOwnerId(monster.getObjectId());
                        monster.getMap().spawnMist(mapleMist, false);
                        break;
                    }
                    if (this.skillLevel == 11) {
                        if (monster.getCustomValue0(18611) == 0L) {
                            monster.setNextSkill(0);
                            monster.setNextSkillLvl(0);
                            return;
                        }
                        monster.setCustomInfo(18611, 0, 0);
                        server.Timer.MapTimer.getInstance().schedule(() -> {
                            MapleMist DustMist = new MapleMist(new Rectangle(-664, -940, 1387, 803), monster, this, (int) getDuration());
                            DustMist.setPosition(new Point(-45, -157));
                            if (DustMist != null) {
                                monster.getMap().spawnMist(DustMist, true);
                            }
                        },
                                2500L);
                        break;
                    }
                    if (player != null && this.lt != null && this.rb != null) {
                        MapleMist mapleMist = new MapleMist(calculateBoundingBox(player.getTruePosition(), player.isFacingLeft()), monster, this, (int) this.duration);
                        if (mapleMist != null) {
                            monster.getMap().spawnMist(mapleMist, true);
                        }
                    }
                    break;
                case 188:
                    this.duration = 30000L;
                    diseases.put(SecondaryStat.Stance, new Pair<>(Integer.valueOf(100), Integer.valueOf((int) this.duration)));
                    diseases.put(SecondaryStat.Attract, new Pair<>(Integer.valueOf(((monster.getPosition()).x < (player.getPosition()).x) ? 1 : 2), Integer.valueOf((int) this.duration)));
                    diseases.put(SecondaryStat.Slow, new Pair<>(Integer.valueOf(50), Integer.valueOf((int) this.duration)));
                    player.getClient().send(CField.ChangeFaceMotion(17, 30000));
                    break;
                case 189:
                    diseases.put(SecondaryStat.CapDebuff, new Pair<>(Integer.valueOf(100), Integer.valueOf((int) this.duration)));
                    break;
                case 190:
                    diseases.put(SecondaryStat.CapDebuff, new Pair<>(Integer.valueOf(200), Integer.valueOf((int) this.duration)));
                    break;
                case 191:
                    clockPos = new Point(Randomizer.rand(-1063, 720), (monster.getTruePosition()).y);
                    box = calculateBoundingBox(monster.getTruePosition(), true);
                    clock = new MapleMist(box, monster, this, (int) this.duration);
                    clock.setPosition(clockPos);
                    monster.getMap().spawnMist(clock, false);
                    monster.getMap().broadcastMessage(CWvsContext.getTopMsg("시간의 틈새에 '균열'이 발생하였습니다."));
                    break;
                case 200:
                case 201:
                    if (monster == null) {
                        return;
                    }
                    if ((this.skillId == 201 && this.skillLevel == 40) || (this.skillId == 201 && this.skillLevel == 237) || this.skillLevel == 162) {
                        return;
                    }
                    if (this.skillId == 200) {
                        if (this.skillLevel == 228) {
                            monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 2, 8, "시그너스가 상대의 행동에 분노를 느낍니다."));
                            monster.getMap().broadcastMessage(CWvsContext.getTopMsg("시그너스가 상대의 행동에 분노를 느낍니다."));
                        } else if (this.skillLevel == 223) {
                            monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 2, 2, "시그너스가 자신의 기사단을 위해 신수를 소환합니다."));
                            monster.getMap().broadcastMessage(CWvsContext.getTopMsg("시그너스가 자신의 기사단을 위해 신수를 소환합니다."));
                        }
                    } else if (this.skillId == 201) {
                        if (this.skillLevel == 160) {
                            monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 2, 8, "시그너스가 상대의 행동에 분노를 느낍니다."));
                        } else if (this.skillLevel == 159) {
                            monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 2, 2, "시그너스가 자신의 기사단을 위해 신수를 소환합니다."));
                        }
                    }
                    if (this.skillId == 201 && this.skillLevel == 199) {
                        this.toSummon.clear();
                        if (Randomizer.nextBoolean()) {
                            this.toSummon.add(Integer.valueOf(8880170));
                        } else {
                            this.toSummon.add(Integer.valueOf(8880175));
                            this.toSummon.add(Integer.valueOf(8880178));
                            this.toSummon.add(Integer.valueOf(8880179));
                        }
                    }
                    if (this.skillId == 201 && this.skillLevel == 211) {
                        monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 1, 5, 0, monster.getStats().getName() + "이 적들의 잠재능력에 위협을 느껴 무효화 된 틈을타 부하 몬스터를 소환합니다."));
                    } else if ((this.skillId == 200 && this.skillLevel == 212) || (this.skillId == 201 && this.skillLevel == 210)) {
                        monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, monster.getStats().getName() + "이 주변에 체력을 흡수할 몬스터가 없어 새롭게 소환을 시도합니다."));
                        Timer.MapTimer.getInstance().schedule(() -> {
                            if (monster.getController() != null && this.skillId == 201 && this.skillLevel == 210) {
                                monster.setNextSkill(105);
                                monster.setNextSkillLvl(13);
                            }
                        },
                                5000L);
                    }
                    i22 = 0;
                    for (Integer mobId : getSummons()) {
                        MapleMonster toSpawn = null;
                        try {
                            toSpawn = MapleLifeFactory.getMonster(GameConstants.getCustomSpawnID(monster.getId(), mobId.intValue()));
                        } catch (RuntimeException e) {
                            continue;
                        }
                        if (toSpawn == null || toSpawn.getStats().getName().contains("악몽의 골렘") || toSpawn.getStats().getName().equals("악몽의 골렘")) {
                            continue;
                        }
                        if (toSpawn.getStats().getHp() < toSpawn.getHp()) {
                            toSpawn.setHp(toSpawn.getStats().getHp());
                        }
                        toSpawn.setPosition(monster.getTruePosition());
                        int ypos = (int) monster.getTruePosition().getY(), xpos = (int) monster.getTruePosition().getX();
                        if (this.skillId == 200 && this.skillLevel == 251) {
                            stats.add(new Pair<>(MonsterStatus.MS_ExchangeAttack, new MonsterStatusEffect(200, 1000, 1L)));
                        } else if (this.skillId == 200 && this.skillLevel == 250) {
                            monster.setLastSkillUsed(this, System.currentTimeMillis(), (Randomizer.rand(10, 20) * 1000));
                        }
                        if (getAfterDead() > 0) {
                            toSpawn.setHp(monster.getHp());
                            for (MobSkill sk : toSpawn.getSkills()) {
                                if (sk.getAfterDead() > 0) {
                                    toSpawn.setLastSkillUsed(sk, System.currentTimeMillis(), 40000L);
                                }
                            }
                            if ((toSpawn.getId() >= 8920000 && toSpawn.getId() <= 8920003) || (toSpawn.getId() >= 8920100 && toSpawn.getId() <= 8920103)) {
                                if (toSpawn.getId() == 8920000 || toSpawn.getId() == 8920100) {
                                    player.getMap().broadcastMessage(CField.removeMapEffect());
                                    player.getMap().broadcastMessage(CField.startMapEffect("내가 상대해주겠어요.", 5120099, true));
                                } else if (toSpawn.getId() == 8920001 || toSpawn.getId() == 8920101) {
                                    player.getMap().broadcastMessage(CField.removeMapEffect());
                                    player.getMap().broadcastMessage(CField.startMapEffect("킥킥. 다 없애주지", 5120101, true));
                                } else if (toSpawn.getId() == 8920002 || toSpawn.getId() == 8920102) {
                                    player.getMap().broadcastMessage(CField.removeMapEffect());
                                    player.getMap().broadcastMessage(CField.startMapEffect("모두 불태워주마!", 5120100, true));
                                } else if (toSpawn.getId() == 8920003 || toSpawn.getId() == 8920103) {
                                    player.getMap().broadcastMessage(CField.removeMapEffect());
                                    player.getMap().broadcastMessage(CField.startMapEffect("내 고통을 느끼게 해줄게요.", 5120102, true));
                                }
                            }
                            monster.getMap().killMonster(monster, player, false, false, (byte) 0);
                        }
                        int deadtime = 0, deadtimekillmob = 0;
                        int randcount = 0, i6 = 0, maxx = 0, effect = 0;
                        boolean flying = false;
                        switch (mobId.intValue()) {
                            case 8920005:
                            case 8920105:
                                toSpawn.setLastSkillUsed(MobSkillFactory.getMobSkill(188, 1), System.currentTimeMillis(), 5000L);
                                xpos = (monster.getPosition()).x + Randomizer.rand(-500, 500);
                                if (monster.getMap().getLeft() > xpos) {
                                    xpos = monster.getMap().getLeft() + 50;
                                } else if (monster.getMap().getRight() < xpos) {
                                    xpos = monster.getMap().getRight() - 50;
                                }
                                ypos = (monster.getPosition()).y;
                                break;
                            case 8500003:
                                toSpawn.setFh((int) Math.ceil(Math.random() * 19.0D));
                                ypos = -590;
                                break;
                            case 8500004:
                                xpos = (int) (monster.getTruePosition().getX() + Math.ceil(Math.random() * 1000.0D) - 500.0D);
                                ypos = (int) monster.getTruePosition().getY();
                                break;
                            case 8510100:
                                if (Math.ceil(Math.random() * 5.0D) == 1.0D) {
                                    ypos = 78;
                                    xpos = (int) (0.0D + Math.ceil(Math.random() * 5.0D)) + ((Math.ceil(Math.random() * 2.0D) == 1.0D) ? 180 : 0);
                                    break;
                                }
                                xpos = (int) (monster.getTruePosition().getX() + Math.ceil(Math.random() * 1000.0D) - 500.0D);
                                break;
                            case 8820007:
                                continue;
                            case 8500013:
                                xpos = (monster.getPosition()).x + Randomizer.rand(-500, 500);
                                ypos = (monster.getPosition()).y + Randomizer.rand(-150, 150);
                                break;
                            case 8500009:
                            case 8500014:
                                deadtime = 6000;
                                ypos = 179;
                                if (i22 == 0) {
                                    xpos = -562;
                                    break;
                                }
                                if (i22 == 1) {
                                    xpos = -9;
                                    break;
                                }
                                if (i22 == 2) {
                                    xpos = 662;
                                }
                                break;
                            case 8870005:
                            case 8870105:
                                xpos = Randomizer.rand(-610, -550);
                                if (Randomizer.isSuccess(50)) {
                                    xpos = Randomizer.rand(350, 450);
                                }
                                ypos = 196;
                                break;
                            case 8610023:
                            case 8610024:
                            case 8610025:
                            case 8610026:
                            case 8610027:
                            case 8840015:
                            case 8870002:
                            case 8870103:
                            case 8870104:
                            case 8870106:
                            case 8870107:
                            case 8880201:
                                if (mobId.intValue() == 8880201) {
                                    deadtime = 30000;
                                }
                                xpos = (monster.getPosition()).x + Randomizer.rand(-500, 500);
                                if (monster.getMap().getLeft() > xpos) {
                                    xpos = monster.getMap().getLeft() + 50;
                                } else if (monster.getMap().getRight() < xpos) {
                                    xpos = monster.getMap().getRight() - 50;
                                }
                                ypos = (monster.getPosition()).y;
                                break;
                            case 8920004:
                                deadtime = 10000;
                                xpos = Randomizer.rand(-854, 928);
                                break;
                            case 8880157:
                            case 8880164:
                            case 8880184:
                            case 8880185:
                                deadtime = 26000;
                                if (toSpawn.getId() == 8880157 || toSpawn.getId() == 8880164) {
                                    xpos = Randomizer.rand(735, 1100);
                                }
                                monster.setLastSkillUsed(this, System.currentTimeMillis(), (Randomizer.rand(30, 40) * 1000));
                                break;
                            case 8880165:
                            case 8880168:
                            case 8880169:
                            case 8880175:
                            case 8880178:
                            case 8880179:
                                deadtime = 20000;
                                monster.setLastSkillUsed(this, System.currentTimeMillis(), (Randomizer.rand(10, 15) * 1000));
                                break;
                            case 8880315:
                            case 8880316:
                                flying = true;
                                deadtime = 10000;
                                break;
                            case 8880306:
                            case 8880308:
                                xpos = (monster.getMap().getMonsterById(mobId.intValue()) != null) ? -600 : 600;
                                ypos = 215;
                                deadtime = 23000;
                                break;
                            case 8880408:
                                monster.getMap().broadcastMessage(CField.enforceMSG("힐라가 죽음의 밑바닥에서 스우의 사령을 끌어올리는 소리가 들린다.", 254, 6000));
                                break;
                            case 8880409:
                                monster.getMap().broadcastMessage(CField.enforceMSG("힐라가 죽음의 밑바닥에서 데미안의 사령을 끌어올리는 소리가 들린다.", 254, 6000));
                                break;
                        }
                        if (this.skillLevel == 238) {
                            flying = true;
                            if (i22 == 0) {
                                xpos = -580;
                                ypos = 80;
                            } else if (i22 == 1) {
                                xpos = -450;
                                ypos = -250;
                            } else if (i22 == 2) {
                                xpos = 450;
                                ypos = -250;
                            } else if (i22 == 3) {
                                xpos = 580;
                                ypos = 80;
                            }
                        } else if (this.skillLevel == 239) {
                            flying = true;
                            if (i22 == 0) {
                                xpos = -580;
                                ypos = -2300;
                            } else if (i22 == 1) {
                                xpos = -450;
                                ypos = -2500;
                            } else if (i22 == 2) {
                                xpos = 450;
                                ypos = -2500;
                            } else if (i22 == 3) {
                                xpos = 580;
                                ypos = -2300;
                            }
                        } else if (this.skillLevel == 240) {
                            flying = true;
                            if (i22 == 0) {
                                xpos = -580;
                                ypos = -40;
                            } else if (i22 == 1) {
                                xpos = 0;
                                ypos = -40;
                            } else if (i22 == 2) {
                                xpos = 580;
                                ypos = -40;
                            }
                        }
                        if (monster.getStats().getName().contains("어둠의 집행자")) {
                            flying = true;
                        }
                        switch (monster.getMap().getId()) {
                            case 230040420:
                                if (xpos < -239) {
                                    xpos = (int) (-239.0D + Math.ceil(Math.random() * 150.0D));
                                    break;
                                }
                                if (xpos > 371) {
                                    xpos = (int) (371.0D - Math.ceil(Math.random() * 150.0D));
                                }
                                break;
                        }
                        if (this.skillLevel == 139 || this.skillLevel == 83) {
                            flying = true;
                            switch (toSpawn.getId() % 10) {
                                case 3:
                                    xpos = 507;
                                    ypos = -248;
                                    break;
                                case 4:
                                    xpos = -420;
                                    ypos = -421;
                                    break;
                                case 5:
                                    xpos = -511;
                                    ypos = -250;
                                    break;
                                case 7:
                                    xpos = 417;
                                    ypos = -423;
                                    break;
                            }
                        }
                        if (this.skillId == 201 && this.skillLevel == 49) {
                            xpos = (player.getTruePosition()).x;
                            ypos = (player.getTruePosition()).y;
                            deadtime = 5000;
                        }
                        int objectId = monster.getMap().spawnMonsterWithEffect(toSpawn, (effect > 0) ? -1 : ((getSpawnEffect() == 77) ? 1 : getSpawnEffect()), flying ? new Point(xpos, ypos) : monster.getMap().calcPointBelow(new Point(xpos, ypos - 1)));
                        if (isMobGroup()) {
                            if (!monster.getSpawnList().contains(Integer.valueOf(toSpawn.getId()))) {
                                toSpawn.setMobGroup(true);
                                monster.getSpawnList().add(Integer.valueOf(toSpawn.getId()));
                                break;
                            }
                            continue;
                        }
                        if (deadtime > 0) {
                            toSpawn.setDeadTime(deadtime);
                        } else if (deadtimekillmob > 0) {
                            toSpawn.setDeadTimeKillmob(deadtimekillmob);
                        }
                        if (mobId.intValue() == 8800117) {
                            Timer.MapTimer.getInstance().schedule(() -> {
                                if (monster.getController() != null && monster.getMap().getMonsterById(8800117) != null) {
                                    monster.getMap().killMonster(8800117);
                                }
                            },
                                    10000L
                            );
                            i22++;
                        }
                    }
                    break;
                case 203:
                    target = ChannelServer.getInstance(player.getClient().getChannel()).getMapFactory().getMap(monster.getMap().getId() + 10);
                    target.resetFully();
                    target.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(monster.getId() + 1), new Point(Randomizer.rand(-1026, 690), 245));
                    rand = Randomizer.rand(1, 9);
                    monster.setCustomInfo(8910000, 0, 20000);
                    monster.getMap().setCustomInfo(8910000, rand, 0);
                    monster.getMap().broadcastMessage(CField.environmentChange("Pt0" + rand + "gate", 2));
                    for (MapleCharacter mapleCharacter : monster.getMap().getAllChracater()) {
                        mapleCharacter.setSkillCustomInfo(8910000, 0L, 20000L);
                    }
                    monster.getMap().broadcastMessage(CField.VonVonStopWatch(20000));
                    break;
                case 211:
                    mist3 = new MapleMist(calculateBoundingBox(monster.getPosition(), monster.isFacingLeft()), monster, this, 30000);
                    mist3.setPosition(monster.getPosition());
                    monster.getMap().spawnMist(mist3, false);
                    break;
                case 212:
                    stats.add(new Pair<>(MonsterStatus.MS_Pad, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 213:
                    switch (this.skillLevel) {
                        case 10:
                            setMobSkillDelay(player, monster, 2220, (short) 0, isFacingLeft);
                            break;
                        case 13:
                            setMobSkillDelay(player, monster, 2040, (short) 0, isFacingLeft);
                            break;
                        case 14:
                            setMobSkillDelay(player, monster, 1950, (short) 0, isFacingLeft);
                            break;
                        case 15:
                            setMobSkillDelay(player, monster, 2160, (short) 0, isFacingLeft);
                            break;
                    }
                    break;
                case 214:
                    time = 0;
                    if (this.skillLevel == 13) {
                        time = 30000;
                        if (monster.getId() >= 8800130 && monster.getId() <= 8800137) {
                            Timer.MapTimer.getInstance().schedule(() -> {
                                MapleMonster zakum = monster.getMap().getMonsterById(8800102);
                                if (zakum != null && zakum.getPhase() <= 2) {
                                    monster.getMap().killMonster(monster.getId());
                                    MapleMonster part = MapleLifeFactory.getMonster(monster.getId() - 27);
                                    part.setF(1);
                                    part.setFh(1);
                                    part.setStance(5);
                                    part.setPhase((byte) 1);
                                    monster.getMap().spawnMonsterWithEffectBelow(part, monster.getPosition(), -2);
                                }
                            }, time);
                        }
                        player.getMap().broadcastMessage(MobPacket.demianRunaway(monster, (byte) 0, this, time, false));
                        break;
                    }
                    if (this.skillLevel == 14);
                    break;
                case 215:
                    monster.setNextSkill(0);
                    monster.setNextSkillLvl(0);
                    monster.setLastSkillUsed(this, System.currentTimeMillis(), 30000L);
                    monster.setSkillForbid(true);
                    for (z = 1; z <= 4; z++) {
                        Timer.MobTimer.getInstance().schedule(() -> {
                            monster.setNextSkill(0);
                            monster.setNextSkillLvl(0);
                            int[] obsplusx = {379, -5, -388};
                            int[] angle = {642, 514, 642};
                            int[] unk = {323, 0, 36};
                            List<Obstacle> obs2 = new ArrayList<>();
                            for (int iz = 0; iz < 3; iz++) {
                                Obstacle ob = new Obstacle((this.skillLevel == 2) ? 58 : 59, new Point((monster.getPosition()).x, -505), new Point((monster.getPosition()).x + obsplusx[iz], (this.skillLevel == 2) ? 16 : 17), 50, 90, 539, 105, 1, angle[iz], unk[iz]);
                                obs2.add(ob);
                            }
                            player.getMap().broadcastMessage(MobPacket.createObstacle(monster, obs2, (byte) 4));
                        }, (z * 2000 - 1000));
                        server.Timer.MobTimer.getInstance().schedule(() -> {
                            monster.setNextSkill(0);
                            monster.setNextSkillLvl(0);
                            int[] obsplusx = {496, 207, 0, -208, -497};
                            int[] angle = {714, 553, 514, 555, 714};
                            int[] unk = {316, 338, 0, 22, 44};
                            List<Obstacle> obs2 = new ArrayList<>();
                            for (int iz = 0; iz < 5; iz++) {
                                Obstacle ob = new Obstacle((this.skillLevel == 2) ? 58 : 59, new Point((monster.getPosition()).x, -505), new Point((monster.getPosition()).x + obsplusx[iz], (this.skillLevel == 2) ? 16 : 17), 50, 90, 964, 76, 1, angle[iz], unk[iz]);
                                obs2.add(ob);
                            }
                            player.getMap().broadcastMessage(MobPacket.createObstacle(monster, obs2, (byte) 4));
                        }, (z * 2000));
                    }
                    Timer.MobTimer.getInstance().schedule(() -> {
                        if (monster != null) {
                            monster.setNextSkill(0);
                            monster.setNextSkillLvl(0);
                            monster.setSkillForbid(false);
                        }
                    }, 11000L);
                    break;
                case 217:
                    setMobSkillDelay(player, monster, 1920, (short) 0, isFacingLeft);
                    break;
                case 220:
                    stats.add(new Pair<>(MonsterStatus.MS_PImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_MImmune, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_PCounter, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    stats.add(new Pair<>(MonsterStatus.MS_MCounter, new MonsterStatusEffect(this.skillId, (int) this.duration, this.x)));
                    break;
                case 221:
                    diseases.put(SecondaryStat.Seal, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                case 223:
                    if (!monster.isBuffed(MonsterStatus.MS_Laser)) {
                        monster.setCustomInfo(2286, 0, 60000);
                        monster.setCustomInfo(22878, 0, Randomizer.rand(60000, 70000));
                        monster.setEnergyspeed(1);
                        monster.setEnergyleft(false);
                        monster.setEnergycount(45);
                        monster.setLastSkillUsed(this, System.currentTimeMillis(), 10000L);
                        stats.add(new Pair<>(MonsterStatus.MS_Laser, new MonsterStatusEffect(this.skillId, (int) this.duration)));
                        break;
                    }
                    for (MobSkill sk : monster.getSkills()) {
                        if (sk.getSkillId() == 201) {
                            sk.applyEffect(player, monster, true, isFacingLeft);
                            return;
                        }
                    }
                    monster.setLastSkillUsed(this, System.currentTimeMillis(), Randomizer.rand(5000, 10000));
                    break;
                case 226:
                    player.getMap().broadcastMessage(CField.airBone(player, monster, this.skillId, this.skillLevel, -1400));
                    break;
                case 227:
                    Swoomist = new MapleMist(calculateBoundingBox(monster.getTruePosition(), true), monster, this, (int) this.duration);
                    if (Swoomist != null) {
                        Swoomist.setPosition(monster.getPosition());
                        monster.getMap().spawnMist(Swoomist, false);
                    }
                    break;
                case 228:
                    switch (this.skillLevel) {
                        case 6:
                            monster.setSkillForbid(true);
                            monster.setEnergyspeed(0);
                            monster.getMap().broadcastMessage(MobPacket.LaserHandler(monster.getObjectId(), monster.getEnergycount(), monster.getEnergyspeed(), monster.isEnergyleft() ? 0 : 1));
                            Timer.MobTimer.getInstance().schedule(() -> {
                                monster.setSkillForbid(false);
                                monster.setEnergyspeed(1);
                                monster.setEnergyleft(!monster.isEnergyleft());
                                monster.getMap().broadcastMessage(MobPacket.LaserHandler(monster.getObjectId(), monster.getEnergycount(), monster.getEnergyspeed(), monster.isEnergyleft() ? 0 : 1));
                            }, 3000L);
                            break;
                        case 7:
                            monster.setEnergyspeed(2);
                            monster.setCustomInfo(2287, 0, 30000);
                            monster.setCustomInfo(22878, 0, Randomizer.rand(60000, 70000));
                            Timer.MobTimer.getInstance().schedule(() -> monster.setEnergyspeed(Randomizer.rand(3, 4)),
                                    Randomizer.rand(5000, 15000));
                            Timer.MobTimer.getInstance().schedule(() -> {
                                monster.setEnergycount(monster.getEnergycount() + (monster.isEnergyleft() ? -(9 * monster.getEnergyspeed()) : (9 * monster.getEnergyspeed())));
                                monster.setEnergyspeed(1);
                            }, 30000L);
                            break;
                    }
                case 230:
                    if (monster != null
                            && !monster.getRectangles().isEmpty()) {
                        Rectangle rect = monster.getRectangles().get(Integer.valueOf(RectCount));
                        for (MapleCharacter chrs : monster.getMap().getAllChracater()) {
                            if (chrs != null
                                    && chrs.isAlive()
                                    && rect.x - 50 < (chrs.getPosition()).x && rect.x + 150 > (chrs.getPosition()).x && -16 >= (chrs.getPosition()).y && -316 <= (chrs.getPosition()).y) {
                                chrs.getPercentDamage(monster, this.skillId, this.skillLevel, 100, true);
                            }
                        }
                    }
                    break;
                    case 231: {
                        break;
                    }
                    case 232: {
                        break;
                    }
                case 234:
                    this.duration = Randomizer.rand(5000, 10000);
                    diseases.put(SecondaryStat.Contagion, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                    break;
                    case 235: {
                        break;
                    }
                    case 237: {
                        this.duration = 0L;
                        ++player.Stigma;
                        if (player.Stigma >= 7) {
                            player.Stigma = 7;
                        }
                        dds = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                        dds.put(SecondaryStat.Stigma, new Pair<Integer, Integer>(player.Stigma, 0));
                        player.getClient().getSession().writeAndFlush((Object)CWvsContext.BuffPacket.giveDisease(dds, this, player));
                        player.getMap().broadcastMessage(player, CWvsContext.BuffPacket.giveForeignDeBuff(player, dds), false);
                        player.getMap().broadcastMessage(MobPacket.StigmaImage(player, false));
                        player.getClient().getSession().writeAndFlush((Object)SLFCGPacket.playSE("SoundEff/BossDemian/incStigma"));
                        if (player.Stigma == 7 && player.getBuffedEffect(SecondaryStat.NotDamaged) == null && player.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
                            player.Stigma = 0;
                            player.getMap().setStigmaDeath(player.getMap().getStigmaDeath() + 1);
                            player.playerIGDead();
                            player.getMap().broadcastMessage(MobPacket.CorruptionChange((byte)0, player.getMap().getStigmaDeath()));
                            player.getMap().broadcastMessage(CField.environmentChange("Effect/OnUserEff.img/demian/screen", 4));
                            if (monster.getId() != 8880111 && monster.getId() != 8880101) {
                                player.getMap().broadcastMessage(CField.enforceMSG("낙인이 완성되어 데미안이 점점 어둠에 물들어 갑니다.", 216, 30000000));
                            }
                            Timer.MobTimer.getInstance().schedule(() -> {
                                if (monster.isAlive()) {
                                    player.getMap().broadcastMessage(CField.enforceMSG(monster.getSpecialtxt(), 216, 30000000));
                                }
                            }, 3000L);
                            player.getClient().getSession().writeAndFlush((Object)CWvsContext.BuffPacket.cancelBuff(dds, player));
                            player.getMap().broadcastMessage(player, CWvsContext.BuffPacket.cancelForeignBuff(player, dds), false);
                            if (player.getMap().getStigmaDeath() >= 7 && monster.getId() != 8880111 && monster.getId() != 8880101) {
                                monster.DemainChangePhase(player);
                            } else {
                                MapleFlyingSword mapleFlyingSword = new MapleFlyingSword(1, monster);
                                player.getMap().spawnFlyingSword(mapleFlyingSword);
                                player.getMap().setNewFlyingSwordNode(mapleFlyingSword, monster.getTruePosition());
                            }
                        }
                        return;
                    }
                case 238:
                    switch (this.skillLevel) {
                        case 1:
                        case 2:
                        case 3:
                            player.getMap().broadcastMessage(MobPacket.BossLucid.doFlowerTrapSkill(this.skillLevel, Randomizer.nextInt(3), 1000, 48, Randomizer.nextBoolean()));
                            break;
                        case 4:
                            case 10: {
                                ArrayList<FairyDust> arrayList = new ArrayList<FairyDust>(){
                                    {
                                        int x;
                                        int w2;
                                        int w;
                                        int v2;
                                        int v;
                                        int s2;
                                        int s;
                                        int u = 2640;
                                        if (MobSkill.this.skillLevel == 4) {
                                            s = 180;
                                            s2 = 240;
                                            v = 100;
                                            v2 = 5;
                                            w = 3;
                                            w2 = 1;
                                            x = 40;
                                        } else {
                                            s = 30;
                                            s2 = 330;
                                            v = 250;
                                            v2 = 100;
                                            w = 6;
                                            w2 = 3;
                                            x = 5;
                                        }
                                        int max = Randomizer.rand(w, w + w2);
                                        for (int i = 0; i < max; ++i) {
                                            x += Randomizer.nextInt(x);
                                            this.add(new FairyDust(Randomizer.nextInt(3), u, v + Randomizer.nextInt(v2), x + Randomizer.rand(s, s2)));
                                        }
                                    }
                                };
                                monster.setLastSkillUsed(this, System.currentTimeMillis(), Randomizer.rand(10, 20) * 1000);
                                player.getMap().broadcastMessage(MobPacket.BossLucid.doFairyDustSkill(this.skillLevel, (List<FairyDust>)arrayList));
                                player.getMap().broadcastMessage(CField.enforceMSG("저 바람을 맞으면 꿈이 더 강해질 겁니다!", 222, 2000));
                                break;
                            }
                        case 5:
                             laserIntervals = new ArrayList<Integer>() {
                                    {
                                        for (int i = 0; i < 15; ++i) {
                                            this.add(500);
                                        }
                                    }
                                };
                            monster.setCustomInfo(23807, 0, 10000);
                            player.getMap().broadcastMessage(MobPacket.BossLucid.doLaserRainSkill(4500, laserIntervals));
                            player.getMap().broadcastMessage(CField.enforceMSG("루시드가 강력한 공격을 사용하려 합니다!", 222, 2000));
                            break;
                        case 6:
                            player.getClient().getSession().writeAndFlush(MobPacket.BossLucid.doForcedTeleportSkill(Randomizer.nextInt(8)));
                            break;
                        case 7:
                            isLeft = Randomizer.isSuccess(50);
                            if (monster.getMap().getId() == 450004150 || monster.getMap().getId() == 450004450 || monster.getMap().getId() == 450003840) {
                                player.getMap().broadcastMessage(MobPacket.BossLucid.createDragon(1, 0, 0, 0, 0, isLeft));
                            } else {
                                int createPosX = isLeft ? -138 : 1498;
                                int createPosY = Randomizer.nextBoolean() ? -1312 : 238;
                                int posX = createPosX;
                                int posY = (monster.getPosition()).y;
                                player.getMap().broadcastMessage(MobPacket.BossLucid.createDragon(2, posX, posY, createPosX, createPosY, isLeft));
                            }
                            monster.setCustomInfo(23807, 0, 20000);
                            player.getMap().broadcastMessage(CField.enforceMSG("루시드가 강력한 소환수를 소환했습니다!", 222, 2000));
                            break;
                        case 8:
                            monster.setCustomInfo(23807, 0, 10000);
                            player.getMap().broadcastMessage(CField.enforceMSG("루시드가 강력한 공격을 사용하려 합니다!", 222, 2000));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.doRushSkill());
                            break;
                        case 9:
                            for (MapleMonster mob : monster.getMap().getAllMonster()) {
                                if (mob.getId() != 8880150 && mob.getId() != 8880151 && mob.getId() != 8880155) {
                                    mob.getMap().killMonsterType(mob, 0);
                                }
                            }
                            monster.removeCustomInfo(8880140);
                            player.getMap().broadcastMessage(MobPacket.BossLucid.RemoveButterfly());
                            player.getMap().broadcastMessage(MobPacket.BossLucid.setStainedGlassOnOff(false, FieldLucid.STAINED_GLASS));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.setButterflyAction(Butterfly.Mode.MOVE, new int[]{600, -500}));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.changeStatueState(true, 0, false));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.setFlyingMode(true));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.doBidirectionShoot(50, 20, 100000, 8));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.doSpiralShoot(4, 390, 225, 13, 3, 3500, 10, 10, 1));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.doSpiralShoot(5, 10, 15, 20, 40, 4000, 13, 0, 0));
                            player.getMap().broadcastMessage(MobPacket.BossLucid.doWelcomeBarrageSkill(2));
                            monster.setCustomInfo(23888, 1, 0);
                            monster.setCustomInfo(23807, 0, 15700);
                            Timer.MobTimer.getInstance().schedule(() -> {
                                monster.setCustomInfo(23888, 0, 0);
                                player.getMap().broadcastMessage(MobPacket.BossLucid.setStainedGlassOnOff(true, FieldLucid.STAINED_GLASS));
                                player.getMap().broadcastMessage(MobPacket.BossLucid.setFlyingMode(false));
                                player.getMap().broadcastMessage(MobPacket.BossLucid.changeStatueState(true, 0, true));
                                for (MapleCharacter chr1 : player.getMap().getAllCharactersThreadsafe()) {
                                    chr1.getClient().send(CField.fireBlink(chr1.getId(), new Point(834, -573)));
                                }
                            },
                                    15700L);
                            break;
                        case 12:
                            this.duration = Randomizer.rand(7000, 10000);
                            diseases.put(SecondaryStat.Contagion, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                            break;
                    }
                    break;
                case 241:
                    switch (this.skillLevel) {
                        case 1:
                        case 2:
                            this.duration = 60000L;
                            monster.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 0, 0));
                            monster.setCustomInfo(2412, 0, 60000);
                            stats.add(new Pair<>(MonsterStatus.MS_PopulatusTimer, new MonsterStatusEffect(this.skillId, (int) this.duration, 1L)));
                            ms1 = MobSkillFactory.getMobSkill(241, 3);
                            j = Randomizer.rand(20000, 25000);
                            ms1.setDuration(j);
                            for (MapleCharacter achr : monster.getMap().getAllCharactersThreadsafe()) {
                                achr.setSkillCustomInfo(241, 0L, j);
                                achr.giveDebuff(SecondaryStat.PapulCuss, ms1);
                            }
                            abd = 0;
                            for (Spawns spawnPoint : (monster.getMap()).monsterSpawn) {
                                int i6 = Randomizer.rand(8500007, 8500008);
                                MapleMonster monster3 = MapleLifeFactory.getMonster(i6);
                                monster.getMap().spawnMonsterOnGroundBelow(monster3, spawnPoint.getPosition());
                                abd++;
                                if (abd == 7) {
                                    break;
                                }
                            }
                            break;
                        case 4:
                            chr3 = monster.getController().getClient().getRandomCharacter();
                            if (chr3 != null) {
                                chr3.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr3, this.skillLevel, this.skillId, 73, 0, 0, (byte) 0, true, null, null, null));
                                chr3.getMap().broadcastMessage(chr3, CField.EffectPacket.showEffect(chr3, this.skillLevel, this.skillId, 73, 0, 0, (byte) 0, false, null, null, null), false);
                                Timer.MobTimer.getInstance().schedule(() -> {
                                    chr3.getClient().send(CField.onUserTeleport(Randomizer.rand(-880, 1030), 100));
                                    chr3.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr3, this.skillLevel, this.skillId, 73, 1, 0, (byte) 0, true, null, null, null));
                                    chr3.getMap().broadcastMessage(chr3, CField.EffectPacket.showEffect(chr3, this.skillLevel, this.skillId, 73, 1, 0, (byte) 0, false, null, null, null), false);
                                },
                                        1000L);
                            }
                            break;
                        case 5:
                            diseases.put(SecondaryStat.PapulBomb, new Pair<>(Integer.valueOf(this.x), Integer.valueOf((int) this.duration)));
                            break;
                        case 7:
                            ms = this;
                            if (monster.getBuff(MonsterStatus.MS_PopulatusTimer) != null) {
                                monster.cancelSingleStatus(monster.getBuff(MonsterStatus.MS_PopulatusTimer));
                            }
                            stats.add(new Pair<>(MonsterStatus.MS_PopulatusInvincible, new MonsterStatusEffect(this.skillId, 610000, this.x)));
                            for (MapleReactor reactor : monster.getMap().getAllReactor()) {
                                if (reactor != null
                                        && reactor.getReactorId() == 2208011) {
                                    reactor.forceHitReactor((byte) 1, 0);
                                }
                            }
                            (monster.getMap()).PapulratusPatan = 2;
                            (monster.getMap()).PapulratusTime = 0;
                            monster.SetPatten(0);
                            (monster.getMap()).Papullatushour = Randomizer.rand(0, 12);
                            (monster.getMap()).Papullatusminute = Randomizer.rand(0, 60);
                            (monster.getMap()).Mapcoltime = 60;
                            monster.getMap().respawn(true);
                            a = Randomizer.rand(241, 246);
                            typeed = monster.getId() % 100 / 10;
                            difical = (typeed == 0) ? "Easy" : ((typeed == 1) ? "Normal" : "Chaos");
                            for (ab = 241; ab < 246; ab++) {
                                if (difical.equals("Chaos")) {
                                    monster.setCustomInfo(ab, (a == ab) ? 3 : Randomizer.rand(1, 3), 0);
                                } else if (difical.equals("Normal")) {
                                    monster.setCustomInfo(ab, Randomizer.rand(1, 3), 0);
                                } else if (difical.equals("Easy")) {
                                    monster.setCustomInfo(ab, (a == ab) ? 2 : 1, 0);
                                }
                            }
                            monster.getMap().broadcastMessage(CField.startMapEffect("파풀라투스가 시간 이동을 할 수 없도록 차원의 균열을 봉인해야 합니다.", 5120177, true));
                            monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 0));
                            monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTime(0, false, true));
                            monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTimePatten(0, (monster.getMap()).Papullatushour, (monster.getMap()).Papullatusminute, (monster.getMap()).Mapcoltime, new int[]{(int) monster.getCustomValue0(241), (int) monster.getCustomValue0(242), (int) monster.getCustomValue0(243), (int) monster.getCustomValue0(244), (int) monster.getCustomValue0(245), (int) monster.getCustomValue0(246)}));
                            Timer.MapTimer.getInstance().schedule(new Runnable() {
                                public void run() {
                                    int hppercent = 0;
                                    for (int ab = 241; ab <= 246; ab++) {
                                        switch ((int) monster.getCustomValue0(ab)) {
                                            case 1:
                                                hppercent++;
                                                break;
                                            case 2:
                                                hppercent += 10;
                                                break;
                                            case 3:
                                                hppercent += 100;
                                                break;
                                        }
                                    }
                                    (monster.getMap()).PapulratusPatan = 0;
                                    if (hppercent >= 100) {
                                        hppercent = 100;
                                    }
                                    if (hppercent >= 30) {
                                        monster.setLastSkillUsed(ms, 0L, 0L);
                                    }
                                    if (hppercent > 0) {
                                        long hp = monster.getStats().getHp() / 100L * hppercent;
                                        monster.addHp(hp, true);
                                        monster.getMap().broadcastMessage(MobPacket.damageMonster(monster.getObjectId(), hp, true));
                                    }
                                    for (MapleReactor reactor : monster.getMap().getAllReactor()) {
                                        if (reactor != null
                                                && reactor.getReactorId() == 2208011) {
                                            reactor.forceHitReactor((byte) 0, 0);
                                            break;
                                        }
                                    }
                                    for (MapleMonster monster2 : monster.getMap().getAllMonster()) {
                                        if (monster2 != null && (monster2.getId() == 8500003 || monster2.getId() == 8500004)) {
                                            monster2.getMap().killMonster(monster2);
                                        }
                                    }
                                    monster.getMap().removeDrops();
                                    monster.SetPatten(120);
                                    monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(false, 1));
                                    monster.getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusTime(monster.getPatten() * 1000, false, false));
                                    monster.cancelStatus(MonsterStatus.MS_PopulatusInvincible, monster.getBuff(MonsterStatus.MS_PopulatusInvincible));
                                }
                            },
                                    60000L);
                            break;
                        case 8:
                            diseases.put(SecondaryStat.Stun, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) this.duration)));
                            break;
                    }
                    break;
                    
                case 242:
                    switch (this.skillLevel) {
                        case 1:
                        case 2:
                        case 3:
                            id = 0;
                            if (monster.getId() == 8880321 || monster.getId() == 8880322) {
                                id = monster.getId() - 18;
                            } else if (monster.getId() == 8880323 || monster.getId() == 8880324) {
                                id = monster.getId() - 22;
                            } else if (monster.getId() == 8880353 || monster.getId() == 8880354) {
                                id = monster.getId() - 12;
                            } else {
                                id = monster.getId() - 8;
                            }

                            size = Randomizer.rand(17, 23);

                           idx = new ArrayList<Triple<Integer, Integer, Integer>>() {
                                {
                                    for (int i = 1; i <= size; i++) {
                                        add(new Triple<>(i, (1800 * (1 + (i / 6))), -650 + (130 * Randomizer.rand(1, 9))));
                                    }
                                }
                            };

                            player.getMap().broadcastMessage(MobPacket.BossWill.WillSpiderAttack(id, skillId, skillLevel, 0, idx));
                            break;
                            case 4: {
                                monster.getMap().broadcastMessage(CField.enforceMSG("눈동자를 공격해서 다른 공간에 달빛을 흘려보내세요. 어서 달빛 보호막을 생성해야 해요.", 245, 28000));
                                monster.getMap().broadcastMessage(MobPacket.BossWill.willUseSpecial());
                                ArrayList<Integer> arrayList = new ArrayList<Integer>(){
                                    {
                                        for (int i = 0; i < 9; ++i) {
                                            this.add(i);
                                        }
                                    }
                                };
                                for (MapleMonster mob : monster.getMap().getAllMonstersThreadsafe()) {
                                    mob.getMap().broadcastMessage(MobPacket.BlockAttack(mob, (List<Integer>)arrayList));
                                    mob.setSkillForbid(true);
                                    mob.setUseSpecialSkill(true);
                                    if (mob.getId() != 8880305) continue;
                                    mob.getMap().killMonster(mob);
                                }
                                bluecount = 0;
                                purplecount = 0;
                                reverge = monster.getController().getParty().getMembers().size() / 2;
                                if (monster.getController() == null) {
                                    reverge = player.getParty().getMembers().size() / 2;
                                }
                                for (MapleCharacter chr : monster.getMap().getAllCharactersThreadsafe()) {
                                    int typee = Randomizer.rand(1, 2);
                                    if (typee == 1) {
                                        if (++bluecount > reverge) {
                                            typee = 2;
                                            --bluecount;
                                        }
                                    } else if (++purplecount > reverge) {
                                        typee = 1;
                                        --purplecount;
                                    }
                                    chr.getClient().getSession().writeAndFlush((Object)MobPacket.BossWill.WillSpiderAttack(monster.getId(), this.skillId, this.skillLevel, typee, null));
                                }
                                for (MapleMist mists : monster.getMap().getAllMistsThreadsafe()) {
                                    if (mists.getMobSkill().getSkillId() != 242) continue;
                                    monster.getMap().removeMist(mists);
                                }
                                monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880305), new Point(0, -2020));
                                monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880305), new Point(0, 159));
                                Timer.MobTimer.getInstance().schedule(() -> {
                                    monster.getMap().killMonster(8880305);
                                    ArrayList<MapleMist> mists = new ArrayList<MapleMist>();
                                    ArrayList<MapleCharacter> targets = new ArrayList<MapleCharacter>();
                                    for (MapleMist mi : monster.getMap().getAllMistsThreadsafe()) {
                                        if (mi.getMobSkill() != null && mi.getMobSkill().getSkillId() == this.skillId && mi.getMobSkill().getSkillLevel() == this.skillLevel) {
                                            mists.add(mi);
                                            continue;
                                        }
                                        if (mi.getSource() == null || mi.getSource().getSourceId() != 400031039 && mi.getSource().getSourceId() != 400031040) continue;
                                        mists.add(mi);
                                    }
                                    for (MapleCharacter chr : monster.getMap().getAllCharactersThreadsafe()) {
                                        boolean add = true;
                                        chr.getClient().getSession().writeAndFlush((Object)CField.EffectPacket.showWillEffect(chr, 1, this.skillId, this.skillLevel));
                                        chr.getClient().getSession().writeAndFlush((Object)MobPacket.BossWill.WillSpiderAttack(monster.getId(), this.skillId, this.skillLevel, 0, null));
                                        for (MapleMist mist4 : mists) {
                                            if (!mist4.getBox().contains(chr.getTruePosition())) continue;
                                            add = false;
                                        }
                                        if (!add) continue;
                                        targets.add(chr);
                                    }
                                    for (MapleCharacter chr : targets) {
                                        if (chr.getBuffedEffect(SecondaryStat.HolyMagicShell) != null) {
                                            if (chr.getHolyMagicShell() > 1) {
                                                chr.setHolyMagicShell((byte)(chr.getHolyMagicShell() - 1));
                                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                                statups.put(SecondaryStat.HolyMagicShell, new Pair<Integer, Integer>(Integer.valueOf(chr.getHolyMagicShell()), (int)chr.getBuffLimit(chr.getBuffSource(SecondaryStat.HolyMagicShell))));
                                                chr.getClient().getSession().writeAndFlush((Object)CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(SecondaryStat.HolyMagicShell), chr));
                                                continue;
                                            }
                                            chr.cancelEffectFromBuffStat(SecondaryStat.HolyMagicShell);
                                            continue;
                                        }
                                        if (chr.getBuffedValue(4341052)) {
                                            chr.getClient().getSession().writeAndFlush((Object)CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte)0, true, null, null, null));
                                            chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte)0, false, null, null, null), false);
                                            continue;
                                        }
                                        if (chr.getBuffedEffect(SecondaryStat.WindWall) != null) {
                                            int windWall = Math.max(0, chr.getBuffedValue(SecondaryStat.WindWall) - 100 * chr.getBuffedEffect(SecondaryStat.WindWall).getZ());
                                            if (windWall > 1) {
                                                chr.setBuffedValue(SecondaryStat.WindWall, windWall);
                                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                                statups.put(SecondaryStat.WindWall, new Pair<Integer, Integer>(windWall, (int)chr.getBuffLimit(400031030)));
                                                chr.getClient().getSession().writeAndFlush((Object)CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(SecondaryStat.WindWall), chr));
                                                continue;
                                            }
                                            chr.cancelEffectFromBuffStat(SecondaryStat.WindWall);
                                            continue;
                                        }
                                        if (chr.getBuffedEffect(SecondaryStat.TrueSniping) != null || chr.getBuffedEffect(SecondaryStat.Etherealform) != null || chr.getBuffedEffect(SecondaryStat.IndieNotDamaged) != null || chr.getBuffedEffect(SecondaryStat.NotDamaged) != null) continue;
                                        chr.playerIGDead();
                                    }
                                    if (monster.isUseSpecialSkill()) {
                                        ArrayList<Integer> newHpList = new ArrayList<Integer>();
                                        if (monster.getHPPercent() <= 67 && monster.getWillHplist().contains(666)) {
                                            newHpList.add(333);
                                            newHpList.add(3);
                                        } else if (monster.getHPPercent() <= 34 && monster.getWillHplist().contains(333)) {
                                            newHpList.add(3);
                                        } else if (monster.getHPPercent() > 1 || !monster.getWillHplist().contains(3)) {
                                            newHpList.add(666);
                                            newHpList.add(333);
                                            newHpList.add(3);
                                        }
                                        monster.setWillHplist(newHpList);
                                        monster.getMap().broadcastMessage(MobPacket.BossWill.setWillHp(monster.getWillHplist(), monster.getMap(), monster.getId(), monster.getId() + 3, monster.getId() + 4));
                                        MobSkillFactory.getMobSkill(242, 8).applyEffect(player, monster, skill, isFacingLeft);
                                    } else {
                                        for (MapleMonster mob : monster.getMap().getAllMonstersThreadsafe()) {
                                            mob.getMap().broadcastMessage(MobPacket.BlockAttack(mob, new ArrayList<Integer>()));
                                            mob.setSkillForbid(false);
                                            mob.setUseSpecialSkill(false);
                                        }
                                        monster.setNextSkill(0);
                                        monster.setNextSkillLvl(0);
                                    }
                                    Timer.MobTimer.getInstance().schedule(() -> MobSkillFactory.getMobSkill(242, 15).applyEffect(player, monster, skill, isFacingLeft), 11000L);
                                }, 30000L);
                                break;
                            }
                            case 5: {//mush 
                                type = Randomizer.nextInt(2);
                                monster.getMap().broadcastMessage(MobPacket.showBossHP(monster));
                                monster.getMap().broadcastMessage(MobPacket.BossWill.WillSpiderAttack(monster.getId(), this.skillId, this.skillLevel, type, null));
                                Timer.MobTimer.getInstance().schedule(() -> {
                                    if (monster.getId() == 8880300 || monster.getId() == 8880340) {
                                        MapleMonster will1 = monster.getMap().getMonsterById(monster.getId() + 3);
                                        MapleMonster will2 = monster.getMap().getMonsterById(monster.getId() + 4);
                                        long hp1 = 0L;
                                        long hp2 = 0L;
                                        if (will1 != null) {
                                            hp1 = will1.getHp();
                                        }
                                        if (will2 != null) {
                                            hp2 = will2.getHp();
                                        }
                                        long newhp = Math.max(hp1, hp2);
                                        monster.setHp(newhp);
                                        if (will1 != null) {
                                            will1.setHp(newhp);
                                        }
                                        if (will2 != null) {
                                            will2.setHp(newhp);
                                        }
                                        monster.getMap().broadcastMessage(MobPacket.BossWill.setWillHp(monster.getWillHplist(), monster.getMap(), monster.getId(), monster.getId() + 3, monster.getId() + 4));
                                    }
                                    ArrayList<MapleMist> mists = new ArrayList<MapleMist>();
                                    ArrayList<MapleCharacter> targets = new ArrayList<MapleCharacter>();
                                    for (MapleMist mi : monster.getMap().getAllMistsThreadsafe()) {
                                        if (mi.getSource() == null || mi.getSource().getSourceId() != 400031039 && mi.getSource().getSourceId() != 400031040) continue;
                                        mists.add(mi);
                                    }
                                    for (MapleCharacter chr : monster.getMap().getAllCharactersThreadsafe()) {
                                        boolean add = true;
                                        chr.getClient().getSession().writeAndFlush((Object)CField.EffectPacket.showWillEffect(chr, 1, this.skillId, this.skillLevel));
                                        for (MapleMist mist4 : mists) {
                                            if (!mist4.getBox().contains(chr.getTruePosition())) continue;
                                            add = false;
                                        }
                                        if (!add) continue;
                                        targets.add(chr);
                                    }
                                    for (MapleCharacter chr : targets) {
                                        if (chr.getBuffedEffect(SecondaryStat.HolyMagicShell) != null) {
                                            if (chr.getHolyMagicShell() > 1) {
                                                chr.setHolyMagicShell((byte)(chr.getHolyMagicShell() - 1));
                                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                                statups.put(SecondaryStat.HolyMagicShell, new Pair<Integer, Integer>(Integer.valueOf(chr.getHolyMagicShell()), (int)chr.getBuffLimit(chr.getBuffSource(SecondaryStat.HolyMagicShell))));
                                                chr.getClient().getSession().writeAndFlush((Object)CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(SecondaryStat.HolyMagicShell), chr));
                                                continue;
                                            }
                                            chr.cancelEffectFromBuffStat(SecondaryStat.HolyMagicShell);
                                            continue;
                                        }
                                        if (chr.getBuffedValue(4341052)) {
                                            chr.getClient().getSession().writeAndFlush((Object)CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte)0, true, null, null, null));
                                            chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 1, 0, 36, 0, 0, (byte)0, false, null, null, null), false);
                                            continue;
                                        }
                                        if (chr.getBuffedEffect(SecondaryStat.WindWall) != null) {
                                            int windWall = Math.max(0, chr.getBuffedValue(SecondaryStat.WindWall) - 100 * chr.getBuffedEffect(SecondaryStat.WindWall).getZ());
                                            if (windWall > 0) {
                                                chr.setBuffedValue(SecondaryStat.WindWall, windWall);
                                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                                statups.put(SecondaryStat.WindWall, new Pair<Integer, Integer>(windWall, (int)chr.getBuffLimit(400031030)));
                                                chr.getClient().getSession().writeAndFlush((Object)CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(SecondaryStat.WindWall), chr));
                                                continue;
                                            }
                                            chr.cancelEffectFromBuffStat(SecondaryStat.WindWall);
                                            continue;
                                        }
                                        if (chr.getBuffedEffect(SecondaryStat.TrueSniping) != null || chr.getBuffedEffect(SecondaryStat.Etherealform) != null || chr.getBuffedEffect(SecondaryStat.IndieNotDamaged) != null || chr.getBuffedEffect(SecondaryStat.NotDamaged) != null) continue;
                                        int reduce = 0;
                                        if (chr.getBuffedEffect(SecondaryStat.IndieDamageReduce) != null) {
                                            reduce = chr.getBuffedValue(SecondaryStat.IndieDamageReduce);
                                        } else if (chr.getBuffedEffect(SecondaryStat.IndieDamReduceR) != null) {
                                            reduce = -chr.getBuffedValue(SecondaryStat.IndieDamReduceR).intValue();
                                        }
                                        if (type == 0 && chr.getTruePosition().y > -455 && chr.getTruePosition().y < 300) {
                                            chr.addHP(-chr.getStat().getCurrentMaxHp() * (long)(100 - reduce) / 100L);
                                            continue;
                                        }
                                        if (type != 1 || chr.getTruePosition().y <= -2500 || chr.getTruePosition().y >= -1800) continue;
                                        chr.addHP(-chr.getStat().getCurrentMaxHp() * (long)(100 - reduce) / 100L);
                                    }
                                    Timer.MobTimer.getInstance().schedule(() -> {
                                        double hppercent = (double)monster.getHp() * 100.0 / (double)monster.getStats().getHp();
                                        if (hppercent <= 66.6 && monster.getWillHplist().contains(666)) {
                                            MobSkillFactory.getMobSkill(242, 4).applyEffect(player, monster, skill, isFacingLeft);
                                        } else if (hppercent <= 33.3 && monster.getWillHplist().contains(333)) {
                                            MobSkillFactory.getMobSkill(242, 4).applyEffect(player, monster, skill, isFacingLeft);
                                        } else if (hppercent <= 0.3 && monster.getWillHplist().contains(3)) {
                                            MobSkillFactory.getMobSkill(242, 4).applyEffect(player, monster, skill, isFacingLeft);
                                        }
                                    }, 1000L);
                                }, 3000L);
                                break;
                            }
                            case 6: {//mush
                                break;
                            }
                            case 7: {
                                monster.getMap().broadcastMessage(CField.enforceMSG("거짓의 거울은 공격을 반전시켜요. 균열이 나타나면 공격을 마주하세요.", 245, 26000));
                                monster.getMap().broadcastMessage(MobPacket.BossWill.willUseSpecial());
                                idss = new ArrayList<Integer>(){
                                    {
                                        for (int i = 0; i < 9; ++i) {
                                            this.add(i);
                                        }
                                    }
                                };
                                for (MapleMonster mob : monster.getMap().getAllMonstersThreadsafe()) {
                                    mob.getMap().broadcastMessage(MobPacket.BlockAttack(mob, (List<Integer>)idss));
                                    mob.setUseSpecialSkill(true);
                                    mob.setSkillForbid(true);
                                    mob.setNextSkill(0);
                                    mob.setNextSkillLvl(0);
                                }
                                Timer.MobTimer.getInstance().schedule(() -> MobSkillFactory.getMobSkill(242, 14).applyEffect(player, monster, skill, isFacingLeft), 5000L);
                                break;
                            }
                            case 8: {
                                monster.getMap().broadcastMessage(MobPacket.BossWill.willStun());
                                monster.getMap().broadcastMessage(CField.enforceMSG("지금이에요. 윌이 무방비 상태일 때 피해를 입혀야 해요.", 245, 12000));
                                monster.setNextSkill(0);
                                monster.setNextSkillLvl(0);
                                for (MapleMist mist4 : monster.getMap().getAllMistsThreadsafe()) {
                                    if (mist4.getMobSkill().getSkillId() != 242) continue;
                                    monster.getMap().broadcastMessage(CField.removeMist(mist4));
                                    monster.getMap().removeMapObject(mist4);
                                }
                                if (monster.getId() == 8880300 || monster.getId() == 8880340) {
                                    MapleMonster will2;
                                    MapleMonster will1 = monster.getMap().getMonsterById(monster.getId() + 3);
                                    if (will1 != null) {
                                        monster.getMap().broadcastMessage(MobPacket.forcedSkillAction(will1.getObjectId(), 3, false));
                                    }
                                    if ((will2 = monster.getMap().getMonsterById(monster.getId() + 4)) != null) {
                                        monster.getMap().broadcastMessage(MobPacket.forcedSkillAction(will2.getObjectId(), 3, false));
                                    }
                                } else {
                                    monster.getMap().broadcastMessage(MobPacket.forcedSkillAction(monster.getObjectId(), 2, false));
                                }
                                Timer.MapTimer.getInstance().schedule(() -> {
                                    for (MapleMonster mob : monster.getMap().getAllMonstersThreadsafe()) {
                                        mob.getMap().broadcastMessage(MobPacket.BlockAttack(mob, new ArrayList<Integer>()));
                                        mob.setSkillForbid(false);
                                        mob.setUseSpecialSkill(false);
                                    }
                                }, 10000L);
                                break;
                            }
                        case 9:
                            diseases.put(SecondaryStat.WillPoison, new Pair<>(Integer.valueOf(1), Integer.valueOf(7000)));
                            player.setSkillCustomInfo(24219, Randomizer.rand(1, 2147483647), 0L);
                            player.setSkillCustomInfo(24209, 1L, 0L);
                            player.setSkillCustomInfo(24220, 0L, 3000L);
                            player.getMap().broadcastMessage(MobPacket.BossWill.posion(player, (int) player.getSkillCustomValue0(24219), 0, 0, 0));
                            Timer.MapTimer.getInstance().schedule(() -> {
                                if (player.getSkillCustomValue0(24219) > 0L) {
                                    player.getMap().broadcastMessage(MobPacket.BossWill.removePoison(player, (int) player.getSkillCustomValue0(24219)));
                                    player.removeSkillCustomInfo(24219);
                                    player.removeSkillCustomInfo(24209);
                                    player.removeSkillCustomInfo(24220);
                                }
                            }, 7000L);
                            break;
                        case 10:
                            player.getMap().broadcastMessage(MobPacket.BossWill.WillSpiderAttack(monster.getId(), this.skillId, this.skillLevel, 1, null));
                            break;
                        case 11:
                            player.getMap().broadcastMessage(MobPacket.BossWill.WillSpiderAttack(monster.getId(), this.skillId, this.skillLevel, 1, null));
                            break;
                        case 12:
                            id = 0;
                            if (monster.getId() == 8880325 || monster.getId() == 8880326) {
                                if (monster.getMap().getId() == 450008150) {
                                    if ((monster.getTruePosition()).y < 0) {
                                        id = 8880304;
                                    } else {
                                        id = 8880303;
                                    }
                                } else if ((monster.getTruePosition()).y < 0) {
                                    id = 8880344;
                                } else {
                                    id = 8880343;
                                }
                            } else if (monster.getId() == 8880327 || monster.getId() == 8880328) {
                                id = monster.getId() - 26;
                                if (player.getMapId() == 450008250 || player.getMapId() == 450008850) {
                                    id = (player.getMapId() == 450008250) ? 8880301 : 8880341;
                                    if (Randomizer.nextBoolean()) {
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, -492, -370}));
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, -8, -370}));
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, 501, -370}));
                                    } else {
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, -300, -370}));
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, 300, -370}));
                                    }
                                } else if (player.getMapId() == 450008350 || player.getMapId() == 450008950) {
                                    id = (player.getMapId() == 450008250) ? 8880302 : 8880342;
                                    if (Randomizer.nextBoolean()) {
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, -300, -400}));
                                    } else {
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, -415, -400}));
                                        player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, 300, -400}));
                                    }
                                }
                            } else if (monster.getId() == 8880355 || monster.getId() == 8880356) {
                                id = monster.getId() - 12;
                            }
                            monster.setLastSkillUsed(this, System.currentTimeMillis(), getInterval());
                            if (id != 8880341 && id != 8880342 && id != 8880302 && id != 8880301) {
                                if (Randomizer.nextBoolean()) {
                                    player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, -250, -370}));
                                    player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, 250, -370}));
                                } else {
                                    player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, -470, -440}));
                                    player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{0, id, 470, -440}));
                                }
                            }
                            if ((monster.getTruePosition()).y < 0) {
                                player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{1, id, 300, 100, -690, -2634, 695, -2019}));
                                break;
                            }
                            player.getMap().broadcastMessage(MobPacket.BossWill.createBulletEyes(new int[]{1, id, 300, 100, -690, -455, 695, 500}));
                            break;
                            case 13: {
                                webSize = monster.getMap().getAllSpiderWeb().size();
                                ArrayList<Integer> a2 = new ArrayList<Integer>();
                                if (webSize >= 67) break;
                                i5 = 0;
                                boolean respawn = false;
                                for (SpiderWeb web : monster.getMap().getAllSpiderThreadsafe()) {
                                    a2.add(web.getNum());
                                }
                                Collections.sort(a2);
                                for (Integer liw : a2) {
                                    if (liw != i5) {
                                        monster.getMap().spawnSpiderWeb(new SpiderWeb(i5));
                                        respawn = true;
                                        break;
                                    }
                                    ++i5;
                                }
                                if (i5 >= 67 || respawn) break;
                                monster.getMap().spawnSpiderWeb(new SpiderWeb(i5));
                                break;
                            }
                            case 14: {
                                if (monster.getId() != 8880301 && monster.getId() != 8880341) {
                                    return;
                                }
                                monster.setWillSpecialPattern(true);
                                a = Randomizer.rand(0, 1);//int a
                                this.willSpider(monster.getController(), monster, a, false);
                                Timer.MapTimer.getInstance().schedule(() -> this.willSpider(monster.getController(), monster, a == 0 ? 1 : 0, true), 11000L);
                                Timer.MapTimer.getInstance().schedule(() -> {
                                    if (monster.isWillSpecialPattern()) {
                                        MobSkillFactory.getMobSkill(242, 8).applyEffect(player, monster, skill, isFacingLeft);
                                        if (monster.getHPPercent() <= 50 && monster.getWillHplist().contains(500)) {
                                            monster.setWillHplist(new ArrayList<Integer>());
                                            monster.getWillHplist().add(3);
                                        } else if (monster.getHPPercent() <= 3 && monster.getWillHplist().contains(3)) {
                                            monster.setWillHplist(new ArrayList<Integer>());
                                        } else {
                                            monster.setWillHplist(new ArrayList<Integer>());
                                            monster.getWillHplist().add(500);
                                            monster.getWillHplist().add(3);
                                        }
                                    } else {
                                        for (MapleMonster mob : monster.getMap().getAllMonstersThreadsafe()) {
                                            mob.getMap().broadcastMessage(MobPacket.BlockAttack(mob, new ArrayList<Integer>()));
                                            mob.setUseSpecialSkill(false);
                                            mob.setSkillForbid(false);
                                        }
                                    }
                                    monster.getMap().broadcastMessage(MobPacket.BossWill.setWillHp(monster.getWillHplist()));
                                    monster.setWillSpecialPattern(false);
                                }, 30000L);
                                break;
                            }
                        case 15:
                            for (MapleCharacter mapleCharacter : monster.getMap().getAllCharactersThreadsafe()) {
                                monster.getMap().broadcastMessage(CField.EffectPacket.showWillEffect(mapleCharacter, 0, this.skillId, this.skillLevel));
                            }
                            Timer.MobTimer.getInstance().schedule(() -> {
                                for (MapleCharacter chr : monster.getMap().getAllCharactersThreadsafe()) {
                                    chr.getClient().getSession().writeAndFlush(CField.portalTeleport(Randomizer.nextBoolean() ? "ptup" : "ptdown"));
                                    chr.getClient().getSession().writeAndFlush(MobPacket.BossWill.WillSpiderAttack(monster.getId(), this.skillId, this.skillLevel, Randomizer.nextInt(2), null));
                                }
                            }, 3000L);
                            break;
                    }
                case 246:
                    datas = new ArrayList<>();
                    for (int ii = 0; ii < 7; ii++) {
                        List<Rectangle> rectz = new ArrayList<>();
                        int[] randXs = {0, 280, -560, 560, -280, -840, 840};
                        int t = Randomizer.nextInt(randXs.length);
                        int randX = randXs[t];
                        int delay = (t + 1) * 250;
                        int[][][] rectXs = {{{-75, 50}, {13, -50}, {83, 72}, {83, 72}}, {{-81, 90}, {-59, -20}, {-25, 13}, {123, 31}, {138, -54}}, {{-78, 28}, {-13, -50}, {42, 81}, {75, -18}, {133, 4}}, {{-75, 50}, {13, -60}, {83, 72}, {83, 72}}, {{-81, 90}, {-59, -20}, {-25, 13}, {123, 31}, {138, -54}}, {{-78, 28}, {-13, -50}, {42, 81}, {75, -18}, {133, 4}}, {{-78, 28}, {-13, -50}, {42, 81}, {75, -18}, {133, 4}}};
                        int[][] rectX = rectXs[t];
                        for (int i6 = 0; i6 < rectX.length; i6++) {
                            rectz.add(new Rectangle(rectX[i6][0], -80, rectX[i6][1], 640));
                        }
                        datas.add(new Triple<>(new Point(randX, -260), Integer.valueOf(delay), rectz));
                    }
                    monster.getMap().broadcastMessage(MobPacket.jinHillahBlackHand(monster.getObjectId(), this.skillId, this.skillLevel, datas));
                    break;
                case 247:
                    if (monster.getCustomValue0(24701) == 0L) {
                        monster.setNextSkill(0);
                        monster.setNextSkillLvl(0);
                        return;
                    }
                    monster.setCustomInfo(24701, 0, 0);
                    Glasstime = (monster.getHPPercent() >= 60) ? 150 : ((monster.getHPPercent() >= 30) ? 120 : 100);
                    monster.getMap().setSandGlassTime(Glasstime);
                    monster.getMap().broadcastMessage(CField.JinHillah(4, (monster.getController() == null) ? player : monster.getController(), monster.getMap()));
                    monster.setLastSkillUsed(this, System.currentTimeMillis(), (Glasstime * 1000));
                    for (MapleCharacter mapleCharacter : monster.getMap().getAllCharactersThreadsafe()) {
                        for (int i6 = 0; i6 < (mapleCharacter.getDeathCounts()).length; i6++) {
                            if (mapleCharacter.getDeathCounts()[i6] == 0) {
                                mapleCharacter.getDeathCounts()[i6] = 2;
                            }
                        }
                        mapleCharacter.getClient().getSession().writeAndFlush(CField.JinHillah(3, mapleCharacter, monster.getMap()));
                        mapleCharacter.getMap().broadcastMessage(CField.JinHillah(10, mapleCharacter, monster.getMap()));
                    }
                    sandCount = 0;
                    for (MapleCharacter mapleCharacter : monster.getMap().getAllCharactersThreadsafe()) {
                        sandCount += mapleCharacter.liveCounts();
                    }
                    monster.getMap().setCandles((int) Math.round(sandCount * 0.5D));
                    monster.getMap().broadcastMessage(CField.JinHillah(0, player, monster.getMap()));
                    monster.getMap().setLightCandles(0);
                    monster.getMap().broadcastMessage(CField.JinHillah(1, player, monster.getMap()));
                    monster.getMap().broadcastMessage(CField.JinHillah(8, player, monster.getMap()));
                    break;
                case 248:
                    mapleMist1 = new MapleMist(new Rectangle(Randomizer.rand(-400, 300), Randomizer.rand(-400, 100), 400, 400), monster, this, 8000);
                    if (mapleMist1 != null) {
                        monster.getMap().spawnMist(mapleMist1, false);
                    }
                    break;
                case 249:
                    switch (this.skillLevel) {
                        case 1:
                            diseases.put(SecondaryStat.CurseOfCreation, new Pair<>(Integer.valueOf(4), Integer.valueOf(6000)));
                            break;
                        case 2:
                            diseases.put(SecondaryStat.CurseOfDestruction, new Pair<>(Integer.valueOf(10), Integer.valueOf(6000)));
                            break;
                    }
                    if ((player.hasDisease(SecondaryStat.CurseOfCreation) && this.skillLevel == 2) || (player.hasDisease(SecondaryStat.CurseOfDestruction) && this.skillLevel == 1)) {
                        player.setDeathCount((byte) (player.getDeathCount() - 1));
                        player.getClient().getSession().writeAndFlush(CField.BlackMageDeathCountEffect());
                        player.getClient().getSession().writeAndFlush(CField.getDeathCount(player.getDeathCount()));
                        player.dispelDebuffs();
                        if (player.getDeathCount() > 0) {
                            player.addHP(-player.getStat().getCurrentMaxHp() * 30L / 100L);
                            if (player.isAlive()) {
                                MobSkillFactory.getMobSkill(120, 39).applyEffect(player, monster, skill, isFacingLeft);
                            }
                            break;
                        }
                        player.addHP(-player.getStat().getCurrentMaxHp());
                    }
                    break;
                case 260:
                    switch (this.skillLevel) {
                        case 1:
                        case 3:
                            n = Randomizer.rand(2, 4);
                            bool1 = Randomizer.nextBoolean();
                            for (i1 = 0; i1 < n; i1++) {
                                int xgoal = Randomizer.rand(-782, 772);
                                int xdistance = Randomizer.rand(800, 820);
                                int xstart = bool1 ? (xgoal + xdistance) : (xgoal - xdistance);
                                int i6 = xstart - xgoal - 29;
                                if (i6 > 0) {
                                    i6 *= -1;
                                }
                                Obstacle ob = new Obstacle(bool1 ? 81 : 82, new Point(xstart, i6), new Point(xgoal, 29), 110, 30, 0, 5, 2, 260, 1);
                                ob.setVperSec(1000);
                                monster.getMap().CreateObstacle2(null, ob, (byte) 5);
                                player.getClient().getSession().writeAndFlush(MobPacket.TeleportMonster(monster, true, 9, monster.getTruePosition()));
                            }
                            break;
                        case 5:
                            obs = new ArrayList<>();
                            size_ = 11;
                            i3 = -1050;
                            y = 0;
                            for (i5 = 0; i5 < size_; i5++) {
                                int i6 = 83;
                                i3 += Randomizer.rand(200, 280);
                                y = Randomizer.rand(80, 150);
                                y += 80;
                                Obstacle ob = new Obstacle(i6, new Point(i3, -y), new Point(i3, 305), 80, 40, 0, Randomizer.rand(2, 3), Randomizer.rand(2, 3), y + 305, 0);
                                obs.add(ob);
                            }
                            monster.getMap().CreateObstacle(monster, obs);
                            break;
                    }
                    break;
                case 262:
                    switch (this.skillLevel) {
                        case 1:
                            if (player.getSkillCustomValue(26201) == null) {
                                player.setSkillCustomInfo(26201, 0L, 1000L);
                                player.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(new int[]{1, 255, 240, 240, 240, 400, 0}));
                                Timer.BuffTimer.getInstance().schedule(() -> player.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(new int[]{0, 0, 0, 0, 0, 300, 0})), 1000L);
                            }
                            break;
                    }
                    break;
                case 263:
                    monster.getMap().broadcastMessage(MobPacket.BossSeren.SerenSpawnOtherMist(monster.getObjectId(), isFacingLeft, monster.getPosition()));
                    break;
                case 264:
                    monster.getMap().broadcastMessage(MobPacket.BossSeren.SerenMobLazer(monster, this.skillLevel, (this.skillLevel == 1) ? 1800 : 1500));
                    break;
                case 265:
                    monster.setCustomInfo(monster.getId(), 25, 0);
                    monster.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 4, 0));
                    monster.getMap().broadcastMessage(MobPacket.HillaDrainStart(monster.getObjectId()));
                    for (k = 0; k < 8; k++) {
                        int posx = (monster.getPosition()).x + Randomizer.rand(-500, 500);
                        if (monster.getMap().getLeft() > posx) {
                            posx = monster.getMap().getLeft() + 50;
                        } else if (monster.getMap().getRight() < posx) {
                            posx = monster.getMap().getRight() - 50;
                        }
                        monster.getMap().spawnMonsterWithEffect(MapleLifeFactory.getMonster(8870106), 43, new Point(posx, (monster.getPosition()).y));
                    }
                    break;
            }
            if (stats.size() > 0 && monster != null) {
                if (player != null && this.lt != null && this.rb != null && skill) {
                    monster.applyMonsterBuff(player.getMap(), stats, this);
                    for (MapleMapObject mons : getObjectsInRange(monster, MapleMapObjectType.MONSTER)) {
                        if (mons.getObjectId() != monster.getObjectId()) {
                            ((MapleMonster) mons).applyMonsterBuff(player.getMap(), stats, this);
                        }
                    }
                } else {
                    monster.applyMonsterBuff(monster.getMap(), stats, this);
                }
            }
            if (diseases.size() > 0 && player != null) {
                if (allchr) {
                    for (MapleCharacter mapleCharacter : monster.getMap().getAllChracater()) {
                        mapleCharacter.giveDebuff(diseases, this);
                    }
                } else if (this.lt != null && this.rb != null && skill && monster != null) {
                    for (MapleCharacter mapleCharacter : getPlayersInRange(monster, player)) {
                        if (!cancels.isEmpty()) {
                            for (SecondaryStat cancel : cancels) {
                                if (mapleCharacter.hasDisease(cancel)) {
                                    mapleCharacter.cancelDisease(cancel);
                                }
                            }
                        }
                        mapleCharacter.giveDebuff(diseases, this);
                    }
                } else {
                    player.giveDebuff(diseases, this);
                }
            }
            if (monster != null) {
                monster.setMp(monster.getMp() - getMpCon());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSkillId() {
        return this.skillId;
    }

    public int getSkillLevel() {
        return this.skillLevel;
    }

    public int getMpCon() {
        return this.mpCon;
    }

    public List<Integer> getSummons() {
        return Collections.unmodifiableList(this.toSummon);
    }

    public int getSpawnEffect() {
        return this.spawnEffect;
    }

    public int getHP() {
        return this.hp;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Point getLt() {
        return this.lt;
    }

    public Point getRb() {
        return this.rb;
    }

    public int getLimit() {
        return this.limit;
    }

    public boolean makeChanceResult() {
        return (this.prop >= 1.0D || Math.random() < this.prop);
    }

    public Rectangle calculateBoundingBox(Point posFrom, boolean facingLeft) {
        Point mylt;
        Point myrb;
        if (facingLeft) {
            mylt = new Point(this.lt.x + posFrom.x, this.lt.y + posFrom.y);
            myrb = new Point(this.rb.x + posFrom.x, this.rb.y + posFrom.y);
        } else {
            myrb = new Point(this.lt.x * -1 + posFrom.x, this.rb.y + posFrom.y);
            mylt = new Point(this.rb.x * -1 + posFrom.x, this.lt.y + posFrom.y);
        }
        return new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
    }

    private List<MapleCharacter> getPlayersInRange(Point pos, boolean facingleft, MapleCharacter player) {
        Rectangle bounds = calculateBoundingBox(pos, facingleft);
        List<MapleCharacter> players = new ArrayList<>();
        players.add(player);
        return player.getMap().getPlayersInRectAndInList(bounds, players);
    }

    private List<MapleCharacter> getPlayersInRange(MapleMonster monster, MapleCharacter player) {
        Rectangle bounds = calculateBoundingBox(monster.getTruePosition(), monster.isFacingLeft());
        List<MapleCharacter> players = new ArrayList<>();
        players.add(player);
        return monster.getMap().getPlayersInRectAndInList(bounds, players);
    }

    private List<MapleMapObject> getObjectsInRange(MapleMonster monster, MapleMapObjectType objectType) {
        Rectangle bounds = calculateBoundingBox(monster.getTruePosition(), monster.isFacingLeft());
        List<MapleMapObjectType> objectTypes = new ArrayList<>();
        objectTypes.add(objectType);
        return monster.getMap().getMapObjectsInRect(bounds, objectTypes);
    }

    public boolean isOnlyFsm() {
        return this.onlyFsm;
    }

    public void setOnlyFsm(boolean b) {
        this.onlyFsm = b;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(Integer valueOf) {
        this.action = valueOf.intValue();
    }

    public long getInterval() {
        return this.interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setSkillAfter(int i) {
        this.skillAfter = i;
    }

    public int getSkillAfter() {
        return this.skillAfter;
    }

    public void setMobSkillDelay(MapleCharacter chr, MapleMonster monster, int skillAfter, short option, boolean isFacingLeft) {
        try {
            if (monster.getCustomValue0(1234567) == 1L) {
                return;
            }
            ArrayList<Rectangle> skillRectInfo = new ArrayList<Rectangle>();
            block1 : switch (this.skillId) {
                case 145: {
                    switch (this.skillLevel) {
                        case 12: {
                            if (monster.getId() != 8870000) break;
                            chr.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, monster.getId() == 8870100 ? 3 : 2, 0));
                        }
                    }
                    chr.getClient().getSession().writeAndFlush((Object)MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                    break;
                }
                case 213: {
                    switch (this.skillLevel) {
                        case 10: {
                            skillRectInfo.add(new Rectangle(-300, -1022, 510, 875));
                            monster.getMap().broadcastMessage(CField.enforceMSG("더스크 중심으로 주변의 에너지가 빠른 속도로 빨려 들어갑니다.", 249, 2000));
                            Timer.MapTimer.getInstance().schedule(() -> {
                                MobSkill msi = MobSkillFactory.getMobSkill(252, 1);
                                MapleMist DustMist = new MapleMist(new Rectangle(-300, -1022, 510, 875), monster, msi, 5500);
                                DustMist.setPosition(new Point(-45, -157));
                                if (DustMist != null) {
                                    monster.getMap().spawnMist(DustMist, true);
                                }
                            }, 2220L);
                            break;
                        }
                        case 13: {
                            int paramrandom = Randomizer.nextInt(100);
                            skillRectInfo.add(new Rectangle(108, -771, 300, 805));
                            if (paramrandom < 50) {
                                skillRectInfo.add(new Rectangle(-792, -771, 300, 805));
                            } else {
                                skillRectInfo.add(new Rectangle(-192, -771, 300, 805));
                            }
                            skillRectInfo.add(new Rectangle(-492, -771, 300, 805));
                            skillRectInfo.add(new Rectangle(408, -771, 300, 805));
                            break;
                        }
                        case 14: {
                            MapleMap map = monster.getMap();
                            for (int count = 0; count < 5; ++count) {
                                int x = Randomizer.nextInt(map.getRight() - map.getLeft()) + map.getLeft();
                                skillRectInfo.add(new Rectangle(x, -757, 200, 610));
                            }
                            break;
                        }
                        case 15: {
                            MapleMap map = monster.getMap();
                            for (int count = 0; count < 3; ++count) {
                                int x = Randomizer.nextInt(map.getRight() - map.getLeft()) + map.getLeft();
                                skillRectInfo.add(new Rectangle(x, -907, 360, 760));
                            }
                            Collections.shuffle(skillRectInfo);
                            break;
                        }
                    }
                    chr.getClient().getSession().writeAndFlush((Object)MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                    break;
                }
                case 215: {
                    chr.getMap().broadcastMessage(MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                    break;
                }
                case 217: {
                    switch (this.skillLevel) {
                        case 6: 
                        case 13: 
                        case 14: 
                        case 16: {
                            ArrayList<Integer> list = new ArrayList<Integer>();
                            for (MapleNodes.Environment env : chr.getMap().getNodez().getEnvironments()) {
                                env.setShow(false);
                            }
                            chr.getMap().broadcastMessage(CField.getUpdateEnvironment(chr.getMap().getNodez().getEnvironments()));
                            for (int i = 0; i < 7; ++i) {
                                if (!Randomizer.isSuccess(50) || list.size() >= 7) continue;
                                skillRectInfo.add(new Rectangle(-650 + 180 * i, -81, 180, 65));
                                list.add(-650 + 180 * i);
                            }
                            if (list.isEmpty()) {
                                int rand = Randomizer.rand(0, 7);
                                skillRectInfo.add(new Rectangle(-650 + 180 * rand, -81, 180, 65));
                                list.add(-650 + 180 * rand);
                            }
                            monster.setSkillForbid(true);
                            monster.setCustomInfo(1234567, 1, 0);
                            monster.setNextSkill(0);
                            monster.setNextSkillLvl(0);
                            chr.getMap().broadcastMessage(MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                            int i2 = 0;
                            ArrayList<MapleEnergySphere> spawn = new ArrayList<MapleEnergySphere>();
                            for (Integer x : list) {
                                MapleEnergySphere mes = new MapleEnergySphere(true, 1, -500, 100000, 15, 10000, 0, list, i2);
                                mes.setCustomx(x);
                                chr.getMap().addMapObject(mes);
                                spawn.add(mes);
                                ++i2;
                            }
                            chr.getMap().spawnEnergySphereListTimer(monster.getObjectId(), this.skillLevel, spawn, 1700);
                            Timer.MapTimer.getInstance().schedule(() -> {
                                monster.setSkillForbid(false);
                                monster.setUseSpecialSkill(false);
                                if (monster.getBuff(MonsterStatus.MS_Freeze) == null) {
                                    MapleMist mist;
                                    int mistskillid = 0;
                                    int mistskilllv = 0;
                                    for (MobSkill ms : monster.getSkills()) {
                                        if (ms.getSkillId() != 131) continue;
                                        mistskillid = ms.getSkillId();
                                        mistskilllv = ms.getSkillLevel();
                                    }
                                    if (mistskillid != 0 && mistskilllv != 0 && (mist = new MapleMist(new Rectangle(-684, -29, 1365, 15), monster, MobSkillFactory.getMobSkill(mistskillid, mistskilllv), (int)MobSkillFactory.getMobSkill(mistskillid, mistskilllv).getDuration())) != null && monster != null && monster.getMap() != null && monster.isAlive()) {
                                        monster.getMap().spawnMist(mist, false);
                                    }
                                }
                            }, Randomizer.rand(4000, 10000));
                            Timer.MapTimer.getInstance().schedule(() -> {
                                monster.removeCustomInfo(1234567);
                                for (MapleNodes.Environment env : chr.getMap().getNodez().getEnvironments()) {
                                    env.setShow(false);
                                }
                                chr.getMap().broadcastMessage(CField.getUpdateEnvironment(chr.getMap().getNodez().getEnvironments()));
                            }, 12800L);
                            break block1;
                        }
                        case 7: 
                        case 8: 
                        case 15: 
                        case 17: 
                        case 23: {
                            chr.getMap().broadcastMessage(MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                            Timer.MobTimer.getInstance().schedule(() -> chr.getMap().spawnEnergySphere(monster.getObjectId(), this.skillLevel, new MapleEnergySphere(monster.getTruePosition().x, 10, 20000, 0, true, true)), 3000L);
                            break block1;
                        }
                        case 21: {
                            chr.getMap().broadcastMessage(MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                            for (int i = 0; i < 3; ++i) {
                                MapleEnergySphere mes = new MapleEnergySphere(0, 10, 30000, 1050, false, false);
                                mes.setX(Randomizer.rand(10, 150));
                                mes.setY(Randomizer.rand(100, 150));
                                chr.getMap().spawnEnergySphere(monster.getObjectId(), this.skillLevel, mes);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 226: {
                    option = 0;
                    skillRectInfo.add(new Rectangle(-592, -1016, this.rb.x - this.lt.x, this.rb.y - this.lt.y));
                    skillRectInfo.add(new Rectangle(-79, -1016, this.rb.x - this.lt.x, this.rb.y - this.lt.y));
                    skillRectInfo.add(new Rectangle(383, -1016, this.rb.x - this.lt.x, this.rb.y - this.lt.y));
                    chr.getMap().broadcastMessage(MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                    break;
                }
                case 230: {
                    option = 0;
                    monster.getRectangles().clear();
                    for (int count = 0; count < 10; ++count) {
                        int x = -659 + 180 * Randomizer.nextInt(7);
                        skillRectInfo.add(new Rectangle(x, -316, 120, 315));
                    }
                    Collections.shuffle(skillRectInfo);
                    int a = 10;
                    for (Rectangle re : skillRectInfo) {
                        monster.getRectangles().put(a, re);
                        --a;
                    }
                    chr.getMap().broadcastMessage(MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, 960, option, skillRectInfo));
                    break;
                }
                case 238: {
                    monster.getMap().getLucidDream().clear();
                    ArrayList<Point> pos = new ArrayList<Point>();
                    if (monster.getMap().getId() == 450003920 || monster.getMap().getId() == 450004250 || monster.getMap().getId() == 450004550) {
                        pos.add(new Point(670, -48));
                        pos.add(new Point(1168, -143));
                        pos.add(new Point(1046, -842));
                    } else {
                        pos.add(new Point(877, 44));
                        pos.add(new Point(264, 44));
                        pos.add(new Point(1610, 44));
                    }
                    pos.remove(Randomizer.rand(0, 2));
                    for (Point p : pos) {
                        monster.getMap().getLucidDream().add(p);
                    }
                    monster.getMap().broadcastMessage(MobPacket.BossLucid.SpawnLucidDream(pos));
                    chr.getClient().getSession().writeAndFlush((Object)MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                    break;
                }
                case 241: {
                    chr.getMap().broadcastMessage(MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, 1, option, skillRectInfo));
                    break;
                }
                case 140: 
                case 141: {
                    if (monster.getId() != 8860000 && monster.getId() != 8860005) {
                        skillAfter = Randomizer.rand(7000, 8000);
                    }
                    chr.getClient().getSession().writeAndFlush((Object)MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                    break;
                }
                case 136: {
                    switch (this.skillLevel) {
                        case 26: {
                            skillAfter = Randomizer.rand(3200, 3600);
                            chr.getClient().getSession().writeAndFlush((Object)MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                            break;
                        }
                        default: {
                            chr.getClient().getSession().writeAndFlush((Object)MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                        }
                    }
                }
                default: {
                    chr.getClient().getSession().writeAndFlush((Object)MobPacket.MobSkillDelay(monster.getObjectId(), this.skillId, this.skillLevel, skillAfter, option, skillRectInfo));
                }
            }
            chr.dropMessageGM(6, this.skillId + " / " + this.skillLevel + " 의 딜레이 패킷 보냄 : " + skillAfter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isOnlyOtherSkill() {
        return this.onlyOtherSkill;
    }

    public void setOnlyOtherSkill(boolean onlyOtherSkill) {
        this.onlyOtherSkill = onlyOtherSkill;
    }

    public int getOtherSkillID() {
        return this.otherSkillID;
    }

    public void setOtherSkillID(int otherSkillID) {
        this.otherSkillID = otherSkillID;
    }

    public int getOtherSkillLev() {
        return this.otherSkillLev;
    }

    public void setOtherSkillLev(int otherSkillLev) {
        this.otherSkillLev = otherSkillLev;
    }

    public long getSkillForbid() {
        return this.skillForbid;
    }

    public void setSkillForbid(long skillForbid) {
        this.skillForbid = skillForbid;
    }

    public int getAfterAttack() {
        return this.afterAttack;
    }

    public void setAfterAttack(int afterAttack) {
        this.afterAttack = afterAttack;
    }

    public int getAfterAttackCount() {
        return this.afterAttackCount;
    }

    public void setAfterAttackCount(int afterAttackCount) {
        this.afterAttackCount = afterAttackCount;
    }

    public int getAfterDead() {
        return this.afterDead;
    }

    public void setAfterDead(int afterDead) {
        this.afterDead = afterDead;
    }

    public boolean isMobGroup() {
        return this.isMobGroup;
    }

    public void setMobGroup(boolean isMobGroup) {
        this.isMobGroup = isMobGroup;
    }

    public int getForce() {
        return this.force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public void willSpider(MapleCharacter player, MapleMonster monster, int type, boolean solo) {
        List<Pair<Integer, Integer>> spider = new ArrayList<>();
        if (type == 0) {
            spider.add(new Pair<>(Integer.valueOf(1200), Integer.valueOf(-480)));
            spider.add(new Pair<>(Integer.valueOf(1200), Integer.valueOf(-80)));
            spider.add(new Pair<>(Integer.valueOf(1200), Integer.valueOf(320)));
            spider.add(new Pair<>(Integer.valueOf(2800), Integer.valueOf(-320)));
            spider.add(new Pair<>(Integer.valueOf(2800), Integer.valueOf(80)));
            spider.add(new Pair<>(Integer.valueOf(2800), Integer.valueOf(480)));
            spider.add(new Pair<>(Integer.valueOf(4400), Integer.valueOf(-550)));
            spider.add(new Pair<>(Integer.valueOf(4400), Integer.valueOf(-150)));
            spider.add(new Pair<>(Integer.valueOf(4400), Integer.valueOf(250)));
            spider.add(new Pair<>(Integer.valueOf(7000), Integer.valueOf(-470)));
            spider.add(new Pair<>(Integer.valueOf(7000), Integer.valueOf(-70)));
            spider.add(new Pair<>(Integer.valueOf(7000), Integer.valueOf(330)));
            spider.add(new Pair<>(Integer.valueOf(8600), Integer.valueOf(-320)));
            spider.add(new Pair<>(Integer.valueOf(8600), Integer.valueOf(80)));
            spider.add(new Pair<>(Integer.valueOf(8600), Integer.valueOf(480)));
            spider.add(new Pair<>(Integer.valueOf(10200), Integer.valueOf(-150)));
            spider.add(new Pair<>(Integer.valueOf(10200), Integer.valueOf(250)));
            spider.add(new Pair<>(Integer.valueOf(10200), Integer.valueOf(650)));
        } else {
            spider.add(new Pair<>(Integer.valueOf(1200), Integer.valueOf(-480)));
            spider.add(new Pair<>(Integer.valueOf(1200), Integer.valueOf(-80)));
            spider.add(new Pair<>(Integer.valueOf(1200), Integer.valueOf(320)));
            spider.add(new Pair<>(Integer.valueOf(2800), Integer.valueOf(-320)));
            spider.add(new Pair<>(Integer.valueOf(2800), Integer.valueOf(80)));
            spider.add(new Pair<>(Integer.valueOf(2800), Integer.valueOf(480)));
            spider.add(new Pair<>(Integer.valueOf(4400), Integer.valueOf(-550)));
            spider.add(new Pair<>(Integer.valueOf(4400), Integer.valueOf(-150)));
            spider.add(new Pair<>(Integer.valueOf(4400), Integer.valueOf(250)));
            spider.add(new Pair<>(Integer.valueOf(7000), Integer.valueOf(-480)));
            spider.add(new Pair<>(Integer.valueOf(7000), Integer.valueOf(-80)));
            spider.add(new Pair<>(Integer.valueOf(7000), Integer.valueOf(320)));
            spider.add(new Pair<>(Integer.valueOf(8600), Integer.valueOf(-320)));
            spider.add(new Pair<>(Integer.valueOf(8600), Integer.valueOf(80)));
            spider.add(new Pair<>(Integer.valueOf(8600), Integer.valueOf(480)));
            spider.add(new Pair<>(Integer.valueOf(10200), Integer.valueOf(-550)));
            spider.add(new Pair<>(Integer.valueOf(10200), Integer.valueOf(-150)));
            spider.add(new Pair<>(Integer.valueOf(10200), Integer.valueOf(250)));
        }
        if (solo) {
            spider.add(new Pair<>(Integer.valueOf(12800), Integer.valueOf(-480)));
            spider.add(new Pair<>(Integer.valueOf(12800), Integer.valueOf(-80)));
            spider.add(new Pair<>(Integer.valueOf(12800), Integer.valueOf(320)));
            spider.add(new Pair<>(Integer.valueOf(14400), Integer.valueOf(-320)));
            spider.add(new Pair<>(Integer.valueOf(14400), Integer.valueOf(80)));
            spider.add(new Pair<>(Integer.valueOf(14400), Integer.valueOf(480)));
            spider.add(new Pair<>(Integer.valueOf(16000), Integer.valueOf(-550)));
            spider.add(new Pair<>(Integer.valueOf(16000), Integer.valueOf(-150)));
            spider.add(new Pair<>(Integer.valueOf(16000), Integer.valueOf(250)));
        }
        if (spider.size() > 0) {
            player.getMap().broadcastMessage(MobPacket.BossWill.WillSpiderAttackPaten(monster.getObjectId(), type, spider));
        }
    }
}
