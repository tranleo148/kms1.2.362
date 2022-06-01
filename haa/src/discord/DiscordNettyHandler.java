package discord;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import tools.data.LittleEndianAccessor;

public class DiscordNettyHandler extends SimpleChannelInboundHandler<LittleEndianAccessor> {
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    String IP = ctx.channel().remoteAddress().toString().split(":")[0];
    System.out.println("디스코드 봇 " + IP + " 가 접속했습니다.");
    DiscordClient cli = new DiscordClient(ctx.channel());
    if (DiscordServer.getInstance().getClientStorage().getClient(cli.getAddressIP()) != null)
      DiscordServer.getInstance().getClientStorage().removeClient(cli.getAddressIP()); 
    DiscordServer.getInstance().getClientStorage().registerClient(cli, cli.getAddressIP());
    ctx.flush();
    ctx.channel().attr(DiscordClient.CLIENTKEY).set(cli);
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    DiscordClient client = (DiscordClient)ctx.channel().attr(DiscordClient.CLIENTKEY).get();
    if (client != null)
      DiscordServer.getInstance().getClientStorage().deregisterClient(client); 
    ctx.channel().attr(DiscordClient.CLIENTKEY).set(null);
  }
  
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) {
    ex.printStackTrace();
  }
  
  public byte[] intToByteArray(int value) {
    byte[] byteArray = new byte[4];
    byteArray[0] = (byte)(value >> 24);
    byteArray[1] = (byte)(value >> 16);
    byteArray[2] = (byte)(value >> 8);
    byteArray[3] = (byte)value;
    return byteArray;
  }
  
  protected void channelRead0(ChannelHandlerContext ctx, LittleEndianAccessor slea) throws Exception {
    DiscordClient client = (DiscordClient)ctx.channel().attr(DiscordClient.CLIENTKEY).get();
    byte header = slea.readByte();
    for (RecvPacketOpcode recv : RecvPacketOpcode.values()) {
      if (recv.getValue() == header)
        DiscordServerHandler.HandlePacket(recv, slea, client); 
    } 
  }
}
