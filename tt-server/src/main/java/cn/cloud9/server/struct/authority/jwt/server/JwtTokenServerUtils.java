package cn.cloud9.server.struct.authority.jwt.server;

import cn.cloud9.server.struct.authority.jwt.util.JwtHelper;
import cn.cloud9.server.struct.authority.jwt.util.JwtToken;
import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * jwt token 工具
 *
 */
@Component
@AllArgsConstructor
public class JwtTokenServerUtils {
    /**
     * 认证服务端使用，如 authority-server
     * 生成和 解析token
     */
    @Resource
    private AuthServerProperty authServerProperty;

    /**
     * 生成token
     * @param userCtx
     * @param expire
     * @return
     * @throws ServiceException
     */
    public JwtToken generateUserToken(UserContext userCtx, Integer expire) throws ServiceException {
        AuthServerProperty.TokenInfo userTokenInfo = authServerProperty.getUser();
        if (expire == null || expire <= 0) {
            expire = userTokenInfo.getExpire();
        }
        return JwtHelper.generateUserToken(userCtx, userTokenInfo.getPriKey(), expire);
    }

    /**
     * 解析token
     * @param token
     * @return
     * @throws ServiceException
     */
    public UserContext getUserContext(String token) throws ServiceException {
        AuthServerProperty.TokenInfo userTokenInfo = authServerProperty.getUser();
        return JwtHelper.getJwtFromToken(token, userTokenInfo.getPubKey());
    }
}
