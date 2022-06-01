package server.maps;

import java.util.ArrayList;
import java.util.List;
import server.maps.ForceAtom;

public class MapleAtom {//mush
    private boolean byMob;
    private boolean toMob;
    private byte dwUnknownByte;
    private int dwUserOwner;
    private int dwTargetId;
    private int nForceAtomType;
    private int nSkillId;
    private int dwFirstTargetId;
    private int nItemId;
    private int nForcedTargetX;
    private int nForcedTargetY;
    private int nArriveDir;
    private int nArriveRange;
    private int dwSummonObjectId;
    private int dwUnknownPoint;
    private int dwUnknownInteger;
    private int nFoxSpiritSkillId;
    private int searchX;
    private int searchY;
    private int searchX1;
    private int searchY1;
    private int nDuration;
    private List<ForceAtom> forceAtoms = new ArrayList<ForceAtom>();
    private List<Integer> dwTargets = new ArrayList<Integer>();

    public MapleAtom(boolean byMob, int dwTargetId, int type, boolean toMob, int skillId, int nForcedTargetX, int nForcedTargetY) {
        this.byMob = byMob;
        this.dwTargetId = dwTargetId;
        this.nForceAtomType = type;
        this.toMob = toMob;
        this.nSkillId = skillId;
        this.nForcedTargetX = nForcedTargetX;
        this.nForcedTargetY = nForcedTargetY;
    }

    public void addForceAtom(ForceAtom forceAtom) {
        this.forceAtoms.add(forceAtom);
    }

    public List<ForceAtom> getForceAtoms() {
        return this.forceAtoms;
    }

    public void setForceAtoms(List<ForceAtom> forceAtoms) {
        this.forceAtoms = forceAtoms;
    }

    public boolean isByMob() {
        return this.byMob;
    }

    public void setByMob(boolean byMob) {
        this.byMob = byMob;
    }

    public int getDwUserOwner() {
        return this.dwUserOwner;
    }

    public void setDwUserOwner(int dwUserOwner) {
        this.dwUserOwner = dwUserOwner;
    }

    public int getDwTargetId() {
        return this.dwTargetId;
    }

    public void setDwTargetId(int dwTargetId) {
        this.dwTargetId = dwTargetId;
    }

    public int getnForceAtomType() {
        return this.nForceAtomType;
    }

    public void setnForceAtomType(int nForceAtomType) {
        this.nForceAtomType = nForceAtomType;
    }

    public boolean isToMob() {
        return this.toMob;
    }

    public void setToMob(boolean toMob) {
        this.toMob = toMob;
    }

    public List<Integer> getDwTargets() {
        return this.dwTargets;
    }

    public void setDwTargets(List<Integer> dwTargets) {
        this.dwTargets = dwTargets;
    }

    public int getnSkillId() {
        return this.nSkillId;
    }

    public void setnSkillId(int nSkillId) {
        this.nSkillId = nSkillId;
    }

    public int getDwFirstTargetId() {
        return this.dwFirstTargetId;
    }

    public void setDwFirstTargetId(int dwFirstTargetId) {
        this.dwFirstTargetId = dwFirstTargetId;
    }

    public int getnItemId() {
        return this.nItemId;
    }

    public void setnItemId(int nItemId) {
        this.nItemId = nItemId;
    }

    public int getnForcedTargetX() {
        return this.nForcedTargetX;
    }

    public void setnForcedTargetX(int nForcedTargetX) {
        this.nForcedTargetX = nForcedTargetX;
    }

    public int getnForcedTargetY() {
        return this.nForcedTargetY;
    }

    public void setnForcedTargetY(int nForcedTargetY) {
        this.nForcedTargetY = nForcedTargetY;
    }

    public int getnArriveDir() {
        return this.nArriveDir;
    }

    public void setnArriveDir(int nArriveDir) {
        this.nArriveDir = nArriveDir;
    }

    public int getnArriveRange() {
        return this.nArriveRange;
    }

    public void setnArriveRange(int nArriveRange) {
        this.nArriveRange = nArriveRange;
    }

    public int getDwSummonObjectId() {
        return this.dwSummonObjectId;
    }

    public void setDwSummonObjectId(int dwSummonObjectId) {
        this.dwSummonObjectId = dwSummonObjectId;
    }

    public int getDwUnknownPoint() {
        return this.dwUnknownPoint;
    }

    public void setDwUnknownPoint(int dwUnknownPoint) {
        this.dwUnknownPoint = dwUnknownPoint;
    }

    public int getDwUnknownInteger() {
        return this.dwUnknownInteger;
    }

    public void setDwUnknownInteger(int dwUnknownInteger) {
        this.dwUnknownInteger = dwUnknownInteger;
    }

    public byte getDwUnknownByte() {
        return this.dwUnknownByte;
    }

    public void setDwUnknownByte(byte dwUnknownByte) {
        this.dwUnknownByte = dwUnknownByte;
    }

    public int getnFoxSpiritSkillId() {
        return this.nFoxSpiritSkillId;
    }

    public void setnFoxSpiritSkillId(int nFoxSpiritSkillId) {
        this.nFoxSpiritSkillId = nFoxSpiritSkillId;
    }

    public int getSearchX() {
        return this.searchX;
    }

    public void setSearchX(int searchX) {
        this.searchX = searchX;
    }

    public int getSearchY() {
        return this.searchY;
    }

    public void setSearchY(int searchY) {
        this.searchY = searchY;
    }

    public int getSearchX1() {
        return this.searchX1;
    }

    public void setSearchX1(int searchX1) {
        this.searchX1 = searchX1;
    }

    public int getSearchY1() {
        return this.searchY1;
    }

    public void setSearchY1(int searchY1) {
        this.searchY1 = searchY1;
    }

    public int getnDuration() {
        return this.nDuration;
    }

    public void setnDuration(int nDuration) {
        this.nDuration = nDuration;
    }
}

