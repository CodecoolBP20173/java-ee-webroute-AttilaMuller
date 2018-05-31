package com.codecool.javaee;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;

import static javax.imageio.ImageIO.read;


public class Handler implements HttpHandler {
    private String route;

    public Handler(){}
    public Handler(String route) {
        this.route = route;
    }

    @WebRoute("/test")
    public void testRoute(HttpExchange requestData) throws IOException {
        InputStream inputStream = requestData.getRequestBody();
        read(inputStream); // .. read the request body
        String response = "This is the response for the test route";
        requestData.sendResponseHeaders(200, response.length());
        OutputStream os = requestData.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute("/another-test")
    public void anotherTestRoute(HttpExchange requestData) throws IOException {
        InputStream inputStream = requestData.getRequestBody();
        read(inputStream); // .. read the request body
        String response = "This is the response for test route 2";
        requestData.sendResponseHeaders(200, response.length());
        OutputStream os = requestData.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @Override
    public void handle(HttpExchange requestData) throws IOException {
        Class<Handler> handler = Handler.class;
        for (Method method: handler.getDeclaredMethods()){
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                if (webRoute.value().equals(route)){
                    try {
                        method.invoke(handler.newInstance(), requestData);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
