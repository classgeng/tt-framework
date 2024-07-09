package cn.cloud9.server.system.rbac.dto;

import cn.cloud9.server.struct.common.BaseDTO;
import cn.cloud9.server.struct.datauth.config.DataAuthConfigDTO;
import cn.cloud9.server.struct.masking.annotation.MaskingField;
import cn.cloud9.server.struct.masking.enums.MaskingType;
import cn.cloud9.server.struct.validator.groups.InsertCheck;
import cn.cloud9.server.struct.validator.groups.QueryCheck;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 下午 06:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class UserDTO extends BaseDTO<UserDTO> {

    @NotNull(groups = { InsertCheck.class, QueryCheck.class }, message = "用户名称不能为空")
    @NotEmpty(groups = { InsertCheck.class, QueryCheck.class }, message = "用户名称不能为空")
    @TableField(value = "user_name")
    private String userName;

    @NotNull(groups = { InsertCheck.class, QueryCheck.class }, message = "登录账号不能为空")
    @NotEmpty(groups = { InsertCheck.class, QueryCheck.class }, message = "登录账号不能为空")
    @TableField(value = "user_username")
    private String userUserName;

    @NotNull(groups = { InsertCheck.class, QueryCheck.class }, message = "用户密码不能为空")
    @NotEmpty(groups = { InsertCheck.class, QueryCheck.class }, message = "用户密码不能为空")
    @TableField(value = "user_password")
    private String userPassword;

    @MaskingField(srcField = "userPassword", maskingType = MaskingType.PASSWORD)
    @TableField(exist = false)
    private String maskedPassword;

    @TableField(exist = false)
    private List<Integer> roleIds;

    @TableField(exist = false)
    private List<DataAuthConfigDTO> dataAuthConfigs;

    @TableField(exist = false)
    private String groupName;

    @TableField(exist = false)
    private BigDecimal amount;
}
