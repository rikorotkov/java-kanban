import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.InMemoryTaskManager;

import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        InMemoryTaskManager taskManager = new InMemoryTaskManager(logger);

        Task task1 = new Task("Закрыть дверь", "Дверь");
        Task task2 = new Task("Оплатить счет", "Счет");
        Task task3 = new Task("Помыть кота", "Кот");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        // id epic = 4
        Epic epic1 = new Epic("Спринт 4 (14.10 - 27.10)", "Спринт Java");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Закрыть модуль 1", "Модуль 1", 4);
        Subtask subtask2 = new Subtask("Выучить теорию модуля 2", "Модуль 2", 4);
        Subtask subtask3 = new Subtask("Сделать задание по модулю 3", "Модуль 3", 4);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.updateSubtask(5,"Закрыть модуль 1", "Модуль 1", TaskStatus.DONE);
        taskManager.updateSubtask(6,"Выучить теорию модуля 2", "Модуль 2", TaskStatus.DONE);
//        taskManager.updateSubtask(7,"Сделать задание по модулю 3", "Модуль 3", TaskStatus.DONE);

        taskManager.updateEpicStatus(epic1);

        System.out.println(epic1.toString());
        System.out.println(subtask1.toString());
        System.out.println(task1.toString());
    }
}
