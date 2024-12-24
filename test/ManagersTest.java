package test;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void testUtilityClassInitializesManagers() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}