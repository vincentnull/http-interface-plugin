# 项目背景
Spring Cloud 2022 **官方宣布OpenFeign进入维护期**，并建议Spring Interface 进行替代。而Spring Interface 目前直接使用还有许多不方便的地方，所以我参考openfeign源码编写了此插件有助于大大的提高Spring Interface的开发效率，使得你可以像使用openFeign一样使用Spring Interface
# Spring HTTP Interface 使用版本要求

| 项目                | 最低版本要求                     |
|--------------------|--------------------------------|
| Spring Framework   | 6.0.0                          |
| Spring Boot        | 3.0.0                          |
| Java (JDK)         | 17 及以上       |
# 简单入门
1、添加依赖
```xml
<dependency>
    <groupId>io.github.vincentnull</groupId>
    <artifactId>http-interface-plugin-core</artifactId>
    <version>0.0.1</version>
</dependency>
```
2、在启动类上添加@EnableHttpClients注解
```java
@EnableHttpClients
```
3、创建http client接口

java代码
```java
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
```
yml配置
```yaml
http:
  host: https://m1.apifoxmock.com
```
4、注入client接口
```java
@SpringBootTest
class ApiFoxMockHttpClientTest {
    @Resource
    private ApiFoxMockHttpClient apiFoxMockHttpClient;

    @Test
    void sendMock() {
        String result1 = apiFoxMockHttpClient.sendMock("");
        System.out.println(result1); // {"code":200,"msg":"POST"}
        Mono<String> getResult = apiFoxMockHttpClient.sendMock2();
        System.out.println(getResult.block()); // {"code":200,"msg":"get"}
    }
}
```
5、自定义配置webclient
如果有自定义配置web client的需求,如传递请求头信息（traceId）、拦截器、统一异常处理等
可参考项目中[http-interface-plugin-test]模块com.vincentnull.http.config.HttpClientConfig进行配置


* 如果对您有帮助，请给个star支持一下，欢迎提issue或PR参与贡献

# 附件：
openfeign 官方文档：https://github.com/spring-cloud/spring-cloud-openfeign

spring interface 官方文档：https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface