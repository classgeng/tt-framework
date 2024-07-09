package cn.cloud9.server.tool.ds;

import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public interface IDbConnService extends IService<DbConnDTO> {
    List<Entity> getSchemaList(Integer dbId) throws Exception;

    void deleteDbConn(Integer dbId);

    void updateDbConn(DbConnDTO dto);

    List<Entity> getTableList(Integer dbId, String schemaName) throws Exception;

    Entity getTableSchema(Integer dbId, String schemaName, String tableName) throws Exception;

    void saveDbSchema(Integer dbId, String schemaName) throws Exception;

    void deleteDbSchema(Integer dbId, String schemaName) throws Exception;

    void deleteDbTable(Integer dbId, String schemaName, String tableName) throws Exception;

    IPage<Entity> getTablePage(Integer dbId, String schemaName, DbTableDTO dto) throws SQLException;

    void exportDbSchema(Integer dbId, String schemaName, HttpServletResponse response) throws Exception;

    void backupDbSchema(Integer dbId, String schemaName, HttpServletResponse response) throws Exception;
}
