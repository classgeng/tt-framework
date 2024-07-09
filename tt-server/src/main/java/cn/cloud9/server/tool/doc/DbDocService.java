package cn.cloud9.server.tool.doc;

import cn.cloud9.server.struct.file.FileProperty;
import cn.cloud9.server.struct.file.FileUtil;
import cn.cloud9.server.tool.ds.DbConnDTO;
import cn.cloud9.server.tool.ds.DbConnMapper;
import cn.hutool.core.io.IoUtil;
import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Service
public class DbDocService {

    @Resource
    private FileProperty fileProperty;

    @Resource
    private DbConnMapper dbConnMapper;

    /**
     * 生成mysql文档
     * @param dbId 连接实体
     * @param schemaName 约束名
     * @param dto 文档参数实体
     * @param response 响应对象
     * @throws IOException IO异常
     */
    public void getDbSchemaDoc(Integer dbId, String schemaName, DbDocDTO dto, HttpServletResponse response) throws IOException {
        Configuration config = Configuration.builder()
                .organization(dto.getOrganization()) // 组织机构
                .organizationUrl(dto.getOrganizationUrl()) // 组织链接
                .title(dto.getDocTitle()) // 文档标题
                .version(dto.getVersion()) // 版本
                .description(dto.getDescription()) // 描述
                .dataSource(buildDataSource(dbId, schemaName)) // 数据源
                .engineConfig(buildEngineConfig(dto)) // 引擎配置
                .produceConfig(buildProcessConfig(dto)) // 处理配置
                .build();
        new DocumentationExecute(config).execute();

        /* 从服务器本地读取生成的文档文件下载 */
        final String fileName =  dto.getDocName() + dto.getDocType().getFileSuffix();
        final File docFile = new File(fileProperty.getBaseDirectory() + File.separator + fileName);
        if (!docFile.exists()) return;
        FileUtil.setDownloadResponseInfo(response, docFile, fileName);

        /* 下载 */
        BufferedInputStream in = cn.hutool.core.io.FileUtil.getInputStream(docFile.getAbsolutePath());
        ServletOutputStream os = response.getOutputStream();
        long copySize = IoUtil.copy(in, os, IoUtil.DEFAULT_BUFFER_SIZE);
        log.info("文档生成成功！ {}bytes", copySize);
        in.close();
        os.close();
        
        /* 必须要等到其他IO流使用完毕资源释放了才能操作删除 */
        docFile.delete();
    }

    private HikariDataSource buildDataSource(Integer dbId, String schemaName) {
        DbConnDTO dbConn = dbConnMapper.selectById(dbId);
        // 创建 HikariConfig 配置类
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + dbConn.getHost() + ":" + dbConn.getPort() + "/" + schemaName);
        hikariConfig.setUsername(dbConn.getUsername());
        hikariConfig.setPassword(dbConn.getPassword());
        // 设置可以获取 tables remarks 信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf-8");
        hikariConfig.addDataSourceProperty("useSSL", "false");
        hikariConfig.addDataSourceProperty("serverTimezone", "UTC");
        hikariConfig.addDataSourceProperty("useAffectedRows", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        // 创建数据源
        return new HikariDataSource(hikariConfig);
    }

    private ProcessConfig buildProcessConfig(DbDocDTO dto) {
        return ProcessConfig.builder()
            // 根据名称指定表生成
            .designatedTableName(CollectionUtils.isEmpty(dto.getSpecifyTables()) ? Collections.emptyList() : dto.getSpecifyTables())
            // 根据表前缀生成
            .designatedTablePrefix(CollectionUtils.isEmpty(dto.getSpecifyTablePrefixes()) ? Collections.emptyList() : dto.getSpecifyTablePrefixes())
            // 根据表后缀生成
            .designatedTableSuffix(CollectionUtils.isEmpty(dto.getSpecifyTableSuffixes()) ? Collections.emptyList() : dto.getSpecifyTableSuffixes())
            // 忽略数据库中address这个表名
            .ignoreTableName(CollectionUtils.isEmpty(dto.getIgnoreTables()) ? Collections.emptyList() : dto.getIgnoreTables())
            // 忽略表前缀，就是db1数据库中表名是t_开头的都不生产数据库文档(t_student,t_user这两张表)
            .ignoreTablePrefix(CollectionUtils.isEmpty(dto.getIgnoreTablePrefixes()) ? Collections.emptyList() : dto.getIgnoreTablePrefixes())
            // 忽略表后缀(就是db1数据库中表名是_teacher结尾的都不生产数据库文档：stu_teacher)
            .ignoreTableSuffix(CollectionUtils.isEmpty(dto.getIgnoreTableSuffixes()) ? Collections.emptyList() : dto.getIgnoreTableSuffixes())
            .build();
    }

    /**
     * 引擎配置创建
     * @param dto 文档参数实体
     * @return EngineConfig
     */
    private EngineConfig buildEngineConfig(DbDocDTO dto) {
        return EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(fileProperty.getBaseDirectory())
                // 打开目录
                .openOutputDir(false)
                // 文件类型
                .fileType(dto.getDocType())
                // 文件类型
                .produceType(EngineTemplateType.freemarker)
                // 自定义文件名称
                .fileName(dto.getDocName())
                .build();
    }
}
