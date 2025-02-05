import model.Epic;
import model.Subtask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Описание задачи 1", "Задача 1");
        Task task2 = new Task("Описание задачи 2", "Задача 2");
        Task task3 = new Task("Описание задачи 3", "Задача 3");
        task3.setDuration(Duration.ofHours(1));
        task3.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        taskManager.createTask(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        Epic epic = new Epic("Новый эпик", "Эпик 1");
        Subtask stask = new Subtask("Описание сабтаски","Сабтаска 1", epic.getId());
        taskManager.createEpic(epic);
        taskManager.createSubtask(stask);

        logger.info("История: " + historyManager.getHistory());
        historyManager.add(task2);
        logger.info("История после повторного добавления Task 2: " + historyManager.getHistory());

        historyManager.remove(1);
        logger.info("История после удаления Task 1: " + historyManager.getHistory());
        System.out.println(historyManager.getHistory());
    }
}
