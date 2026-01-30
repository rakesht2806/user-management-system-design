package main.java.com.usermanagement;

import java.util.ArrayList;
import java.util.List;

public class UserStore {

    private static final List<User> USERS = new ArrayList<>();

    static {
        USERS.add(new User(1, "Ram", "ram@gmail.com"));
        USERS.add(new User(1, "R", "R@gmail.com"));
        USERS.add(new User(1, "K", "K@gmail.com"));
    }

    public static List<User> getAllUsers() {
        return USERS;
    }

    public static User getUserById(int id) {
        for (User user : USERS) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
}
