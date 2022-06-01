// 
// Decompiled by Procyon v0.5.36
// 

package tools.packet;

import server.events.MapleBattleGroundMobInfo;
import constants.GameConstants;
import server.life.MapleMonster;
import java.util.List;
import java.awt.Point;
import client.MapleCharacter;
import java.util.Iterator;
import tools.Triple;
import handling.SendPacketOpcode;
import tools.data.MaplePacketLittleEndianWriter;
import server.events.MapleBattleGroundCharacter;

public class BattleGroundPacket
{
    public static byte[] UpgradeMainSkill(final MapleBattleGroundCharacter chr) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLEGROUND_UPGRADE_SKILL.getValue());
        mplew.writeInt(5);
        mplew.writeInt(chr.getAttackUp());
        mplew.writeInt(460);
        mplew.writeInt(100 + chr.getHpMpUp());
        mplew.writeInt(460);
        mplew.writeInt(200 + chr.getCriUp());
        mplew.writeInt(460);
        mplew.writeInt(300 + chr.getSpeedUp());
        mplew.writeInt(460);
        mplew.writeInt(400 + chr.getRegenUp());
        mplew.writeInt(460);
        return mplew.getPacket();
    }
    
    public static byte[] UpgradeSkillEffect(final MapleBattleGroundCharacter chr, final int skillid) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLEGROUND_UPGRADE_EFFECT.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillid);
        mplew.writeInt(840);
        return mplew.getPacket();
    }
    
    public static byte[] AvaterSkill(final MapleBattleGroundCharacter chr, final int type) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.USE_SKILL_WITH_UI.getValue());
        mplew.writeInt(type);
        mplew.writeInt(type);
        mplew.write(0);
        mplew.writeInt(chr.getSkillList().size());
        for (final Triple<Integer, Integer, Integer> skill : chr.getSkillList()) {
            mplew.write(skill.getLeft());
            mplew.writeInt(skill.getMid());
            mplew.writeInt(skill.getRight());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.writeInt(5000);
            mplew.write(0);
            mplew.write(0);
        }
        return mplew.getPacket();
    }
    
    public static byte[] ChangeAvater(final MapleBattleGroundCharacter chr, final int type) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_CHANGE_AVATER.getValue());
        mplew.writeInt(3);
        mplew.writeInt(chr.getId());
        mplew.writeInt(0);
        mplew.writeInt(chr.getExp());
        mplew.writeInt(chr.getMoney());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(type);
        mplew.writeInt(chr.getKill());
        mplew.writeInt(chr.getDeath());
        mplew.write(0);
        mplew.writeInt(chr.getTeam());
        mplew.writeInt(chr.getJobType());
        mplew.writeInt(chr.getLevel());
        mplew.writeInt(chr.getMindam());
        mplew.writeInt(chr.getMaxdam());
        mplew.writeInt(chr.getAttackSpeed());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(chr.getCritical());
        mplew.writeInt(chr.getHpRegen());
        mplew.writeInt(chr.getMp());
        mplew.writeInt(chr.getMpRegen());
        mplew.writeInt(chr.getMp());
        mplew.writeInt(chr.getSpeed());
        mplew.writeInt(chr.getJump());
        mplew.writeInt(chr.getMaxHp());
        mplew.writeInt(chr.getMaxMp());
        mplew.writeInt(0);
        return mplew.getPacket();
    }
    
    public static byte[] UpdateAvater(final MapleBattleGroundCharacter chr, final int type) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_UPDATE_AVATER.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(13);
        mplew.writeInt(chr.getId());
        mplew.writeInt(0);
        mplew.writeInt(chr.getExp());
        mplew.writeInt(chr.getMoney());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(chr.getChr().getSkillCustomValue0(80001741));
        mplew.writeInt(type);
        mplew.writeInt(chr.getKill());
        mplew.writeInt(chr.getDeath());
        mplew.write(1);
        mplew.writeInt(chr.getTeam());
        mplew.writeInt(chr.getJobType());
        mplew.writeInt(chr.getLevel());
        mplew.writeInt(chr.getMindam());
        mplew.writeInt(chr.getMaxdam());
        mplew.writeInt(chr.getAttackSpeed());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(chr.getCritical());
        mplew.writeInt(chr.getHpRegen());
        mplew.writeInt(chr.getMp());
        mplew.writeInt(chr.getMpRegen());
        mplew.writeInt(chr.getMp());
        mplew.writeInt(chr.getSpeed());
        mplew.writeInt(chr.getJump());
        mplew.writeInt(chr.getMaxHp());
        mplew.writeInt(chr.getMaxMp());
        mplew.writeInt(0);
        mplew.write(0);
        mplew.write(0);
        return mplew.getPacket();
    }
    
    public static byte[] AttackSkill(final MapleCharacter chr, final Point oldpos, final Point newpos, final int skillid, final int attackimg, final int delay, final int delay2, final int imgafter, final int speed, final int left, final int range, final List<Integer> mob, final int moving) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_ATTACK.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillid);
        mplew.write(1);
        mplew.writeInt(attackimg);
        mplew.writeInt(delay);
        mplew.writeInt(delay2);
        mplew.writeInt(moving);
        mplew.writeInt(1);
        mplew.writeInt(imgafter);
        mplew.writeInt((skillid == 80003009) ? 300 : 0);
        mplew.writeInt(mob.isEmpty() ? 1 : mob.size());
        if (mob.isEmpty()) {
            mplew.writeInt(chr.getSkillCustomValue0(156789));
            mplew.write(speed);
            mplew.write((skillid == 80001739) ? 2 : 0);
            mplew.write(left);
            mplew.writeInt(1);
            mplew.writePosInt(oldpos);
            mplew.writePosInt(newpos);
            Point bpos = new Point(newpos.x + ((left == 0) ? range : (-range)), newpos.y);
            if (skillid == 80001736) {
                bpos = new Point(600, 500);
            }
            else if (skillid == 80002678) {
                bpos = new Point(newpos.x + ((left == 0) ? range : (-range)), newpos.y + 75);
            }
            mplew.writePosInt(bpos);
            mplew.writeInt((skillid == 80001736) ? 15 : 0);
            mplew.writeInt((skillid == 80001736) ? 15 : 0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            int rangespeed = 0;
            if (range > 0) {
                rangespeed = 100;
                if (skillid == 80001736 || skillid == 80002339 || skillid == 80003003) {
                    rangespeed = 40;
                }
                else if (skillid == 80001735) {
                    rangespeed = 80;
                }
                else if (skillid == 80001664) {
                    rangespeed = 5;
                }
            }
            mplew.writeInt(rangespeed);
            mplew.writeInt(0);
            mplew.writeInt((skillid == 80001736) ? 1 : 0);
            mplew.writeInt(0);
        }
        else {
            for (final Integer mobs : mob) {
                mplew.writeInt(chr.getSkillCustomValue0(156789));
                mplew.write(speed);
                mplew.write((chr.getMap().getCharacter(mobs) != null) ? 1 : ((skillid == 80001650 || skillid == 80001739) ? 2 : 0));
                mplew.write(left);
                mplew.writeInt(mobs);
                final MapleMonster monster = chr.getMap().getMonsterByOid(mobs);
                if (skillid == 80001650) {
                    mplew.writePosInt(oldpos);
                    mplew.writePosInt(oldpos);
                    mplew.writePosInt(newpos);
                }
                else if (skillid == 80001739) {
                    if (monster == null) {
                        final MapleCharacter dchr = chr.getMap().getCharacter(mobs);
                        mplew.writePosInt(new Point(dchr.getPosition().x, dchr.getPosition().y));
                        mplew.writePosInt(new Point(dchr.getPosition().x, dchr.getPosition().y - 10));
                        mplew.writePosInt(new Point(dchr.getPosition().x, dchr.getPosition().y));
                    }
                    else {
                        mplew.writePosInt(new Point(monster.getPosition().x, monster.getPosition().y));
                        mplew.writePosInt(new Point(monster.getPosition().x, monster.getPosition().y - 10));
                        mplew.writePosInt(new Point(monster.getPosition().x, monster.getPosition().y));
                    }
                }
                else {
                    mplew.writePosInt(oldpos);
                    if (monster != null) {
                        mplew.writePosInt(monster.getPosition());
                    }
                    else {
                        mplew.writePosInt(newpos);
                    }
                    final Point bpos2 = new Point(newpos.x + ((left == 0) ? range : (-range)), newpos.y);
                    mplew.writePosInt(oldpos);
                }
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt((skillid == 80001739) ? 216352990 : 0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                chr.setSkillCustomInfo(156789, chr.getSkillCustomValue0(156789) + 1L, 0L);
            }
        }
        return mplew.getPacket();
    }
    
    public static byte[] AttackSkillStack(final MapleCharacter chr, final Point oldpos, final Point newpos, final int skillid, final int attackimg, final int delay, final int delay2, final int imgafter, final int speed, final int left, final int range, final int stack) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_ATTACK.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillid);
        mplew.write(1);
        mplew.writeInt(attackimg);
        mplew.writeInt(delay);
        mplew.writeInt(delay2);
        mplew.writeInt(0);
        mplew.writeInt(stack);
        for (int i = 0; i < stack; ++i) {
            mplew.writeInt((skillid == 80003006 && i == 1) ? (imgafter + 450) : imgafter);
            mplew.writeInt((skillid == 80003006 && i == 0) ? 30 : ((skillid == 80003006 && i == 1) ? 480 : 90));
            mplew.writeInt(1);
            mplew.writeInt(chr.getSkillCustomValue0(156789));
            mplew.write(speed);
            mplew.write(0);
            mplew.write(left);
            mplew.writeInt(0);
            mplew.writePosInt(oldpos);
            mplew.writePosInt(newpos);
            final Point bpos = new Point(newpos.x + ((left == 0) ? range : (-range)), newpos.y);
            if (skillid == 80002680) {
                if (i == 0) {
                    final Point point = bpos;
                    point.y -= 157;
                }
                else if (i == 2) {
                    final Point point2 = bpos;
                    point2.y += 157;
                }
            }
            else if (skillid != 80003006 && skillid != 80001741) {
                if (i == 0) {
                    final Point point3 = bpos;
                    point3.y += 190;
                }
                else if (i == 1) {
                    final Point point4 = bpos;
                    point4.y += 95;
                }
                else if (i == 3) {
                    final Point point5 = bpos;
                    point5.y -= 95;
                }
                else if (i == 4) {
                    final Point point6 = bpos;
                    point6.y -= 190;
                }
            }
            mplew.writePosInt(bpos);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt((skillid == 80001741) ? ((left == 0) ? (-1 * (276 + i * 276)) : (276 + i * 276)) : 0);
            mplew.writeInt((skillid == 80001741) ? ((left == 0) ? (-1 * (55 + i * 55)) : (55 + i * 55)) : 0);
            int rangespeed = 0;
            if (range > 0) {
                rangespeed = 100;
                if (skillid == 80001736 || skillid == 80002339 || skillid == 80003003) {
                    rangespeed = 40;
                }
                else if (skillid == 80001735 || skillid == 80003006) {
                    rangespeed = 80;
                }
                else if (skillid == 80001664) {
                    rangespeed = 5;
                }
                else if (skillid == 80001741) {
                    rangespeed = 55;
                }
            }
            mplew.writeInt(rangespeed);
            mplew.writeInt((skillid == 80001741) ? ((left == 0) ? -20 : 20) : 0);
            mplew.writeInt((skillid == 80001741) ? ((left == 0) ? -10 : 10) : 0);
            mplew.writeInt(0);
            chr.setSkillCustomInfo(156789, chr.getSkillCustomValue0(156789) + 1L, 0L);
        }
        return mplew.getPacket();
    }
    
    public static byte[] MoveAttack(final MapleCharacter chr, final Point pos, final Point pos1, final int attackcount) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_MOVE_ATTACK.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(attackcount);
        mplew.writeInt(4);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.writePosInt(pos);
        mplew.writePosInt(pos1);
        final Point pos2 = new Point(800, 600);
        mplew.writePosInt(pos2);
        mplew.writeInt(15);
        mplew.writeInt(15);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(40);
        mplew.writeInt(0);
        mplew.writeInt(1);
        mplew.writeInt(0);
        mplew.writeInt(1);
        return mplew.getPacket();
    }
    
    public static byte[] AttackRefresh(final MapleCharacter chr, final int stype, final int count, final int type, final int... args) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_ATTACK_REFRESH.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(stype);
        if (stype == 1) {
            mplew.writeInt(count);
            mplew.writeInt(type);
        }
        else {
            mplew.writeInt(count);
            mplew.writeInt(type);
            mplew.writeInt(args[0]);
            mplew.writeInt(args[1]);
        }
        return mplew.getPacket();
    }
    
    public static byte[] CoolDown(final int skillid, final int cool, final int maxcool) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_COOLDOWN.getValue());
        mplew.writeInt(skillid);
        mplew.writeInt(cool);
        mplew.writeInt(maxcool);
        return mplew.getPacket();
    }
    
    public static byte[] BonusAttack(final int skillid) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_ATTACK_BONUS.getValue());
        mplew.writeInt(skillid);
        return mplew.getPacket();
    }
    
    public static byte[] SkillOn(final MapleBattleGroundCharacter chr, final int skillid, final int skilllv, final int unk, final int skillid2, final int skillid3, final int cooltime) {
        return SkillOn(chr, skillid, skilllv, unk, skillid2, skillid3, cooltime, 0, 0);
    }
    
    public static byte[] SkillOn(final MapleBattleGroundCharacter chr, final int skillid, final int skilllv, final int unk, final int skillid2, final int skillid3, final int cooltime, final int nowstack, final int maxstack) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_SKILLON.getValue());
        mplew.writeInt(GameConstants.BattleGroundJobType(chr));
        mplew.writeShort(1);
        mplew.writeInt(skillid);
        mplew.write(skilllv);
        mplew.writeInt(unk);
        mplew.writeInt(skillid2);
        mplew.writeInt(skillid3);
        mplew.writeInt(nowstack);
        mplew.writeInt(maxstack);
        mplew.writeInt(cooltime);
        mplew.writeShort(0);
        mplew.write(0);
        return mplew.getPacket();
    }
    
    public static byte[] SkillOnList(final MapleBattleGroundCharacter chr, final int type, final List<Triple<Integer, Integer, Integer>> info) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_SKILLON.getValue());
        mplew.writeInt(type);
        for (final Triple<Integer, Integer, Integer> info2 : info) {
            mplew.writeShort(1);
            mplew.writeInt(info2.getLeft());
            mplew.write(info2.getMid());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(info2.getRight());
            mplew.writeShort(0);
        }
        mplew.write(0);
        return mplew.getPacket();
    }
    
    public static byte[] AttackDamage(final MapleCharacter chr, final Point pos1, final Point pos2, final Point pos3, final Point pos4, final Point pos5, final List<MapleBattleGroundMobInfo> minfo, final boolean people, final int... args) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_ATTACK_DAMAGE.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(args[0]);
        mplew.writeInt(args[1]);
        mplew.writeShort(args[2]);
        mplew.write(args[3]);
        mplew.writeInt(0);
        mplew.writePosInt(pos1);
        mplew.writePosInt(pos2);
        mplew.writePosInt(pos3);
        mplew.writePosInt(pos4);
        mplew.writePosInt(pos5);
        mplew.writeInt(args[5]);
        mplew.writeInt(args[6]);
        mplew.writeInt(args[7]);
        mplew.writeInt(args[8]);
        mplew.writeInt(args[9]);
        mplew.writeInt(args[10]);
        mplew.writeInt(args[11]);
        mplew.writeInt(args[12]);
        mplew.writeInt(people ? minfo.size() : 0);
        if (!people) {
            mplew.writeInt(people ? 0 : minfo.size());
        }
        int i = 0;
        for (final MapleBattleGroundMobInfo m : minfo) {
            mplew.writeInt(i);
            mplew.writeInt(m.getDamage());
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(m.getCritiCal() ? 1 : 0);
            mplew.write(0);
            mplew.writeInt((m.getSkillid() == 80001662) ? 300 : 0);
            mplew.writeInt(0);
            mplew.write(1);
            mplew.writeInt(m.getSkillid());
            mplew.write(1);
            mplew.writeInt(m.getCid());
            mplew.writeInt(m.getOid());
            mplew.writeInt(m.getUnk1());
            mplew.writeInt(m.getUnk2());
            mplew.writeInt(m.getUnk3());
            mplew.write(m.getUnk4());
            mplew.writePosInt(m.getPos1());
            mplew.writePosInt(m.getPos2());
            ++i;
        }
        mplew.writeZeroBytes(100);
        return mplew.getPacket();
    }
    
    public static byte[] TakeDamage(final MapleCharacter chr, final int oid, final int damage, final int type, final int heal) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_TAKEDAMAGE.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(oid);
        mplew.writeInt(damage);
        mplew.writeInt(heal);
        mplew.writeInt(-1);
        mplew.writeInt(type);
        mplew.write(0);
        mplew.writeInt((chr.getBuffedValue(80001655) || chr.getBuffedValue(80001740)) ? 1 : 0);
        if (chr.getBuffedValue(80001655)) {
            mplew.writeInt(80001655);
        }
        else if (chr.getBuffedValue(80001740)) {
            mplew.writeInt(80001740);
        }
        return mplew.getPacket();
    }
    
    public static byte[] ShowPoint(final MapleMonster monster, final int point) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_SHOW_POINT.getValue());
        mplew.writeInt(monster.getObjectId());
        mplew.writeInt(point);
        return mplew.getPacket();
    }
    
    public static byte[] Respawn(final int cid, final int type) {
        final MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.RESPAWN.getValue());
        packet.writeInt(cid);
        packet.writeInt(type);
        return packet.getPacket();
    }
    
    public static byte[] Death(final int type) {
        final MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.BATTLE_GROUND_DEATH.getValue());
        packet.writeInt(type);
        return packet.getPacket();
    }
    
    public static byte[] DeathEffect(final MapleBattleGroundCharacter chr) {
        final MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
        packet.writeShort(SendPacketOpcode.BATTLE_GROUND_DEATH_EFFECT.getValue());
        packet.writeInt(chr.getId());
        return packet.getPacket();
    }
    
    public static byte[] TakeDamageEffect(final MapleBattleGroundCharacter chr, final int stack) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_EFFECT.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeZeroBytes(54);
        mplew.writeInt(1);
        mplew.writeZeroBytes(66);
        mplew.writeInt(stack);
        return mplew.getPacket();
    }
    
    public static byte[] SelectAvater() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_SELECT_AVATER.getValue());
        mplew.writeInt(13);
        for (int i = 1; i <= 13; ++i) {
            mplew.writeInt((i == 13) ? 0 : i);
            mplew.writeInt((i == 13) ? 0 : ((i == 8 || i == 12) ? 2 : 1));
        }
        return mplew.getPacket();
    }
    
    public static byte[] SelectAvaterOther(final MapleCharacter chr, final int type, final int unk) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_SELECT_AVATER_OTHER.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(type);
        mplew.writeInt(unk);
        return mplew.getPacket();
    }
    
    public static byte[] SelectAvaterClock(final int time) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.BATTLE_GROUND_SELECT_AVATER_CLOCK.getValue());
        mplew.writeInt(time);
        mplew.write(0);
        return mplew.getPacket();
    }
}
