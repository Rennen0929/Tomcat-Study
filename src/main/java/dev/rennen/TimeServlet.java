package dev.rennen;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(
    urlPatterns = {"/CurrentTime"}
)
public class TimeServlet extends HttpServlet {
    public TimeServlet() {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 获取当前时间
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedTime = sdf.format(currentTime);

        // 构建HTML响应内容
        String htmlResponse = "<html><head><title>Current Time</title></head><body>"
                + "<h1>当前时间：</h1><p>" + formattedTime + "</p></body></html>";

        // 将HTML响应内容写入输出流
        OutputStream outputStream = resp.getOutputStream();
        outputStream.write(htmlResponse.getBytes(StandardCharsets.UTF_8));
    }
}