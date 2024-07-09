package cn.cloud9.lombok;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.junit.Test;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月13日 下午 10:26
 */
@Slf4j
public class StringTest {

    /**
     * 截取SQL的表名
     */
    @Test
    public void substringTest() {
        String sql = "SELECT * FROM `system_dict`";
        final int index = sql.indexOf("FROM");
        final String substring = sql.substring(index + 4).replace("`", "").trim();
        log.info("cut -> [{}]", substring);

        String pc = "parentCode|36-321423";

        final String substring1 = pc.substring("parentCode|".length());
        log.info("cut2 -> [{}]", substring1);
    }

}
