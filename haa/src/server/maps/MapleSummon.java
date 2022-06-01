package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import server.SecondaryStatEffect;
import tools.packet.CField;

public class MapleSummon extends AnimatedMapleMapObject {

    private MapleCharacter owner;

    private final int skillLevel;

    private MapleMap map;

    private byte rltype;

    private int hp;

    private int duration;

    private int skill;

    private boolean changedMap = false;

    private boolean controlCrystal = false;

    private boolean noapply = false;

    private boolean SpecialSkill = false;

    private SummonMovementType movementType;

    private int lastSummonTickCount;

    private int energy = 0;

    public int mechAddAttackCount = 0;

    public int debuffshell;

    private byte Summon_tickResetCount;

    private byte changePositionCount = 0;

    private long Server_ClientSummonTickDiff;

    private long startTime;

    private long lastAttackTime;

    private List<Integer> magicSkills = new ArrayList<>();

    private List<Boolean> crystalSkills = new ArrayList<>();

    public MapleSummon(MapleCharacter owner, SecondaryStatEffect skill, Point pos, SummonMovementType movementType) {
        this(owner, skill.getSourceId(), pos, movementType, (byte) 0, skill.getDuration());
    }

    public MapleSummon(MapleCharacter owner, SecondaryStatEffect skill, Point pos, SummonMovementType movementType, byte rltype) {
        this(owner, skill.getSourceId(), pos, movementType, rltype, skill.getDuration());
    }

    public MapleSummon(MapleCharacter owner, int sourceid, Point pos, SummonMovementType movementType, byte rltype, int duration) {
        this.owner = owner;
        this.skill = sourceid;
        this.map = owner.getMap();
        this.skillLevel = owner.getTotalSkillLevel(GameConstants.getLinkedSkill(this.skill));
        this.rltype = rltype;
        this.movementType = movementType;
        this.duration = duration;
        setPosition(pos);
        if (!isPuppet()) {
            this.lastSummonTickCount = 0;
            this.Summon_tickResetCount = 0;
            this.Server_ClientSummonTickDiff = 0L;
            this.lastAttackTime = 0L;
        }
        if (sourceid == 152101000 || sourceid == 400021073 || sourceid == 164121008) {
            this.controlCrystal = true;
            if (sourceid == 152101000) {
                this.energy = owner.CrystalCharge;
            }
        }
    }

    public final void sendSpawnData(MapleClient client) {
    }

    public final void sendDestroyData(MapleClient client) {
        client.getSession().writeAndFlush(CField.SummonPacket.removeSummon(this, false));
    }

    public final void updateMap(MapleMap map) {
        this.map = map;
    }

    public final int getSkill() {
        return this.skill;
    }

    public final void setSkill(int skillid) {
        this.skill = skillid;
    }

    public final int getHP() {
        return this.hp;
    }

    public final int getSummonRLType() {
        return this.rltype;
    }

    public final void addHP(int delta) {
        this.hp += delta;
    }

    public final SummonMovementType getMovementType() {
        return this.movementType;
    }

    public final void setMovementType(SummonMovementType type) {
        this.movementType = type;
    }

    public final boolean isPuppet() {
        switch (this.skill) {
            case 3111002:
            case 3120012:
            case 3211002:
            case 3220012:
            case 4341006:
            case 13111004:
            case 13111024:
            case 13120007:
            case 33111003:
                return true;
        }
        return isAngel();
    }

    public final boolean isAngel() {
        return GameConstants.isAngel(this.skill);
    }

    public final boolean isMultiAttack() {
        if (this.skill != 35111002 && this.skill != 35121003 && this.skill != 61111002 && this.skill != 61111220 && (isGaviota() || this.skill == 33101008 || this.skill >= 35000000) && this.skill != 35111009 && this.skill != 35111010 && this.skill != 35111001) {
            return false;
        }
        return true;
    }

    public final boolean isGaviota() {
        return (this.skill == 5211002);
    }

    public final boolean isBeholder() {
        return (this.skill == 1301013);
    }

    public final int getSkillLevel() {
        return this.skillLevel;
    }

