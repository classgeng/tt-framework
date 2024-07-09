package cn.cloud9.jdbc;

import cn.cloud9.server.MainApplication;
import cn.cloud9.server.struct.util.JdbcUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月24日 下午 09:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectionTest {

    @Test
    public void getConnTest() {
        final Connection connection = JdbcUtil.getConnection();
        System.out.println(connection);
    }

    @Test
    public void druidTest() throws SQLException {
        DruidDataSource druidDs = new DruidDataSource();
        druidDs.setUrl("jdbc:mysql://9.135.95.39:3306/tt_db?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&useAffectedRows=true");
        druidDs.setUsername("root");
        druidDs.setPassword("123456");
        DruidPooledConnection connection = druidDs.getConnection();
        System.out.println(connection);

    }


    @Test
    public void druidDamengTest() throws SQLException {
        DruidDataSource druidDs = new DruidDataSource();
        druidDs.setDriverClassName("dm.jdbc.driver.DmDriver");
        druidDs.setUrl("jdbc:dm://192.168.124.13:5236/");
        druidDs.setUsername("SYSDBA");
        druidDs.setPassword("SYSDBA");
        DruidPooledConnection connection = druidDs.getConnection();
        System.out.println(connection);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT banner as 版本信息 FROM v$version");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Map<Integer,Object>> list = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData(); //获取结果集的元数据对象
        int columnCount = metaData.getColumnCount(); // 元数据对象获取总共的记录的列数
        while (resultSet.next()){   // 判断是否还有下一个记录行
            Map<Integer,Object> map = new HashMap<Integer, Object>(); //作为每一行的记录
            for (int i = 0; i < columnCount ; i++) { // 通过遍历记录列数，获取每个列的值
                Object object = resultSet.getObject(i + 1);
                map.put(i,object); // 装入每个字段的值
            }
            list.add(map); // 遍历多少，装载多少个记录
        }

        System.out.println(list);
        resultSet.close(); // 资源释放
        connection.close();
    }

    @Test
    public void druidUpdateTest() throws SQLException {
        DruidDataSource druidDs = new DruidDataSource();
        druidDs.setUrl("jdbc:mysql://9.135.95.39:3306/tt_db?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&useAffectedRows=true");
        druidDs.setUsername("root");
        druidDs.setPassword("123456");
        DruidPooledConnection connection = druidDs.getConnection();
        System.out.println(connection);


        PreparedStatement preparedStatement = connection.prepareStatement("update sys_user set user_name = '超级用户' where id = 7");
        boolean resultSet = preparedStatement.execute();

        System.out.println(resultSet);

        preparedStatement.close();
        connection.close();
    }
}
