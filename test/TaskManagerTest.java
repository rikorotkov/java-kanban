import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    // Этот метод должен быть реализован в подклассах для создания конкретного менеджера
    protected abstract T createTaskManager();

    @Test
    void shouldCalculateEpicStatusWhenAllSubtasksAreNew() {
        taskManager = createTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.NEW, epic.getTaskStatus(), "Статус эпика должен быть NEW.");
    }

    @Test
    void shouldCalculateEpicStatusWhenAllSubtasksAreDone() {
        taskManager = createTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());
        subtask1.setTaskStatus(TaskStatus.DONE);
        subtask2.setTaskStatus(TaskStatus.DONE);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.DONE, epic.getTaskStatus(), "Статус эпика должен быть DONE.");
    }

    @Test
    void shouldCalculateEpicStatusWhenSubtasksAreMixed() {
        taskManager = createTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());
        subtask2.setTaskStatus(TaskStatus.DONE);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void shouldCalculateEpicStatusWhenSubtasksAreInProgress() {
        taskManager = createTaskManager();
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());
        subtask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        subtask2.setTaskStatus(TaskStatus.DONE);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void shouldDetectTimeOverlap() {
        taskManager = createTaskManager();

        Task task1 = new Task(111, "Task 1", "Description", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(1));
        Task task2 = new Task(112, "Task 2", "Description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(30), Duration.ofHours(1));

        taskManager.createTask(task1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
        assertEquals("Задача пересекается с другой задачей по времени выполнения.", exception.getMessage());
    }
}