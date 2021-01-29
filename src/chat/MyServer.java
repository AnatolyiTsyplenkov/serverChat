package chat;

import auth.BaseAuthService;
import handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    private final ServerSocket serverSocket;
    private final BaseAuthService authService;

    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.authService = new BaseAuthService();
    }

    public void start() throws IOException {
        System.out.println("Server successful start");

            waitAndProcessNewClientConnection();

    }

    private void waitAndProcessNewClientConnection() throws IOException {
        System.out.println("Waiting user connect...");
        Socket socket = serverSocket.accept();
        System.out.println("Successful user connection");

        processClientConnection(socket);
    }

    private void processClientConnection(Socket socket) {
        ClientHandler clientHandler = new ClientHandler(this, socket);
        try {
            clientHandler.handle();

        } catch (IOException e) {
            System.out.println("Handle error start");
            e.printStackTrace();
        }
    }
}
