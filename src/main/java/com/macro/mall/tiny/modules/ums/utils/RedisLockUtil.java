package com.macro.mall.tiny.modules.ums.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2022-01-04 14:39
 */
@Slf4j
public class RedisLockUtil {

    private static final long SUCCESS = 1L;

    //获取锁的超时时间
    private static final long TIMEOUT = 9999;





    /**
     * 获取锁
     * @param redisTemplate
     * @param lockKey
     * @param value
     * @param start
     * @return
     */
    public static boolean getLock(RedisTemplate<String,Object> redisTemplate,String lockKey, String value,Long start) {
        Boolean ret = redisTemplate.opsForValue().setIfAbsent(lockKey, value, 2, TimeUnit.MINUTES);
        if(ret){
            log.info("获取锁成功，{},",lockKey);
            return true;
        }
        Long stats = System.currentTimeMillis();
        long end = stats - start;
        if(end >= TIMEOUT){
            return false;
        }
        return getLock(redisTemplate,lockKey, value,start);
    }

    /**
     * 释放锁
     * @param lockKey
     * @param value
     * @return
     */
    public static boolean releaseLock(RedisTemplate<String,Object> redisTemplate,String lockKey, String value){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Object result = redisTemplate.execute(redisScript,Collections.singletonList(lockKey),value);
        return result.equals(SUCCESS);
    }








}
