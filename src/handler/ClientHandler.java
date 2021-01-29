package handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import chat.MyServer;

public class ClientHandler {
    private final MyServer myServer;
    private final Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public static final String AUTH_CMD_PREFIX = "/auth";
    public static final String AUTHOK_CMD_PREFIX = "/authok";
    public static final String AUTHERR_CMD_PREFIX = "/autherr";
    public static final String CLIENT_MSG_CMD_PREFIX = "/clientMsg";
    public static final String SERVER_MSG_CMD_PREFIX = "/serverMsg";
    public static final String PRIVATE_MSG_CMD_PREFIX = "/w";
    public static final String END_MSG_CMD_PREFIX = "/end";

    public ClientHandler(MyServer myServer, Socket socket) {
        this.myServer = myServer;
        this.clientSocket = socket;

    }

    public void handle() throws IOException {
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());

        new Thread(() -> {
            try {
                authentication();
                readMessage();
            } catch (IOException e) {
                System.out.println(e.getMessage());

            }
        }).start();

    }

    private void readMessage() {

    }

    private void authentication() throws IOException {

        while (true) {
            //String message = in.readUTF();
            String message = "/auth user1 1111";

            if (message.startsWith(AUTH_CMD_PREFIX)) {
                boolean isSuccesAuth = processAuthCommand(message);
                if (isSuccesAuth) {
                    break;
                }

            } else {
                out.writeUTF(AUTHERR_CMD_PREFIX + " Auth error");

            }

        }

    }

    private boolean processAuthCommand(String message) {
        String[] words = message.split("\\s+", 3);
        String login = words[1];
        String password = words[2];

        System.out.println(login + " " + password);

        return true;

    }
}
