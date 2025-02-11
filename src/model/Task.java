package model;

import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    @Expose
    protected int id;
    @Expose
    protected String taskName;
    @Expose
    protected String taskDescription;
    @Expose
    protected TaskStatus taskStatus;
    @Expose
    protected Duration duration;
    @Expose
    protected LocalDateTime startTime;
    @Expose
    private static int lastId = 0;

    public Task() {
        this.id = ++lastId;
    }

    public Task(String taskDescription, String taskName) {
        this.id = ++lastId;
        this.taskDescription = taskDescription;
        this.taskName = taskName;
        this.taskStatus = TaskStatus.NEW;
    }

    public Task(int id, String taskName, String taskDescription, TaskStatus taskStatus) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;

        lastId = Math.max(lastId, id);
    }

    public Task(int id, String taskName, String taskDescription, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.id = ++lastId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;

        if (id > lastId) {
            lastId = id;
        }
        this.startTime = startTime;
        this.duration = duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void setLastId(int id) {
        lastId = Math.max(lastId, id);
    }

    public static int getLastId() {
        return lastId;
    }

    public int getId() {
        return id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public static Task fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");

        int id = Integer.parseInt(fields[0].trim());
        String name = fields[2].trim();
        String description = fields[3].trim();

        TaskStatus status = (fields[4] != null && !fields[4].trim().isEmpty() && !"null".equalsIgnoreCase(fields[4].trim()))
                ? TaskStatus.valueOf(fields[4].trim())
                : TaskStatus.NEW;

        LocalDateTime startTime = (fields.length > 5 && !fields[5].isEmpty() && !"null".equals(fields[5].trim()))
                ? LocalDateTime.parse(fields[5].trim())
                : null;

        Duration duration = (fields.length > 6 && !fields[6].isEmpty())
                ? Duration.ofMinutes(Long.parseLong(fields[6].trim()))
                : null;

        Task task = new Task(id, name, description, status);
        task.setDuration(duration);
        task.setStartTime(startTime);
        return task;
    }

    public String toCsv() {
        String durationStr = (duration != null) ? String.valueOf(duration.toMinutes()) : "";
        String startTimeStr = (startTime != null) ? startTime.toString() : "";
        return String.format("%d,%s,%s,%s,%s,%s,%s,",
                id,
                getType(),
                taskName,
                taskDescription,
                taskStatus,
                startTimeStr,
                durationStr
        );
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "Task {" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
