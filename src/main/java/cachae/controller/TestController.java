package cachae.controller;

import cachae.distributedlock.DistributedLocker;
import cachae.entity.TestLockVo;
import cachae.mapper.TestLockDAO;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;

/**
 * @Description Created by zeng.yubo on 2019/8/11.
 */
@RestController
public class TestController {

    @Autowired
    TestLockDAO dao;

    @Autowired
    DistributedLocker distributedLocker;

    @RequestMapping("/test")
    public String test(){
        distributedLocker.tryLock("LK:foo",10,60, ()->{
                TestLockVo vo = dao.getVoById(1);
                int count = vo.getCountnum();
                if (count == 0) {
                    System.out.println("===================错误");
                    return ;
                } else {
                    --count;
                    System.out.println("yes");
                }
                vo.setCountnum(count);
                dao.updateVoById(vo);
        });
//        RLock lock = distributedLocker.lock("LK:foo",10);
//        lock.lock();
//        try {
//
//        }catch (Exception e){
//
//        }finally {
//            lock.unlock();
//        }
        return "success";
    }
}
