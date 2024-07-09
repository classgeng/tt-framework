package cn.cloud9.server.system.common.param;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月07日 下午 11:19
 */
@RestController
@RequestMapping("${auth-server.api-path}/sys-param")
public class SystemParamController extends BaseController {

    @Resource
    private ISystemParamService systemParamService;

    /**
     * 系统参数 分页查询
     * @param dto SystemParamDTO
     * @return IPage<SystemParamDTO>
     */
    @PermissionRequire(permission = "system-param@query")
    @SystemLog(module = "系统管理 - 通用管理 - 系统参数 - 查询")
    @PostMapping("/page")
    public IPage<SystemParamDTO> getSystemParamPage(@RequestBody SystemParamDTO dto) {
        return systemParamService.getSystemParamPage(dto);
    }

    /**
     * 系统参数 详情获取
     * @param id 系统参数主键
     * @return SystemParamDTO
     */
    @GetMapping("/{sys-param-id}")
    public SystemParamDTO getSystemParamById(@PathVariable("sys-param-id") Integer id) {
        return systemParamService.getById(id);
    }

    /**
     * 系统参数 新增
     * @param dto SystemParamDTO
     * @return SystemParamDTO
     */
    @PermissionRequire(permission = "system-param@insert")
    @SystemLog(businessType = BusinessType.INSERT, module = "系统管理 - 通用管理 - 系统参数 - 新增")
    @PutMapping("/add")
    public SystemParamDTO addSystemParam(@RequestBody SystemParamDTO dto) {
        return systemParamService.addSystemParam(dto);
    }

    /**
     * 系统参数 更新"
     * @param dto SystemParamDTO
     * @return boolean
     */
    @PermissionRequire(permission = "system-param@update")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 通用管理 - 系统参数 - 更新")
    @PostMapping("/update")
    public boolean updateSystemParam(@RequestBody SystemParamDTO dto) {
        return systemParamService.updateSystemParam(dto);
    }

    /**
     * 系统参数 更新"
     * @param dto SystemParamDTO
     * @return boolean
     */
    @PermissionRequire(permission = "system-param@delete")
    @SystemLog(businessType = BusinessType.DELETE, module = "系统管理 - 通用管理 - 系统参数 - 删除")
    @DeleteMapping("/delete")
    public boolean deleteSystemParam(@RequestBody SystemParamDTO dto) {
        return systemParamService.deleteSystemParam(dto);
    }


}
