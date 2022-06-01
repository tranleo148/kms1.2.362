// 
// Decompiled by Procyon v0.5.36
// 
package handling.channel.handler;

import java.util.EnumMap;
import server.MapleItemInformationProvider;
import java.util.Arrays;
import client.inventory.MapleInventoryType;
import handling.channel.ChannelServer;
import handling.world.World;
import client.inventory.Item;
import java.awt.Rectangle;
import handling.world.MaplePartyCharacter;
import client.MatrixSkill;
import java.util.Map;
import java.util.Iterator;
import client.Skill;
import client.SummonSkillEntry;
import server.maps.MapleMap;
import tools.packet.MobPacket;
import java.awt.geom.Point2D;
import server.maps.ForceAtom;
import server.maps.MapleAtom;
import server.SecondaryStatEffect;
import tools.packet.CWvsContext;
import java.util.HashMap;
import server.Randomizer;
import tools.Triple;
import constants.GameConstants;
import tools.Pair;
import java.util.ArrayList;
import client.SkillFactory;
import client.MapleClient;
import server.life.MapleMonster;
import client.SecondaryStat;
import client.status.MonsterStatusEffect;
import client.status.MonsterStatus;
import server.maps.MapleMapObject;
import server.maps.SummonMovementType;
import server.maps.MapleSummon;
import server.maps.MapleDragon;
import server.maps.MapleMapObjectType;
import java.awt.Point;
import server.movement.LifeMovementFragment;
import java.util.List;
import tools.packet.CField;
import server.maps.AnimatedMapleMapObject;
import client.MapleCharacter;
import server.field.skill.SecondAtom;
import tools.data.LittleEndianAccessor;

public class SummonHandler {

