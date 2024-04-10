package com.example.request;

import com.example.util.HttpRequestUtils;
import com.example.util.HttpVersion;
import com.example.webserver.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class HttpRequestFactory {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private HttpRequestFactory() {
    }

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        String messageBody = null;
        RequestInfo info = parseStartLine(reader);

        Map<String, String> headers = parseHeaders(reader);
        if(hasMessageBody(headers))
            messageBody = parseMessageBody(reader, headers.get("content-length"));

        HttpRequest request = new HttpRequest(
                HttpMethod.lookup(info.getMethod()),
                info.getPath(),
                info.getQuery(),
                info.getVersion(),
                headers,
                messageBody);

        return request;
    }

    private static RequestInfo parseStartLine(BufferedReader reader) throws IOException {
        RequestInfo info = new RequestInfo();
        String line = reader.readLine();
        if(line == null || line.isBlank())
            throw new IllegalArgumentException("Request is null"); // 그냥 의미없는 처리로 보낸다
        if(!startWithHttpMethod(line))
            throw new IllegalArgumentException("Invalid Request"); // 이런 요청을 막아야 한다

        String[] startLine = line.split(" ");
        if(startLine.length != 3)
            throw new IllegalArgumentException("Invalid Http Request");

        info.setMethod(startLine[0]);

        String[] str = startLine[1].split("\\?");
        if(str.length <= 0 || str.length >= 3)
            throw new IllegalArgumentException("Invalid Http Request");

        info.setPath(str[0]);

        if(str.length == 2)
            info.setQuery(HttpRequestUtils.parseQueryString(str[1]));

        info.setVersion(startLine[2]);
        if(HttpVersion.isSupported(info.getVersion()) == false)
            throw new IllegalArgumentException("Not Supported Version");

        return info;
    }

    private static boolean startWithHttpMethod(String str) {
        List<HttpMethod> collect = Arrays.stream(HttpMethod.values())
                .filter((m) -> str.startsWith(m.toString()))
                .toList();
        assert(collect.size() <= 1);

        return collect.size() == 1;
    }

    private static Map<String, String> parseHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String str;
        while((str = reader.readLine()) != null) {
            if(str.isBlank()) break;
            HttpRequestUtils.Pair header = HttpRequestUtils.parseHeader(str);
            headers.put(header.getKey().toLowerCase(), header.getValue());
        }
        return headers;
    }

    private static boolean hasMessageBody(Map<String, String> headers) {
        return headers.containsKey("content-length");
    }

    private static String parseMessageBody(BufferedReader reader, String contentLength) throws IOException {
        int len = Integer.parseInt(contentLength);
        char[] body = new char[len];
        reader.read(body, 0, len);

        return String.copyValueOf(body);
    }

    private static boolean canReadMoreContent(int remainedContentLength) {
        return remainedContentLength >= 0;
    }
}
