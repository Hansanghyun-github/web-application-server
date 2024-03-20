package com.example.request;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, HEAD;

    public static HttpMethod lookup(String method){
        try{
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Wrong HttpMethod");
        }
    }
}
