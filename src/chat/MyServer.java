package chat;

import auth.BaseAuthService;
import handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final ServerSocket serverSocket;
    private final BaseAuthService authService;
    private final List<ClientHandler> users = new ArrayList<>();

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

    public BaseAuthService getAuthService() {
        return authService;
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        users.add(clientHandler);
        System.out.println("user " + clientHandler.getUserName() + " add in users list");
    }

    public synchronized void unSubscribe(ClientHandler clientHandler) {
        users.remove(clientHandler);
    }

    public synchronized boolean isUserActive(String username) {
        for (ClientHandler user : users) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler user : users) {
            if (user == sender) {
                continue;
            }
            user.sendMessage(sender.getUserName(), message);
            System.out.println("message broadcast");
        }
    }

    public synchronized void sendPrivateMessage(ClientHandler sender, String recipent, String privateMessage) throws IOException {
        for (ClientHandler user : users) {
            if (user.getUserName().equals(recipent)) {
                user.sendMessage(sender.getUserName(), privateMessage);
                System.out.println("private message prepared");
            }
        }
    }
}
