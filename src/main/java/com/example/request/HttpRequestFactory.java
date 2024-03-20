package com.example.request;

import com.example.util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestFactory {
    private HttpRequestFactory() {
    }

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        HttpRequest request = new HttpRequest();
        String str = reader.readLine();
        if(str == null || str.isBlank())
            throw new IllegalArgumentException("Invalid Http Request");
        request.setStartLine(str);
        parseHeader(reader, request);
        parseMessageBody(reader, request);

        return request;
    }

    private static void parseHeader(BufferedReader reader, HttpRequest request) throws IOException {
        String str;
        while((str = reader.readLine()) != null){
            if(str.isBlank()) break;
            request.addHeader(HttpRequestUtils.parseHeader(str));
        }
    }

    private static void parseMessageBody(BufferedReader reader, HttpRequest request) throws IOException {
        String str;
        List<String> list = new ArrayList<>();
        while((str = reader.readLine()) != null){
            list.add(str);
        }

        request.setMessageBody(String.join("\r\n", list.toArray(new String[0])));
    }
}
