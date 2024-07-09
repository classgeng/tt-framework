package cn.cloud9.server.struct.dict.hook;

import cn.cloud9.server.struct.dict.annotation.DictFrom;
import cn.cloud9.server.struct.dict.annotation.DisableTranslate;
import cn.cloud9.server.struct.dict.service.DictService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description 使用响应外切实现翻译入口
 * @project tt-server
 * @date 2022年11月25日 下午 07:19
 */
@Order(2)
@ControllerAdvice(annotations = RestController.class)
public class DictAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private DictService dictService;

    /**
     * 增加入口颗粒度， 可标注在类或方法上控制是否翻译
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        final Class<DisableTranslate> dtClass = DisableTranslate.class;

        /* 1、判断是否在类上标记 */
        DisableTranslate disableTranslate = methodParameter.getContainingClass().getAnnotation(dtClass);
        boolean isMarkOnClass = Objects.nonNull(disableTranslate);

        /* 2、判断是否在方法上标记 */
        disableTranslate = methodParameter.getMethod().getAnnotation(dtClass);
        boolean isMarkOnMethod = Objects.nonNull(disableTranslate);

        /* 3、只要在类或者方法上标记，则表示不使用翻译 */
        return !(isMarkOnClass || isMarkOnMethod);
    }

    @SuppressWarnings("all")
    @Override
    public Object beforeBodyWrite(
            Object result,
            MethodParameter methodParameter,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse
    ) {
        /* 是否为空 */
        final boolean isEmpty = Objects.isNull(result);
        if (isEmpty) return result;
        /* 返回的结果类型是否为基本类型 */
        final boolean isPrimitive = result.getClass().isPrimitive();
        if (isPrimitive) return result;
        /* 返回的结果类型是否为集合 */
        final boolean isCollection = result instanceof Collection;
        /* 返回的结果类型是否为翻页对象 */
        final boolean isPage = result instanceof IPage;

        if (isCollection) {
            Collection<Object> list = (Collection<Object>) result;
            if (CollectionUtils.isEmpty(list)) return result;
            for (Object row : list) translateDTO(row);
        } else if (isPage) {
            IPage<Object> page = (IPage<Object>) result;
            if (CollectionUtils.isEmpty(page.getRecords())) return result;
            final List<Object> records = page.getRecords();
            for (Object record : records) translateDTO(record);
        } else {
            translateDTO(result);
        }
        return result;
    }

    /**
     * 翻译DTO
     * @param result
     */
    public void translateDTO(Object result)  {
        /* 获取这个类下的所有字段 */
        final Field[] declaredFields = result.getClass().getDeclaredFields();
        for (Field field : declaredFields) {

            /* 处理嵌套在目标对象类中的集合类型翻译 */
            final Object fieldValue = BeanUtil.getFieldValue(result, field.getName());
            final boolean isCollection = fieldValue instanceof Collection;
            final boolean isPage = fieldValue instanceof IPage;
            if (isCollection) {
                Collection<Object> list = (Collection<Object>) fieldValue;
                if (CollectionUtils.isEmpty(list)) continue;
                for (Object row : list) {
                    if (row.getClass().isPrimitive()) continue;
                    this.translateDTO(row);
                }
            } else if (isPage) {
                IPage<Object> page = (IPage<Object>) fieldValue;
                if (CollectionUtils.isEmpty(page.getRecords())) continue;
                final List<Object> records = page.getRecords();
                for (Object record : records) {
                    if (record.getClass().isPrimitive()) continue;
                    this.translateDTO(record);
                }
            }

            /* 获取类上的@Translate注解 */
            final DictFrom dictFrom = field.getAnnotation(DictFrom.class);
            /* 如果没有此注解则跳过 */
            if (Objects.isNull(dictFrom)) continue;

            /* 获取声明的字典来源信息 */
            final String srcTable = dictFrom.srcTable();
            final String srcCate = dictFrom.srcCate();
            final String srcField = dictFrom.srcField();
            final boolean isMulti = dictFrom.isMulti();
            final String separator = dictFrom.separator();

            /* 取出目标对象对应字段的值 */
            final Object resultFieldValue = BeanUtil.getFieldValue(result, srcField);

            /* 如果没有值则跳过, 或者值类型不是字符串或者整形 */
            if (Objects.isNull(resultFieldValue) ) continue;
            else if (!(resultFieldValue instanceof String) && !(resultFieldValue instanceof Integer)) continue;

            if (!isMulti) {
                /* 调用Redis资源开始翻译 */
                final String key = srcTable + DictService.SEPARATOR + srcCate + DictService.SEPARATOR + resultFieldValue;
                final String translateName = dictService.findNameFromRedis(key);
                /* 赋值翻译字段 */
                BeanUtil.setFieldValue(result, field.getName(), translateName);
            } else if (resultFieldValue instanceof String && isMulti) {
                /* 按照注解声明的分割符对目标值进行切割，如果标签 */
                final String[] split = ((String)resultFieldValue).split(separator);

                /* 对切片逐一翻译，再拼接回来 */
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < split.length; i++) {
                    final String key = srcTable + DictService.SEPARATOR + srcCate + DictService.SEPARATOR + split[i].trim();
                    if (i == split.length - 1) {
                        final String fromRedis = dictService.findNameFromRedis(key);
                        builder.append(fromRedis);
                    } else {
                        final String fromRedis = dictService.findNameFromRedis(key);
                        builder.append(fromRedis);
                        builder.append(separator);
                    }
                }
                /* 赋值翻译字段 (多个) */
                BeanUtil.setFieldValue(result, field.getName(), builder.toString());
            }
        }
    }
}
