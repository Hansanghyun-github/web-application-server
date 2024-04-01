package com.example.controller;

import com.example.request.HttpRequest;
import com.example.response.HttpResponse;

public abstract class Controller {
    public abstract void service(HttpRequest request, HttpResponse response);
}
