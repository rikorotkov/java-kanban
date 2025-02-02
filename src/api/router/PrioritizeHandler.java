package api.router;

import api.helper.GsonUtil;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

public class PrioritizeHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizeHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = GsonUtil.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String response = "";
        int statusCode = HttpURLConnection.HTTP_OK;

        try {
            switch (requestMethod) {
                case "GET":
                    List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                    response = gson.toJson(prioritizedTasks);
                    break;
                default:
                    response = "{\"error\": \"Метод не доступен\"}";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
            response = "{\"error\": \"Ошибка сервера\"}";
        }

        sendResponse(exchange, response, statusCode);
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
