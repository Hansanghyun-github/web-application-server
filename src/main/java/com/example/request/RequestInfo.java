package com.example.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RequestInfo {
    private String method;
    private String path;
    private Map<String, String> query;
    private String version;

    RequestInfo() {
        query = Map.of();
    }
}
