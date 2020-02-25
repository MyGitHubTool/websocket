package com.shanglike.im.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 全局的ChannelGroup
 *
 * @author GuoJie
 * @date 2019/7/9 11:42
 */

public class MyChannelHandlerPool {

    public MyChannelHandlerPool() {
    }

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
