package cn.cloud9.server.system.rbac.dto.map;

import cn.cloud9.server.struct.common.BaseDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author OnCloud9
 * @description 系统[菜单操作权限]表
 * @project tt-server
 * @date 2022年12月07日 上午 12:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_mepm")
public class MePmDTO extends BaseDTO<MePmDTO> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("menu_id")
    private Integer menuId;

    @TableField("permit_id")
    private Integer permitId;

    @TableField(exist = false)
    private String menuName;

    @TableField(exist = false)
    private String menuValue;

    @TableField(exist = false)
    private String permitName;

    @TableField(exist = false)
    private String permitValue;


    public void rebuildPermitValue() {
        this.permitValue = this.menuValue + "@" + this.permitValue;
    }
}
