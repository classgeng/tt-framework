package cn.cloud9.server.struct.dict.service;

import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.dict.cache.CacheManager;
import cn.cloud9.server.struct.dict.cache.CacheProperty;
import cn.cloud9.server.struct.dict.cache.CacheService;
import cn.cloud9.server.struct.dict.dto.DictDTO;
import cn.cloud9.server.struct.dict.mapper.DictMapper;
import cn.cloud9.server.test.model.MyType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 字典服务
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月13日 下午 09:21
 */
@Slf4j
@Service("dictService")
public class DictService extends ServiceImpl<DictMapper, DictDTO> implements CacheService<DictDTO> {

    public static final String KEY_LISTS = "REDIS-LISTS-CACHE";
    public static final String KEY_MAP = "REDIS-MAPS-CACHE";
    public static final String SEPARATOR = "@";

    @Resource
    private CacheProperty cacheProperty;
    @Resource
    private StringRedisTemplate stringTemplate;
    @Resource
    private RedisTemplate<String, Map<String, String>> mapTemplate;


    /**
     * 缓存初始化处理
     */
    @Override
    public void initializeCacheDataToRedis() {
        final HashOperations<String, Object, Object> hashOps = mapTemplate.opsForHash();
        /* 清空缓存 */
        stringTemplate.delete(KEY_MAP);
        stringTemplate.delete(KEY_LISTS);

        /* 读取配置文件的缓存SQL */
        final Map<String, String> sqlMap = cacheProperty.getSqlMap();
        /* 准备缓存结构容器, 并装载数据 */
        Map<String, String> mapTank = new ConcurrentHashMap<>();
        Map<String, List<DictDTO>> listTank = new ConcurrentHashMap<>();
        for (String sqlKey : sqlMap.keySet()) {
            final String sql = sqlMap.get(sqlKey);
            final List<DictDTO> dictList = baseMapper.queryUsingCustomSql(sql);
            for (DictDTO dict : dictList) {
                final String dictCode = dict.getDictCode();
                final String dictName = dict.getDictName();
                final String dictType = dict.getDictCategory();

                /* 装载 key -> h-key -> h-value */
                final String mapKey = sqlKey + SEPARATOR + dictType + SEPARATOR + dictCode;
                mapTank.put(mapKey, dictName);

                /* 装载 key -> h-key -> h-list */
                final String listKey = sqlKey + SEPARATOR + dictType;
                List<DictDTO> cateList = listTank.get(listKey);
                if (CollectionUtils.isEmpty(cateList)) {
                    cateList = new ArrayList<>();
                    listTank.put(listKey, cateList);
                }
                cateList.add(dict);
            }
        }

        /* 装填到Redis中 */
        hashOps.putAll(KEY_MAP, mapTank);
        hashOps.putAll(KEY_LISTS, listTank);

        log.info("Redis 字典缓存装载完毕 ...... ");
    }

    /**
     *
     * @param dictCode 格式：sqlKey@字典类别@字典编码
     * @return 字典名称 找不到为null
     */
    @Override
    public String findNameFromRedis(String dictCode) {
        final HashOperations<String, Object, Object> hashOps = mapTemplate.opsForHash();
        final Object o = hashOps.get(KEY_MAP, dictCode);
        final boolean isEmpty = Objects.isNull(o);
        return !isEmpty ? (String) o : "";
    }

    /**
     *
     * @param dictCate 格式：sqlKey@字典类别
     * @return 字典类别集合 找不到为空集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DictDTO> findListFromRedis(String dictCate) {
        final HashOperations<String, Object, Object> hashOps = mapTemplate.opsForHash();

        final Object o = hashOps.get(KEY_LISTS, dictCate);
        final boolean isEmpty = Objects.isNull(o);
        return !isEmpty ? (List<DictDTO>) o : Collections.EMPTY_LIST;
    }


    public IPage<DictDTO> getDictPage(DictDTO dto) {
        return lambdaQuery()
            .and(StringUtils.isNotBlank(dto.getDictName()), wq -> wq.like(DictDTO::getDictName, dto.getDictName()).or().like(DictDTO::getDictCode, dto.getDictName()))
            .and(StringUtils.isNotBlank(dto.getDictCateName()), wq -> wq.like(DictDTO::getDictCateName, dto.getDictCateName()).or().like(DictDTO::getDictCategory, dto.getDictCateName()))
            .page(dto.getPage());
    }

    public DictDTO addNewDict(DictDTO dto) {
        dto.preInsert();
        final int insert = baseMapper.insert(dto);
        boolean success = Constant.RES_DONE.equals(insert);
        if (success) CacheManager.refreshCache();
        return success ? dto : null;
    }

    public boolean updateDict(DictDTO dto) {
        dto.preUpdate();
        final int update = baseMapper.updateById(dto);
        boolean success = Constant.RES_DONE.equals(update);
        if (success) CacheManager.refreshCache();
        return success;
    }

    public boolean deleteDict(DictDTO dto) {
        final int delete = baseMapper.deleteById(dto);
        boolean success = Constant.RES_DONE.equals(delete);
        if (success) CacheManager.refreshCache();
        return success;
    }
}
