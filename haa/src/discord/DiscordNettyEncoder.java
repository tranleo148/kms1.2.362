package discord;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.concurrent.locks.Lock;

public class DiscordNettyEncoder extends MessageToByteEncoder<byte[]> {
  protected void encode(ChannelHandlerContext ctx, byte[] pData, ByteBuf buffer) throws Exception {
    DiscordClient client = (DiscordClient)ctx.channel().attr(DiscordClient.CLIENTKEY).get();
    if (client != null) {
      Lock mutex = client.getLock();
      mutex.lock();
      try {
        int i = pData.length;
        byte[] a = { (byte)(i & 0xFF), (byte)(i >>> 8 & 0xFF), (byte)(i >>> 16 & 0xFF), (byte)(i >>> 24 & 0xFF) };
        buffer.writeBytes(a);
        buffer.writeBytes(pData);
      } finally {
        mutex.unlock();
      } 
    } 
    ctx.flush();
  }
}
