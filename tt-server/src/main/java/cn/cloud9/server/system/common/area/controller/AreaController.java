package cn.cloud9.server.system.common.area.controller;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.system.common.area.dto.AreaDTO;
import cn.cloud9.server.system.common.area.service.IAreaService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description 行政区域
 * @project tt-server
 * @date 2022年11月19日 下午 07:58
 */
@RestController
@RequestMapping("${auth-server.api-path}/area")
public class AreaController {

    @Resource
    private IAreaService areaService;

    /**
     *  area/tree
     * 获取区域列表
     * @param area AreaDTO实体
     * @return List<AreaDTO>
     */
    @GetMapping("/list")
    public List<AreaDTO> getAreaList(@ModelAttribute AreaDTO area) {
        return areaService.getAreaList(area);
    }

    /**
     * area/tree
     * 获取区域树
     * @return List<AreaDTO>
     */
    @GetMapping("/tree")
    public List<AreaDTO> getAreaTree() {
        return areaService.getAreaTree();
    }

    /**
     * area/page
     * 区域翻页查询
     * @param dto AreaDTO实体
     * @return IPage<AreaDTO>
     */
    @PermissionRequire(permission = "area-info@query")
    @SystemLog(module = "系统管理 - 通用管理 - 行政区域 - 分页查询")
    @PostMapping("/page")
    public IPage<AreaDTO> getAreaPage(@RequestBody AreaDTO dto) {
        return areaService.getAreaPage(dto);
    }

}
