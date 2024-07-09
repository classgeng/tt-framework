package cn.cloud9.server.test.model;

import cn.cloud9.server.struct.validator.groups.IdCheck;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月07日 下午 07:27
 */
@Data
public class TestModel {

    /**
     * 注解 @Validated(value = IdCheck.class)时触发
     */
    @NotNull(groups = IdCheck.class, message = "ID不能为空")
    private Integer id;

    @NotNull(message = "标签名不可为空")
    @Length(min = 3, max = 24, message = "长度控制在3~24个字符内")
    private String labelName;

    @NotNull(message = "数量不能为空")
    @Min(value = 1L, message = "最少数量为1")
    @Max(value = 999L, message = "最大数量为999")
    private Integer amount;

    @Pattern(message = "邮箱格式不正确", regexp = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @NotEmpty(message = "账号不能为空")
    private String account;
}
