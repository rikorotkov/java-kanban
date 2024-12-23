import model.Task;
import service.HistoryManager;
import service.Managers;

import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Описание задачи 1", "Задача 1");
        Task task2 = new Task("Описание задачи 2", "Задача 2");
        Task task3 = new Task("Описание задачи 3", "Задача 3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        logger.info("История: " + historyManager.getHistory());
        historyManager.add(task2);
        logger.info("История после повторного добавления Task 2: " + historyManager.getHistory());

        historyManager.remove(1);
        logger.info("История после удаления Task 1: " + historyManager.getHistory());
    }
}
