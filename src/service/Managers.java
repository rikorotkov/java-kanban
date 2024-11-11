package service;

import java.util.logging.Logger;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(Logger.getLogger("default"));
    }
}
