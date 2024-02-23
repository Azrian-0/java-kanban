package tests.managers;

import managers.impl.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.util.List;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final String fileSaveHistory = "BackedTasks.csv";
    private final String fileSaveClear = "ClearTasks.csv";
    private final String fileEpicWithoutSub = "EpicWithoutSub.csv";
    private final String fileEmptyHistory = "EmptyHistory.csv";

    @BeforeEach
    public void createInMemoryTasksManagerTest() {
        this.manager = new FileBackedTasksManager(fileSaveHistory);
    }

    @Test
    public void loadFromFileClearTask() {
        manager.loadFromFile(fileSaveClear);
        final List<Task> tasksClear = manager.getAllTasks();
        Assertions.assertEquals(0, tasksClear.size(), "Список задач не пуст");
    }

    @Test
    public void loadFromFileEpicWithoutSubtasks() {
        manager.loadFromFile(fileEpicWithoutSub);
        final List<Epic> epics = manager.getAllEpicTasks();
        Assertions.assertEquals(1, epics.size(), "Список задач пуст");
    }

    @Test
    public void loadFromFileEmptyHistory() {
        manager.loadFromFile(fileEmptyHistory);
        Assertions.assertEquals(0, manager.getHistory().size(), "История задач не пуста");
        Assertions.assertEquals(0, manager.getAllTasks().size(), "Список задач не пуст");
        Assertions.assertEquals(0, manager.getAllSubTasks().size(), "Список задач не пуст");
        Assertions.assertEquals(1, manager.getAllEpicTasks().size(), "Список задач пуст");
    }
}