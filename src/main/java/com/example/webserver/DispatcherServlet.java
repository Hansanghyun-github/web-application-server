package com.example.webserver;

import com.example.request.HttpRequest;
import com.example.response.HttpResponse;
import com.example.response.StatusCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DispatcherServlet {
    private DispatcherServlet() {
    }

    public static void frontController(HttpRequest request, HttpResponse response) throws IOException {
        if(request.getPath().equals("/index.html") == false)
            throw new IllegalArgumentException("Not Supported Path");

        String pathname = "webapp/index.html";
        String output = new String(
                Files.readAllBytes(Path.of(pathname)),
                StandardCharsets.UTF_8);
        response.setMessageBody(output);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.addHeader("Content-Length", ""+output.length());
        response.setStatusCode(StatusCode.OK);
    }
}
