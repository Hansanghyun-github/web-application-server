package com.example.request;

import com.example.util.HttpRequestUtils;
import com.example.util.HttpVersion;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final Map<String, String> query;
    private final String version;
    private final Map<String, String> headers;
    private final String messageBody;

    HttpRequest(HttpMethod method, String path, Map<String, String> query, String version, Map<String, String> headers, String messageBody) {
        this.method = method;
        this.path = path;
        this.query = Map.copyOf(query);
        this.version = version;
        this.headers = Map.copyOf(headers);
        this.messageBody = messageBody;
    }

    public boolean containsHeader(String key){
        return headers.containsKey(key);
    }

    public String findHeader(String key){
        return headers.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String startLine = String.join(" ", method.toString(),
                path, version);
        sb.append(startLine);
        sb.append("\r\n");
        for(Map.Entry e : headers.entrySet()){
            sb.append(e.getKey() + ": "+e.getValue());
            sb.append("\r\n");
        }
        sb.append("\r\n");

        if(messageBody != null && !messageBody.isBlank()){
            sb.append(messageBody);
        }

        return sb.toString();
    }
}
