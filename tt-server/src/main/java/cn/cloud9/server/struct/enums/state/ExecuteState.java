package cn.cloud9.server.struct.enums.state;

import cn.cloud9.server.struct.enums.annotation.DictCate;
import cn.cloud9.server.struct.enums.annotation.DictCode;
import cn.cloud9.server.struct.enums.annotation.DictName;
import lombok.Getter;

@Getter
public enum ExecuteState {
    WAITING("等待执行", 0),
    SUCCESS("执行成功", 1),
    DEFEAT("执行失败", 2),
    ;
    @DictCate
    public static final String CATE_DEFINE = "EXECUTE_STATE";
    @DictName
    private final String title;
    @DictCode
    private final Integer serialNo;

    ExecuteState(String title, Integer serialNo) {
        this.title = title;
        this.serialNo = serialNo;
    }
}
