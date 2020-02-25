package com.shanglike.im.config;

import com.shanglike.im.constant.RabbitMqConstant;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author GuoJie
 * @date 2019/6/24 18:31
 */
@Configuration
public class RabbitMqConfig {


    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitMqConstant.FANOUT_EXCHANGE_NAME, true, false);
    }


}
