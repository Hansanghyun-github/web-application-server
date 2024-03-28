package com.example.webserver;

import com.example.request.HttpMethod;
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

    public static void frontController(HttpRequest request, HttpResponse response) {
        // TODO http method 별로 처리해줘야 한다

    }
}
