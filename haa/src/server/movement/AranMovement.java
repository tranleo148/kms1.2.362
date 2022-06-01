package server.movement;

import java.awt.Point;
import tools.data.MaplePacketLittleEndianWriter;

public class AranMovement extends AbstractLifeMovement {
  public AranMovement(int type, Point position, int duration, int newstate, byte unk) {
    super(type, position, duration, newstate, (short)0, unk);
  }
  
  public void serialize(MaplePacketLittleEndianWriter packet) {
    packet.write(getType());
    packet.write(getNewstate());
    packet.writeShort(getDuration());
    packet.write(getUnk());
  }
}
