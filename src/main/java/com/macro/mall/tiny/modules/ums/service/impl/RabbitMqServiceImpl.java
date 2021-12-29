package com.macro.mall.tiny.modules.ums.service.impl;

import com.alibaba.fastjson.JSON;
import com.macro.mall.tiny.modules.ums.service.RabbitMqService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2021-12-27 17:12
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 简单发送消息
     * @param value
     */
    @Override
    public String send(String value) {
        String messageId =UUID.randomUUID().toString().replaceAll("-","");
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", JSON.toJSONString(map)+"===>"+value);
        return "ok";
    }

    /**
     * 延时发送消息（死信队列方式）
     * @param value
     * @return
     */
    @Override
    public String sendDela(String value) {
        String messageId =UUID.randomUUID().toString().replaceAll("-","");
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：order-pay-overtime-key 发送到交换机:order-pay-overtime-exchange,延时10s
        rabbitTemplate.convertAndSend("order-pay-overtime-exchange", "order-pay-overtime-key", JSON.toJSONString(map)+"===>"+value);
        return "ok";
    }

    /**
     * 延时发送消息（插件方式）
     * @param value
     * @return
     */
    @Override
    public String sendDelaPlug(String value) {
        String messageId =UUID.randomUUID().toString().replaceAll("-","");
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        rabbitTemplate.convertAndSend("act_delay_exchange", "act_delay_routing_key",
                JSON.toJSONString(map) + "延时5秒 ===>" + value,message -> {
            message.getMessageProperties().setHeader("x-delay",5000);
            return message;
        });
        return "ok";
    }

    /**
     * 插件方式
     * @param value
     * @return
     */
    @Override
    public String sendDelaPlug2(String value) {
        String messageId =UUID.randomUUID().toString().replaceAll("-","");
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        rabbitTemplate.convertAndSend("act_delay_exchange", "act_delay_routing_key",
                JSON.toJSONString(map) + "延时10秒 ===>" + value,message -> {
                    message.getMessageProperties().setHeader("x-delay",10000);
                    return message;
                });
        return "ok";
    }
}
