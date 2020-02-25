package com.shanglike.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanglike.im.entity.ImPushMessage;
import com.shanglike.im.vo.ImPushMessageDeleteVo;
import com.shanglike.im.vo.ImPushMessageQueryVo;
import com.shanglike.im.vo.ImPushMessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author GuoJie123
 * @since 2019-12-20
 */
public interface ImPushMessageMapper extends BaseMapper<ImPushMessage> {

    /**
     * 查询未推送的消息列表
     *
     * @param userId 用户id
     * @return 消息列表
     */
    List<ImPushMessageVo> findNoPushMessageList(@Param("userId") Long userId);

    /**
     * 更新消息的状态
     *
     * @param userId 用户id
     */
    void updateImMessageState(@Param("userId") Long userId);

    /**
     * 批量插入消息
     *
     * @param imPushMessageList 消息
     */
    void batchAddPushMessage(@Param("list") List<ImPushMessage> imPushMessageList);

    /**
     * 查询消息历史
     *
     * @param imPushMessageQueryVo 查询条件
     * @return 消息历史
     */
    List<ImPushMessageVo> messageHistory(ImPushMessageQueryVo imPushMessageQueryVo);

    /**
     * 更新作为接收者的状态
     *
     * @param imPushMessageDeleteVo 条件
     */
    void updateReceiveHistory(ImPushMessageDeleteVo imPushMessageDeleteVo);

    /**
     * 更新作为发送者的状态
     *
     * @param imPushMessageDeleteVo 条件
     */
    void updateSendHistory(ImPushMessageDeleteVo imPushMessageDeleteVo);

    /**
     * 更新接收者消息
     *
     * @param imPushMessageDeleteVo 条件
     */
    void updateReceiveMessage(ImPushMessageDeleteVo imPushMessageDeleteVo);

    /**
     * 更新发送者消息
     *
     * @param imPushMessageDeleteVo 条件
     */
    void updateSendMessage(ImPushMessageDeleteVo imPushMessageDeleteVo);
}
