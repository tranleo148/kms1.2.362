package handling.world.guild;

import tools.packet.CWvsContext;

public enum MapleGuildResponse {
  ALREADY_IN_GUILD(68),
  NOT_IN_CHANNEL(81),
  NOT_IN_GUILD(92);
  
  private int value;
  
  MapleGuildResponse(int val) {
    this.value = val;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public byte[] getPacket() {
    return CWvsContext.GuildPacket.genericGuildMessage((byte)this.value);
  }
}
