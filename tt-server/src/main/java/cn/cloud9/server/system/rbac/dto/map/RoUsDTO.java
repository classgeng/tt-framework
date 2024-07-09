package cn.cloud9.server.system.rbac.dto.map;

import cn.cloud9.server.struct.util.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author OnCloud9
 * @description 系统[用户角色]关联实体
 * @project tt-server
 * @date 2022年12月06日 下午 11:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_rous")
public class RoUsDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("role_id")
    private Integer roleId;

    @TableField(value = "creator")
    private String creator;

    @JSONField(format = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @TableField(value = "create_time")
    private LocalDateTime createTime;
}
