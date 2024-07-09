package cn.cloud9.server.struct.masking.hook;

import cn.cloud9.server.struct.masking.annotation.ActiveDataMasking;
import cn.cloud9.server.struct.masking.annotation.MaskingField;
import cn.cloud9.server.struct.masking.enums.MaskingType;
import cn.cloud9.server.struct.masking.intf.CustomMasking;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.SneakyThrows;
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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 11:16
 */
@Order(97)
@ControllerAdvice(annotations = RestController.class)
public class DataMaskingHook implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        final Class<ActiveDataMasking> admClass = ActiveDataMasking.class;
        boolean isMarkOnClass = Objects.nonNull(methodParameter.getContainingClass().getAnnotation(admClass));
        boolean isMarkOnMethod = Objects.nonNull(methodParameter.getMethodAnnotation(admClass));
        return isMarkOnClass || isMarkOnMethod;
    }

    @SuppressWarnings("all")
    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter methodParameter,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse
    ) {
        /* 是否为空 */
        final boolean isEmpty = Objects.isNull(body);
        if (isEmpty) return body;
        /* 返回的结果类型是否为基本类型 */
        final boolean isPrimitive = body.getClass().isPrimitive();
        if (isPrimitive) return body;

        /* 返回的结果类型是否为集合 */
        final boolean isCollection = body instanceof Collection;
        /* 返回的结果类型是否为翻页对象 */
        final boolean isPage = body instanceof IPage;

        if (isCollection) {
            Collection<Object> list = (Collection<Object>) body;
            if (CollectionUtils.isEmpty(list)) return body;
            for (Object row : list) masking(row);
        } else if (isPage) {
            IPage<Object> page = (IPage<Object>) body;
            if (CollectionUtils.isEmpty(page.getRecords())) return body;
            final List<Object> records = page.getRecords();
            for (Object record : records) masking(record);
        } else {
            masking(body);
        }

        return body;
    }

    @SneakyThrows
    private void masking(Object body) {
        /* 获取这个类下的所有字段 */
        final Field[] declaredFields = body.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            /* 处理嵌套在目标对象类中的集合类型脱敏 */
            final Object fieldValue = BeanUtil.getFieldValue(body, field.getName());
            final boolean isCollection = fieldValue instanceof Collection;
            final boolean isPage = fieldValue instanceof IPage;
            if (isCollection) {
                Collection<Object> list = (Collection<Object>) fieldValue;
                if (CollectionUtils.isEmpty(list)) continue;
                for (Object row : list) {
                    if (row.getClass().isPrimitive()) continue;
                    this.masking(row);
                }
            } else if (isPage) {
                IPage<Object> page = (IPage<Object>) fieldValue;
                if (CollectionUtils.isEmpty(page.getRecords())) continue;
                final List<Object> records = page.getRecords();
                for (Object record : records) {
                    if (record.getClass().isPrimitive()) continue;
                    this.masking(record);
                }
            }

            /* 获取类上的@MaskingField注解 */
            final MaskingField mf = field.getAnnotation(MaskingField.class);
            /* 如果没有此注解则跳过 */
            if (Objects.isNull(mf)) continue;

            /* 获取注解声明的源字段，和对应的脱敏类型 */
            final String srcField = mf.srcField();
            final MaskingType maskingType = mf.maskingType();
            final boolean usingCustom = mf.usingCustom();
            final boolean hideSrcData = mf.hideSrcData();

            /* 没有声明脱敏类型，也不使用自定义脱敏接口实现，则跳过，不执行 */
            if (MaskingType.NONE.equals(maskingType) && !usingCustom) continue;

            /* 取出目标对象对应字段的值 */
            final Object bodyFieldVal = BeanUtil.getFieldValue(body, srcField);
            /* 如果为空，类型不是String，不处理 */
            if (Objects.isNull(bodyFieldVal) || !(bodyFieldVal instanceof String)) continue;

            final String data = String.valueOf(bodyFieldVal);
            /* 使用自定义脱敏接口 */
            if (usingCustom) {
                /* 获取自定义脱敏对象， 执行脱敏操作 */
                final CustomMasking customMasking = mf.custom().newInstance();
                final String masking = customMasking.masking(data);
                /* 给当前字段赋值 */
                BeanUtil.setFieldValue(body, field.getName(), masking);
            } else {
                /* 使用枚举声明的脱敏方法来处理 */
                final Function<String, String> maskingFunc = maskingType.getMaskingFunc();
                final String masking = maskingFunc.apply(data);
                /* 给当前字段赋值 */
                BeanUtil.setFieldValue(body, field.getName(), masking);
            }

            /* 隐藏源数据 */
            if (hideSrcData) BeanUtil.setFieldValue(body, srcField, null);
        }
    }
}
