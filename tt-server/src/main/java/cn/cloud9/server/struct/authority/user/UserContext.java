package cn.cloud9.server.struct.authority.user;

import cn.cloud9.server.system.rbac.dto.MenuDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author OnCloud9
 * @description JWT用户内容，继承Map类，支持追加自定义属性
 * @project tt-server
 * @date 2022年11月20日 上午 11:51
 */
@Data
@NoArgsConstructor
public class UserContext {

    /* 用户ID */
    private Long userId;
    /* 用户名称 */
    private String userName;
    /* 用户账号 */
    private String userAccount;
    /* 用户令牌 */
    private String token;

    /* 所有角色 */
    private List<String> roles;

    /* 所有菜单 */
    private List<MenuDTO> menus;

    /* 所有权限 */
    private List<String> permitList;

    private final Map<String, Object> body = new ConcurrentHashMap<>();

    public UserContext(Long userId, String userName, String userAccount) {
        this.userId = userId;
        this.userName = userName;
        this.userAccount = userAccount;
    }

    public void put(String key, Object val) {
        body.put(key, val);
    }

    public Object get(String key) {
        final Object o = body.get(key);
        return Objects.isNull(o) ? null : o;
    }

    /**
     * 是否为admin
     * @return 判断结果
     */
    public boolean isAdmin() {
        return StringUtils.isNoneBlank(userAccount) && "admin".equalsIgnoreCase(userAccount);
    }
}
