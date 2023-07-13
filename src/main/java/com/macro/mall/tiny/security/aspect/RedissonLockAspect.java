package com.macro.mall.tiny.security.aspect;

import com.macro.mall.tiny.security.annotation.ApoLock;
import com.macro.mall.tiny.security.util.SpElUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

/**
 * 分布式锁切面
 * @author xjp
 * @version 1.0
 * @date 2023/7/6 0006 16:52
 */
@Slf4j
@Aspect
@Component
//确保比事务注解先执行，分布式锁在事务外
@Order(0)
public class RedissonLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(com.macro.mall.tiny.security.annotation.ApoLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ApoLock redissonLock = method.getAnnotation(ApoLock.class);
        // 获取key
        String key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        RLock lock = redissonClient.getLock(redissonLock.prefixKey() + key);
        boolean lockSuccess = lock.tryLock(redissonLock.waitTime(), redissonLock.unit());
        if (!lockSuccess) {
            throw new RuntimeException("请求频繁，稍后重试！");
        }
        try {
            // 执行锁内的代码逻辑
            return joinPoint.proceed();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
