package com.example.util;

import java.util.List;

public class HttpVersion {
    private static final List<String> versionList = List.of("HTTP/1.1", "HTTP/2.0", "HTTP/3.0");
    private static final List<Boolean> supportList = List.of(true, false, false);

    private HttpVersion() {
    }

    public static Boolean isSupported(String version){
        try{
            int index = versionList.indexOf(version);
            assert index >= 0 && index < versionList.size();

            return supportList.get(index);
        } catch (NullPointerException e){
            throw new IllegalArgumentException("Invalid Version");
        }
    }
}
