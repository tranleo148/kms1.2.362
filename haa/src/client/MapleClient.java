package client;

import constants.ServerConstants;
import database.DatabaseConnection;
import database.DatabaseException;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.handler.PlayerHandler;
import handling.login.LoginServer;
import handling.world.MapleMessengerCharacter;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import handling.world.guild.MapleGuildCharacter;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.script.ScriptEngine;
import javax.sql.rowset.serial.SerialBlob;
import server.Timer;
import server.games.BattleReverse;
import server.games.OneCardGame;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import server.shops.IMaplePlayerShop;
import tools.CurrentTime;
import tools.FileoutputUtil;
import tools.MapleAESOFB;
import tools.Pair;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;
import tools.packet.LoginPacket;

public class MapleClient {

    private static final long serialVersionUID = 9179541993413738569L;

    public static final byte LOGIN_NOTLOGGEDIN = 0;

    public static final byte LOGIN_SERVER_TRANSITION = 1;

    public static final byte LOGIN_LOGGEDIN = 2;

    public static final byte CHANGE_CHANNEL = 3;

    public static final int DEFAULT_CHARSLOT = 48;//characterslot

    public static final String CLIENT_KEY = "CLIENT";

    public static final AttributeKey<MapleClient> CLIENTKEY = AttributeKey.valueOf("mapleclient_netty");

    private transient MapleAESOFB send;

    private transient MapleAESOFB receive;

    private transient Channel session;

    private MapleCharacter player;

    private int channel = 1, accId = -1, world, birthday, unionLevel = 0, point = 0, SecondPwUse = 0, saveOTPNum = 0;

    private int charslots = DEFAULT_CHARSLOT;

    private long discordid = 0L;

    private long createchartime = 0L;

    private boolean loggedIn = false;

    private boolean serverTransition = false;

    private boolean sendOTP = false;

    private transient Calendar tempban = null;

    private String accountName;

    private String pwd;

    private String saveOTPDay;

    private String LIEDETECT;

    private int lieDectctCount;

    private transient long lastPong = 0L;

    private transient long lastPing = 0L;

    private boolean monitored = false;

    private boolean receiving = true;

    private boolean auction = false;

    private boolean cashShop = false;

    private boolean farm = false;

    private boolean gm;

    private byte greason = 1;

    private byte gender = -1;

    private byte nameChangeEnable = 0;

    public transient short loginAttempt = 0;

    private transient List<Integer> allowedChar = new LinkedList<>();

    private transient Set<String> macs = new HashSet<>();

    private transient Map<String, ScriptEngine> engines = new HashMap<>();

    private transient ScheduledFuture<?> idleTask = null;

    private transient String secondPassword;

    private transient String tempIP = "";

    private final transient Lock mutex = new ReentrantLock(true);

    private final transient Lock npc_mutex = new ReentrantLock();

    private long lastNpcClick = 0L;

    private long chatBlockedTime = 0L;

    private static final Lock login_mutex = new ReentrantLock(true);

    private final List<Integer> soulMatch = new ArrayList<>();

    private final Map<Integer, Pair<Short, Short>> charInfo = new LinkedHashMap<>();

    private boolean firstlogin = true;

    private Map<String, String> keyValues = new HashMap<>();

    private Map<Integer, List<Pair<String, String>>> customDatas = new HashMap<>();

    private Map<Integer, String> customKeyValue = new ConcurrentHashMap<>();

    private List<MapleCabinet> cabinet = new ArrayList<>();

    private List<MapleShopLimit> shops = new ArrayList<>();

    private Map<MapleQuest, MapleQuestStatus> quests = new ConcurrentHashMap<>();

    public MapleClient(Channel session, MapleAESOFB send, MapleAESOFB receive) {
        this.send = send;
        this.receive = receive;
        this.session = session;
    }

    public final MapleAESOFB getReceiveCrypto() {
        return this.receive;
    }

    public final MapleAESOFB getSendCrypto() {
        return this.send;
    }

    public final Channel getSession() {
        return this.session;
    }

    public final Lock getLock() {
        return this.mutex;
    }

    public final Lock getNPCLock() {
        return this.npc_mutex;
    }

    public MapleCharacter getPlayer() {
        return this.player;
    }

    public void setPlayer(MapleCharacter player) {
        this.player = player;
    }

    public void createdChar(int id) {
        this.allowedChar.add(Integer.valueOf(id));
    }

    public final boolean login_Auth(int id) {
        return this.allowedChar.contains(Integer.valueOf(id));
    }

    public boolean canMakeCharacter(int serverId) {
        return (loadCharactersSize(serverId) < getCharacterSlots());
    }

    public final List<MapleCharacter> loadCharacters(int serverId) {
        List<MapleCharacter> chars = new LinkedList<>();
        for (CharNameAndId cni : loadCharactersInternal(serverId)) {
            MapleCharacter chr = MapleCharacter.loadCharFromDB(cni.id, this, false);
            chars.add(chr);
            this.charInfo.put(Integer.valueOf(chr.getId()), new Pair<>(Short.valueOf(chr.getLevel()), Short.valueOf(chr.getJob())));
            this.allowedChar.add(Integer.valueOf(chr.getId()));
        }
        return chars;
    }

    public List<String> loadCharacterNames(int serverId) {
        List<String> chars = new LinkedList<>();
        for (CharNameAndId cni : loadCharactersInternal(serverId)) {
            chars.add(cni.name);
        }
        return chars;
    }

