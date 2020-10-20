package com.xhg;

import com.xhg.utils.SnowflakeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author xiaoh
 * @create 2020/9/11 10:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyBootApplication.class})
public class DemoTest {

    @Test
    public void SnowflakeTest(){
        System.out.println("SnowflakeID: " + SnowflakeUtil.getSnowflakeID());

    }

    @Test
    public void volatileTestDemo(){

    }

}

