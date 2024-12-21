package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic {" +
                "id = " + this.getId() +
                ", epicName = '" + this.getTaskName() + '\'' +
                ", epicDescription = '" + this.getTaskDescription() + '\'' +
                ", subtasksCount = " + subtasks.size() +
                ", status = " + this.getTaskStatus() +
                '}';
    }
}
