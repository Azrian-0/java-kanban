package Server;

import com.google.gson.Gson;
import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tasks.Task;
import util.GsonMappingConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpTaskServerTest {
    private static KVServer kvServer;
    private static HttpTaskServer taskServer;
    private static Gson gson = new GsonMappingConfig().getGson();
    private static final String TASK_BASE_URL = "http://localhost:8080/tasks/task/";
    private static final String EPIC_BASE_URL = "http://localhost:8080/tasks/epic/";
    private static final String SUBTASK_BASE_URL = "http://localhost:8080/tasks/subtask/";

    protected Task task = new Task("1", "2", Status.NEW, 15, "2024-02-26T18:55:00");

    @BeforeAll
    static void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskServer = new HttpTaskServer();
            taskServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createTask() throws IOException, InterruptedException {
        postResponse("/tasks/task/", 8080, gson.toJson(task));
    }
    private HttpResponse<String> postResponse(String url, int port, String json) throws IOException, InterruptedException {
        HttpRequest request = null;
        try {
            if (json.isEmpty()) throw new IOException("Пустое тело запроса");

            URI baseUri = URI.create("http://localhost:" + String.valueOf(port));
            URI requestUri = baseUri.resolve(url);

            request = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Exception e) {
            System.out.println("Что-то не так при создании запроса у метода POST\nURI = " + URI.create("http://localhost:" + String.valueOf(port) + url) + " "
                    + "Body = " + json);
        }
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

}