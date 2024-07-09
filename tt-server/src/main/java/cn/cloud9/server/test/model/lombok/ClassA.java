package cn.cloud9.server.test.model.lombok;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月09日 下午 09:29
 */
public class ClassA {

    public static String function(String var) {
        String aa = "method call, get value -> " + var;
        System.out.println(aa);
        return aa;
    }
}
