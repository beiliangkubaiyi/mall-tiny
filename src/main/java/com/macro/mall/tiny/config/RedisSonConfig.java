package com.macro.mall.tiny.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Mr.Xiao
 * @version 1.0
 * @createdate 2022-01-05 11:20
 */
@Configuration
@Slf4j
public class RedisSonConfig {

    @Bean
    public RedissonClient getRedissionClient(){
        // 1. RedisSon
        Config config = new Config();
        //单机版
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        // 2.构造RedissonClient
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
