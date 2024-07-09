package cn.cloud9.server.system.rbac.service;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface IUserService extends IService<UserDTO> {
    UserDTO newUser(UserDTO user);

    UserContext userLogin(UserDTO user);

    void userLogout(UserContext userContext);

    UserContext userLoginByToken(HttpServletRequest request);

    IPage<UserDTO> queryUserPage(UserDTO query);
}
