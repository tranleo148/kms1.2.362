package client;

public class Core implements Comparable<Core> {
  private int coreId, id, charId, level, exp, state, maxlevel , skill1, skill2, skill3, position;

  private long crcId,period;

  private boolean lock = false;
  
  private SpecialCoreOption spCoreOption = null;
  
  public Core(long crcid, int coreid, int charid, int level, int exp, int state, int maxlevel, int skill1, int skill2, int skill3, int position, SpecialCoreOption spCoreOption) {
    setCrcId(crcid);
    setCoreId(coreid);
    setCharId(charid);
    setLevel(level);
    setExp(exp);
    setState(state);
    setMaxlevel(maxlevel);
    setSkill1(skill1);
    setSkill2(skill2);
    setSkill3(skill3);
    setPosition(position);
    this.spCoreOption = spCoreOption;
    this.period = 0L;
  }
  
  public int getCoreId() {
    return this.coreId;
  }
  
  public void setCoreId(int coreid) {
    this.coreId = coreid;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getCharId() {
    return this.charId;
  }
  
  public void setCharId(int charid) {
    this.charId = charid;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public int getExp() {
    return this.exp;
  }
  
  public void setExp(int exp) {
    this.exp = exp;
  }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) {
    this.state = state;
  }
  
  public int getSkill1() {
    return this.skill1;
  }
  
  public void setSkill1(int skill1) {
    this.skill1 = skill1;
  }
  
  public int getSkill2() {
    return this.skill2;
  }
  
  public void setSkill2(int skill2) {
    this.skill2 = skill2;
  }
  
  public int getSkill3() {
    return this.skill3;
  }
  
  public void setSkill3(int skill3) {
    this.skill3 = skill3;
  }
  
  public long getCrcId() {
    return this.crcId;
  }
  
  public void setCrcId(long crcid) {
    this.crcId = crcid;
  }
  
  public int getMaxlevel() {
    return this.maxlevel;
  }
  
  public void setMaxlevel(int maxlevel) {
    this.maxlevel = maxlevel;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public void setPosition(int position) {
    this.position = position;
  }
  
  public boolean isLock() {
    return this.lock;
  }
  
  public void setLock(boolean lock) {
    this.lock = lock;
  }
  
  public SpecialCoreOption getSpCoreOption() {
    return this.spCoreOption;
  }
  
  public void setSpCoreOption(SpecialCoreOption spCoreOption) {
    this.spCoreOption = spCoreOption;
  }
  
  public long getPeriod() {
    return this.period;
  }
  
  public void setPeriod(long period) {
    this.period = period;
  }
  
  public int compareTo(Core o) {
    if (this.id < o.getId())
      return -1; 
    if (this.id > o.getId())
      return 1; 
    return 0;
  }
}
