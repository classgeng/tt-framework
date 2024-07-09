package cn.cloud9.server.system.rbac.service;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IRoleService extends IService<RoleDTO> {

    IPage<RoleDTO> queryRolePage(RoleDTO query);

    List<RoleDTO> getRoleList(UserContext userContext);

    List<RoleDTO> getAssignedRoleList(Integer sysUserId);

    void assignedRoles(UserDTO user);
}
