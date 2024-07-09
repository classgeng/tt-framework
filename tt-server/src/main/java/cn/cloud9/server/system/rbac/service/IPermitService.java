package cn.cloud9.server.system.rbac.service;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.PermitDTO;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPermitService extends IService<PermitDTO> {
    IPage<PermitDTO> queryPermitPage(PermitDTO query);

    /**
     * 获取权限集合
     * @param userContext 用户信息
     * @param roleList 已经得到的角色集合
     * @return 权限集合
     */
    List<String> getPermitList(UserContext userContext, List<RoleDTO> roleList,  List<MenuDTO> menuList);

    List<PermitDTO> getAllocatePermitList(Integer menuId);

    void allocatePermits(MenuDTO dto);

    List<MePmDTO> assignablePermits(Integer roleId);

    void assignPermits(RoleDTO role);

    List<MePmDTO> assignedPermits(Integer roleId);
}
