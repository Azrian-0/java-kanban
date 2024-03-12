package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import util.GsonMappingConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private TaskManager manager;

    public HttpTaskServer() throws Exception {
        manager = Managers.getDefault();
        gson = GsonMappingConfig.getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task/", this::handleTaskRequest);
        server.createContext("/tasks/epic/", this::handleEpicRequest);
        server.createContext("/tasks/subtask/", this::handleSubTaskRequest);
        server.createContext("/tasks/subtask/epic/", this::handleSubtaskByEpicId);
        server.createContext("/tasks/history", this::handleHistory);
        server.createContext("/tasks/", this::handlePrioritizedTasks);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(HttpURLConnection.HTTP_OK, resp.length);
        h.getResponseBody().write(resp);
    }

    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            switch (requestMethod) {
                case "GET": {
                    if (query != null) {
                        String response = gson.toJson(manager.getTaskById(Integer.valueOf(getQueryId(exchange))));
                        sendText(exchange, response);
                        break;
                    }
                    String response = gson.toJson(manager.getAllTasks());
                    sendText(exchange, response);
                    break;
                }
                case "POST": {
                    String requestBody = readText(exchange);
                    Task task = gson.fromJson(requestBody, Task.class);
                    if (task.getId() == 0) {
                        String response = gson.toJson(manager.createTask(gson.fromJson(requestBody, Task.class)));
                        sendText(exchange, response);
                        break;
                    } else {
                        manager.updateTask(gson.fromJson(requestBody, Task.class));
                        sendText(exchange, "Задача обновлена");
                        break;
                    }
                }
                case "DELETE": {
                    if (query != null) {
                        manager.deleteTaskById(Integer.valueOf(getQueryId(exchange)));
                        String response = gson.toJson("Удалено");
                        sendText(exchange, response);
                        break;
                    }
                    manager.deleteAllTasks();
                    String response = gson.toJson("Удалено");
                    sendText(exchange, response);
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleEpicRequest(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            switch (requestMethod) {
                case "GET": {
                    if (query != null) {
                        String response = gson.toJson(manager.getEpicById(Integer.valueOf(getQueryId(exchange)), true));
                        sendText(exchange, response);
                        break;
                    }
                    String response = gson.toJson(manager.getAllEpicTasks());
                    sendText(exchange, response);
                    break;
                }
                case "POST": {
                    String requestBody = readText(exchange);
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    if (epic.getId() == 0) {
                        String response = gson.toJson(manager.createEpic(gson.fromJson(requestBody, Epic.class)));
                        sendText(exchange, response);
                        break;
                    } else {
                        manager.updateEpic(gson.fromJson(requestBody, Epic.class));
                        sendText(exchange, "Эпик обновлен");
                        break;
                    }
                }
                case "DELETE": {
                    if (query != null) {
                        manager.deleteEpicById(Integer.valueOf(getQueryId(exchange)));
                        String response = gson.toJson("Удалено");
                        sendText(exchange, response);
                        break;
                    }
                    manager.deleteAllEpics();
                    String response = gson.toJson("Удалено");
                    sendText(exchange, response);
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleSubTaskRequest(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            switch (requestMethod) {
                case "GET": {
                    if (query != null) {
                        String response = gson.toJson(manager.getSubTaskById(Integer.valueOf(getQueryId(exchange))));
                        sendText(exchange, response);
                        break;
                    }
                    String response = gson.toJson(manager.getAllSubTasks());
                    sendText(exchange, response);
                    break;
                }
                case "POST": {
                    String requestBody = readText(exchange);
                    SubTask subTask = gson.fromJson(requestBody, SubTask.class);
                    if (subTask.getId() == 0) {
                        String response = gson.toJson(manager.createSubTask(gson.fromJson(requestBody, SubTask.class)));
                        sendText(exchange, response);
                        break;
                    } else {
                        manager.updateSubTask(gson.fromJson(requestBody, SubTask.class));
                        sendText(exchange, "Подзадача обновлена");
                        break;
                    }
                }
                case "DELETE": {
                    if (query != null) {
                        manager.deleteSubTaskById(Integer.valueOf(getQueryId(exchange)));
                        String response = gson.toJson("Удалено");
                        sendText(exchange, response);
                        break;
                    }
                    manager.deleteAllSubTasks();
                    String response = gson.toJson("Удалено");
                    sendText(exchange, response);
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleSubtaskByEpicId(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            if (query != null) {
                if (requestMethod.equals("GET")) {
                    Epic epic = manager.getEpicById(Integer.valueOf(getQueryId(exchange)), false);
                    String response = gson.toJson(manager.getEpicSubTasks(epic));
                    sendText(exchange, response);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleHistory(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equals("GET")) {
                String response = gson.toJson(manager.getHistory());
                sendText(exchange, response);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handlePrioritizedTasks(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equals("GET")) {
                String response = gson.toJson(manager.getPrioritizedTasks());
                sendText(exchange, response);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    protected String getQueryId(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            return Pattern.compile("\\s&\\s")
                    .splitAsStream(exchange.getRequestURI().getQuery().trim())
                    .map(s -> s.split("=", 2))
                    .collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? a[1] : "")).get("id");
        }
        return null;
    }
}