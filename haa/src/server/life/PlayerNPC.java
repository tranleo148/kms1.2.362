// 
// Decompiled by Procyon v0.5.36
// 

package server.life;

import tools.packet.CWvsContext;
import tools.packet.CField;
import client.MapleClient;
import client.inventory.MaplePet;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import handling.channel.ChannelServer;
import handling.world.World;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import server.maps.MapleMap;
import client.MapleCharacter;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import database.DatabaseConnection;
import java.util.HashMap;
import java.sql.ResultSet;
import java.util.Map;

public class PlayerNPC extends MapleNPC
{
    private Map<Byte, Integer> equips;
    private Map<Byte, Integer> secondEquips;
    private int mapid;
    private int face;
    private int hair;
    private int charId;
    private int secondFace;
    private byte skin;
    private byte gender;
    private byte secondSkin;
    private byte secondGender;
    private int[] pets;
    
    public PlayerNPC(final ResultSet rs) throws Exception {
        super(rs.getInt("ScriptId"), rs.getString("name"));
        this.equips = new HashMap<Byte, Integer>();
        this.secondEquips = new HashMap<Byte, Integer>();
        this.pets = new int[3];
        this.hair = rs.getInt("hair");
        this.face = rs.getInt("face");
        this.mapid = rs.getInt("map");
        this.skin = rs.getByte("skin");
        this.charId = rs.getInt("charid");
        this.gender = rs.getByte("gender");
        this.setCoords(rs.getInt("x"), rs.getInt("y"), rs.getInt("dir"), rs.getInt("Foothold"));
        final String[] pet = rs.getString("pets").split(",");
        for (int i = 0; i < 3; ++i) {
            if (pet[i] != null) {
                this.pets[i] = Integer.parseInt(pet[i]);
            }
            else {
                this.pets[i] = 0;
            }
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs2 = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM playernpcs_equip WHERE NpcId = ?");
            ps.setInt(1, this.getId());
            rs2 = ps.executeQuery();
            while (rs2.next()) {
                this.equips.put(rs2.getByte("equippos"), rs2.getInt("equipid"));
            }
            rs2.close();
            ps.close();
            con.close();
        }
        catch (Exception ex) {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            }
            catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }
    
    public PlayerNPC(final MapleCharacter cid, final int npc, final MapleMap map, final MapleCharacter base) {
        super(npc, cid.getName());
        this.equips = new HashMap<Byte, Integer>();
        this.secondEquips = new HashMap<Byte, Integer>();
        this.pets = new int[3];
        this.charId = cid.getId();
        this.mapid = map.getId();
        this.setCoords(base.getTruePosition().x, base.getTruePosition().y, 0, base.getFH());
        this.update(cid);
    }
    
    public void setCoords(final int x, final int y, final int f, final int fh) {
        this.setPosition(new Point(x, y));
        this.setCy(y);
        this.setRx0(x - 50);
        this.setRx1(x + 50);
        this.setF(f);
        this.setFh(fh);
    }
    
