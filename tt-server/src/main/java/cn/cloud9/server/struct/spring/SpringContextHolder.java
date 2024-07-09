package cn.cloud9.server.struct.spring;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Spring上下文持有器类，用于静态方式获取Bean实例
 * @author OnCloud9
 * @description 该类必须一开始就被加载 @Lazy(value = false)
 * @project tt-server
 * @date 2022年11月13日 下午 11:04
 */
@Lazy(value = false)
@Service
public class SpringContextHolder implements ApplicationContextAware {
    /**
     * spring上下文
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取bean
     * @param name  bean名称
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name){
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取bean
     * @param requiredType bean类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取bean
     * @param name  bean名称
     * @param requiredType  bean类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> requiredType){
        return applicationContext.getBean(name,requiredType);
    }

    /**
     * 根据标记的指定注解寻找所有Bean
     * @param annotationClass 标记的注解字节对象
     * @return Bean Map
     */
    public static Map<String, Object> getClassWithAnnotation(Class<? extends Annotation> annotationClass) {
        return applicationContext.getBeansWithAnnotation(annotationClass);
    }

}
