import api.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager taskManager;
    private Logger logger = Logger.getLogger(HttpTaskServer.class.getName());

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer();
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testAddTask() throws IOException {
        String jsonData = "{ \"taskName\": \"Тест\", \"taskDescription\": \"Описание\", \"taskStatus\": \"NEW\", \"duration\": \"PT1H\", \"startTime\": \"0001-01-01T01:01:00\" }";

        URL url = new URL("http://localhost:8080/tasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write(jsonData.getBytes(StandardCharsets.UTF_8));

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "Ожидаемый статус код 200");

        assertEquals(1, taskManager.getAllTasks().size(), "Задача должна быть добавлена");
        assertEquals("Тест", taskManager.findTaskById(1).getTaskName(), "Имя задачи должно соответствовать");
    }
}
