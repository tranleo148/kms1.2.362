package handling.login.handler;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutoRegister {
  private static final int ACCOUNTS_PER_IP = 1;
  
  public static final boolean autoRegister = true;
  
  public static boolean getAccountExists(String login) {
    boolean accountExists = false;
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
      ps.setString(1, login);
      rs = ps.executeQuery();
      if (rs.first())
        accountExists = true; 
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.printStackTrace();
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
      } catch (SQLException se2) {
        se2.printStackTrace();
      } 
    } 
    return accountExists;
  }
  
  public static boolean createAccount(String login, String pwd, String eip) {
    Connection con;
    String sockAddr = eip;
    boolean success = false;
    try {
      con = DatabaseConnection.getConnection();
    } catch (Exception ex) {
      ex.printStackTrace();
      return success;
    } 
    try {
      PreparedStatement ipc = con.prepareStatement("SELECT SessionIP FROM accounts WHERE SessionIP = ?");
      try {
        ipc.setString(1, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
        ResultSet rs = ipc.executeQuery();
        if (!rs.first() || (
          rs.last() && rs.getRow() < 1))
          try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, SessionIP) VALUES (?, ?, ?, ?, ?, ?)");
            try {
              ps.setString(1, login);
              ps.setString(2, pwd);
              ps.setString(3, "no@email.provided");
              ps.setString(4, "2008-04-07");
              ps.setString(5, "00-00-00-00-00-00");
              ps.setString(6, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
              ps.executeUpdate();
              ps.close();
              if (ps != null)
                ps.close(); 
            } catch (Throwable throwable) {
              if (ps != null)
                try {
                  ps.close();
                } catch (Throwable throwable1) {
                  throwable.addSuppressed(throwable1);
                }  
              throw throwable;
            } 
            success = true;
          } catch (SQLException ex2) {
            ex2.printStackTrace();
            boolean bool = success;
            if (ipc != null)
              ipc.close(); 
            return bool;
          }  
        rs.close();
        ipc.close();
        if (ipc != null)
          ipc.close(); 
      } catch (Throwable throwable) {
        if (ipc != null)
          try {
            ipc.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          }  
        throw throwable;
      } 
    } catch (SQLException ex3) {
      ex3.printStackTrace();
      if (con != null)
        try {
          con.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } finally {
      if (con != null)
        try {
          con.close();
        } catch (SQLException e2) {
          e2.printStackTrace();
        }  
    } 
    return success;
  }
}