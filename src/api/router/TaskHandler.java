package api.router;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;
import java.util.Map;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = gson.toJson(taskManager.getAllTasks());
                sendText(exchange, response, 200);
                System.out.println(response);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = gson.toJson(Map.of("error", e.getMessage()));
            exchange.sendResponseHeaders(500, errorResponse.length());
            sendText(exchange, errorResponse, 404);
        }
    }
}
