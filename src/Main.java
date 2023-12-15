import enums.Status;
import manager.Managers;
import manager.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 = manager.createTask(new Task("Задача 1", "1", Status.NEW));
        Task task2 = manager.createTask(new Task("Задача 2", "2", Status.NEW));

        Epic epic1 = manager.createEpic(new Epic("Эпик 1  ", "Первый эпик  ", Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Эпик 2  ", "Второй эпик  ", Status.NEW));

        SubTask subTask1 = manager.createSubTask(new SubTask("Саб 1/1 ", "Первого эпика", Status.NEW, epic1));
        SubTask subTask2 = manager.createSubTask(new SubTask("Саб 2/1 ", "Первого эпика", Status.NEW, epic1));
        ArrayList<SubTask> epic1Sub = new ArrayList<>();
        epic1Sub.add(subTask1);
        epic1Sub.add(subTask2);

        SubTask subTask3 = manager.createSubTask(new SubTask("Саб 1/2 ", "Второго эпика", Status.NEW, epic2));
        ArrayList<SubTask> epic2Sub = new ArrayList<>();
        epic2Sub.add(subTask3);

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3, true);
        manager.getEpicById(4, true);
        manager.getSubTaskById(5);
        manager.getSubTaskById(6);

        manager.getTaskById(2);
        manager.getEpicById(3, true);
        manager.getSubTaskById(6);

        System.out.println(manager.getHistory());
    }
}