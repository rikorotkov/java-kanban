import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.EpicManagerImpl;
import service.SubtaskManagerImpl;
import service.TaskManagerImpl;

public class Main {

    public static void main(String[] args) {
        TaskManagerImpl taskManager = new TaskManagerImpl();
        EpicManagerImpl epicManager = new EpicManagerImpl();
        SubtaskManagerImpl subtaskManager = new SubtaskManagerImpl(epicManager);

        Task task1 = new Task("Закрыть дверь", "Дверь");
        Task task2 = new Task("Оплатить счет", "Счет");
        Task task3 = new Task("Помыть кота", "Кот");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        // id epic = 4
        Epic epic1 = new Epic("Спринт 4 (14.10 - 27.10)", "Спринт Java");
        epicManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Закрыть модуль 1", "Модуль 1", 4);
        Subtask subtask2 = new Subtask("Выучить теорию модуля 2", "Модуль 2", 4);
        Subtask subtask3 = new Subtask("Сделать задание по модулю 3", "Модуль 3", 4);

        subtaskManager.createSubtask(subtask1);
        subtaskManager.createSubtask(subtask2);
        subtaskManager.createSubtask(subtask3);

        subtaskManager.updateSubtask(5,"Закрыть модуль 1", "Модуль 1", TaskStatus.DONE);
        subtaskManager.updateSubtask(6,"Выучить теорию модуля 2", "Модуль 2", TaskStatus.DONE);
//        subtaskManager.updateSubtask(7,"Сделать задание по модулю 3", "Модуль 3", TaskStatus.DONE);

        epicManager.updateEpicStatus(epic1);

        System.out.println(epic1.toString());
        System.out.println(subtask1.toString());
        System.out.println(task1.toString());
    }
}
