package cn.cloud9.server.struct.datauth.config;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.datauth.map.IDataAuthMapService;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月25日 下午 02:53
 */
@RestController
@RequestMapping("${auth-server.api-path}/data-auth-config")
public class DataAuthConfigController {

    @Resource
    private IDataAuthConfigService dataAuthConfigService;

    @Resource
    private IDataAuthMapService dataAuthMapService;

    /**
     * data-auth-config/assignable-list/{user-id}
     * @param userId 用户id
     * @return List<DataAuthConfigDTO>
     */
    @PermissionRequire(permission = "user-info@data-auth-assign")
    @GetMapping("/assignable-list/{sys-user-id}")
    public List<DataAuthConfigDTO> getAssignableList(@PathVariable("sys-user-id") Integer userId) {
        return dataAuthConfigService.getAssignableList(userId);
    }

    /**
     *
     * @param userId
     * @return
     */
    @PermissionRequire(permission = "user-info@data-auth-assign")
    @GetMapping("/assigned-list/{sys-user-id}")
    public List<DataAuthConfigDTO> getAssignedList(@PathVariable("sys-user-id") Integer userId) {
        return dataAuthMapService.getAssignedList(userId);
    }

    /**
     * 数据权限授予
     * @param dto 用户实体
     */
    @PermissionRequire(permission = "user-info@data-auth-assign")
    @PutMapping("/assign")
    public void assign(@RequestBody UserDTO dto) {
        dataAuthMapService.assign(dto);
    }

}
