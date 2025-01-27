package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
        this.subtasks = new ArrayList<>();
        recalculateFields();
    }

    public Epic(int id, String taskDescription, String taskName, TaskStatus status) {
        super(id, taskDescription, taskName, status);
        this.subtasks = new ArrayList<>();
        recalculateFields();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        recalculateFields();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void recalculateFields() {
        if (subtasks.isEmpty()) {
            duration = Duration.ZERO;
            startTime = null;
            endTime = null;
            return;
        }

        duration = subtasks.stream()
                .filter(subtask -> subtask.getDuration() != null)
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        startTime = subtasks.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        endTime = subtasks.stream()
                .filter(subtask -> subtask.getEndTime() != null)
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
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
        long durationInMinutes = duration != null ? duration.toMinutes() : 0;
        String startTimeStr = startTime != null ? startTime.toString() : "";
        String endTimeStr = endTime != null ? endTime.toString() : "";

        return String.format("%d,EPIC,%s,%s,%s,%d,%s,%s", id, taskName, taskStatus, taskDescription,
                durationInMinutes, startTimeStr, endTimeStr);
    }

    @Override
    public String toString() {
        return "Epic {" +
                "id = " + getId() +
                ", epicName = '" + getTaskName() + '\'' +
                ", epicDescription = '" + getTaskDescription() + '\'' +
                ", subtasksCount = " + subtasks.size() +
                ", duration = " + duration +
                ", startTime = " + startTime +
                ", endTime = " + endTime +
                ", status = " + getTaskStatus() +
                '}';
    }
}
