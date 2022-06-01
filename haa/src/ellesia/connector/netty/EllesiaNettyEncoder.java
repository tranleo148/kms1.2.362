package ellesia.connector.netty;

import ellesia.connector.EllesiaClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.concurrent.locks.Lock;

public class EllesiaNettyEncoder extends MessageToByteEncoder<byte[]> {


    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] pData, ByteBuf buffer) throws Exception {
        final EllesiaClient client = ctx.channel().attr(EllesiaClient.CLIENTKEY).get();

        /*if (client != null) {
            final Lock mutex = client.getLock();

            mutex.lock();
            try {
                int i = pData.length;
                byte[] a = {(byte) (i & 0xFF), (byte) ((i >>> 8) & 0xFF), (byte) ((i >>> 16) & 0xFF), (byte) ((i >>> 24) & 0xFF)};
                buffer.writeBytes(a);
                buffer.writeBytes(pData);
            } finally {
                mutex.unlock();
            }
        } else {
            buffer.writeByte((byte) 0xFF);
            buffer.writeBytes(pData);
        }*/
        buffer.writeBytes(pData);
        ctx.flush();
    }
}
