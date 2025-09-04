package com.vincentnull.http.config.properties;

import lombok.Data;

/**
 * http请求客户端WebClient配置属性
 *
 * @author zhongshenghua
 * @date 2024/6/20
 */
@Data
public class WebClientProperties {
    /**
     * 最大连接数
     */
    private Integer maxConnections = 200;

    /**
     * 连接最大等待队列大小
     */
    private Integer pendingAcquireMaxCount = 10000;

    /**
     * 获取线程等待超时时间(秒)
     */
    private Integer pendingAcquireTimeout = 30;

    /**
     * tcp连接建立超时时间
     */
    private Integer connectTimeout = 2000;

    /**
     * http读取超时时间
     */
    private Integer readTimeout = 3500;

    /**
     * http写入超时时间
     */
    private Integer writeTimeout = 3500;

    /**
     * 响应超时时间
     */
    private Integer responseTimeout = 3500;

    /**
     * 连接空闲回收时间(秒)
     */
    private Integer maxIdleTime = 30;

    /**
     * 连接寿命回收时间(秒)
     */
    private Integer maxLifeTime = 120;
}
