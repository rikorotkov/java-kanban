package api.v1.router;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;
import java.util.Map;

public class HistoryHandler extends BaseHttp {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                handleGet(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = gson.toJson(Map.of("error", e.getMessage()));
            sendText(exchange, errorResponse, 400);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getHistory());
        sendText(exchange, response, 200);
    }
}
