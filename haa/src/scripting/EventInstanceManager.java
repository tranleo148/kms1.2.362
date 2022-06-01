// 
// Decompiled by Procyon v0.5.36
// 

package scripting;

import client.SkillFactory;
import tools.packet.CWvsContext;
import server.MapleItemInformationProvider;
import server.maps.MapleMapFactory;
import tools.Pair;
import handling.channel.ChannelServer;
import java.util.Arrays;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collections;
import client.MapleQuestStatus;
import client.MapleTrait;
import server.quest.MapleQuest;
import java.util.Collection;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuild;
import handling.world.MaplePartyCharacter;
import server.maps.MapleMap;
import handling.world.MapleParty;
import tools.packet.CField;
import java.util.Iterator;
import tools.packet.SLFCGPacket;
import server.Timer;
import tools.FileoutputUtil;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ScheduledFuture;
import java.util.Properties;
import java.util.Map;
import server.life.MapleMonster;
import client.MapleCharacter;
import java.util.List;

public class EventInstanceManager
{
    private List<MapleCharacter> chars;
    private List<Integer> dced;
    private List<MapleMonster> mobs;
    private Map<Integer, Integer> killCount;
    private EventManager em;
    private int channel;
    private String name;
    private Properties props;
    private long timeStarted;
    private long eventTime;
    private List<Integer> mapIds;
    private List<Boolean> isInstanced;
    private ScheduledFuture<?> eventTimer;
    private final ReentrantReadWriteLock mutex;
    private final Lock rL;
    private final Lock wL;
    private boolean disposed;
    
    public EventInstanceManager(final EventManager em, final String name, final int channel) {
        this.chars = new CopyOnWriteArrayList<MapleCharacter>();
        this.dced = new LinkedList<Integer>();
        this.mobs = new LinkedList<MapleMonster>();
        this.killCount = new HashMap<Integer, Integer>();
        this.props = new Properties();
        this.timeStarted = 0L;
        this.eventTime = 0L;
        this.mapIds = new LinkedList<Integer>();
        this.isInstanced = new LinkedList<Boolean>();
        this.mutex = new ReentrantReadWriteLock();
        this.rL = this.mutex.readLock();
        this.wL = this.mutex.writeLock();
        this.disposed = false;
        this.em = em;
        this.name = name;
        this.channel = channel;
    }
    
