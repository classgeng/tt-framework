package cn.cloud9.server.test.strategy;

import cn.cloud9.server.struct.exception.ServiceException;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 策略工具类
 * 按注解来区分
 *
 * 参考文档实现：
 * https://juejin.cn/post/7035414939657306126#comment
 */
public class StrategyUtil {

    /**
     * 获取策略Map
     * @param interfaceList
     * @param annotationTypeClass
     * @param annotationFunction
     * @param <Interface>
     * @param <AnnotationType>
     * @return
     */
    public static <Interface, AnnotationType extends Annotation, FlagType>
    Map<FlagType, Interface> getStrategyMap(
            final List<Interface> interfaceList,
            final Class<AnnotationType> annotationTypeClass,
            final Function<AnnotationType, FlagType> annotationFunction
    ) {
        return interfaceList.stream().filter(x -> flagFilter(x, annotationTypeClass)).collect(Collectors.toMap(
                x -> identGet(x, annotationTypeClass, annotationFunction),
                x -> x
        ));
    }

    private static <Type extends Annotation> boolean flagFilter(Object target, Class<Type> typeClass) {
        Class<?> targetClass = target.getClass();
        Type type = targetClass.getAnnotation(typeClass);
        if (Objects.isNull(type)) {
            Class<?> superclass = targetClass.getSuperclass();
            type = superclass.getAnnotation(typeClass);
            return Objects.nonNull(type);
        }
        return true;
    }

    private static <AnnotationType extends Annotation, FlagType> FlagType identGet(
            Object obj,
            Class<AnnotationType> annotationClass,
            Function<AnnotationType, FlagType> function
    ) {
        Class<?> aClass = obj.getClass();
        AnnotationType annotation = aClass.getAnnotation(annotationClass);
        if (Objects.isNull(annotation)) annotation = aClass.getSuperclass().getAnnotation(annotationClass);
        return function.apply(annotation);
    }

    public static <Interface> Interface getStrategyByKey(Map<String, Interface> strategyMap, String key, String exceptionMessage) {
        Interface anInterface = strategyMap.get(key);
        if (Objects.isNull(anInterface)) throw new ServiceException(exceptionMessage + key);
        return  anInterface;
    }
}
