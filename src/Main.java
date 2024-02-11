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

//        for (int i = 1; i <= 3; i++) {
//            Task task1 = manager.createTask(new Task("Задача " + i, "Описание задачи " + i, Status.NEW));
//            manager.getTaskById(task1.getId());
//        }
//
//        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "Первый эпик", Status.NEW));
//        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "Второй эпик", Status.NEW));
//
//        SubTask subTask1 = manager.createSubTask(new SubTask("Саб 1/1", "Первого эпика", Status.NEW, epic1));
//        SubTask subTask2 = manager.createSubTask(new SubTask("Саб 2/1", "Первого эпика", Status.DONE, epic1));
//        SubTask subTask3 = manager.createSubTask(new SubTask("Саб 3/1", "Первого эпика", Status.IN_PROGRESS, epic1));
//
//        SubTask subTask4 = manager.createSubTask(new SubTask("Саб 1/2", "Второго эпика", Status.DONE, epic2));
//
//        ArrayList<SubTask> epic1Sub = new ArrayList<>();
//        epic1Sub.add(subTask1);
//        epic1Sub.add(subTask2);
//        epic1Sub.add(subTask3);
//
//        ArrayList<SubTask> epic2Sub = new ArrayList<>();
//        epic2Sub.add(subTask4);
//
//        manager.getEpicById(epic2.getId(), true);
//        manager.getSubTaskById(subTask2.getId());
//
//        manager.save();

        manager.loadFromFile(filePath);

        System.out.println(manager.getAllSubTasks());

        System.out.println(manager.getHistory());

//        System.out.println(fileManager.getHistory());
    }
}