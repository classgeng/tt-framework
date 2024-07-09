package cn.cloud9.methodhandle.demo;

@FunctionalInterface
public interface MyOperation {

    /**
     * 入参应和被lambda调用的方法一致，在本例中是Operation中的operate方法
     * @param a
     * @param b
     * @param c
     * @return 返回值应和被lambda调用的方法一致，在本例中是Operation中的operate方法
     */
    int toOperate(int a,int b,int c);
}
