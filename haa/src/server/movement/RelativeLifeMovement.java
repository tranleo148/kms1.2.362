package server.movement;

import java.awt.Point;
import tools.data.MaplePacketLittleEndianWriter;

public class RelativeLifeMovement extends AbstractLifeMovement {
  private int nAttr;
  
  private Point v307;
  
  public RelativeLifeMovement(int type, Point position, int duration, int newstate, byte unk) {
    super(type, position, duration, newstate, (short)0, unk);
  }
  
  public void setAttr(int nAttr) {
    this.nAttr = nAttr;
  }
  
  public void setV307(Point v307) {
    this.v307 = v307;
  }
  
  public void serialize(MaplePacketLittleEndianWriter packet) {
    packet.write(getType());
    packet.writePos(getPosition());
    if (getType() == 21 || getType() == 22)
      packet.writeShort(this.nAttr); 
    if (getType() == 59)
      packet.writePos(this.v307); 
    packet.write(getNewstate());
    packet.writeShort(getDuration());
    packet.write(getUnk());
  }
}
