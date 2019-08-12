package cachae.distributedlock;

import cachae.distributedlock.lockservice.ISynMethod;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

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
                    Thread.sleep(5000);
                    return;
                }
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!没拿到");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
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
