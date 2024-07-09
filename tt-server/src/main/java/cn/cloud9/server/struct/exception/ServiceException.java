package cn.cloud9.server.struct.exception;

import cn.cloud9.server.struct.constant.ResultMessage;

import java.text.MessageFormat;

/**
 * 业务异常类
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 04:25
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -7034897190745766939L;
    private ResultMessage resultMessage;
    private Object[] args;

    public ServiceException() {
        super(ResultMessage.SYSTEM_ERROR.getMessage());
        this.resultMessage = ResultMessage.SYSTEM_ERROR;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause, ResultMessage errorMsg) {
        super(errorMsg.getMessage(), cause);
        this.resultMessage = errorMsg;
    }

    public ServiceException(Throwable cause, ResultMessage errorMsg, Object ... args) {
        super(MessageFormat.format(errorMsg.getMessage(), args), cause);
        this.resultMessage = errorMsg;
        this.args = args;
    }

    public ServiceException(ResultMessage errorMsg) {
        super(errorMsg.getMessage());
        this.resultMessage = errorMsg;
    }

    public ServiceException(ResultMessage errorMsg, Object ... args) {
        super(MessageFormat.format(errorMsg.getMessage(), args));
        this.resultMessage = errorMsg;
        this.args = args;
    }

    public ResultMessage getResultMessage() {
        return resultMessage;
    }

    public Object[] getArgs() {
        return args;
    }
}
