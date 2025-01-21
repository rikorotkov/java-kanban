package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
        this.subtasks = new ArrayList<>();
    }

    public Epic(int id, String taskDescription, String taskName, TaskStatus status) {
        super(id, taskDescription, taskName, status);
        this.subtasks = new ArrayList<>();
    }


    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public TaskType getTaskType() {
        return TaskType.EPIC;
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
