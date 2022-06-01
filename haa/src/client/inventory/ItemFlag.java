package client.inventory;

public enum ItemFlag {
  LOCK(1),
  KARMA_USE(2),
  USE_BAG(4),
  UNTRADEABLE(8),
  KARMA_EQUIP(16),
  CHARM_EQUIPED(32),
  ANDROID_ACTIVATED(64),
  CRAFTED(128),
  PROTECT_SHIELD(256),
  LUCKY_PROTECT_SHIELD(512),
  TRADEABLE_ONETIME_EQUIP(4096),
  SAFETY_SHIELD(8192),
  RECOVERY_SHIELD(16384),
  RETURN_SCROLL(32768);
  
  private final int i;
  
  ItemFlag(int i) {
    this.i = i;
  }
  
  public final int getValue() {
    return this.i;
  }
  
    public final boolean check(int flag) {
        if (flag == 0) {
            return false;
        }
        return (flag & this.i) == this.i;
    }
}
