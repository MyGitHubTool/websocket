package com.shanglike.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author GuoJie123
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("im_push_message")
public class ImPushMessage extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 消息接收用户id
     */
    @TableField("receive_user_id")
    private Long receiveUserId;

    /**
     * 消息发送用户id
     */
    @TableField("send_user_id")
    private Long sendUserId;

    /**
     * 推送的内容
     */
    private String content;
    /**
     * 推送的开始时间
     */
    @TableField("push_start_time")
    private LocalDateTime pushStartTime;
    /**
     * 推送的结束时间
     */
    @TableField("push_end_time")
    private LocalDateTime pushEndTime;
    /**
     * 状态  0.未推送 1.已推送 2.已失效
     */
    private Integer state;
    /**
     * 创建人ID
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新人ID
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    /**
     * 默认为1(1:未删除,0:删除)
     */
    private Integer deleted;
    /**
     * 版本号
     */
    private Integer version;

    /**
     * 消息类型 1.聊天消息 2.推送消息
     */
    @TableField("msg_type")
    private Integer msgType;

    /**
     * 消息内容类型 1.文本 2.图片 3.文件
     */
    @TableField("content_type")
    private Integer contentType;

    /**
     * 聊天是否可见 1.可见 0. 不可见
     */
    @TableField("chat_visible")
    private Integer chatVisible;

    /**
     * 创建时间时间戳
     */
    @TableField("create_time_timestamp")
    private Long createTimeTimestamp;

    /**
     * 发送方聊天是否可见 1.可见 0. 不可见
     */
    @TableField("send_visible")
    private Integer sendVisible;


    /**
     * 接收方聊天是否可见 1.可见 0. 不可见
     */
    @TableField("receive_visible")
    private Integer receiveVisible;

    /**
     * 是否撤回的消息 1.是 0.否
     */
    @TableField("retracted")
    private Integer retracted;





}
