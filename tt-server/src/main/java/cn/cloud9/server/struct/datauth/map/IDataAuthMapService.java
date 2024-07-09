package cn.cloud9.server.struct.datauth.map;

import cn.cloud9.server.struct.datauth.config.DataAuthConfigDTO;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IDataAuthMapService extends IService<DataAuthMapDTO> {
    List<DataAuthConfigDTO> getAssignedList(Integer userId);
    void assign(UserDTO dto);
}
