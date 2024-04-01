package com.example.webserver;

import com.example.request.HttpRequest;
import com.example.request.HttpRequestFactory;
import com.example.response.HttpResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.HttpResponseFactory;
import support.StubSocket;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FileRequestHandlerTest {
    @Test
    @DisplayName("valid file test")
    void validFileTest() throws Exception {
        // given
        final String httpRequest= String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: close",
                "Accept: */* ",
                "");

        HttpRequest request = HttpRequestFactory.parse(
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(httpRequest.getBytes()))));
        HttpResponse response = new HttpResponse();

        // when
        FileRequestHandler.handle(request, response);

        // then
        String pathname = "webapp/index.html";
        String output = new String(
                Files.readAllBytes(Path.of(pathname)),
                StandardCharsets.UTF_8);

        assertThat(response.findHeader("content-length")).isEqualTo("" + output.getBytes().length);
        assertThat(response.findHeader("content-type")).isEqualTo("text/html;charset=utf-8");
        assertThat(response.getMessageBody()).isEqualTo(output);
    }
    @Test
    @DisplayName("invalid file test")
    void invalidFileTest() throws Exception {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /indexsdf.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: close",
                "Accept: */* ",
                "");

        HttpRequest request = HttpRequestFactory.parse(
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(httpRequest.getBytes()))));
        HttpResponse response = new HttpResponse();

        // when
        assertThatThrownBy(() -> FileRequestHandler.handle(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid path");
    }
}