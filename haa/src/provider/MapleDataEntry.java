package provider;

public class MapleDataEntry implements MapleDataEntity {
  private String name;
  
  private int size;
  
  private int checksum;
  
  private int offset;
  
  private MapleDataEntity parent;
  
  public MapleDataEntry(String name, int size, int checksum, MapleDataEntity parent) {
    this.name = name;
    this.size = size;
    this.checksum = checksum;
    this.parent = parent;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getSize() {
    return this.size;
  }
  
  public int getChecksum() {
    return this.checksum;
  }
  
  public int getOffset() {
    return this.offset;
  }
  
  public MapleDataEntity getParent() {
    return this.parent;
  }
}
