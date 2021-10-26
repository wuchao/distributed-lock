package com.github.wuchao.distributedlock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.UUID;

@EnableAspectJAutoProxy
@Aspect
public class DistributedLockAspect {

    @Autowired
    private AbstractDistributedLock distributedLock;

    @Around("(@annotation(com.github.wuchao.distributedlock.DistributedLock)")
    public void aroundDistributedLock(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 方法签名
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 获取方法上的 @DistributedLock 注解
        DistributedLock distributedLockAnnotation = methodSignature.getMethod().getAnnotation(DistributedLock.class);

        // 获取锁的过期时间
        long expireMilliSeconds = distributedLockAnnotation.expireMilliSeconds();

        String lockKey = methodSignature.toShortString();
        String lockVal = UUID.randomUUID().toString();

        try {
            // 加锁
            distributedLock.lock(lockKey, lockVal, expireMilliSeconds);
            // 执行业务方法
            proceedingJoinPoint.proceed();
        } finally {
            // 解锁
            distributedLock.unlock(lockKey, lockVal);
        }

    }

}
