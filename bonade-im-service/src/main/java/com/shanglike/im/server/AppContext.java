package com.shanglike.im.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 初始化netty 服务关闭netty
 *
 * @author GuoJie
 */
@Component
public class AppContext {

    private final Logger logger = LoggerFactory.getLogger(AppContext.class);

    @Autowired
    private NettyServer nettyServer;

    private Thread nettyThread;

    /**
     * 程序启动完成之后 启动一个线程来启动netty 服务器
     */
    @PostConstruct
    public void init() {
        nettyThread = new Thread(nettyServer);
        logger.info("开启独立线程，启动Netty WebSocket服务器...");
        nettyThread.start();
    }

    /**
     * 描述：Tomcat服务器关闭前需要手动关闭Netty Websocket相关资源，否则会造成内存泄漏。
     * 1. 释放Netty Websocket相关连接；
     * 2. 关闭Netty Websocket服务器线程。（强行关闭，是否有必要？）
     */
    @PreDestroy
    public void close() {
        logger.info("正在释放Netty Websocket相关连接...");
        nettyServer.close();
        logger.info("正在关闭Netty Websocket服务器线程...");
        nettyThread.stop();
        logger.info("系统成功关闭！");
    }

}
