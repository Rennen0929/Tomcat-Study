package dev.rennen;

import java.net.Socket;

public class Request extends AbstractHttpServletRequest {

    private String method;
    private String url;
    private String protocol;


    public Request(String method, String url, String protocol,Socket socket) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer(url);
    }

    public String getProtocol() {
        return protocol;
    }

}
