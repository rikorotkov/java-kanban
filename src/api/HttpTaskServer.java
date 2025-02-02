package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import api.router.TaskHandler;
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

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager, new Gson()));


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
