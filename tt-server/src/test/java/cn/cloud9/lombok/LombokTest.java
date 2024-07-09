package cn.cloud9.lombok;

import cn.cloud9.server.MainApplication;
import cn.cloud9.server.test.model.lombok.LombokModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月09日 下午 08:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class LombokTest {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void func() {
        final LombokModel model = new LombokModel();
        final String status = LombokModel.Fields.status;


    }

    @Test
    public void redisTest() {
        final ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        System.out.println(opsForValue);
    }
}
