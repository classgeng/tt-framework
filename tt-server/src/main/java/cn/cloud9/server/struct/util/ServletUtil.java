package cn.cloud9.server.struct.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 上午 11:16
 */
@Slf4j
public class ServletUtil {
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }


    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取request中的参数集合转Map
     * Map<String,String> parameterMap = RequestUtil.getParameterMap(request)
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) map.put(paramName, paramValues[0]);
            else if (paramValues.length > 1) map.put(paramName, StringUtils.join(paramValues, ","));
        }
        return map;
    }
    /**
     * 获取request中的请求体中得json参数
     * @param request
     * @return
     */
    public static String getRequestBodyJson(HttpServletRequest request) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) builder.append(inputStr);
        return builder.toString();
    }

    /**
     * 输出响应结果
     * @param response 响应对象
     * @param resultModel 响应模型
     * @param <ResultModel> 响应模型泛型
     */
    public static <ResultModel> void sendJsonResult(HttpServletResponse response, ResultModel resultModel) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(JSON.toJSONString(resultModel));
            writer.flush();
        } catch (Exception e) {
            log.error("HttpServlet响应对象写入JSON异常 ！", e);
        } finally {
           if (Objects.nonNull(writer)) writer.close();
        }
    }

    /**
     * 获取请求url
     * @return url
     */
    public static String getServerDomain() {
        final HttpServletRequest request = getRequest();
        final String scheme = request.getScheme();
        final String serverName = request.getServerName();
        final int serverPort = request.getServerPort();
        final String contextPath = request.getContextPath();
        return String.format("%1$s://%2$s:%3$s%4$s", scheme, serverName, serverPort, contextPath);
    }

}
