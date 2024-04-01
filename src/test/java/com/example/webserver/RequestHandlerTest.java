package com.example.webserver;

import com.example.response.HttpResponse;
import com.example.response.StatusCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.HttpResponseFactory;
import support.StubSocket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class RequestHandlerTest {

    @Test
    @DisplayName("index.html 불러오기")
    void indexHtmlTest() throws Exception {
        // given
        final String httpRequest= String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: close",
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

        HttpResponse response = HttpResponseFactory.parse(socket.output());

        assertThat(response.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
        assertThat(response.findHeader("content-length")).isEqualTo("" + output.getBytes().length);
        assertThat(response.findHeader("content-type")).isEqualTo("text/html;charset=utf-8");
        assertThat(response.getMessageBody()).isEqualTo(output);
    }
}