    public void registerPlayer(final MapleCharacter chr) {
        if (this.disposed || chr == null) {
            return;
        }
        try {
            this.chars.add(chr);
            chr.setEventInstance(this);
            this.em.getIv().invokeFunction("playerEntry", this, chr);
        }
        catch (NullPointerException ex) {
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex);
            ex.printStackTrace();
        }
        catch (Exception ex2) {
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerEntry:\n" + ex2);
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerEntry:\n" + ex2);
        }
    }
    
    public void changedMap(final MapleCharacter chr, final int mapid) {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("changedMap", this, chr, mapid);
        }
        catch (NullPointerException ex2) {}
        catch (Exception ex) {
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name : " + this.em.getName() + ", Instance name : " + this.name + ", method Name : changedMap:\n" + ex);
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : changedMap:\n" + ex);
        }
    }
    
    public void timeOut(final long delay, final EventInstanceManager eim) {
        if (this.disposed || eim == null) {
            return;
        }
        this.eventTimer = Timer.EventTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (EventInstanceManager.this.disposed || eim == null || EventInstanceManager.this.em == null) {
                    return;
                }
                try {
                    EventInstanceManager.this.em.getIv().invokeFunction("scheduledTimeout", eim);
                }
                catch (Exception ex) {
                    FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + EventInstanceManager.this.em.getName() + ", Instance name : " + EventInstanceManager.this.name + ", method Name : scheduledTimeout:\n" + ex);
                    System.out.println("Event name" + EventInstanceManager.this.em.getName() + ", Instance name : " + EventInstanceManager.this.name + ", method Name : scheduledTimeout:\n" + ex);
                }
            }
        }, delay);
    }
    
    public void stopEventTimer() {
        this.eventTime = 0L;
        this.timeStarted = 0L;
        if (this.eventTimer != null) {
            this.eventTimer.cancel(false);
        }
    }
    
    public void restartEventTimerMillSecond(final int time) {
        try {
            if (this.disposed) {
                return;
            }
            this.timeStarted = System.currentTimeMillis();
            this.eventTime = time;
            if (this.eventTimer != null) {
                this.eventTimer.cancel(false);
            }
            this.eventTimer = null;
            for (final MapleCharacter chr : this.getPlayers()) {
                chr.getClient().getSession().writeAndFlush((Object)SLFCGPacket.milliTimer(time));
            }
            this.timeOut(time, this);
        }
        catch (Exception ex) {
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex);
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : restartEventTimerMillSecond:\n");
            ex.printStackTrace();
        }
    }
    
    public void restartEventTimer(final long time) {
        this.restartEventTimer(time, false);
    }
    
    public void restartEventTimer(final long time, final int type) {
        try {
            if (this.disposed) {
                return;
            }
            this.timeStarted = System.currentTimeMillis();
            this.eventTime = time;
            if (this.eventTimer != null) {
                this.eventTimer.cancel(false);
            }
            this.eventTimer = null;
            final int timesend = (int)time / 1000;
            for (final MapleCharacter chr : this.getPlayers()) {
                if (this.name.startsWith("PVP")) {
                    chr.getClient().getSession().writeAndFlush((Object)CField.getPVPClock(Integer.parseInt(this.getProperty("type")), timesend));
                }
                else if (type == 4 || type == 5) {
                    chr.getClient().getSession().writeAndFlush((Object)CField.getVanVanClock((byte)((type != 4) ? 1 : 0), timesend));
                }
                else {
                    chr.getClient().getSession().writeAndFlush((Object)CField.getClock(timesend));
                }
            }
            this.timeOut(time, this);
        }
        catch (Exception ex) {
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex);
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : restartEventTimer:\n");
            ex.printStackTrace();
        }
    }
    
    public void restartEventTimer(final long time, final boolean punchking) {
        try {
            if (this.disposed) {
                return;
            }
            this.timeStarted = System.currentTimeMillis();
            this.eventTime = time;
            if (this.eventTimer != null) {
                this.eventTimer.cancel(false);
            }
            this.eventTimer = null;
            final int timesend = (int)time / 1000;
            for (final MapleCharacter chr : this.getPlayers()) {
                if (this.name.startsWith("PVP")) {
                    chr.getClient().getSession().writeAndFlush((Object)CField.getPVPClock(Integer.parseInt(this.getProperty("type")), timesend));
                }
                else if (punchking) {
                    chr.getClient().getSession().writeAndFlush((Object)CField.getClockMilliEvent(this.timeStarted + this.eventTime));
                }
                else {
                    chr.getClient().getSession().writeAndFlush((Object)CField.getClock(timesend));
                }
            }
            this.timeOut(time, this);
        }
        catch (Exception ex) {
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex);
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : restartEventTimer:\n");
            ex.printStackTrace();
        }
    }
    
    public void startEventTimer(final long time) {
        this.restartEventTimer(time, false);
    }
    
    public void startEventTimer(final long time, final boolean punchking) {
        this.restartEventTimer(time, punchking);
    }
    
    public boolean isTimerStarted() {
        return this.eventTime > 0L && this.timeStarted > 0L;
    }
    
    public long getTimeLeft() {
        return this.eventTime - (System.currentTimeMillis() - this.timeStarted);
    }
    
    public void registerParty(final MapleParty party, final MapleMap map) {
        if (this.disposed) {
            return;
        }
        for (final MaplePartyCharacter pc : party.getMembers()) {
            this.registerPlayer(map.getCharacterById(pc.getId()));
        }
    }
    
    public void registerGuild(final MapleGuild guild, final MapleMap map) {
        if (this.disposed) {
            return;
        }
        for (final MapleGuildCharacter pc : guild.getMembers()) {
            this.registerPlayer(map.getCharacterById(pc.getId()));
        }
    }
    
    public void unregisterPlayer(final MapleCharacter chr) {
        if (this.disposed) {
            chr.setEventInstance(null);
            return;
        }
        this.unregisterPlayer_NoLock(chr);
    }
    
    private boolean unregisterPlayer_NoLock(final MapleCharacter chr) {
        chr.setEventInstance(null);
        if (this.disposed) {
            return false;
        }
        if (this.chars.contains(chr)) {
            this.chars.remove(chr);
            return true;
        }
        return false;
    }
    
    public final boolean disposeIfPlayerBelow(final byte size, final int towarp) {
        if (this.disposed) {
            return true;
        }
        MapleMap map = null;
        if (towarp > 0) {
            map = this.getMapFactory().getMap(towarp);
        }
        if (this.chars != null && this.chars.size() <= size) {
            final List<MapleCharacter> chrs = new LinkedList<MapleCharacter>(this.chars);
            for (final MapleCharacter chr : chrs) {
                if (chr == null) {
                    continue;
                }
                this.unregisterPlayer_NoLock(chr);
                if (towarp <= 0) {
                    continue;
                }
                chr.changeMap(map, map.getPortal(0));
            }
            this.dispose_NoLock();
            return true;
        }
        return false;
    }
    
    public final void saveBossQuest(final int points) {
        if (this.disposed) {
            return;
        }
        for (final MapleCharacter chr : this.getPlayers()) {
            final MapleQuestStatus record = chr.getQuestNAdd(MapleQuest.getInstance(150001));
            if (record.getCustomData() != null) {
                record.setCustomData(String.valueOf(points + Integer.parseInt(record.getCustomData())));
            }
            else {
                record.setCustomData(String.valueOf(points));
            }
            chr.getTrait(MapleTrait.MapleTraitType.will).addExp(points / 100, chr);
        }
    }
    
    public final void saveNX(final int points) {
        if (this.disposed) {
            return;
        }
        for (final MapleCharacter chr : this.getPlayers()) {
            chr.modifyCSPoints(1, points, true);
        }
    }
    
    public List<MapleCharacter> getPlayers() {
        if (this.disposed) {
            return Collections.emptyList();
        }
        return new ArrayList<MapleCharacter>(this.chars);
    }
    
    public List<Integer> getDisconnected() {
        return this.dced;
    }
    
    public final int getPlayerCount() {
        if (this.disposed) {
            return 0;
        }
        return this.chars.size();
    }
    
    public void registerMonster(final MapleMonster mob) {
        if (this.disposed) {
            return;
        }
        this.mobs.add(mob);
        mob.setEventInstance(this);
    }
    
    public void unregisterMonster(final MapleMonster mob) {
        mob.setEventInstance(null);
        if (this.disposed) {
            return;
        }
        if (this.mobs.contains(mob)) {
            this.mobs.remove(mob);
        }
        if (this.mobs.isEmpty()) {
            try {
                this.em.getIv().invokeFunction("allMonstersDead", this);
            }
            catch (Exception ex) {
                FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : allMonstersDead:\n" + ex);
                System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : allMonstersDead:\n" + ex);
            }
        }
    }
    
    public void playerKilled(final MapleCharacter chr) {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("playerDead", this, chr);
        }
        catch (Exception ex) {
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerDead:\n" + ex);
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerDead:\n" + ex);
        }
    }
    
    public boolean revivePlayer(final MapleCharacter chr) {
        if (this.disposed) {
            return false;
        }
        try {
            final Object b = this.em.getIv().invokeFunction("playerRevive", this, chr);
            if (b instanceof Boolean) {
                return (boolean)b;
            }
        }
        catch (Exception ex) {
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerRevive:\n" + ex);
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerRevive:\n" + ex);
        }
        return true;
    }
    
    public void playerDisconnected(final MapleCharacter chr, final int idz) {
        if (this.disposed) {
            return;
        }
        byte ret;
        try {
            ret = ((Double)this.em.getIv().invokeFunction("playerDisconnected", this, chr)).byteValue();
        }
        catch (Exception e) {
            ret = 0;
        }
        if (this.disposed) {
            return;
        }
        if (chr == null || chr.isAlive()) {
            this.dced.add(idz);
        }
        if (chr != null) {
            this.unregisterPlayer_NoLock(chr);
        }
        if (ret == 0) {
            if (this.getPlayerCount() <= 0) {
                this.dispose_NoLock();
            }
        }
        else if ((ret > 0 && this.getPlayerCount() < ret) || (ret < 0 && (this.isLeader(chr) || this.getPlayerCount() < ret * -1))) {
            final List<MapleCharacter> chrs = new LinkedList<MapleCharacter>(this.chars);
            for (final MapleCharacter player : chrs) {
                if (player.getId() != idz) {
                    this.removePlayer(player);
                }
            }
            this.dispose_NoLock();
        }
    }
    
    public void monsterKilled(final MapleCharacter chr, final MapleMonster mob) {
        if (this.disposed) {
            return;
        }
        try {
            final int inc = (int)this.em.getIv().invokeFunction("monsterValue", this, mob.getId());
            if (this.disposed || chr == null) {
                return;
            }
            Integer kc = this.killCount.get(chr.getId());
            if (kc == null) {
                kc = inc;
            }
            else {
                kc += inc;
            }
            this.killCount.put(chr.getId(), kc);
        }
        catch (ScriptException ex) {
            System.out.println("Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex);
        }
        catch (NoSuchMethodException ex2) {
            System.out.println("Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex2);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex2);
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex3);
        }
    }
    
    public void monsterDamaged(final MapleCharacter chr, final MapleMonster mob, final long damage) {
        final List<Integer> mobs = Collections.unmodifiableList((List<? extends Integer>)Arrays.asList(9700037, 8850011));
        if (this.disposed || !mobs.contains(mob.getId())) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("monsterDamaged", this, chr, mob.getId(), damage);
        }
        catch (ScriptException ex) {
            System.out.println("Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex);
        }
        catch (NoSuchMethodException ex2) {
            System.out.println("Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex2);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex2);
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex3);
        }
    }
    
    public void addPVPScore(final MapleCharacter chr, final int score) {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("addPVPScore", this, chr, score);
        }
        catch (ScriptException ex) {
            System.out.println("Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex);
        }
        catch (NoSuchMethodException ex2) {
            System.out.println("Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex2);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + ((this.em == null) ? "null" : this.em.getName()) + ", Instance name : " + this.name + ", method Name : monsterValue:\n" + ex2);
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex3);
        }
    }
    
    public int getKillCount(final MapleCharacter chr) {
        if (this.disposed) {
            return 0;
        }
        final Integer kc = this.killCount.get(chr.getId());
        if (kc == null) {
            return 0;
        }
        return kc;
    }
    
    public void dispose_NoLock() {
        if (this.disposed || this.em == null) {
            return;
        }
        final String emN = this.em.getName();
        try {
            this.disposed = true;
            for (final MapleCharacter chr : this.chars) {
                chr.setEventInstance(null);
            }
            this.chars.clear();
            this.chars = null;
            if (this.mobs.size() >= 1) {
                for (final MapleMonster mob : this.mobs) {
                    if (mob != null) {
                        mob.setEventInstance(null);
                    }
                }
            }
            this.mobs.clear();
            this.mobs = null;
            this.killCount.clear();
            this.killCount = null;
            this.dced.clear();
            this.dced = null;
            this.timeStarted = 0L;
            this.eventTime = 0L;
            this.props.clear();
            this.props = null;
            for (int i = 0; i < this.mapIds.size(); ++i) {
                if (this.isInstanced.get(i)) {
                    this.getMapFactory().removeInstanceMap(this.mapIds.get(i));
                }
            }
            this.mapIds.clear();
            this.mapIds = null;
            this.isInstanced.clear();
            this.isInstanced = null;
            this.em.disposeInstance(this.name);
        }
        catch (Exception e) {
            System.out.println("Caused by : " + emN + " instance name: " + this.name + " method: dispose:");
            e.printStackTrace();
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", e);
        }
    }
    
    public void dispose() {
        this.dispose_NoLock();
    }
    
    public ChannelServer getChannelServer() {
        return ChannelServer.getInstance(this.channel);
    }
    
    public List<MapleMonster> getMobs() {
        return this.mobs;
    }
    
    public final void giveAchievement(final int type) {
        if (this.disposed) {
            return;
        }
        for (MapleCharacter mapleCharacter : this.getPlayers()) {}
    }
    
    public final void broadcastPlayerMsg(final int type, final String msg) {
        if (this.disposed) {
            return;
        }
        for (final MapleCharacter chr : this.getPlayers()) {
            chr.dropMessage(type, msg);
        }
    }
    
    public final void broadcastEnablePvP(final MapleCharacter chr) {
        if (this.disposed) {
            return;
        }
        for (final MapleCharacter mc : chr.getMap().getCharacters()) {
            mc.enablePvP();
        }
    }
    
    public final List<Pair<Integer, String>> newPair() {
        return new ArrayList<Pair<Integer, String>>();
    }
    
    public void addToPair(final List<Pair<Integer, String>> e, final int e1, final String e2) {
        e.add(new Pair<Integer, String>(e1, e2));
    }
    
    public final List<Pair<Integer, MapleCharacter>> newPair_chr() {
        return new ArrayList<Pair<Integer, MapleCharacter>>();
    }
    
    public void addToPair_chr(final List<Pair<Integer, MapleCharacter>> e, final int e1, final MapleCharacter e2) {
        e.add(new Pair<Integer, MapleCharacter>(e1, e2));
    }
    
    public final void broadcastPacket(final byte[] p) {
        if (this.disposed) {
            return;
        }
        for (final MapleCharacter chr : this.getPlayers()) {
            chr.getClient().getSession().writeAndFlush((Object)p);
        }
    }
    
    public final void broadcastTeamPacket(final byte[] p, final int team) {
        if (this.disposed) {
            return;
        }
        for (final MapleCharacter chr : this.getPlayers()) {
            if (chr.getTeam() == team) {
                chr.getClient().getSession().writeAndFlush((Object)p);
            }
        }
    }
    
    public final MapleMap createInstanceMap(final int mapid) {
        if (this.disposed) {
            return null;
        }
        final int assignedid = EventScriptManager.getNewInstanceMapId();
        this.mapIds.add(assignedid);
        this.isInstanced.add(true);
        return this.getMapFactory().CreateInstanceMap(mapid, true, true, true, assignedid);
    }
    
    public final MapleMap createInstanceMapS(final int mapid) {
        if (this.disposed) {
            return null;
        }
        final int assignedid = EventScriptManager.getNewInstanceMapId();
        this.mapIds.add(assignedid);
        this.isInstanced.add(true);
        return this.getMapFactory().CreateInstanceMap(mapid, false, false, false, assignedid);
    }
    
    public final MapleMap setInstanceMap(final int mapid) {
        if (this.disposed) {
            return this.getMapFactory().getMap(mapid);
        }
        this.mapIds.add(mapid);
        this.isInstanced.add(false);
        return this.getMapFactory().getMap(mapid);
    }
    
    public final MapleMapFactory getMapFactory() {
        return this.getChannelServer().getMapFactory();
    }
    
    public final void sendPunchKing(final MapleMap map, final int type, final int data) {
    }
    
    public final MapleMap getMapInstance(final int args) {
        if (this.disposed) {
            return null;
        }
        try {
            boolean instanced = false;
            int trueMapID = -1;
            if (args >= this.mapIds.size()) {
                trueMapID = args;
            }
            else {
                trueMapID = this.mapIds.get(args);
                instanced = this.isInstanced.get(args);
            }
            MapleMap map = null;
            if (!instanced) {
                map = this.getMapFactory().getMap(trueMapID);
                if (map == null) {
                    return null;
                }
                if (map.getCharactersSize() == 0 && this.em.getProperty("shuffleReactors") != null && this.em.getProperty("shuffleReactors").equals("true")) {
                    map.shuffleReactors();
                }
            }
            else {
                map = this.getMapFactory().getInstanceMap(trueMapID);
                if (map == null) {
                    return null;
                }
                if (map.getCharactersSize() == 0 && this.em.getProperty("shuffleReactors") != null && this.em.getProperty("shuffleReactors").equals("true")) {
                    map.shuffleReactors();
                }
            }
            return map;
        }
        catch (NullPointerException ex) {
            FileoutputUtil.outputFileError("Log_Script_Except.rtf", ex);
            ex.printStackTrace();
            return null;
        }
    }
    
    public final void schedule(final String methodName, final long delay) {
        if (this.disposed) {
            return;
        }
        Timer.EventTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (EventInstanceManager.this.disposed || EventInstanceManager.this == null || EventInstanceManager.this.em == null) {
                    return;
                }
                try {
                    EventInstanceManager.this.em.getIv().invokeFunction(methodName, EventInstanceManager.this);
                }
                catch (NullPointerException ex2) {}
                catch (Exception ex) {
                    System.out.println("Event name" + EventInstanceManager.this.em.getName() + ", Instance name : " + EventInstanceManager.this.name + ", method Name : " + methodName + ":\n" + ex);
                    FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + EventInstanceManager.this.em.getName() + ", Instance name : " + EventInstanceManager.this.name + ", method Name(schedule) : " + methodName + " :\n" + ex);
                }
            }
        }, delay);
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setProperty(final String key, final String value) {
        if (this.disposed) {
            return;
        }
        this.props.setProperty(key, value);
    }
    
    public final Object setProperty(final String key, final String value, final boolean prev) {
        if (this.disposed) {
            return null;
        }
        return this.props.setProperty(key, value);
    }
    
    public final String getProperty(final String key) {
        if (this.disposed) {
            return "";
        }
        return this.props.getProperty(key);
    }
    
    public final Properties getProperties() {
        return this.props;
    }
    
    public final void leftParty(final MapleCharacter chr) {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("leftParty", this, chr);
        }
        catch (Exception ex) {
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : leftParty:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : leftParty:\n" + ex);
        }
    }
    
    public final void disbandParty() {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("disbandParty", this);
        }
        catch (Exception ex) {
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : disbandParty:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : disbandParty:\n" + ex);
        }
    }
    
    public final void finishPQ() {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("clearPQ", this);
        }
        catch (Exception ex) {
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : clearPQ:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : clearPQ:\n" + ex);
        }
    }
    
    public final void removePlayer(final MapleCharacter chr) {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("playerExit", this, chr);
        }
        catch (Exception ex) {
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerExit:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : playerExit:\n" + ex);
        }
    }
    
    public void onMapLoad(final MapleCharacter chr) {
        if (this.disposed) {
            return;
        }
        try {
            this.em.getIv().invokeFunction("onMapLoad", this, chr);
        }
        catch (ScriptException ex) {
            System.out.println("Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : onMapLoad:\n" + ex);
            FileoutputUtil.log("Log_Script_Except.rtf", "Event name" + this.em.getName() + ", Instance name : " + this.name + ", method Name : onMapLoad:\n" + ex);
        }
        catch (NoSuchMethodException ex2) {}
    }
    
    public boolean isLeader(final MapleCharacter chr) {
        return chr != null && chr.getParty() != null && chr.getParty().getLeader().getId() == chr.getId();
    }
    
    public boolean isDisconnected(final MapleCharacter chr) {
        return !this.disposed && this.dced.contains(chr.getId());
    }
    
    public void removeDisconnected(final int id) {
        if (this.disposed) {
            return;
        }
        this.dced.remove(id);
    }
    
    public EventManager getEventManager() {
        return this.em;
    }
    
    public void applyBuff(final MapleCharacter chr, final int id) {
        MapleItemInformationProvider.getInstance().getItemEffect(id).applyTo(chr, true);
        chr.getClient().getSession().writeAndFlush((Object)CWvsContext.InfoPacket.getStatusMsg(id));
    }
    
    public void applySkill(final MapleCharacter chr, final int id) {
        SkillFactory.getSkill(id).getEffect(1).applyTo(chr, false);
    }
}
