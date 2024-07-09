package cn.cloud9.server.struct.authority.interceptor.abstracted;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 下午 05:40
 */
@Slf4j
// @Component
public class ImplAuthInterceptor extends AbstractAuthInterceptor {

    @Resource
    private RedisTemplate<String, Map<String, UserContext>> userCtxTemplate;

    @Override
    public UserContext getUserContext(String tokenValue, HttpServletRequest request, HttpServletResponse response, Object handler) {
        final HashOperations<String, String, UserContext> hashOps = userCtxTemplate.opsForHash();
        final String ipAddr = IpUtil.getIpAddr(request);
        UserContext userContext = hashOps.get(Constant.REDIS_USER_CACHE, tokenValue + Constant.SEPARATOR + ipAddr);

        return userContext;
    }
}
