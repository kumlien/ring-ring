package se.kumliens.ringring.service.elks;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class ElksConfig {

    @Bean
    public RestClient elksClient(HttpClient httpClient) {

        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
                .baseUrl("https://api.46elks.com/a1/")
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClients.custom()
                .addRequestInterceptorFirst((req, entity, ctx) -> {
                    log.info("Sending request: {} with body {}", req, entity);
                })
                .addResponseInterceptorFirst(((response, entityDetails, httpContext) -> {
                    log.info("Receiving response: {} with body {}", response, entityDetails);
                }))
                .build();

    }
}
