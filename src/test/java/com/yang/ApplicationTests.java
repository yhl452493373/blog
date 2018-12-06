package com.yang;

import com.github.yhl452493373.config.CommonConfig;
import com.yang.blog.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

    @Test
    public void testFastjsonDateFormat() {
        System.out.println(CommonConfig.getDateFormat());
    }

}
