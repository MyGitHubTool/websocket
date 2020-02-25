package com.shanglike.im.service;

import com.shanglike.im.vo.WebSocketRequestVo;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author GuoJie
 * @date 2019/12/18 11:19
 */

public interface WebsocketService {

    /**
     * 用户连接到websocket
     *
     * @param webSocketRequestVo 相关的信息
     * @param ctx                连接的上下文信息
     */
    void userConnectWebSocket(WebSocketRequestVo webSocketRequestVo, ChannelHandlerContext ctx);


    /**
     * 用户断开连接 websocket
     *
     * @param ctx 连接的上下文信息
     */
    void userDisConnectWebSocket(ChannelHandlerContext ctx);
}
