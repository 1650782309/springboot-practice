package com.example.common.Cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//缓存切面
public @interface Cache {

    long expire() default 1 * 60 * 1000;

    String name() default "";

}