package model;

import java.util.ArrayList;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskDescription, String taskName, int epicId) {
        super(taskDescription, taskName);
        this.epicId = epicId;
    }

    public Subtask(int id, String taskName, String taskDescription, TaskStatus taskStatus, int epicId) {
        super(id, taskDescription, taskName, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public String getEpicName(ArrayList<Epic> epics) {
        for (Epic epic : epics) {
            if (epic.getId() == epicId) {
                return epic.getTaskName();
            }
        }
        return null;
    }

    public static Subtask fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        String name = fields[2];
        String description = fields[4];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        int epicId = Integer.parseInt(fields[5]);

        return new Subtask(id, name, description, status, epicId);
    }

    @Override
    public String toCsv() {
        return String.format("%d,SUBTASK,%s,%s,%s,%d", id, taskName, taskStatus, taskDescription, epicId);
    }

    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask {" +
                "id = " + super.getId() +
                ", subtaskName = " + this.getTaskName() +
                ", subtaskDescription = " + this.getTaskDescription() +
                ", epicId=" + epicId +
                " , taskStatus = " + this.getTaskStatus() +
                '}';
    }
}
