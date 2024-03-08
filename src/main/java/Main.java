import Server.HttpTaskServer;
import Server.KVServer;
import managers.Managers;
import managers.impl.HttpTaskManager;
import managers.interfaces.TaskManager;


public class Main {
    public static void main(String[] args) throws Exception {

        new KVServer().start();
        new HttpTaskServer().start();

    }
}