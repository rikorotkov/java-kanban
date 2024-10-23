package model;

import java.util.ArrayList;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskDescription, String taskName, int epicId) {
        super(taskDescription, taskName);
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
