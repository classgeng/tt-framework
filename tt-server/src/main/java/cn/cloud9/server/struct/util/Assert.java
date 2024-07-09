package cn.cloud9.server.struct.util;

import cn.cloud9.server.struct.common.BaseDTO;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.exception.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 05:15
 */
public class Assert {


    /**
     * 是否成立业务异常
     * @param condition
     * @param errMsg
     * @param params
     */
    public static void isTrue(boolean condition, ResultMessage errMsg, Object ... params) {
        if (condition)
            throw new ServiceException(errMsg, params);
    }

    /**
     * 是否不成立业务异常
     * @param condition
     * @param errMsg
     * @param params
     */
    public static void isFalse(boolean condition, ResultMessage errMsg, Object ... params) {
        if (!condition)
            throw new ServiceException(errMsg, params);
    }

    /**
     * 是否为空串业务异常
     * @param src
     * @param errMsg
     * @param params
     */
    public static void isEmpty(String src, ResultMessage errMsg, Object ... params) {
        if (StringUtils.isBlank(src))
            throw new ServiceException(errMsg, params);
    }

    /**
     * 集合判空业务异常
     * @param collection
     * @param errMsg
     * @param params
     */
    public static void isEmpty(Collection<?> collection, ResultMessage errMsg, Object ... params) {
        if (CollectionUtils.isEmpty(collection))
            throw new ServiceException(errMsg, params);
    }

    /**
     * 映射对象判空业务异常
     * @param map
     * @param errMsg
     * @param params
     */
    public static void isEmpty(Map<?, ?> map, ResultMessage errMsg, Object ... params) {
        if (MapUtils.isEmpty(map))
            throw new ServiceException(errMsg, params);
    }

    /**
     *
     * @param object
     * @param errMsg
     * @param params
     */
    public static void isEmpty(Object object, ResultMessage errMsg, Object ... params) {
        if (Objects.isNull(object))
            throw new ServiceException(errMsg, params);
    }

    /**
     *
     * @param t
     * @param errMsg
     * @param params
     * @param <T>
     */
    public static <T extends BaseDTO<T>> void isEmpty(T t, ResultMessage errMsg, Object ... params) {
        if (Objects.isNull(t))
            throw new ServiceException(errMsg, params);
    }
}
