package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.server.ContentType;

import java.io.IOException;
import java.nio.file.Path;

public class Lesson45Server extends Lesson44Server{

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/",this::freemarkerSampleHandler);
        registerGet("/login", this::loginGetHolder);
        registerPost("/login",this::loginPostHolder);
    }

    private void loginPostHolder(HttpExchange exchange) {
        int jj = 0;
        System.out.println("sdfdasf");
    }

    private void loginGetHolder(HttpExchange exchange) {
        Path path  =  makeFilePath("login.ftl");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }
}
