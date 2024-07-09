package cn.cloud9.server.struct.response;

import cn.cloud9.server.struct.constant.ResultMessage;
import lombok.Data;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 04:23
 */
@Data
public class ApiResult<AnyType> implements Serializable {
    private static final long serialVersionUID = -6297582206786311744L;
    protected Integer code;
    protected String message;
    protected AnyType data;

    /**
     * 响应成功
     * @param data
     * @return ApiResult
     */
    public static <AnyClassType> ApiResult<AnyClassType> success(AnyClassType data) {
        final ApiResult<AnyClassType> result = new ApiResult<>();
        final ResultMessage success = ResultMessage.SUCCESS;
        result.code = success.getCode();
        result.message = success.getMessage();
        result.data = data;
        return result;
    }

    public static <ExtendsResultMessage extends ResultMessage> ApiResult<Void> error(ExtendsResultMessage errorMsg, Object ... args) {
        final ApiResult<Void> result = new ApiResult<>();
        result.message = MessageFormat.format(errorMsg.getMessage(), args);
        result.code = errorMsg.getCode();
        return result;
    }

    public static <ExtendsResultMessage extends ResultMessage> ApiResult<Void> error(ExtendsResultMessage errorMsg) {
        final ApiResult<Void> result = new ApiResult<>();
        result.message = errorMsg.getMessage();
        result.code = errorMsg.getCode();
        return result;
    }
}
