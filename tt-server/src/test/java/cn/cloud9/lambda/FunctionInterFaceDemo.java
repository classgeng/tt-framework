package cn.cloud9.lambda;

import org.junit.Test;

import java.util.function.*;

public class FunctionInterFaceDemo {



    @Test
    public void originJdkFunctionalInterface() {
        Runnable runnable = FID::runnable;
        System.out.println(runnable);
        runnable.run();

        int i = 4;

        Consumer<Integer> consumer = FID::consumer;
        consumer
                .andThen(consumer)
                .andThen(consumer)
                .accept(-- i);
        System.out.println(consumer);
        consumer.accept(9527);

        BooleanSupplier booleanSupplier = FID::booleanSupplier;
        System.out.println(booleanSupplier);
        boolean asBoolean = booleanSupplier.getAsBoolean();


        FID fid = new FID();

        BiConsumer<FID, String> biConsumer = FID::biConsumer;
        System.out.println(biConsumer);
        biConsumer
                .andThen(biConsumer)
                .andThen(biConsumer)
                .accept(fid,"wdwdw");
        biConsumer.accept(fid, "测试参数值");

        BiFunction<FID, FID, FID> biFunction = FID::biFunction;
        System.out.println(biFunction);
        FID fid2 = biFunction.apply(fid, fid);


        BiPredicate<FID, FID> biPredicate = FID::biPredicate;
        System.out.println(biPredicate);
        boolean test = biPredicate.test(fid, fid2);



    }
}
