package service;

import model.Epic;

import java.util.List;

public interface EpicManager {
    Epic createEpic(Epic epic);

    List<Epic> getAllEpics();

    Epic findEpicById(int id);

    Epic updateEpic(int id, String taskDescription, String taskName);

    void deleteEpic(int id);

    void deleteAllEpics();

    List<Epic> findEpicsByStatus(String status);
}