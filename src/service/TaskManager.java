package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TaskManager {
    private final ArrayList<Task> tasks;
    private final ArrayList<Epic> epics;
    private final ArrayList<Subtask> subtasks;
    private final Logger logger;

    public TaskManager(Logger logger) {
        this.logger = logger;
        this.epics = new ArrayList<>();
        this.subtasks = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public Task createTask(Task task) {
        tasks.add(task);
        logger.info("задача " + task.getTaskName() + " зарегистрирована");
        return task;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public Task updateTask(int id, String taskDescription, String taskName, TaskStatus status) {
        Task task = findTaskById(id);
        task.setTaskDescription(taskDescription);
        task.setTaskName(taskName);
        task.setTaskStatus(status);
        return task;
    }

    public void deleteTask(int id) {
        Task task = findTaskById(id);
        tasks.remove(task);
        logger.info("Задача удалена");
    }

    public void deleteAllTasks() {
        tasks.clear();
        logger.info("Список задач очищен");
    }

    public List<Task> findTasksByStatus(String status) {
        ArrayList<Task> tasksByStatus = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskStatus().equals(status.toUpperCase())) {
                tasksByStatus.add(task);
            }
        }
        return tasksByStatus;
    }

    public Epic createEpic(Epic epic) {
        epics.add(epic);
        logger.info("Эпик " + epic.getTaskName() + " успешно создан");
        return epic;
    }

    public List<Epic> getAllEpics() {
        return epics;
    }

    public Epic findEpicById(int id) {
        for (Epic epic : epics) {
            if (epic.getId() == id) {
                return epic;
            }
        }
        return null;
    }

    public Epic updateEpic(int id, String taskDescription, String taskName) {
        Epic epic = findEpicById(id);
        epic.setTaskDescription(taskDescription);
        epic.setTaskName(taskName);
        logger.info("Эпик " + epic.getTaskName() + " обновлен");
        return epic;
    }

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

    public void deleteEpic(int id) {
        Epic epic = findEpicById(id);
        if (epic != null) {
            epics.remove(epic);
            subtasks.removeIf(subtask -> subtask.getEpicId() == id);
        }
        logger.info("Эпик " + epic.getTaskName() + " удален");
    }

    public void deleteAllEpics() {
        epics.clear();
        logger.info("Список эпиков очищен");
    }

    public ArrayList<Epic> findEpicsByStatus(String status) {
        for (Epic epic : epics) {
            if (epic.getTaskStatus().equals(status)) {
                return epics;
            }
        }
        return null;
    }

    public Subtask createSubtask(Subtask subtask) {
        Epic epic = findEpicById(subtask.getEpicId());
        if (epic == null) {
            logger.info("Эпик с ID " + subtask.getEpicId() + " не найден");
            return null;
        }
        epic.addSubtask(subtask);
        subtasks.add(subtask);
        logger.info("Подзадача " + subtask.getTaskName() + " была добавлена в эпик " + epic.getTaskName());
        updateEpicStatus(epic);
        return subtask;
    }

    public List<Subtask> getAllSubtasks() {
        return subtasks;
    }

    public List<Subtask> getSubtasksByEpicId(int id) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask.getEpicId() == id) {
                subtasksByEpic.add(subtask);
            }
        }
        return subtasksByEpic;
    }

    public Subtask findSubtaskById(int id) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == id) {
                return subtask;
            }
        }
        return null;
    }

    public Subtask updateSubtask(int id, String taskDescription, String taskName, TaskStatus status) {
        Subtask subtask = findSubtaskById(id);
        Epic epic = findEpicById(subtask.getEpicId());
        if (subtask != null) {
            subtask.setTaskDescription(taskDescription);
            subtask.setTaskName(taskName);
            subtask.setTaskStatus(status);
            logger.info("Подзадача " + subtask.getTaskName() + " обновлена");
        }
        updateEpicStatus(epic);
        return subtask;
    }

    public void deleteSubtask(int id) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            subtasks.remove(subtask);
            logger.info("Подзадача " + subtask.getTaskName() + " удалена");
        }
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        logger.info("Список подзадач очищен");
    }

    public List<Subtask> findSubtasksByStatus(String status) {
        ArrayList<Subtask> subtasksByStatus = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask.getTaskStatus().toString().equalsIgnoreCase(status)) {
                subtasksByStatus.add(subtask);
            }
        }
        return subtasksByStatus;
    }
}
