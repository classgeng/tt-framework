package cn.cloud9.server.struct.constant;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 下午 01:25
 */
public interface Constant {
    String JWT_KEY_USER_ID = "USER-ID";
    String JWT_KEY_USER_NAME = "USER-NAME";
    String JWT_KEY_USER_ACCOUNT = "USER-ACCOUNT";

    String REDIS_USER_CACHE = "REDIS-USER-CACHE";
    String SEPARATOR = "@";
    String REDIS_LOGIN_LIMIT_KEY = "REDIS-LOGIN-LIMIT";
    String WRONG_PASSWORD_KEY = "HK-WRONG-PWD";
    Integer WRONG_PASSWORD_LIMIT = 5;
    Long WRONG_PWD_TTL = 60 * 5L;

    Integer RES_DONE = 1;

    String ROME_TYPE_MENU = "menu";
    String ROME_TYPE_PERMIT = "permit";
}
