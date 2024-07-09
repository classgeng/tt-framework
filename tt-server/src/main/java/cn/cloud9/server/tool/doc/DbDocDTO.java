package cn.cloud9.server.tool.doc;

import cn.smallbun.screw.core.engine.EngineFileType;
import lombok.Data;

import java.util.List;

@Data
public class DbDocDTO {

    /* 文档名称 */
    private String docName;
    /* 文档标题 */
    private String docTitle;
    /* 组织名称 */
    private String organization;
    /* 组织连接 */
    private String organizationUrl;

    private String version;
    private String description;
    private Integer docType; /* 1,HTML 2,WORD 3,MD */

    /* 按指定的表名生成 */
    private List<String> specifyTables;
    /* 按指定的表前缀生成 */
    private List<String> specifyTablePrefixes;
    /* 按指定的表后缀生成 */
    private List<String> specifyTableSuffixes;
    /* 需要忽略的表名 */
    private List<String> ignoreTables;
    /* 需要忽略的表前缀 */
    private List<String> ignoreTablePrefixes;
    /* 需要忽略的表后缀 */
    private List<String> ignoreTableSuffixes;


    public EngineFileType getDocType() {
        switch (docType) {
            case 2: return EngineFileType.WORD;
            case 3: return EngineFileType.MD;
            default: return EngineFileType.HTML;
        }
    }
}
