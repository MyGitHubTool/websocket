package com.shanglike.im.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author GuoJie
 * @date 2019/12/18 11:05
 */
@Data
@ApiModel(description = "消息推送")
public class WebSocketRequestVo implements Serializable {

    /**
     * 消息接收用户id
     */
    @ApiModelProperty(value = "推送的用户id", name = "receiveUserId", required = true, dataType = "Long")
    private Long receiveUserId;

    /**
     * 消息发送用户id
     */
    @ApiModelProperty(value = "消息发送用户id", name = "sendUserId", dataType = "Long")
    private Long sendUserId;
    /**
     * 推送的内容
     */
    @ApiModelProperty(value = "推送的内容 自己封装", name = "content", dataType = "JSONObject")
    private JSONObject content;

    /**
     * 注册类型  100.连接上注册  101.心跳消息  102.单聊  103.断开连接 104.撤回消息
     */
    private Integer type;
    /**
     * 消息类型 1.聊天消息 2.推送消息
     */
    @ApiModelProperty(value = "消息类型 1.聊天消息 2.推送消息", name = "msgType", dataType = "Integer")
    private Integer msgType;

    /**
     * 消息内容类型 1.文本 2.图片 3.文件
     */
    @ApiModelProperty(value = "消息内容类型 1.文本 2.图片 3.文件", name = "contentType", dataType = "Integer")
    private Integer contentType;

    /**
     * 聊天是否可见 1.可见 0. 不可见
     */
    @ApiModelProperty(value = "聊天是否可见 1.可见 0. 不可见", name = "chatVisible", dataType = "Integer")
    private Integer chatVisible;
    /**
     * 消息id
     */
    @ApiModelProperty(value = "消息id", name = "messageId", dataType = "Long")
    private Long messageId;


}