    public static final void MoveDragon(final LittleEndianAccessor slea, final MapleCharacter chr) {
        slea.skip(12);
        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 5);
        if (chr != null && chr.getDragon() != null && res.size() > 0) {
            final Point pos = chr.getDragon().getPosition();
            MovementParse.updatePosition(res, chr.getDragon(), 0);
            if (!chr.isHidden()) {
                chr.getMap().broadcastMessage(chr, CField.moveDragon(chr.getDragon(), pos, res), chr.getTruePosition());
            }
        }
    }

    public static final void MoveSummon(final LittleEndianAccessor slea, final MapleCharacter chr) {
        if (chr == null || chr.getMap() == null) {
            return;
        }
        final MapleMapObject obj = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.SUMMON);
        if (obj == null) {
            return;
        }
        if (obj instanceof MapleDragon) {
            MoveDragon(slea, chr);
            return;
        }
        final MapleSummon sum = (MapleSummon) obj;
        if (sum.getOwner().getId() != chr.getId() || sum.getSkillLevel() <= 0 || sum.getMovementType() == SummonMovementType.STATIONARY) {
            return;
        }
        slea.skip(12);
        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 4);
        final Point pos = sum.getPosition();
        MovementParse.updatePosition(res, sum, 0);
        if (res.size() > 0) {
            chr.getMap().broadcastMessage(chr, CField.SummonPacket.moveSummon(chr.getId(), sum.getObjectId(), pos, res), sum.getTruePosition());
        }
    }

    public static final void DamageSummon(final LittleEndianAccessor slea, final MapleCharacter chr) {
        final int objectId = slea.readInt();
        final int unkByte = slea.readByte();
        final int damage = slea.readInt();
        final int monsterIdFrom = slea.readInt();
        final MapleSummon summon = chr.getMap().getSummonByOid(objectId);
        final MapleMonster monster = chr.getMap().getMonsterById(monsterIdFrom);
        if (summon == null) {
            return;
        }
        boolean remove = false;
        if (monster != null) {
            switch (summon.getSkill()) {
                case 13111024: {
                    monster.applyStatus(chr.getClient(), MonsterStatus.MS_Speed, new MonsterStatusEffect(13111024, (int) chr.getBuffLimit(13111024)), -100, chr.getBuffedEffect(SecondaryStat.IndieSummon, 13111024));
                    break;
                }
            }
        }
        if ((summon.isPuppet() || summon.getSkill() == 3221014) && summon.getOwner().getId() == chr.getId() && damage > 0) {
            summon.addHP(-damage);
            if (summon.getHP() <= 0) {
                remove = true;
            }
            chr.dropMessageGM(-8, "DamageSummon DMG : " + damage);
            chr.getMap().broadcastMessage(chr, CField.SummonPacket.damageSummon(chr.getId(), summon.getSkill(), damage, unkByte, monsterIdFrom), summon.getTruePosition());
            if (summon.getSkill() == 14000027) {
                summon.removeSummon(chr.getMap(), false);
            }
        }
        if (remove) {
            chr.cancelEffectFromBuffStat(SecondaryStat.IndieSummon);
        }
    }

    public static void SummonAttack(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || !chr.isAlive() || chr.getMap() == null) {
            return;
        }
        final MapleMap map = chr.getMap();
        final MapleMapObject obj = map.getMapObject(slea.readInt(), MapleMapObjectType.SUMMON);
        if (obj == null || !(obj instanceof MapleSummon)) {
            return;
        }
        final MapleSummon summon = (MapleSummon) obj;
        final SummonSkillEntry sse = SkillFactory.getSummonData(summon.getSkill());
        if (summon.getSkill() / 1000000 != 35 && summon.getSkill() != 400011065 && summon.getSkill() != 12120013 && summon.getSkill() != 33101008 && summon.getSkill() != 400041038 && sse == null) {
            chr.dropMessageGM(5, "Error in processing attack." + summon.getSkill());
            return;
        }
        slea.skip(4);
        final int skillid = slea.readInt();
        if (summon.getSkill() != skillid) {
            chr.dropMessage(5, "skill data unmatched.");
        }
        int skillid2 = slea.readInt();
        if (skillid == 12120013) {
            skillid2 = 400021043;
        }
        slea.skip(1);
        switch (skillid2) {
            case 152110001: {
                slea.skip(4);
                break;
            }
        }
        slea.readByte();
        slea.readByte(); // new 361
        final byte animation = slea.readByte();
        final byte tbyte = slea.readByte();
        final byte numAttacked = (byte) (tbyte >>> 4 & 0xF);
        final byte hits = (byte) (tbyte & 0xF);
        slea.readByte();
        if (summon.getSkill() == 35111002 && chr.getBuffedValue(35111002)) {
            slea.skip(12);
        }
        slea.skip(4);
        final Point pos = slea.readPos();
        final byte a = slea.readByte();
        if (a != 0) {
            slea.skip(2);
            slea.skip(2);
        }
        slea.skip(4);
        slea.skip(2);
        slea.skip(4);
        slea.skip(4);
        final List<Pair<Integer, List<Long>>> allDamage = new ArrayList<Pair<Integer, List<Long>>>();
        long damage = 0L;
        long totDamageToOneMonster = 0L;
        for (int i = 0; i < numAttacked; ++i) {
            final int objectId = slea.readInt();
            slea.readInt();
            slea.readByte();
            slea.readByte();
            slea.readByte();
            slea.readByte();
            slea.readByte();
            slea.readInt();
            slea.readByte();
            slea.skip(4);
            slea.skip(4);
            slea.readInt();
            slea.skip(2);
            slea.readInt();
            slea.readInt();
            slea.readByte();
            final List<Long> damages = new ArrayList<Long>();
            for (int j = 0; j < hits; ++j) {
                totDamageToOneMonster += damage;
                damage = slea.readLong();
                if (damage < 0L) {
                    damage &= 0xFFFFFFFFL;
                }
                damages.add(damage);
            }
            slea.skip(4);
            GameConstants.attackSkeletonImage(slea, new AttackInfo());
            allDamage.add(new Pair<Integer, List<Long>>(objectId, damages));
        }
        map.broadcastMessage(chr, CField.SummonPacket.summonAttack(summon, (skillid2 != 0) ? skillid2 : skillid, animation, tbyte, allDamage, chr.getLevel(), pos, false), summon.getTruePosition());
        final Skill summonSkill = SkillFactory.getSkill(summon.getSkill());
        SecondaryStatEffect summonEffect = summonSkill.getEffect(summon.getSkillLevel());
        if (summonEffect == null) {
            chr.dropMessage(5, "Error in attack.");
            return;
        }
        int n3 = 0;
        final int n4 = 0;
        int killmobsize = 0;
        int attackbossmob = 0;
        int plustime = 0;
        MapleMonster mob = null;
        for (final Pair<Integer, List<Long>> attackEntry : allDamage) {
            mob = map.getMonsterByOid(attackEntry.left);
            if (mob == null) {
                continue;
            }
            for (final Long toDamage : attackEntry.right) {
                totDamageToOneMonster += toDamage;
            }
            final List<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
            final List<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz2 = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
            switch (skillid) {
                case 152101000: {
                    if (chr.getSkillLevel(152100012) > 0) {
                        final SecondaryStatEffect curseMark = SkillFactory.getSkill(152000010).getEffect(chr.getSkillLevel(152000010));
                        final int max = (chr.getSkillLevel(152100012) > 0) ? 5 : ((chr.getSkillLevel(152110010) > 0) ? 3 : 1);
                        if (mob.getBuff(152000010) == null && mob.getCustomValue0(152000010) > 0L) {
                            mob.removeCustomInfo(152000010);
                        }
                        if (mob.getCustomValue0(152000010) < max) {
                            mob.addSkillCustomInfo(152000010, 1L);
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_CurseMark, new MonsterStatusEffect(152000010, curseMark.getDuration(), curseMark.getY() * mob.getCustomValue0(152000010)), mob.getCustomValue0(152000010)));
                        break;
                    }
                    break;
                }
                case 164121008: {
                    chr.setSkillCustomInfo(164121008, chr.getSkillCustomValue0(164121008) + 1L, 0L);
                    if (chr.getSkillCustomValue0(164121008) >= summonEffect.getZ()) {
                        chr.removeSkillCustomInfo(164121008);
                        if (chr.getSkillCustomValue0(164121009) < summonEffect.getX()) {
                            chr.setSkillCustomInfo(164121009, chr.getSkillCustomValue0(164121009) + (mob.getStats().isBoss() ? 3 : 1), 0L);
                            if (chr.getSkillCustomValue0(164121009) > summonEffect.getX()) {
                                chr.setSkillCustomInfo(164121009, summonEffect.getX(), 0L);
                            }
                        }
                    }
                    chr.getMap().broadcastMessage(CField.SummonPacket.AbsorbentEdificeps(chr.getId(), obj.getObjectId(), 10, 5));
                    break;
                }
                case 400011077:
                case 400011078: {
                    final int force = (skillid == 400011077) ? summonEffect.getS() : 12;
                    c.getPlayer().handleForceGain(mob.getObjectId(), skillid, force);
                    break;
                }
                case 35111002: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, 5000), 1L));
                    break;
                }
                case 1311019:
                case 1321024:
                case 1321025:
                case 1310018:
                case 1301014: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, (summonEffect.getSubTime() > 0) ? summonEffect.getSubTime() : summonEffect.getDuration()), 1L));
                    break;
                }
                case 2111010: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(skillid, summonEffect.getDOTTime()), summonEffect.getDOT() * totDamageToOneMonster / allDamage.size() / 10000L));
                    summon.removeSummon(map, false);
                    chr.removeSummon(summon);
                    break;
                }
                case 2121005: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(skillid, summonEffect.getDOTTime()), summonEffect.getDOT() * totDamageToOneMonster / allDamage.size() / 10000L));
                    break;
                }
                case 2221005: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, (summonEffect.getSubTime() > 0) ? summonEffect.getSubTime() : summonEffect.getDuration()), (long) summonEffect.getV()));
                    if (mob.getFreezingOverlap() < 5) {
                        mob.setFreezingOverlap((byte) (mob.getFreezingOverlap() + 1));
                        break;
                    }
                    break;
                }
                case 2321003: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_ElementResetBySummon, new MonsterStatusEffect(skillid, (summonEffect.getSubTime() > 0) ? summonEffect.getSubTime() : summonEffect.getDuration()), (long) summonEffect.getX()));
                    break;
                }
                case 3111005: {
                    if (Randomizer.rand(0, 100) < 41) {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, 5000), 1L));
                        break;
                    }
                    break;
                }
                case 3211005: {
                    if (!mob.getStats().isBoss()) {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(skillid, 5000), (long) summonEffect.getX()));
                        break;
                    }
                    break;
                }
                case 3311009: {
                    if (chr.getSkillLevel(3320000) > 0) {
                        final SecondaryStatEffect energyCharge = SkillFactory.getSkill(3320000).getEffect(1);
                        if (energyCharge != null) {
                            final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
                            final int max2 = energyCharge.getU();
                            final int add = energyCharge.getS();
                            if (chr.energy < max2) {
                                chr.energy = Math.min(max2, chr.energy + add);
                            }
                            statups.put(SecondaryStat.RelikGauge, new Pair<Integer, Integer>(chr.energy, 0));
                            chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, null, chr));
                        }
                        break;
                    }
                    break;
                }
                case 3221014: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, (summonEffect.getSubTime() > 0) ? summonEffect.getSubTime() : summonEffect.getDuration()), 1L));
                    break;
                }
                case 23111008: {
                    if (!mob.getStats().isBoss()) {
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(skillid, summonEffect.getSubTime()), -10L));
                        break;
                    }
                    break;
                }
                case 23111009: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned, new MonsterStatusEffect(skillid, summonEffect.getDOTTime()), summonEffect.getDOT() * totDamageToOneMonster / allDamage.size() / 10000L));
                    break;
                }
                case 61111002:
                case 61111220: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, (summonEffect.getSubTime() > 0) ? summonEffect.getSubTime() : summonEffect.getDuration()), (long) summonEffect.getX()));
                    break;
                }
                case 400021067: {
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed, new MonsterStatusEffect(skillid, (summonEffect.getSubTime() > 0) ? summonEffect.getSubTime() : summonEffect.getDuration()), (long) summonEffect.getV()));
                    if (mob.getFreezingOverlap() < 5) {
                        mob.setFreezingOverlap((byte) (mob.getFreezingOverlap() + ((numAttacked == 1) ? summonEffect.getZ() : 1)));
                        break;
                    }
                    break;
                }
                case 400021033: {
                    final SecondaryStatEffect Eff = SkillFactory.getSkill(400021032).getEffect(c.getPlayer().getSkillLevel(400021052));
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_ElementResetBySummon, new MonsterStatusEffect(skillid, (summonEffect.getSubTime() > 0) ? summonEffect.getSubTime() : summonEffect.getDuration()), (long) Eff.getQ2()));
                    break;
                }
                case 400051023: {
                    summonEffect = SkillFactory.getSkill(400051022).getEffect(chr.getSkillLevel(400051023));
                    if (mob.getBuff(MonsterStatus.MS_FixdamRBuff) == null && mob.getCustomValue0(400051023) > 0L) {
                        mob.setCustomInfo(400051023, 0, 0);
                    }
                    if (mob.getCustomValue0(400051023) < 10L) {
                        mob.addSkillCustomInfo(400051023, 1L);
                    }
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_FixdamRBuff, new MonsterStatusEffect(skillid, summonEffect.getS2() * 1000), mob.getCustomValue0(400051023)));
                    break;
                }
                case 400021069: {
                    if (chr.getBuffedValue(32121056) || !chr.getBuffedValue(400021069)) {
                        break;
                    }
                    double nowPlus = mob.getStats().isBoss() ? summonEffect.getZ() : ((mob.getHp() <= totDamageToOneMonster) ? 0.2 : 0.0);
                    nowPlus *= 1000.0;
                    plustime += (int) nowPlus;
                    if (nowPlus > 0.0) {
                        final MapleAtom atom = new MapleAtom(true, mob.getObjectId(), 29, true, 400021069, mob.getTruePosition().x, mob.getTruePosition().y);
                        atom.setDwUserOwner(chr.getId());
                        atom.setDwFirstTargetId(0);
                        final ForceAtom fr = new ForceAtom(4, 37, Randomizer.rand(5, 10), 62, 0);
                        fr.setnAttackCount((int) nowPlus);
                        atom.addForceAtom(fr);
                        chr.getClient().send(CField.createAtom(atom));
                        break;
                    }
                    break;
                }
            }
            final List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
            final List<Pair<MonsterStatus, MonsterStatusEffect>> applys2 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
            for (final Triple<MonsterStatus, MonsterStatusEffect, Long> status : statusz) {
                if (status.left != null && status.mid != null && summonEffect.makeChanceResult()) {
                    if (status.left == MonsterStatus.MS_Burned && status.right < 0L) {
                        status.right = ((long) status.right & 0xFFFFFFFFL);
                    }
                    status.mid.setValue(status.right);
                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(status.left, status.mid));
                }
            }
            for (final Triple<MonsterStatus, MonsterStatusEffect, Long> status : statusz2) {
                if (status.left != null && status.mid != null && summonEffect.makeChanceResult()) {
                    if (status.left == MonsterStatus.MS_Burned && status.right < 0L) {
                        status.right = ((long) status.right & 0xFFFFFFFFL);
                    }
                    status.mid.setValue(status.right);
                    applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(status.left, status.mid));
                }
            }
            mob.applyStatus(c, applys, summonEffect);
            if (applys2.size() > 0) {
                mob.applyStatus(c, applys2, summonEffect);
            }
            statusz.clear();
            if (chr.getBuffedValue(SecondaryStat.BMageDeath) != null && GameConstants.isBMDarkAtackSkill(skillid) && chr.getBuffedValue(SecondaryStat.AttackCountX) != null) {
                chr.setSkillCustomInfo(32001014, 0L, -500L);
            }
            switch (skillid2) {
                case 33001016: {
                    if (mob.getBuff(MonsterStatus.MS_JaguarBleeding) == null && mob.getAnotherByte() > 0) {
                        mob.setAnotherByte(0);
                    }
                    chr.addHP(chr.getStat().getCurrentMaxHp() * SkillFactory.getSkill(33001016).getEffect(chr.getSkillLevel(33001016)).getQ() / 100L);
                    if (mob.getAnotherByte() == 0) {
                        mob.setAnotherByte(1);
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                        break;
                    }
                    if (Randomizer.isSuccess(30)) {
                        if (mob.getAnotherByte() < 3) {
                            mob.setAnotherByte(mob.getAnotherByte() + 1);
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                        break;
                    }
                    break;
                }
                case 33101115: {
                    if (mob.getBuff(MonsterStatus.MS_JaguarBleeding) == null && mob.getAnotherByte() > 0) {
                        mob.setAnotherByte(0);
                    }
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun, new MonsterStatusEffect(skillid, 3000), 1L));
                    if (mob.getAnotherByte() == 0) {
                        mob.setAnotherByte(1);
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                        break;
                    }
                    if (Randomizer.isSuccess(80)) {
                        if (mob.getAnotherByte() < 3) {
                            mob.setAnotherByte(mob.getAnotherByte() + 1);
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                        break;
                    }
                    break;
                }
                case 33111015: {
                    if (mob.getBuff(MonsterStatus.MS_JaguarBleeding) == null && mob.getAnotherByte() > 0) {
                        mob.setAnotherByte(0);
                    }
                    if (mob.getAnotherByte() == 0) {
                        mob.setAnotherByte(1);
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                        break;
                    }
                    if (Randomizer.isSuccess(40)) {
                        if (mob.getAnotherByte() < 3) {
                            mob.setAnotherByte(mob.getAnotherByte() + 1);
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                        break;
                    }
                    break;
                }
                case 33121017: {
                    statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze, new MonsterStatusEffect(33121017, 10000), 1L));
                    statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Smite, new MonsterStatusEffect(33121017, 10000), 1L));
                    if (mob.getBuff(MonsterStatus.MS_JaguarBleeding) == null && mob.getAnotherByte() > 0) {
                        mob.setAnotherByte(0);
                    }
                    if (mob.getAnotherByte() < 3) {
                        mob.setAnotherByte(mob.getAnotherByte() + 1);
                    }
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                    break;
                }
                case 33121255: {
                    if (mob.getBuff(MonsterStatus.MS_JaguarBleeding) == null && mob.getAnotherByte() > 0) {
                        mob.setAnotherByte(0);
                    }
                    if (mob.getAnotherByte() < 3) {
                        mob.setAnotherByte(mob.getAnotherByte() + 1);
                    }
                    statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                    break;
                }
                default: {
                    if (skillid >= 33001007 && skillid <= 33001015 && Randomizer.isSuccess(15)) {
                        if (mob.getBuff(MonsterStatus.MS_JaguarBleeding) == null && mob.getAnotherByte() > 0) {
                            mob.setAnotherByte(0);
                        }
                        if (mob.getAnotherByte() < 3) {
                            mob.setAnotherByte(mob.getAnotherByte() + 1);
                        }
                        statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding, new MonsterStatusEffect(33000036, 15000), (long) mob.getAnotherByte()));
                        break;
                    }
                    break;
                }
            }
            applys.clear();
            for (final Triple<MonsterStatus, MonsterStatusEffect, Long> status : statusz) {
                if (status.left != null && status.mid != null) {
                    if (status.left == MonsterStatus.MS_Burned && status.right < 0L) {
                        status.right = ((long) status.right & 0xFFFFFFFFL);
                    }
                    status.mid.setValue(status.right);
                    applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(status.left, status.mid));
                }
            }
            for (final Triple<MonsterStatus, MonsterStatusEffect, Long> status : statusz2) {
                if (status.left != null && status.mid != null) {
                    if (status.left == MonsterStatus.MS_Burned && status.right < 0L) {
                        status.right = ((long) status.right & 0xFFFFFFFFL);
                    }
                    status.mid.setValue(status.right);
                    applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(status.left, status.mid));
                }
            }
            mob.applyStatus(c, applys, summonEffect);
            if (applys2.size() > 0) {
                mob.applyStatus(c, applys2, summonEffect);
            }
            if (sse == null || sse.delay <= 0 || summon.getMovementType() == SummonMovementType.STATIONARY || summon.getMovementType() == SummonMovementType.CIRCLE_STATIONARY || summon.getMovementType() == SummonMovementType.WALK_STATIONARY || chr.getTruePosition().distanceSq(mob.getTruePosition()) > 400000.0) {
            }
            mob.damage(chr, totDamageToOneMonster, true);
            chr.checkMonsterAggro(mob);
            if (mob.getStats().isBoss()) {
                ++attackbossmob;
            }
            if (mob.isAlive()) {
                continue;
            }
            chr.getClient().getSession().writeAndFlush((Object) MobPacket.killMonster(mob.getObjectId(), 1));
            if (mob.isBuffed(summonSkill.getId()) && GameConstants.isBattleMage(chr.getJob())) {
                byte size = 0;
                for (final MapleSummon sum : map.getAllSummonsThreadsafe()) {
                    if (sum.getSkill() == skillid) {
                        ++size;
                    }
                }
                if (size < 10 && skillid != 32001014 && skillid != 2111010 && skillid != 32100010 && skillid != 32110017 && skillid != 32120019 && !summon.isNoapply()) {
                    summonEffect.applyTo(chr, false);
                }
            }
            n3 = (byte) (n3 + 1);
            ++killmobsize;
        }
        if (summon.getSkill() == 12120013) {
            c.getPlayer().setIgnition(0);
            final SecondaryStatEffect sub = SkillFactory.getSkill(400021042).getEffect(chr.getSkillLevel(400021042));
            if (sub.getCooldown(chr) > 0) {
                chr.addCooldown(skillid2, System.currentTimeMillis(), sub.getCooldown(chr));
                c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid2, sub.getCooldown(chr)));
            }
            c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
        } else if (skillid2 == 152101006) {
            if (summon.getCrystalSkills().size() > 0 && summon.getCrystalSkills().get(0)) {
                summon.getCrystalSkills().set(0, false);
                c.getSession().writeAndFlush((Object) CField.SummonPacket.transformSummon(summon, 2));
                c.getSession().writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 3));
            }
        } else if (skillid == 35121011) {
            summon.removeSummon(map, false);
        } else if (skillid == 400021068 || skillid2 == 400021062) {
            c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon, 5));
        }
        if (!GameConstants.isCaptain(c.getPlayer().getJob()) && summon.getSkill() != 2211011 && summon.getSkill() != 3221014 && summon.getSkill() != 25121133 && summon.getSkill() != 400021068 && skillid != 400021095 && skillid != 164121008 && skillid != 36121002 && skillid != 36121013 && skillid != 5320011 && skillid != 5321004 && skillid != 5211014 && skillid != 14121003 && skillid != 400021071 && (skillid < 33001007 || skillid > 33001015) && summonEffect.getCooldown(c.getPlayer()) > 0 && c.getPlayer().getCooldownLimit(summon.getSkill()) == 0L) {
            c.getPlayer().addCooldown(summon.getSkill(), System.currentTimeMillis(), summonEffect.getCooldown(c.getPlayer()));
            c.getSession().writeAndFlush((Object) CField.skillCooldown(summon.getSkill(), summonEffect.getCooldown(c.getPlayer())));
        }
        if (SkillFactory.getSkill(skillid2) != null && summon.getSkill() != 25121133 && summon.getSkill() != 3221014) {
            final SecondaryStatEffect subSummonEffect = SkillFactory.getSkill(skillid2).getEffect(chr.getSkillLevel(skillid2));
            if (subSummonEffect.getCooldown(c.getPlayer()) > 0 && c.getPlayer().getCooldownLimit(skillid2) == 0L) {
                c.getPlayer().addCooldown(skillid2, System.currentTimeMillis(), subSummonEffect.getCooldown(c.getPlayer()));
                c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid2, subSummonEffect.getCooldown(c.getPlayer())));
            }
        }
        if (summon.getSkill() == 400041038) {
            summon.removeSummon(c.getPlayer().getMap(), false);
            c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.IndieSummon, summon.getSkill());
        } else if (GameConstants.isPathFinder(chr.getJob()) && summon.getSkill() != 400031051) {
            MapleCharacter.렐릭게이지(chr.getClient(), skillid);
        } else if (summon.getSkill() == 400051046) {
            if (!summon.isSpecialSkill() && summon.getEnergy() < 8) {
                summon.setEnergy(summon.getEnergy() + 1);
                chr.getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon, 2));
                chr.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
            }
        } else if (summon.getSkill() == 400011065) {
            summon.removeSummon(c.getPlayer().getMap(), false);
            chr.setSkillCustomInfo(400011065, 0L, 5000L);
        }
        if (killmobsize > 0) {
            chr.CombokillHandler(mob, 1, killmobsize);
        }
    }

    public static final void RemoveSummon(final LittleEndianAccessor slea, final MapleClient c) {
        final MapleMapObject obj = c.getPlayer().getMap().getMapObject(slea.readInt(), MapleMapObjectType.SUMMON);
        if (obj == null || !(obj instanceof MapleSummon)) {
            return;
        }
        final MapleSummon summon = (MapleSummon) obj;
        if (summon.getOwner().getId() != c.getPlayer().getId() || summon.getSkillLevel() <= 0) {
            // c.getPlayer().dropMessageGM(5, "Error.");
            return;
        }
        if (summon.getSkill() == 35111002 || summon.getSkill() == 400031049 || summon.getSkill() == 1301013) {
            return;
        }
        if (summon.getSkill() == 400021047 || summon.getSkill() == 400021063 || summon.getSkill() == 400031047 || summon.getSkill() == 400041033 || summon.getSkill() == 400041034) {
            final byte type = slea.readByte();
            final int skillid = slea.readInt();
            final int level = slea.readInt();
            final int unk1 = slea.readInt();
            final int unk2 = slea.readInt();
            final int bullet = slea.readInt();
            final Point pos1 = slea.readPos();
            final Point pos2 = slea.readIntPos();
            final Point pos3 = slea.readIntPos();
            final int unk3 = slea.readInt();
            final int unk4 = slea.readInt();
            final int unk5 = slea.readByte();
            final List<MatrixSkill> skills = GameConstants.matrixSkills(slea);
            c.getSession().writeAndFlush((Object) CWvsContext.MatrixSkill(skillid, level, skills));
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.SummonPacket.getSummonSkillAttackEffect(summon, type, skillid, level, unk1, unk2, bullet, pos1, pos2, pos3, unk3, unk4, unk5, skills), false);
        }
        if (summon.getSkill() == 400031047 || summon.getSkill() == 400041048 || summon.getSkill() == 400021047 || summon.getSkill() == 400021063) {
            return;
        }
        summon.removeSummon(c.getPlayer().getMap(), false);
        if (summon.getSkill() != 35121011 && summon.getSkill() != 400051011) {
            c.getPlayer().cancelEffect(SkillFactory.getSkill(summon.getSkill()).getEffect(c.getPlayer().getSkillLevel(summon.getSkill())));
        }
    }

    public static final void SubSummon(final LittleEndianAccessor slea, final MapleCharacter chr) {
        if (chr == null || chr.getMap() == null) {
            return;
        }
        final MapleMapObject obj = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.SUMMON);
        if (obj == null || !(obj instanceof MapleSummon)) {
            return;
        }
        final MapleSummon sum = (MapleSummon) obj;
        if (sum == null || sum.getOwner().getId() != chr.getId() || sum.getSkillLevel() <= 0 || !chr.isAlive()) {
            return;
        }
        SecondaryStatEffect eff = SkillFactory.getSkill(sum.getSkill()).getEffect(sum.getSkillLevel());
        switch (sum.getSkill()) {
            case 5210015: {
                slea.skip(18);
                int mobid = slea.readInt();
                final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
                atoms.add(new SecondAtom(0x22, chr.getId(), mobid, 5201017, 4000, 0, 1, new Point(sum.getTruePosition().x, sum.getTruePosition().y), Arrays.asList(0)));
                atoms.add(new SecondAtom(0x22, chr.getId(), mobid, 5201017, 4000, 0, 1, new Point(sum.getTruePosition().x, sum.getTruePosition().y), Arrays.asList(0)));
                chr.spawnSecondAtom(atoms);
                break;
            }
            case 36121014: {
                if (chr.getParty() != null) {
                    for (final MaplePartyCharacter pchr : chr.getParty().getMembers()) {
                        final MapleCharacter chr2 = chr.getClient().getChannelServer().getPlayerStorage().getCharacterByName(pchr.getName());
                        if (chr2 != null && pchr.isOnline() && sum.getTruePosition().x + eff.getLt().x < chr2.getTruePosition().x && sum.getTruePosition().x - eff.getLt().x > chr2.getTruePosition().x && sum.getTruePosition().y + eff.getLt().y < chr2.getTruePosition().y && sum.getTruePosition().y - eff.getLt().y > chr2.getTruePosition().y) {
                            SkillFactory.getSkill(36121014).getEffect(sum.getSkillLevel()).applyTo(chr, chr2, false);
                        }
                    }
                    break;
                }
                if (sum.getTruePosition().x + eff.getLt().x < chr.getTruePosition().x && sum.getTruePosition().x - eff.getLt().x > chr.getTruePosition().x && sum.getTruePosition().y + eff.getLt().y < chr.getTruePosition().y && sum.getTruePosition().y - eff.getLt().y > chr.getTruePosition().y) {
                    SkillFactory.getSkill(36121014).getEffect(sum.getSkillLevel()).applyTo(chr, false);
                    break;
                }
                break;
            }
            case 400041044: {
                final Rectangle box = new Rectangle(sum.getTruePosition().x - 320, sum.getTruePosition().y - 490, 640, 530);
                if (chr.getParty() != null) {
                    for (final MaplePartyCharacter pchr2 : chr.getParty().getMembers()) {
                        final MapleCharacter chr3 = chr.getClient().getChannelServer().getPlayerStorage().getCharacterByName(pchr2.getName());
                        if (chr3 != null && pchr2.isOnline() && box.contains(chr3.getPosition())) {
                            SkillFactory.getSkill(400041047).getEffect(sum.getSkillLevel()).applyTo(chr, chr3);
                        }
                    }
                    break;
                }
                if (box.contains(chr.getPosition())) {
                    SkillFactory.getSkill(400041047).getEffect(sum.getSkillLevel()).applyTo(chr);
                    break;
                }
                break;
            }
            case 25121133: {
                eff = SkillFactory.getSkill(25121209).getEffect(chr.getSkillLevel(25121209));
                chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 25121209, 2, 0, 25121133, (byte) (chr.isFacingLeft() ? 1 : 0), true, null, null, null));
                chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 25121209, 2, 0, 25121133, (byte) (chr.isFacingLeft() ? 1 : 0), false, null, null, null), false);
                eff.applyTo(chr);
                break;
            }
            case 400021032: {
                final SecondaryStatEffect pismaker = SkillFactory.getSkill(400021052).getEffect(chr.getSkillLevel(400021032));
                if (chr.getMapId() != 450013700 && chr.getParty() != null) {
                    for (final MaplePartyCharacter Pchar : chr.getParty().getMembers()) {
                        final MapleCharacter Pchar2 = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(Pchar.getId());
                        if (Pchar2 != null) {
                            Pchar2.addHP(Pchar2.getStat().getCurrentMaxHp() / 100L * 10L);
                            pismaker.applyTo(chr, Pchar2);
                        }
                    }
                }
                if (chr.getParty() == null) {
                    pismaker.applyTo(chr);
                }
                chr.getMap().broadcastMessage(CField.SummonPacket.summonSkill(chr.getId(), sum.getObjectId(), 14));
                break;
            }
            case 35121009: {
                if (!chr.canSummon(eff.getX() * 1000)) {
                    return;
                }
                final int skillId = slea.readInt();
                if (sum.getSkill() != skillId) {
                    return;
                }
                for (final MapleSummon summon : chr.getMap().getAllSummonsThreadsafe()) {
                    if (summon.getSkill() == sum.getSkill()) {
                        slea.skip(1);
                        slea.readInt();
                        for (int i = 0; i < 3; ++i) {
                            final MapleSummon tosummon = new MapleSummon(chr, SkillFactory.getSkill(35121011).getEffect(sum.getSkillLevel()), new Point(sum.getTruePosition().x, sum.getTruePosition().y - 5), SummonMovementType.WALK_STATIONARY);
                            chr.getMap().spawnSummon(tosummon, 5000);
                        }
                        return;
                    }
                }
                break;
            }
            case 35111008:
            case 35120002: {
                if (!chr.canSummon(eff.getX() * 1000)) {
                    return;
                }
                if (chr.getParty() != null) {
                    for (final MaplePartyCharacter pc : chr.getParty().getMembers()) {
                        final int ch = World.Find.findChannel(pc.getId());
                        if (ch > 0) {
                            final MapleCharacter player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(pc.getId());
                            if (player == null) {
                                continue;
                            }
                            player.addHP((int) (player.getStat().getCurrentMaxHp() * eff.getHp() / 100.0));
                            player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showSummonEffect(player, sum.getSkill(), true));
                            player.getMap().broadcastMessage(player, CField.EffectPacket.showSummonEffect(player, sum.getSkill(), false), false);
                        }
                    }
                } else {
                    chr.addHP((int) (chr.getStat().getCurrentMaxHp() * eff.getHp() / 100.0));
                    chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showSummonEffect(chr, sum.getSkill(), true));
                    chr.getMap().broadcastMessage(chr, CField.EffectPacket.showSummonEffect(chr, sum.getSkill(), false), false);
                }
                final List<Pair<MonsterStatus, MonsterStatusEffect>> datas = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
                datas.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr, new MonsterStatusEffect(sum.getSkill(), sum.getDuration(), -SkillFactory.getSkill(35111008).getEffect(sum.getSkillLevel()).getW())));
                datas.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(sum.getSkill(), sum.getDuration(), -SkillFactory.getSkill(35111008).getEffect(sum.getSkillLevel()).getW())));
                for (final MapleMonster mob : chr.getMap().getAllMonstersThreadsafe()) {
                    if (!mob.isBuffed(sum.getSkill())) {
                        mob.applyStatus(chr.getClient(), datas, eff);
                    }
                }
                break;
            }
            case 1301013: {
                final Skill bHealing = SkillFactory.getSkill(slea.readInt());
                final int bHealingLvl = chr.getTotalSkillLevel(bHealing);
                final int bDomilvl = chr.getTotalSkillLevel(1310013);
                if (bHealingLvl <= 0 || bHealing == null) {
                    return;
                }
                final SecondaryStatEffect healEffect = bHealing.getEffect(bHealingLvl);
                if (bHealing.getId() == 1310016) {
                    healEffect.applyTo(chr, true);
                    chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showSummonEffect(chr, 1310016, true));
                    chr.getMap().broadcastMessage(chr, CField.EffectPacket.showSummonEffect(chr, 1310016, false), false);
                    chr.getMap().broadcastMessage(CField.SummonPacket.summonSkill(chr.getId(), sum.getObjectId(), 14));
                    break;
                }
                if (bHealing.getId() == 1301013) {
                    int healhp = healEffect.getHp();
                    if (chr.getSkillLevel(1320045) > 0) {
                        healhp += (int) (chr.getStat().getCurrentMaxHp() / 100L * SkillFactory.getSkill(1320045).getEffect(1).getX());
                    }
                    chr.addHP(healhp);
                    chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showSummonEffect(chr, 1301013, true));
                    chr.getMap().broadcastMessage(chr, CField.EffectPacket.showSummonEffect(chr, 1301013, false), false);
                    chr.getMap().broadcastMessage(CField.SummonPacket.summonSkill(chr.getId(), sum.getObjectId(), 15));
                    break;
                }
                break;
            }
            case 152101000: {
                final Skill harmonyLink = SkillFactory.getSkill(slea.readInt());
                if (harmonyLink.getId() == 152111007 && sum.getCrystalSkills().size() >= 2 && sum.getCrystalSkills().get(1)) {
                    chr.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.transformSummon(sum, 1));
                    harmonyLink.getEffect(chr.getSkillLevel(152111007)).applyTo(chr, false);
                    sum.getCrystalSkills().set(1, false);
                    chr.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.transformSummon(sum, 2));
                    chr.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(sum, 3));
                    break;
                }
                break;
            }
            case 400001013: {
                chr.setSkillCustomInfo(400001016, 2L, 0L);
                eff = SkillFactory.getSkill(400001016).getEffect(chr.getSkillLevel(400001013));
                eff.applyTo(chr);
                break;
            }
            case 400041038: {
                Item nk = null;
                for (short position = 0; position < chr.getInventory(MapleInventoryType.USE).newList().size(); ++position) {
                    nk = chr.getInventory(MapleInventoryType.USE).newList().get(position);
                    if (nk != null) {
                        if (nk.getItemId() / 10000 == 207) {
                            break;
                        }
                    }
                }
                if (nk == null) {
                    break;
                }
                final List<MapleMapObject> objs = chr.getMap().getMapObjectsInRange(sum.getTruePosition(), 500000.0, Arrays.asList(MapleMapObjectType.MONSTER));
                final List<Integer> monsters = new ArrayList<Integer>();
                for (int j = 0; j < eff.getMobCount(); ++j) {
                    int rand;
                    if (objs.size() <= 1) {
                        rand = 1;
                    } else {
                        rand = Randomizer.nextInt(objs.size());
                    }
                    if (objs.size() < eff.getBulletCount()) {
                        if (j < objs.size()) {
                            monsters.add(objs.get(j).getObjectId());
                        }
                    } else if (objs.size() > 1) {
                        monsters.add(objs.get(rand).getObjectId());
                        objs.remove(rand);
                    }
                }
                if (monsters.size() > 0) {
                    final List<ForceAtom> atoms = new ArrayList<ForceAtom>();
                    final MapleAtom atom = new MapleAtom(false, chr.getId(), 49, true, 400041038, sum.getTruePosition().x, sum.getTruePosition().y - 400);
                    atom.setDwSummonObjectId(sum.getObjectId());
                    int key = 0;
                    for (final Integer m : monsters) {
                        for (int k = 0; k < eff.getBulletCount(); ++k) {
                            atom.addForceAtom(new ForceAtom(2, Randomizer.rand(40, 44), Randomizer.rand(3, 4), 360 / (monsters.size() * 5 + 7) * key, 200));
                            ++key;
                        }
                    }
                    for (int l = 0; l < eff.getX(); ++l) {
                        atom.addForceAtom(new ForceAtom(2, Randomizer.rand(40, 44), Randomizer.rand(3, 4), 360 / (monsters.size() * 5 + 7) * key, 200));
                        ++key;
                    }
                    atom.setDwTargets(monsters);
                    atom.setnItemId((chr.getV("csstar") != null) ? Integer.parseInt(chr.getV("csstar")) : nk.getItemId());
                    chr.getMap().spawnMapleAtom(atom);
                    chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, sum.getSkill(), sum.getSkill(), 4, 0, 0, (byte) 0, true, null, null, null));
                    chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, sum.getSkill(), sum.getSkill(), 4, 0, 0, (byte) 0, false, null, null, null), false);
                    break;
                }
                break;
            }
        }
        if (GameConstants.isAngel(sum.getSkill()) && chr.getBuffedEffect(SecondaryStat.RepeatEffect) != null) {
            if (sum.getSkill() % 10000 == 1087) {
                MapleItemInformationProvider.getInstance().getItemEffect(2022747).applyTo(chr, true);
            } else if (sum.getSkill() % 10000 == 1085) {
                MapleItemInformationProvider.getInstance().getItemEffect(2022746).applyTo(chr, true);
            } else if (sum.getSkill() % 10000 == 1090) {
                MapleItemInformationProvider.getInstance().getItemEffect(2022764).applyTo(chr, true);
            } else if (sum.getSkill() % 10000 == 1179) {
                MapleItemInformationProvider.getInstance().getItemEffect(2022823).applyTo(chr, true);
            } else {
                MapleItemInformationProvider.getInstance().getItemEffect(2022746).applyTo(chr, true);
            }
            final int skillid = chr.getBuffedEffect(SecondaryStat.RepeatEffect).getSourceId();
            chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showSummonEffect(chr, sum.getSkill(), true));
            chr.getMap().broadcastMessage(chr, CField.EffectPacket.showSummonEffect(chr, sum.getSkill(), false), false);
            final EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
            statups.put(SecondaryStat.RepeatEffect, new Pair<Integer, Integer>(1, 0));
            final SecondaryStatEffect effect = MapleItemInformationProvider.getInstance().getItemEffect(skillid);
            chr.getMap().broadcastMessage(CWvsContext.BuffPacket.giveForeignBuff(chr, statups, effect));
        }
    }

    public static void replaceSummon(LittleEndianAccessor slea, MapleClient c) {
        int skillId = slea.readInt();
        for (MapleSummon s : c.getPlayer().getSummons()) {
            if (GameConstants.getLinkedSkill(s.getSkill()) == skillId) {
                c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.removeSummon(s, false));
                s.setPosition(c.getPlayer().getTruePosition());
                c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.spawnSummon(s, true));
                continue;
            }
            if (skillId != 400031005 || s.getSkill() < 33001007 || s.getSkill() > 33001015 || s.getSkill() == GameConstants.getSelectJaguarSkillId(GameConstants.getMountItem(33001001, c.getPlayer()))) {
                continue;
            }
            c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.removeSummon(s, false));
            s.setPosition(new Point(c.getPlayer().getPosition().x + Randomizer.rand(-400, 400), c.getPlayer().getPosition().y));
            c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.spawnSummon(s, true));
        }
    }

    public static void effectSummon(final LittleEndianAccessor slea, final MapleClient c) {
        final int objectId = slea.readInt();
        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
            return;
        }
        final MapleSummon target = c.getPlayer().getMap().getSummonByOid(objectId);
        if (target != null) {
            slea.skip(8);
            final int skill = slea.readInt();
            final int type = (skill == 400011054) ? 11 : ((skill == 400021066) ? 9 : 0);
            if (SkillFactory.getSkill(skill) == null) {
                return;
            }
            if (type > 0) {
                c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.summonSkill(c.getPlayer().getId(), target.getObjectId(), type));
            }
            SkillFactory.getSkill(skill).getEffect(c.getPlayer().getSkillLevel(skill)).applyTo(c.getPlayer(), true);
            target.SetNoapply(true);
        }
    }

    public static void cancelEffectSummon(final LittleEndianAccessor slea, final MapleClient c) {
        final int objectId = slea.readInt();
        final MapleSummon target = c.getPlayer().getMap().getSummonByOid(objectId);
        if (target != null) {
            target.SetNoapply(false);
            if (target.getSkill() == 400051046) {
                if (target.getLastAttackTime() > 0L && !c.getPlayer().getBuffedValue(400051046)) {
                    target.removeSummon(c.getPlayer().getMap(), false);
                }
                target.setLastAttackTime(0L);
            }
        }
    }

    public static void specialSummon(final LittleEndianAccessor slea, final MapleClient c) {
        final int objectId = slea.readInt();
        final MapleCharacter chr = c.getPlayer();
        if (chr == null || chr.getMap() == null) {
            return;
        }
        final MapleSummon target = chr.getMap().getSummonByOid(objectId);
        if (target == null || target.getSkill() != 152101000) {
        }
    }

    public static void specialSummon5th(final LittleEndianAccessor slea, final MapleClient c) {
        final int objectId = slea.readInt();
        final Point pos = slea.readPos();
        final int skillid5th = slea.readInt();
        final int skillid = slea.readInt();
        final MapleCharacter chr = c.getPlayer();
        if (chr == null || chr.getMap() == null) {
            return;
        }
        final MapleSummon target = chr.getMap().getSummonByOid(objectId);
        if (target != null && System.currentTimeMillis() - target.getLastAttackTime() > SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid)).getCooldown(chr)) {
            target.setLastAttackTime(System.currentTimeMillis());
            c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.specialSummon(target, 4, skillid));
        }
    }

    public static void mechCarrier(final LittleEndianAccessor slea, final MapleClient c) {
    }
}
