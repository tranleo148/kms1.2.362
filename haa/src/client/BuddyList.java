package client;

import database.DatabaseConnection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import tools.packet.CWvsContext;

public class BuddyList implements Serializable {
  private static final long serialVersionUID = 1413738569L;
  
  private final Map<Integer, BuddylistEntry> buddies;
  
  private byte capacity;
  
  public enum BuddyOperation {
    ADDED, DELETED;
  }
  
  public enum BuddyAddResult {
    BUDDYLIST_FULL, ALREADY_ON_LIST, OK;
  }
  
  private boolean changed = false;
  
  private Deque<CharacterNameAndId> pendingRequests = new LinkedList<>();
  
  public BuddyList(byte capacity) {
    this.buddies = new LinkedHashMap<>();
    this.capacity = capacity;
  }
  
  public boolean contains(int accId) {
    return this.buddies.containsKey(Integer.valueOf(accId));
  }
  
  public boolean containsVisible(int accId) {
    BuddylistEntry ble = this.buddies.get(Integer.valueOf(accId));
    if (ble == null)
      return false; 
    return ble.isVisible();
  }
  
  public byte getCapacity() {
    return this.capacity;
  }
  
  public void setCapacity(byte capacity) {
    this.capacity = capacity;
  }
  
  public BuddylistEntry get(int accId) {
    return this.buddies.get(Integer.valueOf(accId));
  }
  
  public BuddylistEntry get(String characterName) {
    String lowerCaseName = characterName.toLowerCase();
    for (BuddylistEntry ble : this.buddies.values()) {
      if (ble.getName().toLowerCase().equals(lowerCaseName))
        return ble; 
    } 
    return null;
  }
  
  public void put(BuddylistEntry entry) {
    this.buddies.put(Integer.valueOf(entry.getAccountId()), entry);
  }
  
  public void remove(int accId) {
    this.buddies.remove(Integer.valueOf(accId));
    this.changed = true;
  }
  
  public Collection<BuddylistEntry> getBuddies() {
    return this.buddies.values();
  }
  
  public boolean isFull() {
    return (this.buddies.size() >= this.capacity);
  }
  
  public int[] getBuddyIds() {
    int[] buddyIds = new int[this.buddies.size()];
    int i = 0;
    for (BuddylistEntry ble : this.buddies.values())
      buddyIds[i++] = ble.getAccountId(); 
    return buddyIds;
  }
  
  public void loadFromTransfer(Map<CharacterNameAndId, Boolean> data) {
    for (Map.Entry<CharacterNameAndId, Boolean> qs : data.entrySet()) {
      CharacterNameAndId buddyid = qs.getKey();
      boolean pair = ((Boolean)qs.getValue()).booleanValue();
      if (!pair) {
        getPendingRequests().push(buddyid);
        continue;
      } 
      put(new BuddylistEntry(buddyid.getName(), buddyid.getRepName(), buddyid.getAccId(), buddyid.getId(), buddyid.getGroupName(), -1, true, buddyid.getLevel(), buddyid.getJob(), buddyid.getMemo()));
    } 
  }
  
  public void loadFromDb(int accId) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT b.buddyaccid, b.pending, c.name as buddyname, c.id as buddyid, c.job as buddyjob, c.level as buddylevel, b.repname, b.groupname, b.memo FROM buddies as b, characters as c WHERE c.accountid = b.buddyaccid AND b.accid = ?");
      ps.setInt(1, accId);
      rs = ps.executeQuery();
      while (rs.next()) {
        int buddyid = rs.getInt("buddyaccid");
        String buddyname = rs.getString("buddyname");
        if (rs.getInt("pending") == 1) {
          getPendingRequests().push(new CharacterNameAndId(rs.getInt("buddyid"), buddyid, buddyname, rs.getString("repname"), rs.getInt("buddylevel"), rs.getInt("buddyjob"), rs.getString("groupname"), rs.getString("memo")));
          continue;
        } 
        put(new BuddylistEntry(buddyname, rs.getString("repname"), buddyid, rs.getInt("buddyid"), rs.getString("groupname"), -1, true, rs.getInt("buddylevel"), rs.getInt("buddyjob"), rs.getString("memo")));
      } 
      rs.close();
      ps.close();
      ps = con.prepareStatement("DELETE FROM buddies WHERE pending = 1 AND accid = ?");
      ps.setInt(1, accId);
      ps.executeUpdate();
      ps.close();
      con.close();
    } catch (Exception exception) {
    
    } finally {
      if (con != null)
        con.close(); 
      if (ps != null)
        ps.close(); 
      if (rs != null)
        rs.close(); 
    } 
  }
  
  public void addBuddyRequest(MapleClient c, int accid, int cidFrom, String nameFrom, String repName, int channelFrom, int levelFrom, int jobFrom, String groupName, String memo) {
    put(new BuddylistEntry(nameFrom, repName, accid, cidFrom, groupName, channelFrom, false, levelFrom, jobFrom, memo));
    if (getPendingRequests().isEmpty()) {
      c.getSession().writeAndFlush(CWvsContext.BuddylistPacket.requestBuddylistAdd(cidFrom, accid, nameFrom, levelFrom, jobFrom, c, groupName, memo));
    } else {
      getPendingRequests().push(new CharacterNameAndId(cidFrom, accid, nameFrom, repName, levelFrom, jobFrom, groupName, memo));
    } 
  }
  
  public void setChanged(boolean v) {
    this.changed = v;
  }
  
  public boolean changed() {
    return this.changed;
  }
  
  public CharacterNameAndId pollPendingRequest() {
    return getPendingRequests().pollLast();
  }
  
  public Deque<CharacterNameAndId> getPendingRequests() {
    return this.pendingRequests;
  }
  
  public void setPendingRequests(Deque<CharacterNameAndId> pendingRequests) {
    this.pendingRequests = pendingRequests;
  }
}
