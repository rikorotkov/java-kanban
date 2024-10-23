package service;

import model.Task;
import model.TaskStatus;

import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    List<Task> getAllTasks();

    Task findTaskById(int id);

    Task updateTask(int id, String taskDescription, String taskName, TaskStatus status);

    void deleteTask(int id);

    void deleteAllTasks();

    List<Task> findTasksByStatus(String status);
}
