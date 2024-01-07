package dev.rennen;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response extends AbstractHttpServletResponse{

    private int status = 200;
    private String message = "OK";
    private Map<String, String> headers = new HashMap<>();

    @Override
    public void setStatus(int i, String s) {
        status = i;
        message = s;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void addHeader(String s, String s1) {
        headers.put(s, s1);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ResponseServletOutputStream();
    }

    public void complete() {
        // 发送响应
        
    }
}
