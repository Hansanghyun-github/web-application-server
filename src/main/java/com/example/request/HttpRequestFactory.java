package com.example.request;

import com.example.util.HttpRequestUtils;
import com.example.util.IOUtils;
import com.example.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpRequestFactory {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private HttpRequestFactory() {
    }

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        HttpRequest request = new HttpRequest();
        parseStartLine(reader, request);
        parseHeader(reader, request);
        if(hasMessageBody(request))
            parseMessageBody(reader, request);
        return request;
    }

    private static void parseStartLine(BufferedReader reader, HttpRequest request) throws IOException {
        String str = reader.readLine();
        if(str == null || str.isBlank())
            throw new IllegalArgumentException("Request is null");
        if(!startWithHttpMethod(str))
            throw new IllegalArgumentException("Invalid Request");
        request.setStartLine(str);
    }

    private static boolean startWithHttpMethod(String str) {
        List<HttpMethod> collect = Arrays.stream(HttpMethod.values())
                .filter((m) -> str.startsWith(m.toString()))
                .toList();
        assert(collect.size() <= 1);

        return collect.size() == 1;
    }

    private static void parseHeader(BufferedReader reader, HttpRequest request) throws IOException {
        String str;
        while((str = reader.readLine()) != null){
            if(str.isBlank()) break;
            request.addHeader(HttpRequestUtils.parseHeader(str));
        }
    }

    private static boolean hasMessageBody(HttpRequest request) {
        return request.containsHeader("content-length");
    }

    private static void parseMessageBody(BufferedReader reader, HttpRequest request) throws IOException {
        int contentLength = Integer.parseInt(request.findHeader("content-length"));
        char[] body = new char[contentLength];
        reader.read(body, 0, contentLength);

        request.setMessageBody(String.copyValueOf(body));
    }

    private static boolean canReadMoreContent(int remainedContentLength) {
        return remainedContentLength >= 0;
    }
}
