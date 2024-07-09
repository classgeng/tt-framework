package ${controllerPath};

import ${modelPath}.${modelName};
import ${iServicePath}.${iServiceName};
import ${serviceImplPath}.${serviceImplName};
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author ${author}
 * @description ${tableName} 控制器类 ${controllerDescription}
 * @project ${projectName}
 * @date ${dateTime}
 */
@RestController
@RequestMapping("${urlPath}")
public class ${controllerName} {

    @Resource
    private ${iServiceName} ${serviceImplName ? uncap_first};

    /**
     * 翻页查询
     * @param dto ${modelName}
     * @return IPage<${modelName}>
     */
    @PostMapping("/page")
    public IPage${"<"}${modelName}${">"} get${modelName}Page(@RequestBody ${modelName} dto) {
        return ${serviceImplName ? uncap_first}.get${modelName}Page(dto);
    }

    /**
     * 保存
     * @param dto ${modelName}
     * @return ${modelName}
     */
    @PutMapping("/save")
    public ${modelName} save${modelName}(@RequestBody ${modelName} dto) {
        return ${serviceImplName ? uncap_first}.save${modelName}(dto);
    }

    /**
     * 按Id获取
     * @param id 主键
     * @return ${modelName}
     */
    @GetMapping("/get/{id}")
    public ${modelName} get${modelName}ById(@PathVariable("id") Integer id) {
        return ${serviceImplName ? uncap_first}.get${modelName}ById(dto);
    }

    /**
     * 按Id更新
     * @param dto ${modelName}
     * @return boolean
     */
    @PostMapping("/update")
    public boolean update${modelName}ById(@RequestBody ${modelName} dto) {
        return ${serviceImplName ? uncap_first}.update${modelName}ById(dto);
    }

    /**
     * 按Id删除
     * @param dto ${modelName}
     * @return boolean
     */
    @PostMapping("/delete")
    public boolean delete${modelName}ById(@RequestBody ${modelName} dto) {
        return ${serviceImplName ? uncap_first}.delete${modelName}ById(dto);
    }

}
