package api.v1.router;

import api.helper.GsonUtil;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttp implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson = GsonUtil.getGson();

    public BaseHttp(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange httpExchange, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(statusCode, resp.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(resp);
        }
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        String text = "Not Found";
        sendText(httpExchange, text, 404);
    }

    protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        String text = "Not Acceptable";
        sendText(httpExchange, text, 406);
    }

    protected int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

}