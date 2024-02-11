package managers;

import managers.impl.FileBackedTasksManager;
import managers.impl.InMemoryHistoryManager;
import managers.impl.InMemoryTaskManager;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedManager(String filePath) {
        return new FileBackedTasksManager(filePath);
    }
}