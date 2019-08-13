package com.eelve.redissionlock.distributedlock;

import com.eelve.redissionlock.distributedlock.lockservice.ISynMethod;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @Description Created by zeng.yubo on 2019/8/7.
 */
public class RedissonDistributedLocker implements DistributedLocker {
    private RedissonClient redissonClient;


    @Override
    public RLock lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getFairLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    /**
     *
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @return
     */
    @Override
    public void tryLock(String lockKey, int waitTime, int leaseTime, ISynMethod synMethod) {
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            while(true) {
                if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                    synMethod.invoke();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 加锁操作(tryLock锁，有等待时间）
     * @param lockKey   锁名称
     * @param leaseTime  锁有效时间
     * @param waitTime   等待时间
     */
    public boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock rLock = redissonClient.getLock(lockKey);
        boolean getLock;
        try {
            getLock = rLock.tryLock( waitTime,leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 判断该线程是否持有当前锁
     * @param lockName  锁名称
     */
    public boolean isHeldByCurrentThread(String lockName) {
        RLock rLock = redissonClient.getLock(lockName);
        return rLock.isHeldByCurrentThread();
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
