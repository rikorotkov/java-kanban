package api.v1.router;

import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.util.Map;

public class SubtaskHandler extends BaseHttp {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int subtaskId = -1;

            if (pathParts.length > 2) {
                String idPart = pathParts[pathParts.length - 1];
                if (idPart.matches("\\d+")) {
                    subtaskId = parsePathId(idPart);
                }
            }

            switch (method) {
                case "GET":
                    if (subtaskId > 0) {
                        Subtask subtask = taskManager.findSubtaskById(subtaskId);
                        if (subtask != null) {
                            sendText(exchange, gson.toJson(subtask), 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
                    }
                    break;
                case "POST":
                    String body = readText(exchange);
                    Subtask newSubtask = gson.fromJson(body, Subtask.class);
                    Subtask createdSubtask = taskManager.createSubtask(newSubtask);
                    sendText(exchange, gson.toJson(createdSubtask), 201);
                    break;
                case "PUT":
                    if (subtaskId > 0) {
                        String updateBody = readText(exchange);
                        Subtask updatedSubtask = gson.fromJson(updateBody, Subtask.class);
                        Subtask result = taskManager.updateSubtask(
                                subtaskId,
                                updatedSubtask.getTaskDescription(),
                                updatedSubtask.getTaskName(),
                                updatedSubtask.getTaskStatus()
                        );
                        sendText(exchange, gson.toJson(result), 200);
                    } else {
                        sendText(exchange, gson.toJson(Map.of("error", "Invalid subtask ID")), 400);
                    }
                    break;
                case "DELETE":
                    if (subtaskId > 0) {
                        taskManager.deleteSubtask(subtaskId);
                        sendText(exchange, "Subtask deleted", 200);
                    } else if (subtaskId == -1) {
                        taskManager.deleteAllSubtasks();
                        sendText(exchange, "All subtasks deleted", 200);
                    } else {
                        sendText(exchange, gson.toJson(Map.of("error", "Invalid subtask ID")), 400);
                    }
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            String errorResponse = gson.toJson(Map.of("error", e.getMessage()));
            sendText(exchange, errorResponse, 500);
        } finally {
            exchange.close();
        }
    }
}
