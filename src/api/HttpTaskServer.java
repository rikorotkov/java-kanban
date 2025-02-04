package api;

import api.router.BaseHttpHandler;
import api.router.TaskHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final HttpServer server;
    private final Logger logger;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.logger = Logger.getLogger(HttpTaskServer.class.getName());

        server.createContext("/tasks", new TaskHandler(taskManager));

        logger.info("Сервер запущен на порту: " + PORT);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public static void main(String[] args) throws IOException {
        Task task = new Task("Описание", "Имя");
        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(task);
        System.out.println("CREATE TASK - " + task);

        System.out.println("ALL TASKS - " + taskManager.getAllTasks());

        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }

}
