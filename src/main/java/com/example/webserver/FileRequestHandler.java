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

public class FileRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(FileRequestHandler.class);
    private FileRequestHandler() {
    }

    public static void handle(HttpRequest request, HttpResponse response) throws IOException {
        String output = getOutput(request);

        response.setMessageBody(output);
        addContentTypeHeader(response, request.getPath());
        response.addHeader("Content-Length", "" + output.getBytes().length);
        response.setStatusCode(StatusCode.OK);
    }

    private static String getOutput(HttpRequest request) throws IOException {
        String pathname = "webapp" + request.getPath();
        if(request.getPath().equals("/"))
            pathname += "index.html";
        String output = new String(
                Files.readAllBytes(Path.of(pathname)),
                StandardCharsets.UTF_8);
        return output;
    }

    private static void addContentTypeHeader(HttpResponse response, String pathname) {
        if(pathname.endsWith("js"))
            response.addHeader("Content-Type", "application/javascript");
        else if(pathname.endsWith("css"))
            response.addHeader("Content-Type", "text/css");
        else
            response.addHeader("Content-Type", "text/html;charset=utf-8");
    }
}
