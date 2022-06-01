package handling.login;

import constants.ServerType;
import handling.netty.MapleNettyDecoder;
import handling.netty.MapleNettyEncoder;
import handling.netty.MapleNettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferAllocator;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import server.ServerProperties;
import tools.Pair;

public class LoginServer {
  public static final int PORT = Integer.parseInt(ServerProperties.getProperty("ports.login"));
  
  private static InetSocketAddress InetSocketadd;
  
  private static IoAcceptor acceptor;
  
  private static Map<Integer, Integer> load = new HashMap<>();
  
  private static String serverName;
  
  private static String eventMessage;
  
  private static byte flag;
  
  private static int maxCharacters;
  
  private static int userLimit;
  
  private static int usersOn = 0;
  
  private static boolean finishedShutdown = true;
  
  private static boolean adminOnly = false;
  
  private static HashMap<Integer, Pair<String, String>> loginAuth = new HashMap<>();
  
  private static HashSet<String> loginIPAuth = new HashSet<>();
  
  private static ServerBootstrap bootstrap;

  public static HashMap<String, Channel> Channels = new HashMap<String, Channel>();  
  public static void putLoginAuth(int chrid, String ip, String tempIP) {
    loginAuth.put(Integer.valueOf(chrid), new Pair<>(ip, tempIP));
    loginIPAuth.add(ip);
  }
  
  public static Pair<String, String> getLoginAuth(int chrid) {
    return loginAuth.remove(Integer.valueOf(chrid));
  }
  
  public static boolean containsIPAuth(String ip) {
    return loginIPAuth.contains(ip);
  }
  
  public static void removeIPAuth(String ip) {
    loginIPAuth.remove(ip);
  }
  
  public static void addIPAuth(String ip) {
    loginIPAuth.add(ip);
  }
  
  public static final void addChannel(int channel) {
    load.put(Integer.valueOf(channel), Integer.valueOf(0));
  }
  
  public static final void removeChannel(int channel) {
    load.remove(Integer.valueOf(channel));
  }
  
  public static final void run_startup_configurations() {
    userLimit = Integer.parseInt(ServerProperties.getProperty("login.userlimit"));
    serverName = ServerProperties.getProperty("login.serverName");
    eventMessage = ServerProperties.getProperty("login.eventMessage");
    flag = Byte.parseByte(ServerProperties.getProperty("login.flag"));
    adminOnly = Boolean.parseBoolean(ServerProperties.getProperty("world.admin", "false"));
    maxCharacters = Integer.parseInt(ServerProperties.getProperty("login.maxCharacters"));
    IoBuffer.setUseDirectBuffer(false);
    IoBuffer.setAllocator((IoBufferAllocator)new SimpleBufferAllocator());
    NioEventLoopGroup nioEventLoopGroup1 = new NioEventLoopGroup();
    NioEventLoopGroup nioEventLoopGroup2 = new NioEventLoopGroup();
    try {
      bootstrap = new ServerBootstrap();
      ((ServerBootstrap)((ServerBootstrap)bootstrap.group((EventLoopGroup)nioEventLoopGroup1, (EventLoopGroup)nioEventLoopGroup2)
        .channel(NioServerSocketChannel.class))
        .childHandler((ChannelHandler)new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast("decoder", (ChannelHandler)new MapleNettyDecoder());
              ch.pipeline().addLast("encoder", (ChannelHandler)new MapleNettyEncoder());
              ch.pipeline().addLast("idleStateHandler", (ChannelHandler)new IdleStateHandler(60, 30, 0));
              ch.pipeline().addLast("handler", (ChannelHandler)new MapleNettyHandler(ServerType.LOGIN, -1));
            }
          }).option(ChannelOption.SO_BACKLOG, Integer.valueOf(128)))
        .childOption(ChannelOption.SO_SNDBUF, Integer.valueOf(4194304))
        .childOption(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true));
      ChannelFuture f = bootstrap.bind(PORT).sync();
      System.out.println("[알림] 로그인서버가 " + PORT + " 포트를 성공적으로 개방하였습니다.");
    } catch (InterruptedException e) {
      System.err.println("[오류] 로그인서버가 " + PORT + " 포트를 개방하는데 실패했습니다.");
      e.printStackTrace();
    } 
  }
  
  public static final void shutdown() {
    if (finishedShutdown)
      return; 
    System.out.println("Shutting down login...");
    finishedShutdown = true;
  }
  
  public static final String getServerName() {
    return serverName;
  }
  
  public static final String getEventMessage() {
    return eventMessage;
  }
  
  public static final byte getFlag() {
    return flag;
  }
  
  public static final int getMaxCharacters() {
    return maxCharacters;
  }
  
  public static final Map<Integer, Integer> getLoad() {
    return load;
  }
  
  public static void setLoad(Map<Integer, Integer> load_, int usersOn_) {
    load = load_;
    usersOn = usersOn_;
  }
  
  public static final void setEventMessage(String newMessage) {
    eventMessage = newMessage;
  }
  
  public static final void setFlag(byte newflag) {
    flag = newflag;
  }
  
  public static final int getUserLimit() {
    return userLimit;
  }
  
  public static final int getUsersOn() {
    return usersOn;
  }
  
  public static final void setUserLimit(int newLimit) {
    userLimit = newLimit;
  }
  
  public static final boolean isAdminOnly() {
    return adminOnly;
  }
  
  public static final boolean isShutdown() {
    return finishedShutdown;
  }
  
  public static final void setOn() {
    finishedShutdown = false;
  }
}
