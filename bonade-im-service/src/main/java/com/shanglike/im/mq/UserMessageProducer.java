package com.shanglike.im.mq;


import com.alibaba.fastjson.JSON;
import com.shanglike.im.constant.RabbitMqConstant;
import com.shanglike.im.service.ImPushMessageService;
import com.shanglike.im.vo.ImPushMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户同步消息发布
 *
 * @author GuoJie
 * @date 2019/7/13 10:58
 */
@Component
public class UserMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 用户同步Mq
     *
     * @param messageContent 推送的消息
     */
    public void userMessagePush(String messageContent) {
        //转化成中台的用户信息
        LOGGER.info("mq分发推送消息：{}", messageContent);
        //查询消息的内容进行推送
        Message message = MessageBuilder.withBody(messageContent.getBytes()).build();
        rabbitTemplate.convertAndSend(RabbitMqConstant.FANOUT_EXCHANGE_NAME, "", message);

    }


}
