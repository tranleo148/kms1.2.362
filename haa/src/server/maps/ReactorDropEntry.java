package server.maps;

public class ReactorDropEntry {
  public int itemId;
  
  public int chance;
  
  public int questid;
  
  public int Minimum;
  
  public int Maximum;
  
  public int assignedRangeStart;
  
  public int assignedRangeLength;
  
  public ReactorDropEntry(int itemId, int Minimum, int Maximum, int chance, int questid) {
    this.itemId = itemId;
    this.Minimum = Minimum;
    this.Maximum = Maximum;
    this.chance = chance;
    this.questid = questid;
  }
}
