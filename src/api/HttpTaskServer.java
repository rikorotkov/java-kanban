package api;

import api.router.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Logger logger = Logger.getLogger(HttpTaskServer.class.getName());
    private final HttpServer server;
    private final TaskManager taskManager;
    private final HistoryManager historyManager;

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        this.historyManager = Managers.getDefaultHistory();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager, new Gson()));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, new Gson()));
        server.createContext("/epics", new EpicHandler(taskManager, new Gson()));
        server.createContext("/history", new HistoryHandler(historyManager, new Gson()));
        server.createContext("/prioritized", new PrioritizeHandler(taskManager, new Gson()));

        logger.info("Сервер запущен на порту " + PORT);
    }

    public void start() {
        server.start();
        logger.info("Сервер успешно запущен.");
    }

    public void stop() {
        server.stop(0);
        logger.info("Сервер остановлен.");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}
