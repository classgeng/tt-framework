package cn.cloud9.server.struct.redis;

import cn.cloud9.server.struct.util.DateUtils;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月10日 下午 10:20
 */
@Configuration
public class RedisConfiguration {

    /**
     * 改用fastjson redis序列化，请删除redis数据后使用此序列化
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, ?> redisTemplate(@Lazy RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        GenericToStringSerializer<String> stringRedisSerializer = new GenericToStringSerializer<>(String.class);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        FastJsonRedisSerializer<?> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        FastJsonConfig fastJsonConfig = fastJsonRedisSerializer.getFastJsonConfig();
        SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();

        /* 加入的LocalDateTime序列化，也可以不加（但是要用@JSONField(format = "yyyy-MM-dd HH:mm:ss")）格式化 */
        serializeConfig.put(LocalDateTime.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT).format((LocalDateTime) object));
        });
        serializeConfig.put(LocalDate.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate) object));
        });
        serializeConfig.put(LocalTime.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format((LocalTime) object));
        });
        serializeConfig.put(Date.class, (serializer, object, fieldName, fieldType, features) -> {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.write("\"" + DateUtil.format(((Date)object), DateUtils.DEFAULT_DATE_TIME_FORMAT) + "\"");
        });
        fastJsonConfig.setSerializeConfig(serializeConfig);
        fastJsonConfig.setFeatures(Feature.SupportAutoType);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteClassName);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
