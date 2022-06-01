/*
 * Decompiled with CFR 0.150.
 */
package handling.channel.handler;

import client.MapleCharacter;
import client.PlayerStats;
import client.RandomSkillEntry;
import client.RangeAttack;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import handling.channel.handler.AttackInfo;
import handling.channel.handler.AttackType;
import handling.channel.handler.PlayerHandler;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.field.skill.MapleMagicWreck;
import server.field.skill.MapleSecondAtom;
import server.field.skill.SecondAtom;
import server.life.Element;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.ForceAtom;
import server.maps.MapleAtom;
import server.maps.MapleFoothold;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import server.maps.MapleSummon;
import server.maps.SummonMovementType;
import tools.AttackPair;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.Pair;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SkillPacket;

public class DamageParse {

    public static void applyAttack(final AttackInfo attack, Skill theSkill, final MapleCharacter player, double maxDamagePerMonster, SecondaryStatEffect effect, AttackType attack_type, boolean BuffAttack, boolean energy) {
        MapleSummon summon;
        SecondaryStatEffect combatRecovery;
        MapleMonster monster;
        boolean afterimageshockattack;
        int multikill;
        PlayerStats stats;
        long hpMob;
        long totDamageToOneMonster;
        long totDamage;
        block858:
        {
            block859:
            {
                SecondaryStatEffect bytalsteal;
                SecondaryStatEffect stst;
                if (attack.summonattack == 0) {
                    // empty if block
                }
                player.checkSpecialCoreSkills("prob", 0, effect);
                if (attack.skill != 0) {
                    player.checkSpecialCoreSkills("cooltime", 0, effect);
                    if (effect == null) {
                        player.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(player));
                        return;
                    }
                    if (GameConstants.isMulungSkill(attack.skill)) {
                        if (player.getMapId() / 10000 != 92502) {
                            return;
                        }
                        if (player.getMulungEnergy() < 10000) {
                            return;
                        }
                    } else if (GameConstants.isPyramidSkill(attack.skill) ? player.getMapId() / 1000000 != 926 : GameConstants.isInflationSkill(attack.skill) && player.getBuffedValue(SecondaryStat.Inflation) == null) {
                        return;
                    }
                }
                totDamage = 0L;
                MapleMap map = player.getMap();
                totDamageToOneMonster = 0L;
                hpMob = 0L;
                stats = player.getStat();
                multikill = 0;
                afterimageshockattack = false;
                monster = null;
                ArrayList<Triple<Integer, Integer, Integer>> finalMobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                for (Object oned : attack.allDamage) {
                    SecondaryStatEffect mark;
                    monster = map.getMonsterByOid(((AttackPair) oned).objectId);
                    if (monster == null || monster.getLinkCID() > 0) {
                        continue;
                    }
                    totDamageToOneMonster = 0L;
                    hpMob = monster.getMobMaxHp();
                    MapleMonsterStats monsterstats = monster.getStats();
                    long fixeddmg = monsterstats.getFixedDamage();
                    if (monster.getId() >= 9833070 && monster.getId() <= 9833074) {
                        continue;
                    }
                    for (Pair<Long, Boolean> eachde : ((AttackPair) oned).attack) {
                        long eachd = (Long) eachde.left;
                        if (fixeddmg != -1L) {
                            eachd = monsterstats.getOnlyNoramlAttack() ? (attack.skill != 0 ? 0L : fixeddmg) : fixeddmg;
                        }
                        totDamageToOneMonster += eachd;
                        player.checkSpecialCoreSkills("attackCount", monster.getObjectId(), effect);
                        player.checkSpecialCoreSkills("attackCountMob", monster.getObjectId(), effect);
                    }
                    totDamage += totDamageToOneMonster;
                    
                    if (!player.gethottimebossattackcheck()) {
                    player.sethottimebossattackcheck(true);
                }
                                    
                    if (monster.getId() != 8900002 && monster.getId() != 8900102) {
                        player.checkMonsterAggro(monster);
                    }
                    if (!(attack.skill == 0 || SkillFactory.getSkill(attack.skill).isChainAttack() || effect.isMist() || effect.getSourceId() == 400021030 || GameConstants.isLinkedSkill(attack.skill) || GameConstants.isNoApplySkill(attack.skill) || GameConstants.isNoDelaySkill(attack.skill) || monster.getStats().isBoss() || !(player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange)))) {
                        player.dropMessageGM(-5, "타겟이 범위를 벗어났습니다.");
                    }
                    if (monster != null && player.getBuffedValue(SecondaryStat.PickPocket) != null) {
                        SecondaryStatEffect eff = player.getBuffedEffect(SecondaryStat.PickPocket);
                        switch (attack.skill) {
                            case 0:
                            case 4001334:
                            case 4201004:
                            case 4201005:
                            case 4201012:
                            case 4211002:
                            case 4211004:
                            case 4211011:
                            case 4221007:
                            case 4221010:
                            case 4221014:
                            case 4221016:
                            case 4221052:
                            case 400041002:
                            case 400041003:
                            case 400041004:
                            case 400041005:
                            case 400041025:
                            case 400041026:
                            case 400041027:
                            case 400041039: {
                                int i;
                                int max = SkillFactory.getSkill(4211006).getEffect(player.getSkillLevel(4211006)).getBulletCount();
                                int rand = eff.getProp();
                                int suc = 0;
                                if (player.getSkillLevel(4220045) > 0) {
                                    rand += SkillFactory.getSkill(4220045).getEffect(player.getSkillLevel(4220045)).getProp();
                                    max += SkillFactory.getSkill(4220045).getEffect(player.getSkillLevel(4220045)).getBulletCount();
                                }
                                if (attack.skill == 4221007) {
                                    rand /= 2;
                                }
                                for (i = 0; i < attack.hits; ++i) {
                                    if (!Randomizer.isSuccess(rand)) {
                                        continue;
                                    }
                                    ++suc;
                                }
                                for (i = 0; i < suc; ++i) {
                                    int 기준;
                                    Point pos = new Point(monster.getTruePosition().x, monster.getTruePosition().y);
                                    int delay = 208;
                                    int plus = 120 * i;
                                    delay += plus;
                                    if (suc % 2 == 0) {
                                        기준 = suc / 2;
                                        if (i < 기준) {
                                            pos.x -= 18 * (기준 - i);
                                        } else if (i >= 기준) {
                                            pos.x += 18 * (i - 기준);
                                        }
                                    } else {
                                        기준 = suc / 2;
                                        if (i < 기준) {
                                            pos.x -= 18 * (기준 - i);
                                        } else if (i > 기준) {
                                            pos.x += 18 * (i - 기준);
                                        }
                                    }
                                    if (player.getPickPocket().size() >= max) {
                                        continue;
                                    }
                                    player.getMap().spawnMesoDrop(1, player.getMap().calcDropPos(pos, monster.getTruePosition()), monster, player, false, (byte) 0, delay);
                                    player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(eff.getStatups(), eff, player));
                                }
                                break;
                            }
                        }
                    }
                    Integer[] 격투스킬 = new Integer[]{5101012, 5111002, 5121016};
                    if (player.getBuffedValue(SecondaryStat.SeaSerpent) != null) {
                        MapleCharacter chr = player;
                        SecondaryStatEffect SerpentStone = SkillFactory.getSkill(5111017).getEffect(1);
                        int skillid = 0;
                        int max = SerpentStone.getU();
                        if (Arrays.asList(격투스킬).contains(attack.skill)) {
                            if (chr.getSkillLevel(5110016) > 0) {
                                skillid = 5111021;
                                if (chr.getSkillLevel(5120029) > 0) {
                                    skillid = 5121023;
                                }
                            }
                            if (!chr.skillisCooling(skillid) && !chr.getBuffedValue(5110020)) {
                                chr.getClient().getSession().writeAndFlush(CField.rangeAttack(5101017, Arrays.asList(new RangeAttack(skillid, chr.getPosition(), ((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1))));
                                if (chr.서펜트스톤 < max) {
                                    chr.서펜트스톤++;
                                    SerpentStone.applyTo(chr, false, false);
                                }
                            } else if (chr.getBuffedValue(5110020)) {
                                chr.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 5110020);
                                chr.getClient().getSession().writeAndFlush(CField.rangeAttack(5101017, Arrays.asList(new RangeAttack(5121023, chr.getPosition(), ((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1))));
                                chr.getClient().getSession().writeAndFlush(CField.rangeAttack(5110018, Arrays.asList(new RangeAttack(5111019, chr.getPosition(), ((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1))));
                            }
                        } else if (attack.skill == 5121007) {
                            if (!chr.skillisCooling(5121025) && !chr.getBuffedValue(5110020)) {
                                chr.getClient().getSession().writeAndFlush(CField.rangeAttack(5101017, Arrays.asList(new RangeAttack(5121025, chr.getPosition(), ((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1))));
                                if (chr.서펜트스톤 < max) {
                                    chr.서펜트스톤++;
                                    SerpentStone.applyTo(chr, false, false);
                                }
                            } else if (chr.getBuffedValue(5110020)) {
                                chr.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 5110020);
                                final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
                                atoms.add(new SecondAtom(0x24, player.getId(), 0, 5121027, 5000, 0, 15, new Point(monster.getPosition().x, monster.getPosition().y), Arrays.asList(0)));
                                player.spawnSecondAtom(atoms);
                            }
                        }
                    }
                    if (player.getSkillLevel(5120028) > 0) {
                        SkillFactory.getSkill(5120028).getEffect(player.getSkillLevel(5120028)).applyTo(player, false, false);
                    }
                    int probability = 9998;
                    if (monster != null) {
                        if (player.getMonster(9500006) == false && player.getMonster(9500007) == false) {
                            if (Randomizer.nextInt(10000) > probability && !monster.getStats().isBoss()) { // 대박 사냥
                                if (Randomizer.isSuccess(50)) {
                                    if (Randomizer.isSuccess(30)) {
                                        player.spawnMob(9500006, (int) player.getPosition().getX(), (int) player.getPosition().getY());
                                        player.dropShowInfo("[대박 보스] 거대 도령 월묘가 출현 하였습니다 !");
                                        World.Broadcast.broadcastMessage(CField.getGameMessage(1, player.getName() + " 님의 장소에 대박 보스가 출현 하였습니다 !"));
                                    } else {
                                        player.spawnMob(9500007, (int) player.getPosition().getX(), (int) player.getPosition().getY());
                                        player.dropShowInfo("[대박 보스] 거대 아씨 월묘가 출현 하였습니다 !");
                                        World.Broadcast.broadcastMessage(CField.getGameMessage(1, player.getName() + " 님의 장소에 대박 보스가 출현 하였습니다 !"));
                                    }
                                }
                            }
                        }
                    }

                    if (player.getSkillLevel(5100015) > 0) {
                        if (player.getBuffedEffect(SecondaryStat.EnergyCharged) == null) {
                            if (player.getSkillLevel(5120018) > 0) {
                                SkillFactory.getSkill(5120018).getEffect(player.getSkillLevel(5120018)).applyTo(player, false);
                            } else if (player.getSkillLevel(5110014) > 0) {
                                SkillFactory.getSkill(5110014).getEffect(player.getSkillLevel(5110014)).applyTo(player, false);
                            } else {
                                SkillFactory.getSkill(5100015).getEffect(player.getSkillLevel(5100015)).applyTo(player, false);
                            }
                        }
                        HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                        SecondaryStatEffect energyCharge = player.getBuffedEffect(SecondaryStat.EnergyCharged);
                        int max = energyCharge.getZ();
                        int add = energyCharge.getX();
                        if (monster.getStats().isBoss()) {
                            add *= 2;
                        }
                        if (!player.energyCharge && attack.skill != 400051015) {
                            player.energy = Math.min(max, player.energy + add);
                            if (player.energy == max) {
                                player.energyCharge = true;
                            }
                        } else {
                            int forceCon = 0;
                            if (attack.skill == 400051015) {
                                forceCon = effect.getX() / attack.targets;
                                if (monster.getStats().isBoss()) {
                                    forceCon = forceCon * (100 - effect.getZ()) / 100;
                                }
                            } else if (attack.skill != 0) {
                                forceCon = effect.getForceCon() / attack.targets;
                            }
                            player.energy = Math.max(0, player.energy - forceCon);
                            if (player.energy == 0) {
                                player.energyCharge = false;
                            }
                        }
                        statups.put(SecondaryStat.EnergyCharged, new Pair<Integer, Integer>(player.energy, 0));
                        player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(SecondaryStat.EnergyCharged), player));
                        player.getMap().broadcastMessage(player, CWvsContext.BuffPacket.giveForeignBuff(player, statups, player.getBuffedEffect(SecondaryStat.EnergyCharged)), false);
                    }
                    if (monster != null) {
                        monster.setNextSkill(260);
                        monster.setNextSkillLvl(3);
                    }
                    if (attack.skill == 2100001) {
                        ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                        SecondaryStatEffect eff = SkillFactory.getSkill(2100001).getEffect(player.getSkillLevel(2100001));
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, eff.getDOTTime(), (long) eff.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L)));
                        monster.applyStatus(player.getClient(), applys, effect);
                    }
                    if (attack.skill == 1121015) {
                        ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                        SecondaryStatEffect eff = SkillFactory.getSkill(1121015).getEffect(player.getSkillLevel(1121015));
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, 60000, (long) eff.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L)));
                        monster.applyStatus(player.getClient(), applys, effect);
                    }
                    if (player.getBuffedValue(SecondaryStat.QuiverCatridge) != null && attack.skill != 400031021 && attack.skill != 95001000 && attack.skill != 3111013 && attack.skill != 3100010 && attack.skill != 400031000) {
                        boolean adquiver = player.getBuffedValue(SecondaryStat.AdvancedQuiver) != null;
                        boolean quiverFoolburst = player.getBuffedValue(SecondaryStat.QuiverFullBurst) != null;
                        boolean reset = false;
                        SecondaryStatEffect effect2 = SkillFactory.getSkill(3101009).getEffect(player.getSkillLevel(3101009));
                        SecondaryStatEffect quiverEff = SkillFactory.getSkill(3101009).getEffect(player.getSkillLevel(3101009));
                        if (player.getSkillLevel(3121016) > 0) {
                            quiverEff = SkillFactory.getSkill(3121016).getEffect(player.getSkillLevel(3121016));
                        }
                        if (quiverFoolburst) {
                            SecondaryStatEffect quiverFoolEff = player.getBuffedEffect(SecondaryStat.QuiverFullBurst);
                            if (Randomizer.isSuccess(quiverEff.getW()) && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
                                player.addHP((int) ((double) player.getStat().getCurrentMaxHp() * ((double) quiverEff.getX() / 100.0)));
                            }
                            monster.applyStatus(player.getClient(), MonsterStatus.MS_Burned, new MonsterStatusEffect(quiverEff.getSourceId(), quiverEff.getDuration()), quiverEff.getDOT(), effect);
                            MapleAtom atom = new MapleAtom(false, player.getId(), 10, true, 3100010, player.getTruePosition().x, player.getTruePosition().y);
                            atom.setDwFirstTargetId(0);
                            ForceAtom forceAtom = new ForceAtom(0, Randomizer.rand(10, 20), Randomizer.rand(5, 10), Randomizer.rand(4, 301), (short) Randomizer.rand(20, 48));
                            atom.addForceAtom(forceAtom);
                            player.getMap().spawnMapleAtom(atom);
                            if (System.currentTimeMillis() - player.lastFireArrowTime >= 2000L) {
                                player.lastFireArrowTime = System.currentTimeMillis();
                                MapleAtom atom2 = new MapleAtom(false, player.getId(), 50, true, 400031029, monster.getTruePosition().x, monster.getTruePosition().y);
                                for (int i = 0; i < quiverFoolEff.getY(); ++i) {
                                    atom2.addForceAtom(new ForceAtom(1, Randomizer.rand(30, 60), 10, Randomizer.nextBoolean() ? Randomizer.rand(10, 15) : Randomizer.rand(190, 195), (short) (i * 100)));
                                }
                                atom2.setDwFirstTargetId(0);
                                player.getMap().spawnMapleAtom(atom2);
                            }
                        } else {
                            switch (player.getQuiverType()) {
                                case 1: {
                                    if (!Randomizer.isSuccess(attack.skill == 400030002 ? quiverEff.getU() * 2 : quiverEff.getU())) {
                                        break;
                                    }
                                    /*
                                    if (!adquiver) {
                                        int[] arrn = player.getRestArrow();
                                        arrn[1] = arrn[1] - 1; //2였음
                                    }*/
                                    MapleAtom atom = new MapleAtom(false, player.getId(), 10, true, 3100010, player.getTruePosition().x, player.getTruePosition().y);
                                    atom.setDwFirstTargetId(0);
                                    ForceAtom forceAtom = new ForceAtom(0, Randomizer.rand(10, 20), Randomizer.rand(5, 10), Randomizer.rand(4, 301), (short) Randomizer.rand(20, 48));
                                    atom.addForceAtom(forceAtom);
                                    player.getMap().spawnMapleAtom(atom);
                                }
                                case 2: {
                                    if (!Randomizer.isSuccess(quiverEff.getW()) || player.getBuffedEffect(SecondaryStat.DebuffIncHp) != null) {
                                        break;
                                    }
                                    /*
                                    if (!adquiver) {
                                        int[] arrn = player.getRestArrow();
                                        arrn[0] = arrn[0] - 10000;
                                    }
                                     */
                                    player.addHP((int) ((double) player.getStat().getCurrentMaxHp() * ((double) quiverEff.getX() / 100.0)));
                                    break;
                                }
                            }
                        }
                        if (player.getQuiverType() == 0) {
                            player.setQuiverType((byte) 1);
                        }
                        if (player.getRestArrow()[player.getQuiverType() - 1] == 0) {
                            if (player.getRestArrow()[0] == 0 && player.getRestArrow()[1] == 0) {
                                reset = true;
                            } else {
                                player.setQuiverType((byte) (player.getQuiverType() == 2 ? 1 : player.getQuiverType() + 1));
                                int arrowcount = 0;
                                int arrowcount1 = 0;
                                arrowcount = player.getRestArrow()[0];
                                arrowcount1 = player.getRestArrow()[1];
                                player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 3101009, 57, player.getQuiverType() - 1, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getPosition(), null, null));
                                player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 3101009, 57, player.getQuiverType() - 1, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getPosition(), null, null), false);
                            }
                        }
                        if (!adquiver && !quiverFoolburst) {
                            effect2.applyTo(player, reset, false);
                        }
                    }
                    if (player.getBuffedValue(SecondaryStat.ShadowBatt) != null && (attack.skill == 14121001 || attack.skill == 14111022 || attack.skill == 14111020 || attack.skill == 14101020 || attack.skill == 14001020) && attack.targets > 0) {
                        int[] skillids;
                        SecondaryStatEffect b_eff = SkillFactory.getSkill(14000027).getEffect(player.getSkillLevel(14001027));
                        int BatLimit = 3;
                        int Chance = b_eff.getProp();
                        for (int skill : skillids = new int[]{14100027, 0xD74D4D, 14120008}) {
                            if (player.getSkillLevel(skill) <= 0) {
                                continue;
                            }
                            Chance += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getProp();
                            BatLimit += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getY();
                        }
                        ArrayList<MapleSummon> summons = new ArrayList<MapleSummon>();
                        if (player.getSummonsSize() > 0) {
                            for (MapleSummon summon2 : player.getSummons()) {
                                if (summon2.getSkill() != 14000027) {
                                    continue;
                                }
                                summons.add(summon2);
                            }
                        }
                        if (player.battAttackCount <= -1) {
                            player.battAttackCount = 0;
                        }
                        player.battAttackCount = (byte) (player.battAttackCount + 1);
                        if (summons.size() > 0 && Randomizer.isSuccess(Chance)) {
                            MapleSummon deleted = (MapleSummon) summons.get(Randomizer.nextInt(summons.size()));
                            MapleAtom atom = new MapleAtom(false, player.getId(), 15, true, 14000028, deleted.getTruePosition().x, deleted.getTruePosition().y);
                            atom.setDwFirstTargetId(monster.getObjectId());
                            ForceAtom forceAtom = new ForceAtom(player.getSkillLevel(14120008) > 0 ? 2 : 1, 1, 5, Randomizer.rand(45, 90), 500);
                            atom.addForceAtom(forceAtom);
                            player.getMap().spawnMapleAtom(atom);
                            if (deleted != null) {
                                deleted.removeSummon(player.getMap(), false, false);
                                player.removeSummon(deleted);
                            }
                        }
                        if (player.battAttackCount == b_eff.getZ()) {
                            player.battAttackCount = 0;
                            if (summons.size() < BatLimit) {
                                b_eff.applyTo(player, player.getPosition(), false, 60000);
                            }
                        }
                        player.dropMessageGM(-8, "서몬 사이즈 : " + summons.size() + " / 배트 제한 : " + BatLimit);
                    }
                    if ((GameConstants.isDarkAtackSkill(attack.skill) || attack.summonattack != 0) && attack.skill != 14000027 && attack.skill != 14000028 && attack.skill != 14000029) {
                        player.changeCooldown(14121003, -500);
                    }
                    if (attack.skill == 14000028) {
                        player.addHP(player.getStat().getCurrentMaxHp() / 100L);
                    }
                    if (GameConstants.isAngelicBuster(player.getJob())) {
                        MapleSummon summon3;
                        MapleSummon s;
                        if (player.getBuffedValue(400051011) && attack.skill != 60011216 && (s = player.getSummon(400051011)) != null) {
                            MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 29, true, 400051011, monster.getTruePosition().x, monster.getTruePosition().y);
                            atom.setDwUserOwner(player.getId());
                            atom.setDwFirstTargetId(monster.getObjectId());
                            atom.addForceAtom(new ForceAtom(Randomizer.rand(1, 3), Randomizer.rand(30, 50), Randomizer.rand(3, 10), Randomizer.rand(4, 301), 0));
                            atom.addForceAtom(new ForceAtom(Randomizer.rand(1, 3), Randomizer.rand(30, 50), Randomizer.rand(3, 10), Randomizer.rand(4, 301), 0));
                            player.getMap().spawnMapleAtom(atom);
                            player.setEnergyBurst(player.getEnergyBurst() + 1);
                            if (player.getEnergyBurst() == 50) {
                                player.setBuffedValue(SecondaryStat.EnergyBurst, 3);
                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                statups.put(SecondaryStat.EnergyBurst, new Pair<Integer, Integer>(player.getBuffedValue(SecondaryStat.EnergyBurst), (int) player.getBuffLimit(400051011)));
                                player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(SecondaryStat.EnergyBurst), player));
                                player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.updateSummon(s, 15));
                            } else if (player.getEnergyBurst() == 25) {
                                player.setBuffedValue(SecondaryStat.EnergyBurst, 2);
                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                statups.put(SecondaryStat.EnergyBurst, new Pair<Integer, Integer>(player.getBuffedValue(SecondaryStat.EnergyBurst), (int) player.getBuffLimit(400051011)));
                                player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(SecondaryStat.EnergyBurst), player));
                                player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.updateSummon(s, 14));
                            }
                        }
                        if (player.getBuffedValue(400051046) && player.getSkillCustomValue(400051046) == null && (summon3 = player.getSummon(400051046)) != null) {
                            player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.DeathAttack(summon3, Randomizer.rand(8, 9)));
                        }
                    }
                    if (GameConstants.isKadena(player.getJob())) {
                        ArrayList<Pair<Integer, Integer>> WeponList = new ArrayList<Pair<Integer, Integer>>();
                        WeponList.add(new Pair<Integer, Integer>(0, 64121002));
                        WeponList.add(new Pair<Integer, Integer>(1, 64001002));
                        WeponList.add(new Pair<Integer, Integer>(1, 64001013));
                        WeponList.add(new Pair<Integer, Integer>(2, 64101002));
                        WeponList.add(new Pair<Integer, Integer>(2, 64101008));
                        WeponList.add(new Pair<Integer, Integer>(3, 64101001));
                        WeponList.add(new Pair<Integer, Integer>(4, 64111002));
                        WeponList.add(new Pair<Integer, Integer>(5, 64111003));
                        WeponList.add(new Pair<Integer, Integer>(6, 64111004));
                        WeponList.add(new Pair<Integer, Integer>(6, 64111012));
                        WeponList.add(new Pair<Integer, Integer>(7, 64121021));
                        WeponList.add(new Pair<Integer, Integer>(7, 64121022));
                        WeponList.add(new Pair<Integer, Integer>(7, 64121023));
                        WeponList.add(new Pair<Integer, Integer>(7, 64121024));
                        WeponList.add(new Pair<Integer, Integer>(8, 64121003));
                        WeponList.add(new Pair<Integer, Integer>(8, 64121011));
                        WeponList.add(new Pair<Integer, Integer>(8, 64121016));
                        boolean attackc = false;
                        boolean givebuff = false;
                        int attackid = 0;
                        if (attack.skill == 64121002 || attack.skill == 64001002 || attack.skill == 64001013 || attack.skill == 64101002 || attack.skill == 64101008 || attack.skill == 64101001 || attack.skill == 64111002 || attack.skill == 64111003 || attack.skill == 64111004 || attack.skill == 64111012 || attack.skill == 64121021 || attack.skill == 64121022 || attack.skill == 64121023 || attack.skill == 64121024 || attack.skill == 64121003 || attack.skill == 64121011 || attack.skill == 64121016) {
                            for (Pair info : WeponList) {
                                if (attack.skill != (Integer) info.getRight()) {
                                    continue;
                                }
                                if (player.getBuffedEffect(SecondaryStat.WeaponVariety) == null) {
                                    player.weaponChanges1.clear();
                                    player.removeSkillCustomInfo(6412);
                                }
                                if ((Integer) info.left != 0 && !player.weaponChanges1.containsKey(info.left) && player.weaponChanges1.size() < 8) {
                                    player.weaponChanges1.put((Integer) info.left, (Integer) info.right);
                                    attackc = true;
                                    givebuff = true;
                                }
                                if (player.getSkillCustomValue0(6412) != (long) ((Integer) info.getLeft()).intValue()) {
                                    givebuff = true;
                                    attackc = true;
                                }
                                if (player.weaponChanges1.size() == 1) {
                                    attackc = false;
                                }
                                if (givebuff) {
                                    if (player.getSkillLevel(64120006) > 0) {
                                        SkillFactory.getSkill(64120006).getEffect(player.getSkillLevel(64120006)).applyTo(player, false);
                                        attackid = 64120006;
                                    } else if (player.getSkillLevel(64110005) > 0) {
                                        SkillFactory.getSkill(64110005).getEffect(player.getSkillLevel(64110005)).applyTo(player, false);
                                        attackid = 64110005;
                                    } else if (player.getSkillLevel(64100004) > 0) {
                                        SkillFactory.getSkill(64100004).getEffect(player.getSkillLevel(64100004)).applyTo(player, false);
                                        attackid = 64100004;
                                    }
                                }
                                if (attackc && System.currentTimeMillis() - player.lastBonusAttckTime > 500L) {
                                    player.lastBonusAttckTime = System.currentTimeMillis();
                                    player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(attackid == 64120006 ? 64121020 : (attackid == 64110005 ? 64111013 : 64101009), finalMobList, true, 0, new int[0]));
                                }
                                if (!givebuff) {
                                    continue;
                                }
                                player.setSkillCustomInfo(6412, ((Integer) info.getLeft()).intValue(), 0L);
                            }
                        }
                        if (player.getBuffedValue(64121053) && attack.skill != 64121055 && player.getSkillCustomValue(64121055) == null) {
                            ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                            mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 60, 0));
                            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(64121055, mobList, false, 0, new int[0]));
                            player.setSkillCustomInfo(64121055, 0L, 100L);
                        }
                        if ((attack.skill == 64121002 || attack.skill == 64121052 || attack.skill == 64121012 || attack.skill == 400041036) && player.getSkillCustomValue(400441774) != null && player.getSkillCustomTime(400441774) > 0) {
                            int cooltime;
                            SecondaryStatEffect effect2 = SkillFactory.getSkill(400041074).getEffect(player.getSkillLevel(400041074));
                            int n = cooltime = attack.skill == 400041036 ? effect2.getZ() * 1000 : effect2.getSubTime();
                            if (player.getSkillCustomTime(400441774) - cooltime <= 0) {
                                player.setSkillCustomInfo(400441774, 0L, 5L);
                            } else {
                                player.setSkillCustomInfo(400441774, 0L, player.getSkillCustomTime(400441774) - cooltime);
                            }
                        }
                    }
                    if (GameConstants.isFusionSkill(attack.skill) && attack.targets > 0 && player.getSkillCustomValue(22170070) == null) {
                        SecondaryStatEffect magicWreck = player.getSkillLevel(22170070) > 0 ? SkillFactory.getSkill(22170070).getEffect(player.getSkillLevel(22170070)) : SkillFactory.getSkill(22141017).getEffect(player.getSkillLevel(22141017));
                        if (player.getMap().getWrecks().size() < 15) {
                            int x = Randomizer.rand(-100, 150);
                            int y = Randomizer.rand(-50, 70);
                            MapleMagicWreck mw = new MapleMagicWreck(player, magicWreck.getSourceId(), new Point(monster.getTruePosition().x + x, monster.getTruePosition().y + y), 20000);
                            player.getMap().spawnMagicWreck(mw);
                            player.setSkillCustomInfo(22170070, 0L, player.getSkillLevel(22170070) > 0 ? 400L : 600L);
                        }
                    }
                    if (!(player.getBuffedValue(15121054) || attack.skill != 15111022 && attack.skill != 15120003 || player.lightning <= 2 || player.getBuffedEffect(SecondaryStat.CygnusElementSkill, 15001022) == null)) {
                        SecondaryStatEffect lightning = SkillFactory.getSkill(attack.skill).getEffect(attack.skilllevel);
                        lightning.applyTo(player, false);
                        player.cancelEffectFromBuffStat(SecondaryStat.IgnoreTargetDEF, 15001022);
                        player.cancelEffectFromBuffStat(SecondaryStat.IndiePmdR, 15001022);
                    }
                    if (player.getSkillLevel(3110001) > 0 && attack.skill != 95001000 && attack.skill != 3100010 && attack.skill != 400031029 && !GameConstants.is_forceAtom_attack_skill(attack.skill)) {
                        SkillFactory.getSkill(3110001).getEffect(player.getSkillLevel(3110001)).applyTo(player, false, false);
                    }
                    if (player.getSkillLevel(3210001) > 0 && attack.skill != 95001000 && attack.skill != 3100010 && attack.skill != 400031029) {
                        SkillFactory.getSkill(3210001).getEffect(player.getSkillLevel(3210001)).applyTo(player, false, false);
                    }
                    if (player.getSkillLevel(3110012) > 0 && attack.skill != 95001000 && attack.skill != 3100010 && attack.skill != 400031029) {
                        SecondaryStatEffect concentration = SkillFactory.getSkill(3110012).getEffect(player.getSkillLevel(3110012));
                        if (System.currentTimeMillis() - player.lastConcentrationTime >= (long) concentration.getY()) {
                            player.lastConcentrationTime = System.currentTimeMillis();
                            if (player.getConcentration() < 100) {
                                player.setConcentration((byte) (player.getConcentration() + concentration.getX()));
                            }
                            if (player.getBuffedValue(3110012) && player.getConcentration() < player.getSkillLevel(3110012) || !player.getBuffedValue(3110012)) {
                                concentration.applyTo(player, false, false);
                            }
                        }
                    }
                    if (player.getJob() == 112) {
                        if ((attack.skill == 1101011 || attack.skill == 1111010 || attack.skill == 1121008 || attack.skill == 1121015) && attack.targets > 0 && player.getBuffedValue(1121054) && player.발할라검격 >= 3) {
                            for (int i = 0; i < 3; i++) {
                                MapleSummon summon4 = new MapleSummon(player, 1121055, attack.attackPosition, SummonMovementType.STATIONARY, (byte) 0, 10000);
                                player.getMap().spawnSummon(summon4, 10000);
                                player.addSummon(summon4);
                                player.발할라검격--;
                            }
                            SecondaryStatEffect eff = SkillFactory.getSkill(1121054).getEffect(player.getSkillLevel(1121054));
                            HashMap<SecondaryStat, Pair<Integer, Integer>> statups3 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                            statups3.put(SecondaryStat.Stance, new Pair<Integer, Integer>(100, (int) player.skillcool(1121054) - 90000));
                            player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups3, eff, player));
                        }
                    }
                    if (attack.skill == 3301008 && attack.targets > 0) {
                        MapleAtom atom = new MapleAtom(true, player.getId(), 58, true, 3301009, player.getTruePosition().x, player.getTruePosition().y);
                        atom.setDwUserOwner(player.getId());
                        ArrayList<Integer> monsters = new ArrayList<Integer>();
                        monsters.add(0);
                        monsters.add(0);
                        atom.addForceAtom(new ForceAtom(1, Randomizer.rand(21, 25), Randomizer.rand(2, 4), Randomizer.rand(17, 21), 120, player.getTruePosition()));
                        atom.addForceAtom(new ForceAtom(1, Randomizer.rand(21, 25), Randomizer.rand(2, 4), Randomizer.rand(17, 21), 120, player.getTruePosition()));
                        atom.setSearchX1(650);
                        atom.setSearchY1(250);
                        atom.setnDuration(2);
                        atom.setSearchX(560);
                        atom.setSearchY(2);
                        atom.setDwTargets(monsters);
                        player.getMap().spawnMapleAtom(atom);
                    }
                    if (attack.skill == 3321036 && attack.targets > 0 && Randomizer.isSuccess(30)) {
                        MapleAtom atom = new MapleAtom(true, player.getId(), 58, true, 3321037, player.getTruePosition().x, player.getTruePosition().y);
                        atom.setDwUserOwner(player.getId());
                        ArrayList<Integer> monsters = new ArrayList<Integer>();
                        monsters.add(0);
                        monsters.add(0);
                        atom.addForceAtom(new ForceAtom(3, Randomizer.rand(21, 25), Randomizer.rand(2, 4), Randomizer.rand(17, 21), 120, new Point(monster.getTruePosition().x + Randomizer.rand(-150, 150), monster.getTruePosition().y + Randomizer.rand(-200, 100))));
                        atom.addForceAtom(new ForceAtom(3, Randomizer.rand(21, 25), Randomizer.rand(2, 4), Randomizer.rand(17, 21), 120, new Point(monster.getTruePosition().x + Randomizer.rand(-150, 150), monster.getTruePosition().y + Randomizer.rand(-200, 100))));
                        atom.setDwTargets(monsters);
                        atom.setSearchX1(650);
                        atom.setSearchY1(250);
                        atom.setnDuration(2);
                        atom.setSearchX(560);
                        atom.setSearchY(2);
                        player.getMap().spawnMapleAtom(atom);
                    }
                    if (GameConstants.isKaiser(player.getJob()) && attack.skill != 61120018 && attack.skill != 0 && !player.getBuffedValue(61111008) && !player.getBuffedValue(61120008) && !player.getBuffedValue(61121053)) {
                        player.handleKaiserCombo(attack.skill);
                    }
                    if (totDamageToOneMonster <= 0L && attack.skill != 1221011 && attack.skill != 21120006 && attack.skill != 164001001) {
                        continue;
                    }
                    if (GameConstants.isDemonSlayer(player.getJob()) && attack.skill != 31101002) {
                        if (attack.skill != 31101002 && player.getSkillLevel(30010111) > 0) {
                            if (Randomizer.isSuccess(1)) {
                                totDamageToOneMonster *= 2L;
                                player.addHP((long) ((double) player.getStat().getCurrentMaxHp() * 0.05));
                            }
                            if (monster.getHp() <= totDamageToOneMonster) {
                                player.handleForceGain(monster.getObjectId(), 30010111);
                            }
                        }
                        player.handleForceGain(monster.getObjectId(), attack.skill, monster.getStats().isBoss() ? 1 : 0);
                    }
                    if (GameConstants.isPhantom(player.getJob()) && (player.getSkillLevel(24120002) > 0 || player.getSkillLevel(24100003) > 0)) {
                        Skill noir = SkillFactory.getSkill(24120002);
                        Skill blanc = SkillFactory.getSkill(24100003);
                        SecondaryStatEffect ceffect = null;
                        int advSkillLevel = player.getTotalSkillLevel(noir);
                        boolean active = true;
                        if (advSkillLevel > 0) {
                            ceffect = noir.getEffect(advSkillLevel);
                        } else if (player.getSkillLevel(blanc) > 0) {
                            ceffect = blanc.getEffect(player.getTotalSkillLevel(blanc));
                        } else {
                            active = false;
                        }
                        if (attack.skill == 24120055 || attack.skill == noir.getId() || attack.skill == blanc.getId() || attack.skill == 24121011) {
                            active = false;
                        }
                        if (attack.skill == 400041010) {
                            active = true;
                        }
                        if (active) {
                            if (player.getCardStack() < (advSkillLevel > 0 ? (byte) 40 : 20)) {
                                player.setCardStack((byte) (player.getCardStack() + 1));
                                player.getClient().getSession().writeAndFlush((Object) CField.updateCardStack(false, player.getCardStack()));
                            }
                            MapleAtom atom = new MapleAtom(false, player.getId(), 1, true, advSkillLevel > 0 ? 24120002 : 24100003, player.getTruePosition().x, player.getTruePosition().y);
                            atom.setDwFirstTargetId(monster.getObjectId());
                            atom.addForceAtom(new ForceAtom(2, Randomizer.rand(15, 29), Randomizer.rand(7, 11), Randomizer.rand(0, 9), 0));
                            player.getMap().spawnMapleAtom(atom);
                        }
                        if (advSkillLevel > 0 && SkillFactory.getSkill(24121011).getSkillList().contains(attack.skill) && player.getSkillCustomValue(24121011) == null) {
                            SecondaryStatEffect eff = SkillFactory.getSkill(24121011).getEffect(player.getSkillLevel(24120002));
                            ArrayList<Integer> objid = new ArrayList<Integer>();
                            ArrayList<Integer> attackk = new ArrayList<Integer>();
                            int i = 0;
                            for (AttackPair oned1 : attack.allDamage) {
                                objid.add(oned1.objectId);
                            }
                            for (Object mob : player.getMap().getAllMonster()) {
                                boolean attacked = true;
                                for (Object a2 : objid) {
                                    if (((Integer) a2).intValue() != ((MapleMapObject) mob).getObjectId()) {
                                        continue;
                                    }
                                    attacked = false;
                                    break;
                                }
                                if (!attacked || !eff.calculateBoundingBox(player.getPosition(), (attack.facingleft >>> 4 & 0xF) == 0).contains(((MapleMapObject) mob).getPosition())) {
                                    continue;
                                }
                                attackk.add(((MapleMapObject) mob).getObjectId());
                                if (++i < ceffect.getY()) {
                                    continue;
                                }
                                break;
                            }
                            if (!attackk.isEmpty()) {
                                final MapleAtom atom6 = new MapleAtom(false, player.getId(), 73, true, 24121011, player.getTruePosition().x, player.getTruePosition().y);
                                atom6.setDwTargets(attackk);
                                for (final Integer objectId : attackk) {
                                    atom6.addForceAtom(new ForceAtom(1, Randomizer.rand(15, 21), Randomizer.rand(7, 11), Randomizer.rand(0, 9), 0));
                                }
                                player.getMap().spawnMapleAtom(atom6);
                                player.setSkillCustomInfo(24121011, 0L, ceffect.getW() * 1000);
                            }
                        }
                        if (player.getSkillLevel(400041040) > 0) {
                            SecondaryStatEffect eff = SkillFactory.getSkill(400041040).getEffect(player.getSkillLevel(400041040));
                            if (SkillFactory.getSkill(400041040).getSkillList().contains(attack.skill) || attack.skill / 10000 != 2400 && attack.skill / 10000 != 2410 && attack.skill / 10000 != 2411 && attack.skill / 10000 != 2412 && attack.skill < 400000000) {
                                if (attack.skill == 24001000 || attack.skill == 24111000) {
                                    player.setMarkOfPhantomOid(monster.getObjectId());
                                    eff.applyTo(player, false);
                                } else if (attack.skill == 24121000 || attack.skill == 24121005 || attack.skill == 400041055 || attack.skill == 400041056) {
                                    player.setUltimateDriverCount(player.getUltimateDriverCount() + 1);
                                    if (player.getUltimateDriverCount() >= eff.getY()) {
                                        player.setMarkOfPhantomOid(monster.getObjectId());
                                        player.setUltimateDriverCount(0);
                                        eff.applyTo(player, false);
                                    }
                                } else {
                                    player.addSkillCustomInfo(400341040, 1L);
                                    if (player.getSkillCustomValue0(400341040) >= (long) eff.getW()) {
                                        player.setMarkOfPhantomOid(monster.getObjectId());
                                        player.removeSkillCustomInfo(400341040);
                                        eff.applyTo(player, false);
                                    }
                                }
                            }
                        }
                    }
                    if (player.getSkillLevel(80002762) > 0) {
                        if (player.getBuffedEffect(SecondaryStat.EmpiricalKnowledge) != null && player.empiricalKnowledge != null) {
                            if (map.getMonsterByOid(player.empiricalKnowledge.getObjectId()) != null) {
                                if (monster.getObjectId() != player.empiricalKnowledge.getObjectId() && monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
                                    player.empiricalStack = 0;
                                    player.empiricalKnowledge = monster;
                                }
                            } else {
                                player.empiricalStack = 0;
                                player.empiricalKnowledge = monster;
                            }
                        } else if (player.empiricalKnowledge != null) {
                            if (monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
                                player.empiricalKnowledge = monster;
                            }
                        } else {
                            player.empiricalKnowledge = monster;
                        }
                    }
                    boolean debinrear = false;
                    if (player.getSkillLevel(101120207) > 0) {
                        SecondaryStatEffect stunMastery = SkillFactory.getSkill(101120207).getEffect(player.getSkillLevel(101120207));
                        if (player.getGender() == 0 && stunMastery.makeChanceResult()) {
                            debinrear = true;
                            player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 101120207, 4, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null, null));
                            player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 101120207, 4, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null, null), false);
                            player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) stunMastery.getX());
                        }
                    }
                    monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    SecondaryStatEffect markOf = player.getBuffedEffect(SecondaryStat.MarkofNightLord);
                    Item nk = player.getInventory(MapleInventoryType.USE).getItem(attack.slot);
                    if (markOf != null && nk != null) {
                        if (player.getSkillLevel(4120018) > 0) {
                            markOf = SkillFactory.getSkill(4120018).getEffect(player.getSkillLevel(4120018));
                        }
                        int bulletCount = markOf.getBulletCount();
                        if (attack.skill != 400041038 && attack.skill != 4100012 && attack.skill != 4120019) {
                            if (monster.isBuffed(markOf.getSourceId()) || !monster.isAlive() && Randomizer.isSuccess(markOf.getProp())) {
                                if (attack.skill != 400041020 || attack.skill == 400041020 && Randomizer.isSuccess(SkillFactory.getSkill(400041020).getEffect(player.getSkillLevel(400041020)).getW())) {
                                    int i;
                                    List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 400000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
                                    ArrayList<Integer> monsters = new ArrayList<Integer>();
                                    for (i = 0; i < bulletCount; ++i) {
                                        int rand = objs.size() < 1 ? -1 : Randomizer.nextInt(objs.size());
                                        if (rand < 0) {
                                            continue;
                                        }
                                        if (objs.size() < bulletCount) {
                                            if (i < objs.size()) {
                                                monsters.add(objs.get(i).getObjectId());
                                                continue;
                                            }
                                            monsters.add(objs.get(rand).getObjectId());
                                            continue;
                                        }
                                        if (objs.size() <= 1) {
                                            continue;
                                        }
                                        monsters.add(objs.get(rand).getObjectId());
                                        objs.remove(rand);
                                    }
                                    if (monsters.isEmpty()) {
                                        for (i = 0; i < bulletCount; ++i) {
                                            monsters.add(0);
                                        }
                                    }
                                    MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 11, true, monster.isBuffed(4120018) ? 0x3EDDD3 : 4100012, monster.getTruePosition().x, monster.getTruePosition().y);
                                    atom.setDwUserOwner(player.getId());
                                    atom.setDwTargets(monsters);
                                    atom.setnItemId(player.getV("csstar") != null ? Integer.parseInt(player.getV("csstar")) : nk.getItemId());
                                    if (monsters.size() > 0) {
                                        for (Integer objectId : monsters) {
                                            ForceAtom forceAtom = new ForceAtom(2, Randomizer.rand(41, 44), Randomizer.rand(3, 4), Randomizer.rand(67, 292), 200);
                                            atom.addForceAtom(forceAtom);
                                        }
                                        player.getMap().spawnMapleAtom(atom);
                                    }
                                    if (monster.isBuffed(markOf.getSourceId())) {
                                        monster.cancelSingleStatus(monster.getBuff(markOf.getSourceId()), markOf.getSourceId());
                                    }
                                }
                            } else if (attack.skill != 4111003) {
                                if (player.getSkillLevel(4120018) > 0) {
                                    markOf = SkillFactory.getSkill(4120018).getEffect(1);
                                    markOf.setDuration(20000);
                                } else {
                                    markOf = SkillFactory.getSkill(4100011).getEffect(1);
                                    markOf.setDuration(20000);
                                }
                                if (markOf.makeChanceResult() && monster.isAlive()) {
                                    ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(markOf.getSourceId(), markOf.getDOTTime(), 0L)));
                                    monster.applyStatus(player.getClient(), applys, markOf);
                                }
                            }
                        }
                    }
                    if (monster.getId() >= 9500650 && monster.getId() <= 9500654 && totDamageToOneMonster > 0L && player.getGuild() != null) {
                        player.getGuild().updateGuildScore(totDamageToOneMonster);
                    }
                    if (monster.isBuffed(MonsterStatus.MS_PCounter) && player.getBuffedEffect(SecondaryStat.IgnorePImmune) == null && player.getBuffedEffect(SecondaryStat.IgnorePCounter) == null && player.getBuffedEffect(SecondaryStat.IgnoreAllCounter) == null && player.getBuffedEffect(SecondaryStat.IgnoreAllImmune) == null && !SkillFactory.getSkill(attack.skill).isIgnoreCounter() && !energy) {
                        player.addHP(-monster.getBuff(MonsterStatus.MS_PCounter).getValue());
                    }
                    if (SkillFactory.getSkill(164101003).getSkillList().contains(attack.skill) && player.getBuffedEffect(SecondaryStat.Alterego) != null && System.currentTimeMillis() - player.lastAltergoTime >= 1500L) {
                        List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 1000000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
                        player.lastAltergoTime = System.currentTimeMillis();
                        MapleAtom atom = new MapleAtom(false, player.getId(), 60, true, 164101004, player.getTruePosition().x, player.getTruePosition().y);
                        ArrayList<Integer> monsters = new ArrayList<Integer>();
                        int fora = player.getBuffedValue(400041048) ? 12 : 3;
                        int i = 0;
                        for (MapleMapObject o : objs) {
                            monsters.add(o.getObjectId());
                            if (++i < fora) {
                                continue;
                            }
                            break;
                        }
                        while (i < fora) {
                            monsters.add(monster.getObjectId());
                            ++i;
                        }
                        for (Integer m : monsters) {
                            atom.addForceAtom(new ForceAtom(player.getBuffedValue(400041048) ? 1 : 0, Randomizer.rand(40, 49), 3, Randomizer.rand(45, 327), 0));
                        }
                        atom.setDwTargets(monsters);
                        player.getMap().spawnMapleAtom(atom);
                    }
                    if (!monster.isAlive()) {
                        multikill = (byte) (multikill + 1);
                    }
                    if (player.getBuffedValue(400001050) && player.getSkillCustomValue0(400001050) == 400001055L) {
                        SecondaryStatEffect effect6 = SkillFactory.getSkill(400001050).getEffect(player.getSkillLevel(400001050));
                        player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400001055, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
                        player.removeSkillCustomInfo(400001050);
                        long duration = player.getBuffLimit(400001050);
                        effect6.applyTo(player, false, (int) duration);
                    }
                    if (attack.skill == 164001001) {
                        MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 63, true, 164001001, monster.getTruePosition().x, monster.getTruePosition().y);
                        atom.setDwUserOwner(player.getId());
                        atom.setDwFirstTargetId(player.getId());
                        atom.setDwTargetId(monster.getObjectId());
                        atom.addForceAtom(new ForceAtom(1, 5, 30, 0, 0));
                        player.getMap().spawnMapleAtom(atom);
                    }
                    if (attack.skill == 164001002 && monster != null && monster.getBuff(164001001) != null) {
                        monster.cancelSingleStatus(monster.getBuff(164001001));
                    }
                    if (player.getBuffedEffect(SecondaryStat.ButterflyDream) != null && System.currentTimeMillis() - player.lastButterflyTime >= (long) (player.getBuffedEffect(SecondaryStat.ButterflyDream).getX() * 1000)) {
                        player.lastButterflyTime = System.currentTimeMillis();
                        MapleAtom atom = new MapleAtom(false, player.getObjectId(), 63, true, 164001001, player.getTruePosition().x, player.getTruePosition().y);
                        atom.setDwFirstTargetId(0);
                        atom.addForceAtom(new ForceAtom(1, 42, 3, 136, 0));
                        player.getMap().spawnMapleAtom(atom);
                    }
                    if (attack.skill == 400011047 && player.getBuffedValue(400011047)) {
                        player.getMap().broadcastMessage(MobPacket.skillAttackEffect(monster.getObjectId(), attack.skill, player.getId()));
                        player.setGraveTarget(player.getObjectId());
                        player.createSecondAtom(SkillFactory.getSkill(400011047).getSecondAtoms(), monster.getPosition());
                    }
                    if (effect != null && monster.isAlive()) {
                        SecondaryStatEffect eff;
                        Iterator stunMastery;
                        ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
                        ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz1 = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
                        ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz2 = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
                        ArrayList statusz3 = new ArrayList();
                        ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                        ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys1 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                        ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys2 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                        switch (attack.skill) {
                            case 1101012: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 1121015: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Incizing, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                //statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, 60000), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 1201011: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 1201012: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getX())));
                                break;
                            }
                            case 1201013: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 1211008: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 1221004: {
                                if (monster.getStats().isBoss()) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Seal, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getDuration())));
                                break;
                            }
                            case 1301012: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, 1000), 1L));
                                break;
                            }
                            case 2301010: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(attack.skill, 60000), (long) effect.getX()));
                                break;
                            }
                            case 2201004:
                            case 2201008:
                            case 2201009:
                            case 2211002:
                            case 2211006:
                            case 2211010:
                            case 2220014:
                            case 2221003:
                            case 2221011:
                            case 2221012:
                            case 2221054:
                            case 400020002: {
                                if (attack.skill != 2221011) {
                                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(effect.getV())));
                                    if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
                                        monster.setFreezingOverlap(0);
                                    }
                                    if (monster.getFreezingOverlap() < 5) {
                                        monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() + 1));
                                    }
                                }
                                if (attack.skill != 2221011) {
                                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, 13000), 1L));
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getX())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getY())));
                                break;
                            }
                            case 2201005:
                            case 2211003:
                            case 2211011:
                            case 2221006: {
                                if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
                                    monster.setFreezingOverlap(0);
                                }
                                if (monster.getFreezingOverlap() > 0) {
                                    monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
                                    if (monster.getFreezingOverlap() <= 0) {
                                        monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
                                    } else {
                                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
                                    }
                                }
                                if (attack.skill != 2221006) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 2221052:
                            case 400021031:
                            case 400021094: {
                                if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
                                    monster.setFreezingOverlap(0);
                                }
                                if (attack.skill == 400021094) {
                                    monster.addSkillCustomInfo(400021094, 1L);
                                    if (monster.getFreezingOverlap() > 0) {
                                        if (attack.skill == 400021094 && monster.getCustomValue0(400021094) >= 5L) {
                                            monster.removeCustomInfo(400021094);
                                            monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
                                            statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
                                        }
                                    } else {
                                        monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
                                    }
                                }
                                if (monster.getFreezingOverlap() > 0) {
                                    monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
                                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
                                    break;
                                }
                                if (monster.getFreezingOverlap() > 0) {
                                    break;
                                }
                                monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
                                break;
                            }
                            case 2111007: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), 1L));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 2121055: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 131001213: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, SkillFactory.getSkill(131001013).getEffect(1).getV2() * 1000), (long) SkillFactory.getSkill(131001013).getEffect(1).getW() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 131001313: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind, new MonsterStatusEffect(attack.skill, SkillFactory.getSkill(131001013).getEffect(1).getPsdJump() * 1000), Long.valueOf(SkillFactory.getSkill(131001013).getEffect(1).getY())));
                                break;
                            }
                            case 2211007:
                            case 2311007: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 3101005: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 3111003: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 3121014: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_DebuffHealing, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getX())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getW())));
                                break;
                            }
                            case 3121052: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieUNK2, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getS())));
                                break;
                            }
                            case 3201008: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getZ())));
                                break;
                            }
                            case 4111003: {
                                if (monster.getStats().isBoss()) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDuration()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Web, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 4121016: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 4121017: {
                                int data = effect.getX();
                                if (player.getSkillLevel(0x3EDDED) > 0) {
                                    data += SkillFactory.getSkill(0x3EDDED).getEffect(1).getX();
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Showdown, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(data)));
                                break;
                            }
                            case 4201004: {
                                if (monster.getStats().isBoss()) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 4221010: {
                                statusz1.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 4321002: {
                                statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_AdddamParty, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 10L));
                                break;
                            }
                            case 4321004: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_RiseByToss, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 100L));
                                break;
                            }
                            case 4331006: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 4341011: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 5011002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getZ())));
                                break;
                            }
                            case 37110002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_BlessterDamage, new MonsterStatusEffect(37110002, SkillFactory.getSkill(37110001).getEffect(GameConstants.getLinkedSkill(37110002)).getDuration() * 2), Long.valueOf(SkillFactory.getSkill(37110001).getEffect(GameConstants.getLinkedSkill(37110002)).getX())));
                                break;
                            }
                            case 5121001: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_DragonStrike, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getX())));
                                break;
                            }
                            case 5111002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 5311002: {
                                if (attack.charge != 1000) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 5310011: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_AdddamSkill, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getZ())));
                                break;
                            }
                            case 5311010: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Puriaus, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), Long.valueOf(effect.getZ())));
                                break;
                            }
                            case 13121052:
                            case 400031022: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 21100002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 21100013: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 21101016: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_RiseByToss, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 100L));
                                break;
                            }
                            case 21110011:
                            case 21110024:
                            case 21110025:
                            case 21111017: {
                                if (monster.getStats().isBoss()) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 23111002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Puriaus, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getX())));
                                break;
                            }
                            case 23121002: {
                                int minus = effect.getY();
                                if (player.getSkillLevel(23120050) > 0) {
                                    SecondaryStatEffect subeffect = SkillFactory.getSkill(23120050).getEffect(1);
                                    minus -= subeffect.getX();
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-minus)));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-minus)));
                                break;
                            }
                            case 23121003: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_AdddamSkill2, new MonsterStatusEffect(23121000, effect.getDuration()), Long.valueOf(effect.getX())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_DodgeBodyAttack, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 23120013: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 25100011:
                            case 25101003:
                            case 25101004:
                            case 25111004: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_BahamutLightElemAddDam, new MonsterStatusEffect(25100011, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getX())));
                                break;
                            }
                            case 25111206: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 25120003: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 25121006: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 25121007: {
                                if (monster.getStats().getCategory() == 1 || monster.getId() == 8880502 || monster.getId() == 8644650 || monster.getId() == 8644655 || monster.getId() == 8880342 || monster.getId() == 8880302) {
                                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieUNK, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(effect.getLevel())));
                                    break;
                                }
                                if (monster.getBuff(MonsterStatus.MS_SeperateSoulC) != null || monster.getBuff(MonsterStatus.MS_SeperateSoulP) != null) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_SeperateSoulP, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(effect.getLevel())));
                                break;
                            }
                            case 31101002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 31111001: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 31111005: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 31121001: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getX())));
                                break;
                            }
                            case 31101003: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 31121000: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_RiseByToss, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 100L));
                                break;
                            }
                            case 31121003: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getX())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getX())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Pad, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getX())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Mad, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getX())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getZ())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Showdown, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getW())));
                                break;
                            }
                            case 31211011: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 31221002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getY())));
                                break;
                            }
                            case 33101215: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 36001000:
                            case 36101000:
                            case 36101001:
                            case 36101008:
                            case 36101009:
                            case 36111000:
                            case 36111001:
                            case 36111002:
                            case 36111009:
                            case 36111010:
                            case 36121000:
                            case 36121001:
                            case 36121011:
                            case 36121012:
                            case 400041007: {
                                SecondaryStatEffect effect2;
                                if (player.getSkillLevel(36110005) <= 0) {
                                    break;
                                }
                                if (monster.getBuff(MonsterStatus.MS_Explosion) == null && monster.getAirFrame() > 0) {
                                    monster.setAirFrame(0);
                                }
                                if (!(effect2 = SkillFactory.getSkill(36110005).getEffect(player.getSkillLevel(36110005))).makeChanceResult()) {
                                    break;
                                }
                                monster.setAirFrame(monster.getAirFrame() + 1);
                                if (monster.getAirFrame() >= 4) {
                                    monster.setAirFrame(0);
                                    monster.cancelSingleStatus(monster.getBuff(36110005), 36110005);
                                    map.broadcastMessage(CField.ignitionBomb(36110005, monster.getObjectId(), monster.getTruePosition()));
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Explosion, new MonsterStatusEffect(36110005, effect2.getDuration()), Long.valueOf(monster.getAirFrame())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind, new MonsterStatusEffect(36110005, effect2.getDuration()), (long) (-effect2.getX()) * (long) monster.getAirFrame()));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Eva, new MonsterStatusEffect(36110005, effect2.getDuration()), (long) (-effect2.getX()) * (long) monster.getAirFrame()));
                                break;
                            }
                            case 51120057:
                            case 51121007:
                            case 51121009: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind, new MonsterStatusEffect(attack.skill, attack.skill == 51120057 ? 10000 : (effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration())), Long.valueOf(-effect.getX())));
                                break;
                            }
                            case 51121052: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_DeadlyCharge, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getX())));
                                break;
                            }
                            case 61101101:
                            case 61111217: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 61111100:
                            case 61111113:
                            case 61111218: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getZ())));
                                break;
                            }
                            case 61111101:
                            case 61111219: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 64001000: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getS2()), Long.valueOf(-effect.getX())));
                                break;
                            }
                            case 64001001: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getX())));
                                break;
                            }
                            case 64001009:
                            case 64001010:
                            case 64001011: {
                                SecondaryStatEffect Eff = SkillFactory.getSkill(64120000).getEffect(player.getSkillLevel(attack.skill));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(Eff.getSourceId(), Eff.getSubTime() > 0 ? Eff.getSubTime() : Eff.getDuration()), -10L));
                                break;
                            }
                            case 64111003: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getW())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getW())));
                                break;
                            }
                            case 64121011: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Pad, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getU())));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Mad, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(-effect.getU())));
                                break;
                            }
                            case 65101100: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Explosion, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 164121005: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 164111008: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(-effect.getY())));
                                if (monster.getStats().isBoss()) {
                                    break;
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_RWLiftPress, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(2400500 + Randomizer.rand(0, 2))));
                                break;
                            }
                            case 65121002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_AdddamParty, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), Long.valueOf(effect.getY())));
                                break;
                            }
                            case 37121004: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 65121100: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()), 1L));
                                break;
                            }
                            case 100001283: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 151111002: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_TimeBomb, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(effect.getX())));
                                break;
                            }
                            case 164001001: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_RWChoppingHammer, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                break;
                            }
                            case 61121100:
                            case 400011079:
                            case 400011080:
                            case 400011081:
                            case 400011082: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(61121100, 10000), -50L));
                                break;
                            }
                            case 135001012: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieUNK, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getDuration()), -50L));
                                break;
                            }
                            case 164121044: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, 11000), 1L));
                                break;
                            }
                            case 400021001: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                if (player.getBuffedEffect(SecondaryStat.WizardIgnite) == null || !player.getBuffedEffect(SecondaryStat.WizardIgnite).makeChanceResult()) {
                                    break;
                                }
                                SkillFactory.getSkill(2100010).getEffect(player.getSkillLevel(2101010)).applyTo(player, monster.getTruePosition());
                                break;
                            }
                            case 24121010: {
                                SecondaryStatEffect ef = SkillFactory.getSkill(24121003).getEffect(GameConstants.getLinkedSkill(24121003));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, ef.getSubTime() > 0 ? ef.getSubTime() : ef.getDuration()), Long.valueOf(-ef.getY())));
                                break;
                            }
                            case 63121006:
                            case 63121007: {
                                SecondaryStatEffect effect2 = SkillFactory.getSkill(63121006).getEffect(player.getSkillLevel(63121006));
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(effect2.getSourceId(), effect2.getDOTTime()), (long) effect2.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                break;
                            }
                            case 1221052:
                            case 11121004:
                            case 11121013:
                            case 14121004:
                            case 31121006:
                            case 31221003:
                            case 36121053:
                            case 64121001:
                            case 151121040:
                            case 155121007:
                            case 155121306:
                            case 400001008:
                            case 400011121: {
                                int du = 0; // du , n?
                                if (attack.skill == 400011121) {
                                    monster.setCustomInfo(400011121, 1, 0);
                                    monster.setCustomInfo(400011122, 0, 10000);
                                }
                                int n = attack.skill == 400011015 ? effect.getW() * 1000 : (du = effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration());
                                if (attack.skill == 14121004) {
                                    if ((du += attack.targets * 1000) > effect.getS() * 1000) {
                                        du = effect.getS() * 1000;
                                    }
                                } else if (attack.skill == 64121001) {
                                    du = effect.getDuration();
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, du), 1L));
                                break;
                            }
                            case 400011015: {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(400011024, 10000), Long.valueOf(effect.getDuration())));
                                break;
                            }
                            default: {
                                SecondaryStatEffect effect2;
                                if (player.getSkillLevel(5110000) > 0) {
                                    SecondaryStatEffect stunMastery2 = SkillFactory.getSkill(5110000).getEffect(player.getSkillLevel(5110000));
                                    if (!stunMastery2.makeChanceResult()) {
                                        break;
                                    }
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Stun, new MonsterStatusEffect(5110000, 1000, 1L)));
                                    break;
                                }
                                if (player.getBuffedValue(1111003)) {
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_CriticalBind_N, new MonsterStatusEffect(1111003, 20000, 1)));
                                    break;
                                }
                                if (player.getBuffedValue(5311004)) {
                                    effect2 = SkillFactory.getSkill(5311004).getEffect(player.getSkillLevel(5311004));
                                    if (player.getSkillCustomValue0(5311004) == 2L) {
                                        if (!effect2.makeChanceResult()) {
                                            break;
                                        }
                                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed, new MonsterStatusEffect(effect2.getSourceId(), effect2.getV() * 1000, -30L)));
                                        break;
                                    }
                                    if (player.getSkillCustomValue0(5311004) == 4L) {
                                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(effect2.getSourceId(), effect2.getDOTTime(), (long) effect2.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L)));
                                        break;
                                    }
                                }
                                if (player.getBuffedEffect(SecondaryStat.SnowCharge) != null) {
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed, new MonsterStatusEffect(player.getBuffSource(SecondaryStat.SnowCharge), player.getBuffedEffect(SecondaryStat.SnowCharge).getY() * (monster.getStats().isBoss() ? 500 : 1000), -player.getBuffedEffect(SecondaryStat.SnowCharge).getQ() / (monster.getStats().isBoss() ? 2 : 1))));
                                    break;
                                }
                                if (player.getSkillLevel(25110210) > 0) {
                                    SecondaryStatEffect weakness = SkillFactory.getSkill(25110210).getEffect(player.getSkillLevel(25110210));
                                    if (!weakness.makeChanceResult()) {
                                        break;
                                    }
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Acc, new MonsterStatusEffect(25110210, weakness.getDuration(), -weakness.getX())));
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Eva, new MonsterStatusEffect(25110210, weakness.getDuration(), -40L)));
                                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_AdddamSkill2, new MonsterStatusEffect(25110210, weakness.getDuration(), weakness.getX())));
                                    break;
                                }
                                if (GameConstants.isPathFinder(player.getJob())) {
                                    effect2 = SkillFactory.getSkill(3320001).getEffect(player.getSkillLevel(3320001));
                                    if ((attack.skill == 3011004 || attack.skill == 3300002 || attack.skill == 3321003 || attack.skill == 3321005) && player.getBuffedValue(3320008) && effect2 != null && SkillFactory.getSkill(3320008).getEffect(player.getSkillLevel(3320008)).makeChanceResult()) {
                                        player.setSkillCustomInfo(3320008, player.getSkillCustomValue0(3320008) - 1L, 0L);
                                        if (player.getSkillCustomValue0(3320008) == 0L) {
                                            player.cancelEffectFromBuffStat(SecondaryStat.BonusAttack);
                                        } else {
                                            SkillFactory.getSkill(3320008).getEffect(player.getSkillLevel(3320008)).applyTo(player, (int) player.getBuffLimit(3320008));
                                        }
                                        if (monster.getBuff(MonsterStatus.MS_BossPropPlus) == null) {
                                            monster.removeCustomInfo(3320008);
                                        }
                                        if (monster.getCustomValue0(3320008) < 5L) {
                                            monster.addSkillCustomInfo(3320008, 1L);
                                        }
                                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_BossPropPlus, new MonsterStatusEffect(3320001, effect2.getDuration(), monster.getCustomValue0(3320008))));
                                    }
                                    switch (attack.skill) {
                                        case 3321007:
                                        case 3321016:
                                        case 3321018: {
                                            if (monster.getBuff(MonsterStatus.MS_BossPropPlus) == null) {
                                                monster.removeCustomInfo(3320008);
                                            }
                                            if (monster.getCustomValue0(3320008) < 5L) {
                                                monster.addSkillCustomInfo(3320008, 1L);
                                            }
                                            applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_BossPropPlus, new MonsterStatusEffect(3320001, effect2.getDuration(), monster.getCustomValue0(3320008))));
                                            break;
                                        }
                                        case 3321020: {
                                            monster.setCustomInfo(3320008, 5, 0);
                                            applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_BossPropPlus, new MonsterStatusEffect(3320001, effect2.getDuration(), monster.getCustomValue0(3320008))));
                                        }
                                    }
                                }
                                if (GameConstants.isKadena(player.getJob())) {
                                    SecondaryStatEffect eff2 = SkillFactory.getSkill(64120007).getEffect(player.getSkillLevel(64120007));
                                    if (player.getSkillLevel(64120007) > 0 && eff2 != null && eff2.makeChanceResult()) {
                                        MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(eff2.getSourceId(), eff2.getDOTTime(), (int) ((long) eff2.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                                        applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, monsterStatusEffect));
                                    }
                                }
                                int resist = SkillFactory.getSkill(101120110).getEffect(player.getSkillLevel(101120110)).getW() * 1000;
                                if (player.getSkillLevel(101120110) <= 0 || player.getGender() != 1) {
                                    break;
                                }
                                if (System.currentTimeMillis() - monster.getLastCriticalBindTime() > (long) resist) {
                                    if (monster.getStats().isBoss()) {
                                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(101120110, SkillFactory.getSkill(101120110).getEffect(player.getSkillLevel(101120110)).getDuration()), Long.valueOf(SkillFactory.getSkill(101120110).getEffect(player.getSkillLevel(101120110)).getDuration())));
                                    } else {
                                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_CriticalBind_N, new MonsterStatusEffect(101120110, SkillFactory.getSkill(101120110).getEffect(player.getSkillLevel(101120110)).getDuration()), 1L));
                                    }
                                    monster.setLastCriticalBindTime(System.currentTimeMillis());
                                    break;
                                }
                                if (player.getSkillCustomValue(101120110) != null || !monster.getStats().isBoss()) {
                                    break;
                                }
                                player.setSkillCustomInfo(101120110, 0L, 10000L);
                                player.getClient().getSession().writeAndFlush((Object) MobPacket.monsterResist(monster, player, (int) (((long) resist - (System.currentTimeMillis() - monster.getLastCriticalBindTime())) / 1000L), 101120110));
                                break;
                            }
                        }
                        int sk = 0;
                        boolean enhance = false;
                        final int[] array2;
                        final int[] venoms = array2 = new int[]{4110011, 4210010, 4320005};
                        for (final int venom : array2) {
                            if (player.getSkillLevel(venom) > 0) {
                                sk = venom;
                            }
                        }
                        if (sk > 0 && attack.skill != 4111003) {
                            final int[] array3;
                            final int[] fatals = array3 = new int[]{4120011, 4220011, 4340012};
                            for (final int fatal : array3) {
                                if (player.getSkillLevel(fatal) > 0) {
                                    enhance = true;
                                    sk = fatal;
                                }
                            }
                            final SecondaryStatEffect venomEffect = SkillFactory.getSkill(sk).getEffect(player.getSkillLevel(sk));
                            final MonsterStatusEffect monsterStatusEffect2 = new MonsterStatusEffect(venomEffect.getSourceId(), venomEffect.getDOTTime());
                            if (venomEffect.makeChanceResult()) {
                                if (monster.isBuffed(MonsterStatus.MS_Burned)) {
                                    if (monster.getBurnedBuffSize(sk) < (enhance ? venomEffect.getDotSuperpos() : 1)) {
                                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, monsterStatusEffect2, venomEffect.getDOT() * totDamageToOneMonster / attack.allDamage.size() / 10000L));
                                    }
                                } else {
                                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, monsterStatusEffect2, venomEffect.getDOT() * totDamageToOneMonster / attack.allDamage.size() / 10000L));
                                }
                            }
                        }
                        if (player.getSkillLevel(101110103) > 0) {
                            SecondaryStatEffect stunMastery3 = SkillFactory.getSkill(101110103).getEffect(player.getSkillLevel(101110103));
                            if (player.getGender() == 1 && Randomizer.isSuccess(((SecondaryStatEffect) ((Object) stunMastery3)).getProp())) {
                                if (monster.getBuff(101110103) == null && monster.getCustomValue0(101110103) > 0L) {
                                    monster.removeCustomInfo(101110103);
                                }
                                if (monster.getCustomValue0(101110103) < 5L) {
                                    monster.setCustomInfo(101110103, (int) monster.getCustomValue0(101110103) + 1, 0);
                                }
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_MultiPMDR, new MonsterStatusEffect(101110103, ((SecondaryStatEffect) ((Object) stunMastery3)).getDuration()), (long) ((SecondaryStatEffect) ((Object) stunMastery3)).getY() * monster.getCustomValue0(101110103)));
                            }
                        }
                        if (player.getSkillLevel(101120207) > 0) {
                            final SecondaryStatEffect stunMastery3 = SkillFactory.getSkill(101120207).getEffect(player.getSkillLevel(101120207));
                            if (player.getGender() == 0 && debinrear) {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(101120207, ((SecondaryStatEffect) ((Object) stunMastery3)).getDOTTime()), (long) ((SecondaryStatEffect) ((Object) stunMastery3)).getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                            }
                        }
                        if (player.getBuffedValue(SecondaryStat.BleedingToxin) != null) {
                            eff = player.getBuffedEffect(SecondaryStat.BleedingToxin);
                            if (eff != null && eff.makeChanceResult()) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(eff.getSourceId(), eff.getDOTTime());
                                statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, monsterStatusEffect, (long) eff.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                            }
                        } else if (player.getBuffedValue(SecondaryStat.ElementDarkness) != null) {
                            int[] passive;
                            eff = player.getBuffedEffect(SecondaryStat.ElementDarkness);
                            int suc = eff.getProp();
                            int dot = eff.getDOT();
                            for (int pas : passive = new int[]{14100026, 14110028, 0xD77447}) {
                                SecondaryStatEffect eff1;
                                if (player.getSkillLevel(pas) <= 0 || (eff1 = SkillFactory.getSkill(pas).getEffect(player.getSkillLevel(pas))) == null) {
                                    continue;
                                }
                                suc += eff1.getProp();
                                dot += eff1.getDOT();
                            }
                            if (eff != null && Randomizer.isSuccess(suc)) {
                                if (monster.getBuff(MonsterStatus.MS_ElementDarkness) == null) {
                                    monster.setCustomInfo(14001021, 0, 0);
                                }
                                if (monster.getCustomValue0(14001021) < 5L) {
                                    monster.addSkillCustomInfo(14001021, 1L);
                                }
                                if (player.getBuffedValue(14121052)) {
                                    monster.setCustomInfo(14001021, 5, 0);
                                }
                                applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(eff.getSourceId(), eff.getDOTTime(), (int) ((long) eff.getDOT() * totDamageToOneMonster * monster.getCustomValue0(14001021) / (long) attack.allDamage.size() / 1000L))));
                                applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_ElementDarkness, new MonsterStatusEffect(eff.getSourceId(), eff.getDOTTime(), monster.getCustomValue0(14001021))));
                                if (player.getSkillLevel(14120009) > 0 && player.getBuffedEffect(SecondaryStat.Protective) == null) {
                                    SkillFactory.getSkill(14120009).getEffect(player.getSkillLevel(14120009)).applyTo(player, false);
                                }
                            }
                        } else if (GameConstants.isCain(player.getJob()) && player.getSkillLevel(63110011) > 0 && monster != null) {
                            SecondaryStatEffect effect2 = SkillFactory.getSkill(63110011).getEffect(player.getSkillLevel(63110011));
                            if (SkillFactory.getSkill(63110011).getSkillList().contains(attack.skill) && monster.isAlive()) {
                                boolean cast = true;
                                if ((attack.skill == 63101104 || attack.skill == 63121141) && monster.getCustomValue(attack.skill) != null) {
                                    cast = false;
                                }
                                if (cast) {
                                    monster.setCustomInfo(63110011, (int) monster.getCustomValue0(63110011) + 1, effect2.getDuration());
                                    if (monster.getCustomValue0(63110011) >= (long) effect2.getX()) {
                                        monster.setCustomInfo(63110011, effect2.getX(), effect2.getDuration());
                                    }
                                    ArrayList<MapleMonster> moblist = new ArrayList<MapleMonster>();
                                    for (MapleMonster mob : player.getMap().getAllMonster()) {
                                        if (mob.getCustomValue0(63110011) <= 0L) {
                                            continue;
                                        }
                                        moblist.add(mob);
                                    }
                                    player.getClient().send(CField.getDeathBlessStack(player, moblist));
                                    if (attack.skill == 63101104 || attack.skill == 63121141) {
                                        monster.setCustomInfo(attack.skill, 0, 3000);
                                    }
                                }
                            } else if (SkillFactory.getSkill(63110011).getSkillList2().contains(attack.skill)) {
                                if (player.getBuffedValue(63111013)) {
                                    player.handlePossession(10);
                                }
                                if (monster.isAlive() && monster.getCustomValue0(63110011) > 0L) {
                                    monster.setCustomInfo(63110011, (int) monster.getCustomValue0(63110011) - 1, effect2.getDuration());
                                    ArrayList<MapleMonster> moblist = new ArrayList<MapleMonster>();
                                    moblist.add(monster);
                                    player.handlePossession(2);
                                    player.getClient().send(CField.getDeathBlessAttack(moblist, 63111012));
                                    moblist.clear();
                                    for (MapleMonster mob : player.getMap().getAllMonster()) {
                                        if (mob.getCustomValue0(63110011) <= 0L) {
                                            continue;
                                        }
                                        moblist.add(mob);
                                    }
                                    player.getClient().send(CField.getDeathBlessStack(player, moblist));
                                    SkillFactory.getSkill(63111013).getEffect(player.getSkillLevel(63110011)).applyTo(player);
                                }
                                if (player.getSkillLevel(63120001) > 0) {
                                    if (monster.getStats().isBoss() && monster.isAlive()) {
                                        player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) SkillFactory.getSkill(63111013).getEffect(player.getSkillLevel(63111013)).getX());
                                    } else if (!monster.isAlive()) {
                                        player.addSkillCustomInfo(63111013, 1L);
                                        if (player.getSkillCustomValue0(63111013) >= (long) SkillFactory.getSkill(63111013).getEffect(player.getSkillLevel(63111013)).getV()) {
                                            player.removeSkillCustomInfo(63111013);
                                            player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) SkillFactory.getSkill(63111013).getEffect(player.getSkillLevel(63111013)).getV());
                                        }
                                    }
                                }
                            }
                        }
                        if (player.getSkillLevel(400041000) > 0 && attack.skill != 400041000 && Randomizer.isSuccess(50)) {
                            SecondaryStatEffect stunMastery3 = SkillFactory.getSkill(400041000).getEffect(player.getSkillLevel(400041000));
                            applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(400040000, 8000, (int) ((long) ((SecondaryStatEffect) ((Object) stunMastery3)).getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L))));
                        }
                        for (Triple status : statusz) {
                            if (status.left == null || status.mid == null || ((MonsterStatusEffect) status.mid).shouldCancel(System.currentTimeMillis())) {
                                continue;
                            }
                            if (status.left == MonsterStatus.MS_Burned && (Long) status.right < 0L) {
                                status.right = (Long) status.right & 0xFFFFFFFFL;
                            }
                            if (((MonsterStatusEffect) status.mid).getSkill() == 51121009 ? Randomizer.isSuccess(effect.getY()) : (((MonsterStatusEffect) status.mid).getSkill() == 64121016 ? Randomizer.isSuccess(effect.getS2()) : effect.makeChanceResult())) {
                                ((MonsterStatusEffect) status.mid).setValue((Long) status.right);
                                applys.add(new Pair(status.left, status.mid));
                                continue;
                            }
                            if (attack.skill != 1211008) {
                                continue;
                            }
                            ((MonsterStatusEffect) status.mid).setValue((Long) status.right);
                            applys.add(new Pair(status.left, status.mid));
                        }
                        for (Triple status : statusz1) {
                            if (status.left == null || status.mid == null || ((MonsterStatusEffect) status.mid).shouldCancel(System.currentTimeMillis())) {
                                continue;
                            }
                            if (status.left == MonsterStatus.MS_Burned && (Long) status.right < 0L) {
                                status.right = (Long) status.right & 0xFFFFFFFFL;
                            }
                            if (((MonsterStatusEffect) status.mid).getSkill() == 51121009 ? Randomizer.isSuccess(effect.getY()) : (((MonsterStatusEffect) status.mid).getSkill() == 64121016 ? Randomizer.isSuccess(effect.getS2()) : effect.makeChanceResult())) {
                                ((MonsterStatusEffect) status.mid).setValue((Long) status.right);
                                applys1.add(new Pair(status.left, status.mid));
                                continue;
                            }
                            if (attack.skill != 1211008) {
                                continue;
                            }
                            ((MonsterStatusEffect) status.mid).setValue((Long) status.right);
                            applys1.add(new Pair(status.left, status.mid));
                        }
                        for (Triple status : statusz2) {
                            if (status.left == null || status.mid == null || ((MonsterStatusEffect) status.mid).shouldCancel(System.currentTimeMillis())) {
                                continue;
                            }
                            if (status.left == MonsterStatus.MS_Burned && (Long) status.right < 0L) {
                                status.right = (Long) status.right & 0xFFFFFFFFL;
                            }
                            if (((MonsterStatusEffect) status.mid).getSkill() == 51121009 ? Randomizer.isSuccess(effect.getY()) : (((MonsterStatusEffect) status.mid).getSkill() == 64121016 ? Randomizer.isSuccess(effect.getS2()) : effect.makeChanceResult())) {
                                ((MonsterStatusEffect) status.mid).setValue((Long) status.right);
                                applys2.add(new Pair(status.left, status.mid));
                                continue;
                            }
                            if (attack.skill != 1211008) {
                                continue;
                            }
                            ((MonsterStatusEffect) status.mid).setValue((Long) status.right);
                            applys2.add(new Pair(status.left, status.mid));
                        }
                        SecondaryStatEffect elementSoul = player.getBuffedEffect(11001022);
                        if (elementSoul != null && elementSoul.makeChanceResult()) {
                            applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Stun, new MonsterStatusEffect(elementSoul.getSourceId(), elementSoul.getSubTime(), elementSoul.getSubTime())));
                        }
                        if (attack.skill == 13111021 && attack.hits == 2) {
                            applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_MultiDamSkill, new MonsterStatusEffect(effect.getSourceId(), effect.getDuration(), effect.getX())));
                        }
                        if (monster != null && monster.isAlive()) {
                            if (!applys.isEmpty()) {
                                monster.applyStatus(player.getClient(), applys, effect);
                            }
                            if (!applys1.isEmpty()) {
                                monster.applyStatus(player.getClient(), applys1, effect);
                            }
                            if (!applys2.isEmpty()) {
                                monster.applyStatus(player.getClient(), applys2, effect);
                            }
                        }
                        if (!applys.isEmpty() && player.getSkillLevel(80002770) > 0) {
                            SkillFactory.getSkill(80002770).getEffect(player.getSkillLevel(80002770)).applyTo(player, false);
                        }
                    }
                    if (player.getBuffedValue(SecondaryStat.BMageDeath) != null && player.skillisCooling(32001114) && GameConstants.isBMDarkAtackSkill(attack.skill) && player.getBuffedValue(SecondaryStat.AttackCountX) != null) {
                        player.changeCooldown(32001114, -500);
                    }
                    if (player.getBuffedValue(SecondaryStat.BMageDeath) != null && (!monster.isAlive() || monster.getStats().isBoss()) && attack.skill != player.getBuffSource(SecondaryStat.BMageDeath)) {
                        byte count;
                        byte by = player.getBuffedValue(SecondaryStat.AttackCountX) != null ? (byte) 1 : (player.getLevel() >= 100 ? (byte) 6 : (count = player.getLevel() > 60 ? (byte) 8 : 10));
                        if (player.getDeath() < by) {
                            player.setDeath((byte) (player.getDeath() + 1));
                            if (player.getDeath() >= by) {
                                player.setSkillCustomInfo(32120019, 1L, 0L);
                            }
                            HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                            statups.put(SecondaryStat.BMageDeath, new Pair<Integer, Integer>(Integer.valueOf(player.getDeath()), 0));
                            player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(SecondaryStat.BMageDeath), player));
                        }
                    }
                    if (GameConstants.isHolyAttack(attack.skill) && monster.isBuffed(MonsterStatus.MS_ElementResetBySummon)) {
                        monster.cancelStatus(MonsterStatus.MS_ElementResetBySummon, monster.getBuff(MonsterStatus.MS_ElementResetBySummon));
                    }
                    if (monster.isBuffed(MonsterStatus.MS_JaguarBleeding) && attack.targets > 0 && monster.getBuff(MonsterStatus.MS_JaguarBleeding) != null && (attack.skill == 33001105 || attack.skill == 33001205 || attack.skill == 33101113 || attack.skill == 33101213 || attack.skill == 33111112 || attack.skill == 33111212 || attack.skill == 33121114 || attack.skill == 33121214)) {
                        ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                        mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 60, 0));
                        player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(33000036, mobList, false, monster.getAnotherByte(), new int[0]));
                    }
                    if (player.getSkillLevel(400011116) > 0 && SkillFactory.getSkill(400011116).getSkillList().contains(attack.skill)) {
                        SecondaryStatEffect effect2 = SkillFactory.getSkill(400011116).getEffect(player.getSkillLevel(400011116));
                        if (player.getBuffedValue(400011116)) {
                            afterimageshockattack = true;
                            int i = 0;
                            ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                            for (AttackPair a : attack.allDamage) {
                                mobList.add(new Triple<Integer, Integer, Integer>(a.objectId, 120 + 70 * i, 0));
                                ++i;
                            }
                            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011133, mobList, false, 0, new int[0]));
                        } else if (!player.getBuffedValue(400011116) && attack.targets > 0 && player.skillisCooling(400011116) && !player.skillisCooling(400011117)) {
                            ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                            int i = 0;
                            for (MapleMapObject o : player.getMap().getMapObjectsInRange(player.getPosition(), 100000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}))) {
                                MapleMonster mon = (MapleMonster) o;
                                mobList.clear();
                                mobList.add(new Triple<Integer, Integer, Integer>(mon.getObjectId(), 120 + 70 * i, 0));
                                if (++i == 7) {
                                    break;
                                }
                                player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011117, mobList, false, mon.getObjectId(), new int[0]));
                            }
                            player.addCooldown(400011117, System.currentTimeMillis(), effect2.getX() * 1000);
                            player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400011117, effect2.getX() * 1000));
                        }
                    }
                    if (player.getBuffedValue(400031000)) {
                        player.getMap().broadcastMessage(CField.ForceAtomAttack(1, player.getId(), monster.getObjectId()));
                    }
                    if (attack.skill != 400041035 && attack.skill != 400041036 && player.getBuffedValue(400041035) && System.currentTimeMillis() - player.lastChainArtsFuryTime >= 1000L) {
                        player.lastChainArtsFuryTime = System.currentTimeMillis();
                        player.getMap().broadcastMessage(CField.ChainArtsFury(monster.getTruePosition()));
                    }
                    if (player.getBuffedValue(400011016)) {
                        SecondaryStatEffect installMaha = player.getBuffedEffect(400011016);
                        if (System.currentTimeMillis() - player.lastInstallMahaTime >= (long) (installMaha.getX() * 1000)) {
                            player.lastInstallMahaTime = System.currentTimeMillis();
                            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011020, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
                        }
                    }
                    if (attack.skill == 31211001 && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
                        player.addHP(player.getStat().getCurrentMaxHp() * (long) effect.getY() / 100L);
                    }
                    if (totDamage > 0L && attack.skill == 4221016 && player.getSkillLevel(400041025) > 0) {
                        if (player.shadowerDebuffOid == 0) {
                            player.shadowerDebuff = Math.min(3, player.shadowerDebuff + 1);
                            player.shadowerDebuffOid = monster.getObjectId();
                        } else if (player.shadowerDebuffOid != monster.getObjectId()) {
                            player.shadowerDebuff = 1;
                            player.shadowerDebuffOid = monster.getObjectId();
                        } else {
                            player.shadowerDebuff = Math.min(3, player.shadowerDebuff + 1);
                        }
                        effect.applyTo(player);
                    }
                    if (attack.skill == 400041026) {
                        player.shadowerDebuff = 0;
                        player.shadowerDebuffOid = 0;
                        SkillFactory.getSkill(400041026).getEffect(player.getSkillLevel(400041025)).applyTo(player);
                        player.cancelEffectFromBuffStat(SecondaryStat.ShadowerDebuff, 4221016);
                    }
                    if (attack.skill == 5221015) {
                        player.guidedBullet = monster.getObjectId();
                        if (player.getKeyValue(1544, String.valueOf(5221029)) == 1) {
                            MapleSummon summon4 = new MapleSummon(player, 5221029, monster.getPosition(), SummonMovementType.STATIONARY, (byte) 0, 60000);
                            player.getMap().spawnSummon(summon4, 60000);
                            player.addSummon(summon4);
                        }
                    }
                    if (attack.skill == 151121001) {
                        player.graveObjectId = monster.getObjectId();
                    }
                    if (player.getBuffedValue(400031002) && attack.skill != 400030002 && (player.lastArrowRain == 0L || player.lastArrowRain < System.currentTimeMillis())) {
                        SecondaryStatEffect arrowRain = player.getBuffedEffect(400031002);
                        SkillFactory.getSkill(400030002).getEffect(arrowRain.getLevel()).applyTo(player, monster.getTruePosition(), (int) (arrowRain.getT() * 1000.0));
                        player.lastArrowRain = System.currentTimeMillis() + (long) (arrowRain.getX() * 1000);
                    }
                    if (player.getBuffedValue(400041008) && (GameConstants.isDarkAtackSkill(attack.skill) || attack.summonattack != 0)) {
                        SecondaryStatEffect a = SkillFactory.getSkill(400040008).getEffect(player.getSkillLevel(400040008));
                        MapleMist mist = new MapleMist(a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, 2000, (byte) (player.isFacingLeft() ? 1 : 0));
                        mist.setPosition(monster.getPosition());
                        player.getMap().spawnMist(mist, false);
                        int mists = 0;
                        for (MapleMist mist2 : player.getMap().getAllMistsThreadsafe()) {
                            if (mist2.getOwnerId() == player.getId() && mist.getSourceSkill().getId() == 400040008 && ++mists == 9) {
                                break;
                            }
                        }
                        if (player.getSkillCustomValue(400041019) == null && mists >= 9) {
                            player.getMap().broadcastMessage(CField.NightWalkerShadowSpearBig(monster.getTruePosition().x, monster.getTruePosition().y));
                            player.setSkillCustomInfo(400041019, 0L, 3000L);
                        }
                    }
                    if (player.getSkillLevel(32101009) > 0 && !monster.isAlive() && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
                        player.addHP(player.getStat().getCurrentMaxHp() * (long) SkillFactory.getSkill(32101009).getEffect(player.getSkillLevel(32101009)).getKillRecoveryR() / 100L);
                    }
                    if (attack.skill == 400041037) {
                        SecondaryStatEffect shadowBite = SkillFactory.getSkill(400041037).getEffect(player.getSkillLevel(400041037));
                        if (player.getBuffedValue(SecondaryStat.ShadowBatt) != null) {
                            MapleAtom atom = new MapleAtom(false, player.getId(), 15, true, 14000028, monster.getTruePosition().x, monster.getTruePosition().y);
                            atom.setDwFirstTargetId(monster.getObjectId());
                            ForceAtom forceAtom = new ForceAtom(player.getSkillLevel(14120008) > 0 ? 2 : 1, 1, 5, Randomizer.rand(45, 90), 500);
                            atom.addForceAtom(forceAtom);
                            player.getMap().spawnMapleAtom(atom);
                        }
                        if (!monster.isAlive() || monster.getStats().isBoss()) {
                            player.shadowBite = Math.min(shadowBite.getQ(), player.shadowBite + (monster.getStats().isBoss() ? shadowBite.getW() : shadowBite.getY()));
                            MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 42, true, 400041037, monster.getTruePosition().x, monster.getTruePosition().y);
                            atom.setDwUserOwner(player.getId());
                            atom.setDwFirstTargetId(0);
                            atom.addForceAtom(new ForceAtom(2, 42, 6, 33, (short) Randomizer.rand(2500, 3000)));
                            player.getMap().spawnMapleAtom(atom);
                            if (player.shadowBite > 0 && !player.getBuffedValue(400041037)) {
                                SkillFactory.getSkill(400041037).getEffect(attack.skilllevel).applyTo(player, false);
                            }
                        }
                    }
                    if (attack.skill == 155100009 && player.getSkillLevel(155111207) > 0 && Randomizer.isSuccess((mark = SkillFactory.getSkill(155111207).getEffect(player.getSkillLevel(155111207))).getS()) && player.getMwSize(155111207) < (player.getKeyValue(1544, "155111207") == 1L ? mark.getY() : mark.getZ())) {
                        int x = monster.getPosition().x + Randomizer.rand(-100, 100);
                        int y = monster.getPosition().y + Randomizer.rand(-70, 30);
                        MapleMagicWreck mw = new MapleMagicWreck(player, mark.getSourceId(), new Point(x, y), mark.getQ() * 1000);
                        player.getMap().spawnMagicWreck(mw);
                    }
                    if (GameConstants.isIllium(player.getJob()) && monster.getBuff(MonsterStatus.MS_CurseMark) != null && attack.skill != 152001002 && attack.skill != 152120003 && attack.skill != 152120002 && attack.skill != 152120016) {
                        int skillid = player.getSkillLevel(152120013) > 0 ? 152120013 : (player.getSkillLevel(152110010) > 0 ? 152110010 : 152100012);
                        ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                        mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 120, 0));
                        player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(skillid, mobList, false, 0, new int[0]));
                    }
                    if (player.getBuffedValue(400051007) && attack.skill != 400051007 && attack.skill != 400051013 && System.currentTimeMillis() - player.lastThunderTime >= (long) (player.getBuffedEffect(400051007).getY() * 1000)) {
                        player.lastThunderTime = System.currentTimeMillis();
                        player.getClient().getSession().writeAndFlush((Object) CField.lightningUnionSubAttack(attack.skill, 400051007, player.getSkillLevel(400051007)));
                    }
                    if (player.getBuffedValue(80002890) && attack.skill != 80002890 && attack.skill != 80002890 && System.currentTimeMillis() - player.lastThunderTime >= (long) player.getBuffedEffect(80002890).getCooldown(player)) {
                        player.lastThunderTime = System.currentTimeMillis();
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(80002890, Arrays.asList(new RangeAttack(80002890, attack.position, 0, 0, 1))));
                    }
                    if (player.getBuffedValue(4221054)) {
                        if (player.getFlip() < 5) {
                            player.setFlip((byte) (player.getFlip() + 1));
                            SkillFactory.getSkill(4221054).getEffect(player.getSkillLevel(4221054)).applyTo(player, false, false);
                        }
                    }
                    if (attack.skill == 5311002) {
                        player.cancelEffectFromBuffStat(SecondaryStat.KeyDownTimeIgnore, 5310008);
                    } else if (player.getSkillLevel(5311002) > 0 && !player.getBuffedValue(5310008) && attack.skill != 400051008) {
                        SkillFactory.getSkill(5310008).getEffect(player.getSkillLevel(5311002)).applyTo(player, false);
                    }
                    if (player.getBuffedValue(SecondaryStat.PinPointRocket) != null && attack.skill != 36001005 && System.currentTimeMillis() - player.lastPinPointRocketTime >= (long) (player.getBuffedEffect(SecondaryStat.PinPointRocket).getX() * 1000)) {
                        player.lastPinPointRocketTime = System.currentTimeMillis();
                        MapleAtom atom = new MapleAtom(false, player.getId(), 6, true, 36001005, player.getTruePosition().x, player.getTruePosition().y);
                        ArrayList<Integer> monsters = new ArrayList<Integer>();
                        for (int i2 = 0; i2 < player.getBuffedEffect(SecondaryStat.PinPointRocket).getBulletCount(); ++i2) {
                            monsters.add(0);
                            atom.addForceAtom(new ForceAtom(0, 19, Randomizer.rand(20, 40), Randomizer.rand(40, 200), 0));
                        }
                        atom.setDwTargets(monsters);
                        player.getMap().spawnMapleAtom(atom);
                    }
                    if (attack.skill != 400011060 || monster == null || player.getBuffedEffect(SecondaryStat.WillofSwordStrike) != null) {
                        continue;
                    }
                    effect.applyTo(player, false, monster.getTruePosition());
                }
                if (attack.skill == 5121013 || attack.skill == 5221013 || attack.skill == 400051040) {
                    if (player.getSkillLevel(5121013) > 0 && attack.skill == 5121013 && player.getSkillLevel(400051040) > 0 && player.getCooldownLimit(400051040) <= 8000L) {
                        player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400051040, 8000));
                        player.addCooldown(400051040, System.currentTimeMillis(), 8000L);
                    } else if (player.getSkillLevel(5221013) > 0 && attack.skill == 5221013) {
                        if (player.getSkillLevel(400051040) > 0 && player.getCooldownLimit(400051040) <= 8000L) {
                            player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400051040, 8000));
                            player.addCooldown(400051040, System.currentTimeMillis(), 8000L);
                        }
                        int[] array2;
                        int[] reduceSkills = array2 = new int[]{5210015, 5210016, 5210017, 5210018, 5220014, 5211007, 5221022, 5220023, 5220024, 5220025};
                        for (final int reduceSkill : array2) {
                            if (!player.skillisCooling((int) reduceSkill)) {
                                continue;
                            }
                            player.changeCooldown((int) reduceSkill, (int) (-(player.getCooldownLimit((int) reduceSkill) / 2L)));
                        }
                    } else if (player.getSkillLevel(400051040) > 0 && attack.skill == 400051040 && player.getSkillLevel(5121013) > 0 && player.getCooldownLimit(5121013) <= 8000L) {
                        player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(5121013, 8000));
                        player.addCooldown(5121013, System.currentTimeMillis(), 8000L);
                    } else if (player.getSkillLevel(400051040) > 0 && attack.skill == 400051040 && player.getSkillLevel(5221013) > 0 && player.getCooldownLimit(5221013) <= 8000L) {
                        player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(5221013, 8000));
                        player.addCooldown(5221013, System.currentTimeMillis(), 8000L);
                    }
                }
                if (!finalMobList.isEmpty()) {
                    player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(33000036, finalMobList, false, 0, new int[0]));
                }
                if (totDamage > 0L) {
                    Integer[] throw_skillList;
                    if (player.getMapId() == 993000500) {
                        player.setFWolfDamage(player.getFWolfDamage() + totDamage);
                        player.setFWolfAttackCount(player.getFWolfAttackCount() + 1);
                        player.dropMessageGM(5, "total damage : " + player.getFWolfDamage());
                    }
                    if (!GameConstants.isKaiser(player.getJob()) && monster != null) {
                        DamageParse.WFinalAttackRequest(player, attack.skill, monster);
                    }
                    if (attack.skill == 5321001) {
                        if (player.skillisCooling(5311004)) {
                            player.changeCooldown(5311004, (int) (-(player.getCooldownLimit(5311004) / 2L)));
                        }
                        if (player.skillisCooling(5311005)) {
                            player.changeCooldown(5311005, (int) (-(player.getCooldownLimit(5311005) / 2L)));
                        }
                        if (player.skillisCooling(5320007)) {
                            player.changeCooldown(5320007, (int) (-(player.getCooldownLimit(5320007) / 2L)));
                        }
                    }
                    if (GameConstants.isArcher(player.getJob()) && SkillFactory.getSkill(400031021).getSkillList().contains(attack.skill) && player.getSkillLevel(400031020) > 0 && player.getCooldownLimit(400031020) > 0L) {
                        player.setVerseOfRelicsCount(player.getVerseOfRelicsCount() + 1);
                        SecondaryStatEffect att = SkillFactory.getSkill(400031021).getEffect(player.getSkillLevel(400031020));
                        if (System.currentTimeMillis() - player.lastVerseOfRelicsTime >= (long) (att.getSubTime() / 100) && player.getVerseOfRelicsCount() >= 10) {
                            player.lastVerseOfRelicsTime = System.currentTimeMillis();
                            player.setVerseOfRelicsCount(0);
                            att.applyTo(player, false, 1000);
                        }
                    }
                    if (attack.skill == 400011056 && player.getBuffedValue(SecondaryStat.Ellision) != null && player.getSkillCustomValue(400011065) == null) {
                        MapleSummon ellision = player.getSummon(400011065);
                        if (ellision == null) {
                            boolean rltype = (attack.facingleft >>> 4 & 0xF) == 8;
                            MapleSummon summon4 = new MapleSummon(player, 400011065, attack.position, SummonMovementType.STATIONARY, (byte) (rltype ? 1 : 0), effect.getDuration());
                            player.getMap().spawnSummon(summon4, effect.getDuration());
                            player.addSummon(summon4);
                        } else {
                            ellision.setEnergy(ellision.getEnergy() + 1);
                            if (ellision.getEnergy() % 5 == 0) {
                                ellision.setEnergy(0);
                                player.getMap().broadcastMessage(CField.SummonPacket.transformSummon(ellision, 1));
                            }
                        }
                    }
                    if (GameConstants.isLuminous(player.getJob())) {
                        if ((player.getBuffedValue(20040216) || player.getBuffedValue(20040219) || player.getBuffedValue(20040220)) && (GameConstants.isLightSkills(attack.skill) || (player.getBuffedValue(20040219) || player.getBuffedValue(20040220)) && (attack.skill == 27121303 || attack.skill == 27111303))) {
                            player.addHP(player.getStat().getMaxHp() / 100L);
                        }
                        if (!(player.getBuffedValue(20040216) || player.getBuffedValue(20040217) || player.getBuffedValue(20040219) || player.getBuffedValue(20040220))) {
                            if (GameConstants.isLightSkills(attack.skill)) {
                                player.setLuminusMorphUse(1);
                                SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
                                player.setLuminusMorph(false);
                            } else if (GameConstants.isDarkSkills(attack.skill)) {
                                player.setLuminusMorphUse(9999);
                                SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
                                player.setLuminusMorph(true);
                            }
                            player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
                        } else if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220)) {
                            if (player.getLuminusMorph()) {
                                if (GameConstants.isLightSkills(attack.skill)) {
                                    if (player.getLuminusMorphUse() - GameConstants.isLightSkillsGaugeCheck(attack.skill) <= 0) {
                                        if (player.getSkillLevel(20040219) > 0) {
                                            player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                            SkillFactory.getSkill(20040219).getEffect(1).applyTo(player, false);
                                        } else {
                                            player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                            player.setLuminusMorph(false);
                                            SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
                                        }
                                    } else {
                                        player.setLuminusMorphUse(player.getLuminusMorphUse() - GameConstants.isLightSkillsGaugeCheck(attack.skill));
                                    }
                                    if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220) && player.getLuminusMorph()) {
                                        player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                        SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
                                    }
                                }
                            } else if (GameConstants.isDarkSkills(attack.skill)) {
                                if (player.getLuminusMorphUse() + GameConstants.isDarkSkillsGaugeCheck(player, attack.skill) >= 10000) {
                                    if (player.getSkillLevel(20040219) > 0) {
                                        player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                        SkillFactory.getSkill(20040220).getEffect(1).applyTo(player, false);
                                    } else {
                                        player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                        player.setLuminusMorph(true);
                                        SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
                                    }
                                } else {
                                    player.setLuminusMorphUse(player.getLuminusMorphUse() + GameConstants.isDarkSkillsGaugeCheck(player, attack.skill));
                                }
                                if (!(player.getBuffedValue(20040219) || player.getBuffedValue(20040220) || player.getLuminusMorph())) {
                                    player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                    SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
                                }
                            }
                            player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
                        }
                    }
                    if (attack.skill == 27121303 && player.getSkillLevel(400021071) > 0) {
                        boolean give = false;
                        if (player.getPerfusion() < SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).getX() - 1) {
                            give = true;
                        } else if (player.getPerfusion() >= SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).getX() - 1 && player.skillisCooling(400021071)) {
                            give = true;
                        }
                        if (give) {
                            SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).applyTo(player, false);
                        }
                    }
                    if (player.getBuffedValue(32101009) && player.getSkillCustomValue(32111119) == null && player.getId() == player.getBuffedOwner(32101009)) {
                        player.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
                        player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null, null));
                        player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null, null), false);
                        player.setSkillCustomInfo(32111119, 0L, 5000L);
                        if (player.getParty() != null) {
                            for (MaplePartyCharacter pc : player.getParty().getMembers()) {
                                MapleCharacter chr;
                                if (pc.getId() == player.getId() || !pc.isOnline() || (chr = player.getClient().getChannelServer().getPlayerStorage().getCharacterById(pc.getId())) == null || !chr.getBuffedValue(32101009) || chr.getId() == player.getId()) {
                                    continue;
                                }
                                chr.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
                                if (chr.getDisease(SecondaryStat.GiveMeHeal) != null) {
                                    chr.cancelDisease(SecondaryStat.GiveMeHeal);
                                }
                                chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
                                chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null), false);
                            }
                        }
                    }
                    if (player.getBuffedValue(31121002) && System.currentTimeMillis() - player.lastVamTime >= (long) player.getBuffedEffect(31121002).getY() && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
                        player.lastVamTime = System.currentTimeMillis();
                        player.addHP(Math.min((long) player.getBuffedEffect(31121002).getW(), totDamage * (long) player.getBuffedEffect(31121002).getX() / 100L));
                        if (player.getParty() != null) {
                            for (MaplePartyCharacter pc : player.getParty().getMembers()) {
                                MapleCharacter chr;
                                if (pc.getId() == player.getId() || !pc.isOnline() || (chr = player.getClient().getChannelServer().getPlayerStorage().getCharacterById(pc.getId())) == null || !chr.isAlive() || chr.getBuffedEffect(SecondaryStat.DebuffIncHp) != null) {
                                    continue;
                                }
                                chr.addHP(totDamage * (long) player.getBuffedEffect(31121002).getX() / 100L);
                            }
                        }
                    }
                    if (attack.skill == 400011131) {
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400011131, Arrays.asList(new RangeAttack(400011132, attack.attackPosition2, 0, 0, 1))));
                    }
                    if (monster != null && player.getBuffedValue(65121011) && attack.skill != 65120011 && attack.skill != 65111007 && attack.skill != 60011216 && attack.skill < 400000000) {
                        int prop = player.getBuffedEffect(65121011).getProp();
                        if (attack.skill == 65121100) {
                            prop += player.getBuffedEffect(65121011).getZ();
                        }
                        if (player.getBuffedEffect(SecondaryStat.SoulExalt) != null) {
                            prop += player.getBuffedValue(SecondaryStat.SoulExalt).intValue();
                        }
                        if (Randomizer.isSuccess(prop)) {
                            for (int i = 0; i < 2; ++i) {
                                MapleAtom atom = new MapleAtom(false, player.getId(), 25, true, 65120011, player.getTruePosition().x, player.getTruePosition().y);
                                ArrayList<Integer> monsters = new ArrayList<Integer>();
                                monsters.add(0);
                                atom.addForceAtom(new ForceAtom(1, Randomizer.rand(15, 16), Randomizer.rand(27, 34), Randomizer.rand(31, 36), 0));
                                atom.setDwFirstTargetId(monster.getObjectId());
                                atom.setDwTargets(monsters);
                                player.getMap().spawnMapleAtom(atom);
                            }
                        }
                    }
                    if (Arrays.asList(throw_skillList = new Integer[]{4121013, 4121017, 4121052, 4001344, 4101008, 4111010, 4111015}).contains(attack.skill)) {
                        if (player.getBuffedEffect(SecondaryStat.ThrowBlasting) != null && attack.skill != 400041061 && attack.skill != 400041079) {
                            SecondaryStatEffect throw_eff = player.getBuffedEffect(SecondaryStat.ThrowBlasting);
                            int consume = Randomizer.rand(throw_eff.getS(), throw_eff.getW());
                            player.throwBlasting -= consume;
                            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400041061, Arrays.asList(new RangeAttack(400041079, attack.attackPosition2, 0, 0, 3))));
                            if (player.throwBlasting <= 0) {
                                player.cancelEffectFromBuffStat(SecondaryStat.ThrowBlasting);
                            } else {
                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                statups.put(SecondaryStat.ThrowBlasting, new Pair<Integer, Integer>(player.throwBlasting, (int) player.getBuffLimit(player.getBuffSource(SecondaryStat.ThrowBlasting))));
                                player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(SecondaryStat.ThrowBlasting), player));
                            }
                        } else if (player.getSkillLevel(400041061) > 0 && attack.skill != 400041061 && attack.skill != 400041062) {
                            SecondaryStatEffect throw_eff = SkillFactory.getSkill(400041061).getEffect(player.getSkillLevel(400041061));
                            if (System.currentTimeMillis() - player.lastThrowBlastingTime >= (long) (throw_eff.getU() * 1000)) {
                                player.lastThrowBlastingTime = System.currentTimeMillis();
                                player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400041061, Arrays.asList(new RangeAttack(400041079, attack.attackPosition2, 0, 0, 3))));
                            }
                        }
                    }
                }
                if (player.getBuffSource(SecondaryStat.DrainHp) == 20031210) {
                    player.addHP(totDamage * (long) player.getBuffedValue(SecondaryStat.DrainHp).intValue() / 100L);
                }
                if (player.getSkillLevel(1200014) > 0) {
                    player.elementalChargeHandler(attack.skill, 1);
                }
                if (attack.skill == 155121306) {
                    if (!player.getBuffedValue(155000007)) {
                        SkillFactory.getSkill(155000007).getEffect(1).applyTo(player);
                    }
                    SkillFactory.getSkill(155121006).getEffect(attack.skilllevel).applyTo(player, monster.getPosition(), false);
                }
                if (attack.targets > 0 && attack.skill != 1311020 && player.getBuffedEffect(SecondaryStat.TryflingWarm) != null && !GameConstants.isTryFling(attack.skill) && attack.skill != 400031031 && attack.skill != 400031001 && attack.skill != 13111020 && attack.skill != 13121054 && attack.skill != 13101022 && attack.skill != 13110022 && attack.skill != 13120003 && attack.skill != 13111020 && attack.skill != 400001018 && player.getJob() >= 1310 && player.getJob() <= 1312 && attack.targets > 0) {
                    int skillid = 0;
                    if (player.getSkillLevel(SkillFactory.getSkill(13120003)) > 0) {
                        skillid = 13120003;
                    } else if (player.getSkillLevel(SkillFactory.getSkill(13110022)) > 0) {
                        skillid = 13110022;
                    } else if (player.getSkillLevel(SkillFactory.getSkill(13101022)) > 0) {
                        skillid = 13100022;
                    }
                    if (skillid != 0) {
                        Skill trskill = SkillFactory.getSkill(skillid);
                        if (Randomizer.rand(1, 100) <= (skillid == 13100022 ? 5 : (skillid == 13110022 ? 10 : 20))) {
                            int n = skillid == 13120003 ? 13120010 : (skillid = skillid == 13110022 ? 13110027 : 13100027);
                            if (player.getSkillLevel(skillid) <= 0) {
                                player.changeSkillLevel(SkillFactory.getSkill(skillid), (byte) player.getSkillLevel(trskill), (byte) player.getSkillLevel(trskill));
                            }
                        }
                        List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 500000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
                        SecondaryStatEffect eff = trskill.getEffect(player.getSkillLevel(skillid));
                        int maxcount = eff.getX();
                        if (objs.size() > 0) {
                            int trychance = trskill.getEffect(player.getSkillLevel(trskill)).getProp();
                            if (player.getSkillLevel(13120044) > 0) {
                                trychance += SkillFactory.getSkill(13120044).getEffect(1).getProp();
                            }
                            if (attack.skill == 400031004 || attack.skill == 400031003) {
                                trychance /= 2;
                            }
                            if (Randomizer.isSuccess(trychance)) {
                                MapleAtom atom = new MapleAtom(false, player.getId(), 7, true, skillid, player.getTruePosition().x, player.getTruePosition().y);
                                ArrayList<Integer> monsters = new ArrayList<Integer>();
                                for (int i = 0; i < Randomizer.rand(1, maxcount); ++i) {
                                    boolean upgrade = Randomizer.isSuccess(eff.getSubprop());
                                    monsters.add(objs.get(Randomizer.nextInt(objs.size())).getObjectId());
                                    atom.addForceAtom(new ForceAtom(upgrade ? 3 : 1, Randomizer.rand(41, 49), Randomizer.rand(4, 8), Randomizer.nextBoolean() ? Randomizer.rand(171, 174) : Randomizer.rand(6, 9), (short) Randomizer.rand(42, 47)));
                                }
                                atom.setDwTargets(monsters);
                                player.getMap().spawnMapleAtom(atom);
                            }
                        }
                    }
                }
                if (attack.skill == 400031032 || attack.skill == 400051042) {
                    PlayerHandler.Vmatrixstackbuff(player.getClient(), true, null);
                }
                if (attack.skill == 400051075) {
                    player.setSkillCustomInfo(400051074, player.getSkillCustomValue0(400051074) - 1L, 0L);
                    if (attack.targets <= 0) {
                        int counta = 0;
                        for (MapleMist m : player.getMap().getAllMistsThreadsafe()) {
                            if (m.getOwnerId() != player.getId()) {
                                continue;
                            }
                            ++counta;
                        }
                        if (counta < 2) {
                            final SecondaryStatEffect a5 = SkillFactory.getSkill(400051076).getEffect(player.getSkillLevel(400051074));
                            final Rectangle bounds = a5.calculateBoundingBox(new Point(attack.plusPosition2.x, attack.plusPosition2.y), player.isFacingLeft());
                            final MapleMist mist3 = new MapleMist(bounds, player, a5, 20000, (byte) 0);
                            mist3.setPosition(new Point(attack.plusPosition2.x, attack.plusPosition2.y));
                            mist3.setDelay(0);
                            player.getMap().spawnMist(mist3, false);
                        }
                    }
                    if (player.getSkillCustomValue0(400051074) <= 0L) {
                        player.getMap().removeMist(400051076);
                        player.getMap().removeMist(400051076);
                    }
                    player.getClient().send(CField.fullMaker((int) player.getSkillCustomValue0(400051074), player.getSkillCustomValue0(400051074) <= 0L ? 0 : 60000));
                }
                if (totDamage > 0L && player.getSkillLevel(3210013) > 0) {
                    SecondaryStatEffect bar = SkillFactory.getSkill(3210013).getEffect(player.getSkillLevel(3210013));
                    player.setBarrier((int) Math.min(player.getStat().getCurrentMaxHp() * (long) bar.getZ() / 100L, totDamage * (long) bar.getY() / 100L));
                    bar.applyTo(player, false);
                }
                if (totDamage > 0L && player.getBuffedValue(65101002)) {
                    SecondaryStatEffect bar = SkillFactory.getSkill(65101002).getEffect(player.getSkillLevel(65101002));
                    player.setBarrier((int) Math.min(player.getStat().getCurrentMaxHp(), totDamage * (long) bar.getY() / 100L));
                    player.setBarrier((int) Math.min(player.getStat().getCurrentMaxHp(), 99999L));
                    bar.applyTo(player, false, (int) player.getBuffLimit(65101002));
                }
                if (totDamage > 0L && player.getSkillLevel(4221013) > 0) {
                    SecondaryStatEffect instict = SkillFactory.getSkill(4221013).getEffect(player.getSkillLevel(4221013));
                    if (player.killingpoint < 3) {
                        ++player.killingpoint;
                        instict.applyTo(player, false, 0);
                    } else if (player.killingpoint == 3 && attack.skill == 4221016) {
                        player.killingpoint = 0;
                        instict.applyTo(player, false, 0);
                        if (player.getCooldownLimit(400041039) > 0L) {
                            player.changeCooldown(400041039, -1500);
                        }
                    }
                }
                if (GameConstants.isDemonSlash(attack.skill) && player.getSkillLevel(0x1DADAAD) > 0 && !player.getBuffedValue(0x1DADAAD)) {
                    SkillFactory.getSkill(0x1DADAAD).getEffect(1).applyTo(player, false);
                }
                if (player.getBuffedValue(400031044) && player.getSkillCustomValue(400031044) == null && monster != null) {
                    player.setGraveTarget(player.getObjectId());
                    for (int i = 0; i < Randomizer.rand(1, 4); ++i) {
                        player.createSecondAtom(SkillFactory.getSkill(400031045).getSecondAtoms(), monster.getPosition());
                    }
                    player.setSkillCustomInfo(400031044, 0L, (int) (SkillFactory.getSkill(400031044).getEffect(player.getSkillLevel(400031044)).getT() * 1000.0));
                }
                if (GameConstants.isAran(player.getJob()) && attack.skill != 400011122 && player.getBuffedValue(400011121) && System.currentTimeMillis() - player.lastBlizzardTempestTime >= 500L) {
                    player.lastBlizzardTempestTime = System.currentTimeMillis();
                    player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011122, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
                    ArrayList<Pair<Integer, Integer>> lists = new ArrayList<Pair<Integer, Integer>>() {
                        {
                            for (AttackPair pair : attack.allDamage) {
                                MapleMonster mob = player.getMap().getMonsterByOid(pair.objectId);
                                if (mob == null) {
                                    continue;
                                }
                                if (mob.blizzardTempest < 6) {
                                    ++mob.blizzardTempest;
                                }
                                this.add(new Pair<Integer, Integer>(pair.objectId, mob.blizzardTempest));
                            }
                        }
                    };
                    player.getClient().getSession().writeAndFlush((Object) CWvsContext.blizzardTempest((List<Pair<Integer, Integer>>) lists));
                }
                if (attack.skill == 4341002) {
                    effect.applyTo(player, false);
                }
                if (attack.skill == 4341011 && player.getCooldownLimit(4341002) > 0L) {
                    SecondaryStatEffect suddenRade = SkillFactory.getSkill(4341011).getEffect(attack.skilllevel);
                    player.changeCooldown(4341002, (int) (-player.getCooldownLimit(4341002) * (long) suddenRade.getX() / 100L));
                }
                if (player.getBuffedValue(155101008) && attack.skill != 155121006 && attack.skill != 155121007 && attack.skill != 155100009 && attack.skill != 155001000 && attack.skill != 155121004 && attack.skill != 400051035 && attack.skill != 400051334 && player.getBuffedEffect(SecondaryStat.SpectorTransForm) != null) {
                    int i;
                    SecondaryStatEffect eff = SkillFactory.getSkill(155101008).getEffect(155101008);
                    int max = eff.getZ();
                    int count = player.getBuffedValue(400051036) ? player.getBuffedEffect(400051036).getX() : 0;
                    ArrayList<Integer> monsters = new ArrayList<Integer>();
                    MapleAtom atom = new MapleAtom(false, player.getId(), 47, true, 155100009, 0, 0);
                    for (i = 0; i < attack.targets && monsters.size() < max; ++i) {
                        monsters.add(0);
                        atom.addForceAtom(new ForceAtom(0, 1, Randomizer.rand(5, 10), 270, (short) Randomizer.rand(75, 95)));
                    }
                    for (i = 0; i < count; ++i) {
                        monsters.add(0);
                        atom.addForceAtom(new ForceAtom(0, 1, Randomizer.rand(5, 10), 270, (short) Randomizer.rand(75, 95)));
                    }
                    atom.setDwTargets(monsters);
                    player.getMap().spawnMapleAtom(atom);
                }
                if (player.getBuffedValue(400031030) && System.currentTimeMillis() - player.lastWindWallTime >= (long) (player.getBuffedEffect(400031030).getW2() * 1000)) {
                    player.lastWindWallTime = System.currentTimeMillis();
                    MapleAtom atom = new MapleAtom(false, player.getId(), 51, true, 400031031, player.getTruePosition().x, player.getTruePosition().y);
                    for (int i = 0; i < player.getBuffedEffect(400031030).getQ2(); ++i) {
                        atom.addForceAtom(new ForceAtom(Randomizer.nextBoolean() ? 1 : 3, Randomizer.rand(30, 60), 10, Randomizer.nextBoolean() ? Randomizer.rand(0, 5) : Randomizer.rand(180, 185), 0));
                    }
                    atom.setDwFirstTargetId(0);
                    player.getMap().spawnMapleAtom(atom);
                }
                if (player.getSkillLevel(80002762) > 0 && (stst = SkillFactory.getSkill(80002762).getEffect(player.getSkillLevel(80002762))).makeChanceResult()) {
                    stst.applyTo(player, false);
                }
                if (attack.skill == 5221015 || attack.skill == 151121001) {
                    player.cancelEffect(effect);
                    SkillFactory.getSkill(attack.skill).getEffect(attack.skilllevel).applyTo(player, false);
                }
                if (attack.skill == 61121104 || attack.skill == 61121124 || attack.skill == 61121221 || attack.skill == 61121223 || attack.skill == 61121225) {
                    SkillFactory.getSkill(61121116).getEffect(attack.skilllevel).applyTo(player, false);
                }
                if (player.getBuffedValue(SecondaryStat.Steal) != null && monster != null) {
                    monster.handleSteal(player);
                    if (Randomizer.isSuccess(10) && monster.getCustomValue0(4201017) == 0L) {
                        monster.setCustomInfo(4201017, 1, 0);
                        final Item toDrop = new Item(2431835, (short) 1, (short) 1, 0);
                        player.getMap().spawnItemDrop(monster, player, toDrop, monster.getPosition(), true, true);
                    }
                }
                if (player.getSkillLevel(4330007) > 0 && !GameConstants.is_forceAtom_attack_skill(attack.skill) && (bytalsteal = SkillFactory.getSkill(4330007).getEffect(player.getSkillLevel(4330007))).makeChanceResult()) {
                    player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) bytalsteal.getX());
                }
                if (totDamage > 0L && player.getSkillLevel(4200013) > 0 && !GameConstants.is_forceAtom_attack_skill(attack.skill)) {
                    SecondaryStatEffect criticalGrowing = SkillFactory.getSkill(4200013).getEffect(player.getSkillLevel(4200013));
                    if (player.getSkillLevel(4220015) > 0) {
                        criticalGrowing = SkillFactory.getSkill(4220015).getEffect(player.getSkillLevel(4220015));
                    }
                    if (player.criticalGrowing == 100 && player.criticalDamageGrowing >= criticalGrowing.getQ()) {
                        player.criticalGrowing = 0;
                        player.criticalDamageGrowing = 0;
                        player.setSkillCustomInfo(4220015, 0L, 4000L);
                    } else if (player.criticalGrowing + player.getStat().critical_rate >= 100) {
                        player.criticalGrowing = 100;
                    } else {
                        player.criticalGrowing += criticalGrowing.getX();
                        player.criticalDamageGrowing = Math.min(player.criticalDamageGrowing + criticalGrowing.getW(), criticalGrowing.getQ());
                        player.setSkillCustomInfo(4220015, 0L, 4000L);
                    }
                    criticalGrowing.applyTo(player, false, 0);
                }
                if (totDamage > 0L && player.getSkillLevel(5220055) > 0) {
                    SecondaryStatEffect quickDraw = SkillFactory.getSkill(5220055).getEffect(player.getSkillLevel(5220055));
                    if (!player.getBuffedValue(5220055)) {
                        if (quickDraw.makeChanceResult()) {
                            quickDraw.applyTo(player, false);
                        }
                    } else if (player.getBuffedValue(SecondaryStat.QuickDraw) != null && attack.skill != 5220023 && attack.skill != 5220024 && attack.skill != 5220025 && attack.skill != 5220020 && attack.skill != 5221004 && attack.skill != 400051006) {
                        player.cancelEffectFromBuffStat(SecondaryStat.QuickDraw, 5220055);
                    }
                }
                if (player.getBuffedValue(15001022) && attack.skill != 15111022 && attack.skill != 15120003 && attack.skill != 400051016) {
                    SecondaryStatValueHolder lightning;
                    int[] skills;
                    SecondaryStatEffect dkeffect = player.getBuffedEffect(15001022);
                    int prop = dkeffect.getProp();
                    int maxcount = dkeffect.getV();
                    for (int skill : skills = new int[]{15000023, 15100025, 15110026, 15120008}) {
                        if (player.getSkillLevel(skill) <= 0) {
                            continue;
                        }
                        prop += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getProp();
                        maxcount += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getV();
                    }
                    if (Randomizer.nextInt(100) < prop && player.lightning < maxcount) {
                        ++player.lightning;
                    }
                    if (player.getBuffedValue(15121004)) {
                        player.changeCooldown(15121004, -6000);
                    }
                    if (player.lightning < 0) {
                        player.lightning = 0;
                    }
                    if ((lightning = player.checkBuffStatValueHolder(SecondaryStat.CygnusElementSkill)) != null) {
                        lightning.effect.applyTo(player, false);
                    }
                }
                if (attack.skill == 21000006 || attack.skill == 21000007 || attack.skill == 21001010) {
                    if (player.getSkillLevel(21120021) > 0 && player.getSkillCustomValue(21120021) == null) {
                        SkillFactory.getSkill(21120021).getEffect(player.getSkillLevel(21120021)).applyTo(player, false);
                        player.setSkillCustomInfo(21120021, 0L, 3000L);
                    } else if (player.getSkillLevel(21100015) > 0 && player.getSkillCustomValue(21120021) == null) {
                        SkillFactory.getSkill(21100015).getEffect(player.getSkillLevel(21100015)).applyTo(player, false);
                        player.setSkillCustomInfo(21120021, 0L, 3000L);
                    }
                }
                if (attack.skill == 400011079) {
                    player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400011079, Arrays.asList(new RangeAttack(400011081, player.getTruePosition(), 0, 0, 0))));
                }
                if (attack.skill == 400011080) {
                    player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400011080, Arrays.asList(new RangeAttack(400011082, player.getTruePosition(), 0, 0, 0))));
                }
                if (player.getSkillLevel(400011134) > 0 && attack.targets > 0) {
                    SecondaryStatEffect ego_weapon = SkillFactory.getSkill(400011134).getEffect(player.getSkillLevel(400011134));
                    if (player.getGender() == 0 && !player.getBuffedValue(400011134) && player.getCooldownLimit(400011134) == 0L) {
                        ego_weapon.applyTo(player, false);
                    } else if (player.getGender() == 1 && player.getMap().getMist(player.getId(), 400011035) == null && player.getCooldownLimit(400011135) == 0L && !player.skillisCooling(400011135)) {
                        SecondaryStatEffect a = SkillFactory.getSkill(400011135).getEffect(player.getSkillLevel(400011134));
                        player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 400011135, 1, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null, null));
                        player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 400011135, 1, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null, null), false);
                        MapleMist newmist = new MapleMist(a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, 2000, (byte) (player.isFacingLeft() ? 1 : 0));
                        newmist.setPosition(monster.getPosition());
                        player.getMap().spawnMist(newmist, false);
                        newmist = new MapleMist(a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, 2000, (byte) (player.isFacingLeft() ? 1 : 0));
                        newmist.setPosition(monster.getPosition());
                        player.getMap().spawnMist(newmist, false);
                        newmist = new MapleMist(a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, 2000, (byte) (player.isFacingLeft() ? 1 : 0));
                        newmist.setPosition(monster.getPosition());
                        player.getMap().spawnMist(newmist, false);
                        player.addCooldown(400011135, System.currentTimeMillis(), SkillFactory.getSkill(400011134).getEffect(player.getSkillLevel(400011134)).getCooldown(player));
                        player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400011135, SkillFactory.getSkill(400011134).getEffect(player.getSkillLevel(400011134)).getCooldown(player)));
                    }
                }
                if (player.getBuffedEffect(SecondaryStat.CrystalGate) != null) {
                    SecondaryStatEffect crystalGate = player.getBuffedEffect(SecondaryStat.CrystalGate);
                    if ((double) (System.currentTimeMillis() - player.lastCrystalGateTime) >= crystalGate.getT() * 1000.0) {
                        player.lastCrystalGateTime = System.currentTimeMillis();
                        player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400021111, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
                    }
                }
                if (!GameConstants.isKaiser(player.getJob())) {
                    break block858;
                }
                if (monster == null || player.getBuffedValue(400011118) || !player.skillisCooling(400011118) || player.getSkillCustomValue(400011118) != null) {
                    break block859;
                }
                SecondaryStatEffect dragonBlaze = SkillFactory.getSkill(400011118).getEffect(player.getSkillLevel(400011118));
                if (attack.skill == 400011119 || attack.skill == 400011120) {
                    break block858;
                }
                ArrayList<SecondAtom> atoms = new ArrayList<SecondAtom>();
                atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1, new Point((int) player.getTruePosition().getX(), (int) player.getTruePosition().getY() - 200), new ArrayList<Integer>()));
                atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1, new Point((int) player.getTruePosition().getX() - 200, (int) player.getTruePosition().getY() - 100), new ArrayList<Integer>()));
                atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1, new Point((int) player.getTruePosition().getX(), (int) player.getTruePosition().getY() + 200), new ArrayList<Integer>()));
                atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1, new Point((int) player.getTruePosition().getX() + 200, (int) player.getTruePosition().getY() - 100), new ArrayList<Integer>()));
                atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1, new Point((int) player.getTruePosition().getX() - 200, (int) player.getTruePosition().getY() + 100), new ArrayList<Integer>()));
                atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1, new Point((int) player.getTruePosition().getX() + 200, (int) player.getTruePosition().getY() + 100), new ArrayList<Integer>()));
                player.setSkillCustomInfo(400011118, 0L, dragonBlaze.getV2() * 1000);
                player.getMap().spawnSecondAtom(player, atoms, 0);
                break block858;
            }
            if (attack.skill == 400011118) {
                player.getMap().spawnSecondAtom(player, Arrays.asList(new SecondAtom(11, player.getId(), 0, 400011119, 5000, 0, -1, attack.position, new ArrayList<Integer>())), 0);
            } else if (attack.skill != 400011118 && !player.skillisCooling(400111119)) {
                for (SecondAtom at : player.getMap().getAllSecondAtomsThreadsafe()) {
                    if (at.getSkillId() != 400011119) {
                        continue;
                    }
                    player.addCooldown(400111119, System.currentTimeMillis(), (int) SkillFactory.getSkill(400011118).getEffect(player.getSkillLevel(400011118)).getT() * 1000);
                    player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011130, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
                    break;
                }
            }
        }
        if (attack.skill >= 400051059 && attack.skill <= 400051067 && attack.skill != 400051065 && attack.skill != 400051067) {
            SkillFactory.getSkill(400051058).getEffect(player.getSkillLevel(400051058)).applyTo(player);
            if (attack.isLink || attack.skill == 400051066) {
                SkillFactory.getSkill(400051044).getEffect(player.getSkillLevel(400051044)).applyTo(player);
                List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 500000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
                if (objs.size() > 0) {
                    ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                    for (int i = 1; i <= objs.size(); ++i) {
                        mobList.add(new Triple<Integer, Integer, Integer>(objs.get(Randomizer.nextInt(objs.size())).getObjectId(), 134 + (i - 1) * 70, 0));
                        if (i >= (player.getBuffedValue(400051058) ? effect.getW() : effect.getV2())) {
                            break;
                        }
                    }
                    player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(attack.skill == 400051066 ? 400051067 : 400051065, mobList, false, 0, player.getBuffedValue(400051058) ? 3 : 1, attack.position.x, attack.position.y, player.getBuffedValue(400051058) ? 445 : 565, player.getBuffedValue(400051058) ? 315 : 510));
                }
            }
        }
        if (attack.skill == 400041069) {
            ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
            skills.add(new RangeAttack(400041070, attack.position, -1, 900, 1));
            skills.add(new RangeAttack(400041071, attack.position, -1, 1680, 1));
            skills.add(new RangeAttack(400041072, attack.position, -1, 2460, 1));
            skills.add(new RangeAttack(400041070, attack.position, -1, 3120, 1));
            skills.add(new RangeAttack(400041071, attack.position, -1, 3600, 1));
            skills.add(new RangeAttack(400041072, attack.position, -1, 3960, 1));
            skills.add(new RangeAttack(400041070, attack.position, -1, 4200, 1));
            skills.add(new RangeAttack(400041071, attack.position, -1, 4440, 1));
            skills.add(new RangeAttack(400041072, attack.position, -1, 4620, 1));
            skills.add(new RangeAttack(400041070, attack.position, -1, 4800, 1));
            skills.add(new RangeAttack(400041071, attack.position, -1, 4920, 1));
            skills.add(new RangeAttack(400041073, attack.position, -1, 5370, 1));
            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
        }
        if (player.getSkillLevel(400011048) > 0 && !player.skillisCooling(400011048) && attack.targets > 0 && SkillFactory.getSkill(400011048).getSkillList().contains(attack.skill)) {
            SecondaryStatEffect flear = SkillFactory.getSkill(400011048).getEffect(player.getSkillLevel(400011048));
            ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
            int attackid = player.getBuffedValue(400011005) ? 400011049 : 400011048;
            skills.add(new RangeAttack(attackid, player.getTruePosition(), 1, 0, 1));
            player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, attackid, 1, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getPosition(), null, null));
            player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, attackid, 1, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getPosition(), null, null), false);
            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attackid, skills));
            player.addCooldown(400011048, System.currentTimeMillis(), flear.getCooldown(player));
            player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400011048, flear.getCooldown(player)));
        }
        if (player.getSkillLevel(400041075) > 0 && player.getCooldownLimit(400041075) == 0L && (attack.skill == 4341004 || attack.skill == 4341009)) { //사라졌?
            SecondaryStatEffect hunted_edge = SkillFactory.getSkill(400041075).getEffect(player.getSkillLevel(400041075));
            if (attack.skill == 4341004) {
                player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(400041076, attack.position, 0, 0, 6))));
            } else if (attack.skill == 4341009) {
                ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
                skills.add(new RangeAttack(400041078, attack.position, 0, 0, 6));
                player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
            }
            player.getClient().send(CField.skillCooldown(hunted_edge.getSourceId(), hunted_edge.getCooldown(player)));
            player.addCooldown(hunted_edge.getSourceId(), System.currentTimeMillis(), hunted_edge.getCooldown(player));
        }
        if (player.getSkillLevel(23110004) > 0 && attack.isLink && attack.charge == 0 && !player.getBuffedValue(400031017)) {
            int[] linkCooldownSkills;
            for (int ck : linkCooldownSkills = new int[]{23121052, 400031007, 23111002, 23121002}) {
                if (!player.skillisCooling(ck)) {
                    continue;
                }
                if (ck == 400031007) {
                    player.setSkillCustomInfo(400031007, 0L, player.getSkillCustomTime(400031007) - 1000);
                    continue;
                }
                player.changeCooldown(ck, -1000);
            }
            if (!player.getBuffedValue(23110004)) {
                player.removeSkillCustomInfo(23110005);
            }
            if (player.getSkillCustomValue0(23110005) < 10L) {
                player.setSkillCustomInfo(23110005, player.getSkillCustomValue0(23110005) + 1L, 0L);
            }
            SkillFactory.getSkill(23110004).getEffect(player.getSkillLevel(23110004)).applyTo(player);
        }
        if (attack.skill == 23121000 && player.getBuffedValue(23110004) && !player.getBuffedValue(400031017)) {
            SkillFactory.getSkill(23110004).getEffect(player.getSkillLevel(23110004)).applyTo(player, false);
        }
        if (totDamage > 0L && attack.isLink && player.getSkillLevel(400051044) > 0 && (attack.skill < 400051059 || attack.skill > 400051067) && attack.skill != 0) {
            if (player.getBuffedValue(400051044)) {
                if (player.getBuffedValue(SecondaryStat.Striker3rd) <= 8) {
                    SkillFactory.getSkill(400051044).getEffect(player.getSkillLevel(400051044)).applyTo(player, false);
                }
            } else {
                SkillFactory.getSkill(400051044).getEffect(player.getSkillLevel(400051044)).applyTo(player, false);
            }
        }
        if (GameConstants.isAngelicBuster(player.getJob()) && totDamage > 0L) {
            player.Recharge(attack.skill);
            if (attack.skill == 65121007 || attack.skill == 65121008) {
                SkillFactory.getSkill(65121101).getEffect(player.getSkillLevel(65121101)).applyTo(player);
            }
        }
        if (player.getBuffedValue(400031006) && attack.skill == 400031010) {
            --player.trueSniping;
            if (player.trueSniping <= 0) {
                player.cancelEffectFromBuffStat(SecondaryStat.TrueSniping);
            } else {
                player.getBuffedEffect(400031006).applyTo(player, false);
            }
        }
        if (attack.skill == 4331003 && (hpMob <= 0L || totDamageToOneMonster < hpMob)) {
            return;
        }
        if (hpMob > 0L && totDamageToOneMonster > 0L) {
            player.afterAttack(attack);
        }
        if (player.getBuffedValue(400031007) && totDamageToOneMonster > 0L && player.getSkillCustomValue(400031007) == null) {
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400031011, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
            player.setSkillCustomInfo(400031007, 0L, player.getBuffedEffect(400031007).getS2() * 1000);
        }
        if (player.getBuffedValue(400011005) && totDamageToOneMonster > 0L) {
            if (GameConstants.isPollingmoonAttackskill(attack.skill) && player.getSkillCustomValue(400011022) == null) {
                player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011022, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
                player.setSkillCustomInfo(400011022, 0L, player.getBuffedEffect(400011005).getS2() * 1000 + 1000);
            } else if (GameConstants.isRisingsunAttackskill(attack.skill) && player.getSkillCustomValue(400011023) == null) {
                player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011023, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
                player.setSkillCustomInfo(400011023, 0L, player.getBuffedEffect(400011005).getS2() * 1000);
            }
        }
        if ((attack.skill == 11121052 || attack.skill == 11121055 || attack.skill == 400011056) && player.getSkillLevel(400011048) > 0 && player.getCooldownLimit(400011048) > 0L) {
            SecondaryStatEffect flare_slash = SkillFactory.getSkill(400011048).getEffect(player.getSkillLevel(400011048));
            int count = attack.hits;
            if (player.getBuffedEffect(SecondaryStat.Buckshot) != null) {
                count /= 2;
            }
            player.changeCooldown(400011048, -(flare_slash.getZ() / count));
        }
        if (player.getBuffedValue(400051010) && !GameConstants.is_forceAtom_attack_skill(attack.skill) && player.getSkillCustomValue(400051010) == null && attack.skill != 25121055 && attack.skill != 25111012) {
            Skill skill = SkillFactory.getSkill(400051010);
            SecondaryStatEffect eff = null;
            ArrayList<Pair<Integer, Integer>> skillList = new ArrayList<Pair<Integer, Integer>>();
            List<RandomSkillEntry> rse = skill.getRSE();
            for (RandomSkillEntry info : rse) {
                if (!Randomizer.isSuccess(info.getProb())) {
                    continue;
                }
                if (info.getSkillList().size() > 0) {
                    skillList.addAll(info.getSkillList());
                } else {
                    skillList.add(new Pair<Integer, Integer>(info.getSkillId(), 0));
                }
                if (info.getSkillId() == 25121055 || info.getSkillId() == 25111012) {
                    int skilllevel;
                    int n = skilllevel = info.getSkillId() == 25121055 ? 25121030 : 25111012;
                    if (player.getSkillLevel(25121055) < 1) {
                        player.changeSkillLevel(25121055, (byte) 1, (byte) 1);
                    }
                    eff = SkillFactory.getSkill(info.getSkillId()).getEffect(skilllevel);
                    MapleMist mist = new MapleMist(eff.calculateBoundingBox(player.getPosition(), player.isFacingLeft()), player, eff, 3000, (byte) (player.isFacingLeft() ? 1 : 0));
                    mist.setPosition(player.getPosition());
                    player.getMap().spawnMist(mist, false);
                } else {
                    player.getClient().getSession().writeAndFlush((Object) CField.SpiritFlow(skillList));
                }
                player.setSkillCustomInfo(400051010, 0L, player.getBuffedEffect(400051010).getX() * 1000);
                break;
            }
        }
        if (GameConstants.isNightWalker(player.getJob())) {
            if (!GameConstants.is_forceAtom_attack_skill(attack.skill) && player.getBuffedEffect(SecondaryStat.DarkSight) != null && attack.summonattack == 0) {
                player.cancelEffectFromBuffStat(SecondaryStat.DarkSight);
            }
        } else if (!(GameConstants.isKadena(player.getJob()) || GameConstants.isHoyeong(player.getJob()) || attack.skill == 4221052 || player.getBuffedValue(SecondaryStat.DarkSight) == null || player.getBuffedValue(400001023))) {
            int prop = 0;
            if (player.getSkillLevel(4210015) > 0) {
                prop = SkillFactory.getSkill(4210015).getEffect(player.getSkillLevel(4210015)).getProp();
            }
            if (player.getSkillLevel(0x421211) > 0) {
                prop = SkillFactory.getSkill(0x421211).getEffect(player.getSkillLevel(0x421211)).getProp();
            }
            for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
                if (mist.getOwnerId() != player.getId() || mist.getSource().getSourceId() != 4221006 || !mist.getBox().contains(player.getTruePosition())) {
                    continue;
                }
                prop = 100;
                break;
            }
            if (player.getSkillCustomValue0(4221052) != 0L && (long) player.getPosition().x <= player.getSkillCustomValue0(4221052) + 300L && (long) player.getPosition().x >= player.getSkillCustomValue0(4221052) - 300L) {
                prop = 100;
            }
            if (!Randomizer.isSuccess(prop)) {
                player.cancelEffectFromBuffStat(SecondaryStat.DarkSight);
            }
        } else if (!(player.getBuffedValue(SecondaryStat.DarkSight) == null || player.getBuffedValue(400001023) || GameConstants.is_forceAtom_attack_skill(attack.skill) || GameConstants.isDarkSightDispelSkill(attack.skill) || attack.skill >= 400000000)) {
            player.cancelEffectFromBuffStat(SecondaryStat.DarkSight);
        }
        if (player.getSkillLevel(101110205) > 0 && player.getGender() == 0 && totDamageToOneMonster > 0L && (combatRecovery = SkillFactory.getSkill(101110205).getEffect(player.getSkillLevel(101110205))).makeChanceResult()) {
            player.addMP(combatRecovery.getZ());
        }
        if (totDamage > 0L && player.getBuffedValue(400021073) && (summon = player.getSummon(400021073)) != null && summon.getEnergy() < 22) {
            switch (attack.skill) {
                case 22110022:
                case 22110023:
                case 22111012:
                case 22170060:
                case 22170070:
                case 400021012:
                case 400021014:
                case 400021015: {
                    MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073, summon.getTruePosition().x, summon.getTruePosition().y);
                    atom.setDwUserOwner(summon.getOwner().getId());
                    atom.setDwFirstTargetId(0);
                    ForceAtom aatom = new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0);
                    atom.addForceAtom(aatom);
                    player.getMap().spawnMapleAtom(atom);
                    summon.setEnergy(Math.min(SkillFactory.getSkill(400021073).getEffect(player.getSkillLevel(400021073)).getX(), summon.getEnergy() + 1));
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
                    if (summon.getEnergy() < SkillFactory.getSkill(400021073).getEffect(player.getSkillLevel(400021073)).getX()) {
                        break;
                    }
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
                    break;
                }
                case 22110014:
                case 22110024:
                case 22110025:
                case 22111011:
                case 22140014:
                case 22140015:
                case 22140023:
                case 22140024:
                case 22141011:
                case 22170064:
                case 22170065:
                case 22170066:
                case 22170067:
                case 22170093:
                case 22170094:
                case 22171063:
                case 22171083:
                case 22171095:
                case 400021013: {
                    if (summon.getMagicSkills().contains(attack.skill)) {
                        break;
                    }
                    summon.getMagicSkills().add(attack.skill);
                    summon.setEnergy(Math.min(22, summon.getEnergy() + 3));
                    MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073, summon.getTruePosition().x, summon.getTruePosition().y);
                    atom.setDwUserOwner(summon.getOwner().getId());
                    ArrayList<Integer> monsters = new ArrayList<Integer>();
                    monsters.add(0);
                    atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
                    atom.setDwTargets(monsters);
                    player.getMap().spawnMapleAtom(atom);
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
                    if (summon.getEnergy() < 22) {
                        break;
                    }
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
                    break;
                }
            }
        }
        if (player.getBuffedEffect(SecondaryStat.ComboInstict) != null && (attack.skill == 1121008 || attack.skill == 0x111711)) {
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011074, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011075, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011076, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
        }
        if (player.getBuffedValue(400001037) && attack.skill != 400001038 && System.currentTimeMillis() - player.lastAngelTime >= (long) (player.getBuffedEffect(400001037).getZ() * 1000)) {
            player.lastAngelTime = System.currentTimeMillis();
            ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
            int i = 0;
            for (AttackPair a : attack.allDamage) {
                mobList.add(new Triple<Integer, Integer, Integer>(a.objectId, 291 + 70 * i, 0));
                ++i;
            }
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400001038, mobList, true, 0, new int[0]));
        }
        if (attack.skill == 155120000 && player.getSkillLevel(400051047) > 0 && player.getCooldownLimit(400051047) == 0L) {
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400051047, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
        }
        if (attack.skill == 155120001 && player.getSkillLevel(400051047) > 0 && player.getCooldownLimit(400051048) == 0L) {
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400051048, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
        }
        if (attack.skill == 3321014 || attack.skill == 3321016 || attack.skill == 3321018 || attack.skill == 3321020) {
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(attack.skill + 1, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 600, new int[0]));
        }
        if (attack.skill == 400041042) {
            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(400041043, attack.position, 1, 0, 0))));
        }
        if (player.getBuffedValue(21101005) && totDamageToOneMonster > 0L && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
            player.addHP(player.getStat().getCurrentMaxHp() * (long) player.getBuffedEffect(21101005).getX() / 100L);
        }
        if (SkillFactory.getSkill(attack.skill) != null && player.getSkillLevel(1310009) > 0 && !SkillFactory.getSkill(attack.skill).isFinalAttack() && totDamageToOneMonster > 0L && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null && SkillFactory.getSkill(1310009).getEffect(player.getSkillLevel(1310009)).makeChanceResult()) {
            player.addHP(player.getStat().getCurrentMaxHp() * (long) SkillFactory.getSkill(1310009).getEffect(player.getSkillLevel(1310009)).getX() / 100L);
        }
        if (totDamageToOneMonster > 0L && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null && player.getBuffedValue(1321054)) {
            player.addHP(player.getStat().getCurrentMaxHp() * (long) player.getBuffedEffect(1321054).getX() / 100L);
        }
        if (player.getSkillLevel(60030241) > 0 || player.getSkillLevel(80003015) > 0) {
            int skillid;
            int n = player.getSkillLevel(60030241) > 0 ? 60030241 : (skillid = player.getSkillLevel(80003015) > 0 ? 80003015 : 0);
            if (n > 0 && monster != null) { // n
                if (monster.getStats().isBoss()) {
                    if (monster.isAlive()) {
                        player.handlePriorPrepaRation(n, 2);
                    }
                } else if (!monster.isAlive()) {
                    player.handlePriorPrepaRation(n, 1);
                }
            }
        }
        if (player.getSkillLevel(400051015) > 0) {
            if (monster.getStats().isBoss()) {
                if (monster.isAlive()) {
                    player.Serpent = player.Serpent + 1;
                }
            } else if (!monster.isAlive()) {
                player.Serpent2 = player.Serpent2 + 1;
                player.Serpent = player.Serpent + 3;
            } else if (monster.isAlive()){
                player.Serpent = player.Serpent + 3;
            }
            if (player.Serpent2 > 40) {
                player.Serpent2 = 0;
                player.changeCooldown(400051015, -10000);
            }
            if (player.Serpent > 300) {
                player.Serpent = 0;
                player.handleSerpent(1);
            }
        }
        if (player.getSkillLevel(150010241) > 0 && player.getSkillCustomValue(80000514) == null) {
            SkillFactory.getSkill(150010241).getEffect(player.getSkillLevel(150010241)).applyTo(player);
            player.setSkillCustomInfo(80000514, 0L, 3000L);
        } else if (player.getSkillLevel(80000514) > 0 && player.getSkillCustomValue(80000514) == null) {
            SkillFactory.getSkill(80000514).getEffect(player.getSkillLevel(80000514)).applyTo(player);
            player.setSkillCustomInfo(80000514, 0L, 3000L);
        }
        if (player.getBuffedValue(63121044) && player.getSkillCustomValue(80113017) == null && (long) player.getId() != player.getSkillCustomValue0(63121044)) {
            MapleMonster mob = null;
            List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 600000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
            if (objs.size() > 0) {
                for (int i = 1; i <= objs.size(); ++i) {
                    mob = (MapleMonster) objs.get(Randomizer.nextInt(objs.size()));
                    if (mob == null || !mob.isAlive()) {
                        continue;
                    }
                    RangeAttack rg = new RangeAttack(80003017, mob.getTruePosition(), 0, 0, 1);
                    rg.getList().add(mob.getObjectId());
                    player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(80003017, Arrays.asList(rg)));
                    player.setSkillCustomInfo(80113017, 0L, player.getBuffedEffect(63121044).getY() * 1000);
                    break;
                }
            }
        }
        if (attack.targets > 0) {
            int fallingspeed = 30;
            int fallingtime = GameConstants.getFallingTime(attack.skill);
            if (fallingtime != -1) {
                player.getClient().getSession().writeAndFlush((Object) CField.setFallingTime(fallingspeed, fallingtime));
            }
        }
        if (monster != null) {
            if (monster.getBuff(MonsterStatus.MS_DarkLightning) != null && attack.skill != 400021113 && attack.skill != 32110020 && attack.skill != 32111016 && attack.skill != 32101001 && attack.skill != 32111015 && attack.skill != 400021088) {
                ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 0, 0));
                player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(32110020, mobList, false, 0, new int[0]));
                boolean cancel = true;
                if (player.getBuffedValue(400021087)) {
                    int maxcount = SkillFactory.getSkill(400021087).getEffect(player.getSkillLevel(400021087)).getS2();
                    if (player.getSkillCustomValue0(410021087) < (long) (maxcount - 1)) {
                        player.addSkillCustomInfo(410021087, 1L);
                        cancel = false;
                    } else {
                        player.removeSkillCustomInfo(410021087);
                    }
                }
                if (cancel) {
                    monster.cancelStatus(MonsterStatus.MS_DarkLightning, monster.getBuff(MonsterStatus.MS_DarkLightning));
                }
            }
            if (GameConstants.isAran(player.getJob())) {
                if (SkillFactory.getSkill(400011122).getSkillList().contains(attack.skill) && monster.getCustomValue0(400011121) > 0L && monster.getCustomTime(400011122) != null && player.getBuffedValue(400011123)) {
                    ArrayList<Triple<Integer, Integer, Integer>> list = new ArrayList<Triple<Integer, Integer, Integer>>();
                    if (monster.getCustomValue0(400011121) < 6L) {
                        monster.addSkillCustomInfo(400011121, 1L);
                    }
                    list.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), (int) monster.getCustomValue0(400011121), monster.getCustomTime(400011122)));
                    player.getMap().broadcastMessage(CField.getBlizzardTempest(list));
                    ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                    player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011122, mobList, true, 0, new int[0]));
                } else if (attack.skill == 400011121) {
                    ArrayList<Triple<Integer, Integer, Integer>> list = new ArrayList<Triple<Integer, Integer, Integer>>();
                    for (MapleMonster m3 : player.getMap().getAllMonster()) {
                        if (m3 == null || m3.getCustomValue0(400011121) <= 0L || m3.getCustomTime(400011122) == null) {
                            continue;
                        }
                        list.add(new Triple<Integer, Integer, Integer>(m3.getObjectId(), (int) m3.getCustomValue0(400011121), m3.getCustomTime(400011122)));
                    }
                    SkillFactory.getSkill(400011123).getEffect(player.getSkillLevel(400011121)).applyTo(player);
                    if (!list.isEmpty()) {
                        player.getMap().broadcastMessage(CField.getBlizzardTempest(list));
                    }
                }
            }
        }
        if (effect != null && player.getBuffedValue(SecondaryStat.OverloadMana) != null && !GameConstants.is_forceAtom_attack_skill(attack.skill) && !effect.isMist()) {
            if (GameConstants.isKinesis(player.getJob())) {
                player.addHP((int) (-(player.getStat().getCurrentMaxHp() * (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getY() / 100L)));
            } else {
                player.addMP((int) (-(player.getStat().getCurrentMaxMp(player) * (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getX() / 100L)));
            }
        }
        if (GameConstants.isCaptain(player.getJob()) && attack.skill == 400051073) {
            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400051081, Arrays.asList(new RangeAttack(400051081, monster.getPosition(), 0, 0, 1))));
        }
        if (GameConstants.isAdel(player.getJob())) {
            if (player.getBuffedValue(151101013) && player.getSkillCustomValue(151101013) == null && attack.targets > 0 && (attack.skill == 151101000 || attack.skill == 151111000 || attack.skill == 151121000 || attack.skill == 151121002)) {
                player.addMP(-player.getBuffedEffect(151101013).getY());
                player.getMap().broadcastMessage(SkillPacket.CreateSubObtacle(player, 151001001));
                player.setSkillCustomInfo(151101013, 0L, 8000L);
            }
            if (player.getSkillLevel(151100017) > 0 && attack.targets > 0 && (attack.skill == 151101000 || attack.skill == 151111000 || attack.skill == 151121000 || attack.skill == 151121002)) {
                player.\uc5d0\ud14c\ub974\ud578\ub4e4\ub7ec(player, 12, attack.skill, false);
            }
            if (player.getBuffedValue(151101006) && player.\uc5d0\ud14c\ub974\uc18c\ub4dc > 0 && player.getSkillCustomValue(151101016) == null && attack.targets > 0 && (attack.skill == 151101000 || attack.skill == 151111000 || attack.skill == 151121000 || attack.skill == 151121002)) {
                for (int i = 1; i <= player.\uc5d0\ud14c\ub974\uc18c\ub4dc; ++i) {
                    player.getMap().broadcastMessage(SkillPacket.AutoAttackObtacleSword(player, i * 10, i == 1 ? player.\uc5d0\ud14c\ub974\uc18c\ub4dc : 0));
                }
                if (player.getJob() == 15112) {
                    player.setSkillCustomInfo(151101016, 0L, 1500L);
                } else if (player.getJob() == 15111) {
                    player.setSkillCustomInfo(151101016, 0L, 5500L);
                } else {
                    player.setSkillCustomInfo(151101016, 0L, 9500L);
                }
            }
            if (monster != null && attack != null) {
                switch (attack.skill) {
                    case 151111002: {
                        if (!Randomizer.isSuccess(40) || attack.targets <= 0) {
                            break;
                        }
                        player.\uc5d0\ud14c\ub974\uacb0\uc815(player, monster.getTruePosition(), false);
                        break;
                    }
                    case 151111003: {
                        if (!Randomizer.isSuccess(15) || attack.targets <= 0) {
                            break;
                        }
                        player.\uc5d0\ud14c\ub974\uacb0\uc815(player, monster.getTruePosition(), false);
                        break;
                    }
                    case 400011108: {
                        if (!Randomizer.isSuccess(5) || attack.targets <= 0) {
                            break;
                        }
                        player.\uc5d0\ud14c\ub974\uacb0\uc815(player, monster.getTruePosition(), false);
                        break;
                    }
                    case 151121003: {
                        if (!Randomizer.isSuccess(50) || attack.targets <= 0) {
                            break;
                        }
                        player.\uc5d0\ud14c\ub974\uacb0\uc815(player, monster.getTruePosition(), false);
                        break;
                    }
                    case 151101007:
                    case 151101008:
                    case 151101009: {
                        if (!Randomizer.isSuccess(30) || attack.targets <= 0) {
                            break;
                        }
                        player.\uc5d0\ud14c\ub974\uacb0\uc815(player, monster.getTruePosition(), false);
                    }
                }
            }
            if (attack.skill == 151101003 || attack.skill == 151101004 && attack.targets > 0) {
                if (!player.getBuffedValue(151101010)) {
                    player.removeSkillCustomInfo(151101010);
                }
                SkillFactory.getSkill(151101010).getEffect(player.getSkillLevel(151101003)).applyTo(player);
            } else if (attack.skill == 151121001 && monster != null) {
                player.setSkillCustomInfo(151121001, monster.getObjectId(), 0L);
                if (player.getSkillCustomValue0(151121001) != (long) monster.getObjectId()) {
                    player.removeSkillCustomInfo(151121001);
                }
                SkillFactory.getSkill(151121001).getEffect(player.getSkillLevel(151121001)).applyTo(player);
            }
        } else if (GameConstants.isCain(player.getJob()) && attack.targets > 0) {
            if (attack.targets > 0) {
                if (player.getBuffedValue(400031062) && !player.skillisCooling(400031063)) {
                    boolean facingleft = (attack.facingleft >>> 4 & 0xF) == 8;
                    for (int i = 0; i < player.getBuffedEffect(400031062).getMobCount(); ++i) {
                        int x = attack.position.x;
                        int y = attack.position.y - Randomizer.rand(50, 210);
                        x = facingleft ? (x += Randomizer.rand(50, 200)) : (x -= Randomizer.rand(50, 200));
                        player.createSecondAtom(SkillFactory.getSkill(400031063).getSecondAtoms(), new Point(x, y), facingleft);
                    }
                    player.addCooldown(400031063, System.currentTimeMillis(), player.getBuffedEffect(400031062).getSubTime());
                }
                if (player.getSkillCustomValue(63101114) == null && (SkillFactory.getSkill(63101001).getSkillList().contains(attack.skill) || SkillFactory.getSkill(63101001).getSkillList2().contains(attack.skill))) {
                    player.handlePossession(2);
                    if (attack.skill == 63101004) {
                        player.setSkillCustomInfo(63101114, 0L, 1000L);
                    }
                }
                if (player.getBuffedValue(63101005)) {
                    SecondaryStatEffect effect2 = SkillFactory.getSkill(63101005).getEffect(player.getSkillLevel(63101005));
                    if (SkillFactory.getSkill(63101005).getSkillList().contains(attack.skill) || SkillFactory.getSkill(63101005).getSkillList2().contains(attack.skill)) {
                        if (player.getSkillCustomValue0(63101006) > 0L) {
                            for (MapleSecondAtom at : player.getMap().getAllSecondAtoms()) {
                                if (at.getSourceId() != 63101006 || at.getChr().getId() != player.getId() || (long) (effect2.getU() * 1000) - (System.currentTimeMillis() - at.getLastAttackTime()) > 0L) {
                                    continue;
                                }
                                player.getMap().broadcastMessage(SkillPacket.AttackSecondAtom(player, at.getObjectId(), 1));
                            }
                        }
                        player.addSkillCustomInfo(63101005, 1L);
                        if (player.getSkillCustomValue0(63101005) >= 7L && player.getSkillCustomValue0(63101006) < (long) effect2.getQ()) {
                            player.addSkillCustomInfo(63101006, 1L);
                            player.removeSkillCustomInfo(63101005);
                            player.createSecondAtom(63101006, player.getPosition(), (int) player.getSkillCustomValue0(63101006) - 1);
                        }
                    }
                }
                if (attack.skill == 63001000 || attack.skill == 63101003 || attack.skill == 63111002) {
                    RangeAttack rand = new RangeAttack(63001001, attack.position, 0, 0, 1);
                    ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
                    skills.add(rand);
                    player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
                }
                if (player.getBuffedValue(400031062) && player.getSkillCustomValue(400031063) == null && attack.skill != 63111012 && attack.skill != 63101006 && attack.skill != 63111010) {
                    SecondaryStatEffect effect2 = SkillFactory.getSkill(400031062).getEffect(player.getSkillLevel(400031062));
                    player.addSkillCustomInfo(400031062, 1L);
                    if (player.getSkillCustomValue0(400031062) >= (long) effect2.getW()) {
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(effect2.getSourceId(), Arrays.asList(new RangeAttack(400031063, monster.getPosition(), 0, 0, 1))));
                        player.removeSkillCustomInfo(400031062);
                        player.setSkillCustomInfo(400031063, 0L, effect2.getSubTime());
                    }
                }
                if (player.getSkillLevel(400031066) > 0 && monster != null) {
                    SecondaryStatEffect effect2 = SkillFactory.getSkill(400031066).getEffect(player.getSkillLevel(400031066));
                    boolean givebuff = false;
                    if (monster.getStats().isBoss()) {
                        player.addSkillCustomInfo(400031067, 1L);
                        if (player.getSkillCustomValue0(400031067) >= (long) effect2.getQ2()) {
                            player.removeSkillCustomInfo(400031067);
                            givebuff = true;
                        }
                    } else if (!monster.isAlive()) {
                        player.addSkillCustomInfo(400031068, 1L);
                        if (player.getSkillCustomValue0(400031068) >= (long) effect2.getQ2()) {
                            player.removeSkillCustomInfo(400031068);
                            givebuff = true;
                        }
                    }
                    if (givebuff) {
                        player.addSkillCustomInfo(400031066, 1L);
                        if (player.getSkillCustomValue0(400031066) >= (long) effect2.getU()) {
                            player.setSkillCustomInfo(400031066, effect2.getU(), 0L);
                        }
                        effect2.applyTo(player);
                    }
                }
            }
        } else if (GameConstants.isBlaster(player.getJob())) {
            if (afterimageshockattack) {
                SecondaryStatEffect effect2 = SkillFactory.getSkill(400011116).getEffect(player.getSkillLevel(400011116));
                long duration = player.getBuffLimit(400011116);
                player.setSkillCustomInfo(400011116, player.getSkillCustomValue0(400011116) - 1L, 0L);
                if (player.getSkillCustomValue0(400011116) > 0L) {
                    effect2.applyTo(player, false, (int) duration);
                } else {
                    player.cancelEffectFromBuffStat(SecondaryStat.AfterImageShock, 400011116);
                }
            }
        } else if (GameConstants.isDemonAvenger(player.getJob())) {
            if (!player.getBuffedValue(30010230)) {
                player.updateExceed(player.getExceed());
            }
            if (attack.skill == 31221052) {
                player.gainExceed((short) 5);
            }
            if (GameConstants.isExceedAttack(attack.skill)) {
                if (player.getSkillLevel(31220044) > 0) {
                    if (player.getExceed() < 19) {
                        player.gainExceed((short) 1);
                    }
                } else if (player.getExceed() < 20) {
                    player.gainExceed((short) 1);
                }
                player.handleExceedAttack(attack.skill);
            }
            if (player.getBuffedValue(31221054)) {
                player.addHP(stats.getCurrentMaxHp() / 100L * 5L);
            }
            if (attack.skill == 31211001) {
                player.addHP(stats.getCurrentMaxHp() / 100L * 10L, true, false);
            } else if (attack.skill == 400011062) {
                player.addHP(stats.getCurrentMaxHp() / 100L * 10L, true, false);
                player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400011038, effect.getCooldown(player)));
            } else if (attack.skill == 400011063) {
                player.addHP(stats.getCurrentMaxHp() / 100L * 15L, true, false);
                player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400011038, effect.getCooldown(player)));
            } else if (attack.skill == 400011064) {
                player.addHP(stats.getCurrentMaxHp() / 100L * 20L, true, false);
                player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400011038, effect.getCooldown(player)));
            }
            if (SkillFactory.getSkill(attack.skill) != null && attack.targets > 0 && attack.skill != 31220007 && !SkillFactory.getSkill(attack.skill).isFinalAttack() && monster != null) {
                SecondaryStatEffect effect2 = SkillFactory.getSkill(31010002).getEffect(player.getSkillLevel(31010002));
                SecondaryStatEffect Exceed = SkillFactory.getSkill(30010230).getEffect(player.getSkillLevel(30010230));
                int healper = effect2.getX();
                if (player.getSkillLevel(31210006) > 0) {
                    healper = SkillFactory.getSkill(31210006).getEffect(player.getSkillLevel(31210006)).getX();
                }
                if (GameConstants.isExceedAttack(attack.skill)) {
                    int minusper = 0;
                    minusper = player.getExceed() / Exceed.getZ() * Exceed.getY();
                    if (player.getSkillLevel(31210005) > 0 && minusper > 0 && (minusper -= SkillFactory.getSkill(31210005).getEffect(player.getSkillLevel(31210005)).getX()) < 0) {
                        minusper = 0;
                    }
                    if (minusper > 0) {
                        healper -= minusper;
                    }
                }
                player.addHP(stats.getCurrentMaxHp() / 100L * (long) healper);
            }
        } else if (GameConstants.isBattleMage(player.getJob())) {
            if (player.getBuffedEffect(SecondaryStat.AbyssalLightning) != null && player.getSkillCustomValue(400021113) == null) {
                SecondaryStatEffect ef = SkillFactory.getSkill(400021113).getEffect(1);
                ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                for (MapleMonster m : player.getMap().getAllMonster()) {
                    if (!ef.calculateBoundingBox(attack.position, true).contains(m.getPosition()) && !ef.calculateBoundingBox(attack.position, false).contains(m.getPosition())) {
                        continue;
                    }
                    mobList.add(new Triple<Integer, Integer, Integer>(m.getObjectId(), 0, 0));
                }
                player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400021113, mobList, false, 0, new int[0]));
                player.setSkillCustomInfo(400021113, 0L, 600L);
            }
        } else if (GameConstants.isWildHunter(player.getJob())) {
            if (player.getBuffedValue(SecondaryStat.JaguarSummoned) != null) {
                player.setSkillCustomInfo(33001001, 0L, 10000L);
                player.getClient().send(CField.SummonPacket.JaguarAutoAttack(true));
            }
            if (attack.skill == 33121214 && player.getSkillCustomValue(33121214) == null) {
                SecondaryStatEffect eff = SkillFactory.getSkill(33121214).getEffect(player.getSkillLevel(33121214));
                ArrayList<Integer> objid = new ArrayList<Integer>();
                ArrayList<MapleMonster> attackk = new ArrayList<MapleMonster>();
                int i = 0;
                for (AttackPair oned1 : attack.allDamage) {
                    objid.add(oned1.objectId);
                }
                List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 400000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
                for (MapleMapObject m : player.getMap().getMapObjectsInRange(player.getTruePosition(), 400000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}))) {
                    MapleMonster mob = (MapleMonster) m;
                    if (mob == null) {
                        continue;
                    }
                    boolean attacked = true;
                    for (Integer a2 : objid) {
                        if (a2.intValue() != mob.getObjectId()) {
                            continue;
                        }
                        attacked = false;
                        break;
                    }
                    if (!attacked) {
                        continue;
                    }
                    attackk.add(mob);
                    if (++i < eff.getQ()) {
                        continue;
                    }
                    break;
                }
                if (!attackk.isEmpty()) {
                    for (MapleMonster m : attackk) {
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(33121019, m.getPosition(), 1, 0, 1))));
                    }
                    player.setSkillCustomInfo(attack.skill, 0L, eff.getY() * 1000);
                }
            }
        } else if (GameConstants.isMichael(player.getJob())) {
            if (player.skillisCooling(400011032) && player.getSkillCustomValue(400011033) == null) {
                ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
                player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011033, mobList, true, 0, new int[0]));
                player.setSkillCustomInfo(400011033, 0L, 5000L);
            }
        } else if (GameConstants.isEunWol(player.getJob())) {
            SecondaryStatEffect effect2;
            if (player.getSkillLevel(20050285) > 0) {
                SecondaryStatEffect effect22 = SkillFactory.getSkill(20050285).getEffect(1);
                if (!GameConstants.is_forceAtom_attack_skill(attack.skill)) {
                    player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) effect22.getX());
                }
            }
            if (player.getBuffedValue(25121133) && monster != null) {
                final SecondaryStatEffect effect7 = SkillFactory.getSkill(25121133).getEffect(1);
                if (effect7.makeChanceResult()) {
                    final Item toDrop2 = new Item(2434851, (short) 0, (short) 1, 0);
                    monster.getMap().spawnItemDrop(monster, player, toDrop2, new Point(monster.getTruePosition().x, monster.getTruePosition().y), true, false);
                }
            }
        } else if (GameConstants.isIllium(player.getJob())) {
            if (attack.skill == 152110004) {
                ArrayList<Integer> monsters = new ArrayList<Integer>();
                MapleAtom atom = new MapleAtom(false, player.getObjectId(), 38, true, 152120016, player.getPosition().x, player.getPosition().y);
                for (int i2 = 0; i2 < 3; ++i2) {
                    monsters.add(monster.getObjectId());
                    ForceAtom forceAtom = new ForceAtom(2, Randomizer.rand(40, 48), Randomizer.rand(5, 6), Randomizer.rand(14, 256), 0);
                    atom.addForceAtom(forceAtom);
                }
                atom.setDwTargets(monsters);
                player.getMap().spawnMapleAtom(atom);
            }
        } else if (GameConstants.isKadena(player.getJob())) {
            if (attack.skill == 64121020 && player.getSkillLevel(400041074) > 0) {
                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                SecondaryStatEffect effect2 = SkillFactory.getSkill(400041074).getEffect(player.getSkillLevel(400041074));
                player.setSkillCustomInfo(64121020, player.getSkillCustomValue0(64121020) + 1L, 0L);
                if (player.getSkillCustomValue0(64121020) > (long) effect2.getW()) {
                    player.setSkillCustomInfo(64121020, effect2.getW(), 0L);
                }
                statups.put(SecondaryStat.WeaponVarietyFinale, new Pair<Integer, Integer>((int) player.getSkillCustomValue0(400041074), 0));
                player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect2, player));
                if (player.getSkillCustomValue0(400041074) > 0L && player.getSkillCustomValue0(64121020) >= (long) effect2.getW()) {
                    player.removeSkillCustomInfo(64121020);
                    PlayerHandler.Vmatrixstackbuff(player.getClient(), true, null);
                    player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400041074, Arrays.asList(new RangeAttack(400041074, monster != null ? monster.getPosition() : attack.position, 0, 0, 5))));
                }
            }
        } else if (GameConstants.isYeti(player.getJob())) {
            if (monster != null) {
                if (player.getBuffedValue(135001015) && player.getSkillCustomValue(135001015) == null && SkillFactory.getSkill(135001015).getSkillList().contains(attack.skill)) {
                    ArrayList<Integer> monsters = new ArrayList<Integer>();
                    MapleAtom atom = new MapleAtom(false, player.getObjectId(), 60, true, 135002015, player.getPosition().x, player.getPosition().y);
                    for (MapleMonster mob : player.getMap().getAllMonster()) {
                        monsters.add(mob.getObjectId());
                    }
                    for (int i = 0; i < 3; ++i) {
                        ForceAtom forceAtom = new ForceAtom(1, 42 + i, 3, Randomizer.rand(59, 131), 0);
                        atom.addForceAtom(forceAtom);
                        player.getYetiGauge(135001015, 0);
                    }
                    atom.setDwTargets(monsters);
                    player.getMap().spawnMapleAtom(atom);
                    player.setSkillCustomInfo(135001015, 0L, player.getBuffedEffect(135001015).getY() * 1000);
                }
                player.getYetiGauge(attack.skill, monster.getStats().isBoss() ? 2 : 0);
            }
            if (attack.skill == 135001007 || attack.skill == 135001010) {
                PlayerHandler.Vmatrixstackbuff(player.getClient(), true, null);
            }
        } else if (GameConstants.isZero(player.getJob())) {
            SecondaryStatEffect eff;
            if (player.getSkillLevel(101110205) > 0 && attack.targets > 0 && player.getGender() == 0 && Randomizer.isSuccess((eff = SkillFactory.getSkill(101110205).getEffect(101110205)).getY())) {
                player.getClient().send(CField.getTpAdd(20, eff.getZ()));
                player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 101110205, 4, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null, null), false);
            }
        } else if (GameConstants.isPinkBean(player.getJob()) && monster != null && player.getSkillLevel(131000016) > 0 && player.getSkillCustomValue(131000016) == null && attack.skill != 131000016 && Randomizer.isSuccess(50)) {
            SecondaryStatEffect eff = SkillFactory.getSkill(131003016).getEffect(1);
            ArrayList<Integer> monsters = new ArrayList<Integer>();
            MapleAtom atom = new MapleAtom(false, player.getObjectId(), 65, true, 131003016, player.getPosition().x, player.getPosition().y);
            for (MapleMonster mob : player.getMap().getAllMonster()) {
                if (!eff.calculateBoundingBox(attack.position, (attack.facingleft >>> 4 & 0xF) == 0).contains(mob.getPosition())) {
                    continue;
                }
                monsters.add(mob.getObjectId());
            }
            for (int i = 0; i < 4; ++i) {
                ForceAtom forceAtom = new ForceAtom(1, Randomizer.rand(40, 44), Randomizer.rand(3, 4), Randomizer.rand(25, 345), 0);
                forceAtom.setnAttackCount(15 + i);
                atom.addForceAtom(forceAtom);
            }
            atom.setDwTargets(monsters);
            player.getMap().spawnMapleAtom(atom);
            player.setSkillCustomInfo(131000016, 0L, 10000L);
        }
        if (player.getBuffedValue(13121054) && attack.skill != 13121054 && !GameConstants.isTryFling(attack.skill) && attack.targets > 0 && Randomizer.isSuccess(30)) {
            MapleAtom atom = new MapleAtom(false, player.getId(), 8, true, 13121054, player.getTruePosition().x, player.getTruePosition().y);
            atom.setDwFirstTargetId(0);
            ForceAtom ft = new ForceAtom(1, 1, Randomizer.rand(5, 7), 270, 66);
            ft.setnStartY(ft.getnStartY() + Randomizer.rand(-110, 110));
            atom.addForceAtom(ft);
            player.getMap().spawnMapleAtom(atom);
        }
        if (attack.skill == 155001000) {//아크
            SkillFactory.getSkill(155001001).getEffect(attack.skilllevel).applyTo(player, false);
        }
        if (attack.skill == 155101002) {
            SkillFactory.getSkill(155101003).getEffect(attack.skilllevel).applyTo(player, false);
        }
        if (attack.skill == 155111003) {
            SkillFactory.getSkill(155111005).getEffect(attack.skilllevel).applyTo(player, false);
        }
        if (attack.skill == 155121003) {
            SkillFactory.getSkill(155121005).getEffect(attack.skilllevel).applyTo(player, false);
        }
        if (GameConstants.isHoyeong(player.getJob())) {
            player.giveHoyoungGauge(attack.skill);
            if (player.getBuffedValue(SecondaryStat.ButterflyDream) != null && SkillFactory.getSkill(164121007).getSkillList().contains(attack.skill) && player.getSkillCustomValue(164121007) == null) {
                player.getMap().broadcastMessage(SkillPacket.권술호접지몽(player));
                player.setSkillCustomInfo(164121007, 0L, 1000L);
            }
            if (player.getBuffedValue(400041052) && attack.targets != 0) {
                player.setInfinity((byte) (player.getInfinity() + 1));
                if (player.getInfinity() == 12) {
                    for (MapleSummon summon5 : player.getSummons()) {
                        if (summon5.getSkill() != 400041052) {
                            continue;
                        }
                        player.setInfinity((byte) 0);
                        player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.DeathAttack(summon5, Randomizer.rand(8, 10)));
                        break;
                    }
                }
            }
        } else if (GameConstants.isArk(player.getJob())) {
            if (attack.targets > 0 && (attack.skill == 155001100 || attack.skill == 155101100 || attack.skill == 155101101 || attack.skill == 155101112 || attack.skill == 155111102 || attack.skill == 155121102 || attack.skill == 155111111 || attack.skill == 155101212 || attack.skill == 155111211 || attack.skill == 155101214 || attack.skill == 155121215)) {
                player.addSpell(attack.skill);
            }
            if (player.getSkillCustomValue0(400051080) > 0L) {
                if (attack.skill == 400051080) {
                    player.removeSkillCustomInfo(attack.skill);
                }
                if (SkillFactory.getSkill(400051080).getSkillList().contains(attack.skill)) {
                    player.getClient().send(CField.getEarlySkillActive(600));
                } else if (SkillFactory.getSkill(400051080).getSkillList2().contains(attack.skill)) {
                    player.getClient().send(CField.getEarlySkillActive(180));
                }
            }
            if (player.skillisCooling(400051047) || player.skillisCooling(400051048)) {
                SecondaryStatEffect eff = SkillFactory.getSkill(400051047).getEffect(player.getSkillLevel(400051047));
                if (player.skillisCooling(400051047)) {
                    if (SkillFactory.getSkill(400051047).getSkillList().contains(attack.skill) && !player.getWeaponChanges().contains(attack.skill)) {
                        player.getWeaponChanges().add(attack.skill);
                        player.changeCooldown(400051047, -(eff.getX() * 1000));
                    }
                } else if (player.skillisCooling(400051048) && SkillFactory.getSkill(400051048).getSkillList().contains(attack.skill) && !player.getWeaponChanges2().contains(attack.skill)) {
                    player.getWeaponChanges2().add(attack.skill);
                    player.changeCooldown(400051048, -(eff.getX() * 1000));
                }
            }
            if (attack.isLink) {
                if (player.getBuffedValue(SecondaryStat.FightJazz) == null) {
                    player.setSkillCustomInfo(155120015, 0L, 0L);
                }
                if (player.getSkillCustomValue0(155120015) <= 2L) {
                    player.setSkillCustomInfo(155120015, player.getSkillCustomValue0(155120015) + 1L, 0L);
                }
                SkillFactory.getSkill(155120014).getEffect(player.getSkillLevel(155120014)).applyTo(player);
            }
            if (attack.skill == 155101100 || attack.skill == 155101101) {
                player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(155101013, attack.position, 0, 0, 1))));
            } else if (attack.skill == 155101112) {
                player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(155101015, attack.position, 0, 0, 1))));
            } else if (attack.skill == 155121102) {
                player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(155121002, attack.position, 0, 0, 1))));
            }
            if (attack.skill == 155121003 && monster != null) {
                SecondaryStatEffect a = SkillFactory.getSkill(155121004).getEffect(GameConstants.getLinkedSkill(155121004));
                MapleMist mist = new MapleMist(a.calculateBoundingBox(monster.getTruePosition(), player.isFacingLeft()), player, a, 3000, (byte) (player.isFacingLeft() ? 1 : 0));
                mist.setDelay(0);
                player.getMap().spawnMist(mist, false);
            }
        }
        if (player.getSkillLevel(400041063) > 0 && attack.targets > 0 && !player.skillisCooling(400041067)) {
            ArrayList<Integer> sungi_skills = new ArrayList<Integer>();
            Point pos = null;
            Point pos1 = null;
            for (MapleMonster mob : player.getMap().getAllMonster()) {
                if (!mob.isAlive() || player.getPosition().x + 500 < mob.getPosition().x || player.getPosition().x - 500 > mob.getPosition().x || player.getPosition().y + 300 < mob.getPosition().y || player.getPosition().y - 300 > mob.getPosition().y) {
                    continue;
                }
                if (pos == null) {
                    pos = mob.getPosition();
                    continue;
                }
                if (pos1 != null) {
                    continue;
                }
                pos1 = mob.getPosition();
                break;
            }
            if (pos == null) {
                pos = player.getPosition();
            } else if (pos1 == null) {
                pos1 = pos;
            }
            Integer[] skills = new Integer[]{164001000, 164001002, 164101000, 164111000, 164111003, 164111008, 164121000, 164121003, 164121005};
            if (Arrays.asList(skills).contains(attack.skill)) {
                SecondaryStatEffect sungi = SkillFactory.getSkill(400041063).getEffect(player.getSkillLevel(400041063));
                if (!player.useChun) {
                    sungi_skills.add(400041064);
                }
                if (!player.useJi) {
                    sungi_skills.add(400041065);
                }
                if (!player.useIn) {
                    sungi_skills.add(400041066);
                }
                Collections.addAll(sungi_skills, new Integer[0]);
                Collections.shuffle(sungi_skills);
                if (player.getBuffedEffect(SecondaryStat.SageElementalClone) != null && System.currentTimeMillis() - player.lastSungiAttackTime >= (long) (sungi.getV2() * 1000)) {
                    if (!sungi_skills.isEmpty()) {
                        player.lastSungiAttackTime = System.currentTimeMillis();
                        int i = 0;
                        Iterator m = sungi_skills.iterator();
                        while (m.hasNext()) {
                            int sungi_skill = (Integer) m.next();
                            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(sungi_skill, Arrays.asList(new RangeAttack(sungi_skill, i == 0 ? pos : pos1, 1, 0, 1))));
                            ++i;
                        }
                        player.addCooldown(400041067, System.currentTimeMillis(), 2000L);
                        player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400041067, 2000));
                    }
                } else if (player.getBuffedEffect(SecondaryStat.SageElementalClone) == null && System.currentTimeMillis() - player.lastSungiAttackTime >= (long) (sungi.getQ() * 1000)) {
                    int i = 0;
                    if (!sungi_skills.isEmpty()) {
                        player.lastSungiAttackTime = System.currentTimeMillis();
                        int sungi_skill = (Integer) sungi_skills.get(Randomizer.nextInt(sungi_skills.size()));
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(sungi_skill, Arrays.asList(new RangeAttack(sungi_skill, i == 0 ? pos : pos1, 1, 0, 1))));
                        ++i;
                    }
                    player.addCooldown(400041067, System.currentTimeMillis(), 5000L);
                    player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400041067, 5000));
                }
            }
        }
        for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
            if (mist.getSource() == null || mist.getSourceSkill().getId() != attack.skill) {
                continue;
            }
            return;
        }
        if (attack.targets > 0 && player.getSkillCustomValue0(400031053) > 0L && player.getBuffedValue(400031053) && player.getSkillCustomValue(400031054) == null && !GameConstants.is_forceAtom_attack_skill(attack.skill)) {
            SecondaryStatEffect mirage = player.getBuffedEffect(SecondaryStat.SilhouetteMirage);
            MapleAtom atom = new MapleAtom(false, player.getId(), 56, true, 400031054, player.getTruePosition().x, player.getTruePosition().y);
            player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 400031053, 10, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getPosition(), null, null));
            player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 400031053, 10, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getPosition(), null, null), false);
            for (int i = 0; i < mirage.getBulletCount(); ++i) {
                atom.addForceAtom(new ForceAtom(3, Randomizer.rand(21, 29), Randomizer.rand(15, 16), Randomizer.rand(170, 172), (short) (90 + i * 210)));
            }
            player.setSkillCustomInfo(400031054, 0L, (int) player.getBuffedEffect(400031053).getT() * 1000);
            atom.setDwFirstTargetId(0);
            player.getMap().spawnMapleAtom(atom);
        }
        if (attack.skill == 64001009 || attack.skill == 64001010 || attack.skill == 64001011 || attack.skill == 1100012 || attack.skill == 3111010 || attack.skill == 3211010) {
            if (attack.skill == 1100012 || attack.skill == 3111010 || attack.skill == 3211010) {
                player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0, monster != null ? monster.getObjectId() : 0, (byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), true, monster != null ? monster.getPosition() : attack.position, null, null));
                player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0, monster != null ? monster.getObjectId() : 0, (byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), false, monster != null ? monster.getPosition() : attack.position, null, null), false);
            } else {
                player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0, 0, (byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), true, attack.chain, null, null));
                player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0, 0, (byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), false, attack.chain, null, null), false);
            }
        }
        if (totDamage > 0L && player.getBuffedValue(400031047) && !player.getBuffedValue(400031048)) {
            SkillFactory.getSkill(400031048).getEffect(player.getSkillLevel(400031047)).applyTo(player, false);
        }
        if (attack.skill == 400021092 && player.getBuffedEffect(400021092) != null) {
            SecondaryStatEffect mischief = player.getBuffedEffect(400021092);
            if (player.getSkillCustomValue0(400021093) < (long) mischief.getZ()) {
                player.addSkillCustomInfo(400021093, 1L);
            }
            HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
            statups.put(SecondaryStat.SalamanderMischief, new Pair<Integer, Integer>((int) player.getSkillCustomValue0(400021093), (int) player.getBuffLimit(400021092)));
            player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, mischief, player));
        }
        if (attack.targets > 0 && (player.getBuffedValue(400031049) || player.getBuffedValue(400031051)) && player.getSkillCustomValue(400031049) == null) {
            for (MapleSummon s : player.getMap().getAllSummonsThreadsafe()) {
                if (s.getOwner().getId() != player.getId() || s.getSkill() != 400031049 && s.getSkill() != 400031051 || s == null || attack.skill == 400031049 || attack.skill == 400031050 || attack.skill == 400031051) {
                    continue;
                }
                player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.summonRangeAttack(s, s.getSkill()));
                player.setSkillCustomInfo(400031049, 0L, s.getSkill() == 400031051 ? 5000L : 1200L);
            }
        }
        if (GameConstants.isNightLord(player.getJob()) && !GameConstants.is_forceAtom_attack_skill(attack.skill) && attack.skill < 400001001 && Randomizer.isSuccess(SkillFactory.getSkill(4110012).getEffect(player.getSkillLevel(4110012)).getProp())) {
            player.getClient().send(CField.getExpertThrow());
            player.setSkillCustomInfo(4110012, 1L, 0L);
        }
        if (player.getBuffedValue(11121005) && attack.summonattack == 0) {
            if (GameConstants.isPollingmoonAttackskill(attack.skill)) {
                SkillFactory.getSkill(11121011).getEffect(20).applyTo(player);
                if (player.getSkillLevel(400011048) > 0 && player.skillisCooling(400011048) && attack.targets > 0) {
                    player.changeCooldown(400011048, -300);
                }
            } else if (GameConstants.isRisingsunAttackskill(attack.skill)) {
                SkillFactory.getSkill(11121012).getEffect(20).applyTo(player);
                if (player.getSkillLevel(400011048) > 0 && player.skillisCooling(400011048) && attack.targets > 0) {
                    player.changeCooldown(400011048, -300);
                }
            }
        }
        if (multikill > 0) {
            player.CombokillHandler(monster, 1, multikill);
        }
    }

    private static void parseFinalAttack(MapleCharacter player, AttackInfo attack, MapleMonster monster) {
        int[][] finalAttackReq = new int[][]{{1100002, 1120013}, {1200002, 0}, {1300002, 0}, {2121007, 2120013}, {2221007, 2220014}, {3100001, 3120008}, {3200001, 0}, {4341054, 0}, {5121013, 0}, {5220020, 0}, {5311004, 1}, {11101002, 0}, {21100010, 21120012}, {22000015, 22110021}, {23100006, 23120012}, {31220007, 0}, {32121004, 32121011}, {33100009, 33120011}, {37000007, 0}, {51100002, 51120002}};
        Item weapon = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
        for (int[] skill : finalAttackReq) {
            Integer value;
            if (weapon == null || attack.skill / 10000 == 8000 || attack.skill / 10000 > player.getJob()) {
                continue;
            }
            int finalSkill = GameConstants.getLinkedSkill(skill[0]);
            int advSkill = GameConstants.getLinkedSkill(skill[1]);
            SecondaryStatEffect eff = null;
            if (SkillFactory.getSkill(advSkill) != null && player.getSkillLevel(advSkill) > 0) {
                eff = SkillFactory.getSkill(advSkill).getEffect(player.getSkillLevel(advSkill));
            } else if (SkillFactory.getSkill(finalSkill) != null && player.getSkillLevel(finalSkill) > 0) {
                eff = SkillFactory.getSkill(finalSkill).getEffect(player.getSkillLevel(finalSkill));
            }
            if (advSkill == attack.skill || finalSkill == attack.skill || (advSkill == 2120013 || advSkill == 2220014 || advSkill == 32121011) && !player.skillisCooling(finalSkill) || finalSkill == 4341054 && player.getBuffedEffect(SecondaryStat.WindBreakerFinal) == null || finalSkill == 5311004 && ((value = player.getBuffedValue(SecondaryStat.Roulette)) == null || value != 1)) {
                break;
            }
            if (eff == null) {
                continue;
            }
            int chance = eff.getProp();
            int attackCount = Math.max(eff.getAttackCount(), 1);
            if (player.getBuffedEffect(SecondaryStat.FinalAttackProp) != null) {
                chance += player.getBuffedValue(SecondaryStat.FinalAttackProp).intValue();
            }
            if (eff.getSourceId() == 1120013 && player.getSkillLevel(1120048) > 0) {
                chance += SkillFactory.getSkill(1120048).getEffect(player.getSkillLevel(1120048)).getProp();
            }
            if (player.getBuffedValue(33121054)) {
                chance = 100;
            }

            if (!Randomizer.isSuccess(chance)) {
                break;
            }
            player.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(attackCount, attack.skill, eff.getSourceId(), (weapon.getItemId() - 1000000) / 10000, monster));
            break;
        }
    }

    public static final void applyAttackMagic(AttackInfo attack, Skill theSkill, MapleCharacter player, SecondaryStatEffect effect, double maxDamagePerHit) {
        SecondaryStatEffect stst;
        MapleSummon summon;
        SecondaryStatEffect arcaneAim;
        if (attack.summonattack == 0) {
            // empty if block
        }
        player.checkSpecialCoreSkills("prob", 0, effect);
        if (attack.skill != 0) {
            if (effect == null) {
                player.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(player));
                return;
            }
            player.checkSpecialCoreSkills("cooltime", 0, effect);
            if (GameConstants.isMulungSkill(attack.skill)) {
                if (player.getMapId() / 10000 != 92502) {
                    return;
                }
                if (player.getMulungEnergy() < 10000) {
                    return;
                }
            } else if (GameConstants.isPyramidSkill(attack.skill)) {
                if (player.getMapId() / 1000000 != 926) {
                    return;
                }
            } else if (GameConstants.isInflationSkill(attack.skill)) {
                if (player.getBuffedValue(SecondaryStat.Inflation) == null) {
                    return;
                }
            } else if (!GameConstants.isNoApplySkill(attack.skill)) {
                Integer plusCount;
                SecondaryStatEffect oldEffect = SkillFactory.getSkill(attack.skill).getEffect(attack.skilllevel);
                int target = oldEffect.getMobCount();
                for (Skill skill : player.getSkills().keySet()) {
                    int bonusSkillLevel = player.getSkillLevel(skill);
                    if (bonusSkillLevel <= 0 || skill.getId() == attack.skill) {
                        continue;
                    }
                    SecondaryStatEffect bonusEffect = skill.getEffect(bonusSkillLevel);
                    target += bonusEffect.getTargetPlus();
                    target += bonusEffect.getTargetPlus_5th();
                }
                if (oldEffect.getMobCount() > 0 && player.getSkillLevel(70000047) > 0) {
                    target += SkillFactory.getSkill(70000047).getEffect(player.getSkillLevel(70000047)).getTargetPlus();
                }
                boolean useBulletCount = oldEffect.getBulletCount() > 1;
                int attackCount = useBulletCount ? oldEffect.getBulletCount() : oldEffect.getAttackCount();
                int bulletBonus = GameConstants.bullet_count_bonus(attack.skill);
                int attackBonus = GameConstants.attack_count_bonus(attack.skill);
                if (bulletBonus != 0 && useBulletCount) {
                    if (player.getSkillLevel(bulletBonus) > 0) {
                        attackCount += SkillFactory.getSkill(bulletBonus).getEffect(player.getSkillLevel(bulletBonus)).getBulletCount();
                    }
                } else if (attackBonus != 0 && !useBulletCount && player.getSkillLevel(attackBonus) > 0) {
                    attackCount += SkillFactory.getSkill(attackBonus).getEffect(player.getSkillLevel(attackBonus)).getAttackCount();
                }
                if ((plusCount = player.getBuffedValue(SecondaryStat.Buckshot)) != null) {
                    attackCount *= plusCount.intValue();
                }
                if (player.getBuffedEffect(SecondaryStat.ShadowPartner) != null || player.getBuffedEffect(SecondaryStat.Larkness) != null) {
                    attackCount *= 2;
                }
                if (player.getSkillLevel(3220015) > 0 && attackCount >= 2) {
                    attackCount += SkillFactory.getSkill(3220015).getEffect(player.getSkillLevel(3220015)).getX();
                }
                if (player.getBuffedEffect(SecondaryStat.VengeanceOfAngel) != null && attack.skill == 2321007) {
                    attackCount += player.getBuffedEffect(SecondaryStat.VengeanceOfAngel).getY();
                }
                Integer attackCountX = player.getBuffedValue(SecondaryStat.AttackCountX);
                int[] blowSkills = new int[]{32001000, 32101000, 32111002, 32121002, 400021007};
                if (attackCountX != null) {
                    for (int blowSkill : blowSkills) {
                        if (attack.skill != blowSkill) {
                            continue;
                        }
                        attackCount += attackCountX.intValue();
                    }
                }
                if (attack.targets > target) {
                    player.dropMessageGM(-5, attack.skill + " 몹 개체수 > 클라이언트 계산 : " + attack.targets + " / 서버 계산 : " + target);
                    player.dropMessageGM(-6, "개체수가 계산값보다 많습니다.");
                }
                if (attack.hits > attackCount) {
                    player.dropMessageGM(-5, attack.skill + " 공격 횟수 > 클라이언트 계산 : " + attack.hits + " / 서버 계산 : " + attackCount);
                    player.dropMessageGM(-6, "공격 횟수가 계산값보다 많습니다.");
                }
            }
        }
        MapleMonster monster = null;
        PlayerStats stats = player.getStat();
        Element element = player.getBuffedValue(SecondaryStat.ElementalReset) != null ? Element.NEUTRAL : theSkill.getElement();
        double MaxDamagePerHit = 0.0;
        long totDamage = 0L;
        boolean heiz = false;
        int multikill = 0;
        short CriticalDamage = stats.critical_rate;
        MapleMap map = player.getMap();
        for (AttackPair oned : attack.allDamage) {
            monster = map.getMonsterByOid(oned.objectId);
            if (monster == null || monster.getLinkCID() > 0) {
                continue;
            }
            boolean Tempest = false;
            long totDamageToOneMonster = 0L;
            MapleMonsterStats monsterstats = monster.getStats();
            long fixeddmg = monsterstats.getFixedDamage();
            int overallAttackCount = 0;
            if (monster.getId() >= 9833070 && monster.getId() <= 9833074) {
                continue;
            }
            for (Pair<Long, Boolean> eachde : oned.attack) {
                long eachd = (Long) eachde.left;
                overallAttackCount = (byte) (overallAttackCount + 1);
                if (fixeddmg != -1L) {
                    eachd = monsterstats.getOnlyNoramlAttack() ? 0L : fixeddmg;
                } else if (monsterstats.getOnlyNoramlAttack()) {
                    eachd = 0L;
                }
                totDamageToOneMonster += eachd;
                player.checkSpecialCoreSkills("attackCount", monster.getObjectId(), effect);
                player.checkSpecialCoreSkills("attackCountMob", monster.getObjectId(), effect);
            }
            totDamage += totDamageToOneMonster;
            
            if (!player.gethottimebossattackcheck()) {
                    player.sethottimebossattackcheck(true);
                }
            
            if (monster.getId() != 8900002 && monster.getId() != 8900102) {
                player.checkMonsterAggro(monster);
            }
            if (!(attack.skill == 0 || SkillFactory.getSkill(attack.skill).isChainAttack() || effect.isMist() || effect.getSourceId() == 400021030 || GameConstants.isLinkedSkill(attack.skill) || GameConstants.isNoApplySkill(attack.skill) || GameConstants.isNoDelaySkill(attack.skill) || monster.getStats().isBoss() || !(player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange)))) {
                player.dropMessageGM(-5, "타겟이 범위를 벗어났습니다.");
            }
            if (player.getSkillLevel(80002762) > 0) {
                if (player.getBuffedEffect(SecondaryStat.EmpiricalKnowledge) != null && player.empiricalKnowledge != null) {
                    if (map.getMonsterByOid(player.empiricalKnowledge.getObjectId()) != null) {
                        if (monster.getObjectId() != player.empiricalKnowledge.getObjectId() && monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
                            player.empiricalStack = 0;
                            player.empiricalKnowledge = monster;
                        }
                    } else {
                        player.empiricalStack = 0;
                        player.empiricalKnowledge = monster;
                    }
                } else if (player.empiricalKnowledge != null) {
                    if (monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
                        player.empiricalKnowledge = monster;
                    }
                } else {
                    player.empiricalKnowledge = monster;
                }
            }
            if (totDamageToOneMonster <= 0L && attack.skill != 27101101) {
                continue;
            }
            monster.damage(player, totDamageToOneMonster, true, attack.skill);
            if (monster.getId() >= 9500650 && monster.getId() <= 9500654 && totDamageToOneMonster > 0L && player.getGuild() != null) {
                player.getGuild().updateGuildScore(totDamageToOneMonster);
            }
            if (player.getBuffedValue(400021073)) {
                ArrayList atoms = new ArrayList();
                MapleSummon s = null;
                for (MapleSummon summon2 : player.getSummons()) {
                    if (summon2.getSkill() != 400021073) {
                        continue;
                    }
                    s = summon2;
                }
                if (s == null) {
                    player.dropMessage(6, "Zodiac Ray Null Point");
                } else {
                    MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 29, true, 400021073, monster.getTruePosition().x, monster.getTruePosition().y);
                    atom.setDwUserOwner(player.getId());
                    atom.setDwFirstTargetId(0);
                    atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
                    player.getMap().spawnMapleAtom(atom);
                    player.setEnergyBurst(player.getEnergyBurst() + 1);
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.updateSummon(s, 13));
                    HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(player.getEnergyBurst() + 21, (int) player.getBuffLimit(400021073)));
                    player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(400021073), player));
                }
            }
            if (GameConstants.isFusionSkill(attack.skill) && attack.targets > 0 && player.getSkillCustomValue(22170070) == null) {
                SecondaryStatEffect magicWreck = player.getSkillLevel(22170070) > 0 ? SkillFactory.getSkill(22170070).getEffect(player.getSkillLevel(22170070)) : SkillFactory.getSkill(22141017).getEffect(player.getSkillLevel(22141017));
                if (player.getMap().getWrecks().size() < 15) {
                    int x = Randomizer.rand(-100, 150);
                    int y = Randomizer.rand(-50, 70);
                    MapleMagicWreck mw = new MapleMagicWreck(player, magicWreck.getSourceId(), new Point(monster.getTruePosition().x + x, monster.getTruePosition().y + y), 20000);
                    player.getMap().spawnMagicWreck(mw);
                    player.setSkillCustomInfo(22170070, 0L, player.getSkillLevel(22170070) > 0 ? 400L : 600L);
                }
            }
            if (player.getSkillLevel(32101009) > 0 && !monster.isAlive() && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
                player.addHP(player.getStat().getCurrentMaxHp() * (long) SkillFactory.getSkill(32101009).getEffect(player.getSkillLevel(32101009)).getKillRecoveryR() / 100L);
            }
            if (monster.isBuffed(MonsterStatus.MS_MCounter) && player.getBuffedEffect(SecondaryStat.IgnorePImmune) == null && player.getBuffedEffect(SecondaryStat.IgnorePCounter) == null && player.getBuffedEffect(SecondaryStat.IgnoreAllCounter) == null && player.getBuffedEffect(SecondaryStat.IgnoreAllImmune) == null && !SkillFactory.getSkill(attack.skill).isIgnoreCounter()) {
                player.addHP(-monster.getBuff(MonsterStatus.MS_MCounter).getValue());
            }
            player.dropMessageGM(5, "\ub9e4\uc9c1 \uc2a4\ud0ac(" + SkillFactory.getSkill(attack.skill).getName() + ") : " + attack.skill + "");
            switch (attack.skill) {
                case 2101004:
                case 2111002:
                case 2121005:
                case 2121006:
                case 2121007:
                case 400021001: {
                    if (player.getBuffedEffect(SecondaryStat.WizardIgnite) == null || !player.getBuffedEffect(SecondaryStat.WizardIgnite).makeChanceResult()) {
                        break;
                    }
                    SkillFactory.getSkill(2100010).getEffect(player.getSkillLevel(2101010)).applyTo(player, monster.getTruePosition());
                }
            }
            if (effect != null && monster.isAlive()) {
                Object ignition;
                ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
                ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                boolean suc = effect.makeChanceResult();
                switch (attack.skill) {
                    case 2101004:
                    case 2111002:
                    case 2121005:
                    case 2121006:
                    case 2121007: {
                        if (attack.skill != 2121006) {
                            break;
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                        break;
                    }
                    case 2121011: {
                        player.setDotDamage((long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L);
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Showdown, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(effect.getX())));
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), player.getDotDamage()));
                        player.setFlameHeiz(monster.getTruePosition());
                        heiz = true;
                        break;
                    }
                    case 2101005: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                        break;
                    }
                    case 2111003: {
                        if (monster.isBuffed(2121011)) {
                            break;
                        }
                        SecondaryStatEffect bonusTime = null;
                        SecondaryStatEffect bonusDam = null;
                        if (player.getSkillLevel(2120044) > 0) {
                            bonusTime = SkillFactory.getSkill(2120044).getEffect(player.getSkillLevel(2120044));
                        }
                        if (player.getSkillLevel(2120045) > 0) {
                            bonusDam = SkillFactory.getSkill(2120045).getEffect(player.getSkillLevel(2120045));
                        }
                        player.setDotDamage((long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L);
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime() + (bonusTime != null ? bonusTime.getDOTTime() : (short) 0)), player.getDotDamage()));
                        break;
                    }
                    case 2121055: {
                        suc = true;
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                        break;
                    }
                    case 2201004:
                    case 2201008:
                    case 2201009:
                    case 2211002:
                    case 2211006:
                    case 2211010:
                    case 2220014:
                    case 2221003:
                    case 2221011:
                    case 2221012:
                    case 2221054:
                    case 400020002: {
                        if (attack.skill != 2221011) {
                            statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(attack.skill, 10000), Long.valueOf(effect.getV())));
                            if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
                                monster.setFreezingOverlap(0);
                            }
                            if (monster.getFreezingOverlap() < 5) {
                                monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() + 1));
                            }
                        }
                        if (attack.skill == 2221011) {
                            statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, 13000), 1L));
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getX())));
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getY())));
                        break;
                    }
                    case 2201005:
                    case 2211003:
                    case 2211011:
                    case 2221006: {
                        if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
                            monster.setFreezingOverlap(0);
                        }
                        if (monster.getFreezingOverlap() > 0) {
                            monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
                            if (monster.getFreezingOverlap() <= 0) {
                                monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
                            } else {
                                statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
                            }
                        }
                        if (attack.skill != 2221006) {
                            break;
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                        break;
                    }
                    case 2221052:
                    case 400021031:
                    case 400021094: {
                        if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
                            monster.setFreezingOverlap(0);
                        }
                        if (attack.skill == 400021094) {
                            monster.addSkillCustomInfo(400021094, 1L);
                            if (monster.getFreezingOverlap() > 0) {
                                if (attack.skill == 400021094 && monster.getCustomValue0(400021094) >= 5L) {
                                    monster.removeCustomInfo(400021094);
                                    monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
                                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
                                }
                            } else {
                                monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
                            }
                        }
                        if (monster.getFreezingOverlap() > 0) {
                            monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
                            statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
                            break;
                        }
                        if (monster.getFreezingOverlap() > 0) {
                            break;
                        }
                        monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
                        break;
                    }
                    case 2311004: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                        break;
                    }
                    case 2321007: {
                        SecondaryStatEffect effect2 = SkillFactory.getSkill(attack.skill).getEffect(player.getSkillLevel(attack.skill));
                        if (monster.getBuff(MonsterStatus.MS_IndieUNK) == null) {
                            monster.removeCustomInfo(attack.skill);
                        }
                        if (monster.getCustomValue0(attack.skill) < (long) effect2.getQ()) {//MS_IndiePdr
                            monster.addSkillCustomInfo(attack.skill, 1L);
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieUNK, new MonsterStatusEffect(attack.skill, 20000), (monster.getCustomValue0(attack.skill))));
                        //  statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(2320014, effect2.getW() * 1000), -((long)effect2.getX() * monster.getCustomValue0(2320014))));
                        break;
                    }
                    case 27101101: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                        break;
                    }
                    case 27121052:
                    case 162121041: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(effect.getDuration())));
                        break;
                    }
                    case 32101001:
                    case 32111016:
                    case 400021088: {
                        if (attack.skill == 32101001) {
                            statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, SkillFactory.getSkill(32111016).getEffect(player.getSkillLevel(32111016)).getDuration()), 1L));
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_DarkLightning, new MonsterStatusEffect(32111016, SkillFactory.getSkill(32111016).getEffect(player.getSkillLevel(32111016)).getDuration()), 1L));
                        break;
                    }
                    case 32121004:
                    case 32121011: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
                        break;
                    }
                    case 142001000:
                    case 142100000:
                    case 142100001:
                    case 142110000:
                    case 142110001: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(142110000, effect.getDOTTime()), 1L));
                        break;
                    }
                    case 142121031: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, attack.skill == 400011015 ? effect.getW() * 1000 : (effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration())), Long.valueOf(effect.getDuration())));
                        break;
                    }
                    case 400021028: {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
                        break;
                    }
                    case 400021096: {
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Treasure, new MonsterStatusEffect(attack.skill, effect.getDuration(), player.getId())));
                        break;
                    }
                    case 142120002: {
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(attack.skill, effect.getDuration(), -effect.getX())));
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(attack.skill, effect.getDuration(), -effect.getX())));
                        break;
                    }
                    default: {
                        if (!player.getBuffedValue(12101024) || monster.isBuffed(MonsterStatus.MS_Ember) || !((SecondaryStatEffect) (ignition = player.getBuffedEffect(12101024))).makeChanceResult()) {
                            break;
                        }
                        long dam = (long) ((SecondaryStatEffect) ignition).getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 1000L;
                        player.gainIgnition();
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Ember, new MonsterStatusEffect(12101024, ((SecondaryStatEffect) ignition).getDOTTime(), 1L)));
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(12101024, ((SecondaryStatEffect) ignition).getDOTTime(), player.getSkillLevel(12120050) > 0 ? dam + dam / 100L * 20L : dam)));
                        break;
                    }
                }
                for (final Triple<MonsterStatus, MonsterStatusEffect, Long> status : statusz) {
                    if (status.left != null && status.mid != null && suc) {
                        if (status.left == MonsterStatus.MS_Burned && status.right < 0L) {
                            status.right = ((long) status.right & 0xFFFFFFFFL);
                        }
                        status.mid.setValue(status.right);
                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(status.left, status.mid));
                    }
                }
                if (monster != null && monster.isAlive()) {
                    monster.applyStatus(player.getClient(), applys, effect);
                }
                if (GameConstants.isHolyAttack(attack.skill) && monster.isBuffed(MonsterStatus.MS_ElementResetBySummon)) {
                    monster.cancelStatus(MonsterStatus.MS_ElementResetBySummon, monster.getBuff(MonsterStatus.MS_ElementResetBySummon));
                }
            }
            if (player.getSkillLevel(60030241) > 0 || player.getSkillLevel(80003015) > 0) {
                int skillid;
                int n = player.getSkillLevel(60030241) > 0 ? 60030241 : (skillid = player.getSkillLevel(80003015) > 0 ? 80003015 : 0);
                if (n > 0 && monster != null) { // n
                    if (monster.getStats().isBoss()) {
                        if (monster.isAlive()) {
                            player.handlePriorPrepaRation(n, 2);
                        }
                    } else if (!monster.isAlive()) {
                        player.handlePriorPrepaRation(n, 1);
                    }
                }
            }
            if (player.getBuffedValue(SecondaryStat.BMageDeath) != null && player.skillisCooling(32001114) && GameConstants.isBMDarkAtackSkill(attack.skill) && player.getBuffedValue(SecondaryStat.AttackCountX) != null) {
                player.changeCooldown(32001114, -500);
            }
            if (player.getBuffedValue(SecondaryStat.BMageDeath) != null && (!monster.isAlive() || monster.getStats().isBoss()) && attack.skill != player.getBuffSource(SecondaryStat.BMageDeath)) {
                byte count;
                byte by = player.getBuffedValue(SecondaryStat.AttackCountX) != null ? (byte) 1 : (player.getLevel() >= 100 ? (byte) 6 : (count = player.getLevel() > 60 ? (byte) 8 : 10));
                if (player.getDeath() < by) {
                    player.setDeath((byte) (player.getDeath() + 1));
                    if (player.getDeath() >= by) {
                        player.setSkillCustomInfo(32120019, 1L, 0L);
                    }
                    HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups.put(SecondaryStat.BMageDeath, new Pair<Integer, Integer>(Integer.valueOf(player.getDeath()), 0));
                    player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(SecondaryStat.BMageDeath), player));
                }
            }
            if (attack.skill == 400021096 && !player.getBuffedValue(400021096)) {
                player.lawOfGravity = monster.getObjectId();
                effect.applyTo(player);
            }
            if (attack.skill == 400021098) {
                player.getMap().broadcastMessage(MobPacket.skillAttackEffect(monster.getObjectId(), attack.skill, player.getId()));
            }
            if (attack.skill == 2121003 && monster.getBurnedBuffSize() >= 5) {
                player.changeCooldown(2121003, -2000);
                if (player.getCooldownLimit(2121011) > 0L) {
                    player.removeCooldown(2121011);
                }
            }
            if (monster.isAlive()) {
                continue;
            }
            multikill = (byte) (multikill + 1);
        }
        if (attack.skill == 400021096 && !monster.isAlive()) {
            SecondaryStatEffect a = SkillFactory.getSkill(400021104).getEffect(player.getSkillLevel(400021096));
            MapleMist newmist = new MapleMist(a.calculateBoundingBox(player.getPosition(), player.isFacingLeft()), player, a, a.getDuration(), (byte) (player.isFacingLeft() ? 1 : 0));
            newmist.setPosition(monster.getPosition());
            newmist.setDelay(0);
            player.getMap().spawnMist(newmist, false);
        }
        if (player.getBuffedValue(SecondaryStat.OverloadMana) != null && !GameConstants.is_forceAtom_attack_skill(attack.skill) && !effect.isMist()) {
            if (GameConstants.isKinesis(player.getJob())) {
                player.addHP((int) (-(player.getStat().getCurrentMaxHp() * (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getY() / 100L)));
            } else {
                player.addMP((int) (-(player.getStat().getCurrentMaxMp(player) * (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getX() / 100L)));
            }
        }
        if (player.getSkillLevel(2120010) > 0 && (arcaneAim = SkillFactory.getSkill(2120010).getEffect(player.getSkillLevel(2120010))).makeChanceResult()) {
            if (player.getArcaneAim() < 5) {
                player.setArcaneAim(player.getArcaneAim() + 1);
            }
            arcaneAim.applyTo(player, false);
        }
        if (player.getSkillLevel(2220010) > 0 && (arcaneAim = SkillFactory.getSkill(2220010).getEffect(player.getSkillLevel(2220010))).makeChanceResult()) {
            if (player.getArcaneAim() < 5) {
                player.setArcaneAim(player.getArcaneAim() + 1);
            }
            arcaneAim.applyTo(player, false);
        }
        if (player.getSkillLevel(2320011) > 0 && (arcaneAim = SkillFactory.getSkill(2320011).getEffect(player.getSkillLevel(2320011))).makeChanceResult()) {
            if (player.getArcaneAim() < 5) {
                player.setArcaneAim(player.getArcaneAim() + 1);
            }
            arcaneAim.applyTo(player, false);
        }
        if (totDamage > 0L) {
            if (player.getMapId() == 993000500) {
                player.setFWolfDamage(player.getFWolfDamage() + totDamage);
                player.setFWolfAttackCount(player.getFWolfAttackCount() + 1);
            }
            DamageParse.MFinalAttackRequest(player, attack.skill, monster);
            if (attack.skill == 2321007) {
                player.홀리워터++;
                if (player.홀리워터 == 7) {
                    player.홀리워터 = 0;
                    player.홀리워터스택++;
                    if (player.홀리워터스택 > 5) {
                        player.홀리워터스택 = 5;
                    }
                    HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                    statups.put(SecondaryStat.HolyWater, new Pair<Integer, Integer>((int) player.홀리워터스택, 0));
                    player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, player));
                }
            }
            if (GameConstants.isLuminous(player.getJob())) {
                SecondaryStatEffect effect2;
                SecondaryStatEffect dark;
                if ((player.getBuffedValue(20040216) || player.getBuffedValue(20040219) || player.getBuffedValue(20040220)) && (GameConstants.isLightSkills(attack.skill) || (player.getBuffedValue(20040219) || player.getBuffedValue(20040220)) && (attack.skill == 27121303 || attack.skill == 27111303))) {
                    player.addHP(player.getStat().getMaxHp() / 100L);
                }
                if (player.getSkillLevel(27120005) > 0 && (dark = SkillFactory.getSkill(27120005).getEffect(player.getSkillLevel(27120005))).makeChanceResult()) {
                    if (player.stackbuff < dark.getX()) {
                        ++player.stackbuff;
                    }
                    dark.applyTo(player, false);
                }
                if (player.getBuffedValue(400021105) && (GameConstants.isLightSkills(attack.skill) || attack.skill == 27121303 || GameConstants.isDarkSkills(attack.skill)) && player.getSkillLevel(400021105) > 0 && player.getSkillCustomValue(400021109) == null) {
                    effect2 = SkillFactory.getSkill(400021105).getEffect(player.getSkillLevel(400021105));
                    int skillid = 0;
                    if (player.getSkillCustomValue0(400021105) == 2L) {
                        skillid = 400021110;
                    } else if (player.getSkillCustomValue0(400021105) == 1L) {
                        skillid = 400021109;
                    }
                    player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400021105, Arrays.asList(new RangeAttack(skillid, attack.position, 1, 0, 1))));
                    player.setSkillCustomInfo(400021109, 0L, effect2.getU2());
                    player.addSkillCustomInfo(400021110, -1L);
                    if (player.getSkillCustomValue0(400021110) <= 0L) {
                        player.cancelEffect(effect2);
                    }
                }
                if (!(player.getBuffedValue(20040216) || player.getBuffedValue(20040217) || player.getBuffedValue(20040219) || player.getBuffedValue(20040220))) {
                    if (GameConstants.isLightSkills(attack.skill)) {
                        player.setLuminusMorphUse(1);
                        SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
                        player.setLuminusMorph(false);
                    } else if (GameConstants.isDarkSkills(attack.skill)) {
                        player.setLuminusMorphUse(9999);
                        SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
                        player.setLuminusMorph(true);
                    }
                    player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
                } else if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220)) {
                    if (player.getLuminusMorph()) {
                        if (GameConstants.isLightSkills(attack.skill)) {
                            if (!(player.getBuffedValue(20040219) || player.getBuffedValue(20040220) || player.getBuffedValue(400021105) || !GameConstants.isLightSkills(attack.skill) || player.getSkillLevel(400021105) <= 0 || player.skillisCooling(400021106))) {
                                effect2 = SkillFactory.getSkill(400021105).getEffect(player.getSkillLevel(400021105));
                                if (player.getSkillCustomValue0(400021107) < (long) effect2.getU()) {
                                    player.setSkillCustomInfo(400021107, player.getSkillCustomValue0(400021107) + 1L, 0L);
                                }
                                player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400021105, Arrays.asList(new RangeAttack(400021107, attack.position, 1, 0, 1))));
                                player.addCooldown(400021106, System.currentTimeMillis(), effect2.getX() * 1000);
                                player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400021106, effect2.getX() * 1000));
                                HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                                statups.put(SecondaryStat.LiberationOrb, new Pair<Integer, Integer>(1, 0));
                                player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect2, player));
                            }
                            if (player.getLuminusMorphUse() - GameConstants.isLightSkillsGaugeCheck(attack.skill) <= 0) {
                                if (player.getSkillLevel(20040219) > 0) {
                                    player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                    SkillFactory.getSkill(20040219).getEffect(1).applyTo(player, false);
                                    player.setUseTruthDoor(false);
                                } else {
                                    player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                    player.setLuminusMorph(false);
                                    SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
                                }
                            } else {
                                player.setLuminusMorphUse(player.getLuminusMorphUse() - GameConstants.isLightSkillsGaugeCheck(attack.skill));
                            }
                            if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220) && player.getLuminusMorph()) {
                                player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
                            }
                        }
                    } else if (GameConstants.isDarkSkills(attack.skill)) {
                        if (!player.getBuffedValue(400021105) && GameConstants.isDarkSkills(attack.skill) && player.getSkillLevel(400021105) > 0 && !player.skillisCooling(400021106)) {
                            effect2 = SkillFactory.getSkill(400021105).getEffect(player.getSkillLevel(400021105));
                            if (player.getSkillCustomValue0(400021108) < (long) effect2.getU()) {
                                player.setSkillCustomInfo(400021108, player.getSkillCustomValue0(400021108) + 1L, 0L);
                            }
                            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400021105, Arrays.asList(new RangeAttack(400021108, attack.position, 1, 0, 1))));
                            player.addCooldown(400021106, System.currentTimeMillis(), effect2.getX() * 1000);
                            player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400021106, effect2.getX() * 1000));
                            HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                            statups.put(SecondaryStat.LiberationOrb, new Pair<Integer, Integer>(1, 0));
                            player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect2, player));
                        }
                        if (player.getLuminusMorphUse() + GameConstants.isDarkSkillsGaugeCheck(player, attack.skill) >= 10000) {
                            if (player.getSkillLevel(20040219) > 0) {
                                player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                SkillFactory.getSkill(20040220).getEffect(1).applyTo(player, false);
                                player.setUseTruthDoor(false);
                            } else {
                                player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                                player.setLuminusMorph(true);
                                SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
                            }
                        } else {
                            player.setLuminusMorphUse(player.getLuminusMorphUse() + GameConstants.isDarkSkillsGaugeCheck(player, attack.skill));
                        }
                        if (!(player.getBuffedValue(20040219) || player.getBuffedValue(20040220) || player.getLuminusMorph())) {
                            player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
                            SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
                        }
                    }
                    player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
                }
            } else if (GameConstants.isEvan(player.getJob())) {
                if (attack.isLink && player.getSkillLevel(22110016) > 0) {
                    SkillFactory.getSkill(22110016).getEffect(player.getSkillLevel(22110016)).applyTo(player);
                }
            } else if (GameConstants.isLara(player.getJob()) && attack.targets > 0 && (attack.skill == 162001000 || attack.skill == 162121021)) {
                SecondaryStatEffect effects;
                if (player.getSkillLevel(162000003) > 0 && Randomizer.isSuccess((effects = SkillFactory.getSkill(162000003).getEffect(player.getSkillLevel(162000003))).getProp())) {
                    player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(162001004, attack.position, 0, 0, 1))));
                }
                if (player.getBuffedValue(162121003)) {
                    effects = SkillFactory.getSkill(162120002).getEffect(player.getSkillLevel(162120002));
                    if (player.getSkillCustomValue(162121004) == null) {
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(162121004, Arrays.asList(new RangeAttack(162121004, attack.position, 0, 0, 1))));
                        player.setSkillCustomInfo(162121004, 0L, (int) effects.getT() * 1000);
                    }
                }
                if (player.getBuffedValue(162121006)) {
                    effects = SkillFactory.getSkill(162120005).getEffect(player.getSkillLevel(162120005));
                    if (player.getSkillCustomValue(162121007) == null) {
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(162121007, Arrays.asList(new RangeAttack(162121007, attack.position, 0, 0, 1))));
                        player.setSkillCustomInfo(162121007, 0L, (int) effects.getT() * 1000);
                    }
                }
                if (player.getBuffedValue(162121009)) {
                    effects = SkillFactory.getSkill(162120008).getEffect(player.getSkillLevel(162120008));
                    if (player.getSkillCustomValue(162121010) == null) {
                        player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(162121010, Arrays.asList(new RangeAttack(162121010, attack.position, 0, 0, 5))));
                        player.setSkillCustomInfo(162121010, 0L, (int) effects.getT() * 1000);
                    }
                }
            }
            if (attack.targets > 0 && player.getBuffedEffect(SecondaryStat.Triumph) != null && attack.skill != 2311017 && System.currentTimeMillis() - player.TriumphTime >= 2000) {
                List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 500000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
                if (objs.size() > 0) {
                    final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
                    atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(), 2311017, 4000, 0, 1, new Point((int) player.getTruePosition().getX(), (int) player.getTruePosition().getY() - 120), Arrays.asList(0)));
                    atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(), 2311017, 4000, 0, 1, new Point((int) player.getTruePosition().getX() + 20, (int) player.getTruePosition().getY() - 116), Arrays.asList(0)));
                    atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(), 2311017, 4000, 0, 1, new Point((int) player.getTruePosition().getX() - 20, (int) player.getTruePosition().getY() - 120), Arrays.asList(0)));
                    atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(), 2311017, 4000, 0, 1, new Point((int) player.getTruePosition().getX() + 10, (int) player.getTruePosition().getY() - 116), Arrays.asList(0)));
                    atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(), 2311017, 4000, 0, 1, new Point((int) player.getTruePosition().getX() - 10, (int) player.getTruePosition().getY() - 120), Arrays.asList(0)));
                    player.spawnSecondAtom(atoms);
                    player.TriumphTime = System.currentTimeMillis();
                }
            }
            if (attack.skill == 27121303 && player.getSkillLevel(400021071) > 0) {
                boolean give = false;
                if (player.getPerfusion() < SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).getX() - 1) {
                    give = true;
                } else if (player.getPerfusion() >= SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).getX() - 1 && player.skillisCooling(400021071)) {
                    give = true;
                }
                if (give) {
                    SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).applyTo(player, false);
                }
            } else if (GameConstants.isKinesis(player.getJob())) {
                if (player.getSkillLevel(142110011) > 0 && attack.skill != 142110011 && !attack.allDamage.isEmpty()) {
                    switch (attack.skill) {
                        case 142001000:
                        case 142001002:
                        case 142100000:
                        case 142100001:
                        case 142101003:
                        case 142101009:
                        case 142110000:
                        case 142110001:
                        case 142111007:
                        case 142120002:
                        case 142120030:
                        case 142121005:
                        case 142121030: {
                            break;
                        }
                        default: {
                            MapleAtom atom = new MapleAtom(false, player.getId(), 22, true, 142110011, player.getTruePosition().x, player.getTruePosition().y);
                            for (int i = 0; i < attack.targets; ++i) {
                                if (!SkillFactory.getSkill(142110011).getEffect(player.getSkillLevel(142110011)).makeChanceResult() || attack.skill == 142001000 || attack.skill == 142100000 || attack.skill == 142110000) {
                                    continue;
                                }
                                atom.addForceAtom(new ForceAtom(0, 21, 9, 68, 960));
                            }
                            if (atom.getForceAtoms().isEmpty()) {
                                break;
                            }
                            atom.setDwFirstTargetId(0);
                            player.getMap().spawnMapleAtom(atom);
                            break;
                        }
                    }
                }
                if (attack.skill == 142121004) {
                    int up = 0;
                    for (AttackPair att : attack.allDamage) {
                        MapleMonster m = MapleLifeFactory.getMonster(att.monsterId);
                        if (m == null) {
                            continue;
                        }
                        up += m.getStats().isBoss() ? effect.getW() : (int) effect.getIndiePmdR();
                    }
                    if (player.getSkillLevel(142120041) > 0) {
                        up *= 2;
                    }
                    player.setSkillCustomInfo(142121004, up, 0L);
                    if (player.getSkillCustomValue0(142121004) >= (long) effect.getW()) {
                        player.setSkillCustomInfo(142121004, effect.getW(), 0L);
                    }
                    SkillFactory.getSkill(142121004).getEffect(player.getSkillLevel(142121004)).applyTo(player);
                }
                if (attack.skill == 400021075 && monster != null) {
                    player.givePPoint((byte) 1);
                } else if (attack.skill == 142121005) {
                    player.givePPoint((byte) -1);
                }
            } else if (GameConstants.isIllium(player.getJob()) && attack.skill == 400021061 && attack.targets > 0) {
                SkillFactory.getSkill(152000009).getEffect(player.getSkillLevel(152000009)).applyTo(player, false);
            }
            if (player.getBuffedValue(32101009) && player.getSkillCustomValue(32111119) == null && player.getId() == player.getBuffedOwner(32101009)) {
                player.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
                player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null, null));
                player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null, null), false);
                player.setSkillCustomInfo(32111119, 0L, 5000L);
                if (player.getParty() != null) {
                    for (MaplePartyCharacter pc : player.getParty().getMembers()) {
                        MapleCharacter chr;
                        if (pc.getId() == player.getId() || !pc.isOnline() || (chr = player.getClient().getChannelServer().getPlayerStorage().getCharacterById(pc.getId())) == null || !chr.getBuffedValue(32101009) || chr.getId() == player.getId()) {
                            continue;
                        }
                        chr.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
                        if (chr.getDisease(SecondaryStat.GiveMeHeal) != null) {
                            chr.cancelDisease(SecondaryStat.GiveMeHeal);
                        }
                        chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
                        chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null), false);
                    }
                }
            }
        }
        if (player.getBuffedValue(400021092) && player.getSkillCustomValue0(400021092) != 1L) {
            MapleSummon sum = player.getSummon(400021092);
            MapleMonster mon = null;
            List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 800000.0, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
            mon = player.getMap().getMonsterByOid(objs.get(Randomizer.nextInt(objs.size())).getObjectId());
            if (sum != null && mon != null) {
                player.setGraveTarget(mon.getObjectId());
                player.createSecondAtom(SkillFactory.getSkill(400021092).getSecondAtoms(), sum.getPosition());
                player.getMap().broadcastMessage(CField.SummonPacket.updateSummon(sum, 99));
                player.setSkillCustomInfo(400021092, 1L, 0L);
            }
        }
        if (totDamage > 0L && player.getBuffedValue(400021073) && (summon = player.getSummon(400021073)) != null && summon.getEnergy() < 22) {
            switch (attack.skill) {
                case 22110022:
                case 22110023:
                case 22111012:
                case 22170060:
                case 22170070:
                case 400021012:
                case 400021014:
                case 400021015: {
                    MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073, summon.getTruePosition().x, summon.getTruePosition().y);
                    atom.setDwUserOwner(summon.getOwner().getId());
                    atom.setDwFirstTargetId(0);
                    atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
                    player.getMap().spawnMapleAtom(atom);
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
                    if (summon.getEnergy() < 22) {
                        break;
                    }
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
                    break;
                }
                case 22110014:
                case 22110024:
                case 22110025:
                case 22111011:
                case 22140014:
                case 22140015:
                case 22140023:
                case 22140024:
                case 22141011:
                case 22170064:
                case 22170065:
                case 22170066:
                case 22170067:
                case 22170093:
                case 22170094:
                case 22171063:
                case 22171083:
                case 22171095:
                case 400021013: {
                    if (summon.getMagicSkills().contains(attack.skill)) {
                        break;
                    }
                    summon.getMagicSkills().add(attack.skill);
                    summon.setEnergy(Math.min(22, summon.getEnergy() + 3));
                    MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073, summon.getTruePosition().x, summon.getTruePosition().y);
                    atom.setDwUserOwner(summon.getOwner().getId());
                    atom.setDwFirstTargetId(0);
                    atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
                    player.getMap().spawnMapleAtom(atom);
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
                    if (summon.getEnergy() < 22) {
                        break;
                    }
                    player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
                    break;
                }
            }
        }
        if (player.getBuffedValue(400001050) && player.getSkillCustomValue0(400001050) == 400001055L) {
            SecondaryStatEffect effect6 = SkillFactory.getSkill(400001050).getEffect(player.getSkillLevel(400001050));
            player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400001055, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
            player.removeSkillCustomInfo(400001050);
            long duration = player.getBuffLimit(400001050);
            effect6.applyTo(player, false, (int) duration);
        }
        if (attack.skill == 2121003) {
            for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
                if (mist.getSource() == null || mist.getSource().getSourceId() != 2111003) {
                    continue;
                }
                player.getMap().removeMist(mist.getSource().getSourceId());
                if (player.getCooldownLimit(2121011) <= 0L) {
                    continue;
                }
                player.removeCooldown(2121011);
            }
        }
        if (totDamage > 0L && attack.skill >= 400021013 && attack.skill <= 400021016) {
            SkillFactory.getSkill(400021012).getEffect(attack.skilllevel).applyTo(player, false);
        }
        if (player.getSkillLevel(80002762) > 0 && (stst = SkillFactory.getSkill(80002762).getEffect(player.getSkillLevel(80002762))).makeChanceResult()) {
            stst.applyTo(player, false);
        }
        if (player.getSkillLevel(150010241) > 0 && player.getSkillCustomValue(80000514) == null) {
            SkillFactory.getSkill(150010241).getEffect(player.getSkillLevel(150010241)).applyTo(player);
            player.setSkillCustomInfo(80000514, 0L, 3000L);
        } else if (player.getSkillLevel(80000514) > 0 && player.getSkillCustomValue(80000514) == null) {
            SkillFactory.getSkill(80000514).getEffect(player.getSkillLevel(80000514)).applyTo(player);
            player.setSkillCustomInfo(80000514, 0L, 3000L);
        }
        if (attack.skill == 152121007 && player.getBuffedEffect(152111003) != null) {
            player.canUseMortalWingBeat = false;
            HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
            statups.put(SecondaryStat.GloryWing, new Pair<Integer, Integer>(1, (int) player.getBuffLimit(152111003)));
            player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(152111003), player));
        }
        if (attack.skill == 400021012) {
            ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
            skills.add(new RangeAttack(400021013, attack.position, 0, 0, 0));
            skills.add(new RangeAttack(400021014, attack.position, 0, 0, 0));
            skills.add(new RangeAttack(400021015, attack.position, 0, 0, 0));
            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
        }
        if (player.getBuffedEffect(SecondaryStat.CrystalGate) != null) {
            SecondaryStatEffect crystalGate = player.getBuffedEffect(SecondaryStat.CrystalGate);
            if ((double) (System.currentTimeMillis() - player.lastCrystalGateTime) >= crystalGate.getT() * 1000.0) {
                player.lastCrystalGateTime = System.currentTimeMillis();
                player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400021111, new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
            }
        }
        if (player.getBuffedValue(80002890) && attack.skill != 80002890 && attack.skill != 80002890 && System.currentTimeMillis() - player.lastThunderTime >= (long) player.getBuffedEffect(80002890).getCooldown(player)) {
            player.lastThunderTime = System.currentTimeMillis();
            player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(80002890, Arrays.asList(new RangeAttack(80002890, attack.position, 0, 0, 1))));
        }
        if (attack.skill == 400021002) {
            SecondaryStatEffect iceAge = SkillFactory.getSkill(400020002).getEffect(player.getSkillLevel(400021002));
            Rectangle bounds = effect.calculateBoundingBox(player.getTruePosition(), player.isFacingLeft());
            for (int i = 0; i < player.getMap().getFootholds().getAllRelevants().size(); ++i) {
                MapleFoothold fh = player.getMap().getFootholds().getAllRelevants().get(i);
                int rx = fh.getPoint2().x - fh.getPoint1().x;
                if (!bounds.contains(fh.getPoint1()) && !bounds.contains(fh.getPoint2())) {
                    continue;
                }
                if (rx / 200 > 1) {
                    for (int i2 = 0; i2 <= rx / 200; ++i2) {
                        boolean active = true;
                        if (!active) {
                            continue;
                        }
                        iceAge.applyTo(player, false, new Point(fh.getPoint1().x + i2 * 200, fh.getPoint1().y + 30));
                    }
                    continue;
                }
                boolean active = true;
                for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
                    if (mist.getPosition().x - 200 < fh.getPoint1().x && mist.getPosition().x + 200 > fh.getPoint2().x && fh.getPoint1().y - 70 < mist.getPosition().y && fh.getPoint1().y + 70 > mist.getPosition().y) {
                        active = false;
                        break;
                    }
                    if (!mist.getBox().contains(fh.getPoint1()) && !mist.getBox().contains(fh.getPoint2())) {
                        continue;
                    }
                    active = false;
                    break;
                }
                if (!active) {
                    continue;
                }
                iceAge.applyTo(player, false, new Point(fh.getPoint1().x, fh.getPoint1().y + 30));
            }
        }
        if (multikill > 0) {
            player.CombokillHandler(monster, 1, multikill);
        }
    }

    public static final AttackInfo parseDmgMa(LittleEndianAccessor lea, MapleCharacter chr, boolean chilling, boolean orbital) {
        AttackInfo ret = new AttackInfo();
        LittleEndianAccessor data = lea;
        if (orbital) {
            ret.skill = lea.readInt();
            ret.skilllevel = lea.readInt();
            lea.skip(4);
            lea.skip(4);
            lea.skip(4);
        }
        lea.skip(1);
        ret.tbyte = lea.readByte();
        ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        //lea.skip(4);
        ret.skill = lea.readInt();
        ret.skilllevel = lea.readInt();
        try {
            if (orbital) {
                lea.skip(1);
            }
            lea.skip(4);
            lea.skip(4);
            GameConstants.attackBonusRecv(lea, ret);
            GameConstants.calcAttackPosition(lea, ret);
            if (orbital) {
                if (GameConstants.sub_57D400(ret.skill)) {
                    ret.charge = lea.readInt();
                }
            } else if (GameConstants.is_keydown_skill(ret.skill)) {
                ret.charge = lea.readInt();
            }
            ret.isShadowPartner = lea.readByte();
            ret.isBuckShot = lea.readByte();
            ret.display = lea.readByte();
            ret.facingleft = lea.readByte();
            lea.skip(4);
            ret.attacktype = lea.readByte();
            if (GameConstants.is_evan_force_skill(ret.skill)) {
                lea.readByte();
            }
            ret.speed = lea.readByte();
            ret.lastAttackTickCount = lea.readInt();
            int chillingoid = 0;
            if (chilling) {
                chillingoid = lea.readInt();
            }
            lea.readInt();
            if (orbital || ret.skill == 22140024) {
                lea.skip(4);
            }
            ret.allDamage = new ArrayList<AttackPair>();
            for (int i = 0; i < ret.targets; ++i) {
                long damage;
                int oid = lea.readInt();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                int monsterId = lea.readInt();
                lea.readByte();
                Point pos1 = lea.readPos();
                Point pos2 = lea.readPos();
                if (!orbital) {
                    lea.skip(1);
                }
                ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
                if (ret.skill == 80001835) {
                    int cc = lea.readByte();
                    for (int ii = 0; ii < cc; ++ii) {
                        damage = lea.readLong();
                        if (damage < 0L) {
                            damage &= 0xFFFFFFFFL;
                        }
                        allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
                    }
                } else {
                    lea.readShort();
                    lea.skip(4);
                    lea.skip(4);
                    lea.readByte();
                    for (int j = 0; j < ret.hits; ++j) {
                        damage = lea.readLong();
                        if (damage < 0L) {
                            damage &= 0xFFFFFFFFL;
                        }
                        allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
                    }
                }
                lea.skip(4);
                lea.skip(4);
                if (ret.skill == 37111005) {
                    lea.skip(1);
                }
                if (ret.skill == 142120001 || ret.skill == 142120002 || ret.skill == 142110003) {
                    lea.skip(8);
                }
                GameConstants.attackSkeletonImage(lea, ret);
                ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
            }
            ret.position = lea.readPos();
            if (ret.skill == 32111016) {
                ret.plusPosition3 = lea.readPos();
            }
            if (ret.skill == 22140024) {
                lea.skip(4);
            }
            byte posType = lea.readByte();
            if (!orbital && posType != 0) {
                ret.plusPosition = lea.readPos();
                ret.plusPosition2 = lea.readPos();
                if (ret.skill == 12100029) {
                    lea.skip(4);
                } else if (ret.skill == 2121003) {
                    int size = lea.readByte();
                    for (int i = 0; i < size; ++i) {
                        lea.skip(4);
                    }
                } else if (ret.skill == 2111003) {
                    lea.skip(1);
                    ret.plusPosition3 = lea.readPos();
                } else {
                    ret.isLink = lea.readByte() == 1;
                    ret.nMoveAction = lea.readByte();
                    ret.bShowFixedDamage = lea.readByte();
                }
            }
        } catch (Exception e) {
            FileoutputUtil.log("Log_Attack.txt", "error in MagicAttack.\r\n ordinary : " + HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
        }
        return ret;
    }

    public static final AttackInfo parseDmgB(LittleEndianAccessor lea, MapleCharacter chr) {
        AttackInfo ret = new AttackInfo();
        LittleEndianAccessor data = lea;
        lea.skip(1);
        ret.tbyte = lea.readByte();
        ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        //lea.skip(4);
        ret.skill = lea.readInt();
        ret.skilllevel = lea.readInt();
        try {
            lea.skip(4);
            lea.skip(4);
            GameConstants.attackBonusRecv(lea, ret);
            GameConstants.calcAttackPosition(lea, ret);
            if (ret.skill != 0 && (GameConstants.is_keydown_skill(ret.skill) || GameConstants.is_super_nova_skill(ret.skill))) {
                ret.charge = lea.readInt();
            }
            if (GameConstants.sub_883680(ret.skill) || ret.skill == 5300007 || ret.skill == 27120211 || ret.skill == 14111023 || ret.skill == 400031003 || ret.skill == 400031004 || ret.skill == 64101008) {
                lea.skip(4);
            }
            if (GameConstants.isZeroSkill(ret.skill)) {
                ret.asist = lea.readByte();
            }
            if (GameConstants.sub_57DCA0(ret.skill)) {
                lea.skip(4);
            }
            ret.isShadowPartner = lea.readByte();
            ret.isBuckShot = lea.readByte();
            ret.display = lea.readByte();
            ret.facingleft = lea.readByte();
            lea.readInt();
            ret.attacktype = lea.readByte();
            ret.speed = lea.readByte();
            ret.lastAttackTickCount = lea.readInt();
            lea.readInt();
            if (ret.skill == 5111009) {
                lea.skip(1);
            } else if (ret.skill == 25111005) {
                lea.skip(4);
            }
            ret.allDamage = new ArrayList<AttackPair>();
            for (int i = 0; i < ret.targets; ++i) {
                int oid = lea.readInt();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                int monsterId = lea.readInt();
                lea.readByte();
                Point pos1 = lea.readPos();
                Point pos2 = lea.readPos();
                lea.skip(2);
                lea.readInt();
                lea.readInt();
                lea.readByte();
                ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
                for (int j = 0; j < ret.hits; ++j) {
                    long damage = lea.readLong();
                    if (damage < 0L) {
                        damage &= 0xFFFFFFFFL;
                    }
                    allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
                }
                lea.skip(4);
                lea.skip(4);
                if (ret.skill == 37111005) {
                    lea.skip(1);
                }
                GameConstants.attackSkeletonImage(lea, ret);
                ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
            }
            ret.position = lea.readPos();
        } catch (Exception e) {
            FileoutputUtil.log("Log_Attack.txt", "error in BuffAttack.\r\n ordinary : " + HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
        }
        return ret;
    }

    public static final AttackInfo parseDmgM(LittleEndianAccessor lea, MapleCharacter chr, boolean dot) {
        AttackInfo ret = new AttackInfo();
        LittleEndianAccessor data = lea;
        lea.skip(1);
        ret.tbyte = lea.readByte();
        ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        ret.skill = lea.readInt();
        ret.skilllevel = lea.readInt();
        try {
            if (!dot) {
                ret.isLink = lea.readByte() == 1;
            }
            lea.skip(4);
            lea.skip(4);
            GameConstants.attackBonusRecv(lea, ret);
            GameConstants.calcAttackPosition(lea, ret);
            if ((ret.skill == 11121056 || GameConstants.is_keydown_skill(ret.skill) || GameConstants.is_super_nova_skill(ret.skill)) && ret.skill != 35121015) {
                ret.charge = lea.readInt();
            }
            if (GameConstants.sub_883680(ret.skill) || ret.skill == 5300007 || ret.skill == 27120211 || ret.skill == 14111023 || ret.skill == 400031003 || ret.skill == 400031004 || ret.skill == 64101008) {
                lea.skip(4);
            }
            if (GameConstants.isZeroSkill(ret.skill)) {
                ret.asist = lea.readByte();
            }
            if (GameConstants.sub_57DCA0(ret.skill) || ret.skill == 11111220 || ret.skill == 11121201 || ret.skill == 11121202) {
                ret.summonattack = lea.readInt();
            }
            if (ret.skill == 400031010 || ret.skill == 80002823) {
                lea.skip(4);
                lea.skip(4);
            }
            if (ret.skill == 400041019) {
                lea.skip(4);
                lea.skip(4);
            }
            ret.isShadowPartner = lea.readByte();
            ret.isBuckShot = lea.readByte();
            ret.display = lea.readByte();
            ret.facingleft = lea.readByte();
            lea.readInt();
            ret.attacktype = lea.readByte();
            ret.speed = lea.readByte();
            ret.lastAttackTickCount = lea.readInt();
            lea.readInt();
            if (ret.skill != 400051018 && ret.skill != 400051019 && ret.skill != 400051020 && ret.skill != 400051027) {
                lea.readInt();
            }
            if (ret.skill == 5111009) {
                lea.skip(1);
            } else if (ret.skill == 25111005 || ret.skill == 33000036 || ret.skill == 80001762 || ret.skill == 400021131 || ret.skill == 400021130) {
                lea.skip(4);
            } else if (ret.isLink && ret.skill == 23121011 || ret.skill == 80001913) {
                lea.skip(1);
            }
            ret.allDamage = new ArrayList<AttackPair>();
            if (SkillFactory.getSkill(ret.skill) != null && SkillFactory.getSkill(ret.skill).isFinalAttack() && ret.skill != 400001038) {
                lea.skip(1);
            }
            for (int i = 0; i < ret.targets; ++i) {
                int oid = lea.readInt();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                byte ab = lea.readByte();
                int monsterId = lea.readInt();
               // System.err.println(monsterId);
                lea.readByte();
                Point pos1 = lea.readPos();
                Point pos2 = lea.readPos();
                lea.skip(2);
                lea.readInt();
                lea.readInt();
                lea.readByte();
                ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
                for (int j = 0; j < ret.hits; ++j) {
                    long damage = lea.readLong();
                    if (damage < 0L) {
                        damage &= 0xFFFFFFFFL;
                    }
                  //  System.err.println(damage);
                    allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
                }
                lea.skip(4);
                lea.skip(4);
                if (ret.skill == 37111005 || ret.skill == 400021029) {
                    lea.skip(1);
                }
                GameConstants.attackSkeletonImage(lea, ret);
                ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
            }
            ret.position = GameConstants.is_super_nova_skill(ret.skill) ? lea.readPos() : (ret.skill == 101000102 ? lea.readPos() : (ret.skill == 400031016 || ret.skill == 400041024 || ret.skill == 80002452 || GameConstants.sub_84ABA0(ret.skill) ? lea.readPos() : lea.readPos()));
            if (GameConstants.sub_849720(ret.skill)) {
                lea.skip(4);
                ret.position = lea.readPos();
                lea.skip(1);
            }
            if (ret.skill == 21121057) {
                lea.readPos();
            }
            if (GameConstants.sub_846930(ret.skill) > 0 || GameConstants.sub_847580(ret.skill)) {
                lea.skip(1);
            }
            if (ret.skill == 400031059) {
                lea.skip(4);
                ret.plusPosition2 = lea.readPos();
            }
            if (ret.skill == 21120019 || ret.skill == 37121052 || GameConstants.is_shadow_assult(ret.skill) || ret.skill == 11121014 || ret.skill == 5101004) {
                ret.plusPos = lea.readByte();
                ret.plusPosition = new Point(lea.readInt(), lea.readInt());
            }
            if (ret.skill == 61121105 || ret.skill == 61121222 || ret.skill == 24121052) {
                for (short count = lea.readShort(); count > 0; count = (short) (count - 1)) {
                    ret.mistPoints.add(new Point(lea.readShort(), lea.readShort()));
                }
            }
            if (ret.skill == 14111006) {
                lea.skip(2);
                lea.skip(2);
            } else if (ret.skill == 80002686) {
                int size = lea.readInt();
                for (int z = 0; z < size; ++z) {
                    lea.skip(4);
                }
            }
        } catch (Exception e) {
            FileoutputUtil.log("Log_Attack.txt", "error in CloseRangeAttack.\r\n ordinary : " + HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
        }
        return ret;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final AttackInfo parseDmgR(LittleEndianAccessor lea, MapleCharacter chr) {
        AttackInfo ret = new AttackInfo();
        LittleEndianAccessor data = lea;
        byte specialType = lea.readByte();
        lea.skip(1);
        ret.tbyte = lea.readByte();
        ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        //lea.skip(4);
        ret.skill = lea.readInt();
        ret.skilllevel = lea.readInt();
        try {
            ret.isLink = lea.readByte() == 1;
            lea.skip(4);
            lea.skip(4);
            GameConstants.attackBonusRecv(lea, ret);
            GameConstants.calcAttackPosition(lea, ret);
            if (GameConstants.is_keydown_skill(ret.skill)) {
                ret.charge = lea.readInt();
            }
            if (GameConstants.isZeroSkill(ret.skill)) {
                ret.asist = lea.readByte();
            }
            if (GameConstants.sub_57DCA0(ret.skill) || ret.skill == 14121001 || ret.skill == 14101020) {
                ret.summonattack = lea.readInt();
            }
            ret.isShadowPartner = lea.readByte();
            ret.isBuckShot = lea.readByte();
            lea.skip(4);
            lea.skip(1);
            if (specialType == 1) {
                lea.readInt();
                lea.readShort();
                lea.readShort();
            }
            ret.display = lea.readByte();
            ret.facingleft = lea.readByte();
            lea.skip(4);
            ret.attacktype = lea.readByte();
            if (ret.skill == 23111001 || ret.skill == 36111010 || ret.skill == 80001915) {
                lea.skip(4);
                lea.skip(4);
                lea.skip(4);
            }
            ret.speed = lea.readByte();
            ret.lastAttackTickCount = lea.readInt();
            lea.readInt();
            lea.skip(4);
            if (SkillFactory.getSkill(ret.skill) != null && SkillFactory.getSkill(ret.skill).isFinalAttack()) {
                lea.skip(1);
            }
            ret.csstar = lea.readShort();
            lea.skip(1);
            lea.skip(2);
            lea.skip(2);
            lea.skip(2);
            ret.AOE = lea.readShort();
            ret.allDamage = new ArrayList<AttackPair>();
            for (int i = 0; i < ret.targets; ++i) {
                int oid = lea.readInt();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                lea.readByte();
                int monsterId = lea.readInt();
                lea.readByte();
                Point pos1 = lea.readPos();
                Point pos2 = lea.readPos();
                lea.skip(2);
                lea.readInt();
                lea.readInt();
                lea.readByte();
                ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
                for (int j = 0; j < ret.hits; ++j) {
                    long damage = lea.readLong();
                    if (damage < 0L) {
                        damage &= 0xFFFFFFFFL;
                    }
                    allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
                }
                lea.skip(4);
                lea.skip(4);
                GameConstants.attackSkeletonImage(lea, ret);
                ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
            }
            ret.position = lea.readPos();
            if (ret.skill - 64001009 >= -2 && ret.skill - 64001009 <= 2) {
                lea.skip(1);
                ret.chain = lea.readPos();
                return ret;
            }
            ret.bShowFixedDamage = lea.readByte();
            ret.nMoveAction = lea.readByte();
            if (GameConstants.sub_846930(ret.skill) > 0 || GameConstants.sub_847580(ret.skill)) {
                lea.skip(1);
            }
            if (GameConstants.isWildHunter(ret.skill / 10000)) {
                ret.plusPosition = lea.readPos();
            }
            if (GameConstants.sub_8327B0(ret.skill) && ret.skill != 13111020) {
                ret.plusPosition2 = lea.readPos();
            }
            if (ret.skill == 23121002 || ret.skill == 80001914) {
                lea.skip(1);
            }
            if (lea.available() <= 0L) {
                return ret;
            }
        } catch (Exception e) {
            FileoutputUtil.log("Log_Attack.txt", "error in Rangedattack.\r\n ordinary : " + HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
        }
        return ret;
    }

    public static void WFinalAttackRequest(MapleCharacter chr, int skillid, MapleMonster monster) {
        if (SkillFactory.getSkill(skillid) != null) {
            if (chr.getJob() == 512 && chr.skillisCooling(5121013)) {
                if (skillid == 5001002 || skillid == 5101004 || skillid == 5111009 || skillid == 5121007 || skillid == 5121020) {
                    int finalattackid = SkillFactory.getSkill(5121013).getFinalAttackIdx();
                    byte skilllv = 0;
                    skilllv = (byte) chr.getSkillLevel(5121013);
                    finalattackid = 5120021;
                    byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId() / 10000 % 100);
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid, finalattackid, weaponidx, monster));
                    return;
                }
            } else {
                if (GameConstants.isExceedAttack(skillid)) {
                    int finalattackid = SkillFactory.getSkill(31220007).getFinalAttackIdx();
                    byte skilllv = 0;
                    skilllv = (byte) chr.getSkillLevel(31220007);
                    finalattackid = 31220007;
                    byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId() / 10000 % 100);
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid, finalattackid, weaponidx, monster));
                    return;
                }
                if (GameConstants.isAran(chr.getJob()) && chr.getBuffedValue(chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015)) {
                    int finalattackid = SkillFactory.getSkill(chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015).getFinalAttackIdx();
                    byte skilllv = 0;
                    skilllv = (byte) chr.getSkillLevel(chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015);
                    finalattackid = chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015;
                    byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId() / 10000 % 100);
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid, finalattackid, weaponidx, monster));
                    return;
                }
                if (GameConstants.isZero(chr.getJob()) && skillid == 101000101) {
                    int finalattackid = SkillFactory.getSkill(101000102).getFinalAttackIdx();
                    byte skilllv = 0;
                    skilllv = (byte) chr.getSkillLevel(101000102);
                    finalattackid = 101000102;
                    byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10).getItemId() / 10000 % 100);
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid, finalattackid, weaponidx, monster));
                    return;
                }
                if (GameConstants.isCannon(chr.getJob()) && chr.getBuffedValue(5311004) && chr.getSkillCustomValue0(5311004) == 1L) {
                    int finalattackid = SkillFactory.getSkill(5311004).getFinalAttackIdx();
                    byte skilllv = 0;
                    skilllv = (byte) chr.getSkillLevel(5311004);
                    finalattackid = 5310004;
                    byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId() / 10000 % 100);
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid, finalattackid, weaponidx, monster));
                    return;
                }
            }
            int finalattackid = SkillFactory.getSkill(skillid).getFinalAttackIdx();
            byte skilllv = 0;
            if (chr.getJob() == 3212) {
                skilllv = (byte) chr.getSkillLevel(32121004);
                finalattackid = 32121011;
            }
            if (finalattackid > 0) {
                if (skillid == 1001005 && chr.getSkillLevel(finalattackid) <= 0) {
                    if (chr.getSkillLevel(1200002) > 0) {
                        finalattackid = 1200002;
                    } else if (chr.getSkillLevel(1300002) > 0) {
                        finalattackid = 1300002;
                    }
                }
                if (finalattackid == 1100002) {
                    if (chr.getSkillLevel(1120013) > 0) {
                        finalattackid = 1120013;
                    }
                } else if (finalattackid == 51100002) {
                    if (chr.getSkillLevel(51120002) > 0) {
                        finalattackid = 51120002;
                    }
                } else if (finalattackid == 5120021) {
                    finalattackid = 5121013;
                }
                if (skilllv == 0) {
                    skilllv = (byte) chr.getSkillLevel(finalattackid);
                }
                if (SkillFactory.getSkill(finalattackid).getEffect(skilllv) == null) {
                    return;
                }
                int prop = SkillFactory.getSkill(finalattackid).getEffect(skilllv).getProp();
                if ((finalattackid == 1100002 || finalattackid == 1120013) && chr.getSkillLevel(1120048) > 0) {
                    prop = (byte) (prop + 15);
                } else if (finalattackid == 32121011) {
                    prop = 60;
                }
                if (chr.getBuffedValue(33121054)) {
                    prop = 100;
                }
                if (Randomizer.isSuccess(prop)) {
                    byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId() / 10000 % 100);
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid, finalattackid, weaponidx, monster));
                } else if (monster != null) {
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(0, skillid, 0, 0, monster));
                }
            }
        }
    }

    public static void MFinalAttackRequest(MapleCharacter chr, int skillid, MapleMonster monster) {
        if (SkillFactory.getSkill(skillid) != null) {
            int finalattackid = SkillFactory.getSkill(skillid).getFinalAttackIdx();
            byte skilllv = 0;
            if (chr.getJob() == 212) {
                skilllv = (byte) chr.getSkillLevel(2121007);
                finalattackid = 2120013;
            } else if (chr.getJob() == 222) {
                skilllv = (byte) chr.getSkillLevel(2221007);
                finalattackid = 2220014;
            } else if (chr.getJob() == 3212) {
                skilllv = (byte) chr.getSkillLevel(32121011);
                finalattackid = 32121011;
            }
            if (finalattackid > 0) {
                byte prop;
                if (skilllv == 0) {
                    skilllv = (byte) chr.getSkillLevel(finalattackid);
                }
                if (Randomizer.isSuccess(prop = (byte) SkillFactory.getSkill(finalattackid).getEffect(skilllv).getProp())) {
                    byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId() / 10000 % 100);
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid, finalattackid, weaponidx, monster));
                } else {
                    chr.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(0, skillid, 0, 0, monster));
                }
            }
        }
    }
}
