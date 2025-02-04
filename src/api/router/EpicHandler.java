package api.router;

import api.helper.GsonUtil;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

public class EpicHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = GsonUtil.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String response = "";
        int statusCode = HttpURLConnection.HTTP_OK;

        try {
            switch (requestMethod) {
                case "GET":
                    if (requestURI.getPath().equals("/epics")) {
                        List<Epic> epics = taskManager.getAllEpics();
                        response = gson.toJson(epics);
                    } else if (requestURI.getPath().startsWith("/epics/")) {
                        String[] pathParts = requestURI.getPath().split("/");
                        int epicId = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.findEpicById(epicId);
                        if (epic != null) {
                            response = gson.toJson(epic);
                        } else {
                            statusCode = HttpURLConnection.HTTP_NOT_FOUND;
                            response = "{\"error\": \"Эпик не найден\"}";
                        }
                    }
                    break;
                case "POST":
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    taskManager.createEpic(epic);
                    response = gson.toJson(epic);
                    statusCode = HttpURLConnection.HTTP_CREATED;
                    break;
                case "PUT":
                    String[] pathPartsPut = requestURI.getPath().split("/");
                    int epicIdPut = Integer.parseInt(pathPartsPut[2]);
                    String requestBodyPut = new String(exchange.getRequestBody().readAllBytes());
                    Epic epicPut = gson.fromJson(requestBodyPut, Epic.class);
                    taskManager.updateEpic(epicIdPut, epicPut.getTaskDescription(), epicPut.getTaskName());
                    response = gson.toJson(epicPut);
                    break;
                case "DELETE":
                    String[] pathPartsDelete = requestURI.getPath().split("/");
                    int epicIdDelete = Integer.parseInt(pathPartsDelete[2]);
                    taskManager.deleteEpic(epicIdDelete);
                    response = "{\"message\": \"Эпик успешно удален\"}";
                    break;
                default:
                    statusCode = HttpURLConnection.HTTP_BAD_METHOD;
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
