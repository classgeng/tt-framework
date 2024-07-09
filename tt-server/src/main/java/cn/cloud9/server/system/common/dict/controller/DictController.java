package cn.cloud9.server.system.common.dict.controller;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.dict.dto.DictDTO;
import cn.cloud9.server.struct.dict.service.DictService;
import cn.cloud9.server.struct.log.BusinessType;
import cn.cloud9.server.struct.log.SystemLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月13日 下午 11:47
 */
@RestController
@RequestMapping("${auth-server.api-path}/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @GetMapping("/name")
    public String getName(@RequestParam("mapKey") String mapKey) {
        return dictService.findNameFromRedis(mapKey);
    }

    @GetMapping("/cate")
    public List<DictDTO> getDictCate(@RequestParam("cateKey") String cateKey) {
        return dictService.findListFromRedis(cateKey);
    }

    /**
     * 字典翻页查询
     * @param dto 字典实体
     * @return IPage<DictDTO>
     */
    @PermissionRequire(permission = "dict-info@query")
    @SystemLog(module = "系统管理 - 通用管理 - 字典管理 - 分页查询")
    @PostMapping("/page")
    public IPage<DictDTO> getDictPage(@RequestBody DictDTO dto) {
        return dictService.getDictPage(dto);
    }

    /**
     * 按ID查询字典
     * @param id 字典主键
     * @return DictDTO 字典实体
     */
    @GetMapping("/{dictId}")
    public DictDTO getDictById(@PathVariable("dictId") Integer id) {
        return dictService.getById(id);
    }

    /**
     * 字典新增
     * @param dto 字典实体
     * @return boolean
     */
    @PermissionRequire(permission = "dict-info@insert")
    @SystemLog(businessType = BusinessType.INSERT, module = "系统管理 - 通用管理 - 字典管理 - 新增字典")
    @PutMapping("/add")
    public DictDTO addNewDict(@RequestBody DictDTO dto) {
        return dictService.addNewDict(dto);
    }

    /**
     * 字典更新
     * @param dto 字典实体
     * @return boolean
     */
    @PermissionRequire(permission = "dict-info@update")
    @SystemLog(businessType = BusinessType.UPDATE, module = "系统管理 - 通用管理 - 字典管理 - 更新字典")
    @PostMapping("/update")
    public boolean updateDict(@RequestBody DictDTO dto) {
        return dictService.updateDict(dto);
    }

    /**
     * 字典删除
     * @param dto 字典实体
     * @return boolean
     */
    @PermissionRequire(permission = "dict-info@delete")
    @SystemLog(businessType = BusinessType.DELETE, module = "系统管理 - 通用管理 - 字典管理 - 删除字典")
    @DeleteMapping("/delete")
    public boolean deleteDict(@RequestBody DictDTO dto) {
        return dictService.deleteDict(dto);
    }

}
