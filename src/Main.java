import enums.Status;
import managers.Managers;
import managers.impl.FileBackedTasksManager;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String filePath = "BackedTasks.csv";

        FileBackedTasksManager manager = Managers.getFileBackedManager(filePath);
        Epic epic = manager.createEpic(new Epic("Эпик","описание",Status.NEW,15,"2024-02-21T20:30:00"));
        manager.getEpicById(epic.getId(),true);

        manager.save();

        System.out.println(manager.getHistory());
    }
}