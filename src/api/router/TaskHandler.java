package api.router;

import api.helper.GsonUtil;
import api.request.TaskUpdateRequest;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = GsonUtil.getGson();
        if (taskManager.getAllTasks().size() > 0) {
            Task lastTask = taskManager.getAllTasks().get(taskManager.getAllTasks().size() - 1);
            Task.setLastId(lastTask.getId());
        }
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
        String response = gson.toJson(taskManager.getAllTasks());

        System.out.println("Serialized JSON: " + response);

        exchange.getResponseHeaders().set("Content-Type", "application/json");

        sendText(exchange, response);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);

            if (task == null) {
                sendError(exchange, "Ошибка: Невозможно распознать задачу");
                return;
            }

            taskManager.createTask(task);
            sendText(exchange, "Задача добавлена успешно");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(exchange, "Ошибка обработки данных: " + e.getMessage());
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            try (InputStream is = exchange.getRequestBody()) {
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                System.out.println("Реквест тело: " + body);

                TaskUpdateRequest request = gson.fromJson(body, TaskUpdateRequest.class);

                if (request == null) {
                    sendError(exchange, "Ошибка: Невозможно распознать задачу");
                    return;
                }

                taskManager.updateTask(id, request.getTaskDescription(), request.getTaskName(), request.getStatus());
                sendText(exchange, "Задача обновлена успешно");
            } catch (Exception e) {
                sendError(exchange, "Ошибка обработки данных: " + e.getMessage());
            }
        } else {
            sendError(exchange, "ID задачи не указан");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            taskManager.deleteTask(id);
            sendText(exchange, "Задача удалена успешно");
        } else {
            sendError(exchange, "ID задачи не указан");
        }
    }
}