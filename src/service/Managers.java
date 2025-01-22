package service;

import exception.ManagerSaveException;

import java.io.File;
import java.io.IOException;
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

                try {
                    if (file.createNewFile()) {
                        logger.info("Новый файл был создан: " + file.getAbsolutePath());
                    } else {
                        logger.info("Файл уже существует: " + file.getAbsolutePath());
                    }
                } catch (IOException ioException) {
                    logger.severe("Ошибка при создании файла: " + ioException.getMessage());
                }

                logger.info("Произошла ошибка при загрузке данных из файла. Все данные были сброшены. Создан новый файл.");

                return new FileBackedTaskManager(file);
            }
        } else {
            try {
                if (file.createNewFile()) {
                    logger.info("Новый файл был создан: " + file.getAbsolutePath());
                } else {
                    logger.info("Файл уже существует: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                logger.severe("Ошибка при создании файла: " + e.getMessage());
            }

            return new FileBackedTaskManager(file);
        }
//        return new InMemoryTaskManager(Logger.getLogger("default"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
