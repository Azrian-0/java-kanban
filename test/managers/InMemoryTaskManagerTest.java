package managers;

import managers.impl.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.TasksForTest;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createInMemoryTasksManagerTest() {
        this.manager = new InMemoryTaskManager();
        this.tasksForTest = new TasksForTest(this.manager);
    }

    @Test
    public void createTask() {
        super.createTask();
    }

    @Test
    void updateTask() {
        super.updateTask();
    }

    @Test
    void createEpic() {
        super.createEpic();
    }

    @Test
    void updateEpic() {
        super.updateEpic();
    }

    @Test
    void updateStatusEpic() {
        super.updateStatusEpic();
    }

    @Test
    void createSubTask() {
        super.createSubTask();
    }

    @Test
    void updateSubTask() {
        super.updateSubTask();
    }

    @Test
    void deleteTaskById() {
        super.deleteTaskById();
    }

    @Test
    void deleteSubTaskById() {
        super.deleteSubTaskById();
    }

    @Test
    void deleteEpicById() {
        super.deleteEpicById();
    }

    @Test
    void getEpicSubTasks() {
        super.getEpicSubTasks();
    }

    @Test
    void getAllTasks() {
        super.getAllTasks();
    }

    @Test
    void getAllEpicTasks() {
        super.getAllEpicTasks();
    }

    @Test
    void getAllSubTasks() {
        super.getAllSubTasks();
    }

    @Test
    void getTaskById() {
        super.getTaskById();
    }

    @Test
    void getSubTaskById() {
        super.getSubTaskById();
    }

    @Test
    void getEpicById() {
        super.getEpicById();
    }

    @Test
    void getTasks() {
        super.getTasks();
    }

    @Test
    void getEpics() {
        super.getEpics();
    }

    @Test
    void getSubTasks() {
        super.getSubTasks();
    }

    @Test
    void getHistory() {
        super.getHistory();
    }
}