package managers.impl;

import client.KVTaskClient;
import com.google.gson.Gson;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import util.GsonMappingConfig;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(URI uri) throws IOException, InterruptedException {
        client = new KVTaskClient(uri);
        gson = GsonMappingConfig.getGson();
//        load();
    }

//    @Override
//    protected void save() {
//        String jsonTasks = gson.toJson(tasks);
//        String jsonEpics = gson.toJson(epics);
//        String jsonSubTasks = gson.toJson(subTasks);
//        String jsonHistory = gson.toJson(getHistory());
//        String jsonPrioritizedTasks = gson.toJson(getPrioritizedTasks());
//        client.put("tasks", jsonTasks);
//        client.put("epics", jsonEpics);
//        client.put("subtasks", jsonSubTasks);
//        client.put("history", jsonHistory);
//        client.put("prioritizedTasks", jsonPrioritizedTasks);
//    }

//    public void load() {
//        try {
//            String taskFromJson = client.load("tasks");
//            if (taskFromJson != null && !taskFromJson.isBlank()) {
//                tasks = gson.fromJson(taskFromJson, HashMap.class);
//            }
//            String epicFromJson = client.load("epics");
//            if (epicFromJson != null && !epicFromJson.isBlank()) {
//                epics = gson.fromJson(taskFromJson, HashMap.class);
//            }
//            String subsFromJson = client.load("epics");
//            if (subsFromJson != null && !subsFromJson.isBlank()) {
//                subTasks = gson.fromJson(taskFromJson, HashMap.class);
//            }
//            String historyFromJson = client.load("history");
//            if (historyFromJson != null && !historyFromJson.isBlank()) {
//                List<Task> history = gson.fromJson(historyFromJson, List.class);
//                for (Task task : history) {
//                    historyManager.add(task);
//                }
//            }
//            String prioritizedTasksFromJson = client.load("prioritizedTasks");
//            if (prioritizedTasksFromJson != null && !prioritizedTasksFromJson.isBlank()) {
//                prioritizedTasks = gson.fromJson(prioritizedTasksFromJson, Set.class);
//            }
//        } catch (NullPointerException e) {
//            System.out.println("Пока что нечего загружать");
//        }
//    }

    @Override
    public String toString(Task task) {
        return super.toString(task);
    }

    @Override
    public FileBackedTasksManager loadFromFile(Path path) {
        return super.loadFromFile(path);
    }

    @Override
    public Task createTask(Task task) {
        task = super.createTask(task);
        client.put(String.valueOf(task.getId()), gson.toJson(task));
        return gson.fromJson(client.load(String.valueOf(task.getId())),Task.class);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public Epic createEpic(Epic epic) {
        return super.createEpic(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        return super.createSubTask(subTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        return super.getEpicSubTasks(epic);
    }

    @Override
    public Task getTaskById(Integer taskId) {
        return gson.fromJson(client.load(String.valueOf(taskId)),Task.class);
    }

    @Override
    public SubTask getSubTaskById(Integer taskId) {
        return super.getSubTaskById(taskId);
    }

    @Override
    public Epic getEpicById(Integer taskId, boolean addToHistory) {
        return super.getEpicById(taskId, addToHistory);
    }
}