package client;

import constants.GameConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnionList {
  private List<MapleUnion> unions = new ArrayList<>();
  
  public void loadFromDb(int accId) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT u.unk1, u.unk3, sum(ive.enhance) as starforce, c.name as unionname, c.id as id, c.job as unionjob, c.level as unionlevel, u.position, u.position2, u.priset, u.priset1, u.priset2, u.priset3, u.priset4, u.pos, u.pos1, u.pos2, u.pos3, u.pos4 FROM unions as u, characters as c, inventoryitems as iv, inventoryequipment as ive WHERE c.id = u.id && c.accountid = ? && characterid = c.id && iv.position < 0 && ive.inventoryitemid = iv.inventoryitemid group by unionname");
      ps.setInt(1, accId);
      rs = ps.executeQuery();
      while (rs.next()) {
        if (GameConstants.isZero(rs.getInt("unionjob")) ? (
          rs.getInt("unionlevel") < 130) : (

          
          rs.getInt("unionlevel") < 60))
          continue; 
        this.unions.add(new MapleUnion(rs.getInt("id"), rs.getInt("unionlevel"), rs.getInt("unionjob"), rs.getInt("unk1"), rs.getInt("position2"), rs.getInt("position"), rs.getInt("unk3"), rs.getString("unionname"), rs.getInt("starforce"), rs.getInt("priset"), rs.getInt("priset1"), rs.getInt("priset2"), rs.getInt("priset3"), rs.getInt("priset4"), rs.getInt("pos"), rs.getInt("pos1"), rs.getInt("pos2"), rs.getInt("pos3"), rs.getInt("pos4")));
      } 
      rs.close();
      ps.close();
      con.close();
    } catch (Exception exception) {
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
  }
  
  public void loadFromTransfer(List<MapleUnion> unions) {
    this.unions.addAll(unions);
  }
  
  public void savetoDB(Connection con, int accId) throws SQLException {
    PreparedStatement ps = null;
    try {
      for (MapleUnion union : this.unions) {
        ps = con.prepareStatement("DELETE FROM unions WHERE id = ?");
        ps.setInt(1, union.getCharid());
        ps.executeUpdate();
        ps.close();
        ps = con.prepareStatement("INSERT INTO unions (id, unk1, unk3, position, position2, priset, priset1, priset2, priset3, priset4, pos, pos1, pos2, pos3, pos4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, union.getCharid());
        ps.setInt(2, union.getUnk1());
        ps.setInt(3, union.getUnk3());
        ps.setInt(4, union.getPosition());
        ps.setInt(5, union.getUnk2());
        ps.setInt(6, union.getPriset());
        ps.setInt(7, union.getPriset1());
        ps.setInt(8, union.getPriset2());
        ps.setInt(9, union.getPriset3());
        ps.setInt(10, union.getPriset4());
        ps.setInt(11, union.getPos());
        ps.setInt(12, union.getPos1());
        ps.setInt(13, union.getPos2());
        ps.setInt(14, union.getPos3());
        ps.setInt(15, union.getPos4());
        ps.executeUpdate();
        ps.close();
      } 
    } catch (Exception exception) {
      try {
        if (ps != null)
          ps.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } finally {
      try {
        if (ps != null)
          ps.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  public List<MapleUnion> getUnions() {
    return this.unions;
  }
  
  public void setUnions(List<MapleUnion> unions) {
    this.unions = unions;
  }
}
