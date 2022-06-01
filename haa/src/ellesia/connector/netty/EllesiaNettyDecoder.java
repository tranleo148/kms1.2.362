package ellesia.connector.netty;

import ellesia.connector.EllesiaClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

import tools.MapleAESOFB;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class EllesiaNettyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> list) throws Exception {
        final EllesiaClient client = ctx.channel().attr(EllesiaClient.CLIENTKEY).get();

        if (client == null) {
            return;
        }

        if (buffer.readableBytes() < 4) {
            return;
        }
        final int packetHeader = buffer.readInt();
        int packetlength = MapleAESOFB.getPacketLength(packetHeader);
        if (buffer.readableBytes() < packetlength) {
            buffer.resetReaderIndex();
            return;
        }

        buffer.markReaderIndex();

        byte[] decoded = new byte[packetlength];
        buffer.readBytes(decoded);
        buffer.markReaderIndex();
        list.add(new LittleEndianAccessor(new ByteArrayByteStream(decoded)));
    }

}
