package com.example.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.example.model.User;

public class DataBase {
    private static Map<String, User> users = Map.of();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
