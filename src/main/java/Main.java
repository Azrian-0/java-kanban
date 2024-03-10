import Server.HttpTaskServer;
import Server.KVServer;

public class Main {
    public static void main(String[] args) throws Exception {

        new KVServer().start();
        new HttpTaskServer().start();

    }
}