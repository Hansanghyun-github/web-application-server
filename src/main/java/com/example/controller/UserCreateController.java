package com.example.controller;

import com.example.db.DataBase;
import com.example.model.User;
import com.example.request.HttpRequest;
import com.example.response.HttpResponse;
import com.example.response.StatusCode;
import com.example.util.HttpRequestUtils;

import java.util.Map;

public class UserCreateController extends AbstractController{
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> query = HttpRequestUtils.parseQueryString(request.getMessageBody());
        User user = new User(query.get("userId"),
                query.get("password"),
                query.get("name"),
                query.get("email"));

        DataBase.addUser(user);
        response.setStatusCode(StatusCode.Found);
        response.addHeader("Location", "/index.html");
    }
}
