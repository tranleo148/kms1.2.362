package handling.world;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class MapleMessenger implements Serializable {
  private static final long serialVersionUID = 9179541993413738569L;
  
  private MapleMessengerCharacter[] members = new MapleMessengerCharacter[3];
  
  private String[] silentLink = new String[3];
  
  private int id;
  
  public MapleMessenger(int id, MapleMessengerCharacter chrfor) {
    this.id = id;
    addMem(0, chrfor);
  }
  
  public void addMem(int pos, MapleMessengerCharacter chrfor) {
    if (this.members[pos] != null)
      return; 
    this.members[pos] = chrfor;
  }
  
  public boolean containsMembers(MapleMessengerCharacter member) {
    return (getPositionByName(member.getName()) < 4);
  }
  
  public void addMember(MapleMessengerCharacter member) {
    int position = getLowestPosition();
    if (position > -1 && position < 4)
      addMem(position, member); 
  }
  
  public void removeMember(MapleMessengerCharacter member) {
    int position = getPositionByName(member.getName());
    if (position > -1 && position < 4)
      this.members[position] = null; 
  }
  
  public void silentRemoveMember(MapleMessengerCharacter member) {
    int position = getPositionByName(member.getName());
    if (position > -1 && position < 4) {
      this.members[position] = null;
      this.silentLink[position] = member.getName();
    } 
  }
  
  public void silentAddMember(MapleMessengerCharacter member) {
    for (int i = 0; i < this.silentLink.length; i++) {
      if (this.silentLink[i] != null && this.silentLink[i].equalsIgnoreCase(member.getName())) {
        addMem(i, member);
        this.silentLink[i] = null;
        return;
      } 
    } 
  }
  
  public void updateMember(MapleMessengerCharacter member) {
    for (int i = 0; i < this.members.length; i++) {
      MapleMessengerCharacter chr = this.members[i];
      if (chr != null && chr.equals(member)) {
        this.members[i] = null;
        addMem(i, member);
        return;
      } 
    } 
  }
  
  public int getLowestPosition() {
    for (int i = 0; i < this.members.length; i++) {
      if (this.members[i] == null)
        return i; 
    } 
    return 4;
  }
  
  public int getPositionByName(String name) {
    for (int i = 0; i < this.members.length; i++) {
      MapleMessengerCharacter messengerchar = this.members[i];
      if (messengerchar != null && messengerchar.getName().equalsIgnoreCase(name))
        return i; 
    } 
    return 4;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int hashCode() {
    return 31 + this.id;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    MapleMessenger other = (MapleMessenger)obj;
    if (this.id != other.id)
      return false; 
    return true;
  }
  
  public Collection<MapleMessengerCharacter> getMembers() {
    return Arrays.asList(this.members);
  }
  
  public boolean isMonitored() {
    int ch = -1;
    for (MapleMessengerCharacter m : this.members) {
      if (m != null) {
        ch = World.Find.findChannel(m.getName());
        if (ch != -1) {
          MapleCharacter mc = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(m.getName());
          if (mc != null && mc.getClient() != null && mc.getClient().isMonitored())
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  public String getMemberNamesDEBUG() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.members.length; i++) {
      if (this.members[i] != null) {
        sb.append(this.members[i].getName());
        if (i != this.members.length - 1)
          sb.append(','); 
      } 
    } 
    return sb.toString();
  }
}
