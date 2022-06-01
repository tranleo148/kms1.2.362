package client;

public enum MapleStat {
  SKIN(1),
  FACE(2),
  HAIR(4),
  LEVEL(16),
  JOB(32),
  STR(64),
  DEX(128),
  INT(256),
  LUK(512),
  HP(1024),
  MAXHP(2048),
  MP(4096),
  MAXMP(8192),
  AVAILABLEAP(16384),
  AVAILABLESP(32768),
  EXP(65536),
  FAME(131072),
  MESO(262144),
  FATIGUE(524288),
  CHARISMA(1048576),
  PET(1572872),
  INSIGHT(2097152),
  WILL(4194304),
  CRAFT(8388608),
  SENSE(16777216),
  CHARM(33554432),
  TRAIT_LIMIT(67108864),
  BATTLE_EXP(1073741824),
  BATTLE_RANK(-2147483648),
  BATTLE_POINTS(268435456),
  ICE_GAGE(536870912),
  VIRTUE(1073741824);
  
  private final int i;
  
  MapleStat(int i) {
    this.i = i;
  }
  
  public int getValue() {
    return this.i;
  }
  
  public static final MapleStat getByValue(int value) {
    for (MapleStat stat : values()) {
      if (stat.i == value)
        return stat; 
    } 
    return null;
  }
  
  public enum Temp {
    STR(1),
    DEX(2),
    INT(4),
    LUK(8),
    WATK(16),
    WDEF(32),
    MATK(64),
    MDEF(128),
    ACC(256),
    AVOID(512),
    SPEED(1024),
    JUMP(2048),
    UNKNOWN(4096);
    
    private final int i;
    
    Temp(int i) {
      this.i = i;
    }
    
    public int getValue() {
      return this.i;
    }
  }
}
