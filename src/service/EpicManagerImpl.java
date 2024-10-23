package service;

import model.Epic;
import model.Subtask;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class EpicManagerImpl implements EpicManager {
    private ArrayList<Epic> epics;

    public EpicManagerImpl() {
        this.epics = new ArrayList<>();
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
    public void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtasks();
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
        System.out.println("Статус эпика обновлен");
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
}
