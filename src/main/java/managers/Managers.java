package managers;

import server.KVServer;
import managers.impl.FileBackedTasksManager;
import managers.impl.HttpTaskManager;
import managers.impl.InMemoryHistoryManager;
import managers.impl.InMemoryTaskManager;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(URI.create("http://localhost:" + KVServer.PORT));
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedManager(String filePath) {
        return new FileBackedTasksManager(Path.of("../BackedTasks.csv")).loadFromFile(Path.of(filePath));
    }

    public static TaskManager getHttpTaskManager(String uri, Integer port) throws IOException, InterruptedException {
        return new HttpTaskManager(URI.create(uri + port));
    }
}