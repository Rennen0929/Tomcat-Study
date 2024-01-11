package dev.rennen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        urlPatterns = {"/WuhanWeather"}
)
public class WeatherServlet extends HttpServlet {
    public WeatherServlet() {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应内容类型
        resp.setContentType("text/html;charset=UTF-8");

        // 获取当前时间
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedTime = sdf.format(currentTime);

        // 获取武汉天气信息
        String weatherInfo = getWuhanWeather();

        // 构建HTML响应内容
        String htmlResponse = "<html><head><title>Wuhan Weather</title></head><body>"
                + "<h1>武汉天气（更新时间：" + formattedTime + "）：</h1><p>" + weatherInfo + "</p></body></html>";

        // 将HTML响应内容写入输出流
        OutputStream outputStream = resp.getOutputStream();
        outputStream.write(htmlResponse.getBytes("UTF-8"));
    }

    private String getWuhanWeather() throws IOException {
        // 武汉天气API示例（请替换为实际的API地址）
        String apiUrl = "https://restapi.amap.com/v3/weather/weatherInfo?key=de48905ac3ad6bde1a4cc2e271de3afb&city=420100";

        // 发送HTTP请求获取天气信息
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // 读取API响应
        Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8");
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        // 这里假设API返回的是JSON格式的天气信息，你需要根据实际情况解析JSON
        // 这里只是一个示例，实际应用中你可能需要使用JSON解析库（如Jackson、Gson等）来处理JSON数据
        String weatherInfo = parseWeatherInfo(response.toString());

        return weatherInfo;
    }

    private String parseWeatherInfo(String json) throws JsonProcessingException {
        // 使用 Jackson ObjectMapper 解析 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // 获取 lives 数组
        JsonNode livesNode = rootNode.path("lives");
        if (livesNode.isArray() && !livesNode.isEmpty()) {
            // 获取第一个元素
            JsonNode firstLiveNode = livesNode.get(0);

            // 获取相关字段
            String province = firstLiveNode.path("province").asText();
            String city = firstLiveNode.path("city").asText();
            String weather = firstLiveNode.path("weather").asText();
            String temperature = firstLiveNode.path("temperature").asText();
            String windDirection = firstLiveNode.path("winddirection").asText();
            String windPower = firstLiveNode.path("windpower").asText();
            String humidity = firstLiveNode.path("humidity").asText();
            String reportTime = firstLiveNode.path("reporttime").asText();

            // 构建解析后的字符串
            return String.format("省份：%s，城市：%s，天气：%s，温度：%s°C，风向：%s，风力：%s，湿度：%s%%，报告时间：%s",
                    province, city, weather, temperature, windDirection, windPower, humidity, reportTime);
        }


        return "无法解析天气信息";
    }
}
