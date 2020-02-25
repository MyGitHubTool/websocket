package com.shanglike.im.constant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GuoJie
 * @version 1.0
 * @date 2018年5月18日 下午9:17:35
 */
public class WebsocketConstant {
    /**
     * webSocketServerHandShaker表，用channelId为键，存放握手实例。用来响应CloseWebSocketFrame的请求；
     */
    public static Map<String, WebSocketServerHandshaker> webSocketHandShakerMap =
            new ConcurrentHashMap<String, WebSocketServerHandshaker>();

    /**
     * onlineUser表，用userId为键，存放在线的客户端连接上下文；
     */
    public static Map<Long, ChannelHandlerContext> onlineUserMap =
            new ConcurrentHashMap<Long, ChannelHandlerContext>();

    /**
     * 本地存储在线的用户
     */
    public static Set<Long> onlineUserSet =
            new HashSet<>();

}
