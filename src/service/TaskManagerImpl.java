package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerImpl implements TaskManager {
    private ArrayList<Task> tasks;

    public TaskManagerImpl() {
        tasks = new ArrayList<>();
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
    public Task updateTask(int id, String taskDescription, String taskName, TaskStatus status) {
        Task task = findTaskById(id);
        task.setTaskDescription(taskDescription);
        task.setTaskName(taskName);
        task.setTaskStatus(status);
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
}
