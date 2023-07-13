package com.macro.mall.tiny.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 * @author xjp
 * @version 1.0
 * @date 2023/7/6 0006 16:47
 */
@Retention(RetentionPolicy.RUNTIME)//运行时生效
@Target(ElementType.METHOD)//作用在方法上
public @interface ApoLock {

    /**
     * 说明
     * @return
     */
    String description()  default "";

    /**
     * key的前缀
     * @return
     */
    String prefixKey() default "";

    /**
     * springEl7 key表达式  单参数 id = #id ，json参数 id = #json.id
     *
     * @return key表达式
     */
    String key();
    /**
     * 等待锁的时间，默认-1，不等待直接失败,redisson默认也是-1
     *
     * @return 单位秒
     */
    int waitTime() default -1;

    /**
     * 等待锁的时间单位，默认毫秒
     *
     * @return 单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

}
