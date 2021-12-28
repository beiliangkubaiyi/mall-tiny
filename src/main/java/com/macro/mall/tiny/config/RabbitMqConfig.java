package com.macro.mall.tiny.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * MQ配置
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2021-12-27 16:59
 */
@Configuration
public class RabbitMqConfig {


//    # rabbitmq订单支付超时队列名
//    rabbitmq.order.pay.overtime.queue.name=order-pay-overtime-queue
//    # rabbitmq订单支付超时交换机名
//    rabbitmq.order.pay.overtime.exchange.name=order-pay-overtime-exchange
//    # rabbitmq订单支付超时路由键名
//    rabbitmq.order.pay.overtime.binding.key=order-pay-overtime-key
//
//    # rabbitmq订单支付超时死信队列名
//    rabbitmq.order.pay.overtime.dead.queue.name=order-pay-overtime-dead-queue
//    # rabbitmq订单支付超时死信交换机名
//    rabbitmq.order.pay.overtime.dead.exchange.name=order-pay-overtime-dead-exchange
//    # rabbitmq订单支付超时死信路由键名
//    rabbitmq.order.pay.overtime.dead.binding.key=order-pay-overtime-dead-key


    private static final String orderPayOvertimeQueueName = "order-pay-overtime-queue";
    private static final String orderPayOvertimeExchangeName = "order-pay-overtime-exchange";
    private static final String orderPayOvertimeBindingKey = "order-pay-overtime-key";

    private static final String orderPayOvertimeDeadQueueName = "order-pay-overtime-dead-queue";
    private static final String orderPayOvertimeDeadExchangeName = "order-pay-overtime-dead-exchange";
    private static final String orderPayOvertimeDeadBindingKey = "order-pay-overtime-dead-key";

    //插件方式
    public static final String ACT_DELAY_QUEUE= "act_delay_queue";//延迟消费队列
    public static final String ACT_DELAY_EXCHANGE = "act_delay_exchange"; //延迟交换器
    public static final String ACT_DELAY_ROUTING_KEY = "act_delay_routing_key";//延迟路由键


    /**
     * 序列化
     * @param connectionFactory
     * @param objectMapper
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean(name = "rabbitListener")
    public SimpleRabbitListenerContainerFactory listenerContainer(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return factory;
    }



    /**
     * 延迟队列（插件方式）
     */
    @Bean
    public Queue delayQueue() {
        return new Queue(ACT_DELAY_QUEUE, true);
    }


    /**
     * 延时交换机（插件方式）
     * @return
     */
    @Bean
    public CustomExchange orderDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(ACT_DELAY_EXCHANGE,"x-delayed-message",true, false,args);
    }

    /**
     *  绑定队列与交换机（插件方式）
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(delayQueue()).to(orderDelayExchange()).with(ACT_DELAY_ROUTING_KEY).noargs();
    }




    /**
     * 订单队列
     */
    @Bean
    public Queue orderPayOvertimeQueue() {
        HashMap<String, Object> hashMap = new HashMap<>(3);
        //定义消息存活时间10秒
        hashMap.put("x-message-ttl",10000);
        //设置此队列产生的死信会投递给死信交换机
        hashMap.put("x-dead-letter-exchange", orderPayOvertimeDeadExchangeName);
        //设置死信投递时使用的路由键
        hashMap.put("x-dead-letter-routing-key", orderPayOvertimeDeadBindingKey);
        return new Queue(orderPayOvertimeQueueName,true,false,false,hashMap);
    }


    /**
     * 订单交换机
     */
    @Bean
    public DirectExchange orderPayOvertimeExchange() {
        return new DirectExchange(orderPayOvertimeExchangeName);
    }


    /**
     * 绑定 订单队列->订单交换机
     */
    @Bean
    public Binding orderPayOvertimeBinding() {
        return BindingBuilder.bind(orderPayOvertimeQueue()).to(orderPayOvertimeExchange()).with(orderPayOvertimeBindingKey);
    }


    /**
     * 订单死信队列
     */
    @Bean
    public Queue orderPayOvertimeDeadQueue() {
        return new Queue(orderPayOvertimeDeadQueueName);
    }

    /**
     * 订单死信交换机
     */
    @Bean
    public DirectExchange  orderPayOvertimeDeadExchange() {
        return new DirectExchange(orderPayOvertimeDeadExchangeName);
    }

    /**
     * 绑定 订单死信队列->订单死信交换机
     */
    @Bean
    public Binding orderPayOvertimeDeadBinding() {
        return BindingBuilder.bind(orderPayOvertimeDeadQueue()).to(orderPayOvertimeDeadExchange()).with(orderPayOvertimeDeadBindingKey);
    }






    //队列 起名：TestDirectQueue
    @Bean
    public Queue TestDirectQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue("TestDirectQueue",true);
    }

    //Direct交换机 起名：TestDirectExchange
    @Bean
    DirectExchange TestDirectExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange("TestDirectExchange",true,false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("TestDirectRouting");
    }


}
