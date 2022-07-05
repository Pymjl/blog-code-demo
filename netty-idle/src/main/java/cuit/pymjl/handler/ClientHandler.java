package cuit.pymjl.handler;

import cuit.pymjl.transport.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 19:01
 **/
public class ClientHandler extends AbstractCustomHeartbeatHandler {
    private NettyClient client;
    private AtomicInteger cnt;

    public ClientHandler(NettyClient client) {
        super("client");
        this.client = client;
        this.cnt = new AtomicInteger(0);
    }

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        byte[] data = new byte[byteBuf.readableBytes() - 5];
        byteBuf.skipBytes(5);
        byteBuf.readBytes(data);
        String content = new String(data);
        System.out.println(name + " get content from server: " + content);
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        // 当客户端发送了五个心跳包后，停止发送，触发服务端的读事件，关闭连接
        if (cnt.incrementAndGet() > 5) {
            return;
        }
        super.handleAllIdle(ctx);
        sendPingMsg(ctx);
    }

    /**
     * 当连接关闭后，客户端重新连接服务端
     *
     * @param ctx ctx
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect();
    }
}
