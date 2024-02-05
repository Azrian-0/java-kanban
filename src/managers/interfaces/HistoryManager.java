package managers.interfaces;

import tasks.Task;

import java.util.Set;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    Set<Task> getHistory();

    void clearHistory();
}