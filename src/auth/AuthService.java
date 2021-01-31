package auth;

public interface AuthService {
    String getUserNameByLoginAndPass(String login, String password);
    void startAuthentication();
    void endAuthentication();

}
