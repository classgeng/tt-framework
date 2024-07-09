package cn.cloud9.server.struct.log.aspect;

import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.log.SystemLog;
import cn.cloud9.server.struct.log.SystemLogContext;
import cn.cloud9.server.struct.log.SystemLogDAO;
import cn.cloud9.server.struct.log.SystemLogDTO;
import cn.cloud9.server.struct.util.AddressUtil;
import cn.cloud9.server.struct.util.IpUtil;
import cn.cloud9.server.struct.util.ServletUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 05:08
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {

    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private SystemLogDAO systemLogDAO;


    @Pointcut("@annotation(cn.cloud9.server.struct.log.SystemLog)")
    public void logPointCut() {
    }

    /**
     * 方法执行前预处理日志
     * @param joinPoint
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        /* 没有注解不处理 */
        final SystemLog systemLog = method.getAnnotation(SystemLog.class);
        if (Objects.isNull(systemLog)) return;

        SystemLogContext.tryCatch(dto -> {

            /* 执行位置信息 */
            dto.setBusinessType(systemLog.businessType().getName());
            dto.setModule(systemLog.module());
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();
            dto.setMethod(className + "@" + methodName);

            /* 请求信息 */
            final HttpServletRequest request = ServletUtil.getRequest();
            final String ipAddr = IpUtil.getIpAddr(request);
            dto.setIp(ipAddr);
            dto.setAddress(AddressUtil.getRealAddressByIP(ipAddr));
            dto.setRequestMethod(request.getMethod());
            dto.setUrl(request.getRequestURI());
            try {
                final String json = ServletUtil.getRequestBodyJson(request);
                final Map<String, String> map = ServletUtil.getParameterMap(request);
                dto.setParam(json + JSON.toJSONString(map));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Request参数解析异常：{}", e.getMessage());
                dto.setParam("非法参数：" + e.getMessage());
            }

            /* 记录操作用户信息 */
            final String osInfo = request.getHeader("User-Agent");
            dto.setOperateSystem(osInfo);

            final UserContext userContext = UserContextHolder.getUserContext();
            dto.setOperator(userContext.getUserAccount());

            dto.setCreateTime(LocalDateTime.now());
            SystemLogContext.set(dto);
        });
    }

    /**
     * 执行后的处理
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "apiResult")
    public void doAfterReturning(JoinPoint joinPoint, Object apiResult) {
        SystemLogContext.tryCatch(dto -> {
            /* 结束时间 */
            final LocalDateTime end = LocalDateTime.now();
            final long duration = dto.getCreateTime().until(end, ChronoUnit.MILLIS);
            dto.setFinishTime(end);
            dto.setDuration(duration);

            /* 结果状态 */
            dto.setStatus("成功");
            dto.setResult(JSON.toJSONString(apiResult));

            /* 存储日志 */
            log.info("<- - - - 存储日志： {} - - - ->", JSON.toJSONString(dto));
            systemLogDAO.insert(dto);
            /* 存储完毕，删除日志 */
            SystemLogContext.remove();
        });
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        SystemLogContext.tryCatch(dto -> {
            /* 结束时间 */
            final LocalDateTime end = LocalDateTime.now();
            final long duration = dto.getCreateTime().until(end, ChronoUnit.MILLIS);
            dto.setFinishTime(end);
            dto.setDuration(duration);

            /* 结果状态 */
            dto.setStatus("失败");
            dto.setErrorMessage("失败原因：" + e.getMessage());

            /* 存储日志 */
            log.info("<- - - - 存储日志： {} - - - ->", JSON.toJSONString(dto));
            systemLogDAO.insert(dto);
            /* 存储完毕，删除日志 */
            SystemLogContext.remove();
        });
    }

}
