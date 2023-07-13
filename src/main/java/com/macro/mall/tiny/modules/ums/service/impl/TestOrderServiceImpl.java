package com.macro.mall.tiny.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.mapper.*;
import com.macro.mall.tiny.modules.ums.model.*;
import com.macro.mall.tiny.modules.ums.service.TestOrderService;
import com.macro.mall.tiny.security.annotation.ApoLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.Key;
import java.util.concurrent.TimeUnit;

/**
 * 后台管理员管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
@Slf4j
public class TestOrderServiceImpl extends ServiceImpl<TestOrderMapper,TestOrder> implements TestOrderService {

    @Resource
    private TestOrderService testOrderService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public Boolean redissonLock(TestOrder order) {
        try {
            RLock lock = redissonClient.getLock(order.getId().toString());
            if(!lock.tryLock(5,TimeUnit.SECONDS)){
                throw new RuntimeException("请稍后重试！");
            }
            try {
                LambdaQueryWrapper<TestOrder> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TestOrder::getId,order.getId());
                // 获取商品库存
                TestOrder testOrder = testOrderService.getOne(queryWrapper);
                if(testOrder.getNum() > 0){
                    log.info("抢到了iPhone 14 Pro Max ！");
                    // 减库存
                    lambdaUpdate().eq(TestOrder::getId,testOrder.getId()).set(TestOrder::getNum,testOrder.getNum() - 1).update();
                }else {
                    log.info("很遗憾没有抢到哇，可惜可惜！");
                }
                return true;
            }finally {
                lock.unlock();
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Boolean redissonLockWork(TestOrder order) {
        try {
            RLock lock = redissonClient.getLock(order.getId().toString());
            if(!lock.tryLock(5,TimeUnit.SECONDS)){
                throw new RuntimeException("请稍后重试！");
            }
            try {
                LambdaQueryWrapper<TestOrder> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TestOrder::getId,order.getId());
                // 获取商品库存
                TestOrder testOrder = testOrderService.getOne(queryWrapper);
                if(testOrder.getNum() > 0){
                    log.info("抢到了iPhone 14 Pro Max ！");
                    // 减库存
                    lambdaUpdate().eq(TestOrder::getId,testOrder.getId()).set(TestOrder::getNum,testOrder.getNum() - 1).update();
                }else {
                    log.info("很遗憾没有抢到哇，可惜可惜！");
                }
                return true;
            }finally {
                lock.unlock();
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    @ApoLock(key = "#order.id",waitTime = 5000)
    public Boolean redissonLockAop(TestOrder order) {
        LambdaQueryWrapper<TestOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TestOrder::getId,order.getId());
        // 获取商品库存
        TestOrder testOrder = testOrderService.getOne(queryWrapper);
        if(testOrder.getNum() > 0){
            log.info("抢到了iPhone 14 Pro Max ！");
            // 减库存
            lambdaUpdate().eq(TestOrder::getId,testOrder.getId()).set(TestOrder::getNum,testOrder.getNum() - 1).update();
        }else {
            log.info("很遗憾没有抢到哇，可惜可惜！");
        }
        return true;
    }
}
