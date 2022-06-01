package client.inventory;

public enum EquipSpecialStat {
  TOTAL_DAMAGE(1),
  ALL_STAT(2),
  KARMA_COUNT(4),
  REBIRTH_FIRE(8),
  EQUIPMENT_TYPE(16);
  
  private final int value;
  
  EquipSpecialStat(int value) {
    this.value = value;
  }
  
  public final int getValue() {
    return this.value;
  }
}
