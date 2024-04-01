package com.example.webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import com.example.config.ControllerList;
import com.example.controller.Controller;
import com.example.request.HttpMethod;
import com.example.request.HttpRequest;
import com.example.request.HttpRequestFactory;
import com.example.response.HttpResponse;
import com.example.response.StatusCode;
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

            Controller controller = ControllerList.getController(request.getPath());
            if(controller == null){
                FileRequestHandler.handle(request, response);
            }
            else{
                controller.service(request, response);
            }

            writeResponse(dos, response);
        }
    }

    private boolean isFileRequest(HttpRequest request) {
        return request.getMethod().equals(HttpMethod.GET)
                && request.getPath().contains(".");
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
        try {
            writeStartLine(dos, response);
            writeHeaders(dos, response);
            if(hasMessageBody(response))
                writeMessageBody(dos, response.getMessageBody());
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    private void writeStartLine(DataOutputStream dos, HttpResponse response) throws IOException {
        if(response.getStatusCode() == StatusCode.OK)
            dos.writeBytes(response.getVersion() + " 200 " + response.getStatusCode() + "\r\n");
        else if(response.getStatusCode() == StatusCode.Found)
            dos.writeBytes(response.getVersion() + " 302 " + response.getStatusCode() + "\r\n");
    }

    private boolean hasMessageBody(HttpResponse response) {
        return response.containsHeader("content-length");
    }

    private static BufferedReader getBufferedReader(InputStream in) {
        return new BufferedReader(new InputStreamReader(in));
    }

    private void writeHeaders(DataOutputStream dos, HttpResponse response) throws IOException {
        for(Map.Entry<String, String> e : response.getHeaders().entrySet()){
            dos.writeBytes(e.getKey() + ": " + e.getValue() + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void writeMessageBody(DataOutputStream dos, String body) throws IOException {
        dos.write(body.getBytes(), 0, body.getBytes().length);
        dos.flush();
    }
}
