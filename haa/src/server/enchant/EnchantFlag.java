package server.enchant;

public enum EnchantFlag {
  Watk(1),
  Matk(2),
  Str(4),
  Dex(8),
  Int(16),
  Luk(32),
  Wdef(64),
  Mdef(128),
  Hp(256),
  Mp(512),
  Acc(1024),
  Avoid(2048);
  
  private final int i;
  
  EnchantFlag(int i) {
    this.i = i;
  }
  
  public final int getValue() {
    return this.i;
  }
  
  public final boolean check(int flag) {
    if (flag == 0)
      return false; 
    return ((flag & this.i) == this.i);
  }
}
