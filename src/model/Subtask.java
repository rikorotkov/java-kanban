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
