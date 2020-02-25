package com.shanglike.im.server;


import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义子处理器
 *
 * @author GuoJie
 */
@Component
@Sharable
public class WebSocketChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketServerHandler webSocketServerHandler;

    @Autowired
    private HttpRequestHandler httpRequestHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // HTTP编码解码器
        pipeline.addLast("http-codec", new HttpServerCodec());
        // 把HTTP头、HTTP体拼成完整的HTTP请求
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        // 方便大文件传输，不过实质上都是短的文本数据
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast("Idle-stat-handler", new IdleStateHandler(20, 40, 60));
        pipeline.addLast("webSocket-socket-server-protocol-handler", new WebSocketServerProtocolHandler("/ws"));
        //心跳处理器
        pipeline.addLast("Hear-beat-handler", new HearBeatHandler());
        //处理http的请求
        pipeline.addLast("http-handler", httpRequestHandler);
        //处理websocket
        pipeline.addLast("websocket-handler", webSocketServerHandler);

    }

}
