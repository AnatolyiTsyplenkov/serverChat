package auth;

import chat.User;

import java.util.List;

public class BaseAuthService implements AuthService {
    private static final List<User> users = List.of(
            new User("Ivanov Ivan", "ivanov", "1111"),
            new User("Petrov Petr", "petrov", "2222"),
            new User("Sidorov Oleg", "sidorov", "3333"));

    @Override
    public String getUserNameByLoginAndPass(String login, String password) {
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return user.getUserName();
            }
        }
        return null;
    }

    @Override
    public void startAuthentication() {
        System.out.println("start auth");
    }

    @Override
    public void endAuthentication() {
        System.out.println("end auth");

    }

    public List<User> getUsers() {
        return users;
    }

}
