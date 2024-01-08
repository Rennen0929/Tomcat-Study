package dev.rennen;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response extends AbstractHttpServletResponse{

    private int status = 200;
    private String message = "OK";
    private byte SP = ' ';
    private byte CR = '\r';
    private byte LF = '\n';
    private Map<String, String> headers = new HashMap<>();
    private Request request;
    private OutputStream socketOutputStream;
    public Response(Request request) {
        this.request = request;

    }

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
        try {
            sendResponseLine();
            sendResponseHeader();
            sendResponseBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendResponseBody() {
    }

    private void sendResponseHeader(){
        }


    private void sendResponseLine() throws IOException {
        socketOutputStream.write(request.getProtocol().getBytes());
        socketOutputStream.write(SP);
        socketOutputStream.write(status);
        socketOutputStream.write(SP);
        socketOutputStream.write(message.getBytes());
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }
}
