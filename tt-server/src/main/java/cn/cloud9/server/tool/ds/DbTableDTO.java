package cn.cloud9.server.tool.ds;

import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class DbTableDTO {
    private String tableName;
    private String tableComment;
    private String startCreateTime;
    private String endCreateTime;

    @TableField(exist = false)
    private Page<Entity> page;
}
