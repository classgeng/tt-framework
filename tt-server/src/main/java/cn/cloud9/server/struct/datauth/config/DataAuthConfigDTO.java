package cn.cloud9.server.struct.datauth.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author OnCloud9
 * @description 数据授权配置表实体
 * @project tt-server
 * @date 2022年12月21日 下午 10:13
 */
@Data
@TableName("data_auth_config")
public class DataAuthConfigDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("ident_key")
    private String identKey;

    @TableField("ident_name")
    private String identName;

    @TableField("table_name")
    private String tableName;

    @TableField("field_name")
    private String fieldName;

    @TableField("auth_name")
    private String authName;

    @TableField("auth_type")
    private String authType;

    @TableField("auth_src")
    private String authSrc;

    @TableField(exist = false)
    private String authValue;
}
