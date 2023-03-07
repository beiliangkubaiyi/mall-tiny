package com.macro.mall.tiny.modules.ums.service.impl;

import com.macro.mall.tiny.modules.ums.service.ActivityAbstractExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 黄金会员
 * @author xjp
 * @version 1.0
 * @date 2023/3/7 0007 14:20
 */
@Service
@Slf4j
public class GoldMemberExecutor implements ActivityAbstractExecutor {
    @Override
    public String executeTasks(String params) {
        log.info("黄金会员8折：{}",params);
        return "黄金会员8折";
    }
}
