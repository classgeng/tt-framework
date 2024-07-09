package cn.cloud9.server.struct.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 下午 01:39
 */
public class StringHelper {
    /**
     * 转换
     * @param obj
     * @return
     */
    public static String getObjectValue(Object obj) {
        return Objects.isNull(obj) ? "" : String.valueOf(obj);
    }


    /**
     * 方法提供更多的返回值
     */
    public static void returnMore() {
        Pair<?, ?>[] emptyArray = Pair.EMPTY_ARRAY;
        Pair<Object, Object>[] pairs = Pair.emptyArray();

        Pair<Long, String> pair2 = Pair.of(100L, "String");

        Long key = pair2.getKey();
        String value = pair2.getValue();

        Long  pair2left = pair2.getLeft();
        String pair2Right = pair2.getRight();

        Triple<Double, Long, Integer> triple = Triple.of(3.14D, 213L, 100);
        Triple<Object, Object, Object>[] triples = Triple.emptyArray();
    }
}
