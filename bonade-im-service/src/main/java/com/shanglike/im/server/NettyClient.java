package com.shanglike.im.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClient {


    private String host;

    private int port;

    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }



    public void start() throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)

                    .channel(NioSocketChannel.class)

                    .remoteAddress(new InetSocketAddress(host, port))

                    .handler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            //连接到服务端，connect是异步连接，在调用同步等待sync，等待连接成功
            ChannelFuture channelFuture = bootstrap.connect().sync();

            //阻塞直到客户端通道关闭
            channelFuture.channel().closeFuture().sync();

        }finally {
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }

    }

    public static void main(String []args) throws InterruptedException {
        new NettyClient("127.0.0.1",12301).start();
    }

}
