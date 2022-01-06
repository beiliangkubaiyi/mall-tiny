package com.macro.mall.tiny.modules.ums.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.ums.service.RabbitMqService;
import com.macro.mall.tiny.modules.ums.utils.RedisLockUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveStreamCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2021-12-27 17:20
 */

@Api(tags = "MQ")
@RestController
@Slf4j
@RequestMapping("/mq")
public class RabbitMqController {

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

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

    @ApiOperation(value = "插件延时发送消息5秒")
    @RequestMapping(value = "/sendDelaPlug", method = RequestMethod.POST)
    public CommonResult sendDelaPlug(@RequestParam("value") String value){
        return CommonResult.success(rabbitMqService.sendDelaPlug(value));
    }

    @ApiOperation(value = "插件延时发送消息10秒")
    @RequestMapping(value = "/sendDelaPlug2", method = RequestMethod.POST)
    public CommonResult sendDelaPlug2(@RequestParam("value") String value){
        return CommonResult.success(rabbitMqService.sendDelaPlug2(value));
    }

    @ApiOperation(value = "获取锁")
    @RequestMapping(value = "/getLock", method = RequestMethod.POST)
    public CommonResult getLock(){
        return CommonResult.success(RedisLockUtil.getLock(redisTemplate,"key","test", System.currentTimeMillis()));
    }

    @ApiOperation(value = "释放锁")
    @RequestMapping(value = "/releaseLock", method = RequestMethod.POST)
    public CommonResult releaseLock(){
        return CommonResult.success(RedisLockUtil.releaseLock(redisTemplate,"key","test"));
    }

    @ApiOperation(value = "加商品库存")
    @RequestMapping(value = "/addStock",method = RequestMethod.POST)
    public CommonResult addStock(@RequestParam("num") Integer num){
        redisService.set("stock",num);
        log.info("添加商品库存商品：{}",num);
        return CommonResult.success(num);
    }


    @ApiOperation(value = "购买商品减库存（分布式锁防止超卖）")
    @RequestMapping(value = "/killStock",method = RequestMethod.POST)
    public CommonResult killStock(){
        String key = "lock_key";
        RLock lock = redissonClient.getLock(key);
        try {
            //加锁，的有效期默认30秒，获取锁，不成功则订阅释放锁的消息，获得消息前阻塞。得到释放通知后再去循环获取锁。
            lock.lock();
            Object stock = redisService.get("stock");
            if(null == stock){
                return CommonResult.failed("获取库存异常");
            }
            Integer num = Integer.valueOf(stock.toString());
            if(num <= 0){
                log.info(Thread.currentThread().getName()+"商品库存已售罄！");
            }else{
                //减库存操作
                TimeUnit.MILLISECONDS.sleep(100);
                log.info(Thread.currentThread().getName()+"当前库存库存数量：{}",num);
                redisService.set("stock",num - 1);
            }
        }catch (Exception e){
            log.error("出错了："+e.getMessage());
        }finally {
            //最后都要释放锁
            lock.unlock();
            log.info("------释放锁===>"+ Thread.currentThread().getName());
        }
        return CommonResult.success("ok");
    }

    @ApiOperation(value = "购买商品减库存")
    @RequestMapping(value = "/killStock2",method = RequestMethod.POST)
    public CommonResult killStock2() throws InterruptedException {
        Object stock = redisService.get("stock");
        if(null == stock){
            return CommonResult.failed("获取库存异常");
        }
        Integer num = Integer.valueOf(stock.toString());
        if(num <= 0){
            log.info("商品库存已售罄！");
        }else{
            TimeUnit.MILLISECONDS.sleep(100);
            //减库存操作
            log.info(Thread.currentThread().getName()+"：获取当前库存库存数量：{}",num);
            redisService.set("stock",num - 1);

        }

        return CommonResult.success("ok");
    }



}
