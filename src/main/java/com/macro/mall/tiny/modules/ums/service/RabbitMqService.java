package com.macro.mall.tiny.modules.ums.service;

/**
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2021-12-27 17:10
 */
public interface RabbitMqService {

    /**
     * 发送消息
     * @param value
     */
    String send(String value);

    /**
     * 延时发送消息
     * @param value
     * @return
     */
    String sendDela(String value);

    /**
     * 插件方式
     * @param value
     * @return
     */
    String sendDelaPlug(String value);

    /**
     * 插件方式
     * @param value
     * @return
     */
    String sendDelaPlug2(String value);
}
