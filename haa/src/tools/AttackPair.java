package tools;

import java.awt.Point;
import java.util.List;

public class AttackPair {
  public int objectId;
  
  public int monsterId;
  
  public Point point;
  
  public Point point2;
  
  public List<Pair<Long, Boolean>> attack;
  
  public AttackPair(int objectId, List<Pair<Long, Boolean>> attack) {
    this.objectId = objectId;
    this.attack = attack;
  }
  
  public AttackPair(int objectId, int monsterId, Point point, Point point2, List<Pair<Long, Boolean>> attack) {
    this.objectId = objectId;
    this.monsterId = monsterId;
    this.point = point;
    this.point2 = point2;
    this.attack = attack;
  }
}
