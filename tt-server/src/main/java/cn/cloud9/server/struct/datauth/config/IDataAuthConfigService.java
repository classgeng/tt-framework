package cn.cloud9.server.struct.datauth.config;

import cn.cloud9.server.system.rbac.dto.UserDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IDataAuthConfigService extends IService<DataAuthConfigDTO> {
    List<DataAuthConfigDTO> getAssignableList(Integer userId);
}
