package cn.cloud9.server.system.rbac.controller;

import cn.cloud9.server.struct.authority.annotation.NoLoginRequire;
import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.struct.masking.annotation.ActiveDataMasking;
import cn.cloud9.server.struct.validator.groups.IdCheck;
import cn.cloud9.server.struct.validator.groups.InsertCheck;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import cn.cloud9.server.system.rbac.dto.map.RoUsDTO;
import cn.cloud9.server.system.rbac.service.IUserService;
import cn.cloud9.server.system.rbac.service.impl.RbacService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description 用户类
 * @project tt-server
 * @date 2022年11月20日 下午 02:18
 */
@Slf4j
@ActiveDataMasking
@RestController
@RequestMapping("${auth-server.api-path}/rbac/user")
public class UserController extends BaseController {

    @Resource
    private IUserService userService;

    @Resource
    private RbacService rbacService;

    /**
     * 创建新用户 /user/new
     * @param user
     * @return
     */
    @NoLoginRequire
    @PostMapping("/new")
    public UserDTO newUser(@RequestBody @Validated(value = InsertCheck.class) UserDTO user) {
        return userService.newUser(user);
    }

    /**
     * 查询用户 /user/page
     * @param query 实体查询条件
     * @return 分页对象
     */
    @PermissionRequire(permission = "user-info@query")
    @SystemLog(businessType = BusinessType.QUERY, module = "系统管理 - 权限维护 - 用户管理 - 分页查询")
    @PostMapping("/page")
    public IPage<UserDTO> queryUserPage(@RequestBody UserDTO query) {
        return userService.queryUserPage(query);
    }

    /**
     * 按ID查询用户 /user/${userId}
     * @param userId 用户Id
     * @return 用户实体
     */
    @GetMapping("/{sys-user-id}")
    public UserDTO getUserById(@PathVariable("sys-user-id") Integer userId) {
        return userService.getById(userId);
    }

    /**
     * 新增用户 /user/insert
     * @param user 用户实体
     * @return 用户实体
     */
    @PermissionRequire(permission = "user-info@insert")
    @SystemLog(businessType = BusinessType.INSERT, module = "系统管理 - 权限维护 - 用户管理 - 新增用户")
    @PutMapping("/insert")
    public UserDTO insertUser(@RequestBody @Validated(value = InsertCheck.class) UserDTO user) {
        user.preInsert();
        final boolean save = userService.save(user);
        return save ? user : null;
    }

    /**
     * 更新用户 /user/update
     * @param user 用户实体
     * @return 用户实体
     */
    @PermissionRequire(permission = "user-info@update")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 权限维护 - 用户管理 - 更新用户")
    @PostMapping("/update")
    public boolean updateUser(@RequestBody @Validated(value = IdCheck.class) UserDTO user) {
        user.preUpdate();
        return userService.updateById(user);
    }

    /**
     * 删除用户 /user/delete
     * @param user 用户实体
     * @return 用户实体
     */
    @PermissionRequire(permission = "user-info@delete")
    @SystemLog(businessType = BusinessType.DELETE, module = "系统管理 - 权限维护 - 用户管理 - 删除用户")
    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestBody @Validated(value = IdCheck.class) UserDTO user) {

        /* 安全删除校验 */
        final RoUsDTO roUs = new RoUsDTO();
        roUs.setUserId(user.getId());
        rbacService.deleteRoleOrUserSafetyCheck(roUs);

        return userService.removeById(user);
    }

}
