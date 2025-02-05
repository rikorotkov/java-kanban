    package model;

    import com.google.gson.annotations.Expose;
    import com.google.gson.annotations.SerializedName;

    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.util.ArrayList;

    public class Epic extends Task {
        private ArrayList<Subtask> subtasks;

        @Expose
        @SerializedName("epic_duration")
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

        public void recalculateFields() {
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
            String description = fields[3];
            TaskStatus status = TaskStatus.valueOf(fields[4]);

            LocalDateTime startTime = (fields.length > 5 && !fields[5].isEmpty() && !"null".equals(fields[5].trim()))
                    ? LocalDateTime.parse(fields[5].trim())
                    : null;

            Duration duration = (fields.length > 6 && !fields[6].isEmpty())
                    ? Duration.ofMinutes(Long.parseLong(fields[6].trim()))
                    : null;

            Epic epic = new Epic(id, description, name, status);
            epic.setDuration(duration);
            epic.setStartTime(startTime);
            return epic;
        }


        @Override
        public String toCsv() {
            String durationStr = (duration != null) ? String.valueOf(duration.toMinutes()) : "";
            String startTimeStr = (startTime != null) ? startTime.toString() : "";
            return String.format("%d,EPIC,%s,%s,%s,%s,%s,", id, taskName, taskDescription, taskStatus, startTimeStr, durationStr);
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
