package ellesia.connector;

import ellesia.connector.netty.EllesiaNettyDecoder;
import ellesia.connector.netty.EllesiaNettyEncoder;
import ellesia.connector.netty.EllesiaNettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import server.Timer;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EllesiaConnectorServer {

    private static EllesiaConnectorServer instance = new EllesiaConnectorServer();
    private ServerBootstrap bootstrap;

    private final Map<String, EllesiaClient> clients = new ConcurrentHashMap<>();

    public EllesiaClient getClient(String ip) {
        return clients.get(ip);
    }

    public Collection<EllesiaClient> getClients() {
        return clients.values();
    }

    public void registerClient(String ip, EllesiaClient client) {
        clients.put(ip, client);
    }

    public void unregisterClient(String ip) {
        clients.remove(ip);
    }

    public static EllesiaConnectorServer getInstance() {
        return instance;
    }

    public void run_startup_configurations() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("decodeer", new EllesiaNettyDecoder());
                    ch.pipeline().addLast("encodeer", new EllesiaNettyEncoder());
                    ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(30, 5, 0));
                    ch.pipeline().addLast("handler", new EllesiaNettyHandler());
                    ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(0xFFFF));
                    ch.config().setOption(ChannelOption.SO_RCVBUF, 0xFFFF);
                }
            }).option(ChannelOption.SO_BACKLOG, 128).option(ChannelOption.SO_RCVBUF, 0xFFFF).option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(0xFFFF)).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture cf = bootstrap.bind(5466).sync();
            Timer.WorldTimer.getInstance().register(new EllesiaConnectorThread(), 1000);
            System.out.println("접속기 서버 개방 성공");
        } catch (InterruptedException ex) {
            System.err.println("관리기 서버 개방 실패\r\n" + ex);
        }
    }
}
