package handling.netty;

import client.MapleClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import tools.MapleAESOFB;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class MapleNettyDecoder extends ByteToMessageDecoder {
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> list) throws Exception {
    MapleClient client = (MapleClient)ctx.channel().attr(MapleClient.CLIENTKEY).get();
    if (client == null)
      return; 
    if (buffer.readableBytes() < 4)
      return; 
    int packetHeader = buffer.readInt();
    int packetlength = MapleAESOFB.getPacketLength(packetHeader);
    if (!client.getReceiveCrypto().checkPacket(packetHeader))
      return; 
    if (buffer.readableBytes() < packetlength) {
      buffer.resetReaderIndex();
      return;
    } 
    buffer.markReaderIndex();
    byte[] decoded = new byte[packetlength];
    buffer.readBytes(decoded);
    buffer.markReaderIndex();
    list.add(new LittleEndianAccessor(new ByteArrayByteStream(client.getReceiveCrypto().crypt(decoded))));
  }
}
