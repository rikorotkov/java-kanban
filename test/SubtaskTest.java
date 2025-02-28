package test;

import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubtaskTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testSubtaskEqualsById() {
        Epic epic = new Epic("Описание эпика", "Имя эпика");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Описание подзадачи", "Имя подзадачи", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask findSubtask = taskManager.findSubtaskById(subtask.getId());

        assertNotNull(findSubtask);
        assertEquals(subtask.getId(), findSubtask.getId());
        assertEquals(subtask.getId(), findSubtask.getId(), "Подзадачи не равны");
        assertEquals(subtask, findSubtask, "Объекты подзадач не равны");
    }

}