package cn.cloud9.server.tool.ds;

import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.util.Assert;
import cn.cloud9.server.struct.util.JdbcUtil;
import cn.cloud9.server.tool.doc.DbDocDTO;
import cn.cloud9.server.tool.doc.DbDocService;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/db-conn")
public class DbConnController extends BaseController {

    @Resource
    private IDbConnService dbConnService;

    @Resource
    private DbDocService dbDocService;

    /**
     * 连接参数测试接口，
     * @param host 主机
     * @param port 端口
     * @param username 账户
     * @param password 密码
     * @return boolean true / false
     * @throws Exception SQL执行异常
     */
    @GetMapping(path = "/test", params = {"host", "port", "username", "password"})
    public boolean connectionInfoTest(String host, String port, String username, String password) throws Exception {
        return JdbcUtil.connectionTest(host, port, username, password);
    }

    /**
     * 返回连接配置列表
     * @return List<DbConnDTO>
     */
    @GetMapping("/list")
    public List<DbConnDTO> getDbConnList() {
        return  dbConnService.list();
    }

    /**
     * 获取该连接下的所有约束
     * @param dbId 连接实体ID
     * @return List<Entity>
     * @throws Exception SQL执行异常
     */
    @GetMapping("/{db-id}/schema-list")
    public List<Entity> getSchemaList(@PathVariable("db-id") Integer dbId) throws Exception {
        return dbConnService.getSchemaList(dbId);
    }

    /**
     * 获取该库的文档文件
     * @param dbId 连接实体ID
     * @param schemaName 库名
     * @param dto 文档参数实体
     */
    @PostMapping("/{db-id}/{schema-name}/doc")
    public void getDbSchemaDoc(@PathVariable("db-id") Integer dbId, @PathVariable("schema-name") String schemaName, @RequestBody DbDocDTO dto) throws IOException {
        dbDocService.getDbSchemaDoc(dbId, schemaName, dto, response);
    }

    /**
     * 导出表结构脚本
     * @param dbId 连接实体ID
     * @param schemaName 约束名称
     * @throws Exception SQL执行异常
     */
    @GetMapping(value = "/{db-id}/{schema-name}/export", produces = MediaType.TEXT_PLAIN_VALUE)
    public void exportDbSchema(@PathVariable("db-id") Integer dbId, @PathVariable("schema-name") String schemaName) throws Exception {
        dbConnService.exportDbSchema(dbId, schemaName, response);
    }

    /**
     * 导出完整数据
     * @param dbId 连接实体ID
     * @param schemaName 约束名称
     * @throws Exception SQL执行异常
     */
    @GetMapping(value = "/{db-id}/{schema-name}/backup", produces = MediaType.TEXT_PLAIN_VALUE)
    public void backupDbSchema(@PathVariable("db-id") Integer dbId, @PathVariable("schema-name") String schemaName) throws Exception {
        dbConnService.backupDbSchema(dbId, schemaName, response);
    }

    /**
     * 获取该连接下指定约束名下的所有表
     * @param dbId 连接实体ID
     * @param schemaName 约束名称
     * @return List<Entity>
     * @throws Exception SQL执行异常
     */
    @GetMapping("/{db-id}/{schema-name}/table-list")
    public List<Entity> getTableList(@PathVariable("db-id") Integer dbId, @PathVariable("schema-name") String schemaName) throws Exception {
        return dbConnService.getTableList(dbId, schemaName);
    }

    /**
     *
     * @param dbId 连接id
     * @param schemaName schema名称
     * @param dto 表参数实体
     * @return PageResult<Entity> 翻页对象
     * @throws Exception u异常     */
    @PostMapping("/{db-id}/{schema-name}/table-page")
    public IPage<Entity> getTablePage(
            @PathVariable("db-id") Integer dbId,
            @PathVariable("schema-name") String schemaName,
            @RequestBody DbTableDTO dto
    ) throws Exception {
        return dbConnService.getTablePage(dbId, schemaName, dto);
    }



    /**
     * 获取建表语句
     * @param dbId 连接实体ID
     * @param schemaName 约束名称
     * @param tableName 表名称
     * @return Entity
     * @throws Exception SQL执行异常
     */
    @GetMapping("/{db-id}/{schema-name}/{table-name}/schema-sql")
    public Entity getTableSchemaSQL(
            @PathVariable("db-id") Integer dbId,
            @PathVariable("schema-name") String schemaName,
            @PathVariable("table-name") String tableName
    ) throws Exception {
        return dbConnService.getTableSchema(dbId, schemaName, tableName);
    }

    /**
     * 按Id获取连接信息
     * @param dbId 连接实体ID
     */
    @GetMapping("/get/{db-id}")
    public DbConnDTO getDbConnById(@PathVariable("db-id") Integer dbId) {
        return dbConnService.getById(dbId);
    }

    /**
     * 保存连接信息
     * @param dto 连接实体
     */
    @PutMapping("/save")
    public void saveDbConn(@RequestBody DbConnDTO dto) {
        List<DbConnDTO> list = dbConnService.lambdaQuery().eq(DbConnDTO::getConnName, dto.getConnName()).list();
        Assert.isTrue(CollectionUtils.isNotEmpty(list), ResultMessage.ALREADY_EXIST, "连接名称");
        dbConnService.save(dto);
    }

    /**
     * 更新连接信息
     * @param dto 连接实体
     */
    @PostMapping("/update")
    public void updateDbConn(@RequestBody DbConnDTO dto) {
        dbConnService.updateDbConn(dto);
    }


    /**
     * 删除连接信息
     * @param dbId 连接实体ID
     */
    @DeleteMapping("/delete/{db-id}")
    public void deleteDbConn(@PathVariable("db-id") Integer dbId) {
        dbConnService.deleteDbConn(dbId);
    }

    /**
     * 创建一个新的Schema
     * @param dbId 连接id
     * @param schemaName schema名
     * @throws Exception 执行异常
     */
    @PutMapping("/{db-id}/{schema-name}/save")
    public void saveDbSchema(@PathVariable("db-id") Integer dbId, @PathVariable("schema-name") String schemaName) throws Exception {
        dbConnService.saveDbSchema(dbId, schemaName);
    }

    /**
     * 删除Schema
     * @param dbId 连接id
     * @param schemaName schema名
     * @throws Exception 执行异常
     */
    @DeleteMapping("/{db-id}/{schema-name}/delete")
    public void deleteDbSchema(@PathVariable("db-id") Integer dbId, @PathVariable("schema-name") String schemaName) throws Exception {
        dbConnService.deleteDbSchema(dbId, schemaName);
    }


    /**
     * 删除Table
     * @param dbId
     * @param schemaName schema名
     * @param tableName 表名
     * @throws Exception 执行异常
     */
    @DeleteMapping("/{db-id}/{schema-name}/{table-name}/delete")
    public void deleteDbTable(
            @PathVariable("db-id") Integer dbId,
            @PathVariable("schema-name") String schemaName,
            @PathVariable("table-name") String tableName
    ) throws Exception {
        dbConnService.deleteDbTable(dbId, schemaName, tableName);
    }
}
