package cn.cloud9.methodhandle.demo;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class DemoTest {
    public static void main(String[] args) {
        String className="Operation";
        String methodName="operate";
        try {
            //通过全类名，获取类的实例
            Class<Operation> clazz= Operation.class;
            //获取到类的对象，要求该类必须有无参构造
            Operation o = clazz.newInstance();
            //获取方法对象
            Method method= clazz.getDeclaredMethod(methodName,int.class,int.class,int.class);
            MethodHandles.Lookup lookup=MethodHandles.lookup();
            //指定方法不以反射运行
            MethodHandle mh=lookup.unreflect(method);
            //获取方法的类型
            MethodType type=mh.type();
            //将方法的实例对象类型加到方法类型工厂里
            MethodType factoryType=MethodType.methodType(MyOperation.class,type.parameterType(0));
            //移除方法里的实例对象类型
            type=type.dropParameterTypes(0,1);
            //获取代理对象，注意，第二个参数的字符串必须为函数式接口里的方法名
            MyOperation operator=(MyOperation) LambdaMetafactory.metafactory(lookup,"toOperate",factoryType,type,mh,type).getTarget().invokeExact(o);
            int operate = operator.toOperate(1, 2, 5);
            System.out.println(operate);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
