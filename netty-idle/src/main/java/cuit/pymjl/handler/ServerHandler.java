package cuit.pymjl.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 19:06
 **/
public class ServerHandler extends AbstractCustomHeartbeatHandler {
    public ServerHandler() {
        super("server");
    }

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf buf) {
        byte[] data = new byte[buf.readableBytes() - 5];
        ByteBuf responseBuf = Unpooled.copiedBuffer(buf);
        buf.skipBytes(5);
        buf.readBytes(data);
        String content = new String(data);
        System.out.println(name + " get content: " + content);
        channelHandlerContext.write(responseBuf);
    }

    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        super.handleReaderIdle(ctx);
        System.err.println("---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
        ctx.close();
    }
}
