package ${iServicePath};

import ${modelPath}.${modelName};
import ${iServicePath}.${iServiceName};
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author ${author}
 * @description ${tableName} 服务接口 ${iServiceDescription}
 * @project ${projectName}
 * @date ${dateTime}
 */
public interface ${iServiceName} extends IService${"<"}${modelName}${">"} {
    IPage${"<"}${modelName}${">"} get${modelName}Page(${modelName} dto);

    ${modelName} save${modelName}(${modelName} dto);

    ${modelName} get${modelName}ById(Integer id);

    boolean update${modelName}ById(${modelName} dto);

    boolean delete${modelName}ById(${modelName} dto);
}
