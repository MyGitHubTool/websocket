package com.shanglike.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author YIJIUE
 * server启动类
 */

@Component
public class NettyServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Value("${server.netty.port}")
    private int port;

    @Autowired
    private EventLoopGroup bossGroup;

    @Autowired
    private EventLoopGroup workGroup;

    private ChannelFuture serverChannelFuture;

    @Autowired
    private WebSocketChildChannelHandler webSocketChildChannelHandler;

    /**
     * 启动netty 服务器
     */
    @Override
    public void run() {
        try {
            // 主 子线程模式
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
                    .childHandler(webSocketChildChannelHandler);
            logger.info("socketServer started");
            serverChannelFuture = serverBootstrap.bind(port).sync();
        } catch (Exception e) {
            logger.error("socketServer is error : {}", e.getStackTrace());
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    /**
     * 描述：关闭Netty Websocket服务器，主要是释放连接
     * 连接包括：服务器连接serverChannel，
     * 客户端TCP处理连接bossGroup，
     * 客户端I/O操作连接workerGroup
     * <p>
     * 若只使用
     * bossGroupFuture = bossGroup.shutdownGracefully();
     * workerGroupFuture = workerGroup.shutdownGracefully();
     * 会造成内存泄漏。
     */
    public void close() {
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workGroup.shutdownGracefully();

        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
    }

}