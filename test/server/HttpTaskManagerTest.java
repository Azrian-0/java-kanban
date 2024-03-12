package server;

import com.google.gson.*;
import enums.Status;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import util.GsonMappingConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpTaskManagerTest {
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    URI uri = URI.create("http://localhost:");
    static final int PORT = 8080;
    HttpResponse<String> response;
    private static Gson gson;
    private static HttpClient client;
    protected Task task1, task2;
    protected Epic epic1, epic2;
    protected SubTask subtask1, subtask2, subtask3;

    @BeforeEach
    void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
            client = HttpClient.newHttpClient();
            gson = GsonMappingConfig.getGson();
            {
                this.task1 = new Task("Задача1", "Задача1", Status.NEW, 0, null);
                this.task2 = new Task("Задача2", "Задача2", Status.NEW, 0, null);
                this.epic1 = new Epic("Эпик1", "Эпик1", Status.NEW, 0, null);
                this.epic2 = new Epic("Эпик2", "Эпик2", Status.NEW, 0, null);
                this.subtask1 = new SubTask("Подзадача1", "Первого эпика", Status.NEW, epic1, 0, null);
                this.subtask2 = new SubTask("Подзадача2", "Первого эпика", Status.NEW, epic1, 0, null);
                this.subtask3 = new SubTask("Подзадача3", "Второго эпика", Status.NEW, epic2, 0, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void stopServer() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void createTask() throws IOException, InterruptedException {
        response = postResponse("/tasks/task/", PORT, gson.toJson(task1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена " + response.statusCode());
        response = getResponse("/tasks/task/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Task receivedTask = gson.fromJson(firstObject, Task.class);
        Assertions.assertEquals(receivedTask.getId(), 1, "Подзадача не создана");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void createEpic() throws IOException, InterruptedException {
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Эпик не был добавлен" + response.statusCode());
        response = getResponse("/tasks/epic/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Epic receivedEpic = gson.fromJson(firstObject, Epic.class);
        Assertions.assertEquals(receivedEpic.getId(), 1, "Подзадача не создана");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void createSubTask() throws IOException, InterruptedException {
        createSubTasks();
        response = getResponse("/tasks/subtask/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        SubTask receivedSubtask = gson.fromJson(firstObject, SubTask.class);
        Assertions.assertEquals(receivedSubtask.getId(), 2, "Подзадача не создана");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        response = postResponse("/tasks/task/", PORT, gson.toJson(task1));
        response = postResponse("/tasks/task/", PORT, gson.toJson(task2));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        response = getResponse("/tasks/task/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        JsonObject secondObject = jsonArray.get(1).getAsJsonObject();
        Task receivedTask1 = gson.fromJson(firstObject, Task.class);
        Task receivedTask2 = gson.fromJson(secondObject, Task.class);
        task1.setId(1);
        task2.setId(2);
        Assertions.assertTrue(receivedTask1.equals(task1) && receivedTask2.equals(task2), "Ошибка получения всех задач");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic1));
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic2));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        response = getResponse("/tasks/epic/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        JsonObject secondObject = jsonArray.get(1).getAsJsonObject();
        Epic receivedTask1 = gson.fromJson(firstObject, Epic.class);
        Epic receivedTask2 = gson.fromJson(secondObject, Epic.class);
        epic1.setId(1);
        epic2.setId(2);
        Assertions.assertTrue(receivedTask1.equals(epic1) && receivedTask2.equals(epic2), "Ошибка получения всех задач");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getAllSubTasks() throws IOException, InterruptedException {
        createSubTasks();
        response = getResponse("/tasks/subtask/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        JsonObject secondObject = jsonArray.get(1).getAsJsonObject();
        SubTask receivedTask1 = gson.fromJson(firstObject, SubTask.class);
        SubTask receivedTask2 = gson.fromJson(secondObject, SubTask.class);
        subtask1.setId(2);
        subtask2.setId(3);
        Assertions.assertTrue(receivedTask1.equals(subtask1) && receivedTask2.equals(subtask2), "Ошибка получения всех задач");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        response = postResponse("/tasks/task/", PORT, gson.toJson(task1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена " + response.statusCode());
        response = getResponse("/tasks/task/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Task receivedTask = gson.fromJson(firstObject, Task.class);
        int taskId = firstObject.getAsJsonPrimitive("id").getAsInt();
        response = getResponse("/tasks/task/?id=" + taskId, PORT);
        task1.setId(1);
        Assertions.assertEquals(true, receivedTask.equals(task1), "Задача по id не получена");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Эпик не был добавлен" + response.statusCode());
        response = getResponse("/tasks/epic/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Epic receivedTask = gson.fromJson(firstObject, Epic.class);
        int taskId = firstObject.getAsJsonPrimitive("id").getAsInt();
        response = getResponse("/tasks/epic/?id=" + taskId, PORT);
        epic1.setId(1);
        Assertions.assertEquals(true, receivedTask.equals(epic1), "Задача по id не получена");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getEpicSubTasks() throws IOException, InterruptedException {
        createSubTasks();
        response = getResponse("/tasks/epic/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        int taskId = firstObject.getAsJsonPrimitive("id").getAsInt();
        response = getResponse("/tasks/subtask/epic/?id=" + taskId, PORT);
        JsonArray epicArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject epicObject = epicArray.get(0).getAsJsonObject();
        JsonArray subTasksArray = epicObject.getAsJsonArray("subTasks");
        List<SubTask> subTasksList = new ArrayList<>();
        for (JsonElement subTaskElement : subTasksArray) {
            SubTask subTask = gson.fromJson(subTaskElement, SubTask.class);
            subTasksList.add(subTask);
        }
        subtask1.setEpicId(1);
        subtask1.setId(2);
        Assertions.assertTrue(subTasksList.size() == 2 && subTasksList.contains(subtask1), "Подзадачи по epicId не получены");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getSubTaskById() throws IOException, InterruptedException {
        createSubTasks();
        response = getResponse("/tasks/subtask/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        SubTask receivedTask = gson.fromJson(firstObject, SubTask.class);
        int subTaskId = firstObject.getAsJsonPrimitive("id").getAsInt();
        response = getResponse("/tasks/subtask/?id=" + subTaskId, PORT);
        subtask1.setId(2);
        Assertions.assertTrue(receivedTask.equals(subtask1), "Задача по id не получена");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        postResponse("/tasks/task/", PORT, gson.toJson(task1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        response = getResponse("/tasks/task/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Task receivedTask = gson.fromJson(firstObject, Task.class);
        receivedTask.setStatus(Status.DONE);
        postResponse("/tasks/task/", PORT, gson.toJson(receivedTask));
        Assertions.assertEquals(true, receivedTask.getStatus().equals(Status.DONE), "Задача не обновлена");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        response = getResponse("/tasks/epic/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Task receivedTask = gson.fromJson(firstObject, Epic.class);
        receivedTask.setStatus(Status.DONE);
        postResponse("/tasks/epic/", PORT, gson.toJson(receivedTask));
        Assertions.assertEquals(true, receivedTask.getStatus().equals(Status.DONE), "Задача не обновлена");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void updateSubTask() throws IOException, InterruptedException {
        createSubTasks();
        response = getResponse("/tasks/subtask/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Task receivedTask = gson.fromJson(firstObject, SubTask.class);
        receivedTask.setStatus(Status.DONE);
        postResponse("/tasks/subtask/", PORT, gson.toJson(receivedTask));
        Assertions.assertEquals(true, receivedTask.getStatus().equals(Status.DONE), "Задача не обновлена");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        response = postResponse("/tasks/task/", PORT, gson.toJson(task1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        response = getResponse("/tasks/task/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        int taskId = firstObject.getAsJsonPrimitive("id").getAsInt();
        deleteResponse("/tasks/task/?id=" + taskId, PORT);
        response = getResponse("/tasks/task/?id=" + taskId, PORT);
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        response = getResponse("/tasks/epic/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        int taskId = firstObject.getAsJsonPrimitive("id").getAsInt();
        deleteResponse("/tasks/epic/?id=" + taskId, PORT);
        response = getResponse("/tasks/epic/?id=" + taskId, PORT);
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void deleteSubTaskById() throws IOException, InterruptedException {
        createSubTasks();
        response = getResponse("/tasks/subtask/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        int taskId = firstObject.getAsJsonPrimitive("id").getAsInt();
        deleteResponse("/tasks/subtask/?id=" + taskId, PORT);
        response = getResponse("/tasks/subtask/?id=" + taskId, PORT);
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        response = postResponse("/tasks/task/", PORT, gson.toJson(task1));
        response = postResponse("/tasks/task/", PORT, gson.toJson(task2));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        deleteResponse("/tasks/task/", PORT);
        response = getResponse("/tasks/task/", PORT);
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic1));
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic2));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        deleteResponse("/tasks/epic/", PORT);
        response = getResponse("/tasks/epic/", PORT);
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void deleteAllSubTasks() throws IOException, InterruptedException {
        createSubTasks();
        deleteResponse("/tasks/subtask/", PORT);
        response = getResponse("/tasks/subtask/", PORT);
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        response = postResponse("/tasks/task/", PORT, gson.toJson(task1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена " + response.statusCode());
        response = getResponse("/tasks/task/?id=1", PORT);
        response = getResponse("/tasks/history", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Task receivedSubtask = gson.fromJson(firstObject, Task.class);
        task1.setId(1);
        Assertions.assertTrue(receivedSubtask.equals(task1), "История не получена");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        response = postResponse("/tasks/task/", PORT, gson.toJson(task1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена " + response.statusCode());
        response = getResponse("/tasks/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        Task receivedSubtask = gson.fromJson(firstObject, Task.class);
        task1.setId(1);
        Assertions.assertTrue(receivedSubtask.equals(task1), "Приоритетные задачи не получены");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.statusCode());
    }

    private void createSubTasks() throws IOException, InterruptedException {
        response = postResponse("/tasks/epic/", PORT, gson.toJson(epic1));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Задача не была добавлена" + response.statusCode());
        response = getResponse("/tasks/epic/", PORT);
        String api = response.body();
        JsonArray jsonArray = JsonParser.parseString(api).getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        int epicId = firstObject.getAsJsonPrimitive("id").getAsInt();
        subtask1.setEpicId(epicId);
        subtask2.setEpicId(epicId);
        response = postResponse("/tasks/subtask/", PORT, gson.toJson(subtask1));
        response = postResponse("/tasks/subtask/", PORT, gson.toJson(subtask2));
        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new RuntimeException("Подзадача не была добавлена" + response.statusCode());
    }

    private HttpResponse<String> getResponse(String url, int port) throws IOException, InterruptedException {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(URI.create(uri + String.valueOf(port) + url))
                    .build();
        } catch (Exception e) {
            System.out.println("Ошибка создания GET запроса\nURI = " + URI.create(uri + String.valueOf(port) + url));
        }
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteResponse(String url, int port) throws IOException, InterruptedException {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .DELETE()
                    .version(HttpClient.Version.HTTP_1_1)
                    .uri(URI.create(uri + String.valueOf(port) + url))
                    .build();
        } catch (Exception e) {
            System.out.println("Ошибка создания DELETE запроса\nURI = " + URI.create(uri + String.valueOf(port) + url));
        }
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> postResponse(String url, int port, String json) throws IOException, InterruptedException {
        HttpRequest request = null;
        try {
            if (json.isEmpty()) throw new IOException("Пустое тело запроса");
            URI uri = URI.create("http://localhost:" + port);
            URI requestUri = uri.resolve(url);
            request = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Exception e) {
            System.out.println("Ошибка создания POST запроса\nURI = " + URI.create("http://localhost:" + String.valueOf(port) + url) + " "
                    + "Body = " + json);
        }
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}