package com.example.webserver;

import java.io.*;
import java.net.Socket;

import com.example.request.HttpRequest;
import com.example.request.HttpRequestFactory;
import com.example.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        process(connection);
    }

    public void process(final Socket connection) {
        try (final var in = connection.getInputStream();
             final var out = connection.getOutputStream()) {
            BufferedReader reader = getBufferedReader(in);

            HttpRequest request = HttpRequestFactory.parse(reader);
            HttpResponse response = new HttpResponse(request.getVersion());

            DispatcherServlet.frontController(request, response);

            BufferedWriter writer = getBufferedWriter(out);
            response.writeTo(writer);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static BufferedWriter getBufferedWriter(OutputStream out) {
        return new BufferedWriter(new OutputStreamWriter(out));
    }

    private static BufferedReader getBufferedReader(InputStream in) {
        return new BufferedReader(new InputStreamReader(in));
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
