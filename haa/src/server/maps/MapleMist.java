package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import client.Skill;
import client.SkillFactory;
import constants.GameConstants;
import java.awt.Rectangle;
import java.util.concurrent.ScheduledFuture;
import server.SecondaryStatEffect;
import server.life.MapleMonster;
import server.life.MobSkill;
import tools.packet.CField;

public class MapleMist extends MapleMapObject {
  private MapleCharacter owner;
  
  private MapleMonster mob;
  
  private Rectangle mistPosition;
  
  private SecondaryStatEffect source;
  
  private MobSkill skill;
  
  private boolean isMobMist;
  
  private byte rltype;
  
  private int skillDelay;
  
  private int skilllevel;
  
  private int isMistType = 0;
  
  private int ownerId;
  
  private int duration = 0;
  
  private int endtime = 0;
  
  private int damup = 0;
  
  private int customx = 0;
  
  private long startTime = 0L;
  
  private long bufftime = 0L;
  
  private ScheduledFuture<?> schedule = null, poisonSchedule = null;
  
  public MapleMist(Rectangle mistPosition, MapleMonster mob, MobSkill skill, int duration) {
    this.mistPosition = mistPosition;
    setMob(mob);
    this.ownerId = mob.getObjectId();
    this.skill = skill;
    this.skilllevel = skill.getSkillLevel();
    this.duration = duration;
    setStartTime(System.currentTimeMillis());
    this.isMobMist = true;
    this.skillDelay = 0;
    if (skill.getSkillId() == 191)
      this.skillDelay = 6; 
    if (skill.getSkillId() == 131 && skill.getSkillLevel() == 28)
      this.skillDelay = 9; 
  }
  
  public MapleMist(Rectangle mistPosition, MapleCharacter owner, SecondaryStatEffect source, int duration, byte rltype) {
    this.mistPosition = mistPosition;
    this.owner = owner;
    this.ownerId = owner.getId();
    this.source = source;
    this.skillDelay = 8;
    this.isMobMist = false;
    this.skilllevel = (source.getSourceId() == 25111012) ? owner.getSkillLevel(25111005) : owner.getTotalSkillLevel(GameConstants.getLinkedSkill(source.getSourceId()));
    setDuration(duration);
    setRltype(rltype);
    setStartTime(System.currentTimeMillis());
    if (owner.isGM())
      owner.dropMessage(6, "skillid " + this.source.getSourceId() + " mist lv : " + this.skilllevel + " / duration : " + this.duration); 
    switch (source.getSourceId()) {
      case 1076:
      case 11076:
      case 2111003:
      case 2111013:
      case 12111005:
      case 14111006:
        this.isMistType = 1;
        break;
      case 400001017:
        this.isMistType = 0;
        this.skillDelay = 1;
        break;
      case 4221006:
      case 32121006:
      case 400021030:
      case 400021031:
        this.isMistType = 0;
        break;
      case 22161003:
        this.isMistType = 4;
        break;
      case 12121005:
        this.isMistType = 6;
        break;
      case 61121116:
        this.skillDelay = 0;
        break;
      case 152121041:
        this.skillDelay = 2;
        break;
    } 
    if (source.getSourceId() == 400001017);
  }
  
  public MapleMapObjectType getType() {
    return MapleMapObjectType.MIST;
  }
  
  public Skill getSourceSkill() {
    if (this.source == null)
      return null; 
    return SkillFactory.getSkill(this.source.getSourceId());
  }
  
  public void setSchedule(ScheduledFuture<?> s) {
    this.schedule = s;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  public void setPoisonSchedule(ScheduledFuture<?> s) {
    this.poisonSchedule = s;
  }
  
  public ScheduledFuture<?> getPoisonSchedule() {
    return this.poisonSchedule;
  }
  
  public int getEndTime() {
    return this.endtime;
  }
  
  public void setEndTime(int time) {
    this.endtime = time;
  }
  
  public boolean isMobMist() {
    return this.isMobMist;
  }
  
  public int isPoisonMist() {
    return this.isMistType;
  }
  
  public void setSkillLevel(int skilllv) {
    this.skilllevel = skilllv;
  }
  
  public int getSkillDelay() {
    return this.skillDelay;
  }
  
  public int getSkillLevel() {
    return this.skilllevel;
  }
  
  public int getOwnerId() {
    return this.ownerId;
  }
  
  public MobSkill getMobSkill() {
    return this.skill;
  }
  
  public Rectangle getBox() {
    return this.mistPosition;
  }
  
  public void setBox(Rectangle r) {
    this.mistPosition = r;
  }
  
  public SecondaryStatEffect getSource() {
    return this.source;
  }
  
  public byte[] fakeSpawnData(int level) {
    return CField.spawnMist(this);
  }
  
  public void sendSpawnData(MapleClient c) {
    c.getSession().writeAndFlush(CField.spawnMist(this));
  }
  
  public void sendDestroyData(MapleClient c) {
    c.getSession().writeAndFlush(CField.removeMist(this));
  }
  
  public boolean makeChanceResult() {
    return this.source.makeChanceResult();
  }
  
  public MapleCharacter getOwner() {
    return this.owner;
  }
  
  public MapleMonster getMob() {
    return this.mob;
  }
  
  public void setMob(MapleMonster mob) {
    this.mob = mob;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  public void setDelay(int delay) {
    this.skillDelay = delay;
  }
  
  public void setDuration(int duration) {
    this.duration = duration;
  }
  
  public byte getRltype() {
    return this.rltype;
  }
  
  public void setRltype(byte rltype) {
    this.rltype = rltype;
  }
  
  public long getStartTime() {
    return this.startTime;
  }
  
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  public int getDamup() {
    return this.damup;
  }
  
  public void setDamup(int damup) {
    this.damup = damup;
  }
  
  public int getCustomx() {
    return this.customx;
  }
  
  public void setCustomx(int customx) {
    this.customx = customx;
  }
  
  public void setOwner(MapleCharacter owner) {
    this.owner = owner;
  }
  
  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }
  
  public long getBufftime() {
    return this.bufftime;
  }
  
  public void setBufftime(long bufftime) {
    this.bufftime = bufftime;
  }
}
