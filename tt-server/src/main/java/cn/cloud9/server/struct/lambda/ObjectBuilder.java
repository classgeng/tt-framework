package cn.cloud9.server.struct.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author OnCloud9
 * @description 对象建造者
 * @project tt-server
 * @date 2022年11月22日 下午 10:03
 */
public class ObjectBuilder<Pojo> {

    /**
     * 声明构造器
     */
    private final Supplier<Pojo> constructor;

    /**
     * 存储泛型类 所有需要初始化的类属性
     */
    private final List<Consumer<Pojo>> dInjectList = new ArrayList<>();

    /**
     * 创建建造本身时注入具体的泛型类构造器
     * @param constructor 构造器
     */
    public ObjectBuilder(Supplier<Pojo> constructor) {
        this.constructor = constructor;
    }

    /**
     * 或者通过建造者类方法完成构造器注入
     * @param constructor 构造器
     * @param <Pojo>
     * @return
     */
    public static <Pojo> ObjectBuilder<Pojo> builder(Supplier<Pojo> constructor) {
        return new ObjectBuilder<>(constructor);
    }

    /**
     * 为属性赋值
     * @param <Pojo>
     * @param <FieldValue>
     */
    @FunctionalInterface
    public interface DInjectConsumer<Pojo, FieldValue> {
        void accept(Pojo pojoClass, FieldValue fieldValue);
    }

    /**
     * 建造方法：连续赋值操作?
     * @param consumer
     * @param fieldValue
     * @param <FieldValue>
     * @return
     */
    public <FieldValue> ObjectBuilder<Pojo> with(ObjectBuilder.DInjectConsumer<Pojo, FieldValue> consumer, FieldValue fieldValue) {
        Consumer<Pojo> pojoConsumer = instance -> consumer.accept(instance, fieldValue);
        dInjectList.add(pojoConsumer);
        return this;
    }

    /**
     * 建造完成
     * @return
     */
    public Pojo build() {
        /* 1、调用Supplier 创建泛型类的实例 */
        final Pojo pojo = constructor.get();
        /* 2、变量注入列表，依次赋值，完成属性初始化 */
        dInjectList.forEach(dInject -> dInject.accept(pojo));
        /* 3、返回实例 */
        return pojo;
    }

}
