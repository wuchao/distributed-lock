package com.github.wuchao.distributedlock;

public abstract class AbstractDistributedLock {

    public abstract void lock(String lockKey, Object lockVal, long expireMilliSeconds);

    public abstract void unlock(String lockKey, Object lockVal);

    public abstract boolean tryLock(String lockKey, Object lockVal, long expireMilliSeconds);

}
