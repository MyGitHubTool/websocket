package com.shanglike.im.controller;


import com.shanglike.im.service.ImPushMessageService;
import com.shanglike.im.vo.ImPushMessageAddVo;
import com.shanglike.im.vo.ImPushMessageDeleteVo;
import com.shanglike.im.vo.ImPushMessageQueryVo;
import com.shanglike.im.vo.ImPushMessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spin.common.web.annotation.PostApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author GuoJie123
 * @since 2019-12-20
 */
@RestController
@RequestMapping("v1/imPushMessage")
@Api(value = "前端调用推送消息接口", tags = "前端调用推送消息接口")
public class ImPushMessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImPushMessageController.class);


    @Autowired
    private ImPushMessageService imPushMessageService;


    @PostApi(value = "/add", auth = false)
    @ApiOperation(value = "新增推送消息", notes = "新增推送消息")
    public void addPushMessage(@Validated @RequestBody ImPushMessageAddVo imPushMessageAddVo) {
        LOGGER.info("ImPushMessageController ImPushMessageAddVo  imPushMessageAddVo : {}", imPushMessageAddVo);
        imPushMessageService.addPushMessage(imPushMessageAddVo);
    }

    @PostApi(value = "/history", auth = false)
    @ApiOperation(value = "查询消息历史", notes = "查询消息历史")
    public List<ImPushMessageVo> messageHistory(@Validated @RequestBody ImPushMessageQueryVo imPushMessageQueryVo) {
        LOGGER.info("ImPushMessageController history  imPushMessageQueryVo : {}", imPushMessageQueryVo);
        return imPushMessageService.messageHistory(imPushMessageQueryVo);
    }

    @PostApi(value = "/deleteHistory", auth = false)
    @ApiOperation(value = "删除聊天记录", notes = "删除聊天记录")
    public void deleteHistory(@Validated @RequestBody ImPushMessageDeleteVo imPushMessageDeleteVo) {
        LOGGER.info("ImPushMessageController deleteHistory  imPushMessageDeleteVo : {}", imPushMessageDeleteVo);
        imPushMessageService.deleteHistory(imPushMessageDeleteVo);
    }


    @PostApi(value = "/deleteMessage", auth = false)
    @ApiOperation(value = "删除单条或多条聊天记录", notes = "单条或多条删除聊天记录")
    public void deleteMessage(@Validated @RequestBody ImPushMessageDeleteVo imPushMessageDeleteVo) {
        LOGGER.info("ImPushMessageController deleteMessage  imPushMessageDeleteVo : {}", imPushMessageDeleteVo);
        imPushMessageService.deleteMessage(imPushMessageDeleteVo);
    }


}

