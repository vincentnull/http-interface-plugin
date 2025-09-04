package com.vincentnull.http.config;

import com.vincentnull.http.WebHttpClientWrapper;
import com.vincentnull.http.config.properties.WebClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author 李飞
 * @since 2025/2/6
 */
@Configuration
public class HttpClientConfig {
    @Bean
    public WebHttpClientWrapper webHttpClientWrapper() {
        WebHttpClientWrapper webHttpClientWrapper = new WebHttpClientWrapper();
        webHttpClientWrapper.setWebClientBuilder(httpClientBuilder(new WebClientProperties()));
        return webHttpClientWrapper;
    }
    private WebClient.Builder httpClientBuilder(WebClientProperties webClientProperties) {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("http-webclient-pool")
                .maxConnections(webClientProperties.getMaxConnections())
                .pendingAcquireMaxCount(webClientProperties.getPendingAcquireMaxCount())
                .pendingAcquireTimeout(Duration.ofSeconds(webClientProperties.getPendingAcquireTimeout()))
                .maxIdleTime(Duration.ofSeconds(webClientProperties.getMaxIdleTime()))
                .maxLifeTime(Duration.ofSeconds(webClientProperties.getMaxLifeTime()))
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .wiretap(true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProperties.getConnectTimeout())
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(webClientProperties.getReadTimeout(), TimeUnit.MILLISECONDS));
                    conn.addHandlerLast(new WriteTimeoutHandler(webClientProperties.getWriteTimeout(), TimeUnit.MILLISECONDS));
                })
                .responseTimeout(Duration.ofMillis(webClientProperties.getResponseTimeout()));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder().filter(traceIdFilter()).clientConnector(connector);
    }
    // 自定义过滤器，动态添加 traceId
    private static ExchangeFilterFunction traceIdFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            ClientRequest filteredRequest = ClientRequest.from(clientRequest)
                    .headers(headers -> getTraceId().ifPresent(traceId -> headers.add("traceId", MDC.get("traceId"))))
                    .build();
            return Mono.just(filteredRequest);
        });
    }
    // 获取当前线程中的 traceId
    public static Optional<String> getTraceId() {
        return Optional.ofNullable(MDC.get("traceId"));
    }
}
