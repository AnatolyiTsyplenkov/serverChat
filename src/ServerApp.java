import chat.MyServer;

import java.io.IOException;

public class ServerApp {
    public static final int DEAFAULT_PORT = 8888;

    public static void main(String[] args) {

        int port = DEAFAULT_PORT;

        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
        }

        try {

            new MyServer(port).start();

        } catch (IOException e) {
            System.out.printf("Server start error");
            e.printStackTrace();
        }

    }
}
