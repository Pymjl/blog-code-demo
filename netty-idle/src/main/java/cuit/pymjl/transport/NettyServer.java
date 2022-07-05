package cuit.pymjl.transport;

import cuit.pymjl.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 19:05
 **/
public class NettyServer {
    /**
     * 端口
     */
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            //添加心跳检测的Handler，监听读空闲，当10秒内没有读到数据，则触发读空闲事件，关闭channel
                            p.addLast(new IdleStateHandler(10, 0, 0));
                            //Netty提供的自定义的消息解码器，用于解码消息的长度，并且把消息体封装到ByteBuf中(如若不明白请自行百度)
                            p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                            //添加自定义的消息处理器，用于处理消息
                            p.addLast(new ServerHandler());
                        }
                    });
            Channel ch = bootstrap.bind(port).sync().channel();
            System.out.println("server start success, port: " + port);
            ch.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
