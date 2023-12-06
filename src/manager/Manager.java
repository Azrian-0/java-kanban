package manager;

import enums.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int nextId = 0;

    public int addId() {
        nextId++;
        return nextId;
    }

    public Task createTask(Task task) {
        task.setId(addId());
        tasks.put(task.getId(), task);
        return task;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(addId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateStatusEpic(Epic epic) { // ?
        int done = 0;
        for (SubTask subTask : epic.getSubTasks()) {
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            } else if (subTask.getStatus().equals(Status.DONE)) {
                epic.setStatus(Status.IN_PROGRESS);
                done++;
            }
        }
        if (done == epic.getSubTasks().size()) {
            epic.setStatus(Status.DONE);
        }
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(addId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpicById(subTask.getEpicId());
        epic.setSubTasks(subTask);
        updateStatusEpic(epic);
        return subTask;
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateStatusEpic(epics.get(subTask.getEpicId()));
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubTaskById(int id) {
        SubTask subTaskToDelete = subTasks.get(id);
        subTasks.remove(id);
        Epic epic = getEpicById(subTaskToDelete.getEpicId());
        epic.getSubTasks().remove(subTaskToDelete);
        updateStatusEpic(epic);
    }

    public void deleteEpicById(int id) {
        Epic epicToDelete = epics.remove(id);
        for (SubTask subTask : epicToDelete.getSubTasks()){
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteAllEpic() {
        subTasks.clear();
        epics.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateStatusEpic(epic);
        }
    }

    public Task getTaskById(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        } else {
            System.out.println("Нет задачи с таким ID");
            return null;
        }
    }

    public SubTask getSubTaskById(Integer taskId) {
        if (subTasks.containsKey(taskId)) {
            return subTasks.get(taskId);
        } else {
            System.out.println("Нет подзадачи с таким ID");
            return null;
        }
    }

    public Epic getEpicById(Integer taskId) {
        if (epics.containsKey(taskId)) {
            return epics.get(taskId);
        } else {
            System.out.println("Нет эпика с таким ID");
            return null;
        }
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
}