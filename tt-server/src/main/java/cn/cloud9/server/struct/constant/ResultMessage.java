package cn.cloud9.server.struct.constant;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 04:29
 */
public enum ResultMessage {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "非法的请求参数"),
    REQUEST_FORBIDDEN(403, "禁止访问"),
    WRONG_REQUEST_METHOD(405, "错误的Http请求方式：[ {0} ]"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒介类型：[ {0} ]"),
    UNAUTHORIZED(401, "操作未授权，请登录"),
    NO_PERMISSION(402, "操作无权限 接口：[ {0} ] 权限值：[ {1} ]"),
    NO_API(404, "没有此API或资源路径 [ {0} ]"),
    NOT_FOUND_ERROR(404001, "未能在系统中找到[ {0} ]！"),
    SYSTEM_ERROR(500, "系统异常"),
    SUBMIT_FAIL(501, "提交失败, [ {0} ]"),
    ALREADY_EXIST(502, "[ {0} ]在系统中已经存在！"),
    NULL_ERROR(503, "[ {0} ]参数为空！"),
    WRONG_PASSWORD(504001, "密码错误"),
    WRONG_PASSWORD_LIMIT(504002, "密码错误次数超出限制，请稍候再试！ 时限[ {0} ]分钟, 剩余[ {1} ]秒"),

    JWT_TOKEN_EXPIRED(40001, "会话超时，请重新登录"),
    JWT_SIGNATURE(40002, "不合法的token，请认真比对 token 的签名"),
    JWT_ILLEGAL_ARGUMENT(40003, "缺少token参数"),
    JWT_GEN_TOKEN_FAIL(40004, "生成token失败"),
    JWT_PARSER_TOKEN_FAIL(40005, "解析token失败"),
    JWT_USER_INVALID(40006, "用户名或密码错误"),
    JWT_USER_ENABLED(40007, "用户已经被禁用！"),
    ;

    private final Integer code;
    private final String message;

    ResultMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
