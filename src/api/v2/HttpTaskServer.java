package api.v2;

import api.v2.router.*;
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
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));

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
        TaskManager taskManager = Managers.InMemory();
        taskManager.createTask(task);
        System.out.println("CREATE TASK - " + task);

        System.out.println("ALL TASKS - " + taskManager.getAllTasks());
        Task taskf = taskManager.findTaskById(task.getId());

        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }

}