package cn.cloud9.server.struct.common;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.util.DateUtils;
import cn.cloud9.server.struct.validator.groups.IdCheck;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月16日 上午 12:01
 */
@Data
public class BaseDTO<T> implements Serializable {

    @NotNull(groups = IdCheck.class, message = "ID不能为空！")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "creator")
    private String creator;

    @TableField(value = "updater")
    private String updater;

    @JSONField(format = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String startCreateTime;
    @TableField(exist = false)
    private String endCreateTime;

    @JSONField(format = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Page<T> page;

    public void preInsert() {
        final UserContext userContext = UserContextHolder.getUserContext();
        creator = userContext.getUserAccount();
        updater = userContext.getUserAccount();

        final LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }

    public void preUpdate() {
        final UserContext userContext = UserContextHolder.getUserContext();
        updater = userContext.getUserAccount();
        updateTime = LocalDateTime.now();
    }
}
