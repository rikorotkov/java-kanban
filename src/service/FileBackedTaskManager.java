package service;

import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String some = reader.readLine();
            reader.lines().forEach(line -> tasks.add(Task.fromCsv(line)));
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return tasks;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write("id, type, name, description, status, startTime, duration, epicId");
            writer.newLine();

            for (Task task : super.getAllTasks()) {
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
                if (line.equals("history")) { // Если это заголовок истории
                    manager.loadHistoryFromFile(reader); // Загружаем историю
                    break; // После истории ничего не читаем
                } else if (!line.startsWith("id") && !line.isBlank()) {
                    Task task = Task.fromCsv(line); // Парсим задачу
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
        // Убедимся, что ID обновляется правильно перед созданием подзадачи
        System.out.println("Перед созданием subtask: id = " + subtask.getId() + ", epicId = " + subtask.getEpicId());

        // Проверяем текущий последний ID и обновляем его, если нужно
        if (subtask.getId() == 0) {
            subtask.setId(Task.getLastId() + 1);  // Присваиваем уникальный ID
            Task.setLastId(subtask.getId());  // Обновляем последний ID
        }

        // Создаем подзадачу
        Subtask createdSubtask = super.createSubtask(subtask);
        System.out.println("После создания subtask: id = " + createdSubtask.getId());

        // Если эпик существует, обновляем его поля
        Epic epic = findEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.recalculateFields();
        }

        // Сохраняем изменения
        save();
        return createdSubtask;
    }

    @Override
    public Task findTaskById(int id) {
//        return getAllTasks()
//                .stream()
//                .filter(task -> task.getId() == id)
//                .findFirst()
//                .orElseThrow();
        Task task = super.findTaskById(id);
        if (task != null) {
            return task;
        }

        Epic epic = findEpicById(id);
        if (epic != null) {
            return epic;
        }

        Subtask subtask = findSubtaskById(id);
        if (subtask != null) {
            return subtask;
        }

        return null;
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory(); // Возвращаем историю из памяти
    }


    private void loadHistoryFromFile(BufferedReader reader) throws IOException {
        String line = reader.readLine(); // Читаем строку с ID

        if (line != null && !line.isBlank()) {
            String[] ids = line.split(",");
            for (String id : ids) {
                id = id.trim();
                if (!id.isEmpty()) {
                    try {
                        int taskId = Integer.parseInt(id);
                        Task task = findTaskById(taskId);
                        if (task != null) {
                            getHistoryManager().add(task);
                        } else {
                            System.out.println("Не найдена задача с id: " + taskId);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка парсинга ID: " + id);
                    }
                }
            }
        }
    }
}