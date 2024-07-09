package cn.cloud9.server.struct.dict.dto;

import cn.cloud9.server.struct.common.BaseDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月13日 下午 09:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict")
public class DictDTO extends BaseDTO<DictDTO> {

    @TableField("dict_code")
    private String dictCode;

    @TableField("dict_name")
    private String dictName;

    @TableField("dict_category")
    private String dictCategory;

    @TableField("dict_catename")
    private String dictCateName;
}
