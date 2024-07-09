package cn.cloud9.server.struct.util;

import cn.cloud9.server.struct.spring.SpringContextHolder;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description Jdbc工具类
 * @project tt-server
 * @date 2022年11月24日 下午 09:01
 */
@Slf4j
public class JdbcUtil {
    private static final DataSource ds = SpringContextHolder.getBean(DataSource.class);

    public static Connection getConnection() {
        try {
            log.info("JdbcUtil 获取连接资源  {}", ds);
            return ds.getConnection();
        } catch (SQLException e) {
            log.error("JdbcUtil 获取连接资源失败... {}", e.getMessage());
            return null;
        }
    }

    @SneakyThrows
    public static void close(Connection connection) {
        final boolean nonNull = Objects.nonNull(connection);
        if (nonNull) connection.close();
    }


    public static boolean connectionTest(String host, String port, String username, String password) throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/mysql?serverTimezone=Asia/Shanghai", username, password);
        boolean connected = Objects.nonNull(connection);
        connection.close();
        return connected;
    }
}
