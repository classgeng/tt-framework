package cn.cloud9.server.test.model;

import cn.cloud9.server.struct.dict.annotation.DictFrom;
import lombok.Data;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月12日 下午 10:20
 */
@Data
public class DictAspectModel {

    private Integer dictCode;
    @DictFrom(srcCate = "1006", srcField = "dictCode")
    private String transName;

    private String movieType;
    @DictFrom(srcCate = "1013", srcField = "movieType", isMulti = true)
    private String movieTypeName;

    List<String> strList;

    List<DictAspectModel> models;
}
