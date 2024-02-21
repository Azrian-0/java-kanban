package managers.interfaces;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    Task createTask(Task task);

    void updateTask(Task task);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    void updateStatusEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void deleteTaskById(int id);

    void deleteSubTaskById(int id);

    void deleteEpicById(int id);

    ArrayList<SubTask> getEpicSubTasks(Epic epic);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpicTasks();

    ArrayList<SubTask> getAllSubTasks();

    Task getTaskById(Integer taskId);

    Epic getEpicById(Integer taskId, boolean addToHistory);

    SubTask getSubTaskById(Integer taskId);

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, SubTask> getSubTasks();

    ArrayList<Task> getHistory();

    List<Task> getPrioritizedTasks();
}