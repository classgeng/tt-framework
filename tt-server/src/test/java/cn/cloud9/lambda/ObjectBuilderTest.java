package cn.cloud9.lambda;

import cn.cloud9.server.MainApplication;
import cn.cloud9.server.struct.enums.EnumUtil;
import cn.cloud9.server.struct.lambda.ObjectBuilder;
import cn.cloud9.server.test.model.BuilderModel;
import lombok.SneakyThrows;
import net.bytebuddy.agent.builder.LambdaFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.function.ToIntFunction;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月22日 下午 10:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObjectBuilderTest {

    @Test
    public void instanceBuildTest() {
        final ObjectBuilder<BuilderModel> builder = ObjectBuilder.builder(BuilderModel::new);

        builder
                .with(BuilderModel::setCode, 1001)
                .with(BuilderModel::setMessage, "这是消息")
                .with(BuilderModel::setStatus, true);

        final BuilderModel model = builder.build();
        System.out.println(model);
    }

    @Test
    public void enumTest() {
        EnumUtil.findEnumClassByPath();
    }


    @SneakyThrows
    @Test
    public void magic() {

    }


}
