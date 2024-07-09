package cn.cloud9.reflect;

import cn.cloud9.server.struct.enums.EnumUtil;
import cn.cloud9.server.struct.enums.state.ApproveState;
import cn.cloud9.server.tool.template.TemplateType;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Properties;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月02日 下午 11:27
 */
public class ReflectTest {

    @Test
    public void utilEnumToMap() {
        Map<String, Object> bean = BeanUtil.beanToMap(TemplateType.MODEL);
        System.out.println(bean);
    }

    @Test
    public void isFullFillEnumInstance() {

        final boolean fullFill = EnumUtil.isFullFill(ApproveState.PASSED);



    }

    @Test
    public void getAppStartTime() {
        /* 获取启动时间 */
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        LocalDateTime localDateTime = Instant.ofEpochMilli(startTime).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        System.out.println(localDateTime);
    }

    @Test
    public void getAllProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k,v) -> System.out.println("key " + k + " | val -> " + v));
        System.out.println("- - - - - -");
        Map<String, String> env = System.getenv();
        env.forEach((k,v) -> System.out.println("key " + k + " | val -> " + v));


    }

    @Test
    public void complexObjectBuild() {
        KdVoucherDTO dto1 = KdVoucherDTO.builder().numbers(ArrayUtils.toArray(1)).build();


        KdVoucherDTO voucherDTO = KdVoucherDTO.builder().build().setModel(
                KdVoucherDTO.Model.builder()
                        .fAccountBookID(KdVoucherDTO.Model.FAccountBookID.builder().fNumber("fAccountBookID").build())
                        .fVoucherGroupId(KdVoucherDTO.Model.FVoucherGroupId.builder().fNumber("fVoucherGroupId").build())
                        .build()
        );

        System.out.println(JSON.toJSONString(voucherDTO));
    }
}
