package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
        this.subtasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                "} " + super.toString() +
                '}';
    }
}
