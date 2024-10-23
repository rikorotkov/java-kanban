package service;

import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class SubtaskManagerImpl implements SubtaskManager {
    private ArrayList<Subtask> subtasks;
    private EpicManagerImpl epicManager;

    public SubtaskManagerImpl(EpicManagerImpl epicManager) {
        this.subtasks = new ArrayList<>();
        this.epicManager = epicManager;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epicManager.findEpicById(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик с ID " + subtask.getEpicId() + " не найден");
            return null;
        }
        epic.addSubtask(subtask);
        subtasks.add(subtask);
        System.out.println("Подзадача " + subtask.getTaskName() + " была добавлена в эпик " + epic.getTaskName());
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
    public Subtask updateSubtask(int id, String taskDescription, String taskName, TaskStatus status) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            subtask.setTaskDescription(taskDescription);
            subtask.setTaskName(taskName);
            subtask.setTaskStatus(status);
            System.out.println("Подзадача " + subtask.getTaskName() + " обновлена");
        }
        return subtask;
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
