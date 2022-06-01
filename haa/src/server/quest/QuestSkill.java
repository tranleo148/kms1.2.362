package server.quest;

import java.util.ArrayList;
import java.util.List;

class QuestSkill {
  private int skillid;
  
  private int skillLevel;
  
  private int masterLevel;
  
  private List<Short> jobs = new ArrayList<>();
  
  protected QuestSkill(int skillid, int skillLevel, int masterLevel, List<Short> jobs) {
    this.skillid = skillid;
    this.skillLevel = skillLevel;
    this.masterLevel = masterLevel;
    this.jobs = jobs;
  }
  
  public int getSkillId() {
    return this.skillid;
  }
  
  public int getSkillLevel() {
    return this.skillLevel;
  }
  
  public int getMasterLevel() {
    return this.masterLevel;
  }
  
  public List<Short> getJobs() {
    return this.jobs;
  }
}
