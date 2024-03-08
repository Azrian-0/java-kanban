package Server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.Managers;
import managers.impl.FileBackedTasksManager;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import util.GsonMappingConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {

    private HttpTaskServer taskServer;
    private final Gson gson = GsonMappingConfig.getGson();
    private TaskManager manager;

    @BeforeEach
    void init() throws Exception {
        manager = Managers.getFileBackedManager("src/BackedTasks.csv");
        taskServer = new HttpTaskServer();
        taskServer.start();
    }

    @Test
    void getAllTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}