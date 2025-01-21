package service;

import exception.ManagerSaveException;

import java.io.File;
import java.util.logging.Logger;

public class Managers {
    private static final Logger logger = Logger.getLogger("Managers");

    public static TaskManager getDefault() {
        File file = new File("./test.csv");

        if (file.exists()) {
            try {
                return FileBackedTaskManager.loadFromFile(file);
            } catch (ManagerSaveException e) {
                logger.info("Ошибка загрузки данных из файла: " + e.getMessage());
                return new FileBackedTaskManager(file);
            }
        } else {
            return new FileBackedTaskManager(file);
        }
//        return new InMemoryTaskManager(Logger.getLogger("default"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
