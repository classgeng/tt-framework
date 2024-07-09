package cn.cloud9.server.struct.authority.user;

/**
 * @author OnCloud9
 * @description 用户内容持有器类
 * @project tt-server
 * @date 2022年11月20日 上午 11:57
 */
public class UserContextHolder {

    /* 用户内容持有器对象 */
    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    /* 从持有器中获取用户内容对象 */
    public static UserContext getUserContext() {
        return HOLDER.get();
    }

    /* 将用户内容放入持有器对象中 */
    public static void setUserContext(UserContext userCtx) {
        HOLDER.set(userCtx);
    }

    /* 将用户内容从持有器对象中移除 */
    public static void removeUserContext() {
        HOLDER.remove();
    }
}
