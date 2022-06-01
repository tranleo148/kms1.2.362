package server.field.skill;

import client.MapleCharacter;
import client.MapleClient;
import client.SecondAtom2;
import java.awt.Point;
import java.util.Arrays;
import java.util.concurrent.ScheduledFuture;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.CField;
import tools.packet.SkillPacket;

public class MapleSecondAtom
extends MapleMapObject {
    private boolean numuse = false;
    private MapleCharacter chr;
    private int sourceId;
    private int dataIndex;
    private int num;
    private long startTime;
    private long lastAttackTime;
    private SecondAtom2 secondAtoms;
    private Point pos;
    private ScheduledFuture<?> schedule = null;

    public MapleSecondAtom(MapleCharacter chr, SecondAtom2 secondAtoms, Point pos) {
        this.chr = chr;
        this.secondAtoms = secondAtoms;
        this.pos = pos;
        this.sourceId = secondAtoms.getSourceId();
        this.dataIndex = secondAtoms.getDataIndex();
        this.startTime = System.currentTimeMillis();
    }

    public MapleCharacter getChr() {
        return this.chr;
    }

    public SecondAtom2 getSecondAtoms() {
        return this.secondAtoms;
    }

    public Point getPos() {
        return this.pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getDataIndex() {
        return this.dataIndex;
    }

    public void setDataIndex(int dataIndex) {
        this.dataIndex = dataIndex;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isNumuse() {
        return this.numuse;
    }

    public void setNumuse(boolean numuse) {
        this.numuse = numuse;
    }

    public long getLastAttackTime() {
        return this.lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.SECOND_ATOM;
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        client.getSession().writeAndFlush((Object)SkillPacket.createSecondAtom(Arrays.asList(this)));
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().writeAndFlush((Object)CField.removeSecondAtom(this.chr.getId(), this.getObjectId()));
    }

    public ScheduledFuture<?> getSchedule() {
        return this.schedule;
    }

    public void setSchedule(ScheduledFuture<?> schedule) {
        this.schedule = schedule;
    }
}

