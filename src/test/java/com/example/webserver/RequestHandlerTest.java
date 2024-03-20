package com.example.webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.StubSocket;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {
    @Test
    @DisplayName("First Test")
    void firstTest() throws Exception {
        // given
        var socket = new StubSocket();
        RequestHandler requestHandler = new RequestHandler(socket);

        // when
        requestHandler.process(socket);

        // then
        var expected = String.join("\r\n",
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8",
                "Content-Length: 11",
                "",
                "Hello World");

        assertThat(socket.output()).isEqualTo(expected);
    }

    @Test
    @DisplayName("index.html 불러오기")
    void indexHtmlTest() throws Exception {
        // given
        final String httpRequest= String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Accept: */* ",
                "");

        final var socket = new StubSocket(httpRequest);
        RequestHandler requestHandler = new RequestHandler(socket);

        // when
        requestHandler.process(socket);

        // then
        String pathname = "webapp/index.html";
        String output = new String(
                Files.readAllBytes(Path.of(pathname)),
                StandardCharsets.UTF_8);
        var expected = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: " + output.length() + "\r\n" +
                "\r\n"+
                output;

        assertThat(socket.output()).isEqualTo(expected);
    }
}