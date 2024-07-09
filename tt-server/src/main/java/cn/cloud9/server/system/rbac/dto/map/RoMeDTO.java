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
 * @description 系统[角色菜单]关联表
 * @project tt-server
 * @date 2022年12月07日 上午 12:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_rome")
public class RoMeDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("menu_id")
    private Integer menuId;

    @TableField("role_id")
    private Integer roleId;

    /**
     * menu permit
     */
    @TableField("menu_type")
    private String menuType;

    @TableField(value = "creator")
    private String creator;

    @JSONField(format = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @TableField(value = "create_time")
    private LocalDateTime createTime;
}
