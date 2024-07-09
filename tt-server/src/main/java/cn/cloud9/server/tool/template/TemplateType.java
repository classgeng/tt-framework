package cn.cloud9.server.tool.template;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum TemplateType {
    /* 没有文件 */
    NONE(null, null, null, null),

    /* 后台文件 */
    MODEL(1001, "/java/Model.java.ftl", "modelName", ".java"),
    MAPPER(1002, "/java/Mapper.java.ftl", "mapperName", ".java"),
    I_SERVICE(1003, "/java/IService.java.ftl", "iServiceName", ".java"),
    SERVICE_IMPL(1004, "/java/ServiceImpl.java.ftl", "serviceImplName", ".java"),
    CONTROLLER(1005, "/java/Controller.java.ftl", "controllerName", ".java"),

    /* 前端文件 */
    VUE_API(2001, "/vue/Api.js.ftl", "apiName", ".js"),
    /* VUE_VIEW(2002, "/vue/View.vue.ftl", "viewName", ".vue"), */
    VUE_VIEW(2002, "/vue/View.vue.ftl", null, null),
    ;

    /* 编号和模板文件路径 */
    private final Integer code;
    private final String path;
    private final String filenameKey;
    private final String suffix;

    TemplateType(Integer code, String path, String filenameKey, String suffix) {
        this.code = code;
        this.path = path;
        this.filenameKey = filenameKey;
        this.suffix = suffix;
    }

    /**
     * 获取可用的模板类型集合
     * @return List<TemplateType>
     */
    public static List<TemplateType> getAvailableTypes() {
        return Arrays
            .stream(values())
            .filter(t -> Objects.nonNull(t.code) && Objects.nonNull(t.path) && Objects.nonNull(t.filenameKey) && Objects.nonNull(t.suffix))
            .collect(Collectors.toList());
    }

}
