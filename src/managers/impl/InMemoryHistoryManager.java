package managers.impl;

import managers.interfaces.HistoryManager;
import tasks.Task;
import util.CustomLinkedList;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_MAX_SIZE = 10;

    private final CustomLinkedList<Task> browsingHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        browsingHistory.add(task);
        if (browsingHistory.size() > HISTORY_MAX_SIZE) {
            browsingHistory.remove(0);
        }
    }

    @Override
    public void remove(int id) {
        browsingHistory.remove(id);
    }

    @Override
    public Set<Task> getHistory() {
        return browsingHistory.toSet();
    }

    @Override
    public void clearHistory() {
        browsingHistory.clearHistory();
    }
}