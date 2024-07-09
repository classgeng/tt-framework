package cn.cloud9.server.system.rbac.dto;

import cn.cloud9.server.struct.common.BaseDTO;
import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author OnCloud9
 * @description 角色实体类
 * @project tt-server
 * @date 2022年12月06日 下午 11:53
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class RoleDTO extends BaseDTO<RoleDTO> {

    @TableField("role_name")
    private String roleName;

    @TableField("role_value")
    private String roleValue;

    /* 菜单Id集合 */
    @TableField(exist = false)
    private List<Integer> menuIds;

    /* 权限关联集合 */
    @TableField(exist = false)
    private List<MePmDTO> mePmList;
}
