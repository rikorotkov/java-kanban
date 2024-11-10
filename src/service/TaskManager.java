package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    List<Task> getAllTasks();

    Task findTaskById(int id);

    Task updateTask(int id, String taskDescription, String taskName, TaskStatus status);

    void deleteTask(int id);

    void deleteAllTasks();

    List<Task> findTasksByStatus(String status);

    Epic createEpic(Epic epic);

    List<Epic> getAllEpics();

    Epic findEpicById(int id);

    Epic updateEpic(int id, String taskDescription, String taskName);

    void updateEpicStatus(Epic epic);

    void deleteEpic(int id);

    void deleteAllEpics();

    ArrayList<Epic> findEpicsByStatus(String status);

    Subtask createSubtask(Subtask subtask);

    List<Subtask> getAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int id);

    Subtask findSubtaskById(int id);

    Subtask updateSubtask(int id, String taskDescription, String taskName, TaskStatus status);

    void deleteSubtask(int id);

    void deleteAllSubtasks();

    List<Subtask> findSubtasksByStatus(String status);
}
