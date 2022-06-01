 package handling.cashshop;

 import constants.ServerType;
 import handling.channel.PlayerStorage;
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
 import server.ServerProperties;






















 public class CashShopServer
 {
   private static String ip;
/* 44 */   private static final int PORT = Integer.parseInt(ServerProperties.getProperty("ports.cashshop"));

   private static PlayerStorage players;
   private static boolean finishedShutdown = false;
   private static ServerBootstrap bootstrap;

   public static final void run_startup_configurations() {
/* 51 */     players = new PlayerStorage();
/* 52 */     ip = ServerProperties.getProperty("world.host") + ":" + PORT;

/* 54 */     NioEventLoopGroup nioEventLoopGroup1 = new NioEventLoopGroup();
/* 55 */     NioEventLoopGroup nioEventLoopGroup2 = new NioEventLoopGroup();

     try {
/* 58 */       bootstrap = new ServerBootstrap();
/* 59 */       ((ServerBootstrap)((ServerBootstrap)bootstrap.group((EventLoopGroup)nioEventLoopGroup1, (EventLoopGroup)nioEventLoopGroup2)
/* 60 */         .channel(NioServerSocketChannel.class))
/* 61 */         .childHandler((ChannelHandler)new ChannelInitializer<SocketChannel>()
           {
             public void initChannel(SocketChannel ch) throws Exception {
/* 64 */               ch.pipeline().addLast("decoder", (ChannelHandler)new MapleNettyDecoder());
/* 65 */               ch.pipeline().addLast("encoder", (ChannelHandler)new MapleNettyEncoder());
/* 66 */               ch.pipeline().addLast("handler", (ChannelHandler)new MapleNettyHandler(ServerType.CASHSHOP, -1));
             }
/* 69 */           }).option(ChannelOption.SO_BACKLOG, Integer.valueOf(128)))
/* 70 */         .childOption(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true));
/* 71 */       ChannelFuture f = bootstrap.bind(PORT).sync();
/* 72 */       System.out.println("[알림] 캐시샵서버가 " + PORT + " 포트를 성공적으로 개방하였습니다.");
/* 73 */     } catch (InterruptedException e) {
/* 74 */       System.err.println("[오류] 캐시샵서버가 " + PORT + " 포트를 개방하는데 실패했습니다.");
     }
   }


   public static final String getIP() {
/* 80 */     return ip;
   }

   public static final PlayerStorage getPlayerStorage() {
/* 84 */     return players;
   }

   public static final void shutdown() {
/* 88 */     if (finishedShutdown) {
       return;
     }
/* 91 */     System.out.println("Saving all connected clients (CS)...");
/* 92 */     players.disconnectAll();
/* 93 */     System.out.println("Shutting down CS...");
/* 94 */     finishedShutdown = true;
   }

   public static boolean isShutdown() {
/* 98 */     return finishedShutdown;
   }
 }


/* Location:              C:\Users\Phellos\Desktop\크루엘라\Ozoh디컴.jar!\handling\cashshop\CashShopServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */