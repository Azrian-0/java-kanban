package managers.impl;

import enums.Status;
import exceptions.TaskValidationException;
import managers.Managers;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getInMemoryHistoryManager();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    private int nextId = 0;

    public int addId() {
        nextId++;
        return nextId;
    }

    @Override
    public Task createTask(Task task) {
        validateTask(task);
        task.setId(addId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        validateTask(task);
        if (!tasks.containsKey((task.getId()))) {
            throw new IllegalArgumentException("Невозможно изменить id задачи");
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.remove(task);
        prioritizedTasks.add(task);
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
        if (done == epic.getSubTasks().size() && !epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.DONE);
        } else if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        validateTask(subTask);
        subTask.setId(addId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpicById(subTask.getEpicId(), false);
        epic.setSubTask(subTask);
        updateStatusEpic(epic);
        updateTimesEpic(epic);
        prioritizedTasks.add(subTask);
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        validateTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        updateStatusEpic(epics.get(subTask.getEpicId()));
        Epic epic = epics.get(subTask.getEpicId());
        updateTimesEpic(epic);
        prioritizedTasks.remove(subTask);
        prioritizedTasks.add(subTask);
    }

    @Override
    public void deleteTaskById(int id) {
        Task taskToRemove = getTaskById(id);
        if (taskToRemove != null) {
            tasks.remove(id);
            prioritizedTasks.remove(taskToRemove);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask subTaskToDelete = subTasks.get(id);
        if (subTaskToDelete != null) {
            subTasks.remove(id);
            historyManager.remove(id);

            Epic epic = getEpicById(subTaskToDelete.getEpicId(), false);
            if (epic != null) {
                epic.getSubTasks().remove(subTaskToDelete);
                updateTimesEpic(epic);
                updateStatusEpic(epic);
            }

            SubTask subTaskToRemove = getSubTaskById(id);
            if (subTaskToRemove != null) {
                prioritizedTasks.remove(subTaskToRemove);
            }
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epicToDelete = epics.remove(id);
        if (!getAllSubTasks().isEmpty()) {
            for (SubTask subTask : epicToDelete.getSubTasks()) {
                subTasks.remove(subTask.getId());
                historyManager.remove(subTask.getId());
            }
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
    public void deleteAllTasks() {
        for (Integer idTask : tasks.keySet()) {
            historyManager.remove(idTask);
            Task task = tasks.get(idTask);
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer idEpic : epics.keySet()) {
            historyManager.remove(idEpic);
            if (!getAllSubTasks().isEmpty()) {
                for (SubTask idSubTask : epics.get(idEpic).getSubTasks()) {
                    historyManager.remove(idSubTask.getId());
                    prioritizedTasks.remove(subTasks.get(idSubTask.getId()));
                }
            }
            epics.clear();
            subTasks.clear();
        }
    }


    @Override
    public void deleteAllSubTasks() {
        for (Integer idSubTask : subTasks.keySet()) {
            historyManager.remove(idSubTask);
            prioritizedTasks.remove(subTasks.get(idSubTask));
        }
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
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void validateTask(Task task) {
        boolean result = getPrioritizedTasks().stream()
                .filter(t -> t.getStartTime() != null)
                .filter(t -> !t.equals(task))
                .allMatch(t -> t.getEndTime().isBefore(task.getStartTime()) || t.getStartTime().isAfter(task.getEndTime()) || t.getEndTime().equals(task.getStartTime()));
        if (!result) {
            throw new TaskValidationException("Задачи пересекаются по времени");
        }
    }

    private void updateTimesEpic(Epic epic) {
        List<SubTask> subTasksList = epic.getSubTasks();
        if (subTasksList.isEmpty()) {
            return;
        }
        LocalDateTime startTime = null;
        String startTimeString = null;
        LocalDateTime endTime = null;
        long duration = 0;

        for (SubTask subTask : subTasksList) {
            if (subTask.getDuration() == 0) {
                continue;
            }
            if (startTime == null || subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
                startTimeString = subTask.getStartTimeToString();
            }
            if (endTime == null || subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
            }
            duration = duration + subTask.getDuration();
        }
        if (startTime != null) {
            epic.createTime(duration, startTimeString);
            epic.setEndTime(endTime);
        }
    }
}