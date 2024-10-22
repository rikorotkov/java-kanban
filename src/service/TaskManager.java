package service;

import model.Task;

import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    List<Task> getAllTasks();

    Task findTaskById(int id);

    Task updateTask(int id, String taskDescription, String taskName);

    void deleteTask(int id);

    void deleteAllTasks();

    List<Task> findTasksByStatus(String status);
}
