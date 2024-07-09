package cn.cloud9.server.struct.datauth.map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author OnCloud9
 * @description 数据授权映射表实体
 * @project tt-server
 * @date 2022年12月21日 下午 10:33
 */
@Data
@TableName("data_auth_map")
public class DataAuthMapDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("config_id")
    private Integer configId;

    @TableField("ident_key")
    private String identKey;

    @TableField("auth_value")
    private String authValue;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("creator")
    private String creator;
}
