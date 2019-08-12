package cachae.mapper;

import cachae.entity.TestLockVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description Created by zeng.yubo on 2019/8/11.
 */
@Mapper
public interface TestLockDAO {
    TestLockVo getVoById(@Param("id") int id);

    void updateVoById(TestLockVo vo);
}
