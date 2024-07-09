package cn.cloud9.server.test.model.lombok;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.FieldNameConstants;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月09日 下午 08:27
 */
@Data
@Accessors(fluent = true, chain = true)
@FieldNameConstants
public class LombokModel {
    private Integer id;
    private String name;
    private Boolean status;


}
