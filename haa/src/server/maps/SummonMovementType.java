package server.maps;

public enum SummonMovementType {
  STATIONARY(0),
  FOLLOW(1),
  WALK_STATIONARY(2),
  BIRD_FOLLOW(3),
  CIRCLE_FOLLOW(4),
  CIRCLE_STATIONARY(5),
  ZEROWEAPON(6),
  FLAME_SUMMON(7),
  ShadowServant(8),
  SUMMON_JAGUAR(11),
  SUMMON_UNK(12),
  BIRD_FOLLOW2(13),
  ShadowServantExtend(14),
  WALK_FOLLOWER(16),
  FLY(17);
  
  private final int val;
  
  SummonMovementType(int val) {
    this.val = val;
  }
  
  public int getValue() {
    return this.val;
  }
}
