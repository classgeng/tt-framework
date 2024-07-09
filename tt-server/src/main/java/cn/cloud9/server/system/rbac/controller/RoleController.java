package cn.cloud9.server.system.rbac.controller;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.struct.validator.groups.IdCheck;
import cn.cloud9.server.struct.validator.groups.InsertCheck;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import cn.cloud9.server.system.rbac.dto.map.RoMeDTO;
import cn.cloud9.server.system.rbac.dto.map.RoUsDTO;
import cn.cloud9.server.system.rbac.service.IRoleService;
import cn.cloud9.server.system.rbac.service.impl.RbacService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("${auth-server.api-path}/rbac/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @Resource
    private RbacService rbacService;

    /**
     * 查询角色 /role/page
     * @param query 实体查询条件
     * @return 分页对象
     */
    @PermissionRequire(permission = "role-info@query")
    @SystemLog(businessType = BusinessType.QUERY, module = "系统管理 - 权限维护 - 角色管理 - 分页查询")
    @PostMapping("/page")
    public IPage<RoleDTO> queryRolePage(@RequestBody RoleDTO query) {
        return roleService.queryRolePage(query);
    }

    /**
     * 按ID查询角色
     * @param roleId 角色实体ID
     * @return 角色实体
     */
    @GetMapping("/{sys-role-id}")
    public RoleDTO getRoleById(@PathVariable("sys-role-id") Integer roleId) {
        return roleService.getById(roleId);
    }

    /**
     * 新增角色 /role/insert
     * @param role 角色实体
     * @return 角色实体
     */
    @PermissionRequire(permission = "role-info@insert")
    @SystemLog(businessType = BusinessType.INSERT, module = "系统管理 - 权限维护 - 角色管理 - 新增角色")
    @PutMapping("/insert")
    public RoleDTO insertRole(@RequestBody @Validated(value = InsertCheck.class) RoleDTO role) {
        role.preInsert();
        final boolean save = roleService.save(role);
        return save ? role : null;
    }

    /**
     * 更新角色 /role/update
     * @param role 角色实体
     * @return 角色实体
     */
    @PermissionRequire(permission = "role-info@update")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 角色管理 - 更新角色")
    @PostMapping("/update")
    public boolean updateRole(@RequestBody @Validated(value = IdCheck.class) RoleDTO role) {
        role.preUpdate();
        return roleService.updateById(role);
    }

    /**
     * 删除角色 /role/delete
     * @param role 角色实体
     * @return 角色实体
     */
    @PermissionRequire(permission = "role-info@delete")
    @SystemLog(businessType = BusinessType.DELETE, module = "系统管理 - 权限维护 - 角色管理 - 删除角色")
    @DeleteMapping("/delete")
    public boolean deleteRole(@RequestBody @Validated(value = IdCheck.class) RoleDTO role) {
        safetyCheck(role);
        return roleService.removeById(role);
    }

    private void safetyCheck(RoleDTO role) {
        final RoMeDTO roMe = new RoMeDTO();
        roMe.setRoleId(role.getId());
        rbacService.deleteRoleOrMenuSafetyCheck(roMe);

        final RoUsDTO roUs = new RoUsDTO();
        roUs.setRoleId(role.getId());
        rbacService.deleteRoleOrUserSafetyCheck(roUs);
    }

    @GetMapping("/list")
    public List<RoleDTO> getRoleList() {
        return roleService.list();
    }

    /**
     * 获取该用户已授权的角色集合 /assigned-list/${sys-user-id}
     * @param sysUserId 用户实体ID
     * @return 已授予的角色集合
     */
    @PermissionRequire(permission = "user-info@role-assign")
    @GetMapping("/assigned-list/{sys-user-id}")
    public List<RoleDTO> getAssignedRoleList(@PathVariable("sys-user-id") Integer sysUserId) {
        return roleService.getAssignedRoleList(sysUserId);
    }

    @PermissionRequire(permission = "user-info@role-assign")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 用户管理 - 角色授予")
    @PutMapping("/assign")
    public void assignedRoles(@RequestBody @Validated(value = { IdCheck.class }) UserDTO user) {
        roleService.assignedRoles(user);
    }
}
