package cn.cloud9.server.test.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;


/**
 * 转换器接口
 */
@Mapper(componentModel = "spring")
public interface DemoConverter {
    DemoConverter INSTANCE = Mappers.getMapper(DemoConverter.class);


    /* @BeanMapping(ignoreByDefault = true) 阻止MapStruct默认同名字段赋值行为 */
    // @BeanMapping(ignoreByDefault = true)
    /* DTO -> ENTITY */
    @Mappings(value = {
        @Mapping(source = "fieldA", target = "fieldD"),
        @Mapping(source = "fieldB", target = "fieldE"),
        @Mapping(source = "fieldC", target = "fieldF"),
    })
    DemoEntity dto2entity(DemoDTO demoDTO);

    /* ENTITY -> DTO  */
    @InheritInverseConfiguration(name = "dto2entity")
    DemoDTO entity2dto(DemoEntity demoEntity);

    /* List<ENTITY -> DTO>  */
    @InheritConfiguration(name = "entity2dto")
    List<DemoDTO> entity2dto(List<DemoEntity> demoEntity);

    /* List<DTO -> ENTITY> */
    @InheritInverseConfiguration(name = "entity2dto")
    List<DemoEntity> dto2entity(List<DemoDTO> demoDTO);
}
