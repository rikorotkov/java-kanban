package api.router;

import api.helper.GsonUtil;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = Managers.getDefault();
        this.gson = GsonUtil.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "PUT":
                    handlePut(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.contains("/subtasks/")) {
            int subtaskId = Integer.parseInt(path.split("/")[2]);
            Subtask subtask = taskManager.findSubtaskById(subtaskId);
            if (subtask != null) {
                sendResponse(exchange, gson.toJson(subtask), 200);
            } else {
                sendError(exchange, "Подзадача не найдена");
            }
        } else if (path.contains("/epics/")) {
            String[] pathParts = path.split("/");
            int epicId = Integer.parseInt(pathParts[2]);
            List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);
            sendResponse(exchange, gson.toJson(subtasks), 200);
        } else {
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            sendResponse(exchange, gson.toJson(subtasks), 200);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        String bodyString = new String(body.readAllBytes(), StandardCharsets.UTF_8);

        // Логирование для отладки
        System.out.println("Полученные данные: " + bodyString);

        Subtask subtask = gson.fromJson(bodyString, Subtask.class);
        System.out.println("Создана подзадача с epicId: " + subtask.getEpicId());  // Логирование epicId

        Subtask createdSubtask = taskManager.createSubtask(subtask);
        if (createdSubtask != null) {
            sendResponse(exchange, gson.toJson(createdSubtask), 201);
        } else {
            sendError(exchange, "Эпик не найден");
        }
    }



    private void handlePut(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        String bodyString = new String(body.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(bodyString, Subtask.class);

        int subtaskId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        Subtask updatedSubtask = taskManager.updateSubtask(subtaskId, subtask.getTaskDescription(), subtask.getTaskName(), subtask.getTaskStatus());
        sendResponse(exchange, gson.toJson(updatedSubtask), 200);
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.contains("/subtasks/")) {
            int subtaskId = Integer.parseInt(path.split("/")[2]);
            taskManager.deleteSubtask(subtaskId);
            sendResponse(exchange, "Подзадача удалена", 200);
        } else {
            taskManager.deleteAllSubtasks();
            sendResponse(exchange, "Все подзадачи удалены", 200);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }

}
