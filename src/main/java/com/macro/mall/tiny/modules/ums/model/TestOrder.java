package com.macro.mall.tiny.modules.ums.model;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xjp
 * @version 1.0
 * @date 2023/7/11 0011 16:13
 */
@Data
public class TestOrder implements Serializable {
    private Long id;
    private Integer num;
}
