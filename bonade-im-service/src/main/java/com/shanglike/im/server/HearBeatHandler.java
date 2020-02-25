package com.shanglike.im.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.spin.common.log.LoggerUtilI;

/**
 * 心跳处理
 *
 * @author YIJIUE
 */
public class HearBeatHandler extends ChannelInboundHandlerAdapter {

    LoggerUtilI LOGGER = LoggerUtilI.getLogger(HearBeatHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 读空闲
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                LOGGER.info("写空闲");
            } else if (event.state() == IdleState.ALL_IDLE) {
                LOGGER.info("读写空闲 断开连接");
                Channel channel = ctx.channel();
                channel.close();
            }
        }
    }
}
