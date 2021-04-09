package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kz.attractor.java.server.BasicServer;
import kz.attractor.java.server.ContentType;
import kz.attractor.java.server.ResponseCodes;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;


import static java.util.stream.Collectors.joining;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    private final ArrayList<User> users = new ArrayList<>();
    public static final String MAIL = "email";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::freemarkerSampleHandler);
        registerGet("/user", this::freemarkerUserHandler);
        registerGet("/books", this::freemarkerBookHandler);
        registerGet("/home", this::freemarkerHomeHandler);
//        registerGet("/cookie", this::cookieHandler);

        registerGet("/login", this::loginGetHolder);
        registerGet("/registration",this::sendFileRegistration);
        registerGet("/profile",this::profileGetHolder);


        registerPost("/registration",this::registrationPostHandler);
        registerPost("/login",this::loginPostHandler);

    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

            cfg.setDirectoryForTemplateLoading(new File("data"));

            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void freemarkerSampleHandler(HttpExchange exchange) {
        renderTemplate(exchange,"index.html", getSampleDataModel());
    }
    public void freemarkerHomeHandler(HttpExchange exchange) {
        renderTemplate(exchange,"mainPage.html", getSampleDataModel());
    }
    private void freemarkerBookHandler(HttpExchange exchange) {
        renderTemplate(exchange,"books.ftl", getBookDataModel());
    }

    private void freemarkerUserHandler(HttpExchange exchange) {
        renderTemplate(exchange,"user.ftl", getUserDataModel());
    }

    private void sendFileRegistration(HttpExchange exchange) {
        this.sendFile(exchange,  makeFilePath("register.ftl"), ContentType.TEXT_HTML);
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile,Object dataModel) {
    try {
        Template temp = freemarker.getTemplate(templateFile);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {

            temp.process(dataModel, writer);
            writer.flush();

            var data = stream.toByteArray();

            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
        }
    } catch (IOException | TemplateException e) {
        e.printStackTrace();
    }
}

    private void loginPostHandler(HttpExchange exchange) {
        String cType = getContentType(exchange);
        String raw = getBody(exchange);

        Map<String, String> parsed =
                Utils.parseUrlEncoded(raw, "&");

        if(isExistUser(parsed.get(MAIL))){
            renderTemplate(exchange, "profile.html", parsed);

//            setCookie(exchange,sessionCookie);
            getCookies(exchange);
            return;}
        redirect303(exchange,"/login");
    }

    private boolean isValidUser(Map<String, String> parsed) {
        Optional<User> user = getUserByLogin(parsed.get(LOGIN));
        return user.map(value -> value.getPassword().equals(parsed.get(PASSWORD))).orElse(false);
    }

    private Optional<User> getUserByLogin(String login) {
        for(User user: users) {
            if(user.getLogin().equals(login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private void registrationPostHandler(HttpExchange exchange) {
        String cType = getContentType(exchange);
        String raw = getBody(exchange);

        Map<String, String> parsed =
                Utils.parseUrlEncoded(raw, "&");
        if(isExistUser(parsed.get(MAIL))){
            renderTemplate(exchange, "error.html", parsed);
            return;
        }
        User user = registrationUser(parsed);
        renderTemplate(exchange, "sucsses.ftl", user);
    }

//    private void cookieHandler(HttpExchange exchange) {
//        Cookie sessionCookie = Cookie.make("userId", "123"); // добавим её в заголовки ответа
//        exchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());
//
//        Map<String, Object> data = new HashMap<>();
//        int times = 42;
//        data.put("times", times);
//        Cookie c1 = Cookie.make("user%Id", "456");
//        setCookie(exchange, c1);
//        Cookie c2 = Cookie.make("user-mail", "example@mail");
//        setCookie(exchange, c2);
//        Cookie c3 = Cookie.make("restricted()<>@,;:\\\"/[]?={}", "()<>@,;:\\\"/[]?={}");
//        setCookie(exchange, c3);
//        String cookieString = getCookies(exchange);
//
//        Map<String, String> cookies = Cookie.parse(cookieString);
//
//        data.put("cookies", cookies);
//        renderTemplate(exchange, "cookie.html", data);
//    }

    protected static String getCookies(HttpExchange exchange){
        return exchange.getRequestHeaders().getOrDefault("Cookie", List.of("")).get(0);
    }

    protected void setCookie(HttpExchange exchange, Cookie cookie) {
        exchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    private void loginGetHolder(HttpExchange exchange) {
        Path path  =  makeFilePath("login.ftl");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }
    private void profileGetHolder(HttpExchange exchange) {
        Path path  =  makeFilePath("profile.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    private String getBody(HttpExchange exchange) {
        InputStream input = exchange.getRequestBody();
        Charset utf8 = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(input, utf8);

        try (BufferedReader reader = new BufferedReader(isr)) {
            return reader.lines().collect(joining(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getContentType(HttpExchange exchange) {
        return exchange.getRequestHeaders()
                .getOrDefault("Content-Type", List.of(""))
                .get(0);
    }

    private SampleDataModel getSampleDataModel() {
        return new SampleDataModel();
    }

    private BookDataModel getBookDataModel(){
        return new BookDataModel();
    }

    private UserDataModel getUserDataModel(){
        return new UserDataModel();
    }

    protected void redirect303(HttpExchange exchange, String path) {
        try {
            exchange.getResponseHeaders().add("Location", path);
            exchange.sendResponseHeaders(303, 0);
            exchange.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isExistUser(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    private User registrationUser(Map<String, String> parsed) {
        User newUser = new User();
        newUser.setEmail(parsed.get(MAIL));
        newUser.setLogin(parsed.get(LOGIN));
        newUser.setPassword(parsed.get(PASSWORD));
        users.add(newUser);
        return newUser;
    }




}
