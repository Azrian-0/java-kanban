package managers;

import managers.impl.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.nio.file.Path;
import java.util.List;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final String fileSaveClear = "src/test/resources/ClearTasks.csv";
    private final String fileEpicWithoutSub = "src/test/resources/EpicWithoutSub.csv";
    private final String fileEmptyHistory = "src/test/resources/EmptyHistory.csv";

    @Test
    public void loadFromFileClearTask() {
        manager = Managers.getFileBackedManager(fileSaveClear);
        final List<Task> tasksClear = manager.getAllTasks();
        Assertions.assertEquals(0, tasksClear.size(), "Список задач не пуст");
    }

    @Test
    public void loadFromFileEpicWithoutSubtasks() {
        manager = Managers.getFileBackedManager(fileEpicWithoutSub);
        final List<Epic> epics = manager.getAllEpicTasks();
        Assertions.assertEquals(1, epics.size(), "Список задач пуст");
    }

    @Test
    public void loadFromFileEmptyHistory() {
        manager = Managers.getFileBackedManager(fileEmptyHistory);
        Assertions.assertEquals(0, manager.getHistory().size(), "История задач не пуста");
        Assertions.assertEquals(0, manager.getAllTasks().size(), "Список задач не пуст");
        Assertions.assertEquals(0, manager.getAllSubTasks().size(), "Список задач не пуст");
        Assertions.assertEquals(1, manager.getAllEpicTasks().size(), "Список задач пуст");
    }
}