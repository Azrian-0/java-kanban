package manager.interfaces;

import tasks.Task;
import java.util.Set;

public interface HistoryManager {
    void add(Task task);

    Set<Task> getHistory();
}