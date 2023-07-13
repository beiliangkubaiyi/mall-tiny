package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.TestOrder;
import com.macro.mall.tiny.modules.ums.service.TestOrderService;
import com.macro.mall.tiny.modules.ums.service.impl.ActivityFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2021-12-27 17:20
 */

@Api(tags = "分布式锁测试")
@RestController
@Slf4j
@RequestMapping("/lock")
public class TestController {


    @Resource
    private TestOrderService testOrderService;

    @Autowired
    private ActivityFactory activityFactory;

    @ApiOperation(value = "测试分布式锁（无事务）")
    @PostMapping(value = "/redissonLock")
    public CommonResult<Boolean> redissonLock(@RequestBody TestOrder testOrder){
        for (int i = 0; i < 200; i++) {
            new Thread(() ->{
                testOrderService.redissonLock(testOrder);
            }).start();
        }
        return CommonResult.success(true);
    }

    @ApiOperation(value = "测试分布式锁（有事务）")
    @PostMapping(value = "/redissonLockWork")
    public CommonResult<Boolean> redissonLockWork(@RequestBody TestOrder testOrder){
        for (int i = 0; i < 200; i++) {
            new Thread(() ->{
                testOrderService.redissonLockWork(testOrder);
            }).start();
        }
        return CommonResult.success(true);
    }

    @ApiOperation(value = "测试分布式锁（aop）")
    @PostMapping(value = "/redissonLockAop")
    public CommonResult<Boolean> redissonLockAop(@RequestBody TestOrder testOrder){
        for (int i = 0; i < 200; i++) {
            new Thread(() ->{
                testOrderService.redissonLockAop(testOrder);
            }).start();
        }
        return CommonResult.success(true);
    }

    @ApiOperation(value = "模式+工厂模式")
    @GetMapping(value = "/activityFactory")
    public CommonResult<Boolean> activityFactory(){
        activityFactory.execute(1);
        activityFactory.execute(2);
        activityFactory.execute(3);
        return CommonResult.success(true);
    }





}
