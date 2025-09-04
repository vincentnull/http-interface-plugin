package com.vincentnull.http;

import com.vincentnull.http.client.ApiFoxMockHttpClient;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

/**
 * @author 李飞
 * @since 2025/2/6
 */
@SpringBootTest
class ApiFoxMockHttpClientTest {
    @Resource
    private ApiFoxMockHttpClient apiFoxMockHttpClient;

    @Test
    void sendMock() {
        String result1 = apiFoxMockHttpClient.sendMock("");
        System.out.println(result1);
        Mono<String> getResult = apiFoxMockHttpClient.sendMock2();
        System.out.println(getResult.block());
    }

    @Test
    void testHeader() {
        MDC.put("traceId","123");
        Mono<String> getResult = apiFoxMockHttpClient.sendMock2();
        System.out.println(getResult.block());
    }
}