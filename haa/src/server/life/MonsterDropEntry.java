package server.life;

public class MonsterDropEntry {
  public int itemId;
  
  public int chance;
  
  public int Minimum;
  
  public int Maximum;
  
  public int questid;
  
  public int privated;
  
  public MonsterDropEntry(int itemId, int chance, int Minimum, int Maximum, int questid) {
    this.itemId = itemId;
    this.chance = chance;
    this.questid = questid;
    this.Minimum = Minimum;
    this.Maximum = Maximum;
    this.privated = 0;
  }
  
  public MonsterDropEntry(int itemId, int chance, int Minimum, int Maximum, int questid, int privated) {
    this.itemId = itemId;
    this.chance = chance;
    this.questid = questid;
    this.Minimum = Minimum;
    this.Maximum = Maximum;
    this.privated = privated;
  }
}