    public static void loadAll() {
        final List<PlayerNPC> toAdd = new ArrayList<PlayerNPC>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM playernpcs");
            rs = ps.executeQuery();
            while (rs.next()) {
                toAdd.add(new PlayerNPC(rs));
            }
            rs.close();
            ps.close();
        }
        catch (Exception se) {
            se.printStackTrace();
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch (SQLException se3) {
                se3.printStackTrace();
            }
        }
        for (final PlayerNPC npc : toAdd) {
            npc.addToServer();
        }
    }
    
    public static void updateByCharId(final MapleCharacter chr) {
        if (World.Find.findChannel(chr.getId()) > 0) {
            for (final PlayerNPC npc : ChannelServer.getInstance(World.Find.findChannel(chr.getId())).getAllPlayerNPC()) {
                npc.update(chr);
            }
        }
    }
    
    public void addToServer() {
        for (final ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.addPlayerNPC(this);
        }
    }
    
    public void removeFromServer() {
        for (final ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.removePlayerNPC(this);
        }
    }
    
    public void update(final MapleCharacter chr) {
        if (chr == null || this.charId != chr.getId()) {
            return;
        }
        this.setName(chr.getName());
        this.setHair(chr.getHair());
        this.setFace(chr.getFace());
        this.setSkin(chr.getSkinColor());
        this.setGender(chr.getGender());
        this.equips = new HashMap<Byte, Integer>();
        for (final Item item : chr.getInventory(MapleInventoryType.EQUIPPED).newList()) {
            if (item.getPosition() < -127) {
                continue;
            }
            this.equips.put((byte)item.getPosition(), item.getItemId());
        }
        this.saveToDB();
    }
    
    public void destroy() {
        this.destroy(false);
    }
    
    public void destroy(final boolean remove) {
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("DELETE FROM playernpcs WHERE scriptid = ?");
            ps.setInt(1, this.getId());
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("DELETE FROM playernpcs_equip WHERE npcid = ?");
            ps.setInt(1, this.getId());
            ps.executeUpdate();
            ps.close();
            if (remove) {
                this.removeFromServer();
            }
        }
        catch (Exception se) {
            se.printStackTrace();
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch (SQLException se3) {
                se3.printStackTrace();
            }
        }
    }
    
    public void saveToDB() {
        Connection con = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            if (this.getNPCFromWZ() == null) {
                this.destroy(true);
                return;
            }
            con = DatabaseConnection.getConnection();
            this.destroy();
            ps = con.prepareStatement("INSERT INTO playernpcs(name, hair, face, skin, x, y, map, charid, scriptid, foothold, dir, gender, pets) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, this.getName());
            ps.setInt(2, this.getHair());
            ps.setInt(3, this.getFace());
            ps.setInt(4, this.getSkinColor());
            ps.setInt(5, this.getTruePosition().x);
            ps.setInt(6, this.getTruePosition().y);
            ps.setInt(7, this.getMapId());
            ps.setInt(8, this.getCharId());
            ps.setInt(9, this.getId());
            ps.setInt(10, this.getFh());
            ps.setInt(11, this.getF());
            ps.setInt(12, this.getGender());
            final String[] pet = { "0", "0", "0" };
            for (int i = 0; i < 3; ++i) {
                if (this.pets[i] > 0) {
                    pet[i] = String.valueOf(this.pets[i]);
                }
            }
            ps.setString(13, pet[0] + "," + pet[1] + "," + pet[2]);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("INSERT INTO playernpcs_equip(npcid, charid, equipid, equippos) VALUES (?, ?, ?, ?)");
            ps.setInt(1, this.getId());
            ps.setInt(2, this.getCharId());
            for (final Map.Entry<Byte, Integer> equip : this.equips.entrySet()) {
                ps.setInt(3, equip.getValue());
                ps.setInt(4, equip.getKey());
                ps.executeUpdate();
            }
            ps.close();
        }
        catch (Exception se) {
            se.printStackTrace();
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch (SQLException se3) {
                se3.printStackTrace();
            }
        }
    }
    
    public short getJob() {
        return 0;
    }
    
    public int getDemonMarking() {
        return 0;
    }
    
    public Map<Byte, Integer> getEquips(final boolean moru) {
        return this.equips;
    }
    
    public Map<Byte, Integer> getSecondEquips(final boolean moru) {
        return this.secondEquips;
    }
    
    public Map<Byte, Integer> getTotems() {
        return new HashMap<Byte, Integer>();
    }
    
    public byte getSkinColor() {
        return this.skin;
    }
    
    public byte getSecondSkinColor() {
        return this.secondSkin;
    }
    
    public byte getGender() {
        return this.gender;
    }
    
    public byte getSecondGender() {
        return this.secondGender;
    }
    
    public int getFace() {
        return this.face;
    }
    
    public int getSecondFace() {
        return this.secondFace;
    }
    
    public int getHair() {
        return this.hair;
    }
    
    public int getCharId() {
        return this.charId;
    }
    
    public int getMapId() {
        return this.mapid;
    }
    
    public void setSkin(final byte s) {
        this.skin = s;
    }
    
    public void setFace(final int f) {
        this.face = f;
    }
    
    public void setHair(final int h) {
        this.hair = h;
    }
    
    public void setGender(final int g) {
        this.gender = (byte)g;
    }
    
    public int getPet(final int i) {
        return (this.pets[i] > 0) ? this.pets[i] : 0;
    }
    
    public void setPets(final List<MaplePet> p) {
        for (int i = 0; i < 3; ++i) {
            if (p != null && p.size() > i && p.get(i) != null) {
                this.pets[i] = p.get(i).getPetItemId();
            }
            else {
                this.pets[i] = 0;
            }
        }
    }
    
    @Override
    public void sendSpawnData(final MapleClient client) {
        client.getSession().writeAndFlush((Object)CField.NPCPacket.spawnNPC(this, true));
        client.getSession().writeAndFlush((Object)CWvsContext.spawnPlayerNPC(this, client.getPlayer()));
        client.getSession().writeAndFlush((Object)CField.NPCPacket.spawnNPCRequestController(this, true));
    }
    
    public MapleNPC getNPCFromWZ() {
        final MapleNPC npc = MapleLifeFactory.getNPC(this.getId());
        if (npc != null) {
            npc.setName(this.getName());
        }
        return npc;
    }
}
