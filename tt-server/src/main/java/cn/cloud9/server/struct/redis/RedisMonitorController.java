package cn.cloud9.server.struct.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/redis-monitor")
public class RedisMonitorController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final List<String> infoCommands = new ArrayList<>();

    static {
        infoCommands.add("commandstats");
        infoCommands.add("keyspace");
    }

    /**
     * redis-monitor/info
     * @return Map<String, Object>
     */
    @GetMapping("/info")
    public Map<String, Object> redisInfo() {
        /* Properties info1 = redisTemplate.getRequiredConnectionFactory().getConnection().info(); */
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());
        Map<String, Object> result = new ConcurrentHashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new ConcurrentHashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return result;
    }

    /**
     * https://redis.io/commands/info
     * server: Redis服务器的一般信息
     * clients:客户端连接部分
     * memory:内存消耗相关信息
     * persistence: RDB和AOF相关信息
     * 统计:一般统计
     * replication:主复制复制信息
     * cpu: cpu消耗统计信息
     * commandstats: Redis命令统计信息
     * latencystats: Redis命令延迟百分比分布统计信息
     * cluster: Redis集群部分
     * 模块:模块部分
     * keyspace:数据库相关统计信息
     * modules:模块相关的部分
     * errorstats: Redis错误统计信息
     * 它也可以取以下值:
     * all:返回所有的section(不包括模块生成的section)
     * default:只返回默认的section集
     * everything:包括所有和模块
     * 如果不提供参数，则采用默认(default)选项。
     * @return Map<String, Object>
     */
    @GetMapping("/statistic")
    public Map<String, Object> redisStatistic(@RequestParam(value = "key") String key) {
        Properties properties = redisTemplate.getRequiredConnectionFactory().getConnection().info(key);
        Map<String, Object> result = new ConcurrentHashMap<>();

        boolean needRebuild = infoCommands.contains(key);
        Set<String> propertyNames = properties.stringPropertyNames();
        if (needRebuild) {
            propertyNames.forEach(propertyKey -> {
                String propertyVal = properties.getProperty(propertyKey);
                String[] itemArray = propertyVal.split(",");
                List<Map<String, String>> propertyValList = new ArrayList<>(itemArray.length);
                for (String itemStr : itemArray) {
                    Map<String, String> item = new ConcurrentHashMap<>();
                    String[] split = itemStr.split("=");
                    item.put("name", split[0]);
                    item.put("value", split[1]);
                    propertyValList.add(item);
                }
                result.put(propertyKey, propertyValList);
            });
        } else propertyNames.forEach(propertyKey -> result.put(propertyKey, properties.get(propertyKey)));

        return result;
    }
}
