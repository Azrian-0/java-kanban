package manager;

import enums.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();

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
        epics.put(epic.getId(), epic);
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(addId());
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        subTask.setId(addId());
        updateEpic(epics.get(subTask.getEpicId()));
    }

    public void deleteById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                if (entry.getValue().getEpicId() == id) {
                    subTasks.remove(entry.getKey());
                }
            }
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.remove(id);
        } else {
            System.out.println("Нет задачи с таким ID");
        }
    }

    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getAllEpicTasks() {
        return epics;
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
    }

    public Task getTaskById(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        } else {
            System.out.println("Нет задачи с таким ID");
            return null;
        }
    }

    public Task getSubTaskById(Integer taskId) {
        if (subTasks.containsKey(taskId)) {
            return subTasks.get(taskId);
        } else {
            System.out.println("Нет подзадачи с таким ID");
            return null;
        }
    }

    public Task getEpicById(Integer taskId) {
        if (epics.containsKey(taskId)) {
            return epics.get(taskId);
        } else {
            System.out.println("Нет эпика с таким ID");
            return null;
        }
    }
}