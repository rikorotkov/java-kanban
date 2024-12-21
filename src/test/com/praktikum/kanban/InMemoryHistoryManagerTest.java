package test.com.praktikum.kanban;

import com.praktikum.kanban.model.Epic;
import com.praktikum.kanban.model.Subtask;
import com.praktikum.kanban.model.Task;
import com.praktikum.kanban.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.praktikum.kanban.service.HistoryManager;
import com.praktikum.kanban.service.Managers;
import com.praktikum.kanban.service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault();
    }

    @Test
    public void addToHistoryTest() {
        Task task = new Task("Описание задачи", "Имя задачи");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "История не пустая");
    }

    @Test
    public void addToHistoryAndPreventDuplicatesTest() {
        Task task1 = new Task("Описание задачи 1", "Имя задачи 1");
        Task task2 = new Task("Описание задачи 2", "Имя задачи 2");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История содержит повторяющиеся задачи");
        assertEquals(task2, history.get(0), "Порядок задач в истории нарушен");
        assertEquals(task1, history.get(1), "Последняя задача добавлена неверно");
    }

    @Test
    public void removeTaskFromHistoryTest() {
        Task task1 = new Task("Описание задачи 1", "Имя задачи 1");
        Task task2 = new Task("Описание задачи 2", "Имя задачи 2");

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Задача не удалена из истории");
        assertEquals(task2, history.getFirst(), "Удалена неверная задача");
    }

    @Test
    public void emptyHistoryTest() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть null");
        assertTrue(history.isEmpty(), "История должна быть пустой");
    }

    @Test
    public void clearHistoryTest() {
        Task task1 = new Task("Описание задачи 1", "Имя задачи 1");
        Task task2 = new Task("Описание задачи 2", "Имя задачи 2");

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());
        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления всех задач");
    }

    @Test
    public void epicAndSubtasksHistoryTest() {
        Epic epic = new Epic("Описание эпика", "Имя эпика");
        Subtask subtask1 = new Subtask("Описание подзадачи 1", "Имя подзадачи 1", epic.getId());
        Subtask subtask2 = new Subtask("Описание подзадачи 2", "Имя подзадачи 2", epic.getId());

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteEpic(epic.getId());

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "Удаление эпика должно удалять его подзадачи из истории");
    }

    @Test
    public void updateTaskShouldReflectInHistoryTest() {
        Task task = new Task("Описание задачи", "Имя задачи");
        taskManager.createTask(task);

        historyManager.add(task);

        taskManager.updateTask(task.getId(), "Новое описание", "Новое имя", TaskStatus.IN_PROGRESS);

        Task updatedTask = taskManager.findTaskById(task.getId());
        List<Task> history = historyManager.getHistory();

        assertEquals(updatedTask, history.get(0), "История должна обновляться при изменении задачи");
    }
}