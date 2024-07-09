package cn.cloud9.server.struct.xss;


import com.google.common.net.HttpHeaders;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 06:45
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    private String requestBody = null;

    private static final String[] SUPPORT_TYPES = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_JSON_UTF8_VALUE
    };

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public XssRequestWrapper(HttpServletRequest request, String requestBody) {
        super(request);
        this.requestBody = requestBody;
    }

    /**
     * 过滤URL上的非法值
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        final String[] values = super.getParameterValues(name);
        if (Objects.isNull(values)) return null;
        String[] escapedValues = new String[values.length];
        for (int i = 0; i < values.length; i++)
            escapedValues[i] = StringEscapeUtils.escapeHtml4(values[i]);
        return escapedValues;
    }

    @Override
    public String getParameter(String name) {
        final String parameter = super.getParameter(name);
        if (StringUtils.isBlank(parameter)) return null;
        return StringEscapeUtils.escapeHtml4(parameter);
    }

    @Override
    public String getHeader(String name) {
        final String header = super.getHeader(name);
        if (StringUtils.isBlank(header)) return null;
        return StringEscapeUtils.escapeHtml4(header);
    }

    @Override
    public ServletInputStream getInputStream() {
        if (StringUtils.isBlank(requestBody)) return null;

        final byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
        return new ServletInputStream() {
            private int lastIdxRetrieved = -1;

            @Override
            public boolean isFinished() {
                return lastIdxRetrieved == requestBodyBytes.length - 1;
            }

            @Override
            public boolean isReady() {
                return isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                try {
                    if (isFinished()) readListener.onAllDataRead();
                    else readListener.onDataAvailable();
                } catch (Exception e) {
                    e.printStackTrace();
                    readListener.onError(e);
                }
            }

            @Override
            public int read() {
                if (isFinished()) return -1;
                else {
                    int temp = requestBodyBytes[lastIdxRetrieved + 1];
                    ++ lastIdxRetrieved;
                    return temp;
                }
            }
        };
    }


}
