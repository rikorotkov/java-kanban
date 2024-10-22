package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerImpl implements TaskManager, SubtaskManager, EpicManager {
    private ArrayList<Task> tasks;
    private ArrayList<Epic> epics;
    private ArrayList<Subtask> subtasks;

    public TaskManagerImpl() {
        tasks = new ArrayList<>();
        epics = new ArrayList<>();
        subtasks = new ArrayList<>();
    }

    @Override
    public Task createTask(Task task) {
        tasks.add(task);
        System.out.println("задача " + task.getTaskName() + " зарегистрирована");
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
                return task;
            }
        }
        return null;
    }

    @Override
    public Task updateTask(int id, String taskDescription, String taskName) {
        Task task = findTaskById(id);
        task.setTaskDescription(taskDescription);
        task.setTaskName(taskName);
        return task;
    }

    @Override
    public void deleteTask(int id) {
        Task task = findTaskById(id);
        tasks.remove(task);
        System.out.println("Задача удалена");
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Список задач очищен");
    }

    @Override
    public List<Task> findTasksByStatus(String status) {
        ArrayList<Task> tasksByStatus = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskStatus().equals(status.toUpperCase())) {
                tasksByStatus.add(task);
            }
        }
        return tasksByStatus;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epics.add(epic);
        System.out.println("Эпик " + epic.getTaskName() + " успешно создан");
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
                return epic;
            }
        }
        return null;
    }

    @Override
    public Epic updateEpic(int id, String taskDescription, String taskName) {
        Epic epic = findEpicById(id);
        epic.setTaskDescription(taskDescription);
        epic.setTaskName(taskName);
        System.out.println("Эпик " + epic.getTaskName() + " обновлен");
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = findEpicById(id);
        epics.remove(epic);
        System.out.println("Эпик " + epic.getTaskName() + " удален");
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        System.out.println("Список эпиков очищен");
    }

    @Override
    public List<Epic> findEpicsByStatus(String status) {
        for (Epic epic : epics) {
            if (epic.getTaskStatus().equals(status)) {
                return epics;
            }
        }
        return null;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtasks.add(subtask);
        System.out.println("Подзадача " + subtask.getTaskName() + " была добавлена в эпик " + subtask.getEpicName(epics));
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
                return subtask;
            }
        }
        return null;
    }

    @Override
    public Subtask updateSubtask(int id, String taskDescription, String taskName) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            subtask.setTaskDescription(taskDescription);
            subtask.setTaskName(taskName);
            System.out.println("Подзадача " + subtask.getTaskName() + " обновлена");
        }
        return subtask;
    }

    @Override
    public void deleteSubtaskInEpic(int id) {
        // TODO
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            subtasks.remove(subtask);
            System.out.println("Подзадача " + subtask.getTaskName() + " удалена");
        }
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        System.out.println("Список подзадач очищен");
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
}
