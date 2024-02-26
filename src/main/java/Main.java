import enums.Status;
import managers.Managers;
import managers.impl.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {

        String filePath = "src/BackedTasks.csv";

        FileBackedTasksManager manager = Managers.getFileBackedManager(filePath);
        Task task1 = manager.createTask(new Task("Таск1", "описание", Status.NEW, 5, "2024-02-26T18:55:00"));
        Task task2 = manager.createTask(new Task("Таск2", "описание", Status.DONE, 5, "2024-02-26T19:00:00"));
        Epic epic1 = manager.createEpic(new Epic("Эпик1", "описание", Status.NEW, 30, "2024-02-27T12:30:00"));
        Epic epic2 = manager.createEpic(new Epic("Эпик2", "описание", Status.NEW, 15, "2024-02-27T13:00:00"));
        SubTask subTask1 = manager.createSubTask(new SubTask("Саб1", "1 эпик", Status.DONE,epic1, 15, "2024-02-27T12:30:00"));
        SubTask subTask2 = manager.createSubTask(new SubTask("Саб2", "2 эпик", Status.DONE,epic2, 15, "2024-02-27T12:45:00"));
        SubTask subTask3 = manager.createSubTask(new SubTask("Саб3", "2 эпик", Status.IN_PROGRESS,epic2, 15, "2024-02-27T13:00:00"));
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId(), true);
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(subTask3.getId());

        manager.save();

        System.out.println(manager.getHistory());
    }
}