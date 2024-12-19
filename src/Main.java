import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Описание задачи 1", "Задача 1");
        Task task2 = new Task("Описание задачи 2", "Задача 2");
        Task task3 = new Task("Описание задачи 3", "Задача 3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        System.out.println("История: " + historyManager.getHistory());
        historyManager.add(task2);
        System.out.println("История после повторного добавления Task 2: " + historyManager.getHistory());

        historyManager.remove(1);
        System.out.println("История после удаления Task 1: " + historyManager.getHistory());

    }
}