package cn.cloud9.server.struct.log;

import lombok.Getter;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 07:49
 */
@Getter
public enum BusinessType {

    INSERT("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    QUERY("查询");

    BusinessType(String name) {
        this.name = name;
    }

    private final String name;
}
