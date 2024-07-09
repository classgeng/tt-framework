package cn.cloud9.screw;


import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Arrays;
import java.util.Collections;



public class MySqlDocTest {
    /**
     * 路径
     */
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306";
    /**
     * 数据库名
     */
    private static final String MYSQL_NAME = "db1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    /**
     * mysql用户名
     */
    private static final String MYSQL_USERNAME = "root";
    /**
     * mysql密码
     */
    private static final String MYSQL_PASSWORD = "root";
    /**
     * 文件输出目录
     */
    private static final String FILE_OUTPUT_DIR = "E:\\";

    /**
     * 可以设置 Word 或者 Markdown 格式
     */
    private static final EngineFileType FILE_OUTPUT_TYPE = EngineFileType.WORD;
    private static final String DOC_FILE_NAME = "java实现数据库表设计文档";
    private static final String DOC_VERSION = "V1.0.0";
    private static final String DOC_DESCRIPTION = "数据库表设计描述";

    public static void main(String[] args) {
        // 创建 screw 的配置
        Configuration config = Configuration.builder()
                // 版本
                .version(DOC_VERSION)
                // 描述
                .description(DOC_DESCRIPTION)
                // 数据源
                .dataSource( buildDataSource())
                // 引擎配置
                .engineConfig(buildEngineConfig())
                // 处理配置
                .produceConfig(buildProcessConfig())
                .build();

        // 执行 screw，生成数据库文档
        new DocumentationExecute(config).execute();
    }

    /**
     * 创建数据源
     */
    private static HikariDataSource buildDataSource() {
        // 创建 HikariConfig 配置类
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl(MYSQL_URL + "/" + MYSQL_NAME);
        hikariConfig.setUsername(MYSQL_USERNAME);
        hikariConfig.setPassword(MYSQL_PASSWORD);
        // 设置可以获取 tables remarks 信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        // 创建数据源
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        return  hikariDataSource;
    }

    /**
     * 创建 screw 的引擎配置
     */
    private static EngineConfig buildEngineConfig() {
        return EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(FILE_OUTPUT_DIR)
                // 打开目录
                .openOutputDir(false)
                // 文件类型
                .fileType(FILE_OUTPUT_TYPE)
                // 文件类型
                .produceType(EngineTemplateType.freemarker)
                // 自定义文件名称
                .fileName(DOC_FILE_NAME)
                .build();
    }

    /**
     * 创建 screw 的处理配置，一般可忽略
     * 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
     */
    private static ProcessConfig buildProcessConfig() {
        return ProcessConfig.builder()
                // 根据名称指定表生成
                .designatedTableName(Collections.<String>emptyList())
                // 根据表前缀生成
                .designatedTablePrefix(Collections.<String>emptyList())
                // 根据表后缀生成
                .designatedTableSuffix(Collections.<String>emptyList())
                // 忽略数据库中address这个表名
                .ignoreTableName(Arrays.asList("address"))
                // 忽略表前缀，就是db1数据库中表名是t_开头的都不生产数据库文档(t_student,t_user这两张表)
                .ignoreTablePrefix(Collections.singletonList("t_"))
                // 忽略表后缀(就是db1数据库中表名是_teacher结尾的都不生产数据库文档：stu_teacher)
                .ignoreTableSuffix(Collections.singletonList("_teacher"))
                .build();
    }

}
