package com.shanglike.im.mq;

import com.alibaba.fastjson.JSON;
import com.shanglike.im.constant.RabbitMqConstant;
import com.shanglike.im.constant.WebsocketConstant;
import com.shanglike.im.enums.ImMessageStateEnum;
import com.shanglike.im.service.ImPushMessageService;
import com.shanglike.im.utils.CommonMethods;
import com.shanglike.im.vo.ImPushMessageVo;
import io.netty.channel.ChannelHandlerContext;
import org.spin.common.log.LoggerUtilI;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author GuoJie
 * @date 2020/1/2 10:49
 */
@Component
public class UserMessageConsumeService {
    private LoggerUtilI logger = LoggerUtilI.getLogger(UserMessageConsumeService.class);

    @Autowired
    private ImPushMessageService imPushMessageService;


    //注意这里不要定义队列名称,系统会随机产生
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(autoDelete = "true"),
            exchange = @Exchange(value = RabbitMqConstant.FANOUT_EXCHANGE_NAME, type = ExchangeTypes.FANOUT)
    )
    )
    @RabbitHandler
    public void process(Message message) {
        //mq接收到数据 要根据数据的类型来做相应的处理
        String msg = new String(message.getBody());
        logger.info("接收到mq的消息 :{}", msg);
        ImPushMessageVo imPushMessage = JSON.parseObject(msg, ImPushMessageVo.class);
        //获取到用户的id 查询内存中是不是存在
        if (Objects.isNull(imPushMessage)) {
            return;
        }
        Long receiveUserId = imPushMessage.getReceiveUserId();
        Set<Long> userIdSet = WebsocketConstant.onlineUserSet;
        //表示用户连接的是当前的机器
        if (!userIdSet.contains(receiveUserId)) {
            return;
        }
        ChannelHandlerContext channelHandlerContext = WebsocketConstant.onlineUserMap.get(receiveUserId);
        if (Objects.isNull(channelHandlerContext)) {
            return;
        }
        //推送到客户端
        CommonMethods.sendMessage(channelHandlerContext, JSON.toJSONString(imPushMessage));
        //更新消息的状态是已读
        imPushMessage.setState(ImMessageStateEnum.HAVE_PUSH.getKey());
        imPushMessageService.updateMessageStateById(imPushMessage.getId());
    }

}
