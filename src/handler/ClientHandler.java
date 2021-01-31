package handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import auth.AuthService;
import chat.MyServer;

public class ClientHandler {
    private final MyServer myServer;
    private final Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private String userName;

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

    public String getUserName() {
        return userName;
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

    private void readMessage() throws IOException {
        while (true) {
            //String message = in.readUTF();
            String message = "/w Привет приватное сообщение! Как дела?";//для отладки, убрать в боевой версии
            //String message = "/clientMsg Привет! Как дела?";//для отладки, убрать в боевой версии
            System.out.println(userName + " send message " + message);

            if (message.startsWith(END_MSG_CMD_PREFIX)) {
                break;
            } else if (message.startsWith(PRIVATE_MSG_CMD_PREFIX)) {
                String[] parts = message.split("\\s+", 3);
                String recipent = parts[1];
                String privateMessage = parts[2];
                myServer.sendPrivateMessage(this, recipent, privateMessage);

            } else {
                myServer.broadcastMessage(message, this);

            }
        }
    }

    private void authentication() throws IOException {
        while (true) {
            //String message = in.readUTF();
            String message = "/auth sidorov 3333";//для отладки, убрать в боевой версии

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

    private boolean processAuthCommand(String message) throws IOException {
        String[] parts = message.split("\\s+", 3);
        String login = parts[1];
        String password = parts[2];
        AuthService authService = myServer.getAuthService();
        userName = authService.getUserNameByLoginAndPass(login, password);
        if (userName != null) {

            if (myServer.isUserActive(userName)) {
                out.writeUTF(AUTHERR_CMD_PREFIX + " This user is already logged in");
                return false;
            }

            out.writeUTF(AUTHOK_CMD_PREFIX + " " + userName);
            myServer.subscribe(this);
            System.out.println("auth success");
            return true;

        } else {
            out.writeUTF(AUTHERR_CMD_PREFIX + " Login or password failed");
            return false;
        }
    }

    public void sendMessage(String userName, String message) throws IOException {
        out.writeUTF(String.format("%s %s %s", CLIENT_MSG_CMD_PREFIX, userName, message));
        System.out.println("message send");
    }
}
