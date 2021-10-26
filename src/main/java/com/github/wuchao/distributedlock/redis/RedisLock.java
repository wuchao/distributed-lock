package com.github.wuchao.distributedlock.redis;

import com.github.wuchao.distributedlock.AbstractDistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock extends AbstractDistributedLock {

    @Autowired
    private RedisTemplate redisTemplate;

    private static ValueOperations valueOpes;

    @PostConstruct
    public void init() {
        valueOpes = redisTemplate.opsForValue();
    }

    @Override
    public void lock(String lockKey, Object lockVal, long expireMilliSeconds) {
        while (!valueOpes
                .setIfAbsent(lockKey, lockVal, expireMilliSeconds, TimeUnit.MILLISECONDS)) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unlock(String lockKey, Object lockVal) {

    }

    @Override
    public boolean tryLock(String lockKey, Object lockVal, long expireMilliSeconds) {
        return valueOpes
                .setIfAbsent(lockKey, lockVal, expireMilliSeconds, TimeUnit.MILLISECONDS);
    }

}
