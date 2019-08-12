package cachae.distributedlock;

import cachae.distributedlock.lockservice.ISynMethod;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;


public interface DistributedLocker {

    RLock lock(String lockKey, int timeout);

    void tryLock(String lockKey, int waitTime, int leaseTime, ISynMethod synMethod);

    void unlock(String lockKey);

    void unlock(RLock lock);

    void setRedissonClient(RedissonClient redissonClient);
}
