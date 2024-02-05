import enums.Status;
import managers.Managers;
import managers.interfaces.TaskManager;
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
        SubTask subTask2 = manager.createSubTask(new SubTask("Саб 2/1 ", "Первого эпика", Status.DONE, epic1));
        SubTask subTask3 = manager.createSubTask(new SubTask("Саб 3/1 ", "Первого эпика", Status.IN_PROGRESS, epic1));

        SubTask subTask4 = manager.createSubTask(new SubTask("Саб 1/2 ", "Второго эпика", Status.DONE, epic2));

        ArrayList<SubTask> epic1Sub = new ArrayList<>();
        epic1Sub.add(subTask1);
        epic1Sub.add(subTask2);
        epic1Sub.add(subTask3);

        ArrayList<SubTask> epic2Sub = new ArrayList<>();
        epic2Sub.add(subTask4);

        manager.getTaskById(1);
        manager.getTaskById(2);

        manager.deleteTaskById(1);
        manager.getTaskById(1);

        manager.getEpicById(3, true);
        manager.getEpicById(4, true);


        manager.getSubTaskById(5);
        manager.getSubTaskById(6);
        manager.getSubTaskById(7);

        manager.deleteSubTaskById(6);
        manager.getSubTaskById(6);

        manager.getSubTaskById(8);

        manager.deleteEpicById(4);

        System.out.println(manager.getHistory());
    }
}