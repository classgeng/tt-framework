package cn.cloud9.server.struct.authority.jwt.util;


import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.exception.ServiceException;
import cn.cloud9.server.struct.util.DateUtils;
import cn.cloud9.server.struct.util.StringHelper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

@Slf4j
public class JwtHelper {

    /**
     * 生成用户token
     * @param userCtx 当前用户上下文
     * @param priKeyPath 私钥路径
     * @param expire 过期时间
     * @return 令牌对象
     */
    public static JwtToken generateUserToken(UserContext userCtx, String priKeyPath, int expire) {
        JwtBuilder jwtBuilder = Jwts.builder()
                //设置主题
                .setSubject(String.valueOf(userCtx.getUserId()))
                .claim(Constant.JWT_KEY_USER_ID, userCtx.getUserId())
                .claim(Constant.JWT_KEY_USER_NAME, userCtx.getUserName())
                .claim(Constant.JWT_KEY_USER_ACCOUNT, userCtx.getUserAccount());
        return generateToken(jwtBuilder, priKeyPath, expire);
    }

    /**
     * 获取token中的用户信息
     * @param token 令牌字符串
     * @param pubKeyPath 公钥路径
     * @return 当前用户上下文
     */
    public static UserContext getJwtFromToken(String token, String pubKeyPath) {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        String userId = body.getSubject();
        String userAccount = StringHelper.getObjectValue(body.get(Constant.JWT_KEY_USER_ACCOUNT));
        String userName = StringHelper.getObjectValue(body.get(Constant.JWT_KEY_USER_NAME));

        Long userIdLongVal = Long.valueOf(userId);

        return new UserContext(userIdLongVal, userName, userAccount);
    }

    /**
     * 生成token
     * @param builder 令牌构造器
     * @param priKeyPath 私钥路径
     * @param expire 过期时间
     * @return 令牌对象
     */
    protected static JwtToken generateToken(JwtBuilder builder, String priKeyPath, int expire) {
        try {
            //返回的字符串便是我们的jwt串了
            String compactJws = builder.setExpiration(DateUtils.localDateTime2Date(LocalDateTime.now().plusSeconds(expire)))
                    //设置算法（必须）
                    .signWith(SignatureAlgorithm.RS256, RsaKeyHelper.getPrivateKey(priKeyPath))
                    //这个是全部设置完成后拼成jwt串的方法
                    .compact();
            return new JwtToken(compactJws, expire);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("errcode:{}, message:{}", ResultMessage.JWT_GEN_TOKEN_FAIL.getCode(), e.getMessage());
            throw new ServiceException(ResultMessage.JWT_GEN_TOKEN_FAIL);
        }
    }

    /**
     * 公钥解析token
     * @param token 令牌字符串
     * @param pubKeyPath 公钥路径
     * @return Jws<Claims> 对象
     * @throws ServiceException JWT异常
     */
    private static Jws<Claims> parserToken(String token, String pubKeyPath) throws ServiceException {
        try {
            return Jwts.parser().setSigningKey(RsaKeyHelper.getPublicKey(pubKeyPath)).parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            //过期
            throw new ServiceException(ResultMessage.JWT_TOKEN_EXPIRED);
        } catch (SignatureException ex) {
            //签名错误
            throw new ServiceException(ResultMessage.JWT_SIGNATURE);
        } catch (IllegalArgumentException ex) {
            //token 为空
            throw new ServiceException(ResultMessage.JWT_ILLEGAL_ARGUMENT);
        } catch (Exception e) {
            log.error("errCode:{}, message:{}", ResultMessage.JWT_PARSER_TOKEN_FAIL.getCode(), e.getMessage());
            throw new ServiceException(ResultMessage.JWT_PARSER_TOKEN_FAIL);
        }
    }
}
