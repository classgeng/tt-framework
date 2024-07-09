package cn.cloud9.server.struct.sse;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月10日 下午 10:33
 */
@Slf4j
@Service("sseService")
public class ServerSentEventService {

    private static final Map<String, SseEmitter> SESSION_MAP = new ConcurrentHashMap<>();
    private static final long NO_TIMEOUT = 0L;
    /**
     * 客户端进入接口后建立连接，接口需要返回此会话资源
     * @param clientId
     * @return
     */
     public SseEmitter connectionEstablish(String clientId) {
         final SseEmitter emitter = new SseEmitter(NO_TIMEOUT);

         /*
            emitter在默认的超时时限内 没有向客户端推送消息会触发超时，Spring会抛出异步请求超时异常
            new SseEmitter() 默认超时时间30秒
            new SseEmitter(0L) 可以设置0L为永不超时
          */
         emitter.onTimeout(() -> {
             log.info(" - - - - SSE连接超时 " + clientId + " - - - - ");
             SESSION_MAP.remove(clientId);
         });
         /* 然后调用completion结束 */
         emitter.onCompletion(() -> {
             log.info(" - - - - - SSE会话结束 " + clientId + " - - - - ");
             SESSION_MAP.remove(clientId);
         });

         /* 连接异常可能出现在跨域请求的情况 */
         emitter.onError(exception -> {
             log.error("- - - - - SSE连接异常 " + clientId + " - - - - -", exception);
             SESSION_MAP.remove(clientId);
         });
         SESSION_MAP.put(clientId, emitter);
         return emitter;
     }

    /**
     *
     * @param clientId
     * @param message
     */
     public void sendMessageToClient(String clientId, String message) {
         final SseEmitter emitter = SESSION_MAP.get(clientId);
         if (Objects.isNull(emitter)) return;
         try {
             SseEmitter.SseEventBuilder builder = SseEmitter.event();
             builder.id(UUID.randomUUID(true).toString());
             builder.data(message);
             emitter.send(builder);
         } catch (Exception e) {
             /* 客户端没有调用close() 意外强制退出, 会话容器根据凭证调用失败 */
             log.error("- - - - - 发送给客户端：{} 消息失败，异常原因：{}", clientId, e.getMessage());
             log.error("- - - - - 客户端已经下线，开始删除会话连接 - - - - - ");
             emitter.completeWithError(e);
             SESSION_MAP.remove(clientId);
         }
     }

    /**
     *
     * @param message
     */
    public void sendMessageToClient(String message) {
        for (String clientId : SESSION_MAP.keySet()) {
            final SseEmitter emitter = SESSION_MAP.get(clientId);
            if (Objects.isNull(emitter)) continue;

            SseEmitter.SseEventBuilder builder = SseEmitter.event();
            builder.id(UUID.randomUUID(true).toString());
            builder.data(message);
            try {
                emitter.send(builder);
            } catch (Exception e) {
                log.error("- - - - - 发送给客户端：{} 消息失败，异常原因：{}", clientId, e.getMessage());
                log.error("- - - - - 客户端已经下线，开始删除会话连接 - - - - - ");
                emitter.completeWithError(e);
                SESSION_MAP.remove(clientId);
            }
        }
     }
}
