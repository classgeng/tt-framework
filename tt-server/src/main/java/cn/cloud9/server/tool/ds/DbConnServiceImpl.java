package cn.cloud9.server.tool.ds;

import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.file.FileProperty;
import cn.cloud9.server.struct.file.FileUtil;
import cn.cloud9.server.struct.spring.SpringContextHolder;
import cn.cloud9.server.struct.util.Assert;
import cn.cloud9.server.struct.util.DateUtils;
import cn.hutool.core.io.IoUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Lazy
@Slf4j
@Service("dbConnService")
public class DbConnServiceImpl extends ServiceImpl<DbConnMapper, DbConnDTO> implements IDbConnService {

    /* 数据源管理容器 */
    private static final Map<Integer, DataSource> dsContainer = new ConcurrentHashMap<>();
    private static final DbConnMapper dbConnMapper = SpringContextHolder.getBean("dbConnMapper", DbConnMapper.class);

    @Resource
    private FileProperty fileProperty;

    @Override
    public List<Entity> getSchemaList(Integer dbId) throws Exception {
        Db db = Db.use(getDsById(dbId));
        return db.query("SELECT * FROM `information_schema`.`SCHEMATA` WHERE `schema_name` NOT IN('information_schema', 'performance_schema', 'mysql', 'sys') ORDER BY `schema_name`");
    }

    @Override
    public void deleteDbConn(Integer dbId) {
        dsContainer.remove(dbId);
        removeById(dbId);
    }

    @Override
    public void updateDbConn(DbConnDTO dto) {
        dsContainer.remove(dto.getId());
        updateById(dto);
    }

    @Override
    public List<Entity> getTableList(Integer dbId, String schemaName) throws Exception {
        Db db = Db.use(getDsById(dbId));
        return db.query("SELECT * FROM `information_schema`.`TABLES` WHERE `TABLE_SCHEMA` = ? AND `TABLE_TYPE` = 'BASE TABLE'", schemaName);
    }

    @Override
    public Entity getTableSchema(Integer dbId, String schemaName, String tableName) throws Exception {
        Db db = Db.use(getDsById(dbId));
        return db.queryOne("SHOW CREATE TABLE `" + schemaName + "`.`" + tableName + "`");
    }

    @Override
    public void saveDbSchema(Integer dbId, String schemaName) throws Exception {
        List<Entity> schemaList = this.getSchemaList(dbId);
        Optional<Entity> entityOptional = schemaList.stream().filter(entity -> entity.get("schema_name").equals(schemaName)).findFirst();
        Assert.isTrue(entityOptional.isPresent(), ResultMessage.ALREADY_EXIST, schemaName);

        Db db = Db.use(getDsById(dbId));
        db.execute("CREATE DATABASE IF NOT EXISTS `" + schemaName + "` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci'");
    }

    @Override
    public void deleteDbSchema(Integer dbId, String schemaName) throws Exception {
        Db db = Db.use(getDsById(dbId));
        db.execute("DROP DATABASE IF EXISTS `" + schemaName + "` ");
    }

    @Override
    public void deleteDbTable(Integer dbId, String schemaName, String tableName) throws Exception {
        Db db = Db.use(getDsById(dbId));
        db.execute("DROP TABLE IF EXISTS `" + schemaName + "`.`" + tableName + "`");
    }

    @SuppressWarnings("all")
    @Override
    public IPage<Entity> getTablePage(Integer dbId, String schemaName, DbTableDTO dto) throws SQLException {
        Db db = Db.use(getDsById(dbId));

        Entity condition = Entity.create();
        condition.setTableName("`information_schema`.`TABLES`");
        condition.set("`TABLE_TYPE`", "BASE TABLE");
        condition.set("`TABLE_SCHEMA`", schemaName);
        if (StringUtils.isNotBlank(dto.getTableName()))
            condition.put("`TABLE_NAME`", "LIKE '%" + dto.getTableName() + "%'");
        if (StringUtils.isNotBlank(dto.getTableComment()))
            condition.put("`TABLE_COMMENT`", "LIKE '%" + dto.getTableComment() + "%'");
        if (StringUtils.isNotBlank(dto.getStartCreateTime()) && StringUtils.isNotBlank(dto.getEndCreateTime())) {
            condition.put("`CREATE_TIME`", "BETWEEN '" + dto.getStartCreateTime() + "' AND '" + dto.getEndCreateTime() + "'");
        }

        IPage<Entity> page = dto.getPage();
        PageResult<Entity> pageResult = db.page(condition, new Page((int)page.getCurrent() - 1, (int)page.getSize()));

        page.setRecords(pageResult);
        page.setTotal(pageResult.getTotal());
        page.setPages(pageResult.getTotalPage());
        return page;
    }

