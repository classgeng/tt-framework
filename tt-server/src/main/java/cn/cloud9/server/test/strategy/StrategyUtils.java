package cn.cloud9.server.test.strategy;

import cn.cloud9.server.struct.enums.state.ServiceIdent;
import cn.cloud9.server.struct.exception.ServiceException;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


public class StrategyUtils {
    /**
     * 获取策略Map
     * @param interfaceList
     * @param annotationTypeClass
     * @param annotationFunction
     * @param <Interface>
     * @param <AnnotationType>
     * @return
     */
    public static <Interface, AnnotationType extends Annotation>
    Map<String, Interface> getStrategyMap(
            final List<Interface> interfaceList,
            final Class<StrategyFlag> annotationTypeClass,
            final Function<StrategyFlag, String> annotationFunction
    ) {
        return interfaceList.stream().filter(x -> flagFilter(x, annotationTypeClass)).collect(Collectors.toMap(
                x -> identGetByString(x, annotationTypeClass, annotationFunction),
                x -> x
        ));
    }

    public static <Interface, AnnotationType extends Annotation>
    Map<String, Interface> getStrategyMapByEnum(
            final List<Interface> interfaceList,
            final Class<StrategyFlag> annotationTypeClass,
            final Function<StrategyFlag, ServiceIdent> annotationFunction
    ) {
        return interfaceList.stream().filter(x -> flagFilter(x, annotationTypeClass)).collect(Collectors.toMap(
                x -> identGetByServiceIdent(x, annotationTypeClass, annotationFunction),
                x -> x
        ));
    }

    private static boolean flagFilter(Object target, Class<StrategyFlag> typeClass) {
        Class<?> targetClass = target.getClass();
        StrategyFlag type = targetClass.getAnnotation(typeClass);
        if (Objects.isNull(type)) {
            Class<?> superclass = targetClass.getSuperclass();
            type = superclass.getAnnotation(typeClass);
            return Objects.nonNull(type);
        }
        return true;
    }

    private static <AnnotationType extends Annotation> String identGetByString(
            Object obj,
            Class<AnnotationType> annotationClass,
            Function<AnnotationType, String> function
    ) {
        Class<?> aClass = obj.getClass();
        AnnotationType annotation = aClass.getAnnotation(annotationClass);
        if (Objects.isNull(annotation)) annotation = aClass.getSuperclass().getAnnotation(annotationClass);
        return function.apply(annotation);
    }

    private static <AnnotationType extends Annotation> String identGetByServiceIdent(
            Object obj,
            Class<AnnotationType> annotationClass,
            Function<AnnotationType, ServiceIdent> function
    ) {
        Class<?> aClass = obj.getClass();
        AnnotationType annotation = aClass.getAnnotation(annotationClass);
        if (Objects.isNull(annotation)) annotation = aClass.getSuperclass().getAnnotation(annotationClass);

        ServiceIdent apply = function.apply(annotation);
        return apply.getIdent();
    }

    public static <Interface> Interface getStrategyByKey(Map<String, Interface> strategyMap, String key, String exceptionMessage) {
        Interface anInterface = strategyMap.get(key);
        if (Objects.isNull(anInterface)) throw new ServiceException(exceptionMessage + key);
        return  anInterface;
    }
}
