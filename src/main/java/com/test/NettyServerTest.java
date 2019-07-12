package com.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


public class NettyServerTest {
    private int port;

    /**
     * 端口号
     *
     * @param port
     */
    public NettyServerTest(int port) {
        this.port = port;
    }

    public void start() {
        /**
         * 1.基于netty写的rpc，首先我们应该了解  jdk1.4中的nio ，然后我们看netty 会相对容易一些，nio中涉及的组件有 buffer，channel，selector
         * 2.
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();//相当于socket编程中的服务端的
        EventLoopGroup workGrop = new NioEventLoopGroup();//真正干活的线程
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workGrop).channel(NioServerSocketChannel.class).
                option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(port).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline channelPipeline = socketChannel.pipeline();
                //编码器
                channelPipeline.addLast("encoder", new ObjectEncoder())
                        //解码器
                        .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                        .addLast(new InvokeHandler());
            }
        });

        ChannelFuture future = null;
        try {
            future = b.bind(port).sync();//绑定端口号
            System.out.println("......server is ready......");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workGrop.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new NettyServerTest(9999).start();
    }
}
