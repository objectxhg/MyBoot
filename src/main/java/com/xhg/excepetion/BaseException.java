package com.xhg.excepetion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * AllArgsConstructor使用后添加一个构造函数，该构造函数含有所有已声明字段属性参数
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)

public class BaseException extends RuntimeException {

    protected Integer code;

    protected String message;
}
