package server.life;

import java.awt.Point;
import java.util.List;
import server.movement.LifeMovement;
import server.movement.LifeMovementFragment;

public class MapleHaku {
  private Point pos;
  
  private int stance;
  
  private int hair;
  
  private int face;
  
  public MapleHaku(Point pos, int stance, int hair, int face) {
    setPos(pos);
    setStance(stance);
    setHair(hair);
    setFace(face);
  }
  
  public Point getPos() {
    return this.pos;
  }
  
  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public int getStance() {
    return this.stance;
  }
  
  public void setStance(int stance) {
    this.stance = stance;
  }
  
  public int getHair() {
    return this.hair;
  }
  
  public void setHair(int hair) {
    this.hair = hair;
  }
  
  public int getFace() {
    return this.face;
  }
  
  public void setFace(int face) {
    this.face = face;
  }
  
  public final void updatePosition(List<LifeMovementFragment> movement) {
    for (LifeMovementFragment move : movement) {
      if (move instanceof LifeMovement) {
        if (move instanceof server.movement.AbsoluteLifeMovement)
          setPos(((LifeMovement)move).getPosition()); 
        setStance(((LifeMovement)move).getNewstate());
      } 
    } 
  }
}
