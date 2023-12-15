package manager.impl;

import manager.interfaces.HistoryManager;
import tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> browsingHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        browsingHistory.add(task);
        if (browsingHistory.size() > 10) {
            browsingHistory.remove(0);
        }
    }

    //https://ru.stackoverflow.com/questions/1119105/%D0%9A%D0%B0%D0%BA-%D1%83%D0%B4%D0%B0%D0%BB%D0%B8%D1%82%D1%8C-%D0%BE%D0%B4%D0%B8%D0%BD%D0%B0%D0%BA%D0%BE%D0%B2%D1%8B%D0%B5-%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%D0%B8%D1%8F-%D0%B2-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B5-arraylist
    @Override
    public Set<Task> getHistory() {
        return new LinkedHashSet<>(browsingHistory);
    }
}