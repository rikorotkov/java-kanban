package service;

import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.util.List;

public interface SubtaskManager {
    Subtask createSubtask(Subtask subtask);

    List<Subtask> getAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int id);

    Subtask findSubtaskById(int id);

    Subtask updateSubtask(int id, String taskDescription, String taskName, TaskStatus status);

    void deleteSubtask(int id);

    void deleteAllSubtasks();

    List<Subtask> findSubtasksByStatus(String status);
}
