package com.example.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestFactoryTest {
    @Test
    @DisplayName("HttpRequest Test")
    void httpRequestTest() throws Exception {
        // given
        var input = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Accept: */* ",
                "");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(input.getBytes())));

        // when
        HttpRequest request = HttpRequestFactory.parse(reader);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getQuery().size()).isZero();
        assertThat(request.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(request.getMessageBody()).isNull();

        Map<String, String> headers = request.getHeaders();
        assertThat(headers.size()).isEqualTo(3);
        assertThat(headers.get("host")).isEqualTo("localhost:8080");
        assertThat(headers.get("connection")).isEqualTo("keep-alive");
        assertThat(headers.get("accept")).isEqualTo("*/*");
    }

    @Test
    @DisplayName("HttpRequest Test with Message Body")
    void httpRequestTestWithMessageBody() throws Exception {
        // given
        var body = String.join("\r\n",
                "hello world",
                "hello world2");
        var input = String.join("\r\n",
                "POST /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: " + body.getBytes().length,
                "Accept: */* ",
                "",
                body);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(input.getBytes())));

        // when
        HttpRequest request = HttpRequestFactory.parse(reader);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getQuery().size()).isZero();
        assertThat(request.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(request.getMessageBody()).isEqualTo(body);

        Map<String, String> headers = request.getHeaders();
        assertThat(headers.size()).isEqualTo(4);
        assertThat(headers.get("host")).isEqualTo("localhost:8080");
        assertThat(headers.get("connection")).isEqualTo("keep-alive");
        assertThat(headers.get("content-length")).isEqualTo("" + body.getBytes().length);
        assertThat(headers.get("accept")).isEqualTo("*/*");
    }
}