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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::freemarkerSampleHandler);
        registerGet("/user", this::freemarkerUserHandler);
        registerGet("/books", this::freemarkerBookHandler);
        registerGet("/home", this::freemarkerHomeHandler);
        registerGet("/login", this::loginGetHolder);
        registerPost("/login", this::loginPostHolder);
        registerGet("/register",this::freemarkerRegisterGet);
        registerPost("/register",this::freemarkerRegisterGet);

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

    private void freemarkerRegisterGet(HttpExchange exchange) {
        renderTemplate(exchange,"register.ftl", getUserDataModel());
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

    private void loginPostHolder(HttpExchange exchange) {
        String cType = getContentType(exchange);
        String raw = getBody(exchange);


        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");

        if( parsed.containsKey("mail") && parsed.get("mail").equals("ttt@ttt.ttt")) {
                redirect303(exchange,"/register/sucsses");
        }

        String fmt = "<p>Необработанные данные: <b>%s</b></p>"
                + "<p>Content-type: <b>%s</b></p>"
                + "<p>После обработки: <b>%s</b></p>";
        String data = String.format(fmt, raw, cType, parsed);
        try {
            sendByteData(exchange, ResponseCodes.OK,
                    ContentType.TEXT_PLAIN, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void loginGetHolder(HttpExchange exchange) {
        Path path  =  makeFilePath("login.ftl");
        sendFile(exchange, path, ContentType.TEXT_HTML);
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

    private void loginPost(HttpExchange exchange) {
        redirect303(exchange, "/");
    }


}
