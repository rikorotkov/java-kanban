package model;

import java.time.Duration;
import java.time.LocalDateTime;
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
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        int epicId = Integer.parseInt(fields[5]);
        Duration duration = fields[6].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(fields[6]));
        LocalDateTime startTime = fields[7].isEmpty() ? null : LocalDateTime.parse(fields[7]);

        Subtask subtask = new Subtask(id, name, description, status, epicId);
        subtask.setDuration(duration);
        subtask.setStartTime(startTime);
        return subtask;
    }

    @Override
    public String toCsv() {
        String durationStr = (duration != null) ? String.valueOf(duration.toMinutes()) : "";
        String startTimeStr = (startTime != null) ? startTime.toString() : "";
        return String.format("%d,SUBTASK,%s,%s,%s,%d,%s,%s", id, taskName, taskStatus, taskDescription, epicId, durationStr, startTimeStr);
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
