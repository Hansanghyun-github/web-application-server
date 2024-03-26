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
    private boolean close;
    private final int TIMEOUT;


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        close = false;
        TIMEOUT = 20 * 1000;
    }

    @Override
    public void run() {
        log.debug("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    public void process(final Socket connection) {
        try (final var in = connection.getInputStream();
             final var out = connection.getOutputStream()) {
            connection.setSoTimeout(TIMEOUT);
            handleRequest(in, out);
        } catch (IOException | IllegalArgumentException e){
            log.warn(e.getMessage());
            // TODO error 메시지 보내줘야 함
            // TODO 더 뒷 단에서 에러 처리하는게 어떨까
        }

        close(connection);
    }

    private void handleRequest(InputStream in, OutputStream out) throws IOException {
        BufferedReader reader = getBufferedReader(in);
        DataOutputStream dos = new DataOutputStream(out);

        while (!close){
            HttpRequest request = HttpRequestFactory.parse(reader);
            checkConnection(request);
            HttpResponse response = new HttpResponse();
            response.setVersion(request.getVersion());
            log.debug("method: {} path: {}", request.getMethod(), request.getPath());
            DispatcherServlet.frontController(request, response);

            writeResponse(dos, response);
        }
    }

    private void checkConnection(HttpRequest request) {
        if(request.findHeader("connection").equals("close")){
            close = true;
        }
    }

    private void close(Socket connection) {
        try {
            connection.close();
        } catch (IOException e) {
            // ignore
        }
    }

    private void writeResponse(DataOutputStream dos, HttpResponse response) {
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
