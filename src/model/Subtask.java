package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

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
        System.out.println(epicId);
        return epicId;
    }

    public void setEpicId(int epicId) {
        System.out.println("setEpicId: " + epicId);
        this.epicId = epicId;
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
        String description = fields[3];
        TaskStatus status = TaskStatus.valueOf(fields[4]);

        LocalDateTime startTime = (fields.length > 5 && !fields[5].isEmpty() && !"null".equals(fields[5].trim()))
                ? LocalDateTime.parse(fields[5].trim())
                : null;

        Duration duration = (fields.length > 6 && !fields[6].isEmpty())
                ? Duration.ofMinutes(Long.parseLong(fields[6].trim()))
                : null;

        int epicId = Integer.parseInt(fields[7]);

        Subtask subtask = new Subtask(id, name, description, status, epicId);
        subtask.setDuration(duration);
        subtask.setStartTime(startTime);
        return subtask;
    }

    @Override
    public String toCsv() {
        String durationStr = (duration != null) ? String.valueOf(duration.toMinutes()) : "";
        String startTimeStr = (startTime != null) ? startTime.toString() : "";
        return String.format("%d,SUBTASK,%s,%s,%s,%s,%s,%s", id, taskName, taskDescription, taskStatus, startTimeStr, durationStr, epicId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
