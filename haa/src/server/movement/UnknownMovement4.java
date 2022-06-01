package server.movement;

import java.awt.Point;
import tools.data.MaplePacketLittleEndianWriter;

public class UnknownMovement4 extends AbstractLifeMovement {
  private Point pixelsPerSecond;
  
  private Point offset;
  
  public UnknownMovement4(int type, Point position, int duration, int newstate, short FH, byte unk) {
    super(type, position, duration, newstate, FH, unk);
  }
  
  public void setPixelsPerSecond(Point wobble) {
    this.pixelsPerSecond = wobble;
  }
  
  public void setOffset(Point wobble) {
    this.offset = wobble;
  }
  
  public void serialize(MaplePacketLittleEndianWriter packet) {
    packet.write(getType());
    packet.writePos(getPosition());
    packet.writePos(this.pixelsPerSecond);
    packet.writeShort(getFootHolds());
    packet.writePos(this.offset);
  }
}
