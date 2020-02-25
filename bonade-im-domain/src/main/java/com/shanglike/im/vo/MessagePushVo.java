package com.shanglike.im.vo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GuoJie
 * @date 2019/12/20 11:18
 */

@Data
public class MessagePushVo implements Serializable {

    /**
     * 推送用户id列表
     */
    private List<Long> receiveUserIds;

    /**
     * 消息发送用户id
     */
    private Long sendUserId;

    /**
     * 发送消息的内容
     */
    private JSONObject content;

    /**
     * 开始时间
     */
    private LocalDateTime pushStartTime;

    /**
     * 结束时间
     */
    private LocalDateTime pushEndTime;


}
