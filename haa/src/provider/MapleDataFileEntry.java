package provider;

public class MapleDataFileEntry extends MapleDataEntry {
  private int offset;
  
  public MapleDataFileEntry(String name, int size, int checksum, MapleDataEntity parent) {
    super(name, size, checksum, parent);
  }
  
  public int getOffset() {
    return this.offset;
  }
  
  public void setOffset(int offset) {
    this.offset = offset;
  }
}
