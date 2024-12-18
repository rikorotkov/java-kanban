package test;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void addNewTask() {
        Task task = new Task("Описание задачи", "Имя задачи");
        taskManager.createTask(task);

        final Task savedTask = taskManager.findTaskById(task.getId());
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    public void testTaskEqualsById() {
        Task task = new Task("Описание задачи 1", "Задача 1");
        taskManager.createTask(task);
        Task findTask = taskManager.findTaskById(task.getId());
        assertEquals(task.getId(), findTask.getId(), "Задачи не равны");
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Описание задачи", "Имя задачи");
        taskManager.createTask(task);

        Task updateTask = taskManager.updateTask(task.getId(), "Новое описание задачи", "Новое имя задачи", TaskStatus.IN_PROGRESS);
        assertEquals(taskManager.findTaskById(task.getId()), updateTask);
    }

}
