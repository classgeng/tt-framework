package cn.cloud9.server.struct.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 04:33
 */
public abstract class BaseController {
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;
}
