package cn.cloud9.server.struct.response;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author OnCloud9
 * @description 响应内容序列化配置
 * @project tt-server
 * @date 2022年11月06日 下午 05:27
 */
@Configuration
public class FastJsonConverterConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        final FastJsonConfig config = new FastJsonConfig();
        /* 配置对象中添加序列化的一些特性 */
        config.setSerializerFeatures(
                SerializerFeature.WriteEnumUsingToString, /* Enum输出使用toString() */
                SerializerFeature.WriteMapNullValue, /* 输出null的字段为null,默认为不输出 */
                SerializerFeature.WriteNullListAsEmpty, /* List字段如果为null,输出为[],而非null */
                SerializerFeature.SortField,  /* 按字段排序后输出，默认false */
                SerializerFeature.PrettyFormat /* Pretty格式输出（Pretty是一种代码格式） */
        );
        converter.setFastJsonConfig(config);

        /* 配置支持的MIME类型 */
        List<MediaType> supportTypes = new ArrayList<>();
        supportTypes.add(MediaType.APPLICATION_JSON);
        supportTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportTypes.add(MediaType.TEXT_HTML);
        supportTypes.add(MediaType.TEXT_PLAIN);
        converter.setSupportedMediaTypes(supportTypes);

        /* 最后加入其中, 注意，必须指定为第一个序列转换器，否则不生效，报错 */
        converters.add(0, converter);
    }
}
