package managers;

import enums.Status;
import managers.impl.InMemoryHistoryManager;
import managers.interfaces.HistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

class InMemoryHistoryManagerTest {

    private HistoryManager manager;

    @BeforeEach
    public void createInMemoryTasksManagerTest() {
        this.manager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        final Task task = new Task("Таск1", "Описание", Status.NEW, 0, null);
        manager.add(task);

        final List<Task> history = manager.getHistory();

        Assertions.assertNotNull(history, "Неправильный размер истории");
        Assertions.assertEquals(1, history.size(), "Неправильный размер истории");
    }

    @Test
    void remove() {
        final Task task = new Task("Таск1", "Описание", Status.NEW, 0, null);
        manager.add(task);

        final List<Task> history = manager.getHistory();

        Assertions.assertNotNull(history, "Неправильный размер истории");
        Assertions.assertEquals(1, history.size(), "Неправильный размер истории");
        manager.remove(task.getId());

        final List<Task> historyClear = manager.getHistory();

        Assertions.assertEquals(0, historyClear.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        final Task task = new Task("Таск1", "Описание", Status.NEW, 0, null);
        List<Task> historyClear = manager.getHistory();
        Assertions.assertEquals(0, historyClear.size(), "История не пуста");
        manager.add(task);
        manager.add(task);
        List<Task> history = manager.getHistory();
        Assertions.assertEquals(1, history.size(), "Неверный размер истории");
    }

    @Test
    void clearHistory() {
        List<Task> historyClear = manager.getHistory();
        Assertions.assertEquals(0, historyClear.size(), "История не пуста");
        manager.clearHistory();
        List<Task> emptyHistory = manager.getHistory();
        Assertions.assertEquals(0, emptyHistory.size(), "Неверный размер истории");
    }

    @Test
    void removeFromHistory() {
        final Task task1 = new Task(1, "Таск1", "Описание", Status.NEW, 0, null);
        final Task task2 = new Task(2, "Таск1", "Описание", Status.NEW, 0, null);
        final Task task3 = new Task(3, "Таск1", "Описание", Status.NEW, 0, null);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        List<Task> history1 = manager.getHistory();
        manager.remove(task1.getId());
        Assertions.assertTrue(history1.contains(task2) && history1.contains(task3));
        final Task task4 = new Task(4, "Таск1", "Описание", Status.NEW, 0, null);
        manager.add(task4);
        List<Task> history2 = manager.getHistory();
        manager.remove(task3.getId());
        Assertions.assertTrue(history2.contains(task2) && history2.contains(task4));
        final Task task5 = new Task(5, "Таск1", "Описание", Status.NEW, 0, null);
        manager.add(task5);
        List<Task> history3 = manager.getHistory();
        manager.remove(task5.getId());
        Assertions.assertTrue(history2.contains(task2) && history2.contains(task4));
    }
}