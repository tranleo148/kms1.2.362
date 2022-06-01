package server.life;

public class OverrideMonsterStats {
  public long hp;
  
  public long exp;
  
  public int mp;
  
  public OverrideMonsterStats() {
    this.hp = 1L;
    this.exp = 0L;
    this.mp = 0;
  }
  
  public OverrideMonsterStats(long hp, int mp, long exp, boolean change) {
    this.hp = hp;
    this.mp = mp;
    this.exp = exp;
  }
  
  public OverrideMonsterStats(long hp, int mp, long exp) {
    this(hp, mp, exp, true);
  }
  
  public long getExp() {
    return this.exp;
  }
  
  public void setOExp(long exp) {
    this.exp = exp;
  }
  
  public long getHp() {
    return this.hp;
  }
  
  public void setOHp(long hp) {
    this.hp = hp;
  }
  
  public int getMp() {
    return this.mp;
  }
  
  public void setOMp(int mp) {
    this.mp = mp;
  }
}
