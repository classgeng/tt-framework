package cn.cloud9.server.struct.datauth.test;

import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.datauth.DataAuthActive;
import cn.cloud9.server.struct.datauth.DataAuthIdent;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.struct.validator.groups.IdCheck;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月25日 上午 10:47
 */
@RestController
@RequestMapping("${auth-server.api-path}/data-auth-test")
public class DataAuthTestController extends BaseController {

    @Resource
    private IDataAuthTestService dataAuthTestService;

    @DataAuthActive(ident = DataAuthIdent.DATA_AUTH_TEST, sqlStatementId = "cn.cloud9.server.struct.datauth.test.DataAuthTestMapper.selectPage")
    @SystemLog(module = "实验功能 - 数据授权测试 - 分页查询")
    @PermissionRequire(permission = "data-auth-test@query")
    @PostMapping("/page")
    public IPage<DataAuthTestDTO> getDataAuthTestPage(@RequestBody DataAuthTestDTO dto) {
        return dataAuthTestService.lambdaQuery()
                .orderByDesc(DataAuthTestDTO::getCreateTime)
                .page(dto.getPage());
    }

    @GetMapping("/{id}")
    public DataAuthTestDTO getDataAuthTestById(@PathVariable("id") Integer id) {
        return dataAuthTestService.getById(id);
    }

    @SystemLog(module = "实验功能 - 数据授权测试 - 新增")
    @PermissionRequire(permission = "data-auth-test@insert")
    @PutMapping("/add")
    public DataAuthTestDTO addDataAuthTest(@RequestBody DataAuthTestDTO dto) {
        dto.preInsert();
        final boolean save = dataAuthTestService.save(dto);
        return save ? dto : null;
    }

    @SystemLog(module = "实验功能 - 数据授权测试 - 更新")
    @PermissionRequire(permission = "data-auth-test@update")
    @PostMapping("/update")
    public void updateDataAuthTest(@RequestBody @Validated(value = IdCheck.class) DataAuthTestDTO dto) {
        dto.preUpdate();
        dataAuthTestService.updateById(dto);
    }

    @SystemLog(module = "实验功能 - 数据授权测试 - 删除")
    @PermissionRequire(permission = "data-auth-test@delete")
    @DeleteMapping("/delete")
    public void deleteDataAuthTest(@RequestBody @Validated(value = IdCheck.class) DataAuthTestDTO dto) {
        dataAuthTestService.updateById(dto);
    }



}
