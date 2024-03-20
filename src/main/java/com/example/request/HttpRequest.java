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
        assert(startLine.length == 3);

        setMethod(startLine[0]);

        String[] str = startLine[1].split("\\?");
        assert(0 < str.length && str.length < 3);
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
        headers.put(pair.getKey(), pair.getValue());
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
