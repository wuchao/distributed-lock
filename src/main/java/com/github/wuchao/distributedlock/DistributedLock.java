package com.github.wuchao.distributedlock;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 缓存过期时间（单位 ms）
     *
     * @return
     */
    long expireMilliSeconds() default 500L;

}
