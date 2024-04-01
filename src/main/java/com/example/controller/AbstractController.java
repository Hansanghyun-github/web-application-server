package com.example.controller;

import com.example.request.HttpMethod;
import com.example.request.HttpRequest;
import com.example.response.HttpResponse;

public abstract class AbstractController extends Controller{
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if(request.getMethod().equals(HttpMethod.GET))
            doGet(request, response);
        else
            doPost(request, response);
    }

    public abstract void doGet(HttpRequest request, HttpResponse response);

    public abstract void doPost(HttpRequest request, HttpResponse response);
}
