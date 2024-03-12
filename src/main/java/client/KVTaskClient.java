package client;

import exceptions.HttpRequestException;
import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final URI uri;
    private final String apiToken;

    String json = "";

    public KVTaskClient(URI uri) throws IOException, InterruptedException {
        this.uri = uri;
        this.client = HttpClient.newHttpClient();
        HttpRequest registration = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri + "/register"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        apiToken = client.send(registration, HttpResponse.BodyHandlers.ofString()).body();
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(uri + "/save/" + key + "?API_TOKEN=" + apiToken))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("HTTP Status Code: " + response.statusCode());

            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new HttpRequestException("HTTP request failed with status code: " + response.statusCode());
            }

        } catch (IOException | InterruptedException exception) {
            throw new HttpRequestException("Error while sending HTTP request");
        }
    }

    public String load(String key) {
        String body = null;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri + "/load/" + key + "?API_TOKEN=" + apiToken))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new ManagerSaveException("Can't do save request, status code: " + response.statusCode());
            }
            body = response.body();
        } catch (IOException | InterruptedException exception) {
            throw new HttpRequestException("Error while sending HTTP request");
        }
        return body;
    }

    private HttpResponse<String> getResponse(String method, String url) throws IOException, InterruptedException {
        HttpRequest request = null;
        switch (method) {
            case "GET":
                try {
                    request = HttpRequest.newBuilder()
                            .GET()
                            .version(HttpClient.Version.HTTP_1_1)
                            .uri(URI.create(uri + url))
                            .build();
                } catch (Exception e) {
                    System.out.println("Что-то не так при создании запроса у метода GET\nURI = " + URI.create(uri + url));
                }
                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            case "POST":
                try {
                    if (json.isEmpty()) throw new IOException("Пустое тело запроса");
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(uri + url))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .version(HttpClient.Version.HTTP_1_1)
                            .header("Content-Type", "application/json")
                            .build();
                } catch (Exception e) {
                    System.out.println("Что-то не так при создании запроса у метода POST\nURI = " + URI.create(uri + url) + " "
                            + "Body = " + json);
                }
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return response;
            default:
                System.out.println("Такого метода нет в клиенте");
                return null;
        }
    }
}