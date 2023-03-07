package com.macro.mall.tiny.common.enums;

/**
 * 这是个枚举类，包含所有的会员类型
 * @author xjp
 * @version 1.0
 * @date 2023/3/7 0007 14:28
 */
public enum AgentActivityEnum {
    GOLD_ACTIVITY(1, "goldMemberExecutor", "黄金会员活动"),
    SILVER_ACTIVITY(2, "silverMemberExecutor", "白银会员活动");
    private final Integer activityId;
    private final String beanName;
    private final String desc;
    AgentActivityEnum(Integer activityId, String beanName, String desc) {
        this.beanName = beanName;
        this.activityId = activityId;
        this.desc = desc;
    }
    public Integer getActivityId() {
        return activityId;
    }
    public String getBeanName() {
        return beanName;
    }
    public String getDesc() {
        return desc;
    }

    public static String getNameByCode(Integer activityId){
        for (AgentActivityEnum value : values()){
            if(value.getActivityId().equals(activityId)){
                return value.getBeanName();
            }
        }
        return activityId.toString();
    }
}
