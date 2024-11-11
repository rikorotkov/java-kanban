package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>(10);
    }

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        } else {
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
