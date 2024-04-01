package com.example.controller;

import com.example.db.DataBase;
import com.example.request.HttpRequest;
import com.example.response.HttpResponse;
import com.example.response.StatusCode;
import com.example.util.HttpRequestUtils;

import java.util.stream.Collectors;

public class UserListController extends AbstractController{
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if(!isAuthenticated(request)){
            response.setStatusCode(StatusCode.Found);
            response.addHeader("Location", "/user/login.html");
            response.addHeader("Set-Cookie", "logined=false");
            return;
        }

        response.setStatusCode(StatusCode.OK);
        String body = DataBase.findAll()
                .stream()
                .map((u) -> u.toString() + "\r\n")
                .collect(Collectors.joining());
        response.setMessageBody(
                body);
        response.addHeader("content-length", "" + body.getBytes().length);
        response.addHeader("content-type", "text/html;charset=utf-8");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }

    private boolean isAuthenticated(HttpRequest request) {
        String header = request.findHeader("cookie");
        if(header == null)
            return false;

        String cookie = HttpRequestUtils.parseCookies(header).get("logined");
        return cookie != null && cookie.equals("true");
    }
}
