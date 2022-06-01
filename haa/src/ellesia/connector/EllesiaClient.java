package ellesia.connector;

import client.MapleCharacter;
import client.MapleClient;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EllesiaClient {

    public final static AttributeKey<EllesiaClient> CLIENTKEY = AttributeKey.valueOf("ellesiaclient");
    private Channel session;

    private final transient Lock mutex = new ReentrantLock(true);
    private String id = "";
    private int crcfailed = 0;
    private long lastping = System.currentTimeMillis();

    public EllesiaClient(Channel socket) {
        this.session = socket;
    }
    public final Lock getLock() {
        return mutex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void CRCFailed() {
        crcfailed++;
    }
    public void clearCRCFailed() {
        crcfailed=0;
    }
    public int getCRCFailed() {
        return crcfailed;
    }

    public void Ping() {
        lastping = System.currentTimeMillis();
    }

    public long getLastPing() {
        return lastping;
    }

    public void closeSession() {
        for(ChannelServer ch : ChannelServer.getAllInstances()) {
            for(MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
                MapleClient c = chr.getClient();
                if(c.getSessionIPAddress().equalsIgnoreCase(getIP())) {
                    c.getSession().close();
                }
            }
        }
        if(LoginServer.Channels.containsKey(getIP()))
            LoginServer.Channels.get(getIP()).close();



        for(MapleCharacter chr : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
            MapleClient c = chr.getClient();
            if(c.getSessionIPAddress().equalsIgnoreCase(getIP())) {
                c.getSession().close();
            }
        }

        for(MapleCharacter chr : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
            MapleClient c = chr.getClient();
            if(c.getSessionIPAddress().equalsIgnoreCase(getIP())) {
                c.getSession().close();
            }
        }

        for(MapleCharacter chr : FarmServer.getPlayerStorage().getAllCharacters().values()) {
            MapleClient c = chr.getClient();
            if(c.getSessionIPAddress().equalsIgnoreCase(getIP())) {
                c.getSession().close();
            }
        }

        LoginServer.Channels.remove(getIP());

        EllesiaWalker.setAlive(getId(), false);
        getSession().close();
    }

    public void send(byte[] p) {
        getSession().writeAndFlush(p);
    }

    public void sendPacket(byte[] data) {
        if (data == null) {
            return;
        }
        byte[] temp = new byte[data.length];
        System.arraycopy(data, 0, temp, 0, data.length);
        session.writeAndFlush(temp);
    }

    public final Channel getSession() {
        return session;
    }

    public String getIP() {
        return session.remoteAddress().toString().split(":")[0];
    }

    public String getAddressIP() {
        return session.remoteAddress().toString();
    }
}
