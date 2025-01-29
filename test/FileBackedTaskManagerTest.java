import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private FileBackedTaskManager taskManager;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("test_tasks.csv"));
    }

    @Test
    void shouldLinkSubtaskToEpic() {
        FileBackedTaskManager taskManager = createTaskManager();
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Subtask description", epic.getId());
        taskManager.createSubtask(subtask);

        Epic loadedEpic = taskManager.findEpicById(epic.getId());
        assertNotNull(loadedEpic, "Эпик должен быть найден.");
        assertEquals(1, loadedEpic.getSubtasks().size(), "Эпик должен содержать одну подзадачу.");
        assertEquals(subtask, loadedEpic.getSubtasks().get(0), "Подзадача должна быть привязана к эпику.");
    }

    @Test
    void shouldCalculateEpicStatusBasedOnSubtasks() {
        FileBackedTaskManager taskManager = createTaskManager();
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());
        subtask2.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic loadedEpic = taskManager.findEpicById(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, loadedEpic.getTaskStatus(),
                "Статус эпика должен быть рассчитан на основе подзадач.");
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        File invalidFile = new File("invalid_path_to_file");

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(invalidFile);
        });
    }

    @Test
    void shouldNotThrowExceptionForValidFile() {
        File validFile = new File("test_tasks.csv");

        assertDoesNotThrow(() -> {
            new FileBackedTaskManager(validFile); // Не должно выбросить исключение
        }, "Не должно быть исключения при использовании корректного файла");
    }
}
