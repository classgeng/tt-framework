package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.websocket.WsMessageEndPoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author OnCloud9
 * @description WebSocket测试类
 * @project tt-server
 * @date 2022年11月09日 下午 10:03
 */
//@RestController
@RequestMapping("/websocket/sender")
public class WsMessageController {

    /**
     * 向所有Socket客户端发送消息
     * @param map
     */
    @PostMapping("/all")
    public void sendMessageToAllClient(@RequestBody Map<String, String> map) {
        final String text = map.get("text");
        WsMessageEndPoint.sendMessageForAllClient(text);
    }

    /**
     * 向指定客户端发送消息
     * @param map
     */
    @PostMapping("/one")
    public void sendMessageToClient(@RequestBody Map<String, String> map) {
        final String text = map.get("text");
        final String client = map.get("client");
        WsMessageEndPoint.sendMessageForClient(client, text);
    }

}
