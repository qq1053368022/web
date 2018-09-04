package com.xull.web.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: xull
 * @date: 2018-08-30 9:05
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 声明交换器
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitConstant.EXCHANGE);
    }
    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue queueTransaction() {
        //true 表示持久化该队列
        return new Queue(RabbitConstant.QUEUE_TRANSACTION,true);
    }

    @Bean
    public Queue queueContract() {
        return new Queue(RabbitConstant.QUEUE_CONTRACT,true);
    }

    @Bean
    public Queue queueQualification() {
        return new Queue(RabbitConstant.QUEUE_QUALIFICATION,true);
    }

    /**
     * 绑定
     * @return
     */
    @Bean
    public Binding bindingTransaction() {
        return BindingBuilder.bind(queueTransaction()).to(directExchange()).with(RabbitConstant.RK_TRANSACTION);
    }

    @Bean
    public Binding bindingContract() {
        return BindingBuilder.bind(queueContract()).to(directExchange()).with(RabbitConstant.RK_CONTRACT);
    }

    @Bean
    public Binding bindingQualification() {
        return BindingBuilder.bind(queueContract()).to(directExchange()).with(RabbitConstant.RK_QUALIFICATION);
    }
}
