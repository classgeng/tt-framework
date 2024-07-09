package cn.cloud9.server.struct.authority.user;

import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.spring.SpringContextHolder;
import cn.cloud9.server.struct.util.Assert;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author OnCloud9
 * @description 密码错误工具类
 * @project tt-server
 * @date 2022年11月20日 下午 08:19
 */
public class PwdErrorUtil {

    private final static StringRedisTemplate checkTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
    private final static ValueOperations<String, String> VAL_OPS = checkTemplate.opsForValue();

    /**
     * 密码错误上限检查
     * @param username 用户名称
     */
    public static void pwdErrorCheck(String username) {
        final String hKey = Constant.WRONG_PASSWORD_KEY + Constant.SEPARATOR + username;
        final String redisCount = VAL_OPS.get(hKey);
        int count = 0;
        final boolean isNotEmpty = Objects.nonNull(redisCount);
        if (isNotEmpty) count = Integer.parseInt(redisCount);

        final boolean isExceed = count > Constant.WRONG_PASSWORD_LIMIT;
        if (isExceed) {
            final Long expire = VAL_OPS.getOperations().getExpire(hKey);
            Assert.isTrue(isExceed, ResultMessage.WRONG_PASSWORD_LIMIT, Constant.WRONG_PWD_TTL / 60L, expire);
        }
    }

    /**
     * 自增密码错误计数
     * @param username 用户名称
     */
    public static void pwdErrorCountIncrease(String username) {
        final String hKey = Constant.WRONG_PASSWORD_KEY + Constant.SEPARATOR + username;
        final String redisCount = VAL_OPS.get(hKey);
        int count = 0;
        final boolean isNotEmpty = Objects.nonNull(redisCount);
        if (isNotEmpty) count = Integer.parseInt(redisCount);
        ++ count;
        VAL_OPS.set(hKey, String.valueOf(count), Constant.WRONG_PWD_TTL, TimeUnit.SECONDS);
    }

    /**
     * 重置密码错误计数
     * @param username 用户名称
     */
    public static void resetPwdErrorCount(String username) {
        final String hKey = Constant.WRONG_PASSWORD_KEY + Constant.SEPARATOR + username;
        VAL_OPS.set(hKey, "0");
    }

}
