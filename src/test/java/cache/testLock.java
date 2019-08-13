package cache;

import com.eelve.redissionlock.RedissonTestApplication;
import com.eelve.redissionlock.distributedlock.DistributedLocker;
import com.eelve.redissionlock.mapper.TestLockDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Description Created by zeng.yubo on 2019/8/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedissonTestApplication.class)
public class testLock {

    @Autowired
    TestLockDAO dao;

    @Autowired
    DistributedLocker distributedLocker;

//    RedissLockUtil lockManager = new RedissLockUtil();

    private long counter = 0;

    @Test
    public void testLock() throws InterruptedException {
        counter = 0;
//        Lock lock = lockManager.lock("LK:foo");
        int threadNum = 100;
        int iterationNum = 101;
        for (int i1 = 0; i1 < iterationNum; i1++) {
            distributedLocker.tryLock("LK:foo",10,60, ()->{
                counter++;
            });
        }
//        CountDownLatch latch = new CountDownLatch(threadNum);

//        for (int i = 0; i < threadNum; i++) {
//            new Thread(() -> {
//
//
//                latch.countDown();
//            }).start();
//        }
//        latch.await();
        System.out.println(counter + "|" +threadNum * iterationNum);
        Assert.assertEquals(counter, threadNum * iterationNum);
    }
}
