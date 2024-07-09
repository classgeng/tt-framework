package cn.cloud9.server.struct.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.cloud9.server.struct.websocket.WsMessageEndPoint.PATH;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月09日 下午 09:56
 */
@Slf4j
@Service
@ServerEndpoint(PATH)
public class WsMessageEndPoint {
    /**
     * localhost:8080/websocket/message
     * 路径参数用于客户端身份标识，区分每个客户端的连接
     */
    public static final String PATH = "/websocket/message/{token}";

    /**
     * 客户端会话管理容器
     */
    private static final Map<String, Session> CLIENT_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 侦测客户端开启连接？，通过客户端身份标识 重复创建连接则覆盖原有的连接
     * @param session
     */
    @OnOpen
    public void onConnectionOpen(Session session, @PathParam("token") String clientToken) {
        log.info("客户端 {} 开启了连接", clientToken);
        final Map<String, String> map = JSONObject.parseObject(clientToken, Map.class);
        // 默认按照ID保管存放这个客户端的会话信息
        CLIENT_SESSION_MAP.put(map.get("userId"), session);
    }

    /**
     * 侦测客户端关闭事件
     * @param session
     */
    @OnClose
    public void onClose(Session session,  @PathParam("token") String clientToken) {
        final Map<String, String> map = JSONObject.parseObject(clientToken, Map.class);
        log.info("客户端 {} 关闭了连接...", clientToken);
        // 客户端关闭时，从保管容器中踢出会话
        final String userId = map.get("userId");
        CLIENT_SESSION_MAP.remove(userId);
    }

    /**
     * 侦测客户端异常
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("客户端 {} 连接异常... 异常信息：{}", session.getId(), error.getMessage());
    }



    /**
     * 收到客户端消息
     * @param session
     */
    @OnMessage
    public void receiveClientMessage(String message, Session session) throws IOException {
        log.info("来自客户端 {} 的消息: {}", session.getId(), message);
        session.getBasicRemote().sendText("服务器已收到");
    }

    /**
     * 给所有客户端发送消息
     * @param message
     */
    public static void sendMessageForAllClient(String message) {
        CLIENT_SESSION_MAP.values().forEach(session -> {
            try {
                log.info("给客户端 {} 发送消息 消息内容: {}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("给客户端 {} 发送消息失败， 异常信息：{}", session.getId(), e.getMessage());
            }
        });
    }

    @SneakyThrows
    public static void sendMessageForClient(String clientId, String message) {
        final Session session = CLIENT_SESSION_MAP.get(clientId);
        session.getBasicRemote().sendText(message);
    }
}