    public final int getSummonType() {
        if (this.isAngel()) {
            return 2;
        }
        if (this.skill != 33111003 && this.skill != 3120012 && this.skill != 3220012 && this.isPuppet()) {
            return 0;
        }
        switch (this.skill) {
            case 3211019:
            case 5221029:
            case 14111024:
            case 14121054:
            case 14121055:
            case 14121056:
            case 33101008:
            case 35111002:
            case 131001025:
            case 151100002:
            case 164121006:
            case 400021092:
            case 400051017: {
                return 0;
            }
            case 36121014:
            case 80001722:
            case 400021032: {
                return 2;
            }
            case 36121002: {
                return 3;
            }
            case 164111007: {
                return 5;
            }
            case 35121009: {
                return 6;
            }
            case 25121133:
            case 32001014:
            case 32100010:
            case 32110017:
            case 32120019:
            case 35121003:
            case 152001003:
            case 152101000:
            case 400001013:
            case 400011077:
            case 400011078:
            case 400021068:
            case 400041052: {
                return 7;
            }
            case 5201013:
            case 5201014:
            case 5210016:
            case 5210017:
            case 5210018: {
                return 10;
            }
            case 5201012:
            case 33001009:
            case 33001011:
            case 33001013:
            case 33001014: {
                return 11;
            }
            case 5211019:
            case 5220023:
            case 5220024:
            case 5220025:
            case 5221022: {
                return 12;
            }
            case 400041038: {
                return 13;
            }
            case 400051009: {
                return 15;
            }
            case 400021047:
            case 400021063: {
                return 16;
            }
            case 5210015:
            case 162101003:
            case 162101006:
            case 162121012:
            case 162121015:
            case 400001040:
            case 400021071:
            case 400021073:
            case 400031051:
            case 400041044:
            case 400051046:
            case 400051068: {
                return 17;
            }
            case 400031047:
            case 400051038:
            case 400051052:
            case 400051053: {
                return 18;
            }
            default: {
                return 1;
            }
        }
    }
    
    public final MapleMapObjectType getType() {
        return MapleMapObjectType.SUMMON;
    }

    public final boolean isChangedMap() {
        return this.changedMap;
    }

    public final void setChangedMap(boolean cm) {
        this.changedMap = cm;
    }

    public final void removeSummon(MapleMap map, boolean changechannel) {
        removeSummon(map, true, changechannel);
    }

    public final void removeSummon(MapleMap map, boolean animation, boolean changechannel) {
        map.broadcastMessage(CField.SummonPacket.removeSummon(this, animation));
        map.removeMapObject(this);
        if (!changechannel) {
            getOwner().removeVisibleMapObject(this);
            getOwner().removeSummon(this);
        }
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public MapleCharacter getOwner() {
        return this.owner;
    }

    public void setOwner(MapleCharacter owner) {
        this.owner = owner;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        this.owner.setSkillCustomInfo(152101000, energy, 0L);
    }

    public List<Integer> getMagicSkills() {
        return this.magicSkills;
    }

    public void setMagicSkills(List<Integer> magicSkills) {
        this.magicSkills = magicSkills;
    }

    public boolean isControlCrystal() {
        return this.controlCrystal;
    }

    public void setControlCrystal(boolean controlCrystal) {
        this.controlCrystal = controlCrystal;
    }

    public List<Boolean> getCrystalSkills() {
        return this.crystalSkills;
    }

    public void setCrystalSkills(List<Boolean> crystalSkills) {
        this.crystalSkills = crystalSkills;
    }

    public boolean isNoapply() {
        return this.noapply;
    }

    public void SetNoapply(boolean noapply) {
        this.noapply = noapply;
    }

    public byte getChangePositionCount() {
        return this.changePositionCount;
    }

    public void setChangePositionCount(byte changePositionCount) {
        this.changePositionCount = changePositionCount;
    }

    public int getDebuffshell() {
        return this.debuffshell;
    }

    public void setDebuffshell(int debuffshell) {
        this.debuffshell = debuffshell;
    }

    public boolean isSpecialSkill() {
        return this.SpecialSkill;
    }

    public void setSpecialSkill(boolean SpecialSkill) {
        this.SpecialSkill = SpecialSkill;
    }

    public long getLastAttackTime() {
        return this.lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
