package server.movement;

import java.awt.Point;
import tools.data.MaplePacketLittleEndianWriter;

public class SunknownMovement extends AbstractLifeMovement {
  private int nAttr;
  
  public SunknownMovement(int type, Point position, int duration, int newstate, byte unk) {
    super(type, position, duration, newstate, (short)0, unk);
  }
  
  public void setAttr(int nAttr) {
    this.nAttr = nAttr;
  }
  
  public void serialize(MaplePacketLittleEndianWriter packet) {
    packet.write(getType());
    packet.writePos(getPosition());
    packet.writeShort(this.nAttr);
    packet.write(getNewstate());
    packet.writeShort(getDuration());
    packet.write(getUnk());
  }
}
