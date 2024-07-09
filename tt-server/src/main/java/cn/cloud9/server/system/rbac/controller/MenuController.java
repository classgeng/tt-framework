package cn.cloud9.server.system.rbac.controller;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.struct.util.tree.TreeUtil;
import cn.cloud9.server.struct.validator.groups.IdCheck;
import cn.cloud9.server.struct.validator.groups.InsertCheck;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import cn.cloud9.server.system.rbac.dto.map.RoMeDTO;
import cn.cloud9.server.system.rbac.service.IMenuService;
import cn.cloud9.server.system.rbac.service.impl.RbacService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月07日 上午 12:08
 */
@Slf4j
@RestController
@RequestMapping("${auth-server.api-path}/rbac/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private RbacService rbacService;

    /**
     * 查询菜单 /menu/list
     * @param query 实体查询条件
     * @return List<MenuDTO>
     */
    @GetMapping("/list")
    public List<MenuDTO> getMenuList(@ModelAttribute MenuDTO query) {
        return menuService.lambdaQuery()
                .eq(StringUtils.isNotBlank(query.getMenuType()), MenuDTO::getMenuType, query.getMenuType())
                .orderByDesc(MenuDTO::getCreateTime)
                .list();
    }

    /**
     * 查询菜单 /menu/page
     * @param query 实体查询条件
     * @return 分页对象
     */
    @PermissionRequire(permission = "menu-info@query")
    @SystemLog(businessType = BusinessType.QUERY, module = "系统管理 - 权限维护 - 菜单管理 - 分页查询")
    @PostMapping("/page")
    public IPage<MenuDTO> queryMenuPage(@RequestBody MenuDTO query) {
        return menuService.queryMenuPage(query);
    }

    /**
     * 按ID查询菜单
     * @param menuId 菜单实体ID
     * @return 菜单实体
     */
    @GetMapping("/{sys-menu-id}")
    public MenuDTO getMenuById(@PathVariable("sys-menu-id") Integer menuId) {
        return menuService.getById(menuId);
    }


    /**
     * 新增菜单 /menu/insert
     * @param menu 菜单实体
     * @return 菜单实体
     */
    @PermissionRequire(permission = "menu-info@insert")
    @SystemLog(businessType = BusinessType.INSERT, module = "系统管理 - 权限维护 - 菜单管理 - 新增菜单")
    @PutMapping("/insert")
    public MenuDTO insertMenu(@RequestBody @Validated(value = InsertCheck.class) MenuDTO menu) {
        menu.preInsert();
        final boolean save = menuService.save(menu);
        return save ? menu : null;
    }

    /**
     * 更新菜单 /menu/update
     * @param menu 菜单实体
     * @return 菜单实体
     */
    @PermissionRequire(permission = "menu-info@update")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 菜单管理 - 更新菜单")
    @PostMapping("/update")
    public boolean updateMenu(@RequestBody @Validated(value = IdCheck.class) MenuDTO menu) {
        menu.preUpdate();
        return menuService.updateById(menu);
    }

    /**
     * 删除菜单 /menu/delete
     * @param menu 菜单实体
     * @return 菜单实体
     */
    @PermissionRequire(permission = "menu-info@delete")
    @SystemLog(businessType = BusinessType.DELETE, module = "系统管理 - 权限维护 - 菜单管理 - 删除菜单")
    @DeleteMapping("/delete")
    public boolean deleteMenu(@RequestBody @Validated(value = IdCheck.class) MenuDTO menu) {
        /* 安全删除校验 */
        safetyCheck(menu);
        return menuService.removeById(menu);
    }

    private void safetyCheck(MenuDTO menu) {
        final MePmDTO mePm = new MePmDTO();
        mePm.setMenuId(menu.getId());
        rbacService.deleteMenuOrPermitSafetyCheck(mePm);
        final RoMeDTO roMe = new RoMeDTO();
        roMe.setMenuId(menu.getId());
        rbacService.deleteRoleOrMenuSafetyCheck(roMe);
    }


    /**
     * 查询菜单树 /menu/tree
     * @return List<MenuDTO>
     */
    @GetMapping("/tree")
    public List<MenuDTO> getMenuTree() {
        final List<MenuDTO> list = menuService.lambdaQuery().list();
        return TreeUtil.build(list);
    }

    /**
     * 查询该角色下已授予的菜单
     * @return List<MenuDTO>
     */
    @PermissionRequire(permission = "role-info@menu-assign")
    @GetMapping("/assigned-list/{sys-role-id}")
    public List<MenuDTO> getCheckedMenuList(@PathVariable("sys-role-id") Integer sysRoleId) {
        return menuService.getCheckedMenuList(sysRoleId);
    }

    /**
     * 对该角色授予菜单
     * @param role 角色实体
     */
    @PermissionRequire(permission = "role-info@menu-assign")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 角色管理 - 菜单授予")
    @PutMapping("/assign")
    public void assignMenus(@RequestBody @Validated(value = IdCheck.class) RoleDTO role) {
        menuService.assignMenus(role);
    }
}
