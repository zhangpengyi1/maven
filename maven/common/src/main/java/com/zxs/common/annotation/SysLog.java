package com.zxs.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统日志注解
 * @SysLog(value = "查询列表", params = false, storage = true)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    // 接口的说明
    String value() default "";

    // 是否在控制台展示参数
    boolean params() default true;

    // 是否存储日志信息
    boolean storage() default false;
}
