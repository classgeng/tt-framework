package cn.cloud9.server.struct.log;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 日志上下文类
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月08日 下午 09:04
 */
@Slf4j
public class SystemLogContext {

    private static final ThreadLocal<SystemLogDTO> sysLogContext = new ThreadLocal<>();

    /**
     * 获取日志记录实体
     * @return 日志记录实体
     */
    public static SystemLogDTO get() {
        final SystemLogDTO log = sysLogContext.get();
        final boolean isEmptyLog = Objects.isNull(log);
        return isEmptyLog ? new SystemLogDTO() : log;
    }

    /**
     * 传入日志记录实体
     * @param dto 日志记录实体
     */
    public static void set(SystemLogDTO dto) {
        sysLogContext.set(dto);
    }

    /**
     * 移除日志记录
     */
    public static void remove() {
        sysLogContext.remove();
    }

    /**
     * 入参方法直接执行，暴露线程中的日志DTO进行操作
     * @param consumer
     */
    public static void tryCatch(Consumer<SystemLogDTO> consumer) {
        try {
            final SystemLogDTO logDTO = get();
            consumer.accept(logDTO);
        } catch (Exception e) {
            log.error("记录操作日志异常 {}", e.getMessage());
            sysLogContext.remove();
        }
    }
}
