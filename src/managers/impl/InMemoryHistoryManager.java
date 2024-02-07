package managers.impl;

import managers.interfaces.HistoryManager;
import tasks.Task;
import util.CustomLinkedList;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> browsingHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        browsingHistory.add(task);
    }

    @Override
    public void remove(int id) {
        browsingHistory.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return browsingHistory.toArrayList();
    }

    @Override
    public void clearHistory() {
        browsingHistory.clearHistory();
    }
}