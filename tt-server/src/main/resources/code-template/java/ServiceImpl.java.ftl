package ${serviceImplPath};

import ${modelPath}.${modelName};
import ${mapperPath}.${mapperName};
import ${iServicePath}.${iServiceName};
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author ${author}
 * @description ${tableName} 服务实现类 ${serviceImplDescription}
 * @project ${projectName}
 * @date ${dateTime}
 */
@Service("${serviceImplName ? uncap_first}")
public class ${serviceImplName} extends ServiceImpl${"<"}${mapperName}, ${modelName}${">"} implements ${iServiceName} {

    @Override
    public IPage ${"<"}${modelName}${">"} get${modelName}Page(${modelName} dto) {
        return null;
    }

    @Override
    public ${modelName} save${modelName}(${modelName} dto) {
        return null;
    }

    @Override
    public ${modelName} get${modelName}ById(Integer id) {
        return null;
    }

    @Override
    public boolean update${modelName}ById(${modelName} dto) {
        return true;
    }

    @Override
    public boolean delete${modelName}ById(${modelName} dto) {
        return true;
    }
}
