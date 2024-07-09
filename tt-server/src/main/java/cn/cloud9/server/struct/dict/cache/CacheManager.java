package cn.cloud9.server.struct.dict.cache;

import cn.cloud9.server.struct.enums.EnumUtil;
import cn.cloud9.server.struct.spring.SpringContextHolder;

/**
 * 缓存管理器类，用于刷新缓存
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月13日 下午 11:08
 */
@SuppressWarnings("rawtypes")
public class CacheManager {

    public static void refreshCache() {
        final CacheService cacheService = SpringContextHolder.getBean("dictService", CacheService.class);
        new Thread(() -> {
            cacheService.initializeCacheDataToRedis();
            EnumUtil.initialize();
        }).start();
    }
}
