package com.example.request;

import com.example.util.HttpRequestUtils;
import com.example.util.HttpVersion;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequest {
    // TODO 불변 객체로
    private HttpMethod method;
    private String path;
    private Map<String, String> query;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private String messageBody;

    HttpRequest() {
    }

    public void setStartLine(String line){
        String[] startLine = line.split(" ");
        if(startLine.length != 3)
            throw new IllegalArgumentException("Invalid Http Request");

        setMethod(startLine[0]);

        String[] str = startLine[1].split("\\?");
        if(str.length <= 0 || str.length >= 3)
            throw new IllegalArgumentException("Invalid Http Request");
        setPath(str[0]);

        if(str.length == 2)
            query = HttpRequestUtils.parseQueryString(str[1]);

        String version = startLine[2];
        if(HttpVersion.isSupported(version) == false)
            throw new IllegalArgumentException("Not Supported Version");

        setVersion(version);
    }

    private void setVersion(String version) {
        this.version = version;
    }

    private void setPath(String path) {
        this.path = path;
    }

    private void setMethod(String method) {
        this.method = HttpMethod.lookup(method);
    }

    public void addHeader(HttpRequestUtils.Pair pair){
        headers.put(pair.getKey().toLowerCase(), pair.getValue());
    }

    public boolean containsHeader(String key){
        return headers.containsKey(key);
    }

    public String findHeader(String key){
        return headers.get(key);
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
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
