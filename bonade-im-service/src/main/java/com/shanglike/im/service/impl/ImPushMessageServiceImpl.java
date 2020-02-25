package com.shanglike.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanglike.im.constant.MessageContentConstant;
import com.shanglike.im.constant.MessageTypeConstant;
import com.shanglike.im.constant.WebsocketConstant;
import com.shanglike.im.entity.ImPushMessage;
import com.shanglike.im.enums.ImMessageStateEnum;
import com.shanglike.im.enums.MsgTypeEnum;
import com.shanglike.im.mapper.ImPushMessageMapper;
import com.shanglike.im.mq.UserMessageProducer;
import com.shanglike.im.service.ImPushMessageService;
import com.shanglike.im.utils.CommonMethods;
import com.shanglike.im.utils.ListUtil;
import com.shanglike.im.vo.*;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spin.common.vo.CurrentUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GuoJie123
 * @since 2019-12-20
 */
@Service
public class ImPushMessageServiceImpl extends ServiceImpl<ImPushMessageMapper, ImPushMessage> implements ImPushMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImPushMessageServiceImpl.class);

    @Autowired
    private ImPushMessageMapper imPushMessageMapper;


    @Autowired
    private UserMessageProducer userMessageProducer;

    @Override
    @Async
    @Transactional(rollbackFor = Throwable.class)
    public void addPushMessage(ImPushMessageAddVo imPushMessageAddVo) {
        //将消息先入库 如果用户在线的话 将消息直接推送给前端 改变消息的状态
        List<Long> userIdList = imPushMessageAddVo.getReceiveUserIds();
        if (ListUtil.isEmpty(userIdList)) {
            return;
        }
        //消息通过mq一条条的推送 到相应的机器上面
        userIdList.forEach(m -> {
            ImPushMessage imPushMessage = new ImPushMessage();
            BeanUtils.copyProperties(imPushMessageAddVo, imPushMessage);
            imPushMessage.setReceiveUserId(m);
            imPushMessage.setCreateTime(LocalDateTime.now());
            imPushMessage.setMsgType(MsgTypeEnum.PUSH_MESSAGE.getKey());
            imPushMessage.setState(ImMessageStateEnum.NOT_PUSH.getKey());
            CurrentUser current = CurrentUser.getCurrent();
            if (!Objects.isNull(current)) {
                imPushMessage.setCreateBy(current.getId());
            }
            imPushMessage.setContent(JSON.toJSONString(imPushMessageAddVo.getContent()));
            imPushMessage.setCreateTimeTimestamp(System.currentTimeMillis());
            //数据先保存到数据库
            imPushMessageMapper.insert(imPushMessage);
            //在通过mq广播到对应的机器上面
            this.pushMessage(imPushMessage.getId(), 1);
        });

    }

    private void pushMessage(Long id, Integer pushFlag) {
        ImPushMessageVo imPushMessageVo = new ImPushMessageVo();
        ImPushMessage imPushMessage = imPushMessageMapper.selectById(id);
        BeanUtils.copyProperties(imPushMessage, imPushMessageVo);
        imPushMessageVo.setContent(JSONObject.parseObject(imPushMessage.getContent()));
        imPushMessageVo.setType(MessageTypeConstant.SEND_SINGLE_MESSAGE_TYPE);
        //先发送给自己
        if (Objects.equals(pushFlag, 2)) {
            this.sendMessageToSelf(imPushMessageVo);
        }
        userMessageProducer.userMessagePush(JSON.toJSONString(imPushMessageVo));

    }

    @Override
    public void sendSingleMessage(ImMessagePushVo imMessagePushVo) {
        ImPushMessage imPushMessage = new ImPushMessage();
        BeanUtils.copyProperties(imMessagePushVo, imPushMessage);
        imPushMessage.setContent(JSON.toJSONString(imMessagePushVo.getContent()));
        imPushMessage.setCreateTime(LocalDateTime.now());
        imPushMessage.setCreateTimeTimestamp(System.currentTimeMillis());
        imPushMessage.setState(ImMessageStateEnum.NOT_PUSH.getKey());
        CurrentUser current = CurrentUser.getCurrent();
        if (!Objects.isNull(current)) {
            imPushMessage.setCreateBy(current.getId());
        }
        //数据先保存到数据库
        imPushMessageMapper.insert(imPushMessage);

        //在通过mq广播到对应的机器上面
        this.pushMessage(imPushMessage.getId(), 2);


    }

    private void sendMessageToSelf(ImPushMessageVo imPushMessageVo) {
        Long sendUserId = imPushMessageVo.getSendUserId();
        ChannelHandlerContext sendChannelHandlerContext = WebsocketConstant.onlineUserMap.get(sendUserId);
        //推送到客户端
        CommonMethods.sendMessage(sendChannelHandlerContext, JSON.toJSONString(imPushMessageVo));
    }


    private void batchInsertImMessage(List<ImPushMessage> imPushMessageList, int size) {
        int insertLength = imPushMessageList.size();
        int i = 0;
        while (insertLength > size) {
            imPushMessageMapper.batchAddPushMessage(imPushMessageList.subList(i, i + size));
            i = i + size;
            insertLength = insertLength - size;
        }
        if (insertLength > 0) {
            imPushMessageMapper.batchAddPushMessage(imPushMessageList.subList(i, i + insertLength));
        }
    }

    @Override
    public List<ImPushMessageVo> findNoPushMessageList(Long userId) {
        return imPushMessageMapper.findNoPushMessageList(userId);
    }

    @Override
    public void updateImMessageState(Long userId) {
        imPushMessageMapper.updateImMessageState(userId);
    }

    @Override
    public ImPushMessageVo findOneById(Long id) {
        ImPushMessageVo imPushMessageVo = new ImPushMessageVo();
        ImPushMessage imPushMessage = imPushMessageMapper.selectById(id);
        if (Objects.isNull(imPushMessage)) {
            return null;
        }
        BeanUtils.copyProperties(imPushMessage, imPushMessageVo);
        return imPushMessageVo;
    }

    @Override
    public void updateMessageStateById(Long id) {
        ImPushMessage imPushMessage = new ImPushMessage();
        imPushMessage.setId(id);
        imPushMessage.setState(1);
        imPushMessage.setUpdateTime(LocalDateTime.now());
        imPushMessageMapper.updateById(imPushMessage);

    }

    @Override
    public List<ImPushMessageVo> messageHistory(ImPushMessageQueryVo imPushMessageQueryVo) {
        return imPushMessageMapper.messageHistory(imPushMessageQueryVo);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteHistory(ImPushMessageDeleteVo imPushMessageDeleteVo) {
        //删除两人之间的聊天记录 要根据条件来更新两个状态值 更新作为接收者的状态
        imPushMessageMapper.updateReceiveHistory(imPushMessageDeleteVo);
        //更新作为发送者的状态
        imPushMessageMapper.updateSendHistory(imPushMessageDeleteVo);


    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteMessage(ImPushMessageDeleteVo imPushMessageDeleteVo) {
        if (ListUtil.isEmpty(imPushMessageDeleteVo.getMessageIds())) {
            return;
        }
        //删除两人之间的聊天记录 要根据条件来更新两个状态值 更新作为接收者的状态
        imPushMessageMapper.updateReceiveMessage(imPushMessageDeleteVo);
        //更新作为发送者的状态
        imPushMessageMapper.updateSendMessage(imPushMessageDeleteVo);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void withDrawMessage(ImMessagePushVo imMessagePushVo) {
        //撤回消息 要给发起者和 接收者都发送消息 发起者就是当台机器 接收者不确定是哪台机器 要mq分发发送
        this.updateImMessage(imMessagePushVo);
        ImPushMessageVo imPushMessageVo = this.getImPushMessageVo(imMessagePushVo);
        //给发送者发送消息
        this.sendMessageToSelf(imPushMessageVo);
        //给接收者发送消息
        userMessageProducer.userMessagePush(JSON.toJSONString(imPushMessageVo));
    }

    private ImPushMessageVo getImPushMessageVo(ImMessagePushVo imMessagePushVo) {
        ImPushMessageVo imPushMessageVo = new ImPushMessageVo();
        BeanUtils.copyProperties(imMessagePushVo, imPushMessageVo);
        imPushMessageVo.setId(imMessagePushVo.getMessageId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", MessageContentConstant.RETRACTED_MESSAGE);
        imPushMessageVo.setContent(jsonObject);
        imPushMessageVo.setType(MessageTypeConstant.WITH_DRAW_MESSAGE);
        return imPushMessageVo;
    }

    private void updateImMessage(ImMessagePushVo imMessagePushVo) {
        ImPushMessage imPushMessage = new ImPushMessage();
        imPushMessage.setId(imMessagePushVo.getMessageId());
        imPushMessage.setUpdateTime(LocalDateTime.now());
        imPushMessage.setRetracted(1);
        imPushMessageMapper.updateById(imPushMessage);
    }


}
