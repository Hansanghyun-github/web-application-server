package com.example.webserver;

import com.example.request.HttpRequest;
import com.example.response.HttpResponse;
import com.example.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DispatcherServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private DispatcherServlet() {
    }

    public static void frontController(HttpRequest request, HttpResponse response) throws IOException {
        String pathname = "webapp" + request.getPath();
        if(request.getPath().equals("/"))
            pathname += "index.html";
        String output = new String(
                Files.readAllBytes(Path.of(pathname)),
                StandardCharsets.UTF_8);
        response.setMessageBody(output);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.addHeader("Content-Length", "" + output.getBytes().length);
        response.setStatusCode(StatusCode.OK);
    }
}
