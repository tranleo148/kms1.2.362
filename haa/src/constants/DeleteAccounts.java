package constants;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteAccounts {
  public static void main(String[] args) {
    DatabaseConnection.init();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("DELETE FROM inventoryitems WHERE 'type' = ?");
      ps.setInt(1, 0);
      ps.executeUpdate();
      ps.close();
      con.close();
    } catch (SQLException sex) {
      sex.printStackTrace();
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
}
