package com.xhg.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @Author xiaoh
 * @create 2020/10/16 18:05
 */
public class SnowflakeUtil {

    private static Snowflake snowflake = IdUtil.getSnowflake(1, 1);


    public static Long getSnowflakeID(){

        return snowflake.nextId();
    }

}