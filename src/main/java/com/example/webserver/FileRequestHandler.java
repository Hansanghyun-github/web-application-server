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
        Path path = getPath(request);
        if(Files.exists(path) == false)
            throw new IllegalArgumentException("Invalid path");

        String contents = getFileContents(path);

        response.setMessageBody(contents);
        addContentTypeHeader(response, request.getPath());
        response.addHeader("Content-Length", "" + contents.getBytes().length);
        response.setStatusCode(StatusCode.OK);
    }

    private static String getFileContents(Path path) throws IOException {
        String output = new String(
                Files.readAllBytes(path),
                StandardCharsets.UTF_8);
        return output;
    }

    private static Path getPath(HttpRequest request) {
        String pathname = "webapp" + request.getPath();
        if(request.getPath().equals("/"))
            pathname += "index.html";
        return Path.of(pathname);
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
