// 
// Decompiled by Procyon v0.5.36
// 

package handling.channel.handler;

import java.util.concurrent.ConcurrentHashMap;
import client.SkillFactory;
import client.SkillEntry;
import client.Skill;
import java.util.HashMap;
import java.util.Collection;
import client.VMatrix;
import tools.data.LittleEndianAccessor;
import client.MapleCharacter;
import tools.packet.CWvsContext;
import server.MapleInventoryManipulator;
import server.Randomizer;
import client.MapleClient;
import constants.GameConstants;
import java.util.Iterator;
import provider.MapleDataProvider;
import client.SpecialCoreOption;
import java.util.ArrayList;
import provider.MapleDataTool;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import java.io.File;
import java.util.List;
import client.Core;
import tools.Pair;
import java.util.Map;

public class MatrixHandler
{
    private static Map<Integer, Pair<Core, List<String>>> cores;
    private static List<Pair<Core, List<String>>> passiveCores;
    private static List<Pair<Core, List<String>>> activeCores;
    private static List<Pair<Core, List<String>>> specialCores;
    
    public static void loadCore() {
        final String WZpath = System.getProperty("wz");
        final MapleDataProvider prov = MapleDataProviderFactory.getDataProvider(new File(WZpath + "/Etc.wz"));
        final MapleData nameData = prov.getData("VCore.img");
        try {
            for (final MapleData dat : nameData) {
                if (dat.getName().equals("CoreData")) {
                    for (final MapleData d : dat) {
                        final int coreid = Integer.parseInt(d.getName());
                        final int skillid = MapleDataTool.getInt("connectSkill/0", d, 0);
                        final int skillid2 = MapleDataTool.getInt("connectSkill/1", d, 0);
                        final int skillid3 = MapleDataTool.getInt("connectSkill/2", d, 0);
                        final int maxlevel = MapleDataTool.getInt("maxLevel", d, 0);
                        final List<String> jobs = new ArrayList<String>();
                        if (d.getName().equals(d.getName())) {
                            for (final MapleData j : d) {
                                if (j.getName().equals("job")) {
                                    for (final MapleData jobz : j) {
                                        final String job = MapleDataTool.getString(jobz);
                                        jobs.add(job);
                                    }
                                }
                            }
                        }
                        if (!jobs.contains("none")) {
                            SpecialCoreOption spOption = null;
                            if (d.getChildByPath("spCoreOption") != null) {
                                spOption = new SpecialCoreOption();
                                spOption.setCondType(MapleDataTool.getString("spCoreOption/cond/type", d, null));
                                spOption.setCooltime(MapleDataTool.getInt("spCoreOption/cond/cooltime", d, 0));
                                spOption.setCount(MapleDataTool.getInt("spCoreOption/cond/count", d, 0));
                                spOption.setValidTime(MapleDataTool.getInt("spCoreOption/cond/validTime", d, 0));
                                spOption.setProb(MapleDataTool.getDouble("spCoreOption/cond/prob", d, 0.0));
                                spOption.setEffectType(MapleDataTool.getString("spCoreOption/effect/type", d, null));
                                spOption.setSkillid(MapleDataTool.getInt("spCoreOption/effect/skill_id", d, 0));
                                spOption.setSkilllevel(MapleDataTool.getInt("spCoreOption/effect/skill_level", d, 0));
                                spOption.setHeal_percent(MapleDataTool.getInt("spCoreOption/effect/heal_percent", d, 0));
                                spOption.setReducePercent(MapleDataTool.getInt("spCoreOption/effect/reducePercent", d, 0));
                            }
                            final Core core = new Core(-1L, coreid, 0, 1, 0, 1, maxlevel, skillid, skillid2, skillid3, -1, spOption);
                            final Pair<Core, List<String>> pair = new Pair<Core, List<String>>(core, jobs);
                            MatrixHandler.cores.put(coreid, pair);
                            switch (coreid / 10000000) {
                                case 1: {
                                    MatrixHandler.activeCores.add(new Pair<Core, List<String>>(new Core(-1L, coreid, 0, 1, 0, 1, maxlevel, skillid, 0, 0, -1, spOption), jobs));
                                    continue;
                                }
                                case 2: {
                                    MatrixHandler.passiveCores.add(new Pair<Core, List<String>>(new Core(-1L, coreid, 0, 1, 0, 1, maxlevel, skillid, 0, 0, -1, spOption), jobs));
                                    continue;
                                }
                                case 3: {
                                    MatrixHandler.specialCores.add(new Pair<Core, List<String>>(new Core(-1L, coreid, 0, 1, 0, 1, maxlevel, skillid, 0, 0, -1, spOption), jobs));
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static boolean CheckUseableJobs(final List<String> jobz, final List<String> list) {
        for (final String job : jobz) {
            for (final String jobs : list) {
                if (jobs.equals("none") || job.equals("none")) {
                    return true;
                }
                if (jobs.equals("all") || job.equals("all")) {
                    return true;
                }
                if (jobs.equals("warrior") && GameConstants.isWarrior(Short.valueOf(job))) {
                    return true;
                }
                if (jobs.equals("magician") && GameConstants.isMagician(Short.valueOf(job))) {
                    return true;
                }
                if (jobs.equals("archer") && GameConstants.isArcher(Short.valueOf(job))) {
                    return true;
                }
                if (jobs.equals("rogue") && GameConstants.isThief(Short.valueOf(job))) {
                    return true;
                }
                if (jobs.equals("pirate") && GameConstants.isPirate(Short.valueOf(job))) {
                    return true;
                }
                if (job.equals("warrior") && GameConstants.isWarrior(Short.valueOf(jobs))) {
                    return true;
                }
                if (job.equals("magician") && GameConstants.isMagician(Short.valueOf(jobs))) {
                    return true;
                }
                if (job.equals("archer") && GameConstants.isArcher(Short.valueOf(jobs))) {
                    return true;
                }
                if (job.equals("rogue") && GameConstants.isThief(Short.valueOf(jobs))) {
                    return true;
                }
                if (job.equals("pirate") && GameConstants.isPirate(Short.valueOf(jobs))) {
                    return true;
                }
                if (GameConstants.JobCodeCheck(Short.valueOf(job), Short.valueOf(jobs))) {
                    return true;
                }
                if (GameConstants.JobCodeCheck(Short.valueOf(jobs), Short.valueOf(job))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isNumeric(final String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean checkOwnUseableJobs(final Pair<Core, List<String>> data, final MapleClient c) {
        final int jobcode = c.getPlayer().getJob();
        final List<String> list = data.getRight();
        if (data.getLeft().getCoreId() == 10000024 || data.getLeft().getCoreId() == 10000031) {
            return false;
        }
        for (final String jobs : list) {
            if (isNumeric(jobs) && GameConstants.JobCodeCheck(Short.valueOf(jobs), jobcode)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkUseableJobs(final Pair<Core, List<String>> data, final MapleClient c) {
        final int jobcode = c.getPlayer().getJob();
        final List<String> list = data.getRight();
        if (data.getLeft().getCoreId() == 10000024 || data.getLeft().getCoreId() == 10000031) {
            return false;
        }
        for (final String jobs : list) {
            if (jobs.equals("none")) {
                return true;
            }
            if (jobs.equals("all")) {
                return true;
            }
            if (jobs.equals("warrior") && GameConstants.isWarrior(jobcode)) {
                return true;
            }
            if (jobs.equals("magician") && GameConstants.isMagician(jobcode)) {
                return true;
            }
            if (jobs.equals("archer") && GameConstants.isArcher(jobcode)) {
                return true;
            }
            if (jobs.equals("rogue") && GameConstants.isThief(jobcode)) {
                return true;
            }
            if (jobs.equals("pirate") && GameConstants.isPirate(jobcode)) {
                return true;
            }
            if (isNumeric(jobs) && GameConstants.JobCodeCheck(Short.valueOf(jobs), jobcode)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean ResetCore(final MapleClient c, final Pair<Core, List<String>> origin, final Pair<Core, List<String>> fresh, final boolean checkjob) {
        return origin.getLeft().getCoreId() == fresh.getLeft().getCoreId() || fresh.getLeft().getCoreId() / 10000000 != 2 || (origin.getLeft().getSkill1() == fresh.getLeft().getSkill1() && origin.getLeft().getSkill1() != 0 && fresh.getLeft().getSkill1() != 0) || (origin.getLeft().getSkill2() == fresh.getLeft().getSkill2() && origin.getLeft().getSkill2() != 0 && fresh.getLeft().getSkill2() != 0) || (origin.getLeft().getSkill3() == fresh.getLeft().getSkill3() && origin.getLeft().getSkill3() != 0 && fresh.getLeft().getSkill3() != 0) || (checkjob && !CheckUseableJobs(origin.getRight(), fresh.getRight())) || !checkUseableJobs(fresh, c);
    }
    
    public static boolean ResetCore(final MapleClient c, final Pair<Core, List<String>> origin, final Pair<Core, List<String>> fresh, final Pair<Core, List<String>> fresh2, final boolean checkjob) {
        return origin.getLeft().getCoreId() == fresh.getLeft().getCoreId() || fresh.getLeft().getCoreId() / 10000000 != 2 || (origin.getLeft().getSkill1() == fresh.getLeft().getSkill1() && origin.getLeft().getSkill1() != 0 && fresh.getLeft().getSkill1() != 0) || (origin.getLeft().getSkill2() == fresh.getLeft().getSkill2() && origin.getLeft().getSkill2() != 0 && fresh.getLeft().getSkill2() != 0) || (origin.getLeft().getSkill3() == fresh.getLeft().getSkill3() && origin.getLeft().getSkill3() != 0 && fresh.getLeft().getSkill3() != 0) || (origin.getLeft().getSkill1() == fresh2.getLeft().getSkill1() && origin.getLeft().getSkill1() != 0 && fresh2.getLeft().getSkill1() != 0) || (origin.getLeft().getSkill2() == fresh2.getLeft().getSkill2() && origin.getLeft().getSkill2() != 0 && fresh2.getLeft().getSkill2() != 0) || (origin.getLeft().getSkill3() == fresh2.getLeft().getSkill3() && origin.getLeft().getSkill3() != 0 && fresh2.getLeft().getSkill3() != 0) || (checkjob && !CheckUseableJobs(origin.getRight(), fresh.getRight())) || !checkUseableJobs(fresh, c);
    }
    
    public static void UseMirrorCoreJamStone(final MapleClient c, final int itemid, long crcid) {
        crcid = Randomizer.nextLong();
        if (c.getPlayer().getCore().size() >= 200) {
              c.getPlayer().dropMessage(1, "코어는 최대 200개까지 보유하실 수 있습니다.");
              return;
        }
        if (c.getPlayer().haveItem(itemid)) {
            MapleInventoryManipulator.removeById_Lock(c, GameConstants.getInventoryType(itemid), itemid);
            final Core core = new Core(crcid, 10000024, c.getPlayer().getId(), 1, 0, 1, 25, 400001039, 0, 0, -1, null);
            c.getPlayer().getCore().add(core);
            core.setId(c.getPlayer().getCore().indexOf(core));
            c.getSession().writeAndFlush((Object)CWvsContext.AddCore(core));
        }
        c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer()));
    }
    
    public static void UseCraftCoreJamStone(final MapleClient c, final int itemid, long crcid) {
        crcid = Randomizer.nextLong();
        if (c.getPlayer().getCore().size() >= 200) {
      c.getPlayer().dropMessage(1, "코어는 최대 200개까지 보유하실 수 있습니다.");
      return;
        }
        if (c.getPlayer().haveItem(itemid)) {
            MapleInventoryManipulator.removeById_Lock(c, GameConstants.getInventoryType(itemid), itemid);
            final Core core = new Core(crcid, 10000031, c.getPlayer().getId(), 1, 0, 1, 25, 400001059, 0, 0, -1, null);
            c.getPlayer().getCore().add(core);
            core.setId(c.getPlayer().getCore().indexOf(core));
            c.getSession().writeAndFlush((Object)CWvsContext.AddCore(core));
        }
        c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer()));
    }
    
    public static void UseEnforcedCoreJamStone(final MapleClient c, final int itemid, final long crcid) {
        if (c.getPlayer().getCore().size() >= 200) {
      c.getPlayer().dropMessage(1, "코어는 최대 200개까지 보유하실 수 있습니다.");
      return;
        }
        if (c.getPlayer().haveItem(itemid) && MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemid), itemid, 1, false, false)) {
            final Core core = new Core(crcid, 40000000, c.getPlayer().getId(), 1, 0, 1, 0, 1, 0, 0, -1, null);
            c.getPlayer().getCore().add(core);
            core.setId(c.getPlayer().getCore().indexOf(core));
            c.getSession().writeAndFlush((Object)CWvsContext.AddCore(core));
        }
        c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer()));
    }
    
    public static void UseCoreJamStone(final MapleClient c, final int itemid, long crcid) {
        crcid = Randomizer.nextLong();
        if (c.getPlayer().getCore().size() >= 200) {
        c.getPlayer().dropMessage(1, "코어는 최대 200개까지 보유하실 수 있습니다.");
        return;
        }
        if (c.getPlayer().getLevel() < 200 || GameConstants.isYeti(c.getPlayer().getJob()) || GameConstants.isPinkBean(c.getPlayer().getJob())) {
            return;
        }
        if (c.getPlayer().haveItem(itemid)) {
            MapleInventoryManipulator.removeById_Lock(c, GameConstants.getInventoryType(itemid), itemid);
            final int rand = Randomizer.nextInt(100);
            boolean sp = false;
            Pair<Core, List<String>> skill1;
            Pair<Core, List<String>> skill2;
            Pair<Core, List<String>> skill3;
            if (rand < 5) {
                final int rand2 = Randomizer.nextInt(MatrixHandler.specialCores.size());
                skill1 = MatrixHandler.specialCores.get(rand2);
                skill2 = null;
                skill3 = null;
                while (!checkUseableJobs(skill1, c)) {
                    skill1 = MatrixHandler.specialCores.get(Randomizer.nextInt(MatrixHandler.specialCores.size()));
                }
                sp = true;
            }
            else if (rand < 85) {
                final int rand2 = Randomizer.nextInt(MatrixHandler.passiveCores.size());
                final int rand3 = Randomizer.nextInt(MatrixHandler.passiveCores.size());
                final int rand4 = Randomizer.nextInt(MatrixHandler.passiveCores.size());
                skill1 = MatrixHandler.passiveCores.get(rand2);
                skill2 = MatrixHandler.passiveCores.get(rand3);
                skill3 = MatrixHandler.passiveCores.get(rand4);
                while (!checkUseableJobs(skill1, c)) {
                    skill1 = MatrixHandler.passiveCores.get(Randomizer.nextInt(MatrixHandler.passiveCores.size()));
                }
                while (ResetCore(c, skill1, skill2, true)) {
                    skill2 = MatrixHandler.passiveCores.get(Randomizer.nextInt(MatrixHandler.passiveCores.size()));
                }
                while (ResetCore(c, skill3, skill1, skill2, true)) {
                    skill3 = MatrixHandler.passiveCores.get(Randomizer.nextInt(MatrixHandler.passiveCores.size()));
                }
            }
            else {
                final int rand2 = Randomizer.nextInt(MatrixHandler.activeCores.size());
                skill1 = MatrixHandler.activeCores.get(rand2);
                skill2 = null;
                skill3 = null;
                while (!checkUseableJobs(skill1, c)) {
                    skill1 = MatrixHandler.activeCores.get(Randomizer.nextInt(MatrixHandler.activeCores.size()));
                }
            }
            final Pair<Core, List<String>> tempCore = MatrixHandler.cores.get(skill1.getLeft().getCoreId());
            SpecialCoreOption spCore = null;
            if (tempCore != null) {
                spCore = tempCore.left.getSpCoreOption();
            }
            final Core core = new Core(crcid, skill1.getLeft().getCoreId(), c.getPlayer().getId(), 1, 0, 1, skill1.getLeft().getMaxlevel(), skill1.getLeft().getSkill1(), (skill2 == null) ? 0 : skill2.getLeft().getSkill1(), (skill3 == null) ? 0 : skill3.getLeft().getSkill1(), -1, spCore);
            if (sp) {
                core.setPeriod(System.currentTimeMillis() + 604800000L);
            }
            c.getPlayer().getCore().add(core);
            core.setId(c.getPlayer().getCore().indexOf(core));
            c.getSession().writeAndFlush((Object)CWvsContext.AddCore(core));
        }
        c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer()));
    }
    
    public static void gainVCoreLevel(final MapleCharacter player) {
        for (final Pair<Core, List<String>> coreskill : MatrixHandler.activeCores) {
            if (checkOwnUseableJobs(coreskill, player.getClient())) {
                final Core core = new Core(Randomizer.nextLong(), coreskill.getLeft().getCoreId(), player.getId(), 1, 0, 1, coreskill.getLeft().getMaxlevel(), coreskill.getLeft().getSkill1(), 0, 0, -1, coreskill.getLeft().getSpCoreOption());
                player.getCore().add(core);
                core.setId(player.getCore().indexOf(core));
            }
        }
        player.getClient().getSession().writeAndFlush((Object)CWvsContext.UpdateCore(player));
        player.getClient().getSession().writeAndFlush((Object)CWvsContext.enableActions(player));
    }
    
    public static void updateCore(final LittleEndianAccessor slea, final MapleClient c) {
        final int state = slea.readInt();
        switch (state) {
            case 0: {
                final int coreId = slea.readInt();
                final int prevCoreId = slea.readInt();
                slea.skip(4);
                int position = slea.readInt();
                if (position < 0) {
                    position = 0;
                    for (final VMatrix matrix : c.getPlayer().getMatrixs()) {
                        if (matrix.getId() == -1) {
                            break;
                        }
                        ++position;
                    }
                }
                final Core core = corefromId(c.getPlayer(), coreId);
                VMatrix matrix = VMatrixFromPos(c.getPlayer(), position);
                if (core == null || matrix == null) {
                 c.getPlayer().dropMessage(6, "매트릭스 장착 오류가 발생했습니다.");
                 return;
                }
                if (!matrix.isUnLock() && (position > ((c.getPlayer().getLevel() / 5) - 36))) {
                    c.getPlayer().dropMessage(6, "착용이 불가능합니다.");
                    return;
                }
                if (prevCoreId >= 0) {
                    final Core prevCore = c.getPlayer().getCore().get(prevCoreId);
                    if (prevCore.getPosition() == -1 || prevCore.getState() == 1) {
                          c.getPlayer().dropMessage(6, "코어 장착 도중 오류가 발생했습니다.");
                    }
                    else {
                        prevCore.setState(1);
                        prevCore.setPosition(-1);
                        core.setState(2);
                        core.setPosition(position);
                        matrix.setId(coreId);
                    }
                }
                else if (core.getPosition() >= 0 || core.getState() == 2) {
                    c.getPlayer().dropMessage(6, "이미 착용중인 코어입니다.");
                }
                else {
                    core.setState(2);
                    core.setPosition(position);
                    matrix.setId(coreId);
                }
                c.send(CWvsContext.UpdateCore(c.getPlayer(), 1));
                calcSkillLevel(c.getPlayer(), -1);
                c.getPlayer().setEqpSpCore(c.getPlayer().getEquippedSpecialCore());
                break;
            }
            case 1: {
                final int coreId = slea.readInt();
                slea.skip(4);
                final Core core2 = corefromId(c.getPlayer(), coreId);
                final VMatrix matrix2 = VMatrixFromPos(c.getPlayer(), core2.getPosition());
                if (core2 == null || matrix2 == null) {
                    c.getPlayer().dropMessage(6, "매트릭스 해제 오류가 발생했습니다.");
                    return;
                }
                if (core2.getPosition() == -1 || core2.getState() == 1) {
                   c.getPlayer().dropMessage(6, "미착용중인 코어입니다.");
                }
                else {
                    if (c.getPlayer().getCooldownLimit(core2.getSkill1()) > 0L) {
                   c.getPlayer().dropMessage(6, "재사용 대기시간 중인 코어는 해제할 수 없습니다.");
                   return;
                    }
                    core2.setState(1);
                    core2.setPosition(-1);
                    matrix2.setId(-1);
                }
                c.send(CWvsContext.UpdateCore(c.getPlayer(), -1));
                calcSkillLevel(c.getPlayer(), -1);
                break;
            }
            case 2: {
                final int targetId = slea.readInt();
                final int sourceId = slea.readInt();
                final int targetPosition = slea.readInt();
                final int sourcePosition = slea.readInt();
                final VMatrix targetMatrix = VMatrixFromPos(c.getPlayer(), targetPosition);
                final VMatrix sourceMatrix = VMatrixFromPos(c.getPlayer(), sourcePosition);
                Core targetCore = null;
                Core sourceCore = null;
                targetCore = c.getPlayer().getCore().get(targetId);
                if (sourceId != -1) {
                    sourceCore = c.getPlayer().getCore().get(sourceId);
                }
                if (targetMatrix == null || (sourceMatrix == null && sourceId != -1)) {
                   c.getPlayer().dropMessage(6, "매트릭스 교체 오류가 발생했습니다.");
                   return;
                }
                if (c.getPlayer().getCooldownLimit(targetCore.getSkill1()) > 0L || (sourceCore != null && c.getPlayer().getCooldownLimit(sourceCore.getSkill1()) > 0L)) {
                   c.getPlayer().dropMessage(6, "재사용 대기시간 중인 코어는 해제할 수 없습니다.");
           return;
                }
                targetCore.setPosition(sourcePosition);
                targetMatrix.setId(sourceId);
                if (sourceCore != null) {
                    sourceCore.setPosition(targetPosition);
                    sourceMatrix.setId(targetId);
                }
                calcSkillLevel(c.getPlayer(), -1);
                break;
            }
            case 3: {
                System.out.println("New state " + state + " detected : " + slea);
                break;
            }
            case 4: {
                final int target = slea.readInt();
                final int size = slea.readInt();
                final List<Core> removes = new ArrayList<Core>();
                int exp = 0;
                final Core core3 = corefromId(c.getPlayer(), target);
                if (core3 == null) {
                    c.getPlayer().dropMessage(6, "매트릭스 강화 오류가 발생했습니다.");
                    return;
                }
                final int prevLevel = core3.getLevel();
                for (int i = 0; i < size; ++i) {
                    final int source = slea.readInt();
                    final Core src = c.getPlayer().getCore().get(source);
                    final int gainExp = expByLevel(src);
                    core3.setExp(core3.getExp() + gainExp);
                    exp += gainExp;
                    src.setState(0);
                    src.setExp(0);
                    src.setLevel(0);
                    removes.add(src);
                }
                c.getPlayer().getCore().removeAll(removes);
                while (core3.getExp() >= neededLevelUpExp(core3)) {
                    core3.setExp(core3.getExp() - neededLevelUpExp(core3));
                    core3.setLevel(core3.getLevel() + 1);
                    if (core3.getLevel() >= 25) {
                        core3.setLevel(25);
                        core3.setExp(0);
                        break;
                    }
                }
                calcSkillLevel(c.getPlayer(), 3);
                c.getSession().writeAndFlush((Object)CWvsContext.OnCoreEnforcementResult(target, exp, prevLevel, core3.getLevel()));
                break;
            }
            case 5: {
                final int target = slea.readInt();
                slea.skip(4);
                final Core core2 = corefromId(c.getPlayer(), target);
                if (core2 == null) {
                   c.getPlayer().dropMessage(6, "매트릭스 분해 오류가 발생했습니다.");
                   return;
                }
                final int type = core2.getCoreId() / 10000000;
                int count = 0;
                switch (type) {
                    case 1: {
                        count = 2 * core2.getLevel() * (core2.getLevel() + 19);
                        break;
                    }
                    case 2: {
                        count = (3 * (core2.getLevel() * core2.getLevel()) + 13 * core2.getLevel() + 4) / 2;
                        break;
                    }
                    case 3: {
                        count = 50;
                        break;
                    }
                }
                c.getPlayer().setKeyValue(1477, "count", String.valueOf(c.getPlayer().getKeyValue(1477, "count") + count));
                c.getPlayer().getCore().remove(target);
                calcSkillLevel(c.getPlayer(), 5);
                c.getSession().writeAndFlush((Object)CWvsContext.DeleteCore(count));
                break;
            }
            case 6: {
                final int size2 = slea.readInt();
                int count2 = 0;
                final List<Core> removes = new ArrayList<Core>();
                for (int j = 0; j < size2; ++j) {
                    final int source2 = slea.readInt();
                    final Core core4 = corefromId(c.getPlayer(), source2);
                    if (core4 == null) {
                 c.getPlayer().dropMessage(6, "매트릭스 다중 분해 오류가 발생했습니다.");
        }
                    else {
                        final int type2 = core4.getCoreId() / 10000000;
                        switch (type2) {
                            case 1: {
                                count2 += 2 * core4.getLevel() * (core4.getLevel() + 19);
                                break;
                            }
                            case 2: {
                                count2 += (3 * (core4.getLevel() * core4.getLevel()) + 13 * core4.getLevel() + 4) / 2;
                                break;
                            }
                            case 3: {
                                count2 += 50;
                                break;
                            }
                        }
                        removes.add(core4);
                    }
                }
                c.getPlayer().setKeyValue(1477, "count", String.valueOf(c.getPlayer().getKeyValue(1477, "count") + count2));
                c.getPlayer().getCore().removeAll(removes);
                calcSkillLevel(c.getPlayer(), 5);
                c.getSession().writeAndFlush((Object)CWvsContext.DeleteCore(count2));
                break;
            }
            case 7: {
                final int coreId = slea.readInt();
                final int nCount = slea.readInt();
                List<Pair<Core, List<String>>> cores = new ArrayList<Pair<Core, List<String>>>();
                int lostpont = 0;
                switch (coreId / 10000000) {
                    case 1: {
                        cores = MatrixHandler.activeCores;
                        lostpont = 140;
                        break;
                    }
                    case 2: {
                        cores = MatrixHandler.passiveCores;
                        lostpont = 70;
                        break;
                    }
                    case 3: {
                        cores = MatrixHandler.specialCores;
                        lostpont = 250;
                        break;
                    }
                }
                for (final Pair<Core, List<String>> skill1 : cores) {
                    if (skill1.getLeft().getCoreId() == coreId) {
                        if (nCount == 1) {
                            Pair<Core, List<String>> skill2 = cores.get(Randomizer.nextInt(cores.size()));
                            Pair<Core, List<String>> skill3 = cores.get(Randomizer.nextInt(cores.size()));
                            if (skill1.getLeft().getCoreId() / 10000000 == 2) {
                                while (ResetCore(c, skill1, skill2, true)) {
                                    skill2 = cores.get(Randomizer.nextInt(cores.size()));
                                }
                                while (ResetCore(c, skill3, skill1, skill2, true)) {
                                    skill3 = cores.get(Randomizer.nextInt(cores.size()));
                                }
                            }
                            else {
                                skill2 = null;
                                skill3 = null;
                            }
                            final Core core5 = new Core(Randomizer.nextLong(), skill1.getLeft().getCoreId(), c.getPlayer().getId(), 1, 0, 1, skill1.getLeft().getMaxlevel(), skill1.getLeft().getSkill1(), (skill2 == null) ? 0 : skill2.getLeft().getSkill1(), (skill3 == null) ? 0 : skill3.getLeft().getSkill1(), -1, skill1.getLeft().getSpCoreOption());
                            c.getPlayer().getCore().add(core5);
                            core5.setId(c.getPlayer().getCore().indexOf(core5));
                            c.getSession().writeAndFlush((Object)CWvsContext.ViewNewCore(core5, nCount));
                            c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer(), core5.getId()));
                            c.getPlayer().setKeyValue(1477, "count", String.valueOf(c.getPlayer().getKeyValue(1477, "count") - lostpont));
                        }
                        else {
                            for (int i = 0; i < nCount; ++i) {
                                Pair<Core, List<String>> skill4 = cores.get(Randomizer.nextInt(cores.size()));
                                Pair<Core, List<String>> skill5 = cores.get(Randomizer.nextInt(cores.size()));
                                if (skill1.getLeft().getCoreId() / 10000000 == 2) {
                                    while (ResetCore(c, skill1, skill4, true)) {
                                        skill4 = cores.get(Randomizer.nextInt(cores.size()));
                                    }
                                    while (ResetCore(c, skill1, skill5, true)) {
                                        skill5 = cores.get(Randomizer.nextInt(cores.size()));
                                    }
                                    while (ResetCore(c, skill4, skill5, true)) {
                                        skill5 = cores.get(Randomizer.nextInt(cores.size()));
                                    }
                                }
                                else {
                                    skill4 = null;
                                    skill5 = null;
                                }
                                final Core core6 = new Core(Randomizer.nextLong(), skill1.getLeft().getCoreId(), c.getPlayer().getId(), 1, 0, 1, skill1.getLeft().getMaxlevel(), skill1.getLeft().getSkill1(), (skill4 == null) ? 0 : skill4.getLeft().getSkill1(), (skill5 == null) ? 0 : skill5.getLeft().getSkill1(), -1, skill1.getLeft().getSpCoreOption());
                                c.getPlayer().getCore().add(core6);
                                core6.setId(c.getPlayer().getCore().indexOf(core6));
                                c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer(), core6.getId()));
                            }
                            c.getPlayer().setKeyValue(1477, "count", String.valueOf(c.getPlayer().getKeyValue(1477, "count") - nCount * lostpont));
                            c.getSession().writeAndFlush((Object)CWvsContext.ViewNewCore(skill1.getLeft(), nCount));
                        }
                    }
                }
                break;
            }
            case 9: {
                final int position2 = slea.readInt();
                slea.skip(4);
                final VMatrix matrix3 = VMatrixFromPos(c.getPlayer(), position2);
                if (matrix3 == null) {
            c.getPlayer().dropMessage(6, "매트릭스 강화 오류가 발생했습니다.");
              return;
                }
                matrix3.setLevel(Math.min(5, c.getPlayer().getMatrixs().get(position2).getLevel() + 1));
                calcSkillLevel(c.getPlayer(), -1);
                break;
            }
            case 10: {
                final int position2 = slea.readInt();
                slea.skip(4);
                for (final VMatrix matrix2 : c.getPlayer().getMatrixs()) {
                    if (matrix2.getPosition() == position2) {
                        matrix2.setUnLock(true);
                        break;
                    }
                }
                c.getPlayer().gainMeso(-GameConstants.MatrixSlotAddMeso(c.getPlayer().getLevel(), position2), false, true);
                c.send(CWvsContext.UpdateCore(c.getPlayer()));
                break;
            }
            case 11: {
                for (final VMatrix matrix3 : c.getPlayer().getMatrixs()) {
                    matrix3.setLevel(0);
                }
                calcSkillLevel(c.getPlayer(), -1);
                break;
            }
            case 13: {
                final int pos = slea.readInt();
                int k = 0;
                for (final Core core : c.getPlayer().getCore()) {
                    if (k == pos) {
                        core.setLock(true);
                    }
                    ++k;
                }
                c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer()));
                break;
            }
            case 14: {//코어잠금
                final int pos = slea.readInt();
                final String secondPassword = slea.readMapleAsciiString();
                if (c.CheckSecondPassword(secondPassword)) {
                    int l = 0;
                    for (final Core core3 : c.getPlayer().getCore()) {
                        if (l == pos) {
                            core3.setLock(false);
                        }
                        ++l;
                    }
                    c.getSession().writeAndFlush((Object)CWvsContext.UpdateCore(c.getPlayer()));
                    break;
                }
                break;
            }
            default: {
                System.out.println("New state " + state + " detected : " + slea);
                break;
            }
        }
        c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
    }
    
    private static int neededLevelUpExp(final Core core) {
        final int type = core.getCoreId() / 10000000;
        if (type == 1) {
            return 5 * core.getLevel() + 50;
        }
        return 15 * core.getLevel() + 40;
    }
    
    private static int expByLevel(final Core core) {
        if (core.getCoreId() / 10000000 == 4) {
            return 150;
        }
        int a = core.getExp();
        for (int i = 0; i < core.getLevel(); ++i) {
            a += 50 + i * 5;
        }
        return a;
    }
    
    
    public static void gainMatrix(MapleCharacter chr) {
        List<VMatrix> matrixs = chr.getMatrixs();
        while (matrixs.size() < 26) {
            matrixs.add(new VMatrix(-1, matrixs.size(), 0, false));
        }
        chr.setMatrixs(matrixs);
        chr.getClient().getSession().writeAndFlush(CWvsContext.UpdateCore(chr));
    }
    
    public static VMatrix VMatrixFromPos(final MapleCharacter player, final int pos) {
        for (final VMatrix ma : player.getMatrixs()) {
            if (ma.getPosition() == pos) {
                return ma;
            }
        }
        return null;
    }
    
    public static Core corefromId(final MapleCharacter player, final int id) {
        for (final Core core : player.getCore()) {
            if (core.getId() == id) {
                return core;
            }
        }
        return null;
    }
    
    public static void calcSkillLevel(final MapleCharacter player, final int position) {
        final Map<Skill, SkillEntry> updateSkills = new HashMap<Skill, SkillEntry>();
        for (final Map.Entry<Skill, SkillEntry> skill : player.getSkills().entrySet()) {
            if (skill.getKey().isVMatrix()) {
                updateSkills.put(skill.getKey(), new SkillEntry(0, (byte)0, -1L));
            }
        }
        for (final VMatrix matrix : player.getMatrixs()) {
            matrix.setId(-1);
        }
        player.changeSkillsLevel(updateSkills);
        updateSkills.clear();
        final Map<Integer, Integer> addSkills = new HashMap<Integer, Integer>();
        for (final Core core : player.getCore()) {
            core.setId(player.getCore().indexOf(core));
            if (core.getState() == 2 && core.getPosition() >= 0) {
                if (core.getPosition() >= 28) {//코어 장착
                    core.setState(1);
                    core.setPosition(-1);
                }
                else {
                    final VMatrix matrix2 = VMatrixFromPos(player, core.getPosition());
                    if (matrix2 == null) {
                        continue;
                    }
                    if (matrix2.getId() != core.getId()) {
                        matrix2.setId(core.getId());
                    }
                    if (core.getSkill1() != 0) {
                        if (addSkills.containsKey(core.getSkill1())) {
                            addSkills.put(core.getSkill1(), addSkills.get(core.getSkill1()) + core.getLevel() + Math.max(0, matrix2.getLevel()));
                        }
                        else {
                            addSkills.put(core.getSkill1(), core.getLevel() + Math.max(0, matrix2.getLevel()));
                        }
                        if (core.getSkill1() == 400051000 && (GameConstants.isStriker(player.getJob()) || GameConstants.isArk(player.getJob()) || GameConstants.isEunWol(player.getJob()) || GameConstants.isAngelicBuster(player.getJob()) || GameConstants.isXenon(player.getJob()))) {
                            addSkills.put(400051001, core.getLevel() + Math.max(0, matrix2.getLevel()));
                        }
                    }
                    if (core.getSkill2() != 0) {
                        if (addSkills.containsKey(core.getSkill2())) {
                            addSkills.put(core.getSkill2(), addSkills.get(core.getSkill2()) + core.getLevel() + Math.max(0, matrix2.getLevel()));
                        }
                        else {
                            addSkills.put(core.getSkill2(), core.getLevel() + Math.max(0, matrix2.getLevel()));
                        }
                    }
                    if (core.getSkill3() == 0) {
                        continue;
                    }
                    if (addSkills.containsKey(core.getSkill3())) {
                        addSkills.put(core.getSkill3(), addSkills.get(core.getSkill3()) + core.getLevel() + Math.max(0, matrix2.getLevel()));
                    }
                    else {
                        addSkills.put(core.getSkill3(), core.getLevel() + Math.max(0, matrix2.getLevel()));
                    }
                }
            }
        }
        for (final Map.Entry<Integer, Integer> addSkill : addSkills.entrySet()) {
            if (SkillFactory.getSkill(addSkill.getKey()) != null) {
                updateSkills.put(SkillFactory.getSkill(addSkill.getKey()), new SkillEntry(addSkill.getValue(), (byte)SkillFactory.getSkill(addSkill.getKey()).getMasterLevel(), -1L));
            }
        }
        player.changeSkillsLevel(updateSkills);
        if (position != -1) {
            player.getClient().getSession().writeAndFlush((Object)CWvsContext.UpdateCore(player, 1, position));
        }
        else {
            player.getClient().getSession().writeAndFlush((Object)CWvsContext.UpdateCore(player));
        }
    }
    
    public static Map<Integer, Pair<Core, List<String>>> getCores() {
        return MatrixHandler.cores;
    }
    
    public static void setCores(final Map<Integer, Pair<Core, List<String>>> cores) {
        MatrixHandler.cores = cores;
    }
    
    static {
        MatrixHandler.cores = new ConcurrentHashMap<Integer, Pair<Core, List<String>>>();
        MatrixHandler.passiveCores = new ArrayList<Pair<Core, List<String>>>();
        MatrixHandler.activeCores = new ArrayList<Pair<Core, List<String>>>();
        MatrixHandler.specialCores = new ArrayList<Pair<Core, List<String>>>();
    }
}
