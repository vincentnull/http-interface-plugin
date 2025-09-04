package com.vincentnull.http.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

/**
 * 这个apifox的云端Mock接口
 * @author 李飞
 * @since 2025/2/6
 */
@HttpExchange("{http.host}/m1/6197711-5890682-default")
public interface ApiFoxMockHttpClient {

    /**
     * 发送POST mock请求
     * @param apifoxToken
     * @return
     */
    @PostExchange(url = "/mockPost")
    String sendMock(@RequestParam("apifoxToken") String apifoxToken);

    /**
     * 发送GET mock请求
     * @return
     */
    @GetExchange(url = "/mockGet")
    Mono<String> sendMock2();
}
