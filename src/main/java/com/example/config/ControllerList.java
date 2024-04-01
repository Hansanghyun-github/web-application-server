package com.example.config;

import com.example.controller.Controller;
import com.example.controller.UserCreateController;
import com.example.controller.UserListController;
import com.example.controller.UserLoginController;

import java.util.Map;

public class ControllerList {
    private static Map<String, Controller> controllers = Map.of(
            "/user/create", new UserCreateController(),
            "/user/login", new UserLoginController(),
            "/user/list", new UserListController()
    );

    private ControllerList() {
    }

    public static Controller getController(String requestUrl){
        return controllers.get(requestUrl);
    }
}
