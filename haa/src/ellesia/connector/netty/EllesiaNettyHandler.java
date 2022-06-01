package ellesia.connector.netty;

import client.MapleClient;
import ellesia.connector.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import tools.data.LittleEndianAccessor;

public class EllesiaNettyHandler extends SimpleChannelInboundHandler<LittleEndianAccessor> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final String IP = ctx.channel().remoteAddress().toString().split(":")[0];
        System.out.println(IP + "에서 접속기 서버에 접속했습니다.");
        final EllesiaClient cli = new EllesiaClient(ctx.channel());
        cli.sendPacket(EllesiaPacketCreator.Version(EllesiaSettings.ConnectorVersion));
        EllesiaConnectorServer.getInstance().registerClient(cli.getIP(), cli);
        ctx.channel().attr(EllesiaClient.CLIENTKEY).set(cli);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        EllesiaClient cli = ctx.channel().attr(EllesiaClient.CLIENTKEY).get();
        final String IP = ctx.channel().remoteAddress().toString().split(":")[0];
        cli.closeSession();
        EllesiaConnectorServer.getInstance().unregisterClient(cli.getIP());
        if(!cli.getId().equalsIgnoreCase("")) { //logged in
            EllesiaWalker.setAlive(cli.getId(), false);
        }
        ctx.channel().attr(EllesiaClient.CLIENTKEY).set(null);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            channelInactive(ctx);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LittleEndianAccessor slea) throws Exception {
        final EllesiaClient client = ctx.channel().attr(EllesiaClient.CLIENTKEY).get();
        final short header = slea.readShort();
        for (final EllesiaPacketOpcode recv : EllesiaPacketOpcode.values()) {
            if (recv.getValue() == header) {
                EllesiaConnectorHandler.HandlePacket(recv, slea, client);
                break;
            }
        }
        return;
    }
}
