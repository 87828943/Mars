package com.mars.common.aop;


import java.lang.annotation.*;


/*
* aop日志管理
* */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LoggerAnnotation {

    public String desc() default "未知操作";
}
