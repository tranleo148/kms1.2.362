package discord;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiscordClient {
  public static final AttributeKey<DiscordClient> CLIENTKEY = AttributeKey.valueOf("discordclient_netty");
  
  private Channel session;
  
  private List<String> ingamechar = new ArrayList<>();
  
  private final transient Lock mutex = new ReentrantLock(true);
  
  public DiscordClient(Channel socket) {
    this.session = socket;
  }
  
  public void send(byte[] p) {
    getSession().writeAndFlush(p);
  }
  
  public void sendPacket(byte[] data) {
    if (data == null)
      return; 
    byte[] temp = new byte[data.length];
    System.arraycopy(data, 0, temp, 0, data.length);
    this.session.writeAndFlush(temp);
  }
  
  public final Channel getSession() {
    return this.session;
  }
  
  public String getIP() {
    return this.session.remoteAddress().toString().split(":")[0].split("/")[1];
  }
  
  public String getAddressIP() {
    return this.session.remoteAddress().toString().split("/")[1];
  }
  
  public final Lock getLock() {
    return this.mutex;
  }
}
