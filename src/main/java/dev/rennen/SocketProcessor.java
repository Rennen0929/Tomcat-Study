package dev.rennen;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketProcessor implements Runnable{

    private Socket socket;

    public SocketProcessor(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        processSocket(socket);
    }

    private void processSocket(Socket socket) {
        // 处理 Socket 连接
        // 读取数据和写入数据

        InputStream inputStream = null;
        try {

            // 获取字节流
            inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            // 解析字节流，遇到一个空格就退出
            int pos = 0;
            int begin = 0, end = 0;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == ' ') break;
            }
            // 组合空格之间的字节流，转换成字符串（也就是请求方法）
            String method = new String(bytes, begin, end - begin);
            System.out.println("---获取到请求---");
            System.out.println("请求方法解析结果：" + method);

            // 解析 URL
            pos++;
            begin = pos;
            end++;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == ' ') break;
            }
            String url = new String(bytes, begin, end - begin);
            System.out.println("URL 解析结果：" + url);

            // 解析协议版本
            pos++;
            begin = pos;
            end++;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == '\r') break;
            }
            String protocol = new String(bytes, begin, end - begin);
            System.out.println("协议版本解析结果：" + protocol);

            // 封装请求和响应对象
            Request request = new Request(method, url, protocol);
            Response response = new Response();

            // 匹配 Servlet、doGet、doPost
            MyServlet myServlet = new MyServlet();
            myServlet.service(request, response);

            // 发送响应
            response.complete();



        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
