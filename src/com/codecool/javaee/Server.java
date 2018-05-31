package com.codecool.javaee;

import com.sun.net.httpserver.HttpServer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws Exception {
        Class<Handler> handler = Handler.class;
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        for (Method method: handler.getDeclaredMethods()){
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                server.createContext(webRoute.value(), new Handler(webRoute.value()));
            }
        }
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server running at: http://localhost:8000/");
    }
}
