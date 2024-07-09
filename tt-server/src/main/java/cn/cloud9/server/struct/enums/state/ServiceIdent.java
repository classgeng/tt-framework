package cn.cloud9.server.struct.enums.state;

import cn.cloud9.server.struct.enums.annotation.DictCate;
import cn.cloud9.server.struct.enums.annotation.DictCode;
import cn.cloud9.server.struct.enums.annotation.DictName;
import lombok.Getter;

@Getter
public enum ServiceIdent {
    SERVICE_A("SI-1001", "报销申请"),
    SERVICE_B("SI-1002", "用款申请");

    @DictCate
    public static final String CATE_NAME = "SERVICE_IDENT";
    @DictCode
    private final String ident;
    @DictName
    private final String name;

    ServiceIdent(String ident, String name) {
        this.ident = ident;
        this.name = name;
    }
}
