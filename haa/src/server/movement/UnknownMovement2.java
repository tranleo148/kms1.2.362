package server.movement;

import java.awt.Point;
import tools.data.MaplePacketLittleEndianWriter;

public class UnknownMovement2 extends AbstractLifeMovement {
  private Point pixelsPerSecond;
  
  public UnknownMovement2(int type, int duration, int newstate, short unk, byte unk2) {
    super(type, new Point(0, 0), duration, newstate, unk, unk2);
  }
  
  public Point getPixelsPerSecond() {
    return this.pixelsPerSecond;
  }
  
  public void setPixelsPerSecond(Point wobble) {
    this.pixelsPerSecond = wobble;
  }
  
  public void serialize(MaplePacketLittleEndianWriter packet) {
    packet.write(getType());
    packet.writeShort(getFootHolds());
    packet.write(getNewstate());
    packet.writeShort(getDuration());
    packet.write(getUnk());
  }
}
