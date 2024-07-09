package cn.cloud9.server.struct.dict.cache;

import java.util.List;

/**
 * 缓存服务接口
 *
 */
public interface CacheService<CacheType> {

    void initializeCacheDataToRedis();

    String findNameFromRedis(String code);

    List<CacheType> findListFromRedis(String cate);
}
