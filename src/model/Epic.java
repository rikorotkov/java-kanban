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

    public static Epic fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        String name = fields[2];
        String description = fields[4];
        TaskStatus status = TaskStatus.valueOf(fields[3]);

        return new Epic(id, name, description, status);
    }

    @Override
    public String toCsv() {
        return String.format("%d,EPIC,%s,%s,%s", id, taskName, taskStatus, taskDescription);
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
