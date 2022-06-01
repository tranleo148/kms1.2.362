package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.PlayerStats;
import client.Skill;
import client.SkillFactory;
import constants.GameConstants;
import java.util.EnumMap;
import server.Randomizer;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;

public class StatsHandling {
    public static final void DistributeAP(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        EnumMap<MapleStat, Long> statupdate = new EnumMap<MapleStat, Long>(MapleStat.class);
        c.getSession().writeAndFlush((Object)CWvsContext.updatePlayerStats(statupdate, true, chr));
        slea.readInt();
        PlayerStats stat = chr.getStat();
        short job = chr.getJob();
        if (chr.getRemainingAp() > 0) {
            switch (slea.readInt()) {
                case 64: {
                    if (stat.getStr() >= 32767) {
                        return;
                    }
                    stat.setStr((short)(stat.getStr() + 1), chr);
                    statupdate.put(MapleStat.STR, Long.valueOf(stat.getStr()));
                    break;
                }
                case 128: {
                    if (stat.getDex() >= 32767) {
                        return;
                    }
                    stat.setDex((short)(stat.getDex() + 1), chr);
                    statupdate.put(MapleStat.DEX, Long.valueOf(stat.getDex()));
                    break;
                }
                case 256: {
                    if (stat.getInt() >= 32767) {
                        return;
                    }
                    stat.setInt((short)(stat.getInt() + 1), chr);
                    statupdate.put(MapleStat.INT, Long.valueOf(stat.getInt()));
                    break;
                }
                case 512: {
                    if (stat.getLuk() >= 32767) {
                        return;
                    }
                    stat.setLuk((short)(stat.getLuk() + 1), chr);
                    statupdate.put(MapleStat.LUK, Long.valueOf(stat.getLuk()));
                    break;
                }
                case 2048: {
                    long maxhp = stat.getMaxHp();
                    if (chr.getHpApUsed() >= 10000 || maxhp >= 500000L) {
                        return;
                    }
                    maxhp = GameConstants.isBeginnerJob(job) ? (maxhp += (long)Randomizer.rand(8, 12)) : (job >= 100 && job <= 132 || job >= 3200 && job <= 3212 || job >= 1100 && job <= 1112 || job >= 3100 && job <= 3112 ? (maxhp += (long)Randomizer.rand(36, 42)) : (job >= 200 && job <= 232 || GameConstants.isEvan(job) ? (maxhp += (long)Randomizer.rand(10, 20)) : (job >= 300 && job <= 322 || job >= 400 && job <= 434 || job >= 1300 && job <= 1312 || job >= 1400 && job <= 1412 || job >= 3300 && job <= 3312 || job >= 2300 && job <= 2312 ? (maxhp += (long)Randomizer.rand(16, 20)) : (job >= 510 && job <= 512 || job >= 1510 && job <= 1512 ? (maxhp += (long)Randomizer.rand(28, 32)) : (job >= 500 && job <= 532 || job >= 3500 && job <= 3512 || job == 1500 ? (maxhp += (long)Randomizer.rand(18, 22)) : (job >= 1200 && job <= 1212 ? (maxhp += (long)Randomizer.rand(15, 21)) : (job >= 2000 && job <= 2112 ? (maxhp += (long)Randomizer.rand(38, 42)) : (maxhp += (long)Randomizer.rand(50, 100)))))))));
                    maxhp = Math.min(500000L, Math.abs(maxhp));
                    chr.setHpApUsed((short)(chr.getHpApUsed() + 1));
                    stat.setMaxHp(maxhp, chr);
                    statupdate.put(MapleStat.MAXHP, maxhp);
                    break;
                }
                case 8192: {
                    long maxmp = stat.getMaxMp();
                    if (chr.getHpApUsed() >= 10000 || stat.getMaxMp() >= 500000L) {
                        return;
                    }
                    if (GameConstants.isBeginnerJob(job)) {
                        maxmp += (long)Randomizer.rand(6, 8);
                    } else {
                        if (job >= 3100 && job <= 3112) {
                            return;
                        }
                        maxmp = job >= 200 && job <= 232 || GameConstants.isEvan(job) || job >= 3200 && job <= 3212 || job >= 1200 && job <= 1212 ? (maxmp += (long)Randomizer.rand(38, 40)) : (job >= 300 && job <= 322 || job >= 400 && job <= 434 || job >= 500 && job <= 532 || job >= 3200 && job <= 3212 || job >= 3500 && job <= 3512 || job >= 1300 && job <= 1312 || job >= 1400 && job <= 1412 || job >= 1500 && job <= 1512 || job >= 2300 && job <= 2312 ? (maxmp += (long)Randomizer.rand(10, 12)) : (job >= 100 && job <= 132 || job >= 1100 && job <= 1112 || job >= 2000 && job <= 2112 ? (maxmp += (long)Randomizer.rand(6, 9)) : (maxmp += (long)Randomizer.rand(50, 100))));
                    }
                    maxmp = Math.min(500000L, Math.abs(maxmp));
                    chr.setHpApUsed((short)(chr.getHpApUsed() + 1));
                    stat.setMaxMp(maxmp, chr);
                    statupdate.put(MapleStat.MAXMP, maxmp);
                    break;
                }
                default: {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
            }
            chr.setRemainingAp((short)(chr.getRemainingAp() - 1));
            statupdate.put(MapleStat.AVAILABLEAP, Long.valueOf(chr.getRemainingAp()));
            c.getSession().writeAndFlush((Object)CWvsContext.updatePlayerStats(statupdate, false, chr));
        }
    }

    public static final void DistributeSP(int skillid, int count, MapleClient c, MapleCharacter chr) {
        int remainingSp;
        boolean isBeginnerSkill = false;
        if (GameConstants.isBeginnerJob(skillid / 10000) && (skillid % 10000 == 1000 || skillid % 10000 == 1001 || skillid % 10000 == 1002 || skillid % 10000 == 2)) {
            boolean resistance = skillid / 10000 == 3000 || skillid / 10000 == 3001;
            int snailsLevel = chr.getSkillLevel(SkillFactory.getSkill(skillid / 10000 * 10000 + 1000));
            int recoveryLevel = chr.getSkillLevel(SkillFactory.getSkill(skillid / 10000 * 10000 + 1001));
            int nimbleFeetLevel = chr.getSkillLevel(SkillFactory.getSkill(skillid / 10000 * 10000 + (resistance ? 2 : 1002)));
            remainingSp = Math.min(chr.getLevel() - 1, resistance ? 9 : 6) - snailsLevel - recoveryLevel - nimbleFeetLevel;
            isBeginnerSkill = true;
        } else {
            if (GameConstants.isBeginnerJob(skillid / 10000)) {
                return;
            }
            remainingSp = chr.getRemainingSp(GameConstants.getSkillBookForSkill(skillid));
        }
        Skill skill = SkillFactory.getSkill(skillid);
        for (Pair<String, Integer> ski : skill.getRequiredSkills()) {
            int left;
            if (!(((String)ski.left).equals("level") ? chr.getLevel() < (Integer)ski.right : chr.getSkillLevel(SkillFactory.getSkill(left = Integer.parseInt((String)ski.left))) < (Integer)ski.right)) continue;
            return;
        }
        int maxlevel = skill.isFourthJob() ? (int)chr.getMasterLevel(skill) : skill.getMaxLevel();
        int curLevel = chr.getSkillLevel(skill);
        if (skill.isInvisible() && chr.getSkillLevel(skill) == 0 && (skill.isFourthJob() && chr.getMasterLevel(skill) == 0 || !skill.isFourthJob() && maxlevel < 10 && !isBeginnerSkill)) {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            return;
        }
        for (int i : GameConstants.blockedSkills) {
            if (skill.getId() != i) continue;
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            chr.dropMessage(1, "This skill has been blocked and may not be added.");
            return;
        }
        if (remainingSp > 0 && curLevel + count <= maxlevel && skill.canBeLearnedBy(chr)) {
            if (!isBeginnerSkill) {
                int skillbook = GameConstants.getSkillBookForSkill(skillid);
                chr.setRemainingSp(chr.getRemainingSp(skillbook) - count, skillbook);
            }
            chr.updateSingleStat(MapleStat.AVAILABLESP, 0L);
            chr.changeSingleSkillLevel(skill, (byte)(curLevel + count), chr.getMasterLevel(skill));
        } else {
            c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
        }
    }

    public static final void AutoAssignAP(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) { // 이쪽은 아닌듯
        int amount2;
        slea.skip(4);
        int count = slea.readInt();
        int PrimaryStat = slea.readInt();
        int amount = slea.readInt();
        int SecondaryStat2 = count == 2 ? slea.readInt() : 0;
        int n = amount2 = count == 2 ? slea.readInt() : 0;
        if (amount < 0 || amount2 < 0) {
            return;
        }
        if (chr.getRemainingAp() < amount + amount2 || chr.getRemainingAp() < amount || chr.getRemainingAp() < amount2) {
            return;
        }
        PlayerStats playerst = chr.getStat();
        EnumMap<MapleStat, Long> statupdate = new EnumMap<MapleStat, Long>(MapleStat.class);
        c.getSession().writeAndFlush((Object)CWvsContext.updatePlayerStats(statupdate, true, chr));
        if (chr.getRemainingAp() == amount + amount2 || GameConstants.isXenon(chr.getJob())) {
            switch (PrimaryStat) {
                case 64: {
                    if (playerst.getStr() + amount > 32767) {
                        return;
                    }
                    playerst.setStr((short)(playerst.getStr() + amount), chr);
                    statupdate.put(MapleStat.STR, Long.valueOf(playerst.getStr()));
                    break;
                }
                case 128: {
                    if (playerst.getDex() + amount > 32767) {
                        return;
                    }
                    playerst.setDex((short)(playerst.getDex() + amount), chr);
                    statupdate.put(MapleStat.DEX, Long.valueOf(playerst.getDex()));
                    break;
                }
                case 256: {
                    if (playerst.getInt() + amount > 32767) {
                        return;
                    }
                    playerst.setInt((short)(playerst.getInt() + amount), chr);
                    statupdate.put(MapleStat.INT, Long.valueOf(playerst.getInt()));
                    break;
                }
                case 512: {
                    if (playerst.getLuk() + amount > 32767) {
                        return;
                    }
                    playerst.setLuk((short)(playerst.getLuk() + amount), chr);
                    statupdate.put(MapleStat.LUK, Long.valueOf(playerst.getLuk()));
                    break;
                }
                case 2048: {
                    if (playerst.getMaxHp() + (long)(amount * 30) > 500000L) {
                        return;
                    }
                    playerst.setMaxHp(playerst.getMaxHp() + (long)(amount * 30), chr);
                    statupdate.put(MapleStat.MAXHP, playerst.getMaxHp());
                    break;
                }
                default: {
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    return;
                }
            }
            switch (SecondaryStat2) {
                case 64: {
                    if (playerst.getStr() + amount2 > 32767) {
                        return;
                    }
                    playerst.setStr((short)(playerst.getStr() + amount2), chr);
                    statupdate.put(MapleStat.STR, Long.valueOf(playerst.getStr()));
                    break;
                }
                case 128: {
                    if (playerst.getDex() + amount2 > 32767) {
                        return;
                    }
                    playerst.setDex((short)(playerst.getDex() + amount2), chr);
                    statupdate.put(MapleStat.DEX, Long.valueOf(playerst.getDex()));
                    break;
                }
                case 256: {
                    if (playerst.getInt() + amount2 > 32767) {
                        return;
                    }
                    playerst.setInt((short)(playerst.getInt() + amount2), chr);
                    statupdate.put(MapleStat.INT, Long.valueOf(playerst.getInt()));
                    break;
                }
                case 512: {
                    if (playerst.getLuk() + amount2 > 32767) {
                        return;
                    }
                    playerst.setLuk((short)(playerst.getLuk() + amount2), chr);
                    statupdate.put(MapleStat.LUK, Long.valueOf(playerst.getLuk()));
                    break;
                }
                case 2048: {
                    if (playerst.getMaxHp() + (long)(amount * 30) > 500000L) {
                        return;
                    }
                    playerst.setMaxHp(playerst.getMaxHp() + (long)(amount * 30), chr);
                    statupdate.put(MapleStat.MAXHP, playerst.getMaxHp());
                }
            }
            chr.setRemainingAp((short)(chr.getRemainingAp() - (amount + amount2)));
            statupdate.put(MapleStat.AVAILABLEAP, Long.valueOf(chr.getRemainingAp()));
            c.getSession().writeAndFlush((Object)CWvsContext.updatePlayerStats(statupdate, true, chr));
        }
    }
}

