package dev.rennen;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tomcat {
    private Map<String,Context> contextMap = new HashMap<>();
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        System.out.println("Tomcat 已启动");
        Tomcat tomcat = new Tomcat();
        tomcat.deployApps();
        tomcat.start();
    }

    private void deployApps() throws MalformedURLException, ClassNotFoundException {
        File webapps = new File(System.getProperty("user.dir"), "webapps");
        for (String app : webapps.list()) {
            deployApp(webapps,app);
        }
    }

    private void deployApp(File webapps,String appName) throws ClassNotFoundException, MalformedURLException {
        //确定有哪些Servlet

        Context context = new Context(appName);
        File appDirectory = new File(webapps,appName);
        File classesDirectory = new File(appDirectory, "classes");
        List<File> files = getAllFilePath(classesDirectory);
        for (File clazz : files) {
            //loadClass
            String name = clazz.getPath();
            name = name.replace(classesDirectory.getPath() + "\\","");
            name = name.replace(".class","");
            name = name.replace("\\",".");
//            System.out.printf(name);
            try {
                WebappClassLoader classLoader = new WebappClassLoader(new URL[]{classesDirectory.toURL()});
                Class<?> servletClass = classLoader.loadClass(name);
//            System.out.println(servletClass);
                if(HttpServlet.class.isAssignableFrom(servletClass)) {
//                    System.out.println(servletClass);
                    if(servletClass.isAssignableFrom(WebServlet.class)) {
                        WebServlet annotation = servletClass.getAnnotation(WebServlet.class);
                        String[] urlPatterns = annotation.urlPatterns();
                        for (String urlPattern : urlPatterns) {
                            context.addUrlPatternMapping(urlPattern, (Servlet) servletClass.newInstance());
                        }
                    }
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

//            servletClass.isAssignableFrom()
        }

        contextMap.put(appName,context);
    }
    public List<File> getAllFilePath(File srcFile) {
        List<File> result = new ArrayList<>();
        File[] files = srcFile.listFiles();

        if(files != null) {
            for(File file : files) {
                if(file.isDirectory()) {
                    result.addAll(getAllFilePath(file));
                } else {
                    result.add(file);
                }
            }
        }
        return result;
    }

    public Map<String,Context> getContextMap() {
        return contextMap;
    }
    private void start() {
        // Socket 连接 TCP

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new SocketProcessor(socket,this));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
