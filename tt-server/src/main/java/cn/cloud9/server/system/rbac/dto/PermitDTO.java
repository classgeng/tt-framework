package cn.cloud9.server.system.rbac.dto;

import cn.cloud9.server.struct.common.BaseDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author OnCloud9
 * @description 权限实体类
 * @project tt-server
 * @date 2022年12月06日 下午 11:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permit")
public class PermitDTO extends BaseDTO<PermitDTO> {

    @TableField("permit_name")
    private String permitName;

    @TableField("permit_value")
    private String permitValue;
}
