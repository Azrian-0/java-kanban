import Server.KVServer;

import java.io.IOException;

public class KVServerStarter {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}
