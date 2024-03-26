package com.example.webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

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
            // TODO Exception을 Catch할 클래스(필터)가 필요하다.

            BufferedReader reader = getBufferedReader(in);
            HttpRequest request;
            try{
                request = HttpRequestFactory.parse(reader);
            } catch (IllegalArgumentException e){
                log.debug(e.getMessage());
                return;
            }
            HttpResponse response = new HttpResponse();
            response.setVersion(request.getVersion());

            log.debug("method: {} path: {}", request.getMethod(), request.getPath());

            DispatcherServlet.frontController(request, response);

            writeResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void writeResponse(OutputStream out, HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        writeHeaders(dos, response);
        if(hasMessageBody(response))
            writeMessageBody(dos, response.getMessageBody());
    }

    private boolean hasMessageBody(HttpResponse response) {
        return response.containsHeader("content-length");
    }

    private static BufferedReader getBufferedReader(InputStream in) {
        return new BufferedReader(new InputStreamReader(in));
    }

    private void writeHeaders(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n"); // TODO status 별로 write
            for(Map.Entry<String, String> e : response.getHeaders().entrySet()){
                dos.writeBytes(e.getKey() + ": " + e.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeMessageBody(DataOutputStream dos, String body) {
        try {
            dos.write(body.getBytes(), 0, body.getBytes().length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
