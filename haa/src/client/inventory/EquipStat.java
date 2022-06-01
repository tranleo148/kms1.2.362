package client.inventory;

public enum EquipStat {
  SLOTS(1, 1, 1),
  LEVEL(2, 1, 1),
  STR(4, 2, 1),
  DEX(8, 2, 1),
  INT(16, 2, 1),
  LUK(32, 2, 1),
  MHP(64, 2, 1),
  MMP(128, 2, 1),
  WATK(256, 2, 1),
  MATK(512, 2, 1),
  WDEF(1024, 2, 1),
  MDEF(2048, 2, 1),
  ACC(4096, 2, 1),
  AVOID(8192, 2, 1),
  HANDS(16384, 2, 1),
  SPEED(32768, 2, 1),
  JUMP(65536, 2, 1),
  FLAG(131072, 4, 1),
  INC_SKILL(262144, 1, 1),
  ITEM_LEVEL(524288, 1, 1),
  ITEM_EXP(1048576, 8, 1),
  DURABILITY(2097152, 4, 1),
  VICIOUS_HAMMER(4194304, 4, 1),
  PVP_DAMAGE(8388608, 2, 1),
  DOWNLEVEL(16777216, 1, 1),
  ENHANCT_BUFF(33554432, 2, 1),
  DURABILITY_SPECIAL(67108864, 4, 1),
  REQUIRED_LEVEL(134217728, 1, 1),
  YGGDRASIL_WISDOM(268435456, 1, 1),
  FINAL_STRIKE(536870912, 1, 1),
  IndieBdr(1073741824, 1, 1),
  IGNORE_PDR(-2147483648, 1, 1),
  TOTAL_DAMAGE(1, 1, 2),
  ALL_STAT(2, 1, 2),
  KARMA_COUNT(4, 1, 2),
  REBIRTH_FIRE(8, 8, 2),
  EQUIPMENT_TYPE(16, 4, 2);
  
  private final int value;
  
  private final int datatype;
  
  private final int first;
  
  EquipStat(int value, int datatype, int first) {
    this.value = value;
    this.datatype = datatype;
    this.first = first;
  }
  
  public final int getValue() {
    return this.value;
  }
  
  public final int getDatatype() {
    return this.datatype;
  }
  
  public final int getPosition() {
    return this.first;
  }
  
  public final boolean check(int flag) {
    return ((flag & this.value) != 0);
  }
  
  public enum EnchantBuff {
    UPGRADE_TIER(1),
    NO_DESTROY(2),
    SCROLL_SUCCESS(4);
    
    private final int value;
    
    EnchantBuff(int value) {
      this.value = value;
    }
    
    public final byte getValue() {
      return (byte)this.value;
    }
    
    public final boolean check(int flag) {
      return ((flag & this.value) != 0);
    }
  }
}
