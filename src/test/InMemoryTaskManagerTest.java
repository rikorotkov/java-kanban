package test;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Описание задачи", "Имя задачи");
        taskManager.createTask(task);

        assertEquals(task, taskManager.findTaskById(task.getId()), "Задача не найдена");
    }

    @Test
    public void testAddEpic() {
        Epic epic = new Epic("Описание эпика", "Название эпика");
        taskManager.createEpic(epic);

        assertEquals(epic, taskManager.findEpicById(epic.getId()), "Эпик не найден");
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic("Описание эпика", "Название эпика");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Описание подзадачи", "Имя подзадачи", epic.getId());
        taskManager.createSubtask(subtask);

        assertEquals(subtask, taskManager.findSubtaskById(subtask.getId()), "Подзадача не найдена");
    }
    
}

