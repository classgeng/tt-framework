package cn.cloud9.server.system.rbac.controller;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.struct.validator.groups.IdCheck;
import cn.cloud9.server.struct.validator.groups.InsertCheck;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.PermitDTO;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.map.MePmDTO;
import cn.cloud9.server.system.rbac.service.IPermitService;
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
@RequestMapping("${auth-server.api-path}/rbac/permit")
public class PermitController {
    
    @Resource
    private IPermitService permitService;

    @Resource
    private RbacService rbacService;

    /**
     * 查询权限 /permit/page
     * @param query 实体查询条件
     * @return 分页对象
     */
    @PermissionRequire(permission = "permit-info@query")
    @SystemLog(businessType = BusinessType.QUERY, module = "系统管理 - 权限维护 - 权限管理 - 分页查询")
    @PostMapping("/page")
    public IPage<PermitDTO> queryPermitPage(@RequestBody PermitDTO query) {
        return permitService.queryPermitPage(query);
    }

    /**
     * 按ID查询权限 /permit/{sys-permit-id}
     * @param permitId 权限实体ID
     * @return 权限实体
     */
    @GetMapping("/{sys-permit-id}")
    public PermitDTO getPermitById(@PathVariable("sys-permit-id") Integer permitId) {
        return permitService.getById(permitId);
    }

    /**
     * 新增权限 /permit/insert
     * @param permit 权限实体
     * @return 权限实体
     */
    @PermissionRequire(permission = "permit-info@insert")
    @SystemLog(businessType = BusinessType.INSERT, module = "系统管理 - 权限维护 - 权限管理 - 新增权限")
    @PutMapping("/insert")
    public PermitDTO insertPermit(@RequestBody @Validated(value = InsertCheck.class) PermitDTO permit) {
        permit.preInsert();
        final boolean save = permitService.save(permit);
        return save ? permit : null;
    }

    /**
     * 更新权限 /permit/update
     * @param permit 权限实体
     * @return 权限实体
     */
    @PermissionRequire(permission = "permit-info@update")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 权限管理 - 更新权限")
    @PostMapping("/update")
    public boolean updatePermit(@RequestBody @Validated(value = IdCheck.class) PermitDTO permit) {
        permit.preUpdate();
        return permitService.updateById(permit);
    }

    /**
     * 删除权限 /permit/delete
     * @param permit 权限实体
     * @return 权限实体
     */
    @PermissionRequire(permission = "permit-info@delete")
    @SystemLog(businessType = BusinessType.DELETE, module = "系统管理 - 权限维护 - 权限管理 - 删除权限")
    @DeleteMapping("/delete")
    public boolean deletePermit(@RequestBody @Validated(value = IdCheck.class) PermitDTO permit) {

        /* 安全删除校验 */
        final MePmDTO mePm = new MePmDTO();
        mePm.setPermitId(permit.getId());
        rbacService.deleteMenuOrPermitSafetyCheck(mePm);

        return permitService.removeById(permit);
    }


    /**
     * 查询权限集合 /permit/list
     * @return List<PermitDTO> 权限集合
     */
    @GetMapping("/list")
    public List<PermitDTO> getPermitList() {
        return permitService.list();
    }

    /**
     * 查询该菜单下的权限集合
     * @param menuId 菜单实体ID
     * @return 权限集合
     */
    @PermissionRequire(permission = "menu-info@permit-allocate")
    @GetMapping("/allocated-list/{menu-id}")
    public List<PermitDTO> getAllocatePermitList(@PathVariable("menu-id") Integer menuId) {
        return permitService.getAllocatePermitList(menuId);
    }

    /**
     * 权限配置
     * @param dto 菜单实体
     */
    @PermissionRequire(permission = "menu-info@permit-allocate")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 菜单管理 - 权限配置")
    @PutMapping("/allocate")
    public void allocatePermits(@RequestBody @Validated(value = { IdCheck.class }) MenuDTO dto) {
        permitService.allocatePermits(dto);
    }

    /**
     * 该角色下可授予的权限集合
     * @param roleId 角色Id
     * @return List<MePmDTO>
     */
    @PermissionRequire(permission = "role-info@permit-assign")
    @GetMapping("/assignable-list/{sys-role-id}")
    public List<MePmDTO> assignablePermits(@PathVariable("sys-role-id") Integer roleId) {
        return permitService.assignablePermits(roleId);
    }

    /**
     * 该角色下已授予的权限集合
     * @param roleId 角色Id
     * @return List<MePmDTO>
     */
    @PermissionRequire(permission = "role-info@permit-assign")
    @GetMapping("/assigned-list/{sys-role-id}")
    public List<MePmDTO> assignedPermits(@PathVariable("sys-role-id") Integer roleId) {
        return permitService.assignedPermits(roleId);
    }

    /**
     * 权限授予
     * @param role 角色实体
     */
    @PermissionRequire(permission = "role-info@permit-assign")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 角色管理 - 权限授予")
    @PutMapping("/assign")
    public void assignPermits(@RequestBody @Validated(value = { IdCheck.class }) RoleDTO role) {
        permitService.assignPermits(role);
    }
}
