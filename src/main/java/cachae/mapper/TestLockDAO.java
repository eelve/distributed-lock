package cachae.mapper;

import cachae.distributedlock.annotation.Lock;
import cachae.entity.TestLockVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description Created by zeng.yubo on 2019/8/11.
 */
@Mapper
public interface TestLockDAO {

    @Lock
    TestLockVo getVoById(@Param("id") int id);

    void updateVoById(TestLockVo vo);
}
