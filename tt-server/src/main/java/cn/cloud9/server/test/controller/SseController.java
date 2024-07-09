package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.response.NoApiResult;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.sse.ServerSentEventService;
import cn.cloud9.server.struct.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author OnCloud9
 * @description SSE单向消息推送测试类
 * @project tt-server
 * @date 2022年11月10日 下午 10:46
 */
@RestController
@RequestMapping("/test/sse")
public class SseController {

    @Resource
    private ServerSentEventService sseService;

    /**
     * 发布的接口地址
     * @param clientId
     * @return
     */
    @NoApiResult
    @RequestMapping(value = "/release", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter releasePoint(@RequestParam(name = "clientId", required = true) String clientId) {
        Assert.isTrue(StringUtils.isBlank(clientId), ResultMessage.NULL_ERROR, "clientId");
        return sseService.connectionEstablish(clientId);
    }

    /**
     * 向连接发布接口的客户端发送消息的测试接口
     * @param map
     */
    @PostMapping("/send")
    public void messageSend(@RequestBody Map<String, String> map) {
        final String clientId = map.get("clientId");
        final String txt = map.get("txt");
        Assert.isTrue(StringUtils.isBlank(txt), ResultMessage.NULL_ERROR, "txt");

        boolean isForAll = StringUtils.isBlank(clientId);
        if (isForAll) sseService.sendMessageToClient(txt);
        else sseService.sendMessageToClient(clientId, txt);
    }
}
