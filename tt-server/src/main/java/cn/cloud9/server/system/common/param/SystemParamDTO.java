package cn.cloud9.server.system.common.param;

import cn.cloud9.server.struct.common.BaseDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月07日 下午 11:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_param")
public class SystemParamDTO extends BaseDTO<SystemParamDTO> {

    @TableField("param_name")
    private String paramName;

    @TableField("param_value")
    private String paramValue;
}
