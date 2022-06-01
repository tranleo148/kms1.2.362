package client;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class MapleKeyLayout implements Serializable {
  private static final long serialVersionUID = 9179541993413738569L;
  
  private boolean changed = false;
  
  private Map<Integer, Pair<Byte, Integer>> keymap;
  
  public MapleKeyLayout() {
    this.keymap = new HashMap<>();
  }
  
  public MapleKeyLayout(Map<Integer, Pair<Byte, Integer>> keys) {
    this.keymap = keys;
  }
  
  public final Map<Integer, Pair<Byte, Integer>> Layout() {
    this.changed = true;
    return this.keymap;
  }
  
  public final void unchanged() {
    this.changed = false;
  }
  
  public final void writeData(MaplePacketLittleEndianWriter mplew) {
    for (int x = 0; x < 89; x++) {
      Pair<Byte, Integer> binding = this.keymap.get(Integer.valueOf(x));
      if (binding != null) {
        mplew.write(((Byte)binding.getLeft()).byteValue());
        mplew.writeInt(((Integer)binding.getRight()).intValue());
      } else {
        mplew.write(0);
        mplew.writeInt(0);
      } 
    }
    mplew.write(1); //new 361
    mplew.write(1); //new 361
  }
  
  public final void saveKeys(Connection con, int charid) throws SQLException {
    if (!this.changed)
      return; 
    PreparedStatement ps = con.prepareStatement("DELETE FROM keymap WHERE characterid = ?");
    ps.setInt(1, charid);
    ps.execute();
    ps.close();
    if (this.keymap.isEmpty())
      return; 
    Iterator<Map.Entry<Integer, Pair<Byte, Integer>>> key = this.keymap.entrySet().iterator();
    ps = con.prepareStatement("INSERT INTO keymap (`characterid`, `key`, `type`, `action`) VALUES (?, ?, ?, ?)");
    ps.setInt(1, charid);
    while (key.hasNext()) {
      Map.Entry<Integer, Pair<Byte, Integer>> keybinding = key.next();
      ps.setInt(2, ((Integer)keybinding.getKey()).intValue());
      ps.setInt(3, ((Byte)((Pair)keybinding.getValue()).getLeft()).byteValue());
      ps.setInt(4, ((Integer)((Pair)keybinding.getValue()).getRight()).intValue());
      ps.execute();
    } 
    ps.close();
  }
}
