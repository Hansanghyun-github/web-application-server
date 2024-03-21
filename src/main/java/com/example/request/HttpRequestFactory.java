package com.example.request;

import com.example.util.HttpRequestUtils;
import com.example.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpRequestFactory {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private HttpRequestFactory() {
    }

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        HttpRequest request = new HttpRequest();
        String str = reader.readLine();
        if(str == null || str.isBlank())
            throw new IllegalArgumentException("Invalid Http Request");
        request.setStartLine(str);
        parseHeader(reader, request);

        if(hasMessageBody(request))
            parseMessageBody(reader, request);

        return request;
    }

    private static boolean hasMessageBody(HttpRequest request) {
        return request.containsHeader("content-length");
    }

    private static void parseHeader(BufferedReader reader, HttpRequest request) throws IOException {
        String str;
        while((str = reader.readLine()) != null){
            if(str.isBlank()) break;
            request.addHeader(HttpRequestUtils.parseHeader(str));
        }
    }

    private static void parseMessageBody(BufferedReader reader, HttpRequest request) throws IOException {
        int remainLength = Integer.parseInt(request.findHeader("content-length"));
        String str;
        List<String> list = new ArrayList<>();
        while((str = reader.readLine()) != null && remainLength >= 0){
            list.add(str);
            remainLength -= str.getBytes().length;
        }

        request.setMessageBody(String.join("\r\n", list.toArray(new String[0])));
    }
}
