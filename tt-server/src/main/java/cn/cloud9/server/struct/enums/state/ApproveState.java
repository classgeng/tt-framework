package cn.cloud9.server.struct.enums.state;

import cn.cloud9.server.struct.enums.annotation.DictCate;
import cn.cloud9.server.struct.enums.annotation.DictCode;
import cn.cloud9.server.struct.enums.annotation.DictName;
import lombok.Getter;

@Getter
public enum ApproveState {
    STAND_BY("正在审批", 0),
    PASSED("审批通过", 1),
    REJECT("审批驳回", 2),
    ;

    @DictCate
    public static final String CATE_NAME = "APPROVE_STATE";

    @DictName
    private final String define;

    @DictCode
    private final Integer code;

    ApproveState(String define, Integer code) {
        this.define = define;
        this.code = code;
    }
}
