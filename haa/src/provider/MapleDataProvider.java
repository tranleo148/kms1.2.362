package provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MapleDataProvider {
  private File root;
  
  private MapleDataDirectoryEntry rootForNavigation;
  
  public MapleDataProvider(File fileIn) {
    this.root = fileIn;
    this.rootForNavigation = new MapleDataDirectoryEntry(fileIn.getName(), 0, 0, null);
    fillMapleDataEntitys(this.root, this.rootForNavigation);
  }
  
  private void fillMapleDataEntitys(File lroot, MapleDataDirectoryEntry wzdir) {
    for (File file : lroot.listFiles()) {
      String fileName = file.getName();
      if (file.isDirectory() && !fileName.endsWith(".img")) {
        MapleDataDirectoryEntry newDir = new MapleDataDirectoryEntry(fileName, 0, 0, wzdir);
        wzdir.addDirectory(newDir);
        fillMapleDataEntitys(file, newDir);
      } else if (fileName.endsWith(".xml")) {
        wzdir.addFile(new MapleDataFileEntry(fileName.substring(0, fileName.length() - 4), 0, 0, wzdir));
      } 
    } 
  }
  
  public MapleData getData(String path) {
    FileInputStream fis;
    MapleData domMapleData;
    File dataFile = new File(this.root, path + ".xml");
    File imageDataDir = new File(this.root, path);
    try {
      fis = new FileInputStream(dataFile);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Datafile " + path + " does not exist in " + this.root.getAbsolutePath());
    } 
    try {
      domMapleData = new MapleData(fis, imageDataDir.getParentFile());
    } finally {
      try {
        fis.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      } 
    } 
    return domMapleData;
  }
  
  public MapleDataDirectoryEntry getRoot() {
    return this.rootForNavigation;
  }
}
