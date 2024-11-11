package test;

import model.Epic;
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

}