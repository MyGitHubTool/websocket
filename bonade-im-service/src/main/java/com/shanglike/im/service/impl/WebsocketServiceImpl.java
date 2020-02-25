package com.shanglike.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanglike.im.config.ServerConfig;
import com.shanglike.im.constant.MessageContentConstant;
import com.shanglike.im.constant.MessageTypeConstant;
import com.shanglike.im.constant.RedisKeyConstant;
import com.shanglike.im.constant.WebsocketConstant;
import com.shanglike.im.service.ImPushMessageService;
import com.shanglike.im.service.WebsocketService;
import com.shanglike.im.utils.CommonMethods;
import com.shanglike.im.utils.ListUtil;
import com.shanglike.im.vo.ImPushMessageVo;
import com.shanglike.im.vo.WebSocketRequestVo;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author GuoJie
 * @date 2019/12/18 11:19
 */
@Service
public class WebsocketServiceImpl implements WebsocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketServiceImpl.class);

    @Autowired
    private ImPushMessageService imPushMessageService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ServerConfig serverConfig;

    @Override
    public void userConnectWebSocket(WebSocketRequestVo webSocketRequestVo, ChannelHandlerContext ctx) {
        //如果之前用户已经连接 要推送断开连接
        if (WebsocketConstant.onlineUserSet.contains(webSocketRequestVo.getReceiveUserId())) {
            ChannelHandlerContext channelHandlerContext = WebsocketConstant.onlineUserMap.get(webSocketRequestVo.getReceiveUserId());
            //发送退出登录的通知
            this.loginOut(channelHandlerContext);
        }
        //用户连接到后台的websocket服务之后 将连接的在线的用户信息保存到内存
        WebsocketConstant.onlineUserMap.put(webSocketRequestVo.getReceiveUserId(), ctx);
        //在线的用户
        WebsocketConstant.onlineUserSet.add(webSocketRequestVo.getReceiveUserId());
        //redis也存储一份在线的信息
        redisTemplate.opsForSet().add(RedisKeyConstant.LOGIN_STATUS_PREFIX, String.valueOf(webSocketRequestVo.getReceiveUserId()));
        //存储用户路由前缀 TODO 暂时还用不到
        redisTemplate.opsForValue().set(RedisKeyConstant.ROUTE_PREFIX + webSocketRequestVo.getReceiveUserId(), serverConfig.getUrl());
        //推送消息给前端
        List<ImPushMessageVo> imPushMessageVoList = imPushMessageService.findNoPushMessageList(webSocketRequestVo.getReceiveUserId());
        if (ListUtil.isEmpty(imPushMessageVoList)) {
            return;
        }
        imPushMessageVoList.forEach(m -> {
                    m.setContent(JSONObject.parseObject(m.getContentText()));
                    m.setType(MessageTypeConstant.SEND_SINGLE_MESSAGE_TYPE);
                    CommonMethods.sendMessage(ctx, JSON.toJSONString(m));
                }
        );
        //更新消息的状态
        imPushMessageService.updateImMessageState(webSocketRequestVo.getReceiveUserId());
    }

    private void loginOut(ChannelHandlerContext ctx) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", MessageTypeConstant.LOGIN_OUT);
        jsonObject.put("content", MessageContentConstant.USER_LOGIN_OTHER_MACHINE);
        CommonMethods.sendMessage(ctx, JSON.toJSONString(jsonObject));
    }

    @Override
    public void userDisConnectWebSocket(ChannelHandlerContext ctx) {
        Iterator<Map.Entry<Long, ChannelHandlerContext>> iterator =
                WebsocketConstant.onlineUserMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, ChannelHandlerContext> entry = iterator.next();
            if (entry.getValue() == ctx) {
                LOGGER.info("正在移除握手实例....");
                WebsocketConstant.webSocketHandShakerMap.remove(ctx.channel().id().asLongText());
                iterator.remove();
                //同时要在本地内存和redis中移除在线的用户
                WebsocketConstant.onlineUserSet.remove(entry.getKey());
                //redis也存储一份在线的信息
                redisTemplate.opsForSet().remove(RedisKeyConstant.LOGIN_STATUS_PREFIX, String.valueOf(entry.getKey()));
                //存储用户路由前缀 TODO 暂时还用不到
                redisTemplate.delete(RedisKeyConstant.ROUTE_PREFIX + entry.getKey());
                break;
            }
        }
    }


}
