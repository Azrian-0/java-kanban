package tests.tasks;

import enums.Status;
import managers.Managers;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private TaskManager manager;
    private Epic epic;
    private SubTask subTask1;
    private SubTask subTask2;
    private TasksForTest tasksForTest;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        tasksForTest = new TasksForTest(manager);
        epic = tasksForTest.epic1();
        subTask1 = tasksForTest.subTask1(epic);
        subTask2 = tasksForTest.subTask2(epic);
    }

    @Test
    public void epicStatusWithEmptySubTasks() {
        manager.deleteSubTaskById(subTask1.getId());
        manager.deleteSubTaskById(subTask2.getId());
        boolean subTaskEmpty = epic.getSubTasks().isEmpty();
        assertTrue(subTaskEmpty, "У эпика есть подзадачи");
        Status epicStatus = epic.getStatus();
        Assertions.assertEquals(epicStatus, Status.NEW, "Неправильный статус");
    }

    @Test
    public void epicStatusWithDoneSubTasks() {
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        Assertions.assertEquals(epic.getStatus(), Status.DONE, "Неправильный статус");
    }

    @Test
    public void epicStatusWithNewSubTasks() {
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.NEW);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        Assertions.assertEquals(epic.getStatus(), Status.NEW, "Неправильный статус");
    }

    @Test
    public void epicStatusWithMixedSubTasks() {
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Неправильный статус");
    }

    @Test
    public void epicStatusWithProgressSubTasks() {
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Неправильный статус");
    }

    @Test
    public void epicTimeWithoutSubtasks() {
        LocalDateTime timeEpic = epic.getEndTime();
        Assertions.assertNull(timeEpic, "Неправильное время");
    }

    @Test
    public void epicTimeBasedOnSubtasks() {
        subTask1.createTime(15, "2024-02-21T20:00:00");
        manager.updateSubTask(subTask1);
        subTask2.createTime(15, "2024-02-21T20:00:00");
        manager.updateSubTask(subTask2);
        LocalDateTime timeTestStart = LocalDateTime.of(2024, 2, 21, 20, 00);
        LocalDateTime timeTestEnd = LocalDateTime.of(2024, 2, 21, 20, 30);
        long durationTest = 30;
        LocalDateTime timeStartOfEpic = epic.getStartTime();
        LocalDateTime timeEndOfEpic = epic.getEndTime();
        long durationEpic = epic.getDuration();
        Assertions.assertEquals(timeTestStart, timeStartOfEpic, "Время начала не совпадает");
        Assertions.assertEquals(timeTestEnd, timeEndOfEpic, "Время конца не совпадает");
        Assertions.assertEquals(durationTest, durationEpic, "Продолжительность не совпадает");
    }
}