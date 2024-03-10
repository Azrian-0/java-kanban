package managers;

import enums.Status;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.Assertions;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TasksForTest;

import java.util.HashMap;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private SubTask subTask1;
    private SubTask subTask2;

    protected TasksForTest tasksForTest;

    public void createTask() {
        task1 = tasksForTest.task1();
        final int taskId = task1.getId();

        final Task savedTask = manager.getTaskById(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getAllTasks();

        Assertions.assertNotNull(tasks, "Задачи на возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }


    void updateTask() {
        task1 = tasksForTest.task1();
        task1.setTaskName("Новое имя");
        task1.setDescription("Новое описание");
        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        Task updateTask = manager.getTaskById(task1.getId());
        Assertions.assertEquals("Новое описание", updateTask.getDescription(), "Не меняется описание.");
        Assertions.assertEquals("Новое имя", updateTask.getTaskName(), "Не меняется имя.");
        Assertions.assertEquals(Status.IN_PROGRESS, updateTask.getStatus(), "Не меняется статус.");

        final List<Task> tasks = manager.getAllTasks();

        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
    }


    void createEpic() {
        epic1 = tasksForTest.epic1();
        final int epicId = epic1.getId();

        final Epic savedEpic = manager.getEpicById(epicId, false);

        Assertions.assertNotNull(savedEpic, "Задача не найдена.");
        Assertions.assertEquals(epic1, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = manager.getAllEpicTasks();

        Assertions.assertNotNull(epics, "Задачи на возвращаются.");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач.");
        Assertions.assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    void updateEpic() {
        epic1 = tasksForTest.epic1();
        epic1.setTaskName("Новое имя");
        epic1.setDescription("Новое описание");
        epic1.setStatus(Status.IN_PROGRESS);
        manager.updateEpic(epic1);
        Epic updateEpic = manager.getEpicById(epic1.getId(), false);
        Assertions.assertEquals("Новое описание", updateEpic.getDescription(), "Не меняется описание.");
        Assertions.assertEquals("Новое имя", updateEpic.getTaskName(), "Не меняется имя.");
        Assertions.assertEquals(Status.IN_PROGRESS, updateEpic.getStatus(), "Не меняется статус.");

        final List<Epic> epics = manager.getAllEpicTasks();

        Assertions.assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    void updateStatusEpic() {
        epic1 = tasksForTest.epic1();
        subTask1 = tasksForTest.subTask1(epic1);
        subTask2 = tasksForTest.subTask1(epic1);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        manager.updateStatusEpic(epic1);
        Assertions.assertEquals(epic1.getStatus(), Status.DONE, "Не меняется статус эпика.");
        Assertions.assertEquals(subTask2.getStatus(), Status.DONE, "Не меняется статус подзадачи.");
    }

    void createSubTask() {
        epic1 = tasksForTest.epic1();
        Assertions.assertNotNull(epic1, "Не создан эпик");
        subTask1 = tasksForTest.subTask1(epic1);
        final int subTaskId = subTask1.getId();

        final SubTask savedSubTask = manager.getSubTaskById(subTaskId);

        Assertions.assertNotNull(savedSubTask, "Задача не найдена.");
        Assertions.assertEquals(subTask1, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = manager.getAllSubTasks();

        Assertions.assertNotNull(subTasks, "Задачи на возвращаются.");
        Assertions.assertEquals(1, subTasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(subTask1, subTasks.get(0), "Задачи не совпадают.");
    }

    void updateSubTask() {
        epic1 = tasksForTest.epic1();
        subTask1 = tasksForTest.subTask1(epic1);
        subTask1.setTaskName("Новое имя");
        subTask1.setDescription("Новое описание");
        subTask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask1);
        SubTask updateSub = manager.getSubTaskById(subTask1.getId());
        Assertions.assertEquals("Новое описание", updateSub.getDescription(), "Не меняется описание.");
        Assertions.assertEquals("Новое имя", updateSub.getTaskName(), "Не меняется имя.");
        Assertions.assertEquals(Status.IN_PROGRESS, updateSub.getStatus(), "Не меняется статус.");

        final List<SubTask> subTasks = manager.getAllSubTasks();

        Assertions.assertEquals(1, subTasks.size(), "Неверное количество задач.");
    }

    void deleteTaskById() {
        task1 = tasksForTest.task1();
        task2 = tasksForTest.task2();
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getHistory();
        manager.deleteTaskById(task1.getId());

        final List<Task> afterDeleted = manager.getAllTasks();
        final List<Task> afterDeletedHistory = manager.getHistory();
        final boolean deletedTask = afterDeleted.contains(task1);

        Assertions.assertFalse(deletedTask, "Задача не удалена из списка задач");
        Assertions.assertEquals(1, afterDeleted.size(), "Неверное количество задач в списке задач");
        Assertions.assertEquals(1, afterDeletedHistory.size(), "Неверное количество задач в истории просмотров");
    }

    void deleteSubTaskById() {
        epic1 = tasksForTest.epic1();
        subTask1 = tasksForTest.subTask1(epic1);
        subTask2 = tasksForTest.subTask2(epic1);
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getHistory();
        manager.deleteSubTaskById(subTask2.getId());

        final List<SubTask> afterDeleted = manager.getAllSubTasks();
        final List<Task> afterDeletedHistory = manager.getHistory();
        final boolean deletedTask = afterDeleted.contains(subTask2);

        Assertions.assertFalse(deletedTask, "Задача не удалена из списка задач");
        Assertions.assertEquals(1, afterDeleted.size(), "Неверное количество задач в списке задач");
        Assertions.assertEquals(1, afterDeletedHistory.size(), "Неверное количество задач в истории просмотров");
    }

    void deleteEpicById() {
        epic1 = tasksForTest.epic1();
        epic2 = tasksForTest.epic2();
        manager.getEpicById(epic1.getId(), true);
        manager.getEpicById(epic2.getId(), true);
        manager.getHistory();
        manager.deleteEpicById(epic2.getId());

        final List<Epic> afterDeleted = manager.getAllEpicTasks();
        final List<Task> afterDeletedHistory = manager.getHistory();
        final boolean deletedTask = afterDeleted.contains(epic2);

        Assertions.assertFalse(deletedTask, "Задача не удалена из списка задач");
        Assertions.assertEquals(1, afterDeleted.size(), "Неверное количество задач в списке задач");
        Assertions.assertEquals(1, afterDeletedHistory.size(), "Неверное количество задач в истории просмотров");
    }

    void getEpicSubTasks() {
        epic1 = tasksForTest.epic1();
        subTask1 = tasksForTest.subTask1(epic1);
        subTask2 = tasksForTest.subTask2(epic1);

        final List<SubTask> epicSubs = manager.getEpicSubTasks(epic1);

        Assertions.assertNotNull(epicSubs, "Список задач пуст");
        Assertions.assertEquals(2, epicSubs.size(), "Неверное количество задач");
    }

    void getAllTasks() {
        task1 = tasksForTest.task1();
        task2 = tasksForTest.task2();

        final List<Task> tasks = manager.getAllTasks();

        Assertions.assertNotNull(tasks, "Список задач пуст");
        Assertions.assertEquals(2, tasks.size(), "Неверное количество задач");
    }

    void getAllEpicTasks() {
        epic1 = tasksForTest.epic1();
        epic2 = tasksForTest.epic2();

        final List<Epic> epics = manager.getAllEpicTasks();

        Assertions.assertNotNull(epics, "Список задач пуст");
        Assertions.assertEquals(2, epics.size(), "Неверное количество задач");
    }

    void getAllSubTasks() {
        epic1 = tasksForTest.epic1();
        Assertions.assertNotNull(epic1, "Не создан эпик");
        subTask1 = tasksForTest.subTask1(epic1);
        subTask2 = tasksForTest.subTask2(epic1);

        final List<SubTask> subTasks = manager.getAllSubTasks();

        Assertions.assertNotNull(subTasks, "Список задач пуст");
        Assertions.assertEquals(2, subTasks.size(), "Неверное количество задач");
    }

    void getTaskById() {
        task1 = tasksForTest.task1();
        manager.getTaskById(task1.getId());
        manager.getHistory();

        final List<Task> tasks = manager.getAllTasks();
        final List<Task> tasksHistory = manager.getHistory();

        Assertions.assertNotNull(tasks, "Список задач пуст");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач в списке задач");
        Assertions.assertEquals(1, tasksHistory.size(), "Неверное количество задач в истории просмотров");
    }

    void getEpicById() {
        epic1 = tasksForTest.epic1();
        manager.getEpicById(epic1.getId(), true);
        manager.getHistory();

        final List<Epic> epics = manager.getAllEpicTasks();
        final List<Task> tasksHistory = manager.getHistory();

        Assertions.assertNotNull(epics, "Список задач пуст");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач в списке задач");
        Assertions.assertEquals(1, tasksHistory.size(), "Неверное количество задач в истории просмотров");
    }

    void getSubTaskById() {
        epic1 = tasksForTest.epic1();
        subTask1 = tasksForTest.subTask1(epic1);
        manager.getSubTaskById(subTask1.getId());
        manager.getHistory();

        final List<SubTask> subTasks = manager.getAllSubTasks();
        final List<Task> tasksHistory = manager.getHistory();

        Assertions.assertNotNull(subTasks, "Список задач пуст");
        Assertions.assertEquals(1, subTasks.size(), "Неверное количество задач в списке задач");
        Assertions.assertEquals(1, tasksHistory.size(), "Неверное количество задач в истории просмотров");
    }

    void getTasks() {
        task1 = tasksForTest.task1();
        task2 = tasksForTest.task2();

        final HashMap<Integer, Task> tasks = manager.getTasks();

        Assertions.assertNotNull(tasks, "Список задач пуст");
        Assertions.assertEquals(2, tasks.size(), "Неверное количество задач");
    }

    void getEpics() {
        epic1 = tasksForTest.epic1();
        epic2 = tasksForTest.epic2();

        HashMap<Integer, Epic> epics = manager.getEpics();
        Assertions.assertNotNull(epics, "Список задач пуст");
        Assertions.assertEquals(2, epics.size(), "Неверное количество задач");
    }

    void getSubTasks() {
        epic1 = tasksForTest.epic1();
        subTask1 = tasksForTest.subTask1(epic1);
        subTask2 = tasksForTest.subTask2(epic1);

        HashMap<Integer, SubTask> subs = manager.getSubTasks();
        Assertions.assertNotNull(subs, "Список задач пуст");
        Assertions.assertEquals(2, subs.size(), "Неверное количество задач");
    }

    void getHistory() {
        task1 = tasksForTest.task1();
        manager.getTaskById(task1.getId());

        final List<Task> taskHistory = manager.getHistory();

        Assertions.assertEquals(1, taskHistory.size(), "Неверный размер истории");
        Assertions.assertEquals(task1, taskHistory.get(0), "Задачи не совпадают");
    }
}