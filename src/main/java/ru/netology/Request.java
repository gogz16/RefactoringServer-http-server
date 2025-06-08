package ru.netology;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {
    private final String method;
    private final String path;
    private final List<NameValuePair> queryParams;

    public Request(String requestLine) {
        // requestLine, например: "GET /messages?last=10&user=5 HTTP/1.1"
        String[] parts = requestLine.split(" ");
        this.method = parts[0];

        // rawTarget = "/messages?last=10&user=5"
        String rawTarget = parts[1];
        int idx = rawTarget.indexOf('?');
        if (idx >= 0) {
            this.path = rawTarget.substring(0, idx);
            String query = rawTarget.substring(idx + 1);
            // парсим в список NameValuePair
            this.queryParams = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
        } else {
            this.path = rawTarget;
            this.queryParams = List.of();
        }
    }

    /** HTTP-метод, напр. GET или POST */
    public String getMethod() {
        return method;
    }

    /** Путь без параметров, напр. "/messages" */
    public String getPath() {
        return path;
    }

    /**
     * Первый параметр по имени или null, если нет
     */
    public String getQueryParam(String name) {
        return queryParams.stream()
                .filter(p -> p.getName().equals(name))
                .map(NameValuePair::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Все параметры с данным именем (может быть повтор),
     * например при запросе "?value=1&value=2" getQueryParams("value") вернёт ["1","2"]
     */
    public List<String> getQueryParams(String name) {
        return queryParams.stream()
                .filter(p -> p.getName().equals(name))
                .map(NameValuePair::getValue)
                .toList();
    }

    /** Сырой список всех параметров */
    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }
}
