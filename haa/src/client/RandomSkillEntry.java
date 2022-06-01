package client;

import java.util.List;
import tools.Pair;

public class RandomSkillEntry {
  private int skillid;
  
  private int prob;
  
  private List<Pair<Integer, Integer>> skillList;
  
  public RandomSkillEntry(int skillid, int prob, List<Pair<Integer, Integer>> skillList) {
    this.skillid = skillid;
    this.prob = prob;
    this.skillList = skillList;
  }
  
  public int getSkillId() {
    return this.skillid;
  }
  
  public int getProb() {
    return this.prob;
  }
  
  public List<Pair<Integer, Integer>> getSkillList() {
    return this.skillList;
  }
}
