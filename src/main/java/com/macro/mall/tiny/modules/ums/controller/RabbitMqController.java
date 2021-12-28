package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.service.RabbitMqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2021-12-27 17:20
 */

@Api(tags = "MQ")
@RestController
@RequestMapping("/mq")
public class RabbitMqController {

    @Autowired
    private RabbitMqService rabbitMqService;

    @ApiOperation(value = "发送消息")
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public CommonResult send(@RequestParam("value") String value){
        return CommonResult.success(rabbitMqService.send(value));
    }

    @ApiOperation(value = "延时发送消息")
    @RequestMapping(value = "/sendDela", method = RequestMethod.POST)
    public CommonResult sendDela(@RequestParam("value") String value){
        return CommonResult.success(rabbitMqService.sendDela(value));
    }

    @ApiOperation(value = "插件延时发送消息")
    @RequestMapping(value = "/sendDelaPlug", method = RequestMethod.POST)
    public CommonResult sendDelaPlug(@RequestParam("value") String value){
        return CommonResult.success(rabbitMqService.sendDelaPlug(value));
    }

}
