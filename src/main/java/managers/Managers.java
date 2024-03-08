package managers;

import com.google.gson.*;
import managers.impl.FileBackedTasksManager;
import managers.impl.HttpTaskManager;
import managers.impl.InMemoryHistoryManager;
import managers.impl.InMemoryTaskManager;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Managers {

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(URI.create("http://localhost:8078"));
    }
    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedManager(String filePath) {
        return new FileBackedTasksManager().loadFromFile(Path.of(filePath));
    }
}