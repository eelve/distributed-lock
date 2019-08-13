package cachae.controller;

import cachae.distributedlock.DistributedLocker;
import cachae.distributedlock.annotation.Lock;
import cachae.entity.TestLockVo;
import cachae.mapper.TestLockDAO;
import cachae.service.testAnnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description Created by zeng.yubo on 2019/8/11.
 */
@RestController
public class TestController {

    @Autowired
    TestLockDAO dao;

    @Autowired
    DistributedLocker distributedLocker;

    @Autowired
    testAnnoService testAnnoService;

    @RequestMapping("/test")
    public String test(){

        return testAnnoService.testAnno();


//        distributedLocker.tryLock("LK:foo",10,60, ()->{
//                TestLockVo vo = dao.getVoById(1);
//                int count = vo.getCountnum();
//                if (count == 0) {
//                    System.out.println("===================错误");
//                    return ;
//                } else {
//                    --count;
//                    System.out.println("yes");
//                }
//                vo.setCountnum(count);
//                dao.updateVoById(vo);
//        });
//        RLock lock = distributedLocker.lock("LK:foo",10);
//        lock.lock();
//        try {
//
//        }catch (Exception e){
//
//        }finally {
//            lock.unlock();
//        }
    }
}
