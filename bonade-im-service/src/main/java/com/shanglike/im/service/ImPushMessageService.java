package com.shanglike.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanglike.im.entity.ImPushMessage;
import com.shanglike.im.vo.*;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author GuoJie123
 * @since 2019-12-20
 */
public interface ImPushMessageService extends IService<ImPushMessage> {

    /**
     * 新增推送消息
     *
     * @param imPushMessageAddVo 消息相关内容
     */
    void addPushMessage(ImPushMessageAddVo imPushMessageAddVo);


    /**
     * 新增推送消息
     *
     * @param imMessagePushVo 消息相关内容
     */
    void sendSingleMessage(ImMessagePushVo imMessagePushVo);

    /**
     * 查询未推送的消息列表
     *
     * @param userId 用户id
     * @return 消息列表
     */
    List<ImPushMessageVo> findNoPushMessageList(Long userId);

    /**
     * 更新消息的状态
     *
     * @param userId 用户id
     */
    void updateImMessageState(Long userId);


    /**
     * 查询消息的内容
     *
     * @param id 消息id
     * @return 消息内容
     */
    ImPushMessageVo findOneById(Long id);

    /**
     * 更新消息的状态
     *
     * @param id 消息的id
     */
    void updateMessageStateById(Long id);

    /**
     * 查询消息历史
     *
     * @param imPushMessageQueryVo 查询条件
     * @return 消息历史
     */
    List<ImPushMessageVo> messageHistory(ImPushMessageQueryVo imPushMessageQueryVo);

    /**
     * 删除聊天记录
     *
     * @param imPushMessageDeleteVo 条件
     */
    void deleteHistory(ImPushMessageDeleteVo imPushMessageDeleteVo);

    /**
     * 单条或多条删除聊天历史
     *
     * @param imPushMessageDeleteVo 条件
     */
    void deleteMessage(ImPushMessageDeleteVo imPushMessageDeleteVo);

    /**
     * 测回消息
     *
     * @param imMessagePushVo 条件
     */
    void withDrawMessage(ImMessagePushVo imMessagePushVo);
}
