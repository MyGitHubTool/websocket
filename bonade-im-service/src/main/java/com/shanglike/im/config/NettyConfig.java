package com.shanglike.im.config;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author GuoJie
 * @date 2019/7/5 9:42
 */
@Configuration
public class NettyConfig {

    @Bean
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public EventLoopGroup workGroup() {
        return new NioEventLoopGroup();
    }
}
