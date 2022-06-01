package client.inventory;

public enum MapleInventoryType {
  UNDEFINED(0),
  EQUIP(1),
  USE(2),
  SETUP(3),
  ETC(4),
  CASH(5),
  CODY(6),
  EQUIPPED(-1);
  
  final byte type;
  
  MapleInventoryType(int type) {
    this.type = (byte)type;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public long getBitfieldEncoding() {
    if (this.type <= 5)
      return (2 << this.type); 
    return 2199023255552L << this.type;
  }
  
  public static MapleInventoryType getByType(byte type) {
    for (MapleInventoryType l : values()) {
      if (l.getType() == type)
        return l; 
    } 
    return null;
  }
}
