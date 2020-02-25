package com.shanglike.im.vo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author GuoJie123
 * @since 2019-12-20
 */
@Data
@ApiModel(description = "推送返回给前端的数据")
public class ImPushMessageVo implements Serializable {


    private Long id;

    /**
     * 消息接收用户id
     */

    private Long receiveUserId;

    /**
     * 消息发送用户id
     */

    private Long sendUserId;


    /**
     * 推送的内容
     */
    private JSONObject content;
    /**
     * 推送的开始时间
     */

    private LocalDateTime pushStartTime;
    /**
     * 推送的结束时间
     */

    private LocalDateTime pushEndTime;
    /**
     * 状态  0.未推送 1.已推送 2.已失效
     */
    private Integer state;
    /**
     * 创建人ID
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新人ID
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 默认为1(1:未删除,0:删除)
     */
    private Integer deleted;


    /**
     * 消息类型 1.聊天消息 2.推送消息
     */
    private Integer msgType;

    /**
     * 消息内容类型 1.文本 2.图片 3.文件
     */
    private Integer contentType;

    /**
     * 聊天是否可见 1.可见 0. 不可见
     */
    private Integer chatVisible;

    /**
     * 文本内容
     */
    private String contentText;

    /**
     * 创建时间的时间戳
     */
    private Long createTimeTimestamp;

    /**
     * 消息类型  101.发送心跳请求 101  102.发送单条消息
     */
    @ApiModelProperty(value = "消息类型", name = "type", dataType = "Integer")
    private Integer type;

    /**
     * 推送标识 1.只会发送给接收者 2.接受者和发送者都会接收到消息
     */
    private Integer pushFlag = 2;


    /**
     * 是否撤回的消息 1.是 0.否
     */
    @ApiModelProperty(value = "是否撤回的消息 1.是 0.否", name = "retracted", dataType = "Integer")
    private Integer retracted;


}
