package se.kumliens.ringring.service.elks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElksService {

    private final RestClient elksClient;

    public Map<String, String> getAccount(String clientId, String clientSecret) {
        return elksClient.get()
                .uri("/me")
                .headers(h -> h.setBasicAuth(clientId, clientSecret))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .getBody();
    }

}
