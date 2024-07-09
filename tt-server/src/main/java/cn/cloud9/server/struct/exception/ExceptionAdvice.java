package cn.cloud9.server.struct.exception;

import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.exception.ServiceException;
import cn.cloud9.server.struct.response.ApiResult;
import kotlin.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.*;

/**
 * @author OnCloud9
 * @description 注解@RestControllerAdvice 才会优先返回异常结果...?
 * @project tt-server
 * @date 2022年11月06日 下午 04:39
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * IllegalArgumentException异常处理返回json
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ApiResult<Void> badRequestException(IllegalArgumentException e) {
        return ApiResult.error(ResultMessage.BAD_REQUEST);
    }

    /**
     * AccessDeniedException异常处理返回json
     * 返回状态码:403
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ApiResult<Void> badMethodExpressException(AccessDeniedException e) {
        return ApiResult.error(ResultMessage.REQUEST_FORBIDDEN);
    }

    /**
     * 错误的请求方式响应
     * 返回状态码:405
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<Void> wrongHttpMethod(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ApiResult.error(ResultMessage.WRONG_REQUEST_METHOD, e.getMessage());
    }

    /**
     * 返回状态码:415
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ApiResult<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ApiResult.error(ResultMessage.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
    }

    /**
     * 拦截业务异常响应
     * @param se
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public ApiResult<Void> badRequest(ServiceException se) {
        log.error(se.getMessage(), se);
        final ResultMessage resultMessage = se.getResultMessage();

        boolean isEmptyArgs = ArrayUtils.isEmpty(se.getArgs());
        return isEmptyArgs ? ApiResult.error(resultMessage) : ApiResult.error(resultMessage, se.getArgs());
    }

    /**
     * 拦截普通异常响应
     * @param se
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResult<Void> badRequest(Exception se) {
        log.error(se.getMessage(), se);
        return ApiResult.error(ResultMessage.SYSTEM_ERROR);
    }

    /**
     * Controller @Validated注解校验异常处理
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<Void> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        Iterator<ObjectError> iterator = allErrors.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            ObjectError error = iterator.next();
            String message = Objects.requireNonNull(error.getCodes())[0] + ":" + error.getDefaultMessage();
            builder.append(message);
        }
        return ApiResult.error(ResultMessage.SUBMIT_FAIL, builder.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ApiResult<Void> constraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            ConstraintViolation<?> constraint = iterator.next();
            String message = constraint.getPropertyPath() + ":" + constraint.getMessage();
            builder.append(message);
        }
        return ApiResult.error(ResultMessage.SUBMIT_FAIL, builder.toString());
    }
}
