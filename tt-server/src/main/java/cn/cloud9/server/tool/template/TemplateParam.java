package cn.cloud9.server.tool.template;

import cn.cloud9.server.struct.util.DateUtils;
import cn.hutool.db.Entity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TemplateParam {

    /* 注释信息部分 */
    private String author;
    private String projectName;
    private String dateTime;
    private String modelDescription;
    private String mapperDescription;
    private String iServiceDescription;
    private String serviceImplDescription;
    private String controllerDescription;

    /* 包路径 */
    private String modelPath;
    private String mapperPath;
    private String iServicePath;
    private String serviceImplPath;
    private String controllerPath;

    /* 类名称 & 文件名 */
    private String modelName;
    private String mapperName;
    private String iServiceName;
    private String serviceImplName;
    private String controllerName;
    private String apiName;

    /* controller接口与路径 */
    private String urlPath;

    /* 元数据信息 */
    private Integer dbConnId;
    private String schemaName;
    private String tableName;
    private List<Entity> columns;

    /* 模板类型枚举编号 */
    private Integer templateCode;

    /**
     * 设置当前时间
     */
    public void setDateTime() {
        dateTime = DateUtils.format(LocalDateTime.now(), DateUtils.DEFAULT_DATE_TIME_FORMAT);
    }

    public TemplateType getTemplateTypeByCode() {
        return TemplateType.getAvailableTypes().stream()
                .filter(tt -> tt.getCode().equals(templateCode))
                .findFirst()
                .orElse(TemplateType.NONE);
    }

    public static TemplateType getTemplateTypeByCode(Integer ttCode) {
        return TemplateType.getAvailableTypes().stream()
                .filter(tt -> tt.getCode().equals(ttCode))
                .findFirst()
                .orElse(TemplateType.NONE);
    }
}
