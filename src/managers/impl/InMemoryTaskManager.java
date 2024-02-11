package managers.impl;

import enums.Status;
import managers.Managers;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();

    protected HistoryManager historyManager = Managers.getDefaultHistory();

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
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask subTaskToDelete = subTasks.get(id);
        subTasks.remove(id);
        historyManager.remove(id);
        Epic epic = getEpicById(subTaskToDelete.getEpicId(), false);
        epic.getSubTasks().remove(subTaskToDelete);
        updateStatusEpic(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epicToDelete = epics.remove(id);
        for (SubTask subTask : epicToDelete.getSubTasks()) {
            subTasks.remove(subTask.getId());
            historyManager.remove(subTask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
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
        historyManager.clearHistory();
    }

    @Override
    public void deleteAllEpic() {
        for (Epic epic : epics.values()) {
            int idEpic = epic.getId();
            historyManager.remove(idEpic);
        }
        for (SubTask subTask : subTasks.values()) {
            int idSubtask = subTask.getId();
            historyManager.remove(idSubtask);
        }
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            int idEpic = epic.getId();
            historyManager.remove(idEpic);
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
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}