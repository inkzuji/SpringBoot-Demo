package com.zuji.common.aspect.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface PermissionData {
    /**
     * 暂时没用
     */
    String value() default "";


    /**
     * 配置菜单的组件路径,用于数据权限
     */
    String pageComponent() default "";
}