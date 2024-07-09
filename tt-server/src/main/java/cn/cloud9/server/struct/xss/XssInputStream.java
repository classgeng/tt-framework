package cn.cloud9.server.struct.xss;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 07:00
 */
public class XssInputStream extends ServletInputStream {

    private final byte[] requestBytes;
    private int lastIdxRetrieved = -1;
    private ReadListener listener = null;

    public XssInputStream(byte[] requestBytes) {
        this.requestBytes = requestBytes;
    }

    @Override
    public boolean isFinished() {
        return lastIdxRetrieved == requestBytes.length - 1;
    }

    @Override
    public boolean isReady() {
        return isFinished();
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        this.listener = readListener;
        try {
            if (isFinished()) readListener.onAllDataRead();
            else readListener.onDataAvailable();
        } catch (IOException e) {
            readListener.onError(e);
        }
    }

    @Override
    public int read() throws IOException {
         if (isFinished() && Objects.nonNull(listener)) {
             try {
                 listener.onAllDataRead();
             } catch (IOException e) {
                 listener.onError(e);
                 throw e;
             }
         }
        if (isFinished()) return -1;
        int iterator = requestBytes[lastIdxRetrieved + 1];
        lastIdxRetrieved ++;
        return iterator;
    }
}
