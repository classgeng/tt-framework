package cn.cloud9.lambda;

import cn.cloud9.server.system.rbac.dto.UserDTO;
import lombok.SneakyThrows;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        MethodType methodType = MethodType.methodType(String.class, long.class);
        MethodHandle printName = lookup.findVirtual(Car.class, "getName", methodType);

        Car car = new Car();

        Object invoke = printName.invoke(Car.class, 100);
        System.out.println("invoke" + invoke);

//        MethodType methodType = MethodType.methodType(String.class, int.class);
//        MethodHandle printName = lookup.findVirtual(Car.class, "getName", methodType);
//
//        Car car = new Car();
//
//        Object invoke = printName.invoke(car, 100);
//        System.out.println("invoke" + invoke);

    }

    @Test
    public void streamApi() {
        List<UserDTO> userList = new ArrayList<>();

        List<Integer> userIdList = userList.stream()
                /* map 转换成某个类型来处理，比如这个场景是为了快速提取用户id */
                .map(UserDTO::getId)
                .collect(Collectors.toList());

        /* https://zhuanlan.zhihu.com/p/264811643 */

        List<Integer> distinctUserIds = userIdList.stream()
                /* distinct 可对元素进行去重，推荐类型是基础包装类和String两种 */
                .distinct()
                .collect(Collectors.toList());

        List<UserDTO> matchUsers = userList.stream()
                /* filter 用于检索匹配条件方法的元素 */
                .filter(user -> user.getUserName().contains("张"))
                /* 存在多个情况可以使用 toList 收集匹配的元素 */
                .collect(Collectors.toList());

        UserDTO userDTO = matchUsers.stream()
                .filter(user -> "1001".equals(user.getId().toString()))
                /* 或者使用 findFirst && findAny 提取唯一一个元素  */
                .findFirst()
                .get();

        boolean anyMatch = userList.stream()
                /* anyMatch 检查任意元素是否符合匹配方法，反之 allMatch 要求所有元素符合 */
                .anyMatch(user -> 3001 > user.getId());

        final Integer superPermit = 1001;
        Map<Boolean, List<UserDTO>> permitPartMap = userList.stream()
                /* partitioningBy 使用条件进行分区处理，场景：检查是不是超级权限的用户， 分区为 超级权限用户（true）和非超级权限用户（false） */
                .collect(Collectors.partitioningBy(user -> user.getRoleIds().contains(superPermit)));

        Map<String, List<UserDTO>> userGroupMap = userList.stream()
                /* groupingBy 指定以什么值作为分组的条件，这里以用户的组名称进行分组 */
                .collect(Collectors.groupingBy(UserDTO::getGroupName));

        List<String> fun1 = Arrays.asList("one", "two", "three");
        List<String> fun2 = Arrays.asList("four", "five", "six");
        List<List<String>> nestedList = Arrays.asList(fun1, fun2);
        nestedList.stream()
                /* flatMap 可以支持更深维度的集合转换，stream合并处理 */
                .flatMap(x -> x.stream()).map(x->x.toUpperCase())
                .forEach(System.out::println);

        /* 用于集合的翻页操作, 等同SQL的LIMIT 10, 20 */
        List<UserDTO> collect = userList.stream()
                .skip(10).limit(20)
                .collect(Collectors.toList());

        /* reduce用于聚合处理，例如合计这个用户集合的现金 */
        BigDecimal userCashAmount = userList.stream()
                .map(UserDTO::getAmount)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }
}
