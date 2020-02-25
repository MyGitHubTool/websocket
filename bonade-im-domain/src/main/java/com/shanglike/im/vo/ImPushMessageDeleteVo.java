package com.shanglike.im.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author GuoJie123
 * @since 2019-12-20
 */
@Data
@ApiModel(description = "消息查询vo")
public class ImPushMessageDeleteVo implements Serializable {

    /**
     * 消息接收用户id
     */
    @ApiModelProperty(value = "推送的用户id", name = "receiveUserId", dataType = "Long")
    private Long receiveUserId;

    /**
     * 消息发送用户id
     */
    @ApiModelProperty(value = "消息发送用户id", name = "sendUserId", dataType = "Long")
    private Long sendUserId;

    /**
     * 消息id列表
     */
    @ApiModelProperty(value = "消息发送用户id", name = "messageIds", dataType = "List")
    private List<Long> messageIds;

}
