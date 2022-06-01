package server.field.boss;

import client.MapleCharacter;
import client.SecondaryStat;
import client.SkillFactory;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import handling.world.MaplePartyCharacter;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import server.Obstacle;
import server.Randomizer;
import server.Timer;
import server.field.boss.dunkel.DunkelEliteBoss;
import server.field.boss.lucid.Butterfly;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.life.Transform;
import server.maps.MapleMap;
import server.maps.MapleNodes;
import tools.Pair;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;

public class MapleBossManager {

    public static void changePhase(MapleMonster monster) {
        if (monster.getId() >= 8800102 && monster.getId() <= 8800110) {
            int[] arms = {8800103, 8800104, 8800105, 8800106, 8800107, 8800108, 8800109, 8800110};
            if (monster.getPhase() == 0) {
                monster.setPhase((byte) 1);
            }
            if (monster.getId() == 8800102) {
                boolean nextPhase = true;
                for (int arm : arms) {
                    if (monster.getMap().getMonsterById(arm) != null) {
                        nextPhase = false;
                        break;
                    }
                }
                if (nextPhase) {
                    monster.setPhase((byte) 3);
                }
                if (monster.getHPPercent() <= 20) {
                    monster.setPhase((byte) 4);
                    for (int arm : arms) {
                        monster.getMap().killMonster(arm);
                    }
                }
            }
            monster.getMap().broadcastMessage(MobPacket.changePhase(monster));
        } else if (monster.getId() == 8880000 || monster.getId() == 8880002 || monster.getId() == 8880010) {
            byte phase;
            if (monster.getHPPercent() <= 25) {
                phase = 4;
            } else if (monster.getHPPercent() <= 50) {
                phase = 3;
            } else if (monster.getHPPercent() <= 75) {
                phase = 2;
            } else {
                phase = 1;
            }
            if (monster.getPhase() != phase) {
                monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "매그너스가 구와르를 제어하는 힘이 약화 되었습니다. 구와르의 기운이 더욱 강해집니다."));
                monster.setPhase(phase);
                monster.getMap().broadcastMessage(MobPacket.changePhase(monster));
            }
        }
    }

    public static void setBlockAttack(MapleMonster monster) {
        List<Integer> blocks = new ArrayList<>();
        switch (monster.getId()) {
            case 8800102:
                if (monster.getPhase() != 2) {
                    monster.getMap().killMonster(8800117);
                    List<String> updateLists = new ArrayList<>();
                    monster.getMap().updateEnvironment(updateLists);
                    blocks.add(Integer.valueOf(1));
                }
                if (monster.getPhase() != 3) {
                    blocks.add(Integer.valueOf(2));
                    blocks.add(Integer.valueOf(3));
                    blocks.add(Integer.valueOf(4));
                    blocks.add(Integer.valueOf(5));
                }
                if (monster.getPhase() != 4) {
                    blocks.add(Integer.valueOf(6));
                    blocks.add(Integer.valueOf(7));
                    blocks.add(Integer.valueOf(8));
                }
                break;
            case 8850011:
            case 8850111:
                blocks.add(Integer.valueOf(4));
                break;
            case 8910000:
            case 8910100:
                if (monster.getHPPercent() > 10) {
                    blocks.add(Integer.valueOf(3));
                    blocks.add(Integer.valueOf(4));
                    blocks.add(Integer.valueOf(5));
                    blocks.add(Integer.valueOf(6));
                    blocks.add(Integer.valueOf(7));
                }
                if (monster.getHPPercent() > 70) {
                    blocks.add(Integer.valueOf(2));
                }
                break;
            case 8930000:
            case 8930100:
                if (monster.getId() == 8930100 && monster.getHPPercent() > 70) {
                    blocks.add(Integer.valueOf(2));
                    blocks.add(Integer.valueOf(3));
                    blocks.add(Integer.valueOf(4));
                }
                if (monster.getHPPercent() > 40) {
                    blocks.add(Integer.valueOf(8));
                    blocks.add(Integer.valueOf(9));
                    blocks.add(Integer.valueOf(10));
                    blocks.add(Integer.valueOf(12));
                    blocks.add(Integer.valueOf(13));
                    blocks.add(Integer.valueOf(14));
                    blocks.add(Integer.valueOf(15));
                }
                break;
            case 8880300:
            case 8880301:
            case 8880303:
            case 8880304:
            case 8880321:
            case 8880322:
            case 8880325:
            case 8880326:
            case 8880340:
            case 8880341:
            case 8880343:
            case 8880344:
            case 8880351:
            case 8880352:
            case 8880355:
            case 8880356:
                return;
        }
        monster.getMap().broadcastMessage(MobPacket.BlockAttack(monster, blocks));
    }

    public static void ZakumBodyHandler(MapleMonster monster, MapleCharacter chr, boolean facingLeft) {
        long time = System.currentTimeMillis();
        List<MobSkill> useableSkills = new ArrayList<>();
        for (MobSkill msi : monster.getSkills()) {
            if (time - monster.getLastSkillUsed(msi.getSkillId(), msi.getSkillLevel()) >= 0L
                    && monster.getHPPercent() <= msi.getHP() && !msi.isOnlyOtherSkill()) {
                if (msi.isOnlyFsm()) {
                    if (monster.getPhase() == 2) {
                        if (msi.getSkillId() == 201 && msi.getSkillLevel() == 162) {
                            useableSkills.add(msi);
                        }
                        continue;
                    }
                    if (monster.getPhase() == 3
                            && msi.getSkillId() == 176 && msi.getSkillLevel() == 27
                            && msi.getSkillId() != 201 && msi.getSkillLevel() != 162) {
                        useableSkills.add(msi);
                    }
                    continue;
                }
                useableSkills.add(msi);
            }
        }
        if (!useableSkills.isEmpty()) {
            MobSkill msi = useableSkills.get(Randomizer.nextInt(useableSkills.size()));
            monster.setLastSkillUsed(msi, time, msi.getInterval());
            monster.setNextSkill(msi.getSkillId());
            monster.setNextSkillLvl(msi.getSkillLevel());
        }
    }

    public static void ZakumArmHandler(MapleMonster monster, MapleCharacter chr, int actionAndDir) {
        if ((monster.getId() == 8800002 || (monster.getId() == 8800102 && monster.getPhase() <= 2))
                && monster.getCustomValue(8800002) == null) {
            if (monster.getPhase() == 1) {
                Map<Integer, Integer> list = new LinkedHashMap<>();
                boolean changephase = false;
                if (monster.getCustomValue0(8800003) == 2L || monster.getCustomValue0(8800003) == 3L || monster.getCustomValue0(8800003) == 4L) {
                    monster.addSkillCustomInfo(8800004, 1L);
                    if (monster.getCustomValue0(8800004) == 3L) {
                        monster.addSkillCustomInfo(8800003, (monster.getCustomValue0(8800003) == 4L) ? 2L : 1L);
                        monster.removeCustomInfo(8800004);
                    }
                    monster.setCustomInfo(8800002, 0, 2000);
                } else if (monster.getCustomValue0(8800003) == 6L) {
                    monster.addSkillCustomInfo(8800004, 1L);
                    if (monster.getCustomValue0(8800004) == 2L) {
                        monster.setCustomInfo(8800003, 2, 0);
                        monster.removeCustomInfo(8800004);
                        monster.addSkillCustomInfo(8800005, 1L);
                        if (monster.getCustomValue0(8800005) == 2L) {
                            monster.removeCustomInfo(8800005);
                            for (MapleMonster mob : monster.getMap().getAllMonster()) {
                                if ((mob.getId() >= 8800002 && mob.getId() <= 8800010) || (mob.getId() >= 8800102 && mob.getId() <= 8800110)) {
                                    mob.setPhase((byte) 2);
                                    mob.getMap().broadcastMessage(MobPacket.changePhase(mob));
                                    changephase = true;
                                }
                            }
                        }
                        monster.setCustomInfo(8800002, 0, 4000);
                    } else {
                        monster.setCustomInfo(8800002, 0, 5300);
                    }
                }
                if (!changephase) {
                    int mobsize = 0;
                    for (MapleMonster mob : monster.getMap().getAllMonster()) {
                        if ((mob.getId() >= 8800002 && mob.getId() <= 8800010) || (mob.getId() >= 8800102 && mob.getId() <= 8800110)) {
                            mobsize++;
                        }
                    }
                    int sized = (int) monster.getCustomValue0(8800003);
                    if (mobsize < monster.getCustomValue0(8800003)) {
                        sized = mobsize;
                    }
                    while (list.size() < sized) {
                        int randMob = Randomizer.rand(8800003, 8800010);
                        if (monster.getId() == 8800102) {
                            randMob += 100;
                        }
                        if (!list.containsKey(Integer.valueOf(randMob))) {
                            list.put(Integer.valueOf(randMob), Integer.valueOf(0));
                        }
                    }
                    for (MapleMonster mob : monster.getMap().getAllMonster()) {
                        for (Map.Entry<Integer, Integer> mobs : list.entrySet()) {
                            if (((Integer) mobs.getKey()).intValue() == mob.getId()) {
                                if (monster.getId() == 8800102) {
                                    MobSkill msi = mob.getStats().getSkill(176, (monster.getCustomValue0(8800003) == 6L) ? 26 : 25);
                                    msi.setMobSkillDelay(chr, mob, (monster.getCustomValue0(8800003) == 6L) ? 2430 : 1420, (short) 0, ((actionAndDir & 0x1) != 0));
                                } else {
                                    MobSkill msi = mob.getStats().getSkill(176, (monster.getCustomValue0(8800003) == 6L) ? 34 : 33);
                                    msi.setMobSkillDelay(chr, mob, (monster.getCustomValue0(8800003) == 6L) ? 2430 : 1420, (short) 0, ((actionAndDir & 0x1) != 0));
                                }
                                mob.getMap().broadcastMessage(MobPacket.setAttackZakumArm(mob.getObjectId(), (monster.getCustomValue0(8800003) == 6L) ? 1 : 0));
                            }
                        }
                    }
                } else if (changephase) {
                    List<MapleNodes.Environment> envs = new ArrayList<>();
                    for (MapleNodes.Environment env : monster.getMap().getNodez().getEnvironments()) {
                        if (env.getName().contains("zdc")) {
                            env.setShow(true);
                            envs.add(env);
                        }
                    }
                    monster.getMap().broadcastMessage(CField.getUpdateEnvironment(envs));
                    server.Timer.MapTimer.getInstance().schedule(() -> {
                        int monsterid = 8800117;
                        monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(monsterid), new Point(-60, 86));
                    },
                            1500L);
                    monster.setCustomInfo(8800002, 0, 3000);
                }
            } else if (monster.getPhase() == 2) {
                int mobid = 8800000;
                if (monster.getId() == 8800102) {
                    mobid += 100;
                }
                MapleMonster firstmob = null;
                MapleMonster secondmob = null;
                int whilsize = 0;
                while (firstmob == null || secondmob == null) {
                    int rand = Randomizer.rand(3, 5);
                    for (MapleMonster mob : monster.getMap().getAllMonster()) {
                        if (mobid + rand == mob.getId()) {
                            firstmob = mob;
                            continue;
                        }
                        if (mobid + rand + 4 == mob.getId()) {
                            secondmob = mob;
                        }
                    }
                    if (firstmob != null && secondmob != null) {
                        MobSkill msi = firstmob.getStats().getSkill(176, 27);
                        msi.setMobSkillDelay(chr, firstmob, 1800, (short) 0, ((actionAndDir & 0x1) != 0));
                        firstmob.getMap().broadcastMessage(MobPacket.setAttackZakumArm(firstmob.getObjectId(), 2));
                        msi = secondmob.getStats().getSkill(176, 27);
                        msi.setMobSkillDelay(chr, secondmob, 1800, (short) 0, ((actionAndDir & 0x1) != 0));
                        firstmob.getMap().broadcastMessage(MobPacket.setAttackZakumArm(secondmob.getObjectId(), 2));
                        break;
                    }
                    whilsize++;
                    if (whilsize == 100) {
                        break;
                    }
                }
                monster.addSkillCustomInfo(8800004, 1L);
                if (monster.getCustomValue0(8800004) == 11L) {
                    monster.removeCustomInfo(8800004);
                    List<MapleNodes.Environment> envs = new ArrayList<>();
                    for (MapleNodes.Environment env : monster.getMap().getNodez().getEnvironments()) {
                        if (env.getName().contains("zdc")) {
                            env.setShow(false);
                            envs.add(env);
                        }
                    }
                    monster.getMap().broadcastMessage(CField.getUpdateEnvironment(envs));
                    for (MapleMonster mob : monster.getMap().getAllMonster()) {
                        if ((mob.getId() >= 8800002 && mob.getId() <= 8800010) || (mob.getId() >= 8800102 && mob.getId() <= 8800110)) {
                            mob.setPhase((byte) 1);
                            mob.getMap().broadcastMessage(MobPacket.changePhase(mob));
                            continue;
                        }
                        if (mob.getId() == 8800117 || mob.getId() == 8800120) {
                            mob.getMap().killMonster(mob.getId());
                        }
                    }
                }
                monster.setCustomInfo(8800002, 0, 5000);
            }
        }
    }

    public static void magnusHandler(MapleMonster monster, int type, int actionAndDir) {
        if (monster != null) {
            if (type == 0) {
                int[] types = {1, 2, 3, 4, 5, 6, 7, 8, 9};
                List<Obstacle> obs = new ArrayList<>();
                int size = Randomizer.rand((monster.getPhase() == 1) ? 5 : (3 * monster.getPhase()), 8 + monster.getPhase());
                for (int i = 0; i < size; i++) {
                    Obstacle ob;
                    int obtype = types[Randomizer.nextInt(types.length)];
                    int x = Randomizer.rand(550, 3050);
                    if (type <= 5) {
                        ob = new Obstacle(obtype, new Point(x, -2000), new Point(x, -1347), 25, (monster.getId() == 8880010) ? 10 : 50, 1459, Randomizer.rand(80, 230), 1, 653, 0);
                    } else if (type <= 7) {
                        ob = new Obstacle(obtype, new Point(x, -2000), new Point(x, -1347), 45, (monster.getId() == 8880010) ? 30 : 100, 1481, Randomizer.rand(80, 230), 1, 653, 0);
                    } else {
                        ob = new Obstacle(obtype, new Point(x, -2000), new Point(x, -1347), 65, (monster.getId() == 8880010) ? 50 : 100, 542, Randomizer.rand(50, 270), 2, 653, 0);
                    }
                    obs.add(ob);
                }
                monster.getMap().broadcastMessage(MobPacket.createObstacle(monster, obs, (byte) 0));
            } else if (type == 1) {
                if (monster.getCustomValue(monster.getId()) == null) {
                    int time = Randomizer.rand(20000, 40000);
                    int objtype = Randomizer.rand(1, 2);
                    int[] randlist = {1, 3, 4, 5};
                    if (objtype == 1) {
                        int count = Randomizer.rand(1, 3);
                        int i = 0;
                        List<Integer> listed = new ArrayList<>();
                        listed.add(Integer.valueOf(1));
                        listed.add(Integer.valueOf(3));
                        listed.add(Integer.valueOf(4));
                        listed.add(Integer.valueOf(5));
                        Collections.shuffle(listed);
                        for (Integer a : listed) {
                            int[] randlist2 = {a.intValue()};
                            int delay = Randomizer.isSuccess(50) ? 0 : Randomizer.rand(1000, 1100);
                            if (delay > 0) {
                                server.Timer.MapTimer.getInstance().schedule(() -> monster.getMap().broadcastMessage(CField.DebuffObjON(randlist2, (monster.getId() == 8880000))), delay);
                            } else {
                                monster.getMap().broadcastMessage(CField.DebuffObjON(randlist2, (monster.getId() == 8880000)));
                            }
                            i++;
                            if (i == count) {
                                break;
                            }
                        }
                    } else if (objtype == 2) {
                        monster.getMap().broadcastMessage(CField.DebuffObjON(randlist, (monster.getId() == 8880000)));
                    }
                    monster.setCustomInfo(monster.getId(), 0, (monster.getPhase() > 0) ? (time - monster.getPhase() * 1000) : 30000);
                }
            } else if (type == 2) {
                if (actionAndDir == 27 || actionAndDir == 26) {
                    monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "매그너스가 주변의 적들을 뿌리치려 합니다."));
                }
                if (actionAndDir == 34 || actionAndDir == 35) {
                    monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "매그너스가 남은 적을 처리하기 위해 연속 공격을 시전합니다."));
                }
                if (actionAndDir == 60) {
                    monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "매그너스가 느려진 적들을 향해 강력한 일격을 준비 합니다."));
                }
                if (actionAndDir == 62) {
                    monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "매그너스가 회복/강화 하는 적을 보고 대응책을 준비합니다."));
                }
                AggressIve(monster);
            }
        }
    }

    public static void HillaHandler(MapleMonster monster, MapleCharacter chr, int actionAndDir) {
        if (actionAndDir != -1) {
            if (monster.getId() == 8870100) {
                if (actionAndDir == 76 || actionAndDir == 77) {
                    chr.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 3, 0));
                } else if (actionAndDir == 36 || actionAndDir == 37) {
                    chr.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 1, 0));
                }
            } else if (actionAndDir == 30 || actionAndDir == 31) {
                chr.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 0, 0));
            } else if (actionAndDir == 34 || actionAndDir == 35) {
                chr.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 1, 0));
            }
        }
    }

    public static void lotusHandler(MapleMonster monster) {
        if (monster != null) {
            boolean hard = (monster.getId() == 8950000 || monster.getId() == 8950001 || monster.getId() == 8950002);
            int[] types = {48, 49, 50, 51, 52};
            List<Obstacle> obs = new ArrayList<>();
            int aa = monster.getId() % 10;
            int size = Randomizer.rand(1, 4);
            if (aa == 1) {
                size = Randomizer.rand(2, 5);
            } else if (aa == 2) {
                size = Randomizer.rand(2, 5);
            }
            List<Pair<Integer, Integer>> random = new ArrayList<>();
            if (aa == 1) {
                random.add(new Pair<>(Integer.valueOf(48), Integer.valueOf(hard ? 2000 : 4000)));
                random.add(new Pair<>(Integer.valueOf(49), Integer.valueOf(hard ? 3000 : 3000)));
                random.add(new Pair<>(Integer.valueOf(50), Integer.valueOf(hard ? 3000 : 3000)));
                random.add(new Pair<>(Integer.valueOf(51), Integer.valueOf(hard ? 4000 : 2000)));
            } else if (aa == 2) {
                random.add(new Pair<>(Integer.valueOf(48), Integer.valueOf(hard ? 2000 : 2000)));
                random.add(new Pair<>(Integer.valueOf(49), Integer.valueOf(hard ? 2000 : 3000)));
                random.add(new Pair<>(Integer.valueOf(50), Integer.valueOf(hard ? 2000 : 2000)));
                random.add(new Pair<>(Integer.valueOf(51), Integer.valueOf(hard ? 3000 : 2500)));
                random.add(new Pair<>(Integer.valueOf(52), Integer.valueOf(hard ? 1000 : 500)));
            } else {
                random.add(new Pair<>(Integer.valueOf(48), Integer.valueOf(hard ? 3000 : 5000)));
                random.add(new Pair<>(Integer.valueOf(49), Integer.valueOf(hard ? 3000 : 3000)));
                random.add(new Pair<>(Integer.valueOf(50), Integer.valueOf(hard ? 4000 : 2000)));
            }
            for (int i = 0; i < size; i++) {
                Obstacle ob;
                int type = GameConstants.getWeightedRandom(random);
                int x = Randomizer.rand(-600, 650);
                if (type == 48) {
                    ob = new Obstacle(48, new Point(x, -520), new Point(x, -16), 36, 10, Randomizer.rand(0, 1000), Randomizer.rand(250, 450), 1, 504, 0);
                    if (!hard) {
                        ob.setEffect(false);
                    }
                } else if (type == 49) {
                    ob = new Obstacle(49, new Point(x, -520), new Point(x, -16), 51, hard ? 20 : 10, Randomizer.rand(0, 1000), Randomizer.rand(200, 350), 1, 504, 0);
                } else if (type == 50) {
                    ob = new Obstacle(50, new Point(x, -520), new Point(x, -16), 51, hard ? 30 : 20, Randomizer.rand(0, 1000), Randomizer.rand(100, 200), 2, 504, 0);
                } else if (type == 51) {
                    ob = new Obstacle(51, new Point(x, -520), new Point(x, -16), 65, hard ? 50 : 40, Randomizer.rand(0, 1000), Randomizer.rand(100, 200), 2, 504, 0);
                } else {
                    ob = new Obstacle(52, new Point(x, -520), new Point(x, -16), 190, 100, 190, Randomizer.rand(150, 250), 1, 504, 0);
                }
                obs.add(ob);
            }
            monster.getMap().CreateObstacle(monster, obs);
        }
    }

    public static void duskHandler(MapleMonster dusk, MapleMap map) {
        dusk.setSchedule(Timer.MobTimer.getInstance().register(() -> {
            if ((map.getId() == 450009400 || map.getId() == 450009450) && dusk != null) {
                if ((dusk.getEventInstance() == null || !dusk.isAlive()) && dusk.getMap().getCharacters().size() <= 0) {
                    dusk.getSchedule().cancel(true);
                    dusk.setSchedule((ScheduledFuture<?>) null);
                    dusk.getMap().killMonsterType(dusk, 0);
                    return;
                }
                if (dusk.getCustomValue(86446501) == null) {
                    dusk.setCustomInfo(86446501, 0, 1400);
                    map.getDuskObtacles(dusk, Randomizer.rand(0, 21));
                }
                for (MapleMonster mob : map.getAllMonster()) {
                    if (mob != null) {
                        if ((mob.getId() == 8644650 || mob.getId() == 8644655) && map.getCustomValue(8644650) == null) {
                            map.setCustomInfo(8644651, 1, 0);
                            map.setCustomInfo(8644650, 0, 80000);
                            map.broadcastMessage(CField.getFieldSkillDuskAdd(100020, (mob.getId() == 8644650) ? 1 : 3, new Point(0, 0), false, true));
                            map.broadcastMessage(CField.enforceMSG("방어하던 촉수로 강력한 공격을 할거예요! 버텨낸다면 드러난 공허의 눈을 공격할 수 있어요!", 250, 3000));
                            mob.setPhase((byte) 0);
                            mob.setCustomInfo(100023, 0, 0);
                            mob.setCustomInfo(100024, 0, 40000);
                            map.broadcastMessage(MobPacket.changeMobZone(mob));
                        Timer.MapTimer.getInstance().schedule(() -> {
                            dusk.setCustomInfo(18611, 1, 0);
                            int i = 0;
                            while (dusk.getCustomValue0(18611) == 1L) {
                                dusk.setNextSkill(186);
                                dusk.setNextSkillLvl(11);
                                ++i;
                            }
                        }, 24000L);
                        Timer.MapTimer.getInstance().schedule(() -> {
                            mob.setPhase((byte)1);
                            mob.setCustomInfo(100023, 1, 0);
                            map.broadcastMessage(MobPacket.changeMobZone(mob));
                            map.removeCustomInfo(8644651);
                        }, 37000L);
                        break;
                        }//mush
                        if (mob.getId() == 8644658 || mob.getId() == 8644659) {
                            if (mob.getCustomValue(100021) == null) {
                                mob.setCustomInfo(100021, 0, Randomizer.rand(5000, 10000));
                                if (map.getCustomValue0(8644651) == 0) {
                                    map.broadcastMessage(MobPacket.enableOnlyFsmAttack(mob, 2, 0));
                                }
                                break;
                            }
                            if (mob.getCustomValue(100020) == null) {
                                mob.setCustomInfo(100020, 0, Randomizer.rand(10000, 13000));
                                int randx = Randomizer.rand(-500, 500);
                                if (map.getCustomValue0(8644651) == 0) {
                                    map.broadcastMessage(CField.getFieldSkillDuskAdd(100020, (mob.getId() == 8644658) ? 2 : 4, new Point(randx, -157), !(randx < 0), false));
                                }
                            }
                            break;
                        }
                        if ((mob.getId() == 8644650 || mob.getId() == 8644655) && mob.getCustomValue(100024) == null && mob.getCustomValue0(100023) == 0L) {
                            mob.setPhase((byte) 1);
                            map.broadcastMessage(MobPacket.changeMobZone(mob));
                            map.removeCustomInfo(8644651);
                            mob.setCustomInfo(100023, 0, 0);
                        }
                    }
                }
                for (MapleCharacter chr : map.getAllChracater()) {
                    if (chr != null) {
                        if (chr.getBuffedValue(80002902)) {
                            if (chr.getSkillCustomValue(80002902) == null) {
                                chr.getClient().send(CField.getFieldSkillEffectAdd(100022, 1, (chr.getMapId() == 450009400) ? 8644658 : 8644659));
                                chr.setSkillCustomInfo(80002902, 0L, 5000L);
                            }
                            int mobsize1 = 0;
                            int mobsize2 = 0;
                            for (MapleMonster mo : map.getAllMonster()) {
                                if (mo.getOwner() == chr.getId()) {
                                    if (mo.getId() == 8644653 || mo.getId() == 8644656) {
                                        mobsize1++;
                                        continue;
                                    }
                                    if (mo.getId() == 8644654 || mo.getId() == 8644657) {
                                        mobsize2++;
                                    }
                                }
                            }
                            if (mobsize1 < 2 && map.getCustomValue(450009400) == null) {
                                for (int i = 0; i < Randomizer.rand(1, 2); i++) {
                                    MapleMonster mob2 = MapleLifeFactory.getMonster((chr.getMapId() == 450009400) ? 8644653 : 8644656);
                                    mob2.setOwner(chr.getId());
                                    map.spawnMonsterOnGroundBelow(mob2, new Point(Randomizer.rand(-600, 600), Randomizer.rand(-600, -157)));
                                    map.setCustomInfo(450009400, 0, 2000);
                                }
                            }
                            if (mobsize2 < 4) {
                                int[] randx = {-520, -380, -240, -100, 40, 180, 320, 460, 600};
                                while (mobsize2 < 4) {
                                    mobsize2 = 0;
                                    for (MapleMonster mo : map.getAllMonster()) {
                                        if (mo.getOwner() == chr.getId()) {
                                            if (mo.getId() == 8644653 || mo.getId() == 8644656) {
                                                mobsize1++;
                                                continue;
                                            }
                                            if (mo.getId() == 8644654 || mo.getId() == 8644657) {
                                                mobsize2++;
                                            }
                                        }
                                    }
                                    MapleMonster mob2 = MapleLifeFactory.getMonster((chr.getMapId() == 450009400) ? 8644654 : 8644657);
                                    mob2.setOwner(chr.getId());
                                    map.spawnMonsterOnGroundBelow(mob2, new Point(randx[Randomizer.rand(0, randx.length - 1)], -157));
                                }
                            }
                        }
                        if (chr.getSkillCustomValue0(8644650) == 1L) {
                            chr.setSkillCustomInfo(8644651, chr.getSkillCustomValue0(8644651) + 7L, 0L);
                            chr.getClient().send(MobPacket.handleDuskGauge(false, (int) chr.getSkillCustomValue0(8644651), 1000));
                            if (chr.getSkillCustomValue0(8644651) >= 1000L) {
                                chr.setSkillCustomInfo(8644650, 2L, 0L);
                                chr.setSkillCustomInfo(8644651, 1000L, 0L);
                                SkillFactory.getSkill(80002902).getEffect(1).applyTo(chr);
                            }
                            continue;
                        }
                        if (chr.getSkillCustomValue0(8644650) == 2L) {
                            if (chr.isAlive()) {
                                chr.setSkillCustomInfo(8644651, chr.getSkillCustomValue0(8644651) - 45L, 0L);
                                chr.getClient().send(MobPacket.handleDuskGauge(true, (int) chr.getSkillCustomValue0(8644651), 1000));
                                if (chr.getSkillCustomValue0(8644651) <= 0L) {
                                    chr.setSkillCustomInfo(8644650, 1L, 0L);
                                    chr.setSkillCustomInfo(8644651, 0L, 0L);
                                    while (chr.getBuffedValue(80002902)) {
                                        chr.cancelEffect(chr.getBuffedEffect(80002902));
                                    }
                                    for (MapleMonster m : map.getAllMonster()) {
                                        if (m.getOwner() == chr.getId() && (m.getId() == 8644653 || m.getId() == 8644656 || m.getId() == 8644654 || m.getId() == 8644657)) {
                                            map.killMonster(m);
                                        }
                                    }
                                }
                            }
                            continue;
                        }
                        chr.setSkillCustomInfo(8644650, 1L, 0L);
                    }
                }
            }
        },
                1000L));
    }

    public static void DemianStigmaGive(MapleMonster monster) {
        if (monster == null) {
            return;
        }
        String[] EffectMsgs = {"데미안이 가장 위협적인 적에게 낙인을 새깁니다.", "데미안이 낙인이 가장 많은 적에게 낙인을 새깁니다.", "데미안이 낙인이 가장 적은 적에게 낙인을 새깁니다.", "데미안이 누구에게 낙인을 새길지 알 수 없습니다."};
        int time = (monster.getHPPercent() >= 50) ? 28000 : 18000;
        monster.setStigmaType(Randomizer.rand(0, 3));
        monster.getMap().broadcastMessage(CField.StigmaTime(time));
        monster.getMap().broadcastMessage(CField.enforceMSG(EffectMsgs[monster.getStigmaType()], 216, 30000000));
        monster.setSpecialtxt(EffectMsgs[monster.getStigmaType()]);
        Timer.MobTimer.getInstance().schedule(() -> {
            int size = monster.getMap().getAllCharactersThreadsafe().size();
            if (size > 0) {
                MapleCharacter chr = null;
                if (monster.getStigmaType() < 3) {
                    chr = RandCharacter(monster, monster.getStigmaType(), !(monster.getStigmaType() == 2));
                }
                if (chr == null) {
                    chr = monster.getMap().getAllCharactersThreadsafe().get(Randomizer.nextInt(size));
                }
                if (chr != null) {
                    MobSkill ms = MobSkillFactory.getMobSkill(237, 1);
                    ms.applyEffect(chr, monster, true, monster.isFacingLeft());
                }
                if (monster.isAlive()) {
                    DemianStigmaGive(monster);
                }
            }
        }, time
        );
    }

    public static MapleCharacter RandCharacter(MapleMonster monster, int type, boolean first) {
        MapleCharacter chr = null;
        List<Pair<String, Long>> a2 = new ArrayList<>();
        for (MaplePartyCharacter c : monster.getController().getParty().getMembers()) {
            if (c.getPlayer() != null) {
                long value = (type == 0) ? c.getPlayer().getAggressiveDamage() : (c.getPlayer()).Stigma;
                a2.add(new Pair<>(c.getName(), Long.valueOf(value)));
            }
        }
        for (int i = 0; i < a2.size() - 1; i++) {
            for (int j = 0; j < a2.size() - i - 1; j++) {
                if (((Long) ((Pair) a2.get(j)).getRight()).longValue() < ((Long) ((Pair) a2.get(j + 1)).getRight()).longValue()) {
                    String chridtmp = (String) ((Pair) a2.get(j + 1)).getLeft();
                    long chrpointtmp = ((Long) ((Pair) a2.get(j + 1)).getRight()).longValue();
                    a2.set(j + 1, a2.get(j));
                    a2.set(j, new Pair<>(chridtmp, Long.valueOf(chrpointtmp)));
                }
            }
        }
        if (first) {
            chr = monster.getMap().getCharacterByName((String) ((Pair) a2.get(0)).getLeft());
        } else {
            chr = monster.getMap().getCharacterByName((String) ((Pair) a2.get(a2.size() - 1)).getLeft());
        }
        return chr;
    }

    public static void demianHandler(MapleMonster monster) {
        if (monster.getId() == 8880100 || monster.getId() == 8880110 || monster.getId() == 8880101 || monster.getId() == 8880111) {
            DemianStigmaGive(monster);
        }
    }

    public static void pierreHandler(MapleMonster monster) {
        monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
            if ((monster.getEventInstance() == null || !monster.isAlive()) && monster.getMap().getCharacters().size() <= 0) {
                monster.getSchedule().cancel(true);
                monster.setSchedule((ScheduledFuture<?>) null);
                monster.getMap().killMonsterType(monster, 0);
                return;
            }
            if (monster.getSeperateSoul() > 0) {
                return;
            }
            if (monster.getCustomValue(111645) == null) {
                List<Point> pos = new ArrayList<>();
                int type = Randomizer.rand(1, 2);
                for (int i = 0; i < 11; i++) {
                    if (type == 1) {
                        if (Randomizer.isSuccess(50)) {
                            pos.add(new Point(-400 + i * 180, 551));
                        }
                    } else if (type == 2 && Randomizer.isSuccess(50)) {
                        pos.add(new Point(-310 + i * 180, 551));
                    }
                }
                monster.getMap().broadcastMessage(MobPacket.dropStone("CapEffect", pos));
                monster.setCustomInfo(111645, 0, Randomizer.rand(5000, 11000));
            }
            boolean Chaos = !(monster.getId() % 1000 >= 100);
            Transform trans = null;
            if (Chaos) {
                trans = monster.getStats().getTrans();
            }
            String monstercolor = (monster.getId() % 10 == 0) ? "Purple" : ((monster.getId() % 10 == 1) ? "Red" : "Blue");
            int time = Chaos ? trans.getDuration() : (monstercolor.equals("Purple") ? 10 : 20);
            if ((System.currentTimeMillis() - monster.lastCapTime >= (time * 1000) && monster.lastCapTime != 0L) || monster.lastCapTime == 1L) {
                int TransFormHpPercent = 70;
                int DevideHpPercent = 31;
                boolean Trans = (monster.getHPPercent() <= TransFormHpPercent);
                boolean Devide = (monster.getHPPercent() <= DevideHpPercent);
                if (!Chaos) {
                    Devide = false;
                    Trans = true;
                }
                if (monster.getCustomValue0(8900000) == 1L) {
                    Devide = true;
                    Trans = true;
                }
                if (Trans && !Devide) {
                    if ((monstercolor.equals("Purple") || monstercolor.equals("Red") || monstercolor.equals("Blue")) && TransFormHpPercent >= monster.getHPPercent()) {
                        MapleMonster copy = MapleLifeFactory.getMonster(Randomizer.nextBoolean() ? (monster.getId() + 1) : (monster.getId() + 2));
                        if (monstercolor.equals("Red") || monstercolor.equals("Blue")) {
                            int minus = monstercolor.equals("Red") ? 1 : 2;
                            copy = MapleLifeFactory.getMonster(monster.getId() - minus);
                        }
                        copy.setHp(monster.getHp());
                        copy.lastCapTime = System.currentTimeMillis();
                        if (monster.getEventInstance() != null) {
                            monster.getEventInstance().registerMonster(copy);
                        }
                        monster.getMap().spawnMonsterWithEffect(copy, 254, monster.getTruePosition());
                        monster.getMap().killMonster(monster, monster.getController(), false, false, (byte) 0);
                        monster.getMap().broadcastMessage(MobPacket.showBossHP(monster.getId(), -1L, 0L));
                        for (MapleCharacter chr : copy.getMap().getAllCharactersThreadsafe()) {
                            Pair<Integer, Integer> skill = new Pair<>(Integer.valueOf(Randomizer.rand(189, 190)), Integer.valueOf(1));
                            if (chr.isAlive()) {
                                chr.cancelDisease(SecondaryStat.CapDebuff);
                                if (chr.getBuffedValue(SecondaryStat.NotDamaged) == null && chr.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
                                    MobSkillFactory.getMobSkill(((Integer) skill.left).intValue(), ((Integer) skill.right).intValue()).applyEffect(chr, copy, true, copy.isFacingLeft());
                                }
                            }
                        }
                    }
                } else if (Trans && Devide && Chaos) {
                    if (monster.getCustomValue(8900000) == null || monster.getCustomValue0(8900000) == 0L) {
                        MapleMonster copy = MapleLifeFactory.getMonster(8900001);
                        MapleMonster copy2 = MapleLifeFactory.getMonster(8900002);
                        copy.setCustomInfo(8900000, 1, 0);
                        copy2.setCustomInfo(8900000, 1, 0);
                        copy.lastCapTime = System.currentTimeMillis();
                        copy2.lastCapTime = System.currentTimeMillis();
                        long hp = (monster.getHp() <= 0L) ? (monster.getStats().getHp() * 10L / 100L) : monster.getHp();
                        copy.setHp(hp);
                        copy2.setHp(hp);
                        Point mobpos = new Point((monster.getTruePosition()).x + 150, (monster.getTruePosition()).y);
                        if (monster.getMap().getFootholds().findBelow(mobpos) == null) {
                            mobpos = monster.getPosition();
                        }
                        Point mobpos2 = new Point((monster.getTruePosition()).x - 150, (monster.getTruePosition()).y);
                        if (monster.getMap().getFootholds().findBelow(mobpos2) == null) {
                            mobpos2 = monster.getPosition();
                        }
                        if (monster.getEventInstance() != null) {
                            monster.getEventInstance().registerMonster(copy);
                            monster.getEventInstance().registerMonster(copy2);
                        }
                        monster.getMap().spawnMonsterWithEffect(copy, 254, mobpos);
                        monster.getMap().spawnMonsterWithEffect(copy2, 254, mobpos2);
                        List<Pair<MonsterStatus, MonsterStatusEffect>> stats = new ArrayList<>();
                        stats.add(new Pair<>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(187, 210000000, 70L)));
                        stats.add(new Pair<>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(187, 210000000, 70L)));
                        stats.add(new Pair<>(MonsterStatus.MS_Pad, new MonsterStatusEffect(187, 210000000, 17000L)));
                        stats.add(new Pair<>(MonsterStatus.MS_Mad, new MonsterStatusEffect(187, 210000000, 17000L)));
                        copy.applyMonsterBuff(copy.getMap(), stats, MobSkillFactory.getMobSkill(187, 1));
                        copy2.applyMonsterBuff(copy2.getMap(), stats, MobSkillFactory.getMobSkill(187, 1));
                        monster.getMap().killMonster(monster, monster.getController(), false, false, (byte) 0);
                        monster.getMap().broadcastMessage(MobPacket.showBossHP(monster.getId(), -1L, 0L));
                        for (MapleCharacter chr : copy.getMap().getAllCharactersThreadsafe()) {
                            Pair<Integer, Integer> skill = trans.getSkills().get(Randomizer.nextInt(trans.getSkills().size()));
                            if (chr.isAlive()) {
                                chr.cancelDisease(SecondaryStat.CapDebuff);
                                if (chr.getBuffedValue(SecondaryStat.NotDamaged) == null && chr.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
                                    MobSkillFactory.getMobSkill(((Integer) skill.left).intValue(), ((Integer) skill.right).intValue()).applyEffect(chr, copy, true, copy.isFacingLeft());
                                }
                            }
                        }
                    } else if (monster.getCustomValue0(8900000) == 1L) {
                        MapleMonster copy = MapleLifeFactory.getMonster(monster.getId() + (monstercolor.equals("Red") ? 1 : -1));
                        List<Pair<MonsterStatus, MonsterStatusEffect>> stats = new ArrayList<>();
                        stats.add(new Pair<>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(187, 210000000, 70L)));
                        stats.add(new Pair<>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(187, 210000000, 70L)));
                        stats.add(new Pair<>(MonsterStatus.MS_Pad, new MonsterStatusEffect(187, 210000000, 17000L)));
                        stats.add(new Pair<>(MonsterStatus.MS_Mad, new MonsterStatusEffect(187, 210000000, 17000L)));
                        if (copy != null) {
                            copy.setCustomInfo(8900000, 1, 0);
                            copy.lastCapTime = System.currentTimeMillis();
                            copy.setHp(monster.getHp());
                            int size = 0;
                            int mobid = 0;
                            for (MapleMonster m : monster.getMap().getAllMonster()) {
                                if (m.getId() == 8900001 || m.getId() == 8900002) {
                                    size++;
                                    mobid = m.getId();
                                }
                            }
                            if (size <= 1) {
                                MapleMonster other = MapleLifeFactory.getMonster((mobid == 8900001) ? 8900001 : 8900002);
                                other.lastCapTime = System.currentTimeMillis();
                                other.setCustomInfo(8900000, 1, 0);
                                if (copy.getId() == other.getId()) {
                                    other = MapleLifeFactory.getMonster((copy.getId() == 8900001) ? 8900002 : 8900001);
                                }
                                Point mobpos2 = new Point((monster.getTruePosition()).x - 150, (monster.getTruePosition()).y);
                                if (monster.getMap().getFootholds().findBelow(mobpos2) == null) {
                                    mobpos2 = monster.getPosition();
                                }
                                other.setHp(monster.getStats().getHp() * 10L / 100L);
                                monster.getMap().spawnMonsterWithEffect(other, 254, mobpos2);
                                if (other != null) {
                                    other.applyMonsterBuff(other.getMap(), stats, MobSkillFactory.getMobSkill(187, 1));
                                }
                            }
                            if (monster.getEventInstance() != null) {
                                monster.getEventInstance().registerMonster(copy);
                            }
                            monster.getMap().spawnMonsterWithEffect(copy, 254, monster.getPosition());
                            copy.applyMonsterBuff(copy.getMap(), stats, MobSkillFactory.getMobSkill(187, 1));
                            monster.getMap().killMonster(monster, monster.getController(), false, false, (byte) 0);
                            monster.getMap().broadcastMessage(MobPacket.showBossHP(monster.getId(), -1L, 0L));
                            for (MapleCharacter chr : copy.getMap().getAllCharactersThreadsafe()) {
                                Pair<Integer, Integer> skill = trans.getSkills().get(Randomizer.nextInt(trans.getSkills().size()));
                                if (chr.isAlive()) {
                                    chr.cancelDisease(SecondaryStat.CapDebuff);
                                    if (chr.getBuffedValue(SecondaryStat.NotDamaged) == null && chr.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
                                        MobSkillFactory.getMobSkill(((Integer) skill.left).intValue(), ((Integer) skill.right).intValue()).applyEffect(chr, copy, true, copy.isFacingLeft());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
                1000L));
    }

    public static void blackMageHandler(MapleMonster monster) {
        monster.setSchedule(server.Timer.MobTimer.getInstance().register(() -> {
            if ((monster.getEventInstance() == null || !monster.isAlive()) && monster.getMap().getCharacters().size() <= 0) {
                monster.getSchedule().cancel(true);
                monster.setSchedule((ScheduledFuture<?>) null);
                monster.getMap().killMonsterType(monster, 0);
                return;
            }
            MapleMap map = monster.getMap();
            for (MapleCharacter chr : map.getAllChracater()) {
                if (chr.hasDisease(SecondaryStat.CurseOfCreation) && chr.isAlive()) {
                    chr.addHP(chr.getStat().getCurrentMaxHp() / 100L * 4L);
                    chr.addMP(chr.getStat().getCurrentMaxMp(chr) / 100L * 4L);
                }
                if ((chr.getMapId() == 450013100 && chr.getMap().getMobsSize(8880500) > 0 && chr.getMap().getMobsSize(8880501) > 0) || (chr.getMapId() == 450013300 && chr.getMap().getMobsSize(8880502) > 0) || (chr.getMapId() == 450013500 && chr.getMap().getMobsSize(8880503) > 0) || (chr.getMapId() == 450013700 && chr.getMap().getMobsSize(8880504) > 0)) {
                    if (chr.getMapId() != 450013700 && chr.getMapId() != 450013500 && chr.getSkillCustomValue(45001310) == null && map.getCustomValue0(45001316) == 0) {
                        chr.setSkillCustomInfo(45001310, 0L, Randomizer.rand(7000, 11000));
                        for (MapleMonster mob : chr.getMap().getAllMonster()) {
                            if (mob.getId() == 8880502) {
                                map.broadcastMessage(MobPacket.setAttackZakumArm(mob.getObjectId(), 0));
                                break;
                            }
                        }
                        chr.getClient().send(CField.getFieldSkillAdd(100007, Randomizer.rand(1, 3), false));
                    }
                    if (chr.getSkillCustomValue(45001311) == null && chr.hasDisease(SecondaryStat.CurseOfCreation)) {
                        chr.setSkillCustomInfo(45001311, 0L, Randomizer.rand(1000, 3000));
                        if (chr.isAlive()) {
                            chr.getClient().send(CField.getFieldSkillAdd(100015, (chr.getMapId() == 450013700) ? 2 : 1, false));
                        }
                    }
                }
            }
            if (map.getCustomValue0(45001317) > 0) {
                int type = map.getCustomValue0(45001317);
                int bx = 0;
                int afterx = 0;
                switch (type) {
                    case 1:
                        bx = -105;
                        afterx = 158;
                        break;
                    case 2:
                        bx = -980;
                        afterx = -781;
                        break;
                    case 3:
                        bx = 789;
                        afterx = 950;
                        break;
                }
                for (MapleCharacter chr2 : map.getAllCharactersThreadsafe()) {
                    if (((chr2.getTruePosition()).x <= bx || afterx <= (chr2.getTruePosition()).x || (chr2.getPosition()).y <= -168) && chr2.getBuffedEffect(SecondaryStat.NotDamaged) == null && chr2.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
                        chr2.getPercentDamage(monster, 999, 999, 12500, true);
                    }
                }
            } else if (map.getCustomValue0(45001360) == 1) {
                for (MapleCharacter chr2 : map.getAllCharactersThreadsafe()) {
                    if ((chr2.getPosition()).y == 85 && chr2.getMapId() == 450013500 && chr2.isAlive() && chr2.getBuffedEffect(SecondaryStat.NotDamaged) == null && chr2.getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
                        chr2.addHP(-chr2.getStat().getCurrentMaxHp() * 10L);
                        chr2.getClient().send(CField.DamagePlayer2((int) chr2.getStat().getCurrentMaxHp() * 10));
                    }
                }
            }
            if ((map.getId() == 450013100 && map.getMobsSize(8880500) > 0 && map.getMobsSize(8880501) > 0) || (map.getId() == 450013300 && map.getMobsSize(8880502) > 0) || (map.getId() == 450013500 && map.getMobsSize(8880503) > 0 && map.getCharactersSize() > 0)) {
                if (map.getCustomValue0(45001316) == 0 && map.getCustomValue(45001310) == null) {
                    int type = Randomizer.rand(1, 2);
                    List<Obstacle> obs = new ArrayList<>();
                    map.setCustomInfo(45001310, 0, 60000);
                    for (int i = 0; i < (map.getRight() - map.getLeft()) / 150; i++) {
                        if (type == 1) {
                            Obstacle ob = new Obstacle(75, new Point(map.getRight() - i * 150, -540), new Point(map.getRight() - 300, 16), 50, 50, 0, i * 530, 82, 4, 573, 0);
                            obs.add(ob);
                        } else {
                            Obstacle ob = new Obstacle(75, new Point(map.getLeft() + i * 150, -540), new Point(map.getLeft() - 300, 16), 50, 50, 0, i * 530, 82, 4, 573, 0);
                            obs.add(ob);
                        }
                    }
                    map.CreateObstacle(monster, obs);
                } else if (map.getCustomValue(45001311) == null && map.getId() == 450013100 && map.getMobsSize(8880500) > 0 && map.getMobsSize(8880501) > 0) {
                    map.setCustomInfo(45001311, 0, 63000);
                    map.broadcastMessage(CField.enforceMSG("불길한 붉은 번개가 내리쳐 움직임을 제한한다.", 265, 3000));
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880506), new Point(-1600, 85));
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880506), new Point(400, 85));
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880506), new Point(-1000, 85));
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880506), new Point(1000, 85));
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880506), new Point(-400, 85));
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880506), new Point(1600, 85));
                    Timer.MapTimer.getInstance().schedule(() -> {
                        for (MapleMonster mob : map.getAllMonster()) {
                            if (mob.getId() != 8880506) continue;
                            map.killMonster(mob, -1);
                        }
                    }, 20000L);//
                } else if (map.getCustomValue(45001315) == null && map.getId() == 450013300 && map.getMobsSize(8880502) > 0) {
                    boolean use = true;
                    int randt = 2;
                    for (MapleMonster mob : map.getAllMonster()) {
                        if (mob.getId() == 8880502) {
                            if (mob.isSkillForbid()) {
                                use = false;
                                break;
                            }
                            map.broadcastMessage(MobPacket.UseSkill(mob.getObjectId(), (randt == 1) ? 11 : ((randt == 2) ? 10 : 12)));
                            break;
                        }
                    }
                    if (use) {
                        map.setCustomInfo(45001315, 0, 75000);
                        map.setCustomInfo(45001316, randt, 0);
                        map.broadcastMessage(CField.enforceMSG("검은 마법사의 붉은 번개가 모든 곳을 뒤덮는다. 피할 곳을 찾아야 한다.", 265, 4000));
                        Timer.MapTimer.getInstance().schedule(() -> {
                            map.broadcastMessage(MobPacket.ShowBlackMageSkill(randt - 1));
                            map.getMonsterById(8880502).setSkillForbid(true);
                        }, 5000L);
                        Timer.MapTimer.getInstance().schedule(() -> map.setCustomInfo(45001317, randt, 0), 6000L);
                        Timer.MapTimer.getInstance().schedule(() -> {
                            map.setCustomInfo(45001316, 0, 0);
                            map.setCustomInfo(45001317, 0, 0);
                        }, 15000L);
                        Timer.MapTimer.getInstance().schedule(() -> {
                            map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880516), new Point(-7, 88));
                            map.getMonsterById(8880502).setSkillForbid(false);
                            map.broadcastMessage(MobPacket.mobBarrierEffect(map.getMonsterById(8880502).getObjectId(), "UI/UIWindow8.img/BlackMageShield/mobEffect", "Sound/Etc.img/BlackMageShield", "UI/UIWindow8.img/BlackMageShield/mobEffect0"));
                            map.getMonsterById(8880502).gainShield(3000000000000L, true, 30);
                        }, 16000L);
                    }
                    //mush
                } else if (map.getCustomValue(45001312) == null && map.getId() == 450013100 && map.getMobsSize(8880500) > 0 && map.getMobsSize(8880501) > 0) {
                    List<Point> pt = new ArrayList<>();
                    int mobsize = map.getMobsSize(8880507) + map.getMobsSize(8880508);
                    int basex = 2172;
                    map.setCustomInfo(45001312, 0, 50000);
                    map.broadcastMessage(CField.enforceMSG("통곡의 장벽이 솟아올라 공간을 잠식한다.", 265, 3000));
                    if (mobsize < 16) {
                        pt.add(new Point(-basex + map.getCustomValue0(45001313) * 174, 84));
                        pt.add(new Point(basex - map.getCustomValue0(45001313) * 174, 84));
                        map.broadcastMessage(CField.getFieldSkillEffectAdd(100008, 1, pt));
                    }
                    for (MapleMonster mob : map.getAllMonster()) {
                        if (mob.getId() == 8880501 || mob.getId() == 8880500) {
                            int x = 350;
                            if (mob.getId() == 8880500) {
                                x *= -1;
                            }
                            mob.getMap().broadcastMessage(MobPacket.setAttackZakumArm(mob.getObjectId(), 0));
                            mob.setNextSkill(170);
                            mob.setNextSkillLvl((mob.getId() == 8880500) ? 62 : 64);
                            mob.getMap().broadcastMessage(MobPacket.moveMonsterResponse(mob.getObjectId(), (short) (int) mob.getCustomValue0(99991), mob.getMp(), true, 170, (mob.getId() == 8880500) ? 62 : 64, 0));
                            mob.getMap().broadcastMessage(MobPacket.TeleportMonster(mob, false, 3, new Point(x, 85)));
                        }
                    }
                    Timer.MapTimer.getInstance().schedule(() -> {
                        if (mobsize < 16) {
                            map.broadcastMessage(CField.getFieldSkillAdd(100008, 1, true));
                            map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880507), new Point(-basex + map.getCustomValue0(45001313) * 174, 85));
                            map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880508), new Point(basex - map.getCustomValue0(45001313) * 174, 85));
                        }
                        map.setCustomInfo(45001313, map.getCustomValue0(45001313) + 1, 0);
                        for (MapleMonster mob : map.getAllMonster()) {
                            if (mob.getId() != 8880500 && mob.getId() != 8880501 && mob.getId() != 8880505) continue;
                            if (mob.getId() == 8880500 || mob.getId() == 8880501) {
                                map.broadcastMessage(MobPacket.mobBarrierEffect(mob.getObjectId(), "UI/UIWindow8.img/BlackMageShield/mobEffect", "Sound/Etc.img/BlackMageShield", "UI/UIWindow8.img/BlackMageShield/mobEffect0"));
                            }
                            mob.gainShield(1000000000000L, true, 15);
                        }
                    }, 3000L); //mush
                } else if (map.getCustomValue0(45001316) == 0 && map.getCustomValue(45001313) == null && map.getId() == 450013300 && map.getMobsSize(8880502) > 0) {
                    map.setCustomInfo(45001313, 0, Randomizer.rand(38000, 43000));
                    map.broadcastMessage(CField.enforceMSG("파멸의 눈이 적을 쫒는다.", 265, 4000));
                    for (MapleMonster mob : map.getAllMonster()) {
                        if (mob.getId() == 8880502) {
                            map.broadcastMessage(MobPacket.UseSkill(mob.getObjectId(), 5));
                            break;
                        }
                    }
                    map.broadcastMessage(CField.getFieldSkillAdd(100012, Randomizer.rand(1, 2), false));
                } else if (map.getCustomValue0(45001316) == 0 && map.getCustomValue(45101313) == null && map.getId() == 450013300 && map.getMobsSize(8880502) > 0) {
                    for (MapleMonster mob : map.getAllMonster()) {
                        if (mob.getId() == 8880516) {
                            map.broadcastMessage(MobPacket.enableOnlyFsmAttack(mob, 1, 0));
                            map.setCustomInfo(45101313, 0, Randomizer.rand(10000, 13000));
                            break;
                        }
                    }
                } else if (map.getCustomValue0(45001316) == 0 && map.getCustomValue(45001314) == null && ((map.getId() == 450013300 && map.getMobsSize(8880502) > 0) || (map.getId() == 450013500 && map.getMobsSize(8880503) > 0))) {
                    map.setCustomInfo(45001314, 0, Randomizer.rand(45000, 51000));
                    map.getObtacles(Randomizer.rand(1, 2));
                } else if (map.getCustomValue0(45001316) == 0 && map.getCustomValue(45001320) == null && map.getId() == 450013500 && map.getMobsSize(8880503) > 0) {
                    List<Triple<Point, Integer, Integer>> skillinfo = new ArrayList<>();
                    for (int i = 0; i < Randomizer.rand(3, 5); i++) {
                        skillinfo.add(new Triple<>(new Point(Randomizer.rand(-970, 970), Randomizer.rand(-400, 60)), Integer.valueOf(Randomizer.rand(0, 180)), Integer.valueOf(100 + Randomizer.rand(50, 100) * i)));
                    }
                    boolean spattack = false;
                    for (MapleMonster mob : map.getAllMonster()) {
                        if (mob.getId() == 8880503) {
                            map.broadcastMessage(MobPacket.enableOnlyFsmAttack(mob, 1, 0));
                            map.setCustomInfo(45101313, 0, Randomizer.rand(10000, 13000));
                            if (Randomizer.isSuccess(30)) {
                                spattack = true;
                                map.broadcastMessage(MobPacket.UseSkill(mob.getObjectId(), 13));
                            }
                            break;
                        }
                    }
                    map.setCustomInfo(45001320, 0, Randomizer.rand(4000, 6000));
                    map.broadcastMessage(CField.getFieldLaserAdd(100011, spattack ? 1 : 2, skillinfo));
                } else if (map.getCustomValue0(45001316) == 0 && map.getCustomValue(45001321) == null && map.getId() == 450013500 && map.getMobsSize(8880503) > 0) {
                    Point pos = null;
                    int type;
                    for (type = Randomizer.rand(1, 6); map.getCustomValue0(45001350 + type) == 1; type = Randomizer.rand(1, 6));
                    switch (type) {
                        case 1:
                            pos = new Point(-792, -153);
                            break;
                        case 2:
                            pos = new Point(-568, -298);
                            break;
                        case 3:
                            pos = new Point(-289, -211);
                            break;
                        case 4:
                            pos = new Point(143, -90);
                            break;
                        case 5:
                            pos = new Point(485, -185);
                            break;
                        case 6:
                            pos = new Point(791, -309);
                            break;
                    }
                    List<Triple<Point, String, Integer>> skillinfo = new ArrayList<>();
                    map.setCustomInfo(45001321, 0, Randomizer.rand(10000, 35000));
                    skillinfo.add(new Triple<>(pos, (type == 1) ? "foot1" : ("foo" + type), Integer.valueOf(1)));
                    map.broadcastMessage(CField.getFieldFootHoldAdd(100013, 1, skillinfo, false));
                    map.setCustomInfo(45001350 + type, 1, 0);
                } else if (map.getCustomValue0(45001316) == 0 && (map.getCustomValue(45001322) == null || map.getCustomValue(45001323) == null) && map.getId() == 450013500 && map.getMobsSize(8880503) > 0) {
                    List<Obstacle> obs = new ArrayList<>();
                    if (map.getCustomValue(45001322) == null) {
                        Point pos = new Point(Randomizer.rand(map.getLeft(), map.getRight()), 110);
                        map.setCustomInfo(45001322, 0, Randomizer.rand(50000, 65000));
                        Obstacle ob = new Obstacle(76, pos, new Point(pos.x, 16), 50, 10, 0, 600, 82, 1, 1100, 700);
                        obs.add(ob);
                    } else if (map.getCustomValue(45001323) == null) {
                        Point pos = new Point(Randomizer.rand(map.getLeft(), map.getRight()), -491);
                        map.setCustomInfo(45001323, 0, Randomizer.rand(30000, 45000));
                        Obstacle ob = new Obstacle(79, pos, new Point(pos.x, 16), 50, 10, 390, 82, 1, 573);
                        obs.add(ob);
                    }
                    if (!obs.isEmpty()) {
                        map.CreateObstacle(monster, obs);
                    }
                } else if (map.getCustomValue(45001324) == null && map.getId() == 450013500 && map.getMobsSize(8880503) > 0) {
                    List<Triple<Point, String, Integer>> skillinfo = new ArrayList<>();
                    int type3 = 1;
                    map.broadcastMessage(CField.enforceMSG("검은 마법사가 창조와 파괴의 권능을 사용한다. 위와 아래, 어느 쪽으로 피할지 선택해야 한다.", 265, 4000));
                    map.setCustomInfo(45001324, 0, Randomizer.rand(60000, 75000));
                    map.setCustomInfo(45001316, 1, 0);
                    map.getMonsterById(8880503).setSkillForbid(true);
                    map.broadcastMessage(MobPacket.UseSkill(map.getMonsterById(8880503).getObjectId(), 11));
                    Timer.MapTimer.getInstance().schedule(() -> {
                        ArrayList<Obstacle> obs = new ArrayList<Obstacle>();
                        for (int i = 0; i < 45; ++i) {
                            int bx = 0;
                            int ax = 0;
                            int x = Randomizer.rand(-970, 970);
                            int y = -85;
                            int cy = 0;
                            int cya = 685;
                            for (int a = 1; a <= 6; ++a) {
                                switch (a) {
                                    case 1: {
                                        bx = -922;
                                        ax = -674;
                                        cy = 395;
                                        break;
                                    }
                                    case 2: {
                                        bx = -630;
                                        ax = -500;
                                        cy = 245;
                                        break;
                                    }
                                    case 3: {
                                        bx = -357;
                                        ax = -150;
                                        cy = 338;
                                        break;
                                    }
                                    case 4: {
                                        bx = 77;
                                        ax = 201;
                                        cy = 448;
                                        break;
                                    }
                                    case 5: {
                                        bx = 385;
                                        ax = 570;
                                        cy = 348;
                                        break;
                                    }
                                    case 6: {
                                        bx = 662;
                                        ax = 915;
                                        cy = 250;
                                    }
                                }
                                if (map.getCustomValue0(45001350 + a) != 1 || x < (bx -= 50) || x > (ax += 50)) continue;
                                cya = cy;
                            }
                            Obstacle ob = new Obstacle(78, new Point(x, -600), new Point(x, -85), 100, 999, 0, 450, 0, Randomizer.rand(400, 600), Randomizer.rand(1, 3), cya);
                            obs.add(ob);
                        }
                        map.broadcastMessage(MobPacket.CreateObstacle3(obs));
                    }, 1500L);//mush
                    for (int type = 1; type <= 6; type++) {
                        if (map.getCustomValue0(45001350 + type) == 1) {
                            Point pos = null;
                            switch (type) {
                                case 1:
                                    pos = new Point(-792, -153);
                                    break;
                                case 2:
                                    pos = new Point(-568, -298);
                                    break;
                                case 3:
                                    pos = new Point(-289, -211);
                                    break;
                                case 4:
                                    pos = new Point(143, -90);
                                    break;
                                case 5:
                                    pos = new Point(485, -185);
                                    break;
                                case 6:
                                    pos = new Point(791, -309);
                                    break;
                            }
                            skillinfo.add(new Triple<>(pos, (type == 1) ? "foot1" : ("foo" + type), Integer.valueOf(0)));
                        }
                    }
                                        Timer.MapTimer.getInstance().schedule(() -> {
                        map.removeCustomInfo(45001351);
                        map.removeCustomInfo(45001352);
                        map.removeCustomInfo(45001353);
                        map.removeCustomInfo(45001354);
                        map.removeCustomInfo(45001355);
                        map.removeCustomInfo(45001356);
                        map.removeCustomInfo(45001316);
                        map.removeCustomInfo(45001360);
                        map.getMonsterById(8880503).setSkillForbid(false);
                        map.broadcastMessage(CField.getFieldFootHoldAdd(100013, 1, skillinfo, false));
                        map.broadcastMessage(MobPacket.mobBarrierEffect(map.getMonsterById(0x878177).getObjectId(), "UI/UIWindow8.img/BlackMageShield/mobEffect_210", "Sound/Etc.img/BlackMageShield", "UI/UIWindow8.img/BlackMageShield/mobEffect0_210"));
                        map.getMonsterById(0x878177).gainShield(3000000000000L, true, 0);
                        ArrayList<Pair<Long, Integer>> damage = new ArrayList<Pair<Long, Integer>>();
                        map.setCustomInfo(45001326, 0, 15000);
                        map.setCustomInfo(45001327, map.getMonsterById((int)0x878177).getPosition().x + 300, 0);
                        map.setCustomInfo(45001328, -150, 0);
                        Point pos = new Point(map.getCustomValue0(45001327), map.getCustomValue(45001328));
                        int dealy = 540;
                        MapleCharacter chr = null;
                        Iterator<MapleCharacter> chrs = map.getCharactersThreadsafe().iterator();
                        if (chrs.hasNext()) {
                            chr = chrs.next();
                        }
                        for (int i = 0; i < 14; ++i) {
                            damage.add(new Pair<Long, Integer>(9999999999L, dealy + i * 180));
                            if (chr == null) continue;
                            map.getMonsterById(0x878177).damage(chr, 9999999999L, false);
                        }
                        map.broadcastMessage(MobPacket.FieldSummonTeleport(pos, pos.x < 0));
                        Timer.MapTimer.getInstance().schedule(() -> {
                            map.broadcastMessage(MobPacket.FieldSummonAttack(3, true, pos, map.getMonsterById(0x878177).getObjectId(), damage));
                            map.setCustomInfo(45001327, map.getMonsterById((int)0x878177).getPosition().x + Randomizer.rand(200, 500), 0);
                            map.setCustomInfo(45001328, -304, 0);
                            map.broadcastMessage(MobPacket.FieldSummonTeleport(new Point(map.getCustomValue0(45001327), -304), true));
                        }, 2000L);
                    }, 5000L);
                }//mush
                if (map.getCustomValue(45021328) == null && map.getId() == 450013500 && map.getMobsSize(8880503) > 0 && map.getCustomValue0(45011328) < 3) {
                    int mobid = 0;
                    map.broadcastMessage(CField.enforceMSG("파괴의 천사가 무에서 창조된다.", 265, 4000));
                    map.setCustomInfo(45011328, map.getCustomValue0(45011328) + 1, 0);
                    if (map.getCustomValue0(45011328) < 3) {
                        map.setCustomInfo(45021328, 0, 120000);
                    }
                    Point pos = null;
                    switch (map.getCustomValue0(45011328)) {
                        case 1:
                            mobid = 8880509;
                            pos = new Point(746, 85);
                            break;
                        case 2:
                            mobid = 8880510;
                            pos = new Point(443, 85);
                            break;
                        case 3:
                            mobid = 8880511;
                            pos = new Point(209, 85);
                            break;
                    }
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(pos.x, pos.y));
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(-pos.x, pos.y));
                }
                if (map.getCustomValue(45001326) == null && map.getId() == 450013500 && map.getMobsSize(8880503) > 0) {
                    List<Pair<Long, Integer>> damage = new ArrayList<>();
                    map.setCustomInfo(45001326, 0, 5000);
                    damage.add(new Pair<>(Long.valueOf(3000000000L), Integer.valueOf(0)));
                    MapleCharacter chr = null;
                    Iterator<MapleCharacter> chrs = map.getCharactersThreadsafe().iterator();
                    if (chrs.hasNext()) {
                        chr = chrs.next();
                    }
                    if (chr != null) {
                        map.getMonsterById(8880503).damage(chr, 3000000000L, false);
                    }
                    map.broadcastMessage(MobPacket.FieldSummonAttack(3, false, new Point(0, 0), map.getMonsterById(8880503).getObjectId(), damage));
                }
            }
            if (map.getId() == 450013700 && map.getMobsSize(8880504) > 0) {
                if (map.getCustomValue(45004445) == null) {
                    List<Triple<Point, Point, Integer>> skillinfo = new ArrayList<>();
                    map.broadcastMessage(CField.enforceMSG("신에 가까운 자의 권능이 발현된다. 창조와 파괴, 어떤 힘을 품을지 선택해야 한다.", 265, 5000));
                    map.setCustomInfo(45004445, 0, 30000);
                    map.setCustomInfo(45004446, 1, 0);
                    skillinfo.add(new Triple<>(new Point(-801, -404), new Point(-681, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(-681, -404), new Point(-561, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(-561, -404), new Point(-441, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(-441, -404), new Point(-321, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(-321, -404), new Point(-201, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(-201, -404), new Point(-81, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(-81, -404), new Point(39, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(39, -404), new Point(159, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(159, -404), new Point(279, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(279, -404), new Point(399, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(399, -404), new Point(519, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(519, -404), new Point(639, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(639, -404), new Point(759, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    skillinfo.add(new Triple<>(new Point(759, -404), new Point(879, 238), Integer.valueOf(Randomizer.rand(0, 3))));
                    map.broadcastMessage(CField.getFieldFinalLaserAdd(100014, 2, skillinfo, 0));
                        Timer.MapTimer.getInstance().schedule(() -> {
                        map.setCustomInfo(45004446, 0, 0);
                        map.getMonsterById(0x878187).gainShield(3000000000000L, true, 0);
                        map.broadcastMessage(MobPacket.mobBarrierEffect(map.getMonsterById(0x878178).getObjectId(), "UI/UIWindow8.img/BlackMageShield/mobEffect_211", "Sound/Etc.img/BlackMageShield", "UI/UIWindow8.img/BlackMageShield/mobEffect0_211"));
                    }, 5000L); //mush
                } else if (map.getCustomValue(45004444) == null && map.getCustomValue0(45004446) == 0) {
                    map.setCustomInfo(45004444, 0, Randomizer.rand(7000, 12000));
                    for (int i = 0; i < Randomizer.rand(3, 4); i++) {
                        List<Triple<Point, Point, Integer>> skillinfo = new ArrayList<>();
                        int ranx = Randomizer.rand(map.getLeft(), map.getRight());
                        int rany = Randomizer.rand(-251, 159);
                        skillinfo.add(new Triple<>(new Point(ranx - 150, rany - 150), new Point(ranx + 150, rany + 150), Integer.valueOf(Randomizer.rand(0, 1))));
                        map.broadcastMessage(CField.getFieldFinalLaserAdd(100016, 1, skillinfo, i * 240));
                    }
                }
            }
        },
                1000L));
    }

    public static void SerenHandler(MapleMonster monster) {
        switch (monster.getId()) {
            case 8880600:
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    List<Obstacle> obs = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        int x = Randomizer.rand(-1030, 1030);
                        Obstacle ob = new Obstacle(84, new Point(x, -440), new Point(x, 275), 30, 15, i * 1000, Randomizer.rand(16, 32), 3, 715, 0);
                        obs.add(ob);
                    }
                    monster.getMap().CreateObstacle(monster, obs);
                    for (MapleCharacter chr : monster.getMap().getAllChracater()) {
                        if (chr.isAlive()) {
                            chr.addSerenGauge(-10);
                        }
                    }
                },
                        5000L));
                break;
            case 8880602:
                monster.ResetSerenTime(true);
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    if (monster.getMap().getAllChracater().size() > 0 && monster.getCustomValue0(8880603) == 0L) {
                        monster.addSkillCustomInfo(8880602, 1L);
                        if (monster.getCustomValue0(8880602) >= ((monster.getSerenTimetype() == 3) ? 1L : 5L)) {
                            monster.removeCustomInfo(8880602);
                            if (monster.getSerenTimetype() == 4) {
                                monster.gainShield(monster.getStats().getHp() / 100L, !(monster.getShield() > 0L), 0);
                            }
                            for (MapleCharacter chr : monster.getMap().getAllChracater()) {
                                if (chr.isAlive()) {
                                    chr.addSerenGauge((monster.getSerenTimetype() == 3) ? -20 : 20);
                                }
                            }
                        }
                        monster.AddSerenTimeHandler(monster.getSerenTimetype(), -1);
                    }
                },
                        1000L));
                break;
            case 8880603:{
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    if (monster == null || monster.getMap().getMonsterById(8880603) == null) {
                        monster.getSchedule().cancel(true);
                        monster.setSchedule(null);
                    }
                    if (monster != null) {
                        for (int i = 0; i < 2; ++i) {
                            int time = i == 0 ? 10 : 1000;
                            Timer.MobTimer.getInstance().schedule(() -> {
                                monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880606), new Point(470, 305));
                                monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880606), new Point(-10, 305));
                                monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880606), new Point(-450, 305));
                            }, time);
                        }
                    }
                }, Randomizer.rand(7000, 11000)));
                break;
            }
            case 8880607: {
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    MapleMap mapleMap = monster.getMap();
                    FieldSkillFactory.getInstance();
                    mapleMap.broadcastMessage(MobPacket.useFieldSkill(FieldSkillFactory.getFieldSkill(100023, 1)));
                }, Randomizer.rand(5000, 10000)));
                break;
            }
            case 8880605:
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    for (MapleCharacter chr : monster.getMap().getAllChracater()) {
                        if (chr.isAlive() && (chr.getPosition()).x - 300 <= (monster.getPosition()).x && (chr.getPosition()).x + 300 >= (monster.getPosition()).x) {
                            monster.addSkillCustomInfo(8880605, 1L);
                            if (monster.getCustomValue0(8880605) >= 10L) {
                                monster.switchController(chr.getClient().getRandomCharacter(), true);
                                monster.removeCustomInfo(8880605);
                            }
                            monster.getMap().broadcastMessage(MobPacket.enableOnlyFsmAttack(monster, 1, 0));
                            break;
                        }
                    }
                },
                        2000L));
                break;
            case 8880610:
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    if (monster != null) {
                        int type = Randomizer.rand(0, 2);
                        if (type == 0) {
                            monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880611), new Point(-320, 305));
                            monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880611), new Point(470, 305));
                        } else {
                            monster.getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880611), (type == 1) ? new Point(-320, 305) : new Point(470, 305));
                        }
                    }
                }, Randomizer.rand(30000, 50000)
                ));
                break;
            case 8880601:
            case 8880604:
            case 8880608:
            case 8880613:
                int time = (monster.getId() == 8880601) ? 7000 : Randomizer.rand(7000, 13000);
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> monster.getMap().broadcastMessage(MobPacket.enableOnlyFsmAttack(monster, 1, 0)), time));
                break;
        }
    }

    public static void LucidHandler(MapleMonster monster) {
        switch (monster.getId()) {
            case 8880158: 
            case 8880166: {
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    if (!(monster.getEventInstance() != null && monster.isAlive() || monster.getMap().getCharacters().size() > 0)) {
                        monster.getSchedule().cancel(true);
                        monster.setSchedule(null);
                        monster.getMap().killMonsterType(monster, 0);
                        return;
                    }
                    int count = Randomizer.rand(1, 2);
                    for (int i = 0; i < count; ++i) {
                        Point pos = new Point(Randomizer.rand(216, 1786), 43);
                        int monsterid = monster.getMap().getId() == 450004450 ? 8880180 : (monster.getMap().getId() == 450003840 ? 8880186 : 8880160);
                        MapleMonster mob = MapleLifeFactory.getMonster(monsterid);
                        mob.setDeadTimeKillmob(1000);
                        monster.getMap().spawnMonsterWithEffect(mob, 77, pos);
                    }
                }, monster.getMap().getId() == 450003840 ? 18000L : 15000L));
                break;
            }
            case 8880140: 
            case 8880141: 
            case 8880142: 
            case 8880150: 
            case 8880151: 
            case 8880153: 
            case 8880155: {
                monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
                    if (!(monster.getEventInstance() != null && monster.isAlive() || monster.getMap().getCharacters().size() > 0)) {
                        monster.getSchedule().cancel(true);
                        monster.setSchedule(null);
                        monster.getMap().killMonsterType(monster, 0);
                        return;
                    }
                    if (monster.getCustomValue0(23888) == 0L) {
                        if ((monster.getId() == 8880150 || monster.getId() == 8880151 || monster.getId() == 8880153 || monster.getId() == 8880155) && monster.getCustomValue(8880150) == null) {
                            ArrayList<String> foot = new ArrayList<String>();
                            int rand = Randomizer.rand(0, 11);
                            Point pos = null;
                            while (monster.getMap().getCustomValue(4200000 + rand) != null) {
                                rand = Randomizer.rand(0, 11);
                            }
                            switch (rand) {
                                case 0: {
                                    foot.add("Bblue1");
                                    pos = new Point(Randomizer.rand(169, 501), -855);
                                    break;
                                }
                                case 1: {
                                    foot.add("except1");
                                    pos = new Point(Randomizer.rand(953, 1094), -842);
                                    break;
                                }
                                case 2: {
                                    foot.add("Bred1");
                                    pos = new Point(Randomizer.rand(936, 1276), -619);
                                    break;
                                }
                                case 3: {
                                    foot.add("Bblue2");
                                    pos = new Point(Randomizer.rand(540, 894), -490);
                                    break;
                                }
                                case 4: {
                                    foot.add("Bred2");
                                    pos = new Point(Randomizer.rand(-12, 331), -550);
                                    break;
                                }
                                case 5: {
                                    foot.add("Mred2");
                                    pos = new Point(Randomizer.rand(273, 498), -378);
                                    break;
                                }
                                case 6: {
                                    foot.add("Mred3");
                                    pos = new Point(Randomizer.rand(856, 1088), -331);
                                    break;
                                }
                                case 7: {
                                    foot.add("Bred3");
                                    pos = new Point(Randomizer.rand(642, 957), -194);
                                    break;
                                }
                                case 8: {
                                    foot.add("Myellow3");
                                    pos = new Point(Randomizer.rand(1028, 1268), -143);
                                    break;
                                }
                                case 9: {
                                    foot.add("Myellow2");
                                    pos = new Point(Randomizer.rand(22, 236), -267);
                                    break;
                                }
                                case 10: {
                                    foot.add("Bblue3");
                                    pos = new Point(Randomizer.rand(152, 472), -125);
                                    break;
                                }
                                case 11: {
                                    foot.add("Myellow1");
                                    pos = new Point(Randomizer.rand(414, 641), -685);
                                }
                            }
                            MapleMonster monste3r = MapleLifeFactory.getMonster(monster.getId() == 8880155 ? 8880194 : (monster.getId() == 8880150 ? 8880170 : 8880182));
                            monster.getMap().spawnMonsterOnGroundBelow(monste3r, pos);
                            monster.getMap().killMonsterType(monste3r, 2);
                            monster.getMap().broadcastMessage(MobPacket.BossLucid.setStainedGlassOnOff(false, foot));
                            MapleMonster monste3r4 = MapleLifeFactory.getMonster(monster.getId() == 8880155 ? 8880195 : (monster.getId() == 8880150 ? 8880171 : 8880183));
                            Point pos2 = pos;
                            Timer.MapTimer.getInstance().schedule(() -> {
                                monster.getMap().broadcastMessage(MobPacket.BossLucid.setStainedGlassOnOff(true, foot));
                                monster.getMap().spawnMonsterOnGroundBelow(monste3r4, pos2);
                            }, 2000L);
                            monster.setCustomInfo(8880150, 0, 15000);
                            monster.getMap().setCustomInfo(4200000 + rand, 0, 120000);
                        }
                        if (monster != null && monster.getCustomValue(8880141) == null) {
                            Butterfly bf = new Butterfly(Randomizer.rand(0, 8), Butterfly.getPosition(monster.getId() != 8880150 && monster.getId() != 8880151 && monster.getId() != 8880155, Randomizer.rand(0, 49)));
                            ArrayList<Butterfly> bfl = new ArrayList<Butterfly>();
                            ArrayList<Integer> chra = new ArrayList<Integer>();
                            bfl.add(bf);
                            monster.getMap().broadcastMessage(MobPacket.BossLucid.createButterfly(0, bfl));
                            monster.addSkillCustomInfo(8880140, 1L);
                            if (monster.getCustomValue0(8880140) == 20L) {
                                monster.getMap().broadcastMessage(CField.enforceMSG("꿈이 강해지고 있습니다. 조심하세요!", 222, 3000));
                            } else if (monster.getCustomValue0(8880140) >= 40L) {
                                if (monster.getId() == 8880150 || monster.getId() == 8880151 || monster.getId() == 8880155) {
                                    monster.setNextSkill(238);
                                    monster.setNextSkillLvl(9);
                                } else {
                                    for (int i = 0; i < 40; ++i) {
                                        chra.add(monster.getController().getClient().getRandomCharacter().getId());
                                    }
                                    monster.removeCustomInfo(8880140);
                                    monster.getMap().broadcastMessage(MobPacket.BossLucid.AttackButterfly(chra));
                                }
                            }
                            if (monster.getId() == 8880140 || monster.getId() == 8880141 || monster.getId() == 8880150 || monster.getId() == 8880151 || monster.getId() == 8880142 || monster.getId() == 8880155) {
                                if (monster.getPhase() == 0 && monster.getHPPercent() <= 75) {
                                    monster.setPhase((byte)1);
                                    monster.getMap().broadcastMessage(CField.enforceMSG("루시드가 힘을 이끌어내고 있습니다!", 222, 5000));
                                } else if (monster.getPhase() == 1 && monster.getHPPercent() <= 50) {
                                    monster.setPhase((byte)2);
                                    monster.getMap().broadcastMessage(CField.enforceMSG("루시드가 더 강한 힘을 발휘할 겁니다!", 222, 5000));
                                } else if (monster.getPhase() == 2 && monster.getHPPercent() <= 25) {
                                    monster.setPhase((byte)3);
                                    monster.getMap().broadcastMessage(CField.enforceMSG("루시드가 분노한 것 같습니다!", 222, 5000));
                                }
                            }
                            monster.setCustomInfo(8880141, 0, monster.getPhase() == 0 ? 4000 : (monster.getPhase() == 1 ? 3000 : (monster.getPhase() == 2 ? 2000 : (monster.getPhase() == 3 ? 1000 : 3000))));
                        }
                        if (monster.getId() == 8880153 && monster.getCustomValue(8880153) == null) {
                            monster.getMap().broadcastMessage(CWvsContext.getTopMsg("루시드가 전범위 공격을 지속합니다."));
                            for (MapleCharacter achr : monster.getMap().getAllCharactersThreadsafe()) {
                                if (achr == null || achr.getBuffedValue(SecondaryStat.NotDamaged) != null || achr.getBuffedValue(SecondaryStat.IndieNotDamaged) != null) continue;
                                achr.addHP(-(achr.getStat().getCurrentMaxHp() / 100L * 30L));
                            }
                            monster.setCustomInfo(8880153, 0, Randomizer.rand(4000, 7000));
                        }
                    }
                }, 1000L));
            }
        }
    }

    public static void AggressIve(MapleMonster monster) {
        if (monster.getCustomValue(987654) == null) {
            List<Pair<String, Long>> a2 = new ArrayList<>();
            int i2 = 0;
            for (MaplePartyCharacter c : monster.getController().getParty().getMembers()) {
                if (c.getPlayer() != null) {
                    a2.add(new Pair<>(c.getName(), Long.valueOf(c.getPlayer().getAggressiveDamage())));
                }
            }
            for (int i = 0; i < a2.size() - 1; i++) {
                for (int j = 0; j < a2.size() - i - 1; j++) {
                    if (((Long) ((Pair) a2.get(j)).getRight()).longValue() < ((Long) ((Pair) a2.get(j + 1)).getRight()).longValue()) {
                        String chridtmp = (String) ((Pair) a2.get(j + 1)).getLeft();
                        long chrpointtmp = ((Long) ((Pair) a2.get(j + 1)).getRight()).longValue();
                        a2.set(j + 1, a2.get(j));
                        a2.set(j, new Pair<>(chridtmp, Long.valueOf(chrpointtmp)));
                    }
                }
            }
            MapleCharacter chr = monster.getMap().getCharacterByName((String) ((Pair) a2.get(0)).getLeft());
            monster.getMap().broadcastMessage(CField.Aggressive(a2, monster.getMap()));
            String txt = (monster.getStats().getName().equals("데미안") || monster.getStats().getName().equals("반 레온") || monster.getStats().getName().equals("카웅")) ? "이" : "가";
            monster.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 1, 3, 0, monster.getStats().getName() + txt + " " + chr.getName() + "님을 가장 위협적인 적으로 간주하고 있습니다."));
            if (monster.getStats().getName().equals("반 레온")) {
                for (MobSkill msi : monster.getSkills()) {
                    if ((msi.getSkillId() == 170 && msi.getSkillLevel() == 1) || msi.getSkillLevel() == 105) {
                        msi.setOnlyFsm(false);
                    }
                }
            }
            monster.setCustomInfo(987654, 0, 20000);
        }
    }

    public static void dunkelHandler(MapleMonster dunkel, MapleMap map) {
        dunkel.setSchedule(Timer.MobTimer.getInstance().register(() -> {
            if ((dunkel.getEventInstance() == null || !dunkel.isAlive()) && dunkel.getMap().getCharacters().size() <= 0) {
                dunkel.getSchedule().cancel(true);
                dunkel.setSchedule((ScheduledFuture<?>) null);
                dunkel.getMap().killMonster(dunkel);
                return;
            }
            long time = System.currentTimeMillis();
            if (dunkel.isAlive()) {
                if (dunkel.getCustomValue(dunkel.getId()) == null) {
                    dunkel.setCustomInfo(dunkel.getId(), 0, 10000);
                    int radomcount = Randomizer.rand(2, 5);
                    for (int i = 0; i < radomcount; i++) {
                        MapleMonster fallenWarrior = MapleLifeFactory.getMonster(8645003);
                        if (dunkel.getId() == 8645009) {
                            fallenWarrior.setHp(10000000000L);
                            fallenWarrior.getStats().setHp(10000000000L);
                        } else if (dunkel.getId() == 8645066) {
                            fallenWarrior.setHp(30000000000L);
                            fallenWarrior.getStats().setHp(30000000000L);
                        }
                        dunkel.getMap().spawnMonsterOnGroundBelow(fallenWarrior, new Point(Randomizer.rand(-782, 774), 29));
                        List<Pair<MonsterStatus, MonsterStatusEffect>> stats = new ArrayList<>();
                        stats.add(new Pair<>(MonsterStatus.MS_PowerImmune, new MonsterStatusEffect(146, 12000, 1L)));
                        if (fallenWarrior != null) {
                            fallenWarrior.applyMonsterBuff(dunkel.getMap(), stats, new MobSkill(146, 18));
                        }
                    }
                }
                if (dunkel.getCustomValue(dunkel.getId() + 1) == null) {
                    int[] types = {72, 73, 74};
                    List<Obstacle> obs = new ArrayList<>();
                    int size = Randomizer.rand(3, 8);
                    for (int i = 0; i < size; i++) {
                        Obstacle ob;
                        int type = types[Randomizer.nextInt(types.length)];
                        int x = Randomizer.rand(-782, 774);
                        if (type == 72) {
                            ob = new Obstacle(type, new Point(x, -815), new Point(x, 29), 24, 10, 180, Randomizer.rand(300, 850), 1, 844, 0);
                        } else if (type == 73) {
                            ob = new Obstacle(type, new Point(x, -815), new Point(x, 29), 24, 10, 601, Randomizer.rand(300, 850), 1, 844, 0);
                        } else if (type == 74) {
                            ob = new Obstacle(type, new Point(x, -815), new Point(x, 29), 24, 10, 1377, Randomizer.rand(300, 850), 1, 844, 0);
                        } else {
                            ob = new Obstacle(type, new Point(x, -815), new Point(x, 29), 24, 10, 0, Randomizer.rand(300, 850), 1, 844, 0);
                        }
                        obs.add(ob);
                    }
                    dunkel.getMap().CreateObstacle(dunkel, obs);
                    dunkel.setCustomInfo(dunkel.getId() + 1, 0, 3000);
                }
                if (dunkel.getCustomValue(dunkel.getId() + 2) == null) {
                    List<DunkelEliteBoss> eliteBosses = new ArrayList<>();
                    int count = 1;
                    if (dunkel.getId() == 8645009) {
                        if (dunkel.getHPPercent() >= 66) {
                            count = 1;
                        } else if (dunkel.getHPPercent() < 66 && dunkel.getHPPercent() >= 33) {
                            count = 2;
                        } else {
                            count = 3;
                        }
                    } else if (dunkel.getId() == 8645066) {
                        if (dunkel.getHPPercent() >= 50) {
                            count = 2;
                        } else {
                            count = 3;
                        }
                    }
                    for (int i = 0; i < count; i++) {
                        eliteBosses.add(getEliteBossAttack(dunkel));
                        for (int j = 0; j < i; j++) {
                            if (((DunkelEliteBoss) eliteBosses.get(i)).getbosscode() == ((DunkelEliteBoss) eliteBosses.get(j)).getbosscode()) {
                                eliteBosses.set(i, getEliteBossAttack(dunkel));
                                j--;
                            }
                        }
                    }
                    dunkel.getMap().broadcastMessage(MobPacket.BossDunKel.eliteBossAttack(dunkel, eliteBosses, null, Randomizer.nextBoolean()));
                    dunkel.setCustomInfo(dunkel.getId() + 2, 0, 7000);
                }
            }
        },
                1000L));
    }

    public static DunkelEliteBoss getEliteBossAttack(MapleMonster dunkel) {
        DunkelEliteBoss eboss;
        int type = Randomizer.nextInt(10);
        List<MapleCharacter> chrlist = dunkel.getMap().getAllChracater();
        Collections.shuffle(chrlist);
        MapleCharacter chr = chrlist.get(0);
        Point cp = chr.getPosition();
        byte isLeft = (byte) (Randomizer.nextBoolean() ? 0 : 1);
        if (type == 0) {
            eboss = new DunkelEliteBoss((short) type, (short) 1, 2800, 1440, 1, 3, 0, 0, 0, (short) 0, (short) 0, (short) 0, (byte) 0, isLeft, new Point(-280, -220), new Point(10, 10), cp, (short) 0, (short) 0);
        } else if (type == 1) {
            eboss = new DunkelEliteBoss((short) type, (short) 2, 3000, 1620, 1, 4, 300, 0, 1200, (short) 65, (short) 100, (short) 0, (byte) 0, (byte) 1, new Point(-100, -75), new Point(0, 0), new Point(0, 0), (short) 0, (short) 2);
        } else if (type == 2) {
            eboss = new DunkelEliteBoss((short) type, (short) 2, 3000, 1800, 1, 5, 0, 1, 1600, (short) 35, (short) 600, (short) 0, (byte) 0, (byte) 0, new Point(-45, -20), new Point(0, 0), new Point(0, 0), (short) 0, (short) 2);
        } else if (type == 3) {
            eboss = new DunkelEliteBoss((short) type, (short) 1, 2900, 1500, 1, 6, 0, 0, 0, (short) 0, (short) 0, (short) 0, (byte) 0, isLeft, new Point(-620, -135), new Point(50, 5), cp, (short) 0, (short) 0);
        } else if (type == 4) {
            eboss = new DunkelEliteBoss((short) type, (short) 3, 3300, 1710, 5, 7, 0, 0, 0, (short) 0, (short) 0, (short) 0, (byte) 1, isLeft, new Point(-40, -80), new Point(40, 0), cp, (short) Randomizer.rand(5, 8), (short) 0);
        } else if (type == 5) {
            eboss = new DunkelEliteBoss((short) type, (short) 1, 4800, 3630, 1, 8, 0, 0, 0, (short) 0, (short) 0, (short) 1, (byte) 1, isLeft, new Point(-290, -420), new Point(270, 25), cp, (short) 0, (short) 0);
        } else if (type == 6) {
            eboss = new DunkelEliteBoss((short) type, (short) 3, 3000, 2160, 7, 11, 0, 0, 0, (short) 0, (short) 0, (short) 1, (byte) 1, isLeft, new Point(-50, -170), new Point(50, 5), new Point(0, 0), (short) Randomizer.rand(5, 9), (short) 0);
        } else if (type == 7) {
            eboss = new DunkelEliteBoss((short) type, (short) 1, 4800, 3630, 1, 8, 0, 0, 0, (short) 0, (short) 0, (short) 1, (byte) 1, isLeft, new Point(-290, -420), new Point(270, 25), cp, (short) 0, (short) 0);
        } else if (type == 8) {
            eboss = new DunkelEliteBoss((short) type, (short) 1, 2800, 840, 1, 12, 0, 0, 0, (short) 0, (short) 0, (short) 0, (byte) 0, isLeft, new Point(-360, -155), new Point(10, 10), cp, (short) 0, (short) 0);
        } else if (type == 9) {
            eboss = new DunkelEliteBoss((short) type, (short) 1, 2700, 840, 1, 10, 0, 0, 0, (short) 0, (short) 0, (short) 0, (byte) 0, isLeft, new Point(-350, -155), new Point(10, 10), cp, (short) 0, (short) 0);
        } else {
            eboss = null;
        }
        return eboss;
    }

    public static void JinHillaGlassTime(MapleMonster jinhilla, int time) {
        if (jinhilla == null || jinhilla.getMap().getAllChracater().size() <= 0) {
            return;
        }
        MobSkill msi = MobSkillFactory.getMobSkill(247, 1);
        jinhilla.setSchedule(Timer.MobTimer.getInstance().schedule(() -> {
            if (jinhilla != null && jinhilla.isAlive()) {
                MapleCharacter player = jinhilla.getController();
                if (player == null) {
                    Iterator<MapleCharacter> iterator = jinhilla.getMap().getAllChracater().iterator();
                    if (iterator.hasNext()) {
                        MapleCharacter chr = iterator.next();
                        player = chr;
                    }
                }
                if (player == null) {
                    return;
                }
                jinhilla.setCustomInfo(24701, 1, 0);
                jinhilla.setNextSkill(247);
                jinhilla.setNextSkillLvl(1);
                jinhilla.getMap().broadcastMessage(MobPacket.setAttackZakumArm(jinhilla.getObjectId(), 2));
                jinhilla.getMap().broadcastMessage(MobPacket.moveMonsterResponse(jinhilla.getObjectId(), (short) (int) jinhilla.getCustomValue0(99991), jinhilla.getMp(), true, 247, 1, 0));
                msi.applyEffect(player, jinhilla, true, true);
                jinhilla.setLastSkillUsed(msi, System.currentTimeMillis(), (time * 1000));
                JinHillaGlassTime(jinhilla, (jinhilla.getHPPercent() >= 60) ? 150 : ((jinhilla.getHPPercent() >= 30) ? 120 : 100));
            }
        }, (time * 1000)));
    }
}
