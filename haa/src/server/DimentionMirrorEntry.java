package server;

import client.inventory.Item;
import java.util.List;

public class DimentionMirrorEntry {
  private int id;
  
  private int level;
  
  private int type;
  
  private String name;
  
  private String desc;
  
  private String script;
  
  private List<Item> items;
  
  public DimentionMirrorEntry(String name, String desc, int level, int id, int type, String script, List<Item> items) {
    this.id = id;
    this.level = level;
    setName(name);
    this.desc = desc;
    this.type = type;
    setScript(script);
    setItems(items);
  }
  
  public int getId() {
    return this.id;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public int getType() {
    return this.type;
  }
  
  public String getDesc() {
    return this.desc;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public List<Item> getItems() {
    return this.items;
  }
  
  public void setItems(List<Item> items) {
    this.items = items;
  }
  
  public String getScript() {
    return this.script;
  }
  
  public void setScript(String script) {
    this.script = script;
  }
}
