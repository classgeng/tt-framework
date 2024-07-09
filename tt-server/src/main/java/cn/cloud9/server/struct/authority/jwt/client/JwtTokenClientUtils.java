package cn.cloud9.server.struct.authority.jwt.client;

import cn.cloud9.server.struct.authority.jwt.util.JwtHelper;
import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * JwtToken 客户端工具
 *
 */
@Component
@AllArgsConstructor
public class JwtTokenClientUtils {
    /**
     * 用于 认证服务的 客户端使用（如 网关） ， 在网关获取到token后，
     * 调用此工具类进行token 解析。
     * 客户端一般只需要解析token 即可
     */
    @Resource
    private AuthClientProperty authClientProperty;

    /**
     * 解析token
     * @param token
     * @return
     * @throws ServiceException
     */
    public UserContext getUserContext(String token) throws ServiceException {
        AuthClientProperty.TokenInfo userTokenInfo = authClientProperty.getUser();
        return JwtHelper.getJwtFromToken(token, userTokenInfo.getPubKey());
    }
}
