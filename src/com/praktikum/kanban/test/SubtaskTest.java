package com.praktikum.kanban.test;

import com.praktikum.kanban.model.Epic;
import com.praktikum.kanban.model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.praktikum.kanban.service.Managers;
import com.praktikum.kanban.service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testSubtaskEqualsById() {
        Epic epic = new Epic("Описание эпика", "Имя эпика");
        Subtask subtask = new Subtask("Описание подзадачи", "Имя подзадачи", epic.getId());

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        Subtask findSubtask = taskManager.findSubtaskById(subtask.getId());
        assertEquals(subtask.getId(), findSubtask.getId(), "Подзадачи не равны");
    }
}