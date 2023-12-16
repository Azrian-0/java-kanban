package manager.impl;

import manager.interfaces.HistoryManager;
import tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_MAX_SIZE = 10;

    private final List<Task> browsingHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        browsingHistory.add(task);
        if (browsingHistory.size() > HISTORY_MAX_SIZE) {
            browsingHistory.remove(0);
        }
    }

    @Override
    public Set<Task> getHistory() {
        return new LinkedHashSet<>(browsingHistory);
    }
}