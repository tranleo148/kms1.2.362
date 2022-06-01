package client;

import database.DatabaseConnection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class MapleHyperStats implements Serializable {
    private int position;
    private int skillid;
    private int skilllevel;
    private Map<Integer, Pair<Integer, Integer>> hyperstats;

    public MapleHyperStats(int pos, int skill, int level) {
        this.position = pos;
        this.skillid = skill;
        this.skilllevel = level;
    }
    
    public MapleHyperStats MapleHyperStats(int pos, int skill, int level) {
        this.position = pos;
        this.skillid = skill;
        this.skilllevel = level;
        return this;
    }

    public void saveToDB(Connection connection) throws SQLException {
        try {
            final PreparedStatement hps = connection.prepareStatement("UPDATE hyperstats SET chrid = ?, pos = ?, skillid = ?, skilllevel = ? WHERE chrid = ?");
            hps.setInt(1, 1);
            hps.setInt(2, position);
            hps.setInt(3, skillid);
            hps.setInt(4, skilllevel);
            hps.executeUpdate();
            hps.close();
            connection.close();
        } catch(Exception e) {
            
        } finally {
            
        }
    }
    
    public static ArrayList<MapleHyperStats> loadFromDB(Connection connection, int characterId) {
        ArrayList<MapleHyperStats> hyperStats = new ArrayList<MapleHyperStats>();
        try {
            
        } catch(Exception e) {
            
        } finally {
            
        }
        return hyperStats;
    }
        
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public int getSkillid() {
        return skillid;
    }
    
    public void setSkillid(int skill) {
        this.skillid = skill;
    }
    
    public int getSkillLevel() {
        return skilllevel;
    }
    
    public void setSkillLevel(int level) {
        this.skilllevel = level;
    }
}
