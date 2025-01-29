package service;

import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final HistoryManager historyManager;
    private final File file;

    public FileBackedTaskManager(File file) {
        super(Logger.getLogger(FileBackedTaskManager.class.getName()));
        this.file = file;
        this.historyManager = Managers.getDefaultHistory();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(task.toCsv());
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(epic.toCsv());
                writer.newLine();
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtask.toCsv());
                writer.newLine();
            }

            List<Task> history = getHistory();
            if (!history.isEmpty()) {
                writer.write("history");
                writer.newLine();
                for (Task task : history) {
                    writer.write(String.valueOf(task.getId()));
                    writer.write(",");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных в файл: " + file.getAbsolutePath(), e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        if (file == null || !file.exists() || !file.isFile()) {
            String filePath = file != null ? file.getAbsolutePath() : "null";
            throw new ManagerSaveException("Файл не существует или недоступен: " + filePath, null);
        }

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("history")) {
                    line = reader.readLine();
                    if (line != null && !line.isBlank()) {
                        String[] ids = line.split(",");
                        for (String id : ids) {
                            Task task = manager.findTaskById(Integer.parseInt(id.trim()));
                            if (task != null) {
                                manager.getHistoryManager().add(task);
                            }
                        }
                    }
                } else if (!line.startsWith("id") && !line.isBlank()) {
                    Task task = Task.fromCsv(line);
                    if (task != null) {
                        if (task instanceof Subtask) {
                            manager.createSubtask((Subtask) task);
                        } else if (task instanceof Epic) {
                            manager.createEpic((Epic) task);
                        } else {
                            manager.createTask(task);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла: " + file.getAbsolutePath(), e);
        }

        return manager;
    }


    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        Epic epic = findEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.recalculateFields();
        }
        save();
        return createdSubtask;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            super.deleteSubtask(id);
            Epic epic = findEpicById(subtask.getEpicId());
            if (epic != null) {
                epic.recalculateFields();
            }

            save();
        }
    }
}
