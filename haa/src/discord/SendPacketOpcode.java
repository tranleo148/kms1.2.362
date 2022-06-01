package discord;

public enum SendPacketOpcode {
  NULLPACKET(0),
  RpBasicInfo(1),
  RpPlayerList(2),
  RpGiveGM(3),
  MegaPhone(4),
  SendMessage(5);
  
  private int code = -2;
  
  SendPacketOpcode(int code) {
    this.code = code;
  }
  
  public int getValue() {
    return this.code;
  }
}
