package ${modelPath};

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ${author}
 * @description ${tableName} 实体类 ${modelDescription!}
 * @project ${projectName}
 * @date ${dateTime}
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("${tableName}")
public class ${modelName} {

<#list columns as column>
    @TableField("${column.columnName}")
    private ${column.type} ${column.fieldName};

</#list>

    @TableField(exist = false)
    private Page<${modelName}> page;
}
