package api.v1.router;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.Map;

public class TaskHandler extends BaseHttp {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
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
                    exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = gson.toJson(Map.of("error", e.getMessage()));
            sendText(exchange, errorResponse, 400);
        } finally {
            exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAllTasks());
        sendText(exchange, response, 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        Task task = gson.fromJson(requestBody, Task.class);
        Task createdTask = taskManager.createTask(task);
        String response = gson.toJson(createdTask);
        sendText(exchange, response, 201);
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        Task task = gson.fromJson(requestBody, Task.class);
        Task updatedTask = taskManager.updateTask(task.getId(), task.getTaskDescription(), task.getTaskName(), task.getTaskStatus());
        String response = gson.toJson(updatedTask);
        sendText(exchange, response, 200);
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length > 2) {
            int taskId = parsePathId(parts[2]);
            taskManager.deleteTask(taskId);
            sendText(exchange, "Task deleted", 200);
        } else {
            taskManager.deleteAllTasks();
            sendText(exchange, "All tasks deleted", 200);
        }
    }
}