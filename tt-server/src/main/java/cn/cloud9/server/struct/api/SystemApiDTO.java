package cn.cloud9.server.struct.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月03日 下午 08:31
 */
@Data
public class SystemApiDTO {

    /* 处理器方法名 */
    private String handlerMethodName;

    /* 处理器方法返回类型 */
    private String handlerMethodReturn;

    /* 处理器方法参数列表 */
    private String handlerMethodParams;

    /* 处理器方法所在包 */
    private String handlerPackage;

    /* 处理器方法所在类 */
    private String handlerController;

    /* 处理器接口地址 */
    private String apiPath;

    /* 处理器请求方式 */
    private String methodType;

    /* 处理器请求MimeType类型 */
    private String consumeMimeType;

    /* 处理器响应MimeType类型 */
    private String produceMimeType;

    /* 处理器入参Header信息 */
    private String requireHeaders;

    /* 处理器入参Get Param参数 */
    private String requireParams;


    private Page<SystemApiDTO> page;
}
