package client.inventory;

import database.DatabaseConnection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MapleInventoryIdentifier implements Serializable {
  private static final long serialVersionUID = 21830921831301L;
  
  private final AtomicLong runningUID = new AtomicLong(0L);
  
  private static MapleInventoryIdentifier instance = new MapleInventoryIdentifier();
  
  private final Lock runningUIDLock = new ReentrantLock();
  
  public static long getInstance() {
    return instance.getNextUniqueId();
  }
  
  public long getNextUniqueId() {
    this.runningUIDLock.lock();
    try {
      if (this.runningUID.get() <= 0L) {
        this.runningUID.set(initUID());
      } else {
        this.runningUID.set(this.runningUID.get() + 1L);
      } 
    } finally {
      this.runningUIDLock.unlock();
    } 
    return this.runningUID.get();
  }
  
  public long initUID() {
    long ret = 0L;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      long[] ids = new long[6];
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT MAX(uniqueid) FROM inventoryitems WHERE uniqueid > 0");
      rs = ps.executeQuery();
      if (rs.next())
        ids[0] = rs.getLong(1) + 1L; 
      rs.close();
      ps.close();
      ps = con.prepareStatement("SELECT MAX(uniqueid) FROM inventoryitemscash WHERE uniqueid > 0");
      rs = ps.executeQuery();
      if (rs.next())
        ids[1] = rs.getLong(1) + 1L; 
      rs.close();
      ps.close();
      ps = con.prepareStatement("SELECT MAX(petid) FROM pets");
      rs = ps.executeQuery();
      if (rs.next())
        ids[2] = rs.getLong(1) + 1L; 
      rs.close();
      ps.close();
      ps = con.prepareStatement("SELECT MAX(ringid) FROM rings");
      rs = ps.executeQuery();
      if (rs.next())
        ids[3] = rs.getLong(1) + 1L; 
      rs.close();
      ps.close();
      ps = con.prepareStatement("SELECT MAX(partnerringid) FROM rings");
      rs = ps.executeQuery();
      if (rs.next())
        ids[4] = rs.getLong(1) + 1L; 
      rs.close();
      ps.close();
      ps = con.prepareStatement("SELECT MAX(uniqueid) FROM androids");
      rs = ps.executeQuery();
      if (rs.next())
        ids[5] = rs.getLong(1) + 1L; 
      rs.close();
      ps.close();
      con.close();
      for (int i = 0; i < ids.length; i++) {
        if (ids[i] > ret)
          ret = ids[i]; 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
    return ret;
  }
}
