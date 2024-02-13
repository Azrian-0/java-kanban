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

    // почему нужно возвращать FileBackedTasksManager, когда он не является реализацией HistoryManager?
    // должен ли метод getDefaultHistory принимать параметр String filePath?
    // должен ли FileBackedTasksManager имплементировать HistoryManager? по т.з я не понял

    public static FileBackedTasksManager getFileBackedManager(String filePath) {
        return new FileBackedTasksManager(filePath);
    }
}