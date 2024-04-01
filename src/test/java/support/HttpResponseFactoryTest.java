package support;

import com.example.response.HttpResponse;
import com.example.response.StatusCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class HttpResponseFactoryTest {
    @Test
    @DisplayName("HttpResponse Parsing Test")
    void httpResponseParsingTest() throws Exception {
        // given
        String s = String.join("\r\n",
                "HTTP/1.1 200 OK");

        // when
        HttpResponse response = HttpResponseFactory.parse(s);

        // then
        assertThat(response.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
    }

    @Test
    @DisplayName("HttpResponse Parsing Test with message body")
    void httpResponseParsingTestWithMessageBody() throws Exception {
        // given
        String body = "Hello World!";
        String s = String.join("\r\n",
                "HTTP/1.1 200 OK",
                "Content-Length: " + body.getBytes().length,
                "",
                body);

        // when
        HttpResponse response = HttpResponseFactory.parse(s);

        // then
        assertThat(response.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
        assertThat(HttpResponseFactory
                .isValidHeader(response,
                        "content-length",
                        "" + body.getBytes().length)).isTrue();
        assertThat(response.getMessageBody()).isEqualTo(body);
    }
}
