package cuit.pymjl.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 18:27
 **/
public abstract class AbstractCustomHeartbeatHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 数据类型：PING（心跳包）
     */
    public static final byte PING_MSG = (byte) 1;
    /**
     * 数据类型：PONG（心跳包）
     */
    public static final byte PONG_MSG = (byte) 2;
    /**
     * 数据类型：自定义内容
     */
    public static final byte CUSTOM_MSG = (byte) 3;

    /**
     * 名称
     */
    protected String name;

    /**
     * 触发事件的次数
     */
    protected AtomicInteger heartbeatCount;

    public AbstractCustomHeartbeatHandler(String name) {
        this.name = name;
        this.heartbeatCount = new AtomicInteger(0);
    }

    /**
     * 当有数据到达时，触发该方法，定义处理数据的模板方法
     *
     * @param context 上下文
     * @param byteBuf 字节缓冲区
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf byteBuf) throws Exception {
        if (byteBuf.getByte(4) == PING_MSG) {
            //服务端接到ping才发送pong
            sendPongMsg(context);
        } else if (byteBuf.getByte(4) == PONG_MSG) {
            System.out.println(name + " get pong msg from " + context.channel().remoteAddress());
        } else {
            handleData(context, byteBuf);
        }
    }

    /**
     * 处理数据的方法，由子类实现
     *
     * @param context 上下文
     * @param byteBuf 字节缓冲区
     */
    protected abstract void handleData(ChannelHandlerContext context, ByteBuf byteBuf);

    /**
     * 当触发事件后，会根据不同的事件类型，调用不同的方法
     *
     * @param ctx ctx channel handler context
     * @param evt evt idle state event
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                // 当读空闲时，说明没有收到客户端数据，服务端关闭连接
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                // 写空闲
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                // 读写空闲,客户端发送心跳包，发送了五个心跳包后，停止发送，触发服务端的读事件，关闭连接
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }

    /**
     * 发送pong心跳包，由服务端发送
     *
     * @param context 上下文
     */
    private void sendPongMsg(ChannelHandlerContext context) {
        ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PONG_MSG);
        context.channel().writeAndFlush(buf);
        System.out.println(name + " sent pong msg to " + context.channel().remoteAddress() +
                ", count: " + heartbeatCount.incrementAndGet());
    }

    /**
     * 发送ping心跳包，由客户端发送
     *
     * @param context 上下文
     */
    protected void sendPingMsg(ChannelHandlerContext context) {
        ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PING_MSG);
        context.writeAndFlush(buf);
        System.out.println(name + " sent ping msg to " + context.channel().remoteAddress()
                + ", count: " + heartbeatCount.incrementAndGet());
    }

}
