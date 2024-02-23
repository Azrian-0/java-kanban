package tests.tasks;

import enums.Status;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class TasksForTest {

    private final TaskManager manager;

    public TasksForTest(TaskManager manager) {
        this.manager = manager;
    }

    public Epic epic1() {
        return manager.createEpic(new Epic("Эпик1", "Описание", Status.NEW, 0, null));
    }

    public Epic epic2() {
        return manager.createEpic(new Epic("Эпик2", "Описание", Status.NEW, 0, null));
    }

    public SubTask subTask1(Epic epic) {
        return manager.createSubTask(new SubTask("Подзадача1", "Описание", Status.NEW, epic, 0, null));
    }

    public SubTask subTask2(Epic epic) {
        return manager.createSubTask(new SubTask("Подзадача2", "Описание", Status.NEW, epic, 0, null));
    }

    public Task task1() {
        return manager.createTask(new Task("Задача1", "Описание", Status.NEW, 0, null));
    }

    public Task task2() {
        return manager.createTask(new Task("Задача2", "Описание", Status.NEW, 0, null));
    }
}