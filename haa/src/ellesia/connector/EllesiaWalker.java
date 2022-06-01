package ellesia.connector;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EllesiaWalker {
    public static void setAlive(String id, boolean alive) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET allowed = ? WHERE name = ?");
            ps.setInt(1, alive ? 1 : 0);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void setIP(String id, String ip) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET connecterIP = ? WHERE name = ?");
            ps.setString(1, ip);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void setBanned(String id, boolean banned, String reason) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE name = ?");
            ps.setBoolean(1, banned);
            ps.setString(2, reason);
            ps.setString(3, id);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean Login(String id, String pw) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = rs.getString("password");
                if(password.equalsIgnoreCase(pw)) return true;
            }
            ps.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
