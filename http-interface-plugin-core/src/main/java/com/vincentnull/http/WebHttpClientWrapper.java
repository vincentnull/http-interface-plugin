package com.vincentnull.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 自定义HttpClient包装类
 * @author 李飞
 * @since 2025/2/6
 */
@Getter
@Setter
public class WebHttpClientWrapper {

    /**
     * webClient构建器
     */
    private WebClient.Builder webClientBuilder;
}
