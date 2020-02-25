package com.shanglike.im.utils;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.spin.common.web.RestfulResponse;

/**
 * @author GuoJie
 * @date 2019/12/30 10:32
 */

public class CommonMethods {

    public static void sendMessage(ChannelHandlerContext ctx, String message) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(obtainMessage(message)));
    }


    private static String obtainMessage(String message) {
        return JSON.toJSONString(RestfulResponse.ok(JSON.parse(message)));

    }
}
