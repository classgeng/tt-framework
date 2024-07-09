package cn.cloud9.server.struct.enums;

import cn.cloud9.server.struct.dict.dto.DictDTO;
import cn.cloud9.server.struct.enums.annotation.DictCate;
import cn.cloud9.server.struct.enums.annotation.DictCode;
import cn.cloud9.server.struct.enums.annotation.DictName;
import cn.cloud9.server.struct.enums.hook.ScanSupport;
import cn.cloud9.server.struct.lambda.ObjectBuilder;
import cn.cloud9.server.struct.spring.SpringContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static cn.cloud9.server.struct.dict.service.DictService.*;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月27日 下午 08:53
 */
@Slf4j
@SuppressWarnings("all")
public class EnumUtil {
    private static final List<Class> enumClass = new ArrayList<>();
    private static final String CLASS_SCAN_PACKAGE_PATH = "cn.cloud9.server.struct.enums.state";

    @SneakyThrows
    public static void findEnumClassByPath() {
        if (CollectionUtils.isNotEmpty(enumClass)) return;
        try {
            final ScanSupport scanSupport = SpringContextHolder.getBean(ScanSupport.class);
            final Set<Class<?>> classes = scanSupport.doScan(CLASS_SCAN_PACKAGE_PATH);
            enumClass.addAll(classes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initialize() {
        findEnumClassByPath();

        RedisTemplate<String, Map<String, String>> mapTemplate = SpringContextHolder.getBean("redisTemplate", RedisTemplate.class);
        final HashOperations<String, Object, Object> hashOps = mapTemplate.opsForHash();

        String sqlKey = "default";
        /* 准备缓存结构容器, 并装载数据 */
        Map<String, String> mapTank = new ConcurrentHashMap<>();
        Map<String, List<DictDTO>> listTank = new ConcurrentHashMap<>();

        final List<DictDTO> dicts = transEnumsToDicts();
        for (DictDTO dict : dicts) {
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

        /* 装填到Redis中 */
        hashOps.putAll(KEY_MAP, mapTank);
        hashOps.putAll(KEY_LISTS, listTank);

        log.info("Redis 枚举缓存装载完毕 ...... ");
    }

    /**
     * 将声明的枚举集合转换为字典集合
     * @return
     */
    @SneakyThrows
    private static List<DictDTO> transEnumsToDicts() {
        final List<DictDTO> dicts = new ArrayList<>();
        for (Class enumClass : enumClass) {
            if (!enumClass.isEnum()) continue;
            final String cateName = enumClass.getSimpleName();
            final Object[] enumInstances = enumClass.getEnumConstants();
            for (Object inst : enumInstances) {

                /* 反射，寻找注解的编号字段 */
                final Field codeField = findFieldByAnnotaion(enumClass, DictCode.class);
                final Object codeVal = codeField.get(inst);

                /* 反射，寻找注解的名字字段 */
                final Field nameField = findFieldByAnnotaion(enumClass, DictName.class);
                final Object nameVal = nameField.get(inst);

                /* 反射，寻找注解的类别字段 */
                final Field cateField = findFieldByAnnotaion(enumClass, DictCate.class);
                final Object cateVal = cateField.get(inst);

                DictDTO dto = ObjectBuilder
                        .<DictDTO>builder(DictDTO::new)
                        .with(DictDTO::setDictCategory, String.valueOf(cateVal))
                        .with(DictDTO::setDictCode, String.valueOf(codeVal))
                        .with(DictDTO::setDictName, String.valueOf(nameVal))
                        .build();
                dicts.add(dto);
            }
        }
        return dicts;
    }

    /**
     * 获取枚举类中标记了指定注解的字段
     * @param targetClass 枚举目标类对象
     * @param annotationClass 声明的注解类对象
     * @param <T> 注解泛型
     * @param <E> 枚举泛型
     * @return 被注解的字段
     */
    @SneakyThrows
    private static <T extends Annotation, E extends Enum> Field findFieldByAnnotaion(Class<E> targetClass, Class<T> annotationClass) {
        final Field targetField = Arrays
                .stream(targetClass.getDeclaredFields())
                .filter(field -> Objects.nonNull(field.getAnnotation(annotationClass)))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(targetField)) throw new RuntimeException(targetClass.getName() + " 没有声明@" + annotationClass.getName() + "注解！！加载失败！！！");
        targetField.setAccessible(true);
        return targetField;
    }


    /**
     * 检查此枚举实例是否全部填充了成员属性值
     * @param ei
     * @param <EnumInstance>
     * @return
     */
    public static <EnumInstance extends Enum> boolean isFullFill(EnumInstance ei) {
        try {
            final Class<? extends Enum> enumClass = ei.getClass();
            final Field[] declaredFields = enumClass.getDeclaredFields();

            /* 过滤枚举常量和静态成员 */
            final List<Field> fields = Arrays.stream(declaredFields)
                    .filter(field -> !field.isEnumConstant() && !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toList());

            for (Field field : fields) {
                field.setAccessible(true);
                final Object o = field.get(ei);
                boolean isString =  o instanceof String;
                if (isString && StringUtils.isBlank(o.toString())) return false;
                else if (Objects.isNull(o)) return false;
            }
            return true;
        } catch (Exception e) {
            log.error("判断异常， {}", e.getMessage());
        }
        return false;
    }
}
