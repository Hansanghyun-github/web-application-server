package com.example.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {
    @Test
    @DisplayName("StartLine Test")
    void startLineTest() throws Exception {
        // given
        String startLine = "GET / HTTP/1.1";
        HttpRequest request = new HttpRequest();

        // when
        request.setStartLine(startLine);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/");
        assertThat(request.getQuery()).isNull();
        assertThat(request.getVersion()).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("StartLine Test with QueryParam")
    void setStartLineWithQueryParam() throws Exception {
        // given
        String startLine = "GET /?name=Han&age=17 HTTP/1.1";
        HttpRequest request = new HttpRequest();

        // when
        request.setStartLine(startLine);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/");
        assertThat(request.getQuery()).isNotNull();
        assertThat(request.getVersion()).isEqualTo("HTTP/1.1");

        Map<String, String> query = request.getQuery();
        assertThat(query.size()).isEqualTo(2);
        assertThat(query.get("name")).isEqualTo("Han");
        assertThat(query.get("age")).isEqualTo("17");
    }

    @Test
    @DisplayName("올바른 start line을 입력해야 한다")
    void setStartLineWithWrongInput() throws Exception {
        // given
        String startLine = " ";
        HttpRequest request = new HttpRequest();

        // when // then
        assertThatThrownBy(() -> request.setStartLine(startLine))
                .isInstanceOf(AssertionError.class);
    }
    
    @Test
    @DisplayName("올바른 http method를 입력해야 한다")
    void setStartLineWithWrongHttpMethod() throws Exception {
        // given
        String startLine = "GETs / HTTP/1.1";
        HttpRequest request = new HttpRequest();

        // when // then
        assertThatThrownBy(() -> request.setStartLine(startLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong HttpMethod");
    }

    @ParameterizedTest
    @CsvSource(value = {"2.0", "3.0"})
    @DisplayName("지원되는 HTTP 버전만 가능하다")
    void setStartLineWithNotSupportedVersion(String version) throws Exception {
        // given
        String startLine = "GET / HTTP/" + version;
        HttpRequest request = new HttpRequest();

        // when // then
        assertThatThrownBy(() -> request.setStartLine(startLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not Supported Version");
    }
}