package com.shanglike.im.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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
public class ImPushMessageQueryVo implements Serializable {

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
     * 查询的数量
     */
    @ApiModelProperty(value = "查询的条数", name = "count", dataType = "Integer")
    private Integer count = 20;

    /**
     * 查询时间之前的数据
     */
    @ApiModelProperty(value = "查询的结束时间", name = "endTime", dataType = "Long")
    private Long endTime;

    /**
     * messageId 之前的数据
     */
    @ApiModelProperty(value = "消息id", name = "messageId", dataType = "Long")
    private Long messageId;
}
