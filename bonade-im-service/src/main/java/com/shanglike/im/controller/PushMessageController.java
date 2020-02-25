package com.shanglike.im.controller;

import com.shanglike.im.server.MyChannelHandlerPool;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.spin.common.web.annotation.GetApi;
import org.spin.common.web.annotation.PostApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GuoJie
 * @date 2019/7/9 12:01
 */
@RestController
@RequestMapping("v1/messagePush")
public class PushMessageController {

    @PostApi(value = "pushMessage", auth = false)
    public void pushToAllClient() {
        ChannelGroup channels = MyChannelHandlerPool.channelGroup;
        if (channels == null || channels.size() == 0) {
            return;
        }
        String content = "推送的消息 客户端都能接收到";
        channels.writeAndFlush(new TextWebSocketFrame(content));
    }

    @GetApi(value = "pushToConditionClient", auth = false)
    public void pushToConditionClient() {
        //条件推送给相应的客户端 channel 要和用户进行绑定 获取条件的client 前端可以传对应的条件
        ChannelGroup channels = MyChannelHandlerPool.channelGroup;
        String content = "推送的消息 客户端都能接收到";
        channels.writeAndFlush(new TextWebSocketFrame(content));
    }


}
