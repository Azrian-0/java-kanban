package managers.impl;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import util.GsonMappingConfig;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson;

    public HttpTaskManager(URI uri) throws IOException, InterruptedException {
        super(Path.of("../BackedTasks.csv"));
        client = new KVTaskClient(uri);
        gson = GsonMappingConfig.getGson();
        load();
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(tasks);
        client.put("tasks", jsonTasks);

        String jsonEpics = gson.toJson(epics);
        client.put("epics", jsonEpics);

        String jsonSubtasks = gson.toJson(subTasks);
        client.put("subtasks", jsonSubtasks);

        String jsonHistory = gson.toJson(getHistory());
        client.put("history", jsonHistory);

        String jsonPrioritizedTasks = gson.toJson(getPrioritizedTasks());
        client.put("prioritizedTasks", jsonPrioritizedTasks);

    }

    private void load() {
        try {
            String taskFromJson = client.load("tasks");
            if (taskFromJson != null && !taskFromJson.isEmpty()) {
                tasks = gson.fromJson(taskFromJson, new TypeToken<HashMap<Integer, Task>>() {
                }.getType());
            }
            String epicFromJson = client.load("epics");
            if (epicFromJson != null && !epicFromJson.isEmpty()) {
                epics = gson.fromJson(epicFromJson, new TypeToken<HashMap<Integer, Epic>>() {
                }.getType());
            }
            String subsFromJson = client.load("subtasks");
            if (subsFromJson != null && !subsFromJson.isEmpty()) {
                subTasks = gson.fromJson(subsFromJson, new TypeToken<HashMap<Integer, SubTask>>() {
                }.getType());
            }
            String historyFromJson = client.load("history");
            if (historyFromJson != null && !historyFromJson.isEmpty()) {
                List<Task> history = gson.fromJson(historyFromJson, new TypeToken<List<Task>>() {
                }.getType());
                for (Task task : history) {
                    historyManager.add(task);
                }
            }
            String prioritizedTasksFromJson = client.load("prioritizedTasks");
            if (prioritizedTasksFromJson != null && !prioritizedTasksFromJson.isEmpty()) {
                prioritizedTasks = gson.fromJson(prioritizedTasksFromJson, new TypeToken<TreeSet<Task>>() {
                }.getType());
            }
        } catch (ManagerSaveException e) {
            System.out.println("Пока что нечего загружать");
        }
    }
}