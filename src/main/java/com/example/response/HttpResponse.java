package com.example.response;

import lombok.Getter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpResponse {
    private String version;
    private StatusCode statusCode;
    private final Map<String, String> headers = new HashMap<>();
    private String messageBody;

    public HttpResponse() {
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void addHeader(String key, String value){
        headers.put(key.toLowerCase(), value);
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
        String startLine = String.join(" ",
                version,
                "200",
                statusCode.toString());
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
