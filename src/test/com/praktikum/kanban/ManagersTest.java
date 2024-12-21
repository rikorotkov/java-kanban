package test.com.praktikum.kanban;

import org.junit.jupiter.api.Test;
import com.praktikum.kanban.service.HistoryManager;
import com.praktikum.kanban.service.Managers;
import com.praktikum.kanban.service.TaskManager;

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