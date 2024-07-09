package cn.cloud9.server.system.rbac.service;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IMenuService extends IService<MenuDTO> {
    IPage<MenuDTO> queryMenuPage(MenuDTO query);

    /**
     * 根据当前登陆用户获取菜单树
     * @param userContext 登陆用户实体
     * @return List<MenuDTO> 菜单树
     */
    List<MenuDTO> getMenuList(UserContext userContext);

    List<MenuDTO> getCheckedMenuList(Integer sysRoleId);

    void assignMenus(RoleDTO role);
}
