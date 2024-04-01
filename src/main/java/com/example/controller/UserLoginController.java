package com.example.controller;

import com.example.db.DataBase;
import com.example.model.User;
import com.example.request.HttpRequest;
import com.example.response.HttpResponse;
import com.example.response.StatusCode;
import com.example.util.HttpRequestUtils;

import java.util.Map;

public class UserLoginController extends AbstractController{
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> query = HttpRequestUtils.parseQueryString(request.getMessageBody());
        if(loginSuccessed(query)){
            response.setStatusCode(StatusCode.Found);
            response.addHeader("Location", "/index.html");
            response.addHeader("Set-Cookie", "logined=true");
        }
        else{
            response.setStatusCode(StatusCode.Found);
            response.addHeader("Location", "/user/login_failed.html");
            response.addHeader("Set-Cookie", "logined=false");
        }
    }

    private boolean loginSuccessed(Map<String, String> query) {
        User user = DataBase.findUserById(query.get("userId"));
        return user != null
                && user
                .getPassword()
                .equals(query.get("password"));
    }
}
