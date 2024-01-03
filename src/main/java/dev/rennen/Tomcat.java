package dev.rennen;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tomcat {

    public static void main(String[] args) {
        System.out.println("Tomcat 已启动");
        Tomcat tomcat = new Tomcat();
        tomcat.start();
    }

    private void start() {
        // Socket 连接 TCP

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new SocketProcessor(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
