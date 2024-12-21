package com.praktikum.kanban.service;

import java.util.logging.Logger;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(Logger.getLogger("default"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
