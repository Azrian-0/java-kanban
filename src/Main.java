import enums.Status;
import manager.Manager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

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

        for (Map.Entry<Integer, Task> entry : manager.getTasks().entrySet()) {
            System.out.println(entry.getValue());
        }
        for (Map.Entry<Integer, Epic> entry : manager.getEpics().entrySet()) {
            System.out.println(entry.getValue());
        }
        for (Map.Entry<Integer, SubTask> entry : manager.getSubTasks().entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println("_________________________________________________________________________________________");

        task1.setStatus(Status.DONE);
        manager.updateTask(task1);
        task2.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task2);
        subTask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask1);
        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(subTask2);
        subTask3.setStatus(Status.DONE);
        manager.updateSubTask(subTask3);

        for (Map.Entry<Integer, Task> entry : manager.getTasks().entrySet()) {
            System.out.println(entry.getValue());
        }
        for (Map.Entry<Integer, Epic> entry : manager.getEpics().entrySet()) {
            System.out.println(entry.getValue());
        }
        for (Map.Entry<Integer, SubTask> entry : manager.getSubTasks().entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println("_________________________________________________________________________________________");

        manager.deleteTaskById(1);
        manager.deleteSubTaskById(5);
        manager.deleteEpicById(4);

        for (Map.Entry<Integer, Task> entry : manager.getTasks().entrySet()) {
            System.out.println(entry.getValue());
        }
        for (Map.Entry<Integer, Epic> entry : manager.getEpics().entrySet()) {
            System.out.println(entry.getValue());
        }
        for (Map.Entry<Integer, SubTask> entry : manager.getSubTasks().entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println("_________________________________________________________________________________________");
    }
}