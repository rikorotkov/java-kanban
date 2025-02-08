package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager;
    private final ArrayList<Task> tasks;
    private final ArrayList<Epic> epics;
    private final ArrayList<Subtask> subtasks;
    private final Logger logger;
    private final TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager(Logger logger) {
        this.logger = logger;
        this.historyManager = Managers.getDefaultHistory();
        this.epics = new ArrayList<>();
        this.subtasks = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.prioritizedTasks = new TreeSet<>((task1, task2) -> {
            if (task1.getStartTime() == null && task2.getStartTime() == null) {
                return 0;
            }
            if (task1.getStartTime() == null) {
                return 1;
            }
            if (task2.getStartTime() == null) {
                return -1;
            }
            return task1.getStartTime().compareTo(task2.getStartTime());
        });
    }

    @Override
    public Task createTask(Task task) {
        if (task.getId() == 0) {
            Task.setLastId(Task.getLastId());
            task = new Task(task.getTaskDescription(), task.getTaskName());
        }

        if (isTaskOverlapping(task)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей по времени выполнения.");
        }

        tasks.add(task);
        prioritizedTasks.add(task);

        logger.info("задача " + task.getTaskName() + " зарегистрирована. ID: " + task.getId());

        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                historyManager.add(task);
                return task;
            }
        }
        return null;
    }

    @Override
    public Task updateTask(int id, String taskDescription, String taskName, TaskStatus status) {
        Task task = findTaskById(id);
        if (task == null) {
            throw new IllegalArgumentException("Задача с ID - " + id + "не найдена");
        }
        Task tempTask = new Task(id, taskName, taskDescription, status, task.getStartTime(), task.getDuration());
        if (isTaskOverlapping(tempTask)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей по времени выполнения.");
        }
        task.setTaskDescription(taskDescription);
        task.setTaskName(taskName);
        task.setTaskStatus(status);
        return task;
    }

    @Override
    public void deleteTask(int id) {
        Task task = findTaskById(id);
        tasks.remove(task);
        prioritizedTasks.remove(task);
        logger.info("Задача удалена");
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        logger.info("Список задач очищен");
    }

    @Override
    public List<Task> findTasksByStatus(String status) {
        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warning("Некорректный статус: " + status);
            return new ArrayList<>();
        }

        ArrayList<Task> tasksByStatus = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskStatus() == taskStatus) {
                tasksByStatus.add(task);
            }
        }
        return tasksByStatus;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic.getId() == 0) {
            Epic.setLastId(Epic.getLastId() + 1);
            epic = new Epic(Epic.getLastId(), epic.getTaskDescription(), epic.getTaskName(), epic.getTaskStatus());
        }

        epics.add(epic);
        logger.info("Эпик " + epic.getTaskName() + " успешно создан с ID: " + epic.getId());
        return epic;
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics;
    }

    @Override
    public Epic findEpicById(int id) {
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                historyManager.add(epic);
                return epic;
            }
        }
        return null;
    }

    @Override
    public Epic updateEpic(int id, String taskDescription, String taskName) {
        Epic epic = findEpicById(id);
        if (epic == null) {
            throw new IllegalArgumentException("Эпик с ID - " + id + "не найден");
        }
        epic.setTaskDescription(taskDescription);
        epic.setTaskName(taskName);
        logger.info("Эпик " + epic.getTaskName() + " обновлен");
        return epic;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (Subtask subtask : subtasks) {
            if (subtask.getTaskStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getTaskStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }
        if (allNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
        logger.info("Статус эпика обновлен");
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = findEpicById(id);
        if (epic != null) {
            epics.remove(epic);
            if (epic.getSubtasks() != null) {
                for (Subtask subtask : epic.getSubtasks()) {
                    historyManager.remove(subtask.getId());
                }
            }
            historyManager.remove(id);
            subtasks.removeIf(subtask -> subtask.getEpicId() == id);
        }
        logger.info("Эпик " + epic.getTaskName() + " удален");
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        logger.info("Список эпиков очищен");
    }

    @Override
    public List<Epic> findEpicsByStatus(String status) {
        ArrayList<Epic> filteredEpics = new ArrayList<>();
        for (Epic epic : epics) {
            if (epic.getTaskStatus().toString().equalsIgnoreCase(status)) {
                filteredEpics.add(epic);
            }
        }
        return filteredEpics;
    }

    public Subtask createSubtask(Subtask subtask) {
        logger.info("Получен epicId: " + subtask.getEpicId());
        Epic epic = findEpicById(subtask.getEpicId());
        if (epic == null) {
            logger.info("Не удалось найти эпик с ID " + subtask.getEpicId());
            return null;
        }
        logger.info("Создание подзадачи с названием " + subtask.getTaskName() + " для эпика с названием " + epic.getTaskName());
        epic.addSubtask(subtask);
        subtasks.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return subtasks;
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask.getEpicId() == id) {
                subtasksByEpic.add(subtask);
            }
        }
        return subtasksByEpic;
    }

    @Override
    public Subtask findSubtaskById(int id) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == id) {
                historyManager.add(subtask);
                return subtask;
            }
        }
        return null;
    }

    @Override
    public Subtask updateSubtask(int id, String taskDescription, String taskName, TaskStatus status) {
        Subtask subtask = findSubtaskById(id);

        if (subtask == null) {
            throw new IllegalArgumentException("Подзадача с ID - " + id + "не найдена");
        }

        Epic epic = findEpicById(subtask.getEpicId());
        subtask.setTaskDescription(taskDescription);
        subtask.setTaskName(taskName);
        subtask.setTaskStatus(status);
        logger.info("Подзадача " + subtask.getTaskName() + " обновлена");
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            subtasks.remove(subtask);
            prioritizedTasks.remove(subtask);
            logger.info("Подзадача " + subtask.getTaskName() + " удалена");
        }
        Epic epic = findEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.recalculateFields();
        }
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        logger.info("Список подзадач очищен");
    }

    @Override
    public List<Subtask> findSubtasksByStatus(String status) {
        ArrayList<Subtask> subtasksByStatus = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask.getTaskStatus().toString().equalsIgnoreCase(status)) {
                subtasksByStatus.add(subtask);
            }
        }
        return subtasksByStatus;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean isTimeOverlapping(Task existingTask, Task newTask) {
        if (existingTask.getStartTime() == null || existingTask.getDuration() == null ||
                newTask.getStartTime() == null || newTask.getDuration() == null) {
            return false;
        }
        LocalDateTime existingStart = existingTask.getStartTime();
        LocalDateTime existingEnd = existingStart.plus(existingTask.getDuration());
        LocalDateTime newStart = newTask.getStartTime();
        LocalDateTime newEnd = newStart.plus(newTask.getDuration());

        return !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd));
    }

    @Override
    public boolean isTaskOverlapping(Task newTask) {
        return getPrioritizedTasks().stream()
                .anyMatch(existingTask -> isTimeOverlapping(existingTask, newTask));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
