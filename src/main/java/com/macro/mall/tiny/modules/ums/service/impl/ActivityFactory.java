package com.macro.mall.tiny.modules.ums.service.impl;

import com.macro.mall.tiny.common.enums.AgentActivityEnum;
import com.macro.mall.tiny.modules.ums.service.ActivityAbstractExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工厂启动时候扫入所有的会员服务
 * @author xjp
 * @version 1.0
 * @date 2023/3/7 0007 14:35
 */
@Component
@Slf4j
public class ActivityFactory {

    //通过Map注入，通过 spring bean 的名称作为key动态获取对应实例 （ActivityAbstractExecutor的全部实现都注入，key为bean名称）
    @Autowired
    private Map<String, ActivityAbstractExecutor> executorMap;

    public String execute(Integer activityId){
        // 通过会员等级获取bean名称
        String beanName = AgentActivityEnum.getNameByCode(activityId);
        if (null == beanName) {
            log.error("不存在该会员等级:{}",activityId);
            return "不存在该会员等级！";
        }
        //决定最终走哪个类的执行器（通过key获取beam实现）
        ActivityAbstractExecutor executor = executorMap.get(beanName);
        if (executor == null) {
            log.error("无策略实现:{}",beanName);
            return "无策略实现！";
        }
        return executor.executeTasks("策略（Strategy）模式+工厂（Factory）模式");
    }

}
