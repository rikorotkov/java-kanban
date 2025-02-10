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

        Task task = new Task("Описание задачи 3", "Задача 3");
        task.setDuration(Duration.ofHours(1));
        task.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        taskManager.createTask(task);
        historyManager.add(task);

        Epic epic = new Epic("Новый эпик", "Эпик 1");
        taskManager.createEpic(epic);
        Subtask stask = new Subtask("Описание сабтаски","Сабтаска 1", epic.getId());
        taskManager.createSubtask(stask);
        Epic findEpic = taskManager.findEpicById(2);
        System.out.println("ищу эпик по id " + findEpic);
    }
}
