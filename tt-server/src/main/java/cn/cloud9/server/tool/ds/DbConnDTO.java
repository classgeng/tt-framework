package cn.cloud9.server.tool.ds;

import cn.cloud9.server.struct.common.BaseDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("db_conn")
public class DbConnDTO extends BaseDTO<DbConnDTO> {

    @NotNull
    @TableField(value = "conn_name")
    private String connName;

    @NotNull
    @TableField(value = "host")
    private String host;

    @NotNull
    @TableField(value = "port")
    private String port;

    @NotNull
    @TableField(value = "username")
    private String username;

    @NotNull
    @TableField(value = "password")
    private String password;
}
