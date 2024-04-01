package support;

import com.example.request.HttpRequestFactory;
import com.example.response.HttpResponse;
import com.example.response.StatusCode;
import com.example.util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpResponseFactory {
    private HttpResponseFactory() {
    }

    public static HttpResponse parse(String output) throws IOException {
        BufferedReader reader = getBufferedReader(output);
        HttpResponse response = new HttpResponse();

        parseStartLine(reader, response);
        parseHeader(reader, response);
        if (hasMessageBody(response))
            parseMessageBody(reader, response);

        return response;
    }

    private static BufferedReader getBufferedReader(String output) {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(output.getBytes())));
    }

    private static void parseStartLine(BufferedReader reader, HttpResponse response) throws IOException {
        String str = reader.readLine();
        if (str == null || str.isBlank())
            throw new IllegalArgumentException("Invalid response");
        setStartLine(response, str);
    }

    private static void setStartLine(HttpResponse response, String str) {
        String[] strings = str.split(" ");
        response.setVersion(strings[0]);
        response.setStatusCode(StatusCode.valueOf(strings[2]));
    }

    private static void parseHeader(BufferedReader reader, HttpResponse response) throws IOException {
        String str;
        while ((str = reader.readLine()) != null) {
            if (str.isBlank()) break;
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(str);
            response.addHeader(
                    pair.getKey().toString(),
                    pair.getValue().toString());
        }
    }

    private static boolean hasMessageBody(HttpResponse response) {
        return response.containsHeader("content-length");
    }

    private static void parseMessageBody(BufferedReader reader, HttpResponse response) throws IOException {
        int remainedContentLength = Integer.parseInt(response.findHeader("content-length"));
        String str;
        List<String> list = new ArrayList<>();
        while ((str = reader.readLine()) != null && canReadMoreContent(remainedContentLength)) {
            list.add(str);
            remainedContentLength -= str.getBytes().length;
        }

        response.setMessageBody(String.join("\r\n", list.toArray(new String[0])));
    }

    private static boolean canReadMoreContent(int remainedContentLength) {
        return remainedContentLength >= 0;
    }
}