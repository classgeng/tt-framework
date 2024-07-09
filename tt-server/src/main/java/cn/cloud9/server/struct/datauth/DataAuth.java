package cn.cloud9.server.struct.datauth;

import cn.cloud9.server.struct.datauth.config.DataAuthConfigDTO;
import lombok.Data;

import java.util.List;

/**
 * @author OnCloud9
 * @description 数据权限实体类
 * @project tt-server
 * @date 2022年12月20日 下午 07:25
 */
@Data
public class DataAuth {
    /* 是否激活授权执行 */
    private boolean isActive = false;
    /* sql语句Id */
    private String statementId;
    /* 授权配置项集合 */
    private List<DataAuthConfigDTO> configList;
}
