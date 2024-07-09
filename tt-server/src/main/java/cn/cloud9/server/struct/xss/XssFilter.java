package cn.cloud9.server.struct.xss;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 07:24
 */
@Component
public class XssFilter implements Filter {

    @Resource
    private XssProperty xssProperty;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!isExcludeUrl(request)){
            String body = readRequestBody(request);
            body = jsonEscapeConvert(body);
            request = new XssRequestWrapper(request, body);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isExcludeUrl(HttpServletRequest request) {
        if (!xssProperty.isEnabled()) return true;
        else if (CollectionUtils.isEmpty(xssProperty.getExcludes())) return false;
        final String url = request.getServletPath();
        for (String excludeUrl : xssProperty.getExcludes()) {
            final Pattern pattern = Pattern.compile("^" + excludeUrl);
            final Matcher matcher = pattern.matcher(url);
            if (matcher.find()) return true;
        }
        return false;
    }

    private String jsonEscapeConvert(String body) {
        final JSONObject objectJson = JSONObject.parseObject(body);
        final List<String> properties = xssProperty.getProperties();
        escapeJsonRecursive(objectJson, properties);
        return Objects.isNull(objectJson) ? null : objectJson.toJSONString();
    }

    private void escapeJsonRecursive(JSONObject objectJson, List<String> properties) {
        if (Objects.isNull(objectJson)) return;

        for (Map.Entry<String, Object> next : objectJson.entrySet()) {
            final String key = next.getKey();
            final boolean contains = properties.contains(key);
            if (contains) continue;

            final Object value = next.getValue();
            boolean isString = value instanceof String;
            if (isString) {
                final String escapeHtml4 = StringEscapeUtils.escapeHtml4(String.valueOf(value));
                next.setValue(escapeHtml4);
                continue;
            }
            boolean isJsonObj = value instanceof JSONObject;
            if (isJsonObj) {
                escapeJsonRecursive((JSONObject) value, properties);
                continue;
            }
            boolean isJsonArray = value instanceof JSONArray;
            if (isJsonArray) {
                JSONArray jsonArray = (JSONArray) value;
                for (Object o : jsonArray) {
                    isString = o instanceof String;
                    if (isString) {
                        final String escapeHtml4 = StringEscapeUtils.escapeHtml4(String.valueOf(o));
                        next.setValue(escapeHtml4);
                        continue;
                    }
                    isJsonObj = o instanceof JSONObject;
                    if (isJsonObj) escapeJsonRecursive((JSONObject) o, properties);
                }
            }
        }

    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        final ServletInputStream inputStream = request.getInputStream();
        final ByteArrayOutputStream bAoS = new ByteArrayOutputStream();

        byte[] byteCache = new byte[1024 * 4];
        int idx = inputStream.read(byteCache);
        while (idx > 0) {
            bAoS.write(byteCache, 0, idx);
            idx = inputStream.read(byteCache);
        }
        return new String(bAoS.toByteArray(), StandardCharsets.UTF_8);
    }
}
