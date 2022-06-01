package server.movement;

import java.awt.Point;
import tools.data.MaplePacketLittleEndianWriter;

public class TunknownMovement extends AbstractLifeMovement {
  private Point offset;
  
  public TunknownMovement(int type, Point position, int duration, int newstate, byte unk) {
    super(type, position, duration, newstate, (short)0, unk);
  }
  
  public void setOffset(Point wobble) {
    this.offset = wobble;
  }
  
  public void serialize(MaplePacketLittleEndianWriter packet) {
    packet.write(getType());
    packet.writePos(getPosition());
    packet.writePos(this.offset);
    packet.write(getNewstate());
    packet.writeShort(getDuration());
    packet.write(getUnk());
  }
}