    private List<CharNameAndId> loadCharactersInternal(int serverId) {
        List<CharNameAndId> chars = new LinkedList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT id, name, gm FROM characters WHERE accountid = ? AND world = ? ORDER BY `order` ASC");
            ps.setInt(1, this.accId);
            ps.setInt(2, serverId);
            rs = ps.executeQuery();
            while (rs.next()) {
                chars.add(new CharNameAndId(rs.getString("name"), rs.getInt("id")));
                LoginServer.getLoginAuth(rs.getInt("id"));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("error loading characters internal");
            e.printStackTrace();
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return chars;
    }

    private int loadCharactersSize(int serverId) {
        int chars = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT count(*) FROM characters WHERE accountid = ? AND world = ?");
            ps.setInt(1, this.accId);
            ps.setInt(2, serverId);
            rs = ps.executeQuery();
            if (rs.next()) {
                chars = rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("error loading characters internal");
            e.printStackTrace();
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return chars;
    }

    public boolean isLoggedIn() {
        return (this.loggedIn && this.accId >= 0);
    }

    private Calendar getTempBanCalendar(ResultSet rs) throws SQLException {
        Calendar lTempban = Calendar.getInstance();
        if (rs.getLong("tempban") == 0L) {
            lTempban.setTimeInMillis(0L);
            return lTempban;
        }
        Calendar today = Calendar.getInstance();
        lTempban.setTimeInMillis(rs.getTimestamp("tempban").getTime());
        if (today.getTimeInMillis() < lTempban.getTimeInMillis()) {
            return lTempban;
        }
        lTempban.setTimeInMillis(0L);
        return lTempban;
    }

    public Calendar getTempBanCalendar() {
        return this.tempban;
    }

    public byte getBanReason() {
        return this.greason;
    }

    public void ban() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ?");
            ps.setString(1, "꺼지세요 ㅋㅋ");
            ps.setInt(2, this.accId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error while banning" + e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public boolean hasBannedIP() {
        boolean ret = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT COUNT(*) FROM ipbans WHERE ? LIKE CONCAT(ip, '%')");
            ps.setString(1, getSessionIPAddress());
            rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                ret = true;
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error checking ip bans" + ex);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return ret;
    }

    public boolean hasBannedMac() {
        if (this.macs.isEmpty()) {
            return false;
        }
        boolean ret = false;
        int i = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM macbans WHERE mac IN (");
            for (i = 0; i < this.macs.size(); i++) {
                sql.append("?");
                if (i != this.macs.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");
            ps = con.prepareStatement(sql.toString());
            i = 0;
            for (String mac : this.macs) {
                i++;
                ps.setString(i, mac);
            }
            rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                ret = true;
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error checking mac bans" + ex);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return ret;
    }

    private void loadMacsIfNescessary() throws SQLException {
        if (this.macs.isEmpty()) {
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT macs FROM accounts WHERE id = ?");
                ps.setInt(1, this.accId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getString("macs") != null) {
                        String[] macData = rs.getString("macs").split(", ");
                        for (String mac : macData) {
                            if (!mac.equals("")) {
                                this.macs.add(mac);
                            }
                        }
                    }
                } else {
                    rs.close();
                    ps.close();
                    throw new RuntimeException("No valid account associated with this client.");
                }
                rs.close();
                ps.close();
                con.close();
            } catch (Exception exception) {
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
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            } finally {
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
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    public void banMacs() {
        try {
            loadMacsIfNescessary();
            if (this.macs.size() > 0) {
                String[] macBans = new String[this.macs.size()];
                int z = 0;
                for (String mac : this.macs) {
                    macBans[z] = mac;
                    z++;
                }
                banMacs(macBans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static final void banMacs(String[] macs) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            List<String> filtered = new LinkedList<>();
            ps = con.prepareStatement("SELECT filter FROM macfilters");
            rs = ps.executeQuery();
            while (rs.next()) {
                filtered.add(rs.getString("filter"));
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("INSERT INTO macbans (mac) VALUES (?)");
            for (String mac : macs) {
                boolean matched = false;
                for (String filter : filtered) {
                    if (mac.matches(filter)) {
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    ps.setString(1, mac);
                    try {
                        ps.executeUpdate();
                    } catch (SQLException sQLException) {
                    }
                }
            }
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error banning MACs" + e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public int finishLogin() {
        updateLoginState(2, getSessionIPAddress());
        return 0;
    }

    public void clearInformation() {
        this.accountName = null;
        this.accId = -1;
        this.secondPassword = null;
        this.gm = false;
        this.loggedIn = false;
        this.greason = 1;
        this.tempban = null;
        this.gender = -1;
    }

    public void SaveQuest(Connection con) {
        PreparedStatement ps = null;
        PreparedStatement pse = null;
        PreparedStatement pse1 = null;
        ResultSet rs = null;
        try {
            if (this.quests != null) {
                pse1 = con.prepareStatement("DELETE FROM acc_queststatus WHERE `accid` = ?");
                pse1.setInt(1, this.accId);
                pse1.executeUpdate();
                pse1.close();
                for (MapleQuestStatus q : this.quests.values()) {
                    ps = con.prepareStatement("INSERT INTO acc_queststatus (`queststatusid`, `accid`, `quest`, `status`, `time`, `forfeited`, `customData`) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", 1);
                    ps.setInt(1, this.accId);
                    ps.setInt(2, q.getQuest().getId());
                    ps.setInt(3, q.getStatus());
                    ps.setInt(4, (int) (q.getCompletionTime() / 1000L));
                    ps.setInt(5, q.getForfeited());
                    ps.setString(6, q.getCustomData());
                    ps.execute();
                    rs = ps.getGeneratedKeys();
                    if (q.hasMobKills()) {
                        rs.next();
                        for (Iterator<Integer> iterator = q.getMobKills().keySet().iterator(); iterator.hasNext();) {
                            int mob = ((Integer) iterator.next()).intValue();
                            pse = con.prepareStatement("INSERT INTO acc_queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
                            pse.setInt(1, rs.getInt(1));
                            pse.setInt(2, mob);
                            pse.setInt(3, q.getMobKills(mob));
                            pse.execute();
                            pse.close();
                        }
                    }
                    ps.close();
                    rs.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (pse != null) {
                    pse.close();
                }
                if (pse1 != null) {
                    pse1.close();
                }
                if (rs != null) {
                    pse.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public void loadCustomDatas() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        this.customDatas.clear();
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM account_customdata WHERE accid = ?");
            ps.setInt(1, this.accId);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                if (!this.customDatas.containsKey(Integer.valueOf(id))) {
                    this.customDatas.put(Integer.valueOf(id), new ArrayList<>());
                }
                ((List) this.customDatas.get(Integer.valueOf(id))).add(new Pair<>(rs.getString("key"), rs.getString("value")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadCustomKeyValue() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        this.customKeyValue.clear();
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM acc_questinfo WHERE accid = ?");
            ps.setInt(1, this.accId);
            rs = ps.executeQuery();
            while (rs.next()) {
                this.customKeyValue.put(Integer.valueOf(rs.getInt("quest")), rs.getString("key"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadQuest() {
        PreparedStatement ps = null;
        PreparedStatement pse = null;
        ResultSet rs = null;
        Connection con = null;
        this.quests.clear();
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM acc_queststatus WHERE accid = ?");
            ps.setInt(1, this.accId);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("quest");
                MapleQuest q = MapleQuest.getInstance(id);
                byte stat = rs.getByte("status");
                MapleQuestStatus status = new MapleQuestStatus(q, stat);
                long cTime = rs.getLong("time");
                if (cTime > -1L) {
                    status.setCompletionTime(cTime * 1000L);
                }
                status.setForfeited(rs.getInt("forfeited"));
                status.setCustomData(rs.getString("customData"));
                this.quests.put(q, status);
                pse = con.prepareStatement("SELECT * FROM acc_queststatusmobs WHERE queststatusid = ?");
                pse.setInt(1, rs.getInt("queststatusid"));
                ResultSet rsMobs = pse.executeQuery();
                if (rsMobs.next()) {
                    status.setMobKills(rsMobs.getInt("mob"), rsMobs.getInt("count"));
                }
                pse.close();
                rsMobs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pse != null) {
                    pse.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadKeyValues() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        this.keyValues.clear();
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM acckeyvalue WHERE id = ?");
            ps.setInt(1, this.accId);
            rs = ps.executeQuery();
            while (rs.next()) {
                this.keyValues.put(rs.getString("key"), rs.getString("value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int login(String login, String pwd, String mac, boolean ipMacBanned) {
        boolean ipBan = hasBannedIP();
        boolean macBan = hasBannedMac();
        return login(login, pwd, mac, false, (ipBan || macBan));
    }

    public int login(String login, String pwd, String mac, boolean weblogin, boolean ipMacBanned) {
        int loginok = 5;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                int banned = rs.getInt("banned");
                String passhash = rs.getString("password");
                String oldSession = rs.getString("SessionIP");
                this.accountName = login;
                this.accId = rs.getInt("id");
                this.secondPassword = rs.getString("2ndpassword");
                this.gm = (rs.getInt("gm") > 0);
                this.greason = rs.getByte("greason");
                this.tempban = getTempBanCalendar(rs);
                this.gender = rs.getByte("gender");
                this.nameChangeEnable = rs.getByte("nameChange");
                this.point = rs.getInt("points");
                this.chatBlockedTime = rs.getLong("chatBlockedTime");
                this.discordid = Long.parseUnsignedLong(rs.getString("DiscordId"));
                this.SecondPwUse = rs.getInt("SecondPwUse");
                this.saveOTPNum = rs.getInt("saveOTPNum");
                this.saveOTPDay = rs.getString("saveOTPDay");

                int allowed = rs.getByte("allowed");
                    rs.close();
                    ps.close();

                if (banned > 0 && !this.gm) {
                    loginok = 3;
                } else {
                    if (banned == -1) {
                        unban();
                    }
                    if (pwd.equals(passhash)) {
                        this.pwd = pwd;
                        loginok = 0;
                    } else {
                        this.loggedIn = false;
                        loginok = 4;
                        return loginok;
                    }
                    byte loginstate = getLoginState();
                    if (loginstate > 0) {
                        this.loggedIn = false;
                        loginok = 7;
                    } else {
                        boolean updatePasswordHash = false;
                        if (passhash == null || passhash.isEmpty()) {
                            if (oldSession != null && !oldSession.isEmpty()) {
                                this.loggedIn = getSessionIPAddress().equals(oldSession);
                                loginok = this.loggedIn ? 0 : 4;
                                updatePasswordHash = this.loggedIn;
                            } else {
                                loginok = 4;
                                this.loggedIn = false;
                            }
                        }
                    }
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("ERROR" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return loginok;
    }

  
    public boolean CheckSecondPassword(String in) {
        boolean allow = false;
        if (in.equals(this.secondPassword)) {
            allow = true;
        }
        return allow;
    }

    private void unban() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET banned = 0, banreason = '' WHERE id = ?");
            ps.setInt(1, this.accId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error while unbanning" + e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static final byte unban(String charname) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT accountid from characters where name = ?");
            ps.setString(1, charname);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            int accid = rs.getInt(1);
            rs.close();
            ps.close();
            ps = con.prepareStatement("UPDATE accounts SET banned = 0, banreason = '' WHERE id = ?");
            ps.setInt(1, accid);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error while unbanning" + e);
            return -2;
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return 0;
    }

    public void updateMacs(String macData) {
        for (String mac : macData.split(", ")) {
            this.macs.add(mac);
        }
        StringBuilder newMacData = new StringBuilder();
        Iterator<String> iter = this.macs.iterator();
        while (iter.hasNext()) {
            newMacData.append(iter.next());
            if (iter.hasNext()) {
                newMacData.append(", ");
            }
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET macs = ? WHERE id = ?");
            ps.setString(1, newMacData.toString());
            ps.setInt(2, this.accId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error saving MACs" + e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void setAccID(int id) {
        this.accId = id;
    }

    public int getAccID() {
        return this.accId;
    }

    public final void updateLoginState(int newstate, String SessionID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET loggedin = ?, SessionIP = ?, lastlogin = CURRENT_TIMESTAMP() WHERE id = ?");
            ps.setInt(1, newstate);
            ps.setString(2, SessionID);
            ps.setInt(3, getAccID());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("error updating login state" + e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        if (newstate == 0) {
            this.loggedIn = false;
            this.serverTransition = false;
        } else {
            this.serverTransition = (newstate == 1 || newstate == 3);
            this.loggedIn = !this.serverTransition;
        }
    }

    public final void updateLoginState(int newstate, String SessionID, String accname) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET loggedin = ?, SessionIP = ?, lastlogin = CURRENT_TIMESTAMP() WHERE name = ?");
            ps.setInt(1, newstate);
            ps.setString(2, SessionID);
            ps.setString(3, accname);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("error updating login state" + e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        if (newstate == 0) {
            this.loggedIn = false;
            this.serverTransition = false;
        } else {
            this.serverTransition = (newstate == 1 || newstate == 3);
            this.loggedIn = !this.serverTransition;
        }
    }

    public final void updateSecondPassword() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE `accounts` SET `2ndpassword` = ? WHERE id = ?");
            ps.setString(1, this.secondPassword);
            ps.setInt(2, this.accId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("error updating login state" + e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public final byte getLoginState() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT loggedin, lastlogin FROM accounts WHERE id = ?");
            ps.setInt(1, getAccID());
            rs = ps.executeQuery();
            if (!rs.next()) {
                ps.close();
                throw new DatabaseException("Everything sucks ?꾩씠??: " + getAccID());
            }
            byte state = rs.getByte("loggedin");
            if ((state == 1 || state == 3)
                    && rs.getTimestamp("lastlogin").getTime() + 20000L < System.currentTimeMillis()) {
                state = 0;
                updateLoginState(state, null);
            }
            rs.close();
            ps.close();
            if (state == 2) {
                this.loggedIn = true;
            } else {
                this.loggedIn = false;
            }
            return state;
        } catch (SQLException e) {
            this.loggedIn = false;
            throw new DatabaseException("error getting login state", e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public final boolean checkBirthDate(int date) {
        return (this.birthday == date);
    }

    public final void removalTask(boolean shutdown) {
        try {
            this.player.cancelAllBuffs_();
            this.player.cancelAllDebuffs();
            if (this.player.getMarriageId() > 0) {
                MapleQuestStatus stat1 = this.player.getQuestNoAdd(MapleQuest.getInstance(160001));
                MapleQuestStatus stat2 = this.player.getQuestNoAdd(MapleQuest.getInstance(160002));
                if (stat1 != null && stat1.getCustomData() != null && (stat1.getCustomData().equals("2_") || stat1.getCustomData().equals("2"))) {
                    if (stat2 != null && stat2.getCustomData() != null) {
                        stat2.setCustomData("0");
                    }
                    stat1.setCustomData("3");
                }
            }
            if (this.player.getMapId() == 180000002 && !this.player.isIntern()) {
                MapleQuestStatus stat1 = this.player.getQuestNAdd(MapleQuest.getInstance(123455));
                MapleQuestStatus stat2 = this.player.getQuestNAdd(MapleQuest.getInstance(123456));
                if (stat1.getCustomData() == null) {
                    stat1.setCustomData(String.valueOf(System.currentTimeMillis()));
                } else if (stat2.getCustomData() == null) {
                    stat2.setCustomData("0");
                } else {
                    int seconds = Integer.parseInt(stat2.getCustomData()) - (int) ((System.currentTimeMillis() - Long.parseLong(stat1.getCustomData())) / 1000L);
                    if (seconds < 0) {
                        seconds = 0;
                    }
                    stat2.setCustomData(String.valueOf(seconds));
                }
            }
            this.player.changeRemoval(true);
            if (this.player.getEventInstance() != null) {
                this.player.getEventInstance().playerDisconnected(this.player, this.player.getId());
            }
            IMaplePlayerShop shop = this.player.getPlayerShop();
            if (shop != null) {
                shop.removeVisitor(this.player);
                if (shop.isOwner(this.player)) {
                    if (shop.getShopType() == 1 && shop.isAvailable() && !shutdown) {
                        shop.setOpen(true);
                    } else {
                        shop.closeShop(true, !shutdown);
                    }
                }
            }
            this.player.setMessenger(null);
            if (this.player.getMap() != null) {
                if (shutdown || (getChannelServer() != null && getChannelServer().isShutdown())) {
                    int questID = -1;
                    switch (this.player.getMapId()) {
                        case 240060200:
                            questID = 160100;
                            break;
                        case 240060201:
                            questID = 160103;
                            break;
                        case 280030000:
                            questID = 160101;
                            break;
                        case 280030001:
                            questID = 160102;
                            break;
                        case 270050100:
                            questID = 160101;
                            break;
                        case 105100300:
                        case 105100400:
                            questID = 160106;
                            break;
                        case 211070000:
                        case 211070100:
                        case 211070101:
                        case 211070110:
                            questID = 160107;
                            break;
                        case 551030200:
                            questID = 160108;
                            break;
                        case 271040100:
                            questID = 160109;
                            break;
                    }
                    if (questID > 0) {
                        this.player.getQuestNAdd(MapleQuest.getInstance(questID)).setCustomData("0");
                    }
                } else if (this.player.isAlive()) {
                    switch (this.player.getMapId()) {
                        case 220080001:
                        case 541010100:
                        case 541020800:
                            this.player.getMap().addDisconnected(this.player.getId());
                            break;
                    }
                }
                this.player.getMap().removePlayer(this.player);
            }
        } catch (Throwable e) {
            FileoutputUtil.outputFileError("Log_AccountStuck.rtf", e);
        }
    }

    public String getPassword(String login) {
        String password = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                password = rs.getString("password");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return password;
    }

    public final void disconnect(boolean RemoveInChannelServer, boolean fromCS) {
        disconnect(RemoveInChannelServer, fromCS, false);
    }

    public final void disconnect(boolean RemoveInChannelServer, boolean fromCS, boolean shutdown) {
        if (this.player != null) {
            FileoutputUtil.log(FileoutputUtil.접속종료로그, "[종료] 계정번호 : " + this.player.getClient().getAccID() + " | " + this.player.getName() + "(" + this.player.getId() + ")이 종료.");
            MapleMap map = this.player.getMap();
            MapleParty party = this.player.getParty();
            String namez = this.player.getName();
            int idz = this.player.getId(), messengerid = (this.player.getMessenger() == null) ? 0 : this.player.getMessenger().getId(), gid = this.player.getGuildId();
            BuddyList bl = this.player.getBuddylist();
            MaplePartyCharacter chrp = new MaplePartyCharacter(this.player);
            MapleMessengerCharacter chrm = new MapleMessengerCharacter(this.player);
            MapleGuildCharacter chrg = this.player.getMGC();
            removalTask(shutdown);
            LoginServer.getLoginAuth(this.player.getId());
            this.player.setLastDisconnectTime(System.currentTimeMillis());
            this.player.saveToDB(true, fromCS);
            if (OneCardGame.oneCardMatchingQueue.contains(this.player)) {
                OneCardGame.oneCardMatchingQueue.remove(this.player);
            }
            if (BattleReverse.BattleReverseMatchingQueue.contains(this.player)) {
                BattleReverse.BattleReverseMatchingQueue.remove(this.player);
            }
            if (this.player.getSecondaryStatEffectTimer() != null) {
                this.player.getSecondaryStatEffectTimer().cancel(true);
            } else {
                System.out.println("NULL TIMER");
            }
            if (shutdown) {
                this.player = null;
                this.receiving = false;
                return;
            }
            if (!fromCS) {
                ChannelServer ch = ChannelServer.getInstance((map == null) ? this.channel : map.getChannel());
                int chz = World.Find.findChannel(idz);
                if (chz < -1) {
                    disconnect(RemoveInChannelServer, true);
                    return;
                }
                try {
                    if (chz == -1 || ch == null || ch.isShutdown()) {
                        this.player = null;
                        return;
                    }
                    if (messengerid > 0) {
                        World.Messenger.leaveMessenger(messengerid, chrm);
                    }
                    if (party != null) {
                        chrp.setOnline(false);
                        World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, chrp);
                        if (map != null && party.getLeader().getId() == idz) {
                            MaplePartyCharacter lchr = null;
                            for (MaplePartyCharacter pchr : party.getMembers()) {
                                if (pchr != null && map.getCharacterById(pchr.getId()) != null && (lchr == null || lchr.getLevel() < pchr.getLevel())) {
                                    lchr = pchr;
                                }
                            }
                            if (lchr != null) {
                                World.Party.updateParty(party.getId(), PartyOperation.CHANGE_LEADER_DC, lchr);
                            }
                        }
                    }
                    if (bl != null) {
                        if (!this.serverTransition) {
                            World.Buddy.loggedOff(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                        } else {
                            World.Buddy.loggedOn(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                        }
                    }
                    if (gid > 0 && chrg != null) {
                        World.Guild.setGuildMemberOnline(chrg, false, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FileoutputUtil.outputFileError("Log_AccountStuck.rtf", e);
                    System.err.println(getLogMessage(this, "ERROR") + e);
                } finally {
                    if (RemoveInChannelServer && ch != null) {
                        ch.removePlayer(idz, this.accId, namez);
                    }
                    this.player = null;
                }
            } else {
                int ch = World.Find.findChannel(idz);
                if (ch > 0) {
                    disconnect(RemoveInChannelServer, false);
                    return;
                }
                try {
                    if (party != null) {
                        chrp.setOnline(false);
                        World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, chrp);
                    }
                    if (!this.serverTransition) {
                        World.Buddy.loggedOff(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                    } else {
                        World.Buddy.loggedOn(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                    }
                    if (gid > 0 && chrg != null) {
                        World.Guild.setGuildMemberOnline(chrg, false, -1);
                    }
                    if (this.player != null) {
                        this.player.setMessenger(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FileoutputUtil.outputFileError("Log_AccountStuck.rtf", e);
                    System.err.println(getLogMessage(this, "ERROR") + e);
                } finally {
                    if (RemoveInChannelServer && ch > 0) {
                        CashShopServer.getPlayerStorage().deregisterPlayer(idz, this.accId, namez);
                    }
                    this.player = null;
                }
            }
        }
        if (!this.serverTransition && isLoggedIn()) {
            updateLoginState(0, getSessionIPAddress());
        }
        this.engines.clear();
    }

    public final void disconnect(MapleCharacter player, boolean RemoveInChannelServer, boolean fromCS, boolean shutdown) {
        if (player != null) {
            FileoutputUtil.log(FileoutputUtil.접속종료로그, "[종료] 계정번호 : " + player.getClient().getAccID() + " | " + player.getName() + "(" + player.getId() + ")이 종료 2번째에서 처리.");
            MapleMap map = player.getMap();
            MapleParty party = player.getParty();
            String namez = player.getName();
            int idz = player.getId(), messengerid = (player.getMessenger() == null) ? 0 : player.getMessenger().getId(), gid = player.getGuildId();
            BuddyList bl = player.getBuddylist();
            MaplePartyCharacter chrp = new MaplePartyCharacter(player);
            MapleMessengerCharacter chrm = new MapleMessengerCharacter(player);
            MapleGuildCharacter chrg = player.getMGC();
            removalTask(shutdown);
            LoginServer.getLoginAuth(player.getId());
            player.setLastDisconnectTime(System.currentTimeMillis());
            player.saveToDB(true, fromCS);
            if (OneCardGame.oneCardMatchingQueue.contains(player)) {
                OneCardGame.oneCardMatchingQueue.remove(player);
            }
            if (BattleReverse.BattleReverseMatchingQueue.contains(player)) {
                BattleReverse.BattleReverseMatchingQueue.remove(player);
            }
            if (player.getSecondaryStatEffectTimer() != null) {
                player.getSecondaryStatEffectTimer().cancel(true);
            } else {
                System.out.println("NULL TIMER");
            }
            if (!fromCS) {
                ChannelServer ch = ChannelServer.getInstance((map == null) ? this.channel : map.getChannel());
                int chz = World.Find.findChannel(idz);
                if (chz < -1) {
                    disconnect(RemoveInChannelServer, true);
                    return;
                }
                try {
                    if (chz == -1 || ch == null || ch.isShutdown()) {
                        player = null;
                        return;
                    }
                    if (messengerid > 0) {
                        World.Messenger.leaveMessenger(messengerid, chrm);
                    }
                    if (party != null) {
                        chrp.setOnline(false);
                        World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, chrp);
                        if (map != null && party.getLeader().getId() == idz) {
                            MaplePartyCharacter lchr = null;
                            for (MaplePartyCharacter pchr : party.getMembers()) {
                                if (pchr != null && map.getCharacterById(pchr.getId()) != null && (lchr == null || lchr.getLevel() < pchr.getLevel())) {
                                    lchr = pchr;
                                }
                            }
                            if (lchr != null) {
                                World.Party.updateParty(party.getId(), PartyOperation.CHANGE_LEADER_DC, lchr);
                            }
                        }
                    }
                    if (bl != null) {
                        if (!this.serverTransition) {
                            World.Buddy.loggedOff(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                        } else {
                            World.Buddy.loggedOn(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                        }
                    }
                    if (gid > 0 && chrg != null) {
                        World.Guild.setGuildMemberOnline(chrg, false, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FileoutputUtil.outputFileError("Log_AccountStuck.rtf", e);
                    System.err.println(getLogMessage(this, "ERROR") + e);
                } finally {
                    if (RemoveInChannelServer && ch != null) {
                        ch.removePlayer(idz, this.accId, namez);
                    }
                    player = null;
                }
            } else {
                int ch = World.Find.findChannel(idz);
                try {
                    if (party != null) {
                        chrp.setOnline(false);
                        World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, chrp);
                    }
                    if (!this.serverTransition) {
                        World.Buddy.loggedOff(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                    } else {
                        World.Buddy.loggedOn(namez, idz, this.channel, this.accId, bl.getBuddyIds());
                    }
                    if (gid > 0 && chrg != null) {
                        World.Guild.setGuildMemberOnline(chrg, false, -1);
                    }
                    if (player != null) {
                        player.setMessenger(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FileoutputUtil.outputFileError("Log_AccountStuck.rtf", e);
                    System.err.println(getLogMessage(this, "ERROR") + e);
                } finally {
                    if (RemoveInChannelServer && ch > 0) {
                        CashShopServer.getPlayerStorage().deregisterPlayer(idz, this.accId, namez);
                    }
                    player = null;
                }
            }
        }
        updateLoginState(0, getSessionIPAddress());
        this.engines.clear();
    }

    public final String getSessionIPAddress() {
        return this.session.remoteAddress().toString().split(":")[0].split("/")[1];
    }

    public final boolean CheckIPAddress() {
        if (this.accId < 0) {
            return false;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT SessionIP, banned FROM accounts WHERE id = ?");
            ps.setInt(1, this.accId);
            rs = ps.executeQuery();
            boolean canlogin = false;
            if (rs.next()) {
                String sessionIP = rs.getString("SessionIP");
                if (sessionIP != null) {
                    canlogin = getSessionIPAddress().equals(sessionIP.split(":")[0]);
                }
                if (rs.getInt("banned") > 0) {
                    canlogin = false;
                }
            }
            rs.close();
            ps.close();
            con.close();
            return canlogin;
        } catch (SQLException e) {
            System.out.println("Failed in checking IP address for client.");
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return true;
    }

    public final void DebugMessage(StringBuilder sb) {
    }

    public final int getChannel() {
        return this.channel;
    }

    public final ChannelServer getChannelServer() {
        return ChannelServer.getInstance(this.channel);
    }

    public final int deleteCharacter(int cid) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT guildid, guildrank, familyid, name FROM characters WHERE id = ? AND accountid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, this.accId);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return 9;
            }
            String name = rs.getString("name");
            if (rs.getInt("guildid") > 0) {
                if (rs.getInt("guildrank") == 1) {
                    rs.close();
                    ps.close();
                    return 10;
                }
                World.Guild.deleteGuildCharacter(rs.getInt("guildid"), cid);
            }
            rs.close();
            ps.close();
            ps2 = con.prepareStatement("SELECT email, macs FROM accounts WHERE id = ?");
            ps2.setInt(1, this.accId);
            ResultSet rs2 = ps2.executeQuery();
            if (!rs2.next()) {
                ps2.close();
                rs2.close();
            }
            String email = rs2.getString("email");
            String macs = rs2.getString("macs");
            ps2.close();
            rs2.close();
            String data = "캐릭터명 : " + name + " (AccountID : " + this.accId + ", AccountName : " + getAccountName() + ") 이 삭제됨.\r\n";
            data = data + "EMAIL : " + email + ", MACS : " + macs + "\r\n\r\n";
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM characters WHERE id = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM hiredmerch WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mountdata WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitems WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitemsuse WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitemssetup WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitemsetc WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitemscash WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitemscody WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM famelog WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM famelog WHERE characterid_to = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM dueypackages WHERE RecieverId = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM wishlist WHERE characterid = ?", cid);
            ps = con.prepareStatement("DELETE FROM buddies WHERE repname = ?");
            ps.setString(1, name);
            ps.executeUpdate();
            ps.close();
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM keymap WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM trocklocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM regrocklocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM hyperrocklocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM savedlocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM skills WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mountdata WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM skillmacros WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM trocklocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM questinfo WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM queststatus WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryslot WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM extendedSlots WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM `unions` WHERE id = ?", cid);

            con.close();
            return 0;
        } catch (Exception e) {
            FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e);
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return 10;
    }

    public final byte getGender() {
        return this.gender;
    }

    public final void setGender(byte gender) {
        this.gender = gender;
    }

    public final String getSecondPassword() {
        return this.secondPassword;
    }

    public final void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }

    public final String getAccountName() {
        return this.accountName;
    }

    public final void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public final String getPassword() {
        return this.pwd;
    }

    public final void setPassword(String pwd) {
        this.pwd = pwd;
    }

    public final void setChannel(int channel) {
        this.channel = channel;
    }

    public final int getWorld() {
        return this.world;
    }

    public final void setWorld(int world) {
        this.world = world;
    }

    public final int getLatency() {
        return (int) (this.lastPong - this.lastPing);
    }

    public final long getLastPong() {
        return this.lastPong;
    }

    public final long getLastPing() {
        return this.lastPing;
    }

    public final void pongReceived() {
        this.lastPong = System.currentTimeMillis();
    }

    public final void sendPing() {
        lastPing = System.currentTimeMillis();
        session.writeAndFlush(LoginPacket.getPing());

        Timer.PingTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                try {
                    if (getLatency() < 0) {
                        disconnect(true, false);
                        getSession().close();
                    }
                } catch (final NullPointerException e) {
                }
            }
        }, 60000);
    }

    public static final String getLogMessage(MapleClient cfor, String message) {
        return getLogMessage(cfor, message, new Object[0]);
    }

    public static final String getLogMessage(MapleCharacter cfor, String message) {
        return getLogMessage((cfor == null) ? null : cfor.getClient(), message);
    }

    public static final String getLogMessage(MapleCharacter cfor, String message, Object... parms) {
        return getLogMessage((cfor == null) ? null : cfor.getClient(), message, parms);
    }

    public static final String getLogMessage(MapleClient cfor, String message, Object... parms) {
        StringBuilder builder = new StringBuilder();
        if (cfor != null) {
            if (cfor.getPlayer() != null) {
                builder.append("<");
                builder.append(MapleCharacterUtil.makeMapleReadable(cfor.getPlayer().getName()));
                builder.append(" (cid: ");
                builder.append(cfor.getPlayer().getId());
                builder.append(")> ");
            }
            if (cfor.getAccountName() != null) {
                builder.append("(Account: ");
                builder.append(cfor.getAccountName());
                builder.append(") ");
            }
        }
        builder.append(message);
        for (Object parm : parms) {
            int start = builder.indexOf("{}");
            builder.replace(start, start + 2, parm.toString());
        }
        return builder.toString();
    }

    public static final int findAccIdForCharacterName(String charName) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            ps.setString(1, charName);
            rs = ps.executeQuery();
            int ret = -1;
            if (rs.next()) {
                ret = rs.getInt("accountid");
            }
            rs.close();
            ps.close();
            con.close();
            return ret;
        } catch (SQLException e) {
            System.err.println("findAccIdForCharacterName SQL error");
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return -1;
    }

    public final Set<String> getMacs() {
        return Collections.unmodifiableSet(this.macs);
    }

    public final boolean isGm() {
        return this.gm;
    }

    public final void setScriptEngine(String name, ScriptEngine e) {
        this.engines.put(name, e);
    }

    public final ScriptEngine getScriptEngine(String name) {
        return this.engines.get(name);
    }

    public final void removeScriptEngine(String name) {
        this.engines.remove(name);
    }

    public final ScheduledFuture<?> getIdleTask() {
        return this.idleTask;
    }

    public final void setIdleTask(ScheduledFuture<?> idleTask) {
        this.idleTask = idleTask;
    }

    public boolean isFirstLogin() {
        return firstlogin;
    }

    public void setLogin(boolean a) {
        firstlogin = a;
    }
    
    protected static final class CharNameAndId {

        public final String name;

        public final int id;

        public CharNameAndId(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    public int getCharacterSlots() {// 재 코딩
        if (this.charslots != 30) {
            return this.charslots;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM character_slots WHERE accid = ? AND worldid = ?");
            ps.setInt(1, this.accId);
            ps.setInt(2, this.world);
            rs = ps.executeQuery();
            if (rs.next()) {
                this.charslots = rs.getInt("charslots");
            } else {
                PreparedStatement psu = con.prepareStatement("INSERT INTO character_slots (accid, worldid, charslots) VALUES (?, ?, ?)");
                psu.setInt(1, this.accId);
                psu.setInt(2, this.world);
                psu.setInt(3, this.charslots);
                psu.executeUpdate();
                psu.close();
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return this.charslots;
    }

    public boolean gainCharacterSlot() {
        if (this.getCharacterSlots() > 48) {
            return false;
        }
        this.charslots++;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE character_slots SET charslots = ? WHERE worldid = ? AND accid = ?");
            ps.setInt(1, this.charslots);
            ps.setInt(2, this.world);
            ps.setInt(3, this.accId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            return false;
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return true;
    }

    public static final byte unbanIPMacs(String charname) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psa = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT accountid from characters where name = ?");
            ps.setString(1, charname);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            int accid = rs.getInt(1);
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, accid);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            String sessionIP = rs.getString("sessionIP");
            String macs = rs.getString("macs");
            rs.close();
            ps.close();
            byte ret = 0;
            if (sessionIP != null) {
                psa = con.prepareStatement("DELETE FROM ipbans WHERE ip like ?");
                psa.setString(1, sessionIP);
                psa.execute();
                psa.close();
                ret = (byte) (ret + 1);
            }
            if (macs != null) {
                String[] macz = macs.split(", ");
                for (String mac : macz) {
                    if (!mac.equals("")) {
                        psa = con.prepareStatement("DELETE FROM macbans WHERE mac = ?");
                        psa.setString(1, mac);
                        psa.execute();
                        psa.close();
                    }
                }
                ret = (byte) (ret + 1);
            }
            con.close();
            return ret;
        } catch (SQLException e) {
            System.err.println("Error while unbanning" + e);
            return -2;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (psa != null) {
                    psa.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static final byte unHellban(String charname) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT accountid from characters where name = ?");
            ps.setString(1, charname);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            int accid = rs.getInt(1);
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, accid);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            String sessionIP = rs.getString("sessionIP");
            String email = rs.getString("email");
            rs.close();
            ps.close();
            ps = con.prepareStatement("UPDATE accounts SET banned = 0, banreason = '' WHERE email = ?" + ((sessionIP == null) ? "" : " OR sessionIP = ?"));
            ps.setString(1, email);
            if (sessionIP != null) {
                ps.setString(2, sessionIP);
            }
            ps.execute();
            ps.close();
            con.close();
            return 0;
        } catch (SQLException e) {
            System.err.println("Error while unbanning" + e);
            return -2;
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public boolean isMonitored() {
        return this.monitored;
    }

    public void setMonitored(boolean m) {
        this.monitored = m;
    }

    public boolean isReceiving() {
        return this.receiving;
    }

    public void setReceiving(boolean m) {
        this.receiving = m;
    }

    public boolean canClickNPC() {
        return (this.lastNpcClick + 500L < System.currentTimeMillis());
    }

    public void setClickedNPC() {
        this.lastNpcClick = System.currentTimeMillis();
    }

    public void removeClickedNPC() {
        this.lastNpcClick = 0L;
    }

    public final Timestamp getCreated() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT createdat FROM accounts WHERE id = ?");
            ps.setInt(1, getAccID());
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return null;
            }
            Timestamp ret = rs.getTimestamp("createdat");
            rs.close();
            ps.close();
            con.close();
            return ret;
        } catch (SQLException e) {
            throw new DatabaseException("error getting create", e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public String getTempIP() {
        return this.tempIP;
    }

    public void setTempIP(String s) {
        this.tempIP = s;
    }

    public final byte getNameChangeEnable() {
        return this.nameChangeEnable;
    }

    public final void setNameChangeEnable(byte nickName) {
        this.nameChangeEnable = nickName;
    }

    public boolean isLocalhost() {
        return ServerConstants.Use_Localhost;
    }

    public boolean isAuction() {
        return this.auction;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    public int getPoint() {
        return this.point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void order(LittleEndianAccessor slea) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int accId = slea.readInt();
            if (accId != this.accId) {
                this.session.close();
            }
            slea.readByte();
            int size = slea.readInt();
            for (int i = 0; i < size; i++) {
                int id = slea.readInt();
                try {
                    con = DatabaseConnection.getConnection();
                    ps = con.prepareStatement("UPDATE characters SET `order` = ? WHERE `id` = ?");
                    ps.setInt(1, i);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception exception) {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public Map<String, String> getKeyValues() {
        return this.keyValues;
    }

    public void setKeyValues(Map<String, String> keyValues) {
        this.keyValues = keyValues;
    }

    public void saveKeyValueToDB(Connection con) {
        try {
            if (this.keyValues != null) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM acckeyvalue WHERE `id` = ?");
                ps.setInt(1, this.accId);
                ps.executeUpdate();
                ps.close();
                PreparedStatement pse = con.prepareStatement("INSERT INTO acckeyvalue (`id`, `key`, `value`) VALUES (?, ?, ?)");
                pse.setInt(1, this.accId);
                for (Map.Entry<String, String> keyValue : this.keyValues.entrySet()) {
                    pse.setString(2, keyValue.getKey());
                    pse.setString(3, keyValue.getValue());
                    pse.execute();
                }
                pse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCustomDataToDB(Connection con) {
        PreparedStatement ps = null;
        PreparedStatement pse = null;
        try {
            if (this.customDatas != null) {
                ps = con.prepareStatement("DELETE FROM account_customdata WHERE `accid` = ?");
                ps.setInt(1, this.accId);
                ps.executeUpdate();
                ps.close();
                pse = con.prepareStatement("INSERT INTO account_customdata (`accid`, `id`, `key`, `value`) VALUES (?, ?, ?, ?)");
                pse.setInt(1, this.accId);
                for (Map.Entry<Integer, List<Pair<String, String>>> customData : this.customDatas.entrySet()) {
                    pse.setInt(2, ((Integer) customData.getKey()).intValue());
                    for (Pair<String, String> data : customData.getValue()) {
                        pse.setString(3, (String) data.left);
                        pse.setString(4, (String) data.right);
                        pse.execute();
                    }
                }
                pse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (pse != null) {
                    pse.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public void saveCustomKeyValueToDB(Connection con) {
        PreparedStatement ps = null;
        PreparedStatement pse = null;
        try {
            if (this.customKeyValue != null) {
                ps = con.prepareStatement("DELETE FROM acc_questinfo WHERE `accid` = ?");
                ps.setInt(1, this.accId);
                ps.executeUpdate();
                ps.close();
                pse = con.prepareStatement("INSERT INTO acc_questinfo (`accid`, `quest`, `key`) VALUES (?, ?, ?)");
                pse.setInt(1, this.accId);
                for (Map.Entry<Integer, String> q : this.customKeyValue.entrySet()) {
                    pse.setInt(2, ((Integer) q.getKey()).intValue());
                    pse.setString(3, q.getValue());
                    pse.execute();
                }
                pse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (pse != null) {
                    pse.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public String getKeyValue(String key) {
        if (this.keyValues.containsKey(key)) {
            return this.keyValues.get(key);
        }
        return null;
    }

    public void setKeyValue(String key, String value) {
        this.keyValues.put(key, value);
    }

    public void removeKeyValue(String key) {
        this.keyValues.remove(key);
    }

    public Map<Integer, List<Pair<String, String>>> getCustomDatas() {
        return this.customDatas;
    }

    public String getCustomData(int id, String key) {
        if (this.customDatas.containsKey(Integer.valueOf(id))) {
            for (Pair<String, String> datas : this.customDatas.get(Integer.valueOf(id))) {
                if (((String) datas.left).equals(key)) {
                    return (String) datas.right;
                }
            }
        }
        return null;
    }

    public void setCustomData(int id, String key, String value) {
        if (this.customDatas.containsKey(Integer.valueOf(id))) {
            for (Pair<String, String> datas : this.customDatas.get(Integer.valueOf(id))) {
                if (((String) datas.getLeft()).equals(key)) {
                    datas.right = value;
                    this.session.writeAndFlush(CWvsContext.InfoPacket.updateClientInfoQuest(id, key + "=" + value));
                    this.session.writeAndFlush(CWvsContext.InfoPacket.updateInfoQuest(id, key + "=" + value));
                    return;
                }
            }
            ((List) this.customDatas.get(Integer.valueOf(id))).add(new Pair<>(key, value));
        } else {
            List<Pair<String, String>> datas = new ArrayList<>();
            datas.add(new Pair<>(key, value));
            this.customDatas.put(Integer.valueOf(id), datas);
        }
        this.session.writeAndFlush(CWvsContext.InfoPacket.updateClientInfoQuest(id, key + "=" + value));
        this.session.writeAndFlush(CWvsContext.InfoPacket.updateInfoQuest(id, key + "=" + value));
    }

    public long getChatBlockedTime() {
        return this.chatBlockedTime;
    }

    public void setChatBlockedTime(long chatBlockedTime) {
        this.chatBlockedTime = chatBlockedTime;
    }

    public boolean isFarm() {
        return this.farm;
    }

    public void setFarm(boolean farm) {
        this.farm = farm;
    }

    public boolean isCashShop() {
        return this.cashShop;
    }

    public void setCashShop(boolean shop) {
        this.cashShop = shop;
    }

    public byte[] getFarmImg() {
        byte[] farmImg = new byte[0];
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
            ps.setString(1, this.accountName);
            rs = ps.executeQuery();
            if (rs.next()) {
                Blob img = rs.getBlob("farmImg");
                if (img != null) {
                    farmImg = img.getBytes(1L, (int) img.length());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return farmImg;
    }

    public final void setFarmImg(byte[] farmImg) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Blob blob = null;
            if (farmImg != null) {
                blob = new SerialBlob(farmImg);
            }
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET farmImg = ? WHERE `id` = ?");
            ps.setBlob(1, blob);
            ps.setInt(2, this.accId);
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public final boolean isAllowedClient() {
        if (this.accountName == null) {
            return false;
        }
        return false;
    }

    public int allChargePoint() {
        try {
            return Integer.parseInt(getCustomData(5, "amount"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int chargePoint() {
        int charge = 0;
        int month = CurrentTime.getMonth() + 1;
        int year = CurrentTime.getYear();
        for (int i = 0; i < 3; i++) {
            month--;
            if (month <= 0) {
                month += 12;
                year--;
            }
            String str = String.valueOf(year) + StringUtil.getLeftPaddedStr(String.valueOf(month), '0', 2);
            String chargeMonth = getCustomData(4, str);
            if (chargeMonth != null) {
                charge += Integer.parseInt(chargeMonth);
            }
        }
        return charge;
    }

    public int getMVPGrade() {
        int totalAmount = allChargePoint(), lastThreeMonth = chargePoint();
        if (lastThreeMonth >= 1500000) {
            return 8;
        }
        if (lastThreeMonth >= 900000) {
            return 7;
        }
        if (lastThreeMonth >= 600000) {
            return 6;
        }
        if (lastThreeMonth >= 300000) {
            return 5;
        }
        if (totalAmount >= 300000) {
            return 4;
        }
        if (totalAmount >= 200000) {
            return 3;
        }
        if (totalAmount < 100000) {
            return (totalAmount >= 10000) ? 1 : 0;
        }
        return 2;
    }

    public void send(byte[] p) {
        getSession().writeAndFlush(p);
    }

    public long getDiscordId() {
        return this.discordid;
    }

    public int getSecondPw() {
        return this.SecondPwUse;
    }

    public void setSecondPw(int SecondPwUse) {
        this.SecondPwUse = SecondPwUse;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET SecondPwUse = ? WHERE id = ?");
            ps.setInt(1, this.SecondPwUse);
            ps.setInt(2, getAccID());
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            throw new DatabaseException("error", e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void setCheckOTP(boolean a) {
        this.sendOTP = a;
    }

    public boolean getCheckOTP() {
        return this.sendOTP;
    }

    public int getSaveOTPNum() {
        return this.saveOTPNum;
    }

    public void setSaveOTPNum(int saveOTPNum) {
        this.saveOTPNum = saveOTPNum;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET saveOTPNum = ? WHERE id = ?");
            ps.setInt(1, this.saveOTPNum);
            ps.setInt(2, getAccID());
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            throw new DatabaseException("error", e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public String getEmail() {
        String email = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
            ps.setString(1, this.accountName);
            rs = ps.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return email;
    }

    public String getLIEDETECT() {
        return this.LIEDETECT;
    }

    public void setLIEDETECT(String LIEDETECT) {
        this.LIEDETECT = LIEDETECT;
    }

    public int getLieDectctCount() {
        return this.lieDectctCount;
    }

    public void setLieDectctCount(int lieDectctCount) {
        this.lieDectctCount = lieDectctCount;
    }


    public MapleCharacter getRandomCharacter() {
        MapleCharacter chr = null;
        List<MapleCharacter> players = new ArrayList<>();
        if (getPlayer().getMap().getCharacters().size() > 0) {
            players.addAll(getPlayer().getMap().getCharacters());
            Collections.addAll(players, new MapleCharacter[0]);
            Collections.shuffle(players);
            for (MapleCharacter chr3 : players) {
                if (chr3.isAlive()) {
                    chr = chr3;
                    break;
                }
            }
        } else {
            return null;
        }
        return chr;
    }

    public void loadCabinet() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM cabinet WHERE accountid = ?");
            ps.setInt(1, this.accId);
            rs = ps.executeQuery();
            while (rs.next()) {
                MapleCabinet cb = new MapleCabinet(rs.getInt("accountid"), rs.getInt("itemid"), rs.getInt("count"), rs.getString("bigname"), rs.getString("smallname"), rs.getLong("savetime"), rs.getInt("delete"));
                cb.setPlayerid(rs.getInt("playerid"));
                cb.setName(rs.getString("name"));
                getCabiNet().add(cb);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public List<MapleCabinet> getCabiNet() {
        return this.cabinet;
    }

    public void setCabiNet(List<MapleCabinet> cabinet) {
        this.cabinet = cabinet;
    }

    public void saveCabiNet(List<MapleCabinet> cabinet) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            if (getPlayer() == null) {
                return;
            }
            if (cabinet.isEmpty()) {
                MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM `cabinet` WHERE `accountid` = ?", this.accId);
                return;
            }
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM `cabinet` WHERE `accountid` = ?", this.accId);
            ps = null;
            for (MapleCabinet cn : cabinet) {
                if (cn == null) {
                    continue;
                }
                ps = con.prepareStatement("INSERT INTO `cabinet` (`accountid`, `itemid`, `count`, `bigname`, `smallname`, `savetime`, `delete`, `playerid`, `name`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setLong(1, this.accId);
                ps.setInt(2, cn.getItemid());
                ps.setInt(3, cn.getCount());
                ps.setString(4, cn.getBigname());
                ps.setString(5, cn.getSmallname());
                ps.setLong(6, cn.getSaveTime());
                ps.setInt(7, cn.getDelete());
                ps.setInt(8, cn.getPlayerid());
                ps.setString(9, cn.getName());
                ps.execute();
                ps.close();
            }
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public void loadShopLimit() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM shopitemlimit WHERE accountid = ?");
            ps.setInt(1, this.accId);
            rs = ps.executeQuery();
            while (rs.next()) {
                MapleShopLimit shops = new MapleShopLimit(rs.getInt("accountid"), rs.getInt("charid"), rs.getInt("shopid"), rs.getInt("itemid"), rs.getInt("position"), rs.getInt("limitcountacc"), rs.getInt("limitcountchr"), rs.getInt("lastbuymonth"), rs.getInt("lastbuyday"));
                getShopLimit().add(shops);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public List<MapleShopLimit> getShopLimit() {
        return this.shops;
    }

    public void setShopLimit(List<MapleShopLimit> shops) {
        this.shops = shops;
    }

    public void saveShopLimit(List<MapleShopLimit> shoplimit) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            if (getPlayer() == null) {
                return;
            }
            if (this.shops.isEmpty()) {
                return;
            }
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM `shopitemlimit` WHERE `accountid` = ?", this.accId);
            ps = null;
            for (MapleShopLimit sl : shoplimit) {
                if (sl == null) {
                    continue;
                }
                ps = con.prepareStatement("INSERT INTO `shopitemlimit` (`accountid`, `charid`, `shopid`, `itemid`, `position`, `limitcountacc`, `limitcountchr`, `lastbuymonth`, `lastbuyday`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setLong(1, this.accId);
                ps.setInt(2, sl.getCharId());
                ps.setInt(3, sl.getShopId());
                ps.setInt(4, sl.getItemid());
                ps.setInt(5, sl.getPosition());
                ps.setInt(6, sl.getLimitCountAcc());
                ps.setInt(7, sl.getLimitCountChr());
                ps.setInt(8, sl.getLastBuyMonth());
                ps.setInt(9, sl.getLastBuyDay());
                ps.execute();
                ps.close();
            }
            con.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public String getSaveOTPDay() {
        return this.saveOTPDay;
    }

    public void setSaveOTPDay(String saveOTPDay) {
        this.saveOTPDay = saveOTPDay;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("UPDATE accounts SET saveOTPDay = ? WHERE id = ?");
            ps.setString(1, this.saveOTPDay);
            ps.setInt(2, getAccID());
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            throw new DatabaseException("error", e);
        } finally {
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
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void setCharCreatetime(long time) {
        this.createchartime = time;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs1 = null;
        try {
            con = DatabaseConnection.getConnection();
            ps2 = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps2.setInt(1, getAccID());
            rs1 = ps2.executeQuery();
            ps = con.prepareStatement("UPDATE accounts SET createTime = ? WHERE id = ?");
            ps.setLong(1, this.createchartime);
            ps.setInt(2, getAccID());
            ps.executeUpdate();
            ps.close();
            rs1.close();
            ps2.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public long getCharCreatetime() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, getAccID());
            rs = ps.executeQuery();
            if (rs.next()) {
                this.createchartime = rs.getLong("createTime");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }
        return this.createchartime;
    }

    public boolean isFirstlogin() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, getAccID());
            rs = ps.executeQuery();
            if (rs.next()) {
                this.firstlogin = (rs.getInt("FirstLogin") > 0);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception exception) {
            }
        }
        return this.firstlogin;
    }

    public void setFirstlogin(boolean firstlogin) {
        this.firstlogin = firstlogin;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs1 = null;
        try {
            con = DatabaseConnection.getConnection();
            ps2 = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps2.setInt(1, getAccID());
            rs1 = ps2.executeQuery();
            ps = con.prepareStatement("UPDATE accounts SET FirstLogin = ? WHERE id = ?");
            ps.setInt(1, firstlogin ? 1 : 0);
            ps.setInt(2, getAccID());
            ps.executeUpdate();
            ps.close();
            rs1.close();
            ps2.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public long getCustomKeyValue(int type, String key) {
        String questInfo = getInfoQuest(type);
        if (questInfo == null) {
            return -1L;
        }
        String[] data = questInfo.split(";");
        for (String s : data) {
            if (s.startsWith(key + "=")) {
                String newkey = s.replace(key + "=", "");
                String newkey2 = newkey.replace(";", "");
                long dd = Long.valueOf(newkey2).longValue();
                return dd;
            }
        }
        return -1L;
    }

    public String getCustomKeyValueStr(int type, String key) {
        String questInfo = getInfoQuest(type);
        if (questInfo == null) {
            return null;
        }
        String[] data = questInfo.split(";");
        for (String s : data) {
            if (s.startsWith(key + "=")) {
                String newkey = s.replace(key + "=", "");
                String newkey2 = newkey.replace(";", "");
                return newkey2;
            }
        }
        return null;
    }

    public void setCustomKeyValue(int id, String key, String value) {
        String questInfo = getInfoQuest(id);
        if (questInfo == null) {
            this.customKeyValue.put(Integer.valueOf(id), key + "=" + value + ";");
            getSession().writeAndFlush(CWvsContext.InfoPacket.updateInfoQuest(id, key + "=" + value + ";"));
            return;
        }
        String[] data = questInfo.split(";");
        for (String s : data) {
            if (s.startsWith(key + "=")) {
                String newkey = questInfo.replace(s, key + "=" + value);
                this.customKeyValue.put(Integer.valueOf(id), newkey);
                getSession().writeAndFlush(CWvsContext.InfoPacket.updateInfoQuest(id, newkey));
                return;
            }
        }
        this.customKeyValue.put(Integer.valueOf(id), questInfo + key + "=" + value + ";");
        getSession().writeAndFlush(CWvsContext.InfoPacket.updateInfoQuest(id, questInfo + key + "=" + value + ";"));
    }

    public void removeCustomKeyValue(int type) {
        MapleQuest quest = MapleQuest.getInstance(type);
        if (quest == null) {
            return;
        }
        send(CWvsContext.InfoPacket.updateInfoQuest(type, ""));
        this.customKeyValue.remove(Integer.valueOf(type));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("DELETE FROM acc_questinfo WHERE accid = " + this.accId + " AND quest = ?");
            ps.setInt(1, type);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception exception) {
            }
        }
    }

    public final List<MapleQuestStatus> getStartedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<>();
        for (MapleQuestStatus q : this.quests.values()) {
            if (q.getStatus() == 1 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<MapleQuestStatus> getCompletedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<>();
        for (MapleQuestStatus q : this.quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final void updateQuest(MapleQuestStatus quest, boolean update) {
        this.quests.put(quest.getQuest(), quest);
        if (update) {
            getSession().writeAndFlush(CWvsContext.InfoPacket.updateQuest(quest));
        }
    }

    public final byte getQuestStatus(int quest) {
        MapleQuest qq = MapleQuest.getInstance(quest);
        if (getQuestNoAdd(qq) == null) {
            return 0;
        }
        return getQuestNoAdd(qq).getStatus();
    }

    public final MapleQuestStatus getQuestNoAdd(MapleQuest quest) {
        if (!this.quests.containsKey(quest)) {
            return null;
        }
        return this.quests.get(quest);
    }

    public final MapleQuestStatus getQuest(MapleQuest quest) {
        if (!this.quests.containsKey(quest)) {
            return new MapleQuestStatus(quest, 0);
        }
        return this.quests.get(quest);
    }

    public Map<MapleQuest, MapleQuestStatus> getQuests() {
        return this.quests;
    }

    public final String getInfoQuest(int questid) {
        if (this.customKeyValue.containsKey(Integer.valueOf(questid))) {
            return this.customKeyValue.get(Integer.valueOf(questid));
        }
        return "";
    }

    public Map<Integer, String> getCustomKeyValue() {
        return this.customKeyValue;
    }
}
