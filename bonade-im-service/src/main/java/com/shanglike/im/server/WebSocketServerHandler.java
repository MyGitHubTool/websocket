package com.shanglike.im.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanglike.im.constant.MessageContentConstant;
import com.shanglike.im.constant.MessageTypeConstant;
import com.shanglike.im.constant.WebsocketConstant;
import com.shanglike.im.service.ImPushMessageService;
import com.shanglike.im.service.WebsocketService;
import com.shanglike.im.utils.CommonMethods;
import com.shanglike.im.vo.ImMessagePushVo;
import com.shanglike.im.vo.WebSocketRequestVo;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spin.common.web.RestfulResponse;
import org.spin.core.ErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author websocket处理器
 */
@Component
@Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandler.class);


    @Autowired
    private WebsocketService websocketService;

    @Autowired
    private ImPushMessageService imPushMessageService;


    /**
     * 描述：读取完连接的消息后，对消息进行处理。
     * 这里主要是处理WebSocket请求
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handlerWebSocketFrame(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("与客户端建立连接，通道开启！");
        //添加到channelGroup通道组
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
    }

    /**
     * 描述：处理WebSocketFrame
     *
     * @param ctx   ChannelHandlerContext上下文
     * @param frame 客户端传递过来的数据
     * @throws Exception 异常处理
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 关闭请求 对消息进行处理
        if (frame instanceof CloseWebSocketFrame) {
            //关闭请求的时候要删除
            WebSocketServerHandshaker handShaker =
                    WebsocketConstant.webSocketHandShakerMap.get(ctx.channel().id().asLongText());
            if (handShaker == null) {
                sendErrorMessage(ctx, "不存在的客户端连接！");
            } else {
                handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            }
            return;
        }
        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            sendErrorMessage(ctx, "仅支持文本(Text)格式，不支持二进制消息");
        }
        WebSocketRequestVo param = null;
        if (frame instanceof TextWebSocketFrame) {
            // 客户端发送过来的消息
            String request = ((TextWebSocketFrame) frame).text();
            LOGGER.info("服务端收到新信息：" + request);
            try {
                param = JSON.parseObject(request, WebSocketRequestVo.class);
            } catch (Exception e) {
                sendErrorMessage(ctx, "JSON字符串转换出错！");
                LOGGER.info(e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        if (param == null) {
            sendErrorMessage(ctx, "参数为空！");
            return;
        }
        Integer type = param.getType();
        switch (type) {
            //连接之后 将连接的信息保存到内存中去 100
            //用户如果在线的话要做限制 目前是单机
            case MessageTypeConstant
                    .LINKED_WEBSOCKET_MESSAGE_TYPE:
                websocketService.userConnectWebSocket(param, ctx);
                break;
            //表示发送的是心跳消息 101
            case MessageTypeConstant.HEART_BEAT_MESSAGE_TYPE:
                sendPongMessage(ctx);
                break;
            //发送单条消息 单聊 102
            case MessageTypeConstant.SEND_SINGLE_MESSAGE_TYPE:
                this.sendSingleMessage(param);
                break;
            //关闭连接 103
            case MessageTypeConstant.LOGIN_OUT:
                websocketService.userDisConnectWebSocket(ctx);
                break;
            //消息撤回 104
            case MessageTypeConstant.WITH_DRAW_MESSAGE:
                this.withDrawMessage(param);
                break;

            default:
                break;
        }
    }

    private void withDrawMessage(WebSocketRequestVo param) {
        ImMessagePushVo imMessagePushVo = new ImMessagePushVo();
        BeanUtils.copyProperties(param, imMessagePushVo);
        imPushMessageService.withDrawMessage(imMessagePushVo);
    }

    private void sendSingleMessage(WebSocketRequestVo param) {
        ImMessagePushVo imMessagePushVo = new ImMessagePushVo();
        BeanUtils.copyProperties(param, imMessagePushVo);
        imPushMessageService.sendSingleMessage(imMessagePushVo);
    }

    /**
     * 描述：客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("与客户端断开连接,通道关闭！");
        websocketService.userDisConnectWebSocket(ctx);

    }

    /**
     * 异常处理：关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 发送心跳机制
     *
     * @param ctx channel
     */
    private void sendPongMessage(ChannelHandlerContext ctx) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", MessageTypeConstant.HEART_BEAT_MESSAGE_TYPE);
        jsonObject.put("content", MessageContentConstant.HEART_BEAT_MESSAGE);
        CommonMethods.sendMessage(ctx, JSON.toJSONString(jsonObject));
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        RestfulResponse response = RestfulResponse.error(new ErrorCode(505, errorMsg));
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(response)));
    }


}
