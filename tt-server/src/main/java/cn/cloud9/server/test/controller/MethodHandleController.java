package cn.cloud9.server.test.controller;

import cn.cloud9.server.test.methodhandle.MyBiFunction;
import cn.cloud9.server.test.methodhandle.SimpleClass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@RestController
@RequestMapping("/test/method-handle")
public class MethodHandleController {


    @GetMapping("/lambda")
    public void userResolve() {
        try {
            Class<MethodHandles.Lookup> lookupClass = MethodHandles.Lookup.class;
            Field implLookup = lookupClass.getDeclaredField("IMPL_LOOKUP");
            implLookup.setAccessible(true);
            MethodHandles.Lookup lookup = (MethodHandles.Lookup)implLookup.get(null);

            // MethodHandles.Lookup lookup = MethodHandles.lookup();

            Method method = SimpleClass.class.getDeclaredMethod("forFunctionInterface", int.class);
            method.setAccessible(true);
            MethodHandle methodHandle = lookup.in(SimpleClass.class).unreflect(method);

            MethodType methodType = methodHandle.type();

            CallSite apply = LambdaMetafactory.metafactory(
                lookup,
                "apply",
                MethodType.methodType(MyBiFunction.class),
                MethodType.methodType(String.class, Object.class, int.class),
                methodHandle,
                // methodType
                MethodType.methodType(String.class, SimpleClass.class, int.class)
            );

            SimpleClass simpleClass = new SimpleClass();
            MyBiFunction<SimpleClass, String, Integer> biFunction = (MyBiFunction)apply.getTarget().invokeExact(simpleClass);

            String apply1 = biFunction.apply(simpleClass, 19923);
            System.out.println("apply result -> " + apply1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
