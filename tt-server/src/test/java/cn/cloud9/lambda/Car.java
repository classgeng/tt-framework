package cn.cloud9.lambda;

public class Car {
    private String name = "asdhasd";


    public String getName(int sss) {
        System.out.println(name);
        return name;
    }

    public static String getName(long sss) {
        System.out.println("1001");
        return "1001";
    }
}
