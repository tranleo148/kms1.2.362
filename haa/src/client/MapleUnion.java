package client;

public class MapleUnion {
  private int charid;
  
  private int level;
  
  private int job;
  
  private int unk1;
  
  private int unk2;
  
  private int position;
  
  private int unk3;
  
  private int starforce;
  
  private int priset;
  
  private int priset1;
  
  private int priset2;
  
  private int priset3;
  
  private int priset4;
  
  private int pos;
  
  private int pos1;
  
  private int pos2;
  
  private int pos3;
  
  private int pos4;
  
  private String name;
  
  public MapleUnion(int charid, int level, int job, int unk1, int unk2, int position, int unk3, String name, int starforce, int priset, int priset1, int priset2, int priset3, int priset4, int pos, int pos1, int pos2, int pos3, int pos4) {
    setCharid(charid);
    setLevel(level);
    setJob(job);
    setUnk1(unk1);
    setUnk2(unk2);
    setPosition(position);
    setUnk3(unk3);
    setName(name);
    setStarForce(starforce);
    setPriset(priset);
    setPriset1(priset1);
    setPriset2(priset2);
    setPriset3(priset3);
    setPriset4(priset4);
    setPos(pos);
    setPos1(pos1);
    setPos2(pos2);
    setPos3(pos3);
    setPos4(pos4);
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public void setPosition(int position) {
    this.position = position;
  }
  
  public int getCharid() {
    return this.charid;
  }
  
  public void setCharid(int charid) {
    this.charid = charid;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public int getJob() {
    return this.job;
  }
  
  public void setJob(int job) {
    this.job = job;
  }
  
  public int getUnk1() {
    return this.unk1;
  }
  
  public void setUnk1(int unk1) {
    this.unk1 = unk1;
  }
  
  public int getUnk2() {
    return this.unk2;
  }
  
  public void setUnk2(int unk2) {
    this.unk2 = unk2;
  }
  
  public int getUnk3() {
    return this.unk3;
  }
  
  public void setUnk3(int unk3) {
    this.unk3 = unk3;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setStarForce(int starforce) {
    this.starforce = starforce;
  }
  
  public int getStarForce() {
    return this.starforce;
  }
  
  public int getPriset() {
    return this.priset;
  }
  
  public void setPriset(int priset) {
    this.priset = priset;
  }
  
  public int getPriset1() {
    return this.priset1;
  }
  
  public void setPriset1(int priset1) {
    this.priset1 = priset1;
  }
  
  public int getPriset2() {
    return this.priset2;
  }
  
  public void setPriset2(int priset2) {
    this.priset2 = priset2;
  }
  
  public int getPriset3() {
    return this.priset3;
  }
  
  public void setPriset3(int priset3) {
    this.priset3 = priset3;
  }
  
  public int getPriset4() {
    return this.priset4;
  }
  
  public void setPriset4(int priset4) {
    this.priset4 = priset4;
  }
  
  public int getPos() {
    return this.pos;
  }
  
  public void setPos(int pos) {
    this.pos = pos;
  }
  
  public int getPos1() {
    return this.pos1;
  }
  
  public void setPos1(int pos1) {
    this.pos1 = pos1;
  }
  
  public int getPos2() {
    return this.pos2;
  }
  
  public void setPos2(int pos2) {
    this.pos2 = pos2;
  }
  
  public int getPos3() {
    return this.pos3;
  }
  
  public void setPos3(int pos3) {
    this.pos3 = pos3;
  }
  
  public int getPos4() {
    return this.pos4;
  }
  
  public void setPos4(int pos4) {
    this.pos4 = pos4;
  }
}
