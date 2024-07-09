package cn.cloud9.server.system.common.hardware;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SystemServiceDTO {
    String name;
    String state;
    Page<Map<String, Object>> page;


    /* 查询条件 */
    public boolean queryWrapper(Map<String, Object> row) {
        List<Boolean> resList = new ArrayList<>();
        resList.add(serviceNameLike(row));
        resList.add(stateIs(row));
        return resList.stream().allMatch(b -> b);
    }

    /**
     * 服务名称模糊查询
     * @param row stream迭代的行对象
     * @return boolean
     */
    private boolean serviceNameLike(Map<String, Object> row) {
        final String serviceName = String.valueOf(row.get("name"));
        final boolean isEmptyParam = StringUtils.isBlank(name);
        if (isEmptyParam) return true;
        return StringUtils.isNotBlank(serviceName) && serviceName.contains(name);
    }

    /**
     * 服务状态匹配查询
     * @param row stream迭代的行对象
     * @return boolean
     */
    private boolean stateIs(Map<String, Object> row) {
        final String serviceState = String.valueOf(row.get("state"));
        final boolean isEmptyParam = StringUtils.isBlank(state);
        if (isEmptyParam) return true;
        return StringUtils.isNotBlank(serviceState) && serviceState.equals(state);
    }
}
