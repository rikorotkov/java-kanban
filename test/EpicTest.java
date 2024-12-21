package test;

import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testEpicEqualsById() {
        Epic epic = new Epic("Описание эпика", "Имя эпика");
        taskManager.createEpic(epic);
        Epic findEpic = taskManager.findEpicById(epic.getId());
        assertEquals(epic.getId(), findEpic.getId(), "Эпики не равны");
    }

    @Test
    public void removeEpicShouldRemoveSubtasksTest() {
        Epic epic = new Epic("Описание эпика", "Название эпика");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Описание подзадачи 1", "Имя подзадачи 1", epic.getId());
        Subtask subtask2 = new Subtask("Описание подзадачи 2", "Имя подзадачи 2", epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteEpic(epic.getId());

        assertNull(taskManager.findSubtaskById(subtask1.getId()), "Подзадача 1 должна быть удалена вместе с эпиком");
        assertNull(taskManager.findSubtaskById(subtask2.getId()), "Подзадача 2 должна быть удалена вместе с эпиком");
    }

}