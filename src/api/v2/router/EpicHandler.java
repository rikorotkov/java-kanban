package api.v2.router;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
import model.Epic;
import java.io.IOException;
import java.util.Map;

public class EpicHandler extends BaseHttp {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        try {
            if (pathParts.length > 1 && "epics".equals(pathParts[1])) {
                handleEpicRequest(exchange, method, pathParts);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = gson.toJson(Map.of("error", e.getMessage()));
            sendText(exchange, errorResponse, 500);
        }
    }

    private void handleEpicRequest(HttpExchange exchange, String method, String[] pathParts) throws IOException {
        switch (method) {
            case "GET":
                if (pathParts.length == 3) {
                    int id = parsePathId(pathParts[2]);
                    sendText(exchange, gson.toJson(taskManager.findEpicById(id)), 200);
                } else {
                    sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
                }
                break;
            case "POST":
                Epic epic = gson.fromJson(readText(exchange), Epic.class);
                sendText(exchange, gson.toJson(taskManager.createEpic(epic)), 201);
                break;
            case "PUT":
                int updateId = parsePathId(pathParts[2]);
                Epic updatedEpic = gson.fromJson(readText(exchange), Epic.class);
                sendText(exchange, gson.toJson(taskManager.updateEpic(updateId, updatedEpic.getTaskDescription(), updatedEpic.getTaskName())), 200);
                break;
            case "DELETE":
                int deleteId = parsePathId(pathParts[2]);
                taskManager.deleteEpic(deleteId);
                sendText(exchange, "Deleted", 200);
                break;
            default:
                exchange.sendResponseHeaders(405, -1);
        }
    }
}
