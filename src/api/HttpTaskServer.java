package api;

import api.router.TaskHandler;
import api.util.GsonUtil;
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
    private final int PORT = 8080;
    private final HttpServer server;
    private final Logger logger;
    private final TaskManager taskManager;
    private final HistoryManager historyManager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.logger = Logger.getLogger(HttpTaskServer.class.getName());
        this.taskManager = Managers.getDefault();
        this.historyManager = Managers.getDefaultHistory();
        this.gson = GsonUtil.getGson();

        server.createContext("/tasks", new TaskHandler(taskManager, gson));

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
        System.out.println("CREATE TASK - "+task);

        System.out.println("ALL TASKS - " + taskManager.getAllTasks());

        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}
