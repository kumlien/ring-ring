package se.kumliens.ringring.service.elks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.kumliens.ringring.model.VirtualNumber;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElksService {

    private final RestClient elksClient;
    private final ObjectMapper objectMapper;

    public Map<String, String> getAccount(String clientId, String clientSecret) {
        return elksClient.get()
                .uri("/me")
                .headers(h -> h.setBasicAuth(clientId, clientSecret))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .getBody();
    }

    @SneakyThrows
    public List<VirtualNumber> getVirtualNumbers(String clientId, String clientSecret) {
        var response = elksClient.get()
                .uri("/numbers")
                .headers(h -> h.setBasicAuth(clientId, clientSecret))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<JsonNode>() {
                })
                .getBody();
        // Extract the "data" field from the response
        JsonNode dataNode = response.get("data");
        if (dataNode == null || !dataNode.isArray()) {
            throw new IllegalArgumentException("Expected 'data' field to be an array in the response JSON.");
        }
        return objectMapper.convertValue(dataNode, new TypeReference<List<VirtualNumber>>(){});
    }
}
