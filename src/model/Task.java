package model;

import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Task {
    @Expose
    protected final int id;
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

    public Task(String taskDescription, String taskName) {
        this.id = ++lastId;
        this.taskDescription = taskDescription;
        this.taskName = taskName;
        this.taskStatus = TaskStatus.NEW;
    }

    public Task(int id, String taskName, String taskDescription, TaskStatus taskStatus) {
        this.id = ++lastId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;

        if (id > lastId) {
            lastId = id;
        }
    }

    public Task(int id, String taskName, String taskDescription, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.id = ++lastId;  // Генерация нового ID
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;

        if (id > lastId) {
            lastId = id;
        }
        this.startTime = startTime;
        this.duration = duration;
    }

    public static void setLastId(int id) {
        if (id > lastId) {
            lastId = id;
        }
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
        try {
            String[] fields = csvLine.split(",");
            if (fields.length < 7) {
                throw new IllegalArgumentException("Неверный формат строки CSV: недостаточно данных");
            }

            int id = Integer.parseInt(fields[0].trim());
            String name = fields[2].trim();
            TaskStatus status = TaskStatus.valueOf(fields[3].trim());
            String description = fields[4].trim();

            Duration duration = fields[5].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(fields[5].trim()));

            LocalDateTime startTime = null;
            if (!fields[6].isEmpty()) {
                try {
                    startTime = LocalDateTime.parse(fields[6].trim(), DateTimeFormatter.ISO_DATE_TIME);
                } catch (DateTimeParseException e) {
                    System.err.println("Ошибка парсинга даты/времени в строке: " + csvLine);
                }
            }

            Task task = new Task(id, name, description, status);
            task.setDuration(duration);
            task.setStartTime(startTime);
            return task;
        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования числа в строке: " + csvLine);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка обработки строки CSV: " + csvLine);
            e.printStackTrace();
        }

        return null;
    }

    public String toCsv() {
        String durationStr = (duration != null) ? String.valueOf(duration.toMinutes()) : "";
        String startTimeStr = (startTime != null) ? startTime.toString() : "";
        return String.format("%d,TASK,%s,%s,%s,%s,%s", id, taskName, taskStatus, taskDescription, durationStr, startTimeStr);
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
