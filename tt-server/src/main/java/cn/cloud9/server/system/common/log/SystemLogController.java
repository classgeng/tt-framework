package cn.cloud9.server.system.common.log;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLogDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月11日 下午 09:55
 */
@RestController
@RequestMapping("${auth-server.api-path}/sys-log")
public class SystemLogController {

    @Resource
    private SystemLogService systemLogService;

    /**
     * 获取全部业务类型
     * @return List<String> 业务类型名称
     */
    @GetMapping("/bus-type-list")
    public List<String> getBusinessTypeList() {
        return Arrays.stream(BusinessType.values()).map(BusinessType::getName).collect(Collectors.toList());
    }

    /**
     * 系统日志翻页
     * @param dto 系统日志实体
     * @return IPage<SystemLogDTO> 翻页对象
     */
    @PermissionRequire(permission = "system-log@update")
    @PostMapping("/page")
    public IPage<SystemLogDTO> getSystemLogPage(@RequestBody SystemLogDTO dto) {
        return systemLogService
            .lambdaQuery()
            .eq(StringUtils.isNotBlank(dto.getBusinessType()), SystemLogDTO::getBusinessType, dto.getBusinessType())
            .eq(StringUtils.isNotBlank(dto.getStatus()), SystemLogDTO::getStatus, dto.getStatus())
            .eq(StringUtils.isNotBlank(dto.getRequestMethod()), SystemLogDTO::getRequestMethod, dto.getRequestMethod())
            .like(StringUtils.isNotBlank(dto.getModule()), SystemLogDTO::getModule, dto.getModule())
            .like(StringUtils.isNotBlank(dto.getIp()), SystemLogDTO::getIp, dto.getIp())
            .like(StringUtils.isNotBlank(dto.getOperator()), SystemLogDTO::getOperator, dto.getOperator())
            .orderByDesc(SystemLogDTO::getCreateTime)
            .page(dto.getPage());
    }

    /**
     * 按ID获取系统日志
     * @param id 系统日志ID
     * @return SystemLogDTO
     */
    @GetMapping("/{sys-log-id}")
    public SystemLogDTO getSystemLogById(@PathVariable("sys-log-id") Integer id) {
        return systemLogService.lambdaQuery().getBaseMapper().selectById(id);
    }

    /**
     * 清空日志信息
     */
    @PermissionRequire(permission = "system-log@delete")
    @DeleteMapping("/flush")
    public void flushSystemLog() {
        systemLogService.flushSystemLog();
    }

}
