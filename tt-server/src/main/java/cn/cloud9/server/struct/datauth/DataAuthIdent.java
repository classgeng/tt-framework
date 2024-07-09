package cn.cloud9.server.struct.datauth;

import lombok.Getter;

/**
 * 数据授权标识枚举
 */
@Getter
public enum DataAuthIdent {
    DATA_AUTH_TEST("数据授权测试", "data-auth-test", true),

    ;
    private final String identKey;
    private final String identName;
    private final boolean isActive;

    /**
     *
     * @param identName 业务标识名称
     * @param identKey 业务标识key
     * @param isActive 是否激活
     */
    DataAuthIdent(String identName, String identKey, boolean isActive) {
        this.identName = identName;
        this.identKey = identKey;
        this.isActive = isActive;
    }
}
