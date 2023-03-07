package com.macro.mall.tiny.modules.ums.service.impl;

import com.macro.mall.tiny.modules.ums.service.ActivityAbstractExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 白银会员
 * @author xjp
 * @version 1.0
 * @date 2023/3/7 0007 14:23
 */
@Service
@Slf4j
public class SilverMemberExecutor implements ActivityAbstractExecutor {
    @Override
    public String executeTasks(String params) {
        log.info("白银会员9折：{}",params);
        return "白银会员9折";
    }
}
