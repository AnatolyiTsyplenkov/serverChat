package chat;

public class User {
    private String userName;
    private String login;
    private String password;

    public User(String userName, String login, String password) {
        this.userName = userName;
        this.login = login;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
