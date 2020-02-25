package com.shanglike.im.constant;

/**
 * @author GuoJie
 * @date 2019/12/30 11:44
 */

public class MessageTypeConstant {


    /**
     * 100 通道打开时 连接websocket
     */
    public static final int LINKED_WEBSOCKET_MESSAGE_TYPE = 100;
    /**
     * 101 发送心跳请求
     */
    public static final int HEART_BEAT_MESSAGE_TYPE = 101;

    /**
     * 102 发送单条消息
     */
    public static final int SEND_SINGLE_MESSAGE_TYPE = 102;

    /**
     * 103 退出登录 关闭连接
     */
    public static final int LOGIN_OUT = 103;

    /**
     * 104 消息撤回
     */
    public static final int WITH_DRAW_MESSAGE = 104;

}
