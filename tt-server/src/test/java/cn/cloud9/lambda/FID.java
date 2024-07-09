package cn.cloud9.lambda;

import java.util.Objects;

public class FID {


    public static void runnable() {
        System.out.println("无参数执行！");
    }

    public static <Type> void consumer(Type consume) {
        System.out.println("单参数执行！" + consume);
    }

    public static boolean booleanSupplier() {
        return true;
    }




    public void biConsumer(String consume) {
        System.out.println("实例方法参数执行！" + consume);
    }

    public <Type> Type biFunction(Type type) {
        System.out.println("实例方法参数执行！且返回：" + type.toString());
        return type;
    }

    public boolean biPredicate(FID fid) {
        return Objects.nonNull(fid);
    }
}
