package cn.cloud9.server.struct.datauth.test;

import cn.cloud9.server.struct.common.BaseDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月25日 上午 10:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("data_auth_test")
public class DataAuthTestDTO extends BaseDTO<DataAuthTestDTO> {
    @TableField("content")
    private String content;
}
