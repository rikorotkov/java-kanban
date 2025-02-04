package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Task {
    protected final int id;
    protected String taskName;
    protected String taskDescription;
    protected TaskStatus taskStatus;
    protected Duration duration;
    protected LocalDateTime startTime;

    private static int lastId = 0;

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

        if (id > lastId) {
            lastId = id;
        }
    }

    public Task(int id, String taskName, String taskDescription, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;

        if (id > lastId) {
            lastId = id;
        }
        this.startTime = startTime;
        this.duration = duration;
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
                System.err.println("Неверный формат строки CSV: недостаточно данных. Строка: " + csvLine);
                return null;
            }

            // Парсим ID задачи
            int id;
            try {
                id = Integer.parseInt(fields[0].trim());
            } catch (NumberFormatException e) {
                System.err.println("Ошибка преобразования ID задачи в строке: " + csvLine);
                return null;
            }

            // Имя задачи
            String name = fields[2].trim();
            if (name.isEmpty()) {
                System.err.println("Имя задачи пустое в строке: " + csvLine);
                return null;
            }

            // Статус задачи
            TaskStatus status;
            try {
                status = TaskStatus.valueOf(fields[3].trim());
            } catch (IllegalArgumentException e) {
                System.err.println("Неверный статус задачи в строке: " + csvLine);
                return null;
            }

            // Описание задачи
            String description = fields[4].trim();
            if (description.isEmpty()) {
                System.err.println("Описание задачи пустое в строке: " + csvLine);
                return null;
            }

            // Длительность задачи
            Duration duration = null;
            if (!fields[5].isEmpty()) {
                try {
                    duration = Duration.ofMinutes(Long.parseLong(fields[5].trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка преобразования длительности задачи в строке: " + csvLine);
                    return null;
                }
            }

            // Время начала задачи
            LocalDateTime startTime = null;
            if (!fields[6].isEmpty()) {
                try {
                    String correctedTime = fields[6].trim().replace(",", "T"); // Исправляем запятую на T
                    startTime = LocalDateTime.parse(correctedTime, DateTimeFormatter.ISO_DATE_TIME);
                } catch (DateTimeParseException e) {
                    System.err.println("Ошибка парсинга даты/времени в строке: " + csvLine);
                    return null;
                }
            }

            // Создаем и возвращаем задачу
            Task task = new Task(id, name, description, status);
            task.setDuration(duration);
            task.setStartTime(startTime);
            return task;

        } catch (Exception e) {
            System.err.println("Неизвестная ошибка при обработке строки CSV: " + csvLine);
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
