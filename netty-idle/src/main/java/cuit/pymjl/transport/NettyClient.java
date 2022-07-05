package cuit.pymjl.transport;


import cuit.pymjl.handler.AbstractCustomHeartbeatHandler;
import cuit.pymjl.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 18:26
 **/
public class NettyClient {
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;
    private final String host;
    private final Integer port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 发送数据
     *
     * @throws Exception 异常
     */
    public void sendData() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            if (channel != null && channel.isActive()) {
                //构建消息内容
                String content = "client msg " + i;
                //分配缓冲区
                ByteBuf buf = channel.alloc().buffer(5 + content.getBytes().length);
                //写入消息长度(4字节)
                buf.writeInt(5 + content.getBytes().length);
                //写入消息类型(1字节)
                buf.writeByte(AbstractCustomHeartbeatHandler.CUSTOM_MSG);
                //写入消息内容(字符串)
                buf.writeBytes(content.getBytes());
                //发送消息
                channel.writeAndFlush(buf);
            }
            //随机睡眠0-20秒
            Thread.sleep(random.nextInt(20000));
        }
    }

    public void start() {
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            //添加心跳检测的Handler，监听读写空闲，当5秒内没有数据交互，则触发读写空闲事件，发送心跳包
                            p.addLast(new IdleStateHandler(0, 0, 5));
                            //Netty提供的自定义的消息解码器，用于解码消息的长度，并且把消息体封装到ByteBuf中(如若不明白请自行百度)
                            p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                            //自定义消息处理器，用于处理消息
                            p.addLast(new ClientHandler(NettyClient.this));
                        }
                    });
            //连接服务器
            doConnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture future = bootstrap.connect(host, port);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = futureListener.channel();
                    System.out.println("Connect to server successfully!");
                } else {
                    System.out.println("Failed to connect to server, try connect after 10s");
                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        });
    }

}
