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

    public HttpResponse(String version) {
        this.version = version;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public void writeTo(BufferedWriter writer) throws IOException {
        String startLine = String.join(" ",
                version,
                "200",
                statusCode.toString());
        writer.write(startLine);
        writer.write("\r\n");
        for(Map.Entry e : headers.entrySet()){
            writer.write(e.getKey() + ": "+e.getValue());
            writer.write("\r\n");
        }
        writer.write("\r\n");

        if(messageBody != null && !messageBody.isBlank()){
            writer.write(messageBody);
        }
    }
}
