package cn.cloud9.server.struct.dict;

import cn.cloud9.server.struct.dict.dto.DictDTO;
import cn.cloud9.server.struct.lambda.ObjectBuilder;
import cn.cloud9.server.test.model.BuilderModel;
import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月24日 下午 09:15
 */
public class DictUtil {


    /**
     * 将任意模型集合转换为字典集合
     * @param modelList 模型集合
     * @param getCodeFun 获取code的方法
     * @param getNameFun 获取name的方法
     * @param cate 集合类别
     * @param <Model> 模型类型
     * @param <FieldType> 属性类型
     * @return 字典集合
     */
    public static <Model, FieldType> List<DictDTO> transToDictList(
            List<Model> modelList,
            Function<Model, FieldType> getCodeFun,
            Function<Model, FieldType> getNameFun,
            String cate
    ) {
        final List<DictDTO> dictList = new ArrayList<>(modelList.size());
        modelList.forEach(model -> {
            final FieldType code = getCodeFun.apply(model);
            final FieldType name = getNameFun.apply(model);
            final DictDTO dto = ObjectBuilder.builder(DictDTO::new)
                .with(DictDTO::setDictCode, String.valueOf(code))
                .with(DictDTO::setDictName, String.valueOf(name))
                .with(DictDTO::setDictCategory, cate)
                .build();
            dictList.add(dto);
        });
        return dictList;
    }
}
