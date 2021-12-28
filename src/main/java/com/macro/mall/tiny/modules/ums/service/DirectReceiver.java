package com.macro.mall.tiny.modules.ums.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费消息
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2021-12-27 17:16
 */
@Component
@Slf4j
public class DirectReceiver {

    //监听的队列名称 TestDirectQueue
    @RabbitListener(queues = "TestDirectQueue",containerFactory = "rabbitListener")
    @RabbitHandler
    public void process(String testMessage) {
        try {
            System.out.println("DirectReceiver消费者收到消息  : " + testMessage);
        }catch (Exception e){
            log.info("出错咯");
            e.printStackTrace();
        }


    }
    //监听的死信队列名称 order-pay-overtime-dead-queue
    @RabbitListener(queues = "order-pay-overtime-dead-queue",containerFactory = "rabbitListener")
    @RabbitHandler
    public void processDela(String testMessage) {
        try {
            System.out.println("order-pay-overtime-dead-queue消费者收到延时消息  : " + testMessage);
        }catch (Exception e){
            log.info("出错咯");
            e.printStackTrace();
        }

    }

    //监听的队列名称
    @RabbitListener(queues = "act_delay_queue",containerFactory = "rabbitListener")
    public void processDelaPlug(String testMessage) {
        try {
            System.out.println("act_delay_queue消费者收到延时插件消息  : " + testMessage);
        }catch (Exception e){
            log.info("出错咯");
            e.printStackTrace();
        }

    }




}
