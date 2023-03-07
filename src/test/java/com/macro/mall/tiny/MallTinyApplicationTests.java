package com.macro.mall.tiny;

import com.macro.mall.tiny.modules.ums.service.impl.ActivityFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallTinyApplicationTests {
    @Autowired
    private ActivityFactory activityFactory;

    @Test
    public void contextLoads() {

        activityFactory.execute(1);
        activityFactory.execute(2);
        activityFactory.execute(3);
    }

}
