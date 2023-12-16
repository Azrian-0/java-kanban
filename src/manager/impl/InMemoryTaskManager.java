package manager.impl;

import enums.Status;
import manager.Managers;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();

    private int nextId = 0;

    public int addId() {
        nextId++;
        return nextId;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(addId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(addId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateStatusEpic(Epic epic) {
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

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(addId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpicById(subTask.getEpicId(), false);
        epic.setSubTasks(subTask);
        updateStatusEpic(epic);
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateStatusEpic(epics.get(subTask.getEpicId()));
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask subTaskToDelete = subTasks.get(id);
        subTasks.remove(id);
        Epic epic = getEpicById(subTaskToDelete.getEpicId(), false);
        epic.getSubTasks().remove(subTaskToDelete);
        updateStatusEpic(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epicToDelete = epics.remove(id);
        for (SubTask subTask : epicToDelete.getSubTasks()) {
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateStatusEpic(epic);
        }
    }

    @Override
    public Task getTaskById(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            Task task = tasks.get(taskId);
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(Integer taskId) {
        if (subTasks.containsKey(taskId)) {
            SubTask subTask = subTasks.get(taskId);
            historyManager.add(subTask);
            return subTask;
        }
        return null;
    }

    @Override
    public Epic getEpicById(Integer taskId, boolean addToHistory) {
        if (epics.containsKey(taskId)) {
            Epic epic = epics.get(taskId);
            if (addToHistory) {
                historyManager.add(epic);
            }
            return epic;
        }
        return null;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public Set<Task> getHistory() {
        return historyManager.getHistory();
    }
}