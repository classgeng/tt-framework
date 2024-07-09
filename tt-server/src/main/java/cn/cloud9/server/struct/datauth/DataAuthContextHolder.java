package cn.cloud9.server.struct.datauth;

import java.util.Objects;

/**
 * @author OnCloud9
 * @description 数据权限持有器类
 * @project tt-server
 * @date 2022年12月20日 下午 07:24
 */
public class DataAuthContextHolder {
    private static final ThreadLocal<DataAuth> HOLDER = new ThreadLocal<>();

    /**
     * 获取授权对象
     * @return 授权对象
     */
    public static DataAuth get() {
        DataAuth dataAuth = HOLDER.get();
        if (Objects.isNull(dataAuth)) {
            dataAuth = new DataAuth();
            HOLDER.set(dataAuth);
        }
        return dataAuth;
    }

    /**
     * 放置授权对象
     * @param dataAuth 授权对象
     */
    public static void set(DataAuth dataAuth) {
        HOLDER.set(dataAuth);
    }

    /**
     * 移除授权对象
     */
    public static void remove() {
        HOLDER.remove();
    }
}