    @Override
    public void exportDbSchema(Integer dbId, String schemaName, HttpServletResponse response) throws Exception {
        List<Entity> tableList = this.getTableList(dbId, schemaName);
        if (CollectionUtils.isEmpty(tableList)) return;

        StringBuilder builder = new StringBuilder();

        /* 建库脚本 */
        Db db = Db.use(getDsById(dbId));
        builder.append("DROP DATABASE IF EXISTS `").append(schemaName).append("`;\n");
        Entity dbEntity = db.queryOne("SHOW CREATE DATABASE `" + schemaName + "` ");
        String createSchemaSql = dbEntity.get("create database").toString();
        builder.append(createSchemaSql).append(";");
        builder.append("\n\n");

        /* 选中库，预置处理 */
        builder.append("USE `").append(schemaName).append("`;\n");
        builder.append("SET NAMES utf8mb4;\n");
        builder.append("SET FOREIGN_KEY_CHECKS = 0;\n\n");

        /* 建表脚本 */
        for (Entity entity : tableList) {
            String tableName = entity.get("table_name").toString();
            builder.append("DROP TABLE IF EXISTS `").append(tableName).append("`;\n");
            Entity schemaEntity = this.getTableSchema(dbId, schemaName, tableName);
            String createTableSql = schemaEntity.get("create table").toString();
            builder.append(createTableSql).append(";");
            builder.append("\n\n");
        }

        String filename = schemaName + "_" + DateUtils.format(LocalDateTime.now(), DateUtils.DEFAULT_DATE_TIME_FORMAT_CN) + ".sql";
        FileUtil.setDownloadResponseInfo(response, filename);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(builder.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void backupDbSchema(Integer dbId, String schemaName, HttpServletResponse response) throws Exception {
        List<Entity> tableList = this.getTableList(dbId, schemaName);
        if (CollectionUtils.isEmpty(tableList)) return;

        StringBuilder builder = new StringBuilder();

        /* 建库脚本 */
        Db db = Db.use(getDsById(dbId));
        builder.append("DROP DATABASE IF EXISTS `").append(schemaName).append("`;\n");
        Entity dbEntity = db.queryOne("SHOW CREATE DATABASE `" + schemaName + "` ");
        String createSchemaSql = dbEntity.get("create database").toString();
        builder.append(createSchemaSql).append(";");
        builder.append("\n\n");

        /* 选中库，预置处理 */
        builder.append("USE `").append(schemaName).append("`;\n");
        builder.append("SET NAMES utf8mb4;\n");
        builder.append("SET FOREIGN_KEY_CHECKS = 0;\n\n");

        /* 建表脚本 */
        for (Entity entity : tableList) {
            String tableName = entity.get("table_name").toString();
            builder.append("DROP TABLE IF EXISTS `").append(tableName).append("`;\n");
            Entity schemaEntity = this.getTableSchema(dbId, schemaName, tableName);
            String createTableSql = schemaEntity.get("create table").toString();
            builder.append(createTableSql).append(";");
            builder.append("\n\n");

            /* 获取这个表的字段, 按原始定位排序， 并列为一行, 逗号空格分隔 */
            Entity fieldsEntity= db.queryOne("SELECT GROUP_CONCAT(DISTINCT CONCAT('`', COLUMN_NAME, '`') ORDER BY `ORDINAL_POSITION` ASC SEPARATOR ', ') AS `fields` FROM `information_schema`.`COLUMNS` WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?", schemaName, tableName);
            String fields = fieldsEntity.get("fields").toString();
            String[] columns = fields.trim().toLowerCase().replaceAll("`", "").split(", ");

            /* 这张表固定的字段格式 */
            final String specifyFields = "INSERT INTO `" + tableName + "` (" + fields + ") VALUES (";

            /* 开始批量写入Builder */
            batchBuildInsertSql(builder, db, schemaName, tableName, specifyFields, columns);
        }

        String storePath = fileProperty.getBaseDirectory() + File.separator + "backup";
        File storeDir = new File(storePath);
        if (!storeDir.exists()) storeDir.mkdirs();

        String backupFilePath = storePath + File.separator + "db-" + schemaName + DateUtils.format(LocalDateTime.now(), DateUtils.DEFAULT_DATE_TIME_FORMAT_CN) + ".sql";
        File file = new File(backupFilePath);
        file.createNewFile();

        /* 写入本地文件 */
        FileOutputStream fos = new FileOutputStream(backupFilePath);
        fos.write(builder.toString().getBytes());
        fos.close();

        /* 给文件设置信息 */
        FileUtil.setDownloadResponseInfo(response, file, file.getName());

        /* 输出给客户段 */
        BufferedInputStream in = cn.hutool.core.io.FileUtil.getInputStream(backupFilePath);
        ServletOutputStream os = response.getOutputStream();
        IoUtil.copy(in, os, IoUtil.DEFAULT_LARGE_BUFFER_SIZE);
        in.close();
        os.close();

        /* 不保留备份文件 */
        file.delete();
    }

    /**
     * 翻页查询来组装INSERT脚本 缓解IO压力
     * @param builder 脚本组装对象
     * @param db sql执行对象
     * @param schemaName 库名
     * @param tableName 表明
     * @param insertSqlPrePart INSERT前半部分
     * @param columns 列名数组
     */
    public void batchBuildInsertSql(StringBuilder builder, Db db, String schemaName, String tableName, String insertSqlPrePart, String[] columns) {
        try {
            int pageIdx = 0;
            int pageSize = 1000;
            int pageTotal = 1;

            final Entity condition = Entity.create();
            condition.setTableName("`" + schemaName + "`.`" + tableName + "`");

            while(pageIdx < pageTotal) {
                PageResult<Entity> pageResult = db.page(condition, new Page(pageIdx, pageSize));
                if (CollectionUtils.isEmpty(pageResult)) return;

                pageTotal = pageResult.getTotalPage();
                ++ pageIdx;

                /* 组装INSERT语句 */
                for (Entity recordEntity : pageResult) {
                    builder.append(insertSqlPrePart);
                    List<String> valList = new ArrayList<>();
                    for (String column : columns) {
                        Object val = recordEntity.get(column.trim());
                        boolean isEmpty = Objects.isNull(val);
                        valList.add(isEmpty ? "NULL" : "'" + val + "'");
                    }
                    String joinedValues = String.join(", ", valList);
                    builder.append(joinedValues).append(");\n");
                }
                builder.append("\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据源对象的方法 （所有的接口操作都需要经过这个步骤）
     * @param dbId 连接参数实体ID
     * @return 参数对应的数据源对象
     */
    public static DataSource getDsById(Integer dbId) {
        DataSource dataSource = dsContainer.get(dbId);
        boolean isEmpty = Objects.isNull(dataSource);
        if (isEmpty) {
            DbConnDTO dbConn = dbConnMapper.selectById(dbId);
            Assert.isTrue(Objects.isNull(dbConn), ResultMessage.NOT_FOUND_ERROR, "连接配置");
            DruidDataSource druidDs = new DruidDataSource();
            druidDs.setUrl("jdbc:mysql://" + dbConn.getHost() + ":" + dbConn.getPort() + "/sys");
            druidDs.setUsername(dbConn.getUsername());
            druidDs.setPassword(dbConn.getPassword());
            druidDs.addConnectionProperty("useInformationSchema", "true");
            druidDs.addConnectionProperty("characterEncoding", "utf-8");
            druidDs.addConnectionProperty("useSSL", "false");
            druidDs.addConnectionProperty("serverTimezone", "UTC");
            druidDs.addConnectionProperty("useAffectedRows", "true");
            dataSource = druidDs;
            /* 置入容器 */
            dsContainer.put(dbId, druidDs);
        }
        return dataSource;
    }

}
